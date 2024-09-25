package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Schedulings implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<Scheduling> schedulings = new ArrayList<Scheduling>();
	
	
	public List<Scheduling> getSchedulings() {
		return schedulings;
	}
	
	public void addScheduling(Scheduling scheduling){
		this.schedulings.add(scheduling);
	}

}
