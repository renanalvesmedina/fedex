/**
 * 
 */
package com.mercurio.lms.contasreceber.report;

import static com.mercurio.lms.util.JTFormatUtils.DEFAULT;
import static com.mercurio.lms.util.JTFormatUtils.format;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Luis Carlos Poletto
 * @since 21/05/2007
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.emitirOcorrenciasRetornoBancosService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirOcorrenciasRetornoBancos.jasper"
 *
 */
public class EmitirOcorrenciasRetornoBancosService extends ReportServiceSupport {

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.report.ReportServiceSupport#execute(java.util.Map)
	 */
	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		String tpFormatoRelatorio = ((TypedFlatMap) parameters).getString("tpFormatoRelatorio");
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tpFormatoRelatorio);
		jr.setParameters(parametersReport);
		return jr;
	}
	
	@Override
	public void configReportDomains(ReportDomainConfig config) {
		super.configReportDomains(config);
		config.configDomainField("TP_SITUACAO_BOLETO", "DM_STATUS_BOLETO");
	}
	
	private SqlTemplate getSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplate();
		sql.addProjection("FC.SG_FILIAL", "SG_FILIAL_COBRADORA");
		sql.addProjection("FC.ID_FILIAL", "ID_FILIAL_COBRADORA");
		sql.addProjection("PFC.NM_FANTASIA", "NM_FANTASIA_FILIAL_COBRADORA");
		sql.addProjection("FC.SG_FILIAL || ' - ' ||  PFC.NM_FANTASIA", "NM_FILIAL_COBRADORA");
		sql.addProjection("C.ID_CEDENTE", "ID_CEDENTE");
		sql.addProjection("C.DS_CEDENTE", "DS_CEDENTE");
		sql.addProjection("OB.ID_OCORRENCIA_BANCO", "ID_OCORRENCIA_BANCO");
		sql.addProjection("OB.NR_OCORRENCIA_BANCO", "NR_OCORRENCIA_BANCO");
		sql.addProjection("OB.DS_OCORRENCIA_BANCO", "DS_OCORRENCIA_BANCO");
		sql.addProjection("F.ID_FATURA", "ID_FATURA");
		sql.addProjection("FO.SG_FILIAL", "SG_FILIAL_ORIGEM");
		sql.addProjection("F.NR_FATURA", "NR_FATURA");
		sql.addProjection("B.NR_BOLETO", "NR_BOLETO");
		sql.addProjection("B.TP_SITUACAO_BOLETO", "TP_SITUACAO_BOLETO");
		sql.addProjection("P.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sql.addProjection("P.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sql.addProjection("P.NM_PESSOA", "NM_PESSOA");
		sql.addProjection("TO_CHAR(HB.DH_OCORRENCIA, 'dd/MM/yyyy')", "DH_OCORRENCIA");
		sql.addProjection("HMO.ID_HISTORICO_MOTIVO_OCORRENCIA", "ID_HISTORICO_MOTIVO_OCORRENCIA");
		sql.addProjection("MOB.NR_MOTIVO_OCORRENCIA_BANCO", "NR_MOTIVO_OCORRENCIA_BANCO");
		sql.addProjection("MOB.DS_MOTIVO_OCORRENCIA_BANCO", "DS_MOTIVO_OCORRENCIA_BANCO");
		
		sql.addFrom("FATURA", "F");
		sql.addFrom("BOLETO", "B");
		sql.addFrom("HISTORICO_BOLETO", "HB");
		sql.addFrom("OCORRENCIA_BANCO", "OB");
		sql.addFrom("HISTORICO_MOTIVO_OCORRENCIA", "HMO");
		sql.addFrom("MOTIVO_OCORRENCIA_BANCO", "MOB");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("FILIAL", "FC");
		sql.addFrom("CEDENTE", "C");
		sql.addFrom("FILIAL", "FO");
		sql.addFrom("PESSOA", "PFC");
		sql.addFrom("BANCO", "BAN");
		sql.addFrom("CEDENTE", "CED");
		
		sql.addJoin("F.ID_BOLETO", "B.ID_BOLETO");
		sql.addJoin("B.ID_BOLETO", "HB.ID_BOLETO");
		sql.addJoin("HB.ID_OCORRENCIA_BANCO", "OB.ID_OCORRENCIA_BANCO");
		sql.addJoin("HB.ID_HISTORICO_BOLETO", "HMO.ID_HISTORICO_BOLETO");
		sql.addJoin("HMO.ID_MOTIVO_OCORRENCIA_BANCO", "MOB.ID_MOTIVO_OCORRENCIA_BANCO");
		sql.addJoin("F.ID_CLIENTE", "P.ID_PESSOA");
		sql.addJoin("F.ID_FILIAL_COBRADORA", "FC.ID_FILIAL");
		sql.addJoin("F.ID_CEDENTE", "C.ID_CEDENTE");
		sql.addJoin("F.ID_FILIAL", "FO.ID_FILIAL");
		sql.addJoin("PFC.ID_PESSOA", "FC.ID_FILIAL");
		sql.addJoin("OB.ID_BANCO", "BAN.ID_BANCO");
		sql.addJoin("F.ID_CEDENTE", "CED.ID_CEDENTE");
		
		sql.addOrderBy("FC.SG_FILIAL");
		sql.addOrderBy("CED.DS_CEDENTE");
		sql.addOrderBy("OB.NR_OCORRENCIA_BANCO");
		sql.addOrderBy("FO.SG_FILIAL");
		sql.addOrderBy("F.NR_FATURA");
		sql.addOrderBy("MOB.NR_MOTIVO_OCORRENCIA_BANCO");
		
		sql.addCriteria("F.ID_FILIAL_COBRADORA", "=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("OB.TP_OCORRENCIA_BANCO", "=", "RET");
		sql.addFilterSummary("filialCobranca", criteria.getString("filial.sgFilial") + " - " + criteria.getString("filial.pessoa.nmFantasia"));
		Long idCedente = criteria.getLong("cedente.idCedente");
		if (idCedente != null) {
			sql.addCriteria("F.ID_CEDENTE", "=", idCedente);
			sql.addFilterSummary("banco", criteria.getString("dsCedente"));
		}
		YearMonthDay dhOcorrenciaInicial = criteria.getYearMonthDay("dhOcorrenciaInicial");
		if (dhOcorrenciaInicial != null) {
			sql.addCriteria("HB.DH_OCORRENCIA", ">=", dhOcorrenciaInicial);
			sql.addFilterSummary("periodoInicial", format(dhOcorrenciaInicial, DEFAULT));
		}
		YearMonthDay dhOcorrenciaFinal = criteria.getYearMonthDay("dhOcorrenciaFinal");
		if (dhOcorrenciaFinal != null) {
			sql.addCriteria("HB.DH_OCORRENCIA", "<=", dhOcorrenciaFinal);
			sql.addFilterSummary("periodoFinal", format(dhOcorrenciaFinal, DEFAULT));
		}
		Long idOcorrenciaBanco = criteria.getLong("ocorrenciaBanco.idOcorrenciaBanco");
		if (idOcorrenciaBanco != null) {
			sql.addCriteria("HB.ID_OCORRENCIA_BANCO", "=", idOcorrenciaBanco);
			sql.addFilterSummary("ocorrencia", criteria.getString("dsOcorrenciaBanco"));
		}
		
		return sql;
	}

}
