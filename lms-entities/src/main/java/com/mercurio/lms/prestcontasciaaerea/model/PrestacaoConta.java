package com.mercurio.lms.prestcontasciaaerea.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;

/** @author LMS Custom Hibernate CodeGenerator */
public class PrestacaoConta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPrestacaoConta;

    private CiaFilialMercurio ciaFilialMercurio;

    /** persistent field */
    private Long nrPrestacaoConta;

    /** persistent field */
    private Long qtAwb;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private YearMonthDay dtInicial;

    /** persistent field */
    private YearMonthDay dtFinal;

    /** persistent field */
    private YearMonthDay dtVencimento;

    /** persistent field */
    private List valorPrestacaoContas;

    /** persistent field */
    private List awbCancelados;

    /** persistent field */
    private List intervaloAwbs;

    /** persistent field */
    private List icmsPrestacoes;

    public Long getIdPrestacaoConta() {
        return this.idPrestacaoConta;
    }

    public void setIdPrestacaoConta(Long idPrestacaoConta) {
        this.idPrestacaoConta = idPrestacaoConta;
    }

    public CiaFilialMercurio getCiaFilialMercurio() {
		return ciaFilialMercurio;
	}

	public void setCiaFilialMercurio(CiaFilialMercurio ciaFilialMercurio) {
		this.ciaFilialMercurio = ciaFilialMercurio;
	}

	public Long getNrPrestacaoConta() {
        return this.nrPrestacaoConta;
    }

    public void setNrPrestacaoConta(Long nrPrestacaoConta) {
        this.nrPrestacaoConta = nrPrestacaoConta;
    }

	public Long getQtAwb() {
        return this.qtAwb;
    }

    public void setQtAwb(Long qtAwb) {
        this.qtAwb = qtAwb;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public YearMonthDay getDtInicial() {
        return this.dtInicial;
    }

    public void setDtInicial(YearMonthDay dtInicial) {
        this.dtInicial = dtInicial;
    }

    public YearMonthDay getDtFinal() {
        return this.dtFinal;
    }

    public void setDtFinal(YearMonthDay dtFinal) {
        this.dtFinal = dtFinal;
    }

    public YearMonthDay getDtVencimento() {
        return this.dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.prestcontasciaaerea.model.ValorPrestacaoConta.class)     
    public List getValorPrestacaoContas() {
        return this.valorPrestacaoContas;
    }

    public void setValorPrestacaoContas(List valorPrestacaoContas) {
        this.valorPrestacaoContas = valorPrestacaoContas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.prestcontasciaaerea.model.AwbCancelado.class)     
    public List getAwbCancelados() {
        return this.awbCancelados;
    }

    public void setAwbCancelados(List awbCancelados) {
        this.awbCancelados = awbCancelados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.prestcontasciaaerea.model.IntervaloAwb.class)     
    public List getIntervaloAwbs() {
        return this.intervaloAwbs;
    }

    public void setIntervaloAwbs(List intervaloAwbs) {
        this.intervaloAwbs = intervaloAwbs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.prestcontasciaaerea.model.IcmsPrestacao.class)     
    public List getIcmsPrestacoes() {
        return this.icmsPrestacoes;
    }

    public void setIcmsPrestacoes(List icmsPrestacoes) {
        this.icmsPrestacoes = icmsPrestacoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPrestacaoConta",
				getIdPrestacaoConta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PrestacaoConta))
			return false;
        PrestacaoConta castOther = (PrestacaoConta) other;
		return new EqualsBuilder().append(this.getIdPrestacaoConta(),
				castOther.getIdPrestacaoConta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPrestacaoConta()).toHashCode();
    }

}
