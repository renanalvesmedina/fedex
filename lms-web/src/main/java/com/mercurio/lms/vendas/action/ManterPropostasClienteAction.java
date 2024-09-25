package com.mercurio.lms.vendas.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.Zona;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.municipios.model.service.ZonaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.tabelaprecos.util.TabelaPrecoUtils;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.Simulacao;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.ParametroClienteService;
import com.mercurio.lms.vendas.model.service.SimulacaoService;
import com.mercurio.lms.vendas.util.ConstantesVendas;
import com.mercurio.lms.vendas.util.SimulacaoUtils;

/**
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vendas.manterPropostasClienteAction"
 */
public class ManterPropostasClienteAction extends CrudAction {
	private ClienteService clienteService;
	private DivisaoClienteService divisaoClienteService;
	private TabelaPrecoService tabelaPrecoService;
	private ServicoService servicoService;
	private ZonaService zonaService;
	private PaisService paisService;
	private UnidadeFederativaService unidadeFederativaService;
	private FilialService filialService;
	private EnderecoPessoaService enderecoPessoaService;
	private MunicipioFilialService municipioFilialService;
	private AeroportoService aeroportoService;
	private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
	private ParametroGeralService parametroGeralService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroClienteService parametroClienteService;
	private DomainValueService domainValueService;
	private GrupoRegiaoService grupoRegiaoService;
	private UsuarioService usuarioService;

	public TypedFlatMap findDadosSessao() {
		Pais pais = getPaisService().findById(SessionUtils.getPaisSessao().getIdPais());
		Filial filial = SessionUtils.getFilialSessao();
		TypedFlatMap result = new TypedFlatMap();
		result.put("pais.idPais", pais.getIdPais());
		result.put("pais.nmPais", pais.getNmPais());
		result.put("zona.idZona", pais.getZona().getIdZona());
		result.put("filial.idFilial", filial.getIdFilial());
		return result;
	}

	public void reconfiguraSessao() {
		SimulacaoUtils.removeSimulacaoFromSession();
	}

	public List findLookupTabelaPreco(Map criteria) {
		return getTabelaPrecoService().findLookup(criteria);
	}

	public List findClienteLookup(TypedFlatMap criteria) {
		return clienteService.findLookupCliente(criteria.getString("pessoa.nrIdentificacao"));
	}

	@SuppressWarnings("rawtypes")
	public List findFuncionarioPromotorLookup(TypedFlatMap criteria) {
		return usuarioService.findLookupFuncionarioPromotor(FormatUtils.fillNumberWithZero(criteria.get("nrMatricula").toString(), 9));
	}

	public List findDivisaoCombo(TypedFlatMap criteria){
		Long idCliente = criteria.getLong("cliente.idCliente");
		if(LongUtils.hasValue(idCliente)) {
		return getDivisaoClienteService().findLookupDivisoesCliente(
				 idCliente
			,ConstantesVendas.SITUACAO_ATIVO);
	}
		return Collections.EMPTY_LIST;
	}

	public List findServicoCombo(TypedFlatMap criteria) {
		return getServicoService().find(criteria);
	}

	public List findZona(Map criteria) {
		return getZonaService().find(criteria);
	}

	public List findLookupPais(Map criteria) {
		return getPaisService().findLookup(criteria);
	}

	public List findUnidadeFederativaByPais(Map criteria){
		return getUnidadeFederativaService().findByPais(criteria);
	}

	public List findLookupFilial(TypedFlatMap criteria) {
		List filiais = getFilialService().findLookupBySgFilial(criteria.getString("sgFilial"), null);
		if(filiais.size() == 1) {
			Map map = (Map)filiais.get(0);
			map.put("endereco", findEndereco((Long)map.get("idFilial")));
		}
		return filiais;
	}

	public TypedFlatMap findEndereco(Long idPessoa) {
		EnderecoPessoa ep = findEnderecoPessoa(idPessoa);
		if(ep != null){
			TypedFlatMap result = new TypedFlatMap();
			result.put("municipio.unidadeFederativa.idUnidadeFederativa",ep.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
			result.put("municipio.unidadeFederativa.pais.idPais",ep.getMunicipio().getUnidadeFederativa().getPais().getIdPais());
			result.put("municipio.unidadeFederativa.pais.nmPais",ep.getMunicipio().getUnidadeFederativa().getPais().getNmPais());
			result.put("municipio.unidadeFederativa.pais.zona.idZona",ep.getMunicipio().getUnidadeFederativa().getPais().getZona().getIdZona());
			result.put("municipio.unidadeFederativa.pais.zona.dsZona",ep.getMunicipio().getUnidadeFederativa().getPais().getZona().getDsZona());
			return result;
		}
		return null;
	}

	public EnderecoPessoa findEnderecoPessoa(Long idPessoa) {
		return getEnderecoPessoaService().findEnderecoPessoaPadrao(idPessoa);
	}

	public List findLookupMunicipioFilial(Map criteria){
		FilterList filter = new FilterList(getMunicipioFilialService().findLookup(criteria)) {
			public Map filterItem(Object item) {
				MunicipioFilial mf = (MunicipioFilial)item;
				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("municipio.idMunicipio",mf.getMunicipio().getIdMunicipio());
				typedFlatMap.put("municipio.nmMunicipio",mf.getMunicipio().getNmMunicipio());
				typedFlatMap.putAll(findEndereco(mf.getFilial().getIdFilial()));
				typedFlatMap.put("filial.idFilial",mf.getFilial().getIdFilial());
				typedFlatMap.put("filial.sgFilial",mf.getFilial().getSgFilial());
				typedFlatMap.put("filial.pessoa.nmFantasia",mf.getFilial().getPessoa().getNmFantasia());
				return typedFlatMap;
			}
		};
		return (List)filter.doFilter();
	}

	public List findLookupAeroporto(Map criteria) {
		List l = getAeroportoService().findLookupAeroporto(criteria);
		if(l != null && l.size() == 1) {
			Map map = (Map)l.get(0);
			map.put("endereco", findEndereco((Long)map.get("idAeroporto")));
		}
		return l;
	}

	public List findTipoLocalizacao(Map criteria){
		return getTipoLocalizacaoMunicipioService().find(criteria);
	}

	public List findByUfGrupoRegiao(TypedFlatMap criteria){		
    	if(criteria == null || 
    	   criteria.get("unidadeFederativaByIdUf.idUnidadeFederativa") == null || 
    	   criteria.get("tabelaPreco.idTabelaPreco") == null) return null;
    	
    	Long idUnidadeFederativa = MapUtils.getLong(criteria, "unidadeFederativaByIdUf.idUnidadeFederativa");
    	Long idTabela = MapUtils.getLong(criteria, "tabelaPreco.idTabelaPreco");
    	List<GrupoRegiao> grupos = grupoRegiaoService.findByUfAndTabela(idUnidadeFederativa, idTabela);
    	return grupos;
    	}

    public List findTipoLocalizacaoOperacional(Map criteria){
    	return getTipoLocalizacaoMunicipioService().findTipoLocalizacaoOperacional();
    }
	
	public TypedFlatMap disableFields() {
		TypedFlatMap result = new TypedFlatMap();
		
		Filial filial = SessionUtils.getFilialSessao();
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();

		Boolean disable = Boolean.FALSE;
		if (simulacao != null && simulacao.getFilial() != null) {
			if (!filial.getIdFilial().equals(simulacao.getFilial().getIdFilial()) ||
					Boolean.TRUE.equals(simulacao.getBlEfetivada())) {
				disable = Boolean.TRUE;
			}
		}
		result.put("disableAll", disable);

		return result;
	}

	public TypedFlatMap findPercentuaisTotalFrete() {
		TypedFlatMap result = new TypedFlatMap();

		BigDecimal pcr = (BigDecimal) getParametroGeralService().findConteudoByNomeParametro("PercentualCobrancaReentrega",false);
		result.put("pcCobrancaReentrega", new DecimalFormat("##0.00").format(pcr));

		BigDecimal pcd = (BigDecimal) getParametroGeralService().findConteudoByNomeParametro("PercentualCobrancaDevolucao",false);
		result.put("pcCobrancaDevolucoes", new DecimalFormat("##0.00").format(pcd));

		return result;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		DomainValue tpGeracaoProposta = SimulacaoUtils.getSimulacaoInSession().getTpGeracaoProposta();
		if(tpGeracaoProposta != null) {
			criteria.put("tpGeracaoProposta", tpGeracaoProposta.getValue());
		}
		return getSimulacaoService().findPaginatedParametros(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		DomainValue tpGeracaoProposta = SimulacaoUtils.getSimulacaoInSession().getTpGeracaoProposta();
		if(tpGeracaoProposta != null) {
			criteria.put("tpGeracaoProposta", tpGeracaoProposta.getValue());
		}
		return getSimulacaoService().getRowCountParametros(criteria);
	}

	public TypedFlatMap findById(Long idParametroCliente) {
		TypedFlatMap simulacao = getSimulacaoService().findDadosByIdParametroCliente(idParametroCliente);

		String tpTipoTabelaPreco = simulacao.getString("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco.value");
		String tpSubtipoTabelaPreco = simulacao.getString("tabelaPreco.subtipoTabelaPreco.tpSubtipoTabelaPreco");
		Integer nrVersao = simulacao.getInteger("tabelaPreco.tipoTabelaPreco.nrVersao");
		String tabelaPrecoString = TabelaPrecoUtils.formatTabelaPrecoString(tpTipoTabelaPreco, nrVersao, tpSubtipoTabelaPreco);
		simulacao.put("tabelaPreco.tabelaPrecoString", tabelaPrecoString);
		simulacao.put("tabelaDivisaoCliente.tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco", simulacao.get("tabelaPreco.subtipoTabelaPreco.idSubtipoTabelaPreco"));
		simulacao.put("tabelaDivisaoCliente.tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco", simulacao.get("tabelaPreco.tipoTabelaPreco.tpTipoTabelaPreco"));

		simulacao.put("zonaOrigem.idZona", simulacao.get("zonaByIdZonaOrigem.idZona"));
		simulacao.put("zonaDestino.idZona", simulacao.get("zonaByIdZonaDestino.idZona"));

		String dsMoeda = simulacao.getString("tabelaPreco.moeda.sgMoeda") + " " + simulacao.getString("tabelaPreco.moeda.dsSimbolo"); 
		simulacao.put("tabelaPreco.moeda.dsMoeda", dsMoeda);

		simulacao.put("municipioByIdMunicipioOrigem.municipio.idMunicipio", simulacao.get("municipioByIdMunicipioOrigem.idMunicipio"));
		simulacao.put("municipioByIdMunicipioDestino.municipio.idMunicipio", simulacao.get("municipioByIdMunicipioDestino.idMunicipio"));
		simulacao.put("municipioByIdMunicipioOrigem.municipio.nmMunicipio", simulacao.remove("municipioByIdMunicipioOrigem.nmMunicipio"));
		simulacao.put("municipioByIdMunicipioDestino.municipio.nmMunicipio", simulacao.remove("municipioByIdMunicipioDestino.nmMunicipio"));
		simulacao.put("simulacao.idSimulacao", simulacao.get("idSimulacao"));

		Boolean blEfetivada = simulacao.getBoolean("blEfetivada");
		if (Boolean.FALSE.equals(blEfetivada)) {
			simulacao.put("blEfetivada", "N");
		} else {
			simulacao.put("blEfetivada", "S");
		}
		
		String tpIdentificacao = simulacao.getString("cliente.pessoa.tpIdentificacao.value");
		String nrIdentificacao = simulacao.getString("cliente.pessoa.nrIdentificacao");
		String nrIdentificacaoFormatado = FormatUtils.formatIdentificacao(tpIdentificacao, nrIdentificacao);
		simulacao.put("cliente.pessoa.nrIdentificacao", nrIdentificacaoFormatado);

		return simulacao; 
	}
	
	public TypedFlatMap findDescricaoDados(TypedFlatMap criteria) {
		Long idDivisaoCliente = criteria.getLong("divisaoCliente.idDivisaoCliente");
		Long idServico = criteria.getLong("servico.idServico");

		TypedFlatMap result = new TypedFlatMap();
		if (idDivisaoCliente != null) {
			DivisaoCliente divisaoCliente = divisaoClienteService.findById(idDivisaoCliente);
			result.put("divisaoCliente.dsDivisaoCliente", divisaoCliente.getDsDivisaoCliente());
		}
		if (idServico != null) {
			Servico servico = servicoService.findById(idServico);
			result.put("servico.dsServico", servico.getDsServico());
		}
		return result;
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getParametroClienteService().removeByIdsProposta(ids, SimulacaoUtils.getSimulacaoInSession().getIdSimulacao());
		refreshSimulacaoInSession();
	}

	public void removeById(Long id) {
		getParametroClienteService().removeByIdProposta(id, SimulacaoUtils.getSimulacaoInSession().getIdSimulacao());
		refreshSimulacaoInSession();
	}

	public TypedFlatMap store(TypedFlatMap data) {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		
		String dsMoeda = null;
		Long idTabelaPreco = data.getLong("tabelaPreco.idTabelaPreco");
		if (idTabelaPreco != null) {
			TypedFlatMap mapMoeda = getTabelaPrecoService().findMoedaByIdTabelaPreco(idTabelaPreco);
			Long idMoeda = mapMoeda.getLong("idMoeda");
			if (idMoeda != null) {
				Moeda moeda = new Moeda();
				moeda.setDsSimbolo(mapMoeda.getString("dsSimbolo"));
				moeda.setSgMoeda(mapMoeda.getString("sgMoeda"));
				
				dsMoeda = FormatUtils.concatSiglaSimboloMoeda(moeda);
			}
		}

		ParametroCliente pc = new ParametroCliente();
		Long idParametroCliente = data.getLong("parametroCliente.idParametroCliente");
		if (LongUtils.hasValue(idParametroCliente)) {
			pc = getParametroClienteService().findById(idParametroCliente);
		}
		pc.setDtVigenciaInicial(JTDateTimeUtils.getDataAtual());
		pc.setDtVigenciaFinal(null);
		pc.setTpIndicadorPercentualGris(new DomainValue(data.getString("tpIndicadorPercentualGris")));
		pc.setVlPercentualGris(data.getBigDecimal("vlPercentualGris"));
		pc.setTpIndicadorMinimoGris(new DomainValue(data.getString("tpIndicadorMinimoGris")));
		pc.setVlMinimoGris(data.getBigDecimal("vlMinimoGris"));
		pc.setTpIndicadorPercentualTrt(new DomainValue(data.getString("tpIndicadorPercentualTrt")));
		pc.setVlPercentualTrt(data.getBigDecimal("vlPercentualTrt"));
		pc.setTpIndicadorMinimoTrt(new DomainValue(data.getString("tpIndicadorMinimoTrt")));
		pc.setVlMinimoTrt(data.getBigDecimal("vlMinimoTrt"));
		pc.setTpIndicadorPedagio(new DomainValue(data.getString("tpIndicadorPedagio")));
		pc.setVlPedagio(data.getBigDecimal("vlPedagio"));
		pc.setTpIndicadorMinFretePeso(new DomainValue(data.getString("tpIndicadorMinFretePeso")));
		pc.setVlMinFretePeso(data.getBigDecimal("vlMinFretePeso"));
		pc.setTpIndicadorPercMinimoProgr(new DomainValue(data.getString("tpIndicadorPercMinimoProgr")));
		pc.setVlPercMinimoProgr(data.getBigDecimal("vlPercMinimoProgr"));
		pc.setTpIndicadorFretePeso(new DomainValue(data.getString("tpIndicadorFretePeso")));
		pc.setVlFretePeso(data.getBigDecimal("vlFretePeso"));
		pc.setTpIndicadorAdvalorem(new DomainValue(data.getString("tpIndicadorAdvalorem")));
		pc.setVlAdvalorem(data.getBigDecimal("vlAdvalorem"));
		pc.setTpIndicadorAdvalorem2(new DomainValue(data.getString("tpIndicadorAdvalorem2")));
		pc.setVlAdvalorem2(data.getBigDecimal("vlAdvalorem2"));
		pc.setTpIndicadorValorReferencia(new DomainValue(data.getString("tpIndicadorValorReferencia")));
		pc.setVlValorReferencia(data.getBigDecimal("vlValorReferencia"));
		pc.setVlMinimoFreteQuilo(data.getBigDecimal("vlMinimoFreteQuilo"));
		pc.setPcFretePercentual(data.getBigDecimal("pcFretePercentual"));
		pc.setVlMinimoFretePercentual(data.getBigDecimal("vlMinimoFretePercentual"));
		pc.setVlToneladaFretePercentual(data.getBigDecimal("vlToneladaFretePercentual"));
		pc.setPsFretePercentual(data.getBigDecimal("psFretePercentual"));
		pc.setPcDescontoFreteTotal(data.getBigDecimal("pcDescontoFreteTotal"));
		pc.setTpIndicVlrTblEspecifica(new DomainValue(data.getString("tpIndicVlrTblEspecifica")));
		pc.setVlTblEspecifica(data.getBigDecimal("vlTblEspecifica"));
		pc.setVlFreteVolume(data.getBigDecimal("vlFreteVolume"));
		
		/*Solicitacao para o branch RPP - 01.04.01.07*/
		pc.setBlPagaCubagem(Boolean.TRUE);
		pc.setPcPagaCubagem(BigDecimalUtils.HUNDRED);
		
		pc.setBlPagaPesoExcedente(data.getBoolean("blPagaPesoExcedente"));
		pc.setTpTarifaMinima(new DomainValue(data.getString("tpTarifaMinima")));
		pc.setVlTarifaMinima(data.getBigDecimal("vlTarifaMinima"));
		pc.setPcCobrancaReentrega(data.getBigDecimal("pcCobrancaReentrega"));
		if (BigDecimal.ZERO.compareTo(pc.getPcCobrancaReentrega()) >= 0 ||
				new BigDecimal(100).compareTo(pc.getPcCobrancaReentrega()) == -1){
			throw new BusinessException("LMS-01206");
		}		
		pc.setPcCobrancaDevolucoes(data.getBigDecimal("pcCobrancaDevolucoes"));
		if (BigDecimal.ZERO.compareTo(pc.getPcCobrancaDevolucoes()) >= 0 || 
				new BigDecimal(100).compareTo(pc.getPcCobrancaDevolucoes()) == -1){
			throw new BusinessException("LMS-01205");
		}
		pc.setTpIndicadorPercentualTde(new DomainValue(data.getString("tpIndicadorPercentualTde")));
		pc.setVlPercentualTde(data.getBigDecimal("vlPercentualTde"));
		pc.setTpIndicadorMinimoTde(new DomainValue(data.getString("tpIndicadorMinimoTde")));
		pc.setVlMinimoTde(data.getBigDecimal("vlMinimoTde"));
		pc.setTpSituacaoParametro(new DomainValue("P"));
		pc.setTabelaDivisaoCliente(null);
		pc.setClienteByIdClienteRedespacho(null);
		pc.setFilialByIdFilialMercurioRedespacho(null);

		Long idMunicipioOrigem = data.getLong("municipioByIdMunicipioOrigem.idMunicipio");
		if (idMunicipioOrigem != null) {
			Municipio municipioOrigem = new Municipio();
			municipioOrigem.setIdMunicipio(idMunicipioOrigem);
			pc.setMunicipioByIdMunicipioOrigem(municipioOrigem);
		} else {
			pc.setMunicipioByIdMunicipioOrigem(null);
		}

		Long idMunicipioDestino = data.getLong("municipioByIdMunicipioDestino.idMunicipio");
		if (idMunicipioDestino != null) {
			Municipio municipioDestino = new Municipio();
			municipioDestino.setIdMunicipio(idMunicipioDestino);
			pc.setMunicipioByIdMunicipioDestino(municipioDestino);
		} else {
			pc.setMunicipioByIdMunicipioDestino(null);
		}

		Long idFilialOrigem = data.getLong("filialByIdFilialOrigem.idFilial");
		if (idFilialOrigem != null) {
			Filial filialOrigem = new Filial();
			filialOrigem.setIdFilial(idFilialOrigem);
			pc.setFilialByIdFilialOrigem(filialOrigem);
		} else {
			pc.setFilialByIdFilialOrigem(null);
		}

		Long idFilialDestino = data.getLong("filialByIdFilialDestino.idFilial");
		if (idFilialDestino != null) {
			Filial filialDestino = new Filial();
			filialDestino.setIdFilial(idFilialDestino);
			pc.setFilialByIdFilialDestino(filialDestino);
		} else {
			pc.setFilialByIdFilialDestino(null);
		}

		Long idZonaOrigem = data.getLong("zonaByIdZonaOrigem.idZona");
		if (idZonaOrigem != null) {
			Zona zonaOrigem = new Zona();
			zonaOrigem.setIdZona(idZonaOrigem);
			pc.setZonaByIdZonaOrigem(zonaOrigem);
		} else {
			pc.setZonaByIdZonaOrigem(null);
		}

		Long idZonaDestino = data.getLong("zonaByIdZonaDestino.idZona");
		if (idZonaDestino != null) {
			Zona zonaDestino = new Zona();
			zonaDestino.setIdZona(idZonaDestino);
			pc.setZonaByIdZonaDestino(zonaDestino);
		} else {
			pc.setZonaByIdZonaDestino(null);
		}

		Long idPaisOrigem = data.getLong("paisByIdPaisOrigem.idPais");
		if (idPaisOrigem != null) {
			Pais paisOrigem = new Pais();
			paisOrigem.setIdPais(idPaisOrigem);
			pc.setPaisByIdPaisOrigem(paisOrigem);
		} else {
			pc.setPaisByIdPaisOrigem(null);
		}

		Long idPaisDestino = data.getLong("paisByIdPaisDestino.idPais");
		if (idPaisDestino != null) {
			Pais paisDestino = new Pais();
			paisDestino.setIdPais(idPaisDestino);
			pc.setPaisByIdPaisDestino(paisDestino);
		} else {
			pc.setPaisByIdPaisDestino(null);
		}

		Long idTipoLocalizacaoMunicipioOrigem = data.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		if (idTipoLocalizacaoMunicipioOrigem != null) {
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioOrigem = new TipoLocalizacaoMunicipio();
			tipoLocalizacaoMunicipioOrigem.setIdTipoLocalizacaoMunicipio(idTipoLocalizacaoMunicipioOrigem);
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(tipoLocalizacaoMunicipioOrigem);
		} else {
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem(null);
		}

		Long idTipoLocalizacaoMunicipioDestino = data.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio");
		if (idTipoLocalizacaoMunicipioDestino != null) {
			TipoLocalizacaoMunicipio tipoLocalizacaoMunicipioDestino = new TipoLocalizacaoMunicipio();
			tipoLocalizacaoMunicipioDestino.setIdTipoLocalizacaoMunicipio(idTipoLocalizacaoMunicipioDestino);
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(tipoLocalizacaoMunicipioDestino);
		} else {
			pc.setTipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino(null);
		}

		Long idUnidadeFederativaOrigem = data.getLong("unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		if (idUnidadeFederativaOrigem != null) {
			UnidadeFederativa unidadeFederativaOrigem = new UnidadeFederativa();
			unidadeFederativaOrigem.setIdUnidadeFederativa(idUnidadeFederativaOrigem);
			pc.setUnidadeFederativaByIdUfOrigem(unidadeFederativaOrigem);
		} else {
			pc.setUnidadeFederativaByIdUfOrigem(null);
		}

		Long idUnidadeFederativaDestino = data.getLong("unidadeFederativaByIdUfDestino.idUnidadeFederativa");
		if (idUnidadeFederativaDestino != null) {
			UnidadeFederativa unidadeFederativaDestino = new UnidadeFederativa();
			unidadeFederativaDestino.setIdUnidadeFederativa(idUnidadeFederativaDestino);
			pc.setUnidadeFederativaByIdUfDestino(unidadeFederativaDestino);
		} else {
			pc.setUnidadeFederativaByIdUfDestino(null);
		}

		Long idAeroportoOrigem = data.getLong("aeroportoByIdAeroportoOrigem.idAeroporto");
		if (idAeroportoOrigem != null) {
			Aeroporto aeroportoOrigem = new Aeroporto();
			aeroportoOrigem.setIdAeroporto(idAeroportoOrigem);
			pc.setAeroportoByIdAeroportoOrigem(aeroportoOrigem);
		} else {
			pc.setAeroportoByIdAeroportoOrigem(null);
		}

		Long idAeroportoDestino = data.getLong("aeroportoByIdAeroportoDestino.idAeroporto");
		if (idAeroportoDestino != null) {
			Aeroporto aeroportoDestino = new Aeroporto();
			aeroportoDestino.setIdAeroporto(idAeroportoDestino);
			pc.setAeroportoByIdAeroportoDestino(aeroportoDestino);
		} else {
			pc.setAeroportoByIdAeroportoDestino(null);
		}

		Long idGrupoRegiaoOrigem = data.getLong("grupoRegiaoOrigem.idGrupoRegiao");
		if (idGrupoRegiaoOrigem != null){
			GrupoRegiao grupoRegiao = new GrupoRegiao();
			grupoRegiao.setIdGrupoRegiao(idGrupoRegiaoOrigem);
			pc.setGrupoRegiaoOrigem(grupoRegiao);
		} else {
			pc.setGrupoRegiaoOrigem(null);
		}
		
		Long idGrupoRegiaoDestino = data.getLong("grupoRegiaoDestino.idGrupoRegiao");
		if (idGrupoRegiaoDestino != null){			
			GrupoRegiao grupoRegiao = new GrupoRegiao();
			grupoRegiao.setIdGrupoRegiao(idGrupoRegiaoDestino);
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
		pc.setDsEspecificacaoRota(data.getString("dsEspecificacaoRota"));
		pc.setTabelaPreco(simulacao.getTabelaPreco());

		/** Salva Parametro e valida Simulacao */
		simulacao.setDtEfetivacao(null); // LMS-2293
		getSimulacaoService().storeProposta(simulacao, pc);

		TypedFlatMap result = new TypedFlatMap();
		result.put("parametroCliente.idParametroCliente", pc.getIdParametroCliente());
		result.put("simulacao.idSimulacao", simulacao.getIdSimulacao());
		result.put("tabelaPreco.moeda.dsMoeda", "");
		result.put("simulacao.setDtTabelaVigenciaInicial", simulacao.getDtTabelaVigenciaInicial());
		if (StringUtils.isNotBlank(dsMoeda)) {
			result.put("tabelaPreco.moeda.dsMoeda", dsMoeda);	
		}
		result.put("tpSituacaoParametro", pc.getTpSituacaoParametro().getValue());

		if (simulacao.getTpSituacaoAprovacao() != null) {
			String value = simulacao.getTpSituacaoAprovacao().getValue();
			String description = getDomainValueService().findDomainValueDescription("DM_SITUACAO_SIMULACAO", value);
			
			result.put("tpSituacaoAprovacao.description", description);
			result.put("tpSituacaoAprovacao.value", value);
		}
		if (simulacao.getUsuarioByIdUsuarioAprovou() != null) {
			result.put("usuarioByIdUsuarioAprovou.nrMatricula", simulacao.getUsuarioByIdUsuarioAprovou().getNrMatricula());
			result.put("usuarioByIdUsuarioAprovou.nmUsuario", simulacao.getUsuarioByIdUsuarioAprovou().getNmUsuario());
		}
		result.put("dtAprovacao", simulacao.getDtAprovacao());

		if (simulacao.getTpSituacaoAprovacao() != null) {
			result.put("tpSituacaoAprovacao.description", simulacao.getTpSituacaoAprovacao().getDescription());
			result.put("tpSituacaoAprovacao.value", simulacao.getTpSituacaoAprovacao().getValue());
		}
		
		setSimulacaoInSession(simulacao.getIdSimulacao());
		
		return result;
	}
	
	private void refreshSimulacaoInSession() {
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		setSimulacaoInSession(simulacao.getIdSimulacao());
	}
	
	public TypedFlatMap findParametroCliente(Long idParametroCliente) {
		TypedFlatMap parametro = (TypedFlatMap) getParametroClienteService().findByIdMap(idParametroCliente);
		parametro.put("tabelaPreco.moeda.dsSimbolo", parametro.remove("tabelaDivisaoCliente.tabelaPreco.moeda.dsSimbolo"));

		parametro.remove("paisByIdPaisOrigem.idPais");
		parametro.remove("paisByIdPaisOrigem.nmPais");
		parametro.remove("paisByIdPaisDestino.idPais");
		parametro.remove("paisByIdPaisDestino.nmPais");
		parametro.remove("zonaByIdZonaDestino.idZona");
		parametro.remove("zonaByIdZonaOrigem.idZona");
		parametro.remove("zonaByIdZonaDestino.dsZona");
		parametro.remove("zonaByIdZonaOrigem.dsZona");
		parametro.remove("unidadeFederativaByIdUfOrigem.idUnidadeFederativa");
		parametro.remove("unidadeFederativaByIdUfOrigem.siglaDescricao");
		parametro.remove("unidadeFederativaByIdUfOrigem.sgUnidadeFederativa");
		parametro.remove("unidadeFederativaByIdUfOrigem.nmUnidadeFederativa");
		parametro.remove("unidadeFederativaByIdUfDestino.idUnidadeFederativa");
		parametro.remove("unidadeFederativaByIdUfDestino.siglaDescricao");
		parametro.remove("unidadeFederativaByIdUfDestino.sgUnidadeFederativa");
		parametro.remove("unidadeFederativaByIdUfDestino.nmUnidadeFederativa");
		parametro.remove("filialByIdFilialOrigem.idFilial");
		parametro.remove("filialByIdFilialOrigem.sgFilial");
		parametro.remove("filialByIdFilialOrigem.pessoa.nmFantasia"); 
		parametro.remove("filialByIdFilialDestino.idFilial");
		parametro.remove("filialByIdFilialDestino.sgFilial");
		parametro.remove("filialByIdFilialDestino.pessoa.nmFantasia");
		parametro.remove("municipioByIdMunicipioOrigem.idMunicipio");
		parametro.remove("municipioByIdMunicipioOrigem.municipio.nmMunicipio");
		parametro.remove("municipioByIdMunicipioDestino.idMunicipio");
		parametro.remove("municipioByIdMunicipioDestino.municipio.nmMunicipio");
		parametro.remove("aeroportoByIdAeroportoOrigem.idAeroporto");
		parametro.remove("aeroportoByIdAeroportoOrigem.sgAeroporto");
		parametro.remove("aeroportoByIdAeroportoOrigem.pessoa.nmPessoa");
		parametro.remove("aeroportoByIdAeroportoDestino.idAeroporto");
		parametro.remove("aeroportoByIdAeroportoDestino.sgAeroporto");
		parametro.remove("aeroportoByIdAeroportoDestino.pessoa.nmPessoa"); 
		parametro.remove("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio");
		parametro.remove("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio");
		
		Simulacao simulacao = SimulacaoUtils.getSimulacaoInSession();
		parametro.put("simulacao.blEfetivada", simulacao.getBlEfetivada());
		
		return parametro;
	}
	
	public TypedFlatMap continuarParametros(TypedFlatMap criteria) {
		criteria.put("zonaByIdZonaOrigem.idZona", criteria.remove("zonaOrigem.idZona"));
		criteria.put("zonaByIdZonaDestino.idZona", criteria.remove("zonaDestino.idZona"));
		verifyParameters(criteria);
		
		TypedFlatMap moeda = getTabelaPrecoService().findMoedaByIdTabelaPreco(criteria.getLong("tabelaPreco.idTabelaPreco"));
		String dsMoeda = moeda.getString("sgMoeda") + " " + moeda.getString("dsSimbolo");
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("tabelaPreco.moeda.dsMoeda", dsMoeda);
		return result;
	}

	private void verifyParameters(TypedFlatMap criteria) {

		RestricaoRota restricaoRotaOrigem = new RestricaoRota();
		restricaoRotaOrigem.setIdZona(criteria.getLong("zonaByIdZonaOrigem.idZona"));
		restricaoRotaOrigem.setIdPais(criteria.getLong("paisByIdPaisOrigem.idPais"));
		restricaoRotaOrigem.setIdUnidadeFederativa(criteria.getLong("unidadeFederativaByIdUfOrigem.idUnidadeFederativa"));
		restricaoRotaOrigem.setIdFilial(criteria.getLong("filialByIdFilialOrigem.idFilial"));
		restricaoRotaOrigem.setIdMunicipio(criteria.getLong("municipioByIdMunicipioOrigem.idMunicipio"));
		restricaoRotaOrigem.setIdAeroporto(criteria.getLong("aeroportoByIdAeroportoOrigem.idAeroporto"));
		restricaoRotaOrigem.setIdTipoLocalizacao(criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"));
		restricaoRotaOrigem.setIdGrupoRegiao(criteria.getLong("grupoRegiaoOrigem.idGrupoRegiao"));

		RestricaoRota restricaoRotaDestino = new RestricaoRota();
		restricaoRotaDestino.setIdZona(criteria.getLong("zonaByIdZonaDestino.idZona"));
		restricaoRotaDestino.setIdPais(criteria.getLong("paisByIdPaisDestino.idPais"));
		restricaoRotaDestino.setIdUnidadeFederativa(criteria.getLong("unidadeFederativaByIdUfDestino.idUnidadeFederativa"));
		restricaoRotaDestino.setIdFilial(criteria.getLong("filialByIdFilialDestino.idFilial"));
		restricaoRotaDestino.setIdMunicipio(criteria.getLong("municipioByIdMunicipioDestino.idMunicipio"));
		restricaoRotaDestino.setIdAeroporto(criteria.getLong("aeroportoByIdAeroportoDestino.idAeroporto"));
		restricaoRotaDestino.setIdTipoLocalizacao(criteria.getLong("tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"));
		restricaoRotaDestino.setIdGrupoRegiao(criteria.getLong("grupoRegiaoDestino.idGrupoRegiao"));

		boolean isRotaOrigemValida = RotaPrecoUtils.isRotaValida(restricaoRotaOrigem);
		boolean isRotaDestinoValida = RotaPrecoUtils.isRotaValida(restricaoRotaDestino);

		if(!(isRotaOrigemValida && isRotaDestinoValida)) {
			throw new BusinessException("LMS-30001");
		}
	}

	private void setSimulacaoInSession(Long idSimulacao) {
		Simulacao s = getSimulacaoService().findById(idSimulacao);
		SimulacaoUtils.setSimulacaoInSession(s);
	}

	public void setService(SimulacaoService serviceService) {
		super.defaultService = serviceService;
	}
	public SimulacaoService getSimulacaoService() {
		return (SimulacaoService) super.defaultService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}
	public TabelaPrecoService getTabelaPrecoService() {
		return tabelaPrecoService;
	}
	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}
	public ServicoService getServicoService() {
		return servicoService;
	}
	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}
	public ZonaService getZonaService() {
		return zonaService;
	}
	public void setZonaService(ZonaService zonaService) {
		this.zonaService = zonaService;
	}
	public PaisService getPaisService() {
		return paisService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public MunicipioFilialService getMunicipioFilialService() {
		return municipioFilialService;
	}
	public void setMunicipioFilialService(
			MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	public AeroportoService getAeroportoService() {
		return aeroportoService;
	}
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
	public TipoLocalizacaoMunicipioService getTipoLocalizacaoMunicipioService() {
		return tipoLocalizacaoMunicipioService;
	}
	public void setTipoLocalizacaoMunicipioService(
			TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
		this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
	}
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public ParametroClienteService getParametroClienteService() {
		return parametroClienteService;
	}
	public void setParametroClienteService(
			ParametroClienteService parametroClienteService) {
		this.parametroClienteService = parametroClienteService;
	}
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public GrupoRegiaoService getGrupoRegiaoService() {
		return grupoRegiaoService;
	}
}