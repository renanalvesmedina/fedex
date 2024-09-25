package com.mercurio.lms.tracking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Cities implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<City> cities = new ArrayList<City>();

	public List<City> getCities() {
		return cities;
	}

	public void addCity(City city) {
		this.cities.add(city);
	}

}
