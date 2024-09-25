package com.mercurio.lms.entrega.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.entrega.model.AgendamentoDoctoServico;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;

import org.apache.commons.lang3.StringUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class ManifestoEntregaDocumentoDAO extends BaseCrudDao<ManifestoEntregaDocumento, Long> {
    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    @Override
    protected final Class getPersistentClass() {
        return ManifestoEntregaDocumento.class;
    }

    /**
     * Método usado na tela de Didgitar CTRC Reentrega.
     *
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountManifestoEntregaDoctoServicoReentrega(Long idDoctoServico) {
        DetachedCriteria dcMax = DetachedCriteria.forClass(getPersistentClass(), "meda");
        dcMax.setProjection(Projections.max("meda.dhInclusao.value"));
        dcMax.add(Restrictions.eqProperty("meda.doctoServico.idDoctoServico", "medb.doctoServico.idDoctoServico"));

        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "medb")
                .setProjection(Projections.rowCount())
                .createAlias("medb.doctoServico", "ds")
                .createAlias("medb.ocorrenciaEntrega", "oe")
                .add(Restrictions.eq("ds.idDoctoServico", idDoctoServico))
                .add(Restrictions.eq("oe.blOcasionadoMercurio", Boolean.FALSE))
                .add(Property.forName("medb.dhInclusao.value").eq(dcMax));

        return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
    }

    public List findByIdDoctoServico(Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "MED")
                .createAlias("MED.doctoServico", "DS")
                .createAlias("MED.manifestoEntrega", "ME")
			.createAlias("ME.manifesto","M")
                .createAlias("DS.filialByIdFilialOrigem", "FO")
			.setFetchMode("M.controleCarga",FetchMode.JOIN)
                .add(Restrictions.eq("DS.id", idDoctoServico))
                .add(Restrictions.isNull("MED.dhOcorrencia.value"))
			.add(Restrictions.ne("M.tpStatusManifesto","CA"))
                .addOrder(Order.desc("MED.id"));

        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	public ManifestoEntregaDocumento findByIdDoctoServicoManifesto(Long idDoctoServico,Long idManifestoEntrega) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "MED")
                .createAlias("MED.doctoServico", "DS")
                .createAlias("MED.manifestoEntrega", "ME")
			.setFetchMode("DS.filialByIdFilialOrigem",FetchMode.JOIN)
                .add(Restrictions.eq("DS.id", idDoctoServico))
                .add(Restrictions.eq("ME.id", idManifestoEntrega));

		return (ManifestoEntregaDocumento)getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public EventoControleCarga findEventoControleCarga(Long idManifesto, String tpEventoControleCarga) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("ECC");
        hql.addFrom(new StringBuilder()
                .append(Manifesto.class.getName()).append(" AS M ")
                .append("INNER JOIN M.controleCarga AS CC ")
                .append("INNER JOIN CC.eventoControleCargas AS ECC ").toString());

		hql.addCriteria("ECC.tpEventoControleCarga","=",tpEventoControleCarga);
		hql.addCriteria("M.id","=",idManifesto);		
        hql.addCustomCriteria("ECC.filial = M.filialByIdFilialDestino");

		return (EventoControleCarga)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
    }

    /**
     *
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    private String addSqlByEntregasRealizar(Long idControleCarga, Long idFilial, List param, boolean isRowCount) {
        StringBuffer sql = new StringBuffer();
        if (!isRowCount) {
            sql.append("select new map(")
                    .append("med.idManifestoEntregaDocumento as idManifestoEntregaDocumento, ")
                    .append("ds.idDoctoServico as doctoServico_idDoctoServico, ")
                    .append("ds.tpDocumentoServico as doctoServico_tpDocumentoServico, ")
                    .append("ds.dtPrevEntrega as doctoServico_dtPrevEntrega, ")
                    .append("ds.nrDoctoServico as doctoServico_nrDoctoServico, ")
                    .append("ds.dsEnderecoEntregaReal as doctoServico_dsEnderecoEntregaReal, ")
                    .append("filialOrigem.sgFilial as doctoServico_filialByIdFilialOrigem_sgFilial, ")
                    .append("clientePessoa.nmPessoa as doctoServico_clienteByIdClienteDestinatario_pessoa_nmPessoa, ")
                    .append("clientePessoa.tpIdentificacao as doctoServico_clienteByIdClienteDestinatario_pessoa_tpIdentificacao, ")
                    .append("clientePessoa.nrIdentificacao as doctoServico_clienteByIdClienteDestinatario_pessoa_nrIdentificacao, ")
                    .append("moeda.sgMoeda as doctoServico_moeda_sgMoeda, ")
                    .append("moeda.dsSimbolo as doctoServico_moeda_dsSimbolo")
                    .append(") ");
        }
        sql.append("from ")
                .append(ManifestoEntregaDocumento.class.getName()).append(" as med ")
                .append("inner join med.doctoServico as ds ")
                .append("inner join ds.filialByIdFilialOrigem as filialOrigem ")
                .append("inner join ds.moeda as moeda ")
                .append("left join ds.clienteByIdClienteDestinatario as clienteDestinatario ")
                .append("left join clienteDestinatario.pessoa as clientePessoa ")
                .append("inner join med.manifestoEntrega as me ")
                .append("inner join me.manifesto as manifesto ")
                .append("where ")
                .append("manifesto.controleCarga.id = ? ")
                .append("and manifesto.tpStatusManifesto not in ('CA', 'DC', 'ED', 'FE', 'PM') ")
                .append("and med.tpSituacaoDocumento NOT IN('CANC', 'FECH') ")
                .append("and not exists (")
                .append("select 1 ")
                .append("from ")
                .append(EventoDocumentoServico.class.getName()).append(" as eds ")
                .append("inner join eds.evento as evento ")
                .append("where ")
                .append("evento.cdEvento in (?, ?) ")
                .append("and eds.blEventoCancelado = ? ")
                .append("and eds.filial.id = ? ")
                .append("and eds.doctoServico.id = ds.id) ");

        param.add(idControleCarga);
        param.add(Short.valueOf("21"));
        param.add(Short.valueOf("85"));
        param.add(Boolean.FALSE);
        param.add(idFilial);

        if (!isRowCount) {
            sql.append("order by ds.tpDocumentoServico, filialOrigem.sgFilial, ds.nrDoctoServico ");
        }
        return sql.toString();
    }

    /**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idControleCarga seja informado.
     * @param idFilial
     *
     * @param Long idControleCarga
     * @param FindDefinition findDefinition
     * @return ResultSetPage com os dados da grid.
     */
	public ResultSetPage findPaginatedEntregasRealizar(Long idControleCarga, Long idFilial, FindDefinition findDefinition){
        List param = new ArrayList();
        String sql = addSqlByEntregasRealizar(idControleCarga, idFilial, param, false);
        ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
        return rsp;
    }

	public List<Map<String, Object>> findEntregasRealizar(TypedFlatMap criteria){
        StringBuilder sql = new StringBuilder();
        sql
                .append(mountProjectionEntregaRealizar())
                .append(mountFromEntregaRealizar())
                .append(mountWhereEntregasRealizar(criteria))
                .append(mountGroupByEntregasRealizar());

		List<Map<String, Object>> list = getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), criteria, configureSqlQueryEntregasRealizar() );
        return list;
    }

	public List<Map<String, Object>> findEntregasRealizadas(TypedFlatMap criteria){
        StringBuilder sql = new StringBuilder();
        sql
                .append(mountProjectionEntregaRealizadas())
                .append(mountFromEntregaRealizadas())
                .append(mountWhereEntregasRealizadas(criteria))
                .append(mountGroupByEntregasRealizadas());

		List<Map<String, Object>> list = getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), criteria, configureSqlQueryEntregasRealizadas() );
        return list;
    }

	
    private ConfigureSqlQuery configureSqlQueryEntregasRealizar() {
        return new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("sgFilialOrigemDocto", Hibernate.STRING);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("sgEmpresa", Hibernate.STRING);
				sqlQuery.addScalar("preAwbAwb",Hibernate.STRING);
                sqlQuery.addScalar("tpStatusAwb", Hibernate.STRING);
                sqlQuery.addScalar("idFilialOrigemAwb", Hibernate.LONG);
                sqlQuery.addScalar("volumes", Hibernate.INTEGER);
                sqlQuery.addScalar("dtPrevEntrega", Hibernate.STRING);
                sqlQuery.addScalar("nmCliente", Hibernate.STRING);
                sqlQuery.addScalar("endCliente", Hibernate.STRING);
                sqlQuery.addScalar("veiculo", Hibernate.STRING);
                sqlQuery.addScalar("peso", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("valor", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("tpManifestoEntrega");
            }
        };
    }

    private ConfigureSqlQuery configureSqlQueryEntregasRealizadas() {
        return new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("sgFilialOrigemDocto", Hibernate.STRING);
                sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
                sqlQuery.addScalar("nrManifesto", Hibernate.STRING);
                sqlQuery.addScalar("sgEmpresa", Hibernate.STRING);
				sqlQuery.addScalar("preAwbAwb",Hibernate.STRING);
                sqlQuery.addScalar("tpStatusAwb", Hibernate.STRING);
                sqlQuery.addScalar("volumes", Hibernate.INTEGER);
                sqlQuery.addScalar("dtPrevEntrega", Hibernate.STRING);
                sqlQuery.addScalar("nmCliente", Hibernate.STRING);
                sqlQuery.addScalar("endCliente", Hibernate.STRING);
                sqlQuery.addScalar("veiculo", Hibernate.STRING);
                sqlQuery.addScalar("dsEvento", Hibernate.STRING);
                sqlQuery.addScalar("dhEvento", Hibernate.STRING);
                sqlQuery.addScalar("peso", Hibernate.BIG_DECIMAL);
                sqlQuery.addScalar("valor", Hibernate.BIG_DECIMAL);
            }
        };
    }

    private String mountProjectionEntregaRealizar() {
        StringBuilder projection = new StringBuilder();

        projection
                .append("SELECT ")
                .append("  ds.id_docto_servico		   AS idDoctoServico, ")
                .append("  fod.sg_filial               AS sgFilialOrigemDocto, ")
                .append("  ds.nr_docto_servico         AS nrDoctoServico, ")
                .append("  e.sg_empresa                AS sgEmpresa, ")
                .append("  CASE ")
                .append("    WHEN aw.NR_AWB<0 ")
                .append("    THEN TO_CHAR(aw.ID_AWB) ")
                .append("    ELSE aw.DS_SERIE ")
                .append("      ||aw.NR_AWB ")
                .append("      ||aw.DV_AWB ")
                .append("  END                         AS preAwbAwb, ")
                .append("  aw.tp_status_awb			   AS tpStatusAwb, ")
                .append("  aw.id_filial_origem  	   AS idFilialOrigemAwb, ")
                .append("  ds.qt_volumes               AS volumes, ")
                .append("  TO_CHAR(ds.dt_prev_entrega,'DD/MM/YYYY') AS dtPrevEntrega, ")
                .append("  pessCliente.nm_pessoa       AS nmCliente, ")
                .append("  epCliente.ds_endereco       AS endCliente, ")
                .append("  mt.nr_identificador         AS veiculo, ")
                .append("  m.tp_manifesto_entrega      AS tpManifestoEntrega, ")
                .append("  SUM(ds.ps_real)             AS peso, ")
                .append("  SUM(ds.vl_mercadoria)       AS valor ");

        return projection.toString();
    }

    private String mountProjectionEntregaRealizadas() {
        StringBuilder projection = new StringBuilder();

        projection
                .append("SELECT ")
                .append("  fod.sg_filial               AS sgFilialOrigemDocto, ")
                .append("  ds.nr_docto_servico         AS nrDoctoServico, ")
                .append("  fm.sg_filial || ' ' || me.nr_manifesto_entrega AS nrManifesto, ")
                .append("  e.sg_empresa                AS sgEmpresa, ")
                .append("  CASE ")
                .append("    WHEN aw.NR_AWB<0 ")
                .append("    THEN TO_CHAR(aw.ID_AWB) ")
                .append("    ELSE aw.DS_SERIE ")
                .append("      ||aw.NR_AWB ")
                .append("      ||aw.DV_AWB ")
                .append("  END                         AS preAwbAwb, ")
                .append("  aw.tp_status_awb			   AS tpStatusAwb, ")
                .append("  ds.qt_volumes               AS volumes, ")
                .append("  TO_CHAR(ds.dt_prev_entrega,'DD/MM/YYYY') AS dtPrevEntrega, ")
                .append("  pessCliente.nm_pessoa       AS nmCliente, ")
                .append("  epCliente.ds_endereco       AS endCliente, ")
                .append("  mt.nr_identificador         AS veiculo, ")
                .append("  ").append(PropertyVarcharI18nProjection.createProjection("de.ds_descricao_evento_i")).append("    AS dsEvento, ")
                .append("  TO_CHAR(eds.dh_evento, 'DD/MM/YYYY hh24:mi TZR') AS dhEvento, ")
                .append("  SUM(ds.ps_real)             AS peso, ")
                .append("  SUM(ds.vl_mercadoria)       AS valor ");

		
        return projection.toString();
    }

    private String mountFromEntregaRealizar() {
        StringBuilder from = new StringBuilder();

        from
                .append("FROM AWB aw, ")
                .append("  CTO_AWB cto, ")
                .append("  DOCTO_SERVICO ds, ")
                .append("  CONHECIMENTO co, ")
                .append("  FILIAL fod, ")
                .append("  CIA_FILIAL_MERCURIO cia, ")
                .append("  EMPRESA e, ")
                .append("  PESSOA pessCliente, ")
                .append("  ENDERECO_PESSOA epCliente, ")
                .append("  MEIO_TRANSPORTE mt, ")
                .append("  MANIFESTO_ENTREGA_DOCUMENTO med, ")
                .append("  MANIFESTO_ENTREGA me, ")
                .append("  MANIFESTO m, ")
                .append("  CONTROLE_CARGA cc ");

        return from.toString();
    }

    private String mountFromEntregaRealizadas() {
        StringBuilder from = new StringBuilder();

        from
                .append("FROM AWB aw, ")
                .append("  CTO_AWB cto, ")
                .append("  DOCTO_SERVICO ds, ")
                .append("  CONHECIMENTO co, ")
                .append("  FILIAL fod, ")
                .append("  CIA_FILIAL_MERCURIO cia, ")
                .append("  EMPRESA e, ")
                .append("  PESSOA pessCliente, ")
                .append("  ENDERECO_PESSOA epCliente, ")
                .append("  MEIO_TRANSPORTE mt, ")
                .append("  MANIFESTO_ENTREGA_DOCUMENTO med, ")
                .append("  MANIFESTO_ENTREGA me, ")
                .append("  MANIFESTO m, ")
                .append("  FILIAL fm, ")
                .append("  CONTROLE_CARGA cc, ")
                .append("  EVENTO_DOCUMENTO_SERVICO eds, ")
                .append("  EVENTO ev, ")
                .append("  DESCRICAO_EVENTO de ");

        return from.toString();
    }

    private String mountWhereEntregasRealizar(TypedFlatMap criteria) {
        List<Short> eventosEntrega = new ArrayList<Short>();
        eventosEntrega.add(ConstantesEventosDocumentoServico.CD_EVENTO_ENTREGA_REALIZADA_AEROPORTO);
        eventosEntrega.add(ConstantesEventosDocumentoServico.CD_EVENTO_ENTREGA_REALIZADA_NORMALMENTE);

        criteria.put("cdEventosEntrega", eventosEntrega);
        criteria.put("blEventoCancelado", Boolean.FALSE);
        StringBuilder where = new StringBuilder();

        where
                .append("WHERE aw.id_awb                     = cto.id_awb ")
                .append("AND cto.id_conhecimento             = ds.id_docto_servico ")
                .append("AND ds.id_docto_servico             = co.id_conhecimento ")
                .append("AND ds.id_filial_origem             = fod.id_filial ")
                .append("AND aw.id_cia_filial_mercurio       = cia.id_cia_filial_mercurio ")
                .append("AND cia.id_empresa                  = e.id_empresa ")
                .append("AND ds.id_cliente_destinatario      = pessCliente.id_pessoa ")
                .append("AND pessCliente.id_endereco_pessoa  = epCliente.id_endereco_pessoa	")
                .append("AND ds.id_docto_servico         	 = med.id_docto_servico ")
                .append("AND med.id_manifesto_entrega        = me.id_manifesto_entrega ")
                .append("AND me.id_manifesto_entrega         = m.id_manifesto ")
                .append("AND m.id_controle_carga             = cc.id_controle_carga ")
                .append("AND cc.id_transportado              = mt.id_meio_transporte ")
                .append("AND m.tp_status_manifesto NOT IN('CA', 'DC', 'ED', 'FE', 'PM') ")
                .append("AND med.tp_situacao_documento NOT IN('CANC', 'FECH') ")
                .append("AND cc.id_filial_origem = :idFilialSessao ")
                .append("AND NOT EXISTS ")
                .append("( ")
                .append("  SELECT 1 ")
                .append("    FROM ")
                .append("      EVENTO_DOCUMENTO_SERVICO eds, ")
                .append("      EVENTO e ")
                .append("    WHERE ")
                .append("        eds.id_evento = e.id_evento ")
                .append("    AND eds.bl_evento_cancelado = :blEventoCancelado ")
                .append("    AND eds.id_docto_servico = ds.id_docto_servico ")
                .append("    AND e.cd_evento in (:cdEventosEntrega) ")
                .append("	 AND eds.id_filial = cc.id_filial_origem")
                .append(") ")
                .append("AND aw.id_awb = ")
                .append("( SELECT MAX(aw2.id_awb) ")
                .append("    FROM  cto_awb cto2, ")
                .append("          awb aw2, ")
                .append("          conhecimento co2 ")
                .append("    WHERE ")
                .append("          cto2.id_awb = aw2.id_awb ")
                .append("      AND cto2.id_conhecimento = co2.id_conhecimento ")
                .append("      AND co2.id_conhecimento = ds.id_docto_servico ")
                .append("      AND aw2.tp_status_awb <> 'C' ")
                .append(") ");

        Long idControleCarga = criteria.getLong("idControleCarga");
        if (idControleCarga != null) {
            where.append(" AND cc.id_controle_carga = :idControleCarga ");
        }

        Long idMeiorTransporte = criteria.getLong("idMeioTransporte");
        if (idMeiorTransporte != null) {
            where.append(" AND mt.id_meio_transporte = :idMeioTransporte ");
        }

        Long idManifestoEntrega = criteria.getLong("idManifestoEntrega");
        if (idManifestoEntrega != null) {
            where.append(" AND me.id_manifesto_entrega = :idManifestoEntrega ");
        }

        Long idDoctoServico = criteria.getLong("idConhecimento");
        if (idDoctoServico != null) {
            where.append(" AND ds.id_docto_servico = :idConhecimento ");
        }

        Long idAwb = criteria.getLong("idAwb");
        if (idAwb != null) {
            where.append(" AND aw.id_awb = :idAwb ");
        }

        return where.toString();
    }

    private String mountWhereEntregasRealizadas(TypedFlatMap criteria) {
        List<Short> eventosEntrega = new ArrayList<Short>();
        eventosEntrega.add(ConstantesEventosDocumentoServico.CD_EVENTO_ENTREGA_REALIZADA_AEROPORTO);
        eventosEntrega.add(ConstantesEventosDocumentoServico.CD_EVENTO_ENTREGA_REALIZADA_NORMALMENTE);

        criteria.put("cdEventosEntregaAero", eventosEntrega);
        criteria.put("blEventoCancelado", Boolean.FALSE);
        StringBuilder where = new StringBuilder();

        where
                .append("WHERE aw.id_awb                     = cto.id_awb ")
                .append("AND cto.id_conhecimento             = ds.id_docto_servico ")
                .append("AND ds.id_docto_servico             = co.id_conhecimento ")
                .append("AND ds.id_filial_origem             = fod.id_filial ")
                .append("AND aw.id_cia_filial_mercurio       = cia.id_cia_filial_mercurio ")
                .append("AND cia.id_empresa                  = e.id_empresa ")
                .append("AND ds.id_cliente_destinatario      = pessCliente.id_pessoa ")
                .append("AND pessCliente.id_endereco_pessoa  = epCliente.id_endereco_pessoa	")
                .append("AND ds.id_docto_servico         	 = med.id_docto_servico ")
                .append("AND med.id_manifesto_entrega        = me.id_manifesto_entrega ")
                .append("AND me.id_manifesto_entrega         = m.id_manifesto ")
                .append("AND m.id_filial_origem			     = fm.id_filial ")
                .append("AND m.id_controle_carga             = cc.id_controle_carga ")
                .append("AND cc.id_transportado              = mt.id_meio_transporte ")
                .append("AND eds.id_docto_servico 			 = ds.id_docto_servico ")
                .append("AND eds.id_evento 					 = ev.id_evento ")
                .append("AND ev.id_descricao_evento 		 = de.id_descricao_evento ")
		
                .append("AND eds.id_evento_documento_servico = ( ")
                .append("SELECT max(eds.id_evento_documento_servico) ")
                .append("	FROM EVENTO_DOCUMENTO_SERVICO eds, ")
                .append("		 EVENTO e")
                .append("	WHERE eds.id_evento         = e.id_evento")
                .append("	AND eds.bl_evento_cancelado = :blEventoCancelado")
                .append("	AND eds.id_docto_servico    = ds.id_docto_servico")
                .append("	AND e.cd_evento IN (:cdEventosEntregaAero)")
                .append("	AND eds.id_filial      = cc.id_filial_origem")
                .append(" ) ")
		
                .append("AND cc.tp_status_controle_carga NOT IN ('FE', 'CA')  ")
                .append("AND m.tp_status_manifesto <> 'CA' ")
                .append("AND cc.id_filial_origem = :idFilialSessao ")
                .append("AND aw.id_awb = ")
                .append("( SELECT MAX(aw2.id_awb) ")
                .append("    FROM  cto_awb cto2, ")
                .append("          awb aw2, ")
                .append("          conhecimento co2 ")
                .append("    WHERE ")
                .append("          cto2.id_awb = aw2.id_awb ")
                .append("      AND aw2.tp_status_awb <> 'C' ")
                .append("      AND cto2.id_conhecimento = co2.id_conhecimento ")
                .append("      AND co2.id_conhecimento = ds.id_docto_servico ")
                .append(") ");

        Long idControleCarga = criteria.getLong("idControleCarga");
        if (idControleCarga != null) {
            where.append(" AND cc.id_controle_carga = :idControleCarga ");
        }

        Long idMeiorTransporte = criteria.getLong("idMeioTransporte");
        if (idMeiorTransporte != null) {
            where.append(" AND mt.id_meio_transporte = :idMeioTransporte ");
        }

        Long idManifestoEntrega = criteria.getLong("idManifestoEntrega");
        if (idManifestoEntrega != null) {
            where.append(" AND me.id_manifesto_entrega = :idManifestoEntrega ");
        }

        Long idDoctoServico = criteria.getLong("idConhecimento");
        if (idDoctoServico != null) {
            where.append(" AND ds.id_docto_servico = :idConhecimento ");
        }

        Long idAwb = criteria.getLong("idAwb");
        if (idAwb != null) {
            where.append(" AND aw.id_awb = :idAwb ");
        }

        return where.toString();
    }

    private String mountGroupByEntregasRealizar() {
        StringBuilder groupBy = new StringBuilder();

        groupBy
                .append("group by ")
                .append("  ds.id_docto_servico		   ,")
                .append("  fod.sg_filial               ,")
                .append("  ds.nr_docto_servico         ,")
                .append("  e.sg_empresa                ,")
                .append("  CASE                         ")
                .append("    WHEN aw.NR_AWB<0           ")
                .append("    THEN TO_CHAR(aw.ID_AWB)    ")
                .append("    ELSE aw.DS_SERIE           ")
                .append("      ||aw.NR_AWB              ")
                .append("      ||aw.DV_AWB              ")
                .append("  END                         ,")
                .append("  aw.tp_status_awb			   ,")
                .append("  aw.id_filial_origem		   ,")
                .append("  ds.qt_volumes               ,")
                .append("  ds.dt_prev_entrega          ,")
                .append("  pessCliente.nm_pessoa       ,")
                .append("  epCliente.ds_endereco       ,")
                .append("  mt.nr_identificador         ,")
                .append("  m.tp_manifesto_entrega		")
                .append("ORDER BY 2,3,4,5 ");

        return groupBy.toString();
    }

    private String mountGroupByEntregasRealizadas() {
        StringBuilder groupBy = new StringBuilder();

        groupBy
                .append("group by ")
                .append("  fod.sg_filial               ,")
                .append("  ds.nr_docto_servico         ,")
                .append("  fm.sg_filial || ' ' || me.nr_manifesto_entrega, ")
                .append("  e.sg_empresa                ,")
                .append("  CASE                         ")
                .append("    WHEN aw.NR_AWB<0           ")
                .append("    THEN TO_CHAR(aw.ID_AWB)    ")
                .append("    ELSE aw.DS_SERIE           ")
                .append("      ||aw.NR_AWB              ")
                .append("      ||aw.DV_AWB              ")
                .append("  END                         ,")
                .append("  aw.tp_status_awb			   ,")
                .append("  ds.qt_volumes               ,")
                .append("  ds.dt_prev_entrega          ,")
                .append("  pessCliente.nm_pessoa       ,")
                .append("  epCliente.ds_endereco       ,")
                .append("  mt.nr_identificador         ,")
                .append("  ").append(PropertyVarcharI18nProjection.createProjection("de.ds_descricao_evento_i")).append(", ")
                .append("  TO_CHAR(eds.dh_evento, 'DD/MM/YYYY hh24:mi TZR') ")
                .append("ORDER BY 1,2,13 ");

        return groupBy.toString();
    }

    /**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
     *
     * @param idControleCarga
     * @param idFilial
     * @return Integer com o numero de registos com os dados da grid.
     */
	public Integer getRowCountEntregasRealizar(Long idControleCarga, Long idFilial){
        List param = new ArrayList();
        String sql = addSqlByEntregasRealizar(idControleCarga, idFilial, param, true);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
    }

    /**
	 * Consulta dados dos documentos associados ao ManifestoEntrega, utilizado no cancelamento de manifestos de entrega
     * @param idManifestoEntrega
     * @return
     */
	public List findDocumentosByManifestoEntrega(Long idManifestoEntrega){
        SqlTemplate sql = new SqlTemplate();
        sql.addProjection("new Map(med.idManifestoEntregaDocumento", "idManifestoEntregaDocumento");
        sql.addProjection("ds.idDoctoServico", "idDoctoServico");
        sql.addProjection("ds.tpDocumentoServico", "tpDocumentoServico");
        sql.addProjection("ds.nrDoctoServico", "nrDoctoServico");

        sql.addProjection("l.cdLocalizacaoMercadoria", "cdLocalizacaoMercadoria");
        sql.addProjection("me.nrManifestoEntrega", "nrManifestoEntrega");
        sql.addProjection("m.tpManifestoEntrega", "tpManifestoEntrega");
        sql.addProjection("fOrigem.sgFilial", "sgFilialOrigem");
        sql.addProjection("f.sgFilial", "sgFilial)");

        sql.addInnerJoin(getPersistentClass().getName(), "med");
        sql.addInnerJoin("med.doctoServico", "ds");
        sql.addInnerJoin("ds.localizacaoMercadoria", "l");
        sql.addInnerJoin("ds.filialByIdFilialOrigem", "fOrigem");
        sql.addInnerJoin("med.manifestoEntrega", "me");
        sql.addInnerJoin("me.manifesto", "m");
        sql.addInnerJoin("me.filial", "f");

        sql.addCriteria("me.id", "=", idManifestoEntrega);

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    /**
     * Altera a situacao do manifesto_entrega_documento
     * @param idManifestoEntregaDocumento
     * @param tpSituacao
     */
	public void updateSituacaoManifestoEntregaDocumento(final Long idManifestoEntregaDocumento, final String tpSituacao){
        HibernateCallback updateSituacao = new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                String update = "update ManifestoEntregaDocumento as med set med.tpSituacaoDocumento = ? where med.id = ?";
                session.createQuery(update)
                        .setString(0, tpSituacao)
                        .setLong(1, idManifestoEntregaDocumento.longValue())
                        .executeUpdate();

                return null;
            }
        };
        getAdsmHibernateTemplate().execute(updateSituacao);
    }

    public List<ManifestoEntregaDocumento> findManifestoEntregaDocumentoByDoctoServico(Long idDoctoServico) {
        TypedFlatMap namedParams = new TypedFlatMap();
        namedParams.put("tpSituacaoDocumentoCancelado", "CANC");
        namedParams.put("idDoctoServico", idDoctoServico);

        StringBuilder sql = new StringBuilder();
        sql
                .append("SELECT med ")
                .append("FROM ")
                .append(ManifestoEntregaDocumento.class.getSimpleName()).append(" med ")
                .append("INNER JOIN FETCH med.manifestoEntrega me ")
                .append("INNER JOIN med.doctoServico ds ")
                .append("WHERE ")
                .append("	 med.tpSituacaoDocumento <> :tpSituacaoDocumentoCancelado ")
		.append("AND ds.id =:idDoctoServico")
		;

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), namedParams);
    }

    public List findManifestoEntregaByDoctoServico(Long idDoctoServico) {
        return findManifestoEntregaByDoctoServico(idDoctoServico, null, null);
    }

    public List findManifestoEntregaByDoctoServico(Long idDoctoServico, String tpManifesto, List<String> tpStatusManifestoList) {
        SqlTemplate sql = new SqlTemplate();

        sql.addProjection("new Map(me.idManifestoEntrega", "idManifestoEntrega");
        sql.addProjection("me.nrManifestoEntrega", "nrManifestoEntrega");
        sql.addProjection("med.idManifestoEntregaDocumento", "idManifestoEntregaDocumento");
        sql.addProjection("f.idFilial", "idFilial");
        sql.addProjection("f.sgFilial", "sgFilial");
        sql.addProjection("cc.idControleCarga", "idControleCarga");
        sql.addProjection("cc.nrControleCarga", "nrControleCarga");
        sql.addProjection("fcc.idFilial", "idFilialControleCarga");
        sql.addProjection("fcc.sgFilial", "sgFilialControleCarga");
        sql.addProjection("oe.idOcorrenciaEntrega", "idOcorrenciaEntrega");
        sql.addProjection("oe.cdOcorrenciaEntrega", "cdOcorrenciaEntrega");
        sql.addProjection("oe.dsOcorrenciaEntrega", "dsOcorrenciaEntrega");
        sql.addProjection("oe.ocorrenciaPendencia.idOcorrenciaPendencia", "idOcorrenciaPendencia");
        sql.addProjection("med.dhOcorrencia", "dhBaixa");
        sql.addProjection("med.nmRecebedor", "nmRecebedor");
        sql.addProjection("med.tpEntregaParcial", "tpEntregaParcial");
        sql.addProjection("med.obManifestoEntregaDocumento", "obManifestoEntregaDocumento");
        sql.addProjection("med.obAlteracao", "obAlteracao");
        sql.addProjection("oe.tpOcorrencia", "tpOcorrencia");
        sql.addProjection("m.tpStatusManifesto", "tpStatusManifesto");
        sql.addProjection("m.filialByIdFilialOrigem.idFilial", "idFilialOrigemManifesto)");

        sql.addInnerJoin(getPersistentClass().getName(), "med");
        sql.addInnerJoin("med.manifestoEntrega", "me");
        sql.addInnerJoin("me.filial", "f");
        sql.addInnerJoin("me.manifesto", "m");
        sql.addLeftOuterJoin("m.controleCarga", "cc");
        sql.addLeftOuterJoin("cc.filialByIdFilialOrigem", "fcc");
        sql.addLeftOuterJoin("med.ocorrenciaEntrega", "oe");

		if(tpManifesto != null && !"".equals(tpManifesto)){
            sql.addCriteria("m.tpManifesto", "=", tpManifesto);
        }

		if(tpStatusManifestoList != null){
            sql.addCriteriaNotIn("m.tpStatusManifesto", tpStatusManifestoList);
        }

        sql.addCriteria("med.doctoServico.id", "=", idDoctoServico);
        sql.addOrderBy("me.dhEmissao.value", "desc");

        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
    public Long findIdUltimoManifestoEntregaDocByCdOcorrencia(final Long idDoctoServico, final Short cdOcorrenciaEntrega) {
		final StringBuilder sql = new StringBuilder()
		.append(" SELECT MED.ID_MANIFESTO_ENTREGA_DOCUMENTO ")
		.append(" FROM MANIFESTO_ENTREGA_DOCUMENTO MED, ")
		.append(" OCORRENCIA_ENTREGA OE ")
		.append(" WHERE MED.ID_DOCTO_SERVICO = :idDoctoServico ")
		.append(" AND OE.CD_OCORRENCIA_ENTREGA = :cdOcorrenciaEntrega ")
		.append(" AND MED.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA ")
		.append(" ORDER BY MED.DH_OCORRENCIA DESC ");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.setLong("idDoctoServico", idDoctoServico);
				sqlQuery.setShort("cdOcorrenciaEntrega", cdOcorrenciaEntrega);
				sqlQuery.addScalar("ID_MANIFESTO_ENTREGA_DOCUMENTO", Hibernate.LONG);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);
				return query.list();
			}
		};
		
		List<Long> retorno = (List<Long>) getHibernateTemplate().execute(hcb);
		return retorno.isEmpty() ? null : retorno.get(0);
	}

	public List findDtEntregaDoctoServico(Long idDoctoServico){
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(med.dhOcorrencia as dhOcorrencia)");

		hql.addFrom(ManifestoEntregaDocumento.class.getName()+" med " +
				"join med.doctoServico ds " +
				"left outer join med.ocorrenciaEntrega oe " );

        hql.addCriteria("ds.idDoctoServico", "=", idDoctoServico);
		hql.addCriteria("oe.tpOcorrencia","=", "E");

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    /**
     * Busca o último manifestoEntregaDocumento a partir do id do doctoServico;
     * @param idDoctoServico
     * @return
     */
	public ManifestoEntregaDocumento findLastManifestoEntregaDocumentoByIdDoctoServico(Long idDoctoServico){
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med")
                .add(Restrictions.eq("med.doctoServico.id", idDoctoServico))
                .addOrder(Order.desc("med.dhInclusao.value"));

        List manifestosEntregaDocumento = this.findByDetachedCriteria(detachedCriteria);
		if (manifestosEntregaDocumento.size()>0){
            return (ManifestoEntregaDocumento) manifestosEntregaDocumento.get(0);
        }
        return null;
    }

    /**
     * Busca Manifesto Entrega Documento
     *
     * @param idManifestoEntrega
     * @param idDoctoServico
     * @return
     */
    public ManifestoEntregaDocumento findManifestoEntregaDocumento(Long idManifestoEntrega, Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med");

        dc.add(Restrictions.eq("med.manifestoEntrega.id", idManifestoEntrega));
        dc.add(Restrictions.eq("med.doctoServico.id", idDoctoServico));

		return (ManifestoEntregaDocumento)getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public DateTime findDhOcorrenciaByMeioTransporte(Long idMeioTransporte) {
        SqlTemplate sql = new SqlTemplate();

        StringBuffer sb = new StringBuffer();
        sb.append(MeioTransporte.class.getName()).append(" as mt ");
		sb.append(" inner join mt.controleCargasByIdTransportado as cc " );
		sb.append(" inner join cc.manifestos m " );
        sb.append(" inner join m.manifestoEntrega me ");
        sb.append(" inner join me.manifestoEntregaDocumentos med ");

        sql.addFrom(sb.toString());
        sql.addProjection("med.dhOcorrencia");

        sql.addCriteria("mt.idMeioTransporte", "=", idMeioTransporte);
        sql.addCustomCriteria("cc.tpStatusControleCarga not in ('FE,CA')");

		return (DateTime) getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0);
    }

    /**
	 * Método responsável por retornar a menor data de ocorrencia do manifesto de entrega 
	 * caso o documento de serviço tem uma ocorrência de entrega relacionada ao manifesto de entrega documento
     * @param idDoctoServico
     * @return DateTime a data de ocorrencia
     */
    public DateTime findMenorDhOcorrenciaByIdDoctoServico(Long idDoctoServico) {
        SqlTemplate sql = new SqlTemplate();

        StringBuffer sb = new StringBuffer();
        sb.append(ManifestoEntregaDocumento.class.getName()).append(" as med ");

        sql.addFrom(sb.toString());
        sql.addProjection("TRUNC(med.dhOcorrencia.value)");

        sql.addCriteria("med.doctoServico.idDoctoServico", "=", idDoctoServico);

        sql.addCustomCriteria(new StringBuffer("med.idManifestoEntregaDocumento = (SELECT MIN(med1.idManifestoEntregaDocumento) FROM ").append(ManifestoEntregaDocumento.class.getName()).append(" med1 ")
                .append("WHERE med1.doctoServico.idDoctoServico = ? AND med.ocorrenciaEntrega.cdOcorrenciaEntrega = ?)").toString());
        sql.addCriteriaValue(idDoctoServico); // id_docto_servico
        sql.addCriteriaValue(Short.valueOf("1")); // cd_ocorrencia_entrega

		return JodaTimeUtils.toDateTime(getAdsmHibernateTemplate(), getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria()));
    }

    /**
     *
     * @param idControleCarga
     * @return
     */
    private String addSqlByEntregasRealizadasByProgramacaoColetas(Long idControleCarga, List param, boolean isRowCount) {
        StringBuffer sql = new StringBuffer();
        if (!isRowCount) {
            sql.append("select new map(")
                    .append("manifestoEntegaDoc.idManifestoEntregaDocumento as idManifestoEntregaDocumento, ")
                    .append("doctoServico.idDoctoServico as doctoServico_idDoctoServico, ")
                    .append("doctoServico.tpDocumentoServico as doctoServico_tpDocumentoServico, ")
                    .append("doctoServico.dtPrevEntrega as doctoServico_dtPrevEntrega, ")
                    .append("doctoServico.nrDoctoServico as doctoServico_nrDoctoServico, ")
                    .append("doctoServico.dsEnderecoEntregaReal as doctoServico_dsEnderecoEntregaReal, ")
                    .append("filialByIdFilialOrigem.sgFilial as doctoServico_filialByIdFilialOrigem_sgFilial, ")
                    .append("clientePessoa.nmPessoa as doctoServico_clienteByIdClienteDestinatario_pessoa_nmPessoa, ")
                    .append("clientePessoa.tpIdentificacao as doctoServico_clienteByIdClienteDestinatario_pessoa_tpIdentificacao, ")
                    .append("clientePessoa.nrIdentificacao as doctoServico_clienteByIdClienteDestinatario_pessoa_nrIdentificacao, ")
                    .append("moeda.sgMoeda as doctoServico_moeda_sgMoeda, ")
                    .append("moeda.dsSimbolo as doctoServico_moeda_dsSimbolo, ")
                    .append("eds.dhEvento as doctoServico_eventoDocumentoServico_dhEvento")
                    .append(") ");
        }
        sql.append("from ")
                .append(ManifestoEntregaDocumento.class.getName()).append(" as manifestoEntegaDoc ")
                .append("inner join manifestoEntegaDoc.doctoServico as doctoServico ")
                .append("inner join doctoServico.filialByIdFilialOrigem as filialByIdFilialOrigem ")
                .append("inner join doctoServico.localizacaoMercadoria as lm ")
                .append("inner join doctoServico.eventoDocumentoServicos as eds ")
                .append("inner join eds.evento as evento ");

        if (!isRowCount) {
            sql.append("inner join doctoServico.moeda as moeda ")
                    .append("left join doctoServico.clienteByIdClienteDestinatario as clienteByIdClienteDestinatario ")
                    .append("left join clienteByIdClienteDestinatario.pessoa as clientePessoa ");
        }

        sql.append("inner join manifestoEntegaDoc.manifestoEntrega as manifestoEntrega ")
                .append("inner join manifestoEntrega.manifesto as manifesto ")
                .append("where ")
                .append("manifesto.controleCarga.id = ? ")
                .append("and evento.cdEvento = ? ")
                .append("and eds.blEventoCancelado = ? ")
                .append("and lm.cdLocalizacaoMercadoria = ? ");

        param.add(idControleCarga);
        param.add(Short.valueOf("21"));
        param.add(Boolean.FALSE);
        param.add(Short.valueOf("1"));

        if (!isRowCount) {
            sql.append("order by doctoServico.tpDocumentoServico, filialByIdFilialOrigem.sgFilial, doctoServico.nrDoctoServico ");
        }
        return sql.toString();
    }

    /**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * Exige que um idControleCarga seja informado.
     *
     * @param Long idControleCarga
     * @param FindDefinition findDefinition
     * @return ResultSetPage com os dados da grid.
     */
	public ResultSetPage findPaginatedEntregasRealizadasByProgramacaoColetas(Long idControleCarga, FindDefinition findDefinition){
        List param = new ArrayList();
        String sql = addSqlByEntregasRealizadasByProgramacaoColetas(idControleCarga, param, false);
        ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql, findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());
        return rsp;
    }

    /**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
	 * parametros.
     *
     * @param idControleCarga
     * @return Integer com o numero de registos com os dados da grid.
     */
    public Integer getRowCountEntregasRealizadasByProgramacaoColetas(Long idControleCarga) {
        List param = new ArrayList();
        String sql = addSqlByEntregasRealizadasByProgramacaoColetas(idControleCarga, param, true);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
    }

    public AgendamentoEntrega findAgendamentoByDoctoServico(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("AE");

        hql.addFrom(new StringBuffer(AgendamentoDoctoServico.class.getName()).append(" ADS ")
                .append("INNER JOIN ADS.agendamentoEntrega AE ").toString());

		hql.addCriteria("AE.tpSituacaoAgendamento","=","A");
		hql.addCriteria("ADS.doctoServico.id","=",idDoctoServico);

        hql.addCustomCriteria(new StringBuffer("NOT EXISTS (SELECT ADS2.id FROM ").append(AgendamentoDoctoServico.class.getName()).append(" ADS2 ")
                .append("INNER JOIN ADS2.doctoServico DS2 ")
                .append("INNER JOIN DS2.localizacaoMercadoria LM2 ")
                .append("WHERE ADS2.agendamentoEntrega.id = AE.id AND DS2.id <> ADS.doctoServico.id AND LM2.cdLocalizacaoMercadoria <> ?)").toString());
		hql.addCriteriaValue(Short.valueOf((short)1));

		return (AgendamentoEntrega)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
    }

    public ReciboReembolso findReciboReembolsoAssociado(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("RR");

        hql.addFrom(new StringBuffer(ReciboReembolso.class.getName()).append(" RR ").toString());
		hql.addCriteria("RR.id","=",idDoctoServico);

		return (ReciboReembolso)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

	public void removeByIdManifestoEntrega(Long idManifestoEntrega){
        String sql = "delete from " + ManifestoEntregaDocumento.class.getName() + " as med where med.manifestoEntrega.id = :id";
        getAdsmHibernateTemplate().removeById(sql, idManifestoEntrega);
    }

    /**
	 * Verifica se o devedor do Documento de Serviço não possui indicador de retenção do comprovante de entrega
     * @param idDoctoServico
     * @return número de devedores encontrados.
     */
    public Integer getRowCountDevedorSemRetencaoComprovante(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();

        hql.addFrom(new StringBuilder()
                .append(DevedorDocServFat.class.getName()).append(" DEV ")
                .append(" inner join DEV.cliente as C")
                .toString());

		hql.addCriteria("DEV.doctoServico.id","=",idDoctoServico);
        hql.addCustomCriteria("(C.blRetencaoComprovanteEntrega = ? or C.blRetencaoComprovanteEntrega is null)", Boolean.FALSE);

		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(Boolean.FALSE),hql.getCriteria());
    }

    public List<ManifestoEntregaDocumento> findManifestoSemOcorrenciaEntregaByIdDoctoServico(Long idDoctoServico, String[] tpStatusManifesto) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "MED");
        dc.createAlias("MED.doctoServico", "DS");
        dc.createAlias("MED.manifestoEntrega", "ME");
        dc.createAlias("ME.manifesto", "M");

    	dc.add( Restrictions.eq("DS.idDoctoServico", idDoctoServico) );
    	dc.add( Restrictions.isNull("MED.ocorrenciaEntrega") );
    	dc.add( Restrictions.in("M.tpStatusManifesto", tpStatusManifesto) );

        return findByDetachedCriteria(dc);
    }

    public List findManifestoByIdDoctoServico(Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "MED")
                .createAlias("MED.doctoServico", "DS")
                .add(Restrictions.eq("DS.id", idDoctoServico))
                .addOrder(Order.desc("MED.id"));

        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    public ManifestoEntregaDocumento findManifestoByIdDoctoServico(Long idDoctoServico, Long idControleCarga) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "MED")
                .createAlias("MED.doctoServico", "DS")
                .createAlias("MED.manifestoEntrega", "ME")
			.createAlias("ME.manifesto","M")
                .add(Restrictions.eq("DS.id", idDoctoServico))
                .add(Restrictions.eq("M.controleCarga.id", idControleCarga))
                .addOrder(Order.desc("MED.id"));
        List result = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		return result != null && !result.isEmpty()? (ManifestoEntregaDocumento) result.get(0):null;
    }

    @SuppressWarnings("unchecked")
    public List<ManifestoEntregaDocumento> findManifestoByDoctoServicoManifesto(Long idDoctoServico, Long idManifesto) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "MED")
                .createAlias("MED.doctoServico", "DS")
                .createAlias("MED.manifestoEntrega", "ME")
				.createAlias("ME.manifesto","M")
                .createAlias("DS.filialByIdFilialOrigem", "FO")
				.setFetchMode("M.controleCarga",FetchMode.JOIN)
                .add(Restrictions.eq("DS.id", idDoctoServico))
                .add(Restrictions.eq("M.id", idManifesto))
                .add(Restrictions.isNull("MED.dhOcorrencia.value"))
				.add(Restrictions.ne("M.tpStatusManifesto","CA"))
                .addOrder(Order.desc("MED.id"));

        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

    /**
     * Retorna os manifestos que tenham ocorrências de não entrega relacionadas
     * ao documento de serviço e que a responsabilidade não seja da TNT
     *
     * @param idDoctoServico
     * @param tpOcorrencia
     * @param ocasionadoTnt
     * @return
     */
    public List<ManifestoEntregaDocumento> findManifestoByIdDoctoServicoSemOcorrenciaEntregaENaoOcasionadoTnt(Long idDoctoServico) {
        DetachedCriteria dc = DetachedCriteria.forClass(ManifestoEntregaDocumento.class, "med")
                .createAlias("med.ocorrenciaEntrega", "oe")
                .add(Restrictions.eq("med.doctoServico.idDoctoServico", idDoctoServico))
                .add(Restrictions.ne("oe.tpOcorrencia", "E"))
                .add(Restrictions.eq("oe.blOcasionadoMercurio", Boolean.FALSE));

        return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	
    /**
	 * Retorna o manifesto que tenham ocorrências de entrega nula,
	 * que o tipo de situação do documento seja Pendente de Baixa (PBAI),
	 * e que o documento de serviço e a data e a hora do fechamento for nula.
	 * JIRA: EDI-1061
     *
     * @param idDoctoServico
	 * @return idDoctoServico (para comparações ao lado client), número do conhecimento, sigla da filial, id manifesto de entrega
     *
     */
	
    @SuppressWarnings("unchecked")
    public Map<String, Object> findManifestoByIdDoctoServicoSemOcorrenciaEntregaEPendenteDeBaixa(Long doctoServico) {
        SqlTemplate hql = new SqlTemplate();
        StringBuffer pj = new StringBuffer()
                .append(" new map( ")
                .append("   med.doctoServico.idDoctoServico as idDoctoServico, ")
                .append("   med.doctoServico.nrDoctoServico as nrConhecimento, ")
                .append("   med.doctoServico.filialByIdFilialOrigem.sgFilial as sgFilial, ")
                .append("   med.manifestoEntrega.idManifestoEntrega as idManifesto ")
                .append(")");
        hql.addProjection(pj.toString());
        hql.addFrom(new StringBuilder().append(ManifestoEntregaDocumento.class.getName()).append(" med ").toString());
        hql.addCustomCriteria(" med.ocorrenciaEntrega is null ");
        hql.addCustomCriteria(" med.manifestoEntrega.dhFechamento is null ");
        hql.addCustomCriteria(" med.tpSituacaoDocumento in ('PBAI', 'PBCO')");
        hql.addCustomCriteria(" med.doctoServico.idDoctoServico = ?", doctoServico);
        return (Map<String, Object>) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

    public List<Map<String, Object>> findUltimasOcorrenciasByDoctoServico(Long idDoctoServico) {
        TypedFlatMap criteria = new TypedFlatMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select MED.ID_MANIFESTO_ENTREGA_DOCUMENTO as ID_MANIFESTO_ENTREGA_DOCUMENTO "
                + ", MED.ID_DOCTO_SERVICO as ID_DOCTO_SERVICO "
                + ", OE.CD_OCORRENCIA_ENTREGA as CD_OCORRENCIA_ENTREGA "
                + ", MED.DH_OCORRENCIA as DH_OCORRENCIA ")
                .append("   from MANIFESTO_ENTREGA_DOCUMENTO MED ")
                .append("      , OCORRENCIA_ENTREGA OE ")
                .append("  where MED.ID_OCORRENCIA_ENTREGA = OE.ID_OCORRENCIA_ENTREGA ")
                .append("    and MED.DH_OCORRENCIA is not null ");

        if (idDoctoServico != null) {
			sql.append(" and MED.ID_DOCTO_SERVICO = "+idDoctoServico+" ");
        } else {
            return new ArrayList<Map<String, Object>>();
        }

        sql.append(" order by MED.DH_OCORRENCIA desc ");

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_MANIFESTO_ENTREGA_DOCUMENTO", Hibernate.LONG);
                sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
                sqlQuery.addScalar("CD_OCORRENCIA_ENTREGA", Hibernate.LONG);
                sqlQuery.addScalar("DH_OCORRENCIA", Hibernate.DATE);
            }
        };

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), criteria, csq);
    }

    /**
     * Busca todos os conhecimentos que não tiveram sucesso de entrega e estão
     * parados há mais de 1 hora.
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findRecalcularDpeHorario() {        
        /**
         * Foi realizado a pesquisa de clientes em memória devido ao custo
         * cardinal da query completa com inner join.
         *
         * A query original era executada em quase duas horas, passando para em
         * média, dez segundos.
         */

        StringBuilder sql;

        /**
         * Query 01: Encontra clientes que gerem cálculo de novo de DPE
         */
        sql = new StringBuilder();

        sql.append("select\n");
        sql.append("  c.id_cliente as idCliente\n");
        sql.append("from\n");
        sql.append("  cliente c\n");
        sql.append("where 1 = 1\n");
        sql.append("  and c.bl_gera_novo_dpe = 'S'"); // Gera DPE

        List clientes = getAdsmHibernateTemplate().findBySql(sql.toString(), null, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("idCliente", Hibernate.LONG);
            }

        });

        if (clientes == null || clientes.isEmpty()) {
            return new ArrayList<Map<String, Object>>();
        }

        String idClientes = StringUtils.join(clientes, ", ");

        /**
         * Query 02: Resgata todos os docto_servico emitidos a x horas
         */
        sql = new StringBuilder();

        sql.append("select\n");
		sql.append("  ds.id_docto_servico        as iddoctoservico,\n");
		sql.append("  ds.id_filial_destino       as idpessoaorigem,\n");
		sql.append("  ds.id_cliente_destinatario as idpessoadestino\n");
		sql.append("from\n");
		sql.append("  manifesto_entrega_documento med\n");
		sql.append("inner join docto_servico ds      on med.id_docto_servico      = ds.id_docto_servico\n");
		sql.append("inner join ocorrencia_entrega oe on med.id_ocorrencia_entrega = oe.id_ocorrencia_entrega\n");
		sql.append("where\n");
		sql.append("  oe.bl_gera_novo_dpe                     = 'S'\n");
		sql.append("  and sys_extract_utc(med.dh_ocorrencia) >= sysdate-(5/24)\n");
		sql.append("  and sys_extract_utc(med.dh_ocorrencia) <= sysdate-(1/24)\n");
		sql.append("  and\n");
		sql.append("    (\n");
		sql.append("      exists\n");
		sql.append("      (\n");
		sql.append("        select\n");
		sql.append("          1\n");
		sql.append("        from\n");
		sql.append("          novo_dpe_docto_servico ns\n");
		sql.append("        where\n");
		sql.append("          ns.id_docto_servico       = ds.id_docto_servico\n");
		sql.append("          and trunc(ns.novo_dt_prev_entrega) = trunc(sysdate)\n");
		sql.append("      )\n");
		sql.append("    or trunc(med.dh_ocorrencia) = trunc(ds.dt_prev_entrega)\n");
		sql.append("    )\n");
		sql.append("  and not exists\n");
		sql.append("    (\n");
		sql.append("      select\n");
		sql.append("        1\n");
		sql.append("      from\n");
		sql.append("        novo_dpe_docto_servico ns\n");
		sql.append("      where\n");
		sql.append("        ns.id_docto_servico         = ds.id_docto_servico\n");
		sql.append("        and ns.novo_dt_prev_entrega > trunc(sysdate)\n");
		sql.append("    )\n");
		sql.append("  and med.id_ocorrencia_entrega        != 5\n");
		sql.append("  and ds.id_localizacao_mercadoria not in (126, 139, 182, 183)\n");
        sql.append("  and ds.id_cliente_remetente in (").append(idClientes).append(")");

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), null, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("idDoctoServico", Hibernate.LONG);
                query.addScalar("idPessoaOrigem", Hibernate.LONG);
                query.addScalar("idPessoaDestino", Hibernate.LONG);
            }
        });
    }

    public List<Map<String, Object>> findRecalcularDpeDiario(int diaEmissaoDoctoServico) {
        /**
         * Foi realizado a pesquisa de clientes em memória devido ao custo
         * cardinal da query completa com inner join.
         *
         * A query original era executada em quase duas horas, passando para em
         * média, dez segundos.
         */

        StringBuilder sql;

        /**
         * Query 01: Encontra clientes que gerem cálculo de novo de DPE
         */
        sql = new StringBuilder();

        sql.append("select\n");
        sql.append("  c.id_cliente as idCliente\n");
        sql.append("from\n");
        sql.append("  cliente c\n");
        sql.append("where 1 = 1\n");
        sql.append("  and c.bl_gera_novo_dpe = 'S'"); // Gera DPE

        List clientes = getAdsmHibernateTemplate().findBySql(sql.toString(), null, new ConfigureSqlQuery() {

            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("idCliente", Hibernate.LONG);
            }

        });

        if (clientes == null || clientes.isEmpty()) {
            return new ArrayList<Map<String, Object>>();
        }

        String idClientes = StringUtils.join(clientes, ", ");

        /**
         * Query 02: Resgata todos os docto_servico emitidos a x dias
         */
        sql = new StringBuilder();

        sql.append("select\n");
        sql.append("  ds.id_docto_servico        as idDoctoServico,\n");
        sql.append("  ds.id_filial_destino       as idPessoaOrigem,\n");
        sql.append("  ds.id_cliente_destinatario as idPessoaDestino\n");
        sql.append("from\n");
        sql.append("  docto_servico ds\n");
        sql.append("where 1 = 1\n");
        sql.append("and ds.id_filial_localizacao = ds.id_filial_destino\n"); //  Se encontram da filial destino
        sql.append("and trunc(cast(ds.dh_emissao as date)) >= trunc(sysdate) - ").append(diaEmissaoDoctoServico).append("\n"); // Deve ter sido emitido a no máximo x dias
        sql.append("and ds.id_localizacao_mercadoria not in (126, 139, 182, 183)\n"); // Não sejam ocorrências Finalizadoras;

        // Somente serão calculados DPEs caso encontrem um novo prazo combinado para o dia de hoje
        sql.append("and\n");
        sql.append("  (\n");
        sql.append("    exists\n");
        sql.append("    (\n");
        sql.append("      select\n");
        sql.append("        1\n");
        sql.append("      from\n");
        sql.append("        novo_dpe_docto_servico ns\n");
        sql.append("      where\n");
        sql.append("        ns.id_docto_servico       = ds.id_docto_servico\n");
        sql.append("      and ns.novo_dt_prev_entrega = trunc(sysdate)\n");
        sql.append("    )\n");
        sql.append("  or ds.dt_prev_entrega <= trunc(sysdate)\n");
        sql.append("  )\n");

        // Somente serão calculados DPEs que não possuirem manifesto
        sql.append("and not exists\n");
        sql.append("  (\n");
        sql.append("    select\n");
        sql.append("      med.id_docto_servico\n");
        sql.append("    from\n");
        sql.append("      manifesto_entrega_documento med\n");
        sql.append("    inner join manifesto_entrega me on med.id_manifesto_entrega = me.id_manifesto_entrega\n");
        sql.append("    inner join manifesto m          on me.id_manifesto_entrega  = m.id_manifesto\n");
        sql.append("    where\n");
        sql.append("      med.id_docto_servico         = ds.id_docto_servico\n");
        sql.append("    and m.tp_status_manifesto not in ('FE','CA')\n");
        sql.append("  )\n");

        // Somente serão calculados DPEs que não foram calculados ou que a data de previsão de entrega seja menor que hoje
        sql.append("and not exists\n");
        sql.append("  (\n");
        sql.append("    select\n");
        sql.append("      1\n");
        sql.append("    from\n");
        sql.append("      novo_dpe_docto_servico ns\n");
        sql.append("    where\n");
        sql.append("      ns.id_docto_servico       = ds.id_docto_servico\n");
        sql.append("    and ns.novo_dt_prev_entrega > trunc(sysdate)\n");
        sql.append("  )\n");
        sql.append("and ds.id_cliente_remetente in (").append(idClientes).append(")");

        return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), null, new ConfigureSqlQuery() {
            
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("idDoctoServico", Hibernate.LONG);
                query.addScalar("idPessoaOrigem", Hibernate.LONG);
                query.addScalar("idPessoaDestino", Hibernate.LONG);
            }
            
        });
    }

    public List findNomeRecebedorByIdDoctoServico(Long idDoctoServico) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idDoctoServico", idDoctoServico);

        StringBuilder queryEvento = new StringBuilder();
        queryEvento.append("SELECT med.nm_recebedor as nmRecebedor \n");
        queryEvento.append("FROM docto_servico ds, \n");
        queryEvento.append("  manifesto_entrega_documento med \n");

        queryEvento.append("WHERE ds.id_docto_servico = :idDoctoServico \n");
        queryEvento.append("AND ds.id_docto_servico = med.id_docto_servico \n");

        queryEvento.append(" order by med.id_manifesto_entrega_documento desc \n");

        return getAdsmHibernateTemplate().findBySql(queryEvento.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("nmRecebedor", Hibernate.STRING);
            }
        }, AliasToNestedMapResultTransformer.getInstance());
    }

	public List findDadosNotasEntregaParcial(Long idManifestoEntrega, Long idDoctoServico ) {
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("idManifestoEntrega", idManifestoEntrega);
        parameters.put("idDoctoServico", idDoctoServico);
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select nfc.NR_CHAVE as nrChave, nfc.NR_NOTA_FISCAL as nrNotaFiscal,  oe.CD_OCORRENCIA_ENTREGA as cdOcorrenciaEntrega,  oe.ID_OCORRENCIA_ENTREGA as idOcorrenciaEntrega, ")
			.append(" med.id_manifesto_entrega_documento as idManifestoEntregaDocumento, ")
			.append(" nfc.id_nota_fiscal_conhecimento as idNotaFiscalConhecimento ")
			.append(" from manifesto_entrega_documento med, nota_fiscal_conhecimento nfc, ocorrencia_entrega oe")
			.append(" where")
			.append(" med.ID_DOCTO_SERVICO = nfc.ID_CONHECIMENTO ")
			.append(" and med.ID_DOCTO_SERVICO = :idDoctoServico ")
			.append(" and med.ID_OCORRENCIA_ENTREGA = oe.ID_OCORRENCIA_ENTREGA (+)")
			.append(" and med.ID_MANIFESTO_ENTREGA = :idManifestoEntrega");
		  
		  
		return getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, new ConfigureSqlQuery() {
            @Override
            public void configQuery(SQLQuery query) {
                query.addScalar("nrChave", Hibernate.STRING);
                query.addScalar("nrNotaFiscal", Hibernate.INTEGER);
                query.addScalar("cdOcorrenciaEntrega", Hibernate.SHORT);
                query.addScalar("idOcorrenciaEntrega", Hibernate.LONG);
                query.addScalar("idManifestoEntregaDocumento", Hibernate.LONG);
                query.addScalar("idNotaFiscalConhecimento", Hibernate.LONG);
                
            }
        }, AliasToNestedMapResultTransformer.getInstance());
		
	}

	public boolean isFormaBaixaByIdDoctoServico(Long idDoctoServico){
        StringBuilder projection = new StringBuilder();

        projection.append("SELECT TP_FORMA_BAIXA AS formaBaixa FROM MANIFESTO_ENTREGA_DOCUMENTO ")
                  .append("WHERE ID_DOCTO_SERVICO = :idDoctoServico ")
                  .append("AND (TP_FORMA_BAIXA = 'C' OR TP_FORMA_BAIXA = 'O' ) ");

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("idDoctoServico", idDoctoServico);

        List<Object[]> retorno = getAdsmHibernateTemplate()
                .findBySql(
                        projection.toString(),
                        param,
                        new ConfigureSqlQuery() {
                            @Override
                            public void configQuery(SQLQuery sql) {
                                sql.addScalar("formaBaixa", Hibernate.STRING);
                            }
                        }
                );

        return !retorno.isEmpty();
    }

}