package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;

/** @author LMS Custom Hibernate CodeGenerator */
public class Despachante implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDespachante;

    private DomainValue tpSituacao;
    
    /** nullable persistent field */
    private Pessoa pessoa;

    /** persistent field */
    private List despachanteCtoInts;

    /** persistent field */
    private List clienteDespachantes;

    public Long getIdDespachante() {
        return this.idDespachante;
    }

    public void setIdDespachante(Long idDespachante) {
        this.idDespachante = idDespachante;
    }

    public Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DespachanteCtoInt.class)     
    public List getDespachanteCtoInts() {
        return this.despachanteCtoInts;
    }

    public void setDespachanteCtoInts(List despachanteCtoInts) {
        this.despachanteCtoInts = despachanteCtoInts;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ClienteDespachante.class)     
    public List getClienteDespachantes() {
        return this.clienteDespachantes;
    }

    public void setClienteDespachantes(List clienteDespachantes) {
        this.clienteDespachantes = clienteDespachantes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDespachante",
				getIdDespachante()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Despachante))
			return false;
        Despachante castOther = (Despachante) other;
		return new EqualsBuilder().append(this.getIdDespachante(),
				castOther.getIdDespachante()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDespachante()).toHashCode();
    }

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

}
