package com.mercurio.lms.vendas.model.dao;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.vendas.model.ComiteNivelMarkup;

public class ComiteNivelMarkupDAO extends BaseCrudDao<ComiteNivelMarkup, Long> {

	@Override
	protected Class getPersistentClass() {
		return ComiteNivelMarkup.class;
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("tabelaPreco",FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.tipoTabelaPreco",FetchMode.JOIN);
		lazyFindById.put("tabelaPreco.subtipoTabelaPreco",FetchMode.JOIN);
		lazyFindById.put("parcelaPreco",FetchMode.JOIN);
		lazyFindById.put("parcelaPreco.tpParcelaPreco",FetchMode.JOIN);
		lazyFindById.put("eventoWorkflow",FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	
	public List<Map<String, Object>> findNiveisMarkupVigentesNaoIsentoByIdTabelaPreco(Long idTabelaPreco){
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idTabelaPreco", idTabelaPreco);
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idParcelaPreco", Hibernate.LONG);
				sqlQuery.addScalar("pcVariacao", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("blIsento", Hibernate.STRING);
				sqlQuery.addScalar("nrTipoEvento", Hibernate.SHORT);
			}
		};
		
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append("cnm.id_parcela_preco as idParcelaPreco ");
		sql.append(",cnm.pc_variacao as pcVariacao ");
		sql.append(",cnm.bl_isento as blIsento ");
		sql.append(",tp.nr_tipo_evento as nrTipoEvento ");
		sql.append("FROM ");
		sql.append("comite_nivel_markup cnm ");
		sql.append(",tipo_evento tp ");
		sql.append("WHERE ");
		sql.append("cnm.id_evento_workflow = tp.id_tipo_evento ");
		sql.append("AND id_tabela_preco = :idTabelaPreco ");
		sql.append("AND trunc(dt_vigencia_inicial) <= trunc(sysdate) ");
		sql.append("AND trunc(dt_vigencia_final) >= trunc(sysdate) ");
		sql.append("ORDER BY cnm.pc_variacao, cnm.id_parcela_preco ");
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, confSql);
	}
	
	
	public List<ComiteNivelMarkup> findByTabelaParcelaComiteVigencia(
			Long idTabelaPreco, Long idParcelaPreco, Long idEventoWorkflow,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		StringBuilder query = new StringBuilder(
				"SELECT c FROM ComiteNivelMarkup c WHERE 1 = 1 ");
		Map<String, Object> parametros = new HashMap<String, Object>();
		if (idTabelaPreco != null && idTabelaPreco > 0) {
			query.append("AND c.tabelaPreco.idTabelaPreco = :idTabela ");
			parametros.put("idTabela", idTabelaPreco);
		}
		if (idParcelaPreco != null && idParcelaPreco > 0) {
			query.append("AND c.parcelaPreco.idParcelaPreco = :idParcela ");
			parametros.put("idParcela", idParcelaPreco);
		}
		if (idEventoWorkflow != null && idEventoWorkflow > 0) {
			query.append("AND c.eventoWorkflow.idEventoWorkflow= :idEventoWorkflow ");
			parametros.put("idEventoWorkflow", idEventoWorkflow);
		}
		query.append("AND (" +
				"    (:dtVigenciaInicial < c.dtVigenciaInicial AND :dtVigenciaFinal > c.dtVigenciaFinal) " +
				" OR (:dtVigenciaInicial BETWEEN c.dtVigenciaInicial AND c.dtVigenciaFinal)" +
				" OR (:dtVigenciaFinal BETWEEN c.dtVigenciaInicial AND c.dtVigenciaFinal) " +
				") ");
		
		parametros.put("dtVigenciaInicial", dtVigenciaInicial);
		parametros.put("dtVigenciaFinal", dtVigenciaFinal);

		@SuppressWarnings("unchecked")
		List<ComiteNivelMarkup> lista = getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametros);
		if (CollectionUtils.isEmpty(lista)) {
			return Collections.emptyList();
		}
		for (ComiteNivelMarkup comite : lista) {
			getAdsmHibernateTemplate().evict(comite);
		}
		return lista;
	}
	
}
