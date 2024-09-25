package com.mercurio.lms.vendas.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.vendas.model.ParcelaCotacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaCotacaoDAO extends BaseCrudDao<ParcelaCotacao, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParcelaCotacao.class;
    }

	public void removeParcelasByIdCotacao(Long idCotacao) {
		StringBuilder hql = new StringBuilder()

    	.append(" DELETE FROM " + ParcelaCotacao.class.getName() + " AS pc \n")
		.append(" WHERE		pc.cotacao.id = :id"); 
    	
        getAdsmHibernateTemplate().removeById(hql.toString(), idCotacao);
        getAdsmHibernateTemplate().flush();
	}

	private ProjectionList createProjection(){
		return Projections.projectionList()
		.add(Projections.property("idParcelaCotacao"), "idParcelaCotacao")
		.add(Projections.property("vlBrutoParcela"), "vlBrutoParcela")
		.add(Projections.property("vlParcelaCotacao"), "vlParcelaCotacao")
		.add(Projections.property("parcelaPreco"), "parcelaPreco");
	}

	public List findByCotacao(Long idCotacao) {
		ProjectionList pl = createProjection();
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
			.add(Restrictions.eq("cotacao.id", idCotacao))
			.setProjection(pl)
			.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return findByDetachedCriteria(dc);
	}
}