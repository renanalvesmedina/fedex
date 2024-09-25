package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EmpresaCobranca implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEmpresaCobranca;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List redecos;

    /** persistent field */
    private List historicoCheques;
    
    private DomainValue tpSituacao;

    public Long getIdEmpresaCobranca() {
        return this.idEmpresaCobranca;
    }

    public void setIdEmpresaCobranca(Long idEmpresaCobranca) {
        this.idEmpresaCobranca = idEmpresaCobranca;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Redeco.class)     
    public List getRedecos() {
        return this.redecos;
    }

    public void setRedecos(List redecos) {
        this.redecos = redecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoCheque.class)     
    public List getHistoricoCheques() {
        return this.historicoCheques;
    }

    public void setHistoricoCheques(List historicoCheques) {
        this.historicoCheques = historicoCheques;
    }

    public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEmpresaCobranca",
				getIdEmpresaCobranca()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EmpresaCobranca))
			return false;
        EmpresaCobranca castOther = (EmpresaCobranca) other;
		return new EqualsBuilder().append(this.getIdEmpresaCobranca(),
				castOther.getIdEmpresaCobranca()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEmpresaCobranca())
            .toHashCode();
    }

}
