package com.mercurio.lms.pendencia.model;

import com.mercurio.lms.expedicao.model.CalculoServico;

public class CalculoMda extends CalculoServico<Mda> {

	private static final long serialVersionUID = 1L;

	@Override
	public Mda getDoctoServico() {
		return this.doctoServico;
	}

	@Override
	public void setDoctoServico(Mda doctoServico) {
		this.doctoServico = doctoServico;
	}

	@Override
	public String getTpDocumentoServico() {
		return this.doctoServico.getTpDocumentoServico().getValue();
	}

}
