package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class Volume implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idVolume;
	private Long mapaCarregamento;
	private String codigoVolume;
	private Long cnpjDestinatario;
	private DateTime dataEmbarque;
	private String itemVolume;
	private BigDecimal cubagemVolume; 
	private BigDecimal pesoVolume;
	private DomainValue codigoStatus;
	private Long matriculaResponsavel;
	private Carregamento carregamento;
	private CabecalhoCarregamento cabecalhoCarregamento;
	
	public Volume(Long id) {
		this.idVolume = id;
	}

	public Volume() {
	}

	public Volume(Long idVolume, Long mapaCarregamento, String codigoVolume, Long cnpjDestinatario, DateTime dataEmbarque, String itemVolume, BigDecimal cubagemVolume, BigDecimal pesoVolume, DomainValue codigoStatus, Long matriculaResponsavel, Carregamento carregamento, CabecalhoCarregamento cabecalhoCarregamento) {
		this.idVolume = idVolume;
		this.mapaCarregamento = mapaCarregamento;
		this.codigoVolume = codigoVolume;
		this.cnpjDestinatario = cnpjDestinatario;
		this.dataEmbarque = dataEmbarque;
		this.itemVolume = itemVolume;
		this.cubagemVolume = cubagemVolume;
		this.pesoVolume = pesoVolume;
		this.codigoStatus = codigoStatus;
		this.matriculaResponsavel = matriculaResponsavel;
		this.carregamento = carregamento;
		this.cabecalhoCarregamento = cabecalhoCarregamento;
	}

	public Long getIdVolume() {
		return idVolume;
	}

	public void setIdVolume(Long idVolume) {
		this.idVolume = idVolume;
	}

	public Long getMapaCarregamento() {
		return mapaCarregamento;
	}

	public void setMapaCarregamento(Long mapaCarregamento) {
		this.mapaCarregamento = mapaCarregamento;
	}

	public String getCodigoVolume() {
		return codigoVolume;
	}

	public void setCodigoVolume(String codigoVolume) {
		this.codigoVolume = codigoVolume;
	}

	public Long getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	public void setCnpjDestinatario(Long cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	public DateTime getDataEmbarque() {
		return dataEmbarque;
	}

	public void setDataEmbarque(DateTime dataEmbarque) {
		this.dataEmbarque = dataEmbarque;
	}

	public String getItemVolume() {
		return itemVolume;
	}

	public void setItemVolume(String itemVolume) {
		this.itemVolume = itemVolume;
	}

	public BigDecimal getCubagemVolume() {
		return cubagemVolume;
	}

	public void setCubagemVolume(BigDecimal cubagemVolume) {
		this.cubagemVolume = cubagemVolume;
	}

	public BigDecimal getPesoVolume() {
		return pesoVolume;
	}

	public void setPesoVolume(BigDecimal pesoVolume) {
		this.pesoVolume = pesoVolume;
	}

	public DomainValue getCodigoStatus() {
		return codigoStatus;
	}

	public void setCodigoStatus(DomainValue codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	public Long getMatriculaResponsavel() {
		return matriculaResponsavel;
	}

	public void setMatriculaResponsavel(Long matriculaResponsavel) {
		this.matriculaResponsavel = matriculaResponsavel;
	}

	public Carregamento getCarregamento() {
		return carregamento;
	}

	public void setCarregamento(Carregamento carregamento) {
		this.carregamento = carregamento;
	}

	public CabecalhoCarregamento getCabecalhoCarregamento() {
		return cabecalhoCarregamento;
	}

	public void setCabecalhoCarregamento(
			CabecalhoCarregamento cabecalhoCarregamento) {
		this.cabecalhoCarregamento = cabecalhoCarregamento;
	}

}