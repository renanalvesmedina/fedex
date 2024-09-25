package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.emitirClienteAfetadoAtendimentoService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/atualizarTrocaFiliais.jasper"
 */
public class EmitirClienteAfetadoAtendimentoService extends
		ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		 
		SqlTemplate sql = montaSql(tfm); 
		
		if (StringUtils.isNotBlank(tfm.getString("nmMunicipio")))
			sql.addFilterSummary("municipio", tfm.getString("nmMunicipio"));
		
		if (StringUtils.isNotBlank(tfm.getString("sgFilialAtual")))
			sql.addFilterSummary("filialSubstituicao", tfm.getString("sgFilialAtual"));
		
		if (StringUtils.isNotBlank(tfm.getString("sgFilialNova")))
			sql.addFilterSummary("filialSubstituinte", tfm.getString("sgFilialNova"));	
		
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
    	        
        Map parametersReport = new HashMap();
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
		
		return jr;
	}   

	public void configReportDomains(ReportDomainConfig config) {
		 config.configDomainField("TP_IDENTIFICACAO", "DM_TIPO_IDENTIFICACAO"); 
	}
	
	private SqlTemplate montaSql(TypedFlatMap parameters){
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("P.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("P.TP_IDENTIFICACAO", "TIPO_ID");
		sql.addProjection("P.NR_IDENTIFICACAO", "NR_IDENTIFICACAO"); 
		sql.addProjection("P.NM_PESSOA", "NM_PESSOA");
		sql.addProjection("C.ID_CLIENTE");
		
		sql.addProjection("(SELECT F_.SG_FILIAL || ' - ' || P_.NM_FANTASIA"
						 +"	FROM  FILIAL F_"
						 +"      ,PESSOA P_"
						 +" WHERE F_.ID_FILIAL = (CASE WHEN HTFC.BL_FILIAL_RESPONSAVEL = 'N' THEN C.ID_FILIAL_ATENDE_OPERACIONAL ELSE F.ID_FILIAL END)"		    
						 +"       AND F_.ID_FILIAL = P_.ID_PESSOA)", "UNIDADE_RESPONSAVEL_ANTIGA");
		
		sql.addProjection("(SELECT F_.SG_FILIAL || ' - ' || P_.NM_FANTASIA"
						 +"	FROM  FILIAL F_"
						 +"		 ,PESSOA P_"
						 +"	WHERE F_.ID_FILIAL = (CASE WHEN HTFC.BL_FILIAL_RESPONSAVEL = 'N' THEN C.ID_FILIAL_ATENDE_OPERACIONAL ELSE F2.ID_FILIAL END)"		    
						 +"		  AND F_.ID_FILIAL = P_.ID_PESSOA)", "UNIDADE_RESPONSAVEL_NOVA");
		
		sql.addProjection("(SELECT F_.SG_FILIAL || ' - ' || P_.NM_FANTASIA"
						 +"	FROM  FILIAL F_"
						 +"		 ,PESSOA P_"
						 +"	WHERE F_.ID_FILIAL = (CASE WHEN HTFC.BL_FILIAL_COBRANCA = 'N' THEN C.ID_FILIAL_COBRANCA ELSE F.ID_FILIAL END)"		    
						 +"		  AND F_.ID_FILIAL = P_.ID_PESSOA)", "UNIDADE_COBRANCA_ANTIGA");
		
		sql.addProjection("(SELECT F_.SG_FILIAL || ' - ' || P_.NM_FANTASIA"
						 +"	FROM  FILIAL F_"
						 +"	      ,PESSOA P_"
						 +"	WHERE F_.ID_FILIAL = (CASE WHEN HTFC.BL_FILIAL_COBRANCA = 'N' THEN C.ID_FILIAL_COBRANCA ELSE F2.ID_FILIAL END)"		    
						 +"       AND F_.ID_FILIAL = P_.ID_PESSOA)", "UNIDADE_COBRANCA_NOVA");
		
		sql.addProjection("MF2.DT_VIGENCIA_INICIAL");
		sql.addProjection("F.SG_FILIAL || ' - ' || P2.NM_FANTASIA", "FILIAL_ATUAL");
		sql.addProjection("F2.SG_FILIAL || ' - ' || P3.NM_FANTASIA", "FILIAL_NOVA");
		sql.addProjection("M.NM_MUNICIPIO");
		
		sql.addFrom("HISTORICO_TROCA_FILIAL", "HTF");
		sql.addFrom("HIST_TROCA_FILIAL_CLIENTE", "HTFC");
		sql.addFrom("CLIENTE", "C");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("PESSOA", "P2");
		sql.addFrom("PESSOA", "P3");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("FILIAL", "F");
		sql.addFrom("MUNICIPIO_FILIAL", "MF");
		sql.addFrom("FILIAL", "F2");
		sql.addFrom("MUNICIPIO_FILIAL", "MF2");
		
		sql.addJoin("HTF.ID_HISTORICO_TROCA_FILIAL", "HTFC.ID_HISTORICO_TROCA_FILIAL");
		sql.addJoin("HTFC.ID_CLIENTE", "C.ID_CLIENTE (+)");
		sql.addJoin("C.ID_CLIENTE", "P.ID_PESSOA (+)");
		sql.addJoin("HTF.ID_MUNICIPIO_FILIAL", "MF.ID_MUNICIPIO_FILIAL");
		sql.addJoin("F.ID_FILIAL", "MF.ID_FILIAL");
		sql.addJoin("F.ID_FILIAL", "P2.ID_PESSOA");
		sql.addJoin("HTF.ID_MUNICIPIO_FILIAL_TROCA", "MF2.ID_MUNICIPIO_FILIAL");		
		sql.addJoin("F2.ID_FILIAL", "MF2.ID_FILIAL");
		sql.addJoin("F2.ID_FILIAL", "P3.ID_PESSOA");
		sql.addJoin("MF2.ID_MUNICIPIO", "M.ID_MUNICIPIO");
		
		sql.addCriteria("F2.ID_FILIAL", "=", parameters.getString("municipioFilial.filial.idFilial"));
		sql.addCriteria("MF.ID_MUNICIPIO_FILIAL", "=", parameters.getString("filialAtual.idMunicipioFilial"));
		sql.addCriteria("MF2.ID_MUNICIPIO", "=", parameters.getString("municipioFilial.municipio.idMunicipio"));
		
		sql.addOrderBy("P.NM_FANTASIA");
						
		return sql;
	}
	
}
