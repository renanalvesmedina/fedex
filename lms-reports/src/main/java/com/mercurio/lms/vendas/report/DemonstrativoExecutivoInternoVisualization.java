package com.mercurio.lms.vendas.report;

import java.io.Serializable;
import java.math.BigDecimal;

public class DemonstrativoExecutivoInternoVisualization implements Serializable {

	private static final long serialVersionUID = 1L;

	private String territorio;
	private String metaRodoviario;
	private String receitaRodoviario;
	private String percentualMetaRodoviario;
	private String metaAereo;
	private String receitaAereo;
	private String percentualMetaAereo;
	private String metaTri;
	private String receitaTri;
	private String percentualMetaTri;
	private String receitaLiquidaPaga;
	private String clienteNovoRodoviario;
	private String clienteNovoAereo;
	
	public DemonstrativoExecutivoInternoVisualization() {
		metaRodoviario = "0";
		receitaRodoviario = "0";
		metaAereo = "0";
		receitaAereo = "0";
		receitaLiquidaPaga = "0";
		clienteNovoRodoviario = "0";
		clienteNovoAereo = "0";
	}
	
	public DemonstrativoExecutivoInternoVisualization(String territorio,
			String metaRodoviario, String receitaRodoviario,
			String percentualMetaRodoviario, String metaAereo,
			String receitaAereo, String percentualMetaAereo, String metaTri,
			String receitaTri, String percentualMetaTri,
			String receitaLiquidaPaga, String clienteNovoRodoviario,
			String clienteNovoAereo) {
		
		this.territorio = territorio;
		this.metaRodoviario = metaRodoviario;
		this.receitaRodoviario = receitaRodoviario;
		this.percentualMetaRodoviario = percentualMetaRodoviario;
		this.metaAereo = metaAereo;
		this.receitaAereo = receitaAereo;
		this.percentualMetaAereo = percentualMetaAereo;
		this.metaTri = metaTri;
		this.receitaTri = receitaTri;
		this.percentualMetaTri = percentualMetaTri;
		this.receitaLiquidaPaga = receitaLiquidaPaga;
		this.clienteNovoRodoviario = clienteNovoRodoviario;
		this.clienteNovoAereo = clienteNovoAereo;
	}
	
	public String getTerritorio() {
		return territorio;
	}
	public void setTerritorio(String territorio) {
		this.territorio = territorio;
	}
	public String getMetaRodoviario() {
		return metaRodoviario;
	}
	public void setMetaRodoviario(String metaRodoviario) {
		this.metaRodoviario = metaRodoviario;
	}
	public String getReceitaRodoviario() {
		return receitaRodoviario;
	}
	public void setReceitaRodoviario(String receitaRodoviario) {
		this.receitaRodoviario = receitaRodoviario;
	}
	public String getPercentualMetaRodoviario() {
		return percentualMetaRodoviario;
	}
	public void setPercentualMetaRodoviario(String percentualMetaRodoviario) {
		this.percentualMetaRodoviario = percentualMetaRodoviario;
	}
	public String getMetaAereo() {
		return metaAereo;
	}
	public void setMetaAereo(String metaAereo) {
		this.metaAereo = metaAereo;
	}
	public String getReceitaAereo() {
		return receitaAereo;
	}
	public void setReceitaAereo(String receitaAereo) {
		this.receitaAereo = receitaAereo;
	}
	public String getPercentualMetaAereo() {
		return percentualMetaAereo;
	}
	public void setPercentualMetaAereo(String percentualMetaAereo) {
		this.percentualMetaAereo = percentualMetaAereo;
	}
	public String getMetaTri() {
		return metaTri;
	}
	public void setMetaTri(String metaTri) {
		this.metaTri = metaTri;
	}
	public String getReceitaTri() {
		return receitaTri;
	}
	public void setReceitaTri(String receitaTri) {
		this.receitaTri = receitaTri;
	}
	public String getPercentualMetaTri() {
		return percentualMetaTri;
	}
	public void setPercentualMetaTri(String percentualMetaTri) {
		this.percentualMetaTri = percentualMetaTri;
	}
	public String getReceitaLiquidaPaga() {
		return receitaLiquidaPaga;
	}
	public void setReceitaLiquidaPaga(String receitaLiquidaPaga) {
		this.receitaLiquidaPaga = receitaLiquidaPaga;
	}
	public String getClienteNovoRodoviario() {
		return clienteNovoRodoviario;
	}
	public void setClienteNovoRodoviario(String clienteNovoRodoviario) {
		this.clienteNovoRodoviario = clienteNovoRodoviario;
	}
	public String getClienteNovoAereo() {
		return clienteNovoAereo;
	}
	public void setClienteNovoAereo(String clienteNovoAereo) {
		this.clienteNovoAereo = clienteNovoAereo;
	}

	public void addMetaAereo(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.metaAereo = new BigDecimal(this.metaAereo).add(valueToAdd).toString();	
		}
	}
	
	public void addMetaRodoviario(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.metaRodoviario = new BigDecimal(this.metaRodoviario).add(valueToAdd).toString();	
		}
	}
	
	public void addReceitaAereo(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.receitaAereo = new BigDecimal(this.receitaAereo).add(valueToAdd).toString();	
		}
	}
	
	public void addReceitaRodoviario(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.receitaRodoviario = new BigDecimal(this.receitaRodoviario).add(valueToAdd).toString();	
		}
	}

	public void addReceitaLiquidaPaga(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.receitaLiquidaPaga = new BigDecimal(this.receitaLiquidaPaga).add(valueToAdd).toString();	
		}
	}

	public void addClienteNovoRodoviario(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.clienteNovoRodoviario = new BigDecimal(this.clienteNovoRodoviario).add(valueToAdd).toString();	
		}
	}

	public void addClienteNovoAereo(BigDecimal valueToAdd) {
		if (valueToAdd != null) {
			this.clienteNovoAereo = new BigDecimal(this.clienteNovoAereo).add(valueToAdd).toString();	
		}
	}

}
