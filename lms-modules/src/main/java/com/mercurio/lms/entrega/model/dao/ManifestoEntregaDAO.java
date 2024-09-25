package com.mercurio.lms.entrega.model.dao;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.EventoManifesto;
import com.mercurio.lms.carregamento.model.IntegranteEqOperac;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestosEntregas;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.DocumentoCliente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class ManifestoEntregaDAO extends BaseCrudDao<ManifestoEntrega, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return ManifestoEntrega.class;
    }

    protected void initFindByIdLazyProperties(Map map) {
        map.put("manifesto", FetchMode.JOIN);
    }

    public List<ManifestoEntrega> findByNrManifestoByFilial(Integer nrManifesto, Long idFilial) {
        DetachedCriteria dc = createDetachedCriteria();
        dc.add(Restrictions.eq("nrManifestoEntrega", nrManifesto));
        dc.add(Restrictions.eq("filial.idFilial", idFilial));

        return findByDetachedCriteria(dc);
    }

    public List<ManifestoEntrega> findDocumentosServico(Long idManifestoEntrega) {
        DetachedCriteria dc = createDetachedCriteria();
        dc.createAlias("manifestoEntregaDocumentos", "med");

        dc.add(Restrictions.eq("idManifestoEntrega", idManifestoEntrega));
        dc.setProjection(Projections.property("med.doctoServico.idDoctoServico"));

        return findByDetachedCriteria(dc);
    }

    /**
     * Retorna uma list de registros de Manifesto de Entrega com o ID do
     * Manifesto
     *
     * @param idManifesto
     * @return
     */
    public List<ManifestoEntrega> findManifestoEntregaByIdManifesto(Long idManifesto) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntrega.class);
        dc.add(Restrictions.eq("manifesto.id", idManifesto));

        return super.findByDetachedCriteria(dc);
    }

    /**
     * Consulta dados relativos a solicitacao de retirada do manifesto
     *
     * @param idSolicitacaoRetirada
     * @return
     */
    public List<Map<String, Object>> findManifestoByIdSolicitacaoRetirada(Long idSolicitacaoRetirada) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map(me.idManifestoEntrega", "idManifestoEntrega");
        sql.addProjection("me.nrManifestoEntrega", "nrManifestoEntrega");
        sql.addProjection("m.nrPreManifesto", "nrPreManifesto");
        sql.addProjection("f_m.sgFilial", "sgFilialManifesto");
        sql.addProjection("f_me.sgFilial", "sgFilialManifestoEntrega");
        sql.addProjection("m.dhEmissaoManifesto", "dhEmissaoManifesto");
        sql.addProjection("me.dhFechamento", "dhFechamento)");

        sql.addFrom(ManifestoEntrega.class.getName() + " me inner join me.manifesto m inner join m.filialByIdFilialOrigem f_m inner join me.filial f_me");

        sql.addCriteria("m.solicitacaoRetirada.id", "=", idSolicitacaoRetirada);

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     * Verifica se o documento de servico esta associada a algum manifesto nao
     * finalizado
     *
     * @param idDoctoServico
     * @return
     */
    public boolean validateDoctoServicoNaoFinalizado(Long idDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("count(*)");

        sql.addFrom(PreManifestoDocumento.class.getName() + " pmed inner join pmed.manifesto m");
        sql.addCustomCriteria("m.tpStatusManifesto in ('TC', 'PM', 'ME', 'CC', 'EC')");
        sql.addCriteria("pmed.doctoServico.idDoctoServico", "=", idDoctoServico);

        Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
        return (result.intValue() > 0);
    }

    /**
     * RowCount da grid da tela Reemitir/Cancelar Manifesto de Entrega
     *
     * @param parametros
     * @param findDefinition
     * @return
     */
    public Integer getRowCountGridCancelarManifesto(TypedFlatMap parametros) {
        SqlTemplate sql = montaSqlTemplateGridCancelarManifesto(parametros);
        return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false), sql.getCriteria());
    }

    /**
     * Consulta utilizada na grid da tela Reemitir/Cancelar Manifesto de Entrega
     *
     * @param parametros
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedGridCancelarManifesto(TypedFlatMap parametros, FindDefinition findDefinition) {
        SqlTemplate sql = montaSqlTemplateGridCancelarManifesto(parametros);

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("idManifestoEntrega", Hibernate.LONG);
                sqlQuery.addScalar("sgFilial", Hibernate.STRING);
                sqlQuery.addScalar("nrManifestoEntrega", Hibernate.INTEGER);
                sqlQuery.addScalar("dhEmissaoManifesto", Hibernate.custom(JodaTimeDateTimeUserType.class));
                sqlQuery.addScalar("sgFilialCC", Hibernate.STRING);
                sqlQuery.addScalar("nrControleCarga", Hibernate.LONG);
                sqlQuery.addScalar("nrFrota", Hibernate.STRING);
                sqlQuery.addScalar("nrIdentificador", Hibernate.STRING);
                sqlQuery.addScalar("nrRota", Hibernate.STRING);
                sqlQuery.addScalar("dsRota", Hibernate.STRING);
                sqlQuery.addScalar("quantidadeEntregas", Hibernate.INTEGER);

                Properties propertiesTpManifesto = new Properties();
                propertiesTpManifesto.put("domainName", "DM_TIPO_MANIFESTO_ENTREGA");
                sqlQuery.addScalar("tpManifestoEntrega", Hibernate.custom(DomainCompositeUserType.class, propertiesTpManifesto));
                Properties propertiesTpStatus = new Properties();
                propertiesTpStatus.put("domainName", "DM_STATUS_MANIFESTO");
                sqlQuery.addScalar("tpStatusManifesto", Hibernate.custom(DomainCompositeUserType.class, propertiesTpStatus));
            }
        };

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria(), csq);
    }

    /**
     * Sql da grid da tela Reemitir/Cancelar Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    private SqlTemplate montaSqlTemplateGridCancelarManifesto(TypedFlatMap parametros) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("M.TP_MANIFESTO_ENTREGA", "tpManifestoEntrega");
        sql.addProjection("ME.ID_MANIFESTO_ENTREGA", "idManifestoEntrega");
        sql.addProjection("F.SG_FILIAL", "sgFilial");
        sql.addProjection("ME.NR_MANIFESTO_ENTREGA", "nrManifestoEntrega");
        sql.addProjection("M.DH_EMISSAO_MANIFESTO", "dhEmissaoManifesto");
        sql.addProjection("F_CC.SG_FILIAL", "sgFilialCC");
        sql.addProjection("CC.NR_CONTROLE_CARGA", "nrControleCarga");
        sql.addProjection("MT.NR_FROTA", "nrFrota");
        sql.addProjection("MT.NR_IDENTIFICADOR", "nrIdentificador");
        sql.addProjection("RCE.NR_ROTA", "nrRota");
        sql.addProjection("RCE.DS_ROTA", "dsRota");
        sql.addProjection("(SELECT COUNT(MED_.ID_MANIFESTO_ENTREGA) FROM MANIFESTO_ENTREGA_DOCUMENTO MED_ WHERE ME.ID_MANIFESTO_ENTREGA=MED_.ID_MANIFESTO_ENTREGA)", "quantidadeEntregas");
        sql.addProjection("M.TP_STATUS_MANIFESTO", "tpStatusManifesto");

        sql.addFrom("MANIFESTO_ENTREGA", "ME");
        sql.addFrom("MANIFESTO", "M");
        sql.addFrom("CONTROLE_CARGA", "CC");
        sql.addFrom("FILIAL", "F_CC");
        sql.addFrom("MEIO_TRANSPORTE", "MT");
        sql.addFrom("ROTA_COLETA_ENTREGA", "RCE");
        sql.addFrom("FILIAL", "F");

        sql.addJoin("ME.ID_FILIAL", "F.ID_FILIAL");
        sql.addJoin("CC.ID_ROTA_COLETA_ENTREGA", "RCE.ID_ROTA_COLETA_ENTREGA(+)");
        sql.addJoin("CC.ID_TRANSPORTADO", "MT.ID_MEIO_TRANSPORTE(+)");
        sql.addJoin("CC.ID_FILIAL_ORIGEM", "F_CC.ID_FILIAL(+)");
        sql.addJoin("M.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA(+)");
        sql.addJoin("ME.ID_MANIFESTO_ENTREGA", "M.ID_MANIFESTO");

        sql.addCustomCriteria("M.TP_STATUS_MANIFESTO NOT IN  ('PM' , 'CA')");

        sql.addCriteria("F.ID_FILIAL", "=", parametros.getLong("filial.idFilial"));
        sql.addCriteria("M.TP_MANIFESTO_ENTREGA", "=", parametros.getString("manifesto.tpManifestoEntrega"));
        sql.addCriteria("MT.ID_MEIO_TRANSPORTE", "=", parametros.getLong("meioTransporteRodoviario.idMeioTransporte"));
        sql.addCriteria("RCE.ID_ROTA_COLETA_ENTREGA", "=", parametros.getLong("rotaColetaEntrega.idRotaColetaEntrega"));
        sql.addCriteria("ME.ID_MANIFESTO_ENTREGA", "=", parametros.getLong("manifestoEntrega.idManifestoEntrega"));
        sql.addCriteria("CC.ID_CONTROLE_CARGA", "=", parametros.getLong("controleCarga.idControleCarga"));
        sql.addCriteria("M.TP_STATUS_MANIFESTO", "=", parametros.getString("tpSituacaoManifesto"));
        sql.addCriteria("TRUNC(CAST(M.DH_EMISSAO_MANIFESTO AS DATE))", ">=", parametros.getYearMonthDay("dhEmissaoManifestoInicial"), YearMonthDay.class);
        sql.addCriteria("TRUNC(CAST(M.DH_EMISSAO_MANIFESTO AS DATE))", "<=", parametros.getYearMonthDay("dhEmissaoManifestoFinal"), YearMonthDay.class);

        sql.addOrderBy("ME.NR_MANIFESTO_ENTREGA");

        return sql;
    }

    /**
     * RowCount da grid da lookup de Manifesto de Entrega
     *
     * @param parametros
     * @param findDefinition
     * @return
     */
    public Integer getRowCountLookup(TypedFlatMap parametros) {
        SqlTemplate sql = montaSqlTemplateGridLookup(parametros);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false), sql.getCriteria());
    }

    /**
     * Consulta utilizada na grid da lookup de Manifesto de Entrega
     *
     * @param parametros
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedGridLookup(TypedFlatMap parametros, FindDefinition findDefinition) {
        SqlTemplate sql = montaSqlTemplateGridLookup(parametros);
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(),
                findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
    }

    /**
     * Consulta da lookup de Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    public List<Map<String, Object>> findLookupCustom(TypedFlatMap parametros) {
        SqlTemplate sql = montaSqlTemplateGridLookup(parametros);
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(), Integer.valueOf(1), Integer.valueOf(2), sql.getCriteria()).getList();
    }

    /**
     * Sql da grid da lookup de Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    private SqlTemplate montaSqlTemplateGridLookup(TypedFlatMap parametros) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map(me.idManifestoEntrega", "idManifestoEntrega");
        sql.addProjection("m.tpManifestoEntrega", "manifesto_tpManifestoEntrega");
        sql.addProjection("f.sgFilial", "filial_sgFilial");
        sql.addProjection("f.idFilial", "filial_idFilial");
        sql.addProjection("f_pes.nmFantasia", "filial_pessoa_nmFantasia");
        sql.addProjection("me.nrManifestoEntrega", "nrManifestoEntrega");
        sql.addProjection("m.dhEmissaoManifesto", "manifesto_dhEmissaoManifesto");
        sql.addProjection("rce.nrRota", "manifesto_controleCarga_rotaColetaEntrega_nrRota");
        sql.addProjection("rce.dsRota", "manifesto_controleCarga_rotaColetaEntrega_dsRota");
        sql.addProjection("m.tpStatusManifesto", "manifesto_tpStatusManifesto");
        sql.addProjection("mt.nrIdentificador", "manifesto_controleCarga_meioTransporteByIdTransportado_nrIdentificador");
        sql.addProjection("mt.nrFrota", "manifesto_controleCarga_meioTransporteByIdTransportado_nrFrota");
        sql.addProjection("prop.idProprietario", "manifesto_controleCarga_proprietario_idProprietario");
        sql.addProjection("pess.nmFantasia", "manifesto_controleCarga_proprietario_pessoa_nmFantasia");
        sql.addProjection("pess.nmPessoa", "manifesto_controleCarga_proprietario_pessoa_nmPessoa");
        sql.addProjection("pess.nrIdentificacao", "manifesto_controleCarga_proprietario_pessoa_nrIdentificacao");
        sql.addProjection("pess.tpIdentificacao", "manifesto_controleCarga_proprietario_pessoa_tpIdentificacao)");

        sql.addInnerJoin(getPersistentClass().getName(), "me");
        sql.addInnerJoin("me.manifesto", "m");
        sql.addInnerJoin("me.filial", "f");
        sql.addInnerJoin("f.pessoa", "f_pes");
        sql.addLeftOuterJoin("m.controleCarga", "cc");
        sql.addLeftOuterJoin("cc.meioTransporteByIdTransportado", "mt");
        sql.addLeftOuterJoin("cc.rotaColetaEntrega", "rce");
        sql.addLeftOuterJoin("cc.proprietario", "prop");
        sql.addLeftOuterJoin("prop.pessoa", "pess");

        sql.addCriteria("f.id", "=", parametros.getLong("filial.idFilial"));
        sql.addCriteria("me.nrManifestoEntrega", "=", parametros.getInteger("nrManifestoEntrega"));
        sql.addCriteria("m.tpManifestoEntrega", "=", parametros.getString("manifesto.tpManifestoEntrega"));
        sql.addCriteria("m.tpStatusManifesto", "=", parametros.getString("manifesto.tpStatusManifesto"));
        sql.addCriteria("mt.id", "=", parametros.getLong("manifesto.controleCarga.meioTransporteByIdTransportado.idMeioTransporte"));
        sql.addCriteria("rce.id", "=", parametros.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega"));
        sql.addCriteria("m.dhEmissaoManifesto.value", ">=", parametros.getYearMonthDay("manifesto.dhEmisssaoManifestoInicial"));
        sql.addCriteria("m.dhEmissaoManifesto.value", "<=", parametros.getYearMonthDay("manifesto.dhEmisssaoManifestoFinal"));
        sql.addCriteria("cc.id", "=", parametros.getLong("manifesto.controleCarga.idControleCarga"));
        sql.addCriteria("pess.nrIdentificacao", "=", parametros.getString("nrIdentificacao"));

        sql.addOrderBy("f.sgFilial");
        sql.addOrderBy("me.nrManifestoEntrega");

        return sql;
    }

    public ResultSetPage findPaginatedPreManifesto(TypedFlatMap criteria, FindDefinition findDef) {
        SqlTemplate sql = getSqlPaginatedPreManifesto(criteria);

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("idManifesto", Hibernate.LONG);
                sqlQuery.addScalar("sgFilialManifesto", Hibernate.STRING);
                sqlQuery.addScalar("nrPreManifesto", Hibernate.LONG);
                sqlQuery.addScalar("nrRota", Hibernate.INTEGER);
                sqlQuery.addScalar("dsRota", Hibernate.STRING);
                sqlQuery.addScalar("qtEntregas", Hibernate.INTEGER);
                sqlQuery.addScalar("sgFilialSolicitacao", Hibernate.STRING);
                sqlQuery.addScalar("nrSolicitacao", Hibernate.INTEGER);

                Properties propertiesTpManifesto = new Properties();
                propertiesTpManifesto.put("domainName", "DM_TIPO_MANIFESTO_ENTREGA");
                sqlQuery.addScalar("tpManifesto", Hibernate.custom(DomainCompositeUserType.class, propertiesTpManifesto));
                Properties propertiesTpStatus = new Properties();
                propertiesTpStatus.put("domainName", "DM_STATUS_MANIFESTO");
                sqlQuery.addScalar("tpStatusManifesto", Hibernate.custom(DomainCompositeUserType.class, propertiesTpStatus));

                sqlQuery.addScalar("nrManifestoEntrega", Hibernate.INTEGER);
                sqlQuery.addScalar("sgFilialManifestoEntrega", Hibernate.STRING);
                sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
            }
        };

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDef.getCurrentPage(),
                findDef.getPageSize(), sql.getCriteria(), csq);
    }

    public Integer getRowCountPreManifesto(TypedFlatMap criteria) {
        SqlTemplate sql = getSqlPaginatedPreManifesto(criteria);
        return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false), sql.getCriteria());
    }

    private SqlTemplate getSqlPaginatedPreManifesto(TypedFlatMap parametros) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("MANIF.ID_MANIFESTO", "idManifesto");
        sql.addProjection("F.SG_FILIAL", "sgFilialManifesto");
        sql.addProjection("MANIF.NR_PRE_MANIFESTO", "nrPreManifesto");
        sql.addProjection("ROTA.NR_ROTA", "nrRota");
        sql.addProjection("ROTA.DS_ROTA", "dsRota");
        sql.addProjection("(SELECT COUNT(*) FROM PRE_MANIFESTO_DOCUMENTO PM "
                + " WHERE PM.ID_MANIFESTO = MANIF.ID_MANIFESTO)", "qtEntregas");
        sql.addProjection("FSR.SG_FILIAL", "sgFilialSolicitacao");
        sql.addProjection("SR.NR_SOLICITACAO_RETIRADA", "nrSolicitacao");
        sql.addProjection("MANIF.TP_MANIFESTO_ENTREGA", "tpManifesto");
        sql.addProjection("MANIF.TP_STATUS_MANIFESTO", "tpStatusManifesto");
        sql.addProjection("ME.NR_MANIFESTO_ENTREGA", "nrManifestoEntrega");
        sql.addProjection("F_ME.SG_FILIAL", "sgFilialManifestoEntrega");
        sql.addProjection("MANIF.DH_EMISSAO_MANIFESTO", "dhEmissao");

        StringBuilder sqlFrom = new StringBuilder()
                .append(" MANIFESTO MANIF ")
                .append(" INNER JOIN FILIAL F ON MANIF.ID_FILIAL_ORIGEM = F.ID_FILIAL ")
                .append("  LEFT JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = MANIF.ID_CONTROLE_CARGA ")
                .append("  LEFT JOIN ROTA_COLETA_ENTREGA ROTA ON CC.ID_ROTA_COLETA_ENTREGA = ROTA.ID_ROTA_COLETA_ENTREGA ")
                .append("  LEFT JOIN SOLICITACAO_RETIRADA SR ON SR.ID_SOLICITACAO_RETIRADA = MANIF.ID_SOLICITACAO_RETIRADA ")
                .append("  LEFT JOIN FILIAL FSR ON FSR.ID_FILIAL = SR.ID_FILIAL_RETIRADA ")
                .append("  LEFT JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MANIF.ID_MANIFESTO ")
                .append("  LEFT JOIN FILIAL F_ME ON F_ME.ID_FILIAL = ME.ID_FILIAL ");
        sql.addFrom(sqlFrom.toString());

        sql.addCriteria("MANIF.TP_MANIFESTO", "=", "E");

        sql.addCriteria("F.ID_FILIAL", "=", parametros.getLong("filial.idFilial"));
        sql.addCriteria("MANIF.TP_MANIFESTO_ENTREGA", "=", parametros.getString("tpManifestoEntrega"));
        sql.addCriteria("ROTA.ID_ROTA_COLETA_ENTREGA", "=",
                parametros.getLong("controleCarga.rotaColetaEntrega.idRotaColetaEntrega"));

        Long idManifestoEntrega = parametros.getLong("manifestoEntrega.idManifestoEntrega");
        if (idManifestoEntrega != null) {
            sql.addCriteria("MANIF.ID_MANIFESTO", "=", idManifestoEntrega);
        } else {
            sql.addCustomCriteria("(MANIF.TP_STATUS_MANIFESTO IN (?,?) OR (MANIF.TP_STATUS_MANIFESTO = ? AND MANIF.TP_MANIFESTO_ENTREGA IN (?,?))) ", new Object[]{"CC", "ME", "PM", "PR", "CR"});
        }

        Long idManifesto = parametros.getLong("manifesto.idManifesto");
        sql.addCriteria("MANIF.ID_MANIFESTO", "=", idManifesto);

        sql.addOrderBy("F.SG_FILIAL");
        sql.addOrderBy("MANIF.NR_PRE_MANIFESTO");

        return sql;
    }

    /**
     * Consulta manifesto a partir de um id recebido. Implementado para otimizar
     * alguns joins.
     *
     * @param id
     * @return
     */
    public Manifesto findByIdEmitirManifesto(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O parâmetro 'id' não pode ser null.");
        }

        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("M");

        StringBuilder hqlFrom = new StringBuilder()
                .append(Manifesto.class.getName()).append(" as M ")
                .append(" inner join fetch M.filialByIdFilialOrigem as FO ")
                .append(" inner join fetch FO.pessoa as PO ")
                .append("  left join fetch M.controleCarga as CC ")
                .append("  left join fetch CC.filialByIdFilialOrigem ")
                .append("  left join fetch CC.rotaColetaEntrega RCE ")
                .append("  left join fetch M.solicitacaoRetirada as SR ")
                .append("  left join fetch SR.filial FR ")
                .append("  left join fetch FR.pessoa PR ")
                .append("  left join fetch M.manifestoEntrega ME ")
                .append("  left join fetch ME.filial as f_ME ");

        hql.addFrom(hqlFrom.toString());
        hql.addCriteria("M.id", "=", id);

        return (Manifesto) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    public List<ManifestoEntregaDocumento> findManifestoEntregaDocumentosByEntrega(Long idManifestoEntrega, String tpSituacaoFatura) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("MED");
        StringBuilder hqlFrom = new StringBuilder()
                .append(ManifestoEntregaDocumento.class.getName()).append(" as MED ")
                .append(" inner join MED.manifestoEntrega as ME ")
                .append(" inner join MED.doctoServico as DS ");
        hql.addFrom(hqlFrom.toString());

        hql.addCriteria("ME.idManifestoEntrega", "=", idManifestoEntrega);

        SqlTemplate subHql = new SqlTemplate();
        subHql.addProjection("IF.id");
        subHql.addFrom(ItemFatura.class.getName() + " as IF");
        subHql.addFrom(DevedorDocServFat.class.getName() + " as DevF");
        subHql.addFrom(Fatura.class.getName() + " as F");
        subHql.addJoin("DevF.doctoServico.id", "DS.id");
        subHql.addJoin("IF.devedorDocServFat.id", "DevF.id");
        subHql.addJoin("IF.fatura.id", "F.id");
        subHql.addJoin("F.manifestoEntrega.id", "ME.id");
        subHql.addCustomCriteria("F.tpSituacaoFatura = ?");

        hql.addCustomCriteria("exists(" + subHql.getSql() + ")");
        hql.addCriteriaValue(tpSituacaoFatura);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public boolean validateIfExistsDoctoServicoInRegistroDocumentoEntrega(
            Long idDoctoServico, Long idTipoDocumentoEntrega) {
        DetachedCriteria dc = DetachedCriteria.forClass(RegistroDocumentoEntrega.class, "RDE");
        dc.setProjection(Projections.count("RDE.id"));

        dc.add(Restrictions.eq("RDE.doctoServico.id", idDoctoServico));
        dc.add(Restrictions.eq("RDE.tipoDocumentoEntrega.id", idTipoDocumentoEntrega));
        dc.add(Restrictions.eq("RDE.blComprovanteRecolhido", Boolean.FALSE));

        return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc).intValue() > 0;
    }

    /**
     * Valida se existe documento de cobrança do tipo informado.
     *
     * @param idManifestoEntrega
     * @param tpDocumentoCobranca
     * @return boolean true se existe documentos de cobrança.
     */
    public boolean validateIfExistsDocumentoCobranca(Long idManifestoEntrega, String tpDocumentoCobranca) {
        SqlTemplate hql = new SqlTemplate();
        hql.addFrom(ManifestoEntregaDocumento.class.getName(), "MED");
        hql.addCriteria("MED.manifestoEntrega.id", "=", idManifestoEntrega);
        hql.addCriteria("MED.tpDocumentoCobranca", "=", tpDocumentoCobranca);

        Integer count = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
        return !count.equals(Integer.valueOf(0));
    }

    public boolean validateIfExistsReembolsoNaoReembolsado(Long idManifestoEntrega) {
        return validateIfExistsReembolso(idManifestoEntrega, false);
    }

    public boolean validateIfExistsReembolso(Long idManifestoEntrega) {
        return validateIfExistsReembolso(idManifestoEntrega, true);
    }

    private boolean validateIfExistsReembolso(Long idManifestoEntrega, boolean blReembolsado) {
        StringBuilder hql = new StringBuilder()
                .append(" from ").append(ManifestoEntregaDocumento.class.getName()).append(" MED ")
                .append(" WHERE MED.manifestoEntrega.id = ? ")
                .append("   AND EXISTS (SELECT C.id FROM ").append(Conhecimento.class.getName()).append(" C ")
                .append("       WHERE C.blReembolso = ? ")
                .append("         AND C.id = MED.doctoServico.id ")
                .append("         AND MED.awb is null ");

        if (!blReembolsado) {
            hql.append(" AND NOT EXISTS (SELECT RR.id FROM ").append(ReciboReembolso.class.getName()).append(" RR ")
                    .append(" WHERE RR.doctoServicoByIdDoctoServReembolsado.id = C.id)");
        }

        hql.append(")");

        return (getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{idManifestoEntrega, Boolean.TRUE})).intValue() > 0;
    }

    /**
     * Encontra comprovantes do devedor original.
     *
     * @param idDoctoServico
     * @param subselectRegistroDocumentoEntrega
     * @return
     */
    public List<DocumentoCliente> findComprovantesDevedorOriginal(Long idDoctoServico, boolean subselectRegistroDocumentoEntrega) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("DC");
        hql.addFrom(DocumentoCliente.class.getName() + " as DC "
                + " inner join fetch DC.tipoDocumentoEntrega as TDE ");

        SqlTemplate subHql = new SqlTemplate();
        subHql.addProjection("DDSF.id");
        subHql.addFrom(DevedorDocServFat.class.getName() + " as DDSF");
        subHql.addFrom(DoctoServico.class.getName() + " as DS");
        subHql.addJoin("DS.idDoctoServico", "DDSF.doctoServico.id");
        subHql.addJoin("DDSF.cliente.id", "DC.cliente.id");
        subHql.addCustomCriteria("trunc(cast(DS.dhEmissao.value as date)) >= DC.dtVigenciaInicial");
        subHql.addCustomCriteria("trunc(cast(DS.dhEmissao.value as date)) <= DC.dtVigenciaFinal");
        subHql.addCustomCriteria("DS.idDoctoServico = ?");

        hql.addCustomCriteria("exists(" + subHql.getSql() + ")");
        hql.addCriteriaValue(idDoctoServico);

        if (subselectRegistroDocumentoEntrega) {
            SqlTemplate subHql2 = new SqlTemplate();
            subHql2.addProjection("RDE.id");
            subHql2.addFrom(RegistroDocumentoEntrega.class.getName() + " as RDE");
            subHql2.addJoin("RDE.tipoDocumentoEntrega.id", "TDE.id");
            subHql2.addCustomCriteria("RDE.doctoServico.id = ?");

            hql.addCustomCriteria("not exists(" + subHql2.getSql() + ")");
            hql.addCriteriaValue(idDoctoServico);
        }
        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public List<ManifestoEntrega> findManifestoEntregaAtivoByControleCarga(Long idControleCarga) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("me");

        hql.addInnerJoin(getPersistentClass().getName(), "me");
        hql.addInnerJoin("me.manifesto", "m");

        hql.addCriteria("m.controleCarga.id", "=", idControleCarga);
        hql.addCriteria("m.tpStatusManifesto", "<>", "CA");

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

//  ######################################################################################################################
//      Métodos utilizados na tela Consulta Manifesto
//  ######################################################################################################################
    /**
     * Find da consulta paginada da tela 'Consultar Manifesto'.
     *
     * @param criteria
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedConsultaManifesto(TypedFlatMap criteria, FindDefinition findDef) {
        SqlTemplate sql = getSqlPaginatedConsultaManifesto(criteria);

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("idManifestoEntrega", Hibernate.LONG);
                sqlQuery.addScalar("sgFilialManifesto", Hibernate.STRING);
                sqlQuery.addScalar("nrManifestoEntrega", Hibernate.INTEGER);

                Properties propertiesTpManifesto = new Properties();
                propertiesTpManifesto.put("domainName", "DM_TIPO_MANIFESTO_ENTREGA");
                sqlQuery.addScalar("tpManifesto", Hibernate.custom(DomainCompositeUserType.class, propertiesTpManifesto));

                sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));

                sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
                sqlQuery.addScalar("nrControleCarga", Hibernate.LONG);
                sqlQuery.addScalar("nrFrota", Hibernate.STRING);
                sqlQuery.addScalar("nrIdentificador", Hibernate.STRING);
                sqlQuery.addScalar("nrFrotaSemi", Hibernate.STRING);
                sqlQuery.addScalar("nrIdentificadorSemi", Hibernate.STRING);
                sqlQuery.addScalar("dhSaidaColetaEntrega", Hibernate.custom(JodaTimeDateTimeUserType.class));
                sqlQuery.addScalar("dhChegadaColetaEntrega", Hibernate.custom(JodaTimeDateTimeUserType.class));
                sqlQuery.addScalar("dhFechamento", Hibernate.custom(JodaTimeDateTimeUserType.class));

                Properties propertiesTpStatus = new Properties();
                propertiesTpStatus.put("domainName", "DM_STATUS_MANIFESTO");
                sqlQuery.addScalar("tpStatusManifesto", Hibernate.custom(DomainCompositeUserType.class, propertiesTpStatus));

                sqlQuery.addScalar("dsEquipe", Hibernate.STRING);
                sqlQuery.addScalar("qtDocumentos", Hibernate.INTEGER);

                sqlQuery.addScalar("nrRota", Hibernate.SHORT);
                sqlQuery.addScalar("dsRota", Hibernate.STRING);

                sqlQuery.addScalar("dsRegiaoColetaEntregaFil", Hibernate.STRING);
                sqlQuery.addScalar("idFilialManifesto", Hibernate.LONG);
                sqlQuery.addScalar("nmFantasiaFilialManifesto", Hibernate.STRING);
            }
        };

        return getAdsmHibernateTemplate().findPaginatedBySql(sql.getSql(), findDef.getCurrentPage(),
                findDef.getPageSize(), sql.getCriteria(), csq);
    }

    public List<String> findEnderecosManifestosEntregas(final Long idControleCarga) {
        final StringBuilder sql = new StringBuilder()
                .append("SELECT ds.ds_Endereco_Entrega_Real as ds_endereco_entrega ")
                .append("FROM DOCTO_SERVICO ds ")
                .append("inner join nota_credito_docto ncd on ncd.id_docto_servico = ds.id_docto_servico ")
                .append("inner join manifesto m on m.id_manifesto = ncd.id_manifesto_entrega ")
                .append("WHERE m.id_controle_carga = :idControleCarga");

        final ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("ds_endereco_entrega", Hibernate.STRING);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                configureSqlQuery.configQuery(query);

                query.setLong("idControleCarga", idControleCarga);

                return query.list();
            }
        };

        List<String> list = getHibernateTemplate().executeFind(hcb);
        return list;
    }

    /**
     * Retorna o número de registros encontrados na consulta da grid da tela
     * 'Consultar Manifesto'.
     *
     * @param criteria
     * @return
     */
    public Integer getRowCountConsultaManifesto(TypedFlatMap criteria) {
        SqlTemplate sql = getSqlPaginatedConsultaManifesto(criteria);
        return getAdsmHibernateTemplate().getRowCountBySql(sql.getSql(false), sql.getCriteria());
    }

    /**
     * Método que cria o sql da consulta.
     *
     * @see findPaginatedConsultaManifesto
     * @see getRowCountConsultaManifesto
     * @param parametros
     * @return
     */
    private SqlTemplate getSqlPaginatedConsultaManifesto(TypedFlatMap parametros) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("ME.ID_MANIFESTO_ENTREGA", "idManifestoEntrega");
        sql.addProjection("F.SG_FILIAL", "sgFilialManifesto");
        sql.addProjection("ME.NR_MANIFESTO_ENTREGA", "nrManifestoEntrega");
        sql.addProjection("MANIF.TP_MANIFESTO_ENTREGA", "tpManifesto");
        sql.addProjection("MANIF.DH_EMISSAO_MANIFESTO", "dhEmissao");
        sql.addProjection("FO.SG_FILIAL", "sgFilialOrigem");
        sql.addProjection("CC.NR_CONTROLE_CARGA", "nrControleCarga");
        sql.addProjection("MEIO.NR_FROTA", "nrFrota");
        sql.addProjection("MEIO.NR_IDENTIFICADOR", "nrIdentificador");
        sql.addProjection("SEMI.NR_FROTA", "nrFrotaSemi");
        sql.addProjection("SEMI.NR_IDENTIFICADOR", "nrIdentificadorSemi");
        sql.addProjection("CC.DH_SAIDA_COLETA_ENTREGA", "dhSaidaColetaEntrega");
        sql.addProjection("CC.DH_CHEGADA_COLETA_ENTREGA", "dhChegadaColetaEntrega");
        sql.addProjection("ME.DH_FECHAMENTO", "dhFechamento");
        sql.addProjection("MANIF.TP_STATUS_MANIFESTO", "tpStatusManifesto");
        sql.addProjection(this.getSqlEquipe(), "dsEquipe");
        sql.addProjection(this.getSqlCountDocumentos(), "qtDocumentos");
        sql.addProjection("RCE.NR_ROTA", "nrRota");
        sql.addProjection("RCE.DS_ROTA", "dsRota");
        sql.addProjection("F.ID_FILIAL", "idFilialManifesto");
        sql.addProjection("P.NM_FANTASIA", "nmFantasiaFilialManifesto");

        SqlTemplate sqlRegiao = new SqlTemplate();
        sqlRegiao.addProjection("Max(RCEF.DS_REGIAO_COLETA_ENTREGA_FIL)");
        sqlRegiao.addFrom("REGIAO_FILIAL_ROTA_COL_ENT", "REG_CE");
        sqlRegiao.addFrom("REGIAO_COLETA_ENTREGA_FIL", "RCEF");
        sqlRegiao.addJoin("RCEF.ID_REGIAO_COLETA_ENTREGA_FIL", "REG_CE.ID_REGIAO_COLETA_ENTREGA_FIL");
        sqlRegiao.addJoin("REG_CE.ID_ROTA_COLETA_ENTREGA", "RCE.ID_ROTA_COLETA_ENTREGA");
        StringBuilder exists = new StringBuilder()
                .append(" SELECT CC.* FROM CONTROLE_CARGA CC ")
                .append(" WHERE CC.ID_ROTA_COLETA_ENTREGA= REG_CE.ID_ROTA_COLETA_ENTREGA ")
                .append("   AND CC.DH_GERACAO IS NOT NULL ")
                .append("   AND Trunc(REG_CE.DT_VIGENCIA_INICIAL) <= Trunc(Cast(CC.DH_GERACAO AS DATE)) ")
                .append("   AND Trunc(REG_CE.DT_VIGENCIA_FINAL)   >= Trunc(Cast(CC.DH_GERACAO AS DATE)) ");
        sqlRegiao.addCustomCriteria("exists (" + exists.toString() + ")");
        sql.addProjection("(" + sqlRegiao.getSql() + ")", "dsRegiaoColetaEntregaFil");

        StringBuilder sqlFrom = new StringBuilder()
                .append(" MANIFESTO_ENTREGA ME")
                .append(" INNER JOIN MANIFESTO MANIF ON MANIF.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA")
                .append(" INNER JOIN FILIAL F ON F.ID_FILIAL = ME.ID_FILIAL")
                .append(" INNER JOIN PESSOA P ON F.ID_FILIAL = P.ID_PESSOA")
                .append(" LEFT JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = MANIF.ID_CONTROLE_CARGA")
                .append(" LEFT JOIN FILIAL FO ON FO.ID_FILIAL = CC.ID_FILIAL_ORIGEM")
                .append("  LEFT JOIN MEIO_TRANSPORTE MEIO ON MEIO.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO")
                .append("  LEFT JOIN MEIO_TRANSPORTE SEMI ON SEMI.ID_MEIO_TRANSPORTE = CC.ID_SEMI_REBOCADO")
                .append("  LEFT JOIN ROTA_COLETA_ENTREGA RCE ON RCE.ID_ROTA_COLETA_ENTREGA = CC.ID_ROTA_COLETA_ENTREGA");

        sql.addFrom(sqlFrom.toString());

        sql.addCriteria("F.ID_FILIAL", "=", parametros.getLong("filial.idFilial"));
        sql.addCriteria("ME.NR_MANIFESTO_ENTREGA", "=", parametros.getInteger("nrManifestoEntrega"));
        sql.addCriteria("MANIF.TP_STATUS_MANIFESTO", "=", parametros.getString("tpSituacaoManifesto"));
        sql.addCriteria("MANIF.TP_MANIFESTO_ENTREGA", "=", parametros.getString("tpManifestoEntrega"));
        sql.addCriteria("CC.ID_CONTROLE_CARGA", "=",
                parametros.getLong("manifesto.controleCarga.idControleCarga"));
        sql.addCriteria("CC.ID_ROTA_COLETA_ENTREGA", "=",
                parametros.getLong("manifesto.controleCarga.rotaColetaEntrega.idRotaColetaEntrega"));

        Long idDoctoServico = parametros.getLong("idDoctoServico");
        if (idDoctoServico != null) {
            sql.addCustomCriteria("EXISTS" + this.getSqlDocumentos(), idDoctoServico);
        }

        Long idRegiao = parametros.getLong("regiaoColetaEntregaFil.idRegiaoColetaEntregaFil");
        if (idRegiao != null) {
            sql.addCustomCriteria("EXISTS" + this.getSqlRegiao(), idRegiao);
        }

        Long idEquipe = parametros.getLong("equipe.idEquipe");
        if (idEquipe != null) {
            sql.addCustomCriteria("EXISTS" + this.getSqlEquipeCriteria(), idEquipe);
        }

        sql.addCriteria("MEIO.ID_MEIO_TRANSPORTE", "=", parametros.getLong("meioTransporte.idMeioTransporte"));

        sql.addCriteria("Trunc(MANIF.DH_EMISSAO_MANIFESTO)", ">=", parametros.getYearMonthDay("dhEmisssaoManifestoInicial"));
        sql.addCriteria("Trunc(MANIF.DH_EMISSAO_MANIFESTO)", "<=", parametros.getYearMonthDay("dhEmisssaoManifestoFinal"));
        sql.addCriteria("Trunc(ME.DH_FECHAMENTO)", ">=", parametros.getYearMonthDay("dhFechamentoInicial"));
        sql.addCriteria("Trunc(ME.DH_FECHAMENTO)", "<=", parametros.getYearMonthDay("dhFechamentoFinal"));

        sql.addOrderBy("F.SG_FILIAL");
        sql.addOrderBy("ME.NR_MANIFESTO_ENTREGA");

        return sql;
    }

    /**
     * Cria a subquery que encontra o número de documentos do manifesto.
     *
     * @return
     */
    private String getSqlCountDocumentos() {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("Count(*)");
        sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO", "MED");
        sql.addJoin("MED.ID_MANIFESTO_ENTREGA", "ME.ID_MANIFESTO_ENTREGA");

        return "(" + sql.getSql() + ")";
    }

    /**
     * Cria a subquery que retorna uma subquery para restringir a consulta por
     * um documento específico.
     *
     * @return
     */
    private String getSqlDocumentos() {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("*");
        sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO", "MED");
        sql.addJoin("MED.ID_MANIFESTO_ENTREGA", "ME.ID_MANIFESTO_ENTREGA");
        sql.addCustomCriteria("MED.ID_DOCTO_SERVICO = ?");

        return "(" + sql.getSql() + ")";
    }

    private String getHqlDocumentos() {
        SqlTemplate sql = new SqlTemplate();

        sql.addFrom(ManifestoEntregaDocumento.class.getName(), "MED_E");

        sql.addCustomCriteria("MED_E.manifestoEntrega.idManifestoEntrega = me.idManifestoEntrega");
        sql.addCustomCriteria("MED_E.doctoServico.idDoctoServico = ?");

        return "(" + sql.getSql() + ")";
    }

    /**
     * Cria a subquery que retorna uma subquery para restringir a consulta por
     * uma região.
     *
     * @return
     */
    private String getSqlRegiao() {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("RCE.ID_ROTA_COLETA_ENTREGA");
        sql.addFrom("REGIAO_FILIAL_ROTA_COL_ENT", "RCE");
        sql.addJoin("RCE.ID_ROTA_COLETA_ENTREGA", "CC.ID_ROTA_COLETA_ENTREGA");
        sql.addCustomCriteria("RCE.ID_REGIAO_COLETA_ENTREGA_FIL = ?");
        sql.addCustomCriteria("CC.DH_GERACAO IS NOT NULL ");
        sql.addCustomCriteria("Trunc(RCE.DT_VIGENCIA_INICIAL) <= Trunc(Cast(CC.DH_GERACAO AS DATE)) ");
        sql.addCustomCriteria("Trunc(RCE.DT_VIGENCIA_FINAL) >= Trunc(Cast(CC.DH_GERACAO AS DATE)) ");

        return "(" + sql.getSql() + ")";
    }

    /**
     * Cria a Subquery para retornar a primeira equipe encontrada na lista de
     * equipes do controle de carga do manifesto.
     *
     * @return
     */
    private String getSqlEquipe() {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("Max(EQ.DS_EQUIPE)");

        sql.addFrom("EQUIPE", "EQ");
        sql.addFrom("EQUIPE_OPERACAO", "EQOP");

        sql.addJoin("EQ.ID_EQUIPE", "EQOP.ID_EQUIPE");
        sql.addJoin("EQOP.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
        sql.addCustomCriteria("EQOP.ID_CARREGAMENTO_DESCARGA IS NULL");

        return "(" + sql.getSql() + ")";
    }

    /**
     * Cria a Subquery para retornar a primeira equipe encontrada na lista de
     * equipes do controle de carga do manifesto.
     *
     * @return
     */
    private String getSqlEquipeCriteria() {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("*");

        sql.addFrom("EQUIPE", "EQ");
        sql.addFrom("EQUIPE_OPERACAO", "EQOP");

        sql.addJoin("EQ.ID_EQUIPE", "EQOP.ID_EQUIPE");
        sql.addJoin("EQOP.ID_CONTROLE_CARGA", "CC.ID_CONTROLE_CARGA");
        sql.addCustomCriteria("EQOP.ID_CARREGAMENTO_DESCARGA IS NULL");
        sql.addCustomCriteria("EQ.ID_EQUIPE = ?");

        return "(" + sql.getSql() + ")";
    }

    /**
     * Consulta responsável pelo detalhamento de um registro na tela 'Consultar
     * Manifesto'.
     *
     * @param idManifestoEntrega
     * @return Map com os parâmtros a retornarem para tela.
     */
    public Map<String, Object> findByIdConsultaManifesto(Long idManifestoEntrega) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(ME.idManifestoEntrega", "idManifestoEntrega");
        hql.addProjection("F.idFilial", "filial_idFilial");
        hql.addProjection("F.sgFilial", "filial_sgFilial");
        hql.addProjection("P.nmFantasia", "nmFantasiaFilial");
        hql.addProjection("ME.nrManifestoEntrega", "nrManifestoEntrega");
        hql.addProjection("MANIF.tpStatusManifesto", "tpStatusManifesto");
        hql.addProjection("MANIF.tpManifestoEntrega", "tpManifestoEntrega");
        hql.addProjection("MANIF.dhEmissaoManifesto", "dhEmissao");
        hql.addProjection("ME.dhFechamento", "dhFechamento");
        hql.addProjection("FO.idFilial", "controleCarga_idFilial");
        hql.addProjection("FO.sgFilial", "controleCarga_sgFilial");
        hql.addProjection("PO.nmFantasia", "controleCarga_nmFantasia");
        hql.addProjection("CC.idControleCarga", "controleCarga_idControleCarga");
        hql.addProjection("CC.nrControleCarga", "controleCarga_nrControleCarga");
        hql.addProjection("RCE.idRotaColetaEntrega", "rotaColetaEntrega_idRotaColetaEntrega");
        hql.addProjection("RCE.nrRota", "rotaColetaEntrega_nrRota");
        hql.addProjection("RCE.dsRota", "rotaColetaEntrega_dsRota");
        hql.addProjection("TTCE.dsTipoTabelaColetaEntrega", "dsTipoTabelaColetaEntrega");
        hql.addProjection("FNC.sgFilial", "notaCredito_sgFilial");
        hql.addProjection("NC.nrNotaCredito", "notaCredito_nrNotaCredito");
        hql.addProjection("MEIO.nrFrota", "meioTransporte_nrFrota");
        hql.addProjection("MEIO.nrIdentificador", "meioTransporte_nrIdentificador");
        hql.addProjection("SEMI.nrFrota", "semiReboque_nrFrota");
        hql.addProjection("SEMI.nrIdentificador", "semiReboque_nrIdentificador");
        hql.addProjection("CC.dhSaidaColetaEntrega", "dhSaidaColetaEntrega");
        hql.addProjection("CC.dhChegadaColetaEntrega", "dhChegadaColetaEntrega");
        hql.addProjection("P_CLI.tpIdentificacao", "cliente_pessoa_tpIdentificacao");
        hql.addProjection("P_CLI.nrIdentificacao", "cliente_pessoa_nrIdentificacao");
        hql.addProjection("P_CLI.nmPessoa", "cliente_pessoa_nmPessoa)");

        StringBuilder hqlFrom = new StringBuilder()
                .append(ManifestoEntrega.class.getName()).append(" ME ")
                .append(" inner join ME.manifesto as MANIF ")
                .append(" inner join ME.filial as F ")
                .append(" inner join F.pessoa as P ")
                .append("  left join MANIF.controleCarga as CC ")
                .append("  left join CC.rotaColetaEntrega as RCE ")
                .append("  left join CC.filialByIdFilialOrigem as FO ")
                .append("  left join FO.pessoa as PO ")
                .append("  left join CC.meioTransporteByIdTransportado as MEIO ")
                .append("  left join CC.meioTransporteByIdSemiRebocado as SEMI ")
                .append("  left join CC.tipoTabelaColetaEntrega as TTCE ")
                .append("  left join CC.notaCredito as NC ")
                .append("  left join NC.filial as FNC ")
                .append("  left join MANIF.cliente as CLI ")
                .append("  left join CLI.pessoa as P_CLI ");

        hql.addFrom(hqlFrom.toString());

        hql.addCustomCriteria("ME.idManifestoEntrega = ?", idManifestoEntrega);

        hql.addOrderBy("F.sgFilial");
        hql.addOrderBy("ME.nrManifestoEntrega");

        return (Map<String, Object>) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    /**
     * Retorna on integrantes da equipe de um controle de carga.
     *
     * @param idControleCarga
     * @param blEmpresa
     * @return
     */
    public List<Map<String, Object>> findIntegrantesEquipeControleCarga(Long idControleCarga) {
        SqlTemplate hql = new SqlTemplate();
        // Mantem-se o alias para não gerar muito retrabalho (03/01/2007)
        hql.addProjection("new Map(U.nmUsuario as nmPessoa)");

        StringBuilder hqlFrom = new StringBuilder()
                .append(IntegranteEqOperac.class.getName()).append(" IEOP ")
                .append(" inner join IEOP.equipeOperacao as EQOP ")
                .append(" inner join IEOP.usuario as U ");

        hql.addFrom(hqlFrom.toString());

        hql.addCustomCriteria("EQOP.controleCarga.id = ?");
        hql.addCustomCriteria("EQOP.carregamentoDescarga.id is NULL");

        return getAdsmHibernateTemplate().find(hql.getSql(), idControleCarga);
    }

    /**
     * Encontra a quantidade de documentos associados ao manifesto.
     *
     * @param idManifestoEntrega
     * @return
     */
    public Integer findQtDocumentos(Long idManifestoEntrega) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med");
        dc.setProjection(Projections.rowCount());
        dc.add(Restrictions.eq("med.manifestoEntrega.id", idManifestoEntrega));

        return (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    /**
     * Encontra a quantidade de ocorrências de entrega do manifesto.
     *
     * @param idManifestoEntrega
     * @return
     */
    public Integer findQtEntregas(Long idManifestoEntrega) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med");
        dc.setProjection(Projections.rowCount());
        dc.createAlias("med.ocorrenciaEntrega", "oe");
        dc.add(Restrictions.eq("med.manifestoEntrega.id", idManifestoEntrega));
        dc.add(Restrictions.or(Restrictions.eq("oe.tpOcorrencia", "E"), Restrictions.eq("oe.tpOcorrencia", "A")));

        return (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    /**
     * Encontra a região vigente de acordo com o controle de carga de uma rota
     * de coleta entrega. Informação apresentada no detalhamento de 'Consulta
     * Manifesto'.
     *
     * @param idRotaColetaEntrega
     * @return
     */
    public List<RegiaoFilialRotaColEnt> findRegioesRotaColetaEntrega(Long idRotaColetaEntrega) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("RCEF");

        hql.addFrom(RegiaoFilialRotaColEnt.class.getName(),
                "REG_CE inner join REG_CE.regiaoColetaEntregaFil as  RCEF");

        hql.addCriteria("REG_CE.rotaColetaEntrega.id", "=", idRotaColetaEntrega);

        StringBuilder exists = new StringBuilder()
                .append(" select CC.id from ").append(ControleCarga.class.getName()).append(" CC ")
                .append(" where CC.rotaColetaEntrega.id = REG_CE.rotaColetaEntrega.id ")
                .append("   and CC.dhGeracao.value is not null ")
                .append("   and trunc(REG_CE.dtVigenciaInicial) <= trunc(cast(CC.dhGeracao.value as date)) ")
                .append("   and trunc(REG_CE.dtVigenciaFinal) >= trunc(cast(CC.dhGeracao.value as date)) ");

        hql.addCustomCriteria("exists (" + exists.toString() + ")");

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    /**
     * Encontra os lacres de um controle de carga. Este método não está na
     * LacreControleCargaService por ser simples e específico. Já foi criado por
     * PERFORMANCE e não deve ser alterado. Se não fosse por performance,
     * poderia-se trabalhar diretamente persistindo objetos no Java.
     *
     * @param idControleCarga
     * @return List com Maps contendo o atributo nrLacre.
     */
    public List<Map<String, Object>> findLacresControleCarga(Long idControleCarga) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("new Map(L.nrLacres as nrLacres, L.tpStatusLacre as tpStatusLacre)");
        hql.addFrom(LacreControleCarga.class.getName(), "L");
        hql.addCustomCriteria("L.controleCarga.id = ?");

        hql.addOrderBy("L.nrLacres");
        return getAdsmHibernateTemplate().find(hql.getSql(), idControleCarga);
    }

    /**
     * Retorna a descrição da primeira equipe encontrada na lista de equipes do
     * controle de carga do manifesto.
     *
     * @return
     */
    public String findDsEquipeByControleCarga(Long idControleCarga) {
        if (idControleCarga == null) {
            return "";
        }

        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("Max(EQ.dsEquipe)");
        sql.addFrom(EquipeOperacao.class.getName() + " EQOP"
                + " inner join EQOP.equipe as EQ ");

        sql.addCustomCriteria("EQOP.controleCarga.id = ?", idControleCarga);
        sql.addCustomCriteria("EQOP.carregamentoDescarga IS NULL");

        List<String> l = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

        return l.isEmpty() ? "" : l.get(0);
    }

    /**
     * Consulta as informações a serem apresentadas na grid da 'Consulta de
     * Manifestos / Documentos'.
     *
     * @param idManifestoEntrega
     * @param fDef
     * @return
     */
    public ResultSetPage findPaginatedDoctosServico(Long idManifestoEntrega, FindDefinition fDef) {
        SqlTemplate hql = getSqlDoctosServicos(idManifestoEntrega);
        return getAdsmHibernateTemplate().findPaginated(hql.getSql(), fDef.getCurrentPage(), fDef.getPageSize(), hql.getCriteria());
    }

    public Integer getRowCountDoctosServico(Long idManifestoEntrega) {
        SqlTemplate hql = getSqlDoctosServicos(idManifestoEntrega);
        return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false), hql.getCriteria());
    }

    private SqlTemplate getSqlDoctosServicos(Long idManifestoEntrega) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(MED.idManifestoEntregaDocumento", "idManifestoEntregaDocumento");
        hql.addProjection("FO.sgFilial", "sgFilialDoctoServico");
        hql.addProjection("DS.idDoctoServico", "idDoctoServico");
        hql.addProjection("DS.tpDocumentoServico", "tpDocumentoServico");
        hql.addProjection("DS.nrDoctoServico", "nrDoctoServico");
        hql.addProjection("MED.dhOcorrencia", "dhBaixa");
        hql.addProjection("OE.cdOcorrenciaEntrega", "cdOcorrenciaEntrega");
        hql.addProjection("OE.dsOcorrenciaEntrega", "dsOcorrenciaEntrega");
        hql.addProjection("PD.tpIdentificacao", "tpIdentificacaoDestinatario");
        hql.addProjection("PD.nrIdentificacao", "nrIdentificacaoDestinatario");
        hql.addProjection("PD.nmPessoa", "nmPessoaDestinatario");
        hql.addProjection("DS.dsEnderecoEntregaReal", "dsEnderecoEntrega");
        hql.addProjection("MED.tpSituacaoDocumento", "tpSituacaoDocumento");
        hql.addProjection("MED.nmRecebedor", "nmRecebedor)");

        StringBuilder hqlFrom = new StringBuilder()
                .append(ManifestoEntregaDocumento.class.getName()).append(" MED ")
                .append(" inner join MED.doctoServico as DS ")
                .append(" inner join DS.filialByIdFilialOrigem as FO ")
                .append("  left join MED.ocorrenciaEntrega as OE ")
                .append("  left join DS.clienteByIdClienteDestinatario as CD ")
                .append("  left join CD.pessoa as PD ");
        hql.addFrom(hqlFrom.toString());

        hql.addCustomCriteria("MED.manifestoEntrega.id = ?", idManifestoEntrega);

        hql.addOrderBy("FO.sgFilial");
        hql.addOrderBy("DS.tpDocumentoServico");
        hql.addOrderBy("DS.nrDoctoServico");
        return hql;
    }

    /**
     * Consulta manifestoEntregaDocumento para apresentar no detalhamento da aba
     * 'Documentos da tela 'Consultar Manifesto'.
     *
     * @param idManifestoEntregaDocumento
     * @return
     */
    public Map<String, Object> findByIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(MED.idManifestoEntregaDocumento", "idManifestoEntregaDocumento");
        hql.addProjection("DS.idDoctoServico", "idDoctoServico");
        hql.addProjection("FO.sgFilial", "sgFilialDoctoServico");
        hql.addProjection("DS.tpDocumentoServico", "tpDocumentoServico");
        hql.addProjection("DS.nrDoctoServico", "nrDoctoServico");
        hql.addProjection("PR.tpIdentificacao", "tpIdentificacaoRemetente");
        hql.addProjection("PR.nrIdentificacao", "nrIdentificacaoRemetente");
        hql.addProjection("PR.nmPessoa", "nmPessoaRemetente");
        hql.addProjection("PD.tpIdentificacao", "tpIdentificacaoDestinatario");
        hql.addProjection("PD.nrIdentificacao", "nrIdentificacaoDestinatario");
        hql.addProjection("PD.nmPessoa", "nmPessoaDestinatario");
        hql.addProjection("DS.dsEnderecoEntregaReal", "dsEnderecoEntrega");
        hql.addProjection("DS.qtVolumes", "qtVolumes");
        hql.addProjection("DS.psReferenciaCalculo", "psReferenciaCalculo");
        hql.addProjection("DS.vlTotalDocServico", "vlTotalDocServico");
        hql.addProjection("OE.cdOcorrenciaEntrega", "cdOcorrenciaEntrega");
        hql.addProjection("OE.dsOcorrenciaEntrega", "dsOcorrenciaEntrega");
        hql.addProjection("MED.obManifestoEntregaDocumento", "obManifestoEntregaDocumento");
        hql.addProjection("MED.dhOcorrencia", "dhOcorrencia");
        hql.addProjection("MED.nmRecebedor", "nmRecebedor");
        hql.addProjection("MED.tpSituacaoDocumento", "tpSituacaoDocumento)");

        StringBuilder hqlFrom = new StringBuilder()
                .append(ManifestoEntregaDocumento.class.getName()).append(" MED ")
                .append(" inner join MED.doctoServico as DS ")
                .append(" inner join DS.filialByIdFilialOrigem as FO ")
                .append("  left join MED.ocorrenciaEntrega as OE ")
                .append("  left join DS.clienteByIdClienteRemetente as CR ")
                .append("  left join CR.pessoa as PR ")
                .append("  left join DS.clienteByIdClienteDestinatario as CD ")
                .append("  left join CD.pessoa as PD ");
        hql.addFrom(hqlFrom.toString());

        hql.addCustomCriteria("MED.idManifestoEntregaDocumento = ?", idManifestoEntregaDocumento);

        return (Map<String, Object>) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    /**
     * Busca lista de entregas para o projeto VOL
     *
     * @param idControleCarga
     * @param idsClienteQuestionario
     * @return
     */
    public List<Map<String, Object>> findEntregasToMobile(Long idControleCarga, List<Long> idsClienteQuestionario) {

        /**
         * retorna as entregas que tem documento
         */
        SqlTemplate hql = mountSqlTemplateFindEntregasToMobile("MANIFESTO_ENTREGA_DOCUMENTO", idControleCarga, idsClienteQuestionario);
        List<Map<String, Object>> retorno = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

        /**
         * retorna os volumes que NÃO tem documento vinculado
         */
        hql = mountSqlTemplateFindEntregasToMobile("MANIFESTO_ENTREGA_VOLUME", idControleCarga, idsClienteQuestionario);
        retorno.addAll(getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()));

        List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();

        for (Map<String, Object> map : retorno) {
            boolean haveDoc = false;
            for (Map<String, Object> map2 : ret) {
                if (map.get("DOCT").equals(map2.get("DOCT"))) {
                    if ("N".equals(map2.get("DCMA")) && "S".equals(map.get("DCMA"))) {
                        map2 = map;
                    }
                    haveDoc = true;
                    break;
                }
            }
            if (!haveDoc) {
                ret.add(map);
            }
        }

        return ret;
    }

    private SqlTemplate mountSqlTemplateFindEntregasToMobile(String tipoManifesto, Long idControleCarga, List<Long> idsClienteQuestionario) {
        String idsClienteQuestionarioQuery = StringUtils.join(idsClienteQuestionario, ", ");

        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("new map (FILI.sgFilial || to_char(CTRC.nrDoctoServico,'0000000000')", "CTRC");
        hql.addProjection("CTRC.idDoctoServico", "DOCT");
        hql.addProjection("PESS.nmPessoa", "DEST");
        hql.addProjection("to_char(CTRC.dtPrevEntrega,'dd/mm/rrrr')", "DPEC");
        hql.addProjection("CTRC.dsEnderecoEntregaReal", "ENDE");
        //LMS-3952 - Nova validação de obriga recebedor é feita no retorno da query
        hql.addProjection("REMETENTE.blObrigaRecebedor || "
                + "CTRC.blReembolso || "
                + "case when REDO.tpSituacaoRegistro = 'CR' then 'S' else 'N' end || "
                + "case when REMETENTE.blAssinaturaDigital is null then 'N' else REMETENTE.blAssinaturaDigital end || "
                + "case when REMETENTE.blObrigaRg is null then 'N' else REMETENTE.blObrigaRg end || "
                + "case when REMETENTE.blObrigaBaixaPorVolume is null then 'N' else REMETENTE.blObrigaBaixaPorVolume end || "
                + "case when REMETENTE.blPermiteBaixaParcial is null then 'N' else REMETENTE.blPermiteBaixaParcial end || "
                + "case when REMETENTE.idCliente in (" + idsClienteQuestionarioQuery + ") then 'S' else 'N' end || "
                + "case when REMETENTE.blObrigaParentesco is null then 'N' else REMETENTE.blObrigaParentesco end ", " FLAG");
        hql.addProjection("to_char(AGEN.hrPreferenciaInicial,'hh24:mi') || to_char(AGEN.hrPreferenciaFinal,'hh24:mi')", "AGEN");
        hql.addProjection("MAEN.idManifestoEntrega", "IDMA");
        hql.addProjection("OCEN.dsOcorrenciaEntrega", "MOTI");
        hql.addProjection("COCA.nrControleCarga", "NRCG");
        hql.addProjection("MAEN.nrManifestoEntrega", "MANI");
        //LMS-3952 / Obrigatoriedade da observação "Obrigatório nome e recebedor" para TNT Express - rodoviário e aéreo
        hql.addProjection("CLI1.idCliente", "ID_DESTINATARIO");
        hql.addProjection("MDE.nrChave", "NR_CHAVE_CTE");
        hql.addProjection("REMETENTE.idCliente", "ID_REMETENTE");

        if ("MANIFESTO_ENTREGA_DOCUMENTO".equalsIgnoreCase(tipoManifesto)) {
            hql.addProjection("case when MEDO.dhOcorrencia.value is null then '-' else 'H' end", "STAT");
            hql.addProjection("MEDO.nmRecebedor", "RECE");
            hql.addProjection("case when MANI.tpManifestoEntrega = 'ED' then '' else to_char(AW.idAwb) end", "ID_AWB");
            hql.addProjection("EMP.sgEmpresa", "SG_EMPRESA ");
            hql.addProjection("case when AW.nrAwb < 0 then to_char(AW.idAwb) else to_char(AW.dsSerie||AW.nrAwb||AW.dvAwb) end", "PREAWB_AWB ");
            hql.addProjection("'S'", "DCMA)");
        } else if ("MANIFESTO_ENTREGA_VOLUME".equalsIgnoreCase(tipoManifesto)) {
            hql.addProjection("case when MEVO.dhOcorrencia.value is null then '-' else 'H' end", "STAT");
            hql.addProjection("'-'", "RECE");
            hql.addProjection("'N'", "DCMA)");
        }

        StringBuilder from = new StringBuilder()
                .append(Conhecimento.class.getName() + " as CTRC");
        if ("MANIFESTO_ENTREGA_DOCUMENTO".equalsIgnoreCase(tipoManifesto)) {
            from.append(" inner join CTRC.manifestoEntregaDocumentos as MEDO");
            from.append(" left  join MEDO.awb as AW");
            from.append(" left join AW.ciaFilialMercurio as CIA");
            from.append(" left join CIA.empresa as EMP");
            from.append(" inner join MEDO.manifestoEntrega as MAEN");
            from.append(" left join MEDO.ocorrenciaEntrega as OCEN");
        } else if ("MANIFESTO_ENTREGA_VOLUME".equalsIgnoreCase(tipoManifesto)) {
            from.append(" inner join CTRC.manifestoEntregaVolumes as MEVO");
            from.append(" inner join MEVO.manifestoEntrega as MAEN");
            from.append(" left join MEVO.ocorrenciaEntrega as OCEN");
        }

        from.append(" inner join CTRC.servico as SERV");

        from.append(" inner join MAEN.manifesto as MANI")
                .append(" inner join MANI.controleCarga as COCA")
                .append(" inner join CTRC.filialByIdFilialOrigem as FILI")
                .append(" inner join CTRC.clienteByIdClienteDestinatario as CLI1")
                .append(" inner join CLI1.pessoa as PESS")
                .append(" inner join CTRC.clienteByIdClienteRemetente as REMETENTE")
                .append("  left join CTRC.agendamentoDoctoServicos as ADCS")
                .append("  left join ADCS.agendamentoEntrega as AGEN")
                .append("  left join CTRC.registroDocumentoEntregas as REDO,")
                .append(MonitoramentoDocEletronico.class.getSimpleName()).append(" as MDE")
                .append(" left join MDE.doctoServico as DS");

        hql.addFrom(from.toString());

        hql.addCriteria("NVL(ADCS.tpSituacao,'A')", "=", "A");
        if ("MANIFESTO_ENTREGA_DOCUMENTO".equalsIgnoreCase(tipoManifesto)) {
            hql.addCriteria("MEDO.tpSituacaoDocumento", "<>", "CANC");
        }

        hql.addCustomCriteria("CTRC.id = DS.idDoctoServico");
        hql.addCustomCriteria("COCA.idControleCarga = ?", idControleCarga);

        hql.addOrderBy("FILI.sgFilial");
        hql.addOrderBy("CTRC.nrDoctoServico");

        return hql;

    }

// ######################################################################################################################
// ######################################################################################################################
    /**
     * Consulta da lookup de Manifesto de Entrega
     *
     * @param parametros
     * @return
     */
    public List<Map<String, Object>> findLookupByTagManifesto(TypedFlatMap parametros) {
        SqlTemplate sql = new SqlTemplate();
        sql.addProjection("new Map(me.idManifestoEntrega", "idManifestoEntrega");
        sql.addProjection("me.nrManifestoEntrega", "nrManifestoEntrega");
        sql.addProjection("m.tpManifestoEntrega", "manifesto_tpManifestoEntrega");
        sql.addProjection("filialOrigem.idFilial", "manifesto_filialByIdFilialOrigem_idFilial");
        sql.addProjection("filialOrigem.sgFilial", "manifesto_filialByIdFilialOrigem_sgFilial");
        sql.addProjection("pessoaFilialOrigem.nmFantasia", "manifesto_filialByIdFilialOrigem_pessoa_nmFantasia)");

        sql.addInnerJoin(getPersistentClass().getName(), "me");
        sql.addInnerJoin("me.manifesto", "m");
        sql.addInnerJoin("m.filialByIdFilialOrigem", "filialOrigem");
        sql.addInnerJoin("filialOrigem.pessoa", "pessoaFilialOrigem");

        sql.addCriteria("me.nrManifestoEntrega", "=", parametros.getInteger("nrManifestoEntrega"));
        sql.addCriteria("m.tpManifesto", "=", parametros.getString("manifesto.tpManifesto"));
        sql.addCriteria("m.tpStatusManifesto", "=", parametros.getString("tpSituacaoManifesto"));
        sql.addCriteria("m.tpManifestoEntrega", "=", parametros.getString("tpManifestoEntrega"));
        sql.addCriteria("m.filialByIdFilialOrigem.id", "=", parametros.getLong("filial.idFilial"));

        Long idDoctoServico = parametros.getLong("doctoServico.idDoctoServico");
        if (idDoctoServico != null) {
            sql.addCustomCriteria("EXISTS " + this.getHqlDocumentos(), idDoctoServico);
        }

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     *
     * @param idControleCarga
     * @return
     */
    public List<Map<String, Object>> findSomatorioEntregasRealizadasByIdControleCarga(Long idControleCarga) {
        StringBuilder sql = new StringBuilder()
                .append("select new map(")
                .append("sum(ds.vlMercadoria) as somaVlMercadoria, ")
                .append("sum(ds.psReal) as somaPsReal, ")
                .append("ds.paisOrigem.id as idPais, ")
                .append("ds.moeda.id as idMoeda) ")
                .append("from ").append(ManifestoEntrega.class.getName()).append(" as me ")
                .append("inner join me.manifesto m ")
                .append("inner join me.manifestoEntregaDocumentos med ")
                .append("inner join med.doctoServico ds ")
                .append("where ")
                .append("m.tpStatusManifesto <> 'CA' ")
                //LMS-5005 / Considerar também os documentos eletrônicos
                .append("and ds.tpDocumentoServico in ('CTR', 'NFT', 'CRT', 'CTE', 'NTE') ")
                .append("and m.controleCarga.id = ? ")
                .append("and exists (")
                .append("select 1 ")
                .append("from ")
                .append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("inner join eds.evento as evento ")
                .append("where ")
                .append("evento.cdEvento = ? ")
                .append("and eds.blEventoCancelado = ? ")
                .append("and eds.doctoServico.id = ds.id) ")
                .append("group by ds.paisOrigem.id, ds.moeda.idMoeda ");

        List<Object> param = new ArrayList<Object>();
        param.add(idControleCarga);
        param.add(Short.valueOf("21"));
        param.add(Boolean.FALSE);
        return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    /**
     *
     * @param idControleCarga
     * @return
     */
    public List<Map<String, Object>> findSomatorioEntregasASeremRealizadasByIdControleCarga(Long idControleCarga) {
        StringBuilder sql = new StringBuilder()
                .append("select new map(")
                .append("sum(ds.vlMercadoria) as somaVlMercadoria, ")
                .append("sum(ds.psReal) as somaPsReal, ")
                .append("sum(ds.psAforado) as somaPsAforado, ")
                .append("ds.paisOrigem.id as idPais, ")
                .append("ds.moeda.id as idMoeda) ")
                .append("from ").append(ManifestoEntrega.class.getName()).append(" as me ")
                .append("inner join me.manifesto m ")
                .append("inner join me.manifestoEntregaDocumentos med ")
                .append("inner join med.doctoServico ds ")
                .append("where ")
                .append("m.tpStatusManifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")
                //LMS-5005 / Considerar também os documentos eletrônicos
                .append("and ds.tpDocumentoServico in ('CTR', 'NFT', 'CRT', 'CTE', 'NTE') ")
                .append("and m.controleCarga.id = ? ")
                .append("and not exists (")
                .append("select 1 ")
                .append("from ")
                .append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("inner join eds.evento as evento ")
                .append("where ")
                .append("evento.cdEvento = ? ")
                .append("and eds.blEventoCancelado = ? ")
                .append("and eds.doctoServico.id = ds.id) ")
                .append("group by ds.paisOrigem.id, ds.moeda.idMoeda ");

        List<Object> param = new ArrayList<Object>();
        param.add(idControleCarga);
        param.add(Short.valueOf("21"));
        param.add(Boolean.FALSE);
        return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    public List<Map<String, Object>> findCodNroPlacaManifestosEntregasCorporativo(Long seceNumero, String unidSigla) {
        StringBuilder sql = new StringBuilder()
                .append(" select new map(")
                .append(" veicCodPlaca as veicCodPlaca,")
                .append(" veicNroPlaca as veicNroPlaca)")
                .append(" from ").append(ManifestosEntregas.class.getName())
                .append(" where seceNumero = ?")
                .append(" and seceUnidSigla = ?");

        List<Object> param = new ArrayList<Object>();
        param.add(seceNumero);
        param.add(unidSigla);

        return getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
    }

    public Long findTipoTabelaManifestosEntregasCorporativo(Long seceNumero, String unidSigla) {
        StringBuilder sql = new StringBuilder()
                .append(" select tipoTabela")
                .append(" from ").append(ManifestosEntregas.class.getName())
                .append(" where seceNumero = ?")
                .append(" and seceUnidSigla = ?");

        List<Object> param = new ArrayList<Object>();
        param.add(seceNumero);
        param.add(unidSigla);

        List result = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
        
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }

        return (Long) result.get(0);

    }

    public List<ManifestoEntrega> findManifestoByIdControleCarga(Long idControleCarga) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select maen ");
        hql.append(" from ManifestoEntrega as maen ");
        hql.append(" join maen.manifesto as mani join mani.controleCarga as coca ");
        hql.append(" where coca.idControleCarga = ? ");
        return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idControleCarga});
    }

    /**
     * Consulta a quantidade de entregas efetuadas
     *
     * @param idManifestoEntrega
     * @return
     */
    public Integer findQuantidadeEntregasEfetuadasByIdManifestoEntrega(Long idManifestoEntrega) {
        StringBuilder hql = new StringBuilder();
        hql.append(" select me ");
        hql.append(" from ManifestoEntrega as me ");
        hql.append(" join me.manifesto as m ");
        hql.append(" join me.manifestoEntregaDocumentos as med ");
        hql.append(" join med.ocorrenciaEntrega as oe ");
        hql.append(" join oe.evento as ev ");
        hql.append(" where me.idManifestoEntrega = ? ");
        hql.append("   and m.tpStatusManifesto != 'CA' ");
        hql.append("   and m.tpManifestoEntrega IN ('");
        hql.append(ConstantesEntrega.DM_MANIFESTO_ENTREGA_NORMAL.getValue());
        hql.append("', '");
        hql.append(ConstantesEntrega.DM_MANIFESTO_ENTREGA_DIRETA.getValue());
        hql.append("', '");
        hql.append(ConstantesEntrega.DM_MANIFESTO_ENTREGA_PARCEIRA.getValue());
        hql.append("') ");

        return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), new Object[]{idManifestoEntrega});

    }

    public List<Map<String, Object>> findManifestoEntregaSuggest(final String sgFilial, final Long nrManifestoEntrega, final Long idEmpresa) {
        final StringBuilder sql = new StringBuilder();

        sql.append("SELECT me.id_manifesto_entrega, ");
        sql.append("       fo.sg_filial as sg_filial, ");
        sql.append("       me.nr_manifesto_entrega, ");
        sql.append("       me.dh_emissao ");

        sql.append("  FROM manifesto_entrega me ");
        sql.append("       inner join filial fo on fo.id_filial = me.id_filial ");

        sql.append(" WHERE fo.sg_filial = :sgFilial ");
        sql.append("   and me.nr_manifesto_entrega = :nrManifestoEntrega ");
        if (idEmpresa != null) {
            sql.append("   and fo.id_empresa = :idEmpresa ");
        }

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("id_manifesto_entrega", Hibernate.LONG);
                sqlQuery.addScalar("sg_filial", Hibernate.STRING);
                sqlQuery.addScalar("nr_manifesto_entrega", Hibernate.LONG);
                sqlQuery.addScalar("dh_emissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                query.setString("sgFilial", sgFilial);
                query.setLong("nrManifestoEntrega", nrManifestoEntrega);
                if (idEmpresa != null) {
                    query.setLong("idEmpresa", idEmpresa);
                }
                csq.configQuery(query);
                return query.list();
            }
        };

        List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();

        List<Object[]> list = getHibernateTemplate().executeFind(hcb);

        for (Object[] o : list) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("idManifestoEntrega", o[0]);
            map.put("sgFilial", o[1]);
            map.put("nrManifestoEntrega", o[2]);
            map.put("dhEmissao", o[3]);
            toReturn.add(map);

        }

        return toReturn;
    }

	public ManifestoEntrega findManifestoAbertoSubcontratacaoFedex(
			Filial filialEvento, Short nrRota, DateTime dhOcorrencia, Long idDoctoServico) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select me from ManifestoEntrega me ")
			.append(" where ")
			.append(" me.filial = :filialEvento")
			.append(" and me.manifesto.tpStatusManifesto = :tpStatusManifesto")
			.append(" and me.manifesto.dhEmissaoManifesto.value = :dhEmissao")
			.append(" and me.manifesto.tpManifesto = :tpManifesto")
			.append(" and me.manifesto.tpManifestoEntrega = :tpManifestoEntrega")
			.append(" and me.manifesto.controleCarga.rotaColetaEntrega.nrRota = :rota ")
			
			.append(" and not exists (")
			.append(" 	select 1 from ")
			.append(ManifestoEntregaDocumento.class.getName()).append(" as med ")
			.append("	where me.manifesto.id = med.manifestoEntrega.id")
			.append("	and med.doctoServico.id = :idDoctoServico) ")
			;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filialEvento", filialEvento);
		params.put("tpStatusManifesto", "TC");
		params.put("tpManifestoEntrega", "EP");
		params.put("tpManifesto", "E");
		params.put("rota", nrRota);
		params.put("dhEmissao", dhOcorrencia);
		params.put("idDoctoServico", idDoctoServico);

		
		List<ManifestoEntrega> manifestos = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		if (CollectionUtils.isNotEmpty(manifestos)){
			return manifestos.get(0);
		}
		return null;
	}
	
	
	public ManifestoEntrega findManifestoAbertoByOcorrenciaSubcontratacaoFedex(DoctoServico doctoServico){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append("select med.manifestoEntrega from ManifestoEntregaDocumento med ")
			.append(" where ")
			.append(" med.manifestoEntrega.manifesto.tpStatusManifesto not in ('CA','FE') ")
			.append(" and med.doctoServico = :doctoServico  ")
			.append(" and med.ocorrenciaEntrega is not null ")
			;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("doctoServico", doctoServico);
		
		List<ManifestoEntrega> manifestos = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		if (CollectionUtils.isNotEmpty(manifestos)){
			return manifestos.get(0);
		}
		
		return null;
		
	}

	public ManifestoEntrega findManifestoAbertoByDoctoServico(
			Filial filialEvento, Short nrRota, DoctoServico doctoServico) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("select me from ManifestoEntrega me, ManifestoEntregaDocumento mde  ")
			.append(" where ")
			.append(" me.filial = :filialEvento")
			.append(" and me.manifesto.tpStatusManifesto = :tpStatusManifesto")
			.append(" and me.manifesto.tpManifesto = :tpManifesto")
			.append(" and me.manifesto.tpManifestoEntrega = :tpManifestoEntrega")
			.append(" and me.manifesto.controleCarga.rotaColetaEntrega.nrRota = :rota ")
			.append(" and me = mde.manifestoEntrega ")
			.append(" and mde.doctoServico = :doctoServico")
			.append(" and mde.ocorrenciaEntrega is null ")
			;
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("filialEvento", filialEvento);
		params.put("tpStatusManifesto", "TC");
		params.put("tpManifestoEntrega", "EP");
		params.put("tpManifesto", "E");
		params.put("rota", nrRota);
		params.put("doctoServico", doctoServico);
		
		List<ManifestoEntrega> manifestos = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		if (CollectionUtils.isNotEmpty(manifestos)){
			return manifestos.get(0);
		}
		return null;
	}
    public Integer findQuantidadeManifestoEntrega(Long idDoctoServico){
        StringBuilder query = new StringBuilder();

        query.append("select nvl(count(id_manifesto_entrega_documento), 0) as countEntrega  ")
              .append("from manifesto_entrega_documento where id_docto_servico = :idDoctoServico");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idDoctoServico", idDoctoServico);


        List quantidadeManifesto = getAdsmHibernateTemplate()
                .findBySql(
                        query.toString(),
                        param,
                        new ConfigureSqlQuery() {
                            @Override
                            public void configQuery(SQLQuery sql) {
                                sql.addScalar("countEntrega", Hibernate.INTEGER);

                            }
                        }
                );
        return (Integer)quantidadeManifesto.get(0);


    }
}
