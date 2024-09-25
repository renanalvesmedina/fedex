package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings
 *
 * @spring.bean id="lms.expedicao.cargaAereaService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirRelacaoCargaAerea.jasper"
 */
public class CargaAereaService extends ReportServiceSupport {
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		// Pega o numero da relacao de carga da sessao e passa como parêmetro para o relatorio
		Long nrRelCarga = (Long)SessionContext.get("NR_RELACAO_CARGA_IN_SESSION");
		String nrRelacaoCarga = (String)parameters.get("sgFilial") + " " + FormatUtils.formatLongWithZeros(nrRelCarga,"000000");  
		parametersReport.put("nrRelacaoCarga", nrRelacaoCarga);

		// Passa o nome da cia aerea como parâmetro
		String nmCiaAerea = (String)parameters.get("ciaFilialMercurio.filial.pessoa.nmFantasia") + "-" + (String)parameters.get("empresa.pessoa.nmPessoa");
		parametersReport.put("nmCiaAerea", nmCiaAerea);
		
		// Passa o nome da cia aerea como parâmetro
		String observacao = (String)parameters.get("observacao");
		parametersReport.put("observacao", observacao);
		
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);

		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		sql.addProjection("awb.ds_serie", "ds_serie");
		sql.addProjection("awb.nr_awb", "nr_awb");
		sql.addProjection("awb.dv_awb", "dv_awb");
		sql.addProjection("emp.sg_empresa", "sg_empresa");
		sql.addProjection("a.sg_aeroporto", "sg_aeroporto");
		sql.addProjection("awb.ds_voo_previsto", "ds_voo_previsto");
		sql.addProjection("awb.qt_volumes", "qt_volumes");
		sql.addProjection("awb.ps_total", "ps_total");
		sql.addProjection("p.nm_Fantasia", "nm_Fantasia");
		sql.addProjection("p.nm_Pessoa", "nm_Pessoa");
		sql.addProjection("f.sg_filial", "sg_Filial");

		sql.addFrom("awb", "awb");
		sql.addFrom("cia_filial_mercurio", "cfm");
		sql.addFrom("empresa", "emp");
		sql.addFrom("pessoa", "p");
		sql.addFrom("filial", "f");
		sql.addFrom("aeroporto", "a");

		Long idFilialOrigem = SessionUtils.getFilialSessao().getIdFilial();
		sql.addCriteria("awb.id_filial_origem", "=", idFilialOrigem);

		Long idCiaFilialMercurio = parameters.getLong("ciaFilialMercurio.idCiaFilialMercurio");
		if(idCiaFilialMercurio != null) {
			sql.addCriteria("awb.id_cia_filial_mercurio", "=", idCiaFilialMercurio);
			String dsFilial = parameters.getString("ciaFilialMercurio.filial.pessoa.nmFantasia");
			sql.addFilterSummary("ciaAerea", dsFilial);
		}
		
		
		String in = "";
		List awbs = (List)SessionContext.get(ConstantesExpedicao.AWB_IN_SESSION);
		for (Iterator it = awbs.iterator(); it.hasNext();) {
			TypedFlatMap awb = (TypedFlatMap) it.next();
			Long idAwb = awb.getLong("idAwb");
			in += idAwb.toString() + ",";
		}
		if (in.length()>0){
			in = in.substring(0, in.length()-1);
			sql.addCustomCriteria("awb.id_awb in (" + in + ")");
		}

		sql.addJoin("awb.id_cia_filial_mercurio", "cfm.id_cia_filial_mercurio");
		sql.addJoin("f.id_filial", "awb.id_filial_origem");
		sql.addJoin("cfm.id_empresa", "p.id_pessoa");
		sql.addJoin("awb.id_aeroporto_destino", "a.id_aeroporto");
		sql.addJoin("cfm.id_empresa", "emp.id_empresa");
		
		sql.addOrderBy("awb.ds_serie");
		sql.addOrderBy("awb.nr_awb");
		sql.addOrderBy("awb.dv_awb");
		sql.addOrderBy("a.sg_aeroporto");

		return sql;
	}
	
	/** 
	 *  Este metodo será executado apos a execução do relatório.
	 *  @param JdbcTemplate que será injetado pelo ReportServiceSupport.
	 *  @return void
	 **/
	@Override
	public void postExecute(JdbcTemplate jdbcTemplate, Map parameters){
		SessionContext.remove(ConstantesExpedicao.AWB_IN_SESSION);
	}	

}
