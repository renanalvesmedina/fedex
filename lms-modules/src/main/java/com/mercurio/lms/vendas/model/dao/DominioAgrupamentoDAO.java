package com.mercurio.lms.vendas.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DominioAgrupamento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DominioAgrupamentoDAO extends BaseCrudDao<DominioAgrupamento, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return DominioAgrupamento.class;
    }

    /**
     * Remove todos os dom�nios agrupamentos que perten�am a forma de agrupamento passada por par�metro
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