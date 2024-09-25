package com.mercurio.lms.indenizacoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.indenizacoes.model.MdaSalvadoIndenizacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MdaSalvadoIndenizacaoDAO extends BaseCrudDao<MdaSalvadoIndenizacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MdaSalvadoIndenizacao.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("mda", FetchMode.JOIN);
    	lazyFindPaginated.put("mda.filialByIdFilialOrigem", FetchMode.JOIN);
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("mda", FetchMode.JOIN);
    	lazyFindById.put("mda.filialByIdFilialOrigem", FetchMode.JOIN);
    }
    
    public MdaSalvadoIndenizacao findByIdMdaAndIdReciboIndenizacao(Long idMda, Long idReciboIndenizacao) {
    	return (MdaSalvadoIndenizacao)getAdsmHibernateTemplate().find("from "+MdaSalvadoIndenizacao.class.getName()+" msi where msi.mda.id = ? and msi.reciboIndenizacao.id = ?", new Object[]{idMda, idReciboIndenizacao});
    }
    
    private String getItensByIdReciboIndenizacaoQuery(boolean withFetch) {    	
    	String fetch = withFetch ? "fetch" : "";    	
    	StringBuffer s = new StringBuffer()
    	.append("from "+MdaSalvadoIndenizacao.class.getName()+" msi ")
    	.append("join "+fetch+" msi.mda mda ")
    	.append("join "+fetch+" mda.filialByIdFilialOrigem ")
    	.append("where msi.reciboIndenizacao.id = ? ");
    	return s.toString();
    }
    
    public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getAdsmHibernateTemplate().find(getItensByIdReciboIndenizacaoQuery(true), idReciboIndenizacao);
    }
    
    public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	return getAdsmHibernateTemplate().getRowCountForQuery(getItensByIdReciboIndenizacaoQuery(false), new Object[] {idReciboIndenizacao});
    }
}