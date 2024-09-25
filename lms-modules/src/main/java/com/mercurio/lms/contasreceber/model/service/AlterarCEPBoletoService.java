package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Cedente;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.alterarCEPBoletoService"
 */
public class AlterarCEPBoletoService {
	private HistoricoBoletoService historicoBoletoService;
	private CedenteService cedenteService;

	public void executeAlterarCEPBoleto(Boleto boleto){
		validateBoleto(boleto);
		historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_ALTERAR_CEP, "REM");
	}

	private void validateBoleto(Boleto boleto){
		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();
		//A situação só pode ser:
		//'Em banco',
		if (!tpSituacao.equals("BN")) {
			throw new BusinessException("LMS-36080");			
		}

		Cedente cedente = cedenteService.findById(boleto.getCedente().getIdCedente());
		//Só pode ser do banco Itau
		
		if (!isBradescoItau(cedente.getAgenciaBancaria().getBanco())){
			throw new BusinessException("LMS-36081");			
		}
		
	}
	
	private boolean isBradescoItau(Banco banco){
		return ConstantesConfiguracoes.COD_ITAU.equals(banco.getNrBanco()) ||ConstantesConfiguracoes.COD_BRADESCO.equals(banco.getNrBanco()); 
	}
	

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setCedenteService(CedenteService cedenteService) {
		this.cedenteService = cedenteService;
	}

}