package com.mercurio.lms.seguros.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.seguros.model.SituacaoReembolso;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SituacaoReembolsoDAO extends BaseCrudDao<SituacaoReembolso, Long>{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	protected Class getPersistentClass() {
		return SituacaoReembolso.class;
	}
	
}
		