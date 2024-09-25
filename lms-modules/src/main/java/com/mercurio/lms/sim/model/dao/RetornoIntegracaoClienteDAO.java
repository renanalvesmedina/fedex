package com.mercurio.lms.sim.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.sim.model.RetornoIntegracaoCliente;

public class RetornoIntegracaoClienteDAO extends BaseCrudDao<RetornoIntegracaoCliente, Long> {
    @Override
    protected Class getPersistentClass() {
        return RetornoIntegracaoCliente.class;
    }
}
