package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;

public class Tomador  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5518210884863947169L;
	private String nomeTomador;
	private Long cnpjTomador;
	private String ieTomador;
	private String enderecoTomador;
	private String bairroTomador;
	private String municipioTomador;
	private String ufTomador;
	private Integer cepEnderTomador;
	private Integer cepMunicTomador;
	
	public final String getNomeTomador() {
		return nomeTomador;
	}
	public final void setNomeTomador(String nomeTomador) {
		this.nomeTomador = nomeTomador;
	}
	public final Long getCnpjTomador() {
		return cnpjTomador;
	}
	public final void setCnpjTomador(Long cnpjTomador) {
		this.cnpjTomador = cnpjTomador;
	}
	public final String getIeTomador() {
		return ieTomador;
	}
	public final void setIeTomador(String ieTomador) {
		this.ieTomador = ieTomador;
	}
	public final String getEnderecoTomador() {
		return enderecoTomador;
	}
	public final void setEnderecoTomador(String enderecoTomador) {
		this.enderecoTomador = enderecoTomador;
	}
	public final String getBairroTomador() {
		return bairroTomador;
	}
	public final void setBairroTomador(String bairroTomador) {
		this.bairroTomador = bairroTomador;
	}
	public final String getMunicipioTomador() {
		return municipioTomador;
	}
	public final void setMunicipioTomador(String municipioTomador) {
		this.municipioTomador = municipioTomador;
	}
	public final String getUfTomador() {
		return ufTomador;
	}
	public final void setUfTomador(String ufTomador) {
		this.ufTomador = ufTomador;
	}
	public final Integer getCepEnderTomador() {
		return cepEnderTomador;
	}
	public final void setCepEnderTomador(Integer cepEnderTomador) {
		this.cepEnderTomador = cepEnderTomador;
	}
	public final Integer getCepMunicTomador() {
		return cepMunicTomador;
	}
	public final void setCepMunicTomador(Integer cepMunicTomador) {
		this.cepMunicTomador = cepMunicTomador;
	}
	
	public static Tomador parseMap(HashMap<String, Object> mapa){
		Tomador tomador = new Tomador();
		
		if (mapa.get("NOME_TOMADOR") != null)
			tomador.setNomeTomador((String)mapa.get("NOME_TOMADOR"));

		if (mapa.get("CNPJ_TOMADOR") != null)
			tomador.setCnpjTomador(Long.valueOf(mapa.get("CNPJ_TOMADOR").toString()));
		
		if (mapa.get("IE_TOMADOR") != null)
			tomador.setIeTomador((String)mapa.get("IE_TOMADOR"));

		if (mapa.get("CEP_ENDER_TOMADOR") != null)
			tomador.setCepEnderTomador(Integer.valueOf(mapa.get("CEP_ENDER_TOMADOR").toString()));
		
		if (mapa.get("CEP_MUNIC_TOMADOR") != null)
			tomador.setCepMunicTomador(Integer.valueOf(mapa.get("CEP_MUNIC_TOMADOR").toString()));
		
		if (mapa.get("ENDERECO_TOMADOR") != null)
			tomador.setEnderecoTomador((String)mapa.get("ENDERECO_TOMADOR"));
		
		if (mapa.get("BAIRRO_TOMADOR") != null)
			tomador.setBairroTomador((String)mapa.get("BAIRRO_TOMADOR"));
		
		if (mapa.get("MUNICIPIO_TOMADOR") != null)
			tomador.setMunicipioTomador((String)mapa.get("MUNICIPIO_TOMADOR"));

		if (mapa.get("UF_TOMADOR") != null)
			tomador.setUfTomador((String)mapa.get("UF_TOMADOR"));
		
		return tomador;
	}
}
