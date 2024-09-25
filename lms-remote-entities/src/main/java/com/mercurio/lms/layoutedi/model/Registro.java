package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.List;


public class Registro  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1534594088399266139L;
	private Remetente remetente;
	private List<Detalhe> detalhes;
	
	public final Remetente getRemetente() {
		return remetente;
	}
	public final void setRemetente(Remetente remetente) {
		this.remetente = remetente;
	}
	public final List<Detalhe> getDetalhes() {
		return detalhes;
	}
	public final void setDetalhes(List<Detalhe> detalhes) {
		this.detalhes = detalhes;
	}
	
	
	
}
