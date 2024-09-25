package com.mercurio.lms.entrega.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirMIRComprovantesService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirMIRComprovantes.jasper"
 */

public class EmitirMIRComprovantesService extends ReportServiceSupport {
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;

		SqlTemplate sql = createSqlTemplate(tfm);
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        parametersReport.put("BL_REEMISSAO",tfm.getBoolean("blReemissao"));
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        jr.setParameters(parametersReport);
        
		return jr;

	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap tfm) {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("FILIAL_MIR.sg_filial", "SG_FILIAL_MIR");
		sql.addProjection("FILIAL_MIR_DESTINO.sg_filial", "SG_FILIAL_MIR_DESTINO"); 
		sql.addProjection("M.NR_MIR", "NR_MIR");
		sql.addProjection("m.dh_emissao", "DH_EMISSAO");
		sql.addProjection("m.id_usuario_criacao", "id_usuario_criacao");
		sql.addProjection("usuario_origem.NM_USUARIO", "NM_USUARIO_ORIGEM");
		sql.addProjection("usuario_destino.NM_USUARIO", "NM_USUARIO_DESTINO");
		
		sql.addProjection("pRemetente.nr_identificacao", "NR_IDENTIFICACAO_REMETENTE");
		sql.addProjection("pRemetente.TP_identificacao", "TP_IDENTIFICACAO_REMETENTE");
		sql.addProjection("pRemetente.TP_identificacao", "TP_IDENT_REMET_DESC");
		sql.addProjection("pRemetente.NM_PESSOA", "NM_PESSOA_REMETENTE");
		sql.addProjection("pRemetente.ID_PESSOA", "ID_PESSOA_REMETENTE");
		sql.addProjection("DOCTO_SERVICO.id_filial_origem", "ID_FILIAL_ORIGEM_DOCTO_SERVICO");
		sql.addProjection("FILIAL_DOCTO_SERVICO.SG_FILIAL", "SG_FILIAL_DOCTO_SERVICO");
		sql.addProjection("DOCTO_SERVICO.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO_SERVICO");
		sql.addProjection("DOCTO_SERVICO.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
		
		sql.addProjection("pDoctoServicoDest.nm_pessoa", "nm_pessoa_destinatario");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("TDE.DS_TIPO_DOCUMENTO_ENTREGA_I"), "COMPROVANTE");
		sql.addProjection("RDE.OB_COMPROVANTE", "OB_COMPROVANTE");
		sql.addProjection("m.tp_documento_mir", "TP_DOCUMENTO_MIR"); 
		

		sql.addFrom("tipo_documento_entrega","TDE");		// Tipo Documento entrega
		sql.addFrom("filial","FILIAL_MIR"); 				// filial Mir 
		sql.addFrom("filial","FILIAL_MIR_DESTINO"); 		// filial Destino Mir
		sql.addFrom("mir","M");								// Mir
		sql.addFrom("usuario","usuario_destino"); 	
		sql.addFrom("usuario","usuario_origem"); 	
		sql.addFrom("PESSOA","pRemetente"); 		
		sql.addFrom("docto_servico","DOCTO_SERVICO");
		sql.addFrom("pessoa","pDoctoServicoDest"); 
		sql.addFrom("registro_documento_entrega","RDE");	// documento de serviço
		sql.addFrom("FILIAL","FILIAL_DOCTO_SERVICO"); 		// Documento de serviço
		sql.addFrom("documento_mir","dm"); 					// Documento de serviço

		sql.addJoin("M.id_usuario_recebimento", "usuario_destino.id_usuario (+)");
		sql.addJoin("M.id_filial_origem", "filial_mir.id_filial");
		sql.addJoin("M.id_filial_destino", "filial_mir_destino.id_filial");
		sql.addJoin("RDE.ID_DOCTO_SERVICO", "DOCTO_SERVICO.ID_DOCTO_SERVICO");
		sql.addJoin("M.id_usuario_criacao", "usuario_origem.id_usuario"); 
		sql.addJoin("DOCTO_SERVICO.id_cliente_remetente", "pRemetente.id_PESSOA");
		sql.addJoin("DM.ID_REGISTRO_DOCUMENTO_ENTREGA", "RDE.ID_REGISTRO_DOCUMENTO_ENTREGA ");

		sql.addJoin("DOCTO_SERVICO.ID_FILIAL_ORIGEM", "FILIAL_DOCTO_SERVICO.ID_FILIAL");
		sql.addJoin("DOCTO_SERVICO.ID_CLIENTE_DESTINATARIO", "pDoctoServicoDest.ID_PESSOA (+)");
		sql.addJoin("rde.id_tipo_documento_entrega", "tde.id_tipo_documento_entrega (+)");
		sql.addJoin("M.ID_MIR", "dm.ID_MIR");
		sql.addCriteria("m.id_mir", "=", tfm.getLong("idMir"), Long.class);
		                 
		sql.addOrderBy("PREMETENTE.NM_PESSOA");
		sql.addOrderBy("DOCTO_SERVICO.ID_FILIAL_ORIGEM");
		sql.addOrderBy("FILIAL_DOCTO_SERVICO.SG_FILIAL");
		sql.addOrderBy("DOCTO_SERVICO.NR_DOCTO_SERVICO");
		
		return sql;
	}

	public void configReportDomains(ReportDomainConfig config) {
        config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
        config.configDomainField("TP_IDENT_REMET_DESC", "DM_TIPO_IDENTIFICACAO");
        config.configDomainField("TP_DOCUMENTO_MIR", "DM_TIPO_DOCUMENTO_MIR");
    }

}
