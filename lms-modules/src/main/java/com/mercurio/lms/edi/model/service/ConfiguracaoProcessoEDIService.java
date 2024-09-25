package com.mercurio.lms.edi.model.service;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.CrudService;

import com.mercurio.lms.edi.model.ConfiguracaoProcessoEDI;
import com.mercurio.lms.edi.model.LogEDI;

import com.mercurio.lms.edi.model.dao.ConfiguracaoProcessoEDIDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.edi.configuracaoProcessoEDIService"
 */

public class ConfiguracaoProcessoEDIService extends CrudService<ConfiguracaoProcessoEDI, Long> {

	
	@Override
	public LogEDI findById(Long id) {		
		return (LogEDI)super.findById(id);
	}

	public ConfiguracaoProcessoEDI findConfigByChave(String chave){
		return this.getConfiguracaoProcessoEDIDAO().findConfigByChave(chave);
	}
	
	
	@Override
	public Serializable store(ConfiguracaoProcessoEDI bean) {
		return super.store(bean);
	}
	
	private ConfiguracaoProcessoEDIDAO getConfiguracaoProcessoEDIDAO() {
        return (ConfiguracaoProcessoEDIDAO) getDao();
    }
    
    public void setConfiguracaoProcessoEDIDAO(ConfiguracaoProcessoEDIDAO dao) {
        setDao(dao);
    }	
	
}
