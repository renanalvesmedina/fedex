package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.CptComplexidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CptComplexidadeDAO extends BaseCrudDao<CptComplexidade, Long> {

	protected final Class getPersistentClass() {
		return CptComplexidade.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("cptTipoValor",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cptTipoValor",FetchMode.JOIN);
	}

	public List findTiposComplexidade(Long idCptTipoValor) {
		
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("cpt.idCptComplexidade"), "idCptComplexidade")
			.add(Projections.property("cpt.tpComplexidade"), "tpComplexidade")
			.add(Projections.property("cpt.vlComplexidade"), "vlComplexidade")
		.add(Projections.property("cpt.tpMedidaComplexidade"), "tpMedidaComplexidade");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "cpt")			
			.add(Restrictions.eq("cpt.cptTipoValor.id", idCptTipoValor))
			.setProjection(pl)
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);

		return super.findByDetachedCriteria(dc);
	}


}