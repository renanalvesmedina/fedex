package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;

/** @author LMS Custom Hibernate CodeGenerator */
public class AjusteTarifa implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idAjusteTarifa;

	/** persistent field (DM_ACRESCIMO_DESCONTO) */
	private DomainValue tpAjusteFretePeso;

	/** persistent field (DM_ACRESCIMO_DESCONTO) */
	private DomainValue tpAjusteFreteValor;

	/** persistent field (DM_TIPO_VALOR) */
	private DomainValue tpValorFretePreso;
	
	/** persistent field */
	private BigDecimal vlFretePeso;

	/** persistent field */
	private BigDecimal vlFreteValor;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfDestino;

	/** persistent field */
	private UnidadeFederativa unidadeFederativaByIdUfOrigem;

	/** persistent field */
	private Municipio municipioByIdMunicipioOrigem;

	/** persistent field */
	private Municipio municipioByIdMunicipioDestino;

	/** persistent field */
	private Filial filialByIdFilialDestino;

	/** persistent field */
	private Filial filialByIdFilialOrigem;

	/** persistent field */
	private TabelaPreco tabelaPrecoByIdTabelaPreco;

	public Long getIdAjusteTarifa() {
		return idAjusteTarifa;
	}

	public void setIdAjusteTarifa(Long idAjusteTarifa) {
		this.idAjusteTarifa = idAjusteTarifa;
	}

	public DomainValue getTpAjusteFretePeso() {
		return tpAjusteFretePeso;
	}

	public void setTpAjusteFretePeso(DomainValue tpAjusteFretePeso) {
		this.tpAjusteFretePeso = tpAjusteFretePeso;
	}

	public DomainValue getTpAjusteFreteValor() {
		return tpAjusteFreteValor;
	}

	public void setTpAjusteFreteValor(DomainValue tpAjusteFreteValor) {
		this.tpAjusteFreteValor = tpAjusteFreteValor;
	}

	public DomainValue getTpValorFretePreso() {
		return tpValorFretePreso;
	}

	public void setTpValorFretePreso(DomainValue tpValorFretePreso) {
		this.tpValorFretePreso = tpValorFretePreso;
	}

	public BigDecimal getVlFretePeso() {
		return vlFretePeso;
	}

	public void setVlFretePeso(BigDecimal vlFretePeso) {
		this.vlFretePeso = vlFretePeso;
	}

	public BigDecimal getVlFreteValor() {
		return vlFreteValor;
	}

	public void setVlFreteValor(BigDecimal vlFreteValor) {
		this.vlFreteValor = vlFreteValor;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfDestino() {
		return this.unidadeFederativaByIdUfDestino;
	}

	public void setUnidadeFederativaByIdUfDestino(
			UnidadeFederativa unidadeFederativaByIdUfDestino) {
		this.unidadeFederativaByIdUfDestino = unidadeFederativaByIdUfDestino;
	}

	public UnidadeFederativa getUnidadeFederativaByIdUfOrigem() {
		return this.unidadeFederativaByIdUfOrigem;
	}

	public void setUnidadeFederativaByIdUfOrigem(
			UnidadeFederativa unidadeFederativaByIdUfOrigem) {
		this.unidadeFederativaByIdUfOrigem = unidadeFederativaByIdUfOrigem;
	}

	public Municipio getMunicipioByIdMunicipioOrigem() {
		return this.municipioByIdMunicipioOrigem;
	}

	public void setMunicipioByIdMunicipioOrigem(
			Municipio municipioByIdMunicipioOrigem) {
		this.municipioByIdMunicipioOrigem = municipioByIdMunicipioOrigem;
	}

	public Municipio getMunicipioByIdMunicipioDestino() {
		return this.municipioByIdMunicipioDestino;
	}

	public void setMunicipioByIdMunicipioDestino(
			Municipio municipioByIdMunicipioDestino) {
		this.municipioByIdMunicipioDestino = municipioByIdMunicipioDestino;
	}

	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public void setTabelaPrecoByIdTabelaPreco(
			TabelaPreco tabelaPrecoByIdTabelaPreco) {
		this.tabelaPrecoByIdTabelaPreco = tabelaPrecoByIdTabelaPreco;
	}

	public TabelaPreco getTabelaPrecoByIdTabelaPreco() {
		return tabelaPrecoByIdTabelaPreco;
	}

}
