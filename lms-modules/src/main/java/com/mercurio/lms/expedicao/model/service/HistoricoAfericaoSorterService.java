package com.mercurio.lms.expedicao.model.service;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.expedicao.model.HistoricoAfericao;
import com.mercurio.lms.expedicao.model.dao.HistoricoAfericaoSorterDAO;
import com.mercurio.lms.municipios.model.service.FilialService;

@Assynchronous
public class HistoricoAfericaoSorterService extends CrudService<HistoricoAfericao, Long> {
	
	private ParametroGeralService parametroGeralService;
	private FilialService filialService;
	private EtiquetaAfericaoService etiquetaAfericaoService;
	private ReportExecutionManager reportExecutionManager;

	public void setHistoricoAfericaoSorterDAO(HistoricoAfericaoSorterDAO historicoAfericaoSorterDAO) {
		setDao( historicoAfericaoSorterDAO );
	}
	private HistoricoAfericaoSorterDAO getHistoricoAfericaoSorterDAO() {
		return (HistoricoAfericaoSorterDAO) getDao();
	}

	public HistoricoAfericao findByFilial(Long idFilial) {
		return getHistoricoAfericaoSorterDAO().findByFilial(idFilial);
	}

	public ResultSetPage findPaginatedHistoricoAfericaoSorter(TypedFlatMap criteria) {
		return getHistoricoAfericaoSorterDAO().findPaginatedHistoricoAfericaoSorter(criteria, FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCountHistoricoAfericaoSorter(TypedFlatMap criteria) {
		return getHistoricoAfericaoSorterDAO().getRowCountHistoricoAfericaoSorter(criteria);
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void storeHistoricoAfericaoFromIntegracao(HistoricoAfericao historicoAfericao, String sgFilial){
		historicoAfericao.setFilial(filialService.findBySgFilialAndTpEmpresa(sgFilial, "M"));
		historicoAfericao.setEtiquetaAfericao(etiquetaAfericaoService.findByNrCodBarra(historicoAfericao.getNrCodigoBarras()));
		store(historicoAfericao);
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setEtiquetaAfericaoService(EtiquetaAfericaoService etiquetaAfericaoService) {
		this.etiquetaAfericaoService = etiquetaAfericaoService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
}
