package com.mercurio.lms.contasreceber.model.dao;

import br.com.tntbrasil.integracao.domains.dell.FaturaDellDMN;
import br.com.tntbrasil.integracao.domains.dell.layout.d2l.*;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.*;
import com.mercurio.lms.contasreceber.model.param.FaturaLookupParam;
import com.mercurio.lms.contasreceber.model.param.RelacaoFaturaDepositoParam;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;
import org.apache.commons.lang.StringUtils;
import org.hibernate.*;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.YearMonthDay;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * DAO pattern.
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean
 */
public class FaturaDAO extends BaseCrudDao<Fatura, Long> {

	private static final String KEY_FATURA_INICIAL = "faturaInicial";
	private static final String KEY_FATURA_FINAL = "faturaFinal";
	private static final String KEY_FILIAL_ID_FILIAL = "filial.idFilial";
	private static final String KEY_ABRANGENCIA = "abrangencia";
	private static final String KEY_MODAL = "modal";
	private static final String SQL_ROWNUM = "ROWNUM";
	private static final String SQL_NOTA_FISCAL = "NOTA_FISCAL";
	private static final String SQL_DOCUMENTOSERVICO = "DOCUMENTOSERVICO";
	private static final String SQL_VALOR = "VALOR";
	private static final String SQL_ICMS = "ICMS";
	private static final String SQL_DESCONTO = "DESCONTO";
	private static final String SQL_MERCADORIA = "MERCADORIA";
	private static final String SQL_PESO = "PESO";
	private static final String SQL_ID_DOCTO_SERVICO = "ID_DOCTO_SERVICO";
	private static final String SQL_ID_DEVEDOR_DOC_SERV_FAT = "ID_DEVEDOR_DOC_SERV_FAT";
	private static final String SQL_NR_DOCUMENTO_ELETRONICO = "NR_DOCUMENTO_ELETRONICO";
	private static final String SQL_ID_FATURA = "ID_FATURA";
	private static final String SQL_EMPRESA = "EMPRESA";
	private static final String SQL_OB_FATURA = "OB_FATURA";
	private static final String SQL_COTACAO_DOLAR = "COTACAO_DOLAR";
	private static final String SQL_SG_FILIAL_NR_FATURA = "SG_FILIAL_NR_FATURA";
	private static final String SQL_ENDERECOFILIAL = "ENDERECOFILIAL";
	private static final String SQL_EMISSAO = "EMISSAO";
	private static final String SQL_NM_FILIAL = "NM_FILIAL";
	private static final String SQL_FORMA_AGRUPAMENTO = "FORMA_AGRUPAMENTO";
	private static final String SQL_VENCIMENTO = "VENCIMENTO";
	private static final String SQL_CEP = "CEP";
	private static final String SQL_TIPO_AGRUPAMENTO = "TIPO_AGRUPAMENTO";
	private static final String SQL_MOEDA = "MOEDA";
	private static final String SQL_MODAL = "MODAL";
	private static final String SQL_DS_DIVISAO_CLIENTE = "DS_DIVISAO_CLIENTE";
	private static final String SQL_ABRANGENCIA = "ABRANGENCIA";
	private static final String SQL_EMPRESACLIENTE = "EMPRESACLIENTE";
	private static final String SQL_TP_IDENTIFICACAO = "TP_IDENTIFICACAO";
	private static final String SQL_NR_IDENTIFICACAO = "NR_IDENTIFICACAO";
	private static final String SQL_TELEFONECLIENTE = "TELEFONECLIENTE";
	private static final String SQL_INSCRICAO = "INSCRICAO";
	private static final String SQL_ENDERECOCLIENTE = "ENDERECOCLIENTE";
	private static final String SQL_BAIRROCLIENTE = "BAIRROCLIENTE";
	private static final String SQL_CIDADECLIENTE = "CIDADECLIENTE";
	private static final String SQL_UFCLIENTE = "UFCLIENTE";
	private static final String SQL_TP_DOCUMENTO = "TP_DOCUMENTO";
	private static final String SQL_CEPCLIENTE = "CEPCLIENTE";
	private static final String SQL_BL_VALOR_LIQUIDO = "BL_VALOR_LIQUIDO";
	private static final String SQL_TPF_IDENTIFICACAO = "TPF_IDENTIFICACAO";
	private static final String SQL_CNPJ_FILIAL = "CNPJ_FILIAL";
	private static final String SQL_CJURODIARIO = "CJURODIARIO";
	private static final String SQL_FILJURODIARIO = "FILJURODIARIO";
	private static final String SQL_ID_FILIAL = "ID_FILIAL";
	private static final String SQL_TP_SITUACAO_FATURA = "TP_SITUACAO_FATURA";
	private static final String SQL_ID_MOEDA_ORIGEM = "ID_MOEDA_ORIGEM";
	private static final String SQL_ID_PAIS_ORIGEM = "ID_PAIS_ORIGEM";
	private static final int UPDATE_SIZE = 1000;
	private static final String KEY_NR_FATURA = "nrFatura";
	private static final String KEY_ID_FILIAL = "idFilial";
	private static final String KEY_DT_VENCIMENTO = "dtVencimento";
	private static final String KEY_CLIENTE_ID_CLIENTE = "cliente.idCliente";
	private static final String KEY_TP_ABRANGENCIA = "tpAbrangencia";
	private static final String ALIAS_FAT = "fat";
	private static final String ALIAS_FIL = "fil";
	private static final String ALIAS_PES = "pes";
	private static final String HQL_FETCH_FAT_FILIAL_BY_ID_FILIAL = "fetch fat.filialByIdFilial";
	private static final String HQL_FETCH_FIL_PESSOA = "fetch fil.pessoa";
	private static final String HQL_FAT_NR_FATURA = "fat.nrFatura";
	private static final String HQL_FIL_ID = "fil.id";
	private static final String HQL_FAT_DT_VENCIMENTO = "fat.dtVencimento";
	private static final String HQL_FAT_CLIENTE_ID = "fat.cliente.id";
	private static final String HQL_FAT_TP_ABRANGENCIA = "fat.tpAbrangencia";
	private static final String HQL_UF_ID_PAIS = "UF.ID_PAIS";
	private static final String HQL_F_ID_MOEDA = "F.ID_MOEDA";
	private static final String HQL_F_TP_SITUACAO_FATURA = "F.TP_SITUACAO_FATURA";
	private static final String HQL_F_ID_FILIAL = "F.ID_FILIAL";
	private static final String HQL_F_ID_FATURA = "F.ID_FATURA";
	private static final String HQL_FIL_PC_JURO_DIARIO = "FIL.PC_JURO_DIARIO";
	private static final String HQL_C_PC_JURO_DIARIO = "C.PC_JURO_DIARIO";
	private static final String HQL_PF_NR_IDENTIFICACAO = "PF.NR_IDENTIFICACAO";
	private static final String HQL_PF_TP_IDENTIFICACAO = "PF.TP_IDENTIFICACAO";
	private static final String HQL_UFCLI_SG_UNIDADE_FEDERATIVA = "UFCLI.SG_UNIDADE_FEDERATIVA";
	private static final String HQL_MUNCLI_NM_MUNICIPIO = "MUNCLI.NM_MUNICIPIO";
	private static final String HQL_P_NR_IDENTIFICACAO = "P.NR_IDENTIFICACAO";
	private static final String HQL_P_TP_IDENTIFICACAO = "P.TP_IDENTIFICACAO";
	private static final String HQL_P_NM_PESSOA = "P.NM_PESSOA";
	private static final String HQL_DC_DS_DIVISAO_CLIENTE = "DC.DS_DIVISAO_CLIENTE";
	private static final String HQL_DOC_ID_DOCTO_SERVICO = "DOC.ID_DOCTO_SERVICO";
	private static final String HQL_DEV_ID_DEVEDOR_DOC_SERV_FAT = "DEV.ID_DEVEDOR_DOC_SERV_FAT";
	private static final String HQL_DEVEDOR_DOC_SERV_FAT = "DEVEDOR_DOC_SERV_FAT";
	private static final String HQL_DOCTO_SERVICO = "DOCTO_SERVICO";
	private static final String HQL_FILIAL = "FILIAL";
	private static final String HQL_MONITORAMENTO_DOC_ELETRONICO = "MONITORAMENTO_DOC_ELETRONICO";
	private static final String HQL_DOC_ID_FILIAL_ORIGEM = "DOC.ID_FILIAL_ORIGEM";
	private static final String HQL_DEV_ID_FATURA = "DEV.ID_FATURA";
	private static final String HQL_DEV_ID_FILIAL = "DEV.ID_FILIAL";
	private static final String HQL_FILORIGEM_SG_FILIAL = "FILORIGEM.SG_FILIAL";
	private static final String HQL_DOC_NR_DOCTO_SERVICO = "DOC.NR_DOCTO_SERVICO";
	private static final String HQL_FAT_FILIAL_BY_ID_FILIAL = "fat.filialByIdFilial";
	private static final String HQL_FIL_PESSOA = "fil.pessoa";
	private static MessageSource messageSource;

	private static final String WHERE_ID = "WHERE id = ";
	private static final String UPDATE = "UPDATE ";
	private static final String CANCELFATURASBYIDREDECO = UPDATE + Fatura.class.getName() + " SET tpSituacaoFatura = :tpSituacaoFatura, redeco = null WHERE id IN (:idsFatura)";
	private static final String REEMITIR = "reemitir";
	private static final String SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS = "BL_ENVIA_DOCS_FATURAMENTO_NAS";
	private static final String SQL_TP_DOCUMENTO_SERVICO = "TP_DOCUMENTO_SERVICO";

	private JdbcTemplate jdbcTemplate;

	private static final ConfigureSqlQuery csqFaturasCobrancaTerceira = new ConfigureSqlQuery() {
		@Override
		public void configQuery(SQLQuery sqlQuery) {
			sqlQuery.addScalar("id_fatura", Hibernate.LONG);
			sqlQuery.addScalar("id_cliente", Hibernate.LONG);
			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
			sqlQuery.addScalar("sg_filial_fat", Hibernate.STRING);
			sqlQuery.addScalar("id_filial_fat", Hibernate.LONG);
			sqlQuery.addScalar("nr_fatura", Hibernate.STRING);
			sqlQuery.addScalar("tp_situacao_fatura", Hibernate.STRING);
			sqlQuery.addScalar("dt_emissao", Hibernate.STRING);
			sqlQuery.addScalar("dt_vencimento", Hibernate.STRING);
		}
	};

	private static final ConfigureSqlQuery csqFaturasExclusaoSerasa = new ConfigureSqlQuery() {
		@Override
		public void configQuery(SQLQuery sqlQuery) {
			sqlQuery.addScalar("id_fatura", Hibernate.LONG);
			sqlQuery.addScalar("id_cliente", Hibernate.LONG);
			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
			sqlQuery.addScalar("sg_filial_fat", Hibernate.STRING);
			sqlQuery.addScalar("id_filial_fat", Hibernate.LONG);
			sqlQuery.addScalar("nr_fatura", Hibernate.STRING);
			sqlQuery.addScalar("tp_situacao_fatura", Hibernate.STRING);
			sqlQuery.addScalar("dt_emissao", Hibernate.STRING);
			sqlQuery.addScalar("dt_vencimento", Hibernate.STRING);
			sqlQuery.addScalar("dh_negativacao_serasa", Hibernate.STRING);
			sqlQuery.addScalar("dh_exclusao_serasa", Hibernate.STRING);
			sqlQuery.addScalar("dt_liquidacao", Hibernate.STRING);
			sqlQuery.addScalar("qt_rec_pos_liq", Hibernate.INTEGER);
		}
	};

	private static final ConfigureSqlQuery csqFaturasNegativacaoSerasa = new ConfigureSqlQuery() {
		@Override
		public void configQuery(SQLQuery sqlQuery) {
			sqlQuery.addScalar("id_fatura", Hibernate.LONG);
			sqlQuery.addScalar("id_cliente", Hibernate.LONG);
			sqlQuery.addScalar("nr_identificacao", Hibernate.STRING);
			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
			sqlQuery.addScalar("sg_filial_fat", Hibernate.STRING);
			sqlQuery.addScalar("id_filial_fat", Hibernate.LONG);
			sqlQuery.addScalar("nr_fatura", Hibernate.STRING);
			sqlQuery.addScalar("tp_situacao_fatura", Hibernate.STRING);
			sqlQuery.addScalar("dt_emissao", Hibernate.STRING);
			sqlQuery.addScalar("dt_vencimento", Hibernate.STRING);
			sqlQuery.addScalar("dh_negativacao_serasa", Hibernate.STRING);
			sqlQuery.addScalar("dh_exclusao_serasa", Hibernate.STRING);
		}
	};

	private static final ConfigureSqlQuery csqFaturasNaoEnviadasEmail = new ConfigureSqlQuery() {
		@Override
		public void configQuery(SQLQuery sqlQuery) {
			sqlQuery.addScalar("sg_regional", Hibernate.STRING);
			sqlQuery.addScalar("id_regional", Hibernate.STRING);
			sqlQuery.addScalar("ds_email_faturamento", Hibernate.STRING);
			sqlQuery.addScalar("sg_filial_cob", Hibernate.STRING);
			sqlQuery.addScalar("id_filial_cob", Hibernate.LONG);
			sqlQuery.addScalar("nm_pessoa", Hibernate.STRING);
			sqlQuery.addScalar("id_cliente", Hibernate.LONG);
			sqlQuery.addScalar("sg_filial_fat", Hibernate.STRING);
			sqlQuery.addScalar("id_filial_fat", Hibernate.LONG);
			sqlQuery.addScalar("nr_fatura", Hibernate.STRING);
			sqlQuery.addScalar("ds_email", Hibernate.STRING);
			sqlQuery.addScalar("id_fatura", Hibernate.LONG);
		}
	};

	public Fatura store(Fatura fatura, ItemList itemFatura) {
		super.store(fatura);
		removeItemFatura(itemFatura.getRemovedItems());
		storeItemFatura(itemFatura.getNewOrModifiedItems());
		getAdsmHibernateTemplate().flush();
		return fatura;
	}

	@Override
	public void flush() {
		getAdsmHibernateTemplate().flush();
	}

	/**
	 * Salva a fatura usando o hibernate puro para optimizar o tempo de inserï¿½ï¿½o
	 *
	 * @param fatura
	 * @return Fatura
	 * @author Mickaël Jalbert
	 * @since 26/07/2006
	 */
	public Fatura storeBasic(Fatura fatura) {
		getAdsmHibernateTemplate().saveOrUpdate(fatura);

		return fatura;
	}

	/**
	 * Salva os itens fatura usando o hibernate puro para optimizar o tempo de inserï¿½ï¿½o
	 *
	 * @param lstItemFatura
	 * @author Mickaël Jalbert
	 * @since 26/07/2006
	 */
	public void storeItemFaturaBasic(final List lstItemFatura) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws SQLException {
				for (int i = 0; i < lstItemFatura.size(); i++) {
					ItemFatura itemFatura = (ItemFatura) lstItemFatura.get(i);

					session.save(itemFatura);

					if (i % 20 == 0) { //20, same as the JDBC batch size
						//flush a batch of inserts and release memory:
						session.flush();
						session.clear();
					}
				}
				return null;
			}
		});
	}


	public void storeItemFatura(List newOrModifiedItems) {
		getAdsmHibernateTemplate().saveOrUpdateAll(newOrModifiedItems);
		getAdsmHibernateTemplate().flush();
	}

	public void removeItemFatura(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
		getAdsmHibernateTemplate().flush();
	}


	public Long findNextSeqMonitoramentoMensagem() {
		return Long.valueOf(getSession().createSQLQuery("select MONITORAMENTO_MENSAGEM_SQ.nextval from dual").uniqueResult().toString());
	}

	public List findFaturasCobrancaTerceira() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f.id_fatura,");
		sql.append("       f.id_cliente,");
		sql.append("       pe.nr_identificacao,");
		sql.append("       pe.nm_pessoa,");
		sql.append("       ffat.sg_filial AS sg_filial_fat,");
		sql.append("       ffat.id_filial AS id_filial_fat,");
		sql.append("       f.nr_fatura,");
		sql.append("       f.tp_situacao_fatura,");
		sql.append("       f.dt_emissao,");
		sql.append("       f.dt_vencimento");
		sql.append("  FROM fatura                     f,");
		sql.append("       pessoa                     pe,");
		sql.append("       filial                     ffat,");
		sql.append("       excecao_cliente_financeiro ecf");
		sql.append(" WHERE f.id_cliente = pe.id_pessoa(+)");
		sql.append("   AND f.id_filial = ffat.id_filial(+)");
		sql.append("   AND f.id_cliente = ecf.id_cliente(+)");
		sql.append("   AND (ecf.tp_envio_cobranca_terceira = 'A' OR");
		sql.append("       ecf.tp_envio_cobranca_terceira IS NULL)");
		sql.append("   AND f.tp_situacao_fatura = 'BL'");
		sql.append("   AND f.dt_emissao >=");
		sql.append("       nvl((SELECT to_date(pg.ds_conteudo, 'dd/mm/yyyy')");
		sql.append("             FROM parametro_geral pg");
		sql.append("            WHERE pg.nm_parametro_geral = 'DT_INICIO_ENVIO_COB_TERC'");
		sql.append("              AND rownum = 1),");
		sql.append("           trunc(SYSDATE-10000))");
		sql.append("   AND f.dt_vencimento < trunc(SYSDATE - nvl((SELECT to_number(pg.ds_conteudo)");
		sql.append("                                               FROM parametro_geral pg");
		sql.append("                                              WHERE pg.nm_parametro_geral =");
		sql.append("                                                    'NR_DIAS_ENVIO_COB_TERC'");
		sql.append("                                                AND rownum = 1),");
		sql.append("                                             90))");
		sql.append("   AND (f.tp_situacao_aprovacao = 'A' OR f.tp_situacao_aprovacao IS NULL)");
		sql.append("   AND (nvl((SELECT pg.ds_conteudo");
		sql.append("              FROM parametro_geral pg");
		sql.append("             WHERE pg.nm_parametro_geral = 'BL_FINANCEIRO_CORPORATIVO'");
		sql.append("               AND rownum = 1),");
		sql.append("            'S') = 'N' OR NOT EXISTS");
		sql.append("        (SELECT 1");
		sql.append("           FROM romaneios    r,");
		sql.append("                filial_sigla fs");
		sql.append("          WHERE r.unid_sigla = fs.ds_siglaanterior");
		sql.append("            AND fs.id_filial_lms = f.id_filial");
		sql.append("            AND r.numero = f.nr_fatura");
		sql.append("            AND r.status = 4");
		sql.append("            AND rownum = 1))");
		sql.append("   AND (nvl((SELECT pg.ds_conteudo");
		sql.append("              FROM parametro_geral pg");
		sql.append("             WHERE pg.nm_parametro_geral = 'BL_COB_TERC_FRANQUIA'");
		sql.append("               AND rownum = 1),");
		sql.append("            'N') = 'S' OR");
		sql.append("       nvl((SELECT hf.tp_filial");
		sql.append("              FROM historico_filial hf");
		sql.append("             WHERE hf.id_filial = f.id_filial");
		sql.append("               AND trunc(SYSDATE) BETWEEN hf.dt_real_operacao_inicial AND");
		sql.append("                   hf.dt_real_operacao_final),");
		sql.append("            'FI') <> 'FR')");
		sql.append("   AND NOT EXISTS");
		sql.append(" (SELECT 1");
		sql.append("          FROM excecao_negativacao_serasa ens");
		sql.append("         WHERE ens.id_fatura = f.id_fatura");
		sql.append("           AND trunc(SYSDATE) BETWEEN ens.dt_vigencia_inicial AND");
		sql.append("               nvl(ens.dt_vigencia_final,");
		sql.append("                   to_date('01/01/4000', 'DD/MM/YYYY'))");
		sql.append("           AND rownum = 1)");
		//LMSA-1407
		sql.append("	AND NOT EXISTS (SELECT '1' FROM ");
		sql.append("	                HISTORICO_BOLETO HB WHERE ");
		sql.append("	                HB.ID_BOLETO = F.ID_BOLETO ");
		sql.append("	                AND HB.TP_SITUACAO_APROVACAO IN ('E')) ");
		sql.append("   AND (f.dh_envio_cob_terceira IS NULL)");
		sql.append(" ORDER BY pe.nr_identificacao,");
		sql.append("          pe.nm_pessoa,");
		sql.append("          ffat.sg_filial,");
		sql.append("          f.nr_fatura");
		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csqFaturasCobrancaTerceira);
	}

	public List findFaturasExclusaoSerasa() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f.id_fatura,");
		sql.append("       f.id_cliente,");
		sql.append("       pe.nr_identificacao,");
		sql.append("       pe.nm_pessoa,");
		sql.append("       ffat.sg_filial AS sg_filial_fat,");
		sql.append("       ffat.id_filial AS id_filial_fat,");
		sql.append("       f.nr_fatura,");
		sql.append("       f.tp_situacao_fatura,");
		sql.append("       f.dt_emissao,");
		sql.append("       f.dt_vencimento,");
		sql.append("       f.dh_negativacao_serasa,");
		sql.append("       f.dh_exclusao_serasa,");
		sql.append("       f.dt_liquidacao,");
		sql.append("       (SELECT COUNT(*)");
		sql.append("          FROM recebimento_pos_liq_fatura rplf");
		sql.append("         WHERE rplf.id_fatura = f.id_fatura");
		sql.append("           AND rownum = 1) qt_rec_pos_liq");
		sql.append("  FROM fatura                     f,");
		sql.append("       pessoa                     pe,");
		sql.append("       filial                     ffat,");
		sql.append("       excecao_cliente_financeiro ecf");
		sql.append(" WHERE f.id_cliente = pe.id_pessoa(+)");
		sql.append("   AND f.id_filial = ffat.id_filial(+)");
		sql.append("   AND f.id_cliente = ecf.id_cliente(+)");
		sql.append("   AND ((f.tp_situacao_fatura = 'LI' AND NOT EXISTS");
		sql.append("        (SELECT 1");
		sql.append("            FROM recebimento_pos_liq_fatura rplf");
		sql.append("           WHERE rplf.id_fatura = f.id_fatura");
		sql.append("             AND rownum = 1) AND NOT EXISTS");
		sql.append("        (SELECT 1");
		sql.append("            FROM redeco      re,");
		sql.append("                 item_redeco ir");
		sql.append("           WHERE re.id_redeco = ir.id_redeco");
		sql.append("             AND ir.id_fatura = f.id_fatura");
		sql.append("             AND re.tp_finalidade IN ('CJ', 'LP','DF')");

		sql.append("             AND re.tp_situacao_redeco = 'LI') AND NOT EXISTS ");
		sql.append(" 	(SELECT 1  ");
		sql.append("		FROM redeco                      re,");
		sql.append(" 			item_redeco                 ir,");
		sql.append(" 			composicao_pagamento_redeco cpr");
		sql.append(" 		WHERE re.id_redeco = ir.id_redeco");
		sql.append(" 			AND re.id_redeco = cpr.id_redeco");
		sql.append(" 			AND ir.id_fatura = f.id_fatura");
		sql.append(" 			AND cpr.tp_composicao_pagamento_redeco IN ('J', 'L', 'R')");
		sql.append(" 			AND re.tp_situacao_redeco = 'LI')");
		sql.append(" 	) OR ");

		sql.append("       (f.tp_situacao_fatura = 'LI' AND EXISTS");
		sql.append("        (SELECT 1");
		sql.append("            FROM recebimento_pos_liq_fatura rplf");
		sql.append("           WHERE rplf.id_fatura = f.id_fatura");
		sql.append("             AND rownum = 1)) OR (f.tp_situacao_fatura = 'CA'))");
		sql.append("   AND ((f.dh_negativacao_serasa IS NOT NULL AND");
		sql.append("       f.dh_exclusao_serasa IS NULL) OR");
		sql.append("       (f.dh_negativacao_serasa IS NOT NULL AND");
		sql.append("       f.dh_exclusao_serasa IS NOT NULL AND");
		sql.append("       f.dh_negativacao_serasa > f.dh_exclusao_serasa))");
		sql.append(" ORDER BY pe.nr_identificacao,");
		sql.append("          pe.nm_pessoa,");
		sql.append("          ffat.sg_filial,");
		sql.append("          f.nr_fatura");
		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csqFaturasExclusaoSerasa);
	}

	public List findFaturasNegativacaoSerasa() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT f.id_fatura,");
		sql.append("       f.id_cliente,");
		sql.append("       pe.nr_identificacao,");
		sql.append("       pe.nm_pessoa,");
		sql.append("       ffat.sg_filial AS sg_filial_fat,");
		sql.append("       ffat.id_filial AS id_filial_fat,");
		sql.append("       f.nr_fatura,");
		sql.append("       f.tp_situacao_fatura,");
		sql.append("       f.dt_emissao,");
		sql.append("       f.dt_vencimento,");
		sql.append("       f.dh_negativacao_serasa,");
		sql.append("       f.dh_exclusao_serasa");
		sql.append("  FROM fatura                     f,");
		sql.append("       pessoa                     pe,");
		sql.append("       filial                     ffat,");
		sql.append("       excecao_cliente_financeiro ecf");
		sql.append(" WHERE f.id_cliente = pe.id_pessoa(+)");
		sql.append("   AND f.id_filial = ffat.id_filial(+)");
		sql.append("   AND f.id_cliente = ecf.id_cliente(+)");
		sql.append("   AND (ecf.tp_envio_serasa = 'A' OR ecf.tp_envio_serasa IS NULL)");
		sql.append("   AND f.tp_situacao_fatura = 'BL'");
		sql.append("   AND f.dt_emissao >=");
		sql.append("       nvl((SELECT to_date(pg.ds_conteudo, 'dd/mm/yyyy')");
		sql.append("             FROM parametro_geral pg");
		sql.append("            WHERE pg.nm_parametro_geral = 'DT_INICIO_ENVIO_SERASA'");
		sql.append("              AND rownum = 1),");
		sql.append("           trunc(SYSDATE))");
		sql.append("   AND f.dt_vencimento < trunc(SYSDATE - nvl((SELECT to_number(pg.ds_conteudo)");
		sql.append("                                               FROM parametro_geral pg");
		sql.append("                                              WHERE pg.nm_parametro_geral =");
		sql.append("                                                    'NR_DIAS_ENVIO_SERASA'");
		sql.append("                                                AND rownum = 1),");
		sql.append("                                             10))");
		sql.append("   AND (f.tp_situacao_aprovacao = 'A' OR f.tp_situacao_aprovacao IS NULL)");
		sql.append("   AND (nvl((SELECT pg.ds_conteudo");
		sql.append("              FROM parametro_geral pg");
		sql.append("             WHERE pg.nm_parametro_geral = 'BL_FINANCEIRO_CORPORATIVO'");
		sql.append("               AND rownum = 1),");
		sql.append("            'S') = 'N' OR NOT EXISTS");
		sql.append("        (SELECT 1");
		sql.append("           FROM romaneios    r,");
		sql.append("                filial_sigla fs");
		sql.append("          WHERE r.unid_sigla = fs.ds_siglaanterior");
		sql.append("            AND fs.id_filial_lms = f.id_filial");
		sql.append("            AND r.numero = f.nr_fatura");
		sql.append("            AND r.status = 4");
		sql.append("            AND rownum = 1))");
		sql.append("   AND (nvl((SELECT pg.ds_conteudo");
		sql.append("              FROM parametro_geral pg");
		sql.append("             WHERE pg.nm_parametro_geral = 'BL_SERASA_FRANQUIA'");
		sql.append("               AND rownum = 1),");
		sql.append("            'N') = 'S' OR");
		sql.append("       nvl((SELECT hf.tp_filial");
		sql.append("              FROM historico_filial hf");
		sql.append("             WHERE hf.id_filial = f.id_filial");
		sql.append("               AND trunc(SYSDATE) BETWEEN hf.dt_real_operacao_inicial AND");
		sql.append("                   hf.dt_real_operacao_final),");
		sql.append("            'FI') <> 'FR')");
		sql.append("   AND NOT EXISTS");
		sql.append(" (SELECT 1");
		sql.append("          FROM excecao_negativacao_serasa ens");
		sql.append("         WHERE ens.id_fatura = f.id_fatura");
		sql.append("           AND trunc(SYSDATE) BETWEEN ens.dt_vigencia_inicial AND");
		sql.append("               nvl(ens.dt_vigencia_final,");
		sql.append("                   to_date('01/01/4000', 'DD/MM/YYYY'))");
		sql.append("           AND rownum = 1)");
		//LMSA-1407
		sql.append(" 	AND NOT EXISTS (SELECT '1' FROM	");
		sql.append("	                   HISTORICO_BOLETO HB WHERE ");
		sql.append("	                   HB.ID_BOLETO = F.ID_BOLETO ");
		sql.append("	                   AND HB.TP_SITUACAO_APROVACAO IN ('E')) ");
		sql.append("   AND (f.dh_negativacao_serasa IS NULL OR");
		sql.append("       (f.dh_negativacao_serasa IS NOT NULL AND");
		sql.append("       f.dh_exclusao_serasa IS NOT NULL))");
		sql.append(" ORDER BY pe.nr_identificacao,");
		sql.append("          pe.nm_pessoa,");
		sql.append("          ffat.sg_filial,");
		sql.append("          f.nr_fatura");

		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csqFaturasNegativacaoSerasa);
	}

	public List findCobrancaFaturasNaoEnviadasEmail() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cast(decode(f.ds_email, NULL, regfa.sg_regional, reg.sg_regional) as varchar2(100)) sg_regional,");
		sql.append("       cast(decode(f.ds_email, NULL, regfa.id_regional, reg.id_regional) as number(10)) id_regional,");
		sql.append("       cast(decode(f.ds_email,");
		sql.append("              NULL,");
		sql.append("              regfa.ds_email_faturamento,");
		sql.append("              reg.ds_email_faturamento) as varchar2(100)) ds_email_faturamento,");
		sql.append("       fcob.sg_filial AS sg_filial_cob,");
		sql.append("       fcob.id_filial AS id_filial_cob,");
		sql.append("       pe.nm_pessoa,");
		sql.append("       f.id_cliente,");
		sql.append("       ffat.sg_filial AS sg_filial_fat,");
		sql.append("       ffat.id_filial AS id_filial_fat,");
		sql.append("       f.nr_fatura,");
		sql.append("       f.ds_email,");
		sql.append("       F.ID_FATURA");
		sql.append("  FROM (SELECT f.*,");
		sql.append("               to_char((SELECT wm_concat(co.ds_email)");
		sql.append("                         FROM contato co");
		sql.append("                        WHERE co.tp_contato = 'CB'");
		sql.append("                          AND co.ds_email IS NOT NULL");
		sql.append("                          AND co.id_pessoa = f.id_cliente)) ds_email");
		sql.append("          FROM fatura f) f,");
		sql.append("       pessoa pe,");
		sql.append("       filial ffat,");
		sql.append("       cliente cl,");
		sql.append("       filial fcob,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional reg");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND");
		sql.append("               reg.dt_vigencia_final) reg,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional_filial regf");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND");
		sql.append("               regf.dt_vigencia_final) regf,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional reg");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND");
		sql.append("               reg.dt_vigencia_final) regfa,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional_filial regf");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND");
		sql.append("               regf.dt_vigencia_final) regffa,");
		sql.append("       excecao_cliente_financeiro ecf");
		sql.append(" WHERE f.id_cliente = pe.id_pessoa(+)");
		sql.append("   AND f.id_filial = ffat.id_filial(+)");
		sql.append("   AND f.id_cliente = cl.id_cliente(+)");
		sql.append("   AND cl.id_filial_cobranca = fcob.id_filial(+)");
		sql.append("   AND cl.id_filial_cobranca = regf.id_filial(+)");
		sql.append("   AND regf.id_regional = reg.id_regional(+)");
		sql.append("   AND f.id_filial = regffa.id_filial(+)");
		sql.append("   AND regffa.id_regional = regfa.id_regional(+)");
		sql.append("   AND f.id_cliente = ecf.id_cliente(+)");
		sql.append("   AND (ecf.tp_envio_carta_cobranca = 'E' OR");
		sql.append("       ecf.tp_envio_carta_cobranca IS NULL)");
		sql.append("   AND f.tp_situacao_fatura = 'BL'");
		sql.append("   AND f.dt_emissao >=");
		sql.append("       nvl((SELECT to_date(pg.ds_conteudo, 'dd/mm/yyyy')");
		sql.append("             FROM parametro_geral pg");
		sql.append("            WHERE pg.nm_parametro_geral = 'DT_INICIO_ENVIO_COB_EMAIL_FAT'");
		sql.append("              AND rownum = 1),");
		sql.append("           trunc(SYSDATE))");
		sql.append("   AND f.dt_vencimento < trunc(SYSDATE - nvl((SELECT to_number(pg.ds_conteudo)");
		sql.append("                                               FROM parametro_geral pg");
		sql.append("                                              WHERE pg.nm_parametro_geral =");
		sql.append("                                                    'NR_DIAS_ENVIO_CARTA_COBRANCA'");
		sql.append("                                                AND rownum = 1),");
		sql.append("                                             5))");
		sql.append("   AND NOT EXISTS");
		sql.append(" (SELECT 1");
		sql.append("          FROM relacao_pagto_parcial rpp");
		sql.append("         WHERE rpp.id_fatura = f.id_fatura");
		sql.append("           AND rownum = 1)");
		sql.append("   AND (f.tp_situacao_aprovacao = 'A' OR f.tp_situacao_aprovacao IS NULL)");
		sql.append("   AND (nvl((SELECT pg.ds_conteudo");
		sql.append("              FROM parametro_geral pg");
		sql.append("             WHERE pg.nm_parametro_geral = 'BL_FINANCEIRO_CORPORATIVO'");
		sql.append("               AND rownum = 1),");
		sql.append("            'S') = 'N' OR NOT EXISTS");
		sql.append("        (SELECT 1");
		sql.append("           FROM romaneios    r,");
		sql.append("                filial_sigla fs");
		sql.append("          WHERE r.unid_sigla = fs.ds_siglaanterior");
		sql.append("            AND fs.id_filial_lms = f.id_filial");
		sql.append("            AND r.numero = f.nr_fatura");
		sql.append("            AND r.status = 4");
		sql.append("            AND rownum = 1))");
		sql.append("   AND (nvl((SELECT pg.ds_conteudo");
		sql.append("              FROM parametro_geral pg");
		sql.append("             WHERE pg.nm_parametro_geral = 'BL_CARTA_COBRANCA_FRANQUIA'");
		sql.append("               AND rownum = 1),");
		sql.append("            'N') = 'S' OR");
		sql.append("       nvl((SELECT hf.tp_filial");
		sql.append("              FROM historico_filial hf");
		sql.append("             WHERE hf.id_filial = f.id_filial");
		sql.append("               AND trunc(SYSDATE) BETWEEN hf.dt_real_operacao_inicial AND");
		sql.append("                   hf.dt_real_operacao_final),");
		sql.append("            'FI') <> 'FR')");
		sql.append("   AND NOT EXISTS");
		sql.append(" (SELECT 1");
		sql.append("          FROM monit_mens_fatura      mmf,");
		sql.append("               monitoramento_mensagem mm");
		sql.append("         WHERE mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem");
		sql.append("           AND mmf.id_fatura = f.id_fatura");
		sql.append("           AND mm.tp_modelo_mensagem = 'CO'");
		sql.append("           AND mm.tp_envio_mensagem = 'E'");
		sql.append("           AND rownum = 1)");
		//LMSA-1407
		sql.append("	AND NOT EXISTS (SELECT '1' FROM	");
		sql.append("	                   HISTORICO_BOLETO HB WHERE ");
		sql.append("	                   HB.ID_BOLETO = F.ID_BOLETO ");
		sql.append("	                   AND HB.TP_SITUACAO_APROVACAO IN ('E')) ");
		//LMS-8682
		sql.append("	AND NOT EXISTS (SELECT 1 ");
		sql.append("	  FROM excecao_negativacao_serasa ens ");
		sql.append("	  WHERE ens.id_fatura = f.id_fatura ");
		sql.append("	  AND trunc(SYSDATE) BETWEEN ens.dt_vigencia_inicial ");
		sql.append(" 	  AND nvl(ens.dt_vigencia_final, to_date('01/01/4000', 'DD/MM/YYYY')) ");
		sql.append("	  AND rownum = 1) ");
		sql.append(" ORDER BY sg_regional,");
		sql.append("          id_regional,");
		sql.append("          fcob.sg_filial,");
		sql.append("          fcob.id_filial,");
		sql.append("          pe.nm_pessoa,");
		sql.append("          f.id_cliente,");
		sql.append("          ffat.sg_filial,");
		sql.append("          ffat.id_filial,");
		sql.append("          f.id_cliente,");
		sql.append("          f.nr_fatura");
		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csqFaturasNaoEnviadasEmail);
	}

	public List findFaturasNaoEnviadasEmail() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT cast(decode(f.ds_email, NULL, regfa.sg_regional, reg.sg_regional) as varchar2(100)) sg_regional,");
		sql.append("       cast(decode(f.ds_email, NULL, regfa.id_regional, reg.id_regional) as number(10)) id_regional,");
		sql.append("       cast(decode(f.ds_email,");
		sql.append("              NULL,");
		sql.append("              regfa.ds_email_faturamento,");
		sql.append("              reg.ds_email_faturamento) as varchar2(100)) ds_email_faturamento,");
		sql.append("       fcob.sg_filial AS sg_filial_cob,");
		sql.append("       fcob.id_filial AS id_filial_cob,");
		sql.append("       pe.nm_pessoa,");
		sql.append("       f.id_cliente,");
		sql.append("       ffat.sg_filial AS sg_filial_fat,");
		sql.append("       ffat.id_filial AS id_filial_fat,");
		sql.append("       f.nr_fatura,");
		sql.append("       f.ds_email,");
		sql.append("       f.id_fatura");
		sql.append("  FROM (SELECT f.*,");
		sql.append("               to_char((SELECT wm_concat(co.ds_email)");
		sql.append("                         FROM contato co");
		sql.append("                        WHERE co.tp_contato = 'FA'");
		sql.append("                          AND co.ds_email IS NOT NULL");
		sql.append("                          AND co.id_pessoa = f.id_cliente)) ds_email");
		sql.append("          FROM fatura f) f,");
		sql.append("       pessoa pe,");
		sql.append("       filial ffat,");
		sql.append("       cliente cl,");
		sql.append("       filial fcob,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional reg");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND");
		sql.append("               reg.dt_vigencia_final) reg,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional_filial regf");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND");
		sql.append("               regf.dt_vigencia_final) regf,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional reg");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND");
		sql.append("               reg.dt_vigencia_final) regfa,");
		sql.append("       (SELECT *");
		sql.append("          FROM regional_filial regf");
		sql.append("         WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND");
		sql.append("               regf.dt_vigencia_final) regffa,");
		sql.append("       excecao_cliente_financeiro ecf");
		sql.append(" WHERE f.id_cliente = pe.id_pessoa(+)");
		sql.append("   AND f.id_filial = ffat.id_filial(+)");
		sql.append("   AND f.id_cliente = cl.id_cliente(+)");
		sql.append("   AND cl.id_filial_cobranca = fcob.id_filial(+)");
		sql.append("   AND cl.id_filial_cobranca = regf.id_filial(+)");
		sql.append("   AND regf.id_regional = reg.id_regional(+)");
		sql.append("   AND f.id_filial = regffa.id_filial(+)");
		sql.append("   AND regffa.id_regional = regfa.id_regional(+)");
		sql.append("   AND f.id_cliente = ecf.id_cliente(+)");
		sql.append("   AND (ecf.tp_envio_faturamento = 'E' OR ecf.tp_envio_faturamento IS NULL)");
		sql.append("   AND f.tp_situacao_fatura = 'BL'");
		sql.append("   AND f.tp_origem <> 'E'");
		sql.append("   AND f.dt_emissao >=");
		sql.append("       nvl((SELECT to_date(pg.ds_conteudo, 'dd/mm/yyyy')");
		sql.append("             FROM parametro_geral pg");
		sql.append("            WHERE pg.nm_parametro_geral = 'DT_INICIO_ENVIO_EMAIL_FAT'");
		sql.append("              AND rownum = 1),");
		sql.append("           trunc(SYSDATE))");
		sql.append("   AND NOT EXISTS");
		sql.append(" (SELECT 1");
		sql.append("          FROM monit_mens_fatura      mmf,");
		sql.append("               monitoramento_mensagem mm");
		sql.append("         WHERE mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem");
		sql.append("           AND mmf.id_fatura = f.id_fatura");
		sql.append("           AND mm.tp_envio_mensagem = 'E'");
		sql.append("           AND rownum = 1)");
		sql.append(" ORDER BY sg_regional,");
		sql.append("          id_regional,");
		sql.append("          fcob.sg_filial,");
		sql.append("          fcob.id_filial,");
		sql.append("          pe.nm_pessoa,");
		sql.append("          f.id_cliente,");
		sql.append("          ffat.sg_filial,");
		sql.append("          ffat.id_filial,");
		sql.append("          f.id_cliente,");
		sql.append("          f.nr_fatura");

		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csqFaturasNaoEnviadasEmail);
	}

	public List findItemFatura(Long masterId) {
		SqlTemplate hql = mountSqlItem(masterId);

		hql.addProjection("ifat");

		List lstDevedor = this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		for (Iterator iter = lstDevedor.iterator(); iter.hasNext(); ) {
			ItemFatura itemFatura = (ItemFatura) iter.next();

			getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getCliente());
			getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getCliente().getPessoa());
			getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem());
			getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getPessoa());
			getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getDoctoServico().getMoeda());
			getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getDoctoServico().getServico());

			if (itemFatura.getDevedorDocServFat().getDesconto() != null) {
				getHibernateTemplate().initialize(itemFatura.getDevedorDocServFat().getDesconto().getMotivoDesconto());
			}
		}

		return lstDevedor;
	}

	public Integer getRowCountItemFatura(Long masterId) {
		SqlTemplate hql = new SqlTemplate();
		hql.addInnerJoin(ItemFatura.class.getName(), "ifat");
		hql.addInnerJoin(" ifat.fatura", ALIAS_FAT);
		hql.addCriteria("fat.idFatura", "=", masterId);

		return this.getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Nome da classe que o DAO ï¿½ responsável por persistir.
	 */
	@Override
	protected final Class getPersistentClass() {
		return Fatura.class;
	}

	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filialByIdFilial", FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilial.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialCobradora", FetchMode.JOIN);
		lazyFindPaginated.put("cliente", FetchMode.JOIN);
		lazyFindPaginated.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("divisaoCliente", FetchMode.JOIN);
		lazyFindPaginated.put("moeda", FetchMode.JOIN);
	}

	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filialByIdFilial", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilial.pessoa", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialCobradora", FetchMode.JOIN);
		lazyFindById.put("filialByIdFilialCobradora.pessoa", FetchMode.JOIN);
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("divisaoCliente", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("cotacaoMoeda", FetchMode.JOIN);
		lazyFindById.put("pendencia", FetchMode.JOIN);
		lazyFindById.put("recibo", FetchMode.JOIN);
	}

	/**
	 * Retorna a fatura do id informado, disconectado.
	 *
	 * @param idFatura Long
	 * @return Fatura
	 */
	public Fatura findByIdDisconnected(Long idFatura) {
		Fatura fatura = (Fatura) this.findById(idFatura);
		this.getSession().evict(fatura);
		return fatura;
	}

	/**
	 * Retorna a fatura com o boleto ativo 'fetchado' se existe.
	 *
	 * @param idFatura Long
	 * @return Fatura
	 * @author Mickaël Jalbert
	 * @since 26/06/2006
	 */
	public Fatura findByIdWithBoleto(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(ALIAS_FAT);

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addLeftOuterJoin("fetch fat.pendencia", "pen");

		hql.addCriteria("fat.id", "=", idFatura);

		List lstFatura = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (lstFatura.size() == 1) {
			return (Fatura) lstFatura.get(0);
		} else {
			return null;
		}
	}

	public Fatura findByIdTela(Long idFatura) {
		TypedFlatMap criteria = new TypedFlatMap();

		criteria.put("idFatura", idFatura);

		SqlTemplate sql = mountSql(criteria);

		List lstFatura = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		if (lstFatura.size() == 1) {
			return (Fatura) lstFatura.get(0);
		} else {
			return null;
		}
	}

	private SqlTemplate mountSql(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(ALIAS_FAT);
		sql.addInnerJoin(Fatura.class.getName(), "fat " +
				"left outer join fetch fat.cotacaoMoeda as cotmoe " +
				"left outer join fetch cotmoe.moedaPais as moePai " +
				"left outer join fetch moePai.moeda as moe " +
				"left outer join fetch fat.usuario as usu " +
				"left outer join fetch fat.pendencia as pen " +
				"left outer join fetch fat.relacaoCobranca as relcob " +
				"left outer join fetch relcob.filial as filrelcob " +
				"left outer join fetch fat.redeco as redeco " +
				"left outer join fetch redeco.filial as filRedeco " +
				"left outer join fetch filRedeco.pessoa as pesFilRedeco " +
				"join fetch fat.cliente as cli " +
				"left outer join fetch cli.cedente as cedCli " +
				"left outer join fetch fat.divisaoCliente as divcli " +
				"join fetch fat.moeda as moe " +
				"left outer join fetch fat.tipoAgrupamento as tipo " +
				"left outer join fetch fat.agrupamentoCliente as agr " +
				"left outer join fetch fat.cedente as ced " +
				"join fetch fat.filialByIdFilial as fil " +
				"join fetch fat.filialByIdFilialCobradora as filcob " +
				"left outer join fetch fat.filialByIdFilialDebitada as fildeb " +
				"left outer join fetch fat.relacaoCobranca as relCob " +
				"join fetch fil.pessoa as pfil " +
				"join fetch filcob.pessoa as pfilcob " +
				"left outer join fetch fildeb.pessoa as pfildeb " +
				"left outer join fetch fat.servico as se " +
				"join fetch cli.pessoa as pescli " +
				"join fetch cli.filialByIdFilialCobranca as filcobcli " +
				"join fetch filcobcli.pessoa as pesfilcobcli ");

		sql.addCriteria("fat.idFatura", "=", criteria.getLong("idFatura"));
		return sql;
	}

	private SqlTemplate mountSqlItem(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();
		hql.addInnerJoin(ItemFatura.class.getName(), "ifat");
		hql.addInnerJoin(" ifat.fatura", ALIAS_FAT);
		hql.addInnerJoin(" fetch ifat.devedorDocServFat", "dev");
		hql.addInnerJoin(" fetch dev.doctoServico", "doc");
		hql.addLeftOuterJoin(" fetch dev.descontos", "des");
		hql.addCriteria("fat.idFatura", "=", idFatura);
		hql.addOrderBy("doc.tpDocumentoServico");
		hql.addOrderBy("doc.nrDoctoServico");
		return hql;
	}

	/**
	 * Busca faturas para cï¿½lculo de juros diï¿½rio.<BR>
	 *
	 * @param criterions
	 * @return
	 */
	public List findFaturaCalculoJuroDiario(Map criterions) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("f");

		sql.addFrom(getPersistentClass().getName() + " f " +
				"inner join fetch f.filialByIdFilial filial " +
				"inner join fetch f.cliente cli " +
				"left outer join f.itemRedecos ir " +
				"left outer join ir.redeco r " +
				"left outer join f.boleto b");

		YearMonthDay hoje = JTDateTimeUtils.getDataAtual();

		sql.addCustomCriteria("(filial.dtImplantacaoLMS is not null and filial.dtImplantacaoLMS <= ?)");
		sql.addCriteriaValue(hoje);

		YearMonthDay hojeMenosUmDia = hoje.minusDays(1);

		while (JTDateTimeUtils.getNroDiaSemana(hojeMenosUmDia) == DateTimeConstants.SUNDAY || JTDateTimeUtils.getNroDiaSemana(hojeMenosUmDia) == DateTimeConstants.SATURDAY) {
			hojeMenosUmDia = hojeMenosUmDia.minusDays(1);
		}

		sql.addCustomCriteria("f.dtVencimento < ?");
		sql.addCriteriaValue(hojeMenosUmDia);

		sql.addCustomCriteria("f.tpSituacaoFatura in ('BL', 'RE')");
		sql.addCustomCriteria("((r.tpFinalidade = 'CF' AND r.tpSituacaoRedeco <> 'CA') OR r.tpFinalidade is null )");

		sql.addCustomCriteria(" not exists(select 1 from " + OcorrenciaRetornoBanco.class.getName() + " oco where oco.boleto.id = b.id)");

		sql.addOrderBy("filial.id");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public List findFaturasVencidasEAVencerFull(TypedFlatMap tfm) {
		Map<String, Object> objects = new HashMap();
		StringBuilder sql = new StringBuilder();
		getFullQueryVencidadsVencer(sql);
		createFullFromWhereQueryVencidadasVencer(tfm, objects, sql);
		return getAdsmHibernateTemplateReadOnly().findBySqlToMappedResult(sql.toString(), objects, configureSqlQueryFaturasVencidasEAVencer());
	}

	public List findFaturasVencidasEAVencer(TypedFlatMap tfm) {
		Map<String, Object> objects = new HashMap();
		StringBuilder sql = new StringBuilder();
		getSmallQueryVencidadsVencer(sql);
		createSmallFromWhereQueryVencidadasVencer(tfm, objects, sql);
		return getAdsmHibernateTemplateReadOnly().findBySqlToMappedResult(sql.toString(), objects, configureSqlSmallQueryFaturasVencidasEAVencer());
	}

	private void createSmallFromWhereQueryVencidadasVencer(TypedFlatMap tfm, Map<String, Object> objects, StringBuilder sql) {
		sql.append("   FROM fatura f, ");
		sql.append("        filial fi, ");
		sql.append("        filial fideb, ");
		sql.append("        motivo_desconto mdes, ");
		sql.append("        (SELECT b.* FROM boleto b WHERE b.tp_situacao_boleto <> 'CA') b, ");
		sql.append("        cedente cedbol, ");
		sql.append("        pessoa pcli, ");
		sql.append("        divisao_cliente divcli, ");
		sql.append("        agrupamento_cliente ac, ");
		sql.append("        forma_agrupamento fa, ");
		sql.append("        tipo_agrupamento ta, ");
		sql.append("        cliente cli, ");
		sql.append("        filial ficobcli, ");
		sql.append("        excecao_cliente_financeiro ecf, ");
		sql.append("        cedente ced, ");
		sql.append("        servico s, ");
		sql.append("        (SELECT red.*, ");
		sql.append("                ired.id_fatura, ");
		sql.append("                ired.id_item_redeco ");
		sql.append("           FROM redeco      red, ");
		sql.append("                item_redeco ired ");
		sql.append("          WHERE red.id_redeco = ired.id_redeco ");
		sql.append("            AND red.tp_situacao_redeco <> 'CA') red, ");
		sql.append("        filial fred, ");
		sql.append("        (SELECT * ");
		sql.append("           FROM regional reg ");
		sql.append("          WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND ");
		sql.append("                reg.dt_vigencia_final) reg, ");
		sql.append("        (SELECT * ");
		sql.append("           FROM regional_filial regf ");
		sql.append("          WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND ");
		sql.append("                regf.dt_vigencia_final) regf ");
		sql.append("  WHERE f.id_filial = fi.id_filial(+) ");
		sql.append("    AND f.id_filial_debitada = fideb.id_filial(+) ");
		sql.append("    AND f.id_motivo_desconto = mdes.id_motivo_desconto(+) ");
		sql.append("    AND f.id_fatura = b.id_fatura(+) ");
		sql.append("    AND b.id_cedente = cedbol.id_cedente(+) ");
		sql.append("    AND f.id_cliente = pcli.id_pessoa(+) ");
		sql.append("    AND f.id_divisao_cliente = divcli.id_divisao_cliente(+) ");
		sql.append("    AND f.id_agrupamento_cliente = ac.id_agrupamento_cliente(+) ");
		sql.append("    AND ac.id_forma_agrupamento = fa.id_forma_agrupamento(+) ");
		sql.append("    AND f.id_tipo_agrupamento = ta.id_tipo_agrupamento(+) ");
		sql.append("    AND f.id_cliente = cli.id_cliente(+) ");
		sql.append("    AND cli.id_filial_cobranca = ficobcli.id_filial(+) ");
		sql.append("    AND f.id_cliente = ecf.id_cliente(+) ");
		sql.append("    AND f.id_cedente = ced.id_cedente(+) ");
		sql.append("    AND f.id_servico = s.id_servico(+) ");
		sql.append("    AND f.id_fatura = red.id_fatura(+) ");
		sql.append("    AND red.id_filial = fred.id_filial(+) ");
		sql.append("    AND f.id_filial = regf.id_filial(+) ");
		sql.append("    AND regf.id_regional = reg.id_regional(+) ");
		sql.append("    AND (red.ID_REDECO IS NULL OR ");
		sql.append("           red.ID_REDECO IN ");
		sql.append("           (SELECT MAX(RED2.ID_REDECO) ");
		sql.append("               FROM REDECO      RED2, ");
		sql.append("                    ITEM_REDECO IRED2 ");
		sql.append("              WHERE RED2.ID_REDECO = IRED2.ID_REDECO ");
		sql.append("                AND RED2.TP_SITUACAO_REDECO <> 'CA' ");
		sql.append("                AND IRED2.ID_FATURA = f.ID_FATURA)) ");

		addWhereClausesToSql(tfm, objects, sql);

		if ((tfm.get("dtLiquidacaoInicial") == null) && (tfm.get("dtLiquidacaoFinal") == null)) {
			sql.append("    AND f.TP_SITUACAO_FATURA NOT IN ('LI', 'CA') ");
		} else {
			sql.append("    AND f.TP_SITUACAO_FATURA NOT IN ('CA') ");
		}
	}

	private void createFullFromWhereQueryVencidadasVencer(TypedFlatMap tfm, Map<String, Object> objects, StringBuilder sql) {
		sql.append(" from (SELECT /*+ FULL (f) PARALLEL (f,4) */																");
		sql.append("                f.id_fatura AS IDfatura,                                                                    ");
		sql.append("                reg.sg_regional AS Regional,                                                                ");
		sql.append("                reg.ds_regional AS Descricoreg,                                                             ");
		sql.append("                (SELECT fi.sg_filial                                                                        ");
		sql.append("                   FROM filial fi                                                                           ");
		sql.append("                  WHERE fi.id_filial = f.id_filial) AS Filial,                                              ");
		sql.append("                f.nr_fatura AS Numero,                                                                      ");
		sql.append("                pcli.nr_identificacao AS CNPJ,                                                              ");
		sql.append("                pcli.nm_pessoa AS Razaosocial,                                                              ");
		sql.append("                (SELECT ficobcli.sg_filial                                                                  ");
		sql.append("                   FROM filial ficobcli                                                                     ");
		sql.append("                  WHERE ficobcli.id_filial = cli.id_filial_cobranca) AS Filialcobcliente,                   ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_PESSOA')                                              ");
		sql.append("                    AND vl_valor_dominio = pcli.tp_pessoa) AS Tipopessoa,                                   ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_CLIENTE')                                             ");
		sql.append("                    AND vl_valor_dominio = cli.tp_cliente) AS Tipocliente,                                  ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_COBRANCA')                                            ");
		sql.append("                    AND vl_valor_dominio = cli.tp_cobranca) AS Tipocobranca,                                ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_SIM_NAO')                                                  ");
		sql.append("                    AND vl_valor_dominio = cli.bl_pre_fatura) AS Clientecomprefatura,                       ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_SIM_NAO')                                                  ");
		sql.append("                    AND vl_valor_dominio = cli.bl_cobranca_centralizada) AS Cobrancacentralizada,           ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TP_CLIENTE_FINANCEIRO')                                    ");
		sql.append("                    AND vl_valor_dominio = Nvl(ecf.tp_cliente, 'G')) AS Classificacaocliente,               ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TP_ENVIO_FATURAMENTO')                                     ");
		sql.append("                    AND vl_valor_dominio = Nvl(ECF.tp_envio_faturamento, 'E')) AS Tipodeenviofaturamento,   ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TP_ENVIO_CARTA_COBRANCA')                                  ");
		sql.append("                    AND vl_valor_dominio =                                                                  ");
		sql.append("                        Nvl(ECF.tp_envio_carta_cobranca, 'E')) AS Tipodeenviocartacobranca,                 ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TP_ENVIO_SERASA')                                          ");
		sql.append("                    AND vl_valor_dominio = Nvl(ECF.tp_envio_serasa, 'A')) AS TipodeenvioSerasa,             ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TP_ENVIO_COBRANCA_TERCEIRA')                               ");
		sql.append("                    AND vl_valor_dominio =                                                                  ");
		sql.append("                        Nvl(ECF.tp_envio_cobranca_terceira, 'A')) AS Tipodeenviocobrancaterceira,           ");
		sql.append("                (SELECT To_char(Wm_concat(co.ds_email))                                                     ");
		sql.append("                   FROM contato co                                                                          ");
		sql.append("                  WHERE co.tp_contato = 'FA'                                                                ");
		sql.append("                    AND co.ds_email IS NOT NULL                                                             ");
		sql.append("                    AND co.id_pessoa = f.id_cliente                                                         ");
		sql.append("                    AND ROWNUM <= 10) AS Emailfaturamento,                                                  ");
		sql.append("                (SELECT To_char(Wm_concat(co.ds_email))                                                     ");
		sql.append("                   FROM contato co                                                                          ");
		sql.append("                  WHERE co.tp_contato = 'CB'                                                                ");
		sql.append("                    AND co.ds_email IS NOT NULL                                                             ");
		sql.append("                    AND co.id_pessoa = f.id_cliente                                                         ");
		sql.append("                    AND ROWNUM <= 10) AS Emailcobranca,                                                     ");
		sql.append("                (SELECT ced.ds_cedente                                                                      ");
		sql.append("                   FROM cedente ced                                                                         ");
		sql.append("                  WHERE ced.id_cedente = f.id_cedente) AS Cedente,                                          ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_MODAL')                                                    ");
		sql.append("                    AND vl_valor_dominio = f.tp_modal) AS Modal,                                            ");
		sql.append("                (SELECT divcli.ds_divisao_cliente                                                           ");
		sql.append("                   FROM divisao_cliente divcli                                                              ");
		sql.append("                  WHERE divcli.id_divisao_cliente = f.id_divisao_cliente) AS Divisao,                       ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_STATUS_ROMANEIO')                                          ");
		sql.append("                    AND vl_valor_dominio = f.tp_situacao_fatura) AS Situacaofatura,                         ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_STATUS_WORKFLOW')                                          ");
		sql.append("                    AND vl_valor_dominio = f.tp_situacao_aprovacao) AS Situacaoaprovacao,                   ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_SIM_NAO')                                                  ");
		sql.append("                    AND vl_valor_dominio = Nvl(f.bl_indicador_impressao, 'N')) AS Faturaimpressa,           ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_ORIGEM_ROMANEIO')                                          ");
		sql.append("                    AND vl_valor_dominio = f.tp_origem) AS Origemfatura,                                    ");
		sql.append("                To_char(f.dt_emissao, 'DD/MM/YYYY') AS Dataemissao,                                         ");
		sql.append("                To_char(f.dt_vencimento, 'DD/MM/YYYY') AS Datavencimento,                                   ");
		sql.append("                To_char(f.dt_liquidacao, 'DD/MM/YYYY') AS Dataliquidacao,                                   ");
		sql.append("                To_char(Trunc(Cast(f.dh_negativacao_serasa AS DATE)),                                       ");
		sql.append("                        'DD/MM/YYYY') AS DatanegSerasa,                                                     ");
		sql.append("                To_char(Trunc(Cast(f.dh_exclusao_serasa AS DATE)),                                          ");
		sql.append("                        'DD/MM/YYYY') AS DataexclusaoSerasa,                                                ");
		sql.append("                To_char(Trunc(Cast(f.dh_envio_cob_terceira AS DATE)),                                       ");
		sql.append("                        'DD/MM/YYYY') AS DataenvioCobTerceira,                                              ");
		sql.append("                To_char(Trunc(Cast(f.dh_pagto_cob_terceira AS DATE)),                                       ");
		sql.append("                        'DD/MM/YYYY') AS DatapagtoCobTerceira,                                              ");
		sql.append("                To_char(Trunc(Cast(f.dh_devol_cob_terceira AS DATE)),                                       ");
		sql.append("                        'DD/MM/YYYY') AS DatadevolCobTerceira,                                              ");
		sql.append("                To_char(f.dt_pre_fatura, 'DD/MM/YYYY') AS Dataprefatura,                                    ");
		sql.append("                To_char(f.dt_importacao, 'DD/MM/YYYY') AS Dataimportacao,                                   ");
		sql.append("                To_char(f.dt_envio_aceite, 'DD/MM/YYYY') AS Dataenvioaceite,                                ");
		sql.append("                To_char(f.dt_retorno_aceite, 'DD/MM/YYYY') AS Dataretornoaceite,                            ");
		sql.append("                f.qt_documentos AS Qtdedocumentos,                                                          ");
		sql.append("                Trim(To_char(f.vl_total, '9G999G999G999G999G990D00')) AS Totalfatura,                       ");
		sql.append("                Trim(To_char(f.vl_desconto, '9G999G999G999G999G990D00')) AS Totaldescontos,                 ");
		sql.append("                Trim(To_char(f.vl_juro_calculado, '9G999G999G999G999G990D00')) AS Totaljuros,               ");
		sql.append("                Trim(To_char(Nvl((SELECT SUM(rpp.vl_pagamento)                                              ");
		sql.append("                                   FROM relacao_pagto_parcial rpp                                           ");
		sql.append("                                  WHERE rpp.id_fatura = f.id_fatura),                                       ");
		sql.append("                                 0),                                                                        ");
		sql.append("                             '9G999G999G999G999G990D00')) AS Valorrecebidoparcial,                          ");
		sql.append("                Trim(To_char(Decode(f.tp_situacao_fatura,                                                   ");
		sql.append("                                    'LI',                                                                   ");
		sql.append("                                    0,                                                                      ");
		sql.append("                                    f.vl_total -                                                            ");
		sql.append("                                    Nvl((SELECT SUM(rpp.vl_pagamento)                                       ");
		sql.append("                                          FROM relacao_pagto_parcial rpp                                    ");
		sql.append("                                         WHERE rpp.id_fatura = f.id_fatura),                                ");
		sql.append("                                        0) - f.vl_desconto),                                                ");
		sql.append("                             '9G999G999G999G999G990D00')) AS Valorsaldo,                                    ");
		sql.append("                (SELECT Max(rpp.dt_pagamento)                                                               ");
		sql.append("                   FROM relacao_pagto_parcial rpp                                                           ");
		sql.append("                  WHERE rpp.id_fatura = f.id_fatura) AS Ultimorecebparcial,                                 ");
		sql.append("                (SELECT fideb.sg_filial                                                                     ");
		sql.append("                   FROM filial fideb                                                                        ");
		sql.append("                  WHERE fideb.id_filial = f.id_filial_debitada) AS Filialdebitada,                          ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_SETOR_CAUSADOR')                                           ");
		sql.append("                    AND vl_valor_dominio = f.tp_setor_causador_abatimento) AS Setorcausadordoabatimento,    ");
		sql.append("                (SELECT TRANSLATE(Vi18n(mdes.ds_motivo_desconto_i),                                         ");
		sql.append("                          Chr(10) || Chr(11) || Chr(13),                                                    ");
		sql.append("                          ' ')                                                                              ");
		sql.append("                   FROM motivo_desconto mdes                                                                ");
		sql.append("                  WHERE mdes.id_motivo_desconto = f.id_motivo_desconto) AS Motivododesconto,                ");
		sql.append("                TRANSLATE(f.ob_acao_corretiva,                                                              ");
		sql.append("                          Chr(10) || Chr(11) || Chr(13),                                                    ");
		sql.append("                          ' ') AS Acaocorretiva,                                                            ");
		sql.append("                TRANSLATE(f.ob_fatura, Chr(10) || Chr(11) || Chr(13), ' ') AS Observacoes,                  ");
		sql.append("                                                                                                            ");
		sql.append("                b.nr_boleto AS Numboleto,                                                                   ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_STATUS_BOLETO')                                            ");
		sql.append("                    AND vl_valor_dominio = b.tp_situacao_boleto) AS Situacaoboleto,                         ");
		sql.append("                To_char(b.dt_emissao, 'DD/MM/YYYY') AS Dataemissaoboleto,                                   ");
		sql.append("                To_char(b.dt_vencimento, 'DD/MM/YYYY') AS Datavctoboleto,                                   ");
		sql.append("                To_char(b.dt_vencimento_novo, 'DD/MM/YYYY') AS Datavctoaprovacao,                           ");
		sql.append("                Trim(To_char(b.vl_total, '9G999G999G999G999G990D00')) AS Valortotalboleto,                  ");
		sql.append("                Trim(To_char(b.vl_desconto, '9G999G999G999G999G990D00')) AS Valordescontoboleto,            ");
		sql.append("                Trim(To_char(b.vl_juros_dia, '9G999G999G999G999G990D00')) AS Valorjurodiario,               ");
		sql.append("                (SELECT fred.sg_filial                                                                      ");
		sql.append("                   FROM filial fred                                                                         ");
		sql.append("                  WHERE fred.id_filial = red.id_filial) AS Filialredeco,                                    ");
		sql.append("                red.nr_redeco AS Numeroredeco,                                                              ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_STATUS_REDECO')                                            ");
		sql.append("                    AND vl_valor_dominio = red.tp_situacao_redeco) AS Situacaoredeco,                       ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_FINALIDADE_REDECO')                                        ");
		sql.append("                    AND vl_valor_dominio = red.tp_finalidade) AS Finalidaderedeco,                          ");
		sql.append("                (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_RECEBIMENTO')                                         ");
		sql.append("                    AND vl_valor_dominio = red.tp_recebimento) AS Tiporecebimento,                          ");
		sql.append("                To_char(red.dt_emissao, 'DD/MM/YYYY') AS Dataemissaoredeco,                                 ");
		sql.append("                To_char(red.dt_recebimento, 'DD/MM/YYYY') AS Datarecebimentoredeco,                         ");
		sql.append("                red.nm_responsavel_cobranca AS Cobrador,                                                    ");
		sql.append("                (select count(ic.id_fatura)                                                                 ");
		sql.append("                   from item_cobranca ic                                                                    ");
		sql.append("                  where ic.id_fatura = f.id_fatura) item_cobranca_count,                                    ");
		sql.append("                if_docto.*,                                                                                 ");
		sql.append("                mmf.*                                                                                       ");
		sql.append("           FROM regional reg,                                                                               ");
		sql.append("                regional_filial regf,                                                                       ");
		sql.append("                fatura f,                                                                                   ");
		sql.append("                (SELECT b.* FROM boleto b WHERE b.tp_situacao_boleto <> 'CA') b,                            ");
		sql.append("                pessoa pcli,                                                                                ");
		sql.append("                cliente cli,                                                                                ");
		sql.append("                excecao_cliente_financeiro ecf,                                                             ");
		sql.append("                (SELECT red.*, ired.id_fatura, ired.id_item_redeco                                          ");
		sql.append("                   FROM redeco red, item_redeco ired                                                        ");
		sql.append("                  WHERE red.id_redeco = ired.id_redeco                                                      ");
		sql.append("                    AND red.tp_situacao_redeco <> 'CA') red,                                                ");
		sql.append("                (SELECT if.id_fatura,                                                                       ");
		sql.append("                        max(dev.id_docto_servico) id_docto_servico_1                                        ");
		sql.append("                   FROM item_fatura IF, devedor_doc_serv_fat dev                                            ");
		sql.append("                  WHERE IF.id_devedor_doc_serv_fat =                                                        ");
		sql.append("                        dev.id_devedor_doc_serv_fat                                                         ");
		sql.append("                  group by if.id_fatura) if_docto,                                                          ");
		sql.append("                  (select id_fatura,																							");
		sql.append("                   To_char(Trunc(Cast(Max(dh_envio_fa) AS DATE)), 'DD/MM/YYYY') Dataenviomensagem, 								");
		sql.append("      			   To_char(Trunc(Cast(max(dh_recebimento_fa) AS DATE)), 'DD/MM/YYYY') Datarecebimentomensagem,                  ");
		sql.append("				   To_char(Trunc(Cast(max(dh_devolucao_fa) AS DATE)), 'DD/MM/YYYY') Datadevolucaomensagem,                      ");
		sql.append("				   To_char(Trunc(Cast(max(dh_erro_fa) AS DATE)), 'DD/MM/YYYY') Dataerromensagem,                                ");
		sql.append("				   To_char(Trunc(Cast(max(dh_envio_co) AS DATE)), 'DD/MM/YYYY') Dataenviomensagemcobranca,                      ");
		sql.append("				   To_char(Trunc(Cast(max(dh_recebimento_co) AS DATE)), 'DD/MM/YYYY') Dtrecmenscobranca,                        ");
		sql.append("				   To_char(Trunc(Cast(max(dh_devolucao_co) AS DATE)), 'DD/MM/YYYY') Datadevolucaomensagemcobranca,              ");
		sql.append("				   To_char(Trunc(Cast(max(dh_erro_co) AS DATE)), 'DD/MM/YYYY') Dataerromensagemcobranca,                        ");
		sql.append("				   To_char(Trunc(Cast(max(dh_envio_gl) AS DATE)), 'DD/MM/YYYY') Dataenviomensagemcobterceira,                   ");
		sql.append("				   To_char(Trunc(Cast(max(dh_recebimento_gl) AS DATE)), 'DD/MM/YYYY') Dtrecmenscobterceira,                     ");
		sql.append("				   To_char(Trunc(Cast(max(dh_devolucao_gl) AS DATE)), 'DD/MM/YYYY') Dtdevmenscobterceira,                       ");
		sql.append("				   To_char(Trunc(Cast(max(dh_erro_gl) AS DATE)), 'DD/MM/YYYY') Dataerromensagemcobterceira                      ");
		sql.append("				   from (                                                                                                       ");
		sql.append("						  SELECT mmf.id_fatura,                                                                                 ");
		sql.append("							case when mm.tp_modelo_mensagem = 'FA' then mmd.dh_envio else null end as dh_envio_fa,              ");
		sql.append("							case when mm.tp_modelo_mensagem = 'FA' then mmd.dh_recebimento else null end as dh_recebimento_fa,  ");
		sql.append("							case when mm.tp_modelo_mensagem = 'FA' then mmd.dh_devolucao else null end as dh_devolucao_fa,      ");
		sql.append("							case when mm.tp_modelo_mensagem = 'FA' then mmd.dh_erro end as dh_erro_fa,                          ");
		sql.append("							case when mm.tp_modelo_mensagem = 'CO' then mmd.dh_envio else null end as dh_envio_co,              ");
		sql.append("							case when mm.tp_modelo_mensagem = 'CO' then mmd.dh_recebimento else null end as dh_recebimento_co,  ");
		sql.append("							case when mm.tp_modelo_mensagem = 'CO' then mmd.dh_devolucao else null end as dh_devolucao_co,      ");
		sql.append("							case when mm.tp_modelo_mensagem = 'CO' then mmd.dh_erro end as dh_erro_co,                          ");
		sql.append("							case when mm.tp_modelo_mensagem = 'GL' then mmd.dh_envio else null end as dh_envio_gl,              ");
		sql.append("							case when mm.tp_modelo_mensagem = 'GL' then mmd.dh_recebimento else null end as dh_recebimento_gl,  ");
		sql.append("							case when mm.tp_modelo_mensagem = 'GL' then mmd.dh_devolucao else null end as dh_devolucao_gl,      ");
		sql.append("							case when mm.tp_modelo_mensagem = 'GL' then mmd.dh_erro end as dh_erro_gl                           ");
		sql.append("						  FROM monit_mens_detalhe mmd, monitoramento_mensagem mm,                                               ");
		sql.append("						  monit_mens_fatura mmf                                                                                 ");
		sql.append("						  WHERE mmd.id_monitoramento_mensagem = mm.id_monitoramento_mensagem (+)                                ");
		sql.append("						  and mmf.id_monitoramento_mensagem = mmd.id_monitoramento_mensagem(+)                                  ");
		sql.append("				        ) f group by id_fatura                                                                                  ");
		sql.append("				  ) mmf	                                                                                                        ");
		sql.append("          WHERE f.id_fatura = b.id_fatura( + )                                                              ");
		sql.append("            AND f.id_cliente = pcli.id_pessoa                                                               ");
		sql.append("            AND f.id_cliente = cli.id_cliente                                                               ");
		sql.append("            AND f.id_cliente = ecf.id_cliente(+)                                                            ");
		sql.append("            AND f.id_fatura = red.id_fatura(+)                                                              ");
		sql.append("            AND f.id_filial = regf.id_filial                                                                ");
		sql.append("            AND regf.id_regional = reg.id_regional                                                          ");
		sql.append("            AND if_docto.id_fatura(+) = f.id_fatura                                                         ");
		sql.append("            AND f.id_fatura = mmf.id_fatura(+)                                                              ");
		sql.append("            AND (red.id_redeco IS NULL OR                                                                   ");
		sql.append("                red.id_redeco IN                                                                            ");
		sql.append("                (SELECT Max(RED2.id_redeco)                                                                 ");
		sql.append("                    FROM redeco RED2, item_redeco IRED2                                                     ");
		sql.append("                   WHERE RED2.id_redeco = IRED2.id_redeco                                                   ");
		sql.append("                     AND RED2.tp_situacao_redeco <> 'CA'                                                    ");
		sql.append("                     AND IRED2.id_fatura = f.id_fatura))                                                    ");
		sql.append("            AND Trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND regf.dt_vigencia_final                  ");
		sql.append("            AND Trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND reg.dt_vigencia_final                    ");

		addWhereClausesToSql(tfm, objects, sql);
		if ((tfm.get("dtLiquidacaoInicial") == null) && (tfm.get("dtLiquidacaoFinal") == null)) {
			sql.append("    AND f.TP_SITUACAO_FATURA NOT IN ('LI', 'CA')) f ");
		} else {
			sql.append("    AND f.TP_SITUACAO_FATURA NOT IN ('CA')) f ");
		}
	}

	private void addWhereClausesToSql(TypedFlatMap tfm,
	                                  Map<String, Object> objects, StringBuilder sql) {
		if (tfm.get("filialResponsavel") != null) {
			sql.append("    AND cli.id_filial_cobranca = :filialResponsavel");
			objects.put("filialResponsavel", tfm.get("filialResponsavel"));
		}
		if (tfm.get("tipoCliente") != null) {
			sql.append("    AND cli.tp_cliente = :tipoCliente ");
			objects.put("tipoCliente", tfm.get("tipoCliente"));
		}
		if (tfm.get("classificacaoCliente") != null) {
			sql.append("    AND nvl(ecf.tp_cliente, 'G') = :classificacaoCliente ");
			objects.put("classificacaoCliente", tfm.get("classificacaoCliente"));
		}
		if (tfm.get("tipoCobranca") != null) {
			sql.append("    AND cli.tp_cobranca = :tipoCobranca ");
			objects.put("tipoCobranca", tfm.get("tipoCobranca"));
		}
		if (tfm.get("cobrancaCentralizada") != null) {
			sql.append("    AND cli.bl_cobranca_centralizada = :cobrancaCentralizada ");
			objects.put("cobrancaCentralizada", tfm.get("cobrancaCentralizada"));
		}
		if (tfm.get("clienteComPreFatura") != null) {
			sql.append("    AND cli.bl_pre_fatura = :clienteComPreFatura ");
			objects.put("clienteComPreFatura", tfm.get("clienteComPreFatura"));
		}
		if (tfm.get("clienteEmailFaturamento") != null) {
			sql.append("    AND ((:clienteEmailFaturamento1 = 'S' AND EXISTS ");
			sql.append("         (SELECT 1 ");
			sql.append("             FROM contato co ");
			sql.append("            WHERE co.tp_contato = 'FA' ");
			sql.append("              AND co.ds_email IS NOT NULL ");
			sql.append("              AND co.id_pessoa = f.id_cliente ");
			sql.append("              AND rownum = 1)) OR (:clienteEmailFaturamento2 = 'N')) ");
			objects.put("clienteEmailFaturamento1", tfm.get("clienteEmailFaturamento"));
			objects.put("clienteEmailFaturamento2", tfm.get("clienteEmailFaturamento"));
		}
		if (tfm.get("clienteEmailCobranca") != null) {
			sql.append("    AND ((:clienteEmailCobranca1 = 'S' AND EXISTS ");
			sql.append("         (SELECT 1 ");
			sql.append("             FROM contato co ");
			sql.append("            WHERE co.tp_contato = 'CB' ");
			sql.append("              AND co.ds_email IS NOT NULL ");
			sql.append("              AND co.id_pessoa = f.id_cliente ");
			sql.append("              AND rownum = 1)) OR (:clienteEmailCobranca2 = 'N')) ");
			objects.put("clienteEmailCobranca1", tfm.get("clienteEmailCobranca"));
			objects.put("clienteEmailCobranca2", tfm.get("clienteEmailCobranca"));
		}
		if (tfm.get("devedoresExcluir") != null) {
			String[] devedores = ((String) tfm.get("devedoresExcluir")).split(";");
			if (devedores.length > 0 && !devedores[0].isEmpty()) {
				sql.append("   AND (pcli.nr_identificacao LIKE -1");
				for (String devedor : devedores) {
					sql.append(" OR pcli.nr_identificacao NOT LIKE '" + devedor + "' ");
				}
				sql.append(" ) ");
			}
		}
		if (tfm.get("devedoresListar") != null) {
			String[] devedores = ((String) tfm.get("devedoresListar")).split(";");
			if (devedores.length > 0 && !devedores[0].isEmpty()) {
				sql.append("   AND (pcli.nr_identificacao LIKE -1");
				for (String devedor : devedores) {
					sql.append(" OR pcli.nr_identificacao like '" + devedor + "' ");
				}
				sql.append(" ) ");
			}
		}
		if (tfm.get("idServico") != null) {
			sql.append(" AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM item_fatura          IF, ");
			sql.append("                devedor_doc_serv_fat dev, ");
			sql.append("                docto_servico        ds ");
			sql.append("          WHERE IF.id_fatura = f.id_fatura ");
			sql.append("            AND IF.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
			sql.append("            AND dev.id_docto_servico = ds.id_docto_servico ");
			sql.append("            AND ds.id_servico = :idServico ");
			sql.append("            AND rownum = 1) ");
			objects.put("idServico", tfm.get("idServico"));
		}
		if (tfm.get("dtVigenciaInicial") != null || tfm.get("dtVigenciaFinal") != null) {
			sql.append(" AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM item_fatura          IF, ");
			sql.append("                devedor_doc_serv_fat dev, ");
			sql.append("                docto_servico        ds ");
			sql.append("          WHERE IF.id_fatura = f.id_fatura ");
			sql.append("            AND IF.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
			sql.append("            AND dev.id_docto_servico = ds.id_docto_servico ");
			if (tfm.get("dtVigenciaInicial") != null) {
				sql.append("            AND trunc(CAST(ds.dh_emissao AS DATE)) >= :dtVigenciaInicial ");
				objects.put("dtVigenciaInicial", tfm.get("dtVigenciaInicial"));
			}
			if (tfm.get("dtVigenciaFinal") != null) {
				sql.append("            AND trunc(CAST(ds.dh_emissao AS DATE)) <= :dtVigenciaFinal ");
				objects.put("dtVigenciaFinal", tfm.get("dtVigenciaFinal"));
			}
			sql.append("            AND rownum = 1) ");
		}
		if (tfm.get("tipoDocumento") != null) {
			sql.append(" AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM item_fatura          IF, ");
			sql.append("                devedor_doc_serv_fat dev, ");
			sql.append("                docto_servico        ds ");
			sql.append("          WHERE IF.id_fatura = f.id_fatura ");
			sql.append("            AND IF.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
			sql.append("            AND dev.id_docto_servico = ds.id_docto_servico ");
			sql.append("            AND ds.tp_documento_servico = :tipoDocumento ");
			sql.append("            AND rownum = 1) ");
			objects.put("tipoDocumento", tfm.get("tipoDocumento"));
		}
		if (tfm.get("tipoCalculo") != null) {
			sql.append(" AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM item_fatura          IF, ");
			sql.append("                devedor_doc_serv_fat dev, ");
			sql.append("                docto_servico        ds ");
			sql.append("          WHERE IF.id_fatura = f.id_fatura ");
			sql.append("            AND IF.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
			sql.append("            AND dev.id_docto_servico = ds.id_docto_servico ");
			sql.append("            AND ds.tp_calculo_preco = :tipoCalculo ");
			sql.append("            AND rownum = 1) ");
			objects.put("tipoCalculo", tfm.get("tipoCalculo"));
		}
		if (tfm.get("tipoFrete") != null) {
			sql.append(" AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM item_fatura          IF, ");
			sql.append("                devedor_doc_serv_fat dev, ");
			sql.append("                conhecimento         con ");
			sql.append("          WHERE IF.id_fatura = f.id_fatura ");
			sql.append("            AND IF.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
			sql.append("            AND dev.id_docto_servico = con.id_conhecimento");
			sql.append("            AND con.tp_frete = :tipoFrete ");
			sql.append("            AND rownum = 1) ");
			objects.put("tipoFrete", tfm.get("tipoFrete"));
		}
		if (tfm.get("tipoConhecimento") != null) {
			sql.append(" AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM item_fatura          IF, ");
			sql.append("                devedor_doc_serv_fat dev, ");
			sql.append("                conhecimento         con ");
			sql.append("          WHERE IF.id_fatura = f.id_fatura ");
			sql.append("            AND IF.id_devedor_doc_serv_fat = dev.id_devedor_doc_serv_fat ");
			sql.append("            AND dev.id_docto_servico = con.id_conhecimento ");
			sql.append("            AND con.tp_conhecimento = :tipoConhecimento ");
			sql.append("            AND rownum = 1) ");
			objects.put("tipoConhecimento", tfm.get("tipoConhecimento"));
		}
		if (tfm.get("idRegional") != null) {
			sql.append("    AND reg.id_regional = :idRegional ");
			objects.put("idRegional", tfm.get("idRegional"));
		}
		if (tfm.get("idFilialCobranca") != null) {
			sql.append("    AND f.id_filial = :idFilialCobranca ");
			objects.put("idFilialCobranca", tfm.get("idFilialCobranca"));
		}
		if (tfm.get("dtEmissaoFatInicial") != null) {
			sql.append("    AND f.dt_emissao >= :dtEmissaoFatInicial ");
			objects.put("dtEmissaoFatInicial", tfm.get("dtEmissaoFatInicial"));
		}
		if (tfm.get("dtEmissaoFatFinal") != null) {
			sql.append("    AND f.dt_emissao <= :dtEmissaoFatFinal ");
			objects.put("dtEmissaoFatFinal", tfm.get("dtEmissaoFatFinal"));
		}
		if (tfm.get("dtVencimentoInicial") != null) {
			sql.append("    AND f.dt_vencimento >= :dtVencimentoInicial ");
			objects.put("dtVencimentoInicial", tfm.get("dtVencimentoInicial"));
		}
		if (tfm.get("dtVencimentoFinal") != null) {
			sql.append("    AND f.dt_vencimento <= :dtVencimentoFinal ");
			objects.put("dtVencimentoFinal", tfm.get("dtVencimentoFinal"));
		}
		if (tfm.get("dtNegSerasaInicial") != null) {
			sql.append("    AND f.dh_negativacao_serasa >= :dtNegSerasaInicial ");
			objects.put("dtNegSerasaInicial", tfm.get("dtNegSerasaInicial"));
		}
		if (tfm.get("dtNegSerasaFinal") != null) {
			sql.append("    AND f.dh_negativacao_serasa <= :dtNegSerasaFinal + 1 ");
			objects.put("dtNegSerasaFinal", tfm.get("dtNegSerasaFinal"));
		}
		if (tfm.get("dtExecSerasaInicial") != null) {
			sql.append("    AND f.dh_exclusao_serasa >= :dtExecSerasaInicial ");
			objects.put("dtExecSerasaInicial", tfm.get("dtExecSerasaInicial"));
		}
		if (tfm.get("dtExecSerasaFinal") != null) {
			sql.append("    AND f.dh_exclusao_serasa <= :dtExecSerasaFinal + 1 ");
			objects.put("dtExecSerasaFinal", tfm.get("dtExecSerasaFinal"));
		}

		if (tfm.get("dtEnvioCobTerceiraInicial") != null) {
			sql.append("    AND f.dh_envio_cob_terceira >= :dtEnvioCobTerceiraInicial ");
			objects.put("dtEnvioCobTerceiraInicial", tfm.get("dtEnvioCobTerceiraInicial"));
		}
		if (tfm.get("dtEnvioCobTerceiraFinal") != null) {
			sql.append("    AND f.dh_envio_cob_terceira <= :dtEnvioCobTerceiraFinal ");
			objects.put("dtEnvioCobTerceiraFinal", tfm.get("dtEnvioCobTerceiraFinal"));
		}

		if (tfm.get("dtPagtoCobTerceiraInicial") != null) {
			sql.append("    AND f.dh_pagto_cob_terceira >= :dtPagtoCobTerceiraInicial ");
			objects.put("dtPagtoCobTerceiraInicial", tfm.get("dtPagtoCobTerceiraInicial"));
		}
		if (tfm.get("dtPagtoCobTerceiraFinal") != null) {
			sql.append("    AND f.dh_pagto_cob_terceira <= :dtPagtoCobTerceiraFinal ");
			objects.put("dtPagtoCobTerceiraFinal", tfm.get("dtPagtoCobTerceiraFinal"));
		}

		if (tfm.get("dtDevolCobTerceiraInicial") != null) {
			sql.append("    AND f.dh_devol_cob_terceira >= :dtDevolCobTerceiraInicial ");
			objects.put("dtDevolCobTerceiraInicial", tfm.get("dtDevolCobTerceiraInicial"));
		}
		if (tfm.get("dtDevolCobTerceiraFinal") != null) {
			sql.append("    AND f.dh_devol_cob_terceira <= :dtDevolCobTerceiraFinal ");
			objects.put("dtDevolCobTerceiraFinal", tfm.get("dtDevolCobTerceiraFinal"));
		}

		if (tfm.get("dtEnvioInicial") != null || tfm.get("dtEnvioFinal") != null) {
			sql.append("    AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM monit_mens_fatura      mmf, ");
			sql.append("                monit_mens_detalhe     mmd, ");
			sql.append("                monitoramento_mensagem mm ");
			sql.append("          WHERE mmf.id_monitoramento_mensagem = mmd.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_fatura = f.id_fatura ");
			sql.append("            AND mm.tp_modelo_mensagem = 'FA' ");
			if (tfm.get("dtEnvioInicial") != null) {
				sql.append("            AND mmd.dh_envio >= :dtEnvioInicial ");
				objects.put("dtEnvioInicial", tfm.get("dtEnvioInicial"));
			}
			if (tfm.get("dtEnvioFinal") != null) {
				sql.append("            AND mmd.dh_envio <= :dtEnvioFinal + 1");
				objects.put("dtEnvioFinal", tfm.get("dtEnvioFinal"));
			}
			sql.append(") ");
		}
		if (tfm.get("dtRecebimentoInicial") != null || tfm.get("dtRecebimentoFinal") != null) {
			sql.append("    AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM monit_mens_fatura      mmf, ");
			sql.append("                monit_mens_detalhe     mmd, ");
			sql.append("                monitoramento_mensagem mm ");
			sql.append("          WHERE mmf.id_monitoramento_mensagem = mmd.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_fatura = f.id_fatura ");
			sql.append("            AND mm.tp_modelo_mensagem = 'FA' ");
			if (tfm.get("dtRecebimentoInicial") != null) {
				sql.append("            AND mmd.dh_recebimento >= :dtRecebimentoInicial ");
				objects.put("dtRecebimentoInicial", tfm.get("dtRecebimentoInicial"));
			}
			if (tfm.get("dtRecebimentoFinal") != null) {
				sql.append("            AND mmd.dh_recebimento <= :dtRecebimentoFinal + 1");
				objects.put("dtRecebimentoFinal", tfm.get("dtRecebimentoFinal"));
			}
			sql.append(" ) ");
		}
		if (tfm.get("dtDevolucaoInicial") != null || tfm.get("dtDevolucaoFinal") != null) {
			sql.append("    AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM monit_mens_fatura      mmf, ");
			sql.append("                monit_mens_detalhe     mmd, ");
			sql.append("                monitoramento_mensagem mm ");
			sql.append("          WHERE mmf.id_monitoramento_mensagem = mmd.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_fatura = f.id_fatura ");
			sql.append("            AND mm.tp_modelo_mensagem = 'FA' ");
			if (tfm.get("dtDevolucaoInicial") != null) {
				sql.append("            AND mmd.dh_devolucao >= :dtDevolucaoInicial ");
				objects.put("dtDevolucaoInicial", tfm.get("dtDevolucaoInicial"));
			}
			if (tfm.get("dtDevolucaoFinal") != null) {
				sql.append("            AND mmd.dh_devolucao <= :dtDevolucaoFinal + 1");
				objects.put("dtDevolucaoFinal", tfm.get("dtDevolucaoFinal"));
			}
			sql.append(" ) ");
		}
		if (tfm.get("dtErroInicial") != null || tfm.get("dtErroFinal") != null) {
			sql.append("    AND EXISTS ");
			sql.append("  (SELECT 1 ");
			sql.append("           FROM monit_mens_fatura      mmf, ");
			sql.append("                monit_mens_detalhe     mmd, ");
			sql.append("                monitoramento_mensagem mm ");
			sql.append("          WHERE mmf.id_monitoramento_mensagem = mmd.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem ");
			sql.append("            AND mmf.id_fatura = f.id_fatura ");
			sql.append("            AND mm.tp_modelo_mensagem = 'FA' ");
			if (tfm.get("dtErroInicial") != null) {
				sql.append("            AND mmd.dh_erro >= :dtErroInicial ");
				objects.put("dtErroInicial", tfm.get("dtErroInicial"));
			}
			if (tfm.get("dtErroFinal") != null) {
				sql.append("            AND mmd.dh_erro <= :dtErroFinal + 1 ");
				objects.put("dtErroFinal", tfm.get("dtErroFinal"));
			}
			sql.append(" ) ");
		}
		if (tfm.get("dtEmissaoBolInicial") != null) {
			sql.append("    AND b.dt_emissao >= :dtEmissaoBolInicial ");
			objects.put("dtEmissaoBolInicial", tfm.get("dtEmissaoBolInicial"));
		}
		if (tfm.get("dtEmissaoBolFinal") != null) {
			sql.append("    AND b.dt_emissao <= :dtEmissaoBolFinal ");
			objects.put("dtEmissaoBolFinal", tfm.get("dtEmissaoBolFinal"));
		}
		if (tfm.get("nrFaturaInicial") != null) {
			sql.append("    AND f.nr_fatura >= :nrFaturaInicial ");
			objects.put("nrFaturaInicial", tfm.get("nrFaturaInicial"));
		}
		if (tfm.get("nrFaturaFinal") != null) {
			sql.append("    AND f.nr_fatura <= :nrFaturaFinal ");
			objects.put("nrFaturaFinal", tfm.get("nrFaturaFinal"));
		}
		if (tfm.get("preFatura") != null) {
			sql.append("    AND f.nr_pre_fatura LIKE :preFatura ");
			objects.put("preFatura", tfm.get("preFatura"));
		}
		if (tfm.get("situacaoFatura") != null) {
			sql.append("    AND f.tp_situacao_fatura = :situacaoFatura ");
			objects.put("situacaoFatura", tfm.get("situacaoFatura"));
		}
		if (tfm.get("situacaoBoleto") != null) {
			sql.append("    AND b.tp_situacao_boleto = :situacaoBoleto ");
			objects.put("situacaoBoleto", tfm.get("situacaoBoleto"));
		}
		if (tfm.get(KEY_MODAL) != null) {
			sql.append("    AND f.tp_modal = :modal ");
			objects.put(KEY_MODAL, tfm.get(KEY_MODAL));
		}
		if (tfm.get(KEY_ABRANGENCIA) != null) {
			sql.append("    AND f.tp_abrangencia = :abrangencia ");
			objects.put(KEY_ABRANGENCIA, tfm.get(KEY_ABRANGENCIA));
		}
		if (tfm.get("situacaoAprovacao") != null) {
			sql.append("    AND nvl(f.tp_situacao_aprovacao, 'A') = :situacaoAprovacao ");
			objects.put("situacaoAprovacao", tfm.get("situacaoAprovacao"));
		}
		if (tfm.get("comTratativa") != null) {
			sql.append("    AND ((:comTratativa = 'S' AND EXISTS ");
			sql.append("         (SELECT 1 ");
			sql.append("             FROM ITEM_COBRANCA ic, ");
			sql.append("				  TRATATIVA_COB_INADIMPLENCIA TCI ");
			sql.append("            WHERE IC.ID_COBRANCA_INADIMPLENCIA = TCI.ID_COBRANCA_INADIMPLENCIA ");
			sql.append("              AND IC.ID_FATURA = F.ID_FATURA ");
			sql.append("              AND rownum = 1)) OR (:comTratativa2 = 'N' and not exists ( ");
			sql.append("              												select 1 ");
			sql.append("              												  from ITEM_COBRANCA IC, ");
			sql.append("              												       TRATATIVA_COB_INADIMPLENCIA TCI ");
			sql.append("              											     WHERE IC.ID_COBRANCA_INADIMPLENCIA = TCI.ID_COBRANCA_INADIMPLENCIA ");
			sql.append("              											       and IC.ID_FATURA = F.ID_FATURA ");
			sql.append("              											       and ROWNUM = 1))) ");
			objects.put("comTratativa", tfm.get("comTratativa"));
			objects.put("comTratativa2", tfm.get("comTratativa"));
		}
		if (tfm.get("dtLiquidacaoInicial") != null) {
			sql.append("    AND f.DT_LIQUIDACAO >= :dtLiquidacaoInicial ");
			objects.put("dtLiquidacaoInicial", tfm.get("dtLiquidacaoInicial"));
		}
		if (tfm.get("dtLiquidacaoFinal") != null) {
			sql.append("    AND f.DT_LIQUIDACAO <= :dtLiquidacaoFinal ");
			objects.put("dtLiquidacaoFinal", tfm.get("dtLiquidacaoFinal"));
		}
	}

	private void getFullQueryVencidadsVencer(StringBuilder sql) {
		sql.append(" select f.Regional,                                                                                         ");
		sql.append("        f.Descricoreg,                                                                                      ");
		sql.append("        f.Filial,                                                                                           ");
		sql.append("        f.Numero,                                                                                           ");
		sql.append("        f.CNPJ,                                                                                             ");
		sql.append("        f.Razaosocial,                                                                                      ");
		sql.append("        f.Filialcobcliente,                                                                                 ");
		sql.append(" 	   f.Tipopessoa,                                                                                        ");
		sql.append(" 	   f.Tipocliente,                                                                                       ");
		sql.append(" 	   f.Tipocobranca,                                                                                      ");
		sql.append(" 	   f.Clientecomprefatura,                                                                               ");
		sql.append(" 	   f.Cobrancacentralizada,                                                                              ");
		sql.append(" 	   f.Classificacaocliente,                                                                              ");
		sql.append(" 	   f.Tipodeenviofaturamento,                                                                            ");
		sql.append(" 	   f.Tipodeenviocartacobranca,                                                                          ");
		sql.append(" 	   f.TipodeenvioSerasa,                                                                                 ");
		sql.append(" 	   f.Tipodeenviocobrancaterceira,                                                                       ");
		sql.append(" 	   f.Emailfaturamento,                                                                                  ");
		sql.append(" 	   f.Emailcobranca,                                                                                     ");
		sql.append(" 	   f.Cedente,                                                                                           ");
		sql.append(" 	   f.Modal,                                                                                             ");
		sql.append(" 	   (SELECT Vi18n(s.ds_servico_i)                                                                        ");
		sql.append("           FROM docto_servico ds, servico s                                                                 ");
		sql.append("          WHERE ds.id_docto_servico = id_docto_servico_1                                                    ");
		sql.append("            AND ds.id_servico = s.id_servico) AS Servico,                                                   ");
		sql.append(" 	   f.Divisao,                                                                                           ");
		sql.append(" 	   f.Situacaofatura,                                                                                    ");
		sql.append(" 	   f.Situacaoaprovacao,                                                                                 ");
		sql.append(" (SELECT (SELECT Vi18n(ds_valor_dominio_i)                                                                  ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO')                                   ");
		sql.append("                    AND vl_valor_dominio = ds.tp_documento_servico)                                         ");
		sql.append("           FROM docto_servico ds                                                                            ");
		sql.append("          WHERE ds.id_docto_servico = id_docto_servico_1) AS Tipodocumentodeservico,                        ");
		sql.append("                                                                                                            ");
		sql.append("        (SELECT (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_CONHECIMENTO')                                        ");
		sql.append("                    AND vl_valor_dominio = con.tp_conhecimento)                                             ");
		sql.append("           FROM conhecimento con                                                                            ");
		sql.append("          WHERE con.id_conhecimento = id_docto_servico_1) AS Tipoconhecimento,                              ");
		sql.append("                                                                                                            ");
		sql.append("        (SELECT (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_FRETE')                                               ");
		sql.append("                    AND vl_valor_dominio = con.tp_frete)                                                    ");
		sql.append("           FROM conhecimento con                                                                            ");
		sql.append("          WHERE con.id_conhecimento = id_docto_servico_1) AS Tipofreteconhecimento,                         ");
		sql.append("                                                                                                            ");
		sql.append("        (SELECT (SELECT Vi18n(ds_valor_dominio_i)                                                           ");
		sql.append("                   FROM valor_dominio                                                                       ");
		sql.append("                  WHERE id_dominio IN                                                                       ");
		sql.append("                        (SELECT id_dominio                                                                  ");
		sql.append("                           FROM dominio                                                                     ");
		sql.append("                          WHERE nm_dominio = 'DM_TIPO_CALCULO_FRETE')                                       ");
		sql.append("                    AND vl_valor_dominio = ds.tp_calculo_preco)                                             ");
		sql.append("           FROM docto_servico ds                                                                            ");
		sql.append("          WHERE ds.id_docto_servico = id_docto_servico_1) AS Tipocalculo,                                   ");
		sql.append("                                                                                                            ");
		sql.append("        (SELECT Vi18n(sa.ds_servico_adicional_i)                                                            ");
		sql.append("           FROM docto_servico           ds,                                                                 ");
		sql.append("                serv_adicional_doc_serv sads,                                                               ");
		sql.append("                servico_adicional       sa                                                                  ");
		sql.append("          WHERE ds.id_docto_servico = id_docto_servico_1                                                    ");
		sql.append("            and ds.id_docto_servico = sads.id_docto_servico                                                 ");
		sql.append("            AND sads.id_servico_adicional = sa.id_servico_adicional                                         ");
		sql.append("            AND ROWNUM = 1) AS Servicoadicional,                                                            ");
		sql.append("                                                                                                            ");
		sql.append("        (SELECT To_char(Wm_concat(                                                                          ");
		sql.append("                                  (SELECT Substr(pciaaw.nm_pessoa, 1, 3)                                    ");
		sql.append("                                     FROM pessoa pciaaw,                                                    ");
		sql.append("                                          cia_filial_mercurio cfm                                           ");
		sql.append("                                    WHERE cfm.id_empresa = pciaaw.id_pessoa                                 ");
		sql.append("                                      AND cfm.id_cia_filial_mercurio = aw.id_cia_filial_mercurio) || ' ' || ");
		sql.append("                                  aw.ds_serie || ' ' || aw.nr_awb ||                                        ");
		sql.append("                                  Decode(aw.dv_awb,                                                         ");
		sql.append("                                         NULL,                                                              ");
		sql.append("                                         '',                                                                ");
		sql.append("                                         '-' || aw.dv_awb)))                                                ");
		sql.append("           FROM awb                  aw,                                                                    ");
		sql.append("                cto_awb              caw,                                                                   ");
		sql.append("                devedor_doc_serv_fat dev                                                                    ");
		sql.append("          WHERE aw.id_awb = caw.id_awb                                                                      ");
		sql.append("            AND caw.id_conhecimento = dev.id_docto_servico                                                  ");
		sql.append("            AND dev.id_fatura = f.IDfatura                                                                  ");
		sql.append("            AND aw.tp_status_awb = 'E'                                                                      ");
		sql.append("            AND ROWNUM <= 10) AS AWB,                                                                       ");
		sql.append(" 	   f.Faturaimpressa,                                                                                    ");
		sql.append(" 	   f.Origemfatura,                                                                                      ");
		sql.append(" 	   f.Dataemissao,                                                                                       ");
		sql.append(" 	   f.Datavencimento,                                                                                    ");
		sql.append(" 	   f.Dataliquidacao,                                                                                    ");
		sql.append(" 	   f.DatanegSerasa,                                                                                     ");
		sql.append(" 	   f.DataexclusaoSerasa,                                                                                ");
		sql.append(" 	   f.DataenvioCobTerceira,                                                                              ");
		sql.append(" 	   f.DatapagtoCobTerceira,                                                                              ");
		sql.append(" 	   f.DatadevolCobTerceira,                                                                              ");
		sql.append("       f.Dataenviomensagem,               																	");
		sql.append("       f.Datarecebimentomensagem,         																	");
		sql.append("       f.Datadevolucaomensagem,           																	");
		sql.append("       f.Dataerromensagem,                																	");
		sql.append("       f.Dataenviomensagemcobranca,       																	");
		sql.append("       f.Dtrecmenscobranca,               																	");
		sql.append("       f.Datadevolucaomensagemcobranca,   																	");
		sql.append("       f.Dataerromensagemcobranca,        																	");
		sql.append("       f.Dataenviomensagemcobterceira,    																	");
		sql.append("       f.Dtrecmenscobterceira,            																	");
		sql.append("       f.Dtdevmenscobterceira,            																	");
		sql.append("       f.Dataerromensagemcobterceira,     																	");
		sql.append("       f.Dataprefatura,                                                                                     ");
		sql.append(" 	   f.Dataimportacao,                                                                                    ");
		sql.append(" 	   f.Dataenvioaceite,                                                                                   ");
		sql.append(" 	   f.Dataretornoaceite,                                                                                 ");
		sql.append(" 	   f.Qtdedocumentos,                                                                                    ");
		sql.append(" 	   f.Totalfatura,                                                                                       ");
		sql.append(" 	   f.Totaldescontos,                                                                                    ");
		sql.append(" 	   f.Totaljuros,                                                                                        ");
		sql.append(" 	   f.Valorrecebidoparcial,                                                                              ");
		sql.append(" 	   f.Valorsaldo,                                                                                        ");
		sql.append(" 	   f.Ultimorecebparcial,                                                                                ");
		sql.append(" 	   f.Filialdebitada,                                                                                    ");
		sql.append(" 	   f.Setorcausadordoabatimento,                                                                         ");
		sql.append(" 	   f.Motivododesconto,                                                                                  ");
		sql.append(" 	   f.Acaocorretiva,                                                                                     ");
		sql.append(" 	   f.Observacoes,                                                                                       ");
		sql.append(" 	   case                                                                                                 ");
		sql.append("          when item_cobranca_count = 0 then                                                                 ");
		sql.append("           null                                                                                             ");
		sql.append("          else                                                                                              ");
		sql.append("           (SELECT                                                                                          ");
		sql.append("            /*+ FULL(TCI) FULL(IC) USE_HASH(TCI) USE_HASH(IC)*/                                             ");
		sql.append("             To_char(Max(TCI.dh_tratativa), 'DD/MM/YYYY HH24:MI')                                           ");
		sql.append("              FROM item_cobranca IC, tratativa_cob_inadimplencia TCI                                        ");
		sql.append("             WHERE IC.id_cobranca_inadimplencia =                                                           ");
		sql.append("                   TCI.id_cobranca_inadimplencia                                                            ");
		sql.append("               AND IC.id_fatura = F.IDfatura                                                                ");
		sql.append("               AND TCI.dh_tratativa =                                                                       ");
		sql.append("                   (SELECT Max(TCI2.dh_tratativa)                                                           ");
		sql.append("                      FROM tratativa_cob_inadimplencia TCI2                                                 ");
		sql.append("                     WHERE TCI2.id_cobranca_inadimplencia =                                                 ");
		sql.append("                           IC.id_cobranca_inadimplencia)                                                    ");
		sql.append("               AND ROWNUM = 1)                                                                              ");
		sql.append("        end AS Datatratativa,                                                                               ");
		sql.append("        case                                                                                                ");
		sql.append("          when item_cobranca_count = 0 then                                                                 ");
		sql.append("           null                                                                                             ");
		sql.append("          else                                                                                              ");
		sql.append("           (SELECT                                                                                          ");
		sql.append("            /*+ FULL(TCI) FULL(IC) USE_HASH(TCI) USE_HASH(IC)*/                                             ");
		sql.append("             US.nm_usuario                                                                                  ");
		sql.append("              FROM item_cobranca               IC,                                                          ");
		sql.append("                   tratativa_cob_inadimplencia TCI,                                                         ");
		sql.append("                   usuario                     US                                                           ");
		sql.append("             WHERE IC.id_cobranca_inadimplencia =                                                           ");
		sql.append("                   TCI.id_cobranca_inadimplencia                                                            ");
		sql.append("               AND IC.id_fatura = F.IDfatura                                                                ");
		sql.append("               AND TCI.id_usuario = US.id_usuario                                                           ");
		sql.append("               AND TCI.dh_tratativa =                                                                       ");
		sql.append("                   (SELECT Max(TCI2.dh_tratativa)                                                           ");
		sql.append("                      FROM tratativa_cob_inadimplencia TCI2                                                 ");
		sql.append("                     WHERE TCI2.id_cobranca_inadimplencia =                                                 ");
		sql.append("                           IC.id_cobranca_inadimplencia)                                                    ");
		sql.append("               AND ROWNUM = 1)                                                                              ");
		sql.append("        end AS Usuariotratativa,                                                                            ");
		sql.append("       f.Numboleto,                                                                                         ");
		sql.append(" 	   f.Situacaoboleto,                                                                                    ");
		sql.append(" 	   f.Dataemissaoboleto,                                                                                 ");
		sql.append(" 	   f.Datavctoboleto,                                                                                    ");
		sql.append(" 	   f.Datavctoaprovacao,                                                                                 ");
		sql.append(" 	   f.Valortotalboleto,                                                                                  ");
		sql.append(" 	   f.Valordescontoboleto,                                                                               ");
		sql.append(" 	   f.Valorjurodiario,                                                                                   ");
		sql.append(" 	   f.Filialredeco,                                                                                      ");
		sql.append(" 	   f.Numeroredeco,                                                                                      ");
		sql.append(" 	   f.Situacaoredeco,                                                                                    ");
		sql.append(" 	   f.Finalidaderedeco,                                                                                  ");
		sql.append(" 	   f.Tiporecebimento,                                                                                   ");
		sql.append(" 	   f.Dataemissaoredeco,                                                                                 ");
		sql.append(" 	   f.Datarecebimentoredeco,                                                                             ");
		sql.append(" 	   f.Cobrador                                                                                           ");
	}


	private void getSmallQueryVencidadsVencer(StringBuilder sql) {
		sql.append(" SELECT fi.sg_filial as Filial, ");
		sql.append("        f.nr_fatura as Numero, ");
		sql.append("        pcli.nr_identificacao as CNPJ, ");
		sql.append("        pcli.nm_pessoa as Razaosocial, ");
		sql.append("        to_char(f.dt_emissao, 'DD/MM/YYYY') as Dataemissao, ");
		sql.append("        to_char(f.dt_vencimento, 'DD/MM/YYYY') as Datavencimento, ");
		sql.append("        (SELECT vi18n(ds_valor_dominio_i) ");
		sql.append("           FROM valor_dominio ");
		sql.append("          WHERE id_dominio IN ");
		sql.append("                (SELECT id_dominio ");
		sql.append("                   FROM dominio ");
		sql.append("                  WHERE nm_dominio = 'DM_STATUS_ROMANEIO') ");
		sql.append("            AND vl_valor_dominio = f.tp_situacao_fatura) as Situacaofatura, ");
		sql.append("        TRIM(to_char(f.vl_total, '9G999G999G999G999G990D00')) as Totalfatura, ");
		sql.append("        TRIM(to_char(f.vl_desconto, '9G999G999G999G999G990D00')) as Totaldescontos, ");
		sql.append("        b.nr_boleto as Numboleto ");
	}

	private ConfigureSqlQuery configureSqlSmallQueryFaturasVencidasEAVencer() {
		return new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Filial", Hibernate.STRING);
				sqlQuery.addScalar("Numero", Hibernate.STRING);
				sqlQuery.addScalar("CNPJ", Hibernate.STRING);
				sqlQuery.addScalar("Razaosocial", Hibernate.STRING);
				sqlQuery.addScalar("Dataemissao", Hibernate.STRING);
				sqlQuery.addScalar("Datavencimento", Hibernate.STRING);
				sqlQuery.addScalar("Situacaofatura", Hibernate.STRING);
				sqlQuery.addScalar("Totalfatura", Hibernate.STRING);
				sqlQuery.addScalar("Totaldescontos", Hibernate.STRING);
				sqlQuery.addScalar("Numboleto", Hibernate.STRING);
			}
		};
	}

	private ConfigureSqlQuery configureSqlQueryFaturasVencidasEAVencer() {
		return new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("Regional", Hibernate.STRING);
				sqlQuery.addScalar("Descricoreg", Hibernate.STRING);
				sqlQuery.addScalar("Filial", Hibernate.STRING);
				sqlQuery.addScalar("Numero", Hibernate.STRING);
				sqlQuery.addScalar("CNPJ", Hibernate.STRING);
				sqlQuery.addScalar("Razaosocial", Hibernate.STRING);
				sqlQuery.addScalar("Filialcobcliente", Hibernate.STRING);
				sqlQuery.addScalar("Tipopessoa", Hibernate.STRING);
				sqlQuery.addScalar("Tipocliente", Hibernate.STRING);
				sqlQuery.addScalar("Tipocobranca", Hibernate.STRING);
				sqlQuery.addScalar("Clientecomprefatura", Hibernate.STRING);
				sqlQuery.addScalar("Cobrancacentralizada", Hibernate.STRING);
				sqlQuery.addScalar("Classificacaocliente", Hibernate.STRING);
				sqlQuery.addScalar("Tipodeenviofaturamento", Hibernate.STRING);
				sqlQuery.addScalar("Tipodeenviocartacobranca", Hibernate.STRING);
				sqlQuery.addScalar("TipodeenvioSerasa", Hibernate.STRING);
				sqlQuery.addScalar("Tipodeenviocobrancaterceira", Hibernate.STRING);
				sqlQuery.addScalar("Emailfaturamento", Hibernate.STRING);
				sqlQuery.addScalar("Emailcobranca", Hibernate.STRING);
				sqlQuery.addScalar("Cedente", Hibernate.STRING);
				sqlQuery.addScalar("Modal", Hibernate.STRING);
				sqlQuery.addScalar("Servico", Hibernate.STRING);
				sqlQuery.addScalar("Divisao", Hibernate.STRING);
				sqlQuery.addScalar("Situacaofatura", Hibernate.STRING);
				sqlQuery.addScalar("Situacaoaprovacao", Hibernate.STRING);
				sqlQuery.addScalar("Tipodocumentodeservico", Hibernate.STRING);
				sqlQuery.addScalar("Tipoconhecimento", Hibernate.STRING);
				sqlQuery.addScalar("Tipofreteconhecimento", Hibernate.STRING);
				sqlQuery.addScalar("Tipocalculo", Hibernate.STRING);
				sqlQuery.addScalar("Servicoadicional", Hibernate.STRING);
				sqlQuery.addScalar("AWB", Hibernate.STRING);
				sqlQuery.addScalar("Faturaimpressa", Hibernate.STRING);
				sqlQuery.addScalar("Origemfatura", Hibernate.STRING);
				sqlQuery.addScalar("Dataemissao", Hibernate.STRING);
				sqlQuery.addScalar("Datavencimento", Hibernate.STRING);
				sqlQuery.addScalar("Dataliquidacao", Hibernate.STRING);
				sqlQuery.addScalar("DatanegSerasa", Hibernate.STRING);
				sqlQuery.addScalar("DataexclusaoSerasa", Hibernate.STRING);
				sqlQuery.addScalar("DataenvioCobTerceira", Hibernate.STRING);
				sqlQuery.addScalar("DatapagtoCobTerceira", Hibernate.STRING);
				sqlQuery.addScalar("DatadevolCobTerceira", Hibernate.STRING);
				sqlQuery.addScalar("Dataenviomensagem", Hibernate.STRING);
				sqlQuery.addScalar("Datarecebimentomensagem", Hibernate.STRING);
				sqlQuery.addScalar("Datadevolucaomensagem", Hibernate.STRING);
				sqlQuery.addScalar("Dataerromensagem", Hibernate.STRING);
				sqlQuery.addScalar("Dataenviomensagemcobranca", Hibernate.STRING);
				sqlQuery.addScalar("Dtrecmenscobranca", Hibernate.STRING);
				sqlQuery.addScalar("Datadevolucaomensagemcobranca", Hibernate.STRING);
				sqlQuery.addScalar("Dataerromensagemcobranca", Hibernate.STRING);
				sqlQuery.addScalar("Dataenviomensagemcobterceira", Hibernate.STRING);
				sqlQuery.addScalar("Dtrecmenscobterceira", Hibernate.STRING);
				sqlQuery.addScalar("Dtdevmenscobterceira", Hibernate.STRING);
				sqlQuery.addScalar("Dataerromensagemcobterceira", Hibernate.STRING);
				sqlQuery.addScalar("Dataprefatura", Hibernate.STRING);
				sqlQuery.addScalar("Dataimportacao", Hibernate.STRING);
				sqlQuery.addScalar("Dataenvioaceite", Hibernate.STRING);
				sqlQuery.addScalar("Dataretornoaceite", Hibernate.STRING);
				sqlQuery.addScalar("Qtdedocumentos", Hibernate.STRING);
				sqlQuery.addScalar("Totalfatura", Hibernate.STRING);
				sqlQuery.addScalar("Totaldescontos", Hibernate.STRING);
				sqlQuery.addScalar("Totaljuros", Hibernate.STRING);
				sqlQuery.addScalar("Valorrecebidoparcial", Hibernate.STRING);
				sqlQuery.addScalar("Valorsaldo", Hibernate.STRING);
				sqlQuery.addScalar("Ultimorecebparcial", Hibernate.STRING);
				sqlQuery.addScalar("Filialdebitada", Hibernate.STRING);
				sqlQuery.addScalar("Setorcausadordoabatimento", Hibernate.STRING);
				sqlQuery.addScalar("Motivododesconto", Hibernate.STRING);
				sqlQuery.addScalar("Acaocorretiva", Hibernate.STRING);
				sqlQuery.addScalar("Observacoes", Hibernate.STRING);
				sqlQuery.addScalar("Datatratativa", Hibernate.STRING);
				sqlQuery.addScalar("Usuariotratativa", Hibernate.STRING);
				sqlQuery.addScalar("Numboleto", Hibernate.STRING);
				sqlQuery.addScalar("Situacaoboleto", Hibernate.STRING);
				sqlQuery.addScalar("Dataemissaoboleto", Hibernate.STRING);
				sqlQuery.addScalar("Datavctoboleto", Hibernate.STRING);
				sqlQuery.addScalar("Datavctoaprovacao", Hibernate.STRING);
				sqlQuery.addScalar("Valortotalboleto", Hibernate.STRING);
				sqlQuery.addScalar("Valordescontoboleto", Hibernate.STRING);
				sqlQuery.addScalar("Valorjurodiario", Hibernate.STRING);
				sqlQuery.addScalar("Filialredeco", Hibernate.STRING);
				sqlQuery.addScalar("Numeroredeco", Hibernate.STRING);
				sqlQuery.addScalar("Situacaoredeco", Hibernate.STRING);
				sqlQuery.addScalar("Finalidaderedeco", Hibernate.STRING);
				sqlQuery.addScalar("Tiporecebimento", Hibernate.STRING);
				sqlQuery.addScalar("Dataemissaoredeco", Hibernate.STRING);
				sqlQuery.addScalar("Datarecebimentoredeco", Hibernate.STRING);
				sqlQuery.addScalar("Cobrador", Hibernate.STRING);
			}
		};
	}

	public List findFaturas(TypedFlatMap tfm) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(ALIAS_FAT);

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addInnerJoin(HQL_FETCH_FAT_FILIAL_BY_ID_FILIAL, ALIAS_FIL);
		hql.addInnerJoin(HQL_FETCH_FIL_PESSOA, ALIAS_PES);

		hql.addCriteria(HQL_FAT_NR_FATURA, "=", tfm.getLong(KEY_NR_FATURA));
		hql.addCriteria(HQL_FIL_ID, "=", tfm.getLong(KEY_ID_FILIAL));
		hql.addCriteria(HQL_FAT_DT_VENCIMENTO, "<=", tfm.getYearMonthDay(KEY_DT_VENCIMENTO));
		hql.addCriteria(HQL_FAT_CLIENTE_ID, "=", tfm.getLong(KEY_CLIENTE_ID_CLIENTE));
		hql.addCriteria(HQL_FAT_TP_ABRANGENCIA, "=", tfm.getString(KEY_TP_ABRANGENCIA));

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public List<Long> findIdsFaturasByCriteria(TypedFlatMap tfm) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("fat.id");

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addInnerJoin(HQL_FAT_FILIAL_BY_ID_FILIAL, ALIAS_FIL);
		hql.addInnerJoin(HQL_FIL_PESSOA, ALIAS_PES);

		hql.addCriteria(HQL_FAT_NR_FATURA, ">=", tfm.getLong(KEY_FATURA_INICIAL));
		hql.addCriteria(HQL_FAT_NR_FATURA, "<=", tfm.getLong(KEY_FATURA_FINAL));
		hql.addCriteria(HQL_FIL_ID, "=", tfm.getLong(KEY_FILIAL_ID_FILIAL));
		hql.addCriteria(HQL_FAT_TP_ABRANGENCIA, "=", tfm.getString(KEY_ABRANGENCIA));
		hql.addCriteria("fat.tpModal", "=", tfm.getString(KEY_MODAL));

		hql.addCustomCriteria("fat.tpSituacaoFatura not in ('CA','IN')");
		hql.addCustomCriteria("(fat.tpSituacaoAprovacao = 'A' or fat.tpSituacaoAprovacao is null )");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna a fatura com o número de fatura e a filial informado.
	 *
	 * @param nrFatura Long
	 * @param idFilial Long
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 31/01/2006
	 */
	public List findByNrFaturaByFilial(Long nrFatura, Long idFilial) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(ALIAS_FAT);

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addInnerJoin(HQL_FETCH_FAT_FILIAL_BY_ID_FILIAL, ALIAS_FIL);
		hql.addLeftOuterJoin("fetch fat.cedente", "ced");
		hql.addInnerJoin(HQL_FETCH_FIL_PESSOA, ALIAS_PES);

		hql.addCriteria("fat.tpFatura", "=", "R");
		hql.addCriteria(HQL_FIL_ID, "=", idFilial);
		if (nrFatura == null) {
			hql.addCriteria(HQL_FAT_NR_FATURA, "=", 0l);
		} else {
			hql.addCriteria(HQL_FAT_NR_FATURA, "=", nrFatura);
		}

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria(), "+INDEX(FAT FATU_IDX_02)");
	}

	/**
	 * Find de lookup que retorna dados (array) baseado no nrFatura e idFilial
	 *
	 * @param nrFatura Long
	 * @param idFilial Long
	 * @return List
	 * @author Regis de Souza Novais
	 */
	public List findByNrFaturaIdFilialOrigem(Long nrFatura, Long idFilial) {
		ProjectionList pl = getProjectionListFatura();
		DetachedCriteria dc = getDetachedCriteriaFatura(nrFatura, idFilial);
		dc.setProjection(pl).setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	public List findByNrFaturaIdFilialOrigemTpSituacaoFatura(Long nrFatura, Long idFilial, String tpSituacaoFatura) {
		ProjectionList pl = getProjectionListFatura();
		DetachedCriteria dc = getDetachedCriteriaFaturaTpSituacaoFatura(nrFatura, idFilial, tpSituacaoFatura);
		dc.setProjection(pl).setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	private ProjectionList getProjectionListFatura() {
		return Projections.projectionList()
				.add(Projections.property("fat.idFatura"), "idFatura")
				.add(Projections.property(HQL_FAT_NR_FATURA), KEY_NR_FATURA)
				.add(Projections.property("fat.tpSituacaoFatura"), "tpSituacaoFatura")
				.add(Projections.property("c.idCliente"), "idCliente")
				.add(Projections.property("c.idCliente"), "remetente_idCliente")
				.add(Projections.property("c.tpCliente"), "remetente_tpCliente")
				.add(Projections.property("c.idCliente"), "destinatario_idCliente")
				.add(Projections.property("c.tpCliente"), "destinatario_tpCliente")
				.add(Projections.property("p.idPessoa"), "remetente_idPessoa")
				.add(Projections.property("p.tpPessoa"), "remetente_tpPessoa")
				.add(Projections.property("p.idPessoa"), "destinatario_idPessoa")
				.add(Projections.property("p.tpPessoa"), "destinatario_tpPessoa")
				.add(Projections.property("p.nmPessoa"), "remetente_nmPessoa")
				.add(Projections.property("p.nmPessoa"), "destinatario_nmPessoa")
				.add(Projections.property("p.nrIdentificacao"), "remetente_nrIdentificacao")
				.add(Projections.property("p.nrIdentificacao"), "destinatario_nrIdentificacao")
				//*** Endereco
				.add(Projections.property("ep.dsEndereco"), "endereco_dsEndereco")
				.add(Projections.property("ep.nrEndereco"), "endereco_nrEndereco")
				.add(Projections.property("ep.nrCep"), "endereco_nrCep")
				.add(Projections.property("tipoLogradouro.dsTipoLogradouro"), "endereco_dsTipoLogradouro")
				.add(Projections.property("m.id"), "endereco_idMunicipio")
				.add(Projections.property("m.nmMunicipio"), "endereco_nmMunicipio")
				.add(Projections.property("uf.id"), "endereco_idUnidadeFederativa")
				.add(Projections.property("uf.sgUnidadeFederativa"), "endereco_sgUnidadeFederativa")
				.add(Projections.property("uf.nmUnidadeFederativa"), "endereco_nmUnidadeFederativa");
	}

	private DetachedCriteria getDetachedCriteriaFaturaTpSituacaoFatura(Long nrFatura, Long idFilial, String tpSituacaoFatura) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), ALIAS_FAT);
		dc.createAlias(HQL_FAT_FILIAL_BY_ID_FILIAL, "filial");
		dc.createAlias("fat.cliente", "c");
		dc.createAlias("c.pessoa", "p");
		dc.createAlias("p.enderecoPessoa", "ep");
		dc.createAlias("ep.tipoLogradouro", "tipoLogradouro");
		dc.createAlias("ep.municipio", "m");
		dc.createAlias("m.unidadeFederativa", "uf");
		dc.add(Restrictions.eq(HQL_FAT_NR_FATURA, nrFatura));
		dc.add(Restrictions.ne("fat.tpSituacaoFatura", tpSituacaoFatura));
		dc.add(Restrictions.eq(KEY_FILIAL_ID_FILIAL, idFilial));
		return dc;
	}

	private DetachedCriteria getDetachedCriteriaFatura(Long nrFatura, Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), ALIAS_FAT);
		dc.createAlias(HQL_FAT_FILIAL_BY_ID_FILIAL, "filial");
		dc.createAlias("fat.cliente", "c");
		dc.createAlias("c.pessoa", "p");
		dc.createAlias("p.enderecoPessoa", "ep");
		dc.createAlias("ep.tipoLogradouro", "tipoLogradouro");
		dc.createAlias("ep.municipio", "m");
		dc.createAlias("m.unidadeFederativa", "uf");
		dc.add(Restrictions.eq(HQL_FAT_NR_FATURA, nrFatura));
		dc.add(Restrictions.eq(KEY_FILIAL_ID_FILIAL, idFilial));
		return dc;
	}

	/**
	 * Retorna uma lista de fatura onde tem o nrPreFatura informado
	 *
	 * @param nrPreFatura Long
	 * @author Mickaël Jalbert
	 * @since 12/05/2006
	 */
	public List findByNrPreFatura(Long nrPreFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(ALIAS_FAT);

		hql.addInnerJoin(PreFatura.class.getName(), "prefat");
		hql.addInnerJoin("prefat.fatura", ALIAS_FAT);

		hql.addCriteria("fat.nrPreFatura", "=", nrPreFatura.toString());
		hql.addCriteria("fat.tpSituacaoFatura", "<>", "CA");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public List findDocumentosServico(Long idFatura) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("itemFaturas", "if");
		dc.add(Restrictions.eq("idFatura", idFatura));
		dc.setProjection(Projections.property("if.doctoServico.idDoctoServico"));

		return findByDetachedCriteria(dc);
	}

	/**
	 * Retorna a fatura ativa por Devedor_Doc_Serv_Fat
	 *
	 * @param idDevedorDocServFat
	 * @return List
	 */
	public List findByDevedorDocServFat(Long idDevedorDocServFat) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(ALIAS_FAT);
		sql.addInnerJoin(ItemFatura.class.getName(), "item");
		sql.addInnerJoin("item.devedorDocServFat", "dev");
		sql.addInnerJoin("item.fatura", ALIAS_FAT);
		sql.addLeftOuterJoin("fetch fat.cedente", "ced");
		sql.addInnerJoin("fetch fat.cliente", "cli");
		sql.addInnerJoin("fetch cli.pessoa", ALIAS_PES);
		sql.addInnerJoin(HQL_FETCH_FAT_FILIAL_BY_ID_FILIAL, ALIAS_FIL);
		sql.addInnerJoin("fetch fat.filialByIdFilial.pessoa", ALIAS_PES);
		sql.addLeftOuterJoin("fetch fat.manifestoEntregaOrigem", "man");

		sql.addCriteria("fat.tpSituacaoFatura", "!=", "CA");
		sql.addCriteria("fat.tpSituacaoFatura", "!=", "IN");

		sql.addCriteria("dev.id", "=", idDevedorDocServFat);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna a lista de faturas ligado ao Recibo informado
	 *
	 * @param idRecibo
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 05/05/2006
	 */
	public List findByRecibo(Long idRecibo) {
		SqlTemplate sql = mountHqlFaturaRecibo(idRecibo);

		sql.addProjection(ALIAS_FAT);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}


	public List<Fatura> findByRecibos(List<Long> idRecibos) {
		if (idRecibos.isEmpty()) {
			return new ArrayList<Fatura>();
		}
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(FaturaRecibo.class.getName(), "fatRec");
		sql.addInnerJoin("fatRec.fatura", ALIAS_FAT);
		sql.addInnerJoin("fat.filialByIdFilialCobradora", "filcob");
		sql.addInnerJoin("fat.cliente", "cli");
		sql.addInnerJoin("cli.filialByIdFilialCobranca", ALIAS_FIL);

		sql.addCriteriaIn("fatRec.recibo.id", idRecibos);

		sql.addProjection(ALIAS_FAT);

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}


	/**
	 * Retorna a lista de id de faturas ligado ao Recibo informado
	 *
	 * @param idRecibo
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 23/08/2006
	 */
	public List findIdFaturaByRecibo(Long idRecibo) {
		SqlTemplate sql = mountHqlFaturaRecibo(idRecibo);

		sql.addProjection("fat.id");

		return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	private SqlTemplate mountHqlFaturaRecibo(Long idRecibo) {
		SqlTemplate sql = new SqlTemplate();

		sql.addInnerJoin(FaturaRecibo.class.getName(), "fatRec");
		sql.addInnerJoin("fatRec.fatura", ALIAS_FAT);
		sql.addInnerJoin("fat.filialByIdFilialCobradora", "filcob");
		sql.addInnerJoin("fat.cliente", "cli");
		sql.addInnerJoin("cli.filialByIdFilialCobranca", ALIAS_FIL);

		sql.addCriteria("fatRec.recibo.id", "=", idRecibo);
		return sql;
	}

	/**
	 * Método responsável por carregar dados pï¿½ginados de acordo com os filtros passados
	 *
	 * @param criteria
	 * @return ResultSetPage contendo o resultado do hql.
	 */
	public List findPaginatedByFatura(TypedFlatMap criteria) {

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);

		Long idCobrancaInadimplencia = criteria.getLong("idCobrancaInadimplencia");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(
				new StringBuilder()
						.append("new Map( ic.idItemCobranca as idItemCobranca, ")
						.append(" f.nrFatura as nrFatura, ")
						.append(" fil.sgFilial as sgFilial, ")
						.append(" f.vlTotal as vlTotal, ")
						.append(" f.dtVencimento as dtVencimento, ")
						.append(" f.vlJuroCalculado as vlJuroCalculado ) ")
						.toString()
		);

		sql.addFrom(ItemCobranca.class.getName(), "ic JOIN ic.fatura as f " +
				"JOIN ic.cobrancaInadimplencia as ci " +
				"JOIN f.filialByIdFilial as fil "
		);

		if (idCobrancaInadimplencia != null && StringUtils.isNotBlank(idCobrancaInadimplencia.toString())) {
			sql.addCriteria("ci.idCobrancaInadimplencia", "=", idCobrancaInadimplencia);
		}

		sql.addOrderBy("f.nrFatura");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria()).getList();
	}

	/**
	 * Método responsável por fazer a contagem dos registros que retornam do hql.
	 *
	 * @param criteria
	 * @return Integer contendo o número de registros retornados.
	 */
	public Integer getRowCountByFatura(TypedFlatMap criteria) {

		Long idCobrancaInadimplencia = criteria.getLong("idCobrancaInadimplencia");

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(f.idFatura)");

		sql.addFrom(ItemCobranca.class.getName(), "ic JOIN ic.fatura as f " +
				"JOIN ic.cobrancaInadimplencia as ci " +
				"JOIN f.filialByIdFilial as fil "
		);

		if (idCobrancaInadimplencia != null && StringUtils.isNotBlank(idCobrancaInadimplencia.toString())) {
			sql.addCriteria("ci.idCobrancaInadimplencia", "=", idCobrancaInadimplencia);
		}

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Método responsável por buscar dados referentes as faturas de uma cobrancï¿½a inadimplï¿½ncia
	 *
	 * @param criteria
	 * @return List
	 */
	public List findDadosFaturasByCobrancaInadimplencia(TypedFlatMap criteria) {

		Long idCobrancaInadimplencia = criteria.getLong("cobrancaInadimplencia.idCobrancaInadimplencia");

		StringBuilder strBuf = new StringBuilder()
				.append("SELECT new Map( COUNT(ci.idCobrancaInadimplencia) AS nrFaturas, SUM(f.vlTotal) AS somaFaturas, SUM(f.vlJuroCalculado) AS somaJurosFaturas ) ")
				.append("FROM ItemCobranca AS ic JOIN ic.fatura AS f ")
				.append("JOIN ic.cobrancaInadimplencia AS ci ")
				.append("JOIN f.filialByIdFilial AS fil ")
				.append("WHERE ci.idCobrancaInadimplencia = " + idCobrancaInadimplencia + " ")
				.append("GROUP BY ci.idCobrancaInadimplencia ");

		return getAdsmHibernateTemplate().find(strBuf.toString());

	}

	/**
	 * Carrega uma fatura de acordo com idItemCobranca
	 *
	 * @param idItemCobranca
	 * @return Map
	 */
	public Map findFaturaByIdItemCobranca(Long idItemCobranca) {

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(
				new StringBuilder()
						.append("new map( f.idFatura as fatura_idFatura, ")
						.append("f.nrFatura as fatura_nrFatura, ")
						.append("fil.idFilial as filial_idFilial, ")
						.append("fil.sgFilial as filial_sgFilial, ")
						.append("p.nmPessoa as filial_pessoa_nmPessoa )")
						.toString()
		);

		sql.addFrom(
				new StringBuilder()
						.append(ItemCobranca.class.getName() + " as ic JOIN ic.fatura as f ")
						.append("JOIN f.filialByIdFilial as fil ")
						.append("JOIN fil.pessoa as p")
						.toString()
		);

		sql.addCriteria("ic.idItemCobranca", "=", idItemCobranca);

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);

		return (Map) list.get(0);
	}

	/**
	 * Método responsável por trazer uma fatura com a filial carregada de acordo com o id da fatura
	 *
	 * @param idFatura
	 * @return Fatura
	 */
	public Fatura findByIdFatura(Long idFatura) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" f ");

		sql.addFrom(
				new StringBuilder()
						.append(Fatura.class.getName() + " as f JOIN FETCH f.filialByIdFilial as fil ")
						.append("JOIN FETCH f.moeda as m ")
						.append("JOIN FETCH fil.pessoa as p ")
						.append("JOIN FETCH f.cliente as c ")
						.append("JOIN FETCH c.pessoa as pes ")
						.toString()
		);

		sql.addCriteria("f.idFatura", "=", idFatura);

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		if (list != null && !list.isEmpty()) {
			return (Fatura) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna a fatura com filial, filial cobradora, cliente, moeda.
	 *
	 * @param idFatura
	 * @return fatura
	 * @author Mickaël Jalbert
	 * @since 17/01/2007
	 */
	public Fatura findByIdTelaNotaDebitoNacional(Long idFatura) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" f ");

		sql.addFrom(
				new StringBuilder()
						.append(Fatura.class.getName() + " as f JOIN FETCH f.filialByIdFilial as fil ")
						.append("JOIN FETCH f.moeda as m ")
						.append("JOIN FETCH fil.pessoa as p ")
						.append("JOIN FETCH f.filialByIdFilialCobradora as filc ")
						.append("JOIN FETCH filc.pessoa as pc ")
						.append("JOIN FETCH f.cliente as c ")
						.append("JOIN FETCH c.pessoa as cp ")
						.append("LEFT OUTER JOIN FETCH f.notaDebitoNacional as nota ")
						.toString()
		);

		sql.addCriteria("f.idFatura", "=", idFatura);

		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		if (list != null && !list.isEmpty()) {
			return (Fatura) list.get(0);
		} else {
			return null;
		}
	}


	@Override
	public void removeById(Long id) {
		super.removeById(id, true);
	}

	@Override
	public int removeByIds(List ids) {
		return super.removeByIds(ids, true);
	}

	/**
	 * FindPaginated especial para a tela de Relaï¿½ï¿½o de documentos com deposito
	 *
	 * @param param
	 * @param findDef
	 * @return ResultSetPage
	 * @author Mickaël Jalbert
	 * @since 29/03/2006
	 */
	public ResultSetPage findPaginatedRelacaoDeposito(RelacaoFaturaDepositoParam param, FindDefinition findDef) {
		SqlTemplate hql = mountHqlFindPaginatedFatura(param);

		hql.addProjection(ALIAS_FAT);
		hql.addOrderBy("fil.sgFilial");
		hql.addOrderBy(HQL_FAT_NR_FATURA);

		return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
	}

	/**
	 * GetRowCount especial para a tela de Relaï¿½ï¿½o de documentos com deposito
	 *
	 * @param param
	 * @return número de linhas
	 * @author Mickaël Jalbert
	 * @since 29/03/2006
	 */
	public Integer getRowCountRelacaoDeposito(RelacaoFaturaDepositoParam param) {
		SqlTemplate hql = mountHqlFindPaginatedFatura(param);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
	}

	/**
	 * @param param
	 * @return sqlTemplate
	 * @author Mickaël Jalbert
	 * @since 26/01/2007
	 */
	private SqlTemplate mountHqlFindPaginatedFatura(RelacaoFaturaDepositoParam param) {
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addInnerJoin("fetch fat.moeda", "moe");
		hql.addInnerJoin(HQL_FETCH_FAT_FILIAL_BY_ID_FILIAL, ALIAS_FIL);
		hql.addLeftOuterJoin("fetch fat.redeco", "red");

		if (param.getLstFaturas() != null && !param.getLstFaturas().isEmpty()) {
			//Filtrar para não devolver as faturas que jï¿½ foram informadas
			hql.addCriteriaNotIn("fat.id", param.getLstFaturas());
		}

		hql.addCriteria(HQL_FAT_CLIENTE_ID, "=", param.getIdCliente());
		hql.addCriteria("fat.dtEmissao", ">=", param.getDtEmissaoInicial());
		hql.addCriteria("fat.dtEmissao", "<=", param.getDtEmissaoFinal());
		hql.addCriteria(HQL_FAT_NR_FATURA, ">=", param.getNrFaturaInicial());
		hql.addCriteria(HQL_FAT_NR_FATURA, "<=", param.getNrFaturaFinal());
		hql.addCustomCriteria("(fat.tpSituacaoFatura IN ('DI', 'EM', 'BL', 'RC') OR (fat.tpSituacaoFatura = 'RE' AND red.tpFinalidade = 'CF'))");

		hql.addCustomCriteria("NOT EXISTS ( " +
				" SELECT 	idcctmp " +
				" FROM 		" + ItemDepositoCcorrente.class.getName() + " idcctmp " +
				" WHERE 	idcctmp.fatura.id = fat.id " +
				" ) ");

		return hql;
	}

	/**
	 * Retorna o valor iva da fatura quando a filial de destino
	 * do documento de servirão ï¿½ igual a filial de faturamento. Calculado a partir dos
	 * valores de frete externo dos ctos internacionais e dos porcentagens de
	 * aliquota vigentes dos paises de destino dos documentos da fatura informada.
	 *
	 * @param idFatura            Long
	 * @param idFilialFaturamento Long
	 * @param dtVigencia          YearMonthDay
	 * @return BigDecimal
	 * @author Mickaël Jalbert
	 * @since 04/07/2006
	 */
	public BigDecimal findVlIvaByFatura(Long idFatura, Long idFilialFaturamento, YearMonthDay dtVigencia) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("sum(cto.vl_frete_externo * ali.pc_aliquota / 100)", "vlAliquota");

		sql.addFrom("item_fatura", "ifa");
		sql.addFrom("devedor_doc_serv_fat", "dev");
		sql.addFrom("docto_servico", "doc");
		sql.addFrom("cto_internacional", "cto");
		sql.addFrom("pessoa", ALIAS_PES);
		sql.addFrom("endereco_pessoa", "enp");
		sql.addFrom("municipio", "mun");
		sql.addFrom("unidade_federativa", "uf");
		sql.addFrom("aliquota_iva", "ali");

		sql.addJoin("ifa.id_devedor_doc_serv_fat", "dev.id_devedor_doc_serv_fat");
		sql.addJoin("dev.id_docto_servico", "doc.id_docto_servico");
		sql.addJoin("doc.id_docto_servico", "cto.id_cto_internacional");
		sql.addJoin("doc.id_filial_destino", "pes.id_pessoa");
		sql.addJoin("pes.id_endereco_pessoa", "enp.id_endereco_pessoa");
		sql.addJoin("enp.id_municipio", "mun.id_municipio");
		sql.addJoin("mun.id_unidade_federativa", "uf.id_unidade_federativa");
		sql.addJoin("uf.id_pais", "ali.id_pais");

		sql.addCustomCriteria("(ali.dt_vigencia_inicial <= ? and ali.dt_vigencia_final >= ? )");
		sql.addCriteriaValue(dtVigencia);
		sql.addCriteriaValue(dtVigencia);
		sql.addCriteria("ifa.id_fatura", "=", idFatura);
		sql.addCriteria("doc.id_filial_destino", "=", idFilialFaturamento);

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("vlAliquota", Hibernate.BIG_DECIMAL);
			}
		};

		Object[] arraySomatorio = (Object[]) getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), csq);

		if (arraySomatorio != null) {
			return (BigDecimal) arraySomatorio[0];
		} else {
			return new BigDecimal(0);
		}
	}

	/**
	 * Atualiza a situação das faturas para 'emitido', 'em recibo' ou 'em boleto'
	 * do redeco informado
	 *
	 * @param idRedeco Long
	 * @author Mickaël Jalbert
	 * @since 06/07/2006
	 */
	public void cancelFaturasByIdRedeco(final Long idRedeco) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List lstIdFatura = null;

				lstIdFatura = findIdFaturaByIdRedeco(idRedeco);
				//Atualiza TODAS as fatura para 'emitido'
				updateList(session, lstIdFatura, "EM");

				lstIdFatura = findIdFaturaEmBoletoByIdRedeco(idRedeco);
				//Atualiza as fatura para 'em boleto' são aquelas que tem boleto ativo
				updateList(session, lstIdFatura, "BL");

				lstIdFatura = findIdFaturaEmReciboByIdRedeco(idRedeco);
				//Atualiza as fatura para 'em recibo' são aquelas que tem recibo ativo
				updateList(session, lstIdFatura, "RC");

				//REGRA, alterar a situação da fatura para 'emitido', se a fatura tiver um recibo
				//ativo, alterar para 'em recibo' e se a fatura tiver boleto, alterar para 'em boleto'

				//NOTA, o primeiro faz update em cima de todas as faturas e o segundo e terceiro
				//os update sãoo restrengido e você fazer um segundo e talvez um terceiro update em
				//cima do mesmo registro -> não pode mexer na ordem!!!!

				return null;
			}

			private void updateList(Session session, List lstIdFatura, String tpSituacaoFatura) {
				int i = UPDATE_SIZE;
				for (; i < lstIdFatura.size(); i += UPDATE_SIZE) {
					List sublist = lstIdFatura.subList(i - UPDATE_SIZE, i);
					updateFaturas(session, sublist, tpSituacaoFatura);
				}
				if (!lstIdFatura.isEmpty()) {
					i -= UPDATE_SIZE;
					List sublist = lstIdFatura.subList(i, i + (lstIdFatura.size() % UPDATE_SIZE));
					updateFaturas(session, sublist, tpSituacaoFatura);
				}
			}

			private void updateFaturas(Session session, List lstIdFatura, String tpSituacaoFatura) {
				Query query = session.createQuery(CANCELFATURASBYIDREDECO);
				query.setParameterList("idsFatura", lstIdFatura);
				query.setParameter("tpSituacaoFatura", tpSituacaoFatura);
				query.executeUpdate();
			}

		});
	}

	/**
	 * Retorna a lista de ids de faturado redeco informado
	 *
	 * @param idRedeco Long
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 07/07/2006
	 */
	public List findIdFaturaByIdRedeco(Long idRedeco) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("itr.fatura.id");

		hql.addInnerJoin(ItemRedeco.class.getName(), "itr");

		hql.addCriteria("itr.redeco.id", "=", idRedeco);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public List findFaturasByIdRedeco(Long idRedeco) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("itr.fatura");

		hql.addInnerJoin(ItemRedeco.class.getName(), "itr");

		hql.addCriteria("itr.redeco.id", "=", idRedeco);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna a lista de ids de faturado redeco informado que tem recibo ativo.
	 *
	 * @param idRedeco Long
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 07/07/2006
	 */
	public List findIdFaturaEmReciboByIdRedeco(Long idRedeco) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("f.id");

		hql.addInnerJoin(ItemRedeco.class.getName(), "itr");
		hql.addInnerJoin("itr.fatura", "f");
		hql.addInnerJoin("f.faturaRecibos", "fr");
		hql.addInnerJoin("fr.recibo", "r");

		hql.addCriteria("itr.redeco.id", "=", idRedeco);
		hql.addCriteria("r.tpSituacaoRecibo", "!=", "C");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna a lista de ids de faturado redeco informado que tem boleto ativo
	 *
	 * @param idRedeco Long
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 07/07/2006
	 */
	public List findIdFaturaEmBoletoByIdRedeco(Long idRedeco) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("f.id");

		hql.addInnerJoin(ItemRedeco.class.getName(), "itr");
		hql.addInnerJoin("itr.fatura", "f");
		hql.addInnerJoin("f.boletos", "b");

		hql.addCriteria("itr.redeco.id", "=", idRedeco);
		hql.addCriteria("b.tpSituacaoBoleto", "!=", "CA");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retornar true se a fatura tem boleto ativo
	 *
	 * @param idFatura Long
	 * @return Boolean
	 * @author Mickaël Jalbert
	 * @since 12/07/2006
	 */
	public Boolean validateFaturaEmBoleto(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("count(bol.id)");

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addInnerJoin("fat.boletos", "bol");
		hql.addCriteria("fat.id", "=", idFatura);

		hql.addCriteria("bol.tpSituacaoBoleto", "!=", "CA");

		List lstCount = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (lstCount != null && !lstCount.isEmpty()) {
			Long numReg = (Long) lstCount.get(0);

			if (numReg.compareTo(0L) > 0) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * Retornar true se a fatura tem recibo ativo
	 *
	 * @param idFatura Long
	 * @return Boolean
	 * @author Mickaël Jalbert
	 * @since 12/07/2006
	 */
	public Boolean validateFaturaEmRecibo(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("count(rec.id)");

		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addInnerJoin("fat.faturaRecibos", "fatRec");
		hql.addInnerJoin("fatRec.recibo", "rec");
		hql.addCriteria("fat.id", "=", idFatura);

		hql.addCriteria("rec.tpSituacaoRecibo", "!=", "C");

		List lstCount = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (lstCount != null && !lstCount.isEmpty()) {
			Integer numReg = (Integer) lstCount.get(0);

			if (numReg.compareTo(0) > 0) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * Método responsável por buscar faturas de acordo com idDoctoServico
	 *
	 * @param idDoctoServico   Long
	 * @param tpSituacaoFatura List
	 * @return List <Fatura>
	 * @author HectorJ
	 * @since 29/06/2006
	 */
	public List findFaturasByDoctoServico(Long idDoctoServico, List tpSituacaoFatura) {

		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("f");

		hql.addFrom(DevedorDocServFat.class.getName(), " ddsf " +
				" INNER JOIN ddsf.itemFaturas if " +
				" INNER JOIN if.fatura f " +
				" INNER JOIN FETCH f.filialByIdFilial fil " +
				" LEFT OUTER JOIN FETCH f.boletos b " +
				" LEFT OUTER JOIN FETCH f.itemRedecos ir " +
				" LEFT OUTER JOIN FETCH ir.redeco r ");

		hql.addCriteria("ddsf.doctoServico.id", "=", idDoctoServico);

		if (tpSituacaoFatura != null) {
			hql.addCriteriaNotIn("f.tpSituacaoFatura", new Object[]{tpSituacaoFatura});
		}

		hql.addCustomCriteria("(b.tpSituacaoBoleto <> 'CA' or b.tpSituacaoBoleto is null)");
		hql.addCustomCriteria("(r.tpSituacaoRedeco <> 'CA' or r.tpSituacaoRedeco is null)");
		return getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	public void updateIndicadorImpressao(Long idFatura, Boolean indicadorImpressao) {
		List<Object> param = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(UPDATE);
		hql.append(getPersistentClass().getName());
		hql.append(" f ");
		hql.append("SET ");
		hql.append("f.blIndicadorImpressao = ? ");
		param.add(indicadorImpressao);
		hql.append("WHERE f.idFatura = ? ");
		param.add(idFatura);

		executeHql(hql.toString(), param);
	}

	/**
	 * Atualiza a situação da fatura para 'Emitido'
	 *
	 * @param idFatura Long
	 * @author Mickaël Jalbert
	 * @since 22/08/2006
	 */
	public void updateSituacaoFatura(Long idFatura, int tpSituacaoAnterior) {
		StringBuilder update = new StringBuilder();

		update.append(UPDATE).append(Fatura.class.getName()).append(" ");
		update.append("SET tpSituacaoFatura = 'EM' \n ");

		switch (tpSituacaoAnterior) {
			case 1:
				update.append(", redeco = null \n ");
				break;
			case 2:
				update.append(", redeco = null \n ");
				update.append(", recibo = null \n ");
				break;
			default:
				break;
		}

		update.append(WHERE_ID).append(idFatura);

		executeUpdate(update.toString());
	}

	/**
	 * Atualiza a situação da fatura para 'Em boleto' se a fatura está dentro de um boleto ativo
	 *
	 * @param idFatura Long
	 * @author Mickaël Jalbert
	 * @since 22/08/2006
	 */
	public void updateSituacaoFaturaBoleto(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("1");
		hql.addInnerJoin(Boleto.class.getName(), "bol");
		hql.addCustomCriteria("bol.tpSituacaoBoleto != 'CA'");
		hql.addCustomCriteria("bol.fatura.id = " + idFatura);


		String update = UPDATE + Fatura.class.getName() + "\n " +
				"SET tpSituacaoFatura = 'BL' \n " +
				WHERE_ID + idFatura + " \n" +
				"AND EXISTS (" + hql.getSql() + ")";

		executeUpdate(update);
	}

	/**
	 * Atualiza a situação da fatura para 'Em recibo' se a fatura está dentro de um recibo ativo
	 *
	 * @param idFatura Long
	 * @author Mickaël Jalbert
	 * @since 22/08/2006
	 */
	public void updateSituacaoFaturaRecibo(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("1");
		hql.addInnerJoin(FaturaRecibo.class.getName(), "fatrec");
		hql.addCustomCriteria("fatrec.recibo.tpSituacaoRecibo != 'C'");
		hql.addCustomCriteria("fatrec.fatura.id = " + idFatura);


		String update = UPDATE + Fatura.class.getName() + "\n " +
				"SET tpSituacaoFatura = 'RC' \n " +
				WHERE_ID + idFatura + " \n" +
				"AND EXISTS (" + hql.getSql() + ")";

		executeUpdate(update);
	}

	/**
	 * Atualiza para nulo o manifesto de entrega e manifesto de entrega origem das faturas
	 * do manifesto informado. Como o hibernate ï¿½ muito limitado, eu tive que fazer assim.
	 *
	 * @param idManifesto Long
	 * @author Mickaël Jalbert
	 * @since 14/03/2007
	 */
	public void updateManifestoFatura(Long idManifesto) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("fat.id");
		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addCustomCriteria("fat.manifestoEntrega.id = " + idManifesto);

		String update = UPDATE + Fatura.class.getName() + "\n " +
				"SET manifestoEntrega = null \n " +
				"WHERE id in (" + hql.getSql() + ")";

		executeUpdate(update);

		hql = new SqlTemplate();

		hql.addProjection("fat.id");
		hql.addInnerJoin(Fatura.class.getName(), ALIAS_FAT);
		hql.addCustomCriteria("fat.manifestoEntregaOrigem.id = " + idManifesto);

		update = UPDATE + Fatura.class.getName() + "\n " +
				"SET manifestoEntregaOrigem = null \n " +
				"WHERE id in (" + hql.getSql() + ")";

		executeUpdate(update);
	}

	public void executeUpdate(final String hql) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (StringUtils.isNotBlank(hql)) {
					query.executeUpdate();
				}

				return null;
			}
		});
	}

	/**
	 * Retorna um map de dados por devedorDocServFat.
	 *
	 * @param idDevedorDocServFat Long
	 * @return Map
	 * @author Mickaël Jalbert
	 * @since 28/08/2006
	 */
	public Map findDadosEmitirDocumentosServicoPendenteCliente(Long idDevedorDocServFat) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(fat.idFatura as idFatura, fat.nrFatura as nrFatura, " +
				"fat.tpSituacaoFatura as tpSituacaoFatura, fat.dtEmissao as dtEmissao, " +
				"fat.dtVencimento as dtVencimento," +
				"fil.sgFilial as sgFilial, pas.idPais as idPais)");

		sql.addInnerJoin(ItemFatura.class.getName(), "item");
		sql.addInnerJoin("item.devedorDocServFat", "dev");
		sql.addInnerJoin("item.fatura", ALIAS_FAT);
		sql.addInnerJoin(HQL_FAT_FILIAL_BY_ID_FILIAL, ALIAS_FIL);
		sql.addInnerJoin(HQL_FIL_PESSOA, ALIAS_PES);
		sql.addInnerJoin("pes.enderecoPessoa", "ende");
		sql.addInnerJoin("ende.municipio", "mun");
		sql.addInnerJoin("mun.unidadeFederativa", "uf");
		sql.addInnerJoin("uf.pais", "pas");

		sql.addCriteria("fat.tpSituacaoFatura", "!=", "CA");
		sql.addCriteria("fat.tpSituacaoFatura", "!=", "IN");

		sql.addCriteria("dev.id", "=", idDevedorDocServFat);

		List lstFatura = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		if (!lstFatura.isEmpty()) {
			return (Map) lstFatura.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna o somatório dos valores totais dos documentos
	 * de servirão pertencentes aos itens da fatura
	 *
	 * @param idFatura Identificador da fatura
	 * @return Valor total dos documentos de servirão dos itens da fatura
	 * @author José Rodrigo Moraes
	 * @since 22/09/2006
	 */
	public BigDecimal findValorTotalDocumentosServico(Long idFatura) {

		BigDecimal valor = null;

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("sum(ddsf.vlDevido)");
		sql.addInnerJoin(ItemFatura.class.getName(), "if");
		sql.addInnerJoin("if.fatura", ALIAS_FAT);
		sql.addInnerJoin("if.devedorDocServFat", "ddsf");
		sql.addInnerJoin("ddsf.doctoServico", "ds");

		sql.addCriteria("fat.id", "=", idFatura);

		List retorno = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());

		if (retorno != null && !retorno.isEmpty()) {
			valor = (BigDecimal) retorno.get(0);
		}

		return valor;
	}

	/**
	 * Retorna um array com a situação da fatura e a situação do boleto se tem
	 *
	 * @param idFatura Id da fatura
	 * @return Object[]
	 * @author Mickaël Jalbert
	 * @since 20/11/2006
	 */
	public Object[] findSituacaoFaturaBoletoByFatura(Long idFatura) {
		SqlTemplate sql = new SqlTemplate();

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("tpSituacaoFatura", Hibernate.STRING);
				sqlQuery.addScalar("tpSituacaoBoleto", Hibernate.STRING);
				sqlQuery.addScalar(KEY_ID_FILIAL, Hibernate.LONG);
				sqlQuery.addScalar("blConhecimentoResumo", Hibernate.STRING);
			}
		};

		sql.addProjection("fat.tp_situacao_fatura", "tpSituacaoFatura");
		sql.addProjection("bol.tp_situacao_boleto", "tpSituacaoBoleto");
		sql.addProjection("fil.id_filial", KEY_ID_FILIAL);
		sql.addProjection("fat.BL_CONHECIMENTO_RESUMO", "blConhecimentoResumo");

		sql.addFrom("fatura", ALIAS_FAT);
		sql.addFrom("boleto", "bol");
		sql.addFrom("filial", ALIAS_FIL);

		sql.addJoin("fat.id_fatura", "bol.id_fatura(+)");
		sql.addJoin("fat.id_filial", "fil.id_filial");

		sql.addCustomCriteria("bol.tp_situacao_boleto(+) != 'CA'");

		sql.addCriteria("fat.id_fatura", "=", idFatura);

		return (Object[]) getAdsmHibernateTemplate().findByIdBySql(sql.getSql(), sql.getCriteria(), csq);
	}

	/**
	 * Retorna a situação da aprovação da fatura baseado nas duas pendencias da fatura.
	 *
	 * @param idFatura Long
	 * @return String
	 * @author Mickaël Jalbert
	 * @since 21/11/2006
	 */
	public String getSituacaoAprovacaoFatura(Long idFatura) {
		StringBuilder sql = new StringBuilder();

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("tpSituacaoPendencia", Hibernate.STRING);
			}
		};
		sql.append("SELECT tpSituacaoPendencia FROM (");
		sql.append(mountSqlSituacaoAprovacaoFatura(idFatura));
		sql.append(" AND fat.id_pendencia = pen.id_pendencia(+) \n");

		sql.append("UNION \n");

		sql.append(mountSqlSituacaoAprovacaoFatura(idFatura));
		sql.append(" AND fat.id_pendencia_desconto = pen.id_pendencia(+) \n");

		sql.append("ORDER BY prioridade");
		sql.append(") WHERE ROWNUM < 2");

		String tpSituacaoAprovacao = (String) getAdsmHibernateTemplate().findByIdBySql(sql.toString(), new Object[0], csq);

		if (tpSituacaoAprovacao != null && "C".equals(tpSituacaoAprovacao)) {
			tpSituacaoAprovacao = null;
		}

		return tpSituacaoAprovacao;
	}


	private String mountSqlSituacaoAprovacaoFatura(Long idFatura) {
		StringBuilder str = new StringBuilder();

		str.append("SELECT pen.tp_situacao_pendencia as tpSituacaoPendencia, \n");
		str.append("	DECODE(pen.tp_situacao_pendencia, 'E', 1, 2) AS prioridade \n");
		str.append("FROM  fatura fat, \n");
		str.append("	pendencia pen \n");
		str.append("WHERE fat.id_fatura = " + idFatura);

		return str.toString();
	}

	/**
	 * Método responsável por buscar faturas de acordo com os filtros
	 *
	 * @param flp     FaturaLookupParam
	 * @param findDef FindDefinition
	 * @return ResultSetPage
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 */
	public ResultSetPage findPaginated(FaturaLookupParam flp, FindDefinition findDef) {

		/* Monta o hql genérico */
		SqlTemplate hql = mountGenericHqlLookupFatura(flp);

		/* Projection */
		mountProjectionLookupFatura(hql);

		/* Order By */
		hql.addOrderBy("filialFaturamento.sgFilial, fatura.nrFatura");

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.getSql(true)
				, findDef.getCurrentPage()
				, findDef.getPageSize()
				, hql.getCriteria());

		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));

		return rsp;
	}

	/**
	 * Método responsável por busca a quantidade de faturas de acordo com os filtros
	 *
	 * @param flp FaturaLookupParam
	 * @return Quantidade de faturas
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 */
	public Integer getRowCount(FaturaLookupParam flp) {

		/* Monta o hql genérico */
		SqlTemplate hql = mountGenericHqlLookupFatura(flp);

		/* Projection */
		hql.addProjection(" COUNT(fatura.id) ");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());

		return result.intValue();
	}

	/**
	 * Método responsável por buscar uma lista de faturas de acordo com os filtros
	 *
	 * @param flp FaturaLookupParam
	 * @return Lista de faturas
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 */
	public List findLookupFatura(FaturaLookupParam flp) {

		/* Monta o hql genérico */
		SqlTemplate hql = mountGenericHqlLookupFatura(flp);

		/* Projection */
		mountProjectionLookupFatura(hql);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Monta a clausula From e Where do hql da lookup de Fatura
	 *
	 * @param flp FaturaLookupParam
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 */
	private SqlTemplate mountGenericHqlLookupFatura(FaturaLookupParam flp) {

		SqlTemplate hql = new SqlTemplate();

		/* FROM */
		mountFromFatura(hql);

		/* WHERE */
		mountCriteriaFatura(hql, flp);

		return hql;
	}

	/**
	 * Monta a clausula Where do hql
	 *
	 * @param hql SqlTemplate
	 * @param flp FaturaLookupParam
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 */
	private void mountCriteriaFatura(SqlTemplate hql, FaturaLookupParam flp) {

		/* WHERE */

		hql.addCriteria("fatura.cedente.idCedente", "=", flp.getIdBancoFatura());
		hql.addCriteria(KEY_CLIENTE_ID_CLIENTE, "=", flp.getIdClienteFatura());
		hql.addCriteria("filialCobranca.idFilial", "=", flp.getIdFilialCobrancaFatura());
		hql.addCriteria("filialFaturamento.idFilial", "=", flp.getIdFilialFaturamentoFatura());
		hql.addCriteria("filialCobranca.sgFilial", "like", flp.getSgFilialCobrancaFatura());
		hql.addCriteria("filialFaturamento.sgFilial", "like", flp.getSgFilialFaturamentoFatura());
		hql.addCriteria("fatura.nrFatura", "=", flp.getNrFatura());
		hql.addCriteria("fatura.nrPreFatura", "like", flp.getNrPreFatura());
		hql.addCriteria("fatura.tpAbrangencia", "=", flp.getTpAbrangemciaFatura());
		hql.addCriteria("fatura.tpModal", "=", flp.getTpModalFatura());
		hql.addCriteria("fatura.tpSituacaoFatura", "=", flp.getTpSituacaoFatura());
		hql.addCustomCriteria("not exists (select 1 from " + Pendencia.class.getName() + " as p inner join p.ocorrencia as o inner join o.eventoWorkflow as e inner join e.tipoEvento as te where p.id = fatura.pendencia.id and te.nrTipoEvento = " + ConstantesWorkflow.NR3611_INCL_PRE_FAT + " and p.tpSituacaoPendencia = 'E')");

		//Situacao de fatura possãoveis
		if (flp.getTpSituacaoFaturaValido() != null) {
			SQLUtils.joinExpressionsWithComma(flp.getTpSituacaoFaturaValidoList(), hql, "fatura.tpSituacaoFatura");
		}

		YearMonthDay dtLiquidacaoInicial = flp.getDtLiquidacaoInicialFatura();
		if (dtLiquidacaoInicial != null) {

			/* Filtra pela dtLiquidacao */
			hql.addCustomCriteria(" ( fatura.dtLiquidacao BETWEEN ? AND ? ) ");
			hql.addCriteriaValue(dtLiquidacaoInicial);
			hql.addCriteriaValue(JTDateTimeUtils.maxYmd(flp.getDtLiquidacaoFinalFatura()));

		}

		YearMonthDay dtEmissaoInicial = flp.getDtEmissaoInicialFatura();
		if (dtEmissaoInicial != null) {

			/* Filtra pela dtEmissao */
			hql.addCustomCriteria(" ( fatura.dtEmissao BETWEEN ? AND ? ) ");
			hql.addCriteriaValue(dtEmissaoInicial);
			hql.addCriteriaValue(JTDateTimeUtils.maxYmd(flp.getDtEmissaoFinalFatura()));

		}

		YearMonthDay dtVencimentoInicial = flp.getDtVencimentoInicialFatura();
		if (dtVencimentoInicial != null) {

			/* Filtra pela dtVencimento */
			hql.addCustomCriteria(" ( fatura.dtVencimento BETWEEN ? AND ? ) ");
			hql.addCriteriaValue(dtVencimentoInicial);
			hql.addCriteriaValue(JTDateTimeUtils.maxYmd(flp.getDtVencimentoFinalFatura()));

		}

	}

	/**
	 * Monta a clausula From do hql
	 *
	 * @param hql SqlTemplate
	 * @author Hector Julian Esnaola Junior
	 * @since 28/11/2006
	 */
	private void mountFromFatura(SqlTemplate hql) {

		/* FROM */

		hql.addInnerJoin(getPersistentClass().getName(), " fatura ");
		hql.addInnerJoin("fatura.filialByIdFilial", "filialFaturamento");
		hql.addInnerJoin("filialFaturamento.pessoa", "pessoaFilialFaturamento");
		hql.addInnerJoin("fatura.filialByIdFilialCobradora", "filialCobranca");
		hql.addInnerJoin("fatura.cliente", "cliente");
		hql.addInnerJoin("cliente.pessoa", "pessoaCliente");
		hql.addLeftOuterJoin("fatura.divisaoCliente", "divisaoCliente");
		hql.addLeftOuterJoin("fatura.moeda", "moeda");

	}

	/**
	 * @param hql SqlTemplate
	 * @author Hector Julian Esnaola Junior
	 * @since 29/11/2006
	 */
	private void mountProjectionLookupFatura(SqlTemplate hql) {

		hql.addProjection("new Map( fatura.idFatura", "idFatura");
		hql.addProjection("filialFaturamento.idFilial", "filialByIdFilial_idFilial");
		hql.addProjection("filialFaturamento.sgFilial", "filialByIdFilial_sgFilial");
		hql.addProjection("pessoaFilialFaturamento.nmFantasia", "filialByIdFilial_pessoa_nmFantasia");
		hql.addProjection("fatura.nrFatura", KEY_NR_FATURA);
		hql.addProjection("filialCobranca.sgFilial", "filialByIdFilialCobradora_sgFilial");
		hql.addProjection("pessoaCliente.tpIdentificacao", "cliente_pessoa_tpIdentificacao");
		hql.addProjection("pessoaCliente.nrIdentificacao", "cliente_pessoa_nrIdentificacao");
		hql.addProjection("pessoaCliente.nmPessoa", "cliente_pessoa_nmPessoa");
		hql.addProjection("fatura.dtEmissao", "dtEmissao");
		hql.addProjection("fatura.dtVencimento", KEY_DT_VENCIMENTO);
		hql.addProjection("fatura.dtLiquidacao", "dtLiquidacao");
		hql.addProjection("fatura.tpSituacaoFatura", "tpSituacaoFatura");
		hql.addProjection("moeda.sgMoeda || ' ' || moeda.dsSimbolo", "siglaSimbolo");
		hql.addProjection("moeda.sgMoeda || ' ' || moeda.dsSimbolo", "siglaSimboloDesconto");
		hql.addProjection("fatura.vlTotal", "vlTotal");
		hql.addProjection("fatura.vlDesconto", "vlDesconto");
		hql.addProjection("fatura.vlJuroCalculado", "vlJuroCalculado");
		hql.addProjection("(fatura.vlJuroCalculado - fatura.vlJuroRecebido)", "vlJuroCobrar");
		hql.addProjection("fatura.vlJuroRecebido", "vlJuroRecebido )");

	}

	/**
	 * Retorna a lista de DevedorDocServFat da fatura informada
	 *
	 * @param idFatura Long
	 * @return List
	 * @author Mickaël Jalbert
	 * @since 30/11/2006
	 */
	public List findNrDoctoServicoByIdFatura(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("ddsf");

		hql.addInnerJoin(DevedorDocServFat.class.getName(), "ddsf");
		hql.addInnerJoin("fetch ddsf.doctoServico", "doc");
		hql.addInnerJoin("fetch doc.filialByIdFilialOrigem", "f");
		hql.addInnerJoin("fetch ddsf.itemFaturas", "if");

		hql.addCriteria("if.fatura.id", "=", idFatura);

		hql.addOrderBy("doc.nrDoctoServico");

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Retorna a lista de número de documento de servirão da fatura informada
	 * e do tipo de documento diferente daquele informado
	 *
	 * @param idFatura Long
	 * @return Long
	 * @author Mickaël Jalbert
	 * @since 30/11/2006
	 */
	public Long findQtDocumentoServicoByFatura(Long idFatura, String strTpDocumentoServico) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("count(doc.id)");

		hql.addInnerJoin(DevedorDocServFat.class.getName(), "dev");
		hql.addInnerJoin("dev.doctoServico", "doc");
		hql.addInnerJoin("dev.itemFaturas", "if");

		hql.addCriteria("doc.tpDocumentoServico", "!=", strTpDocumentoServico);
		hql.addCriteria("if.fatura.id", "=", idFatura);

		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Busca a fatura de acordo com o número da fatura e o idFilialOrigem
	 *
	 * @param nrFatura Long
	 * @param idFilial Long
	 * @return Fatura
	 * @author Hector Julian Esnaola Junior
	 * @since 23/04/2007
	 */
	public Fatura findFaturaByNrFaturaAndIdFilial(Long nrFatura, Long idFilial) {

		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName() + " f ");

		hql.addProjection(" f ");

		hql.addCriteria("f.nrFatura", "=", nrFatura);
		hql.addCriteria("f.filialByIdFilial.id", "=", idFilial);

		return (Fatura) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Busca a fatura de acordo com o número do documento de servirão e a idFilialOrigem.
	 *
	 * @param nrDoctoServico Long
	 * @param idFilial       Long
	 * @return Fatura
	 * @author Hector Julian Esnaola Junior
	 * @since 10/10/2007
	 */
	public Fatura findFaturaByNrDoctoServicoAndIdFilial(Long nrDoctoServico, Long idFilial) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection(" f ");
		hql.addFrom(getPersistentClass().getName() + " f ");
		hql.addInnerJoin("f.itemFaturas.devedorDocServFat.doctoServico", "ds");
		hql.addCriteria("ds.nrDoctoServico", "=", nrDoctoServico);
		hql.addCriteria("f.filialByIdFilial.id", "=", idFilial);
		return (Fatura) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Carrega a fatura de acordo com o idFatura.
	 *
	 * @param idFatura Long
	 * @return Fatura
	 * @author Hector Julian Esnaola Junior
	 * @since 19/07/2007
	 */
	public Fatura findFaturaByIdFatura(Long idFatura) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection(" f ");

		hql.addInnerJoin(getPersistentClass().getName() + " f ");
		hql.addInnerJoin("f.itemFaturas", "if");
		hql.addInnerJoin("if.devedorDocServFat", "ddsf");
		hql.addLeftOuterJoin("f.boleto", "bol");
		hql.addLeftOuterJoin("bol.historicoBoletos", "hb");
		hql.addLeftOuterJoin("hb.ocorrenciaBanco", "ob");
		hql.addLeftOuterJoin("ddsf.descontos", "d");
		hql.addLeftOuterJoin("d.reciboDesconto", "rd");

		hql.addLeftOuterJoin("d.demonstrativoDesconto", "dd");

		hql.addCriteria("f.idFatura", "=", idFatura);

		return (Fatura) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

	public boolean findExistFaturasParaPgto(Long idFatura) {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) as tot from (");
		sql.append("select f.id_fatura ");
		sql.append("   from fatura f , ");
		sql.append("        item_Redeco ir,");
		sql.append("        redeco r");
		sql.append("    where f.id_fatura = ir.id_Fatura");
		sql.append("      and f.tp_Situacao_Fatura = 'LI'");
		sql.append("      and r.id_redeco = ir.id_redeco");
		sql.append("      and r.tp_Situacao_Redeco = 'LI'");
		sql.append("      and r.tp_finalidade in ('CJ','LP')");
		sql.append("      and f.id_fatura = :id_fatura");
		sql.append(")");

		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("id_fatura", idFatura);
		List list = getAdsmHibernateTemplate().findBySql(sql.toString(), objects, new ConfigureSqlQuery() {

			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("tot", Hibernate.INTEGER);
			}
		});
		Integer total = (Integer) list.get(0);
		return total > 0;
	}

	public Long findByIdPendenciaDesconto(Long idPendenciaDesconto) {

		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("f.idFatura");
		hql.addInnerJoin(Fatura.class.getName(), "f");

		hql.addCustomCriteria("f.idPendenciaDesconto = " + idPendenciaDesconto);

		List lId = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

		if (!lId.isEmpty()) {
			return (Long) lId.get(0);
		}

		return null;
	}

	public List<Fatura> findByIdRedeco(Long idRedeco) {
		Criteria crit = getSession().createCriteria(persistentClass);
		crit.add(Restrictions.eq("redeco.id", idRedeco));
		return crit.list();
	}

	public List<Map<String, Object>> findInfoDoctosFatura(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().findBySqlToMappedResult(
				getSqlRelatorioExcel(), parameters, configureSqlFaturaExcel());
	}


	private ConfigureSqlQuery configureSqlFaturaExcel() {
		return new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("fil_origem", Hibernate.STRING);
				sqlQuery.addScalar("nr_fat", Hibernate.LONG);
				sqlQuery.addScalar("tp_doc_serv", Hibernate.STRING);
				sqlQuery.addScalar("fil_or_doc_serv", Hibernate.STRING);
				sqlQuery.addScalar("nr_doc_serv", Hibernate.LONG);
				sqlQuery.addScalar("vl_devido", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_desconto", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_receb_parc", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("vl_saldo_dev", Hibernate.BIG_DECIMAL);
			}
		};
	}

	private String getSqlRelatorioExcel() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ff.sg_filial as fil_origem, ")
				.append("fa.nr_fatura as nr_fat, ")
				.append("ds.tp_documento_servico as tp_doc_serv, ")
				.append("fd.sg_filial as fil_or_doc_serv, ")
				.append("ds.nr_docto_servico as nr_doc_serv, ")
				.append("NVL(dd.vl_devido,0) as vl_devido, ")
				.append("NVL(de.vl_desconto,0) as vl_desconto, ")
				.append("NVL((	SELECT SUM(rp.vl_pagamento) ")
				.append("		FROM item_rel_pagto_parcial rp ")
				.append("		WHERE dd.id_devedor_doc_serv_fat = rp.id_devedor_doc_serv_fat),0) as vl_receb_parc, ")
				.append("dd.vl_devido - NVL(")
				.append("(	SELECT SUM(rp.vl_pagamento) ")
				.append("	FROM item_rel_pagto_parcial rp ")
				.append("	WHERE dd.id_devedor_doc_serv_fat = rp.id_devedor_doc_serv_fat)")
				.append(",0) - NVL(de.vl_desconto,0) as vl_saldo_dev ")
				.append("FROM filial ff, fatura fa,  item_fatura IF, devedor_doc_serv_fat dd, ")
				.append("docto_servico ds, desconto de,  filial fd ")
				.append("WHERE fa.id_filial = ff.id_filial ")
				.append("AND fa.id_fatura               = if.id_fatura ")
				.append("AND if.id_devedor_doc_serv_fat = dd.id_devedor_doc_serv_fat ")
				.append("AND dd.id_docto_servico        = ds.id_docto_servico ")
				.append("AND ds.id_filial_origem        = fd.id_filial ")
				.append("AND dd.id_devedor_doc_serv_fat = de.id_devedor_doc_serv_fat(+) ")
				.append("AND ff.sg_filial               = :sgFilial ")
				.append("AND fa.nr_fatura               = :nrFatura ")
				.append("ORDER BY ff.sg_filial, fa.nr_fatura, ")
				.append("ds.tp_documento_servico, fd.sg_filial, ds.nr_docto_servico ");
		return sb.toString();
	}


	@SuppressWarnings("rawtypes")
	public List findFaturasWithoutBoleto(Integer qtDiasEmissaoBoletoAutomatico) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fat  ")
				.append(" FROM ").append(getPersistentClass().getName()).append(" as fat ")
				.append(" JOIN fat.filialByIdFilial fil ")
				.append(" JOIN fat.cliente cli ")
				.append(" WHERE ")
				.append(" 	fat.tpSituacaoFatura = 'EM' ")
				.append(" 	AND fat.dtEmissao < (trunc(sysdate()) -  ?) ")
				.append(" 	AND cli.tpCobranca IN ('3','4') ")
				.append(" 	AND cli.blPreFatura = 'N' ")
				.append(" 	AND (fat.blConhecimentoResumo = 'N'  ")
				.append(" 		OR fat.blConhecimentoResumo IS NULL) ")
				.append(" 	AND (fat.tpSituacaoAprovacao = 'A'  ")
				.append(" 		OR fat.tpSituacaoAprovacao IS NULL) ")
				.append("  AND NOT EXISTS(SELECT 1  ")
				.append(" 				  FROM RelacaoPagtoParcial as rpp  ")
				.append("				  JOIN rpp.fatura as f ")
				.append(" 				  WHERE f.idFatura = fat.idFatura) ")
				.append(" ORDER BY  ")
				.append(" 	fil.sgFilial, ")
				.append("  fat.nrFatura ");


		return getHibernateTemplate().find(sb.toString(), new Object[]{qtDiasEmissaoBoletoAutomatico});
	}

	/**
	 * LMS-6106 - Busca <tt>Fatura</tt> correspondente a um boleto. Utilizado
	 * para recuperar o tipo de manutenï¿½ï¿½o (<tt>tpManutencaoFatura</tt>) e no
	 * processo de cancelamento da fatura.
	 *
	 * @param idBoleto id do <tt>Boleto</tt>
	 * @return <tt>Fatura</tt> relacionada ao boleto
	 */
	public Fatura findFaturaByBoleto(Long idBoleto) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT b.fatura ")
				.append("FROM Boleto b ")
				.append("WHERE b.idBoleto = ? ");
		@SuppressWarnings("unchecked")
		List<Fatura> result = getAdsmHibernateTemplate().find(hql.toString(), idBoleto);
		return result == null || result.isEmpty() ? null : result.get(0);
	}

	/**
	 * LMS-6106 - Executa o cancelamento de uma fatura. Para o cancelamento
	 * total (I: Cancelar a fatura inteira) ajusta o atributo
	 * <tt>blCancelaFaturaInteira</tt> da <tt>Fatura</tt> e o atributo
	 * <tt>blExcluir</tt> de todos os <tt>ItemFatura</tt> para <tt>true</tt>.
	 * Para o cancelamento parcial (E: Excluir alguns documentos de servirão)
	 * ajusta o atributo <tt>blCancelaFaturaInteira</tt> da <tt>Fatura</tt> para
	 * <tt>false</tt> e o atributo <tt>blExcluir</tt> de todos os
	 * <tt>ItemFatura</tt> para <tt>true</tt> ou <tt>false</tt> conforme a
	 * parï¿½metro <tt>idDoctoServicoList</tt>.
	 *
	 * @param fatura             <tt>Fatura</tt> para cancelamento
	 * @param tpManutencaoFatura tipo de manutenï¿½ï¿½o da fatura (I ou E)
	 * @param idDoctoServicoList lista de id's de <tt>DoctoServico</tt>
	 */
	public void executeCancelarFatura(Fatura fatura, String tpManutencaoFatura, List<Long> idDoctoServicoList) {
		Boolean blCancelaFaturaInteira = "I".equals(tpManutencaoFatura);
		fatura.setBlCancelaFaturaInteira(blCancelaFaturaInteira);
		store(fatura);
		@SuppressWarnings("unchecked")
		List<ItemFatura> itemFaturas = fatura.getItemFaturas();
		boolean blExcluirFaturaInteira = true;
		for (ItemFatura itemFatura : itemFaturas) {
			boolean blExcluir = blCancelaFaturaInteira;
			if (!blCancelaFaturaInteira) {
				Long idDoctoServico = itemFatura.getDevedorDocServFat().getDoctoServico().getIdDoctoServico();
				blExcluir = idDoctoServicoList.contains(idDoctoServico);
				blExcluirFaturaInteira = blExcluirFaturaInteira && blExcluir;
			}
			itemFatura.setBlExcluir(blExcluir);
			getAdsmHibernateTemplate().saveOrUpdate(itemFatura);
		}
		if (!blCancelaFaturaInteira && blExcluirFaturaInteira) {
			throw new BusinessException("LMS-36299");
		}
		getAdsmHibernateTemplate().flush();
	}

	public List<Object[]> findFaturaByCriteria(TypedFlatMap criteria) {
		boolean hasPagination = false;
		Integer firstResult = criteria.getInteger("firstResult");
		Integer maxResult = criteria.getInteger("maxResult");
		hasPagination = firstResult != null && maxResult != null ? true : false;
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder();
		if (hasPagination) {
			sql.append(" select * from ( select row_.*, rownum rownum_ from (");
		}
		sql.append(" select")
				.append(" fat.id_fatura as id_fatura,")
				.append(" fe.sg_filial as filial_emissora,")
				.append(" pfe.nm_fantasia as nm_fantasia_filial_emissora,")
				.append(" fat.nr_fatura as nr_fatura,")
				.append(" to_char (fat.dt_emissao, 'dd/MM/yyyy') as dt_emissao,")
				.append(" to_char (fat.dt_vencimento, 'dd/MM/yyyy') as dt_vencimento,")
				.append(" to_char (fat.dt_liquidacao, 'dd/MM/yyyy') as dt_liquidacao,")
				.append(" pd.nr_identificacao as cnpj_devedor,")
				.append(" pd.nm_pessoa as nome_devedor,")
				.append(" fc.sg_filial as filial_cobradora,")
				.append(" pfc.nm_fantasia as nm_fantasia_filial_cobradora,")
				.append(" fat.qt_documentos as qtd_documentos,")
				.append(" fat.vl_total as vl_total,")
				.append(" bol.nr_boleto as nr_boleto,")
				.append(" vd.ds_valor_dominio_i as valor_dominio")
				.append(" from fatura fat,")
				.append(" filial fe,")
				.append(" filial fc,")
				.append(" pessoa pfe,")
				.append(" pessoa pfc,")
				.append(" pessoa pd,")
				.append(" boleto bol,")
				.append(" dominio dom,")
				.append(" valor_dominio vd");
		mountWhereFaturaByCriteria(criteria, sql, true);

		if (hasPagination) {
			sql.append(" ) row_ ) where  rownum_ > " + firstResult + " and rownum <= " + maxResult + "");
		}

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_fatura");
				sqlQuery.addScalar("filial_emissora");
				sqlQuery.addScalar("nm_fantasia_filial_emissora");
				sqlQuery.addScalar("nr_fatura");
				sqlQuery.addScalar("dt_emissao");
				sqlQuery.addScalar("dt_vencimento");
				sqlQuery.addScalar("dt_liquidacao");
				sqlQuery.addScalar("cnpj_devedor");
				sqlQuery.addScalar("nome_devedor");
				sqlQuery.addScalar("filial_cobradora");
				sqlQuery.addScalar("nm_fantasia_filial_cobradora");
				sqlQuery.addScalar("qtd_documentos");
				sqlQuery.addScalar("vl_total");
				sqlQuery.addScalar("nr_boleto");
				sqlQuery.addScalar("valor_dominio");
			}
		};


		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
	}


	public List<Object[]> getRowCountFaturaByCriteria(TypedFlatMap criteria) {
		Map<String, Object> params = new HashMap<String, Object>();

		StringBuilder sql = new StringBuilder();
		sql.append(" select")
				.append(" count(fat.id_fatura) as qtde_registros,")
				.append(" pd.nr_identificacao cnpj,")
				.append(" sum(fat.vl_total) valor")
				.append(" from fatura fat,")
				.append(" pessoa pd,")
				.append(" boleto bol");
		mountWhereFaturaByCriteria(criteria, sql, false);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("qtde_registros");
				sqlQuery.addScalar("cnpj");
				sqlQuery.addScalar("valor");
			}
		};

		return getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
	}

	private void mountWhereFaturaByCriteria(TypedFlatMap criteria, StringBuilder sql, boolean isConditionFull) {

		String nrFatura = criteria.getString(KEY_NR_FATURA);
		String nrBoleto = criteria.getString("nrBoleto");
		String idFatura = criteria.getString("idFatura");
		String dtEmissaoInicio = criteria.getString("dtEmissaoInicio");
		String dtEmissaoFim = criteria.getString("dtEmissaoFim");
		boolean isFaturaInAberto = criteria.getBoolean("isFaturaInAberto");
		String idsPessoasVinculadasCNPJ = criteria.getString("idsPessoasVinculadas");
		String idsClientesSelecionados = criteria.getString("idsClientesSelecionados");

		sql.append(" where 1=1 ");

		setMoreFilterWhereFatura(sql, isConditionFull);

		sql.append(" and fat.id_boleto = bol.id_boleto");
		sql.append(" and pd.id_pessoa = (select max(dev.id_cliente) from devedor_doc_serv_fat dev where dev.id_fatura = fat.id_fatura)");
		if (nrFatura != null) {
			sql.append(" and fat.nr_fatura = " + nrFatura);
		}
		if (nrBoleto != null) {
			sql.append(" and bol.nr_boleto = '" + nrBoleto + "'");
		}
		if (idFatura != null) {
			sql.append(" and fat.id_fatura = " + idFatura);
		}
		if (dtEmissaoInicio != null && dtEmissaoFim != null) {
			sql.append(" and fat.dt_emissao between to_date('" + dtEmissaoInicio + "','dd/MM/yyyy') and to_date('" + dtEmissaoFim + "','dd/MM/yyyy')")
					.append(" and fat.tp_situacao_fatura = 'BL'");
		} else if (isFaturaInAberto) {
			sql.append(" and fat.tp_situacao_fatura = 'BL'");
		}
		if (idsClientesSelecionados != null) {
			sql.append(" and pd.id_pessoa in(" + idsClientesSelecionados + ")");
		} else if (idsPessoasVinculadasCNPJ != null) {
			sql.append(" and pd.id_pessoa in(" + idsPessoasVinculadasCNPJ + ")");
		}

		if (!isConditionFull) {
			sql.append("  group by pd.nr_identificacao ");

		} else {
			sql.append(" order by pd.nr_identificacao, fat.nr_fatura asc");
		}
	}

	/**
	 * @param sql             StringBuilder
	 * @param isConditionFull boolean
	 */
	private void setMoreFilterWhereFatura(StringBuilder sql,
	                                      boolean isConditionFull) {
		if (isConditionFull) {
			sql.append("and  fat.id_filial = fe.id_filial");
			sql.append(" and fat.id_filial = pfe.id_pessoa");
			sql.append(" and fat.id_filial_cobradora = fc.id_filial");
			sql.append(" and fat.id_filial_cobradora = pfc.id_pessoa");
			sql.append(" and dom.nm_dominio  = 'DM_STATUS_ROMANEIO'");
			sql.append(" and vd.vl_valor_dominio = fat.tp_situacao_fatura");
			sql.append(" and vd.id_dominio = dom.id_dominio");
		}
	}

	public String findFaturasVencidasEAVencerFullSQL(TypedFlatMap tfm) {
		Map<String, Object> objects = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		getFullQueryVencidadsVencer(sql);
		createFullFromWhereQueryVencidadasVencer(tfm, objects, sql);
		return sql.toString() + " [ " + objects.toString() + "]";
	}

	public void updateDhEnvioCobTerceiraFatura(Long idFatura) {
		Fatura fatura = findById(idFatura);
		fatura.setDhEnvioCobTerceira(new DateTime());
		storeBasic(fatura);
	}

	public List<Object[]> findCobrancaProAtivaFaturas() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT cast(decode(f.ds_email, NULL, regfa.sg_regional, reg.sg_regional) as varchar2(100)) sg_regional,  \n ");
		sql.append("        cast(decode(f.ds_email, NULL, regfa.id_regional, reg.id_regional) as number(10)) id_regional,  \n ");
		sql.append("        cast(decode(f.ds_email,  \n ");
		sql.append("               NULL,  \n ");
		sql.append("               regfa.ds_email_faturamento,  \n ");
		sql.append("               reg.ds_email_faturamento) as varchar2(100)) ds_email_faturamento,  \n ");
		sql.append("        fcob.sg_filial AS sg_filial_cob,  \n ");
		sql.append("        fcob.id_filial AS id_filial_cob,  \n ");
		sql.append("        pe.nm_pessoa,  \n ");
		sql.append("        f.id_cliente,  \n ");
		sql.append("        ffat.sg_filial AS sg_filial_fat,  \n ");
		sql.append("        ffat.id_filial AS id_filial_fat,  \n ");
		sql.append("        f.nr_fatura,  \n ");
		sql.append("        f.ds_email,  \n ");
		sql.append("        f.id_fatura  \n ");
		sql.append("   FROM (SELECT f.*,  \n ");
		sql.append("                to_char((SELECT wm_concat(co.ds_email)  \n ");
		sql.append("                          FROM contato co  \n ");
		sql.append("                         WHERE co.tp_contato = 'CB'  \n ");
		sql.append("                           AND co.ds_email IS NOT NULL  \n ");
		sql.append("                           AND co.id_pessoa = f.id_cliente)) ds_email  \n ");
		sql.append("           FROM fatura f) f,  \n ");
		sql.append("        pessoa pe,  \n ");
		sql.append("        filial ffat,  \n ");
		sql.append("        cliente cl,  \n ");
		sql.append("        filial fcob,  \n ");
		sql.append("        (SELECT *  \n ");
		sql.append("           FROM regional reg  \n ");
		sql.append("          WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND  \n ");
		sql.append("                reg.dt_vigencia_final) reg,  \n ");
		sql.append("        (SELECT *  \n ");
		sql.append("           FROM regional_filial regf  \n ");
		sql.append("          WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND  \n ");
		sql.append("                regf.dt_vigencia_final) regf,  \n ");
		sql.append("        (SELECT *  \n ");
		sql.append("           FROM regional reg  \n ");
		sql.append("          WHERE trunc(SYSDATE) BETWEEN reg.dt_vigencia_inicial AND  \n ");
		sql.append("                reg.dt_vigencia_final) regfa,  \n ");
		sql.append("        (SELECT *  \n ");
		sql.append("           FROM regional_filial regf  \n ");
		sql.append("          WHERE trunc(SYSDATE) BETWEEN regf.dt_vigencia_inicial AND  \n ");
		sql.append("                regf.dt_vigencia_final) regffa,  \n ");
		sql.append("        excecao_cliente_financeiro ecf  \n ");
		sql.append("  WHERE f.id_cliente = pe.id_pessoa(+)  \n ");
		sql.append("    AND f.id_filial = ffat.id_filial(+)  \n ");
		sql.append("    AND f.id_cliente = cl.id_cliente(+)  \n ");
		sql.append("    AND cl.id_filial_cobranca = fcob.id_filial(+)  \n ");
		sql.append("    AND cl.id_filial_cobranca = regf.id_filial(+)  \n ");
		sql.append("    AND regf.id_regional = reg.id_regional(+)  \n ");
		sql.append("    AND f.id_filial = regffa.id_filial(+)  \n ");
		sql.append("    AND regffa.id_regional = regfa.id_regional(+)  \n ");
		sql.append("    AND f.id_cliente = ecf.id_cliente  \n ");
		sql.append("    AND ecf.tp_envio_cobranca_pro_ativa = 'E'  \n ");
		sql.append("    AND f.tp_situacao_fatura = 'BL'  \n ");
		sql.append("    AND f.dt_emissao >= nvl((SELECT to_date(pg.ds_conteudo, 'dd/mm/yyyy')  \n ");
		sql.append("                              FROM parametro_geral pg  \n ");
		sql.append("                             WHERE pg.nm_parametro_geral =  \n ");
		sql.append("                                   'DT_INICIO_ENVIO_COB_PRO_ATIVA_EMAIL_FAT'  \n ");
		sql.append("                               AND rownum = 1),  \n ");
		sql.append("                            trunc(SYSDATE))  \n ");
		sql.append("    AND f.dt_vencimento >= trunc(SYSDATE)  \n ");
		sql.append("    AND f.dt_vencimento - nvl((SELECT to_number(pg.ds_conteudo)  \n ");
		sql.append("                                FROM parametro_geral pg  \n ");
		sql.append("                               WHERE pg.nm_parametro_geral =  \n ");
		sql.append("                                     'NR_DIAS_ENVIO_COBRANCA_PRO_ATIVA'  \n ");
		sql.append("                                 AND rownum = 1),  \n ");
		sql.append("                              5) <= trunc(SYSDATE)  \n ");
		sql.append("    AND NOT EXISTS  \n ");
		sql.append("  (SELECT 1  \n ");
		sql.append("           FROM relacao_pagto_parcial rpp  \n ");
		sql.append("          WHERE rpp.id_fatura = f.id_fatura  \n ");
		sql.append("            AND rownum = 1)  \n ");
		sql.append("    AND (f.tp_situacao_aprovacao = 'A' OR f.tp_situacao_aprovacao IS NULL)  \n ");
		sql.append("    AND (nvl((SELECT pg.ds_conteudo  \n ");
		sql.append("               FROM parametro_geral pg  \n ");
		sql.append("              WHERE pg.nm_parametro_geral = 'BL_FINANCEIRO_CORPORATIVO'  \n ");
		sql.append("                AND rownum = 1),  \n ");
		sql.append("             'S') = 'N' OR NOT EXISTS  \n ");
		sql.append("         (SELECT 1  \n ");
		sql.append("            FROM romaneios    r,  \n ");
		sql.append("                 filial_sigla fs  \n ");
		sql.append("           WHERE r.unid_sigla = fs.ds_siglaanterior  \n ");
		sql.append("             AND fs.id_filial_lms = f.id_filial  \n ");
		sql.append("             AND r.numero = f.nr_fatura  \n ");
		sql.append("             AND r.status = 4  \n ");
		sql.append("             AND rownum = 1))  \n ");
		sql.append("    AND (nvl((SELECT pg.ds_conteudo  \n ");
		sql.append("               FROM parametro_geral pg  \n ");
		sql.append("              WHERE pg.nm_parametro_geral = 'BL_COBRANCA_PRO_ATIVA_FRANQUIA'  \n ");
		sql.append("                AND rownum = 1),  \n ");
		sql.append("             'N') = 'S' OR  \n ");
		sql.append("        nvl((SELECT hf.tp_filial  \n ");
		sql.append("               FROM historico_filial hf  \n ");
		sql.append("              WHERE hf.id_filial = f.id_filial  \n ");
		sql.append("                AND trunc(SYSDATE) BETWEEN hf.dt_real_operacao_inicial AND  \n ");
		sql.append("                    hf.dt_real_operacao_final),  \n ");
		sql.append("             'FI') <> 'FR')  \n ");
		sql.append("    AND NOT EXISTS  \n ");
		sql.append("  (SELECT 1  \n ");
		sql.append("           FROM monit_mens_fatura      mmf,  \n ");
		sql.append("                monitoramento_mensagem mm  \n ");
		sql.append("          WHERE mmf.id_monitoramento_mensagem = mm.id_monitoramento_mensagem  \n ");
		sql.append("            AND mmf.id_fatura = f.id_fatura  \n ");
		sql.append("            AND mm.tp_modelo_mensagem = 'CP'  \n ");
		sql.append("            AND mm.tp_envio_mensagem = 'E'  \n ");
		sql.append("            AND rownum = 1)  \n ");
		sql.append("  ORDER BY sg_regional,  \n ");
		sql.append("           id_regional,  \n ");
		sql.append("           fcob.sg_filial,  \n ");
		sql.append("           fcob.id_filial,  \n ");
		sql.append("           pe.nm_pessoa,  \n ");
		sql.append("           f.id_cliente,  \n ");
		sql.append("           ffat.sg_filial,  \n ");
		sql.append("           ffat.id_filial,  \n ");
		sql.append("           f.id_cliente,  \n ");
		sql.append("           f.nr_fatura  \n ");
		return getAdsmHibernateTemplate().findBySql(sql.toString(), null, csqFaturasNaoEnviadasEmail);
	}

	public boolean findExistsComposicaoPagamento(Long idFatura) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(*) as tot ");
		sql.append(" from composicao_pagamento_redeco cpr, redeco re, item_redeco ire ");
		sql.append(" where cpr.id_redeco = re.id_redeco ");
		sql.append(" and re.id_redeco = ire.id_redeco ");
		sql.append(" and ire.id_fatura = :idFatura ");
		sql.append(" and re.tp_situacao_redeco = 'LI' ");
		sql.append(" and tp_composicao_pagamento_redeco in ('J', 'L','R') ");

		Map<String, Object> objects = new HashMap<String, Object>();
		objects.put("idFatura", idFatura);
		List list = getAdsmHibernateTemplate().findBySql(sql.toString(),
				objects, new ConfigureSqlQuery() {
					@Override
					public void configQuery(SQLQuery sqlQuery) {
						sqlQuery.addScalar("tot", Hibernate.INTEGER);
					}
				});
		Integer total = (Integer) list.get(0);
		return total > 0;
	}


	//------------------------------ Métodos relacionados a geração das Faturas (Report)


	/**
	 * Itera cada itens da fatura
	 *
	 * @param sql SqlTemplate
	 * @return Lista faturas
	 * @author Mickaël Jalbert
	 * @since 07/03/2007
	 */
	public List iteratorSubSelectFatura(SqlTemplate sql) {
		return getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map mapDocumento = new HashMap();

				mapDocumento.put(SQL_ROWNUM, rs.getLong(SQL_ROWNUM));
				mapDocumento.put(SQL_NOTA_FISCAL, rs.getLong(SQL_NOTA_FISCAL));
				mapDocumento.put(SQL_DOCUMENTOSERVICO, rs.getString(SQL_DOCUMENTOSERVICO));
				mapDocumento.put(SQL_VALOR, rs.getBigDecimal(SQL_VALOR));
				mapDocumento.put(SQL_ICMS, rs.getBigDecimal(SQL_ICMS) != null ? rs.getBigDecimal(SQL_ICMS) : new BigDecimal(0));
				mapDocumento.put(SQL_DESCONTO, rs.getBigDecimal(SQL_DESCONTO));
				mapDocumento.put(SQL_MERCADORIA, rs.getBigDecimal(SQL_MERCADORIA));
				mapDocumento.put(SQL_PESO, rs.getBigDecimal(SQL_PESO));
				mapDocumento.put(SQL_ID_DOCTO_SERVICO, rs.getLong(SQL_ID_DOCTO_SERVICO));
				mapDocumento.put(SQL_ID_DEVEDOR_DOC_SERV_FAT, rs.getLong(SQL_ID_DEVEDOR_DOC_SERV_FAT));
				mapDocumento.put(SQL_NR_DOCUMENTO_ELETRONICO, rs.getString(SQL_NR_DOCUMENTO_ELETRONICO));
				mapDocumento.put(SQL_ID_FATURA, rs.getLong(SQL_ID_FATURA));
				mapDocumento.put(SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS, rs.getString(SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS));
				mapDocumento.put(SQL_TP_DOCUMENTO_SERVICO, rs.getString(SQL_TP_DOCUMENTO_SERVICO));
				return mapDocumento;
			}

		});
	}

	/**
	 * @param criteria TypedFlatMap
	 * @author Mickaël Jalbert
	 * @since 09/03/2007
	 */
	public SqlTemplate mountSqlFatura(TypedFlatMap criteria) {

		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("PE.NM_PESSOA", SQL_EMPRESA);
		sql.addProjection("C.OB_FATURA", SQL_OB_FATURA);
		sql.addProjection("NVL(F.VL_COTACAO_MOEDA, CM.VL_COTACAO_MOEDA)", SQL_COTACAO_DOLAR);
		sql.addProjection("FIL.SG_FILIAL || ' ' || TO_CHAR(F.NR_FATURA, '0000000000')", SQL_SG_FILIAL_NR_FATURA);
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("TLO.DS_TIPO_LOGRADOURO_I") + " || ' ' || TRIM(END.DS_ENDERECO) || ', ' || END.NR_ENDERECO || DECODE(END.DS_COMPLEMENTO, NULL, '', ' - ' || TRIM(END.DS_COMPLEMENTO))", SQL_ENDERECOFILIAL);
		sql.addProjection("F.DT_EMISSAO", SQL_EMISSAO);
		sql.addProjection("MUN.NM_MUNICIPIO || ' - ' || UF.SG_UNIDADE_FEDERATIVA", SQL_NM_FILIAL);
		sql.addProjection("(SELECT " + PropertyVarcharI18nProjection.createProjection("FAG.DS_FORMA_AGRUPAMENTO_I") + " FROM FORMA_AGRUPAMENTO FAG, AGRUPAMENTO_CLIENTE AGC WHERE F.ID_AGRUPAMENTO_CLIENTE = AGC.ID_AGRUPAMENTO_CLIENTE AND	F.ID_DIVISAO_CLIENTE = AGC.ID_DIVISAO_CLIENTE AND AGC.ID_FORMA_AGRUPAMENTO = FAG.ID_FORMA_AGRUPAMENTO)", SQL_FORMA_AGRUPAMENTO);
		sql.addProjection("F.DT_VENCIMENTO", SQL_VENCIMENTO);
		sql.addProjection("END.NR_CEP", SQL_CEP);
		sql.addProjection("TA.DS_TIPO_AGRUPAMENTO", SQL_TIPO_AGRUPAMENTO);
		sql.addProjection("MDS.SG_MOEDA || ' ' || MDS.DS_SIMBOLO", SQL_MOEDA);
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VDOMMOD.DS_VALOR_DOMINIO_I"), SQL_MODAL);

		sql.addProjection("(SELECT " + PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I") +
				" FROM DOCTO_SERVICO DOC, DEVEDOR_DOC_SERV_FAT DEV, DOMINIO D, VALOR_DOMINIO VD " +
				" WHERE DOC.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO " +
				" AND DEV.ID_FATURA = F.ID_FATURA " +
				" AND D.ID_DOMINIO = VD.ID_DOMINIO " +
				" AND D.NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO' " +
				" AND VD.VL_VALOR_DOMINIO = DOC.TP_DOCUMENTO_SERVICO " +
				" AND ROWNUM = 1)", SQL_TP_DOCUMENTO);

		sql.addProjection(HQL_DC_DS_DIVISAO_CLIENTE, SQL_DS_DIVISAO_CLIENTE);
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("VDOMAB.DS_VALOR_DOMINIO_I"), SQL_ABRANGENCIA);
		sql.addProjection(HQL_P_NM_PESSOA, SQL_EMPRESACLIENTE);
		sql.addProjection(HQL_P_TP_IDENTIFICACAO, SQL_TP_IDENTIFICACAO);
		sql.addProjection(HQL_P_NR_IDENTIFICACAO, SQL_NR_IDENTIFICACAO);
		sql.addProjection("(SELECT '(' || TE.NR_DDD || ') ' || TRIM(TE.NR_TELEFONE) FROM TELEFONE_ENDERECO TE WHERE TE.ID_ENDERECO_PESSOA = ENDCLI.ID_ENDERECO_PESSOA AND ROWNUM = 1)", SQL_TELEFONECLIENTE);
		sql.addProjection("DECODE(IE.NR_INSCRICAO_ESTADUAL, NULL, 'ISENTO', IE.NR_INSCRICAO_ESTADUAL)", SQL_INSCRICAO);
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("TLOCLI.DS_TIPO_LOGRADOURO_I") + "|| ' ' || TRIM(ENDCLI.DS_ENDERECO) || ', ' || ENDCLI.NR_ENDERECO || DECODE(ENDCLI.DS_COMPLEMENTO, NULL, '', ' - ' || TRIM(ENDCLI.DS_COMPLEMENTO))", SQL_ENDERECOCLIENTE);
		sql.addProjection("TRIM(NVL(ENDCLI.DS_BAIRRO, ''))", SQL_BAIRROCLIENTE);
		sql.addProjection(HQL_MUNCLI_NM_MUNICIPIO, SQL_CIDADECLIENTE);
		sql.addProjection(HQL_UFCLI_SG_UNIDADE_FEDERATIVA, SQL_UFCLIENTE);
		sql.addProjection("TRIM(ENDCLI.NR_CEP)", SQL_CEPCLIENTE);
		sql.addProjection("NVL((SELECT	PBF.BL_VALOR_LIQUIDO FROM PARAMETRO_BOLETO_FILIAL PBF WHERE PBF.ID_FILIAL = F.ID_FILIAL AND SYSDATE BETWEEN DT_VIGENCIA_INICIAL AND DT_VIGENCIA_FINAL) ,'N')", SQL_BL_VALOR_LIQUIDO);
		sql.addProjection(HQL_PF_TP_IDENTIFICACAO, SQL_TPF_IDENTIFICACAO);
		sql.addProjection(HQL_PF_NR_IDENTIFICACAO, SQL_CNPJ_FILIAL);
		sql.addProjection(HQL_C_PC_JURO_DIARIO, SQL_CJURODIARIO);
		sql.addProjection(HQL_FIL_PC_JURO_DIARIO, SQL_FILJURODIARIO);
		sql.addProjection(HQL_F_ID_FATURA, SQL_ID_FATURA);
		sql.addProjection(HQL_F_ID_FILIAL, SQL_ID_FILIAL);
		sql.addProjection(HQL_F_TP_SITUACAO_FATURA, SQL_TP_SITUACAO_FATURA);
		sql.addProjection(HQL_F_ID_MOEDA, SQL_ID_MOEDA_ORIGEM);
		sql.addProjection(HQL_UF_ID_PAIS, SQL_ID_PAIS_ORIGEM);
		sql.addProjection("PF.NM_PESSOA", "NM_PESSOA_FILIAL");
		sql.addProjection("C.BL_ENVIA_DOCS_FATURAMENTO_NAS", SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS);

		return getDefaultQuery(sql, criteria);
	}

	public SqlTemplate getDefaultQuery(SqlTemplate sql, TypedFlatMap criteria) {
		sql.addFrom("FATURA", "F");
		sql.addFrom("CLIENTE", "C");
		sql.addFrom("INSCRICAO_ESTADUAL", "IE");
		sql.addFrom("DIVISAO_CLIENTE", "DC");
		sql.addFrom(HQL_FILIAL, "FIL");
		sql.addFrom("PESSOA", "PF");
		sql.addFrom(SQL_MOEDA, "MDS");
		sql.addFrom("COTACAO_MOEDA", "CM");

		sql.addFrom("PESSOA", "P");
		sql.addFrom("PESSOA", "PE");
		sql.addFrom(SQL_TIPO_AGRUPAMENTO, "TA");
		sql.addFrom("ENDERECO_PESSOA", "END");
		sql.addFrom("ENDERECO_PESSOA", "ENDCLI");
		sql.addFrom("TIPO_LOGRADOURO", "TLO");
		sql.addFrom("TIPO_LOGRADOURO", "TLOCLI");
		sql.addFrom("MUNICIPIO", "MUN");
		sql.addFrom("MUNICIPIO", "MUNCLI");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF");
		sql.addFrom("UNIDADE_FEDERATIVA", "UFCLI");
		sql.addFrom("DOMINIO", "DOMMOD");
		sql.addFrom("VALOR_DOMINIO", "VDOMMOD");
		sql.addFrom("DOMINIO", "DOMAB");
		sql.addFrom("VALOR_DOMINIO", "VDOMAB");

		sql.addJoin("F.ID_CLIENTE", "C.ID_CLIENTE");
		sql.addJoin("C.ID_CLIENTE", "IE.ID_PESSOA(+)");
		sql.addJoin("F.ID_DIVISAO_CLIENTE", "DC.ID_DIVISAO_CLIENTE(+)");
		sql.addJoin(HQL_F_ID_FILIAL, "FIL.ID_FILIAL");
		sql.addJoin("FIL.ID_FILIAL", "PF.ID_PESSOA");
		sql.addJoin(HQL_F_ID_MOEDA, "MDS.ID_MOEDA");
		sql.addJoin("F.ID_COTACAO_MOEDA", "CM.ID_COTACAO_MOEDA(+)");
		sql.addJoin("C.ID_CLIENTE", "P.ID_PESSOA");
		sql.addJoin("FIL.ID_EMPRESA", "PE.ID_PESSOA");
		sql.addJoin("F.ID_TIPO_AGRUPAMENTO", "TA.ID_TIPO_AGRUPAMENTO(+)");
		sql.addJoin("PF.ID_ENDERECO_PESSOA", "END.ID_ENDERECO_PESSOA");
		sql.addJoin("END.ID_TIPO_LOGRADOURO", "TLO.ID_TIPO_LOGRADOURO");
		sql.addJoin("END.ID_MUNICIPIO", "MUN.ID_MUNICIPIO");
		sql.addJoin("MUN.ID_UNIDADE_FEDERATIVA", "UF.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("F_BUSCA_ENDERECO_PESSOA(P.ID_PESSOA, 'COB', SYSDATE)", "ENDCLI.ID_ENDERECO_PESSOA");
		sql.addJoin("ENDCLI.ID_TIPO_LOGRADOURO", "TLOCLI.ID_TIPO_LOGRADOURO");
		sql.addJoin("ENDCLI.ID_MUNICIPIO", "MUNCLI.ID_MUNICIPIO");
		sql.addJoin("MUNCLI.ID_UNIDADE_FEDERATIVA", "UFCLI.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("DOMMOD.ID_DOMINIO", "VDOMMOD.ID_DOMINIO");
		sql.addJoin("DOMAB.ID_DOMINIO", "VDOMAB.ID_DOMINIO");
		sql.addJoin("LOWER(VDOMMOD.VL_VALOR_DOMINIO)", "LOWER(F.TP_MODAL)");
		sql.addJoin("LOWER(VDOMAB.VL_VALOR_DOMINIO)", "LOWER(F.TP_ABRANGENCIA)");
		sql.addCustomCriteria("LOWER(DOMMOD.NM_DOMINIO) = 'dm_modal'");
		sql.addCustomCriteria("LOWER(DOMAB.NM_DOMINIO) = 'dm_abrangencia'");
		sql.addCustomCriteria("IE.BL_INDICADOR_PADRAO(+) = 'S'");
		sql.addCustomCriteria("IE.TP_SITUACAO(+) = 'A'");

		mountFilter(criteria, sql, true);

		sql.addOrderBy("FIL.SG_FILIAL");
		sql.addOrderBy("F.NR_FATURA");

		return sql;
	}


	/**
	 * @param criteria        TypedFlatMap
	 * @param sql             SqlTemplate
	 * @param min13Documentos boolean
	 * @author Mickaël Jalbert
	 * @since 09/03/2007
	 */
	private void mountFilter(TypedFlatMap criteria, SqlTemplate sql, boolean min13Documentos) {
		sql.addCustomCriteria("F.TP_SITUACAO_FATURA NOT IN ('CA','IN')");
		sql.addCustomCriteria("(F.TP_SITUACAO_APROVACAO = 'A' OR F.TP_SITUACAO_APROVACAO IS NULL)");

		// REEMISSÃO de faturas nacionais
		if (criteria.get(REEMITIR) != null && criteria.getBoolean(REEMITIR)) {
			sql.addCriteria(HQL_F_ID_FATURA, "=", criteria.getLong("fatura.idFatura"));
			sql.addCriteria("F.ID_CLIENTE", "=", criteria.getLong(KEY_CLIENTE_ID_CLIENTE));

		} else if (StringUtils.isNotBlank(criteria.getString("importacaoPreFaturas"))) {
			/*
			 * Vem da importação de arquivo de pré-faturas. Imprime relatório
			 * por uma lista de ids montado ao chamar o relatorio
			 */
			sql.addCustomCriteria("F.ID_FATURA IN (" + criteria.getString("importacaoPreFaturas") + ")");

		} else {
			// Filtros da tela
			Long idFilial = criteria.getLong(KEY_FILIAL_ID_FILIAL);
			if (idFilial == null) {
				idFilial = criteria.getLong("filialByIdFilial.idFilial");

			}

			if (null != idFilial) {
				sql.addCriteria(HQL_F_ID_FILIAL, "=", idFilial);

			}
			sql.addCriteria(HQL_F_ID_FATURA, "=", criteria.getLong("idFatura"));
			sql.addCriteria("F.TP_MODAL", "=", criteria.getString(KEY_MODAL));
			sql.addCriteria("F.TP_ABRANGENCIA", "=", criteria.getString(KEY_ABRANGENCIA));
			sql.addCriteria("F.NR_FATURA", ">=", criteria.getLong(KEY_FATURA_INICIAL));
			sql.addCriteria("F.NR_FATURA", "<=", criteria.getLong(KEY_FATURA_FINAL));

			/*
			 * Se for de filial diferente só pode imprimir faturas já impressas
			 */
			if (null != idFilial && SessionUtils.getFilialSessao().getIdFilial().compareTo(idFilial) != 0) {
				sql.addCustomCriteria("F.BL_INDICADOR_IMPRESSAO = 'S'");
			}

			// Se é informado um filtro de fatura (intervalo), filtrar por
			// situação
			if (criteria.getLong(KEY_FATURA_INICIAL) != null || criteria.getLong(KEY_FATURA_FINAL) != null) {
				sql.addCustomCriteria("F.TP_SITUACAO_FATURA IN('DI','EM','BL')");

				// Se não tem nenhum filtro de intervalor ou de fatura
			} else if (criteria.getLong("idFatura") == null) {
				sql.addCustomCriteria("F.TP_SITUACAO_FATURA IN('DI','BL')");
				sql.addCustomCriteria("F.BL_INDICADOR_IMPRESSAO = 'N'");

				// Se tem que selecionar faturas com o mínimo 13 characteres
				if (min13Documentos) {
					sql.addCustomCriteria("F.QT_DOCUMENTOS > 12");

				}
			}
		}
	}

	/**
	 * Itera cada fatura
	 *
	 * @param criteria TypedFlatMap
	 * @return Lista de faturas
	 * @author Mickaël Jalbert
	 * @since 07/03/2007
	 */
	public List iteratorSelectFatura(TypedFlatMap criteria) {
		SqlTemplate sql = mountSqlFatura(criteria);
		return getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowMapper() {

			@SuppressWarnings("unchecked")
			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map mapFatura = new HashMap();

				mapFatura.put(SQL_EMPRESA, rs.getString(SQL_EMPRESA));
				mapFatura.put(SQL_OB_FATURA, rs.getString(SQL_OB_FATURA) != null ? rs.getString(SQL_OB_FATURA) : "");
				mapFatura.put(SQL_COTACAO_DOLAR, rs.getBigDecimal(SQL_COTACAO_DOLAR));
				mapFatura.put(SQL_SG_FILIAL_NR_FATURA, rs.getString(SQL_SG_FILIAL_NR_FATURA));
				mapFatura.put(SQL_ENDERECOFILIAL, rs.getString(SQL_ENDERECOFILIAL));
				mapFatura.put(SQL_EMISSAO, rs.getDate(SQL_EMISSAO));
				mapFatura.put(SQL_NM_FILIAL, rs.getString(SQL_NM_FILIAL));
				mapFatura.put(SQL_FORMA_AGRUPAMENTO, rs.getString(SQL_FORMA_AGRUPAMENTO));
				mapFatura.put(SQL_VENCIMENTO, rs.getDate(SQL_VENCIMENTO));
				mapFatura.put(SQL_CEP, rs.getString(SQL_CEP));
				mapFatura.put(SQL_TIPO_AGRUPAMENTO, rs.getString(SQL_TIPO_AGRUPAMENTO));
				mapFatura.put(SQL_MOEDA, rs.getString(SQL_MOEDA));
				mapFatura.put(SQL_MODAL, rs.getString(SQL_MODAL));
				mapFatura.put(SQL_TP_DOCUMENTO, rs.getString(SQL_TP_DOCUMENTO));
				mapFatura.put(SQL_DS_DIVISAO_CLIENTE, rs.getString(SQL_DS_DIVISAO_CLIENTE));
				mapFatura.put(SQL_ABRANGENCIA, rs.getString(SQL_ABRANGENCIA));
				mapFatura.put(SQL_TELEFONECLIENTE, rs.getString(SQL_TELEFONECLIENTE));
				mapFatura.put(SQL_INSCRICAO, rs.getString(SQL_INSCRICAO));
				mapFatura.put(SQL_ENDERECOCLIENTE, rs.getString(SQL_ENDERECOCLIENTE));
				mapFatura.put(SQL_BAIRROCLIENTE, rs.getString(SQL_BAIRROCLIENTE));
				mapFatura.put(SQL_CIDADECLIENTE, rs.getString(SQL_CIDADECLIENTE));
				mapFatura.put(SQL_UFCLIENTE, rs.getString(SQL_UFCLIENTE));
				mapFatura.put(SQL_CEPCLIENTE, rs.getString(SQL_CEPCLIENTE));
				mapFatura.put(SQL_BL_VALOR_LIQUIDO, rs.getString(SQL_BL_VALOR_LIQUIDO));
				mapFatura.put(SQL_TPF_IDENTIFICACAO, rs.getString(SQL_TPF_IDENTIFICACAO));
				mapFatura.put(SQL_CNPJ_FILIAL, rs.getString(SQL_CNPJ_FILIAL));
				mapFatura.put(SQL_ID_FATURA, rs.getLong(SQL_ID_FATURA));
				mapFatura.put(SQL_ID_FILIAL, rs.getLong(SQL_ID_FILIAL));
				mapFatura.put(SQL_TP_SITUACAO_FATURA, rs.getString(SQL_TP_SITUACAO_FATURA));
				mapFatura.put(SQL_ID_PAIS_ORIGEM, rs.getLong(SQL_ID_PAIS_ORIGEM));
				mapFatura.put(SQL_ID_MOEDA_ORIGEM, rs.getLong(SQL_ID_MOEDA_ORIGEM));
				mapFatura.put(SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS, rs.getString(SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS));

				mapFatura.put(SQL_EMPRESACLIENTE, FormatUtils.formatIdentificacao(rs.getString(SQL_TP_IDENTIFICACAO), rs.getString(SQL_NR_IDENTIFICACAO)) + " - " + rs.getString(SQL_EMPRESACLIENTE));
				mapFatura.put("NM_PESSOA_FILIAL", rs.getString("NM_PESSOA_FILIAL"));
				return mapFatura;
			}
		});

	}

	/**
	 * Monta o sql do itens da fatura
	 *
	 * @param idFatura      Long
	 * @param idFilial      Long
	 * @param idPaisOrigem  Long
	 * @param idMoedaOrigem Long
	 * @return SqlTemplate
	 * @author Mickaël Jalbert
	 * @author Mickaël Jalbert
	 * @since 07/03/2007
	 */
	public SqlTemplate mountSqlSubFatura(Long idFatura, Long idFilial, Long idPaisOrigem, Long idMoedaOrigem) {
		SqlTemplate sqlSub = new SqlTemplate();
		sqlSub.addProjection(SQL_ROWNUM);
		sqlSub.addProjection(HQL_DEV_ID_FATURA, SQL_ID_FATURA);
		sqlSub.addProjection("(CASE WHEN (DOC.TP_DOCUMENTO_SERVICO = 'CTR' OR DOC.TP_DOCUMENTO_SERVICO = 'CTE' OR DOC.TP_DOCUMENTO_SERVICO = 'NFT' OR DOC.TP_DOCUMENTO_SERVICO = 'NTE') THEN ((SELECT MIN(NFC.NR_NOTA_FISCAL) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = DOC.ID_DOCTO_SERVICO)) ELSE NULL END)", SQL_NOTA_FISCAL);
		sqlSub.addProjection("FILORIGEM.SG_FILIAL || ' ' || TO_CHAR(DOC.NR_DOCTO_SERVICO, (CASE WHEN(DOC.TP_DOCUMENTO_SERVICO = 'CRT') THEN '000000' WHEN (DOC.TP_DOCUMENTO_SERVICO = 'NDN') THEN '0000000000' ELSE '00000000' END))", SQL_DOCUMENTOSERVICO);
		sqlSub.addProjection("F_CONV_MOEDA(?,?,?,?,?,DEV.VL_DEVIDO)", SQL_VALOR);
		sqlSub.addCriteriaValue(idPaisOrigem);
		sqlSub.addCriteriaValue(idMoedaOrigem);
		sqlSub.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sqlSub.addCriteriaValue(SessionUtils.getMoedaSessao().getIdMoeda());
		sqlSub.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sqlSub.addProjection("F_CONV_MOEDA(?,?,?,?,?,(CASE WHEN (DOC.TP_DOCUMENTO_SERVICO IN ('NFT', 'NTE')) THEN (SELECT sum(VL_IMPOSTO) FROM imposto_servico iser WHERE iser.id_conhecimento=DOC.ID_DOCTO_SERVICO and TP_IMPOSTO='IS') ELSE (CASE WHEN (DOC.TP_DOCUMENTO_SERVICO IN ('NFS', 'NSE')) THEN (SELECT sum(VL_IMPOSTO) FROM imposto_servico iser WHERE iser.id_nota_fiscal_servico=DOC.ID_DOCTO_SERVICO and TP_IMPOSTO='IS') ELSE DOC.VL_IMPOSTO END) END))", SQL_ICMS);
		sqlSub.addCriteriaValue(idPaisOrigem);
		sqlSub.addCriteriaValue(idMoedaOrigem);
		sqlSub.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sqlSub.addCriteriaValue(SessionUtils.getMoedaSessao().getIdMoeda());
		sqlSub.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sqlSub.addProjection("NVL(F_CONV_MOEDA(?,?,?,?,?,DES.VL_DESCONTO), 0)", SQL_DESCONTO);
		sqlSub.addCriteriaValue(idPaisOrigem);
		sqlSub.addCriteriaValue(idMoedaOrigem);
		sqlSub.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sqlSub.addCriteriaValue(SessionUtils.getMoedaSessao().getIdMoeda());
		sqlSub.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sqlSub.addProjection("NVL(F_CONV_MOEDA(?,?,?,?,?,DOC.VL_MERCADORIA), 0)", SQL_MERCADORIA);
		sqlSub.addCriteriaValue(idPaisOrigem);
		sqlSub.addCriteriaValue(idMoedaOrigem);
		sqlSub.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sqlSub.addCriteriaValue(SessionUtils.getMoedaSessao().getIdMoeda());
		sqlSub.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sqlSub.addProjection("(CASE WHEN (DOC.TP_DOCUMENTO_SERVICO IN ('CTR', 'CRT')) THEN CASE WHEN NVL(DOC.PS_AFORADO,0) = 0 THEN NVL(DOC.PS_REAL,0) ELSE NVL(DOC.PS_AFORADO,0) END ELSE 0 END)", SQL_PESO);
		sqlSub.addProjection(HQL_DOC_ID_DOCTO_SERVICO, SQL_ID_DOCTO_SERVICO);
		sqlSub.addProjection(HQL_DEV_ID_DEVEDOR_DOC_SERV_FAT, SQL_ID_DEVEDOR_DOC_SERV_FAT);
		sqlSub.addProjection("CAST( CASE WHEN (LENGTH(mdc.nr_documento_eletronico) > 10) then " +
				"(SUBSTR(lpad(mdc.nr_documento_eletronico, 15, '0'), LENGTH(mdc.nr_documento_eletronico) - 10, 11)) ELSE " +
				"mdc.nr_documento_eletronico||'' END " +
				"as number(15))", SQL_NR_DOCUMENTO_ELETRONICO);
		sqlSub.addProjection("(SELECT " + SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS + " FROM CLIENTE C WHERE C.ID_CLIENTE = DEV.ID_CLIENTE) ", SQL_BL_ENVIA_DOCS_FATURAMENTO_NAS);
		sqlSub.addProjection("(CASE WHEN (DOC.TP_DOCUMENTO_SERVICO = 'CTE') THEN DOC.TP_DOCUMENTO_SERVICO ELSE (CASE WHEN (DOC.TP_DOCUMENTO_SERVICO = 'NTE' OR DOC.TP_DOCUMENTO_SERVICO = 'NSE') AND NOT EXISTS (SELECT 1 FROM PARAMETRO_FILIAL PF, CONTEUDO_PARAMETRO_FILIAL CPF WHERE CPF.ID_FILIAL = FILORIGEM.ID_FILIAL AND CPF.ID_PARAMETRO_FILIAL = PF.ID_PARAMETRO_FILIAL AND CPF.VL_CONTEUDO_PARAMETRO_FILIAL = 'S' AND PF.NM_PARAMETRO_FILIAL = 'ATIVA_NFE_CONJUGADA') THEN 'RPS' ELSE 'NFE' END) END)", SQL_TP_DOCUMENTO_SERVICO);

		sqlSub.addFrom(HQL_DEVEDOR_DOC_SERV_FAT, "DEV");
		sqlSub.addFrom(HQL_DOCTO_SERVICO, "DOC");
		sqlSub.addFrom(HQL_FILIAL, "FILORIGEM");
		sqlSub.addFrom(SQL_DESCONTO, "DES");
		sqlSub.addFrom(HQL_MONITORAMENTO_DOC_ELETRONICO, "MDC");

		sqlSub.addJoin(HQL_DOC_ID_DOCTO_SERVICO, "DEV.ID_DOCTO_SERVICO");
		sqlSub.addJoin(HQL_DOC_ID_FILIAL_ORIGEM, "FILORIGEM.ID_FILIAL");
		sqlSub.addJoin(HQL_DEV_ID_DEVEDOR_DOC_SERV_FAT, "DES.ID_DEVEDOR_DOC_SERV_FAT(+)");
		sqlSub.addJoin(HQL_DOC_ID_DOCTO_SERVICO, "MDC.ID_DOCTO_SERVICO (+)");
		sqlSub.addCustomCriteria("DES.TP_SITUACAO_APROVACAO(+) <> 'E'");
		sqlSub.addCriteria(HQL_DEV_ID_FATURA, "=", idFatura);
		sqlSub.addCriteria(HQL_DEV_ID_FILIAL, "=", idFilial);

		sqlSub.addOrderBy(HQL_FILORIGEM_SG_FILIAL);
		sqlSub.addOrderBy(HQL_DOC_NR_DOCTO_SERVICO);

		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(SQL_ROWNUM);
		sql.addProjection("SUB.*");
		sql.addFrom("(" + sqlSub.getSql() + ")", "SUB");
		sql.addOrderBy(SQL_ROWNUM); // FORCA ORDENACAO SEQUENCIAL LMS-592
		sql.addCriteriaValue(sqlSub.getCriteria());

		return sql;
	}


	/**
	 * Atualiza cada fatura
	 *
	 * @param criteria TypedFlatMap
	 * @author Mickaël Jalbert
	 * @since 06/03/2007
	 */
	public void updateFatura(TypedFlatMap criteria) {
		boolean reemissao = criteria.get(REEMITIR) != null && criteria.getBoolean(REEMITIR);
		SqlTemplate sql = new SqlTemplate();
		StringBuilder sqlUpdate = new StringBuilder();

		if (reemissao) {
			sql.addCriteriaValue(JTDateTimeUtils.getDataHoraAtual());
			mountSqlUpdate(sql, criteria);
			sqlUpdate.append("UPDATE 	FATURA " +
					"SET 		BL_FATURA_REEMITIDA = 'S', " +
					"			ID_USUARIO = " + SessionUtils.getUsuarioLogado().getIdUsuario() + ", " +
					"			DH_REEMISSAO = ? " +
					"WHERE ID_FATURA IN (" + sql.getSql() + ") ");
		} else {
			mountSqlUpdate(sql, criteria);
			sqlUpdate.append("UPDATE 	FATURA " +
					"SET 	BL_INDICADOR_IMPRESSAO = 'S', " +
					"		TP_SITUACAO_FATURA = DECODE(TP_SITUACAO_FATURA, 'DI', 'EM', TP_SITUACAO_FATURA) " +
					"WHERE ID_FATURA IN (" + sql.getSql() + ")");
		}

		//should I alterar essa chamada?
		getJdbcTemplate().update(sqlUpdate.toString(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()));
	}

	private void mountSqlUpdate(SqlTemplate sql, TypedFlatMap criteria) {
		sql.addProjection(HQL_F_ID_FATURA);
		sql.addFrom("FATURA", "F");

		mountFilter(criteria, sql, false);

	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * Retorna uma instância de <code>com.mercurio.adsm.framework.util.SqlTemplate</code>, configurada
	 * para utilizar o <code>MessageSource</code> do Spring Framework.
	 *
	 * @return SqlTemplate
	 */
	public static SqlTemplate createSqlTemplate() {
		SqlTemplate sqlTemplate = new SqlTemplate();
		sqlTemplate.setMessageSource(messageSource);

		return sqlTemplate;

	}

	public FaturaDellDMN findFaturasDellByIdFatura(Long idFatura) {

		Object[] arr = new Object[]{idFatura};
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT P.NR_IDENTIFICACAO, F.TP_SITUACAO_FATURA, F.ID_FATURA, F.DT_EMISSAO, F.DT_VENCIMENTO, DS.TP_DOCUMENTO_SERVICO ");
		sql.append(" FROM LMS_PD.FATURA F, ");
		sql.append(" LMS_PD.ITEM_FATURA IF, ");
		sql.append(" LMS_PD.DEVEDOR_DOC_SERV_FAT DDSF, ");
		sql.append(" LMS_PD.DOCTO_SERVICO DS, ");
		sql.append(" LMS_PD.PESSOA P ");
		sql.append(" WHERE F.ID_CLIENTE = P.ID_PESSOA AND F.TP_SITUACAO_FATURA = 'EM' AND F.ID_FATURA = ? ");
		sql.append(" AND F.ID_FATURA = IF.ID_FATURA AND IF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT AND DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND ROWNUM = 1 ");

		List<FaturaDellDMN> fatura = jdbcTemplate.query(sql.toString(), arr,
				(resultSet, row) -> {
					FaturaDellDMN fdd = new FaturaDellDMN();
					fdd.setNrIdentificacaoCliente(resultSet.getString("nr_identificacao"));
					fdd.setTpSituacaoFatura(resultSet.getString("tp_situacao_fatura"));
					fdd.setIdFatura(resultSet.getLong("id_fatura"));
					fdd.setDtEmissao(YearMonthDay.fromDateFields(resultSet.getDate("dt_emissao")));
					fdd.setDtVencimento(YearMonthDay.fromDateFields(resultSet.getDate("dt_vencimento")));
					fdd.setTpDocumentoServico(resultSet.getString("tp_documento_servico"));
					return fdd;
				});
		return fatura.get(0);
	}

	public FaturaD2l findDadosFatura(Long idFatura){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NR_FATURA, ID_CLIENTE, DT_EMISSAO, PES.NR_IDENTIFICACAO NR_DEST, FAT.VL_TOTAL, PEF.NR_IDENTIFICACAO NR_EMIT,	fat.dt_vencimento ");
		sql.append("FROM LMS_PD.FATURA FAT, ");
		sql.append("LMS_PD.PESSOA PES, ");
		sql.append("LMS_PD.PESSOA PEF ");
		sql.append("WHERE FAT.ID_CLIENTE = PES.ID_PESSOA ");
		sql.append("AND FAT.ID_FILIAL = PEF.ID_PESSOA ");
		sql.append("AND FAT.ID_FATURA = ?");
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		List<FaturaD2l> result = jdbcTemplate.query(sql.toString(), new Object[]{idFatura}, (resultSet, row) ->{
			FaturaD2l dadosFatura = new FaturaD2l();
			dadosFatura.setNumeroDocumentoFiscal(resultSet.getString("nr_fatura"));
			dadosFatura.setDataEmissaoDocumentoFiscal(dateFormat.format(resultSet.getDate("dt_emissao")));
			dadosFatura.setCnpjDestinatarioDof(resultSet.getString("nr_dest"));
			dadosFatura.setValorTotalItensDof(resultSet.getDouble("vl_total"));
			dadosFatura.setCnpjEmitenteDof(resultSet.getString("nr_emit"));
			dadosFatura.setCnpjRemetenteDof(resultSet.getString("nr_emit"));
			dadosFatura.setIdCliente(resultSet.getString("ID_CLIENTE"));
			return dadosFatura;
		});
		return result.get(0);
	}
	public FaturaDetalheD2l findDadosDetalheFatura(Long idFatura) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT NR_FATURA," );
		sql.append("       DT_EMISSAO," );
		sql.append("       PES.NR_IDENTIFICACAO NR_DEST," );
		sql.append("    	FAT.VL_TOTAL," );
		sql.append("       PEF.NR_IDENTIFICACAO NR_EMIT," );
		sql.append("       fat.dt_vencimento, " );
		sql.append("       fat.dt_emissao" );
		sql.append("  FROM LMS_PD.FATURA FAT," );
		sql.append("       LMS_PD.PESSOA PES," );
		sql.append("       LMS_PD.PESSOA PEF" );
		sql.append(" WHERE FAT.ID_CLIENTE = PES.ID_PESSOA" );
		sql.append("   AND FAT.ID_FILIAL = PEF.ID_PESSOA" );
		sql.append("   AND FAT.ID_FATURA = ?");
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		List<FaturaDetalheD2l> result = jdbcTemplate.query(sql.toString(), new Object[]{idFatura}, (rs, row)->{
			FaturaDetalheD2l dadosFatura = new FaturaDetalheD2l();
			dadosFatura.setNumeroDOF(rs.getString("nr_fatura"));
			dadosFatura.setDataEmissaoDOF(dateFormat.format(rs.getDate("dt_emissao")));
			dadosFatura.setCnpjDestinatarioDOF(rs.getString("nr_dest"));
			dadosFatura.setValorParcela(rs.getDouble("vl_total"));
			dadosFatura.setCnpjEmitenteDOF(rs.getString("nr_emit"));
			dadosFatura.setDataEmissaoParcela(dateFormat.format(rs.getDate("dt_vencimento")));
			dadosFatura.setDataVencimentoParcela(dateFormat.format(rs.getDate("dt_vencimento")));
			return dadosFatura;
		});
		return result.get(0);
	}

	public List<DadosConhecimentoD2l> findDadosConhecimentoCtr(Long idFatura){
		StringBuilder sql = new StringBuilder();
		sql.append(	"SELECT ds.nr_docto_servico, " );
		sql.append("	c.ds_serie, " );
		sql.append("	ds.dh_emissao, ");
		sql.append("	ds.nr_cfop, " );
		sql.append("	PF_O.NR_IDENTIFICACAO NR_FIL_ORIG, " );
		sql.append("	PC_R.NR_IDENTIFICACAO NR_CLI_REM, " );
		sql.append("	PC_D.NR_IDENTIFICACAO NR_CLI_DEST, " );
		sql.append("	DS.VL_TOTAL_DOC_SERVICO, " );
		sql.append("	DS.VL_BASE_CALC_IMPOSTO, " );
		sql.append("	DS.VL_ICMS_ST, " );
		sql.append("	DDSF.VL_DEVIDO, " );
		sql.append("	(SELECT (pg.DS_CONTEUDO * DDSF.VL_DEVIDO)/100 FROM lms_pd.PARAMETRO_GERAL pg  WHERE pg.NM_PARAMETRO_GERAL = 'PC_PIS_INTEGRACAO_DELL')	 VL_PIS, " );
		sql.append("	(SELECT (pg.DS_CONTEUDO * DDSF.VL_DEVIDO)/100 FROM lms_pd.PARAMETRO_GERAL pg  WHERE pg.NM_PARAMETRO_GERAL = 'PC_COFINS_INTEGRACAO_DELL') 	VL_COFINS, " );
		sql.append("	DS.QT_VOLUMES, " );
		sql.append("	DS.PS_REFERENCIA_CALCULO, " );
		sql.append("	C.TP_FRETE, " );
		sql.append("	(SELECT VL_PARCELA_BRUTO " );
		sql.append("		FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, " );
	    sql.append("			LMS_PD.PARCELA_PRECO pp " );
		sql.append("			WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO " );
		sql.append("				AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("				AND pp.CD_PARCELA_PRECO = 'IDFretePeso')			 VL_FRETE_PESO, " );
		sql.append("	(SELECT VL_PARCELA_BRUTO " );
		sql.append("		FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, " );
		sql.append("			LMS_PD.PARCELA_PRECO pp " );
		sql.append("			WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO " );
		sql.append("				AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
    	sql.append("		    	AND pp.CD_PARCELA_PRECO = 'IDCat') 					VL_CAT, " );
	    sql.append("	(SELECT VL_PARCELA_BRUTO " );
		sql.append("		FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, " );
		sql.append("			LMS_PD.PARCELA_PRECO pp " );
		sql.append("			WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO " );
		sql.append("				AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("				AND pp.CD_PARCELA_PRECO = 'IDAgendamentoEntrega') 	VL_AGENDAMENTO_ENTREGA, " );
		sql.append("	(SELECT VL_PARCELA_BRUTO " );
		sql.append("		FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, " );
		sql.append("			LMS_PD.PARCELA_PRECO pp " );
		sql.append("			WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO " );
		sql.append("				AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("				AND pp.CD_PARCELA_PRECO = 'IDDespacho') 			VL_DESPACHO, " );
		sql.append("	(SELECT VL_PARCELA_BRUTO " );
		sql.append("		FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, " );
		sql.append("			LMS_PD.PARCELA_PRECO pp " );
		sql.append("			WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO " );
		sql.append("				AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("				AND pp.CD_PARCELA_PRECO = 'IDPedagio') 				VL_PEDAGIO, " );
		sql.append("	(SELECT VL_PARCELA_BRUTO " );
		sql.append("		FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, " );
		sql.append("			LMS_PD.PARCELA_PRECO pp " );
		sql.append("			WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO " );
		sql.append("				AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("				AND pp.CD_PARCELA_PRECO = 'IDGris') 				VL_GRIS, " );
		sql.append("	SUBSTR(C.TP_CONHECIMENTO,1,1) TP_CONHECIMENTO, " );
		sql.append("	MDE.NR_CHAVE, " );
		sql.append("	(SELECT UPPER(VD.DS_VALOR_DOMINIO_I) " );
		sql.append("		FROM lms_pd.VALOR_DOMINIO VD, lms_pd.DOMINIO D " );
		sql.append("		WHERE VD.ID_DOMINIO = D.ID_DOMINIO " );
		sql.append("			AND NM_DOMINIO = 'DM_SITUACAO_DOC_ELETRONICO' " );
		sql.append("			AND VD.VL_VALOR_DOMINIO = mde.TP_SITUACAO_DOCUMENTO ) 	DS_SITUACAO_DOC " );
		sql.append("FROM LMS_PD.FATURA FAT, " );
		sql.append("	LMS_PD.ITEM_FATURA ITF, " );
		sql.append("	LMS_PD.DEVEDOR_DOC_SERV_FAT DDSF, " );
		sql.append("	LMS_PD.docto_servico ds, " );
		sql.append("	lms_pd.conhecimento c, " );
		sql.append("	LMS_PD.PESSOA PF_O, " );
		sql.append("	LMS_PD.PESSOA PC_R, " );
		sql.append("	LMS_PD.PESSOA PC_D, " );
		sql.append("	LMS_PD.MONITORAMENTO_DOC_ELETRONICO mde " );
		sql.append("		where FAT.ID_FATURA = ITF.ID_FATURA " );
		sql.append("			AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " );
		sql.append("			AND DDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO " );
		sql.append("			AND ds.id_docto_servico = c.id_conhecimento " );
		sql.append("			and PF_O.ID_PESSOA = DS.ID_FILIAL_ORIGEM " );
		sql.append("			AND PC_R.ID_PESSOA = DS.ID_CLIENTE_REMETENTE " );
		sql.append("			AND PC_D.ID_PESSOA = ds.id_cliente_destinatario " );
		sql.append("			AND MDE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("			AND DS.TP_DOCUMENTO_SERVICO = 'CTE' " );
		sql.append("			AND FAT.ID_FATURA = ?");

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		List<DadosConhecimentoD2l> result = jdbcTemplate.query(sql.toString(), new Object[]{idFatura}, (rs, row) ->{
			DadosConhecimentoD2l dadosConhecimentoCTRC = new DadosConhecimentoD2l();
			dadosConhecimentoCTRC.setNumeroDocumentoFiscal(rs.getString("nr_docto_servico"));
			dadosConhecimentoCTRC.setSerieDocumentoFiscal(rs.getString("ds_serie"));
			dadosConhecimentoCTRC.setDataEmissaoDocumentoFiscal(dateFormat.format(rs.getDate("dh_emissao")));
			dadosConhecimentoCTRC.setCfopDocumentoFiscal(String.format("%,d", Long.valueOf(rs.getString("nr_cfop"))));
			dadosConhecimentoCTRC.setCnpjEmitenteDof(rs.getString("nr_fil_orig"));
			dadosConhecimentoCTRC.setCnpjRemetenteDof(rs.getString("nr_cli_rem"));
			dadosConhecimentoCTRC.setCnpjDestinatarioDof(rs.getString("nr_cli_dest"));
			dadosConhecimentoCTRC.setValorTotalItensDof(rs.getDouble("vl_total_doc_servico"));
			dadosConhecimentoCTRC.setBaseCalculoICMS(rs.getDouble("vl_base_calc_imposto"));
			dadosConhecimentoCTRC.setValorIcms(rs.getDouble("vl_icms_st"));
			dadosConhecimentoCTRC.setValorBrutoDof(rs.getDouble("vl_total_doc_servico"));
			dadosConhecimentoCTRC.setBaseCalculoPis(rs.getDouble("vl_devido"));
			dadosConhecimentoCTRC.setValorRetencaoPis(rs.getDouble("vl_pis"));
			dadosConhecimentoCTRC.setBaseCalculoCofins(rs.getDouble("vl_devido"));
			dadosConhecimentoCTRC.setValorRetencaoCofins(rs.getDouble("vl_cofins"));
			dadosConhecimentoCTRC.setNumeroVolumes(rs.getInt("qt_volumes"));
			dadosConhecimentoCTRC.setPesoLiquido(rs.getDouble("ps_referencia_calculo"));
			dadosConhecimentoCTRC.setPesoBruto(rs.getDouble("ps_referencia_calculo"));
			dadosConhecimentoCTRC.setCondicaoFrete(rs.getString("tp_frete"));
			dadosConhecimentoCTRC.setPesoTransportado(rs.getDouble("ps_referencia_calculo"));
			dadosConhecimentoCTRC.setValorFretePesoVolume(rs.getDouble("vl_frete_peso"));
			dadosConhecimentoCTRC.setValorSecCat(rs.getDouble("vl_cat"));
			dadosConhecimentoCTRC.setValorItr(rs.getDouble("vl_agendamento_entrega"));
			dadosConhecimentoCTRC.setValorDespacho(rs.getDouble("vl_despacho"));
			dadosConhecimentoCTRC.setValorPedagio(rs.getDouble("vl_pedagio"));
			dadosConhecimentoCTRC.setValorAdeme(rs.getDouble("vl_gris"));
			dadosConhecimentoCTRC.setTipoConhecimento(rs.getString("TP_CONHECIMENTO"));
			dadosConhecimentoCTRC.setNfeLocalizador(rs.getString("NR_CHAVE"));
			String sit = rs.getString("DS_SITUACAO_DOC");
			sit = sit.replace("PT_BR","");
			sit = sit.replace("¦","");
			sit = sit.replace("»","");
			dadosConhecimentoCTRC.setSituacaoNfEletronica(sit);

			return dadosConhecimentoCTRC;
		});
		return result;
	}

	public List<FaturaConhecimentoD2l> findRelacionaFaturaConhecimento(Long idFatura){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT PES.NR_IDENTIFICACAO NR_EMIT_FAT, ");
		sql.append("	FAT.NR_FATURA, " );
		sql.append("	FAT.DT_EMISSAO, " );
		sql.append("	PES2.NR_IDENTIFICACAO NR_EMIT_CTE, " );
		sql.append("	C.DS_SERIE, " );
		sql.append("	DS.NR_DOCTO_SERVICO, " );
		sql.append("	DS.DH_EMISSAO " );
		sql.append("FROM LMS_PD.FATURA FAT, " );
		sql.append("	LMS_PD.ITEM_FATURA ITF, " );
		sql.append("	LMS_PD.DEVEDOR_DOC_SERV_FAT DDSF, " );
		sql.append("	LMS_PD.DOCTO_SERVICO DS, " );
		sql.append("	LMS_PD.PESSOA PES, " );
		sql.append("	LMS_PD.PESSOA PES2, " );
		sql.append("	LMS_PD.CONHECIMENTO c " );
		sql.append("	WHERE FAT.ID_FATURA = ITF.ID_FATURA " );
		sql.append("		AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " );
		sql.append("		AND DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " );
		sql.append("		AND DS.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO " );
		sql.append("		AND FAT.ID_FILIAL = PES.ID_PESSOA " );
		sql.append("		AND DS.ID_FILIAL_ORIGEM = PES2.ID_PESSOA " );
		sql.append("		AND FAT.ID_FATURA = ?");
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		List<FaturaConhecimentoD2l> result = jdbcTemplate.query(sql.toString(), new Object[]{idFatura}, (rs, row) -> {
			FaturaConhecimentoD2l relacionaFaturaConhecimento  = new FaturaConhecimentoD2l();
			relacionaFaturaConhecimento.setCnpjEmissorDocumentoPai(rs.getString("nr_emit_fat"));
			relacionaFaturaConhecimento.setNumeroDocumentoPai(rs.getString("nr_fatura"));
			relacionaFaturaConhecimento.setDataEmissaoDocumentoPai(dateFormat.format(rs.getDate("dt_emissao")));

			relacionaFaturaConhecimento.setCnpjEmissorDocumentoFilho(rs.getString("NR_EMIT_CTE"));
			relacionaFaturaConhecimento.setSerieDocumentoFilho(rs.getString("ds_serie"));
			relacionaFaturaConhecimento.setNumeroDocumentoFilho(rs.getString("nr_docto_servico"));
			relacionaFaturaConhecimento.setDataEmissaoDocumentoFilho(dateFormat.format(rs.getDate("dh_emissao")));
			return relacionaFaturaConhecimento;
		});
		return result;
	}

	public List<ConhecimentoNotaFiscalD2l> findRelacionaConhecimentoNotaFiscal(Long idFatura){
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PES.NR_IDENTIFICACAO NR_EMIT_CTE, ");
		sql.append(" NVL(C.DS_SERIE,0) DS_SERIE,");
		sql.append(" DS.NR_DOCTO_SERVICO ,");
		sql.append(" DS.DH_EMISSAO ,");
		sql.append(" PES2.NR_IDENTIFICACAO NR_EMIT_NFC,");
		sql.append(" NFC.DS_SERIE DS_SERIE_NFC ,");
		sql.append(		" NFC.NR_NOTA_FISCAL ,");
		sql.append(" NFC.DT_EMISSAO ,");
		sql.append(" 		NFC.VL_TOTAL");
		sql.append(" FROM LMS_PD.FATURA FAT,");
		sql.append(" LMS_PD.ITEM_FATURA ITF,");
		sql.append(" LMS_PD.DEVEDOR_DOC_SERV_FAT DDSF,");
		sql.append(" LMS_PD.DOCTO_SERVICO DS,");
		sql.append(" LMS_PD.PESSOA PES,");
		sql.append(" LMS_PD.PESSOA pES2,");
		sql.append(" LMS_PD.CONHECIMENTO c,");
		sql.append(" LMS_PD.NOTA_FISCAL_CONHECIMENTO nfc");
		sql.append(" WHERE FAT.ID_FATURA = ITF.ID_FATURA");
		sql.append(" AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT");
		sql.append(" AND DDSF.ID_DOCTO_SERVICO  = DS.ID_DOCTO_SERVICO");
		sql.append(" AND DS.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO");
		sql.append(" AND DS.ID_FILIAL_ORIGEM = PES.ID_PESSOA");
		sql.append(" AND C.ID_CONHECIMENTO = NFC.ID_CONHECIMENTO");
		sql.append(" AND PES2.ID_PESSOA = NFC.ID_CLIENTE");
		sql.append(" AND FAT.ID_FATURA = ?");

		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		List<ConhecimentoNotaFiscalD2l> resp = jdbcTemplate.query(sql.toString(), new Object[]{idFatura}, (rs, rownum) -> {
			ConhecimentoNotaFiscalD2l rcnf = new ConhecimentoNotaFiscalD2l();
			rcnf.setCnpjEmissorDocumentoPai(String.valueOf(rs.getLong("nr_emit_cte")));
			rcnf.setSerieDocumentoPai(String.valueOf(rs.getLong("ds_serie")));
			rcnf.setNumeroDocumentoPai(String.valueOf(rs.getLong("nr_docto_servico")));
			rcnf.setCnpjEmissorDocumento(rs.getString("nr_emit_nfc"));
			rcnf.setSerieDocumentoFilho(String.valueOf(rs.getLong("ds_serie_nfc")));
			rcnf.setNumeroDocumentoFilho(String.valueOf(rs.getLong("nr_nota_fiscal")));
			rcnf.setValorNotaFiscal(rs.getDouble("vl_total"));
			rcnf.setDataEmissaoDocumentoPai(dateFormat.format(rs.getDate("dh_emissao")));
			rcnf.setDataEmissaoDocumentoFilho(dateFormat.format(rs.getDate("dt_emissao")));
			return rcnf;
		});
		return resp;
	}

	public List<Tipo07> findTipo7(Long idFatura){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DS.ID_SERVICO, FAT.ID_FATURA, ");
		sql.append("CASE WHEN C.TP_FRETE = 'C' THEN 1 ");
		sql.append("     WHEN C.TP_FRETE = 'F' THEN 2 END TP_FRETE, ");
		sql.append("CASE WHEN FAT.TP_MODAL = 'A' THEN 'AIR' ");
		sql.append("     WHEN FAT.TP_MODAL = 'R' THEN 'LTL' END TP_MODAL, ");
		sql.append("CASE WHEN C.TP_CONHECIMENTO = 'NO' THEN 'BRRC0001' ");
		sql.append("     ELSE 'BRRC0002' END DS_CONTA_MATRIZ, ");
		sql.append("DS.NR_DOCTO_SERVICO, ");
		sql.append("(SELECT MAX(NFC.DT_EMISSAO) FROM lms_pd.NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = C.ID_CONHECIMENTO) DT_EMISSAO, ");
		sql.append("DS.PS_CUBADO_AFERIDO, ");
		sql.append("PESR.NM_PESSOA NM_PESSOA_R, ");
		sql.append("EPR.DS_ENDERECO || ',' || EPR.NR_ENDERECO DS_ENDERECO_R, ");
		sql.append("MUNR.NM_MUNICIPIO NM_MUNICIPIO_R, ");
		sql.append("UFR.SG_UNIDADE_FEDERATIVA UF_R, ");
		sql.append("PESR.NR_IDENTIFICACAO NR_IDENT_R, ");
		sql.append("IER.NR_INSCRICAO_ESTADUAL NR_IE_R, ");
		sql.append("PESD.NM_PESSOA NM_DEST, ");
		sql.append("EPD.DS_ENDERECO || ',' || EPD.NR_ENDERECO DS_ENDERECO_D, " );
		sql.append("MUND.NM_MUNICIPIO NM_MUNICIPIO_D, " );
		sql.append("UFD.SG_UNIDADE_FEDERATIVA UF_D, ");
		sql.append( "PESD.NR_IDENTIFICACAO NR_IDENT_D, ");
		sql.append("IED.NR_INSCRICAO_ESTADUAL NR_IE_D, ");
		sql.append("DS.PC_ALIQUOTA_ICMS, " );
		sql.append("DDSF.VL_DEVIDO, ");
		sql.append("(SELECT pg.DS_CONTEUDO FROM lms_pd.PARAMETRO_GERAL pg WHERE pg.NM_PARAMETRO_GERAL = 'PC_PIS_INTEGRACAO_DELL') PC_PIS, ");
		sql.append("(SELECT pg.DS_CONTEUDO FROM lms_pd.PARAMETRO_GERAL pg WHERE pg.NM_PARAMETRO_GERAL = 'PC_COFINS_INTEGRACAO_DELL') PC_COFINS, ");
		sql.append("DS.VL_TOTAL_DOC_SERVICO, ");
		sql.append("(SELECT sum(pds.VL_PARCELA_BRUTO) FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO in ('IDAdvalorem','IDAdvalorem1','IDAdvalorem2')) VL_ADVALOREM, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDTde') VL_TDE, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDTas') VL_TAS, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDTaxaPaletizacao') VL_PALETIZACAO, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDEntregaHorarioEspecial') VL_HORARIO_ESPECIAL, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDRetornoCanhotoNF') VL_RETORNO_CANHOTO, ");
		sql.append("(SELECT CASE WHEN S.id_tipo_servico = 3 THEN 'EM' ");
		sql.append("             WHEN TP_MODAL = 'A' THEN 'AE' ");
		sql.append("             WHEN TP_MODAL = 'R' THEN 'GD' END ");
		sql.append(" FROM LMS_PD.SERVICO S WHERE S.ID_SERVICO = DS.ID_SERVICO) TP_SERVICO, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDDespacho') VL_DESPACHO, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDGris') VL_GRIS, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDPedagio') VL_PEDAGIO, ");
		sql.append("(SELECT VL_PARCELA_BRUTO FROM LMS_PD.PARCELA_DOCTO_SERVICO pds, LMS_PD.PARCELA_PRECO pp WHERE pds.ID_PARCELA_PRECO = pp.ID_PARCELA_PRECO AND PDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND pp.CD_PARCELA_PRECO = 'IDFretePeso') VL_FRETE_PESO, ");
		sql.append("FAT.NR_FATURA, ");
		sql.append("FAT.DT_EMISSAO, ");
		sql.append("FAT.DT_VENCIMENTO, ");
		sql.append("PESFE.NR_IDENTIFICACAO NR_EMIT_CTE, ");
		sql.append("DS.DH_EMISSAO, ");
		sql.append("DS.NR_DOCTO_SERVICO, ");
		sql.append("F.SG_FILIAL || TO_CHAR(DS.DH_EMISSAO, 'rrmmdd') || LPAD(DS.NR_DOCTO_SERVICO, 9, 0) FILIAL_DT_NR_SERV ");
		sql.append("FROM LMS_PD.FATURA FAT, ");
		sql.append("LMS_PD.ITEM_FATURA ITF, ");
		sql.append("LMS_PD.DEVEDOR_DOC_SERV_FAT DDSF, ");
		sql.append("LMS_PD.DOCTO_SERVICO DS, ");
		sql.append("LMS_PD.PESSOA PESR, ");
		sql.append("LMS_PD.INSCRICAO_ESTADUAL IER, ");
		sql.append("LMS_PD.CONHECIMENTO C, ");
		sql.append("LMS_PD.ENDERECO_PESSOA EPR, ");
		sql.append("LMS_PD.MUNICIPIO MUNR, ");
		sql.append("LMS_PD.UNIDADE_FEDERATIVA UFR, ");
		sql.append("LMS_PD.PESSOA PESD, ");
		sql.append("LMS_PD.INSCRICAO_ESTADUAL IED, ");
		sql.append("LMS_PD.ENDERECO_PESSOA EPD, ");
		sql.append("LMS_PD.MUNICIPIO MUND, ");
		sql.append("LMS_PD.UNIDADE_FEDERATIVA UFD, ");
		sql.append("LMS_PD.PESSOA PESFE, ");
		sql.append("LMS_PD.FILIAL F ");
		sql.append("WHERE FAT.ID_FATURA = ITF.ID_FATURA ");
		sql.append("AND ITF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT ");
		sql.append("AND DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sql.append("AND DS.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO ");
		sql.append("AND DS.ID_CLIENTE_REMETENTE = PESR.ID_PESSOA ");
		sql.append("AND DS.ID_IE_REMETENTE = IER.ID_INSCRICAO_ESTADUAL ");
		sql.append("AND PESR.ID_ENDERECO_PESSOA = EPR.ID_ENDERECO_PESSOA ");
		sql.append("AND EPR.ID_MUNICIPIO = MUNR.ID_MUNICIPIO ");
		sql.append("AND MUNR.ID_UNIDADE_FEDERATIVA = UFR.ID_UNIDADE_FEDERATIVA ");
		sql.append("AND DS.ID_IE_DESTINATARIO = IED.ID_INSCRICAO_ESTADUAL ");
		sql.append("AND DS.ID_CLIENTE_DESTINATARIO = PESD.ID_PESSOA ");
		sql.append("AND PESD.ID_ENDERECO_PESSOA = EPD.ID_ENDERECO_PESSOA ");
		sql.append("AND EPD.ID_MUNICIPIO = MUND.ID_MUNICIPIO ");
		sql.append("AND MUND.ID_UNIDADE_FEDERATIVA = UFD.ID_UNIDADE_FEDERATIVA ");
		sql.append("AND DS.TP_DOCUMENTO_SERVICO = 'CTE' ");
		sql.append("AND DS.ID_FILIAL_ORIGEM = PESFE.ID_PESSOA ");
		sql.append("AND DS.ID_FILIAL_ORIGEM = F.ID_FILIAL ");
		sql.append("AND FAT.ID_FATURA = ?");
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		List<Tipo07> resp = jdbcTemplate.query(sql.toString(), new Object[]{idFatura}, (rs, rownum)->{
			Tipo07 tp07 = new Tipo07();

			tp07.setTipoTransporte(rs.getInt("tp_frete"));
			tp07.setTipoModal(rs.getString("tp_modal"));
			tp07.setTipoFaturaDell(rs.getInt("tp_frete"));
			tp07.setContaMatrizDell(rs.getString("ds_conta_matriz"));
			tp07.setNumeroOrdemDell(rs.getString("nr_docto_servico"));
			tp07.setPesoCubado(rs.getDouble("ps_cubado_aferido"));
			tp07.setRazaoSocialDell(rs.getString("nm_pessoa_r"));
			tp07.setEnderecoOrigem1(rs.getString("ds_endereco_r"));
			tp07.setCidade(rs.getString("nm_municipio_r"));
			tp07.setEstado(rs.getString("uf_r"));
			tp07.setCnpjRemetente(rs.getString("nr_ident_r"));
			tp07.setIeRemetente(rs.getString("nr_ie_r"));
			tp07.setNomeEntrega(rs.getString("nm_dest"));
			tp07.setEnderecoEntrega1(rs.getString("ds_endereco_d"));
			tp07.setCidadeEntrega(rs.getString("nm_municipio_d"));
			tp07.setEstadoEntrega(rs.getString("uf_d"));
			tp07.setCnpjDestino(rs.getString("nr_ident_d"));
			tp07.setInscricaoEstadual(rs.getString("nr_ie_d"));
			tp07.setAliquotaIcms(rs.getDouble("pc_aliquota_icms"));
			tp07.setValorPis(Double.parseDouble(rs.getString("pc_pis").replace(",",".")));
			tp07.setValorCofins(Double.parseDouble(rs.getString("pc_cofins").replace(",",".")));
			tp07.setValorTotalCTE(rs.getDouble("vl_total_doc_servico"));
			tp07.setValorAdValorem(rs.getDouble("vl_advalorem"));
			tp07.setValorTde(rs.getDouble("vl_tde"));
			tp07.setValorTas(rs.getDouble("vl_tas"));
			tp07.setValorPallet(rs.getDouble("vl_paletizacao"));
			tp07.setValorSabadoDomingo(rs.getDouble("vl_horario_especial"));
			tp07.setValorTaxaComprovante(rs.getDouble("vl_retorno_canhoto"));
			tp07.setTipoServico(rs.getString("tp_servico"));
			tp07.setValorDespacho(rs.getDouble("vl_despacho"));
			tp07.setVlDevido(rs.getDouble("vl_devido"));
			tp07.setValorTotalGris(rs.getDouble("vl_gris"));
			tp07.setValorTotalPedagio(rs.getDouble("vl_pedagio"));
			tp07.setIdFatura(rs.getInt("id_fatura"));
			tp07.setIdServico(rs.getInt("id_servico"));
			tp07.setVlFretePeso(rs.getDouble(	"vl_frete_peso"));
			tp07.setNumeroFatura(rs.getString("nr_fatura"));
			tp07.setDataColeta(dateFormat.format(rs.getDate("dt_emissao")));
			tp07.setEmissaoFatura(dateFormat.format(rs.getDate("dt_emissao")));
			tp07.setVencimentoFatura(dateFormat.format(rs.getDate("dt_vencimento")));
			tp07.setDhEmissao(dateFormat.format(rs.getDate("dh_emissao")));
			tp07.setNrEmitCte(rs.getString("nr_emit_cte"));
			tp07.setFilialEmissoraCTE(rs.getString("filial_dt_nr_serv"));
			return tp07;
		});
		return resp;
	}

}
