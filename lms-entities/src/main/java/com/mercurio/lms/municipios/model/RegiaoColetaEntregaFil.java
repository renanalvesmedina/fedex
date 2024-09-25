package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegiaoColetaEntregaFil implements Serializable,Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegiaoColetaEntregaFil;

    /** persistent field */
    private String dsRegiaoColetaEntregaFil;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    private Integer acaoVigenciaAtual;
    
    /** persistent field */
    private List regiaoFilialRotaColEnts;

    /** persistent field */
    private List funcionarioRegioes;

    public Long getIdRegiaoColetaEntregaFil() {
        return this.idRegiaoColetaEntregaFil;
    }

    public void setIdRegiaoColetaEntregaFil(Long idRegiaoColetaEntregaFil) {
        this.idRegiaoColetaEntregaFil = idRegiaoColetaEntregaFil;
    }

    public String getDsRegiaoColetaEntregaFil() {
        return this.dsRegiaoColetaEntregaFil;
    }

    public void setDsRegiaoColetaEntregaFil(String dsRegiaoColetaEntregaFil) {
        this.dsRegiaoColetaEntregaFil = dsRegiaoColetaEntregaFil;
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

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt.class)     
    public List getRegiaoFilialRotaColEnts() {
        return this.regiaoFilialRotaColEnts;
    }

    public void setRegiaoFilialRotaColEnts(List regiaoFilialRotaColEnts) {
        this.regiaoFilialRotaColEnts = regiaoFilialRotaColEnts;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.FuncionarioRegiao.class)     
    public List getFuncionarioRegioes() {
        return this.funcionarioRegioes;
    }

    public void setFuncionarioRegioes(List funcionarioRegioes) {
        this.funcionarioRegioes = funcionarioRegioes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRegiaoColetaEntregaFil",
				getIdRegiaoColetaEntregaFil()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegiaoColetaEntregaFil))
			return false;
        RegiaoColetaEntregaFil castOther = (RegiaoColetaEntregaFil) other;
		return new EqualsBuilder().append(this.getIdRegiaoColetaEntregaFil(),
				castOther.getIdRegiaoColetaEntregaFil()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegiaoColetaEntregaFil())
            .toHashCode();
    }

	public Integer getAcaoVigenciaAtual() {
		return acaoVigenciaAtual;
	}

	public void setAcaoVigenciaAtual(Integer acaoVigenciaAtual) {
		this.acaoVigenciaAtual = acaoVigenciaAtual;
	}

}
