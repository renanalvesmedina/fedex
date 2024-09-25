package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.contratacaoveiculos.model.MarcaMeioTransporte;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MarcaMeioTransporteDAO extends BaseCrudDao<MarcaMeioTransporte, Long>
{

	public List findListByCriteria(Map criterions) {
		List lista = new ArrayList();
		lista.add("dsMarcaMeioTransporte");
		return super.findListByCriteria(criterions,lista);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MarcaMeioTransporte.class;
    }
   
    //verifica se existe alguma marca com o mesmo tipo de meio de transporte.
    public boolean findMarcaMeioTransporte(MarcaMeioTransporte marcaMeioTransporte){
    	DetachedCriteria dc = createDetachedCriteria();
    	if(marcaMeioTransporte.getIdMarcaMeioTransporte()!= null){
    		dc.add(Restrictions.ne("idMarcaMeioTransporte",marcaMeioTransporte.getIdMarcaMeioTransporte()));
    	}
    	dc.add(Restrictions.ilike("dsMarcaMeioTransporte", marcaMeioTransporte.getDsMarcaMeioTransporte(),MatchMode.EXACT));
    	
    	
    	dc.add(Restrictions.eq("tpMeioTransporte",marcaMeioTransporte.getTpMeioTransporte()));
    	dc.add(Restrictions.eq("tpSituacao", marcaMeioTransporte.getTpSituacao()));
    	
    	return findByDetachedCriteria(dc).size()>0;
    }

    /**
     * Busca marca de meio de transporte a partir da descrição
     * @param dsMarcaMeioTransporte
     * @return
     */
    public MarcaMeioTransporte findMarcaMeioTransporte(String dsMarcaMeioTransporte) {
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    	dc.add(Restrictions.ilike("dsMarcaMeioTransporte",dsMarcaMeioTransporte,MatchMode.EXACT));
    	return (MarcaMeioTransporte) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
}