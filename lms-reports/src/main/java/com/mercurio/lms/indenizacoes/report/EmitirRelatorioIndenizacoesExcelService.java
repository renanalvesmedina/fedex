package com.mercurio.lms.indenizacoes.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class EmitirRelatorioIndenizacoesExcelService extends ReportServiceSupport {

	private ConfiguracoesFacade configuracoesFacade;
	
	/**
	 * Método que chama o relatorio principal
	 */
	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap) parameters;
		SqlTemplate sql = createMainSql(tfm);
		JRReportDataObject jr = executeQuery(sql.getSql(), sql.getCriteria());

		Map<String, Object> parametersReport = new HashMap<String, Object>();
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_XLS);
		preencherParametrosPesquisa(tfm, parametersReport);
		jr.setParameters(parametersReport);

		return jr;
	}

	/**
	 * Layout do relátorio exibe os parâmetros utilizados na pesquisa em várias
	 * linhas, devido a isto não foi possível utlizar o método do framework
	 * sql.getFilterSummary(). 
	 * 
	 * @param tfm
	 * @param parametersReport
	 */
	private void preencherParametrosPesquisa(TypedFlatMap tfm, Map<String, Object> parametersReport) {

		// Regional
		if (tfm.getLong("regional.idRegionalFilial") != null) {
			parametersReport.put("PP_SGL_DESC_REG", tfm.getString("siglaDescricaoRegionalHidden"));
		}

		// Data emissão inicial
		YearMonthDay dtEmissaoInicial = tfm.getYearMonthDay("dtEmissaoInicial");
		if (dtEmissaoInicial != null) {
			parametersReport.put("PP_DT_EMISSAO_INICIAL", JTFormatUtils.format(dtEmissaoInicial));
		}

		// Data emissão final
		YearMonthDay dtEmissaoFinal = tfm.getYearMonthDay("dtEmissaoFinal");
		if (dtEmissaoFinal != null) {
			parametersReport.put("PP_DT_EMISSAO_FINAL", JTFormatUtils.format(dtEmissaoFinal));
		}

		// Sigla filial
		String siglaFilialHidden = tfm.getString("siglaFilialHidden");
		if (StringUtils.isNotBlank(siglaFilialHidden)) {
			parametersReport.put("PP_SGL_FILIAL", siglaFilialHidden);
		}

		// Nome filial
		String nmFilial = tfm.getString("filial.pessoa.nmFantasia");
		if (StringUtils.isNotBlank(nmFilial)) {
			parametersReport.put("PP_NM_FILIAL", nmFilial);
		}

		// Data liberação pagamento inicial
		YearMonthDay dataLiberacaoPgtoInicial = tfm.getYearMonthDay("dataLiberacaoPgtoInicial");
		if (dataLiberacaoPgtoInicial != null) {
			parametersReport.put("PP_DT_LIB_PGTO_INICIAL", JTFormatUtils.format(dataLiberacaoPgtoInicial));
		}

		// Data liberação pagamento final
		YearMonthDay dataLiberacaoPgtoFinal = tfm.getYearMonthDay("dataLiberacaoPgtoFinal");
		if (dataLiberacaoPgtoFinal != null) {
			parametersReport.put("PP_DT_LIB_PGTO_FINAL", JTFormatUtils.format(dataLiberacaoPgtoFinal));
		}

		// Número do recibo de indenização inicial
		Integer numeroInicial = tfm.getInteger("numeroInicial");
		if (numeroInicial != null) {
			parametersReport.put("PP_NUM_INICIAL", numeroInicial);
		}

		// Número do recibo de indenização final
		Integer numeroFinal = tfm.getInteger("numeroFinal");
		if (numeroFinal != null) {
			parametersReport.put("PP_NUM_FINAL", numeroFinal);
		}

		// Data programada pagamento inicial
		YearMonthDay dataProgramadaPagamentoInicial = tfm.getYearMonthDay("dataProgramadaPagamentoInicial");
		if (dataProgramadaPagamentoInicial != null) {
			parametersReport.put("PP_DT_PROG_PGTO_INICIAL", JTFormatUtils.format(dataProgramadaPagamentoInicial));
		}

		// Data programada pagamento final
		YearMonthDay dataProgramadaPagamentoFinal = tfm.getYearMonthDay("dataProgramadaPagamentoFinal");
		if (dataProgramadaPagamentoFinal != null) {
			parametersReport.put("PP_DT_PROG_PGTO_FINAL", JTFormatUtils.format(dataProgramadaPagamentoFinal));
		}

		// Tipo de indenização
		if (StringUtils.isNotBlank(tfm.getString("tpIndenizacao"))) {
			parametersReport.put("PP_TP_IND", tfm.getString("tpIndenizacaoHidden"));
		}

		// Data de pagamento inicial
		YearMonthDay dataPagamentoInicial = tfm.getYearMonthDay("dataPagamentoInicial");
		if (dataPagamentoInicial != null) {
			parametersReport.put("PP_DT_PGTO_INICIAL", JTFormatUtils.format(dataPagamentoInicial));
		}

		// Data de pagamento final
		YearMonthDay dataPagamentoFinal = tfm.getYearMonthDay("dataPagamentoFinal");
		if (dataPagamentoFinal != null) {
			parametersReport.put("PP_DT_PGTO_FINAL", JTFormatUtils.format(dataPagamentoFinal));
		}

		// Status
		if (StringUtils.isNotBlank(tfm.getString("tpStatusIndenizacao"))) {
			parametersReport.put("PP_TP_STA", tfm.getString("tpStatusIndenizacaoHidden"));
		}

		// Data análise NC inicial
		YearMonthDay dataAnaliseNcInicial = tfm.getYearMonthDay("dataAnaliseNcInicial");
		if (dataAnaliseNcInicial != null) {
			parametersReport.put("PP_DT_ANAL_INCIAL", JTFormatUtils.format(dataAnaliseNcInicial));
		}

		// Data análise NC final
		YearMonthDay dataAnaliseNcFinal = tfm.getYearMonthDay("dataAnaliseNcFinal");
		if (dataAnaliseNcFinal != null) {
			parametersReport.put("PP_DT_ANAL_FINAL", JTFormatUtils.format(dataAnaliseNcFinal));
		}

		// Situação da aprovação
		if (StringUtils.isNotBlank(tfm.getString("situacaoAprovacao"))) {
			parametersReport.put("PP_DM_SIT_APRO", tfm.getString("situacaoAprovacaoHidden"));
		}

		// Usuário análise NC
		String usuarioAnaliseNc = tfm.getString("usuario.nmUsuario");
		if (StringUtils.isNotBlank(usuarioAnaliseNc)) {
			parametersReport.put("PP_NM_USU_ANAL_NC", usuarioAnaliseNc);
		}

		// Natureza do produto
		if (tfm.getLong("naturezaProduto.idNaturezaProduto") != null) {
			parametersReport.put("PP_DS_NAT_PROD", tfm.getString("naturezaProduto.dsNaturezaProdutoHidden"));
		}

		// Data lote inicial
		YearMonthDay dataLoteInicial = tfm.getYearMonthDay("dataLoteInicial");
		if (dataLoteInicial != null) {
			parametersReport.put("PP_DT_LOT_INCIAL", JTFormatUtils.format(dataLoteInicial));
		}

		// Data lote final
		YearMonthDay dataLoteFinal = tfm.getYearMonthDay("dataLoteFinal");
		if (dataLoteFinal != null) {
			parametersReport.put("PP_DT_LOT_FINAL", JTFormatUtils.format(dataLoteFinal));
		}

		// Forma de pagamento
		if (StringUtils.isNotBlank(tfm.getString("formaPagamento"))) {
			parametersReport.put("PP_DM_FORM_PGTO", tfm.getString("formaPagamentoHidden"));
		}

		// Número do lote
		parametersReport.put("PP_NR_LOT_JDE", tfm.getLong("nrLoteJdeRim"));

		// Número do processo sinistro
		String nrProcessoSinistro = tfm.getString("nrProcessoSinistro");
		if (StringUtils.isNotBlank(nrProcessoSinistro)) {
			parametersReport.put("PP_NR_PROC_SIN", nrProcessoSinistro);
		}

		// Sigla filial RNC
		String siglaFilialRncHidden = tfm.getString("siglaFilialRncHidden");
		if (StringUtils.isNotBlank(siglaFilialRncHidden)) {
			parametersReport.put("PP_SGL_FILIAL_RNC", siglaFilialRncHidden);
		}

		// Nome filial RNC
		String nmFilialRnc = tfm.getString("filialRnc.pessoa.nmFantasia");
		if (StringUtils.isNotBlank(nmFilialRnc)) {
			parametersReport.put("PP_NM_FILIAL_RNC", nmFilialRnc);
		}

		// Tipo de seguro
		if (tfm.getLong("tipoSeguro.idTipoSeguro") != null) {
			parametersReport.put("PP_SG_TP_SEG", tfm.getString("sgTipoSeguroHidden"));
		}

		// Número RNC inicial
		Integer numeroRncInicial = tfm.getInteger("numeroRncInicial");
		if (numeroRncInicial != null) {
			parametersReport.put("PP_NUM_RNC_INICIAL", numeroRncInicial);
		}

		// Número RNC final
		Integer numeroRncFinal = tfm.getInteger("numeroRncFinal");
		if (numeroRncFinal != null) {
			parametersReport.put("PP_NUM_RNC_FINAL", numeroRncFinal);
		}

		// Tipo de sinistro
		if (tfm.getLong("tipoSinistro.idTipo") != null) {
			parametersReport.put("PP_DS_TP_SIN", tfm.getString("dsTipoSinistroHidden"));
		}

		// Data emissão RNC inicial
		YearMonthDay dataEmissaoRncInicial = tfm.getYearMonthDay("dataEmissaoRncInicial");
		if (dataEmissaoRncInicial != null) {
			parametersReport.put("PP_DT_EM_RNC_INCIAL", JTFormatUtils.format(dataEmissaoRncInicial));
		}

		// Data emissão RNC final
		YearMonthDay dataEmissaoRncFinal = tfm.getYearMonthDay("dataEmissaoRncFinal");
		if (dataEmissaoRncFinal != null) {
			parametersReport.put("PP_DT_EM_RNC_FINAL", JTFormatUtils.format(dataEmissaoRncFinal));
		}

		// Salvados
		if (StringUtils.isNotBlank(tfm.getString("blSalvados"))) {
			parametersReport.put("PP_BL_SALV", tfm.getString("blSalvadosHidden"));
		}

		// Motivo abertura
		if (tfm.getLong("motivoAbertura.idMotivoAberturaNc") != null) {
			parametersReport.put("PP_DS_MOT_ABER", tfm.getString("dsMotivoAberturaHidden"));
		}

		// Valor indenização inicial
		BigDecimal valorIndenizacaoInicial = tfm.getBigDecimal("valorIndenizacaoInicial");
		if (valorIndenizacaoInicial != null) {
			parametersReport.put("PP_VL_IND_INICIAL", valorIndenizacaoInicial);
		}

		// Valor indenização final
		BigDecimal valorIndenizacaoFinal = tfm.getBigDecimal("valorIndenizacaoFinal");
		if (valorIndenizacaoFinal != null) {
			parametersReport.put("PP_VL_IND_FINAL", valorIndenizacaoFinal);
		}

		// Sigla filial ocorrência 1
		String siglaFilialOcorrencia1Hidden = tfm.getString("siglaFilialOcorrencia1Hidden");
		if (StringUtils.isNotBlank(siglaFilialOcorrencia1Hidden)) {
			parametersReport.put("PP_SGL_FILIAL_OCOR_1", siglaFilialOcorrencia1Hidden);
		}

		// Nome filial ocorrência 1
		String nmFilialOcorrencia1 = tfm.getString("filialOcorrencia1.pessoa.nmFantasia");
		if (StringUtils.isNotBlank(nmFilialOcorrencia1)) {
			parametersReport.put("PP_NM_FILIAL_OCOR_1", nmFilialOcorrencia1);
		}

		// Sigla filial debitada
		String siglaFilialDebitadaHidden = tfm.getString("siglaFilialDebitadaHidden");
		if (StringUtils.isNotBlank(siglaFilialDebitadaHidden)) {
			parametersReport.put("PP_SGL_FILIAL_DEB", siglaFilialDebitadaHidden);
		}

		// Nome filial debitada
		String nmFilialDebitada = tfm.getString("filialDebitada.pessoa.nmFantasia");
		if (StringUtils.isNotBlank(nmFilialDebitada)) {
			parametersReport.put("PP_NM_FILIAL_DEB", nmFilialDebitada);
		}

		// Sigla filial ocorrência 2
		String siglaFilialOcorrencia2Hidden = tfm.getString("siglaFilialOcorrencia2Hidden");
		if (StringUtils.isNotBlank(siglaFilialOcorrencia2Hidden)) {
			parametersReport.put("PP_SGL_FILIAL_OCOR_2", siglaFilialOcorrencia2Hidden);
		}

		// Nome filial ocorrência 2
		String nmFilialOcorrencia2 = tfm.getString("filialOcorrencia2.pessoa.nmFantasia");
		if (StringUtils.isNotBlank(nmFilialOcorrencia2)) {
			parametersReport.put("PP_NM_FILIAL_OCOR_2", nmFilialOcorrencia2);
		}

		// Beneficiário CNPJ/CPF
		String beneficiarioNrIdentificacaoHidden = tfm.getString("beneficiario.nrIdentificacaoHidden");
		if (StringUtils.isNotBlank(beneficiarioNrIdentificacaoHidden)) {
			parametersReport.put("PP_BEN_CNPJ_CPF", beneficiarioNrIdentificacaoHidden);
		}

		// Beneficiário nome pessoa
		String beneficiarioNmPessoa = tfm.getString("beneficiario.nmPessoa");
		if (StringUtils.isNotBlank(beneficiarioNmPessoa)) {
			parametersReport.put("PP_BEN_NM_PES", beneficiarioNmPessoa);
		}

		// Favorecido CNPJ/CPF
		String favorecidoNrIdentificacaoHidden = tfm.getString("favorecido.nrIdentificacaoHidden");
		if (StringUtils.isNotBlank(favorecidoNrIdentificacaoHidden)) {
			parametersReport.put("PP_FAV_CNPJ_CPF", favorecidoNrIdentificacaoHidden);
		}

		// Favorecido nome pessoa
		String favorecidoNmPessoa = tfm.getString("favorecido.nmPessoa");
		if (StringUtils.isNotBlank(favorecidoNmPessoa)) {
			parametersReport.put("PP_FAV_NM_PES", favorecidoNmPessoa);
		}

		// Documento de serviço
		if (tfm.getLong("doctoServico.idDoctoServico") != null) {
			String identificacaoDocumento = tfm.getString("doctoServico.tpDocumentoHidden");
			identificacaoDocumento += " ";
			identificacaoDocumento += tfm.getString("doctoServico.sgFilialOrigemHidden");
			identificacaoDocumento += " ";
			identificacaoDocumento += tfm.getString("doctoServico.nrDoctoServicoHidden");
			parametersReport.put("PP_DOC_SERV", identificacaoDocumento);
		}

		// Data emissão documento inicial
		YearMonthDay dtEmissaoDocInicial = tfm.getYearMonthDay("dtEmissaoDocInicial");
		if (dtEmissaoDocInicial != null) {
			parametersReport.put("PP_DT_EM_DOC_INCIAL", JTFormatUtils.format(dtEmissaoDocInicial));
		}

		// Data emissão documento final
		YearMonthDay dtEmissaoDocFinal = tfm.getYearMonthDay("dtEmissaoDocFinal");
		if (dtEmissaoDocFinal != null) {
			parametersReport.put("PP_DT_EM_DOC_FINAL", JTFormatUtils.format(dtEmissaoDocFinal));
		}

		// Remetente CNPJ/CPF
		String remetenteNrIdentificacaoHidden = tfm.getString("remetente.nrIdentificacaoHidden");
		if (StringUtils.isNotBlank(remetenteNrIdentificacaoHidden)) {
			parametersReport.put("PP_REM_CNPJ_CPF", remetenteNrIdentificacaoHidden);
		}

		// Remetente nome pessoa
		String remetenteNmPessoa = tfm.getString("remetente.nmPessoa");
		if (StringUtils.isNotBlank(remetenteNmPessoa)) {
			parametersReport.put("PP_REM_NM_PES", remetenteNmPessoa);
		}

		// Destinatário CNPJ/CPF
		String destinatarioNrIdentificacaoHidden = tfm.getString("destinatario.nrIdentificacaoHidden");
		if (StringUtils.isNotBlank(destinatarioNrIdentificacaoHidden)) {
			parametersReport.put("PP_DES_CNPJ_CPF", destinatarioNrIdentificacaoHidden);
		}

		// Destinatário nome pessoa
		String destinatarioNmPessoa = tfm.getString("destinatario.nmPessoa");
		if (StringUtils.isNotBlank(destinatarioNmPessoa)) {
			parametersReport.put("PP_DES_NM_PES", destinatarioNmPessoa);
		}

		// Devedor CNPJ/CPF
		String devedorNrIdentificacaoHidden = tfm.getString("devedor.nrIdentificacaoHidden");
		if (StringUtils.isNotBlank(devedorNrIdentificacaoHidden)) {
			parametersReport.put("PP_DEV_CNPJ_CPF", devedorNrIdentificacaoHidden);
		}

		// Devedor nome pessoa
		String devedorNmPessoa = tfm.getString("devedor.nmPessoa");
		if (StringUtils.isNotBlank(devedorNmPessoa)) {
			parametersReport.put("PP_DEV_NM_PES", devedorNmPessoa);
		}

		// Modal
		if (StringUtils.isNotBlank(tfm.getString("modal"))) {
			parametersReport.put("PP_MODAL", tfm.getString("modalHidden"));
		}

		// Abrangência
		if (StringUtils.isNotBlank(tfm.getString("abrangencia"))) {
			parametersReport.put("PP_ABR", tfm.getString("abrangenciaHidden"));
		}

		// Serviço
		if (tfm.getLong("servico.idServico") != null) {
			parametersReport.put("PP_SERV", tfm.getString("dsServicoHidden"));
		}
	}

	/**
	 * Consulta principal.
	 * 
	 * @param tfm
	 * @return
	 */
	public SqlTemplate createMainSql(TypedFlatMap tfm) {
		String idsPerfisAnalistasNc = (String) configuracoesFacade.getValorParametro("PERFIS_ANALISTAS_NC");
		idsPerfisAnalistasNc = idsPerfisAnalistasNc.replace(";", ",");
		
		SqlTemplate sql = createSqlTemplate();

		//Campos
		sql.addProjection("REG.SG_REGIONAL", "SG_REGIONAL");
		sql.addProjection("REG.DS_REGIONAL", "DS_REGIONAL");
		sql.addProjection("FI.SG_FILIAL", "SG_FILIAL");
		sql.addProjection("RI.NR_RECIBO_INDENIZACAO", "NR_RECIBO_INDENIZACAO");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_INDENIZACAO') AND VL_VALOR_DOMINIO = RI.TP_INDENIZACAO)", "TP_INDENIZACAO");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_INDENIZACAO') AND VL_VALOR_DOMINIO = RI.TP_STATUS_INDENIZACAO)", "STATUS");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_WORKFLOW') AND VL_VALOR_DOMINIO = RI.TP_SITUACAO_WORKFLOW)", "ST_APROVACAO");
		sql.addProjection("RI.DT_EMISSAO", "DT_EMISSAO");
		sql.addProjection("RI.DT_LIBERACAO_PAGAMENTO", "DT_LIB_PGTO");
		sql.addProjection("RI.DT_PROGRAMADA_PAGAMENTO", "DT_PROG_PGTO");
		sql.addProjection("RI.DT_PAGAMENTO_EFETUADO", "DT_PGTO_EFET");
		sql.addProjection("(SELECT TRUNC(A.DH_ACAO) FROM ACAO A, INTEGRANTE I WHERE A.ID_INTEGRANTE = I.ID_INTEGRANTE AND A.ID_PENDENCIA = RI.ID_PENDENCIA AND (I.ID_USUARIO IN (SELECT PU.ID_USUARIO FROM PERFIL_USUARIO PU WHERE PU.ID_PERFIL IN (" + idsPerfisAnalistasNc + ")) OR I.ID_PERFIL IN (" + idsPerfisAnalistasNc +")) AND A.TP_SITUACAO_ACAO = 'A' AND ROWNUM = 1)", "DT_ANAL_NC");
		
		String idUsuarioNc = tfm.getString("usuario.idUsuario");
		StringBuilder sqlUsuarioAnaliseNc = new StringBuilder("(SELECT U.NM_USUARIO FROM ACAO A, INTEGRANTE I, USUARIO U WHERE A.ID_INTEGRANTE = I.ID_INTEGRANTE AND A.ID_PENDENCIA = RI.ID_PENDENCIA AND A.ID_USUARIO = U.ID_USUARIO AND (I.ID_USUARIO IN (SELECT PU.ID_USUARIO FROM PERFIL_USUARIO PU WHERE PU.ID_PERFIL IN (");
		sqlUsuarioAnaliseNc.append(idsPerfisAnalistasNc); 
		sqlUsuarioAnaliseNc.append(")) OR I.ID_PERFIL IN (");
		sqlUsuarioAnaliseNc.append(idsPerfisAnalistasNc); 
		sqlUsuarioAnaliseNc.append(")) AND A.TP_SITUACAO_ACAO = 'A' ");
		if(StringUtils.isNotBlank(idUsuarioNc)){
			sqlUsuarioAnaliseNc.append("AND U.ID_USUARIO = ");
			sqlUsuarioAnaliseNc.append(idUsuarioNc);
			sqlUsuarioAnaliseNc.append(" ");
		}
		sqlUsuarioAnaliseNc.append("AND ROWNUM = 1)");
		sql.addProjection(sqlUsuarioAnaliseNc.toString(), "US_ANAL_NC");
		
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_FORMA_PAGAMENTO_INDENIZACAO') AND VL_VALOR_DOMINIO = RI.TP_FORMA_PAGAMENTO)", "FM_PGTO");	
		sql.addProjection("BA.NR_BANCO", "NR_BANCO");
		sql.addProjection("AB.NR_AGENCIA_BANCARIA", "NR_AGEN_BANC");
		sql.addProjection("AB.NR_DIGITO", "NR_DIGITO");
		sql.addProjection("RI.NR_CONTA_CORRENTE", "NR_CONTA_CORRENTE");
		sql.addProjection("RI.NR_DIGITO_CONTA_CORRENTE", "NR_DIG_CC");
		sql.addProjection("(SELECT MIN(PRI.DT_VENCIMENTO) FROM PARCELA_RECIBO_INDENIZACAO PRI WHERE PRI.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO)", "DT_VENC");
		sql.addProjection("RI.VL_INDENIZACAO", "VL_INDENIZACAO");
		sql.addProjection("DS.VL_MERCADORIA", "VL_MERCADORIA");
		sql.addProjection("CAST(ROUND((RI.VL_INDENIZACAO / DS.VL_MERCADORIA) * 100, 2) AS NUMBER(19,2))", "PERC_IND");
		sql.addProjection("RI.QT_VOLUMES_INDENIZADOS", "QT_VOL_IND");
		sql.addProjection("DS.QT_VOLUMES", "QT_VOL");
		sql.addProjection("VI18N(NP.DS_NATUREZA_PRODUTO_I)", "DS_NAT_PROD");
		sql.addProjection("VI18N(P.DS_PRODUTO_I)", "DS_PROD");
		sql.addProjection("(SELECT FID.SG_FILIAL FROM FILIAL_DEBITADA FD, FILIAL FID WHERE FD.ID_FILIAL = FID.ID_FILIAL AND FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ROWNUM = 1)", "SG_FIL_DEB_1");
		sql.addProjection("(SELECT FD.PC_DEBITADO FROM FILIAL_DEBITADA FD WHERE FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ROWNUM = 1)", "PERC_FIL_DEB_1");
		sql.addProjection("(SELECT FID.SG_FILIAL FROM FILIAL_DEBITADA FD, FILIAL FID WHERE FD.ID_FILIAL = FID.ID_FILIAL AND FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ROWNUM = 1 AND FD.ID_FILIAL_DEBITADA <> (SELECT FD2.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD2 WHERE FD2.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO AND ROWNUM = 1))", "SG_FIL_DEB_2");
		sql.addProjection("(SELECT FD.PC_DEBITADO FROM FILIAL_DEBITADA FD WHERE FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ROWNUM = 1 AND FD.ID_FILIAL_DEBITADA <> (SELECT FD2.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD2 WHERE FD2.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO AND ROWNUM = 1))", "PERC_FIL_DEB_2");
		sql.addProjection("(SELECT FID.SG_FILIAL FROM FILIAL_DEBITADA FD, FILIAL FID WHERE FD.ID_FILIAL = FID.ID_FILIAL AND FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND FD.ID_FILIAL_DEBITADA <> (SELECT FD2.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD2 WHERE FD2.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO AND ROWNUM = 1) AND FD.ID_FILIAL_DEBITADA <> (SELECT FD2.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD2 WHERE FD2.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO AND ROWNUM = 1 AND FD2.ID_FILIAL_DEBITADA <> (SELECT FD3.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD3 WHERE FD3.ID_RECIBO_INDENIZACAO = FD2.ID_RECIBO_INDENIZACAO AND ROWNUM = 1)) AND ROWNUM = 1)", "SG_FIL_DEB_3");	
		sql.addProjection("(SELECT FD.PC_DEBITADO FROM FILIAL_DEBITADA FD  WHERE FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND FD.ID_FILIAL_DEBITADA <> (SELECT FD2.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD2 WHERE FD2.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO AND ROWNUM = 1) AND FD.ID_FILIAL_DEBITADA <> (SELECT FD2.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD2 WHERE FD2.ID_RECIBO_INDENIZACAO = FD.ID_RECIBO_INDENIZACAO AND ROWNUM = 1 AND FD2.ID_FILIAL_DEBITADA <> (SELECT FD3.ID_FILIAL_DEBITADA FROM FILIAL_DEBITADA FD3 WHERE FD3.ID_RECIBO_INDENIZACAO = FD2.ID_RECIBO_INDENIZACAO AND ROWNUM = 1)) AND ROWNUM = 1)", "PERC_FIL_DEB_3");		
		sql.addProjection("FNCONC.SG_FILIAL", "FL_RNC");
		sql.addProjection("NCONC.NR_NAO_CONFORMIDADE", "NR_RNC");
		sql.addProjection("TRUNC(NCONC.DH_EMISSAO)", "DT_EMISSAO_RNC");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_NAO_CONFORMIDADE') AND VL_VALOR_DOMINIO = NCONC.TP_STATUS_NAO_CONFORMIDADE)", "STS_RNC");
		sql.addProjection("VI18N(MANC.DS_MOTIVO_ABERTURA_I)", "DS_MOT_ABER");
		sql.addProjection("PS.NR_PROCESSO_SINISTRO", "NR_PRO_SIN");
		sql.addProjection("TS.SG_TIPO", "TP_SEG");
		sql.addProjection("VI18N(TSIN.DS_TIPO_I)", "DS_TP_SIN");
		sql.addProjection("RI.NR_NOTA_FISCAL_DEBITO_CLIENTE", "NR_NOTA_FIS_DEB_CLI");
		sql.addProjection("FSIN.SG_FILIAL", "SG_FIL_OCOR_1");
		sql.addProjection("FROTA.SG_FILIAL", "SG_FIL_OCOR_2");
		sql.addProjection("RI.OB_RECIBO_INDENIZACAO", "OBS_RI");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_SIM_NAO') AND VL_VALOR_DOMINIO = RI.BL_SEGURADO)", "BL_SEG");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_SIM_NAO') AND VL_VALOR_DOMINIO = RI.BL_SALVADOS)", "SALVADOS");
		sql.addProjection("(SELECT F2.SG_FILIAL FROM DOCTO_SERVICO DS2, FILIAL F2, MDA_SALVADO_INDENIZACAO MD WHERE DS2.ID_FILIAL_ORIGEM = F2.ID_FILIAL AND MD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND MD.ID_MDA = DS2.ID_DOCTO_SERVICO AND MD.ID_MDA_SALVADO_INDENIZACAO = (SELECT MAX(MD2.ID_MDA_SALVADO_INDENIZACAO) FROM MDA_SALVADO_INDENIZACAO MD2 WHERE MD2.ID_RECIBO_INDENIZACAO = MD.ID_RECIBO_INDENIZACAO))", "FIL_MDA");	
		sql.addProjection("(SELECT DS2.NR_DOCTO_SERVICO FROM DOCTO_SERVICO DS2, FILIAL F2, MDA_SALVADO_INDENIZACAO MD WHERE DS2.ID_FILIAL_ORIGEM = F2.ID_FILIAL AND MD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND MD.ID_MDA = DS2.ID_DOCTO_SERVICO AND MD.ID_MDA_SALVADO_INDENIZACAO = (SELECT MAX(MD2.ID_MDA_SALVADO_INDENIZACAO) FROM MDA_SALVADO_INDENIZACAO MD2 WHERE MD2.ID_RECIBO_INDENIZACAO = MD.ID_RECIBO_INDENIZACAO))", "NR_MDA");	
		sql.addProjection("LJ.ID_LOTE_JDE_RIM", "NR_LOTE_JDE");
		sql.addProjection("TRUNC(LJ.DH_LOTE_JDE_RIM)", "DT_LOTE_JDE");
		sql.addProjection("(SELECT TRUNC(ER.DH_EVENTO_RIM) FROM EVENTO_RIM ER WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'CR'))", "DT_CANC");
		sql.addProjection("(SELECT U.NM_USUARIO FROM EVENTO_RIM ER, USUARIO U WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_USUARIO = U.ID_USUARIO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'CR'))", "NM_USU_CANC");
		sql.addProjection("(SELECT TRUNC(ER.DH_EVENTO_RIM) FROM EVENTO_RIM ER WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'LI'))", "DT_LIB_PAG");
		sql.addProjection("(SELECT U.NM_USUARIO FROM EVENTO_RIM ER, USUARIO U WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_USUARIO = U.ID_USUARIO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'LI'))", "NM_USU_LIB_PAG");
		sql.addProjection("(SELECT TRUNC(ER.DH_EVENTO_RIM) FROM EVENTO_RIM ER WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'EJ'))", "DT_ENV_JDE");
		sql.addProjection("(SELECT U.NM_USUARIO FROM EVENTO_RIM ER, USUARIO U WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_USUARIO = U.ID_USUARIO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'EJ'))", "NM_USU_ENV_JDE");
		sql.addProjection("(SELECT TRUNC(ER.DH_EVENTO_RIM) FROM EVENTO_RIM ER WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'RP'))", "DT_RET_PAG_BAN");
		sql.addProjection("(SELECT U.NM_USUARIO FROM EVENTO_RIM ER, USUARIO U WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_USUARIO = U.ID_USUARIO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO = 'RP'))", "NM_USU_RET_PAG_BAN");
		sql.addProjection("(SELECT TRUNC(ER.DH_EVENTO_RIM) FROM EVENTO_RIM ER WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO IN ('PA', 'PM')))", "DT_PAG");
		sql.addProjection("(SELECT U.NM_USUARIO FROM EVENTO_RIM ER, USUARIO U WHERE ER.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND ER.ID_USUARIO = U.ID_USUARIO AND ER.ID_EVENTO_RIM = (SELECT MAX(ER2.ID_EVENTO_RIM) FROM EVENTO_RIM ER2 WHERE ER2.ID_RECIBO_INDENIZACAO = ER.ID_RECIBO_INDENIZACAO AND ER2.TP_EVENTO_INDENIZACAO IN ('PA', 'PM')))", "NM_USU_PAG");
		sql.addProjection("NVL(cast((	SELECT WM_CONCAT(NFC.NR_NOTA_FISCAL)	FROM RECIBO_INDENIZACAO_NF RNF	,NOTA_FISCAL_CONHECIMENTO NFC	WHERE RNF.ID_DOCTO_SERVICO_INDENIZACAO = DSI.ID_DOCTO_SERVICO_INDENIZACAO	AND RNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO	AND ROWNUM <= 50	) AS varchar2(13)), cast((	SELECT WM_CONCAT(NFC.NR_NOTA_FISCAL)	FROM NOTA_FISCAL_CONHECIMENTO NFC	WHERE NFC.ID_CONHECIMENTO = DSI.ID_DOCTO_SERVICO	AND ROWNUM <= 50	) AS varchar2(13)))", "NR_NOTAS_FISCAIS");
		sql.addProjection("FORIG.SG_FILIAL", "SG_FIL_ORIG");
		sql.addProjection("DS.NR_DOCTO_SERVICO", "NR_DOCTO_SERV");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO') AND VL_VALOR_DOMINIO = DS.TP_DOCUMENTO_SERVICO)", "TP_DOCTO_SERV");
		sql.addProjection("FDEST.SG_FILIAL", "SG_FIL_DEST");
		sql.addProjection("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", "DT_EMI_DS");
		sql.addProjection("DS.DT_PREV_ENTREGA", "DT_PREV_ENT");
		sql.addProjection("(SELECT TRUNC(CAST(MAX(MED.DH_OCORRENCIA) AS DATE)) DH_OCORRENCIA FROM MANIFESTO_ENTREGA_DOCUMENTO MED WHERE MED.ID_OCORRENCIA_ENTREGA = 5 AND MED.TP_SITUACAO_DOCUMENTO <> 'CANC' AND MED.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO)", "DT_ENT");
		sql.addProjection("DS.NR_DIAS_REAL_ENTREGA", "NR_DIAS_REAL_ENT");
		sql.addProjection("DS.VL_IMPOSTO", "VL_IMP_ICMS");
		sql.addProjection("DS.VL_FRETE_LIQUIDO", "VL_FRE_LIQ");
		sql.addProjection("DEV.VL_DEVIDO", "VL_DEV");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_STATUS_COBRANCA_DOCTO_SERVICO') AND VL_VALOR_DOMINIO = DEV.TP_SITUACAO_COBRANCA)", "DS_SIT_COB_FRET");	
		sql.addProjection("DEV.DT_LIQUIDACAO", "DT_LIQ_FRET");
		
		//Remetente
		sql.addProjection("PREM.TP_IDENTIFICACAO", "PREM_TP_IDENT");
		sql.addProjection("PREM.NR_IDENTIFICACAO", "PREM_NR_IDENT");
		sql.addProjection("PREM.NM_PESSOA", "PREM_NM_PESSOA");
		
		//Destinatário
		sql.addProjection("PDES.TP_IDENTIFICACAO", "PDES_TP_IDENT");
		sql.addProjection("PDES.NR_IDENTIFICACAO", "PDES_NR_IDENT");
		sql.addProjection("PDES.NM_PESSOA", "PDES_NM_PESSOA");
		
		//Consignatário
		sql.addProjection("PCONS.TP_IDENTIFICACAO", "PCONS_TP_IDENT");
		sql.addProjection("PCONS.NR_IDENTIFICACAO", "PCONS_NR_IDENT");
		sql.addProjection("PCONS.NM_PESSOA", "PCONS_NM_PESSOA");

		//Redespacho
		sql.addProjection("PREDE.TP_IDENTIFICACAO", "PREDE_TP_IDENT");
		sql.addProjection("PREDE.NR_IDENTIFICACAO", "PREDE_NR_IDENT");
		sql.addProjection("PREDE.NM_PESSOA", "PREDE_NM_PESSOA");
		
		//Devedor
		sql.addProjection("PDEV.TP_IDENTIFICACAO", "PDEV_TP_IDENT");
		sql.addProjection("PDEV.NR_IDENTIFICACAO", "PDEV_NR_IDENT");
		sql.addProjection("PDEV.NM_PESSOA", "PDEV_NM_PESSOA");
		
		//Beneficiário
		sql.addProjection("PBEN.TP_IDENTIFICACAO", "PBEN_TP_IDENT");
		sql.addProjection("PBEN.NR_IDENTIFICACAO", "PBEN_NR_IDENT");
		sql.addProjection("PBEN.NM_PESSOA", "PBEN_NM_PESSOA");
		
		//Favorecido
		sql.addProjection("PFAV.TP_IDENTIFICACAO", "PFAV_TP_IDENT");
		sql.addProjection("PFAV.NR_IDENTIFICACAO", "PFAV_NR_IDENT");
		sql.addProjection("PFAV.NM_PESSOA", "PFAV_NM_PESSOA");
		
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_MODAL') AND VL_VALOR_DOMINIO = S.TP_MODAL)", "DS_MODAL");
		sql.addProjection("(SELECT VI18N(DS_VALOR_DOMINIO_I) FROM VALOR_DOMINIO WHERE ID_DOMINIO IN (SELECT ID_DOMINIO FROM DOMINIO WHERE NM_DOMINIO = 'DM_ABRANGENCIA') AND VL_VALOR_DOMINIO = S.TP_ABRANGENCIA)", "DS_ABRANG");
		sql.addProjection("VI18N(S.DS_SERVICO_I)", "DS_SERVICO");
		
		
		//From´s
		sql.addFrom("RECIBO_INDENIZACAO", "RI");
		sql.addFrom("FILIAL", "FI");
		sql.addFrom("(SELECT * FROM REGIONAL_FILIAL RF WHERE TRUNC(SYSDATE) BETWEEN RF.DT_VIGENCIA_INICIAL AND RF.DT_VIGENCIA_FINAL)", "RF");
		sql.addFrom("REGIONAL", "REG");
		sql.addFrom("DOCTO_SERVICO_INDENIZACAO", "DSI");
		sql.addFrom("DOCTO_SERVICO", "DS");
		sql.addFrom("CONHECIMENTO", "CO");
		sql.addFrom("FILIAL", "FORIG");
		sql.addFrom("FILIAL", "FDEST");
		sql.addFrom("DEVEDOR_DOC_SERV_FAT", "DEV");
		sql.addFrom("DEVEDOR_DOC_SERV", "DEVS");
		sql.addFrom("PESSOA", "PREM");
		sql.addFrom("PESSOA", "PDES");
		sql.addFrom("PESSOA", "PDEV");
		sql.addFrom("PESSOA", "PCONS");
		sql.addFrom("PESSOA", "PREDE");
		sql.addFrom("PESSOA", "PBEN");
		sql.addFrom("PESSOA", "PFAV");
		sql.addFrom("SERVICO", "S");
		sql.addFrom("PRODUTO", "P");
		sql.addFrom("NATUREZA_PRODUTO", "NP");
		sql.addFrom("BANCO", "BA");
		sql.addFrom("AGENCIA_BANCARIA", "AB");
		sql.addFrom("PROCESSO_SINISTRO", "PS");
		sql.addFrom("TIPO_SEGURO", "TS");
		sql.addFrom("TIPO_SINISTRO", "TSIN");
		sql.addFrom("FILIAL", "FSIN");
		sql.addFrom("FILIAL", "FROTA");
		sql.addFrom("OCORRENCIA_NAO_CONFORMIDADE", "ONC");
		sql.addFrom("NAO_CONFORMIDADE", "NCONC");
		sql.addFrom("FILIAL", "FNCONC");
		sql.addFrom("MOTIVO_ABERTURA_NC", "MANC");
		sql.addFrom("LOTE_JDE_RIM", "LJ");

		
		//Join´s
		sql.addJoin("RI.ID_RECIBO_INDENIZACAO", "DSI.ID_RECIBO_INDENIZACAO");
		sql.addJoin("RI.ID_FILIAL", "FI.ID_FILIAL");
		sql.addJoin("FI.ID_FILIAL", "RF.ID_FILIAL(+)");
		sql.addJoin("RF.ID_REGIONAL", "REG.ID_REGIONAL(+)");
		sql.addJoin("DSI.ID_DOCTO_SERVICO", "DS.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "CO.ID_CONHECIMENTO(+)");
		sql.addJoin("DS.ID_FILIAL_ORIGEM", "FORIG.ID_FILIAL(+)");
		sql.addJoin("DS.ID_FILIAL_DESTINO", "FDEST.ID_FILIAL(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DEV.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DS.ID_DOCTO_SERVICO", "DEVS.ID_DOCTO_SERVICO(+)");
		sql.addJoin("DS.ID_CLIENTE_REMETENTE", "PREM.ID_PESSOA(+)");
		sql.addJoin("DS.ID_CLIENTE_DESTINATARIO", "PDES.ID_PESSOA(+)");
		sql.addJoin("DS.ID_CLIENTE_CONSIGNATARIO", "PCONS.ID_PESSOA(+)");
		sql.addJoin("DS.ID_CLIENTE_REDESPACHO", "PREDE.ID_PESSOA(+)");
		sql.addJoin("RI.ID_BENEFICIARIO", "PBEN.ID_PESSOA(+)");
		sql.addJoin("RI.ID_FAVORECIDO", "PFAV.ID_PESSOA(+)");
		sql.addJoin("DEVS.ID_CLIENTE", "PDEV.ID_PESSOA(+)");
		sql.addJoin("DS.ID_SERVICO", "S.ID_SERVICO(+)");
		sql.addJoin("DSI.ID_PRODUTO", "P.ID_PRODUTO(+)");
		sql.addJoin("CO.ID_NATUREZA_PRODUTO", "NP.ID_NATUREZA_PRODUTO(+)");
		sql.addJoin("RI.ID_BANCO", "BA.ID_BANCO(+)");
		sql.addJoin("RI.ID_AGENCIA_BANCARIA", "AB.ID_AGENCIA_BANCARIA(+)");
		sql.addJoin("RI.ID_PROCESSO_SINISTRO", "PS.ID_PROCESSO_SINISTRO(+)");
		sql.addJoin("PS.ID_TIPO_SEGURO", "TS.ID_TIPO_SEGURO(+)");
		sql.addJoin("PS.ID_TIPO_SINISTRO", "TSIN.ID_TIPO_SINISTRO(+)");
		sql.addJoin("DSI.ID_FILIAL_SINISTRO", "FSIN.ID_FILIAL(+)");
		sql.addJoin("DSI.ID_ROTA_SINISTRO", "FROTA.ID_FILIAL(+)");
		sql.addJoin("DSI.ID_OCORRENCIA_NAO_CONFORMIDADE", "ONC.ID_OCORRENCIA_NAO_CONFORMIDADE(+)");
		sql.addJoin("ONC.ID_NAO_CONFORMIDADE", "NCONC.ID_NAO_CONFORMIDADE(+)");
		sql.addJoin("NCONC.ID_FILIAL", "FNCONC.ID_FILIAL(+)");
		sql.addJoin("ONC.ID_MOTIVO_ABERTURA_NC", "MANC.ID_MOTIVO_ABERTURA_NC(+)");
		sql.addJoin("RI.ID_LOTE_JDE_RIM", "LJ.ID_LOTE_JDE_RIM(+)");

		//Order by
		sql.addOrderBy("REG.SG_REGIONAL");
		sql.addOrderBy("REG.DS_REGIONAL");
		sql.addOrderBy("FI.SG_FILIAL");
		sql.addOrderBy("RI.NR_RECIBO_INDENIZACAO");

		
		// Critérios

		// Regional
		sql.addCriteria("REG.ID_REGIONAL", "=", tfm.getLong("regional.idRegionalFilial"));

		// Data de emissão
		sql.addCustomCriteria("(RI.DT_EMISSAO BETWEEN  ? AND ? ) ");
		sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoInicial"));
		sql.addCriteriaValue(tfm.getYearMonthDay("dtEmissaoFinal"));

		// Filial
		sql.addCriteria("RI.ID_FILIAL", "=", tfm.getLong("filial.idFilial"));

		// Data de liberação de pagamento
		sql.addCriteria("RI.DT_LIBERACAO_PAGAMENTO", ">=", tfm.getYearMonthDay("dataLiberacaoPgtoInicial"));
		sql.addCriteria("RI.DT_LIBERACAO_PAGAMENTO", "<=", tfm.getYearMonthDay("dataLiberacaoPgtoFinal"));
		
		// Número do recibo de indenização
		sql.addCriteria("RI.NR_RECIBO_INDENIZACAO", ">=", tfm.getInteger("numeroInicial"));
		sql.addCriteria("RI.NR_RECIBO_INDENIZACAO", "<=", tfm.getInteger("numeroFinal"));
				
		// Data de programada de pagamento
		sql.addCriteria("RI.DT_PROGRAMADA_PAGAMENTO", ">=", tfm.getYearMonthDay("dataProgramadaPagamentoInicial"));
		sql.addCriteria("RI.DT_PROGRAMADA_PAGAMENTO", "<=", tfm.getYearMonthDay("dataProgramadaPagamentoFinal"));
		
		// Tipo de indenização
		sql.addCriteria("RI.TP_INDENIZACAO", "=", tfm.getString("tpIndenizacao"));

		// Data de pagamento
		sql.addCriteria("RI.DT_PAGAMENTO_EFETUADO", ">=", tfm.getYearMonthDay("dataPagamentoInicial"));
		sql.addCriteria("RI.DT_PAGAMENTO_EFETUADO", "<=", tfm.getYearMonthDay("dataPagamentoFinal"));
		
		// Status
		sql.addCriteria("RI.TP_STATUS_INDENIZACAO", "=", tfm.getString("tpStatusIndenizacao"));
		
		// Data análise NC
		YearMonthDay dataAnaliseNcInicial = tfm.getYearMonthDay("dataAnaliseNcInicial");
		YearMonthDay dataAnaliseNcFinal = tfm.getYearMonthDay("dataAnaliseNcFinal");
		
		if(dataAnaliseNcInicial != null || dataAnaliseNcFinal != null){
			StringBuilder criterioDataAnaliseNc = new StringBuilder("RI.ID_PENDENCIA IS NOT NULL AND EXISTS (SELECT 1 FROM ACAO A, INTEGRANTE I WHERE A.ID_INTEGRANTE = I.ID_INTEGRANTE ");
			criterioDataAnaliseNc.append("AND A.ID_PENDENCIA = RI.ID_PENDENCIA AND (I.ID_USUARIO IN (SELECT PU.ID_USUARIO FROM PERFIL_USUARIO PU ");
			criterioDataAnaliseNc.append("WHERE PU.ID_PERFIL IN (" + idsPerfisAnalistasNc +")) OR "); 
			criterioDataAnaliseNc.append("I.ID_PERFIL IN (" + idsPerfisAnalistasNc +")) AND A.TP_SITUACAO_ACAO = 'A' ");
			
			if(dataAnaliseNcInicial != null){
				criterioDataAnaliseNc.append("AND TRUNC(A.DH_ACAO) >= ? "); 
				sql.addCriteriaValue(dataAnaliseNcInicial);
			}
			
			if(dataAnaliseNcFinal != null){
				criterioDataAnaliseNc.append("AND TRUNC(A.DH_ACAO) <= ? "); 
				sql.addCriteriaValue(dataAnaliseNcFinal);
			}
			
			criterioDataAnaliseNc.append("AND ROWNUM = 1) ");

			sql.addCustomCriteria(criterioDataAnaliseNc.toString());
		}
		
		// Situação da aprovação
		sql.addCriteria("RI.TP_SITUACAO_WORKFLOW", "=", tfm.getString("situacaoAprovacao"));
		
		// Usuário análise NC
		if(StringUtils.isNotBlank(idUsuarioNc)){
			sql.addCustomCriteria("RI.ID_PENDENCIA IS NOT NULL AND EXISTS (SELECT 1 FROM ACAO A, INTEGRANTE I WHERE A.ID_INTEGRANTE = I.ID_INTEGRANTE AND A.ID_PENDENCIA = RI.ID_PENDENCIA AND I.ID_USUARIO = ? AND A.TP_SITUACAO_ACAO = 'A' AND ROWNUM = 1)");
			sql.addCriteriaValue(idUsuarioNc);
		}

		// Natureza do Produto
		sql.addCriteria("NP.ID_NATUREZA_PRODUTO", "=", tfm.getLong("naturezaProduto.idNaturezaProduto"));

		// Data lote
		sql.addCriteria("TRUNC(LJ.DH_LOTE_JDE_RIM)", ">=", tfm.getYearMonthDay("dataLoteInicial"));
		sql.addCriteria("TRUNC(LJ.DH_LOTE_JDE_RIM)", "<=", tfm.getYearMonthDay("dataLoteFinal"));

		// Forma de pagamento
		sql.addCriteria("RI.TP_FORMA_PAGAMENTO", "=", tfm.getString("formaPagamento"));

		// Número do lote
		sql.addCriteria("LJ.ID_LOTE_JDE_RIM", "=", tfm.getLong("nrLoteJdeRim"));

		// Número do processo sinistro
		sql.addCriteria("PS.ID_PROCESSO_SINISTRO", "=", tfm.getLong("processoSinistro.idProcessoSinistro"));

		// Filial RNC
		sql.addCriteria("NCONC.ID_FILIAL", "=", tfm.getLong("filialRnc.idFilial"));

		// Tipo de seguro
		sql.addCriteria("TS.ID_TIPO_SEGURO", "=", tfm.getLong("tipoSeguro.idTipoSeguro"));

		// Número RNC
		sql.addCriteria("NCONC.NR_NAO_CONFORMIDADE", ">=", tfm.getInteger("numeroRncInicial"));
		sql.addCriteria("NCONC.NR_NAO_CONFORMIDADE", "<=", tfm.getInteger("numeroRncFinal"));

		// Tipo de sinistro
		sql.addCriteria("TSIN.ID_TIPO_SINISTRO", "=", tfm.getLong("tipoSinistro.idTipo"));

		// Data emissão RNC
		sql.addCriteria("TRUNC(NCONC.DH_EMISSAO)", ">=", tfm.getYearMonthDay("dataEmissaoRncInicial"));
		sql.addCriteria("TRUNC(NCONC.DH_EMISSAO)", "<=", tfm.getYearMonthDay("dataEmissaoRncFinal"));

		// Salvados
		sql.addCriteria("RI.BL_SALVADOS", "=", tfm.getString("blSalvados"));

		// Motivo da abertura
		sql.addCriteria("MANC.ID_MOTIVO_ABERTURA_NC", "=", tfm.getLong("motivoAbertura.idMotivoAberturaNc"));

		// Valor indenização
		sql.addCriteria("RI.VL_INDENIZACAO", ">=", tfm.getBigDecimal("valorIndenizacaoInicial"));
		sql.addCriteria("RI.VL_INDENIZACAO", "<=", tfm.getBigDecimal("valorIndenizacaoFinal"));

		// Filial ocorrência 1
		sql.addCriteria("DSI.ID_FILIAL_SINISTRO", "=", tfm.getLong("filialOcorrencia1.idFilial"));

		// Filial debitada
		String idFilialDebitada = tfm.getString("filialDebitada.idFilial");
		if(StringUtils.isNotBlank(idFilialDebitada)){
			sql.addCustomCriteria("EXISTS (SELECT 1 FROM FILIAL_DEBITADA FD WHERE FD.ID_RECIBO_INDENIZACAO = RI.ID_RECIBO_INDENIZACAO AND FD.ID_FILIAL = ? AND ROWNUM = 1)");
			sql.addCriteriaValue(idFilialDebitada);
		}
		
		// Filial ocorrência 2
		sql.addCriteria("DSI.ID_ROTA_SINISTRO", "=", tfm.getLong("filialOcorrencia2.idFilial"));

		// Beneficiário
		sql.addCriteria("PBEN.ID_PESSOA", "=", tfm.getLong("beneficiario.idPessoa"));

		// Favorecido
		sql.addCriteria("PFAV.ID_PESSOA", "=", tfm.getLong("favorecido.idPessoa"));

		// Documento de serviço
		sql.addCriteria("DS.ID_DOCTO_SERVICO", "=", tfm.getLong("doctoServico.idDoctoServico"));

		// Data de emissão do documento
		sql.addCriteria("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", ">=", tfm.getYearMonthDay("dtEmissaoDocInicial"));
		sql.addCriteria("TRUNC(CAST(DS.DH_EMISSAO AS DATE))", "<=", tfm.getYearMonthDay("dtEmissaoDocFinal"));

		// Remetente
		sql.addCriteria("PREM.ID_PESSOA", "=", tfm.getLong("remetente.idPessoa"));

		// Destinatário
		sql.addCriteria("PDES.ID_PESSOA", "=", tfm.getLong("destinatario.idPessoa"));

		// Devedor
		sql.addCriteria("PDEV.ID_PESSOA", "=", tfm.getLong("devedor.idPessoa"));

		// Modal
		sql.addCriteria("S.TP_MODAL", "=", tfm.getString("modal"));

		// Abrangência
		sql.addCriteria("S.TP_ABRANGENCIA", "=", tfm.getString("abrangencia"));

		// Serviço
		sql.addCriteria("S.ID_SERVICO", "=", tfm.getLong("servico.idServico"));
		
		return sql;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}