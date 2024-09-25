package com.mercurio.lms.edi.model.dao;


import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.NotaFiscalApple;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalAppleDAO extends BaseCrudDao<NotaFiscalApple, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalApple.class;
    }
    
    public NotaFiscalApple find(Long nrNotaFiscal, String nrSerie, YearMonthDay dtEmissao) {
    	StringBuilder hql = new StringBuilder()
    		.append("from " + this.getPersistentClass().getName() + " ")
    		.append("where nrNotaFiscal = :nrNotaFiscal ");
    	
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("nrNotaFiscal", nrNotaFiscal);
    	
    	if(nrSerie != null) {
    		hql.append("and nrSerie = :nrSerie ");
    		param.put("nrSerie", nrSerie);
    	}
    	if(dtEmissao != null) {
    		hql.append("and dtEmissao = :dtEmissao ");
    		param.put("dtEmissao", dtEmissao);
    	}    	    	    	        
    	
    	return (NotaFiscalApple) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param);
    }
}