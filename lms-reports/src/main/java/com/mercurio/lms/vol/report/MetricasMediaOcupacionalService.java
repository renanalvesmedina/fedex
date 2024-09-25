package com.mercurio.lms.vol.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração do relatório Emitir RNC.
 * Especificação técnica 12.01.01.03
 * @author Roberto Azambuja
 * 
 * @spring.bean id="lms.vol.metricasMediaOcupacionalService"
 * @spring.property name="reportName" value="com/mercurio/lms/vol/report/metricasMediaOcupacional.jasper"
 */
public class MetricasMediaOcupacionalService extends ReportServiceSupport {
	  private FilialService filialService;
	 /**
     * Método responsável pela geração do relatório 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap tfm = (TypedFlatMap)parameters;
        SqlTemplate sql = mountSql(tfm);
        
        Object[] criterias = new Object[6];
		criterias[0] = tfm.getLong("filial.idFilial");
		criterias[1] = (YearMonthDay) ReflectionUtils.toObject(""+tfm.get("dataInicial"), YearMonthDay.class);
		criterias[2] = (YearMonthDay) ReflectionUtils.toObject(""+tfm.get("dataFinal"), YearMonthDay.class);
		criterias[3] = tfm.getLong("filial.idFilial");
		criterias[4] = (YearMonthDay) ReflectionUtils.toObject(""+tfm.get("dataInicial"), YearMonthDay.class);
		criterias[5] = (YearMonthDay) ReflectionUtils.toObject(""+tfm.get("dataFinal"), YearMonthDay.class);
		
		List l = filialService.findNmSgFilialByIdFilial(tfm.getLong("filial.idFilial"));
		TypedFlatMap t = (TypedFlatMap)l.get(0);
		
		sql.addFilterSummary("filial",t.getString("sgFilial") + " - " + t.getString("pessoa.nmFantasia"));
		sql.addFilterSummary("periodoInicial",criterias[1]);
		sql.addFilterSummary("periodoFinal",criterias[2]);
        JRReportDataObject jr = executeQuery(sql.getSql(), criterias);

        // Seta os parametros que irão no cabeçalho da página, 
        // os parametros de pesquisa
        Map parametersReport = new HashMap();
        
        parametersReport.put("PARAM_PESQUISA", sql.getFilterSummary());
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
        jr.setParameters(parametersReport);
        return jr;
    }
 
   
    public SqlTemplate mountSql(TypedFlatMap parameters) {
        SqlTemplate sql = createSqlTemplate();
        
        StringBuffer sb = new StringBuffer();
    	sb.append("   id_meio_transporte, nr_frota, mes, sum(cte_tot) cte , sum(ctc_tot) ctc ") 
          .append("   from (  ")
          .append("          select mt.id_meio_transporte ")
          .append("              ,1 cte_tot ")
          .append("              ,0 ctc_tot ")
          .append("              ,mt.nr_frota ") 
          .append("              ,to_char(dh_ocorrencia, 'mm/w') mes ")
          .append("           from manifesto_entrega_documento med ") 
          .append("               ,manifesto_entrega me ") 
          .append("               ,manifesto m ") 
          .append("               ,controle_carga cc ")  
          .append("               ,meio_transporte mt ") 
          .append("               ,GRUPO_FROTA_VEICULO gv ") 
          .append("               ,grupo_frota gf ") 
          .append("         where med.id_manifesto_entrega = me.id_manifesto_entrega ") 
          .append("           and me.id_manifesto_entrega = m.id_manifesto ")  
          .append("           and m.id_controle_carga = cc.id_controle_carga ") 
          .append("           and cc.id_transportado = mt.id_meio_transporte ") 
          .append("           and med.dh_ocorrencia is not null ") 
          .append("           and mt.id_meio_transporte = gv.id_meio_transporte ")
          .append("           and gv.id_grupo_frota = gf.id_grupo_frota ") 
          .append("           and gf.id_filial = ? ")
          .append("           and trunc(dh_ocorrencia) between ? and ? ")
          .append("union all ")
          .append("         select mt.id_meio_transporte") 
          .append("               ,0 cte_tot")
          .append("               ,1 ctc_tot")
          .append("               , mt.nr_frota") 
          .append("               , to_char(dh_pedido_coleta, 'mm/w') mes ")
          .append("           from manifesto_coleta mc ") 
          .append("               ,manifesto m ") 
          .append("               ,controle_carga cc ")
          .append("               ,meio_transporte mt ") 
          .append("               ,pedido_coleta pc ") 
          .append("               ,evento_coleta ec ") 
          .append("               ,GRUPO_FROTA_VEICULO gv ") 
          .append("               ,grupo_frota gf ") 
          .append("          where mc.id_manifesto_coleta = m.id_manifesto ") 
          .append("            and m.id_controle_carga = cc.id_controle_carga ") 
          .append("            and cc.id_transportado = mt.id_meio_transporte ") 
          .append("            and mc.id_manifesto_coleta = pc.id_manifesto_coleta ") 
          .append("            and pc.id_pedido_coleta = ec.id_pedido_coleta ") 
          .append("            and ec.tp_evento_coleta = 'EX' ")
          .append("            and mt.id_meio_transporte = gv.id_meio_transporte ")
          .append("            and gv.id_grupo_frota = gf.id_grupo_frota ") 
          .append("            and gf.id_filial = ? ")
          .append("            and trunc(dh_pedido_coleta) between ? and ? ")
          .append(" ) ")
          .append("group by id_meio_transporte,nr_frota,mes ");
     
    	sql.addProjection(sb.toString());  
        
        
        return sql;
    
    }


	public FilialService getFilialService() {
		return filialService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}