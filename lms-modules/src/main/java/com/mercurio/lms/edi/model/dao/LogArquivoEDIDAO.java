package com.mercurio.lms.edi.model.dao;


import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.LogArquivoEDI;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class LogArquivoEDIDAO extends BaseCrudDao<LogArquivoEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return LogArquivoEDI.class;
	}
		

	
}

