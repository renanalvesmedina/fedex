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
public class GrupoClassificacao implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String ID_GRUPO_CLASSIFICACAO_DESCONTOS = "IDGrupoClassificacaoDescontos";

    /** identifier field */
    private Long idGrupoClassificacao;

    /** persistent field */
    private VarcharI18n dsGrupoClassificacao;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List divisaoGrupoClassificacoes;

    public Long getIdGrupoClassificacao() {
        return this.idGrupoClassificacao;
    }

    public void setIdGrupoClassificacao(Long idGrupoClassificacao) {
        this.idGrupoClassificacao = idGrupoClassificacao;
    }

    public VarcharI18n getDsGrupoClassificacao() {
		return dsGrupoClassificacao;
    }

	public void setDsGrupoClassificacao(VarcharI18n dsGrupoClassificacao) {
        this.dsGrupoClassificacao = dsGrupoClassificacao;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao.class)     
    public List getDivisaoGrupoClassificacoes() {
        return this.divisaoGrupoClassificacoes;
    }

    public void setDivisaoGrupoClassificacoes(List divisaoGrupoClassificacoes) {
        this.divisaoGrupoClassificacoes = divisaoGrupoClassificacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idGrupoClassificacao",
				getIdGrupoClassificacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GrupoClassificacao))
			return false;
        GrupoClassificacao castOther = (GrupoClassificacao) other;
		return new EqualsBuilder().append(this.getIdGrupoClassificacao(),
				castOther.getIdGrupoClassificacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGrupoClassificacao())
            .toHashCode();
    }

}
