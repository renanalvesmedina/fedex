package com.mercurio.lms.contasreceber.model.service;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;


/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.retransmitirBoletoService"
 */
public class RetransmitirBoletoService {
	private HistoricoBoletoService historicoBoletoService;
	private FaturaService faturaService;

	public void executeRetransmitirBoleto(Boleto boleto){

		updateFatura(boleto);
    		historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_RETRANSMITIR, "REM");
		
	}

	/**
	 * Atualiza com a nova data de vencimento a fatura do boleto informado
	 * */
	private void updateFatura(Boleto boleto){
		Fatura fatura = boleto.getFatura();
		fatura.setDhTransmissao(null);
		
		faturaService.store(fatura);
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
}