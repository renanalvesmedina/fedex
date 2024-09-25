package com.mercurio.lms.expedicao.model;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;

public class ParcelaServicoAdicional extends ParcelaServico {
	private static final long serialVersionUID = 1L;
	private ServAdicionalDocServ servAdicionalDocServ;

	public ParcelaServicoAdicional(ParcelaPreco parcelaPreco) {
		super(parcelaPreco);
	}

	public ServAdicionalDocServ getServAdicionalDocServ() {
		return servAdicionalDocServ;
	}

	public void setServAdicionalDocServ(
			ServAdicionalDocServ servAdicionalDocServ) {
		this.servAdicionalDocServ = servAdicionalDocServ;
	}

}
