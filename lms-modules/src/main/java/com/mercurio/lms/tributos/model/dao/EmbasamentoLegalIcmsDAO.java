package com.mercurio.lms.tributos.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.EmbasamentoLegalIcms;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EmbasamentoLegalIcmsDAO extends BaseCrudDao<EmbasamentoLegalIcms, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EmbasamentoLegalIcms.class;
    }
    
    @Override
    protected void initFindPaginatedLazyProperties(Map fetchModes) {    	
    	fetchModes.put("unidadeFederativaOrigem", FetchMode.JOIN);
		fetchModes.put("tipoTributacaoIcms", FetchMode.JOIN);	
    }
    
    
	private ProjectionList createProjectionList() {
		return Projections.projectionList()
		.add(Projections.property("idEmbasamento"), "idEmbasamento")		
		.add(Projections.property("uf.idUnidadeFederativa"), "unidadeFederativaOrigem.idUnidadeFederativa")
		.add(Projections.property("uf.sgUnidadeFederativa"), "unidadeFederativaOrigem.sgUnidadeFederativa")
		.add(Projections.property("tr.dsTipoTributacaoIcms"), "tipoTributacaoIcms.dsTipoTributacaoIcms")
		.add(Projections.property("tr.idTipoTributacaoIcms"), "tipoTributacaoIcms.idTipoTributacaoIcms")
		.add(Projections.property("dsEmbLegalCompleto"), "dsEmbLegalCompleto")
		.add(Projections.property("dsEmbLegalResumido"), "dsEmbLegalResumido")
		.add(Projections.property("cdEmbLegalMasterSaf"), "cdEmbLegalMasterSaf")
		.add(Projections.property("obEmbLegalIcms"), "obEmbLegalIcms");
	}    

	
	public EmbasamentoLegalIcms findById(Long id) {
		DetachedCriteria dc = createDetachedCriteria()
			.createAlias("unidadeFederativaOrigem", "uf")
			.createAlias("tipoTributacaoIcms", "tr")
			.setFetchMode("uf", FetchMode.JOIN)
			.setFetchMode("tr", FetchMode.JOIN)
			.setProjection(createProjectionList())
			.add(Restrictions.eq("id", id));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return (EmbasamentoLegalIcms) getAdsmHibernateTemplate().findUniqueResult(dc);
	}	
	
	
	private DetachedCriteria createPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = createDetachedCriteria()
			.createAlias("unidadeFederativaOrigem", "uf")
			.createAlias("tipoTributacaoIcms", "tr")			
			.setFetchMode("uf", FetchMode.JOIN)
			.setFetchMode("tr", FetchMode.JOIN)
			.addOrder(Order.asc("uf.sgUnidadeFederativa"))
			.addOrder(Order.asc("tr.dsTipoTributacaoIcms"))
			.addOrder(Order.asc("dsEmbLegalCompleto"));

		/** Restrições */
		Long idUnidade    = MapUtils.getLong(criteria,"unidadeFederativaOrigem.idUnidadeFederativa");
		if(LongUtils.hasValue(idUnidade)){
			dc.add(Restrictions.eq("uf.idUnidadeFederativa", idUnidade));
		}
		
		String sgUnidade  = MapUtils.getString(criteria,"unidadeFederativaOrigem.sgUnidadeFederativa");
		if(StringUtils.isNotBlank(sgUnidade)){
			dc.add(Restrictions.eq("uf.sgUnidadeFederativa", sgUnidade));
		}
		
		Long idTributacao = MapUtils.getLong(criteria,"tipoTributacaoIcms.idTipoTributacaoIcms");
		if(LongUtils.hasValue(idTributacao)){		
			dc.add(Restrictions.eq("tr.idTipoTributacaoIcms", idTributacao));
		}
		return dc;
	}	
	
	
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		DetachedCriteria dc = createPaginated(criteria);
		dc.setProjection(createProjectionList());

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}	

	
	public Integer getRowCount(Map criteria) {		
		return super.getRowCount(criteria);
	}	
	
	public List findEmbasamentoLookup(TypedFlatMap criteria){
		
		String sgUF = MapUtils.getString(criteria, "unidadeFederativaOrigem.sgUnidadeFederativa");
				
		DetachedCriteria dc = createDetachedCriteria()
			.createAlias("unidadeFederativaOrigem", "uf")
			.createAlias("tipoTributacaoIcms", "tr")
			.setProjection(createProjectionList())
			.add(Restrictions.eq("uf.sgUnidadeFederativa", sgUF));
			
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(getPersistentClass()));
		return getAdsmHibernateTemplate().findByCriteria(dc);		
	}
	
}