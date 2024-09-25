package com.mercurio.lms.rest.vol.dto.request;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class GerenciaBaixaRequestDTO {

	private static final long serialVersionUID = 1L;

	@JsonProperty("idFilial")
	private String idFilial;

	@JsonProperty("tpBaixa")
	@NotNull
	private String tpBaixa;

	@JsonProperty("ids")
	private String ids;
	
	@JsonProperty("idDoctoServico")
	private String idDoctoServico;
	
	@JsonProperty("image")
	private String image;
	
	@JsonProperty("valor")
	private String valor;

	@JsonProperty("idEquipamento")
	private String idEquipamento;

	@JsonProperty("qtVolumes")
	private String qtVolumes;

	@JsonProperty("peso")
	private String peso;

	@JsonProperty("cdOcorrenciaColeta")
	private String cdOcorrenciaColeta;

	@JsonProperty("dhEvento")
	private String dhEvento;

	@JsonProperty("idManifesto")
	private String idManifesto;

	@JsonProperty("nmRecebedor")
	private String nmRecebedor;

	@JsonProperty("cdOcorrencia")
	private String cdOcorrencia;

	@JsonProperty("tpEntregaParcial")
	private String tpEntregaParcial;

	@JsonProperty("cdComplementoMotivo")
	private String cdComplementoMotivo;

	@JsonProperty("dcma")
	private String dcma;

	@JsonProperty("dhOcorrencia")
	private String dhOcorrencia;
	
	@JsonProperty("perguntasRiscoResposta")
	List<Map<String, Object>> perguntasRiscoResposta;
	
	@JsonProperty("idPedidoColeta")
	private String idPedidoColeta;
	
	public GerenciaBaixaRequestDTO() {
	}

	public GerenciaBaixaRequestDTO(String idFilial, String tpBaixa, String ids, String idDoctoServico, String image,
			String valor, String idEquipamento, String qtVolumes, String peso, String cdOcorrenciaColeta,
			String dhEvento, String idManifesto, String nmRecebedor, String cdOcorrencia, String tpEntregaParcial,
			String cdComplementoMotivo, String dcma, String dhOcorrencia,
			List<Map<String, Object>> perguntasRiscoResposta, String idPedidoColeta) {
		super();
		this.idFilial = idFilial;
		this.tpBaixa = tpBaixa;
		this.ids = ids;
		this.idDoctoServico = idDoctoServico;
		this.image = image;
		this.valor = valor;
		this.idEquipamento = idEquipamento;
		this.qtVolumes = qtVolumes;
		this.peso = peso;
		this.cdOcorrenciaColeta = cdOcorrenciaColeta;
		this.dhEvento = dhEvento;
		this.idManifesto = idManifesto;
		this.nmRecebedor = nmRecebedor;
		this.cdOcorrencia = cdOcorrencia;
		this.tpEntregaParcial = tpEntregaParcial;
		this.cdComplementoMotivo = cdComplementoMotivo;
		this.dcma = dcma;
		this.dhOcorrencia = dhOcorrencia;
		this.perguntasRiscoResposta = perguntasRiscoResposta;
		this.idPedidoColeta = idPedidoColeta;
	}

	public String getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(String idFilial) {
		this.idFilial = idFilial;
	}

	public String getTpBaixa() {
		return tpBaixa;
	}

	public void setTpBaixa(String tpBaixa) {
		this.tpBaixa = tpBaixa;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getIdDoctoServico() {
		return idDoctoServico;
	}

	public void setIdDoctoServico(String idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getIdEquipamento() {
		return idEquipamento;
	}

	public void setIdEquipamento(String idEquipamento) {
		this.idEquipamento = idEquipamento;
	}

	public String getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(String qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getCdOcorrenciaColeta() {
		return cdOcorrenciaColeta;
	}

	public void setCdOcorrenciaColeta(String cdOcorrenciaColeta) {
		this.cdOcorrenciaColeta = cdOcorrenciaColeta;
	}

	public String getDhEvento() {
		return dhEvento;
	}

	public void setDhEvento(String dhEvento) {
		this.dhEvento = dhEvento;
	}

	public String getIdManifesto() {
		return idManifesto;
	}

	public void setIdManifesto(String idManifesto) {
		this.idManifesto = idManifesto;
	}

	public String getNmRecebedor() {
		return nmRecebedor;
	}

	public void setNmRecebedor(String nmRecebedor) {
		this.nmRecebedor = nmRecebedor;
	}

	public String getCdOcorrencia() {
		return cdOcorrencia;
	}

	public void setCdOcorrencia(String cdOcorrencia) {
		this.cdOcorrencia = cdOcorrencia;
	}

	public String getTpEntregaParcial() {
		return tpEntregaParcial;
	}

	public void setTpEntregaParcial(String tpEntregaParcial) {
		this.tpEntregaParcial = tpEntregaParcial;
	}

	public String getCdComplementoMotivo() {
		return cdComplementoMotivo;
	}

	public void setCdComplementoMotivo(String cdComplementoMotivo) {
		this.cdComplementoMotivo = cdComplementoMotivo;
	}

	public String getDcma() {
		return dcma;
	}

	public void setDcma(String dcma) {
		this.dcma = dcma;
	}

	public String getDhOcorrencia() {
		return dhOcorrencia;
	}

	public void setDhOcorrencia(String dhOcorrencia) {
		this.dhOcorrencia = dhOcorrencia;
	}

	public List<Map<String, Object>> getPerguntasRiscoResposta() {
		return perguntasRiscoResposta;
	}

	public void setPerguntasRiscoResposta(List<Map<String, Object>> perguntasRiscoResposta) {
		this.perguntasRiscoResposta = perguntasRiscoResposta;
	}

	public String getIdPedidoColeta() {
		return idPedidoColeta;
	}

	public void setIdPedidoColeta(String idPedidoColeta) {
		this.idPedidoColeta = idPedidoColeta;
	}
	
}
