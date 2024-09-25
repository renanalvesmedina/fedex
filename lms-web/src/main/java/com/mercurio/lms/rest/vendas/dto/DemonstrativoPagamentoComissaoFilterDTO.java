package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class DemonstrativoPagamentoComissaoFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 8515872686513674732L;
	
	private UsuarioDTO executivo;
	private YearMonthDay dtInicio;
	private DomainValue tpModal;
	private FilialSuggestDTO filial;

	private boolean enableDemonstrativoField;
	private boolean enableDemonstrativoIndoor;
	private boolean enableDemonstrativoGerente;
	
	public UsuarioDTO getExecutivo() {
		return executivo;
	}
	public void setExecutivo(UsuarioDTO executivo) {
		this.executivo = executivo;
	}
	
	public YearMonthDay getDtInicio() {
		return dtInicio;
	}
	
	public void setDtInicio(YearMonthDay dtInicio) {
		this.dtInicio = dtInicio;
	}
	
	public DomainValue getTpModal() {
		return tpModal;
	}
	
	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}
	
	public FilialSuggestDTO getFilial() {
		return filial;
	}
	
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	
	public boolean isEnableDemonstrativoField() {
		return enableDemonstrativoField;
	}
	
	public void setEnableDemonstrativoField(boolean enableDemonstrativoField) {
		this.enableDemonstrativoField = enableDemonstrativoField;
	}
	
	public boolean isEnableDemonstrativoIndoor() {
		return enableDemonstrativoIndoor;
	}
	
	public void setEnableDemonstrativoIndoor(boolean enableDemonstrativoIndoor) {
		this.enableDemonstrativoIndoor = enableDemonstrativoIndoor;
	}
	
	public boolean isEnableDemonstrativoGerente() {
		return enableDemonstrativoGerente;
	}
	
	public void setEnableDemonstrativoGerente(boolean enableDemonstrativoGerente) {
		this.enableDemonstrativoGerente = enableDemonstrativoGerente;
	}
	
}
