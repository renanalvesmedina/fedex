package com.mercurio.lms.rest.portaria.saida.contract;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.util.JTDateTimeUtils;

public class Saida extends BaseDTO {

	private String placa;
	private String frota;
	private String frotaSemiReboque;
	private String idSemiReboque;
	private String siglaFilial;
	private String controleCarga;
	private String filialDestino;
	private String numeroRota;
	private String descricaoRota;
	private String tipoControleCarga;
	private String horaSaidaPrevista;

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getFrota() {
		return frota;
	}

	public void setFrota(String frota) {
		this.frota = frota;
	}

	public String getFrotaSemiReboque() {
		return frotaSemiReboque;
	}

	public void setFrotaSemiReboque(String frotaSemiReboque) {
		this.frotaSemiReboque = frotaSemiReboque;
	}

	public String getIdSemiReboque() {
		return idSemiReboque;
	}

	public void setIdSemiReboque(String idSemiReboque) {
		this.idSemiReboque = idSemiReboque;
	}

	public String getSiglaFilial() {
		return siglaFilial;
	}

	public void setSiglaFilial(String siglaFilial) {
		this.siglaFilial = siglaFilial;
	}

	public String getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(String controleCarga) {
		this.controleCarga = controleCarga;
	}

	public String getFilialDestino() {
		return filialDestino;
	}

	public void setFilialDestino(String filialDestino) {
		this.filialDestino = filialDestino;
	}

	public String getNumeroRota() {
		return numeroRota;
	}

	public void setNumeroRota(String numeroRota) {
		this.numeroRota = numeroRota;
	}

	public String getDescricaoRota() {
		return descricaoRota;
	}

	public void setDescricaoRota(String descricaoRota) {
		this.descricaoRota = descricaoRota;
	}

	public String getTipoControleCarga() {
		return tipoControleCarga;
	}

	public void setTipoControleCarga(String tipoControleCarga) {
		this.tipoControleCarga = tipoControleCarga;
	}

	public String getHoraSaidaPrevista() {
		return horaSaidaPrevista;
	}

	public void setHoraSaidaPrevista(String horaSaidaPrevista) {
		this.horaSaidaPrevista = horaSaidaPrevista;
	}

}