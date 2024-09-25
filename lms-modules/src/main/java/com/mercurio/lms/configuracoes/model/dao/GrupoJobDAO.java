package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.GrupoJob;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GrupoJobDAO extends BaseCrudDao<GrupoJob, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return GrupoJob.class;
    }
    
    /**
     * Realiza a pesquisa tendo como parametro o nome do <code>Grupo</code>
     * 
     * @param nmGrupo
     * @return
     */
    public List findByNomeGrupo(String nmGrupo) {
    	
    	DetachedCriteria detachedCriteria =  DetachedCriteria.forClass(GrupoJob.class)
    		.add(Restrictions.eq("nmGrupo", nmGrupo));
    	
    	List result = this.findByDetachedCriteria(detachedCriteria);
    	
    	return result;
    }
    
}