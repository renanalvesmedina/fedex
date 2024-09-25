package com.mercurio.lms.franqueados.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.franqueados.model.FranqueadoFranquia;

public class FranqueadoFranquiaDAO extends BaseCrudDao<FranqueadoFranquia, Long> {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return FranqueadoFranquia.class;
	}
	

	/**
	 * Busca Franqueados Franquias com com vigência menor a data passada por parâmentro
	 * @param franqueadoId
	 * @param date
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FranqueadoFranquia> findFranqueadoFranquiasByFranqueado(Long franqueadoId,YearMonthDay date) {
		
		StringBuilder hql = new StringBuilder("select ff from ");
		hql.append(getPersistentClass().getName()).append(" as ff ");
		hql.append("inner join fetch ff.franqueado f ");
		hql.append(" where ff.dtVigenciaInicial <= :date ");
		hql.append(" and f.idFranqueado = :franqueadoId ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("date", date);
		parametros.put("franqueadoId", franqueadoId);
		
		return (List<FranqueadoFranquia>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
	}

	
	@SuppressWarnings("unchecked")
	public List<FranqueadoFranquia> findFranqueadoFranquiasVigentes(YearMonthDay date) {
		
		StringBuilder hql = new StringBuilder("select ff from ");
		hql.append(getPersistentClass().getName()).append(" as ff ");
		hql.append("inner join fetch ff.franquia fr ");
		hql.append("inner join fetch fr.filial fil ");
		hql.append("inner join fetch fil.pessoa p ");
		hql.append(" where ff.dtVigenciaInicial <= :date ");
		hql.append(" and ff.dtVigenciaFinal >= :date ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("date", date);
		
		return (List<FranqueadoFranquia>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
	}
	
	@SuppressWarnings("unchecked")
	public List<FranqueadoFranquia> findFranqueadoFranquiasVigentesByFranqueado(Long franqueadoId,YearMonthDay date) {
		
		StringBuilder hql = new StringBuilder("select ff from ");
		hql.append(getPersistentClass().getName()).append(" as ff ");
		hql.append("inner join fetch ff.franqueado f ");
		hql.append(" where ff.dtVigenciaInicial <= :date ");
		hql.append(" and ff.dtVigenciaFinal >= :date ");
		hql.append(" and f.idFranqueado = :franqueadoId ");
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("date", date);
		parametros.put("franqueadoId", franqueadoId);
		
		return (List<FranqueadoFranquia>)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametros);
	}

	@SuppressWarnings("rawtypes")
	public void removeByFranqueado(Long idFranqueado) {
		List franquias = findByFranqueado(idFranqueado);
		
		List<Long> ids = new ArrayList<Long>();
		for (Object frq : franquias) {
			FranqueadoFranquia franquia = (FranqueadoFranquia)frq;
			
			ids.add(franquia.getIdFranqueadoFranquia());
		}
		
		removeByIds(ids);
	}

	@SuppressWarnings("rawtypes")
	public List findByFranqueado(Long idFranqueado) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ff")
				.setFetchMode("franqueado", FetchMode.JOIN)
				.setFetchMode("franquia", FetchMode.JOIN)
				.createAlias("franqueado", "f")
				.add(Restrictions.eq("f.idFranqueado", idFranqueado));
			
		return findByDetachedCriteria(dc);
	}
	
	
	@Override
	public FranqueadoFranquia findById(Long id) {
		StringBuilder query = new StringBuilder();
		query.append("from " + getPersistentClass().getName() + " as ff ");
		query.append("inner join fetch ff.franqueado f ");
		query.append("inner join fetch ff.franquia fr ");
		query.append("inner join fetch fr.filial fi ");
		query.append("where ");
		query.append(" ff.idFranqueadoFranquia = :id ");
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("id", id);
		
		return (FranqueadoFranquia) getAdsmHibernateTemplate().findUniqueResult(query.toString(), criteria);
	}

}
