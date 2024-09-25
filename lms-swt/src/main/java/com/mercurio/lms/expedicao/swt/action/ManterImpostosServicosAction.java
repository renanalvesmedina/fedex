package com.mercurio.lms.expedicao.swt.action;

import java.util.Map;

import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.service.ImpostoServicoService;

public class ManterImpostosServicosAction {
	private ImpostoServicoService impostoServicoService;

    public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
    	return impostoServicoService.findPaginated(new PaginatedQuery(criteria));
	}

	public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
		this.impostoServicoService = impostoServicoService;
	}
}