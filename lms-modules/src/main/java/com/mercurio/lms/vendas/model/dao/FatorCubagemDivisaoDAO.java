package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.FatorCubagemDivisao;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FatorCubagemDivisaoDAO extends BaseCrudDao<FatorCubagemDivisao, Long> {

    @Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("divisaoCliente", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente.cliente", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return FatorCubagemDivisao.class;
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		
		SqlTemplate sql = getSqlTemplateFindPaginated(criteria);
		sql.addProjection("fcd");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
		
        return rsp;
	}

    public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = getSqlTemplateFindPaginated(criteria);
	
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate getSqlTemplateFindPaginated(TypedFlatMap criteria) {
		StringBuilder from = new StringBuilder();
		from.append(FatorCubagemDivisao.class.getName()).append(" fcd ");
		from.append("left join fetch fcd.divisaoCliente").append(" divisao ");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());
		
		// criterias, campos da tela de listagem
		
		
		sql.addCriteria("divisao.id", "=", criteria.getLong("divisaoCliente.idDivisaoCliente"));
		
        YearMonthDay dtVigenciaInicial = criteria.getYearMonthDay("dtVigenciaInicial");
        YearMonthDay dtVigenciaFinal = criteria.getYearMonthDay("dtVigenciaFinal");
        if (dtVigenciaInicial != null) {
            sql.addCriteria("fcd.dtVigenciaInicial", ">=", dtVigenciaInicial, YearMonthDay.class);
        }
        if (dtVigenciaFinal != null) {
            sql.addCriteria("fcd.dtVigenciaFinal", "<=", dtVigenciaFinal, YearMonthDay.class);
        }
		sql.addOrderBy("fcd.dtVigenciaInicial,fcd.dtVigenciaFinal");
		
		return sql;
		
	}
	
	public FatorCubagemDivisao findFatorCubagemDivisaoVigenteMaxima(Long idDivisaoCliente) {
		
		StringBuilder from = new StringBuilder();
		from.append(FatorCubagemDivisao.class.getName()).append(" as fcd ");
		from.append("left join fetch fcd.divisaoCliente").append(" as divisao ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());

		// criterias, campos da tela de listagem
		sql.addCriteria("divisao.id", "=", idDivisaoCliente, Long.class);
		sql.addCriteria("fcd.dtVigenciaFinal", "=", JTDateTimeUtils.MAX_YEARMONTHDAY, YearMonthDay.class);
		
		return (FatorCubagemDivisao) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}
	
	public FatorCubagemDivisao findFatorCubagemVigenteByIdDivisaoCliente(Long idDivisaoCliente) {
		return findFatorCubagemVigenteByIdDivisaoCliente(idDivisaoCliente, null, JTDateTimeUtils.getDataAtual());
	}

    public FatorCubagemDivisao findFatorCubagemVigenteByIdDivisaoCliente(Long idDivisaoCliente, Long idFatorCubagemDivisao, YearMonthDay data) {
        StringBuilder from = new StringBuilder();
        from.append(FatorCubagemDivisao.class.getName()).append(" as fcd ");
        from.append("left join fetch fcd.divisaoCliente").append(" as divisao ");
        
        SqlTemplate sql = new SqlTemplate();
        sql.addFrom(from.toString());
        
        // criterias, campos da tela de listagem
        sql.addCriteria("divisao.id", "=", idDivisaoCliente, Long.class);
        sql.addCriteria("fcd.dtVigenciaInicial", "<=", data, YearMonthDay.class);
        sql.addCriteria("fcd.dtVigenciaFinal", ">=", data, YearMonthDay.class);
        if (idFatorCubagemDivisao != null) {
            sql.addCriteria("fcd.idFatorCubagemDivisao", "<>", idFatorCubagemDivisao, Long.class);
        }
        
        return (FatorCubagemDivisao) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
    
    public List<FatorCubagemDivisao> findFatorCubagemConflitoByIdDivisaoCliente(Long idDivisaoCliente, Long idFatorCubagemDivisao, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
        
        StringBuilder from = new StringBuilder();
        from.append(FatorCubagemDivisao.class.getName()).append(" as fcd ");
        from.append("left join fetch fcd.divisaoCliente").append(" as divisao ");
        
        SqlTemplate sql = new SqlTemplate();
        sql.addFrom(from.toString());
        
        // criterias, campos da tela de listagem
        sql.addCriteria("divisao.id", "=", idDivisaoCliente, Long.class);
        sql.addCustomCriteria("fcd.dtVigenciaFinal >= fcd.dtVigenciaInicial");
        sql.addCustomCriteria("(fcd.dtVigenciaInicial >= to_date('"+dtVigenciaInicial.toString("yyyy-MM-dd")+"','yyyy-mm-dd') " +
        		" and fcd.dtVigenciaInicial <= to_date('"+dtVigenciaFinal.toString("yyyy-MM-dd")+"','yyyy-mm-dd') " +
        		" or fcd.dtVigenciaFinal >= to_date('" + dtVigenciaInicial.toString("yyyy-MM-dd") + "','yyyy-mm-dd') " +
        		" and fcd.dtVigenciaFinal <= to_date('"+dtVigenciaFinal.toString("yyyy-MM-dd")+"','yyyy-mm-dd'))");
        
        
        if (idFatorCubagemDivisao != null) {
            sql.addCriteria("fcd.idFatorCubagemDivisao", "<>", idFatorCubagemDivisao, Long.class);
        }
        
        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
}