package com.mercurio.lms.rest.sim;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class EventoDispositivoUnitizacaoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private DateTime dhInclusao;
	
	private EventoDTO evento;
	
	private String obComplemento;
	 
	private DomainValue tpScan;
	
	private FilialSuggestDTO filial;
	
	private UsuarioDTO usuario;

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public EventoDTO getEvento() {
		return evento;
	}

	public void setEvento(EventoDTO evento) {
		this.evento = evento;
	}

	public String getObComplemento() {
		return obComplemento;
	}

	public void setObComplemento(String obComplemento) {
		this.obComplemento = obComplemento;
	}

	public DomainValue getTpScan() {
		return tpScan;
	}

	public void setTpScan(DomainValue tpScan) {
		this.tpScan = tpScan;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}

	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
}