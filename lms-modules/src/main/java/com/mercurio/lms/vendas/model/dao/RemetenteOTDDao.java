package com.mercurio.lms.vendas.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.ClienteOperadorLogistico;
import com.mercurio.lms.vendas.model.RemetenteOTD;

public class RemetenteOTDDao extends BaseCrudDao<RemetenteOTD, Long> {

	@Override
	protected Class getPersistentClass() {
		return RemetenteOTD.class;
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map map) {
//		map.put("cliente", FetchMode.JOIN);
//		map.put("cliente.pessoa", FetchMode.JOIN);
		
		map.put("remetente", FetchMode.JOIN);
		map.put("remetente.pessoa", FetchMode.JOIN);

	}

	public List<RemetenteOTD> findByIdClienteRemetente(Long idCliente, Long idRemetente) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("select remOtd from " + RemetenteOTD.class.getName() + " remOtd ");
		sb.append(" where remOtd.cliente.idCliente = ? ");
		sb.append(" and remOtd.remetente.idCliente = ? ");
		
		return getHibernateTemplate().find(sb.toString(),new Object[] { idCliente, idRemetente});
	}
	
}
