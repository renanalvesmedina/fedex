package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Emissão de Rota Coleta/Entrega
 * 
 * @author 
 * 
 * @spring.bean id="lms.municipios.emitirRotaColetaEntregaService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirRotaColetaEntrega.jasper"
 */
public class EmitirRotaColetaEntregaService extends ReportServiceSupport{
	
	private FilialService filialService;

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		 SqlTemplate sql = getSql(parameters);
	     JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
	     
	     Map parametersReport = new HashMap();
		 parametersReport.put("PARAMETROS_PESQUISA",  getFiltros(parameters));
         parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
         parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		 jr.setParameters(parametersReport);
		return jr;
	}

	 private SqlTemplate getSql(Map parameters) {
		  
		 /**
SELECT FS.DS_SIGLALMS AS FILIAL,
       FS.DS_DESCRICAOSIGLA AS DS_FILIAL,
       RE.NR_ROTA AS NR_ROTA,
       RE.DS_ROTA AS DS_ROTA, 
       RE.NR_KM AS NR_KM,
       TO_CHAR(RE.DT_VIGENCIA_INICIAL,'DD/MM/YYYY') AS DT_VIG_INICIAL,
       TO_CHAR(RE.DT_VIGENCIA_FINAL,'DD/MM/YYYY') AS DT_VIG_FINAL,
       RI.NR_ORDEM_OPERACAO AS ORDEM_OPERACAO,
       RI.NR_CEP_INICIAL AS CEP_INICIAL,
       RI.NR_CEP_FINAL AS CEP_FINAL,
       M.NM_MUNICIPIO AS MUNICIPIO
  FROM ROTA_COLETA_ENTREGA RE
 INNER JOIN FILIAL_SIGLA FS
    ON FS.ID_FILIAL_LMS          = RE.ID_FILIAL
 INNER JOIN ROTA_INTERVALO_CEP RI
    ON RI.ID_ROTA_COLETA_ENTREGA = RE.ID_ROTA_COLETA_ENTREGA
 INNER JOIN MUNICIPIO M
    ON M.ID_MUNICIPIO            = RI.ID_MUNICIPIO
	
-- WHERE FS.DS_SIGLALMS = 'MTZ' AND RE.NR_ROTA = 1 AND (RE.DT_VIGENCIA_FINAL    > TRUNC(SYSDATE) AND 'S' = 'S')
 WHERE FS.DS_SIGLALMS            = ?   
   AND RE.NR_ROTA                = ?
   AND RE.DT_VIGENCIA_INICIAL    <= TRUNC(SYSDATE) -- VIGENTE - novo
   AND RE.DT_VIGENCIA_FINAL     >= TRUNC(SYSDATE) -– NAO VIGENTE - novo
   
--   AND RE.DT_VIGENCIA_INICIAL   >= ?
--   AND RE.DT_VIGENCIA_FINAL     <= ? -– APENAS SE FOR INFORMADO FINAL
--   AND ((RE.DT_VIGENCIA_FINAL    > TRUNC(SYSDATE) AND ? = 'S') -- VIGENTE
--   OR (RE.DT_VIGENCIA_FINAL     <= TRUNC(SYSDATE) AND ? = 'N')) -– NAO VIGENTE

   

ORDER BY RE.NR_ROTA,
      RE.NR_ROTA,
      RE.DT_VIGENCIA_INICIAL,
      RI.NR_ORDEM_OPERACAO
  */
	    	SqlTemplate sql = createSqlTemplate();
	    	
	    	//projection...
	    	sql.addProjection("FS.DS_SIGLALMS" , "FILIAL");
	    	sql.addProjection("FS.DS_DESCRICAOSIGLA", "DS_FILIAL");
	    	sql.addProjection("RE.NR_ROTA", "NR_ROTA");
	    	sql.addProjection("RE.DS_ROTA", "DS_ROTA");
	    	sql.addProjection("RE.NR_KM", "NR_KM");
	    	sql.addProjection("TO_CHAR(RE.DT_VIGENCIA_INICIAL,'DD/MM/YYYY')", "DT_VIG_INICIAL");
	    	sql.addProjection("CASE TO_CHAR(RE.DT_VIGENCIA_FINAL,'DD/MM/YYYY') WHEN '01/01/4000' THEN '' ELSE TO_CHAR(RE.DT_VIGENCIA_FINAL,'DD/MM/YYYY') END", "DT_VIG_FINAL");
	    	sql.addProjection("RI.NR_ORDEM_OPERACAO", "NR_ORDEM_OPERACAO");
	    	sql.addProjection("RI.NR_CEP_INICIAL", "NR_CEP_INICIAL");
	    	sql.addProjection("RI.NR_CEP_FINAL", "NR_CEP_FINAL");
	    	sql.addProjection("M.NM_MUNICIPIO", "NM_MUNICIPIO");

			
	    	//from...
	    	sql.addFrom("ROTA_COLETA_ENTREGA", "RE");
	    	sql.addFrom("FILIAL_SIGLA", "FS");
	    	sql.addFrom("ROTA_INTERVALO_CEP", "RI");
	    	sql.addFrom("MUNICIPIO", "M");
	    	 
	    	
	    	//join...
	    	sql.addJoin("FS.ID_FILIAL_LMS", "RE.ID_FILIAL");
	    	sql.addJoin("RI.ID_ROTA_COLETA_ENTREGA", "RE.ID_ROTA_COLETA_ENTREGA");
	    	sql.addJoin("M.ID_MUNICIPIO", "RI.ID_MUNICIPIO");
	    	
	    	//Criteria...	    	
	    	
	    	Long idFilial = MapUtils.getLong(parameters,"idFilial");
	    	if(idFilial!=null){
	    		sql.addCriteria("FS.ID_FILIAL_LMS", "=", idFilial);	    		
	    		
	    		sql.addFilterSummary("filial.idFilial",  idFilial);
	    	}
	    	
	    	Long numeroRota = MapUtils.getLong(parameters,"numeroRota");
	    	if(numeroRota!=null){
	    		sql.addCriteria("RE.NR_ROTA", "=", numeroRota);
	    		sql.addFilterSummary("numeroRota",  numeroRota);
	    	}
	    	
	    	final String DH_VIGENCIA_INICIAL = "RE.DT_VIGENCIA_INICIAL <= TRUNC(SYSDATE)";
	    	final String DH_VIGENCIA_FINAL = "RE.DT_VIGENCIA_FINAL >= TRUNC(SYSDATE)";
	    	
	    	sql.addCustomCriteria(DH_VIGENCIA_INICIAL);
	    	sql.addCustomCriteria(DH_VIGENCIA_FINAL);
	    	
	    	sql.addOrderBy("FS.DS_DESCRICAOSIGLA");
	        sql.addOrderBy("RE.NR_ROTA");
	        sql.addOrderBy("M.NM_MUNICIPIO");
	        sql.addOrderBy("RI.NR_ORDEM_OPERACAO"); 

	        return sql;         
	    }
	 
	private String getFiltros(Map parameters){
		
		Map resultado = new HashMap();
		
		Long idFilial = MapUtils.getLong(parameters,"idFilial");
		Long numeroRota = MapUtils.getLong(parameters,"numeroRota");
		 
		Filial filial = this.getFilialService().findById(idFilial);
		
		StringBuffer sb =new StringBuffer();
		sb.append("Filial: " + filial.getSgFilial() + " - " + filial.getPessoa().getNmPessoa());
		
		return sb.toString();
	}
	
	
}