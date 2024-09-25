package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;

import com.mercurio.lms.expedicao.model.ProcessamentoFilaIbAgrupar;
import com.mercurio.lms.expedicao.model.ProcessamentoIbEdi;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessamentoFilaIbAgruparDAO extends BaseCrudDao<ProcessamentoFilaIbAgrupar, Long> {

    @Override
    protected Class<ProcessamentoFilaIbAgrupar> getPersistentClass() {
        return ProcessamentoFilaIbAgrupar.class;
    }

    public ProcessamentoFilaIbAgrupar findByNrProcessamento(Long nrProcessamento) {
        StringBuilder hql = new StringBuilder();

        hql.append("SELECT p");
        hql.append("  FROM " + getPersistentClass().getName() + " p");
        hql.append(" WHERE p.nrProcessamento = :nrProcessamento");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("nrProcessamento", nrProcessamento);

        ProcessamentoFilaIbAgrupar result = (ProcessamentoFilaIbAgrupar) getAdsmHibernateTemplate()
            .findUniqueResult(hql.toString(), parameters);

        return result;
    }

    public void removeByNrProcessamento(Long nrProcessamento) {
        StringBuffer sql = new StringBuffer();

        sql.append("delete from ");
        sql.append(getPersistentClass().getName());
        sql.append(" as pla ");
        sql.append(" where pla.nrProcessamento = ? ");

        List param = new ArrayList();
        param.add(nrProcessamento);

        super.executeHql(sql.toString(), param);
    }
}
