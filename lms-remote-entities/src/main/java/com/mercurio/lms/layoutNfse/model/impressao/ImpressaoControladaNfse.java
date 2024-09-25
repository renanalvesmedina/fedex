package com.mercurio.lms.layoutNfse.model.impressao;

import java.util.ArrayList;
import java.util.List;

public class ImpressaoControladaNfse {
	
	private List<Nfse> nfse =  new ArrayList<Nfse>();

	public List<Nfse> getNfse() {
		return nfse;
	}

	public void addNfse(Nfse nfse) {
		this.nfse.add(nfse);
	}
	
}
