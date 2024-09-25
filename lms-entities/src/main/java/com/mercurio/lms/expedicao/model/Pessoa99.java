package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Pessoa;

public class Pessoa99 implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idPessoa99;
    
	/** identifier field */
    private Pessoa pessoa;

    /** persistent field */
    private String nrIdentificacao99;

	/**
	 * @return the pessoa
	 */
	public Pessoa getPessoa() {
		return pessoa;
	}

	/**
	 * @param pessoa
	 *            the pessoa to set
	 */
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	/**
	 * @return the nrIdentificacao99
	 */
	public String getNrIdentificacao99() {
		return nrIdentificacao99;
	}

	/**
	 * @param nrIdentificacao99
	 *            the nrIdentificacao99 to set
	 */
	public void setNrIdentificacao99(String nrIdentificacao99) {
		this.nrIdentificacao99 = nrIdentificacao99;
	}

	public Long getIdPessoa99() {
		return idPessoa99;
	}

	public void setIdPessoa99(Long idPessoa99) {
		this.idPessoa99 = idPessoa99;
	}
}
