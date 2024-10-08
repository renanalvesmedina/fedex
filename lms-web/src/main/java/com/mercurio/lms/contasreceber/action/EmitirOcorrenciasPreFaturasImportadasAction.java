package com.mercurio.lms.contasreceber.action;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.lms.contasreceber.report.EmitirOcorrenciasPreFaturasImportadasService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirOcorrenciasPreFaturasImportadasAction"
 */

public class EmitirOcorrenciasPreFaturasImportadasAction extends ReportActionSupport {
	
	private ClienteService clienteService;
	
	/**
	 * Busca os Clientes para a Lookup de cliente
	 * @param map Crit�rios de pesquisa como nrIdentificacao do cliente
	 * @return Lista de clientes encontrados na pesquisa
	 */
	public List findLookupCliente(Map map){
		return clienteService.findLookup(map);
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	
	public void setEmitirOcorrenciasPreFaturasImportadasService(EmitirOcorrenciasPreFaturasImportadasService service){
		super.reportServiceSupport = service;
	}
	
}