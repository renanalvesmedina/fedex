package com.mercurio.lms.vendas.action;

import java.io.File;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.report.EmitirTabelaPropostaService;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.vendas.emitirTabelaPropostaAction"
 */
public class EmitirTabelaPropostaAction {
	private EmitirTabelaPropostaService emitirTabelaPropostaService;
	private EmitirTabelasClienteAction emitirTabelasClienteAction;

	public File execute(TypedFlatMap parameters) throws Exception {
		return emitirTabelaPropostaService.executePDF(parameters);
		}

	
	/**
	 * Analisa se deve ser emitida uma simulação do relatório.
	 * FIXME melhorar esta documentação, não sei porque destes testes == null, para cada parametro.
	 * @return
	 */
	private boolean emiteSimulacao() {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();

		if(simulacao == null)
			return false;
		if(simulacao.getTabelaPreco() == null)
			return false;
		if(simulacao.getClienteByIdCliente() == null)
			return false;
		if(simulacao.getParametroClientes() == null)
			return false;
		if(simulacao.getServico() == null)
			return false;

		return true;
	}

	public void setEmitirTabelaPropostaService(EmitirTabelaPropostaService emitirTabelaPropostaService) {
		this.emitirTabelaPropostaService = emitirTabelaPropostaService;
	}
	public EmitirTabelasClienteAction getEmitirTabelasClienteAction() {
		return emitirTabelasClienteAction;
}

	public void setEmitirTabelasClienteAction(
			EmitirTabelasClienteAction emitirTabelasClienteAction) {
		this.emitirTabelasClienteAction = emitirTabelasClienteAction;
	}

	
}
