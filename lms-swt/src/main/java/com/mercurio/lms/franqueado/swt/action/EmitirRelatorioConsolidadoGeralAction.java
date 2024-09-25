package com.mercurio.lms.franqueado.swt.action;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.report.RelatorioConsolidadoGeralCSVService;

public class EmitirRelatorioConsolidadoGeralAction {

	private RelatorioConsolidadoGeralCSVService relatorioConsolidadoGeralCSVService;
	

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
		return this.relatorioConsolidadoGeralCSVService.execute(parameters);
	}

	public void setRelatorioConsolidadoGeralCSVService(
			RelatorioConsolidadoGeralCSVService relatorioConsolidadoGeralCSVService) {
		this.relatorioConsolidadoGeralCSVService = relatorioConsolidadoGeralCSVService;
	}

}
