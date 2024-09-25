package com.mercurio.lms.rest.coleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class PedidoColetaDTO extends BaseDTO{
	private static final long serialVersionUID = 1L;

	private Long idPedidoColeta;
	private String sgFilial;
	private Long nrColeta;

	public Long getIdPedidoColeta() {
		return idPedidoColeta;
	}
	public void setIdPedidoColeta(Long idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public Long getNrColeta() {
		return nrColeta;
	}
	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}
	
}
