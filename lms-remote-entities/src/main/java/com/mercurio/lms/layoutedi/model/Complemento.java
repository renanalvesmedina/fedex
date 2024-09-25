package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;

public class Complemento  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3336978508914728701L;
	private long indcIdInformacaoDoctoClien;
	private String valorComplemento;
	private String nomeComplemento;
	private String nrPedido;
	
	public final long getIndcIdInformacaoDoctoClien() {
		return indcIdInformacaoDoctoClien;
	}
	public final void setIndcIdInformacaoDoctoClien(long indcIdInformacaoDoctoClien) {
		this.indcIdInformacaoDoctoClien = indcIdInformacaoDoctoClien;
	}
	public final String getValorComplemento() {
		return valorComplemento;
	}
	public final void setValorComplemento(String valorComplemento) {
		this.valorComplemento = valorComplemento;
	}	
	public String getNomeComplemento() {
		return nomeComplemento;
	}
	public void setNomeComplemento(String nomeComplemento) {
		this.nomeComplemento = nomeComplemento;
	}	
	
	public String getNrPedido() {
		return nrPedido;
	}
	public void setNrPedido(String nrPedido) {
		this.nrPedido = nrPedido;
	}
	public static Complemento parseMap(HashMap<String, Object> mapa){
		Complemento complemento = new Complemento();
		
		if (mapa.get("INDC_ID_INFORMACAO_DOCTO_CLIEN") != null)
			complemento.setIndcIdInformacaoDoctoClien(Long.valueOf(mapa.get("INDC_ID_INFORMACAO_DOCTO_CLIEN").toString()));

		if (mapa.get("VALOR_COMPLEMENTO") != null)
			complemento.setValorComplemento(mapa.get("VALOR_COMPLEMENTO").toString());
		
		if (mapa.get("NOME_COMPLEMENTO") != null)
			complemento.setNomeComplemento(mapa.get("NOME_COMPLEMENTO").toString());

		return complemento; 
	}
}
