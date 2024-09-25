package com.mercurio.lms.franqueados.model.service.calculo;

public abstract class DocumentoFranqueadoDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long idDoctoServico;

	public Long getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}
	
	public abstract ICalculoFranquiado createCalculo();

}
