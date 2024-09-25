package com.mercurio.lms.pendencia.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Modulo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idModulo;

    /** persistent field */
    private Short nrModulo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Terminal terminal;

    /** persistent field */
    private List enderecoEstoques;

    /** persistent field */
    private List enderecoArmazems;
    
    /** persistent field */
    private List boxs;

    public Long getIdModulo() {
        return this.idModulo;
    }

    public void setIdModulo(Long idModulo) {
        this.idModulo = idModulo;
    }

    public Short getNrModulo() {
        return this.nrModulo;
    }

    public void setNrModulo(Short nrModulo) {
        this.nrModulo = nrModulo;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.portaria.model.Terminal getTerminal() {
        return this.terminal;
    }

    public void setTerminal(com.mercurio.lms.portaria.model.Terminal terminal) {
        this.terminal = terminal;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EnderecoEstoque.class)     
    public List getEnderecoEstoques() {
        return this.enderecoEstoques;
    }

    public void setEnderecoEstoques(List enderecoEstoques) {
        this.enderecoEstoques = enderecoEstoques;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EnderecoArmazem.class)     
    public List getEnderecoArmazems() {
        return this.enderecoArmazems;
    }

    public void setEnderecoArmazems(List enderecoArmazems) {
        this.enderecoArmazems = enderecoArmazems;
    }    
    
    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.Box.class)     
    public List getBoxs() {
        return this.boxs;
    }

    public void setBoxs(List boxs) {
        this.boxs = boxs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idModulo", getIdModulo())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Modulo))
			return false;
        Modulo castOther = (Modulo) other;
		return new EqualsBuilder().append(this.getIdModulo(),
				castOther.getIdModulo()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdModulo()).toHashCode();
    }

}
