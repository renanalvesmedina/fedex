package com.mercurio.lms.sim.swt.action;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.sim.model.service.ClienteUsuarioCCTService;
import com.mercurio.lms.sim.model.service.RelatorioRiscoAgendamentoService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class RelatorioRiscoAgendamentoAction{
	
	private ClienteService clienteService;
	private RelatorioRiscoAgendamentoService relatorioRiscoAgendamentoService;
	private ReportExecutionManager reportExecutionManager;
	private ClienteUsuarioCCTService clienteUsuarioCCTService;
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findLookupCliente(Map criteria) {
		List<Map<String, Object>> clientes = clienteService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		
		if (clientes != null) {
			for(Map cliente : clientes) {
				cliente.remove("tpCliente");
				cliente.get("idcliente");
				Map pessoa = (Map) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
					
				}
			}
		}
		
		return clientes;
	}
	
	
	 public String executeExportacaoCsv(TypedFlatMap criteria) throws Exception {
	    TypedFlatMap novoTypedFlatMap = montaTypedFlatMapListagem(criteria);
	    return reportExecutionManager.generateReportLocator(relatorioRiscoAgendamentoService.executeExportacaoCsv(novoTypedFlatMap, reportExecutionManager.generateOutputDir()));
	 }
	
	public TypedFlatMap montaTypedFlatMapListagem(TypedFlatMap criteria){	
		// foi implementado desta forma pois o metodo do DAO recebe como string
		if(criteria.get("modal") == null){
			criteria.put("modal","");
		}
		return criteria;
	}
	
	public List findClienteCCTByUsuario(Map criteria){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		criteria = clienteUsuarioCCTService.findClienteCCTByUsuario(usuario.getIdUsuario());
			if(criteria.containsKey("nrIdentificacao")){
				result = findLookupCliente(criteria) ;
			}
		return result;
	}
		
		
	public ClienteService getClienteService() {
		return clienteService;
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public RelatorioRiscoAgendamentoService getRelatorioRiscoAgendamentoService() {
		return relatorioRiscoAgendamentoService;
	}

	public void setRelatorioRiscoAgendamentoService(
			RelatorioRiscoAgendamentoService relatorioRiscoAgendamentoService) {
		this.relatorioRiscoAgendamentoService = relatorioRiscoAgendamentoService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}


	public ClienteUsuarioCCTService getClienteUsuarioCCTService() {
		return clienteUsuarioCCTService;
	}


	public void setClienteUsuarioCCTService(
			ClienteUsuarioCCTService clienteUsuarioCCTService) {
		this.clienteUsuarioCCTService = clienteUsuarioCCTService;
	}
	
	
	
	
	
	
	
	

}
