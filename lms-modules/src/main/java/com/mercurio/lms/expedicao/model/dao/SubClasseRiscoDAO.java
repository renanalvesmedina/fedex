package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.SubClasseRisco;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SubClasseRiscoDAO extends BaseCrudDao{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return SubClasseRisco.class;
	}

	public List findListByCriteria(Map criterions, List order) {
		order = new ArrayList(1);
		order.add("nrSubClasseRisco:asc");
		return super.findListByCriteria(criterions, order);
	}
}