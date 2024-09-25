package com.mercurio.lms.tabelaprecos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TarifaPrecoRota;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TarifaPrecoRotaDAO extends BaseCrudDao<TarifaPrecoRota, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected final Class<TarifaPrecoRota> getPersistentClass() {
		return TarifaPrecoRota.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initFindByIdLazyProperties(Map map) {
		map.put("tarifaPreco", FetchMode.JOIN);
		map.put("rotaPreco", FetchMode.JOIN);
		map.put("rotaPreco.zonaByIdZonaDestino", FetchMode.JOIN);
		map.put("rotaPreco.zonaByIdZonaOrigem", FetchMode.JOIN);
		map.put("rotaPreco.aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		map.put("rotaPreco.aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		map.put("rotaPreco.municipioByIdMunicipioOrigem", FetchMode.JOIN);
		map.put("rotaPreco.municipioByIdMunicipioDestino", FetchMode.JOIN);
		map.put("rotaPreco.unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		map.put("rotaPreco.unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		map.put("rotaPreco.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		map.put("rotaPreco.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino", FetchMode.JOIN);
		map.put("rotaPreco.tipoLocalizacaoMunicipioComercialOrigem", FetchMode.JOIN);
		map.put("rotaPreco.tipoLocalizacaoMunicipioComercialDestino", FetchMode.JOIN);
		map.put("rotaPreco.paisByIdPaisOrigem", FetchMode.JOIN);
		map.put("rotaPreco.paisByIdPaisDestino", FetchMode.JOIN);
		map.put("rotaPreco.filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("rotaPreco.filialByIdFilialDestino", FetchMode.JOIN);
		map.put("tabelaPreco", FetchMode.JOIN);
		map.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		map.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("tarifaPreco", FetchMode.JOIN);
		map.put("rotaPreco", FetchMode.JOIN);
		map.put("rotaPreco.zonaByIdZonaDestino", FetchMode.JOIN);
		map.put("rotaPreco.zonaByIdZonaOrigem", FetchMode.JOIN);
		map.put("rotaPreco.aeroportoByIdAeroportoOrigem", FetchMode.JOIN);
		map.put("rotaPreco.aeroportoByIdAeroportoDestino", FetchMode.JOIN);
		map.put("rotaPreco.municipioByIdMunicipioOrigem", FetchMode.JOIN);
		map.put("rotaPreco.municipioByIdMunicipioDestino", FetchMode.JOIN);
		map.put("rotaPreco.unidadeFederativaByIdUfOrigem", FetchMode.JOIN);
		map.put("rotaPreco.unidadeFederativaByIdUfDestino", FetchMode.JOIN);
		map.put("rotaPreco.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem", FetchMode.JOIN);
		map.put("rotaPreco.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino", FetchMode.JOIN);
		map.put("rotaPreco.paisByIdPaisOrigem", FetchMode.JOIN);
		map.put("rotaPreco.paisByIdPaisDestino", FetchMode.JOIN);
		map.put("rotaPreco.filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("rotaPreco.filialByIdFilialDestino", FetchMode.JOIN);
		map.put("tabelaPreco", FetchMode.JOIN);
		map.put("tabelaPreco.tipoTabelaPreco", FetchMode.JOIN);
		map.put("tabelaPreco.subtipoTabelaPreco", FetchMode.JOIN);
	}

	
	public ResultSetPage findPaginated(TypedFlatMap criteria,FindDefinition def) {
		StringBuilder sqlFrom = new StringBuilder()
		.append(TarifaPrecoRota.class.getName()).append(" as tpr ")
		.append("left join fetch tpr.tabelaPreco as tabela ")
		.append("left join fetch tabela.tipoTabelaPreco as ttp ")
		.append("left join fetch tabela.subtipoTabelaPreco as stp ")
		.append("left join fetch tpr.tarifaPreco as tarifa ") 
		.append("left join fetch tpr.rotaPreco as rota ")
		.append("left join fetch rota.zonaByIdZonaOrigem as zo ")
		.append("left join fetch rota.zonaByIdZonaDestino as zd ")
		.append("left join fetch rota.paisByIdPaisOrigem as po ")
		.append("left join fetch rota.paisByIdPaisDestino as pd ")
		.append("left join fetch rota.unidadeFederativaByIdUfOrigem as ufo ")
		.append("left join fetch rota.unidadeFederativaByIdUfDestino as ufd ")
		.append("left join fetch rota.filialByIdFilialOrigem as fo ")
		.append("left join fetch rota.filialByIdFilialDestino as fd ")
		.append("left join fetch rota.municipioByIdMunicipioOrigem as mo ")
		.append("left join fetch rota.municipioByIdMunicipioDestino as md ")
		.append("left join fetch rota.aeroportoByIdAeroportoOrigem as ao ")
		.append("left join fetch rota.aeroportoByIdAeroportoDestino as ad ")
		.append("left join fetch rota.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem as tlo ")
		.append("left join fetch rota.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino as tld ")
		.append("left join fetch rota.tipoLocalizacaoMunicipioComercialOrigem as tlco ")
		.append("left join fetch rota.tipoLocalizacaoMunicipioComercialDestino as tlcd ")
		.append("left join fetch rota.grupoRegiaoOrigem  as gro ")
		.append("left join fetch rota.grupoRegiaoDestino as grd ");

		
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("tabela.id", "=", criteria.getLong("tabelaPreco.idTabelaPreco"));
		sql.addCriteria("tarifa.id", "=", criteria.getLong("tarifaPreco.idTarifaPreco"));
		sql.addCriteria("rota.id", "=", criteria.getLong("rotaPreco.idRotaPreco"));

		sql.addOrderBy("ttp.tpTipoTabelaPreco");
		sql.addOrderBy("ttp.nrVersao");
		sql.addOrderBy("stp.tpSubtipoTabelaPreco");
		sql.addOrderBy("tarifa.cdTarifaPreco");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zo.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("po.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufo.sgUnidadeFederativa");
		sql.addOrderBy("fo.sgFilial");
		sql.addOrderBy("mo.nmMunicipio");
		sql.addOrderBy("ao.sgAeroporto");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlo.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlco.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("zd.dsZona", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("pd.nmPais", LocaleContextHolder.getLocale()));
		sql.addOrderBy("ufd.sgUnidadeFederativa");
		sql.addOrderBy("fd.sgFilial");
		sql.addOrderBy("md.nmMunicipio");
		sql.addOrderBy("ad.sgAeroporto");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tld.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("tlcd.dsTipoLocalizacaoMunicipio", LocaleContextHolder.getLocale()));

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),def.getCurrentPage(),def.getPageSize(),sql.getCriteria());
	}

	public List findByIdTabelaPreco(Long idTabelaPreco) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "trp");
		dc.add(Restrictions.eq("trp.tabelaPreco.id", idTabelaPreco));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public List<TarifaPrecoRota> findTarifaPrecoRota(Long idTabelaPreco, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {

		DetachedCriteria dc = DetachedCriteria.forClass(TarifaPrecoRota.class, "tpr");
		
		dc.createAlias("tpr.rotaPreco","rp");
		dc.createAlias("tpr.tarifaPreco","tarp");
		dc.createAlias("tpr.tabelaPreco","tabp");
		
		dc.add(Restrictions.eq("tabp.idTabelaPreco", idTabelaPreco));

		RotaPrecoUtils.addRotaPrecoRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);

		return getAdsmHibernateTemplate().findByCriteria(dc);
	}

	public TarifaPrecoRota findByTarifaRota(Long idTabelaPreco,
			Long idTarifaPreco, Long idRotaPreco) {
		StringBuilder query = new StringBuilder("SELECT t FROM TarifaPrecoRota t ")
			.append("WHERE t.tabelaPreco.idTabelaPreco = :idTabela ")
			.append(" AND t.rotaPreco.idRotaPreco = :idRota ")
			.append(" AND t.tarifaPreco.idTarifaPreco = :idTarifa ");
		Map<String, Long> parametros = new HashMap<String, Long>();
		parametros.put("idTabela", idTabelaPreco);
		parametros.put("idRota", idRotaPreco);
		parametros.put("idTarifa", idTarifaPreco);
		
		return (TarifaPrecoRota) getAdsmHibernateTemplate().findUniqueResult(query.toString(), parametros);
	}

	public void removeByIdTabelaPreco(Long idTabelaPreco) {
		StringBuilder query = new StringBuilder("DELETE FROM tarifa_preco_rota t ")
			.append("WHERE t.id_tabela_preco = :idTabela");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabela", idTabelaPreco);
		
		getAdsmHibernateTemplate().executeUpdateBySql(query.toString(), parametros);
	
	}

	@SuppressWarnings("unchecked")
	public List<TarifaPrecoRota> findByTabelaETarifa(Long idTabelaPreco, Long idTarifa) {
		StringBuilder query = new StringBuilder("SELECT t FROM TarifaPrecoRota t ")
			.append("WHERE t.tabelaPreco.idTabelaPreco = :idTabela ")
			.append(" AND t.tarifaPreco.idTarifaPreco = :idTarifa ");
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idTabela", idTabelaPreco);
		parametros.put("idTarifa", idTarifa);
	
		return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
	}
	
	public Boolean updateTarifaPrecoRota(Long idRotaTabelaBase, Long idRotaTabelaNova, Long idTabelaNova){
		StringBuilder query = new StringBuilder("Update TARIFA_PRECO_ROTA ")
		.append(" Set ID_ROTA_PRECO = :idRotaTabelaNova ")
		.append(" Where ID_TABELA_PRECO = :idTabelaNova ")
		.append(" and   ID_ROTA_PRECO =  :idRotaTabelaBase ");

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idRotaTabelaBase", idRotaTabelaBase);
		parametros.put("idRotaTabelaNova", idRotaTabelaNova);
		parametros.put("idTabelaNova", idTabelaNova);

		getAdsmHibernateTemplate().executeUpdateBySql(query.toString(), parametros);

		return true;
	}
}