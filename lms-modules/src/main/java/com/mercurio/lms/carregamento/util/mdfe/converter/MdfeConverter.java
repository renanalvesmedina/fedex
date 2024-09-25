package com.mercurio.lms.carregamento.util.mdfe.converter;

import java.util.List;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100.MDFe;

public class MdfeConverter {
    
    private final ManifestoEletronico mdfe;
    
    private final Proprietario proprietarioFilialOrigem;

    private final String nrVersaoLayout;

    private final String retiraZeroInicialIe;
    
    private final String bairroPadrao;
    
    private final String ambienteMdfe;
    
    private final List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    private final boolean utilizarTagXMunCarrega;
    
    private final boolean utilizarTagVeicTracao;
    
    public MdfeConverter(ManifestoEletronico mdfe, 
            Proprietario proprietarioFilialOrigem,
            String nrVersaoLayout, 
            String retiraZeroInicialIe,
            String bairroPadrao, 
            String ambienteMdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes,
            boolean utilizarTagXMunCarrega, boolean utilizarTagVeicTracao) {
        super();
        this.mdfe = mdfe;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.nrVersaoLayout = nrVersaoLayout;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
        this.bairroPadrao = bairroPadrao;
        this.ambienteMdfe = ambienteMdfe;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
        this.utilizarTagXMunCarrega = utilizarTagXMunCarrega;
        this.utilizarTagVeicTracao = utilizarTagVeicTracao;
    }

    public MDFe convert() {
        MDFe mdFe = new MDFe();
        
        //InfMDFe
        mdFe.setInfMDFe(new InfMDFeConverter(mdfe, proprietarioFilialOrigem, nrVersaoLayout, retiraZeroInicialIe, bairroPadrao, ambienteMdfe, monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao).convert());
        
        return mdFe;
    }
    
}
