package com.mercurio.lms.carregamento.util.mdfe.converter.v100a;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100a.InfMDFe;
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

	private final boolean contingencia;
	
	private final List<String> filiaisPercurso;
	
	private final CIOT ciot;
    
    public InfMDFeConverter(ManifestoEletronico mdfe, 
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

    public InfMDFe convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
        InfMDFe infMDFe = new InfMDFe();
        infMDFe.setVersao(nrVersaoLayout.replace("a", ""));
        
        //IDE
        infMDFe.setIde(new IdeConverter(mdfe, nrVersaoLayout, ambienteMdfe, utilizarTagXMunCarrega, contingencia, filiaisPercurso).convert(isViagem, isAgrupaPorUFDestino));
        
        //Emit
        infMDFe.setEmit(new EmitConverter(mdfe.getFilialOrigem().getPessoa(), retiraZeroInicialIe, bairroPadrao).convert());
        
        //InfModal
        infMDFe.setInfModal(new InfModalConverter(mdfe, proprietarioFilialOrigem, nrVersaoLayout, utilizarTagVeicTracao, retiraZeroInicialIe, ciot).convert());
        
        //InfDoc
        infMDFe.setInfDoc(new InfDocConverter(mdfe, monitoramentoDocEltronicoCtes).convert(isViagem, isAgrupaPorUFDestino));
        
        //Tot
        infMDFe.setTot(new TotConverter(mdfe).convert());

        String chave;
        if (StringUtils.isBlank(mdfe.getNrChave()) || contingencia) {
        	chave = ChaveMdfeGenerator.generate(infMDFe);
        } else {
        	chave = mdfe.getNrChave();
        }
        infMDFe.setId("MDFe"+chave);
        infMDFe.getIde().setCDV(chave.substring(chave.length()-1));

        return infMDFe;
    }

}
