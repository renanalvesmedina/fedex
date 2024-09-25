package com.mercurio.lms.configuracoes.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.IndicadorFinanceiro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class IndicadorFinanceiroDAO extends BaseCrudDao<IndicadorFinanceiro, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return IndicadorFinanceiro.class;
    }
    
    /**
     * Busca as Indicadores Financeiros do Pais informado.
     * @param idPais
     * @return Indicadores Financeiros do país
     */
    public List findIndicadoresFinanceirosByPais(Long idPais){
    	
    	List list = findByDetachedCriteria(
    			createDetachedCriteria()
					.add(Expression.eq("pais.idPais",idPais))
					.addOrder(Order.asc("nmIndicadorFinanceiro")));

    	return list;
    }    

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
       	lazyFindPaginated.put("pais",FetchMode.JOIN);
       	lazyFindPaginated.put("frequencia",FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("pais",FetchMode.JOIN);
    	lazyFindById.put("frequencia",FetchMode.JOIN);
    }    
    
    public List findListByCriteria(Map criterions) {    	
    	
    	List order = new ArrayList(1);
    	order.add("nmIndicadorFinanceiro");
    	
        return super.findListByCriteria(criterions, order);
    }

	public List findIndicadoresFinanceirosByPais(Long idPais, Long idIndicadorFinanceiroInativo, String tpSituacao) {
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("indF");
		sql.addInnerJoin(IndicadorFinanceiro.class.getName(),"indF");
		sql.addInnerJoin("indF.pais","p");
		
		String criterios = "";
		
		criterios = "p.id = ? "; 
		sql.addCriteriaValue(idPais);
		
		if( tpSituacao != null ){
			criterios += "and indF.tpSituacao = ? ";
			sql.addCriteriaValue(tpSituacao); 
		}
		
		if( idIndicadorFinanceiroInativo != null ){
			
			criterios += "or ( indF.id = ? )";
			sql.addCriteriaValue(idIndicadorFinanceiroInativo);						
			
		}
		
		sql.addCustomCriteria(criterios);
		
		sql.addOrderBy("indF.nmIndicadorFinanceiro");
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
	}
}