package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.EventoPce;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EventoPceDAO extends BaseCrudDao<EventoPce, Long>
{

	public List findListByCriteria(Map criterions) {
		List listOrder = new ArrayList();
		listOrder.add("cdEventoPce");
		listOrder.add("dsEventoPce");
		return super.findListByCriteria(criterions,listOrder);
	}
	
	public List findListByCriteriaToCombo(Map criterions) {
		List listOrder = new ArrayList();
		listOrder.add("cdEventoPce");
		listOrder.add("dsEventoPce");
		return super.findListByCriteria(criterions,listOrder);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("processoPce", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EventoPce.class;
    }

   


}