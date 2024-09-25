package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v300.AutXML;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.mdfe.model.v300.InfMDFe;
import com.mercurio.lms.mdfe.model.v300.Seg;
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
	
	private final String cnpjCiot;
	
	private List<Seg> segList;
	
	private List<InfContratante> infContratanteList;
	
	private List<AutXML> autXMLList;

	private final String incluirGrupoProdPredMdfe;
	
	private final String dsProdutoPredominante;
	
	private final String cepCarregadoMDFE;
	
	private final String cepDescarregadoMDFE;
    
    public InfMDFeConverter(ManifestoEletronico mdfe, 
            Proprietario proprietarioFilialOrigem,
            String nrVersaoLayout, 
            String retiraZeroInicialIe,
            String bairroPadrao,
            String ambienteMdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes,
            boolean utilizarTagXMunCarrega, boolean utilizarTagVeicTracao,
            boolean contingencia, List<String> filiaisPercurso,
            CIOT ciot,
            String cnpjCiot,
            List<Seg> segList,
            List<InfContratante> infContratanteList,
            List<AutXML> autXMLList, 
            String incluirGrupoProdPredMdfe,
            String dsProdutoPredominante,
            String cepCarregadoMDFE,
            String cepDescarregadoMDFE
            ) {
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
        this.cnpjCiot = cnpjCiot;
        this.segList = segList; 
        this.infContratanteList = infContratanteList;
        this.autXMLList = autXMLList;
        this.incluirGrupoProdPredMdfe = incluirGrupoProdPredMdfe;
        this.dsProdutoPredominante = dsProdutoPredominante;
        this.cepCarregadoMDFE = cepCarregadoMDFE;
        this.cepDescarregadoMDFE = cepDescarregadoMDFE;
    }

    public InfMDFe convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
        InfMDFe infMDFe = new InfMDFe();
        infMDFe.setVersao(nrVersaoLayout.replace("a", ""));
        
        //IDE
        infMDFe.setIde(new IdeConverter(mdfe, nrVersaoLayout, ambienteMdfe, utilizarTagXMunCarrega, contingencia, filiaisPercurso).convert(isViagem, isAgrupaPorUFDestino));
        
        //Emit
        infMDFe.setEmit(new EmitConverter(mdfe.getFilialOrigem().getPessoa(), retiraZeroInicialIe, bairroPadrao).convert());
        
        //InfModal
        infMDFe.setInfModal(new InfModalConverter(mdfe, proprietarioFilialOrigem, nrVersaoLayout, utilizarTagVeicTracao, retiraZeroInicialIe, ciot, cnpjCiot, infContratanteList).convert());
        
        //InfDoc
        infMDFe.setInfDoc(new InfDocConverter(mdfe, monitoramentoDocEltronicoCtes).convert(isViagem, isAgrupaPorUFDestino));
        
        //Seg
        infMDFe.setSeg(new SegConverter(segList).convert());
        
        //ProdPred
        if("S".equalsIgnoreCase(incluirGrupoProdPredMdfe)){
        	infMDFe.setProdPred(new ProdPredConverter(mdfe, dsProdutoPredominante, cepCarregadoMDFE, cepDescarregadoMDFE).convert());
        }
        
        //Tot
        infMDFe.setTot(new TotConverter(mdfe).convert());

        //AutXml
        infMDFe.setAutXML(new AutXMLConverter(autXMLList).convert());
        
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
