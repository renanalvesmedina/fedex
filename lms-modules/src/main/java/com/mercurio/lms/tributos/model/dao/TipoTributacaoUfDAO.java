package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.tributos.model.TipoTributacaoUf;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class TipoTributacaoUfDAO extends BaseCrudDao<TipoTributacaoUf, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return TipoTributacaoUf.class;
    }
    
    /**
     * Método que inicializa os relacionamentos do pojo
     */
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("unidadeFederativa", FetchMode.JOIN);
		lazyFindById.put("tipoTributacaoIcms", FetchMode.JOIN);
	} 

	
	/**
	* Retorna os dados para a grid.
	* 
	* @author Diego Umpierre
	* @since 24/08/2006
	* 
	* @param TypedFlatMap criteria, FindDefinition findDef
	* @return ResultSetPage
	* */
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = mountHql(criteria);
		
		sql.addProjection("ttuf");
		sql.addOrderBy("uf.sgUnidadeFederativa");
		sql.addOrderBy("ttuf.dtVigenciaInicial");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		
		return rsp;
	}


	
	/**
	* Retorna o numero de linhas da grid
	* 
	* @author Diego Umpierre
	* @since 24/08/2006
	* 
	* @param TypedFlatMap criteria
	* @return Integer
	* */	
	public Integer getRowCountTela(TypedFlatMap criteria) {
		SqlTemplate sql = mountHql(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}	
	
	
	
	
	/**
	* Monta o HQL para ser usado na consulta do findPaginated e rowCount.
	* 
	* @author Diego Umpierre
	* @since 24/08/2006
	* 
	* @param Map criteria
	* @return SqlTemplate
	* */
	private SqlTemplate mountHql(TypedFlatMap map){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addInnerJoin(getPersistentClass().getName() + " ttuf ");
		sql.addInnerJoin(" fetch ttuf.unidadeFederativa","uf");
		sql.addInnerJoin(" fetch ttuf.tipoTributacaoIcms","tti");
				
		
		sql.addCriteria("uf.idUnidadeFederativa","=",map.getLong("unidadeFederativa.idUnidadeFederativa"));
		sql.addCriteria("tti.idTipoTributacaoIcms","=",map.getLong("tipoTributacaoIcms.idTipoTributacaoIcms"));
		
		sql.addCriteria("ttuf.dtVigenciaInicial","<=",map.getYearMonthDay("dtVigencia"));
		sql.addCriteria("ttuf.dtVigenciaFinal",">=",map.getYearMonthDay("dtVigencia"));

		return sql;
	}
	
	
	/**
	 * Método responsável por buscar TipoTributacaoUf que estejam no mesmo intervalo de vigência
	 * 
	 *@author Diego Umpierre
	 *@since 24/08/2006
	 * 
	 * @param vigenciaInicial
	 * @param vigenciaFinal
	 * @return List <TipoTributacaoUf>
	 */
	public List findExcecaoByVigenciaEquals(
					  YearMonthDay vigenciaInicial
					, YearMonthDay vigenciaFinal
					, Long idUnidadeFederativa,
					  Long idTipoTributacaoUf , String tpFrete, String tpAbrangencia){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("ttuf");

		hql.addInnerJoin(getPersistentClass().getName(), "ttuf");
		hql.addInnerJoin("fetch ttuf.tipoTributacaoIcms", "tti");

		
		JTVigenciaUtils.getHqlVigenciaNotNull(hql, "ttuf.dtVigenciaInicial", "ttuf.dtVigenciaFinal", vigenciaInicial, vigenciaFinal);
		
		hql.addCriteria("ttuf.unidadeFederativa.idUnidadeFederativa", "=", idUnidadeFederativa);
		
		hql.addCriteria("ttuf.idTipoTributacaoUf", "<>", idTipoTributacaoUf);
		
		if(StringUtils.isNotBlank(tpFrete)){
			hql.addCriteria("ttuf.tpTipoFrete", "=", tpFrete);
		}
	
		if(StringUtils.isNotBlank(tpAbrangencia)){
			hql.addCriteria("ttuf.tpAbrangenciaUf", "=", tpAbrangencia);
		}	
		
		List lst = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		return lst;
	} 	
	
	/**
	 * Busca TipoTributação pala Situação Tributaria
	 * @author André Valadas
	 * @since 04/05/2009
	 * 
	 * @param tpTipoFrete
	 * @param idIEResponsavel
	 * @param idUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param dtVigenciaInicial
	 * @return
	 */
	public TipoTributacaoUf findTipoTributacao(
			String tpTipoFrete,
			Long idIEResponsavel,
			String tpSituacaoTributariaResponsavel,
			Long idUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino,
			YearMonthDay dtVigenciaInicial) {
	
		//*** Situações Tributarias válidas
		List<String> situacoesTrib = new ArrayList<String>();
		situacoesTrib.add("CO");
		situacoesTrib.add("CI");
		situacoesTrib.add("CM");
		situacoesTrib.add("ME");
		situacoesTrib.add("OP");
		situacoesTrib.add("PR");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ttu");
		dc.createAlias("ttu.tipoTributacaoIcms", "tti");
		dc.add(Restrictions.eq("ttu.unidadeFederativa.id", idUnidadeFederativaOrigem));
		dc.add(Restrictions.or(
				Restrictions.isNull("ttu.tpTipoFrete"),
				Restrictions.eq("ttu.tpTipoFrete", tpTipoFrete)));

		dc.add(Restrictions.or(
				Restrictions.isNull("ttu.tpAbrangenciaUf"), 
				Restrictions.or(
						Restrictions.and(Restrictions.eq("ttu.tpAbrangenciaUf", "I"), Restrictions.eq("ttu.unidadeFederativa.id", idUnidadeFederativaDestino)),
						Restrictions.and(Restrictions.eq("ttu.tpAbrangenciaUf", "N"), Restrictions.ne("ttu.unidadeFederativa.id", idUnidadeFederativaDestino)))));

		JTVigenciaUtils.setDetachedVigencia(dc, "ttu.dtVigenciaInicial", "ttu.dtVigenciaFinal", dtVigenciaInicial, JTDateTimeUtils.MAX_YEARMONTHDAY);

		TipoTributacaoUf ttuf = (TipoTributacaoUf) getAdsmHibernateTemplate().findUniqueResult(dc);

		if(ttuf != null && ttuf.getBlContribuinte() != null && ttuf.getBlContribuinte()){


		Integer rowCount = IntegerUtils.ZERO;
		if(LongUtils.hasValue(idIEResponsavel)) {
			DetachedCriteria dcTributacaoIE = DetachedCriteria.forClass(TipoTributacaoIE.class, "ttie")
				.setProjection(Projections.rowCount());
			dcTributacaoIE.add(Restrictions.eq("ttie.inscricaoEstadual.id", idIEResponsavel));
			dcTributacaoIE.add(Restrictions.in("ttie.tpSituacaoTributaria", situacoesTrib.toArray()));
			JTVigenciaUtils.setDetachedVigencia(dcTributacaoIE, "ttie.dtVigenciaInicial", "ttie.dtVigenciaFinal", dtVigenciaInicial, JTDateTimeUtils.MAX_YEARMONTHDAY);
			rowCount = getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dcTributacaoIE);
		} else if (situacoesTrib.contains(tpSituacaoTributariaResponsavel)) {
			rowCount = IntegerUtils.ONE;
		}
		//*** Se não validou Tributação IE, retorna nulo
		if(!IntegerUtils.hasValue(rowCount)) {
			return null;
		}


			dc.add(Restrictions.and(
						Restrictions.eq("ttu.blContribuinte", Boolean.TRUE),
						//Deve retornar algum registro da consulta acima
					Restrictions.sqlRestriction(IntegerUtils.defaultInteger(rowCount)+" > 0")));			

		return (TipoTributacaoUf) getAdsmHibernateTemplate().findUniqueResult(dc);

	} 

		return ttuf;

	} 

    /**
     * Retorna o tipo de tributação UF vigente da UF informada.
     * 
     * @author Mickaël Jalbert
     * @since 11/09/2006
     * 
     * @param Long idUF
     * @param YearMonthDay dtVigencia
     * @return TipoTributacaoUf 
     */
    public TipoTributacaoUf findByIdUFVigente(Long idUF, YearMonthDay dtVigencia){
    	List lstTipoTributacaoUf = findExcecaoByVigenciaEquals(dtVigencia, null, idUF, null, null, null);
    	
    	if (!lstTipoTributacaoUf.isEmpty()){
    		return (TipoTributacaoUf) lstTipoTributacaoUf.get(0);
    	} else {
    		return null;
    	}
    }  
}