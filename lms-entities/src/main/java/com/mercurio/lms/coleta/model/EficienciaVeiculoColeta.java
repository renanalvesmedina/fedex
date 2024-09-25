package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class EficienciaVeiculoColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEficienciaVeiculoColeta;

    /** persistent field */
    private YearMonthDay dtMovimentacao;

    /** persistent field */
    private Integer qtSolicitadas;

    /** persistent field */
    private Integer qtManifestadas;

    /** persistent field */
    private Integer qtTransmitidas;

    /** persistent field */
    private Integer qtExecutadas;

    /** persistent field */
    private Integer qtCanceladas;

    /** persistent field */
    private Integer qtRetornaram;

    /** persistent field */
    private Integer qtProgramadasMaisUmaVez;

    /** persistent field */
    private Integer qtProgramadasNaData;

    /** persistent field */
    private Integer qtRetornaramPorIneficiencia;

    /** persistent field */
    private Integer qtTotalProgramacoes;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    public Long getIdEficienciaVeiculoColeta() {
        return this.idEficienciaVeiculoColeta;
    }

    public void setIdEficienciaVeiculoColeta(Long idEficienciaVeiculoColeta) {
        this.idEficienciaVeiculoColeta = idEficienciaVeiculoColeta;
    }

    public YearMonthDay getDtMovimentacao() {
        return this.dtMovimentacao;
    }

    public void setDtMovimentacao(YearMonthDay dtMovimentacao) {
        this.dtMovimentacao = dtMovimentacao;
    }

    public Integer getQtSolicitadas() {
        return this.qtSolicitadas;
    }

    public void setQtSolicitadas(Integer qtSolicitadas) {
        this.qtSolicitadas = qtSolicitadas;
    }

    public Integer getQtManifestadas() {
        return this.qtManifestadas;
    }

    public void setQtManifestadas(Integer qtManifestadas) {
        this.qtManifestadas = qtManifestadas;
    }

    public Integer getQtTransmitidas() {
        return this.qtTransmitidas;
    }

    public void setQtTransmitidas(Integer qtTransmitidas) {
        this.qtTransmitidas = qtTransmitidas;
    }

    public Integer getQtExecutadas() {
        return this.qtExecutadas;
    }

    public void setQtExecutadas(Integer qtExecutadas) {
        this.qtExecutadas = qtExecutadas;
    }

    public Integer getQtCanceladas() {
        return this.qtCanceladas;
    }

    public void setQtCanceladas(Integer qtCanceladas) {
        this.qtCanceladas = qtCanceladas;
    }

    public Integer getQtRetornaram() {
        return this.qtRetornaram;
    }

    public void setQtRetornaram(Integer qtRetornaram) {
        this.qtRetornaram = qtRetornaram;
    }

    public Integer getQtProgramadasMaisUmaVez() {
        return this.qtProgramadasMaisUmaVez;
    }

    public void setQtProgramadasMaisUmaVez(Integer qtProgramadasMaisUmaVez) {
        this.qtProgramadasMaisUmaVez = qtProgramadasMaisUmaVez;
    }

    public Integer getQtProgramadasNaData() {
        return this.qtProgramadasNaData;
    }

    public void setQtProgramadasNaData(Integer qtProgramadasNaData) {
        this.qtProgramadasNaData = qtProgramadasNaData;
    }

    public Integer getQtRetornaramPorIneficiencia() {
        return this.qtRetornaramPorIneficiencia;
    }

	public void setQtRetornaramPorIneficiencia(
			Integer qtRetornaramPorIneficiencia) {
        this.qtRetornaramPorIneficiencia = qtRetornaramPorIneficiencia;
    }

    public Integer getQtTotalProgramacoes() {
        return this.qtTotalProgramacoes;
    }

    public void setQtTotalProgramacoes(Integer qtTotalProgramacoes) {
        this.qtTotalProgramacoes = qtTotalProgramacoes;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEficienciaVeiculoColeta",
				getIdEficienciaVeiculoColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EficienciaVeiculoColeta))
			return false;
        EficienciaVeiculoColeta castOther = (EficienciaVeiculoColeta) other;
		return new EqualsBuilder().append(this.getIdEficienciaVeiculoColeta(),
				castOther.getIdEficienciaVeiculoColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEficienciaVeiculoColeta())
            .toHashCode();
    }

}
