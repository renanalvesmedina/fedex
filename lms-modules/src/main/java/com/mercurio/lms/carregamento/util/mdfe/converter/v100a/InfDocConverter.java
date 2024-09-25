package com.mercurio.lms.carregamento.util.mdfe.converter.v100a;

import java.util.List;

import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100a.InfDoc;

public class InfDocConverter {

    private ManifestoEletronico mdfe;
    
    private List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    public InfDocConverter(ManifestoEletronico mdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes) {
        super();
        this.mdfe = mdfe;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
    }

    public InfDoc convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
        InfDoc infDoc = new InfDoc();

        //InfMunDescarga
        if(isViagem){
        	infDoc.setInfMunDescarga(new InfMunDescargaConverter(mdfe, monitoramentoDocEltronicoCtes).convertViagem());
        }else{
        	infDoc.setInfMunDescarga(new InfMunDescargaConverter(mdfe, monitoramentoDocEltronicoCtes).convert(isViagem, isAgrupaPorUFDestino));
        }
        
        
        return infDoc;
    }

}
