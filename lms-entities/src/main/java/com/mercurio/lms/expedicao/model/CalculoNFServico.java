package com.mercurio.lms.expedicao.model;

import java.util.List;

import com.mercurio.lms.tributos.model.ImpostoCalculado;

public class CalculoNFServico extends CalculoServico<NotaFiscalServico> {
	private static final long serialVersionUID = 1L;
	
	private List<ImpostoServico> tributos;
	private List<ImpostoCalculado> impostosCalculados;

	@Override
	public NotaFiscalServico getDoctoServico() {
		return this.doctoServico;
	}

	@Override
	public void setDoctoServico(NotaFiscalServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	@Override
	public String getTpDocumentoServico() {
		return this.doctoServico.getTpDocumentoServico().getValue();
	}

	public List<ImpostoServico> getTributos() {
		return tributos;
	}

	public void setTributos(List<ImpostoServico> tributos) {
		this.tributos = tributos;
	}

	public List<ImpostoCalculado> getImpostosCalculados() {
		return impostosCalculados;
}

	public void setImpostosCalculados(List<ImpostoCalculado> impostosCalculados) {
		this.impostosCalculados = impostosCalculados;
	}

}
