package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ExcecaoNegativacaoSerasa;

public class ExcecaoNegativacaoSerasaDAO extends BaseCrudDao<ExcecaoNegativacaoSerasa, Long> {

	@Override
	protected final Class getPersistentClass() {
		return ExcecaoNegativacaoSerasa.class;
	}

	public ExcecaoNegativacaoSerasa findExcecaoById(Long id){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ExcecaoNegativacaoSerasa.class.getName(), "e" +
			" JOIN fetch e.usuario u " +
			" JOIN fetch e.fatura f " );
		hql.addCriteria("e.idExcecaoNegativacaoSerasa", "=", id);

		List dados = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (dados != null && dados.size() == 1){
			return (ExcecaoNegativacaoSerasa) dados.get(0);
		} else {
			return null;
		}		
	}

	public List<ExcecaoNegativacaoSerasa> findAll(Map criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(ExcecaoNegativacaoSerasa.class.getName(), "e" +
				" JOIN fetch e.usuario u " +
				" JOIN fetch e.fatura.filialByIdFilial fi " +
				" JOIN fetch e.fatura f " );
		hql = allCriteria(criteria, hql);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	private SqlTemplate allCriteria(Map criteria, SqlTemplate hql){
		if(containValue(criteria, "id_excecao_negativacao"))
			hql.addCriteria("e.idExcecaoNegativacaoSerasa", "=", criteria.get("id_excecao_negativacao"));

		if(containValue(criteria, "id_fatura")) {
			hql.addCriteria("e.fatura.idFatura", "=", criteria.get("id_fatura"));
		}
		
		if(containValue(criteria, "id_filial")) {
			hql.addCriteria("e.fatura.filialByIdFilial.idFilial", "=", criteria.get("id_filial"));
		}
		
		if(containValue(criteria, "dt_vigencia")) { 
			hql.addCriteria("TRUNC(e.dtVigenciaInicial)","<=", criteria.get("dt_vigencia"), YearMonthDay.class);
			hql.addCriteria("TRUNC(e.dtVigenciaFinal)",">=", criteria.get("dt_vigencia"), YearMonthDay.class);
		}

		return hql;
	}
	
	public boolean containValue(Map criteria, String key){
		return criteria.get(key) != null && StringUtils.isNotBlank(criteria.get(key).toString());
	}

}