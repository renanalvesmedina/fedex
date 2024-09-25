package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DominioAgrupamento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DominioAgrupamentoDAO extends BaseCrudDao<DominioAgrupamento, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DominioAgrupamento.class;
    }

    /**
     * Remove todos os domínios agrupamentos que pertençam a forma de agrupamento passada por parâmetro
     * @param formaAgrupamento
     */
    public void removeDominiosAgrupamentosByFormaAgrupamento(Long idFormaAgrupamento){
    	if(idFormaAgrupamento != null){
    		StringBuilder query = new StringBuilder();
    		query.append("delete ").append(DominioAgrupamento.class.getName());
			query.append(" where formaAgrupamento.id = :id");

    		getAdsmHibernateTemplate().removeById(query.toString(),idFormaAgrupamento);
    	}
    }    
      
}