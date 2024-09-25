package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Depots implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private List<Depot> depots = new ArrayList<Depot>();

	public List<Depot> getDepots() {
		return depots;
	}
	
	public void addDepot(Depot depot){
		this.depots.add(depot);
	}
	
	public void addAllDepots(List<Depot> depots){
		this.depots.addAll(depots);
	}
}
