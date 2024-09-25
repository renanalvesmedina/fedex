package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import com.mercurio.lms.mdfe.model.v300.InfResp;

public class InfRespConverter {
    
	private com.mercurio.lms.mdfe.model.v300.types.RespSegType respSegType;
	private java.lang.String CNPJ;
	private java.lang.String CPF;
    
    public InfRespConverter(com.mercurio.lms.mdfe.model.v300.types.RespSegType respSegType, java.lang.String CNPJ, java.lang.String CPF) {
        super();
        this.respSegType = respSegType;
        this.CNPJ = CNPJ;
        this.CPF = CPF;
    }

    public InfResp convert() {
    	InfResp infResp = new InfResp();
        
    	infResp.setRespSeg(respSegType);
    	if(CNPJ != null){
    		infResp.setCNPJ(CNPJ);
    	}
    	if(CPF != null){
    		infResp.setCPF(CPF);
    	}
        
        return infResp;
    }

}
