package com.mercurio.lms.carregamento.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;

/**
 * Classe responsável pela geração do Relatório de Controle de Cargas - Coleta-Entrega Especificação técnica 05.01.01.03
 */
@SuppressWarnings({ "deprecation" })
public abstract class RelatorioControleCargaViagemService extends ReportServiceSupport {

    private OcorrenciaEntregaService ocorrenciaEntregaService;
    private ControleCargaService controleCargaService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public JRReportDataObject executeMountCabecalho(Map parameters) throws Exception {
        TypedFlatMap tfm = (TypedFlatMap) parameters;
        Long idControleCarga = tfm.getLong("idControleCarga");
        Long idManifesto = tfm.getLong("idManifesto");

        SqlTemplate sqlCabecalho = mountSqlCabecalho(idControleCarga, idManifesto);
        JRReportDataObject jrCabecalho = executeQuery(sqlCabecalho.getSql(), sqlCabecalho.getCriteria());

        Map parametersReport = new HashMap();

        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);

        jrCabecalho.setParameters(parametersReport);

        return jrCabecalho;
    }

    private SqlTemplate mountSqlCabecalho(Long idControleCarga, Long idManifesto) throws Exception {
        SqlTemplate sql = createSqlTemplate();

        BigDecimal valorTotal = controleCargaService.findValorTotalFrota(idControleCarga);

        sql.addProjection("CONTROLE_CARGA.ID_CONTROLE_CARGA", "ID_CONTROLE_CARGA");
        sql.addProjection("FILIAL.SG_FILIAL", "SG_FILIAL");
        sql.addProjection("CONTROLE_CARGA.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");

        sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
                + "			WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_CONTROLE_CARGAS') "
                + "					AND VL_VALOR_DOMINIO = CONTROLE_CARGA.TP_CONTROLE_CARGA)", "TP_CONTROLE_CARGA");

        sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
                + "			WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_CONTROLE_CARGA') "
                + "					AND VL_VALOR_DOMINIO = CONTROLE_CARGA.TP_STATUS_CONTROLE_CARGA)", "TP_STATUS_CONTROLE_CARGA");

        sql.addProjection("MEIO_TRANSPORTE.NR_FROTA", "FROTA_VEICULO");
        sql.addProjection("MEIO_TRANSPORTE.NR_IDENTIFICADOR", "IDENTIFICADOR_VEICULO");
        sql.addProjection("PROPRIETARIO_PESSOA.NM_PESSOA", "NM_PROPRIETARIO");
        sql.addProjection("MOTORISTA_PESSOA.NM_PESSOA", "NM_MOTORISTA");
        sql.addProjection("MOEDA.SG_MOEDA", "SG_MOEDA");
        sql.addProjection("MOEDA.DS_SIMBOLO", "DS_SIMBOLO");
        sql.addProjection(String.valueOf(valorTotal), "VL_TOTAL_FROTA");
        sql.addProjection("CONTROLE_CARGA.VL_FRETE_CARRETEIRO", "VL_FRETE_CARRETEIRO");
        sql.addProjection("CONTROLE_CARGA.NR_TEMPO_VIAGEM", "NR_TEMPO_VIAGEM");
        sql.addProjection("SEMI_REBOQUE.NR_FROTA", "FROTA_SEMI_REBOQUE");
        sql.addProjection("SEMI_REBOQUE.NR_IDENTIFICADOR", "IDENTIFICADOR_SEMI_REBOQUE");

        sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
                + "			WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_ROTA_VIAGEM_CC') "
                + "					AND VL_VALOR_DOMINIO = CONTROLE_CARGA.TP_ROTA_VIAGEM)", "TP_ROTA_VIAGEM");

        sql.addProjection("CASE WHEN CONTROLE_CARGA.TP_ROTA_VIAGEM = 'EV' " + "	THEN ROTA.DS_ROTA " + "	ELSE ROTA_ROTA_IDA_VOLTA.DS_ROTA END", "DS_ROTA");

        if (idManifesto != null) {
            sql.addProjection(idManifesto.toString(), "ID_MANIFESTO");
            sql.addProjection("MANIFESTO_VIAGEM_NACIONAL.NR_MANIFESTO_ORIGEM", "NR_MANIFESTO");
            sql.addProjection("FILIAL_MANIFESTO.SG_FILIAL", "SG_FILIAL_MANIFESTO");

            sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
                    + "			WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_ABRANGENCIA') "
                    + "					AND VL_VALOR_DOMINIO = MANIFESTO.TP_ABRANGENCIA)", "TP_ABRANGENCIA");

            sql.addProjection("CONTROLE_CARGA.PS_TOTAL_FROTA", "PS_TOTAL_FROTA");
            sql.addProjection("CONTROLE_CARGA.PS_TOTAL_AFORADO", "PS_TOTAL_AFORADO");
            sql.addProjection("CONTROLE_CARGA.DH_PREVISAO_SAIDA", "DH_PREVISAO_SAIDA");
        }
        
        // LMSA-6351
        sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO "
                + "          WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_CARGA_COMPARTILHADA') "
                + "                AND VL_VALOR_DOMINIO = SCONTRATACAO.TP_CARGA_COMPARTILHADA)", "DS_CARGA_COMPARTILHADA");


        sql.addInnerJoin("CONTROLE_CARGA CONTROLE_CARGA");
        sql.addInnerJoin("FILIAL FILIAL on CONTROLE_CARGA.ID_FILIAL_ORIGEM = FILIAL.ID_FILIAL");

        sql.addLeftOuterJoin("MEIO_TRANSPORTE MEIO_TRANSPORTE on CONTROLE_CARGA.ID_TRANSPORTADO = MEIO_TRANSPORTE.ID_MEIO_TRANSPORTE");
        sql.addLeftOuterJoin("MEIO_TRANSPORTE SEMI_REBOQUE on CONTROLE_CARGA.ID_SEMI_REBOCADO = SEMI_REBOQUE.ID_MEIO_TRANSPORTE");
        sql.addLeftOuterJoin("PESSOA PROPRIETARIO_PESSOA on CONTROLE_CARGA.ID_PROPRIETARIO = PROPRIETARIO_PESSOA.ID_PESSOA");
        sql.addLeftOuterJoin("PESSOA MOTORISTA_PESSOA on CONTROLE_CARGA.ID_MOTORISTA = MOTORISTA_PESSOA.ID_PESSOA");
        sql.addLeftOuterJoin("MOEDA MOEDA on CONTROLE_CARGA.ID_MOEDA = MOEDA.ID_MOEDA");

        sql.addLeftOuterJoin("ROTA_IDA_VOLTA ROTA_IDA_VOLTA on CONTROLE_CARGA.ID_ROTA_IDA_VOLTA = ROTA_IDA_VOLTA.ID_ROTA_IDA_VOLTA ");
        sql.addLeftOuterJoin("ROTA ROTA_ROTA_IDA_VOLTA on ROTA_IDA_VOLTA.ID_ROTA = ROTA_ROTA_IDA_VOLTA.ID_ROTA ");
        sql.addLeftOuterJoin("ROTA ROTA on CONTROLE_CARGA.ID_ROTA = ROTA.ID_ROTA ");

        // LMSA-6351
        sql.addLeftOuterJoin("SOLICITACAO_CONTRATACAO SCONTRATACAO on CONTROLE_CARGA.ID_SOLICITACAO_CONTRATACAO = SCONTRATACAO.ID_SOLICITACAO_CONTRATACAO ");
        
        if (idManifesto != null) {
            sql.addInnerJoin("MANIFESTO MANIFESTO on MANIFESTO.ID_CONTROLE_CARGA = CONTROLE_CARGA.ID_CONTROLE_CARGA ");
            sql.addLeftOuterJoin(
                    "MANIFESTO_VIAGEM_NACIONAL MANIFESTO_VIAGEM_NACIONAL on MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL = MANIFESTO.ID_MANIFESTO  ");
            sql.addLeftOuterJoin("FILIAL FILIAL_MANIFESTO on MANIFESTO_VIAGEM_NACIONAL.ID_FILIAL = FILIAL_MANIFESTO.ID_FILIAL");

        }

        sql.addCriteria("CONTROLE_CARGA.ID_CONTROLE_CARGA", "=", idControleCarga);
        if (idManifesto != null) {
            sql.addCriteria("MANIFESTO.ID_MANIFESTO", "=", idManifesto);
            sql.addCriteria("MANIFESTO.TP_MANIFESTO ", "=", ConstantesEntrega.TP_MANIFESTO_VIAGEM);

        }

        return sql;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JRDataSource executeMountManifestos(Long idControleCarga) throws Exception {
        StringBuilder sqlManifestos = new StringBuilder(mountSqlManifestos()).append(" UNION ALL ").append(mountSqlManifestosFedex());
        Object[] criteriaManifestos = { idControleCarga, idControleCarga };
        JRReportDataObject jrReportDataObject = executeQuery(sqlManifestos.toString(), criteriaManifestos);
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        jrReportDataObject.setParameters(parametersReport);

        return jrReportDataObject.getDataSource();
    }

    private String mountSqlManifestos() throws Exception {
        StringBuilder sql = new StringBuilder();

        StringBuilder projection = new StringBuilder(" select ");
        StringBuilder join = new StringBuilder(" from ");
        StringBuilder criteria = new StringBuilder(" where ");
        StringBuilder orderBy = new StringBuilder(" order by ");

        // LMSA-6268: LMSA-6378
        projection.append(" 'T' AS ORIGEM_DADOS, ");
        
        projection.append(" MANIFESTO.ID_CONTROLE_CARGA as ID_CONTROLE_CARGA, ");
        projection.append(" MANIFESTO.ID_MANIFESTO as ID_MANIFESTO, ");

        projection.append(" MANIFESTO_VIAGEM_NACIONAL.NR_MANIFESTO_ORIGEM as NR_MANIFESTO,");

        projection.append(" FILIAL_ORIGEM_MAN_ENTR.SG_FILIAL as FILIAL_ORIGEM_MANIFESTO, ");

        projection.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
                .append("WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_MANIFESTO_VIAGEM') ")
                .append("AND VL_VALOR_DOMINIO = MANIFESTO.TP_MANIFESTO_VIAGEM)").append(" as TP_MANIFESTO_VIAGEM, ");

        projection.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
                .append("WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_ABRANGENCIA') ")
                .append("AND VL_VALOR_DOMINIO = MANIFESTO.TP_ABRANGENCIA)").append("as TP_ABRANGENCIA, ");

        projection.append("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO ")
                .append(" WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_MANIFESTO') ")
                .append(" AND VL_VALOR_DOMINIO = MANIFESTO.TP_STATUS_MANIFESTO)").append(" as TP_STATUS_MANIFESTO, ");

        projection.append("(SELECT SUM(DOCTO_SERVICO.PS_REAL) FROM DOCTO_SERVICO DOCTO_SERVICO, MANIFESTO_NACIONAL_CTO MANIFESTO_NACIONAL_CTO ")
                .append(" WHERE MANIFESTO_NACIONAL_CTO.ID_CONHECIMENTO = DOCTO_SERVICO.ID_DOCTO_SERVICO ")
                .append(" AND MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL ) ")
                .append(" as PS_TOTAL_MANIFESTO, ");

        projection.append("(SELECT SUM(DOCTO_SERVICO.ps_aforado) FROM DOCTO_SERVICO DOCTO_SERVICO, MANIFESTO_NACIONAL_CTO MANIFESTO_NACIONAL_CTO ")
                .append(" WHERE MANIFESTO_NACIONAL_CTO.ID_CONHECIMENTO = DOCTO_SERVICO.ID_DOCTO_SERVICO ")
                .append(" AND MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL) ")
                .append(" as PS_TOTAL_AFORADO_MANIFESTO, ");

        projection.append(" MOEDA_MANIFESTO_ENTREGA.SG_MOEDA as SG_MOEDA, ");
        projection.append(" MOEDA_MANIFESTO_ENTREGA.DS_SIMBOLO as DS_SIMBOLO, ");

        projection.append("(SELECT SUM(DOCTO_SERVICO.vl_mercadoria) FROM DOCTO_SERVICO DOCTO_SERVICO, MANIFESTO_NACIONAL_CTO MANIFESTO_NACIONAL_CTO ")
                .append(" WHERE MANIFESTO_NACIONAL_CTO.ID_CONHECIMENTO = DOCTO_SERVICO.ID_DOCTO_SERVICO ")
                .append(" AND MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL = MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL) ")
                .append(" as VL_TOTAL_MANIFESTO");

        join.append("MANIFESTO MANIFESTO ");
        join.append(
                "LEFT OUTER JOIN MANIFESTO_VIAGEM_NACIONAL MANIFESTO_VIAGEM_NACIONAL on MANIFESTO.ID_MANIFESTO = MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL ");
        join.append("LEFT OUTER JOIN FILIAL FILIAL_ORIGEM_MAN_ENTR on MANIFESTO.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_MAN_ENTR.ID_FILIAL  ");
        join.append("LEFT OUTER JOIN MOEDA MOEDA_MANIFESTO_ENTREGA on MANIFESTO.ID_MOEDA = MOEDA_MANIFESTO_ENTREGA.ID_MOEDA ");

        criteria.append("MANIFESTO.TP_MANIFESTO = '").append(ConstantesEntrega.TP_MANIFESTO_VIAGEM).append("' ");
        criteria.append("and MANIFESTO.ID_CONTROLE_CARGA = ?");

        orderBy.append(" NR_MANIFESTO ASC ");

        sql.append(projection).append(join).append(criteria);//.append(orderBy);

        return sql.toString();
    }

    /**
     * LMSA-6268: LMSA-6378
     * @return SQL
     * @throws Exception
     */
    private String mountSqlManifestosFedex() throws Exception {
        StringBuilder sql = new StringBuilder();

        StringBuilder projection = new StringBuilder(" select ");
        StringBuilder join = new StringBuilder(" from ");
        StringBuilder criteria = new StringBuilder(" where ");
        StringBuilder groupBy = new StringBuilder(" group by ");
        StringBuilder orderBy = new StringBuilder(" order by ");

        projection.append(" 'F' AS ORIGEM_DADOS, ");
        
        projection.append(" cf.id_controle_carga as ID_CONTROLE_CARGA, ");
        projection.append(" cf.id_controle_carga as ID_MANIFESTO, ");

        projection.append(" to_number(cf.nr_manifesto_viagem_fedex) as NR_MANIFESTO, ");

        projection.append(" substr(cf.nr_manifesto_viagem_fedex,3,4) as FILIAL_ORIGEM_MANIFESTO, ");

        projection.append(" 'Integração' as TP_MANIFESTO_VIAGEM, ");

        projection.append(" 'Nacional' as TP_ABRANGENCIA, ");

        projection.append("VI18N(VD.DS_VALOR_DOMINIO_I) AS TP_STATUS_MANIFESTO, ");

        projection.append(" SUM(cf.ps_real) as PS_TOTAL_MANIFESTO, ");

        projection.append(" SUM(cf.ps_cubado) as PS_TOTAL_AFORADO_MANIFESTO, ");

        projection.append(" md.sg_moeda as SG_MOEDA, ");
        projection.append(" md.ds_simbolo as DS_SIMBOLO, ");

        projection.append(" SUM(cf.vl_mercadoria) as VL_TOTAL_MANIFESTO ");

        join.append("conhecimento_fedex cf ");
        join.append("INNER JOIN moeda md ON md.id_moeda = cf.tp_moeda ");
        join.append("INNER JOIN controle_carga cc ON cc.id_controle_carga = cf.id_controle_carga ");
        
        join.append("LEFT JOIN VALOR_DOMINIO VD ON VL_VALOR_DOMINIO = cc.tp_status_controle_carga AND ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_CONTROLE_CARGA' ) ");

        criteria.append(" cf.id_controle_carga = ? ");

        groupBy.append(" cf.id_controle_carga, to_number(cf.nr_manifesto_viagem_fedex), SUBSTR(cf.nr_manifesto_viagem_fedex,3,4), VI18N(VD.DS_VALOR_DOMINIO_I), md.sg_moeda, md.ds_simbolo ");

        orderBy.append(" ORIGEM_DADOS ASC, NR_MANIFESTO ASC ");

        sql.append(projection).append(join).append(criteria).append(groupBy).append(orderBy);

        return sql.toString();
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public JRDataSource executeMountDocumentosNacionais(Long idControleCarga, Long idManifesto, String origemDados) throws Exception {
        StringBuilder sqlManifestos = new StringBuilder(mountSqlDocumentosNacionais()).append(" UNION ALL ").append(mountSqlDocumentosNacionaisFromConhecimentoFedex());
        Object[] criteriaManifestos = { idControleCarga, idManifesto, origemDados, idControleCarga, origemDados };
        JRReportDataObject jrReportDataObject = executeQuery(sqlManifestos.toString(), criteriaManifestos);
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        jrReportDataObject.setParameters(parametersReport);

        return jrReportDataObject.getDataSource();
    }

    private String mountSqlDocumentosNacionais() {

        StringBuilder sql = new StringBuilder();

        StringBuilder projection = new StringBuilder(" select ");
        StringBuilder join = new StringBuilder(" from ");
        StringBuilder criteria = new StringBuilder(" where ");

        // LMSA-6268: LMSA-6378
        projection.append(" 'T' AS ORIGEM_DADOS, ");
        
        projection.append(" DOCTO_SERVICO.TP_DOCUMENTO_SERVICO as TP_DOCUMENTO_SERVICO, ");
        projection.append(" FILIAL_ORIGEM.SG_FILIAL as SG_FILIAL_ORIGEM, ");
        projection.append(" DOCTO_SERVICO.NR_DOCTO_SERVICO as NR_DOCTO_SERVICO, ");
        projection.append(" DOCTO_SERVICO.DT_PREV_ENTREGA as OTD, ");
        projection.append(" FILIAL_DESTINO.SG_FILIAL as SG_FILIAL_DESTINO, ");
        projection.append(" DOCTO_SERVICO.DH_EMISSAO as DH_EMISSAO, ");
        projection.append(" PESSOA_REMETENTE.NM_PESSOA as NM_PESSOA_REMETENTE, ");
        projection.append(" PESSOA_DESTINATARIO.NM_PESSOA as NM_PESSOA_DESTINATARIO, ");
        projection.append(" SERVICO.SG_SERVICO as SG_SERVICO, ");
        projection.append(" MUNICIPIO.NM_MUNICIPIO as NM_MUNICIPIO, ");
        projection.append(" NVL((SELECT ROTA_COLETA_ENTREGA.NR_ROTA||' '||ROTA_COLETA_ENTREGA.DS_ROTA FROM ROTA_COLETA_ENTREGA ")
                .append(" WHERE DOCTO_SERVICO.ID_ROTA_COLETA_ENTREGA_REAL = ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA), ")
                .append(" (SELECT ROTA_COLETA_ENTREGA.NR_ROTA||' '||ROTA_COLETA_ENTREGA.DS_ROTA FROM ROTA_COLETA_ENTREGA ")
                .append(" WHERE DOCTO_SERVICO.ID_ROTA_COLETA_ENTREGA_SUGERID = ROTA_COLETA_ENTREGA.ID_ROTA_COLETA_ENTREGA)) as DS_ROTA_COLETA_ENTREGA, ");
        projection.append(" DOCTO_SERVICO.QT_VOLUMES as QT_VOLUMES, ");
        projection.append(" DOCTO_SERVICO.PS_REAL as PS_REAL, ");
        projection.append(" DOCTO_SERVICO.PS_AFERIDO as PS_AFERIDO, ");
        projection.append(" DOCTO_SERVICO.PS_AFORADO as PS_AFORADO, ");
        projection.append(" DOCTO_SERVICO.PS_REFERENCIA_CALCULO as PS_REFERENCIA_CALCULO, ");
        projection.append(" MOEDA.SG_MOEDA as SG_MOEDA, ");
        projection.append(" MOEDA.DS_SIMBOLO as DS_SIMBOLO, ");
        projection.append(" DOCTO_SERVICO.VL_MERCADORIA as VL_MERCADORIA, ");
        projection.append(" DOCTO_SERVICO.VL_FRETE_LIQUIDO as VL_FRETE_LIQUIDO, ");
        projection.append(" MANIFESTO.TP_MANIFESTO_VIAGEM as TP_MANIFESTO_VIAGEM, ");
        projection.append(" DOCTO_SERVICO.ID_DOCTO_SERVICO as ID_DOCTO_SERVICO, ");
        projection.append(" (SELECT AGENDAMENTO_ENTREGA.DT_AGENDAMENTO FROM AGENDAMENTO_ENTREGA, AGENDAMENTO_DOCTO_SERVICO ")
                .append(" WHERE DOCTO_SERVICO.ID_DOCTO_SERVICO = AGENDAMENTO_DOCTO_SERVICO.ID_DOCTO_SERVICO ")
                .append(" AND AGENDAMENTO_DOCTO_SERVICO.ID_AGENDAMENTO_ENTREGA = AGENDAMENTO_ENTREGA.ID_AGENDAMENTO_ENTREGA ")
                .append(" AND AGENDAMENTO_ENTREGA.TP_SITUACAO_AGENDAMENTO = 'A') as DT_AGENDAMENTO");

        join.append("MANIFESTO MANIFESTO ");

        join.append(
                "INNER JOIN MANIFESTO_VIAGEM_NACIONAL MANIFESTO_VIAGEM_NACIONAL on MANIFESTO.ID_MANIFESTO = MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL ");
        join.append(
                "INNER JOIN MANIFESTO_NACIONAL_CTO MANIFESTO_NACIONAL_CTO on MANIFESTO_VIAGEM_NACIONAL.ID_MANIFESTO_VIAGEM_NACIONAL = MANIFESTO_NACIONAL_CTO.ID_MANIFESTO_VIAGEM_NACIONAL ");

        join.append("INNER JOIN DOCTO_SERVICO DOCTO_SERVICO on MANIFESTO_NACIONAL_CTO.ID_CONHECIMENTO = DOCTO_SERVICO.ID_DOCTO_SERVICO ");
        join.append("INNER JOIN FILIAL FILIAL_ORIGEM on DOCTO_SERVICO.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL ");
        join.append("INNER JOIN FILIAL FILIAL_DESTINO on DOCTO_SERVICO.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL ");
        join.append("INNER JOIN SERVICO SERVICO on DOCTO_SERVICO.ID_SERVICO = SERVICO.ID_SERVICO ");
        join.append("INNER JOIN CONHECIMENTO on DOCTO_SERVICO.ID_DOCTO_SERVICO = CONHECIMENTO.ID_CONHECIMENTO ");
        join.append("INNER JOIN MUNICIPIO on CONHECIMENTO.ID_MUNICIPIO_ENTREGA = MUNICIPIO.ID_MUNICIPIO ");
        join.append("INNER JOIN PESSOA PESSOA_REMETENTE on DOCTO_SERVICO.ID_CLIENTE_REMETENTE = PESSOA_REMETENTE.ID_PESSOA ");
        join.append("INNER JOIN PESSOA PESSOA_DESTINATARIO on DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO = PESSOA_DESTINATARIO.ID_PESSOA ");
        join.append("INNER JOIN MOEDA MOEDA on DOCTO_SERVICO.ID_MOEDA = MOEDA.ID_MOEDA ");

        criteria.append("MANIFESTO.TP_MANIFESTO = '").append(ConstantesEntrega.TP_MANIFESTO_VIAGEM).append("' ");
        criteria.append("and MANIFESTO.ID_CONTROLE_CARGA = ? ");
        criteria.append("and MANIFESTO.ID_MANIFESTO = ? ");
        
        // LMSA-6268: LMSA-6378: LMSA-7214
        criteria.append("and ? IN ('T','E') ");

        sql.append(projection).append(join).append(criteria);

        return sql.toString();
    }

    /**
     *  LMSA-6268: LMSA-6378
     * @return
     */
    private String mountSqlDocumentosNacionaisFromConhecimentoFedex() {

        StringBuilder sql = new StringBuilder();

        StringBuilder projection = new StringBuilder(" select ");
        StringBuilder join = new StringBuilder(" from ");
        StringBuilder criteria = new StringBuilder(" where ");
        StringBuilder orderBy = new StringBuilder(" order by ");

        projection.append(" 'F' AS ORIGEM_DADOS, ");
        
        projection.append(" c.TP_DOCUMENTO as TP_DOCUMENTO_SERVICO, ");
        projection.append(" c.sg_filial_origem as SG_FILIAL_ORIGEM, ");
        projection.append(" to_number(c.nr_conhecimento) as NR_DOCTO_SERVICO, ");
        projection.append(" c.dt_prev_entrega as OTD, ");
        projection.append(" c.sg_filial_destino as SG_FILIAL_DESTINO, ");
        projection.append(" c.dt_emissao as DH_EMISSAO, ");
        projection.append(" c.nm_remetente as NM_PESSOA_REMETENTE, ");
        projection.append(" c.nm_destinatario as NM_PESSOA_DESTINATARIO, ");
        projection.append(" 'RNC' as SG_SERVICO, ");
        projection.append(" null as NM_MUNICIPIO, ");
        projection.append(" null as DS_ROTA_COLETA_ENTREGA, ");
        projection.append(" c.QT_VOLUMES as QT_VOLUMES, ");
        projection.append(" c.PS_REAL as PS_REAL, ");
        projection.append(" c.PS_AFERIDO as PS_AFERIDO, ");
        projection.append(" c.PS_CUBADO as PS_AFORADO, ");
        projection.append(" null as PS_REFERENCIA_CALCULO, ");
        projection.append(" m.SG_MOEDA as SG_MOEDA, ");
        projection.append(" m.DS_SIMBOLO as DS_SIMBOLO, ");
        projection.append(" c.VL_MERCADORIA as VL_MERCADORIA, ");
        projection.append(" c.PS_CALCULO_FRETE as VL_FRETE_LIQUIDO, ");
        projection.append(" null as TP_MANIFESTO_VIAGEM, ");
        projection.append(" c.ID_DOCTO_SERVICO as ID_DOCTO_SERVICO, ");
        projection.append(" null as DT_AGENDAMENTO");

        join.append("CONHECIMENTO_FEDEX c ");

        join.append("INNER JOIN CONTROLE_CARGA cc on cc.ID_CONTROLE_CARGA = c.ID_CONTROLE_CARGA ");
        join.append("INNER JOIN MOEDA m on m.ID_MOEDA = cc.ID_MOEDA ");

        criteria.append("c.ID_CONTROLE_CARGA = ? ");
        // LMSA-6268: LMSA-6378: LMSA-7214
        criteria.append(" and ? IN ('F','E') ");

        orderBy.append(" ORIGEM_DADOS ");
        
        sql.append(projection).append(join).append(criteria);

        return sql.toString();
    }


    
    public String findOcorrenciaEntrega(Long idDoctoServico) throws Exception {
        OcorrenciaEntrega oe = ocorrenciaEntregaService.findOcorrenciaEntregaNaoCanceladaByIdDoctoServico(idDoctoServico);
        return oe == null ? null : oe.getDsOcorrenciaEntrega().toString();
    }

    public OcorrenciaEntregaService getOcorrenciaEntregaService() {
        return ocorrenciaEntregaService;
    }

    public void setOcorrenciaEntregaService(OcorrenciaEntregaService ocorrenciaEntregaService) {
        this.ocorrenciaEntregaService = ocorrenciaEntregaService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }
    
}