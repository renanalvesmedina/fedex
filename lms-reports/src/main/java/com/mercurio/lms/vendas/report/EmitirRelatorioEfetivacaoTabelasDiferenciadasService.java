package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do Relatório de Emissão de Rota Coleta/Entrega
 * 
 * @author 
 * @spring.bean id="lms.vendas.emitirRelatorioEfetivacaoTabelasDiferenciadasService"
 * @spring.property name="reportName" value="com/mercurio/lms/vendas/report/emitirRelatorioEfetivacaoTabelasDiferenciadas.jrxml"
 */
public class EmitirRelatorioEfetivacaoTabelasDiferenciadasService extends ReportServiceSupport{
	
	private static String PDF_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioEfetivacaoTabelasDiferenciadas.jasper";
    private static String EXCEL_REPORT_NAME = "com/mercurio/lms/vendas/report/emitirRelatorioEfetivacaoTabelasDiferenciadasExcel.jasper";
	
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		SqlTemplate sql = getSql(parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();
		
		if(parameters.get("tpFormatoRelatorio") != null){
			if(parameters.get("tpFormatoRelatorio").equals("pdf")){
				this.setReportName(PDF_REPORT_NAME);				
			} else {
				this.setReportName(EXCEL_REPORT_NAME);
			}
		}
		
		parametersReport.put("parametrosPesquisa",  getFiltros(parameters));
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
			
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, "csv".equals(String.valueOf(parameters.get("tpFormatoRelatorio"))) ? "xls" : parameters.get("tpFormatoRelatorio"));
		jr.setParameters(parametersReport);
		return jr;
	}

	 private SqlTemplate getSql(Map parameters) {
		  
		 /**
	<field name="RazÃ£o Social" class="java.lang.String"/>
	<field name="Nome Fantasia" class="java.lang.String"/>
	<field name="CPF/CNPJ" class="java.lang.String"/>
	<field name="Tabela" class="java.lang.String"/>
	<field name="Data GeraÃ§Ã£o" class="java.sql.Timestamp"/>
	<field name="Data EfetivaÃ§Ã£o" class="java.sql.Timestamp"/>
 PESSOA.NM_PESSOA
 PESSOA.NM_FANTASIA
 PESSOA.NR_IDENTIFICACAO
 TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO || TIPO_TABELA_PRECO.NR_VERSAO || '-' || SUBTIPO_TABELA_PRECO.TP_SUBTIPO_TABELA_PRECO
 TABELA_PRECO.DT_GERACAO
 TABELA_PRECO.DT_VIGENCIA_INICIAL


TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO            = 'D'   
TABELA_PRECO.ID_TIPO_TABELA_PRECO                     = TIPO_TABELA_PRECO.ID_TIPO_TABELA_PRECO   
TABELA_PRECO.BL_EFETIVADA                                    = 'S'   
SUBTIPO_TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO = TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO   
TABELA_DIVISAO_CLIENTE.ID_TABELA_PRECO             = TABELA_PRECO.ID_TABELA_PRECO  
DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE                      = TABELA_DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE  
PESSOA.ID_PESSOA                                                     = DIVISAO_CLIENTE.ID_CLIENTE 
  */
	    	SqlTemplate sql = createSqlTemplate();
	    	//projection...
	    	sql.addProjection("P.NM_PESSOA" , "nmRazaoSocial");
	    	sql.addProjection("P.NM_FANTASIA", "nmFantasia");
	    	sql.addProjection("P.NR_IDENTIFICACAO", "nrCpfCnpj");
	    	sql.addProjection("P.TP_IDENTIFICACAO", "tpIdentificacao");
	    	sql.addProjection("TTP.TP_TIPO_TABELA_PRECO || TTP.NR_VERSAO || '-' ||  STP.TP_SUBTIPO_TABELA_PRECO", "nmTabela");
	    	sql.addProjection("to_char(TP.DT_GERACAO, 'dd/mm/yyyy')", "dtGeracao");
	    	sql.addProjection("to_char(TP.DT_VIGENCIA_INICIAL, 'dd/mm/yyyy')", "dtEfetivacao");
			 
	    	//from...
	    	sql.addFrom("TIPO_TABELA_PRECO", "TTP");
	    	sql.addFrom("TABELA_PRECO", "TP");
	    	sql.addFrom("SUBTIPO_TABELA_PRECO", "STP");
	    	sql.addFrom("TABELA_DIVISAO_CLIENTE", "TDC");
	    	sql.addFrom("DIVISAO_CLIENTE", "DC");
	    	sql.addFrom("PESSOA", "P");
	    	 
	    	
	    	//join...
	    	sql.addJoin("TTP.ID_TIPO_TABELA_PRECO", "TP.ID_TIPO_TABELA_PRECO");
	    	sql.addJoin("STP.ID_SUBTIPO_TABELA_PRECO", "TP.ID_SUBTIPO_TABELA_PRECO");
	    	sql.addJoin("TDC.ID_TABELA_PRECO", "TP.ID_TABELA_PRECO");
	    	sql.addJoin("DC.ID_DIVISAO_CLIENTE", "TDC.ID_DIVISAO_CLIENTE");
	    	sql.addJoin("P.ID_PESSOA", "DC.ID_CLIENTE");
	    	YearMonthDay dtInicio = (YearMonthDay )parameters.get("dtEfetivacaoInicial");
	    	YearMonthDay dtFim = (YearMonthDay )parameters.get("dtEfetivacaoFinal");
	    	String strDtInicio = null;
	    	StringBuilder datas = new StringBuilder();
	    	if(dtInicio != null){
	    		strDtInicio = dtInicio.toString("dd/MM/yyyy");
	    		datas.append(" TP.DT_VIGENCIA_INICIAL >= to_date('" + strDtInicio+"','dd/mm/yyyy')");
	    	}
	    	
	    	String strDtFim = null;
	    	if(dtFim != null){
	    		strDtFim = dtFim.toString("dd/MM/yyyy");
	    		datas.append(" and TP.DT_VIGENCIA_INICIAL <= to_date('" + strDtFim+"','dd/mm/yyyy')");	    		
	    	}
	    	sql.addCustomCriteria(datas.toString());
	    	sql.addCustomCriteria("  TP.BL_EFETIVADA = 'S'");
	    	sql.addCustomCriteria(" TTP.TP_TIPO_TABELA_PRECO = 'D' ");
	    	 
	    	sql.addOrderBy("TP.ID_TABELA_PRECO");
	        sql.addOrderBy("P.NM_FANTASIA");
	        sql.addOrderBy("TP.DT_VIGENCIA_INICIAL");
	        return sql;         
	    }
	 
	private String getFiltros(Map parameters){
		
		StringBuffer sb =new StringBuffer();
		YearMonthDay dtInicio = (YearMonthDay )parameters.get("dtEfetivacaoInicial");
    	YearMonthDay dtFim = (YearMonthDay )parameters.get("dtEfetivacaoFinal");
    	 
    	String strDtInicio = null;
    	String strDtFim = null;
    	if(dtFim != null){
    		strDtFim = dtFim.toString("dd/MM/yyyy");
    	}
    	if(dtInicio != null && dtFim != null){
    		strDtInicio = dtInicio.toString("dd/MM/yyyy");
    		sb.append("Data Efetivação: " + strDtInicio+" até " + strDtFim);
    	}
    	
    	
		return sb.toString();
	}
	
	
}