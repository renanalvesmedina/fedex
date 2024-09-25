package com.mercurio.lms.franqueado.swt.action;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.report.RelatorioConsolidadoGeralCSVService;

public class EmitirRelatorioConsolidadoGeralAction {

	private RelatorioConsolidadoGeralCSVService relatorioConsolidadoGeralCSVService;
	

	/**
	 * Método responsável pela execução dos relatórios.
	 * Caso o tipo do relatório seja PDF o retorno sempre será uma lista vazia.
	 * Pois o método generateReportLocator cumpre a função de exibir ao usuário o relatório. 
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
