package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.Collator;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.vendas.model.Cotacao;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServAdicionalDocServ implements Serializable,
		Comparable<ServAdicionalDocServ> {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idServAdicionalDocServ;

	/** nullable persistent field */
	private Integer qtDias;

	/** nullable persistent field */
	private Integer qtSegurancasAdicionais;

	/** nullable persistent field */
	private Integer qtColetas;

	/** nullable persistent field */
	private Integer qtPaletes;

	/** nullable persistent field */
	private Integer nrKmRodado;

	/** nullable persistent field */
	private BigDecimal vlMercadoria;
	
	/** nullable persistent field */
	private BigDecimal vlFrete;

	/** nullable persistent field */
	private Integer qtCheques;

	/** nullable persistent field */
	private YearMonthDay dtPrimeiroCheque;

	/** persistent field */
	private DoctoServico doctoServico;

	/** persistent field */
	private ServicoAdicional servicoAdicional;
	
	/** persistent field */
	private Cotacao cotacao;
	
	private transient String cdParcelaPreco;

	public ServAdicionalDocServ() {
	}

	public ServAdicionalDocServ(Long idServAdicionalDocServ, Integer qtDias, Integer qtSegurancasAdicionais, Integer qtColetas, Integer qtPaletes, Integer nrKmRodado, BigDecimal vlMercadoria, BigDecimal vlFrete, Integer qtCheques, YearMonthDay dtPrimeiroCheque, DoctoServico doctoServico, ServicoAdicional servicoAdicional, Cotacao cotacao, String cdParcelaPreco) {
		this.idServAdicionalDocServ = idServAdicionalDocServ;
		this.qtDias = qtDias;
		this.qtSegurancasAdicionais = qtSegurancasAdicionais;
		this.qtColetas = qtColetas;
		this.qtPaletes = qtPaletes;
		this.nrKmRodado = nrKmRodado;
		this.vlMercadoria = vlMercadoria;
		this.vlFrete = vlFrete;
		this.qtCheques = qtCheques;
		this.dtPrimeiroCheque = dtPrimeiroCheque;
		this.doctoServico = doctoServico;
		this.servicoAdicional = servicoAdicional;
		this.cotacao = cotacao;
		this.cdParcelaPreco = cdParcelaPreco;
	}

	public Long getIdServAdicionalDocServ() {
		return this.idServAdicionalDocServ;
	}

	public void setIdServAdicionalDocServ(Long idServAdicionalDocServ) {
		this.idServAdicionalDocServ = idServAdicionalDocServ;
	}

	public Integer getQtDias() {
		return this.qtDias;
	}

	public void setQtDias(Integer qtDias) {
		this.qtDias = qtDias;
	}

	public Integer getQtSegurancasAdicionais() {
		return this.qtSegurancasAdicionais;
	}

	public void setQtSegurancasAdicionais(Integer qtSegurancasAdicionais) {
		this.qtSegurancasAdicionais = qtSegurancasAdicionais;
	}

	public Integer getQtColetas() {
		return this.qtColetas;
	}

	public void setQtColetas(Integer qtColetas) {
		this.qtColetas = qtColetas;
	}

	public Integer getQtPaletes() {
		return this.qtPaletes;
	}

	public void setQtPaletes(Integer qtPaletes) {
		this.qtPaletes = qtPaletes;
	}

	public Integer getNrKmRodado() {
		return this.nrKmRodado;
	}

	public void setNrKmRodado(Integer nrKmRodado) {
		this.nrKmRodado = nrKmRodado;
	}

	public BigDecimal getVlMercadoria() {
		return this.vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public BigDecimal getVlFrete() {
		return vlFrete;
	}

	public void setVlFrete(BigDecimal vlFrete) {
		this.vlFrete = vlFrete;
	}

	public Integer getQtCheques() {
		return this.qtCheques;
	}

	public void setQtCheques(Integer qtCheques) {
		this.qtCheques = qtCheques;
	}

	public YearMonthDay getDtPrimeiroCheque() {
		return this.dtPrimeiroCheque;
	}

	public void setDtPrimeiroCheque(YearMonthDay dtPrimeiroCheque) {
		this.dtPrimeiroCheque = dtPrimeiroCheque;
	}

	public DoctoServico getDoctoServico() {
		return this.doctoServico;
	}

	public void setDoctoServico(DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public ServicoAdicional getServicoAdicional() {
		return this.servicoAdicional;
	}

	public void setServicoAdicional(ServicoAdicional servicoAdicional) {
		this.servicoAdicional = servicoAdicional;
	}
	
	public Cotacao getCotacao() {
		return cotacao;
	}

	public void setCotacao(Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idServAdicionalDocServ",
				getIdServAdicionalDocServ()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServAdicionalDocServ))
			return false;
		ServAdicionalDocServ castOther = (ServAdicionalDocServ) other;
		return new EqualsBuilder().append(this.getIdServAdicionalDocServ(),
				castOther.getIdServAdicionalDocServ()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdServAdicionalDocServ())
			.toHashCode();
	}
	
	public int compareTo(ServAdicionalDocServ o) {
		Collator collator = Collator.getInstance(LocaleContextHolder
				.getLocale());
		return collator.compare(this.getServicoAdicional()
				.getDsServicoAdicional().getValue(), o.getServicoAdicional()
				.getDsServicoAdicional().getValue());
	}

	public String getCdParcelaPreco() {
		return cdParcelaPreco;
	}

	public void setCdParcelaPreco(String cdParcelaPreco) {
		this.cdParcelaPreco = cdParcelaPreco;
	}
	
}
