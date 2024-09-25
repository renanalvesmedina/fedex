package com.mercurio.lms.indenizacoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.indenizacoes.model.ParcelaReciboIndenizacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaReciboIndenizacaoDAO extends BaseCrudDao<ParcelaReciboIndenizacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ParcelaReciboIndenizacao.class;
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("moeda", FetchMode.JOIN);
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("moeda", FetchMode.JOIN);
    }

    public int removeParcelasByIdReciboIndenizacao(Long idReciboIndenizacao) {    	
    	
    	String s = "delete from " +
    		ParcelaReciboIndenizacao.class.getName() + " as parcelaReciboIndenizacao " +
    		" where parcelaReciboIndenizacao.id in (select pri.id from ParcelaReciboIndenizacao pri where pri.reciboIndenizacao.id = ?) ";
    	
    	return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s).setParameter(0, idReciboIndenizacao).executeUpdate();
    }
    

    private String getItensByIdReciboIndenizacaoQuery(boolean withFetch) {    	
    	String fetch = withFetch ? "fetch" : "";    	
    	StringBuffer s = new StringBuffer()
    	.append("from "+ParcelaReciboIndenizacao.class.getName()+" pri ")
    	.append("join "+fetch+" pri.moeda mo ")
    	.append("where pri.reciboIndenizacao.id = ? ");
    	return s.toString();
    }
    
    public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getAdsmHibernateTemplate().find(getItensByIdReciboIndenizacaoQuery(true), idReciboIndenizacao);
    }
    
    public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getAdsmHibernateTemplate().getRowCountForQuery(getItensByIdReciboIndenizacaoQuery(false), new Object[] {idReciboIndenizacao});
    }
    
}