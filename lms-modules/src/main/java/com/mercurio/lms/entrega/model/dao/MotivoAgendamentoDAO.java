package com.mercurio.lms.entrega.model.dao;

import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.entrega.model.MotivoAgendamento;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotivoAgendamentoDAO extends BaseCrudDao<MotivoAgendamento, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return MotivoAgendamento.class;
    }

    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	// TODO Auto-generated method stub
    	super.initFindPaginatedLazyProperties(lazyFindPaginated);
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("filial", FetchMode.JOIN);
    	lazyFindById.put("filial.pessoa", FetchMode.JOIN);
    	super.initFindByIdLazyProperties(lazyFindById);
    }
	
    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		return super.findPaginated(criteria, findDef);
	}

    
    
	public MotivoAgendamento findMotivoAgendamento(String dsMotivoAgendamento){
    	SqlTemplate hql = new SqlTemplate();
		hql.addFrom(MotivoAgendamento.class.getName() + " ma ");
		hql.addCriteria("i18n(ma.dsMotivoAgendamento)","=",dsMotivoAgendamento);
		java.util.List obj = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		MotivoAgendamento result = null;
		if(obj.size()>0){
			result = (MotivoAgendamento) obj.get(0);
	}
		return result;
    }

}