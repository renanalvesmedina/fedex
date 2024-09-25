package com.mercurio.lms.carregamento.swt.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.carregamento.model.service.*;
import com.mercurio.lms.contratacaoveiculos.model.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.dto.ManifestoFedexSegundoCarregamentoDTO;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.carregamento.model.CargoOperacional;
import com.mercurio.lms.carregamento.model.CartaoPedagio;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;
import com.mercurio.lms.carregamento.model.PrestadorServico;
import com.mercurio.lms.carregamento.model.TrechoCorporativo;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.FluxoContratacaoService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.expedicao.edi.model.service.NotaFiscalExpedicaoEDIService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaFreteCarreteiroCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TipoTabelaColetaEntregaService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.MotoristaRotaViagem;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MeioTransporteRotaViagemService;
import com.mercurio.lms.municipios.model.service.MotivoParadaService;
import com.mercurio.lms.municipios.model.service.MotoristaRotaViagemService;
import com.mercurio.lms.municipios.model.service.PontoParadaTrechoService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.RotaViagemService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Nao inserir documentacao apos ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servico.
 * @spring.bean id="lms.carregamento.swt.gerarControleCargasAction"
 */
public class GerarControleCargasAction extends MasterDetailAction {
	
	private CargoOperacionalService cargoOperacionalService;
	private CartaoPedagioService cartaoPedagioService;
	private ControleTrechoService controleTrechoService;
	private DomainValueService domainValueService;
	private EmpresaService empresaService;
	private EquipeService equipeService;
	private FilialRotaService filialRotaService;
	private FilialService filialService;
	private FluxoContratacaoService fluxoContratacaoService;
	private IntegranteEqOperacService integranteEqOperacService;
	private MeioTransporteRotaViagemService meioTransporteRotaViagemService;
	private MeioTransporteService meioTransporteService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private MoedaService moedaService;
	private MotivoParadaService motivoParadaService;
	private MotoristaRotaViagemService motoristaRotaViagemService;
	private MotoristaService motoristaService;
	private PagtoPedagioCcService pagtoPedagioCcService;
	private PostoPassagemCcService postoPassagemCcService;
	private PessoaService pessoaService;
	private PontoParadaTrechoService pontoParadaTrechoService;
	private PrestadorServicoService prestadorServicoService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private RotaViagemService rotaViagemService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService;
	private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
	private UsuarioService usuarioService;
	private ConfiguracoesFacade configuracoesFacade;
	private TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	// LMSA-6520: LMSA-6534
	private NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService;

	private AdiantamentoTrechoService adiantamentoTrechoService;
	
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";
	
	
	/**
	 * Busca a Service default desta Action
	 * 
	 * @param carregamentoDescargaService
	 */
	public ControleCargaService getControleCargaService() {
		return (ControleCargaService) super.getMasterService();
	}	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.setMasterService(controleCargaService);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupFilialBySolicitacaoContratacao(Map criteria) {
    	List list = filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		typedFlatMap.put("nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	
	@SuppressWarnings("rawtypes")
    public void validateCNHMotorista(Map criteria) {
		Long idMotorista = (Long)criteria.get("idMotorista");
		motoristaService.validateCNHMotorista(idMotorista);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupSolicitacaoContratacao(Map criteria) {
		Map mapFilial = new HashMap();
		mapFilial.put("idFilial", criteria.get("idFilial"));

		Map mapSolicitacao = new HashMap();
		mapSolicitacao.put("filial", mapFilial);
		mapSolicitacao.put("nrSolicitacaoContratacao", criteria.get("nrSolicitacaoContratacao"));
		mapSolicitacao.put("tpSolicitacaoContratacao", criteria.get("tpSolicitacaoContratacao"));

    	List list = solicitacaoContratacaoService.findLookup(mapSolicitacao);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		SolicitacaoContratacao solicitacaoContratacao = (SolicitacaoContratacao)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idSolicitacaoContratacao", solicitacaoContratacao.getIdSolicitacaoContratacao());
    		tfm.put("nrSolicitacaoContratacao", solicitacaoContratacao.getNrSolicitacaoContratacao());
    		tfm.put("idFilial", solicitacaoContratacao.getFilial().getIdFilial());
    		tfm.put("sgFilial", solicitacaoContratacao.getFilial().getSgFilial());
    		tfm.put("nmFantasia", solicitacaoContratacao.getFilial().getPessoa().getNmFantasia());

    		// LMSA-6520: LMSA-6534
            tfm.put("tpCargaCompartilhada", 
            		solicitacaoContratacao.getTpCargaCompartilhada() != null ? 
            				solicitacaoContratacao.getTpCargaCompartilhada().getValue() : "NAO CADASTRADO");

    		retorno.add(tfm);
    	}
    	return retorno;
    }
	
	// LMSA-6520: LMSA-6534
    public TypedFlatMap getTipoCargaCompartilhadaSolicitacaoContratacao(Map criteria) {
    	TypedFlatMap retorno = new TypedFlatMap();
    	String tpCargaCompartilhada = solicitacaoContratacaoService.getSolicitacaoContratacaoCargaCompartilhada((Long) criteria.get("idSolicitacaoContratacao"));
    	retorno.put("tpCargaCompartilhada", tpCargaCompartilhada);
    	return retorno;
    }

	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupRotaIdaVolta(TypedFlatMap criteria) {
    	List list = rotaIdaVoltaService.findLookupRotaIdaVolta(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		RotaIdaVolta rotaIdaVolta = (RotaIdaVolta)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idRotaIdaVolta", rotaIdaVolta.getIdRotaIdaVolta());
    		tfm.put("nrRota", rotaIdaVolta.getNrRota());
    		tfm.put("idRota", rotaIdaVolta.getRota().getIdRota());
    		tfm.put("dsRota", rotaIdaVolta.getRota().getDsRota());
    		tfm.put("vlFreteKm", rotaIdaVolta.getVlFreteKm());
    		tfm.put("nrDistancia", rotaIdaVolta.getNrDistancia());
    		tfm.put("vlFreteCarreteiro", rotaIdaVolta.getVlFreteCarreteiro());
    		tfm.put("sgMoeda", rotaIdaVolta.getMoedaPais().getMoeda().getSgMoeda());
    		tfm.put("dsSimbolo", rotaIdaVolta.getMoedaPais().getMoeda().getDsSimbolo());
    		tfm.put("idRotaViagem", rotaIdaVolta.getRotaViagem().getIdRotaViagem());
    		tfm.put("idTipoMeioTransporte", rotaIdaVolta.getRotaViagem().getTipoMeioTransporte().getIdTipoMeioTransporte());
    		retorno.add(tfm);
    	}
    	return retorno;
    }

    /**
     * 
     * @param map
     * @return
     */
    public TypedFlatMap findDadosRotaIdaVolta(TypedFlatMap criteria) {
    	Boolean isFilialOrigemDaRota = filialRotaService.validatePermissaoFilialUsuarioParaGerarControleCarga(criteria.getLong("idRota"));

    	TypedFlatMap tfm = new TypedFlatMap();

    	if (isFilialOrigemDaRota){
    		tfm.put("blPermiteInformarVeiculo", Boolean.TRUE);
    	} else {
    		tfm.put("blPermiteInformarVeiculo", Boolean.FALSE);
    	}

    	Long idRotaIdaVolta = criteria.getLong("idRotaIdaVolta");
    	tfm.putAll( getTrechoRotaCompleta(idRotaIdaVolta) );
    	tfm.putAll( getMotoristaByRotaViagem(idRotaIdaVolta) );
    	tfm.putAll( getVlFreteCarreteiro(criteria.getBigDecimal("vlFreteCarreteiro"), criteria.getString("sgMoeda"), criteria.getString("dsSimbolo")) );
    	
    	return tfm;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupMotorista(Map criteria) {
   		TypedFlatMap tfm = new TypedFlatMap();
   		tfm.put("pessoa.nrIdentificacao", criteria.get("nrIdentificacao") + "%");
   		tfm.put("tpSituacao", criteria.get("tpSituacao"));
   		
    	List list = motoristaService.findLookupMotorista(tfm);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Motorista motorista = (Motorista)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMotorista", motorista.getIdMotorista());
    		typedFlatMap.put("tpIdentificacao", motorista.getPessoa().getTpIdentificacao());
    		typedFlatMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(motorista.getPessoa()));
    		typedFlatMap.put("nmPessoa", motorista.getPessoa().getNmPessoa());
    		typedFlatMap.put("tpVinculo", motorista.getTpVinculo());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupInstrutorMotorista(Map criteria) {
   		TypedFlatMap tfm = new TypedFlatMap();
   		tfm.put("pessoa.nrIdentificacao", criteria.get("nrIdentificacao") + "%");
   		tfm.put("tpSituacao", criteria.get("tpSituacao"));
   		
    	List list = motoristaService.findLookupInstrutorMotorista(tfm);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Motorista motorista = (Motorista)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMotorista", motorista.getIdMotorista());
    		typedFlatMap.put("tpIdentificacao", motorista.getPessoa().getTpIdentificacao());
    		typedFlatMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(motorista.getPessoa()));
    		typedFlatMap.put("nmPessoa", motorista.getPessoa().getNmPessoa());
    		typedFlatMap.put("tpVinculo", motorista.getTpVinculo());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupRotaColetaEntrega(Map criteria) {
    	Map mapFilial = new HashMap();
    	mapFilial.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	criteria.put("filial", mapFilial);
    	
    	List list = rotaColetaEntregaService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		RotaColetaEntrega rotaColetaEntrega = (RotaColetaEntrega)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idRotaColetaEntrega", rotaColetaEntrega.getIdRotaColetaEntrega());
    		typedFlatMap.put("nrRota", rotaColetaEntrega.getNrRota());
    		typedFlatMap.put("dsRota", rotaColetaEntrega.getDsRota());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupMeioTransporteTransportado(Map criteria) {
    	criteria.put("tipoMeioTransporte", "transportado");
    	return findLookupMeioTransporte(criteria);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupMeioTransporteSemiRebocado(Map criteria) {
    	String tpMeioTransporte = (String)criteria.get("tpMeioTransporte");
    	Long idTipoMeioTransporte = (Long)criteria.get("idTipoMeioTransporte");
    	
    	criteria.put("tipoMeioTransporte", "semiRebocado");
    	criteria.remove("tpMeioTransporte");
    	criteria.remove("idTipoMeioTransporte");

    	Map mapTipoMeioTransporte = new HashMap();
    	mapTipoMeioTransporte.put("tpMeioTransporte", tpMeioTransporte);
    	mapTipoMeioTransporte.put("idTipoMeioTransporte", idTipoMeioTransporte);
    	
    	Map mapModeloMeioTransporte = new HashMap();
    	mapModeloMeioTransporte.put("tipoMeioTransporte", mapTipoMeioTransporte);
    	
    	criteria.put("modeloMeioTransporte", mapModeloMeioTransporte);

    	return findLookupMeioTransporte(criteria);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
    private List findLookupMeioTransporte(Map criteria) {
    	List list = meioTransporteService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		MeioTransporte meioTransporte = (MeioTransporte)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
    		tfm.put("nrIdentificador", meioTransporte.getNrIdentificador());
    		tfm.put("nrFrota", meioTransporte.getNrFrota());
    		tfm.put("tpVinculo", meioTransporte.getTpVinculo().getValue());
    		tfm.put("tpMeioTransporte", meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getTpMeioTransporte().getValue());
    		tfm.put("idTipoMeioTransporte", meioTransporte.getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte());
    		tfm.put("nrRastreador", meioTransporte.getMeioTransporteRodoviario().getNrRastreador());
    		retorno.add(tfm);
    	}
    	return retorno;
    }
    

	@SuppressWarnings("rawtypes")
    public TypedFlatMap findDadosVeiculoColeta(TypedFlatMap criteria) {
		Long idMeioTransporte = criteria.getLong("idMeioTransporte");
		Long idRotaColetaEntrega = criteria.getLong("idRotaColetaEntrega");
		String tpControleCarga = criteria.getString("tpControleCarga"); 
		TypedFlatMap typedFlatMap = findDadosVeiculo(idMeioTransporte);
	
		boolean calculoPadrao = false;
		
		MeioTransporte meioTransporte =  meioTransporteService.findById(idMeioTransporte);
		
		solicitacaoContratacaoService.validateSolicitacaoMeioTransporteColetaEntrega(tpControleCarga, meioTransporte, SessionUtils.getFilialSessao());
		
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}
		
		if(calculoPadrao){	
			if(!"P".equals(meioTransporte.getTpVinculo().getValue())){
				Boolean possuiSolicitacao = solicitacaoContratacaoService.validateSolicitacaoValida(SessionUtils.getFilialSessao().getIdFilial(),meioTransporte.getNrIdentificador());
			
				if(Boolean.FALSE.equals(possuiSolicitacao)){
					throw new BusinessException("LMS-05411");
				}				
			}				
			
			String[] tipoOperacao = new String[] {"C","E","D","CE"};
			List<TabelaFcValores> tabelas = tabelaFreteCarreteiroCeService.findByIdFilial(SessionUtils.getFilialSessao().getIdFilial(),tipoOperacao);
			
			if(tabelas.isEmpty()){
				throw new BusinessException("LMS-05410");
			}
			typedFlatMap.put("padrao", true);
		}
		try {
			typedFlatMap.put("tabelas",tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaWithTabelaColetaEntrega(
					SessionUtils.getFilialSessao().getIdFilial(), idMeioTransporte,idRotaColetaEntrega));
			
			if(typedFlatMap.get("tabelas")!=null && !typedFlatMap.getList("tabelas").isEmpty()){
				typedFlatMap.put("tpCalculoTabelas", ((Map)typedFlatMap.getList("tabelas").get(0)).get("tpCalculo"));
			}
		} catch (BusinessException be) {
				throw be;
		}
		return typedFlatMap;
	}

	public TypedFlatMap findDadosVeiculoViagem(Long idMeioTransporte) {
		return findDadosVeiculo(idMeioTransporte);
	}


    private TypedFlatMap findDadosVeiculo(Long idMeioTransporte) {
    	getControleCargaService().validateVeiculoControleCarga(idMeioTransporte, false);

    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.putAll( findProprietarioVeiculo(idMeioTransporte) );

    	TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.findTipoMeioTransporteCompostoByIdMeioTransporte(idMeioTransporte);
    	if (tipoMeioTransporte != null) {
    		tfm.put("idTipoMeioTransporte", tipoMeioTransporte.getIdTipoMeioTransporte());
    	}
    	return tfm;
    }


	public void validateMeioTransporteSemiRebocado(Long idMeioTransporte) {
   		getControleCargaService().validateSemiReboqueControleCarga(idMeioTransporte);
	}

	
	public TypedFlatMap getVlAdiantamento(TypedFlatMap map) {
		BigDecimal pc = map.getBigDecimal("pcAdiantamentoFrete");
		BigDecimal vl = map.getBigDecimal("vlFreteCarreteiro");
		
		if (vl == null)
			throw new BusinessException("LMS-05094");

		Filial filial = SessionUtils.getFilialSessao();
		BigDecimal pcMaximo = filial.getPcFreteCarreteiro();
		if (pc.compareTo(pcMaximo) > 0) {
    		NumberFormat nf = DecimalFormat.getInstance(LocaleContextHolder.getLocale());
    		String strVlPercMaximo = nf.format(pcMaximo.doubleValue());
			throw new BusinessException("LMS-05131", new Object[]{strVlPercMaximo});
		}

		TypedFlatMap tfm = new TypedFlatMap();
		BigDecimal vlAdiantamento = null;
		if (pc != null && pc.compareTo(BigDecimalUtils.HUNDRED) <= 0 && pc.compareTo(BigDecimalUtils.ZERO) > 0) { 
			vlAdiantamento = pc.divide(BigDecimalUtils.HUNDRED).multiply(vl);
			vlAdiantamento = vlAdiantamento.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP);
		}
		tfm.put("vlAdiantamento", vlAdiantamento);
		return tfm;
	}

    private TypedFlatMap findNrTempoViagemBySolicitacaoContratacao(Long idRota, Long idSolicitacaoContratacao) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	if (idRota != null && idSolicitacaoContratacao != null) {
	    	Long nrTempoViagem = getControleCargaService().findNrTempoViagemBySolicitacaoContratacao(idRota, idSolicitacaoContratacao);
			tfm.put("nrTempoViagem", Integer.valueOf(nrTempoViagem.intValue()));
			tfm.put("hrTempoViagem", FormatUtils.converteMinutosParaHorasMinutos(nrTempoViagem, FormatUtils.ESCALA_HHH));
    	}
    	return tfm;
    }

    /**
     * 
     * @param criteria
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public TypedFlatMap findDadosSolicitacaoContratacao(TypedFlatMap criteria) {
		Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
		String tpRotaViagem = criteria.getString("tpRotaViagem");
		Long idRota = null;
		String tipoControleCarga = criteria.getString("tipoControleCarga");
		Boolean blControleCargaComSemiReboque = criteria.getBoolean("blControleCargaComSemiReboque");

		solicitacaoContratacaoService.validateExistSolicitacaoContratacao(SessionUtils.getFilialSessao().getIdFilial(), idSolicitacaoContratacao);
		SolicitacaoContratacao sc = solicitacaoContratacaoService.findById(idSolicitacaoContratacao);

		TypedFlatMap tfm = new TypedFlatMap();
		
		// LMSA-6520: LMSA-6534
		tfm.put("tpCargaCompartilhada", sc != null && sc.getTpCargaCompartilhada() != null ? sc.getTpCargaCompartilhada().getValue() : null);
		
		tfm.put("vlFreteNegociado", sc.getVlFreteNegociado());
		if (sc.getRotaIdaVolta() != null) {
			if (!tpRotaViagem.equals(sc.getRotaIdaVolta().getRotaViagem().getTpRota().getValue())) {
				String tipoRota = domainValueService.findDomainValueDescription(
						"DM_TIPO_ROTA_VIAGEM_CC", sc.getRotaIdaVolta().getRotaViagem().getTpRota().getValue()); 
				throw new BusinessException("LMS-05128", new Object[]{tipoRota});
			}

			// Busca os dados da rotaIdaVolta
	    	filialRotaService.validatePermissaoFilialUsuarioParaGerarControleCarga(sc.getRotaIdaVolta().getRota().getIdRota());
	    	tfm.put("rotaIdaVolta_idRotaIdaVolta", sc.getRotaIdaVolta().getIdRotaIdaVolta());
	    	tfm.put("rotaIdaVolta_nrRota", sc.getRotaIdaVolta().getNrRota());
	    	tfm.put("rota_dsRota", sc.getRotaIdaVolta().getRota().getDsRota());
	    	tfm.put("rota_idRota", sc.getRotaIdaVolta().getRota().getIdRota());
	    	tfm.put("rotaIdaVolta_rotaViagem_idRotaViagem", sc.getRotaIdaVolta().getRotaViagem().getIdRotaViagem());
	    	tfm.putAll( getTrechoRotaCompleta(sc.getRotaIdaVolta().getIdRotaIdaVolta()) );
	    	tfm.putAll( getMotoristaByRotaViagem(sc.getRotaIdaVolta().getIdRotaIdaVolta()) );
		}
		else
		if (sc.getRota() != null) {
			if (!tpRotaViagem.equals("EV")) {
				String tipoRota = domainValueService.findDomainValueDescription("DM_TIPO_ROTA_VIAGEM_CC", "EV"); 
				throw new BusinessException("LMS-05128", new Object[]{tipoRota});
			}
			filialRotaService.validatePermissaoFilialUsuarioParaGerarControleCarga(sc.getRota().getIdRota());
			tfm.put("rota_idRota", sc.getRota().getIdRota());
			tfm.put("rota_dsRota", sc.getRota().getDsRota());
			idRota = sc.getRota().getIdRota();
		}

		if (sc.getVlFreteNegociado() != null) {
			tfm.put("moedaPais_moeda_siglaSimbolo", sc.getMoedaPais().getMoeda().getSiglaSimbolo());
		}
		
		if ( !StringUtils.isBlank(sc.getNrIdentificacaoMeioTransp()) &&  StringUtils.isBlank(sc.getNrIdentificacaoSemiReboque())) {
			Map mapTransportado = new HashMap();
			mapTransportado.put("nrIdentificador", sc.getNrIdentificacaoMeioTransp());
			tfm.putAll( findMeioTransporte(mapTransportado, "Transportado") );
			tfm.putAll( findDadosVeiculo(tfm.getLong("idMeioTransporteTransportado")) );
		}

		if ( !StringUtils.isBlank(sc.getNrIdentificacaoSemiReboque()) ) {
			Map mapSemiRebocado = new HashMap();
			mapSemiRebocado.put("nrIdentificador", sc.getNrIdentificacaoSemiReboque());
			tfm.putAll( findMeioTransporte(mapSemiRebocado, "SemiRebocado") );
		}

		if ("V".equals(tipoControleCarga) && Boolean.TRUE.equals(!blControleCargaComSemiReboque)) {

			MeioTransporte meioTransporte = meioTransporteService.findMeioTransporteByIdentificacao(sc.getNrIdentificacaoMeioTransp());
			Map mapTransportado = new HashMap();

			mapTransportado.put("nrIdentificador", sc.getNrIdentificacaoMeioTransp());
			tfm.putAll( findMeioTransporte(mapTransportado, "Transportado") );
			tfm.putAll( findDadosVeiculo(meioTransporte.getIdMeioTransporte()));
		}

		tfm.putAll( findNrTempoViagemBySolicitacaoContratacao(idRota, idSolicitacaoContratacao) );
		return tfm;
	}

	public TypedFlatMap existeIdentificacaoMeioTranspIdentificacaoSemiReboque(TypedFlatMap criteria) {

		Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
		TypedFlatMap resultado = new TypedFlatMap();
		Boolean blIdentificacaoMeioTranspIdentificacaoSemiReboque = Boolean.FALSE;

		SolicitacaoContratacao sc = solicitacaoContratacaoService.findById(idSolicitacaoContratacao);

		if(sc != null && sc.getNrIdentificacaoMeioTransp() != null && sc.getNrIdentificacaoSemiReboque() != null) {
			blIdentificacaoMeioTranspIdentificacaoSemiReboque = Boolean.TRUE;
		}

		resultado.put("blIdentificacaoMeioTranspIdentificacaoSemiReboque", blIdentificacaoMeioTranspIdentificacaoSemiReboque);

		return resultado;

	}

	/**
	 * 
	 * @param criteria
	 * @param tipo
	 */
	@SuppressWarnings("rawtypes")
    private TypedFlatMap findMeioTransporte(Map criteria, String tipo) {
    	List list = meioTransporteService.find(criteria);
    	if (list.isEmpty())
    		return new TypedFlatMap();

    	MeioTransporte meioTransporte = (MeioTransporte)list.get(0);
    	TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idMeioTransporte" + tipo, meioTransporte.getIdMeioTransporte());
		tfm.put("nrIdentificador" + tipo, meioTransporte.getNrIdentificador());
		tfm.put("nrFrota" + tipo, meioTransporte.getNrFrota());
		tfm.put("tpVinculo" + tipo, meioTransporte.getTpVinculo().getValue());
		tfm.put("nrRastreador" + tipo, meioTransporte.getMeioTransporteRodoviario().getNrRastreador());
		
		MeioTransporte mt = meioTransporteService.findById(meioTransporte.getIdMeioTransporte());
		tfm.put("tpMeioTransporte" + tipo, mt.getModeloMeioTransporte().getTipoMeioTransporte().getTpMeioTransporte().getValue());
		return tfm;
    }

	/**
	 * 
	 * @param vlFreteCarreteiro
	 * @param sgMoeda
	 * @param dsSimbolo
	 */
    private TypedFlatMap getVlFreteCarreteiro(BigDecimal vlFreteCarreteiro, String sgMoeda, String dsSimbolo) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	
    	tfm.put("moedaVlFreteCarreteiro", sgMoeda + " " + dsSimbolo);
    	if (vlFreteCarreteiro != null)
    		tfm.put("vlFreteCarreteiro", vlFreteCarreteiro);
    	else
    		tfm.put("vlFreteCarreteiro", BigDecimalUtils.ZERO);

    	return tfm;
    }

	
    /**
     * Consulta trecho correspondente a rota completa
     * 
     * @param idRotaIdaVolta
     * @return
     */
    private TypedFlatMap getTrechoRotaCompleta(Long idRotaIdaVolta) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	TrechoRotaIdaVolta triv = trechoRotaIdaVoltaService.findByTrechoRotaCompleta(idRotaIdaVolta);
		tfm.put("hrPrevisaoSaida", triv.getHrSaida());
		tfm.put("nrTempoViagem", triv.getNrTempoViagem());

		Integer nrTempo = rotaViagemService.findMaiorTempoViagemOfRota(idRotaIdaVolta);
		tfm.put("hrTempoViagem", FormatUtils.converteMinutosParaHorasMinutos(Long.valueOf(nrTempo.longValue()), FormatUtils.ESCALA_HHH));

		final int horasTempoLimite = ((BigDecimal)configuracoesFacade.getValorParametro("TEMPO_LIMITE_ROTA_DO_DIA")).intValue();
		YearMonthDay dtSaidaRota = getControleCargaService().validateRotaControleCarga(idRotaIdaVolta, horasTempoLimite);
		DateTime dhPrevisaoSaida = triv.getHrSaida() == null ? null : dtSaidaRota.toDateTime(triv.getHrSaida(), JTDateTimeUtils.getUserDtz());

		tfm.put("dhPrevisaoSaida", dhPrevisaoSaida);
    	return tfm;
    }
    

    @SuppressWarnings("rawtypes")
    private TypedFlatMap getMotoristaByRotaViagem(Long idRotaIdaVolta) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	List lista = motoristaRotaViagemService.findByIdRotaIdaVolta(idRotaIdaVolta);
    	if (lista.size() == 1) {
    		Motorista motorista = ((MotoristaRotaViagem)lista.get(0)).getMotorista();
    		tfm.put("motorista_idMotorista", motorista.getIdMotorista());
    		tfm.put("motorista_pessoa_nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(motorista.getPessoa()));
    		tfm.put("motorista_pessoa_nmPessoa", motorista.getPessoa().getNmPessoa());
    		tfm.put("motorista_tpVinculo", motorista.getTpVinculo().getValue());
    	}
    	return tfm;
    }

    /**
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedPontosParada(TypedFlatMap criteria) {
    	Long idRotaIdaVolta = criteria.getLong("idRotaIdaVolta");
    	List lista = pontoParadaTrechoService.findToGerarControleCarga(idRotaIdaVolta);
    	
    	List retorno = new ArrayList();
    	TypedFlatMap mapRetorno = null;
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
    		TypedFlatMap map = (TypedFlatMap)iter.next();
    		mapRetorno = new TypedFlatMap();
    		mapRetorno.put("trecho", map.getString("sgFilialOrigem") + " - " + map.getString("sgFilialDestino"));
    		mapRetorno.put("rodovia", (map.getString("sgRodovia") == null ? "" : map.getString("sgRodovia")) + 
    				(map.getString("dsRodovia") == null ? "" : (" - " + map.getString("dsRodovia"))) );
    		mapRetorno.put("nrKm", map.getInteger("nrKm"));
    		mapRetorno.put("nmMunicipio", map.getString("nmMunicipio"));
    		mapRetorno.put("sgUf", map.getString("sgUf"));
    		mapRetorno.put("hrTempoParada", 
    				map.getInteger("nrTempoParada") == null ? null : JTFormatUtils.formatTime((map.getInteger("nrTempoParada").longValue() * 60), 2, 1));
    		mapRetorno.put("nrLatitude", map.getBigDecimal("nrLatitude"));
    		mapRetorno.put("nrLongitude", map.getBigDecimal("nrLongitude"));
    		mapRetorno.put("idPontoParadaTrecho", map.getLong("idPontoParadaTrecho"));
    		retorno.add(mapRetorno);
    	}
    	return new ResultSetPage(Integer.valueOf(1), retorno);
    }


    /**
     * 
     * @param criteria
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public ResultSetPage findPaginatedMotivosParada(TypedFlatMap criteria) {
		List lista = motivoParadaService.findByIdPontoParadaTrecho(criteria.getLong("idPontoParadaTrecho"));
		return new ResultSetPage(Integer.valueOf(1), lista); 
	}


	
	private void validaDadosTela(String tpControleCarga, String tpRotaViagem, String tpVinculo, Long idRotaViagem, 
			Long idMeioTransporte, Long idSolicitacaoContratacao, Boolean blEntregaDireta, Long idTipoTabelaColetaEntrega) 
	{
		if (tpControleCarga.equals("C") && !blEntregaDireta && idMeioTransporte != null && idTipoTabelaColetaEntrega == null){
			throw new BusinessException("LMS-05321");
		}
		if (tpControleCarga.equals("V") && (tpRotaViagem.equals("EX") || tpRotaViagem.equals("EC"))) {
			if (tpVinculo != null && tpVinculo.equals("A")) {
				// Verifica se existe um veiculo cadastrado para a rota de viagem.
				if (!meioTransporteRotaViagemService.validateMeioTransporteWithRotaViagem(idRotaViagem, idMeioTransporte) && idSolicitacaoContratacao == null) {
					throw new BusinessException("LMS-05166");
			    }
			}
		}
	}
	

    
	/************************************************************************************
							INICIO - ControleTrecho
	************************************************************************************/
    /**
     * Salva um item na sessao.
     * 
     * @param bean
     * @return
     */
    public Serializable saveControleTrecho(TypedFlatMap criteria) {
    	return saveItemInstance(criteria, "trechos");
    }


    /**
     * Faz o findPaginated do filho
     * Possui uma chamada 'interna' para o findPaginated(initialize) contido dentro do 'createMasterConfig'
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findPaginatedControleTrecho(TypedFlatMap parameters) {
    	Long idControleCarga = parameters.getLong("idControleCarga");
		Long idRotaIdaVolta = parameters.getLong("idRotaIdaVolta");
		Long idRota = parameters.getLong("idRota");
		DateTime dhPrevisaoSaida = parameters.getDateTime("dhPrevisaoSaida");

    	inicializaDadosControleTrecho(Boolean.TRUE, idControleCarga, idRotaIdaVolta, idRota, dhPrevisaoSaida);

		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("trechos");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("trechos");

		TypedFlatMap map = null;
		List listRetorno = new ArrayList();
    	for (Iterator iter = itemList.iterator(null, itemListConfig); iter.hasNext();) {
    		ControleTrecho controleTrecho = (ControleTrecho)iter.next();
			map = new TypedFlatMap();
			map.put("idControleTrecho", controleTrecho.getIdControleTrecho());
			map.put("filialByIdFilialOrigem_sgFilial", controleTrecho.getFilialByIdFilialOrigem().getSgFilial());
			map.put("filialByIdFilialDestino_sgFilial", controleTrecho.getFilialByIdFilialDestino().getSgFilial());
			map.put("dhPrevisaoSaida", controleTrecho.getDhPrevisaoSaida());
			map.put("nrDistancia", controleTrecho.getNrDistancia());
			map.put("hrTempoViagem", controleTrecho.getNrTempoViagem() == null ? null : JTFormatUtils.formatTime((controleTrecho.getNrTempoViagem().longValue() * 60), 2, 1));
			map.put("hrTempoOperacao", controleTrecho.getNrTempoOperacao() == null ? null : JTFormatUtils.formatTime((controleTrecho.getNrTempoOperacao().longValue() * 60), 2, 1));
			map.put("vlRateio", controleTrecho.getTrechoRotaIdaVolta() != null ? BigDecimalUtils.defaultBigDecimal(controleTrecho.getTrechoRotaIdaVolta().getVlRateio()) : null);
			listRetorno.add(map);
    	}
    	return listRetorno;
    }

    
    /**
     * Faz o getRowCount do filho
     * Possui uma chamada 'interna' para o getRowCount contido dentro do 'createMasterConfig'
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Integer getRowCountControleTrecho(Map parameters){
    	return getRowCountItemList(parameters, "trechos");
    }

    /**
     * 
     * @param validaInicializacao
     * @param idControleCarga
     * @param idRotaIdaVolta
     * @param idRota
     * @param dhPrevisaoSaida
     */
	@SuppressWarnings("rawtypes")
    private void inicializaDadosControleTrecho(Boolean validaInicializacao, Long idControleCarga, Long idRotaIdaVolta, Long idRota, DateTime dhPrevisaoSaida) {
		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("trechos");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("trechos");

    	if (!validaInicializacao.booleanValue() || !itemList.isInitialized()) {
    		List lista = controleTrechoService.findTrechosByTrechosRota(getValorIdControleCarga(idControleCarga), idRotaIdaVolta, idRota, dhPrevisaoSaida, null, null, null);
    		populateItemList(lista, masterEntry, itemList, itemListConfig);
    	}
	}

	/**
	 * 
	 * @param parameters
	 */
	public void resetDataByTrechosByViagem(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga"); 
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("idControleCarga", idControleCarga);
		map.put("alias", "trechos"); 
		resetDataTab(map);

		Long idRotaIdaVolta = parameters.getLong("idRotaIdaVolta");
		Long idRota = parameters.getLong("idRota");
		DateTime dhPrevisaoSaida = parameters.getDateTime("dhPrevisaoSaida");
		inicializaDadosControleTrecho(Boolean.FALSE, idControleCarga, idRotaIdaVolta, idRota, dhPrevisaoSaida);
	}

	/************************************************************************************
	 							FIM - ControleTrecho
	************************************************************************************/
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public Map store(TypedFlatMap parameters) {
		Long idControleCarga = (Long)parameters.get("idControleCarga");
    	MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
    	ControleCarga controleCarga = (ControleCarga) masterEntry.getMaster();
		Long idSolicitacaoContratacao = parameters.get("idSolicitacaoContratacao") != null
				? (Long)parameters.get("idSolicitacaoContratacao") : null;

    	boolean calculoPadrao = false;
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}

    	if(!calculoPadrao){
    		validaDadosTela((String)parameters.get("tpControleCarga"), (String)parameters.get("tpRotaViagem"), 
    				(String)parameters.get("tpVinculo"), (Long)parameters.get("idRotaViagemByRotaIdaVolta"), 
    				(Long)parameters.get("idMeioTransporteTransportado"), (Long)parameters.get("idSolicitacaoContratacao"),
    				(Boolean)parameters.get("blEntregaDireta"),(Long)parameters.get("idTabelaColetaEntrega"));    	
    	}

    	ItemList itemListIntegranteEqOperac = getItemsFromSession(masterEntry, "integrantes");
    	ItemList itemListPagtoPedagioCc = getItemsFromSession(masterEntry, "pagamentos");
    	ItemList itemListPostoPassagemCc = getItemsFromSession(masterEntry, "postos");
    	ItemList itemListTrechos = getItemsFromSession(masterEntry, "trechos");
    	ItemList itemListTrechoCorporativo = getItemsFromSession(masterEntry, "trechoCorporativo");
    	ItemList itemListAdiantamentoTrecho = getItemsFromSession(masterEntry, "adiantamentoTrecho");
    	ItemListConfig itemListConfigIntegranteEqOperac = getMasterConfig().getItemListConfig("integrantes");
    	ItemListConfig itemListConfigPagtoPedagioCc = getMasterConfig().getItemListConfig("pagamentos");
    	ItemListConfig itemListConfigPostoPassagemCc = getMasterConfig().getItemListConfig("postos");
    	ItemListConfig itemListConfigTrechos = getMasterConfig().getItemListConfig("trechos");
    	ItemListConfig itemListConfigTrechoCorporativo = getMasterConfig().getItemListConfig("trechoCorporativo");
    	ItemListConfig itemListConfigAdiantamentoTrecho = getMasterConfig().getItemListConfig("adiantamentoTrecho");

    	inicializaItemLists(idControleCarga, parameters, masterEntry, 
							itemListIntegranteEqOperac, itemListConfigIntegranteEqOperac, 
							itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc,
							itemListPostoPassagemCc, itemListConfigPostoPassagemCc, 
							itemListTrechos, itemListConfigTrechos);

    	updateMasterInSession(masterEntry);
    	
    	saveAdiantamentoTrecho(parameters);

    	ControleCarga newControleCarga = new ControleCarga();
    	
    	try {
    		List<TypedFlatMap> manifestosFedexSegundoCarregamento = parameters.getList("manifestosFedexSegundoCarregamento");
    	    
	    	newControleCarga = (ControleCarga) getControleCargaService().storeGerarControleCarga(
	    	        mapToTypedFlatMap(parameters), controleCarga,
					itemListIntegranteEqOperac, itemListConfigIntegranteEqOperac,
					itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc,
					itemListPostoPassagemCc, itemListConfigPostoPassagemCc,
					itemListTrechos, itemListConfigTrechos, (String)parameters.get("tpBeneficiarioAdiantamento"),
					itemListTrechoCorporativo, itemListConfigTrechoCorporativo,  
					itemListAdiantamentoTrecho, itemListConfigAdiantamentoTrecho
					// LMSA-6520: LMSA-6534
					, parameters.getBoolean("cargaCompartilhadaSegundoCarregamento")
					, manifestosFedexSegundoCarregamento
					);

			if(idSolicitacaoContratacao != null && newControleCarga.getMeioTransporteByIdTransportado() == null
					&& newControleCarga.getMeioTransporteByIdSemiRebocado() != null
					&& new DomainValue("V").equals(newControleCarga.getTpControleCarga())) {

				criaAdiantamentoParaControleCargaComSemiReboque(newControleCarga);
			}
    	} catch (BusinessException e) {
    		// Quando ocorre o bloqueio do meio de transporte é necessário armazenar a informação do bloqueio.
			if ("LMS-26044".equals(e.getMessageKey()) && e.getMessageArguments() != null) {
    			Object[] args = e.getMessageArguments();
    			getControleCargaService().storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
    		}
			// Caso contrário passa quem sabe tratar o erro.
    		throw e;
    	}

    	itemListIntegranteEqOperac.resetItemsState(); 
    	itemListPagtoPedagioCc.resetItemsState(); 
    	itemListPostoPassagemCc.resetItemsState(); 
    	itemListTrechos.resetItemsState(); 
    	updateMasterInSession(masterEntry);

    	Map mapRetorno = new HashMap();
    	mapRetorno.put("idControleCarga", newControleCarga.getIdControleCarga());
    	mapRetorno.put("nrControleCarga", newControleCarga.getNrControleCarga());
    	mapRetorno.put("filialByIdFilialOrigem_idFilial", newControleCarga.getFilialByIdFilialOrigem().getIdFilial());
    	mapRetorno.put("filialByIdFilialOrigem_sgFilial", newControleCarga.getFilialByIdFilialOrigem().getSgFilial());
    	return mapRetorno;
	}


	private void inicializaTabAdiantamentoTrecho(	Long idControleCarga, 
													String tpControleCarga, 
													Long idMotorista, 
													Long idSolicitacaoContratacao, 
													String tpVinculo) 
	{
		if (tpControleCarga.equals("V") && idMotorista == null) {
			if (tpVinculo != null && tpVinculo.equals("E") && idSolicitacaoContratacao != null) {
				populateAdiantamentoTrecho(idControleCarga, idSolicitacaoContratacao);
			}
		}
	}


    /**
     * 
     * @param idControleCarga
     * @param parameters
     * @param itemListIntegranteEqOperac
     * @param itemListPagtoPedagioCc
     * @param itemListPostoPassagemCc
     */
    @SuppressWarnings("rawtypes")
    private void inicializaItemLists(Long idControleCarga, 
    								 Map parameters,
    								 MasterEntry masterEntry,
    								 ItemList itemListIntegranteEqOperac, 
    								 ItemListConfig itemListConfigIntegranteEqOperac, 
    								 ItemList itemListPagtoPedagioCc, 
    								 ItemListConfig itemListConfigPagtoPedagioCc, 
    								 ItemList itemListPostoPassagemCc,
    								 ItemListConfig itemListConfigPostoPassagemCc,
    								 ItemList itemListTrechos,
    								 ItemListConfig itemListConfigTrechos) 
 
    {
    	Long idSolicitacaoContratacao = (Long)parameters.get("idSolicitacaoContratacao");
    	Long idRotaIdaVolta = (Long)parameters.get("idRotaIdaVolta");
    	Long idRota = (Long)parameters.get("idRota");
    	Long idRotaColetaEntrega = (Long)parameters.get("idRotaColetaEntrega");
    	Long idMeioTransporteTransportado = (Long)parameters.get("idMeioTransporteTransportado");
    	Long idMeioTransporteSemiRebocado = (Long)parameters.get("idMeioTransporteSemiRebocado");
    	String tpControleCarga = (String)parameters.get("tpControleCarga");
    	DateTime dhPrevisaoSaida = (DateTime)parameters.get("dhPrevisaoSaida");
    	
    	inicializaTabAdiantamentoTrecho(idControleCarga, tpControleCarga, 
				(Long)parameters.get("idMotorista"), idSolicitacaoContratacao, (String)parameters.get("tpVinculo"));

    	// Se a rota de viagem foi informada ou a solicitacao de contratacao
    	if (idRotaIdaVolta != null || idRota != null) {
    		if (!itemListTrechos.isInitialized()) {
	    		String tpRotaViagem = (String)parameters.get("tpRotaViagem");
	    		List listTrechos = null;
	    		if (idSolicitacaoContratacao != null || tpRotaViagem.equals("EV")) {
	    			listTrechos = controleTrechoService.findTrechosByTrechosRota(
	    					getValorIdControleCarga(idControleCarga), null, idRota, null, null, null, null);
	    		}
	    		else
	    		if (tpRotaViagem.equals("EX") || tpRotaViagem.equals("EC")) {
	    			listTrechos = controleTrechoService.findTrechosByTrechosRota(
	    					getValorIdControleCarga(idControleCarga), idRotaIdaVolta, null, dhPrevisaoSaida, null, null, null);
	    		}

	    		populateItemList(listTrechos, masterEntry, itemListTrechos, itemListConfigTrechos);
    		}
    	}

    	if ((idRotaIdaVolta != null || idRotaColetaEntrega != null || idRota != null) && idMeioTransporteTransportado != null) {
			if (!itemListPagtoPedagioCc.isInitialized()) {
				itemListPagtoPedagioCc.initialize(Collections.EMPTY_LIST);
			}

			if (!itemListPostoPassagemCc.isInitialized()) {
				List listaPostoPassagemCc = null;
				if (tpControleCarga.equals("C")) {
					listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByColetaEntrega(
							getValorIdControleCarga(idControleCarga), idMeioTransporteTransportado, idMeioTransporteSemiRebocado, idRotaColetaEntrega, null);
				}
				else
				if (tpControleCarga.equals("V")) {
					List filiaisRota = filialRotaService.findFiliaisRota(idRotaIdaVolta, idRota);
					listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByViagem(getValorIdControleCarga(idControleCarga), 
							idMeioTransporteTransportado, idMeioTransporteSemiRebocado, filiaisRota, Boolean.TRUE, null);
				}
				populateItemList(listaPostoPassagemCc, masterEntry, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);

				if (listaPostoPassagemCc != null && !listaPostoPassagemCc.isEmpty()) {
					atualizaDadosPostosNaSessao(idControleCarga, parameters, masterEntry, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
					atualizaDadosPagtosNaSessao(idControleCarga, null, masterEntry, 
						itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
				}
			}
			else {
				atualizaDadosPostosNaSessao(idControleCarga, parameters, masterEntry, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
				if (!itemListPagtoPedagioCc.isEmpty() || !itemListPostoPassagemCc.isEmpty()) {
					atualizaDadosPagtosNaSessao(idControleCarga, (List)parameters.get("pagamentos"), masterEntry, 
						itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
				}
			}
    	}
    }

    public void newMaster() {
    	super.newMaster();
    	ControleCarga controleCarga = new ControleCarga();
    	controleCarga.setIdControleCarga(Long.valueOf(-1));
    	putMasterInSession(controleCarga);
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map findPostosPagamentosByVeiculo(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	Long idRotaIdaVolta = criteria.getLong("idRotaIdaVolta");
    	Long idRota = criteria.getLong("idRota");
    	Long idRotaColetaEntrega = criteria.getLong("idRotaColetaEntrega");
    	Long idMeioTransporteTransportado = criteria.getLong("idMeioTransporteTransportado");
    	Long idMeioTransporteSemiRebocado = criteria.getLong("idMeioTransporteSemiRebocado");
    	String tpControleCarga = criteria.getString("tpControleCarga");
    	Boolean blInicializaPostos = criteria.getBoolean("blInicializaPostos");
    	resetTabPostos(criteria);

    	if (blInicializaPostos != null && blInicializaPostos) {     	
	    	MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
	    	ItemList itemListPagtoPedagioCc = getItemsFromSession(masterEntry, "pagamentos");
	    	ItemList itemListPostoPassagemCc = getItemsFromSession(masterEntry, "postos");
	    	ItemListConfig itemListConfigPagtoPedagioCc = getMasterConfig().getItemListConfig("pagamentos");
	    	ItemListConfig itemListConfigPostoPassagemCc = getMasterConfig().getItemListConfig("postos");
	    	if (itemListPagtoPedagioCc.isInitialized()) {
		    	List listaPostoPassagemCc = null;
				if (tpControleCarga.equals("C"))
					listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByColetaEntrega(
							idControleCarga,idMeioTransporteTransportado,idMeioTransporteSemiRebocado, idRotaColetaEntrega, null);
				else 
				if (tpControleCarga.equals("V")) {
					List filiaisRota = filialRotaService.findFiliaisRota(idRotaIdaVolta, idRota);
					listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByViagem(
							idControleCarga, idMeioTransporteTransportado, idMeioTransporteSemiRebocado, filiaisRota, Boolean.TRUE, null);
				}
				populateListaPostoPassagemCc(listaPostoPassagemCc, masterEntry, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
	
				if (listaPostoPassagemCc != null && !listaPostoPassagemCc.isEmpty() && itemListPagtoPedagioCc.isEmpty()) {
					atualizaDadosPagtosNaSessao(idControleCarga, null, masterEntry, 
							itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
				}
	    	}
		}

    	Map mapRetorno = new HashMap();
    	if (blInicializaPostos == null || !blInicializaPostos) {
    		mapRetorno.put("blDesabilitaTab", true);
    	}
    	else {
			List listaPostoPassagemCc = Collections.EMPTY_LIST;
			if (tpControleCarga.equals("C")) {
				listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByColetaEntrega(
						getValorIdControleCarga(idControleCarga), idMeioTransporteTransportado, idMeioTransporteSemiRebocado, idRotaColetaEntrega, null);
			}
			else
			if (tpControleCarga.equals("V")) {
				List filiaisRota = filialRotaService.findFiliaisRota(idRotaIdaVolta, idRota);
				listaPostoPassagemCc = postoPassagemCcService.findPostoPassagemCcByViagem(getValorIdControleCarga(idControleCarga), 
						idMeioTransporteTransportado, idMeioTransporteSemiRebocado, filiaisRota, Boolean.TRUE, null);
			}
    		mapRetorno.put("blDesabilitaTab", listaPostoPassagemCc.isEmpty());
    	}
    	return mapRetorno;
	}
    
    
    
    
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupEmpresa(Map criteria) {
		if ( !StringUtils.isBlank((String)criteria.get("nrIdentificacao")) ) {
			Map mapPessoa = new HashMap();
			mapPessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));
			criteria.put("pessoa", mapPessoa);
		}
    	List list = empresaService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Empresa empresa = (Empresa)iter.next();
    		Map map = new HashMap();
    		map.put("idEmpresa", empresa.getIdEmpresa());
    		map.put("nmPessoa", empresa.getPessoa().getNmPessoa());
    		map.put("nrIdentificacao", FormatUtils.formatIdentificacao(empresa.getPessoa()) );
    		map.put("tpIdentificacao", empresa.getPessoa().getTpIdentificacao().getValue() );
    		retorno.add(map);
    	}
    	return retorno;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupPrestadorServico(Map criteria) {
    	Map mapPessoa = new HashMap();
		mapPessoa.put("nrIdentificacao", PessoaUtils.clearIdentificacao((String)criteria.get("nrIdentificacao")));
		mapPessoa.put("tpIdentificacao", criteria.get("tpIdentificacao"));
		mapPessoa.put("nmPessoa", criteria.get("nmPessoa"));
		mapPessoa.put("tpPessoa", "F");
		criteria.put("pessoa", mapPessoa);

		criteria.remove("nrIdentificacao");
		criteria.remove("tpIdentificacao");
		criteria.remove("nmPessoa");
    	List result = prestadorServicoService.findLookup(criteria);

    	List lista = new ArrayList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			PrestadorServico ps = (PrestadorServico)iter.next();
			Map map = new HashMap();
			map.put("idPrestadorServico", ps.getIdPrestadorServico());
			map.put("tpIdentificacao", ps.getPessoa().getTpIdentificacao());
			map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(ps.getPessoa()) );
			map.put("nmPessoa", ps.getPessoa().getNmPessoa());
			map.put("tpSituacao", ps.getTpSituacao());
			lista.add(map);
		}
		return lista;
    }

    @SuppressWarnings("rawtypes")
    public List findLookupUsuarioFuncionario(TypedFlatMap tfm){
    	String nrMatricula = tfm.getString("nrMatricula");
    	if (!StringUtils.isBlank(nrMatricula)){
    		nrMatricula = StringUtils.leftPad(nrMatricula, 9, '0');
    	}
    	return usuarioService.findLookupUsuarioFuncionario(
    			tfm.getLong("idUsuario"), nrMatricula, null, null, null, null, true);
    }

    @SuppressWarnings("rawtypes")
    public List findMoeda(Map criteria) {
    	FilterList filter = new FilterList(moedaService.find(criteria)) {
			public Map filterItem(Object item) {
				Moeda moeda = (Moeda)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idMoeda", moeda.getIdMoeda());
	    		typedFlatMap.put("siglaSimbolo", moeda.getSiglaSimbolo());
		    	typedFlatMap.put("sgMoeda", moeda.getSgMoeda());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findCargoOperacional(Map criteria) {
    	List list = cargoOperacionalService.findCargo(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		CargoOperacional cargoOperacional = (CargoOperacional)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idCargoOperacional", cargoOperacional.getIdCargoOperacional());
    		typedFlatMap.put("dsCargo", cargoOperacional.getDsCargo());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupEquipe(Map criteria) {
    	Map mapFilial = new HashMap();
    	mapFilial.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	
    	criteria.put("filial", mapFilial);
    	return equipeService.findLookup(criteria);
    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map findCartaoPedagio(TypedFlatMap criteria) {
    	Map retorno = new HashMap();
    	List resultado = cartaoPedagioService.findCartaoPedagioByOperadora(
    			criteria.getLong("idOperadoraCartaoPedagio"), criteria.getLong("nrCartao"), Boolean.FALSE);

    	if (!resultado.isEmpty()) {
    		CartaoPedagio cartaoPedagio = (CartaoPedagio)resultado.get(0);
    		cartaoPedagioService.validateDtValidadeByIdCartaoPedagio(cartaoPedagio.getIdCartaoPedagio());
    		retorno.put("idCartaoPedagio", cartaoPedagio.getIdCartaoPedagio());
    	}
		return retorno;
    }
    

    /**
     * 
     * @param idMeioTransporte
     * @param mapRetorno
     */
    @SuppressWarnings("rawtypes")
    public TypedFlatMap findProprietarioVeiculo(Long idMeioTransporte) {
    	TypedFlatMap tfm = new TypedFlatMap();

    	Map mapResultado = meioTranspProprietarioService.findProprietarioByMeioTransporte(idMeioTransporte);
    	if (mapResultado != null) {
	    	Map mapProprietario = (Map)mapResultado.get("proprietario");
	    	if (mapProprietario != null) {
	    		tfm.put("proprietario_idProprietario", mapProprietario.get("idProprietario"));
	    		Map mapPessoa = (Map)mapProprietario.get("pessoa");
	    		if (mapPessoa != null) {
	    			tfm.put("proprietario_pessoa_nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(
		    			(String)((Map)mapPessoa.get("tpIdentificacao")).get("value"), (String)mapPessoa.get("nrIdentificacao"))); 
		    		tfm.put("proprietario_pessoa_nmPessoa", mapPessoa.get("nmPessoa"));
		    	}
		    }
    	}
    	return tfm;
    }

    /**
     * 
     * @param parameters
     */
	public void resetDataTab(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
		String alias = parameters.getString("alias"); 

		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
    	ItemList itemList = getItemsFromSession(masterEntry, alias);
    	ItemListConfig itemListConfig = getMasterConfig().getItemListConfig(alias);
    	
    	if (itemList.isInitialized()) {
    		removeAllItemList(idControleCarga, itemList, itemListConfig);
    	}
    	updateMasterInSession(masterEntry);
	}

	
    /**
     * 
     * @param idControleCarga
     * @param masterEntry
     * @param itemList
     * @param itemListConfig
     * @param lista
     */
    @SuppressWarnings("rawtypes")
    public void resetaAtualizaItemList(Long idControleCarga, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig, List lista) {
		for (Iterator iter = itemList.iterator(idControleCarga, itemListConfig); iter.hasNext();) {
			iter.next();
			iter.remove();
		}
		populateItemList(lista, masterEntry, itemList, itemListConfig);
	}

    /**
     * 
     * @param lista
     * @param masterEntry
     * @param itemList
     * @param itemListConfig
     */
    @SuppressWarnings("rawtypes")
    private void populateItemList(List lista, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig) {
    	if (lista.isEmpty()) {
    		itemList.initialize(Collections.EMPTY_LIST);
    	}
    	else {
			for (Iterator iter = lista.iterator(); iter.hasNext();) {
				itemList.addItem(iter.next(), itemListConfig);
			}
    	}
		updateMasterInSession(masterEntry);
	}

    
    /**
     * 
     * @param idControleCarga
     * @param itemList
     * @param itemListConfig
     */
    @SuppressWarnings("rawtypes")
    public void removeAllItemList(Long idControleCarga, ItemList itemList, ItemListConfig itemListConfig) {
		for (Iterator iter = itemList.iterator(idControleCarga, itemListConfig); iter.hasNext();) {
			iter.next();
			iter.remove();
		}
	}
    
    /**
     * 
     * @param idControleCarga
     * @return
     */
    public Long getValorIdControleCarga(Long idControleCarga) {
    	if (idControleCarga != null && idControleCarga.compareTo(Long.valueOf(0)) > 0)
    		return idControleCarga;
    	return null;
    }

    
	@SuppressWarnings("rawtypes")
    private TypedFlatMap mapToTypedFlatMap(Map map) {
		TypedFlatMap tfm = new TypedFlatMap();
		
		tfm.put("rota.idRota", map.get("idRota"));
    	tfm.put("rotaIdaVolta.idRotaIdaVolta", map.get("idRotaIdaVolta"));
    	tfm.put("rotaColetaEntrega.idRotaColetaEntrega", map.get("idRotaColetaEntrega"));
    	tfm.put("idEquipe", map.get("idEquipe"));
		tfm.put("meioTransporteByIdTransportado.idMeioTransporte", map.get("idMeioTransporteTransportado"));
		tfm.put("meioTransporteByIdSemiRebocado.idMeioTransporte", map.get("idMeioTransporteSemiRebocado"));
		tfm.put("motorista.idMotorista", map.get("idMotorista"));
		tfm.put("instrutorMotorista.idInstrutorMotorista", map.get("idInstrutorMotorista"));
		tfm.put("proprietario.idProprietario", map.get("idProprietario"));
		tfm.put("tpControleCarga", new DomainValue((String)map.get("tpControleCarga")) );

		tfm.put("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", map.get("idTipoTabelaColetaEntrega"));
		tfm.put("tabelaColetaEntrega.idTabelaColetaEntrega", map.get("idTabelaColetaEntrega"));
		tfm.put("solicitacaoContratacao.idSolicitacaoContratacao", map.get("idSolicitacaoContratacao"));
		
		if (map.get("tpRotaViagem") != null) {
			tfm.put("tpRotaViagem", new DomainValue((String)map.get("tpRotaViagem")) );
		}

		tfm.put("vlFreteCarreteiro", map.get("vlFreteCarreteiro"));
		tfm.put("dhPrevisaoSaida", map.get("dhPrevisaoSaida"));
		tfm.put("nrTempoViagem", map.get("nrTempoViagem"));
		tfm.put("blEntregaDireta", map.get("blEntregaDireta"));

		tfm.put("solicitacaoSinal.nmEmpresaAnterior", map.get("solicitacaoSinal.nmEmpresaAnterior"));
		tfm.put("solicitacaoSinal.nrTelefoneEmpresa", map.get("solicitacaoSinal.nrTelefoneEmpresa"));
		tfm.put("solicitacaoSinal.nmResponsavelEmpresa", map.get("solicitacaoSinal.nmResponsavelEmpresa"));
		tfm.put("solicitacaoSinal.blPertenceProjCaminhoneiro", map.get("solicitacaoSinal.blPertenceProjCaminhoneiro"));

		Map mapAdiantamento = (Map)map.get("adiantamento");
		if (mapAdiantamento != null) {
			Double dbValor = (Double)mapAdiantamento.get("pcAdiantamentoFrete");
			BigDecimal vlPc = dbValor == null ? null :new BigDecimal(dbValor.doubleValue());
			tfm.put("adiantamento.pcAdiantamentoFrete", vlPc);
			tfm.put("adiantamento.vlBruto", mapAdiantamento.get("vlBruto"));
			tfm.put("adiantamento.obReciboFreteCarreteiro", mapAdiantamento.get("obReciboFreteCarreteiro"));
		}
		
		return tfm;
	}    
	
	
	
    /************************************************************************************
     								INICIO - EQUIPE OPERACAO
    ************************************************************************************/

	/**
	 * Faz a pesquisa da grid para o filho.
	 * 
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    public List findPaginatedIntegranteEqOperac(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
		Long idEquipe = parameters.getLong("idEquipe");
		if (idEquipe == null)
			return Collections.EMPTY_LIST;

		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("integrantes");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("integrantes");

		// Na primeira passada ele se obriga a carregar a tela...
		if (!itemList.isInitialized()) {
			inicializaItemListIntegranteEqOperac(idEquipe, idControleCarga, itemList);
		}
		return getListaIntegrantesOrdenada(itemList.iterator(null, itemListConfig));
	}


	/**
	 * Remove uma lista de registros items.
	 *  
	 * @param ids ids dos registros item a serem removidos.
	 */
	@SuppressWarnings("rawtypes")
    @ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsIntegranteEquipe(List ids) {
		super.removeItemByIds(ids, "integrantes");
	}


	/**
	 * 
	 * @param parameters
	 */
    @SuppressWarnings("rawtypes")
    public void inicializaGridIntegranteEqOperac(TypedFlatMap parameters) {
    	Long idControleCarga = parameters.getLong("idControleCarga");
    	Long idEquipe = parameters.getLong("idEquipe");

    	MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("integrantes");

		if (!itemList.isInitialized()) {
			inicializaItemListIntegranteEqOperac(idEquipe, idControleCarga, itemList);
		} 
		else {
			List lista = integranteEqOperacService.findIntegranteEqOperacao(idEquipe);
	    	ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("integrantes");
	    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
	    		IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
	    		integranteEqOperac.setIdIntegranteEqOperac(null);
	    	}
	    	resetaAtualizaItemList(idControleCarga, masterEntry, itemList, itemListConfig, lista);
		}
    }

    /**
     * 
     * @param idEquipe
     * @param masterId
     * @param itemList
     */
	@SuppressWarnings("rawtypes")
    private void inicializaItemListIntegranteEqOperac(Long idEquipe, Long masterId, ItemList itemList) {
		List listaIntegrantes = integranteEqOperacService.findIntegranteEqOperacao(idEquipe);
		itemList.initialize(Collections.EMPTY_LIST);
		for (Iterator iter = listaIntegrantes.iterator(); iter.hasNext();) {
			IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac)iter.next();
			integranteEqOperac.setIdIntegranteEqOperac(null);
			super.saveItemInstanceOnSession(masterId, integranteEqOperac, "integrantes");
		}
	}


    /**
     * Salva um item na sessao.
     * 
     * @param bean
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Serializable saveIntegranteEqOperac(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	String tpIntegrante = criteria.getString("tpIntegrante");
    	Long idUsuario = criteria.getLong("idUsuario");
    	Long idPessoa = criteria.getLong("idPrestadorServico");
    	Long idIntegranteEqOperac = criteria.getLong("idIntegranteEqOperac");

    	MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("integrantes");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("integrantes");
		
		Iterator iter = itemList.iterator(idControleCarga, itemListConfig);
		getControleCargaService().validateIntegranteExisteSessao(iter, tpIntegrante, idUsuario, idPessoa, idIntegranteEqOperac);
		if (idControleCarga != null && idControleCarga.longValue() == -1) {
			idControleCarga = null;
		}
		getControleCargaService().validateIntegranteEmEquipesComControleCarga(idControleCarga, idUsuario, idPessoa);
		if (tpIntegrante.equals("T")) {
			Pessoa pessoa = pessoaService.findById(idPessoa);
			equipeService.validateTerceiroIsFuncionario(FormatUtils.formatIdentificacao(pessoa));
		}

    	return saveItemInstance(criteria, "integrantes");
    }


    /**
     * 
     * @param key
     * @return
     */
    public TypedFlatMap findByIdIntegranteEqOperac(TypedFlatMap map) {
    	MasterDetailKey key = new MasterDetailKey();
    	key.setDetailId(map.getLong("idIntegranteEqOperac"));
    	key.setMasterId(map.getLong("idControleCarga"));
    	
    	IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac)findItemById(key, "integrantes");

    	TypedFlatMap mapIntegranteEqOperac = new TypedFlatMap();
    	mapIntegranteEqOperac.put("idIntegranteEqOperac", integranteEqOperac.getIdIntegranteEqOperac());
        mapIntegranteEqOperac.put("nmIntegranteEquipe", integranteEqOperac.getNmIntegranteEquipe());
        mapIntegranteEqOperac.put("tpIntegrante", integranteEqOperac.getTpIntegrante().getValue());

        // Funcionario
        if (integranteEqOperac.getTpIntegrante().getValue().equals("F")) {
              mapIntegranteEqOperac.put("idUsuario", integranteEqOperac.getUsuario().getIdUsuario());
              mapIntegranteEqOperac.put("nrMatricula", integranteEqOperac.getUsuario().getNrMatricula());
              mapIntegranteEqOperac.put("nmUsuario", integranteEqOperac.getUsuario().getNmUsuario());
              if (integranteEqOperac.getCargoOperacional() != null) {
            	  mapIntegranteEqOperac.put("dsFuncao", integranteEqOperac.getCargoOperacional().getDsCargo());
              }
        }
        else
        	// Terceiro
        	if (integranteEqOperac.getTpIntegrante().getValue().equals("T")) {
				mapIntegranteEqOperac.put("idPessoa", integranteEqOperac.getPessoa().getIdPessoa());
				mapIntegranteEqOperac.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa()));
				mapIntegranteEqOperac.put("tpIdentificacao", integranteEqOperac.getPessoa().getTpIdentificacao().getValue());
				mapIntegranteEqOperac.put("nmPessoa", integranteEqOperac.getPessoa().getNmPessoa());
        	}  

        if (integranteEqOperac.getCargoOperacional() != null) {
              mapIntegranteEqOperac.put("idCargoOperacional", integranteEqOperac.getCargoOperacional().getIdCargoOperacional());
              mapIntegranteEqOperac.put("dsCargo", integranteEqOperac.getCargoOperacional().getDsCargo());
        }  

        if (integranteEqOperac.getEmpresa() != null) {
              mapIntegranteEqOperac.put("idEmpresa", integranteEqOperac.getEmpresa().getIdEmpresa());
              mapIntegranteEqOperac.put("nmPessoaEmpresa", integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
              mapIntegranteEqOperac.put("nrIdentificacaoFormatadoEmpresa", FormatUtils.formatIdentificacao(integranteEqOperac.getEmpresa().getPessoa()));
              mapIntegranteEqOperac.put("tpIdentificacaoEmpresa", integranteEqOperac.getEmpresa().getPessoa().getTpIdentificacao().getValue());
        }
    	return mapIntegranteEqOperac;
    }

    
	/**
	 * 
	 * @param parameters
	 */
	public void resetDataByEquipe(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga"); 
		TypedFlatMap map = new TypedFlatMap();
		map.put("idControleCarga", idControleCarga);
		map.put("alias", "integrantes"); 
		resetDataTab(map);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List getListaIntegrantesOrdenada(Iterator iterator) {
		List listaRetorno = new ArrayList();
		for (Iterator iter = iterator; iter.hasNext();) {
			IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) iter.next();
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idIntegranteEqOperac", integranteEqOperac.getIdIntegranteEqOperac());
			tfm.put("nmIntegranteEquipe", integranteEqOperac.getNmIntegranteEquipe());
			tfm.put("tpIntegrante", integranteEqOperac.getTpIntegrante());
			if (integranteEqOperac.getUsuario() != null) {
				tfm.put("usuario_idUsuario", integranteEqOperac.getUsuario().getIdUsuario());
				tfm.put("usuario_nrMatricula", integranteEqOperac.getUsuario().getNrMatricula());
			}
			if (integranteEqOperac.getPessoa() != null) {
				tfm.put("pessoa_idPessoa", integranteEqOperac.getPessoa().getIdPessoa());
				tfm.put("pessoa_nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(integranteEqOperac.getPessoa()) );
			}
			if (integranteEqOperac.getCargoOperacional() != null) {
				tfm.put("cargoOperacional_dsCargo", integranteEqOperac.getCargoOperacional().getDsCargo());
			}
			if (integranteEqOperac.getEmpresa() != null) {
				tfm.put("empresa_pessoa_nmPessoa", integranteEqOperac.getEmpresa().getPessoa().getNmPessoa());
			}
			listaRetorno.add(tfm);
		}

		Collections.sort(listaRetorno, new Comparator() {
			public int compare(Object obj1, Object obj2) {
				TypedFlatMap bean1 = (TypedFlatMap)obj1;
				TypedFlatMap bean2 = (TypedFlatMap)obj2;
				return bean1.getString("nmIntegranteEquipe").compareTo(bean2.getString("nmIntegranteEquipe"));
			}    		
		});
		return listaRetorno;
	}
    /************************************************************************************
									FIM - EQUIPE OPERACAO
     ************************************************************************************/
	
	
    /************************************************************************************
									INICIO - PagtoPedagioCc
	************************************************************************************/

	/**
	 * 
	 * @param parameters
	 */
	@SuppressWarnings("rawtypes")
    public void generatePagtoPedagioCcByPostosPassagem(Map parameters) {
		Long idControleCarga = (Long)parameters.get("idControleCarga");
    	MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);

    	ItemList itemListPagtoPedagioCc = getItemsFromSession(masterEntry, "pagamentos");
    	ItemList itemListPostoPassagemCc = getItemsFromSession(masterEntry, "postos");
    	ItemListConfig itemListConfigPagtoPedagioCc = getMasterConfig().getItemListConfig("pagamentos");
    	ItemListConfig itemListConfigPostoPassagemCc = getMasterConfig().getItemListConfig("postos");
    	
		atualizaDadosPostosNaSessao(idControleCarga, parameters, masterEntry, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);
		atualizaDadosPagtosNaSessao(idControleCarga, null, masterEntry, 
				itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc, itemListPostoPassagemCc, itemListConfigPostoPassagemCc);

		List listaPagtos = pagtoPedagioCcService.findPagtoPedagioCc(getValorIdControleCarga(idControleCarga), 
				itemListPostoPassagemCc.iterator(idControleCarga, itemListConfigPostoPassagemCc), Boolean.FALSE);

		resetaAtualizaItemList(idControleCarga, masterEntry, itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc, listaPagtos);
	}


    /**
     * 
     * @param idControleCarga
     * @param parameters
     * @param masterEntry
     * @param itemListPagtoPedagioCc
     * @param itemListConfigPagtoPedagioCc
     * @param itemListPostoPassagemCc
     * @param itemListConfigPostoPassagemCc
     */
    @SuppressWarnings("rawtypes")
    private void atualizaDadosPagtosNaSessao(	Long idControleCarga, List listaPagamentosGrid, MasterEntry masterEntry, 
    											ItemList itemListPagtoPedagioCc, ItemListConfig itemListConfigPagtoPedagioCc, 
    											ItemList itemListPostoPassagemCc, ItemListConfig itemListConfigPostoPassagemCc) 
    {
		// Recebe a lista com os valores da grid pagamentos
		if (listaPagamentosGrid != null) {
			atualizaDadosPagtosNaSessao(idControleCarga, 
					listaPagamentosGrid, masterEntry, itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc); 
		}
		else {
			List listaPagtos = pagtoPedagioCcService.findPagtoPedagioCc(getValorIdControleCarga(idControleCarga), 
					itemListPostoPassagemCc.iterator(idControleCarga, itemListConfigPostoPassagemCc), Boolean.TRUE);
			
			populateItemList(listaPagtos, masterEntry, itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc);
		}
	}
	
	
	/**
	 * Salva um item na sessao.
	 * 
	 * @param bean
	 * @return
	 */
	public Serializable savePagtoPedagioCc(TypedFlatMap criteria) {
		return saveItemInstance(criteria, "pagamentos");
	}


    /**
     * Faz a paginacao da grid pagto pedagio. Esse metodo eh chamado somente pela grid (na parte web).
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedPagtoPedagioCc(TypedFlatMap parameters) {
    	ItemList itemList = getMasterFromSession(parameters.getLong("idControleCarga"), false).getItems("pagamentos");
    	if (!itemList.isInitialized())
    		return ResultSetPage.EMPTY_RESULTSET;

    	ResultSetPage rsp = findPaginatedItemList(parameters, "pagamentos");

    	Map mapValoresOperadora = pagtoPedagioCcService.findOperadoraCartaoPedagio();
    	List listaValor = (List)mapValoresOperadora.get("listaValor");
    	List listaDescricao = (List)mapValoresOperadora.get("listaDescricao");

    	List retorno = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();

			TypedFlatMap map = new TypedFlatMap();
			map.put("idPagtoPedagioCc", pagtoPedagioCc.getIdPagtoPedagioCc());
			map.put("vlPedagio", pagtoPedagioCc.getVlPedagio());
			map.put("idMoeda", pagtoPedagioCc.getMoeda().getIdMoeda());
			map.put("siglaSimbolo", pagtoPedagioCc.getMoeda().getSiglaSimbolo());
			map.put("idTipoPagamPostoPassagem", pagtoPedagioCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem());
			map.put("dsTipoPagamPostoPassagem", pagtoPedagioCc.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem());
			
			if (pagtoPedagioCc.getOperadoraCartaoPedagio() != null) {
				map.put("idOperadoraCartaoPedagio", pagtoPedagioCc.getOperadoraCartaoPedagio().getIdOperadoraCartaoPedagio());
				map.put("operadoraCartaoPedagio_idOperadoraCartaoPedagio", pagtoPedagioCc.getOperadoraCartaoPedagio().getIdOperadoraCartaoPedagio());
			}
			if (pagtoPedagioCc.getCartaoPedagio() != null) {
				map.put("cartaoPedagio_idCartaoPedagio", pagtoPedagioCc.getCartaoPedagio().getIdCartaoPedagio());
				map.put("cartaoPedagio_nrCartao", pagtoPedagioCc.getCartaoPedagio().getNrCartao());
			}
			map.put("pagtoPedagioCc_idPagtoPedagioCc", pagtoPedagioCc.getIdPagtoPedagioCc());
			map.put("tipoPagamPostoPassagem_blCartaoPedagio", pagtoPedagioCc.getTipoPagamPostoPassagem().getBlCartaoPedagio());

	    	List values = new ArrayList();
	    	for (int i = 0; i < listaValor.size(); i++) {
	    		Map item = new HashMap();
	    		item.put("idOperadoraCartaoPedagio", listaValor.get(i));
	    		item.put("nmPessoaOperadoraCartaoPedagio", listaDescricao.get(i));
	    		values.add(item);
	    	}
	    	map.put("values", values);

			
			retorno.add(map);
		}
    	rsp.setList(retorno);
    	return rsp;
    }
    
    
    /**
     * Recebe da tela uma lista com os valores da grid pagamentos. Se algum registro foi alterado na grid, 
     * esse metodo atualiza na sessao.
     * Se nao receber da tela nenhuma lista da da grid pagamentos,  
     *    
     * @param idControleCarga
     * @param parameters
     * @param masterEntry
     * @param itemListPagtoPedagioCc
     * @param itemListConfigPagtoPedagioCc
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void atualizaDadosPagtosNaSessao(	Long idControleCarga, List listaPagamentos, MasterEntry masterEntry, 
												ItemList itemListPagtoPedagioCc, ItemListConfig itemListConfigPagtoPedagioCc) 
    {
		for (Iterator iterPagamentos = listaPagamentos.iterator(); iterPagamentos.hasNext();) {
			Map map = (Map)iterPagamentos.next();

			Long newIdPagtoPedagioCc = (Long)map.get("pagtoPedagioCc_idPagtoPedagioCc");
			Long newIdOperadoraCartaoPedagio = (Long)map.get("operadoraCartaoPedagio_idOperadoraCartaoPedagio");
			Long newIdCartaoPedagio = (Long)map.get("cartaoPedagio_idCartaoPedagio");

			for (Iterator iterPagamentosSecao = itemListPagtoPedagioCc.iterator(idControleCarga, itemListConfigPagtoPedagioCc); iterPagamentosSecao.hasNext();) { 
				PagtoPedagioCc pagtoPedagioCcSecao = (PagtoPedagioCc) iterPagamentosSecao.next();

				if (pagtoPedagioCcSecao.getIdPagtoPedagioCc().compareTo(newIdPagtoPedagioCc) == 0) {
					if ( (newIdOperadoraCartaoPedagio == null && pagtoPedagioCcSecao.getOperadoraCartaoPedagio() == null) ||
						 (newIdCartaoPedagio == null && pagtoPedagioCcSecao.getCartaoPedagio() == null) )
					{
						continue;
					}
					if ( (newIdOperadoraCartaoPedagio != null && pagtoPedagioCcSecao.getOperadoraCartaoPedagio() == null) ||
						 (newIdOperadoraCartaoPedagio == null && pagtoPedagioCcSecao.getOperadoraCartaoPedagio() != null) ||
						 (newIdOperadoraCartaoPedagio.compareTo(pagtoPedagioCcSecao.getOperadoraCartaoPedagio().getIdOperadoraCartaoPedagio()) != 0) ||
						 (newIdCartaoPedagio != null && pagtoPedagioCcSecao.getCartaoPedagio() == null) ||
						 (newIdCartaoPedagio == null && pagtoPedagioCcSecao.getCartaoPedagio() != null) ||
					     (newIdCartaoPedagio.compareTo(pagtoPedagioCcSecao.getCartaoPedagio().getIdCartaoPedagio()) != 0) )
					{
						map.put("idControleCarga", idControleCarga == null ? "-1" : idControleCarga.toString());
						PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) itemListConfigPagtoPedagioCc.populateNewItemInstance(map, new PagtoPedagioCc());
						itemListPagtoPedagioCc.addItem(pagtoPedagioCc, itemListConfigPagtoPedagioCc);
					}
				}
			}
			updateMasterInSession(masterEntry);
		}
	}
	
	
	/**
	 * Insere os dados no no itemList de pagtoPedagioCc de acordo com o conteudo da lista. 
	 * O metodo initialize eh usado quando os dados estao cadastrados no banco e o populateItemList quando os dados estao sendo
	 * gerados 'virtualmente'. 
	 * 
	 * @param lista
	 * @param masterEntry
	 * @param itemList
	 * @param itemListConfig
	 */
	@SuppressWarnings("rawtypes")
    private void populateListaPagtoPedagioCc(List lista, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig) {
		if (pagtoPedagioCcExisteBanco(lista))
			itemList.initialize(lista);
		else
			populateItemList(lista, masterEntry, itemList, itemListConfig);
	}


	/**
	 * Verifica se o conteudo da lista estah cadastrado no banco (id positivo).
	 * 
	 * @param lista
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    private boolean pagtoPedagioCcExisteBanco(List lista) {
		if (lista.isEmpty())
			return true;
	
		PagtoPedagioCc bean = (PagtoPedagioCc)lista.get(0);
		return bean.getIdPagtoPedagioCc() != null && bean.getIdPagtoPedagioCc().compareTo(Long.valueOf(0)) > 0;
	}
    /************************************************************************************
									FIM - PagtoPedagioCc
     ************************************************************************************/

	/************************************************************************************
	 								INICIO - PostoPassagemCc
	 ************************************************************************************/
	/**
	 * 
	 * @param parameters
	 */
	public void resetDataByPostos(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga"); 
		
		TypedFlatMap mapPostos = new TypedFlatMap();
		mapPostos.put("idControleCarga", idControleCarga);
		mapPostos.put("alias", "postos"); 
		resetDataTab(mapPostos);

		TypedFlatMap mapPgtos = new TypedFlatMap();
		mapPgtos.put("idControleCarga", idControleCarga);
		mapPgtos.put("alias", "pagamentos"); 
		resetDataTab(mapPgtos);
	}

	/**
     * Salva um item na sessao.
     * 
     * @param bean
     * @return
     */
    public Serializable savePostoPassagemCc(TypedFlatMap criteria) {
    	return saveItemInstance(criteria, "postos");
    }
    
    
    /**
     * Faz a paginacao da grid postos passagem. Esse metodo eh chamado somente pela grid (na parte web).
     * Inicializa a grid de pagto caso ela nao esteja inicializada.
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedPostoPassagemCc(TypedFlatMap parameters) {
    	Long idControleCarga = parameters.getLong("idControleCarga");
    	String tpControleCarga = parameters.getString("tpControleCarga");
    	ResultSetPage rsp = findPaginatedItemList(parameters, "postos");

    	// Verifica se o itemListPagamentos nao estah inicializado. 
    	// Se nao estiver, busca os pagtos, inicializa o itemList e insere
    	// os pgtos dentro de itemList.
		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
    	ItemList itemListPagtoPedagioCc = getItemsFromSession(masterEntry, "pagamentos");

    	if (!itemListPagtoPedagioCc.isInitialized()) {
	    	ItemListConfig itemListConfigPagtoPedagioCc = getMasterConfig().getItemListConfig("pagamentos");
	    	List lista = pagtoPedagioCcService.findPagtoPedagioCc(getValorIdControleCarga(idControleCarga),
	    			masterEntry.getItems("postos").iterator(idControleCarga, itemListConfigPagtoPedagioCc), Boolean.TRUE);

	    	populateListaPagtoPedagioCc(lista, masterEntry, itemListPagtoPedagioCc, itemListConfigPagtoPedagioCc);
    	}

    	List retorno = new ArrayList();
		for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
			PostoPassagemCc postoPassagemCc = (PostoPassagemCc) iter.next();

			TypedFlatMap map = new TypedFlatMap();
			map.put("idPostoPassagemCc", postoPassagemCc.getIdPostoPassagemCc());
			map.put("vlPagar", postoPassagemCc.getVlPagar());
			map.put("idPostoPassagem", postoPassagemCc.getPostoPassagem().getIdPostoPassagem());
			map.put("nrKm", postoPassagemCc.getPostoPassagem().getNrKm());
			map.put("tpPostoPassagem", postoPassagemCc.getPostoPassagem().getTpPostoPassagem());
			map.put("nmMunicipio", postoPassagemCc.getPostoPassagem().getMunicipio().getNmMunicipio());
			if (postoPassagemCc.getPostoPassagem().getRodovia() != null) {
				map.put("sgRodovia", postoPassagemCc.getPostoPassagem().getRodovia().getSgRodovia());
			}
			map.put("idMoeda", postoPassagemCc.getMoeda().getIdMoeda());
			map.put("siglaSimbolo", postoPassagemCc.getMoeda().getSiglaSimbolo());

			map.put("idTipoPagamPostoPassagem", postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem());
			map.put("tipoPagamPostoPassagem_idTipoPagamPostoPassagem", postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem());

			map.put("tipoPagamPostoPassagem_blCartaoPedagio", postoPassagemCc.getTipoPagamPostoPassagem().getBlCartaoPedagio());
			map.put("postoPassagemCc_idPostoPassagemCc", postoPassagemCc.getIdPostoPassagemCc());

	    	Map mapValoresTipoPagamento = postoPassagemCcService.
	    			findFormasPagamentoPostoPassagemCc(postoPassagemCc.getPostoPassagem().getIdPostoPassagem(), tpControleCarga);
	    	
	    	List listaValor = (List)(mapValoresTipoPagamento.get("listaValor"));
	    	List listaDescricao = (List)(mapValoresTipoPagamento.get("listaDescricao"));
	    	List values = new ArrayList();
	    	for (int i = 0; i < listaValor.size(); i++) {
	    		Map item = new HashMap();
	    		item.put("idTipoPagamPostoPassagem", listaValor.get(i));
	    		item.put("dsTipoPagamPostoPassagem", listaDescricao.get(i));
	    		values.add(item);
	    	}
	    	map.put("values", values);
	    	retorno.add(map);
		}
		rsp.setList(retorno);
    	return rsp;
    }

    /**
	 * Recebe da tela uma lista com os valores da grid postos. Se algum registro foi alterado na grid, 
     * esse metodo atualiza na sessao.
     *  
     * @param idControleCarga
     * @param parameters
     * @param masterEntry
     * @param itemListPostoPassagemCc
     * @param itemListConfigPostoPassagemCc
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public Boolean atualizaDadosPostosNaSessao(	Long idControleCarga, Map parameters, MasterEntry masterEntry, 
								  					ItemList itemListPostoPassagemCc, ItemListConfig itemListConfigPostoPassagemCc) 
	{
		Boolean blAtualizou = Boolean.FALSE;

		// Recebe a lista com os valores da grid postos passagens
		List listaPostos = (List)parameters.get("postos");
		if (listaPostos != null) {
			for (Iterator iterPostos = listaPostos.iterator(); iterPostos.hasNext();) {
				Map map = (Map)iterPostos.next();

				Long newIdPostoPassagemCc = (Long)map.get("postoPassagemCc_idPostoPassagemCc");
				Long newIdTipoPagamPostoPassagem = (Long)map.get("tipoPagamPostoPassagem_idTipoPagamPostoPassagem");
				
				for (Iterator iterPostosSecao = itemListPostoPassagemCc.iterator(idControleCarga, itemListConfigPostoPassagemCc); iterPostosSecao.hasNext();) { 
					PostoPassagemCc postoPassagemCcSecao = (PostoPassagemCc) iterPostosSecao.next();
					if (postoPassagemCcSecao.getIdPostoPassagemCc().compareTo(newIdPostoPassagemCc) == 0 &&
						postoPassagemCcSecao.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem().compareTo(newIdTipoPagamPostoPassagem) != 0) 
					{
						map.put("idControleCarga", idControleCarga == null ? "-1" : idControleCarga.toString());
						PostoPassagemCc postoCc = (PostoPassagemCc) itemListConfigPostoPassagemCc.populateNewItemInstance(map, new PostoPassagemCc());
						itemListPostoPassagemCc.addItem(postoCc, itemListConfigPostoPassagemCc);
						blAtualizou = Boolean.TRUE;
					}
				}
			}
			updateMasterInSession(masterEntry);
		}
		return blAtualizou;
	}
	
	
	/**
	 * Insere os dados no no itemList de postoPassagemCc de acordo com o conteudo da lista. 
	 * O metodo initialize eh usado quando os dados estao cadastrados no banco e o populateItemList quando os dados estao sendo
	 * gerados 'virtualmente'.
	 *  
	 * @param lista
	 * @param masterEntry
	 * @param itemList
	 * @param itemListConfig
	 */
	@SuppressWarnings("rawtypes")
    private void populateListaPostoPassagemCc(List lista, MasterEntry masterEntry, ItemList itemList, ItemListConfig itemListConfig) {
		if (postoPassagemCcExisteBanco(lista))
			itemList.initialize(lista);
		else
			populateItemList(lista, masterEntry, itemList, itemListConfig);
	}

	/**
	 * Verifica se o conteudo da lista estah cadastrado no banco (id positivo).
	 * 
	 * @param lista
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    private boolean postoPassagemCcExisteBanco(List lista) {
		if (lista.isEmpty())
			return true;

		PostoPassagemCc bean = (PostoPassagemCc)lista.get(0);
		return bean.getIdPostoPassagemCc() != null && bean.getIdPostoPassagemCc().compareTo(Long.valueOf(0)) > 0;
	}


	public void resetTabPostos(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga"); 

		TypedFlatMap mapTab = new TypedFlatMap();
		mapTab.put("idControleCarga", idControleCarga);
		mapTab.put("alias", "postos"); 
		resetDataTab(mapTab);

		mapTab.put("alias", "pagamentos"); 
		resetDataTab(mapTab);
    }
	/************************************************************************************
									FIM - PostoPassagemCc
     ************************************************************************************/
    
	/************************************************************************************
	 							INICIO - AdiantamentoTrecho
	************************************************************************************/
	public void inicializaAdiantamentoTrecho(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("idControleCarga"); 
		Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
		populateAdiantamentoTrecho(idControleCarga, idSolicitacaoContratacao);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    private void populateAdiantamentoTrecho(Long idControleCarga, Long idSolicitacaoContratacao) {
		List listaFluxoContratacao = fluxoContratacaoService.findByIdSolicitacaoContratacao(idSolicitacaoContratacao);
		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(idControleCarga); 

		SolicitacaoContratacao sc = solicitacaoContratacaoService.findById(idSolicitacaoContratacao);
		BigDecimal vlFreteNegociado = sc.getVlFreteNegociado();
		
		List listaAdiantamentoTrecho = new ArrayList();
		for (Iterator iter = listaFluxoContratacao.iterator(); iter.hasNext();) {
			FluxoContratacao fc = (FluxoContratacao)iter.next();

			AdiantamentoTrecho adiantamentoTrecho = new AdiantamentoTrecho();
			adiantamentoTrecho.setControleCarga(controleCarga);
			adiantamentoTrecho.setFilialByIdFilialDestino(fc.getFilialDestino());
			adiantamentoTrecho.setFilialByIdFilialOrigem(fc.getFilialOrigem());
			adiantamentoTrecho.setIdAdiantamentoTrecho(null);
			adiantamentoTrecho.setPcFrete(BigDecimalUtils.ZERO);
			adiantamentoTrecho.setReciboFreteCarreteiro(null);
			adiantamentoTrecho.setTpStatusRecibo(new DomainValue("G"));
			adiantamentoTrecho.setVlAdiantamento(BigDecimalUtils.ZERO);
			adiantamentoTrecho.setVlFrete(vlFreteNegociado.multiply(fc.getPcValorFrete().divide(BigDecimalUtils.HUNDRED)));
			listaAdiantamentoTrecho.add(adiantamentoTrecho);
		}

		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("adiantamentoTrecho");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("adiantamentoTrecho");
		if (!itemList.isInitialized()) {
			itemList.initialize(Collections.EMPTY_LIST);
		}
   		removeAllItemList(idControleCarga, itemList, itemListConfig);
   		populateItemList(listaAdiantamentoTrecho, masterEntry, itemList, itemListConfig);
	}

	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findPaginatedAdiantamentoTrecho(TypedFlatMap parameters) {
    	Long idControleCarga = parameters.getLong("idControleCarga");
    	Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();

		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("adiantamentoTrecho");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("adiantamentoTrecho");

		List listRetorno = new ArrayList();
		for (Iterator iter = itemList.iterator(null, itemListConfig); iter.hasNext();) {
			AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho)iter.next();
			if (!adiantamentoTrecho.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario)) 
				continue;

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idAdiantamentoTrecho", adiantamentoTrecho.getIdAdiantamentoTrecho());
			tfm.put("idFilialOrigem", adiantamentoTrecho.getFilialByIdFilialOrigem().getIdFilial());
			tfm.put("sgFilialOrigem", adiantamentoTrecho.getFilialByIdFilialOrigem().getSgFilial());
			tfm.put("sgFilialDestino", adiantamentoTrecho.getFilialByIdFilialDestino().getSgFilial());
			tfm.put("vlFrete", adiantamentoTrecho.getVlFrete());
			tfm.put("vlAdiantamento", adiantamentoTrecho.getVlAdiantamento() == null ? BigDecimalUtils.ZERO : adiantamentoTrecho.getVlAdiantamento());
			tfm.put("pcFrete", adiantamentoTrecho.getPcFrete() == null ? BigDecimalUtils.ZERO : adiantamentoTrecho.getPcFrete());
			listRetorno.add(tfm);
		}
    	return listRetorno;
    }

    
	/**
     * Salva um item na sessao.
     * 
     * @param bean
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void saveAdiantamentoTrecho(TypedFlatMap criteria) {
    	List listaAdiantamentos = criteria.getList("adiantamentosTrecho");
    	if (listaAdiantamentos == null || listaAdiantamentos.isEmpty())
    		return;

    	Long idControleCarga = criteria.getLong("idControleCarga");
    	
		MasterEntry masterEntry = getMasterFromSession(idControleCarga, false);
		ItemList itemList = masterEntry.getItems("adiantamentoTrecho");
		ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("adiantamentoTrecho");

		for (Iterator iterPagamentos = listaAdiantamentos.iterator(); iterPagamentos.hasNext();) {
			Map map = (Map)iterPagamentos.next();
			Long newIdAdiantamentoTrecho = (Long)map.get("idAdiantamentoTrecho");

			for (Iterator iterAdiantamentoTrechoSecao = itemList.iterator(idControleCarga, itemListConfig); iterAdiantamentoTrechoSecao.hasNext();) { 
				AdiantamentoTrecho adiantamentoTrechoSecao = (AdiantamentoTrecho) iterAdiantamentoTrechoSecao.next();

				if (adiantamentoTrechoSecao.getIdAdiantamentoTrecho().compareTo(newIdAdiantamentoTrecho) == 0) {
					BigDecimal newPcFrete = (BigDecimal)map.get("pcFrete");
					BigDecimal newVlAdiantamento = (BigDecimal)map.get("vlAdiantamento");

					if ( (newPcFrete != null && adiantamentoTrechoSecao.getPcFrete() == null) ||
						 (newPcFrete == null && adiantamentoTrechoSecao.getPcFrete() != null) ||
						 (newPcFrete.compareTo(adiantamentoTrechoSecao.getPcFrete()) != 0) ||
						 (newVlAdiantamento != null && adiantamentoTrechoSecao.getVlAdiantamento() == null) ||
						 (newVlAdiantamento == null && adiantamentoTrechoSecao.getVlAdiantamento() != null) ||
					     (newVlAdiantamento.compareTo(adiantamentoTrechoSecao.getVlAdiantamento()) != 0) )
					{
						map.put("idControleCarga", idControleCarga == null ? "-1" : idControleCarga.toString());
						AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho) itemListConfig.populateNewItemInstance(map, new AdiantamentoTrecho());
						itemList.addItem(adiantamentoTrecho, itemListConfig);
					}
				}
			}
			updateMasterInSession(masterEntry);
		}
    }
    
    
	public TypedFlatMap getVlAdiantamentoByAdiantamentoTrecho(TypedFlatMap map) {
		BigDecimal pc = map.getBigDecimal("pcFrete");
		BigDecimal vl = map.getBigDecimal("vlFrete");
		Long idFilialOrigem = map.getLong("idFilialOrigem");

		if (vl == null)
			throw new BusinessException("LMS-05094");

		Filial filial = filialService.findById(idFilialOrigem);
		BigDecimal pcMaximo = filial.getPcFreteCarreteiro();
		if (pc.compareTo(pcMaximo) > 0) {
    		NumberFormat nf = DecimalFormat.getInstance(LocaleContextHolder.getLocale());
    		String strVlPercMaximo = nf.format(pcMaximo.doubleValue());
			throw new BusinessException("LMS-05131", new Object[]{strVlPercMaximo});
		}

		TypedFlatMap tfm = new TypedFlatMap();
		BigDecimal vlAdiantamento = BigDecimalUtils.ZERO;
		if (pc != null && pc.compareTo(BigDecimalUtils.HUNDRED) <= 0 && pc.compareTo(BigDecimalUtils.ZERO) > 0) { 
			vlAdiantamento = pc.divide(BigDecimalUtils.HUNDRED).multiply(vl);
			vlAdiantamento = vlAdiantamento.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP);
		}
		tfm.put("vlAdiantamento", vlAdiantamento);
		return tfm;
	}
	/************************************************************************************
									FIM - AdiantamentoTrecho
    ************************************************************************************/
    
	@SuppressWarnings({ "serial", "rawtypes" })
    protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) { 
		//Declaracao da classe pai
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(ControleCarga.class, true);

		// Esta classe e reponsavel por ordenar a List dos filhos que estao em memoria de acordo com as regras de negocio
    	Comparator comparatorIntegrantes = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				IntegranteEqOperac integranteEqOperac1 = (IntegranteEqOperac)obj1;
				IntegranteEqOperac integranteEqOperac2 = (IntegranteEqOperac)obj2;
        		return integranteEqOperac1.getNmIntegranteEquipe().compareTo(integranteEqOperac2.getNmIntegranteEquipe());
			}    		
    	};

    	Comparator comparatorGeneric = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				return 1;
			}    		
    	};

    	// Aba Equipe
    	// Esta instancia eh responsavel por carregar os items filhos na sessao a partir do banco de dados.
    	ItemListConfig itemEquipeInit = new ItemListConfig() {

    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters todos os parametros vindo da tela pai
    		 */
			public List initialize(Long masterId, Map parameters) {
				return null;
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return Integer.valueOf(1);
			}			

			/**
			 * Seta um pai para o itemConfig de Integrante
			 */
			public void setMasterOnItem(Object master, Object itemBean) {
				((IntegranteEqOperac) itemBean).setEquipeOperacao(new EquipeOperacao());
			}			
			
			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object object) {
				TypedFlatMap criteria = (TypedFlatMap)(Map)parameters;

				String tpIntegrante = criteria.getDomainValue("tpIntegrante").getValue();
				IntegranteEqOperac integranteEqOperac = (IntegranteEqOperac) object;
				integranteEqOperac.setTpIntegrante(domainValueService.findDomainValueByValue("DM_INTEGRANTE_EQUIPE", tpIntegrante));
				integranteEqOperac.setIdIntegranteEqOperac(criteria.getLong("idIntegranteEqOperac"));

				if (tpIntegrante.equals("F")) {
					Usuario usuario = usuarioService.findById(criteria.getLong("idUsuario")) ;

					CargoOperacional cargoOperacional = new CargoOperacional();
					cargoOperacional.setDsCargo(criteria.getString("dsFuncao"));

					integranteEqOperac.setNmIntegranteEquipe(usuario.getNmUsuario());
					integranteEqOperac.setUsuario(usuario);
				} 
				else {
					Long idPessoa = criteria.getLong("idPrestadorServico");
					Long idCargoOperacional = criteria.getLong("idCargoOperacional");
					Long idEmpresa = criteria.getLong("idEmpresa");

					if (idPessoa != null) { 
						integranteEqOperac.setPessoa(pessoaService.findById(idPessoa));
						integranteEqOperac.setNmIntegranteEquipe(integranteEqOperac.getPessoa().getNmPessoa()); 
					}

					if (idCargoOperacional != null) 
						integranteEqOperac.setCargoOperacional(cargoOperacionalService.findById(idCargoOperacional));

					if (idEmpresa != null) 
						integranteEqOperac.setEmpresa(empresaService.findById(idEmpresa));					
				}
				return integranteEqOperac;
			}
			
			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			@SuppressWarnings("unchecked")
            public void modifyItemValues(Object newBean, Object oldBean) {
				Set ignore = new HashSet();
				ignore.add("versao");
				ignore.add("idIntegranteEqOperac");
				ignore.add("equipeOperacao");				
				ReflectionUtils.syncObjectProperties(oldBean, newBean, ignore);				
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}
    	};

    	// Aba - 1a grid
    	// Esta instancia eh responsavel por carregar os items filhos na sessao a partir do banco de dados.
    	ItemListConfig itemPagamentoInit = new ItemListConfig() {

    		/**
    		 * Esse metodo eh chamado somente na primeira vez em que o metodo findPaginatedItemList for executado.
    		 * Apos a primeira vez ela eh carregada na memoria, e o item list fica inicializado.
    		 * Nesse caso estah retornando uma lista vazia, porque a grid de pagto eh povoada a partir da grid postos.
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters
    		 */
			public List initialize(Long masterId, Map parameters) {
				return Collections.EMPTY_LIST;
			}

			/**
			 * Busca rowCount da grid pagto.
			 * Passa por este ponto apenas na primeira vez em que a list filha eh chamada.
    		 * Apos a primeira vez ela eh carregada da memoria. 
    		 * Estah retornando um valor qualquer, porque neste caso nao teremos paginacao e o getRowCount nao sera usado.
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return Integer.valueOf(1);
			}			

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este metodo.
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos parametros enviados da tela para se 
			 * evitar um 'ReflectionUtils'.
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map criteria, Object object) {
				TipoPagamPostoPassagem tipoPagamPostoPassagem = new TipoPagamPostoPassagem();
				tipoPagamPostoPassagem.setIdTipoPagamPostoPassagem((Long)criteria.get("tipoPagamPostoPassagem_idTipoPagamPostoPassagem"));

				CartaoPedagio cartaoPedagio = null;
				Long idCartaoPedagio = (Long)criteria.get("cartaoPedagio_idCartaoPedagio");
				if (idCartaoPedagio != null) {
					cartaoPedagio = new CartaoPedagio();
					cartaoPedagio.setIdCartaoPedagio(idCartaoPedagio);
					cartaoPedagio.setNrCartao((Long)criteria.get("cartaoPedagio_nrCartao"));
				}

				OperadoraCartaoPedagio operadoraCartaoPedagio = null;
				Long idOperadoraCartaoPedagio = (Long)criteria.get("operadoraCartaoPedagio_idOperadoraCartaoPedagio");
				if (idOperadoraCartaoPedagio != null) {
					operadoraCartaoPedagio = new OperadoraCartaoPedagio();
					operadoraCartaoPedagio.setIdOperadoraCartaoPedagio((Long)criteria.get("operadoraCartaoPedagio_idOperadoraCartaoPedagio"));
				}

				PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) object;
				pagtoPedagioCc.setIdPagtoPedagioCc((Long)criteria.get("pagtoPedagioCc_idPagtoPedagioCc"));
				pagtoPedagioCc.setOperadoraCartaoPedagio(operadoraCartaoPedagio);
				pagtoPedagioCc.setCartaoPedagio(cartaoPedagio);
				return pagtoPedagioCc;
			}

			/**
			 * Metodo chamado quando um item da grid for alterado. Os atributos desnecessarios para o filho sao removidos.
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object oldBean) {
				PagtoPedagioCc newPagtoPedagioCc = (PagtoPedagioCc)newBean;
				PagtoPedagioCc oldPagtoPedagioCc = (PagtoPedagioCc)oldBean;
				oldPagtoPedagioCc.setCartaoPedagio(newPagtoPedagioCc.getCartaoPedagio());
				oldPagtoPedagioCc.setOperadoraCartaoPedagio(newPagtoPedagioCc.getOperadoraCartaoPedagio());
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}
    	};

    	// Aba - 2a grid
    	// Esta instancia eh responsavel por carregar os items filhos na sessao a partir do banco de dados.
    	ItemListConfig itemPostoPassagemInit = new ItemListConfig() {

    		/**
    		 * Esse metodo eh chamado somente na primeira vez em que o metodo findPaginatedItemList for executado.
    		 * Apos a primeira vez ela eh carregada na memoria, e o item list fica inicializado.
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters todos os parametros vindo da tela pai
    		 */
			public List initialize(Long masterId, Map parameters) {
				if (parameters.isEmpty())
					return Collections.EMPTY_LIST;

				TypedFlatMap map = (TypedFlatMap)parameters;
				Long idControleCarga = map.getLong("idControleCarga");
				Long idMeioTransporteTransportado = map.getLong("idMeioTransporteTransportado");
				Long idMeioTransporteSemiRebocado = map.getLong("idMeioTransporteSemiRebocado");
				Long idRotaColetaEntrega = map.getLong("idRotaColetaEntrega");
				Long idRotaIdaVolta = map.getLong("idRotaIdaVolta");
				Long idRota = map.getLong("idRota");
				String tpControleCarga = map.getString("tpControleCarga");

				if (idRotaIdaVolta == null && idRotaColetaEntrega == null && idRota == null)
					return Collections.EMPTY_LIST;
				
				List result = Collections.EMPTY_LIST;
				if (tpControleCarga.equals("C"))
					result =  postoPassagemCcService.findPostoPassagemCcByColetaEntrega(getValorIdControleCarga(idControleCarga), 
							idMeioTransporteTransportado, idMeioTransporteSemiRebocado, idRotaColetaEntrega, null);
				else {
					List filiaisRota = filialRotaService.findFiliaisRota(idRotaIdaVolta, idRota);
					result = postoPassagemCcService.findPostoPassagemCcByViagem(getValorIdControleCarga(idControleCarga), 
							idMeioTransporteTransportado, idMeioTransporteSemiRebocado, filiaisRota, Boolean.TRUE, null);
				}

				int id = -1;
				for (Iterator iter = result.iterator(); iter.hasNext();) {
					PostoPassagemCc ppCC = (PostoPassagemCc)iter.next();
					if (ppCC.getIdPostoPassagemCc() == null) {
						ppCC.setIdPostoPassagemCc(Long.valueOf(id--));
					}
				}
				return result;
			}

			/**
			 * Busca rowCount da grid postos.
			 * Passa por este ponto apenas na primeira vez em que a list filha eh chamada.
    		 * Apos a primeira vez ela eh carregada da memoria. 
    		 * Estah retornando um valor qualquer, porque neste caso nao teremos paginacao e o getRowCount nao sera usado.
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return Integer.valueOf(1);
			}			

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este metodo.
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos parametros enviados da tela para se 
			 * evitar um 'ReflectionUtils'.
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object object) {
				TipoPagamPostoPassagem tipoPagamPostoPassagem = new TipoPagamPostoPassagem();
				tipoPagamPostoPassagem.setIdTipoPagamPostoPassagem((Long)parameters.get("tipoPagamPostoPassagem_idTipoPagamPostoPassagem"));

				PostoPassagemCc postoPassagemCc = (PostoPassagemCc) object;
				postoPassagemCc.setIdPostoPassagemCc((Long)parameters.get("postoPassagemCc_idPostoPassagemCc"));
				postoPassagemCc.setTipoPagamPostoPassagem(tipoPagamPostoPassagem);
				return postoPassagemCc;
			}

			/**
			 * Metodo chamado quando um item da grid for alterado. Os atributos desnecessarios para o filho seho removidos.
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object oldBean) {
				PostoPassagemCc newPostoPassagemCc = (PostoPassagemCc)newBean;
				PostoPassagemCc oldPostoPassagemCc = (PostoPassagemCc)oldBean;
				oldPostoPassagemCc.setTipoPagamPostoPassagem(newPostoPassagemCc.getTipoPagamPostoPassagem());
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}
    	};
   	
    	// Aba Trechos
    	// Esta instancia eh responsavel por carregar os items filhos na sessao a partir do banco de dados.
    	ItemListConfig itemTrechoInit = new ItemListConfig() {

    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters todos os parametros vindo da tela pai
    		 */
			public List initialize(Long masterId, Map parameters) {
				TypedFlatMap map = (TypedFlatMap)parameters;
				Long idRotaIdaVolta = map.getLong("_idRotaIdaVolta");
				Long idRota = map.getLong("_idRota");
				DateTime dhPrevisaoSaida = map.getDateTime("_dhPrevisaoSaida");
				List listTrechos = controleTrechoService.findTrechosByTrechosRota(null, idRotaIdaVolta, idRota, dhPrevisaoSaida, null, null, null);
				return listTrechos;
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return Integer.valueOf(1);
			}			

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object object) {
				return null;
			}

			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object oldBean) {
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}
    	};

    	
    	
    	// Esta instancia eh responsavel por carregar os items filhos na sessao a partir do banco de dados.
    	ItemListConfig itemTrechoCorporativoInit = new ItemListConfig() {

    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters todos os parametros vindo da tela pai
    		 */
			public List initialize(Long masterId, Map parameters) {
				return Collections.EMPTY_LIST;
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return null;
			}

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object object) {
				return null;
			}

			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object oldBean) {
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}
    	};
    	
    	
    	// Esta instancia eh responsavel por carregar os items filhos na sessao a partir do banco de dados.
    	ItemListConfig itemAdiantamentoTrechoInit = new ItemListConfig() {

    		/**
    		 * Find paginated do filho
    		 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
    		 * 
    		 *  @param masterId id do pai
    		 *  @param parameters todos os parametros vindo da tela pai
    		 */
			public List initialize(Long masterId, Map parameters) {
				return Collections.EMPTY_LIST;
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
    		 * Apos a primeira vez ela e carregada da memoria
			 * 
			 * @param masterId id do pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return null;
			}

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 * 
			 * @param parameters 
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object object) {
				AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho) object;
				adiantamentoTrecho.setIdAdiantamentoTrecho((Long)parameters.get("idAdiantamentoTrecho"));
				adiantamentoTrecho.setPcFrete(parameters.get("pcFrete") != null ? (BigDecimal)parameters.get("pcFrete") : BigDecimalUtils.ZERO);
				adiantamentoTrecho.setVlAdiantamento(parameters.get("vlAdiantamento") != null ? (BigDecimal)parameters.get("vlAdiantamento") : BigDecimalUtils.ZERO);
				return adiantamentoTrecho;
			}

			/**
			 * Metodo chamado quando um item da grid for alterado. Os atributos desnecessarios para o filho sao removidos.
			 * 
			 * @param newBean 
			 * @param oldBean 
			 */
			public void modifyItemValues(Object newBean, Object oldBean) {
				AdiantamentoTrecho newAdiantamentoTrecho = (AdiantamentoTrecho)newBean;
				AdiantamentoTrecho oldAdiantamentoTrecho = (AdiantamentoTrecho)oldBean;
				oldAdiantamentoTrecho.setPcFrete(newAdiantamentoTrecho.getPcFrete());
				oldAdiantamentoTrecho.setVlAdiantamento(newAdiantamentoTrecho.getVlAdiantamento());
			}

			/**
			 * Mapeia atributos de dominio do pojo filho
			 */
			public Map configItemDomainProperties() {
				return null;
			}
    	};


    	// Seta as configuracoes do filho...
		config.addItemConfig("integrantes", IntegranteEqOperac.class, itemEquipeInit, comparatorIntegrantes);
		config.addItemConfig("pagamentos", PagtoPedagioCc.class, itemPagamentoInit, comparatorGeneric);
		config.addItemConfig("postos", PostoPassagemCc.class, itemPostoPassagemInit, comparatorGeneric);
		config.addItemConfig("trechos", ControleTrecho.class, itemTrechoInit, comparatorGeneric);
		config.addItemConfig("trechoCorporativo", TrechoCorporativo.class, itemTrechoCorporativoInit, comparatorGeneric);
		config.addItemConfig("adiantamentoTrecho", AdiantamentoTrecho.class, itemAdiantamentoTrechoInit, comparatorGeneric);
		return config;
	}
	
	public boolean getBlrestringeCCVinculo(){
		return SessionUtils.getFilialSessao().getBlRestringeCCVinculo();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @ParametrizedAttribute(type = java.lang.String.class)
	public Map<String, Map<String, DomainValue>> findTiposVinculoByDomainName(List domainNameList){
		Map<String, Map<String, DomainValue>> retorno = new HashMap<String, Map<String, DomainValue>>();
		
		for (Object	domainName : domainNameList) {
			if(domainName instanceof String){
				List<DomainValue> domainValueList = domainValueService.findDomainValues((String)domainName);

				Map<String, DomainValue> domainValueMap = new HashMap<String, DomainValue>();
				for (DomainValue domainValue : domainValueList) {
					domainValueMap.put(domainValue.getValue(), domainValue);
				}
				retorno.put((String)domainName, domainValueMap);
			}
		}
		return retorno;
    }
	
    /**
     * Busca as Notas Fiscais EDI relacionadas ao Docto do Cliente 
     * @param criteria
     * @return
     * LMSA-6520: LMSA-6534
     */
    public List<ManifestoFedexSegundoCarregamentoDTO> findNotasFiscaisByChaveMdfeFedex(final Map<String, String> chaveMdfeFedex) {
        return notaFiscalExpedicaoEDIService.findNotaFiscalByChaveMdfeFedex(chaveMdfeFedex.get("chaveMdfeFedex"));
    }

	private void criaAdiantamentoParaControleCargaComSemiReboque(ControleCarga controleCarga) {

		List<FluxoContratacao> listFluxoContratacao = fluxoContratacaoService.findByIdSolicitacaoContratacao(
				controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao());

		BigDecimal vlFreteMaximoAutorizado = controleCarga.getSolicitacaoContratacao().getVlFreteMaximoAutorizado();

		if (listFluxoContratacao != null && !listFluxoContratacao.isEmpty()) {
			for (FluxoContratacao fc : listFluxoContratacao) {
				AdiantamentoTrecho adiantamentoTrecho = new AdiantamentoTrecho();

				adiantamentoTrecho.setControleCarga(controleCarga);
				adiantamentoTrecho.setTpStatusRecibo(new DomainValue("G"));
				adiantamentoTrecho.setPcFrete(BigDecimal.ZERO);
				adiantamentoTrecho.setVlFrete(BigDecimal.ZERO);
				adiantamentoTrecho.setFilialByIdFilialOrigem(fc.getFilialOrigem());
				adiantamentoTrecho.setFilialByIdFilialDestino(fc.getFilialDestino());

				criaAdiantamentoParaControleCargaValorFrete(vlFreteMaximoAutorizado, fc, adiantamentoTrecho);

				BigDecimal vlAdiantamento = aplicaPorcentagem(adiantamentoTrecho.getVlFrete(), adiantamentoTrecho.getPcFrete());

				if (vlAdiantamento != null) {
					adiantamentoTrecho.setVlAdiantamento(vlAdiantamento);
				} else {
					adiantamentoTrecho.setVlAdiantamento(BigDecimal.ZERO);
				}

				adiantamentoTrechoService.store(adiantamentoTrecho);
			}
		}
	}

	private void criaAdiantamentoParaControleCargaValorFrete(BigDecimal vlFreteMaximoAutorizado, FluxoContratacao fc, AdiantamentoTrecho adiantamentoTrecho) {
		if (fc.getPcValorFrete() != null) {
			BigDecimal vlFrete = aplicaPorcentagem(vlFreteMaximoAutorizado, fc.getPcValorFrete());

			if (vlFrete != null) {
				adiantamentoTrecho.setVlFrete(vlFrete);
			}
		}
	}

	private BigDecimal aplicaPorcentagem(BigDecimal base, BigDecimal porcentagem) {
		BigDecimal valor = base.multiply(porcentagem).divide(BigDecimalUtils.HUNDRED);
		valor.setScale(2, BigDecimal.ROUND_HALF_UP);
		return valor;
	}
	
	public void setRotaViagemService(RotaViagemService rotaViagemService) {
		this.rotaViagemService = rotaViagemService;
	}
	public void setFluxoContratacaoService(FluxoContratacaoService fluxoContratacaoService) {
		this.fluxoContratacaoService = fluxoContratacaoService;
	}
	public void setPrestadorServicoService(PrestadorServicoService prestadorServicoService) {
		this.prestadorServicoService = prestadorServicoService;
	}
	public void setMotivoParadaService(MotivoParadaService motivoParadaService) {
		this.motivoParadaService = motivoParadaService;
	}
	public void setMeioTransporteRotaViagemService(MeioTransporteRotaViagemService meioTransporteRotaViagemService) {
		this.meioTransporteRotaViagemService = meioTransporteRotaViagemService;
	}
	public void setMotoristaRotaViagemService(MotoristaRotaViagemService motoristaRotaViagemService) {
		this.motoristaRotaViagemService = motoristaRotaViagemService;
	}
	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setIntegranteEqOperacService(IntegranteEqOperacService integranteEqOperacService) {
		this.integranteEqOperacService = integranteEqOperacService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setPontoParadaTrechoService(PontoParadaTrechoService pontoParadaTrechoService) {
		this.pontoParadaTrechoService = pontoParadaTrechoService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}
	public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}
	public void setTipoMeioTransporteService(TipoMeioTransporteService tipoMeioTransporteService) {
		this.tipoMeioTransporteService = tipoMeioTransporteService;
	}
	public void setTipoTabelaColetaEntregaService(TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService) {
		this.tipoTabelaColetaEntregaService = tipoTabelaColetaEntregaService;
	}
	public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
		this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
	}
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
		this.postoPassagemCcService = postoPassagemCcService;
	}
	public void setCartaoPedagioService(CartaoPedagioService cartaoPedagioService) {
		this.cartaoPedagioService = cartaoPedagioService;
	}
	public void setCargoOperacionalService(CargoOperacionalService cargoOperacionalService) {
		this.cargoOperacionalService = cargoOperacionalService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public void setPagtoPedagioCcService(PagtoPedagioCcService pagtoPedagioCcService) {
		this.pagtoPedagioCcService = pagtoPedagioCcService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setTabelaFreteCarreteiroCeService(
			TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService) {
		this.tabelaFreteCarreteiroCeService = tabelaFreteCarreteiroCeService;
	}
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	// LMSA-6520: LMSA-6534
	public void setNotaFiscalExpedicaoEDIService(NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService) {
	    this.notaFiscalExpedicaoEDIService = notaFiscalExpedicaoEDIService;
	}

	public void setAdiantamentoTrechoService(AdiantamentoTrechoService adiantamentoTrechoService) {
		this.adiantamentoTrechoService = adiantamentoTrechoService;
	}
}
