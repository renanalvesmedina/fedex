package com.mercurio.lms.carregamento.model.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.ResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EventoControleCarga;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class EventoControleCargaDAO extends BaseCrudDao<EventoControleCarga, Long> {

    private static final String TP_EVENTO_CONTROLE_CARGA_LIBERACAO_RISCO = "LR";
    private static final String HQL_USUARIO = "usuario";
    private static final String HQL_USUARIO_SOLICITANTE = "usuarioSolicitacao";
    private static final String HQL_USUARIO_APROVADOR = "usuarioAprovador";
    private static final String HQL_FILIAL = "filial";
    private static final String HQL_MEIO_TRANSPORTE = "meioTransporte";
    private static final String HQL_MOEDA = "moeda";
    private static final String HQL_EQUIPE_OPERACAO = "equipeOperacao";
    private static final String HQL_TP_EVENTO_CONTROLE_CARGA = "tpEventoControleCarga";
    private static final String HQL_CONTROLE_CARGA = "controleCarga";

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    @SuppressWarnings("rawtypes")
    protected final Class getPersistentClass() {
        return EventoControleCarga.class;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void initFindPaginatedLazyProperties(Map map) {
    	map.put(HQL_USUARIO, FetchMode.JOIN);
    	map.put(HQL_USUARIO_SOLICITANTE, FetchMode.JOIN);
    	map.put(HQL_USUARIO_APROVADOR, FetchMode.JOIN);
        map.put(HQL_FILIAL, FetchMode.JOIN);
    }

    /**
     * Método find especifico para consulta liberação de risco (E.T. 02.03.01.16),
     * busca no intervalo de datas os resultados com o TP_EVENTO_CONTROLE_CARGA = LR
     *
     * @param idMeioTransporte
     * @param dataEventoInicial
     * @param dataEventoFinal
     * @param findDefinition
     * @return
     */
    @SuppressWarnings("rawtypes")
    public ResultSetPage findPaginatedConsultaLiberacaoRisco(Long idMeioTransporte, YearMonthDay dataEventoInicial, YearMonthDay dataEventoFinal, FindDefinition findDefinition) {
        DetachedCriteria dc = DetachedCriteria.forClass(EventoControleCarga.class);
        // seta os fetchs necessários para a consulta
        dc.setFetchMode(HQL_MOEDA, FetchMode.JOIN);
        dc.setFetchMode(HQL_USUARIO, FetchMode.JOIN);
        dc.setFetchMode(HQL_USUARIO_SOLICITANTE, FetchMode.JOIN);
        dc.setFetchMode(HQL_USUARIO_APROVADOR, FetchMode.JOIN);
        dc.setFetchMode(HQL_MEIO_TRANSPORTE, FetchMode.JOIN);
        dc.setFetchMode(HQL_EQUIPE_OPERACAO, FetchMode.JOIN);
        dc.setFetchMode(HQL_CONTROLE_CARGA, FetchMode.JOIN);

        if (idMeioTransporte != null) {
            dc.createAlias(HQL_MEIO_TRANSPORTE, "mt");
            dc.add(Restrictions.eq("mt.idMeioTransporte", idMeioTransporte));
        }
        dc.add(Restrictions.eq(HQL_TP_EVENTO_CONTROLE_CARGA, TP_EVENTO_CONTROLE_CARGA_LIBERACAO_RISCO));
        dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_EVENTO AS DATE)) >= ?", dataEventoInicial, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
        dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_EVENTO AS DATE)) <= ?", dataEventoFinal, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));

        return super.findPaginatedByDetachedCriteria(dc, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }

    /**
     * Método rowCount para consulta liberação de risco (E.T. 02.03.01.16).     *
     *
     * @param idMeioTransporte
     * @param dataEventoInicial
     * @param dataEventoFinal
     * @param findDefinition
     * @return
     */
    @SuppressWarnings("rawtypes")
    public Integer getRowCountConsultaLiberacaoRisco(Long idMeioTransporte, YearMonthDay dataEventoInicial, YearMonthDay dataEventoFinal, FindDefinition findDefinition) {
        DetachedCriteria dc = DetachedCriteria.forClass(EventoControleCarga.class);

        dc.setProjection(Projections.rowCount());
        dc.setFetchMode(HQL_USUARIO, FetchMode.JOIN);
        dc.setFetchMode(HQL_MEIO_TRANSPORTE, FetchMode.JOIN);
        dc.setFetchMode(HQL_EQUIPE_OPERACAO, FetchMode.JOIN);

        if (idMeioTransporte != null) {
            dc.createAlias(HQL_MEIO_TRANSPORTE, "mt");
            dc.add(Restrictions.eq("mt.idMeioTransporte", idMeioTransporte));
        }

        dc.add(Restrictions.eq(HQL_TP_EVENTO_CONTROLE_CARGA, TP_EVENTO_CONTROLE_CARGA_LIBERACAO_RISCO));
        dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_EVENTO AS DATE)) >= ?", dataEventoInicial, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));
        dc.add(Restrictions.sqlRestriction("TRUNC(CAST({alias}.DH_EVENTO AS DATE)) <= ?", dataEventoFinal, Hibernate.custom(JodaTimeYearMonthDayUserType.class)));

        // seta os fetchs necessários para a consulta
        List result = super.findByDetachedCriteria(dc);
        return (Integer) result.get(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void initFindByIdLazyProperties(Map map) {
        map.put(HQL_MEIO_TRANSPORTE, FetchMode.JOIN);
        map.put(HQL_EQUIPE_OPERACAO, FetchMode.JOIN);
    }


    /**
     * Retorna Lista de Eventos e Controle de Carga pelo ID da filial, ID do controle de carga
     * e tipo evento de controle de carga.
     *
     * @param idFilial
     * @param idControleCarga
     * @param tpEvento
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(Long idFilial, Long idControleCarga, String tpEvento) {
        DetachedCriteria dc = DetachedCriteria.forClass(EventoControleCarga.class);

        if (idFilial != null) {
            dc.add(Restrictions.eq("filial.id", idFilial));
        }
        dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
        dc.add(Restrictions.eq(HQL_TP_EVENTO_CONTROLE_CARGA, tpEvento));

        return super.findByDetachedCriteria(dc);
    }

    @SuppressWarnings("unchecked")
    public List<EventoControleCarga> findEventoControleCargaByIdFilialByIdControleCarga(TypedFlatMap criteria) {
        Long idControleCarga = criteria.getLong("idControleCarga");
        Long idFilial = criteria.getLong("idFilial");
        return this.findEventoControleCargaByIdFilialByIdControleCargaByTpEvento(idFilial, idControleCarga, "FD");
    }
    
    // LMSA-6159 LMSA-6249
    private static final String FRAGMENTO_SQL_FROM_EVENTO_DOCUMENTO = 
            "CONTROLE_CARGA c, MANIFESTO m, PRE_MANIFESTO_DOCUMENTO p, EVENTO_DOCUMENTO_SERVICO ed, EVENTO e ";
    private static final String FRAGMENTO_SQL_WHERE_COUNT_EVENTO_DOCUMENTO =
            "AND m.id_controle_carga = c.id_controle_carga "
            + "AND p.id_manifesto = m.id_manifesto "
            + "AND ed.id_docto_servico = p.id_docto_servico "
            + "AND e.id_evento = ed.id_evento "
            + "AND E.cd_evento IN (:cdsEventosReenvNotfis) "
            + "AND c.id_controle_carga = :idControleCarga ";

    private static final String FRAGMENTO_SQL_FIELDS_EVENTO_DOCUMENTO =
            "ed.ID_DOCTO_SERVICO, m.ID_FILIAL_ORIGEM, m.ID_FILIAL_DESTINO, e.cd_evento, ed.ID_FILIAL ";

    /**
     * Verificar se existe Eventos vinculados aos documentos de um determinado controle de carga
     * @param idControleCarga 
     * @return Integer
     * @author ernani.brandao
     */
    public Integer countReSendEventoDocumentoByControleCarga(Long idControleCarga, List<Short> cdsEventosReenvNotfis) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(FRAGMENTO_SQL_FROM_EVENTO_DOCUMENTO)
                .append(" WHERE 1=1 ")
                .append(FRAGMENTO_SQL_WHERE_COUNT_EVENTO_DOCUMENTO);
        
        Session session = super.getAdsmHibernateTemplate().getSessionFactory().openSession();
        
        try {
            SQLQuery query = session.createSQLQuery(sql.toString());
            query.setParameterList("cdsEventosReenvNotfis", cdsEventosReenvNotfis);
            query.setParameter("idControleCarga", idControleCarga);
            
            BigDecimal count = (BigDecimal) query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Recuperar todos os eventos de documentos associados ao controle de carga informado
     * Ira retornar uma lista de EventoDocumentoServicoDMN
     * @param idControleCarga
     * @return List<EventoDocumentoServicoDMN>
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
    public List<EventoDocumentoServicoDMN> getReSendEventoDocumentoByControleCarga(Long idControleCarga, List<Short> cdsEventosReenvNotfis) {
        StringBuilder sql = new StringBuilder("SELECT ")
                .append(FRAGMENTO_SQL_FIELDS_EVENTO_DOCUMENTO)
                .append(" FROM ")
                .append(FRAGMENTO_SQL_FROM_EVENTO_DOCUMENTO)
                .append(" WHERE 1=1 ")
                .append(FRAGMENTO_SQL_WHERE_COUNT_EVENTO_DOCUMENTO);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cdsEventosReenvNotfis", cdsEventosReenvNotfis);
        params.put("idControleCarga", idControleCarga);
        
        List<EventoDocumentoServicoDMN> result = (List<EventoDocumentoServicoDMN>) 
                super.getAdsmHibernateTemplate().findBySql(
                        sql.toString(), 
                    params, 
                    new ConfigureSqlQuery() {
                        @Override
                        public void configQuery(SQLQuery sqlQuery) {
                            sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
                            sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
                            sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG);
                            sqlQuery.addScalar("CD_EVENTO", Hibernate.SHORT);
                            sqlQuery.addScalar("ID_FILIAL", Hibernate.LONG);
                        }
                    },
                    new ResultTransformer() {
                        @Override
                        public EventoDocumentoServicoDMN transformTuple(Object[] valores, String[] alias) {
                            EventoDocumentoServicoDMN obj = new EventoDocumentoServicoDMN();
                            obj.setIdDoctoServico((Long) valores[0]);
                            obj.setIdFilialOrigem((Long) valores[1]);
                            obj.setIdFilialDestino((Long) valores[2]);
                            obj.setCdEvento((Short) valores[3]);
                            obj.setIdFilialEvento((Long) valores[4]);
                            return obj;
                        }
                        @Override
                        public List transformList(List list) {
                            return list;
                        }
                    }
                    );
        
        return result;
    }
    
}
