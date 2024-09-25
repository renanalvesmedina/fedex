package com.mercurio.lms.carregamento.report;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import org.joda.time.YearMonthDay;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */

public abstract class EmitirRelatorioCargasService extends ReportServiceSupport {

	private static final String SIM = "Sim";
	private ConversaoMoedaService conversaoMoedaService;
	private EnderecoPessoaService enderecoPessoaService;

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	/**
	 * @param tfm
	 * @param criterias
	 * @param sqlTemplate
	 * @return
	 */
	protected String populateCriterios(TypedFlatMap tfm, List criterias, SqlTemplate sqlTemplate) {

		StringBuilder sql = new StringBuilder();

		if (tfm.getBoolean("blAguardandoAgendamento")) {
			sqlTemplate.addFilterSummary("aguardandoAgendamento", SIM);
		}

		if (isAgendamento(tfm)) {
			sqlTemplate.addFilterSummary("documentosComAgendamento", SIM);
		}

		// Filial Localizao
		Long idFilialLocalizacao = tfm.getLong("filialLocalizacao.idFilial");
		if (idFilialLocalizacao != null) {
			sql.append("AND ds.ID_FILIAL_LOCALIZACAO = ? ");
			criterias.add(idFilialLocalizacao);
			sqlTemplate.addFilterSummary("filialLocalizacao", tfm.getString("filialLocalizacao.sgFilial"));
		}

		// Filial Destino
		Long idFilialDestino = tfm.getLong("filialDestino.idFilial");
		if (idFilialDestino != null) {
			if (!idFilialDestino.equals(idFilialLocalizacao)) {
				sql.append("and EXISTS ( ");
				sql.append("select 1 from fluxo_filial fluxofilial, ordem_filial_fluxo off1, ordem_filial_fluxo off2 ");
				sql.append("where off1.id_fluxo_filial = fluxofilial.id_fluxo_filial ");
				sql.append("and off2.id_fluxo_filial = fluxofilial.id_fluxo_filial ");
				sql.append("and off1.id_filial = " + SessionUtils.getFilialSessao().getIdFilial().toString());
				sql.append(" and off2.id_filial = " + idFilialDestino.toString());
				sql.append(" and off1.nr_ordem < off2.nr_ordem ");
				sql.append(" and fluxofilial.id_fluxo_filial = ff.id_fluxo_filial ) ");
			} else {
				sql.append(" and ds.ID_FILIAL_DESTINO = ? ");
				criterias.add(idFilialDestino);
			}
			sqlTemplate.addFilterSummary("destino",
					tfm.getString("filialDestino.sgFilial"));
		}

		// Localizao Mercadoria
		Long cdLocalizacaoMercadoria = tfm.getLong("localizacaoMercadoria.cdLocalizacaoMercadoria");
		if (cdLocalizacaoMercadoria == null) {
			sql.append("and lm.CD_LOCALIZACAO_MERCADORIA IN (24, 28, 35, 43, 5, 7, 4, 6) ");

		}
		if (cdLocalizacaoMercadoria != null) {
			if (cdLocalizacaoMercadoria.doubleValue() != 24) {
				sql.append("and lm.CD_LOCALIZACAO_MERCADORIA = ? ");
				criterias.add(cdLocalizacaoMercadoria);
			}
			sqlTemplate.addFilterSummary(
					"localizacao", tfm.getVarcharI18n("localizacaoMercadoria.dsLocalizacaoMercadoria").toString());
		} else {
			sql.append("");
		}

		// Dias
		Integer nrDiaInicial = tfm.getInteger("diaInicial");
		Integer nrDiaFinal = tfm.getInteger("diaFinal");
		if (nrDiaInicial != null && nrDiaFinal != null) {
			sqlTemplate.addFilterSummary("diasTitulo", nrDiaInicial.toString()
					+ " - " + nrDiaFinal.toString());
		} else if (nrDiaInicial != null) {
			sqlTemplate.addFilterSummary("diasTitulo", nrDiaInicial);
		}

		// Filial Origem
		Long idFilialOrigem = tfm.getLong("filialOrigem.idFilial");
		if (idFilialOrigem != null) {
			sql.append("and ds.ID_FILIAL_ORIGEM = ? ");
			criterias.add(idFilialOrigem);
			sqlTemplate.addFilterSummary("origem", tfm.getString("filialOrigem.sgFilial"));
		}

		// Rota Entrega
		Long idRotaColetaEntrega = tfm
				.getLong("rotaColetaEntrega.idRotaColetaEntrega");
		if (idRotaColetaEntrega != null) {
			sql.append("and ((ds.ID_ROTA_COLETA_ENTREGA_REAL IS NULL AND ds.ID_ROTA_COLETA_ENTREGA_SUGERID = ? )");
			sql.append("or (ds.ID_ROTA_COLETA_ENTREGA_REAL = ? ))");
			criterias.add(idRotaColetaEntrega);
			criterias.add(idRotaColetaEntrega);
			sqlTemplate.addFilterSummary("rotaEntrega", tfm.getString("dsRota"));
		}

		// Remetente
		Long idClienteRemetente = tfm.getLong("clienteRemetente.idCliente");
		if (idClienteRemetente != null) {
			sql.append("and ds.ID_CLIENTE_REMETENTE = ? ");
			criterias.add(idClienteRemetente);
			sqlTemplate.addFilterSummary("remetente", tfm.getString("clienteRemetente.pessoa.nmPessoa"));
		}

		// Tipo Destinatrio
		String tpDestinatario = tfm.getString("tpDestinatario");
		if (tpDestinatario != null && !tpDestinatario.equals("")) {
			sql.append("and pessoaDestinatario.TP_PESSOA = ? ");
			criterias.add(tpDestinatario);
			sqlTemplate.addFilterSummary("tipoDestinatario", tfm.getString("dsTpDestinatario"));
		}

		// Destinatrio
		Long idClienteDestinatario = tfm
				.getLong("clienteDestinatario.idCliente");
		if (idClienteDestinatario != null) {
			sql.append("and ds.ID_CLIENTE_DESTINATARIO = ? ");
			criterias.add(idClienteDestinatario);
			sqlTemplate.addFilterSummary("destinatario", tfm.getString("clienteDestinatario.pessoa.nmPessoa"));
		}

		// Servio
		Long idServico = tfm.getLong("servico.idServico");
		if (idServico != null) {
			sql.append("and ds.ID_SERVICO = ? ");
			criterias.add(idServico);
			sqlTemplate.addFilterSummary("servico", tfm.getString("servico.dsServico"));
		}

		if (tfm.getString("agrupadoPor") != null && !"".equals(tfm.getString("agrupadoPor"))) {
			sqlTemplate.addFilterSummary("agrupadoPor", tfm.getString("dsAgrupadoPor"));
		}

		return sql.toString();
	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_DOCUMENTO_SERVICO", "DM_TIPO_DOCUMENTO_SERVICO");
		config.configDomainField("TP_CARGA_DOCUMENTO", "DM_TP_CARGA_DOCUMENTO");
		config.configDomainField("TP_FRETE", "DM_TIPO_FRETE");
	}

	public BigDecimal executeRetornaValorConvertido(Long idFilial,
													Long idMoedaOrigem, BigDecimal valor, Long idMoedaDestino) {
		if (valor == null)
			return BigDecimalUtils.ZERO;

		Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		// FIXME Pode ser que no seja necessrio buscar o endereo toda a vez q
		// se faz a converso.
		EnderecoPessoa enderecoPessoaPadrao = enderecoPessoaService.findEnderecoPessoaPadrao(idFilial);
		Long idPaisOrigem = enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais();

		return conversaoMoedaService.findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino, dataAtual, valor);
	}

	/**
	 * Seta os parametros que iro no cabealho da pgina e os parametros de
	 * pesquisa.
	 *
	 * @param tfm
	 * @return
	 */

	protected void populateCriterioDias(StringBuilder sql, List criterias,
										TypedFlatMap tfm) {
		Integer nrDiaInicial = tfm.getInteger("diaInicial");
		Integer nrDiaFinal = tfm.getInteger("diaFinal");
		if (nrDiaInicial != null && nrDiaFinal != null) {
			sql.append("WHERE NR_DIAS between ? and ? ");
			criterias.add(nrDiaInicial);
			criterias.add(nrDiaFinal);
		} else if (nrDiaInicial != null) {
			sql.append("WHERE NR_DIAS >= ? ");
			criterias.add(nrDiaInicial);
		} else if (nrDiaFinal != null) {
			sql.append("WHERE NR_DIAS <= ? ");
			criterias.add(nrDiaFinal);
		}
	}

	/**
	 * Seta os parametros que ir?o no cabe?alho da p?gina e os parametros de
	 * pesquisa.
	 *
	 * @param tfm
	 * @return
	 */
	protected Map getParametrosRelatorio(TypedFlatMap tfm, SqlTemplate sqlTemplate) {
		Map parametersReport = new HashMap();
		parametersReport.put("blRoteirizacao", tfm.getBoolean("blRoteirizacao", false));
		parametersReport.put("blDocumentosComAgendamento", tfm.getBoolean("blDocumentosComAgendamento", false));
		parametersReport.put("blDocumentosComProdutoPerigoso", tfm.getBoolean("blDocumentosComProdutoPerigoso", false));
		parametersReport.put("blDocumentosComProdutoControlado", tfm.getBoolean("blDocumentosComProdutoControlado", false));
		parametersReport.put("idMoedaTela", tfm.getLong("moeda.idMoeda"));
		parametersReport.put("siglaSimboloMoedaTela", tfm.getString("moeda.siglaSimbolo"));
		parametersReport.put("parametrosPesquisa", sqlTemplate.getFilterSummary());
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		parametersReport.put("filialUsuarioEmissor", SessionUtils.getFilialSessao().getSgFilial());
		parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, tfm.getString("formatoRelatorio"));

		return parametersReport;
	}

	protected StringBuilder getDocumentosComAgendamentoFilter(TypedFlatMap tfm) {
		StringBuilder sql = new StringBuilder();

		if ( "24".equals(tfm.getString("localizacaoMercadoria.cdLocalizacaoMercadoria"))){
			sql.append(" AND ds.id_localizacao_mercadoria in (123,128,135,164) ");
			if(isAgendamento(tfm)) {
				sql.append(" AND ads.id_docto_servico = ds.id_docto_servico ")
						.append(" AND ads.ID_AGENDAMENTO_ENTREGA = ae.ID_AGENDAMENTO_ENTREGA ")
						.append(" AND ae.TP_SITUACAO_AGENDAMENTO = 'A' ");
			} else {
				sql.append(" AND NOT EXISTS (SELECT * from AGENDAMENTO_DOCTO_SERVICO ads, AGENDAMENTO_ENTREGA ae ")
						.append(" WHERE ads.id_docto_servico = ds.id_docto_servico ")
						.append(" AND ads.id_agendamento_entrega = ae.id_agendamento_entrega ")
						.append(" AND ae.tp_situacao_agendamento = 'A') ");
			}
		}

		return sql;
	}

	protected StringBuilder getAguardandoAgendamentoFilter(TypedFlatMap tfm) {
		StringBuilder sql = new StringBuilder();

		if (tfm.getBoolean("blAguardandoAgendamento")) {
			sql.append(" AND ods.id_docto_servico = ds.id_docto_servico ")
					.append(" AND ods.ID_OCOR_BLOQUEIO = op.ID_OCORRENCIA_PENDENCIA ");
			sql.append(" AND op.CD_OCORRENCIA = 119 ");
			sql.append(" AND ds.id_localizacao_mercadoria in (103) ");
			sql.append(" AND ods.DH_LIBERACAO IS NULL ");
		} else if ("5".equals(tfm.getString("localizacaoMercadoria.cdLocalizacaoMercadoria"))) {
			sql.append(" AND ods.id_docto_servico = ds.id_docto_servico ")
					.append(" AND ods.ID_OCOR_BLOQUEIO = op.ID_OCORRENCIA_PENDENCIA ");
			sql.append(" AND op.CD_OCORRENCIA != 119 ")
					.append(" AND ds.id_localizacao_mercadoria in (103) ");
			sql.append(" AND ods.DH_LIBERACAO IS NULL ");
		}

		return sql;
	}

	protected void getProdutoControladoField(TypedFlatMap tfm, StringBuilder sql) {
		if (tfm.getBoolean("blDocumentosComProdutoControlado")) {
			sql.append("conhecimento.BL_PRODUTO_CONTROLADO, ");
		} else {
			sql.append("NULL as BL_PRODUTO_CONTROLADO, ");
		}
	}

	protected String getAguardandoAgendamentoTables(TypedFlatMap tfm) {
		if ("5".equals(tfm.getString("localizacaoMercadoria.cdLocalizacaoMercadoria"))) {
			return " OCORRENCIA_DOCTO_SERVICO ods, OCORRENCIA_PENDENCIA op, ";
		}
		return "";
	}

	protected String getDocumentosComAgendamentoTables(TypedFlatMap tfm) {
		if (isAgendamento(tfm)) {
			return " AGENDAMENTO_DOCTO_SERVICO ads, AGENDAMENTO_ENTREGA ae, ";
		}
		return "";
	}

	protected void getDtAgendamentoField(TypedFlatMap tfm, StringBuilder sql) {
		if (isAgendamento(tfm)) {
			sql.append("ae.DT_AGENDAMENTO, ");
		} else {
			sql.append("NULL as DT_AGENDAMENTO, ");
		}
	}

	protected void getProdutoPerigosoField(TypedFlatMap tfm, StringBuilder sql) {
		if (tfm.getBoolean("blDocumentosComProdutoPerigoso")) {
			sql.append("conhecimento.BL_PRODUTO_PERIGOSO, ");
		} else {
			sql.append("NULL as BL_PRODUTO_PERIGOSO, ");
		}
	}

	protected Boolean isAgendamento(TypedFlatMap tfm) {
		return tfm.getBoolean("blDocumentosComAgendamento");
	}

	protected Boolean isRoteirizacao(TypedFlatMap tfm) {
		return tfm.getBoolean("blRoteirizacao");
	}
}
