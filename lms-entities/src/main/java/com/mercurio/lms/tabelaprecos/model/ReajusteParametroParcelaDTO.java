package com.mercurio.lms.tabelaprecos.model;

import java.math.BigDecimal;

public class ReajusteParametroParcelaDTO {

	private Long idReajusteTabelaPrecoParcela;
	private Long idTabelaPrecoParcela;

	private Long idParametro;
	private Long idPrecoFrete;
	private Long idFaixaProgressiva;
	private BigDecimal pcParcela;
	private BigDecimal pcMinParcela;
	private BigDecimal valorParcela;
	private BigDecimal valorMinParcela;

	public Long getIdReajusteTabelaPrecoParcela() {
		return idReajusteTabelaPrecoParcela;
	}

	public void setIdReajusteTabelaPrecoParcela(
			Long idReajusteTabelaPrecoParcela) {
		this.idReajusteTabelaPrecoParcela = idReajusteTabelaPrecoParcela;
	}

	public Long getIdTabelaPrecoParcela() {
		return idTabelaPrecoParcela;
	}

	public void setIdTabelaPrecoParcela(Long idTabelaPrecoParcela) {
		this.idTabelaPrecoParcela = idTabelaPrecoParcela;
	}

	public Long getIdParametro() {
		return idParametro;
	}

	public void setIdParametro(Long idParametro) {
		this.idParametro = idParametro;
	}

	public Long getIdPrecoFrete() {
		return idPrecoFrete;
	}

	public void setIdPrecoFrete(Long idPrecoFrete) {
		this.idPrecoFrete = idPrecoFrete;
	}

	public Long getIdFaixaProgressiva() {
		return idFaixaProgressiva;
	}

	public void setIdFaixaProgressiva(Long idFaixaProgressiva) {
		this.idFaixaProgressiva = idFaixaProgressiva;
	}

	public BigDecimal getPcParcela() {
		return pcParcela;
	}

	public void setPcParcela(BigDecimal pcParcela) {
		this.pcParcela = pcParcela;
	}

	public BigDecimal getPcMinParcela() {
		return pcMinParcela;
	}

	public void setPcMinParcela(BigDecimal pcMinParcela) {
		this.pcMinParcela = pcMinParcela;
	}

	public BigDecimal getValorParcela() {
		return valorParcela;
	}

	public void setValorParcela(BigDecimal valorParcela) {
		this.valorParcela = valorParcela;
	}

	public BigDecimal getValorMinParcela() {
		return valorMinParcela;
	}

	public void setValorMinParcela(BigDecimal valorMinParcela) {
		this.valorMinParcela = valorMinParcela;
	}

	@Override
	public String toString() {
		return "ReajusteParametroParcelaDTO [idReajusteTabelaPrecoParcela="
				+ idReajusteTabelaPrecoParcela + ", idTabelaPrecoParcela="
				+ idTabelaPrecoParcela + ", idParametro=" + idParametro
				+ ", idPrecoFrete=" + idPrecoFrete + ", idFaixaProgressiva="
				+ idFaixaProgressiva + ", pcParcela=" + pcParcela
				+ ", pcMinParcela=" + pcMinParcela + ", valorParcela="
				+ valorParcela + ", valorMinParcela=" + valorMinParcela + "]";
	}

	
	
	
}
