package com.mercurio.lms.carregamento.model.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.EstoqueDispIdentificado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EstoqueDispIdentificadoDAO extends BaseCrudDao<EstoqueDispIdentificado, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EstoqueDispIdentificado.class;
    }

	/**
	 * Retorna um POJO de EstoqueDispIdentificado
	 * 
	 * @param idDispositivoUnitizacao
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */   
    public EstoqueDispIdentificado findEstoqueDispIdentificado(Long idDispositivoUnitizacao, Long idControleCarga, Long idFilial) {
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(EstoqueDispIdentificado.class);
    	dc.add(Restrictions.eq("dispositivoUnitizacao.id", idDispositivoUnitizacao));
    	if (idControleCarga != null) {
    		dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
		}
    	if (idFilial != null) {
    		dc.add(Restrictions.eq("filial.id", idFilial));
		}    	
        Criteria criteria = dc.getExecutableCriteria(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession());
        
        return (EstoqueDispIdentificado)criteria.uniqueResult();
    }
    
}