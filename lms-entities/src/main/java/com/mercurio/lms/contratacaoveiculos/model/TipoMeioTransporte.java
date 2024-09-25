package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoMeioTransporte;

    /** persistent field */
    private String dsTipoMeioTransporte;

    /** persistent field */
    private Integer nrCapacidadePesoInicial;

    /** persistent field */
    private Integer nrCapacidadePesoFinal;

    /** persistent field */
    private DomainValue tpCategoria;

    /** persistent field */
    private DomainValue tpMeioTransporte;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private List eixosTipoMeioTransporte;
    
    /** persistent field */
    private List modeloMeioTransportes;

    /** persistent field */
    private List criterioAplicSimulacoes;

    /** persistent field */
    private List valorTarifaPostoPassagems;

    /** persistent field */
    private List itChecklistTpMeioTransps;

    /** persistent field */
    private List solicitacaoContratacoes;

    /** persistent field */
    private List tipoMeioTranspRotaEvents;

    /** persistent field */
    private List tpCombustTpMeioTransps;

    /** persistent field */
    private List referenciaTipoVeiculos;

    /** persistent field */
    private List rotaViagems;

    /** persistent field */
    private List tabelaColetaEntregas;

    /** persistent field */
    private List tipoMeioTransportes;

    /** persistent field */
    private List rotaTipoMeioTransportes;

    /** persistent field */
    private List paramSimulacaoHistoricas;

    /** persistent field */
    private List simulacaoReajusteFreteCes;
 
    public Long getIdTipoMeioTransporte() {
        return this.idTipoMeioTransporte;
    }

    public void setIdTipoMeioTransporte(Long idTipoMeioTransporte) {
        this.idTipoMeioTransporte = idTipoMeioTransporte;
    }

    public String getDsTipoMeioTransporte() {
        return this.dsTipoMeioTransporte;
    }

    public void setDsTipoMeioTransporte(String dsTipoMeioTransporte) {
        this.dsTipoMeioTransporte = dsTipoMeioTransporte;
    }

    public Integer getNrCapacidadePesoInicial() {
        return this.nrCapacidadePesoInicial;
    }

    public void setNrCapacidadePesoInicial(Integer nrCapacidadePesoInicial) {
        this.nrCapacidadePesoInicial = nrCapacidadePesoInicial;
    }

    public Integer getNrCapacidadePesoFinal() {
        return this.nrCapacidadePesoFinal;
    }

    public void setNrCapacidadePesoFinal(Integer nrCapacidadePesoFinal) {
        this.nrCapacidadePesoFinal = nrCapacidadePesoFinal;
    }

    public DomainValue getTpCategoria() {
        return this.tpCategoria;
    }

    public void setTpCategoria(DomainValue tpCategoria) {
        this.tpCategoria = tpCategoria;
    }

    public DomainValue getTpMeioTransporte() {
        return this.tpMeioTransporte;
    }

    public void setTpMeioTransporte(DomainValue tpMeioTransporte) {
        this.tpMeioTransporte = tpMeioTransporte;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTransporte.class)     
    public List getModeloMeioTransportes() {
        return this.modeloMeioTransportes;
    }

    public void setModeloMeioTransportes(List modeloMeioTransportes) {
        this.modeloMeioTransportes = modeloMeioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.CriterioAplicSimulacao.class)     
    public List getCriterioAplicSimulacoes() {
        return this.criterioAplicSimulacoes;
    }

    public void setCriterioAplicSimulacoes(List criterioAplicSimulacoes) {
        this.criterioAplicSimulacoes = criterioAplicSimulacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.ValorTarifaPostoPassagem.class)     
    public List getValorTarifaPostoPassagems() {
        return this.valorTarifaPostoPassagems;
    }

    public void setValorTarifaPostoPassagems(List valorTarifaPostoPassagems) {
        this.valorTarifaPostoPassagems = valorTarifaPostoPassagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp.class)     
    public List getItChecklistTpMeioTransps() {
        return this.itChecklistTpMeioTransps;
    }

    public void setItChecklistTpMeioTransps(List itChecklistTpMeioTransps) {
        this.itChecklistTpMeioTransps = itChecklistTpMeioTransps;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao.class)     
    public List getSolicitacaoContratacoes() {
        return this.solicitacaoContratacoes;
    }

    public void setSolicitacaoContratacoes(List solicitacaoContratacoes) {
        this.solicitacaoContratacoes = solicitacaoContratacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.TipoMeioTranspRotaEvent.class)     
    public List getTipoMeioTranspRotaEvents() {
        return this.tipoMeioTranspRotaEvents;
    }

    public void setTipoMeioTranspRotaEvents(List tipoMeioTranspRotaEvents) {
        this.tipoMeioTranspRotaEvents = tipoMeioTranspRotaEvents;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.TpCombustTpMeioTransp.class)     
    public List getTpCombustTpMeioTransps() {
        return this.tpCombustTpMeioTransps;
    }

    public void setTpCombustTpMeioTransps(List tpCombustTpMeioTransps) {
        this.tpCombustTpMeioTransps = tpCombustTpMeioTransps;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReferenciaTipoVeiculo.class)     
    public List getReferenciaTipoVeiculos() {
        return this.referenciaTipoVeiculos;
    }

    public void setReferenciaTipoVeiculos(List referenciaTipoVeiculos) {
        this.referenciaTipoVeiculos = referenciaTipoVeiculos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaViagem.class)     
    public List getRotaViagems() {
        return this.rotaViagems;
    }

    public void setRotaViagems(List rotaViagems) {
        this.rotaViagems = rotaViagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega.class)     
    public List getTabelaColetaEntregas() {
        return this.tabelaColetaEntregas;
    }

    public void setTabelaColetaEntregas(List tabelaColetaEntregas) {
        this.tabelaColetaEntregas = tabelaColetaEntregas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte.class)     
    public List getTipoMeioTransportes() {
        return this.tipoMeioTransportes;
    }

    public void setTipoMeioTransportes(List tipoMeioTransportes) {
        this.tipoMeioTransportes = tipoMeioTransportes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaTipoMeioTransporte.class)     
    public List getRotaTipoMeioTransportes() {
        return this.rotaTipoMeioTransportes;
    }

    public void setRotaTipoMeioTransportes(List rotaTipoMeioTransportes) {
        this.rotaTipoMeioTransportes = rotaTipoMeioTransportes;
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

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.EixosTipoMeioTransporte.class)     
    public List getEixosTipoMeioTransporte() {
		return eixosTipoMeioTransporte;
	}

	public void setEixosTipoMeioTransporte(List eixosTipoMeioTransporte) {
		this.eixosTipoMeioTransporte = eixosTipoMeioTransporte;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idTipoMeioTransporte",
				getIdTipoMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoMeioTransporte))
			return false;
        TipoMeioTransporte castOther = (TipoMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdTipoMeioTransporte(),
				castOther.getIdTipoMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoMeioTransporte())
            .toHashCode();
    }

}
