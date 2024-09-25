package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.DoctoServicoPPEPadrao;

public class DoctoServicoPPEPadraoDAO extends BaseCrudDao<DoctoServicoPPEPadrao, Long>{

	@Override
	protected Class getPersistentClass() {
		return DoctoServicoPPEPadrao.class;
	}

	public DoctoServicoPPEPadrao findByIdDoctoServico(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT dsppep FROM ");
		hql.append(getPersistentClass().getName()).append(" dsppep ");
		hql.append(" LEFT JOIN FETCH dsppep.doctoServico ds ");
		hql.append(" WHERE ");
		hql.append(" ds.idDoctoServico = ? ");
		
		return (DoctoServicoPPEPadrao) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idDoctoServico});
	}

}
