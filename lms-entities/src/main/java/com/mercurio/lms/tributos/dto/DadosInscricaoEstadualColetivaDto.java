package com.mercurio.lms.tributos.dto;

import java.io.Serializable;
import java.util.Map;

public class DadosInscricaoEstadualColetivaDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String sgUnidadeFederativa;
	private String nrInscricaoEstadualColetiva;
	
	public String getSgUnidadeFederativa() {
		return sgUnidadeFederativa;
	}
	
	public void setSgUnidadeFederativa(String sgUnidadeFederativa) {
		this.sgUnidadeFederativa = sgUnidadeFederativa;
	}
	
	public String getNrInscricaoEstadualColetiva() {
		return nrInscricaoEstadualColetiva;
	}
	
	public void setNrInscricaoEstadualColetiva(String nrInscricaoEstadualColetiva) {
		this.nrInscricaoEstadualColetiva = nrInscricaoEstadualColetiva;
	}

	public DadosInscricaoEstadualColetivaDto buildByMap(Map<String, Object> data) {
		setNrInscricaoEstadualColetiva((String) data.get("nrInscricaoEstadualColetiva"));
		setSgUnidadeFederativa((String) data.get("sgUnidadeFederativa"));
		return this;
	}

}
