package com.mercurio.lms.sim.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sim.model.RegistroPriorizacaoDocto;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistroPriorizacaoDoctoDAO extends BaseCrudDao<RegistroPriorizacaoDocto, Long> {
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RegistroPriorizacaoDocto.class;
    }
    
    public List findByIdDocto(Long idDocto,Boolean isCanceled,Long idPriorizaoEmbarq) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"RPD")
						    		.createAlias("RPD.doctoServico","DS")
						    		.createAlias("RPD.registroPriorizacaoEmbarq","RPE")
						    		.add(Restrictions.eq("DS.id",idDocto));
    	if (isCanceled.booleanValue())
    		dc.add(Restrictions.isNotNull("RPE.dhCancelamento.value"));
    	else
    		dc.add(Restrictions.isNull("RPE.dhCancelamento.value"));
    	
    	if (idPriorizaoEmbarq != null)
    		dc.add(Restrictions.ne("RPE.id",idPriorizaoEmbarq));
    	
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

}
