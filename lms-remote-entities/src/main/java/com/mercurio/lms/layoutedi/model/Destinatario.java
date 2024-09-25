package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.HashMap;

public class Destinatario  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3382406363040055498L;
	private String nomeDest;
	private Long cnpjDest;
	private String ieDest;
	private String enderecoDest;
	private String bairroDest;
	private String municipioDest;
	private String ufDest;
	private Integer cepEnderDest;
	private Integer cepMunicDest;
	
	public final String getNomeDest() {
		return nomeDest;
	}
	public final void setNomeDest(String nomeDest) {
		this.nomeDest = nomeDest;
	}
	public final Long getCnpjDest() {
		return cnpjDest;
	}
	public final void setCnpjDest(Long cnpjDest) {
		this.cnpjDest = cnpjDest;
	}
	public final String getIeDest() {
		return ieDest;
	}
	public final void setIeDest(String ieDest) {
		this.ieDest = ieDest;
	}
	public final String getEnderecoDest() {
		return enderecoDest;
	}
	public final void setEnderecoDest(String enderecoDest) {
		this.enderecoDest = enderecoDest;
	}
	public final String getBairroDest() {
		return bairroDest;
	}
	public final void setBairroDest(String bairroDest) {
		this.bairroDest = bairroDest;
	}
	public final String getMunicipioDest() {
		return municipioDest;
	}
	public final void setMunicipioDest(String municipioDest) {
		this.municipioDest = municipioDest;
	}
	public final String getUfDest() {
		return ufDest;
	}
	public final void setUfDest(String ufDest) {
		this.ufDest = ufDest;
	}
	public final Integer getCepEnderDest() {
		return cepEnderDest;
	}
	public final void setCepEnderDest(Integer cepEnderDest) {
		this.cepEnderDest = cepEnderDest;
	}
	public final Integer getCepMunicDest() {
		return cepMunicDest;
	}
	public final void setCepMunicDest(Integer cepMunicDest) {
		this.cepMunicDest = cepMunicDest;
	}
	
	public static Destinatario parseMap(HashMap<String, Object> mapa){
		Destinatario dest = new Destinatario();
		
		if (mapa.get("NOME_DEST") != null)
			dest.setNomeDest(mapa.get("NOME_DEST").toString());

		if (mapa.get("CNPJ_DEST") != null)
			dest.setCnpjDest(Long.valueOf(mapa.get("CNPJ_DEST").toString()));
		
		if (mapa.get("IE_DEST") != null)
			dest.setIeDest(mapa.get("IE_DEST").toString());

		if (mapa.get("CEP_ENDER_DEST") != null)
			dest.setCepEnderDest(Integer.valueOf(mapa.get("CEP_ENDER_DEST").toString()));
		
		if (mapa.get("CEP_MUNIC_DEST") != null)
			dest.setCepMunicDest(Integer.valueOf(mapa.get("CEP_MUNIC_DEST").toString()));
		
		if (mapa.get("ENDERECO_DEST") != null)
			dest.setEnderecoDest(mapa.get("ENDERECO_DEST").toString());
		
		if (mapa.get("BAIRRO_DEST") != null)
			dest.setBairroDest(mapa.get("BAIRRO_DEST").toString());
		
		if (mapa.get("MUNICIPIO_DEST") != null)
			dest.setMunicipioDest(mapa.get("MUNICIPIO_DEST").toString());

		if (mapa.get("UF_DEST") != null)
			dest.setUfDest(mapa.get("UF_DEST").toString());
		
		return dest;
	}
}
