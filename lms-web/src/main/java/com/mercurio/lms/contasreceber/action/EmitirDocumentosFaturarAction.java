package com.mercurio.lms.contasreceber.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.report.DocumentosFaturarService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * @author Hector Julian Esnaola Junior
 * 
 * @spring.bean id="lms.contasreceber.emitirDocumentosFaturarAction"
 */

public class EmitirDocumentosFaturarAction extends ReportActionSupport {

	/** Inversion of control - Spring (DocumentosFaturarService) */
	public void setDocumentosFaturarService(DocumentosFaturarService documentosFaturarService) {
		this.reportServiceSupport = documentosFaturarService;
	}
	
	/** Inversion of control - Spring (FilialService) */
	private FilialService filialService;
	public void setFilialService(FilialService filialService){
		this.filialService = filialService;
	}
	
	/** Inversion of control - Spring (ClienteService) */
	private ClienteService clienteService;
	public void setClienteService(ClienteService clienteService){
		this.clienteService = clienteService;
	}
	
	/** Método invocado pela lookup de filial, para preenchimento da mesma */
	public List findLookupFilial(TypedFlatMap criteria){
        return this.filialService.findLookupBySgFilial(criteria);
    }
	
	/** Método invocado pela lookup de cliente, para preenchimento da mesma */
	public List findClienteLookup(Map criteria){
		return clienteService.findLookup(criteria);
	}
	
}
