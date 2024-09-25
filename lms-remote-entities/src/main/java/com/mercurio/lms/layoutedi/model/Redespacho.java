package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;

public class Redespacho  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8336909127482139618L;
	private String nomeRedesp;
	private Long cnpjRedesp;
	private String ieRedesp;
	private String enderecoRedesp;
	private String bairroRedesp;
	private String municipioRedesp;
	private String ufRedesp;
	private Integer cepEnderRedesp;
	private Integer cepMunicRedesp;
	public final String getNomeRedesp() {
		return nomeRedesp;
	}
	public final void setNomeRedesp(String nomeRedesp) {
		this.nomeRedesp = nomeRedesp;
	}
	public final Long getCnpjRedesp() {
		return cnpjRedesp;
	}
	public final void setCnpjRedesp(Long cnpjRedesp) {
		this.cnpjRedesp = cnpjRedesp;
	}
	public final String getIeRedesp() {
		return ieRedesp;
	}
	public final void setIeRedesp(String ieRedesp) {
		this.ieRedesp = ieRedesp;
	}
	public final String getEnderecoRedesp() {
		return enderecoRedesp;
	}
	public final void setEnderecoRedesp(String enderecoRedesp) {
		this.enderecoRedesp = enderecoRedesp;
	}
	public final String getBairroRedesp() {
		return bairroRedesp;
	}
	public final void setBairroRedesp(String bairroRedesp) {
		this.bairroRedesp = bairroRedesp;
	}
	public final String getMunicipioRedesp() {
		return municipioRedesp;
	}
	public final void setMunicipioRedesp(String municipioRedesp) {
		this.municipioRedesp = municipioRedesp;
	}
	public final String getUfRedesp() {
		return ufRedesp;
	}
	public final void setUfRedesp(String ufRedesp) {
		this.ufRedesp = ufRedesp;
	}
	public final Integer getCepEnderRedesp() {
		return cepEnderRedesp;
	}
	public final void setCepEnderRedesp(Integer cepEnderRedesp) {
		this.cepEnderRedesp = cepEnderRedesp;
	}
	public final Integer getCepMunicRedesp() {
		return cepMunicRedesp;
	}
	public final void setCepMunicRedesp(Integer cepMunicRedesp) {
		this.cepMunicRedesp = cepMunicRedesp;
	}

	public static Redespacho parseMap(HashMap<String, Object> mapa){
		Redespacho redesp = new Redespacho();
		
		if (mapa.get("NOME_REDESP") != null)
			redesp.setNomeRedesp(mapa.get("NOME_REDESP").toString());

		if (mapa.get("CNPJ_REDESP") != null)
			redesp.setCnpjRedesp(Long.valueOf(mapa.get("CNPJ_REDESP").toString()));
		
		if (mapa.get("IE_REDESP") != null)
			redesp.setIeRedesp(mapa.get("IE_REDESP").toString());

		if (mapa.get("CEP_ENDER_REDESP") != null)
			redesp.setCepEnderRedesp(Integer.valueOf(mapa.get("CEP_ENDER_REDESP").toString()));
		
		if (mapa.get("CEP_MUNIC_REDESP") != null)
			redesp.setCepMunicRedesp(Integer.valueOf(mapa.get("CEP_MUNIC_REDESP").toString()));
		
		if (mapa.get("ENDERECO_REDESP") != null)
			redesp.setEnderecoRedesp(mapa.get("ENDERECO_REDESP").toString());
		
		if (mapa.get("BAIRRO_REDESP") != null)
			redesp.setBairroRedesp(mapa.get("BAIRRO_REDESP").toString());
		
		if (mapa.get("MUNICIPIO_REDESP") != null)
			redesp.setMunicipioRedesp(mapa.get("MUNICIPIO_REDESP").toString());

		if (mapa.get("UF_REDESP") != null)
			redesp.setUfRedesp(mapa.get("UF_REDESP").toString());
		
		return redesp;
	}
	
}
