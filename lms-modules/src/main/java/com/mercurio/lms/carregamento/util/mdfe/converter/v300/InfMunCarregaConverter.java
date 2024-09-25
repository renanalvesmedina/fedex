package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.InfMunCarrega;
import com.mercurio.lms.util.FormatUtils;

public class InfMunCarregaConverter {

    private final ManifestoEletronico mdfe;
    
    private final boolean utilizarTagXMunCarrega;
    
    public InfMunCarregaConverter(ManifestoEletronico mdfe, boolean utilizarTagXMunCarrega) {
        super();
        this.mdfe = mdfe;
        this.utilizarTagXMunCarrega = utilizarTagXMunCarrega;
    }

    public InfMunCarrega[] convert() {

        List<InfMunCarrega> toReturn = new ArrayList<InfMunCarrega>();
        InfMunCarrega infMunCarrega = new InfMunCarrega();
        
        toReturn.add(infMunCarrega);
        
        infMunCarrega.setCMunCarrega(MdfeConverterUtils.formatCMun(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio()));
        if (utilizarTagXMunCarrega) {
        	infMunCarrega.setXMunCarrega(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 60)));
        } else {
            infMunCarrega.setCMunCarrega(MdfeConverterUtils.tratarString(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(),60));
        }
        
        return toReturn.toArray(new InfMunCarrega[] {});
    }
    
}
