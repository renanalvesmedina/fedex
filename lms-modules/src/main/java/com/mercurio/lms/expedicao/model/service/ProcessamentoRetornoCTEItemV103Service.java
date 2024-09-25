package com.mercurio.lms.expedicao.model.service;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xerces.jaxp.DocumentBuilderImpl;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.InputSource;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.entrega.model.service.RegistrarBaixaEntregaPorNotaFiscalService;
import com.mercurio.lms.expedicao.model.dao.TbDatabaseInputCTEDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.sim.model.MonitoramentoCCT;
import com.mercurio.lms.sim.model.service.MonitoramentoNotasFiscaisCCTService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.integracaoNDDigitalService"
 */
public class ProcessamentoRetornoCTEItemV103Service {

	private Logger log = LogManager.getLogger(this.getClass());
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private PessoaService pessoaService;
	private ConhecimentoService conhecimentoService;
    private MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConfiguracoesFacade configuracoesFacade;
	private TbDatabaseInputCTEDAO tbDatabaseInputCTEDAO;
	private IntegracaoJmsService integracaoJmsService;
	private MonitoramentoDescargaService monitoramentoDescargaService;
	private BatchLogger batchLogger;
	private NotaFiscalOperadaService notaFiscalOperadaService;
	private RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService;
	private WorkflowPendenciaService workflowPendenciaService;
	private UnidadeFederativaService unidadeFederativaService;
	private DoctoServicoService doctoServicoService;
	private FilialService filialService;

	@SuppressWarnings("rawtypes")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeLeituraRetornoInutilizacao(final ConnectorIntegrationCTE connectorIntegrationCTE, String xml) {
		StringReader sr = null;
		try {
			sr = new StringReader(xml);

			if(xml.contains("retInutCTe")){
				DocumentBuilderFactoryImpl dbf = (DocumentBuilderFactoryImpl)DocumentBuilderFactoryImpl.newInstance();

				DocumentBuilderImpl docBuilder = (DocumentBuilderImpl)dbf.newDocumentBuilder();

				DocumentImpl doc = (DocumentImpl) docBuilder.parse(new InputSource(sr));

				ElementImpl docElement = (ElementImpl)doc.getDocumentElement();

				ElementImpl infInut = (ElementImpl) docElement.getElementsByTagName("infInut").item(0);
				ElementImpl nProt = (ElementImpl) infInut.getElementsByTagName("nProt").item(0);
				ElementImpl xMotivo = (ElementImpl) infInut.getElementsByTagName("xMotivo").item(0);
				ElementImpl cNPJ = (ElementImpl) infInut.getElementsByTagName("CNPJ").item(0);
				ElementImpl nCTIni = (ElementImpl) infInut.getElementsByTagName("nCTIni").item(0);

				Pessoa filialOrigem = pessoaService.findByNrIdentificacao(cNPJ.getTextContent());

				List<Conhecimento> conhecimentoList = conhecimentoService.findByNrConhecimentoByFilial(
						Long.valueOf(nCTIni.getTextContent()), filialOrigem.getIdPessoa(), "CTE");

				if(conhecimentoList != null && !conhecimentoList.isEmpty()){
					Conhecimento conh = conhecimentoList.get(0);

					MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(conh.getIdDoctoServico());
					if(monitoramentoDocEletronico != null){
						if(nProt.getTextContent() == null || Long.valueOf(nProt.getTextContent()) == 0){

							BigDecimal qtdeReenviosCte = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.QTDE_REENVIOS_CTE);

							if(monitoramentoDocEletronico.getNrEnvios()==null || monitoramentoDocEletronico.getNrEnvios().longValue() < qtdeReenviosCte.longValue() ){
								monitoramentoDocEletronico.setNrEnvios(monitoramentoDocEletronico.getNrEnvios()!=null?monitoramentoDocEletronico.getNrEnvios()+1:1);
								DetachedCriteria dc = DetachedCriteria.forClass(TBDatabaseInputCTE.class);
								dc.add(Restrictions.eq("id", monitoramentoDocEletronico.getIdEnvioDocEletronicoC()));
								List findByDetachedCriteria = tbDatabaseInputCTEDAO.findByDetachedCriteria(dc);
								if(findByDetachedCriteria!=null && !findByDetachedCriteria.isEmpty()){
									TBDatabaseInputCTE tBDatabaseInputCTE = (TBDatabaseInputCTE) findByDetachedCriteria.get(0);
									tBDatabaseInputCTE.setStatus(0);
									tbDatabaseInputCTEDAO.store(tBDatabaseInputCTE);
								}
								monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("J"));
							}else if(monitoramentoDocEletronico.getNrEnvios().longValue() == qtdeReenviosCte.longValue()){
								enviarEmail(monitoramentoDocEletronico);
								monitoramentoDocEletronico.setNrEnvios(monitoramentoDocEletronico.getNrEnvios()!=null?monitoramentoDocEletronico.getNrEnvios()+1:1);
							}

						} else {
							monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("I"));
							monitoramentoDocEletronico.setNrProtocolo(Long.valueOf(nProt.getTextContent()));
						}

						//LMS-4048
						List<MonitoramentoDescarga> monitoramentosDescarga = monitoramentoDescargaService.findByIdConhecimento(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
						if (monitoramentosDescarga != null  && monitoramentosDescarga.size() > 0) {
							MonitoramentoDescarga monitoramentoDescarga = monitoramentosDescarga.get(0);
							monitoramentoDescargaService.updateSituacaoMonitoramentoByIdMeioTransporte(monitoramentoDescarga.getIdMonitoramentoDescarga(), monitoramentoDescarga.getFilial().getIdFilial());
						}

						monitoramentoDocEletronico.setDsObservacao(xMotivo.getTextContent());

						monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);

						integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());
					}

					// LMS-3252
					MonitoramentoCCT monitoramentoCCT = null;

					for (NotaFiscalConhecimento nfc : conh.getNotaFiscalConhecimentos()) {
						monitoramentoCCT = this.monitoramentoNotasFiscaisCCTService.findMonitoramentoCCTByNrChave(nfc.getNrChave());

						if (monitoramentoCCT != null) {
							this.monitoramentoNotasFiscaisCCTService.storeEvento("CA", nfc.getNrChave(), null, null,
									conh, null, null, SessionUtils.getUsuarioLogado());
						}
					}
				}

			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			if(sr != null){
				sr.close();
			}
		}
	}

	private void enviarEmail(MonitoramentoDocEletronico monitoramentoDocEletronico) {
		String from = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		String cte = monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial() + " " +
		monitoramentoDocEletronico.getDoctoServico().getNrDoctoServico();

		Mail mail = this.createMail(
			SessionUtils.getUsuarioLogado().getDsEmail(),
			from,
			"Rejeicao de Inutilizacao do CT-e "+cte,
			"O CT-e "+cte+" teve o sua Inutilizacao rejeitada pela SEFAZ, favor verificar."
		);

		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}

    @SuppressWarnings({"rawtypes", "unchecked"})
	@Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeLeituraRetornoCancelamento(final ConnectorIntegrationCTE connectorIntegrationCTE, String xml) {
    	StringReader sr = null;
    	try {
    		sr = new StringReader(xml);
    		if(xml.contains("retCancCTe")){

    			DocumentBuilderFactoryImpl dbf = (DocumentBuilderFactoryImpl)DocumentBuilderFactoryImpl.newInstance();

    			DocumentBuilderImpl docBuilder = (DocumentBuilderImpl)dbf.newDocumentBuilder();

    			DocumentImpl doc = (DocumentImpl)docBuilder.parse(new InputSource(sr));

    			ElementImpl docElement = (ElementImpl) doc.getDocumentElement();

    			ElementImpl infCanc = (ElementImpl) docElement.getElementsByTagName("infCanc").item(0);

    			ElementImpl chCTe = (ElementImpl) infCanc.getElementsByTagName("chCTe").item(0);
    			ElementImpl nProt = (ElementImpl) infCanc.getElementsByTagName("nProt").item(0);
    			ElementImpl xMotivo = (ElementImpl) infCanc.getElementsByTagName("xMotivo").item(0);

    			String chave = chCTe.getTextContent();

    			MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findByNrChave(chave);
    			if(monitoramentoDocEletronico != null) {
    				if(nProt == null || (nProt.getTextContent() == null || Long.valueOf(nProt.getTextContent()) == 0)){

    					BigDecimal qtdeReenviosCte = (BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.QTDE_REENVIOS_CTE);

    					if(monitoramentoDocEletronico.getNrEnvios()==null || monitoramentoDocEletronico.getNrEnvios() < qtdeReenviosCte.longValue() ){
    						monitoramentoDocEletronico.setNrEnvios(monitoramentoDocEletronico.getNrEnvios()!=null?monitoramentoDocEletronico.getNrEnvios()+1:1);
    						DetachedCriteria dc = DetachedCriteria.forClass(TBDatabaseInputCTE.class);
    						dc.add(Restrictions.eq("id", monitoramentoDocEletronico.getIdEnvioDocEletronicoC()));
    						List findByDetachedCriteria = tbDatabaseInputCTEDAO.findByDetachedCriteria(dc);
    						if(findByDetachedCriteria!=null && !findByDetachedCriteria.isEmpty()){
    							TBDatabaseInputCTE tBDatabaseInputCTE = (TBDatabaseInputCTE) findByDetachedCriteria.get(0);
    							tBDatabaseInputCTE.setStatus(0);
    							tbDatabaseInputCTEDAO.store(tBDatabaseInputCTE);
    						}
    						monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("D"));
    					}else if(monitoramentoDocEletronico.getNrEnvios().longValue() == qtdeReenviosCte.longValue()){
     						String from = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
    						String to = (String) configuracoesFacade.getValorParametro("EMAIL_REJEICAO_CTE");
    						String cte = monitoramentoDocEletronico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial()
    								+ " " + monitoramentoDocEletronico.getDoctoServico().getNrDoctoServico();

    						Mail mail = this.createMail(
								to,
								from,
								"Rejeicao de Cancelamento do CT-e "+cte+".",
								"O CT-e "+cte+" teve o seu cancelamento rejeitado pela SEFAZ, favor verificar."
							);

    						JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
    						integracaoJmsService.storeMessage(msg);

    						monitoramentoDocEletronico.setNrEnvios(monitoramentoDocEletronico.getNrEnvios()!=null?monitoramentoDocEletronico.getNrEnvios()+1:1);
    					}

    				} else {
    					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("C"));
    					monitoramentoDocEletronico.setNrProtocolo(Long.valueOf(nProt.getTextContent()));
    				}

					//LMS-4048
					List<MonitoramentoDescarga> monitoramentosDescarga = monitoramentoDescargaService.findByIdConhecimento(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
					if (monitoramentosDescarga != null && monitoramentosDescarga.size() > 0) {
						MonitoramentoDescarga monitoramentoDescarga = monitoramentosDescarga.get(0);
						monitoramentoDescargaService.updateSituacaoMonitoramentoByIdMeioTransporte(monitoramentoDescarga.getIdMonitoramentoDescarga(), monitoramentoDescarga.getFilial().getIdFilial());
					}

    				monitoramentoDocEletronico.setDsObservacao(xMotivo.getTextContent());

    				monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);

    				integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());
    			}
    		}

    	} catch (Exception e) {
    		log.error(e);
    	} finally {
    		if(sr != null){
    			sr.close();
    		}
    	}
    }

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeLeituraRetornoEmissao(final ConnectorIntegrationCTE connectorIntegrationCTE, String xml){
		StringReader sr = null;
		try {
			sr = new StringReader(xml);
			if(xml.contains("infProt")){
				sr = new StringReader(xml);

				DocumentBuilderFactoryImpl dbf = (DocumentBuilderFactoryImpl)DocumentBuilderFactoryImpl.newInstance();
				DocumentBuilderImpl docBuilder = (DocumentBuilderImpl)dbf.newDocumentBuilder();

				DocumentImpl doc = (DocumentImpl)docBuilder.parse(new InputSource(sr));

				ElementImpl docElement = (ElementImpl)doc.getDocumentElement();

				ElementImpl chCTe = (ElementImpl) docElement.getElementsByTagName("chCTe").item(0);
				ElementImpl nProt = (ElementImpl) docElement.getElementsByTagName("nProt").item(0);
				ElementImpl xMotivo = (ElementImpl) docElement.getElementsByTagName("xMotivo").item(0);

				String chave = chCTe.getTextContent();

				MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findByNrChave(chave);
				if(monitoramentoDocEletronico != null){


					if(nProt.getTextContent() != null && Long.valueOf(nProt.getTextContent()) != 0){
					    // LMS-3252
					    List<NotaFiscalConhecimento> nfcs = notaFiscalConhecimentoService.findByConhecimento(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
					    MonitoramentoCCT monitoramentoCCT = null;

					    for (NotaFiscalConhecimento nfc : nfcs) {
					        monitoramentoCCT = this.monitoramentoNotasFiscaisCCTService.findMonitoramentoCCTByNrChave(nfc.getNrChave());

					        if (monitoramentoCCT != null) {
					            this.monitoramentoNotasFiscaisCCTService.storeEvento("EM", nfc.getNrChave(), null, null,monitoramentoDocEletronico.getDoctoServico(),
										null, null, SessionUtils.getUsuarioLogado());
					        }
					    }
						monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("A"));
						monitoramentoDocEletronico.setNrProtocolo(Long.valueOf(nProt.getTextContent()));
						monitoramentoDocEletronico.setDsObservacao(xMotivo.getTextContent());
						conhecimentoService.executeEmissaoCTEAutorizado(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());

						BigDecimal vlImpostoDifal = monitoramentoDocEletronico.getDoctoServico().getVlImpostoDifal();

						if(vlImpostoDifal != null && vlImpostoDifal.compareTo(new BigDecimal(0)) > 0) {

							DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();

							String sgUnidadeFederativa = this.unidadeFederativaService.findSgUnidadeFederativaByIdDoctoServico(doctoServico.getIdDoctoServico());
							String sgFilial = this.filialService.findSgFilialByIdDoctoServico(doctoServico.getIdDoctoServico());

							String dsProcesso = this.configuracoesFacade.getMensagem("LMS-23050", new Object[] {
									sgFilial,
									StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), 0),
									sgUnidadeFederativa,
									FormatUtils.formatDecimal("#,##0.00", doctoServico.getVlImpostoDifal(), true)
							});

							Pendencia pendencia = this.workflowPendenciaService.generatePendencia(
									doctoServico.getFilialByIdFilialOrigem().getIdFilial(),
									Short.valueOf(ConstantesWorkflow.NR5001_SOLICITACAO_PAGAMENTO_GNRE),
									doctoServico.getIdDoctoServico(), dsProcesso, JTDateTimeUtils.getDataHoraAtual(),
									SessionUtils.getUsuarioLogado(), SessionUtils.getEmpresaSessao().getIdEmpresa());

							doctoServico.setPendencia(pendencia);
							doctoServico.setTpSituacaoPendencia(new DomainValue(ConstantesWorkflow.EM_APROVACAO));

							this.doctoServicoService.store(doctoServico);

						}

					}else if("E".equals(monitoramentoDocEletronico.getTpSituacaoDocumento().getValue())
							&& (nProt.getTextContent() == null || Long.valueOf(nProt.getTextContent()) == 0)){
						monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("R"));
						monitoramentoDocEletronico.setDsObservacao(xMotivo.getTextContent());
					}

					//LMS-4048
					List<MonitoramentoDescarga> monitoramentosDescarga = monitoramentoDescargaService.findByIdConhecimento(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico());
					if (monitoramentosDescarga != null && monitoramentosDescarga.size() > 0) {
						MonitoramentoDescarga monitoramentoDescarga = monitoramentosDescarga.get(0);
						boolean validateUpdateSituacaoMonitoramento = monitoramentoDescargaService.validateUpdateSituacaoMonitoramento(monitoramentoDescarga.getIdMonitoramentoDescarga());
						if(validateUpdateSituacaoMonitoramento){
							monitoramentoDescargaService.updateSituacaoMonitoramentoByIdMeioTransporte(monitoramentoDescarga.getIdMonitoramentoDescarga(), monitoramentoDescarga.getFilial().getIdFilial());
						}
					}

					if (monitoramentoDocEletronico.getDsEnvioEmail() != null ) {
						monitoramentoDocEletronico.setBlEnviadoCliente(Boolean.TRUE);
						monitoramentoDocEletronico.setDhEnvio(new DateTime());
					}

					monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);

					integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());
					if(monitoramentoDocEletronico.getDoctoServico().getDoctoServicoOriginal()!= null){
					    finalizacaoCteOriginal(monitoramentoDocEletronico.getDoctoServico().getIdDoctoServico(),
					            monitoramentoDocEletronico.getDoctoServico().getDoctoServicoOriginal().getIdDoctoServico());
					}
				}
			}
		} catch (Exception e) {
			batchLogger.warning("ConnectorIntegrationCTEID:"+connectorIntegrationCTE.getConnectorIntegrationCTEID(), e);
		} finally {
			if(sr != null){
				sr.close();
			}
		}

	}

	private void finalizacaoCteOriginal(Long idDoctoServico, Long idDoctoServicoOriginal) {
		if (CollectionUtils.isNotEmpty(notaFiscalOperadaService.findByIdDoctoServico(idDoctoServico))) {
			registrarBaixaEntregaPorNotaFiscalService.executeFinalizacaoCteOriginal(idDoctoServicoOriginal, SessionUtils.getFilialSessao(), SessionUtils.getUsuarioLogado());
		}
	}

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

    public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
    }

	public void setTbDatabaseInputCTEDAO(TbDatabaseInputCTEDAO tbDatabaseInputCTEDAO) {
		this.tbDatabaseInputCTEDAO = tbDatabaseInputCTEDAO;
    }

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setNotaFiscalConhecimentoService(
			NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setMonitoramentoNotasFiscaisCCTService(
			MonitoramentoNotasFiscaisCCTService monitoramentoNotasFiscaisCCTService) {
		this.monitoramentoNotasFiscaisCCTService = monitoramentoNotasFiscaisCCTService;
	}

	public void setMonitoramentoDescargaService(
			MonitoramentoDescargaService monitoramentoDescargaService) {
		this.monitoramentoDescargaService = monitoramentoDescargaService;
	}

	public void setBatchLogger(BatchLogger batchLogger){
		batchLogger.logClass(this.getClass());
		this.batchLogger = batchLogger;
	}

	public void setNotaFiscalOperadaService(
			NotaFiscalOperadaService notaFiscalOperadaService) {
		this.notaFiscalOperadaService = notaFiscalOperadaService;
	}

	public void setRegistrarBaixaEntregaPorNotaFiscalService(
			RegistrarBaixaEntregaPorNotaFiscalService registrarBaixaEntregaPorNotaFiscalService) {
		this.registrarBaixaEntregaPorNotaFiscalService = registrarBaixaEntregaPorNotaFiscalService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}
