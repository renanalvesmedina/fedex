package com.mercurio.lms.rest.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class StoreConfirmaRecebimentoColetaRequestDTO { 

	private static final long serialVersionUID = 1L;

	@JsonProperty("idFilial")
	private String idFilial;

	@JsonProperty("idPedidoColeta")
	@NotNull
	private String idPedidoColeta;

	@JsonProperty("dhConfirmacaoVol")
	@NotNull
	private String dhConfirmacaoVol;

	public StoreConfirmaRecebimentoColetaRequestDTO() {
	}

	public StoreConfirmaRecebimentoColetaRequestDTO(String idFilial,
			String idPedidoColeta, String dhConfirmacaoVol) {
		super();
		this.idFilial = idFilial;
		this.idPedidoColeta = idPedidoColeta;
		this.dhConfirmacaoVol = dhConfirmacaoVol;
	}

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getIdPedidoColeta() {
		return idPedidoColeta;
	}

	public void setIdPedidoColeta(String idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}

	public String getDhConfirmacaoVol() {
		return dhConfirmacaoVol;
	}

	public void setDhConfirmacaoVol(String dhConfirmacaoVol) {
		this.dhConfirmacaoVol = dhConfirmacaoVol;
	}

}
