package com.mercurio.lms.fretecarreteirocoletaentrega.report;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

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
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.MoedaUtils;
import com.mercurio.lms.util.session.SessionUtils;
/**
 * @author Andrêsa Vargas
 * 
 * @spring.bean id="lms.fretecarreteirocoletaentrega.emitirReciboService"
 * @spring.property name="reportName" value="com/mercurio/lms/fretecarreteirocoletaentrega/report/emitirRecibo.jasper"
 */
public class EmitirReciboService extends ReportServiceSupport {
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ConfiguracoesFacade configuracoesFacade;
	private DomainService domainService;
	private MoedaService moedaService;
	private NotaCreditoService notaCreditoService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JRReportDataObject execute(Map parameters) throws Exception {

		final TypedFlatMap mapIds = (TypedFlatMap)parameters;		
		SqlTemplate sql = createSqlTemplate(mapIds);

		final List dados = new LinkedList();

		getJdbcTemplate().query(sql.getSql(true), 
				JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), 
				new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					Map linha = new HashMap();
					Long idReciboFreteCarreteiro = Long.valueOf(rs.getLong("ID_RECIBO_FRETE_CARRETEIRO"));

					linha.put("ID_RECIBO_FRETE_CARRETEIRO",idReciboFreteCarreteiro);
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
					linha.put("ID_BENEFICIARIO",Long.valueOf(rs.getLong("ID_BENEFICIARIO")));
					linha.put("NM_BENEFICIARIO",rs.getString("NM_BENEFICIARIO"));
					linha.put("NR_BANCO",Integer.valueOf(rs.getInt("NR_BANCO")));
					linha.put("NR_AGENCIA_BANCARIA",Integer.valueOf(rs.getInt("NR_AGENCIA_BANCARIA")));
					linha.put("NR_DIGITO",rs.getString("NR_DIGITO"));
					linha.put("TP_CONTA",rs.getString("TP_CONTA"));
					linha.put("NR_CONTA_BANCARIA",rs.getString("NR_CONTA_BANCARIA"));
					linha.put("DV_CONTA_BANCARIA",rs.getString("DV_CONTA_BANCARIA"));
					linha.put("OB_RECIBO_FRETE_CARRETEIRO",rs.getString("OB_RECIBO_FRETE_CARRETEIRO"));

					BigDecimal vlBruto = rs.getBigDecimal("VL_BRUTO");
					linha.put("VL_BRUTO",BigDecimalUtils.round(vlBruto));										
					BigDecimal vlIssqn = rs.getBigDecimal("VL_ISSQN");
					linha.put("VL_ISSQN",vlIssqn != null ? vlIssqn : new BigDecimal(0));
					BigDecimal vlInss = rs.getBigDecimal("VL_INSS");
					linha.put("VL_INSS",vlInss != null ? vlInss : new BigDecimal(0));
					BigDecimal vlIrrf = rs.getBigDecimal("VL_IRRF");
					linha.put("VL_IRRF",vlIrrf != null ? vlIrrf : new BigDecimal(0));
					BigDecimal vlLiquido = rs.getBigDecimal("VL_LIQUIDO");
					linha.put("VL_LIQUIDO",vlLiquido != null ? vlLiquido : new BigDecimal(0));
					linha.put("VL_DESCONTO",rs.getBigDecimal("VL_DESCONTO"));
					BigDecimal vlPostoPassagem = rs.getBigDecimal("VL_POSTO_PASSAGEM");
					linha.put("VL_POSTO_PASSAGEM",vlPostoPassagem != null ? vlPostoPassagem : new BigDecimal(0));

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
					linha.put("NM_PESSOA_ORIGEM",rs.getString("NM_PESSOA_ORIGEM"));
					linha.put("SG_FILIAL_DESTINO",rs.getString("SG_FILIAL_DESTINO"));
					linha.put("NM_PESSOA_DESTINO",rs.getString("NM_PESSOA_DESTINO"));
					linha.put("SG_MOEDA",rs.getString("SG_MOEDA"));
					linha.put("DS_SIMBOLO",rs.getString("DS_SIMBOLO"));
					long idComplementado = rs.getLong("ID_RECIBO_COMPLEMENTADO");
					linha.put("ID_RECIBO_COMPLEMENTADO",idComplementado == 0 ? null : Long.valueOf(idComplementado));

					Long idProprietario = Long.valueOf(rs.getLong("ID_PROPRIETARIO"));
					Long idMotorista = Long.valueOf(rs.getLong("ID_MOTORISTA"));
					linha.put("DS_ENDERECO_PROP",getDsEnderecoPadrao(idProprietario));
					linha.put("DS_ENDERECO_MOT",getDsEnderecoPadrao(idMotorista));
					linha.put("DS_TELEFONE_PROP",getDsTelefonePadrao(idProprietario));
					linha.put("DS_TELEFONE_MOT",getDsTelefonePadrao(idMotorista));

					Domain dmTipoIdentificacao = domainService.findByName("DM_TIPO_IDENTIFICACAO");
					if(rs.getString("TP_IDENT_MOT") != null){
					linha.put("identMot",dmTipoIdentificacao.findDomainValueByValue(rs.getString("TP_IDENT_MOT")).getDescription().getValue());
					}
					linha.put("identProp",dmTipoIdentificacao.findDomainValueByValue(rs.getString("TP_IDENT_PROP")).getDescription().getValue());

					String tpBenef = rs.getString("TP_IDENT_BENEF");
					if (StringUtils.isNotBlank(tpBenef)) {
						linha.put("identBenef",dmTipoIdentificacao.findDomainValueByValue(tpBenef).getDescription().getValue());
					}	

					String strDhInicial = rs.getString("DH_PERIODO_INICIAL");
					String strDhFinal = rs.getString("DH_PERIODO_FINAL");
					if (strDhInicial != null && strDhFinal != null) {
						linha.put("PERIODO",
								JTFormatUtils.format(strDhInicial,JTFormatUtils.SHORT,JTFormatUtils.YEARMONTHDAY) +
								" " + configuracoesFacade.getMensagem("ate") + " " +
								JTFormatUtils.format(strDhFinal,JTFormatUtils.SHORT,JTFormatUtils.YEARMONTHDAY));
					}

					linha.put("DH_EMISSAO" ,rs.getString("DH_EMISSAO"));
					linha.put("OBSERVACAO", getObservacao(rs));
					linha.put("BL_REEMISSAO", (Boolean)((Object[])mapIds.get(idReciboFreteCarreteiro))[0]);

					linha.put("ID_PAGINA", Long.valueOf(1));
					dados.add(linha);
				}
				return null;
			}}
		);
		JRMapCollectionDataSource ds = new JRMapCollectionDataSource(dados);
		
		Map parametersReport = new HashMap();
		parametersReport.put("dataAtual",JTFormatUtils.format(JTDateTimeUtils.getDataAtual(),JTFormatUtils.SHORT));
		parametersReport.put("LB_INSS",configuracoesFacade.getMensagem("lbInss"));		
		return createReportDataObject(ds, parametersReport);
	}	
	
	/***
	 * Chamado pelo subreport emitirReciboPagamentoAutonomoFreteCarreteiro.jasper
	 * <B>Report do RPA  (LMSA-1880) </B>
	 * @param idReciboFreteCarreteiro
	 * @author AlessandroSF
	 * @return JRDataSource
	 */
	public JRDataSource executeDadosProprietario(Long idReciboFreteCarreteiro) {
		return new JRMapCollectionDataSource(getDadosProprietario(idReciboFreteCarreteiro));
	}

	private List<Map<String,Object>> getDadosProprietario(Long idReciboFreteCarreteiro) {
		final List<Map<String,Object>> retorno =  new ArrayList<Map<String,Object>>();

		StringBuffer sql = getSqlRPA();
		
		final Map<String,Object> recibo1 = new HashMap<String,Object>();
		getJdbcTemplate().query(sql.toString(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),new Object[]{idReciboFreteCarreteiro}), 
				new ResultSetExtractor() {
			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					
					recibo1.put("RPA_NUMERO_RECIBO",rs.getString("RPA_NUMERO_RECIBO"));
					recibo1.put("RPA_NOME",rs.getString("RPA_NOME"));
					recibo1.put("RPA_MATRICULA",FormatUtils.formatIdentificacao("CNPJ",rs.getBigDecimal("RPA_MATRICULA").toString()));
					recibo1.put("RPA_TEXTO_APLICA_CARRETEIRO",rs.getString("RPA_TEXTO_APLICA_CARRETEIRO"));
					
					recibo1.put("RPA_NO_PIS",rs.getString("RPA_NO_PIS"));
					recibo1.put("RPA_NO_CPF",FormatUtils.formatIdentificacao("CPF", rs.getString("RPA_NO_CPF")) );
					recibo1.put("RPA_RG_NUMERO",rs.getString("RPA_RG_NUMERO"));
					recibo1.put("RPA_RG_ORGAO_EMISSOR",rs.getString("RPA_RG_ORGAO_EMISSOR"));
					
					recibo1.put("RPA_VALOR_BRUTO",getValorFormatado(rs.getBigDecimal("RPA_VALOR_BRUTO")));
					recibo1.put("RPA_VALOR_DESCONTO",getValorFormatado(rs.getBigDecimal("RPA_VALOR_DESCONTO")));
					recibo1.put("RPA_VALOR_INSS",getValorFormatado(rs.getBigDecimal("RPA_VALOR_INSS")));
					recibo1.put("RPA_VALOR_IRRF",getValorFormatado(rs.getBigDecimal("RPA_VALOR_IRRF")));
					recibo1.put("RPA_VALOR_LIQUEDO",getValorFormatado(rs.getBigDecimal("RPA_VALOR_LIQUEDO")));
					recibo1.put("RPA_SOMA_DESCONTO",getValorFormatado(rs.getBigDecimal("RPA_SOMA_DESCONTO")));
					
					recibo1.put("RPA_NOME_COMPLETO",rs.getString("RPA_NOME_COMPLETO"));
					recibo1.put("RPA_LOCALIDADE",rs.getString("RPA_LOCALIDADE"));
					
					recibo1.put("RPA_SALARIO","");
					recibo1.put("RPA_TAXA","");		
					recibo1.put("RPA_VALORES_INSS","");
					
					recibo1.put("RPA_NUMERO_TALAO",rs.getString("RPA_NUMERO_TALAO"));
					
					recibo1.put("RPA_DATA",new java.text.SimpleDateFormat("dd 'de' MMMMM',' yyyy", new Locale("pt","BR")).format(rs.getDate("RPA_DATA")).toLowerCase());
					
					recibo1.put("RPA_TEXTO_RECIBO",getTextoRPA(rs.getBigDecimal("RPA_VALOR_LIQUEDO"),rs.getString("RPA_NUMERO_RECIBO")));
					recibo1.put("PRINT_CORTE",Boolean.TRUE);
					
					retorno.add(recibo1);
					
				}
				return null;
			}

			private String getValorFormatado(BigDecimal bigDecimal) {
				return  SessionUtils.getMoedaSessao().getDsSimbolo() + " "+ FormatUtils.formatDecimal("#,###,###,###,##0.00",bigDecimal);
			}
		});
		
		if(recibo1.isEmpty()){
			return retorno;
		}
		Map<String,Object> recibo2 = new HashMap<String,Object>();
		recibo2.putAll(recibo1);
		recibo2.put("PRINT_CORTE",Boolean.FALSE);
		
		retorno.add(recibo2);
		
		return retorno;
	}

	private StringBuffer getSqlRPA() {
		StringBuffer sql = new StringBuffer();
		sql.append("  SELECT FI.SG_FILIAL || ' ' || LPAD(NR_RECIBO_FRETE_CARRETEIRO, 10, '0') AS RPA_NUMERO_RECIBO, 	");
		sql.append("  PE.NM_PESSOA AS RPA_NOME, 	");
		sql.append("  PE.NR_IDENTIFICACAO AS RPA_MATRICULA, 	");
		sql.append("  (SELECT P1.DS_CONTEUDO || ' ' || P2.DS_CONTEUDO FROM PARAMETRO_GERAL P1, PARAMETRO_GERAL P2  	");
		sql.append("   WHERE P1.NM_PARAMETRO_GERAL  = 'LEI_BASE_IRRF' 	");
		sql.append("    AND  P2.NM_PARAMETRO_GERAL  = 'LEI_BASE_INSS') AS RPA_TEXTO_APLICA_CARRETEIRO, 	");
		sql.append("    PRO.NR_PIS AS RPA_NO_PIS, 	");
		sql.append("    PEP.NR_IDENTIFICACAO AS RPA_NO_CPF, 	");
		sql.append("    PEP.NR_RG AS RPA_RG_NUMERO, 	");
		sql.append("    PEP.DS_ORGAO_EMISSOR_RG AS RPA_RG_ORGAO_EMISSOR, 	");
		sql.append("    NVL(RFC.VL_BRUTO,0) AS RPA_VALOR_BRUTO, 	");
		sql.append("    NVL(RFC.VL_IRRF,0) AS RPA_VALOR_IRRF, 	");
		sql.append("    NVL(RFC.VL_INSS,0) AS RPA_VALOR_INSS, 	");
		sql.append("    NVL(RFC.VL_DESCONTO,0) AS RPA_VALOR_DESCONTO, 	");
		sql.append("    NVL(RFC.VL_LIQUIDO,0) AS RPA_VALOR_LIQUEDO, 	");
		sql.append("    ( NVL(RFC.VL_IRRF,0) + NVL(RFC.VL_INSS,0)+ NVL(RFC.VL_DESCONTO,0)) AS RPA_SOMA_DESCONTO, 	");
		sql.append("    PEP.NM_PESSOA AS RPA_NOME_COMPLETO, 	");
		sql.append("    MU.NM_MUNICIPIO AS RPA_LOCALIDADE, 	");
		sql.append("    RPA.DH_GERACAO_RPA AS RPA_DATA, 	");
		sql.append("    RPA.NR_RPA AS RPA_NUMERO_TALAO     	");
		sql.append("   FROM RECIBO_FRETE_CARRETEIRO RFC,  	");
		sql.append("       FILIAL FI,  	");
		sql.append("       PESSOA PE, 	");
		sql.append("       PROPRIETARIO PRO, 	");
		sql.append("       PESSOA PEP, 	");
		sql.append("       ENDERECO_PESSOA EP, 	");
		sql.append("       MUNICIPIO MU, 	");
		sql.append("       PARAMETRO_GERAL PG,	");
		sql.append("       PROPRIETARIO_RPA RPA	");
		sql.append("  WHERE RFC.ID_RECIBO_FRETE_CARRETEIRO = ?  	");
		sql.append("        AND RFC.ID_FILIAL = FI.ID_FILIAL  	");
		sql.append("        AND FI.ID_FILIAL  = PE.ID_PESSOA 	");
		sql.append("        AND PE.ID_ENDERECO_PESSOA = EP.ID_ENDERECO_PESSOA 	");
		sql.append("        AND EP.ID_MUNICIPIO = MU.ID_MUNICIPIO  	");
		sql.append("        AND RFC.ID_PROPRIETARIO = PRO.ID_PROPRIETARIO 	");
		sql.append("        AND RFC.ID_PROPRIETARIO = PEP.ID_PESSOA     	");
		sql.append("        AND PEP.TP_PESSOA = 'F'       	");
		sql.append("        AND PG.NM_PARAMETRO_GERAL ='ATIVA_EMISSAO_RPA'  	");
		sql.append("        AND UPPER(PG.DS_CONTEUDO) ='S'  	");
		sql.append("        AND RPA.ID_PROPRIETARIO = PRO.ID_PROPRIETARIO 	");
		sql.append("        AND RPA.ID_RECIBO_FRETE_CARRETEIRO = RFC.ID_RECIBO_FRETE_CARRETEIRO	");
		return sql;
	}

	private String getTextoRPA(BigDecimal valor, String nrRecibo) {
		List<String> parametros = new ArrayList<String>();
		parametros.add("Coleta/Entrega, pré fatura " +nrRecibo);
		parametros.add(FormatUtils.formatDecimal("#,###,###,###,##0.00",valor));
		parametros.add(MoedaUtils.formataPorExtenso(valor,SessionUtils.getMoedaSessao()).toLowerCase());
		return configuracoesFacade.getMensagem("textoReciboRPA", parametros.toArray());
	}

	private SqlTemplate createSqlTemplate(TypedFlatMap mapIds) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("RFC.ID_RECIBO_FRETE_CARRETEIRO");
		sql.addProjection("RFC.NR_RECIBO_FRETE_CARRETEIRO");

		sql.addProjection("P_EMPRESA.NR_IDENTIFICACAO","NR_IDENT_EMPRESA");
		sql.addProjection("P_EMPRESA.TP_IDENTIFICACAO","TP_IDENT_EMPRESA");
		sql.addProjection("P_EMPRESA.NM_PESSOA","NM_EMPRESA");
		sql.addProjection("P_FILIAL.TP_IDENTIFICACAO","TP_IDENT_FILIAL");
		sql.addProjection("P_FILIAL.NR_IDENTIFICACAO","NR_IDENT_FILIAL");
		sql.addProjection("P_FILIAL.NM_FANTASIA","NM_FILIAL");
		sql.addProjection("F.ID_FILIAL");
		sql.addProjection("F.SG_FILIAL");

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
		sql.addProjection("TIPO.DS_TIPO_MEIO_TRANSPORTE");
		sql.addProjection("MT.NR_ANO_FABRICAO","NR_ANO_FABRICACAO");
		sql.addProjection("MUNIC_MT.NM_MUNICIPIO","NM_MUNICIPIO_MT");
		sql.addProjection("TIPO.NR_CAPACIDADE_PESO_FINAL","CAPACIDADE");
		sql.addProjection("P_BENEF.ID_PESSOA","ID_BENEFICIARIO");
		sql.addProjection("P_BENEF.NR_IDENTIFICACAO","NR_IDENT_BENEF");
		sql.addProjection("P_BENEF.TP_IDENTIFICACAO","TP_IDENT_BENEF");
		sql.addProjection("P_BENEF.NM_PESSOA","NM_BENEFICIARIO");
		sql.addProjection("B.NR_BANCO");
		sql.addProjection("AB.NR_AGENCIA_BANCARIA");
		sql.addProjection("AB.NR_DIGITO");
		sql.addProjection("CB.NR_CONTA_BANCARIA");
		sql.addProjection("CB.DV_CONTA_BANCARIA");
		sql.addProjection("CB.TP_CONTA");
		sql.addProjection("RFC.VL_BRUTO");
		sql.addProjection("RFC.VL_POSTO_PASSAGEM");
		sql.addProjection("RFC.VL_PREMIO");
		sql.addProjection("RFC.VL_DIARIA");
		sql.addProjection("RFC.DH_EMISSAO");

		sql.addProjection("(" + this.getVlAdiantamentoSubQuery() + ")","VL_ADIANTAMENTO");
		sql.addCriteriaValue("S");

		sql.addProjection(
				"CASE " +
				"WHEN P_PROP.TP_PESSOA = 'F' THEN RFC.VL_INSS " +
				"ELSE NULL " +
				"END","VL_INSS");

		sql.addProjection(
				"CASE " +
				"WHEN P_PROP.TP_PESSOA = 'J' THEN VL_ISSQN " +
				"ELSE NULL " +
				"END","VL_ISSQN");

		sql.addProjection(
				"CASE " +
				"WHEN P_PROP.TP_PESSOA = 'F' THEN VL_IRRF " +
				"ELSE NULL " +
				"END","VL_IRRF");

		sql.addProjection("RFC.VL_LIQUIDO");
		sql.addProjection("RFC.VL_SALARIO_CONTRIBUICAO");
		sql.addProjection("RFC.PC_ALIQUOTA_INSS");
		sql.addProjection("(RFC.VL_SALARIO_CONTRIBUICAO * PC_ALIQUOTA_INSS)/100","VL_APURADO");
		sql.addProjection("RFC.VL_OUTRAS_FONTES");
		sql.addProjection("((RFC.VL_SALARIO_CONTRIBUICAO * PC_ALIQUOTA_INSS)/100) - VL_OUTRAS_FONTES","RESULTADO_APURADO");
		sql.addProjection("FCC_ORIGEM.SG_FILIAL","SG_FILIAL_ORIGEM");
		sql.addProjection("PCC_ORIGEM.NM_PESSOA","NM_PESSOA_ORIGEM");
		sql.addProjection("FCC_DESTINO.SG_FILIAL","SG_FILIAL_DESTINO");
		sql.addProjection("PCC_DESTINO.NM_PESSOA","NM_PESSOA_DESTINO");
		sql.addProjection("MOEDA.ID_MOEDA");
		sql.addProjection("MOEDA.SG_MOEDA");
		sql.addProjection("MOEDA.DS_SIMBOLO");
		sql.addProjection("RFC.ID_RECIBO_COMPLEMENTADO");
		sql.addProjection("RFC.VL_DESCONTO");
		sql.addProjection("RFC.DH_EMISSAO");

		sql.addProjection("(SELECT Min(NC1.DH_GERACAO) FROM NOTA_CREDITO NC1 " +
				"WHERE NC1.ID_RECIBO_FRETE_CARRETEIRO = RFC.ID_RECIBO_FRETE_CARRETEIRO)","DH_PERIODO_INICIAL");
		sql.addProjection("(SELECT Max(NC2.DH_GERACAO) FROM NOTA_CREDITO NC2 " +
				"WHERE NC2.ID_RECIBO_FRETE_CARRETEIRO = RFC.ID_RECIBO_FRETE_CARRETEIRO)","DH_PERIODO_FINAL");

		sql.addProjection("RFC.OB_RECIBO_FRETE_CARRETEIRO", "OB_RECIBO_FRETE_CARRETEIRO");
		
		StringBuffer sqlFrom = new StringBuffer()
			.append("RECIBO_FRETE_CARRETEIRO RFC")
			.append("    INNER JOIN FILIAL F ON RFC.ID_FILIAL = F.ID_FILIAL")
			.append("    INNER JOIN PESSOA P_FILIAL ON F.ID_FILIAL = P_FILIAL.ID_PESSOA")
			.append("    INNER JOIN EMPRESA E ON F.ID_EMPRESA = E.ID_EMPRESA")
			.append("    INNER JOIN PESSOA P_EMPRESA ON E.ID_EMPRESA = P_EMPRESA.ID_PESSOA")
			.append("    LEFT JOIN CONTROLE_CARGA CC ON RFC.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA")
			.append("    LEFT JOIN FILIAL FCC_ORIGEM ON CC.ID_FILIAL_ORIGEM = FCC_ORIGEM.ID_FILIAL")
			.append("    LEFT JOIN PESSOA PCC_ORIGEM ON FCC_ORIGEM.ID_FILIAL = PCC_ORIGEM.ID_PESSOA")
			.append("    LEFT JOIN FILIAL FCC_DESTINO ON CC.ID_FILIAL_DESTINO = FCC_DESTINO.ID_FILIAL")
			.append("    LEFT JOIN PESSOA PCC_DESTINO ON FCC_DESTINO.ID_FILIAL = PCC_DESTINO.ID_PESSOA")
			.append("    LEFT JOIN ROTA_IDA_VOLTA RIV ON CC.ID_ROTA_IDA_VOLTA = RIV.ID_ROTA_IDA_VOLTA")
			.append("    LEFT JOIN ROTA R ON RIV.ID_ROTA = R.ID_ROTA")
			.append("    INNER JOIN PROPRIETARIO PROP ON RFC.ID_PROPRIETARIO = PROP.ID_PROPRIETARIO")
			.append("    INNER JOIN PESSOA P_PROP ON PROP.ID_PROPRIETARIO = P_PROP.ID_PESSOA")
			.append("    LEFT JOIN MOTORISTA MOT ON RFC.ID_MOTORISTA = MOT.ID_MOTORISTA")
			.append("    LEFT JOIN PESSOA P_MOT ON MOT.ID_MOTORISTA = P_MOT.ID_PESSOA")
			.append("	 LEFT JOIN (SELECT MAX(ADIANTAMENTO_TRECHO.ID_POSTO_CONVENIADO) ID_POSTO_CONVENIADO, ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO ID_RECIBO_FRETE_CARRETEIRO FROM ADIANTAMENTO_TRECHO GROUP BY ADIANTAMENTO_TRECHO.ID_RECIBO_FRETE_CARRETEIRO) POSTO_C ON RFC.ID_RECIBO_FRETE_CARRETEIRO  = POSTO_C.ID_RECIBO_FRETE_CARRETEIRO ")
			.append("     LEFT JOIN PESSOA P_BENEF ON NVL(POSTO_C.ID_POSTO_CONVENIADO , RFC.ID_BENEFICIARIO) = P_BENEF.ID_PESSOA")
			.append("    INNER JOIN MEIO_TRANSPORTE MT ON RFC.ID_MEIO_TRANSPORTE = MT.ID_MEIO_TRANSPORTE")
			.append("    INNER JOIN MODELO_MEIO_TRANSPORTE MODELO ON MT.ID_MODELO_MEIO_TRANSPORTE = MODELO.ID_MODELO_MEIO_TRANSPORTE")
			.append("    INNER JOIN MARCA_MEIO_TRANSPORTE MARCA ON MODELO.ID_MARCA_MEIO_TRANSPORTE = MARCA.ID_MARCA_MEIO_TRANSPORTE")
			.append("    INNER JOIN TIPO_MEIO_TRANSPORTE TIPO ON MODELO.ID_TIPO_MEIO_TRANSPORTE = TIPO.ID_TIPO_MEIO_TRANSPORTE")
			.append("    LEFT JOIN MEIO_TRANSPORTE_RODOVIARIO MT_RODO ON MT.ID_MEIO_TRANSPORTE = MT_RODO.ID_MEIO_TRANSPORTE")
			.append("    LEFT JOIN MUNICIPIO MUNIC_MT ON MT_RODO.ID_MUNICIPIO = MUNIC_MT.ID_MUNICIPIO")
			.append("    LEFT JOIN CONTA_BANCARIA CB ON RFC.ID_CONTA_BANCARIA = CB.ID_CONTA_BANCARIA")
			.append("    LEFT JOIN AGENCIA_BANCARIA AB ON CB.ID_AGENCIA_BANCARIA = AB.ID_AGENCIA_BANCARIA")
			.append("    LEFT JOIN BANCO B ON AB.ID_BANCO = B.ID_BANCO")
			.append("    INNER JOIN MOEDA_PAIS MP ON RFC.ID_MOEDA_PAIS = MP.ID_MOEDA_PAIS")
			.append("    INNER JOIN MOEDA MOEDA ON MP.ID_MOEDA = MOEDA.ID_MOEDA");

		sql.addFrom(sqlFrom.toString());

		sql.addOrderBy("RFC.NR_RECIBO_FRETE_CARRETEIRO");

		if (!mapIds.isEmpty()) {
			Set ids = mapIds.keySet();
			StringBuffer criterias = new StringBuffer("");			
			Long idReciboParam = null;

			Iterator keys = ids.iterator();
			while (keys.hasNext()) {
				idReciboParam = (Long)keys.next();
				if (criterias.length() > 0)
					criterias.append(",");
				criterias.append("?");
				sql.addCriteriaValue(idReciboParam);
			}

			sql.addCustomCriteria(new StringBuffer("RFC.ID_RECIBO_FRETE_CARRETEIRO IN (")
					.append(criterias).append(")").toString());
		}

		return sql;
	}

	private String getVlAdiantamentoSubQuery() {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("Sum(RFC2.VL_BRUTO)");
		sql.addFrom("RECIBO_FRETE_CARRETEIRO","RFC2");
		sql.addCustomCriteria("RFC2.BL_ADIANTAMENTO = ?");
		sql.addJoin("RFC2.ID_CONTROLE_CARGA","RFC.ID_CONTROLE_CARGA");
		sql.addJoin("RFC2.ID_PROPRIETARIO","RFC.ID_PROPRIETARIO");

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
		if (telefoneEndereco != null) {
			return FormatUtils.formatTelefone(telefoneEndereco.getNrTelefone(),telefoneEndereco.getNrDdd(),telefoneEndereco.getNrDdi());
		}
		else return "";
	}

	public Object[] executeOutrasFontes(Long idReciboFreteCarreteiro, Long idProprietario, String strDhEmissao) {
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
			String strDhEmissao) {
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

	private SqlTemplate getSqlNotasCredito() {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("NC.ID_NOTA_CREDITO");
		sql.addProjection("F.SG_FILIAL");
		sql.addProjection("NC.NR_NOTA_CREDITO");
		sql.addProjection("M.SG_MOEDA");
		sql.addProjection("M.DS_SIMBOLO");
		sql.addFrom("NOTA_CREDITO NC " +
				"INNER JOIN FILIAL F ON NC.ID_FILIAL = F.ID_FILIAL " +
				"INNER JOIN MOEDA_PAIS MP ON NC.ID_MOEDA_PAIS = MP.ID_MOEDA_PAIS " +
				"INNER JOIN MOEDA M ON MP.ID_MOEDA = M.ID_MOEDA");

		sql.addCustomCriteria("NC.ID_RECIBO_FRETE_CARRETEIRO = ?");

		return sql;
	}

	public JRDataSource executeNotasCreditoCol1(Long idReciboFreteCarreteiro) {
		SqlTemplate s = this.getSqlNotasCredito();

		StringBuffer sql = new StringBuffer()
				.append("SELECT * FROM ( SELECT Power(-1,ROWNUM) AS NR_REGISTRO, V1.* FROM (")
				.append(s.getSql())
				.append(") V1 ) WHERE NR_REGISTRO = -1");

		return executeQuery(sql.toString(),new Object[]{idReciboFreteCarreteiro}).getDataSource();
	}

	public JRDataSource executeNotasCreditoCol2(Long idReciboFreteCarreteiro) {
		SqlTemplate s = this.getSqlNotasCredito();

		StringBuffer sql = new StringBuffer()
				.append("SELECT * FROM ( SELECT Power(-1,ROWNUM) AS NR_REGISTRO, V1.* FROM (")
				.append(s.getSql())
				.append(") V1 ) WHERE NR_REGISTRO = 1");

		return executeQuery(sql.toString(),new Object[]{idReciboFreteCarreteiro}).getDataSource();
	}

	public String getDsNotaCredito(Long idNotaCredito, Long nrNotaCredito, String sgFilial, String sgMoeda, String dsSimbolo) {
		StringBuffer sb = new StringBuffer();

		NotaCredito notaCredito = notaCreditoService.findById(idNotaCredito);
		sb.append(sgFilial).append(" ").append(nrNotaCredito)
			.append(" ").append(dsSimbolo).append(" ")
			.append(FormatUtils.formatDecimal("###,##0.00",notaCreditoService.findValorTotalNotaCredito(idNotaCredito)))
			.append(" - ")
			.append(JTDateTimeUtils.formatDateTimeToString(notaCredito.getDhEmissao(),"dd/MM/yyyy"));

		if(notaCredito.getTpNotaCredito()!= null){
			sb.append("(")
			.append(notaCredito.getTpNotaCredito().getValue())
			.append(") ");
		}
		return sb.toString();
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
		if (vlLiquido == null) {
			vlLiquido = new BigDecimal(0);
		}

		String valorPorExtenso = MoedaUtils.formataPorExtenso(vlLiquido.abs(),moeda);

		parameters[1] = rs.getString("SG_MOEDA") + " " + rs.getString("DS_SIMBOLO") +
				" " + FormatUtils.formatDecimal("###,##0.00", vlLiquido) +
				" (" + valorPorExtenso +
				")";

		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(Long.valueOf(rs.getLong("ID_FILIAL")));
		Municipio municipio = enderecoPessoa.getMunicipio();

		//Limita o nome da cidade em até 40 caracteres para não afetar o layout do relatório
		parameters[2] = StringUtils.left(municipio.getNmMunicipio(), 39);

		return configuracoesFacade.getMensagem("obsReciboColetaEntrega", parameters);
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
	
    public NotaCreditoService getNotaCreditoService() {
        return notaCreditoService;
}

    public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
        this.notaCreditoService = notaCreditoService;
    }

}
