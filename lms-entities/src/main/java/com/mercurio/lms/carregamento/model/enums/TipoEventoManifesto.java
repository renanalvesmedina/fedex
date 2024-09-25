package com.mercurio.lms.carregamento.model.enums;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;

public enum TipoEventoManifesto {
	CHEGADA_NA_PORTARIA("CP","Chegada na Portaria"),
	BLOQUEADO("BL","Bloqueado"),
	LIBERADO("LI","Liberado"),
	PRE_MANIFESTO_GERADO("PG","Pré-Manifesto Gerado"),
	CANCELAMENTO("CA","Cancelamento"),
	EMISSAO_MANIFESTO("EM","Emissão Manifesto"),
	INICIO_CARREGAMENTO("IC","Início Carregamento"),
	FECHAMENTO("FE","Fechamento"),
	FIM_CARREGAMENTO("FC","Fim Carregamento"),
	INICIO_DA_DESCARGA("ID","Início da Descarga"),
	FIM_DA_DESCARGA("FD","Fim da Descarga"),
	SAIDA_NA_PORTARIA("SP","Saída na Portaria");
	
	private String value;
	private String description;
	
	private TipoEventoManifesto(String value, String description) {
		this.value = value;
		this.description = description;
	}

	public String getValue() {
		return value;
	}

	public DomainValue getDomainValue() {
		return new DomainValue(value);
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean equals(String value) {
		return this.value.equals(value);
	}
	
	public boolean equals(DomainValue domainValue) {
		if(domainValue == null || StringUtils.isEmpty(domainValue.getValue())) {
			return false;
		}
		
		return this.value.equals(domainValue.getValue());
	}
}
