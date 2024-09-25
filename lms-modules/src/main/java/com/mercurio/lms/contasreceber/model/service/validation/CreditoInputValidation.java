package com.mercurio.lms.contasreceber.model.service.validation;

import org.apache.commons.lang3.StringUtils;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationTemplate;

public class CreditoInputValidation extends ValidationTemplate<CreditoBancario>{

	public CreditoInputValidation() {
		super("LMS-36337");
	}

	@Override
	protected boolean theValidation(CreditoBancario creditoBancario) {

		return !( creditoBancario == null ||
				StringUtils.isEmpty(creditoBancario.getSgFilial()) || 
				(creditoBancario.getNrBanco() == null) ||
				(creditoBancario.getDtCredito() == null) ||
				(creditoBancario.getVlCredito() == null) ||
				StringUtils.isEmpty(creditoBancario.getTpModalidade()) || 
				StringUtils.isEmpty(creditoBancario.getTpOrigem()) ||
				StringUtils.isEmpty(creditoBancario.getLogin())
				);
	}

}
