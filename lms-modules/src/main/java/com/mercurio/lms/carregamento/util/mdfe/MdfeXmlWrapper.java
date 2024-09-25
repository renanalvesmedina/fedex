package com.mercurio.lms.carregamento.util.mdfe;

import java.io.IOException;
import java.util.List;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ResolverException;
import org.exolab.castor.xml.ValidationException;

import com.mercurio.lms.carregamento.util.mdfe.converter.v100a.MdfeConverter;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100a.MDFe;
import com.mercurio.lms.mdfe.model.v300.AutXML;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.mdfe.model.v300.Seg;
import com.mercurio.lms.util.CastorMarshaller;


public class MdfeXmlWrapper {

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
	
	private String cepCarregadoMDFE;
	
	private String cepDescarregadoMDFE;
    
    public MdfeXmlWrapper(ManifestoEletronico mdfe, 
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

    public XmlMdfe generate(boolean isViagem, boolean isAgrupaPorUFDestino) throws ResolverException, MarshalException, ValidationException, IOException {
    	
    	XmlMdfe toReturn = new XmlMdfe();
    	StringBuffer xml = null;
    			
    	if ("3.00".equals(nrVersaoLayout)) {
    		com.mercurio.lms.mdfe.model.v300.MDFe mdFe = convert300(isViagem, isAgrupaPorUFDestino);
    		toReturn.setChave(mdFe.getInfMDFe().getId().replace("MDFe", ""));
    		xml = CastorMarshaller.marshall(mdFe);
    	} else if ("1.00a".equals(nrVersaoLayout)) {
    		MDFe mdFe = convert100a(isViagem, isAgrupaPorUFDestino);
    		toReturn.setChave(mdFe.getInfMDFe().getId().replace("MDFe", ""));
    		xml = CastorMarshaller.marshall(mdFe);
    	} else {
    		com.mercurio.lms.mdfe.model.v100.MDFe mdFe = convert100();
    		toReturn.setChave(mdFe.getInfMDFe().getId().replace("MDFe", ""));
    		xml = CastorMarshaller.marshall(mdFe);
    	}
        
        toReturn.setXml(xml);
        
        return toReturn;
    }
    
    private com.mercurio.lms.mdfe.model.v300.MDFe convert300(boolean isViagem, boolean isAgrupaPorUFDestino) {
        return new com.mercurio.lms.carregamento.util.mdfe.converter.v300.MdfeConverter(mdfe,proprietarioFilialOrigem,nrVersaoLayout,retiraZeroInicialIe,bairroPadrao,ambienteMdfe,monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao, contingencia, filiaisPercurso, 
        		ciot, cnpjCiot, segList, infContratanteList, incluirQrcodeMdfe, linkQrCodeMdfe, autXMLList, incluirGrupoProdPredMdfe, dsProdutoPredominante, cepCarregadoMDFE, cepDescarregadoMDFE).convert(isViagem, isAgrupaPorUFDestino);
    }

    private MDFe convert100a(boolean isViagem, boolean isAgrupaPorUFDestino) {
        return new MdfeConverter(mdfe,proprietarioFilialOrigem,nrVersaoLayout,retiraZeroInicialIe,bairroPadrao,ambienteMdfe,monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao, contingencia, filiaisPercurso, ciot).convert(isViagem, isAgrupaPorUFDestino);
    }
    
    private com.mercurio.lms.mdfe.model.v100.MDFe convert100() {
    	return new com.mercurio.lms.carregamento.util.mdfe.converter.MdfeConverter(mdfe,proprietarioFilialOrigem,nrVersaoLayout,retiraZeroInicialIe,bairroPadrao,ambienteMdfe,monitoramentoDocEltronicoCtes, utilizarTagXMunCarrega, utilizarTagVeicTracao).convert();
    }

}
