package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.configuracoes.model.MoedaPais;

/** @author LMS Custom Hibernate CodeGenerator */
public class SimulacaoReajusteFreteCe implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSimulacaoReajusteFreteCe;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega;
    
    private MoedaPais moedaPais;

    /** persistent field */
    private List parcelaReajustes;

    /** persistent field */
    private List filialReajustes;

    public Long getIdSimulacaoReajusteFreteCe() {
        return this.idSimulacaoReajusteFreteCe;
    }

    public void setIdSimulacaoReajusteFreteCe(Long idSimulacaoReajusteFreteCe) {
        this.idSimulacaoReajusteFreteCe = idSimulacaoReajusteFreteCe;
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

    public com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte getTipoMeioTransporte() {
        return this.tipoMeioTransporte;
    }

	public void setTipoMeioTransporte(
			com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte) {
        this.tipoMeioTransporte = tipoMeioTransporte;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega getTipoTabelaColetaEntrega() {
        return this.tipoTabelaColetaEntrega;
    }

	public void setTipoTabelaColetaEntrega(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega) {
        this.tipoTabelaColetaEntrega = tipoTabelaColetaEntrega;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaReajuste.class)     
    public List getParcelaReajustes() {
        return this.parcelaReajustes;
    }

    public void setParcelaReajustes(List parcelaReajustes) {
        this.parcelaReajustes = parcelaReajustes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.FilialReajuste.class)     
    public List getFilialReajustes() {
        return this.filialReajustes;
    }

    public void setFilialReajustes(List filialReajustes) {
        this.filialReajustes = filialReajustes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSimulacaoReajusteFreteCe",
				getIdSimulacaoReajusteFreteCe()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SimulacaoReajusteFreteCe))
			return false;
        SimulacaoReajusteFreteCe castOther = (SimulacaoReajusteFreteCe) other;
		return new EqualsBuilder().append(this.getIdSimulacaoReajusteFreteCe(),
				castOther.getIdSimulacaoReajusteFreteCe()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSimulacaoReajusteFreteCe())
            .toHashCode();
    }

	public MoedaPais getMoedaPais() {
		return moedaPais;
	}

	public void setMoedaPais(MoedaPais moedaPais) {
		this.moedaPais = moedaPais;
	}

}
