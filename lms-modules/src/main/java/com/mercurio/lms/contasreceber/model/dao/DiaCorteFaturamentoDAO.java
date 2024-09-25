package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.DiaCorteFaturamento;


public class DiaCorteFaturamentoDAO extends BaseCrudDao<DiaCorteFaturamento, Long> {

	@Override
	protected final Class getPersistentClass() {
		return DiaCorteFaturamento.class;
	}

	public DiaCorteFaturamento findDiaCorteById(Long id){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(DiaCorteFaturamento.class.getName(), "d" +
			" JOIN fetch d.usuario u " );
		hql.addCriteria("d.idDiaCorteFaturamento", "=", id);
		hql.addOrderBy(" d.dtCorte");

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (dados != null && dados.size() == 1){
			return (DiaCorteFaturamento)dados.get(0);
		} else {
			return null;
		}		
	}

	public List<DiaCorteFaturamento> findAll(Map criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(DiaCorteFaturamento.class.getName(), "d" +
				" JOIN fetch d.usuario u ");
		hql = allCriteria(criteria, hql);
		hql.addOrderBy(" d.dtCorte");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	private SqlTemplate allCriteria(Map criteria, SqlTemplate hql){
		if(containValue(criteria, "id_dia_corte_faturamento"))
			hql.addCriteria("d.idDiaCorteFaturamento", "=", criteria.get("id_dia_corte_faturamento"));

		if(containValue(criteria, "dt_corte_inicial"))
			hql.addCriteria("TRUNC(d.dtCorte)",">=", criteria.get("dt_corte_inicial"), YearMonthDay.class);

		if(containValue(criteria, "dt_corte_final"))
			hql.addCriteria("TRUNC(d.dtCorte)","<=", criteria.get("dt_corte_final"), YearMonthDay.class);
		
		if(containValue(criteria, "dt_alteracao_inicial"))
			hql.addCriteria("TRUNC(d.dhAlteracao.value)",">=", criteria.get("dt_alteracao_inicial"), YearMonthDay.class);

		if(containValue(criteria, "dt_alteracao_final"))
			hql.addCriteria("TRUNC(d.dhAlteracao.value)","<=", criteria.get("dt_alteracao_final"), YearMonthDay.class);

		return hql;
	}
	
	public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}

}