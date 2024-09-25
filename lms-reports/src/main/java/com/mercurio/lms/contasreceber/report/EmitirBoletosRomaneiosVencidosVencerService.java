package com.mercurio.lms.contasreceber.report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * @author Rafael Andrade de Oliveira
 * @since 03/04/2006
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirBoletosRomaneiosVencidosVencerService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirBoletosRomaneiosVencidosVencer.jasper" 
 */

public class EmitirBoletosRomaneiosVencidosVencerService extends ReportServiceSupport {

	DomainValueService domainValueService;


	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public JRReportDataObject execute(Map parameters) throws Exception {

		/* Busca SqlTemplate */
		SqlTemplate sql = getSql((TypedFlatMap) parameters);
		
		RowMapper rowMapper = new RowMapper() {
			
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Map map = new HashMap();
				
				map.put("FILIAL", rs.getString("FILIAL"));
				map.put("DATA_VENCIMENTO", rs.getDate("DATA_VENCIMENTO"));
				
				String identificacao = FormatUtils.formatIdentificacao(rs.getString("TP_IDENTIFICACAO"), rs.getString("NR_IDENTIFICACAO"));
				
				map.put("TELEFONE", rs.getString("TELEFONE"));
				
				map.put("VL_DESCONTO", rs.getBigDecimal("VL_DESCONTO"));
				map.put("VL_REC_PARCAIS", rs.getBigDecimal("VL_REC_PARCAIS"));
				map.put("VL_SALDO_DEVEDOR", rs.getBigDecimal("VL_SALDO_DEVEDOR"));

				map.put("CLIENTE", identificacao + " - " + rs.getString("CLIENTE"));
				
				map.put("ROMANEIO", rs.getString("SG_FILIAL_FAT") + " " + FormatUtils.completaDados(rs.getString("F_NR_FATURA"),"0",10,0,true));
				
				String nrBoleto = rs.getString("NRO_BOLETO");
				
				if (StringUtils.isNotBlank(nrBoleto)) {
					map.put("SITUACAO", domainValueService.findDomainValueByValue("DM_STATUS_BOLETO", rs.getString("TP_SITUACAO_BOLETO")).getDescription().getValue());
					map.put("NRO_BOLETO", FormatUtils.formataNrDocumento(nrBoleto.toString(), "BOL"));
				} else {
					String situacao = rs.getString("TP_SITUACAO_FATURA");
					
					if (situacao.equals("RE") && rs.getString("TP_FINALIDADE").equals("EC")) {
						// Em Redeco/Empresa de Cobran�a 
						situacao = domainValueService.findDomainValueByValue("DM_STATUS_ROMANEIO", "RE").getDescription().getValue();
						situacao += "/" + domainValueService.findDomainValueByValue("DM_FINALIDADE_REDECO", "EC").getDescription().getValue();
					} else {
						situacao = domainValueService.findDomainValueByValue("DM_STATUS_ROMANEIO", situacao).getDescription().getValue();
						map.put("NRO_BOLETO", null);
					}
					
					map.put("SITUACAO", situacao);
				}

				map.put("VALOR", rs.getDouble("VALOR"));
				
				Timestamp dhUltimaLigacao = rs.getTimestamp("ULTIMA_LIGACAO");
				
				if (dhUltimaLigacao != null) {
					map.put("ULTIMA_LIGACAO", JTFormatUtils.format(dhUltimaLigacao, JTFormatUtils.DEFAULT, JTFormatUtils.DATETIME));
				} else {
					map.put("ULTIMA_LIGACAO", null);
				}
				
				return map;
			}
			
		};
		
		/**
		 * Trata exce��o UncategorizedSQLException e adapta a mesma para ser
		 * uma InfrastructureException, a qual ser� tratada pelo framework.
		 */
		List rel;
		try {
			rel = getJdbcTemplate().query(sql.getSql(),
					JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()),
					rowMapper);
		} catch (UncategorizedSQLException e) {
			throw new InfrastructureException(e.getCause());
		}
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(rel);

		JRReportDataObject jr = createReportDataObject(jrMap, parameters);

		Map parametersReport = new HashMap();
		
		/* Tipo do relat�rio */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		/* Parametros de pesquisa */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary());
		/* Usuario emissor */
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        
        jr.setParameters(parametersReport);
		
		return jr;
	}
	
	private SqlTemplate getSql(TypedFlatMap tfm) {

		boolean filiaisComFatCentralizado = tfm.getBoolean("filiaisComFatCentralizado").booleanValue();
		String situacao = tfm.getString("situacao");
		
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection("f.sg_filial || ' - ' || pf.nm_fantasia", "FILIAL");
		sql.addProjection("fat.dt_vencimento", "DATA_VENCIMENTO");
		sql.addProjection("p.tp_identificacao", "TP_IDENTIFICACAO");
		sql.addProjection("p.nr_identificacao", "NR_IDENTIFICACAO");
		sql.addProjection("p.nm_pessoa", "CLIENTE");
		sql.addProjection("c.id_cliente", "ID_CLIENTE");
		
		sql.addProjection("ffat.sg_filial", "SG_FILIAL_FAT");
		sql.addProjection("f.sg_filial", "F_SG_FILIAL");
		sql.addProjection("fat.nr_fatura", "F_NR_FATURA");
		
		sql.addProjection("b.tp_situacao_boleto", "TP_SITUACAO_BOLETO");
		sql.addProjection("fat.tp_situacao_fatura", "TP_SITUACAO_FATURA");
		sql.addProjection("b.nr_boleto", "NRO_BOLETO"); 
		sql.addProjection("r.tp_finalidade", "TP_FINALIDADE");
		sql.addProjection("F_CONV_MOEDA(u.id_pais, fat.id_moeda, ?, ?, DECODE(fat.tp_situacao_fatura, 'LI', NVL(fat.dt_liquidacao, SYSDATE), SYSDATE), fat.vl_total)", "VALOR");
		
		/* 2683 */
		sql.addProjection("fat.vl_desconto" , "VL_DESCONTO");
		sql.addProjection("(select nvl(sum(rpp.vl_pagamento),0) from RELACAO_PAGTO_PARCIAL rpp where rpp.id_fatura = fat.id_fatura)" , "VL_REC_PARCAIS");
		sql.addProjection("CASE "+
						  	"WHEN fat.dt_liquidacao is null "+
						  	"THEN ((nvl(fat.vl_total,0) - nvl(fat.vl_desconto,0) - "+
						  	"(select nvl(sum(rpp.vl_pagamento),0) from RELACAO_PAGTO_PARCIAL rpp where rpp.id_fatura = fat.id_fatura))) "+
						  	"ELSE 0 "+
						  "END AS VL_SALDO_DEVEDOR");
		
		sql.addCriteriaValue(SessionUtils.getPaisSessao().getIdPais());
		sql.addCriteriaValue(tfm.getLong("moeda.idMoeda"));

		sql.addProjection("(SELECT DECODE(TE.NR_DDI, null, '', '(' || TE.NR_DDI || ')') || DECODE(TE.NR_DDD, null, '', '(' || TE.NR_DDD || ')') || TE.NR_TELEFONE FROM TELEFONE_ENDERECO TE WHERE TE.ID_TELEFONE_ENDERECO = (SELECT MIN(TETMP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TETMP WHERE TETMP.ID_ENDERECO_PESSOA = F_BUSCA_ENDERECO_PESSOA(P.ID_PESSOA, 'COB', fat.dt_emissao) and TETMP.tp_uso in ('FO', 'FF')))", "TELEFONE");
		
		sql.addProjection(new StringBuffer()
				.append("(")
				.append("select max(lc.dh_ligacao_cobranca) ")
				.append("from ligacao_cobranca lc ")
				.append("inner join item_ligacao il on il.id_ligacao_cobranca = lc.id_ligacao_cobranca ")
				.append("inner join item_cobranca ic on ic.id_item_cobranca = il.id_item_cobranca ")
				.append("where ic.id_fatura = fat.id_fatura")
				.append(")")
				.toString(), 
				"ULTIMA_LIGACAO");
		
		sql.addFrom("fatura fat "+
					" left join boleto b on b.id_fatura = fat.id_fatura and b.tp_situacao_boleto <> ? "+
					" left join item_redeco ir  on ir.id_fatura = fat.id_fatura and exists(select 1 from redeco where redeco.id_redeco = ir.id_redeco and redeco.tp_situacao_redeco <> ?) "+
					" left join redeco r on r.id_redeco = ir.id_redeco and r.tp_situacao_redeco <> ? "+
					" inner join cliente c on c.id_cliente = fat.id_cliente "+
					" inner join pessoa p on p.id_pessoa = c.id_cliente "+
					" inner join  filial f on f.id_filial = fat.id_filial_cobradora "+
					" inner join  filial ffat on  ffat.id_filial = fat.id_filial "+
					" inner join  pessoa pf on pf.id_pessoa = f.id_filial "+
					" inner join  endereco_pessoa ef on ef.id_endereco_pessoa = pf.id_endereco_pessoa "+
					" inner join  municipio m on m.id_municipio = ef.id_municipio "+
					" inner join  unidade_federativa u on u.id_unidade_federativa = m.id_unidade_federativa "+
					(StringUtils.isNotBlank(tfm.getString("tipoFilial")) ? " inner join historico_filial hf on  hf.id_filial = f.id_filial " : "")
		);
		
		sql.addCriteriaValue("CA");
		sql.addCriteriaValue("CA");
		sql.addCriteriaValue("CA");
		
		// N�o selecionar faturas inutilizadas
		sql.addCriteria("fat.tp_situacao_fatura", "<>", "IN");
		
		/* situacao */
		sql.addFilterSummary("situacao", domainValueService.findDomainValueByValue("DM_RELATORIO_VENCIDOS_VENCER", situacao).getDescription());
		
		if (filiaisComFatCentralizado) {
			
			sql.addFrom("centralizadora_faturamento", "cf");
			sql.addJoin("cf.id_filial_centralizadora", "fat.id_filial");
			
			sql.addJoin("cf.id_filial_centralizada", "fat.id_filial_cobradora");
			
			sql.addJoin("cf.tp_modal", "fat.tp_modal");
			sql.addJoin("cf.tp_abrangencia", "fat.tp_abrangencia");
		}
		
		sql.addFilterSummary("periodoInicial", tfm.getYearMonthDay("periodoInicial"));
		sql.addFilterSummary("periodoFinal", tfm.getYearMonthDay("periodoFinal"));
		
		if (situacao.equals("LI")) { // Liquidados
			
			sql.addCriteria("fat.tp_situacao_fatura", "<>", "CA");
			sql.addCriteria("fat.dt_liquidacao", ">=", tfm.getYearMonthDay("periodoInicial"));
			sql.addCriteria("fat.dt_liquidacao", "<=", tfm.getYearMonthDay("periodoFinal"));
			
		} else if (situacao.equals("VV")) { // Vencidos / A vencer
			
			sql.addCustomCriteria("fat.dt_liquidacao is null");
			
			sql.addCriteria("fat.dt_vencimento", ">=", tfm.getYearMonthDay("periodoInicial"));
			sql.addCriteria("fat.dt_vencimento", "<=", tfm.getYearMonthDay("periodoFinal"));
			sql.addCriteria("fat.tp_situacao_fatura", "<>", "CA");
			sql.addCustomCriteria("((fat.tp_situacao_fatura = ? and r.tp_finalidade IN (?, ?)) or (fat.tp_situacao_fatura <> ?))");
			sql.addCriteriaValue("RE");
			sql.addCriteriaValue("CF");
			sql.addCriteriaValue("EC");
			sql.addCriteriaValue("RE");
			
		} else if (situacao.equals("EM")) { // Emitidos
			
			sql.addCriteria("fat.tp_situacao_fatura", "<>", "CA");
			sql.addCriteria("fat.dt_emissao", ">=", tfm.getYearMonthDay("periodoInicial"));
			sql.addCriteria("fat.dt_emissao", "<=", tfm.getYearMonthDay("periodoFinal"));
			
		} else if (situacao.equals("EP")) { // Em Protesto
			
			sql.addCriteria("fat.tp_situacao_fatura", "<>", "CA");
			sql.addCriteria("b.tp_situacao_boleto", "=", "BP");
			
		} else if (situacao.equals("CA")) { // Cancelados
			
			sql.addCriteria("fat.tp_situacao_fatura", "=", "CA");
			sql.addCriteria("fat.dt_emissao", ">=", tfm.getYearMonthDay("periodoInicial"));
			sql.addCriteria("fat.dt_emissao", "<=", tfm.getYearMonthDay("periodoFinal"));
			
		} else if (situacao.equals("PE")) { // Posicao em
			
			sql.addCriteria("fat.dt_emissao", ">=", tfm.getYearMonthDay("periodoInicial"));
			sql.addCriteria("fat.dt_emissao", "<=", tfm.getYearMonthDay("periodoFinal"));

			sql.addCustomCriteria("(fat.dt_liquidacao is null or fat.dt_liquidacao > ?)");
			sql.addCriteriaValue(tfm.getYearMonthDay("periodoFinal"));

			sql.addCriteria("fat.tp_situacao_fatura", "<>", "CA");
		}
		
		/* modal */ 
		if (!tfm.getDomainValue("modal").getValue().equals("")) {
			sql.addCriteria("fat.tp_modal", "=", tfm.getDomainValue("modal").getValue());			
			sql.addFilterSummary("modal", domainValueService.findDomainValueByValue("DM_MODAL", tfm.getDomainValue("modal").getValue()).getDescription());
		}
		
		/* abrangencia */ 
		if (!tfm.getDomainValue("abrangencia").getValue().equals("")) {
			sql.addCriteria("fat.tp_abrangencia", "=", tfm.getDomainValue("abrangencia").getValue());			
			sql.addFilterSummary("abrangencia", domainValueService.findDomainValueByValue("DM_ABRANGENCIA", tfm.getDomainValue("abrangencia").getValue()).getDescription());
		}
		
		/* cendente */ 
		if (tfm.getLong("cedente.idCedente") != null) {
			
			sql.addCriteria("fat.id_cedente", "=", tfm.getLong("cedente.idCedente"));			
			sql.addFilterSummary("banco", tfm.getString("cedente.dsCedente"));
		}

		/* checkbox: Filiais com fat. centralizado */
		if (filiaisComFatCentralizado) {
		
			/* i18n para o valor de "Sim" */
			String sim = domainValueService.findDomainValueDescription("DM_SIM_NAO", "S");
			sql.addFilterSummary("filiaisComFatCentralizado", sim);
		}
		
		if (StringUtils.isNotBlank(tfm.getString("tipoFilial"))) {
			sql.addCriteria("hf.tp_filial", "=", tfm.getString("tipoFilial"));
			sql.addFilterSummary("tipoFilial", tfm.getString("dsTipoFilial"));
			
				sql.addCriteria("hf.dt_real_operacao_inicial", "<=", JTDateTimeUtils.getDataAtual());
				sql.addCriteria("hf.dt_real_operacao_final", ">=", JTDateTimeUtils.getDataAtual());
		}
		
		// Ou considera 0 ou considera filial
		if (tfm.getLong("regional.idRegional") != null) {
			sql.addCustomCriteria(new StringBuffer()
					.append("f.id_filial IN (")
					.append("  select rf.id_filial")
					.append("  from regional_filial rf ")
					.append("  where rf.id_regional = " + tfm.getLong("regional.idRegional"))
					.append("  and rf.dt_vigencia_inicial <= ? and rf.dt_vigencia_final >= ? ")
					.append(")")
					.toString()
			);
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addCriteriaValue(JTDateTimeUtils.getDataAtual());
			sql.addFilterSummary("regional", tfm.getString("sgDsRegional"));
		} else {

			if (tfm.getLong("filial.idFilial") != null) {
				if (filiaisComFatCentralizado) {
					sql.addCriteria("ffat.id_filial", "=", tfm.getLong("filial.idFilial"));
				} else {
					sql.addCriteria("f.id_filial", "=", tfm.getLong("filial.idFilial"));
				}
				sql.addFilterSummary("filial", tfm.getString("sgFilial") + " - " + tfm.getString("filial.pessoa.nmFantasia"));
			}
		}
		
		// moeda
		sql.addFilterSummary("moedaExibicao", tfm.getString("moeda.dsSimbolo"));
		
		// data de emiss�o at�
		YearMonthDay dtEmissao = tfm.getYearMonthDay("dtEmissaoAte"); 
		if(dtEmissao != null){
			sql.addCriteria("fat.dt_emissao","<=",JTDateTimeUtils.createWithMaxTime(dtEmissao));
		}
		
		/* CLIENTE */
		sql.addCriteria("p.tp_identificacao", "=", tfm.getString("tpIdentificacao"));			
		sql.addFilterSummary("tipoIdentificacao", tfm.getString("tpIdentificacao"));
		
		if (StringUtils.isNotBlank(tfm.getString("nrIdentificacao"))){
			sql.addCustomCriteria("p.nr_identificacao like '" + tfm.getString("nrIdentificacao") + "%'");
		}
		
		sql.addFilterSummary("identificacaoCliente", tfm.getString("nrIdentificacao"));

		sql.addCriteria("c.id_cliente", "=", tfm.getString("cliente.idCliente"));
		sql.addFilterSummary("cliente", tfm.getString("cliente.pessoa.nmPessoa"));
		
		if (tfm.getDomainValue("tpDocumentoServico") != null
				&& !"".equals(tfm.getDomainValue("tpDocumentoServico").getValue())) {
			String tpDocumentoServico = tfm.getDomainValue("tpDocumentoServico").getValue();

			sql.addCustomCriteria(" exists ( select 1 from ITEM_FATURA ITF "
					+ "         inner join DEVEDOR_DOC_SERV_FAT DDSF on DDSF.ID_DEVEDOR_DOC_SERV_FAT = ITF.ID_DEVEDOR_DOC_SERV_FAT "
					+ "         inner join DOCTO_SERVICO DS on DS.ID_DOCTO_SERVICO = DDSF.ID_DOCTO_SERVICO "
					+ "         where ITF.ID_FATURA = fat.ID_FATURA "
					+ "			AND DS.TP_DOCUMENTO_SERVICO = '" + tpDocumentoServico + "' )");

			String tpDocumentoServicoDominio = domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", tpDocumentoServico);
			sql.addFilterSummary("tipoDocumentoServico", tpDocumentoServicoDominio);
		}

		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("fat.dt_vencimento");
		sql.addOrderBy("p.nm_pessoa");
		sql.addOrderBy("fat.nr_fatura");
		sql.addOrderBy("b.nr_boleto");
		
		return sql;
	}
	
}