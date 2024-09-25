package com.mercurio.lms.carregamento.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * 
 * @spring.bean id="lms.carregamento.documentosNaoEmitidosService"
 * @spring.property name="reportName" value="com/mercurio/lms/carregamento/report/documentosNaoEmitidos.jasper" 
 *                  
 */
public class DocumentosNaoEmitidosServiceSupport extends ReportServiceSupport {
	
	private ControleCargaService controleCargaService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		SqlTemplate sql = sqlTpdocumentoCarregamento( 
				Long.parseLong(parameters.get("idControleCarga").toString()),
				(List<Long>)parameters.get("idsManifesto"));
	
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
              
        ControleCarga cc = controleCargaService.findById(Long.parseLong(parameters.get("idControleCarga").toString()));
        
        String nrControleCarga = cc.getFilialByIdFilialOrigem().getSgFilial()+" "+ cc.getNrControleCarga();
        Map parametersReport = new HashMap();
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        parametersReport.put("nrControleCarga", nrControleCarga);
        
        jr.setParameters(parametersReport);
        
        return jr;
	}
	
	private SqlTemplate sqlTpdocumentoCarregamento(Long idControleCarga, List<Long> idsManifesto) {
   		SqlTemplate sql = createSqlTemplate();
   		sql.addProjection("cc.nr_controle_carga", "cc_nr_controle_carga");
   		sql.addProjection("m.id_manifesto", "m_manifesto");
   		sql.addProjection("cc.id_motorista", "cc_id_motorista");
   		sql.addProjection("motorista.nm_pessoa", "motorista_nome");
   		sql.addProjection("cc.id_transportado", "id_transportado");
   		sql.addProjection("ds.tp_documento_servico", "ds_tp_documento_servico");
   		sql.addProjection("flo.sg_filial", "flo_sg_filial");
   		sql.addProjection("ds.nr_docto_servico", "ds_nr_docto_servico");
   		sql.addProjection("ds.dt_prev_entrega", "ds_dt_prev_entrega");
   		sql.addProjection("pc.tp_modo_pedido_coleta", "pc_tp_modo_pedido_coleta");
   		sql.addProjection("pc.ds_inf_coleta", "pc_ds_inf_coleta");
   		sql.addProjection("ds.qt_volumes", "ds_qt_volumes");
   		sql.addProjection("r.nm_pessoa", "remetente");
   		sql.addProjection("ds.ds_endereco_entrega_real", "endereco_entrega");
   		sql.addProjection("decode (pc.tp_modo_pedido_coleta, 'TE', mt.nr_frota||'/'||mt.nr_identificador , '')", "mt_frota");
   		
   		sql.addFrom("docto_servico ds " +
   				"INNER JOIN filial flo ON flo.id_filial=ds.id_filial_origem " +
   				"INNER JOIN conhecimento c ON c.id_conhecimento = ds.id_docto_servico " +
   				"INNER JOIN pedido_coleta pc ON pc.id_pedido_coleta = ds.id_pedido_coleta " +
   				"INNER JOIN pre_manifesto_documento pmd ON pmd.id_docto_servico = ds.id_docto_servico " +
   				"INNER JOIN manifesto m ON m.id_manifesto = pmd.id_manifesto " +
   				"INNER JOIN controle_carga cc ON cc.id_controle_carga = m.id_controle_carga " +
   				"INNER JOIN pessoa r ON r.id_pessoa= ds.id_cliente_remetente " +
   				"LEFT JOIN pessoa motorista ON motorista.id_pessoa= cc.id_motorista " +
   				"LEFT JOIN manifesto_coleta mc ON pc.id_manifesto_coleta = mc.id_manifesto_coleta " +
   				"LEFT JOIN meio_transporte mt ON mt.id_meio_transporte = cc.id_transportado ");
   		sql.addCriteria("cc.id_controle_carga", "=", idControleCarga);
   		sql.addCriteriaIn("m.id_manifesto", idsManifesto); 
   		sql.addCriteria("c.tp_situacao_conhecimento", "<>", "E"); 
   		return sql;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

}
