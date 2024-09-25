package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.coleta.model.PedidoColeta;

public class EventosColetaDTO extends BaseDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DomainValue tpEventoColeta;
	private DateTime dhEvento;
	private String nrFrota;
	private String dsDescricaoCompleta;
	private String nmUsuario;
	
	
	
	public DomainValue getTpEventoColeta() {
		return tpEventoColeta;
	}
	public void setTpEventoColeta(DomainValue tpEventoColeta) {
		this.tpEventoColeta = tpEventoColeta;
	}
	public DateTime getDhEvento() {
		return dhEvento;
	}
	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}
	public String getNrFrota() {
		return nrFrota;
	}
	public void setNrFrota(String nrFrota) {
		this.nrFrota = nrFrota;
	}
	public String getDsDescricaoCompleta() {
		return dsDescricaoCompleta;
	}
	public void setDsDescricaoCompleta(String dsDescricaoCompleta) {
		this.dsDescricaoCompleta = dsDescricaoCompleta;
	}
	public String getNmUsuario() {
		return nmUsuario;
	}
	public void setNmUsuario(String nmUsuario) {
		this.nmUsuario = nmUsuario;
	}

}
