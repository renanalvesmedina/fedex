package com.mercurio.lms.prestcontasciaaerea.model.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.prestcontasciaaerea.model.FaturamentoCiaAerea;
/**
* @spring.bean 
*/
public class FaturamentoCiaAereaDAO extends BaseCrudDao<FaturamentoCiaAerea, Long>{
	
	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("ciaFilialMercurio", FetchMode.JOIN);
		lazyFindList.put("ciaFilialMercurio.empresa", FetchMode.JOIN);
		lazyFindList.put("ciaFilialMercurio.empresa.pessoa", FetchMode.JOIN);
		lazyFindList.put("ciaFilialMercurio.filial", FetchMode.JOIN);
		lazyFindList.put("ciaFilialMercurio.filial.pessoa", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("ciaFilialMercurio", FetchMode.JOIN);
		lazyFindById.put("ciaFilialMercurio.empresa", FetchMode.JOIN);
		lazyFindById.put("ciaFilialMercurio.empresa.pessoa", FetchMode.JOIN);
		lazyFindById.put("ciaFilialMercurio.filial", FetchMode.JOIN);
		lazyFindById.put("ciaFilialMercurio.filial.pessoa", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	@Override
	protected Class getPersistentClass() {
		return FaturamentoCiaAerea.class;
	}
	
	/**
	 *  carrega faturamento cia aerea Vigente
	 * 
	 * @param idCiaAerea
	 * @param idFilial
	 * @return
	 */
	public FaturamentoCiaAerea findFaturamentoCiaAereaVigente(Long idEmpresaCiaAerea, Long idFilial) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(getPersistentClass().getName() , "faturamento");
   		hql.addCriteria("faturamento.ciaFilialMercurio.empresa.id", "=", idEmpresaCiaAerea);
   		hql.addCriteria("faturamento.ciaFilialMercurio.filial.id", "=", idFilial);
   		
   		hql.addCustomCriteria("faturamento.dtVigenciaInicial <= sysdate ");
   		hql.addCustomCriteria("faturamento.dtVigenciaFinal  >= sysdate ");
		
   		return (FaturamentoCiaAerea)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 *  carrega faturamento cia aerea Vigente por data vigencia
	 * 
	 * @param idCiaAerea
	 * @param idFilial
	 * @return
	 */
	public FaturamentoCiaAerea findFaturamentoCiaAereaVigente(Long idEmpresaCiaAerea, Long idFilial, YearMonthDay vigencia) {
		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(getPersistentClass().getName() , "faturamento");
   		hql.addCriteria("faturamento.ciaFilialMercurio.empresa.id", "=", idEmpresaCiaAerea);
   		hql.addCriteria("faturamento.ciaFilialMercurio.filial.id", "=", idFilial);
   		
   		hql.addCriteria("faturamento.dtVigenciaInicial", "<=", vigencia );
   		hql.addCriteria("faturamento.dtVigenciaFinal",  ">=", vigencia );
		
   		return (FaturamentoCiaAerea)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		TypedFlatMap map = new TypedFlatMap();
		map.putAll(criteria);
		
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(getPersistentClass().getName(), "f");
		sql.addInnerJoin("fetch f.ciaFilialMercurio","ciaFilial");
		sql.addInnerJoin("fetch f.ciaFilialMercurio.empresa", "ciaFiliale");
		sql.addInnerJoin("fetch f.ciaFilialMercurio.empresa.pessoa", "ciaFilialp");
		
		sql.addInnerJoin("fetch f.ciaFilialMercurio.filial","filial");
		sql.addInnerJoin("fetch f.ciaFilialMercurio.filial.pessoa", "filialp");
		
		sql.addCriteria("f.tpPeriodicidade","=",map.getString("tpPeriodicidade"));
		map.getLong("ciaFilialMercurio.idCiaFilialMercurio");
		if (((HashMap) map.get("ciaFilialMercurio")).get("idCiaFilialMercurio") != null && 
		 !("".equals((String) ( (HashMap) map.get("ciaFilialMercurio")).get("idCiaFilialMercurio")))){
			sql.addCriteria("ciaFilial.empresa.id","=", Long.valueOf( (String) ((HashMap) map.get("ciaFilialMercurio")).get("idCiaFilialMercurio")));
		}		

		if (((HashMap) map.get("filial")).get("idFilial") != null && 
		 !("".equals((String) ( (HashMap) map.get("filial")).get("idFilial")))){
			sql.addCriteria("f.ciaFilialMercurio.filial.id","=", Long.valueOf( (String) ((HashMap) map.get("filial")).get("idFilial")));
		}
		
		sql.addOrderBy("ciaFilial.empresa.pessoa.nmPessoa", "asc");
		sql.addOrderBy("filial.sgFilial", "asc");

		
		return getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		
	}
	
	@Override
	public Integer getRowCount(Map criteria) {
		return super.getRowCount(criteria);
	}

	/**
	 * metodo utilizado em validação 
	 * retorna se existe faturamento no periodo informado
	 * 
	 * @param idCiaFilialMercurio cia filial mercurio
	 * @param dtVigenciaInicial Data de vigencia inicial
	 * @param dtVigenciaFinal data de vigencia final (opcional)
	 * @param idfaturamentociafililalmercurioToIgnore id de faturamento cia filial mercurio a ser ignorado (opcional)
	 * @return true se existe faturamento para a cia filial mercurio no periodo
	 */
	public boolean hasFaturamentoInPeriod(Long idCiaFilialMercurio,
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, Long idfaturamentociafililalmercurioToIgnore) {

		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(getPersistentClass().getName() , "faturamento");
   		hql.addCriteria("faturamento.ciaFilialMercurio.id", "=", idCiaFilialMercurio);
   		
   		hql.addCriteria("faturamento.id", "!=", idfaturamentociafililalmercurioToIgnore);
   		
   		if (dtVigenciaFinal == null){
   			hql.addCustomCriteria("((faturamento.dtVigenciaInicial <= ? and faturamento.dtVigenciaFinal >= ?) or (faturamento.dtVigenciaInicial >= ? ))");
   			hql.addCriteriaValue(dtVigenciaInicial);
   			hql.addCriteriaValue(dtVigenciaInicial);
   			hql.addCriteriaValue(dtVigenciaInicial);
   		} else {
   			hql.addCustomCriteria("((faturamento.dtVigenciaInicial <= ? and faturamento.dtVigenciaFinal >= ?) or (faturamento.dtVigenciaInicial >= ? and faturamento.dtVigenciaInicial <= ?))");
   			hql.addCriteriaValue(dtVigenciaInicial);
   			hql.addCriteriaValue(dtVigenciaInicial);
   			
   			hql.addCriteriaValue(dtVigenciaInicial);
   			hql.addCriteriaValue(dtVigenciaFinal);
   		}
   		
		return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
	}
}
