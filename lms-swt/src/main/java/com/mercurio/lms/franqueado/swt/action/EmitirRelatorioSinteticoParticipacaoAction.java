package com.mercurio.lms.franqueado.swt.action;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;
import com.mercurio.lms.franqueados.model.service.DoctoServicoFranqueadoService;
import com.mercurio.lms.franqueados.model.service.FranqueadoFranquiaService;
import com.mercurio.lms.franqueados.report.RelatorioSinteticoParticipacaoResumoMesService;
import com.mercurio.lms.util.JTDateTimeUtils;

public class EmitirRelatorioSinteticoParticipacaoAction extends ReportActionSupport {

	private ReportExecutionManager reportExecutionManager;
	private FranqueadoFranquiaService franqueadoFranquiaService;
	private DoctoServicoFranqueadoService doctoServicoFranqueadoService;

	
	public String executeSWT(TypedFlatMap parameters) throws Exception {
		File reportFile;
		MultiReportCommand mrc = new MultiReportCommand("relatorioSinteticoParticipacao"); 

		
		if(parameters.containsKey("idFilial") && parameters.get("idFilial") != null){
			executeQuery(parameters);
			
			if(parameters.containsKey("idRelatorio") && StringUtils.isNotEmpty((String) parameters.get("idRelatorio"))){
				if(parameters.get("idRelatorio").equals("1")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoMesService", parameters);
				if(parameters.get("idRelatorio").equals("2")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoFretesVisaoGerencialService", parameters);
				if(parameters.get("idRelatorio").equals("3")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoLancamentosDiversosService", parameters);
				if(parameters.get("idRelatorio").equals("4")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService", parameters);
				if(parameters.get("idRelatorio").equals("5")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoOperacaoTipoFreteService", parameters);
				if(parameters.get("idRelatorio").equals("6")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoTrackingAnualService", parameters);
				if(parameters.get("idRelatorio").equals("7")) mrc.addCommand("lms.franqueados.relatorioSinteticoOperacaoTipoFreteAnualService", parameters);
			}else{
				mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoMesService", parameters);
				mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoFretesVisaoGerencialService", parameters);
				mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoLancamentosDiversosService", parameters);
				mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService", parameters);
				mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoOperacaoTipoFreteService", parameters);
				mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoTrackingAnualService", parameters);
				mrc.addCommand("lms.franqueados.relatorioSinteticoOperacaoTipoFreteAnualService", parameters);
			}
		} else {
			List<FranqueadoFranquia> franqueadoFranquiasList = franqueadoFranquiaService.findFranqueadoFranquiasVigentes(JTDateTimeUtils.getDataAtual());
			
			for(FranqueadoFranquia franqueadoFranquia : franqueadoFranquiasList){

				TypedFlatMap map = new TypedFlatMap();
				map.putAll(parameters);
				map.put("idFilial", franqueadoFranquia.getFranquia().getIdFranquia());
				map.put("dsFranquia", franqueadoFranquia.getFranquia().getFilial().getSgFilial());
				
				executeQuery(map);
				
				if(parameters.containsKey("idRelatorio") && StringUtils.isNotEmpty((String) parameters.get("idRelatorio"))){
					if(parameters.get("idRelatorio").equals("1")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoMesService", map);
					if(parameters.get("idRelatorio").equals("2")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoFretesVisaoGerencialService", map);
					if(parameters.get("idRelatorio").equals("3")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoLancamentosDiversosService", map);
					if(parameters.get("idRelatorio").equals("4")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService", map);
					if(parameters.get("idRelatorio").equals("5")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoOperacaoTipoFreteService", map);
					if(parameters.get("idRelatorio").equals("6")) mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoTrackingAnualService", map);
					if(parameters.get("idRelatorio").equals("7")) mrc.addCommand("lms.franqueados.relatorioSinteticoOperacaoTipoFreteAnualService", map);
				}else{
					mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoMesService", map);
					mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoFretesVisaoGerencialService", map);
					mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoLancamentosDiversosService", map);
					mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoExtratoInformacaoEmissaoDocumentacaoFiscalService", map);
					mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoResumoOperacaoTipoFreteService", map);
					mrc.addCommand("lms.franqueados.relatorioSinteticoParticipacaoTrackingAnualService", map);
					mrc.addCommand("lms.franqueados.relatorioSinteticoOperacaoTipoFreteAnualService", map);
				}
			}
		}
		reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			
		return reportExecutionManager.generateReportLocator(reportFile); 
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

	public void setFranqueadoFranquiaService(
			FranqueadoFranquiaService franqueadoFranquiaService) {
		this.franqueadoFranquiaService = franqueadoFranquiaService;
	}

	public void setDoctoServicoFranqueadoService(
			DoctoServicoFranqueadoService doctoServicoFranqueadoService) {
		this.doctoServicoFranqueadoService = doctoServicoFranqueadoService;
	}
}
