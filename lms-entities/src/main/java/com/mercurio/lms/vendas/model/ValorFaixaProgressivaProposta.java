package com.mercurio.lms.vendas.model;

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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import com.mercurio.adsm.framework.model.DomainValue;

@Entity
@Table(name = "VL_FAIXA_PROGR_PROPOSTA")
@SequenceGenerator(name = "VL_FAIXA_PROGR_PROPOSTA_SEQ", sequenceName = "VL_FAIXA_PROGR_PROPOSTA_SQ", allocationSize = 1)
public class ValorFaixaProgressivaProposta implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID_VL_FAIXA_PROGR_PROPOSTA", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VL_FAIXA_PROGR_PROPOSTA_SEQ")
	private Long idValorFaixaProgressivaProposta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DESTINO_PROPOSTA", nullable = true)
	private DestinoProposta destinoProposta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_FAIXA_PROGRESSIVA_PROPOSTA", nullable = false)
	private FaixaProgressivaProposta faixaProgressivaProposta;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PARAMETRO_CLIENTE", nullable = true)
	private ParametroCliente parametroCliente;

	@Column(name = "PC_VARIACAO", nullable = true)
	private BigDecimal pcVariacao;

	@Column(name = "TP_INDICADOR", length = 1, nullable = true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType", parameters = { @Parameter(name = "domainName", value = "DM_INDICADOR_PARAMETRO_CLIENTE") })
	private DomainValue tpIndicador;

	@Column(name = "VALOR_FIXO", nullable = false)
	private BigDecimal vlFixo;

	public String toString() {
		return new ToStringBuilder(this).append("idValorFaixaProgressivaProposta", getIdValorFaixaProgressivaProposta()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorFaixaProgressivaProposta))
			return false;
		ValorFaixaProgressivaProposta castOther = (ValorFaixaProgressivaProposta) other;
		return new EqualsBuilder().append(this.getIdValorFaixaProgressivaProposta(), castOther.getIdValorFaixaProgressivaProposta()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdValorFaixaProgressivaProposta()).toHashCode();
	}

	public Long getIdValorFaixaProgressivaProposta() {
		return idValorFaixaProgressivaProposta;
	}

	public void setIdValorFaixaProgressivaProposta(Long idValorFaixaProgressivaProposta) {
		this.idValorFaixaProgressivaProposta = idValorFaixaProgressivaProposta;
	}

	public DestinoProposta getDestinoProposta() {
		return destinoProposta;
	}

	public void setDestinoProposta(DestinoProposta destinoProposta) {
		this.destinoProposta = destinoProposta;
	}

	public FaixaProgressivaProposta getFaixaProgressivaProposta() {
		return faixaProgressivaProposta;
	}

	public void setFaixaProgressivaProposta(FaixaProgressivaProposta faixaProgressivaProposta) {
		this.faixaProgressivaProposta = faixaProgressivaProposta;
	}

	public ParametroCliente getParametroCliente() {
		return parametroCliente;
	}

	public void setParametroCliente(ParametroCliente parametroCliente) {
		this.parametroCliente = parametroCliente;
	}

	public BigDecimal getPcVariacao() {
		return pcVariacao;
	}

	public void setPcVariacao(BigDecimal pcVariacao) {
		this.pcVariacao = pcVariacao;
	}

	public DomainValue getTpIndicador() {
		return tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public BigDecimal getVlFixo() {
		return vlFixo;
	}

	public void setVlFixo(BigDecimal vlFixo) {
		this.vlFixo = vlFixo;
	}

}
