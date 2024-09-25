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
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneContatoService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.rest.PaginacaoUtil;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.utils.Closure;
import com.mercurio.lms.rest.utils.ListResponseBuilder;
import com.mercurio.lms.util.FormatUtils;

/**
 * Rest responsável pelo controle da tela manter contato pessoa.
 * 
 */
@Path("/configuracoes/manterContatoPessoa")
public class ContatoPessoaRest {

	@InjectInJersey
	private ContatoService contatoService; 
		
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;
			
	@InjectInJersey
	private TelefoneContatoService telefoneContatoService;
	
	@InjectInJersey
	private TelefoneEnderecoService telefoneEnderecoService;
	
	
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
		
		return new ListResponseBuilder(filtro, limiteRegistros, reportExecutionManager.getReportOutputDir(), "contatoPessoa", filtro.getColumns())
			.findClosure(new Closure<List<Map<String,Object>>>() {
			
				@Override
				@SuppressWarnings({ "unchecked", "rawtypes" })
				public List<Map<String, Object>> execute() {
					ResultSetPage rs = contatoService.findPaginatedContatoPessoa(tfm);
					
					return new ListToMapConverter<Contato>().mapRows(rs.getList(), new RowMapper<Contato>() {
						@Override
						public Map<String, Object> mapRow(Contato o) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("idContato", o.getIdContato());
							map.put("nmContato", o.getNmContato());
							map.put("dsEmail", o.getDsEmail());
							map.put("dsDepartamento", o.getDsDepartamento());
							map.put("dsFuncao", o.getDsFuncao());
							return map;
						}
					});
				}
				
			})
			.rowCountClosure(new Closure<Integer>() {
						
				@Override
				public Integer execute() {
					return contatoService.getRowCountContato(tfm);
				}
				
			})
			.build();
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
		return Response.ok(findContatoById(id)).build();
	}
	
	/**
	 * Remove item da tabela de endereço pessoa.
	 * 
	 * @param ids
	 */
	@GET
	@Path("removeContatoPessoaById")
	public Response removeContatoById(@QueryParam("id") Long id) {
		contatoService.removeById(id);
		
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
	private TypedFlatMap findContatoById(Long id){						
		return getResult(getContato(id));
	}
	
	private TypedFlatMap getResult(Contato contatoPessoa) {
		Contato contato =  contatoService.findById(contatoPessoa.getIdContato());

    	if(contato == null){
    		throw new BusinessException("LMS-01194", new Object[]{""});
    	}
    					
		return createResultMap(contatoPessoa);
	}
	
	/**
	 * Retorna um registro.
	 * 
	 * @param id
	 * @return Contato
	 */
	private Contato getContato(Long id) {
		return contatoService.findContatoById(id);
	}
	
	/**
	 * Popula mapa com os atributos que serão necessários para visualizar a
	 * tela.
	 * 
	 * @param contatoPessoa
	 * 
	 * @return TypedFlatMap
	 */
	private TypedFlatMap createResultMap(Contato contatoPessoa) {
		TypedFlatMap result = new TypedFlatMap();
		
		getDadosContato(contatoPessoa, result);
    	getDadosPessoa(contatoPessoa, result);
    	getDadosTelefone(contatoPessoa,result);    	
    	
		return result;
	}

	private void getDadosTelefone(Contato contatoPessoa, TypedFlatMap result) {
		List<Map<String, Object>> listTelefones = new ArrayList<Map<String, Object>>();

		List<TelefoneContato> listTelefoneContato = getTelefonesContato(contatoPessoa);
		
		for (TelefoneContato telefoneContato : listTelefoneContato) {
			Map<String, Object> telefone = new HashMap<String, Object>();
			telefone.put("idTelefoneContato", telefoneContato.getIdTelefoneContato());
			telefone.put("idTelefoneEndereco", telefoneContato.getTelefoneEndereco().getIdTelefoneEndereco());
			telefone.put("numero", telefoneContato.getTelefoneEndereco().getDddTelefone());
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("telefone", telefone);			
			map.put("nrRamal", telefoneContato.getNrRamal());
			
			listTelefones.add(map);				
		}
		
		result.put("listTelefones", listTelefones);
	}

	@SuppressWarnings("unchecked")
	private List<TelefoneContato> getTelefonesContato(Contato contatoPessoa) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("contato.idContato", contatoPessoa.getIdContato());		
		criteria.put("cliente.idCliente", contatoPessoa.getPessoa().getIdPessoa());
		
		return telefoneContatoService.findTelefoneByContato(criteria);
	}

	@SuppressWarnings("unchecked")
	private void getTelefonesPessoa(Contato contatoPessoa, TypedFlatMap result) {
		List<TelefoneEndereco> listTelefoneEndereco = telefoneEnderecoService.findByIdPessoa(contatoPessoa.getPessoa().getIdPessoa());
		
		List<Map<String, Object>> telefones = new ArrayList<Map<String, Object>>();
		
		for (TelefoneEndereco telefoneEndereco : listTelefoneEndereco) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("numero", telefoneEndereco.getDddTelefone());
			map.put("idTelefoneEndereco", telefoneEndereco.getIdTelefoneEndereco());			
			
			telefones.add(map);
		}
		
		result.put("telefones", telefones);
	}

	/**
	 * @param contatoPessoa
	 * @param result
	 */
	private void getDadosPessoa(Contato contatoPessoa,	TypedFlatMap result) {
		result.put("idPessoa", contatoPessoa.getPessoa().getIdPessoa());
    	result.put("nrIdentificacao", FormatUtils.formatIdentificacao(contatoPessoa.getPessoa()));
    	result.put("tpIdentificacao", contatoPessoa.getPessoa().getTpIdentificacao().getDescriptionAsString());
    	result.put("nmPessoa",contatoPessoa.getPessoa().getNmPessoa());
    	getTelefonesPessoa(contatoPessoa, result);
	}

	/**
	 * @param contatoPessoa
	 * @param result
	 */
	private void getDadosContato(Contato contatoPessoa,TypedFlatMap result) {
		result.put("idContato", contatoPessoa.getIdContato());		
		result.put("nmContato", contatoPessoa.getNmContato());
		result.put("dsEmail", contatoPessoa.getDsEmail());
		result.put("dsDepartamento", contatoPessoa.getDsDepartamento());
		result.put("dsFuncao", contatoPessoa.getDsFuncao());
		result.put("nrRepresentante", contatoPessoa.getNrRepresentante());
		result.put("tpContato", contatoPessoa.getTpContato());
		result.put("dtAniversario", contatoPessoa.getDtAniversario());		
		result.put("obContato", contatoPessoa.getObContato());
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
		Object id = bean.get("idContato");
		
		Contato contato = null;
		
		if(id == null){
			contato = new Contato();
		} else {			
			contato = getContato(NumberUtils.createLong(id.toString()));
		}	
		
		return doStore(bean, contato);
    }
	
	/**
	 * Executa o store das alterações.
	 * 
	 * @param bean
	 * @param contatoPessoa
	 * 
	 * @return Response
	 */
	@SuppressWarnings("unchecked")
	private Response doStore(Map<String, Object> bean, Contato contatoPessoa) {		
		contatoPessoa = populateContato(bean, contatoPessoa);
		
		Long idContato = (Long) contatoService.storeContato(contatoPessoa, MapUtils.getMap(bean, "listTelefones"));
    	  	    			
    	return Response.ok(findContatoById(idContato)).build();
	}
	
	/**
	 * Popula um objeto endereço pessoa de acordo com o bean.
	 * 
	 * @param bean
	 * @param contatoPessoa
	 * 
	 * @return Contato
	 */
	private Contato populateContato(Map<String, Object> bean,Contato contatoPessoa) {    	
		setContato(bean, contatoPessoa);
		setPessoa(bean, contatoPessoa);
		
		return contatoPessoa;
	}

	private void setContato(Map<String, Object> bean, Contato contatoPessoa) {				
		contatoPessoa.setNmContato(MapUtils.getString(bean,"nmContato")); 
		contatoPessoa.setDsEmail(MapUtils.getString(bean,"dsEmail"));
		contatoPessoa.setDsDepartamento(MapUtils.getString(bean,"dsDepartamento"));
		contatoPessoa.setDsFuncao(MapUtils.getString(bean,"dsFuncao"));
		contatoPessoa.setNrRepresentante(MapUtils.getLong(bean,"nrRepresentante"));
		contatoPessoa.setTpContato(getValueDomain(bean));
		contatoPessoa.setDtAniversario(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtAniversario"));
		contatoPessoa.setObContato(MapUtils.getString(bean,"obContato"));		
	}

	@SuppressWarnings("unchecked")
	private DomainValue getValueDomain(Map<String,Object> bean){
		Map<String,String> domain =  (Map<String, String>) bean.get("tpContato");		
		return new DomainValue(domain.get("value"));
	}
	
	/**
	 * @param bean
	 * @param contatoPessoa
	 */
	private void setPessoa(Map<String, Object> bean,Contato contatoPessoa) {
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(NumberUtils.createLong(bean.get("idPessoa").toString()));
		contatoPessoa.setPessoa(pessoa);
	}
}