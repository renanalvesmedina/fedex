package com.mercurio.lms.expedicao.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @spring.bean id="lms.expedicao.emitirMinutaEletronicaService"
 * @spring.property name="reportName" value="com/mercurio/lms/expedicao/report/emitirMinutaEletronica.jasper"
 */

public class EmitirMinutaEletronicaService extends ReportServiceSupport{

	@Override
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
		Long idAwb = tfm.getLong("awb.idAwb");
		SqlTemplate sql = mountCabecalhoQuery(idAwb);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());
        Map<String, Object> parametersReport = new HashMap<String, Object>();
        parametersReport.put("ID_AWB", idAwb);
        parametersReport.put("SG_PAIS", SessionUtils.getPaisSessao().getSgPais());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        jr.setParameters(parametersReport);

		return jr;
	}
	
	public JRDataSource executeSubCtes(Long idAwb) throws Exception {
		SqlTemplate sql = createListCtes(idAwb);
		
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	
	private SqlTemplate createListCtes(Long idAwb) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("CONCAT(CONCAT(FDS.SG_FILIAL, ' '), DS.NR_DOCTO_SERVICO)", "DOCTO_SERVICO");
		sql.addProjection("TO_CHAR(DS.DH_EMISSAO, 'dd/MM/yyyy')", "DH_EMISSAO");
		sql.addProjection("DS.QT_VOLUMES", "QT_VOLUMES");
		sql.addProjection("DS.PS_REAL", "PESO");
		sql.addProjection("DS.VL_MERCADORIA", "VL_MERCADORIA");
		sql.addProjection("MDE.NR_CHAVE", "NR_CHAVE");
		sql.addProjection("MDE.NR_CHAVE", "CODIGO_BARRAS");
		sql.addProjection("(SELECT NR_NOTA_FISCAL FROM NOTA_FISCAL_CONHECIMENTO NFC WHERE NFC.ID_CONHECIMENTO = CTO.ID_CONHECIMENTO AND ROWNUM < 2)", "NR_NOTA_FISCAL");
		
		mountBaseSql(idAwb, sql, Boolean.FALSE);
		return sql;
	}

	private SqlTemplate mountCabecalhoQuery(Long idAwb) {
		SqlTemplate sql = createSqlTemplate();
		
//		Cabeçalho
		sql.setDistinct();
		sql.addProjection("TP.DS_DESCRICAO", "SERVICO_FRETE_CONTRATADO");
		sql.addProjection("TO_CHAR(AWB.DH_DIGITACAO,'DD/MM/YYYY HH24:MI')", "DT_HR_EMISSAO");
		sql.addProjection("FO.SG_FILIAL", "EMISSOR");
		sql.addProjection("CONCAT(FO.SG_FILIAL, AWB.NR_AWB)", "NUMERO_MINUTA");
		sql.addProjection("P.NM_PESSOA", "CIA_AEREA");
		sql.addProjection("E.SG_EMPRESA", "SG_CIA");
		sql.addProjection("CASE WHEN AWB.ID_AWB > 0 THEN TO_CHAR(AWB.ID_AWB) ELSE LPAD(AWB.DS_SERIE, 4, '0') || LPAD(AWB.NR_AWB, 6, '0') || AWB.DV_AWB END", "AWB");
//		Dados do expedidor
		sql.addProjection("PCE.NM_PESSOA", "EXPEDIDOR");
		sql.addProjection("PCE.NR_IDENTIFICACAO", "CNPJ_CPF_EXP");
		sql.addProjection("PCE.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_EXP");
		sql.addProjection("IECE.NR_INSCRICAO_ESTADUAL", "INSCRICAO_ESTADUAL_EXP");
		sql.addProjection("CONCAT(CONCAT("+PropertyVarcharI18nProjection.createProjection("TLCE.DS_TIPO_LOGRADOURO_I")+", ' '), EPCE.DS_ENDERECO)", "ENDERECO_LOGRADOURO_EXP");
		sql.addProjection("EPCE.NR_ENDERECO", "NUMERO_EXP");
		sql.addProjection("EPCE.DS_COMPLEMENTO", "COMPLEMENTO_EXP");
		sql.addProjection("CONCAT(CONCAT(TECE.NR_DDD, '-'), TECE.NR_TELEFONE)", "FONE_EXP");
		sql.addProjection("EPCE.DS_BAIRRO", "BAIRRO_EXP");
		sql.addProjection("MCE.NM_MUNICIPIO", "CIDADE_EXP");
		sql.addProjection("UFCE.SG_UNIDADE_FEDERATIVA", "UF_EXP");
		sql.addProjection("EPCE.NR_CEP", "CEP_EXP");
//		Dados Recebedor
		sql.addProjection("PCD.NM_PESSOA", "RECEBEDOR");
		sql.addProjection("PCD.NR_IDENTIFICACAO", "CNPJ_CPF_REC");
		sql.addProjection("PCD.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_REC");
		sql.addProjection("IECD.NR_INSCRICAO_ESTADUAL", "INSCRICAO_ESTADUAL_REC");
		sql.addProjection("CONCAT(CONCAT("+PropertyVarcharI18nProjection.createProjection("TLCD.DS_TIPO_LOGRADOURO_I")+", ' '), EPCD.DS_ENDERECO)", "ENDERECO_LOGRADOURO_REC");
		sql.addProjection("EPCD.NR_ENDERECO", "NUMERO_REC");
		sql.addProjection("EPCD.DS_COMPLEMENTO", "COMPLEMENTO_REC");
		sql.addProjection("CONCAT(CONCAT(TECD.NR_DDD , '-'), TECD.NR_TELEFONE)", "FONE_REC");
		sql.addProjection("EPCD.DS_BAIRRO", "BAIRRO_REC");
		sql.addProjection("MCD.NM_MUNICIPIO", "CIDADE_REC");
		sql.addProjection("UFCD.SG_UNIDADE_FEDERATIVA", "UF_REC");
		sql.addProjection("EPCD.NR_CEP", "CEP_REC");
//		Dados Tomador
		sql.addProjection("PTOM.NM_PESSOA", "TOMADOR");
		sql.addProjection("PTOM.NR_IDENTIFICACAO",  "CNPJ_CPF_TOM");
		sql.addProjection("PTOM.TP_IDENTIFICACAO", "TP_IDENTIFICACAO_TOM");
		sql.addProjection("IECT.NR_INSCRICAO_ESTADUAL", "INSCRICAO_ESTADUAL_TOM");
		sql.addProjection("CONCAT(CONCAT("+PropertyVarcharI18nProjection.createProjection("TLCT.DS_TIPO_LOGRADOURO_I")+", ' '), EPCT.DS_ENDERECO)", "ENDERECO_LOGRADOURO_TOM");
		sql.addProjection("EPCT.NR_ENDERECO", "NUMERO_TOM");
		sql.addProjection("EPCT.DS_COMPLEMENTO", "COMPLEMENTO_TOM");
		sql.addProjection("CONCAT(CONCAT(TECT.NR_DDD, '-'), TECT.NR_TELEFONE)", "FONE_TOM");
		sql.addProjection("EPCT.DS_BAIRRO", "BAIRRO_TOM");
		sql.addProjection("MCT.NM_MUNICIPIO", "CIDADE_TOM");
		sql.addProjection("UFCT.SG_UNIDADE_FEDERATIVA", "UF_TOM");
		sql.addProjection("EPCT.NR_CEP", "CEP_TOM");
//		--
		sql.addProjection("AWB.QT_VOLUMES", "QT_VOLUMES_TOTAL");
		sql.addProjection("AWB.PS_TOTAL", "PESO_TOTAL");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("EMB.DS_EMBALAGEM_I"), "TP_EMBALAGEM");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("NP.DS_NATUREZA_PRODUTO_I"), "DECLARACAO_CONTEUDO");
		sql.addProjection("ASAWB.NR_APOLICE", "NR_APOLICE");
		sql.addProjection("NVL(AWB.NR_CC_TOMADOR, E.NR_CONTA_CORRENTE)", "NR_CONTA_CORRENTE"); 
		sql.addProjection("PSEG.NM_FANTASIA", "SEGURADORA");
		sql.addProjection("(SELECT SUM(D.VL_MERCADORIA) FROM DOCTO_SERVICO D, CTO_AWB CTO WHERE D.ID_DOCTO_SERVICO = CTO.ID_CONHECIMENTO AND CTO.ID_AWB =  AWB.ID_AWB)", "VL_MERCADORIA");
		sql.addProjection("AWB.OB_AWB", "OBSERVACOES");
		sql.addProjection("(SELECT COUNT(D.ID_DOCTO_SERVICO) FROM DOCTO_SERVICO D, CTO_AWB CTO WHERE D.ID_DOCTO_SERVICO = CTO.ID_CONHECIMENTO AND CTO.ID_AWB =  AWB.ID_AWB)", "QTD_DACTE");
		sql.addProjection("(SELECT NM_CONTATO FROM CONTATO CONE WHERE CONE.ID_PESSOA=AWB.ID_CLIENTE_EXPEDIDOR and TP_CONTATO='AE'and rownum < 2)", "NM_CONTATO_EXP");
		sql.addProjection("(SELECT NM_CONTATO FROM CONTATO CONR WHERE CONR.ID_PESSOA=AWB.ID_CLIENTE_DESTINATARIO and TP_CONTATO='AE' and rownum < 2)", "NM_CONTATO_REC");
		
		mountBaseSql(idAwb, sql, Boolean.TRUE);
		
		return sql;
	}

	private void mountBaseSql(Long idAwb, SqlTemplate sql, Boolean isCabecalho) {
		sql.addInnerJoin("AWB");	
		sql.addInnerJoin("CIA_FILIAL_MERCURIO CFM ON CFM.ID_CIA_FILIAL_MERCURIO = AWB.ID_CIA_FILIAL_MERCURIO");
		sql.addInnerJoin("FILIAL FO ON FO.ID_FILIAL = AWB.ID_FILIAL_ORIGEM");
		sql.addInnerJoin("PESSOA PCE ON PCE.ID_PESSOA = AWB.ID_CLIENTE_EXPEDIDOR");
		sql.addInnerJoin("ENDERECO_PESSOA EPCE ON EPCE.ID_ENDERECO_PESSOA = PCE.ID_ENDERECO_PESSOA");
		sql.addInnerJoin("TIPO_LOGRADOURO TLCE ON TLCE.ID_TIPO_LOGRADOURO = EPCE.ID_TIPO_LOGRADOURO");
		sql.addLeftOuterJoin("TELEFONE_ENDERECO TECE ON (TECE.ID_TELEFONE_ENDERECO = EPCE.ID_ENDERECO_PESSOA AND TECE.TP_TELEFONE = 'C' AND TECE.TP_USO IN('FO','FF'))");
		sql.addInnerJoin("MUNICIPIO MCE ON MCE.ID_MUNICIPIO = EPCE.ID_MUNICIPIO");
		sql.addInnerJoin("UNIDADE_FEDERATIVA UFCE ON UFCE.ID_UNIDADE_FEDERATIVA = MCE.ID_UNIDADE_FEDERATIVA");
		sql.addInnerJoin("INSCRICAO_ESTADUAL IECE ON IECE.ID_INSCRICAO_ESTADUAL = AWB.ID_IE_EXPEDIDOR");
		sql.addInnerJoin("PESSOA PCD ON PCD.ID_PESSOA = AWB.ID_CLIENTE_DESTINATARIO");
		sql.addInnerJoin("ENDERECO_PESSOA EPCD ON EPCD.ID_ENDERECO_PESSOA = PCD.ID_ENDERECO_PESSOA");
		sql.addInnerJoin("TIPO_LOGRADOURO TLCD ON TLCD.ID_TIPO_LOGRADOURO = EPCD.ID_TIPO_LOGRADOURO");
		sql.addLeftOuterJoin("TELEFONE_ENDERECO TECD ON (TECD.ID_TELEFONE_ENDERECO = EPCD.ID_ENDERECO_PESSOA AND TECD.TP_TELEFONE = 'C' AND TECD.TP_USO IN('FO','FF'))");
		sql.addInnerJoin("MUNICIPIO MCD ON MCD.ID_MUNICIPIO = EPCD.ID_MUNICIPIO");
		sql.addInnerJoin("UNIDADE_FEDERATIVA UFCD ON UFCD.ID_UNIDADE_FEDERATIVA = MCD.ID_UNIDADE_FEDERATIVA");
		sql.addInnerJoin("INSCRICAO_ESTADUAL IECD ON IECD.ID_INSCRICAO_ESTADUAL = AWB.ID_IE_DESTINATARIO");
		sql.addInnerJoin("EMPRESA E ON E.ID_EMPRESA = CFM.ID_EMPRESA");
		sql.addInnerJoin("PESSOA P ON P.ID_PESSOA = E.ID_EMPRESA");
		sql.addLeftOuterJoin("PESSOA PTOM ON PTOM.ID_PESSOA = AWB.ID_CLIENTE_TOMADOR");
		sql.addLeftOuterJoin("ENDERECO_PESSOA EPCT ON EPCT.ID_ENDERECO_PESSOA = PTOM.ID_ENDERECO_PESSOA");
		sql.addLeftOuterJoin("TIPO_LOGRADOURO TLCT ON TLCT.ID_TIPO_LOGRADOURO = EPCT.ID_TIPO_LOGRADOURO");
		sql.addLeftOuterJoin("TELEFONE_ENDERECO TECT ON (TECT.ID_TELEFONE_ENDERECO = EPCT.ID_ENDERECO_PESSOA AND TECT.TP_TELEFONE = 'C' AND TECT.TP_USO IN('FO','FF'))");
		sql.addLeftOuterJoin("MUNICIPIO MCT ON MCT.ID_MUNICIPIO = EPCT.ID_MUNICIPIO");
		sql.addLeftOuterJoin("UNIDADE_FEDERATIVA UFCT ON UFCT.ID_UNIDADE_FEDERATIVA = MCT.ID_UNIDADE_FEDERATIVA");
		sql.addLeftOuterJoin("INSCRICAO_ESTADUAL IECT ON IECT.ID_INSCRICAO_ESTADUAL = AWB.ID_IE_TOMADOR");
		sql.addInnerJoin("CTO_AWB ON CTO_AWB.ID_AWB = AWB.ID_AWB");
		sql.addInnerJoin("CONHECIMENTO CTO ON CTO.ID_CONHECIMENTO = CTO_AWB.ID_CONHECIMENTO");
		sql.addInnerJoin("AWB_EMBALAGEM EMBAWB ON EMBAWB.ID_AWB = AWB.ID_AWB");
		sql.addInnerJoin("EMBALAGEM EMB ON EMB.ID_EMBALAGEM = EMBAWB.ID_EMBALAGEM");
		sql.addInnerJoin("NATUREZA_PRODUTO NP ON NP.ID_NATUREZA_PRODUTO =  AWB.ID_NATUREZA_PRODUTO");
		sql.addLeftOuterJoin("APOLICE_SEGURO ASAWB ON ASAWB.ID_APOLICE_SEGURO = AWB.ID_APOLICE_SEGURO");
		sql.addLeftOuterJoin("PESSOA PSEG ON PSEG.ID_PESSOA = ASAWB.ID_SEGURADORA");
		sql.addInnerJoin("DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = CTO.ID_CONHECIMENTO");
		sql.addInnerJoin("MONITORAMENTO_DOC_ELETRONICO MDE ON MDE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO");
		sql.addInnerJoin("FILIAL FDS ON FDS.ID_FILIAL = DS.ID_FILIAL_ORIGEM");
		sql.addLeftOuterJoin("TABELA_PRECO TP ON TP.ID_TABELA_PRECO = AWB.ID_TABELA_PRECO");
		sql.addCriteria("AWB.ID_AWB", "=", idAwb);
		
		if (!isCabecalho) {
			sql.addOrderBy("FDS.SG_FILIAL, DS.NR_DOCTO_SERVICO");
		}
	}

}
