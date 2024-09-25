package com.mercurio.lms.contasreceber.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.AgendaCobranca;
import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;
import com.mercurio.lms.contasreceber.model.dao.AgendaCobrancaDAO;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.agendaCobrancaService"
 */
public class AgendaCobrancaService extends CrudService<AgendaCobranca, Long> {
	
	private CobrancaInadimplenciaService cobrancaInadimplenciaService;


	/**
	 * Recupera uma instância de <code>AgendaCobranca</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public TypedFlatMap findByIdMap(Long id) {
        return getAgendaCobrancaDAO().findByIdMap(id);
    }
    
    /**
     * @author José Rodrigo Moraes
     * @since  22/05/2006
     *
     * Busca a Cobrança Inadimplência associada a agenda Cobrança em questão
     * @param idAgendaCobranca Identificador da agenda de cobrança
     * @return Cobranca Inadimplência associada a agenda cobrança
     */
    public CobrancaInadimplencia findCobrancaInadimplenciaByAgendaCobranca(Long idAgendaCobranca){
    	return getAgendaCobrancaDAO().findCobrancaInadimplenciaByAgendaCobranca(idAgendaCobranca);
    }
    
    /**
     * @author José Rodrigo Moraes
     * @since  22/05/2006
     * 
     * Antes de Excluir uma agenda verifica se a Cobrança Inadimplência não está cancelada.
     * Caso esteja lança BusinessException : LMS-36138
     * @throws BusinessException LMS-36138
     */
    @Override
    protected void beforeRemoveById(Long id) {
    	
    	AgendaCobranca ac = new AgendaCobranca();
    	ac.setIdAgendaCobranca((Long)id);
    	
    	verificaCobrancaInadimplencia(ac);
    	
    	super.beforeRemoveById(id);
    }
    
    @Override
    protected void beforeRemoveByIds(List<Long> ids) {
    	for (Long id : ids) {
			beforeRemoveById(id);
		}
    	super.beforeRemoveByIds(ids);
    }
    
    /**
     * @author José Rodrigo Moraes
     * @since  22/05/2006
     * 
     * Antes de salvar uma agenda verifica se a Cobrança Inadimplência não está cancelada.
     * Caso esteja lança BusinessException : LMS-36138
     * @throws BusinessException LMS-36138
     */
    public AgendaCobranca beforeStore(AgendaCobranca bean) {
    	
    	AgendaCobranca ac = (AgendaCobranca) bean;
    	
    	if (ac.getDhAgendaCobranca().compareTo(JTDateTimeUtils.getDataHoraAtual().withMillisOfSecond(0).withSecondOfMinute(0)) < 0){
    		throw new BusinessException("LMS-36224");
    	}
    	
    	verificaCobrancaInadimplencia(ac);
    	
    	return super.beforeStore(bean);
    }

    /** 
     * @author José Rodrigo Moraes
     * @since  22/05/2006
     * 
     *  Verifica se a cobrança inadimplência está Encerrada
     * Caso esteja lança BusinessException : LMS-36138
     * @throws BusinessException LMS-36138  
     * @param long1 identificador da Agenda de Cobrança
     */
	private void verificaCobrancaInadimplencia(AgendaCobranca agendaCobranca) {
		
		CobrancaInadimplencia ci = null;
		
		if( agendaCobranca.getIdAgendaCobranca() != null ){		
			ci = findCobrancaInadimplenciaByAgendaCobranca(agendaCobranca.getIdAgendaCobranca());	    		    	
		} else if (agendaCobranca.getCobrancaInadimplencia() != null){
			ci = cobrancaInadimplenciaService.findById(agendaCobranca.getCobrancaInadimplencia().getIdCobrancaInadimplencia());
		}
		
		if( ci != null && ci.getBlCobrancaEncerrada().booleanValue() ){
			ci = null;
			throw new BusinessException("LMS-36138");
    	}
		
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
    public java.io.Serializable store(AgendaCobranca bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAgendaCobrancaDAO(AgendaCobrancaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AgendaCobrancaDAO getAgendaCobrancaDAO() {
        return (AgendaCobrancaDAO) getDao();
    }
    
    /**
     * Método responsável por carregar dados páginados de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage contendo o resultado do hql.
     */
    public ResultSetPage findPaginatedByAgendaCobranca(TypedFlatMap tfm) throws Exception{
    	return getAgendaCobrancaDAO().findPaginatedByAgendaCobranca(tfm);
    }
    
    /**
     * Método responsável por fazer a contagem dos registros que retornam do hql para paginação.
     * @param criteria
     * @return Integer contendo o número de registros retornados.
     */
    public Integer getRowCountByAgendaCobranca(TypedFlatMap tfm) throws Exception{
    	return getAgendaCobrancaDAO().getRowCountByAgendaCobranca(tfm);
    }
    
    /**
     * Método padrão de pesquisa da listagem do Manter Agenda Cobrança
     * @param criteria Critérios de pesquisa
     * @return ResultSetPage contendo os dados resultantes da pesquisa e informações sobre paginação
     */
    public ResultSetPage findPaginated(TypedFlatMap criteria) {     
        FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
        return getAgendaCobrancaDAO().findPaginated(criteria,findDef);
    }
    
    /**
     * Método padrão para contagem de itens resultantes da pesquisa na listagem do Manter Agenda Cobrança
     * @param criteria Critérios de pesquisa
     * @return Inteiro informando quantos registros serão exibidos na listagem
     */
    public Integer getRowCount(TypedFlatMap criteria) {        
        return getAgendaCobrancaDAO().getRowCount(criteria);
    }
    
    /**
     * CArrega a descrição da agendaCobranca em questão
     * @param idAgendaCobranca
     * @return Map
     */
    public Map findDescricaoAgendaCobranca(Long idAgendaCobranca){
    	return getAgendaCobrancaDAO().findDescricaoAgendaCobranca(idAgendaCobranca);
    }

	public void setCobrancaInadimplenciaService(
			CobrancaInadimplenciaService cobrancaInadimplenciaService) {
		this.cobrancaInadimplenciaService = cobrancaInadimplenciaService;
	}
    
}