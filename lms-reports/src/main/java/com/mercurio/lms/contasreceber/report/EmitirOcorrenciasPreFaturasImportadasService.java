package com.mercurio.lms.contasreceber.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author José Rodrigo Moraes
 * @since  27/04/2006
 *
 * @spring.bean id="lms.contasreceber.emitirOcorrenciasPreFaturasImportadasService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirOcorrenciasPreFaturasImportadas.jasper"
 */
public class EmitirOcorrenciasPreFaturasImportadasService extends ReportServiceSupport {
	
	private DomainValueService domainValueService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
		SqlTemplate sql = getSqlTemplate(tfm);
		
		Object[] values = JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),  sql.getCriteria());
		
		List list = getJdbcTemplate().query(sql.getSql(), values, new RowMapper() {
			
			
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				Map map = new HashMap();
				
				map.put("ID_CLIENTE", Long.valueOf(rs.getLong("ID_CLIENTE")));
				map.put("TP_IDENTIFICACAO",rs.getString("TP_IDENTIFICACAO"));
				map.put("NR_IDENTIFICACAO",rs.getString("NR_IDENTIFICACAO"));
				map.put("NM_PESSOA",rs.getString("NM_PESSOA"));
				map.put("DT_IMPORTACAO",rs.getString("DT_IMPORTACAO"));
				map.put("NR_PRE_FATURA",rs.getString("NR_PRE_FATURA"));
				map.put("DT_EMISSAO_OPF",rs.getDate("DT_EMISSAO_OPF"));
				map.put("DT_VENCIMENTO",rs.getDate("DT_VENCIMENTO"));
				map.put("IMPORTACAO",rs.getString("DH_IMPORTACAO"));
				map.put("NM_ARQUIVO",rs.getString("NM_ARQUIVO"));
				
				if( rs.getString("TP_DOCUMENTO_SERVICO") != null){
					map.put("DS_TP_DOCUMENTO",(domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO",rs.getString("TP_DOCUMENTO_SERVICO")).getDescription()).toString());
					map.put("TP_DOCUMENTO_SERVICO",rs.getString("TP_DOCUMENTO_SERVICO"));
					map.put("SG_FILIAL_DOCTO",rs.getString("SG_FILIAL_DOCTO"));
					map.put("NR_DOCUMENTO_SERVICO",rs.getString("NR_DOCUMENTO_SERVICO"));
					map.put("DT_EMISSAO_IOPF",rs.getDate("DT_EMISSAO_IOPF"));
					map.put("OBSERVACAO",rs.getString("OBSERVACAO"));
				}								
				
				return map;
			}
			
		});
		
		JRReportDataObject jr = createReportDataObject(new JRMapCollectionDataSource(list), new HashMap());
        
		Map parametersReport = new HashMap();
		
		if( tfm.getLong("cliente.idCliente") != null ){
			sql.addFilterSummary("cliente", tfm.getString("nrIdentificacaoFormatado") + " - " + tfm.getString("cliente.pessoa.nmPessoa"));
		}
		
		if( tfm.getYearMonthDay("periodoInicial") != null ){
			sql.addFilterSummary("periodoImportacaoInicial",JTFormatUtils.format(tfm.getYearMonthDay("periodoInicial")));
		}
		
		if( tfm.getYearMonthDay("periodoFinal") != null ){
			sql.addFilterSummary("periodoImportacaoFinal",JTFormatUtils.format(tfm.getYearMonthDay("periodoFinal")));
		}

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
        
        /** Seta o parâmetro de tipo de arquivo a ser gerado */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));
		
		jr.setParameters(parametersReport);
		
		/** Seleciona qual relatório a ser impresso, através do h */
		if (tfm.getString("tpOcorrencia").equals("DHL"))
			this.setReportName("com/mercurio/lms/contasreceber/report/emitirOcorrenciasPreFaturasImportadasDHL.jasper");
		else if(tfm.getString("tpOcorrencia").equals("PADRAO"))
			this.setReportName("com/mercurio/lms/contasreceber/report/emitirOcorrenciasPreFaturasImportadas.jasper");
		
		return jr;
	}

	private SqlTemplate getSqlTemplate(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("CLI.ID_CLIENTE, " +
				          "PES.TP_IDENTIFICACAO, " +
				          "PES.NR_IDENTIFICACAO, " +
				          "PES.NM_PESSOA, " +
				          "OPF.DH_IMPORTACAO DT_IMPORTACAO, " +
				          "OPF.NR_PRE_FATURA, " +
				          "OPF.DT_EMISSAO DT_EMISSAO_OPF, " +
				          "OPF.DT_VENCIMENTO, " +
				          "OPF.DH_IMPORTACAO, " +
				          "OPF.NM_ARQUIVO, " +		
				          "IOPF.TP_DOCTO_SERVICO TP_DOCUMENTO_SERVICO, " +
				          "NVL(IOPF.SG_FILIAL,' ') SG_FILIAL_DOCTO, " +
				          "IOPF.NR_DOCTO_SERVICO NR_DOCUMENTO_SERVICO, " +
				          "IOPF.DT_EMISSAO DT_EMISSAO_IOPF, " +
				          "IOPF.OB_ITEM_OCORRENCIA_PRE_FATURA OBSERVACAO");
		
		sql.addFrom("OCORRENCIA_PRE_FATURA OPF " +
				    "	INNER JOIN CLIENTE CLI ON OPF.ID_CLIENTE = CLI.ID_CLIENTE " +
				    "   INNER JOIN PESSOA  PES ON CLI.ID_CLIENTE = PES.ID_PESSOA " +
				    "   INNER JOIN ITEM_OCORRENCIA_PRE_FATURA IOPF ON OPF.ID_OCORRENCIA_PRE_FATURA = IOPF.ID_OCORRENCIA_PRE_FATURA");

		sql.addCriteria("CLI.ID_CLIENTE","=", tfm.getLong("cliente.idCliente"));
		
		if( tfm.getYearMonthDay("periodoInicial") != null ){
			if( tfm.getYearMonthDay("periodoFinal") != null ){
				sql.addCustomCriteria("TRUNC(CAST(OPF.DH_IMPORTACAO AS DATE)) BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("periodoInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("periodoFinal"));
			} else {
				sql.addCustomCriteria("TRUNC(CAST(OPF.DH_IMPORTACAO AS DATE)) >= ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("periodoInicial"));
			}
		} else if( tfm.getYearMonthDay("periodoFinal") != null ){
			sql.addCustomCriteria("TRUNC(CAST(OPF.DH_IMPORTACAO AS DATE)) <= ?");
			sql.addCriteriaValue(tfm.getYearMonthDay("periodoFinal"));
		}
		
		sql.addOrderBy("PES.NR_IDENTIFICACAO");
		sql.addOrderBy("OPF.DH_IMPORTACAO ");
		sql.addOrderBy("OPF.NR_PRE_FATURA");
		sql.addOrderBy("IOPF.TP_DOCTO_SERVICO");
		sql.addOrderBy("IOPF.SG_FILIAL");
		sql.addOrderBy("IOPF.NR_DOCTO_SERVICO");		
		
		return sql;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}
