package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

public class GerenciadoraRisco implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idGerenciadoraRisco;
	private String dsEnderecoWeb;
	private DomainValue tpSituacao;
	private Pessoa pessoa;
	private List<SolicMonitPreventivo> solicMonitPreventivos;

	public Long getIdGerenciadoraRisco() {
		return idGerenciadoraRisco;
	}

	public void setIdGerenciadoraRisco(Long idGerenciadoraRisco) {
		this.idGerenciadoraRisco = idGerenciadoraRisco;
	}

	public String getDsEnderecoWeb() {
		return dsEnderecoWeb;
	}

	public void setDsEnderecoWeb(String dsEnderecoWeb) {
		this.dsEnderecoWeb = dsEnderecoWeb;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@ParametrizedAttribute(type = SolicMonitPreventivo.class)
	public List<SolicMonitPreventivo> getSolicMonitPreventivos() {
		return solicMonitPreventivos;
	}

	public void setSolicMonitPreventivos(List<SolicMonitPreventivo> solicMonitPreventivos) {
		this.solicMonitPreventivos = solicMonitPreventivos;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idGerenciadoraRisco)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof GerenciadoraRisco)) {
			return false;
		}
		GerenciadoraRisco cast = (GerenciadoraRisco) other;
		return new EqualsBuilder()
				.append(idGerenciadoraRisco, cast.idGerenciadoraRisco)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idGerenciadoraRisco)
				.toHashCode();
	}

}
