package com.mercurio.lms.contasreceber.model.service.validation;

import java.math.BigDecimal;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationTemplate;

public class ValorValidoCreditoValidation  extends ValidationTemplate<CreditoBancario>{

	public ValorValidoCreditoValidation() {
		super("LMS-36340");
	}

	@Override
	protected boolean theValidation(CreditoBancario creditoBancario) {

		return !(creditoBancario.getVlCredito().compareTo(BigDecimal.ZERO) <= 0);
	}
}
