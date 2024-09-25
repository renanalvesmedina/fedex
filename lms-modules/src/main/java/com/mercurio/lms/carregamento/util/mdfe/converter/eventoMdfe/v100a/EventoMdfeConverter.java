package com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe.v100a;

import com.mercurio.lms.carregamento.util.mdfe.type.TipoEventoMdfe;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.EventoMDFe;
import com.mercurio.lms.municipios.model.Filial;

public class EventoMdfeConverter {

    private ManifestoEletronico mdfe;
    
    private TipoEventoMdfe tipoEventoMdfe;
    
    private String nrVersaoLayout;
    
    private String ambienteMdfe;
    
    private Filial filialUsuarioLogado;
    
    private String justificativaCancelamentoMdfe;
    
    public EventoMdfeConverter(ManifestoEletronico mdfe, TipoEventoMdfe tipoEventoMdfe,
            String nrVersaoLayout, String ambienteMdfe,
            Filial filialUsuarioLogado, String justificativaCancelamentoMdfe) {
        super();
        this.mdfe = mdfe;
        this.tipoEventoMdfe = tipoEventoMdfe;
        this.nrVersaoLayout = nrVersaoLayout;
        this.ambienteMdfe = ambienteMdfe;
        this.filialUsuarioLogado = filialUsuarioLogado;
        this.justificativaCancelamentoMdfe = justificativaCancelamentoMdfe;
    }

    public EventoMDFe convert() {
        EventoMDFe eventoMDFe = new EventoMDFe();
        
        eventoMDFe.setVersao(nrVersaoLayout.replace("a", ""));
        
        eventoMDFe.setInfEvento(new InfEventoConverter(mdfe, tipoEventoMdfe, ambienteMdfe, filialUsuarioLogado, justificativaCancelamentoMdfe, nrVersaoLayout).convert());
        
        return eventoMDFe;
    }
    
}
