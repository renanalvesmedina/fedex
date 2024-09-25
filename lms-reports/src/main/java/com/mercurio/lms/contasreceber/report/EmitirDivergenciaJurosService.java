package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.contasreceber.emitirDivergenciaJurosService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirDivergenciaJuros.jasper"
 */
public class EmitirDivergenciaJurosService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {

		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = null;

		/** Seleciona qual relatório a ser impresso, através do checkbox */
		if ( tfm.getBoolean("soTotais").equals(Boolean.TRUE)) {
			this.setReportName("com/mercurio/lms/contasreceber/report/emitirDivergenciaJurosTotais.jasper");
			sql = getSqlTemplateTotais(tfm);
		} else {
			this.setReportName("com/mercurio/lms/contasreceber/report/emitirDivergenciaJuros.jasper");
			sql = getSqlTemplateAnalitico(tfm);
		}
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,tfm.getString("tpFormatoRelatorio"));		
		
		jr.setParameters(parametersReport);
		return jr;
	}
	
	// Projection e ordenação do relatório sem a opção "Só totais"
	private SqlTemplate getSqlTemplateAnalitico(TypedFlatMap tfm) throws Exception{
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("f.nr_fatura", "FATURA");
		sql.addProjection("filfat.sg_filial", "FILIAL_FATURA");
		sql.addProjection("pc.nm_pessoa", "CLIENTE");
		sql.addProjection("pc.tp_identificacao", "TP_IDENTIFICACAO");
		sql.addProjection("pc.nr_identificacao", "NR_IDENTIFICACAO");
		sql.addProjection("f.vl_juro_calculado", "JUROS_CALCULADO");

		StringBuffer sb = new StringBuffer();
		
		sb.append("NVL((SELECT INDN.VL_JURO_RECEBER ");
		sb.append("FROM ITEM_NOTA_DEBITO_NACIONAL INDN, ");
		sb.append("NOTA_DEBITO_NACIONAL NDN ");
		sb.append("WHERE NDN.ID_NOTA_DEBITO_NACIONAL = INDN.ID_NOTA_DEBITO_NACIONAL ");
		sb.append("AND NDN.TP_SITUACAO_NOTA_DEBITO_NAC NOT IN ('C', 'I') ");
		sb.append("AND INDN.ID_FATURA = f.id_fatura ), 0)");		

		sql.addProjection("(ir.vl_juros + "+ sb.toString() + ")", "JUROS_COBRADO");
		sql.addProjection("(ir.vl_juros - f.vl_juro_calculado + " + sb.toString() + ")", "DIFERENCA_JUROS");
		sql.addProjection("(filfat.sg_filial || ' - ' || pfilfat.nm_fantasia)", "FILIAL_COBRANCA");
		
		getSqlTemplateGeral(tfm, sql);
		
		sql.addOrderBy("filfat.sg_filial, f.nr_fatura");

		return sql;
	
	}

	// Projection e ordenação do relatório com a opção "Só totais"
	private SqlTemplate getSqlTemplateTotais(TypedFlatMap tfm) throws Exception{

		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("null", "FATURA");
		sql.addProjection("null", "FILIAL_FATURA");
		sql.addProjection("null", "CLIENTE");
		sql.addProjection("null", "TP_IDENTIFICACAO");
		sql.addProjection("null", "NR_IDENTIFICACAO");
		sql.addProjection("sum(f.vl_juro_calculado)", "JUROS_CALCULADO");

		StringBuffer sb = new StringBuffer();
		
		sb.append("NVL((SELECT INDN.VL_JURO_RECEBER ");
		sb.append("FROM ITEM_NOTA_DEBITO_NACIONAL INDN, ");
		sb.append("NOTA_DEBITO_NACIONAL NDN ");
		sb.append("WHERE NDN.ID_NOTA_DEBITO_NACIONAL = INDN.ID_NOTA_DEBITO_NACIONAL ");
		sb.append("AND NDN.TP_SITUACAO_NOTA_DEBITO_NAC NOT IN ('C', 'I') ");
		sb.append("AND INDN.ID_FATURA = f.id_fatura ), 0)");		
		
		sql.addProjection("sum(ir.vl_juros + " + sb.toString() + ")", "JUROS_COBRADO");
		sql.addProjection("sum((ir.vl_juros - f.vl_juro_calculado + " + sb.toString() + "))", "DIFERENCA_JUROS");
		sql.addProjection("(filfat.sg_filial || ' - ' || pfilfat.nm_fantasia)", "FILIAL_COBRANCA");
		
		getSqlTemplateGeral(tfm, sql);

		sql.addGroupBy("(filfat.sg_filial || ' - ' || pfilfat.nm_fantasia)");
		sql.addOrderBy("(filfat.sg_filial || ' - ' || pfilfat.nm_fantasia)");
		
		return sql;
	}

	// SQL genérico para os 2 relatórios... só totais e analítico 
	private void getSqlTemplateGeral(TypedFlatMap tfm, SqlTemplate sql) {

		sql.addFrom("redeco", "r");
		sql.addFrom("item_redeco", "ir");
		sql.addFrom("fatura", "f");
		sql.addFrom("cliente", "c");
		sql.addFrom("filial", "filfat");
		sql.addFrom("pessoa", "pc");
		sql.addFrom("filial", "filred");
		sql.addFrom("regional_filial", "rf");
		sql.addFrom("regional", "reg");
		sql.addFrom("pessoa", "pfilfat");
		
		
		sql.addJoin("r.id_redeco", "ir.id_redeco");
		sql.addJoin("ir.id_fatura", "f.id_fatura");
		sql.addJoin("f.id_cliente", "c.id_cliente");
		sql.addJoin("f.id_filial", "filfat.id_filial");
		sql.addJoin("c.id_cliente", "pc.id_pessoa");
		sql.addJoin("r.id_filial", "filred.id_filial");
		sql.addJoin("filred.id_filial", "rf.id_filial");
		sql.addJoin("rf.id_regional", "reg.id_regional");
		sql.addJoin("filfat.id_filial", "pfilfat.id_pessoa");
		
		sql.addCustomCriteria("rf.dt_vigencia_inicial <= sysdate");
		sql.addCustomCriteria("rf.dt_vigencia_final >= sysdate");
		
		// só deve buscar quando realmente há uma divergência no valor dos juros
		sql.addCustomCriteria("f.vl_juro_calculado <> ir.vl_juros");

		sql.addCriteria("r.tp_situacao_redeco", "<>", "CA");
		sql.addCriteria("f.tp_situacao_fatura", "<>", "CA");
		sql.addCriteria("f.tp_situacao_fatura", "<>", "IN");
	
		/** Resgata a o status do request */
		YearMonthDay competencia = tfm.getYearMonthDay("competencia");
		
		/**
		 *  Verifica se o status não é nulo, caso não seja, adiciona o status como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(competencia != null){
			sql.addCriteria("r.dt_liquidacao", ">=", competencia);
			sql.addCriteria("r.dt_liquidacao", "<=", competencia.plusMonths(1).minusDays(1));
			sql.addFilterSummary("competencia", JTFormatUtils.format(competencia, JTFormatUtils.MONTHYEAR, JTFormatUtils.SHORT));
		}
		
		Long idFilial = tfm.getLong("filial.idFilial");
		if(idFilial != null){
			sql.addCriteria("r.id_filial", "=", idFilial);
			sql.addFilterSummary("filialCobranca", tfm.getString("sgFilial") + " - " + tfm.getString("nmFilial"));
		}
		
		Long idRegional = tfm.getLong("regional.idRegional");
		if(idRegional != null){
			sql.addCriteria("reg.id_regional", "=", idRegional);
			sql.addFilterSummary("regional", tfm.getString("sgDsRegional"));
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("not exists (SELECT 1 ");
		sb.append("FROM ITEM_NOTA_DEBITO_NACIONAL INDN, ");
		sb.append("NOTA_DEBITO_NACIONAL NDN ");
		sb.append("WHERE NDN.ID_NOTA_DEBITO_NACIONAL = INDN.ID_NOTA_DEBITO_NACIONAL ");
		sb.append("AND NDN.TP_SITUACAO_NOTA_DEBITO_NAC NOT IN (?, ?) ");
		sb.append("AND INDN.ID_FATURA = f.id_fatura ");
		sb.append("AND INDN.VL_JURO_RECEBER = (f.VL_JURO_CALCULADO - f.VL_JURO_RECEBIDO))");
		
		sql.addCriteriaValue("C");
		sql.addCriteriaValue("I");

		sql.addCustomCriteria(sb.toString());
	}

}

