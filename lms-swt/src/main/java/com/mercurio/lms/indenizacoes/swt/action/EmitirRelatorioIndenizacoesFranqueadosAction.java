package com.mercurio.lms.indenizacoes.swt.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.report.RelatorioIndenizacoesFranqueadosCSVService;

public class EmitirRelatorioIndenizacoesFranqueadosAction extends ReportActionSupport {

	private RelatorioIndenizacoesFranqueadosCSVService relatorioIndenizacoesFranqueadosCSVService;

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
		return this.relatorioIndenizacoesFranqueadosCSVService.execute(parameters);
	}
	
	
	public void setEmitirRelatorioIndenizacoesFranqueadosCSVService(RelatorioIndenizacoesFranqueadosCSVService relatorioIndenizacoesFranqueadosCSVService) {
		this.relatorioIndenizacoesFranqueadosCSVService = relatorioIndenizacoesFranqueadosCSVService;
	}
	
}
