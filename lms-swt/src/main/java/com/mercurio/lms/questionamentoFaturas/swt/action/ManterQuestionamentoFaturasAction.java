package com.mercurio.lms.questionamentoFaturas.swt.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.MotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.ItemFaturaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.questionamentoFaturas.model.service.QuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.WarningCollectorUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

public class ManterQuestionamentoFaturasAction {
	
	private QuestionamentoFaturasService questionamentoFaturasService;
	private FilialService filialService;
	private FaturaService faturaService;
	private ItemFaturaService itemFaturaService;
	private ConfiguracoesFacade configuracoesFacade;

    public Map<String, Object> findConhecimentoCorporativo(Map<String, Object> criteria) {
    	WarningCollectorUtils.remove();
    	
    	Map<String, Object> toReturn =  null;
		WarningCollectorUtils.putAll(toReturn);
		return toReturn;
    }

    public void validateSubmeterAnalise(Map<String, Object> criteria) {
    }

    public void validateCancelamentoQuestionamento(Map<String, Object> criteria) {
		questionamentoFaturasService.validateCancelarQuestionamento(criteria);
    }

    public Map<String, Object> findRomaneioCorporativo(Map<String, Object> criteria) {
    	WarningCollectorUtils.remove();
    	
    	Map<String, Object> toReturn = null;
		WarningCollectorUtils.putAll(toReturn);
		return toReturn;
    }

    public Map<String, Object> storeAnaliseQuestionamento(Map<String, Object> criteria) {
    	/** Validacao ref aos dados da tela cad */

    	Long idQuestionamentoFatura = MapUtils.getLong(criteria,"idQuestionamentoFatura");
    	QuestionamentoFatura questionamentoFatura = new QuestionamentoFatura();
		if(idQuestionamentoFatura != null) {
			questionamentoFatura = questionamentoFaturasService.findById(idQuestionamentoFatura);
		}

		/** Tipo de documento */
		String tpDocumento = MapUtils.getString(criteria,"tpDocumento");
		if(Arrays.asList(new String[]{"N","C","E","S","A","B"}).contains(tpDocumento)){
			String sgFilialOrigem = MapUtils.getString(criteria,"sgFilialOrigem");
			Integer nrDocumento = MapUtils.getInteger(criteria, "nrDocumento");
			Map mapCorp = null;
		}
		questionamentoFatura.setTpDocumento(new DomainValue(tpDocumento));
		questionamentoFatura.setTpSituacao(new DomainValue("AA1"));
		String tpSituacaoBoleto = MapUtils.getString(criteria,"tpSituacaoBoleto");
		if(StringUtils.isNotBlank(tpSituacaoBoleto)) {
			questionamentoFatura.setTpSituacaoBoleto(new DomainValue(tpSituacaoBoleto));
		}
		String tpSetorCausadorAbatimento = MapUtils.getString(criteria,"setorCausadorAbatimento");
		if(StringUtils.isNotBlank(tpSetorCausadorAbatimento)) {
			questionamentoFatura.setTpSetorCausadorAbatimento(new DomainValue(tpSetorCausadorAbatimento));
		}

		questionamentoFatura.setObAbatimento(MapUtils.getString(criteria,"obAbatimento"));
		questionamentoFatura.setObAcaoCorretiva(MapUtils.getString(criteria,"obAcaoCorretiva"));
		questionamentoFatura.setDsEmailRetorno(MapUtils.getString(criteria,"dsEmailRetorno"));

		questionamentoFatura.setBlConcedeAbatimentoSol(MapUtils.getBoolean(criteria, "blConcedeAbatimentoSol", Boolean.FALSE));
		questionamentoFatura.setBlProrrogaVencimentoSol(MapUtils.getBoolean(criteria, "blProrrogaVencimentoSol", Boolean.FALSE));
		questionamentoFatura.setBlSustarProtestoSol(MapUtils.getBoolean(criteria, "blSustarProtestoSol", Boolean.FALSE));
		questionamentoFatura.setBlBaixaTitCancelSol(MapUtils.getBoolean(criteria, "blBaixaTitCancelSol", Boolean.FALSE));
		questionamentoFatura.setBlRecalcularFreteSol(MapUtils.getBoolean(criteria, "blRecalcularFreteSol", Boolean.FALSE));

		questionamentoFatura.setDhSolicitacao(JTDateTimeUtils.getDataHoraAtual());
		questionamentoFatura.setDtEmissaoDocumento((YearMonthDay) MapUtils.getObject(criteria, "dtEmissaoDocumento"));
		questionamentoFatura.setNrBoleto(MapUtils.getString(criteria, "nrBoleto"));

		/** Valores */
		Object vlDocumento = MapUtils.getObject(criteria, "vlDocumento");
		if(vlDocumento != null) {
			questionamentoFatura.setVlDocumento(BigDecimalUtils.getBigDecimal(vlDocumento));
		}
		Object vlAbatimentoSolicitado = MapUtils.getObject(criteria, "vlAbatimentoSolicitado");
		if(vlAbatimentoSolicitado != null) {
			questionamentoFatura.setVlAbatimentoSolicitado(BigDecimalUtils.getBigDecimal(vlAbatimentoSolicitado));
		}

		/** Datas de Vencimento */
		YearMonthDay dtVencimentoDocumento = (YearMonthDay) MapUtils.getObject(criteria, "dtVencimentoDocumento");
		questionamentoFatura.setDtVencimentoDocumento(dtVencimentoDocumento);
		YearMonthDay dtVencimentoSolicitado = (YearMonthDay) MapUtils.getObject(criteria, "dtVencimentoSolicitado");
		questionamentoFatura.setDtVencimentoSolicitado(dtVencimentoSolicitado);

		/** Referencias/Motivos */
		Cliente cliente = new Cliente();
		cliente.setIdCliente(MapUtils.getLong(criteria, "idCliente"));
		questionamentoFatura.setCliente(cliente);

		Long idFilialCobradora = MapUtils.getLong(criteria,"idFilialCobradora");
		if(idFilialCobradora != null) {
			Filial filialCobradora = new Filial();
			filialCobradora.setIdFilial(idFilialCobradora);
			questionamentoFatura.setFilialCobradora(filialCobradora);
		}
		questionamentoFatura.setFilialSolicitante(SessionUtils.getFilialSessao());

		Long idMotivoCancelamento = MapUtils.getLong(criteria, "idMotivoCancelamento");
		if(idMotivoCancelamento != null) {
			MotivoOcorrencia motivoCancelamento = new MotivoOcorrencia();
			motivoCancelamento.setIdMotivoOcorrencia(idMotivoCancelamento);
			questionamentoFatura.setMotivoCancelamento(motivoCancelamento);
		}
		Long idMotivoSustacaoProtesto = MapUtils.getLong(criteria, "idMotivoSustacaoProtesto");
		if(idMotivoSustacaoProtesto != null) {
			MotivoOcorrencia motivoSustacaoProtesto = new MotivoOcorrencia();
			motivoSustacaoProtesto.setIdMotivoOcorrencia(idMotivoSustacaoProtesto);
			questionamentoFatura.setMotivoSustacaoProtesto(motivoSustacaoProtesto);
		}
		Long idMotivoProrrogacaoVcto = MapUtils.getLong(criteria, "idMotivoProrrogacaoVcto");
		if(idMotivoProrrogacaoVcto != null) {
			MotivoOcorrencia motivoProrrogacaoVcto = new MotivoOcorrencia();
			motivoProrrogacaoVcto.setIdMotivoOcorrencia(idMotivoProrrogacaoVcto);
			questionamentoFatura.setMotivoProrrogacaoVcto(motivoProrrogacaoVcto);
		}
		Long idMotivoDesconto = MapUtils.getLong(criteria,"idMotivoDesconto");
		if(idMotivoDesconto != null) {
			MotivoDesconto motivoDesconto = new MotivoDesconto();
			motivoDesconto.setIdMotivoDesconto(idMotivoDesconto);
			questionamentoFatura.setMotivoDesconto(motivoDesconto);
		}

		UsuarioLMS usuarioSolicitante = new UsuarioLMS();
		usuarioSolicitante.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		questionamentoFatura.setUsuarioSolicitante(usuarioSolicitante);

		questionamentoFatura.setDsChaveLiberacao(null);
		questionamentoFatura.setDhConclusao(null);
		questionamentoFatura.setBlProrrogaVencimentoAprov(null);
		questionamentoFatura.setBlConcedeAbatimentoAprov(null);
		questionamentoFatura.setBlSustarProtestoAprov(null);
		questionamentoFatura.setBlBaixaTitCancelAprov(null);

		String dsHistorico = MapUtils.getString(criteria,"obHistorico");
		if (StringUtils.isBlank(dsHistorico)){
			dsHistorico = questionamentoFatura.getObAbatimento();
		}

		//*** regra 7
		if(!Boolean.TRUE.equals(questionamentoFatura.getBlConcedeAbatimentoSol())) {
			questionamentoFatura.setVlAbatimentoSolicitado(null);
			questionamentoFatura.setMotivoDesconto(null);
			questionamentoFatura.setObAbatimento(null);
			questionamentoFatura.setTpSetorCausadorAbatimento(null);
			questionamentoFatura.setObAcaoCorretiva(null);
		}
		questionamentoFaturasService.storeAnaliseQuestionamento(questionamentoFatura, dsHistorico);

		Map<String, Object> mapRetorno = new HashMap<String, Object>();
		mapRetorno.put("idQuestionamentoFatura", questionamentoFatura.getIdQuestionamentoFatura());
		return mapRetorno;    	
    }

    public Map<String, Object> storeCancelarQuestionamento(Map<String, Object> criteria) {
    	WarningCollectorUtils.remove();
    	QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(MapUtils.getLong(criteria,"idQuestionamentoFatura"));
    	
    	questionamentoFaturasService.storeCancelarQuestionamento(questionamentoFatura, MapUtils.getString(criteria,"obHistorico"));

    	Map<String, Object> toReturn = new HashMap<String, Object>();
		WarningCollectorUtils.putAll(toReturn);
		return toReturn;
    }

    public Map store(Map<String, Object> criteria) {
		QuestionamentoFatura questionamentoFatura = questionamentoFaturasService.findById(MapUtils.getLong(criteria,"idQuestionamentoFatura"));
		questionamentoFaturasService.store(questionamentoFatura);

		Map<String, Object> mapRetorno = new HashMap<String, Object>();
		mapRetorno.put("idQuestionamentoFatura", questionamentoFatura.getIdQuestionamentoFatura());
		return mapRetorno;
	}

    @SuppressWarnings("rawtypes")
	public Map findManutencoesByRomaNrAndFilial(TypedFlatMap criteria){
    	Integer nrFatura = criteria.getInteger("nrDocumento");
    	Long idFilial = criteria.getLong("idFilialCobradora");
    	Fatura fatura = faturaService.findFaturaByNrFaturaAndIdFilial(nrFatura.longValue(), idFilial);
    	
    	if(fatura != null){
    		if(!fatura.getBlCancelaFaturaInteira()){
    			DomainValue tpManutencao = configuracoesFacade.getDomainValue("DM_TP_MANUTENCAO_FATURA", "E");
    			criteria.put("tpManutencao", tpManutencao.getValue());
    			
	    		List items = itemFaturaService.findItemsManutenidos(nrFatura.longValue(),idFilial);
	    		
	    		List<Map<String,Object>> documentos = new ArrayList<Map<String,Object>>();
	    		for (Object object : items) {
	    			
	    			Map<String,Object> item = new HashMap<String,Object>();
	    			Object[] array = (Object[])object;
	    			
	    			Long idItemFatura = (Long) array[0];
	    			DomainValue tpDocumento = (DomainValue)array[3];
	    			String nrDocumento = (String) tpDocumento.getDescriptionAsString()+" "+array[2]+" "+(Long) array[1];
	    			
	    			item.put("idItemFatura", idItemFatura);
	    			item.put("nrDocumento", nrDocumento);
	    			
	    			documentos.add(item);
				}
	    		
	    		criteria.put("documentos", documentos);
    		} else {
    			DomainValue tpManutencao = configuracoesFacade.getDomainValue("DM_TP_MANUTENCAO_FATURA", "I");
    			criteria.put("tpManutencao", tpManutencao.getValue());
        	}
    	}
    	
    	return criteria;
    }
    

    public ResultSetPage<QuestionamentoFatura> findPaginated(Map<String, Object> criteria) {
		PaginatedQuery paginatedQuery = new PaginatedQuery(criteria);
		return questionamentoFaturasService.findPaginated(paginatedQuery);
	}

	public void setQuestionamentoFaturasService(QuestionamentoFaturasService questionamentoFaturasService) {
		this.questionamentoFaturasService = questionamentoFaturasService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public ItemFaturaService getItemFaturaService() {
		return itemFaturaService;
	}

	public void setItemFaturaService(ItemFaturaService itemFaturaService) {
		this.itemFaturaService = itemFaturaService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}