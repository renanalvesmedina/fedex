package com.mercurio.lms.fretecarreteiroviagem.model.dao;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteiroviagem.model.RateioDoctoServicoTrecho;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RateioDoctoServicoTrechoDAO extends BaseCrudDao<RateioDoctoServicoTrecho, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RateioDoctoServicoTrecho.class;
    }

    /**
     * Remove todos os RateioDoctoServicoTrecho onde tiverem o idControleTrecho == ao parâmetro idControleTrecho 
     * @param idControleTrecho
     */
	public void removeByIdControleTrecho(Serializable idControleTrecho) {
    	StringBuffer hql = new StringBuffer()
		.append(" select rdst ")
		.append(" from " + getPersistentClass().getName() + " rdst ")
		.append(" inner join rdst.controleTrecho cotr ")
		.append(" where cotr.idControleTrecho = ? ");
	
		List list = getAdsmHibernateTemplate().find(hql.toString(), new Object [] {idControleTrecho});
		if (list != null && list.size() > 0) {
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				RateioDoctoServicoTrecho rateioDoctoServicoTrecho = (RateioDoctoServicoTrecho) iter.next();
				getAdsmHibernateTemplate().delete(rateioDoctoServicoTrecho);
			}
		}
    }
    
}