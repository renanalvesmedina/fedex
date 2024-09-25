package com.mercurio.lms.layoutNfse.model.retorno;

import java.util.ArrayList;
import java.util.List;

public class Dados {
	
	private List<Param> param = new ArrayList<Param>();
	private Mensagens mensagens;

	public Mensagens getMensagens() {
		return mensagens;
	}

	public void setMensagens(Mensagens mensagens) {
		this.mensagens = mensagens;
	}

	public List<Param> getParam() {
		return param;
	}

	public void addParam(Param param) {
		this.param.add(param);
	}

}
