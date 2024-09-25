package com.mercurio.lms.contasreceber.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contasreceber.model.OcorrenciaPreFatura;

/**
 * @author Jos� Rodrigo Moraes
 * @since  28/04/2006
 *  
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaPreFaturaDAO extends BaseCrudDao<OcorrenciaPreFatura, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return OcorrenciaPreFatura.class;
	}

}
