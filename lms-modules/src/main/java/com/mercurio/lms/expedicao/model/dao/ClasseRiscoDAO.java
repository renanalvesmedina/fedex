package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ClasseRisco;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ClasseRiscoDAO extends BaseCrudDao{
	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return ClasseRisco.class;
	}

	public List findListByCriteria(Map criterions, List order) {
		order = new ArrayList(1);
		order.add("nrClasseRisco");
		return super.findListByCriteria(criterions, order);
	}
}