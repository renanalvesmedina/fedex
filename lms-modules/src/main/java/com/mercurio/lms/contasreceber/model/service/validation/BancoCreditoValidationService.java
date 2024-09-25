package com.mercurio.lms.contasreceber.model.service.validation;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationTemplate;

public class BancoCreditoValidationService extends ValidationTemplate<CreditoBancario>{

	
	public BancoCreditoValidationService() {
		super("LMS-36338");
	}

	@Override
	protected boolean theValidation(CreditoBancario creditoBancario) {

		if(creditoBancario.getIdBanco() == null){
			setMessageParameters(new Object[]{creditoBancario.getNrBanco()});
			return false;
		}

		return true;
	}
}