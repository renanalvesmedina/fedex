package com.mercurio.lms.expedicao.model.enums;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.DomainValue;

public enum TipoDoctoServico {
	CONHECIMENTO_ELETRONICO("CTE","CT-e"),
	CONHECIMENTO_NACIONAL("CTR","CTRC"),
	MINUTA_DESPACHO_ACOMPANHAMENTO("MDA","MDA"),
	NOTA_FISCAL_SERVICO("NFS","NFS"),
	NOTA_FISCAL_TRANSPORTE("NFT","NFT"),
	NOTA_FISCAL_TRANSPORTE_ELETRONICA("NTE","RPS-t"),
	NOTA_FISCAL_SERVICO_ELETRONICA("NSE","RPS-s"),
	CONHECIMENTO_INTERNACIONAL("CRT","CRT"),
	NOTA_DEBITO_NACIONAL("NDN","NDN");
	
	private String value;
	private String description;
	
	private TipoDoctoServico(String value, String description) {
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
