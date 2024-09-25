package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

public class ParcelasFreteWebService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dsParcela;
	private String vlParcela;
	private String vlParcelaBruto;
	private String idParcelaPreco;
	
	
	public String getDsParcela() {
		return dsParcela;
	}
	public void setDsParcela(String dsParcela) {
		this.dsParcela = dsParcela;
	}
	
	public String getVlParcela() {
		return vlParcela;
	}
	public void setVlParcela(String vlParcela) {
		this.vlParcela = vlParcela;
	}
	public String getVlParcelaBruto() {
		return vlParcelaBruto;
	}
	public void setVlParcelaBruto(String vlParcelaBruto) {
		this.vlParcelaBruto = vlParcelaBruto;
	}
	public String getIdParcelaPreco() {
		return idParcelaPreco;
	}
	public void setIdParcelaPreco(String idParcelaPreco) {
		this.idParcelaPreco = idParcelaPreco;
	}

}
