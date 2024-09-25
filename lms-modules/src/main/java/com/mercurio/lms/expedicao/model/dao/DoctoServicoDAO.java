package com.mercurio.lms.expedicao.model.dao;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CONHECIMENTO_NACIONAL;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.tntbrasil.integracao.domains.expedicao.DoctoServicoSaltoDMN;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.core.model.hibernate.SimNaoType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.util.AliasToNestedBeanResultTransformer;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.CentralizadoraFaturamento;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.WhiteList;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente;
import com.mercurio.lms.contasreceber.model.param.DoctoServicoParam;
import com.mercurio.lms.contasreceber.model.param.RelacaoDocumentoServicoDepositoParam;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoInternacCto;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadraoDocto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.DocumentoServicoRetirada;
import com.mercurio.lms.sim.model.Evento;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.RegistroPriorizacaoDocto;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplica??o
 * atrav?s do suporte ao Hibernate em conjunto com o Spring.
 * N?o inserir documenta??o ap?s ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class DoctoServicoDAO extends BaseCrudDao<DoctoServico, Long> {

    private static final String WHERE = "WHERE ";

    public List<DoctoServico> findByIdControleCarga(Long idControleCarga) {
        return findByIdControleCargaAndIdCliente(idControleCarga, null);
    }
    
    @SuppressWarnings("unchecked")
    public List<DoctoServico> findByIdControleCargaAndIdCliente(Long idControleCarga, Long idCliente) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.setFetchMode("devedorDocServs", FetchMode.JOIN)
                .createAlias("devedorDocServs", "cliente");
        criteria.setFetchMode("manifestoEntregaDocumentos", FetchMode.JOIN)
                .createAlias("manifestoEntregaDocumentos", "manifestoEntregaDocumento");
        criteria.setFetchMode("manifestoEntregaDocumento.ocorrenciaEntrega", FetchMode.JOIN)
                .createAlias("manifestoEntregaDocumento.ocorrenciaEntrega", "ocorrenciaEntrega");
        criteria.setFetchMode("ocorrenciaEntrega.evento", FetchMode.JOIN)
                .createAlias("ocorrenciaEntrega.evento", "evento");
        criteria.setFetchMode("manifestoEntregaDocumento.manifestoEntrega", FetchMode.JOIN)
                .createAlias("manifestoEntregaDocumento.manifestoEntrega", "manifestoEntrega");
        criteria.setFetchMode("manifestoEntrega.manifesto", FetchMode.JOIN)
                .createAlias("manifestoEntrega.manifesto", "manifesto");
        criteria.setFetchMode("manifesto.controleCarga", FetchMode.JOIN)
                .createAlias("manifesto.controleCarga", "controleCarga");

        criteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));

        if (idCliente != null) {
            criteria.add(Restrictions.eq("cliente.idCliente", idCliente));
        }

        criteria.add(Restrictions.ne("manifesto.tpStatusManifesto", ConstantesEntrega.STATUS_MANIFESTO_CANCELADO));
        criteria.add(
                Restrictions.in(
                        "manifesto.tpManifestoEntrega",
                        new Object[]{
                                ConstantesEntrega.DM_MANIFESTO_ENTREGA_NORMAL.getValue(),
                                ConstantesEntrega.DM_MANIFESTO_ENTREGA_PARCEIRA.getValue(),
                                ConstantesEntrega.DM_MANIFESTO_ENTREGA_DIRETA.getValue()}));

        criteria.add(
                Restrictions.in(
                        "ocorrenciaEntrega.tpOcorrencia",
                        new Object[]{
                                ConstantesEntrega.TP_OCORRENCIA_ENTREGUE,
                                ConstantesEntrega.TP_OCORRENCIA_ENTREGUE_AEROPORTO}));


        return (List<DoctoServico>) findByDetachedCriteria(criteria);
    }

    public List<Long> findDoctoServicoToEnviaCTeCliente(Integer rownum) {
        final StringBuilder sql = new StringBuilder();
        sql.append(" SELECT DS.ID_DOCTO_SERVICO FROM MONITORAMENTO_DOC_ELETRONICO MDE ")
                .append(" INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MDE.ID_DOCTO_SERVICO ")
                .append(" INNER JOIN CONHECIMENTO C ON C.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
                .append(" WHERE C.TP_DOCUMENTO_SERVICO = 'CTE' ")
                .append(" AND C.TP_SITUACAO_CONHECIMENTO = 'E' ")
                .append(" AND MDE.DS_DADOS_DOCUMENTO IS NOT NULL ")
                .append(" AND MDE.DH_ENVIO IS NULL ")
                .append(" AND MDE.TP_SITUACAO_DOCUMENTO = 'A' ");
        if (rownum != null) {
            sql.append(" AND ROWNUM <= " + rownum);
        }

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);
                return query.list();
            }
        };
        return getHibernateTemplate().executeFind(hcb);
    }


    /**
     * Nome da classe que o DAO ? respons?vel por persistir.
     */
    @Override
    protected final Class<DoctoServico> getPersistentClass() {
        return DoctoServico.class;
    }

    public List<DoctoServico> findByIdManifestoMoreRestrictions(Long idManifesto) {
        if (idManifesto == null) {
            return null;
        }

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ds ");
        sql.append("   FROM ").append(ManifestoEntregaDocumento.class.getName()).append(" med ");
        sql.append("      , ").append(Manifesto.class.getName()).append(" m ");
        sql.append("      , ").append(DoctoServico.class.getName()).append(" ds ");

        sql.append("  WHERE m.id = med.manifestoEntrega.id ");
        sql.append("    AND ds.id = med.doctoServico.id ");

        sql.append("    AND NOT EXISTS  ");
        sql.append("    ( SELECT 1 FROM  ").append(EventoDocumentoServico.class.getName()).append(" eds ");
        sql.append("      				,").append(Evento.class.getName()).append(" e ");
        sql.append("    				WHERE  eds.blEventoCancelado <> 'S' ");
        sql.append("    				AND e.cdEvento = 21 ");
        sql.append("    				AND e.id = eds.evento.id ");
        sql.append("    				AND eds.doctoServico.id = ds.id ");
        sql.append("     ) ");

        sql.append("    AND med.tpSituacaoDocumento <> 'CANC' ");
        sql.append("    AND m.tpStatusManifesto <> 'CA' ");
        sql.append("    AND m.id = ").append(idManifesto);

        return getAdsmHibernateTemplate().find(sql.toString());
    }

    public Long findIdClienteRemetenteById(Long idDoctoServico) {
        ProjectionList pl = Projections.projectionList()
                .add(Projections.property("cr.idCliente"), "idCliente");

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ds")
                .createAlias("ds.clienteByIdClienteRemetente", "cr")
                .add(Restrictions.eq("ds.idDoctoServico", idDoctoServico));

        dc.setProjection(pl);
        List<Long> l = findByDetachedCriteria(dc);
        if (!l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }

    public Long findIdClienteDestinatarioById(Long idDoctoServico) {
        ProjectionList pl = Projections.projectionList()
                .add(Projections.property("cd.idCliente"), "idCliente");

        DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ds")
                .createAlias("ds.clienteByIdClienteDestinatario", "cd")
                .add(Restrictions.eq("ds.idDoctoServico", idDoctoServico));

        dc.setProjection(pl);
        List<Long> l = findByDetachedCriteria(dc);
        if (!l.isEmpty()) {
            return l.get(0);
        }
        return null;
    }

    /**
     * Retorna o doctumento de servico com os clientes remetente e destinat?rio 'fetchado'.
     *
     * @param Long idDoctoServico
     * @return DoctoServico
     * @author Micka?l Jalbert
     * @since 06/06/2006
     */
    public DoctoServico findByIdWithRemetenteDestinatario(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("doc");

        hql.addInnerJoin(DoctoServico.class.getName(), "doc");
        hql.addLeftOuterJoin("fetch doc.clienteByIdClienteRemetente", "rem");
        hql.addLeftOuterJoin("fetch doc.clienteByIdClienteDestinatario", "des");

        hql.addCriteria("doc.id", "=", idDoctoServico);

        List<DoctoServico> lstDocto = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
        if (!lstDocto.isEmpty()) {
            return lstDocto.get(0);
        } else {
            return null;
        }
    }

    /**
     * Verifica se o usu?rio logado possui permiss?es de acesso ao documento de servi?o
     *
     * @param idDoctoServico Identificador do documento de servi?o
     * @param idFilial       Identificador da filial
     * @return <code>TRUE</code> Se o usu?rio logado possui permiss?es de acesso ao documento de servi?o informado ou <code>FALSE</code>
     * caso contr?rio.
     */
    public Boolean validatePermissaoDocumentoUsuario(Long idDoctoServico, Long idFilial) {
        Boolean temPermissao = Boolean.FALSE;

        Filial filialSessao = (Filial) SessionContext.get(SessionKey.FILIAL_KEY);

        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("count(*)");

        sql.addFrom(CentralizadoraFaturamento.class.getName() + " cf " +
                "    inner join cf.filialByIdFilialCentralizada filialCentralizada " +
                "    inner join cf.filialByIdFilialCentralizadora filialCentralizadora ");
        sql.addFrom(DevedorDocServFat.class.getName() + " ddsf " +
                "    inner join ddsf.doctoServico ds " +
                "    inner join ds.servico se " +
                "    inner join ddsf.filial filialDevedor");

        sql.addJoin("cf.tpModal", "se.tpModal");
        sql.addJoin("cf.tpAbrangencia", "se.tpAbrangencia");
        sql.addJoin("filialCentralizada.idFilial", "filialDevedor.idFilial");
        sql.addCriteria("filialCentralizadora.idFilial", "=", filialSessao.getIdFilial());
        sql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);

        List<Long> ret = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
        int retorno = 0;

        if (ret != null && !ret.isEmpty()) {
            retorno = ret.get(0).intValue();
        }

        if (retorno > 0) {
            temPermissao = Boolean.TRUE;
        } else {
            sql = new SqlTemplate();

            sql.addProjection("count(*)");

            sql.addFrom(CentralizadoraFaturamento.class.getName() + " cf " +
                    "    inner join cf.filialByIdFilialCentralizada filialCentralizada " +
                    "    inner join cf.filialByIdFilialCentralizadora filialCentralizadora ");
            sql.addFrom(DevedorDocServFat.class.getName() + " ddsf " +
                    "    inner join ddsf.doctoServico ds " +
                    "    inner join ds.servico se " +
                    "    inner join ddsf.filial filialDevedor");

            sql.addJoin("cf.tpModal", "se.tpModal");
            sql.addJoin("cf.tpAbrangencia", "se.tpAbrangencia");
            sql.addJoin("filialCentralizadora.idFilial", "filialDevedor.idFilial");
            sql.addCriteria("filialCentralizada.idFilial", "=", filialSessao.getIdFilial());
            sql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);

            ret = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

            retorno = 0;

            if (ret != null && !ret.isEmpty()) {
                retorno = ((Long) ret.get(0)).intValue();
            }

            if (retorno > 0) {
                temPermissao = Boolean.FALSE;
            } else if (idFilial != null && idFilial.equals(filialSessao.getIdFilial())) {
                temPermissao = Boolean.TRUE;
            } else {
                temPermissao = Boolean.FALSE;
            }
        }

        if (temPermissao.equals(Boolean.FALSE)) {
            throw new BusinessException("LMS-36004");
        }

        return temPermissao;
    }

    /**
     * Retorna um map dos valores somados dos documentos da fatura informada.
     *
     * @param Long idFatura
     * @return List
     */
    public Object findSomaValoresByFatura(Long idFatura) {
        SqlTemplate sqlSub = new SqlTemplate();

        sqlSub.addProjection("1");
        sqlSub.addFrom("item_fatura", "ite");
        sqlSub.addFrom("devedor_doc_serv_fat", "dev");
        sqlSub.addFrom("docto_servico", "doc");
        sqlSub.addFrom("imposto_servico", "impsub");

        sqlSub.addJoin("dev.id_devedor_doc_serv_fat", "ite.id_devedor_doc_serv_fat");
        sqlSub.addJoin("doc.id_docto_servico", "dev.id_docto_servico");
        sqlSub.addJoin("imp.id_imposto_servico", "impsub.id_imposto_servico");
        sqlSub.addCustomCriteria("doc.id_docto_servico in (impsub.id_nota_fiscal_servico, impsub.id_conhecimento)");
        sqlSub.addCriteria("ite.id_fatura", "=", idFatura);

        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("SUM(decode(imp.tp_imposto, 'PI', imp.vl_base_calculo, 'CO', imp.vl_base_calculo, 'CS', imp.vl_base_calculo, 0))", "basepisconfinscsll");
        sql.addProjection("SUM(decode(imp.tp_imposto, 'IR', imp.vl_base_calculo, 0))", "baseIR");
        sql.addProjection("SUM(decode(imp.tp_imposto, 'CO', imp.vl_imposto, 0))", "confins");
        sql.addProjection("SUM(decode(imp.tp_imposto,'IN', imp.vl_imposto, 0))", "inss");
        sql.addProjection("SUM(decode(imp.tp_imposto,'CS', imp.vl_imposto, 0))", "csll");
        sql.addProjection("SUM(decode(imp.tp_imposto,'IR', imp.vl_imposto, 0))", "ir");
        sql.addProjection("SUM(decode(imp.tp_imposto,'IS', imp.vl_imposto, 0))", "iss");
        sql.addProjection("SUM(decode(imp.tp_imposto,'PI', imp.vl_imposto, 0))", "pis");

        sql.addFrom("imposto_servico", "imp");
        sql.addCustomCriteria("exists (" + sqlSub.getSql() + ")");

        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("basepisconfinscsll", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("baseIR", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("confins", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("inss", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("csll", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ir", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("iss", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("pis", Hibernate.BIG_DECIMAL);
            }
        };

        //CUIDADO, ESTA MANDANDO O SELECT DO OBJECTO SQL MAS OS FILTROS DO OBJETO SQLSUB!!!
        return (Object) this.getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sqlSub.getCriteria(), configSql);
    }

    // Retorna o documento de servi?o original do documento de servi?o passado por par?metro
    public List<Map<String, Object>> findDoctoServicoOriginal(TypedFlatMap tfm) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection(" new Map(co.tpDocumentoServico as tpDocumentoServico, " +
                "co.idDoctoServico as idDoctoServico, " +
                "co.nrDoctoServico as nrDoctoServico, " +
                "df as df, " +
                "fil.sgFilial as sgFilial)"
        );

        sql.addFrom(Conhecimento.class.getName() + " co " +
                " join co.devedorDocServFats df " +
                " join co.filialByIdFilialOrigem fil");

        sql.addCriteria("co.doctoServicoOriginal.id", "=", tfm.getLong("idDoctoServico"));
        sql.addCustomCriteria("co.tpSituacaoConhecimento in ('E', 'B')");

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }


    private ConfigureSqlQuery configureSqlQueryDoctoSevicoNaoFaturadoReport() {
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("tipodocumento", Hibernate.STRING);
                sqlQuery.addScalar("filialdeorigem", Hibernate.STRING);
                sqlQuery.addScalar("numerodocumento", Hibernate.STRING);
                sqlQuery.addScalar("filialdedestino", Hibernate.STRING);
                sqlQuery.addScalar("datadeemissao", Hibernate.STRING);
                sqlQuery.addScalar("cnpjdevedor", Hibernate.STRING);
                sqlQuery.addScalar("razaosocialdevedor", Hibernate.STRING);
                sqlQuery.addScalar("tipopessoadevedor", Hibernate.STRING);
                sqlQuery.addScalar("tipocliente", Hibernate.STRING);
                sqlQuery.addScalar("valordevido", Hibernate.STRING);
                sqlQuery.addScalar("numdoceletronico", Hibernate.STRING);
                sqlQuery.addScalar("situacaodoceletronico", Hibernate.STRING);
                sqlQuery.addScalar("tipofrete", Hibernate.STRING);
                sqlQuery.addScalar("tipoconhecimento", Hibernate.STRING);
                sqlQuery.addScalar("tipocalculo", Hibernate.STRING);
                sqlQuery.addScalar("modal", Hibernate.STRING);
                sqlQuery.addScalar("abrangencia", Hibernate.STRING);
                sqlQuery.addScalar("servico", Hibernate.STRING);
                sqlQuery.addScalar("datadeentrega", Hibernate.STRING);
                sqlQuery.addScalar("valormercadoria", Hibernate.STRING);
                sqlQuery.addScalar("valorfretetotal", Hibernate.STRING);
                sqlQuery.addScalar("valorfreteliquido", Hibernate.STRING);
                sqlQuery.addScalar("valoricms", Hibernate.STRING);
                sqlQuery.addScalar("icms", Hibernate.STRING);
                sqlQuery.addScalar("valoricmsst", Hibernate.STRING);
                sqlQuery.addScalar("pesoaferido", Hibernate.STRING);
                sqlQuery.addScalar("pesoreal", Hibernate.STRING);
                sqlQuery.addScalar("qtdvolumes", Hibernate.STRING);
                sqlQuery.addScalar("notasfiscais", Hibernate.STRING);
                sqlQuery.addScalar("cnpjremetente", Hibernate.STRING);
                sqlQuery.addScalar("razaosocialremetente", Hibernate.STRING);
                sqlQuery.addScalar("municipioremetente", Hibernate.STRING);
                sqlQuery.addScalar("ufremetente", Hibernate.STRING);
                sqlQuery.addScalar("cnpjdestinatario", Hibernate.STRING);
                sqlQuery.addScalar("razaosocialdestinatario", Hibernate.STRING);
                sqlQuery.addScalar("municipiodestinatario", Hibernate.STRING);
                sqlQuery.addScalar("ufdestinatario", Hibernate.STRING);
                sqlQuery.addScalar("tipocobranca", Hibernate.STRING);
                sqlQuery.addScalar("clientecomprefatura", Hibernate.STRING);
                sqlQuery.addScalar("classificacaocliente", Hibernate.STRING);
                sqlQuery.addScalar("divisaocobranca", Hibernate.STRING);
                sqlQuery.addScalar("periodicidadedadivisao", Hibernate.STRING);
                sqlQuery.addScalar("cobrancacentralizada", Hibernate.STRING);
                sqlQuery.addScalar("filialresponsavelcliente", Hibernate.STRING);
                sqlQuery.addScalar("regionalcobranca", Hibernate.STRING);
                sqlQuery.addScalar("filialcobranca", Hibernate.STRING);
                sqlQuery.addScalar("faturasanteriores", Hibernate.STRING);
                sqlQuery.addScalar("situacaocobrancafrete", Hibernate.STRING);
                sqlQuery.addScalar("bloqueado", Hibernate.STRING);
                sqlQuery.addScalar("possuiagendastransf", Hibernate.STRING);
                sqlQuery.addScalar("possuitransfpendentes", Hibernate.STRING);
                sqlQuery.addScalar("dtnatura", Hibernate.STRING);
                sqlQuery.addScalar("chavecte", Hibernate.STRING);
            }
        };
    }

    private ConfigureSqlQuery configureSqlQueryDoctoSevicoNaoFaturado() {
        return new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
            	sqlQuery.addScalar("chaveCTE", Hibernate.STRING);
                sqlQuery.addScalar("vl_devido", Hibernate.STRING);
                sqlQuery.addScalar("sg_filial_cob", Hibernate.STRING);
                sqlQuery.addScalar("sg_filial_resp", Hibernate.STRING);
                sqlQuery.addScalar("devedor", Hibernate.STRING);

                sqlQuery.addScalar("sg_filial", Hibernate.STRING);
                sqlQuery.addScalar("nr_docto_servico", Hibernate.STRING);
                sqlQuery.addScalar("tpDocumento", Hibernate.STRING);

                sqlQuery.addScalar("dtEntrega", Hibernate.STRING);
                sqlQuery.addScalar("tpConhecimento", Hibernate.STRING);
                sqlQuery.addScalar("tpFrete", Hibernate.STRING);
                sqlQuery.addScalar("dtEmissao", Hibernate.STRING);

                sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
                sqlQuery.addScalar("id_devedor_doc_serv_fat", Hibernate.LONG);
            }
        };
    }

    public Integer getRowCountDoctoServicoNaoFaturado(final TypedFlatMap tfm) {
        Map<String, Object> params = new HashMap<String, Object>();
        final StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) as ct from ( ");
        sql.append(generateDoctoServicoNaoFaturadoSQL(tfm, params));
        sql.append(" ) ");

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("ct", Hibernate.INTEGER);
            }
        };

        List<Map<String, Object>> toReturn = getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), params, csq);
        return (Integer) toReturn.get(0).get("ct");

    }

    public List<Map<String, Object>> findDoctoServicoNaoFaturado(TypedFlatMap tfm) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = createDoctoNaoFaturadoProjection(tfm);
        sql.append(generateDoctoServicoNaoFaturadoSQL(tfm, params));
        return getAdsmHibernateTemplateReadOnly().findBySqlToMappedResult(sql.toString(), params, configureSqlQueryDoctoSevicoNaoFaturado());
    }

    public List<Map<String, Object>> findDoctoServicoNaoFaturadoReport(TypedFlatMap tfm) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = createDoctoNaoFaturadoReportProjection();
        sql.append(generateDoctoServicoNaoFaturadoSQL(tfm, params));
        return getAdsmHibernateTemplateReadOnly().findBySqlToMappedResult(sql.toString(), params, configureSqlQueryDoctoSevicoNaoFaturadoReport());
    }

	private StringBuilder createDoctoNaoFaturadoReportProjection() {
		StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ( ");
        sql.append("     SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio ");
        sql.append("     WHERE id_dominio IN ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO') ");
        sql.append("         AND vl_valor_dominio = ds.tp_documento_servico) AS tipodocumento, ");
        sql.append("         ( SELECT forig.sg_filial FROM filial forig WHERE ds.id_filial_origem = forig.id_filial) AS filialdeorigem, ");
        sql.append("         ds.nr_docto_servico AS numerodocumento, ");
        sql.append("         ( SELECT fdest.sg_filial FROM filial fdest WHERE ds.id_filial_destino = fdest.id_filial) AS filialdedestino, ");
        sql.append("         to_char( trunc( CAST(ds.dh_emissao AS DATE) ), 'dd/MM/yyyy' ) AS datadeemissao, ");
        sql.append("         pdev.nr_identificacao AS cnpjdevedor, ");
        sql.append("         pdev.nm_pessoa AS razaosocialdevedor, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_PESSOA' ) ");
        sql.append("         AND vl_valor_dominio = pdev.tp_pessoa) AS tipopessoadevedor, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             (SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_CLIENTE' ) ");
        sql.append("         AND vl_valor_dominio = cl.tp_cliente) AS tipocliente, ");
        sql.append("         dev.vl_devido AS valordevido, ");
        sql.append("         mde.nr_documento_eletronico AS numdoceletronico, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_SITUACAO_DOC_ELETRONICO' ) AND vl_valor_dominio = mde.tp_situacao_documento ) AS situacaodoceletronico, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_FRETE' ) AND vl_valor_dominio = con.tp_frete) AS tipofrete, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_CONHECIMENTO' ) AND vl_valor_dominio = con.tp_conhecimento) AS tipoconhecimento, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_CALCULO_FRETE' ) AND vl_valor_dominio = ds.tp_calculo_preco ) AS tipocalculo, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_MODAL' ) AND vl_valor_dominio = s.tp_modal) AS modal, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_ABRANGENCIA' ) AND vl_valor_dominio = s.tp_abrangencia) AS abrangencia, ");
        sql.append("         vi18n( s.ds_servico_i) AS servico, ");
        sql.append("         to_char(( SELECT trunc( CAST(MAX(med.dh_ocorrencia) AS DATE) ) dh_ocorrencia FROM manifesto_entrega_documento med ");
        sql.append("             WHERE med.id_ocorrencia_entrega = 5 AND med.tp_situacao_documento <> 'CANC'AND med.id_docto_servico = ds.id_docto_servico AND ROWNUM = 1), 'dd/MM/yyyy') AS datadeentrega, ");
        sql.append("         ds.vl_mercadoria AS valormercadoria, ");
        sql.append("         ds.vl_total_doc_servico AS valorfretetotal, ");
        sql.append("         ds.vl_frete_liquido AS valorfreteliquido, ");
        sql.append("         ds.vl_imposto AS valoricms, ");
        sql.append("         ds.pc_aliquota_icms AS icms, ");
        sql.append("         ds.vl_icms_st AS valoricmsst, ");
        sql.append("         ds.ps_aferido AS pesoaferido, ");
        sql.append("         ds.ps_real AS pesoreal, ");
        sql.append("         ds.qt_volumes AS qtdvolumes, ");
        sql.append("         ( SELECT substr( XMLCAST(XMLAGG(XMLELEMENT( e, '/' || nfc.nr_nota_fiscal ) ORDER BY nr_nota_fiscal ) AS CLOB), 2) ");
        sql.append("             FROM nota_fiscal_conhecimento nfc WHERE nfc.id_conhecimento = con.id_conhecimento ) AS notasfiscais, ");
        sql.append("         ( SELECT prem.nr_identificacao FROM pessoa prem WHERE ds.id_cliente_remetente = prem.id_pessoa ) AS cnpjremetente, ");
        sql.append("         ( SELECT prem.nm_pessoa FROM pessoa prem WHERE ds.id_cliente_remetente = prem.id_pessoa ) AS razaosocialremetente, ");
        sql.append("         ( SELECT m_prem.nm_municipio FROM pessoa prem, endereco_pessoa ep_prem, municipio m_prem ");
        sql.append("             WHERE ds.id_cliente_remetente = prem.id_pessoa AND prem.id_endereco_pessoa = ep_prem.id_endereco_pessoa ");
        sql.append("                 AND ep_prem.id_municipio = m_prem.id_municipio ) AS municipioremetente, ");
        sql.append("         ( SELECT uf_prem.sg_unidade_federativa FROM pessoa prem, endereco_pessoa ep_prem, municipio m_prem, unidade_federativa uf_prem ");
        sql.append("             WHERE ds.id_cliente_remetente = prem.id_pessoa AND prem.id_endereco_pessoa = ep_prem.id_endereco_pessoa ");
        sql.append("                 AND ep_prem.id_municipio = m_prem.id_municipio AND m_prem.id_unidade_federativa = uf_prem.id_unidade_federativa ) AS ufremetente, ");
        sql.append("         ( SELECT pdes.nr_identificacao FROM pessoa pdes WHERE ds.id_cliente_destinatario = pdes.id_pessoa ) AS cnpjdestinatario, ");
        sql.append("         ( SELECT pdes.nm_pessoa FROM pessoa pdes WHERE ds.id_cliente_destinatario = pdes.id_pessoa ) AS razaosocialdestinatario, ");
        sql.append("         ( SELECT m_pdes.nm_municipio FROM pessoa pdes, endereco_pessoa ep_pdes, municipio m_pdes ");
        sql.append("             WHERE ds.id_cliente_destinatario = pdes.id_pessoa AND pdes.id_endereco_pessoa = ep_pdes.id_endereco_pessoa ");
        sql.append("                 AND ep_pdes.id_municipio = m_pdes.id_municipio) AS municipiodestinatario, ");
        sql.append("         ( SELECT uf_pdes.sg_unidade_federativa FROM pessoa pdes, endereco_pessoa ep_pdes, municipio m_pdes, unidade_federativa uf_pdes ");
        sql.append("             WHERE ds.id_cliente_destinatario = pdes.id_pessoa AND pdes.id_endereco_pessoa = ep_pdes.id_endereco_pessoa ");
        sql.append("                 AND ep_pdes.id_municipio = m_pdes.id_municipio AND m_pdes.id_unidade_federativa = uf_pdes.id_unidade_federativa) AS ufdestinatario, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TIPO_COBRANCA' ) AND vl_valor_dominio = cl.tp_cobranca ) tipocobranca, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("            ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_SIM_NAO' ) AND vl_valor_dominio = cl.bl_pre_fatura) clientecomprefatura, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("            ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_TP_CLIENTE_FINANCEIRO' ) AND vl_valor_dominio = nvl( ecf.tp_cliente, 'G' )) classificacaocliente, ");
        sql.append("         divcli.ds_divisao_cliente AS divisaocobranca, ");
        sql.append("         ( SELECT to_char( wm_concat( ");
        sql.append("             ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("                 ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_PERIODICIDADE_FATURAMENTO' ) AND vl_valor_dominio = df.tp_periodicidade ) ");
        sql.append("             )) FROM dia_faturamento df WHERE df.id_divisao_cliente = dev.id_divisao_cliente) AS periodicidadedadivisao, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("            ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_SIM_NAO' )  AND vl_valor_dominio = cl.bl_cobranca_centralizada ) AS cobrancacentralizada, ");
        sql.append("         fcobcli.sg_filial AS filialresponsavelcliente, ");
        sql.append("         reg.ds_regional AS regionalcobranca, ");
        sql.append("         ( SELECT fcob.sg_filial FROM filial fcob WHERE dev.id_filial = fcob.id_filial ) AS filialcobranca, ");
        sql.append("         ( SELECT to_char( wm_concat(ffat.sg_filial || ' ' || fat.nr_fatura) ) FROM item_fatura ifat, fatura fat, filial ffat ");
        sql.append("             WHERE ifat.id_fatura = fat.id_fatura AND fat.id_filial = ffat.id_filial AND ifat.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ) AS faturasanteriores, ");
        sql.append("         ( SELECT vi18n(ds_valor_dominio_i) FROM valor_dominio WHERE id_dominio IN ");
        sql.append("             ( SELECT id_dominio FROM dominio WHERE nm_dominio = 'DM_STATUS_COBRANCA_DOCTO_SERVICO' ) AND vl_valor_dominio = dev.tp_situacao_cobranca ) AS situacaocobrancafrete, ");
        sql.append("         nvl(( SELECT 'Sim' FROM bloqueio_faturamento bf ");
        sql.append("                 WHERE bf.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat AND bf.dh_desbloqueio IS NULL AND ROWNUM = 1 ), 'Nao' ) AS bloqueado, ");
        sql.append("         nvl(( SELECT 'Sim' FROM agenda_transferencia ag ");
        sql.append("                 WHERE ag.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat AND ROWNUM = 1 ), 'Nao' ) AS possuiagendastransf, ");
        sql.append("         nvl(( SELECT 'Sim' FROM transferencia tr, item_transferencia itr ");
        sql.append("                 WHERE  tr.id_transferencia = itr.id_transferencia AND itr.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
        sql.append("                   AND tr.tp_situacao_transferencia = 'PR' AND ROWNUM = 1 ), 'Nao' ) AS possuitransfpendentes, ");
        sql.append("         ( SELECT dc.ds_valor_campo FROM nota_fiscal_conhecimento nfc ");
        sql.append("             INNER JOIN conhecimento conh ON conh.id_conhecimento = nfc.id_conhecimento ");
        sql.append("             LEFT OUTER JOIN nf_dados_comp nfdados ON nfdados.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
        sql.append("             LEFT OUTER JOIN dados_complemento dc ON dc.id_dados_complemento = nfdados.id_dados_complemento ");
        sql.append("             LEFT JOIN informacao_doc_servico ids ON ids.id_informacao_doc_servico = dc.id_informacao_doc_servico ");
        sql.append("             LEFT JOIN informacao_docto_cliente idc ON idc.id_informacao_docto_cliente = dc.id_informacao_docto_cliente ");
        sql.append("             WHERE conh.id_conhecimento = con.id_conhecimento ");
        sql.append("             AND ( CASE WHEN ids.ds_campo IS NOT NULL THEN ids.ds_campo ELSE idc.ds_campo END) = 'DT Natura') AS dtnatura, ");
        sql.append("             (SELECT'\"' || mde2.nr_chave || '\"' FROM  monitoramento_doc_eletronico mde2 WHERE mde2.id_docto_servico = ds.id_docto_servico) AS chavecte");

        return sql;
	}
	
	private StringBuilder generateDoctoServicoNaoFaturadoSQL(TypedFlatMap tfm, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();

        sql.append("  FROM docto_servico ds,");
        sql.append("       conhecimento con,");
        sql.append("       nota_fiscal_servico nfs,");
        sql.append("       servico s,");
        sql.append("       devedor_doc_serv_fat dev,");
        sql.append("       cliente cl,");
        sql.append("       filial fcobcli,");
        sql.append("       excecao_cliente_financeiro ecf,");
        sql.append("       divisao_cliente divcli,");

        sql.append("		pessoa prem,");
        sql.append("		endereco_pessoa ep_prem, ");
        sql.append("		municipio m_prem, ");
        sql.append("		unidade_federativa uf_prem, ");
        sql.append("		pessoa pdes,");
        sql.append("		endereco_pessoa ep_pdes, ");
        sql.append("		municipio m_pdes, ");
        sql.append("		unidade_federativa uf_pdes, ");
        sql.append("       pessoa pdev,");
        sql.append("       pessoa pcons,");
        sql.append("       pessoa prede,");
        sql.append("       filial forig,");
        sql.append("       filial fdest,");
        sql.append("       filial fcob,");
        sql.append("       docto_servico dsorig,");
        sql.append("       filial fdsorig,");
        sql.append("       conhecimento conorig,");
        sql.append("       (SELECT *");
        sql.append("          FROM regional reg");
        sql.append("         WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND");
        sql.append("               reg.dt_vigencia_final) reg,");
        sql.append("       (SELECT *");
        sql.append("          FROM regional_filial regf");
        sql.append("         WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND");
        sql.append("               regf.dt_vigencia_final) regf,");
        sql.append("       (SELECT DISTINCT eds.id_docto_servico");
        sql.append("          FROM evento_documento_servico eds,");
        sql.append("               evento                   ev");
        sql.append("         WHERE eds.bl_evento_cancelado = 'N'");
        sql.append("           AND eds.id_evento = ev.id_evento");
        sql.append("           AND ev.cd_evento IN (7, 90, 91)) eds,");
        sql.append("       (SELECT MAX(mde.nr_documento_eletronico) nr_documento_eletronico,");
        sql.append("               MAX(mde.tp_situacao_documento) tp_situacao_documento,");
        sql.append("               mde.id_docto_servico");
        sql.append("          FROM monitoramento_doc_eletronico mde");
        sql.append("         WHERE mde.nr_documento_eletronico IS NOT NULL");
        sql.append("         GROUP BY mde.id_docto_servico) mde");
        sql.append(" WHERE ds.id_docto_servico = con.id_conhecimento(+)");
        sql.append("   AND ds.id_docto_servico = nfs.id_nota_fiscal_servico(+)");
        sql.append("   AND ds.id_servico = s.id_servico(+)");
        sql.append("   AND ds.id_docto_servico = dev.id_docto_servico(+)");
        sql.append("   AND dev.id_cliente = cl.id_cliente(+)");
        sql.append("   AND cl.id_filial_cobranca = fcobcli.id_filial(+)");
        sql.append("   AND dev.id_cliente = ecf.id_cliente(+)");
        sql.append("   AND dev.id_divisao_cliente = divcli.id_divisao_cliente(+)");
        sql.append("   AND ds.id_cliente_remetente = prem.id_pessoa(+)");
        sql.append("   AND ds.id_cliente_destinatario = pdes.id_pessoa(+)");
        sql.append("   AND ds.id_cliente_consignatario = pcons.id_pessoa(+)");
        sql.append("   AND ds.id_cliente_redespacho = prede.id_pessoa(+)");
        sql.append("   AND dev.id_cliente = pdev.id_pessoa(+)");
        sql.append("   AND ds.id_filial_origem = forig.id_filial(+)");
        sql.append("   AND ds.id_filial_destino = fdest.id_filial(+)");
        sql.append("   AND dev.id_filial = fcob.id_filial(+)");
        sql.append("   AND ds.id_docto_servico_original = dsorig.id_docto_servico(+)");
        sql.append("   AND dsorig.id_filial_origem = fdsorig.id_filial(+)");
        sql.append("   AND dsorig.id_docto_servico = conorig.id_conhecimento(+)");
        sql.append("   AND dev.id_filial = regf.id_filial(+)");
        sql.append("   AND regf.id_regional = reg.id_regional(+)");
        sql.append("   AND ds.id_docto_servico = mde.id_docto_servico(+)");
        sql.append("   AND ds.id_docto_servico = eds.id_docto_servico(+)");

        sql.append("   AND ds.dh_emissao IS NOT NULL");
        sql.append("   AND ds.nr_docto_servico > 0");
        sql.append("   AND (con.tp_situacao_conhecimento IN ('E', 'B') OR");
        sql.append("       con.tp_situacao_conhecimento IS NULL)");
        sql.append("   AND (nfs.tp_situacao_nf = 'E' OR nfs.tp_situacao_nf IS NULL)");
        sql.append("	AND prem.id_endereco_pessoa = ep_prem.id_endereco_pessoa(+) ");
        sql.append("	AND ep_prem.id_municipio = m_prem.id_municipio(+)");
        sql.append("	AND m_prem.id_unidade_federativa = uf_prem.id_unidade_federativa(+) ");
        sql.append("	AND pdes.id_endereco_pessoa = ep_pdes.id_endereco_pessoa(+) ");
        sql.append("	AND ep_pdes.id_municipio = m_pdes.id_municipio(+)");
        sql.append("	AND m_pdes.id_unidade_federativa = uf_pdes.id_unidade_federativa(+) ");

        if (tfm.get("filial") != null) {
            sql.append("   AND fcobcli.id_filial = :id_filial_resp");
            params.put("id_filial_resp", tfm.getLong("filial"));
        }
        if (tfm.get("tpCliente") != null) {
            sql.append("   AND cl.tp_cliente = :tp_cliente");
            params.put("tp_cliente", tfm.getString("tpCliente"));
        }
        if (tfm.get("classificacaoCliente") != null) {
            sql.append("   AND nvl(ecf.tp_cliente, 'G') = :tp_cliente_financ");
            params.put("tp_cliente_financ", tfm.getString("classificacaoCliente"));
        }
        if (tfm.get("tpCobranca") != null) {
            sql.append("   AND cl.tp_cobranca = :tp_cobranca");
            params.put("tp_cobranca", tfm.getString("tpCobranca"));
        }
        if (tfm.get("blCobrancaCentralizada") != null) {
            sql.append("   AND cl.bl_cobranca_centralizada = :blCobrancaCentralizada");
            params.put("blCobrancaCentralizada", tfm.getString("blCobrancaCentralizada"));
        }
        if (tfm.get("blPreFatura") != null) {
            sql.append("   AND cl.bl_pre_fatura = :bl_pre_fatura");
            params.put("bl_pre_fatura", tfm.getString("blPreFatura"));
        }

        if (tfm.get("estadoCobranca") != null) {
            if ("AF".equals(tfm.getString("estadoCobranca"))) {
                sql.append("   AND dev.tp_situacao_cobranca IN ('P', 'C')");

            } else if ("FA".equals(tfm.getString("estadoCobranca"))) {
                sql.append("   AND dev.tp_situacao_cobranca  = 'F'");

            } else if ("PR".equals(tfm.getString("estadoCobranca"))) {
                sql.append("   AND dev.tp_situacao_cobranca IN ('P', 'C', 'F')");
            }
        }

        if (tfm.get("devedoresExcluir") != null) {
            String[] devedores = ((String) tfm.get("devedoresExcluir")).split(";");
            if (devedores.length > 0 && !devedores[0].isEmpty()) {
                sql.append("   AND (pdev.nr_identificacao LIKE -1");
                for (String devedor : devedores) {
                    sql.append(" OR pdev.nr_identificacao NOT LIKE '" + devedor + "' ");
                }
                sql.append(" ) ");
            }
        }
        if (tfm.get("devedoresListar") != null) {
            String[] devedores = ((String) tfm.get("devedoresListar")).split(";");
            if (devedores.length > 0 && !devedores[0].isEmpty()) {
                sql.append("   AND (pdev.nr_identificacao LIKE -1");
                for (String devedor : devedores) {
                    sql.append(" OR pdev.nr_identificacao like '" + devedor + "' ");
                }
                sql.append(" ) ");
            }
        }

        if (tfm.get("idRegional") != null) {
            sql.append("   AND reg.id_regional = :id_regional");
            params.put("id_regional", tfm.getLong("idRegional"));
        }
        if (tfm.get("idFilialCobranca") != null) {
            sql.append("   AND fcob.id_filial = :id_filial_cob");
            params.put("id_filial_cob", tfm.getLong("idFilialCobranca"));
        }
        if (tfm.get("dtVigenciaInicial") != null) {
            sql.append("   AND trunc(CAST(ds.dh_emissao AS DATE)) >= :dt_inicial");
            params.put("dt_inicial", tfm.get("dtVigenciaInicial"));
        }
        if (tfm.get("dtVigenciaFinal") != null) {
            sql.append("   AND trunc(CAST(ds.dh_emissao AS DATE)) <= :dt_final");
            params.put("dt_final", tfm.get("dtVigenciaFinal"));
        }
        if (tfm.get("idServico") != null) {
            sql.append("   AND ds.id_servico = :idServico");
            params.put("idServico", tfm.getLong("idServico"));
        }
        if (tfm.get("tpDocumento") != null) {
            sql.append("   AND ds.tp_documento_servico = :tpDocumento");
            params.put("tpDocumento", tfm.getString("tpDocumento"));
        }
        if (tfm.get("tpFrete") != null) {
            sql.append("   AND con.tp_frete = :tpFrete");
            params.put("tpFrete", tfm.getString("tpFrete"));
        }
        if (tfm.get("tpConhecimento") != null) {
            sql.append("   AND con.tp_conhecimento = :tpConhecimento");
            params.put("tpConhecimento", tfm.getString("tpConhecimento"));
        }
        if (tfm.get("tpCalculo") != null) {
            sql.append("   AND ds.tp_calculo_preco = :tpCalculo");
            params.put("tpCalculo", tfm.getString("tpCalculo"));
        }
        if (tfm.get("blBloqueado") != null) {
            sql.append("   AND nvl((SELECT 'S'");
            sql.append("             FROM bloqueio_faturamento bf");
            sql.append("            WHERE bf.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat");
            sql.append("              AND bf.dh_desbloqueio IS NULL");
            sql.append("              AND rownum = 1),");
            sql.append("           'N') = :blBloqueado");
            params.put("blBloqueado", tfm.getString("blBloqueado"));
        }
        if (tfm.get("blAgendaTransferencia") != null) {
            sql.append("   AND nvl((SELECT 'S'");
            sql.append("             FROM agenda_transferencia ag");
            sql.append("            WHERE ag.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat");
            sql.append("              AND rownum = 1),");
            sql.append("           'N') = :blAgendaTransferencia");
            params.put("blAgendaTransferencia", tfm.getString("blAgendaTransferencia"));
        }
        if (tfm.get("blTransferenciaPendentes") != null) {
            sql.append("   AND nvl((SELECT 'S'");
            sql.append("             FROM transferencia      tr,");
            sql.append("                  item_transferencia itr");
            sql.append("            WHERE tr.id_transferencia = itr.id_transferencia");
            sql.append("              AND itr.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat");
            sql.append("              AND tr.tp_situacao_transferencia = 'PR'");
            sql.append("              AND rownum = 1),");
            sql.append("           'N') = :blTransferenciaPendentes");
            params.put("blTransferenciaPendentes", tfm.getString("blTransferenciaPendentes"));
        }
        if (tfm.get("filialCobDifResp") != null) {
            if (tfm.getBoolean("filialCobDifResp")) {
                sql.append(" AND  FCOB.ID_FILIAL <> FCOBCLI.ID_FILIAL ");
            } else {
                sql.append(" AND FCOB.ID_FILIAL = FCOBCLI.ID_FILIAL ");
            }
        }
        return sql;
    }

    private StringBuilder generateDoctoServicoNaoFaturadoReportSQL(TypedFlatMap tfm, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();

        sql.append(" FROM docto_servico ds,");
        sql.append(" servico s,");
        sql.append(" conhecimento               con,");
        sql.append(" nota_fiscal_servico        nfs,");
        sql.append(" devedor_doc_serv_fat       dev,");
        sql.append(" cliente                    cl,");
        sql.append(" filial                     fcobcli,");
        sql.append(" excecao_cliente_financeiro ecf,");
        sql.append(" divisao_cliente            divcli,");
        sql.append(" pessoa                     pdev,");
        sql.append(" (SELECT * FROM regional reg WHERE trunc(sysdate) BETWEEN reg.dt_vigencia_inicial AND reg.dt_vigencia_final) reg,");
        sql.append(" (SELECT * FROM regional_filial regf WHERE trunc(sysdate) BETWEEN regf.dt_vigencia_inicial AND regf.dt_vigencia_final) regf,");
        sql.append(" (SELECT MAX(mde.nr_documento_eletronico) nr_documento_eletronico, MAX(mde.tp_situacao_documento) tp_situacao_documento,");
        sql.append(" mde.id_docto_servico FROM monitoramento_doc_eletronico mde WHERE mde.nr_documento_eletronico IS NOT NULL GROUP BY mde.id_docto_servico) mde");

        sql.append(" WHERE s.id_servico = ds.id_servico");
        sql.append(" AND ds.id_docto_servico = con.id_conhecimento (+)");
        sql.append(" AND ds.id_docto_servico = nfs.id_nota_fiscal_servico (+)");
        sql.append(" AND ds.id_docto_servico = dev.id_docto_servico (+)");
        sql.append(" AND dev.id_cliente = cl.id_cliente (+)");
        sql.append(" AND cl.id_filial_cobranca = fcobcli.id_filial (+)");
        sql.append(" AND dev.id_cliente = ecf.id_cliente (+)");
        sql.append(" AND dev.id_divisao_cliente = divcli.id_divisao_cliente (+)");
        sql.append(" AND dev.id_cliente = pdev.id_pessoa (+)");
        sql.append(" AND ds.id_docto_servico = mde.id_docto_servico (+)");
        sql.append(" AND dev.id_filial = regf.id_filial (+)");
        sql.append(" AND regf.id_regional = reg.id_regional (+)");
        sql.append(" AND ds.nr_docto_servico > 0");
        sql.append("     AND ( con.tp_situacao_conhecimento IN ( 'E', 'B' )");
        sql.append("       OR con.tp_situacao_conhecimento IS NULL )");
        sql.append(" AND ( nfs.tp_situacao_nf = 'E'");
        sql.append("       OR nfs.tp_situacao_nf IS NULL )");
        sql.append(" AND dev.tp_situacao_cobranca IN ( 'P', 'C' )");

        if (tfm.get("filial") != null) {
            sql.append("   AND fcobcli.id_filial = :id_filial_resp");
            params.put("id_filial_resp", tfm.getLong("filial"));
        }
        if (tfm.get("tpCliente") != null) {
            sql.append("   AND cl.tp_cliente = :tp_cliente");
            params.put("tp_cliente", tfm.getString("tpCliente"));
        }
        if (tfm.get("classificacaoCliente") != null) {
            sql.append("   AND nvl(ecf.tp_cliente, 'G') = :tp_cliente_financ");
            params.put("tp_cliente_financ", tfm.getString("classificacaoCliente"));
        }
        if (tfm.get("tpCobranca") != null) {
            sql.append("   AND cl.tp_cobranca = :tp_cobranca");
            params.put("tp_cobranca", tfm.getString("tpCobranca"));
        }
        if (tfm.get("blCobrancaCentralizada") != null) {
            sql.append("   AND cl.bl_cobranca_centralizada = :blCobrancaCentralizada");
            params.put("blCobrancaCentralizada", tfm.getString("blCobrancaCentralizada"));
        }
        if (tfm.get("blPreFatura") != null) {
            sql.append("   AND cl.bl_pre_fatura = :bl_pre_fatura");
            params.put("bl_pre_fatura", tfm.getString("blPreFatura"));
        }

        if (tfm.get("estadoCobranca") != null) {
            if ("AF".equals(tfm.getString("estadoCobranca"))) {
                sql.append("   AND dev.tp_situacao_cobranca IN ('P', 'C')");

            } else if ("FA".equals(tfm.getString("estadoCobranca"))) {
                sql.append("   AND dev.tp_situacao_cobranca  = 'F'");

            } else if ("PR".equals(tfm.getString("estadoCobranca"))) {
                sql.append("   AND dev.tp_situacao_cobranca IN ('P', 'C', 'F')");
            }
        }

        if (tfm.get("devedoresExcluir") != null) {
            String[] devedores = ((String) tfm.get("devedoresExcluir")).split(";");
            if (devedores.length > 0 && !devedores[0].isEmpty()) {
                sql.append("   AND (pdev.nr_identificacao LIKE -1");
                for (String devedor : devedores) {
                    sql.append(" OR pdev.nr_identificacao NOT LIKE '" + devedor + "' ");
                }
                sql.append(" ) ");
            }
        }
        if (tfm.get("devedoresListar") != null) {
            String[] devedores = ((String) tfm.get("devedoresListar")).split(";");
            if (devedores.length > 0 && !devedores[0].isEmpty()) {
                sql.append("   AND (pdev.nr_identificacao LIKE -1");
                for (String devedor : devedores) {
                    sql.append(" OR pdev.nr_identificacao like '" + devedor + "' ");
                }
                sql.append(" ) ");
            }
        }

        if (tfm.get("idRegional") != null) {
            sql.append("   AND reg.id_regional = :id_regional");
            params.put("id_regional", tfm.getLong("idRegional"));
        }
        if (tfm.get("idFilialCobranca") != null) {
            sql.append("   AND fcobcli.id_filial = :id_filial_cob");
            params.put("id_filial_cob", tfm.getLong("idFilialCobranca"));
        }
        if (tfm.get("dtVigenciaInicial") != null) {
            sql.append("   AND trunc(CAST(ds.dh_emissao AS DATE)) >= :dt_inicial");
            params.put("dt_inicial", tfm.get("dtVigenciaInicial"));
        }
        if (tfm.get("dtVigenciaFinal") != null) {
            sql.append("   AND trunc(CAST(ds.dh_emissao AS DATE)) <= :dt_final");
            params.put("dt_final", tfm.get("dtVigenciaFinal"));
        }
        if (tfm.get("idServico") != null) {
            sql.append("   AND ds.id_servico = :idServico");
            params.put("idServico", tfm.getLong("idServico"));
        }
        if (tfm.get("tpDocumento") != null) {
            sql.append("   AND ds.tp_documento_servico = :tpDocumento");
            params.put("tpDocumento", tfm.getString("tpDocumento"));
        }
        if (tfm.get("tpFrete") != null) {
            sql.append("   AND con.tp_frete = :tpFrete");
            params.put("tpFrete", tfm.getString("tpFrete"));
        }
        if (tfm.get("tpConhecimento") != null) {
            sql.append("   AND con.tp_conhecimento = :tpConhecimento");
            params.put("tpConhecimento", tfm.getString("tpConhecimento"));
        }
        if (tfm.get("tpCalculo") != null) {
            sql.append("   AND ds.tp_calculo_preco = :tpCalculo");
            params.put("tpCalculo", tfm.getString("tpCalculo"));
        }
        if (tfm.get("blBloqueado") != null) {
            sql.append("   AND nvl((SELECT 'S'");
            sql.append("             FROM bloqueio_faturamento bf");
            sql.append("            WHERE bf.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat");
            sql.append("              AND bf.dh_desbloqueio IS NULL");
            sql.append("              AND rownum = 1),");
            sql.append("           'N') = :blBloqueado");
            params.put("blBloqueado", tfm.getString("blBloqueado"));
        }
        if (tfm.get("blAgendaTransferencia") != null) {
            sql.append("   AND nvl((SELECT 'S'");
            sql.append("             FROM agenda_transferencia ag");
            sql.append("            WHERE ag.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat");
            sql.append("              AND rownum = 1),");
            sql.append("           'N') = :blAgendaTransferencia");
            params.put("blAgendaTransferencia", tfm.getString("blAgendaTransferencia"));
        }
        if (tfm.get("blTransferenciaPendentes") != null) {
            sql.append("   AND nvl((SELECT 'S'");
            sql.append("             FROM transferencia      tr,");
            sql.append("                  item_transferencia itr");
            sql.append("            WHERE tr.id_transferencia = itr.id_transferencia");
            sql.append("              AND itr.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat");
            sql.append("              AND tr.tp_situacao_transferencia = 'PR'");
            sql.append("              AND rownum = 1),");
            sql.append("           'N') = :blTransferenciaPendentes");
            params.put("blTransferenciaPendentes", tfm.getString("blTransferenciaPendentes"));
        }
        if (tfm.get("filialCobDifResp") != null) {
            if (tfm.getBoolean("filialCobDifResp")) {
                sql.append(" AND  FCOB.ID_FILIAL <> FCOBCLI.ID_FILIAL ");
            } else {
                sql.append(" AND FCOB.ID_FILIAL = FCOBCLI.ID_FILIAL ");
            }
        }
        return sql;
    }

    private StringBuilder createDoctoNaoFaturadoProjection(TypedFlatMap tfm) {
        StringBuilder sql = new StringBuilder();
        if (tfm.get("idFilialCobranca") != null) {
            sql.append("SELECT /*+ leading(fcob dev) use_nl(fcob dev nfs) index(dev DDSF_IDX_02)  */");
        } else {
            sql.append("SELECT /*+ leading(reg regf dev) use_nl(reg regf dev nfs) index(dev DDSF_IDX_02) */");
        }
        sql.append("       ds.id_docto_servico,");
        sql.append("       dev.id_devedor_doc_serv_fat,");
        sql.append("       (SELECT vi18n(ds_valor_dominio_i)");
        sql.append("          FROM valor_dominio");
        sql.append("         WHERE id_dominio IN");
        sql.append("               (SELECT id_dominio");
        sql.append("                  FROM dominio");
        sql.append("                 WHERE nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO')");
        sql.append("           AND vl_valor_dominio = ds.tp_documento_servico) tpDocumento,");
        sql.append("       forig.sg_filial,");
        sql.append("       ds.nr_docto_servico,");
        sql.append("       fdest.sg_filial as sg_filial_destino,");
        sql.append("       to_char(trunc(CAST(ds.dh_emissao AS DATE)),'dd/MM/yyyy') as dtEmissao,");
        sql.append("       (SELECT vi18n(ds_valor_dominio_i)");
        sql.append("          FROM valor_dominio");
        sql.append("         WHERE id_dominio IN");
        sql.append("               (SELECT id_dominio");
        sql.append("                  FROM dominio");
        sql.append("                 WHERE nm_dominio = 'DM_TIPO_FRETE')");
        sql.append("           AND vl_valor_dominio = con.tp_frete) tpFrete,");
        sql.append("       (SELECT vi18n(ds_valor_dominio_i)");
        sql.append("          FROM valor_dominio");
        sql.append("         WHERE id_dominio IN");
        sql.append("               (SELECT id_dominio");
        sql.append("                  FROM dominio");
        sql.append("                 WHERE nm_dominio = 'DM_TIPO_CONHECIMENTO')");
        sql.append("           AND vl_valor_dominio = con.tp_conhecimento) tpConhecimento,");
        sql.append("       TO_CHAR((SELECT trunc(CAST(MAX(med.dh_ocorrencia) AS DATE)) dh_ocorrencia");
        sql.append("          FROM manifesto_entrega_documento med");
        sql.append("         WHERE med.id_ocorrencia_entrega = 5");
        sql.append("           AND med.tp_situacao_documento <> 'CANC'");
        sql.append("           AND med.id_docto_servico = ds.id_docto_servico");
        sql.append("           AND rownum = 1),'dd/MM/yyyy' ) dtEntrega,");
        sql.append("       pdev.nr_identificacao ||' - '||pdev.nm_pessoa as devedor,");
        sql.append("       fcobcli.sg_filial sg_filial_resp,");
        sql.append("       fcob.sg_filial sg_filial_cob,");
        sql.append("       TRIM(TO_CHAR(dev.vl_devido,'9G999G999G999G999G990D00')) as vl_devido,");
        sql.append("      (select mdocel.NR_CHAVE from MONITORAMENTO_DOC_ELETRONICO mdocel where mdocel.ID_DOCTO_SERVICO =  ds.id_docto_servico) as chaveCTE ");
        return sql;
    }

    /*
     * Edenilson
	 * Mesmo comportamento do m?todo findDoctoServicoOriginal, retornando POJO
	 * findDoctoServicoOriginal retornava POJO e a altera??o para MAP quebrou um item
	 * */
    public List<DoctoServico> findDoctoServicoOriginalPojo(TypedFlatMap tfm) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection(" ds ");

        sql.addFrom(getPersistentClass().getName() + " ds " +
                " join fetch ds.devedorDocServFats df " +
                " join fetch ds.filialByIdFilialOrigem fil");

        sql.addCriteria("ds.doctoServicoOriginal.id", "=", tfm.getLong("idDoctoServico"));

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    @Override
    protected DoctoServico findById(Long id) {
        return (DoctoServico) (getAdsmHibernateTemplate().get(getPersistentClass(), id));
    }

    public DoctoServico findDoctoServicoByTpDocumento(Long idDoctoServico) {
        StringBuilder sb = new StringBuilder()
                .append(" select d")
                .append(" from " + DoctoServico.class.getName() + " as d")
                .append(" where d.id = ?");

        List<DoctoServico> list = getAdsmHibernateTemplate().find(sb.toString(), idDoctoServico);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Retorna os documentos de servico que tenha como destino a rota de viagem especificada.
     * Retorna apenas os documentos que tenham prioridade alta.
     *
     * @param idFilial
     * @return
     */
    public List<Map<String, Object>> findDoctoServicoWithServicoPrioritario(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega) {
    	Map<String, Object> parametersValues = new HashMap<>();

        StringBuilder sql = new StringBuilder();
        createProjectionDpeAtrasoServicoPrioritario(sql);
        sql.append(this.getSqlDoctoServicoWithServicoPrioritario(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, parametersValues));

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, configureSqlQueryDpeAtrasadoServicoPrioritario());
    }

    /**
     * Consulta paginada que retorna documentos de servi?o com servi?o priorit?rio a a partir de informa??es de carregamento
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithServicoPrioritario(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, FindDefinition findDefinition) {
        List<Object> param = new ArrayList<>();

        StringBuilder sql = new StringBuilder()
                .append("select new map(")
                .append("ds.filialByIdFilialOrigem.sgFilial as sgFilialDocumento, ")
                .append("ds.nrDoctoServico as nrConhecimento ")
                .append(") ")
                .append(this.getHqlDoctoServicoWithServicoPrioritario(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, param));

        return getAdsmHibernateTemplate().findPaginated(
                sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }

    /**
     * Retorna HQL comum aos finds WithServicoPrioritario
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param param
     * @return
     */
    private String getHqlDoctoServicoWithServicoPrioritario(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, List<Object> param) {
        StringBuilder sql = new StringBuilder()
                .append("from ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("inner join ds.filialByIdFilialOrigem as filialOrigem ")
                .append("inner join ds.localizacaoMercadoria as lm ")
                .append("inner join ds.servico as servico ")
                .append("inner join servico.tipoServico as ts ")
                .append("where ")
                .append("ds.nrDoctoServico > 0 ")
                .append("and ds.filialLocalizacao.id = ? ")
                .append("and (lm.cdLocalizacaoMercadoria = 24 or lm.cdLocalizacaoMercadoria = 34) ")
                .append("and ts.blPriorizar = ? ");

        param.add(idFilial);
        param.add(Boolean.TRUE);

        populateHqlFluxoFilial(tpControleCarga, listaManifestos, idRotaColetaEntrega, sql, param);

        return sql.toString();
    }


    /**
     * Retorna os documentos de servico que tenha como destino a rota de viagem especificada
     * Retorna apenas os documentos que tenham sua DPE (data previsao entegra) igual ou menor que a data de hoje.
     *
     * @param idFilial
     * @return
     */
    public List<Map<String, Object>> findDoctoServicoWithDpeAtrasado(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega) {
    	Map<String, Object> parametersValues = new HashMap<>();
    	
        StringBuilder sql = new StringBuilder();
        createProjectionDpeAtrasoServicoPrioritario(sql);
        sql.append(this.getSqlDoctoServicoWithDpeAtrasado(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, parametersValues));

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(),parametersValues, configureSqlQueryDpeAtrasadoServicoPrioritario());
    }

    private void createProjectionDpeAtrasoServicoPrioritario(StringBuilder sql) {
    	sql.append("SELECT ")
           .append("ds.ID_DOCTO_SERVICO as idDoctoServico, ")
           .append("ds.TP_DOCUMENTO_SERVICO as tpDocumentoServico, ")
           .append("(SELECT SG_FILIAL FROM FILIAL f WHERE f.ID_FILIAL=ds.ID_FILIAL_ORIGEM) as sgFilialOrigem, ")
           .append("ds.NR_DOCTO_SERVICO as nrDoctoServico ");
    	montarTpDocumentoServicoDominio(sql);
    }

    /**
     * Consulta paginada que retorna documentos de servi?o com DPE atrasado a a partir de informa??es de carregamento
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithDpeAtrasado(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, FindDefinition findDefinition) {
        List<Object> param = new ArrayList<Object>();        
        StringBuilder sql = new StringBuilder()
                .append("select new map( ")
                .append("ds.filialByIdFilialOrigem.sgFilial as sgFilialDocumento, ")
                .append("ds.nrDoctoServico as nrConhecimento ")
                .append(") ")
                .append(this.getHqlDoctoServicoWithDpeAtrasado(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, param));


        return getAdsmHibernateTemplate().findPaginated(
                sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }

    /**
     * Retorna HQL comum aos finds Documentos de Servi?o com DPE Atrasado
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param param
     * @return
     */
    private String getHqlDoctoServicoWithDpeAtrasado(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, List<Object> param) {
        StringBuilder sql = new StringBuilder()
                .append("from ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("where ")
                .append("exists (select 1 from ")
                .append(LocalizacaoMercadoria.class.getName()).append(" as lm ")
                .append("where ds.localizacaoMercadoria = lm and (lm.cdLocalizacaoMercadoria = 24 or lm.cdLocalizacaoMercadoria = 34) ")
                .append("and ds.nrDoctoServico > 0 ")
                .append("and ds.filialLocalizacao.id = ? ")                
                .append("and ds.dtPrevEntrega <= ? ");

        param.add(idFilial);
        param.add(JTDateTimeUtils.getDataAtual());

        populateHqlFluxoFilial(tpControleCarga, listaManifestos, idRotaColetaEntrega, sql, param);
        
        return sql.toString();
    }
    
    
    
    private String getSqlDoctoServicoWithDpeAtrasado(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, Map<String, Object> parametersValues) {
    	StringBuilder sql = new StringBuilder()
    			.append(" FROM DOCTO_SERVICO ds ")    			
    			.append(clausulaWhere())    			
    			.append("AND ds.DT_PREV_ENTREGA <= :dataAtual ");
    	
    	parametersValues.put("idFilial", idFilial);
    	parametersValues.put("dataAtual", JTDateTimeUtils.getDataAtual());
    	
    	populateSqlFluxoFilial(tpControleCarga, listaManifestos, idRotaColetaEntrega, sql, parametersValues);
    			
    	return sql.toString();
    }
    
    private String clausulaWhere() {
    	StringBuilder clausula = new StringBuilder(350)
    			.append(" WHERE ")
    			.append(" ds.ID_FILIAL_LOCALIZACAO = :idFilial " )
    			.append("AND ds.NR_DOCTO_SERVICO > 0 ")
    			.append("AND EXISTS(SELECT 1 FROM LOCALIZACAO_MERCADORIA lm")
				.append("        	WHERE ds.ID_LOCALIZACAO_MERCADORIA = lm.ID_LOCALIZACAO_MERCADORIA") 
				.append("          		AND (lm.CD_LOCALIZACAO_MERCADORIA = 24") 
				.append("					 OR lm.CD_LOCALIZACAO_MERCADORIA      =34)) ");
    	
    	return clausula.toString();
    }
    
    private String getSqlDoctoServicoWithServicoPrioritario(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, Map<String, Object> parametersValues) {
    	StringBuilder sql = new StringBuilder()
    			.append(" FROM ")
    			.append(" DOCTO_SERVICO ds")
    			.append("  INNER JOIN SERVICO s ")
    			.append("   ON ds.ID_SERVICO = s.ID_SERVICO ")
    			.append("  INNER JOIN TIPO_SERVICO ts")
    			.append("   ON s.ID_TIPO_SERVICO = ts.ID_TIPO_SERVICO")
    			.append(clausulaWhere())    			
    			.append("AND ts.BL_PRIORIZAR = 'S' ");
    	
    	parametersValues.put("idFilial", idFilial);
    	
		populateSqlFluxoFilial(tpControleCarga, listaManifestos, idRotaColetaEntrega, sql, parametersValues);
		
    	return sql.toString();
    }
    /**
     * Faz uma busca paginada retornando os documentos de servi?o de um determinado controle de carga onde n?o foram carregados todos os volumes
     * do documento, isto ?, onde a quantidades de volumes do documento ? diferente da quantidade de volumes carregados no controle de carga
     *
     * @param idControleCarga
     * @param findDef
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithDivergenciaCarregamento(Long idControleCarga, FindDefinition findDef) {
        StringBuilder commonSql = new StringBuilder()
                .append("from  ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("inner join ds.preManifestoVolumes pmv ")
                .append("inner join pmv.manifesto m ")
                .append("inner join ds.filialByIdFilialOrigem filialOrigem ")
                .append("where ")
                .append("	( ")
                .append("		select count(*)")
                .append("		from ")
                .append(PreManifestoVolume.class.getName()).append(" as ipmv ")
                .append("		where ")
                .append("			ipmv.doctoServico.id = ds.id ")
                .append("		and ipmv.manifesto.controleCarga.id = m.controleCarga.id ")
                .append(" 	)")
                .append("	 between 1 and ds.qtVolumes-1 ")
                .append("and m.controleCarga.id = ? ");

        StringBuilder sql = new StringBuilder()
                .append("select new map (")
                .append("	filialOrigem.sgFilial as sgFilialDocumento, ")
                .append("	ds.nrDoctoServico as nrConhecimento, ")
                .append("	ds.qtVolumes as qtVolumes, ")
                .append("	count(distinct pmv.id) as qtVolumesCarregados ")
                .append(") ")
                .append(commonSql)
                .append("group by ")
                .append("	filialOrigem.sgFilial, ds.nrDoctoServico, ds.qtVolumes ");

     	/* RowCount, para pegar a quantidade de documentos de servi?o com diverg?ncia no carregamento */
        StringBuilder sqlRowCount = new StringBuilder()
                .append("select count(distinct ds) as rowCount ")
                .append(commonSql);

        List<Object> params = new ArrayList<Object>();
        params.add(idControleCarga);

        ResultSetPage<Map<String, Object>> rs = getAdsmHibernateTemplate().findPaginated(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), params.toArray());
        Long rowCount = (Long) getAdsmHibernateTemplate().findUniqueResult(sqlRowCount.toString(), params.toArray());

        return new ResultSetPage<Map<String, Object>>(rs.getCurrentPage(), rs.getHasPriorPage(), rs.getHasNextPage(), rs.getList(), rowCount);
    }
   
    public List<Map<String, Object>> findDoctoServicoWithWithPriorizacaoEmbarque(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega) {
        
        Map<String, Object> parametersValues = new HashMap<>();
        StringBuilder sql = new StringBuilder()
                .append("SELECT ")
                .append("rpd.ID_REGISTRO_PRIORIZACAO_DOCTO as idRegistroPriorizacaoDocto, ")
                .append("ds.ID_DOCTO_SERVICO as idDoctoServico, ")
                .append("ds.TP_DOCUMENTO_SERVICO as tpDocumentoServico, ")
                .append("f.SG_FILIAL as sgFilialOrigem, ")
                .append("ds.NR_DOCTO_SERVICO as nrDoctoServico ");
        montarTpDocumentoServicoDominio(sql);
        sql.append(this.getSqlDoctoServicoWithPriorizacaoEmbarque(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, parametersValues));

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, configureSqlQueryWithPriorizacaoEmbarqueo()); //.find(sql.toString(), params.toArray());
    }
    
    private void montarTpDocumentoServicoDominio(StringBuilder sql) {
    	sql.append(", (SELECT vd.DS_VALOR_DOMINIO_I FROM")
    	   .append("   DOMINIO d INNER JOIN VALOR_DOMINIO vd")
    	   .append("    ON d.ID_DOMINIO = vd.ID_DOMINIO")
    	   .append(" WHERE NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO'")
    	   .append("   AND vd.TP_SITUACAO = 'S'")
    	   .append("   AND vd.VL_VALOR_DOMINIO = ds.TP_DOCUMENTO_SERVICO) as tpDocumentoServicoDescription ");
    }
    
    /**
     * Consulta paginada para Documentos de Servico que possuem prioriza??o de embarque, a partir de informa??es de carregamento
     *
     * @param idFilial
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param findDefinition
     * @return
     */
    public ResultSetPage<Map<String, Object>> findPaginatedDoctoServicoWithPriorizacaoEmbarque(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, FindDefinition findDefinition) {
        List<Object> param = new ArrayList<Object>();

        StringBuilder sql = new StringBuilder()
                .append("select new map(")
                .append("filialOrigem.sgFilial as sgFilialDocumento, ")
                .append("ds.nrDoctoServico as nrConhecimento ")
                .append(") ")
                .append(this.getHqlDoctoServicoWithPriorizacaoEmbarque(idFilial, tpControleCarga, listaManifestos, idRotaColetaEntrega, param));


        return getAdsmHibernateTemplate().findPaginated(
                sql.toString(), sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
    }

    private String getHqlDoctoServicoWithPriorizacaoEmbarque(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, List<Object> param) {
        StringBuilder sql = new StringBuilder()
                .append("from ")
                .append(RegistroPriorizacaoDocto.class.getName()).append(" as rpd ")
                .append("inner join rpd.doctoServico as ds ")
                .append("inner join ds.filialByIdFilialOrigem as filialOrigem ")
                .append("inner join rpd.registroPriorizacaoEmbarq rpe ")
                .append("inner join ds.localizacaoMercadoria lm ")
                .append("where ")
                .append("NVL(TO_CHAR(rpe.dhCancelamento.value, 'yyyy-MM-dd'), '1900-01-01') = '1900-01-01' ")
                .append("and ds.filialLocalizacao.id = ? ")
               // .append("and lm.blVerificacaoDpe = 'S' ")
                 .append("and (lm.cdLocalizacaoMercadoria = 24 OR lm.cdLocalizacaoMercadoria = 34) ")
                .append("and ds.nrDoctoServico > 0 ");

        param.add(idFilial);

        populateHqlFluxoFilial(tpControleCarga, listaManifestos, idRotaColetaEntrega, sql, param);
        return sql.toString();
    }
    
    private String getSqlDoctoServicoWithPriorizacaoEmbarque(Long idFilial, String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, Map<String, Object> params) {
        StringBuilder sql = new StringBuilder()
        		.append("FROM  ")
        		.append("REGISTRO_PRIORIZACAO_EMBARQ rpe")  
        		.append("    INNER JOIN REGISTRO_PRIORIZACAO_DOCTO rpd") 
        		.append("      ON rpe.ID_REGISTRO_PRIORIZACAO_EMBARQ = rpd.ID_REGISTRO_PRIORIZACAO_EMBARQ") 
        		.append("    INNER JOIN DOCTO_SERVICO ds") 
        		.append("      ON rpd.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO") 
        		.append("    INNER JOIN LOCALIZACAO_MERCADORIA lm") 
        		.append("      ON ds.ID_LOCALIZACAO_MERCADORIA = lm.ID_LOCALIZACAO_MERCADORIA") 
        		.append("    INNER JOIN FILIAL f ") 
        		.append("      ON ds.ID_FILIAL_ORIGEM = f.ID_FILIAL")
        		.append(" WHERE ")
        		.append("NVL(to_char(rpe.DH_CANCELAMENTO, 'yyyy-MM-dd'), '1900-01-01') = '1900-01-01' ")
        		.append("AND ds.ID_FILIAL_LOCALIZACAO = :idFilialLocalizacao ")
        		.append("AND (lm.CD_LOCALIZACAO_MERCADORIA =24 OR lm.CD_LOCALIZACAO_MERCADORIA=34) ")
        		.append("AND ds.NR_DOCTO_SERVICO >0 ");
        
        params.put("idFilialLocalizacao", idFilial);
        
        populateSqlFluxoFilial(tpControleCarga, listaManifestos, idRotaColetaEntrega, sql, params);
        return sql.toString();
    }
    
    /**
     * @param tpControleCarga
     * @param listaManifestos
     * @param idRotaColetaEntrega
     * @param sql
     * @param param
     */
    private void populateHqlFluxoFilial(String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, StringBuilder sql, List<Object> param) {
        if ("V".equals(tpControleCarga)) {
            sql.append("and ds.blBloqueado = ? ");
            param.add(Boolean.FALSE);
            List<Long> idFilialOrigem = new ArrayList<>(1);
            List<Long> idsFiliaisDestino = povoarListIdFiliaisDestinoParametroIn(listaManifestos, idFilialOrigem);
            
            sql.append("and exists( ")
                    .append("select 1 from ").append(OrdemFilialFluxo.class.getName()).append(" offo, ")
                    .append(OrdemFilialFluxo.class.getName()).append(" offd ")
                    .append("inner join offo.fluxoFilial as ff ")
                    .append("where offo.fluxoFilial = offd.fluxoFilial ")
                    .append("and ff.id = ds.fluxoFilial.id ")
                    .append("and offd.nrOrdem > offo.nrOrdem ")
                    .append("and offo.filial.id = ? ")
                    .append("and offd.filial.id in (?)")
                    .append(")");

            param.add(idFilialOrigem.get(idFilialOrigem.size()-1));
            param.add(idsFiliaisDestino);
        } else {
            sql.append("and ds.rotaColetaEntregaByIdRotaColetaEntregaReal.id = ? ");
            param.add(idRotaColetaEntrega);
        }
    }
    
    private void populateSqlFluxoFilial(String tpControleCarga, List listaManifestos, Long idRotaColetaEntrega, StringBuilder sql, Map<String, Object> params) {
        if ("V".equals(tpControleCarga)) {
        	sql.append("AND ds.BL_BLOQUEADO = 'N' ");
            
            List<Long> idFilialOrigem = new ArrayList<>(1);
            List<Long> idsFiliaisDestino = povoarListIdFiliaisDestinoParametroIn(listaManifestos, idFilialOrigem);
            
            sql.append("and exists( ")
	            .append("SELECT 1 FROM ")
	            .append("ORDEM_FILIAL_FLUXO offo, ")
	            .append("ORDEM_FILIAL_FLUXO offd, ")
	            .append("FLUXO_FILIAL ff")	            
	            .append(" WHERE ")
	            .append("  offo.ID_FLUXO_FILIAL = ff.ID_FLUXO_FILIAL") 
	            .append("  AND offo.ID_FLUXO_FILIAL   = offd.ID_FLUXO_FILIAL") 
	            .append("  AND ff.ID_FLUXO_FILIAL     = ds.ID_FLUXO_FILIAL")
	            .append("  AND offd.NR_ORDEM          > offo.NR_ORDEM ")
	            .append("  AND offo.ID_FILIAL = :idFilialOrigem")  
	            .append("  AND offd.ID_FILIAL in (:idsFiliaisDestino)")
	            .append(")");

            params.put("idFilialOrigem", idFilialOrigem.get(idFilialOrigem.size()-1));
            params.put("idsFiliaisDestino", idsFiliaisDestino);
        } else {
            sql.append("AND ds.ID_ROTA_COLETA_ENTREGA_REAL = :idRotaColetaEntrega ");
            params.put("idRotaColetaEntrega", idRotaColetaEntrega);
        }
    }
    
    private List<Long> povoarListIdFiliaisDestinoParametroIn(List listaManifestos, List<Long> idFilialOrigem) {
    	List<Long> idsFiliaisDestino = new ArrayList<>();
    	int sizeListaManifestos = listaManifestos.size(); 
        for (int index = 0; index < sizeListaManifestos; index++) {
            Map mapManifesto = (Map) listaManifestos.get(index);
            idFilialOrigem.add((Long)mapManifesto.get("idFilialOrigem"));
            idsFiliaisDestino.add((Long)mapManifesto.get("idFilialDestino"));
        }
        
        return idsFiliaisDestino;
    }
    
    
    /**
     * Obt?m o documento de servico, agregando a filial origem e tamb?m a moeda.
     *
     * @param idDoctoServico
     * @return
     */
    public DoctoServico findByIdJoinFilial(Long idDoctoServico) {
        Criteria criteria = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(getPersistentClass());
        criteria.createAlias("filialByIdFilialOrigem", "filialOrigem");
        criteria.createAlias("filialOrigem.pessoa", "filialOrigemPessoa");
        criteria.createAlias("filialByIdFilialDestino", "filialDestino", Criteria.LEFT_JOIN);
        criteria.createAlias("filialDestino.pessoa", "filialDestinoPessoa", Criteria.LEFT_JOIN);
        criteria.createAlias("moeda", "moeda");
        criteria.add(Restrictions.eq("id", idDoctoServico));
        return (DoctoServico) criteria.uniqueResult();
    }

    /**
     * Metodo gerando overload de dados, otimizar pontualmente a projecao para para cada caso
     *
     * @see novos m?todos findDoctoServicoByIdManifesto
     * @deprecated
     */
    @Deprecated
    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto) {
        Set<DoctoServico> result = new HashSet<DoctoServico>();

        //Conhecimento Nacional
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoNacionalCto.class, "mnc");
        dc.setProjection(Projections.property("mnc.conhecimento"));
        dc.createAlias("mnc.conhecimento", "ds");
        dc.add(Restrictions.eq("mnc.manifestoViagemNacional.id", idManifesto));
        dc.add(Restrictions.gt("ds.nrConhecimento", LongUtils.ZERO));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        //Conhecimento Internacional
        dc = DetachedCriteria.forClass(ManifestoInternacCto.class, "mic");
        dc.setProjection(Projections.property("mic.ctoInternacional"));
        dc.createAlias("mic.ctoInternacional", "ds");
        dc.add(Restrictions.eq("mic.manifestoViagemInternacional.id", idManifesto));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        //Manifesto de entrega
        dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med");
        dc.setProjection(Projections.property("med.doctoServico"));
        dc.createAlias("med.doctoServico", "ds");
        dc.add(Restrictions.eq("med.manifestoEntrega.id", idManifesto));
        dc.add(Restrictions.gt("ds.nrDoctoServico", LongUtils.ZERO));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        //Pre Manifesto
        dc = DetachedCriteria.forClass(PreManifestoDocumento.class, "pmd");
        dc.setProjection(Projections.property("pmd.doctoServico"));
        dc.createAlias("pmd.doctoServico", "ds");
        dc.add(Restrictions.eq("pmd.manifesto.id", idManifesto));
        dc.add(Restrictions.gt("ds.nrDoctoServico", LongUtils.ZERO));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        return new ArrayList<DoctoServico>(result);
    }

    /**
     * Busca conhecimentos do manifesto
     *
     * @param idManifesto
     * @param projection
     * @param alias
     * @param criterions
     * @return
     * @author Andr? Valadas
     */
    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions) {
        return findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterions, false);
    }

    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions, boolean validateDoctosEntregues) {
        return findDoctoServicoByIdManifesto(idManifesto, projection, alias, criterions, validateDoctosEntregues, false, null);
    }

    public List<DoctoServico> findDoctoServicoByIdManifesto(final Long idManifesto, final Projection projection, final Map<String, String> alias, final List<Criterion> criterions, boolean validateDoctosEntregues, boolean validateDoctosDescarrecados, DateTime dhInicioOperacao) {
        Set<DoctoServico> result = new HashSet<DoctoServico>();

        //Conhecimento Nacional
        DetachedCriteria dc = createConhecimentoNacionalCommonQuery(idManifesto);
        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);

        if (validateDoctosEntregues) {
            dc.add(getSubqueryDoctoEntregues());
        }
        if (validateDoctosDescarrecados) {
            dc.add(getSubqueryDoctoDescarrecados(dhInicioOperacao));
        }
        dc.setResultTransformer(new AliasToNestedBeanResultTransformer(DoctoServico.class));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        //Conhecimento Internacional
        dc = createConhecimentoInternacionalCommonQuery(idManifesto);
        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);

        if (validateDoctosEntregues) {
            dc.add(getSubqueryDoctoEntregues());
        }
        if (validateDoctosDescarrecados) {
            dc.add(getSubqueryDoctoDescarrecados(dhInicioOperacao));
        }
        dc.setResultTransformer(new AliasToNestedBeanResultTransformer(DoctoServico.class));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        //Manifesto de entrega
        dc = createManifestoEntregaCommonQuery(idManifesto);
        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);

        if (validateDoctosEntregues) {
            dc.add(getSubqueryDoctoEntregues());
        }
        if (validateDoctosDescarrecados) {
            dc.add(getSubqueryDoctoDescarrecados(dhInicioOperacao));
        }
        dc.setResultTransformer(new AliasToNestedBeanResultTransformer(DoctoServico.class));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        //Pre Manifesto
        dc = createPreManifestoCommonQuery(idManifesto);
        configProjection(dc, projection);
        configAlias(dc, alias);
        configCriterions(dc, criterions);

        if (validateDoctosEntregues) {
            dc.add(getSubqueryDoctoEntregues());
        }
        if (validateDoctosDescarrecados) {
            dc.add(getSubqueryDoctoDescarrecados(dhInicioOperacao));
        }
        dc.setResultTransformer(new AliasToNestedBeanResultTransformer(DoctoServico.class));
        result.addAll(getAdsmHibernateTemplate().findByDetachedCriteria(dc));

        return new ArrayList<DoctoServico>(result);
    }

    //LMS-4506
    private Criterion getSubqueryDoctoEntregues() {
        DetachedCriteria subquery = DetachedCriteria.forClass(EventoDocumentoServico.class, "eds");
        subquery.createAlias("eds.evento", "ev");
        subquery.add(Restrictions.eqProperty("eds.doctoServico.id", "ds.id"));
        subquery.add(Restrictions.and(
                Restrictions.eq("ev.cdEvento", ConstantesSim.EVENTO_ENTREGA),
                Restrictions.eq("eds.blEventoCancelado", Boolean.FALSE)));
        subquery.setProjection(Projections.property("eds.id"));
        return Subqueries.notExists(subquery);
    }

    private Criterion getSubqueryDoctoDescarrecados(DateTime dhInicioOperacao) {
        DetachedCriteria dc = DetachedCriteria.forClass(EventoDocumentoServico.class, "edser");
        dc.setProjection(Projections.property("edser.idEventoDocumentoServico"));
        dc.createAlias("edser.evento", "e");
        dc.createAlias("edser.doctoServico", "dseds");
        dc.add(Restrictions.eqProperty("dseds.id", "ds.id"));
        dc.add(Restrictions.eq("edser.filial.id", SessionUtils.getFilialSessao().getIdFilial()));
        dc.add(Restrictions.in("e.cdEvento", new Short[]{ConstantesSim.EVENTO_DESCARGA_CONCLUIDA, ConstantesSim.EVENTO_FIM_DESCARGA_SEM_MUDAR_LOCALIZACAO}));
        dc.add(Restrictions.eq("edser.blEventoCancelado", Boolean.FALSE));

        dc.add(Restrictions.ge("edser.dhInclusao", dhInicioOperacao));
        dc.add(Restrictions.le("edser.dhInclusao", JTDateTimeUtils.getDataHoraAtual()));

        return Subqueries.notExists(dc);
    }

    /**
     * Verifica se existe conhecimentos para o Manifesto
     *
     * @param idManifesto
     * @param alias
     * @param criterions
     * @return
     * @author Andr? Valadas
     */
    public Boolean verifyDoctoServicoByIdManifesto(final Long idManifesto, final Map<String, String> alias, final List<Criterion> criterions) {

        //Conhecimento Nacional
        DetachedCriteria dc = createConhecimentoNacionalCommonQuery(idManifesto);
        dc.setProjection(Projections.count("mnc.conhecimento.id"));
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        Integer rowCount = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
        if (IntegerUtils.hasValue(rowCount)) {
            return Boolean.TRUE;
        }

        //Conhecimento Internacional
        dc = createConhecimentoInternacionalCommonQuery(idManifesto);
        dc.setProjection(Projections.count("mic.ctoInternacional.id"));//proje??o
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        rowCount = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
        if (IntegerUtils.hasValue(rowCount)) {
            return Boolean.TRUE;
        }

        //Manifesto de entrega
        dc = createManifestoEntregaCommonQuery(idManifesto);
        dc.setProjection(Projections.count("med.doctoServico.id"));//proje??o
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        rowCount = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
        if (IntegerUtils.hasValue(rowCount)) {
            return Boolean.TRUE;
        }

        //Pre Manifesto
        dc = createPreManifestoCommonQuery(idManifesto);
        dc.setProjection(Projections.count("pmd.doctoServico.id"));//proje??o
        configAlias(dc, alias);
        configCriterions(dc, criterions);
        rowCount = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
        if (IntegerUtils.hasValue(rowCount)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Metodos para consulta de "comum" de conhecimento por Manifesto
     *
     * @param idManifesto
     * @return
     * @author Andr? Valadas
     */
    private DetachedCriteria createConhecimentoNacionalCommonQuery(final Long idManifesto) {
        //Conhecimento Nacional
        final DetachedCriteria dc = DetachedCriteria.forClass(ManifestoNacionalCto.class, "mnc");
        dc.createAlias("mnc.conhecimento", "ds");
        dc.createAlias("mnc.manifestoViagemNacional", "mvn");
        dc.createAlias("mvn.manifesto", "manifesto");
        dc.add(Restrictions.eq("mvn.id", idManifesto));
        dc.add(Restrictions.gt("ds.nrConhecimento", LongUtils.ZERO));
        return dc;
    }

    private DetachedCriteria createConhecimentoInternacionalCommonQuery(final Long idManifesto) {
        //Conhecimento Internacional
        final DetachedCriteria dc = DetachedCriteria.forClass(ManifestoInternacCto.class, "mic");
        dc.createAlias("mic.ctoInternacional", "ds");
        dc.createAlias("mic.manifestoViagemInternacional", "mvi");
        dc.createAlias("mvi.manifesto", "manifesto");
        dc.add(Restrictions.eq("mvi.id", idManifesto));
        return dc;
    }

    private DetachedCriteria createManifestoEntregaCommonQuery(final Long idManifesto) {
        //Manifesto de entrega
        final DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med");
        dc.createAlias("med.doctoServico", "ds");
        dc.createAlias("med.manifestoEntrega", "me");
        dc.createAlias("me.manifesto", "manifesto");
        dc.add(Restrictions.eq("me.id", idManifesto));
        dc.add(Restrictions.gt("ds.nrDoctoServico", LongUtils.ZERO));
        return dc;
    }

    private DetachedCriteria createPreManifestoCommonQuery(final Long idManifesto) {
        //Pre Manifesto
        final DetachedCriteria dc = DetachedCriteria.forClass(PreManifestoDocumento.class, "pmd");
        dc.createAlias("pmd.doctoServico", "ds");
        dc.createAlias("pmd.manifesto", "manifesto");
        dc.add(Restrictions.eq("manifesto.id", idManifesto));
        dc.add(Restrictions.gt("ds.nrDoctoServico", LongUtils.ZERO));
        return dc;
    }

    private void configProjection(final DetachedCriteria dc, final Projection projection) {
        if (projection == null) {
            throw new IllegalArgumentException("Proje??o deve ser informada para essa consulta!");
        }
        dc.setProjection(projection);
    }

    private void configAlias(final DetachedCriteria dc, final Map<String, String> alias) {
        if (alias == null) {
            return;
        }

        final Set<String> keySet = alias.keySet();
        for (final String key : keySet) {
            dc.createAlias(key, alias.get(key));
        }
    }

    private void configCriterions(final DetachedCriteria dc, final List<Criterion> criterions) {
        if (criterions == null) {
            return;
        }

        for (final Criterion criterion : criterions) {
            dc.add(criterion);
        }
    }

    public Integer getRowCountDoctoServicoByIdManifesto(Long idManifesto, String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, String notaFiscal) {
        SqlTemplate sql = getSqlTemplateFindPaginatedDoctoByManifesto(idManifesto, tpDoctoServico, idFilialOrigem, idDoctoServico, notaFiscal);
        return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false), sql.getCriteria());
    }

    public ResultSetPage findPaginatedDoctoServicoByIdManifesto(Long idManifesto, String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, String notaFiscal, FindDefinition findDefinition) {
        ConfigureSqlQuery csl = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
                sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
                sqlQuery.addScalar("DS_SERVICO", Hibernate.STRING);
                sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
                sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
                sqlQuery.addScalar("SG_FILIAL_DESTINO", Hibernate.STRING);
                sqlQuery.addScalar("REMETENTE", Hibernate.STRING);
                sqlQuery.addScalar("DESTINATARIO", Hibernate.STRING);
                sqlQuery.addScalar("QT_VOLUMES", Hibernate.INTEGER);
                sqlQuery.addScalar("PS_REAL", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("SG_MOEDA", Hibernate.STRING);
                sqlQuery.addScalar("DS_SIMBOLO", Hibernate.STRING);
                sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_TOTAL_DOC_SERVICO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("DT_PREV_ENTREGA", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
                sqlQuery.addScalar("SG_SERVICO", Hibernate.STRING);
                sqlQuery.addScalar("DS_LOCALIZACAO_MERCADORIA", Hibernate.STRING);
            }
        };
        SqlTemplate sql = getSqlTemplateFindPaginatedDoctoByManifesto(idManifesto, tpDoctoServico, idFilialOrigem, idDoctoServico, notaFiscal);
        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria(), csl);
    }

    private SqlTemplate getSqlTemplateFindPaginatedDoctoByManifesto(Long idManifesto, String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, String notaFiscal) {

        SqlTemplate sql = new SqlTemplate();
        sql.addProjection("DS.ID_DOCTO_SERVICO");
        sql.addProjection("DS.NR_DOCTO_SERVICO");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("SE.DS_SERVICO_I", "DS_SERVICO"));
        sql.addProjection("DS.TP_DOCUMENTO_SERVICO");
        sql.addProjection("FILIAL_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
        sql.addProjection("FILIAL_DESTINO.SG_FILIAL", "SG_FILIAL_DESTINO");
        sql.addProjection("REMETENTE.NM_PESSOA", "REMETENTE");
        sql.addProjection("DESTINATARIO.NM_PESSOA", "DESTINATARIO");
        sql.addProjection("DS.QT_VOLUMES");
        sql.addProjection("DS.PS_REAL");
        sql.addProjection("MO.SG_MOEDA");
        sql.addProjection("MO.DS_SIMBOLO");
        sql.addProjection("DS.VL_MERCADORIA");
        sql.addProjection("DS.VL_TOTAL_DOC_SERVICO");
        sql.addProjection("DS.DT_PREV_ENTREGA");
        sql.addProjection("SE.SG_SERVICO");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I", "DS_LOCALIZACAO_MERCADORIA"));

        StringBuilder from = new StringBuilder()
                .append("DOCTO_SERVICO DS ")
                .append("INNER JOIN SERVICO SE        ON (DS.ID_SERVICO = SE.ID_SERVICO) ")
                .append("INNER JOIN FILIAL FILIAL_ORIGEM  ON (DS.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL) ")
                .append("INNER JOIN FILIAL FILIAL_DESTINO ON (DS.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL) ")
                .append("INNER JOIN CLIENTE CLIENTE_R     ON (DS.ID_CLIENTE_REMETENTE = CLIENTE_R.ID_CLIENTE) ")
                .append("INNER JOIN PESSOA REMETENTE      ON (CLIENTE_R.ID_CLIENTE = REMETENTE.ID_PESSOA) ")
                .append("INNER JOIN CLIENTE CLIENTE_D     ON (DS.ID_CLIENTE_DESTINATARIO = CLIENTE_D.ID_CLIENTE) ")
                .append("INNER JOIN PESSOA DESTINATARIO   ON (CLIENTE_D.ID_CLIENTE = DESTINATARIO.ID_PESSOA) ")
                .append("INNER JOIN MOEDA MO              ON (DS.ID_MOEDA = MO.ID_MOEDA) ")
                .append("INNER JOIN LOCALIZACAO_MERCADORIA LM ON (DS.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA) ");

        sql.addFrom(from.toString());

        sql.addCriteria("DS.NR_DOCTO_SERVICO", ">", Long.valueOf(0));

        StringBuilder sub = new StringBuilder()
                .append("DS.ID_DOCTO_SERVICO IN (")
                .append("SELECT MNC.ID_CONHECIMENTO ")
                .append("FROM MANIFESTO_NACIONAL_CTO MNC ")
                .append("WHERE  MNC.ID_MANIFESTO_VIAGEM_NACIONAL = ? ")
                .append("UNION ")
                .append("SELECT MIC.ID_CTO_INTERNACIONAL ")
                .append("FROM MANIFESTO_INTERNAC_CTO MIC ")
                .append("WHERE MIC.ID_MANIFESTO_INTERNACIONAL = ? ")
                .append("UNION ")
                .append("SELECT MED.ID_DOCTO_SERVICO ")
                .append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
                .append("WHERE MED.ID_MANIFESTO_ENTREGA = ? ")
                .append("UNION ")
                .append("SELECT PMD.ID_DOCTO_SERVICO ")
                .append("FROM PRE_MANIFESTO_DOCUMENTO PMD ")
                .append("WHERE PMD.ID_MANIFESTO = ? )");

        sql.addCustomCriteria(sub.toString());
        sql.addCriteriaValue(idManifesto);
        sql.addCriteriaValue(idManifesto);
        sql.addCriteriaValue(idManifesto);
        sql.addCriteriaValue(idManifesto);

        if (idDoctoServico != null) {
            sql.addCriteria("DS.ID_DOCTO_SERVICO ", "=", idDoctoServico);
        } else {
            if (StringUtils.isNotEmpty(tpDoctoServico)) {
                sql.addCriteria("DS.TP_DOCUMENTO_SERVICO", "=", tpDoctoServico);
            }
            if (idFilialOrigem != null) {
                sql.addCriteria("DS.ID_FILIAL_ORIGEM", "=", idFilialOrigem);
            }
        }
        if (notaFiscal != null && !StringUtils.isEmpty(notaFiscal)) {
            sql.addCustomCriteria("EXISTS(SELECT 1 FROM NOTA_FISCAL_CONHECIMENTO NFCON WHERE DS.ID_DOCTO_SERVICO = NFCON.ID_CONHECIMENTO AND NFCON.NR_NOTA_FISCAL like '" + notaFiscal + "')");
        }
        return sql;
    }


    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do manifesto de viagem de nacional
     *
     * @param idManifestoViagem
     * @return
     */
    public List<Map<String, Object>> findDocumentosByManifestoViagemNacional(Long idManifestoViagemNacional) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map(c.idDoctoServico", "idDoctoServico");
        sql.addProjection("c.moeda.idMoeda", "idMoeda");
        sql.addProjection("c.clienteByIdClienteRemetente.idCliente", "idClienteRemetente");
        sql.addProjection("c.servico.idServico", "idServico");
        sql.addProjection("c.paisOrigem.idPais", "idPais");
        sql.addProjection("c.filialByIdFilialDestino.idFilial", "idFilial");
        sql.addProjection("c.clienteByIdClienteDestinatario.idCliente", "idClienteDestinatario");
        sql.addProjection("c.clienteByIdClienteConsignatario.idCliente", "idClienteConsignatario");
        sql.addProjection("c.clienteByIdClienteRedespacho.idCliente", "idClienteRedespacho");
        sql.addProjection("c.filialByIdFilialOrigem.idFilial", "idFilialOrigem");
        sql.addProjection("c.vlMercadoria", "vlMercadoria");
        sql.addProjection("ed.idEnderecoPessoa", "idEnderecoPessoa");
        sql.addProjection("ed.nrCep", "nrCep)");

        sql.addInnerJoin("ManifestoViagemNacional", "mvn");
        sql.addInnerJoin("mvn.manifestoNacionalCtos", "mnc");
        sql.addInnerJoin("mnc.conhecimento", "c");
        sql.addLeftOuterJoin("c.pedidoColeta", "pc");
        sql.addLeftOuterJoin("pc.enderecoPessoa", "ed");

        sql.addCustomCriteria("c.blReembolso = 'S'");
        sql.addCustomCriteria(new StringBuilder()
                .append("not exists (select 1 ")
                .append("			 from ReciboReembolso rr ")
                .append("			 where rr.doctoServicoByIdDoctoServReembolsado.idDoctoServico = c.idDoctoServico)").toString());

        sql.addCriteria("mvn.id", "=", idManifestoViagemNacional);

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do manifesto de entrega
     *
     * @param idManifestoEntrega
     * @return
     */
    public List<Map<String, Object>> findDocumentosByManifestoEntrega(Long idManifestoEntrega) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map(c.idDoctoServico", "idDoctoServico");
        sql.addProjection("c.moeda.idMoeda", "idMoeda");
        sql.addProjection("c.clienteByIdClienteRemetente.idCliente", "idClienteRemetente");
        sql.addProjection("c.servico.idServico", "idServico");
        sql.addProjection("c.paisOrigem.idPais", "idPais");
        sql.addProjection("c.filialByIdFilialDestino.idFilial", "idFilial");
        sql.addProjection("c.filialByIdFilialOrigem.idFilial", "idFilialOrigem");
        sql.addProjection("c.clienteByIdClienteDestinatario.idCliente", "idClienteDestinatario");
        sql.addProjection("c.clienteByIdClienteConsignatario.idCliente", "idClienteConsignatario");
        sql.addProjection("c.clienteByIdClienteRedespacho.idCliente", "idClienteRedespacho");
        sql.addProjection("c.vlMercadoria", "vlMercadoria");
        sql.addProjection("ed.nrCep", "nrCep");
        sql.addProjection("ed.idEnderecoPessoa", "idEnderecoPessoa)");

        sql.addInnerJoin("Conhecimento", "c");
        sql.addInnerJoin("c.manifestoEntregaDocumentos", "med");
        sql.addInnerJoin("med.manifestoEntrega", "me");
        sql.addLeftOuterJoin("c.pedidoColeta", "pc");
        sql.addLeftOuterJoin("pc.enderecoPessoa", "ed");

        sql.addCustomCriteria("c.blReembolso = 'S'");
        sql.addCustomCriteria(new StringBuilder()
                .append("not exists (select 1 ")
                .append("			 from ReciboReembolso rr ")
                .append("			 where rr.doctoServicoByIdDoctoServReembolsado.idDoctoServico = c.idDoctoServico)").toString());

        sql.addCriteria("me.id", "=", idManifestoEntrega);

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     * Busca todos os documentos de servico
     * a partir do id do Manifesto de entrega.
     * Os documentos buscados sao: 'CRT','CTR','NFT','RRE','MDA'
     */
    public List<DoctoServico> findDoctosServicoByIdManifestoEntrega(Long idManifesto) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select doctoServico from DoctoServico as doctoServico, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("ManifestoEntrega as manifestoEntrega, ");
        sql.append("ManifestoEntregaDocumento as manifestoEntregaDocumento ");
        sql.append("where manifesto.id = manifestoEntrega.id ");
        sql.append("and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id ");
        sql.append("and manifestoEntregaDocumento.doctoServico.id = doctoServico.id ");
        sql.append("and doctoServico.tpDocumentoServico in ('CRT','CTR','NFT','RRE','MDA')");
        sql.append("and manifesto.id = :idManifesto");
        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idManifesto", idManifesto);
    }


    public boolean validateDoctoServicoByIdManifestoEntregaIdDoctoServico(Long idManifesto, Long idControleCarga, Long idDoctoServico) {
        int exec = 0;
        StringBuilder sql = new StringBuilder("");
        sql.append("select count(*) from ManifestoEntregaDocumento as med ");
        sql.append(" left join med.doctoServico as ds ");
        sql.append(" right join med.manifestoEntrega as me ");
        sql.append(" right join me.manifesto as m ");
        sql.append(" right join m.controleCarga as cc ");
        sql.append("where 1 = 1 ");

        Map<String, Object> namedParams = new HashMap<String, Object>();
        if (idManifesto != null) {
            sql.append("and m.id = :idManifesto ");
            namedParams.put("idManifesto", idManifesto);
            exec++;
        }

        if (idControleCarga != null) {
            sql.append("and cc.id = :idControleCarga ");
            namedParams.put("idControleCarga", idControleCarga);
            exec++;
        }

        if (idDoctoServico != null) {
            sql.append("and ds.id = :idDoctoServico ");
            namedParams.put("idDoctoServico", idDoctoServico);
            exec++;
        }

        if (exec == 1) {
            return true;
        }

        if (idManifesto == null && idControleCarga == null && idDoctoServico == null) {
            return false;
        }

        List l = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), namedParams);
        return (Long) l.get(0) > 0;
    }

    /**
     * Busca todos os documentos de servico mais a localizacao da mercadoria
     * a partir do id do Manifesto de entrega.
     * Os documentos buscados sao: 'CRT','CTR','NFT','RRE','MDA'
     */
    public List<Map<String, Object>> findDoctosServicoLocalizacaoByIdManifestoEntrega(Long idManifesto) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select new Map(doctoServico.id as idDoctoServico,");
        sql.append("doctoServico.nrDoctoServico as nrDoctoServico,");
        sql.append("lm.cdLocalizacaoMercadoria as cdLocalizacaoMercadoria) ");

        sql.append("from DoctoServico as doctoServico left join doctoServico.localizacaoMercadoria lm, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("ManifestoEntrega as manifestoEntrega, ");
        sql.append("ManifestoEntregaDocumento as manifestoEntregaDocumento ");

        sql.append("where manifesto.id = manifestoEntrega.id ");
        sql.append("and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id ");
        sql.append("and manifestoEntregaDocumento.doctoServico.id = doctoServico.id ");
        sql.append("and doctoServico.tpDocumentoServico in ('CRT','CTR','NFT','RRE','MDA','CTE')");
        sql.append("and manifesto.id = :idManifesto");

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idManifesto", idManifesto);
    }

	/*
	 * FIXME: Alterar esses m?todos para apenas um que valide todas as regras
	 * n?o foi feito pois ? necess?rio verificar todos os casos.
	 * INICIO ******************************************************************
	 */

    /**
     * Busca o n?mero de doctos de servico que tenham manifesto de entrega
     *
     * @author Rodrigo Antunes
     */
    public Integer getRowCountDoctosServicoByIdManifestoEntrega(Long idManifesto, boolean filtrarTipoDocumento) {
        StringBuilder sql = new StringBuilder();
        sql.append("from DoctoServico as doctoServico, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("ManifestoEntrega as manifestoEntrega, ");
        sql.append("ManifestoEntregaDocumento as manifestoEntregaDocumento ");
        sql.append("where manifesto.id = manifestoEntrega.id ");
        sql.append("and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id ");
        sql.append("and manifestoEntregaDocumento.doctoServico.id = doctoServico.id ");
        if (filtrarTipoDocumento) {
            sql.append("and doctoServico.tpDocumentoServico in ('CRT','CTR','NFT','RRE','MDA','CTE')");
        }
        sql.append("and manifesto.id = ?");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idManifesto});
    }

    /**
     * Busca o n?mero de doctos de servico que tenham manifesto de viagem nacional
     *
     * @author Rodrigo Antunes
     */
    public Integer getRowCountConhecimentosByIdManifestoViagemNacional(Long idManifesto, boolean filtrarTipoDocumento) {
        StringBuilder sql = new StringBuilder();
        sql.append("from DoctoServico as doctoServico, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("ManifestoViagemNacional as manifestoViagemNacional, ");
        sql.append("ManifestoNacionalCto as manifestoNacionalCto ");
        sql.append("where manifestoViagemNacional.id = manifesto.id ");
        sql.append("and manifestoViagemNacional.id = manifestoNacionalCto.manifestoViagemNacional.id ");
        sql.append("and manifestoNacionalCto.conhecimento.id = doctoServico.id ");
        if (filtrarTipoDocumento) {
            sql.append("and doctoServico.tpDocumentoServico = 'CTR' ");
        } else {
            sql.append("and doctoServico.tpDocumentoServico <> 'MDA' ");
        }
        sql.append("and doctoServico.nrDoctoServico >= 0 ");
        sql.append("and manifesto.id = ?");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idManifesto});
    }

    /**
     * Busca o n?mero de doctos de servico que tenham manifesto de viagem internacional
     *
     * @author Rodrigo Antunes
     */
    public Integer getRowCountCtosInternacionaisByIdManifestoViagemInternacional(Long idManifesto, boolean filtrarTipoDocumento) {
        StringBuilder sql = new StringBuilder();

        sql.append("from DoctoServico as doctoServico, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("ManifestoInternacional as manifestoInternacional, ");
        sql.append("ManifestoInternacCto as manifestoInternacCto ");
        sql.append("where manifestoInternacional.id = manifesto.id ");
        sql.append("and manifestoInternacional.id = manifestoInternacCto.manifestoViagemInternacional.id ");
        sql.append("and manifestoInternacCto.ctoInternacional.id = doctoServico.id ");
        if (filtrarTipoDocumento) {
            sql.append("and doctoServico.tpDocumentoServico = 'CRT' ");
        }
        sql.append("and manifesto.id = ?");

        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idManifesto});
    }

    /**
     * Busca o n?mero de doctos de servico (MDA) que tenham manifesto de viagem nacional
     *
     * @author Rodrigo Antunes
     */
    public Integer getRowCountMdasByIdManifestoViagemNacional(Long idManifesto) {
        StringBuilder sql = new StringBuilder();
        sql.append("from DoctoServico as doctoServico, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("PreManifestoDocumento as preManifestoDocumento ");
        sql.append("where manifesto.id = preManifestoDocumento.manifesto.id ");
        sql.append("and preManifestoDocumento.doctoServico.id = doctoServico.id ");
        sql.append("and doctoServico.tpDocumentoServico = 'MDA' ");
        sql.append("and manifesto.id = ?");
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idManifesto});
    }
	/*
	 * FIM *********************************************************************
	 */

    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do conhecimento
     *
     * @param idConhecimento
     * @return
     */
    public List<Map<String, Object>> findDocumentosByConhecimento(Long idConhecimento) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map(c.idDoctoServico", "idDoctoServico");
        sql.addProjection("c.moeda.idMoeda", "idMoeda");
        sql.addProjection("c.clienteByIdClienteRemetente.idCliente", "idClienteRemetente");
        sql.addProjection("c.servico.idServico", "idServico");
        sql.addProjection("c.paisOrigem.idPais", "idPais");
        sql.addProjection("c.filialByIdFilialDestino.idFilial", "idFilial");
        sql.addProjection("c.filialByIdFilialOrigem.idFilial", "idFilialOrigem");
        sql.addProjection("c.clienteByIdClienteDestinatario.idCliente", "idClienteDestinatario");
        sql.addProjection("c.clienteByIdClienteConsignatario.idCliente", "idClienteConsignatario");
        sql.addProjection("c.clienteByIdClienteRedespacho.idCliente", "idClienteRedespacho");
        sql.addProjection("c.vlMercadoria", "vlMercadoria");
        sql.addProjection("ed.nrCep", "nrCep");
        sql.addProjection("ed.idEnderecoPessoa", "idEnderecoPessoa)");

        sql.addInnerJoin("Conhecimento", "c");
        sql.addLeftOuterJoin("c.pedidoColeta", "pc");
        sql.addLeftOuterJoin("pc.enderecoPessoa", "ed");

        sql.addCustomCriteria("c.blReembolso = 'S'");
        sql.addCustomCriteria(new StringBuilder()
                .append("not exists (select 1 ")
                .append("			 from ReciboReembolso rr ")
                .append("			 where rr.doctoServicoByIdDoctoServReembolsado.idDoctoServico = c.idDoctoServico)").toString());

        sql.addCriteria("c.id", "=", idConhecimento);

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    public ResultSetPage findPaginatedRelacaoDeposito(RelacaoDocumentoServicoDepositoParam param, FindDefinition findDef) {
        SqlTemplate hql = mountHqlRelacaoDeposito(param);

        hql.addProjection("dev");

        hql.addOrderBy("fil.sgFilial");
        hql.addOrderBy("doc.tpDocumentoServico");
        hql.addOrderBy("doc.nrDoctoServico");

        return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
    }

    public Integer getRowCountRelacaoDeposito(RelacaoDocumentoServicoDepositoParam param) {
        SqlTemplate hql = mountHqlRelacaoDeposito(param);

        return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }


    private SqlTemplate mountHqlRelacaoDeposito(RelacaoDocumentoServicoDepositoParam param) {
        SqlTemplate hql = new SqlTemplate();

        hql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
        hql.addInnerJoin("fetch dev.doctoServico", "doc");
        hql.addLeftOuterJoin("fetch dev.descontos", "des");
        hql.addInnerJoin("fetch doc.filialByIdFilialOrigem", "fil");
        hql.addInnerJoin("fetch doc.moeda", "moe");


        hql.addCriteria("doc.tpDocumentoServico", "=", param.getTpDoctoServico());
        hql.addCriteria("trunc(cast(doc.dhEmissao.value as date))", ">=", param.getDtEmissaoInicial());
        hql.addCriteria("trunc(cast(doc.dhEmissao.value as date))", "<=", param.getDtEmissaoFinal());
        hql.addCriteria("doc.nrDoctoServico", ">=", param.getNrDoctoServicoInicial());
        hql.addCriteria("doc.nrDoctoServico", "<=", param.getNrDoctoServicoFinal());

        //Para n?o selecionar os pre-conhecimento
        hql.addCriteria("doc.nrDoctoServico", ">", Long.valueOf(0));
        hql.addCriteria("dev.cliente.id", "=", param.getIdCliente());

        String strCriterios = null;

        if (param.getLstTpSituacaoDocumento() != null && !param.getLstTpSituacaoDocumento().isEmpty()) {
            //Filtrar por tpSituacaoCobranca
            strCriterios = new String();

            for (Iterator<String> iter = param.getLstTpSituacaoDocumento().iterator(); iter.hasNext(); ) {
                strCriterios = strCriterios + "?,";
                hql.addCriteriaValue(iter.next());
            }

            hql.addCustomCriteria("dev.tpSituacaoCobranca in (" + strCriterios.substring(0, strCriterios.length() - 1) + ")");
        }

        if (param.getLstDevedores() != null && !param.getLstDevedores().isEmpty()) {
            //Filtrar para n?o devolver os devedores que j? foram informado
            strCriterios = new String();

            for (Iterator<Long> iter = param.getLstDevedores().iterator(); iter.hasNext(); ) {
                strCriterios = strCriterios + "?,";
                hql.addCriteriaValue(iter.next());
            }

            hql.addCustomCriteria("dev.id not in (" + strCriterios.substring(0, strCriterios.length() - 1) + ")");
        }

        hql.addCustomCriteria("NOT EXISTS ( " +
                " SELECT 	idcctmp " +
                " FROM 		" + ItemDepositoCcorrente.class.getName() + " idcctmp " +
                " WHERE 	idcctmp.devedorDocServFat.id = dev.id " +
                " ) ");

        return hql;
    }

    public List<Object[]> findDoctoServicoByParams(Long idFilial, Long nrDoctoServico, String tpDoctoServico,
                                                   String dhEmissao) {
        SqlTemplate sql = new SqlTemplate();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("idFilial", idFilial);
        map.put("nrDoctoServico", nrDoctoServico);
        map.put("tpDoctoServico", tpDoctoServico);
        map.put("dhEmissao", dhEmissao);

        sql.addProjection("DEV.ID_FILIAL", "idFilial");
        sql.addProjection("DEV.ID_DEVEDOR_DOC_SERV_FAT", "idDevedorDocServFat");
        sql.addProjection("DOC.ID_DOCTO_SERVICO", "idDoctoServico");
        sql.addProjection("DOC.NR_DOCTO_SERVICO", "nrDoctoServico");
        sql.addProjection("P.NR_IDENTIFICACAO", "nrIdentificacao");
        sql.addProjection("cast (DOC.DH_EMISSAO as Date)", "dtEmissao");
        sql.addProjection("S.TP_MODAL", "tpModal");
        sql.addProjection("S.TP_ABRANGENCIA", "tpAbrangencia");
        sql.addProjection("DEV.TP_SITUACAO_COBRANCA", "tpSituacaoCobranca");

        sql.addFrom("DOCTO_SERVICO", "DOC");
        sql.addFrom("FILIAL", "FIL");
        sql.addJoin("DOC.ID_FILIAL_ORIGEM", "FIL.ID_FILIAL");
        sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DEV");
        sql.addJoin("DEV.ID_DOCTO_SERVICO", "DOC.ID_DOCTO_SERVICO");
        sql.addFrom("SERVICO", "S");
        sql.addJoin("S.ID_SERVICO", "DOC.ID_SERVICO");
        sql.addFrom("PESSOA", "P");
        sql.addJoin("P.ID_PESSOA", "DEV.ID_CLIENTE");

        sql.addCustomCriteria("DOC.TP_DOCUMENTO_SERVICO = :tpDoctoServico");
        sql.addCustomCriteria("FIL.ID_FILIAL = :idFilial");

        sql.addCustomCriteria("SUBSTR(LPAD(TRIM(:nrDoctoServico), 12, '0'), 7, 6) = SUBSTR(LPAD(DOC.NR_DOCTO_SERVICO, 12, '0'), 7, 6)");
        sql.addCustomCriteria("TRUNC(DOC.DH_EMISSAO) = TO_DATE(:dhEmissao, 'DDMMYYYY')");

        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("idFilial", Hibernate.LONG);
                sqlQuery.addScalar("idDevedorDocServFat", Hibernate.LONG);
                sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("nrIdentificacao", Hibernate.STRING);
                sqlQuery.addScalar("dtEmissao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
                sqlQuery.addScalar("tpModal", Hibernate.STRING);
                sqlQuery.addScalar("tpAbrangencia", Hibernate.STRING);
                sqlQuery.addScalar("tpSituacaoCobranca", Hibernate.STRING);
            }
        };

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1), Integer.valueOf(1), map, configSql).getList();
    }


    public DoctoServico findDoctoServicoByFilialNumeroEDhEmissao(Long idFilial, Long nrDoctoServico,
                                                                 DateTime dhEmissao) {
    	
    	DateTimeFormatter formatter = DateTimeFormat.forPattern("ddMMyyyy");

        List params = new ArrayList();
        params.add(idFilial);
        params.add(nrDoctoServico);
        params.add(dhEmissao.toString(formatter));

        StringBuilder sql = new StringBuilder();
        sql.append(" from DoctoServico ")
                .append(" where filialByIdFilialOrigem.idFilial = ? and ")
                .append(" nrDoctoServico = ? and ")
                .append("TRUNC(DH_EMISSAO) = TO_DATE(?, 'DDMMYYYY')");

        List<DoctoServico> retorno = getAdsmHibernateTemplate().find(sql.toString(), params.toArray());
        DoctoServico docto = null;
        if (retorno.isEmpty()) {
            return docto;
        }

        return (DoctoServico) retorno.get(0);
    }

    public ResultSetPage findPaginatedComConhecimento(DoctoServicoParam doctoServicoParam, List<String> tpsSituacao, FindDefinition findDef) {
        SqlTemplate sql = mountSql(doctoServicoParam, tpsSituacao);

        sql.addProjection("DEV.ID_DEVEDOR_DOC_SERV_FAT", "idDevedorDocServFat");
        sql.addProjection("MIN(NFCON.NR_NOTA_FISCAL)", "nrNotaFiscal");
        sql.addProjection(" (select " + PropertyVarcharI18nProjection.createProjection("VDOM1.DS_VALOR_DOMINIO_I") + " from VALOR_DOMINIO VDOM1, DOMINIO DOM1 " +
                "WHERE lower(DOM1.NM_DOMINIO) = lower('DM_TIPO_DOCUMENTO_SERVICO') " +
                "AND VDOM1.ID_DOMINIO = DOM1.ID_DOMINIO " +
                "AND VDOM1.VL_VALOR_DOMINIO = DOC.TP_DOCUMENTO_SERVICO)", "tpDocumentoServico ");
        sql.addProjection("FIL.SG_FILIAL", "sgFilial");
        sql.addProjection("DOC.NR_DOCTO_SERVICO", "nrDoctoServico");
        sql.addProjection("DOC.DH_EMISSAO", "dtEmissao");
        sql.addProjection("DOC.VL_MERCADORIA", "vlMercadoria");
        sql.addProjection("DEV.VL_DEVIDO", "vlTotalDocServico");
        sql.addProjection(" (select " + PropertyVarcharI18nProjection.createProjection("VDOM1.DS_VALOR_DOMINIO_I") + " from VALOR_DOMINIO VDOM1, DOMINIO DOM1 " +
                "WHERE lower(DOM1.NM_DOMINIO) = lower('DM_TIPO_FRETE') " +
                "AND VDOM1.ID_DOMINIO = DOM1.ID_DOMINIO " +
                "AND VDOM1.VL_VALOR_DOMINIO = CON.TP_FRETE) ", "tpFrete");
        sql.addProjection("DOC.ID_MOEDA", "idMoeda");
        sql.addProjection("SER.TP_MODAL", "tpModal");
        sql.addProjection("SER.TP_ABRANGENCIA", "tpAbrangencia");
        sql.addProjection("SER.ID_SERVICO", "idServico");
        sql.addProjection("DEV.ID_DIVISAO_CLIENTE", "idDivisaoCliente");

        sql.addGroupBy("DEV.ID_DEVEDOR_DOC_SERV_FAT");
        sql.addGroupBy("FIL.SG_FILIAL");
        sql.addGroupBy("DOC.NR_DOCTO_SERVICO");
        sql.addGroupBy("DOC.DH_EMISSAO");
        sql.addGroupBy("DOC.VL_MERCADORIA");
        sql.addGroupBy("DEV.VL_DEVIDO");
        sql.addGroupBy("CON.TP_FRETE");
        sql.addGroupBy("DOC.TP_DOCUMENTO_SERVICO");
        sql.addGroupBy("DOC.ID_MOEDA");
        sql.addGroupBy("SER.TP_MODAL");
        sql.addGroupBy("SER.TP_ABRANGENCIA");
        sql.addGroupBy("SER.ID_SERVICO");
        sql.addGroupBy("DEV.ID_DIVISAO_CLIENTE");

        sql.addOrderBy("nrNotaFiscal");
        sql.addOrderBy("dtEmissao");
        sql.addOrderBy("tpDocumentoServico");
        sql.addOrderBy("DOC.NR_DOCTO_SERVICO");


        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("idDevedorDocServFat", Hibernate.LONG);
                sqlQuery.addScalar("nrNotaFiscal", Hibernate.LONG);
                sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
                sqlQuery.addScalar("sgFilial", Hibernate.STRING);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("dtEmissao", Hibernate.TIMESTAMP);
                sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlTotalDocServico", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("tpFrete", Hibernate.STRING);
                sqlQuery.addScalar("idMoeda", Hibernate.LONG);
                sqlQuery.addScalar("tpModal", Hibernate.STRING);
                sqlQuery.addScalar("tpAbrangencia", Hibernate.STRING);
                sqlQuery.addScalar("idServico", Hibernate.LONG);
                sqlQuery.addScalar("idDivisaoCliente", Hibernate.LONG);
            }
        };

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(), configSql);
    }

    public Integer getRowCountComConhecimento(DoctoServicoParam doctoServicoParam, List<String> tpsSituacao) {
        SqlTemplate sql = mountSql(doctoServicoParam, tpsSituacao);

        sql.addProjection("distinct DEV.ID_DEVEDOR_DOC_SERV_FAT");

        return getAdsmHibernateTemplate().getRowCountBySql("FROM (" + sql.getSql() + ")", sql.getCriteria());
    }

    private SqlTemplate mountSql(DoctoServicoParam doctoServicoParam, List<String> tpsSituacao) {
        SqlTemplate sql = new SqlTemplate();

        sql.addFrom("DOCTO_SERVICO", "DOC");

        sql.addFrom("FILIAL", "FIL");
        sql.addJoin("DOC.ID_FILIAL_ORIGEM", "FIL.ID_FILIAL");

        sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DEV");
        sql.addJoin("DEV.ID_DOCTO_SERVICO", "DOC.ID_DOCTO_SERVICO");

        sql.addFrom("CONHECIMENTO", "CON");
        sql.addJoin("DOC.ID_DOCTO_SERVICO", "CON.ID_CONHECIMENTO(+)");

        sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "NFCON");
        sql.addJoin("CON.ID_CONHECIMENTO", "NFCON.ID_CONHECIMENTO(+)");

        sql.addFrom("SERVICO", "SER");
        sql.addJoin("DOC.ID_SERVICO", "SER.ID_SERVICO(+)");

        String strCriterios = null;

        if (!tpsSituacao.isEmpty()) {
            //Filtrar por tpSituacaoCobranca
            strCriterios = new String();
            for (Iterator<String> iter = tpsSituacao.iterator(); iter.hasNext(); ) {
                strCriterios = strCriterios + "?,";
                sql.addCriteriaValue(iter.next());
            }
            sql.addCustomCriteria("DEV.TP_SITUACAO_COBRANCA in (" + strCriterios.substring(0, strCriterios.length() - 1) + ")");
        }

        if (doctoServicoParam.getIdDevedores() != null && !doctoServicoParam.getIdDevedores().isEmpty()) {
            //Filtrar para n?o devolver os devedores que j? foram informado
            strCriterios = new String();

            for (Iterator<Long> iter = doctoServicoParam.getIdDevedores().iterator(); iter.hasNext(); ) {
                strCriterios = strCriterios + "?,";
                sql.addCriteriaValue(iter.next());
            }

            sql.addCustomCriteria("DEV.ID_DEVEDOR_DOC_SERV_FAT not in (" + strCriterios.substring(0, strCriterios.length() - 1) + ")");
        }

        sql.addCustomCriteria("NOT EXISTS (SELECT 1 FROM AGENDA_TRANSFERENCIA AT WHERE AT.ID_DEVEDOR_DOC_SERV_FAT = DEV.ID_DEVEDOR_DOC_SERV_FAT)");
        sql.addCustomCriteria("NOT EXISTS (SELECT 1 FROM TRANSFERENCIA TA, ITEM_TRANSFERENCIA ITA WHERE TA.ID_TRANSFERENCIA = ITA.ID_TRANSFERENCIA AND ITA.ID_DEVEDOR_DOC_SERV_FAT = DEV.ID_DEVEDOR_DOC_SERV_FAT AND TA.TP_SITUACAO_TRANSFERENCIA = ?)", "PR");
        sql.addCriteria("DOC.ID_DOCTO_SERVICO", "=", doctoServicoParam.getIdDoctoServico());
        sql.addCriteria("DOC.TP_DOCUMENTO_SERVICO", "=", doctoServicoParam.getTpDocumentoServico());
        sql.addCriteria("FIL.ID_FILIAL", "=", doctoServicoParam.getIdFilialorigem());
        sql.addCriteria("trunc(cast(DOC.DH_EMISSAO as date))", ">=", doctoServicoParam.getDtEmissaoInicial());
        sql.addCriteria("trunc(cast(DOC.DH_EMISSAO as date))", "<=", doctoServicoParam.getDtEmissaoFinal());
        sql.addCriteria("NFCON.NR_NOTA_FISCAL", ">=", doctoServicoParam.getNrNotaFiscalInicial());
        sql.addCriteria("NFCON.NR_NOTA_FISCAL", "<=", doctoServicoParam.getNrNotaFiscalFinal());
        sql.addCriteria("DOC.NR_DOCTO_SERVICO", ">=", doctoServicoParam.getNrDoctoServicoInicial());
        sql.addCriteria("DOC.NR_DOCTO_SERVICO", "<=", doctoServicoParam.getNrDoctoServicoFinal());
        sql.addCriteria("CON.TP_FRETE", "=", doctoServicoParam.getTpFrete());

        sql.addCriteria("DEV.ID_CLIENTE", "=", doctoServicoParam.getIdCliente());
        sql.addCriteria("SER.TP_MODAL", "=", doctoServicoParam.getTpModal());
        sql.addCriteria("SER.TP_ABRANGENCIA", "=", doctoServicoParam.getTpAbrangencia());
        sql.addCriteria("SER.ID_SERVICO", "=", doctoServicoParam.getIdServico());
        sql.addCriteria("DEV.ID_DIVISAO_CLIENTE", "=", doctoServicoParam.getIdDivisaoCliente());

        //Para n?o selecionar os pre-conhecimento
        sql.addCriteria("DOC.NR_DOCTO_SERVICO", ">", new BigDecimal(0));
        return sql;
    }

    /**
     * M?todo que monta o SQL para pesquisa de documentos de Servi?o
     *
     * @param idLocalizacao
     * @param bloqueado
     * @param criteria
     * @return
     */
    private SqlTemplate mountSqlTemp(List<Long> idsLocalizacao, Boolean bloqueado, TypedFlatMap criteria, List<Long> idsDoctoServico) {
        SqlTemplate sqlTemp = new SqlTemplate();

        sqlTemp.addFrom(DoctoServico.class.getName() + " doc left join fetch doc.servico ser " +
                " left join fetch ser.tipoServico tps " +
                " join fetch doc.moeda moe " +
                " join fetch doc.filialByIdFilialOrigem fo " +
                " join fetch fo.pessoa fop " +
                " left join fetch doc.filialByIdFilialDestino fd " +
                " left join fetch fd.pessoa fdp " +
                " left join fetch doc.clienteByIdClienteRemetente cr " +
                " left join fetch cr.pessoa crp " +
                " left join fetch doc.clienteByIdClienteDestinatario cd " +
                " left join fetch cd.pessoa cdp " +
                " left join fetch doc.clienteByIdClienteConsignatario cc " +
                " left join fetch cc.pessoa ccp " +
                " left join fetch doc.rotaColetaEntregaByIdRotaColetaEntregaReal rce " +
                " left join fetch doc.rotaColetaEntregaByIdRotaColetaEntregaSugerid rces " +
                " left join fetch doc.documentoServicoRetiradas dsr " +
                " left join fetch dsr.solicitacaoRetirada sr ");


        if ("E".equals(criteria.getString("manifesto.tpManifesto"))) {
            if ("PR".equals(criteria.getString("manifesto.tpPreManifesto")) || "EP".equals(criteria.getString("manifesto.tpPreManifesto"))) {
                sqlTemp.addCriteria("doc.clienteByIdClienteConsignatario.id", "=",
                        criteria.getLong("doctoServico.clienteByIdClienteConsignatario.idCliente"));
            } else {
                sqlTemp.addCustomCriteria("nvl(doc.filialDestinoOperacional.id, doc.filialByIdFilialDestino.id) = ? ");
                sqlTemp.addCriteriaValue(SessionUtils.getFilialSessao().getIdFilial());

                if (criteria.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega") != null) {
                    sqlTemp.addCustomCriteria("nvl(rce.id, rces.id) = ? ");
                    sqlTemp.addCriteriaValue(criteria.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega"));
                }
            }

            sqlTemp.addCustomCriteria("doc.tpDocumentoServico in (?, ?, ?, ?, ?)");
            sqlTemp.addCriteriaValue("CRT");
            sqlTemp.addCriteriaValue("CTR");
            sqlTemp.addCriteriaValue("NFT");
            sqlTemp.addCriteriaValue("RRE");
            sqlTemp.addCriteriaValue("MDA");

            sqlTemp.addCustomCriteria("( not exists ( select conhecimento.id " +
                    "			  	  from " + Conhecimento.class.getName() + " conhecimento " +
                    "			  	  where conhecimento.tpSituacaoConhecimento = ? " +
                    "				    	and conhecimento.id = doc.id ) or " +
                    "	 not exists ( select ctoInternacional.id " +
                    "			  	  from " + CtoInternacional.class.getName() + " ctoInternacional " +
                    "			  	  where ctoInternacional.tpSituacaoCrt = ? " +
                    "				    	and ctoInternacional.id = doc.id ) or " +
                    "  not exists ( select mda.id " +
                    "	 		  	  from " + Mda.class.getName() + " mda " +
                    "			  	  where mda.tpStatusMda = ? " +
                    "				    	and mda.id = doc.id ) or " +
                    "  not exists ( select rre.id " +
                    "	 		  	  from " + ReciboReembolso.class.getName() + " rre " +
                    "			  	  where rre.tpSituacaoRecibo = ? " +
                    "				    	and rre.id = doc.id ) )");
            sqlTemp.addCriteriaValue("C");
            sqlTemp.addCriteriaValue("C");
            sqlTemp.addCriteriaValue("C");
            sqlTemp.addCriteriaValue("CA");
        }

        if ("V".equals(criteria.getString("manifesto.tpManifesto"))) {
            List<Long> idsFluxoFilial = criteria.getList("idsFluxoFilial");
            sqlTemp.addCustomCriteria("doc.fluxoFilial.id in " + SQLUtils.mountNumberForInExpression(idsFluxoFilial));

            if ("N".equals(criteria.getString("manifesto.tpAbrangencia"))) {
                sqlTemp.addCustomCriteria("doc.tpDocumentoServico in (?, ?)");
                sqlTemp.addCriteriaValue("CTR");
                sqlTemp.addCriteriaValue("MDA");

                sqlTemp.addCustomCriteria("( not exists ( select conhecimento.id " +
                        "			  	  from " + Conhecimento.class.getName() + " conhecimento " +
                        "			  	  where conhecimento.tpSituacaoConhecimento = ? " +
                        "				    	and conhecimento.id = doc.id ) or " +
                        "  not exists ( select mda.id " +
                        "	 		 	  from " + Mda.class.getName() + " mda " +
                        "			  	  where mda.tpStatusMda = ? " +
                        "				    	and mda.id = doc.id ) )");
                sqlTemp.addCriteriaValue("C");
                sqlTemp.addCriteriaValue("C");
            }

            if ("I".equals(criteria.getString("manifesto.tpAbrangencia"))) {
                sqlTemp.addCustomCriteria("doc.tpDocumentoServico in (?)");
                sqlTemp.addCriteriaValue("CRT");

                sqlTemp.addCustomCriteria("( not exists ( select ctoInternacional.id " +
                        "			  	  from " + CtoInternacional.class.getName() + " ctoInternacional " +
                        "			  	  where ctoInternacional.tpSituacaoCrt = ? " +
                        "				    	and ctoInternacional.id = doc.id ) )");
                sqlTemp.addCriteriaValue("C");
            }
            if (criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial") != null) {
                sqlTemp.addCriteria("doc.filialByIdFilialDestino.id", "=", criteria.getLong("doctoServico.filialByIdFilialDestino.idFilial"));
            }
        }

        sqlTemp.addCustomCriteria("( sr is null " +
                " or (sr.filialRetirada.id <> doc.filialLocalizacao.id " +
                " or (sr.filialRetirada.id = doc.filialLocalizacao.id and sr.tpSituacao <> ?)) " +
                " and sr.idSolicitacaoRetirada = ( select max(sr2.idSolicitacaoRetirada) " +
                "   				   from " + DocumentoServicoRetirada.class.getName() + " dsr2 " +
                " 							 join dsr2.solicitacaoRetirada sr2 " +
                " 							 join dsr2.doctoServico doc2 " +
                "   				   where doc2.idDoctoServico = doc.idDoctoServico ) )");
        sqlTemp.addCriteriaValue("A");

        sqlTemp.addCustomCriteria("doc.localizacaoMercadoria.id in " + SQLUtils.mountNumberForInExpression(idsLocalizacao));

        if (bloqueado.booleanValue()) {
            sqlTemp.addCriteria("doc.blBloqueado", "=", Boolean.TRUE);
        } else {
            sqlTemp.addCriteria("doc.blBloqueado", "=", Boolean.FALSE);
        }

        if (criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente") != null) {
            sqlTemp.addCriteria("doc.clienteByIdClienteRemetente.id", "=",
                    criteria.getLong("doctoServico.clienteByIdClienteRemetente.idCliente"));
        }

        if (criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente") != null) {
            sqlTemp.addCriteria("doc.clienteByIdClienteDestinatario.id", "=",
                    criteria.getLong("doctoServico.clienteByIdClienteDestinatario.idCliente"));
        }

        if (criteria.getLong("doctoServico.ctoAwbs.idAwb") != null) {
            sqlTemp.addCriteria("doc.ctoAwbs.awb.id", "=", criteria.getLong("doctoServico.ctoAwbs.idAwb"));
        }

        if (criteria.getString("doctoServico.tpDocumentoServico") != null &&
                !"".equals(criteria.getString("doctoServico.tpDocumentoServico"))) {
            sqlTemp.addCriteria("doc.tpDocumentoServico", "=", criteria.getString("doctoServico.tpDocumentoServico"));
        }

        if (criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null) {
            sqlTemp.addCriteria("doc.filialByIdFilialOrigem.id", "=", criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
        }

        if (criteria.getLong("doctoServico.nrDoctoServico") != null) {
            sqlTemp.addCriteria("doc.nrDoctoServico", "=", criteria.getLong("doctoServico.nrDoctoServico"));
        }

        if (criteria.getLong("doctoServico.idDoctoServico") != null) {
            sqlTemp.addCriteria("doc.id", "=", criteria.getLong("doctoServico.idDoctoServico"));
        }

        sqlTemp.addCriteria("doc.filialLocalizacao.id", "=", SessionUtils.getFilialSessao().getIdFilial());

        if (idsDoctoServico != null && !idsDoctoServico.isEmpty()) {
            sqlTemp.addCustomCriteria("doc.id not in " + SQLUtils.mountNumberForInExpression(idsDoctoServico));
        }

        return sqlTemp;
    }

    /**
     * M?todo que busca a pagina??o de documentos de servi?o pela localiza??o mercadoria, pelo bloqueio e
     * criterios de filtro da p?gina
     *
     * @param idLocalizacao
     * @param bloqueado
     * @param criteria
     * @param idsDoctoServico
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoByLocalizacaoMercadoria(
            List<Long> idsLocalizacao,
            Boolean bloqueado,
            TypedFlatMap criteria,
            List<Long> idsDoctoServico,
            FindDefinition findDefinition
    ) {
        SqlTemplate sqlTemp = mountSqlTemp(idsLocalizacao, bloqueado, criteria, idsDoctoServico);

        sqlTemp.addProjection("doc");

        sqlTemp.addOrderBy("doc.dtPrevEntrega", "asc");

        return getAdsmHibernateTemplate().findPaginated(sqlTemp.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sqlTemp.getCriteria());
    }

    /**
     * M?todo que busca uma list de documentos de servi?o pela localiza??o mercadoria, pelo bloqueio e
     * criterios de filtro da p?gina
     *
     * @param idLocalizacao
     * @param bloqueado
     * @param criteria
     * @param idsDoctoServico
     * @return
     */
    public List<DoctoServico> findListDoctoServicoByLocalizacaoMercadoria(
            List<Long> idsLocalizacao,
            Boolean bloqueado,
            TypedFlatMap criteria,
            List<Long> idsDoctoServico
    ) {
        SqlTemplate sqlTemp = mountSqlTemp(idsLocalizacao, bloqueado, criteria, idsDoctoServico);

        sqlTemp.addProjection("doc");
        sqlTemp.addCriteria("doc.nrDoctoServico", ">", 0);
        sqlTemp.addOrderBy("doc.dtPrevEntrega", "asc");

        return getAdsmHibernateTemplate().find(sqlTemp.getSql(), sqlTemp.getCriteria());
    }

    /**
     * Obt?m o SqlTemplate para os m?todos (findPaginatedConsultarVeiculoDocumentos) e (getRowCountConsultarVeiculoDocumentos)
     *
     * @param tfm
     * @return
     */
    private SqlTemplate getDocumentosManifestoQuery(TypedFlatMap tfm) {
        Long idManifesto = tfm.getLong("manifesto.idManifesto");
        Long idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");

        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map( ds.idDoctoServico ", "idDoctoServico");
        sql.addProjection("ds.tpDocumentoServico", "tpDocumentoServico");
        sql.addProjection("fi.sgFilial", "sgFilial");
        sql.addProjection("ds.nrDoctoServico", "nrDoctoServico");
        sql.addProjection("ds.vlMercadoria", "vlMercadoria");
        sql.addProjection("ds.psReal", "psReal");
        sql.addProjection("ds.qtVolumes", "qtVolumes");
        sql.addProjection("mo.sgMoeda", "sgMoeda");
        sql.addProjection("mo.dsSimbolo", "dsSimbolo");
        sql.addProjection("remPessoa.nmPessoa", "nmClienteRemetente");
        sql.addProjection("destPessoa.nmPessoa", "nmClienteDestinatario)");

        String fromClause = new StringBuilder()
                .append(DoctoServico.class.getName() + " ds")
                .append(" inner join ds.moeda mo")
                .append(" inner join ds.filialByIdFilialOrigem fi")
                .append(" inner join ds.clienteByIdClienteRemetente rem")
                .append(" inner join rem.pessoa remPessoa")
                .append("  left join ds.clienteByIdClienteDestinatario dest")
                .append("  left join dest.pessoa destPessoa").toString();
        sql.addFrom(fromClause);

        String viagemNacionalSub = new StringBuilder()
                .append(" select cto.id")
                .append("  from " + Manifesto.class.getName() + " m")
                .append("  join m.manifestoViagemNacional mvn")
                .append("  join mvn.manifestoNacionalCtos mnc")
                .append("  join mnc.conhecimento cto")
                .append(" where m.id = ?").toString();
        sql.addCriteriaValue(idManifesto);

        String viagemInterSub = new StringBuilder()
                .append(" select ctoi.id")
                .append("   from " + Manifesto.class.getName() + " m")
                .append("   join m.manifestoInternacional mi")
                .append("   join mi.manifestoInternacCtos mic")
                .append("   join mic.ctoInternacional ctoi ")
                .append("  where m.id = ?").toString();
        sql.addCriteriaValue(idManifesto);

        String entregaSub = new StringBuilder()
                .append(" select med.doctoServico.id")
                .append("   from " + Manifesto.class.getName() + " m")
                .append("   join m.manifestoEntrega me")
                .append("   join me.manifestoEntregaDocumentos med")
                .append("  where m.id = ?").toString();
        sql.addCriteriaValue(idManifesto);

        sql.addCustomCriteria("( ds.id in (" + viagemNacionalSub + ")"
                + " or ds.id in (" + viagemInterSub + ")"
                + " or ds.id in (" + entregaSub + ")) ");

        String cdEvento = new StringBuilder()
                .append("select eds.doctoServico.id ")
                .append("from " + EventoDocumentoServico.class.getName() + " eds ")
                .append("join eds.evento e ")
                .append("where ")
                .append("eds.blEventoCancelado = ? ")
                .append("and e.cdEvento = ? ").toString();
        sql.addCriteriaValue(Boolean.FALSE);
        sql.addCriteriaValue(Short.valueOf("21"));
        sql.addCustomCriteria("ds.id not in (" + cdEvento + ")");

        sql.addCriteria("ds.id", "=", idDoctoServico);

        sql.addOrderBy("ds.tpDocumentoServico");
        sql.addOrderBy("fi.sgFilial");
        sql.addOrderBy("ds.nrDoctoServico");

        return sql;
    }

    /**
     * FindPaginated que obt?m os documentos de servico de determinado manifesto
     *
     * @param tfm
     * @param fd
     * @return
     */
    public ResultSetPage findPaginatedDocumentosManifesto(TypedFlatMap tfm, FindDefinition fd) {
        SqlTemplate sql = getDocumentosManifestoQuery(tfm);
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria());
    }

    /**
     * GetRowCount que obt?m a quantidade de documentos de servico de determinado manifesto
     *
     * @param tfm
     * @return
     */
    public Integer getRowCountDocumentosManifesto(TypedFlatMap tfm) {
        SqlTemplate sql = getDocumentosManifestoQuery(tfm);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    }

    /**
     * @param tpDoctoServico
     * @param dtEmissao
     * @param valor
     * @return List
     */
    public ResultSetPage findPaginatedDoctoServicos(TypedFlatMap criteria) {
        /** Define os parametros para pagina??o */
        FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
                sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
                sqlQuery.addScalar("dsTpDocumentoServico", Hibernate.STRING);
                sqlQuery.addScalar("tpSituacao", Hibernate.STRING);
                sqlQuery.addScalar("dhEmissao", Hibernate.TIMESTAMP);
                sqlQuery.addScalar("valorTotal", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("sgMoeda", Hibernate.STRING);
                sqlQuery.addScalar("dsSimbolo", Hibernate.STRING);
            }
        };

        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("distinct(ds.id_docto_servico) idDoctoServico");
        sql.addProjection("ds.tp_documento_servico", "tpDocumentoServico");
        sql.addProjection("f.sg_filial", "sgFilialOrigem");
        sql.addProjection("ds.nr_docto_servico", "nrDoctoServico");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I", "dsTpDocumentoServico"));
        sql.addProjection("NVL(cctr.tp_situacao_conhecimento," +
                " NVL(ci.tp_situacao_crt, NVL(nfs.tp_situacao_nf," +
                " NVL(cnft.tp_situacao_conhecimento," +
                " NVL(ndn.tp_situacao_nota_debito_nac, '')))))", "tpSituacao");
        sql.addProjection("ds.dh_emissao", "dhEmissao");
        sql.addProjection("ds.vl_total_doc_servico", "valorTotal");
        sql.addProjection("moeda.sg_moeda", "sgMoeda");
        sql.addProjection("moeda.ds_simbolo", "dsSimbolo");

        defaultSqlTemplate(sql, criteria);

        sql.addOrderBy("tpDocumentoServico");
        sql.addOrderBy("sgFilialOrigem");
        sql.addOrderBy("nrDoctoServico");

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria(), configSql);
    }

    /**
     * @param tpDoctoServico
     * @param dtEmissao
     * @param valor
     * @return List
     */
    public Integer getRowCountDoctoServicos(TypedFlatMap criteria) {
        SqlTemplate sql = new SqlTemplate();
        sql.addProjection("distinct(ds.id_docto_servico)");

        defaultSqlTemplate(sql, criteria);
        return getAdsmHibernateTemplate().getRowCountBySql(" from (" + sql.getSql() + ")", sql.getCriteria());
    }

    /**
     * @param sql
     */
    private void defaultSqlTemplate(SqlTemplate sql, TypedFlatMap criteria) {

        Long idDoctoServicoHidden = criteria.getLong("doctoServico.idDoctoServicoHidden");
        String tpDoctoServico = criteria.getString("doctoServico.tpDocumentoServico");
        Long idFilial = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");

        YearMonthDay dtEmissao = criteria.getYearMonthDay("dataEmissao");
        java.math.BigDecimal valor = criteria.getBigDecimal("valorDocumento");

        sql.addFrom("docto_servico ds " +
                "inner join moeda moeda " +
                "	on ds.id_moeda = moeda.id_moeda " +
                "inner join devedor_doc_serv_fat ddsf " +
                "on ds.id_docto_servico = ddsf.id_docto_servico " +
                "left join conhecimento cctr " +
                "on ds.id_docto_servico = cctr.id_conhecimento " +
                "and ds.tp_documento_servico in ('CTR', 'CTE') " +
                "left join cto_internacional ci " +
                "on ds.id_docto_servico = ci.id_cto_internacional " +
                "left join nota_fiscal_servico nfs " +
                "on ds.id_docto_servico = nfs.id_nota_fiscal_servico " +
                "left join conhecimento cnft " +
                "on ds.id_docto_servico = cnft.id_conhecimento " +
                "and ds.tp_documento_servico in ('NFT', 'NTE') " +
                "left join nota_debito_nacional ndn " +
                "on ds.id_docto_servico = ndn.id_nota_debito_nacional " +
                "inner join filial f " +
                "on f.id_filial = ds.id_filial_origem, " +
                "VALOR_DOMINIO VD " +
                "	inner join dominio d " +
                "		on VD.id_dominio = d.id_dominio");

        sql.addCriteria("d.nm_dominio", "=", "DM_TIPO_DOCUMENTO_SERVICO");
        sql.addCustomCriteria("VD.vl_valor_dominio = ds.TP_DOCUMENTO_SERVICO");

        sql.addCustomCriteria("( cctr.id_conhecimento is not null " +
                "or ci.id_cto_internacional is not null " +
                "or nfs.id_nota_fiscal_servico is not null " +
                "or cnft.id_conhecimento is not null " +
                "or ndn.id_nota_debito_nacional is not null )");


        sql.addCriteria("ds.id_docto_servico", "=", idDoctoServicoHidden);

        sql.addCriteria("ds.tp_documento_servico", "=", tpDoctoServico);

        sql.addCriteria("f.id_filial", "=", idFilial);

        sql.addCriteria("TRUNC(CAST(ds.dh_emissao as date))", "=", dtEmissao);

        sql.addCriteria("ds.vl_total_doc_servico", "=", valor);
    }


    /**
     * FindById que carrega a filial de origem com o doctoServico
     *
     * @param id
     * @return DoctoServico
     */
    public DoctoServico findDoctoServico(Long id) {
        DoctoServico doctoServico = (DoctoServico) findById(id);
        Hibernate.initialize(doctoServico.getFilialByIdFilialOrigem());
        Hibernate.initialize(doctoServico.getMoeda());
        return doctoServico;
    }

    public boolean findEventoByDoctoServico(Long idDoctoServico, String tpDocumento) {
        SqlTemplate hql = new SqlTemplate();


        hql.addFrom("DoctoServico dc " +
                "left outer join dc.eventoDocumentoServicos eventoDoc ");

        hql.addCriteria("dc.idDoctoServico", "=", idDoctoServico);
        hql.addCriteria("eventoDoc.tpDocumento", "=", tpDocumento);

        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();

    }

    public boolean findLocalizacaoMercByDoctoServico(Long idDoctoServico, String dsLocalizacaoMercadoria) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("locMerc");
        hql.addFrom("DoctoServico dc " +
                "left outer join dc.eventoDocumentoServicos eventoDoc " +
                "join eventoDoc.evento evento " +
                "left outer join evento.localizacaoMercadoria locMerc ");

        hql.addCriteria("dc.idDoctoServico", "=", idDoctoServico);
        hql.addCriteria("upper(" + PropertyVarcharI18nProjection.createProjection("locMerc.dsLocalizacaoMercadoria") + ")", "like", dsLocalizacaoMercadoria.toUpperCase());

        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
    }

    public boolean findLocalizacaoMerCANByDoctoServico(Long idDoctoServico, Short cdLocalizacaoMercadoria) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("locMerc");
        hql.addFrom("DoctoServico dc " +
                "left outer join dc.eventoDocumentoServicos eventoDoc " +
                "join eventoDoc.evento evento " +
                "left outer join evento.localizacaoMercadoria locMerc ");

        hql.addCriteria("eventoDoc.blEventoCancelado", "=", Boolean.FALSE);
        hql.addCriteria("dc.idDoctoServico", "=", idDoctoServico);
        hql.addCriteria("locMerc.cdLocalizacaoMercadoria", "=", cdLocalizacaoMercadoria);

        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
    }

    public List<Map<String, Object>> findRotaColetaEntregaById(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("new Map(" +
                "rcer.nrRota as nrRota)");

        hql.addFrom("DoctoServico dc " +
                "left outer join dc.rotaColetaEntregaByIdRotaColetaEntregaReal rcer");

        hql.addCriteria("dc.idDoctoServico", "=", idDoctoServico);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    /**
     * M?todo que retorna um DoctoServico com fetch de alguns objetos a partir de um ID de DoctoServico.
     *
     * @param idDoctoServico
     * @return DoctoServico
     */
    public DoctoServico findDoctoServicoById(Long idDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        sql.addFrom(DoctoServico.class.getName() + " doc left join fetch doc.servico ser " +
                " left join fetch ser.tipoServico tps " +
                " left join fetch doc.localizacaoMercadoria lm " +
                " join fetch doc.moeda moe " +
                " join fetch doc.filialByIdFilialOrigem fo " +
                " join fetch fo.pessoa fop " +
                " left join fetch doc.filialByIdFilialDestino fd " +
                " left join fetch fd.pessoa fdp " +
                " left join fetch doc.clienteByIdClienteRemetente cr " +
                " left join fetch cr.pessoa crp " +
                " left join fetch doc.clienteByIdClienteDestinatario cd " +
                " left join fetch cd.pessoa cdp ");

        sql.addCriteria("doc.id", "=", idDoctoServico);

        return (DoctoServico) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }

    /**
     * M?todo que retorna um DoctoServico usando como filtro o Tipo de Documento,
     * a Filial de Origem e o N?mero.
     *
     * @param tpDocumento
     * @param idFilialOrigem
     * @param nrDoctoServico
     * @param idFilialLocalizacao
     * @return DoctoServico
     */
    public DoctoServico findDoctoServicoByTpDocumentoByIdFilialOrigemByNrDoctoServicoByIdFilialLocalizacao(
            String tpDocumento, Long idFilialOrigem, Long nrDoctoServico, Long idFilialLocalizacao) {

        SqlTemplate hql = new SqlTemplate();
        hql.addFrom(DoctoServico.class.getName() + " doc left join fetch doc.servico ser " +
                " left join fetch ser.tipoServico tps " +
                " left join fetch doc.localizacaoMercadoria lm " +
                " join fetch doc.moeda moe " +
                " join fetch doc.filialLocalizacao fLoc " +
                " join fetch doc.filialByIdFilialOrigem fo " +
                " join fetch fo.pessoa fop " +
                " left join fetch doc.filialByIdFilialDestino fd " +
                " left join fetch fd.pessoa fdp " +
                " left join fetch doc.clienteByIdClienteRemetente cr " +
                " left join fetch cr.pessoa crp " +
                " left join fetch doc.clienteByIdClienteDestinatario cd " +
                " left join fetch cd.pessoa cdp " +
                " left join fetch doc.clienteByIdClienteConsignatario cc " +
                " left join fetch cc.pessoa ccp " +
                " left join fetch doc.fluxoFilial " +
                " left join fetch doc.rotaColetaEntregaByIdRotaColetaEntregaReal rce" +
                " left join fetch doc.rotaColetaEntregaByIdRotaColetaEntregaSugerid rces ");

        hql.addCriteria("doc.tpDocumentoServico", "=", tpDocumento);
        hql.addCriteria("doc.filialByIdFilialOrigem.id", "=", idFilialOrigem);
        hql.addCriteria("doc.nrDoctoServico", "=", nrDoctoServico);
        if (idFilialLocalizacao != null) {
            hql.addCriteria("doc.filialLocalizacao.id", "=", idFilialLocalizacao);
        }

        if ("CTR".equals(tpDocumento) || "NFT".equals(tpDocumento) || "CTE".equals(tpDocumento) || "NTE".equals(tpDocumento)) {
            hql.addCustomCriteria("( not exists ( select conhecimento.id " +
                    "			  from " + Conhecimento.class.getName() + " conhecimento " +
                    "			  where conhecimento.tpSituacaoConhecimento = ? " +
                    "				    and conhecimento.id = doc.id ) )");
            hql.addCriteriaValue("C");
        } else if ("CRT".equals(tpDocumento)) {
            hql.addCustomCriteria("( not exists ( select ctoInternacional.id " +
                    "			  from " + CtoInternacional.class.getName() + " ctoInternacional " +
                    "			  where ctoInternacional.tpSituacaoCrt =<> ? " +
                    "				    and ctoInternacional.id = doc.id ) )");
            hql.addCriteriaValue("C");
        } else if ("MDA".equals(tpDocumento)) {
            hql.addCustomCriteria("( not exists ( select mda.id " +
                    "	 		  from " + Mda.class.getName() + " mda " +
                    "			  where mda.tpStatusMda = ? " +
                    "				    and mda.id = doc.id ) )");
            hql.addCriteriaValue("C");
        } else if ("RRE".equals(tpDocumento)) {
            hql.addCustomCriteria("( not exists ( select rre.id " +
                    "	 		  from " + ReciboReembolso.class.getName() + " rre " +
                    "			  where rre.tpSituacaoRecibo = ? " +
                    "				    and rre.id = doc.id ) )");
            hql.addCriteriaValue("CA");
        }

        hql.addOrderBy("doc.dtPrevEntrega", "asc");

        return (DoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    public DoctoServico findByIdJoinProcessoSinistro(Long idDoctoServico) {
        StringBuilder s = new StringBuilder()
                .append(" select ds ")
                .append(" from " + DoctoServico.class.getName() + " ds ")
                .append(" inner join fetch ds.filialByIdFilialOrigem filialOrigem ")
                .append(" left  join fetch ds.filialByIdFilialDestino filialDestino ")
                .append(" inner join fetch ds.clienteByIdClienteRemetente clienteOrigem ")
                .append(" inner join fetch ds.moeda ")

                .append(" left  join ds.naoConformidades nc ")
                .append(" left  join nc.filial ")
                .append(" inner join fetch clienteOrigem.pessoa clientePessoaOrigem ")
                .append(" left  join fetch ds.clienteByIdClienteDestinatario clienteDestino ")
                .append(" left  join fetch clienteDestino.pessoa clientePessoaDestino ")
                .append(" inner join fetch ds.sinistroDoctoServicos sds ")
                .append(" left  join fetch sds.moeda ")

                .append(" where ds.id = ?");

        Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s.toString());
        query.setParameter(0, idDoctoServico);
        return (DoctoServico) query.uniqueResult();
    }


    /**
     * Busca todas as notas fiscais de um determinado conhecimento.
     *
     * @param idDoctoServico do doctoServico em questao
     * @return
     */
    public Integer getRowCountDoctoServicoWithNFConhecimento(Long idDoctoServico) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Conhecimento.class)
                .setFetchMode("notaFiscalConhecimento", FetchMode.JOIN)
                .add(Restrictions.eq("id", idDoctoServico))
                .setProjection(Projections.rowCount());
        Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(detachedCriteria);

        return result.intValue();
    }

    public Integer getRowCountDoctoServicoWithNFConhecimentoToInteger(Long idDoctoServico) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Conhecimento.class)
                .setFetchMode("notaFiscalConhecimento", FetchMode.JOIN)
                .add(Restrictions.eq("id", idDoctoServico))
                .setProjection(Projections.rowCount());
        Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(detachedCriteria);

        return result;
    }

    /**
     * Busca todas as notas fiscais de um determinado conhecimento.
     *
     * @param idDoctoServico do doctoServico em questao
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoWithNFConhecimento(Long idDoctoServico, FindDefinition findDefinition) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(NotaFiscalConhecimento.class)
                .setFetchMode("conhecimento", FetchMode.JOIN)
                .setFetchMode("conhecimento.moeda", FetchMode.JOIN)
                .add(Restrictions.eq("conhecimento.id", idDoctoServico));

        return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(detachedCriteria, findDefinition.getCurrentPage(), findDefinition.getPageSize());
    }

    public List findDataEmissaoDoctoServico(Long idDoctoServico) {

        SqlTemplate sql = new SqlTemplate();
        sql.addProjection("DS.DH_EMISSAO", "dhEmissao");
        sql.addProjection("ENDP.ID_MUNICIPIO", "idMunicipioFilDest");
        sql.addProjection("NVL(ENDPE.ID_MUNICIPIO, CTO.ID_MUNICIPIO_ENTREGA)", "idMunicipioEntregaDs");

        sql.addFrom("DOCTO_SERVICO", "DS");
        sql.addFrom("FILIAL", "FD");
        sql.addFrom("ENDERECO_PESSOA", "ENDP");
        sql.addFrom("CONHECIMENTO", "CTO");
        sql.addFrom("ENDERECO_PESSOA", "ENDPE");

        sql.addJoin("DS.ID_FILIAL_DESTINO", "FD.ID_FILIAL");
        sql.addJoin("FD.ID_FILIAL", "ENDP.ID_PESSOA (+)");
        sql.addJoin("DS.ID_DOCTO_SERVICO", "CTO.ID_CONHECIMENTO (+)");
        sql.addJoin("DS.ID_ENDERECO_ENTREGA", "ENDPE.ID_ENDERECO_PESSOA (+)");

        sql.addCriteria("DS.ID_DOCTO_SERVICO", "=", idDoctoServico);

        ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
                sqlQuery.addScalar("idMunicipioFilDest", Hibernate.LONG);
                sqlQuery.addScalar("idMunicipioEntregaDs", Hibernate.LONG);
            }
        };

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1), Integer.valueOf(10000), sql.getCriteria(), configSql).getList();
    }

    public List<Map<String, Object>> findLookupDoctoServico(final TypedFlatMap criteria) {
        StringBuilder projection = new StringBuilder()
                .append("new Map(emp.idEmpresa as idEmpresa, \n")
                .append("        ds.nrDoctoServico as nrDoctoServico, \n")
                .append("        ds.idDoctoServico as idDoctoServico, \n")
                .append("        ds.tpDocumentoServico as tpDocumentoServico, \n")
                .append("        ds.tpConhecimento as tpConhecimento, \n")
                .append("        ser.tpModal as modal, \n")
                .append("        ds.dhEmissao as dhEmissao, \n")
                .append("        ser.tpAbrangencia as abrangencia, \n")
                .append("        ts.idTipoServico as idTipoServico, \n")
                .append("        filOr.idFilial as idFilialOrigem, \n")
                .append("        filOr.sgFilial as sgFilialOrigem, \n")
                .append("        pesFilOr.nmFantasia as nmFantasiaFilialOrigem, \n")
                .append("        filDest.idFilial as idFilialDestino, \n")
                .append("        filDest.sgFilial as sgFilialDestino, \n")
                .append("        pesFilDest.nmFantasia as nmFantasiaFilialDestino, \n")
                .append("        pc.idPedidoColeta as idPedidoColeta, \n")
                .append("        pc.nrColeta as nrColeta, \n")
                .append("        filResp.sgFilial as sgFilialPedidoColeta, \n")
                .append("        filResp.idFilial as idFilialPedidoColeta, \n")
                .append("        rem.idCliente as idClienteRemetente, \n")
                .append("        rem.nrConta as nrContaRemetente, \n")
                .append("        pesRem.nrIdentificacao as nrIdentificacaoRemetente, \n")
                .append("        pesRem.tpIdentificacao as tpIdentificacaoRem, \n")
                .append("        pesRem.nmPessoa as nmPessoaRemetente, \n")
                .append("        pesRem.nmFantasia as nmFantasiaRemetente, \n")
                .append("        dest.idCliente as idClienteDestinatario, \n")
                .append("        dest.nrConta as nrContaDestinatario, \n")
                .append("        pesDest.nrIdentificacao as nrIdentificacaoDestinatario, \n")
                .append("        pesDest.tpIdentificacao as tpIdentificacaoDest, \n")
                .append("        pesDest.nmPessoa as nmPessoaDestinatario, \n")
                .append("        pesDest.nmFantasia as nmFantasiaDestinatario) \n");

        return findLookupCustom(projection, criteria);
    }

    public List<Map<String, Object>> findLookupCustomLocMerc(TypedFlatMap criteria) {
        StringBuilder projection = new StringBuilder()
                .append("new Map(emp.idEmpresa as empresa_idEmpresa, \n")
                .append("        ds.nrDoctoServico as nrDoctoServico, \n")
                .append("        ds.idDoctoServico as doctoServico_idDoctoServico, \n")
                .append("        ds.tpDocumentoServico as tpDoctoServico, \n")
                .append("        ds.tpConhecimento as tpConhecimento, \n")
                .append("        ser.tpModal as modal, \n")
                .append("        ds.dhEmissao as dhEmissao, \n")
                .append("        ser.tpAbrangencia as abrangencia, \n")
                .append("        ts.idTipoServico as tipoServico_idTipoServico, \n")
                .append("        filOr.idFilial as filialOrigem_idFilial, \n")
                .append("        filOr.sgFilial as filialOrigem_sgFilial, \n")
                .append("        pesFilOr.nmFantasia as filialOrigem_pessoa_nmFantasia, \n")
                .append("        filDest.idFilial as filialDestino_idFilial, \n")
                .append("        filDest.sgFilial as filialDestino_sgFilial, \n")
                .append("        pesFilDest.nmFantasia as filialDestino_pessoa_nmFantasia, \n")
                .append("        pc.idPedidoColeta as pedidoColeta_idPedidoColeta, \n")
                .append("        pc.nrColeta as pedidoColeta_nrColeta, \n")
                .append("        filResp.sgFilial as pedidoColeta_filialByIdFilialResponsavel_sgFilial, \n")
                .append("        filResp.idFilial as pedidoColeta_filialByIdFilialResponsavel_idFilial, \n")
                .append("        rem.idCliente as remetente_idCliente, \n")
                .append("        rem.nrConta as remetenteConta_nrConta, \n")
                .append("        pesRem.nrIdentificacao as remetente_pessoa_nrIdentificacao, \n")
                .append("        pesRem.tpIdentificacao as tpIdentificacaoRem, \n")
                .append("        pesRem.nmPessoa as remetente_pessoa_nmPessoa, \n")
                .append("        pesRem.nmFantasia as remetenteFantasia_pessoa_nmFantasia, \n")
                .append("        dest.idCliente as destinatario_idCliente, \n")
                .append("        dest.nrConta as destinatarioConta_nrConta, \n")
                .append("        pesDest.nrIdentificacao as destinatario_pessoa_nrIdentificacao, \n")
                .append("        pesDest.tpIdentificacao as tpIdentificacaoDest, \n")
                .append("        pesDest.nmPessoa as destinatario_pessoa_nmPessoa, \n")
                .append("        pesDest.nmFantasia as destinatariofantasia_pessoa_nmFantasia) \n");

        return findLookupCustom(projection, criteria);
    }

    private List<Map<String, Object>> findLookupCustom(final StringBuilder projection, final TypedFlatMap criteria) {
        StringBuilder from = new StringBuilder()
                .append(Conhecimento.class.getName()).append(" ds \n")
                .append("join ds.filialByIdFilialOrigem filOr \n")
                .append("left outer join filOr.pessoa pesFilOr \n")
                .append("join filOr.empresa emp \n")
                .append("left outer join ds.servico ser \n")
                .append("left outer join ser.tipoServico ts \n")
                .append("left outer join ds.filialByIdFilialDestino filDest \n")
                .append("left outer join filDest.pessoa pesFilDest \n")
                .append("left outer join ds.pedidoColeta pc \n")
                .append("left outer join pc.filialByIdFilialResponsavel filResp \n")
                .append("left outer join ds.clienteByIdClienteRemetente rem \n")
                .append("left outer join rem.pessoa pesRem \n")
                .append("left outer join ds.clienteByIdClienteDestinatario dest \n")
                .append("left outer join dest.pessoa pesDest \n");

        SqlTemplate hql = new SqlTemplate();
        hql.addProjection(projection.toString());
        hql.addFrom(from.toString());

        if (CONHECIMENTO_NACIONAL.equals(criteria.get("tpDocumentoServico"))) {
            hql.addCriteria("ds.tpConhecimento", "=", criteria.getString("finalidade"));
        }

        hql.addCriteria("ds.idDoctoServico", "=", criteria.getLong("idDoctoServico"));
        if (criteria.getLong("idDoctoServico") == null) {
            hql.addRequiredCriteria("ds.nrDoctoServico", "=", criteria.getLong("nrDoctoServico"));
        }

        hql.addCriteria("ser.tpModal", "=", criteria.getString("servico.tpModal"));
        hql.addCriteria("ser.tpAbrangencia", "=", criteria.getString("servico.tpAbrangencia"));
        hql.addCriteria("ts.idTipoServico", "=", criteria.getLong("servico.tipoServico.idTipoServico"));
        hql.addCriteria("filOr.idFilial", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
        hql.addCriteria("filDest.idFilial", "=", criteria.getLong("filialByIdFilialDestino.idFilial"));
        hql.addCriteria("ds.tpDocumentoServico", "=", criteria.getString("tpDocumentoServico"));
        hql.addCriteria("filOr.idFilial", "=", criteria.getLong("idFilialDoctoSer"));
        hql.addCriteria("ds.tpSituacaoConhecimento", "<>", "P");

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public List<Map<String, Object>> findLookupCustomReemb(TypedFlatMap criteria) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(" +
                "ds.nrDoctoServico as nrDoctoServico, " +
                "ds.idDoctoServico as doctoServico_idDoctoServico)");

        hql.addFrom(DoctoServico.class.getName() + " ds " +
                "join ds.filialByIdFilialOrigem filOr " +
                "left outer join filOr.pessoa pesFilOr ");

        hql.addCriteria("ds.nrDoctoServico", "=", criteria.getLong("nrDoctoServico"));
        hql.addCriteria("filOr.idFilial", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
        hql.addCriteria("ds.tpDocumentoServico", "=", criteria.getString("tpDocumentoServico"));

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public List<Map<String, Object>> findLookupCustomRotaColetaEnt(TypedFlatMap criteria) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(ds.nrDoctoServico as nrDoctoServico, " +
                "ds.idDoctoServico as doctoServico_idDoctoServico, " +
                "rotaReal.nrRota as nrRota, " +
                "rotaReal.dsRota as dsRota, " +
                "filDest.idFilial as idFilialDest, " +
                "filOr.sgFilial as filialByIdFilialOrigem_sgFilial, " +
                "filOr.idFilial as filialByIdFilialOrigem_idFilial, " +
                "pesOr.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, " +
                "filOper.idFilial as idFilialOper)");

        hql.addFrom(DoctoServico.class.getName() + " ds " +
                "join ds.filialByIdFilialOrigem filOr " +
                "left outer join filOr.pessoa pesOr " +
                "left outer join ds.rotaColetaEntregaByIdRotaColetaEntregaReal rotaReal " +
                "left outer join filOr.pessoa pesFilOr " +
                "left outer join ds.filialDestinoOperacional filOper " +
                "left outer join ds.filialByIdFilialDestino filDest ");

        hql.addCriteria("ds.idDoctoServico", "=", criteria.getLong("idDoctoServico"));
        hql.addCriteria("ds.nrDoctoServico", "=", criteria.getLong("nrDoctoServico"));
        hql.addCriteria("ds.tpDocumentoServico", "=", criteria.getString("tpDocumentoServico"));
        hql.addCriteria("filOr.idFilial", "=", criteria.getLong("idFilialDoctoSer"));

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public Filial findFilialDestinoOperacionalById(Long idDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("f");
        sql.addInnerJoin(getPersistentClass().getName(), "ds");
        sql.addInnerJoin("ds.filialDestinoOperacional", "f");
        sql.addCriteria("ds.id", "=", idDoctoServico);

        List<Filial> retorno = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

        return (!retorno.isEmpty()) ? retorno.get(0) : null;
    }

    public DivisaoCliente findDivisaoClienteById(Long idDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("dc");
        sql.addInnerJoin(getPersistentClass().getName(), "ds");
        sql.addInnerJoin("ds.divisaoCliente", "dc");
        sql.addCriteria("ds.id", "=", idDoctoServico);

        List<DivisaoCliente> retorno = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

        return (!retorno.isEmpty()) ? retorno.get(0) : null;
    }

    /**
     * Realiza o getRowCount da tela de <b>ReceberDocumentoServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @return
     */
    public Integer getRowCountDoctoServicoByIdManifestoAndIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico,
                                                                         Long idManifesto, Long idRotaIdaVolta) {

        SqlTemplate sql = getDoctoServicoByIdManifestoAndIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idManifesto, idRotaIdaVolta);

        Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
        if ((idDoctoServico != null || idRotaIdaVolta != null) && idManifesto == null && (rowCount.intValue() == 0)) {
            sql = getDoctoServicoByIdDoctoServicoOrByIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idRotaIdaVolta);
            rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
        }

        return rowCount;
    }

    /**
     * Realiza o findPaginated da tela de <b>ReceberDocumentoServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoByIdManifestoAndIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico,
                                                                                 Long idManifesto, Long idRotaIdaVolta, FindDefinition findDefinition) {

        SqlTemplate sql = getDoctoServicoByIdManifestoAndIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idManifesto, idRotaIdaVolta);

        sql.addOrderBy("rota.dsRota");

        sql.addOrderBy("doctoServico.tpDocumentoServico");
        sql.addOrderBy("filialOrigemDoctoServico.sgFilial");
        sql.addOrderBy("doctoServico.nrDoctoServico");

        ResultSetPage resultSetPage = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
        if ((idDoctoServico != null || idRotaIdaVolta != null) && idManifesto == null && resultSetPage.getList().isEmpty()) {
            sql = getDoctoServicoByIdDoctoServicoOrByIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idRotaIdaVolta);

            sql.addOrderBy("rota.dsRota");

            sql.addOrderBy("doctoServico.tpDocumentoServico");
            sql.addOrderBy("filialOrigemDoctoServico.sgFilial");
            sql.addOrderBy("doctoServico.nrDoctoServico");
            resultSetPage = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
        }

        return resultSetPage;
    }

    /**
     * Realiza a soma dos atributos encontrados pela pesquisa da tela de <b>ReceberDocumentosServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @param findDefinition
     * @return
     */
    public List<Map<String, Object>> findSumDoctoServicoByIdManifestoAndIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico,
                                                                                       Long idManifesto, Long idRotaIdaVolta) {

        StringBuilder projecao = new StringBuilder();
        projecao.append("select new map(SUM(doctoServico.qtVolumes) as qtVolumesTotal, ");
        projecao.append("SUM(doctoServico.psReal) as psRealTotal, ");
        projecao.append("SUM(doctoServico.vlMercadoria) as vlMercadoriaTotal, ");
        projecao.append("moeda.idMoeda as idMoeda, ");
        projecao.append("pais.idPais as idPais) ");

        SqlTemplate sql = getDoctoServicoByIdManifestoAndIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idManifesto, idRotaIdaVolta);

        sql.addGroupBy("moeda.idMoeda");
        sql.addGroupBy("pais.idPais");

        List<Map<String, Object>> listResult = getAdsmHibernateTemplate().find(projecao.toString() + sql.getSql(false), sql.getCriteria());
        if ((idDoctoServico != null || idRotaIdaVolta != null) && idManifesto == null && listResult.isEmpty()) {
            sql = getDoctoServicoByIdDoctoServicoOrByIdRotaIdaVolta(idDoctoServico, idFilialOrigem, tpDoctoServico, idRotaIdaVolta);

            sql.addGroupBy("moeda.idMoeda");
            sql.addGroupBy("pais.idPais");

            listResult = getAdsmHibernateTemplate().find(projecao.toString() + sql.getSql(false), sql.getCriteria());
        }

        return listResult;
    }

    /**
     * Consulta da tela de <b>ReceberDocumentoServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idFilialOrigem
     * @param tpDoctoServico
     * @param idManifesto
     * @param idRotaIdaVolta
     * @return
     */
    private SqlTemplate getDoctoServicoByIdManifestoAndIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico,
                                                                      Long idManifesto, Long idRotaIdaVolta) {

        SqlTemplate sql = new SqlTemplate();

        StringBuilder projecao = new StringBuilder();
        projecao.append("new Map (doctoServico.idDoctoServico as idDoctoServico, ");
        projecao.append("rota.dsRota as dsRota, ");
        projecao.append("rotaIdaVolta.nrRota as nrRota, ");
        projecao.append("filialOrigemManifesto.sgFilial as sgFilialManifesto, ");
        projecao.append("manifesto.idManifesto as idManifesto, ");
        projecao.append("manifesto.nrPreManifesto as nrPreManifesto, ");
        projecao.append("doctoServico.nrDoctoServico as nrDoctoServico, ");
        projecao.append("filialOrigemDoctoServico.sgFilial as sgFilialDoctoServico, ");
        projecao.append("doctoServico.tpDocumentoServico as tpDoctoServico, ");
        projecao.append("doctoServico.qtVolumes as qtVolumes, ");
        projecao.append("doctoServico.psReal as psReal, ");
        projecao.append("moeda.dsSimbolo as dsSimboloMoeda, ");
        projecao.append("doctoServico.vlMercadoria as vlMercadoria, ");
        projecao.append("rotaIdaVolta.idRotaIdaVolta as idRotaIdaVolta, ");
        projecao.append("moeda.sgMoeda as sgMoeda, ");
        //Esta subquery nao esta rodando na versao 3.0.5 do hibernate. Decorrente disto ela esta desativada.
        projecao.append("pais.idPais as idPais) ");

        sql.addProjection(projecao.toString());

        StringBuilder from = new StringBuilder();
        if (idRotaIdaVolta != null) {
            from.append(DoctoServico.class.getName() + " as doctoServico ");
            from.append("join doctoServico.rotaIdaVolta as rotaIdaVolta ");
            from.append("join rotaIdaVolta.rota as rota ");
            from.append("left join doctoServico.preManifestoDocumentos as preManifestoDocumento ");
            from.append("left join preManifestoDocumento.manifesto as manifesto ");
        } else {
            from.append(Manifesto.class.getName() + " as manifesto ");
            from.append("join manifesto.preManifestoDocumentos as preManifestoDocumento ");
            from.append("join preManifestoDocumento.doctoServico as doctoServico ");
            from.append("join manifesto.controleCarga as controleCarga ");
            from.append("left join controleCarga.rotaIdaVolta as rotaIdaVolta ");
            from.append("left join rotaIdaVolta.rota as rota ");
        }

        from.append("left join doctoServico.moeda as moeda ");
        from.append("left join doctoServico.filialByIdFilialOrigem as filialOrigemDoctoServico ");

        //Joins referentes ao pais do docto de servico.
        from.append("left join filialOrigemDoctoServico.empresa  as empresa ");
        from.append("left join empresa.pessoa as pessoa ");
        from.append("left join pessoa.enderecoPessoa as enderecoPessoa ");
        from.append("left join enderecoPessoa.municipio as municipio ");
        from.append("left join municipio.unidadeFederativa as unidadeFederativa ");
        from.append("left join unidadeFederativa.pais as pais ");
        from.append("left join manifesto.filialByIdFilialOrigem as filialOrigemManifesto");

        sql.addFrom(from.toString());

        if (idDoctoServico != null) {
            sql.addCriteria("doctoServico.id", "=", idDoctoServico);
        }
        if (idFilialOrigem != null) {
            sql.addCriteria("doctoServico.filialByIdFilialOrigem.id", "=", idFilialOrigem);
        }
        if (tpDoctoServico != null && !"".equals(tpDoctoServico)) {
            sql.addCriteria("doctoServico.tpDocumentoServico", "=", tpDoctoServico);
        }
        if (idManifesto != null) {
            sql.addCriteria("manifesto.id", "=", idManifesto);
        }
        if (idRotaIdaVolta != null) {
            sql.addCriteria("rotaIdaVolta.id", "=", idRotaIdaVolta);
        }

        sql.addCustomCriteria("manifesto.filialByIdFilialOrigem.id = " + SessionUtils.getFilialSessao().getIdFilial());
        sql.addCustomCriteria("((moeda.idMoeda is not null) AND (pais.idPais is not null))");

        return sql;
    }


    /**
     * Consulta da tela de <b>ReceberDocumentoServicoCheckIn</b>
     *
     * @param idDoctoServico
     * @param idFilialOrigem
     * @param tpDoctoServico
     * @param idRotaIdaVolta
     * @return
     */
    private SqlTemplate getDoctoServicoByIdDoctoServicoOrByIdRotaIdaVolta(Long idDoctoServico, Long idFilialOrigem, String tpDoctoServico, Long idRotaIdaVolta) {

        SqlTemplate sql = new SqlTemplate();

        StringBuilder projecao = new StringBuilder();
        projecao.append("new Map (doctoServico.idDoctoServico as idDoctoServico, ");
        projecao.append("rota.dsRota as dsRota, ");
        projecao.append("rotaIdaVolta.nrRota as nrRota, ");
        projecao.append("doctoServico.nrDoctoServico as nrDoctoServico, ");
        projecao.append("filialOrigemDoctoServico.sgFilial as sgFilialDoctoServico, ");
        projecao.append("doctoServico.tpDocumentoServico as tpDoctoServico, ");
        projecao.append("doctoServico.qtVolumes as qtVolumes, ");
        projecao.append("doctoServico.psReal as psReal, ");
        projecao.append("moeda.dsSimbolo as dsSimboloMoeda, ");
        projecao.append("doctoServico.vlMercadoria as vlMercadoria, ");
        projecao.append("rotaIdaVolta.idRotaIdaVolta as idRotaIdaVolta, ");
        projecao.append("moeda.sgMoeda as sgMoeda, ");
        //Esta subquery nao esta rodando na versao 3.0.5 do hibernate. Decorrente disto ela esta desativada.
        projecao.append("pais.idPais as idPais) ");

        sql.addProjection(projecao.toString());

        StringBuilder from = new StringBuilder();
        from.append(DoctoServico.class.getName() + " as doctoServico ");
        from.append("join doctoServico.rotaIdaVolta as rotaIdaVolta ");
        from.append("join rotaIdaVolta.rota as rota ");
        from.append("left join doctoServico.moeda as moeda ");
        from.append("left join doctoServico.filialByIdFilialOrigem as filialOrigemDoctoServico ");

        //Joins referentes ao pais do docto de servico.
        from.append("left join filialOrigemDoctoServico.empresa as empresa ");
        from.append("left join empresa.pessoa as pessoa ");
        from.append("left join pessoa.enderecoPessoa as enderecoPessoa ");
        from.append("left join enderecoPessoa.municipio as municipio ");
        from.append("left join municipio.unidadeFederativa as unidadeFederativa ");
        from.append("left join unidadeFederativa.pais as pais ");

        sql.addFrom(from.toString());

        if (idDoctoServico != null) {
            sql.addCriteria("doctoServico.id", "=", idDoctoServico);
        }
        if (idFilialOrigem != null) {
            sql.addCriteria("doctoServico.filialByIdFilialOrigem.id", "=", idFilialOrigem);
        }
        if (tpDoctoServico != null && !"".equals(tpDoctoServico)) {
            sql.addCriteria("doctoServico.tpDocumentoServico", "=", tpDoctoServico);
        }
        if (idRotaIdaVolta != null) {
            sql.addCriteria("rotaIdaVolta.id", "=", idRotaIdaVolta);
        }
        sql.addCustomCriteria("((moeda.idMoeda is not null) AND (pais.idPais is not null))");

        return sql;
    }

    /**
     * Retorna a hrSaida do trecho_rota_ida_volta apartir de um
     * idRotaIdaVolta
     *
     * @param idRotaIdaVolta
     * @return
     */
    public List<TimeOfDay> findSubQueryHrSaida(Long idRotaIdaVolta) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("trechoRotaIdaVolta.hrSaida as hrSaida ");

        StringBuilder from = new StringBuilder();

        from.append(TrechoRotaIdaVolta.class.getName() + " as trechoRotaIdaVolta ");
        from.append("left join trechoRotaIdaVolta.filialRotaByIdFilialRotaOrigem as filialRotaOrigem ");
        from.append("left join trechoRotaIdaVolta.filialRotaByIdFilialRotaDestino as filialRotaDestino ");
        sql.addFrom(from.toString());

        sql.addCriteria("trechoRotaIdaVolta.rotaIdaVolta.idRotaIdaVolta", "=", idRotaIdaVolta);
        sql.addCustomCriteria("filialRotaOrigem.blOrigemRota = 'S'");
        sql.addCustomCriteria("filialRotaDestino.blDestinoRota = 'S'");

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     * @param tpManifesto
     * @param blPreManifesto
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param idFilialOrigem
     * @param param
     * @param isRowCount
     * @return
     */
    public String addSqlByFindPaginatedDoctoServicoByManifesto(
            String tpManifesto,
            Boolean blPreManifesto,
            Long idManifesto,
            Long idDoctoServico,
            String tpDocumentoServico,
            Long idFilialOrigem,
            List<Object> param,
            boolean isRowCount
    ) {
        StringBuilder sql = new StringBuilder();
        if (!isRowCount) {
            sql.append("select new map(")
                    .append("doctoServico.tpDocumentoServico as tpDoctoServico, ")
                    .append("filial.sgFilial as sgFilial, ")
                    .append("filialDestino.sgFilial as sgFilialDestino, ")
                    .append("doctoServico.id as idDoctoServico, ")
                    .append("doctoServico.nrDoctoServico as nrDoctoServico, ")
                    .append("pessoaClienteRemetente.nmPessoa as nmPessoaRemetente, ")
                    .append("pessoaClienteDestinatario.nmPessoa as nmPessoaDestinatario, ")
                    .append("doctoServico.vlMercadoria as vlMercadoria, ")
                    .append("doctoServico.vlTotalDocServico as vlTotalDocServico, ")
                    .append("doctoServico.psReal as psReal, ")
                    .append("doctoServico.psAforado as psAforado, ")
                    .append("doctoServico.qtVolumes as qtVolumes, ")
                    .append("doctoServico.dhEmissao as dhEmissao, ")
                    .append("doctoServico.dtPrevEntrega as dtPrevEntrega, ")
                    .append("moeda.sgMoeda as sgMoeda, ")
                    .append("moeda.dsSimbolo as dsSimbolo, ")
                    .append("servico.sgServico as sgServico) ");
        } else {
            sql.append("select count(doctoServico.idDoctoServico) as valor ");
        }

        sql.append("from ")
                .append(Manifesto.class.getName()).append(" as manifesto ");

        // pr? - manifesto
        if (blPreManifesto) {
            sql.append("inner join manifesto.preManifestoDocumentos as pmd ")
                    .append("inner join pmd.doctoServico as doctoServico ");
        } else if ("VN".equals(tpManifesto)) {
            //manifesto viagem nacional
            sql.append("inner join manifesto.manifestoViagemNacional as mvn ")
                    .append("inner join mvn.manifestoNacionalCtos as mnCto ")
                    .append("inner join mnCto.conhecimento as doctoServico ");
        } else if ("VI".equals(tpManifesto)) {
            //manifesto viagem internacional
            sql.append("inner join manifesto.manifestoInternacional as mi ")
                    .append("inner join mi.manifestoInternacCtos as miCto ")
                    .append("inner join miCto.ctoInternacional as doctoServico ");
        } else {
            //manifesto entrega
            sql.append("inner join manifesto.manifestoEntrega as me ")
                    .append("inner join me.manifestoEntregaDocumentos as meDoc ")
                    .append("inner join meDoc.doctoServico as doctoServico ");
        }

        sql.append("inner join doctoServico.moeda as moeda ")
                .append("left join doctoServico.servico as servico ")
                .append("inner join doctoServico.filialByIdFilialOrigem as filial ")
                .append("left join doctoServico.filialByIdFilialDestino as filialDestino ")
                .append("left join doctoServico.clienteByIdClienteRemetente as clienteRemetente ")
                .append("left join doctoServico.clienteByIdClienteDestinatario as clienteDestinatario ")
                .append("left join clienteRemetente.pessoa as pessoaClienteRemetente ")
                .append("left join clienteDestinatario.pessoa as pessoaClienteDestinatario ")
                .append("where ")
                .append("manifesto.id = ? ");

        param.add(idManifesto);

        if (idDoctoServico != null) {
            sql.append("and doctoServico.id = ? ");
            param.add(idDoctoServico);
        } else {
            if (!StringUtils.isBlank(tpDocumentoServico)) {
                sql.append("and doctoServico.tpDocumentoServico = ? ");
                param.add(tpDocumentoServico);
            }
            if (idFilialOrigem != null) {
                sql.append("and doctoServico.filialByIdFilialOrigem.id = ? ");
                param.add(idFilialOrigem);
            }
        }
        return sql.toString();
    }

    /**
     * @param tpManifesto
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param idFilialOrigem
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedDoctoServicoByManifesto(String tpManifesto, Boolean blPreManifesto, Long idManifesto,
                                                              Long idDoctoServico, String tpDocumentoServico, Long idFilialOrigem, FindDefinition findDefinition) {
        List<Object> param = new ArrayList<Object>();
        String sql = addSqlByFindPaginatedDoctoServicoByManifesto(tpManifesto,
                blPreManifesto, idManifesto, idDoctoServico, tpDocumentoServico, idFilialOrigem, param, false);
        ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
        return rsp;
    }


    /**
     * @param tpManifesto
     * @param blPreManifesto
     * @param idManifesto
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param idFilialOrigem
     * @return
     */
    public Integer getRowCountDoctoServicoByManifesto(String tpManifesto, Boolean blPreManifesto, Long idManifesto,
                                                      Long idDoctoServico, String tpDocumentoServico, Long idFilialOrigem) {
        List<Object> param = new ArrayList<Object>();
        String sql = addSqlByFindPaginatedDoctoServicoByManifesto(tpManifesto,
                blPreManifesto, idManifesto, idDoctoServico, tpDocumentoServico, idFilialOrigem, param, true);
        List<Long> result = getAdsmHibernateTemplate().find(sql, param.toArray());
        return result.get(0).intValue();
    }

    /**
     * Verifica se o documento de servico possui uma localiza??o de mercadoria entregue
     *
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountDoctoServicoEntregues(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();
        hql.addFrom(new StringBuilder(DoctoServico.class.getName()).append(" AS DS ")
                .append("INNER JOIN DS.localizacaoMercadoria AS LM ").toString());

        hql.addCriteria("LM.cdLocalizacaoMercadoria", "=", ConstantesSim.CD_MERCADORIA_ENTREGA_EFETUADA);
        hql.addCriteria("DS.id", "=", idDoctoServico);

        return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }


    /**
     * Consulta os documentos de servico sem recibo de reembolso associado, a partir do conhecimento
     *
     * @param idConhecimento
     * @return
     */
    public Map<String, Object> findDocumentoWithTipoFrete(Long idDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection(" new Map(ds.tpDocumentoServico as tpDocumentoServico, " +
                "ds.idDoctoServico as idDoctoServico, " +
                "ds.nrDoctoServico as nrDoctoServico, " +
                "ds.dhEmissao as dhEmissao, " +
                "ds.vlTotalDocServico as vlTotalDocServico, " +
                "moeda.sgMoeda ||' '|| moeda.dsSimbolo as siglaSimbolo, " +
                "fil.sgFilial as filial, " +
                "fil.idFilial as idFilial, " +
                "p.nmFantasia as nmPessoa," +
                "ds.vlIcmsSubstituicaoTributaria as vlIcmsSubstituicaoTributaria," +
                "ddsf.vlDevido as vlDevido)"

        );

        sql.addFrom(DoctoServico.class.getName() + " ds " +
                "join ds.filialByIdFilialOrigem fil " +
                "join fil.pessoa p " +
                "join ds.moeda moeda " +
                "join ds.devedorDocServFats ddsf ");

        sql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);

        List<Map<String, Object>> parcial = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
        Map<String, Object> mapa = null;
        if (parcial != null && !parcial.isEmpty()) {
            mapa = parcial.get(0);
        }

        return mapa;
    }

    /**
     * verifica se existe uma controle de carga para o documento de servico
     *
     * @param idDoctoServico
     * @return
     */
    public boolean findCCByIdDoctoServico(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("1");

        hql.addFrom(DoctoServico.class.getName() + " ds " +
                "join ds.pedidoColeta pc " +
                "join pc.manifestoColeta manC " +
                "join manC.controleCarga cc ");

        hql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);
        hql.addCriteria("cc.tpStatusControleCarga", "<>", "CA");

        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
    }

    //###########################################################
    // Consulta de adicionar doctoServico pre-manifesto Terminal
    //###########################################################

    /**
     * Consulta de adicionar doctoServico pre-manifesto Terminal.
     * Esta trata apenas de gerar a consulta caso o manifesto em questao seja de coleta/entrega.
     *
     * @param idSolicitacaoRetirada
     * @param idRotaColetaEntrega
     * @param idConsignatario
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param tpDoctoServico
     * @param idFilialOrigemDoctoServico
     * @param idFilialDestinoDoctoServico
     * @param idDoctoServico
     * @param idAwb
     * @param tpPreManifesto
     * @param idsDoctoServicos
     * @param idsLocalizacao
     * @return
     */
    public List findAdicionarDoctoServicoEntrega(
            Long idSolicitacaoRetirada,
            Long idRotaColetaEntrega,
            Long idConsignatario,
            Long idClienteRemetente,
            Long idClienteDestinatario,
            String tpDoctoServico,
            Long idFilialOrigemDoctoServico,
            Long idFilialDestinoDoctoServico,
            Long idDoctoServico,
            Long idAwb,
            String tpPreManifesto,
            List<Long> idsDoctoServicos,
            List<Long> idsLocalizacao,
            Long idFilialLocalizacao,
            Long idPedidoColeta,
            String dsBox,
            String bloqFluxoSubcontratacao) {
        //Caso a consulta seja para a soma das informacoes contidas na grid...
        boolean hasBox = dsBox != null;

        SqlTemplate sql = getHqlAdicionarDoctoServico(hasBox);

        sql.addCriteria("doctoServico.bl_bloqueado", "=", "N");

        //Adicionado para compatibilizar com versao atual de sql...
        sql.addCustomCriteria("doctoServico.tp_documento_servico in ('CRT','CTR','NFT','RRE','MDA','CTE','NTE')");
        sql.addCustomCriteria("localizacaoMercadoria.id_Localizacao_Mercadoria in " + SQLUtils.mountNumberForInExpression(idsLocalizacao));
        if (idFilialLocalizacao != null) {
            sql.addCustomCriteria("doctoServico.ID_FILIAL_LOCALIZACAO = " + idFilialLocalizacao);
        }

        if (idPedidoColeta != null) {
            sql.addCustomCriteria("doctoServico.ID_PEDIDO_COLETA = " + idPedidoColeta);
        }

        if("S".equalsIgnoreCase(bloqFluxoSubcontratacao)){
            sql.addCustomCriteria("(doctoServico.BL_FLUXO_SUBCONTRATACAO = 'N' or doctoServico.BL_FLUXO_SUBCONTRATACAO is null) ");
        }

        if (hasBox) {
            sql.addCustomCriteria("dadosComplemento.DS_VALOR_CAMPO = '" + dsBox.toString() + "'");
        }
        
        //Filtro por tipos de doctoServico...
        StringBuilder subQueryConhecimento = new StringBuilder();
        subQueryConhecimento.append("select conhecimento.id_conhecimento ");
        subQueryConhecimento.append("from conhecimento ");
        subQueryConhecimento.append("where conhecimento.tp_situacao_conhecimento = ? and conhecimento.id_conhecimento = doctoServico.id_docto_servico");

        StringBuilder subQueryConhecimentoInt = new StringBuilder();
        subQueryConhecimentoInt.append("select cto_Internacional.id_cto_Internacional ");
        subQueryConhecimentoInt.append("from cto_internacional ");
        subQueryConhecimentoInt.append("where cto_internacional.tp_situacao_crt = ? and cto_internacional.id_cto_internacional = doctoServico.id_docto_servico");

        StringBuilder subQueryMDA = new StringBuilder();
        subQueryMDA.append("select mda.id_mda ");
        subQueryMDA.append("from mda ");
        subQueryMDA.append("where mda.tp_status_mda = ? and mda.id_mda = doctoServico.id_docto_servico");

        StringBuilder subQueryReciboReembolso = new StringBuilder();
        subQueryReciboReembolso.append("select recibo_reembolso.id_recibo_reembolso ");
        subQueryReciboReembolso.append("from recibo_reembolso ");
        subQueryReciboReembolso.append("where recibo_reembolso.tp_situacao_recibo = ? and recibo_reembolso.id_recibo_reembolso = doctoServico.id_docto_servico");

        StringBuilder subQuerys = new StringBuilder();
        subQuerys.append("(");
        subQuerys.append(" not exists ");
        subQuerys.append("(" + subQueryConhecimento.toString() + ")");
        subQuerys.append(" and not exists ");
        subQuerys.append("(" + subQueryConhecimentoInt.toString() + ")");
        subQuerys.append(" and not exists");
        subQuerys.append("(" + subQueryMDA.toString() + ")");
        subQuerys.append(" and not exists");
        subQuerys.append("(" + subQueryReciboReembolso.toString() + ")");
        subQuerys.append(")");

        sql.addCustomCriteria(subQuerys.toString());
        sql.addCriteriaValue("C");
        sql.addCriteriaValue("C");
        sql.addCriteriaValue("C");
        sql.addCriteriaValue("CA");

        if (!"PR".equals(tpPreManifesto) && !"EP".equals(tpPreManifesto)) {

            sql.addFrom("filial", "filialDestinoOperacional");
            sql.addCustomCriteria("doctoServico.id_filial_destino_operacional = filialDestinoOperacional.id_filial(+)");
            sql.addCustomCriteria("nvl(filialDestinoOperacional.id_filial, filialDestino.id_filial) = ? ");
            sql.addCriteriaValue(SessionUtils.getFilialSessao().getIdFilial());

            if (idRotaColetaEntrega != null) {
                sql.addFrom("rota_Coleta_Entrega", "rotaColetaEntregaSugerida");
                sql.addCustomCriteria("doctoServico.id_rota_Coleta_Entrega_Sugerid = rotaColetaEntregaSugerida.id_rota_coleta_entrega(+)");
                sql.addCustomCriteria("nvl(rotaColetaEntrega.id_rota_coleta_entrega, rotaColetaEntregaSugerida.id_rota_coleta_entrega) = ? ");
                sql.addCriteriaValue(idRotaColetaEntrega);

            }
        }

        if ("CR".equals(tpPreManifesto) && idSolicitacaoRetirada != null) {
            sql.addCustomCriteria("doctoServico.id_docto_servico in (" + subQueryAdicionarDoctoServicoEntregaClienteRetira() + ")");
            sql.addCriteriaValue(idSolicitacaoRetirada);
        }

        this.findAdicionarDoctoServicoBasicFilters(
                sql,
                idClienteRemetente,
                idClienteDestinatario,
                tpDoctoServico,
                idFilialOrigemDoctoServico,
                idFilialDestinoDoctoServico,
                idDoctoServico,
                idAwb,
                idsDoctoServicos);

        ConfigureSqlQuery configureSqlQuery = this.getConfigureSqlQuery();

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), Integer.valueOf(1), Integer.valueOf(10000), sql.getCriteria(), configureSqlQuery).getList();
    }

    /**
     * Consulta de adicionar doctoServico pre-manifesto Terminal.
     * Esta trata apenas de gerar a consulta caso o manifesto em questao seja de viagem.
     *
     * @param idSolicitacaoRetirada
     * @param idRotaColetaEntrega
     * @param idFilialDestino
     * @param idConsignatario
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param tpDoctoServico
     * @param idFilialOrigemDoctoServico
     * @param idFilialDestinoDoctoServico
     * @param idDoctoServico
     * @param idAwb
     * @param idFilialOrigemManifesto
     * @param tpManifesto
     * @param tpAbrangencia
     * @param tpPreManifesto
     * @param idsDoctoServicos
     * @param soma
     * @return
     */
    public List findAdicionarDoctoServicoViagem(
            Long idFilialDestino,
            Long idClienteRemetente,
            Long idClienteDestinatario,
            String tpDoctoServico,
            Long idFilialOrigemDoctoServico,
            Long idFilialDestinoDoctoServico,
            Long idDoctoServico,
            Long idAwb,
            String tpAbrangencia,
            String tpPreManifesto,
            List<Long> idsDoctoServicos,
            List<Long> idsLocalizacao) {
        return this.findAdicionarDoctoServicoViagem(idFilialDestino,
                idClienteRemetente,
                idClienteDestinatario,
                tpDoctoServico,
                idFilialOrigemDoctoServico,
                idFilialDestinoDoctoServico,
                idDoctoServico,
                idAwb,
                tpAbrangencia,
                tpPreManifesto,
                idsDoctoServicos,
                idsLocalizacao,
                null,
                null,
                null,
                "S");
    }

    /**
     * Consulta de adicionar doctoServico pre-manifesto Terminal.
     * Esta trata apenas de gerar a consulta caso o manifesto em questao seja de viagem.
     *
     * @param idFilialDestino
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param tpDoctoServico
     * @param idFilialOrigemDoctoServico
     * @param idFilialDestinoDoctoServico
     * @param idDoctoServico
     * @param idAwb
     * @param tpAbrangencia
     * @param tpPreManifesto
     * @param idsDoctoServicos
     * @param idsLocalizacao
     * @param idFilialLocalizacao
     * @return
     */
    public List findAdicionarDoctoServicoViagem(
            Long idFilialDestino,
            Long idClienteRemetente,
            Long idClienteDestinatario,
            String tpDoctoServico,
            Long idFilialOrigemDoctoServico,
            Long idFilialDestinoDoctoServico,
            Long idDoctoServico,
            Long idAwb,
            String tpAbrangencia,
            String tpPreManifesto,
            List<Long> idsDoctoServicos,
            List<Long> idsLocalizacao,
            Long idFilialLocalizacao,
            Long idPedidoColeta,
            String dsBox,
            String bloqFluxoSubcontratacao) {

        //Caso a consulta seja para a soma das informacoes contidas na grid...
        boolean hasBox = dsBox != null;

        SqlTemplate sql = getHqlAdicionarDoctoServico(hasBox);

        sql.addCustomCriteria("localizacaoMercadoria.id_localizacao_mercadoria in " + SQLUtils.mountNumberForInExpression(idsLocalizacao));
        if (idFilialLocalizacao != null) {
            sql.addCustomCriteria("doctoServico.ID_FILIAL_LOCALIZACAO = " + idFilialLocalizacao);
        }

        if (idPedidoColeta != null) {
            sql.addCustomCriteria("doctoServico.ID_PEDIDO_COLETA = " + idPedidoColeta);
        }

        if (hasBox) {
            sql.addCustomCriteria("dadosComplemento.DS_VALOR_CAMPO = '" + dsBox.toString() + "'");
        }

        sql.addCriteria("doctoServico.bl_bloqueado", "=", "N");

        if("S".equalsIgnoreCase(bloqFluxoSubcontratacao)){
            sql.addCustomCriteria("(doctoServico.BL_FLUXO_SUBCONTRATACAO = 'N' or doctoServico.BL_FLUXO_SUBCONTRATACAO is null) ");
        }
        
        StringBuilder strFluxoFilial = new StringBuilder()
                .append("exists (select 1 from ")
                .append("Ordem_Filial_Fluxo offo, ")
                .append("Ordem_Filial_Fluxo offd ")
                .append("where offo.id_fluxo_Filial = offd.id_fluxo_Filial ")
                .append("and offd.nr_Ordem > offo.nr_Ordem ")
                .append("and offo.id_filial = ? ");
        if (idPedidoColeta == null && !hasBox) {
            strFluxoFilial.append("and offd.id_filial = ? ");
        }
        strFluxoFilial.append("and offo.id_fluxo_Filial = doctoServico.id_fluxo_filial) ");

        sql.addCustomCriteria(strFluxoFilial.toString());
        sql.addCriteriaValue(SessionUtils.getFilialSessao().getIdFilial());
        if (idPedidoColeta == null && !hasBox) {
            sql.addCriteriaValue(idFilialDestino);
        }

        if (tpPreManifesto != null) {
            if ("VI".equals(tpPreManifesto) && ("I".equals(tpAbrangencia))) {
                sql.addCriteria("doctoServico.tp_documento_servico", "=", "CRT");

                StringBuilder subQueryConhecimentoInt = new StringBuilder();
                subQueryConhecimentoInt.append("select cto_Internacional.id_cto_Internacional  ");
                subQueryConhecimentoInt.append("from cto_internacional ");
                subQueryConhecimentoInt.append("where cto_internacional.tp_situacao_crt = ? and cto_internacional.id = doctoServico.id_docto_servico");

                sql.addCustomCriteria("( not exists ( " + subQueryConhecimentoInt.toString() + " ))");
                sql.addCriteriaValue("C");

            } else if ("VI".equals(tpPreManifesto) && ("N".equals(tpAbrangencia))) {
                sql.addCustomCriteria("doctoServico.tp_documento_servico in ('CTR','MDA','CTE','NTE')");

                StringBuilder subQueryConhecimento = new StringBuilder();
                subQueryConhecimento.append("select conhecimento.id_conhecimento ");
                subQueryConhecimento.append("from conhecimento ");
                subQueryConhecimento.append("where conhecimento.tp_situacao_conhecimento = ? and conhecimento.id_conhecimento = doctoServico.id_docto_servico");

                StringBuilder subQueryMDA = new StringBuilder();
                subQueryMDA.append("select mda.id_mda ");
                subQueryMDA.append("from mda ");
                subQueryMDA.append("where mda.tp_status_mda = ? and mda.id_mda = doctoServico.id_docto_servico");

                sql.addCustomCriteria("( not exists (" + subQueryConhecimento.toString() + ") and not exists (" + subQueryMDA.toString() + "))");
                sql.addCriteriaValue("C");
                sql.addCriteriaValue("C");
            }
        }

        this.findAdicionarDoctoServicoBasicFilters(
                sql,
                idClienteRemetente,
                idClienteDestinatario,
                tpDoctoServico,
                idFilialOrigemDoctoServico,
                idFilialDestinoDoctoServico,
                idDoctoServico,
                idAwb,
                idsDoctoServicos);

        sql.addOrderBy("filialOrigem.sg_Filial");
        sql.addOrderBy("doctoServico.nr_Docto_Servico");

        ConfigureSqlQuery configureSqlQuery = this.getConfigureSqlQuery();
        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), IntegerUtils.ONE, Integer.valueOf(10000), sql.getCriteria(), configureSqlQuery).getList();
    }

    /**
     * Insercao de filtros basicos para a consulta.
     *
     * @param sql
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param tpDoctoServico
     * @param idFilialOrigemDoctoServico
     * @param idFilialDestinoDoctoServico
     * @param idDoctoServico
     * @param idAwb
     * @param idsDoctoServicos
     */
    private void findAdicionarDoctoServicoBasicFilters(
            SqlTemplate sql,
            Long idClienteRemetente,
            Long idClienteDestinatario,
            String tpDoctoServico,
            Long idFilialOrigemDoctoServico,
            Long idFilialDestinoDoctoServico,
            Long idDoctoServico,
            Long idAwb,
            List<Long> idsDoctoServicos) {

        sql.addCustomCriteria("( solicitacaoRetirada.id_solicitacao_retirada is null " +
                " or ((solicitacaoRetirada.id_filial_retirada <> filialLocalizacao.id_filial) " +
                " or ((solicitacaoRetirada.id_filial_retirada = filialLocalizacao.id_filial) and (solicitacaoRetirada.tp_situacao <> 'A'))) " +
                " and solicitacaoRetirada.id_solicitacao_retirada = (" + subDoctoServicoRetirada() + "))");

        sql.addCriteria("clienteRemetente.id_cliente", "=", idClienteRemetente);

        /** LMS-1248 Conhecimento emitido na filial e manifestado por outra filial */
        sql.addCriteria("filialLocalizacao.id_filial", "=", SessionUtils.getFilialSessao().getIdFilial());

        sql.addCriteria("clienteDestinatario.id_cliente", "=", idClienteDestinatario);

        if (idAwb != null) {
            sql.addFrom("cto_awb", "ctoAwb");
            sql.addFrom("awb");

            sql.addCustomCriteria("doctoServico.id_docto_servico = ctoAwb.id_conhecimento");
            sql.addCustomCriteria("ctoAwb.id_awb = awb.id_awb");
            sql.addCriteria("awb.id_awb", "=", idAwb);
        }

        if (tpDoctoServico != null && !"".equals(tpDoctoServico)) {
            sql.addCriteria("doctoServico.tp_documento_servico", "=", tpDoctoServico);
        }

        sql.addCriteria("filialOrigem.id_filial", "=", idFilialOrigemDoctoServico);
        sql.addCriteria("filialDestino.id_filial", "=", idFilialDestinoDoctoServico);
        sql.addCriteria("doctoServico.id_docto_servico", "=", idDoctoServico);

        if (!idsDoctoServicos.isEmpty()) {
            String ids = "";
            for (Iterator<Long> iter = idsDoctoServicos.iterator(); iter.hasNext(); ) {
                Long id = (Long) iter.next();
                ids += id.toString();
                if (iter.hasNext()) {
                    ids += ", ";
                }
            }
            sql.addCustomCriteria("doctoServico.id_docto_servico not in (" + ids + ")");
        }
        sql.addCriteria("doctoServico.nr_docto_servico", ">", 0);
    }

    /**
     * Gera a subConsulta para a consulta de AdicionarDoctoServicoEntrega.
     *
     * @param idSolicitacaoRetirada
     * @return
     */
    private String subQueryAdicionarDoctoServicoEntregaClienteRetira() {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("doctoServicoRetirada.id_docto_servico", "idDoctoServico");
        hql.addFrom("documento_Servico_Retirada", "doctoServicoRetirada");
        hql.addCustomCriteria("doctoServicoRetirada.id_solicitacao_Retirada = ?");

        return hql.getSql();
    }

    /**
     * @param idFilialOrigemManifesto
     * @param idFilialDestinoManifesto
     * @return
     */
    private String subDoctoServicoRetirada() {

        SqlTemplate sql = new SqlTemplate();
        sql.addProjection("max(sr.id_solicitacao_retirada)");

        sql.addFrom("documento_servico_retirada", "dsr");
        sql.addFrom("solicitacao_retirada", "sr");

        sql.addCustomCriteria("dsr.id_solicitacao_retirada = sr.id_solicitacao_retirada");
        sql.addCustomCriteria("dsr.id_docto_servico = doctoServico.id_docto_servico");

        return sql.getSql();
    }

    /**
     * Retorna uma SqlTemplate contendo a sql basica para a consulta de terminais da
     * tela de addicionar doctoServico pre-manifesto.
     * <p>
     * Incluido parametro "tipo" para que ser utilizado tamb?m para retornar os documentos servi?o que n?o est?o
     * mais ma filial, por?m ainda possuem volumes na filial
     *
     * @return SqlTemplate
     */
    private SqlTemplate getHqlAdicionarDoctoServico(boolean hasBox) {

        SqlTemplate sql = new SqlTemplate();

        StringBuilder projection = new StringBuilder();
        projection
                .append("doctoServico.id_Docto_Servico as idDoctoServico, ") //0
                .append("doctoServico.tp_Documento_Servico as tpDocumentoServico, ") //1
                .append("doctoServico.nr_Docto_Servico as nrDoctoServico, ") //2
                .append("(case when (doctoServico.tp_Documento_Servico = '" + ConstantesExpedicao.CONHECIMENTO_NACIONAL + "') then " +
                        "(select conhecimento.dv_conhecimento " +
                        "from conhecimento " +
                        "where conhecimento.id_conhecimento = doctoServico.id_Docto_Servico) " +
                        "else null end) as dvDoctoServico, ") //3
                .append("doctoServico.qt_Volumes as qtVolumes, ") //4
                .append("doctoServico.ps_Real as psReal, ") //5
                .append("doctoServico.vl_Mercadoria as vlMercadoria, ") //6
                .append("doctoServico.dt_Prev_Entrega as dtPrevEntrega, ") //7
                .append("doctoServico.dh_Emissao as dhEmissao, ") //8
                .append("doctoServico.bl_Bloqueado as blBloqueado, ") //9
                .append("filialOrigem.id_Filial as idFilialOrigem, ") //10
                .append("filialOrigem.sg_Filial as sgFilialOrigem, ") //11
                .append("pessoaFilialOrigem.nm_Fantasia as nmFantasiaFilialOrigem, ") //12
                .append("filialDestino.id_Filial as idFilialDestino, ") //13
                .append("filialDestino.sg_Filial as sgFilialDestino, ") //14
                .append("pessoaFilialDestino.nm_Fantasia as nmFantasiaFilialDestino, ") //15
                .append("moeda.id_Moeda as idMoeda, ") //16
                .append("moeda.sg_Moeda as sgMoeda, ") //17
                .append("moeda.ds_Simbolo as dsSimbolo, ") //18
                .append("servico.id_Servico as idServico, ") //19
                .append("servico.sg_Servico as sgServico, ") //20
                .append("servico.tp_Modal as tpModal, ") //21
                .append("servico.tp_Abrangencia as tpAbrangencia, ") //22
                .append("tipoServico.bl_Priorizar as blPriorizar, ") //23
                .append("clienteRemetente.id_Cliente as idClienteRemetente, ") //24
                .append("pessoaClienteRemetente.nm_Pessoa as nmPessoaClienteRemetente, ") //25
                .append("clienteRemetente.bl_Agendamento_Pessoa_Fisica as blAgendamentoPessoaFisica, ") //26
                .append("clienteRemetente.bl_Agendamento_Pessoa_Juridica as blAgendamentoPessoaJuridica, ") //27
                .append("clienteDestinatario.id_Cliente as idClienteDestinatario, ") //28
                .append("pessoaClienteDestinatario.nm_Pessoa as nmPessoaClienteDestinatario, ") //29
                .append("pessoaClienteDestinatario.tp_Pessoa as tpPessoaClienteDestinatario, ") //30
                .append("clienteConsignatario.id_Cliente as idClienteConsignatario, ") //31
                .append("pessoaClienteConsignatario.nm_Pessoa as nmPessoaClienteConsignatario, ") //32
                .append("pessoaClienteConsignatario.tp_Pessoa as tpPessoaClienteConsignatario, ") //33
                .append("rotaColetaEntrega.id_Rota_Coleta_Entrega as idRotaColetaEntrega, ") //34
                .append("paisOrigem.id_Pais as idPaisOrigem, ") //35
                .append("(select max(eventoDocumentoServico.dh_evento) ")
                .append("from evento_documento_servico eventoDocumentoServico ")
                .append("inner join evento ev on (eventoDocumentoServico.id_evento = ev.id_evento) ")
                .append("inner join localizacao_Mercadoria lm on (ev.id_localizacao_mercadoria = lm.id_localizacao_mercadoria) ")
                .append("where eventoDocumentoServico.id_docto_servico = doctoservico.id_docto_servico ")
                .append("and lm.cd_Localizacao_Mercadoria = 24 ")
                .append(") as dhEvento"); //36

        sql.addProjection(projection.toString());

        sql.addFrom("docto_servico", "doctoServico");
        sql.addFrom("filial", "filialOrigem");
        sql.addFrom("pessoa", "pessoaFilialOrigem");
        sql.addFrom("filial", "filialDestino");
        sql.addFrom("pessoa", "pessoaFilialDestino");
        sql.addFrom("localizacao_Mercadoria", "localizacaoMercadoria");
        sql.addFrom("filial", "filialLocalizacao");
        sql.addFrom("moeda", "moeda");
        sql.addFrom("servico");
        sql.addFrom("tipo_servico", "tipoServico");
        sql.addFrom("cliente", "clienteRemetente");
        sql.addFrom("pessoa", "pessoaClienteRemetente");
        sql.addFrom("cliente", "clienteDestinatario");
        sql.addFrom("pessoa", "pessoaClienteDestinatario");
        sql.addFrom("cliente", "clienteConsignatario");
        sql.addFrom("pessoa", "pessoaClienteConsignatario");
        sql.addFrom("rota_Coleta_Entrega", "rotaColetaEntrega");
        sql.addFrom("pais", "paisOrigem");
        sql.addFrom("documento_servico_retirada", "documentoServicoRetirada");
        sql.addFrom("solicitacao_retirada", "solicitacaoRetirada");
        if (hasBox) {
            sql.addFrom("dados_complemento", "dadosComplemento");
            sql.addFrom("informacao_docto_cliente", "informacaoDoctoCliente");
        }

        sql.addCustomCriteria("doctoServico.id_pais = paisOrigem.id_pais(+)");
        sql.addCustomCriteria("doctoServico.id_rota_coleta_entrega_real = rotaColetaEntrega.id_rota_coleta_entrega(+)");
        sql.addCustomCriteria("clienteConsignatario.id_cliente = pessoaClienteConsignatario.id_pessoa(+)");
        sql.addCustomCriteria("doctoServico.id_cliente_consignatario = clienteConsignatario.id_cliente(+)");
        sql.addCustomCriteria("clienteDestinatario.id_cliente = pessoaClienteDestinatario.id_pessoa(+)");
        sql.addCustomCriteria("doctoServico.id_cliente_destinatario = clienteDestinatario.id_cliente(+)");
        sql.addCustomCriteria("clienteRemetente.id_cliente = pessoaClienteRemetente.id_pessoa(+)");
        sql.addCustomCriteria("doctoServico.id_cliente_remetente = clienteRemetente.id_cliente(+)");
        sql.addCustomCriteria("doctoServico.id_docto_servico = documentoServicoRetirada.id_docto_servico(+)");
        sql.addCustomCriteria("documentoServicoRetirada.id_solicitacao_retirada = solicitacaoRetirada.id_solicitacao_retirada(+)");
        sql.addCustomCriteria("servico.id_tipo_servico = tipoServico.id_tipo_servico");
        sql.addCustomCriteria("doctoServico.id_servico = servico.id_servico(+)");
        sql.addCustomCriteria("doctoServico.id_moeda = moeda.id_moeda(+)");
        sql.addCustomCriteria("doctoServico.id_localizacao_mercadoria = localizacaoMercadoria.id_localizacao_mercadoria");
        sql.addCustomCriteria("doctoServico.id_filial_localizacao = filialLocalizacao.id_filial");
        sql.addCustomCriteria("doctoServico.id_filial_destino = filialDestino.id_filial");
        sql.addCustomCriteria("filialDestino.id_filial = pessoaFilialDestino.id_pessoa");
        sql.addCustomCriteria("doctoServico.id_filial_origem = filialOrigem.id_filial");
        sql.addCustomCriteria("filialOrigem.id_filial = pessoaFilialOrigem.id_pessoa");
        if (hasBox) {
            sql.addCustomCriteria("dadosComplemento.id_conhecimento = doctoServico.id_docto_servico");
            sql.addCustomCriteria("dadosComplemento.id_informacao_docto_cliente = informacaoDoctoCliente.id_informacao_docto_cliente");
            sql.addCustomCriteria("clienteRemetente.Id_Indc_Proc_Especial = dadosComplemento.Id_Informacao_Docto_Cliente");
        }

        return sql;
    }

    /**
     * Gera o <code>ConfigureSqlQuery</code> de acordo com a consulta.
     *
     * @return
     */
    private ConfigureSqlQuery getConfigureSqlQuery() {
        ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("dvDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("qtVolumes", Hibernate.INTEGER);
                sqlQuery.addScalar("psReal", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("dtPrevEntrega", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
                sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
                sqlQuery.addScalar("blBloqueado", Hibernate.custom(SimNaoType.class));
                sqlQuery.addScalar("idFilialOrigem", Hibernate.LONG);
                sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
                sqlQuery.addScalar("nmFantasiaFilialOrigem", Hibernate.STRING);
                sqlQuery.addScalar("idFilialDestino", Hibernate.LONG);
                sqlQuery.addScalar("sgFilialDestino", Hibernate.STRING);
                sqlQuery.addScalar("nmFantasiaFilialDestino", Hibernate.STRING);
                sqlQuery.addScalar("idMoeda", Hibernate.LONG);
                sqlQuery.addScalar("sgMoeda", Hibernate.STRING);
                sqlQuery.addScalar("dsSimbolo", Hibernate.STRING);
                sqlQuery.addScalar("idServico", Hibernate.LONG);
                sqlQuery.addScalar("sgServico", Hibernate.STRING);
                sqlQuery.addScalar("tpModal", Hibernate.STRING);
                sqlQuery.addScalar("tpAbrangencia", Hibernate.STRING);
                sqlQuery.addScalar("blPriorizar", Hibernate.custom(SimNaoType.class));
                sqlQuery.addScalar("idClienteRemetente", Hibernate.LONG);
                sqlQuery.addScalar("nmPessoaClienteRemetente", Hibernate.STRING);
                sqlQuery.addScalar("blAgendamentoPessoaFisica", Hibernate.custom(SimNaoType.class));
                sqlQuery.addScalar("blAgendamentoPessoaJuridica", Hibernate.custom(SimNaoType.class));
                sqlQuery.addScalar("idClienteDestinatario", Hibernate.LONG);
                sqlQuery.addScalar("nmPessoaClienteDestinatario", Hibernate.STRING);
                sqlQuery.addScalar("tpPessoaClienteDestinatario", Hibernate.STRING);
                sqlQuery.addScalar("idClienteConsignatario", Hibernate.LONG);
                sqlQuery.addScalar("nmPessoaClienteConsignatario", Hibernate.STRING);
                sqlQuery.addScalar("tpPessoaClienteConsignatario", Hibernate.STRING);
                sqlQuery.addScalar("idRotaColetaEntrega", Hibernate.LONG);
                sqlQuery.addScalar("idPaisOrigem", Hibernate.LONG);
                sqlQuery.addScalar("dhEvento", Hibernate.custom(JodaTimeDateTimeUserType.class));
            }
        };

        return configureSqlQuery;
    }

    /**
     * Retorna uma lista de mapas com as informa??es dos Documentos de Servi?o.
     *
     * @param idControleCarga
     * @return
     */
    public List<Map<String, Object>> findDoctoServicoByIdControleCarga(Long idControleCarga) {
        StringBuilder sql = new StringBuilder()
                .append("select new map(ds.idDoctoServico as idDoctoServico, ")
                .append("m.nrPreManifesto as nrPreManifesto, ")
                .append("m.tpManifesto as tpManifesto, ")
                .append("m.tpStatusManifesto as tpStatusManifesto, ")
                .append("filialOrigemManifesto.sgFilial as sgFilialOrigemManifesto ")
                .append(")")
                .append(" from ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("inner join ds.preManifestoDocumentos pmd ")
                .append("inner join pmd.manifesto m ")
                .append("inner join m.filialByIdFilialOrigem filialOrigemManifesto ")
                .append("where ")
                .append("m.controleCarga.id = ? ");

        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
    }

    /**
     * Retorna uma lista de Documentos de Servico a partir de um Controle de Carga.
     *
     * @param idControleCarga
     * @return
     */
    public List<DoctoServico> findDoctosServicoByIdControleCarga(Long idControleCarga) {
        StringBuilder sql = new StringBuilder()
                .append("select ds ")
                .append(" from ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("inner join ds.preManifestoDocumentos pmd ")
                .append("inner join pmd.manifesto m ")
                .append("inner join m.filialByIdFilialOrigem filialOrigemManifesto ")
                .append("where ")
                .append("m.controleCarga.id = ? ");
        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
    }

    /**
     * Retorna uma lista de Documentos de Servico a partir de um Controle de Carga de Coleta gerado automaticamente para parceira.
     *
     * @param idControleCarga
     * @return
     */
    public List<DoctoServico> findDoctosServicoByIdControleCargaColetaParceira(Long idControleCarga) {
        StringBuilder sql = new StringBuilder()
                .append("select ds ")
                .append(" from ")
                .append(DetalheColeta.class.getName()).append(" as dc ")
                .append("inner join dc.doctoServico ds ")
                .append("inner join dc.pedidoColeta pc ")
                .append("inner join pc.manifestoColeta mc ")
                .append("inner join mc.controleCarga cc ")
                .append(" where ")
                .append(" cc.idControleCarga = ? ");

        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
    }


    /**
     * Carrega doctoServico's de acordo com os idsDevedorDocServFat
     * que exitam no Lms e n?o vieram no "movimento da integra??o".
     *
     * @param idsDevedores
     * @return
     * @author Hector Julian Esnaola Junior
     * @since 26/09/2007
     */
    public DetachedCriteria findDoctoServicoDivergenteMovimentoLms(List<Long> idsDevedores) {


        DetachedCriteria dc = DetachedCriteria.forClass(DoctoServico.class, "ds")
                .createAlias("ds.devedorDocServFats", "ddsf")
                .createAlias("ddsf.descontos", "d")
                .createAlias("d.reciboDesconto", "rd")
                .createAlias("d.demonstrativoDesconto", "dd")
                .add(Restrictions.not(Restrictions.in("ddsf.id", idsDevedores)));

        return dc;

    }

    /**
     * Carrega doctoServico's de acordo com o idReciboDesconto
     * que exitam no Lms e n?o vieram no "movimento da integra??o".
     *
     * @param idsDevedores
     * @return
     * @author Hector Julian Esnaola Junior
     * @since 26/09/2007
     */
    public List<DoctoServico> findDoctoServicoDivergenteMovimentoLmsByReciboDesconto(
            List<Long> idsDevedores,
            Long idReciboDesconto
    ) {
        DetachedCriteria dc = findDoctoServicoDivergenteMovimentoLms(idsDevedores);
        dc.add(Restrictions.eq("rd.id", idReciboDesconto));

        return findByDetachedCriteria(dc);

    }

    /**
     * Carrega doctoServico's de acordo com o idDemonstrativoDesconto
     * que exitam no Lms e n?o vieram no "movimento da integra??o".
     *
     * @param idsDevedores
     * @return
     * @author Hector Julian Esnaola Junior
     * @since 26/09/2007
     */
    public List<DoctoServico> findDoctoServicoDivergenteMovimentoLmsByDemonstrativoDesconto(
            List<Long> idsDevedores,
            Long idDemonstrativoDesconto
    ) {

        DetachedCriteria dc = findDoctoServicoDivergenteMovimentoLms(idsDevedores);
        dc.add(Restrictions.eq("dd.id", ""));

        return findByDetachedCriteria(dc);
    }

    /**
     * Verifica se o documento possui um agendamento.
     *
     * @param idDoctoServico
     * @return
     */
    public List<DoctoServico> findDoctoServicoWithAgendamento(Long idDoctoServico) {
        StringBuilder sql = new StringBuilder()
                .append("select ds ")
                .append("from ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("inner join ds.agendamentoDoctoServicos ads ")
                .append("inner join ads.agendamentoEntrega ae ")
                .append("where ")
                .append("ds.id = ? ")
                .append("and ae.tpSituacaoAgendamento = 'A'");

        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico});
    }


    public List<DoctoServico> findDocSemFluxoFilial(Long idUsuario) {

        ProjectionList projectionList = Projections.projectionList()
                .add(Projections.property("doc.idDoctoServico"), "idDoctoServico")
                .add(Projections.property("doc.servico.id"), "servico.idServico")
                .add(Projections.property("doc.filialByIdFilialOrigem.id"), "filialByIdFilialOrigem.idFilial")
                .add(Projections.property("doc.filialByIdFilialDestino.id"), "filialByIdFilialDestino.idFilial");

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(DoctoServico.class, "doc")
                .setProjection(projectionList)
                .add(Restrictions.isNull("doc.fluxoFilial"))
                .add(Restrictions.isNotNull("doc.filialByIdFilialOrigem.id"))
                .add(Restrictions.isNotNull("doc.filialByIdFilialDestino.id"))
                .add(Restrictions.eq("doc.usuarioByIdUsuarioInclusao.id", idUsuario))
                .setResultTransformer(new AliasToNestedBeanResultTransformer(DoctoServico.class));

        return getHibernateTemplate().findByCriteria(detachedCriteria, 0, 2000);

    }


    public void executeUpdateFluxoDoc(DoctoServico doctoServico) {

        StringBuilder hql = new StringBuilder()
                .append(" update DoctoServico as doc set doc.fluxoFilial.id = ? ")
                .append(" where ")
                .append(" doc.id = ? ");

        List<Long> params = new ArrayList<Long>();
        params.add(doctoServico.getFluxoFilial().getIdFluxoFilial());
        params.add(doctoServico.getIdDoctoServico());

        super.executeHql(hql.toString(), params);
    }

    public void executeAtualizarObComplementoLocalizacao(Long idConhecimento, String obComplementoLocalizacao) {
        getAdsmHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder sql = new StringBuilder();
                sql.append(" update docto_servico ");
                sql.append(" set ob_complemento_localizacao = :ob_complemento_localizacao ");
                sql.append(" where id_docto_servico = :id_docto_servico ");

                SQLQuery query = session.createSQLQuery(sql.toString());
                query.setString("ob_complemento_localizacao", obComplementoLocalizacao);
                query.setLong("id_docto_servico", idConhecimento);
                return query.executeUpdate();
            }
        });
    }

    public DoctoServico findCdLocalizacaoByParams(Long idFilial, Long nrDoctoServico, String tpDoctoServico) {

        StringBuilder sql = new StringBuilder();
        sql.append("select ds ");
        sql.append("from " + DoctoServico.class.getName() + "	ds	");
        sql.append("inner join fetch ds.localizacaoMercadoria 	lm	");
        sql.append("inner join ds.filialByIdFilialOrigem 		fo	");
        sql.append("where fo.idFilial		= ?	and					");
        sql.append("ds.nrDoctoServico 		= ?	and					");
        sql.append("ds.tpDocumentoServico 	= ?						");

        return (DoctoServico) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idFilial, nrDoctoServico, tpDoctoServico});

    }

    public List<DoctoServico> findDoctoServicoByIdManifestoAndTipoManifesto(Long idManifesto, String tpManifesto) {

        StringBuilder sb = new StringBuilder();
        sb.append("select ds ")
                .append("from " + getPersistentClass().getName() + " ds ")
                .append("inner join ds.manifestoEntregaDocumentos    med ")
                .append("inner join med.manifestoEntrega             me ")
                .append("inner join me.manifesto                     m ")
                .append("where m.id = ? ")
                .append("      and m.tpManifesto = ? ");

        return getAdsmHibernateTemplate().find(sb.toString(), new Object[]{idManifesto, tpManifesto});
    }


    public boolean findLocalizacaoMercadoriaByidDoctoServicoCdLocalizacao(Long idDoctoServico, Short cdLocalizacaoMercadoria) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("locMerc");
        hql.addFrom("DoctoServico dc " +
                "left outer join dc.eventoDocumentoServicos eventoDoc " +
                "join eventoDoc.evento evento " +
                "left outer join evento.localizacaoMercadoria locMerc ");

        hql.addCriteria("dc.idDoctoServico", "=", idDoctoServico);
        hql.addCriteria("locMerc.cdLocalizacaoMercadoria", "=", cdLocalizacaoMercadoria);

        return !getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()).isEmpty();
    }

    public List<String> findXObsCTE(Long idDoctoServico) {
        List<String> retorno;
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT (ids.dsCampo || ':' || dc.dsValorCampo) as DS_OBSERVACAO_DOCTO_SERVICO ")
                .append("   FROM DadosComplemento dc, ")
                .append("               InformacaoDocServico ids ")
                .append("         WHERE dc.informacaoDocServico.idInformacaoDocServico = ids.idInformacaoDocServico ")
                .append("         AND dc.conhecimento.idDoctoServico = " + idDoctoServico + " ")
                .append("         AND ids.blImprimeConhecimento='S' ");
        retorno = getAdsmHibernateTemplate().find(sb.toString());

        sb = new StringBuilder();
        sb.append("   SELECT (idc.dsCampo || ':' || dc.dsValorCampo) as DS_OBSERVACAO_DOCTO_SERVICO ")
                .append("         FROM DadosComplemento dc, ")
                .append("               InformacaoDoctoCliente idc ")
                .append("         WHERE dc.informacaoDoctoCliente.idInformacaoDoctoCliente = idc.idInformacaoDoctoCliente ")
                .append("         AND dc.conhecimento.idDoctoServico = " + idDoctoServico + " ")
                .append("         AND idc.blImprimeConhecimento='S' ");
        retorno.addAll(getAdsmHibernateTemplate().find(sb.toString()));

        return retorno;
    }

    public boolean validateDoctoServicoByIdControleCarga(Long idControleCarga) {

        DetachedCriteria subquery = DetachedCriteria.forClass(NotaCredito.class, "nc")
                .createCriteria("notaCreditoDoctos", "ncd")
                .createCriteria("doctoServico", "docto")
                .setProjection(Projections.projectionList().add(Projections.property("docto.idDoctoServico")));

        Criteria c = getSession().createCriteria(DoctoServico.class, "ds")
                .createCriteria("ds.manifestoEntregaDocumentos", "med")
                .createCriteria("med.ocorrenciaEntrega", "oc")
                .createCriteria("oc.evento", "ev")
                .createCriteria("ev.localizacaoMercadoria", "lm")
                .createCriteria("med.manifestoEntrega", "me")
                .createCriteria("me.manifesto", "m")
                .createCriteria("m.controleCarga", "cc")
                .add(Restrictions.eq("cc.idControleCarga", idControleCarga))
                .add(Subqueries.propertyNotIn("ds.idDoctoServico", subquery));

        return !c.list().isEmpty();
    }

    public boolean validateDoctoServicoSemOcorrenciaEntrega(Long idControleCarga) {

        Criteria c = getSession().createCriteria(DoctoServico.class, "ds")
                .createCriteria("manifestoEntregaDocumentos", "med")
                .createCriteria("manifestoEntrega", "me")
                .createCriteria("manifesto", "m")
                .createCriteria("controleCarga", "cc")

                .add(Restrictions.isNull("med.ocorrenciaEntrega.id"))
                .add(Restrictions.ne("med.tpSituacaoDocumento", "CANC"))
                .add(Restrictions.eq("cc.id", idControleCarga));

        return c.list().isEmpty();
    }

    public List<Integer> validateDoctoServicoComPendenciaAprovacao(final Long idMonitoramentoDescarga) {
        final StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ds.id_docto_servico as nrDoctoServico");
        sql.append(" FROM docto_servico ds, monitoramento_descarga md, volume_nota_fiscal vnf, nota_fiscal_conhecimento nfc, docto_servico_workflow dsw");
        sql.append(" WHERE md.id_monitoramento_descarga = :idMonitoramentoDescarga AND ");
        sql.append(" md.id_monitoramento_descarga = vnf.id_monitoramento_descarga AND ");
        sql.append(" vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento AND ");
        sql.append(" nfc.id_conhecimento = ds.id_docto_servico AND ");
        sql.append(" ds.id_docto_servico = dsw.id_docto_servico AND ");
        sql.append(" dsw.tp_situacao_aprovacao = 'E' ");

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.setLong("idMonitoramentoDescarga", idMonitoramentoDescarga);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.INTEGER);
            }
        };
        HibernateCallback hcb = findBySql(sql.toString(), null, csq);

        List<Integer> list = (List) getAdsmHibernateTemplate().execute(hcb);

        return list;
    }

    public List<Integer> validateDoctoServicoComMonitoramentoEletronicoAutorizado(final Long idMonitoramentoDescarga) {
        final StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ds.id_docto_servico as nrDoctoServico");
        sql.append(" FROM docto_servico ds, monitoramento_descarga md, volume_nota_fiscal vnf, nota_fiscal_conhecimento nfc, docto_servico_workflow dsw");
        sql.append(" WHERE md.id_monitoramento_descarga = :idMonitoramentoDescarga AND ");
        sql.append(" md.id_monitoramento_descarga = vnf.id_monitoramento_descarga AND ");
        sql.append(" vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento AND ");
        sql.append(" nfc.id_conhecimento = ds.id_docto_servico AND ");
        sql.append(" ds.id_docto_servico = dsw.id_docto_servico AND ");
        sql.append(" dsw.tp_situacao_aprovacao = 'E' ");
        sql.append(" and rownum <=1 ");

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.setLong("idMonitoramentoDescarga", idMonitoramentoDescarga);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.INTEGER);
            }
        };
        HibernateCallback hcb = findBySql(sql.toString(), null, csq);

        List<Integer> list = (List) getAdsmHibernateTemplate().execute(hcb);

        return list;
    }

    public List<Map<String, Object>> findConhecimentoByIdManifestoEntrega(Long idManifestoEntrega) {
        StringBuilder hql = new StringBuilder();
        hql.append("select new Map(fior.sgFilial as sgFilial ");
        hql.append("      ,dose.nrDoctoServico as nrDoctoServico ");
        hql.append("      ,dose.dsEnderecoEntregaReal as dsEnderecoEntregaReal ");
        hql.append("      ,dose.vlTotalParcelas as vlTotalParcelas ");
        hql.append("      ,dose.vlMercadoria as vlMercadoria ");
        hql.append("      ,dose.psReal as psReal ");
        hql.append("      ,dose.psAforado as psAforado");
        hql.append("      ,dose.psAferido as psAferido ");
        hql.append("      ,(select count(vonf) ");
        hql.append("        from VolumeNotaFiscal as vonf ");
        hql.append("        join vonf.notaFiscalConhecimento as nfco ");
        hql.append("        join nfco.conhecimento as conh ");
        hql.append("        where conh.idDoctoServico = dose.idDoctoServico ");
        hql.append("       ) as countVolumes ) ");
        hql.append("from DoctoServico dose ");
        hql.append("join dose.filialByIdFilialOrigem fior ");
        hql.append("join dose.notaCreditoDoctos as ncdo ");
        hql.append("join ncdo.manifestoEntrega as maen ");
        hql.append("where maen.idManifestoEntrega = ? ");

        return (List<Map<String, Object>>) getAdsmHibernateTemplate().find(hql.toString(), Arrays.asList(idManifestoEntrega).toArray());
    }

    public List<DoctoServicoSaltoDMN> findSaltosNrCte(final Date startDhEmissao, final Date endDhEmissao) {

        List<DoctoServicoSaltoDMN> doctos = new ArrayList<DoctoServicoSaltoDMN>();

        //SQL que busca o intervalo das datas
        final StringBuilder buscarIntervaloSaltosNrCteSQL = new StringBuilder();
        buscarIntervaloSaltosNrCteSQL.append("SELECT ");
        buscarIntervaloSaltosNrCteSQL.append("MIN(NR_DOCTO_SERVICO) max,");
        buscarIntervaloSaltosNrCteSQL.append("MAX(NR_DOCTO_SERVICO) min,");
        buscarIntervaloSaltosNrCteSQL.append("ID_FILIAL_ORIGEM fil ");
        buscarIntervaloSaltosNrCteSQL.append("FROM DOCTO_SERVICO ");
        buscarIntervaloSaltosNrCteSQL.append(WHERE);
        buscarIntervaloSaltosNrCteSQL.append("DH_INCLUSAO BETWEEN ");
        buscarIntervaloSaltosNrCteSQL.append("'" + new DateTime(startDhEmissao).toString("dd/MM/yyyy HH:mm:ss ZZ") + "' AND ");
        buscarIntervaloSaltosNrCteSQL.append("'" + new DateTime(endDhEmissao).toString("dd/MM/yyyy HH:mm:ss ZZ") + "' AND ");
        buscarIntervaloSaltosNrCteSQL.append("TP_DOCUMENTO_SERVICO = 'CTE' AND NR_DOCTO_SERVICO > 0 ");
        buscarIntervaloSaltosNrCteSQL.append("GROUP BY ID_FILIAL_ORIGEM ");

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("max", Hibernate.LONG);
                sqlQuery.addScalar("min", Hibernate.LONG);
                sqlQuery.addScalar("fil", Hibernate.LONG);
            }
        };
        HibernateCallback hcb = findBySql(buscarIntervaloSaltosNrCteSQL.toString(), null, csq);

        List<Object[]> list = (List) getAdsmHibernateTemplate().execute(hcb);

        for (Object[] obj : list) {
            final long ini = (Long) obj[0];
            final long fim = (Long) obj[1];
            final long filial = (Long) obj[2];

            StringBuilder buscarSaltosNrCteSQL = new StringBuilder();
            buscarSaltosNrCteSQL.append("WITH FndDoctoServicoSltCte(nrDoctoServico) AS (");
            buscarSaltosNrCteSQL.append("SELECT "+ini+" FROM DUAL ");
            buscarSaltosNrCteSQL.append("UNION ALL ");
            buscarSaltosNrCteSQL.append("SELECT nrDoctoServico + 1 FROM FndDoctoServicoSltCte ");
            buscarSaltosNrCteSQL.append("WHERE nrDoctoServico < "+fim+") ");
            buscarSaltosNrCteSQL.append("SELECT * FROM FndDoctoServicoSltCte ");
            buscarSaltosNrCteSQL.append("WHERE nrDoctoServico NOT IN (");
            buscarSaltosNrCteSQL.append("SELECT NR_DOCTO_SERVICO ");
            buscarSaltosNrCteSQL.append("FROM DOCTO_SERVICO ");
            buscarSaltosNrCteSQL.append("WHERE ");
            buscarSaltosNrCteSQL.append("TP_DOCUMENTO_SERVICO = 'CTE' AND ");
            buscarSaltosNrCteSQL.append("NR_DOCTO_SERVICO > 0 AND ");
            buscarSaltosNrCteSQL.append("ID_FILIAL_ORIGEM = "+filial+") ");

            ConfigureSqlQuery buscarSaltosNrCteCsq = new ConfigureSqlQuery() {
                @Override
                public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                }
            };

            HibernateCallback buscarSaltosNrCteHcb = findBySql(buscarSaltosNrCteSQL.toString(), null, buscarSaltosNrCteCsq);

            List<BigDecimal> saltosNrCteList = (List) getAdsmHibernateTemplate().execute(buscarSaltosNrCteHcb);

            for (BigDecimal saltoNrCte : saltosNrCteList) {
                DoctoServicoSaltoDMN doc = new DoctoServicoSaltoDMN();
                doc.setIdFilial(filial);
                doc.setNrDoctoServico(saltoNrCte.longValue());
                doctos.add(doc);
            }
        }

        return doctos;
    }

    public static HibernateCallback findBySql(final String sql, final Object[] parametersValues, final ConfigureSqlQuery configQuery) {

        final HibernateCallback hcb = new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);

                configQuery.configQuery(query);

                if (parametersValues != null) {
                    for (int i = 0; i < parametersValues.length; i++) {
                        if (parametersValues[i] instanceof YearMonthDay) {
                            query.setParameter(i, parametersValues[i], Hibernate.custom(JodaTimeYearMonthDayUserType.class));
                        } else {
                            query.setParameter(i, parametersValues[i]);
                        }
                    }
                }
                return query.list();
            }
        };
        return hcb;
    }

    public Long findDoctoServicoByTpDoctoByIdFilialOrigemByNrDoctoSalto(final String tpDocumento, final Long idFilial, final Long nrDoctoServico) {

        final StringBuilder sql = new StringBuilder();

        sql.append("select id_docto_servico from docto_servico ds ");
        sql.append("where ds.id_filial_origem = :idFilial ");
        sql.append("and ds.TP_DOCUMENTO_SERVICO = :tpDocumento ");
        sql.append("and ds.NR_DOCTO_SERVICO = ");
        sql.append("(select min(ds2.NR_DOCTO_SERVICO) from docto_servico ds2 ");
        sql.append("where ds2.id_filial_origem = :idFilial ");
        sql.append("and ds2.TP_DOCUMENTO_SERVICO = :tpDocumento ");
        sql.append("and ds2.NR_DOCTO_SERVICO > :nrDoctoServico) ");

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.setString("tpDocumento", tpDocumento);
                sqlQuery.setLong("idFilial", idFilial);
                sqlQuery.setLong("nrDoctoServico", nrDoctoServico);
                sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
            }
        };
        HibernateCallback hcb = findBySql(sql.toString(), null, csq);

        List<Long> list = (List) getAdsmHibernateTemplate().execute(hcb);

        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public Long findDoctoServicoByTpDoctoByIdFilialOrigemByNrDocto(String tpDocumento, Long idFilial, Long nrDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("ds.idDoctoServico");

        sql.addFrom(DoctoServico.class.getName(), "ds");

        sql.addCriteria("ds.tpDocumentoServico", "=", tpDocumento);
        sql.addCriteria("ds.filialByIdFilialOrigem.id", "=", idFilial);
        sql.addCriteria("ds.nrDoctoServico", "=", nrDoctoServico);

        return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findDoctosServicoByIdPedidoColetaLiberaEtiquetaEdi(Long idPedidoColeta) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select ");
        hql.append(" 	new Map(con.nrConhecimento as nrConhecimento, ");
        hql.append(" 			con.filialOrigem.idFilial as idFilialOrigem, ");
        hql.append(" 			con.tpDoctoServico as tpDoctoServico, ");
        hql.append(" 			con.idDoctoServico as idDoctoServico ) ");
        hql.append(" from ");
        hql.append(DoctoServico.class.getName()).append(" ds, ");
        hql.append(Conhecimento.class.getName()).append(" con ");
        hql.append(" 	join ds.pedidoColeta pc ");
        hql.append(" 	join pc.cliente cli ");
        hql.append(" where ");
        hql.append("	con.id = ds.idDoctoServico ");
        hql.append(" 	and cli.blLiberaEtiquetaEdi = 'S' ");
        hql.append(" 	and pc.id = ? ");

        return (List<Map<String, Object>>) getAdsmHibernateTemplate().find(hql.toString(), Arrays.asList(idPedidoColeta).toArray());

    }

    public List findDoctoServicoManual(Map criteria) {
        String tpDoctoServico = (String) criteria.get("tpDocumentoServico");
        Long idFilialOrigem = (Long) criteria.get("idFilialOrigem");
        Long nrDoctoServico = (Long) criteria.get("nrDoctoServico");

        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(ds.idDoctoServico", "idDoctoServico");
        hql.addProjection("ds.nrDoctoServico", "nrDoctoServico");
        hql.addProjection("ds.dvConhecimento", "dvConhecimento");
        hql.addProjection("fil.idFilial", "filialByIdFilialOrigem_idFilial");
        hql.addProjection("fil.sgFilial", "filialByIdFilialOrigem_sgFilial");
        hql.addProjection("pess.nmFantasia", "filialByIdFilialOrigem_pessoa_nmFantasia)");

        StringBuilder hqlFrom = new StringBuilder()
                .append(DoctoServico.class.getName()).append(" ds ")
                .append(" inner join ds.filialByIdFilialOrigem fil ")
                .append(" inner join fil.pessoa pess ");
        hql.addFrom(hqlFrom.toString());

        hql.addCriteria("ds.nrDoctoServico", "=", nrDoctoServico);
        hql.addCriteria("fil.id", "=", idFilialOrigem);
        hql.addCriteria("ds.tpDocumentoServico", "=", tpDoctoServico);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public List<Map<String, Object>> findConhecimentoByIdManifestoEntregaIdNotaCredito(Long idManifestoEntrega, Long idNotaCredito) {
        StringBuilder hql = new StringBuilder();
        hql.append("select new Map(fior.sgFilial as sgFilial ");
        hql.append("      ,dose.idDoctoServico as idDoctoServico ");
        hql.append("      ,dose.nrDoctoServico as nrDoctoServico ");
        hql.append("	  ,dose.enderecoPessoa.id as idEnderecoDoctoServico ");
        hql.append("      ,dose.dsEnderecoEntregaReal as dsEnderecoEntregaReal ");
        hql.append("      ,dose.vlTotalParcelas as vlTotalParcelas ");
        hql.append("      ,dose.vlMercadoria as vlMercadoria ");
        hql.append("      ,dose.psReal as psReal ");
        hql.append("      ,dose.psAforado as psAforado");
        hql.append("      ,dose.psAferido as psAferido ");
        hql.append("      ,(select count(vonf) ");
        hql.append("        from VolumeNotaFiscal as vonf ");
        hql.append("        join vonf.notaFiscalConhecimento as nfco ");
        hql.append("        join nfco.conhecimento as conh ");
        hql.append("        where conh.idDoctoServico = dose.idDoctoServico ");
        hql.append("       ) as countVolumes ) ");
        hql.append("from DoctoServico dose ");
        hql.append("join dose.filialByIdFilialOrigem fior ");
        hql.append("join dose.notaCreditoDoctos as ncdo ");
        hql.append("join ncdo.manifestoEntrega as maen ");
        hql.append("join ncdo.notaCredito as nc ");
        hql.append("where maen.idManifestoEntrega = ? ");
        hql.append("and nc.idNotaCredito = ? ");

        return (List<Map<String, Object>>) getAdsmHibernateTemplate().find(hql.toString(), Arrays.asList(idManifestoEntrega, idNotaCredito).toArray());
    }

    public Map<String, Object> findDoctoServicoByNrFiscalRps(Long idFilial, Long nrFiscalRps) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new Map(ds as doctoServico, mde as monitoramentoDocEletronico) ");
        sb.append(" from MonitoramentoDocEletronico mde ");
        sb.append("      inner join mde.doctoServico ds ");
        sb.append(" where ds.filialByIdFilialOrigem.idFilial = ? ");
        sb.append("   and mde.nrFiscalRps = ? ");
        sb.append("   and ds.tpDocumentoServico in ('NTE','NSE')  ");

        Object doctoServico = getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[]{idFilial, nrFiscalRps});
        if (doctoServico != null) {

            return (Map<String, Object>) doctoServico;

        }
        return null;
    }

    public Long findIdDoctoServicoByNrDoctoServico(Long nrDoctoServico, String nrIdentificacao, int destRmt) {
        StringBuilder hql = new StringBuilder();
        hql.append("select max(doctoServico.idDoctoServico) ")
                .append(" from ").append(getPersistentClass().getName()).append(" as doctoServico ");
        if (destRmt == 0) { //remetente
            hql.append(" inner join doctoServico.clienteByIdClienteRemetente remetente ")
                    .append(" inner join remetente.pessoa pessoa ");
        } else if (destRmt == 1) { //destinatario
            hql.append(" inner join doctoServico.clienteByIdClienteDestinatario destinatario ")
                    .append(" inner join destinatario.pessoa pessoa");
        }
        hql.append(" where doctoServico.nrDoctoServico = ? ")
                .append("      and pessoa.nrIdentificacao = ? ");

        return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{nrDoctoServico, nrIdentificacao});
    }

    public Integer findCodigoLocalizacaoMercadoria(final Long idDoctoServico) {

        HibernateCallback hcb = new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder sql = new StringBuilder();
                sql.append(" select lm.cd_localizacao_mercadoria ");
                sql.append(" from localizacao_mercadoria lm ");
                sql.append("      inner join docto_servico ds on ds.id_localizacao_mercadoria = lm.id_localizacao_mercadoria ");
                sql.append(" where ds.id_docto_servico = ").append(idDoctoServico);

                SQLQuery query = session.createSQLQuery(sql.toString());

                query.addScalar("cd_localizacao_mercadoria", Hibernate.INTEGER);

                return (Integer) query.uniqueResult();
            }

        };

        Integer cdLocalizacaoMercadoria = (Integer) getAdsmHibernateTemplate().execute(hcb);

        return cdLocalizacaoMercadoria;

    }

    public void executeAtualizarDoctoServicoRegerarRps(final Long idDoctoServico,
                                                       final BigDecimal vlFaturado) {

        //Tabela DOCTO_SERVICO: VL_FRETE_LIQUIDO = valor faturado retornado da rotina de c?lculo dos impostos
        getAdsmHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder sql = new StringBuilder();
                sql.append(" update docto_servico ");
                sql.append(" set vl_frete_liquido = :vl_frete_liquido ");
                sql.append(" where id_docto_servico = :id_docto_servico ");

                SQLQuery query = session.createSQLQuery(sql.toString());
                query.setLong("id_docto_servico", idDoctoServico);
                query.setBigDecimal("vl_frete_liquido", vlFaturado);

                return query.executeUpdate();
            }

        });


        //Tabela DEVEDOR_DOC_SERV: VL_DEVIDO = DOCTO_SERVICO.VL_TOTAL_DOC_SERVICO
        getAdsmHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder sql = new StringBuilder();
                sql.append(" update devedor_doc_serv ");
                sql.append(" set vl_devido = :vl_frete_liquido ");
                sql.append(" where id_docto_servico = :id_docto_servico ");

                SQLQuery query = session.createSQLQuery(sql.toString());
                query.setLong("id_docto_servico", idDoctoServico);
                query.setBigDecimal("vl_frete_liquido", vlFaturado);

                return query.executeUpdate();
            }

        });


        //Tabela DEVEDOR_DOC_SERV_FAT: VL_DEVIDO = DOCTO_SERVICO.VL_TOTAL_DOC_SERVICO
        getAdsmHibernateTemplate().execute(new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                StringBuilder sql = new StringBuilder();
                sql.append(" update devedor_doc_serv_fat ");
                sql.append(" set vl_devido = :vl_frete_liquido ");
                sql.append(" where id_docto_servico = :id_docto_servico ");

                SQLQuery query = session.createSQLQuery(sql.toString());
                query.setLong("id_docto_servico", idDoctoServico);
                query.setBigDecimal("vl_frete_liquido", vlFaturado);

                return query.executeUpdate();
            }

        });


    }

    @SuppressWarnings("unchecked")
    public List<DoctoServico> findDoctoServicoByTpControleCargaViagem(Long idControleCarga) {

        StringBuilder hql = new StringBuilder();

        hql.append("SELECT ds ");
        hql.append("FROM ");
        hql.append(DoctoServico.class.getName() + " ds, ");
        hql.append(Conhecimento.class.getName() + " c, ");
        hql.append(ManifestoNacionalCto.class.getName() + " mnc, ");
        hql.append(ManifestoViagemNacional.class.getName() + " mvn, ");
        hql.append(Manifesto.class.getName() + " m, ");
        hql.append(ControleCarga.class.getName() + " cc ");
        hql.append(WHERE);
        hql.append("	c.id = ds.idDoctoServico ");
        hql.append("AND mnc.conhecimento = c ");
        hql.append("AND mvn = mnc.manifestoViagemNacional ");
        hql.append("AND m.idManifesto = mvn.idManifestoViagemNacional ");
        hql.append("AND cc = m.controleCarga ");
        hql.append("AND cc.tpControleCarga = 'V' ");
        hql.append("AND cc.idControleCarga = ?");

        List<DoctoServico> list = (List<DoctoServico>) getAdsmHibernateTemplate().find(hql.toString(), idControleCarga);

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<DoctoServico> findDoctoServicoByTpControleCargaEntrega(Long idControleCarga) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());

        criteria.setFetchMode("manifestoEntregaDocumentos", FetchMode.JOIN)
                .createAlias("manifestoEntregaDocumentos", "manifestoEntregaDocumento");
        criteria.setFetchMode("manifestoEntregaDocumento.manifestoEntrega", FetchMode.JOIN)
                .createAlias("manifestoEntregaDocumento.manifestoEntrega", "manifestoEntrega");
        criteria.setFetchMode("manifestoEntrega.manifesto", FetchMode.JOIN)
                .createAlias("manifestoEntrega.manifesto", "manifesto");
        criteria.setFetchMode("manifesto.controleCarga", FetchMode.JOIN)
                .createAlias("manifesto.controleCarga", "controleCarga");
        criteria.add(Restrictions.eq("controleCarga.idControleCarga", idControleCarga));
        criteria.add(Restrictions.eq("controleCarga.tpControleCarga", "C"));

        return (List<DoctoServico>) findByDetachedCriteria(criteria);
    }

    public TypedFlatMap findDoctoServOutroManifestoViagem(Long idManifesto, Long idConhecimento, Long idFilialDestino) {
        final StringBuilder sql = new StringBuilder();
        Filial filialSessao = (Filial) SessionContext.get(SessionKey.FILIAL_KEY);

        sql.append("SELECT ds.tp_documento_servico, ");
        sql.append("       fo.sg_filial as sg_filial_docto,");
        sql.append("       ds.nr_docto_servico,");
        sql.append("       fo_cc.sg_filial as sg_filial_cc,");
        sql.append("       cc.nr_controle_carga");

        sql.append("  FROM manifesto_nacional_cto mnc,");
        sql.append("       manifesto_viagem_nacional mvn,");
        sql.append("       manifesto m,");
        sql.append("       docto_servico ds,");
        sql.append("       controle_carga cc, ");
        sql.append("       filial fo, ");
        sql.append("       filial fo_cc ");

        sql.append(" WHERE m.tp_manifesto = 'V' ");
        sql.append("   AND m.tp_status_manifesto <> 'CA'");
        sql.append("   AND m.id_manifesto = mvn.id_manifesto_viagem_nacional");
        sql.append("   AND mvn.id_manifesto_viagem_nacional = mnc.id_manifesto_viagem_nacional");
        sql.append("   AND mnc.id_conhecimento = ds.id_docto_servico");
        sql.append("   AND cc.Id_Controle_Carga = m.Id_Controle_Carga");
        sql.append("   AND ds.id_filial_origem = fo.id_filial");
        sql.append("   AND cc.Id_Filial_Origem = fo_cc.id_Filial");
        sql.append("   AND m.id_manifesto <> " + idManifesto);
        sql.append("   AND mnc.id_conhecimento = " + idConhecimento);
        sql.append("   AND m.id_filial_destino = " + idFilialDestino);
        sql.append("   AND m.id_filial_origem = " + filialSessao.getIdFilial());

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
                sqlQuery.addScalar("sg_filial_docto", Hibernate.STRING);
                sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
                sqlQuery.addScalar("sg_filial_cc", Hibernate.STRING);
                sqlQuery.addScalar("nr_controle_carga", Hibernate.LONG);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);
                return query.list();
            }
        };

        TypedFlatMap map = new TypedFlatMap();
        List<TypedFlatMap[]> list = getHibernateTemplate().executeFind(hcb);

        if (list != null && !list.isEmpty()) {
            Object[] obj = list.get(0);
            map.put("tp_documento_servico", obj[0]);
            map.put("sg_filial_docto", obj[1]);
            map.put("nr_docto_servico", obj[2]);
            map.put("sg_filial_cc", obj[3]);
            map.put("nr_controle_carga", obj[4]);
        }

        return map;
    }

    public List<Map<String, Object>> findDoctoServicoSuggest(final String sgFilial, final Long nrDoctoServico, final Long idEmpresa) {
        final StringBuilder sql = new StringBuilder();

        sql.append("SELECT ds.id_docto_servico, ");
        sql.append("        ds.tp_documento_servico, ");
        sql.append("        fo.sg_filial AS sg_filial, ");
        sql.append("        ds.nr_docto_servico, ");
        sql.append("        ds.dh_Emissao, ");
        sql.append("        s.tp_modal, ");
        sql.append("        s.tp_abrangencia, ");
        sql.append("        ts.id_tipo_servico, ");
        sql.append(PropertyVarcharI18nProjection.createProjection("ts.ds_tipo_servico_i")).append(" as ds_tipo_servico_i,");
        sql.append("        fo.id_filial as id_filial_origem, ");
        sql.append("        fo.sg_filial as sg_filial_origem, ");
        sql.append("        pfo.nm_fantasia as nm_filial_origem, ");
        sql.append("        fd.id_filial as id_filial_destino, ");
        sql.append("        fd.sg_filial as sg_filial_destino, ");
        sql.append("        pfd.nm_fantasia as nm_filial_destino, ");
        sql.append("        c.TP_CONHECIMENTO as finalidade, ");
        sql.append("        (select min(nfc.nr_nota_fiscal) from nota_fiscal_conhecimento nfc where nfc.id_conhecimento = c.id_conhecimento) as nf_cliente, ");
        sql.append("        pc.id_pedido_coleta, ");
        sql.append("        fc.sg_filial, ");
        sql.append("        pc.nr_coleta, ");
        sql.append("        pr.id_pessoa as id_pessoa_remetente, ");
        sql.append("        pr.nm_pessoa as nm_pessoa_remetente, ");
        sql.append("        pr.nm_fantasia as nm_fantasia_remetente, ");
        sql.append("        pr.nr_identificacao as nr_identificacao_remetente, ");
        sql.append("        cr.nr_conta as nr_conta_remetente, ");
        sql.append("        pd.id_pessoa as id_pessoa_destinatario, ");
        sql.append("        pd.nm_pessoa as nm_pessoa_destinatario, ");
        sql.append("        pd.nm_fantasia as nm_fantasia_destinatario, ");
        sql.append("        pd.nr_identificacao as nr_identificacao_destinatario, ");
        sql.append("        cd.nr_conta as nr_conta_destinatario ");
        sql.append(" FROM docto_servico ds ");
        sql.append("      INNER JOIN filial fo ON fo.id_filial = ds.id_filial_origem ");
        sql.append("      left join pessoa pfo on pfo.id_pessoa = fo.id_filial ");
        sql.append("      INNER JOIN conhecimento c ON c.id_conhecimento = ds.id_docto_servico ");
        sql.append("      left JOIN filial fd ON fd.id_filial = ds.id_filial_destino ");
        sql.append("      left join pessoa pfd on pfd.id_pessoa = fd.id_filial ");
        sql.append("      left join servico s on s.id_servico = ds.id_Servico ");
        sql.append("      left join tipo_Servico ts on ts.id_tipo_servico = s.id_tipo_Servico ");
        sql.append("      left join pedido_coleta pc on pc.id_pedido_coleta = ds.id_pedido_coleta ");
        sql.append("      left join filial fc on fc.id_filial = pc.ID_FILIAL_RESPONSAVEL ");
        sql.append("      left join pessoa pr on pr.id_pessoa = ds.id_cliente_remetente ");
        sql.append("      left join cliente cr on cr.id_cliente = pr.id_pessoa ");
        sql.append("      left join pessoa pd on pd.id_pessoa = ds.id_cliente_destinatario ");
        sql.append("      left join cliente cd on cd.id_cliente = pd.id_pessoa ");

        sql.append(" WHERE fo.sg_filial = :sgFilial ");
        sql.append("   and ds.nr_docto_servico = :nrDoctoServico ");
        sql.append("   and c.tp_situacao_conhecimento <> 'P' ");
        if (idEmpresa != null) {
            sql.append("   and fo.id_empresa = :idEmpresa ");
        }

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
                sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
                sqlQuery.addScalar("sg_filial", Hibernate.STRING);
                sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
                sqlQuery.addScalar("dh_Emissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
                sqlQuery.addScalar("tp_modal", Hibernate.STRING);
                sqlQuery.addScalar("tp_abrangencia", Hibernate.STRING);
                sqlQuery.addScalar("id_tipo_servico", Hibernate.LONG);
                sqlQuery.addScalar("ds_tipo_servico_i", Hibernate.STRING);
                sqlQuery.addScalar("id_filial_origem", Hibernate.LONG);
                sqlQuery.addScalar("sg_filial_origem", Hibernate.STRING);
                sqlQuery.addScalar("nm_filial_origem", Hibernate.STRING);
                sqlQuery.addScalar("id_filial_destino", Hibernate.LONG);
                sqlQuery.addScalar("sg_filial_destino", Hibernate.STRING);
                sqlQuery.addScalar("nm_filial_destino", Hibernate.STRING);
                sqlQuery.addScalar("finalidade", Hibernate.STRING);
                sqlQuery.addScalar("nf_cliente", Hibernate.INTEGER);
                sqlQuery.addScalar("id_pedido_coleta", Hibernate.LONG);
                sqlQuery.addScalar("sg_filial", Hibernate.STRING);
                sqlQuery.addScalar("nr_coleta", Hibernate.LONG);
                sqlQuery.addScalar("id_pessoa_remetente", Hibernate.LONG);
                sqlQuery.addScalar("nm_pessoa_remetente", Hibernate.STRING);
                sqlQuery.addScalar("nm_fantasia_remetente", Hibernate.STRING);
                sqlQuery.addScalar("nr_identificacao_remetente", Hibernate.STRING);
                sqlQuery.addScalar("nr_conta_remetente", Hibernate.LONG);
                sqlQuery.addScalar("id_pessoa_destinatario", Hibernate.LONG);
                sqlQuery.addScalar("nm_pessoa_destinatario", Hibernate.STRING);
                sqlQuery.addScalar("nm_fantasia_destinatario", Hibernate.STRING);
                sqlQuery.addScalar("nr_identificacao_destinatario", Hibernate.STRING);
                sqlQuery.addScalar("nr_conta_destinatario", Hibernate.LONG);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                query.setString("sgFilial", sgFilial);
                query.setLong("nrDoctoServico", nrDoctoServico);
                if (idEmpresa != null) {
                    query.setLong("idEmpresa", idEmpresa);
                }
                csq.configQuery(query);
                return query.list();
            }
        };

        List<Map<String, Object>> toReturn = new ArrayList<>();

        List<Object[]> list = getHibernateTemplate().executeFind(hcb);

        for (Object[] o : list) {
            TypedFlatMap map = new TypedFlatMap();
            map.put("idDoctoServico", o[0]);
            map.put("tpDoctoServico", o[1]);
            map.put("sgFilial", o[2]);
            map.put("nrDoctoServico", o[3]);
            map.put("dhEmissao", o[4]);
            map.put("modal", o[5]);
            map.put("abrangencia", o[6]);
            map.put("idTipoServico", o[7]);
            map.put("dsTipoServico_i", o[8]);
            map.put("idFilialOrigem", o[9]);
            map.put("sgFilialOrigem", o[10]);
            map.put("nmFilialOrigem", o[11]);
            map.put("idFilialDestino", o[12]);
            map.put("sgFilialDestino", o[13]);
            map.put("nmFilialDestino", o[14]);
            map.put("finalidade", o[15]);
            map.put("nfCliente", o[16]);
            map.put("idPedidoColeta", o[17]);
            map.put("sgFilial", o[18]);
            map.put("nrColeta", o[19]);
            map.put("idPessoaRemetente", o[20]);
            map.put("nmPessoaRemetente", o[21]);
            map.put("nmFantasiaRemetente", o[22]);
            map.put("nrIdentificacaoRemetente", o[23]);
            map.put("nrContaRemetente", o[24]);
            map.put("idPessoaDestinatario", o[25]);
            map.put("nmPessoaDestinatario", o[26]);
            map.put("nmFantasiaDestinatario", o[27]);
            map.put("nrIdentificacaoDestinatario", o[28]);
            map.put("nrContaDestinatario", o[29]);
            toReturn.add(map);

        }

        return toReturn;
    }

    public List<TypedFlatMap> findDocumentoReembarcado(final Long idFranquia, final YearMonthDay dtInicioCompetencia, final YearMonthDay dtFimCompetencia) {
        final StringBuilder sql = new StringBuilder();

        sql.append("SELECT DS.ID_DOCTO_SERVICO, DS.PS_AFERIDO, MA.ID_MANIFESTO");
        sql.append("  FROM MANIFESTO MA");
        sql.append(" INNER JOIN MANIFESTO_VIAGEM_NACIONAL MVN ON (MVN.ID_MANIFESTO_VIAGEM_NACIONAL = MA.ID_MANIFESTO)");
        sql.append(" INNER JOIN MANIFESTO_NACIONAL_CTO MNC ON (MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL)");
        sql.append(" INNER JOIN DOCTO_SERVICO DS ON (DS.ID_DOCTO_SERVICO = MNC.ID_CONHECIMENTO)");
        sql.append("   AND DS.ID_FILIAL_ORIGEM <> ? ");
        sql.append("   AND DS.ID_FILIAL_DESTINO <> ? ");
        sql.append("   AND MA.ID_FILIAL_ORIGEM = ? ");
        sql.append("   AND MA.DH_EMISSAO_MANIFESTO BETWEEN to_date(?,'yyyy-mm-dd') AND to_date(?,'yyyy-mm-dd') ");

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
                sqlQuery.addScalar("PS_AFERIDO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);

                query.setParameter(0, idFranquia);
                query.setParameter(1, idFranquia);
                query.setParameter(2, idFranquia);
                query.setParameter(3, dtInicioCompetencia.toString());
                query.setParameter(4, dtFimCompetencia.toString());

                return query.list();
            }
        };

        List<Object[]> documentoReembarcadoList = getHibernateTemplate().executeFind(hcb);

        List<TypedFlatMap> r = new ArrayList<>(documentoReembarcadoList.size());

        for (Object[] o : documentoReembarcadoList) {
            TypedFlatMap tfm = new TypedFlatMap();
            tfm.put("ID_DOCTO_SERVICO", o[0]);
            tfm.put("PS_AFERIDO", o[1]);
            tfm.put("ID_MANIFESTO", o[2]);

            r.add(tfm);
        }

        return r;
    }

    @SuppressWarnings("unchecked")
    public BigDecimal findValorCustoFreteAereo(Long idDoctoServico) {
        final StringBuilder sql = new StringBuilder()
                .append("select f_custo_frete_aereo( " + idDoctoServico + " ) from dual ");

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);
                return query.list();
            }
        };

        List<Object> valorCustoFreteAereo = getHibernateTemplate().executeFind(hcb);

        if (valorCustoFreteAereo != null && valorCustoFreteAereo.size() == 1
                && valorCustoFreteAereo.get(0) instanceof BigDecimal) {
            return (BigDecimal) valorCustoFreteAereo.get(0);
        }

        return BigDecimal.ZERO;
    }

    @SuppressWarnings("unchecked")
    public BigDecimal findValorCustoFreteCarreteiro(Long idDoctoServico) {
        final StringBuilder sql = new StringBuilder()
                .append("select f_custo_frete_carreteiro( " + idDoctoServico + " ) from dual ");

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);
                return query.list();
            }
        };

        List<Object> valorCustoFreteCarreteiro = getHibernateTemplate().executeFind(hcb);

        if (valorCustoFreteCarreteiro != null && valorCustoFreteCarreteiro.size() == 1
                && valorCustoFreteCarreteiro.get(0) instanceof BigDecimal) {
            return (BigDecimal) valorCustoFreteCarreteiro.get(0);
        }

        return BigDecimal.ZERO;
    }


    /**
     * Retorna os manifestos de entrega que tenham ocorr?ncias entrega OU a entrega ? nula,
     * que a responsabilidade n?o seja TNT e sim do Parceiro (PBAI)
     * relacionadas ao documento de servi?o
     *
     * @param listIdDoctoServico
     * @param tpOcorrencia
     * @param ocasionadoTnt
     * @return
     */
    @SuppressWarnings("rawtypes")
    public List findNrDoctoServicoFilialByDoctosServico(List<Long> doctosServico) {
        SqlTemplate hql = new SqlTemplate();
        StringBuilder pj = new StringBuilder()
                .append(" new map( ")
                .append("   doctoServico.idDoctoServico as idDoctoServico, ")
                .append("   doctoServico.nrDoctoServico as nrDoctoServico, ")
                .append("   doctoServico.filialByIdFilialOrigem.sgFilial as filial ")
                .append(")");
        hql.addProjection(pj.toString());

        hql.addFrom(new StringBuilder()
                .append(DoctoServico.class.getName()).append(" doctoServico ")
                .toString());
        hql.addCriteriaIn("doctoServico.idDoctoServico", doctosServico);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findPreAwbDoctoServicoAtivo(Long idAwb) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select ");
        hql.append(" 	new Map(con.nrConhecimento as nrConhecimento, ");
        hql.append(" 			con.filialOrigem.idFilial as idFilialOrigem, ");
        hql.append(" 			con.tpDoctoServico as tpDoctoServico, ");
        hql.append(" 			con.idDoctoServico as idDoctoServico ) ");
        hql.append(" from ");
        hql.append(Conhecimento.class.getName()).append(" con ");
        hql.append(" 	join con.ctoAwbs ctoAwbs ");
        hql.append(" 	join ctoAwbs.awb awb ");
        hql.append(" where ");
        hql.append(" 	awb.idAwb = :idAwb ");
        hql.append(" 	and awb.tpStatusAwb = :tpStatusAwb ");
        hql.append(" 	and con.tpSituacaoConhecimento <> :tpSituacaoConhecimento ");

        Map<String, Object> namedParams = new HashMap<>();
        namedParams.put("idAwb", idAwb);
        namedParams.put("tpStatusAwb", ConstantesExpedicao.TP_STATUS_PRE_AWB);
        namedParams.put("tpSituacaoConhecimento", ConstantesExpedicao.TP_STATUS_CANCELADO);

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), namedParams);
    }

    public List<DoctoServico> findDocumentosByTpOcorrenciasByIdManifesto(Long idManifesto, String tpOcorrencias) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("ds");
        sql.addInnerJoin(getPersistentClass().getName(), "ds");
        sql.addInnerJoin("ds.manifestoEntregaDocumentos", "med");
        sql.addInnerJoin("med.manifestoEntrega", "me");
        sql.addInnerJoin("med.ocorrenciaEntrega", "oe");
        sql.addCriteria("me.idManifestoEntrega", "=", idManifesto);
        
        sql.addCustomCriteria("((oe.tpOcorrencia not in( " + tpOcorrencias + ")) OR (oe.tpOcorrencia in("+ tpOcorrencias +") and oe.cdOcorrenciaEntrega in('102'))) ");
        
        return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
    }

    /**
     * LMS-6106 - Busca <tt>DoctoServico</tt> por tipo, filial de origem e
     * n?mero. Retorna <tt>null</tt> se n?o encontrado.
     *
     * @param tpDocumentoServico tipo do documento de servi?o
     * @param idFilialOrigem     id da filial de origem
     * @param nrDoctoServico     n?mero do documento de servi?o
     * @return <tt>DoctoServico</tt> ou <tt>null</tt> se n?o encontrado
     */
    public DoctoServico findDoctoServico(String tpDocumentoServico, Long idFilialOrigem, Long nrDoctoServico) {
        List<Criterion> criterions = new ArrayList<>();
        criterions.add(Restrictions.eq("tpDocumentoServico", tpDocumentoServico));
        criterions.add(Restrictions.eq("filialByIdFilialOrigem.idFilial", idFilialOrigem));
        criterions.add(Restrictions.eq("nrDoctoServico", nrDoctoServico));
        List<DoctoServico> result = findByCriterion(criterions, null);
        return result == null || result.isEmpty() ? null : result.get(0);
    }

    /**
     * LMS-6106 - Verifica se <tt>DoctoServico</tt> est? relacionado a
     * determinado <tt>Boleto</tt>.
     *
     * @param idBoleto       id do <tt>Boleto</tt>
     * @param idDoctoServico id do <tt>DoctoServico</tt>
     * @return <tt>true</tt> se <tt>Boleto</tt> estiver relacionado ao
     * <tt>DoctoServico</tt>, <tt>false</tt> caso contr?rio
     */
    public boolean isBoletoDoctoServico(Long idBoleto, Long idDoctoServico) {
        StringBuilder hql = new StringBuilder()
                .append("SELECT ds ")
                .append("FROM Boleto b ")
                .append("JOIN b.fatura.itemFaturas f ")
                .append("JOIN f.devedorDocServFat.doctoServico ds ")
                .append("WHERE b.idBoleto = ? ")
                .append("AND ds.idDoctoServico = ? ");
        Object[] values = new Object[]{
                idBoleto,
                idDoctoServico
        };
        @SuppressWarnings("unchecked")
        List<DoctoServico> result = getAdsmHibernateTemplate().find(hql.toString(), values);
        return result != null && !result.isEmpty();
    }

    /**
     * LMS-6106 - Busca dados de documentos de servi?o relacionados a um
     * <tt>Boleto</tt> e marcados para exclus?o (<tt>BL_EXCLUIR</tt>) no
     * processo de cancelamento parcial de fatura.
     *
     * @param idBoleto id do <tt>Boleto</tt>
     * @return lista de mapas para popular <tt>&lt;adsm:listbox /&gt;</tt>
     */
    public List<Map<String, Object>> findDoctoServicoListByBoleto(Long idBoleto) {
        StringBuilder sql = new StringBuilder()
                .append("SELECT ")
                .append("  ds.id_docto_servico          AS \"doctoServico.idDoctoServico\", ")
                .append("  ds.tp_documento_servico      AS \"tpDocumentoServico.value\", ")
                .append("  vi18n(vd.ds_valor_dominio_i) AS \"tpDocumentoServico.description\", ")
                .append("  f.id_filial                  AS \"filial.idFilial\", ")
                .append("  f.sg_filial                  AS \"filial.sgFilial\", ")
                .append("  ds.nr_docto_servico          AS \"doctoServico.nrDoctoServico\" ")
                .append("FROM boleto b ")
                .append("INNER JOIN item_fatura it ")
                .append("  ON it.id_fatura = b.id_fatura ")
                .append("INNER JOIN devedor_doc_serv_fat ddsf ")
                .append("  ON ddsf.id_devedor_doc_serv_fat = it.id_devedor_doc_serv_fat ")
                .append("INNER JOIN docto_servico ds ")
                .append("  ON ds.id_docto_servico = ddsf.id_docto_servico ")
                .append("INNER JOIN valor_dominio vd ")
                .append("  ON vd.vl_valor_dominio = ds.tp_documento_servico ")
                .append("INNER JOIN dominio d ")
                .append("  ON d.id_dominio=vd.id_dominio ")
                .append("  AND d.nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO' ")
                .append("INNER JOIN filial f ")
                .append("  ON f.id_filial = ds.id_filial_origem ")
                .append("WHERE b.id_boleto = :idBoleto ")
                .append("AND it.bl_excluir = 'S' ")
                .append("ORDER BY ")
                .append("  vi18n(vd.ds_valor_dominio_i), ")
                .append("  f.sg_filial, ")
                .append("  ds.nr_docto_servico ");

        Map<String, Object> values = new HashMap<>();
        values.put("idBoleto", idBoleto);
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, null);
    }


    public BigDecimal findVlFreteLiquidoByIdDoctoServico(Long idDoctoServico) {

        StringBuilder hql = new StringBuilder();

        hql.append("SELECT ds.vlLiquido ");
        hql.append("FROM ");
        hql.append(DoctoServico.class.getName() + " ds ");
        hql.append(WHERE);
        hql.append(" ds.idDoctoServico = ?");

        return (BigDecimal) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idDoctoServico});

    }

    public Object[] findDetailImage(TypedFlatMap criteria) {

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT                       																");
        sql.append("  F.CD_FILIAL AS FILIAL,              														");
        sql.append("  DS.NR_DOCTO_SERVICO AS CTRC,                                                              ");
        sql.append("  To_Char(DS.DH_EMISSAO,'yyyymmdd') AS DATA                                                 ");
        sql.append(" FROM                                                                                       ");
        sql.append("  FILIAL F,                                                                         		");
        sql.append("  DOCTO_SERVICO DS                                                                		    ");
        sql.append(" WHERE DS.ID_DOCTO_SERVICO = :id                                                            ");
        sql.append(" AND DS.ID_FILIAL_ORIGEM = F.ID_FILIAL                                                 		");

        Map<String, Object> params = new HashMap<>();

        ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("FILIAL", Hibernate.STRING);
                sqlQuery.addScalar("CTRC", Hibernate.INTEGER);
                sqlQuery.addScalar("DATA", Hibernate.STRING);
            }
        };

        Object[] retorno = null;
        params.put("id", criteria.getLong("id"));
        List<Object[]> result = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
        if (result != null && !result.isEmpty()) {
            retorno = result.get(0);
        }

        return retorno;
    }

    public DoctoServico findDoctoServicoByIdWhiteList(Long idWhiteList) {

        StringBuilder hql = new StringBuilder();

        hql.append("SELECT ds ");
        hql.append("FROM ");
        hql.append(DoctoServico.class.getName() + " ds, ");
        hql.append(WhiteList.class.getName() + " wl ");
        hql.append(WHERE);
        hql.append(" ds.idDoctoServico = wl.doctoServico.idDoctoServico ");
        hql.append(" and wl.idWhiteList = ?");

        return (DoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{idWhiteList});

    }

    public List<DoctoServico> findDoctosServicoByControleCargaNCPadrao(Long idControleCarga, List<Long> idPedidoColeta) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("doc");
        sql.addFrom(ControleCarga.class.getName(), "cc");
        sql.addFrom(ManifestoColeta.class.getName(), "mc");
        sql.addFrom(PedidoColeta.class.getName(), "pc");
        sql.addFrom(DoctoServico.class.getName(), "doc");
        sql.addFrom(Conhecimento.class.getName(), "con");
        sql.addCustomCriteria("cc.idControleCarga = mc.controleCarga.idControleCarga");
        sql.addCustomCriteria("mc.idManifestoColeta = pc.manifestoColeta.idManifestoColeta ");
        sql.addCustomCriteria("con.idDoctoServico = doc.idDoctoServico ");
        sql.addCustomCriteria("pc.tpStatusColeta in('FI','NT','EX') ");
        sql.addCriteriaIn("pc.idPedidoColeta", idPedidoColeta);
        sql.addCriteria("cc.idControleCarga", "=", idControleCarga);
        sql.addCustomCriteria("doc.pedidoColeta.idPedidoColeta = pc.idPedidoColeta ");
        sql.addCustomCriteria("doc.dhEmissao is not null ");
        sql.addCustomCriteria("con.tpSituacaoConhecimento = 'E' ");
        sql.addCustomCriteria("con.tpConhecimento in('CF','NO') ");
        sql.addCustomCriteria("NOT exists(select idNotaCreditoCalcPadraoDocto FROM " + NotaCreditoCalcPadraoDocto.class.getName() + " ncp WHERE ncp.pedidoColeta.idPedidoColeta = pc.idPedidoColeta AND ncp.blCalculado = 'S') ");

        return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
    }

    public List<NotaFiscalConhecimento> findNotasFiscaisByDoctoServico(DoctoServico doctoServico) {

        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT nf FROM ");
        hql.append(NotaFiscalConhecimento.class.getName() + " nf ,");
        hql.append(Pessoa.class.getName() + " p ,");
        hql.append(Filial.class.getName() + " f ");
        hql.append(" WHERE ");
        hql.append(" nf.conhecimento.idDoctoServico = ? ");
        hql.append(" AND nf.conhecimento.clienteByIdClienteRemetente.idCliente = p.idPessoa ");
        hql.append(" AND nf.conhecimento.filialByIdFilialOrigem.idFilial = f.idFilial ");
        hql.append(" ORDER BY nf.nrNotaFiscal ASC");

        return (List<NotaFiscalConhecimento>) getAdsmHibernateTemplate().find(hql.toString(), new Object[]{doctoServico.getIdDoctoServico()});
    }


    /***
     * Busca detalhes dos docs para calculo de rateio
     * @param idNotaCredito
     * @return
     */
    public List<Map<String, Object>> findDoctosByIdNotaCredito(Long idNotaCredito) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT NCDOC.ID_DOCTO_SERVICO AS ID_DOCUMENTO, ");
        sql.append("  DC.ID_CLIENTE_REMETENTE, ");
        sql.append("  DC.QT_VOLUMES, ");
        sql.append("  DC.PS_REFERENCIA_CALCULO, ");
        sql.append("  DC.PS_REAL AS PS_REAL, ");
        sql.append("  DC.PS_AFORADO AS PS_AFORADO, ");
        sql.append("  DC.PS_AFERIDO AS PS_AFERIDO, ");
        sql.append("  DC.PS_CUBADO_AFERIDO AS PS_CUBADO_AFERIDO, ");
        sql.append("  DC.VL_TOTAL_PARCELAS, ");
        sql.append("  DC.VL_FRETE_LIQUIDO, ");
        sql.append("  DC.VL_TOTAL_DOC_SERVICO, ");
        sql.append("  DC.VL_MERCADORIA, ");
        sql.append("  DC.DS_ENDERECO_ENTREGA_REAL AS ENDERECO ");
        sql.append("FROM NOTA_CREDITO_CALC_PAD_DOCTO NCDOC , ");
        sql.append("  DOCTO_SERVICO DC ");
        sql.append("WHERE NCDOC.BL_CALCULADO         = 'S' ");
        sql.append("AND NCDOC.ID_NOTA_CREDITO  = :idNotaCredito ");
        sql.append("AND NCDOC.ID_DOCTO_SERVICO = DC.ID_DOCTO_SERVICO ");
        sql.append("ORDER BY DC.DS_ENDERECO_ENTREGA_REAL");

        Map<String, Object> values = new HashMap<>();
        values.put("idNotaCredito", idNotaCredito);
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, null);

    }

    /***
     * Busca detalhes dos docs para calculo de rateio
     * @param idNotaCredito
     * @return
     */
    public List<Map<String, Object>> findColeatsByIdNotaCredito(Long idNotaCredito) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT ");
        sql.append("   DC.ID_PEDIDO_COLETA AS ID_PEDIDO_COLETA, ");
        sql.append("   DC.ID_DOCTO_SERVICO AS ID_DOCUMENTO, ");
        sql.append("   DC.ID_CLIENTE_REMETENTE, ");
        sql.append("   DC.QT_VOLUMES AS QT_VOLUMES, ");
        sql.append("   DC.PS_REFERENCIA_CALCULO AS PS_REFERENCIA_CALCULO, ");
        sql.append("   DC.PS_REAL AS PS_REAL, ");
        sql.append("   DC.PS_AFORADO AS PS_AFORADO, ");
        sql.append("   DC.PS_AFERIDO AS PS_AFERIDO, ");
        sql.append("   DC.PS_CUBADO_AFERIDO AS PS_CUBADO_AFERIDO, ");
        sql.append("   DC.VL_TOTAL_PARCELAS, ");
        sql.append("   DC.VL_FRETE_LIQUIDO AS VL_FRETE_LIQUIDO, ");
        sql.append("   DC.VL_TOTAL_DOC_SERVICO AS VL_TOTAL_DOC_SERVICO, ");
        sql.append("   DC.VL_MERCADORIA         AS VL_MERCADORIA, ");
        sql.append("   DC.DS_ENDERECO_ENTREGA_REAL  AS ENDERECO ");
        sql.append(" FROM NOTA_CREDITO_CALC_PAD_DOCTO NCDOC , ");
        sql.append("   DOCTO_SERVICO DC ");
        sql.append(" WHERE BL_CALCULADO         = 'S' ");
        sql.append(" AND NCDOC.ID_NOTA_CREDITO  = :idNotaCredito ");
        sql.append(" AND NCDOC.ID_PEDIDO_COLETA = DC.ID_PEDIDO_COLETA ");

        Map<String, Object> values = new HashMap<>();
        values.put("idNotaCredito", idNotaCredito);
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, null);
    }

    /***
     * Busca detalhes dos docs para calculo de rateio
     * @param idNotaCredito
     * @return
     */
    public List<Map<String, Object>> findColeatsByIdNotaCreditoRateio(Long idNotaCredito) {
        StringBuilder sql = new StringBuilder();

        sql.append(" 	SELECT distinct 'S' AS COLETA, ");
        sql.append("         PC.PS_TOTAL_VERIFICADO, ");
        sql.append("         PC.PS_TOTAL_AFORADO_VERIFICADO, ");
        sql.append("  DC.ID_PEDIDO_COLETA AS ID_PEDIDO_COLETA, ");
        sql.append("  DC.ID_DOCTO_SERVICO AS ID_DOCUMENTO, ");
        sql.append(" 		 DC.ID_CLIENTE_REMETENTE,  ");
        sql.append("  DC.QT_VOLUMES            AS QT_VOLUMES, ");
        sql.append("  DC.PS_REFERENCIA_CALCULO AS PS_REFERENCIA_CALCULO, ");
        sql.append("  DC.PS_REAL AS PS_REAL, ");
        sql.append("  DC.PS_AFORADO AS PS_AFORADO, ");
        sql.append("  DC.PS_AFERIDO AS PS_AFERIDO, ");
        sql.append("  DC.PS_CUBADO_AFERIDO AS PS_CUBADO_AFERIDO, ");
        sql.append(" 		 DC.VL_TOTAL_PARCELAS,  ");
        sql.append("  DC.VL_FRETE_LIQUIDO      AS VL_FRETE_LIQUIDO, ");
        sql.append("  DC.VL_TOTAL_DOC_SERVICO  AS VL_TOTAL_DOC_SERVICO, ");
        sql.append("  DC.VL_MERCADORIA         AS VL_MERCADORIA, ");
        sql.append(" 		 DC.DS_ENDERECO_ENTREGA_REAL  AS ENDERECO,  ");
        sql.append(" 		 DC.ID_CLIENTE_DESTINATARIO AS ID_CLIENTE_DESTINATARIO, ");
        sql.append(" 		 PC.ID_ENDERECO_PESSOA AS ID_ENDERECO_PESSOA, ");
        sql.append(" 		 PC.ID_CLIENTE AS ID_CLIENTE, ");
        sql.append(" 		 TO_CHAR(DC.DH_EMISSAO, 'ddMMyyyy') AS DH_EMISSAO ");
        sql.append(" 	FROM DOCTO_SERVICO DC, PEDIDO_COLETA PC,  CONHECIMENTO CON  ");
        sql.append("     WHERE PC.ID_PEDIDO_COLETA = DC.ID_PEDIDO_COLETA  ");
        sql.append("           AND DC.ID_PEDIDO_COLETA IN (SELECT ID_PEDIDO_COLETA FROM NOTA_CREDITO_COLETA WHERE ID_NOTA_CREDITO = :idNotaCredito) ");
        sql.append("		   AND DC.DH_EMISSAO IS NOT NULL");
        sql.append("		   AND DC.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO ");
        sql.append("		   AND CON.TP_SITUACAO_CONHECIMENTO IN ('E') ");

        ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("COLETA", Hibernate.STRING);
                sqlQuery.addScalar("PS_TOTAL_VERIFICADO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_TOTAL_AFORADO_VERIFICADO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_PEDIDO_COLETA", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_DOCUMENTO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_CLIENTE_REMETENTE", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("QT_VOLUMES", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_REFERENCIA_CALCULO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_REAL", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_AFORADO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_AFERIDO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("PS_CUBADO_AFERIDO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_TOTAL_PARCELAS", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_FRETE_LIQUIDO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_TOTAL_DOC_SERVICO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ENDERECO", Hibernate.STRING);
                sqlQuery.addScalar("ID_CLIENTE_DESTINATARIO", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_ENDERECO_PESSOA", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("ID_CLIENTE", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("DH_EMISSAO", Hibernate.STRING);
            }
        };

        Map<String, Object> values = new HashMap<>();
        values.put("idNotaCredito", idNotaCredito);
        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, configureSqlQuery);
    }


    public List<Map<String, Object>> findDoctsManifestoEntregaByNotaCredito(Long idNotaCredito) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT doc.ID_DOCTO_SERVICO AS ID_DOCUMENTO, ");
        sql.append("  doc.ID_CLIENTE_REMETENTE, ");
        sql.append("  doc.QT_VOLUMES, ");
        sql.append("  doc.PS_REFERENCIA_CALCULO, ");
        sql.append("  doc.PS_REAL AS PS_REAL, ");
        sql.append("  doc.PS_AFORADO AS PS_AFORADO, ");
        sql.append("  doc.PS_AFERIDO AS PS_AFERIDO, ");
        sql.append("  doc.PS_CUBADO_AFERIDO AS PS_CUBADO_AFERIDO, ");
        sql.append("  doc.VL_TOTAL_PARCELAS, ");
        sql.append("  doc.VL_FRETE_LIQUIDO, ");
        sql.append("  doc.VL_TOTAL_DOC_SERVICO, ");
        sql.append("  doc.VL_MERCADORIA, ");
        sql.append("  doc.DS_ENDERECO_ENTREGA_REAL  AS ENDERECO,  ");
        sql.append("  doc.ID_CLIENTE_DESTINATARIO AS ID_CLIENTE_DESTINATARIO ");
        sql.append("  FROM NOTA_CREDITO_DOCTO NCD, DOCTO_SERVICO doc WHERE NCD.id_docto_servico = doc.id_docto_servico and  NCD.ID_NOTA_CREDITO = :idNotaCredito ");

        Map<String, Object> values = new HashMap<>();
        values.put("idNotaCredito", idNotaCredito);

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, null);
    }

    public Long findMaxIdDoctoServico(Long idMonitoramentoDescarga) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT MAX(ds.id_docto_servico) as idDoctoServico ");
        sql.append(" FROM docto_servico ds, ");
        sql.append("      monitoramento_descarga md, ");
        sql.append("      volume_nota_fiscal vnf, ");
        sql.append("      nota_fiscal_conhecimento nfc ");
        sql.append(" WHERE md.id_monitoramento_descarga = :idMonitoramentoDescarga ");
        sql.append(" AND md.id_monitoramento_descarga = vnf.id_monitoramento_descarga ");
        sql.append(" AND vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
        sql.append(" AND nfc.id_conhecimento = ds.id_docto_servico ");

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
            }
        };

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idMonitoramentoDescarga", idMonitoramentoDescarga);

        List<?> object = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);

        if (!object.isEmpty()) {
            return (Long) object.get(0);
        }

        return null;
    }

    public boolean findExisteDoctoServicoNegativo(Long idMonitoramentoDescarga) {
        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT 1 ");
        sql.append(" FROM docto_servico ds, ");
        sql.append("   monitoramento_descarga md, ");
        sql.append("   volume_nota_fiscal vnf, ");
        sql.append("   nota_fiscal_conhecimento nfc ");
        sql.append(" WHERE md.id_monitoramento_descarga  = :idMonitoramentoDescarga ");
        sql.append(" AND md.id_monitoramento_descarga    = vnf.id_monitoramento_descarga ");
        sql.append(" AND vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
        sql.append(" AND nfc.id_conhecimento             = ds.id_docto_servico ");
        sql.append(" AND ds.nr_docto_servico < 0 ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idMonitoramentoDescarga", idMonitoramentoDescarga);

        List<?> object = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, null);

        return !object.isEmpty();
    }

    public List<DoctoServico> findDoctosByIdControleCarga(Long idControleCarga) {
        StringBuilder sql = new StringBuilder()
                .append("select ds ")
                .append("from ")
                .append(DoctoServico.class.getName()).append(" as ds ")
                .append("inner join ds.devedorDocServs dds ")
                .append("inner join ds.manifestoEntregaDocumentos med ")
                .append("inner join med.manifestoEntrega me ")
                .append("inner join me.manifesto ma ")
                .append("inner join ma.controleCarga cc ")
                .append("where ")
                .append("cc.id = ? ");
        return getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
    }
    
    /**
     *  LMSA-7399 - 09/07/2018 - Inicio
     * Obtem os conhecimentos cujo o cliente exige a assinatura digital, por�m aind
     * n�o foi digitalizada 
     * @param idManifesto
     * @return lista de DoctoServico
     */    
    public List<DoctoServico> findDoctosByIdManifestoEntregaSemAssinaturaDigital(Long idManifesto) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select doctoServico from DoctoServico as doctoServico, ");
        sql.append("Manifesto as manifesto, ");
        sql.append("ManifestoEntrega as manifestoEntrega, ");
        sql.append("ManifestoEntregaDocumento as manifestoEntregaDocumento, ");
		sql.append("com.mercurio.lms.vendas.model.Cliente as cliente ");
        sql.append("where manifesto.id = manifestoEntrega.idManifestoEntrega ");
        sql.append("and manifestoEntrega.idManifestoEntrega = manifestoEntregaDocumento.manifestoEntrega.idManifestoEntrega ");
        sql.append("and manifestoEntregaDocumento.doctoServico.idDoctoServico = doctoServico.idDoctoServico ");
		sql.append("and cliente.idCliente = doctoServico.clienteByIdClienteRemetente.idCliente ");
        sql.append("and doctoServico.tpDocumentoServico in ('CTE','NFT','NTE') ");
        sql.append("and cliente.blObrigaComprovanteEntrega = 'S' ");    
        sql.append("and not exists (select 1 from com.mercurio.lms.entrega.model.ComprovanteEntrega as comprovante where comprovante.idDoctoServico = doctoServico.idDoctoServico and comprovante.assinatura is not null) ");
        sql.append("and manifesto.id = :idManifesto");
       
        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idManifesto", idManifesto);
    }
    // LMSA-7399 - 09/07/2018 - Fim

	public DoctoServico findByTipoDoctoFilialNumero(
			String nrIdentificacaoFilialOrigem, Long nrDoctoServico,
			String tpDoctoServico) {
		
		StringBuilder hql = new StringBuilder("select ds from DoctoServico ds where ")
			.append(" ds.filialByIdFilialOrigem.pessoa.nrIdentificacao = :nrIdentificacaoFilial ")
			.append(" and ds.tpDocumentoServico = :tpDoctoServico ")
			.append(" and ds.nrDoctoServico = :nrDoctoServico ")
		;
		
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("nrIdentificacaoFilial", nrIdentificacaoFilialOrigem);
		parameters.put("tpDoctoServico", tpDoctoServico);
		parameters.put("nrDoctoServico", nrDoctoServico);
		
		return (DoctoServico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), parameters);
	}
	
    public Integer getCountDoctosServicoReentregaByIdDoctoServico(Long idDoctoServico) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from DoctoServico as doctoServico, ");
        sql.append(" Conhecimento as conhecimento ");
        sql.append(" where doctoServico.doctoServicoOriginal.id = ? ");
        sql.append(" and doctoServico.id = conhecimento.id ");
        sql.append(" and conhecimento.tpConhecimento = 'RE' ");
        sql.append(" and conhecimento.tpSituacaoConhecimento = 'E' ");
        
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idDoctoServico});
    }

	public List<Map<String, Object>> findDadosPodScanning(Long idDoctoServico) {
		StringBuilder sql = new StringBuilder()
			.append("     SELECT fi.cd_filial,")
			.append("            pesFi.nr_identificacao,")
			.append("            ds.dh_emissao,")
			.append("            ds.nr_docto_servico,")
			.append("            mde.nr_chave,")
			.append("            pesDds.nr_identificacao cnpjDevedor,")
			.append("            pesDds.nm_pessoa nomeDevedor,")
			.append("            pesRem.nm_pessoa nomeRemetente,")
			.append("            pesDest.nm_pessoa nomeDestinatario")
			.append("       FROM docto_servico ds")
			.append(" INNER JOIN filial fi")
			.append("         ON ds.id_filial_origem = fi.id_filial")
			.append(" INNER JOIN pessoa pesFi")
			.append("         ON fi.id_filial = pesFi.id_pessoa")
			.append(" INNER JOIN pessoa pesRem")
			.append("         ON ds.id_cliente_remetente = pesRem.id_pessoa")
			.append(" INNER JOIN pessoa pesDest")
			.append("         ON ds.id_cliente_destinatario = pesDest.id_pessoa")
			.append(" INNER JOIN devedor_doc_serv dds ")
			.append("         ON ds.id_docto_servico = dds.id_docto_servico")
			.append(" INNER JOIN pessoa pesDds")
			.append("         ON dds.id_cliente = pesDds.id_pessoa")
			.append(" INNER JOIN monitoramento_doc_eletronico mde ")
			.append("         ON ds.id_docto_servico = mde.id_docto_servico")
			.append("      WHERE ds.id_docto_servico = :idDoctoServico");
		
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("cd_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
				sqlQuery.addScalar("dh_emissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("nr_chave", Hibernate.STRING);
				sqlQuery.addScalar("cnpjDevedor", Hibernate.STRING);
				sqlQuery.addScalar("nomeDevedor", Hibernate.STRING);
				sqlQuery.addScalar("nomeRemetente", Hibernate.STRING);
				sqlQuery.addScalar("nomeDestinatario", Hibernate.STRING);
			}
		};
		
		Map<String, Object> values = new HashMap<>();
		values.put("idDoctoServico", idDoctoServico);
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), values, configSql);
	}
	
	private ConfigureSqlQuery configureSqlQueryWithPriorizacaoEmbarqueo() {
        return new ConfigureSqlQuery() {

			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idRegistroPriorizacaoDocto", Hibernate.LONG);
				configCommonQueryProjectionDoctoServico(sqlQuery);
			}
        	
        };
	}
	
	private void configCommonQueryProjectionDoctoServico(SQLQuery sqlQuery) {
		sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
        sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
        sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
        sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
        sqlQuery.addScalar("tpDocumentoServicoDescription", Hibernate.STRING);
	}
	
	private ConfigureSqlQuery configureSqlQueryDpeAtrasadoServicoPrioritario() {
		return new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				configCommonQueryProjectionDoctoServico(sqlQuery);
				
			}
		};
	}
	
}
