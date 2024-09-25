package com.mercurio.lms.carregamento.swt.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.carregamento.model.CartaoPedagio;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.carregamento.model.LocalTroca;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;
import com.mercurio.lms.carregamento.model.RotasExpressas;
import com.mercurio.lms.carregamento.model.TrechoCorporativo;
import com.mercurio.lms.carregamento.model.service.AdiantamentoTrechoService;
import com.mercurio.lms.carregamento.model.service.CartaoPedagioService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ControleTrechoService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.FilialRotaCcService;
import com.mercurio.lms.carregamento.model.service.LocalTrocaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.carregamento.model.service.PagtoPedagioCcService;
import com.mercurio.lms.carregamento.model.service.PostoPassagemCcService;
import com.mercurio.lms.carregamento.model.service.RotasExpressasService;
import com.mercurio.lms.carregamento.model.service.TrechoCorporativoService;
import com.mercurio.lms.carregamento.report.EmitirRelatorioControleCargaColetaEntregaService;
import com.mercurio.lms.carregamento.report.EmitirRelatorioControleCargaViagemService;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.TipoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.BeneficiarioProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.PostoConveniadoService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.contratacaoveiculos.model.service.TipoMeioTransporteService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaFreteCarreteiroCeService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TipoTabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MeioTransporteRotaViagemService;
import com.mercurio.lms.municipios.model.service.MotoristaRotaViagemService;
import com.mercurio.lms.municipios.model.service.PontoParadaTrechoService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.municipios.model.service.RotaIdaVoltaService;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.municipios.model.service.TipoPagamPostoPassagemService;
import com.mercurio.lms.municipios.model.service.TrechoRotaIdaVoltaService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;
import com.mercurio.lms.sgr.model.service.SolicitacaoSinalService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.swt.manterControleCargasAction"
 */

public class ManterControleCargasAction {

	private AdiantamentoTrechoService adiantamentoTrechoService;
	private BeneficiarioProprietarioService beneficiarioProprietarioService;
	private CartaoPedagioService cartaoPedagioService;
	private ControleCargaService controleCargaService;
	private ControleQuilometragemService controleQuilometragemService;
	private ControleTrechoService controleTrechoService;
	private EventoControleCargaService eventoControleCargaService;
	private FilialRotaCcService filialRotaCcService;
	private FilialService filialService;
	private LocalTrocaService localTrocaService;
	private ManifestoService manifestoService;
	private MeioTransporteRotaViagemService meioTransporteRotaViagemService;
	private MeioTransporteService meioTransporteService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private MotoristaService motoristaService;
	private MotoristaRotaViagemService motoristaRotaViagemService;
	private PagtoPedagioCcService pagtoPedagioCcService;
	private ParametroGeralService parametroGeralService;
	private PontoParadaTrechoService pontoParadaTrechoService;
	private PostoPassagemCcService postoPassagemCcService;
	private ProprietarioService proprietarioService;
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private RotaColetaEntregaService rotaColetaEntregaService;
	private RotaIdaVoltaService rotaIdaVoltaService;
	private RotaService rotaService;
	private RotasExpressasService rotasExpressasService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private SolicitacaoSinalService solicitacaoSinalService;
	private TipoMeioTransporteService tipoMeioTransporteService;
	private TipoPagamPostoPassagemService tipoPagamPostoPassagemService;
	private TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService;
	private TrechoCorporativoService trechoCorporativoService;
	private PostoConveniadoService postoConveniadoService;
	private ReportExecutionManager reportExecutionManager;
	private EmitirRelatorioControleCargaColetaEntregaService emitirRelatorioControleCargaColetaEntregaService;
	private EmitirRelatorioControleCargaViagemService emitirRelatorioControleCargaViagemService;
	
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService;
	
	private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
	
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";
	
	@SuppressWarnings("rawtypes")
    public void validateCNHMotorista(Map criteria) {
		Long idMotorista = (Long)criteria.get("idMotorista");
		motoristaService.validateCNHMotorista(idMotorista);
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public void setAdiantamentoTrechoService(AdiantamentoTrechoService adiantamentoTrechoService) {
		this.adiantamentoTrechoService = adiantamentoTrechoService;
	}
	public void setRotasExpressasService(RotasExpressasService rotasExpressasService) {
		this.rotasExpressasService = rotasExpressasService;
	}
	public void setTrechoCorporativoService(TrechoCorporativoService trechoCorporativoService) {
		this.trechoCorporativoService = trechoCorporativoService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public void setSolicitacaoSinalService(SolicitacaoSinalService solicitacaoSinalService) {
		this.solicitacaoSinalService = solicitacaoSinalService;
	}
	public void setTipoPagamPostoPassagemService(TipoPagamPostoPassagemService tipoPagamPostoPassagemService) {
		this.tipoPagamPostoPassagemService = tipoPagamPostoPassagemService;
	}
	public void setCartaoPedagioService(CartaoPedagioService cartaoPedagioService) {
		this.cartaoPedagioService = cartaoPedagioService;
	}
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setPagtoPedagioCcService(PagtoPedagioCcService pagtoPedagioCcService) {
		this.pagtoPedagioCcService = pagtoPedagioCcService;
	}
	public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
		this.postoPassagemCcService = postoPassagemCcService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setBeneficiarioProprietarioService(BeneficiarioProprietarioService beneficiarioProprietarioService) {
		this.beneficiarioProprietarioService = beneficiarioProprietarioService;
	}
	public void setControleQuilometragemService(ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
		this.filialRotaCcService = filialRotaCcService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setLocalTrocaService(LocalTrocaService localTrocaService) {
		this.localTrocaService = localTrocaService;
	}
	public void setMeioTransporteRotaViagemService(MeioTransporteRotaViagemService meioTransporteRotaViagemService) {
		this.meioTransporteRotaViagemService = meioTransporteRotaViagemService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setMotoristaRotaViagemService(MotoristaRotaViagemService motoristaRotaViagemService) {
		this.motoristaRotaViagemService = motoristaRotaViagemService;
	}
	public void setPontoParadaTrechoService(PontoParadaTrechoService pontoParadaTrechoService) {
		this.pontoParadaTrechoService = pontoParadaTrechoService;
	}
	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}
	public void setReciboFreteCarreteiroService(ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}
	public void setRotaIdaVoltaService(RotaIdaVoltaService rotaIdaVoltaService) {
		this.rotaIdaVoltaService = rotaIdaVoltaService;
	}
	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
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


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupFilial(Map criteria) {
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

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupFilialByTpAcesso(Map criteria) {
		String sgFilial = (String)criteria.get("sgFilial");
		String tpAcesso = (String)criteria.get("tpAcesso");
		List list = filialService.findLookupBySgFilial(sgFilial, tpAcesso);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Map mapFilial = (Map) iter.next();
			Map mapResult = new HashMap();
			mapResult.put("idFilial", mapFilial.get("idFilial"));
			mapResult.put("sgFilial", mapFilial.get("sgFilial"));
			Map mapPessoa = (Map)mapFilial.get("pessoa");
			mapResult.put("nmFantasia", mapPessoa.get("nmFantasia"));
    		retorno.add(mapResult);
    	}
    	return retorno;
    }


	@SuppressWarnings({ "rawtypes", "unchecked" })
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
    		retorno.add(tfm);
    	}
    	return retorno;
    }

	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List findLookupMeioTransporteTransportado(Map criteria) {
    	criteria.put("tipoMeioTransporte", "transportado");
    	return findLookupMeioTransporte(criteria);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

	
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupProprietario(Map criteria) {
   		Map mapPessoa = new HashMap();
   		mapPessoa.put("nrIdentificacao", criteria.get("nrIdentificacao"));

   		Map map = new HashMap();
   		map.put("pessoa", mapPessoa);
   		map.put("tpSituacao", criteria.get("tpSituacao"));

    	List list = proprietarioService.findLookup(map);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Proprietario proprietario = (Proprietario)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idProprietario", proprietario.getIdProprietario());
    		typedFlatMap.put("tpIdentificacao", proprietario.getPessoa().getTpIdentificacao());
    		typedFlatMap.put("nrIdentificacao", FormatUtils.formatIdentificacao(proprietario.getPessoa()));
    		typedFlatMap.put("nmPessoa", proprietario.getPessoa().getNmPessoa());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupRotaIdaVolta(TypedFlatMap criteria) {
    	criteria.put("filialOrigem.idFilial", SessionUtils.getFilialSessao().getIdFilial());
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

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List findLookupRota(TypedFlatMap criteria) {
    	List list = rotaService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Rota rota = (Rota)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idRota", rota.getIdRota());
    		tfm.put("dsRota", rota.getDsRota());
    		retorno.add(tfm);
    	}
    	return retorno;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List findTipoTabelaColetaEntrega(Filial filial, MeioTransporte meioTransporte, TipoTabelaColetaEntrega tipoTabelaColetaEntrega, RotaColetaEntrega rotaColetaEntrega) {
    	if (meioTransporte == null)
    		return Collections.EMPTY_LIST;
 
    	if (tipoTabelaColetaEntrega != null) {
    		TipoTabelaColetaEntrega ttce = tipoTabelaColetaEntregaService.findById(tipoTabelaColetaEntrega.getIdTipoTabelaColetaEntrega());
    		
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idTipoTabelaColetaEntrega", ttce.getIdTipoTabelaColetaEntrega());
    		tfm.put("dsTipoTabelaColetaEntrega", ttce.getDsTipoTabelaColetaEntrega().toString());
    		tfm.put("tpCalculo", "C1"); // esse valor vem vinculado direto com o controle de cargam logo só pode ser do tipo 1
    		tfm.put("blNormal", ttce.getBlNormal());

    		List lista = new ArrayList();
    		lista.add(tfm);
    		return lista;
    	}
    	try {
    		return tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaWithTabelaColetaEntrega(
    				filial.getIdFilial(), meioTransporte.getIdMeioTransporte(), rotaColetaEntrega.getIdRotaColetaEntrega());
    	} catch (BusinessException be) {
    		return Collections.EMPTY_LIST;
    	}
    }

    
    @SuppressWarnings({ "rawtypes", "unchecked" })
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
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }

    
    @SuppressWarnings("rawtypes")
    private TypedFlatMap populateParametrosPesquisa(Map criteria) {
    	TypedFlatMap tfm = new TypedFlatMap();     	
    	tfm.put("nrControleCarga", criteria.get("nrControleCarga"));
    	tfm.put("tpControleCarga", criteria.get("tpControleCarga"));
    	tfm.put("tpRotaViagem", criteria.get("tpRotaViagem"));
    	tfm.put("tpStatusControleCarga", criteria.get("tpStatusControleCarga"));
    	tfm.put("filialByIdFilialOrigem.idFilial", criteria.get("idFilialOrigem"));
    	tfm.put("filialByIdFilialDestino.idFilial", criteria.get("idFilialDestino"));
    	tfm.put("solicitacaoContratacao.idSolicitacaoContratacao", criteria.get("idSolicitacaoContratacao"));
    	tfm.put("rotaIdaVolta.idRotaIdaVolta", criteria.get("idRotaIdaVolta"));
    	tfm.put("rotaColetaEntrega.idRotaColetaEntrega", criteria.get("idRotaColetaEntrega"));
    	tfm.put("rota.idRota", criteria.get("idRota"));
    	tfm.put("meioTransporteByIdTransportado.idMeioTransporte", criteria.get("idMeioTransporteTransportado"));
    	tfm.put("meioTransporteByIdSemiRebocado.idMeioTransporte", criteria.get("idMeioTransporteSemiRebocado"));
    	tfm.put("proprietario.idProprietario", criteria.get("idProprietario"));
    	tfm.put("motorista.idMotorista", criteria.get("idMotorista"));
    	tfm.put("dtGeracaoInicial", criteria.get("dtGeracaoInicial"));
    	tfm.put("dtGeracaoFinal", criteria.get("dtGeracaoFinal"));
    	tfm.put("hrPrevisaoSaidaInicial", criteria.get("hrPrevisaoSaidaInicial"));
    	tfm.put("hrPrevisaoSaidaFinal", criteria.get("hrPrevisaoSaidaFinal"));
    	return tfm;
    }

    
    /**
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings("rawtypes")
    public void validatePaginatedControleCarga(Map criteria) {
    	TypedFlatMap tfmParam = populateParametrosPesquisa(criteria);
    	controleCargaService.validatePaginatedControleCarga(tfmParam);
    }

    
    /**
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedControleCarga(Map criteria) {
    	TypedFlatMap tfmParam = populateParametrosPesquisa(criteria);
    	
    	List<Map> listMap = new ArrayList<Map>();
    	ResultSetPage rsp = controleCargaService.findPaginatedControleCarga(tfmParam, FindDefinition.createFindDefinition(criteria));
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext(); ){
    		TypedFlatMap tfm = (TypedFlatMap)iter.next();
    		Map map = new HashMap();  
    		
    		map.put("idControleCarga", tfm.get("idControleCarga"));
    		map.put("nrControleCarga", tfm.get("nrControleCarga"));
    		map.put("dhGeracao", tfm.get("dhGeracao"));
    		map.put("dhPrevisaoSaida", tfm.get("dhPrevisaoSaida"));
    		map.put("tpControleCarga", tfm.get("tpControleCarga"));
    		map.put("idFilialOrigem", tfm.get("filialByIdFilialOrigem.idFilial"));
    		map.put("sgFilialOrigem", tfm.get("filialByIdFilialOrigem.sgFilial"));
    		map.put("nmFantasiaFilialOrigem", tfm.get("filialByIdFilialOrigem.pessoa.nmFantasia"));
    		map.put("idMeioTransporteTransportado", tfm.get("meioTransporteByIdTransportado.idMeioTransporte"));
    		map.put("nrFrotaTransportado", tfm.get("meioTransporteByIdTransportado.nrFrota"));
    		map.put("nrIdentificadorTransportado", tfm.get("meioTransporteByIdTransportado.nrIdentificador"));
    		map.put("idMeioTransporteSemiRebocado", tfm.get("meioTransporteByIdSemiRebocado.idMeioTransporte"));
    		map.put("nrFrotaSemiRebocado", tfm.get("meioTransporteByIdSemiRebocado.nrFrota"));
    		map.put("nrIdentificadorSemiRebocado", tfm.get("meioTransporteByIdSemiRebocado.nrIdentificador"));
    		map.put("dsRotaColeta", tfm.get("dsRotaColeta"));
    		map.put("dsRotaViagem", tfm.get("dsRotaViagem"));
    		map.put("idFilialDestino", tfm.get("filialByIdFilialDestino.idFilial"));
    		map.put("sgFilialDestino", tfm.get("filialByIdFilialDestino.sgFilial"));
    		map.put("nmFantasiaFilialDestino", tfm.get("filialByIdFilialDestino.pessoa.nmFantasia"));
    		map.put("idMotorista", tfm.get("motorista.pessoa.idPessoa"));
    		map.put("nmMotorista", tfm.get("motorista.pessoa.nmPessoa"));
    		map.put("nrIdentificacaoMotorista", tfm.get("motorista.pessoa.nrIdentificacao"));
    		
    		String tpControleCargaValue = tfm.getDomainValue("tpControleCarga").getValue();
    		if (tpControleCargaValue.equals("C")) {
    			map.put("dsRota", tfm.getString("dsRotaColeta"));
    			map.put("tpRotaViagem", tfm.getDomainValue("tpControleCarga").getDescription().toString());
    		}
    		else {
    			if (tfm.getDomainValue("tpRotaViagemDominio") != null) {
    				map.put("tpRotaViagem", tfm.getDomainValue("tpRotaViagemDominio").getDescription().toString());
    			}
    			map.put("dsRota", tfm.getString("dsRotaViagem"));
    		}

    		DateTime dhPrevisaoSaida = tfm.getDateTime("dhPrevisaoSaida");
    		if (dhPrevisaoSaida != null) {
    			map.put("hrPrevisaoSaida", JTFormatUtils.format(dhPrevisaoSaida, JTFormatUtils.SHORT, JTFormatUtils.TIMEOFDAY));
    		}
    		tfm.remove("tpRotaViagemDominio");

    		if (tfm.get("motorista.pessoa.nrIdentificacao") != null){
    			map.put("idMotorista", tfm.get("motorista.pessoa.idPessoa"));
    			map.put("nmMotorista", tfm.get("motorista.pessoa.nmPessoa"));
    			if (tfm.get("motorista.pessoa.tpIdentificacao") != null) {
    				map.put("tpIdentificacaoMotorista", tfm.get("motorista.pessoa.tpIdentificacao"));
				}
    			map.put("nrIdentificacaoFormatadoMotorista", FormatUtils.formatIdentificacao((String)tfm.get("motorista.pessoa.tpIdentificacao.value"),(String)tfm.get("motorista.pessoa.nrIdentificacao")));
    		}
    		map.put("nrManif", tfm.get("nrManif"));
    		
    		listMap.add(map);
    	}
    	rsp.setList(listMap);
    	return rsp;
    }

    
    /**
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Integer getRowCountControleCarga(Map criteria) {
    	return controleCargaService.getRowCountControleCarga(populateParametrosPesquisa(criteria));
    }

    
    
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


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedAdiantamentos(TypedFlatMap parameters) {
    	ResultSetPage rsp = reciboFreteCarreteiroService.
    		findPaginatedByIdControleCarga(parameters.getLong("idControleCarga"), FindDefinition.createFindDefinition(parameters));
    	
    	List lista = new ArrayList();
    	for (Iterator iter = rsp.getList().iterator(); iter.hasNext();) {
    		ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro)iter.next();
    		Map map = new HashMap();
    		map.put("idReciboFreteCarreteiro", rfc.getIdReciboFreteCarreteiro());
    		map.put("vlBruto", rfc.getVlBruto());
    		map.put("siglaSimbolo", rfc.getMoedaPais().getMoeda().getSiglaSimbolo());
    		if (rfc.getContaBancaria() != null) {
	    		map.put("nrContaBancaria", rfc.getContaBancaria().getNrContaBancaria());
	    		if (rfc.getContaBancaria().getAgenciaBancaria() != null) {
		    		map.put("nrAgenciaBancaria", rfc.getContaBancaria().getAgenciaBancaria().getNrAgenciaBancaria());
		    		if (rfc.getContaBancaria().getAgenciaBancaria().getBanco() != null) {
		    			map.put("nrBanco", rfc.getContaBancaria().getAgenciaBancaria().getBanco().getNrBanco());
		    		}
	    		}
    		}
    		if (rfc.getBeneficiario() != null) {
    			map.put("beneficiarioNmPessoa", rfc.getBeneficiario().getPessoa().getNmPessoa());
    		}
    		lista.add(map);
    	}
    	rsp.setList(lista);
    	return rsp;
    }
    
    public Integer getRowCountAdiantamentos(TypedFlatMap parameters) {
    	return reciboFreteCarreteiroService.getRowCountFindPaginatedByIdControleCarga(parameters.getLong("idControleCarga"));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map findById(Long idControleCarga) {
    	
    	ControleCarga cc = controleCargaService.findByIdControleCarga(idControleCarga);
    	
    	Map map = new TypedFlatMap();
    	map.put("idControleCarga", cc.getIdControleCarga());
    	map.put("nrControleCarga", cc.getNrControleCarga());
    	map.put("idFilialOrigem", cc.getFilialByIdFilialOrigem().getIdFilial());
    	map.put("sgFilialOrigem", cc.getFilialByIdFilialOrigem().getSgFilial());
    	map.put("nmFantasiaFilialOrigem", cc.getFilialByIdFilialOrigem().getPessoa().getNmFantasia());

    	map.put("tpControleCargaValue", cc.getTpControleCarga().getValue());
    	map.put("tpControleCargaDescription", cc.getTpControleCarga().getDescription().toString());
    	map.put("tpStatusControleCargaValue", cc.getTpStatusControleCarga().getValue());
    	map.put("tpStatusControleCargaDescription", cc.getTpStatusControleCarga().getDescription().toString());
    	map.put("blEntregaDireta", cc.getBlEntregaDireta());
    	map.put("nrManif", cc.getNrManif());
    	
    	if (cc.getMoeda() != null) {
    		map.put("moedaSiglaSimbolo", cc.getMoeda().getSiglaSimbolo());
    	}

    	BigDecimal valorTotalFrota = controleCargaService.findValorTotalFrota(idControleCarga);
    	map.put("vlTotalFrota",  valorTotalFrota);

    	if (!cc.getTpStatusControleCarga().getValue().equals("EV")) {
    		map.put("sgFilialAtualizaStatus", cc.getFilialByIdFilialAtualizaStatus().getSgFilial());
    	}
    	
    	if (cc.getTpRotaViagem() != null) {
    		map.put("tpRotaViagemValue", cc.getTpRotaViagem().getValue());
    		map.put("tpRotaViagemDescription", cc.getTpRotaViagem().getDescription().toString());
    	}
    	
    	map.put("vlFreteCarreteiro", cc.getVlFreteCarreteiro());
    	Long valorHrTempoViagem = cc.getNrTempoViagem() == null ? null : Long.valueOf(cc.getNrTempoViagem());
    	map.put("hrTempoViagem", FormatUtils.converteMinutosParaHorasMinutos(valorHrTempoViagem, FormatUtils.ESCALA_HHH));
    	map.put("dhPrevisaoSaida", cc.getDhPrevisaoSaida());

    	if (cc.getRotaColetaEntrega() != null) {
        	map.put("idRotaColetaEntrega", cc.getRotaColetaEntrega().getIdRotaColetaEntrega());
        	map.put("nrRotaColetaEntrega", cc.getRotaColetaEntrega().getNrRota());
        	map.put("dsRotaColetaEntrega", cc.getRotaColetaEntrega().getDsRota());
    	}
    	
    	if (cc.getRotaIdaVolta() != null) {
        	map.put("idRotaIdaVolta", cc.getRotaIdaVolta().getIdRotaIdaVolta());
        	map.put("nrRotaIdaVolta", cc.getRotaIdaVolta().getNrRota());
        	map.put("dsRotaIdaVolta", cc.getRotaIdaVolta().getRota().getDsRota());
        	map.put("idRotaViagem", cc.getRotaIdaVolta().getRotaViagem().getIdRotaViagem());
        	
        	if (cc.getRotaIdaVolta().getRotaViagem().getTipoMeioTransporte() != null) {
        		map.put("idTipoMeioTransporteByRotaIdaVolta", cc.getRotaIdaVolta().getRotaViagem().getTipoMeioTransporte().getIdTipoMeioTransporte());
        	}

        	List listaRotaViagem = motoristaRotaViagemService.findByIdRotaIdaVolta(cc.getRotaIdaVolta().getIdRotaIdaVolta());
        	if (!listaRotaViagem.isEmpty()) {
        		map.put("blFiltrarMotorista", Boolean.TRUE);
        	}
        	
        	if (cc.getVlFreteCarreteiro() == null) {
        		// TODO
        	}
    	}
    	
    	if (cc.getSolicitacaoContratacao() != null) {
    		map.put("idSolicitacaoContratacao", cc.getSolicitacaoContratacao().getIdSolicitacaoContratacao());
        	map.put("nrSolicitacaoContratacao", cc.getSolicitacaoContratacao().getNrSolicitacaoContratacao());
        	map.put("idFilialSolicitacaoContratacao", cc.getSolicitacaoContratacao().getFilial().getIdFilial());
        	map.put("sgFilialSolicitacaoContratacao", cc.getSolicitacaoContratacao().getFilial().getSgFilial());
        	map.put("nmFantasiaSolicitacaoContratacao", cc.getSolicitacaoContratacao().getFilial().getPessoa().getNmFantasia());
    	}
    	
    	if (cc.getTabelaColetaEntrega() != null) {
    		map.put("idTabelaColetaEntrega", cc.getTabelaColetaEntrega().getIdTabelaColetaEntrega());
    	}
    	
		if (cc.getTipoTabelaColetaEntrega() != null) {
	    	map.put("idTipoTabelaColetaEntrega", cc.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega());
    	}
		
    	if (cc.getVlFreteCarreteiro() != null && cc.getMoeda() != null) {
    		map.put("moedaVlFreteCarreteiro", cc.getMoeda().getSgMoeda() + " " + cc.getMoeda().getDsSimbolo());
    	}
    	
    	map.put("blDesabilitaSemiReboque", Boolean.TRUE);
    	map.put("blDesabilitaSolicSinal", Boolean.FALSE);

    	// VIAGEM
    	if (cc.getTpControleCarga().getValue().equals("V")) {
    		if (cc.getMeioTransporteByIdTransportado() != null) {
    			map.put("idMeioTransporteTransportadoViagem", cc.getMeioTransporteByIdTransportado().getIdMeioTransporte());
    			map.put("nrFrotaTransportadoViagem", cc.getMeioTransporteByIdTransportado().getNrFrota());
    			map.put("nrIdentificadorTransportadoViagem", cc.getMeioTransporteByIdTransportado().getNrIdentificador());
    			map.put("tpVinculo", cc.getMeioTransporteByIdTransportado().getTpVinculo().getValue());
    			map.put("tpMeioTransporteValue", cc.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte().getTpMeioTransporte().getValue());
    			if (cc.getMeioTransporteByIdTransportado().getMeioTransporteRodoviario() != null) {
    				map.put("nrRastreador", 
    						cc.getMeioTransporteByIdTransportado().getMeioTransporteRodoviario().getNrRastreador());
    			}
    			map.put("idTipoMeioTransporteTransportado", cc.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte());
    			
    	    	TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.
    	    			findTipoMeioTransporteCompostoByIdMeioTransporte(cc.getMeioTransporteByIdTransportado().getIdMeioTransporte());
    	    	if (tipoMeioTransporte != null) {
    	    		map.put("blDesabilitaSemiReboque", Boolean.FALSE);
    	    	}

    			Map mapControleCarga = new HashMap();
    			mapControleCarga.put("idControleCarga", idControleCarga);

    			Map mapMeioTransporte = new HashMap();
    			mapMeioTransporte.put("idMeioTransporte", cc.getMeioTransporteByIdTransportado().getIdMeioTransporte());
    			
    			Map mapSolicitacaoSinal = new HashMap();
    			mapSolicitacaoSinal.put("controleCarga", mapControleCarga);
    			mapSolicitacaoSinal.put("meioTransporte", mapMeioTransporte);
    			mapSolicitacaoSinal.put("tpStatusSolicitacao", "GE");
    			if (!solicitacaoSinalService.find(mapSolicitacaoSinal).isEmpty()) {
    				map.put("blDesabilitaSolicSinal", Boolean.TRUE);
    			}
    		}
    		if (cc.getMeioTransporteByIdSemiRebocado() != null) {
    			map.put("idMeioTransporteSemiRebocadoViagem", cc.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte());
    			map.put("nrFrotaSemiRebocadoViagem", cc.getMeioTransporteByIdSemiRebocado().getNrFrota());
    			map.put("nrIdentificadorSemiRebocadoViagem", cc.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
    		}
    		if (cc.getProprietario() != null) {
    			map.put("idProprietario", cc.getProprietario().getIdProprietario());
    			map.put("nrIdentificacaoFormatadoProprietarioViagem", FormatUtils.formatIdentificacao(cc.getProprietario().getPessoa()) );
    			map.put("nmPessoaProprietarioViagem", cc.getProprietario().getPessoa().getNmPessoa());
    		}
    		if (cc.getMotorista() != null) {
    			map.put("idMotoristaViagem", cc.getMotorista().getIdMotorista());
    			map.put("nrIdentificacaoFormatadoMotoristaViagem", FormatUtils.formatIdentificacao(cc.getMotorista().getPessoa()) );
    			map.put("nmPessoaMotoristaViagem", cc.getMotorista().getPessoa().getNmPessoa());
    		}
    		if (cc.getRota() != null) {
    			map.put("rotaDsRota", cc.getRota().getDsRota());
    			map.put("rotaIdRota", cc.getRota().getIdRota());
    		}
    	}
    	// COLETA
    	else {
    		if (cc.getMeioTransporteByIdTransportado() != null) {
    			map.put("idMeioTransporteTransportadoColeta", cc.getMeioTransporteByIdTransportado().getIdMeioTransporte());
    			map.put("nrFrotaTransportadoColeta", cc.getMeioTransporteByIdTransportado().getNrFrota());
    			map.put("nrIdentificadorTransportadoColeta", cc.getMeioTransporteByIdTransportado().getNrIdentificador());
    			map.put("idTipoMeioTransporteTransportado", cc.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte().getIdTipoMeioTransporte());
    			map.put("tpMeioTransporteValue", cc.getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte().getTpMeioTransporte().getValue());

    	    	TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.
		    			findTipoMeioTransporteCompostoByIdMeioTransporte(cc.getMeioTransporteByIdTransportado().getIdMeioTransporte());
		    	if (tipoMeioTransporte != null) {
		    		map.put("blDesabilitaSemiReboque", Boolean.FALSE);
		    	}
    		}
    		if (cc.getMeioTransporteByIdSemiRebocado() != null) {
    			map.put("idMeioTransporteSemiRebocadoColeta", cc.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte());
    			map.put("nrFrotaSemiRebocadoColeta", cc.getMeioTransporteByIdSemiRebocado().getNrFrota());
    			map.put("nrIdentificadorSemiRebocadoColeta", cc.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
    		}
    		if (cc.getProprietario() != null) {
    			map.put("idProprietario", cc.getProprietario().getIdProprietario());
    			map.put("nrIdentificacaoFormatadoProprietarioColeta", FormatUtils.formatIdentificacao(cc.getProprietario().getPessoa()) );
    			map.put("nmPessoaProprietarioColeta", cc.getProprietario().getPessoa().getNmPessoa());
    		}
    		
    		if (cc.getMotorista() != null) {
    			map.put("idMotoristaColeta", cc.getMotorista().getIdMotorista());
    			map.put("nrIdentificacaoFormatadoMotoristaColeta", FormatUtils.formatIdentificacao(cc.getMotorista().getPessoa()) );
    			map.put("nmPessoaMotoristaColeta", cc.getMotorista().getPessoa().getNmPessoa());
    		}

     		ControleQuilometragem controleQuilometragemSaida  = controleQuilometragemService.findControleQuilometragemByIdControleCargaByIdFilial(cc.getIdControleCarga(), cc.getFilialByIdFilialOrigem().getIdFilial(), Boolean.TRUE);
    		if (controleQuilometragemSaida != null)
    			map.put("nrQuilometragemSaida", controleQuilometragemSaida.getNrQuilometragem());
    		
    		ControleQuilometragem controleQuilometragemRetorno  = controleQuilometragemService.findControleQuilometragemByIdControleCargaByIdFilial(cc.getIdControleCarga(), cc.getFilialByIdFilialOrigem().getIdFilial(), Boolean.FALSE);
    		if (controleQuilometragemRetorno != null)
    			map.put("nrQuilometragemRetorno", controleQuilometragemRetorno.getNrQuilometragem());
    		
    		map.put("tabelas", findTipoTabelaColetaEntrega(cc.getFilialByIdFilialOrigem(), cc.getMeioTransporteByIdTransportado(), cc.getTipoTabelaColetaEntrega(),cc.getRotaColetaEntrega()));
    		if(map.get("tabelas")!=null && !((List) map.get("tabelas")).isEmpty()){
    			map.put("tpCalculoTabelas", ((Map)((List) map.get("tabelas")).get(0)).get("tpCalculo"));
    		}    
    		
    	}
    	
    	map.put("padrao", isCalculoPadrao(cc.getFilialByIdFilialOrigem().getIdFilial()));
    	
    	map.put("blSolicitacaoPuxada",controleCargaService.findSolicitacaoPuxada(idControleCarga,cc.getFilialByIdFilialOrigem().getIdFilial()));
    	
    	map.put("blFilialUsuarioIsMatriz", SessionUtils.isFilialSessaoMatriz());

    	map.put("blPermiteAlterar", validatePermissaoAlteracaoCc(cc));
    	map.put("blPermiteAlterarPgtoProprietario", validatePermissaoAlteracaoPagtoProprietario(cc.getMeioTransporteByIdTransportado()));
    	map.put("blPermiteAlterarPostosPassagem", validatePermissaoAlteracaoPostosPassagem(idControleCarga, cc.getFilialByIdFilialOrigem().getIdFilial()));
    	
		if (validateExisteDadosPagtoPedagioCc(idControleCarga)) {
			if (pagtoPedagioCcService.validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(idControleCarga)) {
				map.put("blNecessitaCartaoPedagio", Boolean.TRUE);
			}
			map.put("blDesabilitaTabPostos", Boolean.FALSE);
		} else {
			map.put("blDesabilitaTabPostos", Boolean.TRUE);
		}
		
    	return map;
    }
    
    private boolean isCalculoPadrao(Long idFilial){	
		boolean calculoPadrao = false;
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(idFilial, PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}
		return calculoPadrao;		
	}

    private boolean validatePermissaoAlteracaoPostosPassagem(Long idControleCarga, Long idFilialOrigem) {
    	Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
    	if (!idFilialUsuario.equals(idFilialOrigem))
    		return false;

    	if (controleCargaService.validateExisteEventoEmitido(idControleCarga)) {
    		return false;
    	}
    	return true;
    }


    /**
     * 
     * @param nrRotaIdaVolta
     * @return true se existe, caso contrário, false.
     */
    @SuppressWarnings({ "rawtypes", "unused" })
    private boolean verificaExisteTrechoComAgrupadorDiferente(Integer nrRotaIdaVolta) {
    	boolean blExisteTrecho = false;
		List listaRotasExpressas = rotasExpressasService.findByNrRotaIdaVolta(nrRotaIdaVolta, null);
		if (listaRotasExpressas.size() > 1) {
			RotasExpressas re1 = (RotasExpressas)listaRotasExpressas.get(0);
			RotasExpressas re2 = (RotasExpressas)listaRotasExpressas.get(listaRotasExpressas.size() -1);
			if (!re1.getTpAgrupador().equals( re2.getTpAgrupador() )) {
				blExisteTrecho = true;
			}
		}
		return blExisteTrecho;
    }


    /**
     * O perfil DIVOP não tem acesso ao método validatePermiteAlteracaoCcByPerfilUsuario.
     * 
     * @param meioTransporte
     * @return True se tem a permissão, caso contrário, False.
     */
	private Boolean validatePermissaoAlteracaoPagtoProprietario(MeioTransporte meioTransporte) {
		
		if (meioTransporte != null && meioTransporte.getTpVinculo().getValue().equals("P")) {
			return controleCargaService.validateManutencaoEspecialCC(SessionUtils.getUsuarioLogado());
						
			}
		return Boolean.FALSE;
	}

	
	
	private Boolean validatePermissaoAlteracaoCc(ControleCarga cc) {
		
		/*Status do contorle de carga*/
		String tpStatusCcValue = cc.getTpStatusControleCarga().getValue();
		
		if (tpStatusCcValue.equals("FE") || tpStatusCcValue.equals("EV")) {
			return controleCargaService.validateManutencaoEspecialCC(SessionUtils.getUsuarioLogado());
			}

		/*Obtem a filial de destino*/
    	Long idFilialDestino = null;
    	if (cc.getFilialByIdFilialDestino() != null) {
    		idFilialDestino = cc.getFilialByIdFilialDestino().getIdFilial();
    	}
    	    	
		return controleCargaService.validatePermissaoAlteracaoCc(cc.getIdControleCarga(),
													    		 cc.getFilialByIdFilialOrigem().getIdFilial(), 
													    		 idFilialDestino,
													    		 cc.getFilialByIdFilialAtualizaStatus().getIdFilial(),
													    		 cc.getTpControleCarga().getValue(), 
													    		 tpStatusCcValue);
		
	}
	
	

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public TypedFlatMap getVlAdiantamento(TypedFlatMap parameters) {
		BigDecimal pc = parameters.getBigDecimal("pcAdiantamentoFrete");
		BigDecimal vl = parameters.getBigDecimal("vlFreteCarreteiro");
		Long idControleCarga = parameters.getLong("idControleCarga");

		if (vl == null)
			throw new BusinessException("LMS-05094");

		Filial filial = SessionUtils.getFilialSessao();
		BigDecimal pcMaximo = filial.getPcFreteCarreteiro();
		BigDecimal pcTotalAdiantamentos = getPcTotalAdiantamentos(idControleCarga);
		if (pc.add(pcTotalAdiantamentos).compareTo(pcMaximo) > 0) {
    		NumberFormat nf = DecimalFormat.getInstance(LocaleContextHolder.getLocale());
    		String strVlPercMaximo = nf.format(pcMaximo.doubleValue());
			throw new BusinessException("LMS-05131", new Object[]{strVlPercMaximo});
		}

		TypedFlatMap toReturn = new TypedFlatMap();
		BigDecimal vlAdiantamento = null;
		if (pc != null && pc.compareTo(new BigDecimal(100)) <= 0 && pc.compareTo(BigDecimalUtils.ZERO) > 0) { 
			vlAdiantamento = pc.divide(new BigDecimal(100)).multiply(vl);
			vlAdiantamento = vlAdiantamento.divide(new BigDecimal(1), 2, BigDecimal.ROUND_HALF_UP);
		}
		toReturn.put("vlAdiantamento", vlAdiantamento);
		return toReturn;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private BigDecimal getPcTotalAdiantamentos(Long idControleCarga) {
		Map mapControleCarga = new HashMap();
		mapControleCarga.put("idControleCarga", idControleCarga);

		Map map = new HashMap();
		map.put("controleCarga", mapControleCarga);

		BigDecimal vlSoma = BigDecimalUtils.ZERO;
		List lista = reciboFreteCarreteiroService.find(map);
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
			ReciboFreteCarreteiro rfc = (ReciboFreteCarreteiro)iter.next();
			if (rfc.getPcAdiantamentoFrete() != null) {
				vlSoma = vlSoma.add(rfc.getPcAdiantamentoFrete());
			}
		}
		return vlSoma;
	}
	
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public TypedFlatMap findBeneficiario(Long idProprietario, Long idControleCarga, String tpBeneficiarioAdiantamentoParametro) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	
    	List<AdiantamentoTrecho> adiantamentosTrechos = idControleCarga == null ?  new ArrayList<AdiantamentoTrecho>() : adiantamentoTrechoService.findByIdControleCarga(idControleCarga, null, null) ;
    	PostoConveniado postoConveniado = null;
    	// se o idcontrolecarga é null então a tela veio do gerar controle de carga, e certamente vai ter adiantementos
    	boolean hasAdiantamento = idControleCarga == null;
    	
    	String tpBeneficiarioAdiantamento = StringUtils.isBlank(tpBeneficiarioAdiantamentoParametro) ? "PR" : tpBeneficiarioAdiantamentoParametro;
    	if(adiantamentosTrechos != null && !adiantamentosTrechos.isEmpty()){
    		Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
    		AdiantamentoTrecho adiantamentoTrecho = adiantamentosTrechos.get(0);
    		
			if (adiantamentoTrecho.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario)) {
				hasAdiantamento = true;
				postoConveniado = adiantamentoTrecho.getPostoConveniado();    		
			}
    	}
    	
    	if(postoConveniado != null && !"PR".equals(tpBeneficiarioAdiantamentoParametro)){
    		tpBeneficiarioAdiantamento = "PO";
		}else{
			Long idFilial = idControleCarga == null?
								SessionUtils.getFilialSessao().getIdFilial() :
								controleCargaService.findByIdInitLazyProperties(idControleCarga, false).getFilialByIdFilialOrigem().getIdFilial();
					
    		postoConveniado = postoConveniadoService.findPostoConveniadoByIdFilial(idFilial);
		}
    	
    	if(postoConveniado != null && hasAdiantamento){
    		tfm.put("hasPostoConveniado", Boolean.TRUE);
    	} else {
    		tfm.put("hasPostoConveniado", Boolean.FALSE);
    	}
    	
    	if(postoConveniado == null || "PR".equals(tpBeneficiarioAdiantamento) ){
    		// popula campos com informações do proprietarios
    		if (idProprietario != null ){
		    	List retorno = beneficiarioProprietarioService.findBeneficiarioByIdProprietario(idProprietario);
		    	if (!retorno.isEmpty()) {
		    		tfm.put("idBeneficiario", ((Map)retorno.get(0)).get("idPessoa"));
		    		tfm.putAll( populateDadosBeneficiario((Map)retorno.get(0)) );
		    	} else {
		    		retorno = proprietarioService.findDadosBancariosByIdProprietario(idProprietario);
		    		tfm.put("idBeneficiario", ((Map)retorno.get(0)).get("idPessoa"));
		    		if (!retorno.isEmpty())
		    			tfm.putAll( populateDadosBeneficiario((Map)retorno.get(0)) );
		    	}
    		}
    	} else {
    		if("PO".equals(tpBeneficiarioAdiantamento) ){
    			tfm.putAll( populateDadosBeneficiario( postoConveniadoService.findDadosBancariosPostoConveniado(postoConveniado.getIdPostoConveniado())) );
    			tfm.put("idBeneficiario", postoConveniado.getIdPostoConveniado());
    		}
    	}
    	tfm.put("tpBeneficiario", tpBeneficiarioAdiantamento);
    	
    	return tfm;
    }

	@SuppressWarnings("rawtypes")
    private TypedFlatMap populateDadosBeneficiario(Map map) {
		TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.put("nrIdentificacaoBeneficiario", map.get("tpIdentificacaoBeneficiario") == null ? "" :
			FormatUtils.formatIdentificacao( ((DomainValue)map.get("tpIdentificacaoBeneficiario")).getValue(), (String)map.get("nrIdentificacaoBeneficiario")) );
		toReturn.put("nmPessoaBeneficiario", map.get("nmPessoaBeneficiario") == null ? "" : map.get("nmPessoaBeneficiario"));
		toReturn.put("nrContaBancaria", map.get("nrContaBancaria") == null ? "" : map.get("nrContaBancaria"));
		toReturn.put("dvContaBancaria", map.get("dvContaBancaria") == null ? "" : map.get("dvContaBancaria"));
		toReturn.put("nrAgenciaBancaria", map.get("nrAgenciaBancaria") == null ? "" : map.get("nrAgenciaBancaria"));
		toReturn.put("nmAgenciaBancaria", map.get("nmAgenciaBancaria") == null ? "" : map.get("nmAgenciaBancaria"));
		toReturn.put("nrBanco", map.get("nrBanco") == null ? "" : map.get("nrBanco"));
		toReturn.put("nmBanco", map.get("nmBanco") == null ? "" : map.get("nmBanco"));
		return toReturn;
	}


    public TypedFlatMap findByIdAdiantamento(Long idReciboFreteCarreteiro) {
    	ReciboFreteCarreteiro bean = reciboFreteCarreteiroService.findByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
    	TypedFlatMap toReturn = new TypedFlatMap();
    	toReturn.put("idReciboFreteCarreteiro", bean.getIdReciboFreteCarreteiro());
    	toReturn.put("pcAdiantamentoFrete", bean.getPcAdiantamentoFrete());
    	toReturn.put("vlBruto", bean.getVlBruto());
    	toReturn.put("obReciboFreteCarreteiro", bean.getObReciboFreteCarreteiro());
    	if (bean.getMoedaPais() != null) {
    		toReturn.put("siglaSimbolo", bean.getMoedaPais().getMoeda().getSiglaSimbolo());
    	}
    	if (bean.getBeneficiario() != null) {
    		toReturn.put("nrIdentificacaoBeneficiario", FormatUtils.formatIdentificacao(bean.getBeneficiario().getPessoa()) );
    		toReturn.put("nmPessoaBeneficiario", bean.getBeneficiario().getPessoa().getNmPessoa());
    	}
    	if (bean.getProprietario() != null) {
    		toReturn.put("nrIdentificacaoProprietario", FormatUtils.formatIdentificacao(bean.getProprietario().getPessoa()) );
    		toReturn.put("nmPessoaProprietario", bean.getProprietario().getPessoa().getNmPessoa());
    	}
    	if (bean.getContaBancaria() != null) {
        	toReturn.put("nrContaBancaria", bean.getContaBancaria().getNrContaBancaria());
        	toReturn.put("dvContaBancaria", bean.getContaBancaria().getDvContaBancaria());
        	if (bean.getContaBancaria().getAgenciaBancaria() != null) {
            	toReturn.put("nrAgenciaBancaria", bean.getContaBancaria().getAgenciaBancaria().getNrAgenciaBancaria());
            	toReturn.put("nmAgenciaBancaria", bean.getContaBancaria().getAgenciaBancaria().getNmAgenciaBancaria());
            	if (bean.getContaBancaria().getAgenciaBancaria().getBanco() != null) {
                	toReturn.put("nrBanco", bean.getContaBancaria().getAgenciaBancaria().getBanco().getNrBanco());
                	toReturn.put("nmBanco", bean.getContaBancaria().getAgenciaBancaria().getBanco().getNmBanco());
            	}
        	}
    	}
    	return toReturn;
    }
    
    public Long storeAdiantamento(TypedFlatMap criteria) {
    	Long idMeioTransporte = criteria.getLong("idMeioTransporte");
    	if (idMeioTransporte == null)
    		throw new BusinessException("LMS-05064");

    	Long idFilial = null;
    	if(!controleCargaService.validateManutencaoEspecialCC(SessionUtils.getUsuarioLogado())){
			idFilial = criteria.getLong("idFilialOrigem");
		}
		
		double pcAdiantamento = criteria.getDouble("pcAdiantamentoFrete").doubleValue();

    	return reciboFreteCarreteiroService.storeReciboFreteCarreteiroByControleCarga(	
				criteria.getLong("idControleCarga"),
				idMeioTransporte,
				criteria.getLong("idMotorista"), 
				criteria.getLong("idProprietario"),
				idFilial,
				BigDecimal.valueOf(pcAdiantamento),
				criteria.getBigDecimal("vlBruto"),
				criteria.getString("obReciboFreteCarreteiro"),
				Boolean.TRUE,
				null,
				null);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map findDescricaoLocalTroca(Long idLocalTroca) {
    	LocalTroca localTroca = localTrocaService.findById(idLocalTroca);
    	Map map = new HashMap();
    	map.put("dsTroca", localTroca.getDsTroca());
    	return map;
    }

    
	@SuppressWarnings("rawtypes")
    public TypedFlatMap findDadosVeiculoColeta(TypedFlatMap criteria) {
		final Long idMeioTransporte = criteria.getLong("idMeioTransporte");
		final Long idRotaColetaEntrega = criteria.getLong("idRotaColetaEntrega");
		final Long idControleCarga = criteria.getLong("idControleCarga");
		
		TypedFlatMap tfm = findDadosVeiculo(idMeioTransporte, idControleCarga);

		tfm.put("tabelas", tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaWithTabelaColetaEntrega(SessionUtils.getFilialSessao().getIdFilial(), idMeioTransporte, idRotaColetaEntrega));
		if(tfm.get("tabelas")!=null && !tfm.getList("tabelas").isEmpty() ){
			tfm.put("tpCalculoTabelas", ((Map)tfm.getList("tabelas").get(0)).get("tpCalculo"));
		}
		
		boolean calculoPadrao = false;
		tfm.put("padrao", false);
		
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}
			
		if(calculoPadrao){	
			String[] tipoOperacao = new String[] {"E","D","CE"};
			List<TabelaFcValores> tabelas = tabelaFreteCarreteiroCeService.findByIdFilial(SessionUtils.getFilialSessao().getIdFilial(),tipoOperacao);
			
			if(tabelas.isEmpty()){
				throw new BusinessException("LMS-25117");
			}
			tfm.put("padrao", true);
		}
		return tfm;
	}

	public TypedFlatMap findDadosVeiculoViagem(final TypedFlatMap criteria) {
		final Long idMeioTransporte = criteria.getLong("idMeioTransporte");
		final Long idControleCarga = criteria.getLong("idControleCarga");
	
		return findDadosVeiculo(idMeioTransporte, idControleCarga);
	}

	private TypedFlatMap findDadosVeiculo(final Long idMeioTransporte, final Long idControleCarga) {
		/** Valida Veiculo Controle Carga: LMS-671 */
		final ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		final MeioTransporte meioTransporteTransportado = controleCarga.getMeioTransporteByIdTransportado();
		if(meioTransporteTransportado == null || !idMeioTransporte.equals(meioTransporteTransportado.getIdMeioTransporte())) {
    	controleCargaService.validateVeiculoControleCarga(idMeioTransporte, false);
		}
		
		MeioTransporte meioTransporte =  meioTransporteService.findById(idMeioTransporte);
		solicitacaoContratacaoService.validateSolicitacaoMeioTransporteColetaEntrega(controleCarga.getTpControleCarga().getValue(), meioTransporte, SessionUtils.getFilialSessao());

		final TypedFlatMap toReturn = new TypedFlatMap();
		toReturn.putAll( findProprietarioVeiculo(idMeioTransporte) );

		final TipoMeioTransporte tipoMeioTransporte = tipoMeioTransporteService.findTipoMeioTransporteCompostoByIdMeioTransporte(idMeioTransporte);
    	if (tipoMeioTransporte != null) {
			toReturn.put("idTipoMeioTransporte", tipoMeioTransporte.getIdTipoMeioTransporte());
    	}
		return toReturn;
    }

    /**
     * 
     * @param idMeioTransporte
     * @param mapRetorno
     */
    @SuppressWarnings("rawtypes")
    private TypedFlatMap findProprietarioVeiculo(Long idMeioTransporte) {
    	TypedFlatMap toReturn = new TypedFlatMap();

    	Map mapResultado = meioTranspProprietarioService.findProprietarioByMeioTransporte(idMeioTransporte);
    	if (mapResultado != null) {
	    	Map mapProprietario = (Map)mapResultado.get("proprietario");
	    	if (mapProprietario != null) {
	    		toReturn.put("proprietario_idProprietario", mapProprietario.get("idProprietario"));
	    		Map mapPessoa = (Map)mapProprietario.get("pessoa");
	    		if (mapPessoa != null) {
	    			toReturn.put("proprietario_pessoa_nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(
		    			(String)((Map)mapPessoa.get("tpIdentificacao")).get("value"), (String)mapPessoa.get("nrIdentificacao"))); 
		    		toReturn.put("proprietario_pessoa_nmPessoa", mapPessoa.get("nmPessoa"));
		    	}
		    }
    	}
    	return toReturn;
    }

    
    /**
     * 
     * @param criteria
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public TypedFlatMap findDadosSolicitacaoContratacao(TypedFlatMap criteria) {
		Long idSolicitacaoContratacao = criteria.getLong("idSolicitacaoContratacao");
		Long idControleCarga = criteria.getLong("idControleCarga");

		SolicitacaoContratacao sc = solicitacaoContratacaoService.findById(idSolicitacaoContratacao);
		String nrIdentificacaoMeioTransp = sc.getNrIdentificacaoMeioTransp();
		String nrIdentificacaoSemiReboque = sc.getNrIdentificacaoSemiReboque();

		final ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
		if(!idSolicitacaoContratacao.equals(controleCarga.getSolicitacaoContratacao().getIdSolicitacaoContratacao())){
		solicitacaoContratacaoService.validateExistSolicitacaoContratacao(
				SessionUtils.getFilialSessao().getIdFilial(), idSolicitacaoContratacao);
		}

		if (sc.getRota() != null && controleCarga.getRota() != null && sc.getRota().getIdRota().compareTo(controleCarga.getRota().getIdRota()) != 0) {
			throw new BusinessException("LMS-05120");
		}

		TypedFlatMap toReturn = new TypedFlatMap();
		if ( !StringUtils.isBlank(nrIdentificacaoMeioTransp) ) {
			Map mapTransportado = new HashMap();
			mapTransportado.put("nrIdentificador", nrIdentificacaoMeioTransp);
			toReturn.putAll( findMeioTransporte(mapTransportado, "Transportado") );
			toReturn.putAll( findDadosVeiculo(toReturn.getLong("idMeioTransporteTransportado"), idControleCarga) );

			if (controleCarga.getMeioTransporteByIdTransportado() != null) {
				if ( !controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador().equals(nrIdentificacaoMeioTransp) ) {
					String meioTransporteSC = toReturn.getString("nrFrotaTransportado") + " " + nrIdentificacaoMeioTransp;
					String meioTransporteCC = controleCarga.getMeioTransporteByIdTransportado().getNrFrota() + " " + controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador();
					throw new BusinessException("LMS-05121", new Object[] {meioTransporteCC, meioTransporteSC});
				}
			}
		}

		if ( !StringUtils.isBlank(nrIdentificacaoSemiReboque) ) {
			Map mapSemiRebocado = new HashMap();
			mapSemiRebocado.put("nrIdentificador", nrIdentificacaoSemiReboque);
			toReturn.putAll( findMeioTransporte(mapSemiRebocado, "SemiRebocado") );

			if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
				if ( !controleCarga.getMeioTransporteByIdSemiRebocado().getNrIdentificador().equals(nrIdentificacaoSemiReboque) ) {
					String meioTransporteSC = toReturn.getString("nrFrotaSemiRebocado") + " " + nrIdentificacaoSemiReboque;
					String meioTransporteCC = controleCarga.getMeioTransporteByIdSemiRebocado().getNrFrota() + " " + controleCarga.getMeioTransporteByIdSemiRebocado().getNrIdentificador();
					throw new BusinessException("LMS-05122", new Object[] {meioTransporteCC, meioTransporteSC});
				}
			}
		} else {
			if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
				Map mapSemiRebocado = new HashMap();
				mapSemiRebocado.put("nrIdentificador", controleCarga.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
				toReturn.putAll(findMeioTransporte(mapSemiRebocado, "SemiRebocado"));
			}
		}

		if (sc.getVlFreteNegociado() != null) {
			toReturn.put("moedaVlFreteCarreteiro", sc.getMoedaPais().getMoeda().getSiglaSimbolo());
			toReturn.put("vlFreteCarreteiro", sc.getVlFreteNegociado());
		}
		return toReturn;
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
		return tfm;
    }

	
	public void validateMeioTransporteSemiRebocado(Long idMeioTransporte) {
   		controleCargaService.validateSemiReboqueControleCarga(idMeioTransporte);
	}
	
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Map findEventoControleCarga(TypedFlatMap criteria) {
		Map mapControleCarga = new HashMap();
		mapControleCarga.put("idControleCarga", criteria.getLong("idControleCarga"));
		
		Map map = new HashMap();
		map.put("controleCarga", mapControleCarga);
		map.put("tpEventoControleCarga", "EM");
		
		Boolean blPermiteAlteracao = Boolean.FALSE;
		
		List result = eventoControleCargaService.find(map);
		if (result.isEmpty())
			blPermiteAlteracao = Boolean.TRUE;
		
		Map mapRetorno = new HashMap();
		mapRetorno.put("blPermiteAlteracao", blPermiteAlteracao);
		
		return mapRetorno;
	}
	
	

	public TypedFlatMap findFilialById(Long idFilial) {
		Filial filial = filialService.findById(idFilial);
		TypedFlatMap result = new TypedFlatMap();		
		result.put("filial.sgFilial", filial.getSgFilial());
		result.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		return result;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private Boolean validateExisteDadosPagtoPedagioCc(Long idControleCarga) {
    	Map mapControleCarga = new HashMap();
    	mapControleCarga.put("idControleCarga", idControleCarga);
    	
    	Map map = new HashMap();
    	map.put("controleCarga", mapControleCarga);
    	Integer rowCount = pagtoPedagioCcService.getRowCount(map);
    	return rowCount.intValue() > 0;
	}
	
	
    /**
     * 
     * @param parameters
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public Map store(Map parameters) {
    	validaDadosTela((String)parameters.get("tpControleCargaValue"), (String)parameters.get("tpRotaViagemValue"), 
    			(String)parameters.get("tpVinculo"), (Long)parameters.get("idRotaViagemByRotaIdaVolta"), 
    			(Long)parameters.get("idMeioTransporteTransportado"), (Long)parameters.get("idSolicitacaoContratacao"));

    	try {
    		controleCargaService.storeManterControleCarga( mapToTypedFlatMap(parameters) );
    	} catch (BusinessException e) {
    		// Quando ocorre o bloqueio do meio de transporte é necessário armazenar a informação do bloqueio.
			if ("LMS-26044".equals(e.getMessageKey()) && e.getMessageArguments() != null) {
    			Object[] args = e.getMessageArguments();
    			controleCargaService.storeBloqueioViagemEventual(Long.parseLong(args[0].toString()), Long.parseLong(args[1].toString()), Boolean.parseBoolean(args[2].toString()));
    		}
			// Caso contrário passa quem sabe tratar o erro.
    		throw e;
    	}
    	
    	Map mapRetorno = new TypedFlatMap();
    	
    	Long idControleCarga = (Long)parameters.get("idControleCarga");
    	
		mapRetorno.putAll(findById(idControleCarga));
		
    	return mapRetorno;
	}

	
	private void validaDadosTela(String tpControleCarga, String tpRotaViagem, String tpVinculo, Long idRotaViagem, 
			Long idMeioTransporte, Long idSolicitacaoContratacao) 
	{
		if (tpControleCarga.equals("V") && (tpRotaViagem.equals("EX") || tpRotaViagem.equals("EC"))) {
			if (tpVinculo != null && tpVinculo.equals("A")) {
				// Verifica se existe um veículo cadastrado para a rota de viagem.
				if (!meioTransporteRotaViagemService.validateMeioTransporteWithRotaViagem(idRotaViagem, idMeioTransporte) && idSolicitacaoContratacao == null) {
					throw new BusinessException("LMS-05166");
			    }
			}
		}
	}

	
	@SuppressWarnings("rawtypes")
    private TypedFlatMap mapToTypedFlatMap(Map criteria) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idControleCarga", criteria.get("idControleCarga"));
		tfm.put("meioTransporteByIdTransportado.idMeioTransporte", criteria.get("idMeioTransporteTransportado"));
		tfm.put("meioTransporteByIdSemiRebocado.idMeioTransporte", criteria.get("idMeioTransporteSemiRebocado"));
		tfm.put("rotaColetaEntrega.idRotaColetaEntrega", criteria.get("idRotaColetaEntrega"));
		tfm.put("tpControleCargaValor", criteria.get("tpControleCargaValue"));
		tfm.put("solicitacaoContratacao.idSolicitacaoContratacao", criteria.get("idSolicitacaoContratacao"));
		tfm.put("tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega", criteria.get("idTipoTabelaColetaEntrega"));
		tfm.put("tabelaColetaEntrega.idTabelaColetaEntrega", criteria.get("idTabelaColetaEntrega"));
		tfm.put("motorista.idMotorista", criteria.get("idMotorista"));
		tfm.put("proprietario.idProprietario", criteria.get("idProprietario"));
		tfm.put("vlFreteCarreteiro", criteria.get("vlFreteCarreteiro"));
		tfm.put("veiculoInformadoManualmente", criteria.get("veiculoInformadoManualmente"));
		tfm.put("semiReboqueInformadoManualmente", criteria.get("semiReboqueInformadoManualmente"));
		tfm.put("motoristaInformadoManualmente", criteria.get("motoristaInformadoManualmente"));
		
		Map mapSolicitacaoSinal = (Map)criteria.get("solicitacaoSinal");
		if (mapSolicitacaoSinal != null) {
			tfm.put("solicitacaoSinal.nrTelefoneEmpresa", mapSolicitacaoSinal.get("nrTelefoneEmpresa"));
			tfm.put("solicitacaoSinal.nmEmpresaAnterior", mapSolicitacaoSinal.get("nmEmpresaAnterior"));
			tfm.put("solicitacaoSinal.nmResponsavelEmpresa", mapSolicitacaoSinal.get("nmResponsavelEmpresa"));
			tfm.put("solicitacaoSinal.blPertenceProjCaminhoneiro", mapSolicitacaoSinal.get("blPertenceProjCaminhoneiro"));
		}
		tfm.put("blEntregaDireta", criteria.get("blEntregaDireta"));
		return tfm;
	}
	
	
	/************************************************************************************
									INICIO - ControleTrecho
    ************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List findPaginatedControleTrecho(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
    	ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
    	List lista = controleTrechoService.findControleTrechoByControleCarga(idControleCarga, Boolean.TRUE, null, null);

		List listRetorno = new ArrayList();
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
			ControleTrecho ct = (ControleTrecho) iter.next();
			if (!ct.getBlTrechoDireto())
				continue;

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idControleTrecho", ct.getIdControleTrecho());
			tfm.put("idFilialOrigem", ct.getFilialByIdFilialOrigem().getIdFilial());
			tfm.put("sgFilialOrigem", ct.getFilialByIdFilialOrigem().getSgFilial());
			tfm.put("idFilialDestino", ct.getFilialByIdFilialDestino().getIdFilial());
			tfm.put("sgFilialDestino", ct.getFilialByIdFilialDestino().getSgFilial());
			tfm.put("dhSaida", ct.getDhSaida());
			tfm.put("dhChegada", ct.getDhChegada());
			tfm.put("dhPrevisaoSaida", ct.getDhPrevisaoSaida());
			tfm.put("nrDistancia", ct.getNrDistancia());
			tfm.put("hrTempoViagem", ct.getNrTempoViagem() == null ? null : JTFormatUtils.formatTime((ct.getNrTempoViagem().longValue() * 60), 2, 1));
			tfm.put("hrTempoOperacao", ct.getNrTempoOperacao() == null ? null : JTFormatUtils.formatTime((ct.getNrTempoOperacao().longValue() * 60), 2, 1));

			List listControleTrecho = controleTrechoService.findControleTrechoByControleCarga(
					idControleCarga, Boolean.TRUE, ct.getFilialByIdFilialDestino().getIdFilial(), null);

			if (!listControleTrecho.isEmpty()) {
				ControleTrecho controleTrecho = (ControleTrecho) listControleTrecho.get(0);
				if (controleTrecho.getDhSaida() != null && controleTrecho.getDhChegada() != null) {
					tfm.put("hrTempoOperacaoRealizado", JTFormatUtils.formatTime(
							(controleTrecho.getDhSaida().getMillis() - ct.getDhChegada().getMillis()) / 1000, 2, 1));
				}
			} else {
				tfm.put("hrTempoOperacaoRealizado", getHrTempoOperacaoRealizado(idControleCarga, 
						ct.getDhChegada(), cc.getFilialByIdFilialDestino().getIdFilial()));
			}
			
			tfm.put("vlRateio", getVlRateio(ct.getTrechoRotaIdaVolta().getIdTrechoRotaIdaVolta()));

			listRetorno.add(tfm);
		}
		return listRetorno;
	}
	
	private BigDecimal getVlRateio(Long idTrechoRotaIdaVolta) {
		TrechoRotaIdaVolta trechoRotaIdaVolta = trechoRotaIdaVoltaService.findById(idTrechoRotaIdaVolta);

		if (trechoRotaIdaVolta != null) {
			return trechoRotaIdaVolta.getVlRateio();
		}
		return null;
	}


	/**
	 * 
	 * @param idControleCarga
	 * @param dhChegada
	 * @return
	 */
	@SuppressWarnings("rawtypes")
    private String getHrTempoOperacaoRealizado(Long idControleCarga,
			DateTime dhChegada, Long idFilialDestino) {
		List listaEventos = eventoControleCargaService
				.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilialDestino, idControleCarga, "FD");

		if (!listaEventos.isEmpty()) {
			EventoControleCarga ecc = (EventoControleCarga) listaEventos.get(0);
			if (ecc.getDhEvento() != null && dhChegada != null) {
				return JTFormatUtils.formatTime((ecc.getDhEvento().getMillis() - dhChegada.getMillis()) / 1000, 2, 1);
			}
		}
		return null;
	}

	
	public void validateExistenciaInsercaoManualTrecho(TypedFlatMap parameters) {
		filialRotaCcService.validateExistenciaInsercaoManualByControleCarga(parameters.getLong("idControleCarga"));
	}


	@SuppressWarnings("rawtypes")
    public TypedFlatMap findDadosFilialRotaCc(TypedFlatMap criteria) {
		Long idFilialNova = criteria.getLong("idFilialNova");
		Long idFilialAnterior = criteria.getLong("idFilialAnterior");
		Long idControleCarga = criteria.getLong("idControleCarga");
		Long idFilialRotaCcAnterior = criteria.getLong("idFilialRotaCcAnterior");
		BigDecimal vlNrTempoDistanciaPadrao = (BigDecimal)parametroGeralService.findConteudoByNomeParametro("RELACAO_TEMPO_DISTANCIA_PADRAO", false);

		Filial filialAnterior = filialService.findById(idFilialAnterior);
		Filial filialNova = filialService.findById(idFilialNova);

		TypedFlatMap mapRetorno = new TypedFlatMap();
		FilialRotaCc filialRotaCcPosterior = filialRotaCcService.findFilialPosteriorByIdFilialRotaCc(idFilialRotaCcAnterior);
		if (filialRotaCcPosterior != null) {
			mapRetorno.put("idFilialPosteriorRota", filialRotaCcPosterior.getFilial().getIdFilial());
			mapRetorno.put("sgFilialPosteriorRota", filialRotaCcPosterior.getFilial().getSgFilial());
			mapRetorno.put("nmFantasiaFilialPosteriorRota", filialRotaCcPosterior.getFilial().getPessoa().getNmFantasia());
			mapRetorno.put("nrOrdemFilialPosteriorRota", filialRotaCcPosterior.getNrOrdem());
			mapRetorno.put("nrTempoViagem2", findNrDistancia(vlNrTempoDistanciaPadrao, filialNova, filialRotaCcPosterior.getFilial()));
		}

		List listControleTrecho = controleTrechoService.
				findControleTrechoByControleCarga(idControleCarga, Boolean.TRUE, idFilialAnterior, null);

		if (!listControleTrecho.isEmpty()) {
			mapRetorno.put("dhPrevisaoSaida", ((ControleTrecho)listControleTrecho.get(0)).getDhPrevisaoSaida());
		}

		mapRetorno.put("nrTempoViagem1", findNrDistancia(vlNrTempoDistanciaPadrao, filialAnterior, filialNova));
		return mapRetorno;
	}


	/**
	 * 
	 * @param idFilialAnterior
	 * @param idFilialDestino
	 * @return
	 */
	private String findNrDistancia(BigDecimal vlNrTempoDistanciaPadrao, Filial filialAnterior, Filial filialDestino) {
		TypedFlatMap tfm = controleTrechoService.findNrDistanciaAndNrTempoViagem(
				filialAnterior, filialDestino, vlNrTempoDistanciaPadrao);

		Long nrTempoViagem = Long.valueOf(tfm.getInteger("nrTempoViagem").intValue());
		return FormatUtils.converteMinutosParaHorasMinutos(nrTempoViagem, FormatUtils.ESCALA_HHH);
	}


	/**
	 * 
	 * @param criteria
	 */
	@SuppressWarnings("rawtypes")
    public void validateDadosDhSaida1(TypedFlatMap criteria) {
		List listControleTrecho = controleTrechoService.findControleTrechoByControleCarga(
				criteria.getLong("idControleCarga"), Boolean.TRUE, null, criteria.getLong("idFilialAnterior"));

		if (!listControleTrecho.isEmpty()) {
			DateTime dhPrevisaoSaida =  ((ControleTrecho)listControleTrecho.get(0)).getDhPrevisaoSaida();
			DateTime dhSaida1 = criteria.getDateTime("dhSaida1");
			if (dhPrevisaoSaida != null && dhSaida1.compareTo(dhPrevisaoSaida) <= 0)
				throw new BusinessException("LMS-05136");
		}
	}

	
	/**
	 * 
	 * @param criteria
	 */
	@SuppressWarnings("rawtypes")
    public void validateDadosDhSaida2(TypedFlatMap criteria) {
		DateTime dhSaida1 = criteria.getDateTime("dhSaida1");
		DateTime dhSaida2 = criteria.getDateTime("dhSaida2");
		if (dhSaida2.compareTo(dhSaida1) <= 0)
			throw new BusinessException("LMS-05133");
		
		List listControleTrecho = controleTrechoService.findControleTrechoByControleCarga(
				criteria.getLong("idControleCarga"), Boolean.TRUE, criteria.getLong("idFilialPosterior"), null);

		if (!listControleTrecho.isEmpty()) {
			DateTime dhPrevisaoSaida =  ((ControleTrecho)listControleTrecho.get(0)).getDhPrevisaoSaida();
			if (dhSaida2.compareTo(dhPrevisaoSaida) >= 0)
				throw new BusinessException("LMS-05134");
		}
	}
	/************************************************************************************
									FIM - ControleTrecho
	************************************************************************************/

	

	
	/************************************************************************************
								INICIO - FILIAL ROTA CC
    ************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List findFilialRotaCc(TypedFlatMap parameters) {
		Boolean blInseridoManualmente = parameters.getBoolean("blInseridoManualmente");
		Boolean blInclusaoTrecho = parameters.getBoolean("flagInclusaoTrecho");
		Long idControleCarga = parameters.getLong("idControleCarga");
		List lista = filialRotaCcService.findFilialRotaByIdControleCarga(idControleCarga, blInseridoManualmente, null);

		List retorno = new ArrayList();
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
			FilialRotaCc filialRotaCc = (FilialRotaCc)iter.next();
			if (blInseridoManualmente != null && filialRotaCc.getBlInseridoManualmente().compareTo(blInseridoManualmente) != 0)
				continue;
			
			if (blInclusaoTrecho != null && blInclusaoTrecho && !iter.hasNext())
				continue;

			TypedFlatMap mapRetorno = new TypedFlatMap();
			mapRetorno.put("idFilialRotaCc", filialRotaCc.getIdFilialRotaCc());
			mapRetorno.put("idFilial", filialRotaCc.getFilial().getIdFilial());
			mapRetorno.put("sgFilial", filialRotaCc.getFilial().getSgFilial());
			mapRetorno.put("sgFilialConcatenado", filialRotaCc.getFilial().getSgFilial() + " - " + filialRotaCc.getFilial().getPessoa().getNmFantasia());
			mapRetorno.put("nrOrdem", filialRotaCc.getNrOrdem());
			retorno.add(mapRetorno);
		}
		return retorno;
	}
	
	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public TypedFlatMap storeIncluirTrecho(TypedFlatMap parameters) {
		Integer nrTempoViagem1 = Integer.valueOf(
			FormatUtils.converteHorasMinutosParaMinutos(parameters.getString("nrTempoViagem1"),FormatUtils.ESCALA_HHH).intValue());
		DateTime dhPrevisaoSaida1 = parameters.getDateTime("dhSaida1");
		DateTime dhPrevisaoChegada1 = dhPrevisaoSaida1.plusMinutes(nrTempoViagem1.intValue());

		DateTime dhPrevisaoSaida2 = parameters.getDateTime("dhSaida2");
		Integer nrTempoViagem2 = null;
		DateTime dhPrevisaoChegada2 = null;
		if (dhPrevisaoSaida2 != null) {
			nrTempoViagem2 = Integer.valueOf(
				FormatUtils.converteHorasMinutosParaMinutos(parameters.getString("nrTempoViagem2"),FormatUtils.ESCALA_HHH).intValue());
			dhPrevisaoChegada2 = dhPrevisaoSaida2.plusMinutes(nrTempoViagem2.intValue());
		}

		Long idControleCarga = parameters.getLong("idControleCarga");

		filialRotaCcService.storeInclusaoFilialRota(idControleCarga,
													parameters.getLong("idFilialAnteriorRota"),
													parameters.getLong("idFilial"),
													parameters.getLong("idFilialPosteriorRota"),
													Integer.valueOf(parameters.getByte("nrOrdemFilialAnteriorRota").intValue()),
													dhPrevisaoSaida1, dhPrevisaoChegada1, nrTempoViagem1, 
										    		dhPrevisaoSaida2, dhPrevisaoChegada2, nrTempoViagem2);

		TypedFlatMap tfm = new TypedFlatMap();
    	if (pagtoPedagioCcService.validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(idControleCarga)) {
    		tfm.put("blNecessitaCartaoPedagio", Boolean.TRUE);
    	}

    	ControleCarga cc = controleCargaService.findById(idControleCarga);
    	tfm.put("hrTempoViagem", FormatUtils.converteMinutosParaHorasMinutos(Long.valueOf(cc.getNrTempoViagem()), FormatUtils.ESCALA_HHH));
    	return tfm;
    }

	/**
	 * 
	 * @param parameters
	 * @return
	 */
	public TypedFlatMap removeFilialRota(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
		Long idFilial = parameters.getLong("idFilial");
		filialRotaCcService.removeFilialRota(idControleCarga, idFilial);
		
		TypedFlatMap tfm = new TypedFlatMap();
    	if (pagtoPedagioCcService.validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(idControleCarga)) {
    		tfm.put("blNecessitaCartaoPedagio", Boolean.TRUE);
    	}
    	
    	ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
    	tfm.put("hrTempoViagem", FormatUtils.converteMinutosParaHorasMinutos(Long.valueOf(cc.getNrTempoViagem()), FormatUtils.ESCALA_HHH));
    	return tfm;
	}

	
    /**
     * 
     * @param criteria
     */
    @SuppressWarnings("rawtypes")
    public void validateFilialNaRota(TypedFlatMap criteria) {
    	Long idControleCarga = criteria.getLong("idControleCarga");
    	Long idFilial = criteria.getLong("idFilial");
    	List lista = filialRotaCcService.findFilialRotaByIdControleCarga(idControleCarga, null, idFilial);
		if (!lista.isEmpty())
			throw new BusinessException("LMS-05116");
    }
    
    
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List findPaginatedControleTrechoByFilialRotaCc(TypedFlatMap parameters) {
		Byte nrOrdemPosterior = parameters.getByte("nrOrdemPosterior");
		if (nrOrdemPosterior == null)
			return Collections.EMPTY_LIST;

		// O número da ordem está sendo incrementado, pois o número recebido por parâmetro se refere a ordem 
		// antes da inclusão do novo trecho.
		nrOrdemPosterior = Byte.valueOf(Long.toString(nrOrdemPosterior.longValue()) );
		
		Long idControleCarga = parameters.getLong("idControleCarga");
		List listaIdFiliais = getFiliaisRotaByIdControleCargaByNrOrdem(idControleCarga, nrOrdemPosterior);
		List listaControleTrecho = controleTrechoService.findControleTrechoByControleCarga(idControleCarga, Boolean.TRUE, null, null);
		
		List listRetorno = new ArrayList();
		for (Iterator iter = listaControleTrecho.iterator(); iter.hasNext();) {
			ControleTrecho ct = (ControleTrecho) iter.next();
			if (!listaIdFiliais.contains(ct.getFilialByIdFilialOrigem().getIdFilial()))
				continue;

			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idControleTrecho", ct.getIdControleTrecho());
			tfm.put("idFilialByIdFilialOrigem", ct.getFilialByIdFilialOrigem().getIdFilial());
			tfm.put("sgFilialByIdFilialOrigem", ct.getFilialByIdFilialOrigem().getSgFilial());
			tfm.put("idFilialByIdFilialDestino", ct.getFilialByIdFilialDestino().getIdFilial());
			tfm.put("sgFilialByIdFilialDestino", ct.getFilialByIdFilialDestino().getSgFilial());
			tfm.put("dhPrevisaoSaida", ct.getDhPrevisaoSaida());
			tfm.put("hrTempoViagem", ct.getNrTempoViagem() == null ? null : JTFormatUtils.formatTime((ct.getNrTempoViagem().longValue() * 60), 2, 1));
			listRetorno.add(tfm);
		}
		return listRetorno;
	}


	/**
	 * 
	 * @param idControleCarga
	 * @param nrOrdemPosterior
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private List getFiliaisRotaByIdControleCargaByNrOrdem(Long idControleCarga, Byte nrOrdemPosterior) {
		List listaIdFiliais = new ArrayList();
		List result = filialRotaCcService.
				findFilialRotaCcWithNrOrdem(idControleCarga, Integer.valueOf(nrOrdemPosterior.intValue()), null, Boolean.TRUE);

    	for (Iterator iter = result.iterator(); iter.hasNext();) {
    		FilialRotaCc frCc = (FilialRotaCc)iter.next();
   			listaIdFiliais.add(frCc.getFilial().getIdFilial());
    	}
		return listaIdFiliais;
	}
	
	
	
	@SuppressWarnings("rawtypes")
    public void storeControleTrechoByFilialRotaCc(TypedFlatMap parameters) {
    	List listaTrechos = parameters.getList("controleTrechos");
		if (listaTrechos != null) {
			for (Iterator iterTrechos = listaTrechos.iterator(); iterTrechos.hasNext();) {
				TypedFlatMap map = (TypedFlatMap)iterTrechos.next();

				Long newIdControleTrecho = map.getLong("idControleTrecho");
				DateTime newDhPrevisaoSaida = map.getDateTime("dhPrevisaoSaida");

				ControleTrecho ct = controleTrechoService.findById(newIdControleTrecho);
				if (ct.getDhPrevisaoSaida().compareTo(newDhPrevisaoSaida) != 0) {
					ct.setDhPrevisaoSaida(newDhPrevisaoSaida);
					controleTrechoService.store(ct);
				}
			}
		}
	}
    /************************************************************************************
	 								FIM - FILIAL ROTA CC
	************************************************************************************/


	
	@SuppressWarnings("rawtypes")
    public List findPaginatedPagtoPedagioCc(TypedFlatMap parameters) {
		if (parameters.get("blAtualiza") != null && parameters.getBoolean("blAtualiza"))
			return generatePagtoPedagioCcByPostosPassagem(parameters);
		
    	Map mapValoresOperadora = pagtoPedagioCcService.findOperadoraCartaoPedagio();
    	List listaValor = (List)mapValoresOperadora.get("listaValor");
    	List listaDescricao = (List)mapValoresOperadora.get("listaDescricao");

    	List result = pagtoPedagioCcService.findPagtoPedagioCcByIdControleCarga(parameters.getLong("idControleCarga"));
    	return populateListaPagtoPedagioCc(result, listaValor, listaDescricao);
    }


	@SuppressWarnings({ "rawtypes", "unchecked" })
    private List populateListaPagtoPedagioCc(List listaPagtoPedagioCc, List listaValor, List listaDescricao) {
    	List listaRetorno = new ArrayList();
		for (Iterator iter = listaPagtoPedagioCc.iterator(); iter.hasNext();) {
    		PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();
    		
			TypedFlatMap map = new TypedFlatMap();
			map.put("idPagtoPedagioCc", pagtoPedagioCc.getIdPagtoPedagioCc());
			map.put("vlPedagio", pagtoPedagioCc.getVlPedagio());
			map.put("idMoeda", pagtoPedagioCc.getMoeda().getIdMoeda());
			map.put("siglaSimbolo", pagtoPedagioCc.getMoeda().getSiglaSimbolo());
			map.put("idTipoPagamPostoPassagem", pagtoPedagioCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem());
			map.put("dsTipoPagamPostoPassagem", pagtoPedagioCc.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem().toString());
			
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

	    	listaRetorno.add(map);
    	}
		return listaRetorno;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List findPaginatedPostoPassagemCc(TypedFlatMap parameters) {
		Long idControleCarga = parameters.getLong("idControleCarga");
		String tpControleCarga = parameters.getString("tpControleCarga");
    	List result = postoPassagemCcService.findPostoPassagemCcByIdControleCarga(idControleCarga);
    	List retorno = new ArrayList();
		for (Iterator iter = result.iterator(); iter.hasNext();) {
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
		return retorno;
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


    public void storePostosPassagem(TypedFlatMap map) {
		controleCargaService.storePostosPassagem(map.getLong("idControleCarga"), map.getList("pagamentos"), map.getList("postos"));
	}


	/**
	 * Método responsável por alterar os dados da grid pagtoPedagio de acordo com a grid postosPassagem. É chamado quando algum
	 * valor da combo forma de pagamento da grid postosPassagem for alterado.
	 * 
	 * @param parameters
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private List generatePagtoPedagioCcByPostosPassagem(TypedFlatMap parameters) {
		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(parameters.getLong("idControleCarga"));

		List listaPostos = parameters.getList("postos");
		Map mapVerificaPostoPassagem = new HashMap();
		
    	List result = new ArrayList();
    	for (Iterator iter = listaPostos.iterator(); iter.hasNext();) {
			TypedFlatMap mapPostoPassagem = (TypedFlatMap) iter.next();
			PostoPassagemCc ppCc = postoPassagemCcService.findById( mapPostoPassagem.getLong("postoPassagemCc_idPostoPassagemCc") );
			Long idTipoPagamPostoPassagem = mapPostoPassagem.getLong("tipoPagamPostoPassagem_idTipoPagamPostoPassagem");
			String key = idTipoPagamPostoPassagem.toString() + ppCc.getMoeda().getIdMoeda().toString();

			if (!mapVerificaPostoPassagem.containsKey(key)) {
				TipoPagamPostoPassagem tppp = tipoPagamPostoPassagemService.findById(idTipoPagamPostoPassagem);
				PagtoPedagioCc pagtoPedagioCc = new PagtoPedagioCc();
				pagtoPedagioCc.setIdPagtoPedagioCc(null);
				pagtoPedagioCc.setControleCarga(controleCarga);
				pagtoPedagioCc.setCartaoPedagio(null);
				pagtoPedagioCc.setMoeda(ppCc.getMoeda());
				pagtoPedagioCc.setOperadoraCartaoPedagio(null);
				pagtoPedagioCc.setTipoPagamPostoPassagem(tppp);
				pagtoPedagioCc.setVersao(null);
				pagtoPedagioCc.setVlPedagio(ppCc.getVlPagar());
				mapVerificaPostoPassagem.put(key, pagtoPedagioCc);
				result.add(pagtoPedagioCc);
			}
			else {
				PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc)mapVerificaPostoPassagem.get(key);
				pagtoPedagioCc.setVlPedagio( pagtoPedagioCc.getVlPedagio().add(ppCc.getVlPagar()) );
			}
    	}
    	
    	List listaValor = Collections.EMPTY_LIST;
    	List listaDescricao = Collections.EMPTY_LIST;
    	if (!result.isEmpty()) {
        	Map mapValoresOperadora = pagtoPedagioCcService.findOperadoraCartaoPedagio();
        	listaValor = (List)mapValoresOperadora.get("listaValor");
        	listaDescricao = (List)mapValoresOperadora.get("listaDescricao");
        	Collections.sort(result, new Comparator() {
        		public int compare(Object obj1, Object obj2) {
        			PagtoPedagioCc ppCc1 = (PagtoPedagioCc)obj1;
        			PagtoPedagioCc ppCc2 = (PagtoPedagioCc)obj2;
        			return ppCc1.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem().toString().compareTo(ppCc2.getTipoPagamPostoPassagem().getDsTipoPagamPostoPassagem().toString());
        		}
        	});
        	return populateListaPagtoPedagioCc(result, listaValor, listaDescricao);
    	}
    	return Collections.EMPTY_LIST;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedTrechoCorporativo(TypedFlatMap criteria) {
		List listaTrechos = trechoCorporativoService.findByIdControleCarga(criteria.getLong("idControleCarga"), null, null);

		List lista = new ArrayList();
		for (Iterator iter = listaTrechos.iterator(); iter.hasNext();) {
			TrechoCorporativo tc = (TrechoCorporativo)iter.next();
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idTrechoCorporativo", tc.getIdTrechoCorporativo());
			tfm.put("sgFilialOrigem", tc.getFilialByIdFilialOrigem().getSgFilial());
			tfm.put("sgFilialDestino", tc.getFilialByIdFilialDestino().getSgFilial());
			tfm.put("vlFaixa1", tc.getVlFaixa1());
			tfm.put("vlFaixa2", tc.getVlFaixa2());
			tfm.put("vlFaixa3", tc.getVlFaixa3());
			tfm.put("hrSaida", tc.getHrSaida());
			tfm.put("hrPrevisao", tc.getQtdHorasPrev() == null ? null : JTFormatUtils.formatTime((tc.getQtdHorasPrev().longValue() * 60), 2, 1)); 
			lista.add(tfm);
		}
		return new ResultSetPage(Integer.valueOf(1), lista); 
	}

	/**
     * 
     * @param criteria
     * @return
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public ResultSetPage findPaginatedTrechoCorporativoSelecaoAgrupador(TypedFlatMap criteria) {
		List listaRotasExpressas = rotasExpressasService.findByNrRotaIdaVolta(criteria.getInteger("nrRotaIdaVolta"), null);
		List lista = new ArrayList();
		for (Iterator iter = listaRotasExpressas.iterator(); iter.hasNext();) {
			RotasExpressas re = (RotasExpressas)iter.next();
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("sgFilialOrigem", filialService.findFilialBySgFilialLegado(re.getSgUnidOrigem()).getSgFilial());
			tfm.put("sgFilialDestino", filialService.findFilialBySgFilialLegado(re.getSgUnidDestino()).getSgFilial());
			tfm.put("vlFaixa1", re.getVlFaixa1());
			tfm.put("vlFaixa2", re.getVlFaixa2());
			tfm.put("vlFaixa3", re.getVlFaixa3());
			tfm.put("tpAgrupador", re.getTpAgrupador());
			lista.add(tfm);
		}
		return new ResultSetPage(Integer.valueOf(1), lista); 
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
    public ResultSetPage findPaginatedAdiantamentoTrecho(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("idControleCarga");
		List listaTrechos = adiantamentoTrechoService.findByIdControleCarga(idControleCarga, null, null);
		Long idFilialUsuario = SessionUtils.getFilialSessao().getIdFilial();
		
		Boolean blExisteControleCargaEmitido = controleCargaService.validateExisteEventoEmitido(idControleCarga);

		List lista = new ArrayList();
		for (Iterator iter = listaTrechos.iterator(); iter.hasNext();) {
			AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho)iter.next();
			if (!adiantamentoTrecho.getFilialByIdFilialOrigem().getIdFilial().equals(idFilialUsuario)) {
				continue;
			}
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idAdiantamentoTrecho", adiantamentoTrecho.getIdAdiantamentoTrecho());
			tfm.put("idFilialOrigem", adiantamentoTrecho.getFilialByIdFilialOrigem().getIdFilial());
			tfm.put("sgFilialOrigem", adiantamentoTrecho.getFilialByIdFilialOrigem().getSgFilial());
			tfm.put("sgFilialDestino", adiantamentoTrecho.getFilialByIdFilialDestino().getSgFilial());
			tfm.put("vlFrete", adiantamentoTrecho.getVlFrete());
			tfm.put("vlAdiantamento", adiantamentoTrecho.getVlAdiantamento());
			tfm.put("pcFrete", adiantamentoTrecho.getPcFrete());
			tfm.put("tpStatusRecibo", adiantamentoTrecho.getTpStatusRecibo().getValue());

			if (blExisteControleCargaEmitido) {
				tfm.put("blPermiteAlterarPerc", Boolean.FALSE);
			}
			else {
				Boolean blExisteManifestoEmitido = manifestoService.findExisteManifestoEmitidoByControleCarga(
							idControleCarga, 
							adiantamentoTrecho.getFilialByIdFilialOrigem().getIdFilial(), 
							adiantamentoTrecho.getFilialByIdFilialDestino().getIdFilial());
	
				tfm.put("blPermiteAlterarPerc", !blExisteManifestoEmitido);
			}
			lista.add(tfm);
		}
		return new ResultSetPage(Integer.valueOf(1), lista); 
	}


    @SuppressWarnings({ "unchecked", "rawtypes", "unused" })
    public void storeAdiantamentoTrecho(TypedFlatMap map) {
    	Long idControleCarga = map.getLong("idControleCarga");
    	Long idBeneficiario = map.getLong("idBeneficiario");
    	List<Map> listaAdiantamentos = map.getList("adiantamentos");
    	for (Map adiantamento : listaAdiantamentos) {
			if("PO".equals(map.getString("tpBeneficiario"))){
				adiantamento.put("postoConveniado", map.get("idBeneficiario"));
			}
		}
    
		adiantamentoTrechoService.storeAdiantamentoTrechoByControleCarga(idControleCarga, map.getList("adiantamentos"));
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
	
	
    public TypedFlatMap findDadosBeneficiario(TypedFlatMap criteria) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.putAll( findProprietarioVeiculo(criteria.getLong("idMeioTransporte")) );

		// Busca dados do beneficiário
    	Long idProprietario = tfm.getLong("proprietario_idProprietario");
		tfm.putAll( findBeneficiario(idProprietario, criteria.getLong("idControleCarga"), criteria.getString("tpBeneficiarioAdiantamento")) );
		
    	return tfm;
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
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
    
    public String visualizar(TypedFlatMap parameters) throws Exception {
    	if(ConstantesExpedicao.TP_CONTROLE_CARGA_COLETA_ENTREGA.equalsIgnoreCase((String)parameters.get("tpControleCargaValue"))){
    		return this.reportExecutionManager.generateReportLocator(this.emitirRelatorioControleCargaColetaEntregaService, parameters);
    	}else{
    		return this.reportExecutionManager.generateReportLocator(this.emitirRelatorioControleCargaViagemService, parameters);
    	}
	}
    
	public void setPostoConveniadoService(PostoConveniadoService postoConveniadoService) {
		this.postoConveniadoService = postoConveniadoService;
	}
	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}
	public void setEmitirRelatorioControleCargaColetaEntregaService(
			EmitirRelatorioControleCargaColetaEntregaService emitirRelatorioControleCargaColetaEntregaService) {
		this.emitirRelatorioControleCargaColetaEntregaService = emitirRelatorioControleCargaColetaEntregaService;
	}
	public EmitirRelatorioControleCargaViagemService getEmitirRelatorioControleCargaViagemService() {
		return emitirRelatorioControleCargaViagemService;
	}
	public void setEmitirRelatorioControleCargaViagemService(
			EmitirRelatorioControleCargaViagemService emitirRelatorioControleCargaViagemService) {
		this.emitirRelatorioControleCargaViagemService = emitirRelatorioControleCargaViagemService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setTabelaFreteCarreteiroCeService(
			TabelaFreteCarreteiroCeService tabelaFreteCarreteiroCeService) {
		this.tabelaFreteCarreteiroCeService = tabelaFreteCarreteiroCeService;
	}

	public void setTrechoRotaIdaVoltaService(
			TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
		this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
	}
}