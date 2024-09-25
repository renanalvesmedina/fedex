package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import com.mercurio.lms.mdfe.model.v300.InfSeg;

public class InfSegConverter {
    
	private java.lang.String xSeg;
	private java.lang.String CNPJ;
    
    public InfSegConverter(java.lang.String xSeg, java.lang.String CNPJ) {
        super();
        this.xSeg = xSeg;
        this.CNPJ = CNPJ;
    }

    public InfSeg convert() {
        InfSeg infSeg = new InfSeg();
        
        infSeg.setxSeg(xSeg);
        infSeg.setCNPJ(CNPJ);
        
        return infSeg;
    }

}
