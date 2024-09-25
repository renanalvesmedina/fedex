package com.mercurio.lms.seguros.model.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.seguros.model.ReciboReembolsoDoctoServico;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboReembolsoDoctoServicoDAO extends BaseCrudDao<ReciboReembolsoDoctoServico, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ReciboReembolsoDoctoServico.class;
    }

    /**
     * Obt�m as inst�ncias de ReciboReembolsoDoctoServico 
     * relacionadas ao ReciboReembolsoProcesso em quest�o. 
     * @param idReciboReembolsoDoctoServico
     * @return
     * @author luisfco
     */
    public List findRecibosDoctoByIdReciboProcesso(Long idReciboReembolsoDoctoServico) {
    	Query q = getAdsmHibernateTemplate()
				.getSessionFactory()
				.getCurrentSession()
				.createQuery(
						"from "
								+ ReciboReembolsoDoctoServico.class.getName()
								+ " rds join fetch rds.doctoServico ds join fetch ds.moeda join fetch rds.moeda mo join fetch ds.filialByIdFilialOrigem where rds.reciboReembolsoProcesso.id = ? order by ds.tpDocumentoServico, ds.filialByIdFilialOrigem.sgFilial, ds.nrDoctoServico");
		q.setParameter(0, idReciboReembolsoDoctoServico);
		return q.list();
    }

    /**
     * Obt�m o count(*) das inst�ncias de ReciboReembolsoDoctoServico 
     * relacionadas ao ReciboReembolsoProcesso em quest�o. 
     * @param idReciboReembolsoDoctoServico
     * @return
     */
    public Integer getRowCountRecibosDoctoByIdReciboProcesso(Long idReciboReembolsoDoctoServico) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "rds");
    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("rds.reciboReembolsoProcesso.id", idReciboReembolsoDoctoServico));
    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }
}