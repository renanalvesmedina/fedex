package com.mercurio.lms.vendas.report;

import java.io.Serializable;
import java.util.Comparator;

public class DemonstrativoComissoesReportVisualization implements Serializable, Comparable<DemonstrativoComissoesReportVisualization> {

	private static final long serialVersionUID = 1L;

	private String cnpj;
	private String razaoSocial;
	private String territorio;
	private String clienteTerritorio;
	private String executivoTerritorio;
	private String emissao;
	private String cif;
	private String fob;
	private String cifFob;
	private String qtdCtrc;
	private String percentual;
	private String tipo;
	private String valor;
	private String cargoNome;
	private String percentualAtingimentoMeta;
	
	public DemonstrativoComissoesReportVisualization(String cnpj,
			String razaoSocial, String territorio, String clienteTerritorio,
			String executivoTerritorio, String emissao, String cif, String fob,
			String cifFob, String qtdCtrc, String percentual, String tipo,
			String valor, String cargoNome, String percentualAtingimentoMeta) {
		
		super();
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.territorio = territorio;
		this.clienteTerritorio = clienteTerritorio;
		this.executivoTerritorio = executivoTerritorio;
		this.emissao = emissao;
		this.cif = cif;
		this.fob = fob;
		this.cifFob = cifFob;
		this.qtdCtrc = qtdCtrc;
		this.percentual = percentual;
		this.tipo = tipo;
		this.valor = valor;
		this.cargoNome = cargoNome;
		this.percentualAtingimentoMeta = percentualAtingimentoMeta;
	}
	
	public DemonstrativoComissoesReportVisualization()  {
		
	}
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getRazaoSocial() {
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getTerritorio() {
		return territorio;
	}
	public void setTerritorio(String territorio) {
		this.territorio = territorio;
	}
	public String getClienteTerritorio() {
		return clienteTerritorio;
	}
	public void setClienteTerritorio(String clienteTerritorio) {
		this.clienteTerritorio = clienteTerritorio;
	}
	public String getExecutivoTerritorio() {
		return executivoTerritorio;
	}
	public void setExecutivoTerritorio(String executivoTerritorio) {
		this.executivoTerritorio = executivoTerritorio;
	}
	public String getEmissao() {
		return emissao;
	}
	public void setEmissao(String emissao) {
		this.emissao = emissao;
	}
	public String getCif() {
		return cif;
	}
	public void setCif(String cif) {
		this.cif = cif;
	}
	public String getFob() {
		return fob;
	}
	public void setFob(String fob) {
		this.fob = fob;
	}
	public String getCifFob() {
		return cifFob;
	}
	public void setCifFob(String cifFob) {
		this.cifFob = cifFob;
	}
	public String getQtdCtrc() {
		return qtdCtrc;
	}
	public void setQtdCtrc(String qtdCtrc) {
		this.qtdCtrc = qtdCtrc;
	}
	public String getPercentual() {
		return percentual;
	}
	public void setPercentual(String percentual) {
		this.percentual = percentual;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getCargoNome() {
		return cargoNome;
	}
	public void setCargoNome(String cargoNome) {
		this.cargoNome = cargoNome;
	}

	public String getPercentualAtingimentoMeta() {
		return percentualAtingimentoMeta;
	}
	
	public void setPercentualAtingimentoMeta(String percentualAtingimentoMeta) {
		this.percentualAtingimentoMeta = percentualAtingimentoMeta;
	}
	
	@Override
	public int compareTo(DemonstrativoComissoesReportVisualization that) {
		int territorioComp = this.getTerritorio().compareTo(
				that.getTerritorio());
		if (territorioComp != 0) {
			return territorioComp;
		}

		int cnpjComp = this.getCnpj().compareTo(that.getCnpj());
		if (cnpjComp != 0) {
			return cnpjComp;
		}

		return compareDatas(this.getEmissao(), that.getEmissao());
	}

	private int compareDatas(String thisEmissao, String thatEmissao) {
		return monthToIndex(thisEmissao.substring(0, 3)) - monthToIndex(thatEmissao.substring(0, 3));
	}

	private int monthToIndex(String month) {
		if ("Jan".equals(month)) {
			return 1;
		}
		if ("Fev".equals(month)) {
			return 2;
		}
		if ("Mar".equals(month)) {
			return 3;
		}
		if ("Abr".equals(month)) {
			return 4;
		}
		if ("Mai".equals(month)) {
			return 5;
		}
		if ("Jun".equals(month)) {
			return 6;
		}
		if ("Jul".equals(month)) {
			return 7;
		}
		if ("Ago".equals(month)) {
			return 8;
		}
		if ("Set".equals(month)) {
			return 9;
		}
		if ("Out".equals(month)) {
			return 10;
		}
		if ("Nov".equals(month)) {
			return 11;
		}
		if ("Dez".equals(month)) {
			return 11;
		}
		return -1;
	}

}
