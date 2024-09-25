package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.MotivoPreFaturaServico;

public class MotivoPreFaturaServicoDAO extends BaseCrudDao<MotivoPreFaturaServico, Long> {
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return MotivoPreFaturaServico.class;
	}
	
	public MotivoPreFaturaServico findByDsMotivoPreFaturaServico() {
		StringBuffer sb = new StringBuffer();
		sb.append(" From ");
		sb.append("	" + MotivoPreFaturaServico.class.getName() + " as m ");
		sb.append(" Where ");
		sb.append(" 	m.dsMotivoPreFaturaServico = 'Reprovação Automática' ");
		
    	return (MotivoPreFaturaServico)(getAdsmHibernateTemplate().find(sb.toString()).get(0)); 
	}
}