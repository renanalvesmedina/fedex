package com.mercurio.lms.rest.vendas.dto;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RegionalSuggestDTO;

public class ClienteTerritorioFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private ClienteSuggestDTO cliente;
	private TerritorioSuggestDTO territorio;
	private DomainValue tpModal;
	private YearMonthDay dtInicio;
	private YearMonthDay dtFim;
	private FilialSuggestDTO filial;
	private RegionalSuggestDTO regional;

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public TerritorioSuggestDTO getTerritorio() {
		return territorio;
	}

	public void setTerritorio(TerritorioSuggestDTO territorio) {
		this.territorio = territorio;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public YearMonthDay getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}

	public YearMonthDay getDtFim() {
		return dtFim;
	}

	public void setDtFim(YearMonthDay dtFim) {
		this.dtFim = dtFim;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}
	
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public RegionalSuggestDTO getRegional() {
		return regional;
	}
	
	public void setRegional(RegionalSuggestDTO regional) {
		this.regional = regional;
	}
	
}
