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
 * @author Hector Julian Esnaola junior
 *
 * @spring.bean id="lms.contasreceber.emitirProtocoloEntregaChequesService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirProtocoloEntregaCheques.jasper"
 */
public class EmitirProtocoloEntregaChequesService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(getMountSql(tfm), sql.getCriteria());
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
		
		/** Resgata as datas do request */
		YearMonthDay ymdInicial = tfm.getYearMonthDay("dataInicial");
		YearMonthDay ymdFinal = tfm.getYearMonthDay("dataFinal");	
  		
	  	SqlTemplate sqlTemp = this.createSqlTemplate();
	  	
	  	/** Seta o FilterSummary */
	  	if(ymdInicial != null){
	  		sqlTemp.addFilterSummary("vigenciaInicial", JTFormatUtils.format(ymdInicial));
	  	}
	  	if(ymdFinal != null){
	  		sqlTemp.addFilterSummary("vigenciaFinal", JTFormatUtils.format(ymdFinal));
	  	}
	  	
		return sqlTemp;

	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private String getMountSql(TypedFlatMap tfm) throws Exception{
		
		/** Resgata as datas do request */
		YearMonthDay ymdInicial = tfm.getYearMonthDay("dataInicial");
		YearMonthDay ymdFinal = tfm.getYearMonthDay("dataFinal");	
		
		/** Trecho Sql que faz um count*/
		String count = "(select count(*) ";
		
		/** Trecho Sql que faz um sum */
		String sum = "(select sum(vl_cheque) ";
		
		/** Trecho Sql genérico */
		StringBuffer sqlGenerico = new StringBuffer()
				.append("from	lote_cheque lc, cheque c, historico_filial hf ")
		  		.append("where	lc.id_lote_cheque = c.id_lote_cheque ")
		  			.append("and	c.id_filial = hf.id_filial ")
		  			.append("and	lc.dt_emissao between hf.dt_real_operacao_inicial and hf.dt_real_operacao_final ");
		
		/** Trecho Sql filtrando pelas datas que vem da tela */
		String sqlData = "";
		if(ymdInicial != null){
			sqlData = " and C.DT_VENCIMENTO >= to_date('" + ymdInicial + "','yyyy-mm-dd')";
		}	
		if(ymdFinal != null){
			sqlData += " and C.DT_VENCIMENTO <= to_date('" + ymdFinal + "','yyyy-mm-dd')";
		}
		sqlData = sqlData + " and C.DT_REAPRESENTACAO IS NULL "; 
		
		/** Trecho Sql  que filtra pelo banco HSBC */
		String hsbc = "and	c.nr_banco = 399 ";
		
		/** Trecho Sql  que filtra pelos bancos <> HSBC */
		String notHsbc = "and	c.nr_banco <> 399 ";
		
		/** Trecho Sql  que filtra pelo valor do cheque maior 299.99 */
		String valorAcima = "and 	c.vl_cheque > 299.99 ";
		
		/** Trecho Sql  que filtra pelo valor do cheque < 300.00 */
		String valorAbaixo = "and 	c.vl_cheque < 300.00 ";

		/** Trecho Sql  que filtro somente os cheques onde o último histórico é LIQUIDADO */
		String ultimoHistorico = "and 	(select hc1.tp_historico_cheque from historico_cheque hc1 where hc1.id_historico_cheque = " +
										" (select max(hc2.id_historico_cheque) from historico_cheque hc2 where hc2.id_cheque = c.id_cheque)) = 'LI' ";
		
		/** StringBuffer que monta todo Sql */
		StringBuffer sqlBuffer = new StringBuffer()
		  	.append("select ")
		  		.append(count)
		  		.append(sqlGenerico)
		  			.append(hsbc)
		  			.append(valorAcima)
		  			.append(sqlData)
		  			.append(ultimoHistorico)
		  			.append(") as qt_cheque_hsbc_maior, ")
		  			
		  		/** Conta a quantidade de cheques (HSBC - < 300.00) de acordo com a data passada */
		        .append(count)
		        .append(sqlGenerico)
		        	.append(hsbc)
		        	.append(valorAbaixo)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as qt_cheque_hsbc_menor, ")
		        
		        /** Conta a quantidade de cheques (Não HSBC - > 299.99) de acordo com a data passada */
		        .append(count)
		        .append(sqlGenerico)
		        	.append(notHsbc) 
		        	.append(valorAcima)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as qt_cheque_outros_maior, ")
		        
		        /** Conta a quantidade de cheques (Não HSBC - < 300.00) de acordo com a data passada */
		        .append(count)
		        .append(sqlGenerico)
		        	.append(notHsbc) 
		        	.append(valorAbaixo)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as qt_cheque_outros_menor, ")
		        
		        /** Soma os cheques (HSBC - > 299.99) de acordo com a data passada */
		        .append(sum)
		        .append(sqlGenerico)
		        	.append(hsbc)
		        	.append(valorAcima)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as vl_cheque_hsbc_maior, ")
		        
		        /** Soma os cheques (HSBC - < 300.00) de acordo com a data passada */
		        .append(sum)
		        .append(sqlGenerico)
		        	.append(hsbc)
		        	.append(valorAbaixo)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as vl_cheque_hsbc_menor, ")
		        
		        /** Soma os cheques (Não HSBC - > 299.99) de acordo com a data passada */
		        .append(sum)
		        .append(sqlGenerico)
		        	.append(notHsbc) 
		        	.append(valorAcima)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as vl_cheque_outros_maior, ")
		        
		        /** Soma os cheques (Não HSBC - < 300.00) de acordo com a data passada */
		        .append(sum)
		        .append(sqlGenerico)
		        	.append(notHsbc) 
		        	.append(valorAbaixo)
		        	.append(sqlData)
		  			.append(ultimoHistorico)
		        	.append(") as vl_cheque_outros_menor, ")
		        	
		        /** Soma os valores de todos os cheque de acordo com a data passada */	
	        	.append(sum)
		        .append(sqlGenerico)
		        .append(sqlData)
	  			.append(ultimoHistorico)
		        .append(") as vl_total, ")
		        
		        /** Conta a quantidade de todos cheques de acordo com a data passada */
	        	.append(count)
		        .append(sqlGenerico)
		        .append(sqlData)
	  			.append(ultimoHistorico)
		        .append(") as qt_total ")
	        
		.append("from dual");
		
		return sqlBuffer.toString();

	}

}
