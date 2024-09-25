package com.mercurio.lms.indenizacoes.swt.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.report.RelatorioIndenizacoesFranqueadosCSVService;

public class EmitirRelatorioIndenizacoesFranqueadosAction extends ReportActionSupport {

	private RelatorioIndenizacoesFranqueadosCSVService relatorioIndenizacoesFranqueadosCSVService;

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
		return this.relatorioIndenizacoesFranqueadosCSVService.execute(parameters);
	}
	
	
	public void setEmitirRelatorioIndenizacoesFranqueadosCSVService(RelatorioIndenizacoesFranqueadosCSVService relatorioIndenizacoesFranqueadosCSVService) {
		this.relatorioIndenizacoesFranqueadosCSVService = relatorioIndenizacoesFranqueadosCSVService;
	}
	
}
