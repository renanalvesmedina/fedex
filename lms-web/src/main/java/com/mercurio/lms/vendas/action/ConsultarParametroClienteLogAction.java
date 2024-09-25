package com.mercurio.lms.vendas.action;

import java.io.Serializable;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.service.GeneralidadeClienteLogService;
import com.mercurio.lms.vendas.model.service.ParametroClienteLogService;
import com.mercurio.lms.vendas.model.service.TaxaClienteLogService;

/**
 * @spring.bean id="lms.vendas.consultarParametroClienteLogAction"
 */
public class ConsultarParametroClienteLogAction extends CrudAction {
	TaxaClienteLogService taxaClienteLogService;
	GeneralidadeClienteLogService generalidadeClienteLogService;

	//Métodos de ParametroCliente
	public Serializable findById(TypedFlatMap criteria){
		return((ParametroClienteLogService)defaultService).findById(criteria.getLong("parametroCliente.idParametroCliente"));
	}

	//Metodos de GeneralidadeCliente
	public ResultSetPage findPaginatedGeneralidadeCliente(Map criteria) {
		return generalidadeClienteLogService.findPaginated(criteria);
	}

	public Integer getRowCountGeneralidadeCliente(Map criteria) {
		return generalidadeClienteLogService.getRowCount(criteria);
	}
	
	//Metodos de TaxaCliente
	public ResultSetPage findPaginatedTaxaCliente(Map criteria) {
		return taxaClienteLogService.findPaginated(criteria);
	}

	public Integer getRowCountTaxaCliente(Map criteria) {
		return taxaClienteLogService.getRowCount(criteria);
	}

	
	
	//Getters & Setters
	public void setService(ParametroClienteLogService service){
		this.defaultService = service;
	}
	
	public TaxaClienteLogService getTaxaClienteLogService() {
		return taxaClienteLogService;
	}

	public void setTaxaClienteLogService(TaxaClienteLogService taxaClienteLogService) {
		this.taxaClienteLogService = taxaClienteLogService;
	}

	public GeneralidadeClienteLogService getGeneralidadeClienteLogService() {
		return generalidadeClienteLogService;
	}

	public void setGeneralidadeClienteLogService(
			GeneralidadeClienteLogService generalidadeClienteLogService) {
		this.generalidadeClienteLogService = generalidadeClienteLogService;
	}
}