package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.OcorrenciaPce;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaPceDAO extends BaseCrudDao<OcorrenciaPce, Long>
{

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("eventoPce",FetchMode.JOIN);
		fetchModes.put("eventoPce.processoPce",FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("eventoPce",FetchMode.JOIN);
		fetchModes.put("eventoPce.processoPce",FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return OcorrenciaPce.class;
    }

	public List findListByCriteria(Map criterions) {
		List listOrder = new ArrayList();
		listOrder.add("cdOcorrenciaPce");
		listOrder.add("dsOcorrenciaPce");
		// TODO Auto-generated method stub
		return super.findListByCriteria(criterions,listOrder);
	}

   


}