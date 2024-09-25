package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;

public class CabecalhoCarregamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idCabecalhoCarregamento;
	private Long mapaCarregamento;
	private DateTime dataCriacao;
	private DateTime dataDisponivel;
	private Long docaCarregamento;
	private List<Volume> volumes;
	private List<TotalCarregamento> totalCarregamentos;
	
    private List<DetalheCarregamento> detalhesCarregamentos;
	
	public List<DetalheCarregamento> getDetalhesCarregamentos() {
		return detalhesCarregamentos;
	}

	public void setDetalhesCarregamentos(
			List<DetalheCarregamento> detalhesCarregamentos) {
		this.detalhesCarregamentos = detalhesCarregamentos;
	}

	
	public CabecalhoCarregamento(final Long id) {
		this.idCabecalhoCarregamento = id;
	}

	public CabecalhoCarregamento() {
	}

	public Long getIdCabecalhoCarregamento() {
		return idCabecalhoCarregamento;
	}

	public void setIdCabecalhoCarregamento(final Long idCabecalhoCarregamento) {
		this.idCabecalhoCarregamento = idCabecalhoCarregamento;
	}

	public Long getMapaCarregamento() {
		return mapaCarregamento;
	}

	public void setMapaCarregamento(final Long mapaCarregamento) {
		this.mapaCarregamento = mapaCarregamento;
	}

	public DateTime getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(final DateTime dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public DateTime getDataDisponivel() {
		return dataDisponivel;
	}

	public void setDataDisponivel(final DateTime dataDisponivel) {
		this.dataDisponivel = dataDisponivel;
	}

	public Long getDocaCarregamento() {
		return docaCarregamento;
	}

	public void setDocaCarregamento(final Long docaCarregamento) {
		this.docaCarregamento = docaCarregamento;
	}

	public List<Volume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}

	public List<TotalCarregamento> getTotalCarregamentos() {
		return totalCarregamentos;
	}

	public void setTotalCarregamentos(List<TotalCarregamento> totalCarregamentos) {
		this.totalCarregamentos = totalCarregamentos;
	}
	
}
