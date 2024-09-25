package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.EtapaVisita;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EtapaVisitaDAO extends BaseCrudDao<EtapaVisita, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EtapaVisita.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("visita", FetchMode.JOIN);
		lazyFindById.put("tipoVisita", FetchMode.JOIN);
		
		lazyFindById.put("campanhaMarketing", FetchMode.JOIN);
	
	}
    
    /**
     * Consulta as etapas de uma determinada visita
     * @param idVisita
     * @return
     */
    public List findEtapaVisitasByVisita(Long idVisita){
    	 SqlTemplate sql = new SqlTemplate();
    	 
    	 sql.addProjection(" ev ");
    	 sql.addFrom(EtapaVisita.class.getName() + " ev inner join fetch ev.visita v " +
    	 										   " left join fetch ev.tipoVisita tv " +
    	 										   " left join fetch ev.campanhaMarketing cm ");						    
    	 sql.addCriteria("v.id", "=", idVisita);

    	 return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    			 
    }
    
    /**
     * Consulta a quantidade de etapas de uma determinada visita
     * @param idVisita
     * @return
     */
    public Integer getRowCountEtapaVisitasByVisita(Long idVisita){
    	SqlTemplate sql = new SqlTemplate();

    	sql.addProjection(" count(*) ");
    	sql.addFrom(EtapaVisita.class.getName() + " ev ");   	   
    	sql.addCriteria("ev.visita.id", "=", idVisita);

    	Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    	return result.intValue();
    }

    /**
     * Remove os registros pertencentes à visita onde o id  da visita é
     * recebido como parâmetro
     * @param idVisita
     */
    public void removeByIdVisita(Long idVisita){
        String hql = " delete from " + EtapaVisita.class.getName() + " as ev where ev.visita.id = :id ";
        getAdsmHibernateTemplate().removeById(hql, idVisita);
        getAdsmHibernateTemplate().flush();
    }
}