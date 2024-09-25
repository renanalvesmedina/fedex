package com.mercurio.lms.rest.sim;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class EventoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Short cdEvento;

	private DomainValue tpEvento;

	public Short getCdEvento() {
		return cdEvento;
	}

	public void setCdEvento(Short cdEvento) {
		this.cdEvento = cdEvento;
	}

	public DomainValue getTpEvento() {
		return tpEvento;
	}

	public void setTpEvento(DomainValue tpEvento) {
		this.tpEvento = tpEvento;
	}
	
}