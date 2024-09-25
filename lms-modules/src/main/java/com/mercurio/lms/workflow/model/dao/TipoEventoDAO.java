package com.mercurio.lms.workflow.model.dao;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.workflow.model.TipoEvento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoEventoDAO extends BaseCrudDao<TipoEvento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoEvento.class;
    }
    
    
    /**
     * Retorna os tipos de eventos e os eventos a partir 
     * dos filtros informado.
     * 
     * @param Map map
     * @return List
     * */    
    public List findLookupWithEvento(Map map){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection(" te ");
    	sql.addFrom(TipoEvento.class.getName() + " te join fetch te.eventoWorkflow e ");
    	sql.addCriteria("te.nrTipoEvento","=",map.get("nrTipoEvento"), Short.class);
    	sql.addCriteria("te.dsTipoEvento","like",map.get("dsTipoEvento"));
    	sql.addCriteria("te.tpSituacao","like",map.get("tpSituacao"));
    	sql.addCriteria("e.tpSituacao","like",map.get("eventoWorkflow.tpSituacao"));    	
    	sql.addOrderBy("te.dsTipoEvento");
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     * Retorna uma lista de Tipo de Evento por id informado
     * 
     * @param List ids
     * @return List
     * */
    public List findByIds(List ids) {
    	if (ids==null || ids.size() <=0){
    		return null;
    	}
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("te");
    	sql.addFrom(TipoEvento.class.getName(), "te");
    	sql.addCriteriaIn("te.id", ids);
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

	public List<TipoEvento> findTipoEventoCombo(List<Short> listNrEventos) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT te ")
		.append("FROM ")
		.append(TipoEvento.class.getSimpleName()).append(" te ")
		.append("WHERE ")
		.append("te.nrTipoEvento IN (:nrEventos) ")
		.append("ORDER BY dsTipoEvento")
		;
		
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("nrEventos", listNrEventos);
		
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), tfm);
	}
}