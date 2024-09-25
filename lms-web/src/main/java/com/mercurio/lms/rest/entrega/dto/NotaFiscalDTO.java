package com.mercurio.lms.rest.entrega.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.rest.BaseDTO;

public class NotaFiscalDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idNotaFiscal;
	private String nrNotaFiscal;
	private Integer qtVolume;
	private BigDecimal psMercadoria;
	private BigDecimal vlTotal;
	private EntregaNotaFiscalDTO entregaNotaFiscalDTO;
	
	public Long getIdNotaFiscal() {
		return idNotaFiscal;
	}
	public void setIdNotaFiscal(Long idNotaFiscal) {
		this.idNotaFiscal = idNotaFiscal;
	}
	public String getNrNotaFiscal() {
		return nrNotaFiscal;
	}
	public void setNrNotaFiscal(String nrNotaFiscal) {
		this.nrNotaFiscal = nrNotaFiscal;
	}
	public Integer getQtVolume() {
		return qtVolume;
	}
	public void setQtVolume(Integer qtVolume) {
		this.qtVolume = qtVolume;
	}
	public BigDecimal getPsMercadoria() {
		return psMercadoria;
	}
	public void setPsMercadoria(BigDecimal psMercadoria) {
		this.psMercadoria = psMercadoria;
	}
	public BigDecimal getVlTotal() {
		return vlTotal;
	}
	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}
	public EntregaNotaFiscalDTO getEntregaNotaFiscalDTO() {
		return entregaNotaFiscalDTO;
	}
	public void setEntregaNotaFiscalDTO(EntregaNotaFiscalDTO entregaNotaFiscalDTO) {
		this.entregaNotaFiscalDTO = entregaNotaFiscalDTO;
	}

}
