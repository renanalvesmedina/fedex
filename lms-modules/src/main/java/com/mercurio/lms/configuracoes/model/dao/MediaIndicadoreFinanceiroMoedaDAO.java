/*
 * Created on Sep 26, 2005
 *
 */
package com.mercurio.lms.configuracoes.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;

/**
 * @author Mickaël Jalbert
 *@spring.bean 
 */
public class MediaIndicadoreFinanceiroMoedaDAO extends AdsmDao {
	
	public Map<String, List> findMensal(Map criteria) {
		SqlTemplate sql = null;
		SqlTemplate sql2 = null;
		Map<String, List> listsMap = null;
		try {
			if (StringUtils.isNotBlank((String)criteria.get("idMoeda"))){
				sql = getHQLMoedaMensal(criteria, false);
				sql2 = getHQLMoedaMensal(criteria, true);
			} else if (StringUtils.isNotBlank((String)criteria.get("idIndicadorFinanceiro"))) {
				sql = getHQLIndicadorFinanceiroMensal(criteria,false);
				sql2 = getHQLIndicadorFinanceiroMensal(criteria, true);
			}
			listsMap = new HashMap<String, List>();
			List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
			List list2 = getAdsmHibernateTemplate().find(sql2.getSql(), sql2.getCriteria());
			listsMap.put("list1", list);
			listsMap.put("list2", list2);
		} catch (Exception e) {
			throw new InfrastructureException(e);
		}
		return listsMap;
	}
	

	public ResultSetPage findDiario(Map criteria) {	
		SqlTemplate sql = null;	
		ResultSetPage rsp = null;
		try {
			if (StringUtils.isNotBlank((String)criteria.get("idMoeda"))){
				sql = getHQLMoedaDiario(criteria);			
			} else if (StringUtils.isNotBlank((String)criteria.get("idIndicadorFinanceiro"))) {
				sql = getHQLIndicadorFinanceiroDiario(criteria);			
			}
			List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());		
			rsp = new ResultSetPage(Integer.valueOf(1), list);
		} catch (Exception e) {
			throw new InfrastructureException(e);			
		}
		return rsp;
	}

	/**
	 * HQL Utilizada para consulta de Media de Cotacao mensal
	 * @return
	 */
	private SqlTemplate getHQLMoedaMensal(Map criteria, boolean aoAnterior) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new map(month(cm.dtCotacaoMoeda) as mes, year(cm.dtCotacaoMoeda) as ano, avg(cm.vlCotacaoMoeda) as media, count(cm.dtCotacaoMoeda) as nrDia)");
		sql.addFrom("com.mercurio.lms.configuracoes.model.CotacaoMoeda cm join cm.moedaPais as mp join mp.pais as p join mp.moeda as m");

		sql.addCriteria("p.idPais","=", Long.valueOf(criteria.get("idPais").toString()));					
		sql.addCriteria("m.idMoeda","=", Long.valueOf(criteria.get("idMoeda").toString()));

		if (aoAnterior){
			sql.addCriteria("year(cm.dtCotacaoMoeda)","=", Integer.valueOf(criteria.get("ano").toString()) - 1);
			sql.addCriteria("month(cm.dtCotacaoMoeda)","=","12", Integer.class);				
		} else {
			sql.addCriteria("year(cm.dtCotacaoMoeda)","=", Integer.valueOf(criteria.get("ano").toString()));			
		}		
		sql.addGroupBy("year(cm.dtCotacaoMoeda)");
		sql.addGroupBy("month(cm.dtCotacaoMoeda)");
		sql.addOrderBy("year(cm.dtCotacaoMoeda)");		
		sql.addOrderBy("month(cm.dtCotacaoMoeda)");			
		return sql;
	}

	/**
	 * HQL Utilizada para consulta de Media de Indicadore Financeiro mensal
	 * @return
	 */
	private SqlTemplate getHQLIndicadorFinanceiroMensal(Map criteria, boolean aoAnterior) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new map(month(cif.dtCotacaoIndFinanceiro) as mes, year(cif.dtCotacaoIndFinanceiro) as ano, avg(cif.vlCotacaoIndFinanceiro) as media, count(cif.dtCotacaoIndFinanceiro) as nrDia)");
		sql.addFrom("com.mercurio.lms.configuracoes.model.CotacaoIndicadorFinanceiro cif join cif.indicadorFinanceiro as idf join idf.pais as p");

		sql.addCriteria("p.idPais","=", Long.valueOf(criteria.get("idPais").toString()));					
		sql.addCriteria("idf.idIndicadorFinanceiro","=", Long.valueOf(criteria.get("idIndicadorFinanceiro").toString()));

		if (aoAnterior){
			sql.addCriteria("year(cif.dtCotacaoIndFinanceiro)","=", Integer.valueOf(criteria.get("ano").toString()) - 1);
			sql.addCriteria("month(cif.dtCotacaoIndFinanceiro)","=","12", Integer.class);				
		} else {
			sql.addCriteria("year(cif.dtCotacaoIndFinanceiro)","=", Integer.valueOf(criteria.get("ano").toString()));			
		}		
		sql.addGroupBy("year(cif.dtCotacaoIndFinanceiro)");
		sql.addGroupBy("month(cif.dtCotacaoIndFinanceiro)");
		sql.addOrderBy("year(cif.dtCotacaoIndFinanceiro)");		
		sql.addOrderBy("month(cif.dtCotacaoIndFinanceiro)");			
		return sql;
	}	
	
	/**
	 * HQL Utilizada para consulta de Media de Indicador Financeiro diario
	 * @return
	 */
	private SqlTemplate getHQLMoedaDiario(Map criteria) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new map(cm.idCotacaoMoeda as id, day(cm.dtCotacaoMoeda) as day, cm.vlCotacaoMoeda as media)");
		sql.addFrom("com.mercurio.lms.configuracoes.model.CotacaoMoeda cm join cm.moedaPais as mp join mp.pais as p join mp.moeda as m");

		sql.addCriteria("p.idPais","=",(String)criteria.get("idPais"), Long.class);					
		sql.addCriteria("m.idMoeda","=",(String)criteria.get("idMoeda"), Long.class);
		sql.addCriteria("month(cm.dtCotacaoMoeda)","=",(String)criteria.get("mes"), Integer.class);		
		sql.addCriteria("year(cm.dtCotacaoMoeda)","=",(String)criteria.get("ano"), Integer.class);			

		sql.addOrderBy("day(cm.dtCotacaoMoeda)");				
		return sql;
	}

	/**
	 * HQL Utilizada para consulta de Media de Indicador Financeiro diario
	 * @return
	 */
	private SqlTemplate getHQLIndicadorFinanceiroDiario(Map criteria) throws Exception {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new map(cif.idCotacaoIndFinanceiro as id, day(cif.dtCotacaoIndFinanceiro) as day, cif.vlCotacaoIndFinanceiro as media)");
		sql.addFrom("com.mercurio.lms.configuracoes.model.CotacaoIndicadorFinanceiro cif join cif.indicadorFinanceiro as idf join idf.pais as p");

		sql.addCriteria("p.idPais","=",(String)criteria.get("idPais"), Long.class);					
		sql.addCriteria("idf.idIndicadorFinanceiro","=",(String)criteria.get("idIndicadorFinanceiro"), Long.class);
		sql.addCriteria("month(cif.dtCotacaoIndFinanceiro)","=",(String)criteria.get("mes"), Integer.class);		
		sql.addCriteria("year(cif.dtCotacaoIndFinanceiro)","=",(String)criteria.get("ano"), Integer.class);			

		sql.addOrderBy("day(cif.dtCotacaoIndFinanceiro)");				
		return sql;
	}

}
