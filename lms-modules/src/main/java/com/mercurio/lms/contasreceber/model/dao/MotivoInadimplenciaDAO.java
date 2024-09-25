package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.MotivoInadimplencia;

public class MotivoInadimplenciaDAO extends BaseCrudDao<MotivoInadimplencia, Long> {

	@Override
	protected final Class getPersistentClass() {
		return MotivoInadimplencia.class;
	}

	public MotivoInadimplencia findMotivoInadimplenciaById(Long id){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(MotivoInadimplencia.class.getName(), "m");
		hql.addCriteria("m.idMotivoInadimplencia", "=", id);

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (dados != null && dados.size() == 1){
			return (MotivoInadimplencia) dados.get(0);
		} else {
			return null;
		}		
	}
	
	public List<MotivoInadimplencia> findMotivoInadimplenciaByTratativaId(Long idFatura){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(MotivoInadimplencia.class.getName(), "m");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public List<MotivoInadimplencia> findAll(Map criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(MotivoInadimplencia.class.getName(), "m");
		hql = allCriteria(criteria, hql);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	private SqlTemplate allCriteria(Map criteria, SqlTemplate hql){
		if(containValue(criteria, "id_motivo_inadimplencia"))
			hql.addCriteria("m.idMotivoInadimplencia", "=", criteria.get("id_motivo_inadimplencia"));

		if(containValue(criteria, "tp_situacao")) {
			hql.addCriteria("m.tpSituacao", "=", criteria.get("tp_situacao"));
		}
		return hql;
	}
	
	public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}

}