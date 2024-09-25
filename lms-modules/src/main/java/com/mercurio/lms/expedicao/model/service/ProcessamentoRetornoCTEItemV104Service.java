package com.mercurio.lms.expedicao.model.service;

import java.io.StringReader;

import org.exolab.castor.xml.Unmarshaller;
import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.cte.model.v104.InfCanc;
import com.mercurio.lms.cte.model.v104.InfInut;
import com.mercurio.lms.cte.model.v104.InfProt;
import com.mercurio.lms.cte.model.v104.RetCancCTe;
import com.mercurio.lms.cte.model.v104.RetInutCTe;
import com.mercurio.lms.expedicao.model.ConnectorIntegrationCTE;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.util.CastorSingleton;


/**
 * @author JonasFE
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.integracaoNDDigitalService"
 */
public class ProcessamentoRetornoCTEItemV104Service {
	
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	
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
				monitoramentoDocEletronico.setDsObservacao(infCanc.getXMotivo());
				
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

	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}

	public void setIntegracaoNDDigitalService(
			IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}
	
}