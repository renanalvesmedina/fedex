package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PrecoFreteDAO extends BaseCrudDao<PrecoFrete, Long> {

	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition def) {
		StringBuilder sqlFrom = new StringBuilder();
		sqlFrom.append(PrecoFrete.class.getName()).append(" as pf ");
		sqlFrom.append(" left join fetch pf.tarifaPreco as tp ");
		sqlFrom.append(" left join fetch pf.rotaPreco as rp ");
		sqlFrom.append(" left join fetch rp.zonaByIdZonaOrigem as zo ");
		sqlFrom.append(" left join fetch rp.unidadeFederativaByIdUfOrigem as ufo ");
		sqlFrom.append(" left join fetch ufo.pais as po ");
		sqlFrom.append(" left join fetch rp.filialByIdFilialOrigem fo ");
		sqlFrom.append(" left join fetch rp.municipioByIdMunicipioOrigem as mo ");
		sqlFrom.append(" left join fetch rp.aeroportoByIdAeroportoOrigem as ao ");
		sqlFrom.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem tlo ");
		sqlFrom.append(" left join fetch rp.zonaByIdZonaDestino as zd ");
		sqlFrom.append(" left join fetch rp.unidadeFederativaByIdUfDestino as ufd ");
		sqlFrom.append(" left join fetch ufd.pais as pd ");
		sqlFrom.append(" left join fetch rp.filialByIdFilialDestino fd ");
		sqlFrom.append(" left join fetch rp.municipioByIdMunicipioDestino as md ");
		sqlFrom.append(" left join fetch rp.aeroportoByIdAeroportoDestino as ad ");
		sqlFrom.append(" left join fetch rp.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino tld ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(sqlFrom.toString());

		sql.addOrderBy("tp.cdTarifaPreco");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zo.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("po.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufo.sgUnidadeFederativa");
		sql.addOrderBy("fo.sgFilial");
		sql.addOrderBy("mo.nmMunicipio");
		sql.addOrderBy("ao.sgAeroporto");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlo.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zd.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("pd.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufd.sgUnidadeFederativa");
		sql.addOrderBy("fd.sgFilial");
		sql.addOrderBy("md.nmMunicipio");
		sql.addOrderBy("ad.sgAeroporto");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tld.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		sql.addCriteria("pf.tabelaPrecoParcela.idTabelaPrecoParcela", "=", criteria.getLong("tabelaPrecoParcela.idTabelaPrecoParcela"));
		sql.addCriteria("tp.idTarifaPreco", "=", criteria.getLong("tarifaPreco.idTarifaPreco"));
		sql.addCriteria("rp.idRotaPreco", "=", criteria.getLong("rotaPreco.idRotaPreco"));

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), def.getCurrentPage(), def.getPageSize(),sql.getCriteria());
	}


	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PrecoFrete.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tarifaPreco", FetchMode.JOIN);
		lazyFindById.put("rotaPreco", FetchMode.JOIN);
		
		lazyFindById.put("rotaPreco.zonaByIdZonaOrigem", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.municipioByIdMunicipioOrigem", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.aeroportoByIdAeroportoOrigem.pessoa", FetchMode.JOIN);
		
		lazyFindById.put("rotaPreco.zonaByIdZonaDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.municipioByIdMunicipioDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.filialByIdFilialDestino.pessoa", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		lazyFindById.put("rotaPreco.aeroportoByIdAeroportoDestino.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("tarifaPreco", FetchMode.JOIN);
		lazyFindPaginated.put("rotaPreco", FetchMode.JOIN);
	}

	public Integer getCountTabelaPrecoEfetuada(List ids){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pf")
			.setProjection(Projections.rowCount())
			.createAlias("pf.tabelaPrecoParcela", "pftpp")
			.createAlias("pftpp.tabelaPreco", "pftpptp")
			.add(Restrictions.in("pf.id", ids))
			.add(Restrictions.eq("pftpptp.blEfetivada", Boolean.TRUE))
			;
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public PrecoFrete findPrecoFrete(Long idTabelaPreco, String cdParcelaPreco, Long idTarifaPreco) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "pf");
		dc.createAlias("pf.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("pf.tarifaPreco", "tp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		dc.add(Restrictions.eq("tp.id", idTarifaPreco));

		return (PrecoFrete)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public PrecoFrete findByIdTabelaPrecoParaMarkup(Long idTabelaPreco) {
		StringBuilder query = new StringBuilder()
		.append(" SELECT pf ")
		.append(" FROM " + PrecoFrete.class.getName() + " pf ")
		.append(" left join fetch pf.rotaPreco rp")
		.append(" left join fetch pf.tarifaPreco tp")
		.append(" left join fetch pf.tabelaPrecoParcela tpp")
		.append(" left join fetch tpp.parcelaPreco pp")
		.append(" WHERE ")
		.append(" tpp.tabelaPreco.idTabelaPreco = :idTabelaPreco ")
		.append(" AND rownum = 1 ");
	
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idTabelaPreco", idTabelaPreco);
	
		return (PrecoFrete) getAdsmHibernateTemplate().findUniqueResult(query.toString(), params);
	}
	
	public PrecoFrete findPrecoFrete(Long idTabelaPreco, Long idParcelaPreco, Long idRotaPreco, Long idTarifaPreco) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pf ");
		sql.append(" FROM " + PrecoFrete.class.getName() + " pf ");
		sql.append(" left join fetch pf.rotaPreco rp");
		sql.append(" left join fetch pf.tarifaPreco tp");
		sql.append(" left join fetch pf.tabelaPrecoParcela tpp");
		sql.append(" left join fetch tpp.parcelaPreco pp");
		sql.append(" WHERE 1 = 1 ");
		
		if(idTabelaPreco != null){
			sql.append(" AND tpp.tabelaPreco.idTabelaPreco = :idTabelaPreco ");
			params.put("idTabelaPreco", idTabelaPreco);
		}
		if(idParcelaPreco != null){
			sql.append(" AND pp.idParcelaPreco = :idParcelaPreco ");
			params.put("idParcelaPreco", idParcelaPreco);
		}
		if(idRotaPreco != null){
			sql.append(" AND rp.idRotaPreco = :idRotaPreco ");
			params.put("idRotaPreco", idRotaPreco);
		}
		if(idTarifaPreco != null){
			sql.append(" AND tp.idTarifaPreco = :idTarifaPreco ");
			params.put("idTarifaPreco", idTarifaPreco);
		}
	
		return (PrecoFrete) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), params);
	}
}