package com.mercurio.lms.expedicao.model.dao;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescargaVolume;
import com.mercurio.lms.carregamento.model.ManifestoNacionalVolume;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.VolumeSobra;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.NfItemMda;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.sim.model.EventoVolume;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class NotaFiscalConhecimentoDAO extends BaseCrudDao<NotaFiscalConhecimento, Long> {

    private static final int DIAS = 30;
    private static final String HQL_ID_CONHECIMENTO = "idConhecimento";
    private static final String HQL_TBL_NOTA_FISCAL_CONHECIMENTO_NFC = "NotaFiscalConhecimento nfc ";

    protected void initFindListLazyProperties(Map lazyFindList) {
        lazyFindList.put("nfDadosComps", FetchMode.SELECT);
        super.initFindListLazyProperties(lazyFindList);
    }

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return NotaFiscalConhecimento.class;
    }

    /**
     * Servi�o para o app (iPhone e Android).
     *
     * @param nrNotaFiscal
     * @param destRmt
     * @param nrIdentificacao
     * @return
     */
    public Long findNFConhecimentoByNrNotaFiscal(Integer nrNotaFiscal, int destRmt, String nrIdentificacao) {

        ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar(HQL_ID_CONHECIMENTO, Hibernate.LONG);
            }
        };

        HibernateCallback hcb = findBySql(mountSql(destRmt), nrNotaFiscal, nrIdentificacao, csq);
        List idConhecimentoList = (List) getAdsmHibernateTemplate().execute(hcb);

        if (!idConhecimentoList.isEmpty()) {
            return (Long) idConhecimentoList.get(0);
        } else {
            return null;
        }


    }


    private static HibernateCallback findBySql(final String sql, final Integer nrNotaFiscal, final String nrIdentificacao,
                                               final ConfigureSqlQuery configQuery) {

        return new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql);

                // chama a impl que configura a SQLQuery.
                if (configQuery != null) {
                    configQuery.configQuery(query);
                }

                query.setParameter(0, nrNotaFiscal, Hibernate.INTEGER);
                query.setParameter(1, nrIdentificacao, Hibernate.STRING);
                return query.list();
            }

        };
    }

    private String mountSql(int destRmt) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(" SELECT /*+ leading(notafiscal conhecimento doctoservico localizacao pessoa cliente) */ ")
                .append("        conhecimento.id_conhecimento AS idConhecimento ")
                .append(" FROM nota_fiscal_conhecimento notafiscal, ")
                .append("      conhecimento             conhecimento, ")
                .append("      docto_servico            doctoservico, ")
                .append("      localizacao_mercadoria   localizacao, ")
                .append("      cliente                  cliente, ")
                .append("      pessoa                   pessoa ")
                .append(" WHERE notafiscal.id_conhecimento = conhecimento.id_conhecimento ")
                .append("       AND conhecimento.id_conhecimento = doctoservico.id_docto_servico ")
                .append("       AND doctoservico.id_localizacao_mercadoria = localizacao.id_localizacao_mercadoria ");

        if (destRmt == 0) { //remetente
            stringBuilder.append(" AND doctoservico.id_cliente_remetente = pessoa.id_pessoa ");
        } else if (destRmt == 1) {
            stringBuilder.append(" AND doctoservico.id_cliente_destinatario = pessoa.id_pessoa ");
        }

        stringBuilder.append(" AND cliente.id_cliente = pessoa.id_pessoa ")
                .append("             AND notafiscal.nr_nota_fiscal = ? ")
                .append("             AND pessoa.nr_identificacao = ? ")
                .append("             ORDER BY doctoservico.dh_emissao DESC ");

        return stringBuilder.toString();
    }


    /**
     * Método utilizado pela Integração
     *
     * @param idConhecimento
     * @param nrNotaFiscal
     * @return <NotaFiscalConhecimento>
     * @author Andre Valadas
     */
    public NotaFiscalConhecimento findNotaFiscalConhecimento(Long idConhecimento, Integer nrNotaFiscal) {
        DetachedCriteria dc = createDetachedCriteria()
                .add(Restrictions.eq("conhecimento.id", idConhecimento))
                .add(Restrictions.eq("nrNotaFiscal", nrNotaFiscal));
        return (NotaFiscalConhecimento) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerie(Long idRemetente, List nrNotasFiscais,
                                                            List<YearMonthDay> datas, String dsSerie) {
        List<Object> parameters = new ArrayList<Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select nfc.nrNotaFiscal, c.idDoctoServico from ");
        hql.append(NotaFiscalConhecimento.class.getName());
        hql.append(" nfc join nfc.conhecimento c ");
        hql.append(" where nfc.cliente.id = ? ");
        hql.append(" and c.tpSituacaoConhecimento != 'C' ");
        parameters.add(idRemetente);

        if (StringUtils.isNotBlank(dsSerie)) {
            hql.append(" and nfc.dsSerie = ? ");
            parameters.add(dsSerie);
        }

        if (nrNotasFiscais != null && !nrNotasFiscais.isEmpty()) {
            hql.append(" and ( ");
            for (int i = 0; i < nrNotasFiscais.size(); i++) {
                hql.append(" (nfc.nrNotaFiscal = ? and nfc.dtEmissao = ?) ");
                parameters.add(nrNotasFiscais.get(i));
                parameters.add(datas.get(i));
                if (i + 1 < nrNotasFiscais.size()) {
                    hql.append(" or ");
                }

            }
            hql.append(" ) ");
        }

        return getAdsmHibernateTemplate().find(hql.toString(), parameters.toArray());
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerie(Long idRemetente, List nrNotas, List dsSeries) {
        return findListByRemetenteNrNotasDsSerieConhecimentoOriginal(idRemetente, nrNotas, dsSeries, null, null);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerieTipoDoc(Long idRemetente, List nrNotas, List dsSeries, String tipoDoc) {
        return findListByRemetenteNrNotasDsSerieConhecimentoOriginal(idRemetente, nrNotas, dsSeries, null, tipoDoc);
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerieConhecimentoOriginal(Long idRemetente, List nrNotas, List dsSeries, Long idConhecimentoOriginal, String tipoDoc) {
        List<Object> parameters = new ArrayList<Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select nfc.nrNotaFiscal, c.idDoctoServico from ");
        hql.append(NotaFiscalConhecimento.class.getName());
        hql.append(" nfc join nfc.conhecimento c ");

        hql.append(" where nfc.cliente.id = ? ");
        parameters.add(idRemetente);

        hql.append(" and c.tpSituacaoConhecimento != 'C' ");

        if (idConhecimentoOriginal != null) {
            hql.append(" and c.idDoctoServico != ? ");
            parameters.add(idConhecimentoOriginal);
        }

        if (tipoDoc != null) {
            hql.append(" and nfc.tpDocumento = ? ");
            parameters.add(tipoDoc);
        }


        if (nrNotas != null && !nrNotas.isEmpty()) {
            hql.append(" and ( ");
            for (int i = 0; i < nrNotas.size(); i++) {
                hql.append(" (nfc.nrNotaFiscal = ? ");
                parameters.add(nrNotas.get(i));
                if (dsSeries.get(i) != null) {
                    hql.append("and nfc.dsSerie = ?");
                    parameters.add(dsSeries.get(i));
                } else {
                    hql.append("and nfc.dsSerie is null");
                }
                hql.append(")");

                if (i + 1 < nrNotas.size()) {
                    hql.append(" or ");
                }

            }
            hql.append(" ) ");
        }

        return getAdsmHibernateTemplate().find(hql.toString(), parameters.toArray());
    }

    public List<Object[]> findListByRemetenteNrNotasDsSerie(Long idRemetente, List nrNotas, List dsSeries, Long idConhecimentoOriginal, String tipoDoc) {
        List<Object> parameters = new ArrayList<Object>();
        StringBuilder hql = new StringBuilder();
        hql.append(" select nfc.nrNotaFiscal, nfc.dsSerie from ");
        hql.append(NotaFiscalConhecimento.class.getName());
        hql.append(" nfc join nfc.conhecimento c ");

        hql.append(" where nfc.cliente.id = ? ");
        parameters.add(idRemetente);

        hql.append(" and c.tpSituacaoConhecimento != 'C' ");

        if (idConhecimentoOriginal != null) {
            hql.append(" and c.idDoctoServico != ? ");
            parameters.add(idConhecimentoOriginal);
        }

        if (tipoDoc != null) {
            hql.append(" and nfc.tpDocumento = ? ");
            parameters.add(tipoDoc);
        }


        if (nrNotas != null && !nrNotas.isEmpty()) {
            hql.append(" and ( ");
            for (int i = 0; i < nrNotas.size(); i++) {
                hql.append(" (nfc.nrNotaFiscal = ? ");
                parameters.add(nrNotas.get(i));
                if (dsSeries.get(i) != null) {
                    hql.append("and LPAD(nfc.dsSerie, 100, 0) = ?");
                    String leftPaddedSeries = StringUtils.leftPad(dsSeries.get(i).toString().trim(), 100, "0");
                    parameters.add(leftPaddedSeries);
                } else {
                    hql.append("and nfc.dsSerie is null");
                }
                hql.append(")");

                if (i + 1 < nrNotas.size()) {
                    hql.append(" or ");
                }

            }
            hql.append(" ) ");
        }

        return getAdsmHibernateTemplate().find(hql.toString(), parameters.toArray());
    }


    public List findListByCriteriaByMda(Long idMda) {
        DetachedCriteria dc = DetachedCriteria.forClass(NotaFiscalConhecimento.class)
                .setFetchMode("nfItemMdas.itemMda.mda", FetchMode.JOIN)
                .createAlias("nfItemMdas", "nfItemMda")
                .createAlias("nfItemMda.itemMda", "itemMda")
                .createAlias("itemMda.mda", "mda")
                .add(Restrictions.eq("mda.id", idMda))
                .addOrder(Order.asc("nrNotaFiscal"));
        return super.findByDetachedCriteria(dc);
    }

    public List findByConhecimento(Long idConhecimento) {
        DetachedCriteria dc = createDetachedCriteria()
                .add(Restrictions.eq("conhecimento.id", idConhecimento));
        return findByDetachedCriteria(dc);
    }


    public NotaFiscalConhecimento findByIdConhecimento(Long idNotaFiscalConhecimento) {
        DetachedCriteria dc = createDetachedCriteria()
                .setFetchMode("conhecimento", FetchMode.JOIN)
                .setFetchMode("conhecimento.filialOrigem", FetchMode.JOIN)
                .setFetchMode("notaFiscalConhecimento", FetchMode.JOIN)
                .add(Restrictions.eq("idNotaFiscalConhecimento", idNotaFiscalConhecimento));
        return (NotaFiscalConhecimento) getAdsmHibernateTemplate().findUniqueResult(dc);
    }

    public List findByMonitoramentoDescargaByBlPsAferido(Long idMonitoramentoDescarga, Boolean blPesoAferido) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select distinct nofc ");
        sql.append("from NotaFiscalConhecimento as nofc, ");
        sql.append("	Conhecimento as conh, ");
        sql.append("	VolumeNotaFiscal as vonf, ");
        sql.append("	MonitoramentoDescarga as mode ");
        sql.append("where nofc.conhecimento.idDoctoServico = conh.idDoctoServico ");
        sql.append("	and ((conh.blPesoAferido = :blPesoAferido) OR (conh.blPesoAferido is null and 'N' = :blPesoAferido)) ");
        sql.append("	and vonf.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento ");
        sql.append("	and mode.idMonitoramentoDescarga = :idMonitoramentoDescarga ");
        sql.append("	and mode.idMonitoramentoDescarga = vonf.monitoramentoDescarga.idMonitoramentoDescarga ");
        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), new String[]{"blPesoAferido", "idMonitoramentoDescarga"}, new Object[]{blPesoAferido, idMonitoramentoDescarga});
    }

    public List findIdsByIdConhecimento(Long idConhecimento) {
        String sql = "select pojo.idNotaFiscalConhecimento " +
                "from " + NotaFiscalConhecimento.class.getName() + " as pojo " +
                "join pojo.conhecimento as c " +
                "where c.id = :idConhecimento ";
        return getAdsmHibernateTemplate().findByNamedParam(sql, HQL_ID_CONHECIMENTO, idConhecimento);
    }

    public List<Map> findIdNrNotaByIdConhecimento(Long idConhecimento) {
        String sql = "select new map( pojo.id as idNotaFiscalConhecimento, pojo.nrNotaFiscal as nrNotaFiscal, pojo.qtVolumes as qtVolumes ) " +
                " from " + NotaFiscalConhecimento.class.getName() + " as pojo " +
                " join pojo.conhecimento as c " +
                " where c.id = :idConhecimento ";
        return getAdsmHibernateTemplate().findByNamedParam(sql, HQL_ID_CONHECIMENTO, idConhecimento);
    }

    public List<Map<String, Object>> findIdNrNotaEtiquetByIdConhecimento(Long idConhecimento) {
        String sql = "select new map( pojo.id as idNotaFiscalConhecimento, pojo.nrNotaFiscal as nrNotaFiscal, pojo.qtVolumes as qtVolumes ) " +
                " from " + NotaFiscalConhecimento.class.getName() + " as pojo " +
                " join pojo.conhecimento as c " +
                " where c.id = :idConhecimento ";
        List<Map<String, Object>> notaFiscal = getAdsmHibernateTemplate().findByNamedParam(sql, HQL_ID_CONHECIMENTO, idConhecimento);
        return notaFiscal;
    }

    public List<Map<String, Object>> findNFByIdDoctoServico(Long idDoctoServico) {
        SqlTemplate hql = new SqlTemplate();

        hql.addProjection("new Map(nfc.nrNotaFiscal as nrNotaFiscal, " +
                "nfc.idNotaFiscalConhecimento as idNotaFiscalConhecimento, " +
                "nfc.dsSerie as dsSerie, " +
                "nfc.dtEmissao as dtEmissao, " +
                "nfc.qtVolumes as qtVolumes, " +
                "nfc.psMercadoria as psMercadoria, " +
                "nfc.vlTotal as vlTotal, " +
                "nfc.dtSaida as dtSaida, " +
                "cast(nfc.nrCfop as integer) as nrCfop, " +
                "nfc.vlBaseCalculo as vlBaseCalculo, " +
                "nfc.vlIcms as vlIcms, " +
                "nfc.psCubado as psCubado, " +
                "nfc.psAferido as psAferido, " +
                "nfc.nrChave as nrChave, " +
                "nfc.vlTotalProdutos as vlTotalProdutos, " +
                "nfc.vlBaseCalculoSt as vlBaseCalculoSt, " +
                "nfc.vlIcmsSt as vlIcmsSt, " +
                "nfc.nrPinSuframa as nrPinSuframa, " +
                "nfc.nrChave as nrChave, " +
                "nfc.tpDocumento as tpDocumento, " +
                "moeda.dsSimbolo as dsSimbolo, " +
                "moeda.sgMoeda as sgMoeda)");

        hql.addFrom(HQL_TBL_NOTA_FISCAL_CONHECIMENTO_NFC +
                "join nfc.conhecimento conh " +
                "join conh.moeda moeda");
        hql.addCriteria("conh.idDoctoServico", "=", idDoctoServico);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public List findNFByIdConhecimento(Long idConhecimento) {
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("min(nfc.nrNotaFiscal)");
        hql.addFrom(HQL_TBL_NOTA_FISCAL_CONHECIMENTO_NFC);
        hql.addCriteria("nfc.conhecimento.id", "=", idConhecimento);

        return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }

    public List findNFByIdConhecimentoAndIdsClienteDevedor(Long idConhecimento, List<Long> idsCliente) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idConhecimento", idConhecimento);
        parametersValues.put("idsClientes", idsCliente);

        StringBuilder query = new StringBuilder();
        query.append(" select nfc");
        query.append(" from NotaFiscalConhecimento as nfc, ");
        query.append("		DoctoServico as ds, ");
        query.append("		DevedorDocServ as dds ");
        query.append(" where nfc.conhecimento.id = ds.id");
        query.append("	 and ds.id = dds.doctoServico.id");
        query.append("	 and ds.id = :idConhecimento");
        query.append("	 and dds.cliente.id in (:idsClientes)");

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }

    public List findPaginatedByIdAwb(Long idAwb) {

        StringBuilder hql = new StringBuilder()
                .append(" select new map( ")
                .append("        nfc.idNotaFiscalConhecimento as idNotaFiscalConhecimento, ")
                .append("        nfc.nrNotaFiscal as nrNotaFiscal, ")
                .append("        nfc.psMercadoria as psMercadoria, ")
                .append("        nfc.qtVolumes as qtVolumes, ")
                .append("        nfc.vlTotal as vlTotal ")
                .append(" ) ")
                .append(" from ").append(getPersistentClass().getName()).append(" nfc ")
                .append(" join nfc.conhecimento nfcc, ")
                .append(CtoAwb.class.getName()).append(" ca ")
                .append(" where ca.conhecimento.id = nfcc.id ")
                .append("   and ca.awb.id = ? ");

        return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idAwb});
    }

    public Integer getRowCountByIdAwb(Long idAwb) {

        Integer count = IntegerUtils.ZERO;

        StringBuilder hql = new StringBuilder()
                .append(" select count (nfc.id) ")
                .append(" from ").append(getPersistentClass().getName()).append(" nfc ")
                .append(" join nfc.conhecimento nfcc, ")
                .append(CtoAwb.class.getName()).append(" ca ")
                .append(" where ca.conhecimento.id = nfcc.id ")
                .append("   and ca.awb.id = ? ");

        List result = getAdsmHibernateTemplate().find(hql.toString(), idAwb);
        if (result != null && !result.isEmpty()) {
            count = (Integer) result.get(0);
        }

        return count;
    }

    private String getFromNotaFiscalCliente() {

        StringBuilder hql = new StringBuilder()
                .append(" from ").append(NotaFiscalConhecimento.class.getName()).append(" nfc ")
                .append(" join nfc.conhecimento conh ")
                .append(" join conh.moeda moeda ")

                .append(" join conh.filialByIdFilialOrigem fior ")
                .append(" join fior.pessoa pefior ")

                .append(" left join conh.filialByIdFilialDestino fide ")
                .append(" left join fide.pessoa pefide ")

                .append(" left join nfc.cliente cliRe")
                .append(" join cliRe.pessoa pesRe")

		/*.append(" left join conh.clienteByIdClienteRemetente clre ")
        .append(" join clre.pessoa pere ")*/

                .append(" left join conh.clienteByIdClienteDestinatario clde ")
                .append(" join clde.pessoa pede ");

        return hql.toString();
    }

    private HashMap getWhereNotaFiscalCliente(TypedFlatMap criteria) {
        HashMap map = new HashMap();

        List list = new ArrayList();

        StringBuilder hql = new StringBuilder()
                .append(" where 1=1 ");

        StringBuilder stringBuilder = new StringBuilder();

        if (criteria.getInteger("nrNotaFiscal") != null) {
            stringBuilder.append(" and nfc.nrNotaFiscal = ? ");
            list.add(criteria.getInteger("nrNotaFiscal"));
        }

        if (criteria.getYearMonthDay("periodoEmissaoInicial") != null) {
            stringBuilder.append(" and nfc.dtEmissao >= ? ");
            list.add(criteria.getYearMonthDay("periodoEmissaoInicial"));
        }
        if (criteria.getYearMonthDay("periodoEmissaoFinal") != null) {
            stringBuilder.append(" and nfc.dtEmissao <= ? ");
            list.add(criteria.getYearMonthDay("periodoEmissaoFinal"));
        }

        if (criteria.getLong("filialOrigem.idFilial") != null) {
            stringBuilder.append(" and conh.filialByIdFilialOrigem.idFilial = ? ");
            list.add(criteria.getLong("filialOrigem.idFilial"));
        }

        if (criteria.getLong("filialDestino.idFilial") != null) {
            stringBuilder.append(" and conh.filialByIdFilialDestino.idFilial = ? ");
            list.add(criteria.getLong("filialDestino.idFilial"));
        }

        if (criteria.getLong("remetente.idCliente") != null) {
            stringBuilder.append(" and cliRe.idCliente = ? ");
            list.add(criteria.getLong("remetente.idCliente"));
        }

        if (criteria.getLong("destinatario.idCliente") != null) {
            stringBuilder.append(" and conh.clienteByIdClienteDestinatario.idCliente = ? ");
            list.add(criteria.getLong("destinatario.idCliente"));
        }

        if (criteria.getString("doctoServico.tpDocumentoServico") != null && !criteria.getString("doctoServico.tpDocumentoServico").trim().equals("")) {
            stringBuilder.append(" and conh.tpDocumentoServico = ? ");
            list.add(criteria.getString("doctoServico.tpDocumentoServico"));
        } else {
            stringBuilder.append(" and conh.tpDocumentoServico in (?, ?) ");
            list.add("NFT");
            list.add("CTR");
        }

        if (criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null) {
            stringBuilder.append(" and conh.filialByIdFilialOrigem.idFilial = ? ");
            list.add(criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
        }

        if (criteria.getLong("doctoServico.idDoctoServico") != null || criteria.getLong("idDoctoServico") != null) {
            stringBuilder.append(" and conh.idDoctoServico = ? ");
            if (criteria.getLong("doctoServico.idDoctoServico") != null) {
                list.add(criteria.getLong("doctoServico.idDoctoServico"));
            } else if (criteria.getLong("idDoctoServico") != null) {
                list.add(criteria.getLong("idDoctoServico"));
            }
        }

        hql.append(stringBuilder.toString());

        map.put("where", hql);
        map.put("parameters", list);

        return map;
    }

    private String getOrderByNotaFiscalCliente() {
        StringBuilder hql = new StringBuilder()
                .append(" order by nfc.nrNotaFiscal, nfc.dsSerie, conh.tpDocumentoServico, fior.sgFilial, conh.nrDoctoServico ");

        return hql.toString();
    }

    private StringBuilder createProjection() {
        StringBuilder hql = new StringBuilder()
                .append(" select new map( ")
                .append("        nfc.idNotaFiscalConhecimento as idNotaFiscalConhecimento, ")
                .append("        nfc.nrNotaFiscal as nrNotaFiscal, ")
                .append("        nfc.dsSerie as dsSerie, ")
                .append("        nfc.dtEmissao as dtEmissao, ")
                .append("        nfc.qtVolumes as qtVolumes, ")
                .append("        nfc.psMercadoria as psMercadoria, ")
                .append("        nfc.vlTotal as vlTotal, ")
                .append("        moeda.dsSimbolo as dsSimbolo, ")
                .append("        conh.idDoctoServico as idDoctoServico, ")
                .append("        conh.tpDocumentoServico as tpDocumentoServico, ")
                .append("        conh.nrDoctoServico as nrDoctoServico, ")

                .append("        fior.idFilial as idFilialOrigem, ")
                .append("        fior.sgFilial as sgFilialOrigem, ")
                .append("        pefior.nmFantasia as nmFantasiaOrigem, ")

                .append("        fide.sgFilial as sgFilialDestino, ")
                .append("        pefide.nmFantasia as nmFantasiaDestino, ")

                .append("        pesRe.nrIdentificacao as nrIdentificacaoRemetente, ")
                .append("        pesRe.tpIdentificacao as tpIdentificacaoRemetente, ")
                .append("        pesRe.nmPessoa as remetente, ")
                .append("        pede.nrIdentificacao as nrIdentificacaoDestinatario, ")
                .append("        pede.nmPessoa as destinatario, ")
                .append("        pede.tpIdentificacao as tpIdentificacaoDestinatario ")
                .append(" ) ")
                .append(getFromNotaFiscalCliente());

        return hql;
    }

    public ResultSetPage findPaginatedNotaFiscalCliente(TypedFlatMap criteria, FindDefinition def) {
        StringBuilder hql = createProjection();

        HashMap where = getWhereNotaFiscalCliente(criteria);

        hql.append(where.get("where"))
                .append(getOrderByNotaFiscalCliente());

        return getAdsmHibernateTemplate().findPaginated(hql.toString(), def.getCurrentPage(), def.getPageSize(), ((List) where.get("parameters")).toArray());
    }


    public List findLookupNotaFiscalCliente(TypedFlatMap criteria) {
        StringBuilder hql = createProjection();

        HashMap where = getWhereNotaFiscalCliente(criteria);

        hql.append(where.get("where"))
                .append(getOrderByNotaFiscalCliente());

        return getAdsmHibernateTemplate().find(hql.toString(), ((List) where.get("parameters")).toArray());
    }

    public Long getRowCountNotaFiscalCliente(TypedFlatMap criteria) {
        Long count = LongUtils.ZERO;

        StringBuilder hql = new StringBuilder()
                .append(" select count(*) ")
                .append(getFromNotaFiscalCliente());

        HashMap where = getWhereNotaFiscalCliente(criteria);
        hql.append(where.get("where"));

        List result = getAdsmHibernateTemplate().find(hql.toString(), ((List) where.get("parameters")).toArray());
        if (result != null && !result.isEmpty()) {
            count = (Long) result.get(0);
        }

        return count;
    }

    /**
     * Remove todas as notas fiscais vinculadas ao conhecimento recebido.
     *
     * @param idConhecimento identificador do conhecimento
     */
    public void removeByIdConhecimento(Long idConhecimento) {
        StringBuilder hql0 = new StringBuilder()
                .append(" delete ").append(EventoVolume.class.getName())
                .append(" ev where exists(from ")
                .append(VolumeNotaFiscal.class.getName() + " as vnf ")
                .append(" inner join vnf.notaFiscalConhecimento as nfc ")
                .append(" where nfc.conhecimento = :id and  vnf = ev.volumeNotaFiscal )");

        StringBuilder hqlDeleteCarregamentoDescargaVolume = new StringBuilder()
                .append(" delete ").append(CarregamentoDescargaVolume.class.getName())
                .append(" cdv where exists(from ").append(VolumeNotaFiscal.class.getName() + " as vnf ")
                .append(" inner join vnf.notaFiscalConhecimento as nfc ")
                .append(" where nfc.conhecimento = :id  and cdv.volumeNotaFiscal = vnf)");


        StringBuilder hql1 = new StringBuilder()
                .append(" delete ").append(VolumeNotaFiscal.class.getName())
                .append(" vnf where exists(from ").append(getPersistentClass().getName())
                .append(" as nfc where nfc.conhecimento = :id and  nfc = vnf.notaFiscalConhecimento )");

        StringBuilder hql2 = new StringBuilder()
                .append(" delete ").append(NfDadosComp.class.getName())
                .append(" dad where exists(from ").append(getPersistentClass().getName())
                .append(" as nfc where nfc.conhecimento = :id and  nfc = dad.notaFiscalConhecimento )");

        //----Jira LMS-5879
        StringBuilder hqlDeleteVolumeSobraFilial = new StringBuilder()
                .append(" delete ").append(VolumeSobraFilial.class.getName())
                .append(" vsf where exists(from ").append(VolumeNotaFiscal.class.getName() + " as vnf ")
                .append(" inner join vnf.notaFiscalConhecimento ")
                .append(" as nfc where nfc.conhecimento = :id and vnf = vsf.volumeNotaFiscal )");


        StringBuilder hqlDeleteManifestoNacionalVolume = new StringBuilder()
                .append(" delete ").append(ManifestoNacionalVolume.class.getName())
                .append(" mnv where exists(from ").append(VolumeNotaFiscal.class.getName() + " as vnf ")
                .append(" inner join vnf.notaFiscalConhecimento ")
                .append(" as nfc where nfc.conhecimento = :id and vnf = mnv.volumeNotaFiscal )");


        StringBuilder hqlDeletePreManifestoVolume = new StringBuilder()
                .append(" delete ").append(PreManifestoVolume.class.getName())
                .append(" pmv where exists(from ").append(VolumeNotaFiscal.class.getName() + " as vnf ")
                .append(" inner join vnf.notaFiscalConhecimento ")
                .append(" as nfc where nfc.conhecimento = :id and vnf = pmv.volumeNotaFiscal )");


        StringBuilder hqlDeleteVolumeSobra = new StringBuilder()
                .append(" delete ").append(VolumeSobra.class.getName())
                .append(" vs where exists(from ").append(VolumeNotaFiscal.class.getName() + " as vnf ")
                .append(" inner join vnf.notaFiscalConhecimento ")
                .append(" as nfc where nfc.conhecimento = :id and vnf = vs.volumeNotaFiscal )");


        StringBuilder hqlDeleteNfItemMda = new StringBuilder()
                .append(" delete ").append(NfItemMda.class.getName())
                .append(" nim where exists (from ").append(getPersistentClass().getName())
                .append(" as nfc where nfc.conhecimento = :id and nfc = nim.notaFiscalConhecimento )");


        StringBuilder hqlDeleteNotaOcorrenciaNc = new StringBuilder()
                .append(" delete ").append(NotaOcorrenciaNc.class.getName())
                .append(" non where exists (from ").append(getPersistentClass().getName())
                .append(" as nfc where nfc.conhecimento = :id and nfc = non.notaFiscalConhecimento )");
        ////----Jira LMS-5879

        StringBuilder hql = new StringBuilder()
                .append(" delete ").append(getPersistentClass().getName())
                .append(" where conhecimento = :id");

        Conhecimento conhecimento = new Conhecimento();
        conhecimento.setIdDoctoServico(idConhecimento);

    	/* hql0 - Remove eventos dos volumes */
        getAdsmHibernateTemplate().removeById(hql0.toString(), conhecimento);

    	/* Remove os VolumeSobraFilial */
        getAdsmHibernateTemplate().removeById(hqlDeleteVolumeSobraFilial.toString(), conhecimento);
    	/* Remove os ManifestoNacionalVolume */
        getAdsmHibernateTemplate().removeById(hqlDeleteManifestoNacionalVolume.toString(), conhecimento);
    	/* Remove os PreManifestoVolume */
        getAdsmHibernateTemplate().removeById(hqlDeletePreManifestoVolume.toString(), conhecimento);
    	/* Remove os VolumeSobra */
        getAdsmHibernateTemplate().removeById(hqlDeleteVolumeSobra.toString(), conhecimento);
    	/* Remove as NfItemMda */
        getAdsmHibernateTemplate().removeById(hqlDeleteNfItemMda.toString(), conhecimento);
    	/* hqlDeleteCarregamentoDescargaVolume - Remove os CarregamentoDescargaVolume */
        getAdsmHibernateTemplate().removeById(hqlDeleteCarregamentoDescargaVolume.toString(), conhecimento);
    	/* Remove as NotaOcorrenciaNc */
        getAdsmHibernateTemplate().removeById(hqlDeleteNotaOcorrenciaNc.toString(), conhecimento);
    	/* hql1 - Remove os volumes */
        getAdsmHibernateTemplate().removeById(hql1.toString(), conhecimento);
    	/* hql2 - Remove os dados complementares da nota fiscal */
        getAdsmHibernateTemplate().removeById(hql2.toString(), conhecimento);
    	/* hql - Remove as notas fiscais do conhecimento */
        getAdsmHibernateTemplate().removeById(hql.toString(), conhecimento);
    }


    @SuppressWarnings("rawtypes")
    public List findNrNotaByIdConhecimento(Long idConhecimento) {
        String sql = "select new " + NotaFiscalConhecimento.class.getName() + "(pojo.nrNotaFiscal)  " +
                " from " + NotaFiscalConhecimento.class.getName() + " as pojo " +
                " join pojo.conhecimento as c " +
                " where c.id = :idConhecimento " +
                " and rownum < 31 ";
        return getAdsmHibernateTemplate().findByNamedParam(sql, HQL_ID_CONHECIMENTO, idConhecimento);
    }

    @SuppressWarnings("rawtypes")
    public List findByNrChave(String chave) {
        // LMSA-7137
    	return findByNrChaveEClienteRemetente(chave, null);
    }

    // LMSA-7137
    public List findByNrChaveEClienteRemetente(String chave, Long idCliente) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.add(Restrictions.eq("nrChave", chave));

        if (idCliente != null) {
            criteria.add(Restrictions.eq("cliente.idCliente", idCliente));
        }

        ProjectionList projection = Projections.projectionList()
                .add(Projections.property("idNotaFiscalConhecimento"), "idNotaFiscalConhecimento")
                .add(Projections.property("conhecimento"), "conhecimento");

        criteria.setProjection(projection);


        return getAdsmHibernateTemplate().findByCriteria(criteria);
    }

    public List findByNrChaveIdConhecimentoOriginal(String chave, Long idConhecimentoOriginal) {
        DetachedCriteria criteria = DetachedCriteria.forClass(getPersistentClass());
        criteria.add(Restrictions.eq("nrChave", chave));

        if (idConhecimentoOriginal != null) {
        	criteria.add(Restrictions.ne("conhecimento.id", idConhecimentoOriginal));
        }

        ProjectionList projection = Projections.projectionList()
                .add(Projections.property("idNotaFiscalConhecimento"), "idNotaFiscalConhecimento")
                .add(Projections.property("conhecimento"), "conhecimento");

        criteria.setProjection(projection);


        return getAdsmHibernateTemplate().findByCriteria(criteria);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findDadosNotaFiscalConhecimentoCliente(String nrIdentificacaoRemetente, Integer nrNotaFiscal, String dsSerie) {
        StringBuilder hql = montarQueryNotaFiscal();
        Map<String, Object> parametersValues = montarParametrosNotaFiscal(nrIdentificacaoRemetente, nrNotaFiscal, hql);

        if (StringUtils.isNotEmpty(dsSerie)) {
            hql.append(" AND nfc.dsSerie = :dsSerie ");
            parametersValues.put("dsSerie", dsSerie);
        } else {
            hql.append(" AND nfc.dsSerie is null ");
        }

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametersValues);
    }

    public List<Map<String, Object>> findDadosNotaFiscalConhecimentoDell(String nrIdentificacaoRemetente, String numeroPedido) {
        StringBuilder hql = montarQueryNotaFiscal();
        hql.append(" INNER JOIN c.dadosComplementos dc ")
            .append(" INNER JOIN dc.informacaoDoctoCliente idc ");

        Map<String, Object> parametersValues = montarParametrosNotaFiscal(nrIdentificacaoRemetente, null, hql);

        hql.append(" AND idc.dsCampo LIKE :dsPedidoDell ");
        parametersValues.put("dsPedidoDell", "PedidoDell");

        if (StringUtils.isNotEmpty(numeroPedido)) {
           hql.append(" AND dc.dsValorCampo = :numeroPedido ");
            parametersValues.put("numeroPedido", numeroPedido);
        }

        hql.append(" ORDER BY ds.dhEmissao ASC ");

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametersValues);
    }

    private StringBuilder montarQueryNotaFiscal(){
        StringBuilder hql = new StringBuilder();

        hql.append("SELECT ")
                .append("new Map( ")
                .append("nfc.nrNotaFiscal as nrNotaFiscal, ")
                .append("nfc.dsSerie as dsSerie, ")
                .append("pr.nmPessoa as nmPessoaRemetente, ")
                .append("pd.nmPessoa as nmPessoaDestinatario, ")
                .append("ds.nrDoctoServico as nrDoctoServico, ")
                .append("ds.idDoctoServico as idDoctoServico, ")
                .append("fo.idFilial as idFilialOrigem, ")
                .append("ds.dtPrevEntrega as dtPrevEntrega ")
                .append(")")
                .append("FROM ")
                .append(NotaFiscalConhecimento.class.getSimpleName()).append(" nfc ")
                .append("INNER JOIN nfc.conhecimento c, ")
                .append(DoctoServico.class.getSimpleName()).append(" ds ")
                .append("INNER JOIN ds.clienteByIdClienteRemetente cr ")
                .append("INNER JOIN cr.pessoa pr ")
                .append("INNER JOIN ds.clienteByIdClienteDestinatario cd ")
                .append("INNER JOIN cd.pessoa pd ")
                .append("INNER JOIN ds.filialByIdFilialOrigem fo ")
                .append("INNER JOIN ds.filialByIdFilialDestino fd ");
        return hql;
    }

    private Map<String, Object> montarParametrosNotaFiscal(String nrIdentificacaoRemetente, Integer nrNotaFiscal, StringBuilder hql){
        Map<String, Object> parametersValues = new HashMap<String, Object>();
        hql.append("WHERE ")
            .append("	 c.id = ds.id ")
            .append("AND c.tpSituacaoConhecimento <> :tpSituacaoConhecimento ");

        parametersValues.put("tpSituacaoConhecimento", "C");

        if (StringUtils.isNotEmpty(nrIdentificacaoRemetente)) {
            hql.append(" AND pr.nrIdentificacao LIKE :nrIdentificacaoRemetente ");
            parametersValues.put("nrIdentificacaoRemetente", nrIdentificacaoRemetente + "%");
        }

        if (IntegerUtils.hasValue(nrNotaFiscal)) {
            hql.append(" and nfc.nrNotaFiscal = :nrNotaFiscal");
            parametersValues.put("nrNotaFiscal", nrNotaFiscal);
        }

        return parametersValues;
    }

    public List findNotasFiscaisConhecimentoIntegracaoFedex(List<Long> idsFiliaisIntegraOcorreFedex) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();

        StringBuilder query = new StringBuilder();
        query.append(" select nfc");
        query.append(" from NotaFiscalConhecimento as nfc, ");
        query.append("		ControleCarga as cc, ");
        query.append("		Manifesto as m, ");
        query.append("		ManifestoEntregaDocumento as med, ");
        query.append("		DoctoServico as ds ");
        query.append(" where cc.id = m.controleCarga.id");
        query.append("	 and m.id = med.manifestoEntrega.id");
        query.append("	 and med.doctoServico.id = ds.id");
        query.append("	 and nfc.conhecimento.id = ds.id");
        query.append("	 and cc.tpControleCarga = 'C'");
        query.append("	 and med.tpSituacaoDocumento in ('PBAI', 'PBCO', 'PBRC')");
        query.append("	 and med.dhOcorrencia is null");
        query.append("	 and m.filialByIdFilialDestino.id in (:idsFiliaisIntegraOcorreFedex)");
        query.append("	 and trunc(cast (m.dhEmissaoManifesto.value as date)) >= :dataRef ");
        query.append("	 and cc.tpStatusControleCarga in ('TC', 'ED', 'AD') ");

        parametersValues.put("idsFiliaisIntegraOcorreFedex", idsFiliaisIntegraOcorreFedex);
        parametersValues.put("dataRef", JTDateTimeUtils.getDataAtual().minusDays(DIAS));

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }

    public List<NotaFiscalConhecimento> findByNrChavesNaFilial(List<String> nrChavesList, Filial filialOrigem) {
        String hql = "select n from NotaFiscalConhecimento n where nrChave in (:nrChaves)"
                + " and n.conhecimento.filialOrigem = :filialOrigem";

        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("nrChaves", nrChavesList);
        parametersValues.put("filialOrigem", filialOrigem);
        return getAdsmHibernateTemplate().findByNamedParam(hql, parametersValues);
    }

	public List<Long> findNfDisponivel(final Long idDoctoServico) {
		final StringBuilder sql = new StringBuilder()
			.append("  SELECT NFC.id_nota_fiscal_conhecimento" )
			.append("    FROM nota_fiscal_conhecimento NFC" )
			.append("   WHERE NFC.id_conhecimento = :idDoctoServico ")
			.append("     AND NFC.id_nota_fiscal_conhecimento NOT IN ( ")
			.append("  	     SELECT NFC.id_nota_fiscal_conhecimento ")
			.append("          FROM nota_fiscal_conhecimento NFC, nota_fiscal_operada NFO ")
			.append("         WHERE NFC.id_nota_fiscal_conhecimento = NFO.id_nota_fiscal_cto_original ")
			.append("           AND NFC.id_conhecimento = :idDoctoServico ")
			.append("           AND NFO.tp_situacao IN ('DV','RF','EN') ")
			.append("    )");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.setLong("idDoctoServico", idDoctoServico);
				sqlQuery.addScalar("id_nota_fiscal_conhecimento", Hibernate.LONG);
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

		return getHibernateTemplate().executeFind(hcb);
	}
	
	@SuppressWarnings("unchecked")
    public List<Map<String, Object>> findNotasFiscaisDisponiveisByIdDoctoServico(Long idDoctoServico) {
        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idDoctoServico", idDoctoServico);
        
        final StringBuilder sql = new StringBuilder()
        .append("  SELECT new Map( ")
        .append("   NFC.idNotaFiscalConhecimento as idNotaFiscalConhecimento, ")
        .append("   NFC.nrNotaFiscal as nrNotaFiscal " )
        .append("    )FROM NotaFiscalConhecimento NFC " )
        .append("   WHERE NFC.conhecimento.id = :idDoctoServico ")
        .append("     AND NFC.idNotaFiscalConhecimento NOT IN ( ")
        .append("        SELECT NFC.idNotaFiscalConhecimento ")
        .append("          FROM NotaFiscalConhecimento NFC, NotaFiscalOperada NFO ")
        .append("         WHERE NFC.idNotaFiscalConhecimento = NFO.notaFiscalConhecimentoOriginal.idNotaFiscalConhecimento ")
        .append("           AND NFC.conhecimento.id = :idDoctoServico ")
        .append("    )");

        return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parametersValues);
    }


    public List<NotaFiscalConhecimento> findByIdOcorrenciaNaoConformidade(Long idOcorrenciaNaoConformidade) {
        StringBuilder hql = new StringBuilder();
        hql.append(" SELECT nfc FROM ");
        hql.append(" NotaFiscalConhecimento nfc, ");
        hql.append(" OcorrenciaNaoConformidade onc, ");
        hql.append(" NotaOcorrenciaNc noc ");
        hql.append(" WHERE ");
        hql.append(" nfc.id = noc.notaFiscalConhecimento.id AND ");
        hql.append(" noc.ocorrenciaNaoConformidade.id = onc.id AND ");
        hql.append(" onc.id = :idOcorrenciaNaoConformidade ");
         
        Map<String, Object> parametersValues = new HashMap<String, Object>();
        parametersValues.put("idOcorrenciaNaoConformidade", idOcorrenciaNaoConformidade);
        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), parametersValues);
    }

    public List findByidDoctoServico(Long idDoctoServico) {
        StringBuilder hql = new StringBuilder()
            .append(" SELECT nfc.nrNotaFiscal")
            .append("   FROM NotaFiscalConhecimento nfc")
            .append("  WHERE nfc.conhecimento.id = :idDoctoServico");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("idDoctoServico", idDoctoServico);

        return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
    }
    
    public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoNotaCreditoPadrao(Long idConhecimento, Long idControleCarga) {

        Map<String, Object> parametersValues = new HashMap<String, Object>();

        StringBuilder query = new StringBuilder();
        query.append(" select nfc");
        query.append(" from NotaFiscalConhecimento as nfc, ");
        query.append("		EntregaNotaFiscal as enf, ");
        query.append("		OcorrenciaEntrega as oe, ");
        query.append("      Manifesto as man "); 
        query.append(" where nfc.conhecimento.id = :idConhecimento");
        query.append("	 and enf.notaFiscalConhecimento.id = nfc.id");
        query.append("	 and enf.ocorrenciaEntrega.id = oe.id");
        query.append("   and enf.manifesto.id = man.id ");
        query.append("   and man.controleCarga.id = :idControleCarga "); 
        query.append("	 and oe.tpOcorrencia in ('E','A')");
        
        parametersValues.put("idConhecimento", idConhecimento);
        parametersValues.put("idControleCarga", idControleCarga);

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
    public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoSemOcorrenciaEntrega(Long idConhecimento, Long idControleCarga) {
        Map<String, Object> parametersValues = new HashMap<String, Object>();

        StringBuilder query = new StringBuilder();
        query.append(" select nfc");
        query.append(" from NotaFiscalConhecimento as nfc ");
        query.append(" where nfc.conhecimento.id = :idConhecimento");
        
        query.append(" and not exists ");
        query.append("      (select 1 from EntregaNotaFiscal as enf, Manifesto as ma ");
        query.append("      where enf.notaFiscalConhecimento.id = nfc.id ");
        query.append("      and enf.manifesto.id = ma.id ");
        query.append("      and ma.controleCarga.id = :idControleCarga) ");
        
        query.append(" and not exists ");
        query.append("      (select 1 from NotaFiscalOperada as nfo ");
        query.append("      where nfo.notaFiscalConhecimentoOriginal.id = nfc.id ");
        query.append("      and nfo.tpSituacao in ('EN','DV','RF')) ");
        
        parametersValues.put("idConhecimento", idConhecimento);
        parametersValues.put("idControleCarga", idControleCarga);

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
    public List<NotaFiscalConhecimento> findNotasFiscaisConhecimentoSemNotaFiscalOperada(Long idConhecimento) {
        Map<String, Object> parametersValues = new HashMap<String, Object>();

        StringBuilder query = new StringBuilder();
        query.append(" select nfc");
        query.append(" from NotaFiscalConhecimento as nfc ");
        query.append(" where nfc.conhecimento.id = :idConhecimento ");
        query.append(" and nfc.id not in");
        query.append("      (select nfo.notaFiscalConhecimentoOriginal.id from NotaFiscalOperada as nfo ");
        query.append("      where nfo.notaFiscalConhecimentoOriginal.id = nfc.id ");
        query.append("      and nfo.tpSituacao in ('EN','DV','RF')) ");
        
        parametersValues.put("idConhecimento", idConhecimento);

        return getAdsmHibernateTemplate().findByNamedParam(query.toString(), parametersValues);
    }
    
}
