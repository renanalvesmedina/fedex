package com.mercurio.lms.contasreceber.model.service.validation;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationTemplate;

public class FilialCreditoValidationService extends ValidationTemplate<CreditoBancario>{

	public FilialCreditoValidationService() {
		super("LMS-36275");
	}

	@Override
	protected boolean theValidation(CreditoBancario creditoBancario) {

		if(creditoBancario.getIdFilial() == null){
			setMessageParameters(new Object[]{creditoBancario.getSgFilial()});
			return false;
		}

		return true;
	}
}
