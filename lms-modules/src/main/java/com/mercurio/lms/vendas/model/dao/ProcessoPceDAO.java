package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ProcessoPce;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ProcessoPceDAO extends BaseCrudDao<ProcessoPce, Long>
{

	public List findListByCriteria(Map criterions) {
		List listOrder = new ArrayList();
		listOrder.add("cdProcessoPce");
		listOrder.add("dsProcessoPce");
		return super.findListByCriteria(criterions,listOrder);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ProcessoPce.class;
    }

   


}