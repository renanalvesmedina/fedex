package com.mercurio.lms.contasreceber.model.service.validation;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationTemplate;

public class UsuarioCreditoValidationService extends ValidationTemplate<CreditoBancario>{

	public UsuarioCreditoValidationService() {
		super("LMS-36341");
	}

	@Override
	protected boolean theValidation(CreditoBancario creditoBancario) {

		if(creditoBancario.getIdUsuario() == null){
			setMessageParameters(new Object[]{creditoBancario.getLogin()});
			return false;
		}
		return true;
	}

}
