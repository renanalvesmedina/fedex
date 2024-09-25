package com.mercurio.lms.tabelaprecos.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class FaixaProgressivaDAO extends BaseCrudDao<FaixaProgressiva, Long> {

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("produtoEspecifico", FetchMode.JOIN);
		fetchModes.put("tabelaPrecoParcela", FetchMode.JOIN);
		fetchModes.put("unidadeMedida", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("produtoEspecifico", FetchMode.JOIN);
		lazyFindPaginated.put("tabelaPrecoParcela", FetchMode.JOIN);
		lazyFindPaginated.put("unidadeMedida", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FaixaProgressiva.class;
	}

	public FaixaProgressiva findFaixaProgressivaEnquadrada(Long idTabelaPreco, Long idParcelaPreco, BigDecimal pesoReferencia, Long idProdutoEspecifico) {
		FaixaProgressiva faixaProgressiva = null;

		DetachedCriteria dcMin = DetachedCriteria.forClass(FaixaProgressiva.class, "fpb");
		dcMin.setProjection(Projections.min("fpb.vlFaixaProgressiva"));
		dcMin.add(Restrictions.ge("fpb.vlFaixaProgressiva", pesoReferencia));
		dcMin.add(Restrictions.eqProperty("tppa.idTabelaPrecoParcela", "fpb.tabelaPrecoParcela.idTabelaPrecoParcela"));
		dcMin.add(Restrictions.leProperty("fpb.vlFaixaProgressiva", "fpa.vlFaixaProgressiva"));

		DetachedCriteria dc = DetachedCriteria.forClass(FaixaProgressiva.class, "fpa");
		dc.createAlias("fpa.tabelaPrecoParcela", "tppa");
		dc.add(Restrictions.eq("tppa.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tppa.parcelaPreco.idParcelaPreco", idParcelaPreco));
		if(idProdutoEspecifico != null)
			dc.add(Restrictions.eq("fpa.produtoEspecifico.idProdutoEspecifico", idProdutoEspecifico));
		dc.add(Property.forName("fpa.vlFaixaProgressiva").eq(dcMin));

		List faixas = findByDetachedCriteria(dc);
		if(faixas.size() == 1) {
			faixaProgressiva = (FaixaProgressiva) faixas.get(0);
		}
		return faixaProgressiva;
	}

	public FaixaProgressiva findFaixaProgressivaMenorPeso(Long idTabelaPreco, Long idParcelaPreco) {
		FaixaProgressiva faixaProgressiva = null;

		DetachedCriteria dcMin = DetachedCriteria.forClass(FaixaProgressiva.class, "fpb");
		dcMin.setProjection(Projections.min("fpb.vlFaixaProgressiva"));
		dcMin.add(Restrictions.eqProperty("tppa.idTabelaPrecoParcela", "fpb.tabelaPrecoParcela.idTabelaPrecoParcela"));

		DetachedCriteria dc = DetachedCriteria.forClass(FaixaProgressiva.class, "fpa");
		dc.createAlias("fpa.tabelaPrecoParcela", "tppa");
		dc.add(Restrictions.eq("tppa.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tppa.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Property.forName("fpa.vlFaixaProgressiva").eq(dcMin));

		List faixas = findByDetachedCriteria(dc);
		if(faixas.size() == 1) {
			faixaProgressiva = (FaixaProgressiva) faixas.get(0);
		}
		return faixaProgressiva;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition def) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select new map(fp.idFaixaProgressiva as idFaixaProgressiva,");
		sql.append("        fp.vlFaixaProgressiva as vlFaixaProgressiva,");
		sql.append("        pe.nrTarifaEspecifica as produtoEspecifico_nrTarifaEspecifica,");
		sql.append("        fp.cdMinimoProgressivo as cdMinimoProgressivo,");
		sql.append("        fp.tpSituacao as tpSituacao,");
		sql.append("        fp.tpIndicadorCalculo as tpIndicadorCalculo,");
		sql.append("        um.dsUnidadeMedida as unidadeMedida_dsUnidadeMedida)");
		sql.append("   from ").append(FaixaProgressiva.class.getName()).append(" as fp");
		sql.append("        left join fp.produtoEspecifico as pe");
		sql.append("        left join fp.tabelaPrecoParcela as tpp");
		sql.append("        left join fp.unidadeMedida as um");
   		sql.append("  where tpp.id = ").append(criteria.getLong("tabelaPrecoParcela.idTabelaPrecoParcela"));
   		String cdMinimoProgressivo = criteria.getString("cdMinimoProgressivo");
   		if(StringUtils.isNotBlank(cdMinimoProgressivo)) {
   			sql.append(" and fp.cdMinimoProgressivo = '").append(cdMinimoProgressivo).append("'");
		}
   		String tpSituacao = criteria.getString("tpSituacao");
   		if(StringUtils.isNotBlank(tpSituacao)) {
   			sql.append(" and fp.tpSituacao = '").append(tpSituacao).append("'");
   		}
		sql.append(" order by fp.vlFaixaProgressiva,");
		sql.append("          pe.nrTarifaEspecifica");

		Object[] paramValues = null;
		return getAdsmHibernateTemplate().findPaginated(sql.toString(), def.getCurrentPage(), def.getPageSize(), paramValues);
	}

	public void removeById(Long id) {
		super.removeById(id, true);
	}

	public int removeByIds(List ids) {
		for (Iterator iter = ids.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			this.removeById(id);
		}
		return ids.size();
	}

	private void storeValorFaixaProgressiva(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
	}

	private void removeValorFaixaProgressiva(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}

	public FaixaProgressiva store(FaixaProgressiva bean, ItemList items) {
		super.store(bean);
		removeValorFaixaProgressiva(items.getRemovedItems());
		storeValorFaixaProgressiva(items.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();
		return bean;
	}

	/**
	 * Contabiliza quantas tabelaPreco efetuadas para os ids FaixaProgressiva.
	 * 
	 * @param ids
	 * @return
	 */
	public Integer getCountTabelaPrecoEfetuada(List ids){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "fp")
			.setProjection(Projections.rowCount())
			.createAlias("fp.tabelaPrecoParcela", "pftpp")
			.createAlias("pftpp.tabelaPreco", "pftpptp")
			.add(Restrictions.in("fp.id", ids))
			.add(Restrictions.eq("pftpptp.blEfetivada", Boolean.TRUE))
			;
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
	
	@SuppressWarnings("unchecked")
	public List<FaixaProgressiva> findFaixaProgressivaParaMarkup(Long idParcelaPreco, Long idTabelaPreco) {
		StringBuilder query = new StringBuilder()
			.append(" SELECT distinct fp ")
			.append(" FROM " + FaixaProgressiva.class.getName() + " fp ")
			.append(" left join fetch fp.produtoEspecifico pe")
			.append(" left join fetch fp.tabelaPrecoParcela tpp")
			.append(" left join fetch tpp.parcelaPreco pp")
			.append(" left join fetch tpp.tabelaPreco tp")
			.append(" WHERE ")
			.append(" pp.idParcelaPreco = :idParcelaPreco ")
			.append(" AND tp.idTabelaPreco = :idTabelaPreco ")
			.append(" order by fp.vlFaixaProgressiva, pe.nrTarifaEspecifica ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idParcelaPreco", idParcelaPreco);
		params.put("idTabelaPreco", idTabelaPreco);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<FaixaProgressiva> findByIds(List<Long> idsFaixaProgressiva) {
		StringBuilder query = new StringBuilder()
			.append(" SELECT fp ")
			.append(" FROM " + FaixaProgressiva.class.getName() + " fp ")
			.append(" join fetch fp.tabelaPrecoParcela tpp ")
			.append(" join fetch tpp.parcelaPreco pp ")
			.append(" WHERE ")
			.append(" fp.idFaixaProgressiva in (:idsFaixaProgressiva) ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idsFaixaProgressiva", idsFaixaProgressiva);

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), params);
	}
}