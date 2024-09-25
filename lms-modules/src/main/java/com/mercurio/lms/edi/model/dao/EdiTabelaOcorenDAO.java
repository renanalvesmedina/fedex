package com.mercurio.lms.edi.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.model.EdiTabelaOcoren;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdiTabelaOcorenDAO extends BaseCrudDao<EdiTabelaOcoren, Long> {
    @Override
    protected Class getPersistentClass() {
        return EdiTabelaOcoren.class;
    }

    public List<EdiTabelaOcoren> findByNmTabelaOcorenWebService(String nmTabelaOcoren, String blWebservice){
        StringBuilder sql = new StringBuilder();
        sql.append(" from " + getPersistentClass().getName() + " as eto ");
        sql.append(" where lower(eto.nmTabelaOcoren) like :nmTabelaOcoren");

        Map criteria = new HashMap();
        criteria.put("nmTabelaOcoren", nmTabelaOcoren.toLowerCase() + "%");

        if(blWebservice != null){
            sql.append(" and eto.blWebservice = :blWebservice");
            criteria.put("blWebservice", "S".equals(blWebservice));
        }

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
    }
}
