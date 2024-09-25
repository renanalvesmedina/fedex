package com.mercurio.lms.sgr.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

public class Escolta implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idEscolta;
	private String dsEnderecoWeb;
	private DomainValue tpSituacao;
	private Pessoa pessoa;
	private List<EscoltaOperadoraMct> escoltaOperadoraMcts;
	private List<EscoltaReguladora> escoltaReguladoras;
	private List<ValorEscolta> valorEscoltas;

	public Long getIdEscolta() {
		return idEscolta;
	}

	public void setIdEscolta(Long idEscolta) {
		this.idEscolta = idEscolta;
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

	@ParametrizedAttribute(type = EscoltaOperadoraMct.class)
	public List<EscoltaOperadoraMct> getEscoltaOperadoraMcts() {
		return escoltaOperadoraMcts;
	}

	public void setEscoltaOperadoraMcts(List<EscoltaOperadoraMct> escoltaOperadoraMcts) {
		this.escoltaOperadoraMcts = escoltaOperadoraMcts;
	}

	@ParametrizedAttribute(type = EscoltaReguladora.class)
	public List<EscoltaReguladora> getEscoltaReguladoras() {
		return escoltaReguladoras;
	}

	public void setEscoltaReguladoras(List<EscoltaReguladora> escoltaReguladoras) {
		this.escoltaReguladoras = escoltaReguladoras;
	}

	@ParametrizedAttribute(type = ValorEscolta.class)
	public List<ValorEscolta> getValorEscoltas() {
		return valorEscoltas;
	}

	public void setValorEscoltas(List<ValorEscolta> valorEscoltas) {
		this.valorEscoltas = valorEscoltas;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append(idEscolta)
				.toString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || !(other instanceof Escolta)) {
			return false;
		}
		Escolta cast = (Escolta) other;
		return new EqualsBuilder()
				.append(idEscolta, cast.idEscolta)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder()
				.append(idEscolta)
				.toHashCode();
	}

}
