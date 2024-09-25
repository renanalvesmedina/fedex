package com.mercurio.lms.franqueado.swt.action;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.report.RelatorioFinanceiroService;

public class EmitirRelatorioFinanceiroAction extends ReportActionSupport {

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
		return this.executeGenerateLocator(parameters);
	}
	
	public void setEmitirRelatorioFinanceiroService(RelatorioFinanceiroService relatorioFinanceiroService) {
		this.reportServiceSupport = relatorioFinanceiroService;
	}

}
