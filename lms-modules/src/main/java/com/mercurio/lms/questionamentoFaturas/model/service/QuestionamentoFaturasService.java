package com.mercurio.lms.questionamentoFaturas.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;

import com.mercurio.adsm.core.util.VMProperties;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.service.BoletoService;
import com.mercurio.lms.contasreceber.model.service.CancelarBoletoService;
import com.mercurio.lms.contasreceber.model.service.DescontoService;
import com.mercurio.lms.contasreceber.model.service.EfetivarDescontoFaturaService;
import com.mercurio.lms.contasreceber.model.service.EfetivarDescontoService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.HistoricoBoletoService;
import com.mercurio.lms.contasreceber.model.service.MotivoDescontoService;
import com.mercurio.lms.contasreceber.model.service.ProrrogarVencimentoBoletoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.questionamentoFaturas.model.dao.QuestionamentoFaturasDAO;
import com.mercurio.lms.questionamentofaturas.model.AnexoQuestionamentoFatura;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.AnexosQuestionamentoUtils;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.WarningCollector;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.AnaliseCreditoClienteService;
import com.mercurio.lms.vendas.model.service.ClienteService;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;
/**
 * @spring.bean id="lms.questionamentoFaturas.questionamentoFaturasService"
 */

public class QuestionamentoFaturasService extends CrudService<QuestionamentoFatura, Long>{
	private static final String VL_LIMITE_CANCEL_NIVEL = "VL_LIMITE_CANCEL_NIVEL";
	private static final String VL_LIMITE_QUEST_NIVEL = "VL_LIMITE_QUEST_NIVEL";
	private static final String VL_LIMITE_ABAT_NIVEL = "VL_LIMITE_ABAT_NIVEL";
	private Logger log = LogManager.getLogger(this.getClass());
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	private QuestionamentoFaturasDAO questionamentoFaturasDAO;
	private HistoricoQuestionamentoFaturasService historicoQuestionamentoFaturasService;
	private AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService;
	private HistoricoFilialService historicoFilialService;
	private IntegracaoJmsService integracaoJmsService;
	private AnaliseCreditoClienteService analiseCreditoClienteService;
	private ParametroGeralService parametroGeralService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private DomainValueService domainValueService;
	private ConfiguracoesFacade configuracoesFacade;
	private ClienteService clienteService;
	private MotivoDescontoService motivoDescontoService;
	private FilialService filialService;
	private BoletoService boletoService;
	private EfetivarDescontoService efetivarDescontoService;
	private FaturaService faturaService;
	private ProrrogarVencimentoBoletoService prorrogarVencBoletoService;
	private CancelarBoletoService cancelarBoletoService;
	private UsuarioLMSService usuariolmsservice;
	private DescontoService descontoService;
	private HistoricoBoletoService historicoBoletoService;
	private EfetivarDescontoFaturaService efetivarDescontoFaturaService;

	private static final String LINE_SEPARATOR = VMProperties.LINE_SEPARATOR.getValue();
	private static final String MANTER_FORMATACAO = "<pre>";
	private static final String FECHAR_MANTER_FORMATACAO = "</pre>";

	public UsuarioLMSService getUsuariolmsservice() {
		return usuariolmsservice;
	}

	public void setUsuariolmsservice(UsuarioLMSService usuariolmsservice) {
		this.usuariolmsservice = usuariolmsservice;
	}

	/**
	 * Insere o Questionamento de Fatura, muda a situacao para Aguardando analise 1 e gera um histórico do mesmo
	 * @param idQuestionamentoFatura
	 * @param obHistorico
	 */
	public Long storeAnaliseQuestionamento(QuestionamentoFatura questionamentoFatura, String obHistorico) {
		/** Verifica se Questionamento Atual não está em Análise */
		if (questionamentoFatura.getIdQuestionamentoFatura() != null){
			Boolean existQuestionamentoEmAnalise = questionamentoFaturasDAO.findQuestionamentoEmAnalise(questionamentoFatura.getIdQuestionamentoFatura());
			if (Boolean.FALSE.equals(existQuestionamentoEmAnalise)){
				throw new BusinessException("LMS-42022", new Object[]{domainValueService.findDomainValueByValue("DM_SITUACAO_QUEST_FATURAS", "AA1").getDescriptionAsString()});
			}
		}

		/** Busca ultimo Usuario Apropriador referente ao Cliente */
		questionamentoFatura.setUsuarioApropriador(questionamentoFaturasDAO.findLastUserApropriador(clienteService.findById(questionamentoFatura.getCliente().getIdCliente())));
		this.store(questionamentoFatura);

		/** Verifica se existe algum anexo na sessão */
		this.validateAnexosInSessionToStore(questionamentoFatura);

    	/** Gera historico de cancelamento */
		DomainValue tpSituacaoHistorico = new DomainValue("QSO");
		historicoQuestionamentoFaturasService.generateHistoricoQuestionamento(
			 questionamentoFatura
			,tpSituacaoHistorico
			,obHistorico);

	    return questionamentoFatura.getIdQuestionamentoFatura();
	}

	private void validateAnexosInSessionToStore(QuestionamentoFatura questionamentoFatura) {
		/** Verifica se existe Anexos na sessão */
		if(AnexosQuestionamentoUtils.existAnexosInSession()) {
			/** Salva os anexos associados a esse Questionamento */
			List<AnexoQuestionamentoFatura> anexosInSession = AnexosQuestionamentoUtils.findAnexosInSession();
			for (AnexoQuestionamentoFatura anexoQuestionamentoFatura : anexosInSession) {
				anexoQuestionamentoFatura.setIdAnexoQuestionamentoFatura(null);
				anexoQuestionamentoFatura.setQuestionamentoFatura(questionamentoFatura);
				anexoQuestionamentoFaturasService.store(anexoQuestionamentoFatura);
			}
			/** Remove todos Anexos na sessão */
			AnexosQuestionamentoUtils.removeAnexosFromSession();
		}
	}

	/**
	 * Valida as regras de negocio da tela cad - botao submeter analise.
	 * @param criteria
	 */
	public void validateSubmeterAnalise(Map<String, Object> criteria, QuestionamentoFatura questionamentoFatura) {
		/* Verifica se a data de implantação no lms da filial cobradora do documento de serviço é menor ou igual
		 * a data atual e se é recalculo de frete
		 * 
		 * não precisa validar se a tela vier do store do lms - web  
		 * */
    	boolean blRecalcularFreteSol = MapUtils.getBooleanValue(criteria, "blRecalcularFreteSol");
    	Long idFilialCobradora = MapUtils.getLong(criteria,"idFilialCobradora");
    	String itensResumo = MapUtils.getString(criteria, "itensEmResumo");
    	String tpDocumento = MapUtils.getString(criteria, "tpDocumento");	
    	validateFilialImplantada(idFilialCobradora,blRecalcularFreteSol,criteria.containsKey("isStoreQuestionamento"),tpDocumento,itensResumo);
    	
		//** Verifica se os campos foram marcados *//
		if (Boolean.FALSE.equals(MapUtils.getBoolean(criteria, "blBaixaTitCancelSol")) &&
			Boolean.FALSE.equals(MapUtils.getBoolean(criteria, "blSustarProtestoSol")) &&
			Boolean.FALSE.equals(MapUtils.getBoolean(criteria, "blProrrogaVencimentoSol")) &&
			Boolean.FALSE.equals(MapUtils.getBoolean(criteria, "blRecalcularFreteSol")) &&
			Boolean.FALSE.equals(MapUtils.getBoolean(criteria, "blConcedeAbatimentoSol"))){
			throw new BusinessException("LMS-42012");
		}

		//** Verifica a existencia de boletos vinculados */
		if (Boolean.TRUE.equals(MapUtils.getBoolean(criteria, "blProrrogaVencimentoSol")) ||
             Boolean.TRUE.equals(MapUtils.getBoolean(criteria, "blBaixaTitCancelSol"))){
			
			Boolean validateFaturaEmBoleto = faturaService.validateFaturaEmBoleto(questionamentoFatura.getFatura().getIdFatura());
			
			if (!"BL".equals(questionamentoFatura.getFatura().getTpSituacaoFatura().getValue()) || !validateFaturaEmBoleto){
				throw new BusinessException("LMS-42013");
			}
		}
		
		//** Verifica data de vencimento maior que data atual */
		if (Boolean.TRUE.equals(MapUtils.getBoolean(criteria, "blProrrogaVencimentoSol"))){
			YearMonthDay dtVencimentoSolicitado = (YearMonthDay) MapUtils.getObject(criteria, "dtVencimentoSolicitado");
			YearMonthDay dtVencimentoDocumento = (YearMonthDay) MapUtils.getObject(criteria, "dtVencimentoDocumento");
			if (dtVencimentoSolicitado == null ||
			    JTDateTimeUtils.comparaData(dtVencimentoSolicitado, JTDateTimeUtils.getDataAtual()) <= 0 ||
 				JTDateTimeUtils.comparaData(dtVencimentoSolicitado, dtVencimentoDocumento) <= 0	){
				throw new BusinessException("LMS-42014");
			}
		}
		//** Verifica o se informou motivo da prorrogacao */
		if (Boolean.TRUE.equals(MapUtils.getBoolean(criteria, "blProrrogaVencimentoSol"))){
			if (MapUtils.getString(criteria, "idMotivoProrrogacaoVcto") == null) {
				throw new BusinessException("LMS-42015");
			}
			
			try{
				int limiteDias = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro("NR_DIAS_LIMITE_VENCIMENTO", false)).intValue();
				int intervalo = JTDateTimeUtils.getIntervalInDays(MapUtilsPlus.getYearMonthDay(criteria, "dtVencimentoDocumento"), MapUtilsPlus.getYearMonthDay(criteria, "dtVencimentoSolicitado"));
				
				if (intervalo > limiteDias){
					throw new BusinessException("LMS-42040", new Object[]{limiteDias});
		}
			}catch(NullPointerException npe){
				//Se rolar uma NPE aqui é porque alguma das datas nao foi informada ou com formato errado 
				log.error(npe);
			}
		}
		//** Verifica boletos em protesto */
		
		Boolean validateFaturaEmBoleto = faturaService.validateFaturaEmBoleto(questionamentoFatura.getFatura().getIdFatura());

		if (Boolean.TRUE.equals(MapUtils.getBoolean(criteria, "blSustarProtestoSol"))){
			if (!"BL".equals(MapUtils.getString(criteria, "status")) || !validateFaturaEmBoleto){
				throw new BusinessException("LMS-42016");
			}
			//** Verifica motivos da sustacao */
			if (MapUtils.getString(criteria, "idMotivoSustacaoProtesto") == null){
				throw new BusinessException("LMS-42017");
			}
		}
		//** Verifica motivos da baixa */
		if (Boolean.TRUE.equals(MapUtils.getBoolean(criteria, "blBaixaTitCancelSol"))){
			if (MapUtils.getString(criteria, "idMotivoCancelamento") == null){
				throw new BusinessException("LMS-42018");
			}
		}
		
		
	}

	public String convertDocServicoDocQuest(String tpDoctoServico){
		String[] tpDctoQuest = {"N","C","E","S","A","B"};
		String[] tpDctoServicos = {"NFT","CTR","CTE","NFS","NSE","NTE"};

		Integer i = ArrayUtils.indexOf(tpDctoServicos,tpDoctoServico);
		if( i > -1 ){
			return tpDctoQuest[i];
	}
		return null;
	}

	/**
	 * Obtem o valor do tipo de documento 
	 * @param tpDocumento
	 * @return Long
	 */
	public String findValorTipoDocumento(String tpDocumento){
		
		String tipo = null; 
		if("C".equals(tpDocumento)){
			tipo = "0";
		}else 
			if("E".equals(tpDocumento)){
				tipo = "1";
		}else
			if("N".equals(tpDocumento)||"S".equals(tpDocumento)){
				tipo = "2";
		}else
			if("A".equals(tpDocumento)||"B".equals(tpDocumento)){
				tipo = "3";
		}else{
			throw new IllegalArgumentException("tpDocumento - Possui um valor inválido");
		}		
		return tipo;
	}
	
	private void validateConhecimento(QuestionamentoFatura qf) {
		DoctoServico doctoServico = qf.getDoctoServico();
		/** Verifica se existe o conhecimento */
		if(doctoServico == null) {
			throw new BusinessException("LMS-42001");
		}
		/** Verifica se conhecimento já foi cancelado */
		
		if("CA".equals(doctoServico.getTpSituacaoConhecimento().getValue())) {
			throw new BusinessException("LMS-42002");
		}

		/** Verifica se conhecimento esta com situacao pendente ou em carteira */
		String tpSituacaoCobranca = doctoServico.getDevedorDocServFats().iterator().next().getTpSituacaoCobranca().getValue();
		if(!"P".equals(tpSituacaoCobranca) && !"C".equals(tpSituacaoCobranca)) {
			throw new BusinessException("LMS-42003");
		}
		/** Verifica se filial usuario logado pertence a filial responsavel pela cobranca */
		Filial filialMatriz = historicoFilialService.findFilialMatriz(SessionUtils.getFilialSessao().getEmpresa().getIdEmpresa());
		if(!filialMatriz.equals(SessionUtils.getFilialSessao())) {
			if(!SessionUtils.getFilialSessao().getSgFilial().equals(qf.getDoctoServico().getFilialByIdFilialOrigem().getSiglaNomeFilial())){
				throw new BusinessException("LMS-42004");
			}
		}

//		/** Verifica a existencia de analises em andamento */
		Map<String, Object> toReturn = questionamentoFaturasDAO.findQuestionamentoEmAnalise("C", doctoServico.getIdDoctoServico(), null);
		if (toReturn != null){
			if (!"AA1".equals(MapUtilsPlus.getStringOnMap(toReturn, "tpSituacao", "value", null))){
				new WarningCollector(configuracoesFacade.getMensagem("LMS-42005"));
			}
		}
	}

	public void validateFilialImplantada(Long idFilial,Boolean blRecalcularFreteSol,Boolean isStoreQuestionamento,String tpDocumento, String itensResumo ){
		Filial filialCobradora = new Filial();
		filialCobradora.setIdFilial(idFilial);
		if(filialService.findIsFilialLMS(filialCobradora) && !blRecalcularFreteSol && !isStoreQuestionamento){
			if( !"R".equals(tpDocumento)){
	    		throw new BusinessException("LMS-42042");
			}
			
			if ( "R".equals(tpDocumento) && (itensResumo == null || "N".equals(itensResumo))){
	    		throw new BusinessException("LMS-42042");
			}
    	}
	}

	public Boolean validateFatura(Fatura fatura){
		QuestionamentoFatura questionamentoFatura = questionamentoFaturasDAO.findByIdFilialCobradoraRomaNumeroSituacao(fatura);
		if(questionamentoFatura != null) {
			return true;
		}
		return false;
	}
	
	public Boolean validateFilialImplantada(Long idFilial,String tpDocumento, String itensResumo ){
		Filial filialCobradora = new Filial();
		filialCobradora.setIdFilial(idFilial);
		if(filialService.findIsFilialLMS(filialCobradora) ){
			if( !"R".equals(tpDocumento)){
	    		return true;
			}
			
			if ( "R".equals(tpDocumento) && (itensResumo == null || "N".equals(itensResumo))){
	    		return true;
			}
    	}
		return false;
	}
	
	public Boolean validateFilialNaoImplantada(Long idFilial,String tpDocumento, String itensResumo ){
		Filial filialCobradora = new Filial();
		filialCobradora.setIdFilial(idFilial);
		if(!filialService.findIsFilialLMS(filialCobradora) ){
			if ( "R".equals(tpDocumento) && ("S".equals(itensResumo))){
	    		return true;
			}
    	}
		return false;
	}

	private void validateRomaneio(QuestionamentoFatura questionamentoFatura) {
		Fatura fatura = questionamentoFatura.getFatura();
		
		/** Verifica se existe o romaneio */
		if(fatura == null) {
			throw new BusinessException("LMS-42006");
		}
		/** Verifica se romaneio status esta emitido ou boleto */
		
		if(!"EM".equals(fatura.getTpSituacaoFatura().getValue()) && !"BL".equals(fatura.getTpSituacaoFatura().getValue())) {
			throw new BusinessException("LMS-42007");
		}
		/** Verifica bloquetes com status EMITIDO, BANCO, BANCO COM PROTESTO*/
		
		if ("BL".equals(fatura.getTpSituacaoFatura().getValue())) {
			Boolean validateFaturaEmBoleto = faturaService.validateFaturaEmBoleto(questionamentoFatura.getFatura().getIdFatura());

			if (!validateFaturaEmBoleto){
				throw new BusinessException("LMS-42008");				
			}
		}
		/** Verifica se filial usuario logado pertence a filial responsavel pela cobranca do romaneio  */
		if(SessionUtils.getFilialSessao() == null){
			throw new BusinessException("ADSM_SESSION_EXPIRED_EXCEPTION_KEY");
		}
		
		if (!SessionUtils.isFilialSessaoMatriz()){
			if(!SessionUtils.getFilialSessao().getSgFilial().equals(fatura.getFilialByIdFilial().getSgFilial())){
				throw new BusinessException("LMS-42009");
			}
		}

//		/** Verifica a existencia de analises em andamento */
		Map<String, Object> toReturn = questionamentoFaturasDAO.findQuestionamentoEmAnalise("R", null, questionamentoFatura.getFatura().getIdFatura());
		if (toReturn != null){
			if (!"AA1".equals(MapUtilsPlus.getStringOnMap(toReturn, "tpSituacao", "value", null))){
				new WarningCollector(configuracoesFacade.getMensagem("LMS-42010"));
			}
		}
		
	}

	public Serializable storeGenericQuestionamentoFatura( QuestionamentoFatura questionamentoFatura,
															Filial filialCobradora,
															Cliente cliente,
															BigDecimal valor, Long idQuestionamentoGaturaAntigo){
		
		
		if(idQuestionamentoGaturaAntigo != null){
			QuestionamentoFatura questionamentoFaturaAntigo = findById(idQuestionamentoGaturaAntigo);
			
			 
			if ("AA1".equals(questionamentoFaturaAntigo.getTpSituacao().getValue())){
			// se estiver aguardando analise, simplesmente atualiza os valores
				questionamentoFaturaAntigo.setBlConcedeAbatimentoSol(questionamentoFatura.getBlConcedeAbatimentoSol());
				questionamentoFaturaAntigo.setMotivoDesconto(questionamentoFatura.getMotivoDesconto());
				questionamentoFaturaAntigo.setTpSetorCausadorAbatimento(questionamentoFatura.getTpSetorCausadorAbatimento());
				questionamentoFaturaAntigo.setObAbatimento(questionamentoFatura.getObAbatimento());
				questionamentoFaturaAntigo.setObAcaoCorretiva(questionamentoFatura.getObAcaoCorretiva());
				questionamentoFaturaAntigo.setVlAbatimentoSolicitado(questionamentoFatura.getVlAbatimentoSolicitado());
				questionamentoFaturaAntigo.setDoctoServico(questionamentoFatura.getDoctoServico());
				questionamentoFaturaAntigo.setTpDocumento(questionamentoFatura.getTpDocumento());
				questionamentoFaturaAntigo.setFatura(questionamentoFatura.getFatura());
				questionamentoFaturaAntigo.setNrBoleto(questionamentoFatura.getNrBoleto());
				questionamentoFaturaAntigo.setBlProrrogaVencimentoSol(questionamentoFatura.getBlProrrogaVencimentoSol());
				questionamentoFaturaAntigo.setDtVencimentoSolicitado(questionamentoFatura.getDtVencimentoSolicitado());
				questionamentoFaturaAntigo.setMotivoProrrogacaoVcto(questionamentoFatura.getMotivoProrrogacaoVcto());
				questionamentoFaturaAntigo.setMotivoCancelamento(questionamentoFatura.getMotivoCancelamento());
				questionamentoFaturaAntigo.setDtVencimentoDocumento(questionamentoFatura.getDtVencimentoDocumento());
				questionamentoFaturaAntigo.setBlBaixaTitCancelSol(questionamentoFatura.getBlBaixaTitCancelSol());
				questionamentoFaturaAntigo.setDtEmissaoDocumento(questionamentoFatura.getDtEmissaoDocumento());
				questionamentoFatura = questionamentoFaturaAntigo;
			}else if (!"ACO".equals(questionamentoFaturaAntigo.getTpSituacao().getValue()) && !"CAN".equals(questionamentoFaturaAntigo.getTpSituacao().getValue())){
			// se nao estiver concluida nem cancelada, cancela a analise
				storeCancelarQuestionamento(questionamentoFaturaAntigo, "");
			}
			
		}
		
		questionamentoFatura.setFilialCobradora(filialCobradora);
		questionamentoFatura.setFilialSolicitante( SessionUtils.getFilialSessao() );
		questionamentoFatura.setCliente(cliente);
		UsuarioLMS usuariolms = new UsuarioLMS();
		usuariolms.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		questionamentoFatura.setUsuarioSolicitante(usuariolms);
		questionamentoFatura.setDtEmissaoDocumento(questionamentoFatura.getDtEmissaoDocumento());
		questionamentoFatura.setVlDocumento(valor);
		questionamentoFatura.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());
		questionamentoFatura.setTpSituacao(new DomainValue("AA1")); 
		
		if (StringUtils.isBlank(SessionUtils.getUsuarioLogado().getDsEmail()) ) {
			String emailPadraoFilial = (String) conteudoParametroFilialService.findConteudoByNomeParametro( SessionUtils.getFilialSessao().getIdFilial() , "EMAIL_PADRAO_FAT", false);
			if (StringUtils.isBlank(emailPadraoFilial)){
				throw new BusinessException("LMS-42030",new Object[]{ SessionUtils.getFilialSessao().getSgFilial()});
			}
			questionamentoFatura.setDsEmailRetorno(emailPadraoFilial);
		} else {
			questionamentoFatura.setDsEmailRetorno(SessionUtils.getUsuarioLogado().getDsEmail());
		}
		if("R".equals(questionamentoFatura.getTpDocumento().getValue())){
			this.validateRomaneio(questionamentoFatura);
		}else{
			this.validateConhecimento(questionamentoFatura);
		}
		
		
		validateSubmeterAnalise(questionamentoFatura);
		
		boolean isInsert = questionamentoFatura.getIdQuestionamentoFatura() == null;
		
		Long o = null;
		if(idQuestionamentoGaturaAntigo == null){
			o = (Long) this.store(questionamentoFatura);
		} else {
			o = (Long) questionamentoFaturasDAO.store(questionamentoFatura);
		}
		
		if(isInsert){
			DomainValue tpSituacaoHistorico = new DomainValue("QSO");
			historicoQuestionamentoFaturasService.generateHistoricoQuestionamento(
				 questionamentoFatura
				,tpSituacaoHistorico
				,null);
		}
		
		return o;
	}

	public Serializable storeGenericQuestionamentoFatura( QuestionamentoFatura questionamentoFatura,
			Filial filialCobradora,
			Cliente cliente,
			BigDecimal valor, Long idQuestionamentoGaturaAntigo, String obHistorico){


		if(idQuestionamentoGaturaAntigo != null){
			QuestionamentoFatura questionamentoFaturaAntigo = findById(idQuestionamentoGaturaAntigo);
			if (questionamentoFaturaAntigo != null){
			if ("AA1".equals(questionamentoFaturaAntigo.getTpSituacao().getValue())){
				// se estiver aguardando analise, simplesmente atualiza os valores
				questionamentoFaturaAntigo.setBlConcedeAbatimentoSol(questionamentoFatura.getBlConcedeAbatimentoSol());
				questionamentoFaturaAntigo.setMotivoDesconto(questionamentoFatura.getMotivoDesconto());
				questionamentoFaturaAntigo.setTpSetorCausadorAbatimento(questionamentoFatura.getTpSetorCausadorAbatimento());
				questionamentoFaturaAntigo.setObAbatimento(questionamentoFatura.getObAbatimento());
				questionamentoFaturaAntigo.setObAcaoCorretiva(questionamentoFatura.getObAcaoCorretiva());
				questionamentoFaturaAntigo.setVlAbatimentoSolicitado(questionamentoFatura.getVlAbatimentoSolicitado());
				questionamentoFaturaAntigo.setDoctoServico(questionamentoFatura.getDoctoServico());
				questionamentoFaturaAntigo.setTpDocumento(questionamentoFatura.getTpDocumento());
				questionamentoFaturaAntigo.setFatura(questionamentoFatura.getFatura());
				questionamentoFaturaAntigo.setNrBoleto(questionamentoFatura.getNrBoleto());
				questionamentoFaturaAntigo.setBlProrrogaVencimentoSol(questionamentoFatura.getBlProrrogaVencimentoSol());
				questionamentoFaturaAntigo.setDtVencimentoSolicitado(questionamentoFatura.getDtVencimentoSolicitado());
				questionamentoFaturaAntigo.setMotivoProrrogacaoVcto(questionamentoFatura.getMotivoProrrogacaoVcto());
				questionamentoFaturaAntigo.setMotivoCancelamento(questionamentoFatura.getMotivoCancelamento());
				questionamentoFaturaAntigo.setDtVencimentoDocumento(questionamentoFatura.getDtVencimentoDocumento());
				questionamentoFaturaAntigo.setBlBaixaTitCancelSol(questionamentoFatura.getBlBaixaTitCancelSol());
				questionamentoFaturaAntigo.setDtEmissaoDocumento(questionamentoFatura.getDtEmissaoDocumento());
				questionamentoFatura = questionamentoFaturaAntigo;
			}else if (!"ACO".equals(questionamentoFaturaAntigo.getTpSituacao().getValue()) && !"CAN".equals(questionamentoFaturaAntigo.getTpSituacao().getValue())){
				// se nao estiver concluida nem cancelada, cancela a analise
				storeCancelarQuestionamento(questionamentoFaturaAntigo, obHistorico);
			}
		}
		}

		questionamentoFatura.setFilialCobradora(filialCobradora);
		questionamentoFatura.setFilialSolicitante( SessionUtils.getFilialSessao() );
		questionamentoFatura.setCliente(cliente);
		UsuarioLMS usuariolms = new UsuarioLMS();
		usuariolms.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		questionamentoFatura.setUsuarioSolicitante(usuariolms);
		questionamentoFatura.setDtEmissaoDocumento(questionamentoFatura.getDtEmissaoDocumento());
		questionamentoFatura.setVlDocumento(valor);
		questionamentoFatura.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());
		questionamentoFatura.setTpSituacao(new DomainValue("AA1")); 

		if (StringUtils.isBlank(SessionUtils.getUsuarioLogado().getDsEmail()) ) {
			String emailPadraoFilial = (String) conteudoParametroFilialService.findConteudoByNomeParametro( SessionUtils.getFilialSessao().getIdFilial() , "EMAIL_PADRAO_FAT", false);
			if (StringUtils.isBlank(emailPadraoFilial)){
				throw new BusinessException("LMS-42030",new Object[]{ SessionUtils.getFilialSessao().getSgFilial()});
			}
			questionamentoFatura.setDsEmailRetorno(emailPadraoFilial);
		} else {
			questionamentoFatura.setDsEmailRetorno(SessionUtils.getUsuarioLogado().getDsEmail());
		}
		if("R".equals(questionamentoFatura.getTpDocumento().getValue())){
			this.validateRomaneio(questionamentoFatura);
		}else{
			this.validateConhecimento(questionamentoFatura);
		}


		validateSubmeterAnalise(questionamentoFatura);

		boolean isInsert = questionamentoFatura.getIdQuestionamentoFatura() == null;

		Long o = null;
		if(idQuestionamentoGaturaAntigo == null){
			o = (Long) this.store(questionamentoFatura);
		} else {
			o = (Long) questionamentoFaturasDAO.store(questionamentoFatura);
		}

		if(isInsert){
			DomainValue tpSituacaoHistorico = new DomainValue("QSO");
			historicoQuestionamentoFaturasService.generateHistoricoQuestionamento(
					questionamentoFatura
					,tpSituacaoHistorico
					,obHistorico);
		}

		return o;
	}
	
	
	
	

	private void validateSubmeterAnalise(QuestionamentoFatura questionamentoFatura) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idQuestionamentoFatura", questionamentoFatura.getIdQuestionamentoFatura());
		map.put("idFilialCobradora", questionamentoFatura.getFilialCobradora().getIdFilial());
		map.put("sgFilialCobradora", questionamentoFatura.getFilialCobradora().getSgFilial());
		map.put("nmFilialCobradora", questionamentoFatura.getFilialCobradora().getPessoa().getNmFantasia());

		map.put("idFilialSolicitante", questionamentoFatura.getFilialSolicitante().getIdFilial());
		map.put("sgFilialSolicitante", questionamentoFatura.getFilialSolicitante().getSgFilial());
		map.put("nmFilialSolicitante", questionamentoFatura.getFilialSolicitante().getPessoa().getNmFantasia());

		map.put("idCliente", questionamentoFatura.getCliente().getIdCliente());
		Pessoa pessoa = questionamentoFatura.getCliente().getPessoa();
		map.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao()));
		map.put("nmCliente", pessoa.getNmPessoa());

		UsuarioLMS usuarioSolicitante = usuariolmsservice.findById(questionamentoFatura.getUsuarioSolicitante().getIdUsuario());
		questionamentoFatura.setUsuarioSolicitante(usuarioSolicitante);
		
		map.put("idUsuarioSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getIdUsuario());
		map.put("nrMatriculaSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getNrMatricula());
		map.put("nmUsuarioSolicitante", questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
		UsuarioLMS usuarioApropriador = questionamentoFatura.getUsuarioApropriador();
		if(usuarioApropriador != null) {
			map.put("idUsuarioApropriador", usuarioApropriador.getUsuarioADSM().getIdUsuario());
			map.put("nrMatriculaApropriador", usuarioApropriador.getUsuarioADSM().getNrMatricula());
			map.put("nmUsuarioApropriador", usuarioApropriador.getUsuarioADSM().getNmUsuario());
		}
		MotivoDesconto motivoDesconto = questionamentoFatura.getMotivoDesconto();
		if(motivoDesconto != null) {
			map.put("idMotivoDesconto", motivoDesconto.getIdMotivoDesconto());
		}
		MotivoOcorrencia motivoCancelamento = questionamentoFatura.getMotivoCancelamento();
		if(motivoCancelamento != null) {
			map.put("idMotivoCancelamento", motivoCancelamento.getIdMotivoOcorrencia());
		}
		MotivoOcorrencia motivoSustacaoProtesto = questionamentoFatura.getMotivoSustacaoProtesto();
		if(motivoSustacaoProtesto != null) {
			map.put("idMotivoSustacaoProtesto", motivoSustacaoProtesto.getIdMotivoOcorrencia());
		}
		MotivoOcorrencia motivoProrrogacaoVcto = questionamentoFatura.getMotivoProrrogacaoVcto();
		if(motivoProrrogacaoVcto != null) {
			map.put("idMotivoProrrogacaoVcto", motivoProrrogacaoVcto.getIdMotivoOcorrencia());
		}
		map.put("tpDocumento", questionamentoFatura.getTpDocumento().getValue());
		
		map.put("blConcedeAbatimentoSol", questionamentoFatura.getBlConcedeAbatimentoSol());
		map.put("blProrrogaVencimentoSol", questionamentoFatura.getBlProrrogaVencimentoSol());
		map.put("blSustarProtestoSol", questionamentoFatura.getBlSustarProtestoSol());
		map.put("blBaixaTitCancelSol", questionamentoFatura.getBlBaixaTitCancelSol());
		map.put("blRecalcularFreteSol", questionamentoFatura.getBlRecalcularFreteSol());

		/** Booleanos */
		map.put("blConcedeAbatimentoAprov", BooleanUtils.toString(questionamentoFatura.getBlConcedeAbatimentoAprov(), "S", "N", null));
		map.put("blProrrogaVencimentoAprov", BooleanUtils.toString(questionamentoFatura.getBlProrrogaVencimentoAprov(), "S", "N", null));
		map.put("blSustarProtestoAprov", BooleanUtils.toString(questionamentoFatura.getBlSustarProtestoAprov(), "S", "N", null));
		map.put("blBaixaTitCancelAprov", BooleanUtils.toString(questionamentoFatura.getBlBaixaTitCancelAprov(), "S", "N", null));
		map.put("blRecalcularFreteAprov", BooleanUtils.toString(questionamentoFatura.getBlRecalcularFreteAprov(), "S", "N", null));

		map.put("dtEmissaoDocumento", questionamentoFatura.getDtEmissaoDocumento());
		map.put("vlDocumento", questionamentoFatura.getVlDocumento());
		DomainValue tpSituacaoBoleto = questionamentoFatura.getTpSituacaoBoleto();
		if(tpSituacaoBoleto != null) {
			map.put("tpSituacaoBoleto", tpSituacaoBoleto.getValue());
		}
		map.put("dtVencimentoDocumento", questionamentoFatura.getDtVencimentoDocumento());
		map.put("nrBoleto", questionamentoFatura.getNrBoleto());
		map.put("dhSolicitacao", questionamentoFatura.getDhSolicitacao());
		map.put("dhConclusao", questionamentoFatura.getDhConclusao());
		map.put("vlAbatimentoSolicitado", questionamentoFatura.getVlAbatimentoSolicitado());
		map.put("dtVencimentoSolicitado", questionamentoFatura.getDtVencimentoSolicitado());
		DomainValue tpSituacao = questionamentoFatura.getTpSituacao();
		if(tpSituacao != null) {
			map.put("tpSituacao", tpSituacao.getValue());
		}
		DomainValue tpSetorCausadorAbatimento = questionamentoFatura.getTpSetorCausadorAbatimento();
		if(tpSetorCausadorAbatimento != null) {
			map.put("tpSetorCausadorAbatimento", tpSetorCausadorAbatimento.getValue());
		}
		map.put("dsChaveLiberacao", questionamentoFatura.getDsChaveLiberacao());
		map.put("dsEmailRetorno", questionamentoFatura.getDsEmailRetorno());
		map.put("obAcaoCorretiva", questionamentoFatura.getObAcaoCorretiva());
		map.put("obAbatimento", questionamentoFatura.getObAbatimento());
		
		map.put("isStoreQuestionamento", Boolean.TRUE);

		if (questionamentoFatura.getFatura() != null) {
			map.put("status", questionamentoFatura.getFatura().getTpSituacaoFatura().getValue());
			map.put("itensEmResumo", questionamentoFatura.getFatura().getBlConhecimentoResumo());
		} else if (questionamentoFatura.getDoctoServico() != null) {
			
			map.put("status", questionamentoFatura.getDoctoServico().getTpSituacaoConhecimento().getValue());
		}
		
		validateSubmeterAnalise(map, questionamentoFatura);
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage<QuestionamentoFatura> findPaginated(PaginatedQuery paginatedQuery) {
    	//Nao permite a consulta sem nenhum filtro informado
		
		String params[] = { "idQuestionamentoFatura", "tpDocumento",
				"sgFilialOrigem", "nrDocumento", "dhSolicitacaoInicial",
				"dhSolicitacaoFinal", "idFilialCobradora",
				"idUsuarioSolicitante", "tpSituacao", "idCliente",
				"nrIdentificacaoCustom", "dhConclusaoInicial",
				"dhConclusaoFinal", "idUsuarioApropriador", "blSemApropriador" };
		List listParams = Arrays.asList(params);
		
		
    	boolean isEmpty = true;
    	for (Entry entry:paginatedQuery.getCriteria().entrySet()){
    		Object key = entry.getKey();

    		if (key.equals("_currentPage") || key.equals("_order") 
				||key.equals("_pageSize") || key.equals("blSemApropriador")){
    			continue;
    		}
			if (entry.getValue() != null && listParams.contains(key)) {
				isEmpty = false;
				break;
			}
    	}
    	if (isEmpty) throw new BusinessException("LMS-42041");
		
		return questionamentoFaturasDAO.findPaginated(paginatedQuery);
	}

	public QuestionamentoFatura findById(Long id) {
		return questionamentoFaturasDAO.findById(id);
	}

	public QuestionamentoFatura findByIdBasic(Long id) {
		return questionamentoFaturasDAO.findByIdBasic(id);
	}

	@SuppressWarnings("rawtypes")
	public Serializable store(QuestionamentoFatura questionamentoFatura) {
		
		if(questionamentoFatura.getIdQuestionamentoFatura() != null){
				if("C".equals(questionamentoFatura.getTpDocumento().getValue())){
					descontoService.executeSynchronizeQuestionamentoFatura(questionamentoFatura);
				} else if("R".equals(questionamentoFatura.getTpDocumento().getValue())){
					faturaService.executeSynchronizeQuestionamentoFatura(questionamentoFatura);
			}
		}
		return questionamentoFaturasDAO.store(questionamentoFatura);
		
	}

	/**
	 * Valida inicialização da Análise.
	 * @param idQuestionamentoFatura
	 */
	public Map<String, Object> storeIniciarAnalise(Long idQuestionamentoFatura) {
		QuestionamentoFatura questionamentoFatura = this.findById(idQuestionamentoFatura);
    	/** Verifica a situação do Questionamento para iniciar analise */
    	String tpSituacao = questionamentoFatura.getTpSituacao().getValue();
		if ("ACO".equals(tpSituacao) || "CAN".equals(tpSituacao) || !tpSituacao.startsWith("AA")){
			throw new BusinessException("LMS-42022", new Object[]{questionamentoFatura.getTpSituacao().getDescriptionAsString()});
		}
		DomainValue tpSituacaoAnalise = domainValueService.findDomainValueByValue("DM_SITUACAO_QUEST_FATURAS", "EA".concat(StringUtils.right(tpSituacao, 1)));

    	/** Altera Situacao e UsuarioApropriador do Questionamento */
		UsuarioLMS usuarioLMS = new UsuarioLMS();
		usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		questionamentoFatura.setTpSituacao(tpSituacaoAnalise);
		questionamentoFatura.setUsuarioApropriador(usuarioLMS);
		this.store(questionamentoFatura);

    	/** Gera historico de questionamento */
		historicoQuestionamentoFaturasService.generateHistoricoQuestionamento(
			 questionamentoFatura
			,tpSituacaoAnalise
			,configuracoesFacade.getMensagem("emAnalise"));

    	/** Agrupa os Questionamentos que ainda estão Aguardando Análise */
    	if("AA1".equals(tpSituacao)) {
    		Pessoa pessoa = questionamentoFatura.getCliente().getPessoa();
    		if("J".equals(pessoa.getTpPessoa().getValue())) {
    			this.updateApropriadorGrupoByPessoaJuridica(usuarioLMS.getIdUsuario(), tpSituacao, pessoa.getNrIdentificacao());
    		} else {
    			this.updateApropriadorGrupoByPessoaFisica(usuarioLMS.getIdUsuario(), tpSituacao, questionamentoFatura.getCliente().getIdCliente());
    		}
    	}

    	Map<String, Object> toReturn = new HashMap<String, Object>();
    	toReturn.put("tpSituacao", tpSituacaoAnalise.getValue());
    	return toReturn;
	}

	/**
	 * Valida Processo de Conclusão da Análise.
	 * @param criteria
	 * @param dataHoraAtual 
	 */
	public void storeValidateConcluirAnalise(Map<String, Object> criteria, DateTime dataHoraAtual) {
		QuestionamentoFatura questionamentoFatura = this.findById((Long) criteria.get("idQuestionamentoFatura"));
		String tpSituacao = questionamentoFatura.getTpSituacao().getValue();
		String analiseNivel = StringUtils.right(tpSituacao, 1);
		
		String obHistorico = (String)criteria.get("obHistorico");
    
    	/** Verifica a situação do Questionamento para concluir analise */
		if ("ACO".equals(tpSituacao) || "CAN".equals(tpSituacao) || !tpSituacao.startsWith("EA")){
			throw new BusinessException("LMS-42022", new Object[]{questionamentoFatura.getTpSituacao().getDescriptionAsString()});
		}

		// LMS-1753 - Verifica situação da fatura e solicitações no questionamento
		Boolean questionamentoSolicitado = false;
		
		Boolean blConcedeAbatimentoAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blConcedeAbatimentoAprov"), "S", "N", null);
		Boolean blProrrogaVencimentoAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blProrrogaVencimentoAprov"), "S", "N", null);
		Boolean blSustarProtestoAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blSustarProtestoAprov"), "S", "N", null);
		Boolean blBaixaTitCancelAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blBaixaTitCancelAprov"), "S", "N", null);
		Boolean blRecalcularFreteAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blRecalcularFreteAprov"), "S", "N", null);
		if ((null != blConcedeAbatimentoAprov && blConcedeAbatimentoAprov)
				|| (null != blProrrogaVencimentoAprov && blProrrogaVencimentoAprov)
				|| (null != blSustarProtestoAprov && blSustarProtestoAprov)
				|| (null != blBaixaTitCancelAprov && blBaixaTitCancelAprov)
				|| (null != blRecalcularFreteAprov && blRecalcularFreteAprov)) {

			questionamentoSolicitado = true;
		}
			
		Long nrFatura = Long.valueOf(questionamentoFatura.getFatura().getNrFatura());
		Filial filialCobradora = questionamentoFatura.getFilialCobradora();
		Fatura fatura = faturaService.findFaturaByNrFaturaAndIdFilial(nrFatura, filialCobradora.getIdFilial());
		if (null != fatura && questionamentoSolicitado && ("LI".equals(fatura.getTpSituacaoFatura().getValue()) || "CA".equals(fatura.getTpSituacaoFatura().getValue()))) {
			throw new BusinessException("LMS-36372");
		}
		
		// LMS-6109
		Boolean blConcedeAbatimentoSol = (Boolean) criteria.get("blConcedeAbatimentoSol");
		Long idFilialDebitada = (Long) criteria.get("idFilialDebitada");
		if (blConcedeAbatimentoSol != null && blConcedeAbatimentoSol && idFilialDebitada != null) {
			Filial filialDebitada = filialService.findById(idFilialDebitada);
			fatura.setFilialByIdFilialDebitada(filialDebitada);
			faturaService.store(fatura);
		}

		questionamentoFatura = extracAprovacoesFromCriteria(criteria, questionamentoFatura);

		historicoQuestionamentoFaturasService.generateHistoricoQuestionamento( questionamentoFatura,new DomainValue("AQ".concat(analiseNivel)),obHistorico);

    	//regra 5
		if(!Boolean.TRUE.equals(questionamentoFatura.getBlConcedeAbatimentoAprov())
				&& !Boolean.TRUE.equals(questionamentoFatura.getBlProrrogaVencimentoAprov())
				&& !Boolean.TRUE.equals(questionamentoFatura.getBlSustarProtestoAprov())
				&& !Boolean.TRUE.equals(questionamentoFatura.getBlRecalcularFreteAprov())
				&& !Boolean.TRUE.equals(questionamentoFatura.getBlBaixaTitCancelAprov())
			) {
			//Concluir o processo de análise
			this.storeConcluirAnalise(questionamentoFatura, obHistorico,dataHoraAtual);
			return;
		}
		
		//regra 6
		if("EA5".equals(tpSituacao)) {
			//Concluir o processo de análise
			this.storeConcluirAnalise(questionamentoFatura, obHistorico,dataHoraAtual);
			return;
		} 
		
		//regra 8
		validateAprovacoes(dataHoraAtual, questionamentoFatura,analiseNivel, obHistorico);	
	}

	private QuestionamentoFatura extracAprovacoesFromCriteria(Map<String, Object> criteria,
			QuestionamentoFatura questionamentoFatura) {
		Boolean blConcedeAbatimentoAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blConcedeAbatimentoAprov"), "S", "N", null);
		Boolean blProrrogaVencimentoAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blProrrogaVencimentoAprov"), "S", "N", null);
		Boolean blSustarProtestoAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blSustarProtestoAprov"), "S", "N", null);
		Boolean blBaixaTitCancelAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blBaixaTitCancelAprov"), "S", "N", null);
		Boolean blRecalcularFreteAprov = BooleanUtils.toBooleanObject(MapUtils.getString(criteria,"blRecalcularFreteAprov"), "S", "N", null);
		
		questionamentoFatura.setBlProrrogaVencimentoAprov(blProrrogaVencimentoAprov);
		questionamentoFatura.setBlConcedeAbatimentoAprov(blConcedeAbatimentoAprov);
		questionamentoFatura.setBlSustarProtestoAprov(blSustarProtestoAprov);
		questionamentoFatura.setBlBaixaTitCancelAprov(blBaixaTitCancelAprov);
		questionamentoFatura.setBlRecalcularFreteAprov(blRecalcularFreteAprov);

		/*Grava os campos "Motivo do Abatimento" e "Setor Causador" caso alterados somente se estiver marcado
		 * o campo "Conceder abatimento", neste caso estes campos serão obrigatórios
		 */  
		if (MapUtils.getBoolean(criteria, "blConcedeAbatimentoSol")){
			questionamentoFatura.setTpSetorCausadorAbatimento(new DomainValue(MapUtils.getString(criteria, "tpSetorCausadorAbatimento")));
			questionamentoFatura.setMotivoDesconto(motivoDescontoService.findById(MapUtils.getLong(criteria, "idMotivoDesconto")));
		}

		return questionamentoFatura;
			}
		
	private Boolean validateAprovacoes(DateTime dataHoraAtual,QuestionamentoFatura questionamentoFatura, String analiseNivel,String obHistorico) {
		int nextNivel = (Integer.parseInt(analiseNivel)+1);		
		//i - ii - iii
		BigDecimal vlLimiteAbatimento = (BigDecimal)parametroGeralService.findConteudoByNomeParametro(VL_LIMITE_ABAT_NIVEL+analiseNivel, false);
		BigDecimal vlLimiteQuestionamento = (BigDecimal)parametroGeralService.findConteudoByNomeParametro(VL_LIMITE_QUEST_NIVEL+analiseNivel, false);
		BigDecimal vlLimiteCancelamento = (BigDecimal)parametroGeralService.findConteudoByNomeParametro(VL_LIMITE_CANCEL_NIVEL+analiseNivel, false);
			
			//iv. 
			if( ( questionamentoFatura.getBlConcedeAbatimentoSol() != null && questionamentoFatura.getBlConcedeAbatimentoSol() && CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(questionamentoFatura.getVlAbatimentoSolicitado()), vlLimiteAbatimento) )
			 || (questionamentoFatura.getBlProrrogaVencimentoSol() != null && questionamentoFatura.getBlProrrogaVencimentoSol() && CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(questionamentoFatura.getVlDocumento()), vlLimiteQuestionamento))
			 ||	(questionamentoFatura.getBlBaixaTitCancelSol() != null && questionamentoFatura.getBlBaixaTitCancelSol() && CompareUtils.gt(BigDecimalUtils.defaultBigDecimal(questionamentoFatura.getVlDocumento()), vlLimiteCancelamento)) 
			 ) {
			DomainValue domainQuestionamento = domainValueService.findDomainValueByValue("DM_SITUACAO_QUEST_FATURAS", "AA"+nextNivel);
			questionamentoFatura.setTpSituacao(domainQuestionamento);
			
				this.store(questionamentoFatura);


				this.executeSendMailNextProcess(questionamentoFatura, "ID_PERFIL_AN_QUERY_NIVEL"+nextNivel);
			
				/** Exibir mensagem de alerta */
				new WarningCollector(configuracoesFacade.getMensagem("LMS-4203".concat(analiseNivel)));
			
			return Boolean.TRUE;
			} else {
				//Concluir o processo de análise
			this.storeConcluirAnalise(questionamentoFatura, obHistorico,dataHoraAtual);
			
			return Boolean.FALSE;
			}
		}

	/**
	 * Processo de Conclusão da Análise.
	 * @param questionamentoFatura
	 * @param obHistorico
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void storeConcluirAnalise(QuestionamentoFatura questionamentoFatura, String obHistorico, DateTime dataHoraAtual) {
		/** Caso a data da implantação do LMS da filial do documento for inferior ou igual a data atual não executar a regra 2.*/
		
		/** Envia Email de Retorno do Questionamento */
		this.executeSendMailConclusao(questionamentoFatura, obHistorico);

		/** Gera historico de conclusão do questionamento */
		historicoQuestionamentoFaturasService.generateHistoricoQuestionamento(
			 questionamentoFatura
			,new DomainValue("AQC")
			,null);

		/** Atualiza campos em Questionamento Fatura */
		questionamentoFatura.setTpSituacao(new DomainValue("ACO"));
		questionamentoFatura.setDhConclusao(dataHoraAtual);
		this.store(questionamentoFatura);

			List tpSituacaoList = new ArrayList();

			/**Se a solicitação for de desconto*/			
			if(Boolean.TRUE.equals(questionamentoFatura.getBlConcedeAbatimentoSol())){
				/**desconto em documento de serviço*/
				if(Arrays.asList("C","E","N","A","B","S").contains(questionamentoFatura.getTpDocumento().getValue())){					
					/**Executar a rotina de retorno do workflow no descontoService passando por parâmetro as informações conforme as rotinas de workflow.*/
					if(Boolean.TRUE.equals(questionamentoFatura.getBlConcedeAbatimentoAprov())){
						tpSituacaoList.add(String.valueOf("A"));	
					}
					else{
						tpSituacaoList.add(String.valueOf("R"));													
					}
					List idProcesList = new ArrayList();
					Long idDesconto = (Long)descontoService.findbyIdPendencia(questionamentoFatura.getIdQuestionamentoFatura());
					if(idDesconto != null){
						idProcesList.add( idDesconto  );
						
						efetivarDescontoService.executeWorkflow(idProcesList, tpSituacaoList); 
					}
				}
				 /**Se a solicitação for de desconto em fatura*/
				else if("R".equals(questionamentoFatura.getTpDocumento().getValue())){
					/**Executar a rotina de retorno do workflow na faturaService passando por parâmetro as informações conforme as rotinas de workflow.*/
					if(Boolean.TRUE.equals(questionamentoFatura.getBlConcedeAbatimentoAprov())){
						tpSituacaoList.add(String.valueOf("A"));						
					}
					else{
						tpSituacaoList.add(String.valueOf("R"));							
					}
					
					List idProcesList = new ArrayList();
					Long idFatura = (Long)faturaService.findByIdPendenciaDesconto(questionamentoFatura.getIdQuestionamentoFatura());
					if(idFatura != null){
						idProcesList.add( idFatura  );
						
						efetivarDescontoFaturaService.executeWorkflow(idProcesList, tpSituacaoList); 
					}
				}								
			}
			/**Se a solicitação for de prorrogação de vencimento ou cancelamento de boleto*/
			else if(Boolean.TRUE.equals(questionamentoFatura.getBlProrrogaVencimentoSol()) || Boolean.TRUE.equals(questionamentoFatura.getBlBaixaTitCancelSol())){				
				/**Se a solicitação for de prorrogação de vencimento*/
				if(Boolean.TRUE.equals(questionamentoFatura.getBlProrrogaVencimentoSol())){
					if(Boolean.TRUE.equals(questionamentoFatura.getBlProrrogaVencimentoAprov())){
						tpSituacaoList.add(String.valueOf("A"));												
					}
					else{
						tpSituacaoList.add(String.valueOf("R"));
					}
					/**Executar a rotina de retorno do workflow*/
					List idProcesList = new ArrayList();
					idProcesList.add( historicoBoletoService.findByIdQuestionamentoFatura(questionamentoFatura.getIdQuestionamentoFatura()).getIdHistoricoBoleto()  );
					
					prorrogarVencBoletoService.executeWorkflow(idProcesList, tpSituacaoList);
				}
				/**Se a solicitação for de cancelamento de boleto*/				
				else{
					if(Boolean.TRUE.equals(questionamentoFatura.getBlBaixaTitCancelAprov())){
						tpSituacaoList.add(String.valueOf("A"));
					} else{
						tpSituacaoList.add(String.valueOf("R"));
					}
					/**Executar a rotina de retorno do workflow*/
					List idProcesList = new ArrayList();
					idProcesList.add( historicoBoletoService.findByIdQuestionamentoFatura(questionamentoFatura.getIdQuestionamentoFatura()).getIdHistoricoBoleto()  );
					
					cancelarBoletoService.executeWorkflow(idProcesList, tpSituacaoList);
				}				
			}
		
		
		/** Exibir mensagem de alerta(Análise dequestionamento concluída) */
		new WarningCollector(configuracoesFacade.getMensagem("LMS-42044"));
	}

	/**
	 * Calcula Modulo 11
	 * @param strNumber
	 * @return
	 */
	private String getDigitoVerificadorByModulo11(String strNumber) {
    	int nrMultiply = (strNumber.length()+1);
		int nrSumResult = 0;

		for (int i = 0; i < strNumber.length(); i++) {
			nrSumResult += Integer.parseInt(strNumber.substring(i,i+1)) * nrMultiply;
			nrMultiply--;
		}

		int nrDigitoVerificador = (nrSumResult%11);
        if (nrDigitoVerificador < 2) {
        	nrDigitoVerificador = 2;
        } else nrDigitoVerificador = (11 - nrDigitoVerificador);

        return String.valueOf(nrDigitoVerificador);
	}

	/**
	 * Cancela o Questionamento de Fatura, gera um histórico de CAN (Cancelamento) do mesmo 
	 * e envia email para os usuarios do respectivo Perfil
	 * 
	 * @param questionamentoFatura
	 * @param obHistorico
	 */
	public void validateCancelarQuestionamento(Map<String, Object> criteria){
		QuestionamentoFatura questionamentoFatura = findById(MapUtils.getLong(criteria,"idQuestionamentoFatura"));
		Long idFilialCobradora = MapUtils.getLong(criteria,"idFilialCobradora");
    	String itensResumo = MapUtils.getString(criteria, "itensEmResumo");
    	String tpDocumento = MapUtils.getString(criteria, "tpDocumento");	
    	validateFilialImplantada(idFilialCobradora,questionamentoFatura.getBlRecalcularFreteSol(),criteria.containsKey("isStoreQuestionamento"),tpDocumento,itensResumo);
	}
	
	public void storeCancelarQuestionamento(QuestionamentoFatura questionamentoFatura, String obHistorico) {
		String tpSituacaoAntiga = questionamentoFatura.getTpSituacao().getValue();

		
		/** Verifica se Questionamento Atual não está em concluido nem cancelado */
		if ("ACO".equals(tpSituacaoAntiga) || "CAN".equals(tpSituacaoAntiga)){
			throw new BusinessException("LMS-42022", new Object[]{questionamentoFatura.getTpSituacao().getDescriptionAsString()});
		}

		DomainValue tpSituacaoCancelado = new DomainValue("CAN");
		questionamentoFatura.setTpSituacao(tpSituacaoCancelado);
		
		if(StringUtils.isEmpty(obHistorico)){
			questionamentoFaturasDAO.store(questionamentoFatura);
		} else {
		this.store(questionamentoFatura);
		}

    	/** Gera historico de cancelamento */
		historicoQuestionamentoFaturasService.generateHistoricoQuestionamento(
			 questionamentoFatura
			,tpSituacaoCancelado
			,obHistorico);

    	/** Envia emails para os Perfils de acordo com a situação anterior do Questionamento */
		String accessLevel = StringUtils.right(tpSituacaoAntiga, 1);
		if(StringUtils.isNumeric(accessLevel) && IntegerUtils.getInteger(accessLevel).intValue() > 1) {
			this.executeSendMailCancelamento(questionamentoFatura, "ID_PERFIL_AN_QUERY_NIVEL".concat(accessLevel));
		}

		/** Exibir mensagem de alerta(Análise cancelada) */
		new WarningCollector(configuracoesFacade.getMensagem("LMS-42011"));
	}

	/**
	 * Agrupa TODOS questionamento de mesma SITUACAO e de clientes de mesmo NR_IDENTIFICACAO_INICIAL, para o Usuario passado.
	 * @param idUsuarioApropriador
	 * @param tpSituacao
	 * @param nrIdentificacao
	 * @return
	 */
	public Integer updateApropriadorGrupoByPessoaJuridica(Long idUsuarioApropriador, String tpSituacao, String nrIdentificacao) {
		return questionamentoFaturasDAO.updateApropriadorGrupoByPessoaJuridica(idUsuarioApropriador, tpSituacao, nrIdentificacao);
	}

	/**
	 * Agrupa TODOS questionamento de mesma SITUACAO e CLIENTE, para o Usuario passado.
	 * @param idUsuarioApropriador
	 * @param tpSituacao
	 * @param idCliente
	 * @return
	 */
	public Integer updateApropriadorGrupoByPessoaFisica(Long idUsuarioApropriador, String tpSituacao, Long idCliente) {
		return questionamentoFaturasDAO.updateApropriadorGrupoByPessoaFisica(idUsuarioApropriador, tpSituacao, idCliente);
	}

	/**
	 * Verifica se Perfil do Usuario possui permissao de acesso, de acordo com a situação do Questionamento
	 * @param tpSituacaoQuestionamento
	 * @return
	 */
	public Boolean allowUserAccess(String tpSituacaoQuestionamento) {
		String accessLevel = StringUtils.right(tpSituacaoQuestionamento, 1);
		if(StringUtils.isBlank(accessLevel) || StringUtils.isAlpha(accessLevel)) {
			return Boolean.FALSE; //Analise concluida ou cancelada
		}
		return analiseCreditoClienteService.allowUserAccessByPerfil(SessionUtils.getUsuarioLogado().getIdUsuario(), "ID_PERFIL_AN_QUERY_NIVEL".concat(accessLevel));
	}

	/**
	 * Busca e-mails dos usuários ligado ao perfil passado
	 * e de seus substitutos vigentes na data atual.
	 * @param idsPerfil
	 * @return
	 */
	private String[] getUsuariosMailByPerfil(String...idsPerfil) {
		List<String> dsEmails = new ArrayList<String>();
		/** Adiciona Usuarios e Substitutos */
		List<Usuario> usuarios = analiseCreditoClienteService.findUsersAccessByPerfil(idsPerfil);
		usuarios.addAll(analiseCreditoClienteService.findUsersAccessByPerfilSubstituto(idsPerfil));

		/** Adiciona Emails de todos */
		for (Usuario usuario : usuarios) {
			if(StringUtils.isNotBlank(usuario.getDsEmail())) {
				dsEmails.add(usuario.getDsEmail());
			}
		}
		return (String[])dsEmails.toArray(new String[]{});
	}

	/**
	 * Retorna a descrição do tpDocumento+" "+sgFilial+" "+numero 
	 * @param questionamentoFatura
	 * @return
	 */
	public String getDocumentoFullDescription(QuestionamentoFatura questionamentoFatura) {
		DomainValue tpDocumento = questionamentoFatura.getTpDocumento();
		String toReturn = tpDocumento.getDescriptionAsString().concat(" ");
		if(Arrays.asList(new String[]{"N","C","E","A","B","S"}).contains(tpDocumento.getValue())){
			return toReturn
					.concat(questionamentoFatura.getDoctoServico().getTpDocumentoServico().getValue())
					.concat(" ")
					.concat(questionamentoFatura.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial())
					.concat(" ")
					.concat(questionamentoFatura.getDoctoServico().getNrDoctoServico().toString());
		} else {
			return toReturn
					.concat(questionamentoFatura.getFatura().getFilialByIdFilial().getSgFilial())
					.concat(" ")
					.concat(questionamentoFatura.getFatura().getNrFatura().toString());
			
		}
	}

	/**
	 * Envia e-mail de cancelamento
	 * @param questionamentoFatura
	 * @param dsEmails
	 */
	private void executeSendMailCancelamento(QuestionamentoFatura questionamentoFatura, String...idsPerfil) {
		/** Dados do E-mail */
		StringBuilder strText = new StringBuilder();
		strText.append(configuracoesFacade.getMensagem("LMS-42037",
				new Object[]{
					 this.getDocumentoFullDescription(questionamentoFatura)
					,configuracoesFacade.getMensagem("cancelada")
					,SessionUtils.getUsuarioLogado().getNmUsuario()}));

		/** Envia E-mail */
		this.executeSendMail(this.getUsuariosMailByPerfil(idsPerfil), strText);
	}

	/**
	 * Envia e-mail para o próximo Perfil 
	 * @param questionamentoFatura
	 * @param idsPerfil
	 */
	private void executeSendMailNextProcess(QuestionamentoFatura questionamentoFatura, String...idsPerfil) {
		/** Dados do E-mail */
		StringBuilder strText = new StringBuilder();
		strText.append(configuracoesFacade.getMensagem("LMS-42027",
				new Object[]{
					 this.getDocumentoFullDescription(questionamentoFatura)
					,questionamentoFatura.getTpSituacao().getDescriptionAsString()}));

		/** Envia E-mail */
		this.executeSendMail(this.getUsuariosMailByPerfil(idsPerfil), strText);
	}

	/**
	 * Envia Email de Retorno da Conclusão do Questionamento
	 * @param questionamentoFatura
	 * @param obHistorico
	 */
	private void executeSendMailConclusao(QuestionamentoFatura questionamentoFatura, String obHistorico){
		List<String> dsEmails = new ArrayList<String>();
		dsEmails.add(questionamentoFatura.getDsEmailRetorno());

		/** Verifica se Filial Solicitante é Matriz*/
		Long idFilialSolicitante = questionamentoFatura.getFilialSolicitante().getIdFilial();
		if(historicoFilialService.validateFilialUsuarioMatriz(idFilialSolicitante)) {
			String emailPadrao = (String) conteudoParametroFilialService.findConteudoByNomeParametro(questionamentoFatura.getFilialCobradora().getIdFilial(), "EMAIL_PADRAO_FAT", false);
			if(StringUtils.isNotBlank(emailPadrao)) {
				dsEmails.add(emailPadrao);
			}
		} else {
			dsEmails.add(questionamentoFatura.getDsEmailRetorno());
		}

		/** Dados do Cliente */
		Pessoa pessoa = questionamentoFatura.getCliente().getPessoa();
		String nrIdentificacaoFormatted = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao());

		/** Dados do E-mail */
		StringBuilder strText = new StringBuilder();
		strText.append(configuracoesFacade.getMensagem("LMS-42038")).append(":");

		//documento
		strText.append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("documento")).append(": ")
			.append(this.getDocumentoFullDescription(questionamentoFatura));
		//cliente
		strText.append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("cliente")).append(": ")
			.append(nrIdentificacaoFormatted.concat(" ").concat(pessoa.getNmPessoa()));
		//filial solicitante
		strText.append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("filialSolicitante")).append(": ")
			.append(questionamentoFatura.getFilialSolicitante().getSiglaNomeFilial());
		//usuário solicitante
		strText.append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("usuarioSolicitante")).append(": ")
			.append(questionamentoFatura.getUsuarioSolicitante().getUsuarioADSM().getNmUsuario());
		//data/hora solicitacao
		strText.append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("dataHoraSolicitacao")).append(": ")
			.append(DateTimeFormat.forPattern("dd/MM/yyyy HH:mm").print(questionamentoFatura.getDhSolicitacao()));
		
		//acoes solicitadas/aprovadas
		strText.append(LINE_SEPARATOR).append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("acoesSolicitadasQuestionamento"))
			.append(LINE_SEPARATOR)

			.append(MANTER_FORMATACAO)
			.append(configuracoesFacade.getMensagem("prorrogarVencimento")).append(": ")
			.append(StringUtils.leftPad(getBooleanDescription(questionamentoFatura.getBlProrrogaVencimentoSol()), 8)).append(StringUtils.leftPad("", 6))
			.append(configuracoesFacade.getMensagem("aprovado")).append(": ")
			.append(getBooleanDescription(questionamentoFatura.getBlProrrogaVencimentoAprov()))
			.append(FECHAR_MANTER_FORMATACAO)

			.append(MANTER_FORMATACAO)			
			.append(configuracoesFacade.getMensagem("sustarProtestoUrgente")).append(": ")
			.append(StringUtils.leftPad(getBooleanDescription(questionamentoFatura.getBlSustarProtestoSol()), 5)).append(StringUtils.leftPad("", 6))
			.append(configuracoesFacade.getMensagem("aprovado")).append(": ")
			.append(getBooleanDescription(questionamentoFatura.getBlSustarProtestoAprov()))
			.append(FECHAR_MANTER_FORMATACAO)

			.append(MANTER_FORMATACAO)
			.append(configuracoesFacade.getMensagem("baixarTituloCobranca")).append(": ")
			.append(getBooleanDescription(questionamentoFatura.getBlBaixaTitCancelSol())).append(StringUtils.leftPad("", 6))
			.append(configuracoesFacade.getMensagem("aprovado")).append(": ")
			.append(getBooleanDescription(questionamentoFatura.getBlBaixaTitCancelAprov()))
			.append(FECHAR_MANTER_FORMATACAO)

			.append(MANTER_FORMATACAO)			
			.append(configuracoesFacade.getMensagem("concederAbatimento")).append(": ")
			.append(StringUtils.leftPad(getBooleanDescription(questionamentoFatura.getBlConcedeAbatimentoSol()), 9)).append(StringUtils.leftPad("", 6))
			.append(configuracoesFacade.getMensagem("aprovado")).append(": ")
			.append(getBooleanDescription(questionamentoFatura.getBlConcedeAbatimentoAprov()))
			.append(FECHAR_MANTER_FORMATACAO)
		
			.append(MANTER_FORMATACAO)			
			.append(configuracoesFacade.getMensagem("recalcularFrete")).append(": ")
			.append(StringUtils.leftPad(getBooleanDescription(questionamentoFatura.getBlRecalcularFreteSol()), 12)).append(StringUtils.leftPad("", 6))
			.append(configuracoesFacade.getMensagem("aprovado")).append(": ")
			.append(getBooleanDescription(questionamentoFatura.getBlRecalcularFreteAprov()))
			.append(FECHAR_MANTER_FORMATACAO);


		//chave de liberacao
		// logica de impressão no email é a mesma que gera, então se gerou a chave, deve imprimir
		if(StringUtils.isNotBlank(questionamentoFatura.getDsChaveLiberacao())) {
			strText.append(LINE_SEPARATOR)
				.append(configuracoesFacade.getMensagem("chaveLiberacaoLegado")).append(": ")
				.append(questionamentoFatura.getDsChaveLiberacao());
		}
		
		//observacao
		if(Boolean.FALSE.equals(questionamentoFatura.getBlConcedeAbatimentoAprov())
				|| Boolean.FALSE.equals(questionamentoFatura.getBlProrrogaVencimentoAprov())
				|| Boolean.FALSE.equals(questionamentoFatura.getBlSustarProtestoAprov())
				|| Boolean.FALSE.equals(questionamentoFatura.getBlBaixaTitCancelAprov())) {
			strText.append(LINE_SEPARATOR)
				.append(configuracoesFacade.getMensagem("observacao")).append(": ")
				.append(obHistorico);
		}
		strText.append(LINE_SEPARATOR).append(LINE_SEPARATOR)
			.append(configuracoesFacade.getMensagem("LMS-42039"));

		/** Envia E-mail */
		this.executeSendMail((String[])dsEmails.toArray(new String[]{}), strText);
	}
	private String getBooleanDescription(Boolean value) {
		String domainValue = "N";
		if(BooleanUtils.isTrue(value)) {
			domainValue = "S";
		}		
		return domainValueService.findDomainValueByValue("DM_SIM_NAO", domainValue).getDescriptionAsString();
	}

	private void executeSendMail(String[] dsEmails, StringBuilder strText) {
		/** Dados do E-mail */
		String strSubject = configuracoesFacade.getMensagem("lmsQuestionamentoFatura");
		strText.append(LINE_SEPARATOR).append(LINE_SEPARATOR).append(configuracoesFacade.getMensagem("LMS-39022"));
		String strFrom = (String)configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");

		/** Envia E-mail */
		Mail mail = createMail(StringUtils.join(dsEmails, ";"), strFrom, strSubject, strText.toString());
		JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
		integracaoJmsService.storeMessage(msg);
	}

	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body.replace(LINE_SEPARATOR, "<br>"));
		return mail;
 	}
	
	public void setQuestionamentoFaturasDAO(QuestionamentoFaturasDAO questionamentoFaturasDAO) {
		this.questionamentoFaturasDAO = questionamentoFaturasDAO;
	}
	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	public void setHistoricoQuestionamentoFaturasService(HistoricoQuestionamentoFaturasService historicoQuestionamentoFaturasService) {
		this.historicoQuestionamentoFaturasService = historicoQuestionamentoFaturasService;
	}
	public void setAnexoQuestionamentoFaturasService(AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService) {
		this.anexoQuestionamentoFaturasService = anexoQuestionamentoFaturasService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	
	public void setAnaliseCreditoClienteService(AnaliseCreditoClienteService analiseCreditoClienteService) {
		this.analiseCreditoClienteService = analiseCreditoClienteService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setMotivoDescontoService(MotivoDescontoService motivoDescontoService) {
		this.motivoDescontoService = motivoDescontoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public BoletoService getBoletoService() {
		return boletoService;
	}

	public void setEfetivarDescontoService(EfetivarDescontoService efetivarDescontoService) {
		this.efetivarDescontoService = efetivarDescontoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public DescontoService getDescontoService() {
		return descontoService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}

	public void setEfetivarDescontoFaturaService(
			EfetivarDescontoFaturaService efetivarDescontoFaturaService) {
		this.efetivarDescontoFaturaService = efetivarDescontoFaturaService;
	}

	public EfetivarDescontoFaturaService getEfetivarDescontoFaturaService() {
		return efetivarDescontoFaturaService;
	}

	public void setProrrogarVencBoletoService(ProrrogarVencimentoBoletoService prorrogarVencBoletoService) {
		this.prorrogarVencBoletoService = prorrogarVencBoletoService;
	}

	public void setCancelarBoletoService(CancelarBoletoService cancelarBoletoService) {
		this.cancelarBoletoService = cancelarBoletoService;
	}

	/**
	 * LMS-6109 - Busca <tt>Filial</tt> para filial debitada de uma
	 * <tt>Fatura</tt>. Encontra a fatura a partir de um
	 * <tt>QuestionamentoFatura</tt> utilizando o id da filial de cobrança e
	 * número de romaneio do questionamento.
	 * 
	 * @param questionamentoFatura
	 *            questionamento para a busca da fatura e filial debitada
	 * @return filial debitada da fatura relacionada ao questionamento
	 */
	public Filial findFilialDebitada(QuestionamentoFatura questionamentoFatura) {
		return questionamentoFaturasDAO.findFilialDebitadaByQuestionamentoFatura(questionamentoFatura);
	}

	/**
	 * LMS-6109 - Busca id da filial de origem da <tt>Fatura</tt>. Encontra a
	 * fatura a partir de um <tt>QuestionamentoFatura</tt> utilizando o id da
	 * filial de cobrança e número de romaneio do questionamento.
	 * 
	 * @param questionamentoFatura
	 *            questionamento para a busca da fatura e filial de origem
	 * @return id da filial de origem da fatura relacionada ao questionamento
	 */
	public Long findIdFilialOrigemFatura(QuestionamentoFatura questionamentoFatura) {
		return questionamentoFaturasDAO.findIdFilialOrigemFatura(questionamentoFatura);
	}

	/**
	 * LMS-6109 - Busca id's das filiais de origem dos <tt>DoctoServico</tt>
	 * relacionados a <tt>Fatura</tt>. Encontra a fatura a partir de um
	 * <tt>QuestionamentoFatura</tt> utilizando o id da filial de cobrança e
	 * número de romaneio do questionamento.
	 * 
	 * @param questionamentoFatura
	 *            questionamento para a busca da fatura e documentos de serviço
	 * @return id's das filiais de origem dos documentos de serviço
	 */
	public List<Long> findListIdFilialOrigemDoctoServico(QuestionamentoFatura questionamentoFatura) {
		return questionamentoFaturasDAO.findListIdFilialOrigemDoctoServico(questionamentoFatura);
	}

}
