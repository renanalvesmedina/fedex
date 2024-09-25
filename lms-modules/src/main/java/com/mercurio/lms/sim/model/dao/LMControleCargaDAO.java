package com.mercurio.lms.sim.model.dao;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMControleCargaDAO extends AdsmDao {
	public static final int SGFILIALOR = 0;
	public static final int NRCONTROLE = 1;
	public static final int IDCONTROLECARGA = 2;
	public static final int SGFILIALDEST = 3;
	public static final int TPCONTROLECARGA = 4;
	public static final int DH_GERACAO = 5;
	public static final int ROTA = 6;
	public static final int SAIDA = 7;
	public static final int CHEGADA = 8;
	
	/*
	 * Autorizado pelo Felipe Cuozzo, pois nao existe um findSql que retorne uma lista, então foi setado o pageSize e pageNumber na mao
	 */
	public ResultSetPage findPaginatedControleCarga(Long idDoctoServico){
		
		ConfigureSqlQuery configSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("SGFILIALOR", Hibernate.STRING);
				sqlQuery.addScalar("NRCONTROLE", Hibernate.LONG);
				sqlQuery.addScalar("IDCONTROLECARGA", Hibernate.LONG);
				sqlQuery.addScalar("SGFILIALDEST", Hibernate.STRING);
				sqlQuery.addScalar("TPCONTROLECARGA", Hibernate.STRING);
				sqlQuery.addScalar("DH_GERACAO", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("ROTA", Hibernate.STRING);
				sqlQuery.addScalar("SAIDA", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("CHEGADA", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("TPSTATUSCONTROLECARGA", Hibernate.STRING);
				
			}
		};
		
		StringBuffer sb = new StringBuffer()
		.append("SELECT * FROM (")
		.append("select  FILOR.SG_FILIAL AS SGFILIALOR, "+
		       "CC.NR_CONTROLE_CARGA AS NRCONTROLE, " +
		       "CC.ID_CONTROLE_CARGA AS IDCONTROLECARGA, " +
		       "FILDEST.SG_FILIAL AS SGFILIALDEST, "+
		       "CC.TP_CONTROLE_CARGA AS TPCONTROLECARGA, "+
		       "CC.DH_GERACAO AS DH_GERACAO, "+
		       "CC.TP_STATUS_CONTROLE_CARGA AS TPSTATUSCONTROLECARGA, "+
		       
		       "(CASE WHEN CC.TP_CONTROLE_CARGA = 'C' THEN (Nvl2(RCE.NR_ROTA, RCE.NR_ROTA||' - '||RCE.DS_ROTA,'')) ELSE (Nvl2(RIV.NR_ROTA, RIV.NR_ROTA||' - '||ROT.DS_ROTA,'')) END) AS ROTA, "+
		       
		       "(CASE WHEN CC.TP_CONTROLE_CARGA = 'C' THEN CC.DH_CHEGADA_COLETA_ENTREGA ELSE  "+
		       	"(SELECT CT.DH_CHEGADA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = CC.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = CC.ID_FILIAL_DESTINO) END) AS CHEGADA, "+
		      
		       	"(CASE WHEN CC.TP_CONTROLE_CARGA = 'C' THEN CC.DH_SAIDA_COLETA_ENTREGA ELSE  "+
		       	"(SELECT CT.DH_SAIDA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = CC.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = CC.ID_FILIAL_DESTINO) END) AS SAIDA ")
		       	
		.append(" FROM DOCTO_SERVICO DS, "+
					"FILIAL FILOR, "+
					"FILIAL FILDEST, "+
					"CONTROLE_CARGA CC , "+
					"PEDIDO_COLETA PC, "+
					"ROTA_IDA_VOLTA RIV, "+
					"ROTA ROT, "+
					"ROTA_COLETA_ENTREGA RCE, "+
					"MANIFESTO_COLETA MC ")
		  .append(" WHERE "+
			"DS.ID_DOCTO_SERVICO = ?  AND "+	  
			"DS.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA(+) AND "+
			"PC.ID_MANIFESTO_COLETA = MC.ID_MANIFESTO_COLETA(+) AND "+
			"MC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA(+) AND "+
			"CC.ID_FILIAL_ORIGEM = FILOR.ID_FILIAL(+) AND "+
			"CC.ID_FILIAL_DESTINO = FILDEST.ID_FILIAL(+) AND "+
			"CC.ID_ROTA_COLETA_ENTREGA = RCE.ID_ROTA_COLETA_ENTREGA(+) AND "+
			"CC.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA(+)  AND "+
			"RIV.ID_ROTA = ROT.ID_ROTA(+) AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA' ")
		
		.append(" union ")
		
		.append(" select  FILOR.SG_FILIAL AS SGFILIALOR, "+
				"CC.NR_CONTROLE_CARGA AS NRCONTROLE, "+
				"CC.ID_CONTROLE_CARGA AS IDCONTROLECARGA, " +
				"FILDEST.SG_FILIAL AS SGFILIALDEST, "+
				"CC.TP_CONTROLE_CARGA AS TPCONTROLECARGA, "+
				"CC.DH_GERACAO AS DH_GERACAO, "+
				"CC.TP_STATUS_CONTROLE_CARGA AS TPSTATUSCONTROLECARGA, "+
				"(CASE WHEN CC.TP_CONTROLE_CARGA = 'C' THEN (Nvl2(RCE.NR_ROTA, RCE.NR_ROTA||' - '||RCE.DS_ROTA,'')) ELSE (Nvl2(RIV.NR_ROTA,RIV.NR_ROTA||' - '||ROT.DS_ROTA,'')) END) AS ROTA, "+
				
				"(CASE WHEN CC.TP_CONTROLE_CARGA = 'C' THEN CC.DH_CHEGADA_COLETA_ENTREGA ELSE  "+
			       	"(SELECT CT.DH_CHEGADA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = CC.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = CC.ID_FILIAL_DESTINO) END) AS CHEGADA, "+
			      
				"(CASE WHEN CC.TP_CONTROLE_CARGA = 'C' THEN CC.DH_SAIDA_COLETA_ENTREGA ELSE "+
					"(SELECT CT.DH_SAIDA FROM CONTROLE_TRECHO CT WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND CT.ID_FILIAL_ORIGEM = CC.ID_FILIAL_ORIGEM AND CT.ID_FILIAL_DESTINO = CC.ID_FILIAL_DESTINO) END) AS SAIDA ")
		.append(" FROM DOCTO_SERVICO DS,"+
			    "FILIAL FILOR,"+
			    "FILIAL FILDEST,"+
			    "CONTROLE_CARGA CC ,"+
			    "PRE_MANIFESTO_DOCUMENTO PMD,"+
			    "MANIFESTO MAN,"+
			    "ROTA_IDA_VOLTA RIV,"+
			    "ROTA ROT,"+
			    "ROTA_COLETA_ENTREGA RCE")
		.append(" WHERE "+
				" DS.ID_DOCTO_SERVICO = ?  AND "+
			    " DS.ID_DOCTO_SERVICO = PMD.ID_DOCTO_SERVICO(+) AND "+
			    " PMD.ID_MANIFESTO = MAN.ID_MANIFESTO(+) AND "+
			    " MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA(+) AND "+
			    " CC.ID_FILIAL_ORIGEM = FILOR.ID_FILIAL(+) AND "+
			    " CC.ID_FILIAL_DESTINO = FILDEST.ID_FILIAL(+) AND "+
			    " CC.ID_ROTA_COLETA_ENTREGA = RCE.ID_ROTA_COLETA_ENTREGA(+) AND "+
			    " CC.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA(+)  AND "+
			    " RIV.ID_ROTA = ROT.ID_ROTA(+) AND "+
			    " CC.TP_STATUS_CONTROLE_CARGA <> 'CA' ")
			    .append(" UNION	")
	.append(" SELECT FILOR.SG_FILIAL       AS SGFILIALOR,	"+
			" CC.NR_CONTROLE_CARGA        AS NRCONTROLE,		"+
			" CC.ID_CONTROLE_CARGA        AS IDCONTROLECARGA,		"+
			" FILDEST.SG_FILIAL           AS SGFILIALDEST,		"+
			" CC.TP_CONTROLE_CARGA        AS TPCONTROLECARGA,		"+
			" CC.DH_GERACAO               AS DH_GERACAO,		"+
			" CC.TP_STATUS_CONTROLE_CARGA AS TPSTATUSCONTROLECARGA,		"+
			" (		"+
			" CASE		"+
			" WHEN CC.TP_CONTROLE_CARGA = 'C'		"+
			" THEN (Nvl2(RCE.NR_ROTA, RCE.NR_ROTA		"+
			" ||' - '		"+
			" ||RCE.DS_ROTA,''))		"+
			" ELSE (Nvl2(RIV.NR_ROTA, RIV.NR_ROTA		"+
			" ||' - '	"+
			" ||ROT.DS_ROTA,''))	"+
			" END) AS ROTA,		"+
			" (		"+
			" CASE		"+
			" WHEN CC.TP_CONTROLE_CARGA = 'C'		"+
			" THEN CC.DH_CHEGADA_COLETA_ENTREGA		"+
			" ELSE		"+
			" (SELECT CT.DH_CHEGADA		"+
			" FROM CONTROLE_TRECHO CT		"+
			" WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA		"+
			" AND CT.ID_FILIAL_ORIGEM    = CC.ID_FILIAL_ORIGEM		"+
			" AND CT.ID_FILIAL_DESTINO   = CC.ID_FILIAL_DESTINO		"+
			" )		"+
			" END) AS CHEGADA,		"+
			" (		"+
			" CASE		"+
			" WHEN CC.TP_CONTROLE_CARGA = 'C'		"+
			" THEN CC.DH_SAIDA_COLETA_ENTREGA		"+
			" ELSE		"+
			" (SELECT CT.DH_SAIDA		"+
			" FROM CONTROLE_TRECHO CT	"+
			" WHERE CT.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA		"+
			" AND CT.ID_FILIAL_ORIGEM    = CC.ID_FILIAL_ORIGEM		"+
			" AND CT.ID_FILIAL_DESTINO   = CC.ID_FILIAL_DESTINO		"+
			" )		"+
			" END) AS SAIDA		"+
			" FROM DOCTO_SERVICO DS,		"+
			" FILIAL FILOR,		"+
			" FILIAL FILDEST,	"+
			" CONTROLE_CARGA CC ,	"+
			" PEDIDO_COLETA PC,		"+
			" ROTA_IDA_VOLTA RIV,	"+
			" ROTA ROT,		"+
			" ROTA_COLETA_ENTREGA RCE,		"+
			" MANIFESTO_COLETA MC,		"+
			" DETALHE_COLETA DC		"+
			" WHERE DS.ID_DOCTO_SERVICO        = ?	"+
			" AND DS.ID_DOCTO_SERVICO          = DC.ID_DOCTO_SERVICO(+)		"+
			" AND DC.ID_PEDIDO_COLETA          = PC.ID_PEDIDO_COLETA(+)		"+
			" AND PC.ID_MANIFESTO_COLETA       = MC.ID_MANIFESTO_COLETA(+)	"+
			" AND MC.ID_CONTROLE_CARGA         = CC.ID_CONTROLE_CARGA(+)	"+
			" AND CC.ID_FILIAL_ORIGEM          = FILOR.ID_FILIAL(+)			"+
			" AND CC.ID_FILIAL_DESTINO         = FILDEST.ID_FILIAL(+)		"+
			" AND CC.ID_ROTA_COLETA_ENTREGA    = RCE.ID_ROTA_COLETA_ENTREGA(+)	"+
			" AND CC.ID_ROTA_IDA_VOLTA         = RIV.ID_ROTA_IDA_VOLTA(+)		"+
			" AND RIV.ID_ROTA                  = ROT.ID_ROTA(+)					"+
			" AND CC.TP_STATUS_CONTROLE_CARGA <> 'CA'						"+
			" ) ORDER BY DH_GERACAO ");						
			

		
		return getAdsmHibernateTemplate().findPaginatedBySql(sb.toString(),Integer.valueOf(1),Integer.valueOf(1000),new Object[]{idDoctoServico,idDoctoServico,idDoctoServico},configSql);
		
	}	
}
