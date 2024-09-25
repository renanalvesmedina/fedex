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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;

@Entity
@Table(name = "MARKUP")
@SequenceGenerator(name = "MARKUP_SEQ", sequenceName = "MARKUP_SQ")
public class Markup implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MARKUP_SEQ")
	@Column(name = "ID_MARKUP", nullable = false)
	private Long idMarkup;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_PRECO", nullable = false)	
	private TabelaPreco tabelaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PARCELA_PRECO", nullable = true)
	private ParcelaPreco parcelaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ROTA_PRECO", nullable = true)
	private RotaPreco rotaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TARIFA_PRECO", nullable = true)
	private TarifaPreco tarifaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FAIXA_PROGRESSIVA", nullable = true)
	private FaixaProgressiva faixaProgressiva;
	
	@Column(name = "VL_MARKUP", nullable = false)
	private BigDecimal vlMarkup;
	
	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_FINAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	public Markup() {}
	
	public Markup(Long id) {
		this.idMarkup = id;
	}
	
	public Long getIdMarkup() {
		return idMarkup;
	}

	public void setIdMarkup(Long idMarkup) {
		this.idMarkup = idMarkup;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
	
	public Long getIdTabela() {
		if (this.tabelaPreco == null) {
			return null;
		}
		return this.tabelaPreco.getIdTabelaPreco();
		
	}

	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public RotaPreco getRotaPreco() {
		return rotaPreco;
	}

	public void setRotaPreco(RotaPreco rotaPreco) {
		this.rotaPreco = rotaPreco;
	}

	public TarifaPreco getTarifaPreco() {
		return tarifaPreco;
	}

	public void setTarifaPreco(TarifaPreco tarifaPreco) {
		this.tarifaPreco = tarifaPreco;
	}

	public FaixaProgressiva getFaixaProgressiva() {
		return faixaProgressiva;
	}

	public void setFaixaProgressiva(FaixaProgressiva faixaProgressiva) {
		this.faixaProgressiva = faixaProgressiva;
	}
	
	public BigDecimal getVlMarkup() {
		return vlMarkup;
	}

	public void setVlMarkup(BigDecimal vlMarkup) {
		this.vlMarkup = vlMarkup;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	
	public Long getIdParcelaPreco() {
		if (this.parcelaPreco == null) {
			return null;
		}
		return this.parcelaPreco.getIdParcelaPreco();
	}
	
	public String getNomeParcelaPreco() {
		if (this.parcelaPreco == null) {
			return null;
		}
		VarcharI18n nomeParcelaPreco = this.parcelaPreco.getNmParcelaPreco();
		if (nomeParcelaPreco == null) {
			return null;
		}
		return nomeParcelaPreco.getValue();
	}
	
	public String getTipoPrecificacaoParcela() {
		if (this.parcelaPreco == null) {
			return null;
		}
		DomainValue tipoPrecificacao = this.parcelaPreco.getTpPrecificacao();
		if (tipoPrecificacao == null) {
			return null;
		}
		return tipoPrecificacao.getValue();
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Markup))
			return false;
		Markup castOther = (Markup) other;
		return new EqualsBuilder().append(this.getIdMarkup(), castOther.getIdMarkup()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMarkup()).toHashCode();
    }

}
