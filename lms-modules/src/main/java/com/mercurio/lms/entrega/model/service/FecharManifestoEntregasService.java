package com.mercurio.lms.entrega.model.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.EventoManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;
import com.mercurio.lms.entrega.model.dao.FecharManifestoEntregasDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.GerarNotaCreditoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;





/**
 * Classe de serviço para GerarReciboReembolso:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.entrega.fecharManifestoEntregasService"
 */
public class FecharManifestoEntregasService {

    private DoctoServicoService doctoServicoService;
    private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private WorkflowPendenciaService workflowPendenciaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ManifestoEntregaDocumentoService manifestoEntregaDocumentoService;
	private DomainValueService domainValueService;
	private ManifestoEntregaService manifestoEntregaService;
	private GerarNotaCreditoService gerarNotaCreditoService;
	private EventoManifestoService eventoManifestoService;
	private FecharManifestoEntregasDAO fecharManifestoEntregasDAO;

	private static final int MANI_SG_FILIAL = 0;
	private static final int MANI_NR_MANIFESTO = 1;
	private static final int MANI_ID_MANIFESTO = 2;
	private static final int MANI_NM_FILIAL = 3;
	private static final int MANI_ID_FILIAL = 4;
	private static final int DH_EMISSAO_MANIFESTO = 5;
	private static final int ROTA_ID = 6;
	private static final int ROTA_NR = 7;
	private static final int ROTA_DS = 8;
	private static final int DH_CHEGADA_COLETA_ENTREGA = 9;
	private static final int MT_NR_IDENT = 10;
	private static final int MT_NR_FROTA = 11;
	private static final int MT_ID = 12;
	private static final int MTSR_NR_IDENT = 13;
	private static final int MTSR_NR_FROTA = 14;
	private static final int MTSR_ID = 15;
	private static final int CC_ID_CC = 16;
	private static final int CC_NR = 17;
	private static final int CC_FI_ID = 18;
	private static final int CC_FI_SG = 19;
	private static final int CC_FI_NM = 20;
	private static final int PENDENCIAS = 21;
	private static final int MANI_TP_MANIFESTO_ENTREGA = 22;
	private static final int CLI_TP = 23;
	private static final int CLI_NR = 24;
	private static final int CLI_NOME = 25;
	private static final int TP_STATUS_MANIFESTO = 26;
	private static final int TP_MANIFESTO = 27;

	private static final String CONFIRM_COMPROVANTE_ENTREGA = "COMPROVANTE_ENTREGA_CONFIRMADO";
	private static final String CONFIRM_REEMBOLSO = "REEMBOLSO_CONFIRMADO";

	public TypedFlatMap store(TypedFlatMap parameters) {
		String tpSituacao = parameters.getString("tpSituacaoDocumento");
		Long idDoctoServico = parameters.getLong("doctoServico.id");
		Long idManifestoEntregaDocumento = parameters.getLong("idManifestoEntregaDocumento");
		
		// Persistido instância de ManifestoEntregaDocumento para realizar alterações de acordo com especificação.
		ManifestoEntregaDocumento manifestoEntregaDocumento =
				(ManifestoEntregaDocumento)getFecharManifestoEntregaDAO().getAdsmHibernateTemplate()
				.load(ManifestoEntregaDocumento.class,idManifestoEntregaDocumento);
		
		if (isEnabledBaixa(tpSituacao)) {
			if (parameters.getShort("ocorrenciaEntrega.cdOcorrenciaEntrega") == null || parameters.getDateTime("dhOcorrencia") == null)
				throw new BusinessException("LMS-09082");
			String tpFormaBaixa = "F";
			String tpEntregaParcial = parameters.getString("tpEntregaParcial");
			DateTime dhBaixa = parameters.getDateTime("dhOcorrencia");
			Short cdOcorrenciaEntrega = parameters.getShort("ocorrenciaEntrega.cdOcorrenciaEntrega");
			String nmRecebedor = parameters.getString("nmRecebedor");
			Boolean isValidExistPceRemetente = parameters.getBoolean("isValidExistPceRemetente");
			Boolean isValidExistPceDestinatario = parameters.getBoolean("isValidExistPceDestinatario");
			
			ManifestoEntrega manifestoEntrega = manifestoEntregaDocumento.getManifestoEntrega(); 
			manifestoEntregaDocumentoService.executeBaixaDocumento(
					manifestoEntrega.getIdManifestoEntrega(),
					idDoctoServico,cdOcorrenciaEntrega,tpFormaBaixa,tpEntregaParcial,dhBaixa,nmRecebedor,
					null,isValidExistPceRemetente,isValidExistPceDestinatario, null, null, null);
			
			if (!parameters.getString("ocorrenciaEntrega.tpOcorrencia.value").equals("E"))
				return null;
		}
		
		String obComprovanteRecolhido = parameters.getString("obRecolhido");
		if (isEnabledEntrega(tpSituacao).booleanValue()) {
			if (!parameters.getBoolean("blRecolhido").booleanValue() && StringUtils.isBlank(obComprovanteRecolhido))
				throw new BusinessException("LMS-09056");
			
			if (!parameters.getBoolean("blRecolhido").booleanValue()) {
				if (parameters.getBoolean(CONFIRM_COMPROVANTE_ENTREGA) == null)
					throw new BusinessException("LMS-ENGANAR",new Object[] {CONFIRM_COMPROVANTE_ENTREGA});
				else
					updateRegistroDocumentoEntrega(idDoctoServico, Boolean.FALSE,parameters.getString("nrRecolhido"),null,obComprovanteRecolhido);
			}else{
				updateRegistroDocumentoEntrega(idDoctoServico, Boolean.TRUE,parameters.getString("nrRecolhido"),new DomainValue("RR"),obComprovanteRecolhido);
				if (tpSituacao.equalsIgnoreCase("PRCO") || tpSituacao.equalsIgnoreCase("PCOM")) {
					DomainValue newTpSituacao = new DomainValue(((tpSituacao.equalsIgnoreCase("PRCO")) ? "PREC" : "FECH"));
					// Alterado instância a ser salva posteriormente.
					manifestoEntregaDocumento.setTpSituacaoDocumento(newTpSituacao);
				}
			}
		}
		
		String obChequeRecolhido = parameters.getString("obCheques");
		if (isEnabledReembolso(tpSituacao).booleanValue()) {
			if (!parameters.getBoolean("blCheques").booleanValue() && StringUtils.isBlank(obChequeRecolhido))
				throw new BusinessException("LMS-09059");

			if (!parameters.getBoolean("blCheques").booleanValue()) {
				if (parameters.getBoolean(CONFIRM_REEMBOLSO) == null)
					throw new BusinessException("LMS-ENGANAR",new Object[] {CONFIRM_REEMBOLSO});
				else
					updateReciboReembolso(idDoctoServico, obChequeRecolhido);
			}else{
				if (tpSituacao.equalsIgnoreCase("PRCO") || tpSituacao.equalsIgnoreCase("PREC")) {
					DomainValue newTpSituacao = new DomainValue(((tpSituacao.equalsIgnoreCase("PRCO")) ? "PCOM" : "FECH"));
					// Alterado instância a ser salva posteriormente.
					manifestoEntregaDocumento.setTpSituacaoDocumento(newTpSituacao);
				}
			}
			updateReciboReembolso(idDoctoServico,new DomainValue("CR"));
		}
				
		StringBuilder obManifestoEntregaDocumento = new StringBuilder(parameters.getString("obManifesto"));
		// Se há informação de comprovantes recolhidos, acrescenta observação.
		if (StringUtils.isNotEmpty(obComprovanteRecolhido)) {
			if (obManifestoEntregaDocumento.length() != 0) { // se já existir alguma observação, quebra linha.
				obManifestoEntregaDocumento.append("\n");
			}
			obManifestoEntregaDocumento.append(obComprovanteRecolhido);
		}
		// Se há informação de cheques recolhidos, acrescenta observação.
		if (StringUtils.isNotEmpty(obChequeRecolhido)) {
			if (obManifestoEntregaDocumento.length() != 0) { // se já existir alguma observação, quebra linha.
				obManifestoEntregaDocumento.append("\n");
			}
			obManifestoEntregaDocumento.append(obChequeRecolhido);
		}
		
		
		/** Altera observação de Documento do Manifesto de Entrega.*/
		// Se blRetencaoComprovanteEnt for informado no TypedFlatMap, altera o manifestoEntregaDocumento:
		Boolean blRetencaoComprovanteEnt = parameters.getBoolean("blRetencaoComprovanteEnt");
		if (blRetencaoComprovanteEnt != null) {
			manifestoEntregaDocumento.setBlRetencaoComprovanteEnt(blRetencaoComprovanteEnt);
			if (obManifestoEntregaDocumento.length() != 0) { // se já existir alguma observação, quebra linha.
				obManifestoEntregaDocumento.append("\n");
			}
			obManifestoEntregaDocumento.append(configuracoesFacade.getMensagem("retencaoComprovanteEntrega"));
		}
		
		manifestoEntregaDocumento.setObManifestoEntregaDocumento(obManifestoEntregaDocumento.toString());
				
		getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().update(manifestoEntregaDocumento);
		
		return null;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criterias,FindDefinition findDef) {
		ResultSetPage rsp = getFecharManifestoEntregaDAO().findPaginated(criterias,findDef);
		List newList = new ArrayList();
		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Object[] projections = (Object[])i.next();
			TypedFlatMap result = new TypedFlatMap();
			result.put("MANI_SG_FILIAL",projections[MANI_SG_FILIAL]);
			result.put("MANI_NR_MANIFESTO",projections[MANI_NR_MANIFESTO]);
			result.put("MANI_ID_MANIFESTO",projections[MANI_ID_MANIFESTO]);
			result.put("MANI_NM_FILIAL",projections[MANI_NM_FILIAL]);
			result.put("MANI_ID_FILIAL",projections[MANI_ID_FILIAL]);
			result.put("MANI_TP_MANIFESTO_ENTREGA",projections[MANI_TP_MANIFESTO_ENTREGA]);
			result.put("DH_EMISSAO_MANIFESTO",projections[DH_EMISSAO_MANIFESTO]);
			result.put("ROTA_ID",projections[ROTA_ID]);
			result.put("ROTA_NR",projections[ROTA_NR]);
			result.put("ROTA_DS",projections[ROTA_DS]);
			result.put("DH_CHEGADA_COLETA_ENTREGA",projections[DH_CHEGADA_COLETA_ENTREGA]);
			result.put("MT_NR_IDENT",projections[MT_NR_IDENT]);
			result.put("MT_NR_FROTA",projections[MT_NR_FROTA]);
			result.put("MT_ID",projections[MT_ID]);
			result.put("MTSR_NR_IDENT",projections[MTSR_NR_IDENT]);
			result.put("MTSR_NR_FROTA",projections[MTSR_NR_FROTA]);
			result.put("MTSR_ID",projections[MTSR_ID]);
			result.put("CC_ID_CC",projections[CC_ID_CC]);
			result.put("CC_NR",projections[CC_NR]);
			result.put("CC_FI_ID",projections[CC_FI_ID]);
			result.put("CC_FI_SG",projections[CC_FI_SG]);
			result.put("CC_FI_NM",projections[CC_FI_NM]);
			if (projections[CLI_TP] != null && projections[CLI_NR] != null) {
				result.put("CLI_NR",FormatUtils.formatIdentificacao((String)projections[CLI_TP],(String)projections[CLI_NR]));
				result.put("CLI_NOME",projections[CLI_NOME]);
			}
			result.put("TP_MANIFESTO",domainValueService.findDomainValueByValue("DM_TIPO_MANIFESTO_ENTREGA",(String)projections[TP_MANIFESTO]));
			result.put("TP_STATUS_MANIFESTO",domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO",(String)projections[TP_STATUS_MANIFESTO]));
			
			result.put("PENDENCIAS",projections[PENDENCIAS]);
			newList.add(result);
		}
		rsp.setList(newList);
		return rsp;
	}
	
	
	public Integer getRowCount(TypedFlatMap criterias) {
		return getFecharManifestoEntregaDAO().getRowCount(criterias);
	}
	
	public ResultSetPage findPaginatedPendencias(Long idManifesto,FindDefinition findDef) {
		ResultSetPage rsp = getFecharManifestoEntregaDAO().findPaginatedPendencias(idManifesto,findDef);
		for(Iterator i = rsp.getList().iterator(); i.hasNext();) {
			Map row = (Map)i.next();
			row.put("C_NR",FormatUtils.formatIdentificacao((DomainValue)row.get("C_TP"),(String)row.get("C_NR")));
			row.put("CR_NR",FormatUtils.formatIdentificacao((DomainValue)row.get("CR_TP"),(String)row.get("CR_NR")));
			
			/** Se blRetencaoComprovanteEnt for nulo, em vez de apresentar a situação,
			 * apresenta a mensagem de Retenção de comprovante de Entrega.
			 */
			Boolean blRetencaoComprovanteEnt = (Boolean)row.get("blRetencaoComprovanteEnt");
			if (blRetencaoComprovanteEnt == null) {
				row.put("dsPendencia",configuracoesFacade.getMensagem("retencaoComprovanteEntrega"));
			} else {
				DomainValue dv = (DomainValue)row.get("PENDENCIA");
				row.put("dsPendencia",dv.getDescription().getValue());
			}
		}
		return rsp;
	}
	public Integer getRowCountPendencias(Long idManifesto) {
		return getFecharManifestoEntregaDAO().getRowCountPendencias(idManifesto);
	}
	
	public void updateRegistroDocumentoEntrega(Long idDoctoServico, Boolean blRecolhido, String nrComprovante, DomainValue tpSituacaoRegistro, String obComprovante) {
		RegistroDocumentoEntrega bean = findUniqueRegistroDocumentoEntregaByIdDoctoServico(idDoctoServico);
		if (bean != null) {
			bean.setBlComprovanteRecolhido(blRecolhido);
			if (nrComprovante != null)
				bean.setNrComprovante(nrComprovante);

			if (blRecolhido != null && blRecolhido.booleanValue()) {
				bean.setObComprovante(obComprovante);
				bean.setTpSituacaoRegistro(tpSituacaoRegistro);
			}else{
				bean.setObComprovante(obComprovante);
			}
			getFecharManifestoEntregaDAO().store(bean);
		}
	}
	public void updateReciboReembolso(Long idDoctoServico, DomainValue tpSituacaoRecibo) {
		ReciboReembolso bean = findByIdReciboReembolsado(idDoctoServico);
		if (bean != null) {
			bean.setTpSituacaoRecibo(tpSituacaoRecibo);
			getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().update(bean);
		}
	}
	
	public void updateReciboReembolso(Long idDoctoServico, String obRecolhimento) {
		ReciboReembolso bean = findByIdReciboReembolsado(idDoctoServico);
		if (bean != null) {
			bean.setObRecolhimento(obRecolhimento);
			getFecharManifestoEntregaDAO().store(bean);
		}
	}
	
	public ReciboReembolso findByIdReciboReembolsado(Long idDoctoServico) {
		return getFecharManifestoEntregaDAO().findByIdReciboReembolsado(idDoctoServico);
	}
		
	public RegistroDocumentoEntrega findUniqueRegistroDocumentoEntregaByIdDoctoServico(Long idDoctoServico) {
		List rs = getFecharManifestoEntregaDAO().findRegistroDocumentoEntregaByIdDoctoServico(idDoctoServico);
		if (rs.isEmpty())
			return null;
		return (RegistroDocumentoEntrega)rs.get(0);
	}
	
	public void updateManifestoEntrega(Long idManifestoEntrega,DomainValue tpStatusManifesto, DateTime dhFechamento, Long idUsuarioFechamento) {
		ManifestoEntrega bean = (ManifestoEntrega)getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().load(ManifestoEntrega.class,idManifestoEntrega);
		bean.setDhFechamento(dhFechamento);
		if (idUsuarioFechamento != null) {
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(idUsuarioFechamento);
			bean.setUsuarioFechamento(usuario);
		}
		Manifesto manifesto = bean.getManifesto();
		manifesto.setTpStatusManifesto(tpStatusManifesto);
		getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().update(bean);
		getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().update(manifesto);
	}
	
	
	public void executeFechamentoManifesto(Long idManifesto) {
		Manifesto manifesto = (Manifesto)getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().get(Manifesto.class, idManifesto);
		ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
		
		// LMSA-7399 - 09/07/2018 - Inicio
		List<DoctoServico> lisDocsSemAssDigital =  doctoServicoService.findDoctosByIdManifestoEntregaSemAssinaturaDigital(idManifesto);
		if(lisDocsSemAssDigital != null && !lisDocsSemAssDigital.isEmpty()) {			
			String numeroManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " +
					FormatUtils.formatIntegerWithZeros(manifestoEntrega.getNrManifestoEntrega(),"00000000");
			throw new BusinessException("LMS-09156", new Object[] {numeroManifesto, this.obtemNumeroConhecimentos(lisDocsSemAssDigital)});
		}
		// LMSA-7399 - 09/07/2018 - Fim
		
		String tpManifestoEntrega = manifesto.getTpManifestoEntrega().getValue();
		
		// Regra 7: Para os tipos de manifesto [EN,ED,EP], o status do manifesto deve ser Descarga Concluída.
		if ("EN".equals(tpManifestoEntrega) || "ED".equals(tpManifestoEntrega) || "EP".equals(tpManifestoEntrega)) {
			if (!manifesto.getTpStatusManifesto().getValue().equals("DC"))
				throw new BusinessException("LMS-09108");
		}

		if( !tpManifestoEntrega.equals("EP")) {
	
			Integer counts;
			// Regra 2: Nã permite documentos pendentes de baixa.
			counts = getFecharManifestoEntregaDAO().getRowCountManifestoEntregaDocumentoPendenteBaixa(idManifesto);
			if (counts.compareTo(Integer.valueOf(0)) != 0)
				throw new BusinessException("LMS-09058");
	
			// Regra 3: Caso algum comprovante não tenha sido recolhido.
			counts = getFecharManifestoEntregaDAO().getRowCountRegistroDocumentoEntrega(idManifesto);
			if (counts.compareTo(Integer.valueOf(0)) != 0)
				throw new BusinessException("LMS-09061");
	
			// Regra 4: Caso algum recibo de reembolso não tenha sido recolhido e não tenha justificativa.
			counts = getFecharManifestoEntregaDAO().getRowCountReciboReembolso(idManifesto);
			if (counts.compareTo(Integer.valueOf(0)) != 0)
				throw new BusinessException("LMS-09062");
	
			// LMS-3169
			// Regra 5
			List result = getFecharManifestoEntregaDAO().listRNCByManifesto(idManifesto);
			
			if (!result.isEmpty()) {
				throw new BusinessException("LMS-09144",new String[]{createExceptionMessageBasedOnManifestosEntrega(result)});
			}			
				
			// Regra 5: Se houver documentos justificados, chama Workflow
			counts = getFecharManifestoEntregaDAO().getRowCountManifestoEntregaDocumentoJustificado(idManifesto);
			if (counts != null && counts.compareTo(Integer.valueOf(0)) != 0) {
				Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
				DateTime dhLiberacao = JTDateTimeUtils.getDataHoraAtual();
	
				String nrDocumento = new StringBuilder(manifestoEntrega.getFilial().getSgFilial()).append(" ").
							append(new DecimalFormat("00000000").format(manifestoEntrega.getNrManifestoEntrega().doubleValue())).toString();
	
				String dsProcesso = configuracoesFacade.getMensagem("avisoManifestoEntregaFechadoDocumentosNaoRecolhidos",new Object[]{nrDocumento}); 
				Long idProcesso = idManifesto;
	
				workflowPendenciaService.generatePendencia(idFilial,ConstantesWorkflow.NR901_DOCTO_NREC,idProcesso,dsProcesso,dhLiberacao);
			}
	
			// Regra 6: Disponibiliza documentos no terminal
			// Gera um evento de rastreabilidade para os documentos não entregues.
			List rs = getFecharManifestoEntregaDAO().findManifestoEntregaDocumentoNaoEntrega(idManifesto);
			if (!rs.isEmpty()) {
				Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
				DateTime dhEvento = JTDateTimeUtils.getDataHoraAtual();
				String tpDocumento = "MAE";
	
				for(Iterator i = rs.iterator(); i.hasNext();) {
					ManifestoEntregaDocumento med = (ManifestoEntregaDocumento)i.next();
	
					Long idDoctoServico = med.getDoctoServico().getIdDoctoServico();
	
					ManifestoEntrega me = med.getManifestoEntrega();
					Filial filial = me.getFilial();
	
					String nrDocumento = new StringBuilder(filial.getSgFilial()).append(" ").
											append(new DecimalFormat("00000000").format(me.getNrManifestoEntrega().doubleValue())).toString();
	
				}
			}
		}

		updateManifestoEntrega(idManifesto,new DomainValue("FE"),JTDateTimeUtils.getDataHoraAtual(),SessionUtils.getUsuarioLogado().getIdUsuario());

		//Inclui evento de manifesto fechado
		eventoManifestoService.generateEventoManifesto(manifesto, manifestoEntrega.getFilial(), ConstantesEntrega.STATUS_MANIFESTO_FECHADO);

		getFecharManifestoEntregaDAO().getAdsmHibernateTemplate().flush();

		if (hasNotaCreditoControleCarga(manifesto.getControleCarga())) {
			gerarNotaCreditoService.execute(manifesto.getControleCarga().getIdControleCarga());
					}
		
		// LMS-3512
		if("CR".equals(tpManifestoEntrega)) {
		    List<DoctoServico> doctoServicos = this.doctoServicoService.findByIdManifestoMoreRestrictions(manifesto.getIdManifesto());
		    
		    String nrDocumento = manifestoEntrega.getFilial().getSgFilial() + " " + manifestoEntrega.getNrManifestoEntrega();
		    
		    for(DoctoServico doctoServico : doctoServicos) {
		    	this.incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("31"), doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(), nrDocumento, new DateTime(), null, null, "MAE");
				}
		}
	}
	
   // LMSA-7399 - 09/07/2018 - Inicio
	/**
	 * Obtem os numeros do conhecimentos que não posuem assinatura digital,
	 * sendo o número máximo de 5 conhecimentos.
	 * @param lisDocsSemAssDigital
	 * @return codigos dos conhecimentos concatenados.
	 */
	private String obtemNumeroConhecimentos(List<DoctoServico> lisDocsSemAssDigital) {
		String retorno = "";
		int NumMax = Math.min(5, lisDocsSemAssDigital.size());
		for (int i = 0; i < NumMax; i++) {
			DoctoServico doc = lisDocsSemAssDigital.get(i);
			retorno = retorno + doc.getFilialByIdFilialOrigem().getSgFilial() 
					  + " " + doc.getNrDoctoServico() + ", ";
		}
		
		retorno = retorno.substring(0,retorno.length() -2 );
		if(lisDocsSemAssDigital.size() > 5) {
			retorno = retorno + " ...";
		}
		return retorno;
		
	}
	// LMSA-7399 - 09/07/2018 - Fim
	protected String createExceptionMessageBasedOnManifestosEntrega(List result) {
		StringBuilder message = new StringBuilder();
		for(Object ob : result){
			Object[] objs = (Object[]) ob;
			if ( objs[1] instanceof ManifestoEntregaDocumento ){
				ManifestoEntregaDocumento manifestoEntregaDocumento = (ManifestoEntregaDocumento) objs[1];
				DoctoServico docto = manifestoEntregaDocumento.getDoctoServico();
				message.append( createMessageLine(manifestoEntregaDocumento, docto));
			}
		}
		return message+".";
	}

	protected String createMessageLine(ManifestoEntregaDocumento manifestoEntregaDocumento,DoctoServico docto) {
		return "\n"+docto.getFilialByIdFilialOrigem().getSgFilial() + " " + docto.getNrDoctoServico() +" - " + manifestoEntregaDocumento.getTpEntregaParcial().getDescriptionAsString();
	}

	private boolean hasNotaCreditoControleCarga(ControleCarga controleCarga) {
		return controleCarga != null
				&& manifestoEntregaService.validateEmissaoNotaCreditoParaControleCarga(
						controleCarga.getIdControleCarga());
					}

	private Boolean isEnabledBaixa(String tpSituacao) {
		return (tpSituacao.equals("PBRC") || tpSituacao.equals("PBRE") || tpSituacao.equals("PBCO") || tpSituacao.equals("PBAI"));
	}

	private Boolean isEnabledEntrega(String tpSituacao) {
		return (tpSituacao.equals("PBRC") || tpSituacao.equals("PRCO") || tpSituacao.equals("PBCO") || tpSituacao.equals("PCOM"));
	}

	private Boolean isEnabledReembolso(String tpSituacao) {
		return (tpSituacao.equals("PBRC") || tpSituacao.equals("PRCO") || tpSituacao.equals("PBRE") || tpSituacao.equals("PREC"));
	}

	public void setFecharManifestoEntregaDAO(FecharManifestoEntregasDAO dao) {
		this.fecharManifestoEntregasDAO = dao;
	}
	private FecharManifestoEntregasDAO getFecharManifestoEntregaDAO() {
		return this.fecharManifestoEntregasDAO;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setManifestoEntregaDocumentoService(ManifestoEntregaDocumentoService manifestoEntregaDocumentoService) {
		this.manifestoEntregaDocumentoService = manifestoEntregaDocumentoService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public void setGerarNotaCreditoService(GerarNotaCreditoService gerarNotaCreditoService) {
		this.gerarNotaCreditoService = gerarNotaCreditoService;
	}
	public void setEventoManifestoService(EventoManifestoService eventoManifestoService) {
		this.eventoManifestoService = eventoManifestoService;
	}
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
	    this.doctoServicoService = doctoServicoService;
}
	
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
	    this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	
}
