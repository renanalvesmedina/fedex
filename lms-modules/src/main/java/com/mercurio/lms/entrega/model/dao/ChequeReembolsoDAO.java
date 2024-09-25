package com.mercurio.lms.entrega.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.entrega.model.ChequeReembolso;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ChequeReembolsoDAO extends BaseCrudDao<ChequeReembolso, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ChequeReembolso.class;
    }

    /**
     * Retorna Cheques de Reembolso de um Recibo espec�fico.
     * @param idReciboReembolso
     * @return List com pojos ChequeReembolso.
     */
	public List findChequesReciboReembolso(Long idReciboReembolso) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.add(Restrictions.eq("reciboReembolso.id",idReciboReembolso));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
}