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
@Table(name = "TABELA_FC_FAIXA_PESO")
@SequenceGenerator(name = "TABELA_FC_FAIXA_PESO_SQ", sequenceName = "TABELA_FC_FAIXA_PESO_SQ", allocationSize = 1)
public class TabelaFcFaixaPeso implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TABELA_FC_FAIXA_PESO_SQ")
	@Column(name = "ID_TABELA_FC_FAIXA_PESO", nullable = false)
	private Long idTabelaFcFaixaPeso;

	@ManyToOne
	@JoinColumn(name = "ID_TABELA_FC_VALORES")
	private TabelaFcValores tabelaFcValores;

	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_FATOR_FAIXA_PESO") })
	@Column(name = "TP_FATOR", length = 10)
	private DomainValue tpFator;

	@Column(name = "PS_INICIAL")
	private BigDecimal psInicial;

	@Column(name = "PS_FINAL")
	private BigDecimal psFinal;

	@Column(name = "VL_VALOR")
	private BigDecimal vlValor;

	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_FATOR_FAIXA_PESO") })
	@Column(name = "TP_FATOR_2", length = 10 )
	private DomainValue tpFatorSegundo;

	@Column(name = "VL_VALOR_2")
	private BigDecimal vlValorSegundo;

	@Column(name = "BL_CALCULO_FAIXA_UNICA", length = 1)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blCalculoFaixaUnica;

	public Long getIdTabelaFcFaixaPeso() {
		return this.idTabelaFcFaixaPeso;
	}

	public void setIdTabelaFcFaixaPeso(Long idTabelaFcFaixaPeso) {
		this.idTabelaFcFaixaPeso = idTabelaFcFaixaPeso;
	}

	public TabelaFcValores getTabelaFcValores() {
		return this.tabelaFcValores;
	}

	public void setTabelaFcValores(TabelaFcValores tabelaFcValores) {
		this.tabelaFcValores = tabelaFcValores;
	}

	public DomainValue getTpFator() {
		return this.tpFator;
	}

	public void setTpFator(DomainValue tpFator) {
		this.tpFator = tpFator;
	}

	public BigDecimal getPsInicial() {
		return this.psInicial;
	}

	public void setPsInicial(BigDecimal psInicial) {
		this.psInicial = psInicial;
	}

	public BigDecimal getPsFinal() {
		return this.psFinal;
	}

	public void setPsFinal(BigDecimal psFinal) {
		this.psFinal = psFinal;
	}

	public BigDecimal getVlValor() {
		return this.vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
	}

	public DomainValue getTpFatorSegundo() {
		return tpFatorSegundo;
	}

	public void setTpFatorSegundo(DomainValue tpFatorSegundo) {
		this.tpFatorSegundo = tpFatorSegundo;
	}

	public BigDecimal getVlValorSegundo() {
		return vlValorSegundo;
	}

	public Boolean getBlCalculoFaixaUnica() {
		return blCalculoFaixaUnica;
	}

	public void setBlCalculoFaixaUnica(Boolean blCalculoFaixaUnica) {
		this.blCalculoFaixaUnica = blCalculoFaixaUnica;
	}

	public void setVlValorSegundo(BigDecimal vlValorSegundo) {
		this.vlValorSegundo = vlValorSegundo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((idTabelaFcFaixaPeso == null) ? 0
						: idTabelaFcFaixaPeso.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TabelaFcFaixaPeso)) {
			return false;
		}

		TabelaFcFaixaPeso castOther = (TabelaFcFaixaPeso) obj;

		return new EqualsBuilder().append(this.getIdTabelaFcFaixaPeso(),
				castOther.getIdTabelaFcFaixaPeso()).isEquals();
	}

}
