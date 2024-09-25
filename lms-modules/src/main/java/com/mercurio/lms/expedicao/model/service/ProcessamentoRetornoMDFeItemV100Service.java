package com.mercurio.lms.expedicao.model.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.util.mdfe.type.TipoEventoMdfe;
import com.mercurio.lms.expedicao.model.ConnectorIntegrationCTE;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * @author tiagol
 *
 *         Não inserir documentação após ou remover a tag do XDoclet a seguir. O
 *         valor do <code>id</code> informado abaixo deve ser utilizado para
 *         referenciar este serviço.
 * @spring.bean id="lms.expedicao.processamentoRetornoMDFeItemV100Service"
 */
public class ProcessamentoRetornoMDFeItemV100Service {

    private Logger log = LogManager.getLogger(this.getClass());
	private ManifestoEletronicoService manifestoEletronicoService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	
    @SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeLeituraRetornoEmissao(final ConnectorIntegrationCTE connectorIntegrationCTE, String xml){
		StringReader sr = new StringReader(xml);
		try {

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				
			DocumentBuilder docBuilder = dbf.newDocumentBuilder();
				
	        Document doc = docBuilder.parse(new InputSource(sr));
	            
	        Element docElement = doc.getDocumentElement();
	            
	        Element chMdfe = (Element) docElement.getElementsByTagName("chMDFe").item(0);
	        Element nProt = (Element) docElement.getElementsByTagName("nProt").item(0);
	        Element xMotivo = (Element) docElement.getElementsByTagName("xMotivo").item(0);
				
			String chave = chMdfe.getTextContent();
				
			ManifestoEletronico mdfe = manifestoEletronicoService.findByNrChave(chave);
			if(mdfe != null) {
			    if (nProt.getTextContent() != null && Long.valueOf(nProt.getTextContent()) > 0) {

			        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_AUTORIZADO));
				    mdfe.setNrProtocolo(Long.valueOf(nProt.getTextContent()));
                    mdfe.setDsObservacaoSefaz(xMotivo.getTextContent());
                    
                    manifestoEletronicoService.store(mdfe);
		    
			    } else {

			        if (!ConstantesExpedicao.TP_MDFE_AUTORIZADO.equals(mdfe.getTpSituacao().getValue())) {

			            mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_REJEITADO));
		                mdfe.setDsObservacaoSefaz(xMotivo.getTextContent());
		                
		                manifestoEletronicoService.store(mdfe);
		                
			        }
                    
			    }
			    
			    
			}

	         integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());


		} catch (Exception e) {
			log.error(e);
		} finally {
			if(sr != null){
				sr.close();
			}
		}
		
	}
	
    @SuppressWarnings("unchecked")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeLeituraRetornoEvento(final ConnectorIntegrationCTE connectorIntegrationCTE, String xml) {
        StringReader sr = new StringReader(xml);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
                
            Document doc = docBuilder.parse(new InputSource(sr));
                
            Element docElement = doc.getDocumentElement();
                
            Element chMDFe = (Element) docElement.getElementsByTagName("chMDFe").item(0);
            Element nProt = (Element) docElement.getElementsByTagName("nProt").item(0);
            Element xMotivo = (Element) docElement.getElementsByTagName("xMotivo").item(0);
            Element tpEventoChave = (Element) docElement.getElementsByTagName("tpEvento").item(0);
            Element cStat = (Element) docElement.getElementsByTagName("cStat").item(0);
                
            String chave = chMDFe.getTextContent();
            String tpEvento = tpEventoChave.getTextContent();
                
            ManifestoEletronico mdfe = manifestoEletronicoService.findByNrChave(chave);

            if(mdfe != null){
                if(nProt != null && nProt.getTextContent() != null && Long.valueOf(nProt.getTextContent()) > 0){
                    if (TipoEventoMdfe.CANCELAMENTO.getTpEvento().equals(tpEvento)) {
                        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_CANCELADO));
                    } else {
                        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_ENCERRADO));
                        mdfe.setCdStatusEncerramento(Long.valueOf(cStat.getTextContent()));
                    }
                    mdfe.setNrProtocolo(Long.valueOf(nProt.getTextContent()));
                } else {
                    if (TipoEventoMdfe.CANCELAMENTO.getTpEvento().equals(tpEvento)) {
                        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_CANCELADO_REJEITADO));
                    } else {
                        mdfe.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_MDFE_ENCERRADO_REJEITADO));
                        mdfe.setCdStatusEncerramento(Long.valueOf(cStat.getTextContent()));
                    }
                }
                mdfe.setDsObservacaoSefaz(xMotivo.getTextContent());
                
                manifestoEletronicoService.store(mdfe);
            }

            integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());

        } catch (Exception e) {
            log.error(e);
        } finally {
            if(sr != null){
                sr.close();
            }
        }
    }

   public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}
   
   public void setManifestoEletronicoService(
        ManifestoEletronicoService manifestoEletronicoService) {
       this.manifestoEletronicoService = manifestoEletronicoService;
   }

}
