package com.mercurio.lms.tributos.report;

import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Bruno Zaccolo
 * Alterado por : José Rodrigo Moraes em 20/07/2006
 *
 * @spring.bean id="lms.tributos.emitirValoresISSCompetenciaService"
 * @spring.property name="reportName" value="com/mercurio/lms/tributos/report/emitirValoresISSCompetencia.jasper"
 */
public class EmitirValoresISSCompetenciaExcelService extends ReportServiceSupport {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		Map parametersReport = new HashMap();

		SqlTemplate sqlTemplate = createSqlTemplate(tfm);
		
		JRReportDataObject jr = executeQuery(sqlTemplate.getSql(), sqlTemplate.getCriteria()); 
		
		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", parameters);
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());		
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,JRReportDataObject.EXPORT_XLS);		
		
		jr.setParameters(parametersReport);
		 
		this.setReportName("com/mercurio/lms/tributos/report/emitirValoresISSCompetenciaExcel.jasper");
	
		return jr;
	}
	
	private SqlTemplate createSqlTemplate(TypedFlatMap map) {
		SqlTemplate sqlTemplate = this.createSqlTemplate();
		montaProjecao(sqlTemplate);
		
		sqlTemplate.addFrom(getFrom());
		
		montaCriteria(map, sqlTemplate);
		
		sqlTemplate.addOrderBy("FORIG.SG_FILIAL,DS.NR_DOCTO_SERVICO,MINC.NM_MUNICIPIO");
		
		return sqlTemplate;
	}

	private void montaProjecao(SqlTemplate sqlTemplate) {
		sqlTemplate.addProjection("to_char(TRUNC(DS.DH_EMISSAO, 'DD'),'DD/MM/YYYY')","COMPETENCIA");
		sqlTemplate.addProjection("FORIG.SG_FILIAL");
		sqlTemplate.addProjection("(PFIL.NR_INSCRICAO_MUNICIPAL)", "NR_INSCRICAO_MUNICIPAL_PFIL");
		sqlTemplate.addProjection("MFIL.NM_MUNICIPIO" , "NM_MUNICIPIO_MFIL");
		sqlTemplate.addProjection("UFFIL.SG_UNIDADE_FEDERATIVA" , "SG_UNIDADE_FEDERATIVA_UFFIL");
		sqlTemplate.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) "
				+ "FROM VALOR_DOMINIO "
				+ "WHERE ID_DOMINIO IN ("
				+ "		SELECT ID_DOMINIO "
				+ "		FROM DOMINIO "
				+ "		WHERE NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO') "
				+ "AND VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO)", "TP_DOCUMENTO_SERVICO");
		sqlTemplate.addProjection("DS.NR_DOCTO_SERVICO");
		sqlTemplate.addProjection("ME.NR_FISCAL_RPS");
		sqlTemplate.addProjection("ME.NR_DOCUMENTO_ELETRONICO");
		sqlTemplate.addProjection("TO_CHAR(DS.DH_EMISSAO, 'DD/MM/YYYY')", "DH_EMISSAO");
		sqlTemplate.addProjection("NVL(VI18N(SERVAD.DS_SERVICO_ADICIONAL_I),"+
		           "(SELECT ST.DS_SERVICO_TRIBUTO"+
		           "   FROM PARAMETRO_GERAL PG,"+
		           "        SERVICO_TRIBUTO ST"+
		           "  WHERE PG.NM_PARAMETRO_GERAL = 'ID_SERVICO_TRIBUTO_NFT'"+
		           "    AND ST.ID_SERVICO_TRIBUTO = TO_NUMBER(PG.DS_CONTEUDO)))", "SERVICO");
		sqlTemplate.addProjection("PDEV.TP_IDENTIFICACAO");
		sqlTemplate.addProjection("PDEV.NR_IDENTIFICACAO");
		sqlTemplate.addProjection("PDEV.NM_PESSOA");
		sqlTemplate.addProjection("PDEV.NR_INSCRICAO_MUNICIPAL");
		sqlTemplate.addProjection("VI18N(TLDEV.DS_TIPO_LOGRADOURO_I)", "TIPO_LOGRADOURO");
		sqlTemplate.addProjection("EPDEV.DS_ENDERECO");
		sqlTemplate.addProjection("EPDEV.NR_ENDERECO");
		sqlTemplate.addProjection("EPDEV.DS_COMPLEMENTO");
		sqlTemplate.addProjection("EPDEV.DS_BAIRRO");
		sqlTemplate.addProjection("EPDEV.NR_CEP");
		sqlTemplate.addProjection("MDEV.NM_MUNICIPIO" , "NM_MUNICIPIO_MDEV");
		sqlTemplate.addProjection("UFDEV.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA_UFDEV");
		sqlTemplate.addProjection("CAST(NVL(ISER.VL_BASE_CALCULO, 0) AS NUMBER(18, 2))", "VL_BASE_CALCULO");
		sqlTemplate.addProjection("CAST(NVL(ISER.PC_ALIQUOTA, 0) AS NUMBER(18, 2))", "PC_ALIQUOTA");
		sqlTemplate.addProjection("(DECODE(NVL(ISER.BL_RETENCAO_TOMADOR_SERVICO, 'N'), 'N', NVL(ISER.VL_IMPOSTO, 0),'S', 0))" , "VALOR_ISS");
  	    sqlTemplate.addProjection("(DECODE(NVL(ISER.BL_RETENCAO_TOMADOR_SERVICO, 'N'), 'S', NVL(ISER.VL_IMPOSTO, 0), 'N', 0))", "VALOR_ISS_RETIDO");
		sqlTemplate.addProjection("MINC.NM_MUNICIPIO" ,"NM_MUNICIPIO_MINC");
		sqlTemplate.addProjection("UFINC.SG_UNIDADE_FEDERATIVA", "SG_UNIDADE_FEDERATIVA_UFINC");
		sqlTemplate.addProjection("DEV.VL_DEVIDO");
		sqlTemplate.addProjection("DS.VL_TOTAL_DOC_SERVICO");
		sqlTemplate.addProjection("ME.DS_OBSERVACAO");
		sqlTemplate.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I)"+
		         " FROM VALOR_DOMINIO"+
		         " WHERE ID_DOMINIO IN"+
		         "      (SELECT ID_DOMINIO"+
		         "         FROM DOMINIO"+
		         "        WHERE NM_DOMINIO = 'DM_STATUS_CONHECIMENTO')"+
		         "  AND VL_VALOR_DOMINIO ="+
		         "      NVL(CO.TP_SITUACAO_CONHECIMENTO, NFS.TP_SITUACAO_NF))" , "SITUACAO_DOCUMENTO");

	}

	private void montaCriteria(TypedFlatMap map, SqlTemplate sqlTemplate) {		
		sqlTemplate.addCustomCriteria("DS.ID_DOCTO_SERVICO = ISER.ID_DOCTO_SERVICO(+)");
		sqlTemplate.addCustomCriteria("DS.ID_DOCTO_SERVICO = ME.ID_DOCTO_SERVICO(+)");
		sqlTemplate.addCustomCriteria("DS.ID_FILIAL_ORIGEM = FORIG.ID_FILIAL");
		sqlTemplate.addCustomCriteria("FORIG.ID_FILIAL = PFIL.ID_PESSOA(+)");
		sqlTemplate.addCustomCriteria("PFIL.ID_ENDERECO_PESSOA = EPFIL.ID_ENDERECO_PESSOA(+)");
		sqlTemplate.addCustomCriteria("EPFIL.ID_MUNICIPIO = MFIL.ID_MUNICIPIO(+)");
		sqlTemplate.addCustomCriteria("MFIL.ID_UNIDADE_FEDERATIVA = UFFIL.ID_UNIDADE_FEDERATIVA(+)");
		sqlTemplate.addCustomCriteria("DS.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO(+)");
		sqlTemplate.addCustomCriteria("DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO(+)");
		sqlTemplate.addCustomCriteria("DS.ID_SERVICO = SER.ID_SERVICO(+)");
		sqlTemplate.addCustomCriteria("DS.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO(+)");
		sqlTemplate.addCustomCriteria("DEV.ID_CLIENTE = PDEV.ID_PESSOA(+)");
		sqlTemplate.addCustomCriteria("PDEV.ID_ENDERECO_PESSOA = EPDEV.ID_ENDERECO_PESSOA(+)");
		sqlTemplate.addCustomCriteria("EPDEV.ID_TIPO_LOGRADOURO = TLDEV.ID_TIPO_LOGRADOURO(+)");
		sqlTemplate.addCustomCriteria("EPDEV.ID_MUNICIPIO = MDEV.ID_MUNICIPIO(+)");
		sqlTemplate.addCustomCriteria("MDEV.ID_UNIDADE_FEDERATIVA = UFDEV.ID_UNIDADE_FEDERATIVA(+)");
		sqlTemplate.addCustomCriteria("ISER.ID_MUNICIPIO_INCIDENCIA = MINC.ID_MUNICIPIO(+)");
		sqlTemplate.addCustomCriteria("MINC.ID_UNIDADE_FEDERATIVA = UFINC.ID_UNIDADE_FEDERATIVA(+)");
		sqlTemplate.addCustomCriteria("DS.ID_DOCTO_SERVICO = SERVADDS.ID_DOCTO_SERVICO(+)");
		sqlTemplate.addCustomCriteria("SERVADDS.ID_SERVICO_ADICIONAL = SERVAD.ID_SERVICO_ADICIONAL(+)");
		sqlTemplate.addCustomCriteria("ISER.TP_IMPOSTO(+) = 'IS'");
		sqlTemplate.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ('NFS', 'NFT', 'NSE', 'NTE')");

		if (map.getLong("idFilial") != null) {
			sqlTemplate.addCustomCriteria("DS.ID_FILIAL_ORIGEM = ?");
			sqlTemplate.addCriteriaValue(map.getLong("idFilial"));
			sqlTemplate.addFilterSummary("filialOrigem2", map.getString("sgFilial"));
		}
		
		if (map.getLong("idMunicipioServico") != null) {
			sqlTemplate.addCustomCriteria("ISER.ID_MUNICIPIO_INCIDENCIA = ?");
			sqlTemplate.addCriteriaValue(map.getLong("idMunicipioServico"));
			sqlTemplate.addFilterSummary("municipioIncidencia", map.getString("nmMunicipio"));
		}
		
		sqlTemplate.addCriteria("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", ">=", map.getYearMonthDay("periodoInicial"));
		sqlTemplate.addCriteria("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", "<=", map.getYearMonthDay("periodoFinal"));
		
		sqlTemplate.addFilterSummary("periodoInicial",JTFormatUtils.format(map.getYearMonthDay("periodoInicial"), JTFormatUtils.SHORT, JTFormatUtils.YEARMONTHDAY));
		sqlTemplate.addFilterSummary("periodoFinal",JTFormatUtils.format(map.getYearMonthDay("periodoFinal"), JTFormatUtils.SHORT, JTFormatUtils.YEARMONTHDAY));

	}
	
	private String getFrom() {
		StringBuilder from = new StringBuilder();
		from.append("DOCTO_SERVICO                DS");
		from.append(",MONITORAMENTO_DOC_ELETRONICO ME ");
		from.append(",FILIAL                       FORIG ");
		from.append(",PESSOA                       PFIL ");
		from.append(",ENDERECO_PESSOA              EPFIL ");
		from.append(",MUNICIPIO                    MFIL ");
		from.append(",UNIDADE_FEDERATIVA           UFFIL ");
		from.append(",CONHECIMENTO                 CO ");
		from.append(",NOTA_FISCAL_SERVICO          NFS ");
		from.append(",(SELECT ISER.*, NVL(ISER.ID_CONHECIMENTO, ISER.ID_NOTA_FISCAL_SERVICO) ID_DOCTO_SERVICO "
				     + " FROM IMPOSTO_SERVICO ISER) ISER");
		from.append(",SERVICO                      SER ");
		from.append(",DEVEDOR_DOC_SERV             DEV ");
		from.append(",PESSOA                       PDEV ");
		from.append(",ENDERECO_PESSOA              EPDEV ");
		from.append(",TIPO_LOGRADOURO              TLDEV ");
		from.append(",MUNICIPIO                    MDEV ");
		from.append(",UNIDADE_FEDERATIVA           UFDEV ");
		from.append(",MUNICIPIO                    MINC ");
		from.append(",UNIDADE_FEDERATIVA           UFINC ");
		from.append(",SERV_ADICIONAL_DOC_SERV      SERVADDS ");
		from.append(",SERVICO_ADICIONAL            SERVAD ");
		return from.toString();
	}
	
}


