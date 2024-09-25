package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v300.AutXML;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.mdfe.model.v300.Seg;

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
	
	private final String cnpjCiot;
	
	private List<Seg> segList;
	
	private List<InfContratante> infContratanteList;
	
	private String incluirQrcodeMdfe;
	
	private String linkQrCodeMdfe;
	
	private List<AutXML> autXMLList;
	
	private String incluirGrupoProdPredMdfe;
	
	private String dsProdutoPredominante;
	
	private final String cepCarregadoMDFE;
	
	private final String cepDescarregadoMDFE;
    
    public MdfeConverter(ManifestoEletronico mdfe, 
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
            String incluirQrcodeMdfe,
            String linkQrCodeMdfe,
            List<AutXML> autXMLList,
            String incluirGrupoProdPredMdfe,
            String dsProdutoPredominante,
            String cepCarregadoMDFE,
            String cepDescarregadoMDFE) {
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
        this.incluirQrcodeMdfe = incluirQrcodeMdfe; 
        this.linkQrCodeMdfe = linkQrCodeMdfe;
        this.autXMLList = autXMLList;
        this.incluirGrupoProdPredMdfe = incluirGrupoProdPredMdfe;
        this.dsProdutoPredominante = dsProdutoPredominante;
        this.cepCarregadoMDFE = cepCarregadoMDFE;
        this.cepDescarregadoMDFE = cepDescarregadoMDFE;
    }

    public com.mercurio.lms.mdfe.model.v300.MDFe convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
    	com.mercurio.lms.mdfe.model.v300.MDFe mdFe = new com.mercurio.lms.mdfe.model.v300.MDFe();
        
        //InfMDFe
        mdFe.setInfMDFe(new InfMDFeConverter(mdfe, proprietarioFilialOrigem, nrVersaoLayout, retiraZeroInicialIe, bairroPadrao, ambienteMdfe, monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao, contingencia, filiaisPercurso, 
        		ciot, cnpjCiot, segList, infContratanteList, autXMLList, incluirGrupoProdPredMdfe, dsProdutoPredominante, cepCarregadoMDFE, cepDescarregadoMDFE).convert(isViagem, isAgrupaPorUFDestino));
        //InfMDFe
        if("S".equalsIgnoreCase(incluirQrcodeMdfe)){
        	String qrCodMDFe = linkQrCodeMdfe.replace("{0}", mdFe.getInfMDFe().getId().replace("MDFe", "")).replace("{1}", ambienteMdfe);
        	mdFe.setInfMDFeSupl(new InfMDFeSuplConverter(qrCodMDFe).convert());
        }
        
        return mdFe;
    }
    
}
