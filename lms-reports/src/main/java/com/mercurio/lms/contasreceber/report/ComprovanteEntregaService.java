package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;


/**
 * @author Hector junior
 *
 * @spring.bean id="lms.contasreceber.comprovanteEntregaService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirComprovanteEntrega.jasper"
 */
public class ComprovanteEntregaService extends ReportServiceSupport{
	
	private static String joinCTRC = " INNER JOIN CONHECIMENTO CO ON DDSF.ID_DOCTO_SERVICO = CO.ID_CONHECIMENTO ";
	private static String joinCRT = " INNER JOIN CTO_INTERNACIONAL CI ON DDSF.ID_DOCTO_SERVICO = CI.ID_CTO_INTERNACIONAL ";
	private static String joinNFS = " INNER JOIN NOTA_FISCAL_SERVICO NFS ON DDSF.ID_DOCTO_SERVICO = NFS.ID_NOTA_FISCAL_SERVICO ";
	private static String joinNDN = " INNER JOIN NOTA_DEBITO_NACIONAL NDN ON DDSF.ID_DOCTO_SERVICO = NDN.ID_NOTA_DEBITO_NACIONAL ";
	/** 
	 * Método invocado pela EmitirAliquotasICMSAction, é o método default do Struts
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/** Monta o sql */
		StringBuffer sql = new StringBuffer(this.getSqlTemplateCTRC(tfm).getSql());
		sql.append(" UNION ");
		sql.append(this.getSqlTemplateCRT().getSql());
		sql.append(" UNION ");
		sql.append(this.getSqlTemplateNFS().getSql());
		sql.append(" UNION ");
		sql.append(this.getSqlTemplateNDN().getSql());
		
		/** Replica o conteúdo do array de Critéria 4 vezes, para ser usado por todos sqlTemplates */
		Object[] array = new Object[this.getSqlTemplateCTRC(tfm).getCriteria().length * 4];
		Object[] arrayTmp = this.getSqlTemplateCTRC(tfm).getCriteria();
			
		if(this.getSqlTemplateCTRC(tfm).getCriteria() != null && this.getSqlTemplateCTRC(tfm).getCriteria().length > 0){	
			/**
			 *  1° array de origem, 2° posição de inicio do array de origem, 3° array de destino,
			 *  4° posição do array de destino onde será copiado o elemento do array de origem, 
			 *  5° número de elementos que o array de destino receberá do array de origem
			 */
			System.arraycopy(arrayTmp, 0, array, 0, 1);
			System.arraycopy(arrayTmp, 0, array, 1, 1);
			System.arraycopy(arrayTmp, 0, array, 2, 1);
			System.arraycopy(arrayTmp, 0, array, 3, 1);
		}
		
		/** Cria o JRReportDataObject para ser passado para o relatório */
		JRReportDataObject jr = executeQuery(sql.toString(), array);
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", this.getSqlTemplateCTRC(tfm).getFilterSummary());
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		jr.setParameters(parametersReport);
		return jr;
	}

	/** Bloco de código que será utilizado para todos select */
	public void defaultSql(SqlTemplate sql, int tipoDoc){
		
		
		sql.addProjection("FIL.SG_FILIAL", "SGFILIAL");
		sql.addProjection("DS.TP_DOCUMENTO_SERVICO", "TP_DOCUMENTO");
		sql.addProjection("F.NR_FATURA", "NRFATURA");
		sql.addProjection("F.ID_FATURA", "QUEBRAFATURA");
		sql.addProjection("P.NM_PESSOA", "NMPESSOA");
		sql.addProjection("P.NR_IDENTIFICACAO", "NRIDENTIFICACAO");
		sql.addProjection("P.TP_IDENTIFICACAO", "TPIDENTIFICACAO");
		sql.addProjection("P.ID_PESSOA", "QUEBRAPESSOA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LM.DS_LOCALIZACAO_MERCADORIA_I", "DS_LOCALIZACAO_MERCADORIA"));
		sql.addProjection("DS.OB_COMPLEMENTO_LOCALIZACAO", "OB_COMPLEMENTO_LOCALIZACAO");
		sql.addProjection("(SELECT MAX(EDSSUB.DH_EVENTO) " +
				"FROM    DESCRICAO_EVENTO DESUB, " +
						"EVENTO ESUB, " +
						"EVENTO_DOCUMENTO_SERVICO EDSSUB " +
				"WHERE   DESUB.ID_DESCRICAO_EVENTO = ESUB.ID_DESCRICAO_EVENTO " +
						"AND ESUB.ID_EVENTO = EDSSUB.ID_EVENTO " +
						"AND EDSSUB.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
						"AND ESUB.CD_EVENTO IN (21,90,91) " +
						"AND EDSSUB.BL_EVENTO_CANCELADO = 'N' " +
						") ENTREGA");
		
		
		StringBuffer from = new StringBuffer("FATURA F " +
					"INNER JOIN ITEM_FATURA IF " +
						"ON F.ID_FATURA =  IF.ID_FATURA " +
					"INNER JOIN FILIAL FIL " +
						"ON F.ID_FILIAL = FIL.ID_FILIAL " +
					"INNER JOIN DEVEDOR_DOC_SERV_FAT DDSF " +
						"ON IF.ID_DEVEDOR_DOC_SERV_FAT = DDSF.ID_DEVEDOR_DOC_SERV_FAT " +
					"INNER JOIN DOCTO_SERVICO DS " +
						"ON DS.ID_DOCTO_SERVICO = DDSF.ID_DOCTO_SERVICO " +
					"INNER JOIN FILIAL FILORIGEM " +
						"ON DS.ID_FILIAL_ORIGEM = FILORIGEM.ID_FILIAL " +
					"INNER JOIN CLIENTE C " +
						"ON DDSF.ID_CLIENTE = C.ID_CLIENTE " +
					"INNER JOIN PESSOA P " +
						"ON C.ID_CLIENTE = P.ID_PESSOA " +
					"LEFT JOIN NOTA_FISCAL_CONHECIMENTO NFC " +
						"ON DS.ID_DOCTO_SERVICO = NFC.ID_CONHECIMENTO " +
					"INNER JOIN EVENTO_DOCUMENTO_SERVICO EDS " +
						"ON DS.ID_DOCTO_SERVICO = EDS.ID_DOCTO_SERVICO " +
					"INNER JOIN EVENTO E " +
						"ON EDS.ID_EVENTO = E.ID_EVENTO " +
					"INNER JOIN DESCRICAO_EVENTO DE " +
						"ON E.ID_DESCRICAO_EVENTO = DE.ID_DESCRICAO_EVENTO " +
					"INNER JOIN LOCALIZACAO_MERCADORIA LM " +
						"ON DS.ID_LOCALIZACAO_MERCADORIA = LM.ID_LOCALIZACAO_MERCADORIA ");
					
					/** Verifica o tio de documento para utilizar o join adequado */
					if(tipoDoc == 1){ // CTRC
						from.append(ComprovanteEntregaService.joinCTRC);
						sql.addProjection("decode(DS.TP_DOCUMENTO_SERVICO,'CTR','CTRC'," +
								"DS.TP_DOCUMENTO_SERVICO)", "TIPO");
					}else if(tipoDoc == 2){ // CRT
						from.append(ComprovanteEntregaService.joinCRT);
						sql.addProjection("'CRT'", "TIPO");
					}else if(tipoDoc == 3){ // NFS
						from.append(ComprovanteEntregaService.joinNFS);
						sql.addProjection("DS.TP_DOCUMENTO_SERVICO", "TIPO");
					}else{ // NDN
						from.append(ComprovanteEntregaService.joinNDN);
						sql.addProjection("'NDN'", "TIPO");
					}
					
		sql.addFrom(from.toString());
		
					
		
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	public SqlTemplate getSqlTemplateCTRC(TypedFlatMap tfm) throws Exception{
		
		/* ###########  CONHECIMENTO (CTRC) ########### */
		
		SqlTemplate sqlCTRC = this.createSqlTemplate();
		
		this.defaultSql(sqlCTRC, 1);
		
		sqlCTRC.addProjection("FILORIGEM.SG_FILIAL", "SG_FILIAL");
		sqlCTRC.addProjection("CO.NR_CONHECIMENTO", "DOCUMENTO");
		sqlCTRC.addProjection("NFC.NR_NOTA_FISCAL", "NOTA_FISCAL");
		
		/** Resgata o parametro  do request */
		String siglaFilial = tfm.getString("siglaFilial");
		String nrFatura = tfm.getString("nrFatura");
		Long idFatura = tfm.getLong("fatura.idFatura");
		
		/** Verifica se o parametro não é nulo, caso não seja, adiciona o parametro como critério na consulta,
		 *  e seta o mesmo como um campo utilizado no filtro 
		 */
		if(idFatura != null && StringUtils.isNotBlank(idFatura.toString())) {
			sqlCTRC.addCriteria("F.ID_FATURA", "=", idFatura);
			sqlCTRC.addFilterSummary("fatura", siglaFilial + " " + com.mercurio.lms.util.FormatUtils.completaDados(nrFatura, "0", 10, 0, true));
		}
		return sqlCTRC;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplateCRT() throws Exception{
		
		/* ########### CTO_INTERNACIONAL (CRT) */
		
		SqlTemplate sqlCRT = this.createSqlTemplate();
		
		this.defaultSql(sqlCRT, 2);
		
		sqlCRT.addProjection("FILORIGEM.SG_FILIAL", "SG_FILIAL");
		sqlCRT.addProjection("CI.NR_CRT", "DOCUMENTO");
		sqlCRT.addProjection("NULL", "NOTA_FISCAL");
		
		sqlCRT.addCustomCriteria("F.ID_FATURA = ?");
		
		return sqlCRT;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplateNFS() throws Exception{
		
		/* ########### NOTA_FISCAL_SERVICO (NFS) */
		
		SqlTemplate sqlNFS = this.createSqlTemplate();
		
		this.defaultSql(sqlNFS, 3);
		
		sqlNFS.addProjection("FILORIGEM.SG_FILIAL", "SG_FILIAL");
		sqlNFS.addProjection("NFS.NR_NOTA_FISCAL_SERVICO", "DOCUMENTO");
		sqlNFS.addProjection("NULL", "NOTA_FISCAL");
		
		sqlNFS.addCustomCriteria("F.ID_FATURA = ?");
		
		return sqlNFS;
	}
	
	/**
	 * Monta o sql principal
	 * @param tfm
	 * @return sql
	 * @throws Exception
	 */		
	private SqlTemplate getSqlTemplateNDN() throws Exception{
		
		/* ########### NOTA_DEBITO_NACIONAL (NDN) */
		
		SqlTemplate sqlNDN = this.createSqlTemplate();
		
		this.defaultSql(sqlNDN, 4);
		
		sqlNDN.addProjection("FILORIGEM.SG_FILIAL", "SG_FILIAL");
		sqlNDN.addProjection("NDN.NR_NOTA_DEBITO_NAC", "DOCUMENTO");
		sqlNDN.addProjection("NULL", "NOTA_FISCAL");
		
		sqlNDN.addCustomCriteria("F.ID_FATURA = ?");
		
		sqlNDN.addOrderBy("1, 3, 5, 12");
		
		return sqlNDN;
	}


}
