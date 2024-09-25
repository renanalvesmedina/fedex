package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.springframework.dao.DataAccessException;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.AnexoTabelaFreteCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcFaixaPeso;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFcValores;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaFreteCarreteiroCe;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.TabelaFreteCarreteiroCeDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Service responsável por operações de tabela de frete carreteiro de
 * coleta/entrega.
 * 
 */
public class TabelaFreteCarreteiroCeService extends CrudService<TabelaFreteCarreteiroCe, Long> {

	private Logger log = LogManager.getLogger(this.getClass());

	private ConfiguracoesFacade configuracoesFacade;
	
	private MeioTransporteService meioTransporteService;

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	private TabelaFreteCarreteiroCeDAO getTabelaFreteCarreteiroCeDAO() {
        return (TabelaFreteCarreteiroCeDAO) getDao();
    }
	
	public void setTabelaFreteCarreteiroCeDAO(TabelaFreteCarreteiroCeDAO dao) {
        setDao(dao);
    }
	
	@Override
	protected Serializable store(TabelaFreteCarreteiroCe bean) {
		return super.store(bean);
	}
	
	public TabelaFreteCarreteiroCe findById(java.lang.Long id) {
		return (TabelaFreteCarreteiroCe) super.findById(id);
	}
		
	public void removeCascadeById(Long id){
		getTabelaFreteCarreteiroCeDAO().removeCascadeById(id);
	}
	
	/**
	 * Não é possível excluir uma tabela de frete carreteiro.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	@SuppressWarnings({ "rawtypes" })
	public void removeByIds(List ids) {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria){
		return getTabelaFreteCarreteiroCeDAO().findPaginatedCustom(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria){
		return getTabelaFreteCarreteiroCeDAO().getRowCountCustom(criteria);
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findReportData(TypedFlatMap criteria){
		return getTabelaFreteCarreteiroCeDAO().findReportData(criteria, FindDefinition.createFindDefinition(criteria));
	}

	/**
	 * Executa o store de uma tabela de frete carreteiro.
	 * 
	 * @param tabelaFreteCarreteiroCe
	 * 
	 * @return TabelaFreteCarreteiroCe
	 */
	public TabelaFreteCarreteiroCe storeTabelaFreteCarreteiroCe(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		prepareStoreTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);

		/*
		 * Retira a tabela geral para ser persistida após a inclusão da tabela
		 * padrão.
		 */		
		TabelaFcValores tabelaFcValores = getTabelaFcValoresGeral(tabelaFreteCarreteiroCe);
		
		getTabelaFreteCarreteiroCeDAO().store(tabelaFreteCarreteiroCe);
		
		storeEntityTabelaFcValores(tabelaFcValores, tabelaFreteCarreteiroCe);
		
		return tabelaFreteCarreteiroCe;
	}

	/**
	 * @param tabelaFreteCarreteiroCe
	 */
	private void prepareStoreTabelaFreteCarreteiroCe(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		validateTabelaFreteCarreteiroPermissao(tabelaFreteCarreteiroCe);
		validateTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		if(tabelaFreteCarreteiroCe.getIdTabelaFreteCarreteiroCe() == null){
			tabelaFreteCarreteiroCe.setNrTabelaFreteCarreteiroCe(getNrTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe.getFilial()));
			tabelaFreteCarreteiroCe.setUsuarioCriacao(usuario);
			tabelaFreteCarreteiroCe.setDtCriacao(JTDateTimeUtils.getDataAtual());			
		} else {			
			tabelaFreteCarreteiroCe.setUsuarioAlteracao(usuario);
			tabelaFreteCarreteiroCe.setDtAtualizacao(JTDateTimeUtils.getDataAtual());
		}
	}

	/**
	 * Retorna o número da próxima tabela de frete carreteiro quando nova.
	 * 
	 * @param tabelaFreteCarreteiroCe
	 * @return Long
	 */
	private Long getNrTabelaFreteCarreteiroCe(Filial filial) {
		return configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_TABELA_FC_CE", true);
	}

	/**
	 * Executa as validações apenas se data de vigência inicial estiver
	 * preenchida.
	 * 
	 * @param tabelaFreteCarreteiroCe
	 */
	private void validateTabelaFreteCarreteiroCe(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		if (tabelaFreteCarreteiroCe.getDhVigenciaInicial() == null) {
			return;
		}
		
		validateTabelaFreteCarreteiroCePeriodo(tabelaFreteCarreteiroCe);
	}

	/**
	 * Somente a matriz pode inserir ou modificar uma tabela.
	 * 
	 * @param tabelaFreteCarreteiroCe
	 */
	private void validateTabelaFreteCarreteiroPermissao(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		if(!SessionUtils.isFilialSessaoMatriz()){
			throw new BusinessException("LMS-00071");
		}
	}

	/**
	 * @param tabelaFreteCarreteiroCe
	 * @param tabelaFcValores
	 * @return
	 */
	private TabelaFcValores getTabelaFcValoresGeral(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		List<TabelaFcValores> listTabelaFcValores = tabelaFreteCarreteiroCe.getListTabelaFcValores();
		
		if (listTabelaFcValores != null && !listTabelaFcValores.isEmpty()) {
			return listTabelaFcValores.remove(0);
		}

		return null;
	}
	
	/**
	 * Valida se existe outra tabela de frete carreteiro coleta/entrega no mesmo
	 * período.
	 * 
	 * @param tabelaFreteCarreteiroCe
	 */
	private void validateTabelaFreteCarreteiroCePeriodo(TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe){
		Boolean exists = getTabelaFreteCarreteiroCeDAO().isTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
				
		if(exists){
			throw new BusinessException("LMS-25109");
		}
	}
	
	@SuppressWarnings("rawtypes")
	public ResultSetPage findListTabelaFcValores(TypedFlatMap criteria){
		return getTabelaFreteCarreteiroCeDAO().findListTabelaFcValores(criteria, FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCountListTabelaFcValores(TypedFlatMap criteria){
		return getTabelaFreteCarreteiroCeDAO().getRowCountListTabelaFcValores(criteria);
	}

	public TabelaFcValores findTabelaFcValores(Long id) {
		return getTabelaFreteCarreteiroCeDAO().findTabelaFcValores(id);
	}
	
	public TabelaFcValores findTabelaFcValoresGeral(Long idTabelaFreteCarreteiroCe) {
		return getTabelaFreteCarreteiroCeDAO().findTabelaFcValoresGeral(idTabelaFreteCarreteiroCe);
	}	
	
	private void prepareStoreTabelaFcValores(TabelaFcValores tabelaFcValores){
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		if(tabelaFcValores.getIdTabelaFcValores() == null){
			tabelaFcValores.setUsuarioCriacao(usuario);
			tabelaFcValores.setDtCriacao(JTDateTimeUtils.getDataAtual());			
		}
	}
	
	private TabelaFcValores storeEntityTabelaFcValores(TabelaFcValores tabelaFcValores,
			TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		if(tabelaFcValores == null || tabelaFreteCarreteiroCe == null){
			return null;
		}		
		
		prepareStoreTabelaFcValores(tabelaFcValores);
		
		tabelaFcValores.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCe);
		
		/*
		 * Verifica se existem valores na tabela de faixas de peso. 
		 */
		List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso = getListTabelaFcFaixaPeso(tabelaFcValores);
		
		/*
		 * Executa o store da tabela de valores.
		 */
		getTabelaFreteCarreteiroCeDAO().store(tabelaFcValores);
		
		/*
		 * Executa o store dos valores na tabela de faixas de peso. 
		 */
		storeListTabelaFcFaixaPeso(tabelaFcValores, listTabelaFcFaixaPeso);		
		
		return tabelaFcValores;
	}

	/**
	 * @param tabelaFcValores
	 * @return List<TabelaFcFaixaPeso>
	 */
	private List<TabelaFcFaixaPeso> getListTabelaFcFaixaPeso(TabelaFcValores tabelaFcValores) {
		List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso = tabelaFcValores.getListTabelaFcFaixaPeso();
		tabelaFcValores.setListTabelaFcFaixaPeso(null);
		
		/*
		 * Remove os valores existentes para evitar duplicação de registros.
		 * 
		 */
		if(tabelaFcValores.getIdTabelaFcValores() != null){			
			removeTabelaFcFaixaPesoById(tabelaFcValores.getIdTabelaFcValores());
		}
		
		return listTabelaFcFaixaPeso;
	}

	/**
	 * @param tabelaFcValores
	 * @param listTabelaFcFaixaPeso
	 * @throws DataAccessException
	 */
	private void storeListTabelaFcFaixaPeso(TabelaFcValores tabelaFcValores, List<TabelaFcFaixaPeso> listTabelaFcFaixaPeso)
			throws DataAccessException {
		if(listTabelaFcFaixaPeso == null || listTabelaFcFaixaPeso.isEmpty()){
			return;
		}
		
		for (TabelaFcFaixaPeso tabelaFcFaixaPeso : listTabelaFcFaixaPeso) {
			tabelaFcFaixaPeso.setTabelaFcValores(tabelaFcValores);
		}
		
		getTabelaFreteCarreteiroCeDAO().getAdsmHibernateTemplate().saveOrUpdateAll(listTabelaFcFaixaPeso);
	}
	
	/**
	 * 
	 * @param tabelaFcValores
	 * @param tabelaFreteCarreteiroCe
	 * 
	 * @return TabelaFcValores
	 */
	public TabelaFcValores storeTabelaFcValores(TabelaFcValores tabelaFcValores, TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe) {
		tabelaFreteCarreteiroCe = findById(tabelaFreteCarreteiroCe.getIdTabelaFreteCarreteiroCe());
		
		validateTabelaFcValoresExistente(tabelaFcValores);
		
		return storeEntityTabelaFcValores(tabelaFcValores, tabelaFreteCarreteiroCe);
	}

	/**
	 * Verifica se já existe uma tabela de valores cadastrada com os mesmos
	 * parâmetros.
	 * 
	 * @param tabelaFcValores
	 */
	private void validateTabelaFcValoresExistente(TabelaFcValores tabelaFcValores){
		Boolean exists = getTabelaFreteCarreteiroCeDAO().isTabelaFcValores(tabelaFcValores);
		
		if(exists){
			throw new BusinessException("LMS-25110");
		}
	}
	
	/**
	 * Remove um registro da tabela da faixa de peso.
	 * 
	 * @param id
	 */
	private void removeTabelaFcFaixaPesoById(Long id){
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		
		getTabelaFreteCarreteiroCeDAO().removeTabelaFcFaixaPesoByIds(ids);	
	}
	
	/**
	 * Remove um registro da tabela de valores e todos os seus relacionados.
	 * 
	 * @param id
	 */
	public void removeCascadeTabelaValoresById(Long id) {
		List<Long> ids = new ArrayList<Long>();
		ids.add(id);
		
		removeCascadeTabelaValoresByIds(ids);		
	}
	
	/**
	 * Remove um ou mais registros da tabela de valores e todos os seus
	 * relacionados.
	 * 
	 * @param ids
	 */
	public void removeCascadeTabelaValoresByIds(List<Long> ids) {
		getTabelaFreteCarreteiroCeDAO().removeTabelaFcFaixaPesoByIds(ids);
		getTabelaFreteCarreteiroCeDAO().removeTabelaValoresByIds(ids);
	}

	public Map<String, String> getProprietarioByMeioTransporte(Long id) {
		Map<String, String> retornoMap = new HashMap<String, String>(); 
		MeioTranspProprietario retorno = meioTransporteService.findProprietario(id);
		
		if(retorno == null){
			throw new BusinessException("LMS-25036");
		}else{
			retornoMap.put("nmProprietario", retorno.getProprietario().getPessoa().getNmPessoa());
			retornoMap.put("nrIdentificacao", retorno.getProprietario().getPessoa().getNrIdentificacao());
		}
		
		return retornoMap;
	}

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public List<TabelaFcValores> findByIdFilial(Long idFilial,  String[] tipoOperacao) {		
		return getTabelaFreteCarreteiroCeDAO().findByIdFilial(idFilial,tipoOperacao);
	}

	public void storeDhVigenciaFinal(Long idTabelaFreteCarreteiroCe, DateTime dhVigenciaFinal) {
		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCe = findById(idTabelaFreteCarreteiroCe);
		
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		tabelaFreteCarreteiroCe.setUsuarioAlteracao(usuario);
		
		tabelaFreteCarreteiroCe.setDhVigenciaFinal(dhVigenciaFinal);		
		
		store(tabelaFreteCarreteiroCe);
	}

	/**
	 * Executa o clone de uma tabela de frete carreteiro.
	 * 
	 * @param idTabelaFreteCarreteiroCe
	 * @return Long
	 */
	public Long executeClonarTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {		
		TabelaFreteCarreteiroCe target = null;
		
		try {			
			target = createTabelaFreteCarreteiroCeClone(idTabelaFreteCarreteiroCe);				
		} catch (IllegalAccessException e) {
			log.error(e);
			
			throw new BusinessException("LMS-25124");
		} catch (InstantiationException e) {
			log.error(e);
			
			throw new BusinessException("LMS-25124");
		} catch (InvocationTargetException e) {
			log.error(e);
			
			throw new BusinessException("LMS-25124");
		} catch (NoSuchMethodException e) {
			log.error(e);
			
			throw new BusinessException("LMS-25124");
		}
		
		return target.getIdTabelaFreteCarreteiroCe();
	}

	/**
	 * Grava os anexos da tabela de frete.
	 * 
	 * @param idTabelaFreteCarreteiroCe
	 * @param listAnexoTabelaFreteCe
	 * 
	 * @return TabelaFreteCarreteiroCe
	 */
	public TabelaFreteCarreteiroCe storeAnexos(Long idTabelaFreteCarreteiroCe, List<AnexoTabelaFreteCe> listAnexoTabelaFreteCe) {
		TabelaFreteCarreteiroCe tabelaFreteCarreteiroCeDb = findById(idTabelaFreteCarreteiroCe);
		
		if(listAnexoTabelaFreteCe == null || listAnexoTabelaFreteCe.isEmpty()){
			return tabelaFreteCarreteiroCeDb;
		}
		
		/*
		 * Define a entidade da tabela de frete atualizada para cada anexo.
		 */
		for (AnexoTabelaFreteCe anexoTabelaFreteCe : listAnexoTabelaFreteCe) {
			anexoTabelaFreteCe.setTabelaFreteCarreteiroCe(tabelaFreteCarreteiroCeDb);
		}
		
		getTabelaFreteCarreteiroCeDAO().store(listAnexoTabelaFreteCe);

		return tabelaFreteCarreteiroCeDb;
	}	
	
	public AnexoTabelaFreteCe findAnexoTabelaFreteCeById(Long idAnexoTabelaFreteCe) {
		AnexoTabelaFreteCe anexoTabelaFreteCe = getTabelaFreteCarreteiroCeDAO().findAnexoTabelaFreteCeById(idAnexoTabelaFreteCe);
		if(anexoTabelaFreteCe != null){
			Hibernate.initialize(anexoTabelaFreteCe);
		}
		return anexoTabelaFreteCe;
	}
	
	@SuppressWarnings("rawtypes")
	public void removeByIdsAnexoTabelaFreteCe(List ids) {		
		getTabelaFreteCarreteiroCeDAO().removeByIdsAnexoTabelaFreteCe(ids);
	}

	public List<Map<String, Object>> findAnexoTabelaFreteCeByIdAnexoTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {
		return getTabelaFreteCarreteiroCeDAO().findAnexoTabelaFreteCeByIdTabelaFreteCarreteiro(idTabelaFreteCarreteiroCe);
    }
	
	public Integer getRowCountTabelaFreteCeByIdAnexoTabelaFreteCarreteiroCe(Long idTabelaFreteCarreteiroCe) {
		return getTabelaFreteCarreteiroCeDAO().getRowCountAnexoTabelaFreteCeByIdTabelaFreteCarreteiro(idTabelaFreteCarreteiroCe);
	}
	
	/**
	 * Clona entidade TabelaFcFaixaPeso e a persiste.
	 * 
	 * @param tabelaFcFaixaPeso
	 * @return TabelaFcFaixaPeso 
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws DataAccessException
	 */
	private TabelaFcFaixaPeso createTabelFcFaixaPesoClone(
			TabelaFcFaixaPeso tabelaFcFaixaPeso) throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException, DataAccessException {
		TabelaFcFaixaPeso tabelaFcFaixaPesoTarget = (TabelaFcFaixaPeso) BeanUtils.cloneBean(tabelaFcFaixaPeso);	
		getTabelaFreteCarreteiroCeDAO().getAdsmHibernateTemplate().evict(tabelaFcFaixaPeso);
		
		tabelaFcFaixaPesoTarget.setIdTabelaFcFaixaPeso(null);
		tabelaFcFaixaPesoTarget.setTabelaFcValores(null);
		return tabelaFcFaixaPesoTarget;
	}

	/**
	 * Clona entidade TabelaFcValore e a persiste.
	 * 
	 * @param target
	 * @param tabelaFcValores
	 * 
	 * @return TabelaFcValores
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws DataAccessException
	 */
	private TabelaFcValores createTabelaFcValoresClone(
			TabelaFreteCarreteiroCe target, TabelaFcValores tabelaFcValores)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException,
			DataAccessException {
		TabelaFcValores tabelaFcValoresTarget = (TabelaFcValores) BeanUtils.cloneBean(tabelaFcValores);	
		getTabelaFreteCarreteiroCeDAO().getAdsmHibernateTemplate().evict(tabelaFcValores);
		
		tabelaFcValoresTarget.setIdTabelaFcValores(null);
		tabelaFcValoresTarget.setTabelaFreteCarreteiroCe(null);
		tabelaFcValoresTarget.setListTabelaFcFaixaPeso(null);
		
		prepareStoreTabelaFcValores(tabelaFcValoresTarget);
		
		storeEntityTabelaFcValores(tabelaFcValoresTarget, target);
		
		return tabelaFcValoresTarget;
	}

	/**
	 * Clona entidade TabelaFreteCarreteiroCe e a persiste.
	 * 
	 * @return TabelaFreteCarreteiroCe
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws DataAccessException
	 */
	private TabelaFreteCarreteiroCe createTabelaFreteCarreteiroCeClone(Long idTabelaFreteCarreteiroCe) throws IllegalAccessException,
			InstantiationException, InvocationTargetException,
			NoSuchMethodException, DataAccessException {
		TabelaFreteCarreteiroCe source = findById(idTabelaFreteCarreteiroCe);
		
		TabelaFreteCarreteiroCe target = (TabelaFreteCarreteiroCe) BeanUtils.cloneBean(source);		
		getTabelaFreteCarreteiroCeDAO().getAdsmHibernateTemplate().evict(source);
		
		target.setTabelaClonada(source);
		target.setIdTabelaFreteCarreteiroCe(null);
		target.setDhVigenciaInicial(null);
		target.setDhVigenciaFinal(null);
		target.setListTabelaFcValores(null);	
		target.setUsuarioAlteracao(null);
		target.setDtAtualizacao(null);
		
		storeTabelaFreteCarreteiroCe(target);
		
		cloneTabelaFcValoresList(idTabelaFreteCarreteiroCe, target);
		
		return target;
	}

	/**
	 * Clona as tabelas de valores e as persiste, se houverem.
	 * 
	 * @param idTabelaFreteCarreteiroCe
	 * @param listTabelaFcValoresSource
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws DataAccessException
	 */
	private void cloneTabelaFcValoresList(Long idTabelaFreteCarreteiroCe, TabelaFreteCarreteiroCe target)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException,
			DataAccessException {
		List<TabelaFcValores> listTabelaFcValoresSource = getTabelaFreteCarreteiroCeDAO().findListTabelaFcValores(idTabelaFreteCarreteiroCe);
		
		if(listTabelaFcValoresSource == null || listTabelaFcValoresSource.isEmpty()){
			return;
		}
		
		for (TabelaFcValores tabelaFcValores : listTabelaFcValoresSource) {
			TabelaFcValores tabelaFcValoresTarget = createTabelaFcValoresClone(target, tabelaFcValores);
			
			cloneTabelaFcFaixaPeso(tabelaFcValores, tabelaFcValoresTarget);
		}
	}

	/**
	 *  Clona as tabelas de faixas de peso e as persiste, se houverem.
	 *  
	 * @param tabelaFcValores
	 * @param tabelaFcValoresTarget
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws DataAccessException
	 */
	private void cloneTabelaFcFaixaPeso(TabelaFcValores tabelaFcValores,
			TabelaFcValores tabelaFcValoresTarget)
			throws IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException,
			DataAccessException {
		List<TabelaFcFaixaPeso> listTabelaFcFaixaPesoSource = getTabelaFreteCarreteiroCeDAO().findListTabelaFcFaixaPeso(tabelaFcValores.getIdTabelaFcValores());
		
		if(listTabelaFcFaixaPesoSource == null || listTabelaFcFaixaPesoSource.isEmpty()){
			return;
		}
		
		List<TabelaFcFaixaPeso> listTabelaFcFaixaPesoTarget = new ArrayList<TabelaFcFaixaPeso>();		
		
		for (TabelaFcFaixaPeso tabelaFcFaixaPeso : listTabelaFcFaixaPesoSource) {
			TabelaFcFaixaPeso tabelaFcFaixaPesoTarget = createTabelFcFaixaPesoClone(tabelaFcFaixaPeso);
			
			listTabelaFcFaixaPesoTarget.add(tabelaFcFaixaPesoTarget);
		}
		
		storeListTabelaFcFaixaPeso(tabelaFcValoresTarget, listTabelaFcFaixaPesoTarget);
	}
}
