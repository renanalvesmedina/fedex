package com.mercurio.lms.facade.radar;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Adércio Reinan
 */
public interface TrackingFacade {
	
	TypedFlatMap findLocalizacaoFilialAtual(TypedFlatMap criteria);
	
	TypedFlatMap findLocalizacaoFilialOrigemAndDestino(TypedFlatMap criteria);
	
	TypedFlatMap findDepotByIdFilial(TypedFlatMap criteria);
	
	List<TypedFlatMap> findAllCustomers(TypedFlatMap criteria);
	
	TypedFlatMap findEventoByIdDoctoServico(TypedFlatMap criteria);
	
	TypedFlatMap findEventoNaoConformidade(TypedFlatMap criteria);
	
	TypedFlatMap findInfoEventoAtualDoctoByIdDoctoServico(TypedFlatMap criteria);
	
	TypedFlatMap findXml(TypedFlatMap criteria);
	
	TypedFlatMap validatePessoaByNrIdentificacao(TypedFlatMap criteria);
}

