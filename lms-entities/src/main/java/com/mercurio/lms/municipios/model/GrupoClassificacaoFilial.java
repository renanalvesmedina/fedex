package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class GrupoClassificacaoFilial implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idGrupoClassificacaoFilial;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** nullable persistent field */
    private DivisaoGrupoClassificacao divisaoGrupoClassificacao;
    
    /** persistent field */
    private List limiteDescontos;

    public Long getIdGrupoClassificacaoFilial() {
        return this.idGrupoClassificacaoFilial;
    }

    public void setIdGrupoClassificacaoFilial(Long idGrupoClassificacaoFilial) {
        this.idGrupoClassificacaoFilial = idGrupoClassificacaoFilial;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao getDivisaoGrupoClassificacao() {
        return this.divisaoGrupoClassificacao;
    }

	public void setDivisaoGrupoClassificacao(
			com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao divisaoGrupoClassificacao) {
        this.divisaoGrupoClassificacao = divisaoGrupoClassificacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.LimiteDesconto.class)     
    public List getLimiteDescontos() {
        return this.limiteDescontos;
    }

    public void setLimiteDescontos(List limiteDescontos) {
        this.limiteDescontos = limiteDescontos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idGrupoClassificacaoFilial",
				getIdGrupoClassificacaoFilial()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GrupoClassificacaoFilial))
			return false;
        GrupoClassificacaoFilial castOther = (GrupoClassificacaoFilial) other;
		return new EqualsBuilder().append(this.getIdGrupoClassificacaoFilial(),
				castOther.getIdGrupoClassificacaoFilial()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGrupoClassificacaoFilial())
            .toHashCode();
    }

}
