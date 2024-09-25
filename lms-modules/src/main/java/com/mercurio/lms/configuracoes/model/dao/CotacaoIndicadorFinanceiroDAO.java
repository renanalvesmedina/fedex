package com.mercurio.lms.configuracoes.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.CotacaoIndicadorFinanceiro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CotacaoIndicadorFinanceiroDAO extends BaseCrudDao<CotacaoIndicadorFinanceiro, Long>
{

	/**
	 * Busca valor da Cotação do Indicador Financeiro.<BR> 
	 * @param sgIndicadorFinanceiro - Sigla do indicador
	 * @param idPais - País do indicador
	 * @param dtVigencia - Data para que busque o a cotação mais recente, porém anterior ou igual a essa data
	 * @return
	 */
	public BigDecimal findVlCotacaoIndFinanceiro(String sgIndicadorFinanceiro, Long idPais, YearMonthDay dtVigencia){

		SqlTemplate sql = new SqlTemplate();
			
		sql.addProjection("cot.vlCotacaoIndFinanceiro");
		sql.addFrom(getPersistentClass().getName() + " cot join cot.indicadorFinanceiro if " );
		sql.addCriteria("lower(if.sgIndicadorFinanceiro)", "=", sgIndicadorFinanceiro.toLowerCase());
		sql.addCriteria("if.pais.id", "=", idPais);
		
		sql.addCustomCriteria("(cot.dtCotacaoIndFinanceiro <= ? )");
		sql.addCriteriaValue(dtVigencia);
		
		sql.addOrderBy("cot.dtCotacaoIndFinanceiro", "desc");
	
		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		if (!list.isEmpty()){
			//Deve existir sempre um registro no banco
			return (BigDecimal) list.get(0);
		}
		return null;
		
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return CotacaoIndicadorFinanceiro.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {    	
    	lazyFindById.put("indicadorFinanceiro",FetchMode.JOIN);
    	lazyFindById.put("indicadorFinanceiro.pais",FetchMode.JOIN);
    }    
    
}