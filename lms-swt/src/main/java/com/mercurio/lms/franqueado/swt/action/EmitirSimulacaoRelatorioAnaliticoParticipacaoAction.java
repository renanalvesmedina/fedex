package com.mercurio.lms.franqueado.swt.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.report.SimulacaoRelatorioAnaliticoParticipacaoCSVService;
import com.mercurio.lms.franqueados.report.SimulacaoRelatorioAnaliticoParticipacaoService;
import com.mercurio.lms.franqueados.util.FranqueadoReportUtils;

public class EmitirSimulacaoRelatorioAnaliticoParticipacaoAction extends ReportActionSupport {

	private SimulacaoRelatorioAnaliticoParticipacaoCSVService relatorioAnaliticoParticipacaoCSVService;

	/**
	 * M�todo respons�vel pela execu��o dos relat�rios.
	 * Caso o tipo do relat�rio seja PDF o retorno sempre ser� uma lista vazia.
	 * Pois o m�todo generateReportLocator cumpre a fun��o de exibir ao usu�rio o relat�rio. 
	 * 
	 * @param parameters
	 * @return
	 * @throws Exception
	 */

	public Object executeSWT(TypedFlatMap parameters) throws Exception {
		if(parameters.containsKey("exportType") && parameters.getString("exportType").equals(FranqueadoReportUtils.EXPORT_TYPE_PDF)){
			return this.executeGenerateLocator(parameters);
		} else {
			return this.relatorioAnaliticoParticipacaoCSVService.execute(parameters);
		}

	}
	
	
	public void setEmitirRelatorioSinteticoParticipacaoCSVService(SimulacaoRelatorioAnaliticoParticipacaoCSVService relatorioAnaliticoParticipacaoCSVService) {
		this.relatorioAnaliticoParticipacaoCSVService = relatorioAnaliticoParticipacaoCSVService;
	}
	
	
	public void setEmitirRelatorioSinteticoParticipacaoService(SimulacaoRelatorioAnaliticoParticipacaoService relatorioAnaliticoParticipacaoService) {
		this.reportServiceSupport = relatorioAnaliticoParticipacaoService;
	}

}
