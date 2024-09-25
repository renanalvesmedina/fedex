package com.mercurio.lms.gm.report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.RotaEmbarque;
import com.mercurio.lms.gm.model.service.RotaEmbarqueService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório gerencial de embarques in house - GM
 * 
 * 
 * Demanda LMS-2795
 * 
 * @spring.bean id="lms.gm.relatorioGerencialEmbarquesInHouseService"
 * @spring.property name="reportName" value= "com/mercurio/lms/gm/report/relatorioGerencialEmbarquesInHouse.jasper"
 */
public class RelatorioGerencialEmbarquesInHouseService extends ReportServiceSupport {
	private RotaEmbarqueService rotaEmbarqueService;

	/**
	 * Método responsável por gerar o relatório. Passando os parametros necessários para 
	 * as informações da parte superior do relatório e a query que monta a tabela com os 
	 * totais de cada rota master.
	 * 
	 * Demanda LMS-2795
	 */
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		JRReportDataObject jr = null;

		Long idRotaEmbarque = parameters.get("idRotaEmbarque") != null ? (Long) parameters.get("idRotaEmbarque") : null;

		SimpleDateFormat format1, format2;
		format1 = new SimpleDateFormat("yyyy-MM-dd");
		format2 = new SimpleDateFormat("dd-MM-yyyy");
		
		Date dtInicio = format1.parse(parameters.get("dtInicio").toString());
		Date dtFim = format1.parse(parameters.get("dtFim").toString());

		String strDtInicio = format2.format(dtInicio);
		String strDtFim = format2.format(dtFim);
		
		SqlTemplate sql = getSql(idRotaEmbarque, strDtInicio, strDtFim);

		jr = executeQuery(sql.toString(), new Object[] {});

		Map parametersReport = new HashMap();
		String sb = null;
		if (idRotaEmbarque == null) {
			sb = buildSiglaNomeRotas();
		} else {
			sb = rotaEmbarqueService.findById(idRotaEmbarque).getSiglaNomeRota();
		}
		

		parametersReport.put("rotaMaster", sb);
		parametersReport.put("dtInicioPeriodo", strDtInicio);
		parametersReport.put("dtFimPeriodo", strDtFim);
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));

		jr.setParameters(parametersReport);

		return jr;
	}
	
	private String buildSiglaNomeRotas() {
		StringBuilder sb = new StringBuilder();
		List<RotaEmbarque> rotas = rotaEmbarqueService.find(null);
		for (RotaEmbarque rotaEmbarque : rotas) {
			sb.append(rotaEmbarque.getSiglaNomeRota()).append(", ");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		
		return sb.toString();
	}

	/**
	 * Método responsável por gerar a query do relatório.
	 * 
	 * @param siglaRota
	 * @param dtInicio
	 * @param dtFim
	 * @return
	 */
	private SqlTemplate getSql(Long idRotaEmbarque, String dtInicio, String dtFim) {
		
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("re.sigla_rota as sigla_rota,");

		sql.add("(SELECT COUNT(DISTINCT vo.id_cabecalho_carregamento)");
		sql.add("   FROM volume vo,");
		sql.add("        carregamento ca");
		sql.add("  WHERE vo.id_carregamento = ca.id_carregamento");
		sql.add("    AND ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status IN (3,4)");
		sql.add("    AND vo.codigo_status = '6') AS qtde_mpc_embarcados,");

		sql.add("(SELECT SUM(DISTINCT tc.total_volumes)");
		sql.add("   FROM total_carregamento tc,");
		sql.add("        volume vo,");
		sql.add("        carregamento ca");
		sql.add("  WHERE tc.id_cabecalho_carregamento = vo.id_cabecalho_carregamento");
		sql.add("    AND vo.id_carregamento = ca.id_carregamento");
		sql.add("    AND ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status IN (3,4)) AS qtde_volumes_solicitados,");

		sql.add("(SELECT COUNT(vo.id_volume)");
		sql.add("   FROM volume vo,");
		sql.add("        carregamento ca");
		sql.add("  WHERE vo.id_carregamento = ca.id_carregamento");
		sql.add("    AND ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status IN (3,4)");
		sql.add("    AND vo.codigo_status = '6') AS qtde_volumes_embarcados,");

		sql.add("(SELECT COUNT(ca.id_carregamento)");
		sql.add("   FROM carregamento ca");
		sql.add("  WHERE ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status = 3) AS qtde_embarques_ok,");

		sql.add("(SELECT COUNT(ca.id_carregamento)");
		sql.add("   FROM carregamento ca");
		sql.add("  WHERE ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status = 4) AS qtde_embarques_nok,");

		sql.add("(SELECT COUNT(DISTINCT ca.placa_veiculo)");
		sql.add("   FROM carregamento ca");
		sql.add("  WHERE ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status IN (3,4)) AS qtde_veiculos,");

		sql.add("(SELECT COUNT(DISTINCT ca.id_carregamento)");
		sql.add("   FROM carregamento ca");
		sql.add("  WHERE ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status IN (3,4)");
		sql.add("    AND ca.tipo_carregamento = 'D') AS qtde_embarques_diretos,");

		sql.add("(SELECT COUNT(distinct ca.id_carregamento)");
		sql.add("   FROM carregamento ca");
		sql.add("  WHERE ca.rota_carregamento = re.sigla_rota");
		sql.add("    AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("    AND ca.codigo_status IN (3,4)");
		sql.add("    AND ca.tipo_carregamento = 'N') AS qtde_embarques_normais");

		sql.add(" FROM rota_embarque re");

		// Criteria
		sql.add(" WHERE EXISTS (SELECT re.sigla_rota");
		sql.add("           	  FROM carregamento ca");
		sql.add("          		 WHERE ca.rota_carregamento = re.sigla_rota");
		sql.add("    		 	   AND trunc(ca.data_fim) between TO_DATE('" + dtInicio + "','dd/mm/YYYY') AND TO_DATE('" + dtFim + "','dd/mm/YYYY')");
		sql.add("            	   AND ca.codigo_status IN (3,4))");

		if (idRotaEmbarque != null) {
			sql.add(" AND re.id_rota_embarque = " + idRotaEmbarque);
		}
		sql.addOrderBy("re.sigla_rota");

		return sql;
	}

	public RotaEmbarqueService getRotaEmbarqueService() {
		return rotaEmbarqueService;
	}

	public void setRotaEmbarqueService(RotaEmbarqueService rotaEmbarqueService) {
		this.rotaEmbarqueService = rotaEmbarqueService;
	}
	
	
}
