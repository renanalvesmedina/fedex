package com.mercurio.lms.carregamento.util;

import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;

public class MeioTranspProprietarioBuilder {

	public static MeioTranspProprietario buildFromMeioTransporte(Long idMeioTransporte) {
		MeioTranspProprietario meioTranspProprietario = new MeioTranspProprietario();
		MeioTransporte meioTransporte = new MeioTransporte();
		meioTransporte.setIdMeioTransporte(idMeioTransporte);
		meioTranspProprietario.setMeioTransporte(meioTransporte);
		return meioTranspProprietario;
	}
	
	public static MeioTranspProprietario buildFromMeioTransporte(MeioTransporte meioTransporte) {
		MeioTranspProprietario meioTranspProprietario = new MeioTranspProprietario();
		meioTranspProprietario.setMeioTransporte(meioTransporte);
		return meioTranspProprietario;
	}
	
    public static MeioTranspProprietario buildMeioTranspProprietarioFromMeioTransporte(MeioTransporte meioTransporte){
    	return buildFromMeioTransporte(meioTransporte.getIdMeioTransporte());
    }
}
