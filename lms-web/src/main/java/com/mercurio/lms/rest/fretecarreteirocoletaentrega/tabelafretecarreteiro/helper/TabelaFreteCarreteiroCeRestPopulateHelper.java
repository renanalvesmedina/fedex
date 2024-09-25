package com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoTabelaFreteCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcFaixaPeso;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.TipoMeioTransporteDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.FatorCalculoDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFcFaixaPesoRestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFcValoresRestDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFreteCarreteiroCeFilterDTO;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.tabelafretecarreteiro.dto.TabelaFreteCarreteiroCeRestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.PaisDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

public class TabelaFreteCarreteiroCeRestPopulateHelper {

	private static final String CLIENTE_DEDICADO = "CD";
	private static final int MIN_ITEMS_TO_DISPLAY = 3;
	
	/**
	 * Default private constructor
	 */
	private TabelaFreteCarreteiroCeRestPopulateHelper(){
		
	}
	
	public static TypedFlatMap getFilterMap(TypedFlatMap criteria, TabelaFreteCarreteiroCeFilterDTO filter){
		criteria.put("nrTabelaFreteCarreteiroCe", filter.getNrTabelaFreteCarreteiroCe());
		criteria.put("dhVigenciaInicial", filter.getDhVigenciaInicial());
		criteria.put("dhVigenciaFinal", filter.getDhVigenciaFinal());
		
		getFilialFilter(criteria, filter);		
		getTpOperacaoFilter(criteria, filter);		
		getTpVigenteFilter(criteria, filter);		
		getTpVinculoFilter(criteria, filter);		
		getProprietario(criteria, filter);		
		getMeioTransporteFilter(criteria, filter);		
		getTipoMeioTransporteFilter(criteria, filter);		
		getMunicipioFilter(criteria, filter);		
		getRotaFilter(criteria, filter);		
		getClienteFilter(criteria, filter);
		
		return criteria;
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getFilialFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getFilial() != null){
			criteria.put("idFilial", filter.getFilial().getIdFilial());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpOperacaoFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getTpOperacao() != null){
			criteria.put("tpOperacao", filter.getTpOperacao().getValue());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpVigenteFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getTpVigente() != null){
			criteria.put("tpVigente", filter.getTpVigente().getValue());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTpVinculoFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getTpVinculo() != null){
			criteria.put("tpVinculo", filter.getTpVinculo().getValue());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getProprietario(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getProprietario() != null){
			criteria.put("idProprietario", filter.getProprietario().getIdProprietario());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getMeioTransporteFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getMeioTransporte() != null){
			criteria.put("idMeioTransporte", filter.getMeioTransporte().getIdMeioTransporte());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getTipoMeioTransporteFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getTipoMeioTransporte() != null){
			criteria.put("idTipoMeioTransporte", filter.getTipoMeioTransporte().getIdTipoMeioTransporte());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getMunicipioFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getMunicipio() != null){
			criteria.put("idMunicipio", filter.getMunicipio().getIdMunicipio());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getRotaFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getRotaColetaEntrega() != null){
			criteria.put("idRotaColetaEntrega", filter.getRotaColetaEntrega().getIdRotaColetaEntrega());
		}
	}

	/**
	 * @param criteria
	 * @param filter
	 */
	private static void getClienteFilter(TypedFlatMap criteria,
			TabelaFreteCarreteiroCeFilterDTO filter) {
		if(filter.getCliente() != null){
			criteria.put("idCliente", filter.getCliente().getIdCliente());
		}
	}
	
	/**
	 * @param tabelaFreteCarreteiroCe
	 * @return TabelaFreteCarreteiroCeDTO
	 */
	public static TabelaFreteCarreteiroCeRestDTO getTabelaFreteCarreteiroCeRestDTO(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		if(tabelaFreteCarreteiroCe == null){
			return null;
		}
		
		TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO = new TabelaFreteCarreteiroCeRestDTO();		 		
		tabelaFreteCarreteiroCeDTO.setIdTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe.getIdTabelaFreteCarreteiroCe());
		tabelaFreteCarreteiroCeDTO.setNrTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe.getNrTabelaFreteCarreteiroCe());
		tabelaFreteCarreteiroCeDTO.setTpOperacao(tabelaFreteCarreteiroCe.getTpOperacao());
		tabelaFreteCarreteiroCeDTO.setTpVinculo(tabelaFreteCarreteiroCe.getTpVinculo());		
		tabelaFreteCarreteiroCeDTO.setDhVigenciaInicial(tabelaFreteCarreteiroCe.getDhVigenciaInicial());
		tabelaFreteCarreteiroCeDTO.setDhVigenciaFinal(tabelaFreteCarreteiroCe.getDhVigenciaFinal());
		tabelaFreteCarreteiroCeDTO.setDtAtualizacao(tabelaFreteCarreteiroCe.getDtAtualizacao());
		tabelaFreteCarreteiroCeDTO.setDtCriacao(tabelaFreteCarreteiroCe.getDtCriacao());
		tabelaFreteCarreteiroCeDTO.setObTabelaFrete(tabelaFreteCarreteiroCe.getObTabelaFrete());
		tabelaFreteCarreteiroCeDTO.setBlSegunda(getFlagByDomain(tabelaFreteCarreteiroCe.getBlSegunda()));
		tabelaFreteCarreteiroCeDTO.setBlTerca(getFlagByDomain(tabelaFreteCarreteiroCe.getBlTerca()));
		tabelaFreteCarreteiroCeDTO.setBlQuarta(getFlagByDomain(tabelaFreteCarreteiroCe.getBlQuarta()));
		tabelaFreteCarreteiroCeDTO.setBlQuinta(getFlagByDomain(tabelaFreteCarreteiroCe.getBlQuinta()));
		tabelaFreteCarreteiroCeDTO.setBlSexta(getFlagByDomain(tabelaFreteCarreteiroCe.getBlSexta()));
		tabelaFreteCarreteiroCeDTO.setBlSabado(getFlagByDomain(tabelaFreteCarreteiroCe.getBlSabado()));
		tabelaFreteCarreteiroCeDTO.setBlDomingo(getFlagByDomain(tabelaFreteCarreteiroCe.getBlDomingo()));
		tabelaFreteCarreteiroCeDTO.setBlDedicado(getFlagByDomain(tabelaFreteCarreteiroCe.getBlDedicado()));
		tabelaFreteCarreteiroCeDTO.setBlDescontaFrete(getFlagByDomain(tabelaFreteCarreteiroCe.getBlDescontaFrete()));
		tabelaFreteCarreteiroCeDTO.setTpModal(tabelaFreteCarreteiroCe.getTpModal());
		tabelaFreteCarreteiroCeDTO.setTpPeso(tabelaFreteCarreteiroCe.getTpPeso());
		tabelaFreteCarreteiroCeDTO.setPcPremioCte(tabelaFreteCarreteiroCe.getPcPremioCte());
		tabelaFreteCarreteiroCeDTO.setPcPremioEvento(tabelaFreteCarreteiroCe.getPcPremioEvento());
		tabelaFreteCarreteiroCeDTO.setPcPremioDiaria(tabelaFreteCarreteiroCe.getPcPremioDiaria());
		tabelaFreteCarreteiroCeDTO.setPcPremioVolume(tabelaFreteCarreteiroCe.getPcPremioVolume());
		tabelaFreteCarreteiroCeDTO.setPcPremioSaida(tabelaFreteCarreteiroCe.getPcPremioSaida());
		tabelaFreteCarreteiroCeDTO.setPcPremioFreteBruto(tabelaFreteCarreteiroCe.getPcPremioFreteBruto());
		tabelaFreteCarreteiroCeDTO.setPcPremioFreteLiq(tabelaFreteCarreteiroCe.getPcPremioFreteLiq());
		tabelaFreteCarreteiroCeDTO.setPcPremioMercadoria(tabelaFreteCarreteiroCe.getPcPremioMercadoria());
						
		getFilialDTO(tabelaFreteCarreteiroCe, tabelaFreteCarreteiroCeDTO);		
		getTabelaClonada(tabelaFreteCarreteiroCe, tabelaFreteCarreteiroCeDTO);
		getUsuarioCriacaoDTO(tabelaFreteCarreteiroCe, tabelaFreteCarreteiroCeDTO);
		getUsuarioAlteracaoDTO(tabelaFreteCarreteiroCe, tabelaFreteCarreteiroCeDTO);
		
		tabelaFreteCarreteiroCeDTO.setDisabled(isDisabled(tabelaFreteCarreteiroCe.getDhVigenciaInicial(), tabelaFreteCarreteiroCe.getDhVigenciaFinal()));
		tabelaFreteCarreteiroCeDTO.setEnded(isEnded(tabelaFreteCarreteiroCe.getDhVigenciaFinal()));
				
		return tabelaFreteCarreteiroCeDTO;
	}
	
	private static boolean isDisabled(DateTime dhVigenciaInicial, DateTime dhVigenciaFinal){		
		if(dhVigenciaInicial == null){
			return false;
		}
		
		DateTime dataAtual = JTDateTimeUtils.getDataHoraAtual();
		
		return dhVigenciaInicial.compareTo(dataAtual) <= 0 && (dhVigenciaFinal == null || (dhVigenciaFinal.compareTo(dataAtual) >= 0 || dhVigenciaFinal.compareTo(dataAtual) < 0));
	}
	
	private static boolean isEnded(DateTime dhVigenciaFinal){		
		if(dhVigenciaFinal == null){
			return false;
		}
				
		return dhVigenciaFinal.compareTo(JTDateTimeUtils.getDataHoraAtual()) <= 0;
	}
	
	
	private static Boolean getFlagByDomain(DomainValue bl) {		
		if(bl == null){
			return false;
		}
		
		return "S".equals(bl.getValue());
	}
	
	private static DomainValue getDomainByFlag(Boolean bl) {	
		String value = "N";
		if(bl != null && bl){
			value = "S";
		}
		return new DomainValue(value);
	}

	/**
	 * 
	 * @param tabelaFreteCarreteiroCeDTO
	 * 
	 * @return TabelaFreteCarreteiroCe
	 */
	public static TabelaFreteCarreteiroCe getTabelaFreteCarreteiroCe(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO){
		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabelaFreteCarreteiroCe.setIdTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCeDTO.getIdTabelaFreteCarreteiroCe());
		tabelaFreteCarreteiroCe.setNrTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCeDTO.getNrTabelaFreteCarreteiroCe());
		tabelaFreteCarreteiroCe.setTpOperacao(tabelaFreteCarreteiroCeDTO.getTpOperacao());
		tabelaFreteCarreteiroCe.setTpVinculo(tabelaFreteCarreteiroCeDTO.getTpVinculo());
		tabelaFreteCarreteiroCe.setTpModal(tabelaFreteCarreteiroCeDTO.getTpModal());
		tabelaFreteCarreteiroCe.setTpPeso(tabelaFreteCarreteiroCeDTO.getTpPeso());
		tabelaFreteCarreteiroCe.setObTabelaFrete(tabelaFreteCarreteiroCeDTO.getObTabelaFrete());
		tabelaFreteCarreteiroCe.setDhVigenciaInicial(tabelaFreteCarreteiroCeDTO.getDhVigenciaInicial());
		tabelaFreteCarreteiroCe.setDhVigenciaFinal(tabelaFreteCarreteiroCeDTO.getDhVigenciaFinal());
		tabelaFreteCarreteiroCe.setDtAtualizacao(tabelaFreteCarreteiroCeDTO.getDtAtualizacao());
		tabelaFreteCarreteiroCe.setDtCriacao(tabelaFreteCarreteiroCeDTO.getDtCriacao());
		tabelaFreteCarreteiroCe.setBlSegunda(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlSegunda()));
		tabelaFreteCarreteiroCe.setBlTerca(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlTerca()));
		tabelaFreteCarreteiroCe.setBlQuarta(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlQuarta()));
		tabelaFreteCarreteiroCe.setBlQuinta(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlQuinta()));
		tabelaFreteCarreteiroCe.setBlSexta(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlSexta()));
		tabelaFreteCarreteiroCe.setBlSabado(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlSabado()));
		tabelaFreteCarreteiroCe.setBlDomingo(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlDomingo()));		
		tabelaFreteCarreteiroCe.setBlDedicado(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlDedicado()));
		tabelaFreteCarreteiroCe.setBlDescontaFrete(getDomainByFlag(tabelaFreteCarreteiroCeDTO.getBlDescontaFrete()));
		tabelaFreteCarreteiroCe.setPcPremioCte(tabelaFreteCarreteiroCeDTO.getPcPremioCte());
		tabelaFreteCarreteiroCe.setPcPremioEvento(tabelaFreteCarreteiroCeDTO.getPcPremioEvento());
		tabelaFreteCarreteiroCe.setPcPremioDiaria(tabelaFreteCarreteiroCeDTO.getPcPremioDiaria());
		tabelaFreteCarreteiroCe.setPcPremioVolume(tabelaFreteCarreteiroCeDTO.getPcPremioVolume());
		tabelaFreteCarreteiroCe.setPcPremioSaida(tabelaFreteCarreteiroCeDTO.getPcPremioSaida());
		tabelaFreteCarreteiroCe.setPcPremioFreteBruto(tabelaFreteCarreteiroCeDTO.getPcPremioFreteBruto());
		tabelaFreteCarreteiroCe.setPcPremioFreteLiq(tabelaFreteCarreteiroCeDTO.getPcPremioFreteLiq());
		tabelaFreteCarreteiroCe.setPcPremioMercadoria(tabelaFreteCarreteiroCeDTO.getPcPremioMercadoria());
		
		getFilial(tabelaFreteCarreteiroCeDTO, tabelaFreteCarreteiroCe);
		getTabelaClonada(tabelaFreteCarreteiroCeDTO, tabelaFreteCarreteiroCe);
		getUsuarioCriacao(tabelaFreteCarreteiroCeDTO, tabelaFreteCarreteiroCe);
		getUsuarioAlteracao(tabelaFreteCarreteiroCeDTO, tabelaFreteCarreteiroCe);
		getListTabelaFcValores(tabelaFreteCarreteiroCeDTO, tabelaFreteCarreteiroCe);
		
		return tabelaFreteCarreteiroCe;
	}
	
	/**
	 * 
	 * @param resultSetPage
	 * @return List<TabelaFreteCarreteiroCeDTO>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<TabelaFreteCarreteiroCeRestDTO> getListTabelaFreteCarreteiroCeDTO(ResultSetPage resultSetPage){
		List<TabelaFreteCarreteiroCeRestDTO> result = new ArrayList<TabelaFreteCarreteiroCeRestDTO>();
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> tabelaFreteCarreteiroCe : list) {
			TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO = new TabelaFreteCarreteiroCeRestDTO();
			tabelaFreteCarreteiroCeDTO.setIdTabelaFreteCarreteiroCe(MapUtils.getLong(tabelaFreteCarreteiroCe, "idTabelaFreteCarreteiroCe"));
			tabelaFreteCarreteiroCeDTO.setNrTabelaFreteCarreteiroCe(MapUtils.getLong(tabelaFreteCarreteiroCe, "nrTabelaFreteCarreteiroCe"));
			tabelaFreteCarreteiroCeDTO.setDhVigenciaInicial((DateTime) MapUtils.getObject(tabelaFreteCarreteiroCe, "dhVigenciaInicial"));
			tabelaFreteCarreteiroCeDTO.setDhVigenciaFinal((DateTime) MapUtils.getObject(tabelaFreteCarreteiroCe, "dhVigenciaFinal"));
			tabelaFreteCarreteiroCeDTO.setDtAtualizacao((YearMonthDay) MapUtils.getObject(tabelaFreteCarreteiroCe, "dtAtualizacao"));
			tabelaFreteCarreteiroCeDTO.setTpVinculo((DomainValue) MapUtils.getObject(tabelaFreteCarreteiroCe, "tpVinculo"));
			tabelaFreteCarreteiroCeDTO.setTpOperacao((DomainValue) MapUtils.getObject(tabelaFreteCarreteiroCe, "tpOperacao"));
									
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setNmFilial(MapUtils.getString(tabelaFreteCarreteiroCe, "nmFantasia"));
			filialDTO.setSgFilial(MapUtils.getString(tabelaFreteCarreteiroCe, "sgFilial"));
			
			tabelaFreteCarreteiroCeDTO.setFilial(filialDTO);
			
			/*
			 * Necessário informar o ID para o framework.
			 */
			tabelaFreteCarreteiroCeDTO.setId(tabelaFreteCarreteiroCeDTO.getIdTabelaFreteCarreteiroCe());
			
			result.add(tabelaFreteCarreteiroCeDTO);
		}
		
		return result;
	}
	
	/**
	 * @param tabelaFreteCarreteiroCeDTO
	 * @param tabelaFreteCarreteiroCe
	 */
	private static void getFilial(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		Filial filial = new Filial();
		filial.setIdFilial(tabelaFreteCarreteiroCeDTO.getFilial().getIdFilial());
		
		tabelaFreteCarreteiroCe.setFilial(filial);
	}
	
	/**
	 * @param tabelaFreteCarreteiroCeDTO
	 * @param tabelaFreteCarreteiroCe
	 */
	private static void getUsuarioCriacao(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {		
		if(tabelaFreteCarreteiroCeDTO.getUsuarioCriacao() == null){
			return;
		}
		
		UsuarioLMS usuario = converteUsuarioDTOToUsuarioLMS(tabelaFreteCarreteiroCeDTO.getUsuarioCriacao());
		
		tabelaFreteCarreteiroCe.setUsuarioCriacao(usuario);
	}

	private static UsuarioLMS converteUsuarioDTOToUsuarioLMS(UsuarioDTO usuarioDTO) {
		if(usuarioDTO == null){
			return null;
		}
		
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(usuarioDTO.getIdUsuario());
		return usuario;
	}
	
	/**
	 * @param tabelaFreteCarreteiroCeDTO
	 * @param tabelaFreteCarreteiroCe
	 */
	private static void getUsuarioAlteracao(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		if(tabelaFreteCarreteiroCeDTO.getUsuarioAlteracao() == null){
			return;
		}
		
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(tabelaFreteCarreteiroCeDTO.getUsuarioAlteracao().getIdUsuario());
		
		tabelaFreteCarreteiroCe.setUsuarioAlteracao(usuario);
	}
	
	/**
	 * @param tabelaFreteCarreteiroCeDTO
	 * @param tabelaFreteCarreteiroCe
	 */
	private static void getTabelaClonada(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {		
		if(tabelaFreteCarreteiroCeDTO.getIdTabelaClonada() == null){
			return;
		}
		
		TabelaFreteCarreteiroCe tabelaClonada = new TabelaFreteCarreteiroCe();
		tabelaClonada.setIdTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCeDTO.getIdTabelaClonada());
		
		tabelaFreteCarreteiroCe.setTabelaClonada(tabelaClonada);
	}
	
	/**
	 * @param tabelaFreteCarreteiroCeRestDTO
	 * @param tabelaFreteCarreteiroCe
	 */
	private static void getListTabelaFcValores(TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeRestDTO,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		List<TabelaFcValoresRestDTO> listTabelaFcValoresRestDTO = tabelaFreteCarreteiroCeRestDTO.getListTabelaFcValoresRest();
		
		if(listTabelaFcValoresRestDTO == null){
			return;
		}		
		
		List<TabelaFcValores> listTabelaFcValores = new ArrayList<TabelaFcValores>();		
		
		for (TabelaFcValoresRestDTO tabelaFcValoresRestDTO : listTabelaFcValoresRestDTO) {			
			listTabelaFcValores.add(getTabelaFcValores(tabelaFcValoresRestDTO));
		}
		
		tabelaFreteCarreteiroCe.setListTabelaFcValores(listTabelaFcValores);
	}
	
	
	/**
	 * @param tabelaFreteCarreteiroCe
	 * @param tabelaFreteCarreteiroCeDTO
	 */
	private static void getFilialDTO(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe,
			TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO) {
		if(tabelaFreteCarreteiroCe.getFilial() != null){		
			Filial filial = tabelaFreteCarreteiroCe.getFilial();
			
			FilialSuggestDTO filialDTO = new FilialSuggestDTO();
			filialDTO.setIdFilial(filial.getIdFilial());
			filialDTO.setNmFilial(filial.getPessoa().getNmFantasia());
			filialDTO.setSgFilial(filial.getSgFilial());
			
			tabelaFreteCarreteiroCeDTO.setFilial(filialDTO);
		}
	}
	
	private static void getTabelaClonada(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe,
			TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO){
		if(tabelaFreteCarreteiroCe.getTabelaClonada() == null){		
			return;
		}
		
		tabelaFreteCarreteiroCeDTO.setIdTabelaClonada(tabelaFreteCarreteiroCe.getTabelaClonada().getIdTabelaFreteCarreteiroCe());
		tabelaFreteCarreteiroCeDTO.setNrTabelaClonada(tabelaFreteCarreteiroCe.getTabelaClonada().getNrTabelaFreteCarreteiroCe());
		tabelaFreteCarreteiroCeDTO.setSgFilialTabelaClonada(tabelaFreteCarreteiroCe.getTabelaClonada().getFilial().getSgFilial());
	}
		
	/**
	 * @param tabelaFreteCarreteiroCe
	 * @param tabelaFreteCarreteiroCeDTO
	 */
	private static void getUsuarioCriacaoDTO(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe,
			TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO) {
		if(tabelaFreteCarreteiroCe.getUsuarioCriacao() == null){
			return;
		}
		
		UsuarioDTO usuarioDTO = converteUsuarioToUsuarioDTO(tabelaFreteCarreteiroCe.getUsuarioCriacao());		
		
		tabelaFreteCarreteiroCeDTO.setUsuarioCriacao(usuarioDTO);		
	}
	
	/**
	 * @param tabelaFreteCarreteiroCe
	 * @param tabelaFreteCarreteiroCeDTO
	 */
	private static void getUsuarioAlteracaoDTO(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe,
			TabelaFreteCarreteiroCeRestDTO tabelaFreteCarreteiroCeDTO) {
		if(tabelaFreteCarreteiroCe.getUsuarioAlteracao() == null){
			return;
		}
		
		UsuarioDTO usuarioDTO = converteUsuarioToUsuarioDTO(tabelaFreteCarreteiroCe.getUsuarioAlteracao());
		
		tabelaFreteCarreteiroCeDTO.setUsuarioAlteracao(usuarioDTO);		
	}

	private static UsuarioDTO converteUsuarioToUsuarioDTO(UsuarioLMS usuario) {
		if(usuario == null){
			return null;
		}
		
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setIdUsuario(usuario.getIdUsuario());
		usuarioDTO.setNmUsuario(usuario.getUsuarioADSM().getNmUsuario());
		usuarioDTO.setNrMatricula(usuario.getUsuarioADSM().getNrMatricula());
		return usuarioDTO;
	}

	/**
	 *   
	 * @param resultSetPage
	 * @return List<Map<String, Object>> 
	 */
	public static List<Map<String, Object>> getListForReport(ResultSetPage<Map<String, Object>> resultSetPage) {		
		if(resultSetPage == null || resultSetPage.getList().isEmpty()){
			return new ArrayList<Map<String, Object>>();
		}
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> map : list) {			
			map.put("nrIdentificacaoProprietarioFormatado", formatIdentificacao(map.get("tpIdentificacaoProprietario"), map.get("nrIdentificacaoProprietario")));						
			map.put("nrIdentificacaoClienteFormatado", formatIdentificacao(map.get("tpIdentificacaoCliente"), map.get("nrIdentificacaoCliente")));
		}
		
		return resultSetPage.getList();
	}
	
	private static String formatIdentificacao(Object tpIdentificacao, Object nrIdentificacao){
		return FormatUtils.formatIdentificacao((DomainValue) tpIdentificacao, (String) nrIdentificacao);
	}

	/**
	 * Verifica se existe algum valor defindo além de zero na tabela.
	 * 
	 * @param tabelaFcValoresDTO
	 * 
	 * @return Boolean
	 */
	public static Boolean isZeroTabelaFcValores(TabelaFcValoresRestDTO tabelaFcValoresDTO){
		return getListFatorCalculoDTO(tabelaFcValoresDTO).isEmpty();
	}
	
	private static List<FatorCalculoDTO> getListFatorCalculoDTO(TabelaFcValoresRestDTO tabelaFcValoresDTO){
		List<FatorCalculoDTO> fatores = new ArrayList<FatorCalculoDTO>();
		
		testZero(tabelaFcValoresDTO.getVlConhecimento(), "conhecimento", fatores);
        testZero(tabelaFcValoresDTO.getVlEvento(), "evento", fatores);
        testZero(tabelaFcValoresDTO.getVlVolume(), "volume", fatores);
        testZero(tabelaFcValoresDTO.getVlPalete(), "palete", fatores);
        testZero(tabelaFcValoresDTO.getVlTransferencia(), "transferencia", fatores);
        testZero(tabelaFcValoresDTO.getVlAjudante(), "ajudante", fatores);
        testZero(tabelaFcValoresDTO.getVlHora(), "hora", fatores);
        testZero(tabelaFcValoresDTO.getVlDiaria(), "diaria", fatores);
        testZero(tabelaFcValoresDTO.getVlPreDiaria(), "porSaida", fatores);
        testZero(tabelaFcValoresDTO.getVlDedicado(), "dedicado", fatores);
        testZero(tabelaFcValoresDTO.getVlPernoite(), "pernoite", fatores);
        testZero(tabelaFcValoresDTO.getVlCapataziaCliente(), "capataziaCliente", fatores);
        testZero(tabelaFcValoresDTO.getVlLocacaoCarreta(), "locacaoCarreta", fatores);
        testZero(tabelaFcValoresDTO.getVlPremio(), "premio", fatores);
        testZero(tabelaFcValoresDTO.getVlKmExcedente(), "kmExcedente", fatores);
        testZero(tabelaFcValoresDTO.getVlFreteMinimo(), "valorFreteBruto", fatores);
        testZero(tabelaFcValoresDTO.getPcFrete(), "pcFreteBruto", fatores);
        testZero(tabelaFcValoresDTO.getVlMercadoriaMinimo(), "valorMercadoria", fatores);
        testZero(tabelaFcValoresDTO.getPcMercadoria(), "pcMercadoria", fatores);        
        testZero(tabelaFcValoresDTO.getVlFreteMinimoLiq(), "valorFreteLiquido", fatores);
        testZero(tabelaFcValoresDTO.getPcFreteLiq(), "pcFreteLiquido", fatores);
        
        return fatores;
	}
	
	private static void testZero(BigDecimal valor, String nome, List<FatorCalculoDTO> fatores){
		if(fatores.size() > MIN_ITEMS_TO_DISPLAY){
			return;
		}
		
		if(valor != null && valor.compareTo(new BigDecimal(0)) > 0){
			FatorCalculoDTO fatorCalculoDTO = new FatorCalculoDTO();
			fatorCalculoDTO.setNome(nome);
			fatorCalculoDTO.setValor(valor);
			
			fatores.add(fatorCalculoDTO);
		}
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<TabelaFcValoresRestDTO> getListTabelaFcValoresRestDTO(ResultSetPage resultSetPage) {		
		List<TabelaFcValoresRestDTO> result = new ArrayList<TabelaFcValoresRestDTO>();
		
		List<Map<String, Object>> list = resultSetPage.getList();
		
		for (Map<String, Object> tabelaFreteCarreteiroCe : list) {
			TabelaFcValoresRestDTO tabelaFcValoresDTO = new TabelaFcValoresRestDTO();
			tabelaFcValoresDTO.setIdTabelaFcValores(MapUtils.getLong(tabelaFreteCarreteiroCe, "idTabelaFcValores"));
			tabelaFcValoresDTO.setIdTabelaFreteCarreteiroCe(MapUtils.getLong(tabelaFreteCarreteiroCe, "idTabelaFreteCarreteiroCe"));
			tabelaFcValoresDTO.setVlConhecimento((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlConhecimento"));
	        tabelaFcValoresDTO.setVlEvento((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlEvento"));
	        tabelaFcValoresDTO.setVlVolume((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlVolume"));
	        tabelaFcValoresDTO.setVlPalete((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlPalete"));
	        tabelaFcValoresDTO.setVlTransferencia((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlTransferencia"));
	        tabelaFcValoresDTO.setVlAjudante((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlAjudante"));
	        tabelaFcValoresDTO.setVlHora((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlHora"));
	        tabelaFcValoresDTO.setVlDiaria((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlDiaria"));
	        tabelaFcValoresDTO.setVlPreDiaria((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlPreDiaria"));
	        tabelaFcValoresDTO.setVlDedicado((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlDedicado"));
	        tabelaFcValoresDTO.setVlPernoite((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlPernoite"));
	        tabelaFcValoresDTO.setVlCapataziaCliente((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlCapataziaCliente"));
	        tabelaFcValoresDTO.setVlLocacaoCarreta((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlLocacaoCarreta"));
	        tabelaFcValoresDTO.setVlPremio((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlPremio"));
	        tabelaFcValoresDTO.setVlKmExcedente((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlKmExcedente"));
	        tabelaFcValoresDTO.setVlFreteMinimo((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlFreteMinimo"));
	        tabelaFcValoresDTO.setPcFrete((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "pcFrete"));
	        tabelaFcValoresDTO.setVlMercadoriaMinimo((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlMercadoriaMinimo"));
	        tabelaFcValoresDTO.setPcMercadoria((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "pcMercadoria"));
	        tabelaFcValoresDTO.setQtAjudante(MapUtils.getInteger(tabelaFreteCarreteiroCe, "qtAjudante"));
	        tabelaFcValoresDTO.setVlFreteMinimoLiq((BigDecimal) MapUtils.getObject(tabelaFreteCarreteiroCe, "vlFreteMinimoLiq"));
						
			/* set meio transporte. */
			MeioTransporteSuggestDTO meioTransporteDTO = new MeioTransporteSuggestDTO();
			meioTransporteDTO.setIdMeioTransporte(MapUtils.getLong(tabelaFreteCarreteiroCe, "idMeioTransporte"));
			meioTransporteDTO.setNrFrota(MapUtils.getString(tabelaFreteCarreteiroCe, "nrFrota"));
			meioTransporteDTO.setNrIdentificador(MapUtils.getString(tabelaFreteCarreteiroCe, "nrIdentificador"));
			
			tabelaFcValoresDTO.setMeioTransporte(meioTransporteDTO);
			
			/* set rota coleta entrega. */
			RotaColetaEntregaSuggestDTO rotaColetaEntregaDTO = new RotaColetaEntregaSuggestDTO();
			rotaColetaEntregaDTO.setIdRotaColetaEntrega(MapUtils.getLong(tabelaFreteCarreteiroCe, "idRotaColetaEntrega"));
			rotaColetaEntregaDTO.setDsRota(MapUtils.getString(tabelaFreteCarreteiroCe, "dsRota"));
			
			tabelaFcValoresDTO.setRotaColetaEntrega(rotaColetaEntregaDTO);
			
			/* set proprietário. */
			ProprietarioDTO proprietarioDTO = new ProprietarioDTO();
			proprietarioDTO.setIdProprietario(MapUtils.getLong(tabelaFreteCarreteiroCe, "idProprietario"));
			proprietarioDTO.setNmPessoa(MapUtils.getString(tabelaFreteCarreteiroCe, "nmPessoaProprietario"));
			proprietarioDTO.setNrIdentificacao(MapUtils.getString(tabelaFreteCarreteiroCe, "nrIdentificacaoProprietario"));
			proprietarioDTO.setTpIdentificacao((DomainValue) MapUtils.getObject(tabelaFreteCarreteiroCe, "tpIdentificacaoProprietario"));
			
			tabelaFcValoresDTO.setProprietario(proprietarioDTO);
			
			/* set cliente. */
			ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO();
			clienteDTO.setIdCliente(MapUtils.getLong(tabelaFreteCarreteiroCe, "idCliente"));
			clienteDTO.setNmPessoa(MapUtils.getString(tabelaFreteCarreteiroCe, "nmPessoaCliente"));
			clienteDTO.setNrIdentificacao(MapUtils.getString(tabelaFreteCarreteiroCe, "nrIdentificacaoCliente"));
			clienteDTO.setTpIdentificacao((DomainValue) MapUtils.getObject(tabelaFreteCarreteiroCe, "tpIdentificacaoCliente"));
			
			tabelaFcValoresDTO.setCliente(clienteDTO);
			
			/* set município. */
			MunicipioDTO municipioDTO = new MunicipioDTO();
			municipioDTO.setIdMunicipio(MapUtils.getLong(tabelaFreteCarreteiroCe, "idMunicipio"));
			municipioDTO.setNmMunicipio(MapUtils.getString(tabelaFreteCarreteiroCe, "nmMunicipio"));
			
			tabelaFcValoresDTO.setMunicipio(municipioDTO);			
			
			/* set tipo meio transporte. */
			TipoMeioTransporteDTO tipoMeioTransporteDTO = new TipoMeioTransporteDTO();
			tipoMeioTransporteDTO.setIdTipoMeioTransporte(MapUtils.getLong(tabelaFreteCarreteiroCe, "idTipoMeioTransporte"));
			tipoMeioTransporteDTO.setDsTipoMeioTransporte(MapUtils.getString(tabelaFreteCarreteiroCe, "dsTipoMeioTransporte"));
			
			tabelaFcValoresDTO.setTipoMeioTransporte(tipoMeioTransporteDTO);
			
			tabelaFcValoresDTO.setListFatorCalculo(getListFatorCalculoDTO(tabelaFcValoresDTO));
			
			/*
			 * Necessário informar o ID para o framework.
			 */
			tabelaFcValoresDTO.setId(tabelaFcValoresDTO.getIdTabelaFcValores());
			
			result.add(tabelaFcValoresDTO);
		}
		
		return result;
	}

	public static TabelaFcFaixaPesoRestDTO getTabelaFcFaixaPesoDTO(TabelaFcFaixaPeso tabelaFcFaixaPeso) {
		TabelaFcFaixaPesoRestDTO tabelaFcFaixaPesoDTO = new TabelaFcFaixaPesoRestDTO();
		tabelaFcFaixaPesoDTO.setIdTabelaFcFaixaPeso(tabelaFcFaixaPeso.getIdTabelaFcFaixaPeso());        
		tabelaFcFaixaPesoDTO.setTpFator(tabelaFcFaixaPeso.getTpFator());
		tabelaFcFaixaPesoDTO.setPsInicial(tabelaFcFaixaPeso.getPsInicial());
		tabelaFcFaixaPesoDTO.setPsFinal(tabelaFcFaixaPeso.getPsFinal());
		tabelaFcFaixaPesoDTO.setVlValor(tabelaFcFaixaPeso.getVlValor());
		tabelaFcFaixaPesoDTO.setBlCalculoFaixaUnica(BooleanUtils.isTrue(tabelaFcFaixaPeso.getBlCalculoFaixaUnica()));
		tabelaFcFaixaPesoDTO.setTpFatorSegundo(tabelaFcFaixaPeso.getTpFatorSegundo());
		tabelaFcFaixaPesoDTO.setVlValorSegundo(tabelaFcFaixaPeso.getVlValorSegundo());
        
        TabelaFcValoresRestDTO tabelaFcValoresDTO = new TabelaFcValoresRestDTO();
        tabelaFcValoresDTO.setIdTabelaFcValores(tabelaFcFaixaPeso.getTabelaFcValores().getIdTabelaFcValores());
        tabelaFcFaixaPesoDTO.setTabelaFcValores(tabelaFcValoresDTO);
        
		return tabelaFcFaixaPesoDTO;
	}
	
	public static TabelaFcFaixaPeso getTabelaFcFaixaPeso(TabelaFcFaixaPesoRestDTO tabelaFcFaixaPesoDTO) {
		TabelaFcFaixaPeso tabelaFcFaixaPeso = new TabelaFcFaixaPeso();
		    
        tabelaFcFaixaPeso.setTpFator(tabelaFcFaixaPesoDTO.getTpFator());    
        tabelaFcFaixaPeso.setPsInicial(tabelaFcFaixaPesoDTO.getPsInicial());    
        tabelaFcFaixaPeso.setPsFinal(tabelaFcFaixaPesoDTO.getPsFinal());    
        tabelaFcFaixaPeso.setVlValor(tabelaFcFaixaPesoDTO.getVlValor());
        tabelaFcFaixaPeso.setBlCalculoFaixaUnica(BooleanUtils.isTrue(tabelaFcFaixaPesoDTO.getBlCalculoFaixaUnica()));
        tabelaFcFaixaPeso.setTpFatorSegundo(tabelaFcFaixaPesoDTO.getTpFatorSegundo());
        tabelaFcFaixaPeso.setVlValorSegundo(tabelaFcFaixaPesoDTO.getVlValorSegundo());
        
        if(tabelaFcFaixaPesoDTO.getTabelaFcValores() != null){
        	TabelaFcValores tabelaFcValores = new TabelaFcValores();
        	tabelaFcValores.setIdTabelaFcValores(tabelaFcFaixaPesoDTO.getTabelaFcValores().getIdTabelaFcValores());
    		
    		tabelaFcFaixaPeso.setTabelaFcValores(tabelaFcValores);
        }   
        
		return tabelaFcFaixaPeso;
	}
	
	public static TabelaFcValoresRestDTO getTabelaFcValoresRestDTO(TabelaFcValores tabelaFcValores) {
		TabelaFcValoresRestDTO tabelaFcValoresDTO = new TabelaFcValoresRestDTO();
		
        tabelaFcValoresDTO.setIdTabelaFcValores(tabelaFcValores.getIdTabelaFcValores());
        tabelaFcValoresDTO.setIdTabelaFreteCarreteiroCe(tabelaFcValores.getTabelaFreteCarreteiroCe().getIdTabelaFreteCarreteiroCe());
        tabelaFcValoresDTO.setDtCriacao(tabelaFcValores.getDtCriacao());
        tabelaFcValoresDTO.setVlConhecimento(tabelaFcValores.getVlConhecimento());
        tabelaFcValoresDTO.setVlEvento(tabelaFcValores.getVlEvento());
        tabelaFcValoresDTO.setVlVolume(tabelaFcValores.getVlVolume());
        tabelaFcValoresDTO.setVlPalete(tabelaFcValores.getVlPalete());
        tabelaFcValoresDTO.setVlTransferencia(tabelaFcValores.getVlTransferencia());
        tabelaFcValoresDTO.setVlAjudante(tabelaFcValores.getVlAjudante());
        tabelaFcValoresDTO.setVlHora(tabelaFcValores.getVlHora());
        tabelaFcValoresDTO.setVlDiaria(tabelaFcValores.getVlDiaria());
        tabelaFcValoresDTO.setVlPreDiaria(tabelaFcValores.getVlPreDiaria());
        tabelaFcValoresDTO.setVlDedicado(tabelaFcValores.getVlDedicado());
        tabelaFcValoresDTO.setVlPernoite(tabelaFcValores.getVlPernoite());
        tabelaFcValoresDTO.setVlCapataziaCliente(tabelaFcValores.getVlCapataziaCliente());
        tabelaFcValoresDTO.setVlLocacaoCarreta(tabelaFcValores.getVlLocacaoCarreta());
        tabelaFcValoresDTO.setVlPremio(tabelaFcValores.getVlPremio());
        tabelaFcValoresDTO.setVlKmExcedente(tabelaFcValores.getVlKmExcedente());
        tabelaFcValoresDTO.setVlFreteMinimo(tabelaFcValores.getVlFreteMinimo());
        tabelaFcValoresDTO.setPcFrete(tabelaFcValores.getPcFrete());
        tabelaFcValoresDTO.setVlMercadoriaMinimo(tabelaFcValores.getVlmercadoriaMinimo());
        tabelaFcValoresDTO.setPcMercadoria(tabelaFcValores.getPcMercadoria());
        tabelaFcValoresDTO.setQtAjudante(tabelaFcValores.getQtAjudante());
        tabelaFcValoresDTO.setVlFreteMinimoLiq(tabelaFcValores.getVlFreteMinimoLiq());
        tabelaFcValoresDTO.setPcFreteLiq(tabelaFcValores.getPcFreteLiq());
        tabelaFcValoresDTO.setBlGeral(tabelaFcValores.getBlGeral());
        tabelaFcValoresDTO.setBlTipo(tabelaFcValores.getBlTipo());
        
        tabelaFcValoresDTO.setUsuarioCriacao(converteUsuarioToUsuarioDTO(tabelaFcValores.getUsuarioCriacao()));        
        tabelaFcValoresDTO.setCliente(converteClienteToClienteDTO(tabelaFcValores.getCliente()));
        tabelaFcValoresDTO.setMeioTransporte(converteMeioTransporteToMeioTransporteDTO(tabelaFcValores.getMeioTransporte()));
        tabelaFcValoresDTO.setTipoMeioTransporte(converteTipoMeioTransporteToTipoMeioTransporteDTO(tabelaFcValores.getTipoMeioTransporte()));
        tabelaFcValoresDTO.setProprietario(converteProprietarioToProprietarioDTO(tabelaFcValores.getProprietario()));
        
        setMunicipioDTO(tabelaFcValoresDTO, tabelaFcValores);
        setRotaColetaEntregaDTO(tabelaFcValoresDTO, tabelaFcValores);
        
        tabelaFcValoresDTO.setListTabelaFcFaixaPeso(getListTabelaFcFaixaPesoDTO(tabelaFcValores.getListTabelaFcFaixaPeso()));
        tabelaFcValoresDTO.setDestinatario(getBlDestinatario(tabelaFcValores));
        
		return tabelaFcValoresDTO;
	}
	
	private static Boolean getBlDestinatario(TabelaFcValores tabelaFcValores) {
		if(CLIENTE_DEDICADO.equals(tabelaFcValores.getBlTipo().getValue())){
			return true;
		}
		return false;
	}

	private static List<TabelaFcFaixaPesoRestDTO> getListTabelaFcFaixaPesoDTO(List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso) {
		List<TabelaFcFaixaPesoRestDTO> listTabelaFcFaixaPesoDTO = new ArrayList<TabelaFcFaixaPesoRestDTO>();
		
		for (TabelaFcFaixaPeso tabelaFcFaixaPeso : listTabelaFcFaixaPeso) {
			listTabelaFcFaixaPesoDTO.add(getTabelaFcFaixaPesoDTO(tabelaFcFaixaPeso));
		}
		
		return listTabelaFcFaixaPesoDTO;
	}

	private static void setMunicipioDTO(TabelaFcValoresRestDTO tabelaFcValoresDTO, TabelaFcValores tabelaFcValores) {
		Municipio municipio = tabelaFcValores.getMunicipio();
		
		if(municipio == null){
			return;
		}
		
		UnidadeFederativa unidadeFederativa = municipio.getUnidadeFederativa();
		tabelaFcValoresDTO.setUnidadeFederativa(new UnidadeFederativaDTO(
				unidadeFederativa.getIdUnidadeFederativa(), 
				unidadeFederativa.getSgUnidadeFederativa(),
				unidadeFederativa.getNmUnidadeFederativa()));
		
		Pais pais = unidadeFederativa.getPais();		
		tabelaFcValoresDTO.setPais(new PaisDTO(pais.getIdPais(), pais.getCdIso(), pais.getSgPais(), pais.getNmPais().getValue()));
		
		MunicipioDTO municipioDTO = new MunicipioDTO();
		municipioDTO.setIdMunicipio(municipio.getIdMunicipio());
		municipioDTO.setNmMunicipio(municipio.getNmMunicipio());
		
		tabelaFcValoresDTO.setMunicipio(municipioDTO);		
	}
	
	private static void setRotaColetaEntregaDTO(TabelaFcValoresRestDTO tabelaFcValoresDTO, TabelaFcValores tabelaFcValores){
		RotaColetaEntrega rotaColetaEntrega = tabelaFcValores.getRotaColetaEntrega();
		
		if(rotaColetaEntrega == null){
			return;
		}
		
		RotaColetaEntregaSuggestDTO rotaColetaEntregaDTO = new RotaColetaEntregaSuggestDTO();
		rotaColetaEntregaDTO.setIdRotaColetaEntrega(rotaColetaEntrega.getIdRotaColetaEntrega());
		rotaColetaEntregaDTO.setNrRota(rotaColetaEntrega.getNrRota());
		rotaColetaEntregaDTO.setNrKm(rotaColetaEntrega.getNrKm());
		rotaColetaEntregaDTO.setDsRota(rotaColetaEntrega.getDsRota());
		
		FilialSuggestDTO filialDTO = new FilialSuggestDTO();
		filialDTO.setIdFilial(rotaColetaEntrega.getFilial().getIdFilial());
		filialDTO.setSgFilial(rotaColetaEntrega.getFilial().getSgFilial());
		filialDTO.setNmFilial(rotaColetaEntrega.getFilial().getPessoa().getNmFantasia());
		
		tabelaFcValoresDTO.setRotaColetaEntrega(rotaColetaEntregaDTO);
		tabelaFcValoresDTO.setFilialRotaColetaEntrega(filialDTO);
	}
	
	private static ProprietarioDTO converteProprietarioToProprietarioDTO(Proprietario proprietario) {
		if(proprietario == null){
			return null;
		}
		
		ProprietarioDTO proprietarioDTO = new ProprietarioDTO();
		proprietarioDTO.setIdProprietario(proprietario.getIdProprietario());
		proprietarioDTO.setNmPessoa(proprietario.getPessoa().getNmPessoa());
		proprietarioDTO.setNrIdentificacao(
				FormatUtils.formatIdentificacao(
						proprietario.getPessoa().getTpIdentificacao(), 
						proprietario.getPessoa().getNrIdentificacao()));
		
		return proprietarioDTO;
	}
	
	private static MeioTransporteSuggestDTO converteMeioTransporteToMeioTransporteDTO(MeioTransporte meioTransporte) {
		if(meioTransporte == null){
			return null;
		}
		
		MeioTransporteSuggestDTO meioTransporteDTO = new MeioTransporteSuggestDTO();
		meioTransporteDTO.setIdMeioTransporte(meioTransporte.getIdMeioTransporte());
		meioTransporteDTO.setNrFrota(meioTransporte.getNrFrota());
		meioTransporteDTO.setNrIdentificador(meioTransporte.getNrIdentificador());
		return meioTransporteDTO;
	}
	
	private static TipoMeioTransporteDTO converteTipoMeioTransporteToTipoMeioTransporteDTO(TipoMeioTransporte tipoMeioTransporte) {
		if(tipoMeioTransporte == null){
			return null;
		}
		
		TipoMeioTransporteDTO tipoMeioTransporteDTO = new TipoMeioTransporteDTO();
		tipoMeioTransporteDTO.setIdTipoMeioTransporte(tipoMeioTransporte.getIdTipoMeioTransporte());
		tipoMeioTransporteDTO.setDsTipoMeioTransporte(tipoMeioTransporte.getDsTipoMeioTransporte());
		return tipoMeioTransporteDTO;
	}

	private static ClienteSuggestDTO converteClienteToClienteDTO(Cliente cliente) {
		if(cliente == null){
			return null;
		}
		
		ClienteSuggestDTO clienteDTO = new ClienteSuggestDTO();
		clienteDTO.setIdCliente(cliente.getIdCliente());
		clienteDTO.setNmPessoa(cliente.getPessoa().getNmPessoa());
		clienteDTO.setNrIdentificacao(
				FormatUtils.formatIdentificacao(
						cliente.getPessoa().getTpIdentificacao(), 
						cliente.getPessoa().getNrIdentificacao()));
		
		return clienteDTO;
	}

	public static TabelaFcValores getTabelaFcValores(TabelaFcValoresRestDTO tabelaFcValoresDTO) {
		TabelaFcValores tabelaFcValores = new TabelaFcValores();
		    
        tabelaFcValores.setIdTabelaFcValores(tabelaFcValoresDTO.getIdTabelaFcValores());    
        tabelaFcValores.setDtCriacao(tabelaFcValoresDTO.getDtCriacao());
        tabelaFcValores.setVlConhecimento(tabelaFcValoresDTO.getVlConhecimento());    
        tabelaFcValores.setVlEvento(tabelaFcValoresDTO.getVlEvento());    
        tabelaFcValores.setVlVolume(tabelaFcValoresDTO.getVlVolume());    
        tabelaFcValores.setVlPalete(tabelaFcValoresDTO.getVlPalete());    
        tabelaFcValores.setVlTransferencia(tabelaFcValoresDTO.getVlTransferencia());    
        tabelaFcValores.setVlAjudante(tabelaFcValoresDTO.getVlAjudante());    
        tabelaFcValores.setVlHora(tabelaFcValoresDTO.getVlHora());    
        tabelaFcValores.setVlDiaria(tabelaFcValoresDTO.getVlDiaria());    
        tabelaFcValores.setVlPreDiaria(tabelaFcValoresDTO.getVlPreDiaria());    
        tabelaFcValores.setVlDedicado(tabelaFcValoresDTO.getVlDedicado());    
        tabelaFcValores.setVlPernoite(tabelaFcValoresDTO.getVlPernoite());    
        tabelaFcValores.setVlCapataziaCliente(tabelaFcValoresDTO.getVlCapataziaCliente());    
        tabelaFcValores.setVlLocacaoCarreta(tabelaFcValoresDTO.getVlLocacaoCarreta());    
        tabelaFcValores.setVlPremio(tabelaFcValoresDTO.getVlPremio());    
        tabelaFcValores.setVlKmExcedente(tabelaFcValoresDTO.getVlKmExcedente());
        tabelaFcValores.setVlFreteMinimo(tabelaFcValoresDTO.getVlFreteMinimo());    
        tabelaFcValores.setPcFrete(tabelaFcValoresDTO.getPcFrete());    
        tabelaFcValores.setVlmercadoriaMinimo(tabelaFcValoresDTO.getVlMercadoriaMinimo());    
        tabelaFcValores.setPcMercadoria(tabelaFcValoresDTO.getPcMercadoria());
        tabelaFcValores.setPcFreteLiq(tabelaFcValoresDTO.getPcFreteLiq());
        tabelaFcValores.setVlFreteMinimoLiq(tabelaFcValoresDTO.getVlFreteMinimoLiq());
        tabelaFcValores.setBlGeral(tabelaFcValoresDTO.getBlGeral());
        tabelaFcValores.setBlTipo(getBlTipo(tabelaFcValoresDTO));
        tabelaFcValores.setQtAjudante(tabelaFcValoresDTO.getQtAjudante());
        
        tabelaFcValores.setUsuarioCriacao(converteUsuarioDTOToUsuarioLMS(tabelaFcValoresDTO.getUsuarioCriacao()));//        
        tabelaFcValores.setCliente(converteClienteDTOToCliente(tabelaFcValoresDTO.getCliente()));
        tabelaFcValores.setTipoMeioTransporte(converteTipoMeioTransporteDTOToTipoMeioTransporte(tabelaFcValoresDTO.getTipoMeioTransporte()));
        tabelaFcValores.setMeioTransporte(converteMeioTransporteDTOToMeioTransporte(tabelaFcValoresDTO.getMeioTransporte()));
        tabelaFcValores.setProprietario(converteProprietarioDTOToProprietario(tabelaFcValoresDTO.getProprietario()));
        tabelaFcValores.setRotaColetaEntrega(converteRotaColetaEntregaDTOToRotaColetaEntrega(tabelaFcValoresDTO.getRotaColetaEntrega()));
        
        setMunicipio(tabelaFcValores, tabelaFcValoresDTO);        
        getTabelaFreteCarreteiroCe(tabelaFcValoresDTO, tabelaFcValores);        
        getListTabelaFcFaixaPeso(tabelaFcValoresDTO.getListTabelaFcFaixaPeso(), tabelaFcValores);
        
		return tabelaFcValores;
	}

	/**
	 * Popula uma entidade AnexoTabelaFreteCe.
	 * 
	 * @param dados
	 * @param data
	 * 
	 * @return AnexoTabelaFreteCe
	 * 
	 * @throws IOException
	 */
	public static AnexoTabelaFreteCe getAnexoTabelaFreteCe(Map<String, Object> dados, byte[] data) {						
		AnexoTabelaFreteCe anexoTabelaFreteCe = new AnexoTabelaFreteCe();
		anexoTabelaFreteCe.setDsAnexo(MapUtils.getString(dados, "dsAnexo"));
		anexoTabelaFreteCe.setDhCriacao(RestPopulateUtils.getYearMonthDayFromISO8601(dados, "dhCriacao").toDateTimeAtCurrentTime());
		anexoTabelaFreteCe.setDcArquivo(data);

		UsuarioLMS usuarioLMS = new UsuarioLMS();
    	usuarioLMS.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
    	anexoTabelaFreteCe.setUsuarioLMS(usuarioLMS);
    	
    	return anexoTabelaFreteCe;
	}
	
	private static DomainValue getBlTipo(TabelaFcValoresRestDTO tabelaFcValoresDTO) {
		if("CL".equals(tabelaFcValoresDTO.getBlTipo().getValue()) && Boolean.TRUE.equals(tabelaFcValoresDTO.getDestinatario())){
			return new DomainValue(CLIENTE_DEDICADO);
		}
		return tabelaFcValoresDTO.getBlTipo();
	}

	private static void getListTabelaFcFaixaPeso(List<TabelaFcFaixaPesoRestDTO> listTabelaFcFaixaPesoDTO, TabelaFcValores tabelaFcValores) {
		List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso = new ArrayList<TabelaFcFaixaPeso>();
		
		if(listTabelaFcFaixaPesoDTO == null){
			return;
		}
		
		for (TabelaFcFaixaPesoRestDTO tabelaFcFaixaPesoDTO : listTabelaFcFaixaPesoDTO) {
			listTabelaFcFaixaPeso.add(getTabelaFcFaixaPeso(tabelaFcFaixaPesoDTO));
		}
		
		tabelaFcValores.setListTabelaFcFaixaPeso(listTabelaFcFaixaPeso);
	}

	private static void setMunicipio(TabelaFcValores tabelaFcValores, TabelaFcValoresRestDTO tabelaFcValoresDTO) {
		MunicipioDTO municipioDTO = tabelaFcValoresDTO.getMunicipio();
		
		if(municipioDTO == null){
			return;
		}
		
		Municipio municipio = new Municipio();
		municipio.setIdMunicipio(municipioDTO.getIdMunicipio());
		municipio.setNmMunicipio(municipioDTO.getNmMunicipio());
	
		tabelaFcValores.setMunicipio(municipio);		
	}

	private static Cliente converteClienteDTOToCliente(ClienteSuggestDTO clienteDTO) {	
		if(clienteDTO == null){
			return null;
		}
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(clienteDTO.getIdCliente());	
		Pessoa p = new Pessoa();
		p.setNmPessoa(clienteDTO.getNmPessoa());
		p.setNrIdentificacao(clienteDTO.getNrIdentificacao());	
		cliente.setPessoa(p);
		
		return cliente;
	}
	
	private static TipoMeioTransporte converteTipoMeioTransporteDTOToTipoMeioTransporte(TipoMeioTransporteDTO tipoMeioTransporteDTO) {	
		if(tipoMeioTransporteDTO == null){
			return null;
		}
		
		TipoMeioTransporte tipoMeioTransporte = new TipoMeioTransporte();
		tipoMeioTransporte.setIdTipoMeioTransporte(tipoMeioTransporteDTO.getIdTipoMeioTransporte());
		
		return tipoMeioTransporte;
	}
	
	private static MeioTransporte converteMeioTransporteDTOToMeioTransporte(MeioTransporteSuggestDTO meioTransporteDTO) {	
		if(meioTransporteDTO == null){
			return null;
		}
		
		MeioTransporte tipoMeioTransporte = new MeioTransporte();
		tipoMeioTransporte.setIdMeioTransporte(meioTransporteDTO.getIdMeioTransporte());
		
		return tipoMeioTransporte;
	}
	
	private static Proprietario converteProprietarioDTOToProprietario(ProprietarioDTO proprietarioDTO) {	
		if(proprietarioDTO == null){
			return null;
		}
		
		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario(proprietarioDTO.getIdProprietario());
		
		return proprietario;
	}
	
	private static RotaColetaEntrega converteRotaColetaEntregaDTOToRotaColetaEntrega(RotaColetaEntregaSuggestDTO rotaColetaEntregaDTO) {	
		if(rotaColetaEntregaDTO == null){
			return null;
		}
		
		RotaColetaEntrega rotaColetaEntrega = new RotaColetaEntrega();
		rotaColetaEntrega.setIdRotaColetaEntrega(rotaColetaEntregaDTO.getIdRotaColetaEntrega());
		
		return rotaColetaEntrega;
	}

	private static void getTabelaFreteCarreteiroCe(TabelaFcValoresRestDTO tabelaFcValoresDTO, TabelaFcValores tabelaFcValores) {
		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = new TabelaFreteCarreteiroCe();
		tabelaFreteCarreteiroCe.setIdTabelaFreteCarreteiroCe(tabelaFcValoresDTO.getIdTabelaFreteCarreteiroCe());
		tabelaFcValores.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
	}
	
}
