package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ParcelaCtoCooperada;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaCtoCooperadaDAO extends BaseCrudDao<ParcelaCtoCooperada, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class<ParcelaCtoCooperada> getPersistentClass() {
		return ParcelaCtoCooperada.class;
	}

}