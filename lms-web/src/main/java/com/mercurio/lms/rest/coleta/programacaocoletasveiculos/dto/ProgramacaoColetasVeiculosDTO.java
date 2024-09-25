package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.rest.BaseDTO;

public class ProgramacaoColetasVeiculosDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long idPedidoColeta;
	private Long nrColeta;
	private String nrFrota;
	private String nrIdentificador;
	private String sgFilial;
	private Long nrControleCarga;
	private String sgFilialControleCarga;
	private BigDecimal nrCapacidadeKg;
	private String siglaSimbolo1;
	private BigDecimal vlTotalFrota;
	private String siglaSimbolo2;
	private BigDecimal vlAColetar;
	private BigDecimal psTotalFrota;
	private BigDecimal psAColetar;
	private BigDecimal pcOcupacaoInformado;
	private String dsTipoMeioTransporte;
	private BigDecimal vlColetado;
	private BigDecimal vlAEntregar;
	private BigDecimal vlEntregue;
	private BigDecimal psColetado;
	private BigDecimal psAEntregar;
	private BigDecimal psEntregue;
	private Long idMeioTransporte;
	private Short nrRota;
	private String dsRota;
	

	
	private String frotaPlaca;
	
	public Long getIdPedidoColeta() {
		return idPedidoColeta;
	}
	public void setIdPedidoColeta(Long idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}
	public Long getNrColeta() {
		return nrColeta;
	}
	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}
	public BigDecimal getVlColetado() {
		return vlColetado;
	}
	public void setVlColetado(BigDecimal vlColetado) {
		this.vlColetado = vlColetado;
	}
	public BigDecimal getVlAEntregar() {
		return vlAEntregar;
	}
	public void setVlAEntregar(BigDecimal vlAEntregar) {
		this.vlAEntregar = vlAEntregar;
	}
	public BigDecimal getVlEntregue() {
		return vlEntregue;
	}
	public void setVlEntregue(BigDecimal vlEntregue) {
		this.vlEntregue = vlEntregue;
	}
	public BigDecimal getPsColetado() {
		return psColetado;
	}
	public void setPsColetado(BigDecimal psColetado) {
		this.psColetado = psColetado;
	}
	public BigDecimal getPsAEntregar() {
		return psAEntregar;
	}
	public void setPsAEntregar(BigDecimal psAEntregar) {
		this.psAEntregar = psAEntregar;
	}
	public BigDecimal getPsEntregue() {
		return psEntregue;
	}
	public void setPsEntregue(BigDecimal psEntregue) {
		this.psEntregue = psEntregue;
	}
	
	public String getNrFrota() {
		return nrFrota;
	}
	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
	}
	public String getNrIdentificador() {
		return nrIdentificador;
	}
	public void setNrIdentificador(String nrIdentificador) {
		this.nrIdentificador = nrIdentificador;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public Long getNrControleCarga() {
		return nrControleCarga;
	}
	public void setNrControleCarga(Long nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}
	public BigDecimal getNrCapacidadeKg() {
		return nrCapacidadeKg;
	}
	public void setNrCapacidadeKg(BigDecimal nrCapacidadeKg) {
		this.nrCapacidadeKg = nrCapacidadeKg;
	}
	public String getSiglaSimbolo1() {
		return siglaSimbolo1;
	}
	public void setSiglaSimbolo1(String siglaSimbolo1) {
		this.siglaSimbolo1 = siglaSimbolo1;
	}
	public BigDecimal getVlTotalFrota() {
		return vlTotalFrota;
	}
	public void setVlTotalFrota(BigDecimal vlTotalFrota) {
		this.vlTotalFrota = vlTotalFrota;
	}
	public String getSiglaSimbolo2() {
		return siglaSimbolo2;
	}
	public void setSiglaSimbolo2(String siglaSimbolo2) {
		this.siglaSimbolo2 = siglaSimbolo2;
	}
	public BigDecimal getVlAColetar() {
		return vlAColetar;
	}
	public void setVlAColetar(BigDecimal vlAColetar) {
		this.vlAColetar = vlAColetar;
	}
	public BigDecimal getPsTotalFrota() {
		return psTotalFrota;
	}
	public void setPsTotalFrota(BigDecimal psTotalFrota) {
		this.psTotalFrota = psTotalFrota;
	}
	public BigDecimal getPsAColetar() {
		return psAColetar;
	}
	public void setPsAColetar(BigDecimal psAColetar) {
		this.psAColetar = psAColetar;
	}
	public BigDecimal getPcOcupacaoInformado() {
		return pcOcupacaoInformado;
	}
	public void setPcOcupacaoInformado(BigDecimal pcOcupacaoInformado) {
		this.pcOcupacaoInformado = pcOcupacaoInformado;
	}
	public String getDsTipoMeioTransporte() {
		return dsTipoMeioTransporte;
	}
	public void setDsTipoMeioTransporte(String dsTipoMeioTransporte) {
		this.dsTipoMeioTransporte = dsTipoMeioTransporte;
	}
	public Long getIdMeioTransporte() {
		return idMeioTransporte;
	}
	public void setIdMeioTransporte(Long idMeioTransporte) {
		this.idMeioTransporte = idMeioTransporte;
	}
	public String getFrotaPlaca() {
		return frotaPlaca;
	}
	public void setFrotaPlaca(String frotaPlaca) {
		this.frotaPlaca = frotaPlaca;
	}
	public String getSgFilialControleCarga() {
		return sgFilialControleCarga;
	}
	public void setSgFilialControleCarga(String sgFilialControleCarga) {
		this.sgFilialControleCarga = sgFilialControleCarga;
	}
	public String getDsRota() {
		return dsRota;
	}
	public void setDsRota(String dsRota) {
		this.dsRota = dsRota;
	}
	public Short getNrRota() {
		return nrRota;
	}
	public void setNrRota(Short nrRota) {
		this.nrRota = nrRota;
	}
	

}
