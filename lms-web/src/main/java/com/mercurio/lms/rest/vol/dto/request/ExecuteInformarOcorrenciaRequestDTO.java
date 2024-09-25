package com.mercurio.lms.rest.vol.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class ExecuteInformarOcorrenciaRequestDTO {

	private static final long serialVersionUID = 1L;

	@JsonProperty("idFilial")
	private String idFilial;

	@JsonProperty("idMeioTransporte")
	@NotNull
	private String idMeioTransporte;

	@JsonProperty("idControleCarga")
	private String idControleCarga;

	@JsonProperty("idConhecimento")
	private String idConhecimento;

	@JsonProperty("cdEvento")
	@NotNull
	private String cdEvento;

	@JsonProperty("idPedidoColeta")
	private String idPedidoColeta;

	@JsonProperty("usuarioSessao")
	private String usuarioSessao;

	@JsonProperty("dhSolicitacao")
	private String dhSolicitacao;

	public ExecuteInformarOcorrenciaRequestDTO() {
	}

	public ExecuteInformarOcorrenciaRequestDTO(String idFilial,
			String idMeioTransporte, String idControleCarga,
			String idConhecimento, String cdEvento, String idPedidoColeta,
			String usuarioSessao, String dhSolicitacao) {
		super();
		this.idFilial = idFilial;
		this.idMeioTransporte = idMeioTransporte;
		this.idControleCarga = idControleCarga;
		this.idConhecimento = idConhecimento;
		this.cdEvento = cdEvento;
		this.idPedidoColeta = idPedidoColeta;
		this.usuarioSessao = usuarioSessao;
		this.dhSolicitacao = dhSolicitacao;
	}

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getIdMeioTransporte() {
		return idMeioTransporte;
	}

	public void setIdMeioTransporte(String idMeioTransporte) {
		this.idMeioTransporte = idMeioTransporte;
	}

	public String getIdControleCarga() {
		return idControleCarga;
	}

	public void setIdControleCarga(String idControleCarga) {
		this.idControleCarga = idControleCarga;
	}

	public String getIdConhecimento() {
		return idConhecimento;
	}

	public void setIdConhecimento(String idConhecimento) {
		this.idConhecimento = idConhecimento;
	}

	public String getCdEvento() {
		return cdEvento;
	}

	public void setCdEvento(String cdEvento) {
		this.cdEvento = cdEvento;
	}

	public String getIdPedidoColeta() {
		return idPedidoColeta;
	}

	public void setIdPedidoColeta(String idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}

	public String getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(String usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public String getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(String dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

}
