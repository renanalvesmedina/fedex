package com.mercurio.lms.tracking;

import java.io.Serializable;



public class Invoice implements Serializable, Comparable<Invoice>{
	
	
	private static final long serialVersionUID = 1L;

    private String number;
    private String volumes;
    private String weight;
    private String issuedDate;

    public String getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(String issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getVolumes() {
        return volumes;
    }

    public void setVolumes(String volumes) {
        this.volumes = volumes;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

	@Override
	public int compareTo(Invoice invoice) {
		return this.number.compareTo(invoice.getNumber());
	}

    
    
}
