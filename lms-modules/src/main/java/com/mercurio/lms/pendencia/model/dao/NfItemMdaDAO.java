package com.mercurio.lms.pendencia.model.dao;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.pendencia.model.NfItemMda;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NfItemMdaDAO extends BaseCrudDao<NfItemMda, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NfItemMda.class;
    }

	/**
	 * Apaga uma entidade através do Id do ItemMda
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeByIdItemMda(Serializable idItemMda) {
        String sql = "delete from " + NfItemMda.class.getName() + " as nfim " + 
        			 " where " +
        			 "nfim.itemMda.id = :id";
        
        getAdsmHibernateTemplate().removeById(sql, idItemMda);
    } 

}