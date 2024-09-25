package com.mercurio.lms.sgr.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.FornecedorEscolta;
import com.mercurio.lms.sgr.model.FornecedorEscoltaImpedido;
import com.mercurio.lms.sgr.model.FranquiaFornecedorEscolta;

public class FornecedorEscoltaDAO extends BaseCrudDao<FornecedorEscolta, Long> {

	private static final String NR_IDENTIFICACAO = "nrIdentificacao";
	private static final String NM_PESSOA = "nmPessoa";

	@Override
	protected Class<FornecedorEscolta> getPersistentClass() {
		return FornecedorEscolta.class;
	}

	public Integer getRowCountByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeQueryByFilter(criteria), criteria);
	}

	@SuppressWarnings("unchecked")
	public List<FornecedorEscolta> findByFilter(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeQueryByFilter(criteria), criteria);
	}

	private String makeQueryByFilter(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
				.append("FROM FornecedorEscolta fornecedorEscolta ")
				.append("JOIN FETCH fornecedorEscolta.pessoa pessoa ")
				.append("WHERE 1 = 1 ");
		if (criteria.containsKey(NR_IDENTIFICACAO)) {
			String nrIdentificacao = criteria.getString(NR_IDENTIFICACAO).replaceAll("[^\\p{Digit}]", "") + "%";
			criteria.put(NR_IDENTIFICACAO, nrIdentificacao);
			hql.append("AND pessoa.nrIdentificacao LIKE :nrIdentificacao ");
		}
		if (criteria.containsKey(NM_PESSOA)) {
			String nmPessoa = "%" + criteria.getString(NM_PESSOA).replaceAll("[^\\p{Alnum}]+", "%").toUpperCase() + "%";
			criteria.put(NM_PESSOA, nmPessoa);
			hql.append("AND UPPER(pessoa.nmPessoa) LIKE :nmPessoa ");
		}
		return hql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<FornecedorEscolta> findSuggest(String value) {
		StringBuilder hql = new StringBuilder()
				.append("FROM FornecedorEscolta fornecedorEscolta ")
				.append("JOIN FETCH fornecedorEscolta.pessoa pessoa ");
		if (value.matches("[\\p{Digit}\\p{Punct}]+")) {
			String nrIdentificacao = value.replaceAll("[^\\p{Digit}]", "") + "%";
			hql.append("WHERE pessoa.nrIdentificacao LIKE '" + nrIdentificacao + "' ");
		} else {
			String nmPessoa = "%" + value.replaceAll("[^\\p{Alnum}]+", "%").toUpperCase() + "%";
			hql.append("WHERE UPPER(pessoa.nmPessoa) LIKE '" + nmPessoa + "' ");
		}
		return getAdsmHibernateTemplate().find(hql.toString());
	}

	public Integer getRowCountFranquiaByIdFornecedorEscolta(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeFranquiaQueryByIdFornecedorEscolta(), criteria);
	}

	@SuppressWarnings("unchecked")
	public List<FranquiaFornecedorEscolta> findFranquiaByIdFornecedorEscolta(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeFranquiaQueryByIdFornecedorEscolta(), criteria);
	}

	private String makeFranquiaQueryByIdFornecedorEscolta() {
		StringBuilder hql = new StringBuilder()
				.append("FROM FranquiaFornecedorEscolta franquiaFornecedorEscolta ")
				.append("LEFT JOIN FETCH franquiaFornecedorEscolta.filialOrigem.pessoa ")
				.append("LEFT JOIN FETCH franquiaFornecedorEscolta.filialDestino.pessoa ")
				.append("WHERE franquiaFornecedorEscolta.fornecedorEscolta.idFornecedorEscolta = :idFornecedorEscolta ");
		return hql.toString();
	}

	public FranquiaFornecedorEscolta findFranquiaById(Long id) {
		StringBuilder hql = new StringBuilder()
				.append("FROM FranquiaFornecedorEscolta franquiaFornecedorEscolta ")
				.append("LEFT JOIN FETCH franquiaFornecedorEscolta.filiaisAtendimento filiaisAtendimento ")
				.append("LEFT JOIN FETCH filiaisAtendimento.filial.pessoa ")
				.append("LEFT JOIN FETCH franquiaFornecedorEscolta.filialOrigem filialOrigem ")
				.append("LEFT JOIN FETCH filialOrigem.pessoa ")
				.append("LEFT JOIN FETCH franquiaFornecedorEscolta.filialDestino filialDestino ")
				.append("LEFT JOIN FETCH filialDestino.pessoa ")
				.append("WHERE franquiaFornecedorEscolta.idFranquiaFornecedorEscolta = :idFranquiaFornecedorEscolta ");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idFranquiaFornecedorEscolta", id);
		return (FranquiaFornecedorEscolta) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameters);
	}

	public void storeFranquia(FranquiaFornecedorEscolta franquia) {
		if (franquia.getIdFranquiaFornecedorEscolta() != null) {
			final Long idFranquiaFornecedorEscolta = franquia.getIdFranquiaFornecedorEscolta();
			getAdsmHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) {
					Query query = session.createQuery(
							"DELETE FranquiaFornecedorFilialAtendimento " +
							"WHERE franquiaFornecedorEscolta.idFranquiaFornecedorEscolta = ? ");
					query.setParameter(0, idFranquiaFornecedorEscolta);
					return query.executeUpdate();
				}
			});
		}
		store(franquia);
		getAdsmHibernateTemplate().saveOrUpdateAll(franquia.getFiliaisAtendimento());
	}

	public void removeFranquiaByIds(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM FranquiaFornecedorEscolta WHERE idFranquiaFornecedorEscolta IN (:id)", ids);
	}

	public Integer getRowCountBlacklistByIdFornecedorEscolta(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().getRowCountForQuery(makeBlacklistQueryByIdFornecedorEscolta(), criteria);
	}

	@SuppressWarnings("unchecked")
	public List<FornecedorEscoltaImpedido> findBlacklistByIdFornecedorEscolta(TypedFlatMap criteria) {
		return getAdsmHibernateTemplate().findByNamedParam(makeBlacklistQueryByIdFornecedorEscolta(), criteria);
	}

	private String makeBlacklistQueryByIdFornecedorEscolta() {
		StringBuilder hql = new StringBuilder()
				.append("FROM FornecedorEscoltaImpedido fornecedorEscoltaImpedido ")
				.append("JOIN FETCH fornecedorEscoltaImpedido.cliente.pessoa ")
				.append("WHERE fornecedorEscoltaImpedido.fornecedorEscolta.idFornecedorEscolta = :idFornecedorEscolta ");
		return hql.toString();
	}

	public void removeBlacklistByIds(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM FornecedorEscoltaImpedido WHERE idFornecedorEscoltaImpedido IN (:id)", ids);
	}

}
