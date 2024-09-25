package com.mercurio.lms.coleta.model.dto;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public class ConsultarColetaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long idFilial;
	private Long idRotaColetaEntrega;
	private Long idRegiaoColetaEntregaFil;
	private Long idServico;
	private Long idCliente;	
	private Long idFilialDestino;	
	private Long idDestino;
	private Long idUsuario;	
	private Long nrColeta;	
	
	private DateTime dhPedidoColetaInicial;	
	private DateTime dhPedidoColetaFinal;	
	
	private Boolean blSemVinculoDoctoServico;
	private Boolean blProdutoPerigoso;
	private Boolean blProdutoControlado;

	private List<String> tpsStatusColeta;
	
	private String tpPedidoColeta;

	public ConsultarColetaDTO(){
		this.blSemVinculoDoctoServico = Boolean.FALSE;
	}
	
	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public Long getIdRotaColetaEntrega() {
		return idRotaColetaEntrega;
	}

	public void setIdRotaColetaEntrega(Long idRotaColetaEntrega) {
		this.idRotaColetaEntrega = idRotaColetaEntrega;
	}

	public Long getIdRegiaoColetaEntregaFil() {
		return idRegiaoColetaEntregaFil;
	}

	public void setIdRegiaoColetaEntregaFil(Long idRegiaoColetaEntregaFil) {
		this.idRegiaoColetaEntregaFil = idRegiaoColetaEntregaFil;
	}

	public Long getIdServico() {
		return idServico;
	}

	public void setIdServico(Long idServico) {
		this.idServico = idServico;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdFilialDestino() {
		return idFilialDestino;
	}

	public void setIdFilialDestino(Long idFilialDestino) {
		this.idFilialDestino = idFilialDestino;
	}

	public Long getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Long idDestino) {
		this.idDestino = idDestino;
	}

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Long getNrColeta() {
		return nrColeta;
	}

	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}

	public DateTime getDhPedidoColetaInicial() {
		return dhPedidoColetaInicial;
	}

	public void setDhPedidoColetaInicial(DateTime dhPedidoColetaInicial) {
		this.dhPedidoColetaInicial = dhPedidoColetaInicial;
	}

	public DateTime getDhPedidoColetaFinal() {
		return dhPedidoColetaFinal;
	}

	public void setDhPedidoColetaFinal(DateTime dhPedidoColetaFinal) {
		this.dhPedidoColetaFinal = dhPedidoColetaFinal;
	}

	public Boolean getBlSemVinculoDoctoServico() {
		return blSemVinculoDoctoServico;
	}

	public void setBlSemVinculoDoctoServico(Boolean blSemVinculoDoctoServico) {
		this.blSemVinculoDoctoServico = blSemVinculoDoctoServico;
	}

	public List<String> getTpsStatusColeta() {
		return tpsStatusColeta;
	}

	public void setTpsStatusColeta(List<String> tpsStatusColeta) {
		this.tpsStatusColeta = tpsStatusColeta;
	}

	public String getTpPedidoColeta() {
		return tpPedidoColeta;
	}

	public void setTpPedidoColeta(String tpPedidoColeta) {
		this.tpPedidoColeta = tpPedidoColeta;
	}

	public Boolean getBlProdutoPerigoso() {
		return blProdutoPerigoso;
	}

	public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
		this.blProdutoPerigoso = blProdutoPerigoso;
	}

	public Boolean getBlProdutoControlado() {
		return blProdutoControlado;
	}

	public void setBlProdutoControlado(Boolean blProdutoControlado) {
		this.blProdutoControlado = blProdutoControlado;
	}
}
