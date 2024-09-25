package com.mercurio.lms.fretecarreteiroviagem.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.MoedaUtils;

/**
 * @author Felipe Ferreira
 * 
 * @spring.bean id="lms.fretecarreteiroviagem.emitirReciboService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteiroviagem/report/emitirRecibo.jasper"
 */
public class EmitirReciboService extends ReportServiceSupport {

	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ConfiguracoesFacade configuracoesFacade;
	private MoedaService moedaService;
	private DomainService domainService;
	private ParametroGeralService parametroGeralService;
	
	public void configReportDomains(ReportDomainConfig config) {
		super.configReportDomains(config);
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap map = (TypedFlatMap)parameters;		
		SqlTemplate sql = createSqlTemplate(map);

		final Boolean blAdiantamento = MapUtils.getBoolean(parameters, "blAdiantamento", false);

		final List dados = new LinkedList();

		getJdbcTemplate().query(sql.getSql(true), 
				JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), 
				new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					Map linha = new HashMap();

					linha.put("ID_RECIBO_FRETE_CARRETEIRO",Long.valueOf(rs.getLong("ID_RECIBO_FRETE_CARRETEIRO")));
					linha.put("NR_IDENT_EMPRESA",rs.getString("NR_IDENT_EMPRESA"));
					linha.put("TP_IDENT_EMPRESA",rs.getString("TP_IDENT_EMPRESA"));
					linha.put("NM_EMPRESA",rs.getString("NM_EMPRESA"));
					linha.put("TP_IDENT_FILIAL",rs.getString("TP_IDENT_FILIAL"));
					linha.put("NR_IDENT_FILIAL",rs.getString("NR_IDENT_FILIAL"));
					linha.put("NM_FILIAL",rs.getString("NM_FILIAL"));
					linha.put("SG_FILIAL",rs.getString("SG_FILIAL"));
					linha.put("NR_RECIBO_FRETE_CARRETEIRO",Integer.valueOf(rs.getInt("NR_RECIBO_FRETE_CARRETEIRO")));
					linha.put("NR_CONTROLE_CARGA",Integer.valueOf(rs.getInt("NR_CONTROLE_CARGA")));
					linha.put("DS_ROTA",rs.getString("DS_ROTA"));
					linha.put("ID_PROPRIETARIO",Long.valueOf(rs.getLong("ID_PROPRIETARIO")));
					linha.put("TP_PESSOA_PROP",rs.getString("TP_PESSOA_PROP"));
					linha.put("NR_IDENT_PROP",rs.getString("NR_IDENT_PROP"));
					linha.put("TP_IDENT_PROP",rs.getString("TP_IDENT_PROP"));
					linha.put("NM_PROPRIETARIO",rs.getString("NM_PROPRIETARIO"));
					linha.put("NR_PIS",rs.getString("NR_PIS"));
					linha.put("NR_IDENT_MOT",rs.getString("NR_IDENT_MOT"));
					linha.put("TP_IDENT_MOT",rs.getString("TP_IDENT_MOT"));
					linha.put("NM_MOTORISTA",rs.getString("NM_MOTORISTA"));
					linha.put("NR_IDENT_MT",rs.getString("NR_IDENT_MT"));
					linha.put("NR_FROTA",rs.getString("NR_FROTA"));
					linha.put("DS_MODELO_MEIO_TRANSPORTE",rs.getString("DS_MODELO_MEIO_TRANSPORTE"));
					linha.put("DS_MARCA_MEIO_TRANSPORTE",rs.getString("DS_MARCA_MEIO_TRANSPORTE"));
					linha.put("DS_TIPO_MEIO_TRANSPORTE",rs.getString("DS_TIPO_MEIO_TRANSPORTE"));
					linha.put("NR_ANO_FABRICACAO",Integer.valueOf(rs.getInt("NR_ANO_FABRICACAO")));
					linha.put("NM_MUNICIPIO_MT",rs.getString("NM_MUNICIPIO_MT"));
					linha.put("CAPACIDADE",rs.getBigDecimal("CAPACIDADE"));
					linha.put("NR_IDENT_BENEF",rs.getString("NR_IDENT_BENEF"));
					linha.put("TP_IDENT_BENEF",rs.getString("TP_IDENT_BENEF"));
					linha.put("NM_BENEFICIARIO",rs.getString("NM_BENEFICIARIO"));
					linha.put("NR_BANCO",Integer.valueOf(rs.getInt("NR_BANCO")));
					linha.put("NR_AGENCIA_BANCARIA",Integer.valueOf(rs.getInt("NR_AGENCIA_BANCARIA")));
					linha.put("NR_DIGITO",rs.getString("NR_DIGITO"));
					linha.put("NR_CONTA_BANCARIA",rs.getString("NR_CONTA_BANCARIA"));
					linha.put("DV_CONTA_BANCARIA",rs.getString("DV_CONTA_BANCARIA"));

					long idComplementado = rs.getLong("ID_RECIBO_COMPLEMENTADO");
					Long idReciboComplementado = idComplementado == 0 ? null : Long.valueOf(idComplementado);
					linha.put("ID_RECIBO_COMPLEMENTADO",idReciboComplementado);

					BigDecimal vlPremio = rs.getBigDecimal("VL_PREMIO");
					BigDecimal vlDiaria = rs.getBigDecimal("VL_DIARIA");
					BigDecimal vlDesconto = rs.getBigDecimal("VL_DESCONTO");
					if (BigDecimalUtils.hasValue(vlPremio))
						linha.put("VL_PREMIO",vlPremio);
					if (BigDecimalUtils.hasValue(vlDiaria))
						linha.put("VL_DIARIA",vlDiaria);
					if (BigDecimalUtils.hasValue(vlDesconto))
						linha.put("VL_DESCONTO",vlDesconto);
					
					BigDecimal vlBruto = rs.getBigDecimal("VL_BRUTO");
					String blAdiantemento = rs.getString("BL_ADIANTAMENTO");
					if (blAdiantemento.equals("S")) {
						linha.put("VL_ADIANTAMENTO",vlBruto);
						linha.put("VL_TOTAL",new BigDecimal(0));
					} else {
						linha.put("VL_BRUTO",vlBruto);
						
						BigDecimal vlTotal = new BigDecimal(0);
						vlTotal = vlTotal.add(vlBruto);
						
						// imprime valor de adiantamento somente se não é complementar:
						if (idReciboComplementado == null) {
							BigDecimal vlAdiantamento = rs.getBigDecimal("VL_ADIANTAMENTO");
							if (BigDecimalUtils.hasValue(vlAdiantamento)) {
								linha.put("VL_ADIANTAMENTO",vlAdiantamento);
								vlTotal = vlTotal.subtract(vlAdiantamento);
							}
						}

						if (vlPremio != null)
							vlTotal = vlTotal.add(vlPremio);
						if (vlDiaria != null)
							vlTotal = vlTotal.add(vlDiaria);
						if (vlDesconto != null)
							vlTotal = vlTotal.subtract(vlDesconto);

						linha.put("VL_TOTAL",vlTotal);
					}

					BigDecimal vlPostoPassagem = rs.getBigDecimal("VL_POSTO_PASSAGEM");
					if (BigDecimalUtils.hasValue(vlPostoPassagem))
						linha.put("VL_POSTO_PASSAGEM",vlPostoPassagem);

					BigDecimal vlInss = rs.getBigDecimal("VL_INSS");
					if (BigDecimalUtils.hasValue(vlInss))
						linha.put("VL_INSS",vlInss);

					BigDecimal vlIrrf = rs.getBigDecimal("VL_IRRF");
					if (BigDecimalUtils.hasValue(vlIrrf))
						linha.put("VL_IRRF",vlIrrf);

					linha.put("VL_LIQUIDO",rs.getBigDecimal("VL_LIQUIDO"));
					linha.put("VL_SALARIO_CONTRIBUICAO",rs.getBigDecimal("VL_SALARIO_CONTRIBUICAO"));
					linha.put("PC_ALIQUOTA_INSS",rs.getBigDecimal("PC_ALIQUOTA_INSS"));
					linha.put("VL_APURADO",rs.getBigDecimal("VL_APURADO"));
					linha.put("VL_OUTRAS_FONTES",rs.getBigDecimal("VL_OUTRAS_FONTES"));

					BigDecimal vlResultadoApurado = rs.getBigDecimal("RESULTADO_APURADO");
					if (vlResultadoApurado != null && CompareUtils.lt(vlResultadoApurado,BigDecimal.ZERO)) {
						vlResultadoApurado = BigDecimal.ZERO;
					}
					linha.put("RESULTADO_APURADO",vlResultadoApurado);

					linha.put("SG_FILIAL_ORIGEM",rs.getString("SG_FILIAL_ORIGEM"));
					linha.put("NM_FANTASIA_ORIGEM",rs.getString("NM_FANTASIA_ORIGEM"));
					linha.put("SG_FILIAL_DESTINO",rs.getString("SG_FILIAL_DESTINO"));
					linha.put("NM_FANTASIA_DESTINO",rs.getString("NM_FANTASIA_DESTINO"));
					linha.put("SG_MOEDA",rs.getString("SG_MOEDA"));
					linha.put("DS_SIMBOLO",rs.getString("DS_SIMBOLO"));
					linha.put("OB_RECIBO_FRETE_CARRETEIRO",rs.getString("OB_RECIBO_FRETE_CARRETEIRO"));
					linha.put("NR_CARTAO",rs.getString("NR_CARTAO"));

					Long idProprietario = Long.valueOf(rs.getLong("ID_PROPRIETARIO"));
					Long idMotorista = Long.valueOf(rs.getLong("ID_MOTORISTA"));
					linha.put("DS_ENDERECO_PROP",getDsEnderecoPadrao(idProprietario));
					linha.put("DS_ENDERECO_MOT",getDsEnderecoPadrao(idMotorista));
					linha.put("DS_TELEFONE_PROP",getDsTelefonePadrao(idProprietario));
					linha.put("DS_TELEFONE_MOT",getDsTelefonePadrao(idMotorista));

					Domain dmTipoIdentificacao = domainService.findByName("DM_TIPO_IDENTIFICACAO");
					if (idMotorista != null && idMotorista != 0) {
					linha.put("identMot",dmTipoIdentificacao.findDomainValueByValue(rs.getString("TP_IDENT_MOT")).getDescription().getValue());
					}
					linha.put("identProp",dmTipoIdentificacao.findDomainValueByValue(rs.getString("TP_IDENT_PROP")).getDescription().getValue());

					String tpBenef = rs.getString("TP_IDENT_BENEF");
					if (StringUtils.isNotBlank(tpBenef)) {
						linha.put("identBenef",dmTipoIdentificacao.findDomainValueByValue(tpBenef).getDescription().getValue());
					}	

					linha.put("DH_EMISSAO",rs.getString("DH_EMISSAO"));
					linha.put("OBSERVACAO",getObservacao(rs));
					linha.put("DS_ACORDO_ENTREGA_VIAGEM", getParametroGeralService().findByNomeParametro("DS_ACORDO_ENTREGA_VIAGEM", false).getDsConteudo());

					Map linha2 = new HashMap();
					linha2.putAll(linha);
					Map linha3 = new HashMap();
					linha3.putAll(linha);

					linha.put("ID_PAGINA",Long.valueOf(1));
					dados.add(linha);
					linha2.put("ID_PAGINA",Long.valueOf(2));
					dados.add(linha2);
					if(!blAdiantamento) {
						linha3.put("ID_PAGINA",Long.valueOf(3));
						dados.add(linha3);
					}
				}
				return null;
			}}
		);

		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(dados);

		Map parametersReport = new HashMap();
		parametersReport.put("dataAtual",JTFormatUtils.format(JTDateTimeUtils.getDataAtual(),JTFormatUtils.SHORT));
		parametersReport.put("LB_INSS",configuracoesFacade.getMensagem("lbInss"));
		parametersReport.put("blReemissao",parameters.get("blReemissao"));
		parametersReport.put("blAdiantamento",parameters.get("blAdiantamento"));
		return createReportDataObject(ds, parametersReport);
	}		

	private SqlTemplate createSqlTemplate(TypedFlatMap criteria) {
		SqlTemplate sql = createSqlTemplate();
		
		sql.addProjection("RFC.ID_RECIBO_FRETE_CARRETEIRO");
		sql.addProjection("P_EMPRESA.NR_IDENTIFICACAO","NR_IDENT_EMPRESA");
		sql.addProjection("P_EMPRESA.TP_IDENTIFICACAO","TP_IDENT_EMPRESA");
		sql.addProjection("P_EMPRESA.NM_PESSOA","NM_EMPRESA");
		sql.addProjection("P_FILIAL.TP_IDENTIFICACAO","TP_IDENT_FILIAL");
		sql.addProjection("P_FILIAL.NR_IDENTIFICACAO","NR_IDENT_FILIAL");
		sql.addProjection("P_FILIAL.NM_FANTASIA","NM_FILIAL");
		sql.addProjection("F.SG_FILIAL");
		sql.addProjection("RFC.NR_RECIBO_FRETE_CARRETEIRO");
		sql.addProjection("CC.NR_CONTROLE_CARGA");
		sql.addProjection("R.DS_ROTA");
		sql.addProjection("P_PROP.ID_PESSOA","ID_PROPRIETARIO");
		sql.addProjection("P_PROP.TP_PESSOA","TP_PESSOA_PROP");
		sql.addProjection("P_PROP.NR_IDENTIFICACAO","NR_IDENT_PROP");
		sql.addProjection("P_PROP.TP_IDENTIFICACAO","TP_IDENT_PROP");
		sql.addProjection("P_PROP.NM_PESSOA","NM_PROPRIETARIO");
		sql.addProjection("PROP.NR_PIS");
		sql.addProjection("P_MOT.ID_PESSOA","ID_MOTORISTA");
		sql.addProjection("P_MOT.NR_IDENTIFICACAO","NR_IDENT_MOT");
		sql.addProjection("P_MOT.TP_IDENTIFICACAO","TP_IDENT_MOT");
		sql.addProjection("P_MOT.NM_PESSOA","NM_MOTORISTA");
		sql.addProjection("MT.NR_IDENTIFICADOR","NR_IDENT_MT");
		sql.addProjection("MT.NR_FROTA","NR_FROTA");
		sql.addProjection("MODELO.DS_MODELO_MEIO_TRANSPORTE");
		sql.addProjection("MARCA.DS_MARCA_MEIO_TRANSPORTE");
		sql.addProjection("TIPO.DS_TIPO_MEIO_TRANSPORTE","DS_TIPO_MEIO_TRANSPORTE");
		sql.addProjection("MT.NR_ANO_FABRICAO","NR_ANO_FABRICACAO");
		sql.addProjection("MUNIC_MT.NM_MUNICIPIO","NM_MUNICIPIO_MT");
		sql.addProjection("TIPO.NR_CAPACIDADE_PESO_FINAL","CAPACIDADE");
		sql.addProjection("P_BENEF.NR_IDENTIFICACAO","NR_IDENT_BENEF");
		sql.addProjection("P_BENEF.TP_IDENTIFICACAO","TP_IDENT_BENEF");
		sql.addProjection("P_BENEF.NM_PESSOA","NM_BENEFICIARIO");
		sql.addProjection("B.NR_BANCO");
		sql.addProjection("AB.NR_AGENCIA_BANCARIA");
		sql.addProjection("AB.NR_DIGITO");
		sql.addProjection("CB.NR_CONTA_BANCARIA");
		sql.addProjection("CB.DV_CONTA_BANCARIA");
		sql.addProjection("RFC.VL_BRUTO");
		sql.addProjection("RFC.VL_POSTO_PASSAGEM");
		sql.addProjection("RFC.VL_PREMIO");
		sql.addProjection("RFC.VL_DIARIA");
		sql.addProjection("RFC.VL_DESCONTO");
		sql.addProjection("CP_CC.NR_CARTAO");
		sql.addProjection("RFC.BL_ADIANTAMENTO");

		sql.addProjection("(" + this.getVlAdiantamentoSubQuery() + ")","VL_ADIANTAMENTO");
		sql.addCriteriaValue("S");

		sql.addProjection("CASE WHEN RFC.VL_INSS is NULL THEN 0 " +
				"ELSE RFC.VL_INSS " +
				"END","VL_INSS");
		
		sql.addProjection(
				"CASE " +
				"WHEN RFC.VL_IRRF IS NULL THEN 0 " +
				"ELSE RFC.VL_IRRF " +
				"END","VL_IRRF");

		sql.addProjection("RFC.VL_LIQUIDO");
		sql.addProjection("RFC.VL_SALARIO_CONTRIBUICAO");
		sql.addProjection("RFC.PC_ALIQUOTA_INSS");
		sql.addProjection("(RFC.VL_SALARIO_CONTRIBUICAO * PC_ALIQUOTA_INSS)/100", "VL_APURADO");
		sql.addProjection("RFC.VL_OUTRAS_FONTES");
		sql.addProjection("((RFC.VL_SALARIO_CONTRIBUICAO * PC_ALIQUOTA_INSS)/100) - VL_OUTRAS_FONTES", "RESULTADO_APURADO");
		sql.addProjection("FCC_ORIGEM.SG_FILIAL", "SG_FILIAL_ORIGEM");
		sql.addProjection("RFCFP.NM_FANTASIA", "NM_FANTASIA_ORIGEM");
		sql.addProjection("RFCFD.SG_FILIAL", "SG_FILIAL_DESTINO");
		sql.addProjection("RFCFDP.NM_FANTASIA", "NM_FANTASIA_DESTINO");
		sql.addProjection("MOEDA.ID_MOEDA");
		sql.addProjection("MOEDA.SG_MOEDA");
		sql.addProjection("MOEDA.DS_SIMBOLO");
		sql.addProjection("RFC.ID_RECIBO_COMPLEMENTADO");
		sql.addProjection("RFC.OB_RECIBO_FRETE_CARRETEIRO");
		sql.addProjection("RFC.DH_EMISSAO");

		
		String sqlIdPostoConveniado = " select max(adiantamento_trecho.id_posto_conveniado) from adiantamento_trecho where "+ 
		" adiantamento_trecho.id_recibo_frete_carreteiro  = RFC.id_recibo_frete_carreteiro and adiantamento_trecho.id_posto_conveniado is not null";
		StringBuffer sqlFrom = new StringBuffer()
			.append("RECIBO_FRETE_CARRETEIRO RFC")
			.append("    INNER JOIN FILIAL F ON RFC.ID_FILIAL = F.ID_FILIAL")
			.append("    INNER JOIN PESSOA P_FILIAL ON F.ID_FILIAL = P_FILIAL.ID_PESSOA")
			.append("    INNER JOIN EMPRESA E ON F.ID_EMPRESA = E.ID_EMPRESA")
			.append("    INNER JOIN PESSOA P_EMPRESA ON E.ID_EMPRESA = P_EMPRESA.ID_PESSOA")
			.append("     LEFT JOIN CONTROLE_CARGA CC ON RFC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
			.append("     LEFT JOIN FILIAL FCC_ORIGEM ON CC.ID_FILIAL_ORIGEM = FCC_ORIGEM.ID_FILIAL")
			.append("     LEFT JOIN FILIAL RFCF ON RFCF.ID_FILIAL = RFC.ID_FILIAL")
			.append("     LEFT JOIN PESSOA RFCFP ON RFCFP.ID_PESSOA = RFCF.ID_FILIAL")
			.append("     LEFT JOIN FILIAL RFCFD ON RFCFD.ID_FILIAL = RFC.ID_FILIAL_DESTINO")
			.append("     LEFT JOIN PESSOA RFCFDP ON RFCFDP.ID_PESSOA = RFCFD.ID_FILIAL")

			.append("     LEFT JOIN ROTA R ON CC.ID_ROTA = R.ID_ROTA")
			.append("    INNER JOIN PROPRIETARIO PROP ON RFC.ID_PROPRIETARIO = PROP.ID_PROPRIETARIO")
			.append("    INNER JOIN PESSOA P_PROP ON PROP.ID_PROPRIETARIO = P_PROP.ID_PESSOA")
			.append("    LEFT JOIN MOTORISTA MOT ON RFC.ID_MOTORISTA = MOT.ID_MOTORISTA")
			.append("    LEFT JOIN PESSOA P_MOT ON MOT.ID_MOTORISTA = P_MOT.ID_PESSOA")
			.append("	 LEFT JOIN (SELECT MAX(ADIANTAMENTO_TRECHO.ID_POSTO_CONVENIADO) ID_POSTO_CONVENIADO, ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO ID_RECIBO_FRETE_CARRETEIRO FROM ADIANTAMENTO_TRECHO GROUP BY ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO) POSTO_C ON RFC.ID_RECIBO_FRETE_CARRETEIRO  = POSTO_C.ID_RECIBO_FRETE_CARRETEIRO ")
			.append("     LEFT JOIN PESSOA P_BENEF ON nvl(POSTO_C.ID_POSTO_CONVENIADO,RFC.ID_BENEFICIARIO) = P_BENEF.ID_PESSOA")
			.append("    INNER JOIN MEIO_TRANSPORTE MT ON RFC.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE")
			.append("    INNER JOIN MODELO_MEIO_TRANSPORTE MODELO ON MT.ID_MODELO_MEIO_TRANSPORTE = MODELO.ID_MODELO_MEIO_TRANSPORTE")
			.append("    INNER JOIN MARCA_MEIO_TRANSPORTE MARCA ON MODELO.ID_MARCA_MEIO_TRANSPORTE = MARCA.ID_MARCA_MEIO_TRANSPORTE")
			.append("    INNER JOIN TIPO_MEIO_TRANSPORTE TIPO ON MODELO.ID_TIPO_MEIO_TRANSPORTE = TIPO.ID_TIPO_MEIO_TRANSPORTE")
			.append("     LEFT JOIN MEIO_TRANSPORTE_RODOVIARIO MT_RODO ON MT.ID_MEIO_TRANSPORTE = MT_RODO.ID_MEIO_TRANSPORTE")
			.append("     LEFT JOIN MUNICIPIO MUNIC_MT ON MT_RODO.ID_MUNICIPIO = MUNIC_MT.ID_MUNICIPIO")
			.append("     LEFT JOIN CONTA_BANCARIA CB ON RFC.ID_CONTA_BANCARIA = CB.ID_CONTA_BANCARIA")
			.append("     LEFT JOIN AGENCIA_BANCARIA AB ON CB.ID_AGENCIA_BANCARIA = AB.ID_AGENCIA_BANCARIA")
			.append("     LEFT JOIN BANCO B ON AB.ID_BANCO = B.ID_BANCO")
			.append("    INNER JOIN MOEDA_PAIS MP ON RFC.ID_MOEDA_PAIS = MP.ID_MOEDA_PAIS")
			.append("    INNER JOIN MOEDA MOEDA ON MP.ID_MOEDA = MOEDA.ID_MOEDA")
			.append("	  LEFT JOIN PAGTO_PEDAGIO_CC PP_CC ON PP_CC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
			.append("	  LEFT JOIN CARTAO_PEDAGIO CP_CC ON CP_CC.ID_CARTAO_PEDAGIO = PP_CC.ID_CARTAO_PEDAGIO");

		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("RFC.ID_RECIBO_FRETE_CARRETEIRO", "=", criteria.getLong("idReciboFreteCarreteiro"));

		return sql;
	}

	private String getVlAdiantamentoSubQuery() {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("Sum(RFC2.VL_BRUTO)");
		sql.addFrom("RECIBO_FRETE_CARRETEIRO", "RFC2");
		sql.addCustomCriteria("RFC2.BL_ADIANTAMENTO = ?");
		sql.addJoin("RFC2.ID_CONTROLE_CARGA", "RFC.ID_CONTROLE_CARGA");
		sql.addJoin("RFC2.ID_PROPRIETARIO", "RFC.ID_PROPRIETARIO");
		sql.addJoin("RFC2.ID_MANIFESTO_VIAGEM_NACIONAL", "RFC.ID_MANIFESTO_VIAGEM_NACIONAL");
		return sql.getSql();
	}

	
	private String getDsEnderecoPadrao(Long id) {
		EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(id);
		if (ep != null)
			return enderecoPessoaService.formatEnderecoPessoaCompleto(ep);
		else return "";
	}
	
	// +55 (51) 33565050
	private String getDsTelefonePadrao(Long id) {
		TelefoneEndereco telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoPadrao(id,null);
		return getTelefoneFormatado(telefoneEndereco);
	}
	
	private String getTelefoneFormatado(TelefoneEndereco telefoneEndereco) {
		if (telefoneEndereco != null) {
			return FormatUtils.formatTelefone(telefoneEndereco.getNrTelefone(),telefoneEndereco.getNrDdd(),telefoneEndereco.getNrDdi());
		}
		else return "";
	}
	
		
	public Object[] executeOutrasFontes(Long idReciboFreteCarreteiro, Long idProprietario,
			String strDhEmissao) {
		SqlTemplate sql1 = new SqlTemplate();
		SqlTemplate sql2 = new SqlTemplate();
		SqlTemplate union = new SqlTemplate(); 

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		DateTime dhEmissao = JTFormatUtils.buildDateTimeFromTimestampTzString(strDhEmissao);

		sql1.addProjection("D.DS_EMPRESA","NM_EMPRESA");
		sql1.addProjection("F.SG_FILIAL","SG_FILIAL");
		sql1.addProjection("D.NR_RECIBO","NR_RECIBO");
		sql1.addProjection("D.VL_INSS","VL_INSS");
		sql1.addProjection("'1'","TP_FONTE");

		sql1.addFrom("DESCONTO_INSS_CARRETEIRO D " +
				"LEFT JOIN FILIAL F ON D.ID_FILIAL = F.ID_FILIAL");

		sql1.addCustomCriteria("D.ID_PROPRIETARIO = ?");
		union.addCriteriaValue(idProprietario);
		sql1.addCustomCriteria("TO_CHAR(D.DT_EMIS_RECIBO,'YYYY') = ?");
		union.addCriteriaValue(Integer.valueOf(dataAtual.getYear()));
		sql1.addCustomCriteria("TO_CHAR(D.DT_EMIS_RECIBO,'MM') = ?");
		union.addCriteriaValue(Integer.valueOf(dataAtual.getMonthOfYear()));
		sql1.addCustomCriteria("D.DH_INCLUSAO < ?");
		union.addCriteriaValue(dhEmissao);

		sql2.addProjection("P.NM_PESSOA","NM_EMPRESA");
		sql2.addProjection("F.SG_FILIAL","SG_FILIAL");
		sql2.addProjection("TO_CHAR(RFC.NR_RECIBO_FRETE_CARRETEIRO)","NR_RECIBO");
		sql2.addProjection("RFC.VL_INSS","VL_INSS");
		sql2.addProjection("'2'","TP_FONTE");
		
		sql2.addFrom("RECIBO_FRETE_CARRETEIRO RFC " +
				"INNER JOIN FILIAL F ON RFC.ID_FILIAL = F.ID_FILIAL " +
				"INNER JOIN PESSOA P ON F.ID_EMPRESA = P.ID_PESSOA");
		
		sql2.addCustomCriteria("RFC.TP_SITUACAO_RECIBO = ?");
		union.addCriteriaValue("EM");
		sql2.addCustomCriteria("RFC.ID_PROPRIETARIO = ?");
		union.addCriteriaValue(idProprietario);
		sql2.addCustomCriteria("TO_CHAR(RFC.DH_EMISSAO,'YYYY') = ?");
		union.addCriteriaValue(Integer.valueOf(dataAtual.getYear()));
		sql2.addCustomCriteria("TO_CHAR(RFC.DH_EMISSAO,'MM') = ?");
		union.addCriteriaValue(Integer.valueOf(dataAtual.getMonthOfYear()));
		sql2.addCustomCriteria("RFC.ID_RECIBO_FRETE_CARRETEIRO <> ?");
		union.addCriteriaValue(idReciboFreteCarreteiro);
		sql2.addCustomCriteria("RFC.DH_EMISSAO < ?");
		union.addCriteriaValue(dhEmissao);
		
		String retorno = String.valueOf(sql1.getSql() + " UNION " + sql2.getSql());
		return new Object[]{retorno,union.getCriteria()};
	}
	
	public JRDataSource executeOutrasFontesCol1(Long idReciboFreteCarreteiro, Long idProprietario,
			String strDhEmissao)  {
		Object[] obj = this.executeOutrasFontes(idReciboFreteCarreteiro,idProprietario,strDhEmissao);
		
		StringBuffer sql = new StringBuffer()
				.append("SELECT * FROM ( SELECT Power(-1,ROWNUM) AS NR_REGISTRO, V1.* FROM (")
				.append(obj[0])
				.append(") V1 ) WHERE NR_REGISTRO = -1");
		
		return executeQuery(sql.toString(),(Object[])obj[1]).getDataSource();
	}
	
	public JRDataSource executeOutrasFontesCol2(Long idReciboFreteCarreteiro, Long idProprietario,
			String strDhEmissao) {		
		Object[] obj = this.executeOutrasFontes(idReciboFreteCarreteiro,idProprietario,strDhEmissao);
		
		StringBuffer sql = new StringBuffer()
				.append("SELECT * FROM ( SELECT Power(-1,ROWNUM) AS NR_REGISTRO, V1.* FROM (")
				.append(obj[0])
				.append(") V1 ) WHERE NR_REGISTRO = 1");
		
		return executeQuery(sql.toString(),(Object[])obj[1]).getDataSource();
	}
	
	private String getObservacao(ResultSet rs) throws SQLException {
		Object[] parameters = new Object[4];
		
		String nmEmpresa = rs.getString("NM_EMPRESA");
		String tpIdentificacao = rs.getString("TP_IDENT_EMPRESA");
		String nrIdentificacao = rs.getString("NR_IDENT_EMPRESA");
		Domain dmTipoIdentificacao = domainService.findByName("DM_TIPO_IDENTIFICACAO");
		parameters[0] = nmEmpresa + " " + dmTipoIdentificacao.findDomainValueByValue(tpIdentificacao).getDescription().getValue() + " " +
				FormatUtils.formatIdentificacao(tpIdentificacao,nrIdentificacao);

		Moeda moeda = moedaService.findById(Long.valueOf(rs.getLong("ID_MOEDA")));
		BigDecimal vlLiquido = rs.getBigDecimal("VL_LIQUIDO");
		if (vlLiquido == null)
			vlLiquido = new BigDecimal(0);
		parameters[1] = rs.getString("SG_MOEDA") + " " + rs.getString("DS_SIMBOLO") +
				" " + FormatUtils.formatDecimal("###,##0.00",vlLiquido) +
				" (" + MoedaUtils.formataPorExtenso(vlLiquido.abs(),moeda) +
				")";

		parameters[2] = rs.getString("SG_FILIAL_ORIGEM") + " - " + rs.getString("NM_FANTASIA_ORIGEM"); 
		parameters[3] = rs.getString("SG_FILIAL_DESTINO") + " - " + rs.getString("NM_FANTASIA_DESTINO"); 
		
		return configuracoesFacade.getMensagem("obsReciboServicos",parameters);
	}
	
	public String getDsOutrasFontes(String nmEmpresa, String tpFonte, String sgFilial, String nrRecibo, BigDecimal vlInss, String dsMoeda) {
		StringBuffer sb = new StringBuffer();
		sb.append(nmEmpresa).append(", ");
		
		if (tpFonte.equals("1")) {
			if (StringUtils.isNotBlank(sgFilial)) {
				sb.append(sgFilial).append(" - ");
			}
			sb.append(nrRecibo);
		} else {
			sb.append(sgFilial).append(" - ").append(FormatUtils.formatIntegerWithZeros(Integer.valueOf(nrRecibo),"0000000000"));
		}
		
		sb.append(", ").append(dsMoeda).append(" ").append(FormatUtils.formatDecimal("###,##0.00",vlInss,true));

		return sb.toString();
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}
