package com.mercurio.lms.vendas.util;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.vendas.model.Cotacao;

public abstract class VendasUtils {

	public static Cotacao getCotacaoInSession() {
		Cotacao cotacao = (Cotacao)SessionContext.get(ConstantesVendas.COTACAO_IN_SESSION);
		if(cotacao == null)
			throw new BusinessException("objectNotFoundInSession", new Object[]{ConstantesVendas.COTACAO_IN_SESSION});
		return cotacao;
	}

	public static void setCotacaoInSession(Cotacao cotacao) {
		SessionContext.set(ConstantesVendas.COTACAO_IN_SESSION, cotacao);
	}

	public static void createCotacaoInSession(Cotacao cotacao) {
		ExpedicaoUtils.setTpDocumentoInSession(ConstantesVendas.COTACAO);
		setCotacaoInSession(cotacao);
		SessionContext.remove(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
	}

	public static void removeCotacaoFromSession() {
		SessionContext.remove(ConstantesVendas.COTACAO_IN_SESSION);
	}

	/**
	 * Formata número da cotação.
	 * @param sgFilial
	 * @param nrCotacao
	 * @return
	 */
	public static String formatCotacao(String sgFilial, Integer nrCotacao) {
		return formatCotacao(sgFilial, nrCotacao, IntegerUtils.getInteger(6).intValue());
	}

	/**
	 * Formata número da cotação.
	 * @param sgFilial
	 * @param nrCotacao
	 * @param size
	 * @return
	 */
	public static String formatCotacao(String sgFilial, Integer nrCotacao, int size) {
		StringBuilder builder = new StringBuilder();
		builder.append(sgFilial);
		builder.append(" ");
		builder.append(FormatUtils.fillNumberWithZero(nrCotacao.toString(), size));
		return builder.toString();
	}

}
