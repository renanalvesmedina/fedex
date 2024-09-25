package com.mercurio.lms.ppd.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable 
public class PpdJdePK implements Serializable {	
	private static final long serialVersionUID = 1L;
	
	private String bimid;
	private Long bilnid;
	
	public String getBimid() {
		return bimid;
	}

	public void setBimid(String bimid) {
		this.bimid = bimid;
	}

	public Long getBilnid() {
		return bilnid;
	}

	public void setBilnid(Long bilnid) {
		this.bilnid = bilnid;
	}	
}
