package com.mercurio.lms.expedicao.model.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.QueryException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.transform.ResultTransformer;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import br.com.tntbrasil.integracao.domains.sim.EventoDocumentoServicoDMN;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.util.DateTimeUtils;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.XMLUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

@SuppressWarnings("deprecation")
public class MonitoramentoDocEletronicoDAO extends BaseCrudDao<MonitoramentoDocEletronico, Long>{

	private Logger log = LogManager.getLogger(MonitoramentoDocEletronicoDAO.class);
	private static final int DIAS_REF = 60;

	@SuppressWarnings("rawtypes")
    @Override
	protected Class getPersistentClass() {
		return MonitoramentoDocEletronico.class;
	}


	//TODO verificar isso com o Eri, pois mesmo com os joins abaixo ainda pode ter em uma mesma filial dois
	//doctoServico com o mesmo numero e tipos diferentes ex. 123 NSE e 123 NTE, ou filiais diferentes com o mesmo numero de doctoSErvico
	// Isso ocasionaria um problema em
	//MonitoramentoDocEletronicoService.atualizaStatusMonitoramentoEletronico, pois lá a pesquisa é feita pelo
	//nr do doctoServico.
	//como alternativa poderiamos utilizar o id do doctoservico no rpsNumero

	@SuppressWarnings({ "unchecked" })
    public List<MonitoramentoDocEletronico> findMonitoramentoEletronicoByTpSituacaoDocumento(String tpSituacaoDocumento){
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(getPersistentClass().getName() + " monitoramentoDocEletronico ");
		sql.addInnerJoin("monitoramentoDocEletronico.doctoServico", "doctoServico" );
		sql.addInnerJoin("doctoServico.filialByIdFilialOrigem", "filialOrigem" );
		sql.addCriteria("monitoramentoDocEletronico.tpSituacaoDocumento", "=", tpSituacaoDocumento);
		sql.addCustomCriteria("doctoServico.tpDocumentoServico in ('NTE', 'NSE') ");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}


    public MonitoramentoDocEletronico findMonitoramentoDocEletronicoByIdDoctoServicoAndTpSituacaoDocumento(Long idDoctoServico, String tpSituacaoDocumento){
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName() + " monitoramentoDocEletronico");
		hql.addCriteria("monitoramentoDocEletronico.doctoServico.id", "=", idDoctoServico);
		hql.addCriteria("monitoramentoDocEletronico.tpSituacaoDocumento", "=", tpSituacaoDocumento);

		return (MonitoramentoDocEletronico)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

    public boolean findExistsMonitoramentoDocEletronicoByIdDoctoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("count(*)");
		hql.addFrom(getPersistentClass().getName() + " monitoramentoDocEletronico");
		hql.addCriteria("monitoramentoDocEletronico.doctoServico.id", "=", idDoctoServico);
		Object result = getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		if( result == null ){
			result = 0L;
		}
		return (Long)result > 0;
	}


    public MonitoramentoDocEletronico findMonitoramentoDocEletronicoByIdDoctoServico(final Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName() + " monitoramentoDocEletronico");
		hql.addCriteria("monitoramentoDocEletronico.doctoServico.id", "=", idDoctoServico);

		return (MonitoramentoDocEletronico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}



    public Integer getRowCountMonitoramentoDocEletronico(TypedFlatMap criteria) {
		SqlTemplate sql = this.getSqlTemplatePaginatedMonitoramentoDocEletronico(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}


	@SuppressWarnings({ "rawtypes" })
    public ResultSetPage findPaginatedMonitoramentoDocEletronico(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = this.getSqlTemplatePaginatedMonitoramentoDocEletronico(criteria);

		sql.addProjection("new map(" +
				"monitoramentoDocEletronico.id as idMonitoramentoDocEletronic, " +
				"monitoramentoDocEletronico.tpSituacaoDocumento as tpSituacaoDocumento, "+

				"doctoServico.id as idDoctoServico, " +
				"doctoServico.nrDoctoServico as nrDoctoServico, "+
				"doctoServico.dhEmissao as dhEmissao, "+

				"filialOrigem.id as idFilialOrigem, " +
				"filialOrigem.sgFilial as sgFilialOrigem, " +

				"pessoa.nmPessoa as devedorDoctoServico, "+

				"moeda.sgMoeda as sgMoeda, "+
				"moeda.dsSimbolo as dsSimbolo, "+

				"doctoServico.vlTotalDocServico as vlTotalDoctoServico ) ");
		//"devedorDocServs.cliente    )" );


		sql.addOrderBy("doctoServico.nrDoctoServico");

		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
	}

    private SqlTemplate getSqlTemplatePaginatedMonitoramentoDocEletronico( TypedFlatMap criteria ){
		Long idCliente = criteria.getLong("idCliente");
		YearMonthDay dataInicial = criteria.getYearMonthDay("dhEmissaoInicial");
		Date dataFinal = null;
		try {
			//LMS-8738
			dataFinal = new SimpleDateFormat("yyyy-MM-dd").parse(criteria.getYearMonthDay("dhEmissaoFinal").toString());

		} catch (ParseException e) {
			log.error("Erro ao recuperar a data final de emissão", e);

		}

		/** From */
		StringBuffer from = new StringBuffer();
		from.append(MonitoramentoDocEletronico.class.getName() + " as monitoramentoDocEletronico ");
		from.append("inner join monitoramentoDocEletronico.doctoServico as doctoServico ");
		from.append("inner join doctoServico.filialByIdFilialOrigem as filialOrigem ");
		from.append("inner join doctoServico.devedorDocServs as devedorDocServs ");
		from.append("inner join	devedorDocServs.cliente as cliente ");
		from.append("inner join	cliente.pessoa as pessoa ");
		from.append("left join doctoServico.moeda moeda ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom( from.toString() );

		/** where */
		if(criteria.get("idDoctoServico") == null){
			sql.addCriteria("doctoServico.filialByIdFilialOrigem.id", "=", criteria.getLong("idFilial"));
			if (dataInicial != null && dataFinal != null) {
				sql.addCriteria("trunc(cast (doctoServico.dhEmissao.value as date))", ">=", dataInicial);
				//LMS-8738
				sql.addCriteria("trunc(cast (doctoServico.dhEmissao.value as date))", "<=", DateTimeUtils.setLastHourOfDayTimestamp(dataFinal));
			}

			if (idCliente != null){
				sql.addCriteria("cliente.idCliente", "=", idCliente);
			}

			sql.addCriteria("monitoramentoDocEletronico.tpSituacaoDocumento", "=", criteria.getString("tpSituacaoDocumento"));
		}else{
			sql.addCriteria("doctoServico.id", "=",  criteria.getLong("idDoctoServico"));
			sql.addCriteria("doctoServico.filialByIdFilialOrigem.id", "=", criteria.getLong("idFilialOrigem"));
			sql.addCriteria("doctoServico.tpDocumentoServico", "=", criteria.getString("tpDocumentoServico"));
		}

		return sql;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("doctoServico", FetchMode.JOIN);
		lazyFindById.put("doctoServico.filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindById.put("doctoServico.filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map<String,Object>> findByListFatura(final List<Long> idFaturaList, final String tpDocumento){
		if (idFaturaList == null || idFaturaList.size() == 0 ){
			return new ArrayList<Map<String,Object>>();
		}

		final StringBuilder sql = new StringBuilder();

		//ctes
		if (tpDocumento == null || ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumento)) {
			sql.append(" SELECT TDB.DOCUMENTDATA, MDE.TP_SITUACAO_DOCUMENTO, MDE.ID_DOCTO_SERVICO, MDE.NR_PROTOCOLO, FC.SG_FILIAL AS SG_FILIAL_DOCTO, F.NR_FATURA,F.ID_FATURA,DS.TP_DOCUMENTO_SERVICO,MDE.ID_MONITORAMENTO_DOC_ELETRONIC, FL.SG_FILIAL AS SG_FILIAL_FATURA, DS.NR_DOCTO_SERVICO, MDE.DS_DADOS_DOCUMENTO ");
			sql.append(" FROM DEVEDOR_DOC_SERV_FAT DDF  ");
			sql.append(" INNER JOIN MONITORAMENTO_DOC_ELETRONICO MDE ON MDE.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" LEFT JOIN TBDATABASEINPUT_CTE TDB ON TDB.ID = MDE.ID_ENVIO_DOC_ELETRONICO_E ");
			sql.append(" INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN CONHECIMENTO C ON C.ID_CONHECIMENTO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN FILIAL FC ON DS.ID_FILIAL_ORIGEM = FC.ID_FILIAL ");
			sql.append(" INNER JOIN FATURA F ON F.ID_FATURA = DDF.ID_FATURA ");
			sql.append(" INNER JOIN FILIAL FL ON FL.ID_FILIAL = F.ID_FILIAL ");
			sql.append(" WHERE ");
			sql.append(" C.TP_DOCUMENTO_SERVICO = 'CTE' ");
			sql.append(" AND C.TP_SITUACAO_CONHECIMENTO = 'E' ");
			sql.append(" AND MDE.TP_SITUACAO_DOCUMENTO = 'A' ");
			sql.append(" AND DDF.ID_FATURA  in (:faturas1) ");
		}

		if (tpDocumento == null) {
			sql.append(" UNION ALL ");
		}

		//ntes
		if (tpDocumento == null || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumento)) {
			sql.append(" SELECT MDE.DS_DADOS_DOCUMENTO as DOCUMENTDATA, MDE.TP_SITUACAO_DOCUMENTO, MDE.ID_DOCTO_SERVICO, MDE.NR_PROTOCOLO, FC.SG_FILIAL AS SG_FILIAL_DOCTO, F.NR_FATURA,F.ID_FATURA,DS.TP_DOCUMENTO_SERVICO,MDE.ID_MONITORAMENTO_DOC_ELETRONIC, FL.SG_FILIAL AS SG_FILIAL_FATURA, DS.NR_DOCTO_SERVICO, null as DS_DADOS_DOCUMENTO ");
			sql.append(" FROM DEVEDOR_DOC_SERV_FAT DDF ");
			sql.append(" INNER JOIN MONITORAMENTO_DOC_ELETRONICO MDE ON MDE.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN CONHECIMENTO C ON C.ID_CONHECIMENTO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN FILIAL FC ON DS.ID_FILIAL_ORIGEM = FC.ID_FILIAL ");
			sql.append(" INNER JOIN FATURA F ON F.ID_FATURA = DDF.ID_FATURA ");
			sql.append(" INNER JOIN FILIAL FL ON FL.ID_FILIAL = F.ID_FILIAL ");
			sql.append(" WHERE ");
			sql.append(" C.TP_DOCUMENTO_SERVICO = 'NTE' ");
			sql.append(" AND C.TP_SITUACAO_CONHECIMENTO = 'E' ");
			sql.append(" AND MDE.TP_SITUACAO_DOCUMENTO IN ('A','E') ");
			sql.append(" AND DDF.ID_FATURA  in (:faturas2) ");
		}

		if (tpDocumento == null) {
			sql.append(" UNION ALL ");
		}

		//nses
		if (tpDocumento == null || ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(tpDocumento)) {
			sql.append(" SELECT MDE.DS_DADOS_DOCUMENTO as DOCUMENTDATA, MDE.TP_SITUACAO_DOCUMENTO, MDE.ID_DOCTO_SERVICO, MDE.NR_PROTOCOLO, FC.SG_FILIAL AS SG_FILIAL_DOCTO, F.NR_FATURA,F.ID_FATURA,DS.TP_DOCUMENTO_SERVICO,MDE.ID_MONITORAMENTO_DOC_ELETRONIC, FL.SG_FILIAL AS SG_FILIAL_FATURA, DS.NR_DOCTO_SERVICO, null as DS_DADOS_DOCUMENTO ");
			sql.append(" FROM DEVEDOR_DOC_SERV_FAT DDF ");
			sql.append(" INNER JOIN MONITORAMENTO_DOC_ELETRONICO MDE ON MDE.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN NOTA_FISCAL_SERVICO NFS ON NFS.ID_NOTA_FISCAL_SERVICO = DDF.ID_DOCTO_SERVICO ");
			sql.append(" INNER JOIN FILIAL FC ON DS.ID_FILIAL_ORIGEM = FC.ID_FILIAL ");
			sql.append(" INNER JOIN FATURA F ON F.ID_FATURA = DDF.ID_FATURA ");
			sql.append(" INNER JOIN FILIAL FL ON FL.ID_FILIAL = F.ID_FILIAL ");
			sql.append(" WHERE ");
			sql.append(" NFS.TP_SITUACAO_NF = 'E' ");
			sql.append(" AND MDE.TP_SITUACAO_DOCUMENTO IN ('A','E') ");
			sql.append(" AND DDF.ID_FATURA  in (:faturas3) ");
		}

		sql.append(" ORDER BY SG_FILIAL_FATURA, NR_FATURA, SG_FILIAL_DOCTO, NR_DOCTO_SERVICO ");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("DOCUMENTDATA",Hibernate.BLOB);
				sqlQuery.addScalar("TP_SITUACAO_DOCUMENTO",Hibernate.STRING);
				sqlQuery.addScalar("ID_DOCTO_SERVICO",Hibernate.LONG);
				sqlQuery.addScalar("NR_PROTOCOLO",Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_DOCTO",Hibernate.STRING);
				sqlQuery.addScalar("NR_FATURA",Hibernate.LONG);
				sqlQuery.addScalar("ID_FATURA",Hibernate.LONG);
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO",Hibernate.STRING);
				sqlQuery.addScalar("ID_MONITORAMENTO_DOC_ELETRONIC",Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_FATURA",Hibernate.STRING);
				sqlQuery.addScalar("DS_DADOS_DOCUMENTO",Hibernate.BLOB);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);

				if (tpDocumento == null || ConstantesExpedicao.CONHECIMENTO_ELETRONICO.equals(tpDocumento)) {
					query.setParameterList("faturas1", idFaturaList);
				}
				if (tpDocumento == null || ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumento)) {
					query.setParameterList("faturas2", idFaturaList);
				}
				if (tpDocumento == null || ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(tpDocumento)) {
					query.setParameterList("faturas3", idFaturaList);
				}
				return query.list();
			}
		};

		List list = getHibernateTemplate().executeFind(hcb);

		return extractMapCTE(list);
	}

	private List<Map<String, Object>> extractMapCTE(List<Object[]> doctosEletronicos) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if (doctosEletronicos != null && doctosEletronicos.size() >0){
			for (Object[] mde : doctosEletronicos){
				Map<String, Object> map = new HashMap<String, Object>();
				StringBuilder xml = null;
				if( mde[0] != null ){
					byte[] xmlDataTB = readBlob((Blob)mde[0]);
					xml = new StringBuilder(new String(xmlDataTB));
				}else if ( mde[10] != null ){
					byte[] xmlDataMDE = readBlob((Blob)mde[10]);
					xml = XMLUtils.buildXmlCTEAutenticadoSefaz(xmlDataMDE);
				}
				map.put("tpSituacaoDocumento", mde[1]);
				map.put("idConhecimento", mde[2]);
				map.put("nrProtocolo", mde[3]);
				map.put("sgFilial", mde[4]);
				map.put("nrFatura", mde[5]);
				map.put("idFatura", mde[6]);
				map.put("xml", xml == null ? null : addNrProtocolo(xml, (Long)mde[3]));
				map.put("tpDocumento", mde[7]);
				map.put("idMonitoramentoDocEletronic", mde[8]);
				map.put("sgFilialFatura", mde[9]);
				resultList.add(map);
			}
		}
		return resultList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findNteToReportByListFatura(final List<Long> idFaturaList) {
		if (idFaturaList == null || idFaturaList.size() == 0 ){
			return new ArrayList<Map<String,Object>>();
		}

		final StringBuilder sql = new StringBuilder();

		sql.append(" SELECT TID.DOCDATA, MDE.TP_SITUACAO_DOCUMENTO, MDE.ID_DOCTO_SERVICO, MDE.NR_PROTOCOLO, MDE.ID_MONITORAMENTO_DOC_ELETRONIC, " +
				"	F.NR_FATURA, FL.SG_FILIAL ")
				.append(" FROM DEVEDOR_DOC_SERV_FAT DDF ")
				.append(" INNER JOIN MONITORAMENTO_DOC_ELETRONICO MDE ON MDE.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ")
				.append(" INNER JOIN TBINPUTDOCUMENTS TID ON TID.ID = MDE.ID_ENVIO_DOC_ELETRONICO_E ")
				.append(" INNER JOIN CONHECIMENTO C ON C.ID_CONHECIMENTO = DDF.ID_DOCTO_SERVICO ")
				.append(" INNER JOIN FATURA F ON F.ID_FATURA = DDF.ID_FATURA ")
				.append(" INNER JOIN FILIAL FL ON FL.ID_FILIAL = F.ID_FILIAL ")
				.append(" WHERE ")
				.append(" C.TP_DOCUMENTO_SERVICO = 'NTE' ")
				.append(" AND C.TP_SITUACAO_CONHECIMENTO = 'E' ")
				.append(" AND MDE.TP_SITUACAO_DOCUMENTO = 'A' ")
				.append(" AND DDF.ID_FATURA  in (:faturas) ")
				.append(" ORDER BY FL.SG_FILIAL, F.NR_FATURA, C.ID_FILIAL_ORIGEM, C.NR_CONHECIMENTO");
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("DOCDATA",Hibernate.BLOB);
				sqlQuery.addScalar("TP_SITUACAO_DOCUMENTO",Hibernate.STRING);
				sqlQuery.addScalar("ID_DOCTO_SERVICO",Hibernate.LONG);
				sqlQuery.addScalar("NR_PROTOCOLO",Hibernate.LONG);
				sqlQuery.addScalar("ID_MONITORAMENTO_DOC_ELETRONIC",Hibernate.LONG);
				sqlQuery.addScalar("NR_FATURA",Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL",Hibernate.STRING);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);

				query.setParameterList("faturas", idFaturaList);
				return query.list();
			}
		};

		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
		return extractMapNte(list);

	}


	private List<Map<String, Object>> extractMapNte(List<Object[]> list) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if (list != null && list.size() >0){
			for (Object[] mde : list){
				Map<String, Object> map = new HashMap<String, Object>();
				StringBuilder xml = null;
				if( mde[0] != null ){
					byte[] xmlDataTB = readBlob((Blob)mde[0]);
					try{
						xml = new StringBuilder(new String(xmlDataTB, "UTF-8"));
					}catch(UnsupportedEncodingException e ){
						throw new RuntimeException( e );
					}
				}else if ( mde[10] != null ){
					byte[] xmlDataMDE = readBlob((Blob)mde[10]);
					xml = XMLUtils.buildXmlCTEAutenticadoSefaz(xmlDataMDE);
				}
				map.put("tpSituacaoDocumento", mde[1]);
				map.put("idConhecimento", mde[2]);
				map.put("nrProtocolo", mde[3]);
				map.put("xml", addNrProtocolo(xml, (Long)mde[3]));
				map.put("idMonitoramentoDocEletronic", mde[4]);
				map.put("nrFatura", mde[5]);
				map.put("sgFilial", mde[6]);
				resultList.add(map);
			}
		}
		return resultList;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findNseToReportByListFatura(final List<Long> idFaturaList) {
		if (idFaturaList == null || idFaturaList.size() == 0 ){
			return new ArrayList<Map<String,Object>>();
		}

		final StringBuilder sql = new StringBuilder();

		sql.append(" SELECT TID.DOCDATA, MDE.TP_SITUACAO_DOCUMENTO, MDE.ID_DOCTO_SERVICO, MDE.NR_PROTOCOLO, MDE.ID_MONITORAMENTO_DOC_ELETRONIC," +
				" F.NR_FATURA, FL.SG_FILIAL ")
				.append(" FROM DEVEDOR_DOC_SERV_FAT DDF ")
				.append(" INNER JOIN MONITORAMENTO_DOC_ELETRONICO MDE ON MDE.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ")
				.append(" INNER JOIN TBINPUTDOCUMENTS TID ON TID.ID = MDE.ID_ENVIO_DOC_ELETRONICO_E ")
				.append(" INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = DDF.ID_DOCTO_SERVICO ")
				.append(" INNER JOIN NOTA_FISCAL_SERVICO NFS ON NFS.ID_NOTA_FISCAL_SERVICO = DDF.ID_DOCTO_SERVICO ")
				.append(" INNER JOIN FATURA F ON F.ID_FATURA = DDF.ID_FATURA ")
				.append(" INNER JOIN FILIAL FL ON FL.ID_FILIAL = F.ID_FILIAL ")
				.append(" WHERE ")
				.append("  DS.TP_DOCUMENTO_SERVICO = 'NSE' ")
				.append(" AND NFS.TP_SITUACAO_NF = 'E' ")
				.append(" AND MDE.TP_SITUACAO_DOCUMENTO = 'A' ")
				.append(" AND DDF.ID_FATURA  in (:faturas) ")
				.append(" ORDER BY FL.SG_FILIAL, F.NR_FATURA, DS.ID_FILIAL_ORIGEM, DS.NR_DOCTO_SERVICO");
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("DOCDATA",Hibernate.BLOB);
				sqlQuery.addScalar("TP_SITUACAO_DOCUMENTO",Hibernate.STRING);
				sqlQuery.addScalar("ID_DOCTO_SERVICO",Hibernate.LONG);
				sqlQuery.addScalar("NR_PROTOCOLO",Hibernate.LONG);
				sqlQuery.addScalar("ID_MONITORAMENTO_DOC_ELETRONIC",Hibernate.LONG);
				sqlQuery.addScalar("NR_FATURA",Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL",Hibernate.STRING);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);

				query.setParameterList("faturas", idFaturaList);
				return query.list();
			}
		};

		List<Object[]> list = getHibernateTemplate().executeFind(hcb);
		List<Map<String, Object>> resultList = extractMapNte(list);
		return resultList;

	}

	private byte[] readBlob(Blob xml ){
		StringBuilder sb = new StringBuilder();
		try {
			InputStreamReader is = new InputStreamReader(xml.getBinaryStream(),"UTF-8");
			BufferedReader br = new BufferedReader(is);
			String read = br.readLine();
			while(read != null) {
				sb.append(read);
				read =br.readLine();
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		return sb.toString().getBytes();
	}

	/**
	 * Adiciona o nrProtocolo ao xml
	 * Optei por não utilizar parser de xml devido a performance
	 */
	private String addNrProtocolo(StringBuilder xml, Long nrProtocolo) {
		int index = xml.indexOf("</dadosAdic>");
		if (index > -1){
			xml.insert(index, "<nrProtocolo>" + nrProtocolo + "</nrProtocolo>");
		}


		return xml.toString();
	}

	@SuppressWarnings({ "unchecked" })
	public Map<String, Object> findDadosEnvioCTeClienteByNrChave(String nrChave){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(mde.idMonitoramentoDocEletronic as idMonitoramentoDocEletronic, mde.doctoServico.idDoctoServico as idDoctoServico) ");
		hql.addFrom(getPersistentClass().getName(),"mde");
		hql.addCriteria("mde.nrChave", "=", nrChave);
		return (Map<String, Object>) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

    public MonitoramentoDocEletronico findByNrChave(final String nrChave) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName() + " monitoramentoDocEletronico");
		hql.addCriteria("monitoramentoDocEletronico.nrChave", "=", nrChave);

		return (MonitoramentoDocEletronico) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
    
    
    public DoctoServico findDoctoServicoByNrChave(String nrChave){
    	String hql = "select mde.doctoServico from MonitoramentoDocEletronico mde";
    	
    	if (nrChave.substring(20, 22).equals("99")){
    		hql = hql+" where mde.nrChaveEletrRpstFdx = :nrChave ";
    	}else{
    		hql = hql+" where mde.nrChave = :nrChave ";
    	}
    	
    	Map<String,Object> param = new HashMap<String, Object>();
    	param.put("nrChave", nrChave);
    	
    	return (DoctoServico)getAdsmHibernateTemplate().findUniqueResult(hql,param);
    	
    }

	@SuppressWarnings({ "unchecked" })
    public List<MonitoramentoDocEletronico> findListByNrChave(String nrChave) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(getPersistentClass().getName() + " monitoramentoDocEletronico");
		hql.addCriteria("monitoramentoDocEletronico.nrChave", "=", nrChave);

		return (List<MonitoramentoDocEletronico>) getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	@SuppressWarnings({ "unchecked" })
	public List<MonitoramentoDocEletronico> findMonitoramentoDocEletronicoEmitidosAutorizados(String dtInicioNotfisRpstFdx, List<Long> idsFiliaisEnvioNotFis) {
 	    SqlTemplate hql = new SqlTemplate();

	    DateTime dateTime = JTDateTimeUtils.convertDataStringToYearMonthDay(dtInicioNotfisRpstFdx, "dd/MM/yyyy").toDateTimeAtMidnight();
	    hql.addProjection("mde");
	    hql.addFrom(getPersistentClass().getName() + " mde join mde.doctoServico ds");

	    hql.addCustomCriteria("ds.tpSituacaoConhecimento = :tpSituacaoConhecimento");
	    hql.addCustomCriteria("mde.tpSituacaoDocumento = :tpSituacaoDocumento");
	    
	    hql.addCustomCriteria("trunc(cast(ds.dhEmissao.value as date)) >= to_date(:dtInicio,'dd/MM/yyyy')");
	    
	    hql.addCustomCriteria("ds.tpDocumentoServico in ('NTE')");
	    hql.addCustomCriteria("mde.nrChaveEletrRpstFdx is null");
	    hql.addCustomCriteria("ds.filialByIdFilialOrigem.id in (:idsFiliaisEnvioNotFis)");
	    
	    Map<String, Object> param = new HashMap<String, Object>();
	    param.put("tpSituacaoDocumento", ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_AUTORIZADO);
	    param.put("tpSituacaoConhecimento", ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO);
	    param.put("dtInicio", dtInicioNotfisRpstFdx);
	    param.put("idsFiliaisEnvioNotFis", idsFiliaisEnvioNotFis);
	    
	    return (List<MonitoramentoDocEletronico>) getAdsmHibernateTemplate().findByNamedParam(hql.getSql(), param);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Map> findDocumentosRejeitados(String dtInicio, String dtFim){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new map(" +
				"c.idDoctoServico as idDoctoServico," +
				"c.filialOrigem.idFilial as idFilial," +
				"c.filialOrigem.sgFilial as sgFilial," +
				"c.nrConhecimento as nrConhecimento," +
				"c.tpDocumentoServico as tpDocumentoServico" +
				")");
		sql.addFrom(Conhecimento.class.getName() +  " as c ");

		/** where */
		sql.addCustomCriteria("trunc(cast(c.dhEmissao.value as date)) >= to_date(:dtInicio,'ddmmyyyy')");
		sql.addCustomCriteria("trunc(cast(c.dhEmissao.value as date)) < to_date(:dtFim,'ddmmyyyy')");
		sql.addCustomCriteria("c.tpDocumentoServico = 'CTE'");
		sql.addCustomCriteria("c.tpSituacaoConhecimento = 'P'");
		sql.addCustomCriteria("exists (select 1 from "+ MonitoramentoDocEletronico.class.getName() +" as mde where mde.doctoServico.id = c.id and mde.tpSituacaoDocumento = 'R' and rownum = 1)");

		Map<String,Object> param = new HashMap<String, Object>();
		param.put("dtInicio", dtInicio);
		param.put("dtFim", dtFim);

		return getAdsmHibernateTemplate().findByNamedParam(sql.getSql(), param);
	}

	public void removeDocumentoRejeitado(Long idDocumento,Long idLocalizacaoMercadoria ){
		final StringBuilder sql = new StringBuilder();

		sql.append("UPDATE CONHECIMENTO SET TP_SITUACAO_CONHECIMENTO = 'C' WHERE ID_CONHECIMENTO = :idDocumento");
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idDocumento",idDocumento);
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		params.put("dsTimeZone",JTDateTimeUtils.getDataHoraAtual().getZone().getID());
		params.put("idLocalizacaoMercadoria",idLocalizacaoMercadoria);
		sql.append("UPDATE DOCTO_SERVICO SET DH_ALTERACAO = sysdate,DH_ALTERACAO_TZR = :dsTimeZone , ID_LOCALIZACAO_MERCADORIA = :idLocalizacaoMercadoria  WHERE ID_DOCTO_SERVICO = :idDocumento");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);
		params.remove("dsTimeZone");
		params.remove("idLocalizacaoMercadoria");

		sql.append("UPDATE DEVEDOR_DOC_SERV_FAT SET TP_SITUACAO_COBRANCA = 'L' WHERE ID_DOCTO_SERVICO = :idDocumento");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("UPDATE LIBERACAO_NOTA_NATURA SET TP_SITUACAO = 'C' WHERE ID_DOCTO_SERVICO = :idDocumento");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM NOTA_OCORRENCIA_NC WHERE ID_NOTA_FISCAL_CONHECIMENTO IN (SELECT ID_NOTA_FISCAL_CONHECIMENTO FROM NOTA_FISCAL_CONHECIMENTO WHERE ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM PRE_MANIFESTO_VOLUME WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF,NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM VOLUME_SOBRA_FILIAL WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF, NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM MANIFESTO_NACIONAL_VOLUME WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF,NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM VOLUME_SOBRA WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF, NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM NF_ITEM_MDA WHERE ID_NOTA_FISCAL_CONHECIMENTO IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF, NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM CARREG_DESC_VOLUME WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF, NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM MANIFESTO_ENTREGA_VOLUME WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT VNF.ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL VNF,NOTA_FISCAL_CONHECIMENTO NFC WHERE VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO AND NFC.ID_CONHECIMENTO = :idDocumento)");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM PRE_MANIFESTO_DOCUMENTO WHERE ID_DOCTO_SERVICO = :idDocumento");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM MANIFESTO_NACIONAL_CTO WHERE ID_CONHECIMENTO = :idDocumento");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM MANIFESTO_ENTREGA_DOCUMENTO WHERE ID_DOCTO_SERVICO = :idDocumento");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM EVENTO_VOLUME WHERE ID_VOLUME_NOTA_FISCAL IN (SELECT ID_VOLUME_NOTA_FISCAL FROM VOLUME_NOTA_FISCAL WHERE ID_NOTA_FISCAL_CONHECIMENTO IN (SELECT ID_NOTA_FISCAL_CONHECIMENTO from NOTA_FISCAL_CONHECIMENTO WHERE ID_CONHECIMENTO = :idDocumento ))");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM VOLUME_NOTA_FISCAL WHERE ID_NOTA_FISCAL_CONHECIMENTO IN (SELECT ID_NOTA_FISCAL_CONHECIMENTO from NOTA_FISCAL_CONHECIMENTO WHERE ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM NF_DADOS_COMP where ID_NOTA_FISCAL_CONHECIMENTO in ( SELECT ID_NOTA_FISCAL_CONHECIMENTO  FROM NOTA_FISCAL_CONHECIMENTO WHERE ID_CONHECIMENTO = :idDocumento )");
		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

		sql.append("DELETE FROM NOTA_FISCAL_CONHECIMENTO WHERE ID_CONHECIMENTO = :idDocumento");

		getHibernateTemplate().execute(getHibernateCallback(sql.toString(),params));
		sql.setLength(0);

	}

	private HibernateCallback getHibernateCallback(final String sql, final Map<String,Object> params){
		return new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				for( String key : params.keySet() ){
					query.setParameter(key, params.get(key));
				}

				query.executeUpdate();
				return null;
			}
		};
	}


	public void updateDhEnvioEmail(final Long idMonitoramento, final String destinatario) {
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();

					final String sql = "UPDATE MONITORAMENTO_DOC_ELETRONICO " +
							" SET DH_ENVIO = sysdate, DH_ENVIO_TZR = :dsTimeZone, " +
							" DS_ENVIO_EMAIL = :dsEnvioEmail " +
							" WHERE ID_MONITORAMENTO_DOC_ELETRONIC = :idMonitoramento";

					Map<String,Object> params = new HashMap<String, Object>();
					params.put("idMonitoramento",idMonitoramento);
					params.put("dsTimeZone",JTDateTimeUtils.getDataHoraAtual().getZone().getID());
					params.put("dsEnvioEmail",destinatario);

					Query query = null;
					query = session.createSQLQuery(sql);
					query.setLong("idMonitoramento", idMonitoramento);
					query.setString("dsTimeZone", JTDateTimeUtils.getDataHoraAtual().getZone().getID());
					query.setString("dsEnvioEmail", destinatario);
					query.executeUpdate();
					session.getTransaction().commit();
				}catch (Exception e) {
					log.error("Erro ao atualizar o monitoramento = " + idMonitoramento ,e);
					session.getTransaction().rollback();
				}finally {
					if(session != null) session.close();
				}
				return null;
			}
		});
	}

	public void updateEnvioCTeCliente(Long idMonitoramentoDocEletronic, DateTime dhEnvio, String dsEnvioEmail, String xml) {
		try {
			Map<String, Object> parametersValues = new HashMap<String, Object>();
			parametersValues.put("idMonitoramento", idMonitoramentoDocEletronic);
			parametersValues.put("dhEnvio", dhEnvio.toDate());
			parametersValues.put("dsTimeZone", dhEnvio.getZone().getID());
			parametersValues.put("dsEnvioEmail", dsEnvioEmail);
			parametersValues.put("xml", xml.getBytes("UTF-8"));

			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE monitoramento_doc_eletronico SET ");
			sql.append("dh_Envio = :dhEnvio,		");
			sql.append("dh_envio_tzr = :dsTimeZone,	");
			sql.append("ds_envio_email = :dsEnvioEmail, ");
			sql.append("ds_dados_documento = :xml ");
			sql.append("WHERE ");
			sql.append("id_monitoramento_doc_eletronic = :idMonitoramento ");

			getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Persiste o XML através da chave do CTe
	 *
	 * @param idMonitoramento
	 * @param destinatario
	 * @param dhEnvio
	 * @param xml
	 * @param chaveXml
	 *
	 * @return
	 */
	public String updateDhEnvioEmail(final Long idMonitoramento, final String destinatario, final DateTime dhEnvio, final String xml, final String chaveXml, final Filial filial) {

		final StringBuilder chaveErro = new StringBuilder();

		HibernateCallback updateDhEnvioEmail = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuilder update = new StringBuilder()
						.append(" update MONITORAMENTO_DOC_ELETRONICO mde	")
						.append(" set 										")
						.append("	 mde.dh_Envio 			= :dhEnvio		")
						.append(" 	,mde.DH_ENVIO_TZR 		= :dsTimeZone	")
						.append("	,mde.DS_ENVIO_EMAIL 	= :dsEnvioEmail ")
						.append("	,mde.DS_DADOS_DOCUMENTO = :xml 			")
						.append(" where										")
						.append("	mde.ID_MONITORAMENTO_DOC_ELETRONIC = :idMonitoramento ");

				try {
					session = getSessionFactory().openSession();
					session.beginTransaction();

					byte[] blob = xml.getBytes("UTF-8");

					Map<String, Object> params = new HashMap<String, Object>();
					params.put("dhEnvio", dhEnvio == null ? JTDateTimeUtils.getDataHoraAtual() : dhEnvio);
					params.put("dsTimeZone", JTDateTimeUtils.getDataHoraAtual(filial).getZone().getID());
					params.put("dsEnvioEmail", destinatario);
					params.put("xml", blob);
					params.put("idMonitoramento", idMonitoramento);

					Query query = null;
					query = session.createSQLQuery(update.toString());
					query.setLong("idMonitoramento", idMonitoramento);
					query.setString("dsTimeZone", JTDateTimeUtils.getDataHoraAtual(filial).getZone().getID());
					query.setTimestamp("dhEnvio", dhEnvio.toDate());
					query.setString("dsEnvioEmail", destinatario);
					query.setBinary("xml", blob);
					query.executeUpdate();
					session.getTransaction().commit();

				} catch (QueryException qex) {
					String msg = "Erro ao executar update. Chave: " + chaveXml;
					log.error(msg, qex);
					chaveErro.append(msg);
					session.getTransaction().rollback();

				} catch (UnsupportedEncodingException uee) {
					String msg = "Erro ao converter o XML. Chave: " + chaveXml;
					log.error(msg, uee);
					chaveErro.append(msg);
					session.getTransaction().rollback();

				} catch (TransactionException te) {
					String msg = "Erro genérico ao executar o update. Chave: " + chaveXml;
					log.error(msg, te);
					chaveErro.append(msg);
					session.getTransaction().rollback();

				} catch (Exception e) {
					String msg = "Erro genérico ao executar o update. Chave: " + chaveXml;
					log.error(msg, e);
					chaveErro.append(msg);
					session.getTransaction().rollback();

				} finally {
					if (session != null)
						session.close();
				}

				return null;
			}
		};

		getAdsmHibernateTemplate().execute(updateDhEnvioEmail);

		return chaveErro.toString();
	}

	public void executeUpdateNroNfse(final Long idMonitoramentoDocEletronic, final Long nrDocumentoEletronico, final DateTime dhAutorizacao) {
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idMonitoramentoDocEletronic", idMonitoramentoDocEletronic);
		parametersValues.put("nrDocumentoEletronico", nrDocumentoEletronico);
		parametersValues.put("dhAutorizacao", dhAutorizacao.toDate());
		parametersValues.put("dsTimeZone", dhAutorizacao.getZone().getID());

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE monitoramento_doc_eletronico SET ");
		sql.append("NR_DOCUMENTO_ELETRONICO = :nrDocumentoEletronico, ");
		sql.append("DH_AUTORIZACAO = :dhAutorizacao, ");
		sql.append("DH_AUTORIZACAO_TZR = :dsTimeZone,");
		sql.append("TP_SITUACAO_DOCUMENTO = 'A' ");
		sql.append("WHERE ID_MONITORAMENTO_DOC_ELETRONIC = :idMonitoramentoDocEletronic ");

		getAdsmHibernateTemplate().executeUpdateBySql(sql.toString(), parametersValues);
	}

	public Long findMaxNrDoctoEletronicoByIdFilial(Long idFilial){
		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT MAX(mde.NR_DOCUMENTO_ELETRONICO) as nrDoctoEletronico ");
		sql.append(" FROM MONITORAMENTO_DOC_ELETRONICO mde, ");
		sql.append("      DOCTO_SERVICO ds ");
		sql.append(" WHERE mde.ID_DOCTO_SERVICO = ds.ID_DOCTO_SERVICO ");
		sql.append(" AND ds.ID_FILIAL_ORIGEM = :idFilial ");
		sql.append(" AND ds.tp_documento_Servico IN ('NSE','NTE') ");
		sql.append(" AND ds.dh_emissao >= SYSDATE - 60 ");

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("nrDoctoEletronico", Hibernate.LONG);
			}
		};

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idFilial", idFilial);

		List<?> object = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);

		if(!object.isEmpty()){
			return (Long) object.get(0);
		}

		return null;
	}

	public MonitoramentoDocEletronico findMonitoramentosByNrDoctoEletronicoByIdFilial(Long nrDocumentoEletronico, Long idFilial) {

		StringBuilder sql = new StringBuilder();
		sql.append(" select mde ")
				.append(" from MonitoramentoDocEletronico as mde ")
				.append(" join mde.doctoServico as ds ")
				.append(" where ds.id = mde.doctoServico.id ")
				.append(" and mde.nrDocumentoEletronico = :nrDocumentoEletronico ")
				.append(" and ds.filialByIdFilialOrigem.id = :idFilial ")
				.append(" and ds.tpDocumentoServico in ('NSE','NTE') ")
				.append(" and trunc(cast (ds.dhEmissao.value as date)) >= :dataRef ");

		YearMonthDay dataRef = JTDateTimeUtils.getDataAtual().minusDays(DIAS_REF);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("nrDocumentoEletronico", nrDocumentoEletronico);
		params.put("idFilial", idFilial);
		params.put("dataRef", dataRef);

		List<?> object = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);

		if(!object.isEmpty()){
			return (MonitoramentoDocEletronico) object.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
    public List<MonitoramentoDocEletronico> findMonitoramentosByControleCarga(Long idControleCarga) {

		StringBuffer sql = new StringBuffer();
		sql.append(" select mde ")
				.append(" from MonitoramentoDocEletronico as mde ")
				.append(" ,PreManifestoDocumento as pmd ")
				.append(" inner join pmd.manifesto as m ")
				.append(" inner join pmd.doctoServico as ds ")
				.append(" where ")
				.append(" m.controleCarga.id = :idControleCarga ")
				.append(" and ds.id = mde.doctoServico.id ")
				.append(" and m.tpStatusManifesto not in ('CA', 'FE') ")
				.append(" and ds.tpDocumentoServico in ('CTE','CTR')");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);

		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}


	@SuppressWarnings("rawtypes")
	public byte[] findXmlByDoctoServico(Long idMonitoramentoEletronico) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select mde.dsDadosDocumento ");
		sql.append(" from MonitoramentoDocEletronico mde ");
		sql.append(" where mde.idMonitoramentoDocEletronic = ? ");

		List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idMonitoramentoEletronico});

		if(list != null && !list.isEmpty()){
			return (byte[]) list.get(0) ;
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
    public byte[] findXmlMonitByIdDoctoServico(Long idDoctoServico) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select mde.dsDadosDocumento ");
		sql.append(" from MonitoramentoDocEletronico mde join mde.doctoServico ds");
		sql.append(" where ds.idDoctoServico = ? ");

		List list = getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idDoctoServico});

		if(list != null && !list.isEmpty()){
			return (byte[]) list.get(0) ;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
    public List<Object[]> findByIdFatura(final Long idFatura) {

		final StringBuilder sql = new StringBuilder()
				.append(" select doc.id_docto_servico,")
				.append(" mde.ds_dados_documento,")
				.append(" doc.tp_documento_servico,")
				.append(" doc.nr_docto_servico,")
				.append(" fe.sg_filial,")
				.append(" fat.nr_fatura,")
				.append(" mde.nr_protocolo")
				.append(" from")
				.append(" devedor_doc_serv_fat dev,")
				.append(" monitoramento_doc_eletronico mde,")
				.append(" docto_servico doc,")
				.append(" fatura fat,")
				.append(" filial fe")
				.append(" where")
				.append(" mde.ds_dados_documento is not null")
				.append(" and mde.id_docto_servico = dev.id_docto_servico")
				.append(" and fat.id_fatura = dev.id_fatura")
				.append(" and fe.id_filial = doc.id_filial_origem")
				.append(" and mde.tp_situacao_documento in ('A','E')")
				.append(" and doc.id_docto_servico = dev.id_docto_servico")
				.append(" and dev.id_fatura = :idFatura")
				.append(" order by doc.id_docto_servico");;

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("ds_dados_documento", Hibernate.BLOB);
				sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
				sqlQuery.addScalar("nr_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_fatura", Hibernate.LONG);
				sqlQuery.addScalar("nr_protocolo", Hibernate.LONG);

			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);

				query.setParameter("idFatura", idFatura);
				return query.list();
			}
		};

		List<Object[]> list = getHibernateTemplate().executeFind(hcb);

		return list;
	}

	@SuppressWarnings("unchecked")
    public String findNrChaveByIdDoctoServico(final Long idDoctoServico) {
        final StringBuilder sql = new StringBuilder()
                .append("select ")
                .append(" mde.nr_chave ")
                .append(" from")
                .append(" monitoramento_doc_eletronico mde")
                .append(" where")
                .append(" mde.id_docto_servico = :idDoctoServico");

        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("nr_chave", Hibernate.STRING);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);

                query.setParameter("idDoctoServico", idDoctoServico);
                return query.list();
            }
        };

        List<Object> listObj = getHibernateTemplate().executeFind(hcb);
        if(listObj != null && !listObj.isEmpty()){
            return String.valueOf(listObj.get(0));
        }
        return null;
    }

	@SuppressWarnings("unchecked")
    public List<String> findNrChaveByIdFatura(final Long idFatura) {

		final StringBuilder sql = new StringBuilder()
				.append("select ")
				.append(" mde.nr_chave ")
				.append(" from")
				.append(" devedor_doc_serv_fat dev,")
				.append(" monitoramento_doc_eletronico mde,")
				.append(" docto_servico doc,")
				.append(" fatura fat")
				.append(" where")
				.append(" mde.ds_dados_documento is not null")
				.append(" and mde.id_docto_servico = dev.id_docto_servico")
				.append(" and fat.id_fatura = dev.id_fatura")
				.append(" and doc.id_docto_servico = dev.id_docto_servico")
				.append(" and dev.id_fatura = :idFatura");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("nr_chave", Hibernate.STRING);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);

				query.setParameter("idFatura", idFatura);
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	}

	@SuppressWarnings("unchecked")
    public boolean findExistsConhecimentoEletronicoByCriteria(Map<String, Object> criteria) {
		final List<String> tpDocumentoServicoList =  (List<String>)criteria.get("tpDocumentoServicoList");
		final Long idFatura = (Long)criteria.get("idFatura");
		final StringBuilder sql = new StringBuilder()
				.append("select doc.id_docto_servico")
				.append(" from")
				.append(" devedor_doc_serv_fat dev,")
				.append(" monitoramento_doc_eletronico mde,")
				.append(" docto_servico doc")
				.append(" where")
				.append(" mde.ds_dados_documento is not null")
				.append(" and mde.id_docto_servico = dev.id_docto_servico")
				.append(" and mde.tp_situacao_documento in ('A','E')")
				.append(" and doc.tp_documento_servico in (:tpDocumentoServicoList)");
		sql.append(" and doc.id_docto_servico = dev.id_docto_servico")
				.append(" and dev.id_fatura = :idFatura ")
				.append(" and rownum = 1");
		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);
				query.setParameterList("tpDocumentoServicoList", tpDocumentoServicoList);
				query.setParameter("idFatura", idFatura);
				return query.list();
			}
		};

		List<Object[]> listObj = getHibernateTemplate().executeFind(hcb);

		if( listObj != null && !listObj.isEmpty()){
			return true;
		}
		return false;
	}
	
    // LMSA-6159 LMSA-6249
    private static final String FRAGMENTO_SQL_FROM_EVENTO_DOCUMENTO = 
            "MONITORAMENTO_DOC_ELETRONICO m, DOCTO_SERVICO d, EVENTO_DOCUMENTO_SERVICO ed, EVENTO e ";
    private static final String FRAGMENTO_SQL_WHERE_COUNT_EVENTO_DOCUMENTO =
            "AND d.ID_DOCTO_SERVICO = ed.ID_DOCTO_SERVICO " +
            "AND e.ID_EVENTO = ed.ID_EVENTO " +
            "AND e.CD_EVENTO IN (:cdsEventosEdiFedex) " +
            "AND m.id_docto_servico = d.id_docto_servico " +
            "AND m.id_monitoramento_doc_eletronic = :idMonitoramentoDocEletronico ";

    private static final String FRAGMENTO_SQL_FIELDS_EVENTO_DOCUMENTO =
            "d.ID_DOCTO_SERVICO, d.ID_FILIAL_ORIGEM, d.ID_FILIAL_DESTINO, e.CD_EVENTO, ed.ID_FILIAL ";
    
    /**
     * Verificar se existe Eventos vinculados ao documento de um determinado monitoramento de documento eletronico
     * @param idMonitoramentoDocEletronico
     * @return Integer
     * @author ernani.brandao
     */
    public Integer countReSendEventoDocumentoByControleCarga(Long idMonitoramentoDocEletronico, List<Short> cdsEventosEdiFedex) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        sql.append(FRAGMENTO_SQL_FROM_EVENTO_DOCUMENTO)
                .append(" WHERE 1=1 ")
                .append(FRAGMENTO_SQL_WHERE_COUNT_EVENTO_DOCUMENTO);
        
        Session session = super.getAdsmHibernateTemplate().getSessionFactory().openSession();
        
        try {
            SQLQuery query = session.createSQLQuery(sql.toString());
            query.setParameterList("cdsEventosEdiFedex", cdsEventosEdiFedex);
            query.setParameter("idMonitoramentoDocEletronico", idMonitoramentoDocEletronico);
            
            BigDecimal count = (BigDecimal) query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    /**
     * Recuperar todos os eventos de um documento de servico associado ao monitoramento documento eletronico informado
     * Ira retornar uma lista de EventoDocumentoServicoDMN
     * @param idMonitoramentoDocEletronico
     * @return List<EventoDocumentoServicoDMN>
     */
    @SuppressWarnings({ "unchecked", "rawtypes", "serial" })
    public List<EventoDocumentoServicoDMN> getReSendEventoDocumentoByControleCarga(Long idMonitoramentoDocEletronico, List<Short> cdsEventosEdiFedex) {
        StringBuilder sql = new StringBuilder("SELECT ")
                .append(FRAGMENTO_SQL_FIELDS_EVENTO_DOCUMENTO)
                .append(" FROM ")
                .append(FRAGMENTO_SQL_FROM_EVENTO_DOCUMENTO)
                .append(" WHERE 1=1 ")
                .append(FRAGMENTO_SQL_WHERE_COUNT_EVENTO_DOCUMENTO);
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cdsEventosEdiFedex", cdsEventosEdiFedex);
        params.put("idMonitoramentoDocEletronico", idMonitoramentoDocEletronico);
        
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
