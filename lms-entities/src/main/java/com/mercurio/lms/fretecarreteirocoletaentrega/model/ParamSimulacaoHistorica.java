package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParamSimulacaoHistorica implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParamSimulacaoHistorica;

    /** persistent field */
    private String dsParamSimulacaoHistorica;

    /** persistent field */
    private DateTime dhCriacao;

    /** persistent field */
    private Boolean blPercentual;

    /** nullable persistent field */
    private YearMonthDay dtEmissaoInicial;

    /** nullable persistent field */
    private YearMonthDay dtEmissaoFinal;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte tipoMeioTransporte;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private List ncParcelaSimulacoes;

    public Long getIdParamSimulacaoHistorica() {
        return this.idParamSimulacaoHistorica;
    }

    public void setIdParamSimulacaoHistorica(Long idParamSimulacaoHistorica) {
        this.idParamSimulacaoHistorica = idParamSimulacaoHistorica;
    }

    public String getDsParamSimulacaoHistorica() {
        return this.dsParamSimulacaoHistorica;
    }

    public void setDsParamSimulacaoHistorica(String dsParamSimulacaoHistorica) {
        this.dsParamSimulacaoHistorica = dsParamSimulacaoHistorica;
    }

    public DateTime getDhCriacao() {
        return this.dhCriacao;
    }

    public void setDhCriacao(DateTime dhCriacao) {
        this.dhCriacao = dhCriacao;
    }

    public Boolean getBlPercentual() {
        return this.blPercentual;
    }

    public void setBlPercentual(Boolean blPercentual) {
        this.blPercentual = blPercentual;
    }

    public YearMonthDay getDtEmissaoInicial() {
        return this.dtEmissaoInicial;
    }

    public void setDtEmissaoInicial(YearMonthDay dtEmissaoInicial) {
        this.dtEmissaoInicial = dtEmissaoInicial;
    }

    public YearMonthDay getDtEmissaoFinal() {
        return this.dtEmissaoFinal;
    }

    public void setDtEmissaoFinal(YearMonthDay dtEmissaoFinal) {
        this.dtEmissaoFinal = dtEmissaoFinal;
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

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.NcParcelaSimulacao.class)     
    public List getNcParcelaSimulacoes() {
        return this.ncParcelaSimulacoes;
    }

    public void setNcParcelaSimulacoes(List ncParcelaSimulacoes) {
        this.ncParcelaSimulacoes = ncParcelaSimulacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParamSimulacaoHistorica",
				getIdParamSimulacaoHistorica()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParamSimulacaoHistorica))
			return false;
        ParamSimulacaoHistorica castOther = (ParamSimulacaoHistorica) other;
		return new EqualsBuilder().append(this.getIdParamSimulacaoHistorica(),
				castOther.getIdParamSimulacaoHistorica()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParamSimulacaoHistorica())
            .toHashCode();
    }

}
