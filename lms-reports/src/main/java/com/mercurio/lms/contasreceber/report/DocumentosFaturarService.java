package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;


/**
 * @author Hector junior
 *
 * @spring.bean id="lms.contasreceber.documentosFaturarService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirDocumentosFaturar.jasper"
 */
public class DocumentosFaturarService extends ReportServiceSupport {

	/** 
	 * Método invocado pela EmitirAliquotasICMSAction, é o método default do Struts
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
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
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
		
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("FILCOBRANCA.SG_FILIAL", "SIGLAFILIALCOBRANCA");
		sql.addProjection("PESFILCOBRANCA.NM_FANTASIA", "NOMEFILIALCOBRANCA");
		sql.addProjection("P.TP_IDENTIFICACAO", "TPCODCLIENTE");
		sql.addProjection("P.NR_IDENTIFICACAO", "CODCLIENTE");
		sql.addProjection("P.NM_PESSOA", "CLIENTE");
		sql.addProjection("FILORIGEM.SG_FILIAL", "FILIALORIGEM");
		sql.addProjection("FILDESTINO.SG_FILIAL", "FILIALDESTINO");
		sql.addProjection("CI.NR_CRT", "CONHECIMENTO");
		sql.addProjection("DS.DH_EMISSAO", "DATAEMISSAO");
		sql.addProjection("DDSF.VL_DEVIDO", "VALORFRETE");
		sql.addProjection("CI.SG_PAIS", "SGPAIS");
		
		//quando for exportacao eh essa a data que deve ser mostrada
		sql.addProjection("("+
                      " SELECT m.dh_emissao_manifesto dt"+
                       " FROM docto_servico dss "+
                       " INNER JOIN pre_manifesto_documento pmd ON dss.id_docto_servico = pmd.id_docto_servico "+
                       " INNER JOIN manifesto m ON pmd.id_manifesto = m.id_manifesto"+
                      " WHERE "+
                       " DSS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND "+
                       " m.tp_status_manifesto <> 'PM' "+
                       " AND m.tp_status_manifesto <> 'CA'"+ 
                       " AND tp_manifesto = 'V'"+  
                       " and m.id_manifesto in ( "+
                       "                     SELECT min(m.id_manifesto) "+
                       "                       FROM docto_servico dsss  "+
                       "                        INNER JOIN pre_manifesto_documento pmd ON dsss.id_docto_servico = pmd.id_docto_servico "+
                       "                        INNER JOIN manifesto m ON pmd.id_manifesto = m.id_manifesto " +
                       "                      WHERE  "+
                       "                        DSS.ID_DOCTO_SERVICO = DSSS.ID_DOCTO_SERVICO AND "+
                       "                        m.tp_status_manifesto <> 'PM' "+
                       "                        AND m.tp_status_manifesto <> 'CA' "+ 
                       "                        AND tp_manifesto = 'V' "+
                       "                      )"+
                       ") AS DHEXPORTA");
		
		sql.addProjection("(SELECT MAX(EDS.DH_EVENTO) " +
						  "FROM   EVENTO_DOCUMENTO_SERVICO EDS, EVENTO E " +
						  "WHERE  DS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO " +
						  "    AND    EDS.ID_EVENTO = E.ID_EVENTO " +
						  "    AND    E.CD_EVENTO = 105)  AS DATACRUZE ");
		

		
		
		
		sql.addProjection("CI.SG_PAIS || '.' || CI.NR_PERMISSO || '.' || CI.NR_CRT", "MASCARA");
		
		sql.addFrom("DOCTO_SERVICO DS " +
					"INNER JOIN CTO_INTERNACIONAL CI " +
						"ON DS.ID_DOCTO_SERVICO =  CI.ID_CTO_INTERNACIONAL " +
					"INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF " +
						"ON DS.ID_DOCTO_SERVICO = DDSF.ID_DOCTO_SERVICO " +
					"INNER JOIN CLIENTE C " +
						"ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
					"INNER JOIN PESSOA P " +
						"ON C.ID_CLIENTE = P.ID_PESSOA " +
					"INNER JOIN FILIAL FILCOBRANCA " +
						"ON FILCOBRANCA.ID_FILIAL = DDSF.ID_FILIAL " +
					"INNER JOIN PESSOA PESFILCOBRANCA " +
						"ON PESFILCOBRANCA.ID_PESSOA = FILCOBRANCA.ID_FILIAL " +
					"INNER JOIN FILIAL FILORIGEM " +
						"ON FILORIGEM.ID_FILIAL = DS.ID_FILIAL_ORIGEM " +
					"INNER JOIN FILIAL FILDESTINO " +
						"ON FILDESTINO.ID_FILIAL = DS.ID_FILIAL_DESTINO " +
					"LEFT JOIN CENTRALIZADORA_FATURAMENTO CF " +
						"ON (FILCOBRANCA.ID_FILIAL = CF.ID_FILIAL_CENTRALIZADA AND CF.TP_MODAL = 'R' AND CF.TP_ABRANGENCIA = 'I') ");
		
		
		/** Resgata o parametro  do request */
		String nmFilialFaturamento = tfm.getString("filialFaturamento.nmFilial");
		String sgFilialFaturamento = tfm.getString("filialFaturamento.siglaFilial");
		Long idFilialFaturamento = tfm.getLong("filialFaturamento.idFilial");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(idFilialFaturamento != null && StringUtils.isNotBlank(idFilialFaturamento.toString())) {
			sql.addCriteria("CF.ID_FILIAL_CENTRALIZADORA", "=", idFilialFaturamento);
			sql.addFilterSummary("filialFaturamento", sgFilialFaturamento + " - " + nmFilialFaturamento);
		}
		
		/** Resgata o parametro  do request */
		String nmFilialCobranca = tfm.getString("filialCobranca.nmFilial");
		String sgFilialCobranca = tfm.getString("filialCobranca.siglaFilial");
		Long idFilialCobranca = tfm.getLong("filialCobranca.idFilial");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(idFilialCobranca != null && StringUtils.isNotBlank(idFilialCobranca.toString())) {
			sql.addCriteria("DDSF.ID_FILIAL", "=", idFilialCobranca);
			sql.addFilterSummary("filialCobranca", sgFilialCobranca + " - " + nmFilialCobranca);
		}
		
		/** Resgata o parametro  do request */
		String nmCliente = tfm.getString("cliente.nmCliente");
		String nrIdentificacaoCliente = tfm.getString("cliente.nrIdentificacao");
		String tpIdentificacaoCliente = tfm.getString("cliente.tpIdentificacao");
		Long idCliente = tfm.getLong("cliente.idCliente");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(StringUtils.isNotBlank(nrIdentificacaoCliente)) {
			sql.addCriteria("DDSF.ID_CLIENTE", "=", idCliente);
			sql.addFilterSummary("cliente", FormatUtils.formatIdentificacao(tpIdentificacaoCliente, nrIdentificacaoCliente) + " - " + nmCliente);
		}
		
		sql.addOrderBy("FILCOBRANCA.SG_FILIAL, P.NM_PESSOA, FILORIGEM.SG_FILIAL, CI.NR_CRT");
		
		return sql;
	}

	
}
