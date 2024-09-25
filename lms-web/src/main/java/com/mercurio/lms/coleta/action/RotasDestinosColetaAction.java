package com.mercurio.lms.coleta.action;

import java.util.Map;

import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.rotasDestinosColetaAction"
 */

public class RotasDestinosColetaAction {
	
	private DetalheColetaService detalheColetaService;
	
	public DetalheColetaService getDetalheColetaService() {
		return detalheColetaService;
	}
	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}


	public ResultSetPage findPaginated(Map criteria) {
		criteria.put("_currentPage", "1");
		criteria.put("_pageSize", "1000");
		ResultSetPage rsp = getDetalheColetaService().findPaginatedHorarioSaidaViagem(criteria);
		return rsp;
	}
}