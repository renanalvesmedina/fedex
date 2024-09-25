package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.Pessoa99;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class Pessoa99DAO extends BaseCrudDao<Pessoa99, Long> {

	public Pessoa99 findByPessoa(Long idPessoa) {
		Pessoa99 param = null;
		String query = "select ps99 from " + Pessoa99.class.getName() + " as ps99 " +
					   "     join ps99.pessoa as ps" +
		 			   " where ps.id = " + idPessoa;
		List list = (List) getAdsmHibernateTemplate().find(query);
		if(list != null && list.size() > 0) {
			param = (Pessoa99) list.get(0);
		}
    	return param;
    }
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected Class getPersistentClass() {
		return Pessoa99.class;
	}

}
