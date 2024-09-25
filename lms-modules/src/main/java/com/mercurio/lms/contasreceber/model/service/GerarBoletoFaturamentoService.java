package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;

import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.util.LongUtils;


/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.contasreceber.gerarBoletoFaturamentoService"
 */
public class GerarBoletoFaturamentoService extends GerarBoletoService {
	    protected Boleto beforeInsert(Boleto boleto) {
		/** Valida para n�o inserir nrSequenciaFilial com diferen�a de numera��o */
		if (!LongUtils.hasValue(boleto.getNrSequenciaFilial())) {
	    	boleto.setNrSequenciaFilial(boletoService.findNextNrSequenciaFilial(boleto.getFatura().getFilialByIdFilial().getIdFilial()));
		}
	    	boleto.setNrBoleto(nrBoletoService.findNextNrBoleto(boleto.getFatura().getFilialByIdFilial().getIdFilial(), boleto.getCedente().getIdCedente()));
	    	boleto.setBlBoletoConhecimento(Boolean.FALSE);
	    	boleto.setBlBoletoReemitido(Boolean.FALSE);
	    	
	    	//Calcular o valor do jurio diario
	    	boleto.setVlJurosDia(calcularJurosDiarioService.calcularVlJuros(boleto.getFatura(), new BigDecimal(1)));
	    	
	    	return boleto;
	    }
}
