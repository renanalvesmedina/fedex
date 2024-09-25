package com.mercurio.lms.layoutNfse.model.rps;

import java.util.ArrayList;
import java.util.List;

public class DadosAdic {

	private List<CampoAdicional> CamposAdicionais = new ArrayList<CampoAdicional>();
	private EmailDadosAdicionais Email;	

	public List<CampoAdicional> getCamposAdicionais() {
		return CamposAdicionais;
	}

	public void addCamposAdicionais(CampoAdicional camposAdicionais) {
		this.CamposAdicionais.add(camposAdicionais);
	}

	public EmailDadosAdicionais getEmail() {
		return Email;
	}

	public void setEmail(EmailDadosAdicionais email) {
		Email = email;
	}
	
}
