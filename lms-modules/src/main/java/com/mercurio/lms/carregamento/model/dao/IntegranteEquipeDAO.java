package com.mercurio.lms.carregamento.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.IntegranteEquipe;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IntegranteEquipeDAO extends BaseCrudDao<IntegranteEquipe, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return IntegranteEquipe.class;
    }

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("pessoa", FetchMode.JOIN);
	}
 
	public Integer getRowCountIntegranteEquipe(Long idEquipe) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idEquipe)
				.setProjection(Projections.rowCount()));
	}

	private DetachedCriteria getDetachedCriteria(Long idEquipe) {
		return DetachedCriteria.forClass(IntegranteEquipe.class)
			.add(Restrictions.eq("equipe.id", idEquipe));
	}
	
	/**
	 * Busca todos os integrates de uma equipe
	 * 
	 * @param idEquipe da equipe em questao
	 * @return List
	 */
	public List findIntegrantesEquipeByEquipe(Long idEquipe) {
        
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IntegranteEquipe.class)
			.setFetchMode("usuario",FetchMode.JOIN)
            .setFetchMode("usuario.vfuncionario",FetchMode.JOIN)
            .setFetchMode("pessoa", FetchMode.JOIN)
            .setFetchMode("empresa", FetchMode.JOIN)
            .setFetchMode("empresa.pessoa", FetchMode.JOIN)
            .setFetchMode("cargoOperacional", FetchMode.JOIN)
            .setFetchMode("equipeOperacao", FetchMode.JOIN)
			.add(Restrictions.eq("equipe.idEquipe", idEquipe));
    
		return super.findByDetachedCriteria(detachedCriteria);
    }
	
	/**
	 * Busca o numero de integrates de uma equipe
	 * 
	 * @param idEquipe da equipe em questao
	 * @return List
	 */
	public Integer getRowCountIntegrantesEquipeByEquipe(Long idEquipe) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IntegranteEquipe.class)
			.setProjection(Projections.rowCount())
			.setFetchMode("equipe", FetchMode.JOIN)
			.add(Restrictions.eq("equipe", idEquipe));
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(detachedCriteria);
		return result.intValue();
    }

	
	
	
	public IntegranteEquipe findByUsuario(Long idUsuario) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IntegranteEquipe.class)
			.setFetchMode("usuario", FetchMode.JOIN)
			.add(Restrictions.eq("usuario.id", idUsuario));
		return (IntegranteEquipe) getAdsmHibernateTemplate().findUniqueResult(detachedCriteria);
	}

}