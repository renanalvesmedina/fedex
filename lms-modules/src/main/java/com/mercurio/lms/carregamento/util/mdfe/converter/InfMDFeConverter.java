package com.mercurio.lms.carregamento.util.mdfe.converter;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100.InfMDFe;
import com.mercurio.lms.util.ChaveMdfeGenerator;

public class InfMDFeConverter {

    private final ManifestoEletronico mdfe;
    
    private final Proprietario proprietarioFilialOrigem;

    private final String nrVersaoLayout;

    private final String retiraZeroInicialIe;
    
    private final String bairroPadrao;
    
    private final String ambienteMdfe;

    private final List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    private final boolean utilizarTagXMunCarrega;
    
    private final boolean utilizarTagVeicTracao;
    
    public InfMDFeConverter(ManifestoEletronico mdfe, 
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

    public InfMDFe convert() {
        InfMDFe infMDFe = new InfMDFe();
        infMDFe.setVersao(nrVersaoLayout);
        
        //IDE
        infMDFe.setIde(new IdeConverter(mdfe, nrVersaoLayout, ambienteMdfe, utilizarTagXMunCarrega).convert());
        
        //Emit
        infMDFe.setEmit(new EmitConverter(mdfe.getFilialOrigem().getPessoa(), retiraZeroInicialIe, bairroPadrao).convert());
        
        //InfModal
        infMDFe.setInfModal(new InfModalConverter(mdfe, proprietarioFilialOrigem, nrVersaoLayout, utilizarTagVeicTracao).convert());
        
        //InfDoc
        infMDFe.setInfDoc(new InfDocConverter(mdfe, monitoramentoDocEltronicoCtes).convert());
        
        //Tot
        infMDFe.setTot(new TotConverter(mdfe).convert());

        String chave;
        if (StringUtils.isNotBlank(mdfe.getNrChave())) {
            chave = mdfe.getNrChave();
        } else {
            chave = ChaveMdfeGenerator.generate(infMDFe);
        }
        infMDFe.setId("MDFe"+chave);
        infMDFe.getIde().setCDV(chave.substring(chave.length()-1));

        return infMDFe;
    }

}
