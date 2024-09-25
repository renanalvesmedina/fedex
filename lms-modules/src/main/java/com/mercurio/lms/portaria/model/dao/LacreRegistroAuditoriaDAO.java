package com.mercurio.lms.portaria.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.portaria.model.LacreRegistroAuditoria;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LacreRegistroAuditoriaDAO extends BaseCrudDao<LacreRegistroAuditoria, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return LacreRegistroAuditoria.class;
    }

   
    public List findLacresByRegistroAuditoria(Long idRegistroAuditoria, Boolean blOriginal){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("new Map(lcc.nrLacres", "nrLacres");
    	sql.addProjection("lra.idLacreRegistroAuditoria", "idLacreRegistroAuditoria)");
    	
    	sql.addInnerJoin(getPersistentClass().getName(), "lra");
    	sql.addInnerJoin("lra.registroAuditoria", "ra");
    	sql.addInnerJoin("lra.lacreControleCarga", "lcc");
    	
    	sql.addCriteria("ra.id", "=", idRegistroAuditoria); 
    	sql.addCriteria("lra.blOriginal", "=", blOriginal);
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }


}
