package com.mercurio.lms.contasreceber.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.service.RedecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author José Rodrigo Moraes
 * @since 26/04/2006 
 *
 * @spring.bean id="lms.contasreceber.emitirRedecoService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirRedecos.jasper"
 */
public class EmitirRedecoService extends ReportServiceSupport {
	
	private RedecoService redecoService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();
		
		if( tfm.getLong("nrRedeco") != null ){
			sql.addFilterSummary("redeco",tfm.getString("siglaFilial") + " " + FormatUtils.completaDados(tfm.getLong("nrRedeco"),"0",10,0,FormatUtils.ESQUERDA));
		}

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
	 * Monta a query de pesquisa para o relatório
	 * @param tfm Critérios de pesquisa
	 * @return SqlTemplate
	 */
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("REDECO.ID_REDECO, " +
						  "FILIAL_REDECO.SG_FILIAL || ' ' || TO_CHAR(REDECO.NR_REDECO, '0000000000') AS NR_REDECO_COMPLETO, " +
				          "FILIAL_FATURA.SG_FILIAL || ' ' || TO_CHAR(FATURA.NR_FATURA, '0000000000') AS NR_FATURA_COMPLETO, " +
				          "REDECO.NM_RESPONSAVEL_COBRANCA, " +
				          "REDECO.TP_FINALIDADE AS TP_FINALIDADE, " +
				          "REDECO.DT_RECEBIMENTO, " +
				          "FATURA.DT_EMISSAO, " +
				          "FATURA.DT_VENCIMENTO, " +
				          "PESSOA_CLIENTE.NM_PESSOA CLIENTE, " +
				          "FATURA.VL_TOTAL VL_FRETE, " +
				          "FATURA.VL_DESCONTO, " +
				          "ITEM_REDECO.VL_JUROS, " +
				          "ITEM_REDECO.VL_TARIFA VL_TAXA, " +				          				          
				          "(SELECT DOC.NR_DOCTO_SERVICO " +
				          " FROM	ITEM_FATURA IFAT, " +
				          "         DEVEDOR_DOC_SERV_FAT DEV, " +
				          "         DOCTO_SERVICO DOC, " +
				          "         FILIAL FIL " +
				          " WHERE	IFAT.ID_FATURA			= FATURA.ID_FATURA " +
				          "   AND	IFAT.ID_DEVEDOR_DOC_SERV_FAT 	= DEV.ID_DEVEDOR_DOC_SERV_FAT " +
				          "   AND	DEV.ID_DOCTO_SERVICO 		= DOC.ID_DOCTO_SERVICO " +
				          "   AND	DOC.ID_FILIAL_ORIGEM 		= FIL.ID_FILIAL " +
				          "   AND	FATURA.QT_DOCUMENTOS 		= 1) AS NR_DOCUMENTO_SERVICO, " +
				          
				          "(SELECT FIL.SG_FILIAL " +
				          " FROM	ITEM_FATURA IFAT, " +
				          "         DEVEDOR_DOC_SERV_FAT DEV, " +
				          "         DOCTO_SERVICO DOC, " +
				          "         FILIAL FIL " +
				          " WHERE	IFAT.ID_FATURA			= FATURA.ID_FATURA " +
				          "   AND	IFAT.ID_DEVEDOR_DOC_SERV_FAT 	= DEV.ID_DEVEDOR_DOC_SERV_FAT " +
				          "   AND	DEV.ID_DOCTO_SERVICO 		= DOC.ID_DOCTO_SERVICO " +
				          "   AND	DOC.ID_FILIAL_ORIGEM 		= FIL.ID_FILIAL " +
				          "   AND	FATURA.QT_DOCUMENTOS 		= 1) AS SG_FILIAL_DOCTO, " +
				          
				          "NVL((SELECT 	"+PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I") +
				          "     FROM	ITEM_FATURA IFAT, " +
				          "				DEVEDOR_DOC_SERV_FAT DEV, " +
				          "				DOCTO_SERVICO DOC, " +
				          "				FILIAL FIL, " +
				          "				VALOR_DOMINIO VD, " +
				          "             DOMINIO D " +
				          "     WHERE	IFAT.ID_FATURA 			        = FATURA.ID_FATURA" +
				          "       AND	IFAT.ID_DEVEDOR_DOC_SERV_FAT 	= DEV.ID_DEVEDOR_DOC_SERV_FAT " +
				          "       AND	DEV.ID_DOCTO_SERVICO 		    = DOC.ID_DOCTO_SERVICO " +
				          "       AND	DOC.ID_FILIAL_ORIGEM 		    = FIL.ID_FILIAL " +
				          "       AND   VD.ID_DOMINIO                   = D.ID_DOMINIO " +
				          "       AND   LOWER(D.NM_DOMINIO)                    = LOWER('DM_TIPO_DOCUMENTO_SERVICO') " +
				          "       AND   LOWER(VD.VL_VALOR_DOMINIO)             = LOWER(DOC.TP_DOCUMENTO_SERVICO)" +
				          "       AND	FATURA.QT_DOCUMENTOS 		    = 1), 'FAT') AS DS_TP_DOCUMENTO, " +
				          
				          "NVL((SELECT 	DOC.TP_DOCUMENTO_SERVICO " +
				          "     FROM	ITEM_FATURA IFAT, " +
				          "				DEVEDOR_DOC_SERV_FAT DEV, " +
				          "				DOCTO_SERVICO DOC, " +
				          "				FILIAL FIL, " +
				          "				VALOR_DOMINIO VD, " +
				          "             DOMINIO D " +
				          "     WHERE	IFAT.ID_FATURA 			        = FATURA.ID_FATURA" +
				          "       AND	IFAT.ID_DEVEDOR_DOC_SERV_FAT 	= DEV.ID_DEVEDOR_DOC_SERV_FAT " +
				          "       AND	DEV.ID_DOCTO_SERVICO 		    = DOC.ID_DOCTO_SERVICO " +
				          "       AND	DOC.ID_FILIAL_ORIGEM 		    = FIL.ID_FILIAL " +
				          "       AND   VD.ID_DOMINIO                   = D.ID_DOMINIO " +
				          "       AND   LOWER(D.NM_DOMINIO)                    = LOWER('DM_TIPO_DOCUMENTO_SERVICO') " +
				          "       AND   LOWER(VD.VL_VALOR_DOMINIO)             = LOWER(DOC.TP_DOCUMENTO_SERVICO)" +
				          "       AND	FATURA.QT_DOCUMENTOS 		    = 1), 'FAT') AS TP_DOCUMENTO_SERVICO, " +
				          
				          "(FATURA.VL_TOTAL - FATURA.VL_DESCONTO + ITEM_REDECO.VL_JUROS - ITEM_REDECO.VL_TARIFA) AS VALOR_RECEBIMENTO");
		
		sql.addFrom("REDECO " +
				    "	INNER JOIN FILIAL FILIAL_REDECO       ON REDECO.ID_FILIAL        = FILIAL_REDECO.ID_FILIAL " +
				    "   INNER JOIN ITEM_REDECO                ON REDECO.ID_REDECO        = ITEM_REDECO.ID_REDECO " +
				    "   LEFT OUTER JOIN FATURA                ON FATURA.ID_FATURA        = ITEM_REDECO.ID_FATURA " +
				    "   LEFT OUTER JOIN FILIAL FILIAL_FATURA  ON FILIAL_FATURA.ID_FILIAL = FATURA.ID_FILIAL " +
				    "   LEFT OUTER JOIN CLIENTE               ON CLIENTE.ID_CLIENTE      = FATURA.ID_CLIENTE " +
				    "   LEFT OUTER JOIN PESSOA PESSOA_CLIENTE ON CLIENTE.ID_CLIENTE      = PESSOA_CLIENTE.ID_PESSOA " );
		
		sql.addCriteria("FATURA.TP_ABRANGENCIA","=","N");
		
		if( tfm.getLong("redeco.idRedeco") != null ){
			sql.addCriteria("REDECO.ID_REDECO","=",tfm.getLong("redeco.idRedeco"));
			sql.addCriteria("FILIAL_REDECO.ID_FILIAL","=",tfm.getLong("filial.idFilial")); 
			//sendo da filial sempre pode passar
			//caso nao seja da filial tem que ter a situação diferente de DI
			sql.addCustomCriteria(" (( REDECO.TP_SITUACAO_REDECO <> 'DI') or (  FILIAL_REDECO.ID_FILIAL = "+SessionUtils.getFilialSessao().getIdFilial()+"))");
			// Situacação deve ser diferente de DI - digitado e não poderá ser CA - cancelado
			sql.addCriteria("REDECO.TP_SITUACAO_REDECO","<>","CA");
		} else {
			sql.addCriteria("REDECO.TP_SITUACAO_REDECO","=","DI");
			//tem que pertencer a filial do usuario logado
			sql.addCriteria("FILIAL_REDECO.ID_FILIAL","=",SessionUtils.getFilialSessao().getIdFilial());
		}
		
		sql.addCustomCriteria("( REDECO.TP_SITUACAO_WORKFLOW = ? OR REDECO.TP_SITUACAO_WORKFLOW IS NULL )" );
		sql.addCriteriaValue("A");
		
		sql.addOrderBy("NR_REDECO_COMPLETO");
		sql.addOrderBy("NR_FATURA_COMPLETO");		
		
		return sql;
	}

	
	@Override
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_FINALIDADE", "DM_FINALIDADE_REDECO");
		super.configReportDomains(config);
	}

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}

	/**
	 * Método executado depois da montagem do relatório
	 */
	@Override
	public void postExecute(Map parameters) {
	
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);
		
		//depois de buscar os dados do relatorio, faz update
		List list = getJdbcTemplate().queryForList(sql.getSql(),sql.getCriteria());
		
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			
			ListOrderedMap element = (ListOrderedMap) iter.next();
			
			Long idRedeco = Long.valueOf(((BigDecimal) element.get("ID_REDECO")).longValue());
			
			Redeco redeco = redecoService.findById(idRedeco);
			
			//Só pode trocar a situação do redeco se for digitado
			if (redeco.getTpSituacaoRedeco().getValue().equals("DI")){
				redeco.setTpSituacaoRedeco(new DomainValue("EM"));
				
				redecoService.storeBasic(redeco);
			}
		}
	}
	
}
