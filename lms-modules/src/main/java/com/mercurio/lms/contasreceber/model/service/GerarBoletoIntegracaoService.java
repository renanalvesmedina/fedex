package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.lms.contasreceber.model.Boleto;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarBoletoIntegracaoService"
 */
public class GerarBoletoIntegracaoService extends GerarBoletoService {

	/**
     * 
     *
     * @author Hector Julian Esnaola Junior
     * @since 26/06/2007
     *
     * @param boleto
     * @return
     *
     */
    @Override
    protected Boleto beforeInsert(Boleto boleto) {
    	//Calcular o valor do jurio diario
  /*  	boleto.setVlJurosDia(calcularJurosDiarioService.calcularVlJuros(boleto.getFatura(), new BigDecimal(1)));

    	//A data de emissao deve ser menor de 3 dias ou igual a data atual
    	YearMonthDay dtEmissao = boleto.getDtEmissao();
    	
    	if(!SessionUtils.isIntegrationRunning())
    	if (dtEmissao.isAfter(JTDateTimeUtils.getDataAtual()) || dtEmissao.isBefore(JTDateTimeUtils.getDataAtual().minusDays(4))){
    		throw new BusinessException("LMS-36099");
    	}
    	
    	Long idMunicipio = getMunicipioService().findIdMunicipioByPessoa(boleto.getFatura().getCliente().getIdCliente()); 
    	if (idMunicipio != null){
    		//A data de vencimento tem que ser um dia util
    		if (boleto.getDtVencimento() != null){
    			boleto.setDtVencimento(diaUtils.getNextDiaUtilIfNotUtil(boleto.getDtVencimento(), idMunicipio));
    		} else {
    			boleto.setDtVencimento(boleto.getFatura().getDtVencimento());
    		}
    	}else{
    		throw new BusinessException("LMS-00069");
    	}
    	//A filial de origem da fatura tem que ser igual a filial da sessão
    	if (!boleto.getFatura().getFilialByIdFilial().equals(SessionUtils.getFilialSessao())){
    		throw new BusinessException("LMS-36098");
    	}
    	
    	String tpSituacao = boleto.getFatura().getTpSituacaoFatura().getValue();
    	
    	//Se a situação da fatura for diferente de 'Digitado' e 'Emitido', lançar uma exception
    	if (!tpSituacao.equals("DI") && !tpSituacao.equals("EM")) {
    		throw new BusinessException("LMS-36094");
    	}
  */  	
    	return boleto;
    }
    
}
