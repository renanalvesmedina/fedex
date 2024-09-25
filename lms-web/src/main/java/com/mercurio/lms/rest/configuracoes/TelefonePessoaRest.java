package com.mercurio.lms.rest.configuracoes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.rest.PaginacaoUtil;
import com.mercurio.lms.util.FormatUtils;

/**
 * Rest responsável pelo controle da tela manter telefone pessoa.
 * 
 */
@Path("/configuracoes/manterTelefonePessoa")
public class TelefonePessoaRest extends BaseRest{

	@InjectInJersey
	private TelefoneContatoService telefoneContatoService;
	
	@InjectInJersey
	private TelefoneEnderecoService telefoneEnderecoService; 
		
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
		
	@InjectInJersey
	private ContatoService contatoService;
	
	@InjectInJersey
	private EnderecoPessoaService enderecoPessoaService;
	
	/**
	 * Retorna listagem de registros de acordo com o filtro informado.
	 * 
	 * @param filtro
	 * @return Response
	 */
	@POST
	@Path("/find")
	public Response find(FiltroPaginacaoDto filtro) {
		Integer limiteRegistros = getLimiteRegistros(filtro);
				
		final TypedFlatMap tfm = populateCriteria(filtro);		
		tfm.putAll(PaginacaoUtil.getPaginacao(filtro, limiteRegistros));
		
		Long idPessoa = tfm.getLong("idPessoa");
		List<Map<String, Object>> list = telefoneEnderecoService.findTelefonePessoa(idPessoa);
		
		Integer count = telefoneEnderecoService.getRowCount(tfm);
		
		return getReturnFind(list, count);
	}

	/**
	 * Popula dados para a pesquisa.
	 * 
	 * @param filtro
	 * @return TypedFlatMap
	 */
	private TypedFlatMap populateCriteria(FiltroPaginacaoDto filtro) {
		TypedFlatMap criteria = new TypedFlatMap(filtro.getFiltros());

		criteria.put("pessoa.idPessoa", criteria.getLong("idPessoa"));

		return criteria;
	}
	
	private Integer getLimiteRegistros(FiltroPaginacaoDto filtro) {
		return Boolean.TRUE.equals(filtro.getReport()) ? Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_CSV", false).getDsConteudo()) : Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_GRID", false).getDsConteudo());
	}
	
	/**
	 * Retorna um registro para a tela de detalhamento.
	 * 
	 * @param id
	 * @param idProcessoWorkflow
	 * 
	 * @return Response
	 */
	@GET
	@Path("/findById")
	public Response findById(@QueryParam("id") Long id) {		
		return Response.ok(findTelefoneById(id)).build();
	}
	
	/**
	 * Remove item da tabela de endereço pessoa.
	 * 
	 * @param ids
	 */
	@GET
	@Path("removeTelefonePessoaById")
	public Response removeTelefoneById(@QueryParam("id") Long id) {
		telefoneEnderecoService.removeById(id);
		
		return Response.ok().build();
	}
	
	/**
	 * Retorna um registro para a tela de detalhamento, informando que não é
	 * workflow.
	 * 
	 * @param id
	 *  
	 * @return TypedFlatMap
	 */
	private TypedFlatMap findTelefoneById(Long id){						
		return getResult(getTelefone(id));
	}
	
	private TypedFlatMap getResult(TelefoneEndereco telefonePessoa) {
		TelefoneEndereco telefone =  telefoneEnderecoService.findById(telefonePessoa.getIdTelefoneEndereco());

    	if(telefone == null){
    		throw new BusinessException("LMS-01194", new Object[]{""});
    	}
    			
		return createResultMap(telefonePessoa);
	}

	/**
	 * Retorna um registro.
	 * 
	 * @param id
	 * @return Telefone
	 */
	private TelefoneEndereco getTelefone(Long id) {
		return telefoneEnderecoService.findById(id);
	}
	
	/**
	 * Popula mapa com os atributos que serão necessários para visualizar a
	 * tela.
	 * 
	 * @param telefonePessoa
	 * 
	 * @return TypedFlatMap
	 */
	private TypedFlatMap createResultMap(TelefoneEndereco telefonePessoa) {
		TypedFlatMap result = new TypedFlatMap();
		
		getDadosTelefone(telefonePessoa, result);		
		getContatos(telefonePessoa, result);
		getEnderecoPessoa(telefonePessoa, result);
    	getDadosPessoa(telefonePessoa, result);
    	getTelefonesContato(telefonePessoa, result);    	
    	getListEnderecoPessoa(telefonePessoa, result);
    	
		return result;
	}

	/**
	 * @param telefonePessoa
	 * @param result
	 */
	private void getListEnderecoPessoa(TelefoneEndereco telefonePessoa,
			TypedFlatMap result) {
		if(telefonePessoa.getPessoa() == null){
			return;
		}
		    	    	
    	result.put("enderecos", enderecoPessoaService.findEnderecosPessoa(telefonePessoa.getPessoa().getIdPessoa()));
	}


	/**
	 * @param telefonePessoa
	 * @param result
	 */
	private void getDadosPessoa(TelefoneEndereco telefonePessoa,	TypedFlatMap result) {
		result.put("idPessoa", telefonePessoa.getPessoa().getIdPessoa());
    	result.put("nrIdentificacao", FormatUtils.formatIdentificacao(telefonePessoa.getPessoa()));
    	result.put("tpIdentificacao", telefonePessoa.getPessoa().getTpIdentificacao().getDescriptionAsString());
    	result.put("nmPessoa",telefonePessoa.getPessoa().getNmPessoa());
	}

	

	/**
	 * @param telefonePessoa
	 * @param result
	 */
	private void getDadosTelefone(TelefoneEndereco telefonePessoa,TypedFlatMap result) {
		result.put("idTelefone", telefonePessoa.getIdTelefoneEndereco());
		result.put("dddTelefone", telefonePessoa.getDddTelefone());
		result.put("nrTelefone", telefonePessoa.getNrTelefone());
		result.put("tpTelefone", telefonePessoa.getTpTelefone());
		result.put("tpUso", telefonePessoa.getTpUso());
		result.put("nrDdd",telefonePessoa.getNrDdd());
		result.put("nrDdi",telefonePessoa.getNrDdi());		
	}

	private void getEnderecoPessoa(TelefoneEndereco telefonePessoa,
			TypedFlatMap result) {
		if(telefonePessoa.getEnderecoPessoa()!= null){
			Map<String,Object> endereco = new HashMap<String, Object>();
			endereco.put("idEnderecoPessoa",telefonePessoa.getEnderecoPessoa().getIdEnderecoPessoa());
			result.put("enderecoPessoa",endereco);			
		}
	}

	@SuppressWarnings("unchecked")
	private void getContatos(TelefoneEndereco telefonePessoa,TypedFlatMap result) {
		List<Contato> contatos = contatoService.findContatosByIdPessoa(telefonePessoa.getPessoa().getIdPessoa());
		
		List<Map<String, Object>> contatosRetorno = new ArrayList<Map<String, Object>>();
		for (Contato c : contatos) {
			Map<String, Object> contato = new HashMap<String, Object>();
			contato.put("nome", c.getNmContato());
			contato.put("idContato", c.getIdContato());								
			contatosRetorno.add(contato);
		}
		
		result.put("contatos",contatosRetorno);
	}

	private void getTelefonesContato(TelefoneEndereco telefonePessoa,
			TypedFlatMap result) {
		List<Map<String, Object>> listContatos = new ArrayList<Map<String, Object>>();
		
		List<TelefoneContato> listTelefoneContato = telefoneContatoService.findByIdTelefoneEndereco(telefonePessoa.getIdTelefoneEndereco());		
		
		for (TelefoneContato telefoneContato : listTelefoneContato) {
			Map<String, Object> contato = new HashMap<String, Object>();			
			contato.put("nome", telefoneContato.getContato().getNmContato());
			contato.put("idTelefoneContato", telefoneContato.getIdTelefoneContato());
			contato.put("idContato", telefoneContato.getContato().getIdContato());			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("contato", contato);
			map.put("nrRamal", telefoneContato.getNrRamal());
			
			listContatos.add(map);
		}
		
		result.put("listContatos",listContatos);
	}
	
	/**
	 * Grava um registro.
	 * 
	 * @param bean
	 * @return Response
	 */
	@POST
	@Path("store")
    public Response store(Map<String, Object> bean) {				
		Object id = bean.get("idTelefone");
		
		
		TelefoneEndereco telefone = null;
		
		if(id == null){
			telefone = new TelefoneEndereco();
		} else {			
			telefone = getTelefone(NumberUtils.createLong(id.toString()));
		}	
		
		return doStore(bean, telefone);
    }
		
	/**
	 * Executa o store das alterações.
	 * 
	 * @param bean
	 * @param telefonePessoa
	 * 
	 * @return Response
	 */
	@SuppressWarnings("unchecked")
	private Response doStore(Map<String, Object> bean, TelefoneEndereco telefonePessoa) {		
		telefonePessoa = populateTelefone(bean, telefonePessoa);
		
		Long idTelefone = (Long) telefoneEnderecoService.store(telefonePessoa, MapUtils.getMap(bean, "listContatos"));
    	  	    			
    	return Response.ok(findTelefoneById(idTelefone)).build();
	}
	
	/**
	 * Popula um objeto endereço pessoa de acordo com o bean.
	 * 
	 * @param bean
	 * @param telefonePessoa
	 * 
	 * @return Telefone
	 */
	private TelefoneEndereco populateTelefone(Map<String, Object> bean,TelefoneEndereco telefonePessoa) {
		setTelefone(bean, telefonePessoa);
		setPessoa(bean, telefonePessoa);
		setEndereco(bean,telefonePessoa);
				
		return telefonePessoa;
	}

	@SuppressWarnings("unchecked")
	private void setEndereco(Map<String, Object> bean,	TelefoneEndereco telefonePessoa) {
		Object enderecoPessoaMap = bean.get("enderecoPessoa");
		if(enderecoPessoaMap != null){
			EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdFetchTipoEnderecoPessoas(MapUtils.getLong((Map<String, Object>) enderecoPessoaMap, "idEnderecoPessoa"));
			telefonePessoa.setEnderecoPessoa(enderecoPessoa);			
		}else{
			telefonePessoa.setEnderecoPessoa(null);
		}
	}

	private void setTelefone(Map<String, Object> bean, TelefoneEndereco telefonePessoa) {
		telefonePessoa.setIdTelefoneEndereco(MapUtils.getLong(bean,"idTelefone"));
		telefonePessoa.setNrDdd(MapUtils.getString(bean,"dddTelefone"));
		telefonePessoa.setNrTelefone(MapUtils.getString(bean,"nrTelefone"));
		telefonePessoa.setTpTelefone(getValueDomain(bean, "tpTelefone"));
		telefonePessoa.setTpUso(getValueDomain(bean, "tpUso"));
		telefonePessoa.setNrDdd(MapUtils.getString(bean,"nrDdd"));
		telefonePessoa.setNrDdi(MapUtils.getString(bean,"nrDdi"));
	}

	private DomainValue getValueDomain(Map<String,Object> bean,String chave){
		@SuppressWarnings("unchecked")
		Map<String,String> domain =  (Map<String, String>) bean.get(chave);		
		return new DomainValue(domain.get("value"));
	}

	/**
	 * @param bean
	 * @param telefonePessoa
	 */
	private void setPessoa(Map<String, Object> bean,TelefoneEndereco telefonePessoa) {
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(NumberUtils.createLong(bean.get("idPessoa").toString()));
		telefonePessoa.setPessoa(pessoa);
	}
}