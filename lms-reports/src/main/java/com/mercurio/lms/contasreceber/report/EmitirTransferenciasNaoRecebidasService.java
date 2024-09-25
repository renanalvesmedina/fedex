package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author JoseMR
 *
 * @spring.bean id="lms.contasreceber.emitirTransferenciasNaoRecebidasService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirTransferenciasNaoRecebidas.jasper"
 */
public class EmitirTransferenciasNaoRecebidasService extends ReportServiceSupport {

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		SqlTemplate sqlConhecimento = montaQueryConhecimento(tfm);
		SqlTemplate sqlNotasFiscais = montaQueryNotasFiscais(tfm);
		
		YearMonthDay dataInicial = tfm.getYearMonthDay("dataInicial");
		YearMonthDay dataFinal   = tfm.getYearMonthDay("dataFinal");
		
		if( tfm.getLong("filial.idFilial") != null ){
			
			String sgFilial = tfm.getString("siglaNomeFilial");
			
			if( sgFilial == null || sgFilial.equals("") ){
				sgFilial = tfm.getString("siglaFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia");
			}
			
			sqlConhecimento.addFilterSummary("filialDestino",sgFilial);
		}
		
		sqlConhecimento.addFilterSummary("situacaoTransferencia",tfm.getString("dsSituacaoTransferencia"));
		
		if( dataInicial != null ){		
			sqlConhecimento.addFilterSummary("dataEmissaoInicial",JTFormatUtils.format(dataInicial));
		}
		
		if( dataFinal != null ){
			sqlConhecimento.addFilterSummary("dataEmissaoFinal",JTFormatUtils.format(dataFinal));
		}
		
		sqlConhecimento.addCriteriaValue(sqlNotasFiscais.getCriteria());
		
		JRReportDataObject jr = executeQuery("SELECT * FROM (\n" + 
				                             sqlConhecimento.getSql() + 
				                             "\n UNION \n" + 
				                             sqlNotasFiscais.getSql() +
				                             " )\n" +
							                 "ORDER BY SIGLA_FILIAL_ORIGEM, " +
							                 "         NR_TRANSFERENCIA, " +
							                 "		   SG_FILIAL_ORIGEM_DOCTO_SERVICO, " +
							                 "         NR_DOCUMENTO", 
							                 sqlConhecimento.getCriteria());

		Map parametersReport = new HashMap();
		
		/* Tipo do relatório */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		/* Parametros de pesquisa */
		parametersReport.put("parametrosPesquisa", sqlConhecimento.getFilterSummary());
		/* Usuario emissor */
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
        jr.setParameters(parametersReport);
        
		return jr;
	}	

	private SqlTemplate montaQueryConhecimento(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		YearMonthDay dataInicial = tfm.getYearMonthDay("dataInicial");
		YearMonthDay dataFinal   = tfm.getYearMonthDay("dataFinal");
		
		sql.addProjection("T.ID_TRANSFERENCIA, " +
				          "FILIAL_ORIGEM.ID_FILIAL ID_FILIAL_ORIGEM, " +
				          "FILIAL_ORIGEM.SG_FILIAL SIGLA_FILIAL_ORIGEM, " +				          
				          "PESSOA_ORIGEM.NM_FANTASIA NOME_FILIAL_ORIGEM, " +				          
				          "FILIAL_DESTINO.SG_FILIAL SIGLA_FILIAL_DESTINO, " +				          
				          "PESSOA_DESTINO.NM_FANTASIA NOME_FILIAL_DESTINO, " +
				          "T.NR_TRANSFERENCIA, " +
				          "T.DT_EMISSAO, " +
				          PropertyVarcharI18nProjection.createProjection("VD3.DS_VALOR_DOMINIO_I", "TP_DOCUMENTO_SERVICO") + ", " +
				          "DS.TP_DOCUMENTO_SERVICO TP_DOCUMENTO, " + 
				          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I", "TP_SITUACAO_TRANSFERENCIA") + ", " +
				          "FILIAL_ORIGEM_CON.SG_FILIAL SG_FILIAL_ORIGEM_DOCTO_SERVICO, " +
				          "CON.NR_CONHECIMENTO NR_DOCUMENTO, " +
				          "P.TP_IDENTIFICACAO, " +
				          "P.NR_IDENTIFICACAO, " +
				          "P.NM_PESSOA NOME_DEVEDOR, " +
				          "T.TP_ORIGEM, " +
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I", "DS_ORIGEM") + ", " +
				          PropertyVarcharI18nProjection.createProjection("MT.DS_MOTIVO_TRANSFERENCIA_I","DS_MOTIVO_TRANSFERENCIA"));
		
		sql.addFrom("TRANSFERENCIA T " +
				    "	INNER JOIN FILIAL FILIAL_ORIGEM ON T.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL " +
				    "   INNER JOIN PESSOA PESSOA_ORIGEM ON FILIAL_ORIGEM.ID_FILIAL = PESSOA_ORIGEM.ID_PESSOA " +
				    "	INNER JOIN FILIAL FILIAL_DESTINO ON T.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL " +
				    "   INNER JOIN PESSOA PESSOA_DESTINO ON FILIAL_DESTINO.ID_FILIAL = PESSOA_DESTINO.ID_PESSOA, " +
				    "VALOR_DOMINIO VD " +
				    "   INNER JOIN DOMINIO D ON VD.ID_DOMINIO = D.ID_DOMINIO, " +
				    "VALOR_DOMINIO VD2 " +
				    "   INNER JOIN DOMINIO D2 ON VD2.ID_DOMINIO = D2.ID_DOMINIO, " +
				    "ITEM_TRANSFERENCIA IT " +
				    "   INNER JOIN CLIENTE C ON IT.ID_NOVO_RESPONSAVEL = C.ID_CLIENTE " +
				    "   INNER JOIN PESSOA P ON C.ID_CLIENTE = P.ID_PESSOA " +
				    "   INNER JOIN MOTIVO_TRANSFERENCIA MT ON IT.ID_MOTIVO_TRANSFERENCIA = MT.ID_MOTIVO_TRANSFERENCIA " +
				    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON IT.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
				    "   INNER JOIN DOCTO_SERVICO DS ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
				    "   INNER JOIN CONHECIMENTO CON ON DS.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO " +
				    "   INNER JOIN FILIAL FILIAL_ORIGEM_CON ON CON.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_CON.ID_FILIAL, " +
				    "VALOR_DOMINIO VD3 " +
				    "   INNER JOIN DOMINIO D3 ON VD3.ID_DOMINIO = D3.ID_DOMINIO");
		
		sql.addCustomCriteria("T.ID_TRANSFERENCIA  = IT.ID_TRANSFERENCIA");
		sql.addCustomCriteria("lower(D.NM_DOMINIO) 	       = lower('DM_ORIGEM')");
		sql.addCustomCriteria("lower(D2.NM_DOMINIO) 	   = lower('DM_STATUS_TRANSFERENCIA')");
		sql.addCustomCriteria("lower(D3.NM_DOMINIO) 	   = lower('DM_TIPO_DOCUMENTO_SERVICO')");
		sql.addCustomCriteria("lower(VD.VL_VALOR_DOMINIO)  = lower(T.TP_ORIGEM)");
		sql.addCustomCriteria("lower(VD2.VL_VALOR_DOMINIO) = lower(T.TP_SITUACAO_TRANSFERENCIA)");
		sql.addCustomCriteria("lower(VD3.VL_VALOR_DOMINIO) = lower(DS.TP_DOCUMENTO_SERVICO)");
		sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ( 'CTR', 'NFT', 'CTE', 'NTE')");
		
		sql.addCriteria("T.ID_FILIAL_DESTINO","=", tfm.getLong("filial.idFilial"));
		sql.addCriteria("T.TP_SITUACAO_TRANSFERENCIA","=", tfm.getDomainValue("tpSituacaoTransferencia").getValue());
		
		if( dataInicial != null ){
			
			if( dataFinal == null ){
				sql.addCustomCriteria("T.DT_EMISSAO >= ? ");
				sql.addCriteriaValue(tfm.getYearMonthDay("dataInicial"));
			} else {
				sql.addCustomCriteria("T.DT_EMISSAO BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("dataInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dataFinal"));		
			}
			
		} else {
			
			if( dataFinal != null ){
				sql.addCustomCriteria("T.DT_EMISSAO <= ? ");
				sql.addCriteriaValue(tfm.getYearMonthDay("dataFinal"));
			}
			
		}
		
		return sql;
	}
	
	private SqlTemplate montaQueryNotasFiscais(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		YearMonthDay dataInicial = tfm.getYearMonthDay("dataInicial");
		YearMonthDay dataFinal   = tfm.getYearMonthDay("dataFinal");
		
		sql.addProjection("T.ID_TRANSFERENCIA, " +
				          "FILIAL_ORIGEM.ID_FILIAL ID_FILIAL_ORIGEM, " +
				          "FILIAL_ORIGEM.SG_FILIAL SIGLA_FILIAL_ORIGEM, " +
				          "PESSOA_ORIGEM.NM_FANTASIA NOME_FILIAL_ORIGEM, " +
				          "FILIAL_DESTINO.SG_FILIAL SIGLA_FILIAL_DESTINO, " +				          
				          "PESSOA_DESTINO.NM_FANTASIA NOME_FILIAL_DESTINO, " +
				          "T.NR_TRANSFERENCIA, " +
				          "T.DT_EMISSAO, " +
				          PropertyVarcharI18nProjection.createProjection("VD3.DS_VALOR_DOMINIO_I", "TP_DOCUMENTO_SERVICO") + ", " +
				          "DS.TP_DOCUMENTO_SERVICO TP_DOCUMENTO, " + 
				          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I", "TP_SITUACAO_TRANSFERENCIA") + ", " +
				          "FILIAL_ORIGEM_NFS.SG_FILIAL SG_FILIAL_ORIGEM_DOCTO_SERVICO, " +
				          "NFS.NR_NOTA_FISCAL_SERVICO NR_DOCUMENTO, " +
				          "P.TP_IDENTIFICACAO, " +
				          "P.NR_IDENTIFICACAO, " +
				          "P.NM_PESSOA NOME_DEVEDOR, " +
				          "T.TP_ORIGEM, " +
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I", "DS_ORIGEM") + ", " +
				          PropertyVarcharI18nProjection.createProjection("MT.DS_MOTIVO_TRANSFERENCIA_I","DS_MOTIVO_TRANSFERENCIA"));
		
		sql.addFrom("TRANSFERENCIA T " +
				    "	INNER JOIN FILIAL FILIAL_ORIGEM ON T.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL " +
				    "   INNER JOIN PESSOA PESSOA_ORIGEM ON FILIAL_ORIGEM.ID_FILIAL = PESSOA_ORIGEM.ID_PESSOA " +
				    "	INNER JOIN FILIAL FILIAL_DESTINO ON T.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL " +
				    "   INNER JOIN PESSOA PESSOA_DESTINO ON FILIAL_DESTINO.ID_FILIAL = PESSOA_DESTINO.ID_PESSOA, " +
				    "VALOR_DOMINIO VD " +
				    "   INNER JOIN DOMINIO D ON VD.ID_DOMINIO = D.ID_DOMINIO, " +
				    "VALOR_DOMINIO VD2 " +
				    "   INNER JOIN DOMINIO D2 ON VD2.ID_DOMINIO = D2.ID_DOMINIO, " +
				    "ITEM_TRANSFERENCIA IT " +
				    "   INNER JOIN CLIENTE C ON IT.ID_NOVO_RESPONSAVEL = C.ID_CLIENTE " +
				    "   INNER JOIN PESSOA P ON C.ID_CLIENTE = P.ID_PESSOA " +
				    "   INNER JOIN MOTIVO_TRANSFERENCIA MT ON IT.ID_MOTIVO_TRANSFERENCIA = MT.ID_MOTIVO_TRANSFERENCIA " +
				    "   INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON IT.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
				    "   INNER JOIN DOCTO_SERVICO DS ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
				    "   INNER JOIN NOTA_FISCAL_SERVICO NFS ON DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO " +
				    "   INNER JOIN FILIAL FILIAL_ORIGEM_NFS ON NFS.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_NFS.ID_FILIAL, " +
				    "VALOR_DOMINIO VD3 " +
				    "   INNER JOIN DOMINIO D3 ON VD3.ID_DOMINIO = D3.ID_DOMINIO");
		
		sql.addCustomCriteria("T.ID_TRANSFERENCIA      = IT.ID_TRANSFERENCIA");
		 
		sql.addCustomCriteria("lower(D.NM_DOMINIO) 	       = lower('DM_ORIGEM')");
		sql.addCustomCriteria("lower(D2.NM_DOMINIO) 	   = lower('DM_STATUS_TRANSFERENCIA')");
		sql.addCustomCriteria("lower(D3.NM_DOMINIO) 	   = lower('DM_TIPO_DOCUMENTO_SERVICO')");
		sql.addCustomCriteria("lower(VD.VL_VALOR_DOMINIO)  = lower(T.TP_ORIGEM)");
		sql.addCustomCriteria("lower(VD2.VL_VALOR_DOMINIO) = lower(T.TP_SITUACAO_TRANSFERENCIA)");
		sql.addCustomCriteria("lower(VD3.VL_VALOR_DOMINIO) = lower(DS.TP_DOCUMENTO_SERVICO)");
		sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO in ('NFS','NSE')");
		
		sql.addCriteria("T.ID_FILIAL_DESTINO","=", tfm.getLong("filial.idFilial"));
		sql.addCriteria("T.TP_SITUACAO_TRANSFERENCIA","=", tfm.getDomainValue("tpSituacaoTransferencia").getValue());
		
		if( dataInicial != null ){
			
			if( dataFinal == null ){
				sql.addCustomCriteria("T.DT_EMISSAO >= ? ");
				sql.addCriteriaValue(tfm.getYearMonthDay("dataInicial"));
			} else {
				sql.addCustomCriteria("T.DT_EMISSAO BETWEEN ? AND ?");
				sql.addCriteriaValue(tfm.getYearMonthDay("dataInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dataFinal"));		
			}
			
		} else {
			
			if( dataFinal != null ){
				sql.addCustomCriteria("T.DT_EMISSAO <= ? ");
				sql.addCriteriaValue(tfm.getYearMonthDay("dataFinal"));
			}
			
		}		
		
		return sql;
	}

}
