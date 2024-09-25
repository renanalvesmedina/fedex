package com.mercurio.lms.edi.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.edi.model.ClienteEDIFilialEmbarcadora;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

/**
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.swt.manterClienteEDIEmbarcadoraAction"
 */
public class ManterClienteEDIEmbarcadoraAction {
	
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	
	
	/**
	 * Persiste a entidade ClienteEDIFilialEmbarcadora
	 * 
	 * @param param
	 * @return Map
	 */
	public Map<String, Object> store(Map<String, Object> param) {
		
		ClienteEDIFilialEmbarcadora pojo = new ClienteEDIFilialEmbarcadora();
		
		/*cliente*/
		Cliente cliente = new Cliente();
		cliente.setIdCliente((Long)param.get("idClienteEDI"));
		pojo.setCliente(cliente);
		
		/*clienteEmbarcador*/
		Cliente clienteEmbarcador = new Cliente();
		clienteEmbarcador.setIdCliente((Long)param.get("idCliente"));
		pojo.setClienteEmbarcador(clienteEmbarcador);
		
		/*filial*/
		Filial filial = new Filial();
		filial.setIdFilial((Long)param.get("idFilial"));
		pojo.setFilial(filial);
	
		clienteEDIFilialEmbarcadoraService.store(pojo);
		
		return param;				
	}
	
	
	/**
	 * Paginação da Aba Embarcadora (Tela Manter Cliente EDI) 
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage<Map<String, Object>> findPaginated(Map<String, Object> criteria) {
		ResultSetPage rsp = clienteEDIFilialEmbarcadoraService.findPaginated(new PaginatedQuery(criteria));					
		
		List<ClienteEDIFilialEmbarcadora> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(ClienteEDIFilialEmbarcadora embarcadora : list){
 			retorno.add(embarcadora.getMapped());
		}
				
		rsp.setList(retorno);
		return rsp;		
	}
	

	public ResultSetPage<Map<String, Object>> findPaginatedLookup(Map<String, Object> criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		ResultSetPage rsp = clienteEDIFilialEmbarcadoraService.findPaginatedLookup(tfmCriteria);		
		
		List<TypedFlatMap> list = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(list.size());
		
		for(TypedFlatMap embarcadora : list){
 			retorno.add(this.getMapped(embarcadora));
		}
				
		rsp.setList(retorno);
		return rsp;		
	}

	public Integer getRowCountLookup(Map criteria) {
		TypedFlatMap tfmCriteria = createFindCriteria(criteria);
		Integer rsp = clienteEDIFilialEmbarcadoraService.getRowCountLookup(tfmCriteria);		
		return rsp;
	}

	private TypedFlatMap createFindCriteria(Map criteria) {		
		TypedFlatMap tfmCriteria = new TypedFlatMap();
		tfmCriteria.put("pessoa.tpPessoa", criteria.get("tpPessoa"));
		tfmCriteria.put("pessoa.tpIdentificacao", criteria.get("tpIdentificacao"));
		tfmCriteria.put("pessoa.nrIdentificacao", criteria.get("nrIdentificacao"));
		tfmCriteria.put("pessoa.nmPessoa", criteria.get("nmPessoa"));
		tfmCriteria.put("nmFantasia", criteria.get("cliente"));		
		tfmCriteria.put("tpCliente", criteria.get("tipoCliente"));
		tfmCriteria.put("tpSituacao", criteria.get("situacao"));
		tfmCriteria.put("nrConta", criteria.get("numeroConta"));
		tfmCriteria.put("idFilial", criteria.get("idFilial"));
		tfmCriteria.put("idEmpresa", criteria.get("idEmpresa"));
    	tfmCriteria.put("_currentPage", criteria.get("_currentPage"));
    	tfmCriteria.put("_pageSize", criteria.get("_pageSize"));
    	tfmCriteria.put("_order", criteria.get("_order"));
    	return tfmCriteria;
	}
	private Map<String, Object> getMapped(TypedFlatMap embarcadora){
		
		Map<String, Object> param = new HashMap<String, Object>();
		
		/*idClienteEDIFilialEmbarcadora*/
		param.put("idClienteEDIFilialEmbarcadora", embarcadora.get("idClienteEDIFilialEmbarcadora"));
		/*identificacao*/
		param.put("tpIdentificacao", embarcadora.get("pessoa.tpIdentificacao.description"));
		param.put("nrIdentificacaoFormatado", embarcadora.get("pessoa.nrIdentificacaoFormatado"));
		/*cliente*/
		param.put("cliente", embarcadora.get("pessoa.nmFantasia"));
		/*nome*/
		param.put("nmPessoa", embarcadora.get("pessoa.nmPessoa"));
		/*numero conta*/
		param.put("numeroConta", embarcadora.get("pessoa.nrConta"));
		/*tipo cliente*/
		param.put("tipoCliente", embarcadora.get("tpCliente.description"));
		/*situacao*/
		param.put("situacao", embarcadora.get("tpSituacao.description"));
		/*filail*/
		param.put("sgFilial", embarcadora.get("filial.sgFilial"));
		/*idCliente*/
		param.put("idCliente", embarcadora.get("idCliente"));
		
		return param;
	}
	
	/**
	 * Lookup ClienteEDIFilialEmbarcadora 
	 * 
	 * @return List
	 */
	public List findLookup(Map criteria){
		
		List lista = new ArrayList();
		String identificacao = (String)criteria.get("identificacao");
		Long idCliente = (Long)criteria.get("idClienteEdi");
		if(identificacao == null || idCliente == null ){
			return lista;
		}
		
		lista = clienteEDIFilialEmbarcadoraService.findLookupEmbarcadora(idCliente,identificacao);		
		if (!lista.isEmpty() && lista.size() == 1) {
			ClienteEDIFilialEmbarcadora embarcadora = (ClienteEDIFilialEmbarcadora)lista.get(0);
			Map map = new HashMap();
			map.put("idClienteEDIFilialEmbarcadora", embarcadora.getIdClienteEDIFilialEmbarcadora());
			map.put("identificacao", embarcadora.getClienteEmbarcador().getPessoa().getNrIdentificacao());
			map.put("cliente", embarcadora.getClienteEmbarcador().getPessoa().getNmFantasia());
			lista.add(map);
			lista.remove(embarcadora);
		}
		return lista;		
	}	
	
	/**
	 * Remove uma lista de Embarcadoras
	 * @param ids
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		clienteEDIFilialEmbarcadoraService.removeByIds(ids);
	}	
	
	public void setClienteEDIFilialEmbarcadoraService(
			ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}	

}
