package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;

@Entity
@Table(name = "MARKUP_FX_PROGRESSIVA")
@SequenceGenerator(name = "MARKUP_FX_PROGRESSIVA_SEQ", sequenceName = "MARKUP_FX_PROGRESSIVA_SQ")
public class MarkupFaixaProgressiva implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MARKUP_FX_PROGRESSIVA_SEQ")
	@Column(name = "ID_MARKUP_FX_PROGRESSIVA", nullable = false)
	private Long idMarkupFaixaProgressiva;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_ROTA_PRECO", nullable = true)
	private RotaPreco rotaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TARIFA_PRECO", nullable = true)
	private TarifaPreco tarifaPreco;
	
	@Column(name = "DT_VIGENCIA_INI", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_FIM", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@OneToMany(mappedBy="markupFaixaProgressiva",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	private List<ValorMarkupFaixaProgressiva> valoresMarkupFaixaProgressiva;
	
	public MarkupFaixaProgressiva() {
	}

	public Long getIdMarkupFaixaProgressiva() {
		return idMarkupFaixaProgressiva;
	}

	public void setIdMarkupFaixaProgressiva(Long idMarkupFaixaProgressiva) {
		this.idMarkupFaixaProgressiva = idMarkupFaixaProgressiva;
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

	public List<ValorMarkupFaixaProgressiva> getValoresMarkupFaixaProgressiva() {
		return valoresMarkupFaixaProgressiva;
	}

	public void setValoresMarkupFaixaProgressiva(List<ValorMarkupFaixaProgressiva> valoresMarkupFaixaProgressiva) {
		this.valoresMarkupFaixaProgressiva = valoresMarkupFaixaProgressiva;
	}

	public void incluiValor(ValorMarkupFaixaProgressiva valorMarkupFaixaProgressiva) {
		if (valorMarkupFaixaProgressiva == null) {
			return;
		}
		if (this.valoresMarkupFaixaProgressiva == null) {
			this.valoresMarkupFaixaProgressiva = new ArrayList<ValorMarkupFaixaProgressiva>();
		}
		this.valoresMarkupFaixaProgressiva.add(valorMarkupFaixaProgressiva);
	}
	
}
