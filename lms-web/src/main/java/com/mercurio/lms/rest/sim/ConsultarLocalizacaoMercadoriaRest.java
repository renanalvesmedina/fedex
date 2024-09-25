package com.mercurio.lms.rest.sim;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.ListToMapConverter;
import com.mercurio.adsm.framework.util.RowMapper;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.ObservacaoMercadoria;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.TipoServico;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.TipoServicoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.service.ConsultarDadosCobrancaDocumentoServicoService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.CtoCtoCooperadaService;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ImpostoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalCtoCooperadaService;
import com.mercurio.lms.expedicao.model.service.ObservacaoDoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ParcelaCtoCooperadaService;
import com.mercurio.lms.expedicao.model.service.ParcelaDoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ServicoEmbalagemService;
import com.mercurio.lms.expedicao.model.service.TrackingAwbService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.AwbUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.EventoRimService;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.service.PpdReciboService;
import com.mercurio.lms.prestcontasciaaerea.model.FaturaCiaAerea;
import com.mercurio.lms.prestcontasciaaerea.model.service.FaturaCiaAereaService;
import com.mercurio.lms.rest.json.YearMonthDayDeserializer;
import com.mercurio.lms.rest.sim.dto.ManifestoColetaDTO;
import com.mercurio.lms.rest.utils.Closure;
import com.mercurio.lms.rest.utils.ListResponseBuilder;
import com.mercurio.lms.rest.vendas.dto.InformacaoDoctoClienteDTO;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.rnc.report.EmitirRNCService;
import com.mercurio.lms.sim.model.service.ClienteUsuarioCCTService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.ObservacaoMercadoriaService;
import com.mercurio.lms.sim.report.EmitirLocalizacaoMercadoriaService;
import com.mercurio.lms.sim.report.EmitirRelatorioDadosVolumesDocumentoService;
import com.mercurio.lms.tabelaprecos.model.TarifaSpot;
import com.mercurio.lms.tabelaprecos.model.service.TarifaSpotService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;

@Path("/sim/consultarLocalizacaoMercadoria")
public class ConsultarLocalizacaoMercadoriaRest extends BaseRest{

	private static final String REMETENTE = "remetente";
	private static final String DESTINATARIO = "destinatario";
	private static final String RESPONSAVEL_FRETE = "responsavelFrete";
	private static final String LOCALIZACAO_MERCADORIA = "localizacao-mercadoria";
	private static final String NR_IDENTIFICACAO = "nrIdentificacao";
	private static final String ID_DOCTO_SERVICO = "idDoctoServico";
	private static final String ID_RECIBO_INDENIZACAO = "idReciboIndenizacao";
	private static final String VL_TOTAL_SERVICOS = "vlTotalServicos";
	private static final String VL_TOTAL_DOC_SERVICO = "vlTotalDocServico";
	private static final String VL_ICMSST = "vlICMSST";
	private static final String TP_IDENTIFICACAO = "tpIdentificacao";
	private static final String TP_IDENTIFICACAO_REM = "tpIdentificacaoRem";
	private static final String NR_IDENTIFICACAO_REM = "nrIdentificacaoRem";
	private static final String TP_IDENTIFICACAO_DEST = "tpIdentificacaoDest";
	private static final String NR_IDENTIFICACAO_DEST = "nrIdentificacaoDest";
	private static final String NR_IDENTIFICACAO_REDES = "nrIdentificacaoRedes";
	private static final String TP_IDENTIFICACAO_REDES = "tpIdentificacaoRedes";
	private static final String ID_CLIENTE = "idCliente";
	private static final String ID_REGISTRO = "idRegistro";
	private static final String DH_BAIXA = "dhBaixa";
	private static final String DT_AGENDAMENTO = "dtAgendamento";
	private static final String TP_AGENDAMENTO = "tpAgendamento";
	private static final String TP_SITUACAO_AGENDAMENTO = "tpSituacaoAgendamento";
	private static final String DS_TURNO = "dsTurno";
	private static final String TP_CONHECIMENTO = "tpConhecimento";
	private static final String ID_CONHECIMENTO = "idConhecimento";
	private static final String HR_PREFERENCIA_INICIAL = "hrPreferenciaInicial";
	private static final String HR_PREFERENCIA_FINAL = "hrPreferenciaFinal";
	private static final String PS_AFERIDO = "psAferido";
	private static final String DT_PREV_ENTREGA = "dtPrevEntrega";
	private static final String DT_ENTREGA = "dtEntrega";
	private static final String DT_EMISSAO = "dtEmissao";
	private static final String DH_EMISSAO = "dhEmissao";
	private static final String DT_SAIDA = "dtSaida";
	private static final String DT_VENCIMENTO = "dtVencimento";
	private static final String DT_LIQUIDACAO = "dtLiquidacao";
	private static final String CURRENT_PAGE = "_currentPage";
	private static final String PAGE_SIZE = "_pageSize";
	private static final String DH_INCLUSAO = "dhInclusao";
	private static final String TP_SCAN = "tpScan";
	private static final String DH_ALTERACAO = "dhAlteracao";
	private static final String NM_PESSOA_REM = "nmPessoaRem";
	private static final String NM_PESSOA_DEST = "nmPessoaDest";
	private static final String NM_PESSOA_REDES = "nmPessoaRedes";
	private static final String NM_PESSOA = "nmPessoa";
	private static final String TIPO_CLIENTE = "tipoCliente";
	private static final String MUNICIPIO = "municipio";
	private static final String MUNICIPIO_REM = "municipioRem";
	private static final String MUNICIPIO_DEST = "municipioDest";
	private static final String MUNICIPIO_REDES = "municipioRedes";
	private static final String MUNICIPIO_DEV = "municipioDev";
	private static final String NM_PAIS = "nmPais";
	private static final String NR_TELEFONE = "nrTelefone";
	private static final String NR_DDD = "nrDdd";
	private static final String DH_CONTATO = "dhContato";
	private static final String DS_ENDERECO = "dsEndereco";
	private static final String TP_ENDERECO = "tpEndereco";
	private static final String POPUP = "popup";
	private static final String POPUP_GIF = "/image/popup.gif";
	private static final String TP_MODAL = "tpModal";
	private static final String VL_FRETE = "vlFrete";
	private static final String VL_FRETE_EXTERNO = "vlFreteExterno";
	private static final String VL_TOTAL = "vlTotal";
	private static final String VL_TOTAL_PARCELAS = "vlTotalParcelas";
	private static final String VL_PARCELA = "vlParcela";
	private static final String VL_DESCONTO = "vlDesconto";
	private static final String VALOR_MERCADORIA = "valorMercadoria";
	private static final String VL_MERCADORIA = "vlMercadoria";
	private static final String VL_MERCADORIA_I = "vlMercadoriaI";
	private static final String VL_LIQUIDO = "vlLiquido";
	private static final String VALOR_MERCADORIA_REEMB = "vlMercadoriaReemb";
	private static final String TP_SITUACAO_COBRANCA = "tpSituacaoCobranca";
	private static final String BL_CARTAO = "blCartao";
	private static final String DS_SIMBOLO = "dsSimbolo";
	private static final String SG_MOEDA = "sgMoeda";
	private static final String SG_FILIAL = "sgFilial";
	private static final String DT_VIGENCIA_INICIAL = "dtVigenciaInicial";
	private static final String DT_VIGENCIA_FINAL = "dtVigenciaFinal";
	private static final String ANALISE = "analise";
	private static final String FILE_NAME = "fileName";
	private static final String TP_MOTIVO_DESCONTO = "tpMotivoDesconto";
	private static final String DT_RECIBO = "dtRecibo";
	private static final String DT_PROGRAMADA_PAGTO = "dtProgramadaPagto";
	private static final String DT_PAGAMENTO_EFETUADO = "dtpagamentoEfetuado";
	private static final String TP_SITUACAO_APROVACAO = "tpSituacaoAprovacao";

	private static final String BIG_DECIMAL = "#,###,###,###,##0.00";
	private static final String BIG_DECIMAL_TRES_CASAS = "#,###,###,###,##0.000";
	private static final String UM = "1";
	private static final String SIM = "Sim";
	private static final String NAO = "Não";
	private static final String CONTENT_TYPE_ZIP = "application/zip";

	private static final String PROPERTY_URL_IMAGEM_DIGITAL = "url.imagem.digital";
	@InjectInJersey DoctoServicoService doctoServicoService;
	@InjectInJersey TipoServicoService tipoServicoService;
	@InjectInJersey PpdReciboService ppdReciboService;
	@InjectInJersey DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	@InjectInJersey NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	@InjectInJersey VolumeNotaFiscalService volumeNotaFiscalService;
	@InjectInJersey EventoVolumeService eventoVolumeService;
	@InjectInJersey ConfiguracoesFacade configuracoesFacade;
	@InjectInJersey DevedorDocServFatService devedorDocServFatService;
	@InjectInJersey EnderecoPessoaService enderecoPessoaService;
	@InjectInJersey TelefoneEnderecoService telefoneEnderecoService;
	@InjectInJersey ObservacaoDoctoServicoService observacaoDoctoServicoService;
	@InjectInJersey ServicoEmbalagemService servicoEmbalagemService;
	@InjectInJersey DadosComplementoService dadosComplementoService;
	@InjectInJersey AgendamentoEntregaService agendamentoEntregaService;
	@InjectInJersey ConhecimentoService conhecimentoService;
	@InjectInJersey ReportExecutionManager reportExecutionManager;
	@InjectInJersey EmitirLocalizacaoMercadoriaService emitirLocalizacaoMercadoriaService;
	@InjectInJersey EmitirRelatorioDadosVolumesDocumentoService emitirRelatorioDadosVolumesDocumentoService;
	@InjectInJersey CtoCtoCooperadaService ctoCtoCooperadaService;
	@InjectInJersey NotaFiscalCtoCooperadaService notaFiscalCtoCooperadaService;
	@InjectInJersey ParcelaCtoCooperadaService parcelaCtoCooperadaService;
	@InjectInJersey ControleCargaService controleCargaService;
	@InjectInJersey ManifestoViagemNacionalService manifestoViagemNacionalService;
	@InjectInJersey ManifestoEntregaService manifestoEntregaService;
	@InjectInJersey ManifestoColetaService manifestoColetaService;
	@InjectInJersey EventoDocumentoServicoService eventoDocumentoServicoService;
	@InjectInJersey OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	@InjectInJersey ParcelaDoctoServicoService parcelaDoctoServicoService;
	@InjectInJersey ConsultarDadosCobrancaDocumentoServicoService consultarDadosCobrancaDocumentoServicoService;
	@InjectInJersey NaoConformidadeService naoConformidadeService;
	@InjectInJersey OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	@InjectInJersey ImpostoServicoService impostoServicoService;
	@InjectInJersey EmitirRNCService emitirRNCService;
	@InjectInJersey PreManifestoDocumentoService preManifestoDocumentoService;
	@InjectInJersey EventoRimService eventoRimService;
	@InjectInJersey ClienteUsuarioCCTService clienteUsuarioCCTService;
	@InjectInJersey ClienteService clienteService;
	@InjectInJersey CtoAwbService ctoAwbService;
	@InjectInJersey AwbService awbService;
	@InjectInJersey FaturaCiaAereaService faturaCiaAereaService;
	@InjectInJersey TarifaSpotService tarifaSpotService;
	@InjectInJersey TrackingAwbService trackingAwbService;
	@InjectInJersey DetalheColetaService detalheColetaService;
	@InjectInJersey InformacaoDoctoClienteService informacaoDoctoClienteService;
	@InjectInJersey DomainValueService domainValueService;
	@InjectInJersey ObservacaoMercadoriaService observacaoMercadoriaService;
	@InjectInJersey UsuarioService usuarioService;
	@InjectInJersey NotaFiscalOperadaService notaFiscalOperadaService;
	@InjectInJersey	OcorrenciaEntregaService ocorrenciaEntregaService;

	@POST
	@Path(value="findDocumentos")
	public Response findDocumentos(FiltroPaginacaoDto filtro) {
		Integer limiteRegistros = ListResponseBuilder.getLimiteRegistros(filtro.getReport());
		final TypedFlatMap tfm = filtro.getTypedFlatMapWithPaginationInfo(limiteRegistros);

		tfm.put("empresa.idEmpresa", SessionUtils.getEmpresaSessao().getIdEmpresa());

		tfm.put("awb.idAwb", tfm.remove("awb"));
		tfm.put("pedidoColeta.idPedidoColeta", tfm.remove("pedidoColeta"));
		tfm.put("cotacao.idCotacao", tfm.remove("cotacao"));
		tfm.put("filialOrigem.idFilial", tfm.remove("filialOrigem"));
		tfm.put("filialDestino.idFilial", tfm.remove("filialDestino"));
		tfm.put("tipoServico.idTipoServico", tfm.remove("tipoServico"));
		tfm.put("remetente.idCliente", tfm.remove(REMETENTE));
		tfm.put("informacaoDoctoCliente.idInformacaoDoctoCliente", tfm.remove("idInformacaoDoctoCliente"));
		tfm.put("destinatario.idCliente", tfm.remove(DESTINATARIO));
		tfm.put("consignatario.idCliente", tfm.remove("consignatario"));
		tfm.put("redespacho.idCliente", tfm.remove("redespacho"));
		tfm.put("responsavelFrete.idCliente", tfm.remove(RESPONSAVEL_FRETE));
		tfm.put("controleCarga.idControleCarga", tfm.remove("controleCarga"));
		tfm.put("manifestoColeta.idManifestoColeta", tfm.remove("manifestoColeta"));
		tfm.put("manifesto.idManifesto", tfm.remove("manifestoViagemNacional"));
		tfm.put("manifestoEntrega.idManifestoEntrega", tfm.remove("manifestoEntrega"));
		tfm.put("volumeColeta", tfm.remove("volumeColeta"));

		tfm.put("periodoInicial", YearMonthDayDeserializer.parse((String) tfm.get("periodoInicial")));
		tfm.put("periodoFinal", YearMonthDayDeserializer.parse((String) tfm.get("periodoFinal")));

		return new ListResponseBuilder(filtro, limiteRegistros, reportExecutionManager.getReportOutputDir(), LOCALIZACAO_MERCADORIA, filtro.getColumns())
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return doctoServicoService.findPaginatedConsultaLocalizacaoMercadoriaMap(tfm);
					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return doctoServicoService.getRowCountConsultaLocalizacaoMercadoria(tfm);
					}

				})
				.build();

	}


	@GET
	@Path(value="dowloadImagem")
	public Response dowloadImagem(@QueryParam("cdFilial") final String cdFilial,
								  @QueryParam("nrDoctoServico") final Integer nrDoctoServico, @QueryParam("dhEmissao") final String dhEmissao,
								  @QueryParam("idDoctoServico") final Integer idDoctoServico) {
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("cdFilial", cdFilial);
		parametros.put("nrDoctoServico", nrDoctoServico);
		parametros.put("dhEmissao", dhEmissao);
		parametros.put("idDoctoServico", idDoctoServico);
		parametros.put("urlImagemDigital", System.getProperty(PROPERTY_URL_IMAGEM_DIGITAL));
		return ocorrenciaEntregaService.dowloadImagem(parametros);
	}

	@GET
	@Path("findClienteCCTByUsuario")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Response findClienteCCTByUsuario(){
		Map result = clienteUsuarioCCTService.findClienteCCTByUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		if(!result.isEmpty()){
			Map<String, Object> data = new HashMap<>();
			data.put(NR_IDENTIFICACAO, result.get("nrIdentificacaoRemetente"));
			List<Map<String, Object>> list = (List<Map<String, Object>>) clienteService.findSuggest(data);
			if(!list.isEmpty()){
				result.putAll(list.get(0));
			}
		}
		return Response.ok(result).build();
	}

	@GET
	@Path("findById/detalhe")
	public Response findByIdDetalhamento(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put(ID_DOCTO_SERVICO, idDoctoServico);
		//Garante que não retorne um registro que a empresa parceira não tem permissão de visualizar
		criteria.put("idEmpresa", SessionUtils.getEmpresaSessao().getIdEmpresa());

		Map map = doctoServicoService.findByIdDSByLocalizacaoMercadoria(criteria);
		if (map == null) {
			return Response.ok().build();
		}

		List<PpdRecibo> recibos = ppdReciboService.findByIdConhecimento(idDoctoServico);
		Long idRecibo = null;
		if(recibos != null && !recibos.isEmpty()) {
			idRecibo = recibos.get(0).getIdRecibo();
		}
		map.put("idReciboPpd", idRecibo);

		DoctoServicoIndenizacao doctoServicoIndenizacao = doctoServicoIndenizacaoService.findByIdDoctoServicoParaLocalizacaoMercadoria(criteria.getLong(ID_DOCTO_SERVICO));

		Long idDoctoServicoIndenizacao = null;
		Long idReciboIndenizacao = null;
		if (doctoServicoIndenizacao != null) {
			idDoctoServicoIndenizacao =  doctoServicoIndenizacao.getIdDoctoServicoIndenizacao();
			idReciboIndenizacao =  doctoServicoIndenizacao.getReciboIndenizacao().getIdReciboIndenizacao();
		}
		map.put("idDoctoServicoIndenizacao", idDoctoServicoIndenizacao);
		map.put(ID_RECIBO_INDENIZACAO, idReciboIndenizacao);

		map.put("urlImagemDigital", System.getProperty(PROPERTY_URL_IMAGEM_DIGITAL));

		return Response.ok(map).build();

	}

	@GET
	@Path("findById/aba/principal/informacoesBasicas")
	public Response findByIdDetalhamentoAbaPrincipal(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {

		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put(ID_DOCTO_SERVICO, idDoctoServico);
		Map map = doctoServicoService.findByIdDSByLocalizacaoMercadoriaPrincipal(criteria);
		map.put(NR_IDENTIFICACAO_REM, FormatUtils.formatIdentificacao((DomainValue) map.get(TP_IDENTIFICACAO_REM),(String) map.get(NR_IDENTIFICACAO_REM)));
		map.put(NR_IDENTIFICACAO_DEST, FormatUtils.formatIdentificacao((DomainValue) map.get(TP_IDENTIFICACAO_DEST),(String) map.get(NR_IDENTIFICACAO_DEST)));
		if (map.get(DH_BAIXA) != null) {
			map.put(DH_BAIXA, JTFormatUtils.format((DateTime) map.get(DH_BAIXA)));
		}

		//LMSA-2762
		map.put("qtdediasUteis", map.get("nrDiasRealEntrega"));

		String progrDtTurno = "";
		if (map.get(DT_AGENDAMENTO) != null) {
			YearMonthDay data = (YearMonthDay) map.get(DT_AGENDAMENTO);
			progrDtTurno = JTFormatUtils.format(data);
			if (map.get(DS_TURNO) != null)
				progrDtTurno = progrDtTurno + " - " + map.get(DS_TURNO).toString();
		} else if (map.get(DS_TURNO) != null)
			progrDtTurno = map.get(DS_TURNO).toString();

		String progrHorario = "";
		if (map.get(HR_PREFERENCIA_INICIAL) != null) {
			TimeOfDay hrPreferenciaInicial = (TimeOfDay) map.get(HR_PREFERENCIA_INICIAL);
			progrHorario = JTFormatUtils.format(hrPreferenciaInicial);
			if (map.get(HR_PREFERENCIA_FINAL) != null) {
				TimeOfDay hrPreferenciaFinal = (TimeOfDay) map.get(HR_PREFERENCIA_FINAL);
				progrHorario = progrHorario + " às " + JTFormatUtils.format(hrPreferenciaFinal);
			}
		} else if (map.get(HR_PREFERENCIA_FINAL) != null) {
			TimeOfDay hrPreferenciaFinal = (TimeOfDay) map.get(HR_PREFERENCIA_FINAL);
			progrHorario = JTFormatUtils.format(hrPreferenciaFinal);
		}
		if (StringUtils.isNotEmpty(progrHorario)) {
			map.put("programacao", progrDtTurno + " " + progrHorario);
		} else {
			map.put("programacao", progrDtTurno);
		}

		if (map.get(PS_AFERIDO) == null) {
			map.put(PS_AFERIDO, FormatUtils.formatDecimal(BIG_DECIMAL_TRES_CASAS, BigDecimal.ZERO));
		}
		if (map.get(DT_PREV_ENTREGA) != null) {
			YearMonthDay data = (YearMonthDay) map.get(DT_PREV_ENTREGA);
			progrDtTurno = JTFormatUtils.format(data);
			map.put(DT_PREV_ENTREGA, progrDtTurno);
		}

		return Response.ok(map).build();
	}

	@GET
	@Path("findById/notasFiscais")
	public Response findByIdDetalhamentoNotasFiscais(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List<Map<String, Object>> lista = notaFiscalConhecimentoService.findNFByIdDoctoServico(idDoctoServico);
		for (Map<String, Object> map: lista) {
			if (map.get(DT_EMISSAO) != null) {
				YearMonthDay data = (YearMonthDay) map.get(DT_EMISSAO);
				String dtEmissao = JTFormatUtils.format(data);
				map.put(DT_EMISSAO, dtEmissao);
			}
			if (map.get(DT_SAIDA) != null) {
				YearMonthDay data = (YearMonthDay) map.get(DT_SAIDA);
				String dtEmissao = JTFormatUtils.format(data);
				map.put(DT_SAIDA, dtEmissao);
			}
		}

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return lista;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return lista.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@POST
	@Path("findById/volumes")
	public Response findByIdDetalhamentoVolumes(FiltroPaginacaoDto filtro){
		final TypedFlatMap tfm = new TypedFlatMap();

		tfm.put(CURRENT_PAGE, filtro.getPagina() == null ? UM : String.valueOf(filtro.getPagina()));
		tfm.put(PAGE_SIZE, filtro.getQtRegistrosPagina() == null ? String.valueOf(300) : String.valueOf(filtro.getQtRegistrosPagina()));

		tfm.put(ID_DOCTO_SERVICO, Long.valueOf(filtro.getFiltros().get(ID_DOCTO_SERVICO).toString()));

		return new ListResponseBuilder(filtro, -1, reportExecutionManager.getReportOutputDir(), LOCALIZACAO_MERCADORIA, filtro.getColumns())
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return volumeNotaFiscalService.findPaginatedMap(new PaginatedQuery(tfm)).getList();

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return volumeNotaFiscalService.getRowCount(tfm);
					}

				})
				.suppressWarning()
				.build();

	}

	@POST
	@Path("findById/notasFiscaisOperadas")
	public Response findByIdDetalhamentoNotasFiscaisOperadas(FiltroPaginacaoDto filtro) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idNotaFiscalConhecimento", Long.valueOf(filtro.getFiltros().get("idNotaFiscalConhecimento").toString()));
		tfm.put(CURRENT_PAGE, filtro.getPagina() == null ? UM : String.valueOf(filtro.getPagina()));
		tfm.put(PAGE_SIZE, filtro.getQtRegistrosPagina() == null ? String.valueOf(300) : String.valueOf(filtro.getQtRegistrosPagina()));

		final Integer qtRegistros = notaFiscalOperadaService.getRowCount(tfm);
		final List<Map<String, Object>> list = notaFiscalOperadaService.findPaginatedMap(new PaginatedQuery(tfm)).getList();

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return qtRegistros;
					}

				})
				.suppressWarning()
				.build();
	}

	@POST
	@Path("findById/volumes/eventos")
	public Response findByIdDetalhamentoVolumesEventos(FiltroPaginacaoDto filtro) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put("idVolumeNotaFiscal", Long.valueOf(filtro.getFiltros().get("idVolumeNotaFiscal").toString()));
		tfm.put(CURRENT_PAGE, filtro.getPagina() == null ? UM : String.valueOf(filtro.getPagina()));
		tfm.put(PAGE_SIZE, filtro.getQtRegistrosPagina() == null ? String.valueOf(300) : String.valueOf(filtro.getQtRegistrosPagina()));

		final Integer qtRegistros = eventoVolumeService.getRowCount(tfm);
		final List<Map<String, Object>> list = eventoVolumeService.findPaginatedMap(new PaginatedQuery(tfm)).getList();
		for (Map<String, Object> map: list) {
			if (map.get(DH_INCLUSAO) != null) {
				map.put(DH_INCLUSAO,JTFormatUtils.format((DateTime) map.get(DH_INCLUSAO)));
			}
			if(map.get(TP_SCAN)!= null){
				DomainValue dv = domainValueService.findDomainValueByValue("DM_TIPO_SCAN", (String) map.get(TP_SCAN));
				map.put(TP_SCAN,dv.getDescription().toString());
			}
		}

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return qtRegistros;
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/integrantes")
	public Response findByIdDetalhamentoIntegrantes(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		List<Map<String, Object>> doctoServicos =  doctoServicoService.findPaginatedIntegrantes(idDoctoServico);
		final List<Map<String, Object>> result = new ArrayList<>();
		Long idRegistro = LongUtils.ZERO;
		if (doctoServicos != null && !doctoServicos.isEmpty()){
			Map clientes = doctoServicos.get(0);
			if (clientes.get(NM_PESSOA_REM) != null){
				Map<String, Object> registro = new HashMap<>();
				idRegistro = LongUtils.incrementValue(idRegistro);
				registro.put(TIPO_CLIENTE, configuracoesFacade.getMensagem(REMETENTE));
				registro.put(NM_PESSOA, clientes.get(NM_PESSOA_REM));
				registro.put(MUNICIPIO, clientes.get(MUNICIPIO_REM));
				registro.put(ID_CLIENTE, clientes.get("idClienteRem"));
				registro.put(ID_REGISTRO, idRegistro);
				registro.put(POPUP,POPUP_GIF);
				registro.put(TP_IDENTIFICACAO, clientes.get(TP_IDENTIFICACAO_REM));
				registro.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)clientes.get(TP_IDENTIFICACAO_REM),clientes.get(NR_IDENTIFICACAO_REM).toString()));
				result.add(registro);
			}
			if(clientes.get(NM_PESSOA_DEST) != null){
				Map<String, Object> map = new HashMap<>();
				idRegistro = LongUtils.incrementValue(idRegistro);
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem(DESTINATARIO));
				map.put(TP_IDENTIFICACAO, clientes.get(TP_IDENTIFICACAO_DEST));
				if(clientes.get(NR_IDENTIFICACAO_DEST) != null){
					map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)clientes.get(TP_IDENTIFICACAO_DEST), clientes.get(NR_IDENTIFICACAO_DEST).toString()));
				}
				map.put(NM_PESSOA, clientes.get(NM_PESSOA_DEST));
				map.put(MUNICIPIO, clientes.get(MUNICIPIO_DEST));
				map.put(ID_CLIENTE, clientes.get("idClienteDest"));
				map.put(ID_REGISTRO, idRegistro);
				map.put(POPUP,POPUP_GIF);
				result.add(map);
			}
			if (clientes.get("nmPessoaCons") != null){
				Map<String, Object> map = new HashMap<>();
				idRegistro = LongUtils.incrementValue(idRegistro);
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem("recebedor"));
				map.put(TP_IDENTIFICACAO, clientes.get("tpIdentificacaoCons"));
				if(clientes.get("nrIdentificacaoCons") != null){
					map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)clientes.get("tpIdentificacaoCons"), clientes.get("nrIdentificacaoCons").toString()));
				}
				map.put(NM_PESSOA, clientes.get("nmPessoaCons"));
				map.put(MUNICIPIO, clientes.get("municipioCons"));
				map.put(ID_CLIENTE, clientes.get("idClienteCons"));
				map.put(ID_REGISTRO, idRegistro);
				map.put(POPUP,POPUP_GIF);
				result.add(map);
			}
			if (clientes.get(NM_PESSOA_REDES) != null){
				Map map = new HashMap();
				idRegistro = idRegistro + 1L;
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem("expedidor"));
				map.put(TP_IDENTIFICACAO, clientes.get(TP_IDENTIFICACAO_REDES));
				if(clientes.get(NR_IDENTIFICACAO_REDES) != null) {
					map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)clientes.get(TP_IDENTIFICACAO_REDES), clientes.get(NR_IDENTIFICACAO_REDES).toString()));
				}
				map.put(NM_PESSOA, clientes.get(NM_PESSOA_REDES));
				map.put(MUNICIPIO, clientes.get(MUNICIPIO_REDES));
				map.put(ID_CLIENTE, clientes.get("idClienteRedes"));
				map.put(ID_REGISTRO, idRegistro);
				map.put(POPUP,POPUP_GIF);
				result.add(map);
			}

		}

		List<Map> devedores = devedorDocServFatService.findDevedoresByIdDoctoServico(idDoctoServico);
		if(devedores != null) {
			for (Map devedor : devedores) {
				idRegistro = LongUtils.incrementValue(idRegistro);
				devedor.put(NR_IDENTIFICACAO,FormatUtils.formatIdentificacao((DomainValue)devedor.get(TP_IDENTIFICACAO),devedor.get(NR_IDENTIFICACAO).toString()));
				devedor.put(ID_REGISTRO, idRegistro);
				devedor.put(TIPO_CLIENTE, configuracoesFacade.getMensagem(RESPONSAVEL_FRETE));
				devedor.put(POPUP,POPUP_GIF);
				result.add(devedor);
			}
		}

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return result;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return result.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/integrantes/enderecos")
	public Response findByIdDetalhamentoIntegrantesEnderecos(@QueryParam("idPessoa") Long idPessoa, @QueryParam("geral") @DefaultValue(value="true") boolean geral){
		if (geral) {
			return findTelefonesGerais(idPessoa);
		} else {
			return findTelefonesEndereco(idPessoa);
		}

	}

	private Response findTelefonesEndereco(Long idPessoa) {
		final List<Map<String, Object>> toReturn = new ArrayList<>();

		final List<Map<String, Object>> enderecos = enderecoPessoaService.findEnderecoPessoaByIdPessoa(idPessoa);

		if (enderecos != null) {
			toReturn.addAll(enderecos);
			for (Map map : enderecos) {
				String dsTipoLogradouro = ((VarcharI18n) map.get("dsTipoLogradouro")).getValue();
				String dsEndereco = (String) map.get(DS_ENDERECO);
				String nrEndereco = (String) map.get("nrEndereco");
				String dsComplemento = (String) map.get("dsComplemento");
				map.put("enderecoCompleto", FormatUtils.formatEnderecoPessoa(dsTipoLogradouro, dsEndereco, nrEndereco, dsComplemento));
				if (map.get(TP_ENDERECO) != null) {
					map.put(TP_ENDERECO, ((DomainValue)map.get(TP_ENDERECO)).getDescriptionAsString());
				}
				if (map.get(DT_VIGENCIA_INICIAL) != null) {
					map.put(DT_VIGENCIA_INICIAL,JTFormatUtils.format((YearMonthDay) map.get(DT_VIGENCIA_INICIAL)));
				}
				if (map.get(DT_VIGENCIA_FINAL) != null) {
					map.put(DT_VIGENCIA_FINAL,JTFormatUtils.format((YearMonthDay) map.get(DT_VIGENCIA_FINAL)));
				}

				List<Map<String, Object>> mapTelefones = new ArrayList<>();
				List<TelefoneEndereco> telefones = telefoneEnderecoService.findByEnderecoPessoa(idPessoa, (Long)map.get("idEnderecoPessoa"));
				for (TelefoneEndereco te: telefones) {
					Map<String, Object> m = new HashMap<>();
					m.put("idTelefoneEndereco", te.getIdTelefoneEndereco());
					m.put("tpTelefone", te.getTpTelefone().getDescriptionAsString());
					m.put("tpUso", te.getTpUso().getDescriptionAsString());
					m.put(NR_DDD, te.getNrDdd());
					m.put(NR_TELEFONE, te.getNrTelefone());
					m.put("nrDdi", te.getNrDdi());
					mapTelefones.add(m);
				}
				map.put("subItems",mapTelefones);

			}
		}
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return toReturn;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return toReturn.size();
					}

				})
				.suppressWarning()
				.build();
	}

	private Response findTelefonesGerais(Long idPessoa) {
		//Telefones gerais:
		List<TelefoneEndereco> telefonesGerais = telefoneEnderecoService.findByEnderecoPessoa(idPessoa, null);
		List<Map<String, Object>> mapTelefones = new ArrayList<>();
		if (telefonesGerais != null && !telefonesGerais.isEmpty()) {
			for (TelefoneEndereco te: telefonesGerais) {
				Map<String, Object> m = new HashMap<>();
				m.put("idTelefoneEndereco", te.getIdTelefoneEndereco());
				m.put("tpTelefone", te.getTpTelefone().getDescriptionAsString());
				m.put("tpUso", te.getTpUso().getDescriptionAsString());
				m.put(NR_DDD, te.getNrDdd());
				m.put(NR_TELEFONE, te.getNrTelefone());
				m.put("nrDdi", te.getNrDdi());
				mapTelefones.add(m);
			}
		}
		return Response.ok(mapTelefones).build();
	}

	@GET
	@Path("findById/parceiras/principal")
	public Response findCooperadaByIdConhecimento(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		List<Map> ctoCooperadas = ctoCtoCooperadaService.findCooperadaByIdConhecimento(idDoctoServico);

		if (ctoCooperadas == null || ctoCooperadas.isEmpty()) {
			return Response.ok().build();
		}
		Map<String, Object> result = ctoCooperadas.get(0);

		Integer nrCtoCooperada = (Integer) result.remove("nrCtoCooperada");
		if (nrCtoCooperada != null) {
			result.put("nrCtoCooperada", FormatUtils.fillNumberWithZero(nrCtoCooperada.toString(), 8));
		}

		if( result.get("idCtoCtoCooperada") != null) {
			List<Map> notasFiscais = notaFiscalCtoCooperadaService.findNotaFiscalByIdCooperada((Long)result.get("idCtoCtoCooperada"));
			if(notasFiscais != null && !notasFiscais.isEmpty()) {
				Map notaFiscal = notasFiscais.get(0);
				result.put("volumes", notaFiscal.get("qtVolumes"));
				result.put("pesoReal", notaFiscal.get("psMercadoria"));
				if (notaFiscal.get(VL_TOTAL) != null) {
					result.put(VALOR_MERCADORIA, result.get("dsSimboloMoeda") + " " + FormatUtils.formatDecimal(BIG_DECIMAL, (BigDecimal) notaFiscal.get(VL_TOTAL)));
				}
			}
		}
		if (result.get("tpIdentificacaoCooperada") != null) {
			DomainValue dvCoop = (DomainValue) result.get("tpIdentificacaoCooperada");
			result.put("nrIdentificacaoCooperada", FormatUtils.formatIdentificacao(dvCoop.getValue(), result.get("nrIdentificacaoCooperada").toString()));
		}
		if (result.get(TP_CONHECIMENTO) != null) {
			DomainValue dvConh = (DomainValue)result.get(TP_CONHECIMENTO);
			result.put(TP_CONHECIMENTO, dvConh.getDescription().toString());
		}
		if (result.get(TP_IDENTIFICACAO_REM) != null) {
			DomainValue dvRem = (DomainValue)result.get(TP_IDENTIFICACAO_REM);
			result.put(NR_IDENTIFICACAO_REM, FormatUtils.formatIdentificacao(dvRem.getValue(), result.get(NR_IDENTIFICACAO_REM).toString()));
		}
		if (result.get(TP_IDENTIFICACAO_DEST) != null) {
			DomainValue dvDest = (DomainValue)result.get(TP_IDENTIFICACAO_DEST);
			result.put(NR_IDENTIFICACAO_DEST, FormatUtils.formatIdentificacao(dvDest.getValue(),result.get(NR_IDENTIFICACAO_DEST).toString()));
		}
		if (result.get(VL_FRETE) != null) {
			result.put(VL_FRETE, result.get("dsSimboloMoeda")+ " "+ FormatUtils.formatDecimal(BIG_DECIMAL, (BigDecimal)result.get(VL_FRETE)));
		}
		if (result.get(DH_EMISSAO) != null) {
			result.put(DH_EMISSAO, JTFormatUtils.format((DateTime)result.get(DH_EMISSAO)));
		}
		if (result.get(DT_ENTREGA) != null) {
			result.put(DT_ENTREGA, JTFormatUtils.format((DateTime)result.get(DT_ENTREGA)));
		}
		return Response.ok(result).build();
	}

	@GET
	@Path("findById/awb/infobasica")
	public Response findAwbByIdDoctoServico(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Awb awb = this.findAwbByDocto(idDoctoServico);

		if(awb != null){
			Map<String, Object> awbMap = new HashMap<>();

			awb = awbService.findById(awb.getIdAwb());
			awbMap.put("idAwb", awb.getIdAwb());
			awbMap.put("nrAwb", ConstantesExpedicao.TP_STATUS_PRE_AWB.equals(awb.getTpStatusAwb().getValue())
					? awb.getIdAwb()
					: AwbUtils.getNrAwbFormated(awb));
			awbMap.put("blConferido", awb.getBlConferido());
			awbMap.put("tpStatusAwb", this.getDomainMap(awb.getTpStatusAwb()));
			awbMap.put("ciaAerea", this.getEmpresaMap(awb.getCiaFilialMercurio().getEmpresa().getPessoa()));
			awbMap.put("aeroportoOrigem", this.getAeroportoMap(awb.getAeroportoByIdAeroportoOrigem()));
			awbMap.put("aeroportoDestino", this.getAeroportoMap(awb.getAeroportoByIdAeroportoDestino()));

			if(awb.getAeroportoByIdAeroportoEscala() != null){
				awbMap.put("aeroportoEscala", this.getAeroportoMap(awb.getAeroportoByIdAeroportoEscala()));
			}

			awbMap.put("filialOrigem", this.getFilialMap(awb.getFilialByIdFilialOrigem()));
			awbMap.put("filialDestino", this.getFilialMap(awb.getFilialByIdFilialDestino()));
			awbMap.put("cliente", this.getClienteMap(awb.getAeroportoByIdAeroportoOrigem().getPessoa()));
			awbMap.put("dhDigitacao", JTFormatUtils.format(awb.getDhDigitacao()));
			awbMap.put(DH_EMISSAO, awb.getDhEmissao() != null ? JTFormatUtils.format(awb.getDhEmissao()) : "");
			awbMap.put("tpLocalEmissao", this.getDomainMap(awb.getTpLocalEmissao()));

			Awb awbSubstituido = null;
			if(awb.getAwbSubstituido() != null){
				awbSubstituido = awbService.findById(awb.getAwbSubstituido().getIdAwb());
			}
			String nrAwbSubstituido = "";
			if(awbSubstituido != null && awbSubstituido.getCiaFilialMercurio() != null
					&& awbSubstituido.getCiaFilialMercurio().getEmpresa() != null){
				nrAwbSubstituido = awbSubstituido.getCiaFilialMercurio().getEmpresa().getSgEmpresa() + " " + AwbUtils.getNrAwbFormated(awbSubstituido);
			}

			awbMap.put("nrAwbSubstituido", nrAwbSubstituido);
			awbMap.put("tpLocalizacao", awb.getTpLocalizacao() != null ? awb.getTpLocalizacao().getDescriptionAsString() : "");
			awbMap.put("qtVolumes", awb.getQtVolumes());
			awbMap.put("psTotal", awb.getPsTotal());
			awbMap.put("pesoTotal", awb.getPsTotal());
			awbMap.put("psCubado", awb.getPsCubado());
			awbMap.put("dsVooPrevisto", awb.getDsVooPrevisto());
			awbMap.put("dhPrevistaSaida", JTFormatUtils.format(awb.getDhPrevistaSaida()));
			awbMap.put("dhPrevistaChegada", JTFormatUtils.format(awb.getDhPrevistaChegada()));
			awbMap.put("obAwb", awb.getObAwb());

			if(awb.getMoeda() != null){
				awbMap.put("moeda", this.getMoedaMap(awb.getMoeda()));
			}

			if(awb.getVlFrete() != null){
				awbMap.put(VL_FRETE, awb.getMoeda().getDsSimbolo() + " " + FormatUtils.formatDecimal(BIG_DECIMAL, awb.getVlFrete()));
			}
			if(awb.getVlFretePeso() != null){
				awbMap.put("vlFretePeso", awb.getMoeda().getDsSimbolo() + " " + FormatUtils.formatDecimal(BIG_DECIMAL, awb.getVlFretePeso()));
			}
			if(awb.getVlTaxaTerrestre() != null){
				awbMap.put("vlTaxaTerrestre", awb.getMoeda().getDsSimbolo() + " " + FormatUtils.formatDecimal(BIG_DECIMAL, awb.getVlTaxaTerrestre()));
			}
			if(awb.getVlTaxaCombustivel() != null){
				awbMap.put("vlTaxaCombustivel", awb.getMoeda().getDsSimbolo() + " " + FormatUtils.formatDecimal(BIG_DECIMAL, awb.getVlTaxaCombustivel()));
			}
			if(awb.getVlICMS() != null){
				awbMap.put("vlICMS", awb.getMoeda().getDsSimbolo() + " " + FormatUtils.formatDecimal(BIG_DECIMAL, awb.getVlICMS()));
			}
			awbMap.put("pcAliquotaICMS", awb.getPcAliquotaICMS());
			awbMap.put("blConferido", awb.getBlConferido());
			awbMap.put("tpFrete", awb.getTpFrete().getDescription());

			awbMap.put("dsEmbalagem", (awb.getAwbEmbalagems() != null && !awb.getAwbEmbalagems().isEmpty()) ? awb.getAwbEmbalagems().get(0).getEmbalagem().getDsEmbalagem() : "");

			if (awb.getInscricaoEstadualExpedidor() != null){
				awbMap.put("nrInscricaoEstadualExpedidor", awb.getInscricaoEstadualExpedidor().getNrInscricaoEstadual());
			}

			if (awb.getInscricaoEstadualDestinatario() != null){
				awbMap.put("nrInscricaoEstadualDestinatario", awb.getInscricaoEstadualDestinatario().getNrInscricaoEstadual());
			}

			if (awb.getPrestacaoConta() != null) {
				awbMap.put("nrPrestacaoConta", awb.getPrestacaoConta().getNrPrestacaoConta());
			}

			if (awb.getTpAwb() != null){
				awbMap.put("tpAwb", awb.getTpAwb().getDescriptionAsString());
			}

			if (awb.getProdutoEspecifico() != null) {
				awbMap.put("nrTarifaEspecifica", awb.getProdutoEspecifico().getNrTarifaEspecifica());
			}

			List<FaturaCiaAerea> listaFaturas = faturaCiaAereaService.findFaturaCiaAereaByIdAwb(awb.getIdAwb());
			if (listaFaturas != null && !listaFaturas.isEmpty()) {
				awbMap.put("nrFatura", listaFaturas.get(0).getNrFaturaCiaAerea());
			}

			if (awb.getNaturezaProduto() != null) {
				awbMap.put("dsNaturezaProduto", awb.getNaturezaProduto().getDsNaturezaProduto());
			}

			if (awb.getTarifaSpot() != null) {
				awbMap.put("tarifaSpot", this.getTarifaSpotMap(awb.getTarifaSpot()));
			}

			awbMap.put("clienteExpedidor", this.adicionaCliente(awb.getClienteByIdClienteExpedidor()));
			awbMap.put("clienteDestinatario", this.adicionaCliente(awb.getClienteByIdClienteDestinatario()));
			return Response.ok(awbMap).build();
		}

		return Response.ok().build();
	}

	private Map<String, Object> adicionaCliente(Cliente cliente) {
		List clientes = clienteService.findClienteByNrIdentificacao(cliente.getPessoa().getNrIdentificacao());
		if (clientes != null && !clientes.isEmpty()) {
			Map clienteMap = (Map) clientes.get(0);
			adjustEndereco((Map) clienteMap.get("pessoa"));
			return clienteMap;
		}
		return null;
	}

	private void adjustEndereco(Map pessoa) {
		Map endereco = (Map) pessoa.get("endereco");

		if (endereco != null) {
			VarcharI18n tipoLogradouro = (VarcharI18n) endereco.remove("dsTipoLogradouro");
			if(tipoLogradouro != null) {
				endereco.put(DS_ENDERECO, tipoLogradouro.getValue() + " " + endereco.get(DS_ENDERECO));
			}
		}
	}

	private Map<String, Object> getTarifaSpotMap(TarifaSpot tarifaSpot) {
		Map<String, Object> map = new HashMap<>();
		map.put("dsSenha", tarifaSpot.getDsSenha());
		TypedFlatMap tarifaMap = tarifaSpotService.findByIdMap(tarifaSpot.getIdTarifaSpot());
		map.put("nmUsuario", tarifaMap.get("usuarioByIdUsuarioLiberador.nmUsuario"));
		map.put("dtLiberacao", tarifaMap.get("dtLiberacao"));
		return map;
	}

	private Map<String, Object> getMoedaMap(Moeda moeda) {
		Map<String, Object> moedaMap = new HashMap<>();
		moedaMap.put("siglaDescricao", FormatUtils.concatSiglaSimboloMoeda(moeda));
		moedaMap.put("idMoeda", moeda.getIdMoeda());
		moedaMap.put(DS_SIMBOLO, moeda.getDsSimbolo());
		return moedaMap;
	}

	private Map<String, Object> getClienteMap(Pessoa cliente) {
		Map<String, Object> clienteMap = new HashMap<>();
		clienteMap.put(ID_CLIENTE, cliente.getIdPessoa());
		clienteMap.put(NR_IDENTIFICACAO, cliente.getNrIdentificacao());
		return clienteMap;
	}

	private Map<String, Object> getFilialMap(Filial filial) {
		Map<String, Object> filialMap = new HashMap<>();
		filialMap.put("idFilial", filial.getIdFilial());
		filialMap.put(SG_FILIAL, filial.getSgFilial());
		filialMap.put("nmFilial", filial.getPessoa().getNmFantasia());
		return filialMap;
	}

	private Map<String, Object> getAeroportoMap(Aeroporto aeroporto) {
		Map<String, Object> aeroportoMap = new HashMap<>();
		aeroportoMap.put("sgAeroporto", aeroporto.getSgAeroporto());
		aeroportoMap.put("nmAeroporto", aeroporto.getPessoa().getNmPessoa());

		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findByIdPessoa(aeroporto.getPessoa().getIdPessoa());
		Map<String, Object> enderecoMap = new HashMap<>();
		enderecoMap.put("nmMunicipio", enderecoPessoa != null && enderecoPessoa.getMunicipio() != null ? enderecoPessoa.getMunicipio().getNmMunicipio() : "");

		aeroportoMap.put("endereco", enderecoMap);

		return aeroportoMap;
	}

	private Map<String, Object> getEmpresaMap(Pessoa ciaAerea) {
		Map<String, Object> ciaMap = new HashMap<>();
		ciaMap.put("nmCiaAerea", ciaAerea.getNmPessoa());
		return ciaMap;
	}

	private Awb findAwbByDocto(Long idDoctoServico) {
		Awb awb = ctoAwbService.findAwbByIdCto(idDoctoServico, ConstantesExpedicao.TP_STATUS_AWB_EMITIDO);
		if(awb == null){
			awb = ctoAwbService.findAwbByIdCto(idDoctoServico, ConstantesExpedicao.TP_STATUS_PRE_AWB);
		}
		return awb;
	}

	private Object getDomainMap(DomainValue domainValue) {
		Map<String, Object> map = new HashMap<>();
		if(domainValue != null){
			map.put("id", domainValue.getId());
			map.put("value", domainValue.getValue());
			map.put("description", domainValue.getDescriptionAsString());
		}
		return map;
	}

	@GET
	@Path("findById/parceiras/integrantes")
	public Response findPaginatedIntegrantesAbaParcerias(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		List listaClientes =  ctoCtoCooperadaService.findPaginatedIntegrantes(idDoctoServico);
		List listaNova = null;
		Long idRegistro =0L;
		if(!listaClientes.isEmpty()){
			listaNova = new ArrayList();
			Map mapClientes = (HashMap)listaClientes.get(0);
			if(mapClientes.get(NM_PESSOA_REM)!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + 1L;
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem(REMETENTE));
				map.put(TP_IDENTIFICACAO, mapClientes.get(TP_IDENTIFICACAO_REM));
				map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)mapClientes.get(TP_IDENTIFICACAO_REM),mapClientes.get(NR_IDENTIFICACAO_REM).toString()));
				map.put(NM_PESSOA, mapClientes.get(NM_PESSOA_REM));
				map.put(MUNICIPIO, mapClientes.get(MUNICIPIO_REM));
				map.put(ID_CLIENTE, mapClientes.get("idClienteRem"));
				map.put(ID_REGISTRO, idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get(NM_PESSOA_DEST)!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + 1L;
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem(DESTINATARIO));
				map.put(TP_IDENTIFICACAO, mapClientes.get(TP_IDENTIFICACAO_DEST));
				map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)mapClientes.get(TP_IDENTIFICACAO_DEST), mapClientes.get(NR_IDENTIFICACAO_DEST).toString()));
				map.put(NM_PESSOA, mapClientes.get(NM_PESSOA_DEST));
				map.put(MUNICIPIO, mapClientes.get(MUNICIPIO_DEST));
				map.put(ID_CLIENTE, mapClientes.get("idClienteDest"));
				map.put(ID_REGISTRO, idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get("nmPessoaConsi")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + 1L;
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem("consignatario"));
				map.put(TP_IDENTIFICACAO, mapClientes.get("tpIdentificacaoConsi"));
				map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoConsi"), mapClientes.get("nrIdentificacaoConsi").toString()));
				map.put(NM_PESSOA, mapClientes.get("nmPessoaConsi"));
				map.put(MUNICIPIO, mapClientes.get("municipioConsi"));
				map.put(ID_CLIENTE, mapClientes.get("idClienteConsi"));
				map.put(ID_REGISTRO, idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get(NM_PESSOA_REDES)!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + 1L;
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem("redespacho"));
				map.put(TP_IDENTIFICACAO, mapClientes.get(TP_IDENTIFICACAO_REDES));
				map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)mapClientes.get(TP_IDENTIFICACAO_REDES), mapClientes.get(NR_IDENTIFICACAO_REDES).toString()));
				map.put(NM_PESSOA, mapClientes.get(NM_PESSOA_REDES));
				map.put(MUNICIPIO, mapClientes.get(MUNICIPIO_REDES));
				map.put(ID_CLIENTE, mapClientes.get("idClienteRedes"));
				map.put(ID_REGISTRO, idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get("nmPessoaDev")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + 1L;
				map.put(TIPO_CLIENTE, configuracoesFacade.getMensagem(RESPONSAVEL_FRETE));
				map.put(TP_IDENTIFICACAO, mapClientes.get("tpIdentificacaoDev"));
				map.put(NR_IDENTIFICACAO, FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoDev"), mapClientes.get("nrIdentificacaoDev").toString()));
				map.put(NM_PESSOA, mapClientes.get("nmPessoaDev"));
				map.put(MUNICIPIO, mapClientes.get(MUNICIPIO_DEV));
				map.put(ID_CLIENTE, mapClientes.get("idClienteDev"));
				map.put(ID_REGISTRO, idRegistro);
				listaNova.add(map);
			}

		}

		return Response.ok(listaNova).build();
	}

	@GET
	@Path("findById/parceiras/notasFiscais")
	public Response findPaginatedNotaFiscalAbaParcerias(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		return Response.ok(notaFiscalCtoCooperadaService.findNotaFiscalByIdConhecimento(idDoctoServico)).build();
	}

	@GET
	@Path("findById/parceiras/dadosFrete")
	public Response findPaginatedDadosFrete(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		return Response.ok(parcelaCtoCooperadaService.findPaginatedDadosFrete(idDoctoServico)).build();
	}

	@GET
	@Path("findById/parceiras/dadosCalculo")
	public Response findDadosCalculoByIdConhecimento(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Map map = ctoCtoCooperadaService.findDadosCalculoByIdConhecimento(idDoctoServico);

		if (map != null && !map.isEmpty()) {
			if(map.get(VALOR_MERCADORIA)!= null)
				map.put(VALOR_MERCADORIA,map.get(DS_SIMBOLO).toString()+ " " +FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VALOR_MERCADORIA)));

			return Response.ok(map).build();
		} else {
			return Response.ok().build();
		}
	}

	@GET
	@Path("findById/parceiras/outros")
	public Response findOutrosByIdConhecimento(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Map map = ctoCtoCooperadaService.findOutrosByIdConhecimento(idDoctoServico);

		if (map != null && !map.isEmpty()) {
			if(map.get(DH_INCLUSAO)!= null) {
				map.put(DH_INCLUSAO, JTFormatUtils.format((DateTime)map.get(DH_INCLUSAO)));
			}

			if(map.get(TP_MODAL)!= null){
				DomainValue dv= (DomainValue)map.get(TP_MODAL);
				map.put(TP_MODAL,dv.getDescription().toString());
			}

			return Response.ok(map).build();
		} else {
			return Response.ok().build();
		}
	}

	@GET
	@Path("findById/cc")
	public Response findPaginatedControleCarga(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List list = controleCargaService.findPaginatedControleCargaByLocalizacoMerc(idDoctoServico);
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/manifesto/coleta")
	public Response findManifestoColetaByIdDoctoServico(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		List<Map<String, Object>> list =  manifestoColetaService.findListManifestoColetaByIdDoctoServico(idDoctoServico);

		List<ManifestoColetaDTO> listDto = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(list)){
			for (Map<String, Object> map : list) {
				TypedFlatMap tFlatMap = new TypedFlatMap(map);
				ManifestoColetaDTO manifestoColetaDTO = new ManifestoColetaDTO();

				manifestoColetaDTO.setSgFilialManifesto(tFlatMap.getString("sgFilialOr"));
				manifestoColetaDTO.setNrManifesto(tFlatMap.getInteger("nrManifesto"));
				manifestoColetaDTO.setSgFilialColeta(tFlatMap.getString("sgFilialOrPC"));
				manifestoColetaDTO.setNrColeta(tFlatMap.getLong("nrColeta"));
				manifestoColetaDTO.setDhEmissao(tFlatMap.getDateTime(DH_EMISSAO));
				manifestoColetaDTO.setDhColetaDisponivel(tFlatMap.getDateTime("dhColetaDisponivel"));
				manifestoColetaDTO.setDtPrevisaoColeta(tFlatMap.getYearMonthDay("dtPrevisaoColeta"));
				manifestoColetaDTO.setTpModoPedidoColeta(tFlatMap.getDomainValue("tpModoPedidoColeta"));
				manifestoColetaDTO.setTpPedidoColeta(tFlatMap.getDomainValue("tpPedidoColeta"));

				listDto.add(manifestoColetaDTO);
			}
		}

		return getReturnFind(listDto, listDto.size());
	}

	@GET
	@Path("findById/manifesto/viagem")
	public Response findPaginatedManifestoViagem(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final ResultSetPage<Map<String, Object>> lista = manifestoViagemNacionalService.findPaginatedManifestosViagemByLocalizacaoMercadoria(idDoctoServico);

		if (lista != null && lista.getList() != null) {
			final List<Map<String, Object>> list = lista.getList();
			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/manifesto/entrega")
	public Response findPaginatedManifestoEntrega(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final ResultSetPage<Map<String, Object>> lista = manifestoEntregaService.findPaginatedManifestoEntregaByIdDoctoServico(idDoctoServico);

		if (lista != null && lista.getList() != null) {
			final List<Map<String, Object>> list = lista.getList();
			if (list.size() == 1 && list.get(0).get("idManifestoEntrega") == null) {
				return Response.ok().build();
			}
			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/manifesto/entrega/notasFiscais")
	public Response findPaginatedManifestoEntregaNotasFiscais(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		final ResultSetPage<Map<String, Object>> lista = manifestoEntregaService.findPaginatedManifestoEntregaNotasFiscaisByIdDoctoServico(idDoctoServico);

		if (lista != null && lista.getList() != null) {
			final List<Map<String, Object>> list = lista.getList();

			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {
						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {
						@Override
						public Integer execute() {
							return list.size();
						}
					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/eventos")
	public Response findPaginatedEventos(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final ResultSetPage<Map<String, Object>> lista =  eventoDocumentoServicoService.findPaginatedEventosByIdDoctoServico(idDoctoServico);

		if (lista != null && lista.getList() != null) {
			final List<Map<String, Object>> list = lista.getList();
			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/trackingAwb")
	public Response findPaginatedTrackingAwb(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){

		Awb awb = this.findAwbByDocto(idDoctoServico);

		final ResultSetPage<Map<String, Object>> lista = trackingAwbService.findPaginated(awb.getIdAwb());

		if (lista != null && lista.getList() != null) {
			final List<Map<String, Object>> list = lista.getList();
			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/trackingAwb/dtUltimaAtualizacao")
	public Response getDtUltAtualizacaoHistoricoAwb(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Map map = new TypedFlatMap();
		map.put("dtUltimaAtualizacao", JTFormatUtils.format(JTDateTimeUtils.getDataHoraAtual()));
		return Response.ok(map).build();
	}

	@GET
	@Path("findById/historicoAwb")
	public Response findPaginatedHistoricoAwb(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){

		final ResultSetPage<Map<String, Object>> lista = awbService.findPaginatedAwbsByIdConhecimento(idDoctoServico);

		if (lista != null && lista.getList() != null) {
			final List<Map<String, Object>> list = lista.getList();
			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/bloqueios")
	public Response findPaginatedBloqueiosLiberacoes(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		ResultSetPage<Map<String, Object>> lista = ocorrenciaDoctoServicoService.findPaginatedBloqueiosLiberacoesByIdDoctoServ(idDoctoServico);

		if(lista != null && lista.getList() != null){
			final List<Map<String, Object>> list = lista.getList();

			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@GET
	@Path("findById/complementos/observacoes")
	public Response findByIdDetalhamentoComplementosObservacoes(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		final List<Map<String, Object>> list = observacaoDoctoServicoService.findPaginatedComplObservacoes(idDoctoServico);
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/complementos/embalagens")
	public Response findByIdDetalhamentoComplementosDoctoServico(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		final List list = servicoEmbalagemService.findPaginatedComplEmbalagens(idDoctoServico);
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/complementos/doctoServico")
	public Response findPaginatedDadosCompl(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List list = dadosComplementoService.findPaginatedDadosCompl(idDoctoServico);
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/complementos/notasFiscais")
	public Response findByIdDetalhamentoComplementosNotasFiscais(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List list = notaFiscalConhecimentoService.findPaginatedComplNF(idDoctoServico);
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/complementos/agendamentos")
	public Response findByIdDetalhamentoComplementosAgendamentos(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){

		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put(ID_DOCTO_SERVICO, idDoctoServico);
		criteria.put(CURRENT_PAGE, UM);
		criteria.put(PAGE_SIZE, "10000");

		ResultSetPage rsp = agendamentoEntregaService.findPaginatedAgendamentosByDoctoServico(criteria);

		final List<Map<String, Object>> list = rsp.getList();
		for (Map map: list) {
			if (map.get(TP_AGENDAMENTO) != null) {
				map.put(TP_AGENDAMENTO, ((DomainValue)map.get(TP_AGENDAMENTO)).getDescriptionAsString());
			}
			if (map.get(DH_CONTATO) != null) {
				map.put(DH_CONTATO, JTFormatUtils.format((DateTime) map.get(DH_CONTATO)));
			}
			if (map.get(BL_CARTAO) != null) {
				map.put(BL_CARTAO, Boolean.TRUE.equals(map.get(BL_CARTAO)) ? SIM : NAO);
			}
			if (map.get(TP_SITUACAO_AGENDAMENTO) != null) {
				map.put(TP_SITUACAO_AGENDAMENTO, ((DomainValue)map.get(TP_SITUACAO_AGENDAMENTO)).getDescriptionAsString());
			}
		}
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();

	}

	@GET
	@Path("findById/complementos/agendamentos/findById")
	public Response findByIdDetalhamentoComplementosAgendamentosFindById(@QueryParam("idAgendamento") Long idAgendamento){
		Map map = null;
		List lista = agendamentoEntregaService.findAgendamentoByIdAgendamento(idAgendamento);
		if(!lista.isEmpty()){
			map = (Map)lista.get(0);
			DomainValue tipoAgend = (DomainValue) map.get(TP_AGENDAMENTO);
			map.put(TP_AGENDAMENTO, tipoAgend.getDescriptionAsString());
			DomainValue situAgend = (DomainValue) map.get(TP_SITUACAO_AGENDAMENTO);
			map.put(TP_SITUACAO_AGENDAMENTO, situAgend.getDescriptionAsString());
			if(map.get(NR_DDD)!= null) {
				map.put(NR_TELEFONE,"("+map.get(NR_DDD)+")"+" "+map.get(NR_TELEFONE));
			}
			if(map.get(DH_CONTATO)!= null) {
				map.put(DH_CONTATO,JTFormatUtils.format((DateTime)map.get(DH_CONTATO)));
			}
			if(map.get(DT_AGENDAMENTO)!= null) {
				map.put(DT_AGENDAMENTO,JTFormatUtils.format((YearMonthDay)map.get(DT_AGENDAMENTO)));
			}
			if(map.get(HR_PREFERENCIA_INICIAL)!= null) {
				map.put(HR_PREFERENCIA_INICIAL,JTFormatUtils.format((TimeOfDay)map.get(HR_PREFERENCIA_INICIAL)));
			}
			if(map.get(HR_PREFERENCIA_FINAL)!= null) {
				map.put(HR_PREFERENCIA_FINAL,JTFormatUtils.format((TimeOfDay)map.get(HR_PREFERENCIA_FINAL)));
			}
			if (map.get(BL_CARTAO) != null) {
				map.put(BL_CARTAO, Boolean.TRUE.equals(map.get(BL_CARTAO)) ? SIM : NAO);
			}

		}
		return Response.ok(map).build();
	}

	@GET
	@Path("findById/complementos/outros")
	public Response findByIdDetalhamentoComplementosOutros(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		List lista = doctoServicoService.findComplementosOutros(idDoctoServico);
		Map map = null;
		Map mapConh = null;
		if(!lista.isEmpty()){
			map = (Map)lista.get(0);

			if(map.get(DH_INCLUSAO)!= null)
				map.put(DH_INCLUSAO, JTFormatUtils.format((DateTime)map.get(DH_INCLUSAO)));

			if(map.get(DH_ALTERACAO)!= null)
				map.put(DH_ALTERACAO, JTFormatUtils.format((DateTime)map.get(DH_ALTERACAO)));

			List listaConh = conhecimentoService.findComplementosOutrosIndicadorCooperacao(idDoctoServico);
			if(!listaConh.isEmpty()){

				mapConh = (Map)listaConh.get(0);

				DomainValue dv = (DomainValue)mapConh.get("tpCtrcParceria");
				if(dv != null)
					map.put("indicadorCooperacao",dv.getDescription().toString());

				if(mapConh.get(ID_CONHECIMENTO)!= null){
					map.put(ID_CONHECIMENTO, Integer.valueOf(mapConh.get(ID_CONHECIMENTO).toString()));
				}

				map.put("indicadorEDICliente",Boolean.TRUE.equals(mapConh.get("blIndicadorEdi")) ? SIM : NAO);
			}
		}
		return Response.ok(map).build();

	}

	@GET
	@Path("findById/frete/impostos/tributacao")
	public Response findTipoTributacaoIcms(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Map map = (Map)impostoServicoService.findTipoTributacaoIcms(idDoctoServico).get(0);
		DomainValue tpSituacaoPendencia = (DomainValue) map.get("tpSituacaoPendencia");
		if(tpSituacaoPendencia != null) {
			map.put("tpSituacaoPendencia", tpSituacaoPendencia.getDescriptionAsString());
		}

		String vlImpostoDifal = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get("vlImpostoDifal"));
		map.put("vlImpostoDifal", vlImpostoDifal);

		return Response.ok(map).build();
	}

	@GET
	@Path("findById/frete/impostos")
	public Response findPaginatedImpostos(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List lista = impostoServicoService.findPaginatedImpostos(idDoctoServico);
		Map mapIcmsDoctoServico = impostoServicoService.findIcmsDoctoServico(idDoctoServico);
		if(mapIcmsDoctoServico != null && mapIcmsDoctoServico.get("vlImposto") != null) {
			Map mapImposto = new HashMap();
			DomainValue dv = new DomainValue();
			dv.setValue("ICMS");
			dv.setDescription( new VarcharI18n("ICMS"));
			mapImposto.put("tpImposto",dv);
			mapImposto.put("vlBaseCalculo",mapIcmsDoctoServico.get("vlBaseCalcImposto"));
			mapImposto.put("pcAliquota",mapIcmsDoctoServico.get("pcAliquotaIcms"));
			mapImposto.put("vlImpostoServico",mapIcmsDoctoServico.get("vlImposto"));
			mapImposto.put(DS_SIMBOLO,mapIcmsDoctoServico.get(DS_SIMBOLO));
			mapImposto.put(SG_MOEDA,mapIcmsDoctoServico.get(SG_MOEDA));
			lista.add(mapImposto);
		}
		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return lista;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return lista.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/frete/parcela/tabela")
	public Response findPaginatedParcelaPreco(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List lista = parcelaDoctoServicoService.findPaginatedParcelasPreco(idDoctoServico);
		for(Iterator iter = lista.iterator();iter.hasNext();){
			Map map = (HashMap)iter.next();

			BigDecimal vlTotalParcelas = (BigDecimal)map.get(VL_TOTAL_PARCELAS);
			if(vlTotalParcelas.intValue()> 0){
				BigDecimal vlParcela = (BigDecimal)map.get(VL_PARCELA);
				BigDecimal analise = vlParcela.divide(vlTotalParcelas, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
				map.put(ANALISE,analise);
			}

			String vlTotalParcelas2 = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VL_TOTAL_PARCELAS));
			map.put(VL_TOTAL_PARCELAS,vlTotalParcelas2);
		}
		Map map = (Map)lista.get(0);
		if(map.get(VL_DESCONTO)!= null){
			Map mapDesconto = new HashMap();
			mapDesconto.put("nmParcelaPreco",configuracoesFacade.getMensagem("desconto"));
			mapDesconto.put(VL_PARCELA,map.get(VL_DESCONTO));
			mapDesconto.put(ANALISE, null);
			mapDesconto.put(DS_SIMBOLO,map.get(DS_SIMBOLO));
			mapDesconto.put(SG_MOEDA,map.get(SG_MOEDA));
			lista.add(mapDesconto);
		}

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return lista;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return lista.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/frete/calculoServico/tabela")
	public Response findPaginatedCalculoServico(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		final List lista = parcelaDoctoServicoService.findPaginatedCalculoServico(idDoctoServico);

		for(Iterator iter = lista.iterator();iter.hasNext();){
			Map map = (Map)iter.next();
			BigDecimal vlTotalServicos = (BigDecimal)map.get(VL_TOTAL_SERVICOS);
			if(vlTotalServicos.intValue()> 0){
				BigDecimal vlParcela = (BigDecimal)map.get(VL_PARCELA);
				BigDecimal analise = vlParcela.divide(vlTotalServicos, 4, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100));
				map.put(ANALISE,analise);
			}
		}

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return lista;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return lista.size();
					}

				})
				.suppressWarning()
				.build();

	}

	@GET
	@Path("findById/frete/calculoServico")
	public Response findTotaisCalculoServico(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Map map = doctoServicoService.findTotaisCalculoServico(idDoctoServico);
		if(map != null){
			if(map.get(VL_TOTAL_SERVICOS)!= null){
				String vlTotalServicos = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VL_TOTAL_SERVICOS));
				map.put(VL_TOTAL_SERVICOS,vlTotalServicos);
			}
			if(map.get(VL_TOTAL_DOC_SERVICO) != null){
				String vlTotalCTRC = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VL_TOTAL_DOC_SERVICO));
				map.put(VL_TOTAL_DOC_SERVICO,vlTotalCTRC);
			}
			if(map.get(VL_ICMSST) != null){
				String vlICMSST = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VL_ICMSST));
				map.put(VL_ICMSST,vlICMSST);
			}
			if(map.get(VL_LIQUIDO) != null){
				String vlLiquido = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VL_LIQUIDO));
				map.put(VL_LIQUIDO,vlLiquido);
			}
			if(map.get(VL_TOTAL_PARCELAS)!= null){
				String vlTotalParcelas = map.get(DS_SIMBOLO) +" "+FormatUtils.formatDecimal(BIG_DECIMAL,(BigDecimal)map.get(VL_TOTAL_PARCELAS));
				map.put(VL_TOTAL_PARCELAS,vlTotalParcelas);
			}
		}
		return Response.ok(map).build();
	}

	@GET
	@Path("findById/frete/calculo")
	public Response findDadosCalculoFrete(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		Map<String, Object> result = new HashMap<>();

		List<Map<String, Object>> dadosFrete = parcelaDoctoServicoService.findDadosCalculoFrete(idDoctoServico);
		if (dadosFrete != null && !dadosFrete.isEmpty()) {
			result.putAll(dadosFrete.get(0));
			DomainValue dvTipoTabelaPreco = (DomainValue) result.get("tpTipoTabelaPreco");
			if (dvTipoTabelaPreco != null && result.get("tpSubtipoTabelaPreco") != null && result.get("nrVersao")!= null) {
				Integer nrVersao = (Integer) result.get("nrVersao");
				String tabelaPreco = dvTipoTabelaPreco.getDescription()+String.valueOf(nrVersao.intValue())+"-"+result.get("tpSubtipoTabelaPreco").toString();
				result.put("tabelaPreco", tabelaPreco);
			}
			DomainValue dvCalculoPreco = (DomainValue)result.get("tpCalculoPreco");
			if (dvCalculoPreco!=null) {
				result.put("tpCalculoPreco", dvCalculoPreco.getDescription());
			}
		}

		List<Map<String, Object>> dadosDoctoServico = parcelaDoctoServicoService.findDadosCalculoDoctoServico(idDoctoServico);
		if (dadosDoctoServico != null && !dadosDoctoServico.isEmpty()) {
			List<Map<String, Object>> dadosReembolso = parcelaDoctoServicoService.findValorMercadoriaReembolso(idDoctoServico);
			if (dadosReembolso != null && !dadosReembolso.isEmpty()) {
				Map reembolso = dadosReembolso.get(0);
				if (reembolso.get(VALOR_MERCADORIA_REEMB) != null) {
					reembolso.put(VALOR_MERCADORIA_REEMB, reembolso.get(DS_SIMBOLO)+ " " + FormatUtils.formatDecimal(BIG_DECIMAL, (BigDecimal)reembolso.get(VALOR_MERCADORIA_REEMB)));
				}
				result.putAll(reembolso);
			}
			Map<String, Object> doctoServico = dadosDoctoServico.get(0);
			if (doctoServico.get(VL_MERCADORIA) != null) {
				doctoServico.put(VL_MERCADORIA, doctoServico.get(DS_SIMBOLO)+ " " + FormatUtils.formatDecimal(BIG_DECIMAL, (BigDecimal) doctoServico.get(VL_MERCADORIA)));
			}

			if(doctoServico.get(PS_AFERIDO) == null){
				doctoServico.put(PS_AFERIDO, FormatUtils.formatDecimal(BIG_DECIMAL_TRES_CASAS, BigDecimal.ZERO));
			}

			if(doctoServico.get("tpPesoCalculo") != null){
				doctoServico.put("dsTpPesoCalculo", ((DomainValue) doctoServico.get("tpPesoCalculo")).getDescription());
			}

			result.putAll(doctoServico);
		}

		List<Map<String, Object>> dadosInternacional = parcelaDoctoServicoService.findDadosCalculoDoctoServicoInternacional(idDoctoServico);
		if (dadosInternacional != null && !dadosInternacional.isEmpty()) {
			Map internacional = dadosInternacional.get(0);
			if(internacional.get(VL_MERCADORIA_I)!= null) {
				internacional.put(VL_MERCADORIA_I, internacional.get(DS_SIMBOLO)+ " "+ FormatUtils.formatDecimal(BIG_DECIMAL, (BigDecimal) internacional.get(VL_MERCADORIA_I)));
			}
			if(internacional.get(VL_FRETE_EXTERNO)!= null) {
				internacional.put(VL_FRETE_EXTERNO,internacional.get(DS_SIMBOLO)+ " "+ FormatUtils.formatDecimal(BIG_DECIMAL, (BigDecimal) internacional.get(VL_FRETE_EXTERNO)));
			}
			result.putAll(internacional);
		}

		return Response.ok(result).build();
	}

	@GET
	@Path("findById/cobranca/tabela")
	public Response findPaginatedDevedorDocServFatByDoctoServico(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put(ID_DOCTO_SERVICO, idDoctoServico);

		final List lista = consultarDadosCobrancaDocumentoServicoService.findDevedorDocServFatByDoctoServico(criteria);

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return lista;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return lista.size();
					}

				})
				.suppressWarning()
				.build();

	}

	@GET
	@Path("findById/cobranca")
	public Response findDevedorDocServFatDetail(@QueryParam("idDevedorDocServFat") Long idDevedorDocServFat){
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("idDevedorDocServFat", idDevedorDocServFat);

		Map map = consultarDadosCobrancaDocumentoServicoService.findDevedorDocServFatDetail(criteria);

		if(map.get(NM_PAIS)!= null) {
			map.put(NM_PAIS, map.get(NM_PAIS).toString());
		}

		if(map.get(TP_SITUACAO_COBRANCA)!= null) {
			map.put(TP_SITUACAO_COBRANCA, map.get(TP_SITUACAO_COBRANCA).toString());
		}

		if(map.get(TP_MOTIVO_DESCONTO)!= null) {
			map.put(TP_MOTIVO_DESCONTO, map.get(TP_MOTIVO_DESCONTO).toString());
		}

		if(map.get(TP_SITUACAO_APROVACAO)!= null) {
			map.put(TP_SITUACAO_APROVACAO, map.get(TP_SITUACAO_APROVACAO).toString());
		}

		if(map.get(DT_VENCIMENTO)!= null) {
			map.put(DT_VENCIMENTO, JTFormatUtils.format((YearMonthDay)map.get(DT_VENCIMENTO)).toString());
		}

		if(map.get(DT_LIQUIDACAO)!= null) {
			map.put(DT_LIQUIDACAO, JTFormatUtils.format((YearMonthDay)map.get(DT_LIQUIDACAO)).toString());
		}

		return Response.ok(map).build();
	}

	@GET
	@Path("findById/rnc")
	public Response findByIdDetailAbaRNC(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico){
		Map map = new HashMap();
		List lista = naoConformidadeService.findNaoConformidadeByIdDoctoServicoLocMerc(idDoctoServico);
		if(!lista.isEmpty()){
			map = (Map)lista.get(0);
			DomainValue dv = (DomainValue)map.get("tpStatusNaoConformidade");
			map.put("tpStatusNaoConformidade",dv.getDescription());

			if(map.get(DH_EMISSAO)!= null) {
				map.put(DH_EMISSAO, JTFormatUtils.format((DateTime)map.get(DH_EMISSAO)));
			}
		}
		return Response.ok(map).build();
	}

	@GET
	@Path("findById/rnc/tabela")
	public Response findPaginatedOcorrenciaNaoConformidade(@QueryParam("idNaoConformidade") Long idNaoConformidade){

		final List list = ocorrenciaNaoConformidadeService.findPaginatedOcorrenciaNaoConformidade(idNaoConformidade);

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return list.size();
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findById/indenizacoes")
	public Response findIndenizacaoById(@QueryParam("idDoctoServicoIndenizacao") Long idDoctoServicoIndenizacao) {
		Map<String, Object> map = doctoServicoIndenizacaoService.findMapParaLocalizacaoMercadoria(idDoctoServicoIndenizacao);

		if(map == null){
			return Response.ok().build();
		}

		if(map.get(DT_RECIBO)!= null) {
			map.put(DT_RECIBO, JTFormatUtils.format((YearMonthDay)map.get(DT_RECIBO)).toString());
		}

		if(map.get(DT_PROGRAMADA_PAGTO)!= null) {
			map.put(DT_PROGRAMADA_PAGTO, JTFormatUtils.format((YearMonthDay)map.get(DT_PROGRAMADA_PAGTO)).toString());
		}

		if(map.get(DT_PAGAMENTO_EFETUADO)!= null) {
			map.put(DT_PAGAMENTO_EFETUADO, JTFormatUtils.format((YearMonthDay)map.get(DT_PAGAMENTO_EFETUADO)).toString());
		}

		return Response.ok(map).build();

	}

	@GET
	@Path("findById/indenizacoes/tabela")
	public Response findPaginatedFilialDebitada(@QueryParam("idDoctoServicoIndenizacao") Long idDoctoServicoIndenizacao){
		final List<Map<String, Object>> list = doctoServicoIndenizacaoService.findFilialDebitadaByIdDoctoServicoIndenizacao(idDoctoServicoIndenizacao);

		if(list != null){
			return new ListResponseBuilder()
					.findClosure(new Closure<List<Map<String,Object>>>() {

						@Override
						public List<Map<String, Object>> execute() {
							return list;

						}
					})
					.rowCountClosure(new Closure<Integer>() {

						@Override
						public Integer execute() {
							return list.size();
						}

					})
					.suppressWarning()
					.build();
		}

		return Response.ok().build();
	}

	@POST
	@Path("findById/indenizacoes/eventos")
	public Response findPaginatedEventosRim(final FiltroPaginacaoDto filtro) {
		final ResultSetPage rsp = eventoRimService
				.findDoctoServicosByIdReciboIndenizacao(
						Long.valueOf(filtro.getFiltros().get(ID_RECIBO_INDENIZACAO).toString()),
						Integer.valueOf(filtro.getPagina() == null ? UM : String.valueOf(filtro.getPagina())),
						Integer.valueOf(filtro.getQtRegistrosPagina() == null ? String.valueOf(300) : String.valueOf(filtro.getQtRegistrosPagina())));

		List<Map<String, Object>> listStatus = rsp.getList();
		final List<Map<String, Object>> retorno = new ArrayList<>(listStatus.size());

		for(int i=0;i<listStatus.size();i++) {
			Map<String, Object> eventoRim = listStatus.get(i);
			DomainValue domainValue = (DomainValue) eventoRim.get("eventoRim_tpEventoIndenizacao");
			eventoRim.put("eventoRim_tpEventoIndenizacao", domainValue.getDescription().getValue());
			retorno.add(eventoRim);
		}
		Collections.sort(retorno , new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((DateTime) o1.get("eventoRim_dhEventoRim")).compareTo(o2.get("eventoRim_dhEventoRim"));
			}
		});

		return new ListResponseBuilder(filtro, -1, reportExecutionManager.getReportOutputDir(), LOCALIZACAO_MERCADORIA, filtro.getColumns())
				.findClosure(new Closure<List<Map<String,Object>>>() {

					@Override
					public List<Map<String, Object>> execute() {
						return retorno;

					}
				})
				.rowCountClosure(new Closure<Integer>() {

					@Override
					public Integer execute() {
						return eventoRimService.getRowCountEventoRimByIdReciboIndenizacao(Long.valueOf(filtro.getFiltros().get(ID_RECIBO_INDENIZACAO).toString()));
					}

				})
				.suppressWarning()
				.build();
	}

	@GET
	@Path("findTipoServico")
	public Response findTipoServico() {
		List<TipoServico> tiposServico = tipoServicoService.find(new HashMap<>());
		List<Map<String, Object>> list = new ListToMapConverter<TipoServico>().mapRows(tiposServico, new RowMapper<TipoServico>() {
			@Override
			public Map<String, Object> mapRow(TipoServico o) {
				Map<String, Object> toReturn = new HashMap<>();
				toReturn.put("idTipoServico", o.getIdTipoServico());
				toReturn.put("dsTipoServico", o.getDsTipoServico().getValue());
				return toReturn;
			}
		});
		return Response.ok(list).build();
	}

	@POST
	@Path("visualizar")
	public Response visualizar(Map<String, String> filtros) throws Exception {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put(ID_DOCTO_SERVICO, Long.valueOf(filtros.get(ID_DOCTO_SERVICO)));
		Map<String, String> retorno = new HashMap<>();
		String fileName = reportExecutionManager.generateReportLocator(emitirLocalizacaoMercadoriaService, parameters);
		retorno.put(FILE_NAME, fileName);
		return Response.ok(retorno).build();
	}

	@POST
	@Path("imprimirInformacoesVolumes")
	public Response imprimirInformacoesVolumes(Map<String, String> filtros) throws Exception {
		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put(ID_DOCTO_SERVICO, Long.valueOf(filtros.get(ID_DOCTO_SERVICO)));
		parameters.put("formatoRelatorio", filtros.get("formatoRelatorio"));
		Map<String, String> retorno = new HashMap<>();
		String fileName = reportExecutionManager.generateReportLocator(emitirRelatorioDadosVolumesDocumentoService, parameters);
		retorno.put(FILE_NAME, fileName);
		return Response.ok(retorno).build();
	}


	@POST
	@Path("imprimirRNC")
	public Response imprimirRNC(Map<String, String> filtros) throws Exception {

		TypedFlatMap parameters = new TypedFlatMap();
		parameters.put("naoConformidade.idNaoConformidade", Long.valueOf(filtros.get("idNaoConformidade")));
		parameters.put("naoConformidade.nrNaoConformidade", Long.valueOf(filtros.get("nrNaoConformidade")));
		parameters.put("naoConformidade.filial.sgFilial", filtros.get(SG_FILIAL));

		Map<String, String> retorno = new HashMap<>();
		String fileName = reportExecutionManager.generateReportLocator(emitirRNCService, parameters);
		retorno.put(FILE_NAME, fileName);
		return Response.ok(retorno).build();

	}

	@GET
	@Path("findRespostaAbasDetalhamento")
	public Response findRespostaAbasDetalhamento(@QueryParam(ID_DOCTO_SERVICO) Long idDoctoServico) {
		Map mapAbas = new HashMap();

		boolean abaRNC = naoConformidadeService.findNCByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaRNC", abaRNC);

		boolean abaBloqueio = ocorrenciaDoctoServicoService.findOcorDSByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaBloqueio",abaBloqueio);

		boolean abaParceiras =  ctoCtoCooperadaService.findCoopByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaParceiras",abaParceiras);

		boolean abaCC =  preManifestoDocumentoService.findCCByIdDoctoServico(idDoctoServico);
		if(!abaCC) {
			abaCC =  doctoServicoService.findCCByIdDoctoServico(idDoctoServico);
			if(!abaCC) {
				abaCC = detalheColetaService.findCCByIdDoctoServicoDetalheColeta(idDoctoServico);
			}
		}
		mapAbas.put("abaCC",abaCC);

		mapAbas.put("blIndenizacoes", configuracoesFacade.getValorParametro("BL_INDENIZACOES_LMS"));

		boolean abaAgend = agendamentoEntregaService.findAgendamentosAba(idDoctoServico);
		mapAbas.put("abaAgend",abaAgend);

		boolean abaEmb =  servicoEmbalagemService.findEmbalagensAba(idDoctoServico);
		mapAbas.put("abaEmb",abaEmb);

		boolean abaDados = dadosComplementoService.findDadosComplAba(idDoctoServico);
		mapAbas.put("abaDados",abaDados);

		boolean abaAWB = this.findAwbByDocto(idDoctoServico) != null;
		mapAbas.put("abaAWB",abaAWB);

		return Response.ok(mapAbas).build();
	}


	private InformacaoDoctoClienteDTO converteInformacaoDoctoCliente(InformacaoDoctoCliente docto) {
		return new InformacaoDoctoClienteDTO(
				docto.getIdInformacaoDoctoCliente(),
				docto.getDsCampo(),
				docto.getTpCampo().getValue(),
				docto.getDsFormatacao(),
				docto.getNrTamanho(),
				docto.getBlOpcional(),
				docto.getDsValorPadrao(),
				docto.getBlValorFixo());
	}

	@POST
	@Path("/findInformacoesCliente")
	public Response findInformacoesCliente(Map<String, Object> parametros) {
		Map<String, Object> mapFind = new HashMap<>();
		mapFind.put("modal", parametros.get("modal"));
		mapFind.put("abrangencia", parametros.get("abrangencia"));
		mapFind.put("cliente.idCliente", parametros.get(ID_CLIENTE));

		List<InformacaoDoctoClienteDTO> result = new ArrayList<>();
		List<InformacaoDoctoCliente> informacoes = informacaoDoctoClienteService.find(mapFind);
		if (informacoes != null) {
			for (InformacaoDoctoCliente doctoCliente : informacoes) {
				result.add(converteInformacaoDoctoCliente(doctoCliente));
			}
		}
		return Response.ok(result).build();
	}

	@POST
	@Path("storeRemark")
	public Response storeRemark(Map<String, Object> parametros) {
		Long idDoctoServico = Long.parseLong(parametros.get(ID_DOCTO_SERVICO).toString());
		String dsObservacao = parametros.get("dsObservacao").toString();
		observacaoMercadoriaService.saveObservacao(idDoctoServico, dsObservacao);
		return Response.ok().build();
	}

	@POST
	@Path("storeBlAgendamento")
	public Response storeBlAgendamento(Map<String, Object> parametros){
		Long idDoctoServico = Long.parseLong(parametros.get(ID_DOCTO_SERVICO).toString());
		Boolean blAgendamentoObrigatorio = Boolean.parseBoolean(parametros.get("blAgendamentoBloqueio").toString());
		Conhecimento conhecimento = conhecimentoService.findByIdDoctoServico(idDoctoServico);
		conhecimento.setBlObrigaAgendamento(blAgendamentoObrigatorio);
		conhecimentoService.store(conhecimento);
		return Response.ok().build();
	}

	@POST
	@Path("findRemarks")
	public Response findRemarks(FiltroPaginacaoDto filtro) {

		Long idDoctoServico = Long.parseLong(filtro.getFiltros().get(ID_DOCTO_SERVICO).toString());
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Map<String, Object> map;
		List<ObservacaoMercadoria> observacaoMercadoriaList = observacaoMercadoriaService.findByDoctoServico(idDoctoServico);

		final Integer qtRegistros = observacaoMercadoriaList.size();
		final List<Map<String, Object>> list = new ArrayList<>();

		for (ObservacaoMercadoria om : observacaoMercadoriaList) {
			map = new HashMap<>();
			map.put(DH_INCLUSAO, sdf.format(om.getDhInclusao().toDate()));

			map.put("nmUsuario", usuarioService.findById(om.getUsuarioInclusao().getUsuarioADSM().getIdUsuario()).getNmUsuario());
			map.put(SG_FILIAL, observacaoMercadoriaService.findSgFilialFromIdUsuario(om.getUsuarioInclusao().getUsuarioADSM().getIdUsuario()));

			map.put("dsObservacao", om.getDsObservacao());
			list.add(map);
		}

		return new ListResponseBuilder()
				.findClosure(new Closure<List<Map<String,Object>>>() {
					@Override
					public List<Map<String, Object>> execute() {
						return list;

					}
				})
				.rowCountClosure(new Closure<Integer>() {
					@Override
					public Integer execute() {
						return qtRegistros;
					}

				})
				.suppressWarning()
				.build();
	}

}
