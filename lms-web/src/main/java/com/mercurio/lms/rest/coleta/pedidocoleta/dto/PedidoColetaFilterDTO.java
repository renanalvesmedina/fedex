package com.mercurio.lms.rest.coleta.pedidocoleta.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.rest.configuracoes.RegiaoColetaEntregaChosenDTO;
import com.mercurio.lms.rest.configuracoes.ServicoChosenDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;

public class PedidoColetaFilterDTO extends BaseFilterDTO {

	private static final long serialVersionUID = 1L;

	private Long idEnderecoPessoa;
	private Long idCliente;
	private FilialSuggestDTO filial, destino;
	private Long nrColeta;
	private RotaColetaEntregaSuggestDTO rotaColetaEntrega;
	private DateTime dtPedidoColetaInicial;
	private DateTime dtPedidoColetaFinal;
	private UsuarioDTO usuario;
	private RegiaoColetaEntregaChosenDTO regiaoColetaEntrega;
	private ServicoChosenDTO servico;
	private DomainValue tipoColeta;
	private ClienteSuggestDTO cliente;
	private boolean aberto, cancelada, coletaTerminal, transmitida,
			aguardandoDescarga, manifestada, emDescarga, noManifesto, executada,
			finalizada, semVinculo;
	
	public ServicoChosenDTO getServico() {
		return servico;
	}

	public void setServico(ServicoChosenDTO servico) {
		this.servico = servico;
	}

	public Long getIdEnderecoPessoa() {
		return idEnderecoPessoa;
	}

	public void setIdEnderecoPessoa(Long idEnderecoPessoa) {
		this.idEnderecoPessoa = idEnderecoPessoa;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public Long getNrColeta() {
		return nrColeta;
	}

	public void setNrColeta(Long nrColeta) {
		this.nrColeta = nrColeta;
	}

	public ClienteSuggestDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteSuggestDTO cliente) {
		this.cliente = cliente;
	}

	public boolean isAberto() {
		return aberto;
	}

	public void setAberto(boolean aberto) {
		this.aberto = aberto;
	}

	public boolean isCancelada() {
		return cancelada;
	}

	public void setCancelada(boolean cancelada) {
		this.cancelada = cancelada;
	}

	public boolean isColetaTerminal() {
		return coletaTerminal;
	}

	public void setColetaTerminal(boolean coletaTerminal) {
		this.coletaTerminal = coletaTerminal;
	}

	public boolean isTransmitida() {
		return transmitida;
	}

	public void setTransmitida(boolean transmitida) {
		this.transmitida = transmitida;
	}

	public boolean isAguardandoDescarga() {
		return aguardandoDescarga;
	}

	public void setAguardandoDescarga(boolean aguardandoDescarga) {
		this.aguardandoDescarga = aguardandoDescarga;
	}

	public boolean isManifestada() {
		return manifestada;
	}

	public void setManifestada(boolean manifestada) {
		this.manifestada = manifestada;
	}

	public boolean isEmDescarga() {
		return emDescarga;
	}

	public void setEmDescarga(boolean emDescarga) {
		this.emDescarga = emDescarga;
	}

	public boolean isNoManifesto() {
		return noManifesto;
	}

	public void setNoManifesto(boolean noManifesto) {
		this.noManifesto = noManifesto;
	}

	public boolean isExecutada() {
		return executada;
	}

	public void setExecutada(boolean executada) {
		this.executada = executada;
	}

	public boolean isFinalizada() {
		return finalizada;
	}

	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}

	public boolean isSemVinculo() {
		return semVinculo;
	}

	public void setSemVinculo(boolean semVinculo) {
		this.semVinculo = semVinculo;
	}

	public RotaColetaEntregaSuggestDTO getRotaColetaEntrega() {
		return rotaColetaEntrega;
	}

	public void setRotaColetaEntrega(
			RotaColetaEntregaSuggestDTO rotaColetaEntrega) {
		this.rotaColetaEntrega = rotaColetaEntrega;
	}

	public DateTime getDtPedidoColetaInicial() {
		return dtPedidoColetaInicial;
	}

	public void setDtPedidoColetaInicial(DateTime dtPedidoColetaInicial) {
		this.dtPedidoColetaInicial = dtPedidoColetaInicial;
	}

	public DateTime getDtPedidoColetaFinal() {
		return dtPedidoColetaFinal;
	}

	public void setDtPedidoColetaFinal(DateTime dtPedidoColetaFinal) {
		this.dtPedidoColetaFinal = dtPedidoColetaFinal;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public FilialSuggestDTO getDestino() {
		return destino;
	}

	public void setDestino(FilialSuggestDTO destino) {
		this.destino = destino;
	}

	public RegiaoColetaEntregaChosenDTO getRegiaoColetaEntrega() {
		return regiaoColetaEntrega;
	}

	public void setRegiaoColetaEntrega(RegiaoColetaEntregaChosenDTO regiaoColetaEntrega) {
		this.regiaoColetaEntrega = regiaoColetaEntrega;
	}

	public DomainValue getTipoColeta() {
		return tipoColeta;
	}

	public void setTipoColeta(DomainValue tipoColeta) {
		this.tipoColeta = tipoColeta;
	}

}
