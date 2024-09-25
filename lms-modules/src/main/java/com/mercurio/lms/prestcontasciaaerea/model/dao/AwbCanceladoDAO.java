package com.mercurio.lms.prestcontasciaaerea.model.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.prestcontasciaaerea.model.AwbCancelado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AwbCanceladoDAO extends BaseCrudDao<AwbCancelado, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AwbCancelado.class;
    }

   
    /**
     * Remove os registros de awbs cancelados da prestação em questão
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