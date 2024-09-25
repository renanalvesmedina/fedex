package com.mercurio.lms.vendas.action;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.service.HistoricoReajusteClienteService;

/**
 * @author André Valadas
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterHistoricoReajusteClienteAction"
 */
public class ManterHistoricoReajusteClienteAction extends CrudAction {

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getHistoricoReajusteClienteService().findPaginated(criteria);
	}
	public Integer getRowCount(TypedFlatMap criteria) {
		return getHistoricoReajusteClienteService().getRowCount(criteria);
	}

	public void setHistoricoReajusteClienteService(HistoricoReajusteClienteService serviceService) {
		super.defaultService = serviceService;
	}
	public HistoricoReajusteClienteService getHistoricoReajusteClienteService() {
		return (HistoricoReajusteClienteService) super.defaultService;
	}
}