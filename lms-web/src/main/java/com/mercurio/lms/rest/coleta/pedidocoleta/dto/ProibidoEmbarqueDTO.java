package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class ProibidoEmbarqueDTO extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dsMotivoProibidoEmbarque;
	private String dsBloqueio;
	
	public String getDsMotivoProibidoEmbarque() {
		return dsMotivoProibidoEmbarque;
	}
	public void setDsMotivoProibidoEmbarque(String dsMotivoProibidoEmbarque) {
		this.dsMotivoProibidoEmbarque = dsMotivoProibidoEmbarque;
	}
	public String getDsBloqueio() {
		return dsBloqueio;
	}
	public void setDsBloqueio(String dsBloqueio) {
		this.dsBloqueio = dsBloqueio;
	}
}
