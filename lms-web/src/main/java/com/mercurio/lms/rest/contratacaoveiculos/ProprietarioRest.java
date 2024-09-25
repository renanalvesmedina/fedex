package com.mercurio.lms.rest.contratacaoveiculos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.adsm.rest.PaginationDTO;
import com.mercurio.adsm.rest.ResponseDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contratacaoveiculos.model.AnexoProprietario;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.rest.PaginacaoUtil;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.utils.ExportUtils;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.MensagemService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Rest responsável pelo controle da tela manter proprietário.
 * 
 */
@Path("/contratacaoveiculos/manterProprietario")
public class ProprietarioRest extends BaseRest {

	@InjectInJersey
	private ProprietarioService proprietarioService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;
	
	
	@InjectInJersey
	private MensagemService mensagemService;
	
	@InjectInJersey
	protected UsuarioLMSService usuarioLMSService;
	
	@InjectInJersey
	protected ConteudoParametroFilialService conteudoParametroFilialService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	private static final int MAX_LENGTH_ID_PROPRIETARIO = 14;

	private static final String PARAMETRO_FILIAL = "VALIDA_CONTRATO_MT";
	private static final String SIM = "S";
	
	/**
	 * Retorna um registro para a tela de detalhamento.
	 * 
	 * @param id
	 * @param idProcessoWorkflow
	 * @return Response
	 */
	@GET
	@Path("/findById")
	public Response findById(@QueryParam("id") Long id) {		
		return Response.ok(findProprietarioById(id)).build();
	}
	
	
	/**
	 * Retorna um registro para a tela de detalhamento, a partir da tela manter
	 * ações do workflow, utilizando o id do processo.
	 * 
	 * @param id
	 * @return Response
	 */
	@GET
	@Path("/findByIdProcesso")
	public Response findByIdProcesso(@QueryParam("id") Long id) {				
		return Response.ok(findByIdProcesso(id, true)).build();
    }   
	
	/**
	 * Retorna um registro para a tela de detalhamento, informando que é
	 * workflow.
	 * 
	 * @param id
	 * @param workflow
	 * 
	 * @return protected
	 */
	protected TypedFlatMap findByIdProcesso(Long id, Boolean workflow){				
		TypedFlatMap result = getResult(proprietarioService.findByIdProcesso(id));
		result.put("workflow", workflow);
		return result;
	}
		
	/**
	 * Retorna listagem de registros de acordo com o filtro informado.
	 * 
	 * @param filtro
	 * @return Response
	 */
	@POST
	@Path("/find")
	@SuppressWarnings("unchecked")
	public Response find(FiltroPaginacaoDto filtro) {
		final TypedFlatMap tfm = populateCriteria(filtro);		
		tfm.putAll(PaginacaoUtil.getPaginacao(filtro, Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_GRID", false).getDsConteudo())));
		
		List<Map<String, Object>> list = getReturnMap(proprietarioService.findPaginatedProprietario(tfm).getList());				
		Integer count = proprietarioService.getRowCountCustom(tfm);
		
		return getReturnFind(list, count);
	}
	
	/**
	 * Realiza exportação da pesquisa para um csv.
	 * 
	 * @param filtro
	 * @return Response
	 */
	@POST
	@Path("/reportCsv")
	@SuppressWarnings("unchecked")
	public Response reportCsv(FiltroPaginacaoDto filtro){		
		final TypedFlatMap tfm = populateCriteria(filtro);		
		tfm.putAll(PaginacaoUtil.getPaginacao(filtro, Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_CSV", false).getDsConteudo())));
				
		List<Map<String, Object>> list = getReturnMap(proprietarioService.findPaginatedProprietario(tfm).getList());
		
		ResponseDTO responseDTO = new ResponseDTO();
		
		if (list.isEmpty()) {
			responseDTO.setInfo(getLabel("grid.paginacao.nenhum-registro").replace("<BR>", ""));
			return Response.ok(responseDTO).build();
		}
		
		try {
			responseDTO.setFileName(ExportUtils.exportCsv(reportExecutionManager.getReportOutputDir(), "CSV", list, getColumns()));
		} catch (IOException e) {
			throw new BusinessException("fileReportError");
		}
		
		return Response.ok(responseDTO).build();
	}
		
	/**
	 * Cria um map para retornar a tela.
	 * 
	 * @param list
	 * @return List<Map<String, Object>>
	 */
	private List<Map<String, Object>> getReturnMap(List<Map<String, Object>> list){
		for (Map<String, Object> map : list) {
			DomainValue tpIdentificacao = (DomainValue) map.get("pessoa_tpIdentificacao");
			String nrIdentificacao = (String) map.get("pessoa_nrIdentificacao");
			
			map.put("nrIdentificacao_formatado", FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao));
		}
		
		return list;
	}
	
	/**
	 * Retorna os tipos de pagamentos semanais existentes.
	 * 
	 * @return Response
	 */
	@GET
	@Path("populatePagamentoSemanal")
	public Response populatePagamentoSemanal() {
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		
		List<DomainValue> dias = proprietarioService.findDiasUteisPagamentoSemanal();
		
		for (DomainValue dia : dias) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("value",dia.getValue());
			map.put("dia",dia.getDescriptionAsString());
			map.put("id", dia.getId());
			retorno.add(map);			
		}
		
		return Response.ok(retorno).build(); 
	}
	
	/**
	 * Retorna dados para a suggest de proprietários.
	 * 
	 * @param data
	 * @return Response
	 */
	@POST
	@Path("findProprietarioSuggest")
	public Response findProprietarioSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
			
		Integer limiteRegistros = getLimiteRegistros(value);
				
		String id = PessoaUtils.clearIdentificacao(value);
				
		String nrIdentificacao = null;
		String nmPessoa = null;		
		
		if(StringUtils.isNumeric(id) && id.length() <= MAX_LENGTH_ID_PROPRIETARIO){
			nrIdentificacao = id;
		} else if(!StringUtils.isNumeric(value)) {
			nmPessoa = value;
		}
			
		if(nrIdentificacao == null && nmPessoa == null){
			return Response.ok().build();
		}
		
		return new SuggestResponseBuilder(proprietarioService.findProprietarioSuggest(nrIdentificacao, nmPessoa, limiteRegistros == null ? null : limiteRegistros + 1), limiteRegistros).build();		
	}
	
	/**
	 * LMS-5590
	 * 
	 * Retorna dados para a suggest de proprietários somente com os proprietários que possuem CPF 
	 * 
	 * @param data
	 * @return Response
	 */
	@POST
	@Path("findProprietarioSuggestCpf")
	public Response findProprietarioSuggestCpf(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}
			
		Integer limiteRegistros = getLimiteRegistros(value);
				
		String id = PessoaUtils.clearIdentificacao(value);
				
		String nrIdentificacao = null;
		String nmPessoa = null;		
		
		if(StringUtils.isNumeric(id) && id.length() < MAX_LENGTH_ID_PROPRIETARIO){
			nrIdentificacao = id;
		} else if(!StringUtils.isNumeric(value)) {
			nmPessoa = value;
		}
			
		if(nrIdentificacao == null && nmPessoa == null){
			return Response.ok().build();
		}
		
		return new SuggestResponseBuilder(proprietarioService.findProprietarioSuggestCpf(nrIdentificacao, nmPessoa, limiteRegistros == null ? null : limiteRegistros + 1), limiteRegistros).build();		
	}
		
	/**
	 * Efetua o download de um arquivo de anexo.
	 * 
	 * @param id
	 * 
	 * @return Response
	 * @throws UnsupportedEncodingException
	 */
	@GET
	@Path("/findAnexoById")
	public Response findAnexoProprietarioById(@QueryParam("id") Long id) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("table", "ANEXO_PROPRIETARIO");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_ANEXO_PROPRIETARIO");
		result.put("id", id);
		
		return Response.ok(result).build();
	}
	
	/**
	 * Retorna os anexos vinculados ao proprietário.
	 * 
	 * @param filtro
	 * 
	 * @return Response
	 */
	@POST
	@Path("/findAnexos")
	public Response findAnexos(Map<String, Object> filtro) {		
		Long idProprietario = MapUtils.getLong(MapUtils.getMap(filtro, "filtros"), "idProprietario");
		
		if(idProprietario == null){
			return Response.ok().build();	
		}
		
		List<Map<String, Object>> listAnexoProprietario = proprietarioService.findAnexoProprietarioByIdProprietario(idProprietario);
		
		PaginationDTO pagination = new PaginationDTO();
		pagination.setList(listAnexoProprietario);
		pagination.setQtRegistros(listAnexoProprietario.size());
		
		return Response.ok(pagination).build();		
	}
		
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeAnexoProprietarioByIds")
	public void removeAnexoProprietarioByIds(List<Long> ids) {
		proprietarioService.removeByIdsAnexoProprietario(ids);
	}
	
	/**
	 * Prepara os anexos do proprietário para serem persistidos.
	 * 
	 * @param formDataMultiPart
	 * @param files
	 * 
	 * @return List<AnexoProprietario> 
	 * 
	 * @throws IOException
	 */
	private List<AnexoProprietario> getListAnexoProprietario(
			FormDataMultiPart formDataMultiPart, List<Map<String, Object>> files)
			throws IOException {
		List<AnexoProprietario> listAnexoProprietario = new ArrayList<AnexoProprietario>();
		
		if(files == null || files.isEmpty()){
			return listAnexoProprietario;
		}
				
		for (int i = 0; i < files.size(); i++) {
			AnexoProprietario anexoProprietario = getAnexoProprietario(
					(Map<String, Object>) files.get(i),
					getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i));
			
			listAnexoProprietario.add(anexoProprietario);
		}
		
		return listAnexoProprietario;
	}
	
	/**
	 * Popula uma entidade AnexoProprietario.
	 * 
	 * @param dados
	 * @param data
	 * 
	 * @return AnexoProprietario
	 * 
	 * @throws IOException
	 */
	private AnexoProprietario getAnexoProprietario(Map<String, Object> dados, byte[] data) {						
		AnexoProprietario anexoProprietario = new AnexoProprietario();
		anexoProprietario.setUsuario(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
		anexoProprietario.setDsAnexo(MapUtils.getString(dados, "dsAnexo"));
		anexoProprietario.setDhCriacao(RestPopulateUtils.getYearMonthDayFromISO8601(dados, "dhCriacao").toDateTimeAtCurrentTime());
		anexoProprietario.setDcArquivo(data);
    	
    	return anexoProprietario;
	}
	
	/**
	 * Grava um registro.
	 * 
	 * @param formDataMultiPart
	 * @return Response
	 * 
	 * @throws IOException 
	 */	
	@POST
	@Path("store")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@SuppressWarnings("unchecked")
	public Response store(FormDataMultiPart formDataMultiPart) throws IOException {
		Map<String, Object> bean = getModelFromForm(formDataMultiPart, Map.class, "dados");
		List<Map<String, Object>> files = (List<Map<String, Object>>) bean.get("files");
		
		Long id = MapUtils.getLong(bean, "idProprietario");
				
		Proprietario proprietario = null;		
				
		if(id == null){
			proprietario = new Proprietario();
		} else {			
			proprietario = (Proprietario) getProprietario(id).get("proprietario");
			proprietario.setUsuario(SessionUtils.getUsuarioLogado());			
		}	
		
		return doStore(bean, proprietario, getListAnexoProprietario(formDataMultiPart, files));
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
	private Response doStore(Map<String, Object> bean,
			Proprietario proprietario,
			List<AnexoProprietario> listAnexoProprietario) {
		populateProprietario(bean, proprietario);
    			    	
		proprietarioService.storeProprietario(proprietario, listAnexoProprietario);
		
		return Response.ok(findProprietarioById(proprietario.getIdProprietario())).build();
	}

	/**
	 * Popula dados de tela para entidade.
	 * 
	 * @param bean
	 * @param proprietario
	 */
	private void populateProprietario(Map<String, Object> bean, Proprietario proprietario){
		setDadosProprietario(bean, proprietario);
		setDadosESocial(bean, proprietario);
		setFilial(bean, proprietario);
		setEnderecoTelefone(bean, proprietario);
		setDadosWorkflow(bean, proprietario);
	}

	private void setEnderecoTelefone(Map<String, Object> bean, Proprietario proprietario) {
		TelefoneEndereco telefoneEndereco = new TelefoneEndereco();	
		telefoneEndereco.setNrDdd(MapUtils.getString(bean, "nrDdd"));		
		telefoneEndereco.setNrTelefone(MapUtils.getString(bean, "nrTelefone"));
		telefoneEndereco.setTpTelefone(RestPopulateUtils.getDomainValue(bean.get("tpTelefone")));
		telefoneEndereco.setTpUso(RestPopulateUtils.getDomainValue(bean.get("tpUso")));		
		telefoneEndereco.setNrDdi(MapUtils.getString(bean, "nrDdi"));
		telefoneEndereco.setIdTelefoneEndereco(MapUtils.getLong(bean, "idTelefoneEndereco"));	
		
		proprietario.setTelefoneEndereco(telefoneEndereco);		
	}
	
	private void setDadosProprietario(Map<String, Object> bean, Proprietario proprietario) {
		proprietario.setNrAntt(MapUtils.getLong(bean, "nrAntt"));		
		proprietario.setTpSituacao(RestPopulateUtils.getDomainValue(bean.get("tpSituacao")));
		proprietario.setTpProprietario(RestPopulateUtils.getDomainValue(bean.get("tpProprietario")));
		proprietario.setTpPeriodoPagto(RestPopulateUtils.getDomainValue(bean.get("tpPeriodoPagto")));
		proprietario.setDiaSemana(RestPopulateUtils.getDomainValue(bean.get("diaSemana")));
		proprietario.setBlCooperado(RestPopulateUtils.getDomainValue(bean.get("blCooperado")));
		proprietario.setBlMei(RestPopulateUtils.getDomainValue(bean.get("blMei")));
		proprietario.setBlNaoAtualizaDbi(RestPopulateUtils.getDomainValue(bean.get("blNaoAtualizaDbi")));
		proprietario.setBlRotaFixa(RestPopulateUtils.getDomainValue(bean.get("blRotaFixa")));
		proprietario.setTpOperacao(RestPopulateUtils.getDomainValue(bean.get("tpOperacao")));
		proprietario.setDtVigenciaInicial(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtVigenciaInicial"));	
		proprietario.setDtVigenciaFinal(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtVigenciaFinal"));	
	}
	
	@SuppressWarnings("unchecked")
	private void setDadosESocial(Map<String, Object> bean, Proprietario proprietario) {	
		Map<String, Object> esocial = MapUtils.getMap(bean, "esocial");
		
		proprietario.setNmMei(MapUtils.getString(esocial, "nmMei"));
		proprietario.setNrIdentificacaoMei(MapUtils.getString(esocial, "nrIdentificacaoMei"));
		proprietario.setNrPis(MapUtils.getLong(esocial, "nrPis"));
		proprietario.setNrDependentes(MapUtils.getByte(esocial, "nrDependentes"));
		proprietario.setDtNascimento(RestPopulateUtils.getYearMonthDayFromISO8601CorrecaoHorarioVerao(esocial, "dtNascimento"));
				
		setDadosPessoa(bean, esocial, proprietario);
	}

	/**
	 * @param bean
	 * @param proprietario
	 */
	private void setDadosPessoa(Map<String, Object> bean, Map<String, Object> esocial, Proprietario proprietario) {
		Pessoa pessoa = new Pessoa();
				
		pessoa.setIdPessoa(MapUtils.getLong(bean, "idPessoa"));		
		pessoa.setNmPessoa(MapUtils.getString(bean, "nmPessoa"));
		pessoa.setNrIdentificacao(MapUtils.getString(bean,"nrIdentificacao"));
		pessoa.setDsEmail(MapUtils.getString(bean,"dsEmail"));		
		pessoa.setTpPessoa(RestPopulateUtils.getDomainValue(bean.get("tpPessoa")));
		pessoa.setTpIdentificacao(RestPopulateUtils.getDomainValue(bean.get("tpIdentificacao")));
		pessoa.setNrInscricaoMunicipal(MapUtils.getString(esocial, "nrInscricaoMunicipal"));
		pessoa.setDsOrgaoEmissorRg(MapUtils.getString(esocial, "dsOrgaoEmissorRg"));		
		pessoa.setNrRg(MapUtils.getString(esocial, "nrRg"));
		pessoa.setDtEmissaoRg(RestPopulateUtils.getYearMonthDayFromISO8601(esocial, "dtEmissaoRg"));	
		
		proprietario.setPessoa(pessoa);
		
		setUnidadeFederativaExpedicaoRg(esocial, proprietario.getPessoa());
	}
	
	/**
	 * @param bean
	 * @param pessoa
	 */
	@SuppressWarnings("unchecked")
	private void setUnidadeFederativaExpedicaoRg(Map<String, Object> bean,
			Pessoa pessoa) {	
		Map<String, Object> ufExpedicaoRg = MapUtils.getMap(bean, "ufExpedicaoRg");
		
		if(ufExpedicaoRg == null){
			return;
		}
		
		UnidadeFederativa unidadeFederativa = new UnidadeFederativa();				
		unidadeFederativa.setIdUnidadeFederativa(MapUtils.getLong(ufExpedicaoRg, "idUnidadeFederativa"));
		
		pessoa.setUnidadeFederativaExpedicaoRg(unidadeFederativa);
	}
	
	/**
	 * @param bean
	 * @param proprietario
	 */
	private void setFilial(Map<String, Object> bean, Proprietario proprietario) {				
		Filial filial = new Filial();
		filial.setIdFilial(MapUtils.getLong(MapUtils.getMap(bean, "filial"), "idFilial"));
		
		proprietario.setFilial(filial);
	}
	

	/**
	 *  
	 * @param ma
	 * @param proprietariop
	 */
	private void setDadosWorkflow(Map<String, Object> map, Proprietario proprietario){
		if(proprietario.getPendencia() == null){
			return;
		}
		
		map.put("dsPendencia", proprietario.getPendencia().getDsPendencia());
	}
	
	/**
	 * Remove um ou mais itens da tabela de proprietário.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeProprietariosByIds")
	public Response removeProprietariosByIds(List<Long> ids) {
		proprietarioService.removeByIds(ids);
		
		return Response.ok().build();
	}
	
	/**
	 * Valida o tipo da identificação inserida para o proprietário.
	 * 
	 * @param ids
	 */
	@POST
	@Path("validateIdentificacao")
	public Response validateIdentificacao(Map<String, Object> bean) {		
		return Response.ok(proprietarioService.validateTpIdentificacao(new TypedFlatMap(bean))).build();
	}
	
	/**
	 * Valida o tipo da identificação inserida para CPF do MEI.
	 * 
	 * @param ids
	 */
	@POST
	@Path("validateIdentificacaoCpfMei")
	public void validateIdentificacaoCpfMei(Map<String, Object> bean) {
		proprietarioService.validateCpfCnpj(new TypedFlatMap(bean));
	}
	
	/**
	 * Valida o PIS inserido para o proprietário.
	 * 
	 * @param ids
	 */
	@POST
	@Path("validatePis")
	public void validatePis(Map<String, Object> bean) {		
		proprietarioService.validatePis(new TypedFlatMap(bean));
	}
	
	/**
	 * Remove item da tabela de proprietário.
	 * 
	 * @param ids
	 */
	@GET
	@Path("removeProprietarioById")
	public Response removeProprietarioById(@QueryParam("id") Long id) {
		proprietarioService.removeById(id);
		
		return Response.ok().build();
	}
	
	/**
	 * Popula filtro de pesquisa para a listagem de proprietários. 
	 * 
	 * @param filtro
	 * @return TypedFlatMap
	 */
	private TypedFlatMap populateCriteria(FiltroPaginacaoDto filtro) {
		TypedFlatMap criteria = new TypedFlatMap(filtro.getFiltros());

		criteria.put("tpProprietario", criteria.getString("tpProprietario.value"));
		criteria.put("filial.idFilial", criteria.getLong("filial.idFilial"));
		criteria.put("pessoa.tpPessoa", criteria.getString("tpPessoa.value"));
		criteria.put("pessoa.tpIdentificacao", criteria.getString("tpIdentificacao.value"));
		criteria.put("pessoa.nrIdentificacao", criteria.getString("nrIdentificacao"));
		criteria.put("pessoa.nmPessoa", criteria.getString("nmPessoa"));
		criteria.put("tpPeriodoPagto", criteria.getString("tpPeriodoPagto.value"));
		criteria.put("diaSemana", criteria.getString("diaSemana"));
		criteria.put("tpSituacao", criteria.getString("tpSituacao.value"));
		criteria.put("dtAtualizacaoInicial", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "dtAtualizacaoIni"));
		criteria.put("dtAtualizacaoFinal", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "dtAtualizacaoFim"));
		criteria.put("beneficiario.idBeneficiario", criteria.getLong("idBeneficiario"));
		criteria.put("blCooperado", criteria.getString("blCooperado.value"));
		criteria.put("blMei", criteria.getString("blMei.value"));

		return criteria;
	}	
	
	/**
	 * Retorna um registro para a tela de detalhamento, informando que não é
	 * workflow.
	 * 
	 * @param id
	 * @param workflow
	 * 
	 * @return protected
	 */
	private TypedFlatMap findProprietarioById(Long id){						
		return getResult(getProprietario(id));
	}
	
	/**
	 * Retorna um registro.
	 * 
	 * @param id
	 * @return Proprietario
	 */
	private Map<String,Object> getProprietario(Long id) {
		return proprietarioService.findProprietarioById(id);
	}
	
	/**
	 * Retorna um mapa de acordo com a entidade proprietario.
	 * 
	 * @param proprietario
	 * @return TypedFlatMap
	 */
	private TypedFlatMap getResult(Map<String,Object> proprietario) {				
		return createResultMap(proprietario);
	}
	
	/**
	 * Popula mapa com os atributos que serão necessários para visualizar a
	 * tela.
	 * 
	 * @param proprietario
	 * 
	 * @return TypedFlatMap
	 */
	private TypedFlatMap createResultMap(Map<String,Object> result) {
		Proprietario proprietario = (Proprietario) result.get("proprietario");
		
		TypedFlatMap map = new TypedFlatMap();
		
		getDadosProprietario(proprietario, map);
		getDadosUsuario(proprietario, map);
		getDadosESocial(proprietario, map);		
		getFilial(proprietario, map);
		getTelefoneEndereco(proprietario, map);
		getDadosWorkflow(proprietario, map);
		getBloqueio(result, map);
		desabilitaCampos(proprietario, map);
		
		return map;
	}

	private void desabilitaCampos(Proprietario proprietario, TypedFlatMap map) {
		map.put("geraWorkflow", isDesabilitaTpSituacao(proprietario));
		map.put("bloqueiaAcoes", isBloqueiaAcoes(proprietario));		
		map.put("btInscricaoEstadual", !"J".equals(proprietario.getPessoa().getTpPessoa().getValue()));
		
		
	}
	
	private boolean isBloqueiaAcoes(Proprietario proprietario) {
		
		boolean retorno = false;
		
		Pendencia pendencia = proprietario.getPendencia();
		
		if(pendencia != null){		
			boolean emAprovacao = ConstantesWorkflow.EM_APROVACAO.equals(pendencia.getTpSituacaoPendencia().getValue());			
			
			Short nrTipoEvento = pendencia.getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento();
			
			if(isCadastroNovo(nrTipoEvento) && emAprovacao){
				retorno = true;
			}
		}		
		return retorno;
		
	}


	private boolean isCadastroNovo(Short nrTipoEvento) {
		return ConstantesWorkflow.NR2609_APROVACAO_CADASTRO_PROPRIETARIO_CE.equals(nrTipoEvento) || ConstantesWorkflow.NR2613_APROVACAO_CADASTRO_PROPRIETARIO_VI.equals(nrTipoEvento);
	}
	
	


	protected boolean isDesabilitaTpSituacao(Proprietario proprietario) {
		if(proprietario.getPessoa() == null){
			return true;
		}
		
		if(SessionUtils.isFilialSessaoMatriz()){
			return false;
		}
		
		if("P".equals(proprietario.getTpProprietario().getValue())){
			return false;
		}
		
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			return true;
		}
		 
		return false;
	}


	/**
	 * @param result
	 * @param map
	 */
	private void getBloqueio(Map<String, Object> result, TypedFlatMap map) {
		Boolean bloquear = (Boolean) result.get("bloquear");
		
		map.put("tpSituacaoBloqueio", bloquear);
		map.put("tpSituacaoBloqueioDesc", bloquear ? mensagemService.getMessage("bloqueado") : mensagemService.getMessage("liberado"));
		map.put("btSituacaoBloqueioLabel", bloquear ? mensagemService.getMessage("desbloquear") : mensagemService.getMessage("bloquear"));
	}

	/**
	 * @param proprietario
	 * @param map
	 */
	private void getTelefoneEndereco(Proprietario proprietario, TypedFlatMap map) {
		if(proprietario.getTelefoneEndereco() == null){
			return;
		}
	
		TelefoneEndereco telefoneEndereco = proprietario.getTelefoneEndereco();
		
		map.put("idTelefoneEndereco", telefoneEndereco.getIdTelefoneEndereco());
		map.put("tpTelefone", telefoneEndereco.getTpTelefone());
		map.put("tpUso", telefoneEndereco.getTpUso());
		map.put("nrDdi", telefoneEndereco.getNrDdi());
		map.put("nrDdd", telefoneEndereco.getNrDdd());
		map.put("nrTelefone", telefoneEndereco.getNrTelefone());
	}

	/**
	 * @param proprietario
	 * @param map
	 */
	private void getDadosProprietario(Proprietario proprietario,
			TypedFlatMap map) {
		map.put("idProprietario", proprietario.getIdProprietario());
		map.put("nrAntt", proprietario.getNrAntt());
		map.put("tpSituacao", proprietario.getTpSituacao());		
		map.put("tpProprietario", proprietario.getTpProprietario());
		map.put("tpPeriodoPagto", proprietario.getTpPeriodoPagto());
		map.put("diaSemana", proprietario.getDiaSemana());
		map.put("blCooperado", proprietario.getBlCooperado());
		map.put("blMei", proprietario.getBlMei());
		map.put("blNaoAtualizaDbi", proprietario.getBlNaoAtualizaDbi());
		map.put("blRotaFixa", proprietario.getBlRotaFixa());
		map.put("tpOperacao", proprietario.getTpOperacao());
		map.put("dtVigenciaInicial", proprietario.getDtVigenciaInicial());
		map.put("dtVigenciaFinal", proprietario.getDtVigenciaFinal());
		map.put("dtAtualizacao", proprietario.getDtAtualizacao());
		map.put("isMatriz", SessionUtils.isFilialSessaoMatriz());
	}

	/**
	 * @param proprietario
	 * @param map
	 */
	private void getDadosESocial(Proprietario proprietario,
			TypedFlatMap map) {		
		Map<String, Object> esocial = new HashMap<String, Object>();
		esocial.put("nmMei", proprietario.getNmMei());
		esocial.put("nrIdentificacaoMei", proprietario.getNrIdentificacaoMei());
		esocial.put("tpOperacao", proprietario.getTpOperacao());
		esocial.put("idProprietario", proprietario.getIdProprietario());
		esocial.put("nrPis", proprietario.getNrPis());
		esocial.put("nrDependentes", proprietario.getNrDependentes());
		esocial.put("dtNascimento", proprietario.getDtNascimento());

		getDadosPessoa(proprietario, map, esocial);

		map.put("esocial", esocial);
	}
	
	/**
	 * @param proprietario
	 * @param map
	 */
	private void getDadosPessoa(Proprietario proprietario, TypedFlatMap map, Map<String, Object> esocial) {
		map.put("idPessoa", proprietario.getPessoa().getIdPessoa());
		map.put("nmPessoa", proprietario.getPessoa().getNmPessoa());
		map.put("dsEmail", proprietario.getPessoa().getDsEmail());
		map.put("tpPessoa", proprietario.getPessoa().getTpPessoa());
		map.put("nrIdentificacao", proprietario.getPessoa().getNrIdentificacao());		
		map.put("tpIdentificacao", proprietario.getPessoa().getTpIdentificacao());		
		map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(
				proprietario.getPessoa().getTpIdentificacao(),
				proprietario.getPessoa().getNrIdentificacao()));
		
		esocial.put("dsOrgaoEmissorRg", proprietario.getPessoa().getDsOrgaoEmissorRg());
		esocial.put("nrRg", proprietario.getPessoa().getNrRg());
		esocial.put("dtEmissaoRg", proprietario.getPessoa().getDtEmissaoRg());
		esocial.put("nrInscricaoMunicipal", proprietario.getPessoa().getNrInscricaoMunicipal());
				
		getUnidadeFederativaExpedicaoRg(esocial, proprietario.getPessoa().getUnidadeFederativaExpedicaoRg());
	}
	
	/**
	 * @param result
	 * @param unidadeFederativa
	 */
	private void getUnidadeFederativaExpedicaoRg(Map<String, Object> result,
			UnidadeFederativa unidadeFederativa) {
		if(unidadeFederativa == null){
			return;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idUnidadeFederativa", unidadeFederativa.getIdUnidadeFederativa());
		map.put("sgUnidadeFederativa", unidadeFederativa.getSgUnidadeFederativa());
		map.put("nmUnidadeFederativa", unidadeFederativa.getNmUnidadeFederativa());
		
		result.put("ufExpedicaoRg", map);		
	}
	
	/**
	 * @param proprietario
	 * @param map
	 */
	private void getFilial(Proprietario proprietario, TypedFlatMap map) {
		Filial filial = proprietario.getFilial();
		
		if(filial == null){
			return;
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("idFilial", filial.getIdFilial());
		result.put("sgFilial", filial.getSgFilial());
		result.put("nmFilial", filial.getPessoa().getNmFantasia());
		
		map.put("filial", result);
	}
	
	/**
	 * 
	 * @param proprietario
	 * @param map
	 */
	private void getDadosUsuario(Proprietario proprietario, TypedFlatMap map){
		Usuario usuario = proprietario.getUsuario();
		
		if(usuario == null){
			return;
		}
		
		Map<String, Object> usuarioAlteracao = new HashMap<String, Object>();		
		usuarioAlteracao.put("idUsuario", usuario.getIdUsuario());
		usuarioAlteracao.put("nmUsuario", usuario.getNmUsuario());
		usuarioAlteracao.put("nrMatricula", usuario.getNrMatricula());
		
		map.put("usuarioAlteracao", usuarioAlteracao);
	}
	
	/**
	 * 
	 * @param proprietario
	 * @param map
	 */
	private void getDadosWorkflow(Proprietario proprietario, TypedFlatMap map){
		Pendencia pendencia = proprietario.getPendencia();
		
		if(pendencia == null){
			return;
		}
		
		map.put("idPendencia", pendencia.getIdPendencia());
		map.put("dsPendencia", pendencia.getTpSituacaoPendencia().getDescriptionAsString());
	}
	
	/**
	 * Retorna o número de resultados máximos para a suggest listar.
	 * 
	 * @param value
	 * @return Integer
	 */
	private Integer getLimiteRegistros(String value) {
		Integer limiteRegistros = null;
		Integer minimoCaracteresSuggest = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_MINIMO_CARACTERES_SUGGEST", false).getDsConteudo());
		
		if (value.length() <= minimoCaracteresSuggest) {
			limiteRegistros = Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS", false).getDsConteudo());
		}
		
		return limiteRegistros;
	}
	
	private List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("sigla","filial_sgFilial"));
		list.add(getColumn("filial","filial_pessoa_nmFantasia"));
		list.add(getColumn("tipo","pessoa_tpIdentificacao"));																		
		list.add(getColumn("identificacao","nrIdentificacao_formatado"));
		list.add(getColumn("nome","pessoa_nmPessoa"));			
		list.add(getColumn("situacao","tpSituacao"));			
		list.add(getColumn("periodo","tpPeriodoPagto"));
		list.add(getColumn("diaPagamentoSemanal","diaSemanal"));
		list.add(getColumn("sgMei","blMei"));
		list.add(getColumn("cooperado","blCooperado"));
		return list;
	}
	
	protected Map<String,String> getColumn(String label, String column) {
		Map<String,String> retorno = new HashMap<String, String>();
		retorno.put("title", getLabel(label));
		retorno.put("column", column);
		return retorno;
	}

	protected String getLabel(String chave) {
		return configuracoesFacade.getMensagem(chave);
	}

}