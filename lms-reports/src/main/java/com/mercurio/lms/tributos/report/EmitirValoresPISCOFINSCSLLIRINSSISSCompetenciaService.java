package com.mercurio.lms.tributos.report;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author 
 *
 * @spring.bean id="lms.tributos.emitirValoresPISCOFINSCSLLIRINSSISSCompetenciaService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirValoresPISCOFINSCSLLIRINSSISSCompetencia.jasper"
 */
public class EmitirValoresPISCOFINSCSLLIRINSSISSCompetenciaService extends ReportServiceSupport {

	private FilialService filialService;
	
	/** 
	 * Método invocado pela EmitirValoresPISCOFINSCSLLIRINSSISSCompetenciaAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {    
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */ 
		Map mapSql = getSqlTemplate(tfm);
		
		SqlTemplate sqlNf = (SqlTemplate)mapSql.get("sqlNf");
		SqlTemplate sqlCon = (SqlTemplate)mapSql.get("sqlCon");
		String sql = sqlNf.getSql() + "\n UNION \n" + sqlCon.getSql(); 
		
		/** Juntar os aparmetros dos 2 sql */
		Object[] sqlNfCriterios = sqlNf.getCriteria();
		Object[] sqlConCriterios = sqlCon.getCriteria();
		Object[] results = new Object[sqlNfCriterios.length + sqlConCriterios.length];		
		System.arraycopy(sqlNfCriterios, 0, results, 0, sqlNfCriterios.length);
		System.arraycopy(sqlConCriterios, 0, results, sqlNfCriterios.length, sqlConCriterios.length);
		
		/** Concatena os dois SqlTemplate com union e invoca o método execute passando tmb os critérios */
		JRReportDataObject jr = executeQuery(sql, results);
		
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", sqlNf.getFilterSummary());
		
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio")); 
		
		jr.setParameters(parametersReport); 
		
		if ( parameters.get("soTotais").equals("true"))
			this.setReportName("com/mercurio/lms/tributos/report/emitirValoresPISCOFINSCSLLIRINSSISSCompetenciaTotais.jasper");
		else 
			this.setReportName("com/mercurio/lms/tributos/report/emitirValoresPISCOFINSCSLLIRINSSISSCompetencia.jasper");

		return jr;
	}
	
	
	/**
	 * Configura variáveis do relatório, para receberem valores não abreviados do domínio 
	 * Ex: situação = I  -  vai ser configurado, e exibido no relatório como Inativo
	 */
	public void configReportDomains(ReportDomainConfig config) {		
		super.configReportDomains(config);
	}

	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private Map getSqlTemplate(TypedFlatMap tfm) throws Exception{
		Map sqlTemplates = new LinkedHashMap();
		
		/** Monta o sql para NotaFical */
		SqlTemplate sqlNf = this.createSqlTemplate(); 

		sqlNf.addProjection(" 'NFS' ","TP_DOCUMENTO");
		sqlNf.addProjection("FI.SG_FILIAL || ' - ' || PE_FI.NM_FANTASIA", "FILIAL");
		sqlNf.addProjection("NF.NR_NOTA_FISCAL_SERVICO","NOTA_FISCAL");
		sqlNf.addProjection("FI.SG_FILIAL","SG_FILIAL");
		sqlNf.addProjection("DS.DH_EMISSAO", "EMISSAO");
		sqlNf.addProjection("FA.DT_VENCIMENTO", "VENCIMENTO");
		sqlNf.addProjection("PE.NM_PESSOA", "NM_PESSOA");
		sqlNf.addProjection("PE.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sqlNf.addProjection("PE.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sqlNf.addProjection("DECODE(NF.TP_SITUACAO_NF, 'C', 0, DS.VL_TOTAL_DOC_SERVICO)", "VALOR_NOTA");
		
		sqlNf.addProjection("NVL ((SELECT decode(nf.tp_situacao_nf, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	nf.id_nota_fiscal_servico = im.id_nota_fiscal_servico " +
							"         AND im.tp_imposto = 'PI'), 0) ", "VALOR_PIS");
		
		sqlNf.addProjection("NVL ((SELECT decode(nf.tp_situacao_nf, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	nf.id_nota_fiscal_servico = im.id_nota_fiscal_servico " +
							"         AND im.tp_imposto = 'CO'), 0) ", "VALOR_COFINS");
		
		sqlNf.addProjection("NVL ((SELECT decode(nf.tp_situacao_nf, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	nf.id_nota_fiscal_servico = im.id_nota_fiscal_servico " +
							"         AND im.tp_imposto = 'CS'), 0) ", "VALOR_CSLL");
		
		sqlNf.addProjection("NVL ((SELECT decode(nf.tp_situacao_nf, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	nf.id_nota_fiscal_servico = im.id_nota_fiscal_servico " +
							"         AND im.tp_imposto = 'IR'), 0) ", "VALOR_IR");
		
		sqlNf.addProjection("NVL ((SELECT decode(nf.tp_situacao_nf, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	nf.id_nota_fiscal_servico = im.id_nota_fiscal_servico " +
							"         AND im.tp_imposto = 'IN'), 0) ", "VALOR_INSS");
		
		sqlNf.addProjection("NVL ((SELECT DECODE(IM.BL_RETENCAO_TOMADOR_SERVICO, 'S', DECODE(NF.TP_SITUACAO_NF, 'C', 0, IM.VL_IMPOSTO), 0) " +
							" FROM 		imposto_servico im " +
							" WHERE 	nf.id_nota_fiscal_servico = im.id_nota_fiscal_servico " +
							" 		  AND im.tp_imposto = 'IS'), 0) ", "VALOR_ISS");
		
		sqlNf.addFrom("NOTA_FISCAL_SERVICO NF " +
				      "  INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = NF.ID_NOTA_FISCAL_SERVICO " +
				      "  LEFT OUTER JOIN CLIENTE CL ON DS.ID_CLIENTE_REMETENTE = CL.ID_CLIENTE " +
				      "  LEFT OUTER JOIN PESSOA PE ON PE.ID_PESSOA = CL.ID_CLIENTE " +
				      "  INNER JOIN FILIAL FI ON FI.ID_FILIAL = DS.ID_FILIAL_ORIGEM " +
				      "  INNER JOIN PESSOA PE_FI ON PE_FI.ID_PESSOA = FI.ID_FILIAL " +
				      "  INNER JOIN DEVEDOR_DOC_SERV_FAT DD ON DD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +				      
				      "  LEFT OUTER JOIN FATURA FA ON (DD.ID_FATURA = FA.ID_FATURA AND FA.TP_SITUACAO_FATURA <> 'CA')");
		
		if (tfm.get("filial.idFilial") != null && StringUtils.isNotBlank(tfm.getString("filial.idFilial"))){
			Filial filial = this.getFilialService().findById(Long.valueOf(tfm.getString("filial.idFilial")));
			sqlNf.addCriteria("FI.ID_FILIAL","=",tfm.getLong("filial.idFilial"));
			sqlNf.addFilterSummary("filial", filial.getSgFilial() + " - " + filial.getPessoa().getNmFantasia());
		}
		
		if (tfm.get("competencia") != null && StringUtils.isNotBlank(tfm.getString("competencia"))){	
			sqlNf.addCriteria("to_date(to_char(nvl(DS.DH_EMISSAO,to_date('01014000','ddmmyyyy')),'yyyy-mm')||'-01', 'yyyy-mm-dd')","=",tfm.getYearMonthDay("competencia"));
            sqlNf.addFilterSummary("competencia", JTFormatUtils.format(tfm.getYearMonthDay("competencia"), JTFormatUtils.MONTHYEAR, JTFormatUtils.SHORT));
		}
		
		sqlNf.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ('NFS', 'NFT', 'NSE', 'NTE')");

		/** Adiciona sqlNf no Map */
		sqlTemplates.put("sqlNf", sqlNf);
		
		/** Monta o sql para Conhecimento */
		SqlTemplate sqlCon = this.createSqlTemplate();

		sqlCon.addProjection("VD.VL_VALOR_DOMINIO","TP_DOCUMENTO");
		sqlCon.addProjection("FI.SG_FILIAL || ' - ' || PE_FI.NM_FANTASIA", "FILIAL");
		sqlCon.addProjection("CO.NR_CONHECIMENTO", "NOTA_FISCAL");
		sqlCon.addProjection("FI.SG_FILIAL","SG_FILIAL"); 
		sqlCon.addProjection("DS.DH_EMISSAO", "EMISSAO");
		sqlCon.addProjection("FA.DT_VENCIMENTO", "VENCIMENTO");
		sqlCon.addProjection("PE.NM_PESSOA", "NM_PESSOA");
		sqlCon.addProjection("PE.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
		sqlCon.addProjection("PE.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
		sqlCon.addProjection("DECODE(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, DS.VL_TOTAL_DOC_SERVICO)", "VALOR_NOTA");
		
		sqlCon.addProjection("NVL ((SELECT decode(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	CO.ID_CONHECIMENTO = IM.ID_CONHECIMENTO " +
							"          AND im.tp_imposto = 'PI'), 0) ", "VALOR_PIS");
		
		sqlCon.addProjection("NVL ((SELECT decode(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	CO.ID_CONHECIMENTO = IM.ID_CONHECIMENTO " +
							" 		   AND im.tp_imposto = 'CO'), 0) ", "VALOR_COFINS");
		
		sqlCon.addProjection("NVL ((SELECT decode(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	CO.ID_CONHECIMENTO = IM.ID_CONHECIMENTO " +
							" 		   AND im.tp_imposto = 'CS'), 0) ", "VALOR_CSLL");
		
		sqlCon.addProjection("NVL ((SELECT decode(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	CO.ID_CONHECIMENTO = IM.ID_CONHECIMENTO " +
							" 		   AND im.tp_imposto = 'IR'), 0) ", "VALOR_IR");
		
		sqlCon.addProjection("NVL ((SELECT decode(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, im.vl_imposto) " +
							" FROM 		imposto_servico im " +
							" WHERE 	CO.ID_CONHECIMENTO = IM.ID_CONHECIMENTO " +
							"		   AND im.tp_imposto = 'IN'), 0) ", "VALOR_INSS");
		
		sqlCon.addProjection("NVL ((SELECT DECODE(IM.BL_RETENCAO_TOMADOR_SERVICO, 'S', DECODE(CO.TP_SITUACAO_CONHECIMENTO, 'C', 0, IM.VL_IMPOSTO), 0) " +
							" FROM 		imposto_servico im " +
							" WHERE 	CO.ID_CONHECIMENTO = IM.ID_CONHECIMENTO " +
							" 		   AND im.tp_imposto = 'IS'), 0) ", "VALOR_ISS");
		
		sqlCon.addFrom(" CONHECIMENTO CO " +
			           "  INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO " +
				       "  LEFT OUTER JOIN CLIENTE CL ON DS.ID_CLIENTE_REMETENTE = CL.ID_CLIENTE " +
				       "  LEFT OUTER JOIN PESSOA PE ON PE.ID_PESSOA = CL.ID_CLIENTE " +
				       "  INNER JOIN FILIAL FI ON FI.ID_FILIAL = DS.ID_FILIAL_ORIGEM " +
				       "  INNER JOIN PESSOA PE_FI ON PE_FI.ID_PESSOA = FI.ID_FILIAL " +
				       "  INNER JOIN DEVEDOR_DOC_SERV_FAT DD ON DD.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +				      
				       "  LEFT OUTER JOIN FATURA FA ON (DD.ID_FATURA = FA.ID_FATURA AND FA.TP_SITUACAO_FATURA <> 'CA')" +
				       " , DOMINIO D INNER JOIN VALOR_DOMINIO VD ON (D.ID_DOMINIO = VD.ID_DOMINIO AND D.NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO')");
		
		/** Insere os filtros no sql */     
		if (tfm.get("filial.idFilial") != null && StringUtils.isNotBlank(tfm.getString("filial.idFilial"))){
			sqlCon.addCriteria("FI.ID_FILIAL","=",tfm.get("filial.idFilial"),Long.class);
		}
		sqlCon.addCustomCriteria("VD.VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO");		
		
		if (tfm.get("competencia") != null && StringUtils.isNotBlank(tfm.getString("competencia"))){
			sqlCon.addCriteria("to_date(to_char(nvl(DS.DH_EMISSAO,to_date('01014000','ddmmyyyy')),'yyyy-mm')||'-01', 'yyyy-mm-dd')","=",tfm.getYearMonthDay("competencia"));	
		}
		
		sqlCon.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ('NFS', 'NFT', 'NSE', 'NTE')");

		sqlCon.addOrderBy("NOTA_FISCAL");		
		
		/** Adiciona sqlCon no Map */
		sqlTemplates.put("sqlCon", sqlCon);
		
		return sqlTemplates;

	}


	public FilialService getFilialService() {
		return filialService;
	}


	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

}
