package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Transferencia;
import com.mercurio.lms.contasreceber.model.service.GerarTranferenciaDebitoService;
import com.mercurio.lms.contasreceber.model.service.TransferenciaService;

/**
 * @author JoseMR
 *
 * @spring.bean id="lms.contasreceber.emitirTransferenciaDebitoService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirTransferenciasDebito.jasper"
 */
public class EmitirTransferenciasDebitoService extends ReportServiceSupport {
	
	private TransferenciaService transferenciaService;
	private GerarTranferenciaDebitoService gerarTranferenciaDebitoService;
	private JdbcTemplate jdbcTemplate;
	private DomainValueService domainValueService;

	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		JRDataSource jre = null;
		JRReportDataObject jr = null;
		
		if(tfm.getLong("nrTransferencia") != null && tfm.getLong("filial.idFilial") != null){
			verificaSituacaoTransferencia(parameters);
		}
		
		if( tfm.getLong("nrTransferencia") == null ){
			gerarTranferenciaDebitoService.gerarTranferenciaDebito(tfm.getLong("filial.idFilial"),tfm.getDomainValue("tpOrigem").getValue());
		}
		
		SqlTemplate sqlConhecimento = montaQueryConhecimento(tfm);
		SqlTemplate sqlNotasFiscais = montaQueryNotasFiscais(tfm);
		
		sqlConhecimento.addFilterSummary("filial",tfm.getString("siglaNomeFilial"));
		
		
		sqlConhecimento.addFilterSummary("numero",tfm.getLong("nrTransferencia"));
		if (tfm.getString("origem") != null){
			sqlConhecimento.addFilterSummary("origem",tfm.getString("origem"));
		} else {
			sqlConhecimento.addFilterSummary("origem",domainValueService.findDomainValueByValue("DM_ORIGEM", "M").getDescription());
		}
		sqlConhecimento.addCriteriaValue(sqlNotasFiscais.getCriteria());
		
		List retorno = jdbcTemplate.queryForList( "SELECT * FROM ( " +
												  sqlConhecimento.getSql() + 
								                  " UNION ALL " +
								                  sqlNotasFiscais.getSql() +
								                  " ) " +
								                  "ORDER BY SIGLA_FILIAL_ORIGEM, " +
								                  "         NR_TRANSFERENCIA, " +
								                  "			SG_FILIAL_ORIGEM_DOCTO_SERVICO, " +
								                  "         NR_DOCUMENTO",				                             
								                  sqlConhecimento.getCriteria());

		if( retorno == null || retorno.isEmpty() ){
			jre = new JRMapCollectionDataSource(retorno);
			
		} else {
			
			for (Iterator iter = retorno.iterator(); iter.hasNext();) {
				
				ListOrderedMap element = (ListOrderedMap) iter.next();
				
				Transferencia transferencia = transferenciaService.findById(Long.valueOf(((Number)element.get("ID_TRANSFERENCIA")).longValue()));
				
				transferencia.setBlIndicadorImpressao(Boolean.TRUE);
				
				transferenciaService.store(transferencia);
				
				transferencia = null;
			}
			
			jre = new JRMapCollectionDataSource(retorno);
			
		}
		
		jr = createReportDataObject(jre,parameters);
		
		Map parametersReport = new HashMap();

		parametersReport.put("parametrosPesquisa", sqlConhecimento.getFilterSummary());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);
		
		
		return jr;
	}

	/**
	 * Verifica se a transferencia tem tipo situacao transferencia = 'CA'
	 * Caso afirmativo lança a exception LMS-36058
	 * @param parameters Filial, número Transferencia, tipo Origem e tipo Situacao Transferencia
	 */
	private void verificaSituacaoTransferencia(Map parameters) {
		
		Map<String,Object> criteria = new HashMap<String, Object>();
		criteria.put("tpSituacaoTransferencia",parameters.get("tpSituacaoTransferencia"));
		criteria.put("nrTransferencia",parameters.get("nrTransferencia"));
		criteria.put("filialByIdFilialOrigem.idFilial",parameters.get("filial.idFilial"));
		
		List listaTransferencia = transferenciaService.find(criteria);
		
		if( listaTransferencia != null && !listaTransferencia.isEmpty() ){
			throw new BusinessException("LMS-36058");
		}
		
	}	

	private SqlTemplate montaQueryConhecimento(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("T.ID_TRANSFERENCIA AS ID_TRANSFERENCIA, " +
				          "FILIAL_ORIGEM.ID_FILIAL AS ID_FILIAL_ORIGEM, " +
						  "FILIAL_ORIGEM.SG_FILIAL AS SIGLA_FILIAL_ORIGEM, " +
				          "PESSOA_ORIGEM.NM_FANTASIA AS NOME_FILIAL_ORIGEM, " +
				          "T.NR_TRANSFERENCIA AS NR_TRANSFERENCIA, " +
				          "T.TP_ORIGEM AS TP_ORIGEM, " +
				          "D.DS_DIVISAO_CLIENTE AS DS_DIVISAO_NOVO_CLIENTE, " +
				          "FILIAL_DESTINO.SG_FILIAL AS  SIGLA_FILIAL_DESTINO, " +
				          "PESSOA_DESTINO.NM_FANTASIA AS NOME_FILIAL_DESTINO, " +
				          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","TP_DOCUMENTO_SERVICO") + ", "	+	
				          "DS.TP_DOCUMENTO_SERVICO AS TP_DOCUMENTO, " + 
				          "FILIAL_ORIGEM_CON.SG_FILIAL AS SG_FILIAL_ORIGEM_DOCTO_SERVICO, " +
				          "DDSF.VL_DEVIDO as VL_FRETE, " +
				          "CON.NR_CONHECIMENTO AS NR_DOCUMENTO, " +
				          "NVL(TO_CHAR(CON.DV_CONHECIMENTO),'-1') AS DV_CONHECIMENTO, " +
				          "P.TP_IDENTIFICACAO AS TP_IDENTIFICACAO, " +
				          "P.NR_IDENTIFICACAO AS NR_IDENTIFICACAO, " +
				          "P.NM_PESSOA AS NOME_DEVEDOR, " +
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","DS_ORIGEM") + ", "	+	
				          PropertyVarcharI18nProjection.createProjection("MT.DS_MOTIVO_TRANSFERENCIA_I","DS_MOTIVO_TRANSFERENCIA"));
		
		sql.addFrom("TRANSFERENCIA T " +
					"	INNER JOIN FILIAL FILIAL_ORIGEM ON T.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL " +
				    "	INNER JOIN PESSOA PESSOA_ORIGEM ON FILIAL_ORIGEM.ID_FILIAL = PESSOA_ORIGEM.ID_PESSOA" +
				    " 	INNER JOIN FILIAL FILIAL_DESTINO ON T.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL " +
				    "	INNER JOIN PESSOA PESSOA_DESTINO ON FILIAL_DESTINO.ID_FILIAL = PESSOA_DESTINO.ID_PESSOA," +
				    "VALOR_DOMINIO VD " +
				    "	INNER JOIN DOMINIO D ON VD.ID_DOMINIO = D.ID_DOMINIO, " +				    
				    "ITEM_TRANSFERENCIA IT " +
				    "	INNER JOIN CLIENTE C ON IT.ID_NOVO_RESPONSAVEL = C.ID_CLIENTE " +
				    "	INNER JOIN PESSOA P ON C.ID_CLIENTE = P.ID_PESSOA " +
				    "	LEFT JOIN DIVISAO_CLIENTE D ON D.ID_DIVISAO_CLIENTE = IT.ID_DIVISAO_CLIENTE_NOVO " +
				    "	INNER JOIN V_MOTIVO_TRANSFERENCIA_I MT ON IT.ID_MOTIVO_TRANSFERENCIA = MT.ID_MOTIVO_TRANSFERENCIA " +
				    "	INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON IT.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
				    "   INNER JOIN DOCTO_SERVICO DS ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
				    "	INNER JOIN CONHECIMENTO CON ON DS.ID_DOCTO_SERVICO = CON.ID_CONHECIMENTO " +
				    "	INNER JOIN FILIAL FILIAL_ORIGEM_CON ON CON.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_CON.ID_FILIAL," +
				    "VALOR_DOMINIO VD2 " +
				    "	INNER JOIN DOMINIO D2 ON VD2.ID_DOMINIO = D2.ID_DOMINIO");
		
		sql.addCustomCriteria("T.ID_TRANSFERENCIA  = IT.ID_TRANSFERENCIA");
		sql.addCustomCriteria("D.NM_DOMINIO 	   = 'DM_ORIGEM'");
		sql.addCustomCriteria("D2.NM_DOMINIO 	   = 'DM_TIPO_DOCUMENTO_SERVICO'");
		sql.addCustomCriteria("VD.VL_VALOR_DOMINIO = T.TP_ORIGEM");
		sql.addCustomCriteria("VD2.VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO");
		sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ( 'CTR', 'NFT', 'CTE' , 'NTE')");
		
		if( tfm.getLong("nrTransferencia") == null ){
			sql.addCustomCriteria("T.TP_SITUACAO_TRANSFERENCIA <> 'CA'");
			sql.addCustomCriteria("T.BL_INDICADOR_IMPRESSAO = 'N'");
			sql.addCriteria("FILIAL_ORIGEM.ID_FILIAL","=", tfm.getLong("filial.idFilial"));
		} else {
			sql.addCriteria("FILIAL_ORIGEM.ID_FILIAL","=", tfm.getLong("filial.idFilial"));
			sql.addCriteria("T.NR_TRANSFERENCIA","=", tfm.getLong("nrTransferencia"));
		}
		
		sql.addCriteria("T.TP_ORIGEM","=",tfm.getDomainValue("tpOrigem").getValue());
		
		return sql;
	}
	
	private SqlTemplate montaQueryNotasFiscais(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("T.ID_TRANSFERENCIA AS ID_TRANSFERENCIA, " +
				          "FILIAL_ORIGEM.ID_FILIAL AS ID_FILIAL_ORIGEM, " +
				          "FILIAL_ORIGEM.SG_FILIAL AS SIGLA_FILIAL_ORIGEM, " +				          
				          "PESSOA_ORIGEM.NM_FANTASIA AS NOME_FILIAL_ORIGEM, " +
				          "T.NR_TRANSFERENCIA AS NR_TRANSFERENCIA, " +
				          "T.TP_ORIGEM AS TP_ORIGEM, " +
				          "D.DS_DIVISAO_CLIENTE AS DS_DIVISAO_NOVO_CLIENTE, " +
				          "FILIAL_DESTINO.SG_FILIAL AS SIGLA_FILIAL_DESTINO, " +
				          "PESSOA_DESTINO.NM_FANTASIA AS NOME_FILIAL_DESTINO, " +
				          PropertyVarcharI18nProjection.createProjection("VD2.DS_VALOR_DOMINIO_I","TP_DOCUMENTO_SERVICO") + ", "	+			          
				          "DS.TP_DOCUMENTO_SERVICO AS TP_DOCUMENTO, " + 
				          "FILIAL_ORIGEM_NFS.SG_FILIAL AS SG_FILIAL_ORIGEM_DOCTO_SERVICO, " +
				          "DDSF.VL_DEVIDO as VL_FRETE, " +
				          "NFS.NR_NOTA_FISCAL_SERVICO AS NR_DOCUMENTO, " +
				          "NVL(TO_CHAR(NULL),'-1') AS DV_CONHECIMENTO, " +
				          "P.TP_IDENTIFICACAO AS TP_IDENTIFICACAO, " +
				          "P.NR_IDENTIFICACAO AS NR_IDENTIFICACAO, " +
				          "P.NM_PESSOA AS NOME_DEVEDOR, " +
				          PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I","DS_ORIGEM") + ", "	+	
				          PropertyVarcharI18nProjection.createProjection("MT.DS_MOTIVO_TRANSFERENCIA_I","DS_MOTIVO_TRANSFERENCIA"));
		
		sql.addFrom("TRANSFERENCIA T " +
				    "	INNER JOIN FILIAL FILIAL_ORIGEM ON T.ID_FILIAL_ORIGEM = FILIAL_ORIGEM.ID_FILIAL " +
				    "	INNER JOIN PESSOA PESSOA_ORIGEM ON FILIAL_ORIGEM.ID_FILIAL = PESSOA_ORIGEM.ID_PESSOA" +
				    " 	INNER JOIN FILIAL FILIAL_DESTINO ON T.ID_FILIAL_DESTINO = FILIAL_DESTINO.ID_FILIAL " +
				    "	INNER JOIN PESSOA PESSOA_DESTINO ON FILIAL_DESTINO.ID_FILIAL = PESSOA_DESTINO.ID_PESSOA, " +
				    "VALOR_DOMINIO VD " +
				    "	INNER JOIN DOMINIO D ON VD.ID_DOMINIO = D.ID_DOMINIO, " +
				    "ITEM_TRANSFERENCIA IT " +
				    "	INNER JOIN CLIENTE C ON IT.ID_NOVO_RESPONSAVEL = C.ID_CLIENTE " +
				    "	INNER JOIN PESSOA P ON C.ID_CLIENTE = P.ID_PESSOA " +
				    "	LEFT JOIN DIVISAO_CLIENTE D ON D.ID_DIVISAO_CLIENTE = IT.ID_DIVISAO_CLIENTE_NOVO " +
				    "	INNER JOIN V_MOTIVO_TRANSFERENCIA_I MT ON IT.ID_MOTIVO_TRANSFERENCIA = MT.ID_MOTIVO_TRANSFERENCIA " +
				    "	INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF ON IT.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
				    "   INNER JOIN DOCTO_SERVICO DS ON DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
				    "	INNER JOIN NOTA_FISCAL_SERVICO NFS ON DS.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO " +
				    "	INNER JOIN FILIAL FILIAL_ORIGEM_NFS ON NFS.ID_FILIAL_ORIGEM = FILIAL_ORIGEM_NFS.ID_FILIAL," +
				    "VALOR_DOMINIO VD2 " +
				    "	INNER JOIN DOMINIO D2 ON VD2.ID_DOMINIO = D2.ID_DOMINIO");
		
		sql.addCustomCriteria("T.ID_TRANSFERENCIA  = IT.ID_TRANSFERENCIA");
		sql.addCustomCriteria("D.NM_DOMINIO 	   = 'DM_ORIGEM'");
		sql.addCustomCriteria("D2.NM_DOMINIO 	   = 'DM_TIPO_DOCUMENTO_SERVICO'");
		sql.addCustomCriteria("VD.VL_VALOR_DOMINIO = T.TP_ORIGEM");
		sql.addCustomCriteria("VD2.VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO");
		sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN( 'NFS' , 'NSE')");
		
		if( tfm.getLong("nrTransferencia") == null ){
			sql.addCustomCriteria("T.TP_SITUACAO_TRANSFERENCIA <> 'CA'");
			sql.addCustomCriteria("T.BL_INDICADOR_IMPRESSAO = 'N'");
			sql.addCriteria("FILIAL_ORIGEM.ID_FILIAL","=", tfm.getLong("filial.idFilial"));
		} else {
			sql.addCriteria("FILIAL_ORIGEM.ID_FILIAL","=", tfm.getLong("filial.idFilial"));
			sql.addCriteria("T.NR_TRANSFERENCIA","=", tfm.getLong("nrTransferencia"));
		}
		
		sql.addCriteria("T.TP_ORIGEM","=",tfm.getDomainValue("tpOrigem").getValue());
		
		return sql;
		
	}

	public void setTransferenciaService(TransferenciaService transferenciaService) {
		this.transferenciaService = transferenciaService;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public void setGerarTranferenciaDebitoService(
			GerarTranferenciaDebitoService gerarTranferenciaDebitoService) {
		this.gerarTranferenciaDebitoService = gerarTranferenciaDebitoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

}

