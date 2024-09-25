package com.mercurio.lms.tracking;

import java.io.Serializable;



public class Event implements Serializable{
	
	
	private static final long serialVersionUID = 1L;

    private String description;
    private String depotCode;
    private String date;
    private String cdEvent;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getCdEvent() {
		return cdEvent;
	}

	public void setCdEvent(String cdEvent) {
		this.cdEvent = cdEvent;
	}
}
