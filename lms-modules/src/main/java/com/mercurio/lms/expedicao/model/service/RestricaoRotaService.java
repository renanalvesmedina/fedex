package com.mercurio.lms.expedicao.model.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.lms.expedicao.model.CalculoFrete;
import com.mercurio.lms.expedicao.model.dao.CalculoFreteDAO;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota.TIPOROTA;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.model.service.TarifaPrecoRotaService;
import com.mercurio.lms.vendas.model.ParametroCliente;

/**
 * 
 * @spring.bean id="lms.expedicao.restricaoRotaService"
 */
public class RestricaoRotaService {
	
	private static int PARAMETRO = 1;
	private static int TARIFA 	 = 2;
	
	private CalculoFreteDAO 	   calculoFreteDAO;
	private TarifaPrecoRotaService tarifaPrecoRotaService;
	
	/**
	 * Obtem a TarifaPreco através dos dados de origem e destino
	 * 
	 * Caso a consulta feita na tabela TarifaPrecoRota retornar mais
	 * de um registro, filtra TarifaPreco através do indice gerado
	 * no objeto TarifaPrecoRota.RotaPreco.indexRotaOrigem, TarifaPrecoRota.RotaPreco.indexRotaOrigem 
	 * 
	 * Executa o filtro através da rota (origem ou destino) mais restrita até a mais abrangente 
	 * segue abaixo;
	 * 
	 * ZONA-PAIS-UF-FILIAL-MUNICIPIO
	 * ZONA-PAIS-UF-GRUPO REGIAO
	 * ZONA-PAIS-UF-FILIAL
	 * ZONA-PAIS-UF-TIPO LOCALIZACAO
	 * ZONA-PAIS-UF-AEROPORTO
	 * ZONA-PAIS-UF
	 * ZONA-PAIS
	 * ZONA
	 * 
	 * @param  calculoFrete
	 * @return TarifaPreco
	 * 
	 */
	public TarifaPrecoRota findTarifaPrecoRota(CalculoFrete calculoFrete){
		
		calculoFrete.getRestricaoRotaOrigem().setVerificarLocalizacaoRota(true);
		calculoFrete.getRestricaoRotaDestino().setVerificarLocalizacaoRota(true);		
		
		List<TarifaPrecoRota> list = getTarifaPrecoRotaService().findTarifaPrecoRota(calculoFrete.getTabelaPreco().getIdTabelaPreco(), calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino());
		
		if(CollectionUtils.isNotEmpty(list)){
			if(list.size() == 1){
				TarifaPrecoRota tarifaPrecoRota = list.get(0); 
				return tarifaPrecoRota;
			}else{
				TarifaPrecoRota tarifa = findData(list,TARIFA);
				if(tarifa != null){
					return tarifa;
				}
			}
		}
		return null; 
	}	
	
	/**
	 * Obtem o ParametroCliente através dos dados de origem e destino
	 * 
	 * Caso a consulta feita na tabela ParametroCliente retornar mais
	 * de um registro, filtra ParametroCliente através do indice gerado
	 * no objeto ParametroCliente.indexRotaOrigem, ParametroCliente.indexRotaOrigem 
	 * 
	 * Executa o filtro através da rota (origem ou destino) mais restrita até a mais abrangente 
	 * segue abaixo;
	 * 
	 * ZONA-PAIS-UF-FILIAL-MUNICIPIO
	 * ZONA-PAIS-UF-GRUPO REGIAO
	 * ZONA-PAIS-UF-FILIAL
	 * ZONA-PAIS-UF-TIPO LOCALIZACAO
	 * ZONA-PAIS-UF-AEROPORTO
	 * ZONA-PAIS-UF
	 * ZONA-PAIS
	 * ZONA
	 * 
	 * @param  calculoFrete
	 * @return ParametroCliente
	 */
	public ParametroCliente findParametroClienteRota(CalculoFrete calculoFrete){
				
		calculoFrete.getRestricaoRotaOrigem().setVerificarLocalizacaoRota(true);
		calculoFrete.getRestricaoRotaDestino().setVerificarLocalizacaoRota(true);		
		
		List<ParametroCliente> list = getCalculoFreteDAO().findParametroClienteByRota(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico(), calculoFrete.getRestricaoRotaOrigem(), calculoFrete.getRestricaoRotaDestino());
		
		if(CollectionUtils.isNotEmpty(list)){
			if(list.size() == 1){
				return list.get(0); 
			}else{
				ParametroCliente parametro = findData(list,PARAMETRO);
				if(parametro != null){
					return parametro;
				}
			}
		}
		return null;
	}		
	

	/**
	 * Obtem informações de tarifa ou parametro corretos através da lista
	 * informada nos parâmetros
	 * 
	 * @param list
	 * @param type
	 * @return
	 */
	private  <T> T  findData(List<T> list, int type){
		
		if(CollectionUtils.isNotEmpty(list)){
			
			Predicate predicate  = null; 
			
			List<T> lsOrigem  = null;
			List<T> lsDestino = null;
			
			List<TIPOROTA> tiposRota = Arrays.asList(TIPOROTA.values()); 
			for (TIPOROTA rotaOrigem : tiposRota) {
				predicate = getPredicate(rotaOrigem.getIndex(), true, type);				
				lsOrigem = new ArrayList<T>();
				CollectionUtils.select(list, predicate, lsOrigem);
				if(CollectionUtils.isNotEmpty(lsOrigem)){		 			
					for (TIPOROTA rotaDestino : tiposRota) {
						predicate = getPredicate(rotaDestino.getIndex(), false, type);
						lsDestino = new ArrayList<T>();
						CollectionUtils.select(list, predicate, lsDestino);
						if(CollectionUtils.isNotEmpty(lsDestino)){
							return lsDestino.get(0);
						}
					}
				}
			}/*for*/
		}
		return null;
	}	
	
	/**
	 * Obtem o Predicate origem ou destino e informando o tipo
	 * de rota utilizada
	 * 
	 * @param  index
	 * @param  origem
	 * @param  type
	 * @return Predicate
	 */
	private Predicate getPredicate(final int index, final boolean origem, int type){
		
		Predicate predicate = null;
		
		switch (type) {
		case 1:
			predicate = getPredicateParametro(index, origem);
			break;
		case 2:	
			predicate = getPredicateTarifa(index, origem); 
			break;			
		default:
			throw new IllegalArgumentException("invalid type");
		}
		
		return predicate;
	}	
	
	/**
	 * Obtem o Predicate para filtrar a lista de TarifaPrecoRota
	 * 
	 * @param  index
	 * @param  origem
	 * @return Predicate
	 */
	private Predicate getPredicateTarifa(final Integer index, final Boolean origem){
		
		return new Predicate() {
			
			public boolean evaluate(Object object) {
				
				TarifaPrecoRota t = (TarifaPrecoRota) object;
				if(origem){
					Integer indexRotaOrigem = RestricaoRota.indexRota(t.getRotaPreco().getZonaByIdZonaOrigem(), 
							t.getRotaPreco().getPaisByIdPaisOrigem(), t.getRotaPreco().getFilialByIdFilialOrigem(), t.getRotaPreco().getUnidadeFederativaByIdUfOrigem(), 
							t.getRotaPreco().getMunicipioByIdMunicipioOrigem(), t.getRotaPreco().getGrupoRegiaoOrigem(), 
							t.getRotaPreco().getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(), 
							t.getRotaPreco().getAeroportoByIdAeroportoOrigem()); 
					return indexRotaOrigem  == index;
				}else{
					Integer indexRotaDestino = RestricaoRota.indexRota(t.getRotaPreco().getZonaByIdZonaDestino(), 
							t.getRotaPreco().getPaisByIdPaisDestino(), t.getRotaPreco().getFilialByIdFilialDestino(), t.getRotaPreco().getUnidadeFederativaByIdUfDestino(), 
							t.getRotaPreco().getMunicipioByIdMunicipioDestino(), t.getRotaPreco().getGrupoRegiaoDestino(), 
							t.getRotaPreco().getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(), 
							t.getRotaPreco().getAeroportoByIdAeroportoDestino());
					return indexRotaDestino == index;
				}				
			}
		};		
	}	
	
	/**
	 * Obtem o Predicate para filtrar a lista de ParametroCliente
	 * 
	 * @param  index
	 * @param  origem
	 * @return Predicate
	 */
	private Predicate getPredicateParametro(final Integer index, final Boolean origem){
		
		return new Predicate() {
			
			public boolean evaluate(Object object) {
				
				ParametroCliente p = (ParametroCliente) object;
				if(origem){
					return p.getIndexRotaOrigem()  == index;
				}else{
					return p.getIndexRotaDestino() == index;
				}				
			}
		};		
	}

	public Boolean validateGrupoRegiaoDestinoPorTarifa(CalculoFrete calculoFrete){
		TarifaPrecoRota tarifaPrecoRota = findTarifaPrecoRota(calculoFrete);
		if( tarifaPrecoRota != null ){
			return tarifaPrecoRota.getRotaPreco().getGrupoRegiaoDestino() == null;
		}
		return true;
	}
	

	public CalculoFreteDAO getCalculoFreteDAO() {
		return calculoFreteDAO;
	}

	public void setCalculoFreteDAO(CalculoFreteDAO calculoFreteDAO) {
		this.calculoFreteDAO = calculoFreteDAO;
	}

	public TarifaPrecoRotaService getTarifaPrecoRotaService() {
		return tarifaPrecoRotaService;
	}

	public void setTarifaPrecoRotaService(
			TarifaPrecoRotaService tarifaPrecoRotaService) {
		this.tarifaPrecoRotaService = tarifaPrecoRotaService;
	}

}
