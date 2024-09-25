package com.mercurio.lms.workflow.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.workflow.model.HistoricoWorkflow;
import com.mercurio.lms.workflow.model.dao.HistoricoWorkflowDAO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;

/**
 * 
 * 
 * @spring.bean id="lms.workflow.historicoWorkflowService"
 */
public class HistoricoWorkflowService extends CrudService<HistoricoWorkflow, Long> {
	private ReportExecutionManager reportExecutionManager;
	private WorkflowPendenciaService workflowPendenciaService;
	
	public void setHistoricoWorkflowDAO(HistoricoWorkflowDAO historicoWorkflowDAO) {
		setDao(historicoWorkflowDAO);
	}
	
	private HistoricoWorkflowDAO getHistoricoWorkflowDAO() {
        return (HistoricoWorkflowDAO) getDao();
    }
	
	public HistoricoWorkflow findById(java.lang.Long id) {
		return (HistoricoWorkflow) super.findById(id);
	}

	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(HistoricoWorkflow historicoWorkflow) {
		return super.store(historicoWorkflow);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getHistoricoWorkflowDAO().findPaginated(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria){
		return getHistoricoWorkflowDAO().getRowCount(criteria);
	}
			
	public String findRelatorioHistoricoWorkflow(TypedFlatMap criteria) {
		if (criteria == null || criteria.isEmpty() || filtrosVazio(criteria)) {
			throw new BusinessException("LMS-00055");
		}
		
		List<Map<String, Object>> listForCSV = this.getHistoricoWorkflowDAO().findRelatorioHistoricoWorkflow(criteria);
		return reportExecutionManager.generateReportLocator(listForCSV, Boolean.TRUE);
	}
	
	private Boolean filtrosVazio(TypedFlatMap criteria){
		String[] filtros = new String[]{"cliente", "filial", "idRegional", "solicitante", "tpCampoWorkflow", "periodoInicial", "periodoFinal"};
		for (int i = 0; i < filtros.length; i++) {
			if(criteria.get(filtros[i]) != null && !"".equals(criteria.get(filtros[i]).toString())){
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Busca pendências de aprovação do workflow para a tabela e campos informados.
	 * 
	 * @param idProcesso
	 * @param tabelaHistoricoWorkflow
	 * @param camposWK
	 * @return
	 */
	public Map<String, Boolean> findPendenciaAprovacaoByTabelaCampos(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow, List<String> camposWK) {
		Map<String, Boolean> mapRetorno = new HashMap<String, Boolean>();
		List listaCampos = this.getHistoricoWorkflowDAO().findPendenciaAprovacaoByTabelaCampos(idProcesso, tabelaHistoricoWorkflow, camposWK);

		for (String tpCampoWK : camposWK) {
			mapRetorno.put(tpCampoWK, listaCampos.contains(tpCampoWK));
		}
		
		return mapRetorno;
	}
	
	/**
	 * Retorna verdadeiro caso existam Pendências do Workflow "Em Aprovação".
	 * 
	 * @param idProcesso
	 * @param tabelaHistoricoWorkflow
	 * @param campoHistoricoWorkflow
	 * @return
	 */
	public Boolean validateWorkflowPendenciaAprovacao(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow,
			CampoHistoricoWorkflow campoHistoricoWorkflow) {
		return this.getHistoricoWorkflowDAO().validateWorkflowPendenciaAprovacao(idProcesso, tabelaHistoricoWorkflow, campoHistoricoWorkflow);
	}

	/**
	 * Retorna verdadeiro se existe HistoricoWorkflow para a "tabela" e "campo" informados.
	 * 
	 * @param idProcesso
	 * @param tabelaHistoricoWorkflow
	 * @return
	 */
	public Boolean validateHistoricoWorkflow(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow, CampoHistoricoWorkflow campoHistoricoWorkflow){
		return this.getHistoricoWorkflowDAO().validateHistoricoWorkflow(idProcesso, tabelaHistoricoWorkflow, campoHistoricoWorkflow);
	}
	
	/**
	 * Retorna verdadeiro se existe HistoricoWorkflow para a "tabela" informada.
	 * 
	 * @param idProcesso
	 * @param tabelaHistoricoWorkflow
	 * @return
	 */
	public Boolean validateHistoricoWorkflow(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow){
		return this.validateHistoricoWorkflow(idProcesso, tabelaHistoricoWorkflow, null);
	}
	
	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	
	public Long findIdPendencia(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow) {
		return this.getHistoricoWorkflowDAO().findIdPendencia(idProcesso, tabelaHistoricoWorkflow);
	}
	
	public void cancelWorkflow(Long idProcesso, TabelaHistoricoWorkflow tabelaHistoricoWorkflow) {
		Long idPendencia = this.findIdPendencia(idProcesso, tabelaHistoricoWorkflow);
		this.getWorkflowPendenciaService().cancelPendencia(idPendencia);
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	
	public List<HistoricoWorkflow> findByIdPendencia(Long idPendencia) {
		return getHistoricoWorkflowDAO().findByIdPendencia(idPendencia);
	}
}