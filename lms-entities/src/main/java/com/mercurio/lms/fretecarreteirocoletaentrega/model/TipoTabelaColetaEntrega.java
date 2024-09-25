package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoTabelaColetaEntrega implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoTabelaColetaEntrega;

    /** persistent field */
    private String dsTipoTabelaColetaEntrega;

    /** persistent field */
    private Boolean blNormal;

    /** nullable persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List tabelaColetaEntregas;

    /** persistent field */
    private List paramSimulacaoHistoricas;

    /** persistent field */
    private List simulacaoReajusteFreteCes;

    /** persistent field */
    private List controleCargas;

    public Long getIdTipoTabelaColetaEntrega() {
        return this.idTipoTabelaColetaEntrega;
    }

    public void setIdTipoTabelaColetaEntrega(Long idTipoTabelaColetaEntrega) {
        this.idTipoTabelaColetaEntrega = idTipoTabelaColetaEntrega;
    }

    public String getDsTipoTabelaColetaEntrega() {
        return this.dsTipoTabelaColetaEntrega;
    }

    public void setDsTipoTabelaColetaEntrega(String dsTipoTabelaColetaEntrega) {
        this.dsTipoTabelaColetaEntrega = dsTipoTabelaColetaEntrega;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.class)     
    public List getTabelaColetaEntregas() {
        return this.tabelaColetaEntregas;
    }

    public void setTabelaColetaEntregas(List tabelaColetaEntregas) {
        this.tabelaColetaEntregas = tabelaColetaEntregas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.ParamSimulacaoHistorica.class)     
    public List getParamSimulacaoHistoricas() {
        return this.paramSimulacaoHistoricas;
    }

    public void setParamSimulacaoHistoricas(List paramSimulacaoHistoricas) {
        this.paramSimulacaoHistoricas = paramSimulacaoHistoricas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.SimulacaoReajusteFreteCe.class)     
    public List getSimulacaoReajusteFreteCes() {
        return this.simulacaoReajusteFreteCes;
    }

    public void setSimulacaoReajusteFreteCes(List simulacaoReajusteFreteCes) {
        this.simulacaoReajusteFreteCes = simulacaoReajusteFreteCes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleCarga.class)     
    public List getControleCargas() {
        return this.controleCargas;
    }

    public void setControleCargas(List controleCargas) {
        this.controleCargas = controleCargas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoTabelaColetaEntrega",
				getIdTipoTabelaColetaEntrega()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoTabelaColetaEntrega))
			return false;
        TipoTabelaColetaEntrega castOther = (TipoTabelaColetaEntrega) other;
		return new EqualsBuilder().append(this.getIdTipoTabelaColetaEntrega(),
				castOther.getIdTipoTabelaColetaEntrega()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoTabelaColetaEntrega())
            .toHashCode();
    }

	public Boolean getBlNormal() {
		return blNormal;
	}

	public void setBlNormal(Boolean blNormal) {
		this.blNormal = blNormal;
	}

}
