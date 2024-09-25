package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ProcessamentoIbEdi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessamentoIbEdiDAO  extends BaseCrudDao<ProcessamentoIbEdi, Long> {

    @Override
    protected Class<ProcessamentoIbEdi> getPersistentClass() {
        return ProcessamentoIbEdi.class;
    }

    public ProcessamentoIbEdi findRemainingNotesByNrProcesso(Long nrProcessamento, Boolean dhEnvioIsNull) {
        StringBuilder hql = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nrProcessamento", nrProcessamento);

        hql.append("SELECT pie");
        hql.append("  FROM " + ProcessamentoIbEdi.class.getName() + " pie");
        hql.append(" WHERE pie.nrProcessamento = :nrProcessamento");

        if(dhEnvioIsNull) {
            hql.append(" and pie.dhEnvio is null");
        }

        ProcessamentoIbEdi processamentoIbEdi = (ProcessamentoIbEdi) getAdsmHibernateTemplate()
            .findUniqueResult(hql.toString(), parameters);

        return processamentoIbEdi;

    }
}
