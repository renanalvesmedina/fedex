package com.mercurio.lms.contasreceber.report;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;
import org.springframework.jdbc.core.RowMapper;

import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * @author Rafael Andrade de Oliveira
 * @since 10/04/2006
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.emitirRelacaoDocumentosFaturadosService"
 * @spring.property name="reportName" value="com/mercurio/lms/contasreceber/report/emitirRelacaoDocumentosFaturados.jasper" 
 */

public class EmitirRelacaoDocumentosFaturadosService extends ReportServiceSupport {
	
	private ConversaoMoedaService conversaoMoedaService;
	
	private DomainValueService domainValueService;
	
	private PaisService paisService;

	public JRReportDataObject execute(Map parameters) {

		TypedFlatMap tfm = (TypedFlatMap) parameters;
		
		/* Busca SqlTemplate */
		SqlTemplate sql = getSql(tfm);
		
		final Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
		final Long idMoedaDestino = Long.valueOf(parameters.get("moeda.idMoeda").toString());
		
		List rel = getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(), sql.getCriteria()), new RowMapper() {
			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				Map map = new HashMap();
				
				Long idMoeda = Long.valueOf(rs.getLong("MOEDA"));
				Long idPais = Long.valueOf(rs.getLong("PAIS"));
				
				Date dtEmissaoBoleto = rs.getDate("DATAEMISSAOBOLETO");
				
				YearMonthDay dtCotacao = new YearMonthDay();

				if (dtEmissaoBoleto != null) {
					dtCotacao = new YearMonthDay(dtEmissaoBoleto);
				} else {
					dtCotacao = JTDateTimeUtils.getDataAtual();
				}
				
				BigDecimal vlVencer = rs.getBigDecimal("VALORVENCER");
				BigDecimal vlVencidas = rs.getBigDecimal("VALORVENCIDAS");
				
				if (vlVencer.longValue() > 0 || vlVencidas.longValue() > 0){
					
					String sgPais = paisService.findSgPaisByIdPessoa(rs.getLong("FILIAL_FATURA"));
					
					if (vlVencer.longValue() > 0){
						if (sgPais.equals("BR")) {
							vlVencer = conversaoMoedaService.findConversaoMoeda(idPais, idMoeda, idPaisDestino, idMoedaDestino, dtCotacao, vlVencer);						
						}
					}
					
					if (vlVencidas.longValue() > 0){
						if (sgPais.equals("BR")) {
							vlVencidas = conversaoMoedaService.findConversaoMoeda(idPais, idMoeda, idPaisDestino, idMoedaDestino, dtCotacao, vlVencidas);						
						}
					}				
				}
				
				map.put("VALORVENCER", new Double(vlVencer.doubleValue()));
				map.put("VALORVENCIDAS", new Double(vlVencidas.doubleValue()));

				map.put("VALORFATURAR", new Double(rs.getDouble("VALORFATURAR")));
				
				if (StringUtils.isNotBlank(rs.getString("FATURA"))){
					map.put("FATURA", FormatUtils.completaDados(Long.valueOf(rs.getLong("FATURA")), "0", 10, 0, true));
				} else {
					map.put("FATURA", "");
				}
				
				map.put("SIGLAFILIAL", rs.getString("SIGLAFILIAL"));
				map.put("NOMEFILIAL", rs.getString("NOMEFILIAL"));
				map.put("TP_IDENTIFICACAO_CLIENTE", rs.getString("TP_IDENTIFICACAO_CLIENTE"));
				map.put("NR_IDENTIFICACAO_CLIENTE", rs.getString("NR_IDENTIFICACAO_CLIENTE"));
				map.put("NOMECLIENTE", rs.getString("NOMECLIENTE"));
				map.put("CONHECIMENTO", rs.getString("CONHECIMENTO"));

				map.put("DATAEMISSAO", rs.getDate("DATAEMISSAO"));
				map.put("VENCIMENTO", rs.getDate("VENCIMENTO"));
				
				String tpFatura = "";
				if (rs.getString("TIPO_DOCUMENTO")!=null) {
					tpFatura = domainValueService.findDomainValueByValue("DM_TIPO_ROMANEIO", rs.getString("TIPO_DOCUMENTO")).getDescription().toString();
				}
				map.put("TIPO_DOCUMENTO", tpFatura);
								
				return map;
				
			}
		});
		
		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(rel);

		JRReportDataObject jr = createReportDataObject(jrMap, parameters);
		
		Map parametersReport = new HashMap();
		
		/* Tipo do relat�rio */
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, parameters.get("tpFormatoRelatorio"));
		/* Parametros de pesquisa */
		parametersReport.put("parametrosPesquisa", sql.getFilterSummary().trim());
		/* Usuario emissor */
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		
        jr.setParameters(parametersReport);
		
		return jr;
	}

	private SqlTemplate getSql(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();
		
		sql.setDistinct();
		
		sql.addProjection("f.sg_filial", "SIGLAFILIAL");
		sql.addProjection("pf.nm_fantasia", "NOMEFILIAL");
		sql.addProjection("pc.tp_identificacao", "TP_IDENTIFICACAO_CLIENTE");
		sql.addProjection("pc.nr_identificacao", "NR_IDENTIFICACAO_CLIENTE");
		sql.addProjection("pc.nm_pessoa", "NOMECLIENTE");
		sql.addProjection("cti.sg_pais || '.' || cti.nr_permisso || '.' || cti.nr_crt", "CONHECIMENTO");
		sql.addProjection("CAST(ds.dh_emissao AS DATE)", "DATAEMISSAO");
		sql.addProjection("b.dt_emissao", "DATAEMISSAOBOLETO");
		sql.addProjection("fat.tp_fatura", "TIPO_DOCUMENTO");
		sql.addProjection("fat.nr_fatura", "FATURA");
		sql.addProjection("(CASE WHEN fat.id_fatura IS NULL THEN ddsf.vl_devido ELSE 0 END)", "VALORFATURAR");
		sql.addProjection("(CASE WHEN (fat.id_fatura IS NOT NULL and fat.dt_vencimento >= SYSDATE) THEN ddsf.vl_devido ELSE 0 END)", "VALORVENCER");
		sql.addProjection("(CASE WHEN (fat.id_fatura IS NOT NULL and fat.dt_vencimento < SYSDATE) THEN ddsf.vl_devido ELSE 0 END)", "VALORVENCIDAS");
		sql.addProjection("NVL(fat.dt_vencimento, b.dt_vencimento)", "VENCIMENTO");
		sql.addProjection("NVL(fat.id_moeda, ds.id_moeda)", "MOEDA");
		sql.addProjection("u.id_pais", "PAIS");
		
		sql.addProjection("cti.sg_pais");
		sql.addProjection("cti.nr_permisso");
		sql.addProjection("cti.nr_crt");
		
		sql.addProjection("fat.id_filial", "FILIAL_FATURA");

		sql.addFrom("docto_servico", "ds");
		
		sql.addFrom("cto_internacional", "cti");
		sql.addJoin("cti.id_cto_internacional", "ds.id_docto_servico");
		
		sql.addFrom("devedor_doc_serv_fat", "ddsf");
		sql.addJoin("ddsf.id_docto_servico", "ds.id_docto_servico");

		sql.addFrom("desconto", "des");
		sql.addJoin("des.id_devedor_doc_serv_fat(+)", "ddsf.id_devedor_doc_serv_fat");
		
		sql.addFrom("filial", "f");
		sql.addJoin("f.id_filial", "ddsf.id_filial");
		
		sql.addFrom("pessoa", "pf");
		sql.addJoin("pf.id_pessoa", "f.id_filial");
		
		sql.addFrom("endereco_pessoa", "ef");
		sql.addJoin("ef.id_endereco_pessoa", "pf.id_endereco_pessoa");

		sql.addFrom("municipio", "m");
		sql.addJoin("m.id_municipio", "ef.id_municipio");

		sql.addFrom("unidade_federativa", "u");
		sql.addJoin("u.id_unidade_federativa", "m.id_unidade_federativa");
		
		sql.addFrom("cliente", "c");
		sql.addJoin("c.id_cliente", "ddsf.id_cliente");
		
		sql.addFrom("pessoa", "pc");
		sql.addJoin("pc.id_pessoa", "c.id_cliente"); 
		
		sql.addFrom("item_fatura", "itf");
		sql.addJoin("itf.id_devedor_doc_serv_fat(+)", "ddsf.id_devedor_doc_serv_fat"); 
		
		sql.addFrom("fatura", "fat");
		sql.addJoin("fat.id_fatura(+)", "itf.id_fatura");
		
		sql.addFrom("boleto", "b");
		sql.addJoin("b.id_fatura(+)", "fat.id_fatura");
		
		if (tfm.getLong("filialFaturamento.idFilial") != null) {
			sql.addCriteria("fat.id_filial", "=", tfm.getLong("filialFaturamento.idFilial"));
			sql.addFilterSummary("filialFaturamento", tfm.getString("sgFilialFaturamento") + " - " + tfm.getString("filialFaturamento.pessoa.nmFantasia"));
		}
		
		if (tfm.getLong("filialCobranca.idFilial") != null) {
			sql.addCriteria("ddsf.id_filial", "=", tfm.getLong("filialCobranca.idFilial"));
			sql.addFilterSummary("filialCobranca", tfm.getString("sgFilialCobranca") + " - " + tfm.getString("filialCobranca.pessoa.nmFantasia"));
		}
		
		if (StringUtils.isNotBlank(tfm.getDomainValue("tipo").getValue())) {
			if (tfm.getDomainValue("tipo").getValue().equals("AF")) { // a faturar
				sql.addCustomCriteria(new StringBuffer()
						.append(" NOT EXISTS (")
						.append("   SELECT 		1 ")
						.append("   FROM 		devedor_doc_serv_fat ddsf2, ")
						.append("   			item_fatura itf2, ") 
						.append("   			fatura fat2 ") 
						.append("   WHERE 		itf2.id_devedor_doc_serv_fat = ddsf2.id_devedor_doc_serv_fat ")
						.append("	AND			fat2.id_fatura = itf2.id_fatura ")
						.append("	AND			fat2.tp_situacao_fatura <> 'CA' ")
						.append("	AND			ddsf2.id_docto_servico = ds.id_docto_servico ")
						.append(" ) ").toString()
				);
			} else if (tfm.getDomainValue("tipo").getValue().equals("AV")) { // a vencer
				sql.addCriteria("fat.dt_vencimento", ">=", JTDateTimeUtils.getDataAtual());
			} else if (tfm.getDomainValue("tipo").getValue().equals("VE")) { // vencidos
				sql.addCriteria("fat.dt_vencimento", "<", JTDateTimeUtils.getDataAtual());
			}
			sql.addFilterSummary("tipo", domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_PENDENTE", tfm.getDomainValue("tipo").getValue()).getDescription());
		}
		
		
		
		sql.addCustomCriteria("cti.TP_SITUACAO_CRT != 'C'");
		
		sql.addCustomCriteria("NVL(des.TP_SITUACAO_APROVACAO, 'A') = 'A'");
		
		sql.addCustomCriteria("NVL(fat.TP_SITUACAO_FATURA, 'XX') NOT IN ('CA', 'LI', 'IN')");
		
		sql.addCriteria("cast(ds.dh_emissao as date)", ">=", tfm.getYearMonthDay("dtEmissaoInicial"));
		sql.addFilterSummary("dataEmissaoInicial", tfm.getYearMonthDay("dtEmissaoInicial"));
		
		sql.addCriteria("cast(ds.dh_emissao as date)", "<=", tfm.getYearMonthDay("dtEmissaoFinal"));
		sql.addFilterSummary("dataEmissaoFinal", tfm.getYearMonthDay("dtEmissaoFinal"));
		
		sql.addCriteria("cast(ds.dh_emissao as date)", "<=", tfm.getYearMonthDay("dtEmissaoFinal"));
		
		if (tfm.getDomainValue("situacao").getValue().equals("L")) { // liberados 
			sql.addFrom("evento_documento_servico", "eds");
			sql.addJoin("eds.id_docto_servico", "ds.id_docto_servico"); 
			
			sql.addFrom("evento", "e");
			sql.addJoin("e.id_evento", "eds.id_evento");
			sql.addCriteria("e.cd_evento", "=", "105");
		}

		sql.addFilterSummary("situacao", domainValueService.findDomainValueByValue("DM_SITUACAO_DOCUMENTO_PENDENTE", tfm.getDomainValue("situacao").getValue()).getDescription()); 
				
		sql.addCriteria("ddsf.id_cliente", "=", tfm.getLong("cliente.idCliente"));

		// moeda
		sql.addFilterSummary("moedaExibicao", tfm.getString("moeda.dsSimbolo"));

		sql.addFilterSummary("cliente", tfm.getString("cliente.pessoa.nmPessoa"));
		
		sql.addOrderBy("f.sg_filial");
		sql.addOrderBy("pc.nm_pessoa");
		sql.addOrderBy("cti.sg_pais");
		sql.addOrderBy("cti.nr_permisso");
		sql.addOrderBy("cti.nr_crt");
		
		return sql;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
}