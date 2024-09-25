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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.agendaCobrancaService"
 */
public class AgendaCobrancaService extends CrudService<AgendaCobranca, Long> {
	
	private CobrancaInadimplenciaService cobrancaInadimplenciaService;


	/**
	 * Recupera uma inst�ncia de <code>AgendaCobranca</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public TypedFlatMap findByIdMap(Long id) {
        return getAgendaCobrancaDAO().findByIdMap(id);
    }
    
    /**
     * @author Jos� Rodrigo Moraes
     * @since  22/05/2006
     *
     * Busca a Cobran�a Inadimpl�ncia associada a agenda Cobran�a em quest�o
     * @param idAgendaCobranca Identificador da agenda de cobran�a
     * @return Cobranca Inadimpl�ncia associada a agenda cobran�a
     */
    public CobrancaInadimplencia findCobrancaInadimplenciaByAgendaCobranca(Long idAgendaCobranca){
    	return getAgendaCobrancaDAO().findCobrancaInadimplenciaByAgendaCobranca(idAgendaCobranca);
    }
    
    /**
     * @author Jos� Rodrigo Moraes
     * @since  22/05/2006
     * 
     * Antes de Excluir uma agenda verifica se a Cobran�a Inadimpl�ncia n�o est� cancelada.
     * Caso esteja lan�a BusinessException : LMS-36138
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
     * @author Jos� Rodrigo Moraes
     * @since  22/05/2006
     * 
     * Antes de salvar uma agenda verifica se a Cobran�a Inadimpl�ncia n�o est� cancelada.
     * Caso esteja lan�a BusinessException : LMS-36138
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
     * @author Jos� Rodrigo Moraes
     * @since  22/05/2006
     * 
     *  Verifica se a cobran�a inadimpl�ncia est� Encerrada
     * Caso esteja lan�a BusinessException : LMS-36138
     * @throws BusinessException LMS-36138  
     * @param long1 identificador da Agenda de Cobran�a
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
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(AgendaCobranca bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setAgendaCobrancaDAO(AgendaCobrancaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private AgendaCobrancaDAO getAgendaCobrancaDAO() {
        return (AgendaCobrancaDAO) getDao();
    }
    
    /**
     * M�todo respons�vel por carregar dados p�ginados de acordo com os filtros passados
     * @param criteria
     * @return ResultSetPage contendo o resultado do hql.
     */
    public ResultSetPage findPaginatedByAgendaCobranca(TypedFlatMap tfm) throws Exception{
    	return getAgendaCobrancaDAO().findPaginatedByAgendaCobranca(tfm);
    }
    
    /**
     * M�todo respons�vel por fazer a contagem dos registros que retornam do hql para pagina��o.
     * @param criteria
     * @return Integer contendo o n�mero de registros retornados.
     */
    public Integer getRowCountByAgendaCobranca(TypedFlatMap tfm) throws Exception{
    	return getAgendaCobrancaDAO().getRowCountByAgendaCobranca(tfm);
    }
    
    /**
     * M�todo padr�o de pesquisa da listagem do Manter Agenda Cobran�a
     * @param criteria Crit�rios de pesquisa
     * @return ResultSetPage contendo os dados resultantes da pesquisa e informa��es sobre pagina��o
     */
    public ResultSetPage findPaginated(TypedFlatMap criteria) {     
        FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
        return getAgendaCobrancaDAO().findPaginated(criteria,findDef);
    }
    
    /**
     * M�todo padr�o para contagem de itens resultantes da pesquisa na listagem do Manter Agenda Cobran�a
     * @param criteria Crit�rios de pesquisa
     * @return Inteiro informando quantos registros ser�o exibidos na listagem
     */
    public Integer getRowCount(TypedFlatMap criteria) {        
        return getAgendaCobrancaDAO().getRowCount(criteria);
    }
    
    /**
     * CArrega a descri��o da agendaCobranca em quest�o
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