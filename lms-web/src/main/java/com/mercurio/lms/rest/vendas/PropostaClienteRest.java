package com.mercurio.lms.rest.vendas;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
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
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Funcionario;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.MoedaDTO;
import com.mercurio.lms.rest.json.YearMonthDayDeserializer;
import com.mercurio.lms.rest.tabeladeprecos.ProdutoEspecificoDTO;
import com.mercurio.lms.rest.tabeladeprecos.ServicoSuggestDTO;
import com.mercurio.lms.rest.tabeladeprecos.TabelaPrecoSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.BotoesPropostaDTO;
import com.mercurio.lms.rest.vendas.dto.CamposPropostaDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.DivisaoClienteSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.FuncionarioPromotorSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.HistoricoEfetivacaoDTO;
import com.mercurio.lms.rest.vendas.dto.PropostaClienteDTO;
import com.mercurio.lms.rest.vendas.dto.PropostaClienteFiltroDTO;
import com.mercurio.lms.rest.vendas.dto.PropostaClienteServicoAdicionalFiltroDTO;
import com.mercurio.lms.rest.vendas.dto.ServicoAdicionalClienteDTO;
import com.mercurio.lms.rest.vendas.dto.SimulacaoAnexoDTO;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ProdutoEspecificoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DiaFaturamento;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.MotivoReprovacao;
import com.mercurio.lms.vendas.model.PrazoVencimento;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.SimulacaoAnexo;
import com.mercurio.lms.vendas.model.service.DiaFaturamentoService;
import com.mercurio.lms.vendas.model.service.HistoricoEfetivacaoService;
import com.mercurio.lms.vendas.model.service.MotivoReprovacaoService;
import com.mercurio.lms.vendas.model.service.PrazoVencimentoService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import com.mercurio.lms.vendas.model.service.SimulacaoAnexoService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.report.EmitirPropostaService;
import com.mercurio.lms.vendas.report.EmitirTabelaPropostaService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

@Path("/vendas/proposta")
public class PropostaClienteRest extends LmsBaseCrudReportRest<PropostaClienteDTO, PropostaClienteDTO, PropostaClienteFiltroDTO> {
	
	private static final int SIZE = 9;
	private static final String TABELA_PRECO_FOB = "tabelaPrecoFob.";

	@InjectInJersey
	private SimulacaoService simulacaoService;
	
	@InjectInJersey
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	
	@InjectInJersey
	private EmitirPropostaService emitirPropostaService;
	
	@InjectInJersey
	private HistoricoEfetivacaoService historicoEfetivacaoService;
	
	@InjectInJersey
	private SimulacaoAnexoService simulacaoAnexoService;
	
	@InjectInJersey
	private EmitirTabelaPropostaService emitirTabelaPropostaService;
	
	@InjectInJersey
	private ReportExecutionManager reportExecutionManager;
	
	@InjectInJersey
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	
	@InjectInJersey
	private ServicoService servicoService;
	
	@InjectInJersey
	private ConfiguracoesFacade configuracoesFacade;
	
	@InjectInJersey
	private DiaFaturamentoService diaFaturamentoService;
	
	@InjectInJersey
	private PrazoVencimentoService prazoVencimentoService;
	
	@InjectInJersey
	private MotivoReprovacaoService motivoReprovacaoService;
	
	@InjectInJersey
	private ProdutoEspecificoService produtoEspecificoService;
	
	@InjectInJersey
	private DomainValueService domainValueService;
	
	
	@Override
	protected List<Map<String, String>> getColumns() {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		list.add(getColumn("dataSimulacao", "dataSimulacao"));
		list.add(getColumn("numeroProposta", "numeroProposta"));
		list.add(getColumn("cliente", "nmCliente"));
		list.add(getColumn("tabelaPreco", "tabelaPreco"));
		list.add(getColumn("tabelaFob", "tabelaPrecoFob"));
		list.add(getColumn("situacao", "tpSituacaoAprovacao"));
		list.add(getColumn("efetivada", "efetivada"));
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List<Map<String, Object>> findDataForReport(PropostaClienteFiltroDTO filter) {
		return this.simulacaoService.findDataForReport(this.getTypedFlatMap(filter));
	}

	private Map getTypedFlatMap(PropostaClienteFiltroDTO filter) {
		return this.updateInternalFilterWithFilterData(filter, new TypedFlatMap());
	}

	@Override
	protected Integer count(PropostaClienteFiltroDTO filter) {
		Map filterMap = this.getTypedFlatMap(filter);
		if (filter.getCliente() != null){
			filterMap.put("clienteByIdCliente.idCliente", filter.getCliente().getIdCliente());
		}
		return this.simulacaoService.getRowCount(filterMap);
	}

	/**
	 * Cria um mapa com as chaves que são esperadas na busca já existente que também é utilizada na tela Web Antiga.
	 * 
	 * @param param
	 * @return
	 */
	private TypedFlatMap getFiltrosServicosAdicionais(PropostaClienteServicoAdicionalFiltroDTO filter) {
		TypedFlatMap toReturn = getFiltrosBasicosPaginacaoAbas(filter);
		
		if(filter.getTpIndicador() != null){
			toReturn.put("tpIndicador", filter.getTpIndicador().getValue());
		}
		
		if(filter.getIdParcelaPreco() != null){
			toReturn.put("parcelaPreco.idParcelaPreco", filter.getIdParcelaPreco());
		}
		
		return toReturn;
	}
	
	private TypedFlatMap getFiltrosBasicosPaginacaoAbas(BaseFilterDTO filter) {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("_currentPage", filter.getPagina() == null ? "1" : String.valueOf(filter.getPagina()));
		toReturn.put("_pageSize", filter.getQtRegistrosPagina() == null ? String.valueOf(ROW_LIMIT) : String.valueOf(filter.getQtRegistrosPagina()));
		toReturn.put("simulacao.idSimulacao", filter.getId());
		return toReturn;
	}
	
	@POST
	@SuppressWarnings("rawtypes")
	@Path("findServicosAdicionais")
	public Response findServicosAdicionais(PropostaClienteServicoAdicionalFiltroDTO filter) {
		TypedFlatMap criteria = getFiltrosServicosAdicionais(filter);
		ResultSetPage rsp = getServicoAdicionalClienteService().findPaginatedByProposta(criteria);
		Integer qtRegistros = getServicoAdicionalClienteService().getRowCountByProposta(criteria);
		return getReturnFind(convertToServicoAdicionalDTO(rsp.getList()), qtRegistros);
	}
	
	private List<ServicoAdicionalClienteDTO> convertToServicoAdicionalDTO(List list) {
		List<ServicoAdicionalClienteDTO> servicosAdicionais = new ArrayList<ServicoAdicionalClienteDTO>();
		for (Object obj : list) {
			ServicoAdicionalCliente servicoAdicionalCliente = (ServicoAdicionalCliente) obj;
			ServicoAdicionalClienteDTO servicoAdicionalClienteDTO = new ServicoAdicionalClienteDTO(servicoAdicionalCliente.getIdServicoAdicionalCliente(), servicoAdicionalCliente.getVlValor(), servicoAdicionalCliente.getVlValorFormatado(), servicoAdicionalCliente.getTpIndicador().getDescriptionAsString(), servicoAdicionalCliente.getParcelaPreco().getNmParcelaPreco().getValue());
			servicosAdicionais.add(servicoAdicionalClienteDTO);
		}
		return servicosAdicionais;
	}

	@POST
	@Path("findTabelaPrecoParcelaCombo")
	public Map findTabelaPrecoParcelaCombo(Map<String, Object> criteria) {
		List<TypedFlatMap> servicosMap = getTabelaPrecoParcelaService().findMappedList(criteria, "S".equals(MapUtils.getString(criteria, "tpSituacao")));
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("servicos", servicosMap);
		return retorno;
	}
	
	@POST
	@Path("findServicosCombo")
	public Response buscaServicosTabela(Map<String, Object> criteria) {
		List<Servico> entidades = servicoService.find(criteria);
		List<ServicoSuggestDTO> servicos = new ArrayList<ServicoSuggestDTO>();
		for (Servico servico : entidades) {
			servicos.add(new ServicoSuggestDTO(servico.getIdServico(), servico.getDsServico().getValue(), servico.getTpModal(), servico.getTpAbrangencia()));
		}
		return Response.ok(servicos).build();
	}
	
	@POST
	@Path("findServicoPadrao")
	public Response buscaServicoPadrao() {
		BigDecimal idServicoPadrao = (BigDecimal) configuracoesFacade.getValorParametro("SERVICO_PADRAO");
		Servico servico = servicoService.findById(idServicoPadrao.longValue());
		return Response.ok(new ServicoSuggestDTO(servico.getIdServico(), servico.getDsServico().getValue(), servico.getTpModal(), servico.getTpAbrangencia())).build();
	}
	
	@POST
	@Path("findMotivoReprovacaoCombo")
	public Response findMotivoReprovacaoCombo(Map<String, Object> criteria) {
		List<MotivoReprovacao> motivos = motivoReprovacaoService.findMotivosReprovacao();
		return Response.ok(motivos).build();
	}
	
	@POST
	@SuppressWarnings("rawtypes")
	@Path("aprovarProposta")
    public void aprovarProposta(Map criteria) throws Exception {
		Simulacao simulacao = simulacaoService.findById(LongUtils.getLong(criteria.get("id")));
		SimulacaoUtils.setSimulacaoInSession(simulacao);
		simulacaoService.executeAprovacaoProposta();
		SimulacaoUtils.removeSimulacaoFromSession();
    }
	
	@POST
	@SuppressWarnings("rawtypes")
	@Path("findHistoricoEfetivacaoList")
	public Response findHistoricoEfetivacaoList(BaseFilterDTO filter) {
		TypedFlatMap criteria = getFiltrosBasicosPaginacaoAbas(filter);
		ResultSetPage rsp = historicoEfetivacaoService.findHistoricoEfetivacaoList(criteria);
		Integer qtRegistros = historicoEfetivacaoService.findHistoricoEfetivacaoRowCount(criteria);
		return getReturnFind(convertToHistoricoEfetivacaoDTO(rsp.getList()), qtRegistros);
	}
	
	private List<HistoricoEfetivacaoDTO> convertToHistoricoEfetivacaoDTO(List list) {
		List<HistoricoEfetivacaoDTO> historicos = new ArrayList<HistoricoEfetivacaoDTO>();
		for (Object obj : list) {
			TypedFlatMap map = new TypedFlatMap((Map) obj);
			HistoricoEfetivacaoDTO historicoEfetivacaoDTO = new HistoricoEfetivacaoDTO(map.getLong("idHistoricoEfetivacao"),
					map.getDateTime("dhSolicitacao"), map.getString("usuarioSolicitacao_nmUsuario"), map.getDateTime("dhReprovacao"),
					map.getString("usuarioReprovador_nmUsuario"), map.getString("dsMotivo"), map.getString("motivoReprovacao_dsMotivo"));
			historicos.add(historicoEfetivacaoDTO);
		}
		return historicos;
	}
	
	@POST
	@Path("removeServicoAdicionalClienteByIds")
	public Response removeServicoAdicionalClienteByIds(List<Long> ids) {
		getServicoAdicionalClienteService().removeByIdsPropostas(ids);
		return Response.ok().build();
	}
	
	@POST
	@Path("storeServicoAdicionalCliente")
	public Response storeServicoAdicionalCliente(ServicoAdicionalClienteDTO servicoAdicionalClienteDTO) {
		ServicoAdicionalCliente servicoAdicionalCliente = convertToServicoAdicionalCliente(servicoAdicionalClienteDTO);
		getServicoAdicionalClienteService().store(servicoAdicionalCliente);
		servicoAdicionalClienteDTO.setIdServicoAdicionalCliente(servicoAdicionalCliente.getIdServicoAdicionalCliente());
		return Response.ok(servicoAdicionalClienteDTO).build();
	}
	
	/**
	 * Método implementado de forma a garantir compatibilidade com método já existente da web antiga.
	 * @param idSimulacao
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("solicitarEfetivacaoProposta")
    public Response solicitarEfetivacaoProposta(@QueryParam("idSimulacao") Long idSimulacao) throws Exception {
		TypedFlatMap map = new TypedFlatMap();
		map.put("idSimulacao", idSimulacao);
		TypedFlatMap retorno = simulacaoService.executeSolicitarEfetivacaoProposta(map);
		SimulacaoUtils.removeSimulacaoFromSession();
		return Response.ok(retorno).build();
    }
	
	@POST
	@Path("reprovarEfetivacaoProposta")
	public Response reprovarEfetivacaoProposta(Map criteria) throws Exception {
		Long idSimulacao = LongUtils.getLong(criteria.get("idSimulacao"));
		String observacaoReprovacao = MapUtils.getString(criteria, "observacaoReprovacao");
		Long idMotivoReprovacao = LongUtils.getLong(criteria.get("idMotivoReprovacao"));
		
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("idSimulacao", idSimulacao);
		parameters.put("observacaoReprovacao", observacaoReprovacao);
		parameters.put("idMotivoReprovacao", idMotivoReprovacao);
		
		simulacaoService.executeReprovarEfetivacao(parameters);
		
		return Response.ok().build();
    }
	
	@POST
	@SuppressWarnings("rawtypes")
	@Path("efetivarProposta")
    public Response efetivarProposta(TypedFlatMap map) throws Exception {
		Long idSimulacao = LongUtils.getLong(map.get("id"));
		YearMonthDay dtInicioVigencia = YearMonthDayDeserializer.parse(map.getString("dtTabelaVigenciaInicial"));
		Boolean blConfirmaEfetivarProposta = map.getBoolean("blConfirmaEfetivarProposta");
		
		Long idDivisaoCliente = null;
		if(map.get("divisaoCliente") != null){
			idDivisaoCliente = MapUtils.getLong((Map) map.get("divisaoCliente"), "idDivisaoCliente");
		}
		
		simulacaoService.storeEfetivacaoProposta(idSimulacao, idDivisaoCliente, dtInicioVigencia, blConfirmaEfetivarProposta);
		
		PropostaClienteDTO propostaClienteDTO = findById(idSimulacao);
		return Response.ok(propostaClienteDTO).build();
    }
	
	
	@POST
	@Path("findComboProdutosEspecificos")
	public Response findComboProdutosEspecificos(){
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("tpSituacao", "A");
		
		List<ProdutoEspecificoDTO> dtos = new ArrayList<ProdutoEspecificoDTO>();
		List<ProdutoEspecifico> produtoEspecificoList = produtoEspecificoService.find(criteria);
		if (produtoEspecificoList!= null && !produtoEspecificoList.isEmpty()){
			for(ProdutoEspecifico produtoEspecifico:produtoEspecificoList){
				dtos.add(new ProdutoEspecificoDTO(produtoEspecifico.getIdProdutoEspecifico(), produtoEspecifico.getDsProdutoEspecifico().getValue(), produtoEspecifico.getNrTarifaEspecifica()));
			}
		}
		
		return Response.ok(dtos).build();
	}
	
	
	@POST
	@Path("imprimirProposta")
	@SuppressWarnings("rawtypes")
    public Response imprimirProposta(Map criteria) throws Exception {
		Simulacao simulacao = simulacaoService.findById(LongUtils.getLong(criteria.get("id")));

		File reportFile = null;
		TypedFlatMap parameters = convertToOldMap(criteria);;
		if (ConstantesVendas.TP_PROPOSTA_PROMOCIONAL.equals(simulacao.getTpGeracaoProposta().getValue())){
			parameters.put("tpGeracaoProposta", simulacao.getTpGeracaoProposta().getValue());
			
			parameters.put("idServico", simulacao.getServico().getIdServico());
			parameters.put("dsSimbolo", ((Map)((Map) criteria.get("tabelaPreco")).get("moeda")).get("dsSimbolo"));
			parameters.put("idTabelaPreco", ((Map) criteria.get("tabelaPreco")).get("idTabelaPreco"));
			
			MultiReportCommand command = new MultiReportCommand("impressaoProposta");
			command.addCommand("lms.vendas.emitirPropostaCapaService", parameters);
			command.addCommand("lms.vendas.emitirPropostaService", parameters);
			command.addCommand("lms.vendas.clienteContratoTermosCondicoesService", parameters);
			
			reportFile = reportExecutionManager.executeMultiReport(command);

		} else {
			//Implementado desta forma para manter compatibilidade com método de impressão do relatório já existente.
			SimulacaoUtils.setSimulacaoInSession(simulacao);
			reportFile = emitirTabelaPropostaService.executePDF(parameters);
			SimulacaoUtils.removeSimulacaoFromSession();
		}
		
		String reportLocator = reportExecutionManager.generateReportLocator(reportFile);
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("reportLocator", reportLocator);
		return Response.ok(retorno).build();
    }
	
	
	/**
	 * Recebe o map com os critérios vindo da tela do novo front end, e cria um
	 * novo mapa com as chaves esperadas pelo método de impressão já existente.
	 * 
	 * @param criteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private TypedFlatMap convertToOldMap(Map criteria) {
		TypedFlatMap parameters = new TypedFlatMap();
		
		if(criteria.get("divisaoCliente") != null){
			parameters.put("divisaoCliente.idDivisaoCliente", MapUtils.getLong((Map) criteria.get("divisaoCliente"), "idDivisaoCliente"));
		}
		if(criteria.get("cliente") != null){
			parameters.put("cliente.idCliente", MapUtils.getLong((Map) criteria.get("cliente"), "idCliente"));
		}
		
		parameters.put("ordenacao", (String) criteria.get("ordenacao"));
		parameters.put("simulacao.idSimulacao", LongUtils.getLong(criteria.get("id")));
		parameters.put("filial.idFilial", MapUtils.getLong(criteria, "idFilial"));
		parameters.put("pendencia.idPendencia", MapUtils.getLong(criteria, "idPendenciaAprovacao"));
		parameters.put("dtValidadeProposta", YearMonthDayDeserializer.parse(MapUtils.getString(criteria, "dtValidadeProposta")));
		parameters.put("nrDiasPrazoPagamento", MapUtils.getInteger(criteria, "nrDiasPrazoPagamento"));
		parameters.put("nrProposta", MapUtils.getInteger(criteria, "nrSimulacao"));
		parameters.put("dtSimulacao", MapUtils.getString(criteria, "dtSimulacao"));
		parameters.put("sgFilial", MapUtils.getString(criteria, "sgFilial"));
		parameters.put("situacao", ((Map)((Map) criteria.get("tpSituacaoAprovacao")).get("description")).get("value"));
		
		return parameters;
	}

	private ServicoAdicionalCliente convertToServicoAdicionalCliente(ServicoAdicionalClienteDTO servicoAdicionalClienteDTO) {
		ServicoAdicionalCliente servicoAdicionalCliente = new ServicoAdicionalCliente();
		servicoAdicionalCliente.setIdServicoAdicionalCliente(servicoAdicionalClienteDTO.getIdServicoAdicionalCliente());
		servicoAdicionalCliente.setBlCobrancaRetroativa(servicoAdicionalClienteDTO.getBlCobrancaRetroativa());
		servicoAdicionalCliente.setNrDecursoPrazo(servicoAdicionalClienteDTO.getNrDecursoPrazo());
		servicoAdicionalCliente.setNrQuantidadeDias(servicoAdicionalClienteDTO.getNrQuantidadeDias());
		ParcelaPreco parcelaPreco = new ParcelaPreco(); 
		parcelaPreco.setIdParcelaPreco(LongUtils.getLong(servicoAdicionalClienteDTO.getParcelaPreco().get("idParcelaPreco")));
		servicoAdicionalCliente.setParcelaPreco(parcelaPreco);
		Simulacao simulacao = new Simulacao();
		simulacao.setIdSimulacao(LongUtils.getLong(servicoAdicionalClienteDTO.getSimulacao().get("idSimulacao")));
		servicoAdicionalCliente.setSimulacao(simulacao);
		servicoAdicionalCliente.setTpFormaCobranca(servicoAdicionalClienteDTO.getTpFormaCobranca());
		servicoAdicionalCliente.setTpIndicador(servicoAdicionalClienteDTO.getTpIndicador());
		servicoAdicionalCliente.setVlMinimo(servicoAdicionalClienteDTO.getVlMinimo());
		servicoAdicionalCliente.setVlValor(servicoAdicionalClienteDTO.getVlValor());
		servicoAdicionalCliente.setTpUnidMedidaCalcCobr(servicoAdicionalClienteDTO.getTpUnidMedidaCalcCobr());
		return servicoAdicionalCliente;
	}
	
	
	@GET
	@Path("findByIdServicoAdicionalCliente")
	public Response findByIdServicoAdicionalCliente(@QueryParam("idServicoAdicionalCliente") Long idServicoAdicionalCliente) {
		ServicoAdicionalCliente servicoAdicionalCliente = getServicoAdicionalClienteService().findById(idServicoAdicionalCliente);
		ServicoAdicionalClienteDTO servicoAdicionalClienteDTO = convertToServicoAdicionalClienteDTO(servicoAdicionalCliente);
		return Response.ok(servicoAdicionalClienteDTO).build();
	}
	
	@GET
	@Path("findTaxaPermanenciaCargaOrTaxaFielDepositario")
	public Map findTaxaPermanenciaCargaOrTaxaFielDepositario(@QueryParam("idParcelaPreco") Long idParcelaPreco) {
		Map criteria = new HashMap<String, Object>();
		criteria.put("idParcelaPreco", idParcelaPreco);
		Boolean isTaxaPermanenciaCargaOrTaxaFielDepositario = getTabelaPrecoParcelaService().findTaxaPermanenciaCargaOrTaxaFielDepositario(criteria);
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("isTaxaPermanenciaCargaOrTaxaFielDepositario", isTaxaPermanenciaCargaOrTaxaFielDepositario);
		return retorno;
	}

	private ServicoAdicionalClienteDTO convertToServicoAdicionalClienteDTO(ServicoAdicionalCliente servicoAdicionalCliente) {
		ServicoAdicionalClienteDTO servicoAdicionalClienteDTO = new ServicoAdicionalClienteDTO();
		servicoAdicionalClienteDTO.setIdServicoAdicionalCliente(servicoAdicionalCliente.getIdServicoAdicionalCliente());
		servicoAdicionalClienteDTO.setBlCobrancaRetroativa(servicoAdicionalCliente.getBlCobrancaRetroativa());
		servicoAdicionalClienteDTO.setNrDecursoPrazo(servicoAdicionalCliente.getNrDecursoPrazo());
		servicoAdicionalClienteDTO.setNrQuantidadeDias(servicoAdicionalCliente.getNrQuantidadeDias());
		Map<String, Object> mapParcelaPreco = new HashMap<String, Object>();
		mapParcelaPreco.put("idParcelaPreco", servicoAdicionalCliente.getParcelaPreco().getIdParcelaPreco().toString());
		servicoAdicionalClienteDTO.setParcelaPreco(mapParcelaPreco);
		Map<String, Object> mapSimulacao = new HashMap<String, Object>();
		mapSimulacao.put("idSimulacao", servicoAdicionalCliente.getSimulacao().getIdSimulacao());
		servicoAdicionalClienteDTO.setSimulacao(mapSimulacao);
		servicoAdicionalClienteDTO.setTpFormaCobranca(servicoAdicionalCliente.getTpFormaCobranca());
		servicoAdicionalClienteDTO.setTpIndicador(servicoAdicionalCliente.getTpIndicador());
		servicoAdicionalClienteDTO.setVlMinimo(servicoAdicionalCliente.getVlMinimo());
		servicoAdicionalClienteDTO.setVlValor(servicoAdicionalCliente.getVlValor());
		servicoAdicionalClienteDTO.setTpUnidMedidaCalcCobr(servicoAdicionalCliente.getTpUnidMedidaCalcCobr());
		return servicoAdicionalClienteDTO;
	}
	
	@POST
	@Path("removeServicoAdicionalClienteById")
	public Response removeServicoAdicionalClienteById(Long id) {
		getServicoAdicionalClienteService().removeByIdProposta(id);
		return Response.ok().build();
	}
	
	@POST
	@Path("findSimulacaoAnexoList")
	public Response findSimulacaoAnexoList(BaseFilterDTO filter) {
		TypedFlatMap criteria = getFiltrosBasicosPaginacaoAbas(filter);
		ResultSetPage<SimulacaoAnexo> rsp = simulacaoAnexoService.findSimulacaoAnexoList(criteria);
		Integer qtRegistros = simulacaoAnexoService.findSimulacaoAnexoRowCount(criteria);
		return getReturnFind(convertToSimulacaoAnexoDTO(rsp.getList()), qtRegistros);
	}
	
	private List<SimulacaoAnexoDTO> convertToSimulacaoAnexoDTO(List<SimulacaoAnexo> list) {
		List<SimulacaoAnexoDTO> anexos = new ArrayList<SimulacaoAnexoDTO>();
		for (SimulacaoAnexo simulacaoAnexo : list) {
			SimulacaoAnexoDTO simulacaoAnexoDTO = new SimulacaoAnexoDTO(simulacaoAnexo.getIdSimulacaoAnexo(), simulacaoAnexo.getSimulacao().getIdSimulacao(), simulacaoAnexo.getDhInclusao(), simulacaoAnexo.getDsAnexo());
			anexos.add(simulacaoAnexoDTO);
		}
		return anexos;
	}
	
	@GET
	@Path("/executeDownloadSimulacaoAnexo")
	public Response executeDownloadSimulacaoAnexo(@QueryParam("idSimulacaoAnexo") Long idSimulacaoAnexo) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("table", "SIMULACAO_ANEXO");
		result.put("blobColumn", "DS_DOCUMENTO");
		result.put("idColumn", "ID_SIMULACAO_ANEXO");
		result.put("id", idSimulacaoAnexo);

		return Response.ok(result).build();
	}
	
	@POST
	@Path("storeSimulacaoAnexo")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response storeSimulacaoAnexo(FormDataMultiPart formDataMultiPart) throws IOException {
		SimulacaoAnexo simulacaoAnexo = getModelFromForm(formDataMultiPart, SimulacaoAnexo.class, "simulacaoAnexo");
		simulacaoAnexo.setDsDocumento(getBinaryBlobUserTypeFromForm(formDataMultiPart, "arquivo"));
		simulacaoAnexoService.store(simulacaoAnexo);
		return Response.ok("Ok").build();
	}

	/**
	 * Realizada exclusão desta forma para manter compatibilidade com tela web antiga já existente.
	 * 
	 * @param ids
	 * @return
	 */
	@POST
	@Path("removeSimulacaoAnexoByIds")
	public Response removeSimulacaoAnexoByIds(List<String> ids) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("ids", ids);
		simulacaoAnexoService.removeSimulacaoAnexo(criteria);
		return Response.ok().build();
	}
	
	@Override
	protected List<PropostaClienteDTO> find(PropostaClienteFiltroDTO filter) {
		TypedFlatMap paginatedFilter = this.getTypedFlatMapWithPaginationInfo(filter);
		ResultSetPage resultado = this.simulacaoService.findPaginatedProc(updateInternalFilterWithFilterData(filter, paginatedFilter));
		List<PropostaClienteDTO> lista = new ArrayList<PropostaClienteDTO>();
		for (Object registro : resultado.getList()) {
			TypedFlatMap simulacao = (TypedFlatMap) registro;
			
			PropostaClienteDTO propostaClienteDTO = new PropostaClienteDTO();
			propostaClienteDTO.setId(simulacao.getLong("idSimulacao"));
			propostaClienteDTO.setIdentificacao(String.format("%s %s", simulacao.getString("filial.sgFilial"), FormatUtils.formatLongWithZeros(simulacao.getLong("nrSimulacao"), "000000")));
			propostaClienteDTO.setDtSimulacao(JTFormatUtils.format(simulacao.getYearMonthDay("dtSimulacao")));
			propostaClienteDTO.setNrSimulacao(simulacao.getLong("nrSimulacao"));
			propostaClienteDTO.setSgFilial(simulacao.getString("filial.sgFilial"));
			propostaClienteDTO.setCliente(new ClienteSuggestDTO(null, simulacao.getString("clienteByIdCliente.pessoa.nmPessoa"), null, null, null, null, null, null, null, null));
			propostaClienteDTO.setTabelaPreco(extraiTabelaPreco(simulacao, false));
			if(StringUtils.isNotBlank(simulacao.getString("tpSituacaoAprovacao.value"))){
				propostaClienteDTO.setTpSituacaoAprovacao(new DomainValue(simulacao.getString("tpSituacaoAprovacao.value"), simulacao.getVarcharI18n("tpSituacaoAprovacao.description"), true));
			}else{
				DomainValue dv = new DomainValue();
				dv.setDescription(new VarcharI18n(""));
				propostaClienteDTO.setTpSituacaoAprovacao(dv);
			}
			
			if(StringUtils.isNotBlank(simulacao.getString("tpGeracaoProposta.value"))){
				propostaClienteDTO.setTpGeracaoProposta(new DomainValue(simulacao.getString("tpGeracaoProposta.value"), simulacao.getVarcharI18n("tpGeracaoProposta.description"), true));
			}else{
				DomainValue dv = new DomainValue();
				dv.setDescription(new VarcharI18n(""));
				propostaClienteDTO.setTpGeracaoProposta(dv);
			}
			
			propostaClienteDTO.setBlEfetivada(simulacao.getBoolean("blEfetivada"));
			propostaClienteDTO.setEfetivada(simulacao.getBoolean("blEfetivada") ? "Sim": "Não");
			propostaClienteDTO.setTabelaPrecoFob(extraiTabelaPreco(simulacao, true));
			
			lista.add(propostaClienteDTO);
		}
		return lista;
	}
	
	private TabelaPrecoSuggestDTO extraiTabelaPreco(TypedFlatMap simulacao, boolean isFob){
		String tabelaPrecoFob = isFob ? TABELA_PRECO_FOB : "";
		String tabelaPrecoString = "";
		String tpTipoTabelaPreco = simulacao.getString(tabelaPrecoFob + "tipoTabelaPreco.tpTipoTabelaPreco.value");
		String tpSubtipoTabelaPreco = simulacao.getString(tabelaPrecoFob + "subtipoTabelaPreco.tpSubtipoTabelaPreco");
		Integer nrVersao = simulacao.getInteger(tabelaPrecoFob + "tipoTabelaPreco.nrVersao");
		if (StringUtils.isNotBlank(tpTipoTabelaPreco)) {
			tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
		}
		
		TabelaPrecoSuggestDTO tabelaPrecoDto = new TabelaPrecoSuggestDTO();
		tabelaPrecoDto.setTpTipoTabelaPreco(tpTipoTabelaPreco);
		tabelaPrecoDto.setNrVersao(nrVersao);
		tabelaPrecoDto.setTpSubtipoTabelaPreco(tpSubtipoTabelaPreco);
		tabelaPrecoDto.setNomeTabela(tabelaPrecoString);
		return tabelaPrecoDto;
	}

	private TypedFlatMap updateInternalFilterWithFilterData(PropostaClienteFiltroDTO filter, TypedFlatMap flatFilter) {
		flatFilter.put("nrSimulacao", filter.getNrProposta());
		flatFilter.put("tabelaPreco.idTabelaPreco", filter.getIdTabelaPreco());
		flatFilter.put("tabelaPrecoFob.idTabelaPreco", filter.getIdTabelaFob());
		flatFilter.put("cliente.idCliente", filter.getIdCliente());
		flatFilter.put("divisaoCliente.idDivisaoCliente", filter.getDivisaoCliente());
		flatFilter.put("servico.idServico", filter.getServico());
		flatFilter.put("filial.idFilial", filter.getIdFilial());
		flatFilter.put("tpSituacaoAprovacao", filter.getValorSituacao());
		flatFilter.put("tpGeracaoProposta", filter.getValorGeracaoProposta());
		flatFilter.put("blNovaUI", Boolean.TRUE);
		return flatFilter;
	}

	@Override
	protected PropostaClienteDTO findById(Long id) {
		TypedFlatMap s = this.simulacaoService.findDadosById(id);
		
		TabelaPrecoSuggestDTO tabelaPrecoDTO = this.obtemTabelaPreco(s);
		
		TabelaPrecoSuggestDTO tabelaPrecoFobDTO = this.obtemTabelaPrecoFob(s);
		
		ClienteSuggestDTO cliente = this.obtemCliente(s);
		
		DivisaoClienteSuggestDTO divisaoCliente = this.obtemDivisaoCliente(s);
		
		ServicoSuggestDTO servico = this.obtemServico(s);
		
		FuncionarioPromotorSuggestDTO promotor = this.obtemPromotorVenda(s);
		
		DomainValue tpPeriodicidadeFaturamento = this.obtemPeriodicidade(s, divisaoCliente);
		
		Short nrDiasPrazoPagamento = this.obtemNrDiasPrazoPagamento(s,divisaoCliente, servico);
		
		FuncionarioPromotorSuggestDTO funcionarioAprovador = this.obtemFuncionarioAprovador(s);
		
		FuncionarioPromotorSuggestDTO funcionarioEfetivador = this.obtemFuncionarioEfetivador(s);
		
		DomainValue tpSituacaoAprovacao = StringUtils.isNotBlank(s.getString("simulacao.tpSituacaoAprovacao.value")) ? new DomainValue(
				s.getString("simulacao.tpSituacaoAprovacao.value"), s.getVarcharI18n("simulacao.tpSituacaoAprovacao.description"), true) : null;

		Boolean blEfetivada = s.getBoolean("blEfetivada");
		
		PropostaClienteDTO propostaClienteDTO = new PropostaClienteDTO();
		propostaClienteDTO.setId(id);
		propostaClienteDTO.setDtSimulacao(JTFormatUtils.format(s.getYearMonthDay("dtSimulacao")));
		propostaClienteDTO.setNrSimulacao(s.getLong("nrSimulacao"));
		propostaClienteDTO.setIdFilial(s.getLong("filial.idFilial"));
		propostaClienteDTO.setSgFilial(s.getString("filial.sgFilial"));
		propostaClienteDTO.setIdFilialSessao(SessionUtils.getFilialSessao().getIdFilial());
		propostaClienteDTO.setTabelaPreco(tabelaPrecoDTO);
		propostaClienteDTO.setTpSituacaoAprovacao(tpSituacaoAprovacao);
		propostaClienteDTO.setBlEfetivada(blEfetivada);
		propostaClienteDTO.setEfetivada(blEfetivada ? "Sim" : "Não");
		propostaClienteDTO.setTabelaPrecoFob(tabelaPrecoFobDTO);
		propostaClienteDTO.setPromotor(promotor);
		propostaClienteDTO.setCliente(cliente);
		propostaClienteDTO.setDivisaoCliente(divisaoCliente);
		propostaClienteDTO.setIdPendenciaAprovacao(s.getLong("idPendenciaAprovacao"));
		propostaClienteDTO.setServico(servico);
		propostaClienteDTO.setTpGeracaoProposta(StringUtils.isNotBlank(s.getString("tpGeracaoProposta")) ? new DomainValue(s.getString("tpGeracaoProposta")) : null);
		propostaClienteDTO.setNrFatorCubagem(s.getBigDecimal("nrFatorCubagem"));
		propostaClienteDTO.setNrFatorDensidade(s.getBigDecimal("nrFatorDensidade"));
		propostaClienteDTO.setTpPeriodicidadeFaturamento(tpPeriodicidadeFaturamento);
		propostaClienteDTO.setNrDiasPrazoPagamento(nrDiasPrazoPagamento);
		propostaClienteDTO.setDtValidadeProposta(s.getYearMonthDay("dtValidadeProposta"));
		propostaClienteDTO.setDtTabelaVigenciaInicial(s.getYearMonthDay("dtTabelaVigenciaInicial"));
		propostaClienteDTO.setDtAceiteCliente(s.getYearMonthDay("dtAceiteCliente"));
		propostaClienteDTO.setDtAprovacao(s.getYearMonthDay("dtAprovacao"));
		propostaClienteDTO.setDtEmissaoTabela(s.getYearMonthDay("dtEmissaoTabela"));
		propostaClienteDTO.setFuncionarioAprovador(funcionarioAprovador);
		propostaClienteDTO.setFuncionarioEfetivador(funcionarioEfetivador);
		propostaClienteDTO.setObProposta(s.getString("obProposta"));
		propostaClienteDTO.setDtEfetivacao(s.getYearMonthDay("dtEfetivacao"));
		propostaClienteDTO.setDhSolicitacao(s.getDateTime("dhSolicitacao"));
		propostaClienteDTO.setDhReprovacao(s.getDateTime("dhReprovacao"));
		propostaClienteDTO.setDsMotivo(s.getString("dsMotivo"));
		
		propostaClienteDTO.setProdutoEspecifico(obtemProdutoEspecifico(s));
		
		/** Regra para desabilitar campos e botoes das telas*/
		Boolean disableAll = isDisableAll(s, blEfetivada);
		
		propostaClienteDTO.setIsFilialMatriz(isFilialMatriz());
		
		propostaClienteDTO.setDisableAll(disableAll);
		propostaClienteDTO.setBotoes(new BotoesPropostaDTO(disableAll));
		propostaClienteDTO.setCampos(new CamposPropostaDTO(disableAll));
		
		return propostaClienteDTO;
	}

	private ProdutoEspecificoDTO obtemProdutoEspecifico(TypedFlatMap s) {
		if (s.get("produtoEspecifico.idProdutoEspecifico") != null){
			ProdutoEspecificoDTO produtoEspecifico = new ProdutoEspecificoDTO();
			produtoEspecifico.setIdProdutoEspecifico((Long)s.get("produtoEspecifico.idProdutoEspecifico"));
			return produtoEspecifico;
		}
		return null;
	}

	private Boolean isDisableAll(TypedFlatMap s, Boolean blEfetivada) {
		Boolean disableAll = false;
		if (Boolean.TRUE.equals(blEfetivada)) {
			disableAll = true;
		} 
		
		Long idFilial = s.getLong("filial.idFilial");
		Long idFilialSessao = SessionUtils.getFilialSessao().getIdFilial();
		if (!idFilialSessao.equals(idFilial)) {
			disableAll = true;
		}
		return disableAll;
	}

	private FuncionarioPromotorSuggestDTO obtemFuncionarioEfetivador(
			TypedFlatMap s) {
		FuncionarioPromotorSuggestDTO funcionarioEfetivador = null;
		String nrMatriculaFuncionarioEfetivador = s.getString("usuarioByIdUsuarioEfetivou.nrMatricula");
		String nmFuncionarioEfetivador = s.getString("usuarioByIdUsuarioEfetivou.nmUsuario");
		if (StringUtils.isNotBlank(nrMatriculaFuncionarioEfetivador)) {
			funcionarioEfetivador = new FuncionarioPromotorSuggestDTO(null, nmFuncionarioEfetivador, nrMatriculaFuncionarioEfetivador);
		}
		return funcionarioEfetivador;
	}

	private FuncionarioPromotorSuggestDTO obtemFuncionarioAprovador(
			TypedFlatMap s) {
		FuncionarioPromotorSuggestDTO funcionarioAprovador = null;
		String nrMatriculaFuncionarioAprovador = s.getString("usuarioByIdUsuarioAprovou.nrMatricula");
		String nmFuncionarioAprovador = s.getString("usuarioByIdUsuarioAprovou.nmUsuario");
		if (StringUtils.isNotBlank(nrMatriculaFuncionarioAprovador)) {
			funcionarioAprovador = new FuncionarioPromotorSuggestDTO(null, nmFuncionarioAprovador, nrMatriculaFuncionarioAprovador);
		}
		return funcionarioAprovador;
	}

	private Short obtemNrDiasPrazoPagamento(TypedFlatMap s, DivisaoClienteSuggestDTO divisaoCliente, ServicoSuggestDTO servico) {
		Short nrDiasPrazoPagamento =  s.getShort("nrDiasPrazoPagamento");
		if (nrDiasPrazoPagamento == null) {
			PrazoVencimento prazoVencimento = prazoVencimentoService.findPrazoVencimento(divisaoCliente.getIdDivisaoCliente(), servico.getTpModal().getValue());
			if (prazoVencimento != null) {
				nrDiasPrazoPagamento = prazoVencimento.getNrPrazoPagamento();
			}
		}
		return nrDiasPrazoPagamento;
	}

	private DomainValue obtemPeriodicidade(TypedFlatMap s, DivisaoClienteSuggestDTO divisaoCliente) {
		DomainValue tpPeriodicidadeFaturamento = null;
		if(StringUtils.isNotBlank(s.getString("simulacao.tpPeriodicidadeFaturamento"))){
			tpPeriodicidadeFaturamento = new DomainValue(s.getString("simulacao.tpPeriodicidadeFaturamento"));
		}
		
		if (tpPeriodicidadeFaturamento == null) {
			// pré condições para obter o dia de faturamento
			if (divisaoCliente.getIdDivisaoCliente() == null) {
				throw new BusinessException("manterPropostaClienteDivisaoClienteNula");
			}
			DomainValue tpModal = StringUtils.isNotBlank(s.getString("servico.tpModal.value")) ? new DomainValue(s.getString("servico.tpModal.value")) : null;
			if (tpModal == null) {
				throw new BusinessException("manterPropostaClienteServicoNulo");
			}
			
			DiaFaturamento diaFaturamento = diaFaturamentoService.findDiaFaturamento(divisaoCliente.getIdDivisaoCliente(), tpModal.getValue());
			if (diaFaturamento != null) {
				tpPeriodicidadeFaturamento = diaFaturamento.getTpPeriodicidade();
			} else {
				Object[] arguments = new Object[] {
						divisaoCliente.getNmDivisaoCliente(),
						tpModal.getDescriptionAsString()
				};
				throw new BusinessException("manterPropostaClienteDiaFaturamentoNaoCadastrado", arguments);
			}
		}
		return tpPeriodicidadeFaturamento;
	}

	private FuncionarioPromotorSuggestDTO obtemPromotorVenda(TypedFlatMap s) {
		FuncionarioPromotorSuggestDTO promotor = null;
		String nrMatriculaPromotor = s.getString("nrMatriculaPromotor");
		String nmPromotor = s.getString("nmPromotor");
		if (StringUtils.isNotBlank(nrMatriculaPromotor)) {
			promotor = new FuncionarioPromotorSuggestDTO(null, nmPromotor, nrMatriculaPromotor);
		}
		return promotor;
	}

	private ServicoSuggestDTO obtemServico(TypedFlatMap s) {
		ServicoSuggestDTO servico = new ServicoSuggestDTO();
		servico.setIdServico(s.getLong("servico.idServico"));
		servico.setNomeServico(s.getString("servico.dsServico"));
		servico.setTpModal(new DomainValue(s.getString("servico.tpModal.value")));
		servico.setTpAbrangencia(new DomainValue(s.getString("servico.tpAbrangencia.value")));
		return servico;
	}

	private DivisaoClienteSuggestDTO obtemDivisaoCliente(TypedFlatMap s) {
		DivisaoClienteSuggestDTO divisaoCliente = new DivisaoClienteSuggestDTO();
		divisaoCliente.setIdDivisaoCliente(s.getLong("divisaoCliente.idDivisaoCliente"));
		divisaoCliente.setNmDivisaoCliente(s.getString("divisaoCliente.dsDivisaoCliente"));
		return divisaoCliente;
	}

	private ClienteSuggestDTO obtemCliente(TypedFlatMap s) {
		ClienteSuggestDTO cliente = new ClienteSuggestDTO();
		cliente.setIdCliente(s.getLong("cliente.idCliente"));
		cliente.setNmPessoa(s.getString("cliente.pessoa.nmPessoa"));
		cliente.setNrIdentificacao(s.getString("cliente.pessoa.nrIdentificacao"));
		return cliente;
	}
	
	private String isFilialMatriz() {
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		BigDecimal filialMtz = (BigDecimal)configuracoesFacade.getValorParametro("ID_EMPRESA_MERCURIO");
		
		if((filialMtz != null && filialUsuarioLogado != null && filialUsuarioLogado.getIdFilial() != null) 
				&& filialMtz.toString().equals(filialUsuarioLogado.getIdFilial().toString())){
			return "S";
		} else {
			return "N";
		}
	}

	private TabelaPrecoSuggestDTO obtemTabelaPrecoFob(TypedFlatMap s) {
		TabelaPrecoSuggestDTO tabelaPrecoFobDTO = null;
		Long idTabelaPrecoFob = s.getLong("tabelaPrecoFob.idTabelaPreco");
		if (idTabelaPrecoFob != null) {
			
			String tpTipoTabelaPrecoFob = s.getString("tabelaPrecoFob.tipoTabelaPreco.tpTipoTabelaPreco.value");
			String tpSubtipoTabelaPrecoFob = s.getString("tabelaPrecoFob.subtipoTabelaPreco.tpSubtipoTabelaPreco");
			Integer nrVersaoFob = s.getInteger("tabelaPrecoFob.tipoTabelaPreco.nrVersao");
			String descricaoFob = s.getString("tabelaPrecoFob.dsDescricao");
			String tabelaPrecoStringFob = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPrecoFob, nrVersaoFob, tpSubtipoTabelaPrecoFob);
			
			tabelaPrecoFobDTO = new TabelaPrecoSuggestDTO();
			tabelaPrecoFobDTO.setIdTabelaPreco(idTabelaPrecoFob);
			tabelaPrecoFobDTO.setTpTipoTabelaPreco(tpTipoTabelaPrecoFob);
			tabelaPrecoFobDTO.setTpSubtipoTabelaPreco(tpSubtipoTabelaPrecoFob);
			tabelaPrecoFobDTO.setNrVersao(nrVersaoFob);
			tabelaPrecoFobDTO.setDescricao(descricaoFob);
			tabelaPrecoFobDTO.setNomeTabela(tabelaPrecoStringFob);
		}
		return tabelaPrecoFobDTO;
	}

	private TabelaPrecoSuggestDTO obtemTabelaPreco(TypedFlatMap s) {
		TabelaPrecoSuggestDTO tabelaPrecoDTO = null;
		Long idTabelaPreco = s.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {

			String tpTipoTabelaPreco = s.getString("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.value");
			String tpSubtipoTabelaPreco = s.getString("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco");
			Integer nrVersao = s.getInteger("tabelaPreco.tipoTabelaPreco.nrVersao");
			String descricao = s.getString("tabelaPreco.dsDescricao");
			String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
			DomainValue tpModal = StringUtils.isNotBlank(s.getString("servico.tpModal.value")) ? new DomainValue(s.getString("servico.tpModal.value")) : null;
			DomainValue tpAbrangencia = StringUtils.isNotBlank(s.getString("servico.tpAbrangencia.value")) ? new DomainValue(s.getString("servico.tpAbrangencia.value")) : null;
		
			tabelaPrecoDTO = new TabelaPrecoSuggestDTO();
			tabelaPrecoDTO.setIdTabelaPreco(idTabelaPreco);
			tabelaPrecoDTO.setTpTipoTabelaPreco(tpTipoTabelaPreco);
			tabelaPrecoDTO.setTpSubtipoTabelaPreco(tpSubtipoTabelaPreco);
			tabelaPrecoDTO.setNrVersao(nrVersao);
			tabelaPrecoDTO.setDescricao(descricao);
			tabelaPrecoDTO.setNomeTabela(tabelaPrecoString);
			tabelaPrecoDTO.setTpModal(tpModal);
			tabelaPrecoDTO.setTpAbrangencia(tpAbrangencia);
			
			MoedaDTO moeda = new MoedaDTO(null, s.getString("tabelaPreco.moeda.sgMoeda"), s.getString("tabelaPreco.moeda.dsSimbolo"));
			tabelaPrecoDTO.setMoeda(moeda);
		}
		return tabelaPrecoDTO;
	}

	@Override
	protected void removeById(Long id) {
		this.simulacaoService.removeById(id);		
	}

	@Override
	protected void removeByIds(List<Long> ids) {
		this.simulacaoService.removeByIds(ids);
	}

	@Override
	protected Long store(PropostaClienteDTO data) {
		boolean isSimulacao = data.getId() != null;

		Simulacao simulacao = null;
		if (isSimulacao) {
			simulacao = this.simulacaoService.findById(data.getId());
			this.simulacaoService.validaUpdateSimulacao(simulacao);
		} else {
			simulacao = new Simulacao();
			simulacao.setBlNovaUI(Boolean.TRUE);
		}
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(data.getCliente().getIdCliente());
		simulacao.setClienteByIdCliente(cliente);
		
		Long idTabelaPrecoOld = null;
		Long idTabelaPrecoNew = null;
		Long idTabelaPreco = data.getTabelaPreco() != null ? data.getTabelaPreco().getIdTabelaPreco() : null;
		if (idTabelaPreco != null) {
			if (isSimulacao && simulacao.getTabelaPreco() != null) {
				idTabelaPrecoOld = simulacao.getTabelaPreco().getIdTabelaPreco();
				if (!idTabelaPrecoOld.equals(idTabelaPreco)) {
					// tem alteracao nas tabelaPrecoParcela
					idTabelaPrecoNew = idTabelaPreco;
				}
			}

			TabelaPreco tabelaPreco = new TabelaPreco();
			tabelaPreco.setIdTabelaPreco(idTabelaPreco);
			simulacao.setTabelaPreco(tabelaPreco);
		} else {
			simulacao.setTabelaPreco(null);
		}
		
		Long idDivisaoCliente = data.getDivisaoCliente() != null ? data.getDivisaoCliente().getIdDivisaoCliente() : null;
		if(idDivisaoCliente != null) {
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(idDivisaoCliente);
			simulacao.setDivisaoCliente(divisaoCliente);
		} else {
			simulacao.setDivisaoCliente(null);
		}
		
		Long idTabelaPrecoFob = data.getTabelaPrecoFob() != null ? data.getTabelaPrecoFob().getIdTabelaPreco() : null;
		if (idTabelaPrecoFob != null) {
			TabelaPreco tabelaPrecoFob = new TabelaPreco();
			tabelaPrecoFob.setIdTabelaPreco(idTabelaPrecoFob);
			simulacao.setTabelaPrecoFob(tabelaPrecoFob);
		} else {
			simulacao.setTabelaPrecoFob(null);
		}

		Servico servico = new Servico();
		servico.setIdServico(data.getServico().getIdServico());
		simulacao.setServico(servico);

		if (!isSimulacao) {
			simulacao.setTpSimulacao(new DomainValue("S"));
			simulacao.setTpRegistro(new DomainValue("P"));
			simulacao.setTpFormaInsercao(new DomainValue("D"));
			simulacao.setDtSimulacao(JTDateTimeUtils.getDataAtual());
			simulacao.setTpIntegranteFrete(new DomainValue("R"));
			simulacao.setTpSituacaoAprovacao(new DomainValue("I"));
			simulacao.setBlCalculoPesoCubado(Boolean.FALSE);

		} else {
			if("completo".equals(data.getSaveMode())) {
				simulacao.setTpPeriodicidadeFaturamento(data.getTpPeriodicidadeFaturamento());
				simulacao.setNrDiasPrazoPagamento(data.getNrDiasPrazoPagamento());
				simulacao.setDtValidadeProposta(data.getDtValidadeProposta());
				simulacao.setDtTabelaVigenciaInicial(data.getDtTabelaVigenciaInicial());
				simulacao.setDtAceiteCliente(data.getDtAceiteCliente());
				simulacao.setDtAprovacao(data.getDtAprovacao());
				simulacao.setObProposta(data.getObProposta());
			}
		}

		simulacao.setUsuarioByIdUsuario(SessionUtils.getUsuarioLogado());
		simulacao.setFilial(SessionUtils.getFilialSessao());
		simulacao.setBlEfetivada(Boolean.FALSE);
		simulacao.setBlPagaFreteTonelada(Boolean.FALSE);// checkbox saiu na implementacao da tela na UI nova
		simulacao.setBlEmiteCargaCompleta(Boolean.FALSE);// checkbox saiu na implementacao da tela na UI nova
		simulacao.setTpGeracaoProposta(data.getTpGeracaoProposta());
		simulacao.setNrFatorCubagem(data.getNrFatorCubagem());
		simulacao.setNrFatorDensidade(data.getNrFatorDensidade());

		if(StringUtils.isNotEmpty(data.getPromotor().getNrMatricula())) {
			Funcionario promotor = new Funcionario();
			promotor.setNrMatricula(FormatUtils.fillNumberWithZero(data.getPromotor().getNrMatricula(), SIZE));
			simulacao.setPromotor(promotor);
		}

		if (data.getProdutoEspecifico() != null && data.getProdutoEspecifico().getIdProdutoEspecifico() != null){
			ProdutoEspecifico produtoEspecifico = new ProdutoEspecifico();
			produtoEspecifico.setIdProdutoEspecifico(data.getProdutoEspecifico().getIdProdutoEspecifico());
			simulacao.setProdutoEspecifico(produtoEspecifico);
		}else{
			simulacao.setProdutoEspecifico(null);
		}
			
		
		/** Store Proposta*/
		this.simulacaoService.storeProposta(simulacao, idTabelaPrecoNew, idTabelaPrecoOld);

		return simulacao.getIdSimulacao();
	}

	public ServicoAdicionalClienteService getServicoAdicionalClienteService() {
		return servicoAdicionalClienteService;
	}

	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}

	public TabelaPrecoParcelaService getTabelaPrecoParcelaService() {
		return tabelaPrecoParcelaService;
	}

	public void setTabelaPrecoParcelaService(TabelaPrecoParcelaService tabelaPrecoParcelaService) {
		this.tabelaPrecoParcelaService = tabelaPrecoParcelaService;
	}

}
