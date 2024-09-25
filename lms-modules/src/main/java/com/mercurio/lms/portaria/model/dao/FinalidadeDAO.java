package com.mercurio.lms.portaria.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.portaria.model.Finalidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class FinalidadeDAO extends BaseCrudDao<Finalidade, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return Finalidade.class;
    }

   
    public List findListByCriteria(Map criterions) {
    	List order = new ArrayList();
    	order.add("dsFinalidade");
    	return super.findListByCriteria(criterions, order);
    }


}