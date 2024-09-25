package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v300.InfCTe;

public class InfCteConverter {
    
    private Conhecimento conhecimento;

    private List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    public InfCteConverter(Conhecimento conhecimento,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes) {
        super();
        this.conhecimento = conhecimento;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
    }

    public InfCTe convert() {
        InfCTe infCte = new InfCTe();
        
        for (MonitoramentoDocEletronico mde: monitoramentoDocEltronicoCtes) {
            if (mde.getDoctoServico() != null) {
                if (mde.getDoctoServico().getIdDoctoServico().equals(conhecimento.getIdDoctoServico())) {
                    infCte.setChCTe(mde.getNrChave());
                    break;
                }
            }
        }

        return infCte;
    }
    
}
