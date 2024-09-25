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
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.annotations.Type;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.workflow.model.EventoWorkflow;

@Entity
@Table(name = "COMITE_NIVEL_MARKUP")
@SequenceGenerator(name = "COMITE_NIVEL_MARKUP_SEQ", sequenceName = "COMITE_NIVEL_MARKUP_SQ", allocationSize = 1)
public class ComiteNivelMarkup implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMITE_NIVEL_MARKUP_SEQ")
	@Column(name = "ID_COMITE_NIVEL_MARKUP", nullable = false)
	private Long idComiteNivelMarkup;
	
	@Column(name = "PC_VARIACAO", nullable = false)
	private BigDecimal pcVariacao;
	
	@Column(name = "DT_VIGENCIA_INICIAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaInicial;
	
	@Column(name = "DT_VIGENCIA_FINAL", nullable = false)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType")
	private YearMonthDay dtVigenciaFinal;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_PARCELA_PRECO", nullable = false)
	private ParcelaPreco parcelaPreco;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_EVENTO_WORKFLOW", nullable = false)
	private EventoWorkflow eventoWorkflow;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ID_TABELA_PRECO", nullable = true)	
	private TabelaPreco tabelaPreco;
	
	@Column(name = "BL_ISENTO", length = 1, nullable=true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blIsento;
	
	@Column(name = "BL_FATOR_CUBAGEM", length = 1, nullable=true)
	@Type(type = "com.mercurio.adsm.core.model.hibernate.SimNaoType")
	private Boolean blFatorCubagem;

	public Long getIdComiteNivelMarkup() {
		return idComiteNivelMarkup;
	}

	public void setIdComiteNivelMarkup(Long idComiteNivelMarkup) {
		this.idComiteNivelMarkup = idComiteNivelMarkup;
	}

	public BigDecimal getPcVariacao() {
		return pcVariacao;
	}

	public void setPcVariacao(BigDecimal pcVariacao) {
		this.pcVariacao = pcVariacao;
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

	public ParcelaPreco getParcelaPreco() {
		return parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public EventoWorkflow getEventoWorkflow() {
		return eventoWorkflow;
	}

	public void setEventoWorkflow(EventoWorkflow eventoWorkflow) {
		this.eventoWorkflow = eventoWorkflow;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!ComiteNivelMarkup.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		ComiteNivelMarkup item = (ComiteNivelMarkup) o;
		return new EqualsBuilder().append(this.parcelaPreco, item.parcelaPreco)
				.append(this.eventoWorkflow, item.eventoWorkflow)
				.append(this.dtVigenciaInicial, item.dtVigenciaInicial)
				.append(this.dtVigenciaFinal, item.dtVigenciaFinal)
			.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
			.append(this.parcelaPreco)
			.append(this.eventoWorkflow)
			.append(this.dtVigenciaInicial)
			.append(this.dtVigenciaFinal)
			.toHashCode();
	}
	
	/*------------------------*/
	public Long getIdTabelaPreco() {
		if (this.tabelaPreco == null) {
			return NumberUtils.LONG_ZERO;
		}
		Long idTabelaPreco = this.tabelaPreco.getIdTabelaPreco();
		return idTabelaPreco == null ? NumberUtils.LONG_ZERO : idTabelaPreco;
	}
	
	public Long getIdParcelaPreco() {
		if (this.parcelaPreco == null) {
			return NumberUtils.LONG_ZERO;
		}
		Long idParcelaPreco = this.parcelaPreco.getIdParcelaPreco();
		return idParcelaPreco == null ? NumberUtils.LONG_ZERO : idParcelaPreco;
	}
	
	public Long getIdComite() {
		if (this.eventoWorkflow == null) {
			return NumberUtils.LONG_ZERO;
		}
		Long idComite = this.eventoWorkflow.getIdEventoWorkflow();
		return idComite == null ? NumberUtils.LONG_ZERO : idComite;
	}

	public Boolean getBlIsento() {
		return blIsento;
	}

	public void setBlIsento(Boolean blIsento) {
		this.blIsento = blIsento;
	}

	public Boolean getBlFatorCubagem() {
		return blFatorCubagem;
	}

	public void setBlFatorCubagem(Boolean blFatorCubagem) {
		this.blFatorCubagem = blFatorCubagem;
	}
	
}
