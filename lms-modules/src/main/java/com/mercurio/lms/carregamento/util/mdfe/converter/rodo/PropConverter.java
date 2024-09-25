package com.mercurio.lms.carregamento.util.mdfe.converter.rodo;

import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.Prop;
import com.mercurio.lms.util.FormatUtils;

public class PropConverter {
    
    private ManifestoEletronico mdfe;
    
    public PropConverter(ManifestoEletronico mdfe) {
        super();
        this.mdfe = mdfe;
    }

    public Prop convert() {
        Prop prop = new Prop();
        
        prop.setRNTRC(FormatUtils.fillNumberWithZero(mdfe.getControleCarga().getProprietario().getNrAntt() == null ? null : mdfe.getControleCarga().getProprietario().getNrAntt().toString(),8));
        
        return prop;
    }
    
}
