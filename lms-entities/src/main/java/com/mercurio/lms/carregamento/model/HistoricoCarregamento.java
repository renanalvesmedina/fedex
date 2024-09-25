package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class HistoricoCarregamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idCarregamento;
	private Long idHistoricoCarregamento;
	private DomainValue statusCarregamento;
	private Long cnpjRemetenteCliente;
	private String rotaCarregamento;
	private DateTime dataHistorico;
	private Long matriculaChefia;

	public HistoricoCarregamento() {
	}
	
	public Long getIdCarregamento() {
		return idCarregamento;
	}

	public void setIdCarregamento(Long idCarregamento) {
		this.idCarregamento = idCarregamento;
	}

	public Long getIdHistoricoCarregamento() {
		return idHistoricoCarregamento;
	}

	public void setIdHistoricoCarregamento(Long idHistoricoCarregamento) {
		this.idHistoricoCarregamento = idHistoricoCarregamento;
	}

	public DomainValue getStatusCarregamento() {
		return statusCarregamento;
	}

	public void setStatusCarregamento(DomainValue statusCarregamento) {
		this.statusCarregamento = statusCarregamento;
	}

	public Long getCnpjRemetenteCliente() {
		return cnpjRemetenteCliente;
	}

	public void setCnpjRemetenteCliente(Long cnpjRemetenteCliente) {
		this.cnpjRemetenteCliente = cnpjRemetenteCliente;
	}

	public String getRotaCarregamento() {
		return rotaCarregamento;
	}

	public void setRotaCarregamento(String rotaCarregamento) {
		this.rotaCarregamento = rotaCarregamento;
	}

	public DateTime getDataHistorico() {
		return dataHistorico;
	}

	public void setDataHistorico(DateTime dataHistorico) {
		this.dataHistorico = dataHistorico;
	}

	public Long getMatriculaChefia() {
		return matriculaChefia;
	}

	public void setMatriculaChefia(Long matriculaChefia) {
		this.matriculaChefia = matriculaChefia;
	}
}
