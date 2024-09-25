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
public class DepositoCcorrente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDepositoCcorrente;

    private Integer versao;   

    /** persistent field */
    private BigDecimal vlDeposito;

    /** persistent field */
    private YearMonthDay dtDeposito;

    /** persistent field */
    private YearMonthDay dtCarga;

    /** persistent field */
    private DomainValue tpOrigem;

    /** persistent field */
    private Boolean blRelacaoFechada;

    /** persistent field */
    private DomainValue tpSituacaoRelacao;

    /** nullable persistent field */
    private String obDepositoCcorrente;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;
    
    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cedente cedente;    

    /** persistent field */
    private List itemDepositoCcorrentes;

    public Long getIdDepositoCcorrente() {
        return this.idDepositoCcorrente;
    }

    public void setIdDepositoCcorrente(Long idDepositoCcorrente) {
        this.idDepositoCcorrente = idDepositoCcorrente;
    }

    public BigDecimal getVlDeposito() {
        return this.vlDeposito;
    }

    public void setVlDeposito(BigDecimal vlDeposito) {
        this.vlDeposito = vlDeposito;
    }

    public YearMonthDay getDtDeposito() {
        return this.dtDeposito;
    }

    public void setDtDeposito(YearMonthDay dtDeposito) {
        this.dtDeposito = dtDeposito;
    }

    public YearMonthDay getDtCarga() {
        return this.dtCarga;
    }

    public void setDtCarga(YearMonthDay dtCarga) {
        this.dtCarga = dtCarga;
    }

    public DomainValue getTpOrigem() {
        return this.tpOrigem;
    }

    public void setTpOrigem(DomainValue tpOrigem) {
        this.tpOrigem = tpOrigem;
    }

    public Boolean getBlRelacaoFechada() {
        return this.blRelacaoFechada;
    }

    public void setBlRelacaoFechada(Boolean blRelacaoFechada) {
        this.blRelacaoFechada = blRelacaoFechada;
    }

    public DomainValue getTpSituacaoRelacao() {
        return this.tpSituacaoRelacao;
    }

    public void setTpSituacaoRelacao(DomainValue tpSituacaoRelacao) {
        this.tpSituacaoRelacao = tpSituacaoRelacao;
    }

    public String getObDepositoCcorrente() {
        return this.obDepositoCcorrente;
    }

    public void setObDepositoCcorrente(String obDepositoCcorrente) {
        this.obDepositoCcorrente = obDepositoCcorrente;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente.class)     
    public List getItemDepositoCcorrentes() {
        return this.itemDepositoCcorrentes;
    }

    public void setItemDepositoCcorrentes(List itemDepositoCcorrentes) {
        this.itemDepositoCcorrentes = itemDepositoCcorrentes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDepositoCcorrente",
				getIdDepositoCcorrente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DepositoCcorrente))
			return false;
        DepositoCcorrente castOther = (DepositoCcorrente) other;
		return new EqualsBuilder().append(this.getIdDepositoCcorrente(),
				castOther.getIdDepositoCcorrente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDepositoCcorrente())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.contasreceber.model.Cedente getCedente() {
		return cedente;
	}

	public void setCedente(com.mercurio.lms.contasreceber.model.Cedente cedente) {
		this.cedente = cedente;
	}

}
