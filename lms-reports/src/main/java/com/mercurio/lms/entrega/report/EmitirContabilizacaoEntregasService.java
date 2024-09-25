package com.mercurio.lms.entrega.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.entrega.emitirContabilizacaoEntregasService"
 * @spring.property name="reportName" value="com/mercurio/lms/entrega/report/emitirContabilizacaoEntregas.jasper"
 */
public class EmitirContabilizacaoEntregasService extends ReportServiceSupport {
	
	private ConfiguracoesFacade configuracoesFacade;
	
	
	private enum Peso {MANIFESTADOS_MAIS_DE_UMA_VEZ, TOTAL_MANIFESTADO, MEIO_TRANSP_P, MEIO_TRANSP_T, MEIO_TRANSP};
	private enum Quantidade {TOTAL_MANIFESTADO, MEIO_TRANSP_P, MEIO_TRANSP_T, MEIO_TRANSP};
	private enum Volume {MANIFESTADOS_MAIS_DE_UMA_VEZ, TOTAL_MANIFESTADO, MEIO_TRANSP_P, MEIO_TRANSP_T, MEIO_TRANSP};
	private enum Documentos {MANIFESTADOS_MAIS_DE_UMA_VEZ, TOTAL_MANIFESTADO, MEIO_TRANSP_P, MEIO_TRANSP_T, MEIO_TRANSP};
	private enum From {EQUIPES,INTEGRANTES_EQUIPES,SEM_RETORNO,MEIO_TRANP_T,MEIO_TRANP,MEIO_TRANP_P};
	private enum Projection {ENTREGUES,NAO_ENTREGUES,RECUSA,REEMTREGA_SOLICITADA, MANIFESTADO_MAIS_DE_UMA_VEZ,TOTAL_MANIFESTADO,MEIO_TRANSP_P, MEIO_TRANSP_T ,MEIO_TRANSP}
	 
	private static final String ENTREGUES = "E";
	private static final String ENTREGUES_AEROPORTO = "A";
	private static final String NAO_ENTREGUES = "N";
	private static final String RECUSA = "R";
	private static final String REENTREGA_SOLICITADA = "S";

	public JRReportDataObject execute(Map parametersTemp) throws Exception {
		TypedFlatMap parameters = (TypedFlatMap)parametersTemp;
		StringBuilder sql = new StringBuilder();
		List filters = new ArrayList(10);

		// Dados principais
		sql.append("SELECT DS_ANALISE , SUM(QT_MANIFESTO_VEICULO) AS QT_MANIFESTO_VEICULO, SUM(TOT_DS) AS TOT_DS, SUM(TOT_PS) AS TOT_PS, SUM(TOT_VL) AS TOT_VL, ");
		
		sql.append("(SELECT COUNT(DISTINCT E.ID_EQUIPE) ");
		addFrom(From.EQUIPES,sql,filters,parameters);
		sql.append(") AS EQUIPES, ");
		
		sql.append("(SELECT COUNT(DISTINCT IE.ID_USUARIO) ");
		addFrom(From.INTEGRANTES_EQUIPES,sql,filters,parameters);
		sql.append(") AS INTEGRANTES, ");
		
		sql.append("(SELECT COUNT(DISTINCT MED.ID_MANIFESTO_ENTREGA) ");
		addFrom(From.SEM_RETORNO,sql,filters,parameters);
		sql.append(") AS SEM_RETORNO FROM ( ( ");
		
		//Geração de cada union

		//ENTREGUES
		addStartProjection(Projection.ENTREGUES,sql,filters);
		addDocumentosEntrega(ENTREGUES,ENTREGUES_AEROPORTO,sql,filters);
		sql.append(", ");
		addPesoEntrega(ENTREGUES,ENTREGUES_AEROPORTO,sql,filters);
		sql.append(", \n");
		addVolumeEntrega(ENTREGUES,ENTREGUES_AEROPORTO,sql,filters);
		addFrom(null,sql,filters,parameters);
		
		addUnion(sql);
		
		//NÃO ENTREGUES
		addStartProjection(Projection.NAO_ENTREGUES,sql,filters);
		addDocumentosDefault(NAO_ENTREGUES,sql,filters);
		sql.append(", ");
		addPesoDefault(NAO_ENTREGUES,sql,filters);
		sql.append(", \n");
		addVolumeDefault(NAO_ENTREGUES,sql,filters);
		addFrom(null,sql,filters,parameters);
		
		addUnion(sql);
		
		//RECUSA
		addStartProjection(Projection.RECUSA,sql,filters);
		addDocumentosDefault(RECUSA,sql,filters);
		sql.append(", ");
		addPesoDefault(RECUSA,sql,filters);
		sql.append(", \n");
		addVolumeDefault(RECUSA,sql,filters);
		addFrom(null,sql,filters,parameters);
		
		addUnion(sql);
		
		//REENTREGA SOLICITADA
		addStartProjection(Projection.REEMTREGA_SOLICITADA,sql,filters);
		addDocumentosDefault(REENTREGA_SOLICITADA,sql,filters);
		sql.append(", ");
		addPesoDefault(REENTREGA_SOLICITADA,sql,filters);
		sql.append(", \n");
		addVolumeDefault(REENTREGA_SOLICITADA,sql,filters);
		addFrom(null,sql,filters,parameters);
		
		addUnion(sql);
		
		//MANIFESTADOS MAIS DE UMA VEZ
		addStartProjection(Projection.MANIFESTADO_MAIS_DE_UMA_VEZ,sql,filters);
		addSqlFromWhereManifestadoMaisDeUmaVez(sql, filters, parameters);
		
		addUnion(sql);
		
		
		//TOTAL MANIFESTADO
		
		addStartProjection(Projection.TOTAL_MANIFESTADO,sql,filters);
		addQuantidade(Quantidade.TOTAL_MANIFESTADO,sql,filters,parameters);
		addDocumentos(Documentos.TOTAL_MANIFESTADO,sql,filters,parameters);
		sql.append(", ");
		addPeso(Peso.TOTAL_MANIFESTADO,sql,filters,parameters);
		sql.append(", \n");
		addVolume(Volume.TOTAL_MANIFESTADO,sql,filters,parameters);
		addFrom(null,sql,filters,parameters);

		
		addUnion(sql);
		
		//MEIO DE TRANSPORTE PROPRIOS
		
		addStartProjection(Projection.MEIO_TRANSP_P,sql,filters);
		addQuantidade(Quantidade.MEIO_TRANSP_P,sql,filters,parameters);
		addDocumentos(Documentos.MEIO_TRANSP_P,sql,filters,parameters);
		sql.append(", ");
		addPeso(Peso.MEIO_TRANSP_P,sql,filters,parameters);
		sql.append(", \n");
		addVolume(Volume.MEIO_TRANSP_P,sql,filters,parameters);
		addFrom(From.MEIO_TRANP,sql,filters,null);

		addUnion(sql);
		
		//MEIO DE TRANSPORTE DE TERCEIROS
		
		addStartProjection(Projection.MEIO_TRANSP_T,sql,filters);
		addQuantidade(Quantidade.MEIO_TRANSP_T,sql,filters,parameters);
		addDocumentos(Documentos.MEIO_TRANSP_T,sql,filters,parameters);
		sql.append(", ");
		addPeso(Peso.MEIO_TRANSP_T,sql,filters,parameters);
		sql.append(", \n");
		addVolume(Volume.MEIO_TRANSP_T,sql,filters,parameters);
		addFrom(From.MEIO_TRANP,sql,filters,null);

		addUnion(sql);
		
		
		//MEIO DE TRANSPORTE DE TERCEIROS
		
		addStartProjection(Projection.MEIO_TRANSP,sql,filters);
		addQuantidade(Quantidade.MEIO_TRANSP,sql,filters,parameters);
		addDocumentos(Documentos.MEIO_TRANSP,sql,filters,parameters);
		sql.append(", ");
		addPeso(Peso.MEIO_TRANSP,sql,filters,parameters);
		sql.append(", \n");
		addVolume(Volume.MEIO_TRANSP,sql,filters,parameters);
		addFrom(From.MEIO_TRANP,sql,filters,null);		
		
		sql.append(") ) GROUP BY DS_ANALISE, ORDEM ORDER BY ORDEM");
							   
		JRReportDataObject jr = executeQuery(sql.toString(),filters.toArray());
		
		SqlTemplate parametrosPesquisa = createSqlTemplate();
		parametrosPesquisa.addFilterSummary("filial",new StringBuffer(parameters.getString("filial.sgFilial")).append(" - ").append(parameters.getString("filial.pessoa.nmFantasia")).toString());
		if (StringUtils.isNotBlank(parameters.getString("tpManifesto.description"))) {
			parametrosPesquisa.addFilterSummary("tipoDeManifesto",parameters.getString("tpManifesto.description"));
		}
		parametrosPesquisa.addFilterSummary("periodoEmissaoInicial",parameters.getYearMonthDay("dhEmissaoInicial"));
		parametrosPesquisa.addFilterSummary("periodoEmissaoFinal",parameters.getYearMonthDay("dhEmissaoFinal"));
		
		Map parametersReport = new HashMap();
		parametersReport.put("parametrosPesquisa",parametrosPesquisa.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM,parameters.get("tpFormatoRelatorio"));

		jr.setParameters(parametersReport);
		logger.warn(sql.toString());
		return jr; 
	}

	@SuppressWarnings("unchecked")
	private void addSqlFromWhereManifestadoMaisDeUmaVez(StringBuilder sql, List filters, TypedFlatMap parameters){
		sql.append("FROM DOCTO_SERVICO D ");
		sql.append("WHERE ");
		sql.append("EXISTS (SELECT 1 ");
		sql.append(" FROM ");
		sql.append(" MANIFESTO_ENTREGA_DOCUMENTO MED1, ");
		sql.append(" MANIFESTO_ENTREGA ME1, ");
		sql.append(" MANIFESTO M1 ");
		sql.append(" WHERE ");
		sql.append(" 	MED1.ID_DOCTO_SERVICO = D.ID_DOCTO_SERVICO ");
		sql.append(" 	AND MED1.ID_MANIFESTO_ENTREGA = ME1.ID_MANIFESTO_ENTREGA  ");
		sql.append("	AND ME1.ID_MANIFESTO_ENTREGA = M1.ID_MANIFESTO ");
		sql.append("	AND M1.TP_STATUS_MANIFESTO <> 'CA' ");
		sql.append("	AND M1.TP_MANIFESTO = 'E' ");
		sql.append("	AND M1.ID_FILIAL_ORIGEM = ? ");
		if (StringUtils.isNotBlank(parameters.getString("tpManifesto.value"))) {
			sql.append("	AND M1.TP_MANIFESTO_ENTREGA = ? ");
		}
		sql.append(" 	AND TRUNC(CAST(M1.DH_EMISSAO_MANIFESTO AS DATE)) >= ? ");
		sql.append("	AND TRUNC(CAST(M1.DH_EMISSAO_MANIFESTO AS DATE)) <= ? ");
		sql.append(") ");
		
		filters.add(parameters.getLong("filial.idFilial"));
		if (StringUtils.isNotBlank(parameters.getString("tpManifesto.value"))) {
			filters.add(parameters.getString("tpManifesto.value"));
		}
		filters.add(parameters.getYearMonthDay("dhEmissaoInicial"));
		filters.add(parameters.getYearMonthDay("dhEmissaoFinal"));

		// Este EXISTS NÃO DEVE considerar as datas informadas no filtro de tela, pois deve pegar todos documentos manifestados mais de uma vez em qualquer período.
		sql.append("AND EXISTS( SELECT "); 
		sql.append(" MED2.ID_DOCTO_SERVICO, ");
		sql.append(" COUNT(*) ");
		sql.append(" FROM ");
		sql.append(" MANIFESTO_ENTREGA_DOCUMENTO MED2, ");
		sql.append(" MANIFESTO_ENTREGA ME2, ");
		sql.append(" MANIFESTO M2 ");
		sql.append(" WHERE ");
		sql.append("	MED2.ID_DOCTO_SERVICO = D.ID_DOCTO_SERVICO ");
		sql.append("	AND MED2.ID_MANIFESTO_ENTREGA = ME2.ID_MANIFESTO_ENTREGA ");
		sql.append("	AND ME2.ID_MANIFESTO_ENTREGA = M2.ID_MANIFESTO ");
		sql.append("	AND M2.TP_STATUS_MANIFESTO <> 'CA' ");
		sql.append(" 	AND M2.TP_MANIFESTO = 'E' ");
		sql.append(" 	AND M2.ID_FILIAL_ORIGEM = ? ");
		if (StringUtils.isNotBlank(parameters.getString("tpManifesto.value"))) {
			sql.append(" 	AND M2.TP_MANIFESTO_ENTREGA = ? ");
		}
		sql.append(" GROUP BY ");
		sql.append(" 	MED2.ID_DOCTO_SERVICO ");
		sql.append(" 	HAVING COUNT(*) > 1 ");
		sql.append(") ");
		
		filters.add(parameters.getLong("filial.idFilial"));
		if (StringUtils.isNotBlank(parameters.getString("tpManifesto.value"))) {
			filters.add(parameters.getString("tpManifesto.value"));
		}
	}
	
	private void addDocumentosEntrega(String tpOcorrenciaEntrega, String tpOcorrenciaEntregaAeroporto, StringBuilder sql, List filters) {
		
		sql.append("(SELECT COUNT(DISTINCT MED.ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
		 	.append("WHERE OE.TP_OCORRENCIA IN (?,?) AND MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_DS ");
		
		filters.add(tpOcorrenciaEntrega);
		filters.add(tpOcorrenciaEntregaAeroporto);
		
	}
	
	private void addPesoEntrega(String tpOcorrencia,  String tpOcorrenciaEntregaAeroporto, StringBuilder sql, List filters) {
		
		sql.append("(SELECT ") 
				.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
			.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
				.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
			.append("WHERE TP_OCORRENCIA IN (?,?) AND ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_PS ");
		
		filters.add(tpOcorrencia);
		filters.add(tpOcorrenciaEntregaAeroporto);
	}
	
	private void addVolumeEntrega(String tpOcorrencia, String tpOcorrenciaEntregaAeroporto, StringBuilder sql, List filters) {				
		
		sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
				.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("WHERE OE.TP_OCORRENCIA IN (?,?) AND MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_VL ");
		
		filters.add(tpOcorrencia);
		filters.add(tpOcorrenciaEntregaAeroporto);
	}
	
	private void addDocumentosDefault(String tpOcorrencia, StringBuilder sql, List filters) {
		
		sql.append("(SELECT COUNT(DISTINCT MED.ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
		 	.append("WHERE OE.TP_OCORRENCIA = ? AND MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_DS ");
		
		filters.add(tpOcorrencia);
		
	}
	private void addPesoDefault(String tpOcorrencia, StringBuilder sql, List filters) {
		
		sql.append("(SELECT ") 
				.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
			.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
				.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
			.append("WHERE TP_OCORRENCIA = ? AND ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_PS ");
		
		filters.add(tpOcorrencia);
		
	} 
	private void addVolumeDefault(String tpOcorrencia, StringBuilder sql, List filters) {				
		
		sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
				.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("WHERE OE.TP_OCORRENCIA = ? AND MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_VL ");
		
		filters.add(tpOcorrencia);
		
	}
	private void addUnion(StringBuilder sql) {
		sql.append(" ) UNION ( ");
	}
	
	private void addStartProjection(Projection projection, StringBuilder sql, List filters) {
		
		String key = null;
		Integer order = null;
		if (projection == Projection.ENTREGUES) {
			key = "entregues";
			order = 0;
		}else if (projection == Projection.NAO_ENTREGUES) {
			key = "naoEntregues";
			order = 1;
		}else if (projection == Projection.RECUSA) {
			key = "recusas";
			order = 2;
		}else if (projection == Projection.REEMTREGA_SOLICITADA) {
			key = "reentregaSolicitada";
			order = 3;
		}else if (projection == Projection.MANIFESTADO_MAIS_DE_UMA_VEZ) {
			key = "manifestadoMaisUmaVez";
			order = 4;
		}else if (projection == Projection.TOTAL_MANIFESTADO) {
			key = "totalManifestados";
			order = 5;
		}else if (projection == Projection.MEIO_TRANSP_P) {
			key = "meiosTransporteProprios";
			order = 6;
		}else if (projection == Projection.MEIO_TRANSP_T) {
			key = "meiosTransporteTerceiros";
			order = 7;
		}else if (projection == Projection.MEIO_TRANSP) {
			key = "meiosTransporteUtilizados";
			order = 8;
		}
		
		filters.add(configuracoesFacade.getMensagem(key));
		filters.add(order);

		
		if (projection == Projection.TOTAL_MANIFESTADO) {
			
			sql.append("SELECT ? AS DS_ANALISE, ? AS ORDEM, ME.ID_MANIFESTO_ENTREGA, ");
			
		}else if (projection == Projection.MEIO_TRANSP_P ||
					projection == Projection.MEIO_TRANSP_T ||
					projection == Projection.MEIO_TRANSP) {
			
			sql.append("SELECT ? AS DS_ANALISE, ? AS ORDEM, 0 AS ID_MANIFESTO_ENTREGA, ");
			
		}else if (projection == Projection.MANIFESTADO_MAIS_DE_UMA_VEZ){
			
			sql.append("SELECT ? AS DS_ANALISE, ? AS ORDEM, NULL AS ID_MANIFESTO_ENTREGA, NULL AS QT_MANIFESTO_VEICULO, COUNT(*) AS TOT_DS, SUM(CASE WHEN NVL(D.PS_AFORADO,0) < NVL(D.PS_REAL,0) THEN D.PS_REAL ELSE D.PS_AFORADO END) AS TOT_PS, SUM(D.QT_VOLUMES) AS TOT_VL ");
			
		} else {	
			
			sql.append("SELECT ? AS DS_ANALISE, ? AS ORDEM, ME.ID_MANIFESTO_ENTREGA, NULL AS QT_MANIFESTO_VEICULO, ");
			
		}
		
	}
	
	private void addQuantidade(Quantidade quantidade, StringBuilder sql, List filters, TypedFlatMap parameters) {				
		if (quantidade == Quantidade.TOTAL_MANIFESTADO) {

			sql.append("(SELECT COUNT(DISTINCT MED.ID_MANIFESTO_ENTREGA) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
			.append("WHERE MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS QT_MANIFESTO_VEICULO,");
		
			
		}else if (quantidade == Quantidade.MEIO_TRANSP_P) {
			sql.append("(SELECT COUNT(DISTINCT CC.ID_TRANSPORTADO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			
			addFrom(From.MEIO_TRANP_P,sql,filters,parameters);
			sql.append(") AS QT_MANIFESTO_VEICULO, ");
			
		}else if (quantidade == Quantidade.MEIO_TRANSP_T) {
			sql.append("(SELECT COUNT(DISTINCT CC.ID_TRANSPORTADO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			
			addFrom(From.MEIO_TRANP_T,sql,filters,parameters);
			sql.append(") AS QT_MANIFESTO_VEICULO, ");
			
		}else if (quantidade == Quantidade.MEIO_TRANSP) {
			sql.append("(SELECT COUNT(DISTINCT CC.ID_TRANSPORTADO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			
			addFrom(From.MEIO_TRANP,sql,filters,parameters);
			sql.append(") AS QT_MANIFESTO_VEICULO, ");
		}
	}
	
	private void addDocumentos(Documentos documentos, StringBuilder sql, List filters, TypedFlatMap parameters) {
		if (documentos == Documentos.MANIFESTADOS_MAIS_DE_UMA_VEZ) {
			
			sql.append("(SELECT COUNT(ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
 					.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
 				.append("WHERE MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA AND  ")
 					.append("(SELECT COUNT(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED2 WHERE ")
 					.append("MED2.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO) > 1) AS TOT_DS ");
			
		}else if (documentos == Documentos.TOTAL_MANIFESTADO) {
			
			sql.append("(SELECT COUNT(MED.ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
			.append("WHERE MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_DS ");
	
		}else if (documentos == Documentos.MEIO_TRANSP_P) {
		
			sql.append("(SELECT COUNT(ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			
			addFrom(From.MEIO_TRANP_P,sql,filters,parameters);
			sql.append(") AS TOT_DS ");
 
			
		}else if (documentos == Documentos.MEIO_TRANSP_T) {
		
			sql.append("(SELECT COUNT(ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			
			addFrom(From.MEIO_TRANP_T,sql,filters,parameters);
			sql.append(") AS TOT_DS ");

			
		}else if (documentos == Documentos.MEIO_TRANSP) {
		
			sql.append("(SELECT COUNT(ID_DOCTO_SERVICO) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");

			addFrom(From.MEIO_TRANP,sql,filters,parameters);
			sql.append(") AS TOT_DS ");

		}
		
	}
	private void addPeso(Peso peso, StringBuilder sql, List filters,TypedFlatMap parameters) {
		if (peso == Peso.MANIFESTADOS_MAIS_DE_UMA_VEZ) {
			 
			sql.append("(SELECT ") 
					.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
				.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("WHERE MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA AND ")
					.append("(SELECT COUNT(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED2 WHERE ")
					.append("MED2.ID_DOCTO_SERVICO = ID_DOCTO_SERVICO) > 1) AS TOT_PS ");
			
		}else if (peso == Peso.TOTAL_MANIFESTADO) {
			
			sql.append("(SELECT ")    
					.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
				.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("WHERE ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_PS ");
		}else if (peso == Peso.MEIO_TRANSP_P) {
			
			sql.append("(SELECT ") 
					.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
				.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
					
			addFrom(From.MEIO_TRANP_P,sql,filters,parameters);
			sql.append(") AS TOT_PS ");
	
		}else if (peso == Peso.MEIO_TRANSP_T) {
			
			sql.append("(SELECT ") 
					.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
				.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			addFrom(From.MEIO_TRANP_T,sql,filters,parameters);
			sql.append(") AS TOT_PS ");
	
		}else if (peso == Peso.MEIO_TRANSP) {
			
			sql.append("(SELECT ") 
					.append("SUM(CASE WHEN NVL(DS.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE DS.PS_AFORADO END) ")
				.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
			addFrom(From.MEIO_TRANP,sql,filters,parameters);
			sql.append(") AS TOT_PS ");

		}

	}
	private void addVolume(Volume volumes, StringBuilder sql, List filters, TypedFlatMap parameters) {				
		if (volumes == Volume.MANIFESTADOS_MAIS_DE_UMA_VEZ) {
			
			sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN OCORRENCIA_ENTREGA OE ON OE.ID_OCORRENCIA_ENTREGA = MED.ID_OCORRENCIA_ENTREGA ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("WHERE MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA AND ")
					.append("(SELECT COUNT(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED2 WHERE ")
					.append("MED2.ID_DOCTO_SERVICO = ID_DOCTO_SERVICO) > 1) AS TOT_VL ");
			
		}else if (volumes == Volume.TOTAL_MANIFESTADO) {

			sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("WHERE MED.ID_MANIFESTO_ENTREGA = ME.ID_MANIFESTO_ENTREGA) AS TOT_VL ");
			
		}else if (volumes == Volume.MEIO_TRANSP_P) {
			
			sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ");
					
			addFrom(From.MEIO_TRANP_P,sql,filters,parameters);
			sql.append(") AS TOT_VL ");

		}else if (volumes == Volume.MEIO_TRANSP_T) {
	
			sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ");
					
			addFrom(From.MEIO_TRANP_T,sql,filters,parameters);
			sql.append(") AS TOT_VL ");
			
		}else if (volumes == Volume.MEIO_TRANSP) {
			
			sql.append("(SELECT SUM(QT_VOLUMES) FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
					.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN MANIFESTO_ENTREGA ME ON M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
					.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
					.append("INNER JOIN MEIO_TRANSPORTE MT ON MT.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO ")
					.append("INNER JOIN DOCTO_SERVICO DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ");
					
			addFrom(From.MEIO_TRANP,sql,filters,parameters);
			sql.append(") AS TOT_VL ");


		}
	}
	
	private void addFrom(From from, StringBuilder sql, List filters, TypedFlatMap parameters) {
		
		if (from == From.INTEGRANTES_EQUIPES) {

			sql.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN DOCTO_SERVICO     DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ")
				.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ")
				.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
				.append("INNER JOIN EQUIPE_OPERACAO EO ON EO.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ") 
				.append("INNER JOIN INTEGRANTE_EQ_OPERAC IE ON IE.ID_EQUIPE_OPERACAO = EO.ID_EQUIPE_OPERACAO ");
			
		}else if (from == From.EQUIPES) {
			
			sql.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA  ")
				.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ")
				.append("INNER JOIN DOCTO_SERVICO     DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("INNER JOIN CONTROLE_CARGA CC ON CC.ID_CONTROLE_CARGA = M.ID_CONTROLE_CARGA ")
				.append("INNER JOIN EQUIPE_OPERACAO EO ON EO.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
				.append("INNER JOIN EQUIPE E ON E.ID_EQUIPE = EO.ID_EQUIPE ");
			
		}else if (from == From.SEM_RETORNO) {
			
			sql.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN DOCTO_SERVICO     DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
				.append("INNER JOIN MANIFESTO_ENTREGA ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ")
				.append("INNER JOIN MANIFESTO M ON M.ID_MANIFESTO = MED.ID_MANIFESTO_ENTREGA ");
			
		}else if (from == From.MEIO_TRANP || from == From.MEIO_TRANP_P || from == From.MEIO_TRANP_T) {
			if (parameters == null)
				sql.append(" FROM DUAL ");
			else
				addWhere(from,sql,filters,parameters);
			return;

		}else{
			
			sql.append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
				.append("INNER JOIN MANIFESTO_ENTREGA  ME ON ME.ID_MANIFESTO_ENTREGA = MED.ID_MANIFESTO_ENTREGA ")
				.append("INNER JOIN MANIFESTO          M ON MED.ID_MANIFESTO_ENTREGA = M.ID_MANIFESTO ")
				.append("INNER JOIN DOCTO_SERVICO     DS ON DS.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ");
			
		}

		addWhere(from,sql,filters,parameters);
	}
	
	private void addWhere(From from, StringBuilder sql, List filters, TypedFlatMap parameters) {
		sql.append("WHERE trunc(cast(M.DH_EMISSAO_MANIFESTO as date)) >= ? AND trunc(cast(M.DH_EMISSAO_MANIFESTO as date)) <= ? " +
				   "AND M.TP_MANIFESTO = 'E' " +
				   "AND M.TP_STATUS_MANIFESTO <> 'CA' " +
				   "AND M.ID_FILIAL_ORIGEM = ? ");
		filters.add(parameters.getYearMonthDay("dhEmissaoInicial"));
		filters.add(parameters.getYearMonthDay("dhEmissaoFinal"));
		filters.add(parameters.getLong("filial.idFilial"));
		

		if (StringUtils.isNotBlank(parameters.getString("tpManifesto.value"))) {
			sql.append("AND M.TP_MANIFESTO_ENTREGA = ? ");
			filters.add(parameters.getString("tpManifesto.value"));
		}
		if (from == From.SEM_RETORNO)
			sql.append("AND DH_FECHAMENTO IS NULL ");
		
		if (from == From.EQUIPES || from == From.INTEGRANTES_EQUIPES) {
			sql.append("AND EO.ID_CARREGAMENTO_DESCARGA IS NULL ");
		}
		
		if (from == From.MEIO_TRANP_P || from == From.MEIO_TRANP_T) {
			if (from == From.MEIO_TRANP_P)
				sql.append("AND MT.TP_VINCULO = ?");
			else
				sql.append("AND MT.TP_VINCULO <> ?");
			filters.add("P");
		}
	}
    
	
	//Método para tratar os documentos manifestados mais de uma vez
	private void addSqlFromManifestados(StringBuffer sql, List filters, TypedFlatMap parameters){
		
	       sql.append(" FROM (SELECT COUNT(DISTINCT MED.ID_DOCTO_SERVICO) AS TOT_DS, ")
	             .append("SUM(CASE WHEN NVL(D.PS_AFORADO,0) < NVL(PS_REAL,0) THEN PS_REAL ELSE D.PS_AFORADO END) AS TOT_PS, ")
	             .append("SUM(QT_VOLUMES) AS TOT_VL ")
	       .append("FROM MANIFESTO_ENTREGA_DOCUMENTO MED ")
	       .append("INNER JOIN DOCTO_SERVICO D ON D.ID_DOCTO_SERVICO = MED.ID_DOCTO_SERVICO ")
	       .append(" AND (SELECT COUNT(*) FROM MANIFESTO_ENTREGA_DOCUMENTO MED1 ")
	                           .append(" WHERE MED.ID_DOCTO_SERVICO = MED1.ID_DOCTO_SERVICO ")
	                           .append(" AND EXISTS (SELECT 1 FROM MANIFESTO M, MANIFESTO_ENTREGA ME ") 
	                                                 .append(" WHERE M.ID_FILIAL_ORIGEM = ? ")
	                                                 .append(" AND M.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ")
	                                                 .append(" AND ME.ID_MANIFESTO_ENTREGA = MED1.ID_MANIFESTO_ENTREGA ") 
	                                                 .append(" AND M.TP_MANIFESTO = 'E' ")
	                                                 .append(" AND M.TP_STATUS_MANIFESTO <> 'CA' ")
	                                                 .append(" AND trunc(cast(M.DH_EMISSAO_MANIFESTO as date)) >= ? AND trunc(cast(M.DH_EMISSAO_MANIFESTO as date)) <= ? ");
	       
			filters.add(parameters.getLong("filial.idFilial"));
			filters.add(parameters.getYearMonthDay("dhEmissaoInicial"));
			filters.add(parameters.getYearMonthDay("dhEmissaoFinal"));  
	       
			if (!parameters.getString("tpManifesto.value").equals("")) {
				sql.append(" AND M.TP_MANIFESTO_ENTREGA = ? ");
				filters.add(parameters.getString("tpManifesto.value"));
			}
			
			sql.append(" )) > 1) TOTAIS");
		
		
		
		
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
