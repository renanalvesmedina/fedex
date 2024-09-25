package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.model.Boleto;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.baixarBancoBoletoService"
 */
public class BaixarBancoBoletoService {
	private BoletoService boletoService;
	private HistoricoBoletoService historicoBoletoService;

	public void executeBaixarBancoBoleto(Boleto boleto){
		validateBoleto(boleto);

		Short cdOcorrencia = ConstantesConfiguracoes.CD_OCOR_BAIXAR_PROTESTO_BRADESCO;
		if (ConstantesConfiguracoes.COD_HSBC.equals(boleto.getCedente().getAgenciaBancaria().getBanco().getNrBanco())){
			cdOcorrencia = ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_BAIXA;
		}
		
		historicoBoletoService.generateHistoricoBoleto(boleto, cdOcorrencia, "REM");
		updateBoleto(boleto);
	}

	private void validateBoleto(Boleto boleto){
		String tpSituacao = boleto.getTpSituacaoBoleto().getValue();

		//A situação só pode ser:
		//'Em banco com protesto',
		//'Em banco sem protesto',
		if (!tpSituacao.equals("BP")){
			throw new BusinessException("LMS-36079");			
		}
	}

	/**
	 * Atualiza com a nova situação o boleto informado
	 * */	
	private void updateBoleto(Boleto boleto){
		boleto.setTpSituacaoBoleto(new DomainValue("EM"));

		boletoService.store(boleto);
	}	

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

}