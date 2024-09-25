package com.mercurio.lms.carregamento.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 */

public class EmitirCTRCHorarioCorteFilialService extends ReportServiceSupport{

	/**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	SqlTemplate sql = getSql(parameters);
    	
        JRReportDataObject jrReportDataObject = executeQuery(sql.getSql(), sql.getCriteria());
        Map parametersReport = new HashMap();
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("parametrosPesquisa",sql.getFilterSummary());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
        
        jrReportDataObject.setParameters(parametersReport);
        
        return jrReportDataObject;
    }
    
    private SqlTemplate getSql(Map parameters) { 
    	
    	SqlTemplate sql = createSqlTemplate();    	
    	sql.addProjection("fo.sg_filial ||' '||cc.nr_conhecimento as ctrc ");
		sql.addProjection("fd.sg_filial as sg_filial_destino");
		sql.addProjection("to_char(ds.dh_emissao,'DD/MM/YYYY hh24:mi') as dh_emissao");
		sql.addProjection("to_char(fo.hr_corte, 'hh24:mi') as hr_corte");
		sql.addProjection("pr.nm_pessoa as nm_pessoa_remetente");
		sql.addProjection("pd.nm_pessoa as nm_pessoa_destinatario");
		sql.addProjection("to_char(ds.dt_prev_entrega,'DD/MM/YYYY')as dt_prev_entrega");
    	
    	sql.addFrom("docto_servico", "ds");
    	sql.addFrom("conhecimento", "cc");
    	sql.addFrom("pessoa", "pr");
    	sql.addFrom("pessoa", "pd");
    	sql.addFrom("filial", "fo");
    	sql.addFrom("filial", "fd");

    	sql.addJoin("cc.id_conhecimento", "ds.id_docto_servico");
    	sql.addJoin("pr.id_pessoa", "ds.id_cliente_remetente");
    	sql.addJoin("pd.id_pessoa", "ds.id_cliente_destinatario");
    	sql.addJoin("fo.id_filial", "ds.id_filial_origem");
    	sql.addJoin("fd.id_filial", "ds.id_filial_destino");
    	
    	//Criteria...
    	sql.addCustomCriteria("to_char(ds.dh_emissao, 'hh24:mi') > to_char(fo.hr_corte, 'hh24:mi') ");
    	sql.addCustomCriteria("cc.tp_situacao_conhecimento ='E'");

    	final String DH_EMISSAO = "ds.dh_emissao between to_date('%s','DD/MM/YYYY') AND to_date('%s','DD/MM/YYYY')";
    	
    	YearMonthDay dtInicio = (YearMonthDay )parameters.get("dtInicio");
    	YearMonthDay dtFim = (YearMonthDay )parameters.get("dtFim");

    	String strDtInicio = dtInicio.toString("dd/MM/yyyy");
    	String strDtFim = dtFim.toString("dd/MM/yyyy");
    	
    	sql.addCustomCriteria(String.format(DH_EMISSAO, strDtInicio,strDtFim));
    	
    	sql.addCriteria("cc.id_filial_origem", "=", MapUtils.getLong(parameters,"idFilial"));

		sql.addFilterSummary("periodoInicial",  strDtInicio);
		sql.addFilterSummary("periodoFinal",  strDtFim);

		sql.addOrderBy("fd.sg_filial");
    	sql.addOrderBy("ds.dh_emissao");
    	sql.addOrderBy("cc.nr_conhecimento");
        return sql;         
    }
    

}