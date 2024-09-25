package com.mercurio.lms.contasreceber.model.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;

public class RetornoOcorrenciaBoletoNaoEncontradoService {

	private RetornoBancoService retornoBancoService;
	private CreditoBancarioService creditoBancarioService;
	
	private static final Set<Long> OCORRENCIA_LIQUIDACAO = 
			new HashSet<Long>(Arrays.asList(new Long[]{ 6L, 7L, 15L, 16L, 31L, 32L, 33L, 36L, 38L, 39L }));
	
	public void processaBoletoNaoEncontrado(BoletoDMN boletoDmn) {
		retornoBancoService.inclusaoMensagemRetorno(boletoDmn, null, "LMS-36351");
		
		if (OCORRENCIA_LIQUIDACAO.contains(boletoDmn.getNrOcorrencia())) {
			creditoBancarioService.rotinaInclusaoCreditoBancario(boletoDmn, null, "LMS-36352");
		}
	}

	public void setCreditoBancarioService(CreditoBancarioService creditoBancarioService) {
		this.creditoBancarioService = creditoBancarioService;
	}

	public void setRetornoBancoService(RetornoBancoService retornoBancoService) {
		this.retornoBancoService = retornoBancoService;
	}
}
