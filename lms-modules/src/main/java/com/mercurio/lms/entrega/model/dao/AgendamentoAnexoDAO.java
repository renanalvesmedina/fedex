package com.mercurio.lms.entrega.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.entrega.model.AgendamentoAnexo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AgendamentoAnexoDAO extends BaseCrudDao<AgendamentoAnexo, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class<AgendamentoAnexo> getPersistentClass() {
        return AgendamentoAnexo.class;
    }    
    
	public List<AgendamentoAnexo> findAllAgendamentoAnexoByEntrega(Long idAgendamentoEntrega){
		SqlTemplate hql = new SqlTemplate(); 
		hql.addFrom(getPersistentClass().getName(), "agendamentoAnexo");
		hql.addCriteria("agendamentoAnexo.agendamentoEntrega.id", "=", idAgendamentoEntrega);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()); 
	}
	
	public List<AgendamentoAnexo> findAgendamentoAnexosByIdAgendamentoEntrega(Long idAgendamentoEntrega) {
		SqlTemplate hql = getSqlTemplateFilterAgendamentoAnexo(idAgendamentoEntrega);
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}	

	public ResultSetPage findPaginatedAgendamentoAnexo(Long idAgendamentoEntrega, FindDefinition findDef) {
		SqlTemplate sql = this.getSqlTemplateFilterAgendamentoAnexo(idAgendamentoEntrega);
		return this.getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
	}
	
	 public Integer getRowCountAgendamentoAnexo(Long idAgendamentoEntrega){
	    	SqlTemplate hql = new SqlTemplate();
	    	hql.addInnerJoin(getPersistentClass().getName() , "agendamentoAnexo");
	    	hql.addInnerJoin("fetch agendamentoAnexo.usuario","usuario");
	    	hql.addCriteria("agendamentoAnexo.agendamentoEntrega.id", "=", idAgendamentoEntrega);

	    	return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	    }
	
	private SqlTemplate getSqlTemplateFilterAgendamentoAnexo(Long idAgendamentoEntrega) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addInnerJoin(getPersistentClass().getName() , "agendamentoAnexo");
   		hql.addInnerJoin("fetch agendamentoAnexo.usuario","usuario");
   		
   		hql.addCriteria("agendamentoAnexo.agendamentoEntrega.id", "=", idAgendamentoEntrega);
		return hql;
	}		
	
	  public AgendamentoAnexo storeBasic(AgendamentoAnexo agendamentoAnexo){
			getAdsmHibernateTemplate().saveOrUpdate(agendamentoAnexo);
			return agendamentoAnexo;
	    }
}