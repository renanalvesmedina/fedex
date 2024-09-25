package com.mercurio.lms.carregamento.util.mdfe.converter;

import java.util.List;

import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100.InfDoc;

public class InfDocConverter {

    private ManifestoEletronico mdfe;
    
    private List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    public InfDocConverter(ManifestoEletronico mdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes) {
        super();
        this.mdfe = mdfe;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
    }

    public InfDoc convert() {
        InfDoc infDoc = new InfDoc();

        //InfMunDescarga
        infDoc.setInfMunDescarga(new InfMunDescargaConverter(mdfe, monitoramentoDocEltronicoCtes).convert());
        
        return infDoc;
    }

}
