package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Pendencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPendencia;

    /** persistent field */
    private Long idProcesso;

    /** persistent field */
    private DomainValue tpSituacaoPendencia;

    /** nullable persistent field */
    private String dsPendencia;

    /** persistent field */
    private com.mercurio.lms.workflow.model.Ocorrencia ocorrencia;

    /** persistent field */
    private List acoes;

    /** persistent field */    
    private List produtosCliente;

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ProdutoCliente.class)     
    public List getProdutosCliente() {
        return this.produtosCliente;
    }

    public void setProdutosCliente(List produtosCliente) {
        this.produtosCliente = produtosCliente;
    }
	
    public Long getIdPendencia() {
        return this.idPendencia;
    }

    public void setIdPendencia(Long idPendencia) {
        this.idPendencia = idPendencia;
    }

    public Long getIdProcesso() {
        return this.idProcesso;
    }

    public void setIdProcesso(Long idProcesso) {
        this.idProcesso = idProcesso;
    }

    public DomainValue getTpSituacaoPendencia() {
        return this.tpSituacaoPendencia;
    }

    public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
        this.tpSituacaoPendencia = tpSituacaoPendencia;
    }

    public String getDsPendencia() {
        return this.dsPendencia;
    }

    public void setDsPendencia(String dsPendencia) {
        this.dsPendencia = dsPendencia;
    }

    public com.mercurio.lms.workflow.model.Ocorrencia getOcorrencia() {
        return this.ocorrencia;
    }

	public void setOcorrencia(
			com.mercurio.lms.workflow.model.Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.Acao.class)     
    public List getAcoes() {
        return this.acoes;
    }

    public void setAcoes(List acoes) {
        this.acoes = acoes;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idPendencia", getIdPendencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Pendencia))
			return false;
        Pendencia castOther = (Pendencia) other;
		return new EqualsBuilder().append(this.getIdPendencia(),
				castOther.getIdPendencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPendencia()).toHashCode();
    }

}
