package com.mercurio.lms.contasreceber.model.service.validation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;

import com.mercurio.lms.util.validation.ValidationTemplate;

public class CreditoBancarioTpModalidadeValidation extends ValidationTemplate<CreditoBancario> {

	private static final Set<String> MODALIDADES_VALIDAS = new HashSet<String>(Arrays.asList(new String[]{ "BO", "DO", "DE", "TE", "TR" }));
	
	public CreditoBancarioTpModalidadeValidation() {
		super("LMS-01130");
	}

	@Override
	protected boolean theValidation(CreditoBancario creditoBancario) {
		if(!MODALIDADES_VALIDAS.contains(creditoBancario.getTpModalidade())){
			setMessageParameters(new Object[]{ "tpModalidade" });
			return false;
		}

		return true;
	}

}
