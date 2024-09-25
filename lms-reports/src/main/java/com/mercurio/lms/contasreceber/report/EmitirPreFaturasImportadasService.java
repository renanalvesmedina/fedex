package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.tributos.emitirPreFaturasImportadasService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirPreFaturasImportadas.jasper"
 */
public class EmitirPreFaturasImportadasService extends ReportServiceSupport {

	/** 
	 * Método invocado pela EmitirPreFaturasImportadasAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);

		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
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
		
		sql.addProjection("c.id_cliente as ID_CLIENTE, " +
						  "pc.tp_identificacao as TP_IDENTIFICACAO, " +
						  "pc.nr_identificacao as NR_IDENTIFICACAO, " +
						  "pc.nm_pessoa as CLIENTE, " +
						  "opf.nr_pre_fatura as PRE_FATURA, " +
						  "fat.nr_fatura as NR_FATURA, " +
						  "ffat.sg_filial as FILIAL_FATURA, " +
						  "fat.vl_total as VALOR, " +
						  "max(opf.dt_emissao) as EMISSAO, " +
						  "max(opf.dt_vencimento) as VENCIMENTO, " +
						  "max(opf.dh_importacao) as IMPORTADO ");
		
		sql.addFrom("ocorrencia_pre_fatura opf " +
					"left outer join fatura fat on fat.nr_pre_fatura = opf.nr_pre_fatura  " +
					"left outer join cliente c on c.id_cliente = opf.id_cliente " +
					"left outer join pessoa pc on pc.id_pessoa = c.id_cliente " +
					"left outer join filial ffat on ffat.id_filial = fat.id_filial ");
		
		sql.addCustomCriteria("opf.id_cliente = fat.id_cliente");
		
		/** Resgata  o idCliente do request */
		Long idCliente = tfm.getLong("cliente.idCliente");
		if(idCliente != null){
			sql.addCriteria("opf.id_cliente", "=", idCliente);
			sql.addFilterSummary("cliente", FormatUtils.formatIdentificacao(tfm.getString("cliente.tpIdentificacao"),
					tfm.getString("cliente.nrIdentificacao") + " - " + tfm.getString("cliente.nmCliente")));
		}
		
		/** Resgata as datas do request */
		YearMonthDay dtInicial = tfm.getYearMonthDay("periodoImportacaoInicial");
		YearMonthDay dtFinal = tfm.getYearMonthDay("periodoImportacaoFinal");
		
		if( dtInicial != null ){
			if( dtFinal != null ){
				sql.addCustomCriteria("(TRUNC(CAST(OPF.DH_IMPORTACAO AS DATE)) BETWEEN ? AND ?)");
				sql.addCriteriaValue(dtInicial);
				sql.addCriteriaValue(dtFinal);
				sql.addFilterSummary("periodoImportacaoInicial", dtInicial);
				sql.addFilterSummary("periodoImportacaoFinal", dtFinal);
			} else {
				sql.addCustomCriteria("(TRUNC(CAST(OPF.DH_IMPORTACAO AS DATE)) >= ?)");
				sql.addCriteriaValue(dtInicial);
				sql.addFilterSummary("periodoImportacaoInicial", dtInicial);
			}
		} else if( dtFinal != null ){
			sql.addCustomCriteria("(TRUNC(CAST(OPF.DH_IMPORTACAO AS DATE)) <= ?)");
			sql.addCriteriaValue(dtFinal);
			sql.addFilterSummary("periodoImportacaoFinal", dtFinal);
		}

		sql.addCustomCriteria("fat.tp_situacao_fatura <> 'CA'");

		sql.addGroupBy("c.id_cliente");
		sql.addGroupBy("pc.tp_identificacao");
		sql.addGroupBy("pc.nr_identificacao");
		sql.addGroupBy("pc.nm_pessoa");
		sql.addGroupBy("opf.nr_pre_fatura");
		sql.addGroupBy("fat.nr_fatura");
		sql.addGroupBy("ffat.sg_filial");
		sql.addGroupBy("fat.vl_total");
		
		sql.addOrderBy("pc.nr_identificacao, opf.nr_pre_fatura, ffat.sg_filial, fat.nr_fatura");

		logger.info(sql.getSql());
		
		return sql;
	}

}
