package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.vendas.model.MunicipioDestinoCalculo;

public class MunicipioDestinoCalculoDAO extends BaseCrudDao<MunicipioDestinoCalculo, Long> {
	
	public Integer getRowCount(Map criteria) {
    	SqlTemplate sql = getHqlPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }
	
	public MunicipioDestinoCalculo findById(Long id) {
		SqlTemplate hql = getHqlFrom();

		hql.addProjection("mdc");
		hql.addCriteria("mdc.idMunicipioDestinoCalculo", "=", id);
		return (MunicipioDestinoCalculo)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}


	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return MunicipioDestinoCalculo.class;
	}


	public ResultSetPage findPaginated(PaginatedQuery paginatedQuery) {
		Map<String, Object> criteria = paginatedQuery.getCriteria();
		SqlTemplate sql = getHqlPaginated(criteria);

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), paginatedQuery.getCurrentPage(), paginatedQuery.getPageSize(), sql.getCriteria());
	}


	public SqlTemplate getHqlPaginated(Map<String,Object> criteria) {
		final String projection = "new Map(" +
			"mdc.idMunicipioDestinoCalculo as idMunicipioDestinoCalculo, " +
			"moo.id as idMunicipioOriginal, " +
			"mdd.id as idMunicipioDestino, " +
			"mdc.dtVigenciaInicial as dtVigenciaInicial, " +
			"mdc.dtVigenciaFinal as dtVigenciaFinal, " +
			"moo.nmMunicipio as nmMunicipioOrigem, " +
			"mdd.nmMunicipio as nmMunicipioDestino, " +
			"moo.blDistrito as blDistritoOrigem, " +
			"mdd.blDistrito as blDistritoDestino, " +
			"po.idPais as idPaisOrigem, " +
			"po.nmPais as nmPaisOrigem, " +
			"pd.idPais as idPaisDestino, " +
			"pd.nmPais as nmPaisDestino, " +
			"fo.sgFilial as sgFilialOrigem, " +
			"fd.sgFilial as sgFilialDestino, " +
			"ufo.sgUnidadeFederativa as sgUFOrigem, " +
			"ufd.sgUnidadeFederativa as sgUFDestino " +
		")";

		SqlTemplate hql = getHqlFrom();
		hql.addProjection(projection);

		if (criteria.get("dtVigenciaFinal") != null) {
			hql.addCustomCriteria("(" + 
				"(mdc.dtVigenciaInicial >= ? OR nvl(mdc.dtVigenciaFinal, ?) >= ?) AND " +
				"(mdc.dtVigenciaInicial <= ? OR nvl(mdc.dtVigenciaFinal, mdc.dtVigenciaFinal) <= ?)" +
			")");
			hql.addCriteriaValue(criteria.get("dtVigenciaInicial"));
			hql.addCriteriaValue(criteria.get("dtVigenciaFinal"));
			hql.addCriteriaValue(criteria.get("dtVigenciaInicial"));
			hql.addCriteriaValue(criteria.get("dtVigenciaFinal"));
			hql.addCriteriaValue(criteria.get("dtVigenciaFinal"));
		} else {
			hql.addCriteria("trunc(mdc.dtVigenciaInicial)", ">=", criteria.get("dtVigenciaInicial"));
		}

		hql.addCriteria("mdc.municipioOriginal.id", "=", criteria.get("idMunicipioOriginal"));
		hql.addCriteria("mdc.municipioDestino.id", "=", criteria.get("idMunicipioDestino"));
		hql.addCriteria("po.idPais", "=", criteria.get("idPaisOrigem"));
		hql.addCriteria("pd.idPais", "=", criteria.get("idPaisDestino"));
		hql.addCriteria("ufo.idUnidadeFederativa", "=", criteria.get("idUnidadeFederativaOrigem"));
		hql.addCriteria("ufd.idUnidadeFederativa", "=", criteria.get("idUnidadeFederativaDestino"));
		hql.addCriteria("fo.idFilial", "=", criteria.get("idFilialOrigem"));
		hql.addCriteria("fd.idFilial", "=", criteria.get("idFilialDestino"));

		if (criteria.get("idMunicipioDestinoCalculo") != null)
			hql.addCriteria("mdc.idMunicipioDestinoCalculo", "=", criteria.get("idMunicipioDestinoCalculo"));

		return hql;
	}


	public List<MunicipioDestinoCalculo> find(Map criteria) {
		SqlTemplate sql = getHqlPaginated(criteria);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}


	public boolean validateMunicipioDestino(Map criteria) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("mdc");
		hql.addFrom(getPersistentClass().getName(), "mdc");
		hql.addCriteria("mdc.idMunicipioDestinoCalculo", "!=", criteria.get("idMunicipioDestinoCalculo"));
		hql.addCriteria("mdc.municipioOriginal.id", "=", criteria.get("idMunicipioOriginal"));
		hql.addCriteria("mdc.municipioDestino.id", "=", criteria.get("idMunicipioDestino"));

		if (criteria.get("dtVigenciaFinal") != null) {
			hql.addCustomCriteria("(" +
				"(TRUNC(mdc.dtVigenciaInicial) >= ? AND (TRUNC(mdc.dtVigenciaFinal) <= ? OR mdc.dtVigenciaFinal IS NULL))" +
				" OR " +
				"(TRUNC(mdc.dtVigenciaInicial) <= ? AND (TRUNC(mdc.dtVigenciaFinal) >= ? OR mdc.dtVigenciaFinal IS NULL))" +
			")");
			hql.addCriteriaValue(criteria.get("dtVigenciaInicial"));
			hql.addCriteriaValue(criteria.get("dtVigenciaFinal"));
			hql.addCriteriaValue(criteria.get("dtVigenciaFinal"));
			hql.addCriteriaValue(criteria.get("dtVigenciaInicial"));
		} else {
			hql.addCustomCriteria("(" +
				"(TRUNC(mdc.dtVigenciaInicial) >= ? OR mdc.dtVigenciaFinal IS NULL)" +
				" OR " +
				"(TRUNC(mdc.dtVigenciaFinal) >= ? OR mdc.dtVigenciaFinal IS NULL)" +
			")");
			hql.addCriteriaValue(criteria.get("dtVigenciaInicial"));
			hql.addCriteriaValue(criteria.get("dtVigenciaInicial"));
		}

		List result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		return result != null && result.size() > 0;
	}


	private SqlTemplate getHqlFrom() {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(getPersistentClass().getName(), "mdc");
		hql.addFrom("Municipio", "moo");
		hql.addFrom("Municipio", "mdd");
		hql.addFrom("UnidadeFederativa", "ufo");
		hql.addFrom("UnidadeFederativa", "ufd");
		hql.addFrom("Pais", "po");
		hql.addFrom("Pais", "pd");
		hql.addFrom("MunicipioFilial", "mfo");
		hql.addFrom("MunicipioFilial", "mfd");
		hql.addFrom("Filial", "fo");
		hql.addFrom("Filial", "fd");

		hql.addCustomCriteria("mdc.municipioOriginal.id = moo.id");
		hql.addCustomCriteria("mdc.municipioDestino.id = mdd.id");
		hql.addCustomCriteria("moo.id = mfo.municipio.id");
		hql.addCustomCriteria("mdd.id = mfd.municipio.id");
		hql.addCustomCriteria("ufo.id = moo.unidadeFederativa.id");
		hql.addCustomCriteria("ufd.id = mdd.unidadeFederativa.id");
		hql.addCustomCriteria("po.id = ufo.pais.id");
		hql.addCustomCriteria("pd.id = ufd.pais.id");
		hql.addCustomCriteria("fo.id = mfo.filial.id");
		hql.addCustomCriteria("fd.id = mfd.filial.id");

		hql.addCustomCriteria("mfo.dtVigenciaInicial < sysdate");
		hql.addCustomCriteria("(mfo.dtVigenciaFinal >= sysdate or mfo.dtVigenciaFinal is null)");
		hql.addCustomCriteria("mfd.dtVigenciaInicial < sysdate");
		hql.addCustomCriteria("(mfd.dtVigenciaFinal >= sysdate or mfd.dtVigenciaFinal is null)");

		return hql;
	}


	public MunicipioDestinoCalculo findDestinoVigenteByOrigem(Long idMunicipioOrigem) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(getPersistentClass().getName(), "mdc");
		hql.addCriteria("mdc.municipioOriginal.id", "=", idMunicipioOrigem);
		hql.addCustomCriteria("mdc.dtVigenciaInicial <= sysdate");
		hql.addCustomCriteria("(mdc.dtVigenciaFinal >= sysdate or mdc.dtVigenciaFinal is null)");

		return (MunicipioDestinoCalculo)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
}
