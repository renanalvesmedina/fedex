package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

/**
 * Pojo com as dimensões da mercadoria
 * @author lucianos
 *
 */
public class DimensaoWebService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer nrAltura;
	private Integer nrLargura;
	private Integer nrComprimento;
	private Integer nrQuantidade;
	
	public Integer getNrAltura() {
		return nrAltura;
	}
	public void setNrAltura(Integer nrAltura) {
		this.nrAltura = nrAltura;
	}
	public Integer getNrComprimento() {
		return nrComprimento;
	}
	public void setNrComprimento(Integer nrComprimento) {
		this.nrComprimento = nrComprimento;
	}
	public Integer getNrLargura() {
		return nrLargura;
	}
	public void setNrLargura(Integer nrLargura) {
		this.nrLargura = nrLargura;
	}
	public Integer getNrQuantidade() {
		return nrQuantidade;
	}
	public void setNrQuantidade(Integer nrQuantidade) {
		this.nrQuantidade = nrQuantidade;
	}
	
}
