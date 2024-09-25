package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
import java.util.Map;

import com.mercurio.lms.expedicao.model.service.ConhecimentoSubstitutoService;
import com.mercurio.lms.expedicao.model.service.DigitarDadosNotaNormalCalculoCTRCService;


public class DigitarConhecimentoSubstitutoCalculoAction extends AbstractCalculoFreteAction {
	
	private DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService;
	private ConhecimentoSubstitutoService conhecimentoSubstitutoService;
	
	public Serializable storeCteSubstituto(Map<String, Object> parameters){
		return conhecimentoSubstitutoService.storeConhecimentoSubstitutoManual(parameters);
	}

	public DigitarDadosNotaNormalCalculoCTRCService getDigitarDadosNotaNormalCalculoCTRCService() {
		return digitarDadosNotaNormalCalculoCTRCService;
	}

	public void setDigitarDadosNotaNormalCalculoCTRCService(
			DigitarDadosNotaNormalCalculoCTRCService digitarDadosNotaNormalCalculoCTRCService) {
		this.digitarDadosNotaNormalCalculoCTRCService = digitarDadosNotaNormalCalculoCTRCService;
	}

	public ConhecimentoSubstitutoService getConhecimentoSubstitutoService() {
		return conhecimentoSubstitutoService;
	}

	public void setConhecimentoSubstitutoService(
			ConhecimentoSubstitutoService conhecimentoSubstitutoService) {
		this.conhecimentoSubstitutoService = conhecimentoSubstitutoService;
	}

}