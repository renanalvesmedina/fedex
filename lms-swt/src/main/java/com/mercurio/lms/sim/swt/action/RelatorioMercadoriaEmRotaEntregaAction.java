package com.mercurio.lms.sim.swt.action;

import static java.lang.Boolean.TRUE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.sim.model.service.ClienteUsuarioCCTService;
import com.mercurio.lms.sim.model.service.RelatorioMercadoriaEmRotaEntregaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class RelatorioMercadoriaEmRotaEntregaAction {
	
	private ClienteService clienteService;
	private ClienteUsuarioCCTService clienteUsuarioCCTService;
	private ReportExecutionManager reportExecutionManager;
	private RelatorioMercadoriaEmRotaEntregaService relatorioMercadoriaEmRotaEntregaService;

	private static final int INICIO_DIA_MES_DOIS_DIGITOS = 10;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findLookupCliente(Map criteria) {
		List<Map<String, Object>> clientes = clienteService.findClienteByNrIdentificacao((String) criteria.get("nrIdentificacao"));
		if (clientes != null) {
			for(Map cliente : clientes) {
				cliente.remove("tpCliente");
				Map pessoa = (Map) cliente.remove("pessoa");
				if (pessoa != null) {
					cliente.put("nmPessoa", pessoa.get("nmPessoa"));
					cliente.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
				}
			}
		}
		return clientes;
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
	
	public String execute(TypedFlatMap criteria) throws Exception {
	   	TypedFlatMap novoTypedFlatMap = montaTypedFlatMap(criteria);
	   	return reportExecutionManager.generateReportLocator(relatorioMercadoriaEmRotaEntregaService.executeExportacaoCsv(novoTypedFlatMap, reportExecutionManager.generateOutputDir()));
	}

	private TypedFlatMap montaTypedFlatMap(TypedFlatMap criteria) {
		if(criteria.get("dtInicio")!= null){
			criteria.put("dtInicio",formatYearMonthDayToString(criteria.getYearMonthDay("dtInicio")));
		}
		if(criteria.get("dtFim")!= null){
			criteria.put("dtFim",formatYearMonthDayToString(criteria.getYearMonthDay("dtFim")));
		}
		if(TRUE.equals(criteria.get("blClienteCCT"))){
			criteria.put("blClienteCCT",criteria.getBoolean("blClienteCCT"));
		}
		if(criteria.get("idCliente")!= null){
			criteria.put("idCliente",criteria.getLong("idCliente"));
		}
		return criteria;
	}
	
	public String formatYearMonthDayToString(YearMonthDay ymd){
		int day = ymd.getDayOfMonth();
		int month = ymd.getMonthOfYear();
		int year = ymd.getYear();
		
		return (day < INICIO_DIA_MES_DOIS_DIGITOS ? "0" + Integer.toString(day) : Integer.toString(day) ) + "/" + (month < INICIO_DIA_MES_DOIS_DIGITOS ? "0" + Integer.toString(month): Integer.toString(month)) + "/" + Integer.toString(year);
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public ClienteUsuarioCCTService getClienteUsuarioCCTService() {
		return clienteUsuarioCCTService;
	}

	public void setClienteUsuarioCCTService(
			ClienteUsuarioCCTService clienteUsuarioCCTService) {
		this.clienteUsuarioCCTService = clienteUsuarioCCTService;
	}

	public ReportExecutionManager getReportExecutionManager() {
		return reportExecutionManager;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public RelatorioMercadoriaEmRotaEntregaService getRelatorioMercadoriaEmRotaEntregaService() {
		return relatorioMercadoriaEmRotaEntregaService;
	}

	public void setRelatorioMercadoriaEmRotaEntregaService(
			RelatorioMercadoriaEmRotaEntregaService relatorioMercadoriaEmRotaEntregaService) {
		this.relatorioMercadoriaEmRotaEntregaService = relatorioMercadoriaEmRotaEntregaService;
	}
}
