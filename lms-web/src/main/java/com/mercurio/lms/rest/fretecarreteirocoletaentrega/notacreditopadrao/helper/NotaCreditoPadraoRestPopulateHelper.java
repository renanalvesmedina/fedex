package com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoNotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.dto.NotaCreditoPadraoFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.notacreditopadrao.dto.NotaCreditoPadraoRestDTO;
import com.mercurio.lms.rest.fretecarreteiroviagem.dto.ReciboFreteCarreteiroSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

public class NotaCreditoPadraoRestPopulateHelper {
	
	/**
	 * Default private constructor
	 */
	private NotaCreditoPadraoRestPopulateHelper(){
		
	}
	
	/**
	 * 
	 * @param criteria
	 * @param filter
	 * 
	 * @return TypedFlatMap
	 */
	public static TypedFlatMap getFilterMap(TypedFlatMap criteria, NotaCreditoPadraoFilterDTO filter){		
		criteria.put("nrNotaCredito", filter.getNrNotaCredito());
		criteria.put("dhEmissaoInicial", filter.getDhEmissaoInicial());
		criteria.put("dhEmissaoFinal", filter.getDhEmissaoFinal());		
		criteria.put("dhGeracaoInicial", filter.getDhGeracaoInicial());
		criteria.put("dhGeracaoFinal", filter.getDhGeracaoFinal());
		
		getTpSituacao(criteria, filter);
		getTpMostrarNotaZerada(criteria, filter);
		getTpNotaCredito(criteria, filter);
		getFilialFilter(criteria, filter);	
		getProprietarioFilter(criteria, filter);		
		getMeioTransporteFilter(criteria, filter);
		getReciboFreteCarreteiroFilter(criteria, filter);
		
		return criteria;
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpSituacao(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getTpSituacao() != null){
			criteria.put("tpSituacao", filter.getTpSituacao().getValue());
		}
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpMostrarNotaZerada(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getTpMostrarNotaZerada() != null){
			criteria.put("tpMostrarNotaZerada", filter.getTpMostrarNotaZerada().getValue());
		}
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpNotaCredito(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getTpNotaCredito() != null){
			criteria.put("tpNotaCredito", filter.getTpNotaCredito().getValue());
		}
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getFilialFilter(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getFilial() != null){
			criteria.put("idFilial", filter.getFilial().getIdFilial());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getProprietarioFilter(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getProprietario() != null){
			criteria.put("idProprietario", filter.getProprietario().getIdProprietario());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getMeioTransporteFilter(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getMeioTransporte() != null){
			criteria.put("idMeioTransporte", filter.getMeioTransporte().getIdMeioTransporte());
		}
	}
	
	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getReciboFreteCarreteiroFilter(TypedFlatMap criteria,
			NotaCreditoPadraoFilterDTO filter) {
		if(filter.getReciboFreteCarreteiro() != null){
			criteria.put("idReciboFreteCarreteiro", filter.getReciboFreteCarreteiro().getIdReciboFreteCarreteiro());
		}
	}

	/**
	 * @param notaCredito
	 * @return NotaCreditoPadraoRestDTO
	 */
	public static NotaCreditoPadraoRestDTO getNotaCreditoPadraoRestDTO(NotaCredito notaCredito, boolean workflow) {
		if(notaCredito == null){
			return null;
		}
		
		NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO = new NotaCreditoPadraoRestDTO();
		notaCreditoPadraoRestDTO.setIdNotaCredito(notaCredito.getIdNotaCredito());
		notaCreditoPadraoRestDTO.setNrNotaCredito(notaCredito.getNrNotaCredito());
		notaCreditoPadraoRestDTO.setDhEmissao(notaCredito.getDhEmissao());
		notaCreditoPadraoRestDTO.setDhGeracao(notaCredito.getDhGeracao());
		notaCreditoPadraoRestDTO.setObNotaCredito(notaCredito.getObNotaCredito());
		notaCreditoPadraoRestDTO.setTpSituacao(new DomainValue(notaCredito.getDhEmissao() != null ? "E": "N"));
		notaCreditoPadraoRestDTO.setVlTotal(notaCredito.getVlTotal());
		notaCreditoPadraoRestDTO.setTpNotaCredito(notaCredito.getTpNotaCredito());
		notaCreditoPadraoRestDTO.setVlDescUsoEquipamento(notaCredito.getVlDescUsoEquipamento());
		
		getAcrescimoDesconto(notaCredito, notaCreditoPadraoRestDTO);
		getVlTotalItens(notaCredito, notaCreditoPadraoRestDTO);
		
		getSimboloMoeda(notaCredito, notaCreditoPadraoRestDTO);	
		
		getControleCarga(notaCredito, notaCreditoPadraoRestDTO);
		getPendencia(notaCredito, notaCreditoPadraoRestDTO);
		getTpSituacaoPendencia(notaCredito, notaCreditoPadraoRestDTO);
		getFilialDTO(notaCredito, notaCreditoPadraoRestDTO);		
		getReciboDTO(notaCredito, notaCreditoPadraoRestDTO);		
		getMeioTransporteDTO(notaCredito, notaCreditoPadraoRestDTO);		
		getProprietarioDTO(notaCredito, notaCreditoPadraoRestDTO);		
				
		defineDisabled(notaCreditoPadraoRestDTO, notaCredito, workflow);
		
		return notaCreditoPadraoRestDTO;
	}

	/**
	 * Define estado dos botões e campos da tela.
	 * 
	 * @param notaCreditoPadraoRestDTO
	 * @param notaCredito
	 * @param workflow
	 */
	private static void defineDisabled(NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO, NotaCredito notaCredito, boolean workflow){
		Pendencia pendencia = notaCredito.getPendencia();

		boolean parceira = "EP".equals(notaCredito.getTpNotaCredito().getValue());
		boolean recibo = notaCredito.getReciboFreteCarreteiro() != null;
		boolean proprio = "P".equals(notaCreditoPadraoRestDTO.getProprietario().getTpProprietario().getValue());
		boolean controleCarga = notaCredito.getControleCarga() != null;		
		boolean aprovado = notaCredito.getTpSituacaoAprovacao() != null && ConstantesWorkflow.APROVADO.equals(notaCredito.getTpSituacaoAprovacao().getValue());	
		boolean emAprovacao = pendencia != null && ConstantesWorkflow.EM_APROVACAO.equals(pendencia.getTpSituacaoPendencia().getValue());
		
		boolean disabledAcrescimoDesconto = aprovado || emAprovacao || workflow || recibo;
		boolean disabledSalvar = recibo || workflow || emAprovacao;
		boolean disabledTpAcrescimoDesconto = !SessionUtils.isFilialSessaoMatriz() || disabledAcrescimoDesconto;
		boolean disabledEmitir = proprio ||workflow || emAprovacao;
		boolean disabledVisualizar =  workflow || emAprovacao || notaCreditoPadraoRestDTO.getDhEmissao() != null;
		boolean disabledRegerar = recibo || emAprovacao || parceira || !controleCarga;
		
		notaCreditoPadraoRestDTO.setDisabledSalvar(SessionUtils.isFilialSessaoMatriz() ? false : disabledSalvar);
		notaCreditoPadraoRestDTO.setDisabledTpAcrescimoDesconto(disabledTpAcrescimoDesconto);
		notaCreditoPadraoRestDTO.setDisabledAcrescimoDesconto(disabledAcrescimoDesconto);
		notaCreditoPadraoRestDTO.setWorkflow(workflow);
		notaCreditoPadraoRestDTO.setDisabledEmitir(disabledEmitir);
		notaCreditoPadraoRestDTO.setDisabledVisualizar(disabledVisualizar);
		notaCreditoPadraoRestDTO.setDisabledRegerar(disabledRegerar);		
	}
	
	private static void getControleCarga(NotaCredito notaCredito, NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {		
		if(notaCredito.getControleCarga() == null){
			return;
		}
		
		notaCreditoPadraoRestDTO.setIdControleCarga(notaCredito.getControleCarga().getIdControleCarga());
	}
	
	private static void getPendencia(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {		
		if(notaCredito.getPendencia() == null){
			return;
		}
		
		notaCreditoPadraoRestDTO.setIdPendencia(notaCredito.getPendencia().getIdPendencia());
	}
	
	private static void getTpSituacaoPendencia(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {		
		if(notaCredito.getTpSituacaoAprovacao() == null){
			return;
		}
		
		notaCreditoPadraoRestDTO.setTpSituacaoPendenciaDescricao(notaCredito.getTpSituacaoAprovacao().getDescriptionAsString());
	}

	/**
	 * Recupera valor dos itens, sem desconto ou acréscimo.
	 * 
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getVlTotalItens(NotaCredito notaCredito, NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO){
		BigDecimal vlTotalItens = null;
		
		if(notaCredito.getVlAcrescimo() != null){
			vlTotalItens = notaCredito.getVlTotal().subtract(notaCredito.getVlAcrescimo());
		} else if(notaCredito.getVlDesconto() != null){
			vlTotalItens = notaCredito.getVlTotal().add(notaCredito.getVlDesconto());
		}
		
		if(vlTotalItens != null){
			notaCreditoPadraoRestDTO.setVlTotalItens(vlTotalItens);
		}		
	}
	
	/**
	 * Define o tipo de valor definido para acréscimo ou desconto.
	 * 
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getAcrescimoDesconto(NotaCredito notaCredito, NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		/*
		 * Filial apenas pode sugerir descontos.
		 */
		if(notaCredito.getTpSituacaoAprovacao() == null && !SessionUtils.isFilialSessaoMatriz()){
			notaCreditoPadraoRestDTO.setTpAcrescimoDesconto(new DomainValue("D"));
		} else {		
			/*
			 * Caso contrário mostra valores já informados.
			 */
			if(notaCredito.getVlAcrescimoSugerido() != null){
				notaCreditoPadraoRestDTO.setVlAcrescimoDesconto(notaCredito.getVlAcrescimoSugerido());
				notaCreditoPadraoRestDTO.setTpAcrescimoDesconto(new DomainValue("A"));
			} else if(notaCredito.getVlDescontoSugerido() != null){
				notaCreditoPadraoRestDTO.setVlAcrescimoDesconto(notaCredito.getVlDescontoSugerido());
				notaCreditoPadraoRestDTO.setTpAcrescimoDesconto(new DomainValue("D"));
			}
		}
	}

	/**
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getSimboloMoeda(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		if(notaCredito.getMoedaPais() == null){
			return;
		}
		
		notaCreditoPadraoRestDTO.setDsSimboloMoeda(notaCredito.getMoedaPais().getMoeda().getDsSimbolo());
	}

	/**
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getFilialDTO(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		if(notaCredito.getFilial() == null){
			return;
		}
		
		Filial filial = notaCredito.getFilial();
		
		FilialSuggestDTO filialDTO = new FilialSuggestDTO();
		filialDTO.setSgFilial(filial.getSgFilial());
		filialDTO.setNmFilial(filial.getPessoa().getNmFantasia());
		
		notaCreditoPadraoRestDTO.setFilial(filialDTO);
	}

	/**
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getProprietarioDTO(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		if(notaCredito.getControleCarga() == null || notaCredito.getControleCarga().getProprietario() == null){
			return;
		}
		
		Proprietario proprietario = notaCredito.getControleCarga().getProprietario();
		
		ProprietarioDTO proprietarioDTO = new ProprietarioDTO();
		proprietarioDTO.setNrIdentificacao(proprietario.getPessoa().getNrIdentificacao());
		proprietarioDTO.setTpIdentificacao(proprietario.getPessoa().getTpIdentificacao());
		proprietarioDTO.setNmPessoa(proprietario.getPessoa().getNmPessoa());
		proprietarioDTO.setTpProprietario(proprietario.getTpProprietario());
		
		notaCreditoPadraoRestDTO.setProprietario(proprietarioDTO);
	}

	/**
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getMeioTransporteDTO(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		if(notaCredito.getControleCarga() == null || notaCredito.getControleCarga().getMeioTransporteByIdTransportado() == null){
			return;
		}
		
		MeioTransporte meioTransporte = notaCredito.getControleCarga().getMeioTransporteByIdTransportado();
		
		MeioTransporteSuggestDTO meioTransporteDTO = new MeioTransporteSuggestDTO();
		meioTransporteDTO.setNrIdentificador(meioTransporte.getNrIdentificador());
		meioTransporteDTO.setNrFrota(meioTransporte.getNrFrota());
		
		notaCreditoPadraoRestDTO.setMeioTransporte(meioTransporteDTO);
	}

	/**
	 * @param notaCredito
	 * @param notaCreditoPadraoRestDTO
	 */
	private static void getReciboDTO(NotaCredito notaCredito,
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		if(notaCredito.getReciboFreteCarreteiro() == null){
			return;
		}
		
		ReciboFreteCarreteiro reciboFreteCarreteiro = notaCredito.getReciboFreteCarreteiro();
		
		ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiroDTO = new ReciboFreteCarreteiroSuggestDTO();
		reciboFreteCarreteiroDTO.setSgFilial(reciboFreteCarreteiro.getFilial().getSgFilial());
		reciboFreteCarreteiroDTO.setNrReciboFreteCarreteiro(reciboFreteCarreteiro.getNrReciboFreteCarreteiro());
		
		notaCreditoPadraoRestDTO.setReciboFreteCarreteiro(reciboFreteCarreteiroDTO);
	}

	public static NotaCredito getNotaCredito(NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO) {
		NotaCredito notaCredito = new NotaCredito();
		notaCredito.setIdNotaCredito(notaCreditoPadraoRestDTO.getIdNotaCredito());
		notaCredito.setObNotaCredito(notaCreditoPadraoRestDTO.getObNotaCredito());
		
		setAcrescimoDesconto(notaCreditoPadraoRestDTO, notaCredito);
		
		return notaCredito;
	}
	
	/**
	 * Retorna o tipo de valor definido para acréscimo ou desconto.
	 * 
	 * @param notaCreditoPadraoRestDTO
	 * @param notaCredito
	 */
	private static void setAcrescimoDesconto(
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO,
			NotaCredito notaCredito) {
		if(notaCreditoPadraoRestDTO.getTpAcrescimoDesconto() == null){
			return;
		}
		
		if("A".equals(notaCreditoPadraoRestDTO.getTpAcrescimoDesconto().getValue())){
			notaCredito.setVlAcrescimoSugerido(notaCreditoPadraoRestDTO.getVlAcrescimoDesconto());
		} else if("D".equals(notaCreditoPadraoRestDTO.getTpAcrescimoDesconto().getValue())){
			notaCredito.setVlDescontoSugerido(notaCreditoPadraoRestDTO.getVlAcrescimoDesconto());
		}		
	}
	
	/**
	 * 
	 * @param resultSetPage
	 * @return List<NotaCreditoPadraoRestDTO>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<NotaCreditoPadraoRestDTO> getListNotaCreditoPadraoRestDTO(DomainValueService domainValueService, ResultSetPage resultSetPage){
		List<NotaCreditoPadraoRestDTO> result = new ArrayList<NotaCreditoPadraoRestDTO>();
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> notaCredito : list) {
			NotaCreditoPadraoRestDTO notaCreditoPadraoRestDTO = new NotaCreditoPadraoRestDTO();
			notaCreditoPadraoRestDTO.setIdNotaCredito(MapUtils.getLong(notaCredito, "idNotaCredito"));
			notaCreditoPadraoRestDTO.setNrNotaCredito(MapUtils.getLong(notaCredito, "nrNotaCredito"));
			notaCreditoPadraoRestDTO.setDhEmissao((DateTime) MapUtils.getObject(notaCredito, "dhEmissao"));
			notaCreditoPadraoRestDTO.setDhGeracao((DateTime) MapUtils.getObject(notaCredito, "dhGeracao"));
			notaCreditoPadraoRestDTO.setTpSituacao(domainValueService.findDomainValueByValue("DM_SITUACAO_EMISSAO_NC", MapUtils.getString(notaCredito, "tpSituacao")));		
			notaCreditoPadraoRestDTO.setVlTotal((BigDecimal) MapUtils.getObject(notaCredito, "vlTotal"));
			notaCreditoPadraoRestDTO.setDsSimboloMoeda(MapUtils.getString(notaCredito, "dsSimboloMoeda"));
			notaCreditoPadraoRestDTO.setTpNotaCredito((DomainValue) MapUtils.getObject(notaCredito, "tpNotaCredito"));
											
			/* set filial. */
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setNmFilial(MapUtils.getString(notaCredito, "nmFantasia"));
			filialDTO.setSgFilial(MapUtils.getString(notaCredito, "sgFilial"));
			
			notaCreditoPadraoRestDTO.setFilial(filialDTO);

			/* set recibo. */
			ReciboFreteCarreteiroSuggestDTO reciboFreteCarreteiroDTO = new ReciboFreteCarreteiroSuggestDTO();
			reciboFreteCarreteiroDTO.setNrReciboFreteCarreteiro(MapUtils.getLong(notaCredito, "nrReciboFreteCarreteiro"));
			reciboFreteCarreteiroDTO.setSgFilial(MapUtils.getString(notaCredito, "sgFilialReciboFreteCarreteiro"));
			
			notaCreditoPadraoRestDTO.setReciboFreteCarreteiro(reciboFreteCarreteiroDTO);
			
			/* set meio transporte. */
			MeioTransporteSuggestDTO meioTransporteDTO = new MeioTransporteSuggestDTO();
			meioTransporteDTO.setIdMeioTransporte(MapUtils.getLong(notaCredito, "idMeioTransporte"));
			meioTransporteDTO.setNrFrota(MapUtils.getString(notaCredito, "nrFrota"));
			meioTransporteDTO.setNrIdentificador(MapUtils.getString(notaCredito, "nrIdentificador"));
			
			notaCreditoPadraoRestDTO.setMeioTransporte(meioTransporteDTO);
			
			/* set proprietário. */
			ProprietarioDTO proprietarioDTO = new ProprietarioDTO();
			proprietarioDTO.setIdProprietario(MapUtils.getLong(notaCredito, "idProprietario"));
			proprietarioDTO.setNmPessoa(MapUtils.getString(notaCredito, "nmPessoaProprietario"));
			proprietarioDTO.setNrIdentificacao(MapUtils.getString(notaCredito, "nrIdentificacaoProprietario"));
			proprietarioDTO.setTpIdentificacao((DomainValue) MapUtils.getObject(notaCredito, "tpIdentificacaoProprietario"));						
			proprietarioDTO.setTpIdentificacaoFormatado(FormatUtils.formatIdentificacao(proprietarioDTO.getTpIdentificacao(), proprietarioDTO.getNrIdentificacao()));
			
			notaCreditoPadraoRestDTO.setProprietario(proprietarioDTO);
			
			/*
			 * Necessário informar o ID para o framework.
			 */
			notaCreditoPadraoRestDTO.setId(notaCreditoPadraoRestDTO.getIdNotaCredito());
			
			result.add(notaCreditoPadraoRestDTO);
		}
		
		return result;
	}
	
	/**
	 * Popula uma entidade AnexoNotaCredito.
	 * 
	 * @param dados
	 * @param data
	 * 
	 * @return AnexoNotaCredito
	 * 
	 * @throws IOException
	 */
	public static AnexoNotaCredito getAnexoNotaCredito(Map<String, Object> dados, byte[] data) {						
		AnexoNotaCredito anexoNotaCredito = new AnexoNotaCredito();
		anexoNotaCredito.setDsAnexo(MapUtils.getString(dados, "dsAnexo"));
		anexoNotaCredito.setDhCriacao(RestPopulateUtils.getYearMonthDayFromISO8601(dados, "dhCriacao").toDateTimeAtCurrentTime());
		anexoNotaCredito.setDcArquivo(data);

		UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
    	anexoNotaCredito.setUsuario(usuarioLMS);
    	
    	return anexoNotaCredito;
	}
	
	/**
	 *   
	 * @param resultSetPage
	 * @return List<Map<String, Object>> 
	 */
	public static List<Map<String, Object>> getListForReport(DomainValueService domainValueService, ResultSetPage<Map<String, Object>> resultSetPage) {		
		if(resultSetPage == null || resultSetPage.getList().isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> map : list) {
			map.put("nrReciboFreteCarreteiro", FormatUtils.formatLongWithZeros(MapUtils.getLong(map, "nrReciboFreteCarreteiro"), "0000000000"));
			map.put("nrNotaCredito", FormatUtils.formatLongWithZeros(MapUtils.getLong(map, "nrNotaCredito"), "0000000000"));
			map.put("nrIdentificacaoProprietarioFormatado", formatIdentificacao(map.get("tpIdentificacaoProprietario"), map.get("nrIdentificacaoProprietario")));						
			map.put("nrIdentificacaoClienteFormatado", formatIdentificacao(map.get("tpIdentificacaoCliente"), map.get("nrIdentificacaoCliente")));			
			map.put("tpSituacao", domainValueService.findDomainValueByValue("DM_SITUACAO_EMISSAO_NC", MapUtils.getString(map, "tpSituacao")));	
		}
		
		return resultSetPage.getList();
	}
	
	private static String formatIdentificacao(Object tpIdentificacao, Object nrIdentificacao){
		return FormatUtils.formatIdentificacao((DomainValue) tpIdentificacao, (String) nrIdentificacao);
	}
}
