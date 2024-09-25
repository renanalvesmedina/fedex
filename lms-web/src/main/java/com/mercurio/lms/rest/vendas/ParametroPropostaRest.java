package com.mercurio.lms.rest.vendas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.adsm.rest.BaseFilterDTO;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.dto.MunicipioDTO;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.ZonaService;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.TipoLocalizacaoMunicipioDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.PaisDTO;
import com.mercurio.lms.rest.municipios.dto.UnidadeFederativaDTO;
import com.mercurio.lms.rest.municipios.dto.ZonaDTO;
import com.mercurio.lms.rest.tabeladeprecos.GrupoRegiaoDTO;
import com.mercurio.lms.rest.vendas.dto.GeneralidadeClienteDTO;
import com.mercurio.lms.rest.vendas.dto.GeneralidadeClienteFiltroDTO;
import com.mercurio.lms.rest.vendas.dto.GeneralidadeClienteTableDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaFiltroDTO;
import com.mercurio.lms.rest.vendas.dto.ParametroPropostaTableDTO;
import com.mercurio.lms.rest.vendas.dto.TaxaClienteDTO;
import com.mercurio.lms.rest.vendas.dto.TaxaClienteFiltroDTO;
import com.mercurio.lms.rest.vendas.dto.TaxaClienteTableDTO;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoParcelaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.model.service.GeneralidadeClienteService;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.model.service.TaxaClienteService;

@Path("/vendas/parametroProposta")
public class ParametroPropostaRest extends BaseCrudRest<ParametroPropostaDTO, ParametroPropostaDTO, ParametroPropostaFiltroDTO> {
	
	public static final int TP_ROTA_ORIGEM = 1;
	public static final int TP_ROTA_DESTINO = 2;
	private static final DomainValue domainTabela = new DomainValue("T"); 
	private static final DomainValue domainDesconto = new DomainValue("D");
	
	@InjectInJersey
	private SimulacaoService simulacaoService;
	
	@InjectInJersey
	private ParametroClienteService parametroClienteService;
	
	@InjectInJersey
	private TabelaPrecoParcelaService tabelaPrecoParcelaService;
	
	@InjectInJersey
	private TaxaClienteService taxaClienteService;
	
	@InjectInJersey
	private ZonaService zonaService; 
	
	@InjectInJersey
	private PaisService paisService; 
	
	@InjectInJersey
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	
	@InjectInJersey
	private GrupoRegiaoService grupoRegiaoService;
	
	@InjectInJersey
	private ParcelaPrecoService parcelaPrecoService;
	
	@InjectInJersey
	private GeneralidadeClienteService generalidadeClienteService;
	
	@InjectInJersey
	private ParametroGeralService parametroGeralService;
	
	
	private TypedFlatMap getTypedFlatMapWithPaginationInfoAbas(BaseFilterDTO filter) {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("_currentPage", filter.getPagina() == null ? "1" : String.valueOf(filter.getPagina()));
		toReturn.put("_pageSize", filter.getQtRegistrosPagina() == null ? String.valueOf(ROW_LIMIT) : String.valueOf(filter.getQtRegistrosPagina()));
		return toReturn;
	}
	
	@POST
	@Path("findTaxaClienteTable")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response findTaxaClienteTable(TaxaClienteFiltroDTO taxaClienteFiltroDTO) {
		TypedFlatMap criteria = getFiltrosTaxaCliente(taxaClienteFiltroDTO);
		ResultSetPage resultSetPage = taxaClienteService.findPaginatedByParametroClienteProposta(criteria);
		Integer qtRegistros = taxaClienteService.getRowCountByParametroClienteProposta(criteria);
		return getReturnFind(convertToTaxaClienteTableDTO(taxaClienteFiltroDTO, resultSetPage.getList()), qtRegistros);
	}

	
	@POST
	@Path("findGeneralidadeClienteTable")
	public Response findGeneralidadeClienteTable(GeneralidadeClienteFiltroDTO generalidadeClienteFiltroDTO){
		TypedFlatMap criteria = getFiltrosGeneralidadeCliente(generalidadeClienteFiltroDTO);
		ResultSetPage rsp = generalidadeClienteService.findPaginatedByParametroClienteProposta(criteria);
		Integer qtRegistros = generalidadeClienteService.getRowCountByParametroClienteProposta(criteria);
		return getReturnFind(convertToGeneralidadeClienteTableDTO(generalidadeClienteFiltroDTO, rsp.getList()), qtRegistros);
	}
	
	
	private List<TaxaClienteTableDTO> convertToTaxaClienteTableDTO(TaxaClienteFiltroDTO taxaClienteFiltroDTO, List<TaxaCliente> taxasCliente) {
		List<TaxaClienteTableDTO> taxasClienteTableDTO = new ArrayList<TaxaClienteTableDTO>();
		
		for (TaxaCliente taxaCliente : taxasCliente) {
			TaxaClienteTableDTO taxaClienteTableDTO = new TaxaClienteTableDTO();
			taxaClienteTableDTO.setId(taxaCliente.getIdTaxaCliente());
			taxaClienteTableDTO.setIdTaxaCliente(taxaCliente.getIdTaxaCliente());
			
			if(taxaCliente.getParcelaPreco() != null && taxaCliente.getParcelaPreco().getNmParcelaPreco() != null){
				taxaClienteTableDTO.setNmParcelaPreco(taxaCliente.getParcelaPreco().getNmParcelaPreco().getValue());
			}
			
			taxaClienteTableDTO.setTpTaxaIndicador(taxaCliente.getTpTaxaIndicador().getDescriptionAsString());
			taxaClienteTableDTO.setVlTaxa(FormatUtils.formatValorComIndicador(taxaCliente.getTpTaxaIndicador().getValue(), taxaCliente.getVlTaxa(), taxaClienteFiltroDTO.getTabelaPreco().getMoeda().getDsSimbolo(), "DM_INDICADOR_PARAMETRO_CLIENTE"));
			taxaClienteTableDTO.setPsMinimo(taxaCliente.getPsMinimo());
			taxaClienteTableDTO.setVlExcedente(taxaCliente.getVlExcedente());
			
			taxasClienteTableDTO.add(taxaClienteTableDTO);
		}
		
		return taxasClienteTableDTO;
	}
	
	private List<GeneralidadeClienteTableDTO> convertToGeneralidadeClienteTableDTO(GeneralidadeClienteFiltroDTO generalidadeClienteFiltroDTO, List<GeneralidadeCliente> generalidadesCliente) {
		List<GeneralidadeClienteTableDTO> generalidadesClienteTableDTO = new ArrayList<GeneralidadeClienteTableDTO>();

		for (GeneralidadeCliente generalidadeCliente : generalidadesCliente) {
			GeneralidadeClienteTableDTO generalidadeClienteTableDTO = new GeneralidadeClienteTableDTO();
			generalidadeClienteTableDTO.setId(generalidadeCliente.getIdGeneralidadeCliente());
			generalidadeClienteTableDTO.setIdGeneralidadeCliente(generalidadeCliente.getIdGeneralidadeCliente());
			
			if(generalidadeCliente.getParcelaPreco() != null && generalidadeCliente.getParcelaPreco().getNmParcelaPreco() != null){
				generalidadeClienteTableDTO.setNmParcelaPreco(generalidadeCliente.getParcelaPreco().getNmParcelaPreco().getValue());
			}
			
			generalidadeClienteTableDTO.setTpIndicador(generalidadeCliente.getTpIndicador().getDescriptionAsString());
			String dsSimboloMoeda = generalidadeClienteFiltroDTO.getTabelaPreco().getMoeda().getDsSimbolo();
			generalidadeClienteTableDTO.setVlGeneralidade(FormatUtils.formatValorComIndicador(generalidadeCliente.getTpIndicador().getValue(), generalidadeCliente.getVlGeneralidade(), dsSimboloMoeda, "DM_INDICADOR_PARAMETRO_CLIENTE"));
			if (generalidadeCliente.getTpIndicadorMinimo() != null){
				generalidadeClienteTableDTO.setTpIndicadorMinimo(generalidadeCliente.getTpIndicadorMinimo().getDescriptionAsString());
				generalidadeClienteTableDTO.setVlMinimo(FormatUtils.formatValorComIndicador(generalidadeCliente.getTpIndicadorMinimo().getValue(), generalidadeCliente.getVlMinimo(), dsSimboloMoeda, "DM_INDICADOR_PARAMETRO_CLIENTE"));
			}
			
			generalidadesClienteTableDTO.add(generalidadeClienteTableDTO);
		}
		
		return generalidadesClienteTableDTO;
	}

	private TypedFlatMap getFiltrosTaxaCliente(TaxaClienteFiltroDTO taxaClienteFiltroDTO) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfoAbas(taxaClienteFiltroDTO);
		criteria.put("parametroCliente.idParametroCliente", taxaClienteFiltroDTO.getIdParametroCliente());
		return criteria;
	}
	
	private TypedFlatMap getFiltrosGeneralidadeCliente(GeneralidadeClienteFiltroDTO generalidadeClienteFiltroDTO) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfoAbas(generalidadeClienteFiltroDTO);
		criteria.put("parametroCliente.idParametroCliente", generalidadeClienteFiltroDTO.getIdParametroCliente().toString());
		return criteria;
	}
	
	@POST
	@Path("removeTaxaClienteByIds")
	public Response removeTaxaClienteByIds(TaxaClienteFiltroDTO taxaClienteFiltroDTO) {
		Simulacao simulacao = simulacaoService.findById(taxaClienteFiltroDTO.getIdSimulacao()); 
		simulacaoService.validateExclusao(simulacao);
		taxaClienteService.removeByIds(taxaClienteFiltroDTO.getIds());	
		simulacaoService.storePendenciaAprovacaoProposta(simulacao, false);
		return Response.ok().build();
	}
	
	@POST
	@Path("storeTaxaCliente")
	public Response storeTaxaCliente(TaxaClienteDTO taxaClienteDTO) {
		TaxaCliente taxaCliente = convertToTaxaCliente(taxaClienteDTO);
		Simulacao simulacao = simulacaoService.findById(taxaClienteDTO.getIdSimulacao());
		simulacaoService.validaUpdateSimulacao(simulacao);
		simulacao = taxaClienteService.storeProposta(taxaCliente, simulacao.getIdSimulacao());
		taxaClienteDTO.setId(taxaCliente.getIdTaxaCliente());
		return Response.ok(taxaClienteDTO).build();
	}
	
	private TaxaCliente convertToTaxaCliente(TaxaClienteDTO taxaClienteDTO) {
		TaxaCliente taxaCliente = new TaxaCliente();
		taxaCliente.setIdTaxaCliente(taxaClienteDTO.getId());
		
		ParametroCliente parametroCliente = new ParametroCliente();
		parametroCliente.setIdParametroCliente(taxaClienteDTO.getIdParametroCliente());
		taxaCliente.setParametroCliente(parametroCliente);

		ParcelaPreco parcelaPreco = new ParcelaPreco();
		parcelaPreco.setIdParcelaPreco(taxaClienteDTO.getIdParcelaPreco());
		taxaCliente.setParcelaPreco(parcelaPreco);
		
		taxaCliente.setTpTaxaIndicador(taxaClienteDTO.getTpIndicador());
		taxaCliente.setVlTaxa(taxaClienteDTO.getVlValor());
		taxaCliente.setPsMinimo(taxaClienteDTO.getPsMinimo());
		taxaCliente.setVlExcedente(taxaClienteDTO.getVlExcedente());
		taxaCliente.setPcReajTaxa(null);
		taxaCliente.setPcReajVlExcedente(null);
		
		return taxaCliente;
	}

	@POST
	@Path("findTaxaClienteById")
	@SuppressWarnings("rawtypes")
	public Response findTaxaClienteById(@QueryParam("idTaxaCliente") Long idTaxaCliente) {
		Map map = taxaClienteService.findByIdMap(idTaxaCliente);
		TypedFlatMap taxaClienteMap = new TypedFlatMap(map);
		
		TaxaClienteDTO taxaClienteDTO = new TaxaClienteDTO();
		taxaClienteDTO.setId(taxaClienteMap.getLong("idTaxaCliente"));
		taxaClienteDTO.setTpIndicador(new DomainValue((String)((Map)taxaClienteMap.get("tpTaxaIndicador")).get("value")));
		taxaClienteDTO.setVlValor(taxaClienteMap.getBigDecimal("vlTaxa"));
		taxaClienteDTO.setPsMinimo(taxaClienteMap.getBigDecimal("psMinimo"));
		taxaClienteDTO.setVlExcedente(taxaClienteMap.getBigDecimal("vlExcedente"));
		taxaClienteDTO.setIdParcelaPreco((Long)((Map)taxaClienteMap.get("parcelaPreco")).get("idParcelaPreco"));

		return Response.ok(taxaClienteDTO).build();
	}
	
	@POST
	@Path("findComboTaxas")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findComboTaxas(TaxaClienteFiltroDTO taxaClienteFiltroDTO) {
		List<TypedFlatMap> tabelasPrecoParcela = tabelaPrecoParcelaService.findByTpParcelaPrecoTpPrecificacaoIdTabelaPreco("T", "T", taxaClienteFiltroDTO.getTabelaPreco().getIdTabelaPreco());
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("taxas", tabelasPrecoParcela);
		return retorno;
	}

	@POST
	@Path("findComboGeneralidades")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findComboGeneralidades(TaxaClienteFiltroDTO taxaClienteFiltroDTO) {
		
		Long idTabelaPreco = taxaClienteFiltroDTO.getTabelaPreco().getIdTabelaPreco();
    	Long idPedagio = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO);
    	Long idGris = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_GRIS);
    	Long idAdvalorem1 = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_ADVALOREM_1);
    	Long idAdvalorem2 = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_ADVALOREM_2);
    	Long idTde = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_TDE);

    	Long idPedagioDocumento = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_DOCUMENTO);
       	Long idPedagioFaixaPeso = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_FAIXA_PESO);
       	Long idPedagioFracao = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_FRACAO);
       	Long idPedagioPostoFracao = parcelaPrecoService.findIdParcelaByCdParcelaPreco(ConstantesExpedicao.CD_PEDAGIO_POSTO_FRACAO);

    	
       	List parcelasPreco = parcelaPrecoService.findGeneralidadesExcluindoAlgunsTipos(idTabelaPreco, new Long[]{idPedagio,idGris,idAdvalorem1,idAdvalorem2,idTde,idPedagioDocumento,idPedagioFaixaPeso,idPedagioFracao,idPedagioPostoFracao});
		
		
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("generalidades", parcelasPreco);
		return retorno;
	}
	
	@POST
	@Path("findParametrosProposta")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response findParametrosProposta(ParametroPropostaFiltroDTO parametroPropostaFiltroDTO) {
		TypedFlatMap criteria = getFiltrosParametroProposta(parametroPropostaFiltroDTO);
		ResultSetPage resultSetPage = simulacaoService.findPaginatedParametros(criteria);
		Integer qtRegistros = simulacaoService.getRowCountParametros(criteria);
		return getReturnFind(convertToParametroPropostaTableDTO(resultSetPage.getList()), qtRegistros);
	}

	private List<ParametroPropostaTableDTO> convertToParametroPropostaTableDTO(List<TypedFlatMap> list) {
		List<ParametroPropostaTableDTO> parametrosProposta = new ArrayList<ParametroPropostaTableDTO>();
		
		for (TypedFlatMap parametroPropostaMap : list) {
			ParametroPropostaTableDTO parametroPropostaTableDTO = new ParametroPropostaTableDTO();
			parametroPropostaTableDTO.setId(parametroPropostaMap.getLong("idParametroCliente"));
			parametroPropostaTableDTO.setIdParametroCliente(parametroPropostaMap.getLong("idParametroCliente"));
			
			if(parametroPropostaMap.getVarcharI18n("zonaByIdZonaOrigem.dsZona") != null){
				parametroPropostaTableDTO.setDsZonaOrigem(parametroPropostaMap.getVarcharI18n("zonaByIdZonaOrigem.dsZona").getValue());
			}
			
			if(parametroPropostaMap.getVarcharI18n("paisByIdPaisOrigem.nmPais") != null){
				parametroPropostaTableDTO.setNmPaisOrigem(parametroPropostaMap.getVarcharI18n("paisByIdPaisOrigem.nmPais").getValue());
			}
			
			parametroPropostaTableDTO.setSgUnidadeFederativaOrigem(parametroPropostaMap.getString("unidadeFederativaByIdUfOrigem.sgUnidadeFederativa"));
			parametroPropostaTableDTO.setSgFilialOrigem(parametroPropostaMap.getString("filialByIdFilialOrigem.sgFilial"));
			parametroPropostaTableDTO.setNmMunicipioOrigem(parametroPropostaMap.getString("municipioByIdMunicipioOrigem.nmMunicipio"));
			parametroPropostaTableDTO.setSgAeroportoOrigem(parametroPropostaMap.getString("aeroportoByIdAeroportoOrigem.sgAeroporto"));
			
			if(parametroPropostaMap.getVarcharI18n("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio") != null){
				parametroPropostaTableDTO.setDsTipoLocalizacaoMunicipioOrigem(parametroPropostaMap.getVarcharI18n("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio").getValue());
			}
			
			parametroPropostaTableDTO.setDsGrupoRegiaoOrigem(parametroPropostaMap.getString("grupoRegiaoOrigem.dsGrupoRegiao"));
			
			
			if(parametroPropostaMap.getVarcharI18n("zonaByIdZonaDestino.dsZona") != null){
				parametroPropostaTableDTO.setDsZonaDestino(parametroPropostaMap.getVarcharI18n("zonaByIdZonaDestino.dsZona").getValue());
			}
			
			if(parametroPropostaMap.getVarcharI18n("paisByIdPaisDestino.nmPais") != null){
				parametroPropostaTableDTO.setNmPaisDestino(parametroPropostaMap.getVarcharI18n("paisByIdPaisDestino.nmPais").getValue());
			}
			
			parametroPropostaTableDTO.setSgUnidadeFederativaDestino(parametroPropostaMap.getString("unidadeFederativaByIdUfDestino.sgUnidadeFederativa"));
			parametroPropostaTableDTO.setSgFilialDestino(parametroPropostaMap.getString("filialByIdFilialDestino.sgFilial"));
			parametroPropostaTableDTO.setNmMunicipioDestino(parametroPropostaMap.getString("municipioByIdMunicipioDestino.nmMunicipio"));
			parametroPropostaTableDTO.setSgAeroportoDestino(parametroPropostaMap.getString("aeroportoByIdAeroportoDestino.sgAeroporto"));
			
			if(parametroPropostaMap.getVarcharI18n("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio") != null){
				parametroPropostaTableDTO.setDsTipoLocalizacaoMunicipioDestino(parametroPropostaMap.getVarcharI18n("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio").getValue());
			}
			
			parametroPropostaTableDTO.setDsGrupoRegiaoDestino(parametroPropostaMap.getString("grupoRegiaoDestino.dsGrupoRegiao"));
			
			parametrosProposta.add(parametroPropostaTableDTO);
		}
		
		return parametrosProposta;
	}
	
	private TypedFlatMap getFiltrosParametroProposta(ParametroPropostaFiltroDTO parametroPropostaFiltroDTO) {
		TypedFlatMap criteria = getTypedFlatMapWithPaginationInfo(parametroPropostaFiltroDTO);
		criteria.put("simulacao.idSimulacao", parametroPropostaFiltroDTO.getIdSimulacao());
		criteria.put("tabelaPreco.idTabelaPreco", parametroPropostaFiltroDTO.getTabelaPreco().getIdTabelaPreco());
		criteria.put("servico.idServico", parametroPropostaFiltroDTO.getServico().getIdServico());
		criteria.put("divisaoCliente.idDivisaoCliente", parametroPropostaFiltroDTO.getDivisaoCliente().getIdDivisaoCliente());
		criteria.put("tpGeracaoProposta", parametroPropostaFiltroDTO.getTpGeracaoProposta().getValue());
		return criteria;
	}
	
	@POST
	@Path("removeParametrosPropostaByIds")
	public Response removeParametrosPropostaByIds(ParametroPropostaFiltroDTO parametroPropostaFiltroDTO) {
		parametroClienteService.removeByIdsProposta(parametroPropostaFiltroDTO.getIds(), parametroPropostaFiltroDTO.getIdSimulacao());
		return Response.ok().build();
	}

	/**
	 * Não deve ser utilizado utilizado, pois foi implementado de outra forma para poder utilizar um DTO específico para a table.
	 */
	@Override
	protected Integer count(ParametroPropostaFiltroDTO arg0) {
		throw new InfrastructureException("errosistema");
	}

	/**
	 * Não deve ser utilizado utilizado, pois foi implementado de outra forma para poder utilizar um DTO específico para a table.
	 */
	@Override
	protected List<ParametroPropostaDTO> find(ParametroPropostaFiltroDTO arg0) {
		throw new InfrastructureException("errosistema");
	}

	@Override
	protected ParametroPropostaDTO findById(Long idParametroCliente) {
		
		ParametroCliente parametroCliente = parametroClienteService.findByIdParametro(idParametroCliente);
		
		ParametroPropostaDTO parametroPropostaDTO = new ParametroPropostaDTO();
		parametroPropostaDTO.setId(idParametroCliente);
		
		popularDadosAbaRota(parametroCliente, parametroPropostaDTO);
		popularDadosAbaParametroProposta(parametroCliente, parametroPropostaDTO);
		
		return parametroPropostaDTO;
	}
	
	private void popularDadosAbaRota(ParametroCliente parametroCliente, ParametroPropostaDTO parametroPropostaDTO) {
		//Rota origem
		parametroPropostaDTO.setRotaOrigemZona(buildZonaDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemPais(buildPaisDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemUf(buildUFDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemMunicipio(buildMunicipioDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemTipoLocalizacao(buildTipoLocalizacaoDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemFilial(buildFilialDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemAeroporto(buildAeroportoDTO(parametroCliente,TP_ROTA_ORIGEM));
		parametroPropostaDTO.setRotaOrigemGrupoRegiao(buildGrupoRegiaoDTO(parametroCliente,TP_ROTA_ORIGEM));
		
		//Rota destino
		parametroPropostaDTO.setRotaDestinoZona(buildZonaDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoPais(buildPaisDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoUf(buildUFDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoMunicipio(buildMunicipioDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoTipoLocalizacao(buildTipoLocalizacaoDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoGrupoRegiao(buildGrupoRegiaoDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoFilial(buildFilialDTO(parametroCliente,TP_ROTA_DESTINO));
		parametroPropostaDTO.setRotaDestinoAeroporto(buildAeroportoDTO(parametroCliente,TP_ROTA_DESTINO));
		
		parametroPropostaDTO.setDsRotaOrigem(buildDsRotaOrigem(parametroPropostaDTO));
		parametroPropostaDTO.setDsRotaDestino(buildDsRotaDestino(parametroPropostaDTO));
		
	}
	
	private void popularDadosAbaParametroProposta(ParametroCliente parametroCliente, ParametroPropostaDTO parametroPropostaDTO){
		//aba parametro
		parametroPropostaDTO.setDsEspecificacaoRota(parametroCliente.getDsEspecificacaoRota());
		
//		FRETE PESO
		parametroPropostaDTO.setTpIndicadorMinFretePeso(parametroCliente.getTpIndicadorMinFretePeso());
		parametroPropostaDTO.setVlMinFretePeso(parametroCliente.getVlMinFretePeso());
		parametroPropostaDTO.setTpIndicadorPercMinimoProgr(parametroCliente.getTpIndicadorPercMinimoProgr());
		parametroPropostaDTO.setVlPercMinimoProgr(parametroCliente.getVlPercMinimoProgr());
		parametroPropostaDTO.setTpIndicadorFretePeso(parametroCliente.getTpIndicadorFretePeso());
		parametroPropostaDTO.setVlFretePeso(parametroCliente.getVlFretePeso());
		parametroPropostaDTO.setVlMinimoFreteQuilo(parametroCliente.getVlMinimoFreteQuilo());
		parametroPropostaDTO.setBlPagaPesoExcedente(parametroCliente.getBlPagaPesoExcedente());
		parametroPropostaDTO.setTpTarifaMinima(parametroCliente.getTpTarifaMinima());
		parametroPropostaDTO.setVlTarifaMinima(parametroCliente.getVlTarifaMinima());
		parametroPropostaDTO.setVlFreteVolume(parametroCliente.getVlFreteVolume());
		parametroPropostaDTO.setTpIndicVlrTblEspecifica(parametroCliente.getTpIndicVlrTblEspecifica());
		parametroPropostaDTO.setVlTblEspecifica(parametroCliente.getVlTblEspecifica());
		
//		FRETE VALOR
		parametroPropostaDTO.setTpIndicadorAdvalorem(parametroCliente.getTpIndicadorAdvalorem());
		parametroPropostaDTO.setVlAdvalorem(parametroCliente.getVlAdvalorem());
		parametroPropostaDTO.setTpIndicadorAdvalorem2(parametroCliente.getTpIndicadorAdvalorem2());
		parametroPropostaDTO.setVlAdvalorem2(parametroCliente.getVlAdvalorem2());
		parametroPropostaDTO.setTpIndicadorValorReferencia(parametroCliente.getTpIndicadorValorReferencia());
		parametroPropostaDTO.setVlValorReferencia(parametroCliente.getVlValorReferencia());
		
//		FRETE PERCENTUAL
		parametroPropostaDTO.setPcFretePercentual(parametroCliente.getPcFretePercentual());
		parametroPropostaDTO.setVlMinimoFretePercentual(parametroCliente.getVlMinimoFretePercentual());
		parametroPropostaDTO.setVlToneladaFretePercentual(parametroCliente.getVlToneladaFretePercentual());
		parametroPropostaDTO.setPsFretePercentual(parametroCliente.getPsFretePercentual());
		
//		GRIS
		parametroPropostaDTO.setTpIndicadorPercentualGris(parametroCliente.getTpIndicadorPercentualGris());
		parametroPropostaDTO.setVlPercentualGris(parametroCliente.getVlPercentualGris());
		parametroPropostaDTO.setTpIndicadorMinimoGris(parametroCliente.getTpIndicadorMinimoGris());
		parametroPropostaDTO.setVlMinimoGris(parametroCliente.getVlMinimoGris());
		
//		PEDAGIO
		parametroPropostaDTO.setTpIndicadorPedagio(parametroCliente.getTpIndicadorPedagio());
		parametroPropostaDTO.setVlPedagio(parametroCliente.getVlPedagio());
		
//		TRT
		parametroPropostaDTO.setTpIndicadorPercentualTrt(parametroCliente.getTpIndicadorPercentualTrt());
		parametroPropostaDTO.setVlPercentualTrt(parametroCliente.getVlPercentualTrt());
		parametroPropostaDTO.setTpIndicadorMinimoTrt(parametroCliente.getTpIndicadorMinimoTrt());
		parametroPropostaDTO.setVlMinimoTrt(parametroCliente.getVlMinimoTrt());
		
//		TDE
		parametroPropostaDTO.setTpIndicadorPercentualTde(parametroCliente.getTpIndicadorPercentualTde());
		parametroPropostaDTO.setVlPercentualTde(parametroCliente.getVlPercentualTde());
		parametroPropostaDTO.setTpIndicadorMinimoTde(parametroCliente.getTpIndicadorMinimoTde());
		parametroPropostaDTO.setVlMinimoTde(parametroCliente.getVlMinimoTde());
		
//		TOTAL FRETE
		parametroPropostaDTO.setPcDescontoFreteTotal(parametroCliente.getPcDescontoFreteTotal());
		parametroPropostaDTO.setPcCobrancaReentrega(parametroCliente.getPcCobrancaReentrega());
		parametroPropostaDTO.setPcCobrancaDevolucoes(parametroCliente.getPcCobrancaDevolucoes());
		
	}
	
	private String buildDsRotaOrigem(ParametroPropostaDTO dto){
		String rota = "";
		if (dto.getRotaOrigemZona() != null){
			rota =  addItemRotaString(rota, dto.getRotaOrigemZona().getDsZona());
		}
		if (dto.getRotaOrigemPais() != null){
			rota = addItemRotaString(rota, dto.getRotaOrigemPais().getNmPais());
		}
		if (dto.getRotaOrigemUf() != null){
			rota = addItemRotaString(rota, dto.getRotaOrigemUf().getSgUnidadeFederativa());
		}
		if (dto.getRotaOrigemFilial() != null){
			rota = addItemRotaString(rota, dto.getRotaOrigemFilial().getNmFilial());
		}

		if (dto.getRotaOrigemMunicipio() != null){
			rota = addItemRotaString(rota, dto.getRotaOrigemMunicipio().getNmMunicipio());
		}

		if (dto.getRotaOrigemAeroporto() != null){
			rota = addItemRotaString(rota, dto.getRotaOrigemAeroporto().getNmAeroporto());
		}
		
		if (dto.getRotaOrigemTipoLocalizacao() != null){
			rota = addItemRotaString(rota, dto.getRotaOrigemTipoLocalizacao().getDsTipoLocalizacaoMunicipio());
		}
		
		return rota;
	}
	
	private String buildDsRotaDestino(ParametroPropostaDTO dto){
		String rota = "";
		if (dto.getRotaDestinoZona() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoZona().getDsZona());
		}
		if (dto.getRotaDestinoPais() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoPais().getNmPais());
		}
		if (dto.getRotaDestinoUf() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoUf().getSgUnidadeFederativa());
		}
		if (dto.getRotaDestinoFilial() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoFilial().getNmFilial());
		}

		if (dto.getRotaDestinoMunicipio() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoMunicipio().getNmMunicipio());
		}

		if (dto.getRotaDestinoAeroporto() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoAeroporto().getNmAeroporto());
		}
		
		if (dto.getRotaDestinoTipoLocalizacao() != null){
			rota = addItemRotaString(rota, dto.getRotaDestinoTipoLocalizacao().getDsTipoLocalizacaoMunicipio());
		}
		
		return rota;
	}
	
	
	private String addItemRotaString(String rota, String itemDescription){
		if (rota == null || itemDescription == null || itemDescription.trim().length() == 0){
			return null;
		}
		
		if (rota.trim().length() != 0){
			rota = rota.concat(" - ");
		}	
		
		return rota.concat(itemDescription); 
	}

	private TipoLocalizacaoMunicipioDTO buildTipoLocalizacaoDTO(ParametroCliente parametroCliente, int tpRota) {
		TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio = null;
		if (TP_ROTA_ORIGEM == tpRota){
			tipoLocalizacaoMunicipio = parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem();
		}else{
			tipoLocalizacaoMunicipio = parametroCliente.getTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino();
		}
		
		TipoLocalizacaoMunicipioDTO dto =null;
		if (tipoLocalizacaoMunicipio != null){
			dto = new TipoLocalizacaoMunicipioDTO(tipoLocalizacaoMunicipio.getIdTipoLocalizacaoMunicipio(), tipoLocalizacaoMunicipio.getDsTipoLocalizacaoMunicipio().getValue());
		}
		return dto;
	}
	
	private AeroportoSuggestDTO buildAeroportoDTO(ParametroCliente parametroCliente, int tpRota) {
		Aeroporto aeroporto = null;
		if (TP_ROTA_ORIGEM == tpRota){
			aeroporto = parametroCliente.getAeroportoByIdAeroportoOrigem();
		}else{
			aeroporto = parametroCliente.getAeroportoByIdAeroportoDestino();
		}
		
		AeroportoSuggestDTO dto =null;
		if (aeroporto != null){
			dto = new AeroportoSuggestDTO(aeroporto.getIdAeroporto(), aeroporto.getSgAeroporto(), aeroporto.getPessoa().getNmPessoa());
		}
		return dto;
	}

	private GrupoRegiaoDTO buildGrupoRegiaoDTO(
			ParametroCliente parametroCliente, int tpRotaOrigem) {
		GrupoRegiao grupoRegiao = null;
		if (TP_ROTA_ORIGEM == tpRotaOrigem){
			grupoRegiao = parametroCliente.getGrupoRegiaoOrigem();
		}else {
			grupoRegiao = parametroCliente.getGrupoRegiaoDestino();
		}
		GrupoRegiaoDTO dto = null;
		if (grupoRegiao!=null){
			dto=new GrupoRegiaoDTO(grupoRegiao.getIdGrupoRegiao(), grupoRegiao.getDsGrupoRegiao());
		}
		return dto;
	}
	
	
	private FilialSuggestDTO buildFilialDTO(ParametroCliente parametroCliente,
			int tpRota) {
		
		Filial filial = null;
		if (tpRota == TP_ROTA_ORIGEM){
			filial = parametroCliente.getFilialByIdFilialOrigem();
		}else{
			filial = parametroCliente.getFilialByIdFilialDestino();
		}
		FilialSuggestDTO dto = null;
		if (filial != null){
			dto = new FilialSuggestDTO(filial.getIdFilial(), filial.getPessoa().getNmFantasia(), filial.getSgFilial(), null, null);
		}
		return dto;
	}

	private MunicipioDTO buildMunicipioDTO(ParametroCliente parametroCliente, int tpRota) {
		Municipio municipio = null;
		if (TP_ROTA_ORIGEM == tpRota){
			municipio = parametroCliente.getMunicipioByIdMunicipioOrigem();
		}else{
			municipio = parametroCliente.getMunicipioByIdMunicipioDestino();
		}
		MunicipioDTO dto = null;
		if (municipio != null){
			dto = new MunicipioDTO(municipio.getIdMunicipio(),municipio.getNmMunicipio());
		}
		return dto;
	}

	private UnidadeFederativaDTO buildUFDTO(ParametroCliente parametroCliente,int tpRota) {
		UnidadeFederativa unidadeFederativa = null;
		if (TP_ROTA_ORIGEM == tpRota){
			unidadeFederativa = parametroCliente.getUnidadeFederativaByIdUfOrigem();
		}else{
			unidadeFederativa = parametroCliente.getUnidadeFederativaByIdUfDestino();
		}
		
		UnidadeFederativaDTO dto = null;
		if (unidadeFederativa != null){
			dto = new UnidadeFederativaDTO(unidadeFederativa.getIdUnidadeFederativa(),unidadeFederativa.getSgUnidadeFederativa(),unidadeFederativa.getNmUnidadeFederativa());
		}
		return dto;	
	}
	

	private PaisDTO buildPaisDTO(ParametroCliente parametroCliente, int tpRota) {
		Pais pais = null;
		if(TP_ROTA_ORIGEM == tpRota){
			pais = parametroCliente.getPaisByIdPaisOrigem();
		}else{
			pais = parametroCliente.getPaisByIdPaisDestino();
		}
		
		PaisDTO dto = null;
		if (pais!=null){
			dto = new PaisDTO(pais.getIdPais(), pais.getCdIso(),pais.getSgPais(),pais.getNmPais().getValue());
		}
		return dto;
	}

	private ZonaDTO buildZonaDTO(ParametroCliente parametroCliente,int tpRota) {
		Zona zona = null;
		if (TP_ROTA_ORIGEM == tpRota){
			zona = parametroCliente.getZonaByIdZonaOrigem();
		}else{
			zona = parametroCliente.getZonaByIdZonaDestino();
		}
		
		ZonaDTO dto = null;
		if (zona != null){
			dto = new ZonaDTO(zona.getIdZona(),zona.getDsZona().getValue());
		}
		return dto;
	}

	@Override
	protected void removeById(Long arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeByIds(List<Long> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected Long store(ParametroPropostaDTO arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@POST
	@Path("/ajustaDsRota")
	public Response ajustaDsRota(ParametroPropostaDTO parametroPropostaDTO) {
		
		parametroPropostaDTO.setDsRotaOrigem(buildDsRotaOrigem(parametroPropostaDTO));
		parametroPropostaDTO.setDsRotaDestino(buildDsRotaDestino(parametroPropostaDTO));
		
		return Response.ok(parametroPropostaDTO).build();
	}
	
	@POST
	@Path("loadDadosDefault")
	public Response loadDadosDefault(Map<String, Object> criteria) {
		ParametroPropostaDTO parametroPropostaDTO = new ParametroPropostaDTO();
		
		ajustaValoresDefaultAbaRota(parametroPropostaDTO);
		ajustaValoresDefaultAbaParametro(parametroPropostaDTO);
		
		return Response.ok(parametroPropostaDTO).build();	
	}
	
	private void ajustaValoresDefaultAbaRota(ParametroPropostaDTO parametroPropostaDTO) { 
		Map paisMap = paisService.findPaisUsuarioLogado();
		Pais pais = paisService.findById((Long)paisMap.get("idPais"));
		
		PaisDTO paisDTO = new PaisDTO(pais.getIdPais(), pais.getCdIso(), pais.getSgPais(), pais.getNmPais().getValue());
		ZonaDTO zonaDTO = new ZonaDTO(pais.getZona().getIdZona(), pais.getZona().getDsZona().getValue());
		
		parametroPropostaDTO.setRotaOrigemPais(paisDTO);
		parametroPropostaDTO.setRotaDestinoPais(paisDTO);
		parametroPropostaDTO.setRotaOrigemZona(zonaDTO);
		parametroPropostaDTO.setRotaDestinoZona(zonaDTO);
		parametroPropostaDTO.setDsRotaOrigem(buildDsRotaOrigem(parametroPropostaDTO));
		parametroPropostaDTO.setDsRotaDestino(buildDsRotaDestino(parametroPropostaDTO));
	}
	
	private void ajustaValoresDefaultAbaParametro(ParametroPropostaDTO parametroPropostaDTO) { 
		parametroPropostaDTO.setDsEspecificacaoRota(""); 
		parametroPropostaDTO.setTpIndicadorMinFretePeso(domainTabela); 
		parametroPropostaDTO.setTpIndicadorFretePeso(domainTabela); 
		parametroPropostaDTO.setTpTarifaMinima(domainTabela); 
		parametroPropostaDTO.setTpIndicVlrTblEspecifica(domainTabela); 
		parametroPropostaDTO.setTpIndicadorAdvalorem(domainTabela); 
		parametroPropostaDTO.setTpIndicadorAdvalorem2(domainTabela); 
		parametroPropostaDTO.setTpIndicadorValorReferencia(domainTabela); 
		parametroPropostaDTO.setTpIndicadorPercentualGris(domainTabela); 
		parametroPropostaDTO.setTpIndicadorMinimoGris(domainTabela); 
		parametroPropostaDTO.setTpIndicadorPercentualTrt(domainTabela); 
		parametroPropostaDTO.setTpIndicadorMinimoTrt(domainTabela);
		parametroPropostaDTO.setTpIndicadorPercentualTde(domainTabela);
		parametroPropostaDTO.setTpIndicadorMinimoTde(domainTabela);
		parametroPropostaDTO.setTpIndicadorPedagio(domainTabela); 
		parametroPropostaDTO.setTpIndicadorPercMinimoProgr(domainDesconto); 
		parametroPropostaDTO.setVlMinFretePeso(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlPercMinimoProgr(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlFretePeso(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlMinimoFreteQuilo(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlFreteVolume(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlTarifaMinima(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlTblEspecifica(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlAdvalorem(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlAdvalorem2(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlValorReferencia(BigDecimal.ZERO); 
		parametroPropostaDTO.setPcFretePercentual(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlMinimoFretePercentual(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlToneladaFretePercentual(BigDecimal.ZERO); 
		parametroPropostaDTO.setPsFretePercentual(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlPercentualGris(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlMinimoGris(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlPercentualTrt(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlMinimoTrt(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlPercentualTde(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlMinimoTde(BigDecimal.ZERO); 
		parametroPropostaDTO.setVlPedagio(BigDecimal.ZERO); 
		parametroPropostaDTO.setPcDescontoFreteTotal(BigDecimal.ZERO); 
		BigDecimal pcr = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("PercentualCobrancaReentrega",false); 
		parametroPropostaDTO.setPcCobrancaReentrega(pcr); 
		BigDecimal pcd = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("PercentualCobrancaDevolucao",false); 
		parametroPropostaDTO.setPcCobrancaDevolucoes(pcd); 
	}
	
	@POST
	@Path("findZonasCombo")
	public Response findZonasCombo(Map<String, Object> criteria) {
		List<Zona> entidades = zonaService.find(criteria);
		List<ZonaDTO> zonas = new ArrayList<ZonaDTO>();
		for (Zona zona : entidades) {
			zonas.add(new ZonaDTO(zona.getIdZona(), zona.getDsZona().getValue()));
		}
		return Response.ok(zonas).build();
	}
	
	@POST
	@Path("findTiposLocalizacaoCombo")
	public Response findTiposLocalizacaoCombo(Map<String, Object> criteria) {
		criteria = new HashMap<String, Object>();
		
		criteria.put("tpLocalizacao", "O");
		List<TipoLocalizacaoMunicipio> entidades = tipoLocalizacaoMunicipioService.find(criteria);
		List<TipoLocalizacaoMunicipioDTO> tiposLocalizacao = new ArrayList<TipoLocalizacaoMunicipioDTO>();
		for (TipoLocalizacaoMunicipio tipoLocalizacaoMunicipio : entidades) {
			tiposLocalizacao.add(new TipoLocalizacaoMunicipioDTO(tipoLocalizacaoMunicipio.getIdTipoLocalizacaoMunicipio(), tipoLocalizacaoMunicipio.getDsTipoLocalizacaoMunicipio().getValue()));
		}
		return Response.ok(tiposLocalizacao).build();
	}
	
	
	@POST
	@Path("findGruposRegiaoCombo")
	public Response findGruposRegiaoCombo(Map<String, Object> criteria) {
		criteria.put("unidadeFederativa.idUnidadeFederativa", criteria.get("idUnidadeFederativa"));
		criteria.put("tabelaPreco.idTabelaPreco", criteria.get("idTabelaPreco"));
		List<GrupoRegiao> entidades = grupoRegiaoService.find(criteria);
		List<GrupoRegiaoDTO> gruposRegiao = new ArrayList<GrupoRegiaoDTO>();
		for (GrupoRegiao grupoRegiao: entidades) {
			gruposRegiao.add(new GrupoRegiaoDTO(grupoRegiao.getIdGrupoRegiao(), grupoRegiao.getDsGrupoRegiao()));
		}
		return Response.ok(gruposRegiao).build();
	}
	
	@POST
	@Path("/storeParam")
	public Response storeParam(ParametroPropostaDTO parametroPropostaDTO) {
		
		Simulacao simulacao = simulacaoService.findById(parametroPropostaDTO.getIdSimulacao());
		
		ParametroCliente pc = new ParametroCliente();
		Long idParametroCliente = parametroPropostaDTO.getId();
		if (LongUtils.hasValue(idParametroCliente)) {
			pc = parametroClienteService.findById(idParametroCliente);
		}
		pc.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		pc.setDtVigenciaFinal(null);
		pc.setTpIndicadorPercentualGris(parametroPropostaDTO.getTpIndicadorPercentualGris());
		pc.setVlPercentualGris(parametroPropostaDTO.getVlPercentualGris());
		pc.setTpIndicadorMinimoGris(parametroPropostaDTO.getTpIndicadorMinimoGris());
		pc.setVlMinimoGris(parametroPropostaDTO.getVlMinimoGris());
		pc.setTpIndicadorPercentualTrt(parametroPropostaDTO.getTpIndicadorPercentualTrt());
		pc.setVlPercentualTrt(parametroPropostaDTO.getVlPercentualTrt());
		pc.setTpIndicadorMinimoTrt(parametroPropostaDTO.getTpIndicadorMinimoTrt());
		pc.setVlMinimoTrt(parametroPropostaDTO.getVlMinimoTrt());
		pc.setTpIndicadorPedagio(parametroPropostaDTO.getTpIndicadorPedagio());
		pc.setVlPedagio(parametroPropostaDTO.getVlPedagio());
		pc.setTpIndicadorMinFretePeso(parametroPropostaDTO.getTpIndicadorMinFretePeso());
		pc.setVlMinFretePeso(parametroPropostaDTO.getVlMinFretePeso());
		pc.setTpIndicadorPercMinimoProgr(parametroPropostaDTO.getTpIndicadorPercMinimoProgr());
		pc.setVlPercMinimoProgr(parametroPropostaDTO.getVlPercMinimoProgr());
		pc.setTpIndicadorFretePeso(parametroPropostaDTO.getTpIndicadorFretePeso());
		pc.setVlFretePeso(parametroPropostaDTO.getVlFretePeso());
		pc.setTpIndicadorAdvalorem(parametroPropostaDTO.getTpIndicadorAdvalorem());
		pc.setVlAdvalorem(parametroPropostaDTO.getVlAdvalorem());
		pc.setTpIndicadorAdvalorem2(parametroPropostaDTO.getTpIndicadorAdvalorem2());
		pc.setVlAdvalorem2(parametroPropostaDTO.getVlAdvalorem2());
		pc.setTpIndicadorValorReferencia(parametroPropostaDTO.getTpIndicadorValorReferencia());
		pc.setVlValorReferencia(parametroPropostaDTO.getVlValorReferencia());
		pc.setVlMinimoFreteQuilo(parametroPropostaDTO.getVlMinimoFreteQuilo());
		pc.setPcFretePercentual(parametroPropostaDTO.getPcFretePercentual());
		pc.setVlMinimoFretePercentual(parametroPropostaDTO.getVlMinimoFretePercentual());
		pc.setVlToneladaFretePercentual(parametroPropostaDTO.getVlToneladaFretePercentual());
		pc.setPsFretePercentual(parametroPropostaDTO.getPsFretePercentual());
		pc.setPcDescontoFreteTotal(parametroPropostaDTO.getPcDescontoFreteTotal());
		pc.setTpIndicVlrTblEspecifica(parametroPropostaDTO.getTpIndicVlrTblEspecifica());
		pc.setVlTblEspecifica(parametroPropostaDTO.getVlTblEspecifica());
		pc.setVlFreteVolume(parametroPropostaDTO.getVlFreteVolume());
		
		/*Solicitacao para o branch RPP - 01.04.01.07*/
		pc.setBlPagaCubagem(Boolean.TRUE);
		pc.setPcPagaCubagem(BigDecimalUtils.HUNDRED);
		
		pc.setBlPagaPesoExcedente(parametroPropostaDTO.isBlPagaPesoExcedente());
		pc.setTpTarifaMinima(parametroPropostaDTO.getTpTarifaMinima());
		pc.setVlTarifaMinima(parametroPropostaDTO.getVlTarifaMinima());
		pc.setPcCobrancaReentrega(parametroPropostaDTO.getPcCobrancaReentrega());
		if (BigDecimal.ZERO.compareTo(pc.getPcCobrancaReentrega()) >= 0 ||
				new BigDecimal(100).compareTo(pc.getPcCobrancaReentrega()) == -1){
			throw new BusinessException("LMS-01206");
		}		
		pc.setPcCobrancaDevolucoes(parametroPropostaDTO.getPcCobrancaDevolucoes());
		if (BigDecimal.ZERO.compareTo(pc.getPcCobrancaDevolucoes()) >= 0 || 
				new BigDecimal(100).compareTo(pc.getPcCobrancaDevolucoes()) == -1){
			throw new BusinessException("LMS-01205");
		}
		pc.setTpIndicadorPercentualTde(parametroPropostaDTO.getTpIndicadorPercentualTde());
		pc.setVlPercentualTde(parametroPropostaDTO.getVlPercentualTde());
		pc.setTpIndicadorMinimoTde(parametroPropostaDTO.getTpIndicadorMinimoTde());
		pc.setVlMinimoTde(parametroPropostaDTO.getVlMinimoTde());
		pc.setTpSituacaoParametro(new DomainValue("P"));
		pc.setTabelaDivisaoCliente(null);
		pc.setClienteByIdClienteRedespacho(null);
		pc.setFilialByIdFilialMercurioRedespacho(null);

		if (parametroPropostaDTO.getRotaOrigemMunicipio() != null) {
			Municipio municipioOrigem = new Municipio();
			municipioOrigem.setIdMunicipio(parametroPropostaDTO.getRotaOrigemMunicipio().getIdMunicipio());
			pc.setMunicipioByIdMunicipioOrigem(municipioOrigem);
		} else {
			pc.setMunicipioByIdMunicipioOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoMunicipio() != null) {
			Municipio municipioDestino = new Municipio();
			municipioDestino.setIdMunicipio(parametroPropostaDTO.getRotaDestinoMunicipio().getIdMunicipio());
			pc.setMunicipioByIdMunicipioDestino(municipioDestino);
		} else {
			pc.setMunicipioByIdMunicipioDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemFilial() != null) {
			Filial filialOrigem = new Filial();
			filialOrigem.setIdFilial(parametroPropostaDTO.getRotaOrigemFilial().getIdFilial());
			pc.setFilialByIdFilialOrigem(filialOrigem);
		} else {
			pc.setFilialByIdFilialOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoFilial() != null) {
			Filial filialDestino = new Filial();
			filialDestino.setIdFilial(parametroPropostaDTO.getRotaDestinoFilial().getIdFilial());
			pc.setFilialByIdFilialDestino(filialDestino);
		} else {
			pc.setFilialByIdFilialDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemZona() != null) {
			Zona zonaOrigem = new Zona();
			zonaOrigem.setIdZona(parametroPropostaDTO.getRotaOrigemZona().getIdZona());
			pc.setZonaByIdZonaOrigem(zonaOrigem);
		} else {
			pc.setZonaByIdZonaOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoZona() != null) {
			Zona zonaDestino = new Zona();
			zonaDestino.setIdZona(parametroPropostaDTO.getRotaDestinoZona().getIdZona());
			pc.setZonaByIdZonaDestino(zonaDestino);
		} else {
			pc.setZonaByIdZonaDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemPais() != null) {
			Pais paisOrigem = new Pais();
			paisOrigem.setIdPais(parametroPropostaDTO.getRotaOrigemPais().getIdPais());
			pc.setPaisByIdPaisOrigem(paisOrigem);
		} else {
			pc.setPaisByIdPaisOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoPais() != null) {
			Pais paisDestino = new Pais();
			paisDestino.setIdPais(parametroPropostaDTO.getRotaDestinoPais().getIdPais());
			pc.setPaisByIdPaisDestino(paisDestino);
		} else {
			pc.setPaisByIdPaisDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemTipoLocalizacao() != null) {
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioOrigem = new TipoLocalizacaoMunicipio();
			tipoLocalizacaoMunicipioOrigem.setIdTipoLocalizacaoMunicipio(parametroPropostaDTO.getRotaOrigemTipoLocalizacao().getIdTipoLocalizacaoMunicipio());
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(tipoLocalizacaoMunicipioOrigem);
		} else {
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoTipoLocalizacao() != null) {
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino = new TipoLocalizacaoMunicipio();
			tipoLocalizacaoMunicipioDestino.setIdTipoLocalizacaoMunicipio(parametroPropostaDTO.getRotaDestinoTipoLocalizacao().getIdTipoLocalizacaoMunicipio());
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(tipoLocalizacaoMunicipioDestino);
		} else {
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemUf() != null) {
			UnidadeFederativa unidadeFederativaOrigem = new UnidadeFederativa();
			unidadeFederativaOrigem.setIdUnidadeFederativa(parametroPropostaDTO.getRotaOrigemUf().getIdUnidadeFederativa());
			pc.setUnidadeFederativaByIdUfOrigem(unidadeFederativaOrigem);
		} else {
			pc.setUnidadeFederativaByIdUfOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoUf() != null) {
			UnidadeFederativa unidadeFederativaDestino = new UnidadeFederativa();
			unidadeFederativaDestino.setIdUnidadeFederativa(parametroPropostaDTO.getRotaDestinoUf().getIdUnidadeFederativa());
			pc.setUnidadeFederativaByIdUfDestino(unidadeFederativaDestino);
		} else {
			pc.setUnidadeFederativaByIdUfDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemAeroporto() != null) {
			Aeroporto aeroportoOrigem = new Aeroporto();
			aeroportoOrigem.setIdAeroporto(parametroPropostaDTO.getRotaOrigemAeroporto().getIdAeroporto());
			pc.setAeroportoByIdAeroportoOrigem(aeroportoOrigem);
		} else {
			pc.setAeroportoByIdAeroportoOrigem(null);
		}

		if (parametroPropostaDTO.getRotaDestinoAeroporto() != null) {
			Aeroporto aeroportoDestino = new Aeroporto();
			aeroportoDestino.setIdAeroporto(parametroPropostaDTO.getRotaDestinoAeroporto().getIdAeroporto());
			pc.setAeroportoByIdAeroportoDestino(aeroportoDestino);
		} else {
			pc.setAeroportoByIdAeroportoDestino(null);
		}

		if (parametroPropostaDTO.getRotaOrigemGrupoRegiao() != null){
			GrupoRegiao grupoRegiao = new GrupoRegiao();
			grupoRegiao.setIdGrupoRegiao(parametroPropostaDTO.getRotaOrigemGrupoRegiao().getIdGrupoRegiao());
			pc.setGrupoRegiaoOrigem(grupoRegiao);
		} else {
			pc.setGrupoRegiaoOrigem(null);
		}
		
		if (parametroPropostaDTO.getRotaDestinoGrupoRegiao() != null){			
			GrupoRegiao grupoRegiao = new GrupoRegiao();
			grupoRegiao.setIdGrupoRegiao(parametroPropostaDTO.getRotaDestinoGrupoRegiao().getIdGrupoRegiao());
			pc.setGrupoRegiaoDestino(grupoRegiao);
		} else {
			pc.setGrupoRegiaoDestino(null);
		}

		pc.setPcReajFretePeso(null);
		pc.setPcReajVlMinimoFreteQuilo(null);
		pc.setPcReajVlFreteVolume(null);
		pc.setPcReajAdvalorem(null);
		pc.setPcReajAdvalorem2(null);
		pc.setPcReajVlMinimoFretePercen(null);
		pc.setPcReajMinimoGris(null);
		pc.setPcReajMinimoTrt(null);
		pc.setCotacao(null);
		pc.setPcReajTarifaMinima(null);
		pc.setPcReajVlTarifaEspecifica(null);
		pc.setPcReajVlToneladaFretePerc(null);
		pc.setPcReajPedagio(null);
		pc.setPcReajMinimoTde(null);
		pc.setDsEspecificacaoRota(parametroPropostaDTO.getDsEspecificacaoRota());
		pc.setTabelaPreco(simulacao.getTabelaPreco());

		/** Salva Parametro e valida Simulacao */
		simulacao.setDtEfetivacao(null); // LMS-2293
		simulacaoService.storeProposta(simulacao, pc);

		return Response.ok(this.findById(pc.getIdParametroCliente())).build();
	}
	
	
	@POST
	@Path("/findGeneralidadeClienteById")
	public Response findGeneralidadeClienteById(Long id){
		Map map = generalidadeClienteService.findByIdMap(id);
		
		GeneralidadeClienteDTO dto = new GeneralidadeClienteDTO();
		dto.setId(MapUtilsPlus.getLong(map, "idGeneralidadeCliente"));
		dto.setIdGeneralidadeCliente(MapUtilsPlus.getLong(map, "idGeneralidadeCliente"));
		Map parcelaMap = MapUtilsPlus.getMap(map, "parcelaPreco",null);
		
		Map tpIndicadorMap = MapUtilsPlus.getMap(map,"tpIndicador",null);
		Map tpIndicadorMinimoMap = MapUtilsPlus.getMap(map,"tpIndicadorMinimo",null);
		
		dto.setIdParcelaPreco(MapUtilsPlus.getLong(parcelaMap, "idParcelaPreco"));
		if (tpIndicadorMap != null){
			dto.setTpIndicador(new DomainValue((String)tpIndicadorMap.get("value")));
		}
		dto.setVlGeneralidade(MapUtilsPlus.getBigDecimal(map, "vlGeneralidade"));
		if (tpIndicadorMinimoMap != null){
			dto.setTpIndicadorMinimo(new DomainValue((String)tpIndicadorMinimoMap.get("value")));
		}
		dto.setVlMinimo(MapUtilsPlus.getBigDecimal(map, "vlMinimo"));
		
		dto.setIdParametroCliente(MapUtilsPlus.getLong(map, "idParametroCliente"));
		
		return Response.ok(dto).build();
	}
	
	@POST
	@Path("/removeGeneralidadeClienteByIds")
	public Response removeGeneralidadeClienteTableByIds(GeneralidadeClienteFiltroDTO filterDto){
		generalidadeClienteService.removeByIds(filterDto.getIds());
		return Response.ok().build();
	}
	
	
	@POST
	@Path("/storeGeneralidadeCliente")
	public Response storeGeneralidadeCliente(GeneralidadeClienteDTO dto){
		
		
		GeneralidadeCliente bean = new GeneralidadeCliente();
		bean.setIdGeneralidadeCliente(dto.getId());
		bean.setParcelaPreco(new ParcelaPreco(dto.getIdParcelaPreco()));
		
		bean.setTpIndicador(dto.getTpIndicador());
		bean.setVlGeneralidade(dto.getVlGeneralidade());
		
		bean.setTpIndicadorMinimo(dto.getTpIndicadorMinimo());
		bean.setVlMinimo(dto.getVlMinimo());

		bean.setParametroCliente(parametroClienteService.findById(dto.getIdParametroCliente()));
		
		if(bean.getPcReajMinimo() == null){
			bean.setPcReajMinimo(new BigDecimal(0));
		}
		
		generalidadeClienteService.store(bean);
		return Response.ok().build();
	}
	
	
	
	
}
