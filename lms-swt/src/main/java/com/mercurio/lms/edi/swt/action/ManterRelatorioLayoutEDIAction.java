package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.service.ComposicaoLayoutEdiService;
import com.mercurio.lms.edi.report.RelatorioLayoutEdiService;

public class ManterRelatorioLayoutEDIAction {
	
	private ComposicaoLayoutEdiService composicaoLayoutEdiService;
	private RelatorioLayoutEdiService relatorioLayoutEdiService; 
	private ReportExecutionManager reportExecutionManager;
	
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = composicaoLayoutEdiService.findPaginatedReport(new PaginatedQuery(criteria));					
		
		List<ComposicaoLayoutEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
				
		// Adiciona somente os campos específicos do cliente ao resultado final
		HashSet<Long> campoLayoutId = new HashSet<Long>();
		for(ComposicaoLayoutEDI layout : list){
			if(layout.getClienteLayoutEDI() != null) {
				campoLayoutId.add(layout.getCampoLayout().getIdCampo());
			retorno.add(layout.getMapped());
		}
		}
						
		// Adiciona os campos do layout padrão (menos os específicos de mesmo campo)
		for(ComposicaoLayoutEDI layout : list){
			if(layout.getClienteLayoutEDI() == null && !campoLayoutId.contains(layout.getCampoLayout().getIdCampo())) {
				retorno.add(layout.getMapped());
			}
		}
						
		rsp.setList(retorno);
		return rsp;		
	}
	
	public String execute(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(relatorioLayoutEdiService, parameters);
	}	

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public RelatorioLayoutEdiService getRelatorioLayoutEdiService() {
		return relatorioLayoutEdiService;
	}

	public void setRelatorioLayoutEdiService(
			RelatorioLayoutEdiService relatorioLayoutEdiService) {
		this.relatorioLayoutEdiService = relatorioLayoutEdiService;
	}

	public ComposicaoLayoutEdiService getComposicaoLayoutEdiService() {
		return composicaoLayoutEdiService;
	}

	public void setComposicaoLayoutEdiService(
			ComposicaoLayoutEdiService composicaoLayoutEdiService) {
		this.composicaoLayoutEdiService = composicaoLayoutEdiService;
	}		
}