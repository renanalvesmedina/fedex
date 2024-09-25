package com.mercurio.lms.vendas.dto;

import java.util.Arrays;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;

public class ImportarTRTDTO {

	private String cnpj;
	private String municipio;
	private String uf;
	private String cobraTRT;

	public ImportarTRTDTO() {
	}
	
	public ImportarTRTDTO(String line){
		List<String> campos = Arrays.asList(line.split(";"));

		if(campos.size() != 4){
			throw new BusinessException("LMS-00081", new Object[]{"Número de campos inválidos na linha do TRT: " + line});
		}

		cnpj = campos.get(0).trim();
		municipio = campos.get(1).trim();
		uf = campos.get(2).trim();
		cobraTRT = campos.get(3).trim();
		
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public void setCobraTRT(String cobraTRT) {
		this.cobraTRT = cobraTRT;
	}
	
	public String getCobraTRT() {
		return cobraTRT;
	}

	@Override
	public String toString() {
		return "ImportarTRTDTO [cnpj=" + cnpj + ", municipio=" + municipio
				+ ", uf=" + uf + ", cobraTRT=" + cobraTRT + "]";
	}

	
}
