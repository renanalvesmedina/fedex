package com.mercurio.lms.tabelaprecos.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.tabelaprecos.model.ReajusteDivisaoCliente;


public class ReajusteDivisaoClienteDAO extends BaseCrudDao<ReajusteDivisaoCliente, Long> {

	@Override
	protected final Class getPersistentClass() {
		return ReajusteDivisaoCliente.class;
	}
	
}