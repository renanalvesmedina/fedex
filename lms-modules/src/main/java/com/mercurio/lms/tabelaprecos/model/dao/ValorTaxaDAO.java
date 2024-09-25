package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ValorTaxaDAO extends BaseCrudDao<ValorTaxa, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ValorTaxa.class;
    }
    
    public TypedFlatMap findByIdTabelaPrecoParcelaIdTabelaPreco(Long idTabelaPrecoParcela, Long idTabelaPreco) {
    	ProjectionList pl = Projections.projectionList()
    		.add(Projections.property("tpp.idTabelaPrecoParcela"), "idTabelaPrecoParcela")
    		.add(Projections.property("vt.vlTaxa"), "vlTaxa")
    		.add(Projections.property("vt.vlExcedente"), "vlExcedente")
    		.add(Projections.property("vt.psTaxado"), "psTaxado");
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vt");
		dc.setProjection(pl);
		dc.createAlias("vt.tabelaPrecoParcela", "tpp");
		
		if (idTabelaPreco != null) {
			dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		}
		if (idTabelaPrecoParcela != null) {
			dc.add(Restrictions.eq("tpp.idTabelaPrecoParcela", idTabelaPrecoParcela));
		}
		
		dc.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
    	
    	List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (result != null && result.size() > 0) {
    		return (TypedFlatMap) result.get(0);
    	}
    	
    	return null;
    }
    
    public ResultSetPage findPaginatedByIdTabelaPreco(Long idTabelaPreco, FindDefinition def) {
    	ProjectionList pl = Projections.projectionList()
    		.add(Projections.property("tpp.idTabelaPrecoParcela"), "idTabelaPrecoParcela")
    		.add(Projections.property("pp.dsParcelaPreco"), "parcelaPreco_dsParcelaPreco")
    		.add(Projections.property("vt.idValorTaxa"), "idValorTaxa")
    		.add(Projections.property("vt.vlTaxa"), "valorTaxa_vlTaxa")
    		.add(Projections.property("vt.psTaxado"), "valorTaxa_psTaxado")
    		.add(Projections.property("vt.vlExcedente"), "valorTaxa_vlExcedente");
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vt")
    		.setProjection(pl)
    		.createAlias("vt.tabelaPrecoParcela", "tpp")
    		.createAlias("tpp.parcelaPreco", "pp")
    		.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco))
    		.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());
    	
    	return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
    }
    
    public Integer getRowCountByIdTabelaPreco(Long idTabelaPreco) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "vt")
			.setProjection(Projections.rowCount())
			.createAlias("vt.tabelaPrecoParcela", "tpp")
    		.createAlias("tpp.parcelaPreco", "pp")
			.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
    	
    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#initFindByIdLazyProperties(java.util.Map)
	 */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tabelaPrecoParcela", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoParcela.parcelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoParcela.tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoParcela.tabelaPreco.tabelaPreco", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
}