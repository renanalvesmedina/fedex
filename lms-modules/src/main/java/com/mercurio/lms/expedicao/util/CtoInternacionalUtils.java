package com.mercurio.lms.expedicao.util;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.util.FormatUtils;

public class CtoInternacionalUtils {
	private static final String SESSION_KEY = ConstantesExpedicao.CTO_INTERNACIONAL_IN_SESSION; 
	/**
	 * <p>
	 * Retorna o objeto CtoInternacional armazenado na sessao.
	 * 
	 * <p>
	 * Caso nao exista um CtoInternacional na sessao serah gerada uma excessao avisando que
	 * a mesma expirou.
	 * 
	 * @return CtoInternacional da sessao.
	 */
	public static CtoInternacional getCtoInternacionalInSessionExpire() {
		CtoInternacional ctoInternacional = (CtoInternacional) SessionContext.get(SESSION_KEY);
		if (ctoInternacional == null) {
			throw new BusinessException("LMS-04124");
		}
		return ctoInternacional;
	}

	/**
	 * <p>
	 * Retorna o objeto CtoInternacional armazenado na sessao.
	 * 
	 * <p>
	 * Caso nao exista um CtoInternacional na sessao um novo objeto sera armazenado na
	 * mesma e retornado.
	 * 
	 * @return CtoInternacional da sessao.
	 */
	public static CtoInternacional getCtoInternacionalInSession() {
		CtoInternacional ctoInternacional = (CtoInternacional) SessionContext.get(SESSION_KEY);
		if (ctoInternacional == null) {
			ctoInternacional = new CtoInternacional();
			setCtoInternacionalInSession(ctoInternacional);
		}
		return ctoInternacional;
	}

	/**
	 * Seta o objeto CtoInternacional recebido na sessao.
	 * 
	 * @param CtoInternacional para ser setado na sessao.
	 */
	public static void setCtoInternacionalInSession(CtoInternacional ctoInternacional) {
		ExpedicaoUtils.setTpDocumentoInSession(ConstantesExpedicao.CONHECIMENTO_INTERNACIONAL);
		SessionContext.set(SESSION_KEY, ctoInternacional);
	}

	/**
	 * Remove o objeto CtoInternacional da sessao.
	 */
	public static void removeCtoInternacionalFromSession() {
		SessionContext.remove(SESSION_KEY);
	}
	
	/**
	 * Retorna um valor concatenado no formato SG_PAIS.NR_PERMISSO.NR_CRT(BR.002.212334)
	 */
	public static String format(String sgPais, Integer nrPermisso, Long nrCrt) {
		StringBuilder buffer = new StringBuilder()
		.append(sgPais)
		.append(".")
		.append(FormatUtils.formatDecimal("000", nrPermisso))
		.append(".")
		;

		String casas = (nrPermisso.shortValue() > 999)?  "00000" : "000000";

		buffer.append(FormatUtils.formatDecimal(casas, nrCrt));

		return buffer.toString();
	}
}