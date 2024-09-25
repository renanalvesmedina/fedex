package com.mercurio.lms.rest.expedicao.dto;

public class OcorrenciaPendenciaComboDTO {

	private Long idOcorrenciaPendencia;
	private String dsOcorrenciaPendencia;
	private Short cdOcorrenciaPendencia;
	private String tpOcorrenciaPendencia;
	private String label;
	
	public OcorrenciaPendenciaComboDTO(Long idOcorrenciaPendencia,
			String dsOcorrenciaPendencia, Short cdOcorrenciaPendencia,
			String tpOcorrenciaPendencia) {
		super();
		this.idOcorrenciaPendencia = idOcorrenciaPendencia;
		this.dsOcorrenciaPendencia = dsOcorrenciaPendencia;
		this.cdOcorrenciaPendencia = cdOcorrenciaPendencia;
		this.tpOcorrenciaPendencia = tpOcorrenciaPendencia;
	}
	
	public Long getIdOcorrenciaPendencia() {
		return idOcorrenciaPendencia;
	}
	public void setIdOcorrenciaPendencia(Long idOcorrenciaPendencia) {
		this.idOcorrenciaPendencia = idOcorrenciaPendencia;
	}
	public String getDsOcorrenciaPendencia() {
		return dsOcorrenciaPendencia;
	}
	public void setDsOcorrenciaPendencia(String dsOcorrenciaPendencia) {
		this.dsOcorrenciaPendencia = dsOcorrenciaPendencia;
	}
	public Short getCdOcorrenciaPendencia() {
		return cdOcorrenciaPendencia;
	}
	public void setCdOcorrenciaPendencia(Short cdOcorrenciaPendencia) {
		this.cdOcorrenciaPendencia = cdOcorrenciaPendencia;
	}
	public String getTpOcorrenciaPendencia() {
		return tpOcorrenciaPendencia;
	}
	public void setTpOcorrenciaPendencia(String tpOcorrenciaPendencia) {
		this.tpOcorrenciaPendencia = tpOcorrenciaPendencia;
	}
	public String getLabel() {
		label = new StringBuffer(cdOcorrenciaPendencia.toString()).append(" - ").append(dsOcorrenciaPendencia).append(" - ").append(tpOcorrenciaPendencia).toString();
		return label;
	}
	
}
