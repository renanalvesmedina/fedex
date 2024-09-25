package com.mercurio.lms.facade.radar;

import java.util.List;

import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Rodrigo Maciel da Silva
 * @author Gabriel Rubens
 */
public interface CotacaoOnlineFacade {
	public static final String TRUE = "S";
	public static final String FALSE = "N";
	
	TypedFlatMap searchDimensao(TypedFlatMap criteria);

	TypedFlatMap findServicoById(TypedFlatMap criteria);
	
	TypedFlatMap findUserById(TypedFlatMap criteria);
	
	TypedFlatMap findServicosByTpAbrangencia(TypedFlatMap criteria);
	
	TypedFlatMap findIdServicoDefaultModal(TypedFlatMap criteria);
	
	TypedFlatMap getClienteByIdentificador(TypedFlatMap criteria);
	
	List<TypedFlatMap> getIeByCliente(TypedFlatMap criteria);
	
	TypedFlatMap findEnderecoByCliente(TypedFlatMap criteria);

	List<TypedFlatMap> getDivisaoByIdClienteCalculo(TypedFlatMap criteria);

	TypedFlatMap getClienteById(TypedFlatMap criteria);

	TypedFlatMap getDivisaoClienteById(TypedFlatMap criteria);

	List<TypedFlatMap> getResponsavelByIdCliente(TypedFlatMap criteria);

	TypedFlatMap getPessoaById(TypedFlatMap criteria);

	TypedFlatMap findNaturezaProduto(TypedFlatMap criteria);

	TypedFlatMap findTabelaByDivisao(TypedFlatMap criteria);	

	List<TypedFlatMap> getTabDivClienteByIdDivCliente(TypedFlatMap criteria);

	List<TypedFlatMap> findAllEstadosByPaisAndSituacao();

	List<TypedFlatMap> findMunicipiosByEstado(TypedFlatMap map);

	List<TypedFlatMap> getSitTributariaByIE(TypedFlatMap criteria);

	List<TypedFlatMap> getTipoDestinoByIdLocalizacaoMunicipio(TypedFlatMap criteria);

	TypedFlatMap hasParametroGeralDimensoes(TypedFlatMap criteria);

	TypedFlatMap hasParametroFilialOrigemDimensoes(TypedFlatMap criteria);

	TypedFlatMap hasParametroClienteCalculoDimensoes(TypedFlatMap criteria);

	TypedFlatMap getProibidoEmbarqueByIdCliente(TypedFlatMap criteria);

	List<TypedFlatMap> getMunicipioFilialByMunicipioFilial(TypedFlatMap criteria);

	List<TypedFlatMap> getLiberacaoEmbarqueByMunicipioClienteTpModal(TypedFlatMap criteria);
	
	List<TypedFlatMap> findCotacoesByUserAndTipoSituacaoPaginated(TypedFlatMap criteria);

	TypedFlatMap getNrCubagemByDivisao(TypedFlatMap cri);

	List<TypedFlatMap> getAeroportoByFilial(TypedFlatMap criteria);

	List<TypedFlatMap> getProdutoEspecifico();

	List<TypedFlatMap> findAllEstados(TypedFlatMap criteria);

	TypedFlatMap saveCotacao(TypedFlatMap criteria);

	TypedFlatMap saveParcelasCotacao(TypedFlatMap criteria);

	TypedFlatMap saveDimensoesCotacao(TypedFlatMap criteria);

	TypedFlatMap findNroCepByIdMunicipio(TypedFlatMap criteria);
	
	TypedFlatMap findNomesMunicipiosCotacaoPendente(TypedFlatMap criteria);
	
	TypedFlatMap findFilialAtendeOperacional(TypedFlatMap criteria);
	

}
