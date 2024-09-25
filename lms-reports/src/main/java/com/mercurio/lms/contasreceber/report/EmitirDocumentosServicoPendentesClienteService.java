package com.mercurio.lms.contasreceber.report;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * @author Hector Julian Esnaola Junior
 *
 * @spring.bean id="lms.contasreceber.emitirDocumentosServicoPendentesClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirDocumentosServicoPendentesCliente.jasper"
 */
public class EmitirDocumentosServicoPendentesClienteService extends ReportServiceSupport {
	
	/**
	 * Executa o report principal
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param parameters
	 * @return
	 * @throws Exception
	 *
	 */
	@SuppressWarnings("rawtypes")
	public JRReportDataObject execute(Map parameters) throws Exception {
		
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		SqlTemplate sql = mountSqlPrincipal(tfm); 
		
		JRReportDataObject jr =  executeQuery(sql.getSql(), sql.getCriteria());

		// Monta o filterSummary do relatório
		mountFilterSummary(tfm, sql);

		// Monta os parametros do relatório
		jr.setParameters(mountParametersReport(parameters, tfm, sql));
		
		return jr;
		
	}

	/**
	 * Monta os parametros do relatório
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param parameters
	 * @param tfm
	 * @param sql
	 * @param idMoedaDestino
	 * @return
	 *
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Map mountParametersReport(Map parameters, TypedFlatMap tfm, SqlTemplate sql) {
		
		Map parametersReport = new HashMap();
		
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		
		parametersReport.put("TFM", tfm);
		
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		parametersReport.put("diasDeAtraso",tfm.getInteger("diasAtraso") == null ? Integer.valueOf(0) : tfm.getInteger("diasAtraso"));		
		parametersReport.put("idMoedaDestino", tfm.getLong("moeda.idMoeda"));
		
		/* 
		 * Parâmetros para controle de geração dos subreports
		 * Caso não tenha sido informado o cliente e nrIdentificaçãoParcial
		 */
		if( tfm.getLong("cliente.idCliente") == null && (tfm.getString("identificacaoParcial") == null || "".equals(tfm.getString("identificacaoParcial")))){
			
			// Imprime o subreport de filial
			parametersReport.put("imprimeTotalFilial", Boolean.TRUE);
		
			// Caso tenha sido informada a filial 
			if( tfm.getLong("filial.idFilial") != null ){
				// Não imprime o subReport de empresa
				parametersReport.put("imprimeTotalEmpresa", Boolean.FALSE);
			} else {
				// Imprime o subReport de empresa
				parametersReport.put("imprimeTotalEmpresa", Boolean.TRUE);
			}
			
		}else{
			// Não imprime os subReport
			parametersReport.put("imprimeTotalFilial", Boolean.FALSE);

			// Não imprime o subReport de empresa
			parametersReport.put("imprimeTotalEmpresa", Boolean.FALSE);
			
		}
			
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		
		return parametersReport;
	}

	/**
	 * Monta o filterSummary do relatório
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param tfm
	 * @param sql
	 *
	 */
	private void mountFilterSummary(TypedFlatMap tfm, SqlTemplate sql) {
		
		sql.addFilterSummary("filial",tfm.getString("siglaNomeFilial"));
		sql.addFilterSummary("tipoFilial",tfm.getString("dsTipoFilial"));
		sql.addFilterSummary("cliente",tfm.getString("cliente.pessoa.nmPessoa"));
		sql.addFilterSummary("identificacaoParcial",tfm.getString("identificacaoParcial"));
		sql.addFilterSummary("tipoCliente",tfm.getString("dsTipoCliente"));
		
		if( tfm.getYearMonthDay("dtEmissaoAte") != null ){		
			sql.addFilterSummary("emissaoAte",JTFormatUtils.format(tfm.getYearMonthDay("dtEmissaoAte")));
		}
		
		if( tfm.getString("tpFrete") != null ){		
			sql.addFilterSummary("tipoFrete",tfm.getDomainValue("tpFrete").getValue());
		}
		
		sql.addFilterSummary("situacaoCobranca",tfm.getString("dsSituacaoCobranca"));
		sql.addFilterSummary("modal",tfm.getString("dsModal"));
		sql.addFilterSummary("abrangencia",tfm.getString("dsAbrangencia"));
		sql.addFilterSummary("diasAtraso",tfm.getLong("diasAtraso"));
		sql.addFilterSummary("moeda",tfm.getString("dsSiglaSimbolo"));
		
		sql.addFilterSummary("tipoDocumentoServico",tfm.getString("dsTpDocumentoServico"));
		sql.addFilterSummary("situacaoRPS",tfm.getString("dsTpSituacaoRPS"));
	}
	
	/**
	 * Monta o sql do report principal
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param tfm
	 * @return
	 *
	 */
	private SqlTemplate mountSqlPrincipal(TypedFlatMap tfm) {
		
		SqlTemplate sqlPrincipal = createSqlTemplate();
		
		SqlTemplate sqlSubSelect = mountSubSelectSqlPrincipal(tfm);
		
		sqlPrincipal.addProjection("DADOS.*");
		sqlPrincipal.addProjection(PropertyVarcharI18nProjection.createProjection("LOG.DS_TIPO_LOGRADOURO_I")+" || ' ' || EP.DS_ENDERECO || ', ' || EP.NR_ENDERECO || DECODE(EP.DS_COMPLEMENTO, NULL, '', '/' || EP.DS_COMPLEMENTO)", "ENDERECO_CLIENTE");
		sqlPrincipal.addProjection("EP.NR_CEP", "CEP_CLIENTE");
		sqlPrincipal.addProjection("MUN.NM_MUNICIPIO", "MUNICIPIO_CLIENTE");
		sqlPrincipal.addProjection("(SELECT DECODE(TE.NR_DDI, NULL, '', '(' || TE.NR_DDI || ') ') || DECODE(TE.NR_DDD, NULL, '', '(' || TE.NR_DDD || ') ') || TE.NR_TELEFONE FROM TELEFONE_ENDERECO TE WHERE TE.ID_TELEFONE_ENDERECO = (SELECT MIN(TETMP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TETMP WHERE TETMP.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA))", "TELEFONE");
		
		sqlPrincipal.addFrom("( " + sqlSubSelect.getSql() + " )", "DADOS");
		sqlPrincipal.addCriteriaValue(sqlSubSelect.getCriteria());
		
		sqlPrincipal.addFrom("ENDERECO_PESSOA", "EP");
		
		sqlPrincipal.addFrom("TIPO_LOGRADOURO", "LOG");
		sqlPrincipal.addFrom("MUNICIPIO", "MUN");
		
		sqlPrincipal.addCustomCriteria("EP.ID_ENDERECO_PESSOA = F_BUSCA_ENDERECO_PESSOA(DADOS.ID_CLIENTE, 'COB', ?)", JTDateTimeUtils.getDataAtual());
		sqlPrincipal.addJoin("EP.ID_TIPO_LOGRADOURO", "LOG.ID_TIPO_LOGRADOURO");
		sqlPrincipal.addJoin("EP.ID_MUNICIPIO", "MUN.ID_MUNICIPIO");
		
		sqlPrincipal.addOrderBy("DADOS.SG_FILIAL_DEVEDOR");
		sqlPrincipal.addOrderBy("DADOS.NM_DEVEDOR");
		sqlPrincipal.addOrderBy("DADOS.NR_IDENTIFICACAO");
		
		return sqlPrincipal;
	}

	private SqlTemplate mountSubSelectSqlPrincipal(TypedFlatMap tfm){
		
		SqlTemplate sql = createSqlTemplate();
		 
		sql.addProjection("DDSF.ID_CLIENTE", "ID_CLIENTE");
        sql.addProjection("FIL.SG_FILIAL", "SG_FILIAL_DEVEDOR");
        sql.addProjection("FIL.ID_FILIAL", "ID_FILIAL_DEVEDOR");
        sql.addProjection("PES_FIL.NM_FANTASIA", "NM_FILIAL_DEVEDOR");
        sql.addProjection("PES_CLI.NM_PESSOA", "NM_DEVEDOR");
        sql.addProjection("PES_CLI.NR_IDENTIFICACAO", "NR_IDENTIFICACAO");
        sql.addProjection("PES_CLI.TP_IDENTIFICACAO", "TP_IDENTIFICACAO");
        sql.addProjection("SUM(F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, SYSDATE, DDSF.VL_DEVIDO))", "VALOR_DOCUMENTO");
        sql.addProjection("COUNT(*)", "QT_DOC_CLIENTE");
        
        sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
        sql.addFrom("DOCTO_SERVICO", "DS");
        sql.addFrom("FILIAL", "FIL");
        sql.addFrom("PESSOA", "PES_FIL");
        sql.addFrom("PESSOA", "PES_CLI");
        
        // Caso abrangencia ou modal não seja null, faz o join para montar o filtro
        if( StringUtils.isNotBlank(tfm.getDomainValue("tpAbrangencia").getValue()) || StringUtils.isNotBlank(tfm.getDomainValue("tpModal").getValue()) ){
        	sql.addFrom("SERVICO", "S");
        	sql.addJoin("S.ID_SERVICO", "DS.ID_SERVICO");
        }
        
        sql.addFrom("CONHECIMENTO", "CON");
        sql.addJoin("DS.ID_DOCTO_SERVICO", "CON.ID_CONHECIMENTO (+)");

        // Caso tpCliente não seja null, faz o join para montar o filtro 
        if( StringUtils.isNotBlank(tfm.getDomainValue("tpCliente").getValue()) ){
        	sql.addFrom("CLIENTE", "CLI");
        	sql.addJoin("DDSF.ID_CLIENTE", "CLI.ID_CLIENTE"); 
        }
        
        // Caso tpFilial não seja null, faz o join para montar o filtro 
        if( StringUtils.isNotBlank(tfm.getDomainValue("tpFilial").getValue()) ){
        	sql.addFrom("HISTORICO_FILIAL", "HF");
        	sql.addJoin("FIL.ID_FILIAL", "HF.ID_FILIAL");
        }
        
        sql.addJoin("DDSF.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
        sql.addJoin("DDSF.ID_FILIAL", "FIL.ID_FILIAL");
        sql.addJoin("FIL.ID_FILIAL", "PES_FIL.ID_PESSOA");
        sql.addJoin("DDSF.ID_CLIENTE", "PES_CLI.ID_PESSOA");
 
        
        mountCriteriaSqlPrincipal(tfm, sql);
        sql.addGroupBy("DDSF.ID_CLIENTE");
        sql.addGroupBy("FIL.SG_FILIAL");
        sql.addGroupBy("FIL.ID_FILIAL");
        sql.addGroupBy("PES_FIL.NM_FANTASIA");
        sql.addGroupBy("PES_CLI.NM_PESSOA");
        sql.addGroupBy("PES_CLI.NR_IDENTIFICACAO");
        sql.addGroupBy("PES_CLI.TP_IDENTIFICACAO");
        
        return sql;
	}
	
	/**
	 * Monta os filtros do sql principal
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param tfm
	 * @param sql
	 *
	 */
	private void mountCriteriaSqlPrincipal(TypedFlatMap tfm, SqlTemplate sql) {
		
		// Os filtros da conversão de moeda devem ser os primeiros pq são filtros para a projection 
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(tfm.getLong("moeda.idMoeda"));
		
		sql.addCustomCriteria("(DS.TP_DOCUMENTO_SERVICO NOT IN ('CTR','NFT','CTE','NTE') OR (DS.TP_DOCUMENTO_SERVICO IN ('CTR','NFT','CTE','NTE') AND CON.TP_SITUACAO_CONHECIMENTO in ('E','B')))");
		sql.addCriteria("FIL.ID_FILIAL", "=", tfm.getLong("filial.idFilial"));
		
		// Não considerar pré conhecimentos
		sql.addCriteria("DS.NR_DOCTO_SERVICO", ">", 0);
		
		if( tfm.getDomainValue("tpFilial") != null && !tfm.getDomainValue("tpFilial").getValue().equals("") ){
			sql.addCriteria("HF.TP_FILIAL","=",tfm.getDomainValue("tpFilial").getValue());
			sql.addCustomCriteria("? BETWEEN HF.DT_REAL_OPERACAO_INICIAL AND HF.DT_REAL_OPERACAO_FINAL");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}
		
		if (tfm.getBoolean("blCortesia")) {
			sql.addCriteria("DS.TP_CALCULO_PRECO","=","G");
		}
		
		if(StringUtils.isNotBlank(tfm.getString("tpFrete"))){
	    	sql.addCustomCriteria("CON.TP_FRETE = ?");
	    	sql.addCustomCriteria("CON.TP_FRETE is not null");
			sql.addCriteriaValue(tfm.getString("tpFrete"));
    	}
		
		sql.addCriteria("DDSF.ID_CLIENTE","=",tfm.getLong("cliente.idCliente"));
		
		if( tfm.getString("identificacaoParcial") != null && !tfm.getString("identificacaoParcial").equals("") ){
			sql.addCriteria("PES_CLI.NR_IDENTIFICACAO","LIKE",tfm.getString("identificacaoParcial") + "%");
		} 
		
		sql.addCriteria("CLI.TP_CLIENTE","=",tfm.getDomainValue("tpCliente").getValue());
		
		String situacaoCobranca = "";
		
		if( tfm.getDomainValue("tpSituacaoCobranca") != null ){
			situacaoCobranca = tfm.getDomainValue("tpSituacaoCobranca").getValue();
		}
		
		if( tfm.getDomainValue("tpSituacaoCobranca") != null ){
			
			// Em Carteira
			if( "C".equalsIgnoreCase(situacaoCobranca) ){
				sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C"}); 
            // Pendente
			} else if( "P".equalsIgnoreCase(situacaoCobranca) ){
				sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C", "F"});
			//Em Fatura
			} else if( "F".equalsIgnoreCase(situacaoCobranca) ){
				sql.addCriteria("DDSF.TP_SITUACAO_COBRANCA","=","F");
			}
			
		} else {
			sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C", "F"});
		}
		
		YearMonthDay dtEmissao = tfm.getYearMonthDay("dtEmissaoAte"); 
		
		if( dtEmissao != null ){
			sql.addCriteria("DS.DH_EMISSAO","<=",JTDateTimeUtils.createWithMaxTime(dtEmissao));
		}
		
		sql.addCriteria("S.TP_MODAL","=", tfm.getDomainValue("tpModal").getValue());
		
		sql.addCriteria("S.TP_ABRANGENCIA","=", tfm.getDomainValue("tpAbrangencia").getValue());
		
		if( tfm.getLong("diasAtraso") != null ){
			sql.addCustomCriteria("(( to_date('"+JTFormatUtils.format(JTDateTimeUtils.getDataAtual())+"','dd/MM/yyyy') - (trunc(cast(ds.dh_emissao as date)))) >= ?)");
			sql.addCriteriaValue(tfm.getLong("diasAtraso"));
		}
		
		if (tfm.getDomainValue("tpDocumentoServico") != null && !"".equals(tfm.getDomainValue("tpDocumentoServico").getValue())) {
			String tpDocumentoServico = tfm.getDomainValue("tpDocumentoServico").getValue();
			
			sql.addCriteria("DS.TP_DOCUMENTO_SERVICO", "=", tpDocumentoServico);
			
			if ((ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(tpDocumentoServico)
					|| ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) 
					&& tfm.getDomainValue("tpSituacaoRPS") != null && !"".equals(tfm.getDomainValue("tpSituacaoRPS").getValue())) {
				String tpSituacaoRPS = tfm.getDomainValue("tpSituacaoRPS").getValue();
				
				if (ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_AUTORIZADO.equals(tpSituacaoRPS)) {
					sql.addCustomCriteria(" exists ( select 1 from MONITORAMENTO_DOC_ELETRONICO MDE where MDE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND MDE.TP_SITUACAO_DOCUMENTO = 'A' )");
				} else {
					sql.addCustomCriteria(" exists ( select 1 from MONITORAMENTO_DOC_ELETRONICO MDE where MDE.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO AND MDE.TP_SITUACAO_DOCUMENTO != 'A' )");
	}
			}
		}
	}
	
	public JRDataSource executeSubReport(Object[] param) throws Exception {
		return new JREmptyDataSource();
	}
	
	/**
	 * Executa o subreport de totais por filial
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param parameters
	 * @return
	 * @throws Exception
	 *
	 */
	@SuppressWarnings("rawtypes")
	public JRDataSource executeSubRelatorioTotaisFilial(Object[] parameters) throws Exception {
		
		Long idFilial = (Long) parameters[0];
		Long idMoedaDestino = (Long)parameters[1];		
		Map tfm 			= (Map)parameters[2];		
		Long idPaisDestino  = SessionUtils.getPaisSessao().getIdPais();
		
		SqlTemplate sql = this.mountSqlSubReport(idMoedaDestino, idPaisDestino, tfm);
		
		sql.addCriteria("DDSF.ID_FILIAL", "=", idFilial);
		
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());		
		
		return jr.getDataSource();
		
	}
	
	/**
	 * Executa o subreport de totais por empresa
	 *
	 * @author Hector Julian Esnaola Junior 
	 * @since 13/02/2007
	 *
	 * @param parameters
	 * @return
	 * @throws Exception
	 *
	 */
	@SuppressWarnings("rawtypes")
	public JRDataSource executeSubRelatorioTotaisEmpresa(Object[] parameters) throws Exception {
		
		Long idMoedaDestino = (Long)parameters[0];		
		Map tfm 			= (Map)parameters[1];
		Long idPaisDestino  = SessionUtils.getPaisSessao().getIdPais();
		
		SqlTemplate sql = this.mountSqlSubReport(idMoedaDestino, idPaisDestino, tfm);
		
		// Filtro de empresa
		sql.addCriteria("F.ID_EMPRESA", "=", SessionUtils.getEmpresaSessao().getIdEmpresa());
		
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());		
		
		return jr.getDataSource();
	}


	/**
	 * Monta o sql geral dos subReport
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @return
	 *
	 */
	@SuppressWarnings("rawtypes")
	private SqlTemplate mountSqlSubReport(Long idMoedaDestino, Long idPaisDestino, Map tfm){
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection(new StringBuilder()
					.append("SUM((CASE ")
					.append("		WHEN (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) <= 30 ")
					.append("			THEN 1 ")
					.append("			ELSE 0 ")
					.append("	 END)) AS NUMCONHECIMENTOS30, ")
					.append("SUM((CASE ")
					.append("		WHEN (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) <= 30 ")
					.append("			THEN F_CONV_MOEDA(VDS.ID_PAIS, VDS.ID_MOEDA, ?, ?, SYSDATE, DDSF.VL_DEVIDO) ")
					.append("			ELSE 0 ")
					.append("	 END)) AS VALOR30, ")
					.append("SUM((CASE ")
					.append("		WHEN (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) > 30 AND ")
					.append("			 (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) <= 60 ")
					.append("			THEN 1 ")
					.append("			ELSE 0 ")
					.append("	 END)) AS NUMCONHECIMENTOS60, ")
					.append("SUM((CASE ")
					.append("		WHEN (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) > 30 AND ")
					.append("			 (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) <= 60 ")
					.append("			THEN F_CONV_MOEDA(VDS.ID_PAIS, VDS.ID_MOEDA, ?, ?, SYSDATE, DDSF.VL_DEVIDO) ")
					.append("			ELSE 0 ")
					.append("	 END)) AS VALOR60, ")
					.append("SUM((CASE ")
					.append("		WHEN (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) > 60 ")
					.append("			THEN 1 ")
					.append("			ELSE 0 ")
					.append("	 END)) AS NUMCONHECIMENTOS90, ")
					.append("SUM((CASE ")
					.append("		WHEN (TRUNC(SYSDATE) - TRUNC(CAST(VDS.DH_EMISSAO AS date))) > 60 ")
					.append("			THEN F_CONV_MOEDA(VDS.ID_PAIS, VDS.ID_MOEDA, ?, ?, SYSDATE, DDSF.VL_DEVIDO) ")
					.append("			ELSE 0 ")
					.append("	 END)) AS VALOR90 ").toString());
					
					
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("FILIAL", "F");
		sql.addFrom("DOCTO_SERVICO", "VDS");
		sql.addFrom("CONHECIMENTO", "CON");
		sql.addJoin("VDS.ID_DOCTO_SERVICO", "DDSF.ID_DOCTO_SERVICO");
		sql.addJoin("DDSF.ID_FILIAL", "F.ID_FILIAL");
		sql.addJoin("VDS.ID_DOCTO_SERVICO", "CON.ID_CONHECIMENTO (+)");
		
		// Filtros da função de conversão de moeda
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		
		sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C", "F"});
		
		sql.addCriteria("VDS.NR_DOCTO_SERVICO", ">", 0);
		
		sql.addCustomCriteria("(VDS.TP_DOCUMENTO_SERVICO NOT IN ('CTR','NFT','CTE','NTE') OR (VDS.TP_DOCUMENTO_SERVICO IN ('CTR','NFT','CTE','NTE') AND CON.TP_SITUACAO_CONHECIMENTO in ('E','B')))");

		if(StringUtils.isNotBlank((String)tfm.get("tpFrete"))){
			sql.addCustomCriteria("CON.TP_FRETE = ?");
			sql.addCustomCriteria("CON.TP_FRETE is not null");
			sql.addCriteriaValue(tfm.get("tpFrete"));
		}
		
		if (Boolean.valueOf((String)tfm.get("blCortesia"))) {
			sql.addCriteria("VDS.TP_CALCULO_PRECO","=","G");
		}
		
		return sql;
	}

	/**
	 * SubReport de documentos
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 08/03/2007
	 *
	 * @param args
	 * @return
	 *
	 */
	public JRDataSource executeSubReportDocumentos(Object[] args){
		
		Long idCliente = (Long) args[0];
		Long idFilial = (Long) args[1];
		TypedFlatMap tfm = (TypedFlatMap) args[2];
		tfm.put("filial.idFilial", idFilial);
		SqlTemplate sql = mountSqlDocumentos(idCliente, tfm); 
		
		JRReportDataObject jr =  executeQuery(sql.getSql(), sql.getCriteria());
		
				
		return jr.getDataSource();
	}
	
	/**
	 * Monta o sql de documentos
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 08/03/2007
	 *
	 * @return
	 *
	 */
	private SqlTemplate mountSqlDocumentos(Long idCliente, TypedFlatMap tfm){
		
		SqlTemplate sql = createSqlTemplate();
		
		mountProjectionSqlDocumentos(sql);

		moutFromSqlDocumentos(tfm, sql); 
		
		mountCriteriaSqlDocumentos(idCliente, tfm, sql);
		
		mountOrderBySqlDocumentos(sql);
		
		return sql;
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 12/03/2007
	 *
	 * @param sql
	 *
	 */
	private void mountOrderBySqlDocumentos(SqlTemplate sql) {
		sql.addOrderBy("DS_DIVISAO_CLIENTE");
		sql.addOrderBy("VL_TP_DOCUMENTO_SERVICO");
		sql.addOrderBy("SG_F_ORIGEM_DS");
		sql.addOrderBy("NR_DOCTO_SERVICO");
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 09/03/2007
	 *
	 * @param tfm
	 * @param sql
	 *
	 */
	private void moutFromSqlDocumentos(TypedFlatMap tfm, SqlTemplate sql) {
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("FILIAL", "FILIAL_DEVEDOR");
		sql.addFrom("DOCTO_SERVICO", "VDS");

		sql.addFrom("FATURA", "FAT");
		sql.addFrom("REDECO", "RED");
		sql.addFrom("BOLETO", "BOL");
		sql.addFrom("FILIAL", "FILFAT");
		sql.addFrom("FILIAL", "F_ORIGEM_DS");
		sql.addFrom("FILIAL", "F_DESTINO_DS");
		sql.addFrom("DIVISAO_CLIENTE", "DIV_CLI");
		sql.addFrom("DOMINIO", "D");
		sql.addFrom("VALOR_DOMINIO", "VD");
		
		sql.addJoin("DDSF.ID_DOCTO_SERVICO", "VDS.ID_DOCTO_SERVICO");
		sql.addJoin("DDSF.ID_FILIAL", "FILIAL_DEVEDOR.ID_FILIAL");
		sql.addJoin("DDSF.ID_FATURA", "FAT.ID_FATURA(+)");
		sql.addJoin("FAT.ID_REDECO", "RED.ID_REDECO(+)");
		sql.addJoin("FAT.ID_BOLETO", "BOL.ID_BOLETO(+)");
		sql.addJoin("FAT.ID_FILIAL", "FILFAT.ID_FILIAL(+)");
		sql.addJoin("F_ORIGEM_DS.ID_FILIAL", "VDS.ID_FILIAL_ORIGEM");
		sql.addJoin("VDS.ID_FILIAL_DESTINO", "F_DESTINO_DS.ID_FILIAL(+)");
		sql.addJoin("DDSF.ID_DIVISAO_CLIENTE", "DIV_CLI.ID_DIVISAO_CLIENTE(+)");
		sql.addJoin("VD.ID_DOMINIO", "D.ID_DOMINIO");
		sql.addFrom("CONHECIMENTO", "CON");
		sql.addJoin("VDS.ID_DOCTO_SERVICO", "CON.ID_CONHECIMENTO (+)");
		
		if( tfm.getDomainValue("tpFilial") != null && !tfm.getDomainValue("tpFilial").getValue().equals("") ){
			sql.addFrom("HISTORICO_FILIAL", "HF");
			sql.addJoin("FILIAL_DEVEDOR.ID_FILIAL", "HF.ID_FILIAL");
		}
		
		if( tfm.getDomainValue("tpModal") != null || tfm.getDomainValue("tpAbrangencia") != null ){
			sql.addFrom("SERVICO", "S");
			sql.addJoin("VDS.ID_SERVICO", "S.ID_SERVICO(+)");
		}
		
		sql.addCustomCriteria("(VDS.TP_DOCUMENTO_SERVICO NOT IN ('CTR','NFT','CTE','NTE') OR (VDS.TP_DOCUMENTO_SERVICO IN ('CTR','NFT','CTE','NTE') AND CON.TP_SITUACAO_CONHECIMENTO in ('E','B')))");
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 09/03/2007
	 *
	 * @param sql
	 *
	 */
	private void mountProjectionSqlDocumentos(SqlTemplate sql) {
		
		sql.addProjection(
				new StringBuilder() 
						
						// CONVERSÃO DA MOEDA	  
						.append("F_CONV_MOEDA(VDS.ID_PAIS, VDS.ID_MOEDA, ?, ?, SYSDATE, DDSF.VL_DEVIDO) AS VALOR_DOCUMENTO, ")
						
						// EC
						.append(" (CASE ")
						.append("    WHEN DDSF.TP_SITUACAO_COBRANCA = 'P' OR ")
						.append("    	DDSF.TP_SITUACAO_COBRANCA = 'C' ")
						.append("     THEN  ")
						.append("      (CASE")
						.append("         WHEN (SELECT 1 \n")
						.append("         	FROM TRANSFERENCIA T, ITEM_TRANSFERENCIA IT \n")
						.append("         	WHERE IT.ID_TRANSFERENCIA = T.ID_TRANSFERENCIA \n")
						.append("         	AND T.TP_SITUACAO_TRANSFERENCIA = 'PR' \n")
						.append("         	AND DDSF.ID_DEVEDOR_DOC_SERV_FAT = IT.ID_DEVEDOR_DOC_SERV_FAT) \n")
						.append("         IS NOT NULL \n")
						.append("         THEN 'T' \n")
						.append("         ELSE 'C' \n")
						.append("       END)")
						.append("    WHEN DDSF.TP_SITUACAO_COBRANCA = 'F' ")
						.append("     THEN ")
						.append("		(CASE ")
						.append("		   WHEN FAT.TP_SITUACAO_FATURA = 'DI' OR ")
						.append(" 			    FAT.TP_SITUACAO_FATURA = 'RC' OR ")
						.append("  			    FAT.TP_SITUACAO_FATURA = 'EM' ")
						.append(" 		  	THEN 'F' ")
						.append("		   WHEN FAT.TP_SITUACAO_FATURA = 'BL' ")
						.append(" 		  	THEN ")
						.append("     		  (CASE")
						.append(" 		          WHEN BOL.TP_SITUACAO_BOLETO = 'DB' OR")
						.append("     		           BOL.TP_SITUACAO_BOLETO = 'DI' OR")
						.append("           	       BOL.TP_SITUACAO_BOLETO = 'GM' OR")
						.append("            	       BOL.TP_SITUACAO_BOLETO = 'EM' OR")
						.append("            	       BOL.TP_SITUACAO_BOLETO = 'GE'")
						.append("        	       THEN 'B'")
						.append("     	          WHEN BOL.TP_SITUACAO_BOLETO = 'BN'")
						.append("           	   THEN 'BB'")
						.append("       	      WHEN BOL.TP_SITUACAO_BOLETO = 'BP'")
						.append("            	   THEN 'BC'")
						.append("      		  END)")
						.append("  		   WHEN FAT.TP_SITUACAO_FATURA = 'RE' ")
						.append("       	THEN 'FR'")
						.append("		END) ")
						.append(" END) AS EC, ")
						
						// VENCIMENTO
			 			.append(" (CASE ")
						.append("    WHEN DDSF.TP_SITUACAO_COBRANCA = 'P' OR ")
						.append("    	DDSF.TP_SITUACAO_COBRANCA = 'C' ")
						.append("     THEN  ")
						.append("      NULL")
						.append("    WHEN DDSF.TP_SITUACAO_COBRANCA = 'F' ")
						.append("     THEN ")
						.append("		FAT.DT_VENCIMENTO ")
						.append(" END) AS VENCIMENTO, ")
						
						// DOCUMENTO
			 			.append(" (CASE ")
						.append("    WHEN DDSF.TP_SITUACAO_COBRANCA = 'P' OR ")
						.append("    	DDSF.TP_SITUACAO_COBRANCA = 'C' ")
						.append("     THEN  ")
						.append("         (SELECT FIL_TRA.SG_FILIAL || ' ' || TO_CHAR(T.NR_TRANSFERENCIA ,'0000000000') \n")
						.append("         	FROM TRANSFERENCIA T, ITEM_TRANSFERENCIA IT, FILIAL FIL_TRA \n")
						.append("         	WHERE IT.ID_TRANSFERENCIA = T.ID_TRANSFERENCIA \n")
						.append("         	AND	T.ID_FILIAL_ORIGEM = FIL_TRA.ID_FILIAL \n")
						.append("         	AND T.TP_SITUACAO_TRANSFERENCIA = 'PR' \n")
						.append("         	AND DDSF.ID_DEVEDOR_DOC_SERV_FAT = IT.ID_DEVEDOR_DOC_SERV_FAT) \n")
						.append("    WHEN DDSF.TP_SITUACAO_COBRANCA = 'F' ")
						.append("     THEN ")
						.append("		(CASE ")
						.append("		   WHEN FAT.TP_SITUACAO_FATURA = 'DI' OR ")
						.append(" 			    FAT.TP_SITUACAO_FATURA = 'RC' OR ")
						.append("  			    FAT.TP_SITUACAO_FATURA = 'EM' ")
						.append(" 		  	THEN FILFAT.SG_FILIAL || ' ' || TO_CHAR(FAT.NR_FATURA ,'0000000000') ")
						.append("		   WHEN FAT.TP_SITUACAO_FATURA = 'BL' ")
						.append(" 		  	THEN ")
						.append("     		  FILFAT.SG_FILIAL || ' ' || TO_CHAR(FAT.NR_FATURA ,'0000000000') ")
						.append("  		   WHEN FAT.TP_SITUACAO_FATURA = 'RE' ")
						.append("    		THEN")
						.append("      		  FILFAT.SG_FILIAL || ' ' || TO_CHAR(RED.NR_REDECO ,'0000000000')")
						.append("		END) ")
						.append(" END) AS DOC_FATURA, ")
									
						.append(PropertyVarcharI18nProjection.createProjection("VD.DS_VALOR_DOMINIO_I", "VL_TP_DOCUMENTO_SERVICO, "))
						.append("F_ORIGEM_DS.SG_FILIAL AS SG_F_ORIGEM_DS, ")
						.append("VDS.NR_DOCTO_SERVICO AS NR_DOCTO_SERVICO, ")
						.append("VDS.TP_DOCUMENTO_SERVICO AS TP_DOCUMENTO_SERVICO, ")
						.append("F_DESTINO_DS.SG_FILIAL AS SG_FILIAL_DESTINO, ")
						.append("trunc(cast(VDS.DH_EMISSAO as Date)) AS EMISSAO,")
						
						.append(" (SELECT vi18n(DS_VALOR_DOMINIO_I)")
						.append(" FROM conhecimento")
						.append(" INNER JOIN dominio ON dominio.nm_dominio = 'DM_TIPO_FRETE'")
						.append(" INNER JOIN valor_dominio ON valor_dominio.id_dominio = dominio.id_dominio")
						.append(" WHERE conhecimento.id_conhecimento = vds.id_docto_servico")
						.append(" AND valor_dominio.VL_VALOR_DOMINIO = conhecimento.tp_frete")
						
						.append(" ) as TP_FRETE,")
						
						.append("DIV_CLI.DS_DIVISAO_CLIENTE AS DS_DIVISAO_CLIENTE, ")
						.append("(trunc(SYSDATE) - trunc(cast(VDS.DH_EMISSAO as Date))) AS ATRASO").toString());
		
	}
	
	/**
	 * Monta os filtros do sql de documentos
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 13/02/2007
	 *
	 * @param tfm
	 * @param sql
	 *
	 */
	private void mountCriteriaSqlDocumentos(Long idCliente, TypedFlatMap tfm, SqlTemplate sql) {
		
		// Os filtros da conversão de moeda devem ser os primeiros pq são filtros para a projection 
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(tfm.getLong("moeda.idMoeda"));
		  
		// Filtro do dominio
		sql.addCriteria("D.NM_DOMINIO", "=", "DM_TIPO_DOCUMENTO_SERVICO");
		sql.addCustomCriteria("VD.VL_VALOR_DOMINIO = VDS.TP_DOCUMENTO_SERVICO");
		
		sql.addCriteria("FILIAL_DEVEDOR.ID_FILIAL", "=", tfm.getLong("filial.idFilial"));
		
		sql.addCriteria("VDS.NR_DOCTO_SERVICO",">",0);
		
		if( tfm.getDomainValue("tpFilial") != null && !tfm.getDomainValue("tpFilial").getValue().equals("") ){
			sql.addCriteria("HF.TP_FILIAL","=",tfm.getDomainValue("tpFilial").getValue());
			sql.addCustomCriteria("? BETWEEN HF.DT_REAL_OPERACAO_INICIAL AND HF.DT_REAL_OPERACAO_FINAL");
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		}
		
		sql.addCriteria("DDSF.ID_CLIENTE","=", idCliente);
		
		if( tfm.getDomainValue("tpSituacaoCobranca") != null ){
			String situacaoCobranca = tfm.getDomainValue("tpSituacaoCobranca").getValue();
			
			// Em Carteira
			if( "C".equalsIgnoreCase(situacaoCobranca) ){
				sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C"}); 
            // Pendente
			} else if( "P".equalsIgnoreCase(situacaoCobranca) ){
				sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C", "F"});
			//Em Fatura
			} else if( "F".equalsIgnoreCase(situacaoCobranca) ){
				sql.addCriteria("DDSF.TP_SITUACAO_COBRANCA","=","F");
			}
		} else {
			sql.addCriteriaIn("DDSF.TP_SITUACAO_COBRANCA", new Object[]{"P", "C", "F"});
		}
		
		YearMonthDay dtEmissao = tfm.getYearMonthDay("dtEmissaoAte"); 
		
		if( dtEmissao != null ){
			sql.addCriteria("VDS.DH_EMISSAO","<=",JTDateTimeUtils.createWithMaxTime(dtEmissao));
		}
		
		if( tfm.getDomainValue("tpModal") != null && !tfm.getDomainValue("tpModal").getValue().trim().equals("") ){
			sql.addCustomCriteria("(S.TP_MODAL IS NULL OR S.TP_MODAL = '" + tfm.getDomainValue("tpModal").getValue() + "') ");
		}
		
		if( tfm.getDomainValue("tpAbrangencia") != null && !tfm.getDomainValue("tpAbrangencia").getValue().trim().equals("")){
			sql.addCustomCriteria("(S.TP_ABRANGENCIA IS NULL OR S.TP_ABRANGENCIA = '" + tfm.getDomainValue("tpAbrangencia").getValue() + "') ");
		}
		
		if( tfm.getLong("diasAtraso") != null ){
			sql.addCustomCriteria("(( to_date('"+JTFormatUtils.format(JTDateTimeUtils.getDataAtual())+"','dd/MM/yyyy') - (trunc(cast(vds.dh_emissao as date)))) >= ?)");
			sql.addCriteriaValue(tfm.getLong("diasAtraso"));
		}
		
		if (tfm.getBoolean("blCortesia")) {
			sql.addCriteria("VDS.TP_CALCULO_PRECO","=","G");
		}
		
		if(StringUtils.isNotBlank(tfm.getString("tpFrete"))){
			sql.addCustomCriteria("CON.TP_FRETE = ?");
			sql.addCustomCriteria("CON.TP_FRETE is not null");
			sql.addCriteriaValue(tfm.getString("tpFrete"));
	}
		
		if (tfm.getDomainValue("tpDocumentoServico") != null && !"".equals(tfm.getDomainValue("tpDocumentoServico").getValue())) {
			String tpDocumentoServico = tfm.getDomainValue("tpDocumentoServico").getValue();
			
			sql.addCriteria("VDS.TP_DOCUMENTO_SERVICO", "=", tpDocumentoServico);
			
			if ((ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equals(tpDocumentoServico)
					|| ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico))
					&& tfm.getDomainValue("tpSituacaoRPS") != null && !"".equals(tfm.getDomainValue("tpSituacaoRPS").getValue())) {
				String tpSituacaoRPS = tfm.getDomainValue("tpSituacaoRPS").getValue();
				
				if (ConstantesExpedicao.TP_SITUACAO_DOC_ELETRONICO_AUTORIZADO.equals(tpSituacaoRPS)) {
					sql.addCustomCriteria(" exists ( select 1 from MONITORAMENTO_DOC_ELETRONICO MDE where MDE.ID_DOCTO_SERVICO = VDS.ID_DOCTO_SERVICO AND MDE.TP_SITUACAO_DOCUMENTO = 'A' )");
				} else {
					sql.addCustomCriteria(" exists ( select 1 from MONITORAMENTO_DOC_ELETRONICO MDE where MDE.ID_DOCTO_SERVICO = VDS.ID_DOCTO_SERVICO AND MDE.TP_SITUACAO_DOCUMENTO != 'A' )");
}
}
		}
	}
}
;