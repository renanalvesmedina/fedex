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

import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;
import com.mercurio.lms.entrega.model.Turno;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;

@Entity
@Table(name = "VL_MARKUP_PRECO_FRETE")
@SequenceGenerator(name = "VL_MARKUP_PRECO_FRETE_SEQ", sequenceName = "VL_MARKUP_PRECO_FRETE_SQ")
public class ValorMarkupPrecoFrete implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VL_MARKUP_PRECO_FRETE_SEQ")
	@Column(name = "ID_VL_MARKUP_PRECO_FRETE", nullable = false)
	private Long idValorMarkupPrecoFrete;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PRECO_FRETE", nullable = false)
	private PrecoFrete precoFrete;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ROTA_PRECO", nullable = true)
	private RotaPreco rotaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TARIFA_PRECO", nullable = true)
	private TarifaPreco tarifaPreco;
	
	@Column(name = "VL_FIXO", nullable = false)
	private BigDecimal vlMarkup;
	
	@Column(name = "DT_VIGENCIA_INI", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_FIM", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	public ValorMarkupPrecoFrete() {
		
	}

	public Long getIdValorMarkupPrecoFrete() {
		return idValorMarkupPrecoFrete;
	}

	public void setIdValorMarkupPrecoFrete(Long idValorMarkupPrecoFrete) {
		this.idValorMarkupPrecoFrete = idValorMarkupPrecoFrete;
	}

	public PrecoFrete getPrecoFrete() {
		return precoFrete;
	}

	public void setPrecoFrete(PrecoFrete precoFrete) {
		this.precoFrete = precoFrete;
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
		if (precoFrete == null) {
			return null;
		}
		return precoFrete.getIdParcelaPreco();
	}

	public String getNmParcelaPreco() {
		if (precoFrete == null) {
			return null;
		}
		return precoFrete.getNmParcelaPreco();
	}

	public Long getIdPrecoFrete() {
		if (precoFrete == null) {
			return null;
		}
		return precoFrete.getIdPrecoFrete();
	}
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorMarkupPrecoFrete))
			return false;
		ValorMarkupPrecoFrete castOther = (ValorMarkupPrecoFrete) other;
		return new EqualsBuilder().append(this.getIdValorMarkupPrecoFrete(), castOther.getIdValorMarkupPrecoFrete()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorMarkupPrecoFrete()).toHashCode();
    }
}
