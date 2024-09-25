package com.mercurio.lms.tributos.swt.action;


import java.io.File;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.tributos.report.EmitirIcmsSTConhecimentosService;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 *
 * @author Inacio G Klassmann
 *
 */
public class EmitirIcmsSTConhecimentosAction {
	private EmitirIcmsSTConhecimentosService emitirIcmsSTConhecimentosService;
	private ReportExecutionManager reportExecutionManager;
	private ClienteService clienteService;
	private InscricaoEstadualService inscricaoEstadualService;


	public void setEmitirIcmsSTConhecimentosService(EmitirIcmsSTConhecimentosService emitirIcmsSTConhecimentosService) {
		this.emitirIcmsSTConhecimentosService = emitirIcmsSTConhecimentosService;
	}

	public void setReportExecutionManager(ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	@SuppressWarnings("rawtypes")
	public String execute(Map filters) throws Exception {
		TypedFlatMap tfm = new TypedFlatMap();
		ReflectionUtils.flatMap(tfm, filters);

		if (JRReportDataObject.EXPORT_CSV.equalsIgnoreCase(tfm.getString("tpFormatoRelatorio"))) {
			File file = emitirIcmsSTConhecimentosService.executeReport(tfm);
			if (file == null) {
				throw new BusinessException("emptyReport");
			}
			return reportExecutionManager.generateReportLocator(file);
		} else {
			return this.reportExecutionManager.generateReportLocator(this.emitirIcmsSTConhecimentosService, tfm);
		}
	}


	public List<Map<String, Object>> findDadosCliente(Map<String, Object> criteria) {
		return findCliente((String) criteria.get("nrIdentificacao"));
	}


	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> findCliente(String nrIdentificacao) {
		List<Map<String, Object>> result = clienteService.findClienteByNrIdentificacao(nrIdentificacao);
		if (!result.isEmpty()) {
			Map<String, Object> cliente = (Map<String, Object>)result.get(0);
			Map<String, Object> pessoa = (Map<String, Object>) cliente.get("pessoa");
			pessoa.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
			cliente.put("nmPessoa", pessoa.get("nmPessoa"));
			cliente.put("nrIdentificacao", pessoa.get("nrIdentificacao"));
			cliente.put("inscricaoEstadual", findInscricaoEstadual((Long)cliente.get("idCliente")));
		}
		return result;
	}


	@SuppressWarnings({ "rawtypes" })
	private List findInscricaoEstadual(Long idPessoa) {
		if (idPessoa == null) {
			return null;
		}
		return inscricaoEstadualService.findByPessoa(idPessoa);
	}
}
