package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.Vigencia;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class TabelaPreco implements Serializable, Vigencia {
	private static final long serialVersionUID = 2L;

	/** identifier field */
	private Long idTabelaPreco;

	/** persistent field */
	private YearMonthDay dtGeracao;

	/** persistent field */
	private Boolean blEfetivada;

	/** nullable persistent field */
	private YearMonthDay dtVigenciaInicial;

	/** nullable persistent field */
	private YearMonthDay dtVigenciaFinal;

	/** nullable persistent field */
	private BigDecimal pcReajuste;

	/** nullable persistent field */
	private DomainValue tpTarifaReajuste;

	/** nullable persistent field */
	private DomainValue tpCategoria;

	/** nullable persistent field */
	private DomainValue tpServico;

	/** nullable persistent field */
	private BigDecimal psMinimo;

	/** nullable persistent field */
	private DomainValue tpCalculoFretePeso;

	/** nullable persistent field */
	private DomainValue tpCalculoPedagio;

	/** nullable persistent field */
	private String dsDescricao;

	private String obTabelaPreco;

	private String tabelaPrecoStringDescricao;

	/** persistent field */
	private Moeda moeda;

	/** persistent field */
	private TipoTabelaPreco tipoTabelaPreco;

	/** persistent field */
	private Usuario usuario;

	/** persistent field */
	private TabelaPreco tabelaPreco;
	
	/** persistent field */
	private TabelaPreco tabelaPrecoCustoTnt;

	/** persistent field */
	private SubtipoTabelaPreco subtipoTabelaPreco;

	/** persistent field */
	private List<TabelaPrecoParcela> tabelaPrecoParcelas;

	/** persistent field */
	private List<TabelaPreco> tabelaPrecos;

	/** nullable persistent field */
	private BigDecimal pcDescontoFreteMinimo;

	/** persistent field */
    private Pendencia pendencia;
    
    /** persistent field */
    private Pendencia pendenciaEfetivacao;
    
    /** persistent field */
    private Pendencia pendenciaDesefetivacao;

    private Boolean blImprimeTabela;

	private Boolean blIcmsDestacado;
    
	public BigDecimal getPcDescontoFreteMinimo() {
		return pcDescontoFreteMinimo;
	}

	public void setPcDescontoFreteMinimo(BigDecimal pcDescontoFreteMinimo) {
		this.pcDescontoFreteMinimo = pcDescontoFreteMinimo;
	}

	public Long getIdTabelaPreco() {
		return this.idTabelaPreco;
	}

	public void setIdTabelaPreco(Long idTabelaPreco) {
		this.idTabelaPreco = idTabelaPreco;
	}

	public YearMonthDay getDtGeracao() {
		return this.dtGeracao;
	}

	public void setDtGeracao(YearMonthDay dtGeracao) {
		this.dtGeracao = dtGeracao;
	}

	public Boolean getBlEfetivada() {
		return blEfetivada;
	}

	public void setBlEfetivada(Boolean blEfetivada) {
		this.blEfetivada = blEfetivada;
	}

	@Override
	public YearMonthDay getDtVigenciaInicial() {
		return this.dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	@Override
	public YearMonthDay getDtVigenciaFinal() {
		return this.dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public BigDecimal getPcReajuste() {
		return this.pcReajuste;
	}

	public void setPcReajuste(BigDecimal pcReajuste) {
		this.pcReajuste = pcReajuste;
	}

	public DomainValue getTpTarifaReajuste() {
		return tpTarifaReajuste;
	}

	public void setTpTarifaReajuste(DomainValue tpTarifaReajuste) {
		this.tpTarifaReajuste = tpTarifaReajuste;
	}

	public BigDecimal getPsMinimo() {
		return this.psMinimo;
	}

	public void setPsMinimo(BigDecimal psMinimo) {
		this.psMinimo = psMinimo;
	}

	public DomainValue getTpCalculoFretePeso() {
		return tpCalculoFretePeso;
	}

	public void setTpCalculoFretePeso(DomainValue tpCalculoFretePeso) {
		this.tpCalculoFretePeso = tpCalculoFretePeso;
	}

	public String getDsDescricao() {
		return this.dsDescricao;
	}

	public void setDsDescricao(String dsDescricao) {
		this.dsDescricao = dsDescricao;
	}

	public Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public TipoTabelaPreco getTipoTabelaPreco() {
		return this.tipoTabelaPreco;
	}

	public void setTipoTabelaPreco(TipoTabelaPreco tipoTabelaPreco) {
		this.tipoTabelaPreco = tipoTabelaPreco;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public TabelaPreco getTabelaPreco() {
		return this.tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public TabelaPreco getTabelaPrecoCustoTnt() {
		return tabelaPrecoCustoTnt;
	}

	public void setTabelaPrecoCustoTnt(TabelaPreco tabelaPrecoCustoTnt) {
		this.tabelaPrecoCustoTnt = tabelaPrecoCustoTnt;
	}

	public SubtipoTabelaPreco getSubtipoTabelaPreco() {
		return this.subtipoTabelaPreco;
	}

	public void setSubtipoTabelaPreco(SubtipoTabelaPreco subtipoTabelaPreco) {
		this.subtipoTabelaPreco = subtipoTabelaPreco;
	}

	@ParametrizedAttribute(type = TabelaPrecoParcela.class)
	public List<TabelaPrecoParcela> getTabelaPrecoParcelas() {
		return this.tabelaPrecoParcelas;
	}

	public void setTabelaPrecoParcelas(
			List<TabelaPrecoParcela> tabelaPrecoParcelas) {
		this.tabelaPrecoParcelas = tabelaPrecoParcelas;
	}

	@ParametrizedAttribute(type = TabelaPreco.class)
	public List<TabelaPreco> getTabelaPrecos() {
		return this.tabelaPrecos;
	}

	public void setTabelaPrecos(List<TabelaPreco> tabelaPrecos) {
		this.tabelaPrecos = tabelaPrecos;
	}

	public DomainValue getTpCategoria() {
		return tpCategoria;
	}

	public void setTpCategoria(DomainValue tpCategoria) {
		this.tpCategoria = tpCategoria;
	}

	public DomainValue getTpServico() {
		return tpServico;
	}

	public void setTpServico(DomainValue tpServico) {
		this.tpServico = tpServico;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idTabelaPreco",
				getIdTabelaPreco()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TabelaPreco))
			return false;
		TabelaPreco castOther = (TabelaPreco) other;
		return new EqualsBuilder().append(this.getIdTabelaPreco(),
				castOther.getIdTabelaPreco()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaPreco()).toHashCode();
	}

	public String getTabelaPrecoString() {
		StringBuffer sb = new StringBuffer();

		try {
			if (!Hibernate.isInitialized(tipoTabelaPreco)) {
				Hibernate.initialize(tipoTabelaPreco);
			}

			if (getTipoTabelaPreco() != null) {
				if (getTipoTabelaPreco().getTpTipoTabelaPreco() != null
						&& StringUtils.isNotBlank(getTipoTabelaPreco()
						.getTpTipoTabelaPreco().getValue())) {
					sb.append(getTipoTabelaPreco().getTpTipoTabelaPreco()
							.getValue().toString());
				}
				if (getTipoTabelaPreco().getNrVersao() != null) {
					sb.append(getTipoTabelaPreco().getNrVersao());
				}
			}
			if (getSubtipoTabelaPreco() != null
					&& StringUtils.isNotBlank(getSubtipoTabelaPreco()
					.getTpSubtipoTabelaPreco())) {
				sb.append("-");
				sb.append(getSubtipoTabelaPreco().getTpSubtipoTabelaPreco()
						.toString());
			}
		} catch (Exception e){
			return sb.toString();
		}
		return sb.toString();
	}
	
	public String getTabelaPrecoStringCompleto() {
		StringBuffer sb = new StringBuffer(getTabelaPrecoString());
		if (StringUtils.isNotBlank(getDsDescricao())) {
			sb.append(" ").append(getDsDescricao());
		}
		if (Hibernate.isInitialized(getTipoTabelaPreco()) 
				&& getTipoTabelaPreco() != null
				&& StringUtils.isNotBlank(getTipoTabelaPreco().getNomeServico())) {
				sb.append(" | ").append(getTipoTabelaPreco().getNomeServico());
		}
		return sb.toString();
	}

	/**
	 * @return Returns the tabelaPrecoStringDescricao.
	 */
	public String getTabelaPrecoStringDescricao() {
		return tabelaPrecoStringDescricao;
	}

	/**
	 * @param tabelaPrecoStringDescricao
	 *            The tabelaPrecoStringDescricao to set.
	 */
	public void setTabelaPrecoStringDescricao(String tabelaPrecoStringDescricao) {
		this.tabelaPrecoStringDescricao = tabelaPrecoStringDescricao;
	}

	public DomainValue getTpCalculoPedagio() {
		return tpCalculoPedagio;
	}

	public void setTpCalculoPedagio(DomainValue tpCalculoPedagio) {
		this.tpCalculoPedagio = tpCalculoPedagio;
	}

	public boolean isEfetivada() {
		return getBlEfetivada() == null ? false : getBlEfetivada();
	}

	public String getObTabelaPreco() {
		return obTabelaPreco;
	}

	public void setObTabelaPreco(String obTabelaPreco) {
		this.obTabelaPreco = obTabelaPreco;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public Pendencia getPendenciaEfetivacao() {
		return pendenciaEfetivacao;
	}

	public void setPendenciaEfetivacao(Pendencia pendenciaEfetivacao) {
		this.pendenciaEfetivacao = pendenciaEfetivacao;
	}

	public Pendencia getPendenciaDesefetivacao() {
		return pendenciaDesefetivacao;
	}

	public void setPendenciaDesefetivacao(Pendencia pendenciaDesefetivacao) {
		this.pendenciaDesefetivacao = pendenciaDesefetivacao;
	}
	
	public void incluiParcelas(List<TabelaPrecoParcela> entidades) {
		if ((Hibernate.isInitialized(tabelaPrecoParcelas) && CollectionUtils.isEmpty(tabelaPrecoParcelas))
				|| !Hibernate.isInitialized(tabelaPrecoParcelas)
				|| this.tabelaPrecoParcelas == null) {
			this.tabelaPrecoParcelas = new ArrayList<TabelaPrecoParcela>();
		}
		for (TabelaPrecoParcela tabelaPrecoParcela : entidades) {
			tabelaPrecoParcela.setTabelaPreco(this);
			this.tabelaPrecoParcelas.add(tabelaPrecoParcela);
		}
	}

	public void setBlImprimeTabela(Boolean blImprimeTabela) {
		this.blImprimeTabela = blImprimeTabela;
	}
	public Boolean getBlImprimeTabela() {
		return blImprimeTabela;
	}

	public Boolean getBlIcmsDestacado() {
		return blIcmsDestacado;
	}

	public void setBlIcmsDestacado(Boolean blIcmsDestacado) {
		this.blIcmsDestacado = blIcmsDestacado;
	}
}
