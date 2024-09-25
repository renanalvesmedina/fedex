package com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo;

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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.adsm.rest.PaginationDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.CartaoPedagio;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;
import com.mercurio.lms.carregamento.model.service.AdiantamentoTrechoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.contratacaoveiculos.model.Beneficiario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.report.EmitirReciboService;
import com.mercurio.lms.fretecarreteiroviagem.model.AnexoReciboFc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.AnexoReciboFcService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.rest.PaginacaoUtil;
import com.mercurio.lms.rest.RestPopulateUtils;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboHelperConstants;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboRestConstants;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.helper.ReciboColetaEntregaHelper;
import com.mercurio.lms.rest.utils.Closure;
import com.mercurio.lms.rest.utils.ListResponseBuilder;
import com.mercurio.lms.rest.utils.SuggestResponseBuilder;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Rest responsável pelo controle da tela manter recibo coleta entrega.
 * 
 */
@Path("/fretecarreteirocoletaentrega/manterRecibo")
public class ReciboColetaEntregaRest extends BaseRest {
	
	@InjectInJersey
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
		
	@InjectInJersey
	private AnexoReciboFcService anexoReciboFcService;

	@InjectInJersey
	private AdiantamentoTrechoService adiantamentoTrechoService;

	@InjectInJersey
	private UsuarioLMSService usuarioLMSService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	private EmitirReciboService emitirReciboService;
	
	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;
		
	private static final int SIZE_SIGLA = 3;
	
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
		
		addCSVHeader(filtro);
		
		final TypedFlatMap tfm = populateCriteria(filtro);		
		tfm.putAll(PaginacaoUtil.getPaginacao(filtro, limiteRegistros));
		
		return new ListResponseBuilder(filtro, limiteRegistros, reportExecutionManager.getReportOutputDir(), ReciboRestConstants.NOME_REPORT.getValue(), filtro.getColumns())
			.findClosure(new Closure<List<Map<String,Object>>>() {
			
				@SuppressWarnings("unchecked")
				@Override
				public List<Map<String, Object>> execute() {
					return reciboFreteCarreteiroService.findPaginatedReciboColetaEntrega(tfm).getList();
				}
				
			})
			.rowCountClosure(new Closure<Integer>() {
						
				@Override
				public Integer execute() {
					return reciboFreteCarreteiroService.getRowCountColetaEntrega(tfm);
				}
				
			})
			.build();
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
		
		Long id = MapUtils.getLong(bean, ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue());
		
		/*
		 * Não é permitido incluir um novo registro nesta tela, logo apenas
		 * poderá salvar se houver um id carregado.
		 */
		if(id == null){
			return Response.ok().build();
		}
							
		return doStore(bean, getReciboFreteCarreteiro(id),  getListAnexoReciboFc(formDataMultiPart, files));    	
    }

	/**
	 * Executa o store das alterações.
	 *  
	 * @param bean
	 * @param reciboFreteCarreteiro
	 * @param listAnexoReciboFc
	 * 
	 * @return Response
	 * @throws IOException
	 */
	private Response doStore(Map<String, Object> bean,
			ReciboFreteCarreteiro reciboFreteCarreteiro,
			List<AnexoReciboFc> listAnexoReciboFc) throws IOException {
		populaRecibo(reciboFreteCarreteiro, bean);
    	
		reciboFreteCarreteiro = reciboFreteCarreteiroService.storeReciboColetaEntregaWorkflow(reciboFreteCarreteiro, listAnexoReciboFc);
		    	
    	return Response.ok(
    			findById(reciboFreteCarreteiro.getIdReciboFreteCarreteiro(), 
    			MapUtils.getBoolean(bean, ReciboHelperConstants.WORKFLOW.getValue()))).build();
	}

	/**
	 * Cancela o recibo caso não hajam recibos complementares vinculados.
	 * 
	 * @param id
	 * 
	 * @return  Response
	 */
	@GET
	@Path("cancelarRecibo")
	@SuppressWarnings("rawtypes")
    public Response cancelarRecibo(@QueryParam("id") Long id) {
		List listaComplementares = reciboFreteCarreteiroService.findRecibosComplementares(id);
    	
    	if (CollectionUtils.isNotEmpty(listaComplementares)){
    		TypedFlatMap result = new TypedFlatMap();
    		result.put("cancel", Boolean.valueOf(true));
    		
    		return Response.ok(result).build();
    	}
    	    	
    	reciboFreteCarreteiroService.storeCancelarRecibo(id);
    	
    	return findById(id);
    }
	
	/**
	 * Cancela todos os recibos complementares associados com o recibo atual.
	 * 
	 * @param id
	 * 
	 * @return Response
	 */
	@GET
	@Path("cancelarReciboComplementar")
    public Response cancelarReciboComplementar(@QueryParam("id") Long id) {
		TypedFlatMap values = new TypedFlatMap();
		values.put(ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue(), id);
		
		reciboFreteCarreteiroService.storeCancelarReciboAfterValidation(values);
		
		return findById(id);
	}
		
	private void defineDisabled(TypedFlatMap result, String tpSituacaoRecibo, Boolean workflow, Boolean matriz) {
		Map<String, Boolean> situacoes = ReciboColetaEntregaHelper.getSituacoes(tpSituacaoRecibo, workflow, matriz);

		result.put("btCancelar", !ReciboColetaEntregaHelper.isHabilitaBotaoCancelar(situacoes));
		result.put("btSalvar", !ReciboColetaEntregaHelper.isHabilitaBotaoSalvar(situacoes));
		result.put("btEmitir", !ReciboColetaEntregaHelper.isHabilitaEmitir(situacoes));		
		result.put("btComplementares", ReciboColetaEntregaHelper.isDesabilitaBotaoComplementar(situacoes));
		result.put("btOcorrencias", ReciboColetaEntregaHelper.isDesabilitaOcorrencias(situacoes));
		result.put("txtNrNfCarreteiro", !ReciboColetaEntregaHelper.isHabilitaNFCarreteiro(situacoes));
		result.put("txtObservacao", ReciboColetaEntregaHelper.isDesabilitaObservacao(situacoes));
		result.put("dtProgramadaPagtoC", !ReciboColetaEntregaHelper.isHabilitaDataProgramada(situacoes));
		result.put("btAnexar", ReciboColetaEntregaHelper.isDesabilitaAnexos(situacoes));
		result.put("btRim", ReciboColetaEntregaHelper.isDesabilitaRim(situacoes));
		result.put("btNotasCreditos", ReciboColetaEntregaHelper.isDesabilitaNotasCredito(situacoes));
		result.put("btSituacao", !ReciboColetaEntregaHelper.isHabilitaComboSituacao(situacoes));
	}
	
	private void addCSVHeader(FiltroPaginacaoDto filtro) {
		List<Map<String, String>> cabecalho = new ArrayList<Map<String,String>>();
		cabecalho.add(getColumn("numeroRecibo" ,  "nrReciboFreteCarreteiro"));
		cabecalho.add(getColumn("situacao" ,  "tpSituacaoRecibo"));
		cabecalho.add(getColumn("tipoIdentificacao" ,  "proprietario_tpIdentificacao"));
		cabecalho.add(getColumn("identificacao" ,  "proprietario_nrIdentificacao"));
		cabecalho.add(getColumn("proprietario" ,  "proprietario_nmPessoa"));
		cabecalho.add(getColumn("frota" ,  "meioTransporte_nrFrota"));
		cabecalho.add(getColumn("placa" ,  "meioTransporte_nrIdentificador"));
		cabecalho.add(getColumn("marca" ,  "meioTransporte_dsMarca"));
		cabecalho.add(getColumn("modelo" ,  "meioTransporte_dsModelo"));
		cabecalho.add(getColumn("dataEmissao" ,  "dhEmissao"));
		cabecalho.add(getColumn("dataPagamentoReal" ,  "dtPgtoReal"));
		cabecalho.add(getColumn("moeda" ,  "sgMoeda"));
		cabecalho.add(getColumn("simbolo" ,  "dsSimbolo"));
		cabecalho.add(getColumn("valorLiquido" ,  "vlLiquido"));
		filtro.setColumns(cabecalho);
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
	 * Retorna um registro para a tela de detalhamento.
	 * 
	 * @param id
	 * @param idProcessoWorkflow
	 * @return Response
	 */
	@GET
	@Path("/findById")
	public Response findById(@QueryParam("id") Long id) {		
		return Response.ok(findById(id, false)).build();
	}
		
	/**
	 * Retorna um registro para a tela de detalhamento, informando que é
	 * workflow.
	 * 
	 * @param id
	 * @param workflow
	 * 
	 * @return private
	 */
	private TypedFlatMap findByIdProcesso(Long id, Boolean workflow){				
		return getResult(reciboFreteCarreteiroService.findByIdCustom(id), workflow);
		}
		
	/**
	 * Retorna um registro para a tela de detalhamento, informando que não é
	 * workflow.
	 * 
	 * @param id
	 * @param workflow
	 * 
	 * @return private
	 */
	private TypedFlatMap findById(Long id, Boolean workflow){						
		return getResult(getReciboFreteCarreteiro(id), workflow);
	}
		
	/**
	 * Retorna um mapa de acordo com a entidade recibo frete carreteiro, como
	 * também já define os campos de poderão ser habilitados na tela.
	 * 
	 * @param rfc
	 * @return TypedFlatMap
	 */
	private TypedFlatMap getResult(ReciboFreteCarreteiro rfc, Boolean workflow) {
		TypedFlatMap result = createResultMap(rfc);
		
		result.put(ReciboHelperConstants.WORKFLOW.getValue(), workflow);
		
		defineDisabled(result, 
				result.getString(ReciboRestConstants.TP_SITUACAO_RECIBO_VALUE.getValue()), 
				workflow,
				SessionUtils.isFilialSessaoMatriz());
		
		return result;
	}
	
	/**
	 * Emite um relatório de um recibo de coleta e entrega.
	 * 
	 * @param id
	 * @return Response
	 * @throws Exception
	 */
	@GET
	@Path("emitirRecibo")
	public Response emitir(@QueryParam("id") Long id) throws Exception {		
		TypedFlatMap reportCriteria = new TypedFlatMap();
		boolean blReemissao = reciboFreteCarreteiroService.storeValidateEmissaoReciboColetaEntrega(id);
		reportCriteria.put(id, new Object[]{Boolean.valueOf(blReemissao)});
		
		Map<String, String> retorno = new HashMap<String, String>();
		String fileName = reportExecutionManager.generateReportLocator(emitirReciboService, reportCriteria);
		retorno.put("fileName", fileName);
		
		return Response.ok(retorno).build();
	}
	
	/**
	 * Prepara os anexos do recibo para serem persistidos.
	 * 
	 * @param formDataMultiPart
	 * @param files
	 * 
	 * @return List<AnexoReciboFc> 
	 * 
	 * @throws IOException
	 */
	private List<AnexoReciboFc> getListAnexoReciboFc(
			FormDataMultiPart formDataMultiPart, List<Map<String, Object>> files)
			throws IOException {
		List<AnexoReciboFc> listAnexoReciboFc = new ArrayList<AnexoReciboFc>();
		
		if(files == null || files.isEmpty()){
			return listAnexoReciboFc;
		}
				
		for (int i = 0; i < files.size(); i++) {
			AnexoReciboFc anexoReciboFc = getAnexoReciboFc(
					(Map<String, Object>) files.get(i),
					getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo_" + i));
			
			listAnexoReciboFc.add(anexoReciboFc);
		}
		
		return listAnexoReciboFc;
	}
	
	/**
	 * Popula uma entidade AnexoReciboFc.
	 * 
	 * @param dados
	 * @param data
	 * 
	 * @return AnexoReciboFc
	 * 
	 * @throws IOException
	 */
	private AnexoReciboFc getAnexoReciboFc(Map<String, Object> dados, byte[] data) {						
		AnexoReciboFc anexoReciboFc = new AnexoReciboFc();
		anexoReciboFc.setUsuarioLMS(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
		anexoReciboFc.setDescAnexo(MapUtils.getString(dados, "descAnexo"));
		anexoReciboFc.setDhCriacao(RestPopulateUtils.getYearMonthDayFromISO8601(dados, "dhCriacao").toDateTimeAtCurrentTime());
		anexoReciboFc.setDcArquivo(data);
    	
    	return anexoReciboFc;
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
	public Response findAnexoById(@QueryParam("id") Long id) {
		Map<String, Object> result = new HashMap<>();
		result.put("table", "ANEXO_RECIBO_FC");
		result.put("blobColumn", "DC_ARQUIVO");
		result.put("idColumn", "ID_ANEXO_RECIBO_FC");
		result.put("id", id);

		return Response.ok(result).build();
	}
	
	/**
	 * Retorna os anexos vinculados ao recibo.
	 * 
	 * @param filtro
	 * 
	 * @return Response
	 */
	@POST
	@Path("/findAnexos")
	@SuppressWarnings("rawtypes")
	public Response findAnexos(Map<String, Object> filtro) {
		Long idReciboFreteCarreteiro = MapUtils.getLong(MapUtils.getMap(filtro, "filtros"), ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue());
		
		if(idReciboFreteCarreteiro == null){
			return Response.ok().build();	
		}
		
		List result = anexoReciboFcService.findItensByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
		
		PaginationDTO pagination = new PaginationDTO();
		pagination.setList(result);
		pagination.setQtRegistros(result.size());
		
		return Response.ok(pagination).build();		
	}
		
	/**
	 * Remove um ou mais itens da tabela de arquivos de anexos.
	 * 
	 * @param ids
	 */
	@POST
	@Path("removeAnexoReciboByIds")
	public void removeAnexoReciboByIds(List<Long> ids) {
		anexoReciboFcService.removeByIds(ids);
	}
		
	private Integer getLimiteRegistros(FiltroPaginacaoDto filtro) {
		return Boolean.TRUE.equals(filtro.getReport()) ? Integer.parseInt(parametroGeralService.findByNomeParametro(ReciboRestConstants.VL_LIMITE_REGISTROS_CSV.getValue(), false).getDsConteudo()) : Integer.parseInt(parametroGeralService.findByNomeParametro("VL_LIMITE_REGISTROS_GRID", false).getDsConteudo());
	}
		
	private ReciboFreteCarreteiro getReciboFreteCarreteiro(Long id) {
		return reciboFreteCarreteiroService.findByIdCustom(id);
	}
			
	/**
	 * Verifica se o recibo possui anexos.
	 * 
	 * @param reciboFreteCarreteiro
	 * 
	 * @return boolean
	 */
	private boolean isAnexos(Long reciboFreteCarreteiroId) {
		return anexoReciboFcService.getRowCountItensByIdReciboFreteCarreteiro(reciboFreteCarreteiroId) > 0;		
				}
				
	/**
	 * Retorna os tipos de moedas existentes.
	 * 
	 * @return Response
	 */
	@GET
	@Path("populateMoedas")
	public Response populateMoedas() {
		List<Map<String,Object>> retorno = new ArrayList<Map<String,Object>>();
		
		Pais p = SessionUtils.getPaisSessao();
		List<MoedaPais> moedas = configuracoesFacade.getMoedasPais(p.getIdPais(),Boolean.TRUE);
		
		for (MoedaPais moedaPais : moedas) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("sigla",moedaPais.getMoeda().getSgMoeda() +" "+  moedaPais.getMoeda().getDsSimbolo());
			map.put("id", moedaPais.getIdMoedaPais());
			retorno.add(map);			
				}
		
		return Response.ok(retorno).build(); 
	}
				
	/**
	 * Popula filtro de pesquisa para a listagem de recibos. 
	 * 
	 * @param filtro
	 * @return TypedFlatMap
	 */
	private TypedFlatMap populateCriteria(FiltroPaginacaoDto filtro) {
		TypedFlatMap criteria = new TypedFlatMap(filtro.getFiltros());

		criteria.put("filial.idFilial", criteria.getLong("filialEmissao.idFilial"));
		criteria.put("proprietario.idProprietario",criteria.getLong("proprietario.idProprietario"));
		criteria.put("meioTransporteRodoviario.idMeioTransporte", criteria.getLong("meioTransporte.idMeioTransporte"));
		criteria.put("relacaoPagamento.nrRelacaoPagamento", criteria.getLong("nrRelacaoPagamento"));
		criteria.put("controleCarga.idControleCarga", criteria.getLong("controleCarga.idControleCarga"));
		criteria.put("reciboComplementado.nrReciboFreteCarreteiro", criteria.getLong("nrReciboComplementado"));
		criteria.put("reciboComplementado.idReciboFreteCarreteiro", criteria.getLong("idReciboFreteCarreteiro"));
		
		criteria.put("dhEmissaoInicial", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "periodoEmissaoInicial"));
		criteria.put("dhEmissaoFinal", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "periodoEmissaoFinal"));
		criteria.put("dtPagtoRealInicial", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "periodoPagamentoInicial"));
		criteria.put("dtPagtoRealFinal", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "periodoPagamentoFinal"));
		criteria.put("dtProgramadaPagtoInicial", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "periodoPagtoProgramadoInicial"));
		criteria.put("dtProgramadaPagtoFinal", RestPopulateUtils.getYearMonthDayFromISO8601(criteria, "periodoPagtoProgramadoFinal"));
		
		criteria.put("tpSituacaoRecibo", criteria.getString("tpSituacaoRecibo.value"));
		criteria.put(ReciboRestConstants.TP_RECIBO_FRETE_CARRETEIRO.getValue(), criteria.getString("tpReciboFreteCarreteiro.value"));
		
		return criteria;
	}
	
	/**
	 * Popula uma entidade de recibo coleta entrega a partir de um bean.
	 * 
	 * @param reciboFreteCarreteiro
	 * @param bean
	 */
	private void populaRecibo(ReciboFreteCarreteiro reciboFreteCarreteiro, Map<String, Object> bean) {
		setNrNotaFreteCarreteiro(reciboFreteCarreteiro, bean);    	
    	setObservacao(reciboFreteCarreteiro, bean);    	
    	setDtProgramadaPagamento(reciboFreteCarreteiro, bean);  
    	setTpSituacao(reciboFreteCarreteiro, bean);
	}
	
	/**
	 * Retorna observação, se houver.
	 * 
	 * @param reciboFreteCarreteiro
	 * @param bean
	 */
	private void setTpSituacao(ReciboFreteCarreteiro reciboFreteCarreteiro, Map<String, Object> bean) {
		String value = MapUtils.getString(MapUtils.getMap(bean, "tpSituacao"), "value");
				
		if(!StringUtils.isEmpty(value)){
    		reciboFreteCarreteiro.setTpSituacaoRecibo(new DomainValue(value));
    	}
	}

	/**
	 * Retorna dados para a suggest de recibos.
	 * 
	 * @param data
	 *            mapa com entrada "value" para "sgFilial" concatenado com
	 *            "nrReciboFreteCarreteiro" e entrada para filtro opcional
	 *            "sgFilial"
	 * @return
	 */
	@POST
	@Path("findReciboSuggest")
	public Response findReciboSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		String sgFilial = MapUtils.getString(data, ReciboRestConstants.SG_FILIAL.getValue());
		if (StringUtils.isBlank(value)) {	
			return Response.ok().build();
		}

		Integer limiteRegistros = getLimiteRegistros(value);
							
		Long nrReciboFreteCarreteiro = null;
		
		if (StringUtils.isNumeric(value)) {
			nrReciboFreteCarreteiro = Long.valueOf(value);
		} else if(StringUtils.isBlank(sgFilial) && !StringUtils.isNumeric(value.substring(SIZE_SIGLA))){
			sgFilial = value.substring(0, SIZE_SIGLA).toUpperCase();
			nrReciboFreteCarreteiro = Long.valueOf(value.substring(SIZE_SIGLA));
		}
			
		if(sgFilial == null && nrReciboFreteCarreteiro == null){
			return Response.ok().build();
		}
		
		List<Map<String, Object>> findReciboSuggest = reciboFreteCarreteiroService.findReciboSuggest(sgFilial, nrReciboFreteCarreteiro, limiteRegistros == null ? null : limiteRegistros + 1);
				
		return new SuggestResponseBuilder(findReciboSuggest, limiteRegistros).build();		
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
		
	/**
	 * Popula mapa com os atributos que serão necessários para visualizar a
	 * tela.
	 * 
	 * @param rfc
	 * 
	 * @return TypedFlatMap
	 */
	private TypedFlatMap createResultMap(ReciboFreteCarreteiro rfc) {
		TypedFlatMap result = new TypedFlatMap();
		
		getDadosRecibo(rfc, result);
    	
		getDataProgramada(rfc, result);
		
		getRelacaoPagamento(rfc, result);
		
		getFilial(rfc, result);

		getProprietario(rfc, result);

		getBeneficiario(rfc, result);

		getContaBancaria(rfc, result);

		getMeioTransporte(rfc, result);

		getControleCarga(rfc, result);

		getPendencia(rfc, result);		

		getMoedaPais(rfc, result);
		
		getNotasCredito(rfc, result);
		
		return result;
	}

	/**
	 * Retorna pendência, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getPendencia(ReciboFreteCarreteiro rfc,TypedFlatMap result) {
		Pendencia pendencia = rfc.getPendencia();

		if (pendencia == null) {
			result.put(ReciboHelperConstants.EVENTO_2401.getValue(), false);
			result.put(ReciboHelperConstants.EVENTO_2402.getValue(), false);
			return;
		}

		DomainValue tpSituacaoPendencia = pendencia.getTpSituacaoPendencia();
		result.put("idPendencia", pendencia.getIdPendencia());
		result.put("tpSituacaoPendenciaDesc", tpSituacaoPendencia.getDescription());
		
		result.put(ReciboHelperConstants.EVENTO_2401.getValue(), ConstantesWorkflow.NR2401_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_ANALISTAS.equals(rfc.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento()));
		result.put(ReciboHelperConstants.EVENTO_2402.getValue(), ConstantesWorkflow.NR2402_APROVACAO_RECIBO_COMPLEMENTAR_FRETE_CARRETEIRO_COORDENACAO.equals(rfc.getPendencia().getOcorrencia().getEventoWorkflow().getTipoEvento().getNrTipoEvento()));
	}

	/**
	 * Retorna dados genéricos do recibo. 
	 * 
	 * @param rfc
	 * @param parametro
	 * @param result
	 * @param reciboComplementado
	 * @param complementar
	 */
	private void getDadosRecibo(ReciboFreteCarreteiro rfc, TypedFlatMap result) {
		Boolean complementar = rfc.getReciboComplementado() != null;
		
		result.put(ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue(), rfc.getIdReciboFreteCarreteiro());		
		result.put(ReciboRestConstants.ANEXOS.getValue(), isAnexos(rfc.getIdReciboFreteCarreteiro()));
		
		DomainValue tpSituacaoRecibo = rfc.getTpSituacaoRecibo();
		result.put(ReciboRestConstants.TP_SITUACAO_RECIBO_VALUE.getValue(), tpSituacaoRecibo.getValue());
		result.put("coletaEntrega", "C".equals(rfc.getTpReciboFreteCarreteiro().getValue()));
		result.put("tpSituacaoReciboDesc", tpSituacaoRecibo.getDescription());
		result.put("tpSituacaoRecibo", tpSituacaoRecibo);
		result.put("blComplementar", complementar);
		result.put(ReciboRestConstants.TP_RECIBO_FRETE_CARRETEIRO.getValue(), rfc.getTpReciboFreteCarreteiro().getDescriptionAsString());
		result.put("tpReciboFreteCarreteiroValue", rfc.getTpReciboFreteCarreteiro().getValue());
		
		String nrRecibo = FormatUtils.formatLongWithZeros(rfc.getNrReciboFreteCarreteiro(), "0000000000");
		result.put("nrReciboFreteCarreteiro", nrRecibo);
		result.put("nrReciboFreteCarreteiro2", nrRecibo + (complementar ? "C" : ""));

		result.put("nrNfCarreteiro", rfc.getNrNfCarreteiro());
		result.put("obReciboFreteCarreteiro", rfc.getObReciboFreteCarreteiro());
		result.put("dhEmissao", rfc.getDhEmissao());
		result.put("dtContabilizacao", rfc.getDtContabilizacao());
		result.put("dhGeracaoMovimento", rfc.getDhGeracaoMovimento());
		result.put("dtPagtoReal", rfc.getDtPagtoReal());		
		result.put("vlSalarioContribuicao", rfc.getVlSalarioContribuicao());
		result.put("pcAliquotaInss", rfc.getPcAliquotaInss());
		result.put("vlOutrasFontes", rfc.getVlOutrasFontes());
		result.put("vlApuradoInss", rfc.getVlInss());
		result.put("pcAliquotaIssqn", rfc.getPcAliquotaIssqn());
		result.put("vlIssqn", rfc.getVlIssqn());
		result.put("pcAliquotaIrrf", rfc.getPcAliquotaIrrf());
		result.put("vlIrrf", rfc.getVlIrrf());
		result.put("vlBruto", rfc.getVlBruto());
		result.put("vlPostoPassagem", rfc.getVlPostoPassagem());
		result.put("vlDesconto", rfc.getVlDesconto());
		result.put("vlLiquido", rfc.getVlLiquido());
		result.put("dhEnvioJde", rfc.getDhEnvioJde());

		result.put("nmUsuarioLogado", SessionUtils.getUsuarioLogado().getNmUsuario());
		
		if(complementar){
			ReciboFreteCarreteiro rfcComplementado = rfc.getReciboComplementado();
	    	
	    	Map<String, Object> reciboComplementado = new HashMap<String, Object>();
	    	reciboComplementado.put("idReciboFreteCarreteiro", rfcComplementado.getIdReciboFreteCarreteiro());
	    	reciboComplementado.put("nrReciboFreteCarreteiro", FormatUtils.formatLongWithZeros(rfcComplementado.getNrReciboFreteCarreteiro(), "0000000000"));
	    	reciboComplementado.put("sgFilial", rfcComplementado.getFilial().getSgFilial());
	    		
	    	result.put("reciboComplementado", reciboComplementado);
		}		
		
		Map<String, Object> rfcMap = new HashMap<String, Object>();
		rfcMap.put("idReciboFreteCarreteiro", rfc.getIdReciboFreteCarreteiro());
		rfcMap.put("nrReciboFreteCarreteiro", FormatUtils.formatLongWithZeros(rfc.getNrReciboFreteCarreteiro(), "0000000000"));
		rfcMap.put("sgFilial", rfc.getFilial().getSgFilial());
    		
    	result.put("recibo", rfcMap);
	}

	/**
	 * Retorna data de pagamento programada.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getDataProgramada(ReciboFreteCarreteiro rfc, TypedFlatMap result) {
		if (rfc.getDtProgramadaPagto() == null) {
			ParametroGeral parametro = parametroGeralService.findByNomeParametro(ReciboRestConstants.VL_DT_SUGERIDA_PAGTO.getValue(), false);
			
			if (parametro != null && rfc.getDhEmissao() != null) {
				Integer numeroDiasPagamento = Integer.parseInt(parametro.getDsConteudo());
				result.put(ReciboRestConstants.DT_PROGRAMADA_PAGTO.getValue(),rfc.getDhEmissao().plusDays(numeroDiasPagamento));
	    	} else {
					result.put(ReciboRestConstants.DT_PROGRAMADA_PAGTO.getValue(), null);
	    	}
		} else {
			result.put(ReciboRestConstants.DT_PROGRAMADA_PAGTO.getValue(),rfc.getDtProgramadaPagto());					
		}
    }

	/**
	 * Retorna relação de pagamento, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getRelacaoPagamento(ReciboFreteCarreteiro rfc,
			TypedFlatMap result) {
		if (rfc.getRelacaoPagamento() == null) {
			return;
		}
		
		result.put("nrRelacaoPagamento", rfc.getRelacaoPagamento().getNrRelacaoPagamento());		
	}

	/**
	 * Retorna controle de carga, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getControleCarga(ReciboFreteCarreteiro rfc,
			TypedFlatMap result) {
		ControleCarga controleCarga = rfc.getControleCarga();
    	
		if (controleCarga == null) {
			return;
		}
    		
		result.put("idControleCarga", controleCarga.getIdControleCarga());
		result.put("nrControleCarga", controleCarga.getNrControleCarga());
		
		Filial filialCCByIdFilialOrigem = controleCarga.getFilialByIdFilialOrigem();
		result.put("controleCargaIdFilial", filialCCByIdFilialOrigem.getIdFilial());
		result.put("controleCargaSgFilial", filialCCByIdFilialOrigem.getSgFilial());
		result.put("controleCargaNmFilial", filialCCByIdFilialOrigem.getPessoa().getNmFantasia());
		result.put("controleCargaDhEvento", reciboFreteCarreteiroService.findCCDhEvento(controleCarga.getIdControleCarga()));    	   	
		
		List<PagtoPedagioCc> pagtos = controleCarga.getPagtoPedagioCcs();

		if (pagtos != null && !pagtos.isEmpty()) {
			PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) pagtos.get(0);
			CartaoPedagio cartaoPedagio = pagtoPedagioCc.getCartaoPedagio();
			
			if (cartaoPedagio != null) {
				result.put("nrCartao", cartaoPedagio.getNrCartao());
    	}
		}
	}
    	    	
	/**
	 * Retorna meio de transporte, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getMeioTransporte(ReciboFreteCarreteiro rfc,
			TypedFlatMap result) {
		MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();
    	
		if (meioTransporte == null) {
			return;
    }
	
		result.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
		result.put("nrFrota", meioTransporte.getNrFrota());
		result.put("nrIdentificador", meioTransporte.getNrIdentificador());
		result.put("dsMarcaMeioTransporte", meioTransporte.getModeloMeioTransporte().getMarcaMeioTransporte().getDsMarcaMeioTransporte());
		result.put("dsModeloMeioTransporte", meioTransporte.getModeloMeioTransporte().getDsModeloMeioTransporte());
	}

	/**
	 * Retorna conta bancária, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getContaBancaria(ReciboFreteCarreteiro rfc,
			TypedFlatMap result) {
		ContaBancaria contaBancaria = rfc.getContaBancaria();

		if (contaBancaria == null) {
			return;
		}

		AgenciaBancaria agenciaBancaria = contaBancaria.getAgenciaBancaria();
		Banco banco = agenciaBancaria.getBanco();
		result.put("nrContaBancaria", contaBancaria.getNrContaBancaria());
		result.put("dvContaBancaria", contaBancaria.getDvContaBancaria());
		result.put("nrAgenciaBancaria", agenciaBancaria.getNrAgenciaBancaria());
		result.put("nrDigito", agenciaBancaria.getNrDigito());
		result.put("nrBanco", banco.getNrBanco());
	}

	/**
	 * Retorna beneficiário, se houver.
	 * 
	 * @param rfc
	 * @param postoConveniado
	 * @param result
	 */
	private void getBeneficiario(ReciboFreteCarreteiro rfc, TypedFlatMap result) {
		Beneficiario beneficiario = rfc.getBeneficiario();
		PostoConveniado postoConveniado = adiantamentoTrechoService.findPostoConveniadoByIdReciboFreteCarreteiro(rfc.getIdReciboFreteCarreteiro());
		
		Pessoa beneficiarioPessoa = null;
		
		if (beneficiario != null) {
			beneficiarioPessoa = beneficiario.getPessoa();
		} else {
			if (postoConveniado != null) {
				beneficiarioPessoa = postoConveniado.getPessoa();
	}
		}
	
		if (beneficiarioPessoa == null) {
			return;
		}

		result.put("idBeneficiario", beneficiarioPessoa.getIdPessoa());
		result.put("nmPessoaBeneficiario", beneficiarioPessoa.getNmPessoa());
		result.put("nrIdentificacaoBeneficiarioFormatado", FormatUtils
				.formatIdentificacao(beneficiarioPessoa.getTpIdentificacao(),
						beneficiarioPessoa.getNrIdentificacao()));
	}

	/**
	 * Retorna proprietário, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getProprietario(ReciboFreteCarreteiro rfc,
			TypedFlatMap result) {
		Pessoa proprietarioPessoa = rfc.getProprietario().getPessoa();
		
		if (proprietarioPessoa == null) {
			return;
		}
		
		result.put("idProprietario", proprietarioPessoa.getIdPessoa());
		result.put("nmPessoaProprietario", proprietarioPessoa.getNmPessoa());
		result.put("nrIdentificacaoProprietarioFormatado", FormatUtils
				.formatIdentificacao(proprietarioPessoa.getTpIdentificacao(),
						proprietarioPessoa.getNrIdentificacao()));
	}
	
	/**
	 * Retorna filia, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getFilial(ReciboFreteCarreteiro rfc, TypedFlatMap result) {
		Filial filial = rfc.getFilial();

		if (filial == null) {
			return;
		}
			
		Map<String, Object> mapFilial = new HashMap<String, Object>();		
		mapFilial.put("idFilial", filial.getIdFilial());
		mapFilial.put(ReciboRestConstants.SG_FILIAL.getValue(), filial.getSgFilial());
		mapFilial.put("nmFilial", filial.getPessoa().getNmFantasia());
		
		result.put("filialEmissao", mapFilial);
	}
							
	/**
	 * Retorna data programada de pagamento, se houver.
	 * 
	 * @param reciboFreteCarreteiro
	 * @param bean
	 */
	private void setDtProgramadaPagamento(ReciboFreteCarreteiro reciboFreteCarreteiro, Map<String, Object> bean) {
		reciboFreteCarreteiro.setDtProgramadaPagto(RestPopulateUtils.getYearMonthDayFromISO8601(bean, "dtProgramadaPagto"));
	}

	/**
	 * Retorna observação, se houver.
	 * 
	 * @param reciboFreteCarreteiro
	 * @param bean
	 */
	private void setObservacao(ReciboFreteCarreteiro reciboFreteCarreteiro,Map<String, Object> bean) {
		Object obReciboFreteCarreteiro = bean.get("obReciboFreteCarreteiro");
		
		if(obReciboFreteCarreteiro != null){
    		reciboFreteCarreteiro.setObReciboFreteCarreteiro(String.valueOf(obReciboFreteCarreteiro));
    	} else {
    		reciboFreteCarreteiro.setObReciboFreteCarreteiro("");
    	}
	}
	
	/**
	 * Retorna número da nota do frete carreteiro, se houver.
	 * 
	 * @param reciboFreteCarreteiro
	 * @param bean
	 */
	private void setNrNotaFreteCarreteiro(ReciboFreteCarreteiro reciboFreteCarreteiro, Map<String, Object> bean) {
		Object nrNfCarreteiro = bean.get("nrNfCarreteiro");
				
		if(nrNfCarreteiro != null){
    		reciboFreteCarreteiro.setNrNfCarreteiro(String.valueOf(nrNfCarreteiro));
    	}
	}
	
	/**
	 * Retorna moeda do pais, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getMoedaPais(ReciboFreteCarreteiro rfc,
			TypedFlatMap result) {
		MoedaPais moedaPais = rfc.getMoedaPais();
	
		if (moedaPais == null) {
			return;
		}
	
		result.put("idMoedaPais", moedaPais.getIdMoedaPais());
		result.put("siglaSimboloMoeda", rfc.getMoedaPais().getMoeda().getSiglaSimbolo());
	}
	
	/**
	 * Retorna moeda do pais, se houver.
	 * 
	 * @param rfc
	 * @param result
	 */
	private void getNotasCredito(ReciboFreteCarreteiro rfc,TypedFlatMap result) {
		List<NotaCredito> notas = rfc.getNotaCreditos();
	
		if (notas == null) {
			return;
		}
		List<Map<String,Object>> notasRetorno = new ArrayList<Map<String,Object>>(); 
		
		for (NotaCredito nc : notas) {
			Map<String,Object> nota = new HashMap<String, Object>();
			nota.put("filial", nc.getFilial().getSgFilial());
			nota.put("nrNotaCredito", nc.getNrNotaCredito());
			if(nc.getTpNotaCredito() != null){
				nota.put("tipo", nc.getTpNotaCredito().getDescriptionAsString());
			}else{
				nota.put("tipo", "-");	
			}
			if(nc.getControleCarga() != null){
				nota.put("filialControleCarga", nc.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
				nota.put("nrControleCarga", nc.getControleCarga().getNrControleCarga());
			}else{
				nota.put("filialControleCarga", "-");
				nota.put("nrControleCarga", "-" );
			}
			if(nc.getVlTotal() != null){
				nota.put("valor", nc.getVlTotal());	
			}else{
				nota.put("valor", "-");	
			}
			
			nota.put("dataEmissao", nc.getDhEmissao());
			notasRetorno.add(nota);
		}
	
		result.put("notasCredito", notasRetorno);
	}

	private Map<String,String> getColumn(String label, String column) {
		Map<String,String> retorno = new HashMap<String, String>();
		retorno.put("title", getLabel(label));
		retorno.put("column", column);
		return retorno;
	}

}