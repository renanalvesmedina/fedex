package com.mercurio.lms.contasreceber.report;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRAdsmDataSource;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * @author Hector junior
 * @since 29/06/2005
 *
 * @spring.bean id="lms.contasreceber.emitirExtratoClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirExtratoCliente.jasper"
 */
public class EmitirExtratoClienteService extends ReportServiceSupport {
	
	private PessoaService pessoaService;
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}	
	
	private ClienteService clienteService;
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
		
	/** ########################################   REPORT PRINCIPAL  ######################################### */
	
	
	



	/** 
	 * Método invocado pela EmitirExtratoClienteAction
	 */
	public JRReportDataObject execute(Map parameters) {
		
		/** Faz um Cast do Map contendo os parâmetros do request */
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		List retorno = new ArrayList();
		SqlTemplate sql = createSqlTemplate();;
		 
		Long idMoedaDestino = tfm.getLong("moeda.idMoeda");
		Long idPaisDestino = ((Pais)SessionContext.get(SessionKey.PAIS_KEY)).getIdPais();
		
		// Montar a lista de cliente baseado nos filtros da tela
		Set<Long> idsClientes = new HashSet<Long>();
		
		mountIdsClienteIdentificacao(parameters, idsClientes);
		mountIdsClienteByGrupoEconomico(parameters, idsClientes);
		mountIdsClienteByIdsClienteTela(parameters, idsClientes);	
		
		mountReportComplete(tfm, retorno, sql, idMoedaDestino, idPaisDestino, mountIdsClientes(getSqlOrderCliente(idsClientes), idsClientes));
		
		/** Gambiarra para não imprimir os totais quando não retornar documentos */
		Map map = new HashMap();
		
		// Caso retorne documentos de servico e não seja sóTotais, imprime os subReports
		if( !retorno.isEmpty() && !(tfm.getBoolean("soTotais") != null && tfm.getBoolean("soTotais").equals(Boolean.TRUE)) ){
			map.put("DUMMY", Boolean.TRUE);
			retorno.add(map);
		
		// Caso retorne documentos de servico e seja sóTotais, imprime os subReports, mas não imprime o corpo do relatório
		}else if( !retorno.isEmpty() && tfm.getBoolean("soTotais") != null && tfm.getBoolean("soTotais").equals(Boolean.TRUE) ){
			retorno = new ArrayList();
			map.put("DUMMY", Boolean.TRUE);
			retorno.add(map);
		}
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(retorno);
		
		JRReportDataObject jr = createReportDataObject(jrMap, parameters);

		/** Map que armazena os parâmetros do relatório */
		Map parametersReport = new HashMap();

		/** Adiciona os parâmetros de pesquisa no Map */
		parametersReport.put("parametrosPesquisa", addFilterSummary(sql, tfm).getFilterSummary());
		
		/** Adiciona os parameters no report */
		addParametersReport(tfm, idsClientes, idMoedaDestino, idPaisDestino, tfm.getString("tpClienteFiltroComp"), parametersReport);
		
		jr.setParameters(parametersReport);
		
		return jr;
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 *
	 * @param tfm
	 * @param retorno
	 * @param sql
	 * @param idMoedaDestino
	 * @param idPaisDestino
	 * @param idsClientes
	 * @param idsDocumentos
	 * @return
	 *
	 */
	private void mountReportComplete(TypedFlatMap tfm, List retorno, SqlTemplate sql, Long idMoedaDestino, Long idPaisDestino, List<Long> idsClientes) {
		final List lst = new ArrayList();
		
		// Itera os clientes para buscar os documentos do mesmo
		for ( Long idCliente : idsClientes ){ 
			
			/** Instância a classe SqlTemplate, que retorna o sql para geração do relatório */
			sql = getSqlTemplateReportExtrato(tfm, idCliente);
			
			/** Lista gerado após a iteração do ResultSet da consulta do relatório */
			List retornoCliente = iteratorResultSetReportExtrato(sql, idCliente, lst);
			
			/** Veririfica se a lista não é nula, para adicionar os idsDoctoServico no parametersReport */
			if(retornoCliente != null && !retornoCliente.isEmpty()){
				// Copia os dados da collection com os dados do cliente para a collection armazena os dados de todos os clientes
				retorno.addAll(retornoCliente);
				
			}
			
		}

		BatchPreparedStatementSetter bpss = new BatchPreparedStatementSetter(){
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, (Long)lst.get(i));
			}

			public int getBatchSize() {				
				return lst.size();
			}
		};
		
		getJdbcTemplate().batchUpdate("INSERT INTO TMP_DS_EXTRATO_CLIENTE VALUES (?)", bpss);
	}
	
	@Override
	public void postExecute(Map parametros) {
		getJdbcTemplate().execute("DELETE FROM TMP_DS_EXTRATO_CLIENTE");
	}
	
	/**
	 * @author Mickaël Jalbert
	 * @since 24/10/2006
	 * @param parameters
	 * @param idsClientes
	 */
	private void mountIdsClienteByIdsClienteTela(Map parameters, Set idsClientes) {
		if (parameters.get("clientes") != null){
			List<Map> lstClientes = (ArrayList)parameters.get("clientes");
	
			for (Map map : lstClientes){
				idsClientes.add(Long.valueOf((String)map.get("cliente.idCliente")));
			}
		}
	}	

	/**
	 * @author Mickaël Jalbert
	 * @since 24/10/2006
	 * @param parameters
	 * @param idsClientes
	 */
	private void mountIdsClienteByGrupoEconomico(Map parameters, Set idsClientes) {
		if (StringUtils.isNotBlank((String)parameters.get("grupoEconomico.idGrupoEconomico"))) {
			List<Long> lstIdCliente = clienteService.findIdClienteByGrupoEconomico(Long.valueOf((String)parameters.get("grupoEconomico.idGrupoEconomico")));
			
			for (Long idCliente : lstIdCliente){
				idsClientes.add(idCliente);
			}
		}
	}

	/**
	 * @author Mickaël Jalbert
	 * @since 24/10/2006
	 * @param parameters
	 * @param idsClientes
	 */
	private void mountIdsClienteIdentificacao(Map parameters, Set idsClientes) {
		if (StringUtils.isNotBlank((String)parameters.get("identificacaoParcial"))) {
			List<Long> lstIdCliente = pessoaService.findIdPessoaByNrCNPJParcial((String)parameters.get("identificacaoParcial"));
			
			for (Long idCliente : lstIdCliente){
				idsClientes.add(idCliente);
			}
		}
	}
	
	
	/**
	 * Método responsável por adicionar os parameters no report
	 * 
	 * @author HectorJ
	 * @since 18/06/2006
	 *  
	 * @param tfm
	 * @param idsClientes
	 * @param idsDoctoServico
	 * @param idMoedaDestino
	 * @param idPaisDestino
	 * @param parametersReport
	 */
	private void addParametersReport(TypedFlatMap tfm
								   , Set idsClientes
								   , Long idMoedaDestino
								   , Long idPaisDestino
								   , String tpClienteFiltroComp
								   , Map parametersReport){
		
		/** Adiciona os idsClientes no Map */
		parametersReport.put("idsClientes", idsClientes);
		
		/** Adiciona a dtInicial e dtFinal Map */
		parametersReport.put("dtInicial", tfm.getYearMonthDay("dtInicial"));
		parametersReport.put("dtFinal", tfm.getYearMonthDay("dtFinal"));
		
		/** Adiciona os Booleans de totais no Map */
		parametersReport.put("soTotais", tfm.getBoolean("soTotais"));
		parametersReport.put("totaisFilial", tfm.getBoolean("totalPorFilial"));
		
		/** Adiciona a moeda e o país da moeda no Map */
		parametersReport.put("idMoedaDestino", idMoedaDestino);
		parametersReport.put("idPaisDestino", idPaisDestino);
		
		/** Adiciona o tipo de cliente no Map */
		parametersReport.put("tpClienteFiltroComp", tpClienteFiltroComp);
		
		/** Adiciona o usuário no Map */
		parametersReport.put("usuarioEmissor", SessionContext.getUser().getNmUsuario());
		
		/** Adiciona o tipo de relatório no Map */
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tfm.get("tpFormatoRelatorio"));
		
	}
	
	/**
	 * Método responsável por iterar o resultSet do Report
	 * 
	 * @author HectorJ
	 * @since 16/06/2006
	 * 
	 * @param SqlTemplate sql
	 * @return List lst
	 */
	private List iteratorResultSetReportExtrato(SqlTemplate sql, final Long idCliente, final List lstDoctoServico){
		List retorno = (List)getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new ResultSetExtractor() {

			List lst = new ArrayList();
			String nmPessoa;
			Map map;
			
			public Object extractData(ResultSet rs) throws SQLException {
				
				int count = 0;
				while(rs.next()){
					lstDoctoServico.add(rs.getLong("ID_DOCTO_SERVICO"));
					
					map = new HashMap();
					
					map.put("DUMMY", Boolean.FALSE);
					map.put("EC", rs.getString("EC"));
					map.put("DOCTO_COBRANCA", rs.getString("DOCTO_COBRANCA"));
					map.put("VENCIMENTO", rs.getDate("VENCIMENTO") != null ? JTFormatUtils.format(JTFormatUtils.buildYmd(rs.getDate("VENCIMENTO")), JTFormatUtils.SHORT) : null);
					map.put("FILIAL_COBRANCA_DEVEDOR", rs.getString("FILIAL_COBRANCA_DEVEDOR"));
					map.put("FILIAL_DS_ORIGEM", rs.getString("FILIAL_DS_ORIGEM"));
					map.put("NR_DOCTO_SERVICO", rs.getString("TIPO_DS_DOCTO_SERVICO") + " " 
											+ rs.getString("FILIAL_DS_ORIGEM") + " "
											+ FormatUtils.formataNrDocumento(rs.getString("NR_DOCTO_SERVICO")
																		   , rs.getString("TIPO_DOCTO_SERVICO")));
					map.put("FILIAL_DS_DESTINO", rs.getString("FILIAL_DS_DESTINO"));
					
					if(rs.getDate("EMISSAO") != null){
						map.put("EMISSAO", JTFormatUtils.format(JTFormatUtils.buildYmd(rs.getDate("EMISSAO")), JTFormatUtils.SHORT));
						
						if (rs.getString("TP_SITUACAO_COBRANCA") != null && !rs.getString("TP_SITUACAO_COBRANCA").equals("L")) { 						
							map.put("ATRASO", Integer.valueOf(JTDateTimeUtils.getIntervalInDays(JTFormatUtils.buildYmd(rs.getDate("EMISSAO")), JTDateTimeUtils.getDataAtual())));
						} else {
							map.put("ATRASO", null);
						}
					}else{
						map.put("EMISSAO", null);
						map.put("ATRASO", null);
					}
					
					map.put("NOTA_FISCAL", Long.valueOf(rs.getLong("NOTA_FISCAL")));
					map.put("VL_FRETE", rs.getBigDecimal("VL_FRETE"));	
					map.put("VL_MERCADORIA", rs.getBigDecimal("VL_MERCADORIA"));
					map.put("PESO", rs.getDouble("PESO"));
					map.put("P_DENSIDADE", rs.getDouble("P_DENSIDADE"));
					map.put("DPE", rs.getDate("DPE") != null ? JTFormatUtils.format(JTFormatUtils.buildYmd(rs.getDate("DPE")), JTFormatUtils.SHORT) : null);
					map.put("TP_SITUACAO_COBRANCA", rs.getString("TP_SITUACAO_COBRANCA"));
					map.put("ENTREGA", rs.getDate("ENTREGA") != null ? JTFormatUtils.format(JTFormatUtils.buildYmd(rs.getDate("ENTREGA")), JTFormatUtils.SHORT) : null);
					map.put("BAIXA", rs.getDate("BAIXA") != null ? JTFormatUtils.format(JTFormatUtils.buildYmd(rs.getDate("BAIXA")), JTFormatUtils.SHORT) : null);
	
					// Caso esteja no primeiro documento de serviço
					if( count == 0 ){
						
						Map dadosCliente = mountCliente(getSqlDadosCliente(idCliente));
						
						map.put("ID_CLIENTE", dadosCliente.get("ID_CLIENTE"));
						map.put("NR_IDENTIFICACAO", dadosCliente.get("NR_IDENTIFICACAO"));
						map.put("TP_IDENTIFICACAO", dadosCliente.get("TP_IDENTIFICACAO"));
						
						nmPessoa = (String)dadosCliente.get("NM_PESSOA");
						map.put("NM_PESSOA", nmPessoa);
						
						map.put("INSCRICAO_ESTADUAL", dadosCliente.get("INSCRICAO_ESTADUAL"));
						map.put("FILIAL_COBRANCA", dadosCliente.get("FILIAL_COBRANCA"));
						map.put("ENDERECO", dadosCliente.get("ENDERECO"));
						map.put("MUNICIPIO", dadosCliente.get("MUNICIPIO"));
						map.put("CEP", FormatUtils.formatCep("BRA", (String)dadosCliente.get("CEP")));
						map.put("TELEFONE", dadosCliente.get("TELEFONE"));
						
					}else{
						
						// Seta o id do cliente para não quebrar por cliente no relatório
						map.put("ID_CLIENTE", idCliente);
						map.put("NR_IDENTIFICACAO", null);
						map.put("TP_IDENTIFICACAO", null);
						map.put("NM_PESSOA", null);
						map.put("INSCRICAO_ESTADUAL", null);
						map.put("FILIAL_COBRANCA", null);
						map.put("ENDERECO", null);
						map.put("MUNICIPIO", null);
						map.put("CEP", null);
						map.put("TELEFONE", null);
						
					}
					
					/** Adiciona o Map na List */
					lst.add(map);
					
					count++;
				}
				
				return lst;
				
			}
		});
		
		return retorno;
	}
		
	/**
	 * Monta o sql principal
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 29/06/2006
	 *
	 * @param TypedFlatMap tfm
	 * @return SqlTemplate
	 */
	private SqlTemplate getSqlTemplateReportExtrato(TypedFlatMap tfm, Long idCliente){
		
		SqlTemplate sql = this.createSqlTemplate();
		
		/** PROJECTION */
		StringBuffer sb = new StringBuffer();
		
		sb.append("	CASE	WHEN (ddsf.tp_situacao_cobranca = 'P' OR ddsf.tp_situacao_cobranca = 'C') THEN \n");
		sb.append("			(SELECT	filori.sg_filial || ' ' || t.nr_transferencia\n");
		sb.append("			FROM	transferencia t,\n");
		sb.append("				item_transferencia it,\n");
		sb.append("				filial filori\n");
		sb.append("			WHERE	t.id_transferencia = it.id_transferencia\n");
		sb.append("			AND	t.id_filial_origem = filori.id_filial\n");
		sb.append("			AND	it.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat\n");
		sb.append("			AND	t.tp_situacao_transferencia = 'PR')\n");
		sb.append("		WHEN (ddsf.tp_situacao_cobranca = 'F') THEN \n");
		sb.append("			(SELECT	FIL.SG_FILIAL || TO_CHAR(NVL(R.NR_REDECO, F.NR_FATURA), '0000000000')\n");
		sb.append("			FROM	FATURA F,\n");
		sb.append("				FILIAL FIL,\n");
		sb.append("				REDECO R\n");
		sb.append("			WHERE F.ID_FILIAL = FIL.ID_FILIAL\n");
		sb.append("			AND	F.ID_FATURA = DDSF.ID_FATURA\n");
		sb.append("			AND	F.ID_REDECO = R.ID_REDECO(+))\n");
		sb.append("		WHEN (ddsf.tp_situacao_cobranca = 'L') THEN\n");
		sb.append("			(SELECT	FIL.SG_FILIAL || TO_CHAR(r.nr_relacao_cobranca_filial, '0000000000')\n");
		sb.append("			FROM	FATURA F,\n");
		sb.append("				FILIAL FIL,\n");
		sb.append("				relacao_cobranca R\n");
		sb.append("			WHERE F.ID_FILIAL = FIL.ID_FILIAL\n");
		sb.append("			AND	F.ID_FATURA = DDSF.ID_FATURA\n");
		sb.append("			AND	f.id_relacao_cobranca = r.id_relacao_cobranca(+))\n");
		sb.append("	END AS DOCTO_COBRANCA,\n");
		sb.append("	CASE	WHEN (ddsf.tp_situacao_cobranca = 'F' OR ddsf.tp_situacao_cobranca = 'L') THEN \n");
		sb.append("			(SELECT	NVL(B.DT_VENCIMENTO, F.DT_VENCIMENTO)\n");
		sb.append("			FROM	FATURA F,\n");
		sb.append("				BOLETO B\n");
		sb.append("			WHERE F.ID_FATURA = DDSF.ID_FATURA \n");
		sb.append("			AND	F.ID_BOLETO = B.ID_BOLETO(+))\n");
		sb.append("	END AS VENCIMENTO,\n");
		sb.append("	CASE	WHEN (ddsf.tp_situacao_cobranca = 'P' OR ddsf.tp_situacao_cobranca = 'C') THEN \n");
		sb.append("			NVL((SELECT	'T'\n");
		sb.append("			FROM	transferencia t,\n");
		sb.append("				item_transferencia it,\n");
		sb.append("				filial filori\n");
		sb.append("			WHERE	t.id_transferencia = it.id_transferencia\n");
		sb.append("			AND	t.id_filial_origem = filori.id_filial\n");
		sb.append("			AND	it.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat\n");
		sb.append("			AND	t.tp_situacao_transferencia = 'PR'), 'C')\n");
		sb.append("		WHEN (ddsf.tp_situacao_cobranca = 'F') THEN \n");
		sb.append("			(SELECT	CASE	WHEN (B.TP_SITUACAO_BOLETO IN ('DB', 'DI', 'GM', 'EM', 'GE')) THEN\n");
		sb.append("						'B'\n");
		sb.append("					WHEN (B.TP_SITUACAO_BOLETO = 'BN') THEN\n");
		sb.append("						'BB'\n");
		sb.append("					WHEN (B.TP_SITUACAO_BOLETO = 'BP') THEN\n");
		sb.append("						'BC'\n");
		sb.append("					WHEN (B.TP_SITUACAO_BOLETO = 'BP') THEN\n");
		sb.append("						'BC'\n");
		sb.append("					WHEN (R.ID_REDECO IS NOT NULL) THEN\n");
		sb.append("						'FR'\n");
		sb.append("					ELSE\n");
		sb.append("						'F'\n");
		sb.append("				END\n");
		sb.append("			FROM	FATURA F,\n");
		sb.append("				FILIAL FIL,\n");
		sb.append("				REDECO R,\n");
		sb.append("				BOLETO B\n");
		sb.append("			WHERE	F.ID_FILIAL = FIL.ID_FILIAL\n");
		sb.append("			AND	F.ID_FATURA = DDSF.ID_FATURA\n");
		sb.append("			AND	F.ID_REDECO = R.ID_REDECO(+)\n");
		sb.append("			AND	F.ID_BOLETO = B.ID_BOLETO(+))\n");
		sb.append("		WHEN (ddsf.tp_situacao_cobranca = 'L') THEN\n");
		sb.append("			ddsf.tp_situacao_cobranca\n");
		sb.append("	END AS EC\n");;
		
		sql.addProjection(sb.toString());
		
		sql.addProjection("ds.id_moeda", "ID_MOEDA_ORIGEM");
		sql.addProjection("ds.id_pais", "ID_PAIS_ORIGEM");
		sql.addProjection("ds.id_docto_servico", "ID_DOCTO_SERVICO");
		sql.addProjection("ds.tp_documento_servico", "TIPO_DOCTO_SERVICO");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("vdom.ds_valor_dominio_i", "TIPO_DS_DOCTO_SERVICO"));
		sql.addProjection("fildsorigem.sg_filial", "FILIAL_DS_ORIGEM");
		sql.addProjection("ds.nr_docto_servico", "NR_DOCTO_SERVICO");
		sql.addProjection("fildsdestino.sg_filial", "FILIAL_DS_DESTINO");
		sql.addProjection("trunc(cast(ds.dh_emissao as date))", "EMISSAO");
		sql.addProjection("(" +
						    "SELECT MIN(NFCTMP.NR_NOTA_FISCAL) " +
						    "FROM CONHECIMENTO CTMP, " +
						    		"NOTA_FISCAL_CONHECIMENTO NFCTMP, " +
						    		"DOCTO_SERVICO DSTMP " +
						    "WHERE CTMP.ID_CONHECIMENTO = NFCTMP.ID_CONHECIMENTO " +
						    		"AND CTMP.ID_CONHECIMENTO = DSTMP.ID_DOCTO_SERVICO " +
						    		"AND DSTMP.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO" +
						  ") AS NOTA_FISCAL"); 
		
		sql.addProjection("F_CONV_MOEDA(ds.id_pais, ds.id_moeda, ?, ?, ?, ddsf.vl_devido)", "VL_FRETE"); 
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(tfm.getLong("moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("filddsf.sg_filial", "FILIAL_COBRANCA_DEVEDOR");
		
		sql.addProjection("F_CONV_MOEDA(ds.id_pais, ds.id_moeda, ?, ?, ?, ds.vl_mercadoria)", "VL_MERCADORIA");
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(tfm.getLong("moeda.idMoeda"));
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("ds.ps_real", "PESO");
		sql.addProjection("ds.ps_aforado", "P_DENSIDADE");
		sql.addProjection("ds.dt_prev_entrega", "DPE");
		sql.addProjection("ddsf.tp_situacao_cobranca", "TP_SITUACAO_COBRANCA");
		sql.addProjection("(" +
							"SELECT MIN(TRUNC(EDS.DH_EVENTO)) " +
						    "FROM  EVENTO_DOCUMENTO_SERVICO EDS, " +
						    		"EVENTO E " +
						    "WHERE EDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO " +
						        	"AND E.ID_EVENTO = EDS.ID_EVENTO " +
						        	"AND E.CD_EVENTO = 21 " +
						        	"AND EDS.BL_EVENTO_CANCELADO = 'N'" +
						  ") AS ENTREGA");
		sql.addProjection("ddsf.dt_liquidacao", "BAIXA");
		
		/** FROM */
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("devedor_doc_serv_fat", "ddsf");
		sql.addFrom("filial", "fildsorigem");
		sql.addFrom("filial", "fildsdestino");
		sql.addFrom("filial", "filddsf");
		sql.addFrom("cliente", "cli");
		sql.addFrom("inscricao_estadual", "ie");
		sql.addFrom("filial", "filcli");
		sql.addFrom("pessoa", "pesfilcli");
		sql.addFrom("valor_dominio", "vdom");
		sql.addFrom("dominio", "dom");
		
		/** JOINS */
		sql.addJoin("ddsf.id_docto_servico", "ds.id_docto_servico");
		sql.addJoin("fildsorigem.id_filial", "ds.id_filial_origem");
		sql.addJoin("ds.id_filial_destino", "fildsdestino.id_filial (+)");
		sql.addJoin("filddsf.id_filial", "ddsf.id_filial");
		
		String tpClienteFiltroComp = tfm.getString("tpClienteFiltroComp");
		
		if(tpClienteFiltroComp.equals("RF")) {
			sql.addJoin("cli.id_cliente", "ddsf.id_cliente");
		
		} else if (tpClienteFiltroComp.equals("RR")) {
			sql.addJoin("cli.id_cliente", "ds.id_cliente_remetente");

		} else if(tpClienteFiltroComp.equals("RD")) {
			sql.addJoin("cli.id_cliente", "ds.id_cliente_destinatario");
		}
			
		sql.addJoin("ie.id_pessoa", "cli.id_cliente");
		sql.addJoin("filcli.id_filial", "cli.id_filial_cobranca");
		sql.addJoin("pesfilcli.id_pessoa", "filcli.id_filial");
		sql.addJoin("dom.id_dominio", "vdom.id_dominio");
		
		/** CRITERIA */
		this.getCriteriaDefaultReportExtrato(sql, tfm, idCliente);
		
		/** ORDER BY */
		sql.addOrderBy("ds.tp_documento_servico, fildsorigem.sg_filial, ds.nr_docto_servico");

		return sql;
	}
	
	/**
	 * Monta criteria default
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 29/06/2006
	 *
	 * @param sql
	 * @param tfm
	 */
	private void getCriteriaDefaultReportExtrato(SqlTemplate sql, TypedFlatMap tfm, Long idCliente){
		sql.addCustomCriteria("ie.bl_indicador_padrao = ?");
		sql.addCriteriaValue("S");
		
		// FILTROS PRINCIPAIS
		
		sql.addCriteria("ddsf.id_filial", "=", tfm.getLong("filialByIdFilial.idFilial"));
		
		if(tfm.getDomainValue("tpFrete").getValue() != null && StringUtils.isNotBlank(tfm.getDomainValue("tpFrete").getValue())){
			sql.addFrom("conhecimento", "c");
			sql.addJoin("c.id_conhecimento(+)", "ds.id_docto_servico");
			sql.addCriteria("c.tp_frete", "=", tfm.getDomainValue("tpFrete").getValue());
		}
		
		sql.addCriteria("ds.id_servico", "=", tfm.getLong("servico.idServico"));
		
		boolean adicionaServico = false;
		
		if(tfm.getDomainValue("modal").getValue() != null && StringUtils.isNotBlank(tfm.getDomainValue("modal").getValue())){
			adicionaServico = true;
			sql.addCriteria("serv.tp_modal", "=", tfm.getDomainValue("modal").getValue());
		}
		
		if(tfm.getDomainValue("abrangencia") != null && StringUtils.isNotBlank(tfm.getDomainValue("abrangencia").getValue())){
			adicionaServico = true;
			sql.addCriteria("serv.tp_abrangencia", "=", tfm.getDomainValue("abrangencia").getValue());
		}
		
		if (adicionaServico) {
			sql.addFrom("servico", "serv");
			sql.addJoin("serv.id_servico(+)", "ds.id_servico");
		}
		
		String dvEstadoCobranca = tfm.getDomainValue("estadoCobranca").getValue();
		if(StringUtils.isNotBlank(dvEstadoCobranca)){
			
			if(dvEstadoCobranca.equals("PE")){
				
				sql.addCustomCriteria("( trunc(cast(ds.dh_emissao as date)) between ? and ? )");
				sql.addCustomCriteria("( ddsf.dt_liquidacao is null )");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dtFinal"));
				
				sql.addCustomCriteria("( ddsf.tp_situacao_cobranca in (?, ?, ?) )");
				sql.addCriteriaValue("P");
				sql.addCriteriaValue("C");
				sql.addCriteriaValue("F");
				
			}else if(dvEstadoCobranca.equals("PA")){
				
				sql.addCustomCriteria("( ddsf.dt_liquidacao between ? and ? )");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dtFinal"));
				
			}else if(dvEstadoCobranca.equals("AF")){
				
				sql.addCustomCriteria("( trunc(cast(ds.dh_emissao as date)) between ? and ? )");
				sql.addCriteriaValue(tfm.getYearMonthDay("dtInicial"));
				sql.addCriteriaValue(tfm.getYearMonthDay("dtFinal"));
				
				sql.addCustomCriteria("( ddsf.tp_situacao_cobranca in (?, ?) )");
				sql.addCriteriaValue("P");
				sql.addCriteriaValue("C");
				
			}
			
		}else{
			
			sql.addCustomCriteria("( trunc(cast(ds.dh_emissao as date)) between ? and ? )");
			sql.addCustomCriteria("( ddsf.tp_situacao_cobranca != ? )");
			
			sql.addCriteriaValue(tfm.getYearMonthDay("dtInicial"));
			sql.addCriteriaValue(tfm.getYearMonthDay("dtFinal"));
			sql.addCriteriaValue("N");
		}
		
		// MÚLTIPLA ESCOLHA DE CLIENTES
		
		sql.addCriteria("ddsf.id_divisao_cliente", "=", tfm.getLong("divisaoCliente.idDivisaoCliente"));
		String tpClienteFiltroComp = tfm.getString("tpClienteFiltroComp");
	
		// FILTROS COMPLEMENTARES DO CLIENTE
		
		if(tpClienteFiltroComp.equals("RF")) {
			sql.addCriteria("ddsf.id_cliente", "=", idCliente);
		} else if (tpClienteFiltroComp.equals("RR")) {
			sql.addCriteria("ds.id_cliente_remetente", "=", idCliente);
		} else if(tpClienteFiltroComp.equals("RD")) {
			sql.addCriteria("ds.id_cliente_destinatario", "=", idCliente);
		}
		
		if (StringUtils.isNotBlank(tfm.getString("grupoEconomico.idGrupoEconomico"))) {
			sql.addCriteria("cli.id_grupo_economico", "=", tfm.getLong("grupoEconomico.idGrupoEconomico"));
		}
		
		// QUE TENHAM COMO CLIENTE
		
		String tpClienteWithCliente = tfm.getString("tpClienteWithCliente");
		if(tpClienteWithCliente.equals("RF")) {
			sql.addCriteria("ddsf.id_cliente", "=", tfm.getLong("cliente.idCliente"));
		} else if (tpClienteWithCliente.equals("RR")) {
			sql.addCriteria("ds.id_cliente_remetente", "=", tfm.getLong("cliente.idCliente"));
		} else if(tpClienteWithCliente.equals("RD")) {
			sql.addCriteria("ds.id_cliente_destinatario", "=", tfm.getLong("cliente.idCliente"));
		}
		
		sql.addCriteria("dom.nm_dominio", "=", "DM_TIPO_DOCUMENTO_SERVICO");
		sql.addCustomCriteria("lower(ds.tp_documento_servico) = lower(vdom.vl_valor_dominio)");
		
		sql.addCriteriaIn("DS.TP_DOCUMENTO_SERVICO", new Object[] { "NFS", "NFT", "CTR", "NDN", "NSE", "NTE", "CTE" });
				
	}

	/**
	 * Monta o filterSummary do report
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 * 
	 *
	 * @param sql
	 * @param tfm
	 *
	 */
	private SqlTemplate addFilterSummary(SqlTemplate sql, TypedFlatMap tfm) {
		
		sql.addFilterSummary("filialCobranca", !tfm.getString("sgFilial").equals("") && !tfm.getString("filialByIdFilial.pessoa.nmFantasia").equals("") ? tfm.getString("sgFilial") + " - " + tfm.getString("filialByIdFilial.pessoa.nmFantasia") : null);
		sql.addFilterSummary("tipoFrete", !tfm.getDomainValue("tpFrete").getValue().equals("") ? super.getDomainValueService().findDomainValueDescription("DM_TIPO_FRETE", tfm.getDomainValue("tpFrete").getValue()) : null);
		sql.addFilterSummary("periodoInicial", JTFormatUtils.format(tfm.getYearMonthDay("dtInicial")));
		sql.addFilterSummary("periodoFinal", JTFormatUtils.format(tfm.getYearMonthDay("dtFinal")));
		sql.addFilterSummary("servico", tfm.getString("dsServico"));
		sql.addFilterSummary("modal", !tfm.getDomainValue("modal").getValue().equals("") ? super.getDomainValueService().findDomainValueDescription("DM_MODAL", tfm.getDomainValue("modal").getValue()) : null);
		sql.addFilterSummary("abrangencia", !tfm.getDomainValue("abrangencia").getValue().equals("") ? super.getDomainValueService().findDomainValueDescription("DM_ABRANGENCIA", tfm.getDomainValue("abrangencia").getValue()) : null);
		sql.addFilterSummary("estadoCobranca", !tfm.getDomainValue("estadoCobranca").getValue().equals("") ? super.getDomainValueService().findDomainValueDescription("DM_ESTADO_COBRANCA_EXTRATO", tfm.getDomainValue("estadoCobranca").getValue()) : null);
		sql.addFilterSummary("divisao", tfm.getString("descricaoDivisaoCliente"));
		sql.addFilterSummary("moeda", tfm.getString("sgSimbolo"));
		sql.addFilterSummary("identificacaoParcial", tfm.getString("identificacaoParcial"));
		sql.addFilterSummary("tipoCliente", tfm.getString("tpClienteComplementar"));
		sql.addFilterSummary("nome", tfm.getString("nmCliente"));
		sql.addFilterSummary("apelido", tfm.getString("apelido"));
		sql.addFilterSummary("grupoEconomico", tfm.getString("dsGrupoEconomico"));
		sql.addFilterSummary("tipoCliente", !tfm.getString("tpClienteFiltroComp").equals("") ? super.getDomainValueService().findDomainValueDescription("DM_TIPO_CLIENTE_EXTRATO", tfm.getString("tpClienteFiltroComp")) : null);

		sql.addFilterSummary("cliente", tfm.getString("cliente.pessoa.nmPessoa"));
		sql.addFilterSummary("tipoCliente", !tfm.getString("tpClienteWithCliente").equals("") ? super.getDomainValueService().findDomainValueDescription("DM_TIPO_CLIENTE_EXTRATO", tfm.getString("tpClienteWithCliente")) : null);
		
		return sql;
			
	}

	/**
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 *
	 * @param sql
	 * @param tfm
	 *
	 */
	private void mountFilterIdCliente(SqlTemplate sql, String tpCliente) {
		if(tpCliente.equals("RF"))
			sql.addCustomCriteria("ddsf.id_cliente = cli.id_Cliente");
			
		else if(tpCliente.equals("RR"))
			sql.addCustomCriteria("ds.id_cliente_remetente = cli.id_Cliente");
			
		else if(tpCliente.equals("RD"))
			sql.addCustomCriteria("ds.id_cliente_destinatario = cli.id_Cliente");
	}
	
	
	
	/** ########################################   SUBREPORT PRINCIPAL  ######################################### */
	
	/**
	 * SUBREPORT PRINCIPAL
	 * 
	 * @author HectorJ
	 * @since 03/07/2006
	 * 
	 * @param Object[] param
	 * @return JRDataSource
	 * 
	 * @throws Exception
	 */
	public JRDataSource executeSubReport(Object[] param) throws Exception {
		return new JREmptyDataSource();
	}
	
	/** ########################################   SUBREPORT CIF - FOB - RESP FRETE - TOTAL  ######################################### */
	
	
	
	/**
	 * SubReport CIF - FOB - RESP FRETE - TOTAL 
	 * 
	 * @author HectorJ
	 * @since 03/07/2006
	 * 
	 * @param Object[] param
	 * @return JRDataSource
	 * 
	 * @throws Exception
	 */
	public JRDataSource executeSubReportCifFobFreteTotalExtrato(Object[] param) throws Exception {
		
		Set idsClientes = (Set) param[0];
		
		/** Monta o select para o subReport */
		List lst = this.mountSelectSubReportCifFobFreteTotalExtrato(idsClientes);
				
		/** Itera o ResultSet e soma os totais de docto de servicos CIF - FOB - RESP FRETE - TOTAL */ 
		List lstSum = iteratorToCalculateSumCifFobFreteTotalExtrato(((SqlTemplate)lst.get(0)).getSql(), (Object[]) lst.get(1));
		
		/** Itera o resultSet para montar o retorno para o relatório */
		List list = iteratorResultSetSubReportCifFobFreteTotalExtrato(((SqlTemplate)lst.get(0)).getSql()
																	 , (Object[]) lst.get(1)
																	 , (BigDecimal) lstSum.get(0)
																	 , (BigDecimal) lstSum.get(1)
																	 , (BigDecimal) lstSum.get(2)
																	 , (BigDecimal) lstSum.get(3));
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(list);

		return jrMap;
	}
	
	/**
	 * Monta o select da consulta CIF - FOB - RESP FRETE - TOTAL 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 03/07/2006
	 *
	 * @return List
	 *
	 */
	public List mountSelectSubReportCifFobFreteTotalExtrato(Set idsClientes){
		
		/** Busca o subselect para ser usado no select */
		List lst = this.mountSubSelectSubReportCifFobFreteTotalExtrato(idsClientes);
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("DIAS_ATRASO", "DIAS_ATRASO");
		sql.addProjection("SUM (QUANTIDADE_CIF)", "QUANTIDADE_CIF");
		sql.addProjection("SUM (QUANTIDADE_FOB)", "QUANTIDADE_FOB");
		sql.addProjection("SUM (QUANTIDADE_RESP)", "QUANTIDADE_RESP");
		sql.addProjection("SUM (QUANTIDADE_TOT)", "QUANTIDADE_TOT");
		
		/** Adiciona o subSelect no select */
		sql.addFrom("\n ( \n" + (String) lst.get(0) + "\n ) \n");
		
		sql.addGroupBy("DIAS_ATRASO \nHAVING DIAS_ATRASO IS NOT NULL");
		sql.addOrderBy("1", "ASC");
		
		lst.remove(0);
		lst.add(0, sql);
		
		return lst;
	
	}
	
	/**
	 * Monta o subSelect da consulta CIF - FOB - RESP FRETE - TOTAL 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 03/07/2006
	 *
	 * @return List
	 *
	 */
	public List mountSubSelectSubReportCifFobFreteTotalExtrato(Set idsClientes){
		
		/** Busca o sub subselect para ser usado no subselect */
		List lst = this.mountSubSubSelectSubReportCifFobFreteTotalExtrato(idsClientes);
		
		StringBuffer sb = new StringBuffer();
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("DIAS_ATRASO", "DIAS_ATRASO");
		sql.addProjection("SUM (QUANTIDADE_CIF)", "QUANTIDADE_CIF");
		sql.addProjection("SUM (QUANTIDADE_FOB)", "QUANTIDADE_FOB");
		sql.addProjection("SUM (QUANTIDADE_RESP)", "QUANTIDADE_RESP");
		sql.addProjection("SUM (QUANTIDADE_TOT)", "QUANTIDADE_TOT");
		
		/** Adiciona o sub subSelect no subSelect */
		sql.addFrom("\n ( \n" + (String) lst.get(0) + "\n ) \n");
		
		sql.addGroupBy("DIAS_ATRASO");
		
		/** Adiciona no StringBuffer  o sql com o sub subSelect e o subSelect */
		sb.append(sql.getSql());
		
		/** Adiciona UNION's ao subSelect */
		sb.append("\n UNION ALL SELECT		-4, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		-3, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		-2, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		-1, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		0, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		1, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		2, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		3, 0, 0, 0, 0 FROM DUAL ");
		sb.append("\n UNION ALL SELECT		4, 0, 0, 0, 0 FROM DUAL ");
		
		lst.remove(0);
		lst.add(0, sb.toString());
		
		return lst;
	}
	
	/**
	 * Monta o sub subSelect da consulta CIF - FOB - RESP FRETE - TOTAL 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 03/07/2006
	 *
	 * @return List
	 *
	 */
	public List mountSubSubSelectSubReportCifFobFreteTotalExtrato(Set idsClientes){
		List retorno = new ArrayList();
		
		SqlTemplate sql = this.createSqlTemplate();
		
		// SqlTemplate criado para armazenar os criteriavalues de vários SQLs templates para realizar o UNION
		SqlTemplate sqlCriteria = this.createSqlTemplate();
		
		StringBuffer sb = new StringBuffer();
		
		/** String com interrogações concatenadas de acordo com o número de ids, para ser usado no filtro */
		String sizeIdsCliente = repeatParameterIn(idsClientes.size());
		
		/**
		 * MONTA OS SUB - SUBSELECT'S DA CONSULTA
		 */
		
		// CIF
		this.getGenericProjectionSubSubSelectCifFobFreteTotal(sql, Integer.valueOf(1));
		this.getGenericFromSubSubSelectCifFobFreteTotal(sql);
		
		sql.addCustomCriteria("DS.ID_CLIENTE_REMETENTE IN (" + sizeIdsCliente + ") ");
		sqlCriteria.addCriteriaValue(idsClientes.toArray());
		sql.addCriteria("C.TP_FRETE", "=", "?");
		sqlCriteria.addCriteriaValue("C");
		
		this.getGenericCriteriaSubSubSelectCifFobFreteTotal(sql, sqlCriteria, idsClientes);
		
		sb.append(sql.getSql());
		sb.append("\nUNION ALL\n");
		sql = this.createSqlTemplate();
		
		// FOB
		this.getGenericProjectionSubSubSelectCifFobFreteTotal(sql, Integer.valueOf(2));
		this.getGenericFromSubSubSelectCifFobFreteTotal(sql);
		
		sql.addCustomCriteria("DS.ID_CLIENTE_DESTINATARIO IN (" + sizeIdsCliente + ") ");
		sqlCriteria.addCriteriaValue(idsClientes.toArray());
		sql.addCriteria("C.TP_FRETE", "=", "?");
		sqlCriteria.addCriteriaValue("F");
		
		this.getGenericCriteriaSubSubSelectCifFobFreteTotal(sql, sqlCriteria, idsClientes);
		
		sb.append(sql.getSql());
		sb.append("\nUNION ALL\n");
		sql = this.createSqlTemplate();
		
		// RESPONSÁVEL PELO FRETE
		// Busca sempre que o cliente é o responsável pelo frete
		this.getGenericProjectionSubSubSelectCifFobFreteTotal(sql, Integer.valueOf(3));
		this.getGenericFromSubSubSelectCifFobFreteTotal(sql);
		sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ('CTR','CRT','NFT')");
		sql.addCustomCriteria("DEV.ID_CLIENTE IN (" + sizeIdsCliente + ") ");
		sqlCriteria.addCriteriaValue(idsClientes.toArray());
		this.getGenericCriteriaSubSubSelectCifFobFreteTotal(sql, sqlCriteria, idsClientes);
		
		sb.append(sql.getSql());
		sb.append("\nUNION ALL\n");
		sql = this.createSqlTemplate();
		
		// TOTAL
		// Busca todos os documento onde o cliente é remetente ou destinatário ou devedor
		this.getGenericProjectionSubSubSelectCifFobFreteTotal(sql, Integer.valueOf(4));
		this.getGenericFromSubSubSelectCifFobFreteTotal(sql);
		sql.addCustomCriteria("DS.TP_DOCUMENTO_SERVICO IN ('CTR','CRT','NFT', 'CTE', 'NTE')");
		sql.addCustomCriteria("( " +
				  "  DEV.ID_CLIENTE IN  (" + sizeIdsCliente + ")  " +
				  "  OR	DS.ID_CLIENTE_DESTINATARIO IN  (" + sizeIdsCliente + ")  " +
				  "  OR	DS.ID_CLIENTE_REMETENTE IN  (" + sizeIdsCliente + ")  " +
				  ")");
		sqlCriteria.addCriteriaValue(idsClientes.toArray());
		sqlCriteria.addCriteriaValue(idsClientes.toArray());
		sqlCriteria.addCriteriaValue(idsClientes.toArray());
		this.getGenericCriteriaSubSubSelectCifFobFreteTotal(sql, sqlCriteria, idsClientes);
		
		sb.append(sql.getSql());
		
		/** Adiciona os filtros e o sql na List */
		retorno.add(sb.toString());
		retorno.add(sqlCriteria.getCriteria());
		
		return retorno;
	}
	
	/**
	 * Método responsável por montar a PROJECTION dos subselect's do subrelatório  
	 *
	 * tpCount == 1  --->  CIF
	 * tpCount == 2  --->  FOB
	 * tpCount == 3  --->  RESPONSÁVEL PELO FRETE
	 * tpCount == 4  --->  TOTAL
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 30/06/2006
	 *
	 * @param sql
	 * @param tpCount
	 *
	 */
	private void getGenericProjectionSubSubSelectCifFobFreteTotal(SqlTemplate sql, Integer tpCount){
		
		if(tpCount.equals(Integer.valueOf(1)))
			sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "QUANTIDADE_CIF");
		else
			sql.addProjection("0", "QUANTIDADE_CIF");
		
		if(tpCount.equals(Integer.valueOf(2)))
			sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "QUANTIDADE_FOB");
		else
			sql.addProjection("0", "QUANTIDADE_FOB");
		
		if(tpCount.equals(Integer.valueOf(3)))
			sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "QUANTIDADE_RESP");	
		else
			sql.addProjection("0", "QUANTIDADE_RESP");
		
		if(tpCount.equals(Integer.valueOf(4)))
			sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "QUANTIDADE_TOT");	
		else
			sql.addProjection("0", "QUANTIDADE_TOT");
		
		sql.addProjection("LEAST(GREATEST((DS.NR_DIAS_REAL_ENTREGA - DS.NR_DIAS_PREV_ENTREGA), -4) ,4)"
				, "DIAS_ATRASO");
		
	}
	
	/**
	 * Método responsável por montar a clausula FROM dos subselect's do subrelatório  
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 30/06/2006
	 *
	 * @param sql
	 *
	 */
	private void getGenericFromSubSubSelectCifFobFreteTotal(SqlTemplate sql){
		
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DEV");
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("CONHECIMENTO", "C");
		sql.addFrom("TMP_DS_EXTRATO_CLIENTE", "TMP");
		
		sql.addJoin("DS.ID_DOCTO_SERVICO", "TMP.ID_DOCTO_SERVICO ");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DEV.ID_DOCTO_SERVICO");
		sql.addJoin("C.ID_CONHECIMENTO(+)", "DS.ID_DOCTO_SERVICO");
		
	}
	
	/**
	 * Método responsável por montar os FILTROS dos subselect's do subrelatório  
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 30/06/2006
	 *
	 * @param sql
	 *
	 */
	private void getGenericCriteriaSubSubSelectCifFobFreteTotal(SqlTemplate sql, SqlTemplate sqlCriteria, Set idsClientes){
		
		sql.addCustomCriteria("DS.NR_DOCTO_SERVICO > ?");
		sqlCriteria.addCriteriaValue(Integer.valueOf(0));
		
		sql.addGroupBy("(DS.NR_DIAS_REAL_ENTREGA - DS.NR_DIAS_PREV_ENTREGA)");
		
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport CIF - FOB - RESP FRETE - TOTAL 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 14/07/2006
	 *
	 * @param String sql
	 * @param Object[] criteria
	 *
	 * @return List lst
	 */
	private List iteratorToCalculateSumCifFobFreteTotalExtrato(String sql, Object[] criteria){
		
		JRReportDataObject jr = executeQuery(sql, JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),criteria));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();
		
		List lst = new ArrayList();
		
		BigDecimal totalCif = new BigDecimal(0);
		BigDecimal totalFob = new BigDecimal(0);
		BigDecimal totalFrete = new BigDecimal(0);
		BigDecimal totalTot = new BigDecimal(0);		
		
		try {
			while (ds.next()){
				totalCif = BigDecimalUtils.add(totalCif, ds.getBigDecimal("QUANTIDADE_CIF"));
				totalFob = BigDecimalUtils.add(totalFob, ds.getBigDecimal("QUANTIDADE_FOB"));
				totalFrete = BigDecimalUtils.add(totalFrete, ds.getBigDecimal("QUANTIDADE_RESP"));
				totalTot = BigDecimalUtils.add(totalTot, ds.getBigDecimal("QUANTIDADE_TOT"));
			}
			
			lst.add(totalCif);
			lst.add(totalFob);
			lst.add(totalFrete);
			lst.add(totalTot);			
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
		
		return lst;
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport CIF - FOB - RESP FRETE - TOTAL 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 14/07/2006
	 *
	 * @param String sql
	 * @param Object[] criteria
	 *
	 * @return List lst
	 */
	private List iteratorResultSetSubReportCifFobFreteTotalExtrato(String sql
																 , Object[] criteria
																 , final BigDecimal totalCif
																 , final BigDecimal totalFob
																 , final BigDecimal totalResp
																 , final BigDecimal totalTot){
		
		JRReportDataObject jr = executeQuery(sql, JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),criteria));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();

		Map map;
		List lst = new ArrayList();

		try {
			while (ds.next()){
					
				map = new HashMap();
				
				map.put("QUANTIDADE_CIF", ds.getBigDecimal("QUANTIDADE_CIF"));
				map.put("QUANTIDADE_FOB", ds.getBigDecimal("QUANTIDADE_FOB"));
				map.put("QUANTIDADE_RESP", ds.getBigDecimal("QUANTIDADE_RESP"));
				map.put("QUANTIDADE_TOT", ds.getBigDecimal("QUANTIDADE_TOT"));
				map.put("DIAS_ATRASO", Long.valueOf(ds.getLong("DIAS_ATRASO")));
				
				if(!totalCif.equals(new BigDecimal("0")))
					map.put("EFICIENCIA_CIF", BigDecimalUtils.divide(ds.getBigDecimal("QUANTIDADE_CIF").multiply(new BigDecimal("100")), totalCif).setScale(2, BigDecimal.ROUND_HALF_UP));
				else
					map.put("EFICIENCIA_CIF", new BigDecimal("0"));
				
				if(!totalFob.equals(new BigDecimal("0")))
					map.put("EFICIENCIA_FOB", BigDecimalUtils.divide(ds.getBigDecimal("QUANTIDADE_FOB").multiply(new BigDecimal("100")), totalFob).setScale(2, BigDecimal.ROUND_HALF_UP));
				else
					map.put("EFICIENCIA_FOB", new BigDecimal("0"));
				
				if(!totalResp.equals(new BigDecimal("0")))
					map.put("EFICIENCIA_RESP", BigDecimalUtils.divide(ds.getBigDecimal("QUANTIDADE_RESP").multiply(new BigDecimal("100")), totalResp).setScale(2, BigDecimal.ROUND_HALF_UP));
				else
					map.put("EFICIENCIA_RESP", new BigDecimal("0"));
				
				if(!totalTot.equals(new BigDecimal("0")))
					map.put("EFICIENCIA_TOT", BigDecimalUtils.divide(ds.getBigDecimal("QUANTIDADE_TOT").multiply(new BigDecimal("100")), totalTot).setScale(2, BigDecimal.ROUND_HALF_UP));
				else
					map.put("EFICIENCIA_TOT", new BigDecimal("0"));
				
				lst.add(map);
				
			}
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
		
		return lst;
	}


	
	
	/** ########################################   SUBREPORT ENTREGAS - FATURAMENTO - PENDENTES - COBRADOS  ################################### */
	
	
	
	
	/**
	 * SubReport ENTREGAS - FATURAMENTO - PENDENTES - COBRADOS
	 * 
	 * @author HectorJ
	 * @since 04/07/2006
	 * 
	 * @param Object[] param
	 * @return JRDataSource
	 * 
	 * @throws Exception
	 */
	public JRDataSource executeSubReportEntFatPendCobExtrato(Object[] param) throws Exception {
		
		Set idsClientes = (Set) param[0];
		YearMonthDay dtInicial = (YearMonthDay) param[1];
		YearMonthDay dtFinal = (YearMonthDay) param[2];
		Long idMoedaDestino = (Long) param[3];
		Long idPaisDestino = (Long) param[4];
		String tpCliente = (String) param[5];
		
		SqlTemplate sql = createSqlTemplate();
		Map map = new HashMap(); 
		
		/** ############### ENTREGAS ############### */
		
		List lst = this.mountSelectSubReportEntregasExtrato(idsClientes);
		
		/** Itera o resultSet e adiciona no Map geral os elementos do Map ENTREGAS */
		this.iteratorResultSetSubReportEntregasExtrato((SqlTemplate)lst.get(0), (Object[])lst.get(1), map);
		
		/** ############### FATURAMENTO ############### */
		
		sql = getSqlTemplateFaturanmento(tpCliente, idsClientes, idMoedaDestino, idPaisDestino);

		/** Itera o resultSet e adiciona no Map geral os elementos do Map FATURAMENTO */
		this.iteratorResultSetSubReportFaturamentoExtrato(sql, map);
		
		/** ############### PENDENTES ############### */
		
		sql = getSqlTemplatePendentes(tpCliente, idsClientes, idMoedaDestino, idPaisDestino);
		
		/** Itera o resultSet e adiciona no Map geral os elementos do Map PENDENTES */
		this.iteratorResultSetSubReportPendentesExtrato(sql, map);
		
		/** ############### COBRADOS ############### */
		
		sql = getSqlTemplateCobrados(tpCliente, idsClientes, dtInicial, dtFinal, idMoedaDestino, idPaisDestino);
		
		/** Itera o resultSet e adiciona no Map geral os elementos do Map COBRADOS */
		this.iteratorResultSetSubReportCobradosExtrato(sql, map);
		
		
		/** ############### EFICIÊNCIA MÉDIA ############### */
		
		lst = getSqlTemplateEficienciaMedia(idsClientes);
		
		/** Itera o resultSet e adiciona no Map geral os elementos do Map EFICIÊNCIA MÉDIA */
		this.iteratorResultSetSubReportEficienciaMediaExtrato( (String) lst.get(0), (Object[]) lst.get(1), map);
		
		/** ############### EFICIÊNCIA EM DIAS ############### */
		
		sql = getSqlTemplateEficienciaDias(idsClientes);
		
		/** Itera o resultSet e adiciona no Map geral os elementos do Map EFICIÊNCIA EM DIAS */
		this.iteratorResultSetSubReportEficienciaDiasExtrato(sql, map);
		
		lst = new ArrayList();
		lst.add(map);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(lst);

		return jrMap;
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport ENTREGAS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private void iteratorResultSetSubReportEntregasExtrato(SqlTemplate sql, Object[] array, final Map map){
		
		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),array));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();

		BigDecimal nrDoctosTotal = new BigDecimal(0);
		
		try {
			while(ds.next()){
				
				long disaAtraso = ds.getLong("DIAS_ATRASO");
				BigDecimal nrDoctos = ds.getBigDecimal("QUANTIDADE_TOT");
				
				nrDoctosTotal = BigDecimalUtils.add(nrDoctosTotal, nrDoctos);
				
				/** Verifica o que deve ser incluído no Map */
				if(disaAtraso == 1)
					map.put("QUANTIDADE_TOT_1", nrDoctos);
				else if(disaAtraso == 2)
					map.put("QUANTIDADE_TOT_2", nrDoctos);
				else if(disaAtraso == 3)
					map.put("QUANTIDADE_TOT_3", nrDoctos);
				else if(disaAtraso == 4)
					map.put("QUANTIDADE_TOT_4", nrDoctos);
				else if(disaAtraso == 5)
					map.put("QUANTIDADE_TOT_5", nrDoctos);
				else if(disaAtraso == 6)
					map.put("QUANTIDADE_TOT_6", nrDoctos);
				
			}
		} catch (JRException e){
			throw new InfrastructureException(e);
		}			
			
		map.put("QUANTIDADE_TOTAL", nrDoctosTotal);
	
		if (nrDoctosTotal.longValue() > 0){
			/** Calcula os totais e insere no Map */
			map.put("TOTAL_1", BigDecimalUtils.divide(((BigDecimal)map.get("QUANTIDADE_TOT_1")).multiply(new BigDecimal("100")), nrDoctosTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
			map.put("TOTAL_2", BigDecimalUtils.divide(((BigDecimal)map.get("QUANTIDADE_TOT_2")).multiply(new BigDecimal("100")), nrDoctosTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
			map.put("TOTAL_3", BigDecimalUtils.divide(((BigDecimal)map.get("QUANTIDADE_TOT_3")).multiply(new BigDecimal("100")), nrDoctosTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
			map.put("TOTAL_4", BigDecimalUtils.divide(((BigDecimal)map.get("QUANTIDADE_TOT_4")).multiply(new BigDecimal("100")), nrDoctosTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
			map.put("TOTAL_5", BigDecimalUtils.divide(((BigDecimal)map.get("QUANTIDADE_TOT_5")).multiply(new BigDecimal("100")), nrDoctosTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
			map.put("TOTAL_6", BigDecimalUtils.divide(((BigDecimal)map.get("QUANTIDADE_TOT_6")).multiply(new BigDecimal("100")), nrDoctosTotal).setScale(2, BigDecimal.ROUND_HALF_UP));
		} else {
			map.put("TOTAL_1", BigDecimal.ZERO);
			map.put("TOTAL_2", BigDecimal.ZERO);
			map.put("TOTAL_3", BigDecimal.ZERO);
			map.put("TOTAL_4", BigDecimal.ZERO);
			map.put("TOTAL_5", BigDecimal.ZERO);
			map.put("TOTAL_6", BigDecimal.ZERO);
		}
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport FATURAMENTO
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private void iteratorResultSetSubReportFaturamentoExtrato(SqlTemplate sql, final Map map){

		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();
		
		BigDecimal doctoServico = new BigDecimal(0);
		BigDecimal peso = new BigDecimal(0);
		BigDecimal pesoDensidade = new BigDecimal(0);
		BigDecimal valorFrete = new BigDecimal(0);
		BigDecimal valorMercadora = new BigDecimal(0);
		BigDecimal volumes = new BigDecimal(0);

		try {
			while(ds.next()){
					
				BigDecimal vlMercadoriaByMoeda = BigDecimal.ZERO;
				if(ds.getBigDecimal("VALOR_MERCADORIA") != null){
					vlMercadoriaByMoeda = ds.getBigDecimal("VALOR_MERCADORIA");
					
					valorMercadora = BigDecimalUtils.add(valorMercadora, vlMercadoriaByMoeda); 
				}
					
				pesoDensidade = BigDecimalUtils.add(pesoDensidade, ds.getBigDecimal("PESO_DENSIDADE"));
				valorFrete = valorFrete.add(ds.getBigDecimal("VALOR_FRETE"));
				volumes = BigDecimalUtils.add(volumes, ds.getBigDecimal("VOLUMES")); 
				doctoServico = BigDecimalUtils.add(doctoServico, ds.getBigDecimal("DOCTO_SERVICO"));
				peso = BigDecimalUtils.add(peso, ds.getBigDecimal("PESO"));
			}
				
			if (valorMercadora.longValue() > 0 && peso.longValue() > 0){
				map.put("FRETE_MERCADORIA_FAT", BigDecimalUtils.divide(valorFrete, valorMercadora).multiply(new BigDecimal(100)));
				map.put("FRETE_TONELADA_FAT", valorFrete.setScale(6).divide(peso.setScale(6),BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP));
				map.put("PESO_DOCUMENTO_FAT", BigDecimalUtils.divide(peso, doctoServico));
				map.put("VLMERC_KG_FAT", BigDecimalUtils.divide(valorMercadora, peso).setScale(2, BigDecimal.ROUND_HALF_UP));
			} else {
				map.put("FRETE_MERCADORIA_FAT", BigDecimal.ZERO);
				map.put("FRETE_TONELADA_FAT", BigDecimal.ZERO);
				map.put("PESO_DOCUMENTO_FAT", BigDecimal.ZERO);
				map.put("VLMERC_KG_FAT", BigDecimal.ZERO);
			}
			
			map.put("DOCTO_SERVICO_FAT", doctoServico);
			map.put("PESO_FAT", peso);
			map.put("PESO_DENSIDADE_FAT", pesoDensidade);
			map.put("VALOR_FRETE_FAT", valorFrete);
			map.put("VALOR_MERCADORIA_FAT", valorMercadora);
			map.put("VOLUMES_FAT", volumes);
			
			if( !doctoServico.equals(BigDecimal.ZERO) ){
				map.put("FRETE_DOCUMENTOS_FAT", valorFrete.divide(doctoServico,2,BigDecimal.ROUND_HALF_UP) );
			}else{
				map.put("FRETE_DOCUMENTOS_FAT", BigDecimal.ZERO );
			}
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport PENDENTES - COBRADOS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private void iteratorResultSetSubReportPendentesExtrato(SqlTemplate sql, final Map map){
		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();
			
		BigDecimal countDoc = new BigDecimal(0);
		BigDecimal dias = new BigDecimal(0);
		BigDecimal totalFrete = new BigDecimal(0);
			
		try {	
			while(ds.next()){
				countDoc = BigDecimalUtils.add(countDoc, ds.getBigDecimal("COUNT_DOC"));
				dias = BigDecimalUtils.add(dias, ds.getBigDecimal("DIAS"));
				
				totalFrete = totalFrete.add(ds.getBigDecimal("TOTAL_FRETE"));
				
			}
			
			map.put("DOCTO_SERVICO_PENDENTES", countDoc);

			if(countDoc.compareTo(BigDecimal.ZERO) != 0){
				map.put("MEDIA_DIAS_PENDENTES", BigDecimalUtils.divide(dias, countDoc).setScale(2, BigDecimal.ROUND_HALF_UP));
			}else{
				map.put("MEDIA_DIAS_PENDENTES", BigDecimal.ZERO);
			}
			
			map.put("TOTAL_FRETE_PENDENTES", totalFrete);
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport COBRADOS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private void iteratorResultSetSubReportCobradosExtrato(SqlTemplate sql, final Map map){
		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();

		BigDecimal doctoServico = new BigDecimal(0);
		BigDecimal mediaDias = new BigDecimal(0);
		BigDecimal totalFrete = new BigDecimal(0);

		BigDecimal count = new BigDecimal(0);
		try {
			while(ds.next()){
			
				count = count.add(new BigDecimal(1));
				
				doctoServico = BigDecimalUtils.add(doctoServico, ds.getBigDecimal("DOCTO_SERVICO"));
				mediaDias = BigDecimalUtils.add(mediaDias, ds.getBigDecimal("MEDIA_DIAS"));
				
				totalFrete = totalFrete.add(ds.getBigDecimal("TOTAL_FRETE"));
				
			}
			
			map.put("DOCTO_SERVICO_COBRADOS", doctoServico);
			
			if(count.compareTo(new BigDecimal(0)) != 0){
				map.put("MEDIA_DIAS_COBRADOS", BigDecimalUtils.divide(mediaDias, doctoServico).setScale(2, BigDecimal.ROUND_HALF_UP));
			}else{
				map.put("MEDIA_DIAS_COBRADOS", null);
			}
			
			map.put("TOTAL_FRETE_COBRADOS", totalFrete);
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport Eficiência Média
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private void iteratorResultSetSubReportEficienciaMediaExtrato(String sql, Object[] criteria, final Map map){
		
		JRReportDataObject jr = executeQuery(sql, JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),criteria));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();
			
		try {
			while(ds.next()){
				
				BigDecimal total = ds.getBigDecimal("QUANTIDADE_TOT");
				BigDecimal totalEficiencia = ds.getBigDecimal("QUANTIDADE_TOT_EFICIENTE");
				
				if(!total.equals(new BigDecimal(0)))
					map.put("EFICIENCIA_MEDIA", BigDecimalUtils.divide(totalEficiencia.multiply(new BigDecimal("100")), total).setScale(2, BigDecimal.ROUND_HALF_UP));
				else
					map.put("EFICIENCIA_MEDIA", new BigDecimal("0"));
			}
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport Eficiência Dias
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private void iteratorResultSetSubReportEficienciaDiasExtrato(SqlTemplate sql, final Map map){
		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();

		BigDecimal totalDoctoServico = new BigDecimal(0);
		BigDecimal totalDiasAtraso = new BigDecimal(0);
		
		try {
			while(ds.next()){
				totalDoctoServico = BigDecimalUtils.add(totalDoctoServico, ds.getBigDecimal("QUANTIDADE_TOT"));
				totalDiasAtraso = BigDecimalUtils.add(totalDiasAtraso, ds.getBigDecimal("DIAS_ENTREGA"));
			}
			
			if(!totalDoctoServico.equals(new BigDecimal("0")))
				map.put("EFICIENCIA_DIAS", BigDecimalUtils.divide(totalDiasAtraso, totalDoctoServico).setScale(2, BigDecimal.ROUND_HALF_UP));
			else
				map.put("EFICIENCIA_DIAS", new BigDecimal("0"));
			
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
	}
	
	/**
	 * Método responsável por montar o sql de Faturanmento
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param idsClientes
	 * @param idsDoctoServico
	 * @return
	 *
	 */
	private SqlTemplate getSqlTemplateFaturanmento(String tpCliente, Set idsClientes, Long idMoedaDestino, Long idPaisDestino){
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "DOCTO_SERVICO");
		sql.addProjection("SUM(DS.PS_REAL)", "PESO");
		sql.addProjection("SUM(DS.PS_AFORADO)", "PESO_DENSIDADE");
		sql.addProjection("F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, SUM(DDSF.VL_DEVIDO))", "VALOR_FRETE");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());		
		sql.addProjection("F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, SUM(DS.VL_MERCADORIA))", "VALOR_MERCADORIA");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addProjection("SUM(DS.QT_VOLUMES)", "VOLUMES");
		sql.addProjection("SUM(DS.VL_TOTAL_DOC_SERVICO)","VL_TOTAL_DOC_SERVICO");
		
		sql.addGroupBy("DS.ID_MOEDA, DS.ID_PAIS");
		
		this.getGenericFromEntFatPendCob(sql);
		this.getGenericCriteriaEntFatPendCob(tpCliente, sql, idsClientes);
		
		return sql;
	}

	/**
	 * Método responsável por montar o sql de Pendentes
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param idsClientes
	 * @param idsDoctoServico
	 * @return
	 *
	 */
	private SqlTemplate getSqlTemplatePendentes(String tpCliente, Set idsClientes, Long idMoedaDestino, Long idPaisDestino){
		
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "COUNT_DOC");
		sql.addProjection("SUM(TRUNC(SYSDATE) - TRUNC(DS.DH_EMISSAO))", "DIAS");
		
		sql.addProjection("F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, SUM(DDSF.VL_DEVIDO))", "TOTAL_FRETE");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addGroupBy("DS.ID_MOEDA, DS.ID_PAIS");
		
		this.getGenericFromEntFatPendCob(sql);
		this.getGenericCriteriaEntFatPendCob(tpCliente, sql, idsClientes);
		
		sql.addCustomCriteria("DDSF.DT_LIQUIDACAO IS NULL");
		
		return sql;
	}
	
	/**
	 * Método responsável por montar o sql de Cobrados
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param idsClientes
	 * @param idsDoctoServico
	 * @return
	 *
	 */
	private SqlTemplate getSqlTemplateCobrados(String tpCliente, Set idsClientes, YearMonthDay dtInicial, YearMonthDay dtFinal, Long idMoedaDestino, Long idPaisDestino){
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("DS.ID_MOEDA", "ID_MOEDA_ORIGEM");
		sql.addProjection("DS.ID_PAIS", "ID_PAIS_ORIGEM");
		sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "DOCTO_SERVICO");
		sql.addProjection("SUM(TRUNC(DDSF.DT_LIQUIDACAO) - TRUNC(DS.DH_EMISSAO))", "MEDIA_DIAS");
		
		sql.addProjection("F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, SUM(DDSF.VL_DEVIDO))", "TOTAL_FRETE");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sql.addGroupBy("DS.ID_MOEDA, DS.ID_PAIS");

		this.getGenericFromEntFatPendCob(sql);
		this.getGenericCriteriaEntFatPendCob(tpCliente, sql, idsClientes);
		
		sql.addCustomCriteria("(DDSF.DT_LIQUIDACAO BETWEEN  ? AND ? ) ");
		sql.addCriteriaValue(dtInicial);
		sql.addCriteriaValue(dtFinal);
		
		return sql;
	}
	
	/**
	 * Método responsável por montar o sql de Eficiência Média
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param idsClientes
	 * @param idsDoctoServico
	 * @return
	 *
	 */
	private List getSqlTemplateEficienciaMedia(Set idsClientes){
		List lst = new ArrayList();
		
		SqlTemplate sql = createSqlTemplate();
		SqlTemplate sqlSubSelect;
		
		/** SqlTemplate para armazenar o critéria */
		SqlTemplate sqlCriteria = createSqlTemplate();
		
		sqlSubSelect = createSqlTemplate();
		getGenericSqlTemplateEficiencia(sqlSubSelect, idsClientes);
		sqlSubSelect.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "QUANTIDADE_TOT");
		
		sql.addProjection("\n(\n" + sqlSubSelect.getSql() + "\n)\n", "QUANTIDADE_TOT");
		sqlCriteria.addCriteriaValue(sqlSubSelect.getCriteria());
		
		sqlSubSelect = createSqlTemplate();
		getGenericSqlTemplateEficiencia(sqlSubSelect, idsClientes);
		sqlSubSelect.addProjection("COUNT(DS.ID_DOCTO_SERVICO)", "QUANTIDADE_TOT_EFICIENTE");
		
		sqlSubSelect.addCustomCriteria("(DS.NR_DIAS_REAL_ENTREGA - DS.NR_DIAS_PREV_ENTREGA) <= ?");
		sqlSubSelect.addCriteriaValue(Integer.valueOf(0));
		sql.addProjection("\n(\n" + sqlSubSelect.getSql() + "\n)\n", "QUANTIDADE_TOT_EFICIENTE");
		sqlCriteria.addCriteriaValue(sqlSubSelect.getCriteria());
		
		/** FROM */
		sql.addFrom("DUAL");
		
		lst.add(sql.getSql());
		lst.add(sqlCriteria.getCriteria());
		
		return lst;
	}
	
	/**
	 * Método responsável por montar o sql de Eficiência Dias
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 17/07/2006
	 *
	 * @param idsClientes
	 * @param idsDoctoServico
	 * @return
	 *
	 */
	private SqlTemplate getSqlTemplateEficienciaDias(Set idsClientes){
		
		SqlTemplate sql = createSqlTemplate();
		
		getGenericSqlTemplateEficiencia(sql, idsClientes);
		
		sql.addProjection("COUNT(DS.ID_DOCTO_SERVICO) AS QUANTIDADE_TOT");
		sql.addProjection("SUM(DS.NR_DIAS_REAL_ENTREGA)", "DIAS_ENTREGA");
		
		sql.addCustomCriteria("DS.NR_DIAS_REAL_ENTREGA > 0");

		return sql;
	}
	
	/**
	 * Método responsável por montar o sql genérico da Eficiência Dias e Eficiência Média
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 17/07/2006
	 * 
	 * @param sql
	 * @param idsClientes
	 * @param idsDoctoServico
	 */
	private void getGenericSqlTemplateEficiencia(SqlTemplate sql, Set idsClientes){
		
		getGenericFromEntFatPendCob(sql);

		sql.addCriteria("DS.NR_DOCTO_SERVICO", ">", Integer.valueOf(0));	
		
		sql.addCustomCriteria("( " +
				  "  DDSF.ID_CLIENTE IN  (" + repeatParameterIn(idsClientes.size()) + ")  " +
				  "  OR	DS.ID_CLIENTE_DESTINATARIO IN  (" + repeatParameterIn(idsClientes.size()) + ")  " +
				  "  OR	DS.ID_CLIENTE_REMETENTE IN  (" + repeatParameterIn(idsClientes.size()) + ")  " +
				  ")");
		
		sql.addCriteriaValue(idsClientes.toArray());
		sql.addCriteriaValue(idsClientes.toArray());
		sql.addCriteriaValue(idsClientes.toArray());

		sql.addCustomCriteria("(DS.NR_DIAS_REAL_ENTREGA - DS.NR_DIAS_PREV_ENTREGA) IS NOT NULL");
		
	}
	
	/**
	 * Método responsável por montar a clausula FROM do subreport ENTREGAS - FATURAMENTO - PENDENTES - COBRADOS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 *
	 */
	private void getGenericFromEntFatPendCob(SqlTemplate sql){
		
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("TMP_DS_EXTRATO_CLIENTE", "TMP");
		
		sql.addJoin("DS.ID_DOCTO_SERVICO", "TMP.ID_DOCTO_SERVICO ");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DDSF.ID_DOCTO_SERVICO");
		
	}
	
	/**
	 * Método responsável por montar os FILTROS do subreport ENTREGAS - FATURAMENTO - PENDENTES - COBRADOS  
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 30/06/2006
	 *
	 * @param sql
	 *
	 */
	private void getGenericCriteriaEntFatPendCob(String tpClienteFiltroComp, SqlTemplate sql, Set idsClientes){
		
		if(tpClienteFiltroComp.equals("RF")) {
			SQLUtils.joinExpressionsWithComma(new ArrayList(idsClientes), sql, "DDSF.ID_CLIENTE");
		
		} else if (tpClienteFiltroComp.equals("RR")) {
			SQLUtils.joinExpressionsWithComma(new ArrayList(idsClientes), sql, "ds.id_cliente_remetente");

		} else if(tpClienteFiltroComp.equals("RD")) {
			SQLUtils.joinExpressionsWithComma(new ArrayList(idsClientes), sql, "ds.id_cliente_destinatario");
		}
	}
	
	/**
	 * Monta o select da consulta ENTREGAS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @return List
	 *
	 */
	public List mountSelectSubReportEntregasExtrato(Set idsClientes){
		
		/** Busca o subselect para ser usado no select */
		List lst = this.mountSubSelectSubReportEntregasExtrato(idsClientes);
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("DIAS_ATRASO", "DIAS_ATRASO");
		sql.addProjection("SUM (QUANTIDADE_TOT)", "QUANTIDADE_TOT");
		
		/** Adiciona o subSelect no select */
		sql.addFrom("\n ( \n" + (String) lst.get(0) + "\n ) \n");
		
		sql.addGroupBy("DIAS_ATRASO \n HAVING DIAS_ATRASO IS NOT NULL");
		sql.addOrderBy("1", "ASC");
		
		lst.remove(0);
		lst.add(0, sql);
		
		return lst;
	
	}
	
	/**
	 * Monta o subSelect da consulta ENTREGAS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @return List
	 *
	 */
	public List mountSubSelectSubReportEntregasExtrato(Set idsClientes){
		
		
		/** Busca o sub subselect para ser usado no subselect */
		List lst = this.mountSubSubSelectSubReportEntregasExtrato(idsClientes);
		
		StringBuffer sb = new StringBuffer();
		
		SqlTemplate sql = createSqlTemplate();
		
		
		sql.addProjection("DIAS_ATRASO", "DIAS_ATRASO");
		sql.addProjection("SUM (QUANTIDADE_TOT)", "QUANTIDADE_TOT");
		
		/** Adiciona o sub subSelect no subSelect */
		sql.addFrom("\n ( \n" + (String) lst.get(0) + "\n ) \n");
		
		sql.addGroupBy("DIAS_ATRASO");
		
		/** Adiciona no StringBuffer  o sql com o sub subSelect e o subSelect */
		sb.append(sql.getSql());
		
		/** Adiciona UNION's ao subSelect */
		sb.append("\n UNION SELECT		1, 0 FROM DUAL ");
		sb.append("\n UNION SELECT		2, 0 FROM DUAL ");
		sb.append("\n UNION SELECT		3, 0 FROM DUAL ");
		sb.append("\n UNION SELECT		4, 0 FROM DUAL ");
		sb.append("\n UNION SELECT		5, 0 FROM DUAL ");
		sb.append("\n UNION SELECT		6, 0 FROM DUAL ");
		
		lst.remove(0);
		lst.add(0, sb.toString());
		
		return lst;
	}
	
	/**
	 * Monta o sub subSelect da consulta ENTREGAS
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @return SqlTemplate
	 *
	 */
	public List mountSubSubSelectSubReportEntregasExtrato(Set idsClientes){
		
		List lst = new ArrayList();
		
		SqlTemplate sql = this.createSqlTemplate();
		
		sql.addProjection("COUNT(DS.NR_DIAS_REAL_ENTREGA)", "QUANTIDADE_TOT");
		sql.addProjection("LEAST(DS.NR_DIAS_REAL_ENTREGA, 6)", "DIAS_ATRASO");
		
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DEV");
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("TMP_DS_EXTRATO_CLIENTE", "TMP");
		
		sql.addJoin("DS.ID_DOCTO_SERVICO", "TMP.ID_DOCTO_SERVICO ");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DEV.ID_DOCTO_SERVICO");
		
		sql.addCriteria("DS.NR_DOCTO_SERVICO", ">", Integer.valueOf(0));
		
		sql.addCustomCriteria("( " +
				  "  DEV.ID_CLIENTE IN  (" + repeatParameterIn(idsClientes.size()) + ")  " +
				  "  OR	DS.ID_CLIENTE_DESTINATARIO IN  (" + repeatParameterIn(idsClientes.size()) + ")  " +
				  "  OR	DS.ID_CLIENTE_REMETENTE IN  (" + repeatParameterIn(idsClientes.size()) + ")  " +
				  ")");
		
		sql.addCriteriaValue(idsClientes.toArray());
		sql.addCriteriaValue(idsClientes.toArray());
		sql.addCriteriaValue(idsClientes.toArray());
		
		sql.addCriteria("DS.NR_DIAS_REAL_ENTREGA", ">", Integer.valueOf(0));
		
		sql.addGroupBy("DS.NR_DIAS_REAL_ENTREGA");
		
		lst.add(sql.getSql());
		lst.add(sql.getCriteria());
		
		return lst;
	}
	
	
	
	
	/** ########################################   SUBREPORT FATURAMENTO UF ORIGEM - DESTINO  ################################### */
	
	
	
	/**
	 * SubReport FATURAMENTO UF ORIGEM - DESTINO 
	 * 
	 * @author HectorJ
	 * @since 05/07/2006
	 * 
	 * @param Object[] param
	 * @return JRDataSource
	 * 
	 * @throws Exception
	 */
	public JRDataSource executeSubReportFaturamentoUfOrigemAndDestinoExtrato(Object[] param) throws Exception {
		
		Integer origemOrDestino = (Integer) param[0];
		Long idMoedaDestino = (Long) param[1];
		Long idPaisDestino = (Long) param[2];
		String tpClienteFiltroComp = (String) param[3];
		
		/** Monta o sqlTemplate */
		SqlTemplate sql = this.mountSqlTemplateFaturamentoUfOrigemOrDestino(tpClienteFiltroComp, origemOrDestino, idMoedaDestino, idPaisDestino);
				
		List lst = iteratorResultSetFatUfOrigemOrDestinoExtrato(sql);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(lst);
		
		return jrMap;
	}
	
	/**
	 * Monta o sqlTemplate de origem ou destino
	 * 
	 * origemOrDestino == 1 --> ORIGEM
	 * origemOrDestino == 2 --> DESTINO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param origemOrDestino
	 * @param idsDoctoServico
	 *
	 */
	private SqlTemplate mountSqlTemplateFaturamentoUfOrigemOrDestino(String tpClienteFiltroComp, Integer origemOrDestino, Long idMoedaDestino, Long idPaisDestino){
		
		SqlTemplate sql = createSqlTemplate();
		
		/** MONTA O SELECT PRINCIPAL */
		this.getGenericSelectFatUfOrigemOrDestino(tpClienteFiltroComp, sql, origemOrDestino);
		
		/** UF */
		sql.addProjection("UF.SG_UNIDADE_FEDERATIVA", "UF");

		sql.addProjection("COUNT(*)", "QUANTIDADE");
		sql.addProjection("SUM(F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, DDSF.VL_DEVIDO))", "FRETE_SOMATORIO");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addProjection("SUM(F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, DS.VL_MERCADORIA))", "MERCADORIA");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		sql.addProjection("SUM(F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, DS.PS_REAL))", "PESO");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());

		sql.addGroupBy("UF.SG_UNIDADE_FEDERATIVA");

		return sql;
	}
	
	/**
	 * Método responsável por montar o select FATURAMENTO UF ORIGEM - DESTINO
	 * 
	 * origemOrDestino == 1 --> ORIGEM
	 * origemOrDestino == 2 --> DESTINO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param origemOrDestino
	 *
	 */
	private void getGenericSelectFatUfOrigemOrDestino(String tpClienteFiltroComp, SqlTemplate sql, Integer origemOrDestino){
		
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("CLIENTE", "CLI");
		sql.addFrom("FILIAL", "FIL");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("ENDERECO_PESSOA", "EP");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF");
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addJoin("DDSF.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
		
		if(tpClienteFiltroComp.equals("RF")) {
			sql.addJoin("CLI.ID_CLIENTE", "DDSF.ID_CLIENTE");
		
		} else if (tpClienteFiltroComp.equals("RR")) {
			sql.addJoin("CLI.ID_CLIENTE", "ds.id_cliente_remetente");

		} else if(tpClienteFiltroComp.equals("RD")) {
			sql.addJoin("CLI.ID_CLIENTE", "ds.id_cliente_destinatario");
		}
		
		if(origemOrDestino.equals(Integer.valueOf(1)))
			sql.addJoin("FIL.ID_FILIAL", "DS.ID_FILIAL_ORIGEM");
		else
			sql.addJoin("FIL.ID_FILIAL", "DS.ID_FILIAL_DESTINO");
		
		sql.addJoin("P.ID_PESSOA", "FIL.ID_FILIAL");
		sql.addJoin("EP.ID_ENDERECO_PESSOA", "P.ID_ENDERECO_PESSOA");
		sql.addJoin("M.ID_MUNICIPIO", "EP.ID_MUNICIPIO");
		sql.addJoin("UF.ID_UNIDADE_FEDERATIVA", "M.ID_UNIDADE_FEDERATIVA");

		sql.addCustomCriteria("DS.ID_DOCTO_SERVICO IN (SELECT ID_DOCTO_SERVICO FROM TMP_DS_EXTRATO_CLIENTE)");
	}
	
	/**
	 * Método responsável por montar o subSelect FATURAMENTO UF ORIGEM - DESTINO
	 * 
	 * origemOrDestino == 1 --> ORIGEM
	 * origemOrDestino == 2 --> DESTINO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param origemOrDestino
	 *
	 */
	private void getGenericSubSelectFatUfOrigemOrDestino(String tpClienteFiltroComp, SqlTemplate sql, Integer origemOrDestino, boolean blRelacionarDevedor){
		
		sql.addFrom("DOCTO_SERVICO", "DSTMP");
		sql.addFrom("CLIENTE", "CLITMP");
		sql.addFrom("FILIAL", "FILTMP");
		sql.addFrom("PESSOA", "PTMP");
		sql.addFrom("ENDERECO_PESSOA", "EPTMP");
		sql.addFrom("MUNICIPIO", "MTMP");
		sql.addFrom("UNIDADE_FEDERATIVA", "UFTMP");
	
		if (blRelacionarDevedor || tpClienteFiltroComp.equals("RF")){
			sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSFTMP");
			sql.addJoin("DDSFTMP.ID_DOCTO_SERVICO", "DSTMP.ID_DOCTO_SERVICO");
		}
		
		
		if(tpClienteFiltroComp.equals("RF")) {
			sql.addJoin("CLITMP.ID_CLIENTE", "DDSFTMP.ID_CLIENTE");
		
		} else if (tpClienteFiltroComp.equals("RR")) {
			sql.addJoin("CLITMP.ID_CLIENTE", "dstmp.id_cliente_remetente");

		} else if(tpClienteFiltroComp.equals("RD")) {
			sql.addJoin("CLITMP.ID_CLIENTE", "dstmp.id_cliente_destinatario");
		}
		
		if(origemOrDestino.equals(Integer.valueOf(1)))
			sql.addJoin("FILTMP.ID_FILIAL", "DSTMP.ID_FILIAL_ORIGEM");
		else
			sql.addJoin("FILTMP.ID_FILIAL", "DSTMP.ID_FILIAL_DESTINO");
		
		sql.addJoin("PTMP.ID_PESSOA", "FILTMP.ID_FILIAL");
		sql.addJoin("EPTMP.ID_ENDERECO_PESSOA", "PTMP.ID_ENDERECO_PESSOA");
		sql.addJoin("MTMP.ID_MUNICIPIO", "EPTMP.ID_MUNICIPIO");
		sql.addJoin("UFTMP.ID_UNIDADE_FEDERATIVA", "MTMP.ID_UNIDADE_FEDERATIVA");
		
		sql.addJoin("DSTMP.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport FATURAMENTO UF ORIGEM - DESTINO
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private List iteratorResultSetFatUfOrigemOrDestinoExtrato(SqlTemplate sql){
		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();
			
		Map map = new HashMap();;
		List lst = new ArrayList();
		String ufAnterior = "";
		String ufAtual = "";
		BigDecimal quantidade = new BigDecimal(0);
		BigDecimal freteSomatorioTotal = new BigDecimal(0);
		BigDecimal freteSomatorio = new BigDecimal(0);
		BigDecimal freteSomatorioTmp = new BigDecimal(0);
		BigDecimal vlMercadoria = new BigDecimal(0);
		BigDecimal peso = new BigDecimal(0);
		BigDecimal freteTon = new BigDecimal(0);

		int count = 0;
		boolean blIsFirst = true;
		
		try {
			while(ds.next()){
				
				count++;
				ufAtual = ds.getString("UF");
				
				if((!ufAtual.equals(ufAnterior) && !blIsFirst)){
					
					map.put("UF", ufAnterior);
					map.put("QUANTIDADE", quantidade);
					map.put("FRETE_SOMATORIO", freteSomatorio);
					map.put("MERCADORIA", vlMercadoria);
					map.put("PESO", peso);
					map.put("FRETE_TON", freteTon);
					
					freteSomatorioTotal = freteSomatorioTotal.add(freteSomatorio);
					
					quantidade = new BigDecimal(0);
					freteSomatorio = new BigDecimal(0);
					vlMercadoria = new BigDecimal(0);
					peso = new BigDecimal(0);
					freteTon = new BigDecimal(0);
					
					lst.add(map);
					map = new HashMap();
				}

				quantidade = BigDecimalUtils.add(quantidade, ds.getBigDecimal("QUANTIDADE"));
				freteSomatorioTmp = ds.getBigDecimal("FRETE_SOMATORIO");
				freteSomatorio = BigDecimalUtils.add(freteSomatorio, freteSomatorioTmp);

				
				if(ds.getBigDecimal("MERCADORIA") != null){
					vlMercadoria = vlMercadoria.add(ds.getBigDecimal("MERCADORIA"));
				}
				
				peso = BigDecimalUtils.add(peso, ds.getBigDecimal("PESO"));
				
				if(ds.getBigDecimal("FRETE_SOMATORIO") != null && ds.getBigDecimal("PESO") != null){
					freteTon = freteSomatorio.divide(peso, 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				
				ufAnterior = ufAtual;
				blIsFirst = false;
			}
		} catch (JRException e){
			throw new InfrastructureException(e);
		}					
			
		if(count > 0){
			map.put("UF", ufAtual);
			map.put("QUANTIDADE", quantidade);
			map.put("FRETE_SOMATORIO", freteSomatorio);
			map.put("MERCADORIA", vlMercadoria);
			map.put("PESO", peso);
			map.put("FRETE_TON", freteTon);
			
			freteSomatorioTotal = freteSomatorioTotal.add(freteSomatorio);
			
			lst.add(map);
		}
		
		for (Iterator iter = lst.iterator(); iter.hasNext();) {
			map = (Map) iter.next();
			map.put("FRETE_SOMATORIO_UF", ((BigDecimal)map.get("FRETE_SOMATORIO")).divide(freteSomatorioTotal, 6, BigDecimal.ROUND_HALF_UP)
														.multiply(new BigDecimal("100")));
		}

			
		return lst;
		
	}
	
	/**
	 * Método responsável por replicar os elementos do array de objetos ( getCriteria() ), e copia-lo 5 vezes,
	 * o que corresponde aos 5 subselect's da consulta principal, que utilizam o mesmo critéria
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param sql
	 *
	 */
	public void copyCriteriaToSubSelect(SqlTemplate sql, int length){
		
		/** Copia os elementos do filtro para os outros 5 subSelect's */
		Object[] array = new Object[sql.getCriteria().length * length];
		int count = 0;
		
		/**
		 *  1° array de origem, 2° posição de inicio do array de origem, 3° array de destino,
		 *  4° posição do array de destino onde será copiado o elemento do array de origem, 
		 *  5° número de elementos que o array de destino receberá do array de origem
		 */
		for(int i = 0; i < length; i++){
			System.arraycopy(sql.getCriteria(), 0, array, count, sql.getCriteria().length);
			count += sql.getCriteria().length;
		}
		
		/** Adiciona ao criteria o novo array com os dados copiados */
		sql.addCriteriaValue(array);
	
	}
	
	
	
/** ########################################   SUBREPORT FATURAMENTO FILIAL ORIGEM - DESTINO  ################################### */
	
	
	
	/**
	 * SubReport FATURAMENTO FILIAL ORIGEM - DESTINO 
	 * 
	 * @author HectorJ
	 * @since 05/07/2006
	 * 
	 * @param Object[] param
	 * @return JRDataSource
	 * 
	 * @throws Exception
	 */
	public JRDataSource executeSubReportFaturamentoFilialOrigemAndDestinoExtrato(Object[] param) throws Exception {
		Integer origemOrDestino = (Integer) param[0];
		Long idMoedaDestino = (Long) param[1];
		Long idPaisDestino = (Long) param[2];
		String tpClienteFiltroComp = (String)param[3];
		
		/** Monta o sqlTemplate */
		SqlTemplate sql = this.mountSqlTemplateFaturamentoFilialOrigemOrDestino(tpClienteFiltroComp, origemOrDestino, idMoedaDestino, idPaisDestino);
		
		List lst = iteratorResultSetFatFilialOrigemOrDestinoExtrato(sql);
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(lst);
		
		return jrMap;
	}
	
	/**
	 * Monta o sqlTemplate de origem ou destino
	 * 
	 * origemOrDestino == 1 --> ORIGEM
	 * origemOrDestino == 2 --> DESTINO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param origemOrDestino
	 * @param idsDoctoServico
	 *
	 */
	private SqlTemplate mountSqlTemplateFaturamentoFilialOrigemOrDestino(String tpClienteFiltroComp, Integer origemOrDestino, Long idMoedaDestino, Long idPaisDestino){
		
		SqlTemplate sql = createSqlTemplate();
		SqlTemplate sqlSubSelect = createSqlTemplate();
		
		/** MONTA O SELECT PRINCIPAL */
		this.getGenericSelectFatFilialOrigemOrDestino(tpClienteFiltroComp, sql, origemOrDestino);
		
		/** UF */
		sql.addProjection("FIL.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("DS.ID_MOEDA", "ID_MOEDA_ORIGEM");
		sql.addProjection("DS.ID_PAIS", "ID_PAIS_ORIGEM");
		
		/** QUANTIDADE */
		this.getGenericSubSelectFatFilialOrigemOrDestino(tpClienteFiltroComp, sqlSubSelect, origemOrDestino, false);
		sqlSubSelect.addProjection("COUNT(DISTINCT DSTMP.ID_DOCTO_SERVICO)");
		sqlSubSelect.addCustomCriteria("FILTMP.ID_FILIAL = FIL.ID_FILIAL");
		sql.addProjection("SUM((\n" + sqlSubSelect.getSql() + "\n))", "QUANTIDADE");
		sqlSubSelect = createSqlTemplate();
		
		/** FRETE_SOMATORIO */
		this.getGenericSubSelectFatFilialOrigemOrDestino(tpClienteFiltroComp, sqlSubSelect, origemOrDestino, true);
		sqlSubSelect.addProjection("SUM(DDSFTMP.VL_DEVIDO)");
		sqlSubSelect.addCustomCriteria("FILTMP.ID_FILIAL = FIL.ID_FILIAL");
		
		
		sql.addProjection("F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, SUM((\n" + sqlSubSelect.getSql() + "\n)))", "FRETE_SOMATORIO");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sqlSubSelect = createSqlTemplate();
		
		/** MERCADORIA */
		this.getGenericSubSelectFatFilialOrigemOrDestino(tpClienteFiltroComp, sqlSubSelect, origemOrDestino, false);
		sqlSubSelect.addProjection("SUM(DSTMP.VL_MERCADORIA)");
		sqlSubSelect.addCustomCriteria("FILTMP.ID_FILIAL = FIL.ID_FILIAL");

		sql.addProjection("F_CONV_MOEDA(DS.ID_PAIS, DS.ID_MOEDA, ?, ?, ?, SUM((\n" + sqlSubSelect.getSql() + "\n)))", "MERCADORIA");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		sqlSubSelect = createSqlTemplate();
		
		/** PESO */
		this.getGenericSubSelectFatFilialOrigemOrDestino(tpClienteFiltroComp, sqlSubSelect, origemOrDestino, false);
		sqlSubSelect.addProjection("SUM(DSTMP.PS_REAL)");
		sqlSubSelect.addCustomCriteria("FILTMP.ID_FILIAL = FIL.ID_FILIAL");
		sql.addProjection("SUM((\n" + sqlSubSelect.getSql() + "\n))", "PESO");
		
		sql.addGroupBy("FIL.SG_FILIAL, DS.ID_MOEDA, DS.ID_PAIS");
		
		return sql;
	}
	
	/**
	 * Método responsável por montar o select FATURAMENTO FILIAL ORIGEM - DESTINO
	 * 
	 * origemOrDestino == 1 --> ORIGEM
	 * origemOrDestino == 2 --> DESTINO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param origemOrDestino
	 *
	 */
	private void getGenericSelectFatFilialOrigemOrDestino(String tpClienteFiltroComp, SqlTemplate sql, Integer origemOrDestino){
		
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("CLIENTE", "CLI");
		sql.addFrom("FILIAL", "FIL");
		sql.addFrom("TMP_DS_EXTRATO_CLIENTE", "TMP");
		
		sql.addJoin("DS.ID_DOCTO_SERVICO", "TMP.ID_DOCTO_SERVICO ");
		sql.addJoin("DDSF.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
		
		if(tpClienteFiltroComp.equals("RF")) {
			sql.addJoin("CLI.ID_CLIENTE", "DDSF.ID_CLIENTE");
		
		} else if (tpClienteFiltroComp.equals("RR")) {
			sql.addJoin("CLI.ID_CLIENTE", "ds.id_cliente_remetente");

		} else if(tpClienteFiltroComp.equals("RD")) {
			sql.addJoin("CLI.ID_CLIENTE", "ds.id_cliente_destinatario");
		}
		
		if(origemOrDestino.equals(Integer.valueOf(1)))
			sql.addJoin("FIL.ID_FILIAL", "DS.ID_FILIAL_ORIGEM");
		else
			sql.addJoin("FIL.ID_FILIAL", "DS.ID_FILIAL_DESTINO");
		
	}
	
	/**
	 * Método responsável por montar o subSelect FATURAMENTO FILIAL ORIGEM - DESTINO
	 * 
	 * origemOrDestino == 1 --> ORIGEM
	 * origemOrDestino == 2 --> DESTINO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 05/07/2006
	 *
	 * @param origemOrDestino
	 *
	 */
	private void getGenericSubSelectFatFilialOrigemOrDestino(String tpClienteFiltroComp, SqlTemplate sql, Integer origemOrDestino, boolean blRelacionarDevedor){
		
		sql.addFrom("DOCTO_SERVICO", "DSTMP");
		sql.addFrom("CLIENTE", "CLITMP");
		sql.addFrom("FILIAL", "FILTMP");
		
		if (blRelacionarDevedor || tpClienteFiltroComp.equals("RF")){
			sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSFTMP");
			sql.addJoin("DDSFTMP.ID_DOCTO_SERVICO", "DSTMP.ID_DOCTO_SERVICO");
		}
		
		if(tpClienteFiltroComp.equals("RF")) {
			sql.addJoin("CLITMP.ID_CLIENTE", "DDSFTMP.ID_CLIENTE");
		
		} else if (tpClienteFiltroComp.equals("RR")) {
			sql.addJoin("CLITMP.ID_CLIENTE", "dstmp.id_cliente_remetente");

		} else if(tpClienteFiltroComp.equals("RD")) {
			sql.addJoin("CLITMP.ID_CLIENTE", "dstmp.id_cliente_destinatario");
		}
		
		if(origemOrDestino.equals(Integer.valueOf(1)))
			sql.addJoin("FILTMP.ID_FILIAL", "DSTMP.ID_FILIAL_ORIGEM");
		else
			sql.addJoin("FILTMP.ID_FILIAL", "DSTMP.ID_FILIAL_DESTINO");

		sql.addJoin("DSTMP.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO");
		
	}
	
	/**
	 * Método responsável por iterar o resultSet do SubReport FATURAMENTO UF ORIGEM - DESTINO
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 04/07/2006
	 *
	 * @param sql
	 * @param array
	 * @param map
	 *
	 */
	private List iteratorResultSetFatFilialOrigemOrDestinoExtrato(SqlTemplate sql){
		
		JRReportDataObject jr = executeQuery(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()));
		
		JRAdsmDataSource ds = (JRAdsmDataSource)jr.getDataSource();
			
		Map map = new HashMap();
		List lst = new ArrayList();
		String filialAnterior = "";
		String filialAtual = "";
		BigDecimal quantidade = new BigDecimal(0);
		
		BigDecimal freteSomatorio = new BigDecimal(0);
		BigDecimal freteSomatorioTotal = new BigDecimal(0);
		BigDecimal freteSomatorioTmp = new BigDecimal(0);
		BigDecimal vlMercadoria = new BigDecimal(0);
		BigDecimal peso = new BigDecimal(0);
		BigDecimal freteTon = new BigDecimal(0);
		
		int count = 0;
		boolean blIsFirst = true;
		
		try {
			while(ds.next()){
				
				count++;
				filialAtual = ds.getString("SG_FILIAL");
				
				if((!filialAtual.equals(filialAnterior) && !blIsFirst)){
					
					map.put("SG_FILIAL", filialAnterior);
					map.put("QUANTIDADE", quantidade);
					map.put("FRETE_SOMATORIO", freteSomatorio);
					map.put("MERCADORIA", vlMercadoria);
					map.put("PESO", peso);
					map.put("FRETE_TON", freteTon);
					
					freteSomatorioTotal = freteSomatorioTotal.add(freteSomatorio);
					
					
					quantidade = new BigDecimal(0);
					freteSomatorio = new BigDecimal(0);
					vlMercadoria = new BigDecimal(0);
					peso = new BigDecimal(0);
					freteTon = new BigDecimal(0);
					
					lst.add(map);
					map = new HashMap();
				}
				
				
				quantidade = BigDecimalUtils.add(quantidade, ds.getBigDecimal("QUANTIDADE"));
				freteSomatorioTmp = ds.getBigDecimal("FRETE_SOMATORIO");
				freteSomatorio = BigDecimalUtils.add(freteSomatorio, freteSomatorioTmp);
				
				if(ds.getBigDecimal("MERCADORIA") != null){
					vlMercadoria = vlMercadoria.add(ds.getBigDecimal("MERCADORIA"));
				}
				
				peso = BigDecimalUtils.add(peso, ds.getBigDecimal("PESO")); 
				
				if(ds.getBigDecimal("FRETE_SOMATORIO") != null && ds.getBigDecimal("PESO") != null){
					freteTon = freteSomatorio.divide(peso, 6, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(1000)).setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				
				filialAnterior = filialAtual;
				blIsFirst = false;
			}
			
			if(count > 0){
				map.put("SG_FILIAL", filialAtual);
				map.put("QUANTIDADE", quantidade);
				map.put("FRETE_SOMATORIO", freteSomatorio);
				map.put("MERCADORIA", vlMercadoria);
				map.put("PESO", peso);
				map.put("FRETE_TON", freteTon);
				
				freteSomatorioTotal = freteSomatorioTotal.add(freteSomatorio);
				
				lst.add(map);
			}
			
			for (Iterator iter = lst.iterator(); iter.hasNext();) {
				map = (Map) iter.next();
				map.put("FRETE_SOMATORIO_UF", ((BigDecimal)map.get("FRETE_SOMATORIO")).divide(freteSomatorioTotal, 6, BigDecimal.ROUND_HALF_UP)
															.multiply(new BigDecimal("100")));
			}
		} catch (JRException e){
			throw new InfrastructureException(e);
		}
		
		return lst;
	}
	
	
	
	
	/** ########################################   SUBREPORT HISTÓRICO FINANCEIRO PERÍODO  ################################### */
	
	
	
	
	/**
	 * SubReport HISTÓRICO FINANCEIRO PERÍODO
	 * 
	 * @author HectorJ
	 * @since 06/07/2006
	 * 
	 * @param Object[] param
	 * @return JRDataSource
	 * 
	 * @throws Exception
	 */
	public JRDataSource executeSubReportHistoricoFatPeriodoExtrato(Object[] param) throws Exception {
		
		Set idsClientes = (Set) param[0];
		YearMonthDay dtInicial = (YearMonthDay) param[1];
		YearMonthDay dtFinal = (YearMonthDay) param[2];
		Long idMoedaDestino = (Long) param[3];
		Long idPaisDestino = (Long) param[4];
		
		
		/** Monta o sqlTemplate */
		SqlTemplate sql = this.mountSqlTemplateHistoricoFatPeriodo(idsClientes, dtInicial, dtFinal, idMoedaDestino, idPaisDestino);
	
		return executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	}
	
	/**
	 * Monta o sqlTemplate do HISTÓRICO FINANCEIRO PERÍODO
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 06/07/2006
	 *
	 * @param origemOrDestino
	 * @param idsDoctoServico
	 *
	 */
	private SqlTemplate mountSqlTemplateHistoricoFatPeriodo(Set idsClientes, YearMonthDay dtInicial, YearMonthDay dtFinal, Long idMoedaDestino, Long idPaisDestino){
		
		SqlTemplate sql = createSqlTemplate();
		SqlTemplate sqlSubSelect = createSqlTemplate();
		
		/** MONTA O SELECT PRINCIPAL */		
		sql.addProjection("F.ID_MOEDA", "ID_MOEDA_ORIGEM"); 
		sql.addProjection("UF.ID_PAIS", "ID_PAIS_ORIGEM"); 
		sql.addProjection("TO_CHAR(F.DT_VENCIMENTO, 'MM/YYYY')", "COMPETENCIA");
		sql.addProjection("TO_CHAR(F.DT_VENCIMENTO, 'YYYYMM')", "COMPETENCIA_ORDER");
		sql.addProjection("COUNT(F.ID_FATURA)", "QUANTIDADE_TITULOS");
		
		
		sql.addProjection("F_CONV_MOEDA(UF.ID_PAIS, F.ID_MOEDA, ?, ?, ?, SUM(F.VL_TOTAL))", "VALOR_TOTAL");
		sql.addCriteriaValue(idPaisDestino);
		sql.addCriteriaValue(idMoedaDestino);
		sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
		
		/** PONTUAL */
		sql.addProjection("(" +
							  "(SUM(( " + 
								  "		SELECT             COUNT(FTMP.ID_FATURA) " +
								  "		FROM               FATURA FTMP " +
								  "		WHERE              TO_CHAR(F.DT_VENCIMENTO, 'MM/YYYY') = TO_CHAR(FTMP.DT_VENCIMENTO, 'MM/YYYY') " +
								  "		AND                FTMP.DT_VENCIMENTO >= FTMP.DT_LIQUIDACAO " +
								  "		AND                FTMP.ID_CLIENTE IN (" + repeatParameterIn(idsClientes.size()) + ") " +
								  "		AND                (trunc(FTMP.DT_LIQUIDACAO) BETWEEN ? AND ? ) " +
							  "	   )) / COUNT(F.ID_FATURA) " +
							  ") * 100" +
						  ")", "PONTUAL");
		sql.addCriteriaValue(idsClientes.toArray());
		sql.addCriteriaValue(dtInicial);
		sql.addCriteriaValue(dtFinal);
		/** UM_QUINZE */
		this.getGenericSqlTemplateSubSelect(sqlSubSelect);
		sqlSubSelect.addProjection("COUNT(FTMP.ID_FATURA)");
		sqlSubSelect.addCustomCriteria("FTMP.DT_LIQUIDACAO - FTMP.DT_VENCIMENTO <= 15");
		getCriteriahistoricoFaturamento(sqlSubSelect, idsClientes, dtInicial, dtFinal, 2);
		sql.addProjection("(SUM((\n" + sqlSubSelect.getSql() + "\n)) / COUNT(F.ID_FATURA)) * 100", "UM_QUINZE");
		sql.addCriteriaValue(sqlSubSelect.getCriteria());
		sqlSubSelect = createSqlTemplate();
		
		/** DEZESSEIS_TRINTA */
		this.getGenericSqlTemplateSubSelect(sqlSubSelect);
		sqlSubSelect.addProjection("COUNT(FTMP.ID_FATURA)");
		sqlSubSelect.addCustomCriteria("FTMP.DT_LIQUIDACAO - FTMP.DT_VENCIMENTO BETWEEN 16 AND 30");
		getCriteriahistoricoFaturamento(sqlSubSelect, idsClientes, dtInicial, dtFinal, 2);
		sql.addProjection("(SUM((\n" + sqlSubSelect.getSql() + "\n)) / COUNT(F.ID_FATURA)) * 100", "DEZESSEIS_TRINTA");
		sql.addCriteriaValue(sqlSubSelect.getCriteria());
		sqlSubSelect = createSqlTemplate();
		
		/** TRINTA_SESSENTA */
		this.getGenericSqlTemplateSubSelect(sqlSubSelect);
		sqlSubSelect.addProjection("COUNT(FTMP.ID_FATURA)");
		sqlSubSelect.addCustomCriteria("FTMP.DT_LIQUIDACAO - FTMP.DT_VENCIMENTO BETWEEN 31 AND 60");
		getCriteriahistoricoFaturamento(sqlSubSelect, idsClientes, dtInicial, dtFinal, 2);
		sql.addProjection("(SUM((\n" + sqlSubSelect.getSql() + "\n)) / COUNT(F.ID_FATURA)) * 100", "TRINTA_SESSENTA");
		sql.addCriteriaValue(sqlSubSelect.getCriteria());
		sqlSubSelect = createSqlTemplate();
		
		/** MAIOR_SESSENTA */
		this.getGenericSqlTemplateSubSelect(sqlSubSelect);
		sqlSubSelect.addProjection("COUNT(FTMP.ID_FATURA)");
		sqlSubSelect.addCustomCriteria("FTMP.DT_LIQUIDACAO - FTMP.DT_VENCIMENTO > 60");
		getCriteriahistoricoFaturamento(sqlSubSelect, idsClientes, dtInicial, dtFinal, 2);
		sql.addProjection("(SUM((\n" + sqlSubSelect.getSql() + "\n)) / COUNT(F.ID_FATURA)) * 100", "MAIOR_SESSENTA");
		sql.addCriteriaValue(sqlSubSelect.getCriteria());
		sqlSubSelect = createSqlTemplate();
		
		/** ATRASO_MEDIO */
		this.getGenericSqlTemplateSubSelect(sqlSubSelect);
		sqlSubSelect.addProjection("COUNT(FTMP.ID_FATURA) ");
		getCriteriahistoricoFaturamento(sqlSubSelect, idsClientes, dtInicial, dtFinal, 2);
		sql.addProjection("(SUM((\n" + sqlSubSelect.getSql() + "\n)) / COUNT(F.ID_FATURA)) * 100", "ATRASO_MEDIO");
		sql.addCriteriaValue(sqlSubSelect.getCriteria());
		
		/** FROM */
		sql.addFrom("FATURA", "F");
		sql.addFrom("ITEM_FATURA", "IFA");
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DDSF");
		sql.addFrom("FILIAL", "FIL");
		sql.addFrom("PESSOA", "P");
		sql.addFrom("ENDERECO_PESSOA", "EP");
		sql.addFrom("MUNICIPIO", "M");
		sql.addFrom("UNIDADE_FEDERATIVA", "UF");
		sql.addFrom("TMP_DS_EXTRATO_CLIENTE", "TMP");
		
		sql.addJoin("DDSF.ID_DOCTO_SERVICO", "TMP.ID_DOCTO_SERVICO ");
		sql.addJoin("FIL.ID_FILIAL", "F.ID_FILIAL");
		sql.addJoin("IFA.ID_FATURA", "F.ID_FATURA");
		sql.addJoin("DDSF.ID_DEVEDOR_DOC_SERV_FAT", "IFA.ID_DEVEDOR_DOC_SERV_FAT");
		sql.addJoin("P.ID_PESSOA", "FIL.ID_FILIAL");
		sql.addJoin("EP.ID_PESSOA", "P.ID_PESSOA");
		sql.addJoin("M.ID_MUNICIPIO", "EP.ID_MUNICIPIO");
		sql.addJoin("UF.ID_UNIDADE_FEDERATIVA", "M.ID_UNIDADE_FEDERATIVA");		
		sql.addGroupBy("F.ID_MOEDA, UF.ID_PAIS");

		/** CRITÉRIA SQL PRINCIPAL */
		getCriteriahistoricoFaturamento(sql, idsClientes, dtInicial, dtFinal, 1);
		
		sql.addGroupBy("TO_CHAR(F.DT_VENCIMENTO, 'MM/YYYY')");
		sql.addGroupBy("TO_CHAR(F.DT_VENCIMENTO, 'YYYYMM')");
		
		sql.addOrderBy("COMPETENCIA_ORDER");
		
		return sql;
	}
	
	/**
	 * Método responsável por montar o SqlTemplate genérico do select HISTÓRICO FINANCEIRO PERÍODO
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 06/07/2006
	 *
	 * @param sql
	 * @param idsClientes
	 *
	 */
	private void getGenericSqlTemplateSubSelect(SqlTemplate sql){
		
		sql.addFrom("FATURA", "FTMP");
		
		sql.addCustomCriteria("TO_CHAR(F.DT_VENCIMENTO, 'MM/YYYY') = TO_CHAR(FTMP.DT_VENCIMENTO, 'MM/YYYY')");
		sql.addCustomCriteria("FTMP.DT_VENCIMENTO < FTMP.DT_LIQUIDACAO");
		
	}
	
	/**
	 * Método responsável por montar criteria genérico do HISTÓRICO FINANCEIRO PERÍODO
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 11/07/2006
	 *
	 * @param sql
	 * @param idsClientes
	 *
	 */
	private void getCriteriahistoricoFaturamento(SqlTemplate sql, Set idsClientes, YearMonthDay dtInicial, YearMonthDay dtFinal, int tpSql){
		
		/** SQL PRINCIPAL */
		if(tpSql == 1){
			SQLUtils.joinExpressionsWithComma(new ArrayList(idsClientes), sql, "F.ID_CLIENTE");
			sql.addCustomCriteria("( F.DT_LIQUIDACAO BETWEEN ? AND ? )");
		
			/** SUBSELECT */
		}else{
			SQLUtils.joinExpressionsWithComma(new ArrayList(idsClientes), sql, "FTMP.ID_CLIENTE");
			sql.addCustomCriteria("( FTMP.DT_LIQUIDACAO BETWEEN ? AND ? )");
		}
		sql.addCriteriaValue(dtInicial);
		sql.addCriteriaValue(dtFinal);
	}
	
	/**
	 * Método responsável por converter a moeda e adidionar o resultado no Map de acordo com o nmField passado
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 11/07/2006
	 *
	 * @param sql
	 * @param idsClientes
	 *
	 */
	private String repeatParameterIn(int size){
		
		StringBuffer sb = new StringBuffer();
		String retorno = null;
		
		for(int i = 0; i < size; i++){
			sb.append("?, ");
			
		}
		
		if(sb.length() > 0)
			retorno = sb.toString().substring(0, sb.toString().length() - 2);
		
		return retorno;
	}
	
	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 *
	 * @param idCliente
	 * @return
	 *
	 */
	private SqlTemplate getSqlDadosCliente(Long idCliente){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("pes.id_pessoa", "ID_CLIENTE");
		sql.addProjection("pes.nr_identificacao", "NR_IDENTIFICACAO");
		sql.addProjection("pes.tp_identificacao", "TP_IDENTIFICACAO");
		sql.addProjection("pes.nm_pessoa", "NM_PESSOA");
		sql.addProjection("ie.nr_inscricao_estadual", "INSCRICAO_ESTADUAL");
		sql.addProjection("filcli.sg_filial", "FILIAL_COBRANCA");
		sql.addProjection(PropertyVarcharI18nProjection.createProjection("LOG.DS_TIPO_LOGRADOURO_I")+" || ' ' || EP.DS_ENDERECO || ', ' || EP.NR_ENDERECO || DECODE(EP.DS_COMPLEMENTO, NULL, '', '/' || EP.DS_COMPLEMENTO)", "ENDERECO_CLIENTE");
		sql.addProjection("EP.NR_CEP", "CEP_CLIENTE");
		sql.addProjection("MUN.NM_MUNICIPIO", "MUNICIPIO_CLIENTE");
		sql.addProjection("(SELECT DECODE(TE.NR_DDI, NULL, '', '(' || TE.NR_DDI || ') ') || DECODE(TE.NR_DDD, NULL, '', '(' || TE.NR_DDD || ') ') || TE.NR_TELEFONE FROM TELEFONE_ENDERECO TE WHERE TE.ID_TELEFONE_ENDERECO = (SELECT MIN(TETMP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TETMP WHERE TETMP.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA))", "TELEFONE");

		sql.addFrom("cliente", "cli");
		sql.addFrom("filial", "filcli");
		sql.addFrom("pessoa", "pes");
		sql.addFrom("inscricao_estadual", "ie");
		sql.addFrom("endereco_pessoa", "EP");
		sql.addFrom("tipo_logradouro", "LOG");
		sql.addFrom("municipio", "MUN");

		sql.addJoin("pes.id_pessoa", "cli.id_cliente");
		sql.addJoin("filcli.id_filial", "cli.id_filial_cobranca");
		sql.addJoin("ie.id_pessoa", "pes.id_pessoa");
		sql.addCustomCriteria("EP.ID_ENDERECO_PESSOA = F_BUSCA_ENDERECO_PESSOA(cli.id_cliente, 'COB', ?)", JTDateTimeUtils.getDataAtual());
		sql.addJoin("EP.ID_TIPO_LOGRADOURO", "LOG.ID_TIPO_LOGRADOURO");
		sql.addJoin("EP.ID_MUNICIPIO", "MUN.ID_MUNICIPIO");

		sql.addCriteria("cli.id_cliente", "=", idCliente);
		sql.addCriteria("ie.bl_indicador_padrao", "=", "S");
		
		return sql;

	}
	
	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 *
	 * @param sql
	 * @return
	 *
	 */
	public Map mountCliente(SqlTemplate sql){
		final Map dadosCliente = new HashMap();
		
		getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowCallbackHandler(){

			public void processRow(ResultSet rs) throws SQLException {
				dadosCliente.put("ID_CLIENTE", Long.valueOf(rs.getLong("ID_CLIENTE")));
				dadosCliente.put("NR_IDENTIFICACAO", rs.getString("NR_IDENTIFICACAO"));
				dadosCliente.put("TP_IDENTIFICACAO", rs.getString("TP_IDENTIFICACAO"));
				dadosCliente.put("NM_PESSOA", rs.getString("NM_PESSOA"));
				dadosCliente.put("INSCRICAO_ESTADUAL", rs.getString("INSCRICAO_ESTADUAL"));
				dadosCliente.put("FILIAL_COBRANCA", rs.getString("FILIAL_COBRANCA"));
				dadosCliente.put("ENDERECO", rs.getString("ENDERECO_CLIENTE"));
				dadosCliente.put("CEP", rs.getString("CEP_CLIENTE"));
				dadosCliente.put("MUNICIPIO", rs.getString("MUNICIPIO_CLIENTE"));
				dadosCliente.put("TELEFONE", rs.getString("TELEFONE"));
			}
			
		});
		
		return dadosCliente;
		
	}
	
	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 *
	 * @param idsClientes
	 * @return
	 *
	 */
	public SqlTemplate getSqlOrderCliente(Set idsClientes){
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("pes.id_pessoa", "ID_CLIENTE");
		sql.addFrom("pessoa", "pes");
		sql.addCriteriaIn("pes.id_pessoa", idsClientes.toArray());
		sql.addOrderBy("pes.nm_pessoa, pes.nr_identificacao");
		
		return sql;
	}

	/**
	 * 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/03/2007
	 *
	 * @param sql
	 * @return
	 *
	 */
	public List<Long> mountIdsClientes(SqlTemplate sql, Set idsClientes){
		
		List ids = new ArrayList();
		
		if ( idsClientes != null && !idsClientes.isEmpty()){
			
			ids = getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowMapper(){
	
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					return Long.valueOf(rs.getLong("ID_CLIENTE"));
				}
				
			});
		
		}
	
		return ids;
	}
	
}