package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/**
 * LMS-6172 - Entidade para documentos anexos na tela
 * "Gerar Proposta do Cliente".
 * 
 * @author fabianop
 */
public class SimulacaoAnexo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idSimulacaoAnexo;
	private Simulacao simulacao;
	private DateTime dhInclusao;
	private byte[] dsDocumento;
	private String dsAnexo;

	public Long getIdSimulacaoAnexo() {
		return idSimulacaoAnexo;
	}

	public void setIdSimulacaoAnexo(Long idSimulacaoAnexo) {
		this.idSimulacaoAnexo = idSimulacaoAnexo;
	}

	public Simulacao getSimulacao() {
		return simulacao;
	}

	public void setSimulacao(Simulacao simulacao) {
		this.simulacao = simulacao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public byte[] getDsDocumento() {
		return dsDocumento;
	}

	public void setDsDocumento(byte[] dsDocumento) {
		this.dsDocumento = dsDocumento;
	}

	public String getDsAnexo() {
		return dsAnexo;
	}

	public void setDsAnexo(String dsAnexo) {
		this.dsAnexo = dsAnexo;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdSimulacaoAnexo()).toHashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		SimulacaoAnexo other = (SimulacaoAnexo) object;
		return new EqualsBuilder().append(this.getIdSimulacaoAnexo(), other.getIdSimulacaoAnexo()).isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idSimulacaoAnexo", this.getIdSimulacaoAnexo()).toString();
	}

}
