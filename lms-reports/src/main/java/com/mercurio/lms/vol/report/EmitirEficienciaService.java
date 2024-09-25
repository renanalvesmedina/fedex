package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * Classe responsável pela geração do Relatório de Emissão de Eficiencia de Cargas -
 * 
 * @author Tairone Lopes
 * 
 * @spring.bean id="lms.vol.emitirEficienciaService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/emitirEficiencia.jasper"
 */
public class EmitirEficienciaService extends ReportServiceSupport{
	private FilialService filialService;
	private PessoaService pessoaService;
	
	private void addSummary(StringBuffer parametros, String name, String value) {
		if (parametros.length() > 0) {
			parametros.append("|  ");
		}
		parametros.append(name).append(": ").append(value).append("  ");
	}

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		StringBuffer parametros = new StringBuffer();
		if (parameters.get("idFilial") != null) {
			Filial filial = filialService.findById(Long.valueOf(parameters.get("idFilial").toString()));
			addSummary(parametros, "Filial", filial.getSgFilial());
			parameters.put("sgFilial", filial.getSgFilial());
		}
		if (parameters.get("dtInicio") != null && parameters.get("dtFim") != null) {
			YearMonthDay dtInicio = (YearMonthDay )parameters.get("dtInicio");
			YearMonthDay dtFim = (YearMonthDay )parameters.get("dtFim");
			
			String strDtInicio = dtInicio.toString("dd/MM/yyyy");
			String strDtFim = dtFim.toString("dd/MM/yyyy");
			addSummary(parametros, "Data Início", strDtInicio);
			addSummary(parametros, "Data Fim", strDtFim);
		}
		if (parameters.get("idClienteRemetente") != null) {
			Pessoa pessoa = getPessoaService().findById(Long.valueOf(parameters.get("idClienteRemetente").toString())); 
			addSummary(parametros, "Remetente", pessoa.getNmPessoa());
		}

		String sql = getSql(parameters);
	    JRReportDataObject jr = executeQuery(sql, new Object[0]);
	    
	    Map parametersReport = new HashMap();
		parametersReport.put("PARAMETROS_PESQUISA", parametros.toString());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		jr.setParameters(parametersReport);
		return jr;
	}

	
	private String getSql(Map parameters) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tb.sg_filial                                                   AS SG_FILIAL ");
		sql.append(",      tb.tp_n                                                        AS QT_NORMAL ");
		sql.append(",      ROUND( tb.tp_n * 100 / DECODE( tb.tp_u, 0, 1, tp_u ), 2 )      AS QT_P_NORMAL ");
		sql.append(",      tb.tp_o                                                        AS QT_ON_TIME ");
		sql.append(",      ROUND( tb.tp_o * 100 / DECODE( tb.tp_u, 0, 1, tp_u ), 2 )      AS QT_P_ON_TIME ");
		sql.append(",      tb.tp_c                                                        AS QT_VOL ");
		sql.append(",      ROUND( tb.tp_c * 100 / DECODE( tb.tp_u, 0, 1, tp_u ), 2 )      AS QT_P_VOL ");
		sql.append(",      tb.tp_n + tb.tp_o + tb.tp_c                                    AS QT_UNIDADE");
		sql.append(",      ROUND( tb.tp_u * 100 / DECODE( tb.total, 0, 1, tb.total ), 2 ) AS QT_P_UNIDADE ");
		sql.append(",      tb.tp_p                                                        AS QT_PARCEIRA ");
		sql.append(",      ROUND( tb.tp_p * 100 / DECODE( tb.total, 0, 1, tb.total ), 2 ) AS QT_P_PARCEIRA ");
		sql.append(",      tb.total                                                       AS TOTAL ");
		sql.append("FROM   ( SELECT   f.sg_filial ");
		sql.append("         ,        COUNT( DECODE( med.tp_forma_baixa, 'N', 1 ) ) AS tp_n  ");
		sql.append("         ,        COUNT( DECODE( med.tp_forma_baixa, 'O', 1 ) ) AS tp_o ");
		sql.append("         ,        COUNT( DECODE( med.tp_forma_baixa, 'C', 1 ) ) AS tp_c ");
		sql.append("         ,        COUNT( DECODE( med.tp_forma_baixa,  ");
		sql.append("                  'N', 1, 'O', 1, 'C', 1) )                     AS tp_u ");         
		sql.append("         ,        COUNT( DECODE( med.tp_forma_baixa, 'P', 1 ) ) AS tp_p ");
		sql.append("         ,        COUNT( 1 )                                    AS total ");
		sql.append("         FROM     ( SELECT med.id_manifesto_entrega ");
		sql.append("         			,      ds.id_cliente_remetente ");
		sql.append("                    ,      DECODE( m.tp_manifesto_entrega, 'EP', 'P', med.tp_forma_baixa ) AS tp_forma_baixa ");
		sql.append("                    FROM   manifesto_entrega_documento med ");
		sql.append("                    JOIN   manifesto                   m   ON med.id_manifesto_entrega = m.id_manifesto ");
		sql.append("                    JOIN   docto_servico               ds  ON ds.id_docto_servico = med.id_docto_servico ");
		sql.append("                    WHERE  med.tp_forma_baixa IN ( 'N', 'C', 'O', 'P' ) ");
		sql.append("                    AND    med.tp_situacao_documento != 'CANC' ");
		sql.append("                    AND    sys_extract_utc(med.dh_ocorrencia)  ");
		
		if (parameters.get("dtInicio") != null && parameters.get("dtFim") != null) {
			YearMonthDay dtInicio = (YearMonthDay )parameters.get("dtInicio");
	    	YearMonthDay dtFim = (YearMonthDay )parameters.get("dtFim");

	    	String strDtInicio = dtInicio.toString("dd/MM/yyyy");
	    	String strDtFim = dtFim.toString("dd/MM/yyyy");
	    	
			sql.append("                           BETWEEN to_date( '"+strDtInicio+"', 'DD/MM/YYYY' ) "); 
			sql.append("                           AND to_date( '"+strDtFim+"', 'DD/MM/YYYY' ) + 1 ) med ");
		}
		
		sql.append("         JOIN     manifesto_entrega me ON me.id_manifesto_entrega = med.id_manifesto_entrega ");
		sql.append("         JOIN     filial            f  ON me.id_filial = f.id_filial  ");
		
		String sgFilial = null;
		if (parameters.get("sgFilial") != null) {
			sgFilial = "'"+parameters.get("sgFilial").toString()+"'";
		}
		sql.append("         WHERE    NVL( "+sgFilial+", '???' ) IN ( '???', f.sg_filial ) ");

		Long idClienteRemetente = MapUtils.getLong(parameters,"idClienteRemetente");
		sql.append("         AND      NVL( "+idClienteRemetente+", -1 ) IN ( -1, med.id_cliente_remetente ) ");

		sql.append("         GROUP BY f.sg_filial ) tb ");
		sql.append("ORDER BY sg_filial ");
		return sql.toString();
	}
	    
	public FilialService getFilialService() {
		return filialService;
}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public PessoaService getPessoaService() {
		return pessoaService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}	    
}
