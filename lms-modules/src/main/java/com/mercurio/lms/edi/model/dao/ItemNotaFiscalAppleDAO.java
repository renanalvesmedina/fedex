package com.mercurio.lms.edi.model.dao;


import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.ItemNotaFiscalApple;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItemNotaFiscalAppleDAO extends BaseCrudDao<ItemNotaFiscalApple, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItemNotaFiscalApple.class;
    }
        
    public ItemNotaFiscalApple find(Long nrNotaFiscal, Long idInformacaoDoctoCliente, String dsDnn) {
    	StringBuilder hql = new StringBuilder()
    		.append("from " + this.getPersistentClass().getName() + " as itemNotaFiscalApple ")
    		.append("inner join fetch itemNotaFiscalApple.notaFiscalApple as notaFiscalApple ")
    		.append("where notaFiscalApple.nrNotaFiscal = :nrNotaFiscal ")
    		.append("and itemNotaFiscalApple.dsDnn = :dsDnn ")
    		.append("and itemNotaFiscalApple.informacaoDoctoCliente.id = :idInformacaoDoctoCliente ");
    	
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("nrNotaFiscal", nrNotaFiscal);
    	param.put("dsDnn", dsDnn);
    	param.put("idInformacaoDoctoCliente", idInformacaoDoctoCliente); 
    	
    	return (ItemNotaFiscalApple) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param);
    }
}