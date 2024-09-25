package com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe.v100a;

import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.EvCancMDFe;
import com.mercurio.lms.mdfe.model.v100a.types.DescEventoType;

public class EvCancMDFeConverter {
    
    private ManifestoEletronico mdfe;
    
    private String justificativaCancelamentoMdfe;
    
    public EvCancMDFeConverter(ManifestoEletronico mdfe,
            String justificativaCancelamentoMdfe) {
        super();
        this.mdfe = mdfe;
        this.justificativaCancelamentoMdfe = justificativaCancelamentoMdfe;
    }

    public EvCancMDFe convert() {
        EvCancMDFe evCancMDFe = new EvCancMDFe();
        
        evCancMDFe.setDescEvento(DescEventoType.CANCELAMENTO);
        evCancMDFe.setNProt(mdfe.getNrProtocolo() == null ? null : mdfe.getNrProtocolo().toString());
        evCancMDFe.setXJust(justificativaCancelamentoMdfe);
        
        return evCancMDFe;
    }
    
}
