package com.mercurio.lms.edi.model.dao;


import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.ConfiguracaoProcessoEDI;




/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */

public class ConfiguracaoProcessoEDIDAO extends BaseCrudDao<ConfiguracaoProcessoEDI, Long>{

	@Override
	public Class getPersistentClass() {		
		return ConfiguracaoProcessoEDI.class;
	}
		
	@SuppressWarnings("unchecked")
	public ConfiguracaoProcessoEDI findConfigByChave(String chave){
		ConfiguracaoProcessoEDI config = null;
		String hql =  "FROM " + ConfiguracaoProcessoEDI.class.getName() + " cPro WHERE cPro.chave = ? ";   
        
		List<ConfiguracaoProcessoEDI> retorno = (List<ConfiguracaoProcessoEDI>) getHibernateTemplate().find(hql, new Object[]{chave});
		
        if(!retorno.isEmpty()) {
        	config = (ConfiguracaoProcessoEDI) retorno.get(0);
        }                                
        return config; 
	}
	
}

