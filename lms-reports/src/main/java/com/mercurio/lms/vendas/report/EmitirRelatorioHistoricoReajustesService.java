package com.mercurio.lms.vendas.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * 
 * @spring.bean id="lms.vendas.emitirRelatorioHistoricoReajustesService"
 * @spring.property name="reportName" value=
 *                  "com/mercurio/lms/vendas/report/emitirRelatorioHistoricoReajustes.jasper"
 */
public class EmitirRelatorioHistoricoReajustesService extends ReportServiceSupport {

	@SuppressWarnings("unchecked")
	public JRReportDataObject execute(Map criteria) throws Exception {
		TypedFlatMap map = new TypedFlatMap(criteria);
		SqlTemplate sql = createSqlTemplate(map);

		// Seta os parametros
		Map parametersReport = new HashMap();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, criteria.get("formatoRelatorio"));
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		jr.setParameters(parametersReport);
		
		return jr;
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) throws Exception {
		SqlTemplate sql = this.createSqlTemplate();

		
		/** SELECT */
		
		sql.addProjection("REGIONAL.DS_REGIONAL", "dsRegional");
		sql.addProjection("FILIAL.SG_FILIAL", "sgFilial");
	 	sql.addProjection("PESSOA.NR_IDENTIFICACAO", "nrIdentificacao");
	 	sql.addProjection("PESSOA.TP_IDENTIFICACAO", "tpIdentificacao");
		sql.addProjection("PESSOA.NM_PESSOA", "nmPessoa");
		sql.addProjection("PESSOA.NM_FANTASIA", "nmFantasia");
		sql.addProjection("DIVISAO_CLIENTE.DS_DIVISAO_CLIENTE", "dsDivisaoCliente");
		sql.addProjection("TIPO_TABELA_PRECO.TP_TIPO_TABELA_PRECO || TIPO_TABELA_PRECO.NR_VERSAO || '-' || SUBTIPO_TABELA_PRECO.TP_SUBTIPO_TABELA_PRECO ", "nmTabelaDivisao");
		sql.addProjection("case when tabela_divisao_cliente.bl_atualizacao_automatica = 'S' then 'SIM' else 'NÃO' end","snAtualizacaoAutomatica");
		sql.addProjection("HISTORICO_REAJUSTE_CLIENTE.PC_REAJUSTE", "pcReajuste");
		sql.addProjection("to_char(HISTORICO_REAJUSTE_CLIENTE.DT_REAJUSTE,'dd/mm/yyyy hh24:mi')", "dtReajuste");
			 
		sql.addProjection(" case when HISTORICO_REAJUSTE_CLIENTE.tp_forma_reajuste = 'A' then 'AUTOMÁTICO' else case when HISTORICO_REAJUSTE_CLIENTE.tp_forma_reajuste = 'M' then 'MANUAL' else case when HISTORICO_REAJUSTE_CLIENTE.tp_forma_reajuste = 'P' then 'PROPOSTA' else case when HISTORICO_REAJUSTE_CLIENTE.tp_forma_reajuste = 'L' then 'ALTERAÇÃO' end end end end ", "tpReajuste");
		sql.addProjection("DECODE(DIVISAO_CLIENTE.TP_SITUACAO,'A','Ativo',DECODE(DIVISAO_CLIENTE.TP_SITUACAO,'I','Inativo',null))", "stDivisao");
		sql.addProjection("DECODE(CLIENTE.TP_CLIENTE,'E','Eventual',DECODE(CLIENTE.TP_CLIENTE,'P','Potencial',DECODE(CLIENTE.TP_CLIENTE,'S','Especial',DECODE(CLIENTE.TP_CLIENTE,'F','Filial de cliente Especial',null))))", "tpCliente");
	
		sql.addProjection(" case when exists (select pc.id_parametro_cliente from parametro_cliente pc where pc.id_tabela_divisao_cliente = tabela_divisao_cliente.id_tabela_divisao_cliente and   pc.tp_situacao_parametro = 'A'  and   pc.dt_vigencia_inicial <= trunc(sysdate) and   pc.dt_vigencia_final >= trunc(sysdate)  and   pc.pc_frete_percentual > 0) then 'SIM' else 'NÃO' end", "snPercentual");  
	
		sql.addProjection("(select ttp1.tp_tipo_tabela_preco || ttp1.nr_versao || '-' || stp1.tp_subtipo_tabela_preco from tabela_preco tp1, tipo_tabela_preco ttp1, subtipo_tabela_preco stp1 where tp1.id_tabela_preco = historico_reajuste_cliente.id_tabela_preco_anterior and   ttp1.id_tipo_tabela_preco       = tp1.id_tipo_tabela_preco and   stp1.id_subtipo_tabela_preco = tp1.id_subtipo_tabela_preco)", "nmTabelaAntiga");
		sql.addProjection("(SELECT TTP1.TP_TIPO_TABELA_PRECO || TTP1.NR_VERSAO || '-' || STP1.TP_SUBTIPO_TABELA_PRECO FROM TABELA_PRECO TP1, TIPO_TABELA_PRECO TTP1, SUBTIPO_TABELA_PRECO STP1 WHERE TP1.ID_TABELA_PRECO                 = HISTORICO_REAJUSTE_CLIENTE.ID_TABELA_PRECO_NOVA AND TTP1.ID_TIPO_TABELA_PRECO       = TP1.ID_TIPO_TABELA_PRECO AND STP1.ID_SUBTIPO_TABELA_PRECO = TP1.ID_SUBTIPO_TABELA_PRECO)", "nmTabelaReajustada");
		
		sql.addProjection("to_char(CLIENTE.DT_ULTIMO_MOVIMENTO, 'dd/mm/yyyy')","dtUltimoMovimento");
		/** FROM */
		sql.addFrom("CLIENTE");
		sql.addFrom("DIVISAO_CLIENTE");
		sql.addFrom("TABELA_DIVISAO_CLIENTE");
		sql.addFrom("TABELA_PRECO");
		sql.addFrom("TIPO_TABELA_PRECO");
		sql.addFrom("SUBTIPO_TABELA_PRECO");
		sql.addFrom("HISTORICO_REAJUSTE_CLIENTE");
		sql.addFrom("FILIAL");
		sql.addFrom("REGIONAL");
		sql.addFrom("REGIONAL_FILIAL");
		sql.addFrom("PESSOA");
		
		/** JOIN */
		sql.addJoin("DIVISAO_CLIENTE.ID_CLIENTE", "CLIENTE.ID_CLIENTE");
		sql.addJoin("PESSOA.ID_PESSOA", "CLIENTE.ID_CLIENTE");
		sql.addJoin("TABELA_DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE", "DIVISAO_CLIENTE.ID_DIVISAO_CLIENTE");
		sql.addJoin("TABELA_PRECO.ID_TABELA_PRECO", "TABELA_DIVISAO_CLIENTE.ID_TABELA_PRECO");
		sql.addJoin("TIPO_TABELA_PRECO.ID_TIPO_TABELA_PRECO", "TABELA_PRECO.ID_TIPO_TABELA_PRECO");
		sql.addJoin("SUBTIPO_TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO", "TABELA_PRECO.ID_SUBTIPO_TABELA_PRECO");
		sql.addJoin("HISTORICO_REAJUSTE_CLIENTE.ID_TABELA_DIVISAO_CLIENTE", "TABELA_DIVISAO_CLIENTE.ID_TABELA_DIVISAO_CLIENTE");
		sql.addJoin("FILIAL.ID_FILIAL", "CLIENTE.ID_FILIAL_ATENDE_COMERCIAL");
		sql.addJoin("REGIONAL_FILIAL.ID_FILIAL", "FILIAL.ID_FILIAL");
		sql.addJoin("REGIONAL.ID_REGIONAL", "REGIONAL_FILIAL.ID_REGIONAL");

		/** WHERE */
		sql.addCustomCriteria("TRUNC(SYSDATE) BETWEEN REGIONAL_FILIAL.DT_VIGENCIA_INICIAL AND REGIONAL_FILIAL.DT_VIGENCIA_FINAL");
		sql.addCustomCriteria("(CLIENTE.TP_CLIENTE = 'S' OR CLIENTE.TP_CLIENTE = 'F')");
		sql.addCustomCriteria("CLIENTE.TP_SITUACAO 	= 'A'");
		sql.addCustomCriteria("DIVISAO_CLIENTE.TP_SITUACAO = 'A'");
		
		Long idRegional = criteria.getLong("idRegional");
		if (idRegional != null) {
			sql.addCriteria("REGIONAL.ID_REGIONAL", "=", idRegional);
			sql.addFilterSummary("regional",  criteria.getString("dsRegional"));
		}
		
		Long idFilial = criteria.getLong("idFilial");
		if(idFilial != null){
			sql.addCriteria("FILIAL.ID_FILIAL", "=", idFilial); 
			sql.addFilterSummary("filial", criteria.getString("sgFilial"));
		}
		
		
		YearMonthDay dtInicio = (YearMonthDay )criteria.get("periodo");
    	YearMonthDay dtFim = (YearMonthDay )criteria.get("dtPeriodoFinal");
    	StringBuilder datas = new StringBuilder();
    	String strDtInicio = null;
    	if(dtInicio != null){
    		strDtInicio = dtInicio.toString("dd/MM/yyyy");
    		datas.append(" HISTORICO_REAJUSTE_CLIENTE.DT_REAJUSTE  >= to_date('" + strDtInicio+"','dd/mm/yyyy')");
    	}
    	
    	String strDtFim = null;
    	if(dtFim != null){
    		strDtFim = dtFim.toString("dd/MM/yyyy");
    		datas.append(" and HISTORICO_REAJUSTE_CLIENTE.DT_REAJUSTE <= to_date('" + strDtFim+"','dd/mm/yyyy')");	    		
    	}
    	sql.addCustomCriteria(datas.toString());

    	if(dtInicio != null && dtFim != null){
    		sql.addFilterSummary("periodo", strDtInicio + " até " + strDtFim);
    	}
		
		Long idCliente = criteria.getLong("idPessoa");
		if(idCliente != null){
			sql.addCriteria("CLIENTE.ID_CLIENTE", "=", idCliente); 
			sql.addFilterSummary("cliente", criteria.getString("nmCliente"));
		}
		
		String formaReajuste = criteria.getString("formaReajuste");
		if(formaReajuste != null){
			sql.addCriteria("HISTORICO_REAJUSTE_CLIENTE.TP_FORMA_REAJUSTE", "=", formaReajuste);
	        DomainValue dv = getDomainValueService().findDomainValueByValue("DM_FORMA_REAJUSTE", formaReajuste);
	        sql.addFilterSummary("formaReajuste", dv.getDescriptionAsString());
		}
		return sql;
	}
}
