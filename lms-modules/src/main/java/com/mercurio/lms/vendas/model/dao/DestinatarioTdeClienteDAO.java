package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.vendas.model.DestinatarioTdeCliente;

public class DestinatarioTdeClienteDAO extends BaseCrudDao<DestinatarioTdeCliente, Long> {

	@Override
	protected Class getPersistentClass() {
		return DestinatarioTdeCliente.class;
	}

	public DestinatarioTdeCliente findByIdTdeClienteAndIdClienteDestinatario(Long idTdeCliente, Long idClienteDestinatario) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT dtc ")
		.append(" FROM ").append(DestinatarioTdeCliente.class.getSimpleName()).append(" dtc ")
		.append(" JOIN dtc.tdeCliente tde ")
		.append(" JOIN dtc.cliente cl ")
		.append(" WHERE ")
		.append("	 cl.idCliente =:idClienteDestinatario ")
		.append("AND tde.idTdeCliente =:idTdeCliente");
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idClienteDestinatario", idClienteDestinatario);
		parameters.put("idTdeCliente", idTdeCliente);
		
		List<DestinatarioTdeCliente> l = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parameters);
		
		if (!l.isEmpty()) {
			return l.get(0);
		}
		
		return null;
	}

	
	
}
