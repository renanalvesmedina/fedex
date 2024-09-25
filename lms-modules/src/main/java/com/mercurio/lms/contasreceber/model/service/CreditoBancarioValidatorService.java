package com.mercurio.lms.contasreceber.model.service;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.contasreceber.model.service.validation.CreditoBancarioValidationChainFactory;

public class CreditoBancarioValidatorService {

	public Boolean validate(CreditoBancario creditoBancario){
		
		CreditoBancarioValidationChainFactory.CHAIN_INSTANCE.doValidation(creditoBancario);
		
		return true;
	}
	public Boolean validateInput(CreditoBancario creditoBancario){
		
		CreditoBancarioValidationChainFactory.INPUT_CHAIN_INSTANCE.doValidation(creditoBancario);
		
		return true;
	}
}
