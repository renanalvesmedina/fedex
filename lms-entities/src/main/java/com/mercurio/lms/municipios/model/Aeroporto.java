package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

/** @author LMS Custom Hibernate CodeGenerator */
public class Aeroporto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idAeroporto;

	/** persistent field */
	private String sgAeroporto;

	/** persistent field */
	private String cdCidade;

	/** persistent field */
	private DomainValue tpSituacao;

	/** */
	private String municUltimoEndereco;

	/** nullable persistent field */
	private Pessoa pessoa;

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private Boolean blTaxaTerrestreObrigatoria;

	public Long getIdAeroporto() {
		return this.idAeroporto;
	}

	public void setIdAeroporto(Long idAeroporto) {
		this.idAeroporto = idAeroporto;
	}

	public String getSgAeroporto() {
		return this.sgAeroporto;
	}

	public void setSgAeroporto(String sgAeroporto) {
		this.sgAeroporto = sgAeroporto;
	}

	public String getCdCidade() {
		return this.cdCidade;
	}

	public void setCdCidade(String cdCidade) {
		this.cdCidade = cdCidade;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return this.filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public String getMunicUltimoEndereco() {
		return municUltimoEndereco;
	}

	public void setMunicUltimoEndereco(String municUltimoEndereco) {
		this.municUltimoEndereco = municUltimoEndereco;
	}

	public Boolean getBlTaxaTerrestreObrigatoria() {
		return blTaxaTerrestreObrigatoria;
	}

	public void setBlTaxaTerrestreObrigatoria(Boolean blTaxaTerrestreObrigatoria) {
		this.blTaxaTerrestreObrigatoria = blTaxaTerrestreObrigatoria;
	}

	public String getSiglaDescricao() {
		if (Hibernate.isInitialized(this.pessoa) && this.pessoa != null) {
	   		return this.sgAeroporto + " - " + this.getPessoa().getNmPessoa();
		}
		return this.sgAeroporto;
	}

	public String toString() {
		return new ToStringBuilder(this)
				.append("idAeroporto", getIdAeroporto()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Aeroporto))
			return false;
		Aeroporto castOther = (Aeroporto) other;
		return new EqualsBuilder().append(this.getIdAeroporto(),
				castOther.getIdAeroporto()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdAeroporto()).toHashCode();
	}
}
