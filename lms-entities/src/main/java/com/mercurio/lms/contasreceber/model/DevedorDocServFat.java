package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.DoctoServicoFin;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class DevedorDocServFat implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idDevedorDocServFat;

	/** persistent field */
	private BigDecimal vlDevido;

	/** persistent field */
	private DomainValue tpSituacaoCobranca;

	/** nullable persistent field */
	private YearMonthDay dtLiquidacao;

	/** nullable persistent field */
	private YearMonthDay dtEntradaCobrJur;

	/** nullable persistent field */
	private YearMonthDay dtSaidaCobrJur;

	/** nullable persistent field */
	private YearMonthDay dtPrevistaFaturamento;

	/** nullable persistent field */
	private YearMonthDay dtPrevistaVencimento;

	/** persistent field */
	private Cliente cliente;

	/** persistent field */
	private DoctoServico doctoServico;
	
	/** persistent field */
	private DoctoServicoFin doctoServicoFin;	

	/** persistent field */
	private Filial filial;

	/** persistent field */
	private DivisaoCliente divisaoCliente;
	
	/** persistent field */
	private Fatura fatura;

	/** persistent field */
	private List itemTransferencias;

	/** persistent field */
	private List itemDepositoCcorrentes;

	/** persistent field */
	private List itemFaturas;

	/** persistent field */
	private List descontos;

	/** persistent field */
	private List agendaTransferencias;

	/** persistent field */
	private List itemBaixaDevMercs;

	public DevedorDocServFat() {
	}

	public DevedorDocServFat(Long idDevedorDocServFat, BigDecimal vlDevido, DomainValue tpSituacaoCobranca, YearMonthDay dtLiquidacao, YearMonthDay dtEntradaCobrJur, YearMonthDay dtSaidaCobrJur, YearMonthDay dtPrevistaFaturamento, YearMonthDay dtPrevistaVencimento, Cliente cliente, DoctoServico doctoServico, DoctoServicoFin doctoServicoFin, Filial filial, DivisaoCliente divisaoCliente, Fatura fatura, List itemTransferencias, List itemDepositoCcorrentes, List itemFaturas, List descontos, List agendaTransferencias, List itemBaixaDevMercs) {
		this.idDevedorDocServFat = idDevedorDocServFat;
		this.vlDevido = vlDevido;
		this.tpSituacaoCobranca = tpSituacaoCobranca;
		this.dtLiquidacao = dtLiquidacao;
		this.dtEntradaCobrJur = dtEntradaCobrJur;
		this.dtSaidaCobrJur = dtSaidaCobrJur;
		this.dtPrevistaFaturamento = dtPrevistaFaturamento;
		this.dtPrevistaVencimento = dtPrevistaVencimento;
		this.cliente = cliente;
		this.doctoServico = doctoServico;
		this.doctoServicoFin = doctoServicoFin;
		this.filial = filial;
		this.divisaoCliente = divisaoCliente;
		this.fatura = fatura;
		this.itemTransferencias = itemTransferencias;
		this.itemDepositoCcorrentes = itemDepositoCcorrentes;
		this.itemFaturas = itemFaturas;
		this.descontos = descontos;
		this.agendaTransferencias = agendaTransferencias;
		this.itemBaixaDevMercs = itemBaixaDevMercs;
	}

	public Long getIdDevedorDocServFat() {
		return this.idDevedorDocServFat;
	}

	public void setIdDevedorDocServFat(Long idDevedorDocServFat) {
		this.idDevedorDocServFat = idDevedorDocServFat;
	}

	public BigDecimal getVlDevido() {
		return this.vlDevido;
	}

	public void setVlDevido(BigDecimal vlDevido) {
		this.vlDevido = vlDevido;
	}

	public DomainValue getTpSituacaoCobranca() {
		return this.tpSituacaoCobranca;
	}

	public void setTpSituacaoCobranca(DomainValue tpSituacaoCobranca) {
		this.tpSituacaoCobranca = tpSituacaoCobranca;
	}

	public YearMonthDay getDtLiquidacao() {
		return this.dtLiquidacao;
	}

	public void setDtLiquidacao(YearMonthDay dtLiquidacao) {
		this.dtLiquidacao = dtLiquidacao;
	}

	public YearMonthDay getDtEntradaCobrJur() {
		return this.dtEntradaCobrJur;
	}

	public void setDtEntradaCobrJur(YearMonthDay dtEntradaCobrJur) {
		this.dtEntradaCobrJur = dtEntradaCobrJur;
	}

	public YearMonthDay getDtSaidaCobrJur() {
		return this.dtSaidaCobrJur;
	}

	public void setDtSaidaCobrJur(YearMonthDay dtSaidaCobrJur) {
		this.dtSaidaCobrJur = dtSaidaCobrJur;
	}

	public YearMonthDay getDtPrevistaFaturamento() {
		return this.dtPrevistaFaturamento;
	}

	public void setDtPrevistaFaturamento(YearMonthDay dtPrevistaFaturamento) {
		this.dtPrevistaFaturamento = dtPrevistaFaturamento;
	}

	public YearMonthDay getDtPrevistaVencimento() {
		return this.dtPrevistaVencimento;
	}

	public void setDtPrevistaVencimento(YearMonthDay dtPrevistaVencimento) {
		this.dtPrevistaVencimento = dtPrevistaVencimento;
	}

	public Cliente getCliente() {
		return this.cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DoctoServico getDoctoServico() {
		return this.doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}
	
	public DoctoServicoFin getDoctoServicoFin() {
		return doctoServicoFin;
	}

	public void setDoctoServicoFin(DoctoServicoFin doctoServicoFin) {
		this.doctoServicoFin = doctoServicoFin;
	}	

	public Filial getFilial() {
		return this.filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemTransferencia.class) 
	public List getItemTransferencias() {
		return this.itemTransferencias;
	}

	public void setItemTransferencias(List itemTransferencias) {
		this.itemTransferencias = itemTransferencias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente.class) 
	public List getItemDepositoCcorrentes() {
		return this.itemDepositoCcorrentes;
	}

	public void setItemDepositoCcorrentes(List itemDepositoCcorrentes) {
		this.itemDepositoCcorrentes = itemDepositoCcorrentes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemFatura.class) 
	public List getItemFaturas() {
		return this.itemFaturas;
	}
	
	public ItemFatura getItemFatura() {
		
		if (itemFaturas != null && Hibernate.isInitialized(itemFaturas)
				&& itemFaturas.size() > 0) {
			return (ItemFatura)this.itemFaturas.get(0);			
		} else {
			return null;
		}		
	}	

	public void setItemFaturas(List itemFaturas) {
		this.itemFaturas = itemFaturas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Desconto.class) 
	public List getDescontos() {
		return this.descontos;
	}	

	public void setDescontos(List descontos) {
		this.descontos = descontos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.AgendaTransferencia.class) 
	public List getAgendaTransferencias() {
		return this.agendaTransferencias;
	}

	public void setAgendaTransferencias(List agendaTransferencias) {
		this.agendaTransferencias = agendaTransferencias;
	}

	public List getItemBaixaDevMercs() {
		return itemBaixaDevMercs;
	}

	public void setItemBaixaDevMercs(List itemBaixaDevMercs) {
		this.itemBaixaDevMercs = itemBaixaDevMercs;
	}
	
	public Desconto getDesconto() {
		if (this.descontos != null && !this.descontos.isEmpty()){
			return (Desconto)this.descontos.get(0);
		} else {
			return null;
		}
	}	

	public String toString() {
		return new ToStringBuilder(this).append("idDevedorDocServFat",
				getIdDevedorDocServFat()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DevedorDocServFat))
			return false;
		DevedorDocServFat castOther = (DevedorDocServFat) other;
		return new EqualsBuilder().append(this.getIdDevedorDocServFat(),
				castOther.getIdDevedorDocServFat()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdDevedorDocServFat())
			.toHashCode();
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}
}
