package com.mercurio.lms.rest.expedicao;

import com.mercurio.lms.util.IntegerUtils;
 
public class NetworkAereoDTO { 
	
	private static final String YELLOW = "yellow";
	private static final String GREEN = "green";
	private static final String RED = "red";
	private Long idCiaAerea;
	private String nmCiaAerea;
	private Integer countPreAwb;
	private Integer countAwb;	
	private Integer eea;
	private Integer countAguardandoEntrega;
	private Integer countAguardandoEmbarque;
	private Integer eva;
	private Integer countEmTransito;
	private Integer countEmLibFiscal;
	private Integer countDisponivel;
	private Double tempoTransferencia;
	private String tempoTransferenciaFormatado;
	private Integer dpr;
	private Integer countRetirada;
	private String colorEEA;
	private String colorEVA;
	private String colorDpr;
	
	public Long getIdCiaAerea() {
		return idCiaAerea;
	}
	public void setIdCiaAerea(Long idCiaAerea) {
		this.idCiaAerea = idCiaAerea;
	}
	public Integer getCountPreAwb() {
		return countPreAwb;
	}
	public void setCountPreAwb(Integer countPreAwb) {
		this.countPreAwb = countPreAwb;
	}
	public Integer getCountAwb() {
		return countAwb;
	}
	public void setCountAwb(Integer countAwb) {
		this.countAwb = countAwb;
	}
	public Integer getCountAguardandoEntrega() {
		return countAguardandoEntrega;
	}
	public void setCountAguardandoEntrega(Integer countAguardandoEntrega) {
		this.countAguardandoEntrega = countAguardandoEntrega;
	}
	public Integer getCountAguardandoEmbarque() {
		return countAguardandoEmbarque;
	}
	public void setCountAguardandoEmbarque(Integer countAguardandoEmbarque) {
		this.countAguardandoEmbarque = countAguardandoEmbarque;
	}
	public Integer getCountEmTransito() {
		return countEmTransito;
	}
	public void setCountEmTransito(Integer countEmTransito) {
		this.countEmTransito = countEmTransito;
	}
	public Integer getCountEmLibFiscal() {
		return countEmLibFiscal;
	}
	public void setCountEmLibFiscal(Integer countEmLibFiscal) {
		this.countEmLibFiscal = countEmLibFiscal;
	}
	public Integer getCountDisponivel() {
		return countDisponivel;
	}
	public void setCountDisponivel(Integer countDisponivel) {
		this.countDisponivel = countDisponivel;
	}
	public Double getTempoTransferencia() {
		return tempoTransferencia;
	}
	public void setTempoTransferencia(Double tempoTransferencia) {
		this.tempoTransferencia = tempoTransferencia;
	}
	public Integer getCountRetirada() {
		return countRetirada;
	}
	public void setCountRetirada(Integer countRetirada) {
		this.countRetirada = countRetirada;
	}
	public String getNmCiaAerea() {
		return nmCiaAerea;
	}
	public void setNmCiaAerea(String nmCiaAerea) {
		this.nmCiaAerea = nmCiaAerea;
	}
	public Integer getEea() {
		return eea;
	}
	public void setEea(Integer eea) {
		this.eea = eea;
	}
	public Integer getEva() {
		return eva;
	}
	public void setEva(Integer eva) {
		this.eva = eva;
	}
	public Integer getDpr() {
		return dpr;
	}
	
	public void setDpr(Integer dpr) {
		this.dpr = dpr;
	}

	public String getColorEEA() {
		if (IntegerUtils.getInteger("1").equals(getEea())) {
			colorEEA = GREEN;
		} else if (IntegerUtils.getInteger("2").equals(getEea())) {
			colorEEA = YELLOW;
		} else if (IntegerUtils.getInteger("3").equals(getEea())) {
			colorEEA = RED;
		}
		
		return colorEEA;
	}
	
	public String getColorEVA() {
		if (IntegerUtils.getInteger("1").equals(getEva())) {
			colorEVA = GREEN;
		} else if (IntegerUtils.getInteger("2").equals(getEva())) {
			colorEVA = YELLOW;
		} else if (IntegerUtils.getInteger("3").equals(getEva())) {
			colorEVA = RED;
		}
		
		return colorEVA;
	}

	public String getColorDpr() {
		if (IntegerUtils.getInteger("1").equals(getDpr())) {
			colorDpr = GREEN;
		} else if (IntegerUtils.getInteger("2").equals(getDpr())) {
			colorDpr = YELLOW;
		} else if (IntegerUtils.getInteger("3").equals(getDpr())) {
			colorDpr = RED;
		}		
		return colorDpr;
	}
	public String getTempoTransferenciaFormatado() {
		return tempoTransferenciaFormatado;
	}
	public void setTempoTransferenciaFormatado(String tempoTransferenciaFormatado) {
		this.tempoTransferenciaFormatado = tempoTransferenciaFormatado;
	}
	
} 
