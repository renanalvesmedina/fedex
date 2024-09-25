package com.mercurio.lms.sim.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.coleta.model.PedidoColeta;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoColetaDAO extends BaseCrudDao<PedidoColeta, Long> {
    @Override
    protected Class getPersistentClass() {
        return PedidoColeta.class;
    }

    public List<Object[]> findEventoColetaRealizadaBosch(Long idEventoDocumentoServico, String clienteIntegracao){

        StringBuilder query = new StringBuilder();
        query.append("SELECT NR_NOTA_FISCAL AS numeroNotaFiscaL,  DS_SERIE AS serieNotaFiscal, ");
        query.append("TO_CHAR(DH_PEDIDO_COLETA, 'yyyyMMdd') dataSaida, ");
        query.append("TO_CHAR(DH_PEDIDO_COLETA,'HH24MISS') horaSaida, ");
        query.append("'FedEx' AS codigoTransportadora, ");
        query.append("TO_CHAR(DT_PREV_ENTREGA, 'yyyyMMdd') AS dataPrevisaoentrega, ");
        query.append("'FEDEX BRASIL LOGISTICA E TRANS' AS Raz, ");
        query.append("NR_DOCTO_SERVICO AS numeroCte, ");
        query.append("NR_CHAVE AS chaveAcessoNfe, ");
        query.append("CD_COLETA_CLIENTE AS codigoColetaCliente ");
        query.append("FROM PEDIDO_COLETA PC, DOCTO_SERVICO DOS, NOTA_FISCAL_CONHECIMENTO NFC, EVENTO_DOCUMENTO_SERVICO EDS ");
        query.append("WHERE PC.ID_PEDIDO_COLETA = DOS.ID_PEDIDO_COLETA ");
        query.append("AND DOS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO ");
        query.append("AND DOS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO ");
        query.append("AND EDS.ID_EVENTO_DOCUMENTO_SERVICO = :idEventoDocumentoServico ");
        query.append("AND PC.NM_CLIENTE_INTEGRACAO = :nmClienteIntegracao");

        Map<String, Object> param = new HashMap<>();
        param.put("idEventoDocumentoServico", idEventoDocumentoServico);
        param.put("nmClienteIntegracao", clienteIntegracao);

        List<Object[]> eventoColeta = getAdsmHibernateTemplate()
                .findBySql(query.toString(), param, configureSqlQueryColetaRealizada());

        return eventoColeta;
    }

    private ConfigureSqlQuery configureSqlQueryColetaRealizada(){
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {

                sqlQuery.addScalar("numeroNotaFiscaL", Hibernate.LONG);
                sqlQuery.addScalar("serieNotaFiscal", Hibernate.STRING);
                sqlQuery.addScalar("dataSaida", Hibernate.STRING);
                sqlQuery.addScalar("horaSaida", Hibernate.STRING);
                sqlQuery.addScalar("codigoTransportadora", Hibernate.STRING);
                sqlQuery.addScalar("dataPrevisaoentrega", Hibernate.STRING);
                sqlQuery.addScalar("Raz", Hibernate.STRING);
                sqlQuery.addScalar("numeroCte", Hibernate.LONG);
                sqlQuery.addScalar("chaveAcessoNfe", Hibernate.STRING);
                sqlQuery.addScalar("codigoColetaCliente", Hibernate.STRING);
            }
        };
    }

    public PedidoColeta findEventoAceiteColetaBosch(Long idPedidoColeta, Long idEventoColeta){

        StringBuilder sql = new StringBuilder();
        sql.append(" select pc ");
        sql.append(" from " + getPersistentClass().getName() + " pc ");
        sql.append(" inner join fetch pc.eventoColetas ec ");
        sql.append(" where pc.idPedidoColeta = :idPedidoColeta ");
        sql.append("and ec.idEventoColeta = :idEventoColeta ");
        sql.append("and pc.nmClienteIntegracao = :nmClienteIntegracao");

        Map criteria = new HashMap();
        criteria.put("idPedidoColeta", idPedidoColeta);
        criteria.put("idEventoColeta", idEventoColeta);
        criteria.put("nmClienteIntegracao", "BOSCH");

        return (PedidoColeta)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);

    }
}
