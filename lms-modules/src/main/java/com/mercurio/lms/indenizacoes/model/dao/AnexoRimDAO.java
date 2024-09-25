package com.mercurio.lms.indenizacoes.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.indenizacoes.model.AnexoRim;

public class AnexoRimDAO extends BaseCrudDao<AnexoRim, Long>{

	@Override
	protected Class getPersistentClass() {
		return AnexoRim.class;
	}
	
	private String addJoins(boolean isFetch){
		String fetch = isFetch ? "fetch" : "";    	
		StringBuffer hql = new StringBuffer(); 
		
		hql.append(" from " + AnexoRim.class.getName() + " as ar ");
    	hql.append(" join " + fetch + " ar.reciboIndenizacao ri");
    	hql.append(" join " + fetch + " ar.usuarioLMS ulms");
    	hql.append(" join " + fetch + " ulms.usuarioADSM uadsm");
		
    	return hql.toString();
	}

	public List findItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		StringBuffer hql = new StringBuffer(); 
		hql.append(addJoins(true));
    	hql.append(" where ri.idReciboIndenizacao = ?");
    	
		return getAdsmHibernateTemplate().find(hql.toString(), idReciboIndenizacao);
	}

	public Integer getRowCountItensByIdReciboIndenizacao(Long idReciboIndenizacao) {
		StringBuffer hql = new StringBuffer(); 
		hql.append(addJoins(false));
		hql.append(" where ri.idReciboIndenizacao = ?");
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[] {idReciboIndenizacao});
	}
	
}