package com.mercurio.lms.franqueado.swt.action;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.report.RelatorioPendenciaPagamentoCSVService;

public class EmitirRelatorioPendenciaPagamentoAction {

	private RelatorioPendenciaPagamentoCSVService relatorioPendenciaPagamentoCSVService;
	

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
		return this.relatorioPendenciaPagamentoCSVService.execute(parameters);
	}

	public void setRelatorioPendenciaPagamentoCSVService(
			RelatorioPendenciaPagamentoCSVService relatorioPendenciaPagamentoCSVService) {
		this.relatorioPendenciaPagamentoCSVService = relatorioPendenciaPagamentoCSVService;
	}

}
