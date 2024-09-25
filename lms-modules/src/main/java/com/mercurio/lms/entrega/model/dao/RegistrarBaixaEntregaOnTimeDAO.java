package com.mercurio.lms.entrega.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.entrega.model.RegistroDocumentoEntrega;
import com.mercurio.lms.entrega.model.TipoDocumentoEntrega;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistrarBaixaEntregaOnTimeDAO extends AdsmDao {

	public ControleCarga findControleCargaByMeioTransporte(Long idMeioTransporte, Long idFilial, String tpStatusControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class,"CC")
			.createAlias("CC.meioTransporteByIdTransportado","MT")
			.createAlias("CC.filialByIdFilialOrigem","F")
			.add(Restrictions.eq("MT.id",idMeioTransporte))
			.add(Restrictions.eq("F.id",idFilial))
			.addOrder(Order.desc("CC.id"));
		if (StringUtils.isNotBlank(tpStatusControleCarga)) {
			dc.add(Restrictions.eq("CC.tpStatusControleCarga", tpStatusControleCarga));
		}
		List<ControleCarga> list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (!list.isEmpty())
			return (ControleCarga)list.get(0);
		return null;
	}

	public EventoMeioTransporte findLastEvetoMeioTransporteByIdMeioTranspote(Long idMeioTransporte,Long idFilial, Long idControleCarga) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(EventoMeioTransporte.class.getName(),"EMT");
		hql.addCriteria("EMT.meioTransporte.id","=",idMeioTransporte);
		hql.addCriteria("EMT.filial.id","=",idFilial);
		hql.addCriteria("EMT.controleCarga.id","=",idControleCarga);
		hql.addCustomCriteria(
			"EMT.dhInicioEvento.value = (" +
				"select max(E2.dhInicioEvento.value) " +
				"from " + EventoMeioTransporte.class.getName() + " AS E2 " +
				"WHERE E2.meioTransporte.id = ? " +
				"and E2.filial.id = ? " +
				"and E2.controleCarga.id = ? " +
			")"
		);
		hql.addCriteriaValue(idMeioTransporte);
		hql.addCriteriaValue(idFilial);
		hql.addCriteriaValue(idControleCarga);
		hql.addOrderBy("EMT.id", "desc");
		
		List<EventoMeioTransporte> result = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (result.isEmpty()) 
			return null;
		return result.get(0);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		List<Object> paramValues = new ArrayList<Object>();

		sql.append(" FROM (");
		createSQL(sql, paramValues, criteria);
		sql.append(" )");

		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), paramValues.toArray());
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		StringBuilder sql = new StringBuilder();
		List<Object> paramValues = new ArrayList<Object>();

		sql.append("SELECT * FROM (");
		createSQL(sql, paramValues, criteria);
		sql.append(") ");

	    String tpOrdemDoc = criteria.getString("filial.tpOrdemDoc");
	    
	    //Verifica qual tipo de ordenação para listagem 
	    if(tpOrdemDoc != null || !"".equals(tpOrdemDoc)) {
	    	sql.append(" ORDER BY ");
	    	if(tpOrdemDoc.equals("DA")) {
	    	sql.append(" DH_INCLUSAO_DOCTO_SERVICO ASC");
	    	} else if(tpOrdemDoc.equals("AA")){
	    		sql.append("TP_DOCUMENTO_SERVICO ASC, ");	    	
	    		sql.append("SG_FILIAL_ORIGEM ASC, ");
	    		sql.append("NR_DOCTO_SERVICO ASC");	    	
	    	}
	    }
		
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("ID_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("DS_TP_DOCUMENTO_SERVICO", Hibernate.STRING);
				sqlQuery.addScalar("NR_DOCTO_SERVICO", Hibernate.LONG);
				sqlQuery.addScalar("TP_IDENTIFICACAO", Hibernate.STRING);
				sqlQuery.addScalar("DS_TP_IDENTIFICACAO", Hibernate.STRING);
				sqlQuery.addScalar("NR_IDENTIFICACAO", Hibernate.STRING);
				sqlQuery.addScalar("NM_PESSOA", Hibernate.STRING);
				sqlQuery.addScalar("ID_MANIFESTO_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("NR_MANIFESTO_ENTREGA", Hibernate.STRING);
				sqlQuery.addScalar("ID_FILIAL_MANIFESTO_ENTREGA", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_MANIFESTO_ENTREGA", Hibernate.STRING);
				sqlQuery.addScalar("DS_ENDERECO", Hibernate.STRING);
				sqlQuery.addScalar("NR_ORDEM", Hibernate.LONG);
				sqlQuery.addScalar("NR_CONTROLE_CARGA", Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL_CONTROLE_CARGA", Hibernate.STRING);
				sqlQuery.addScalar("ID_AWB", Hibernate.LONG);
				sqlQuery.addScalar("NM_CIA_AEREA", Hibernate.STRING);
			}
	 	};
	 	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), paramValues.toArray(), confSql);
	 	List<Object[]> records = rsp.getList();
	 	List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(records.size());
	 	Map<String, Object> result = null;
	 	for (Object[] record : records) {
			result = new HashMap<String, Object>();
			result.put("idFilialOrigem", record[0]);
			result.put("sgFilialOrigem", record[1]);
			result.put("idDoctoServico", record[2]);
			result.put("tpDocumentoServico", new DomainValue(record[3].toString(), new VarcharI18n(record[4].toString()), Boolean.TRUE));
			result.put("nrDoctoServico", record[5]);
			result.put("tpIdentificacao", new DomainValue(record[6].toString(), new VarcharI18n(record[7].toString()), Boolean.TRUE));
			result.put("nrIdentificacao", record[8]);
			result.put("nmPessoa", record[9]);
			result.put("idManifestoEntrega", record[10]);
			result.put("nrManifestoEntrega", record[11]);
			result.put("idFilialManifestoEntrega", record[12]);
			result.put("sgFilialManifestoEntrega", record[13]);
			result.put("dsEndereco", record[14]);
			result.put("nrOrdem", record[15]);
			result.put("nrControleCarga", record[16]);
			result.put("sgFilialControleCarga", record[17]);
			result.put("idAwb", record[18]);
			result.put("nmCiaAerea", record[19]);
			results.add(result);
		}

	 	rsp.setList(results);
	 	return rsp;
	}

	private void createSQL(StringBuilder sql, List<Object> paramValues, TypedFlatMap criteria) {

		sql.append(" select")
		.append("       F.ID_FILIAL as ID_FILIAL_ORIGEM,")
		.append("       F.SG_FILIAL as SG_FILIAL_ORIGEM,")
		.append("       DS.ID_DOCTO_SERVICO as ID_DOCTO_SERVICO,")
		.append("       DS.TP_DOCUMENTO_SERVICO as TP_DOCUMENTO_SERVICO,")
		.append(PropertyVarcharI18nProjection.createProjection("vdds.DS_VALOR_DOMINIO_I")).append("as DS_TP_DOCUMENTO_SERVICO,")
		.append("       DS.NR_DOCTO_SERVICO as NR_DOCTO_SERVICO,")
		.append("       C.TP_IDENTIFICACAO as TP_IDENTIFICACAO,")
		.append(PropertyVarcharI18nProjection.createProjection("vdp.DS_VALOR_DOMINIO_I")).append("as DS_TP_IDENTIFICACAO,")
		.append("       C.NR_IDENTIFICACAO as NR_IDENTIFICACAO,")
		.append("       C.NM_PESSOA as NM_PESSOA,")
		.append("       ME.ID_MANIFESTO_ENTREGA as ID_MANIFESTO_ENTREGA,")
		.append("       ME.NR_MANIFESTO_ENTREGA as NR_MANIFESTO_ENTREGA,")
		.append("       FM.ID_FILIAL AS ID_FILIAL_MANIFESTO_ENTREGA,")
		.append("       FM.SG_FILIAL AS SG_FILIAL_MANIFESTO_ENTREGA,")
		.append("       DS.DS_ENDERECO_ENTREGA_REAL as DS_ENDERECO,")
		.append("       PMD.NR_ORDEM as NR_ORDEM,")
		.append("       CC.NR_CONTROLE_CARGA as NR_CONTROLE_CARGA,")
		.append("       FCC.SG_FILIAL AS SG_FILIAL_CONTROLE_CARGA, ")
		.append("       DS.DH_INCLUSAO AS DH_INCLUSAO_DOCTO_SERVICO, ")
		.append("       AWB.ID_AWB AS ID_AWB, ")
		.append("       PES.NM_PESSOA AS NM_CIA_AEREA ")
		.append("  from MANIFESTO_ENTREGA_DOCUMENTO MED ")
		.append(" inner join DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
		.append(" inner join FILIAL F ON F.ID_FILIAL = DS.ID_FILIAL_ORIGEM ")
		.append(" inner join PESSOA C ON C.ID_PESSOA = DS.ID_CLIENTE_DESTINATARIO")
		.append(" inner join PESSOA FP ON FP.ID_PESSOA = DS.ID_FILIAL_ORIGEM")
		.append(" inner join MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA") 
		.append(" inner join FILIAL FM ON FM.ID_FILIAL = ME.ID_FILIAL")
		.append(" inner join MANIFESTO M ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA")
		.append(" inner join CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA")
		.append(" inner join FILIAL FCC ON FCC.ID_FILIAL = CC.ID_FILIAL_ORIGEM")
		.append(" inner join PRE_MANIFESTO_DOCUMENTO PMD ON PMD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PMD.ID_MANIFESTO = M.ID_MANIFESTO")
		.append(" inner join DOMINIO dds on dds.nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO'")
		.append(" inner join VALOR_DOMINIO vdds on vdds.vl_valor_dominio = ds.tp_documento_servico and vdds.id_dominio = dds.id_dominio")
		.append(" inner join DOMINIO dp on dp.nm_dominio = 'DM_TIPO_IDENTIFICACAO'")
		.append(" inner join VALOR_DOMINIO vdp on vdp.vl_valor_dominio = c.tp_identificacao and vdp.id_dominio = dp.id_dominio")
		.append(" LEFT JOIN (SELECT * FROM CTO_AWB CA1 WHERE CA1.ID_AWB = (SELECT MIN(AW.ID_AWB) FROM AWB AW JOIN CTO_AWB CA2 ON CA2.ID_AWB = AW.ID_AWB WHERE CA2.ID_CONHECIMENTO = CA1.ID_CONHECIMENTO AND AW.TP_STATUS_AWB = 'P')) CTOAWB ON DS.ID_DOCTO_SERVICO = CTOAWB.ID_CONHECIMENTO")
		.append(" left join AWB AWB on ctoawb.ID_AWB = AWB.ID_AWB ")
		.append(" left join CIA_FILIAL_MERCURIO CFM on AWB.ID_CIA_FILIAL_MERCURIO = cfm.ID_CIA_FILIAL_MERCURIO ")
		.append(" left join PESSOA PES on cfm.ID_EMPRESA = PES.ID_PESSOA ")

		.append(" where MED.ID_OCORRENCIA_ENTREGA IS NULL ");

		Long idFilialManifestoEntrega = criteria.getLong("filial.idFilial");
		sql.append("   AND ME.ID_FILIAL = ? ");
		paramValues.add(idFilialManifestoEntrega);

		Long idMeioTransporte = criteria.getLong("meioTransporte.idMeioTransporte");
		sql.append(" AND CC.ID_TRANSPORTADO = ? ");
		paramValues.add(idMeioTransporte);

		sql.append("   AND M.TP_MANIFESTO_ENTREGA IN (?, ?) ");
		paramValues.add("EN");
		paramValues.add("ED");

		Long idDoctoServico = criteria.getLong("idDoctoServico");
		if (idDoctoServico != null) {
			sql.append(" AND DS.ID_DOCTO_SERVICO = ? ");
			paramValues.add(idDoctoServico);
		}
		sql.append(" AND DS.BL_BLOQUEADO = ? ");
		paramValues.add("N");

		sql.append(" AND M.TP_STATUS_MANIFESTO <> ? ");
		paramValues.add("CA");
		
		String tpDocumentoServico = criteria.getString("doctoServico.tpDocumentoServico");
		if (StringUtils.isNotBlank(tpDocumentoServico)) {
			sql.append(" AND DS.TP_DOCUMENTO_SERVICO = ? ");
			paramValues.add(tpDocumentoServico);
		}

		Long idFilialOrigemDoctoServico = criteria.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
		if(idFilialOrigemDoctoServico != null) {
			sql.append(" AND DS.ID_FILIAL_ORIGEM = ? ");
			paramValues.add(idFilialOrigemDoctoServico);
		}

		Long idControleCarga = criteria.getLong("idControleCarga");
		if(idControleCarga != null) {
			sql.append(" AND CC.ID_CONTROLE_CARGA = ? ");
			paramValues.add(idControleCarga);
		}
		
		Long idAwb = criteria.getLong("idAwb");
		if(idAwb != null){
			sql.append(" AND AWB.ID_AWB = ? ");
			paramValues.add(idAwb);
		}

		sql.append(" UNION ");

		sql.append(" SELECT ")
		.append("       null as ID_FILIAL_ORIGEM,")
		.append("       null as SG_FILIAL_ORIGEM,")
		.append("       null as ID_DOCTO_SERVICO,")
		.append("       '").append(tpDocumentoServico).append("' as TP_DOCUMENTO_SERVICO,")
		.append("       null as DS_TP_DOCUMENTO_SERVICO,")
		.append("       null as NR_DOCTO_SERVICO,")
		.append("       null as TP_IDENTIFICACAO,")
		.append("       null as DS_TP_IDENTIFICACAO,")
		.append("       null as NR_IDENTIFICACAO,")
		.append("       null as NM_PESSOA,")
		.append("       null as ID_MANIFESTO_ENTREGA,")
		.append("       null as NR_MANIFESTO_ENTREGA,")
		.append("       null AS ID_FILIAL_MANIFESTO_ENTREGA,")
		.append("       null AS SG_FILIAL_MANIFESTO_ENTREGA,")
		.append("       null as DS_ENDERECO,")
		.append("       PMD.NR_ORDEM as NR_ORDEM,")
		.append("       CC.NR_CONTROLE_CARGA as NR_CONTROLE_CARGA,")
		.append("       FCC.SG_FILIAL AS SG_FILIAL_CONTROLE_CARGA, ")
		.append("       null AS DH_INCLUSAO_DOCTO_SERVICO, ")
		.append("       null AS ID_AWB, ")
		.append("       null AS NM_CIA_AEREA ")
		.append("  FROM MANIFESTO M ")
		.append(" INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
		.append(" INNER JOIN FILIAL FCC ON FCC.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
		.append(" INNER JOIN MANIFESTO_VIAGEM_NACIONAL MVN ON MVN.ID_MANIFESTO_VIAGEM_NACIONAL = M.ID_MANIFESTO ")
		.append(" INNER JOIN FILIAL F ON M.ID_FILIAL_ORIGEM = F.ID_FILIAL ")
		.append(" INNER JOIN PRE_MANIFESTO_DOCUMENTO PMD ON PMD.ID_MANIFESTO = M.ID_MANIFESTO ");

		sql.append(" WHERE F.ID_FILIAL = ? ");
		paramValues.add(idFilialManifestoEntrega);

		sql.append("   AND M.TP_MANIFESTO_VIAGEM = ? ");
		paramValues.add("EA");

		sql.append("   AND CC.ID_TRANSPORTADO = ? ");
		paramValues.add(idMeioTransporte);

		if (idDoctoServico != null) {
			sql.append(" AND M.ID_MANIFESTO = ? ");
			paramValues.add(idDoctoServico);
		}

		if(idControleCarga != null) {
			sql.append(" AND CC.ID_CONTROLE_CARGA = ? ");
			paramValues.add(idControleCarga);
		}

	}

	public Integer getRowCountConfirmation(TypedFlatMap criteria) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		createSQLConfirmation(sql, parameters, criteria);
		sql.insert(0,"FROM (").append(")");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(),parameters);
	}

	public ResultSetPage findPaginatedConfirmation(TypedFlatMap criteria, FindDefinition findDef) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		createSQLConfirmation(sql, parameters, criteria);
		sql.insert(0,"select * from (").append(") order by tp_documento_servico, sg_filial_origem_ds, nr_docto_servico");

		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("id_manifesto", Hibernate.LONG);
				sqlQuery.addScalar("nr_manifesto_entrega", Hibernate.STRING);
				sqlQuery.addScalar("sg_filial_manifesto_entrega", Hibernate.STRING);
				sqlQuery.addScalar("nr_manifesto_viagem", Hibernate.STRING);
				sqlQuery.addScalar("sg_filial_manifesto_viagem", Hibernate.STRING);
				sqlQuery.addScalar("id_docto_servico", Hibernate.LONG);
				sqlQuery.addScalar("nr_docto_servico", Hibernate.STRING);
				sqlQuery.addScalar("tp_documento_servico", Hibernate.STRING);
				sqlQuery.addScalar("ds_tp_documento_servico", Hibernate.STRING);
				sqlQuery.addScalar("ds_endereco_entrega", Hibernate.STRING);
				sqlQuery.addScalar("sg_filial_origem_ds", Hibernate.STRING);
				sqlQuery.addScalar("nm_cliente_destinatario", Hibernate.STRING);
				sqlQuery.addScalar("nr_identificacao_destinatario", Hibernate.STRING);
				sqlQuery.addScalar("tp_identificacao_destinatario", Hibernate.STRING);
				sqlQuery.addScalar("ds_tp_identificacao_dest", Hibernate.STRING);
				sqlQuery.addScalar("cd_ocorrencia_entrega", Hibernate.STRING);
				sqlQuery.addScalar("ds_ocorrencia_entrega", Hibernate.STRING);
				sqlQuery.addScalar("nr_controle_carga", Hibernate.STRING);
				sqlQuery.addScalar("sg_filial_origem_cc", Hibernate.STRING);
				sqlQuery.addScalar("nm_recebedor", Hibernate.STRING);
				sqlQuery.addScalar("id_awb", Hibernate.LONG);
				sqlQuery.addScalar("nm_cia_aerea", Hibernate.STRING);
			}
		};

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),findDef.getCurrentPage(),findDef.getPageSize(), parameters, confSql);
	 	List<Object[]> records = rsp.getList();
	 	List<Map<String, Object>> results = new ArrayList<Map<String, Object>>(records.size());
	 	Map<String, Object> result = null;
	 	for (Object[] record : records) {
			result = new HashMap<String, Object>();
			result.put("idManifesto", record[0]);
			result.put("nrManifestoEntrega", record[1]);
			result.put("sgFilialManifestoEntrega", record[2]);
			result.put("nrManifestoViagem", record[3]);
			result.put("sgFilialManifestoViagem", record[4]);
			result.put("idDoctoServico", record[5]);
			result.put("nrDoctoServico", record[6]);
			result.put("tpDocumentoServico", new DomainValue(record[7].toString(), new VarcharI18n(record[8].toString()), Boolean.TRUE));
			result.put("dsEnderecoEntrega", record[9]);
			result.put("sgFilialOrigemDoctoServico", record[10]);
			result.put("nmClienteDestinatario", record[11]);
			result.put("nrIdentificacaoClienteDestinatario", record[12]);
			result.put("tpIdentificacaoClienteDestinatario", new DomainValue(record[13].toString(), new VarcharI18n(record[14].toString()), Boolean.TRUE));
			result.put("cdOcorrenciaEntrega", record[15]);
			result.put("dsOcorrenciaEntrega", record[16]);
			result.put("nrControleCarga", record[17]);
			result.put("sgFilialControleCarga", record[18]);
			result.put("nmRecebedorControleCarga", record[19]);
			result.put("idAwb", record[20]);
			result.put("ciaAerea", record[21]);
			results.add(result);
		}

	 	rsp.setList(results);
	 	return rsp;
	}

	private void createSQLConfirmation(StringBuilder sql, Map<String, Object> parameters, TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong("idControleCarga");
		Long idManifestoViagemNacional = criteria.getLong("manifesto.manifestoViagemNacional.idManifestoViagemNacional");

		String statusControleCargaWhere;		
		if(criteria != null && criteria.getBoolean("isBaixaOntime") && criteria.getBoolean("isBaixaOntime").equals(Boolean.TRUE)) {
			statusControleCargaWhere = " = ";
		} else {
			statusControleCargaWhere = " <> ";
		}

		if(idControleCarga != null) {
			sql.append("select ")
			.append("       me.id_manifesto_entrega as id_manifesto,")
			.append("       me.nr_manifesto_entrega as nr_manifesto_entrega,")
			.append("       fm.sg_filial as sg_filial_manifesto_entrega,")
			.append("       NULL as nr_manifesto_viagem,")
			.append("       NULL as sg_filial_manifesto_viagem,")
			.append("       ds.id_docto_servico,")
			.append("       ds.nr_docto_servico,")
			.append("       ds.tp_documento_servico,")
			.append(PropertyVarcharI18nProjection.createProjection("vdds.ds_valor_dominio_i")).append("as ds_tp_documento_servico,")
			.append("       ds.ds_endereco_entrega_real as ds_endereco_entrega,")
			.append("       f.sg_filial as sg_filial_origem_ds, ")
			.append("       c.nm_pessoa as nm_cliente_destinatario, ")
			.append("       c.nr_identificacao as nr_identificacao_destinatario,")
			.append("       c.tp_identificacao as tp_identificacao_destinatario,")
			.append(PropertyVarcharI18nProjection.createProjection("vdp.ds_valor_dominio_i")).append("as ds_tp_identificacao_dest,")
			.append("       oe.cd_ocorrencia_entrega,")
			.append(PropertyVarcharI18nProjection.createProjection("oe.ds_ocorrencia_entrega_i", "ds_ocorrencia_entrega, "))
			.append("       cc.nr_controle_carga,")
			.append("       fcc.sg_filial as sg_filial_origem_cc,")
			.append("       med.nm_recebedor as nm_recebedor,")
			.append("       AWB.ID_AWB AS id_awb, ")
			.append("       PES.NM_PESSOA AS nm_cia_aerea ")
			.append("  from MANIFESTO_ENTREGA_DOCUMENTO med ")
			.append(" inner join DOCTO_SERVICO ds on ds.id_docto_servico = med.id_docto_servico ")
			.append(" inner join FILIAL f on f.id_filial = ds.id_filial_origem ")
			.append(" inner join PESSOA c on c.id_pessoa = ds.id_cliente_destinatario ")
			.append(" inner join OCORRENCIA_ENTREGA oe on oe.id_ocorrencia_entrega = med.id_ocorrencia_entrega ")
			.append(" inner join PESSOA fp on fp.id_pessoa = ds.id_filial_origem ")
			.append(" inner join MANIFESTO_ENTREGA me on me.id_manifesto_entrega = med.id_manifesto_entrega ")
			.append(" inner join FILIAL fm on fm.id_filial = me.id_filial ")
			.append(" inner join MANIFESTO m on m.id_manifesto = me.id_manifesto_entrega ")
			.append(" inner join CONTROLE_CARGA cc on cc.id_controle_carga = m.id_controle_carga ")
			.append(" inner join FILIAL fcc on fcc.id_filial = cc.id_filial_origem ")
			.append(" inner join DOMINIO dds on dds.nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO'")
			.append(" inner join VALOR_DOMINIO vdds on vdds.vl_valor_dominio = ds.tp_documento_servico and vdds.id_dominio = dds.id_dominio")
			.append(" inner join DOMINIO dp on dp.nm_dominio = 'DM_TIPO_IDENTIFICACAO'")
			.append(" inner join VALOR_DOMINIO vdp on vdp.vl_valor_dominio = c.tp_identificacao and vdp.id_dominio = dp.id_dominio")
			.append(" left join (SELECT * FROM CTO_AWB CA1 WHERE CA1.ID_AWB = (SELECT MIN(AW.ID_AWB) FROM AWB AW JOIN CTO_AWB CA2 ON CA2.ID_AWB = AW.ID_AWB WHERE CA2.ID_CONHECIMENTO = CA1.ID_CONHECIMENTO AND AW.TP_STATUS_AWB = 'P')) CTOAWB ON DS.ID_DOCTO_SERVICO = CTOAWB.ID_CONHECIMENTO")
			.append(" left join AWB AWB on ctoawb.ID_AWB = AWB.ID_AWB ")
			.append(" left join CIA_FILIAL_MERCURIO CFM on AWB.ID_CIA_FILIAL_MERCURIO = cfm.ID_CIA_FILIAL_MERCURIO ")
			.append(" left join PESSOA PES on cfm.ID_EMPRESA = PES.ID_PESSOA ")			
			.append(" where med.id_ocorrencia_entrega is not null")
			.append("   and m.tp_status_manifesto <> :tpStatusManifesto")
			.append("   and cc.id_controle_carga = :idControleCarga")
			.append("   and cc.tp_status_controle_carga " + statusControleCargaWhere + " :tpStatusControleCarga");

			
			parameters.put("tpStatusManifesto", "FE");
			parameters.put("idControleCarga", idControleCarga);
			parameters.put("tpStatusControleCarga", "TC");
		}

		if( (idControleCarga != null) && (idManifestoViagemNacional != null)) {
			sql.append(" UNION ALL ");
		}

		if(idManifestoViagemNacional != null) {
			sql.append("select ")
			.append("       mvn.id_manifesto_viagem_nacional as id_manifesto,")
			.append("       NULL as nr_manifesto_entrega,")
			.append("       NULL as sg_filial_manifesto_entrega,")
			.append("       mvn.nr_manifesto_origem as nr_manifesto_viagem,")
			.append("       fmvn.sg_filial as sg_filial_manifesto_viagem,")
			.append("       ds.id_docto_servico,")
			.append("       ds.nr_docto_servico,")
			.append("       ds.tp_documento_servico,")
			.append(PropertyVarcharI18nProjection.createProjection("vdds.ds_valor_dominio_i")).append("as ds_tp_documento_servico,")
			.append("       ds.ds_endereco_entrega_real as ds_endereco_entrega,")
			.append("       fo.sg_filial as sg_filial_origem_ds,")
			.append("       pcd.nm_pessoa as nm_cliente_destinatario,")
			.append("       pcd.nr_identificacao as nr_identificacao_destinatario,")
			.append("       pcd.tp_identificacao as tp_identificacao_destinatario,")
			.append(PropertyVarcharI18nProjection.createProjection("vdp.ds_valor_dominio_i")).append("as ds_tp_identificacao_dest,")
			.append("       oc.cd_ocorrencia_entrega,")
			.append(PropertyVarcharI18nProjection.createProjection("oc.ds_ocorrencia_entrega_i", "ds_ocorrencia_entrega,"))
			.append("       cc.nr_controle_carga,")
			.append("       fcc.sg_filial as sg_filial_origem_cc,")
			.append("       NULL as nm_recebedor,")
			.append("       NULL AS id_awb, ")
			.append("       NULL AS nm_cia_aerea ")	
			.append("  from CONTROLE_CARGA cc")
			.append(" inner join MANIFESTO m on m.id_controle_carga = cc.id_controle_carga")
			.append(" inner join MANIFESTO_VIAGEM_NACIONAL mvn on mvn.id_manifesto_viagem_nacional = m.id_manifesto")
			.append(" inner join MANIFESTO_NACIONAL_CTO mnc on mnc.id_manifesto_viagem_nacional = mvn.id_manifesto_viagem_nacional")
			.append(" inner join FILIAL fmvn on fmvn.id_filial = mvn.id_filial")
			.append(" inner join DOCTO_SERVICO ds on ds.id_docto_servico = mnc.id_conhecimento")
			.append(" inner join FILIAL fo on fo.id_filial = ds.id_filial_origem")
			.append(" inner join FILIAL fcc on fcc.id_filial = cc.id_filial_origem")
			.append(" inner join PESSOA pcd on pcd.id_pessoa = ds.id_cliente_destinatario")
			.append(" inner join EVENTO_DOCUMENTO_SERVICO eds on eds.id_docto_servico = ds.id_docto_servico")
			.append(" inner join OCORRENCIA_ENTREGA oc on oc.id_ocorrencia_entrega = eds.id_ocorrencia_entrega")
			.append(" inner join DOMINIO dds on dds.nm_dominio = 'DM_TIPO_DOCUMENTO_SERVICO'")
			.append(" inner join VALOR_DOMINIO vdds on vdds.vl_valor_dominio = ds.tp_documento_servico and vdds.id_dominio = dds.id_dominio")
			.append(" inner join DOMINIO dp on dp.nm_dominio = 'DM_TIPO_IDENTIFICACAO'")
			.append(" inner join VALOR_DOMINIO vdp on vdp.vl_valor_dominio = pcd.tp_identificacao and vdp.id_dominio = dp.id_dominio")
			.append(" where mvn.id_manifesto_viagem_nacional = :idManifestoViagemNacional")
			.append("   and m.tp_manifesto_viagem = :tpManifestoViagem")
			.append("	and m.tp_status_manifesto in (:tpStatus1, :tpStatus2, :tpStatus3, :tpStatus4) ");

			parameters.put("idManifestoViagemNacional", idManifestoViagemNacional);
			parameters.put("tpManifestoViagem", "ED");
			parameters.put("tpStatus1", "EV");
			parameters.put("tpStatus2", "AD");
			parameters.put("tpStatus3", "ED");
			parameters.put("tpStatus4", "DC");
		}

	}

	public List<TipoDocumentoEntrega> findRegistroDocumentoEntregaBy(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("TDE");

		hql.addFrom(new StringBuffer(RegistroDocumentoEntrega.class.getName()).append(" RDE ")
			.append("INNER JOIN RDE.tipoDocumentoEntrega TDE ").toString());

		hql.addCriteria("RDE.tpSituacaoRegistro","=","CR");

		hql.addCriteria("RDE.doctoServico.id","=",idDoctoServico);

		hql.addOrderBy(PropertyVarcharI18nProjection.createProjection("TDE.dsTipoDocumentoEntrega"));

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

}