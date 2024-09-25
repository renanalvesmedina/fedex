package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.ItemCheckList;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemCheckListDAO extends BaseCrudDao<ItemCheckList, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemCheckList.class;
    }
    
   
    public List findListByCriteria(Map criterions) {
    	if (criterions == null) criterions = new HashMap();
    	List order = new ArrayList(1);
    	order.add("dsItemCheckList");
        return super.findListByCriteria(criterions, order);
    }	
    

   


}