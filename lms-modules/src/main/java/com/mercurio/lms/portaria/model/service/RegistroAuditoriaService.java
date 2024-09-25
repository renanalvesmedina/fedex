package com.mercurio.lms.portaria.model.service;

import java.io.Serializable;
import java.text.MessageFormat;
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
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.LacreControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.portaria.model.EquipeAuditoria;
import com.mercurio.lms.portaria.model.LacreRegistroAuditoria;
import com.mercurio.lms.portaria.model.RegistroAuditoria;
import com.mercurio.lms.portaria.model.dao.RegistroAuditoriaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.registroAuditoriaService"
 */
public class RegistroAuditoriaService extends CrudService<RegistroAuditoria, Long> {

	UsuarioService usuarioService;
	PendenciaService pendenciaService;
	WorkflowPendenciaService workflowPendenciaService;	
	ControleCargaService controleCargaService;
	EquipeAuditoriaService equipeAuditoriaService;
	LacreControleCargaService lacreControleCargaService;
	EventoMeioTransporteService eventoMeioTransporteService;
	LacreRegistroAuditoriaService lacreRegistroAuditoriaService;
	AcaoService acaoService;
	ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Store padrão.
	 * @param bean
	 * @return
	 */
	// FIXME corrigir para retornar o ID
    public TypedFlatMap store(RegistroAuditoria bean) {
    	TypedFlatMap resultado = new TypedFlatMap();
    	
    	if (bean.getIdRegistroAuditoria() == null){
    	
	    	Usuario usuarioLogado = SessionUtils.getUsuarioLogado();
			bean.setUsuario(usuarioLogado);
	    	bean.setNrRegistroAuditoria(getSequencialNrRegistroAuditoria(SessionUtils.getFilialSessao().getIdFilial()));
	    		
	    	
	    	// SE RESULTADO == REPROVADO E NAO POSSUI DATA DE LIBERACAO -> GERAR PENDENCIA
	    	if (bean.getTpResultado().getValue().equals("R") && bean.getDhLiberacao() == null) {
	    		
	    		getRegistroAuditoriaDAO().store(bean);
	    		
	    		String dsProcesso = getDescricaoLiberacaoMeioTransporte(bean.getMeioTransporteRodoviario().getMeioTransporte().getNrIdentificador());	    	
	    		Pendencia pendencia = getWorkflowPendenciaService().generatePendencia(bean.getFilial().getIdFilial(), ConstantesWorkflow.NR604_LIB_AUD_REPRV, bean.getIdRegistroAuditoria(), dsProcesso, JTDateTimeUtils.getDataHoraAtual());
	    		bean.setPendencia(pendencia);
	    		
	    		getRegistroAuditoriaDAO().store(bean);
	    		
	    		resultado.put("isWorkflow", Boolean.TRUE);
	    		
	    	// OU SE VEICULO LIBERADO
	    	} else if (bean.getTpResultado().getValue().equals("A")){
	    		liberarAuditoria(bean);
	    		bean.setUsuarioLiberacao(usuarioLogado);
	    		
	    		getRegistroAuditoriaDAO().store(bean);
	    		
	    		resultado.put("usuarioLiberacao.nmUsuario",usuarioLogado.getNmUsuario());
	    		resultado.put("dhLiberacao", bean.getDhLiberacao());	    		
				resultado.put("mensagem", configuracoesFacade.getMensagem("LMS-06009"));
	    	}
    	} else {
    		getRegistroAuditoriaDAO().store(bean);
    	}
    	
    	return resultado;
    }
    
    private void liberarAuditoria(RegistroAuditoria bean){
    	// SE NAO POSSUI DATA DE LIBERACAO, ENTAO ATRIBUI UMA
		if (bean.getDhLiberacao() == null) {
			bean.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());			
		}

		// CHAMA A ROTINA DE GERACAO DE EVENTOS DE CONTROLE DE CARGA
		gerarEventoControleCarga(bean);
		
		// ALTERACAO DE TP CONTROLE CARGA 		
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(bean.getControleCarga().getIdControleCarga(), false);		
		String tpControleCarga = controleCarga.getTpControleCarga().getValue();
		if (tpControleCarga.equals("V")){
			controleCarga.setTpStatusControleCarga(new DomainValue("AV"));
			
		} else if (tpControleCarga.equals("C")){
			controleCarga.setTpStatusControleCarga(new DomainValue("AE"));
		}
		
		if (tpControleCarga.equals("C") || tpControleCarga.equals("V")) {
			controleCarga.setFilialByIdFilialAtualizaStatus(SessionUtils.getFilialSessao());
			getControleCargaService().store(controleCarga);
		}		
		
    }
    
    /**
     * Store chamado a partir da action
     * @param bean
     * @param tfm
     * @param lacresAtuaisNovos
     * @param lacresAtuaisAntigos
     * @return
     */
    public java.io.Serializable storeFromAction(RegistroAuditoria bean, TypedFlatMap tfm, List lacresAtuaisNovos, List lacresAtuaisAntigos) {
    	compareLacresWithLacresAtuais(tfm);
    	
    	boolean isInsert = (bean.getIdRegistroAuditoria() == null);
    	Serializable retorno = store(bean);
    	storeEquipeAuditoria(tfm, bean);
    	
    	if (isInsert){
    		storeLacresRegistroAuditoriaAntigos(bean);
    		storeLacresAtuais(lacresAtuaisAntigos, lacresAtuaisNovos, bean);
    		updateLacresRompidos(tfm);
    	}
    	    	
    	((TypedFlatMap)retorno).put("idRegistroAuditoria", bean.getIdRegistroAuditoria());
    	((TypedFlatMap)retorno).put("dhRegistroAuditoria", bean.getDhRegistroAuditoria());
    	((TypedFlatMap)retorno).put("nrRegistroAuditoria", bean.getNrRegistroAuditoria());
    	return retorno;
    }

    /**
     * Store para a listbox de equipe de auditoria
     * @param tfm
     * @param bean
     * @return
     */
    private List storeEquipeAuditoria(TypedFlatMap tfm, RegistroAuditoria bean) {
    	List oldList = tfm.getList("equipeAuditoria");
    	List equipesPersistidas = new ArrayList();
    	List newList = new ArrayList();
    	for (Iterator it = oldList.iterator(); it.hasNext(); ) {
    		
    		TypedFlatMap item = (TypedFlatMap)it.next();
    		
    		Usuario usuarioEquipe = new Usuario();
    		usuarioEquipe.setIdUsuario(item.getLong("usuario.idUsuario"));

    		EquipeAuditoria equipe = new EquipeAuditoria();
    		equipe.setUsuario(usuarioEquipe);
    		equipe.setRegistroAuditoria(bean);
    		
    		Long idEquipe = item.getLong("idEquipe");
    		if (idEquipe!= null && idEquipe.intValue() > 0)
    			equipe.setIdEquipeAuditoria(idEquipe);
    		Serializable idEquipeAuditoria = getEquipeAuditoriaService().store(equipe);
    		
    		TypedFlatMap equipePersistida = new TypedFlatMap();
    		equipePersistida.put("idEquipe", idEquipeAuditoria);
    		equipesPersistidas.add(equipePersistida);
    	}
    	
    	bean.setEquipeAuditorias(newList);
    	return equipesPersistidas;
    }
    
    /**
     * Prepara um evento de controle de carga e chama a rotina de geração do próprio 
     * @param bean
     */
    private void gerarEventoControleCarga(RegistroAuditoria bean) {
		EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
		eventoMeioTransporte.setMeioTransporte(bean.getMeioTransporteRodoviario().getMeioTransporte());
		DomainValue tpSituacaoMeioTransporte = new DomainValue();
		tpSituacaoMeioTransporte.setValue("AGSA");
		eventoMeioTransporte.setTpSituacaoMeioTransporte(tpSituacaoMeioTransporte);
		eventoMeioTransporte.setDhInicioEvento(JTDateTimeUtils.getDataHoraAtual());
		eventoMeioTransporte.setFilial(bean.getFilial());
		eventoMeioTransporte.setControleCarga(bean.getControleCarga());
		getEventoMeioTransporteService().generateEvent(eventoMeioTransporte);
    }
    
    /**
     * Seta os lacres de controle de carga que foram rompidos (removidos da list)
     * como RA (rompido por auditoria)
     * @param tfm
     */
    private void updateLacresRompidos(TypedFlatMap tfm) {

		if (tfm.getList("lacresViolados") != null) { 
    	
			for (Iterator it = tfm.getList("lacresViolados").iterator(); it.hasNext(); ) {
    			
    			TypedFlatMap lcc = (TypedFlatMap) it.next();
    			Long idLacreControleCarga = lcc.getLong("idLacreControleCarga");
    			String dsLocalConferencia = lcc.getString("dsLocalConferencia");
    			String obConferenciaLacre = lcc.getString("obConferenciaLacre");
    			
    			LacreControleCarga lacreControleCarga = getLacreControleCargaService().findById(idLacreControleCarga);
    			
    			lacreControleCarga.setDsLocalConferencia(dsLocalConferencia);
    			lacreControleCarga.setObConferenciaLacre(obConferenciaLacre);
    			lacreControleCarga.setTpStatusLacre(new DomainValue("RA"));
    		}
		}
    }

    /**
     * Retorna uma lista de ids de lacres de controle de carga
     * @param lacres
     * @param idControleCarga
     * @return
     * @author luisfco
     */
    private List getIdsLacres(List lacres, Long idControleCarga) {
    	List ids = new ArrayList();
    	for (Iterator it = lacres.iterator(); it.hasNext(); ) {
    		String nrLacres = (String)it.next();
    		Map item = new HashMap();
    		LacreControleCarga lacreControleCarga = getLacreControleCargaService().findLacreControleCargaByIdControleCargaAndNrLacre(idControleCarga, nrLacres);
    		item.put("id", lacreControleCarga.getIdLacreControleCarga());
    		item.put("versao", lacreControleCarga.getVersao());
    		ids.add(item);
    	}
    	return ids;
    }
    
    /**
     * Método para salvar a listbox de lacres atuais.
     * O processo se inicia salvando-se os lacres de controle de carga adicionados na listbox,
     * guardando-se seus ids de lacre de controle de carga. 
     * Buscam-se então, os ids dos lacres antigos, ou seja, os lacres que já estavam na listbox.
     * Salvam-se agora, os lacres de registro de auditoria, com base nos ids de todos os lacres 
     * da listbox de lacres atuais.
     * 
     * TODO: ao salvar duas vezes o mesmo registro esta duplicando os lacres de registro de auditoria.
     *       não consegui resolver isso ainda, ao salvar novamente nao tenho os ids. (findById neles ?)
     * 
     * @param lacresAntigos
     * @param lacresNovos
     * @param bean
     * @author luisfco
     */
    private void storeLacresAtuais(List lacresAntigos, List lacresNovos, RegistroAuditoria bean) {
    	
    	List idsLacresNovos = storeLacresControleCargaNovos(lacresNovos, bean);
    	
    	List idsLacresAntigos = getIdsLacres(lacresAntigos, bean.getControleCarga().getIdControleCarga());
    	
    	idsLacresNovos.addAll(idsLacresAntigos);
    	
    	for (Iterator it = idsLacresNovos.iterator(); it.hasNext(); ) {
	    	LacreRegistroAuditoria lra = new LacreRegistroAuditoria();
	    	Map item = (Map) it.next();
	    	Long  idLacreControleCarga = (Long) item.get("id");
	    	Integer versao = (Integer) item.get("versao");
	    	LacreControleCarga lcc = new LacreControleCarga();
	    	lcc.setIdLacreControleCarga(idLacreControleCarga);
	    	lcc.setVersao(versao);
    		lra.setLacreControleCarga(lcc);
	    	lra.setRegistroAuditoria(bean);
	    	lra.setBlOriginal(Boolean.FALSE);
	    	getLacreRegistroAuditoriaService().store(lra);
    	}
    }

    /**
     * Salva os lacres novos de controle de carga.
     * @param lacresNovos
     * @param bean
     * @return
     */
    private List storeLacresControleCargaNovos(List lacresNovos, RegistroAuditoria bean) {

    	List lacresPersistidos = new ArrayList();
		for (Iterator it = lacresNovos.iterator(); it.hasNext(); ) {
			
			LacreControleCarga lacreControleCarga = new LacreControleCarga();
			lacreControleCarga.setIdLacreControleCarga(null);
			lacreControleCarga.setVersao(null);
			ControleCarga controleCarga = new ControleCarga();
			controleCarga.setIdControleCarga(bean.getControleCarga().getIdControleCarga());
			lacreControleCarga.setControleCarga(controleCarga);
			
			lacreControleCarga.setFilialByIdFilialInclusao(SessionUtils.getFilialSessao());
			lacreControleCarga.setUsuarioByIdFuncInclusao(SessionUtils.getUsuarioLogado());
			
			MeioTransporte meioTransporte = new MeioTransporte();
			meioTransporte.setIdMeioTransporte(bean.getMeioTransporteRodoviario().getIdMeioTransporte());
			lacreControleCarga.setMeioTransporte(meioTransporte);
			
			lacreControleCarga.setNrLacres((String) it.next());
			lacreControleCarga.setTpStatusLacre(new DomainValue("FE"));
			lacreControleCarga.setObInclusaoLacre(bean.getObComentarios());
			lacreControleCarga.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
			
			Map item = new HashMap();
			item.put("id", getLacreControleCargaService().store(lacreControleCarga));
			item.put("versao", lacreControleCarga.getVersao());
			
			lacresPersistidos.add(item);
		}

		return lacresPersistidos;
   	}
    
    /**
     * Salva os lacres antigos (da lisbox de cima) na tabela de lacre de registro de auditoria.
     * @param bean
     */
    private void storeLacresRegistroAuditoriaAntigos(RegistroAuditoria bean) {

    	List oldLacresOriginais = getLacreControleCargaService().findLacresFechadosByIdControleCarga(bean.getControleCarga().getIdControleCarga());
    	for (Iterator it = oldLacresOriginais.iterator(); it.hasNext(); ) {
	    	LacreRegistroAuditoria lra = new LacreRegistroAuditoria();
	    	LacreControleCarga lacreControleCarga = new LacreControleCarga();
	    	Map lacre = (Map) it.next();
	    	lacreControleCarga.setIdLacreControleCarga(Long.valueOf(lacre.get("idLacre").toString()));
	    	lacreControleCarga.setVersao((Integer) lacre.get("versao"));
	    	lra.setLacreControleCarga(lacreControleCarga);
	    	lra.setRegistroAuditoria(bean);
	    	lra.setBlOriginal(Boolean.TRUE);
	    	getLacreRegistroAuditoriaService().store(lra);
    	}
    }

    /**
     * Compara se o número de lacres é diferente do número de lacres atuais. 
     * Dispara exceção em caso afirmativo.
     * @param tfm
     */
    private void compareLacresWithLacresAtuais(TypedFlatMap tfm) {
    	if (tfm.getInteger("lacresSize") != null
    			&& tfm.getInteger("lacresAtuaisSize") != null
    				&& ( ! tfm.getInteger("lacresSize").equals( tfm.getInteger("lacresAtuaisSize")))) {
    					throw new BusinessException("LMS-06006");
    	}
    }
    
    /**
     * Rotina chamada pelo Workflow para aprovar/reprovar a auditoria
     * @param idsRegistros
     * @param tpsSituacaoWorkflow
     * @return
     */
    public String executeWorkflow(List idsRegistros, List tpsSituacaoWorkflow){
    	
    	String retorno = null;
    	
    	if (idsRegistros != null && !idsRegistros.isEmpty() &&    		
    		tpsSituacaoWorkflow != null && !tpsSituacaoWorkflow.isEmpty()) {
    	
    		Long idRegistroAuditoria = (Long) idsRegistros.get(0);    		
    		String tpSituacao = (String) tpsSituacaoWorkflow.get(0);    		
    		RegistroAuditoria ra = findById(idRegistroAuditoria);
    		
    		if ("A".equals(tpSituacao)) {
    			
    			ra.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
    			ra.setUsuarioLiberacao(SessionUtils.getUsuarioLogado());
    			String obMotivoLiberacao = acaoService.findObservasaoUltimaAcao(ra.getPendencia().getIdPendencia());
    			ra.setObMotivoLiberacao(obMotivoLiberacao);    			   			
    			liberarAuditoria(ra);
    			store(ra);
    			    			    			
    		} else if ("R".equals(tpSituacao)){
    			
    			String controleCarga = ra.getControleCarga().getFilialByIdFilialOrigem().getSgFilial() + " " +
				FormatUtils.formatLongWithZeros(ra.getControleCarga().getNrControleCarga(), "00000000");
    			retorno = configuracoesFacade.getMensagem("LMS-06007", new Object[]{controleCarga});    			
    		}
    		
    	}
    	return retorno;
    }
    
    
    /**
     * FindById
     * @param idRegistroAuditoria
     * @return
     */
	public RegistroAuditoria findByIdCustom(Long idRegistroAuditoria) {
		return getRegistroAuditoriaDAO().findByIdCustom(idRegistroAuditoria);
	}

	public boolean updateDhEmissao(Long idRegistroAuditoria){
		RegistroAuditoria ra = (RegistroAuditoria) getRegistroAuditoriaDAO().getAdsmHibernateTemplate().load(RegistroAuditoria.class, idRegistroAuditoria);
		
		DateTime now = JTDateTimeUtils.getDataHoraAtual();
		
		if (ra.getDhEmissao() == null){
			ra.setDhEmissao(now);
			store(ra);
			getRegistroAuditoriaDAO().getAdsmHibernateTemplate().flush();
			return true;
		}
		
		return false;
		
	}
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRegistroAuditoriaDAO(RegistroAuditoriaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RegistroAuditoriaDAO getRegistroAuditoriaDAO() {
        return (RegistroAuditoriaDAO) getDao();
    }
    
    /**
     * Retorna a data do último evento (emitido) 
     * do controle de carga recebido.
     * @param idControleCarga 
     * @return
     * @author luisfco
     */
    public DateTime findDhEmissaoFromLastEventoEmitidoByIdControleCarga(Long idControleCarga) {
    	return getRegistroAuditoriaDAO().findDhEmissaoFromLastEventoEmitidoByIdControleCarga(idControleCarga);
    }
    
    /**
     * Retorna os funcionarios integrantes de IntegranteEqOperac 
     * de acordo com o id do controle de carga.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
    public List findFuncionariosIntegrantesByIdControleCarga(Long idControleCarga) {
    	return getRegistroAuditoriaDAO().findFuncionariosIntegrantesByIdControleCarga(idControleCarga);
    }
    
    public List findFuncionariosIntegrantesByIdEquipeOperacao(Long idControleCarga) {
    	return getRegistroAuditoriaDAO().findFuncionariosIntegrantesByIdEquipeOperacao(idControleCarga);
    }
    
    /**
     * Retorna as empresas integrantes de IntegranteEqOperac 
     * de acordo com o id do controle de carga.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
    public List findEmpresasIntegrantesByIdControleCarga(Long idControleCarga) {
    	return getRegistroAuditoriaDAO().findEmpresasIntegrantesByIdControleCarga(idControleCarga);
    }
    
    public List findEmpresasIntegrantesByIdEquipeOperacao(Long idControleCarga) {
    	return getRegistroAuditoriaDAO().findEmpresasIntegrantesByIdEquipeOperacao(idControleCarga);
    }
    
    public List findEquipeByIdRegistroAuditoria(Long idRegistroAuditoria) {
    	return getRegistroAuditoriaDAO().findEquipeByIdRegistroAuditoria(idRegistroAuditoria);
    }
    
	/**
	* Retorna nrRegistroAuditoria sequencial por filial
	* @param idFilial
	* @return
	* @author luisfco
	*/
	public Integer getSequencialNrRegistroAuditoria(Long idFilial) {
		return Integer.valueOf(configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_REGISTRO_AUDIT", true).intValue());
	}
	
	/**
	 * Recupera uma instância de <code>RegistroAuditoria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public RegistroAuditoria findById(java.lang.Long id) {
        return (RegistroAuditoria)super.findById(id);
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
	
	public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
		return getRegistroAuditoriaDAO().findPaginatedCustom(tfm, FindDefinition.createFindDefinition(tfm));
	}
	
	public Integer getRowCount(TypedFlatMap tfm) {
		return getRegistroAuditoriaDAO().getRowCountCustom(tfm);
	}
	
	public LacreControleCargaService getLacreControleCargaService() {
		return lacreControleCargaService;
	}

	public void setLacreControleCargaService(LacreControleCargaService lacreControleCargaService) {
		this.lacreControleCargaService = lacreControleCargaService;
	}

	public PendenciaService getPendenciaService() {
		return pendenciaService;
	}

	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

    private String getDescricaoLiberacaoMeioTransporte(String placa) {
    	return MessageFormat.format(configuracoesFacade.getMensagem("liberacaoMeioTransporteProcessoAuditoria"), new Object[]{placa});
    }

	public EventoMeioTransporteService getEventoMeioTransporteService() {
		return eventoMeioTransporteService;
	}

	public void setEventoMeioTransporteService(EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public LacreRegistroAuditoriaService getLacreRegistroAuditoriaService() {
		return lacreRegistroAuditoriaService;
	}

	public void setLacreRegistroAuditoriaService(LacreRegistroAuditoriaService lacreRegistroAuditoriaService) {
		this.lacreRegistroAuditoriaService = lacreRegistroAuditoriaService;
	}

	public EquipeAuditoriaService getEquipeAuditoriaService() {
		return equipeAuditoriaService;
	}

	public void setEquipeAuditoriaService(EquipeAuditoriaService equipeAuditoriaService) {
		this.equipeAuditoriaService = equipeAuditoriaService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(
			WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}


	/**
	 * @param acaoService The acaoService to set.
	 */
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}


	/**
	 * @param configuracoes The configuracoes to set.
	 */
	public void setConfiguracoes(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

   }