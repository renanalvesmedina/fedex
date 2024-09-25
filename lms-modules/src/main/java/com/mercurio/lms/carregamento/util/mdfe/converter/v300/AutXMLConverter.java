package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import com.mercurio.lms.mdfe.model.v300.AutXML;
import com.mercurio.lms.mdfe.model.v300.Seg;

public class AutXMLConverter {
	
    List<AutXML> autXmlList;
    
    public AutXMLConverter(List<AutXML> autXmlList) {
        super();
        this.autXmlList = autXmlList;
    }

    public AutXML[] convert() {
        return autXmlList.toArray(new AutXML[autXmlList.size()]);
    }
    
}
