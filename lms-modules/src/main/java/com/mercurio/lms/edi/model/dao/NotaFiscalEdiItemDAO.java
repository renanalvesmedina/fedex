package com.mercurio.lms.edi.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.NotaFiscalEdiItem;


/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalEdiItemDAO extends BaseCrudDao<NotaFiscalEdiItem, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalEdiItem.class;
    }

	public Long findSequence(){		
		return Long.valueOf(getSession().createSQLQuery("select NOTA_FISCAL_EDI_ITEM_SQ.nextval from dual").uniqueResult().toString());
	}		

    public void removeByIdNotaFiscalEdi(List<Long> list) {
    	StringBuilder deleteQuery = new StringBuilder();
    	deleteQuery.append("delete from " + getPersistentClass().getName() + " as nfei ");
    	deleteQuery.append("where nfei.notaFiscalEdi.idNotaFiscalEdi in (");
    	deleteQuery.append("select nfe.idNotaFiscalEdi from " + NotaFiscalEdi.class.getName() + " as nfe where (nfe.nrNotaFiscal, nfe.cnpjReme) in ");
    	deleteQuery.append("(select nfe1.nrNotaFiscal, nfe1.cnpjReme from " + NotaFiscalEdi.class.getName() + " as nfe1 where nfe1.idNotaFiscalEdi in (:id))) ");
    	while(list.size() > 1000){
    		List<Long> sublist = new ArrayList<Long>(list.subList(0, 999));
    		getAdsmHibernateTemplate().removeByIds(deleteQuery.toString(), sublist);
    		list.removeAll(sublist);
    	}
    	getAdsmHibernateTemplate().removeByIds(deleteQuery.toString(), list);
    }
    
    public List findByIdNotaFiscalEdi(Long idNotaFiscalEdi) {
    	StringBuilder query = new StringBuilder()
    	.append(" from " + getPersistentClass().getName() + " as nfei ")
    	.append(" where nfei.notaFiscalEdi.idNotaFiscalEdi = :idNotaFiscalEdi " );
    	
    	Map criteria = new HashMap();
    	criteria.put("idNotaFiscalEdi", idNotaFiscalEdi);
    	
    	return getAdsmHibernateTemplate().findByNamedParam(query.toString(), criteria);
    }
}