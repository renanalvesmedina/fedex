package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GeneralidadeDAO extends BaseCrudDao<Generalidade, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Generalidade.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tabelaPrecoParcela", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoParcela.parcelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoParcela.tabelaPreco", FetchMode.JOIN);
		lazyFindById.put("tabelaPrecoParcela.tabelaPreco.tabelaPreco", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

    public TypedFlatMap findByIdTabelaPrecoParcelaIdTabelaPreco(Long idTabelaPrecoParcela, Long idTabelaPreco) {
    	ProjectionList pl = Projections.projectionList()
    		.add(Projections.property("g.vlGeneralidade"), "vlGeneralidade")
    		.add(Projections.property("g.vlMinimo"), "vlMinimo");
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "g")
    		.setProjection(pl)
    		.createAlias("g.tabelaPrecoParcela", "tpp");

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

    public Generalidade findGeneralidade(Long idTabelaPreco, Long idParcelaPreco) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("tabelaPrecoParcela", "tpp");
		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.id", idParcelaPreco));
		return (Generalidade) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

    public ResultSetPage findPaginatedByIdTabelaPreco(Long idTabelaPreco, FindDefinition def) {
    	ProjectionList pl = Projections.projectionList()
    		.add(Projections.property("tpp.idTabelaPrecoParcela"), "idTabelaPrecoParcela")
    		.add(Projections.property("pp.dsParcelaPreco"), "parcelaPreco_dsParcelaPreco")
    		.add(Projections.property("g.idGeneralidade"), "idGeneralidade")
    		.add(Projections.property("g.vlGeneralidade"), "generalidade_vlGeneralidade")
    		.add(Projections.property("g.vlMinimo"), "generalidade_vlMinimo");

    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "g")
    		.setProjection(pl)
    		.createAlias("g.tabelaPrecoParcela", "tpp")
    		.createAlias("tpp.parcelaPreco", "pp")
    		.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco))
    		.addOrder(OrderVarcharI18n.asc("pp.nmParcelaPreco", LocaleContextHolder.getLocale()))
    		.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

    	return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
    }

    public Integer getRowCountByIdTabelaPreco(Long idTabelaPreco) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "g")
			.setProjection(Projections.rowCount())
			.createAlias("g.tabelaPrecoParcela", "tpp")
    		.createAlias("tpp.parcelaPreco", "pp")
			.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));

    	return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }
    
	public Generalidade findRefaturamentoByIdTabelaPreco(Long idTabelaPreco) {
		DetachedCriteria dc = DetachedCriteria
				.forClass(getPersistentClass(), "g")
				.createAlias("g.tabelaPrecoParcela", "tpp")
				.createAlias("tpp.tabelaPreco", "tp")
				.createAlias("tpp.parcelaPreco", "pp")
				.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco))
				.add(Restrictions.eq("pp.cdParcelaPreco",
						ConstantesExpedicao.CD_REFATURAMENTO));
		Object result = getAdsmHibernateTemplate().findUniqueResult(dc);
		return (Generalidade) result;
    }
    
}