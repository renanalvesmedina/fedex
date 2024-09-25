package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class OperadoraMct implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOperadoraMct;
    
    
    /** persistent field */
    private Integer cdFabricante;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String dsHomepage;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.Pessoa pessoa;

    /** persistent field */
    private List escoltaOperadoraMcts;

    /** persistent field */
    private List meioTransporteRodoviarios;

    /** persistent field */
    private List solicitacaoSinais;

    public Long getIdOperadoraMct() {
        return this.idOperadoraMct;
    }

    public void setIdOperadoraMct(Long idOperadoraMct) {
        this.idOperadoraMct = idOperadoraMct;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getDsHomepage() {
        return this.dsHomepage;
    }

    public void setDsHomepage(String dsHomepage) {
        this.dsHomepage = dsHomepage;
    }

    public com.mercurio.lms.configuracoes.model.Pessoa getPessoa() {
        return this.pessoa;
    }

    public void setPessoa(com.mercurio.lms.configuracoes.model.Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.EscoltaOperadoraMct.class)     
    public List getEscoltaOperadoraMcts() {
        return this.escoltaOperadoraMcts;
    }

    public void setEscoltaOperadoraMcts(List escoltaOperadoraMcts) {
        this.escoltaOperadoraMcts = escoltaOperadoraMcts;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario.class)     
    public List getMeioTransporteRodoviarios() {
        return this.meioTransporteRodoviarios;
    }

    public void setMeioTransporteRodoviarios(List meioTransporteRodoviarios) {
        this.meioTransporteRodoviarios = meioTransporteRodoviarios;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicitacaoSinal.class)     
    public List getSolicitacaoSinais() {
        return this.solicitacaoSinais;
    }

    public void setSolicitacaoSinais(List solicitacaoSinais) {
        this.solicitacaoSinais = solicitacaoSinais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOperadoraMct",
				getIdOperadoraMct()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OperadoraMct))
			return false;
        OperadoraMct castOther = (OperadoraMct) other;
		return new EqualsBuilder().append(this.getIdOperadoraMct(),
				castOther.getIdOperadoraMct()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOperadoraMct()).toHashCode();
    }

	public Integer getCdFabricante() {
		return cdFabricante;
	}

	public void setCdFabricante(Integer cdFabricante) {
		this.cdFabricante = cdFabricante;
	}

}
