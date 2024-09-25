package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;

public class Consignatario  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4876373774383539634L;
	private String nomeConsig;
	private Long cnpjConsig;
	private String ieConsig;
	private String enderecoConsig;
	private String bairroConsig;
	private String municipioConsig;
	private String ufConsig;
	private Integer cepEnderConsig;
	private Integer cepMunicConsig;
	
	public final String getNomeConsig() {
		return nomeConsig;
	}
	public final void setNomeConsig(String nomeConsig) {
		this.nomeConsig = nomeConsig;
	}
	public final Long getCnpjConsig() {
		return cnpjConsig;
	}
	public final void setCnpjConsig(Long cnpjConsig) {
		this.cnpjConsig = cnpjConsig;
	}
	public final String getIeConsig() {
		return ieConsig;
	}
	public final void setIeConsig(String ieConsig) {
		this.ieConsig = ieConsig;
	}
	public final String getEnderecoConsig() {
		return enderecoConsig;
	}
	public final void setEnderecoConsig(String enderecoConsig) {
		this.enderecoConsig = enderecoConsig;
	}
	public final String getBairroConsig() {
		return bairroConsig;
	}
	public final void setBairroConsig(String bairroConsig) {
		this.bairroConsig = bairroConsig;
	}
	public final String getMunicipioConsig() {
		return municipioConsig;
	}
	public final void setMunicipioConsig(String municipioConsig) {
		this.municipioConsig = municipioConsig;
	}
	public final String getUfConsig() {
		return ufConsig;
	}
	public final void setUfConsig(String ufConsig) {
		this.ufConsig = ufConsig;
	}
	public final Integer getCepEnderConsig() {
		return cepEnderConsig;
	}
	public final void setCepEnderConsig(Integer cepEnderConsig) {
		this.cepEnderConsig = cepEnderConsig;
	}
	public final Integer getCepMunicConsig() {
		return cepMunicConsig;
	}
	public final void setCepMunicConsig(Integer cepMunicConsig) {
		this.cepMunicConsig = cepMunicConsig;
	}
	
	public static Consignatario parseMap(HashMap<String, Object> mapa){
		Consignatario consig = new Consignatario();
		
		if (mapa.get("NOME_CONSIG") != null)
			consig.setNomeConsig(mapa.get("NOME_CONSIG").toString());

		if (mapa.get("CNPJ_CONSIG") != null)
			consig.setCnpjConsig(Long.valueOf(mapa.get("CNPJ_CONSIG").toString()));
		
		if (mapa.get("IE_CONSIG") != null)
			consig.setIeConsig(mapa.get("IE_CONSIG").toString());

		if (mapa.get("CEP_ENDER_CONSIG") != null)
			consig.setCepEnderConsig((Long.valueOf(mapa.get("CEP_ENDER_CONSIG").toString())).intValue());
		
		if (mapa.get("CEP_MUNIC_CONSIG") != null)
			consig.setCepMunicConsig((Long.valueOf(mapa.get("CEP_MUNIC_CONSIG").toString())).intValue());
		
		if (mapa.get("ENDERECO_CONSIG") != null)
			consig.setEnderecoConsig(mapa.get("ENDERECO_CONSIG").toString());
		
		if (mapa.get("BAIRRO_CONSIG") != null)
			consig.setBairroConsig(mapa.get("BAIRRO_CONSIG").toString());
		
		if (mapa.get("MUNICIPIO_CONSIG") != null)
			consig.setMunicipioConsig(mapa.get("MUNICIPIO_CONSIG").toString());

		if (mapa.get("UF_CONSIG") != null)
			consig.setUfConsig(mapa.get("UF_CONSIG").toString());
		
		return consig;
	}
}
