package com.mercurio.lms.carregamento.util.mdfe.converter.v100a;

import java.util.List;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100a.MDFe;

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

	private final boolean contingencia;
	
	private final List<String> filiaisPercurso;
	
	private final CIOT ciot;
    
    public MdfeConverter(ManifestoEletronico mdfe, 
            Proprietario proprietarioFilialOrigem,
            String nrVersaoLayout, 
            String retiraZeroInicialIe,
            String bairroPadrao, 
            String ambienteMdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes,
            boolean utilizarTagXMunCarrega, boolean utilizarTagVeicTracao,
            boolean contingencia, List<String> filiaisPercurso,
            CIOT ciot) {
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
        this.contingencia = contingencia;
        this.filiaisPercurso = filiaisPercurso;
        this.ciot = ciot;
    }

    public MDFe convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
        MDFe mdFe = new MDFe();
        
        //InfMDFe
        mdFe.setInfMDFe(new InfMDFeConverter(mdfe, proprietarioFilialOrigem, nrVersaoLayout, retiraZeroInicialIe, bairroPadrao, ambienteMdfe, monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao, contingencia, filiaisPercurso, ciot).convert(isViagem, isAgrupaPorUFDestino));
        
        return mdFe;
    }
    
}
