package com.mercurio.lms.expedicao.model.service;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.Unmarshaller;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.cte.model.v200.InfCanc;
import com.mercurio.lms.cte.model.v200.InfInut;
import com.mercurio.lms.cte.model.v200.InfProt;
import com.mercurio.lms.cte.model.v200.RetCancCTe;
import com.mercurio.lms.cte.model.v200.RetInutCTe;
import com.mercurio.lms.expedicao.model.ConnectorIntegrationCTE;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.util.CastorSingleton;


/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.integracaoNDDigitalService"
 */
public class ProcessamentoRetornoCTEItemV200Service {
	
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private CartaCorrecaoEletronicaService cartaCorrecaoEletronicaService;
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeLeituraRetornoInutilizacao(final ConnectorIntegrationCTE connectorIntegrationCTE) {
		try {
			String xml = String.valueOf(connectorIntegrationCTE.getDocumentData());
			StringReader sr = new StringReader(xml);
			
			Unmarshaller unmarshaller = CastorSingleton.getInstance().getContext().createUnmarshaller();
			unmarshaller.setClass(RetInutCTe.class);
			RetInutCTe retInutCTe = (RetInutCTe) unmarshaller.unmarshal(sr);
			
			InfInut infInut = retInutCTe.getInfInut();
			String chave = infInut.getId().substring(2);
			MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findByNrChave(chave);
			if(monitoramentoDocEletronico != null){
				if(infInut.getNProt() == null || Long.valueOf(infInut.getNProt()) == 0){
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("J"));
					monitoramentoDocEletronico.setNrProtocolo(Long.valueOf(infInut.getNProt()));
				} else {
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("I"));
				}
				monitoramentoDocEletronico.setDsObservacao(infInut.getXMotivo());
				
				monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
			}
			integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());
		} catch (Exception e) {
			//TODO
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeLeituraRetornoCancelamento(final ConnectorIntegrationCTE connectorIntegrationCTE) {
		try {
			String xml = String.valueOf(connectorIntegrationCTE.getDocumentData());
			StringReader sr = new StringReader(xml);
			
			Unmarshaller unmarshaller = CastorSingleton.getInstance().getContext().createUnmarshaller();
			unmarshaller.setClass(RetCancCTe.class);
			RetCancCTe retCancCTe = (RetCancCTe) unmarshaller.unmarshal(sr);
			
			InfCanc infCanc = retCancCTe.getInfCanc();
			String chave = infCanc.getChCTe();
			MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findByNrChave(chave);
			if(monitoramentoDocEletronico != null){
				if(infCanc.getNProt() == null || Long.valueOf(infCanc.getNProt()) == 0){
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("D"));
					monitoramentoDocEletronico.setNrProtocolo(Long.valueOf(infCanc.getNProt()));
				} else {
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("C"));
				}
				monitoramentoDocEletronico.setDsObservacao(infCanc.getXJust());
				
				monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
			}
			integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());
		} catch (Exception e) {
			//TODO
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void executeLeituraRetornoEmissao(final ConnectorIntegrationCTE connectorIntegrationCTE){
		try {
			String xml = String.valueOf(connectorIntegrationCTE.getDocumentData());
			StringReader sr = new StringReader(xml);
			
			Unmarshaller unmarshaller = CastorSingleton.getInstance().getContext().createUnmarshaller();
			unmarshaller.setClass(InfProt.class);
			InfProt infProt = (InfProt) unmarshaller.unmarshal(sr);
			
			String chave = infProt.getChCTe();
			MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findByNrChave(chave);
			if(monitoramentoDocEletronico != null){
				if(infProt.getNProt() == null || Long.valueOf(infProt.getNProt()) == 0){
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("R"));
					monitoramentoDocEletronico.setNrProtocolo(Long.valueOf(infProt.getNProt()));
				} else {
					monitoramentoDocEletronico.setTpSituacaoDocumento(new DomainValue("R"));
				}
				monitoramentoDocEletronico.setDsObservacao(infProt.getXMotivo());
				
				if (monitoramentoDocEletronico.getDsEnvioEmail() != null ) {
			    	monitoramentoDocEletronico.setBlEnviadoCliente(Boolean.TRUE);
			    	monitoramentoDocEletronico.setDhEnvio(new DateTime());
			    }
				
				monitoramentoDocEletronicoService.store(monitoramentoDocEletronico);
			}
			integracaoNDDigitalService.updateStatusConnectorIntegration(connectorIntegrationCTE.getConnectorIntegrationCTEID());
		} catch (Exception e) {
			//TODO
		}
		
	}
	
	/**
	 * Processa integração para retorno de evento de Carta de Correção
	 * Eletrônica.
	 * 
	 * @param connectorIntegrationCTE
	 *            Conector para integração
	 * @param xml
	 *            Conteúdo XML da Carta de Correção Eletrônica
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public TypedFlatMap executeLeituraRetornoCartaCorrecaoEletronica(
			ConnectorIntegrationCTE connectorIntegrationCTE, String xml) {
		StringReader reader = new StringReader(xml);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(reader));
			
			Element infEvento = (Element) document.getElementsByTagName("infEvento").item(0);
			Element chCTe = (Element) infEvento.getElementsByTagName("chCTe").item(0);
			Element nProt = (Element) document.getElementsByTagName("nProt").item(0);
			Element xMotivo = (Element) document.getElementsByTagName("xMotivo").item(0);
			if (nProt == null || xMotivo == null) {
				return null;
			}
			
			String nrChave = chCTe.getTextContent();
			String nrProtocolo = nProt.getTextContent();
			String dsObservacao = xMotivo.getTextContent();
			
			MonitoramentoDocEletronico monitoramentoDocEletronico =
					monitoramentoDocEletronicoService.findByNrChave(nrChave);
			DoctoServico doctoServico = monitoramentoDocEletronico.getDoctoServico();
			Long idDoctoServico = doctoServico.getIdDoctoServico();
			
			TypedFlatMap map = new TypedFlatMap();
			if (StringUtils.isEmpty(nrProtocolo) || Long.valueOf(nrProtocolo) == 0) {
				cartaCorrecaoEletronicaService.updateRejeitadaByIdDoctoServico(
						idDoctoServico, dsObservacao);
				map.put("dsObservacao", dsObservacao);
			} else {
				cartaCorrecaoEletronicaService.updateAutorizadaByIdDoctoServico(
						idDoctoServico, Long.valueOf(nrProtocolo), dsObservacao);
			}
			
			integracaoNDDigitalService.updateStatusConnectorIntegration(
					connectorIntegrationCTE.getConnectorIntegrationCTEID());
			return map;
		} catch (ParserConfigurationException e) {
			throw new InfrastructureException(e);
		} catch (SAXException e) {
			throw new InfrastructureException(e);
		} catch (IOException e) {
			throw new InfrastructureException(e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setIntegracaoNDDigitalService(
			IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

	public void setCartaCorrecaoEletronicaService(
			CartaCorrecaoEletronicaService cartaCorrecaoEletronicaService) {
		this.cartaCorrecaoEletronicaService = cartaCorrecaoEletronicaService;
	}
	
}