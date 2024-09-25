package com.mercurio.lms.carregamento.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.carregamento.volumesFimCarregamentoService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/volumesFimCarregamento.jasper" 
 *                  
 */
public class VolumesFimCarregamentoService extends ReportServiceSupport {
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		SqlTemplate sql = sqlTpdocumentoCarregamento(parameters.get("tpDocumentoCarregamento").toString(), Long.parseLong(parameters.get("idControleCarga").toString()));
		
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("titulo", parameters.get("tpDocumentoCarregamento").toString());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        
        return jr;
	}
	
	
	/**
	 * Valida o tipo de busca a ser realizada dado pelo tipo de chamada.
	 */
	private SqlTemplate sqlTpdocumentoCarregamento(String tpDocCarreg, Long idControleCarga) {
		
		if(tpDocCarreg.equals("carregadosSemPreManifestoDocumento")){
			return sqlCarregadosSemPreManifestoDocumento(idControleCarga);
		}else if(tpDocCarreg.equals("carregadosIncompletos")){
			return sqlCarregadosIncompletos(idControleCarga);
		}else{
			return null;
		}

	}
	
	
	/**
	 * Monta o sql que deverá retornar todos os conhecimentos carregados e sem pre-manifesto
	 * ET 05.01.02.04 - CQ25050 - Todos os volumes vinculados a pre-manifestos do controle de carga e que nao possuam 
	 * conhecimentos vinculados a pre-manifestos do controle de carga.
	 * @param idControleCarga 
	 * @return
	 */
	public SqlTemplate sqlCarregadosSemPreManifestoDocumento(Long idControleCarga) {
		
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("NFC.NR_NOTA_FISCAL", "NR_NOTA_FISCAL");
		sql.addProjection("CON.NR_CONHECIMENTO", "NR_CONHECIMENTO");
		sql.addProjection("MAN.NR_PRE_MANIFESTO", "NR_PRE_MANIFESTO");
		sql.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
		sql.addProjection("FL.SG_FILIAL", "FILIAL_CC");
		
		sql.addFrom("CONHECIMENTO CON "+
					"JOIN DOCTO_SERVICO DO "+
					"ON DO.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO "+
					"LEFT OUTER JOIN PRE_MANIFESTO_VOLUME PMV "+
					"ON DO.ID_DOCTO_SERVICO = PMV.ID_DOCTO_SERVICO "+
					"LEFT OUTER JOIN MANIFESTO MAN "+
					"ON PMV.ID_MANIFESTO = MAN.ID_MANIFESTO "+
					"LEFT OUTER JOIN CONTROLE_CARGA CC "+
					"ON MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA "+
					"JOIN VOLUME_NOTA_FISCAL VNF "+
				    "ON VNF.ID_VOLUME_NOTA_FISCAL = PMV.ID_VOLUME_NOTA_FISCAL "+
				    "JOIN NOTA_FISCAL_CONHECIMENTO NFC "+
				    "ON NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO " +
				    "JOIN FILIAL FL "+
				    "ON FL.ID_FILIAL = CC.ID_FILIAL_ATUALIZA_STATUS");
		
		sql.addCriteria("CC.ID_CONTROLE_CARGA", "=",idControleCarga);
		sql.addCustomCriteria("PMV.ID_PRE_MANIFESTO_VOLUME IS NULL");
		
		return sql;
	}
	


	
	/**
	 * Monta o sql que deverá retornar todos os conhecimentos carregados e incompletos
	 * ET 05.01.02.04 - CQ25050 - Conhecimentos que dentre todos os volumes que possui na filial do carregamento, 
	 * ainda existam alguns que nao foram embarcados no carregamento.
	 * @param idControleCarga 
	 * @return
	 */
	public SqlTemplate sqlCarregadosIncompletos(Long idControleCarga) {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("NFC.NR_NOTA_FISCAL", "NR_NOTA_FISCAL");
		sql.addProjection("CON.NR_CONHECIMENTO", "NR_CONHECIMENTO");
		sql.addProjection("MAN.NR_PRE_MANIFESTO", "NR_PRE_MANIFESTO");
		sql.addProjection("CC.NR_CONTROLE_CARGA", "NR_CONTROLE_CARGA");
		sql.addProjection("FL.SG_FILIAL", "FILIAL_CC");
		
		sql.addFrom("CONHECIMENTO CON "+
					"JOIN DOCTO_SERVICO DO "+
					"ON DO.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO "+
					"JOIN PRE_MANIFESTO_VOLUME PMV "+
					"ON DO.ID_DOCTO_SERVICO = PMV.ID_DOCTO_SERVICO "+
					"JOIN MANIFESTO MAN "+
					"ON PMV.ID_MANIFESTO = MAN.ID_MANIFESTO "+
					"JOIN CONTROLE_CARGA CC "+
					"ON MAN.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA "+
					"JOIN VOLUME_NOTA_FISCAL VNF "+
				    "ON VNF.ID_VOLUME_NOTA_FISCAL = PMV.ID_VOLUME_NOTA_FISCAL "+
				    "JOIN NOTA_FISCAL_CONHECIMENTO NFC "+
				    "ON NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO " +
				    "JOIN FILIAL FL "+
				    "ON FL.ID_FILIAL = CC.ID_FILIAL_ATUALIZA_STATUS ");
		
		
		sql.addCustomCriteria("(	SELECT COUNT(*) "+
							  "     FROM PRE_MANIFESTO_VOLUME IPMV "+
							  "     JOIN MANIFESTO M "+
							  "     ON M.ID_MANIFESTO = IPMV.ID_MANIFESTO "+
							  "     WHERE IPMV.ID_DOCTO_SERVICO = DO.ID_DOCTO_SERVICO "+
							  "		AND M.ID_CONTROLE_CARGA = MAN.ID_CONTROLE_CARGA )"+
							  "		BETWEEN 1 AND DO.QT_VOLUMES-1 ");
		sql.addCriteria("MAN.ID_CONTROLE_CARGA", "=",idControleCarga);
		
		return sql;
	}
	
	
}
