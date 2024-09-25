package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class SubtipoTabelaPreco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSubtipoTabelaPreco;

    /** persistent field */
    private String tpSubtipoTabelaPreco;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private DomainValue tpTipoTabelaPreco;

    /** nullable persistent field */
    private String dsSubtipoTabelaPreco;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List tabelaPrecos;

    /** persistent field */
    private List limiteDescontos;

    public Long getIdSubtipoTabelaPreco() {
        return this.idSubtipoTabelaPreco;
    }

    public void setIdSubtipoTabelaPreco(Long idSubtipoTabelaPreco) {
        this.idSubtipoTabelaPreco = idSubtipoTabelaPreco;
    }

    public String getTpSubtipoTabelaPreco() {
        return this.tpSubtipoTabelaPreco;
    }

    public void setTpSubtipoTabelaPreco(String tpSubtipoTabelaPreco) {
        this.tpSubtipoTabelaPreco = tpSubtipoTabelaPreco;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DomainValue getTpTipoTabelaPreco() {
        return this.tpTipoTabelaPreco;
    }

    public void setTpTipoTabelaPreco(DomainValue tpTipoTabelaPreco) {
        this.tpTipoTabelaPreco = tpTipoTabelaPreco;
    }

    public String getDsSubtipoTabelaPreco() {
        return this.dsSubtipoTabelaPreco;
    }

    public void setDsSubtipoTabelaPreco(String dsSubtipoTabelaPreco) {
        this.dsSubtipoTabelaPreco = dsSubtipoTabelaPreco;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.TabelaPreco.class)     
    public List getTabelaPrecos() {
        return this.tabelaPrecos;
    }

    public void setTabelaPrecos(List tabelaPrecos) {
        this.tabelaPrecos = tabelaPrecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.LimiteDesconto.class)     
    public List getLimiteDescontos() {
        return this.limiteDescontos;
    }

    public void setLimiteDescontos(List limiteDescontos) {
        this.limiteDescontos = limiteDescontos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSubtipoTabelaPreco",
				getIdSubtipoTabelaPreco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubtipoTabelaPreco))
			return false;
        SubtipoTabelaPreco castOther = (SubtipoTabelaPreco) other;
		return new EqualsBuilder().append(this.getIdSubtipoTabelaPreco(),
				castOther.getIdSubtipoTabelaPreco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSubtipoTabelaPreco())
            .toHashCode();
    }

}
