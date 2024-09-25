package com.mercurio.lms.entrega.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;

/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirMIRReembolsosService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirMIRReembolsos.jasper"
 */
public class EmitirMIRReembolsosService extends ReportServiceSupport {

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
		
		sql.addProjection("m.id_filial_origem", "id_filial_origem"); 
		sql.addProjection("fo.sg_filial", "ds_filial_origem");
		sql.addProjection("fd.sg_filial", "ds_filial_destino");
		sql.addProjection("m.nr_mir", "nr_mir");
		sql.addProjection("m.dh_emissao", "dh_emissao");
		sql.addProjection("m.id_usuario_criacao", "id_usuario_criacao");
		sql.addProjection("uo.nm_usuario", "nm_usuario_origem");
		sql.addProjection("m.id_usuario_recebimento", "id_usuario_recebimento");
		sql.addProjection("ud.nm_usuario", "nm_usuario_recebimento");
		sql.addProjection("rr.id_docto_serv_reembolsado", "ID_DOCTO_SERV_REEMBOLSADO");
		sql.addProjection("ds.id_filial_origem", "ID_FILIAL_ORIGEM_DOCTO_SERVICO");
		sql.addProjection("fo.sg_filial", "SG_FILIAL_ORIGEM_DOCTO_SERVICO");
		sql.addProjection("rr.nr_recibo_reembolso", "NR_DOCTO_SERVICO");
		sql.addProjection("rr.vl_reembolso", "vl_reembolso");
		sql.addProjection("rr.vl_aplicado", "vl_aplicado");
		sql.addProjection("rr.tp_valor_atribuido_recibo", "tp_valor_atribuido_recibo");
		sql.addProjection("moeda.ds_simbolo", "DS_SIMBOLO");
		sql.addProjection("ds.id_cliente_remetente", "id_cliente_remetente");
		sql.addProjection("po.nm_pessoa", "NM_PESSOA_REMETENTE");
		sql.addProjection("ds.id_cliente_destinatario", "id_cliente_destinatario");
		sql.addProjection("pd.nm_pessoa", "NM_PESSOA_DESTINATARIO");

		sql.addProjection("cher.nr_banco", "nr_banco");
		sql.addProjection("cher.nr_agencia", "nr_agencia");
		sql.addProjection("cher.nr_cheque", "nr_cheque");
		sql.addProjection("cher.vl_cheque", "VL_CHEQUE_VAL");
		sql.addProjection("cher.dt_cheque", "dt_cheque");
		sql.addProjection("m.tp_documento_mir", "TP_DOCUMENTO_MIR"); 
		
		sql.addFrom("mir","m");
		sql.addFrom("filial","fo"); 			// filial origem 
		sql.addFrom("filial","fd"); 			// filial destino
		sql.addFrom("documento_mir","dm"); 		// documento_mir
		sql.addFrom("recibo_reembolso","rr");	// recibo_reembolso
		sql.addFrom("docto_servico","ds");
		sql.addFrom("cliente","cr"); 			// cliente_remetente
		sql.addFrom("cliente","cd"); 			// cliente_destinatario
		sql.addFrom("pessoa","po"); 			// pessoa origem/remetente cabecalho
		sql.addFrom("pessoa","pd"); 			// pessoa destino cabecalho
		sql.addFrom("moeda","moeda"); 			// pessoa do usuario destino
		
		sql.addFrom("usuario","ud"); 			// Usuario Destino
		sql.addFrom("usuario","uo"); 			// Usuario origem
		sql.addFrom("cheque_reembolso","cher"); // cheque reembolso.
		
		sql.addJoin("m.id_mir","dm.id_mir");
		sql.addJoin("dm.id_recibo_reembolso","rr.id_recibo_reembolso");
		sql.addJoin("rr.id_recibo_reembolso","ds.id_docto_servico");
		sql.addJoin("ds.id_cliente_remetente","cr.id_cliente");
		sql.addJoin("cr.id_cliente","po.id_pessoa");
		sql.addJoin("ds.id_cliente_destinatario","cd.id_cliente (+)");
		sql.addJoin("cd.id_cliente","pd.id_pessoa (+)");
		sql.addJoin("m.id_usuario_criacao","uo.id_usuario"); 
		sql.addJoin("m.id_usuario_recebimento","ud.id_usuario (+)");
		sql.addJoin("m.id_filial_origem","fo.id_filial");
		sql.addJoin("m.id_filial_destino","fd.id_filial");
		sql.addJoin("cher.id_recibo_reembolso (+)","dm.id_recibo_reembolso");
		sql.addJoin("ds.id_moeda","moeda.id_moeda");

		sql.addCriteria("m.id_mir", "=", tfm.getLong("idMir"), Long.class);

		sql.addOrderBy("ds.id_filial_origem");
		sql.addOrderBy("fo.sg_filial");
		sql.addOrderBy("rr.nr_recibo_reembolso");
		return sql;
	}

	/**
	 * Configura Dominios
	 */
	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_DOCUMENTO_MIR", "DM_TIPO_DOCUMENTO_MIR");
	}	
}
