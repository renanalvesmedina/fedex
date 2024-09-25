package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.AdiantamentoTrecho;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.LocalTroca;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PagtoProprietarioCc;
import com.mercurio.lms.carregamento.model.VeiculoControleCarga;
import com.mercurio.lms.carregamento.model.dao.VeiculoControleCargaDAO;
import com.mercurio.lms.carregamento.util.MeioTranspProprietarioBuilder;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.FluxoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.contratacaoveiculos.model.service.BloqueioMotoristaPropService;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.FluxoContratacaoService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.SolicitacaoContratacaoService;
import com.mercurio.lms.entrega.model.service.CancelarManifestoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaColetaEntregaCCService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TabelaColetaEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.TipoTabelaColetaEntregaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PontoParadaService;
import com.mercurio.lms.municipios.model.service.RodoviaService;
import com.mercurio.lms.sgr.model.SolicitacaoSinal;
import com.mercurio.lms.sgr.model.service.GerarEnviarSMPService;
import com.mercurio.lms.sgr.model.service.SolicMonitPreventivoService;
import com.mercurio.lms.sgr.model.service.SolicitacaoSinalService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.veiculoControleCargaService"
 */
public class VeiculoControleCargaService extends CrudService<VeiculoControleCarga, Long> {

	private AdiantamentoTrechoService adiantamentoTrechoService;
	private CancelarManifestoService cancelarManifestoService;
	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private EventoControleCargaService eventoControleCargaService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private FluxoContratacaoService fluxoContratacaoService;
	private GerarEnviarSMPService gerarEnviarSMPService;
	private LocalTrocaService localTrocaService;
	private ManifestoService manifestoService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private MeioTransporteService meioTransporteService;
	private MunicipioService municipioService;
	private PagtoProprietarioCcService pagtoProprietarioCcService;
	private PontoParadaService pontoParadaService;
	private PostoPassagemCcService postoPassagemCcService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private ProprietarioService proprietarioService;
	private RodoviaService rodoviaService;
	private SolicitacaoContratacaoService solicitacaoContratacaoService;
	private SolicitacaoSinalService solicitacaoSinalService;
	private SolicMonitPreventivoService solicMonitPreventivoService;
	private TabelaColetaEntregaService tabelaColetaEntregaService;
	private TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService;
	private TabelaColetaEntregaCCService tabelaColetaEntregaCCService;
	private BloqueioMotoristaPropService bloqueioMotoristaPropService;
	
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";
	
	private ConteudoParametroFilialService conteudoParametroFilialService;
	

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setCancelarManifestoService(CancelarManifestoService cancelarManifestoService) {
		this.cancelarManifestoService = cancelarManifestoService;
	}
	public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}
	public void setFluxoContratacaoService(FluxoContratacaoService fluxoContratacaoService) {
		this.fluxoContratacaoService = fluxoContratacaoService;
	}
	public void setAdiantamentoTrechoService(AdiantamentoTrechoService adiantamentoTrechoService) {
		this.adiantamentoTrechoService = adiantamentoTrechoService;
	}
	public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
		this.postoPassagemCcService = postoPassagemCcService;
	}
	public void setTabelaColetaEntregaService(TabelaColetaEntregaService tabelaColetaEntregaService) {
		this.tabelaColetaEntregaService = tabelaColetaEntregaService;
	}
	public void setTipoTabelaColetaEntregaService(TipoTabelaColetaEntregaService tipoTabelaColetaEntregaService) {
		this.tipoTabelaColetaEntregaService = tipoTabelaColetaEntregaService;
	}
	public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	public void setSolicitacaoSinalService(SolicitacaoSinalService solicitacaoSinalService) {
		this.solicitacaoSinalService = solicitacaoSinalService;
	}
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public void setGerarEnviarSMPService(GerarEnviarSMPService gerarEnviarSMPService) {
		this.gerarEnviarSMPService = gerarEnviarSMPService;
	}
	public void setSolicMonitPreventivoService(SolicMonitPreventivoService solicMonitPreventivoService) {
		this.solicMonitPreventivoService = solicMonitPreventivoService;
	}
	public void setPagtoProprietarioCcService(PagtoProprietarioCcService pagtoProprietarioCcService) {
		this.pagtoProprietarioCcService = pagtoProprietarioCcService;
	}
	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setSolicitacaoContratacaoService(SolicitacaoContratacaoService solicitacaoContratacaoService) {
		this.solicitacaoContratacaoService = solicitacaoContratacaoService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setLocalTrocaService(LocalTrocaService localTrocaService) {
		this.localTrocaService = localTrocaService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setPontoParadaService(PontoParadaService pontoParadaService) {
		this.pontoParadaService = pontoParadaService;
	}
	public void setRodoviaService(RodoviaService rodoviaService) {
		this.rodoviaService = rodoviaService;
	}
	public void setBloqueioMotoristaPropService(BloqueioMotoristaPropService bloqueioMotoristaPropService) {
		this.bloqueioMotoristaPropService = bloqueioMotoristaPropService;
	}
	

	/**
	 * Recupera uma instância de <code>VeiculoControleCarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public VeiculoControleCarga findById(java.lang.Long id) {
        return (VeiculoControleCarga)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(VeiculoControleCarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVeiculoControleCargaDAO(VeiculoControleCargaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VeiculoControleCargaDAO getVeiculoControleCargaDAO() {
        return (VeiculoControleCargaDAO) getDao();
    }
    
    
	/**
	 * 
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, FindDefinition findDefinition) {
    	ResultSetPage rsp = getVeiculoControleCargaDAO().findPaginatedByIdControleCarga(idControleCarga, findDefinition);
    	List result = new AliasToTypedFlatMapResultTransformer().transformListResult(rsp.getList());
    	rsp.setList(result);
    	return rsp;
    }

    /**
     * 
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountFindPaginatedByIdControleCarga(Long idControleCarga) {
    	return getVeiculoControleCargaDAO().getRowCountFindPaginatedByIdControleCarga(idControleCarga);
    }

   
    /**
     * 
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap findVeiculoAtualByIdControleCarga(Long idControleCarga) {
    	Map map = getVeiculoControleCargaDAO().findVeiculoAtualByIdControleCarga(idControleCarga);
    
    	if (map == null)
    		throw new BusinessException("LMS-05100");
    	meioTranspProprietarioService.validateBloqueioMeioTransporte(MeioTranspProprietarioBuilder.buildFromMeioTransporte((Long)map.get("idMeioTransporte")));
    	
    	return new AliasToTypedFlatMapResultTransformer().transformeTupleMap(map);
    }
    

    /**
     * 
     * @param nrKmRodoviaTroca
     * @param dsTroca
     * @param dhTroca
     * @param idMunicipio
     * @param idControleTrecho
     * @param idPontoParada
     * @param idRodovia
     * @param idVeiculoControleCarga
     * @param idControleCarga
     * @param idMeioTransporteTransportado
     * @param idSolicitacaoContratacao
     * @param idProprietario
     */
    @SuppressWarnings("rawtypes")
	public Map storeTrocarVeiculo( Integer nrKmRodoviaTroca,
						    		String dsTroca,
						    		DateTime dhTroca,
						    		Long idMunicipio,
						    		Long idControleTrecho,
						    		Long idPontoParada,
						    		Long idRodovia,
						    		Long idVeiculoControleCarga,
						    		Long idControleCarga, 
						    		Long idMeioTransporteTransportado, 
						    		Long idSolicitacaoContratacao,
						    		Long idProprietario,
						    		Long idTabelaColetaEntrega,
						    		Long idTipoTabelaColetaEntrega,
						    		Boolean isUsuarioDivop) 
    {
    	Map retorno = new HashMap();
    	if (JTDateTimeUtils.comparaData(dhTroca.toYearMonthDay(), JTDateTimeUtils.getDataAtual()) < 0)
    		throw new BusinessException("LMS-05179");

    	meioTransporteRodoviarioService.validateEstadoMeioTransporte(idMeioTransporteTransportado);
    	
    	ControleCarga controleCarga = controleCargaService.findById(idControleCarga);
    	SolicitacaoContratacao sc = null;
    	if (idSolicitacaoContratacao != null) {
    		sc = solicitacaoContratacaoService.findById(idSolicitacaoContratacao);
    		//Mudança CQPRO00004777
			generateAdiantamentoTrecho(idControleCarga, idSolicitacaoContratacao, controleCarga, sc);
    	}

    	DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
    	Filial filialUsuario = SessionUtils.getFilialSessao();
    	MeioTransporte meioTransporteNovo = meioTransporteService.findByIdInitLazyProperties(idMeioTransporteTransportado, false);

    	ControleTrecho controleTrecho = null;
    	if (idControleTrecho != null) {
    		controleTrecho = controleTrechoService.findById(idControleTrecho);
    	}

    	MeioTransporte meioTransporteAntigo = controleCarga.getMeioTransporteByIdTransportado();

    	LocalTroca localTroca = new LocalTroca();
    	localTroca.setDsTroca(dsTroca);
    	localTroca.setNrKmRodoviaTroca(nrKmRodoviaTroca);
    	localTroca.setMunicipio(municipioService.findById(idMunicipio));
		localTroca.setControleTrecho(controleTrecho);
    	if (idPontoParada != null) {
    		localTroca.setPontoParada(pontoParadaService.findById(idPontoParada));
    	}
    	if (idRodovia != null) {
    		localTroca.setRodovia(rodoviaService.findById(idRodovia));
    	}
    	localTroca.setIdLocalTroca( (Long)localTrocaService.store(localTroca) );

    	VeiculoControleCarga veiculoControleCarga = findById(idVeiculoControleCarga);
    	veiculoControleCarga.setDhTroca(dhTroca);
    	veiculoControleCarga.setLocalTroca(localTroca);
    	veiculoControleCarga.setUsuarioByIdFuncAlteraStatus(SessionUtils.getUsuarioLogado());
    	
    	ControleCarga lastControleCarga = new ControleCarga();
    	
    	if (bloqueioMotoristaPropService.executeBloqueioEventual(controleCarga, false, idMeioTransporteTransportado, lastControleCarga)) {
    		Object[] args = new Object[]{lastControleCarga.getIdControleCarga(), idMeioTransporteTransportado, false};
    		
    		throw new BusinessException("LMS-26044", args);
    	}
    	
    	store(veiculoControleCarga);
    	
    	TabelaColetaEntrega tabelaColetaEntrega = null;
    	if (idTabelaColetaEntrega != null) {
    		tabelaColetaEntrega = tabelaColetaEntregaService.findById(idTabelaColetaEntrega);
    	}
    	TipoTabelaColetaEntrega tipoTabelaColetaEntrega = null;
    	if (!isCalculoPadrao() && idTipoTabelaColetaEntrega != null) {
    		tipoTabelaColetaEntrega = tipoTabelaColetaEntregaService.findById(idTipoTabelaColetaEntrega);
    	}
    	Proprietario proprietario = proprietarioService.findById(idProprietario);

    	BigDecimal vlPagamento = BigDecimalUtils.ZERO;
    	Moeda moedaSolicContratacao = null;


    	VeiculoControleCarga vccNovo = new VeiculoControleCarga();
    	vccNovo.setControleCarga(controleCarga);
    	vccNovo.setMeioTransporte(meioTransporteNovo);
    	vccNovo.setLocalTroca(null);
    	if (idSolicitacaoContratacao != null) {
    		vccNovo.setSolicitacaoContratacao(sc);
    		controleCarga.setSolicitacaoContratacao(sc);
    		vlPagamento = sc.getVlFreteNegociado();
    		moedaSolicContratacao = sc.getMoedaPais().getMoeda();
    	}
    	vccNovo.setDhTroca(null);
    	store(vccNovo);

    	Long idPpcc = pagtoProprietarioCcService.
    			validateExistePagtoProprietarioCcByControleCargaByProprietario(idControleCarga, idProprietario);

    	if (idPpcc == null) {
	    	PagtoProprietarioCc pagtoProprietarioCc = new PagtoProprietarioCc();
	    	pagtoProprietarioCc.setControleCarga(controleCarga);
	    	pagtoProprietarioCc.setProprietario(proprietario);
	    	pagtoProprietarioCc.setVeiculoControleCarga(vccNovo);
	    	pagtoProprietarioCc.setMoeda(moedaSolicContratacao);
	    	pagtoProprietarioCc.setVlPagamento(vlPagamento);
	    	pagtoProprietarioCcService.store(pagtoProprietarioCc);
    	}
    	else
    	if (idSolicitacaoContratacao != null) {
    		PagtoProprietarioCc pagtoProprietarioCc = pagtoProprietarioCcService.findById(idPpcc);
    		BigDecimal vlPagamentoProprietarioCc = null;
			if (vlPagamento != null && vlPagamento.compareTo(BigDecimalUtils.ZERO) != 0) {
				vlPagamentoProprietarioCc = vlPagamento;
    		}
    		if (pagtoProprietarioCc.getVlPagamento() != null) {
    			if (vlPagamentoProprietarioCc == null)
    				vlPagamentoProprietarioCc = pagtoProprietarioCc.getVlPagamento();
    			else
    				vlPagamentoProprietarioCc = vlPagamentoProprietarioCc.add(pagtoProprietarioCc.getVlPagamento());
    		}
    		pagtoProprietarioCc.setVlPagamento(vlPagamentoProprietarioCc);
	    	pagtoProprietarioCcService.store(pagtoProprietarioCc);
    	}

    	
    	if (!isCalculoPadrao() && "C".equals(controleCarga.getTpControleCarga().getValue())){
    		
    		List<TabelaColetaEntregaCC> tabelasColetasCC = new ArrayList<TabelaColetaEntregaCC>();
        	if(controleCarga.getIdControleCarga() != null){
    			tabelasColetasCC = tabelaColetaEntregaCCService.findByIdContoleCarga(controleCarga.getIdControleCarga());
    		}
        	
    		List<TypedFlatMap> ltfm = tipoTabelaColetaEntregaService.findTipoTabelaColetaEntregaWithTabelaColetaEntrega(
    				controleCarga.getFilialByIdFilialOrigem().getIdFilial(), 
    				idMeioTransporteTransportado, 
    				controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());
    		tabelaColetaEntregaCCService.removeByIdControleCarga(idControleCarga);
    		
    		//LMS-4902
    		if("C1".equals( ltfm.get(0).get("tpCalculo") )){
        		controleCarga.setTabelaColetaEntrega(tabelaColetaEntrega);
    			controleCarga.setTipoTabelaColetaEntrega(tipoTabelaColetaEntrega);
    		} else {
    			controleCarga.setTabelaColetaEntrega(null);
        		controleCarga.setTipoTabelaColetaEntrega(null);
    			for (TypedFlatMap typedFlatMap : ltfm) {
    				TabelaColetaEntregaCC tabelaColetaEntregaCC = new TabelaColetaEntregaCC();
    				tabelaColetaEntregaCC.setControleCarga(controleCarga);

    				TabelaColetaEntrega tce = new TabelaColetaEntrega();
    				tce.setIdTabelaColetaEntrega(typedFlatMap.getLong("idTabelaColetaEntrega"));
    				tabelaColetaEntregaCC.setTabelaColetaEntrega(tce);
    				tabelasColetasCC.add(tabelaColetaEntregaCC);
    			}
    			tabelaColetaEntregaCCService.storeAll(tabelasColetasCC);
    		}
    		
    	}
    	
    	
    	controleCarga.setMeioTransporteByIdTransportado(meioTransporteNovo);
    	controleCarga.setProprietario(proprietario);
    	controleCargaService.store(controleCarga);
    	
    	
    	getVeiculoControleCargaDAO().getAdsmHibernateTemplate().flush();

    	List listaManifestosViagemEmitidos = manifestoService.
    			findManifestoByIdControleCarga(idControleCarga, filialUsuario.getIdFilial(), "ME", "V");

    	while (!listaManifestosViagemEmitidos.isEmpty()) {
    		Long idManifesto = ((Manifesto)listaManifestosViagemEmitidos.get(0)).getIdManifesto();
    		cancelarManifestoService.executeCancelarManifestoViagem(idManifesto, Boolean.TRUE, Boolean.FALSE);
    		listaManifestosViagemEmitidos = manifestoService.findManifestoByIdControleCarga(idControleCarga, filialUsuario.getIdFilial(), "ME", "V");
    	}


    	List<Manifesto> listaManifestosEntregaEmitidos = manifestoService.
				findManifestoByIdControleCarga(idControleCarga, filialUsuario.getIdFilial(), "ME", "E");

    	List<Long> idsManifestosEntrega = new ArrayList();
    	for (Manifesto manifestoEntrega : listaManifestosEntregaEmitidos) {
    		idsManifestosEntrega.add(manifestoEntrega.getIdManifesto());
    	}
    	cancelarManifestoService.executeCancelarManifestoComAproveitamento(idsManifestosEntrega);


    	EventoMeioTransporte emt = eventoMeioTransporteService.findLastEventoMeioTransporte(meioTransporteAntigo.getIdMeioTransporte(), idControleCarga);
    	DomainValue tpSituacaoMeioTransporteAntigo = null;
		if (emt != null) {
			tpSituacaoMeioTransporteAntigo = emt.getTpSituacaoMeioTransporte();

			EventoMeioTransporte emtAntigo = new EventoMeioTransporte();
			emtAntigo.setTpSituacaoMeioTransporte(new DomainValue("ADFR"));
			emtAntigo.setDhInicioEvento(dhAtual);
			emtAntigo.setControleCarga(controleCarga);
			emtAntigo.setControleTrecho(controleTrecho);
			emtAntigo.setFilial(filialUsuario);
			emtAntigo.setMeioTransporte(meioTransporteAntigo);
			emtAntigo.setUsuario(SessionUtils.getUsuarioLogado());
			eventoMeioTransporteService.generateEvent(emtAntigo);					
		}

		if (controleCargaService.validateExisteEventoEmitido(idControleCarga)) {
			//LMS-3544
    		retorno = controleCargaService.generateCancelamentoEmissaoControleCargaMdfe(idControleCarga);
    	}
    	else 
    		if (tpSituacaoMeioTransporteAntigo != null) {
	    		EventoMeioTransporte emtNovo = new EventoMeioTransporte();
	    		emtNovo.setControleCarga(controleCarga);
	    		emtNovo.setControleTrecho(controleTrecho);
	    		emtNovo.setDhInicioEvento(dhAtual);
	    		emtNovo.setFilial(filialUsuario);
	    		emtNovo.setMeioTransporte(meioTransporteNovo);
	    		emtNovo.setTpSituacaoMeioTransporte(tpSituacaoMeioTransporteAntigo);
	    		emtNovo.setUsuario(SessionUtils.getUsuarioLogado());
	    		eventoMeioTransporteService.generateEvent(emtNovo);
	    	}

    	Long idSolicMonitPreventivo = solicMonitPreventivoService.findSmpByControleCargaLocalTroca(idControleCarga);
    	if (idSolicMonitPreventivo != null) {
	    	gerarEnviarSMPService.generateAlterarSMP(idSolicMonitPreventivo);
    	}


    	Map mapControleCarga = new HashMap();
    	mapControleCarga.put("idControleCarga", idControleCarga);

    	Map mapSolicSinal = new HashMap();
    	mapSolicSinal.put("controleCarga", mapControleCarga);

    	List listaSolicitacaoSinal = solicitacaoSinalService.find(mapSolicSinal);
    	for (Iterator iter = listaSolicitacaoSinal.iterator(); iter.hasNext();) {
    		SolicitacaoSinal solicitacaoSinal = (SolicitacaoSinal)iter.next();
    		solicitacaoSinal.setTpStatusSolicitacao(new DomainValue("CA"));
    		solicitacaoSinalService.store(solicitacaoSinal);
    	}

    	List listaEventos = eventoControleCargaService.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "SP");
    	if (listaEventos.isEmpty() && controleCarga.getFilialByIdFilialOrigem().getIdFilial().equals(filialUsuario.getIdFilial()) ) {
	    	Long idRotaColetaEntrega = null;
	    	if (controleCarga.getRotaColetaEntrega() != null) {
	    		idRotaColetaEntrega = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
	    	}
	    		
	    	Long idMeioTransporteSemiRebocado = null;
	    	if (controleCarga.getMeioTransporteByIdSemiRebocado() != null) {
	    		idMeioTransporteSemiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte();
	    	}

			postoPassagemCcService.generatePostosPassagemCcAndPagtoPedagioCc(idControleCarga, 
																			 controleCarga.getTpControleCarga().getValue(), 
																			 idRotaColetaEntrega, 
																			 idMeioTransporteTransportado, 
																			 idMeioTransporteSemiRebocado);
    	}
    	return retorno;
    }


    //Mudança CQPRO00004777
	private void generateAdiantamentoTrecho(Long idControleCarga, Long idSolicitacaoContratacao, ControleCarga controleCarga, SolicitacaoContratacao sc) {
		List listaFluxoContratacao = fluxoContratacaoService.findByIdSolicitacaoContratacao(idSolicitacaoContratacao);
  	
		if (controleCarga.getFilialByIdFilialOrigem().getIdFilial().equals( SessionUtils.getFilialSessao().getIdFilial() )) {
			if (controleCarga.getSolicitacaoContratacao() != null) {
				adiantamentoTrechoService.removeByIdControleCarga(idControleCarga);
			}
			generateAdiantamentoTrecho(controleCarga, sc, listaFluxoContratacao);
		}
		else {
			if (controleCarga.getSolicitacaoContratacao() != null) {
				for (Iterator iter = listaFluxoContratacao.iterator(); iter.hasNext();) {
					FluxoContratacao fc = (FluxoContratacao)iter.next();
					List listaAdiantamentoTrechos = adiantamentoTrechoService.findByIdControleCarga(
							idControleCarga, fc.getFilialOrigem().getIdFilial(), fc.getFilialDestino().getIdFilial());

					for (Iterator iterAdiantamentoTrecho = listaAdiantamentoTrechos.iterator(); iterAdiantamentoTrecho.hasNext();) {
						AdiantamentoTrecho adiantamentoTrecho = (AdiantamentoTrecho)iterAdiantamentoTrecho.next();
						adiantamentoTrecho.setPcFrete(BigDecimalUtils.ZERO);
						adiantamentoTrecho.setReciboFreteCarreteiro(null);
						adiantamentoTrecho.setTpStatusRecibo(new DomainValue("G"));
						adiantamentoTrecho.setVlAdiantamento(BigDecimalUtils.ZERO);
						adiantamentoTrecho.setVlFrete( sc.getVlFreteNegociado().multiply(fc.getPcValorFrete()).divide(BigDecimalUtils.HUNDRED) );
						adiantamentoTrechoService.store(adiantamentoTrecho);
					}
				}
			}
			else
				generateAdiantamentoTrecho(controleCarga, sc, listaFluxoContratacao);
		}
	}


	private void generateAdiantamentoTrecho(ControleCarga controleCarga, SolicitacaoContratacao sc, List listaFluxoContratacao) {
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
			adiantamentoTrecho.setVlFrete( sc.getVlFreteNegociado().multiply(fc.getPcValorFrete()).divide(BigDecimalUtils.HUNDRED) );
			adiantamentoTrechoService.store(adiantamentoTrecho);
		}
	}
	
	/**
	 * @return
	 */
	private boolean isCalculoPadrao() {
		boolean calculoPadrao = false;
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			calculoPadrao = true;
		}
		return calculoPadrao;
	}
	
	public void setTabelaColetaEntregaCCService(
			TabelaColetaEntregaCCService tabelaColetaEntregaCCService) {
		this.tabelaColetaEntregaCCService = tabelaColetaEntregaCCService;
	}
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
}