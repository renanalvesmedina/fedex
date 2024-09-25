package com.mercurio.lms.contasreceber.model.service.validation;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationInterface;

public class CreditoBancarioValidationChainFactory {
	public static final ValidationInterface<CreditoBancario> CHAIN_INSTANCE = new CreditoBancarioValidationChainFactory().createChain();

	public static final ValidationInterface<CreditoBancario> INPUT_CHAIN_INSTANCE = new CreditoBancarioValidationChainFactory().createInputChain();

	private ValidationInterface<CreditoBancario> createChain() {
		
		FilialCreditoValidationService validation = new FilialCreditoValidationService();
		
		validation.
				setNextValidation(new BancoCreditoValidationService()).
				setNextValidation(new UsuarioCreditoValidationService());
		 
		 return validation;
	}
	
	private ValidationInterface<CreditoBancario> createInputChain() {
		
		CreditoInputValidation inputValidation = new CreditoInputValidation();
		
		 inputValidation
		 		.setNextValidation(new ValorValidoCreditoValidation())
				.setNextValidation(new CreditoBancarioTpModalidadeValidation());
		 
		 return inputValidation;
	}
}
