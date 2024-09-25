package com.mercurio.lms.rest.configuracoes;

import java.math.BigDecimal;
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
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Cep;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TipoEnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.service.CepService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoEnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoLogradouroService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.PaginacaoUtil;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Rest responsável pelo controle da tela manter endereços pessoa.
 * 
 */
@Path("/configuracoes/manterEnderecoPessoa")
public class EnderecoPessoaRest extends BaseRest {

	private static final String MAP_KEY_DESCRIPTION = "description";

	private static final String MAP_KEY_DS_BAIRRO = "dsBairro";

	private static final String MAP_KEY_DT_VIGENCIA_INICIAL = "dtVigenciaInicial";

	private static final String MAP_KEY_DS_ENDERECO = "dsEndereco";

	private static final String MAP_KEY_ID_ENDERECO_PESSOA = "idEnderecoPessoa";

	private static final String MAP_KEY_ID_PESSOA = "idPessoa";

	private static final String MAP_KEY_LIST_TIPO_ENDERECO = "listTipoEndereco";

	private static final String MAP_KEY_NR_CEP = "nrCep";

	private static final String MAP_KEY_NR_LONGITUDE_TMP = "nrLongitudeTmp";

	private static final String MAP_KEY_NR_LATITUDE_TMP = "nrLatitudeTmp";

	private static final String MAP_KEY_QUALIDADE = "qualidade";

	private static final String MAP_KEY_TP_LOGRADOURO = "tpLogradouro";

	@InjectInJersey
	private EnderecoPessoaService enderecoPessoaService; 
	
	@InjectInJersey
	private TipoEnderecoPessoaService tipoEnderecoPessoaService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
		
	@InjectInJersey
	private CepService cepService;
	
	@InjectInJersey
	private TipoLogradouroService tipoLogradouroService;
	
	@InjectInJersey
	private ProprietarioService proprietarioService;	
	
	@InjectInJersey
	private PessoaService pessoaService; 
	
	@InjectInJersey
	DomainValueService domainValueService;	
	
	@InjectInJersey
	FilialService filialService;	
	
	/**
	 * Retorna listagem de registros de acordo com o filtro informado.
	 * 
	 * @param filtro
	 * @return Response
	 */
	@POST
	@Path("/find")
	public Response find(FiltroPaginacaoDto filtro) {
		final TypedFlatMap tfm = populateCriteria(filtro);		
		tfm.putAll(PaginacaoUtil.getPaginacao(filtro, getLimiteRegistros(filtro)));
		
		Long idPessoa = MapUtils.getLong(filtro.getFiltros(), MAP_KEY_ID_PESSOA);
		List<Map<String, Object>> list = enderecoPessoaService.findEnderecosPessoa(idPessoa);
		
		Integer count = enderecoPessoaService.getRowCountEnderecoPessoa(tfm);
		
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

		criteria.put("pessoa.idPessoa", criteria.getLong(MAP_KEY_ID_PESSOA));

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
		return Response.ok(findEnderecoPessoaById(id)).build();
	}
	
	/**
	 * Remove item da tabela de endereço pessoa.
	 * 
	 * @param ids
	 */
	@GET
	@Path("removeEnderecoPessoaById")
	public Response removeEnderecoPessoaById(@QueryParam("id") Long id) {
		enderecoPessoaService.removeById(id);
		
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
	private TypedFlatMap findEnderecoPessoaById(Long id){						
		return getResult(getEnderecoPessoa(id));
	}
	
	private TypedFlatMap getResult(EnderecoPessoa enderecoPessoa) {
		EnderecoPessoa enderecoPessoaPadrao = enderecoPessoaService.findEnderecoPessoaPadrao(enderecoPessoa.getPessoa().getIdPessoa());

    	if(enderecoPessoaPadrao == null){
    		throw new BusinessException("LMS-01194", new Object[]{""});
    	}
    	
		TypedFlatMap result = createResultMap(enderecoPessoa);
		
		defineDisabled(result, enderecoPessoa);		
		defineRequired(result, enderecoPessoa.getPessoa().getIdPessoa());
		return result;
	}

	private void defineRequired(TypedFlatMap result, Long idPessoa) {
		Boolean requiredCoordenadas = filialService.validateEnderecoPessoaDeUmaFilialByIdPessoa(idPessoa);
		result.put("requiredCoordenadas", requiredCoordenadas);
	}
	
	/**
	 * Define comportamento específico para habilitar botões.
	 * 
	 * @param result
	 * @param enderecoPessoa
	 * @param enderecoPessoaPadrao
	 */
	private void defineDisabled(TypedFlatMap result, EnderecoPessoa enderecoPessoa){
		boolean dtVigencia = enderecoPessoa.getDtVigenciaInicial().isBefore(JTDateTimeUtils.getDataAtual()) && !enderecoPessoa.getBlEnderecoMigrado();
		
		/*
		 * Se a data de vigencia inicial é menor que a data atual e que o
		 * blEndrecoMigrado é false, desabilitar os campos da tela.
		 */
    	if (dtVigencia){
    		result.put("disabled", Boolean.TRUE);
    	}

		/*
		 * Se o endereço é um endereco residencial ou comercial, desabilitar a
		 * data vigencia final.
		 */
    	if (tipoEnderecoPessoaService.executeIsComercialOrResidencial(enderecoPessoa.getIdEnderecoPessoa())){
    		result.put("disabledDtVigenciaFinal", Boolean.TRUE);
    		
    		if (dtVigencia && (enderecoPessoa.getDtVigenciaFinal() == null || (enderecoPessoa.getDtVigenciaFinal().isAfter(JTDateTimeUtils.getDataAtual().minusDays(1))))){
    			result.put("mensagemSubstituicao", configuracoesFacade.getMensagem("LMS-27093"));
    		}
    	}
	}
	
	/**
	 * Retorna um registro.
	 * 
	 * @param id
	 * @return EnderecoPessoa
	 */
	private EnderecoPessoa getEnderecoPessoa(Long id) {
		return enderecoPessoaService.findById(id);
	}
	
	/**
	 * Popula mapa com os atributos que serão necessários para visualizar a
	 * tela.
	 * 
	 * @param enderecoPessoa
	 * 
	 * @return TypedFlatMap
	 */
	private TypedFlatMap createResultMap(EnderecoPessoa enderecoPessoa) {
		TypedFlatMap result = new TypedFlatMap();
		
    	getDadosPessoa(enderecoPessoa, result);
    	getDadosEnderecoPessoa(enderecoPessoa, result);	
    	getDadosEnderecoPessoaVigente(result);
    	getTipoLogradouro(enderecoPessoa, result); 	
    	getPais(result, enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais());
		getUnidadeFederativa(result, enderecoPessoa.getMunicipio().getUnidadeFederativa());
		getMunicipio(result, enderecoPessoa.getMunicipio());
    	getTipoEndereco(enderecoPessoa, result);
    	getDadosUsuario(enderecoPessoa, result);
    	
		return result;
	}

	private TypedFlatMap getCurrentPais(){
		Pais pais = SessionUtils.getPaisSessao();
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("idPais", pais.getIdPais());
		result.put("nmPais", pais.getNmPais().getValue());
		result.put("sgPais", pais.getSgPais());
		result.put("cdIso", pais.getCdIso());
		
		return result;
	}
	
	/**
	 * Prepara a tela para um novo cadastro para um endereço de pessoa.
	 * 
	 * @param idPessoa
	 * 
	 * @return Map<String, Object>
	 */
	@GET
	@Path("/createEnderecoPessoa")
	@SuppressWarnings("unchecked")
	public Map<String, Object> createEnderecoPessoa(@QueryParam("id") Long idPessoa){
		TypedFlatMap result = new TypedFlatMap();
		
		Pessoa pessoa = pessoaService.findById(idPessoa);
		
		result.put(MAP_KEY_ID_PESSOA, idPessoa);
		result.put("tpPessoa", pessoa.getTpPessoa().getValue());
		result.put("nmPessoa", pessoa.getNmPessoa());
		result.put("nrIdentificacao", FormatUtils.formatIdentificacao(pessoa));
		result.put("tpIdentificacao", pessoa.getTpIdentificacao().getDescriptionAsString());		
		result.put(MAP_KEY_DT_VIGENCIA_INICIAL, JTDateTimeUtils.getDataAtual());		
		result.put("pais", getCurrentPais());
		result.put("enderecoPessoaVigente", getEnderecoVigente(result));
		result.put(MAP_KEY_LIST_TIPO_ENDERECO, getDefaultTipoEnderecoPessoa(pessoa));
		result.putAll(getDefaultCoordenadasByIdPessoa(idPessoa));
		defineRequired(result, idPessoa);
		return result;
	}

	private Map<String, Object> getDefaultCoordenadasByIdPessoa(Long idPessoa) {
		return enderecoPessoaService.loadCoordenadasTemporariaByIdPessoa(idPessoa);
	}

	/**
	 * Inicializa o tipo de endereço padrão para quando é o primeiro endereço da
	 * pessoa.
	 * 
	 * @param pessoa
	 * 
	 * @return Map<String, Object>
	 */
	private List<Map<String, Object>> getDefaultTipoEnderecoPessoa(Pessoa pessoa) {
		List<Map<String, Object>> listEnderecosPessoa = enderecoPessoaService.findEnderecosPessoa(pessoa.getIdPessoa());
		
		List<Map<String, Object>> listOptions = new ArrayList<Map<String, Object>>();
		
		if(listEnderecosPessoa.isEmpty()){
			if("F".equals(pessoa.getTpPessoa().getValue())){
				listOptions.add(getTpEndereco(null, "RES", "Residencial"));
			} else {
				listOptions.add(getTpEndereco(null, "COM", "Comercial"));
			}
		}
		
		return listOptions;
	}
	
	/**
	 * 
	 * @param idTipoEnderecoPessoa
	 * @param value
	 * @param description
	 * 
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getTpEndereco(Long idTipoEnderecoPessoa, String value, String description){
		Map<String, Object> tpEndereco = new HashMap<String, Object>();
		tpEndereco.put("enderecoPessoaId", idTipoEnderecoPessoa);
		tpEndereco.put("value", value);
		tpEndereco.put(MAP_KEY_DESCRIPTION, description);	
		
		Map<String, Object> tpEnderecoPessoa = new HashMap<String, Object>();
		tpEnderecoPessoa.put("tpEndereco", tpEndereco);
		
		return tpEnderecoPessoa;
	}
	
	/**
	 * 
	 * @param enderecoPessoa
	 * @param result
	 */
	@SuppressWarnings("unchecked")
	private void getTipoEndereco(EnderecoPessoa enderecoPessoa,	TypedFlatMap result) {		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("enderecoPessoa.idEnderecoPessoa", enderecoPessoa.getIdEnderecoPessoa());
		
		List<TipoEnderecoPessoa> listTipoEnderecoPessoa = tipoEnderecoPessoaService.find(criteria);
		
		List<Map<String, Object>> listOptions = new ArrayList<Map<String, Object>>();
		
		for (TipoEnderecoPessoa tipoEnderecoPessoa : listTipoEnderecoPessoa) {
			listOptions.add(getTpEndereco(tipoEnderecoPessoa.getIdTipoEnderecoPessoa(), 
					tipoEnderecoPessoa.getTpEndereco().getValue(), 
					tipoEnderecoPessoa.getTpEndereco().getDescriptionAsString()));
		}
		
		result.put(MAP_KEY_LIST_TIPO_ENDERECO, listOptions);
	}

	/**
	 * @param enderecoPessoa
	 * @param result
	 */
	private void getDadosPessoa(EnderecoPessoa enderecoPessoa,
			TypedFlatMap result) {
		result.put(MAP_KEY_ID_PESSOA, enderecoPessoa.getPessoa().getIdPessoa());
		result.put("tpPessoa", enderecoPessoa.getPessoa().getTpPessoa().getValue());
    	result.put("nrIdentificacao", FormatUtils.formatIdentificacao(enderecoPessoa.getPessoa()));
    	result.put("tpIdentificacao", enderecoPessoa.getPessoa().getTpIdentificacao().getDescriptionAsString());
    	result.put("nmPessoa",enderecoPessoa.getPessoa().getNmPessoa());
	}

	/**
	 * @param enderecoPessoa
	 * @param result
	 */
	private void getTipoLogradouro(EnderecoPessoa enderecoPessoa,
			TypedFlatMap result) {
		if(enderecoPessoa.getTipoLogradouro() != null){
    		TipoLogradouro logradouro = enderecoPessoa.getTipoLogradouro();
    		
    		Map<String,Object> tpLogradouro = new HashMap<String, Object>();
    		tpLogradouro.put("id",logradouro.getIdTipoLogradouro());
    		tpLogradouro.put(MAP_KEY_DESCRIPTION,logradouro.getDsTipoLogradouro().getValue());
    			
    		result.put(MAP_KEY_TP_LOGRADOURO, tpLogradouro);
    	}
	}

	/**
	 * @param result
	 * @param m
	 */
	private void getMunicipio(TypedFlatMap result, Municipio municipio) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idMunicipio", municipio.getIdMunicipio());
		map.put("nmMunicipio", municipio.getNmMunicipio());
		result.put("municipio", map);
	}

	/**
	 * @param result
	 * @param unidadeFederativa2
	 */
	private void getUnidadeFederativa(TypedFlatMap result,
			UnidadeFederativa unidadeFederativa) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idUnidadeFederativa", unidadeFederativa.getIdUnidadeFederativa());
		map.put("sgUnidadeFederativa", unidadeFederativa.getSgUnidadeFederativa());
		map.put("nmUnidadeFederativa", unidadeFederativa.getNmUnidadeFederativa());
		result.put("uf", map);
	}

	/**
	 * @param result
	 * @param pais
	 */
	private void getPais(TypedFlatMap result, Pais pais) {
		Map<String, Object> map = new HashMap<String, Object>();    	
		map.put("idPais", pais.getIdPais());
		map.put("cdIso", pais.getCdIso());
		map.put("nmPais", pais.getNmPais().getValue());
		map.put("sgPais", pais.getSgPais());
		result.put("pais", map);
	}

	/**
	 * @param enderecoPessoa
	 * @param result
	 */
	private void getDadosEnderecoPessoa(EnderecoPessoa enderecoPessoa,
			TypedFlatMap result) {
		result.put(MAP_KEY_ID_ENDERECO_PESSOA, enderecoPessoa.getIdEnderecoPessoa());
    	result.put(MAP_KEY_DS_ENDERECO, enderecoPessoa.getDsEndereco());
    	result.put("nrEndereco", enderecoPessoa.getNrEndereco());
    	result.put("obEnderecoPessoa", enderecoPessoa.getObEnderecoPessoa());
    	result.put(MAP_KEY_NR_CEP, enderecoPessoa.getNrCep());    	
    	result.put("dsComplemento", enderecoPessoa.getDsComplemento());    	
    	result.put(MAP_KEY_DS_BAIRRO, enderecoPessoa.getDsBairro());    	
    	
    	result.put("nrLatitude", enderecoPessoa.getNrLatitude());
    	result.put("nrLongitude", enderecoPessoa.getNrLongitude());
    	
    	result.put(MAP_KEY_NR_LATITUDE_TMP, enderecoPessoa.getNrLatitudeTmp());
    	result.put(MAP_KEY_NR_LONGITUDE_TMP, enderecoPessoa.getNrLongitudeTmp());    	
    	result.put(MAP_KEY_QUALIDADE, enderecoPessoa.getNrQualidade());    	
    	
    	result.put("enderecoCompleto", this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()));
    	result.put(MAP_KEY_DT_VIGENCIA_INICIAL, enderecoPessoa.getDtVigenciaInicial());   
    	
    	if(enderecoPessoa.getDtVigenciaFinal() != null){
    		result.put("dtVigenciaFinal", enderecoPessoa.getDtVigenciaFinal());  	
    	}    	
	}

	@SuppressWarnings("unchecked")
	private void getDadosEnderecoPessoaVigente(TypedFlatMap result) {
		result.put("enderecoPessoaVigente", getEnderecoVigente(result));
	}
	
	/**
	 * @param enderecoPessoa
	 * @param result
	 */
	private void getDadosUsuario(EnderecoPessoa enderecoPessoa,
			TypedFlatMap result) {
		if (enderecoPessoa.getUsuarioInclusao() != null) {
    		result.put("usuarioInclusao", enderecoPessoa.getUsuarioInclusao().getNmUsuario());
    	}
    	
		if (enderecoPessoa.getUsuarioAlteracao() != null) {
    		result.put("usuarioAlteracao", enderecoPessoa.getUsuarioAlteracao().getNmUsuario());
    	}
	}
	
	/**
	 * Retorna os tipos de logradouros existentes.
	 * 
	 * @return Response
	 */
	@GET
	@Path("populateTipoLogradouro")
	@SuppressWarnings("unchecked")
	public Response populateTipoLogradouro() {
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		
		List<TipoLogradouro> listaLogradouro = tipoLogradouroService.find(new HashMap<String, Object>());
		
		for (TipoLogradouro logradouro : listaLogradouro) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put(MAP_KEY_DESCRIPTION,logradouro.getDsTipoLogradouro().getValue());
			map.put("id", logradouro.getIdTipoLogradouro());
			retorno.add(map);			
		}
		
		return Response.ok(retorno).build(); 
	}
	
	/**
	 * Retorna dados do endereço do cep informado.
	 * 
	 * @param cep
	 * @return Response
	 */
	@GET
	@Path("/findCep")
	public Response findCep(@QueryParam("cep") String cep) {
		List<Cep> ceps = cepService.findCepByNrCep(cep);
		TypedFlatMap retorno = new TypedFlatMap();
		
		if(ceps != null && !ceps.isEmpty()){
			Cep cepResult = ceps.get(0);
			
	    	retorno.put(MAP_KEY_NR_CEP, cepResult.getNrCep());	    		    	
	    	retorno.put(MAP_KEY_DS_ENDERECO, cepResult.getNmLogradouro());	    	
	    	retorno.put(MAP_KEY_DS_BAIRRO, cepResult.getNmBairro());
	    	
	    	getPais(retorno, cepResult.getMunicipio().getUnidadeFederativa().getPais());	    	
	    	getUnidadeFederativa(retorno, cepResult.getMunicipio().getUnidadeFederativa());
	    	getMunicipio(retorno, cepResult.getMunicipio());
	    	
	    	if(cepResult.getDsTipoLogradouro() != null){
	    		getCepLogradouro(retorno, cepResult);	
	    	}	    	
		}
		
		return Response.ok(retorno).build();
	}
	
	/**
	 * Retorna se existe algum endereço vigente para a pessoa de acordo com a
	 * data de vigência inicial informada.
	 * 
	 * @param dtVigenciaInicial
	 * @return Response
	 */
	@POST
	@Path("/findEnderecoPessoaVigente")
	public Response findEnderecoPessoaVigente(Map<String,Object> bean) {		
		return Response.ok(getEnderecoVigente(bean)).build();
	}

	/**
	 * @param bean
	 * @param result
	 */
	private TypedFlatMap getEnderecoVigente(Map<String, Object> bean) {
		TypedFlatMap result = new TypedFlatMap();
		
		Boolean disabled = true;
		
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaSubstituir(
				MapUtils.getLong(bean, MAP_KEY_ID_PESSOA), 
				RestPopulateUtils.getYearMonthDayFromISO8601(bean, MAP_KEY_DT_VIGENCIA_INICIAL)); 
		
		if(enderecoPessoa != null){
			Long idEnderecoPessoa = MapUtils.getLong(bean, MAP_KEY_ID_ENDERECO_PESSOA);
			
			if(idEnderecoPessoa == null || idEnderecoPessoa.compareTo(enderecoPessoa.getIdEnderecoPessoa()) != 0){
				getDadosEnderecoPessoa(enderecoPessoa, result);
				getTipoLogradouro(enderecoPessoa, result); 	
		    	getPais(result, enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais());
				getUnidadeFederativa(result, enderecoPessoa.getMunicipio().getUnidadeFederativa());
				getMunicipio(result, enderecoPessoa.getMunicipio());
			
				disabled = false;
			}
		} 
		
		/*
		 * Apenas permite substituir, se houver algum endereço
		 * residencial/comercial vigente.
		 */
		result.put("disabled", disabled);
		
		return result;
	}

	/**
	 * Atualmente para efetuar a busca de cep é utilizada uma view no banco de
	 * dados, a qual possui apenas o ds_tipo_logradouro e não o id. <br>
	 * Para isto é feito um find a partir da descrição para localizar o
	 * logradouro correspondente.
	 * 
	 * @param retorno
	 * @param cepResult
	 */
	@SuppressWarnings("unchecked")
	private void getCepLogradouro(TypedFlatMap retorno, Cep cepResult) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("dsTipoLogradouro", cepResult.getDsTipoLogradouro());
		
		List<TipoLogradouro> listaTipoLogradouro = tipoLogradouroService.find(criteria);
		
		if(listaTipoLogradouro != null && !listaTipoLogradouro.isEmpty()){
			TipoLogradouro tipoLogradouro = listaTipoLogradouro.get(0);	    			
			
			Map<String, Object> tpLogradouro = new HashMap<String, Object>();
			tpLogradouro.put(MAP_KEY_DESCRIPTION, tipoLogradouro.getDsTipoLogradouro());
			tpLogradouro.put("id", tipoLogradouro.getIdTipoLogradouro());
			
			retorno.put(MAP_KEY_TP_LOGRADOURO, tpLogradouro);	
		}
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
		Long id = MapUtils.getLong(bean, MAP_KEY_ID_ENDERECO_PESSOA);
		
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		
		if(id == null){
			enderecoPessoa.setUsuarioInclusao(SessionUtils.getUsuarioLogado());
		} else {			
			enderecoPessoa = getEnderecoPessoa(id);
			enderecoPessoa.setUsuarioAlteracao(SessionUtils.getUsuarioLogado());
		}	
		
		return doStore(bean, enderecoPessoa);
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
	@SuppressWarnings("unchecked")
	private Response doStore(Map<String, Object> bean, EnderecoPessoa enderecoPessoa) {		
		enderecoPessoa = populateEnderecoPessoa(bean, enderecoPessoa);
				
		Long idEnderecoPessoa = (Long) enderecoPessoaService.storeEnderecoPessoa(
				enderecoPessoa, 
				MapUtils.getLong(bean, "idEnderecoPessoaSubstituido"),
				MapUtils.getMap(bean, MAP_KEY_LIST_TIPO_ENDERECO));
				
		enderecoPessoa.setIdEnderecoPessoa(idEnderecoPessoa);
		    	  	    			
    	return Response.ok(findEnderecoPessoaById(idEnderecoPessoa)).build();
	}
	
	
	/**
	 * Popula um objeto endereço pessoa de acordo com o bean.
	 * 
	 * @param bean
	 * @param enderecoPessoa
	 * 
	 * @return EnderecoPessoa
	 */
	private EnderecoPessoa populateEnderecoPessoa(Map<String, Object> bean,
			EnderecoPessoa enderecoPessoa) {
		setDadosEnderecoPessoa(bean, enderecoPessoa);    	
		setPessoa(bean, enderecoPessoa);				
		setMunicipio(bean, enderecoPessoa);		
    	setTipoLogradouro(bean, enderecoPessoa);
    	
		return enderecoPessoa;
	}

	/**
	 * @param bean
	 * @param enderecoPessoa
	 */
	private void setDadosEnderecoPessoa(Map<String, Object> bean, EnderecoPessoa enderecoPessoa) {
		enderecoPessoa.setDtVigenciaInicial(RestPopulateUtils.getYearMonthDayFromISO8601(bean, MAP_KEY_DT_VIGENCIA_INICIAL));		
		enderecoPessoa.setNrCep(MapUtils.getString(bean, MAP_KEY_NR_CEP));
    	enderecoPessoa.setDsEndereco(MapUtils.getString(bean, MAP_KEY_DS_ENDERECO));    	
    	enderecoPessoa.setDsComplemento(MapUtils.getString(bean, "dsComplemento"));
    	enderecoPessoa.setDsBairro(MapUtils.getString(bean, MAP_KEY_DS_BAIRRO));
    	enderecoPessoa.setObEnderecoPessoa(MapUtils.getString(bean, "obEnderecoPessoa"));		
    	enderecoPessoa.setDtVigenciaFinal(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtVigenciaFinal"));    	
    	enderecoPessoa.setNrEndereco(MapUtils.getString(bean, "nrEndereco"));
    	enderecoPessoa.setNrLatitude(this.convertCoordenadasParaBigDecimal(bean, "nrLatitude"));	
    	enderecoPessoa.setNrLongitude(this.convertCoordenadasParaBigDecimal(bean, "nrLongitude"));
    	enderecoPessoa.setNrLatitudeTmp(this.convertCoordenadasParaBigDecimal(bean, MAP_KEY_NR_LATITUDE_TMP));
    	enderecoPessoa.setNrLongitudeTmp(this.convertCoordenadasParaBigDecimal(bean, MAP_KEY_NR_LONGITUDE_TMP));
    	enderecoPessoa.setNrQualidade(MapUtils.getInteger(bean, MAP_KEY_QUALIDADE));
	}
	
	@POST
	@Path("loadTpEndereco")
	public Response loadTpEndereco(String tpPessoa) {
		List<DomainValue> values = domainValueService.findDomainValues("DM_TIPO_ENDERECO", Boolean.TRUE);
		
		for (int i = 0; i < values.size(); i++) {
			DomainValue value = values.get(i);
			if ("F".equals(tpPessoa) && "COM".equals(value.getValue())) {
				values.remove(i);
			} else if("J".equals(tpPessoa) && "RES".equals(value.getValue())) {
				values.remove(i);
			}
		}
		
		List<Map<String, Object>> list = populateListTpEndereco(values);
		
		return Response.ok(list).build();
	}

	private List<Map<String, Object>> populateListTpEndereco(List<DomainValue> values) {
		return new ListToMapConverter<DomainValue>().mapRows(values, new RowMapper<DomainValue>() {
			@Override
			public Map<String, Object> mapRow(DomainValue o) {
				Map<String, Object> toReturn = new HashMap<String, Object>();
				toReturn.put("id", o.getId());
				toReturn.put(MAP_KEY_DESCRIPTION, o.getDescriptionAsString());
				toReturn.put("value", o.getValue());
				return toReturn;
			}
		});
	}

	/**
	 * @param bean
	 * @param enderecoPessoa
	 */
	@SuppressWarnings("unchecked")
	private void setTipoLogradouro(Map<String, Object> bean,
			EnderecoPessoa enderecoPessoa) {
		TipoLogradouro tipoLogradouro = null;
    	
    	if (bean.get(MAP_KEY_TP_LOGRADOURO) != null){
    		Map<String,Object> tpLogradouro = MapUtils.getMap(bean, MAP_KEY_TP_LOGRADOURO);
    		
			tipoLogradouro = new TipoLogradouro();
			tipoLogradouro.setIdTipoLogradouro(MapUtils.getLong(tpLogradouro, "id")); 			
		}
    	
    	enderecoPessoa.setTipoLogradouro(tipoLogradouro);
	}

	/**
	 * @param bean
	 * @param enderecoPessoa
	 */
	private void setMunicipio(Map<String, Object> bean,
			EnderecoPessoa enderecoPessoa) {
		Municipio municipio = new Municipio();
				
		municipio.setIdMunicipio(NumberUtils.createLong(MapUtils.getMap(bean, "municipio").get("idMunicipio").toString()));    	    	
    	enderecoPessoa.setMunicipio(municipio);
	}

	/**
	 * @param bean
	 * @param enderecoPessoa
	 */
	private void setPessoa(Map<String, Object> bean,
			EnderecoPessoa enderecoPessoa) {
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(NumberUtils.createLong(bean.get(MAP_KEY_ID_PESSOA).toString()));
		enderecoPessoa.setPessoa(pessoa);
	}
	
	private BigDecimal convertCoordenadasParaBigDecimal(Map<String, Object> map, String chave) {
		String coordenada = StringUtils.isNoneBlank(MapUtils.getString(map, chave)) ? MapUtils.getString(map, chave).replaceAll(",",".") : null;
		return NumberUtils.createBigDecimal(coordenada);
	}
}