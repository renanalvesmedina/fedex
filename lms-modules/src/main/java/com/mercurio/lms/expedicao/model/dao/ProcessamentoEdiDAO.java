package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.edi.enums.SituacaoMonitoramentoProcesso;
import com.mercurio.lms.expedicao.model.ProcessamentoEdi;
import com.mercurio.lms.expedicao.model.ProcessamentoNotaEdi;
import org.hibernate.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessamentoEdiDAO extends BaseCrudDao<ProcessamentoEdi, Long> {

    private static final String PNE = " pne ";

    protected Class getPersistentClass() {
        return ProcessamentoEdi.class;
    }

    public void updateQtNotasProcessadasProcessamentoEdi(Long idProcessamentoEdi, Long nrNota){
        getAdsmHibernateTemplate().execute(session -> {
            StringBuilder hql = new StringBuilder("UPDATE ")
                    .append(getPersistentClass().getName()).append(" pe ")
                    .append(" SET pe.qtNotasProcessadas = (pe.qtNotasProcessadas+1) ")
                    .append(" WHERE exists( ")
                    .append(" select pne.processamentoEdi.idProcessamentoEdi from ").append(ProcessamentoNotaEdi.class.getName()).append(" pne ")
                    .append(" where pne.processamentoEdi.idProcessamentoEdi = :idProcessamentoEdi and pne.nrNotaFiscal = ").append(nrNota).append(")" )
                    .append(" and pe.idProcessamentoEdi = :idProcessamentoEdi and pe.qtNotasProcessadas < pe.qtTotalNotas");

            Query query = session.createQuery(hql.toString()).setLong("idProcessamentoEdi", idProcessamentoEdi);
            query.executeUpdate();
            return null;
        });
    }

    public List<Map<String, Object>> findByFilterProcessamentoEdi(Map<String, Object> criteria, String situacao){
        StringBuilder hql = new StringBuilder()
            .append(" SELECT new Map(")
                .append("pe.idProcessamentoEdi as idProcessamentoEdi, ")
                .append("pe.filial.idFilial as idFilial, ")
                .append("pe.clienteProcessamento.pessoa.nmPessoa as nomeClienteProcessamento, ")
                .append("pe.usuario.nmUsuario as usuario, ")
                .append("pe.qtNotasProcessadas as qtNotasProcessadas, ")
                .append("pe.qtTotalNotas as qtTotalNotas, ")
                .append("pe.dhProcessamento as dhProcessamento ")
            .append(") FROM ")
            .append(getPersistentClass().getName()).append(" pe ")
            .append(" WHERE nvl(pe.blVisivel, 'S') = 'S' ")
            .append(" AND pe.filial.idFilial = :idFilial ")
            .append(" AND TRUNC(pe.dhProcessamento.value) = TO_DATE(:dhProcessamento,'yyyy-MM-dd') ");
        if(criteria.containsKey("idCliente")) {
            hql.append(" AND pe.clienteProcessamento.idCliente = :idCliente ");
        }
        if(criteria.containsKey("idUsuario")) {
            hql.append(" AND pe.usuario.idUsuario = :idUsuario ");
        }
        if(criteria.containsKey("nrNotaFiscal")) {
            hql.append(" AND exists(SELECT 1 FROM ").append(ProcessamentoNotaEdi.class.getName()).append(PNE)
                    .append(" WHERE pe.idProcessamentoEdi = pne.processamentoEdi.idProcessamentoEdi")
                    .append(" AND pne.nrNotaFiscal = :nrNotaFiscal) ");
        }
        if(situacao != null) {
            hql.append(filterProcessamentoNotaEdi(situacao));
        }
        hql.append(" ORDER BY pe.dhProcessamento");

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), criteria);
    }

    private String filterProcessamentoNotaEdi(String situacao){
        StringBuilder hql = new StringBuilder();
        if(situacao.equals(SituacaoMonitoramentoProcesso.EM_PROCESSAMENTO.getSituacao())) {
            hql.append(" AND pe.qtNotasProcessadas < pe.qtTotalNotas ");
        }
        if(situacao.equals(SituacaoMonitoramentoProcesso.FINALIZADO.getSituacao())) {
            hql.append(" AND pe.qtNotasProcessadas = pe.qtTotalNotas ")
                    .append(" AND NOT EXISTS(SELECT 1 FROM ").append(ProcessamentoNotaEdi.class.getName()).append(PNE)
                    .append(" where pe.idProcessamentoEdi = pne.processamentoEdi.idProcessamentoEdi")
                    .append(" AND pne.dsMensagemErro IS NOT NULL) ");
        }
        if(situacao.equals(SituacaoMonitoramentoProcesso.FINALIZADO_ERRO.getSituacao())) {
            hql.append(" AND pe.qtNotasProcessadas = pe.qtTotalNotas ")
                    .append(" AND EXISTS(SELECT 1 FROM ").append(ProcessamentoNotaEdi.class.getName()).append(PNE)
                    .append(" where pe.idProcessamentoEdi = pne.processamentoEdi.idProcessamentoEdi")
                    .append(" AND pne.dsMensagemErro IS NOT NULL) ");
        }
       return hql.toString();
    }

    public ProcessamentoEdi findById(Long idProcessamentoEdi) {
        return super.findById(idProcessamentoEdi);
    }

    public void updateTpStatus(String nrProcessamento, DomainValue tpStatus){

        StringBuilder sql = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nrProcessamento", nrProcessamento);
        parameters.put("tpStatus", tpStatus.getValue());

        sql.append("UPDATE PROCESSAMENTO_EDI PD SET PD.TP_STATUS = :tpStatus WHERE EXISTS(");
        sql.append("    SELECT 1 FROM PROCESSAMENTO_NOTA_EDI PNE WHERE PNE.NR_PROCESSAMENTO = :nrProcessamento ");
        sql.append("        AND PD.ID_PROCESSAMENTO_EDI = PNE.ID_PROCESSAMENTO_EDI)");

        getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parameters);

    }

}
