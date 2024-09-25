package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EnderecoEstoque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEnderecoEstoque;

    /** persistent field */
    private Short nrRua;

    /** persistent field */
    private Short nrAndar;

    /** persistent field */
    private Short nrApartamento;

    /** persistent field */
    private DomainValue tpSituacaoEndereco;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.Modulo modulo;

    /** persistent field */
    private List mdas;

    /** persistent field */
    private List mercadoriaPendenciaMzs;

    public Long getIdEnderecoEstoque() {
        return this.idEnderecoEstoque;
    }

    public void setIdEnderecoEstoque(Long idEnderecoEstoque) {
        this.idEnderecoEstoque = idEnderecoEstoque;
    }

    public Short getNrRua() {
        return this.nrRua;
    }

    public void setNrRua(Short nrRua) {
        this.nrRua = nrRua;
    }

    public Short getNrAndar() {
        return this.nrAndar;
    }

    public void setNrAndar(Short nrAndar) {
        this.nrAndar = nrAndar;
    }

    public Short getNrApartamento() {
        return this.nrApartamento;
    }

    public void setNrApartamento(Short nrApartamento) {
        this.nrApartamento = nrApartamento;
    }

    public DomainValue getTpSituacaoEndereco() {
        return this.tpSituacaoEndereco;
    }

    public void setTpSituacaoEndereco(DomainValue tpSituacaoEndereco) {
        this.tpSituacaoEndereco = tpSituacaoEndereco;
    }

    public com.mercurio.lms.pendencia.model.Modulo getModulo() {
        return this.modulo;
    }

    public void setModulo(com.mercurio.lms.pendencia.model.Modulo modulo) {
        this.modulo = modulo;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.Mda.class)     
    public List getMdas() {
        return this.mdas;
    }

    public void setMdas(List mdas) {
        this.mdas = mdas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)     
    public List getMercadoriaPendenciaMzs() {
        return this.mercadoriaPendenciaMzs;
    }

    public void setMercadoriaPendenciaMzs(List mercadoriaPendenciaMzs) {
        this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEnderecoEstoque",
				getIdEnderecoEstoque()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EnderecoEstoque))
			return false;
        EnderecoEstoque castOther = (EnderecoEstoque) other;
		return new EqualsBuilder().append(this.getIdEnderecoEstoque(),
				castOther.getIdEnderecoEstoque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEnderecoEstoque())
            .toHashCode();
    }

}
