package com.mercurio.lms.prestcontasciaaerea.model.dao;

import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.prestcontasciaaerea.model.IntervaloAwb;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IntervaloAwbDAO extends BaseCrudDao<IntervaloAwb, Long>
{

	/**
	 * Busca todos Intervalos de AWBs da Prestacao de Conta informada.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPrestacaoConta
	 * @return
	 */
	public ResultSetPage findPaginatedIntervalosAwb(Map map){
		FindDefinition findDef = FindDefinition.createFindDefinition(map);
		DetachedCriteria dc = (findDef.getDetachedCriteria() == null) ? createDetachedCriteria() : findDef.getDetachedCriteria();
		if (map.containsKey("idPrestacaoConta")){
			dc.add(Restrictions.eq("prestacaoConta.id", Integer.valueOf((String) map.get("idPrestacaoConta")) ));	
		}
		findDef.setDetachedCriteria(dc);
		return findByDefinition(findDef);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return IntervaloAwb.class;
    }

    /**
     * Remove os registros de intervalos de awbs da prestação em questão
     * @param idPrestacaoConta
     */
    public void removeDesmarcarPrestacaoConta(final Long idPrestacaoConta){
			
    	getAdsmHibernateTemplate().execute(new HibernateCallback() {
			
    		public Object doInHibernate(Session session) throws HibernateException, SQLException {

    			Query query = null;

    			String hql = "DELETE " + getPersistentClass().getName() + " \n" +
							 "WHERE prestacaoConta.id = :idPrestacaoConta \n"; 
				
				query = session.createQuery(hql);
				query.setParameter("idPrestacaoConta", idPrestacaoConta);
				query.executeUpdate();
				
				return null;
		}
	});
    	
    }
   


}