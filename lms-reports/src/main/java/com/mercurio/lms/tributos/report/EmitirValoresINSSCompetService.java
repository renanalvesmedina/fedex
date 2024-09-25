package com.mercurio.lms.tributos.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Hector junior
 *
 * @spring.bean id="lms.tributos.emitirValoresINSSCompetService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirValoresINSSCompet.jasper"
 */
public class EmitirValoresINSSCompetService extends ReportServiceSupport {

    /**
     * Executa a busca dos dados para gerar os relatórios de Valores de Inss de Competência
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
        
        if( parameters.get("blExibirProprietarios").equals("true") ){
            this.setReportName("com/mercurio/lms/tributos/report/emitirValoresINSSCompetPorCPF.jasper");
        } else {
            this.setReportName("com/mercurio/lms/tributos/report/emitirValoresINSSCompet.jasper");
        }        
        
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
        
        SqlTemplate subSql = createSqlTemplate();
        
        subSql.addProjection("SUM(DES.VL_INSS)");
        subSql.addFrom("DESCONTO_INSS_CARRETEIRO","DES");
        subSql.addJoin("DES.ID_PROPRIETARIO","PRO.ID_PROPRIETARIO");
        
        subSql.addJoin("TO_CHAR(DES.DT_EMIS_RECIBO, 'YYYYMM')","TO_CHAR(REC.DH_EMISSAO,'YYYYMM')");
        
		SqlTemplate sql = this.createSqlTemplate();
        
		sql.addProjection("FIL.ID_FILIAL");
		sql.addProjection("FIL.SG_FILIAL");
		sql.addProjection("PES_FILIAL.NM_FANTASIA");
        sql.addProjection("PES.NR_IDENTIFICACAO");
        sql.addProjection("PES.TP_IDENTIFICACAO");
        sql.addProjection("PES.NM_PESSOA");        
        sql.addProjection("PRO.NR_PIS","NR_INSS");
        sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOG_PRO.DS_TIPO_LOGRADOURO_I"), "DS_TIPO_LOGRADOURO");
        sql.addProjection("END_PRO.DS_ENDERECO");
        sql.addProjection("END_PRO.NR_ENDERECO");
        sql.addProjection("END_PRO.DS_COMPLEMENTO");
        sql.addProjection("0","ENDERECO");
        sql.addProjection("MUN_PRO.NM_MUNICIPIO");
        sql.addProjection("UNI_PRO.SG_UNIDADE_FEDERATIVA","UF");
        
        sql.addProjection("TO_CHAR(REC.DH_EMISSAO, 'MM/YYYY') AS COMPETENCIA");
        sql.addProjection("SUM(REC.VL_BRUTO) AS VL_BRUTO");
        sql.addProjection(montaSubQueryValorBase() + " AS VL_BASE");
        
        sql.addProjection("SUM(REC.VL_INSS) AS VL_INSS");
        sql.addProjection("MAX((" + subSql.getSql() + ")) AS VL_OUTRAS_EMPRESAS");
        
        sql.addFrom("RECIBO_FRETE_CARRETEIRO","REC");
        sql.addFrom("PROPRIETARIO","PRO");
        sql.addFrom("PESSOA","PES");
        sql.addFrom("FILIAL","FIL");
        sql.addFrom("PESSOA","PES_FILIAL");
//--  Referente ao endereço do proprietário
        sql.addFrom("ENDERECO_PESSOA","END_PRO");
        sql.addFrom("TIPO_ENDERECO_PESSOA","TIP_PRO");
        sql.addFrom("TIPO_LOGRADOURO","LOG_PRO");
        sql.addFrom("MUNICIPIO","MUN_PRO");
        sql.addFrom("UNIDADE_FEDERATIVA","UNI_PRO");
//      --  Referente ao endereço da filial        
        sql.addFrom("ENDERECO_PESSOA","END_FIL");
        sql.addFrom("TIPO_ENDERECO_PESSOA","TIP_FIL");
        sql.addFrom("TIPO_LOGRADOURO","LOG_FIL");
        sql.addFrom("MUNICIPIO","MUN_FIL");
        sql.addFrom("UNIDADE_FEDERATIVA","UNI_FIL");
        sql.addFrom("PAIS","PAI_FIL");
        
        sql.addJoin("REC.ID_PROPRIETARIO","PRO.ID_PROPRIETARIO");
        sql.addJoin("PRO.ID_PROPRIETARIO","PES.ID_PESSOA");
        sql.addJoin("REC.ID_FILIAL","FIL.ID_FILIAL");
        sql.addJoin("PES.TP_PESSOA","'F'");
        sql.addJoin("FIL.ID_FILIAL","PES_FILIAL.ID_PESSOA");
//      Referente ao endereço do proprietário
        sql.addJoin("PRO.ID_PROPRIETARIO","END_PRO.ID_PESSOA");
        sql.addJoin("END_PRO.ID_ENDERECO_PESSOA","TIP_PRO.ID_ENDERECO_PESSOA");
        sql.addJoin("END_PRO.ID_TIPO_LOGRADOURO","LOG_PRO.ID_TIPO_LOGRADOURO");
        sql.addJoin("END_PRO.ID_MUNICIPIO","MUN_PRO.ID_MUNICIPIO");
        sql.addJoin("MUN_PRO.ID_UNIDADE_FEDERATIVA","UNI_PRO.ID_UNIDADE_FEDERATIVA");
        sql.addJoin("TIP_PRO.TP_ENDERECO","'RES'");
        sql.addCustomCriteria("(  END_PRO.DT_VIGENCIA_INICIAL <= ? AND " +
                              "(  END_PRO.DT_VIGENCIA_FINAL >= ? ))");
        sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
        sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
//      Referente ao endereço da filial
        sql.addJoin("FIL.ID_FILIAL","END_FIL.ID_PESSOA");
        sql.addJoin("END_FIL.ID_ENDERECO_PESSOA","TIP_FIL.ID_ENDERECO_PESSOA");
        sql.addJoin("END_FIL.ID_TIPO_LOGRADOURO","LOG_FIL.ID_TIPO_LOGRADOURO");
        sql.addJoin("END_FIL.ID_MUNICIPIO","MUN_FIL.ID_MUNICIPIO");
        sql.addJoin("MUN_FIL.ID_UNIDADE_FEDERATIVA","UNI_FIL.ID_UNIDADE_FEDERATIVA");
        sql.addJoin("UNI_FIL.ID_PAIS","PAI_FIL.ID_PAIS");
        sql.addJoin("PAI_FIL.SG_PAIS","'BRA'");
        sql.addJoin("TIP_FIL.TP_ENDERECO","'COM'");
        
        sql.addCustomCriteria("REC.TP_SITUACAO_RECIBO IN ('EM', 'ER', 'LI', 'PA')");
        
        sql.addCustomCriteria("(  END_FIL.DT_VIGENCIA_INICIAL <= ? AND" +
                              "(  END_FIL.DT_VIGENCIA_FINAL >= ?))");        
        sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
        sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
        
        if( !tfm.getString("competenciaInicial" ).equals("") && !tfm.getString("competenciaFinal").equals("") ){
            
            YearMonthDay dtInicial = tfm.getYearMonthDay("competenciaInicial");
            YearMonthDay dtFinal = tfm.getYearMonthDay("competenciaFinal");
            
            sql.addCustomCriteria("REC.DH_EMISSAO BETWEEN ? AND ? ");
            
            sql.addCriteriaValue(dtInicial.toDateTimeAtMidnight());
            sql.addCriteriaValue(dtFinal.plusMonths(1).toDateTimeAtMidnight());
            sql.addFilterSummary("competenciaInicial",JTFormatUtils.format(dtInicial,JTFormatUtils.MONTHYEAR,JTFormatUtils.SHORT));
            sql.addFilterSummary("competenciaFinal",JTFormatUtils.format(dtFinal,JTFormatUtils.MONTHYEAR,JTFormatUtils.SHORT));
        }
        
        if( !tfm.getString("filial.idFilial").equals("") ){
        	sql.addCriteria("REC.ID_FILIAL","=",tfm.getLong("filial.idFilial"));
            sql.addFilterSummary("filial",tfm.getString("siglaFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia"));
        }
        
        if( !tfm.getString("proprietario.idProprietario").equals("") ){
            sql.addCriteria("PRO.ID_PROPRIETARIO","=",tfm.getLong("proprietario.idProprietario"));
            sql.addFilterSummary("proprietario",FormatUtils.formatCPF(tfm.getString("cpfProprietario")) + " - " + tfm.getString("proprietario.pessoa.nmPessoa"));
        }
        
        sql.addOrderBy("FIL.SG_FILIAL");
        sql.addOrderBy("COMPETENCIA");
        sql.addOrderBy("PES.NR_IDENTIFICACAO");
        
        sql.addGroupBy("FIL.ID_FILIAL");
        sql.addGroupBy("FIL.SG_FILIAL");
        sql.addGroupBy("PES_FILIAL.NM_FANTASIA");
        sql.addGroupBy("PES.NR_IDENTIFICACAO");
        sql.addGroupBy("PES.TP_IDENTIFICACAO");
        sql.addGroupBy("PES.NM_PESSOA");        
        sql.addGroupBy("PRO.NR_PIS");
        sql.addGroupBy(PropertyVarcharI18nProjection.createProjection("LOG_PRO.DS_TIPO_LOGRADOURO_I"));
        sql.addGroupBy("END_PRO.DS_ENDERECO");
        sql.addGroupBy("END_PRO.NR_ENDERECO");
        sql.addGroupBy("END_PRO.DS_COMPLEMENTO");
        sql.addGroupBy("MUN_PRO.NM_MUNICIPIO");
        sql.addGroupBy("UNI_PRO.SG_UNIDADE_FEDERATIVA");
        sql.addGroupBy("TO_CHAR(REC.DH_EMISSAO, 'MM/YYYY')");
		
		return sql;
	}

	/**
	 * Monta a query da soma do valor Base de cálculo INSS
	 * @return Subquery montada
	 */
	private String montaSubQueryValorBase() {
		
		String query = "SUM(((REC.VL_BRUTO) * " +
				       "	 (select ALIN.PC_BCALC_REDUZIDA from ALIQUOTA_INSS_PESSOA_FISICA ALIN " +
				       "      where alin.dt_inicio_vigencia = (select max(alin2.dt_inicio_vigencia) " +
				       "                                      from ALIQUOTA_INSS_PESSOA_FISICA ALIN2 " +
				       "                                      where alin.dt_inicio_vigencia <= REC.DH_EMISSAO))/100))"; 
		
		return query;
	}

}
