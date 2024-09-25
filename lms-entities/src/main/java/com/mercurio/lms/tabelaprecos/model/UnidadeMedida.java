package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class UnidadeMedida implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idUnidadeMedida;

    /** persistent field */
    private String dsUnidadeMedida;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private String sgUnidadeMedida;

    /** persistent field */
    private List faixaProgressivas;

    /** persistent field */
    private List parcelaPrecos;

	public Long getIdUnidadeMedida() {
        return this.idUnidadeMedida;
    }

    public void setIdUnidadeMedida(Long idUnidadeMedida) {
        this.idUnidadeMedida = idUnidadeMedida;
    }

    public String getDsUnidadeMedida() {
        return this.dsUnidadeMedida;
    }

    public void setDsUnidadeMedida(String dsUnidadeMedida) {
        this.dsUnidadeMedida = dsUnidadeMedida;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getSgUnidadeMedida() {
        return this.sgUnidadeMedida;
    }

    public void setSgUnidadeMedida(String sgUnidadeMedida) {
        this.sgUnidadeMedida = sgUnidadeMedida;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.FaixaProgressiva.class)     
    public List getFaixaProgressivas() {
        return this.faixaProgressivas;
    }

    public void setFaixaProgressivas(List faixaProgressivas) {
        this.faixaProgressivas = faixaProgressivas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.ParcelaPreco.class)     
    public List getParcelaPrecos() {
        return this.parcelaPrecos;
    }

    public void setParcelaPrecos(List parcelaPrecos) {
        this.parcelaPrecos = parcelaPrecos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idUnidadeMedida",
				getIdUnidadeMedida()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof UnidadeMedida))
			return false;
        UnidadeMedida castOther = (UnidadeMedida) other;
		return new EqualsBuilder().append(this.getIdUnidadeMedida(),
				castOther.getIdUnidadeMedida()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdUnidadeMedida()).toHashCode();
    }

}
