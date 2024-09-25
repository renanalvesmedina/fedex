package com.mercurio.lms.vendas.model.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
import br.com.tntbrasil.integracao.domains.mail.MailAttachment;
import br.com.tntbrasil.integracao.domains.vendas.CTeDadosEnvioDMN;
import br.com.tntbrasil.integracao.domains.vendas.CTeUpdateDMN;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.batch.log.BatchLogger;
import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.model.ClienteLayoutEDI;
import com.mercurio.lms.edi.model.service.ClienteLayoutEDIService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.GerarConhecimentoEletronicoXMLService;
import com.mercurio.lms.expedicao.model.service.IntegracaoNDDigitalService;
import com.mercurio.lms.expedicao.model.service.MonitoramentoDocEletronicoService;
import com.mercurio.lms.expedicao.reports.JRRemoteReportsRunner;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.TemplateRelatorio;
import com.mercurio.lms.sim.model.service.TemplateRelatorioService;
import com.mercurio.lms.util.FileUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.vendas.model.AgrupadorCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.EnvioCteCliente;
import com.mercurio.lms.vendas.model.dao.EnvioCteClienteDAO;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.vendas.manterParametrizacaoEnvioService"
 */
@Assynchronous
@ServiceSecurity
public class ManterParametrizacaoEnvioService extends
		CrudService<EnvioCteCliente, Long> {

	private static final String TP_CONTATO_CTE = "CT";
	private static final String TP_ENVIO_POR_EMAIL = "E";
	private static final Long ID_EMPRESA_MATRIZ = 361L;
	private static final String TEXT_HTML= "text/html; charset='utf-8'";
	
	private DomainValueService domainValueService;
	private EnvioCteClienteDAO envioCteClienteDAO;
	private ClienteLayoutEDIService clienteLayoutEDIService;
	private ConfiguracoesFacade configuracoesFacade;
	private BatchLogger batchLogger;
	private DoctoServicoService doctoServicoService;
	private ContatoService contatoService;
	private PessoaService pessoaService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private ClienteService clienteService;
	private DevedorDocServService devedorDocServService;
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoService;
	private TemplateRelatorioService templateRelatorioService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private ParametroGeralService parametroGeralService;
	private ConhecimentoService conhecimentoService;
	private FilialService filialService;
	private IntegracaoJmsService integracaoJmsService;
	
	private Logger log = LogManager.getLogger(ManterParametrizacaoEnvioService.class);
	
	private static final int TAMANHO_CNPJ = 14;
	private static final int TAMANHO_CPF = 11;
	private static final String EMAIL_FALHA_ENVIO_EMAIL = "EMAIL_SUPORTE_VENDAS";
	
	@Override
	public EnvioCteCliente findById(Long id) {
		return (EnvioCteCliente) super.findById(id);
	}

	public Map<String, Object> findByIdCustom(java.lang.Long id) {
		Map<String, Object> data = new HashMap<String, Object>();

		EnvioCteCliente bean = (EnvioCteCliente) super.findById(id);

		data.put("idEnvioCteCliente", bean.getIdEnvioCteCliente());
		data.put("idClienteRemetente", bean.getCliente().getIdCliente());
		data.put("nrIdentificacaoRemetente",
				FormatUtils.formatIdentificacao(bean.getCliente().getPessoa()
						.getTpIdentificacao(), bean.getCliente().getPessoa()
						.getNrIdentificacao()));
		data.put("nmPessoaRemetente", bean.getCliente().getPessoa().getNmPessoa());

		data.put("tpParametrizacao", bean.getTpParametrizacao().getValue());
		data.put("tpEnvio", bean.getTpEnvio().getValue());

		data.put("blEnviaPdf", bean.getBlEnviaPdf());
		data.put("blEnviaXml", bean.getBlEnviaXml());
		data.put("blEnviaNfe", bean.getBlEnviaNfe());
		data.put("blAgendamentoAutomatico", bean.getBlAgendamentoAutomatico());
		data.put("blConfirmaAgendamento", bean.getBlConfirmaAgendamento());

		data.put("dsAssunto", bean.getDsAssunto());
		data.put("blChaveAssunto", bean.getBlChaveAssunto());
		data.put("blEspacoAssunto", bean.getBlEspacoAssunto());

		data.put("nmPdf", bean.getNmPdf());
		data.put("blChavePdf", bean.getBlChavePdf());
		data.put("blEspacoPdf", bean.getBlEspacoPdf());

		data.put("nmXml", bean.getNmXml());
		data.put("blChaveXml", bean.getBlChaveXml());
		data.put("blEspacoXml", bean.getBlEspacoXml());

		data.put("dsTextoEmail", bean.getDsTextoEmail());
		
		data.put("dtPeriodoInicial", bean.getDtPeriodoInicial());
		data.put("dtPeriodoFinal", bean.getDtPeriodoFinal());

		if(bean.getTemplateRelatorio() != null){
			data.put("idTemplate", bean.getTemplateRelatorio().getIdTemplate());
			data.put("nmTemplate", bean.getTemplateRelatorio().getNmTemplate());
		}
		
		return data;
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		EnvioCteCliente envioCteCliente = this.findById(id);

		if ("C".equals(envioCteCliente.getTpParametrizacao().getValue())
				&& "O".equals(envioCteCliente.getTpEnvio().getValue())) {
			String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
			String strEmails = (String) configuracoesFacade.getValorParametro("EMAIL_PARAMETRIZACAO_ENVIO");
			String strSubject = (String) configuracoesFacade.getValorParametro("ASSUNTO_PARAMETRIZACAO_ENVIO");

			String text = (String) configuracoesFacade.getValorParametro("TEXTO_EXCLUI_PARAMETRIZACAO_ENVIO");
			text = text.replace("{0}", envioCteCliente.getTpParametrizacao().getDescriptionAsString());
			text = text.replace(
					"{1}",
					FormatUtils.formatIdentificacao(envioCteCliente
							.getCliente().getPessoa())
							+ " "
							+ envioCteCliente.getCliente().getPessoa()
									.getNmPessoa());

			sendEmail(strEmails, strSubject, strFrom, text);
		}
		
		if("F".equals(envioCteCliente.getTpParametrizacao().getValue()) || "R".equals(envioCteCliente.getTpParametrizacao().getValue()) || "D".equals(envioCteCliente.getTpParametrizacao().getValue())){
			removeAgrupadorClienteByIdParametrizacaoEnvio(id);
		}

		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(final List<Long> ids) {
		for (final Long id : ids) {
			EnvioCteCliente envioCteCliente = this.findById(id);

			if ("C".equals(envioCteCliente.getTpParametrizacao()
							.getValue())
					&& "O".equals(envioCteCliente.getTpEnvio().getValue())) {
				String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
				String strEmails = (String) configuracoesFacade.getValorParametro("EMAIL_PARAMETRIZACAO_ENVIO");
				String strSubject = (String) configuracoesFacade.getValorParametro("ASSUNTO_PARAMETRIZACAO_ENVIO");

				String text = (String) configuracoesFacade.getValorParametro("TEXTO_INCLUI_PARAMETRIZACAO_ENVIO");

				if (text != null) {
					text = text.replace("{0}", envioCteCliente.getTpParametrizacao().getDescriptionAsString());

					text = text.replace("{1}", envioCteCliente.getCliente()
							.getPessoa().getNrIdentificacaoFormatado()
							+ envioCteCliente.getCliente().getPessoa()
									.getNmPessoa());
				}

				sendEmail(strEmails, strSubject, strFrom, text);
			}
			
			if("F".equals(envioCteCliente.getTpParametrizacao().getValue()) || "R".equals(envioCteCliente.getTpParametrizacao().getValue()) || "D".equals(envioCteCliente.getTpParametrizacao().getValue())){
				removeAgrupadorClienteByIdParametrizacaoEnvio(id);
			}
			
			this.removeById(id);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(Map data) {
		String tpParametrizacao = (String) data.get("tpParametrizacao");
		
		if ("C".equals(tpParametrizacao) 
				&& Boolean.FALSE.equals(data.get("blEnviaXml"))
				&& Boolean.FALSE.equals(data.get("blEnviaNfe"))
				&& Boolean.FALSE.equals(data.get("blEnviaPdf"))) {
			throw new BusinessException("LMS-04412");
		}
		
		EnvioCteCliente bean = new EnvioCteCliente();
		bean.setIdEnvioCteCliente((Long) data.get("idEnvioCteCliente"));
		bean.setTpParametrizacao(domainValueService.findDomainValueByValue("DM_TIPO_EMAIL", tpParametrizacao));
		bean.setTpEnvio(new DomainValue((String) data.get("tpEnvio")));

		Cliente cliente = clienteService.findById((Long) data.get("idClienteRemetente"));
		bean.setCliente(cliente);
		
		if(data.get("idTemplate") != null){
			TemplateRelatorio templateRelatorio = templateRelatorioService.findById((Long) data.get("idTemplate"));
			bean.setTemplateRelatorio(templateRelatorio);
		}
		
		bean.setBlEnviaXml((Boolean) data.get("blEnviaXml"));
		bean.setBlEnviaPdf((Boolean) data.get("blEnviaPdf"));
		bean.setBlEnviaNfe((Boolean) data.get("blEnviaNfe"));
		bean.setBlAgendamentoAutomatico((Boolean) data.get("blAgendamentoAutomatico"));
		bean.setBlConfirmaAgendamento((Boolean) data.get("blConfirmaAgendamento"));
		
		bean.setDsAssunto((String) data.get("dsAssunto"));
		bean.setBlChaveAssunto((Boolean) data.get("blChaveAssunto"));
		bean.setBlEspacoAssunto((Boolean) data.get("blEspacoAssunto"));

		bean.setNmPdf((String) data.get("nmPdf"));
		bean.setBlChavePdf((Boolean) data.get("blChavePdf"));
		bean.setBlEspacoPdf((Boolean) data.get("blEspacoPdf"));

		bean.setNmXml((String) data.get("nmXml"));
		bean.setBlChaveXml((Boolean) data.get("blChaveXml"));
		bean.setBlEspacoXml((Boolean) data.get("blEspacoXml"));

		bean.setDsTextoEmail((String) data.get("dsTextoEmail"));
		
		bean.setDtPeriodoInicial((YearMonthDay) data.get("dtPeriodoInicial"));
		bean.setDtPeriodoFinal((YearMonthDay) data.get("dtPeriodoFinal"));

		if ("C".equals(bean.getTpParametrizacao().getValue())
				&& "O".equals(bean.getTpEnvio().getValue())) {

			String strFrom = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
			String strEmails = (String) configuracoesFacade.getValorParametro("EMAIL_PARAMETRIZACAO_ENVIO");
			String strSubject = (String) configuracoesFacade.getValorParametro("ASSUNTO_PARAMETRIZACAO_ENVIO");

			String text = configuracoesFacade.getValorParametro(
					"TEXTO_INCLUI_PARAMETRIZACAO_ENVIO").toString();

			if (text != null) {
				text = text.replace("{0}", bean.getCliente().getPessoa().getNrIdentificacaoFormatado()
						+ " " + bean.getCliente().getPessoa().getNmPessoa());

				text = text.replace("{1}", bean.getTpParametrizacao()
						.getDescriptionAsString());
			}

			sendEmail(strEmails, strSubject, strFrom, text);
		}

		return super.store(bean);
	}

	private void sendEmail(final String strEmails, final String strSubject,
			final String strFrom, final String strText) {
		List<String> dsEmails = new ArrayList<String>();
		dsEmails.add(strEmails);

		this.sendEmail(strFrom, dsEmails, strSubject, null, strText);
	}

	public ClienteLayoutEDI findOutrosEnvios(Long idClienteEdi) {
		return clienteLayoutEDIService.findByIdCliente(idClienteEdi);

	}

	public List findByCliente(Long idCliente) {
		return envioCteClienteDAO.findByCliente(idCliente);
	}

	/**
	 * Grid da tela Consulta
	 * 
	 * @param parametros
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedManterParametrizacaoEnvio(
			TypedFlatMap parametros) {
		return envioCteClienteDAO.findPaginated(parametros,
				FindDefinition.createFindDefinition(parametros));
	}

	public EnvioCteCliente findEnvioCteClienteByIdClienteTpEnvioEAndParametrizacaoC(
			Long idCliente) {
		return this.envioCteClienteDAO.findEnvioCteClienteByIdClienteTpEnvioEAndParametrizacaoC(idCliente);
	}

	public void updateMonitoramento(CTeUpdateDMN cteUpdateDMN){
		Filial filial = filialService.findFilial(ID_EMPRESA_MATRIZ, cteUpdateDMN.getSgFilialOrigem());
		DateTime dhEnvio = JTDateTimeUtils.getDataHoraAtual(filial);
		
		String dsEmail = cteUpdateDMN.getEmailsDestinatarios();
		if(dsEmail == null){
			dsEmail = (String) configuracoesFacade.getValorParametro("OBS_CLIENTE_SEM_EMAIL_XML");
		}
		
		monitoramentoDocEletronicoService.updateEnvioCTeCliente(cteUpdateDMN.getIdMonitoramentoDocEletronic(), dhEnvio, dsEmail, cteUpdateDMN.getXml());
	}
	
	public CTeDadosEnvioDMN findDadosParametrizacaoEnvio(String nrChave){
		CTeDadosEnvioDMN cteDadosEnvioDMN = null;
		Map<String, Object> dadosEnvioCteCliente = monitoramentoDocEletronicoService.findDadosEnvioCTeClienteByNrChave(nrChave);

		if(dadosEnvioCteCliente!= null && !dadosEnvioCteCliente.isEmpty()){
			cteDadosEnvioDMN = new CTeDadosEnvioDMN();
			cteDadosEnvioDMN.setBlEnviaEmail(Boolean.FALSE);
			
			cteDadosEnvioDMN.setIdMonitoramentoDocEletronic((Long) dadosEnvioCteCliente.get("idMonitoramentoDocEletronic")); 
			Long idDoctoServico = (Long) dadosEnvioCteCliente.get("idDoctoServico");
			cteDadosEnvioDMN.setIdDoctoServico(idDoctoServico);
			Cliente cliente = devedorDocServService.findByIdDoctoServico(idDoctoServico);
			String emailsDestinatarios = findEmailsDestinatarios(cliente.getIdCliente());
			
			if(emailsDestinatarios != null && !"".equals(emailsDestinatarios)){
				EnvioCteCliente envioCteCliente = findByIdClienteParametrizacaoC(cliente.getIdCliente());
				
				String assuntoEmail = null;
				String corpoEmail = null;
				String nmAnexoXml = null;
				String nmAnexoPdf = null;
				if(envioCteCliente == null){
					cteDadosEnvioDMN.setBlEnviaEmail(Boolean.TRUE);
					cteDadosEnvioDMN.setBlEnviaXml(Boolean.TRUE);
					cteDadosEnvioDMN.setBlEnviaPdf(Boolean.TRUE);
					cteDadosEnvioDMN.setXmlParaPdf(gerarConhecimentoEletronicoService.findXmlCteComComplementos(idDoctoServico));
					assuntoEmail = (String) configuracoesFacade.getValorParametro("ASSUNTO_EMAIL_CTE");
					corpoEmail = assuntoEmail;
					nmAnexoXml = getNomeAnexo1(nrChave);
					nmAnexoPdf = nmAnexoXml;
					
				} else if(isClienteConfiguradoEnvioEmail(envioCteCliente)){
					cteDadosEnvioDMN.setBlEnviaEmail(Boolean.TRUE);
					cteDadosEnvioDMN.setBlEnviaXml(envioCteCliente.getBlEnviaXml());
					cteDadosEnvioDMN.setBlEnviaPdf(envioCteCliente.getBlEnviaPdf());
					assuntoEmail = getAssunto(envioCteCliente, nrChave);
					corpoEmail = envioCteCliente.getDsTextoEmail();
					
					if (envioCteCliente.getBlEnviaXml()) {
						nmAnexoXml = getNomeAnexo1(nrChave, envioCteCliente);
					}
					
					if (envioCteCliente.getBlEnviaPdf()) {
						nmAnexoPdf = getNomeAnexo2(nrChave, envioCteCliente);
						cteDadosEnvioDMN.setXmlParaPdf(gerarConhecimentoEletronicoService.findXmlCteComComplementos(idDoctoServico));
					}
				}
				
				if(cteDadosEnvioDMN.getBlEnviaEmail()){
					cteDadosEnvioDMN.setEmailRemetente((String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS"));
					cteDadosEnvioDMN.setEmailsDestinatarios(emailsDestinatarios);
					cteDadosEnvioDMN.setAssuntoEmail(assuntoEmail);
					cteDadosEnvioDMN.setCorpoEmail(corpoEmail);
					cteDadosEnvioDMN.setNmAnexoXml(nmAnexoXml);
					cteDadosEnvioDMN.setNmAnexoPdf(nmAnexoPdf);
				}
			}
		}
		return cteDadosEnvioDMN;
	}
	
	private boolean isClienteConfiguradoEnvioEmail(EnvioCteCliente envioCteCliente) {
		return TP_ENVIO_POR_EMAIL.equals(envioCteCliente.getTpEnvio().getValue()) && (envioCteCliente.getBlEnviaXml() || envioCteCliente.getBlEnviaPdf());
	}

	private String findEmailsDestinatarios(Long idCliente) {
		StringBuilder emailsDestinatarios = new StringBuilder();
		List<Contato> contatos = contatoService.findContatosByIdPessoaTpContato(idCliente, TP_CONTATO_CTE);
		
		if(contatos.isEmpty()){
			String dsEmail = pessoaService.findDsEmailByIdPessoa(idCliente);
			if(dsEmail != null && ValidateUtils.validateEmail(dsEmail)){
				emailsDestinatarios.append(dsEmail); 
			}
			
		} else {
			for (Contato contato : contatos) {
				if (contato.getDsEmail() != null && ValidateUtils.validateEmail(contato.getDsEmail())) {
					emailsDestinatarios.append(contato.getDsEmail());
					emailsDestinatarios.append(";");
				}
			}
		}
		
		return emailsDestinatarios.toString();
	}
	
	/**
	 * @author WagnerFC
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	@AssynchronousMethod(name = "vendas.EnviaCteCliente", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ALWAYS)
	public void enviaCteCliente() throws UnsupportedEncodingException,
			Exception {
		List<Long> doctoServicos = doctoServicoService.findDoctoServicoToEnviaCTeCliente();

		if (!doctoServicos.isEmpty()) {
			/*
			 * REMETENTE
			 */
			String remetente = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
			EnvioCteCliente envioCteCliente = null;
			MonitoramentoDocEletronico mde = null;
			List<String> destinatarios = new ArrayList<String>();
			List<File> anexos = null;
			String assunto = null;
			String corpoEmail = null;
			String nomeAnexo1 = null;
			String nomeAnexo2 = null;

			for (Long idDoctoServico : doctoServicos) {
				try {
					mde = this.monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(idDoctoServico);

					DevedorDocServ docServ = devedorDocServService.findDevedorByDoctoServico(idDoctoServico);
					/*
					 * DESTINATARIO
					 */
					destinatarios = getDestinatario(docServ);

					envioCteCliente = this.findByIdClienteParametrizacaoC(docServ.getCliente().getIdCliente());

					if (envioCteCliente == null) {
						if (destinatarios == null || destinatarios.isEmpty()) {
							String msg = (String)configuracoesFacade.getValorParametro("OBS_CLIENTE_SEM_EMAIL_XML");
							destinatarios.add(msg);
							updateMonitoramentoDocEletronico(mde,destinatarios);
							continue;
						}
						/*
						 * ASSUNTO
						 */
						assunto = (String) configuracoesFacade.getValorParametro("ASSUNTO_EMAIL_CTE");

						/*
						 * CORPO EMAIL
						 */
						corpoEmail = assunto;
						anexos = new ArrayList<File>();

						/*
						 * ANEXO 1
						 */
						nomeAnexo1 = getNomeAnexo1(mde.getNrChave());
						anexos.add(this.getAnexo1XML(mde, nomeAnexo1));

						/*
						 * ANEXO 2
						 */
						nomeAnexo2 = nomeAnexo1;
						anexos.add(this.getAnexo2PDF(mde, nomeAnexo2));
						
						/*
						 * ENVIA EMAIL
						 */
						if (corpoEmail == null) {
							corpoEmail = " ";
						}

						sendEmail(remetente, destinatarios, assunto, anexos,
								corpoEmail);
						
						/*
						 * ATUALIZA MONITORAMENTO
						 */
						updateMonitoramentoDocEletronico(mde, destinatarios);
						
					} else if ("E".equals(envioCteCliente.getTpEnvio()
							.getValue())) {
						/*
						 * Se nenhum endereço de email for gerado para o cliente não
						 * gerar o referido email.
						 */
						if (destinatarios == null || destinatarios.isEmpty()) {
							String msg = (String)configuracoesFacade.getValorParametro("OBS_CLIENTE_SEM_EMAIL_XML");
							destinatarios.add(msg);
							updateMonitoramentoDocEletronico(mde,destinatarios);
							continue;
						}
						
						/*
						 * ASSUNTO
						 */

						assunto = getAssunto(envioCteCliente, mde.getNrChave());

						/*
						 * CORPO EMAIL
						 */
						corpoEmail = envioCteCliente.getDsTextoEmail();

						anexos = new ArrayList<File>();

						/*
						 * ANEXO 1
						 */
						if (envioCteCliente.getBlEnviaXml()) {
							nomeAnexo1 = getNomeAnexo1(mde.getNrChave(), envioCteCliente);
							anexos.add(this.getAnexo1XML(mde, nomeAnexo1));
						}

						/*
						 * ANEXO 2
						 */
						if (envioCteCliente.getBlEnviaPdf()) {
							nomeAnexo2 = getNomeAnexo2(mde.getNrChave(), envioCteCliente);
							anexos.add(this.getAnexo2PDF(mde, nomeAnexo2));
						}
						
						/*
						 * ENVIA EMAIL
						 */
						if (corpoEmail == null) {
							corpoEmail = " ";
						}

						sendEmail(remetente, destinatarios, assunto, anexos,
								corpoEmail);
						
						/*
						 * ATUALIZA MONITORAMENTO
						 */
						updateMonitoramentoDocEletronico(mde, destinatarios);
						
					}
				} catch (Exception e) {
					log.error("Erro ao enviar email: vendas.EnviaCteCliente ", e);
				}
			}
		}
	}
	
	/**
	 * Metodo utilizado pelo batch
	 *  
	 * @param docServ
	 * @return
	 */
	private List<String> getDestinatario(DevedorDocServ docServ) {
		List<String> emailDest = new ArrayList<String>();
		List<Contato> contatosList = contatoService
				.findContatosByIdPessoaTpContato(docServ.getCliente()
						.getIdCliente(), "CT");

		if (contatosList.isEmpty()) {
			Pessoa pessoa = pessoaService.findById(docServ.getCliente()
					.getIdCliente());

			if (pessoa != null) {
				if (pessoa.getDsEmail() != null) {
					emailDest.add(pessoa.getDsEmail());
				}
			}
		} else {
			for (Contato contato : contatosList) {
				if (contato.getDsEmail() != null) {
					emailDest.add(contato.getDsEmail());
				}
			}
		}

		return emailDest;
	}
	
	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param mde
	 * @return
	 */
	private String getNomeAnexo1(String nrChave) {
		StringBuilder nome = new StringBuilder();
		nome.append("CT-e_");
		nome.append(nrChave);
		nome.append("_");
		return nome.toString();
	}

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param mde
	 * @param envioCteCliente
	 * @return
	 */
	private String getNomeAnexo1(String nrChave, EnvioCteCliente envioCteCliente) {
		StringBuilder nome = new StringBuilder();

		if (envioCteCliente.getNmXml() == null
				|| envioCteCliente.getNmXml().equals("")) {
			nome.append("CT-e_");
			nome.append(nrChave);
		} else {
			nome.append(envioCteCliente.getNmXml());

			if (envioCteCliente.getBlEspacoXml()) {
				nome.append(" ");
			}

			if (envioCteCliente.getBlChaveXml()) {
				nome.append(nrChave);
			}
		}
		nome.append("_");
		return nome.toString();
	}

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param mde
	 * @param envioCteCliente
	 * @return
	 */
	private String getNomeAnexo2(String nrChave, EnvioCteCliente envioCteCliente) {
		StringBuilder nome = new StringBuilder();

		if (envioCteCliente.getNmPdf() == null
				|| envioCteCliente.getNmPdf().equals("")) {
			nome.append("CT-e_");
			nome.append(nrChave);

		} else {
			nome.append(envioCteCliente.getNmPdf());

			if (envioCteCliente.getBlEspacoPdf()) {
				nome.append(" ");
			}

			if (envioCteCliente.getBlChavePdf()) {
				nome.append(nrChave);
			}
		}
		nome.append("_");

		return nome.toString();
	}

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param mde
	 * @param nomeAnexo1
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private File getAnexo1XML(MonitoramentoDocEletronico mde, String nomeAnexo1)
			throws UnsupportedEncodingException, Exception {
		String xml = new String(mde.getDsDadosDocumento(),
				Charset.forName("UTF-8"));

		String host = configuracoesFacade.getValorParametro("ADSM_REPORT_HOST")
				.toString();

		JRRemoteReportsRunner reportsRunner = new JRRemoteReportsRunner(
				host);
		File xmlTemp = File.createTempFile(nomeAnexo1, ".xml",
				reportsRunner.generateOutputDir());
		Writer out = new OutputStreamWriter(new FileOutputStream(xmlTemp),
				Charset.forName("UTF-8"));

		try {
			out.write(xml);
		} finally {
			out.close();
		}

		return xmlTemp;
	}

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param mde
	 * @param nomeAnexo1
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws Exception
	 */
	private File getAnexo2PDF(MonitoramentoDocEletronico mde, String nomeAnexo1)
			throws UnsupportedEncodingException, Exception {
		String xml = gerarConhecimentoEletronicoService.findXmlCteComComplementos(mde.getDoctoServico().getIdDoctoServico());

		String host = configuracoesFacade.getValorParametro("ADSM_REPORT_HOST").toString();

		JRRemoteReportsRunner reportsRunner = new JRRemoteReportsRunner(host);
		File pdfTemp = reportsRunner.executeReport(xml, null,nomeAnexo1);

		return pdfTemp;
	}

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param mde
	 * @param destinatarios
	 */
	private void updateMonitoramentoDocEletronico(
			MonitoramentoDocEletronico mde, List<String> destinatarios) {
		this.monitoramentoDocEletronicoService.updateDhEnvioEmail(mde.getIdMonitoramentoDocEletronic(), 
				destinatarios.toString().replaceAll("\\[|\\]", "").replaceAll(",", ";"));
	}

	
	
	
	/**
	 * Metodo ajustado para trabalhar com o retorno do envio de emails feito externamente
	 * 
	 * @param mde
	 * @param destinatarios
	 * @param dhEnvio
	 */
	private String updateMonitoramentoDocEletronico(MonitoramentoDocEletronico mde, List<String> destinatarios, DateTime dhEnvio, String xml, String chaveXml, Filial filial) {
		return this.monitoramentoDocEletronicoService.updateDhEnvioEmail(mde.getIdMonitoramentoDocEletronic(), 
				destinatarios.toString().replaceAll("\\[|\\]", "").replaceAll(",", ";"),
				dhEnvio,
				xml,
				chaveXml,
				filial);
	}

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param remetenteLms
	 * @param dsEmails
	 * @param assunto
	 * @param anexos
	 * @param mensagem
	 */
	private void sendEmail(final String remetenteLms, final List<String> dsEmails, final String assunto,
	final List<File> anexos, final String mensagem) {
		List<MailAttachment> mailAttachments = new ArrayList<MailAttachment>();
		if (anexos != null) {
			for (File anexo : anexos) {
				MailAttachment mailAttachment = new MailAttachment();
				mailAttachment.setName(anexo.getName());
				mailAttachment.setData(FileUtils.readFile(anexo));
				mailAttachments.add(mailAttachment);
			}
		}
		
		Mail mail = createMail(
			StringUtils.join(dsEmails.toArray(new String[dsEmails.size()]), ";"), 
			remetenteLms, 
			assunto, 
			mensagem, 
			mailAttachments
		);
		
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body, List<MailAttachment> mailAttachmentList) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		mail.setAttachements(mailAttachmentList.toArray(new MailAttachment[mailAttachmentList.size()]));
		return mail;
	}	

	/**
	 * Metodo utilizado pelo batch
	 * 
	 * @param envioCteClientes
	 * @param mde
	 * @return
	 */
	private String getAssunto(EnvioCteCliente envioCteClientes,	String nrChave) {
		StringBuilder assunto = new StringBuilder();

		if (envioCteClientes.getDsAssunto() == null
				&& !envioCteClientes.getBlChaveAssunto()) {
			assunto.append(configuracoesFacade
					.getValorParametro("ASSUNTO_EMAIL_CTE"));
		} else {
			if (envioCteClientes.getDsAssunto() != null) {
				assunto.append(envioCteClientes.getDsAssunto());
			} else {
				assunto.append("");
			}

			if (envioCteClientes.getBlEspacoAssunto()) {
				assunto.append(" ");
			}

			if (envioCteClientes.getBlChaveAssunto()) {
				assunto.append(nrChave);
			}
		}

		return assunto.toString();
	}
	
	public void removeAgrupadorClienteByIdParametrizacaoEnvio(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		this.envioCteClienteDAO.removeByAgrupadorClienteByIdParametrizacaoEnvio(ids);
	}
	
	public boolean findBlConfAgendamentoByTpParametrizacaoEnvioCliente(Cliente cliente){
		return this.envioCteClienteDAO.findBlConfAgendamentoByTpParametrizacaoEnvioCliente(cliente);
	}
	
	public boolean findBlRecolheICMSByTpParametrizacaoEnvioCliente(Cliente cliente){
		return this.envioCteClienteDAO.findBlRecolheICMSByTpParametrizacaoEnvioCliente(cliente);
	}

	public EnvioCteCliente findByIdClienteParametrizacaoC(Long idCliente) {
		return this.envioCteClienteDAO.findByIdClienteParametrizacaoC(idCliente);
	}
	
	public EnvioCteCliente findEnvioCteClienteByIdClienteETipoRelatorio(Long idCliente, String tipoRelatorio) {
		return this.envioCteClienteDAO.findEnvioCteClienteByIdClienteETipoRelatorio(idCliente, tipoRelatorio);
	}

	/**
	 * RowCount da grid da tela
	 * 
	 * @param parametros
	 * @return
	 */
	public Integer getRowCount(TypedFlatMap criteria) {
		return getDAO().getRowCount(criteria);
	}

	public Integer getRowCountManterParametrizacaoEnvio(TypedFlatMap criteria) {
		return envioCteClienteDAO.getRowCount(criteria);
	}

	private EnvioCteClienteDAO getDAO() {
		return envioCteClienteDAO;
	}

	public void setEnvioCteClienteDao(EnvioCteClienteDAO dao) {
		this.envioCteClienteDAO = dao;
		setDao(dao);
	}

	public ClienteLayoutEDIService getClienteLayoutEDIService() {
		return clienteLayoutEDIService;
	}

	public void setClienteLayoutEDIService(
			ClienteLayoutEDIService clienteLayoutEDIService) {
		this.clienteLayoutEDIService = clienteLayoutEDIService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setBatchLogger(BatchLogger batchLogger) {
		this.batchLogger = batchLogger;
		this.batchLogger.logClass(ManterParametrizacaoEnvioService.class);
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setMonitoramentoDocEletronicoService(
			MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public void setGerarConhecimentoEletronicoService(
			GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoService) {
		this.gerarConhecimentoEletronicoService = gerarConhecimentoEletronicoService;
	}

	public void setTemplateRelatorioService(TemplateRelatorioService templateRelatorioService) {
		this.templateRelatorioService = templateRelatorioService;
	}

	public void setIntegracaoNDDigitalService(
			IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void storeAgrupadorCliente(AgrupadorCliente agrupadorCliente) {
		this.envioCteClienteDAO.storeAgrupadorCliente(agrupadorCliente);
	}

	public List<Map<String, Object>> findAgrupamentoClienteByIdParametrizacaoEnvio(
			Long idParametrizacaoEnvio) {
		 List<Map<String, Object>> lista = this.envioCteClienteDAO.findAgrupamentoClienteByIdParametrizacaoEnvio(idParametrizacaoEnvio);
		 lista = formataCnpjsToList(lista);
		 return lista;
	}
	
	private List<Map<String, Object>> formataCnpjsToList(List<Map<String, Object>> cnpjs){
		List<Map<String, Object>> cnpjsFomatados = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : cnpjs){
			String cnpj = (String) map.get("nrIdentificacao");
			Map<String, Object> mapFormatado = new HashMap<String, Object>();
			if(cnpj.length() == TAMANHO_CNPJ){
				mapFormatado.put("nrIdentificacao", FormatUtils.formatCNPJ(cnpj));
			}else if (cnpj.length() == TAMANHO_CPF){
				mapFormatado.put("nrIdentificacao", FormatUtils.formatCPF(cnpj));
			}
			cnpjsFomatados.add(mapFormatado);
		}
		return cnpjsFomatados;
	}
	

	/**
	 * Método exposto
	 * 
	 * 1. Recebe um map com uma lista de IdCliente <B>OU</B> uma lista de CNPJ e
	 * devolve uma List<Map<String, Objeto>>
	 * 
	 * @param nrosIdentificacao
	 * @return 
	 */
	@MethodSecurity(processGroup = "vendas.ManterParametrizacaoEnvioService", processName = "findContatos", authenticationRequired=false)
	public List<Map<String, Object>> findContatos(Map<String, List<Map<String, String>>> parametros) {

		List<Map<String, String>> nrsIdentificacao = new ArrayList<Map<String,String>>();
		List<Map<String, String>> idsCliente = new ArrayList<Map<String,String>>();
		
		List<Map<String, Object>> contatosComplementos = new ArrayList<Map<String, Object>>();
		Map<String, Object> contatoComplemento = new HashMap<String, Object>();
		Cliente cliente;
		
		// Colocado fora do loop e long de sua utilização para não buscar o remetente no banco, 
		// sempre o mesmo, para cada cliente
		String remetente = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		
		Map<String, Object> erro = new HashMap<String, Object>();

		if (null != parametros.get("nrosIdentificacao") && parametros.get("nrosIdentificacao").size() > 0) {
			contatoComplemento = new HashMap<String, Object>();
			nrsIdentificacao = parametros.get("nrosIdentificacao");
			erro = new HashMap<String, Object>();
			
			for (Map<String, String> dados : nrsIdentificacao) {
				cliente = new Cliente();
				cliente = getClienteService().findByNumeroIdentificacao(dados.get("cnpj"));

				if (cliente == null) {
					erro.put("nroIdentificacao", dados.get("cnpj"));
					erro.put("erro", "Usuário de CNPJ " + dados.get("cnpj") + " não foi encontrado.");

					contatoComplemento.put("ErroLMS", erro);

					continue;
				}
				
				contatoComplemento = findDadosComplementaresCliente(cliente, MapUtils.getString(dados, "chaveXML"), remetente);

				if(null != contatoComplemento && null != contatoComplemento.get("ErroClienteSemContato")) {
					erro.put("nroIdentificacao", dados.get("cnpj"));
					erro.put("erro", "Usuário de CNPJ " + dados.get("cnpj") + " não contém contatos cadastados.");
					
					contatoComplemento.remove("ErroClienteSemContato");
					contatoComplemento.put("ErroLMS", erro);
				}

				if (null != contatoComplemento && contatoComplemento.size() > 0) {
					contatosComplementos.add(contatoComplemento);
				}
			}
			
		} else if (null != parametros.get("idsCliente") && parametros.get("idsCliente").size() > 0) {
			contatoComplemento = new HashMap<String, Object>();
			idsCliente = null == parametros.get("idsCliente") ? null : parametros.get("idsCliente");
			erro = new HashMap<String, Object>();

			for (Map<String, String> dados : idsCliente) {
				cliente = new Cliente();
				cliente = getClienteService().findById(Long.valueOf(dados.get("idCliente")));

				if (cliente == null) {
					erro.put("idCliente", dados.get("idCliente"));
					erro.put("erro", "Usuário com idCliente " + dados.get("idCliente") + " não foi encontrado.");

					contatoComplemento.put("ErroLMS", erro);

					continue;
				}

				contatoComplemento = findDadosComplementaresCliente(cliente, MapUtils.getString(dados, "chaveXML"), remetente);

				if(null != contatoComplemento && null != contatoComplemento.get("ErroClienteSemContato")) {
					erro.put("nroIdentificacao", dados.get("cnpj"));
					erro.put("erro", "Usuário com idCliente " + dados.get("idCliente") + " não contém contatos cadastados.");

					contatoComplemento.remove("ErroClienteSemContato");
					contatoComplemento.put("ErroLMS", erro);

				}

				if (null != contatoComplemento && contatoComplemento.size() > 0) {
					contatosComplementos.add(contatoComplemento);
				}
			}


		} else {
			erro.put("erro", "Não foi enviado nenhum CNPJ ou idCliente para processar.");
			contatosComplementos.add(erro);

		}

		return contatosComplementos;
	}
	
	
	/**
	 * Método exposto
	 * 
	 * 2. Retorno do envio do email com o XML para o cliente
	 * 
	 * @param nrosIdentificacao
	 * @return 
	 */
	@MethodSecurity(processGroup = "vendas.ManterParametrizacaoEnvioService", processName = "executeUpdMonitDocEletronico", authenticationRequired=false)
	public List<Object> executeUpdMonitDocEletronico(Map<String, Object> parametros) {

		List<String> destinatarios = new ArrayList<String>();
		
		List<Object> retorno = new ArrayList<Object>();
		
		Map<String, Object> erro = new HashMap<String, Object>();
		
		List<Map<String, Object>> xmls = (List<Map<String, Object>>) parametros.get("XMLs");
		
		for (Map<String, Object> map : xmls) {
			erro = new HashMap<String, Object>();
			
			String xml = MapUtils.getString(map, "xml");
			String chaveXml = MapUtils.getString(map, "chave");
			String contatos = MapUtils.getString(map, "contatos");
			String error = MapUtils.getString(map, "error");
			String dhEnvio = MapUtils.getString(map, "dhEnvio");
			String sgFilial = xml.substring(xml.indexOf("<xOrig>") + 7, xml.indexOf("</xOrig>"));
			
			Filial filial = filialService.findFilial(361L, sgFilial);
			
			MonitoramentoDocEletronico mde = this.monitoramentoDocEletronicoService.findByNrChave(chaveXml);

			if (null == mde) {
				erro.put("chaveXML", chaveXml);
				erro.put("erro", "Não é possível inserir o XML. ChaveXML não encontrada em MONITORAMENTO_DOC_ELETRONICO");
				retorno.add(erro);

				continue;
			}			

			// Verifica se foi passada alguma mensagem de erro
			if (null == contatos || contatos.isEmpty()) {
				destinatarios.add((String) configuracoesFacade.getValorParametro("OBS_CLIENTE_SEM_EMAIL_XML"));
			} else {
				String[] splited = contatos.split(";");
				for (String email : splited) {
					destinatarios.add(email);
				}
			}
						
			if (null != error && error.length() > 0) {
				destinatarios.add(error);
			}

			// Atualiza o registro no Monitoramento_Documento_Eletronico
			String retornoUpdate = updateMonitoramentoDocEletronico(mde, destinatarios, new DateTime(dhEnvio), xml, chaveXml, filial);
			if (null != retornoUpdate && !retornoUpdate.isEmpty()) {
				erro.put("chaveXML", chaveXml);
				erro.put("erro", retornoUpdate);
				retorno.add(erro);
			}

		}

		return retorno;
	}

	
	/**
	 * Método exposto
	 * 
	 * 3. Retorna a tag <em>dadosAdic</em>, necessária para a geração do PDF/DACTE.  
	 * 
	 * @param idsDoctoServico
	 * @return 
	 */
	@MethodSecurity(processGroup = "vendas.ManterParametrizacaoEnvioService", processName = "findComplementos", authenticationRequired=false)
	public List<Map<String, Object>> findComplementos(Map<String, List<Object>> parametros) {
		List<Object> idsDoctoServico = parametros.get("idsDoctoServico");
		List<Map<String, Object>> complemento = new ArrayList<Map<String,Object>>();
		
		//Recupera os complementos necessários à geração do PDF
		for (Object idDoctoServico : idsDoctoServico) {
			Map<String, Object> mapComplemento = new HashMap<String, Object>();
			String dadosAdic = getComplementos(Long.valueOf((String) idDoctoServico));
			
			mapComplemento.put("idDoctoServico", idDoctoServico);
			mapComplemento.put("dadosAdic", dadosAdic);
			complemento.add(mapComplemento);
		}		
		
		return complemento;
	}
	

	/**
	 * 4. Unifica os métodos 1 e 3, retornando os contatos e os complementos para envio dos XMLs e geração dos PDF
	 * 
	 * @param parametros
	 * @return uma lista de maps:
	 * 		idCliente
	 * 		cnpj
	 * 		idDoctoServico
	 * 		Emails
	 * 		emailFalha
	 * 		cfigEnvio
	 *	 		dsAssunto
	 * 			dsCorpoEmail
	 * 			blEnviaXML
	 * 			blEnviaPDF
	 * 			fileNameXML
	 * 			fileNamePDF
	 * 		errosLMS
	 * 		complemento
	 */
	@MethodSecurity(processGroup = "vendas.ManterParametrizacaoEnvioService", processName = "findContatosComplementos", authenticationRequired=false)
	public List<Map<String, Object>> findContatosComplementos(Map<String, List<Map<String, String>>> parametros) {
		
		List<Map<String, Object>> contatosList = findContatos(parametros);
		for (Map<String, Object> contatoMap : contatosList) {

			Long idDoctoServico = MapUtils.getLong(contatoMap, "idDoctoServico");
			if (null != idDoctoServico) {
				String dadosAdic = getComplementos(idDoctoServico);

				contatoMap.put("complemento", dadosAdic);
			} else {
				continue;
			}
		}
		
		
		return contatosList;
		
	}
	
	
	/**
	 * Busca dados complementares do cliente para preparar o envio de email
	 * 
	 * @param docServ
	 * @return
	 */
	private Map<String, Object> findDadosComplementaresCliente(Cliente cliente, String chaveXML, String remetente) {
	
		Map<String, Object> dados = new HashMap<String, Object>();
		Map<String, Object> cfigEnvio = new HashMap<String, Object>();
		Map<String, Object> erro = new HashMap<String, Object>();
		StringBuilder destinatarios = new StringBuilder();
		
		Long idCliente = cliente.getIdCliente();
		List<Contato> contatosList = contatoService.findContatosByIdPessoaTpContato(idCliente, "CT");
		MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findByNrChave(chaveXML);

		if (null == contatosList || contatosList.isEmpty()) {
			Pessoa pessoa = pessoaService.findById(idCliente);

			if (pessoa != null) {
				if (pessoa.getDsEmail() != null) {
					destinatarios.append(pessoa.getDsEmail().trim());
				}
			}

		} else {
			for (Contato contato : contatosList) {
				if (contato.getDsEmail() != null) {
					destinatarios.append(contato.getDsEmail().trim()).append(";");
				}
			}
			destinatarios.deleteCharAt(destinatarios.length() - 1);
		}
	
		dados.put("idCliente", idCliente.toString());
		dados.put("cnpj", cliente.getPessoa().getNrIdentificacao());
		dados.put("destinatarios", destinatarios.toString());
		dados.put("emailFalha", parametroGeralService.findByNomeParametro(EMAIL_FALHA_ENVIO_EMAIL));

		EnvioCteCliente envioCteCliente = this
				.findByIdClienteParametrizacaoC(idCliente);
		if (null == envioCteCliente) {
			String nrIdentificacao = cliente.getPessoa().getNrIdentificacao();
			erro.put("nroIdentificacao", nrIdentificacao);
			erro.put("erro", "Usando configuração padrão. Usuário, CNPJ " + nrIdentificacao + ", não contém as configurações de envio.");

			dados.put("ErroLMS", erro);

			cfigEnvio = getDefaultEmailData(destinatarios.toString(), remetente, mde);

			if (null != cfigEnvio.get("ErroLMS")) {

				// Sobrescreve o erro anterior pelo enviado por "getDefaultEmailData" e remove-o de cfigEnvio
				// Esse mensagem deve ser gravada em DS_ENVIO_EMAIL
				dados.put("ErroLMS", cfigEnvio.get("ErroLMS"));
				cfigEnvio.remove("ErroLMS");
			}

		} else {
			cfigEnvio = montaCfigEnvio(envioCteCliente, chaveXML, remetente, mde);
		}

		dados.put("idDoctoServico", mde.getDoctoServico().getIdDoctoServico());
		dados.put("cfigEnvio", cfigEnvio);

		return dados;
	}
	
	private Map<String, Object> getDefaultEmailData(String destinatarios,
			String remetente, MonitoramentoDocEletronico mde) {
		Map<String, Object> dados = new HashMap<String, Object>();
		Map<String, Object> erro = new HashMap<String, Object>();
		String assunto = null;
		String corpoEmail = null;
		String nomeAnexo1 = null;
		String nomeAnexo2 = null;

		if (destinatarios == null || destinatarios.isEmpty()) {
			String msg = (String) configuracoesFacade.getValorParametro("OBS_CLIENTE_SEM_EMAIL_XML");
			destinatarios = msg;
			String cnpj = mde.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao();

			erro.put("nroIdentificacao", cnpj);
			erro.put("erro", "Usuário de CNPJ " + cnpj + " não contém destinatários cadastados.");
			erro.put("DS_ENVIO_EMAIL", msg);
			
			dados.put("ErroLMS", erro);

			return dados;
		}
		
		/*
		 * ASSUNTO
		 */
		assunto = (String) configuracoesFacade.getValorParametro("ASSUNTO_EMAIL_CTE");

		/*
		 * CORPO EMAIL
		 */
		corpoEmail = assunto;
		
		/*
		 * ENVIA EMAIL
		 */
		if (corpoEmail == null) {
			corpoEmail = " ";
		}

		/*
		 * ANEXO 1
		 */
		nomeAnexo1 = getNomeAnexo1(mde.getNrChave());

		/*
		 * ANEXO 2
		 */
		nomeAnexo2 = nomeAnexo1;


		dados.put("dsAssunto", assunto);
		dados.put("dsCorpoEmail", corpoEmail);
		dados.put("dsRemetente", remetente);
		dados.put("filenameXML", nomeAnexo1);
		dados.put("filenamePDF", nomeAnexo2);
		
		return dados;
	}
	
	/**
	 * Monta cfig. de envio de email
	 * 
	 * @param envioCteCliente
	 * @return Map<String, String>
	 */
	private Map<String, Object> montaCfigEnvio(EnvioCteCliente envioCteCliente, String chaveXML, String remetente, MonitoramentoDocEletronico mde) {
		Boolean blEnviaXML = envioCteCliente.getBlEnviaXml();
		Boolean blEnviaPDF = envioCteCliente.getBlEnviaXml();
		
		
		Map<String, Object> cfigEmail = new HashMap<String, Object>();
		cfigEmail.put("dsRemetente", remetente);
		cfigEmail.put("dsAssunto", envioCteCliente.getDsAssunto());
		cfigEmail.put("dsCorpoEmail", envioCteCliente.getDsTextoEmail());
		cfigEmail.put("blEnviaXML", blEnviaXML.toString());
		cfigEmail.put("blEnviaPDF", blEnviaPDF.toString());
		cfigEmail.put("idDoctoServico", mde.getDoctoServico().getIdDoctoServico().toString());

		if (blEnviaXML) {
			String nomeAnexo1 = getNomeAnexo1(mde.getNrChave(), envioCteCliente);
			cfigEmail.put("filenameXML", nomeAnexo1);
		}

		if (blEnviaPDF) {
			String nomeAnexo2 = getNomeAnexo2(mde.getNrChave(), envioCteCliente);
			cfigEmail.put("filenamePDF", nomeAnexo2);
		}

		return cfigEmail;
	}

	
	/**
	 * Desmembrado do método <em>findComplemento</em> para permitir a unificação de retorno (contatos + dadosAdicionais)
	 * 
	 * @param idDoctoServico
	 * @return Trecho de XML com dados adicionais
	 */
	private String getComplementos(Long idDoctoServico) {

		Conhecimento conhecimento = conhecimentoService.findById(idDoctoServico);
		StringBuilder xml  = integracaoNDDigitalService.adicionaDadosAdic(conhecimento);
		Map<String, Object> map = conhecimentoService.findComplementosXMLCTE(idDoctoServico);				

		String dadosAdic = gerarConhecimentoEletronicoService.addComplementos(xml.toString(), map);

		return dadosAdic;
	}

}

