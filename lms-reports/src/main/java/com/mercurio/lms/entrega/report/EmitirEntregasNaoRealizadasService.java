package com.mercurio.lms.entrega.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author José Rodrigo Moraes
 * @since  24/05/2006
 * 
 * @spring.bean id="lms.entrega.emitirEntregasNaoRealizadasService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirEntregasNaoRealizadas.jasper"
 */
public class EmitirEntregasNaoRealizadasService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = getSqlTemplate(tfm);
        
        createFilterSummary(sql,tfm);
        
	    JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	                       
        Map parametersReport = new HashMap();
        
        /** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        
        jr.setParameters(parametersReport);
        
        return jr;
        
	}
	
	/**
	 * Monta os dados de filtragem para o cabeçalho do relatório
	 * @param sql SqlTemplate que abrigará os filtros
	 * @param tfm Filtros da tela
	 */
	private void createFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		
		if( (tfm.getString("sgFilial") != null && !tfm.getString("sgFilial").equals("")) &&  
			(tfm.getString("rotaColetaEntrega.filial.pessoa.nmFantasia") != null && !tfm.getString("rotaColetaEntrega.filial.pessoa.nmFantasia").equals(""))){
			sql.addFilterSummary("filial",tfm.getString("sgFilial") + " - " + tfm.getString("rotaColetaEntrega.filial.pessoa.nmFantasia"));
		}
		
		if( (tfm.getString("numeroRota") != null && !tfm.getString("numeroRota").equals("")) && 
			(tfm.getString("rotaColetaEntrega.dsRota") != null && !tfm.getString("rotaColetaEntrega.dsRota").equals(""))){
			sql.addFilterSummary("rotaEntrega",tfm.getString("numeroRota") + " - " + tfm.getString("rotaColetaEntrega.dsRota"));
		}
			
		if(tfm.getString("nmRemetente") != null && !tfm.getString("nmRemetente").equals("")){
			sql.addFilterSummary("remetente",tfm.getString("nmRemetente"));
		}
		
		if(tfm.getLong("doctoServico.idDoctoServico")!= null){
			sql.addFilterSummary("documentoServico", tfm.getString("doctoServico.filialByIdFilialOrigem.sgFilial") + " " + tfm.getString("nrDoctoServico"));
		}
		
		if(tfm.getLong("manifestoEntrega.nrManifestoEntrega")!= null){
			
			sql.addFilterSummary("manifesto", tfm.getString("manifestoEntrega.filial.sgFilial")+ " " + tfm.getString("manifestoEntrega.nrManifestoEntrega"));
		}
		
		if(tfm.getLong("controleCarga.nrControleCarga")!= null){
			
			sql.addFilterSummary("controleCarga",tfm.getString("controleCarga.filialByIdFilialOrigem.sgFilial")+ " " + tfm.getString("controleCarga.nrControleCarga"));
		}
		
		sql.addFilterSummary("periodoFechamentoInicial",JTFormatUtils.format(tfm.getYearMonthDay("periodoInicial")));
		sql.addFilterSummary("periodoFechamentoFinal",JTFormatUtils.format(tfm.getYearMonthDay("periodoFinal")));
		
	}

	/**
	 * Monta a query principal para o relatório
	 * @param parameters Critérios de pesquisa
	 * @return SqlTemplate com a query e seus parâmetros
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("DS.ID_SERVICO  				AS ID_SERVICO, " +
				PropertyVarcharI18nProjection.createProjection("SER.DS_SERVICO_I","DS_SERVICO") + ", " +
				          "DS.TP_DOCUMENTO_SERVICO 		AS TP_DOCUMENTO_SERVICO, " +
				          "FILIAL_ORIGEM.SG_FILIAL 		AS SG_FILIAL_ORIGEM, " +
				          "DS.NR_DOCTO_SERVICO     		AS NR_DOCTO_SERVICO, " +
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","DS_TP_DOCUMENTO_SERVICO") + ", " +
				          "(SELECT MIN(NR_NOTA_FISCAL) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO) AS NR_NOTA_FISCAL, " +
				          "PESSOA_DEST.TP_IDENTIFICACAO AS TP_IDENTIFICACAO_DEST, " +
				          "PESSOA_DEST.NR_IDENTIFICACAO AS NR_IDENTIFICACAO_DEST, " +
				          "PESSOA_DEST.NM_PESSOA        AS NM_DESTINATARIO, " +
				          "DS.DS_ENDERECO_ENTREGA_REAL  AS DS_ENDERECO_ENTREGA, " +
				          "DS.DH_EMISSAO                AS DH_EMISSAO, " +
				          "DS.DT_PREV_ENTREGA           AS DT_PREV_ENTREGA, " +
				          "OCO.CD_OCORRENCIA_ENTREGA    AS CD_OCORRENCIA_ENTREGA, " +
				          PropertyVarcharI18nProjection.createProjection("OCO.DS_OCORRENCIA_ENTREGA_I","DS_OCORRENCIA_ENTREGA") + " , " + 
				          "MANI_ENT.DH_FECHAMENTO       AS DH_FECHAMENTO ");
		
		sql.addFrom("CONTROLE_CARGA CC " +
				    "	INNER JOIN MANIFESTO MANI                           ON CC.ID_CONTROLE_CARGA               = MANI.ID_CONTROLE_CARGA " +
				    "	INNER JOIN MANIFESTO_ENTREGA MANI_ENT               ON MANI.ID_MANIFESTO                  = MANI_ENT.ID_MANIFESTO_ENTREGA" +
				    "   INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO MANI_ENT_DOC ON MANI_ENT.ID_MANIFESTO_ENTREGA      = MANI_ENT_DOC.ID_MANIFESTO_ENTREGA " +
				    "   INNER JOIN V_OCORRENCIA_ENTREGA_I OCO          		ON MANI_ENT_DOC.ID_OCORRENCIA_ENTREGA = OCO.ID_OCORRENCIA_ENTREGA " +
				    "   INNER JOIN DOCTO_SERVICO DS                         ON MANI_ENT_DOC.ID_DOCTO_SERVICO      = DS.ID_DOCTO_SERVICO " +
				    "   INNER JOIN FILIAL FILIAL_ORIGEM                     ON DS.ID_FILIAL_ORIGEM                = FILIAL_ORIGEM.ID_FILIAL " +
				    "   LEFT OUTER JOIN CLIENTE CLIENTE_REMET               ON DS.ID_CLIENTE_REMETENTE 	    	  = CLIENTE_REMET.ID_CLIENTE " +
				    "   LEFT OUTER JOIN CLIENTE CLIENTE_DEST                ON DS.ID_CLIENTE_DESTINATARIO         = CLIENTE_DEST.ID_CLIENTE " +
				    "   LEFT OUTER JOIN PESSOA PESSOA_DEST                  ON CLIENTE_DEST.ID_CLIENTE            = PESSOA_DEST.ID_PESSOA " +
				    "   LEFT OUTER JOIN V_SERVICO_I SER                     ON DS.ID_SERVICO                      = SER.ID_SERVICO, " +
				    "VALOR_DOMINIO VD " +
				    "	INNER JOIN DOMINIO D                                ON VD.ID_DOMINIO                      = D.ID_DOMINIO");
		
		sql.addCriteria("OCO.TP_OCORRENCIA","<>","E");
		sql.addCriteria("OCO.TP_OCORRENCIA","<>","A");
		sql.addCriteria("MANI.TP_STATUS_MANIFESTO","<>","CA");
		sql.addCriteria("D.NM_DOMINIO","=","DM_TIPO_DOCUMENTO_SERVICO"); 
		sql.addJoin("VD.VL_VALOR_DOMINIO","DS.TP_DOCUMENTO_SERVICO");
		
		sql.addCriteria("MANI_ENT.ID_FILIAL","=",parameters.getString("rotaColetaEntrega.filial.idFilial"));
		sql.addCriteria("CC.ID_ROTA_COLETA_ENTREGA","=",parameters.getString("rotaColetaEntrega.idRotaColetaEntrega"));
		sql.addCriteria("CLIENTE_REMET.ID_CLIENTE","=",parameters.getString("cliente.idCliente"));
		
		if( parameters.getYearMonthDay("periodoInicial") != null && parameters.getYearMonthDay("periodoFinal") != null ){
			sql.addCustomCriteria("TRUNC(CAST(MANI_ENT.DH_FECHAMENTO AS DATE)) BETWEEN  ? AND ?");
			sql.addCriteriaValue(parameters.getYearMonthDay("periodoInicial"));
			sql.addCriteriaValue(parameters.getYearMonthDay("periodoFinal"));
		}
		
		if(parameters.getLong("doctoServico.idDoctoServico")!= null){
			sql.addCriteria("DS.ID_DOCTO_SERVICO","=",parameters.getString("doctoServico.idDoctoServico"));
		}
		
		if(parameters.getLong("manifestoEntrega.idManifestoEntrega")!= null){
			sql.addCriteria("MANI_ENT.ID_MANIFESTO_ENTREGA","=",parameters.getString("manifestoEntrega.idManifestoEntrega"));
		}
		
		if(parameters.getLong("controleCarga.idControleCarga")!= null){
			sql.addCriteria("CC.ID_CONTROLE_CARGA","=",parameters.getString("controleCarga.idControleCarga"));
		}
		
		sql.addOrderBy("DS_SERVICO, " +				
				       "DS.TP_DOCUMENTO_SERVICO, " +
				       "FILIAL_ORIGEM.SG_FILIAL, " +
				       "DS.NR_DOCTO_SERVICO, " +
				       PropertyVarcharI18nProjection.createProjection("DS_OCORRENCIA_ENTREGA_I") );
	
		return sql;
	}	
}
