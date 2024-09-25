package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Cheque implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCheque;
    
    /** persistent field */
    private Integer versao;     

    /** persistent field */
    private Short nrBanco;

    /** persistent field */
    private Short nrAgencia;

    /** persistent field */
    private Long nrCheque;

    /** persistent field */
    private BigDecimal vlCheque;

    /** persistent field */
    private String nrIdentificacaoResponsavel;

    /** persistent field */
    private YearMonthDay dtVencimento;

    /** persistent field */
    private String nrContaCorrente;

    /** persistent field */
    private DomainValue tpIdentificacaoResponsavel;

    /** persistent field */
    private String nmResponsavel;

    /** persistent field */
    private DomainValue tpSituacaoCheque;

    /** persistent field */
    private DomainValue tpSituacaoAprovacao;

    /** nullable persistent field */
    private YearMonthDay dtReapresentacao;

    /** nullable persistent field */
    private Short nrCompe;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cheque cheque;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.LoteCheque loteCheque;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.MoedaPais moedaPais;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Alinea alinea;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia Pendencia;
    
    /** persistent field */
    private List cheques;

    /** persistent field */
    private List chequeFaturas;

    /** persistent field */
    private List historicoCheques;

    public Long getIdCheque() {
        return this.idCheque;
    }

    public void setIdCheque(Long idCheque) {
        this.idCheque = idCheque;
    }

    public Short getNrBanco() {
        return this.nrBanco;
    }

    public void setNrBanco(Short nrBanco) {
        this.nrBanco = nrBanco;
    }

    public Short getNrAgencia() {
        return this.nrAgencia;
    }

    public void setNrAgencia(Short nrAgencia) {
        this.nrAgencia = nrAgencia;
    }

    public Long getNrCheque() {
        return this.nrCheque;
    }

    public void setNrCheque(Long nrCheque) {
        this.nrCheque = nrCheque;
    }

    public BigDecimal getVlCheque() {
        return this.vlCheque;
    }

    public void setVlCheque(BigDecimal vlCheque) {
        this.vlCheque = vlCheque;
    }

    public String getNrIdentificacaoResponsavel() {
        return this.nrIdentificacaoResponsavel;
    }

    public void setNrIdentificacaoResponsavel(String nrIdentificacaoResponsavel) {
        this.nrIdentificacaoResponsavel = nrIdentificacaoResponsavel;
    }

    public YearMonthDay getDtVencimento() {
        return this.dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public String getNrContaCorrente() {
        return this.nrContaCorrente;
    }

    public void setNrContaCorrente(String nrContaCorrente) {
        this.nrContaCorrente = nrContaCorrente;
    }

    public DomainValue getTpIdentificacaoResponsavel() {
        return this.tpIdentificacaoResponsavel;
    }

	public void setTpIdentificacaoResponsavel(
			DomainValue tpIdentificacaoResponsavel) {
        this.tpIdentificacaoResponsavel = tpIdentificacaoResponsavel;
    }

    public String getNmResponsavel() {
        return this.nmResponsavel;
    }

    public void setNmResponsavel(String nmResponsavel) {
        this.nmResponsavel = nmResponsavel;
    }

    public DomainValue getTpSituacaoCheque() {
        return this.tpSituacaoCheque;
    }

    public void setTpSituacaoCheque(DomainValue tpSituacaoCheque) {
        this.tpSituacaoCheque = tpSituacaoCheque;
    }

    public YearMonthDay getDtReapresentacao() {
        return this.dtReapresentacao;
    }

    public void setDtReapresentacao(YearMonthDay dtReapresentacao) {
        this.dtReapresentacao = dtReapresentacao;
    }

    public Short getNrCompe() {
        return this.nrCompe;
    }

    public void setNrCompe(Short nrCompe) {
        this.nrCompe = nrCompe;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.contasreceber.model.Cheque getCheque() {
        return this.cheque;
    }

    public void setCheque(com.mercurio.lms.contasreceber.model.Cheque cheque) {
        this.cheque = cheque;
    }

    public com.mercurio.lms.contasreceber.model.LoteCheque getLoteCheque() {
        return this.loteCheque;
    }

	public void setLoteCheque(
			com.mercurio.lms.contasreceber.model.LoteCheque loteCheque) {
        this.loteCheque = loteCheque;
    }

    public com.mercurio.lms.configuracoes.model.MoedaPais getMoedaPais() {
        return this.moedaPais;
    }

	public void setMoedaPais(
			com.mercurio.lms.configuracoes.model.MoedaPais moedaPais) {
        this.moedaPais = moedaPais;
    }

    public com.mercurio.lms.contasreceber.model.Alinea getAlinea() {
        return this.alinea;
    }

    public void setAlinea(com.mercurio.lms.contasreceber.model.Alinea alinea) {
        this.alinea = alinea;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Cheque.class)     
    public List getCheques() {
        return this.cheques;
    }

    public void setCheques(List cheques) {
        this.cheques = cheques;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ChequeFatura.class)     
    public List getChequeFaturas() {
        return this.chequeFaturas;
    }

    public void setChequeFaturas(List chequeFaturas) {
        this.chequeFaturas = chequeFaturas;
    }

    public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoCheque.class)     
    public List getHistoricoCheques() {
        return this.historicoCheques;
    }

    public void setHistoricoCheques(List historicoCheques) {
        this.historicoCheques = historicoCheques;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCheque", getIdCheque())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Cheque))
			return false;
        Cheque castOther = (Cheque) other;
		return new EqualsBuilder().append(this.getIdCheque(),
				castOther.getIdCheque()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCheque()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return Pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		Pendencia = pendencia;
	}

}