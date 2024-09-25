package com.mercurio.lms.municipios.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.municipios.emitirPostosPassagemRotaViagemService"
 * @spring.property name="reportName" value="com/mercurio/lms/municipios/report/emitirPostosPassagemRotaViagem.jasper"
 */
public class EmitirPostosPassagemRotaViagemService extends ReportServiceSupport {
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = createSqlTemplate(parameters);

		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		return jr; 
	}
	
	private SqlTemplate createSqlTemplate(Map criteria) {
		SqlTemplate sql = createSqlTemplate();

		//PROJECTION
		sql.addProjection("R.DS_ROTA");
		sql.addProjection("RO.SG_RODOVIA");
		sql.addProjection("PP.NR_KM");
		sql.addProjection("PP.ID_POSTO_PASSAGEM");
		sql.addProjection("M.NM_MUNICIPIO");
		sql.addProjection("UF.SG_UNIDADE_FEDERATIVA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("P.NM_PAIS_I"));
		sql.addProjection("VD_TP_SENTIDO_COBRANCA.DS_VALOR_DOMINIO","D_SENTIDO_COBRANCA");
		sql.addProjection("VD_TP_POSTO_PASSAGEM.DS_VALOR_DOMINIO","D_POSTO_PASSAGEM");
		sql.addProjection("VD_TP_FORMA_COBRANCA.DS_VALOR_DOMINIO","D_FORMA_COBRANCA");

		//JOIN
		sql.addFrom("POSTO_PASSAGEM_ROTA_VIAGEM","PPRV");
		sql.addFrom("ROTA","R");
		sql.addFrom("POSTO_PASSAGEM","PP");
		sql.addFrom("VALOR_DOMINIO","VD_TP_POSTO_PASSAGEM");
		sql.addFrom("VALOR_DOMINIO","VD_TP_SENTIDO_COBRANCA");
		sql.addFrom("TARIFA_POSTO_PASSAGEM","TPP");
		sql.addFrom("VALOR_DOMINIO","VD_TP_FORMA_COBRANCA");
		sql.addFrom("RODOVIA","RO");
		sql.addFrom("PAIS","P");
		sql.addFrom("UNIDADE_FEDERATIVA","UF");
		sql.addFrom("MUNICIPIO","M");

		// WHERE JOIN 
		sql.addJoin("PPRV.ID_ROTA","R.ID_ROTA");

		sql.addJoin("PPRV.ID_POSTO_PASSAGEM","PP.ID_POSTO_PASSAGEM");
		sql.addJoin("RO.ID_RODOVIA","PP.ID_RODOVIA");
		sql.addJoin("M.ID_MUNICIPIO","PP.ID_MUNICIPIO");
		sql.addJoin("M.ID_UNIDADE_FEDERATIVA","UF.ID_UNIDADE_FEDERATIVA");
		sql.addJoin("P.ID_PAIS","UF.ID_PAIS");

		sql.addJoin("VD_TP_POSTO_PASSAGEM.VL_VALOR_DOMINIO","PP.TP_POSTO_PASSAGEM");
		sql.addJoin("VD_TP_SENTIDO_COBRANCA.VL_VALOR_DOMINIO","PP.TP_SENTIDO_COBRANCA");
		
		sql.addJoin("TPP.ID_POSTO_PASSAGEM","PP.ID_POSTO_PASSAGEM");
		sql.addJoin("VD_TP_FORMA_COBRANCA.VL_VALOR_DOMINIO","TPP.TP_FORMA_COBRANCA");

		sql.addCustomCriteria("VD_TP_SENTIDO_COBRANCA.ID_DOMINIO = (SELECT (ID_DOMINIO) FROM DOMINIO WHERE NM_DOMINIO = 'DM_SENTIDO_COBRANCA_POSTO_PASSAGEM')");
		sql.addCustomCriteria("VD_TP_FORMA_COBRANCA.ID_DOMINIO = (SELECT (ID_DOMINIO) FROM DOMINIO WHERE NM_DOMINIO = 'DM_FORMA_COBRANCA_POSTO_PASSAGEM')");
		sql.addCustomCriteria("VD_TP_POSTO_PASSAGEM.ID_DOMINIO = (SELECT (ID_DOMINIO) FROM DOMINIO WHERE NM_DOMINIO = 'DM_POSTO_PASSAGEM')");

		//VALIDA AS VIGENCIAS
		sql.addCustomCriteria("PP.DT_VIGENCIA_INICIAL <= CURRENT_DATE");
		sql.addCustomCriteria("(PP.DT_VIGENCIA_FINAL IS NULL OR PP.DT_VIGENCIA_FINAL >= CURRENT_DATE)");
		sql.addCustomCriteria("PPRV.DT_VIGENCIA_INICIAL <= CURRENT_DATE");
		sql.addCustomCriteria("(PPRV.DT_VIGENCIA_FINAL IS NULL OR PPRV.DT_VIGENCIA_FINAL >= CURRENT_DATE)");
		sql.addCustomCriteria("TPP.DT_VIGENCIA_INICIAL <= CURRENT_DATE");
		sql.addCustomCriteria("(TPP.DT_VIGENCIA_FINAL IS NULL OR TPP.DT_VIGENCIA_FINAL >= CURRENT_DATE)");

		//WHERE VIEW
		Long idRota = MapUtils.getLong(MapUtils.getMap(criteria, "rota"), "idRota");
		if(idRota != null)
			sql.addCriteria("R.ID_ROTA", "=", idRota);

		String tpPostoPassagem = MapUtils.getString(criteria, "tpPostoPassagem");
		if (!StringUtils.isBlank(tpPostoPassagem))
			sql.addCriteria("PP.TP_POSTO_PASSAGEM", "=", tpPostoPassagem);

		Long idPostoPassagem = MapUtils.getLong(MapUtils.getMap(criteria, "postoPassagem"), "idPostoPassagem");
		if(idPostoPassagem != null)
			sql.addCriteria("PP.ID_POSTO_PASSAGEM", "=", idPostoPassagem);

		//ORDER BY
		sql.addOrderBy("R.DS_ROTA");
		sql.addOrderBy("RO.SG_RODOVIA");
		sql.addOrderBy("PP.NR_KM");
		return sql;
	}

}
