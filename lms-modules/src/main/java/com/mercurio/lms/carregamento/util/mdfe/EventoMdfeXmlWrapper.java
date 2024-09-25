package com.mercurio.lms.carregamento.util.mdfe;

import java.io.IOException;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.ValidationException;

import com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe.EventoMdfeConverter;
import com.mercurio.lms.carregamento.util.mdfe.type.TipoEventoMdfe;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.EventoMDFe;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.CastorMarshaller;


public class EventoMdfeXmlWrapper {

    private ManifestoEletronico mdfe;
    
    private TipoEventoMdfe tipoEventoMdfe;
    
    private String nrVersaoLayout;
    
    private String ambienteMdfe;
    
    private Filial filialUsuarioLogado;
    
    private String justificativaCancelamentoMdfe;
    
    public EventoMdfeXmlWrapper(ManifestoEletronico mdfe, TipoEventoMdfe tipoEventoMdfe,
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

    public StringBuffer generate() throws ResolverException, MarshalException, ValidationException, IOException {
    	
    	StringBuffer buffer = null;
    	
    	if ("3.00".equals(nrVersaoLayout)) {
    		com.mercurio.lms.mdfe.model.v300.EventoMDFe eventoMDFe = convert300();
    		buffer = CastorMarshaller.marshall(eventoMDFe);
    	}else if ("1.00a".equals(nrVersaoLayout)) {
    		com.mercurio.lms.mdfe.model.v100a.EventoMDFe eventoMDFe = convert100a();
    		buffer = CastorMarshaller.marshall(eventoMDFe);
    	}else{
    		EventoMDFe eventoMDFe = convert();
    		buffer = CastorMarshaller.marshall(eventoMDFe);
    	}
        
        return buffer;
    }

    private EventoMDFe convert() {
        return new EventoMdfeConverter(mdfe, tipoEventoMdfe, nrVersaoLayout, ambienteMdfe, filialUsuarioLogado, justificativaCancelamentoMdfe).convert();
    }
    
    private com.mercurio.lms.mdfe.model.v100a.EventoMDFe convert100a() {
        return new com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe.v100a.EventoMdfeConverter(mdfe, tipoEventoMdfe, nrVersaoLayout, ambienteMdfe, filialUsuarioLogado, justificativaCancelamentoMdfe).convert();
    }
    
    private com.mercurio.lms.mdfe.model.v300.EventoMDFe convert300() {
        return new com.mercurio.lms.carregamento.util.mdfe.converter.eventoMdfe.v300.EventoMdfeConverter(mdfe, tipoEventoMdfe, nrVersaoLayout, ambienteMdfe, filialUsuarioLogado, justificativaCancelamentoMdfe).convert();
    }
    
}
