package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;


public class Remetente  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5285256724032984648L;
	private String nomeReme;
	private Long cnpjReme;
	private String ieReme;
	private String enderecoReme;
	private String bairroReme;
	private String municipioReme;
	private String ufReme;
	private Integer cepEnderReme;
	private Integer cepMuniReme;
	
	public String getNomeReme() {
		return nomeReme;
	}
	public void setNomeReme(String nomeReme) {
		this.nomeReme = nomeReme;
	}
	public Long getCnpjReme() {
		return cnpjReme;
	}
	public void setCnpjReme(Long cnpjReme) {
		this.cnpjReme = cnpjReme;
	}
	public String getIeReme() {
		return ieReme;
	}
	public void setIeReme(String ieReme) {
		this.ieReme = ieReme;
	}
	public String getEnderecoReme() {
		return enderecoReme;
	}
	public void setEnderecoReme(String enderecoReme) {
		this.enderecoReme = enderecoReme;
	}
	public String getBairroReme() {
		return bairroReme;
	}
	public void setBairroReme(String bairroReme) {
		this.bairroReme = bairroReme;
	}
	public String getMunicipioReme() {
		return municipioReme;
	}
	public void setMunicipioReme(String municipioReme) {
		this.municipioReme = municipioReme;
	}
	public String getUfReme() {
		return ufReme;
	}
	public void setUfReme(String ufReme) {
		this.ufReme = ufReme;
	}
	public Integer getCepEnderReme() {
		return cepEnderReme;
	}
	public void setCepEnderReme(Integer cepEnderReme) {
		this.cepEnderReme = cepEnderReme;
	}
	public Integer getCepMuniReme() {
		return cepMuniReme;
	}
	public void setCepMuniReme(Integer cepMuniReme) {
		this.cepMuniReme = cepMuniReme;
	}
	

	public static Remetente parseMap(HashMap<String,Object> mapa){
		Remetente rem = new Remetente();
		
		if (mapa.get("NOME_REME") != null)
			rem.setNomeReme((String)mapa.get("NOME_REME"));

		if (mapa.get("CNPJ_REME") != null)
			rem.setCnpjReme(Long.valueOf(String.valueOf(mapa.get("CNPJ_REME"))));
		
		if (mapa.get("IE_REME") != null)
			rem.setIeReme((String)mapa.get("IE_REME"));
		
		if (mapa.get("CEP_ENDER_REME") != null)
			rem.setCepEnderReme(Integer.valueOf(mapa.get("CEP_ENDER_REME").toString()));
		
		if (mapa.get("CEP_MUNI_REME") != null)
			rem.setCepMuniReme(Integer.valueOf(mapa.get("CEP_MUNI_REME").toString()));
		
		if (mapa.get("ENDERECO_REME") != null)
			rem.setEnderecoReme((String)mapa.get("ENDERECO_REME"));
		
		if (mapa.get("BAIRRO_REME") != null)
			rem.setBairroReme((String)mapa.get("BAIRRO_REME"));
		
		if (mapa.get("MUNICIPIO_REME") != null)
			rem.setMunicipioReme((String)mapa.get("MUNICIPIO_REME"));
		
		if (mapa.get("UF_REME") != null)
			rem.setUfReme((String)mapa.get("UF_REME"));

		return rem;
		
	}
}
