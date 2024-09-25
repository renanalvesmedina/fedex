package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroBoletoFilial;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParametroBoletoFilialDAO extends BaseCrudDao<ParametroBoletoFilial, Long>{

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 *
	 * @return
	 *
	 */
	protected Class getPersistentClass() {
		return ParametroBoletoFilial.class;
	}

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 *
	 * @param fetchModes
	 *
	 */
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 *
	 * @param fetchModes
	 *
	 */
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}
	
	/**
	 * Busca os parâmetros Boleto filial que estejam vigentes
	 * na data passada por parâmetro, que sejam da filial informada
	 * e blValorLiquido seja 'S'
	 *
	 * @author José Rodrigo Moraes
	 * @since 13/09/2006
	 *
	 * @param idFilial Identificador da filial
	 * @param data Data de vigência
	 * @return Lista de Parâmetros Boleto Filial
	 */
	public List findParametroBoletoFilialVigenteByFilial(Long idFilial, YearMonthDay data, Boolean blValorLiquido){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("pbf");
		hql.addFrom(getPersistentClass().getName(),"pbf");
		hql.addCriteria("pbf.filial.id","=",idFilial);
		hql.addCustomCriteria("? between pbf.dtVigenciaInicial and pbf.dtVigenciaFinal");
		hql.addCriteriaValue(data);
		hql.addCriteria("pbf.blValorLiquido","=",blValorLiquido);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * Retorna um map onde cada filial é uma chave e onde o objeto é uma lista de 
	 * array de YearMonthDay
	 * 
	 * @author Mickaël Jalbert
	 * @since 28/02/2007
	 * 
	 * @return map com idFilial como chave
	 */
	public Map findMapParametroBoletoFilial(){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(pbf.filial.id as id, pbf.dtVigenciaInicial as ini, pbf.dtVigenciaFinal as fim)");
		hql.addFrom(getPersistentClass().getName(),"pbf");

		hql.addCriteria("pbf.blValorLiquido","=",Boolean.TRUE);
		
		List <Map>lst = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		Map mapRetorno = new HashMap();
		Long idFilialAnterior = null;
		
		//Montar um map indexado por idFilial
		for (Map map : lst) {
			//Se é uma nova filial, criar uma nova chave
			if (!((Long)map.get("id")).equals(idFilialAnterior)){
				List <YearMonthDay[]>dtVigencias = new ArrayList();
				dtVigencias.add(new YearMonthDay[]{(YearMonthDay)map.get("ini"), (YearMonthDay)map.get("fim")});
				mapRetorno.put(map.get("id"), dtVigencias);
			//Adicionar as datas de vigencia
			} else {
				List <YearMonthDay[]>dtVigencias = (List)mapRetorno.get(map.get("id"));
				dtVigencias.add(new YearMonthDay[]{(YearMonthDay)map.get("ini"), (YearMonthDay)map.get("fim")});
			}
		}
		
		return mapRetorno;
	}	
	
	/**
	 * Método responsável por buscar ParametroBoletoFilial que tenham a mesma filial 
	 * e que a vigência esteja em conflito com o ParametroBoletoFilial que vem da view
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 *
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @param filial
	 * @return
	 *
	 */
	public List findParametrosBoletoFilialVigenciaConflito(YearMonthDay dtVigenciaInicial
														 , YearMonthDay dtVigenciaFinal
														 , Filial filial
														 , Long idParametroBoletoFilial){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection(" pbf ");
		
		hql.addFrom(getPersistentClass().getName() + " as pbf " +
					" JOIN pbf.filial as fil ");
		
		
		hql.addCustomCriteria("(( ? BETWEEN pbf.dtVigenciaInicial and pbf.dtVigenciaFinal ) " +
						    "OR ( ? BETWEEN pbf.dtVigenciaInicial and pbf.dtVigenciaFinal ) " +
						    "OR ( ? < pbf.dtVigenciaInicial and ? > pbf.dtVigenciaFinal )) ");
		
		hql.addCriteriaValue(dtVigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		hql.addCriteriaValue(dtVigenciaInicial);
		hql.addCriteriaValue(JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		
		hql.addCriteria("fil.idFilial", "=", filial.getIdFilial());
		hql.addCriteria("pbf.idParametroBoletoFilial", "<>", idParametroBoletoFilial);
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	
	/**
	* Retorna os dados para a grid.
	* 
	* @author Diego Umpierre
	* @since 26/09/2006
	* 
	* @param TypedFlatMap criteria, FindDefinition findDef
	* @return ResultSetPage
	* */
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = mountHql(criteria);
		
		
		
		sql.addProjection("new Map( (filial.sgFilial || ' - ' || pessoa.nmFantasia) as siglaNomeFilial,"+
						  "pbf.dtVigenciaInicial as dtVigenciaInicial,"+
						  "pbf.dtVigenciaFinal as dtVigenciaFinal,"+
						  "pbf.blValorLiquido as blValorLiquido,"+
						  "pbf.idParametroBoletoFilial as idParametroBoletoFilial," +
						  "pbf.blWorkflowCancelamento as blWorkflowCancelamento)");
		
		sql.addOrderBy("filial.sgFilial,pessoa.nmPessoa,pbf.dtVigenciaInicial");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		
		return rsp;
	}

	/**
	* Retorna o numero de linhas da grid
	* 
	* @author Diego Umpierre
	* @since 26/09/2006
	* 
	* @param TypedFlatMap criteria
	* @return Integer
	* */	
	public Integer getRowCountTela(TypedFlatMap criteria) {
		SqlTemplate sql = mountHql(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}
	
	
	private SqlTemplate mountHql(TypedFlatMap map){
		SqlTemplate sql = new SqlTemplate();
	
		sql.addInnerJoin(getPersistentClass().getName() + " pbf ");
		sql.addInnerJoin(" pbf.filial","filial");
		sql.addInnerJoin(" filial.pessoa","pessoa");
		
		sql.addCriteria("pbf.dtVigenciaInicial","<=",map.getYearMonthDay("dtVigencia"));
		sql.addCriteria("pbf.dtVigenciaFinal",">=",map.getYearMonthDay("dtVigencia"));
		sql.addCriteria("filial.idFilial","=",map.getLong("filial.idFilial"));
		
		return sql;
	}
	
	public void evict(ParametroBoletoFilial pbf){
		getSession().evict(pbf);
	}
}
