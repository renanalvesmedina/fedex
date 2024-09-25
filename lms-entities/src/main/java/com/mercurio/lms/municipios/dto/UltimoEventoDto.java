package com.mercurio.lms.municipios.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class UltimoEventoDto  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String prevChegada;
	private BigDecimal cdLocalizacaoMercadoria;
	private String dsEvento;
	private BigDecimal idDoctoServico;
	
	public String getDsEvento() {
		return dsEvento;
	}
	public void setDsEvento(String dsEvento) {
		this.dsEvento = dsEvento;
	}
	public String getPrevChegada() {
		return prevChegada;
	}
	public void setPrevChegada(String prevChegada) {
		this.prevChegada = prevChegada;
	}
	public BigDecimal getCdLocalizacaoMercadoria() {
		return cdLocalizacaoMercadoria;
	}
	
	public void setCdLocalizacaoMercadoria(BigDecimal cdLocalizacaoMercadoria) {
		this.cdLocalizacaoMercadoria = cdLocalizacaoMercadoria;
	}
	
	public BigDecimal getIdDoctoServico() {
		return idDoctoServico;
	}
	
	public void setIdDoctoServico(BigDecimal idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}
}
