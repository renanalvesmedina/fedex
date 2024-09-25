package com.mercurio.lms.franqueado.swt.action;

import java.io.File;
import java.util.List;

import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.service.SimulacaoDoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.report.RelatorioSinteticoParticipacaoResumoMesService;

public class EmitirSimulacaoRelatorioSinteticoParticipacaoAction extends ReportActionSupport {

	private ReportExecutionManager reportExecutionManager;
	private SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService;

	
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		File reportFile = null;
		MultiReportCommand mrc = null;

		executeQuery(parameters);
			
		mrc = addCommands(parameters);
		
		if(mrc != null){
			reportFile = this.reportExecutionManager.executeMultiReport(mrc);
		}
		
		return reportExecutionManager.generateReportLocator(reportFile); 
	}

	private MultiReportCommand addCommands(TypedFlatMap parameters) {
		MultiReportCommand mrc = new MultiReportCommand("relatorioSinteticoParticipacao"); 

		mrc.addCommand("lms.franqueados.simulacaoRelatorioSinteticoParticipacaoResumoMesService", parameters);
		mrc.addCommand("lms.franqueados.simulacaoRelatorioSinteticoParticipacaoFretesVisaoGerencialService", parameters);
		mrc.addCommand("lms.franqueados.simulacaoRelatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService", parameters);
		mrc.addCommand("lms.franqueados.simulacaoRelatorioSinteticoParticipacaoResumoOperacaoTipoFreteService", parameters);	

		return mrc;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void executeQuery(TypedFlatMap parameters) {
		List defaultQuery = doctoServicoFranqueadoService.findRelatorioSinteticoDefault(parameters,false);
	
		parameters.put("defaultQuery", defaultQuery);
	}
	
	public void setEmitirRelatorioSinteticoParticipacaoService(RelatorioSinteticoParticipacaoResumoMesService emitirRelatorioSinteticoParticipacaoService) {
		this.reportServiceSupport = emitirRelatorioSinteticoParticipacaoService;
	}
	
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}

	public void setDoctoServicoFranqueadoService(
			SimulacaoDoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
}
