package com.mercurio.lms.carregamento.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.ComprovanteEntrega;


/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring.
 * 
 */
public class ComprovanteEntregaDAO extends BaseCrudDao<ComprovanteEntrega, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ComprovanteEntrega.class;
	}

	public ComprovanteEntrega store(ComprovanteEntrega comprovanteEntrega) {
		super.store(comprovanteEntrega);
		getAdsmHibernateTemplate().flush();
		return comprovanteEntrega;
	}
}