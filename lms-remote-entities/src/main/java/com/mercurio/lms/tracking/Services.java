package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Services implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Service> services = new ArrayList<Service>();

	public List<Service> getServices() {
		return services;
	}

	public void addService(Service service) {
		this.services.add(service);
	}

}
