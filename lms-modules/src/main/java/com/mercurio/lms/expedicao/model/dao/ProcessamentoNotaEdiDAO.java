package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ProcessamentoNotaEdi;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessamentoNotaEdiDAO extends BaseCrudDao<ProcessamentoNotaEdi, Long> {

    @Override
    protected Class getPersistentClass() {
        return ProcessamentoNotaEdi.class;
    }

    public void updateProcesamentoByNrNotafiscal(Long idProcessamentoEdi, Long nrNotaFiscal, String mensagem){
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder hql = new StringBuilder("UPDATE ")
                        .append(getPersistentClass().getName()).append(" pne ")
                        .append(" SET pne.blProcessada = 'S'");

                        if(mensagem != null) {
                            hql.append(", pne.dsMensagemErro = :dsMensagemErro ");
                        }

                        hql.append(" WHERE pne.nrNotaFiscal = :nrNotaFiscal ")
                        .append(" AND pne.processamentoEdi.idProcessamentoEdi = :idProcessamentoEdi ");
                Query query = session.createQuery(hql.toString())
                        .setLong("nrNotaFiscal", nrNotaFiscal.longValue())
                        .setLong("idProcessamentoEdi", idProcessamentoEdi.longValue());

                        if(mensagem != null) {
                           query.setString("dsMensagemErro", mensagem);
                        }

                query.executeUpdate();
                return null;
            }
        });
    }

    public List<Map<String, Object>> findByIdProcessamento(Long idProcessamento){
        StringBuilder hql = new StringBuilder("SELECT new map(")
                .append("pne.idProcessamentoNotaEdi as idProcessamentoNotaEdi, ")
                .append("pne.clienteRemetente.pessoa.nmPessoa as nomeClienteRemetente, ")
                .append("pne.nrNotaFiscal as nrNotaFiscal, ")
                .append("pne.dsMensagemErro as dsMensagemErro")
        .append(") FROM ").append(getPersistentClass().getName()).append(" pne")
        .append(" WHERE pne.processamentoEdi.idProcessamentoEdi = :idProcessamento");

        Map criteria = new HashMap();
        criteria.put("idProcessamento", idProcessamento);

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
    }

    public List<ProcessamentoNotaEdi> findByIdProcessamentoEdiAndDsMensagemErroIsNull(Long idProcessamentoEdi) {
        StringBuilder hql = new StringBuilder(200);
        hql.append("Select pne from ");
        hql.append(getPersistentClass().getName());
        hql.append(" pne");
        hql.append(" WHERE pne.processamentoEdi.idProcessamentoEdi = ?");
        hql.append(" and NVL(pne.dsMensagemErro, '0') = '0' ");

        return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idProcessamentoEdi});
    }

    public List<ProcessamentoNotaEdi> findByIdProcessamentoEdiAndNrIndexAndDsMensagemErroIsNull
    (Long idProcessamentoEdi, Long inicioIndex, Long finalIndex) {
        StringBuilder hql = new StringBuilder(200);
        hql.append("Select pne from ");
        hql.append(getPersistentClass().getName());
        hql.append(" pne ");
        hql.append(" WHERE pne.processamentoEdi.idProcessamentoEdi = ? ");
        hql.append(" and pne.nrIndex between ? and ? ");
        hql.append(" and NVL(pne.dsMensagemErro, '0') = '0' ");

        List retorno = getAdsmHibernateTemplate().find(hql.toString(),
                new Object[]{idProcessamentoEdi, inicioIndex, finalIndex});
        return (List<ProcessamentoNotaEdi>)retorno;
    }
}
