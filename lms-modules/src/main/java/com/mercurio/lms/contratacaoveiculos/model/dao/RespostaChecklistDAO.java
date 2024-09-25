package com.mercurio.lms.contratacaoveiculos.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.RespostaChecklist;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RespostaChecklistDAO extends BaseCrudDao<RespostaChecklist, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return RespostaChecklist.class;
    }
    
    public boolean findRespostaCheckListByIdCheckListMeioTransporte(Long idChecklistMeioTransporte){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.createAlias("itChecklistTpMeioTransp","it");
    	
    	dc.add(Restrictions.eq("checklistMeioTransporte.idChecklistMeioTransporte",idChecklistMeioTransporte));
    	
    	dc.add(Restrictions.eq("it.tpItChecklistTpMeioTransp","T"));
    	    	
    	return findByDetachedCriteria(dc).size()>0;
   }
    
    public boolean findRespostaCheckListMotByIdCheckListMeioTransporte(Long idChecklistMeioTransporte){
    	DetachedCriteria dc = createDetachedCriteria();
    	
    	dc.createAlias("itChecklistTpMeioTransp","it");
    	
    	dc.add(Restrictions.eq("checklistMeioTransporte.idChecklistMeioTransporte",idChecklistMeioTransporte));
    	
    	dc.add(Restrictions.eq("it.tpItChecklistTpMeioTransp","M"));
    	
    	return findByDetachedCriteria(dc).size()>0;
    	
   } 
   
   public boolean findRespostaChecklistMTReprovada(Long idChecklistMeioTransporte){
	   DetachedCriteria dc = createDetachedCriteria();
	   dc.createAlias("itChecklistTpMeioTransp","it");
	   dc.add(Restrictions.eq("checklistMeioTransporte.idChecklistMeioTransporte",idChecklistMeioTransporte));
	   dc.add(Restrictions.eq("it.tpItChecklistTpMeioTransp","T"));
	   dc.add(Restrictions.eq("blAprovado",Boolean.FALSE));
	   dc.add(Restrictions.eq("it.blObrigatorioAprovacao", Boolean.TRUE));
	   return findByDetachedCriteria(dc).size()>0;
   }
    
   public boolean findRespostaChecklistMOReprovada(Long idChecklistMeioTransporte){
	   DetachedCriteria dc = createDetachedCriteria();
	   dc.createAlias("itChecklistTpMeioTransp","it");
	   dc.add(Restrictions.eq("checklistMeioTransporte.idChecklistMeioTransporte",idChecklistMeioTransporte));
	   dc.add(Restrictions.eq("it.tpItChecklistTpMeioTransp","M"));
	   dc.add(Restrictions.eq("blAprovado",Boolean.FALSE));
	   dc.add(Restrictions.eq("it.blObrigatorioAprovacao", Boolean.TRUE));
	   return findByDetachedCriteria(dc).size()>0;
   }

   


}