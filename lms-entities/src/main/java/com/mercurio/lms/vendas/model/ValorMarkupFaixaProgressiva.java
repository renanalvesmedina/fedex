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
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;

@Entity
@Table(name = "VL_MARKUP_FAIXA_PROGRESSIVA")
@SequenceGenerator(name = "VL_MARKUP_FAIXA_PROGRESSIVA_SEQ", sequenceName = "VL_MARKUP_FAIXA_PROGRESSIVA_SQ")
public class ValorMarkupFaixaProgressiva implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VL_MARKUP_FAIXA_PROGRESSIVA_SEQ")
	@Column(name = "ID_VL_MARKUP_FX_PROGRESSIVA", nullable = false)
	private Long idValorMarkupFaixaProgressiva;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_MARKUP_FX_PROGRESSIVA", nullable = false)
	private MarkupFaixaProgressiva markupFaixaProgressiva;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_FAIXA_PROGRESSIVA",  nullable = false)
	private FaixaProgressiva faixaProgressiva;
	
	@Column(name = "VL_FIXO", nullable = false)
	private BigDecimal vlMarkup;
	
	public ValorMarkupFaixaProgressiva() {
		
	}

	public Long getIdValorMarkupFaixaProgressiva() {
		return idValorMarkupFaixaProgressiva;
	}

	public void setIdValorMarkupFaixaProgressiva(Long idValorMarkupFaixaProgressiva) {
		this.idValorMarkupFaixaProgressiva = idValorMarkupFaixaProgressiva;
	}

	public MarkupFaixaProgressiva getMarkupFaixaProgressiva() {
		return markupFaixaProgressiva;
	}

	public void setMarkupFaixaProgressiva(
			MarkupFaixaProgressiva markupFaixaProgressiva) {
		this.markupFaixaProgressiva = markupFaixaProgressiva;
	}

	public BigDecimal getVlMarkup() {
		return vlMarkup;
	}

	public void setVlMarkup(BigDecimal vlMarkup) {
		this.vlMarkup = vlMarkup;
	}
	
	public Long getIdParcelaPreco() {
		if (faixaProgressiva == null) {
			return null;
		}
		return faixaProgressiva.getIdParcelaPreco();
	}

	public String getNmParcelaPreco() {
		if (faixaProgressiva == null) {
			return null;
		}
		return faixaProgressiva.getNmParcelaPreco();
	}
	
	public YearMonthDay getDtVigenciaInicial() {
		if (markupFaixaProgressiva == null) {
			return null;
		}
		return markupFaixaProgressiva.getDtVigenciaInicial();
	}
	
	public YearMonthDay getDtVigenciaFinal() {
		if (markupFaixaProgressiva == null) {
			return null;
		}
		return markupFaixaProgressiva.getDtVigenciaFinal();
	}

	public Long getIdFaixaProgressiva() {
		FaixaProgressiva faixa = this.getFaixaProgressiva();
		if (faixa == null) {
			return null;
		}
		return faixa.getIdFaixaProgressiva();
	}

	public BigDecimal getVlFaixaProgressiva() {
		FaixaProgressiva faixa = this.getFaixaProgressiva();
		if (faixa == null) {
			return null;
		}
		return faixa.getVlFaixaProgressiva();
	}

	public TarifaPreco getTarifaPreco() {
		if (markupFaixaProgressiva == null) {
			return null;
		}
		return markupFaixaProgressiva.getTarifaPreco();
	}

	public RotaPreco getRotaPreco() {
		if (markupFaixaProgressiva == null) {
			return null;
		}
		return markupFaixaProgressiva.getRotaPreco();
	}

	public Long getIdMarkupFaixaProgressiva() {
		if (markupFaixaProgressiva == null) {
			return null;
		}
		return markupFaixaProgressiva.getIdMarkupFaixaProgressiva();
	}
	
	public FaixaProgressiva getFaixaProgressiva() {
		return faixaProgressiva;
	}

	public void setFaixaProgressiva(FaixaProgressiva faixaProgressiva) {
		this.faixaProgressiva = faixaProgressiva;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ValorMarkupFaixaProgressiva))
			return false;
		ValorMarkupFaixaProgressiva castOther = (ValorMarkupFaixaProgressiva) other;
		return new EqualsBuilder().append(this.getIdValorMarkupFaixaProgressiva(), castOther.getIdValorMarkupFaixaProgressiva()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdValorMarkupFaixaProgressiva()).toHashCode();
    }
	
}
