package com.mercurio.lms.coleta.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.coleta.model.EventoColeta;
import com.mercurio.lms.coleta.model.EventoManifestoColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.dao.ManifestoColetaDAO;
import com.mercurio.lms.coleta.model.dao.PedidoColetaDAO;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.SetorService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.RotaColetaEntregaService;
import com.mercurio.lms.sim.model.dao.LMManifestoDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.manifestoColetaService"
 */
public class ManifestoColetaService extends CrudService<ManifestoColeta, Long> {

    private ControleCargaService controleCargaService;
    private MeioTransporteService meioTransporteService;
    private RotaColetaEntregaService rotaColetaEntregaService;
    private EventoControleCargaService eventoControleCargaService;
    private EventoManifestoColetaService eventoManifestoColetaService;
    private PedidoColetaService pedidoColetaService;
    private ConteudoParametroFilialService conteudoParametroFilialService;
    private FilialService filialService;
    private UsuarioService usuarioService;
    private SetorService setorService;
    private PedidoColetaDAO pedidoColetaDAO;
    private DomainValueService domainValueService;
    private EventoColetaService eventoColetaService;
    private OcorrenciaColetaService ocorrenciaColetaService; 
    private ConversaoMoedaService conversaoMoedaService;
    private LMManifestoDAO lmManifestoDao;
    private ConfiguracoesFacade configuracoesFacade;

    private static final String TP_STATUS_MANIFESTO_COLETA_EMITIDA = "EM";
    private static final String TP_STATUS_COLETA_MANIFESTADA = "MA";
    
    public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public OcorrenciaColetaService getOcorrenciaColetaService() {
        return ocorrenciaColetaService;
    }
    public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
        this.ocorrenciaColetaService = ocorrenciaColetaService;
    }
    public EventoColetaService getEventoColetaService() {
        return eventoColetaService;
    }
    public void setEventoColetaService(EventoColetaService eventoColetaService) {
        this.eventoColetaService = eventoColetaService;
    }
    public DomainValueService getDomainValueService() {
        return domainValueService;
    }
    public void setDomainValueService(DomainValueService domainValueService) {
        this.domainValueService = domainValueService;
    }
    public ControleCargaService getControleCargaService() {
        return controleCargaService;
    }
    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }
     public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public RotaColetaEntregaService getRotaColetaEntregaService() {
		return rotaColetaEntregaService;
	}
	public void setRotaColetaEntregaService(RotaColetaEntregaService rotaColetaEntregaService) {
		this.rotaColetaEntregaService = rotaColetaEntregaService;
	}
	public EventoControleCargaService getEventoControleCargaService() {
        return eventoControleCargaService;
    }
    public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
        this.eventoControleCargaService = eventoControleCargaService;
    }
    public EventoManifestoColetaService getEventoManifestoColetaService() {
        return eventoManifestoColetaService;
    }
    public void setEventoManifestoColetaService(EventoManifestoColetaService eventoManifestoColetaService) {
        this.eventoManifestoColetaService = eventoManifestoColetaService;
    }
    public PedidoColetaService getPedidoColetaService() {
        return pedidoColetaService;
    }
    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }
    public ConteudoParametroFilialService getConteudoParametroFilialService() {
        return conteudoParametroFilialService;
    }
    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }
    public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public SetorService getSetorService() {
		return setorService;
	}
	public void setSetorService(SetorService setorService) {
		this.setorService = setorService;
	}
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}


	public PedidoColetaDAO getPedidoColetaDAO() {
		return pedidoColetaDAO;
	}
	public void setPedidoColetaDAO(PedidoColetaDAO pedidoColetaDAO) {
		this.pedidoColetaDAO = pedidoColetaDAO;
	}

	/**
	 * Recupera uma instância de <code>ManifestoColeta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public ManifestoColeta findById(java.lang.Long id) {
        return (ManifestoColeta)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(Long idManifestoColeta) {
		ManifestoColeta manifestoColeta = this.findById(idManifestoColeta);
		
		//Desvincula pedidos de coleta...
    	for (Iterator iter = manifestoColeta.getPedidoColetas().iterator(); iter.hasNext();) {
			PedidoColeta pedidoColeta = (PedidoColeta) iter.next();
			pedidoColeta.setManifestoColeta(null);
			pedidoColeta.setTpStatusColeta(new DomainValue("AB"));
			this.getPedidoColetaDAO().store(pedidoColeta);
			this.getPedidoColetaDAO().getAdsmHibernateTemplate().flush();
		}
    	
    	//Deleta os eventos de controle carga e manifesto coleta que estao 
    	//relacionados com o controle de carga deste manifesto... 
    	for (Iterator iter = manifestoColeta.getEventoManifestoColetas().iterator(); iter.hasNext();) {
			EventoManifestoColeta eventoManifestoColeta = (EventoManifestoColeta) iter.next();
			this.getEventoManifestoColetaService().removeById(eventoManifestoColeta.getIdEventoManifestoColeta());
		}
    	
    	for (Iterator iter = manifestoColeta.getControleCarga().getEventoControleCargas().iterator(); iter.hasNext();) {
			EventoControleCarga eventoControleCarga = (EventoControleCarga) iter.next();
			this.getEventoControleCargaService().removeById(eventoControleCarga.getIdEventoControleCarga());
		}
    	
    	//Deleta o manifesto de coleta
        super.removeById(idManifestoColeta);
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
    	for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();			
			this.removeById(id);
		}        
    }
    
    /**
     * Tenta remover varias entidades de controle de carga.
     * Verifica a possibilidade de haver um manifesto de coleta vinculado a ele antes da remoção.
     * Caso exista, a remoção não é efetuada.
     * 
     * @param ids lista com as entidades que deverão ser removida.
	 *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    private void removeControleCargasByIds(List ids) {
    	for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			ControleCarga controleCarga = getControleCargaService().findById(id);
	    	if (controleCarga.getManifestoColetas().isEmpty()) { 
	    		this.getControleCargaService().removeById(id);
	    	}
		}
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ManifestoColeta manifestoColeta) {
    	DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
    	Long idManifestoColeta = null;
    	
    	if (manifestoColeta.getIdManifestoColeta() == null) {
    		Filial filial = getFilialService().findById(SessionUtils.getFilialSessao().getIdFilial());
    		RotaColetaEntrega rotaColetaEntrega = this.rotaColetaEntregaService.findById(manifestoColeta.getRotaColetaEntrega().getIdRotaColetaEntrega());

    		ControleCarga controleCarga = null;
    		if (manifestoColeta.getControleCarga() == null) {
	    		//Gerando novo controle de carga
	    		controleCarga = new ControleCarga();
	    		controleCarga.setBlEntregaDireta(Boolean.FALSE);
	    		controleCarga.setFilialByIdFilialOrigem(filial);
	    		controleCarga.setFilialByIdFilialAtualizaStatus(filial);
	    		controleCarga.setFilialByIdFilialDestino(filial);
	    		controleCarga.setNrControleCarga(
	    				configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_CONTROLE_CARGA", true));
	    		
	    		controleCarga.setTpControleCarga(new DomainValue("C"));
	    		controleCarga.setTpStatusControleCarga(new DomainValue("GE"));
	    		controleCarga.setRotaColetaEntrega(rotaColetaEntrega);
	    		controleCarga.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
	    		controleCarga.setMoeda(SessionUtils.getMoedaSessao());
	    		controleCarga.setIdControleCarga( (Long)this.getControleCargaService().store(controleCarga) );

	    		//Salvando um 'Evento Controle Carga'
	    		this.getControleCargaService().generateEventoChangeStatusControleCarga(
	        			controleCarga.getIdControleCarga(), controleCarga.getFilialByIdFilialOrigem().getIdFilial(), "GE");
	    		
	    		manifestoColeta.setControleCarga(controleCarga);
    		}

    		controleCarga = this.getControleCargaService().findByIdInitLazyProperties(manifestoColeta.getControleCarga().getIdControleCarga(), false);
    		
    		//Caso o manifesto coleta nao venha vazio
    		if (manifestoColeta.getControleCarga() != null){ 
    			//Busca um meio de transporte para salvar no caso dele ser informado 
        		if (manifestoColeta.getControleCarga().getMeioTransporteByIdTransportado()!=null) {
        			MeioTransporte meioTransporte =  this.getMeioTransporteService().findByIdInitLazyProperties(manifestoColeta.getControleCarga().getMeioTransporteByIdTransportado().getIdMeioTransporte(), false);
        			controleCarga.setMeioTransporteByIdTransportado(meioTransporte);
        		}
        		//Busca um meio de semi-rebocado para salvar no caso dele ser informado 
        		if (manifestoColeta.getControleCarga().getMeioTransporteByIdSemiRebocado()!=null) {
        			MeioTransporte meioTransporte =  this.getMeioTransporteService().findByIdInitLazyProperties(manifestoColeta.getControleCarga().getMeioTransporteByIdSemiRebocado().getIdMeioTransporte(), false);
        			controleCarga.setMeioTransporteByIdSemiRebocado(meioTransporte);
        		}
    		}

    		//LMS-7769 : Se o manifesto for gerado automaticamente para parceira (via manter pré-alertas), 
    		// a filial do manifesto deve ser a de destino do awb
    		if(manifestoColeta.getFilial() != null){
    			filial = manifestoColeta.getFilial();
    		}

    		Long nrManifesto = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_MANIFESTO_COLETA", true);

    		//Gerando Manifesto Coleta
    		manifestoColeta.setRotaColetaEntrega(rotaColetaEntrega);
    		manifestoColeta.setFilial(filial);
    		manifestoColeta.setNrManifesto(Integer.valueOf(nrManifesto.intValue()));
    		manifestoColeta.setTpStatusManifestoColeta(new DomainValue("GE"));
    		manifestoColeta.setDhGeracao(dataHoraAtual);

    		EventoManifestoColeta eventoManifestoColeta = new EventoManifestoColeta();
    		eventoManifestoColeta.setTpEventoManifestoColeta(new DomainValue("MG"));
    		eventoManifestoColeta.setDhEvento(dataHoraAtual);
    		
    		//Associando..
    		manifestoColeta.setControleCarga(controleCarga);
    		eventoManifestoColeta.setManifestoColeta(manifestoColeta);

    		//Gravando...
    		idManifestoColeta = (Long) super.store(manifestoColeta);
    		this.getEventoManifestoColetaService().store(eventoManifestoColeta);

    		controleCargaService.generateAtualizacaoTotaisParaCcColetaEntrega(controleCarga, Boolean.TRUE, filial);

    	} else {
    		ManifestoColeta manifestoColetaUpdate = manifestoColeta;
    		manifestoColeta = this.findById(manifestoColeta.getIdManifestoColeta());
    		
    		//Busca um meio de transporte para salvar no caso dele ser informado 
    		if (manifestoColetaUpdate.getControleCarga().getMeioTransporteByIdTransportado()!=null) {
    			MeioTransporte meioTransporte =  this.getMeioTransporteService().findByIdInitLazyProperties(manifestoColetaUpdate.getControleCarga().getMeioTransporteByIdTransportado().getIdMeioTransporte(), false);
    			manifestoColeta.getControleCarga().setMeioTransporteByIdTransportado(meioTransporte);
    		} else {
    			manifestoColeta.getControleCarga().setMeioTransporteByIdTransportado(null);
    		}
    		
    		//Busca um meio de semi-rebocado para salvar no caso dele ser informado 
    		if (manifestoColetaUpdate.getControleCarga().getMeioTransporteByIdSemiRebocado()!=null) {
    			MeioTransporte meioTransporte =  this.getMeioTransporteService().findByIdInitLazyProperties(manifestoColetaUpdate.getControleCarga().getMeioTransporteByIdSemiRebocado().getIdMeioTransporte(), false);
    			manifestoColeta.getControleCarga().setMeioTransporteByIdSemiRebocado(meioTransporte);
    		} else {
    			manifestoColeta.getControleCarga().setMeioTransporteByIdSemiRebocado(null);
    		}

    		idManifestoColeta = (Long) super.store(manifestoColeta);

    		controleCargaService.generateAtualizacaoTotaisParaCcColetaEntrega(manifestoColeta.getControleCarga(), Boolean.TRUE, SessionUtils.getFilialSessao());
    	}
        return idManifestoColeta;
    }


    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setManifestoColetaDAO(ManifestoColetaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ManifestoColetaDAO getManifestoColetaDAO() {
        return (ManifestoColetaDAO) getDao();
    }
    
    /**
     * Processo que monta os manifestos automaticamente para os pedidos coleta que estejam em
     * aberto e cuja data de previsão de coleta seja igual ou menor que a data no parâmetro DATA_PROCESSO_COLETA.
     * Retorna <code>true<code> se pelo menos uma coleta foi programadas.
     * Caso contrário retorna <code>false<code>.
     * @return boolean
     */
    public int generateManifestosAutomaticamente(){
        Filial filialDoUsuario = SessionUtils.getFilialSessao();
        YearMonthDay dataProcessoColetaFilial = null;
        int coletasProgramadas = 0;
        Filial filialUsuario = SessionUtils.getFilialSessao();
        try {
            dataProcessoColetaFilial = (YearMonthDay)getConteudoParametroFilialService().findConteudoByNomeParametro(filialDoUsuario.getIdFilial(), "DATA_PROCESSO_COLETA", false);
            if (dataProcessoColetaFilial==null){
            	String[] args = {filialUsuario.getSgFilial()};
            	throw new BusinessException("LMS-02006", args);
            }
        } catch (IllegalStateException e) {
            //Mesmo que o parâmetro esteja cadastrado, pode ser que ele não seja válido.
        	String[] args = {filialUsuario.getSgFilial()};
            throw new BusinessException("LMS-02006", args);
        }
        
        List <Long>listIdsRotasColetaEntrega = rotaColetaEntregaService.findIdsRotasColetaEntregaByTpStatusColetaUntilPrevisaoColeta("AB", filialDoUsuario.getIdFilial(), dataProcessoColetaFilial);
        for (Long idRotaColetaEntrega : listIdsRotasColetaEntrega) {
			List <PedidoColeta>pedidosColeta = pedidoColetaService.findPedidosColetaByStatusByIdRotaColetaEntregaUntilPrevisaoColeta("AB", filialDoUsuario.getIdFilial(), idRotaColetaEntrega, dataProcessoColetaFilial);
			ControleCarga controleCarga = controleCargaService.findControleCargaGEByIdRotaColetaEntregaWithLowerPsTotalFrota(idRotaColetaEntrega);
			ManifestoColeta manifestoColeta = null;
			if (controleCarga != null){
				manifestoColeta = findManifestoColetaMaisAntigoByIdControleCargaByTpStatusManifestoColeta(controleCarga.getIdControleCarga(), "GE");
				if (manifestoColeta == null){
					manifestoColeta = new ManifestoColeta();
					manifestoColeta.setRotaColetaEntrega(rotaColetaEntregaService.findById(idRotaColetaEntrega));
				}
				manifestoColeta.setControleCarga(controleCarga);
				this.store(manifestoColeta);

			} else {
				//Dentro do store é gerado um novo Controle de Carga
				manifestoColeta = new ManifestoColeta();
				manifestoColeta.setRotaColetaEntrega(rotaColetaEntregaService.findById(idRotaColetaEntrega));
				this.store(manifestoColeta);
				//Nesse ponto já existe um Controle de Carga dentro do ManifestoColeta
			}

			// Atualiza pedidos coleta
			for (PedidoColeta pedidoColeta : pedidosColeta) {
				pedidoColeta.setTpStatusColeta(new DomainValue("NM"));
				pedidoColeta.setManifestoColeta(manifestoColeta);
				pedidoColetaService.store(pedidoColeta);
			}

			controleCargaService
					.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(
							manifestoColeta.getControleCarga().getIdControleCarga(), Boolean.TRUE, SessionUtils.getFilialSessao());

			coletasProgramadas = coletasProgramadas + pedidosColeta.size();
		}
        return coletasProgramadas;
    }    
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param Map criteria 
     * @return Integer numero de registros
     */
    public Integer getRowCountByRotaColetaEntregaGE(Map criteria) {
    	return this.getControleCargaService().getRowCountByRotaColetaEntregaGE(criteria);
    } 
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um idRota seja informado.
     * Tem como restricao buscar apenas "Tipo de Status de Controle de Carga" e "Manifestos de Coleta" setado como "GE". 
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedByRotaColetaEntregaGE(Map criteria) {
    	ResultSetPage resultSetPage = this.getControleCargaService().findPaginatedByRotaColetaEntregaGE(criteria);
    	
    	for (int i=0; i<resultSetPage.getList().size(); i++) {
			Map row = (Map) resultSetPage.getList().get(i);
			String idManifestoColeta = (row.get("idManifestoColeta")!=null) ? String.valueOf(row.get("idManifestoColeta")) : "";
			row.put("id", row.get("idControleCarga") + idManifestoColeta);
		}
    	return resultSetPage; 
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "NM". 
     * 
     * @param Map criteria 
     * @return Integer numero de registros
     */
    public Integer getRowCountPedidosColeta(Map criteria) {
    	Long idManifestoColeta = Long.valueOf(((Map) criteria.get("manifestoColeta")).get("idManifestoColeta").toString());
    	return this.getPedidoColetaDAO().getRowCountByManifestoColeta(idManifestoColeta);
    } 
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um idManifestoColeta seja informado.
     * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "NM". 
     * 
     * @param Map criteria
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedPedidosColeta(Map criteria) {
    	Long idManifestoColeta = Long.valueOf(((Map) criteria.get("manifestoColeta")).get("idManifestoColeta").toString());
    	return this.getPedidoColetaDAO().findPaginatedByManifestoColeta(idManifestoColeta, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * Tem como restricao buscar apenas "Tipo de Status de Coleta" setado como "AB". 
     * 
     * @param TypedFlatMap criteria 
     * @return Integer numero de registros
     */
    public Integer getRowCountAdicionarColeta(TypedFlatMap criteria) {
    	Long idPedidoColeta = criteria.getLong("pedidoColeta.idPedidoColeta");
    	Long idRotaColetaEntrega = criteria.getLong("rotaColetaEntrega.idRotaColetaEntrega");
    	Long idFilialResponsavel = criteria.getLong("filialUsuario.idFilial");
    	return this.getPedidoColetaDAO().getRowCountByRotaColetaEntrega(idRotaColetaEntrega, idPedidoColeta, idFilialResponsavel);
    } 
    
    /**
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedAdicionarColeta(TypedFlatMap criteria) {
    	PedidoColeta pedidoColeta = null;
    	Long idPedidoColeta = criteria.getLong("pedidoColeta.idPedidoColeta");
    	Long idRotaColetaEntrega = criteria.getLong("rotaColetaEntrega.idRotaColetaEntrega");
    	Long idFilialResponsavel = criteria.getLong("filialUsuario.idFilial");
    	ResultSetPage resultSetPage = this.getPedidoColetaDAO().findPaginatedByRotaColetaEntrega(idRotaColetaEntrega, idPedidoColeta, idFilialResponsavel, FindDefinition.createFindDefinition(criteria));
    	
    	for (Iterator iter = resultSetPage.getList().iterator(); iter.hasNext();) {
			TypedFlatMap pedidoColetaMap = (TypedFlatMap) iter.next();
			pedidoColeta =  new PedidoColeta();
			pedidoColeta.setEdColeta(pedidoColetaMap.getString("edColeta"));
			pedidoColeta.setNrEndereco(pedidoColetaMap.getString("nrEndereco"));
			pedidoColeta.setDsBairro(pedidoColetaMap.getString("dsBairro"));
			pedidoColeta.setDsComplementoEndereco(pedidoColetaMap.getString("dsComplementoEndereco"));
			pedidoColetaMap.put("enderecoComComplemento", pedidoColeta.getEnderecoComComplemento());
		}
    	return resultSetPage;
    }

    /**
     * Faz o somatorio de todos os Pedidos de coletas (campos psTotalVerificado e 
     * vlTotalVerificado) e salva na tabela de Controle de Carga que esta relacionada com
     * uma determinada coleta.
     * 
     */
    public void storeControleCargaSumPedidoColeta(TypedFlatMap criteria) {
    	Long idManifestoColeta = criteria.getLong("manifestoColeta.idManifestoColeta");
    	ManifestoColeta manifestoColeta = this.findById(idManifestoColeta);
    	controleCargaService
				.generateAtualizacaoTotaisParaCcColetaEntregaByIdControleCarga(
						manifestoColeta.getControleCarga().getIdControleCarga(), Boolean.TRUE, SessionUtils.getFilialSessao());
    }


    /**
	 * Atualiza várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser atulizadas.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removePedidoColetaByManifestoColeta(List ids) {
    	for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long idPedidoColeta = (Long) iter.next();
			PedidoColeta pedidoColeta = this.getPedidoColetaService().findById(idPedidoColeta);
			//Atualiza...
			pedidoColeta.setTpStatusColeta(new DomainValue("AB"));
			pedidoColeta.setManifestoColeta(null);
			//Salva atualizacao...
			this.getPedidoColetaDAO().store(pedidoColeta);
    	}
    }
    
    /**
     * Faz a atualizacao de na tabela de coletas para a atualizacao dos registros
     * tpStatusColeta de 'AB' para 'NM'.
     * 
     * @param criteria
     */
    public void updateAdicionarColeta(Map criteria) {
    	Long idManifestoColeta = Long.valueOf((String)criteria.get("idManifestoColeta"));
    	ManifestoColeta manifestoColeta = this.findById(idManifestoColeta);
    	List idsPedidoColeta = (List) criteria.get("idsPedidoColeta");
    	for (Iterator iter = idsPedidoColeta.iterator(); iter.hasNext();) {
			String idPedidoColeta = (String) iter.next();
			//Buscando coleta...
			PedidoColeta pedidoColeta = this.getPedidoColetaService().findById(Long.valueOf(idPedidoColeta));
			
			//FIXME: Esta se perdendo para achar os ids
			
			//Atualizando dados...
			pedidoColeta.setTpStatusColeta(new DomainValue("NM"));
			pedidoColeta.setManifestoColeta(manifestoColeta);
			//Persistindo dados...
			this.getPedidoColetaService().store(pedidoColeta);
		}
    }
    
    public void executeRegistrarEmissaoManifestoColeta(Long idManifestoColeta){
    	List manifestosColetas = new ArrayList<Long>();
    	manifestosColetas.add(idManifestoColeta);
    	this.executeRegistrarEmissaoManifestoColeta(manifestosColetas);
    }
    
    
    /**
     * Método responsavel por registrar a emissão do manifesto de coleta.
     * Chamado após evento 02.01.02.06 Emitir Manifestos
     * @param Lista com os ids de manifestosColetas que serão atualizado o TP_STATUS_MANIFESTO_COLETA
     * @author Rodrigo Antunes
     */
    public void executeRegistrarEmissaoManifestoColeta(List manifestosColetas) {
        DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
    	
        DomainValue dv = getDomainValueService().findDomainValueByValue("DM_STATUS_MANIFESTO_COLETA", TP_STATUS_MANIFESTO_COLETA_EMITIDA);

        DomainValue domainValue = getDomainValueService().findDomainValueByValue("DM_STATUS_COLETA", TP_STATUS_COLETA_MANIFESTADA);

        // busca a ocorrencia coleta para o evento coleta
        Map criterioOcorrencia = new HashMap();
        criterioOcorrencia.put("tpEventoColeta", TP_STATUS_COLETA_MANIFESTADA);
        List ocorrenciasList = this.getOcorrenciaColetaService().find(criterioOcorrencia);
        OcorrenciaColeta ocorrenciaColeta = null;
        if (!ocorrenciasList.isEmpty()) {
        	ocorrenciaColeta = (OcorrenciaColeta)ocorrenciasList.get(0);
        }
        
        String labelManifestada = configuracoesFacade.getMensagem("manifestada");

        Iterator iter = manifestosColetas.iterator();
        while (iter.hasNext()) {
            Long idManifestoColeta = (Long)iter.next(); 

            ManifestoColeta manifestoColeta = this.findById(idManifestoColeta);
            manifestoColeta.setTpStatusManifestoColeta(dv);
            manifestoColeta.setDhEmissao(dataHoraAtual);
            manifestoColeta.setQtTotalColetasEmissao(getPedidoColetaService().getCountByManifestoColeta(idManifestoColeta));
            
            // busca o meio de transporte rodoviario
            MeioTransporteRodoviario meioTransporteRodoviario = null;
            if (manifestoColeta.getControleCarga()!=null) {
            	if(manifestoColeta.getControleCarga().getMeioTransporteByIdTransportado()!=null) {
            		meioTransporteRodoviario = 
            			manifestoColeta.getControleCarga().getMeioTransporteByIdTransportado().getMeioTransporteRodoviario();
            	}
            }
            
            
            Map idManifestoColetaMap = new HashMap();
            idManifestoColetaMap.put("idManifestoColeta", idManifestoColeta);
            
            Map map = new HashMap();
            map.put("manifestoColeta", idManifestoColetaMap);
            
            List pedidosColetas = getPedidoColetaService().find(map);

            Iterator pedidosColetasIterator = pedidosColetas.iterator();
            while (pedidosColetasIterator.hasNext()) {
                PedidoColeta pedidoColeta = (PedidoColeta) pedidosColetasIterator.next();
                
                pedidoColeta.setTpStatusColeta(domainValue);
                // atualiza o tp_tstus_coleta
                this.getPedidoColetaService().store(pedidoColeta);
                
                // busca as informações necessárias para gerar um evento_coleta
                EventoColeta eventoColeta = new EventoColeta();
                eventoColeta.setDhEvento(dataHoraAtual);
                eventoColeta.setPedidoColeta(pedidoColeta);
                eventoColeta.setTpEventoColeta(domainValue);
                eventoColeta.setDsDescricao(labelManifestada);
                eventoColeta.setMeioTransporteRodoviario(meioTransporteRodoviario);
                eventoColeta.setUsuario(SessionUtils.getUsuarioLogado());
                eventoColeta.setOcorrenciaColeta(ocorrenciaColeta);
                // Gera um registro em evento_coleta    
                this.getEventoColetaService().store(eventoColeta);
                // grava no topic de eventos de pedido coleta
				eventoColetaService.storeMessageTopic(eventoColeta);
            }

            // atualiza o manifestoColeta
            this.store(manifestoColeta);

            // busca as informações necessárias para gerar um evento_manifesto_coleta
            EventoManifestoColeta eventoManifestoColeta = new EventoManifestoColeta();
            eventoManifestoColeta.setDhEvento(dataHoraAtual);
            eventoManifestoColeta.setTpEventoManifestoColeta(dv);
            eventoManifestoColeta.setManifestoColeta(manifestoColeta);
            // Gera um registro em evento_manifesto_coleta
            this.getEventoManifestoColetaService().store(eventoManifestoColeta);
        }
    }
    
    /**
     * Busca o manifesto de coleta mais antigo com o status passado por parâmetro para um controle de cargas especificado.
     * @param idControleCarga
     * @param tpStatusManifestoColeta
     * @return
     */
    public ManifestoColeta findManifestoColetaMaisAntigoByIdControleCargaByTpStatusManifestoColeta(Long idControleCarga, String tpStatusManifestoColeta) {
    	return this.getManifestoColetaDAO().findManifestoColetaMaisAntigoByIdControleCargaByTpStatusManifestoColeta(idControleCarga, tpStatusManifestoColeta);
    }
    
    /**
     * Retorna uma list de registros de Manifesto de Coleta com o ID do Controle de Carga
     * 
     * @param idControleCarga
     * @return
     */
    public List findManifestoColetaByIdControleCarga(Long idControleCarga) {
    	return this.getManifestoColetaDAO().findManifestoColetaByIdControleCarga(idControleCarga);
    }
    
    /**
     * Remove o <code>ManifestoColeta</code> pelo seu id.
     * Remocao realizada conforme espec. <b>02.01.02.09</b>.
     * 
     * @param id
     */
    public void removeManifestoColeta(Long id) {
    	ManifestoColeta manifestoColeta = this.findById(id);
    	EventoManifestoColeta eventoManifestoColeta = new EventoManifestoColeta();
    	eventoManifestoColeta.setManifestoColeta(manifestoColeta);
    	eventoManifestoColeta.setDhEvento(JTDateTimeUtils.getDataHoraAtual());
    	eventoManifestoColeta.setTpEventoManifestoColeta(new DomainValue("CA"));
    	this.getEventoManifestoColetaService().store(eventoManifestoColeta);
    	
    	for (Iterator iter = manifestoColeta.getPedidoColetas().iterator(); iter.hasNext();) {
			PedidoColeta pedidoColeta = (PedidoColeta) iter.next();
			pedidoColeta.setManifestoColeta(null);
			pedidoColeta.setTpStatusColeta(new DomainValue("AB"));
			this.getPedidoColetaService().store(pedidoColeta);
		}

    	manifestoColeta.setTpStatusManifestoColeta(new DomainValue("CA"));
    	manifestoColeta.setQtTotalColetasEmissao(null);
    	manifestoColeta.setControleCarga(null);
    	
    	this.storeBasico(manifestoColeta);
    }
    
    
    /**
     * Carrega em um Map um controleCarga.
     * Este metodo e utilizado para emular um controle carga na tela de 
     * anterManifestoColeta, para emular a existencia de um controlec de 
     * carga em um determinado manifestoColeta
     * 
     * @param idControleCarga
     * @return
     */
    public TypedFlatMap findControleCarga(Long idControleCarga) {
    	TypedFlatMap tfm = new TypedFlatMap();
    	if (idControleCarga!=null) {
    		ControleCarga controleCarga = this.getControleCargaService().findById(idControleCarga);
    		tfm.put("rotaColetaEntrega.idRotaColetaEntrega", controleCarga.getRotaColetaEntrega().getIdRotaColetaEntrega());
    		tfm.put("rotaColetaEntrega.nrRota", controleCarga.getRotaColetaEntrega().getNrRota());
			tfm.put("rotaColetaEntrega.dsRota", controleCarga.getRotaColetaEntrega().getDsRota());

			tfm.put("controleCarga.idControleCarga", controleCarga.getIdControleCarga());
			tfm.put("controleCarga.nrControleCarga", controleCarga.getNrControleCarga());
			tfm.put("controleCarga.psTotalFrota", controleCarga.getPsTotalFrota());
			tfm.put("controleCarga.vlTotalFrota", controleCarga.getVlTotalFrota());
			tfm.put("controleCarga.tpStatusControleCarga", controleCarga.getTpStatusControleCarga().getValue());
    			
			tfm.put("controleCarga.filialByIdFilialOrigem.idFilial", controleCarga.getFilialByIdFilialOrigem().getIdFilial());
			tfm.put("controleCarga.filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());

			if (controleCarga.getMeioTransporteByIdTransportado()!=null) {
				tfm.put("controleCarga.meioTransporteByIdTransportado.idMeioTransporte", controleCarga.getMeioTransporteByIdTransportado().getIdMeioTransporte());
    			tfm.put("controleCarga.meioTransporteByIdTransportado.nrFrota", controleCarga.getMeioTransporteByIdTransportado().getNrFrota());
    			tfm.put("controleCarga.meioTransporteByIdTransportado.nrIdentificador", controleCarga.getMeioTransporteByIdTransportado().getNrIdentificador());
    			tfm.put("controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg", controleCarga.getMeioTransporteByIdTransportado().getNrCapacidadeKg());
			}

			if (controleCarga.getMeioTransporteByIdSemiRebocado()!=null) {
				tfm.put("controleCarga.meioTransporteByIdSemiRebocado.idMeioTransporte", controleCarga.getMeioTransporteByIdSemiRebocado().getIdMeioTransporte());
    			tfm.put("controleCarga.meioTransporteByIdSemiRebocado.nrFrota", controleCarga.getMeioTransporteByIdSemiRebocado().getNrFrota());
    			tfm.put("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", controleCarga.getMeioTransporteByIdSemiRebocado().getNrIdentificador());
			}

			if (controleCarga.getMoeda()!=null){
				tfm.put("controleCarga.moeda.idMoeda", controleCarga.getMoeda().getIdMoeda());
    			tfm.put("controleCarga.moeda.dsSimbolo", controleCarga.getMoeda().getDsSimbolo());
    			tfm.put("controleCarga.moeda.sgMoeda", controleCarga.getMoeda().getSgMoeda());
			}
    	}
    	return tfm;
    }


    public Map findManifestoColetaByIdDoctoServico(Long idDoctoServico){
    	Map map = null;
    	List lista = lmManifestoDao.findManifestoColetaByIdDoctoServico(idDoctoServico);
    	if(!lista.isEmpty())
    		map = (Map)lista.get(0);
		return map;
	}
    
    public List<Map<String, Object>> findListManifestoColetaByIdDoctoServico(Long idDoctoServico){
    	return lmManifestoDao.findListManifestoColetaByIdDoctoServico(idDoctoServico);
    }

	public void setLmManifestoDao(LMManifestoDAO lmManifestoDao) {
		this.lmManifestoDao = lmManifestoDao;
	}
	
	/**
	 * Busca lista de coletas para o projeto VOL
	 * @param idControleCarga
	 * @return
	 */
	public Map findColetasToMobile(Long idControleCarga) {
		Map map = new HashMap();
				
		List retorno = getManifestoColetaDAO().findColetasToMobile(idControleCarga);
		//Retorna apenas o  evento coleta mais recente
		for (int i=0;i<retorno.size();i++){
			Map reg = (Map)retorno.get(i);
			Long idPedidoColeta = (Long)reg.get("COLE");
			List eventoColeta = this.eventoColetaService.findEventoColetaByIdPedidoColeta(idPedidoColeta); 
			 
			if ( eventoColeta.isEmpty() ){
				reg.put("STAT", "");
			} else {
				Map result = (Map)eventoColeta.get(0);
				DomainValue status = (DomainValue) result.get("status");
				reg.put("STAT", status.getValue());
				reg.put("MOTI", result.get("dsResumida"));
	}
		}

		map.put("list", retorno);
		return map;
	}

	/**
     * Verifica se todos os manifestos relacionados ao controle de carga foram emitidos.
     * 
     * @param idControleCarga
     * @return TRUE se existe, FALSE caso contrário
     */
    public Boolean findVerificaExisteManifestoNaoEmitido(Long idControleCarga) {
    	return getManifestoColetaDAO().findVerificaExisteManifestoNaoEmitido(idControleCarga);
    }
    
    /**
     * Gera a consulta para a grid da tela de excluirManifestoColeta. Requer o id do <code>ManifestoColeta</code> 
     * que será detalhado.
     * 
     * @param idManifestoColeta
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedManifestoColeta(Long idManifestoColeta, FindDefinition findDefinition) {
    	return this.getManifestoColetaDAO().findPaginatedManifestoColeta(idManifestoColeta, findDefinition);
    }
    
    /**
     * Gera a rowCount para a grid da tela de excluirManifestoColeta. Requer o id do <code>ManifestoColeta</code> 
     * que será detalhado.
     * 
     * @param idManifestoColeta
     * @return
     */
    public Integer getRowCountManifestoColeta(Long idManifestoColeta) {
    	return this.getManifestoColetaDAO().getRowCountManifestoColeta(idManifestoColeta);
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param idRota
     * @return Integer numero de registros
     */
    public Integer getRowCountPopUpByRotaColetaEntregaGE(Long idRota) {
    	return this.getManifestoColetaDAO().getRowCountByRotaColetaEntregaGE(idRota);
    } 
    
    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * Exige que um idRota seja informado.
     * Tem como restricao buscar apenas "Tipo de Status de Controle de Carga" setado como "GE". 
     * 
     * @param criteria
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedPopUpByRotaColetaEntregaGE(Long idRota, FindDefinition findDefinition) {
    	ResultSetPage rsp = this.getManifestoColetaDAO().findPaginatedByRotaColetaEntregaGE(idRota, findDefinition);
    	rsp.setList(AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList()));
    	return rsp;
    }


    /**
	 * Verifica se existe algum manifesto com status diferente de CA e com tpEventoManifestoColeta = "EM" 
	 * que esteja vinculado ao controle de carga recebido por parâmetro.
	 *
	 * @param idControleCarga
	 * @return True se encontrar algum registro, caso contrário, False.
	 */
	 public Boolean validateManifestoByCancelamentoControleCarga(Long idControleCarga) {
		 return getManifestoColetaDAO().findManifestoNaoCanceladoByControleCarga(idControleCarga);
	 }
	 
	 public boolean validateIfIsManifestoColetaValidoParaCliente(Cliente cliente, ManifestoColeta manifesto) {
	     if (cliente != null && manifesto != null) {
    	     for (PedidoColeta coleta : manifesto.getPedidoColetas()) {
    	         if (cliente.equals(coleta.getCliente())
    	                 && pedidoColetaService.validateIfIsColetaFinalizadaExecutadaOuNoTerminal(coleta)) {
    	             return true;
    	         }
    	     }
	     }
	 
	     return false;
	 }
	 
	 /**
	 * Apaga vários Manifestos de Coleta através do Id.
	 * 
	 * @param idsManifestoColeta
	 * @param idsControleCarga
	 */
	public void removeManifestoColetaByIds(List idsManifestoColeta, List idsControleCarga) {
		if (!idsManifestoColeta.isEmpty()) {
			removeByIds(idsManifestoColeta);
		}

		if (!idsControleCarga.isEmpty()) {
			removeControleCargasByIds(idsControleCarga);
		}
	}
	

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeBasico(ManifestoColeta bean) {
        return super.store(bean);
    }
    
    
	 /**
	  * Altera a situação dos manifestos de coleta relacionados ao controle de carga.
	  * 
	  * @param idControleCarga
     */
	 public void updateSituacaoManifestoColetaByIdControleCarga(Long idControleCarga){
		 getManifestoColetaDAO().updateSituacaoManifestoColetaByIdControleCarga(idControleCarga);
	 }
	 
	 /**
	 * Consulta a quantidade de coletas efetuadas
	 * 
	 * @param idManifestoColeta
	 * @return
	 */ 
	public Integer findQuantidadeColetasEfetuadasByIdManifestoColeta(Long idManifestoColeta) {
		return getManifestoColetaDAO().findQuantidadeColetasEfetuadasByIdManifestoColeta(idManifestoColeta);
	}
	
	public Boolean findVerificaManifestosAssociados(Long idControleCarga){
		return getManifestoColetaDAO().findVerificaManifestosAssociados(idControleCarga);
	}
	
	public List<Map<String, Object>> findManifestoColetaSuggest(String sgFilial, Long nrManifestoColeta, Long idEmpresa) {
		return getManifestoColetaDAO().findManifestoColetaSuggest(sgFilial, nrManifestoColeta, idEmpresa);
	}
	
}
