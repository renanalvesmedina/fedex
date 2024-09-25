package com.mercurio.lms.gm.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AutorizadorDAO extends BaseCrudDao {
	/** 
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return UsuarioADSM.class;
	}	

}