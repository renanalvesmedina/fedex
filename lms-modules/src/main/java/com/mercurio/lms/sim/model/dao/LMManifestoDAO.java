package com.mercurio.lms.sim.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.DoctoServico;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMManifestoDAO extends AdsmDao {
	
	public List findManifestoColetaByIdDoctoServico(Long idDoctoServico){
		
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(filOr.sgFilial as sgFilialOr, " +
				"filOr.sgFilial as sgFilialOrPC, " +
				"mc.nrManifesto as nrManifesto, " +
				"mc.dhEmissao as dhEmissao, " +
				"pc.nrColeta as nrColeta, " +
				"pc.dtPrevisaoColeta as dtPrevisaoColeta, " +
				"pc.tpPedidoColeta as tpPedidoColeta, " +
				"pc.tpModoPedidoColeta as tpModoPedidoColeta, " +
				"pc.dhColetaDisponivel as dhColetaDisponivel)");
		
		hql.addFrom(DoctoServico.class.getName()+" ds " +
				"join ds.filialByIdFilialOrigem filOr " +
				"left outer join ds.pedidoColeta pc " +
				"left outer join pc.manifestoColeta mc ");
		
		hql.addRequiredCriteria("ds.idDoctoServico","=", idDoctoServico);
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}
	
	
	public List<Map<String, Object>> findListManifestoColetaByIdDoctoServico(Long idDoctoServico) {
		
		ConfigureSqlQuery configQuery = configureSqlQueryManifestoColetaDoctoServico();
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("idDoctoServico", idDoctoServico);
		
		StringBuilder sql = new StringBuilder();
		sql
		.append("SELECT filialPc.SG_FILIAL     AS sgFilialOr,			 ")
		.append("  filialDs.SG_FILIAL          AS sgFilialOrPC,   		 ")
		.append("  mc.NR_MANIFESTO             AS nrManifesto,           ")
		.append("  mc.DH_EMISSAO               AS dhEmissao,             ")
		.append("  pc.NR_COLETA                AS nrColeta,              ")
		.append("  pc.DT_PREVISAO_COLETA       AS dtPrevisaoColeta,      ")
		.append("  pc.TP_PEDIDO_COLETA         AS tpPedidoColeta,        ")
		.append("  pc.TP_MODO_PEDIDO_COLETA    AS tpModoPedidoColeta,    ")
		.append("  pc.DH_COLETA_DISPONIVEL     AS dhColetaDisponivel,    ")
		.append("  pc.ID_PEDIDO_COLETA 		   AS idPedidoColeta	     ")
		.append("FROM docto_servico ds,                                  ")
		.append("  FILIAL filialDs,                                      ")
		.append("  PEDIDO_COLETA pc,                                     ")
		.append("  FILIAL filialPc,                                      ")
		.append("  MANIFESTO_COLETA mc                                   ")
		.append("WHERE ds.ID_FILIAL_ORIGEM 	= filialDs.ID_FILIAL         ")
		.append("AND filialPc.id_filial 	= pc.id_filial_solicitante   ")
		.append("AND ds.ID_PEDIDO_COLETA    = pc.ID_PEDIDO_COLETA        ")
		.append("AND pc.ID_MANIFESTO_COLETA = mc.ID_MANIFESTO_COLETA(+)  ")
		.append("AND ds.ID_DOCTO_SERVICO    = :idDoctoServico            ")
		.append("UNION                                                   ")
		.append("SELECT                                                  ")
		.append("  filialPc.SG_FILIAL          AS sgFilialOr,            ")
		.append("  filialMc.SG_FILIAL          AS sgFilialOrPC,          ")
		.append("  mc.NR_MANIFESTO             AS nrManifesto,           ")
		.append("  mc.DH_EMISSAO               AS dhEmissao,             ")
		.append("  pc.NR_COLETA                AS nrColeta,              ")
		.append("  pc.DT_PREVISAO_COLETA       AS dtPrevisaoColeta,      ")
		.append("  pc.TP_PEDIDO_COLETA         AS tpPedidoColeta,        ")
		.append("  pc.TP_MODO_PEDIDO_COLETA    AS tpModoPedidoColeta,    ")
		.append("  pc.DH_COLETA_DISPONIVEL     AS dhColetaDisponivel,    ")
		.append("  pc.ID_PEDIDO_COLETA 		   AS idPedidoColeta	     ")
		.append("FROM                                                    ")
		.append("docto_servico ds,                                       ")
		.append("detalhe_coleta dc,                                      ")
		.append("pedido_coleta pc,                                       ")
		.append("filial filialPc,                                        ")
		.append("manifesto_coleta mc,                                    ")
		.append("filial filialMc                                         ")
		.append("where ds.id_docto_servico 	= :idDoctoServico            ")
		.append("and ds.id_docto_servico 	= dc.id_docto_servico        ")
		.append("and pc.id_pedido_coleta	= dc.id_pedido_coleta        ")
		.append("AND filialPc.id_filial 	= pc.id_filial_solicitante   ")
		.append("and pc.id_manifesto_coleta = mc.id_manifesto_coleta     ")
		.append("and mc.id_filial_origem 	= filialMc.ID_FILIAL	 	 ")
		.append("order by idPedidoColeta	 	 ");
		
		
		return getAdsmHibernateTemplate().findBySqlToMappedResult(sql.toString(), parametersValues, configQuery);
		
	}


	private ConfigureSqlQuery configureSqlQueryManifestoColetaDoctoServico() {
		ConfigureSqlQuery configQuery = new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("sgFilialOr", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOrPC",  Hibernate.STRING);
				sqlQuery.addScalar("nrManifesto",  Hibernate.INTEGER);
				sqlQuery.addScalar("dhEmissao",  Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("nrColeta",  Hibernate.LONG);
				sqlQuery.addScalar("dtPrevisaoColeta", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("dhColetaDisponivel",  Hibernate.custom(JodaTimeDateTimeUserType.class)); 
				sqlQuery.addScalar("idPedidoColeta",  Hibernate.LONG); 
				
				Properties propertiesTpPedidoColeta = new Properties();
				propertiesTpPedidoColeta.put("domainName","DM_TIPO_PEDIDO_COLETA");
				sqlQuery.addScalar("tpPedidoColeta", Hibernate.custom(DomainCompositeUserType.class,propertiesTpPedidoColeta)); 
				
				Properties propertiesTpModoPedidoColeta = new Properties();
				propertiesTpModoPedidoColeta.put("domainName","DM_MODO_PEDIDO_COLETA");
				sqlQuery.addScalar("tpModoPedidoColeta",Hibernate.custom(DomainCompositeUserType.class,propertiesTpModoPedidoColeta));
				
			}
		};
		return configQuery;
	}
	
	
	public ResultSetPage findPaginatedManifestoViagem(Long idDoctoServico){
		
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("SGFILOR", Hibernate.STRING);
				sqlQuery.addScalar("NUMERO", Hibernate.LONG);
				sqlQuery.addScalar("FILDEST", Hibernate.STRING);
				sqlQuery.addScalar("DHEMISSAOMAN", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DHSAIDAPROG", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DHSAIDAEFET", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DHPREVISAOCHEG", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DHCHEGADA", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DHINICIODESC", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("DHFIMDESC", Hibernate.custom(JodaTimeDateTimeUserType.class));
				
			}
		};
		
		StringBuffer sb= new StringBuffer()
		.append("SELECT * FROM(")
		.append("SELECT " +
				"FILOR.SG_FILIAL AS SGFILOR, "+
				"MVN.NR_MANIFESTO_ORIGEM AS NUMERO, "+
				"FILDEST.SG_FILIAL AS FILDEST, " +
				"MAN.DH_EMISSAO_MANIFESTO AS DHEMISSAOMAN, "+
				"(SELECT CT.DH_PREVISAO_SAIDA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHSAIDAPROG, "+
				"(SELECT CT.DH_SAIDA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHSAIDAEFET, "+
				"(SELECT CT.DH_PREVISAO_CHEGADA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHPREVISAOCHEG, "+
				"(SELECT CT.DH_CHEGADA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHCHEGADA , "+
				"(SELECT CD.DH_INICIO_OPERACAO FROM CARREGAMENTO_DESCARGA CD WHERE CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CD.TP_OPERACAO = 'D' AND CD.DH_CANCELAMENTO_OPERACAO IS NULL AND CD.ID_FILIAL = MAN.ID_FILIAL_DESTINO) AS DHINICIODESC, "+
				"(SELECT CD.DH_FIM_OPERACAO FROM CARREGAMENTO_DESCARGA CD WHERE CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CD.TP_OPERACAO = 'D' AND CD.DH_CANCELAMENTO_OPERACAO IS NULL AND CD.ID_FILIAL = MAN.ID_FILIAL_DESTINO) AS DHFIMDESC ")
			.append(" FROM "+
				"DOCTO_SERVICO DS, "+
				"CONHECIMENTO CO, "+
				"MANIFESTO_NACIONAL_CTO MN, "+
				"MANIFESTO_VIAGEM_NACIONAL MVN, "+
				"MANIFESTO MAN , "+
				"FILIAL FILOR, "+
				"FILIAL FILDEST , "+
				"CONTROLE_CARGA CC")
			.append(" WHERE "+
				"DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO AND "+
				"CO.ID_CONHECIMENTO = MN.ID_CONHECIMENTO AND "+
				"MN.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL AND "+
				"MVN.ID_MANIFESTO_VIAGEM_NACIONAL = MAN.ID_MANIFESTO AND "+
				"MAN.ID_FILIAL_ORIGEM = FILOR.ID_FILIAL(+) AND "+
				"MAN.ID_FILIAL_DESTINO = FILDEST.ID_FILIAL(+) AND "+
				"MAN.ID_CONTROLE_CARGA =  CC.ID_CONTROLE_CARGA(+) AND "+
				"DS.ID_DOCTO_SERVICO=? ")

			.append(" UNION ")
			
			.append(" SELECT "+
				"FILOR.SG_FILIAL AS SGFILOR, " +
				"MI.NR_MANIFESTO_INT AS NUMERO, "+
				"FILDEST.SG_FILIAL AS FILDEST, "+
				"MAN.DH_EMISSAO_MANIFESTO AS DHEMISSAOMAN, "+
				"(SELECT CT.DH_PREVISAO_SAIDA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHSAIDAPROG, "+
				"(SELECT CT.DH_SAIDA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHSAIDAEFET, "+
				"(SELECT CT.DH_PREVISAO_CHEGADA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHPREVISAOCHEG, "+
				"(SELECT CT.DH_CHEGADA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = MAN.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = MAN.ID_FILIAL_DESTINO) AS DHCHEGADA , "+
				"(SELECT CD.DH_INICIO_OPERACAO FROM CARREGAMENTO_DESCARGA CD WHERE CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CD.TP_OPERACAO = 'D' AND CD.DH_CANCELAMENTO_OPERACAO IS NULL AND CD.ID_FILIAL = MAN.ID_FILIAL_DESTINO) AS DHINICIODESC, "+
				"(SELECT CD.DH_FIM_OPERACAO FROM CARREGAMENTO_DESCARGA CD WHERE CD.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CD.TP_OPERACAO = 'D' AND CD.DH_CANCELAMENTO_OPERACAO IS NULL AND CD.ID_FILIAL = MAN.ID_FILIAL_DESTINO) AS DHFIMDESC ")
			.append(" FROM "+
				"DOCTO_SERVICO DS, "+
				"CTO_INTERNACIONAL COI, "+
				"MANIFESTO_INTERNAC_CTO MIC, "+
				"MANIFESTO_INTERNACIONAL MI, "+
				"MANIFESTO MAN , "+
				"FILIAL FILOR, "+
				"FILIAL FILDEST , "+
				"CONTROLE_CARGA CC ")
			.append(" WHERE "+
				"DS.ID_DOCTO_SERVICO = COI.ID_CTO_INTERNACIONAL AND "+
				"COI.ID_CTO_INTERNACIONAL = MIC.ID_CTO_INTERNACIONAL AND "+
				"MIC.ID_MANIFESTO_INTERNACIONAL = MI.ID_MANIFESTO_INTERNACIONAL AND "+
				"MI.ID_MANIFESTO_INTERNACIONAL = MAN.ID_MANIFESTO AND "+
				"MAN.ID_FILIAL_ORIGEM = FILOR.ID_FILIAL(+) AND "+
				"MAN.ID_FILIAL_DESTINO = FILDEST.ID_FILIAL(+) AND "+
				"MAN.ID_CONTROLE_CARGA =  CC.ID_CONTROLE_CARGA(+) AND "+
				"DS.ID_DOCTO_SERVICO=? ) ")
			.append("ORDER BY DHEMISSAOMAN");

		return getAdsmHibernateTemplate().findPaginatedBySql(sb.toString(), Integer.valueOf(1), Integer.valueOf(1000),new Object[]{idDoctoServico,idDoctoServico},configSql);
	}
	
	public ResultSetPage findPaginatedManifestoEntrega(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(fil.sgFilial as sgFilial, me.nrManifestoEntrega as nrManifestoEntrega," +
				"man.dhEmissaoManifesto as dhEmissaoManifesto, "+PropertyVarcharI18nProjection.createProjection("oe.dsOcorrenciaEntrega")+" as dsOcorrenciaEntrega, " +
				"meds.nmRecebedor as nmRecebedor, " +
				"man.tpManifestoEntrega as tpManifestoEntrega, " +
				"me.idManifestoEntrega as idManifestoEntrega)");
		hql.addFrom(DoctoServico.class.getName()+ " ds " +
				"left outer join ds.manifestoEntregaDocumentos meds " +
				"left outer join meds.ocorrenciaEntrega oe " +
				"left outer join meds.manifestoEntrega me " +
				"left outer join me.filial fil " +
				"left outer join me.manifesto man ");
		
		hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
		
		hql.addOrderBy("man.dhEmissaoManifesto.value");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),Integer.valueOf(1), Integer.valueOf(1000), hql.getCriteria());
	}
	
	public ResultSetPage findPaginatedManifestoEntregaNotasFiscaisByIdDoctoServico(Long idDoctoServico) {
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("sg_filial", Hibernate.STRING);
				sqlQuery.addScalar("nr_nota_fiscal", Hibernate.INTEGER);
				sqlQuery.addScalar("ds_serie", Hibernate.STRING);
				sqlQuery.addScalar("dt_emissao", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				sqlQuery.addScalar("ds_ocorrencia_entrega", Hibernate.STRING);
				sqlQuery.addScalar("nr_manifesto", Hibernate.INTEGER);
				sqlQuery.addScalar("qt_volumes_entregues", Hibernate.INTEGER);
			}
		};

		StringBuilder query = new StringBuilder()
			.append("          SELECT F.sg_filial, NFC.nr_nota_fiscal, NFC.ds_serie, NFC.dt_emissao, ")
			.append(				  PropertyVarcharI18nProjection.createProjection("OE.ds_ocorrencia_entrega_i") + " ds_ocorrencia_entrega,")
			.append("            CASE WHEN MED.id_manifesto_entrega IS NULL")
			.append("                 THEN MVN.nr_manifesto_origem ")
			.append("                 ELSE ME.nr_manifesto_entrega ")
			.append("             END as nr_manifesto, ENF.qt_volumes_entregues ")
			.append("            FROM nota_fiscal_conhecimento NFC, ocorrencia_entrega OE, filial F, entrega_nota_fiscal ENF ")
			.append(" LEFT OUTER JOIN manifesto_nacional_cto MNC ")
			.append("              ON MNC.id_manifesto_nacional_cto = ENF.id_manifesto_nacional_cto ")
			.append(" LEFT OUTER JOIN manifesto_viagem_nacional MVN ")
			.append("              ON MVN.id_manifesto_viagem_nacional = MNC.id_manifesto_viagem_nacional ")
			.append(" LEFT OUTER JOIN manifesto_entrega_documento MED ")
			.append("              ON MED.id_manifesto_entrega_documento = ENF.id_manifesto_entrega_documento ")
			.append(" LEFT OUTER JOIN manifesto_entrega ME ")
			.append("              ON ME.id_manifesto_entrega = MED.id_manifesto_entrega ")
			.append("           WHERE NFC.id_nota_fiscal_conhecimento = ENF.id_nota_fiscal_conhecimento ")
			.append("             AND ENF.id_ocorrencia_entrega = OE.id_ocorrencia_entrega ")
			.append("             AND ( F.id_filial = ME.id_filial ")
			.append("                  OR F.id_filial = MVN.id_filial ")
			.append("             ) ")
			.append("             AND NFC.id_conhecimento = ? ")
			.append("        ORDER BY nr_manifesto, NFC.nr_nota_fiscal, NFC.ds_serie, NFC.dt_emissao ");
		
		return getAdsmHibernateTemplate().findPaginatedBySql(
		  query.toString(), Integer.valueOf(1), Integer.valueOf(1000), new Object[]{idDoctoServico}, configSql
		);
	}
}