package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;

import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.util.LongUtils;


/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarBoletoFaturamentoService"
 */
public class GerarBoletoFaturamentoService extends GerarBoletoService {
	    protected Boleto beforeInsert(Boleto boleto) {
		/** Valida para não inserir nrSequenciaFilial com diferença de numeração */
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
