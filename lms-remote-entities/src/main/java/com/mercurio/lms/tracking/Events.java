package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Events implements Serializable{
	
	
	private static final long serialVersionUID = 1L;
	
	private List<Event> events = new ArrayList<Event>();

	public List<Event> getEvents() {
		return events;
	}
	
	public void addEvent(Event event){
		this.events.add(event);
	}

}
