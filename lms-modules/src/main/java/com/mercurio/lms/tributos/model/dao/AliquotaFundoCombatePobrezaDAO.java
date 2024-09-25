package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.AliquotaFundoCombatePobreza;
import com.mercurio.lms.util.JTDateTimeUtils;

public class AliquotaFundoCombatePobrezaDAO extends BaseCrudDao<AliquotaFundoCombatePobreza, Long> {
	
	protected final Class<AliquotaFundoCombatePobreza> getPersistentClass() {
		return AliquotaFundoCombatePobreza.class;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativa", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage<AliquotaFundoCombatePobreza> findPaginated(TypedFlatMap criteria) {
		List param = new ArrayList();
		StringBuilder hql = getHqlFindPaginated(criteria, param);
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), criteria.getInteger("_currentPage"), criteria.getInteger("_pageSize"), param.toArray());
	}

	@SuppressWarnings("rawtypes")
	public Integer getRowCount(TypedFlatMap criteria) {
		List param = new ArrayList();
		StringBuilder hql = getHqlFindPaginated(criteria, param);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), param.toArray());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private StringBuilder getHqlFindPaginated(TypedFlatMap criteria, List param) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT afcp FROM ");
		query.append(AliquotaFundoCombatePobreza.class.getName());
		query.append(" afcp ");
		query.append("INNER JOIN FETCH afcp.unidadeFederativa uf ");
		query.append("WHERE 1=1 ");

		if(criteria.getLong("idUnidadeFederativa") != null){
			query.append("AND uf.idUnidadeFederativa = ? ");
			param.add(criteria.getLong("idUnidadeFederativa"));
		}
		
		if (criteria.getBigDecimal("pcAliquota") != null) {
			query.append("AND afcp.pcAliquota = ? ");
			param.add(criteria.getBigDecimal("pcAliquota"));
		}

		if (criteria.getYearMonthDay("dtVigencia") != null) {
			query.append("AND TRUNC(afcp.dtVigenciaInicial) <= ? ");
			param.add(criteria.getYearMonthDay("dtVigencia"));

			query.append("AND TRUNC(afcp.dtVigenciaFinal) >= ? ");
			param.add(criteria.getYearMonthDay("dtVigencia"));
		}
		
		query.append("ORDER BY afcp.unidadeFederativa.sgUnidadeFederativa, afcp.dtVigenciaFinal");

		return query;
	}
	
	@SuppressWarnings("rawtypes")
	public Boolean validateAliquotaFundoCombatePobrezaVigente(AliquotaFundoCombatePobreza aliquotaFundoCombatePobreza) {
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		hql.append("SELECT COUNT(*) FROM ");
		hql.append(AliquotaFundoCombatePobreza.class.getName());
		hql.append(" afcp ");
		hql.append("INNER JOIN afcp.unidadeFederativa uf ");
		hql.append("WHERE ");
		
		hql.append("uf.idUnidadeFederativa = ? ");
		params.add(aliquotaFundoCombatePobreza.getUnidadeFederativa().getIdUnidadeFederativa());

		if (aliquotaFundoCombatePobreza.getIdAliquotaFundoCombatePobreza() != null) {
			hql.append("and afcp.id != ? ");
			params.add(aliquotaFundoCombatePobreza.getIdAliquotaFundoCombatePobreza());
		}

		if (aliquotaFundoCombatePobreza.getDtVigenciaFinal() != null) {
			params.add(aliquotaFundoCombatePobreza.getDtVigenciaInicial());
			params.add(aliquotaFundoCombatePobreza.getDtVigenciaFinal());
			hql.append("AND NOT(afcp.dtVigenciaFinal < ? OR afcp.dtVigenciaInicial > ?) ");
			
		} else {
			params.add(aliquotaFundoCombatePobreza.getDtVigenciaInicial());
			hql.append("AND afcp.dtVigenciaFinal >= ?  ");
		}
			
		List result = this.getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
		return ((Long) result.get(0)).intValue() > 0;
	}
	
	public AliquotaFundoCombatePobreza findAliquotaFundoCombatePobrezaVigenteByUf(Long idUf) {
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		hql.append("SELECT afcp FROM ");
		hql.append(AliquotaFundoCombatePobreza.class.getName());
		hql.append(" afcp ");
		hql.append("INNER JOIN afcp.unidadeFederativa uf ");
		hql.append("WHERE ");

		hql.append("uf.idUnidadeFederativa = ? ");
		params.add(idUf);
		
		hql.append("AND TRUNC(afcp.dtVigenciaInicial) <= ? ");
		params.add(JTDateTimeUtils.getDataAtual());

		hql.append("AND TRUNC(afcp.dtVigenciaFinal) >= ? ");
		params.add(JTDateTimeUtils.getDataAtual());
		
		return (AliquotaFundoCombatePobreza) this.getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
	}
}