package com.mercurio.lms.tributos.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.service.ExcecaoICMSClienteService;
import com.mercurio.lms.tributos.model.service.RemetenteExcecaoICMSCliLogService;

/**
 * @spring.bean id="lms.tributos.consultarRemetenteExcecaoICMSCliLogAction"
 */
public class ConsultarRemetenteExcecaoICMSCliLogAction extends CrudAction {

	private ExcecaoICMSClienteService excecaoIcmsClienteService;
	
	public void setService(RemetenteExcecaoICMSCliLogService service) {

		this.defaultService = service;
	}

	public ResultSetPage findPaginatedTela(TypedFlatMap criteria) {
		
		if (!"".equals(criteria.getString("nrCNPJParcialRem"))) {
			// Formata o cnpj de acordo com o tipo selecionado na tela.
			String nrIdentificacao = excecaoIcmsClienteService.formatCnpj(criteria.get("nrCNPJParcialRem").toString(), criteria.get("tipoCnpj").toString());
			criteria.put("nrCnpjParcialRem", nrIdentificacao);
		}

		return ((RemetenteExcecaoICMSCliLogService) this.defaultService).findPaginated(criteria);
	}
}