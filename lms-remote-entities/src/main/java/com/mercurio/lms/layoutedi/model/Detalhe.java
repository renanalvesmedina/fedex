package com.mercurio.lms.layoutedi.model;

import java.io.Serializable;
import java.util.List;


public class Detalhe  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5569339838304694409L;
	private Destinatario destinatario;
	private List<NotaFiscal> notasFiscais;
	private Consignatario consignatario;
	private Redespacho redespacho;
	private Tomador tomador;

	public final Destinatario getDestinatario() {
		return destinatario;
	}
	public final void setDestinatario(Destinatario destinatario) {
		this.destinatario = destinatario;
	}
	public final List<NotaFiscal> getNotasFiscais() {
		return notasFiscais;
	}
	public final void setNotasFiscais(List<NotaFiscal> notasFiscais) {
		this.notasFiscais = notasFiscais;
	}
	public final Consignatario getConsignatario() {
		return consignatario;
	}
	public final void setConsignatario(Consignatario consignatario) {
		this.consignatario = consignatario;
	}
	public final Redespacho getRedespacho() {
		return redespacho;
	}
	public final void setRedespacho(Redespacho redespacho) {
		this.redespacho = redespacho;
	}
	public final Tomador getTomador() {
		return tomador;
	}
	public final void setTomador(Tomador tomador) {
		this.tomador = tomador;
	}
	
}
