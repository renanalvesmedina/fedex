package com.mercurio.lms.coleta.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe responsável pela geração do Relatório de MDA
 * Especificação técnica 02.03.01.11
 * @author Cesar Gabbardo
 * 
 * @spring.bean id="lms.coleta.emitirRelatorioEstatisticoColetaUsuarioService"
 * @spring.property name="reportName" value="com/mercurio/lms/coleta/report/emitirRelatorioEstatisticoColetaUsuario.jasper"
 */
public class EmitirRelatorioEstatisticoColetaUsuarioService extends ReportServiceSupport {

    /**
     * Método responsável por gerar o relatório na quantidade informada. 
     */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap param = (TypedFlatMap)parameters;
		      
		SqlTemplate sql = mountSql(param);
        JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
                
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa", getFilterSummary(param));
        parametersReport.put("dataInicial", param.getYearMonthDay("dataInicial"));
        parametersReport.put("dataFinal", param.getYearMonthDay("dataFinal"));
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jr.setParameters(parametersReport);
        return jr;	
	}
	
	
	/**
	 * Método que gera o relatório de MDA. 
	 * @return
	 * @throws Exception
	 */
    public SqlTemplate mountSql(TypedFlatMap parameters) throws Exception { 
    	SqlTemplate sql = createSqlTemplate();

    	// SELECT
    	sql.addProjection("USUARIO.ID_USUARIO", "ID_USUARIO");
    	sql.addProjection("USUARIO.NM_USUARIO", "NM_USUARIO");
    	sql.addProjection("COUNT(*)", "TOTAL_COLETAS");
    	sql.addProjection("SUM(PEDIDO_COLETA.PS_TOTAL_VERIFICADO)", "PS_TOTAL_VERIFICADO");
    	sql.addProjection("SUM(PEDIDO_COLETA.QT_TOTAL_VOLUMES_VERIFICADO)", "QT_TOTAL_VOLUMES_VERIFICADO");
    	    	
    	// FROM
    	sql.addFrom("USUARIO", "USUARIO");
    	sql.addFrom("PEDIDO_COLETA", "PEDIDO_COLETA");

    	// JOIN
    	sql.addJoin("USUARIO.ID_USUARIO", "PEDIDO_COLETA.ID_USUARIO");
         
    	// CRITERIA
    	sql.addCriteria("PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL", "=", SessionUtils.getFilialSessao().getIdFilial());
    	sql.addCriteria("PEDIDO_COLETA.DH_PEDIDO_COLETA", ">=", parameters.getYearMonthDay("dataInicial").toDateTime(TimeOfDay.MIDNIGHT));
    	sql.addCriteria("PEDIDO_COLETA.DH_PEDIDO_COLETA", "<=", parameters.getYearMonthDay("dataFinal").toDateTime(TimeOfDay.MIDNIGHT.minusMillis(1)));

        // GROUP BY
    	sql.addGroupBy("USUARIO.ID_USUARIO");
    	sql.addGroupBy("USUARIO.NM_USUARIO");
    	
    	return sql;
    }
    
	/**
	 * Método que busca o Total de Coletas Geral. 
	 * @return
	 * @throws Exception
	 */
    public Integer executeTotalColetasGeral(YearMonthDay dataInicial, YearMonthDay dataFinal) throws Exception {
        SqlTemplate sql = createSqlTemplate();
        
    	sql.addProjection("COUNT(*)");    	
    	    	
    	sql.addFrom("USUARIO", "USUARIO");
    	sql.addFrom("PEDIDO_COLETA", "PEDIDO_COLETA");
    	    	
    	sql.addJoin("USUARIO.ID_USUARIO", "PEDIDO_COLETA.ID_USUARIO");

    	sql.addCriteria("PEDIDO_COLETA.ID_FILIAL_RESPONSAVEL", "=", SessionUtils.getFilialSessao().getIdFilial());
    	sql.addCustomCriteria("TRUNC(CAST(PEDIDO_COLETA.DH_PEDIDO_COLETA AS DATE)) >= TO_DATE('" + dataInicial + "','yyyy-MM-dd')");
    	sql.addCustomCriteria("TRUNC(CAST(PEDIDO_COLETA.DH_PEDIDO_COLETA AS DATE)) <= TO_DATE('" + dataFinal + "','yyyy-MM-dd')");
    	
        int nrTotalColetas = this.getJdbcTemplate().queryForInt(sql.getSql(),sql.getCriteria());        
        return Integer.valueOf(nrTotalColetas);
    }
    
	/**
	 * Método que calcula o percentual de coleta por usuário. 
	 * @return
	 * @throws Exception
	 */
    public String executeCalculaPercentual(Integer totalColetaPorUsuario, Integer totalColetaGeral) throws Exception {
    	return FormatUtils.formatDecimal("#,###,###,##0.00", roundDouble((totalColetaPorUsuario.doubleValue()*100)/totalColetaGeral.doubleValue(),2));
    }    
    
    public static final double roundDouble(double d, int places) {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10,
            (double) places);
    }


    
	/**
	 * Obtem os parametros comuns a ambas as consultas de coleta e entrega
	 * @param param
	 * @return
	 */
	private String getFilterSummary(TypedFlatMap param) {
		SqlTemplate sqlTemp = createSqlTemplate();

		sqlTemp.addFilterSummary("periodoInicial", JTFormatUtils.format(param.getYearMonthDay("dataInicial")));
		sqlTemp.addFilterSummary("periodoFinal", JTFormatUtils.format(param.getYearMonthDay("dataFinal")));
    	
		return sqlTemp.getFilterSummary();
	}    

}
