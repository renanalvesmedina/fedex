package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.LocalTroca;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.SemiReboqueCc;
import com.mercurio.lms.carregamento.model.dao.SemiReboqueCcDAO;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.entrega.model.service.CancelarManifestoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PontoParadaService;
import com.mercurio.lms.municipios.model.service.RodoviaService;
import com.mercurio.lms.sgr.model.service.GerarEnviarSMPService;
import com.mercurio.lms.sgr.model.service.SolicMonitPreventivoService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.semiReboqueCcService"
 */
public class SemiReboqueCcService extends CrudService<SemiReboqueCc, Long> {

	private CancelarManifestoService cancelarManifestoService;
	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private EventoControleCargaService eventoControleCargaService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private GerarEnviarSMPService gerarEnviarSMPService;
	private LocalTrocaService localTrocaService;
	private ManifestoService manifestoService;
	private MeioTransporteService meioTransporteService;
	private MunicipioService municipioService;
	private PontoParadaService pontoParadaService;
	private PostoPassagemCcService postoPassagemCcService;
	private RodoviaService rodoviaService;
	private SolicMonitPreventivoService solicMonitPreventivoService;

	
	public void setCancelarManifestoService(CancelarManifestoService cancelarManifestoService) {
		this.cancelarManifestoService = cancelarManifestoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}
	public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}
	public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
		this.postoPassagemCcService = postoPassagemCcService;
	}
	public void setGerarEnviarSMPService(GerarEnviarSMPService gerarEnviarSMPService) {
		this.gerarEnviarSMPService = gerarEnviarSMPService;
	}
	public void setSolicMonitPreventivoService(SolicMonitPreventivoService solicMonitPreventivoService) {
		this.solicMonitPreventivoService = solicMonitPreventivoService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setLocalTrocaService(LocalTrocaService localTrocaService) {
		this.localTrocaService = localTrocaService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
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

	/**
	 * Recupera uma instância de <code>SemiReboqueCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public SemiReboqueCc findById(java.lang.Long id) {
        return (SemiReboqueCc)super.findById(id);
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
    public java.io.Serializable store(SemiReboqueCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSemiReboqueCcDAO(SemiReboqueCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SemiReboqueCcDAO getSemiReboqueCcDAO() {
        return (SemiReboqueCcDAO) getDao();
    }
   

	/**
	 * 
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, FindDefinition findDefinition) {
    	ResultSetPage rsp = getSemiReboqueCcDAO().findPaginatedByIdControleCarga(idControleCarga, findDefinition);
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
    	return getSemiReboqueCcDAO().getRowCountFindPaginatedByIdControleCarga(idControleCarga);
    }


    /**
     * 
     * @param idControleCarga
     * @return
     */
	public SemiReboqueCc findSemiReboqueCcByIdControleCarga(Long idControleCarga) {
		Map map = getSemiReboqueCcDAO().findSemiReboqueCcByIdControleCarga(idControleCarga);
		if (map == null)
			return null;
		List lista = new ArrayList();
		lista.add(map);
		lista = new AliasToNestedBeanResultTransformer(SemiReboqueCc.class).transformListResult(lista);
		return (SemiReboqueCc)lista.get(0);
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
     * @param idControleCarga
     * @param idMeioTransporteSemiRebocado
     */
    @SuppressWarnings("rawtypes")
	public Map storeTrocarSemiReboque( Integer nrKmRodoviaTroca, 
    									String dsTroca,
	    								DateTime dhTroca, 
	    								Long idMunicipio, 
	    								Long idControleTrecho,
	    								Long idPontoParada, 
	    								Long idRodovia, 
	    								Long idControleCarga, 
	    								Long idMeioTransporteSemiRebocado) 
    {
    	ControleTrecho controleTrecho = null;
    	Map retorno = new HashMap();
    	
    	if (idControleTrecho != null) {
    		controleTrecho = controleTrechoService.findById(idControleTrecho);
    	}

		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
    	MeioTransporte meioTransporteNovo = meioTransporteService.findByIdInitLazyProperties(idMeioTransporteSemiRebocado, false);
    	DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
    	Filial filialUsuario = SessionUtils.getFilialSessao();
    	
    	MeioTransporte meioTransporteAntigo = controleCarga.getMeioTransporteByIdSemiRebocado();

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
		localTroca.setIdLocalTroca((Long)localTrocaService.store(localTroca));

		SemiReboqueCc semiReboqueCc = findSemiReboqueCcByIdControleCarga(idControleCarga);
		semiReboqueCc.setDhTroca(dhTroca);
		semiReboqueCc.setLocalTroca(localTroca);
    	store(semiReboqueCc);

		SemiReboqueCc semiReboqueCcNovo = new SemiReboqueCc();
		semiReboqueCcNovo.setControleCarga(controleCarga);
		semiReboqueCcNovo.setMeioTransporte(meioTransporteNovo);
		semiReboqueCcNovo.setLocalTroca(null);
		semiReboqueCcNovo.setDhTroca(null);
		store(semiReboqueCcNovo);

		controleCarga.setMeioTransporteByIdSemiRebocado(meioTransporteNovo);
		controleCargaService.store(controleCarga);
		
		getSemiReboqueCcDAO().getAdsmHibernateTemplate().flush();

    	Long idSolicMonitPreventivo = solicMonitPreventivoService.findSmpByControleCargaLocalTroca(idControleCarga);
    	if (idSolicMonitPreventivo != null) {
	    	gerarEnviarSMPService.generateAlterarSMP(idSolicMonitPreventivo);
    	}

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

    	List listaEventos = eventoControleCargaService.
    			findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(null, idControleCarga, "SP");

    	if (listaEventos.isEmpty() && controleCarga.getFilialByIdFilialOrigem().getIdFilial().equals(filialUsuario.getIdFilial()) ) {
	    	Long idRotaColetaEntrega = null;
	    	if (controleCarga.getRotaColetaEntrega() != null) {
	    		idRotaColetaEntrega = controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega();
	    	}
			postoPassagemCcService.generatePostosPassagemCcAndPagtoPedagioCc(idControleCarga, 
																			 controleCarga.getTpControleCarga().getValue(), 
																			 idRotaColetaEntrega, 
																			 controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte(), 
																			 idMeioTransporteSemiRebocado);
	    }
    	
    	return retorno;
    }
}