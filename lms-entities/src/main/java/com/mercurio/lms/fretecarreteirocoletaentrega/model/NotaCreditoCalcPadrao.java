package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name = "NOTA_CREDITO_CALC_PADRAO")
@SequenceGenerator(name = "NOTA_CREDITO_CALC_PADRAO_SQ", sequenceName = "NOTA_CREDITO_CALC_PADRAO_SQ", allocationSize = 1)
public class NotaCreditoCalcPadrao implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_CREDITO_CALC_PADRAO_SQ")
	@Column(name = "ID_NOTA_CREDITO_CALC_PADRAO", nullable = false)
	private Long idNotaCreditoCalcPadrao;

	@ManyToOne
	@JoinColumn(name = "ID_NOTA_CREDITO")
	private NotaCredito notaCredito;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_FRETE_CARRETEIRO_CE")
	private TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_FC_VALORES")
	private TabelaFcValores tabelaFcValores;

	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_TP_VALOR_TABELA_PADRAO") })
	@Column(name = "TP_VALOR")
	private DomainValue tpValor;

	@Column(name = "QT_TOTAL")
	private BigDecimal qtTotal;

	@Column(name = "VL_TOTAL")
	private BigDecimal vlValor;

	public Long getIdNotaCreditoCalcPadrao() {
		return this.idNotaCreditoCalcPadrao;
	}

	public void setIdNotaCreditoCalcPadrao(Long idNotaCreditoCalcPadrao) {
		this.idNotaCreditoCalcPadrao = idNotaCreditoCalcPadrao;
	}

	public NotaCredito getNotaCredito() {
		return this.notaCredito;
	}

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public TabelaFreteCarreteiroCe getTabelaFreteCarreteiroCe() {
		return this.tabelaFreteCarreteiroCe;
	}

	public void setTabelaFreteCarreteiroCe(
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		this.tabelaFreteCarreteiroCe = tabelaFreteCarreteiroCe;
	}

	public TabelaFcValores getTabelaFcValores() {
		return this.tabelaFcValores;
	}

	public void setTabelaFcValores(TabelaFcValores tabelaFcValores) {
		this.tabelaFcValores = tabelaFcValores;
	}

	public DomainValue getTpValor() {
		return this.tpValor;
	}

	public void setTpValor(DomainValue tpValor) {
		this.tpValor = tpValor;
	}

	public BigDecimal getQtTotal() {
		return this.qtTotal;
	}

	public void setQtTotal(BigDecimal qtTotal) {
		this.qtTotal = qtTotal;
	}

	public BigDecimal getVlValor() {
		return this.vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idNotaCreditoCalcPadrao == null) ? 0
						: idNotaCreditoCalcPadrao.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof NotaCreditoCalcPadrao)) {
			return false;
		}

		NotaCreditoCalcPadrao castOther = (NotaCreditoCalcPadrao) obj;

		return new EqualsBuilder().append(this.getIdNotaCreditoCalcPadrao(),
				castOther.getIdNotaCreditoCalcPadrao()).isEquals();
	}

}