package com.mercurio.lms.rest.configuracoes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.service.ExecuteWorkflowProprietarioService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.rest.PaginacaoUtil;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.json.YearMonthDaySerializer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Rest responsável pelo controle da tela manter dados bancários.
 * 
 */
@Path("/configuracoes/manterDadosBancarios")
public class DadosBancariosRest  extends BaseRest {

	@InjectInJersey
	private ContaBancariaService contaBancariaService; 
		
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
		
	@InjectInJersey
	private ExecuteWorkflowProprietarioService executeWorkflowProprietarioService;
		
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
		List<Map<String, Object>> list = contaBancariaService.findDadosBancariosPessoa(idPessoa);
		
		Integer count = contaBancariaService.getRowCount(tfm);
		
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
	 * 
	 * @return Response
	 */
	@GET
	@Path("/findById")
	public Response findById(@QueryParam("id") Long id) {		
		return Response.ok(findContaBancariaById(id)).build();
	}
	
	/**
	 * Remove item da tabela de conta bancaria.
	 * 
	 * @param ids
	 */
	@GET
	@Path("removeDadosBancariosById")
	public Response removeDadosBancariosById(@QueryParam("id") Long id) {
		contaBancariaService.removeById(id);
		
		return Response.ok().build();
	}
	
	/**
	 * Retorna um registro para a tela de detalhamento.
	 * 
	 * @param id
	 *  
	 * @return TypedFlatMap
	 */
	private TypedFlatMap findContaBancariaById(Long id){						
		return getResult(getContaBancaria(id));
	}
	
	/**
	 * Configura Map de retorno para tela.
	 * 
	 * @param contaBancaria
	 * @return TypedFlatMap
	 */
	private TypedFlatMap getResult(ContaBancaria contaBancaria) {    	
		TypedFlatMap result = createResultMap(contaBancaria);
		
		defineDisabled(result, contaBancaria);		
		
		return result;
	}

	/**
	 * Define comportamento específico para habilitar botões.
	 * 
	 * @param result
	 * @param contaBancaria
	 */
	private void defineDisabled(TypedFlatMap result, ContaBancaria contaBancaria){
		result.put("disabled", (contaBancaria.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual()))?Boolean.TRUE:Boolean.FALSE); 
	}
	
	/**
	 * Retorna um registro.
	 * 
	 * @param id
	 * @return ContaBancaria
	 */
	private ContaBancaria getContaBancaria(Long id) {
		return contaBancariaService.findById(id);
	}
	
	/**
	 * Popula mapa com os atributos que serão necessários para visualizar a
	 * tela.
	 * 
	 * @param contaBancaria
	 * 
	 * @return TypedFlatMap
	 */
	private TypedFlatMap createResultMap(ContaBancaria contaBancaria) {
		TypedFlatMap result = new TypedFlatMap();
				    	    	
		getDadosBancarios(contaBancaria, result);				
		getBanco(contaBancaria, result);		
		getAgenciaBancaria(contaBancaria, result);		
		getPessoa(contaBancaria, result);
		
		return result;
	}

	/**
	 * @param contaBancaria
	 * @param result
	 */
	private void getDadosBancarios(ContaBancaria contaBancaria,
			TypedFlatMap result) {
		result.put("idContaBancaria", contaBancaria.getIdContaBancaria());
		result.put("nrContaBancaria", contaBancaria.getNrContaBancaria());
		result.put("dvContaBancaria", contaBancaria.getDvContaBancaria());
		result.put("dtVigenciaInicial", YearMonthDaySerializer.formatter(contaBancaria.getDtVigenciaInicial()));
		result.put("tpConta", contaBancaria.getTpConta());
		
		if(contaBancaria.getDtVigenciaFinal() != null){
			result.put("dtVigenciaFinal", YearMonthDaySerializer.formatter(contaBancaria.getDtVigenciaFinal()));
		}		
	}

	/**
	 * @param contaBancaria
	 * @param result
	 */
	private void getPessoa(ContaBancaria contaBancaria, TypedFlatMap result) {
		result.put("idPessoa", contaBancaria.getPessoa().getIdPessoa());
    	result.put("nrIdentificacao", FormatUtils.formatIdentificacao(contaBancaria.getPessoa()));
    	result.put("tpIdentificacao", contaBancaria.getPessoa().getTpIdentificacao().getDescriptionAsString());
    	result.put("nmPessoa",contaBancaria.getPessoa().getNmPessoa());
	}

	/**
	 * @param contaBancaria
	 * @param result
	 */
	private void getBanco(ContaBancaria contaBancaria, TypedFlatMap result) {
		Map<String, Object> banco = new HashMap<String, Object>();
		banco.put("idBanco", contaBancaria.getAgenciaBancaria().getBanco().getIdBanco());
		banco.put("nrBanco", contaBancaria.getAgenciaBancaria().getBanco().getNrBanco());
		banco.put("nmBanco", contaBancaria.getAgenciaBancaria().getBanco().getNmBanco());
		
		result.put("banco", banco);
	}

	/**
	 * @param contaBancaria
	 * @param result
	 */
	private void getAgenciaBancaria(ContaBancaria contaBancaria,
			TypedFlatMap result) {
		Map<String, Object> agenciaBancaria = new HashMap<String, Object>();
		agenciaBancaria.put("idAgenciaBancaria", contaBancaria.getAgenciaBancaria().getIdAgenciaBancaria());
		agenciaBancaria.put("nrAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNrAgenciaBancaria());
		agenciaBancaria.put("nmAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNmAgenciaBancaria());
		result.put("agenciaBancaria", agenciaBancaria);
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
		Long id = MapUtils.getLong(bean, "idContaBancaria");
				
		ContaBancaria contaBancaria = null;
		
		if(id == null){
			contaBancaria = new ContaBancaria();			
		} else {			
			contaBancaria = getContaBancaria(id);
		}	
		
		return doStore(bean, contaBancaria);
    }
	
	/**
	 * Executa o store das alterações.
	 * 
	 * @param bean
	 * @param id
	 * @param anexos
	 * 
	 * @return Response
	 */
	private Response doStore(Map<String, Object> bean, ContaBancaria contaBancaria) {		
		contaBancaria = populateContaBancaria(bean, contaBancaria);
		
		Long idContaBancaria = (Long) contaBancariaService.store(contaBancaria);
		
		executeWorkflowProprietarioService.executeWorkflowPendencia(contaBancaria.getPessoa(),ExecuteWorkflowProprietarioService.DADOS_BANCARIOS,false);
    	  	    			
    	return Response.ok(findContaBancariaById(idContaBancaria)).build();
	}
	
	/**
	 * Popula um objeto conta bancária de acordo com o bean.
	 * 
	 * @param bean
	 * @param contaBancaria
	 * 
	 * @return ContaBancaria
	 */
	private ContaBancaria populateContaBancaria(Map<String, Object> bean,
			ContaBancaria contaBancaria) {		
		setDadosContaBancaria(bean, contaBancaria);		
		setAgenciaBancaria(bean, contaBancaria);			
		setPessoa(bean, contaBancaria);
		
		return contaBancaria;
	}

	/**
	 * @param bean
	 * @param contaBancaria
	 */
	private void setAgenciaBancaria(Map<String, Object> bean,
			ContaBancaria contaBancaria) {
		Banco banco = new Banco();		
		banco.setIdBanco(MapUtils.getLong(MapUtils.getMap(bean, "banco"), "idBanco"));
		
		AgenciaBancaria agenciaBancaria = new AgenciaBancaria();
		agenciaBancaria.setIdAgenciaBancaria(MapUtils.getLong(MapUtils.getMap(bean, "agenciaBancaria"), "idAgenciaBancaria"));
		agenciaBancaria.setBanco(banco);
		
    	contaBancaria.setAgenciaBancaria(agenciaBancaria);
	}

	/**
	 * @param bean
	 * @param contaBancaria
	 */
	private void setDadosContaBancaria(Map<String, Object> bean,
			ContaBancaria contaBancaria) {
		contaBancaria.setNrContaBancaria(MapUtils.getString(bean, "nrContaBancaria"));
		contaBancaria.setDvContaBancaria(MapUtils.getString(bean, "dvContaBancaria"));
		contaBancaria.setDtVigenciaInicial(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtVigenciaInicial"));
		contaBancaria.setTpConta(RestPopulateUtils.getDomainValue(bean.get("tpConta")));    	
		
		if(bean.get("dtVigenciaFinal") != null){
			contaBancaria.setDtVigenciaFinal(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtVigenciaFinal"));	
		}
	}

	/**
	 * @param bean
	 * @param contaBancaria
	 */
	private void setPessoa(Map<String, Object> bean, ContaBancaria contaBancaria) {
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(MapUtils.getLong(bean, "idPessoa"));
		contaBancaria.setPessoa(pessoa);
	}
}