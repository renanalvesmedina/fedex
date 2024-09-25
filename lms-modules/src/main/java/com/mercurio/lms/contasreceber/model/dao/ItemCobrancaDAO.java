package com.mercurio.lms.contasreceber.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contasreceber.model.ItemCobranca;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemCobrancaDAO extends BaseCrudDao<ItemCobranca, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemCobranca.class;
    }
    
    /**
	 * Método responsável por buscar itemCobranca que associados a fatura em questão
	 * 
	 * @param idFatura
	 * @return List de itemCobranca
	 */
    public List findFaturaInCobrancaInadimplencia(java.lang.Long idFatura) {
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("ic");
    	
    	sql.addFrom(ItemCobranca.class.getName(), "ic JOIN FETCH ic.cobrancaInadimplencia as ci " +
    			"JOIN FETCH ic.cobrancaInadimplencia.usuario as u ");
    	
    	sql.addCriteria("ic.fatura.idFatura", "=", idFatura);
    	sql.addCriteria("rownum", "=", Integer.valueOf(1));
    	
    	sql.addOrderBy("ic.idItemCobranca", "desc");
    	
    	List list = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    	
    	return list;
    }
    
    /**
     * 
     * 
     * Hector Julian Esnaola Junior
     * 29/02/2008
     *
     * @param idFatura
     * @return
     *
     * List
     *
     */
    public Long findItemCobrancaByIdFatura(Long idFatura, DomainValue... domainValues) {
    	
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("count(ic)");
    	hql.addFrom(ItemCobranca.class.getName(), "ic JOIN ic.cobrancaInadimplencia as ci");
    	
    	hql.addCriteria("ic.fatura.idFatura", "=", idFatura);
    	hql.addCriteriaIn("ic.fatura.tpSituacaoFatura", domainValues);
    	
    	return (Long)getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).get(0);
    }

}