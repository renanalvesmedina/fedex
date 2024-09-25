package com.mercurio.lms.seguros.model.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Query;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.seguros.model.DoctoProcessoSinistro;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DoctoProcessoSinistroDAO extends BaseCrudDao<DoctoProcessoSinistro, Long>
{

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
    protected final Class getPersistentClass() {
        return DoctoProcessoSinistro.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("tipoDocumentoSeguro", FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("tipoDocumentoSeguro", FetchMode.JOIN);
    }
    
    /**
     * Obt�m o maior n�mero de protocolo de acordo com o tipo de entrega de recebimento.
     * Caso este n�o seja informado, obt�m o maior n�mero de protocolo.
     * @author luisfco
     * @param tpEntregaRecebimento
     * @return
     */
    public Long findMaxNrProtocolo(String tpEntregaRecebimento) {

    	StringBuffer s = new StringBuffer()
    	.append("select max(dps.nrProtocolo)")
    	.append("  from " + getPersistentClass().getName() + " dps")
    	.append(StringUtils.isNotBlank(tpEntregaRecebimento) ? " where dps.tpEntregaRecebimento = ? " : "");
    	
    	Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s.toString());
    	
    	if (StringUtils.isNotBlank(tpEntregaRecebimento)) 
    		q.setString(0, tpEntregaRecebimento);
    	
    	return (Long)q.uniqueResult();
    }
   
    

   


}