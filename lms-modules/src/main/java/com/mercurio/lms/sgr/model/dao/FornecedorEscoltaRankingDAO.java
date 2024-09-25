package com.mercurio.lms.sgr.model.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.FornecEscoltaRankingItem;
import com.mercurio.lms.sgr.model.FornecedorEscoltaRanking;

public class FornecedorEscoltaRankingDAO extends BaseCrudDao<FornecedorEscoltaRanking, Long> {

	@SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
		return FornecedorEscoltaRanking.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("cliente", FetchMode.JOIN);
		fetchModes.put("cliente.pessoa", FetchMode.JOIN);
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("fornecedoresEscoltaRankingItem", FetchMode.SELECT);
	}
	
	
	public Integer getRowCountByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeQueryByFilter(criteria), criteria);
	}

	@SuppressWarnings("unchecked")
	public List<FornecedorEscoltaRanking> findByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeQueryByFilter(criteria), criteria);
	}

	private String makeQueryByFilter(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("FROM FornecedorEscoltaRanking fornecedorEscoltaRanking ")
				.append("LEFT JOIN FETCH fornecedorEscoltaRanking.cliente cliente ")
				.append("LEFT JOIN FETCH cliente.pessoa pessoa ")
				.append("LEFT JOIN FETCH fornecedorEscoltaRanking.filial filial ")
				.append("LEFT JOIN FETCH filial.pessoa filialPessoa ")
				.append("WHERE 1 = 1 ");
		appendIfContains(criteria, "idCliente", hql, "AND cliente.idCliente = :idCliente ");
		appendIfContains(criteria, "idFilial", hql, "AND filial.idFilial = :idFilial ");
		appendIfContains(criteria, "dtVigenciaInicial", hql, "AND :dtVigenciaInicial >= fornecedorEscoltaRanking.dtVigenciaInicial ");
		appendIfContains(criteria, "dtVigenciaFinal", hql, "AND (:dtVigenciaFinal <= fornecedorEscoltaRanking.dtVigenciaFinal or fornecedorEscoltaRanking.dtVigenciaFinal is null) ");
		return hql.toString();
	}

	private void appendIfContains(TypedFlatMap criteria, String key, StringBuilder hql, String clause) {
		if (criteria.containsKey(key) && !"".equals(criteria.get(key))) {
			hql.append(clause);
		}
	}


	@SuppressWarnings("unchecked")
	public List<FornecEscoltaRankingItem> findRankItemByIdRanking(Long idfornecedorEscoltaRanking) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idfornecedorEscoltaRanking", idfornecedorEscoltaRanking);
		return getAdsmHibernateTemplate().findByNamedParam(makeRankQueryByIdRanking(), criteria);
	}
	
	private String makeRankQueryByIdRanking() {
		StringBuilder hql = new StringBuilder()
				.append("FROM FornecEscoltaRankingItem fornecEscoltaRankingItem")
				.append("LEFT JOIN FETCH fornecEscoltaRankingItem.fornecedorEscoltaRanking fornecedorEscoltaRanking ")
				.append("LEFT JOIN FETCH fornecedorEscoltaRanking.cliente  cliente")
				.append("WHERE fornecedorEscoltaRanking.idfornecedorEscoltaRanking = :idfornecedorEscoltaRanking");
		return hql.toString();
	}
	
	
	public void storeItemRanking(FornecedorEscoltaRanking fornecedorEscoltaRanking) {
		if (fornecedorEscoltaRanking.getIdFornecedorEscoltaRanking() != null  ) {
			final Long idFornecedorEscoltaRanking = fornecedorEscoltaRanking.getIdFornecedorEscoltaRanking();
			getAdsmHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) {
					Query query = session.createQuery(
							"DELETE FornecEscoltaRankingItem fornEscRankItem " +
							"WHERE fornEscRankItem.fornecedorEscoltaRanking.idFornecedorEscoltaRanking = ? ");
					query.setParameter(0, idFornecedorEscoltaRanking);
					return query.executeUpdate();
				}
			});
		}
		getAdsmHibernateTemplate().saveOrUpdateAll(fornecedorEscoltaRanking.getFornecedoresEscoltaRankingItem());
	}

	public void removeItemsByIdFornecEscoltaRanking(Long id) {
		removeItemsByIdsFornecEscoltaRanking(Arrays.asList(id));
	}
	
	public void removeItemsByIdsFornecEscoltaRanking(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FornecEscoltaRankingItem fornEscRankItem WHERE fornEscRankItem.fornecedorEscoltaRanking.idFornecedorEscoltaRanking in(:id) ", ids);
	}

	public boolean validateVigenciaRanking(FornecedorEscoltaRanking bean) {
		String sql = getSqlVigencia(bean);
		List<Object[]> resultList = getAdsmHibernateTemplate().findBySql(sql, getParamsVigencia(bean), null);
		return CollectionUtils.isEmpty(resultList) ? true : false;
	}


	private String getSqlVigencia(FornecedorEscoltaRanking bean) {
		StringBuilder sb = new StringBuilder()
		.append("SELECT * ") 
		.append("FROM fornecedor_escolta_ranking a ")
		.append("WHERE a.id_cliente ")
		.append(getSqlCliente(bean))
		.append("AND a.id_filial ")
		.append(getSqlFilial(bean))
		.append("AND ((:dataInicial >= a.dt_vigencia_inicial and (:dataInicial <= a.dt_vigencia_final or a.dt_vigencia_final is null)) ")
        .append("OR (:dataFinal >= a.dt_vigencia_inicial and (:dataFinal <= a.dt_vigencia_final or a.dt_vigencia_final is null)) ")
        .append("OR (:dataInicial <= a.dt_vigencia_inicial and (:dataFinal >= a.dt_vigencia_final)) ")
  		.append("OR (:dataInicial >= a.dt_vigencia_inicial and (:dataFinal <= a.dt_vigencia_final or a.dt_vigencia_final is null)) ")
  		.append("OR (:dataFinal is null and (:dataInicial <= a.dt_vigencia_final or a.dt_vigencia_final is null) ) ")
  		.append(" ) ")
		.append("AND rownum <= 1 ");
		
		if (bean.getIdFornecedorEscoltaRanking() != null) {
			sb.append("AND a.id_fornecedor_escolta_ranking != " + bean.getIdFornecedorEscoltaRanking());
		}
		
		return sb.toString();
	}
	
	private Map<String, Object> getParamsVigencia(FornecedorEscoltaRanking bean) {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("idFilial", bean.getFilial() == null ? null : bean.getFilial().getIdFilial());
		criteria.put("idCliente", bean.getCliente() == null ? null : bean.getCliente().getIdCliente());
		criteria.put("dataInicial", bean.getDtVigenciaInicial() == null ? "null" : bean.getDtVigenciaInicial());
		criteria.put("dataFinal", bean.getDtVigenciaFinal() == null ? "null" : bean.getDtVigenciaFinal());
		return criteria;
	}
	
	private String getSqlFilial(FornecedorEscoltaRanking bean) {
		String sqlFilial = null;
		if (bean.getFilial() != null) {
			sqlFilial = "= :idFilial ";
		} else {
			sqlFilial = "is null ";
		}
		return sqlFilial;
	}

	private String getSqlCliente(FornecedorEscoltaRanking bean) {
		String sqlCliente = null;
		if (bean.getCliente() != null) {
			sqlCliente = "= :idCliente ";
		} else {
			sqlCliente = "is null ";
		}
		return sqlCliente;
	}
}
