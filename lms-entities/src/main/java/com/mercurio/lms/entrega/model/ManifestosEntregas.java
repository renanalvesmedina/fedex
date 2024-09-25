package com.mercurio.lms.entrega.model;

import java.io.Serializable;

public class ManifestosEntregas implements Serializable{

	private static final long serialVersionUID = 1L;
	private String unidSigla;
	private String numero;
	private Long tipoTabela;
	private String veicCodPlaca;
	private Long veicNroPlaca;
	private String seceUnidSigla;
	private Long seceNumero;

		
	public String getUnidSigla() {
		return unidSigla;
	}

	public void setUnidSigla(String unidSigla) {
		this.unidSigla = unidSigla;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Long getTipoTabela() {
		return tipoTabela;
	}

	public void setTipoTabela(Long tipoTabela) {
		this.tipoTabela = tipoTabela;
	}

	public String getVeicCodPlaca() {
		return veicCodPlaca;
	}

	public void setVeicCodPlaca(String veicCodPlaca) {
		this.veicCodPlaca = veicCodPlaca;
	}

	public Long getVeicNroPlaca() {
		return veicNroPlaca;
	}

	public void setVeicNroPlaca(Long veicNroPlaca) {
		this.veicNroPlaca = veicNroPlaca;
	}

	public String getSeceUnidSigla() {
		return seceUnidSigla;
	}

	public void setSeceUnidSigla(String seceUnidSigla) {
		this.seceUnidSigla = seceUnidSigla;
	}

	public Long getSeceNumero() {
		return seceNumero;
	}

	public void setSeceNumero(Long seceNumero) {
		this.seceNumero = seceNumero;
	}
	
}
