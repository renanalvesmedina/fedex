package com.mercurio.lms.carregamento.util.mdfe.converter.v100a;

import com.mercurio.lms.carregamento.util.mdfe.converter.v100a.rodo.RodoConverter;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.InfModal;

public class InfModalConverter {

    private ManifestoEletronico mdfe;
    
    private String retiraZeroInicialIe;
    
    private Proprietario proprietarioFilialOrigem;
    
    private String nrVersaoLayout;
    
    private final boolean utilizarTagVeicTracao;
    
    private CIOT ciot;
    
    public InfModalConverter(ManifestoEletronico mdfe,
            Proprietario proprietarioFilialOrigem, String nrVersaoLayout, boolean utilizarTagVeicTracao, String retiraZeroInicialIe, CIOT ciot) {
        super();
        this.mdfe = mdfe;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.nrVersaoLayout = nrVersaoLayout;
        this.utilizarTagVeicTracao = utilizarTagVeicTracao;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
        this.ciot = ciot;
    }

    public InfModal convert() {
        InfModal infModal = new InfModal();
        
        infModal.setVersaoModal(nrVersaoLayout.replace("a", ""));
        
        //Rodo
        infModal.setAnyObject(new RodoConverter(mdfe, proprietarioFilialOrigem, utilizarTagVeicTracao, retiraZeroInicialIe, ciot).convert());
        
        return infModal;
    }
    
}
