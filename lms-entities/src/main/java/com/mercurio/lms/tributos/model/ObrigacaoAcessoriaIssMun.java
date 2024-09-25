package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ObrigacaoAcessoriaIssMun implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idObrigacaoAcessoriaIssMun;

    /** persistent field */
    private String dsObrigacaoAcessoriaIssMun;

    /** persistent field */
    private DomainValue tpPeriodicidade;

    /** nullable persistent field */
    private String dsDiaEntrega1;

    /** nullable persistent field */
    private String dsDiaEntrega2;

    /** nullable persistent field */
    private String dsDiaEntrega3;

    /** nullable persistent field */
    private String obObrigacaoAcessoriaIssMun;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    public Long getIdObrigacaoAcessoriaIssMun() {
        return this.idObrigacaoAcessoriaIssMun;
    }

    public void setIdObrigacaoAcessoriaIssMun(Long idObrigacaoAcessoriaIssMun) {
        this.idObrigacaoAcessoriaIssMun = idObrigacaoAcessoriaIssMun;
    }

    public String getDsObrigacaoAcessoriaIssMun() {
        return this.dsObrigacaoAcessoriaIssMun;
    }

    public void setDsObrigacaoAcessoriaIssMun(String dsObrigacaoAcessoriaIssMun) {
        this.dsObrigacaoAcessoriaIssMun = dsObrigacaoAcessoriaIssMun;
    }

    public DomainValue getTpPeriodicidade() {
        return this.tpPeriodicidade;
    }

    public void setTpPeriodicidade(DomainValue tpPeriodicidade) {
        this.tpPeriodicidade = tpPeriodicidade;
    }

    public String getDsDiaEntrega1() {
        return this.dsDiaEntrega1;
    }

    public void setDsDiaEntrega1(String dsDiaEntrega1) {
        this.dsDiaEntrega1 = dsDiaEntrega1;
    }

    public String getDsDiaEntrega2() {
        return this.dsDiaEntrega2;
    }

    public void setDsDiaEntrega2(String dsDiaEntrega2) {
        this.dsDiaEntrega2 = dsDiaEntrega2;
    }

    public String getDsDiaEntrega3() {
        return this.dsDiaEntrega3;
    }

    public void setDsDiaEntrega3(String dsDiaEntrega3) {
        this.dsDiaEntrega3 = dsDiaEntrega3;
    }

    public String getObObrigacaoAcessoriaIssMun() {
        return this.obObrigacaoAcessoriaIssMun;
    }

    public void setObObrigacaoAcessoriaIssMun(String obObrigacaoAcessoriaIssMun) {
        this.obObrigacaoAcessoriaIssMun = obObrigacaoAcessoriaIssMun;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idObrigacaoAcessoriaIssMun",
				getIdObrigacaoAcessoriaIssMun()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObrigacaoAcessoriaIssMun))
			return false;
        ObrigacaoAcessoriaIssMun castOther = (ObrigacaoAcessoriaIssMun) other;
		return new EqualsBuilder().append(this.getIdObrigacaoAcessoriaIssMun(),
				castOther.getIdObrigacaoAcessoriaIssMun()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdObrigacaoAcessoriaIssMun())
            .toHashCode();
    }

}
