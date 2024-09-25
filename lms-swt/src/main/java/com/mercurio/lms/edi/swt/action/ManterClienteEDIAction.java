package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.edi.model.ClienteEDI;
import com.mercurio.lms.edi.model.service.ClienteEDIService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

public class ManterClienteEDIAction {
	
	private ClienteService clienteService; 
	private ClienteEDIService clienteEDIService;
	private FilialService filialService;
		
	/**
	 * Obtem o ClienteEDI através do ID
	 * 
	 * @param id
	 * @return Map
	 */
	public Map<String, Object> findById(Long id) {	
		
		Map<String, Object> mapa = clienteEDIService.findById(id).getMapped();
		
		Cliente cliente = clienteService.findById(id);
	
		mapa.put("idCliente", cliente.getIdCliente());
		mapa.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
		mapa.put("nmPessoa", cliente.getPessoa().getNmPessoa());
		
    	return mapa;    	
	}

	/**
	 * Persiste o ClienteEDI
	 * @param bean
	 * @return Map
	 */
	public Map<String, Object> store(Map<String, Object> bean) {
		
		MapUtilsPlus map = new MapUtilsPlus(bean);
		
		ClienteEDI pojo = new ClienteEDI();
		
		/*Id do cliente*/
		pojo.setIdClienteEDI(map.getLong("idCliente"));
		
		/*Caixa postal contato*/
		pojo.setCpContato(map.getString("cpContato"));
		
		/*Caixa postal destinatario*/
		pojo.setCpDestinatario(map.getString("cpDestinatario"));
		
		/*Caixa postal remetente*/
		pojo.setCpRemetente(map.getString("cpRemetente"));
		
		/*Fone Contato*/
		pojo.setFoneContato(map.getString("foneContato"));
		
		/*Nome do contato*/
		pojo.setNmContato(map.getString("nmContato"));
		
		/*Ocorrencia de entrega*/
		pojo.setOcoEntrega(map.getDomainValue("ocoEntrega"));
		
		/*Recebe ocorrência de entrega*/
		pojo.setRocEntrega(MapUtilsPlus.getBoolean(bean, "rocEntrega"));
		
		/*Recebe Localização*/
		pojo.setRecebeLocal(map.getDomainValue("recebeLocal"));
		
		/*Série NF*/
		pojo.setSerieNf(map.getString("serieNf"));
		
		/*Tipo de geração*/
		pojo.setTpGeracao(map.getDomainValue("tpGeracao"));
		
     	/*Salva o Cliente EDI*/
		clienteEDIService.store(pojo);
			 
		Map<String,Object> retorno = new HashMap<String, Object>();
		retorno.put("idClienteEdi", pojo.getIdClienteEDI());
		
		return retorno;
	}
	
			
	/**
	 * Retorna a lista de registros para a grid
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = clienteEDIService.findPaginated(new PaginatedQuery(criteria));					
		
		List<ClienteEDI> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(ClienteEDI clienteEDI : list){
			
			Map<String, Object> mapa = clienteEDI.getMapped(); 
			
			Cliente cliente = clienteService.findById((Long) mapa.get("idClienteEdi"));
			
			mapa.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
			mapa.put("nmPessoa", cliente.getPessoa().getNmPessoa());
			
			retorno.add(mapa);
		}
			
		rsp.setList(retorno);
		return rsp;		
	}		
	
	/**
	 * Lookup de filiais
	 * @param criteria
	 * @return List
	 */
    public List findLookupFilial(Map criteria) {
		return filialService.findLookup(criteria); 
	}	
	
    /**
     * Lookup ClienteEDI 
     * @param  criteria
     * @return List
     */
	public List findClienteEDI(TypedFlatMap criteria) {				
		
		List<Map<String,Object>> cliente = clienteService.findClienteByNrIdentificacao(criteria.getString("nrIdentificacao")); 
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		if(!cliente.isEmpty()){
			
			Map<String,Object> dados = (HashMap<String, Object>)cliente.get(0);
			Long idCliente = (Long)dados.get("idCliente");
			
			if(clienteEDIService.findClienteEDIById(idCliente) == null){
				throw new BusinessException("FLC-01001");
			}else{	
				retorno.put("idClienteEdi", dados.get("idCliente"));
				dados = (HashMap<String, Object>)dados.get("pessoa");
				retorno.put("nrIdentificacao", dados.get("nrIdentificacaoFormatado"));
				retorno.put("nmPessoa",dados.get("nmPessoa"));				
			}			
		}									
		cliente = new ArrayList<Map<String,Object>>();
		cliente.add(retorno);
		
		return cliente;	
	}
	
	/**
	 * Lookup de Cliente
	 * @param  criteria
	 * @return List
	 */
	public List findCliente(TypedFlatMap criteria) {
		
		List<Map<String,Object>> cliente = clienteService.findClienteByNrIdentificacao(criteria.getString("nrIdentificacao")); 
		
		Map<String,Object> retorno = new HashMap<String, Object>();
		if(!cliente.isEmpty()){		
			Map<String,Object> dados = (HashMap<String, Object>)cliente.get(0);
			retorno.put("idCliente", dados.get("idCliente"));
			dados = (HashMap<String, Object>)dados.get("pessoa");
			retorno.put("nrIdentificacao", dados.get("nrIdentificacaoFormatado"));
			retorno.put("nmPessoa",dados.get("nmPessoa"));
		}									
		cliente = new ArrayList<Map<String,Object>>();
		cliente.add(retorno);
		
		return cliente;
	}	
	
	/**
	 * Remove uma lista de ClienteEDI
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		clienteEDIService.removeByIds(ids);
	}
	
	/**
	 * Remove a entidade pelo ID
	 * @param id
	 */
	public void removeById(Long id) {
		clienteEDIService.removeById(id);
	}		
		
	public ClienteEDIService getClienteEDIService() {
		return clienteEDIService;
	}

	public void setClienteEDIService(ClienteEDIService clienteEDIService) {
		this.clienteEDIService = clienteEDIService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

}