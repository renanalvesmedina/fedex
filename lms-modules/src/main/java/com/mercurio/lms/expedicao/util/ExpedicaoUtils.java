package com.mercurio.lms.expedicao.util;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.CalculoNFServico;
import com.mercurio.lms.expedicao.model.CalculoNFT;
import com.mercurio.lms.expedicao.model.CalculoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.util.FormatUtils;

import java.util.List;
import java.util.regex.Pattern;

public class ExpedicaoUtils {

	public static String getTpDocumentoInSession() {
		String tpDocumento = (String) SessionContext.get(ConstantesExpedicao.TP_DOCUMENTO_IN_SESSION);
		if(tpDocumento == null)
			throw new BusinessException("objectNotFoundInSession", new Object[]{ConstantesExpedicao.TP_DOCUMENTO_IN_SESSION});
		return tpDocumento;
	}

	public static void setTpDocumentoInSession(String tpDocumento) {
		SessionContext.set(ConstantesExpedicao.TP_DOCUMENTO_IN_SESSION, tpDocumento);
	}

	public static CalculoServico getCalculoServicoInSession(boolean isRequired) {
		CalculoServico calculoServico = (CalculoServico) SessionContext.get(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
		if(isRequired && (calculoServico == null) ) {
			throw new BusinessException("objectNotFoundInSession", new Object[]{ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION});
		}
		return calculoServico;
	}

	private static void setCalculoServicoInSession(CalculoServico calculoServico) {
		SessionContext.set(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION, calculoServico);
	}

	public static CalculoFrete getCalculoFreteInSession(boolean isRequired) {
		return (CalculoFrete) getCalculoServicoInSession(isRequired);
	}

	public static CalculoFrete getCalculoFreteInSession() {
		return (CalculoFrete) getCalculoServicoInSession(true);
	}

	public static void setCalculoFreteInSession(CalculoFrete calculoFrete) {
		setCalculoServicoInSession(calculoFrete);
	}

	public static CalculoNFServico getCalculoNFSInSession() {
		return (CalculoNFServico) getCalculoServicoInSession(true);
	}

	public static void setCalculoNFSInSession(CalculoNFServico calculoNFServico) {
		setCalculoServicoInSession(calculoNFServico);
	}

	public static CalculoFrete newCalculoFrete(DomainValue tpDocumentoCotacao){
		CalculoFrete result = null;
		if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equals(tpDocumentoCotacao.getValue()) || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoCotacao.getValue())) {
			result = new CalculoNFT();
		} else {
			result = new CalculoFrete();
		}
		return result;
	}
	
	public static CalculoNFT getCalculoNFTInSession() {
		return (CalculoNFT) getCalculoServicoInSession(true);
	}

	public static void setCalculoNFTInSession(CalculoNFT calculoNFT) {
		setCalculoServicoInSession(calculoNFT);
	}

	public static NotaFiscalServico getNFSInSession() {
		return (NotaFiscalServico) SessionContext.get(ConstantesExpedicao.NOTA_FISCAL_SERVICO_IN_SESSION);
	}

	public static void setNFSInSession(NotaFiscalServico notaFiscalServico) {
		SessionContext.set(ConstantesExpedicao.NOTA_FISCAL_SERVICO_IN_SESSION, notaFiscalServico);
	}

	public static void removeNFSFromSession() {
		SessionContext.remove(ConstantesExpedicao.NOTA_FISCAL_SERVICO_IN_SESSION);
	}

	public static void removeCalculoFreteFromSession() {
		SessionContext.remove(ConstantesExpedicao.CALCULO_SERVICO_IN_SESSION);
	}

	public static String formatManifestoViagemNacional(String sgFilial, Integer nrManifesto, int size) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(sgFilial);
		buffer.append(" ");
		buffer.append(FormatUtils.fillNumberWithZero(nrManifesto.toString(), size));
		return buffer.toString();
	}

	public static boolean validaListEmail(List<String> listEmails) {

		final String regexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
		boolean result;

		if(listEmails == null || listEmails.isEmpty()) {
			return false;
		}

		for(String email: listEmails) {
			result = Pattern.compile(regexPattern)
					.matcher(email)
					.matches();
			if(!result) {
				return false;
			}
		}
		return true;
	}

}
