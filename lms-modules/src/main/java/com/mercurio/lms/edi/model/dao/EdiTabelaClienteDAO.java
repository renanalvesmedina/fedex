package com.mercurio.lms.edi.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.edi.dto.TabelaClienteDTO;
import com.mercurio.lms.edi.model.EdiTabelaCliente;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EdiTabelaClienteDAO extends BaseCrudDao<EdiTabelaCliente, Long> {
    @Override
    protected Class getPersistentClass() {
        return EdiTabelaCliente.class;
    }

    public List<EdiTabelaCliente> findByIdEdiTabelaClienteAndIdEdiTabelaOcoren
        (long idEdiTabelaCliente, Long idEdiTabelaOcoren){

        StringBuilder sql = new StringBuilder();
        sql.append(" from " + getPersistentClass().getName() + " as etc ");
        sql.append(" where etc.idEdiTabelaCliente = :idEdiTabelaCliente");
        sql.append(" and etc.ediTabelaOcoren.idEdiTabelaOcoren = :idEdiTabelaOcoren ");

        Map criteria = new HashMap();
        criteria.put("idEdiTabelaCliente", idEdiTabelaCliente);
        criteria.put("idEdiTabelaOcoren", idEdiTabelaOcoren);

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
    }

    public List<TabelaClienteDTO> findByIdEdiTabelaOcoren(Long idEdiTabelaOcoren){

        StringBuilder sql = new StringBuilder();
        sql.append(" select new com.mercurio.lms.edi.dto.TabelaClienteDTO( ");
        sql.append(" etc.idEdiTabelaCliente, etc.ediTabelaOcoren.idEdiTabelaOcoren, ");
        sql.append(" etc.idCliente, p.nrIdentificacao, p.nmPessoa) ");
        sql.append(" from " + getPersistentClass().getName() + " as etc, ");
        sql.append(" Pessoa p ");
        sql.append(" where etc.idCliente = p.idPessoa ");
        sql.append(" and etc.ediTabelaOcoren.idEdiTabelaOcoren = :idEdiTabelaOcoren ");

        Map criteria = new HashMap();
        criteria.put("idEdiTabelaOcoren", idEdiTabelaOcoren);

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
    }
}
