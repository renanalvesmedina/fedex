package com.mercurio.lms.layoutNfse.model.impressao;

import java.util.ArrayList;
import java.util.List;

public class ImpressaoControladaRps {
	
	private List<Rps> rps = new ArrayList<Rps>();

	public List<Rps> getRps() {
		return rps;
	}

	public void addRps(Rps rps) {
		this.rps.add(rps);
	}

}
