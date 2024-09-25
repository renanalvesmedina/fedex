package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class DivisaoGrupoClassificacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDivisaoGrupoClassificacao;

    /** persistent field */
    private VarcharI18n dsDivisaoGrupoClassificacao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.GrupoClassificacao grupoClassificacao;

    /** persistent field */
    private List grupoClassificacaoFiliais;

    public Long getIdDivisaoGrupoClassificacao() {
        return this.idDivisaoGrupoClassificacao;
    }

    public void setIdDivisaoGrupoClassificacao(Long idDivisaoGrupoClassificacao) {
        this.idDivisaoGrupoClassificacao = idDivisaoGrupoClassificacao;
    }

    public VarcharI18n getDsDivisaoGrupoClassificacao() {
		return dsDivisaoGrupoClassificacao;
    }

	public void setDsDivisaoGrupoClassificacao(
			VarcharI18n dsDivisaoGrupoClassificacao) {
        this.dsDivisaoGrupoClassificacao = dsDivisaoGrupoClassificacao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.GrupoClassificacao getGrupoClassificacao() {
        return this.grupoClassificacao;
    }

	public void setGrupoClassificacao(
			com.mercurio.lms.municipios.model.GrupoClassificacao grupoClassificacao) {
        this.grupoClassificacao = grupoClassificacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.GrupoClassificacaoFilial.class)     
    public List getGrupoClassificacaoFiliais() {
        return this.grupoClassificacaoFiliais;
    }

    public void setGrupoClassificacaoFiliais(List grupoClassificacaoFiliais) {
        this.grupoClassificacaoFiliais = grupoClassificacaoFiliais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDivisaoGrupoClassificacao",
				getIdDivisaoGrupoClassificacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DivisaoGrupoClassificacao))
			return false;
        DivisaoGrupoClassificacao castOther = (DivisaoGrupoClassificacao) other;
		return new EqualsBuilder().append(
				this.getIdDivisaoGrupoClassificacao(),
				castOther.getIdDivisaoGrupoClassificacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDivisaoGrupoClassificacao())
            .toHashCode();
    }

}
