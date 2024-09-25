package com.mercurio.lms.edi.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.vendas.model.Cliente;

public class LogAtualizacaoEDI implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long idLogAtualizacaoEdi;
	
	private Integer nrNotaFiscal;
	
	private Long nrProcessamento;
	
	private String dsMensagemErro;
	
	private Cliente clienteRemetente;

	public Long getIdLogAtualizacaoEdi() {
		return idLogAtualizacaoEdi;
	}

	public void setIdLogAtualizacaoEdi(Long idLogAtualizacaoEdi) {
		this.idLogAtualizacaoEdi = idLogAtualizacaoEdi;
	}

	public Integer getNrNotaFiscal() {
		return nrNotaFiscal;
	}

	public void setNrNotaFiscal(Integer nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}

	public Long getNrProcessamento() {
		return nrProcessamento;
	}

	public void setNrProcessamento(Long nrProcessamento) {
		this.nrProcessamento = nrProcessamento;
	}

	public String getDsMensagemErro() {
		return dsMensagemErro;
	}

	public void setDsMensagemErro(String dsMensagemErro) {
		this.dsMensagemErro = dsMensagemErro;
	}

	public Cliente getClienteRemetente() {
		return clienteRemetente;
	}

	public void setClienteRemetente(Cliente clienteRemetente) {
		this.clienteRemetente = clienteRemetente;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idLogAtualizacaoEdi",
				getIdLogAtualizacaoEdi()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LogAtualizacaoEDI))
			return false;
		LogAtualizacaoEDI castOther = (LogAtualizacaoEDI) other;
		return new EqualsBuilder().append(this.getIdLogAtualizacaoEdi(),
				castOther.getIdLogAtualizacaoEdi()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdLogAtualizacaoEdi())
			.toHashCode();
	}
}