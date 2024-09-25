package com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe.v300;

import com.mercurio.lms.carregamento.util.mdfe.type.TipoEventoMdfe;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.DetEvento;
import com.mercurio.lms.municipios.model.Filial;

public class DetEventoConverter {

    private ManifestoEletronico mdfe;
    
    private Filial filialUsuarioLogado;
    
    private String justificativaCancelamentoMdfe;
    
    private TipoEventoMdfe tipoEventoMdfe;
    
    private String nrVersaoLayout;
    
    public DetEventoConverter(ManifestoEletronico mdfe,
            Filial filialUsuarioLogado, String justificativaCancelamentoMdfe,
            TipoEventoMdfe tipoEventoMdfe, String nrVersaoLayout) {
        super();
        this.mdfe = mdfe;
        this.filialUsuarioLogado = filialUsuarioLogado;
        this.justificativaCancelamentoMdfe = justificativaCancelamentoMdfe;
        this.tipoEventoMdfe = tipoEventoMdfe;
        this.nrVersaoLayout = nrVersaoLayout;
    }

    public DetEvento convert() {
        DetEvento detEvento = new DetEvento();
        
        detEvento.setVersaoEvento(nrVersaoLayout.replace("a", ""));
        
        if (TipoEventoMdfe.CANCELAMENTO.equals(tipoEventoMdfe)) {
            detEvento.setAnyObject(new EvCancMDFeConverter(mdfe, justificativaCancelamentoMdfe).convert());
        } else {
            detEvento.setAnyObject(new EvEncMDFeConverter(mdfe, filialUsuarioLogado).convert());
        }
        
        return detEvento;
    }
    
}
