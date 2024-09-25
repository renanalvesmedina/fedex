package com.mercurio.lms.tributos.action;

import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.tributos.model.ExcecaoICMSClienteLog;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSClienteLogService;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSClienteService;

/**
 * @spring.bean id="lms.tributos.consultarExcecaoICMSClienteLogAction"
 */
public class ConsultarExcecaoICMSClienteLogAction extends CrudAction {
	
	private ExcecaoICMSClienteService excecaoICMSClienteService;

	public void setService(ExcecaoICMSClienteLogService service) {

		this.defaultService = service;
	}
	
	public void setExcecaoICMSClienteService(ExcecaoICMSClienteService excecaoICMSClienteService) {
		
		this.excecaoICMSClienteService = excecaoICMSClienteService;
	}
	
	public ResultSetPage<ExcecaoICMSClienteLog> findPaginated(Map tfm) {
		
		if (!"".equals(tfm.get("nrCNPJParcialDev").toString())) {
			// Formata o cnpj de acordo com o tipo selecionado na tela.
			String nrIdentificacao = this.excecaoICMSClienteService.formatCnpj(
					tfm.get("nrCNPJParcialDev").toString(), 
					tfm.get("tipoCnpj").toString());
			tfm.put("nrCNPJParcialDev", nrIdentificacao);
		}

		return ((ExcecaoICMSClienteLogService) this.defaultService).findPaginated(tfm);
	}
}