package com.mercurio.lms.expedicao.action;

import java.io.Serializable;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.gm.model.service.MonitoramentoEmbarqueGMService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manterMonitoramentoEmbarqueGMAction"
 */

public class ManterMonitoramentoEmbarqueGMAction extends CrudAction {
	private MonitoramentoEmbarqueGMService monitoramentoEmbarqueGMService;

	public void setMonitoramentoEmbarque(MonitoramentoEmbarqueGMService monitoramentoEmbarqueService) {
		this.defaultService = monitoramentoEmbarqueGMService;
	}
	
	@Override
	public void removeById(Serializable id) {
		super.removeById(id);
	}
	
	@Override
	public Serializable findById(Serializable id) {
		return super.findById(id);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage r = super.findPaginated(criteria);
		
		return r;
	}
	
	public void relatorioDiscrepancia() {
		
	}

	public MonitoramentoEmbarqueGMService getMonitoramentoEmbarqueGMService() {
		return monitoramentoEmbarqueGMService;
	}

	public void setMonitoramentoEmbarqueGMService(
			MonitoramentoEmbarqueGMService monitoramentoEmbarqueGMService) {
		this.monitoramentoEmbarqueGMService = monitoramentoEmbarqueGMService;
	}
	
	/**
	 * M�todo que retorna o ResultSetPage da grid da aba de detalhamento da tela
	 * de Monitoramento Embarque GM Sorocaba LMS-2781
	 * 
	 * @param criteria
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedMpc(Map criteria) {
		return this.monitoramentoEmbarqueGMService.findPaginatedDetalheEmbarqueGM(criteria);
	}
	
	/**
	 * M�todo que retorna o count da grid da aba de detalhamento  para a tela de Monitoramento Embarque GM Sorocaba
	 * LMS-2781
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountMpc(Map criteria) {
		return this.monitoramentoEmbarqueGMService.getRowCountMpc(criteria);
	}
	
}
