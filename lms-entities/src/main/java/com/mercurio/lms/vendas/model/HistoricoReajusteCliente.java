package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;

@Entity
@Table(name = "HISTORICO_REAJUSTE_CLIENTE")
@SequenceGenerator(name = "SQ_HISTORICO_REAJUSTE_CLIENTE", sequenceName = "HISTORICO_REAJUSTE_CLIENTE_SQ")
public class HistoricoReajusteCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idHistoricoReajusteCliente;
	private TabelaDivisaoCliente tabelaDivisaoCliente;
	private TabelaPreco tabelaPrecoAnterior;
	private TabelaPreco tabelaPrecoNova;
	private DomainValue tpFormaReajuste;
	private YearMonthDay dtReajuste;
	private BigDecimal pcReajuste;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_HISTORICO_REAJUSTE_CLIENTE")
	@Column(name = "ID_HISTORICO_REAJUSTE_CLIENTE", nullable = false)
	public Long getIdHistoricoReajusteCliente() {
		return this.idHistoricoReajusteCliente;
	}

	public void setIdHistoricoReajusteCliente(Long idHistoricoReajusteCliente) {
		this.idHistoricoReajusteCliente = idHistoricoReajusteCliente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_DIVISAO_CLIENTE", nullable = false)
	public TabelaDivisaoCliente getTabelaDivisaoCliente() {
		return tabelaDivisaoCliente;
	}

	public void setTabelaDivisaoCliente(
			TabelaDivisaoCliente tabelaDivisaoCliente) {
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_PRECO_ANTERIOR", nullable = false)
	public TabelaPreco getTabelaPrecoAnterior() {
		return tabelaPrecoAnterior;
	}

	public void setTabelaPrecoAnterior(TabelaPreco tabelaPrecoAnterior) {
		this.tabelaPrecoAnterior = tabelaPrecoAnterior;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_PRECO_NOVA", nullable = false)
	public TabelaPreco getTabelaPrecoNova() {
		return tabelaPrecoNova;
	}

	public void setTabelaPrecoNova(TabelaPreco tabelaPrecoNova) {
		this.tabelaPrecoNova = tabelaPrecoNova;
	}

	@Column(name = "TP_FORMA_REAJUSTE", length = 1, nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_FORMA_REAJUSTE") })
	public DomainValue getTpFormaReajuste() {
		return tpFormaReajuste;
	}

	public void setTpFormaReajuste(DomainValue tpFormaReajuste) {
		this.tpFormaReajuste = tpFormaReajuste;
	}

	@Column(name = "DT_REAJUSTE", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	public YearMonthDay getDtReajuste() {
		return dtReajuste;
	}

	public void setDtReajuste(YearMonthDay dtReajuste) {
		this.dtReajuste = dtReajuste;
	}

	@Column(name = "PC_REAJUSTE", nullable = false)
	public BigDecimal getPcReajuste() {
		return pcReajuste;
	}

	public void setPcReajuste(BigDecimal pcReajuste) {
		this.pcReajuste = pcReajuste;
	}
}