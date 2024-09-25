package com.mercurio.lms.tracking;

import java.io.Serializable;

public class DeliveryLocation implements Serializable {

	private static final long serialVersionUID = 1L;

	private Depot origin;
	private Depot destination;
	private Depot current;
	private Long ctrc;
	private Event event;
	private String status;
	private Long idDoctoServico;
	private String direction;
	
	public Depot getOrigin() {
		return origin;
	}

	public void setOrigin(Depot origin) {
		this.origin = origin;
	}

	public Depot getDestination() {
		return destination;
	}

	public void setDestination(Depot destination) {
		this.destination = destination;
	}

	public Depot getCurrent() {
		return current;
	}

	public void setCurrent(Depot current) {
		this.current = current;
	}

	public Long getCtrc() {
		return ctrc;
	}

	public void setCtrc(Long ctrc) {
		this.ctrc = ctrc;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Long getIdDoctoServico() {
		return idDoctoServico;
	}
	
	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
