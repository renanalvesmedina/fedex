package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class TabelaPrecoParcela implements Serializable {

	private static final long serialVersionUID = 1L;
	/** identifier field */
	private Long idTabelaPrecoParcela;
	/** persistent field */
	private ParcelaPreco parcelaPreco;
	/** persistent field */
	private TabelaPreco tabelaPreco;
	/** persistent field */
	private Generalidade generalidade;
	/** persistent field */
	private ValorTaxa valorTaxa;
	/** persistent field */
	private ValorServicoAdicional valorServicoAdicional;

	private List<PrecoFrete> precoFretes;
	private List<FaixaProgressiva> faixaProgressivas;

	public Long getIdTabelaPrecoParcela() {
		return this.idTabelaPrecoParcela;
	}

	public void setIdTabelaPrecoParcela(Long idTabelaPrecoParcela) {
		this.idTabelaPrecoParcela = idTabelaPrecoParcela;
	}

	public ParcelaPreco getParcelaPreco() {
		return this.parcelaPreco;
	}

	public void setParcelaPreco(ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public TabelaPreco getTabelaPreco() {
		return this.tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Generalidade getGeneralidade() {
		return generalidade;
	}

	public void setGeneralidade(Generalidade generalidade) {
		this.generalidade = generalidade;
	}

	public ValorTaxa getValorTaxa() {
		return valorTaxa;
	}

	public void setValorTaxa(ValorTaxa valorTaxa) {
		this.valorTaxa = valorTaxa;
	}

	public ValorServicoAdicional getValorServicoAdicional() {
		return valorServicoAdicional;
	}

	public void setValorServicoAdicional(
			ValorServicoAdicional valorServicoAdicional) {
		this.valorServicoAdicional = valorServicoAdicional;
	}

	@ParametrizedAttribute(type = PrecoFrete.class)
	public List<PrecoFrete> getPrecoFretes() {
		return precoFretes;
	}

	public void setPrecoFretes(List<PrecoFrete> precoFretes) {
		this.precoFretes = precoFretes;
	}

	@ParametrizedAttribute(type = FaixaProgressiva.class)
	public List<FaixaProgressiva> getFaixaProgressivas() {
		return faixaProgressivas;
	}

	public void setFaixaProgressivas(List<FaixaProgressiva> faixaProgressivas) {
		this.faixaProgressivas = faixaProgressivas;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idTabelaPrecoParcela",
				getIdTabelaPrecoParcela()).toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TabelaPrecoParcela))
			return false;
		TabelaPrecoParcela castOther = (TabelaPrecoParcela) other;
		return new EqualsBuilder().append(this.getIdTabelaPrecoParcela(),
				castOther.getIdTabelaPrecoParcela()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdTabelaPrecoParcela())
			.toHashCode();
	}

	public void incluiPrecoFrete(PrecoFrete precoFrete){
		if (precoFrete == null) {
			return;
		}
		if (this.precoFretes == null) {
			this.precoFretes = new ArrayList<PrecoFrete>();
		}
		this.precoFretes.add(precoFrete);
	}

	public void incluiFaixasProgressiva(List<FaixaProgressiva> faixasProgressiva){
		if (CollectionUtils.isEmpty(faixasProgressiva)) {
			return;
		}
		if (this.faixaProgressivas == null) {
			this.faixaProgressivas = new ArrayList<FaixaProgressiva>();
		}
		this.faixaProgressivas.addAll(faixasProgressiva);
	}

	public Long getIdParcelaPreco() {
		if (parcelaPreco == null) {
			return null;
		}
		return parcelaPreco.getIdParcelaPreco();
	}

	public String getNmarcelaPreco() {
		if (parcelaPreco != null && parcelaPreco.getNmParcelaPreco() != null) {
			return parcelaPreco.getNmParcelaPreco().getValue();
		}
		return null;
	}
	
}
