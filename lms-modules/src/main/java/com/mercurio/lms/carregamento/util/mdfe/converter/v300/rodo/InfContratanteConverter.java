package com.mercurio.lms.carregamento.util.mdfe.converter.v300.rodo;

import java.util.List;

import com.mercurio.lms.mdfe.model.v300.InfContratante;

public class InfContratanteConverter {
	
    private List<InfContratante> infContratanteList;
    
    public InfContratanteConverter(List<InfContratante> infContratanteList) {
        super();
        this.infContratanteList = infContratanteList;
    }

    public InfContratante[] convert() {
        return infContratanteList.toArray(new InfContratante[infContratanteList.size()]);
    }

}
