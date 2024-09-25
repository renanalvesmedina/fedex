package com.mercurio.lms.entrega.model.dao;

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
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class RegistrarBaixaEntregaDAO extends AdsmDao {

	public ControleCarga findControleCargaByMeioTransporte(Long idMeioTransporte, Long idFilial, String tpStatusControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class,"CC")
			.createAlias("CC.meioTransporteByIdTransportado","MT")
			.createAlias("CC.filialByIdFilialOrigem","F")
			.add(Restrictions.eq("CC.tpStatusControleCarga",tpStatusControleCarga))
			.add(Restrictions.eq("MT.id",idMeioTransporte))
			.add(Restrictions.eq("F.id",idFilial))
			.addOrder(Order.desc("CC.id"));
		List list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
		if (!list.isEmpty())
			return (ControleCarga)list.get(0);
		return null;
	}

	public EventoMeioTransporte findLastEvetoMeioTransporteByIdMeioTranspote(Long idMeioTransporte,Long idFilial) {
		SqlTemplate hql = new SqlTemplate();
		hql.addFrom(EventoMeioTransporte.class.getName(),"EMT");
		hql.addCriteria("EMT.meioTransporte.id","=",idMeioTransporte);
		hql.addCriteria("EMT.filial.id","=",idFilial);
		hql.addCustomCriteria(new StringBuilder("EMT.dhInicioEvento.value = (select max(E2.dhInicioEvento.value) from ").append(EventoMeioTransporte.class.getName()).append(" AS E2 ")
			.append("WHERE E2.meioTransporte.id = ? and E2.filial.id = ?)").toString());
		hql.addCriteriaValue(idMeioTransporte);
		hql.addCriteriaValue(idFilial);
		return (EventoMeioTransporte)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		Map parameters = new HashMap();
		StringBuilder sql = new StringBuilder();
		createSQL(sql,parameters,criteria);
		sql.insert(0,"FROM (").append(")");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(),parameters);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		Map parameters = new HashMap();
		StringBuilder sql = new StringBuilder();
		createSQL(sql,parameters,criteria);
		sql.insert(0,"SELECT * FROM (");
		sql.append(") ");

	    String tpOrdemDoc = criteria.getString("filialByIdFilialOrigem.tpOrdemDoc");
	    
	    //Verifica qual tipo de ordenação para listagem 
	    if(tpOrdemDoc != null || !"".equals(tpOrdemDoc)) {
	    	sql.append(" ORDER BY ");
	    	if(tpOrdemDoc.equals("DA")) {
	    	sql.append(" DH_INCLUSAO ASC");
	    	} else if(tpOrdemDoc.equals("AA")){
	    	sql.append("TP_DOCUMENTO_SERVICO ASC, ");	    	
	    	sql.append("SG_FILIAL ASC, ");
	    	sql.append("NR_DOCTO_SERVICO ASC"); 	
	    	}
	    }


		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				/*00*/ sqlQuery.addScalar("TP_DOCUMENTO_SERVICO",Hibernate.STRING); 
				/*01*/ sqlQuery.addScalar("SG_FILIAL",Hibernate.STRING);
				/*02*/ sqlQuery.addScalar("NR_DOCTO_SERVICO",Hibernate.STRING);
				/*03*/ sqlQuery.addScalar("TP_IDENTIFICACAO",Hibernate.STRING);
				/*04*/ sqlQuery.addScalar("NR_IDENTIFICACAO",Hibernate.STRING);
				/*05*/ sqlQuery.addScalar("NM_PESSOA",Hibernate.STRING);
				/*06*/ sqlQuery.addScalar("SG_FILIAL_1",Hibernate.STRING);
				/*07*/ sqlQuery.addScalar("NR_MANIFESTO_ENTREGA",Hibernate.STRING);
				/*08*/ sqlQuery.addScalar("ID_DOCTO_SERVICO",Hibernate.LONG);
				/*09*/ sqlQuery.addScalar("ID_MANIFESTO",Hibernate.LONG);
				/*10*/ sqlQuery.addScalar("DS_ENDERECO",Hibernate.STRING);
				/*11*/ sqlQuery.addScalar("ID_FILIAL",Hibernate.LONG);
				/*12*/ sqlQuery.addScalar("ID_FILIAL_1",Hibernate.LONG);
				/*13*/ sqlQuery.addScalar("NR_ORDEM",Hibernate.LONG);
				/*14*/ sqlQuery.addScalar("NR_CONTROLE_CARGA",Hibernate.STRING);
				/*15*/ sqlQuery.addScalar("SG_FILIAL_2",Hibernate.STRING);
				/*16*/ sqlQuery.addScalar("SG_FILIAL_MDV",Hibernate.STRING);
				/*17*/ sqlQuery.addScalar("NR_MANIFESTO_MDV",Hibernate.STRING);
				/*18*/ sqlQuery.addScalar("TP_MANIFESTO",Hibernate.STRING);
				/*19*/ sqlQuery.addScalar("ID_AWB",Hibernate.LONG);
				/*20*/ sqlQuery.addScalar("NM_CIA_AEREA",Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findPaginatedBySql(
				sql.toString(),findDef.getCurrentPage(),findDef.getPageSize(),parameters,confSql);
	}

	private void createSQL(StringBuilder sql, Map parameters, TypedFlatMap parametersView) {
		Long idManifestoEntrega = parametersView.getLong("manifestoEntrega.idManifestoEntrega");
		Long idManifestoViagemNacional = parametersView.getLong("manifesto.manifestoViagemNacional.idManifestoViagemNacional");

		Long idDoctoServico = parametersView.getLong("idDoctoServico");
		String tpDocumentoServico = parametersView.getString("doctoServico.tpDocumentoServico");
		Long idFilialOrigemDoctoServico = parametersView.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
		Long idAwb = parametersView.getLong("idAwb");

		if (idManifestoEntrega != null || idManifestoViagemNacional == null) {
			sql.append("SELECT ")
			.append("	DS.TP_DOCUMENTO_SERVICO, ")
			.append("	F.SG_FILIAL, ")
			.append("	DS.NR_DOCTO_SERVICO, ")
			.append("	C.TP_IDENTIFICACAO, ")
			.append("	C.NR_IDENTIFICACAO, ")
			.append("	C.NM_PESSOA, ")
			.append("	FM.SG_FILIAL AS SG_FILIAL_1, ")
			.append("	ME.NR_MANIFESTO_ENTREGA, ")
			.append("	DS.ID_DOCTO_SERVICO, ")
			.append("	ME.ID_MANIFESTO_ENTREGA AS ID_MANIFESTO, ")
			.append("	DS.DS_ENDERECO_ENTREGA_REAL AS DS_ENDERECO, ")
			.append("	F.ID_FILIAL, ")
			.append("	FM.ID_FILIAL AS ID_FILIAL_1, ")
			.append("	PMD.NR_ORDEM, ")
			.append("	CC.NR_CONTROLE_CARGA, ")
			.append("	FCC.SG_FILIAL AS SG_FILIAL_2, ")
			.append("	NULL as SG_FILIAL_MDV, ")
			.append("	NULL AS NR_MANIFESTO_MDV, ")
			.append("	'E' AS TP_MANIFESTO, ")
			.append("   DS.DH_INCLUSAO, ")
			.append("   AWB.ID_AWB AS ID_AWB, ")
			.append("   PES.NM_PESSOA AS NM_CIA_AEREA ")
			
		/* JOINS DOCUMENTO SERVIÇO */
			.append("FROM DOCTO_SERVICO DS ")
			.append("INNER JOIN FILIAL F ON F.ID_FILIAL = DS.ID_FILIAL_ORIGEM ")
			.append("INNER JOIN PESSOA C ON C.ID_PESSOA = DS.ID_CLIENTE_DESTINATARIO ")
			.append("INNER JOIN PESSOA FP ON FP.ID_PESSOA = DS.ID_FILIAL_ORIGEM ")
		/* JOINS MANIFESTO */
			.append("INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO MED ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")			
			.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ")
			.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
			.append("INNER JOIN FILIAL FM ON FM.ID_FILIAL = ME.ID_FILIAL ")
		/* JOINS CONTROLE CARGA */
			.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
			.append("INNER JOIN FILIAL FCC ON FCC.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
			.append("INNER JOIN PRE_MANIFESTO_DOCUMENTO PMD ON PMD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PMD.ID_MANIFESTO = M.ID_MANIFESTO ")
			.append(" LEFT JOIN (SELECT * FROM CTO_AWB CA1 WHERE CA1.ID_AWB = (SELECT MIN(AW.ID_AWB) FROM AWB AW JOIN CTO_AWB CA2 ON CA2.ID_AWB = AW.ID_AWB WHERE CA2.ID_CONHECIMENTO = CA1.ID_CONHECIMENTO AND AW.TP_STATUS_AWB = 'P')) CTOAWB ON DS.ID_DOCTO_SERVICO = CTOAWB.ID_CONHECIMENTO")
			.append(" LEFT JOIN AWB AWB on ctoawb.ID_AWB = AWB.ID_AWB ")
			.append(" LEFT JOIN CIA_FILIAL_MERCURIO CFM on AWB.ID_CIA_FILIAL_MERCURIO = cfm.ID_CIA_FILIAL_MERCURIO ")
			.append(" LEFT JOIN PESSOA PES on cfm.ID_EMPRESA = PES.ID_PESSOA ")
			
			/* WHERE */
			.append("WHERE MED.ID_OCORRENCIA_ENTREGA IS NULL ")
			.append("AND M.TP_STATUS_MANIFESTO not in (:tpStatusManifestoFechado,:tpStatusManifestoCancelado) ")
			.append("AND CC.DH_CHEGADA_COLETA_ENTREGA IS NOT NULL ")
			.append("AND ME.ID_FILIAL = :idFilial ")
			.append("AND TP_MANIFESTO_ENTREGA IN ('EN','ED','EP') ")
			.append("AND DS.BL_BLOQUEADO = 'N'");

			if (idManifestoEntrega != null) {
				sql.append("AND ME.ID_MANIFESTO_ENTREGA = :idManifestoEntrega ");
				parameters.put("idManifestoEntrega", idManifestoEntrega);
			}
			if (idDoctoServico != null) {
				sql.append("AND DS.ID_DOCTO_SERVICO = :idDoctoServico ");
			}				
			if (parametersView.getLong("controleCarga.idControleCarga") != null) {
				sql.append("AND CC.ID_CONTROLE_CARGA = :idControleCarga ");
			}
			if (StringUtils.isNotBlank(tpDocumentoServico)) {
				sql.append("AND DS.TP_DOCUMENTO_SERVICO = :tpDoctoServico ");
			}
			if (idFilialOrigemDoctoServico != null) {
				sql.append("AND DS.ID_FILIAL_ORIGEM = :idFilialOrigem ");
			}
			
			if(idAwb != null){
				sql.append("AND AWB.ID_AWB = :idAwb ");
				parameters.put("idAwb", idAwb);
			}
			
			// TODO: FALTA AJUSTA O DH_BAIXA_AEROPORTO -> ISSO FICARA PARA A MUDANCA
			//DH_BAIXA_AEROPORTO IS NULL
			parameters.put("tpManifesto", "EA");
			parameters.put("tpStatusManifestoFechado", "FE");
			parameters.put("tpStatusManifestoCancelado", "CA");
			parameters.put("idMeioTransporte", parametersView.getLong("meioTransporte.idMeioTransporte"));

			/* [CQPRO00026139] v */
		
			sql.append(" UNION ALL ")
			
			.append("SELECT DISTINCT")
			.append("	DS.TP_DOCUMENTO_SERVICO, ")
			.append("	F.SG_FILIAL, ")
			.append("	DS.NR_DOCTO_SERVICO, ")
			.append("	C.TP_IDENTIFICACAO, ")
			.append("	C.NR_IDENTIFICACAO, ")
			.append("	C.NM_PESSOA, ")
			.append("	FM.SG_FILIAL AS SG_FILIAL_1, ")
			.append("	ME.NR_MANIFESTO_ENTREGA, ")			
			.append("	DS.ID_DOCTO_SERVICO, ")
			.append("	ME.ID_MANIFESTO_ENTREGA AS ID_MANIFESTO, ")
			.append("	DS.DS_ENDERECO_ENTREGA_REAL AS DS_ENDERECO, ")
			.append("	F.ID_FILIAL, ")
			.append("	FM.ID_FILIAL AS ID_FILIAL_1, ")
			.append("	-1 AS NR_ORDEM, ")
			.append("	CC.NR_CONTROLE_CARGA, ")
			.append("	FCC.SG_FILIAL AS SG_FILIAL_2, ")
			.append("	NULL as SG_FILIAL_MDV, ")
			.append("	NULL AS NR_MANIFESTO_MDV, ")
			.append("	'E' AS TP_MANIFESTO, ")
			.append("   DS.DH_INCLUSAO, ")
			.append("   AWB.ID_AWB AS ID_AWB, ")
			.append("   PES.NM_PESSOA AS NM_CIA_AEREA ")
			
		/* JOINS DOCUMENTO SERVIÇO */
			.append("FROM DOCTO_SERVICO DS ")
			.append("INNER JOIN FILIAL F ON F.ID_FILIAL = DS.ID_FILIAL_ORIGEM ")
			.append("INNER JOIN PESSOA C ON C.ID_PESSOA = DS.ID_CLIENTE_DESTINATARIO ")
			.append("INNER JOIN PESSOA FP ON FP.ID_PESSOA = DS.ID_FILIAL_ORIGEM ")
		/* JOINS MANIFESTO */
			.append("INNER JOIN MANIFESTO_ENTREGA_VOLUME MEV ON DS.ID_DOCTO_SERVICO = MEV.ID_DOCTO_SERVICO ")			
			.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MEV.ID_MANIFESTO_ENTREGA ")
			.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
			.append("INNER JOIN FILIAL FM ON FM.ID_FILIAL = ME.ID_FILIAL ")			
		/* JOINS CONTROLE CARGA */
			.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")			
			.append("INNER JOIN FILIAL FCC ON FCC.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
			.append(" LEFT JOIN (SELECT * FROM CTO_AWB CA1 WHERE CA1.ID_AWB = (SELECT MIN(AW.ID_AWB) FROM AWB AW JOIN CTO_AWB CA2 ON CA2.ID_AWB = AW.ID_AWB WHERE CA2.ID_CONHECIMENTO = CA1.ID_CONHECIMENTO AND AW.TP_STATUS_AWB = 'P')) CTOAWB ON DS.ID_DOCTO_SERVICO = CTOAWB.ID_CONHECIMENTO")
			.append(" LEFT JOIN AWB AWB on ctoawb.ID_AWB = AWB.ID_AWB ")
			.append(" LEFT JOIN CIA_FILIAL_MERCURIO CFM on AWB.ID_CIA_FILIAL_MERCURIO = cfm.ID_CIA_FILIAL_MERCURIO ")
			.append(" LEFT JOIN PESSOA PES on cfm.ID_EMPRESA = PES.ID_PESSOA ")
			
		/* COMENTADO POOS NÃO DEVERIA FAZER JOIN COM PRE MANIFESTO DOCUMENTO!!! */
		//	.append("INNER JOIN PRE_MANIFESTO_DOCUMENTO PMD ON PMD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND PMD.ID_MANIFESTO = M.ID_MANIFESTO ")
			/* WHERE */
			.append("WHERE MEV.ID_OCORRENCIA_ENTREGA IS NULL ")
			.append("AND MEV.ID_MANIFESTO_ENTREGA_DOCUMENTO IS NULL ")
			.append("AND M.TP_STATUS_MANIFESTO not in (:tpStatusManifestoFechado,:tpStatusManifestoCancelado) ")
			.append("AND CC.DH_CHEGADA_COLETA_ENTREGA IS NOT NULL ")
			.append("AND ME.ID_FILIAL = :idFilial ")
			.append("AND TP_MANIFESTO_ENTREGA IN ('EN','ED','EP') ")
			.append("AND DS.BL_BLOQUEADO = 'N' ");
			
			if (idManifestoEntrega != null) {
				sql.append("AND ME.ID_MANIFESTO_ENTREGA = :idManifestoEntrega ");
				parameters.put("idManifestoEntrega", idManifestoEntrega);
			}
			if (idDoctoServico != null) {
				sql.append("AND DS.ID_DOCTO_SERVICO = :idDoctoServico ");
			}	
			if (parametersView.getLong("controleCarga.idControleCarga") != null) {
				sql.append("AND CC.ID_CONTROLE_CARGA = :idControleCarga ");
			}
			if (StringUtils.isNotBlank(tpDocumentoServico)) {
				sql.append("AND DS.TP_DOCUMENTO_SERVICO = :tpDoctoServico ");
			}
			if (idFilialOrigemDoctoServico != null) {
				sql.append("AND DS.ID_FILIAL_ORIGEM = :idFilialOrigem ");
			}

			if(idAwb != null){
				sql.append("AND AWB.ID_AWB = :idAwb ");
			}

			
			// TODO: FALTA AJUSTA O DH_BAIXA_AEROPORTO -> ISSO FICARA PARA A MUDANCA
			//DH_BAIXA_AEROPORTO IS NULL
			parameters.put("tpManifesto", "EA");
			parameters.put("tpStatusManifestoFechado", "FE");
			parameters.put("tpStatusManifestoCancelado", "CA");
			parameters.put("idMeioTransporte", parametersView.getLong("meioTransporte.idMeioTransporte"));
		
			/* [CQPRO00026139] ^ */		
		}

		

		if (idManifestoEntrega != null && idManifestoViagemNacional != null) {
			sql.append(" UNION ALL ");
		}

		if (idManifestoViagemNacional != null) {
			sql.append("SELECT ")
			.append("	DS.TP_DOCUMENTO_SERVICO, ")
			.append("	F.SG_FILIAL, ")
			.append("	DS.NR_DOCTO_SERVICO, ")
			.append("	C.TP_IDENTIFICACAO, ")
			.append("	C.NR_IDENTIFICACAO, ")
			.append("	C.NM_PESSOA, ")
			.append("	NULL AS SG_FILIAL_1, ")
			.append("	NULL AS NR_MANIFESTO_ENTREGA, ")
			.append("	DS.ID_DOCTO_SERVICO, ")	
			.append("	M.ID_MANIFESTO, ")
			.append("	DS.DS_ENDERECO_ENTREGA_REAL AS DS_ENDERECO, ")
			.append("	DS.ID_FILIAL_ORIGEM AS ID_FILIAL, ")
			.append("	F.ID_FILIAL AS ID_FILIAL_1, ")
			.append("	NULL AS NR_ORDEM, ")
			.append("	CC.NR_CONTROLE_CARGA, ")
			.append("	FCC.SG_FILIAL AS SG_FILIAL_2, ")
			.append("	F.SG_FILIAL AS SG_FILIAL_MDV, ")
			.append("	MVN.NR_MANIFESTO_ORIGEM AS NR_MANIFESTO_MDV, ")
			.append("	'V' AS TP_MANIFESTO, ")
			.append("   null AS ID_AWB, ")
			.append("   null AS NM_CIA_AEREA ")

			.append("FROM CONTROLE_CARGA CC ")
			.append("	INNER JOIN MANIFESTO M ON M.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
			.append("	INNER JOIN MANIFESTO_VIAGEM_NACIONAL MVN ON MVN.ID_MANIFESTO_VIAGEM_NACIONAL = M.ID_MANIFESTO ")
			.append("	INNER JOIN FILIAL FCC ON FCC.ID_FILIAL = CC.ID_FILIAL_ORIGEM ")
			.append("	INNER JOIN FILIAL F ON MVN.ID_FILIAL = F.ID_FILIAL ")
			.append("	INNER JOIN MANIFESTO_NACIONAL_CTO M_CTO ON MVN.ID_MANIFESTO_VIAGEM_NACIONAL = M_CTO.ID_MANIFESTO_VIAGEM_NACIONAL ")
			.append("	INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = M_CTO.ID_CONHECIMENTO ")
			.append("	INNER JOIN PESSOA C ON C.ID_PESSOA = DS.ID_CLIENTE_DESTINATARIO ")

			.append(" WHERE M.TP_MANIFESTO_VIAGEM = :tipo1")
			.append("	AND M.TP_STATUS_MANIFESTO IN (:tipo2, :tipo3, :tipo4, :tipo5) ")
			.append("   AND NOT EXISTS ( ")
			.append("     SELECT 1")
			.append("       FROM EVENTO_DOCUMENTO_SERVICO EDS")
			.append("      WHERE EDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO")
			.append("        AND ID_OCORRENCIA_ENTREGA IS NOT NULL")
			.append(" ) ");

			sql.append("   AND M.ID_MANIFESTO = :idManifestoViagemNacional ");
			parameters.put("idManifestoViagemNacional", idManifestoViagemNacional);

			sql.append("   AND (M.ID_FILIAL_ORIGEM = :idFilial OR M.ID_FILIAL_DESTINO = :idFilial)");

			parameters.put("tipo1", "ED");
			parameters.put("tipo2", "EV");
			parameters.put("tipo3", "AD");
			parameters.put("tipo4", "ED");
			parameters.put("tipo5", "DC");

			if (idDoctoServico != null) {
				sql.append("   AND DS.ID_DOCTO_SERVICO = :idDoctoServico ");
			}

			if (parametersView.getLong("controleCarga.idControleCarga") != null) {
				sql.append("   AND CC.ID_CONTROLE_CARGA = :idControleCarga ");
			}

			if (StringUtils.isNotBlank(tpDocumentoServico)) {
				sql.append("   AND DS.TP_DOCUMENTO_SERVICO = :tpDoctoServico ");
			}

			if (idFilialOrigemDoctoServico != null) {
				sql.append("   AND DS.ID_FILIAL_ORIGEM = :idFilialOrigem ");
			}
		}

		if (idDoctoServico != null) {
			parameters.put("idDoctoServico", idDoctoServico);
		}

		if (parametersView.getLong("controleCarga.idControleCarga") != null) {
			parameters.put("idControleCarga",parametersView.getLong("controleCarga.idControleCarga"));
		}

		if (StringUtils.isNotBlank(tpDocumentoServico)) {
			parameters.put("tpDoctoServico",tpDocumentoServico);
		}

		if (idFilialOrigemDoctoServico != null) {
			parameters.put("idFilialOrigem",idFilialOrigemDoctoServico);
		}

		parameters.put("idFilial",parametersView.getLong("filial.idFilial"));
	}

	public List findControleCargaByDoctoServico(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(new StringBuffer(ManifestoEntregaDocumento.class.getName()).append(" MED ")
			.append("INNER JOIN FETCH MED.manifestoEntrega ME ")
			.append("INNER JOIN FETCH ME.manifesto M ")
			.append("INNER JOIN FETCH M.controleCarga CC ")
			.append("INNER JOIN FETCH CC.filialByIdFilialOrigem F ")
			.toString());

		hql.addCustomCriteria("MED.ocorrenciaEntrega.id is NULL");

		hql.addCriteria("MED.doctoServico.id","=",idDoctoServico);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findControleCargaByManifestoEntrega(Long idManifestoEntrega) {
		SqlTemplate hql = new SqlTemplate();

		hql.addFrom(new StringBuffer(ManifestoEntrega.class.getName()).append(" ME ")
			.append("INNER JOIN FETCH ME.manifesto M ")
			.append("INNER JOIN FETCH M.controleCarga CC ")
			.append("INNER JOIN FETCH CC.filialByIdFilialOrigem F ")
			.toString());

		hql.addCriteria("ME.id","=",idManifestoEntrega);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}	
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<Map<String,Object>> findPaginatedEventosColetas(PaginatedQuery paginatedQuery) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" evento.idEventoColeta AS idEventoColeta,");		
		hql.append(" manifesto.filial.sgFilial AS sgFilialManifesto,");
		hql.append(" manifesto.nrManifesto AS nrManifesto,");
		hql.append(" evento.ocorrenciaColeta.tpEventoColeta AS tpEventoColeta,");
		hql.append(" evento.ocorrenciaColeta.dsDescricaoResumida AS dsDescricaoResumida,");
		hql.append(" evento.dhEvento.value AS dhEvento,");
		hql.append(" pedido.filialByIdFilialResponsavel.sgFilial AS sgFilialColeta,");
		hql.append(" pedido.nrColeta AS nrColeta,");
		hql.append(" pedido.cliente.pessoa.nmPessoa AS nmPessoa,");
		hql.append(" pedido.enderecoPessoa.tipoLogradouro.dsTipoLogradouro AS dsTipoLogradouro,");
		hql.append(" pedido.enderecoPessoa.dsComplemento AS dsComplemento,");
		hql.append(" pedido.enderecoPessoa.municipio.nmMunicipio AS nmMunicipio,");
		hql.append(" pedido.enderecoPessoa.municipio.unidadeFederativa.sgUnidadeFederativa AS sgUnidadeFederativa,");
		hql.append(" pedido.enderecoPessoa.nrEndereco AS nrEndereco,");
		hql.append(" pedido.enderecoPessoa.dsEndereco AS dsEndereco)");
		hql.append(" FROM EventoColeta AS evento");
		hql.append("  JOIN evento.pedidoColeta pedido");
		hql.append("  JOIN pedido.manifestoColeta manifesto");
		hql.append("  JOIN manifesto.controleCarga controleCarga");
		hql.append(" WHERE controleCarga.idControleCarga = :idControleCarga");
		hql.append("  AND pedido.tpStatusColeta NOT IN('CA','FI','EX','NT')");
		hql.append(" ORDER BY manifesto.nrManifesto, pedido.nrColeta, evento.dhEvento.value");
		
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
	}
	
	public Integer getRowCountEventosColetas(TypedFlatMap criteria) {		
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT COUNT(evento.idEventoColeta)");		
		hql.append(" FROM EventoColeta AS evento");
		hql.append("  JOIN evento.pedidoColeta pedido");
		hql.append("  JOIN pedido.manifestoColeta manifesto");
		hql.append("  JOIN manifesto.controleCarga controleCarga");
		hql.append(" WHERE controleCarga.idControleCarga = :idControleCarga");
		hql.append("  AND pedido.tpStatusColeta NOT IN('CA','FI','EX','NT')");
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), criteria);
	}		
}