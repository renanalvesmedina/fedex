package com.mercurio.lms.carregamento.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.carregamento.documentosFimCarregamentoService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/documentosFimCarregamento.jasper" 
 *                  
 */
public class DocumentosFimCarregamentoService extends ReportServiceSupport {
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		SqlTemplate sql = sqlTpdocumentoCarregamento(parameters.get("tpDocumentoCarregamento").toString(), Long.parseLong(parameters.get("idControleCarga").toString()),
				Long.parseLong(parameters.get("idCarregamentoDescarga").toString()));
		
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("tpDocumentoCarregamento", parameters.get("tpDocumentoCarregamento").toString());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        
        return jr;
	}
	
	
	/**
	 * Valida o tipo de busca a ser realizada dado pelo tipo de chamada.
	 */
	private SqlTemplate sqlTpdocumentoCarregamento(String tpDocCarreg, Long idControleCarga, Long idCarregamentoDescarga) {
		
		if(tpDocCarreg.equals("docCarregadoConferido")){
			return sqlDocCarregadoConferido(idControleCarga, idCarregamentoDescarga);
		}else if(tpDocCarreg.equals("docCarregadoNaoConferido")){
			return sqlDocCarregadoNaoConferido(idControleCarga, idCarregamentoDescarga);
		}else if(tpDocCarreg.equals("docNaoCarregadoConferido")){
			return sqlDocNaoCarregadoConferido(idControleCarga, idCarregamentoDescarga);
		}if(tpDocCarreg.equals("carregadosSemPreManifestoDocumento")){
			return sqlCarregadosSemPreManifestoDocumento(idControleCarga);
		}else if(tpDocCarreg.equals("carregadosIncompletos")){
			return sqlCarregadosIncompletos(idControleCarga);
		}else{
			return null;
		}

	}
	
	/**
	 * Monta o sql que deverá retornar todos os documentos de servico que estão carregados e foram conferidos
	 * ET 05.01.02.04 - CQ25050 - Conhecimentos conferidos e que estão alocados a algum pre-manifesto do controle de carga 
	 * @param idControleCarga 
	 * @return
	 */
	public SqlTemplate sqlDocCarregadoConferido(Long idControleCarga, Long idCarregamentoDescarga) {
		SqlTemplate sql = sqlProjectionDocumento(idControleCarga);
		SqlTemplate sqlSubSelect = sqlProjectionConhecimento(idControleCarga, idCarregamentoDescarga);
		
		sql.addCustomCriteria("CON.ID_CONHECIMENTO IN (" + sqlSubSelect.toString() + ") ");
		
		return sql;
	}


	/**
	 * Monta o sql que deverá retornar todos os documentos de servico carregados e não conferidos
	 * ET 05.01.02.04 - CQ25050 - Conhecimentos alocados a algum pre-manifesto do controle de carga e que nao foram conferidos.
	 * @param idControleCarga 
	 * @return
	 */
	public SqlTemplate sqlDocCarregadoNaoConferido(Long idControleCarga, Long idCarregamentoDescarga) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("CON.NR_CONHECIMENTO", "NR_CONHECIMENTO");
		sql.addProjection("MAN.NR_PRE_MANIFESTO", "NR_PRE_MANIFESTO");
		sql.addProjection("FOM.SG_FILIAL", "FILIAL_MF");
		sql.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
		sql.addProjection("FO.SG_FILIAL", "FILIAL_CC");
		sql.addProjection("DO.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
		sql.addProjection("DO.QT_VOLUMES","QT_VOLUMES");
		sql.addProjection("FDS.SG_FILIAL","FILIAL_DS");
		
		sql.addProjection("DO.QT_VOLUMES","NR_NOTA_FISCAL");
		sql.addProjection("DO.QT_VOLUMES","NR_SEQUENCIA");

		sql.addProjection("PS.NM_PESSOA","REMETENTE"); 
		sql.addProjection("MT.NR_FROTA"); 
		sql.addProjection("MT.NR_IDENTIFICADOR");    

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I"),"LOCALIZACAO");
		sql.addProjection("CON.NR_FORMULARIO","NR_FORMULARIO");
		
		sql.addFrom("CONHECIMENTO","CON");
		sql.addFrom("CONTROLE_CARGA","CC");
		sql.addFrom("MANIFESTO","MAN");
		sql.addFrom("PRE_MANIFESTO_DOCUMENTO","PMD");
		sql.addFrom("FILIAL","FL");
		sql.addFrom("FILIAL","FO");
		sql.addFrom("FILIAL","FOM");
		sql.addFrom("FILIAL","FDS");
		sql.addFrom("DOCTO_SERVICO","DO");
		sql.addFrom("LOCALIZACAO_MERCADORIA","LM");
		
		sql.addFrom("PEDIDO_COLETA","PC");
		sql.addFrom("PESSOA","PS");
		sql.addFrom("MANIFESTO_COLETA" ,"MCC");
		sql.addFrom("CONTROLE_CARGA","CCC");
		sql.addFrom("MEIO_TRANSPORTE","MT");
		
		sql.addCustomCriteria("DO.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO");
		sql.addCustomCriteria("DO.ID_DOCTO_SERVICO = PMD.ID_DOCTO_SERVICO(+)");
		sql.addCustomCriteria("PMD.ID_MANIFESTO = MAN.ID_MANIFESTO(+)");
		sql.addCustomCriteria("MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA(+) ");
		sql.addCustomCriteria("FL.ID_FILIAL = CC.ID_FILIAL_ATUALIZA_STATUS");
		sql.addCustomCriteria("CC.ID_FILIAL_ORIGEM = FO.ID_FILIAL");
		sql.addCustomCriteria("MAN.ID_FILIAL_ORIGEM = FOM.ID_FILIAL");
		sql.addCustomCriteria("CON.ID_FILIAL_ORIGEM = FDS.ID_FILIAL");
		sql.addCustomCriteria("DO.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA");
		sql.addCustomCriteria("CC.ID_CONTROLE_CARGA = "+idControleCarga);
		sql.addCustomCriteria("MAN.ID_FILIAL_ORIGEM ="+SessionUtils.getFilialSessao().getIdFilial());
		sql.addCustomCriteria("DO.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA (+)");
		sql.addCustomCriteria("PC.ID_CLIENTE = PS.ID_PESSOA (+)");
		sql.addCustomCriteria("PC.ID_MANIFESTO_COLETA          = MCC.ID_MANIFESTO_COLETA (+)");
		sql.addCustomCriteria("MCC.ID_CONTROLE_CARGA           = CCC.ID_CONTROLE_CARGA (+)");
		sql.addCustomCriteria("CCC.ID_TRANSPORTADO             = MT.ID_MEIO_TRANSPORTE (+)");
		
		
		String subSelect = "SELECT CONH.ID_CONHECIMENTO "+   
		           			"FROM CONHECIMENTO CONH , "+
		                		"CONTROLE_CARGA_CONH_SCAN CCCS , "+ 
		                		"CONTROLE_CARGA CCA  "+
		          			"WHERE CCCS.ID_CONHECIMENTO (+)= CONH.ID_CONHECIMENTO  "+
		            			"AND CCCS.ID_CONTROLE_CARGA  = CCA.ID_CONTROLE_CARGA (+) "+
		            			"AND CCA.ID_CONTROLE_CARGA   = " +idControleCarga;
		
		sql.addCustomCriteria("CON.ID_CONHECIMENTO NOT IN (" + subSelect + ") ");
		
		return sql;
	}
	

	/**
	 * Monta o sql que deverá retornar todos os documentos de serviço não carregados e conferidos
	 * ET 05.01.02.04 - CQ25050 - Conhecimentos conferidos e que nao estão alocados a algum pre-manifesto do controle de carga.
	 * @param idControleCarga 
	 * @return
	 */
	public SqlTemplate sqlDocNaoCarregadoConferido(Long idControleCarga, Long idCarregamentoDescarga) {
		
		SqlTemplate sqlSubSelect = createSqlTemplate();
		
		sqlSubSelect.addProjection("CON.ID_CONHECIMENTO");
		
		sqlSubSelect.addFrom("CONHECIMENTO CON");
		sqlSubSelect.addFrom("PRE_MANIFESTO_DOCUMENTO PMD");
		sqlSubSelect.addFrom("MANIFESTO MAN");
		sqlSubSelect.addFrom("CONTROLE_CARGA CC");
	
		sqlSubSelect.addCustomCriteria("CON.ID_CONHECIMENTO = PMD.ID_DOCTO_SERVICO(+)");
		sqlSubSelect.addCustomCriteria("PMD.ID_MANIFESTO = MAN.ID_MANIFESTO (+)");
		sqlSubSelect.addCustomCriteria("MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA (+)");
	
		sqlSubSelect.addCustomCriteria("CC.ID_CONTROLE_CARGA ="+idControleCarga);
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("FL.SG_FILIAL","FILIAL_CC");
		sql.addProjection("CCA.NR_CONTROLE_CARGA","NR_CONTROLE_CARGA");
		sql.addProjection("FL_MAN.SG_FILIAL","FILIAL_MF");
		sql.addProjection("MAN.NR_PRE_MANIFESTO","NR_PRE_MANIFESTO");
		sql.addProjection("FL_DS.SG_FILIAL","FILIAL_DS");
		sql.addProjection("CONH.NR_CONHECIMENTO", "NR_CONHECIMENTO");
		sql.addProjection("CONH.NR_FORMULARIO","NR_FORMULARIO");
		sql.addProjection("PS.NM_PESSOA","REMETENTE"); 
		sql.addProjection("MT.NR_FROTA");
		sql.addProjection("MT.NR_IDENTIFICADOR");
		
		
		sql.addProjection("CONH.NR_FORMULARIO","NR_SEQUENCIA");
		sql.addProjection("CONH.NR_FORMULARIO","NR_NOTA_FISCAL");
		sql.addProjection("CONH.NR_FORMULARIO","LOCALIZACAO");
		sql.addProjection("CONH.NR_FORMULARIO","QT_VOLUMES");
		
		sql.addFrom("CONHECIMENTO CONH");
		sql.addFrom("CONTROLE_CARGA_CONH_SCAN CCCS");
		sql.addFrom("CONTROLE_CARGA CCA");
		sql.addFrom("FILIAL FL");
		sql.addFrom("FILIAL FL_MAN");
		sql.addFrom("FILIAL FL_DS");
		sql.addFrom("MANIFESTO MAN");
		sql.addFrom("DOCTO_SERVICO DO");
		sql.addFrom("PEDIDO_COLETA  PC");
		sql.addFrom("PESSOA  PS");
		sql.addFrom("MANIFESTO_COLETA         MCC");
		sql.addFrom("CONTROLE_CARGA           CCC");
		sql.addFrom("MEIO_TRANSPORTE       MT");
	
		sql.addCustomCriteria("CCCS.ID_CONHECIMENTO(+) = CONH.ID_CONHECIMENTO");
		sql.addCustomCriteria("CONH.ID_FILIAL_ORIGEM   = FL_DS.ID_FILIAL");
		sql.addCustomCriteria("CCCS.ID_CONTROLE_CARGA  = CCA.ID_CONTROLE_CARGA (+)");
		sql.addCustomCriteria("DO.ID_DOCTO_SERVICO     = CONH.ID_CONHECIMENTO");
		sql.addCustomCriteria("FL.ID_FILIAL        = CCA.ID_FILIAL_ATUALIZA_STATUS");
		sql.addCustomCriteria("MAN.ID_CONTROLE_CARGA   = CCA.ID_CONTROLE_CARGA");
		sql.addCustomCriteria("MAN.ID_FILIAL_ORIGEM   = FL_MAN.ID_FILIAL");
		sql.addCustomCriteria("CCA.ID_CONTROLE_CARGA ="+idControleCarga);
		sql.addCustomCriteria("MAN.ID_FILIAL_ORIGEM    = "+SessionUtils.getFilialSessao().getIdFilial());
		sql.addCustomCriteria("CONH.ID_CONHECIMENTO NOT IN (" + sqlSubSelect.getSql() + ") ");
	
		sql.addCustomCriteria("DO.ID_PEDIDO_COLETA             = PC.ID_PEDIDO_COLETA (+)");
		sql.addCustomCriteria("PC.ID_CLIENTE                   = PS.ID_PESSOA (+)");
		sql.addCustomCriteria("PC.ID_MANIFESTO_COLETA          = MCC.ID_MANIFESTO_COLETA (+)");
		sql.addCustomCriteria("MCC.ID_CONTROLE_CARGA           = CCC.ID_CONTROLE_CARGA (+)");
		sql.addCustomCriteria("CCC.ID_TRANSPORTADO             = MT.ID_MEIO_TRANSPORTE (+)");

		return sql;
	}
	
	
	private SqlTemplate sqlProjectionDocumento(Long idControleCarga) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("CON.NR_CONHECIMENTO", "NR_CONHECIMENTO");
		sql.addProjection("MAN.NR_PRE_MANIFESTO", "NR_PRE_MANIFESTO");
		sql.addProjection("F_MF.SG_FILIAL", "FILIAL_MF");
		sql.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
		sql.addProjection("FL.SG_FILIAL", "FILIAL_CC");
		sql.addProjection("DO.NR_DOCTO_SERVICO","NR_DOCTO_SERVICO");
		sql.addProjection("DO.QT_VOLUMES","QT_VOLUMES");
		sql.addProjection("NFC.NR_NOTA_FISCAL","NR_NOTA_FISCAL");
		sql.addProjection("VNF.NR_SEQUENCIA_PALETE","NR_SEQUENCIA");
		sql.addProjection("F_DS.SG_FILIAL","FILIAL_DS");
		
		sql.addProjection("PS.NM_PESSOA","REMETENTE");
		sql.addProjection("MT.NR_FROTA");
		sql.addProjection("MT.NR_IDENTIFICADOR");

		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I"),"LOCALIZACAO");
		sql.addProjection("CON.NR_FORMULARIO","NR_FORMULARIO");
	
		sql.addFrom("CONHECIMENTO CON "+
					"JOIN DOCTO_SERVICO DO ON DO.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO "+
					"JOIN PRE_MANIFESTO_DOCUMENTO PMD ON DO.ID_DOCTO_SERVICO = PMD.ID_DOCTO_SERVICO "+
					"JOIN MANIFESTO MAN ON PMD.ID_MANIFESTO = MAN.ID_MANIFESTO " +
					"JOIN PRE_MANIFESTO_VOLUME PMV ON PMV.ID_MANIFESTO = MAN.ID_MANIFESTO " +
					"JOIN FILIAL F_MF ON F_MF.ID_FILIAL = MAN.ID_FILIAL_ORIGEM "+
					"JOIN CONTROLE_CARGA CC ON MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA " +
					"JOIN NOTA_FISCAL_CONHECIMENTO NFC ON DO.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO "+
					"JOIN VOLUME_NOTA_FISCAL VNF ON NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO AND VNF.ID_VOLUME_NOTA_FISCAL = PMV.ID_VOLUME_NOTA_FISCAL " +
					"JOIN FILIAL FL ON FL.ID_FILIAL = CC.ID_FILIAL_ATUALIZA_STATUS " +
					"JOIN FILIAL F_DS ON DO.ID_FILIAL_ORIGEM = F_DS.ID_FILIAL " +
					"JOIN LOCALIZACAO_MERCADORIA LM ON DO.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA "+
					"LEFT OUTER JOIN  PEDIDO_COLETA PC ON DO.ID_PEDIDO_COLETA  = PC.ID_PEDIDO_COLETA "+
					"LEFT OUTER JOIN PESSOA PS ON PC.ID_CLIENTE                   = PS.ID_PESSOA "+
					"LEFT OUTER JOIN MANIFESTO_COLETA MCC ON PC.ID_MANIFESTO_COLETA          = MCC.ID_MANIFESTO_COLETA "+
					"LEFT OUTER JOIN CONTROLE_CARGA CCC ON MCC.ID_CONTROLE_CARGA           = CCC.ID_CONTROLE_CARGA "+
					"LEFT OUTER JOIN MEIO_TRANSPORTE MT ON CCC.ID_TRANSPORTADO             = MT.ID_MEIO_TRANSPORTE ");
		
		sql.addCustomCriteria("CC.ID_CONTROLE_CARGA ="+idControleCarga);
		sql.addCustomCriteria("VNF.TP_VOLUME IN ('U', 'D')");
		return sql;
	}
	
	private SqlTemplate sqlCarregadosIncompletos(Long idControleCarga) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("DISTINCT CON.NR_CONHECIMENTO","NR_CONHECIMENTO");
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("lm.ds_localizacao_mercadoria_i"),"LOCALIZACAO");
		sql.addProjection("NFC.NR_NOTA_FISCAL","NR_NOTA_FISCAL");
		sql.addProjection("VNF.NR_SEQUENCIA_PALETE","NR_SEQUENCIA");
		sql.addProjection("DS.QT_VOLUMES","QT_VOLUMES");
		
		sql.addProjection("FL.SG_FILIAL","FILIAL_CC");
		sql.addProjection("CC.NR_CONTROLE_CARGA","NR_CONTROLE_CARGA");
		sql.addProjection("FM.SG_FILIAL","FILIAL_MF");
		sql.addProjection("M.NR_PRE_MANIFESTO","NR_PRE_MANIFESTO");
		sql.addProjection("FC.SG_FILIAL","FILIAL_DS");
		
		sql.addProjection("CON.NR_FORMULARIO","NR_FORMULARIO");
		
		sql.addProjection("PS.NM_PESSOA","REMETENTE"); 
		sql.addProjection("MT.NR_FROTA");
		sql.addProjection("MT.NR_IDENTIFICADOR");
		
		sql.addFrom("CONHECIMENTO CON");
		sql.addFrom("PRE_MANIFESTO_DOCUMENTO PMD");
		sql.addFrom("DOCTO_SERVICO DS");
		sql.addFrom("MANIFESTO M");
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO NFC");
		sql.addFrom("VOLUME_NOTA_FISCAL VNF");
		sql.addFrom("LOCALIZACAO_MERCADORIA LM");
		sql.addFrom("FILIAL FLA");
		sql.addFrom("FILIAL FL");
		sql.addFrom("FILIAL FM");
		sql.addFrom("FILIAL FC");
		sql.addFrom("PEDIDO_COLETA PC");
		sql.addFrom("CONTROLE_CARGA CC");
		sql.addFrom("PESSOA PS");
		sql.addFrom("MANIFESTO_COLETA MCC");
		sql.addFrom("CONTROLE_CARGA CCC");
		sql.addFrom("MEIO_TRANSPORTE MT");
		
		sql.addCustomCriteria("CON.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO");
		sql.addCustomCriteria("CON.ID_CONHECIMENTO = PMD.ID_DOCTO_SERVICO");
		sql.addCustomCriteria("PMD.ID_MANIFESTO = M.ID_MANIFESTO");
		sql.addCustomCriteria("CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA");
		sql.addCustomCriteria("NFC.ID_CONHECIMENTO = CON.ID_CONHECIMENTO");
		sql.addCustomCriteria("NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO");
		sql.addCustomCriteria("VNF.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA (+)");
		sql.addCustomCriteria("M.ID_FILIAL_ORIGEM = FM.ID_FILIAL");
		sql.addCustomCriteria("CC.ID_FILIAL_ORIGEM = FL.ID_FILIAL");
		sql.addCustomCriteria("CON.ID_FILIAL_ORIGEM = FC.ID_FILIAL");
		sql.addCustomCriteria("DS.ID_PEDIDO_COLETA = PC.ID_PEDIDO_COLETA (+)");
		sql.addCustomCriteria("PC.ID_CLIENTE = PS.ID_PESSOA (+)");
		sql.addCustomCriteria("PC.ID_MANIFESTO_COLETA = MCC.ID_MANIFESTO_COLETA (+)");
		sql.addCustomCriteria("MCC.ID_CONTROLE_CARGA = CCC.ID_CONTROLE_CARGA (+)");
		sql.addCustomCriteria("CCC.ID_TRANSPORTADO = MT.ID_MEIO_TRANSPORTE (+)");
		sql.addCustomCriteria("M.ID_CONTROLE_CARGA = "+idControleCarga);
		sql.addCustomCriteria("VNF.ID_LOCALIZACAO_FILIAL = FLA.ID_FILIAL (+)");
		sql.addCustomCriteria("VNF.TP_VOLUME IN ('U','D') ");
		sql.addCustomCriteria("VNF.ID_VOLUME_NOTA_FISCAL NOT  IN (SELECT PMV.ID_VOLUME_NOTA_FISCAL FROM PRE_MANIFESTO_VOLUME PMV WHERE PMV.ID_MANIFESTO = M.ID_MANIFESTO)");

		sql.addOrderBy("VNF.NR_SEQUENCIA_PALETE");
		
	    return sql;
    }

	private SqlTemplate sqlCarregadosSemPreManifestoDocumento(Long idControleCarga) {
		SqlTemplate sqlSubquery = createSqlTemplate();
		sqlSubquery.addProjection("PMD.ID_DOCTO_SERVICO");
		
		sqlSubquery.addFrom("MANIFESTO","MA"); 
		sqlSubquery.addFrom("CONTROLE_CARGA","CC");
		sqlSubquery.addFrom("PRE_MANIFESTO_DOCUMENTO","PMD");
		
		sqlSubquery.addCustomCriteria("MA.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA");
		sqlSubquery.addCustomCriteria("MA.ID_MANIFESTO = PMD.ID_MANIFESTO");
		sqlSubquery.addCustomCriteria("CC.ID_CONTROLE_CARGA = "+idControleCarga);

		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("DISTINCT MA.NR_PRE_MANIFESTO","NR_PRE_MANIFESTO");
		sql.addProjection("FCC.SG_FILIAL","FILIAL_CC");
		sql.addProjection("CC.NR_CONTROLE_CARGA","NR_CONTROLE_CARGA"); 
		sql.addProjection("FDS.SG_FILIAL","FILIAL_DS");
		sql.addProjection("DS.NR_DOCTO_SERVICO","NR_CONHECIMENTO");
		sql.addProjection("NFC.NR_NOTA_FISCAL","NR_NOTA_FISCAL"); 
		sql.addProjection("VNF.NR_SEQUENCIA_PALETE","NR_SEQUENCIA");
		sql.addProjection("DS.QT_VOLUMES","QT_VOLUMES");
		sql.addProjection("FMF.SG_FILIAL","FILIAL_MF");
		sql.addProjection("PS.NM_PESSOA","REMETENTE"); 
		sql.addProjection("MT.NR_FROTA");
		sql.addProjection("MT.NR_IDENTIFICADOR");      
		
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("lm.ds_localizacao_mercadoria_i"),"LOCALIZACAO");
		sql.addProjection("DS.QT_VOLUMES","NR_FORMULARIO");

		sql.addFrom("MANIFESTO","MA");
		sql.addFrom("CONTROLE_CARGA","CC ");
		sql.addFrom("PRE_MANIFESTO_VOLUME","PMV");
		sql.addFrom("CONHECIMENTO","CON"); 
		sql.addFrom("DOCTO_SERVICO","DS");
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO","NFC"); 
		sql.addFrom("VOLUME_NOTA_FISCAL","VNF");
		sql.addFrom("FILIAL","FDS");
		sql.addFrom("FILIAL","FCC");
		sql.addFrom("FILIAL","FMF"); 
		sql.addFrom("LOCALIZACAO_MERCADORIA","LM");
		sql.addFrom("PEDIDO_COLETA            PC");
		sql.addFrom("PESSOA                PS");
		sql.addFrom("MANIFESTO_COLETA         MCC");
		sql.addFrom("CONTROLE_CARGA           CCC");
		sql.addFrom("MEIO_TRANSPORTE       MT");
		sql.addFrom("FILIAL", "FLA");		

		sql.addCustomCriteria("MA.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA");
		sql.addCustomCriteria("MA.ID_MANIFESTO      = PMV.ID_MANIFESTO");
		sql.addCustomCriteria("PMV.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO");
		sql.addCustomCriteria("CON.ID_CONHECIMENTO  = DS.ID_DOCTO_SERVICO");
		sql.addCustomCriteria("DS.ID_DOCTO_SERVICO  = NFC.ID_CONHECIMENTO");
		sql.addCustomCriteria("NFC.ID_CONHECIMENTO             = DS.ID_DOCTO_SERVICO");
		sql.addCustomCriteria("NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO");
		sql.addCustomCriteria("DS.ID_FILIAL_ORIGEM  = FDS.ID_FILIAL");
		sql.addCustomCriteria("CC.ID_FILIAL_ORIGEM  = FCC.ID_FILIAL");
		sql.addCustomCriteria("MA.ID_FILIAL_ORIGEM  = FMF.ID_FILIAL");
		sql.addCustomCriteria("DS.ID_LOCALIZACAO_MERCADORIA      = LM.ID_LOCALIZACAO_MERCADORIA");
		
		sql.addCustomCriteria("DS.ID_PEDIDO_COLETA             = PC.ID_PEDIDO_COLETA (+)");
		sql.addCustomCriteria("PC.ID_CLIENTE                   = PS.ID_PESSOA (+)");
		sql.addCustomCriteria("PC.ID_MANIFESTO_COLETA          = MCC.ID_MANIFESTO_COLETA (+)");
		sql.addCustomCriteria("MCC.ID_CONTROLE_CARGA           = CCC.ID_CONTROLE_CARGA (+)");
		sql.addCustomCriteria("CCC.ID_TRANSPORTADO             = MT.ID_MEIO_TRANSPORTE (+)"); 
		
		sql.addCustomCriteria("VNF.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA (+)");
		sql.addCustomCriteria("VNF.ID_LOCALIZACAO_FILIAL = FLA.ID_FILIAL (+)");
		sql.addCustomCriteria("VNF.TP_VOLUME IN ('U','D') ");
				
		sql.addCustomCriteria("CC.ID_CONTROLE_CARGA =" +idControleCarga);
		sql.addCustomCriteria("PMV.ID_DOCTO_SERVICO NOT IN ("+sqlSubquery.toString()+")"); 

		sql.addOrderBy("MA.NR_PRE_MANIFESTO");
		
		sql.addOrderBy("DS.NR_DOCTO_SERVICO");
		sql.addOrderBy("NFC.NR_NOTA_FISCAL");
		sql.addOrderBy("VNF.NR_SEQUENCIA_PALETE");
		
	    return sql;
    }
	
	private SqlTemplate sqlProjectionConhecimento(Long idControleCarga, Long idCarregamentoDescarga){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("CONH.ID_CONHECIMENTO ");
		
		sql.addFrom("CONHECIMENTO CONH " +
					"JOIN CONTROLE_CARGA_CONH_SCAN CCCS " +
					"ON CCCS.ID_CONHECIMENTO = CONH.ID_CONHECIMENTO " +
					"JOIN CARREGAMENTO_DESCARGA CADE " +
					"ON CCCS.ID_CARREGAMENTO_DESCARGA = CADE.ID_CARREGAMENTO_DESCARGA " +
					"JOIN CONTROLE_CARGA CCA " +
					"ON CCCS.ID_CONTROLE_CARGA = CCA.ID_CONTROLE_CARGA ");
		
		sql.addCustomCriteria("CCA.ID_CONTROLE_CARGA ="+idControleCarga);
		sql.addCustomCriteria("CADE.ID_CARREGAMENTO_DESCARGA ="+idCarregamentoDescarga);
		
		return sql;
		
	}

}
