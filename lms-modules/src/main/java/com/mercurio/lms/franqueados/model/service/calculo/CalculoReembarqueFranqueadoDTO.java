package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;

public class CalculoReembarqueFranqueadoDTO extends DocumentoFranqueadoDTO {

	private static final long serialVersionUID = 1L;

	private Long idManifesto;
	private BigDecimal psReal;

	public Long getIdManifesto() {
		return idManifesto;
	}

	public void setIdManifesto(Long idManifesto) {
		this.idManifesto = idManifesto;
	}

	public BigDecimal getPsReal() {
		return psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	@Override
	public CalculoReembarqueFranqueados createCalculo() {
		return new CalculoReembarqueFranqueados();
	}
}
