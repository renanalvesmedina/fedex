package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.contasreceber.swt.emitirDACTEFaturasResumidoService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirDACTEFaturasResumido.jasper"
 */
public class EmitirDACTEFaturasResumidoService extends ReportServiceSupport {

    /**
     * Método responsável por gerar o relatório. 
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		
		SqlTemplate sql = getSqlTemplate((TypedFlatMap) parameters);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, "pdf");
		jr.setParameters(parametersReport);
		return jr;
	}
	
	
	
    private SqlTemplate getSqlTemplate(TypedFlatMap parameters) throws Exception { 
    	SqlTemplate sql = createSqlTemplate();

    	// SELECT
    	sql.addProjection("PFIL.NM_PESSOA", "NM_PESSOA_FILIAL");
    	sql.addProjection("VI18N(TLFIL.DS_TIPO_LOGRADOURO_I) || ' ' || EPFIL.DS_ENDERECO || ', ' || EPFIL.NR_ENDERECO || DECODE(EPFIL.DS_COMPLEMENTO, NULL, '', ' - ' || EPFIL.DS_COMPLEMENTO)", "ENDERECO");
    	sql.addProjection("MFIL.NM_MUNICIPIO", "MUNICIPIO");
    	sql.addProjection("UFFIL.SG_UNIDADE_FEDERATIVA", "UNIDADE_FEDERATIVA");
    	sql.addProjection("PFIL.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_FILIAL");
    	sql.addProjection("PFIL.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_FILIAL");
    	sql.addProjection(getSqlIncricaoEstadual(), "NR_INSCRICAO_ESTADUAL");
    	sql.addProjection(getSqlTelefone(), "TELEFONE");
    	sql.addProjection("FFAT.SG_FILIAL", "SG_FILIAL");
    	sql.addProjection("F.NR_FATURA", "NR_FATURA");
    	sql.addProjection("NVL(B.DT_VENCIMENTO, F.DT_VENCIMENTO)", "DT_VENCIMENTO");
    	sql.addProjection("B.NR_BOLETO", "NR_BOLETO");
    	sql.addProjection("B.NR_AGENCIA_BANCARIA", "NR_AGENCIA_BANCARIA");
    	sql.addProjection("B.CD_CEDENTE", "CD_CEDENTE");
    	sql.addProjection("F.DT_EMISSAO", "DT_EMISSAO");
    	sql.addProjection("F.VL_TOTAL", "VL_TOTAL");
    	sql.addProjection("F.VL_DESCONTO", "VL_DESCONTO");
    	sql.addProjection("F.VL_TOTAL - F.VL_DESCONTO", "VL_APAGAR");
    	sql.addProjection("NVL(TO_CHAR(B.NR_CARTEIRA), 'CSB')", "NR_CARTEIRA");
    	sql.addProjection("PCLI.NM_PESSOA", "NM_PESSOA");
    	sql.addProjection("PCLI.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_CLIENTE");
    	sql.addProjection("PCLI.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_CLIENTE");
    	sql.addProjection(getSqlTipoDocumento(), "TIPO_DOCUMENTO");
    	sql.addProjection("FORIG.SG_FILIAL", "SG_FILIAL_ORIGEM");
    	sql.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCTO_SERVICO");
    	sql.addProjection("DS.DH_EMISSAO", "DH_EMISSAO");
    	sql.addProjection("PDEST.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_DEST");
    	sql.addProjection("PDEST.NR_IDENTIFICACAO", "NR_IDENTIFICACAO_DEST");
    	sql.addProjection("PDEST.NM_PESSOA", "NM_PESSOA_DEST");
    	sql.addProjection("DEV.VL_DEVIDO", "VL_DEVIDO");
    	sql.addProjection("MDE.NR_CHAVE", "NR_CHAVE");
    	
    	// FROM
    	sql.addFrom("FATURA ", "F");
    	sql.addFrom("FILIAL", "FFAT");
    	sql.addFrom("PESSOA", "PFIL");
    	sql.addFrom("ENDERECO_PESSOA", "EPFIL");
    	sql.addFrom("TIPO_LOGRADOURO", "TLFIL");
    	sql.addFrom("MUNICIPIO", " MFIL");
    	sql.addFrom("UNIDADE_FEDERATIVA", "UFFIL");
    	sql.addFrom("PESSOA", "PCLI");
    	sql.addFrom(getSqlBoleto(), "B");
    	sql.addFrom("ITEM_FATURA ", "IF");
    	sql.addFrom("DEVEDOR_DOC_SERV_FAT ", "DEV");
    	sql.addFrom("DOCTO_SERVICO ", "DS");
    	sql.addFrom("FILIAL ", "FORIG");
    	sql.addFrom("PESSOA ", "PDEST");
    	sql.addFrom("MONITORAMENTO_DOC_ELETRONICO ", "MDE");

    	// JOIN
    	sql.addJoin("F.ID_FILIAL", "FFAT.ID_FILIAL");
    	sql.addJoin("FFAT.ID_FILIAL", "PFIL.ID_PESSOA");
    	sql.addJoin("PFIL.ID_ENDERECO_PESSOA", "EPFIL.ID_ENDERECO_PESSOA(+)");
    	sql.addJoin("EPFIL.ID_TIPO_LOGRADOURO", "TLFIL.ID_TIPO_LOGRADOURO(+)");
    	sql.addJoin("EPFIL.ID_MUNICIPIO", "MFIL.ID_MUNICIPIO(+)");
    	sql.addJoin("MFIL.ID_UNIDADE_FEDERATIVA", "UFFIL.ID_UNIDADE_FEDERATIVA(+)");
    	sql.addJoin("F.ID_CLIENTE", "PCLI.ID_PESSOA");
    	sql.addJoin("F.ID_FATURA", "B.ID_FATURA(+)");
    	sql.addJoin("F.ID_FATURA", "IF.ID_FATURA");
    	sql.addJoin("IF.ID_DEVEDOR_DOC_SERV_FAT", "DEV.ID_DEVEDOR_DOC_SERV_FAT");
    	sql.addJoin("DEV.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
    	sql.addJoin("DS.ID_FILIAL_ORIGEM", "FORIG.ID_FILIAL");
    	sql.addJoin("DS.ID_CLIENTE_DESTINATARIO", "PDEST.ID_PESSOA");
    	sql.addJoin("DS.ID_DOCTO_SERVICO", "MDE.ID_DOCTO_SERVICO(+)");
    	
    	Long idFilial = parameters.getLong("idFilial");
    	Long idCliente = parameters.getLong("idCliente");
    	Long nroFaturaInicial = parameters.getLong("nroFaturaInicial");
		Long nroFaturaFinal = parameters.getLong("nroFaturaFinal");
    	YearMonthDay dtInicial = parameters.getYearMonthDay("dtDataInicial");
    	YearMonthDay dtFinal = parameters.getYearMonthDay("dtDataFinal");
         
    	// CRITERIA
    	sql.addCriteria("F.TP_SITUACAO_FATURA", "<>", "CA");
    	sql.addCriteria("F.ID_FILIAL", "=", idFilial );
    	sql.addCriteria("F.NR_FATURA", ">=", nroFaturaInicial );
    	sql.addCriteria("F.NR_FATURA", "<=", nroFaturaFinal );
    	sql.addCriteria("F.ID_CLIENTE", "=", idCliente );
    	sql.addCriteria("F.DT_EMISSAO", ">=", dtInicial );
    	sql.addCriteria("F.DT_EMISSAO", "<=", dtFinal );
    	
    	sql.addOrderBy("FFAT.SG_FILIAL");
    	sql.addOrderBy("F.NR_FATURA");
    	sql.addOrderBy("DS.TP_DOCUMENTO_SERVICO");
    	sql.addOrderBy("FORIG.SG_FILIAL");
    	sql.addOrderBy("DS.NR_DOCTO_SERVICO");
    	
    	return sql;
    }



	private String getSqlBoleto() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" (SELECT B.ID_BOLETO, ")
		  .append("         B.ID_FATURA, ")
		  .append("         B.NR_BOLETO, ")
		  .append("      	B.DT_VENCIMENTO, ")
		  .append("         AB.NR_AGENCIA_BANCARIA, ")
		  .append("         CED.CD_CEDENTE, ")
		  .append("         CED.NR_CARTEIRA ")
		  .append("    FROM BOLETO  B, ")
		  .append("         CEDENTE CED, ")
		  .append("         AGENCIA_BANCARIA AB ")
		  .append("   WHERE B.TP_SITUACAO_BOLETO <> 'CA' ")
		  .append("     AND B.ID_CEDENTE = CED.ID_CEDENTE(+) ")
		  .append("     AND B.ID_CEDENTE = CED.ID_CEDENTE(+) ")
		  .append("     AND CED.ID_AGENCIA_BANCARIA = AB.ID_AGENCIA_BANCARIA(+) ")
		  .append(" )  ");
		
		return sb.toString();
	}



	private String getSqlTipoDocumento() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ")
		  .append("     FROM VALOR_DOMINIO ")
		  .append("   WHERE ID_DOMINIO IN (SELECT ID_DOMINIO ")
		  .append("      					 FROM DOMINIO ")
		  .append("                         WHERE NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO') ")
		  .append("     AND VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO ")
		  .append(" ) ");
		return sb.toString();
	}



	private String getSqlTelefone() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(" (SELECT '(' || TE.NR_DDD || ') ' || TE.NR_TELEFONE ")
		  .append("     FROM  TELEFONE_ENDERECO TE ")
		  .append("    WHERE TE.ID_ENDERECO_PESSOA = EPFIL.ID_ENDERECO_PESSOA ")
		  .append("      AND TE.TP_TELEFONE = 'C' ")
		  .append("      AND TE.TP_USO = 'FO' ")
		  .append("      AND ROWNUM = 1 ")
		  .append(" ) ");
		return sb.toString();
	}



	private String getSqlIncricaoEstadual() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" ( SELECT IE.NR_INSCRICAO_ESTADUAL ")
		  .append("     FROM INSCRICAO_ESTADUAL IE ")
		  .append("    WHERE IE.ID_PESSOA = PFIL.ID_PESSOA ")
		  .append("      AND IE.ID_UNIDADE_FEDERATIVA = UFFIL.ID_UNIDADE_FEDERATIVA ")
		  .append("      AND IE.BL_INDICADOR_PADRAO = 'S' ")
		  .append("      AND IE.TP_SITUACAO = 'A' ")
		  .append("      AND ROWNUM = 1 ")
		  .append(" ) ");
		return sb.toString();
	}
   
}
