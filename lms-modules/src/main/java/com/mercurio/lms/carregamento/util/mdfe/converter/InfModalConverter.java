package com.mercurio.lms.carregamento.util.mdfe.converter;

import com.mercurio.lms.carregamento.util.mdfe.converter.rodo.RodoConverter;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.InfModal;

public class InfModalConverter {

    private ManifestoEletronico mdfe;
    
    private Proprietario proprietarioFilialOrigem;
    
    private String nrVersaoLayout;
    
    private final boolean utilizarTagVeicTracao;
    
    public InfModalConverter(ManifestoEletronico mdfe,
            Proprietario proprietarioFilialOrigem, String nrVersaoLayout, boolean utilizarTagVeicTracao) {
        super();
        this.mdfe = mdfe;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.nrVersaoLayout = nrVersaoLayout;
        this.utilizarTagVeicTracao = utilizarTagVeicTracao;
    }

    public InfModal convert() {
        InfModal infModal = new InfModal();
        
        infModal.setVersaoModal(nrVersaoLayout);
        
        //Rodo
        infModal.setAnyObject(new RodoConverter(mdfe, proprietarioFilialOrigem, utilizarTagVeicTracao).convert());
        
        return infModal;
    }
    
}
