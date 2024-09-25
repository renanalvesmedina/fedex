package com.mercurio.lms.facade.radar;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Adércio Reinan
 */
public interface PosicaoFinanceiraFacade {
	
	TypedFlatMap findBancoByNrBanco(TypedFlatMap criteria);

	TypedFlatMap findBoletoByNrBoleto(TypedFlatMap criteria);

	TypedFlatMap findCedenteByIdCedente(TypedFlatMap criteria);

	TypedFlatMap findAgenciaById(TypedFlatMap criteria);

	List<TypedFlatMap> findCnpjAutorizadoByUser(TypedFlatMap criteria);

	List<TypedFlatMap> findFaturaByCriteria(TypedFlatMap criteria);
	
	List<TypedFlatMap> getRowCountFaturaByCriteria(TypedFlatMap criteria) throws Exception;
	
	TypedFlatMap findBoletoByIdFatura(TypedFlatMap criteria);

	TypedFlatMap imprimirBoletoComDataFutura(TypedFlatMap criteria)  throws Exception;
	
	TypedFlatMap imprimirSegundaViaFatura(TypedFlatMap criteria)  throws Exception;

	List<TypedFlatMap> gerarXMLConhecimentoEletronico(TypedFlatMap criteria);
	
	TypedFlatMap findByNomeParametro(TypedFlatMap criteria);

	TypedFlatMap findExistsConhecimentoEletronicoByCriteria(TypedFlatMap criteria);
	
	List<TypedFlatMap> findCustomersByNrIdentificacao(String nrsIdentificacao);

	TypedFlatMap executeEmitirCTEbyIdFatura(TypedFlatMap parameters);
	
	TypedFlatMap executeEmitirNTEbyIdFatura(TypedFlatMap parameters);
	
	TypedFlatMap findFaturaById(TypedFlatMap criteria);
	
	TypedFlatMap findUserByLogin(TypedFlatMap criteria);
	
	List<TypedFlatMap> findContatosByIdPessoaAndTipoContato(TypedFlatMap criteria);
	
	TypedFlatMap storeMonitoramentoMensagem(TypedFlatMap criteria);
	
	TypedFlatMap findRecursoMensagemByChave(TypedFlatMap criteria);
	
	TypedFlatMap existeMonitoramentoMensagem(TypedFlatMap criteria);
	
}

