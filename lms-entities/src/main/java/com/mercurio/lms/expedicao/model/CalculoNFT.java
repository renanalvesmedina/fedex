package com.mercurio.lms.expedicao.model;

import java.util.List;

import com.mercurio.lms.tributos.model.ImpostoCalculado;

public class CalculoNFT extends CalculoFrete {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Método criado para melhorar a rastreabilidade, pois era feito instanceOf CalculoNFT
	 * para saber se o objeto representava um cálculo de nota de transporte ou não 
	 */
	@Override
	public boolean isCalculoNotaTransporte() {
		return true;
	}
	
	private List<ImpostoServico> tributos;
	private List<ImpostoCalculado> impostosCalculados;

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
