package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.ItemRedeco;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemRedecoDAO extends BaseCrudDao<ItemRedeco, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemRedeco.class;
    }

	public void flush() {
		getAdsmHibernateTemplate().flush();
    }
   
	public void clear() {
		getAdsmHibernateTemplate().clear();
    }



}