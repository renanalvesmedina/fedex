package com.mercurio.lms.tracking;

import java.io.Serializable;

public class Scheduling implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String contactDate;
    private String description;
    private String depotCode;
    private String depotName;
    private String schedulingDate;
    
        
	public String getSchedulingDate() {
		return schedulingDate;
	}
	public void setSchedulingDate(String schedulingDate) {
		this.schedulingDate = schedulingDate;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDepotCode() {
		return depotCode;
	}
	public void setDepotCode(String depotCode) {
		this.depotCode = depotCode;
	}

	public String getDepotName() {
		return depotName;
	}

	public void setDepotName(String depotName) {
		this.depotName = depotName;
	}
	public String getContactDate() {
		return contactDate;
	}
	public void setContactDate(String contactDate) {
		this.contactDate = contactDate;
	}

    
	

}
