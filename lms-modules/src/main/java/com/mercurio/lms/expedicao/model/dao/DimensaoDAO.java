package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Dimensao;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DimensaoDAO extends BaseCrudDao<Dimensao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class<Dimensao> getPersistentClass() {
        return Dimensao.class;
    }

	public List<Long> findIdsByIdConhecimento(Long idConhecimento) {
		String sql = "select pojo.idDimensao " +
		"from "+ Dimensao.class.getName() + " as pojo " +
		"join pojo.conhecimento as c " +
		"where c.id = :idConhecimento ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idConhecimento", idConhecimento);
	}

    public List<Dimensao> findByIdAwb(Long idAwb) {
    	DetachedCriteria dc = createDetachedCriteria();

    	dc.createAlias("awb", "a");

    	dc.add(Restrictions.eq("a.idAwb", idAwb));

    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public Integer getRowCountByIdCotacao(Long idCotacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("d.cotacao.id", idCotacao));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public ResultSetPage findPaginatedByIdCotacao(Long idCotacao, FindDefinition def) {
		ProjectionList pl = createProjection();
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(pl)
			.add(Restrictions.eq("d.cotacao.id", idCotacao))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);
		return findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	private DetachedCriteria createDetachedCriteriaIdCrt(Long idCrt){
		return DetachedCriteria.forClass(getPersistentClass(), "d")
		.add(Restrictions.eq("d.ctoInternacional.id", idCrt));
	}

	public List<Dimensao> findByIdCtoInternacional(Long idCtoInternacional){
		ProjectionList pl = createProjection();

		DetachedCriteria dc = createDetachedCriteriaIdCrt(idCtoInternacional)
		.setProjection(pl)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return findByDetachedCriteria(dc);
	}

	public ResultSetPage findPaginatedByIdCtoInternacional(Long idCtoInternacional, FindDefinition def){
		ProjectionList pl = createProjection();

		DetachedCriteria dc = createDetachedCriteriaIdCrt(idCtoInternacional)
		.setProjection(pl)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Integer getRowCountByIdCtoInternacional(Long idCtoInternacional) {
		DetachedCriteria dc = createDetachedCriteriaIdCrt(idCtoInternacional)
		.setProjection(Projections.rowCount());

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public List<Dimensao> findPaginatedByIdAwb(Long idAwb) {
		ProjectionList pl = createProjection();

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
		.setProjection(pl)
		.add(Restrictions.eq("d.awb.id", idAwb))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		
		return findByDetachedCriteria(dc);
	}

	private ProjectionList createProjection(){
		return Projections.projectionList()
		.add(Projections.property("idDimensao"), "idDimensao")
		.add(Projections.property("nrAltura"), "nrAltura")
		.add(Projections.property("nrLargura"), "nrLargura")
		.add(Projections.property("nrComprimento"), "nrComprimento")
		.add(Projections.property("nrQuantidade"), "nrQuantidade");
	}

	public Integer getRowCountByIdAwb(Long idAwb) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("d.awb.id", idAwb));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	public void removeByIdAwb(Long idAwb) {
    	StringBuilder hql = new StringBuilder()
    	.append(" delete ").append(getPersistentClass().getName())
    	.append(" where awb = :id");
    	
    	Awb awb = new Awb();
    	awb.setIdAwb(idAwb);
    	
    	getAdsmHibernateTemplate().removeById(hql.toString(), awb);
    }

	private DetachedCriteria createDetachedCriteriaIdCotacao(Long id){
		return DetachedCriteria.forClass(getPersistentClass(), "d")
		.add(Restrictions.eq("d.cotacao.id", id));
	}

	public List<Dimensao> findByIdCotacao(Long id){
		ProjectionList pl = createProjection();

		DetachedCriteria dc = createDetachedCriteriaIdCotacao(id)
		.setProjection(pl)
		.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));

		return findByDetachedCriteria(dc);
	}
}