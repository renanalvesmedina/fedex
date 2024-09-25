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
 * @since  22/05/2006
 * 
 * @spring.bean id="lms.entrega.emitirOcorrenciasClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirOcorrenciasCliente.jasper"
 */
public class EmitirOcorrenciasClienteService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
        SqlTemplate sql = getSqlTemplate(tfm);
        
        sql = createFilterSummary(sql,tfm);
	    
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
	private SqlTemplate createFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		
		if( (tfm.getString("sgFilialOrigem") != null && !tfm.getString("sgFilialOrigem").equals("")) &&  
			(tfm.getString("nmFilialOrigem") != null && !tfm.getString("nmFilialOrigem").equals(""))){
			sql.addFilterSummary("filialOrigem",tfm.getString("sgFilialOrigem") + " - " + tfm.getString("nmFilialOrigem"));
		}
			
		if( (tfm.getString("nrIdentificacaoFormatado") != null && !tfm.getString("nrIdentificacaoFormatado").equals("")) && 
			(tfm.getString("nmRemetente") != null && !tfm.getString("nmRemetente").equals(""))){
			sql.addFilterSummary("remetente",tfm.getString("nrIdentificacaoFormatado") + " - " + tfm.getString("nmRemetente"));
		}
		
		if( (tfm.getString("sgFilialDestino") != null && !tfm.getString("sgFilialDestino").equals("")) && 
			(tfm.getString("nmFilialDestino") != null && !tfm.getString("nmFilialDestino").equals(""))){
			sql.addFilterSummary("filialDestino",tfm.getString("sgFilialDestino") + " - " + tfm.getString("nmFilialDestino"));
		}
		
		sql.addFilterSummary("ocasionadaPor",tfm.getString("dsOcasionadaPor"));
		
		sql.addFilterSummary("periodoOcorrenciaInicial",JTFormatUtils.format(tfm.getYearMonthDay("lancamentoInicial")));
		sql.addFilterSummary("periodoOcorrenciaFinal",JTFormatUtils.format(tfm.getYearMonthDay("lancamentoFinal")));
		
		return sql;
		 
	}
	
	/**
	 * Monta a query principal para o relatório
	 * @param parameters Critérios de pesquisa
	 * @return SqlTemplate com a query e seus parâmetros
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) {
		
		SqlTemplate sql = createSqlTemplate();
		 
		sql.addProjection("FILIAL_DESTINO.ID_FILIAL 		AS ID_FILIAL_DESTINO, " +
				          "FILIAL_DESTINO.SG_FILIAL 		AS SG_FILIAL_DESTINO, " +
				          "PESSOA_DESTINO.NM_FANTASIA 		AS NM_FILIAL_DESTINO, " +
				          "CLIENTE_REMET.ID_CLIENTE 		AS ID_CLIENTE_REMETENTE, " +
				          "PESSOA_REMET.TP_IDENTIFICACAO 	AS TP_IDENTIFICACAO_REMET, " +
				          "PESSOA_REMET.NR_IDENTIFICACAO 	AS NR_IDENTIFICACAO_REMET, " +
				          "PESSOA_REMET.NM_PESSOA 			AS NM_REMETENTE, " +
				          "DS.TP_DOCUMENTO_SERVICO 			AS TP_DOCUMENTO_SERVICO, " +
				          "FILIAL_ORIGEM.SG_FILIAL 			AS SG_FILIAL_ORIGEM, " +
				          "DS.NR_DOCTO_SERVICO 				AS NR_DOCTO_SERVICO, " +
				          "DS.DH_EMISSAO 					AS DH_EMISSAO, " +
				          "(SELECT MIN(NR_NOTA_FISCAL) FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO) AS NR_NOTA_FISCAL, " +
				          "PESSOA_DEST.TP_IDENTIFICACAO 	AS TP_IDENTIFICACAO_DEST, " +
				          "PESSOA_DEST.NR_IDENTIFICACAO 	AS NR_IDENTIFICACAO_DEST, " +
				          "PESSOA_DEST.NM_PESSOA 			AS NM_DESTINATARIO, " +
				          "DS.DT_PREV_ENTREGA 				AS DT_PREV_ENTREGA, " +
				          "MED.DH_OCORRENCIA 				AS DH_BAIXA, " +
				          "OCO.CD_OCORRENCIA_ENTREGA		AS CD_OCORRENCIA_ENTREGA, " +
				          PropertyVarcharI18nProjection.createProjection("OCO.DS_OCORRENCIA_ENTREGA_I" ,"DS_OCORRENCIA_ENTREGA") +" ,"+
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","DS_TP_DOCUMENTO_SERVICO"));
		
		sql.addFrom("MANIFESTO_ENTREGA_DOCUMENTO MED " +
				    "	LEFT OUTER JOIN OCORRENCIA_ENTREGA OCO ON MED.ID_OCORRENCIA_ENTREGA = OCO.ID_OCORRENCIA_ENTREGA" +
				    
				    "   INNER JOIN DOCTO_SERVICO DS            ON MED.ID_DOCTO_SERVICO 			= DS.ID_DOCTO_SERVICO " +
				    "   INNER JOIN VALOR_DOMINIO VD        ON VD.VL_VALOR_DOMINIO 			= DS.TP_DOCUMENTO_SERVICO " +
        			"   INNER JOIN DOMINIO DOM                 ON VD.ID_DOMINIO 				= DOM.ID_DOMINIO" +
        			
				    "   INNER JOIN FILIAL FILIAL_ORIGEM        ON DS.ID_FILIAL_ORIGEM 			= FILIAL_ORIGEM.ID_FILIAL " +
				    "   INNER JOIN PESSOA PESSOA_ORIGEM        ON FILIAL_ORIGEM.ID_FILIAL	 	= PESSOA_ORIGEM.ID_PESSOA " +
				    
				    "   LEFT OUTER JOIN FILIAL FILIAL_DESTINO  ON DS.ID_FILIAL_DESTINO 			= FILIAL_DESTINO.ID_FILIAL " +
				    "   LEFT OUTER JOIN PESSOA PESSOA_DESTINO  ON FILIAL_DESTINO.ID_FILIAL 		= PESSOA_DESTINO.ID_PESSOA " +
				    
				    "   LEFT OUTER JOIN CLIENTE CLIENTE_REMET  ON DS.ID_CLIENTE_REMETENTE 		= CLIENTE_REMET.ID_CLIENTE " +
				    "   LEFT OUTER JOIN PESSOA PESSOA_REMET    ON CLIENTE_REMET.ID_CLIENTE 		= PESSOA_REMET.ID_PESSOA " +
				    
				    "   LEFT OUTER JOIN CLIENTE CLIENTE_DEST   ON DS.ID_CLIENTE_DESTINATARIO 	= CLIENTE_DEST.ID_CLIENTE " +
				    "   LEFT OUTER JOIN PESSOA PESSOA_DEST     ON CLIENTE_DEST.ID_CLIENTE 		= PESSOA_DEST.ID_PESSOA ");
		
		sql.addCustomCriteria("DOM.NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO'");				
		sql.addCriteria("OCO.TP_OCORRENCIA","<>","E");
		
		sql.addCriteria("FILIAL_ORIGEM.ID_FILIAL","=",parameters.getLong("filialOrigem.idFilial"));		
		sql.addCriteria("CLIENTE_REMET.ID_CLIENTE","=",parameters.getLong("cliente.idCliente"));
		sql.addCriteria("FILIAL_DESTINO.ID_FILIAL","=",parameters.getLong("filialDestino.idFilial"));
		
		if( parameters.getString("ocasionadaPor") != null && !parameters.getString("ocasionadaPor").equals("A")){
			if( parameters.getString("ocasionadaPor").equals("M") ){//Mercúrio
				sql.addCriteria("OCO.BL_OCASIONADO_MERCURIO","=","S");
			} else if( parameters.getString("ocasionadaPor").equals("C") ){//Cliente
				sql.addCriteria("OCO.BL_OCASIONADO_MERCURIO","=","N");
			}
		}
		
		if( parameters.getYearMonthDay("lancamentoInicial") != null && parameters.getYearMonthDay("lancamentoFinal") != null ){
			sql.addCustomCriteria("TRUNC(CAST(MED.DH_OCORRENCIA AS DATE)) BETWEEN ? AND ?");		
			sql.addCriteriaValue(parameters.getYearMonthDay("lancamentoInicial"));
			sql.addCriteriaValue(parameters.getYearMonthDay("lancamentoFinal"));
		}
		
		sql.addOrderBy("FILIAL_DESTINO.SG_FILIAL, " +
				       "PESSOA_REMET.NM_PESSOA, " +
				       "DS.TP_DOCUMENTO_SERVICO, " +
				       "FILIAL_ORIGEM.SG_FILIAL, " +
				       "DS.NR_DOCTO_SERVICO, " +
				       "MED.DH_OCORRENCIA");
		
		return sql;
		
	}	
}
