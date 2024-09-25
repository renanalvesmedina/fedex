package com.mercurio.lms.fretecarreteiroviagem.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.UncategorizedSQLException;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 *
 * @spring.bean id="lms.fretecarreteiroviagem.emitirTotalFretesPagosService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirTotalFretesPagos.jasper"
 */
public class EmitirTotalFretesPagosService extends ReportServiceSupport {

	private ConversaoMoedaService conversaoMoedaService;
	
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap) parameters;
		
		SqlTemplate sql = montaSql(map);  
		
		montaFilterSummary(sql, map);
		
		Map parametersReport = new HashMap();		
		parametersReport.put("ID_MOEDA", Long.valueOf((String)map.get("moeda.idMoeda")));
		parametersReport.put("DS_SIMBOLO", map.get("moedaPais.moeda.dsSimbolo"));
		parametersReport.put("ID_PAIS", Long.valueOf((String)map.get("pais.idPais")));
		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,
					parameters.get("tpFormatoRelatorio"));
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		JRReportDataObject jr = null;			
        
		try {
			jr = executeQuery(sql.getSql(), sql.getCriteria());
			jr.setParameters(parametersReport);
		} catch (UncategorizedSQLException e) {
			throw new InfrastructureException(e.getMessage());
		}
		
		return jr;
	}
	
	private SqlTemplate montaSql(TypedFlatMap parameters){
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("F.ID_FILIAL");
		sql.addProjection("F.SG_FILIAL");
		sql.addProjection("P_FIL.NM_FANTASIA");
		sql.addProjection("RP.ID_RELACAO_PAGAMENTO");
		sql.addProjection("RP.NR_RELACAO_PAGAMENTO");
		sql.addProjection("RP.DT_GERACAO");
		sql.addProjection("RP.DT_PREVISTA_PAGTO");
		sql.addProjection("RP.DT_REAL_PAGTO");
		sql.addProjection("P_PROP.TP_IDENTIFICACAO");
		sql.addProjection("P_PROP.NR_IDENTIFICACAO");
		sql.addProjection("P_PROP.NM_PESSOA");
		sql.addProjection("P.ID_PROPRIETARIO");
		sql.addProjection("P_PROP.NM_PESSOA");
		sql.addProjection("RFC.VL_LIQUIDO");	
		sql.addProjection("RFC.DH_EMISSAO");
		sql.addProjection("MP.ID_MOEDA");
		sql.addProjection("MP.ID_PAIS");
		String moeda = parameters.getString("moeda.idMoeda");
		String pais  = parameters.getString("pais.idPais");
		String projection = "F_CONV_MOEDA("+pais+","+moeda+",MP.ID_PAIS,MP.ID_MOEDA,RFC.DH_EMISSAO,RFC.VL_LIQUIDO)";
		sql.addProjection(projection, "VL_LIQUIDO_CONVERTIDO");
		
		sql.addFrom("RECIBO_FRETE_CARRETEIRO", "RFC");
		sql.addFrom("RELACAO_PAGAMENTO", "RP");
		sql.addFrom("FILIAL", "F");
		sql.addFrom("PESSOA","P_FIL");
		sql.addFrom("PROPRIETARIO", "P");
		sql.addFrom("PESSOA", "P_PROP");
		sql.addFrom("MOEDA_PAIS", "MP");
		 
	    sql.addJoin("RFC.ID_RELACAO_PAGAMENTO", "RP.ID_RELACAO_PAGAMENTO");
	    sql.addJoin("RFC.ID_FILIAL", "F.ID_FILIAL");
	    sql.addJoin("P_FIL.ID_PESSOA", "F.ID_FILIAL");
	    sql.addJoin("RFC.ID_PROPRIETARIO", "P.ID_PROPRIETARIO");
	    sql.addJoin("P.ID_PROPRIETARIO", "P_PROP.ID_PESSOA");
	    sql.addJoin("MP.ID_MOEDA_PAIS", "RFC.ID_MOEDA_PAIS");
	    
	    sql.addCriteria("RFC.ID_FILIAL", "=", parameters.getLong("filial.idFilial"));
	    sql.addCriteria("P.TP_PROPRIETARIO", "=", parameters.getString("tpCarreteiro"));
	    sql.addCriteria("RFC.TP_RECIBO_FRETE_CARRETEIRO", "=", parameters.getString("tpRecibo"));
		sql.addCriteria("RP.DT_REAL_PAGTO", ">=", parameters.getYearMonthDay("dtPeriodoInicial"));
		sql.addCriteria("RP.DT_REAL_PAGTO", "<=", parameters.getYearMonthDay("dtPeriodoFinal"));
		
	    sql.addOrderBy("F.SG_FILIAL");
	    sql.addOrderBy("F.ID_FILIAL");
	    sql.addOrderBy("RP.NR_RELACAO_PAGAMENTO");
	    sql.addOrderBy("RP.ID_RELACAO_PAGAMENTO");
	    sql.addOrderBy("P_PROP.NM_PESSOA");
	    
		return sql;
	}	
	
	private void montaFilterSummary(SqlTemplate sql, TypedFlatMap parametros){
		if (!"".equals(parametros.getString("filial.sgFilial")) && !"".equals(parametros.getString("filial.pessoa.nmFantasia")))
			sql.addFilterSummary("filial", parametros.getString("filial.sgFilial") + " - " + parametros.getString("filial.pessoa.nmFantasia"));
				
		sql.addFilterSummary("tipoProprietario", parametros.getString("dsTpCarreteiro"));
		sql.addFilterSummary("tipoRecibo", parametros.getString("dsTpRecibo"));
		sql.addFilterSummary("situacaoRecibo", parametros.getString("dsTpSituacaoRecibo"));
		
		sql.addFilterSummary("converterParaMoeda", parametros.getString("moeda.siglaSimbolo"));
		
		if (!"".equals(parametros.getString("dtPeriodoInicial")))
			sql.addFilterSummary("periodoDePagamentoInicial", JTFormatUtils.format(parametros.getYearMonthDay("dtPeriodoInicial")));
		 
		if (!"".equals(parametros.getString("dtPeriodoFinal")))
			sql.addFilterSummary("periodoDePagamentoFinal", JTFormatUtils.format(parametros.getYearMonthDay("dtPeriodoFinal")));
	}
	
	public Double converteMoeda(Object[] parameters) {
		String strData = (String)parameters[5];
		if (StringUtils.isBlank(strData))
			return Double.valueOf(0);
		YearMonthDay data = JTFormatUtils.buildDateTimeFromTimestampTzString(strData).toYearMonthDay();

		return new Double(conversaoMoedaService.findConversaoMoeda((Long)parameters[0],(Long)parameters[1],
				(Long)parameters[2],(Long)parameters[3],
				data,(BigDecimal)parameters[4]).doubleValue());
	}
	
	public String formatCurrency(BigDecimal number) {
		if (number == null)
			return FormatUtils.formatDecimal("###,###,##0.00",BigDecimal.ZERO);
		
		return FormatUtils.formatDecimal("###,###,##0.00",number);
	}

	/**
	 * @param conversaoMoedaService The conversaoMoedaService to set.
	 */
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
}
