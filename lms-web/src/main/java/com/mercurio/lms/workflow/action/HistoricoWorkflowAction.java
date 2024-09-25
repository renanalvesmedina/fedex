package com.mercurio.lms.workflow.action;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;

public class HistoricoWorkflowAction extends CrudAction {
	private HistoricoWorkflowService historicoWorkflowService;
	private UsuarioService usuarioService;
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginated(TypedFlatMap criteria){
		return historicoWorkflowService.findPaginated(criteria);
	}
	
	public Integer getRowCount(TypedFlatMap criteria){
		return historicoWorkflowService.getRowCount(criteria);
	}

	public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
		return usuarioService.findLookupUsuarioFuncionario(tfm.getLong("idUsuario"), tfm.getString("nrMatricula"), null, null, null, null, true);
	}
	
	public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
		this.historicoWorkflowService = historicoWorkflowService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

}
