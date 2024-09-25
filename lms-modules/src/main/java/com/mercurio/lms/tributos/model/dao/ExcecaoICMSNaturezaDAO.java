package com.mercurio.lms.tributos.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tributos.model.ExcecaoICMSNatureza;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ExcecaoICMSNaturezaDAO extends BaseCrudDao<ExcecaoICMSNatureza, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ExcecaoICMSNatureza.class;
    }

    /**
     * Remove todas as ExcecaoICMSNatureza através do id ExcecaoICMSCliente  
     * 
     * @param idExcecaoICMSCliente
     */
	public void removeByIdExcecaoICMSCliente(Long idExcecaoICMSCliente) {
		
		StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE excecaoICMSCliente.idExcecaoICMSCliente = :id");

    	getAdsmHibernateTemplate().removeById(hql.toString(), idExcecaoICMSCliente);				
	}

	/**
	 * Retorna um lista de mapas com todas as ExcecaoICMSNatureza relacionadas a  ExcecaoICMSCliente
	 * @param idExcecaoICMSCliente
	 * @return
	 */
	public List findICMSNaturezaByIdICMSCliente(Long idExcecaoICMSCliente) {
				
			DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "e")
			.add(Restrictions.eq("e.excecaoICMSCliente.idExcecaoICMSCliente", idExcecaoICMSCliente))
			.setFetchMode("naturezaProduto", FetchMode.JOIN)
			.createAlias("naturezaProduto", "nat")
			.setProjection(Projections
						.projectionList()
							.add(Projections.property("e.idExcecaoICMSNatureza"), "idExcecaoICMSNatureza")
							.add(Projections.property("nat.dsNaturezaProduto"), "dsNaturezaProduto")
							.add(Projections.property("nat.idNaturezaProduto"), "idNaturezaProduto"))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findByDetachedCriteria(dc);		
				
	}
	
	public List findListICMSNaturezaByIdICMSCliente(Long idExcecaoICMSCliente) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "e")
		.add(Restrictions.eq("e.excecaoICMSCliente.idExcecaoICMSCliente", idExcecaoICMSCliente))
		.setFetchMode("naturezaProduto", FetchMode.JOIN);
		
		return super.findByDetachedCriteria(dc);				
	}

}