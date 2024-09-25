package com.mercurio.lms.layoutNfse.model.rps;

import java.util.ArrayList;
import java.util.List;

public class ListaDeducoes {
	private List<Deducao> deducao = new ArrayList<Deducao>();

	public List<Deducao> getDeducao() {
		return deducao;
	}

	public void addDeducao(Deducao deducao) {
		this.deducao.add(deducao);
	}
}
