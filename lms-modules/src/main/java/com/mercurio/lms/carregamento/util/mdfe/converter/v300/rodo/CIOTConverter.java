package com.mercurio.lms.carregamento.util.mdfe.converter.v300.rodo;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.mdfe.model.v300.InfCIOT;
import com.mercurio.lms.util.FormatUtils;

public class CIOTConverter {
	
    private CIOT ciot;
    private java.lang.String CNPJ;
    
    public CIOTConverter(com.mercurio.lms.expedicao.model.CIOT ciot, java.lang.String CNPJ) {
        super();
        this.ciot = ciot;
        this.CNPJ = CNPJ;
    }

    public InfCIOT[] convert() {

    	List<InfCIOT> infCIOTList = new ArrayList<InfCIOT>();
    	
    	InfCIOT infCIOT = new InfCIOT();
    	
    	infCIOT.setCIOT(FormatUtils.fillNumberWithZero(ciot.getNrCIOT().toString(), 12));
    	infCIOT.setCNPJ(CNPJ);
		infCIOTList.add(infCIOT);
    	
        return infCIOTList.toArray(new InfCIOT[infCIOTList.size()]);
    }

}
