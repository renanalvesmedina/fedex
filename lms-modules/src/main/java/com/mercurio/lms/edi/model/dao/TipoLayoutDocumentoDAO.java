package com.mercurio.lms.edi.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.TipoLayoutDocumento;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class TipoLayoutDocumentoDAO extends BaseCrudDao<TipoLayoutDocumento, Long>{

	@Override
	protected Class getPersistentClass() {
		return TipoLayoutDocumento.class;
	}
		

}
