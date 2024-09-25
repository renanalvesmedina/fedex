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
 * @author 
 *
 * @spring.bean id="lms.contasreceber.emitirFechamentoDiarioChequesService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirFechamentoDiarioCheques.jasper"
 */
public class EmitirFechamentoDiarioChequesService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery("SELECT * FROM ( " + 
																sql.getSql() +
														  " ) WHERE ROWNUM < 2 ", sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));		
		
		jr.setParameters(parametersReport);

		return jr;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) throws Exception{
		
		SqlTemplate sql = this.createSqlTemplate();
		      
		sql.addProjection("F.SG_FILIAL || ' - ' || P.NM_FANTASIA", "FILIAL");
		sql.addProjection("M.SG_MOEDA || ' ' || M.DS_SIMBOLO", "MOEDA");
		
		YearMonthDay dtSaldo = tfm.getYearMonthDay("data");
		Long idMoedaPais = tfm.getLong("moedaPais.idMoedaPais");
		
		sql.addProjection(new StringBuffer()
				.append("(select	sctmp.vl_saldo ")
				.append(" from 		saldo_cheque sctmp ")
				.append(" where 	sctmp.id_filial = SC.id_filial ")
				.append(" and 		sctmp.id_moeda_pais = SC.id_moeda_pais ")
				.append(" and 		sctmp.id_moeda_pais = ? ")
				.append(" and		sctmp.id_saldo_cheque = ( ")
				.append("			select 	max(sctmp2.id_saldo_cheque) ")
				.append(" 			from 	saldo_cheque sctmp2 ")
				.append("			where 	sctmp2.dt_saldo < SC.DT_SALDO ")
				.append("			and 	sctmp2.id_moeda_pais = ? )) AS SALDOANTERIOR ").toString());
		
		sql.addCriteriaValue(idMoedaPais);
		sql.addCriteriaValue(idMoedaPais);
		
		sql.addProjection(new StringBuffer()
				.append("(select 		sum(ctmp.vl_cheque) ")
				.append(" from 			historico_cheque hctmp ")
				.append(" inner join 	cheque ctmp ")
				.append(" on 			hctmp.id_cheque = ctmp.id_cheque ")
				.append(" where 		hctmp.id_filial = sc.id_filial ")
				.append(" and			ctmp.id_moeda_pais = sc.id_moeda_pais ")
				.append(" and			trunc(hctmp.dh_historico_cheque) = ? ")
				.append(" and 		    ctmp.id_moeda_pais = ? ")
				.append(" and			hctmp.tp_operacao = 'C' ")
				.append(") AS ENTRADAS").toString());
		
		sql.addCriteriaValue(dtSaldo);
		sql.addCriteriaValue(idMoedaPais);
		
		sql.addProjection(new StringBuffer()
				.append("(select 		sum(ctmp.vl_cheque) ")
				.append(" from 			historico_cheque hctmp ")
				.append(" inner join 	cheque ctmp ")
				.append(" on 			hctmp.id_cheque = ctmp.id_cheque ")
				.append(" where 		hctmp.id_filial = sc.id_filial ")
				.append(" and			ctmp.id_moeda_pais = sc.id_moeda_pais ")
				.append(" and			trunc(hctmp.dh_historico_cheque) = ? ")
				.append(" and 		    ctmp.id_moeda_pais = ? ")
				.append(" and			hctmp.tp_operacao = 'D' ")
				.append(") AS SAIDAS").toString());
		
		sql.addCriteriaValue(dtSaldo);
		sql.addCriteriaValue(idMoedaPais);
		
		sql.addProjection(new StringBuffer()
				.append("(select 	sctmp.vl_saldo ")
				.append(" from 		saldo_cheque sctmp ")
				.append(" where 	sctmp.id_filial = SC.id_filial ")
				.append(" and 		sctmp.id_moeda_pais = SC.id_moeda_pais ")
				.append(" and 		sctmp.id_moeda_pais = ? ")
				.append(" and 		sctmp.dt_saldo = SC.DT_SALDO) as SALDOFINAL ").toString());
		
		sql.addCriteriaValue(idMoedaPais);
		
		sql.addFrom("SALDO_CHEQUE SC " +
					"INNER JOIN FILIAL F " +
					"	ON SC.ID_FILIAL = F.ID_FILIAL " +
					"INNER JOIN PESSOA P " +
					"	ON P.ID_PESSOA = F.ID_FILIAL " +
					"INNER JOIN MOEDA_PAIS MP " +
					"	ON MP.ID_MOEDA_PAIS =  SC.ID_MOEDA_PAIS " +
					"INNER JOIN MOEDA M " +
					"	ON MP.ID_MOEDA = M.ID_MOEDA "
		
		);
		
		// Monta os filtros e o filterSummary
		mountCriteriaSqlPrincipal(tfm, sql, dtSaldo, idMoedaPais);
		
		sql.addOrderBy("SC.DT_SALDO DESC");

		return sql;

	}

	private void mountCriteriaSqlPrincipal(TypedFlatMap tfm, SqlTemplate sql, YearMonthDay dtSaldo, Long idMoedaPais) {
		
		/** Resgata o id da filial do request */
		String idFilial = tfm.getString("idFilial");
		
		sql.addCriteria("SC.ID_FILIAL", "=", idFilial);
		sql.addFilterSummary("filial", tfm.getString("sgFilial") + " - " + tfm.getString("nmFilial"));

		sql.addCriteria("SC.DT_SALDO", "<=", dtSaldo);
		sql.addFilterSummary("data", JTFormatUtils.format(dtSaldo));
		
		sql.addCriteria("SC.id_moeda_pais", "=", idMoedaPais);
		sql.addFilterSummary("moeda", tfm.getString("siglaSimbolo"));
		
	}

}
