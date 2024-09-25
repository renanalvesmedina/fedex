package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class States implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<State> states = new ArrayList<State>();

	public List<State> getStates() {
		return states;
	}

	public void addState(State state) {
		this.states.add(state);
	}

}
