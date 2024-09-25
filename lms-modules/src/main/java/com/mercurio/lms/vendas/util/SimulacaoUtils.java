/**
 * 
 */
package com.mercurio.lms.vendas.util;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Simulacao;

/**
 * Classe utilitaria para manipulacao se Simulacoes.
 * 
 * @author luisp
 *
 */
public class SimulacaoUtils {

	/**
	 * <p>
	 * Retorna o objeto Simulacao armazenado na sessao.
	 * 
	 * <p>
	 * Caso nao exista uma Simulacao na sessao serah gerada uma excessao avisando que
	 * a mesma expirou.
	 * 
	 * @return Simulacao da sessao.
	 */
	public static Simulacao getSimulacaoInSessionExpire() {
		Simulacao simulacao = (Simulacao) SessionContext.get(ConstantesVendas.SIMULACAO_IN_SESSION);
		if (simulacao == null) {
			throw new BusinessException("LMS-04124");
		}
		return simulacao;
	}

	/**
	 * <p>
	 * Retorna o objeto Simulacao armazenado na sessao.
	 * 
	 * <p>
	 * Caso nao exista uma Simulacao na sessao um novo objeto serah armazenado na
	 * mesma e retornado.
	 * 
	 * @return Simulacao da sessao.
	 */
	public static Simulacao getSimulacaoInSession() {
		Simulacao simulacao = (Simulacao) SessionContext.get(ConstantesVendas.SIMULACAO_IN_SESSION);
		if (simulacao == null) {
			simulacao = new Simulacao();
			setSimulacaoInSession(simulacao);
		}
		return simulacao;
	}

	/**
	 * Seta o objeto Simulacao recebido na sessao.
	 * 
	 * @param simulacao
	 *            simulacao para ser setado na sessao.
	 */
	public static void setSimulacaoInSession(Simulacao simulacao) {
		SessionContext.set(ConstantesVendas.SIMULACAO_IN_SESSION, simulacao);
	}

	/**
	 * Remove o objeto AWB da sessao.
	 */
	public static void removeSimulacaoFromSession() {
		SessionContext.remove(ConstantesVendas.SIMULACAO_IN_SESSION);
	}
	
	/**
	 * 
	 * @param sgFilial
	 * @param nrSimulacao
	 * @return
	 */
	public static String formatNrSimulacao(String sgFilial, String nrSimulacao) {
		return sgFilial + " " + FormatUtils.fillNumberWithZero(nrSimulacao, 6);
	}
	
}
