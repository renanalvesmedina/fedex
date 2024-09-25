package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.workflow.model.Pendencia;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;

/**
 * @author LMS Custom Hibernate CodeGenerator
 */
public class DoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * identifier field
	 */
	private Long idDoctoServico;

	/**
	 * identifier field
	 */
	private Long nrDoctoServico;

	/**
	 * persistent field
	 */
	private BigDecimal vlTotalDocServico;

	/**
	 * persistent field
	 */
	private BigDecimal vlTotalParcelas;

	/**
	 * persistent field
	 */
	private BigDecimal vlTotalServicos;

	/**
	 * persistent field
	 */
	private BigDecimal vlImposto;

	/**
	 * persistent field
	 */
	private BigDecimal vlImpostoPesoDeclarado;

	/**
	 * persistent field
	 */
	private BigDecimal vlDesconto;

	/**
	 * transient field
	 */
	private BigDecimal vlLiquido;

	/**
	 * persistent field
	 */
	private BigDecimal vlFretePesoDeclarado;

	/**
	 * persistent field
	 */
	private BigDecimal vlFreteTabelaCheia;

	/**
	 * transient field
	 */
	private transient Boolean blParcelas;

	/**
	 * transient field
	 */
	private transient Boolean blServicosAdicionais;

	/**
	 * transient field
	 */
	private transient String dsCodigoBarras;

	/**
	 * persistent field
	 */
	private BigDecimal vlMercadoria;

	/**
	 * persistent field
	 */
	private DateTime dhEmissao;

	/**
	 * persistent field
	 */
	private DateTime dhInclusao;

	/**
	 * persistent field
	 */
	private DomainValue tpDocumentoServico;

	/**
	 * persistent field
	 */
	private Boolean blBloqueado;

	/**
	 * nullable persistent field
	 */
	private BigDecimal vlBaseCalcImposto;

	/**
	 * nullable persistent field
	 */
	private BigDecimal vlIcmsSubstituicaoTributaria;

	/**
	 * nullable persistent field
	 */
	private BigDecimal vlIcmsSubstituicaoTributariaPesoDeclarado;

	/**
	 * persistent field
	 */
	private Boolean blIncideIcmsPedagio;

	/**
	 * nullable persistent field
	 */
	private Short nrCfop;

	/**
	 * nullable persistent field
	 */
	private BigDecimal psReferenciaCalculo;

	/**
	 * nullable persistent field
	 */
	private DateTime dhAlteracao;

	/**
	 * nullable persistent field
	 */
	private DateTime dhEntradaSetorEntrega;

	/**
	 * nullable persistent field
	 */
	private DomainValue tpCalculoPreco;

	/**
	 * nullable persistent field
	 */
	private DomainValue tpMotivoLiberacao;

	/**
	 * nullable persistent field
	 */
	private Boolean blPrioridadeCarregamento;

	/**
	 * nullable persistent field
	 */
	private String nrAidf;

	/**
	 * nullable persistent field
	 */
	private BigDecimal pcAliquotaIcms;

	/**
	 * persistent field
	 */
	private BigDecimal psReal;

	/**
	 * nullable persistent field
	 */
	private BigDecimal psAforado;

	/**
	 * persistent field
	 */
	private BigDecimal psAferido;

	/**
	 * persistent field
	 */
	private Integer qtVolumes;

	/**
	 * nullable persistent field
	 */
	private YearMonthDay dtPrevEntrega;

	/**
	 * nullable persistent field
	 */
	private Short nrDiasPrevEntrega;

	/**
	 * nullable persistent field
	 */
	private String dsEnderecoEntregaReal;

	/**
	 * nullable persistent field
	 */
	private Short nrDiasRealEntrega;

	/**
	 * nullable persistent field
	 */
	private Short nrDiasBloqueio;

	/**
	 * nullable persistent field
	 */
	private String obComplementoLocalizacao;

	/**
	 * nullable persistent field
	 */
	private Boolean blSpecialService;

	/**
	 * nullable persistent field
	 */
	private BigDecimal nrFatorDensidade;

	/**
	 * persistent field
	 */
	private Cliente clienteByIdClienteBaseCalculo;

	/**
	 * persistent field
	 */
	private Cliente clienteByIdClienteDestinatario;

	/**
	 * persistent field
	 */
	private Cliente clienteByIdClienteConsignatario;

	/**
	 * persistent field
	 */
	private Cliente clienteByIdClienteRemetente;

	/**
	 * persistent field
	 */
	private Cliente clienteByIdClienteRedespacho;

	/**
	 * persistent field
	 */
	private Usuario usuarioByIdUsuarioInclusao;

	/**
	 * persistent field
	 */
	private Usuario usuarioByIdUsuarioAlteracao;

	/**
	 * persistent field
	 */
	private EnderecoPessoa enderecoPessoa;

	/**
	 * persistent field
	 */
	private FluxoFilial fluxoFilial;

	/**
	 * persistent field
	 */
	private Filial filialDestinoOperacional;

	/**
	 * persistent field
	 */
	private InscricaoEstadual inscricaoEstadualRemetente;

	/**
	 * persistent field
	 */
	private InscricaoEstadual inscricaoEstadualDestinatario;

	/**
	 * persistent field
	 */
	private TabelaPreco tabelaPreco;

	/**
	 * persistent field
	 */
	private Moeda moeda;

	/**
	 * persistent field
	 */
	private Servico servico;

	/**
	 * persistent field
	 */
	private DivisaoCliente divisaoCliente;

	/**
	 * persistent field
	 */
	private RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaReal;

	/**
	 * persistent field
	 */
	private RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaSugerid;

	/**
	 * persistent field
	 */
	private PedidoColeta pedidoColeta;

	/**
	 * persistent field
	 */
	private Filial filialByIdFilialDestino;

	/**
	 * persistent field
	 */
	private Filial filialByIdFilialOrigem;

	/**
	 * persistent field
	 */
	private Pais paisOrigem;

	/**
	 * persistent field
	 */
	private RotaIntervaloCep rotaIntervaloCep;

	/**
	 * persistent field
	 */
	private RotaIdaVolta rotaIdaVolta;

	/**
	 * persistent field
	 */
	private ParametroCliente parametroCliente;

	/**
	 * persistent field
	 */
	private TarifaPreco tarifaPreco;

	/**
	 * persistent field
	 */
	private Impressora impressora;

	/**
	 * persistent field
	 */
	private LocalizacaoMercadoria localizacaoMercadoria;

	/**
	 * persistent field
	 */
	private Filial filialLocalizacao;

	/**
	 * persistent field
	 */
	private DoctoServico doctoServicoOriginal;

	/**
	 * persistent field
	 */
	private List<DoctoServicoIndenizacao> doctoServicoIndenizacoes;

	/**
	 * persistent field
	 */
	private List cartaMercadoriaDisposicoes;

	/**
	 * persistent field
	 */
	private List<LiberacaoDocServ> liberacaoDocServs;

	/**
	 * persistent field
	 */
	private List<DevedorDocServFat> devedorDocServFats;

	/**
	 * persistent field
	 */
	private List agendamentoDoctoServicos;

	/**
	 * persistent field
	 */
	private List documentoServicoRetiradas;

	/**
	 * persistent field
	 */
	private List servicoEmbalagems;

	/**
	 * persistent field
	 */
	private List<ServAdicionalDocServ> servAdicionalDocServs;

	/**
	 * persistent field
	 */
	private List<OcorrenciaDoctoServico> ocorrenciaDoctoServicos;

	/**
	 * persistent field
	 */
	private List<NaoConformidade> naoConformidades;

	/**
	 * persistent field
	 */
	private List valorCustos;

	private DomainValue tpSituacaoConhecimento;

	/**
	 * persistent field
	 */
	private List<EventoDocumentoServico> eventoDocumentoServicos;

	/**
	 * persistent field
	 */
	private List<PreManifestoDocumento> preManifestoDocumentos;

	/**
	 * persistent field
	 */
	private List<PreManifestoVolume> preManifestoVolumes;

	/**
	 * persistent field
	 */
	private List<ObservacaoDoctoServico> observacaoDoctoServicos;

	/**
	 * persistent field
	 */
	private List reciboReembolsoDoctoServicos;

	/**
	 * persistent field
	 */
	private List<ManifestoEntregaDocumento> manifestoEntregaDocumentos;

	/**
	 * persistent field
	 */
	private List<ManifestoEntregaVolume> manifestoEntregaVolumes;

	/**
	 * persistent field
	 */
	private List itemMdasDoctoServico;

	/**
	 * persistent field
	 */
	private List reciboReembolsosByIdDoctoServReembolsado;

	/**
	 * persistent field
	 */
	private List<ParcelaDoctoServico> parcelaDoctoServicos;

	/**
	 * persistent field
	 */
	private List<SinistroDoctoServico> sinistroDoctoServicos;

	/**
	 * persistent field
	 */
	private List<DevedorDocServ> devedorDocServs;

	/**
	 * persistent field
	 */
	private List mercadoriaPendenciaMzs;

	/**
	 * persistent field
	 */
	private List<Cotacao> cotacoes;

	/**
	 * persistent field
	 */
	private List registroDocumentoEntregas;

	/**
	 * persistent field
	 */
	private List justificativasDoctosNaoCarregados;

	private transient Boolean blExecutarVerificacaoDocumentoManifestado = Boolean.FALSE;

	private Boolean blPaletizacao;

	private List<NotaCreditoDocto> notaCreditoDoctos;
	/**
	 * nullable persistent field
	 */
	private BigDecimal psCubadoReal;

	/**
	 * nullable persistent field
	 */
	private BigDecimal nrFatorCubagem;

	/**
	 * nullable persistent field
	 */
	private BigDecimal psEstatistico;

	/**
	 * nullable persistent field
	 */
	private BigDecimal nrCubagemEstatistica;

	/**
	 * nullable persistent field
	 */
	private BigDecimal nrFatorCubagemCliente;

	/**
	 * nullable persistent field
	 */
	private BigDecimal nrFatorCubagemSegmento;

	/**
	 * nullable persistent field
	 */
	private DomainValue tpPesoCalculo;

	/**
	 * persistent field
	 */
	private Boolean blUtilizaPesoEdi;

	/**
	 * persistent field
	 */
	private Boolean blPesoFatPorCubadoAferido;

	/**
	 * persistent field
	 */
	private Boolean blPesoCubadoPorDensidade;

	/**
	 * persistent field
	 */
	private BigDecimal vlTotalSorter;

	/**
	 * persistent field
	 */
	private BigDecimal vlLiquidoSorter;

	//LMS-2353
	private BigDecimal nrCubagemAferida;

	private BigDecimal nrCubagemCalculo;

	private BigDecimal psCubadoAferido;

	private BigDecimal psCubadoDeclarado;

	private BigDecimal nrCubagemDeclarada;

	private List<ImpostoServico> impostoServicosNFS;

	private String nrChaveNfAnulacao;

	private BigDecimal nrNfAnulacao;

	private String dsSerieAnulacao;

	private YearMonthDay dtEmissaoNfAnulacao;

	private BigDecimal vlNfAnulacao;

	private Short nrDiasAgendamento;

	private Short nrDiasTentativasEntregas;

	private YearMonthDay dtProjetadaEntrega;

	private String dsMotivoAnulacao;

	private List<DoctoServicoFranqueado> doctoServicoFranqueados;

	private List<DetalheColeta> detalheColetas;

    private DomainValue tpCargaDocumento;

	/**
	 * persistent field
	 */
	private Boolean blDesconsiderouPesoCubado;

	private Boolean blFluxoSubcontratacao;

	private BigDecimal vlImpostoDifal;

	private BigDecimal pcIcmsUfFim;

	private DomainValue tpSituacaoPendencia;

	private Pendencia pendencia;

	public DoctoServico() {
	}

	public DoctoServico(Long idDoctoServico, Long nrDoctoServico, BigDecimal vlTotalDocServico, BigDecimal vlTotalParcelas, BigDecimal vlTotalServicos, BigDecimal vlImposto, BigDecimal vlImpostoPesoDeclarado, BigDecimal vlDesconto, BigDecimal vlLiquido, BigDecimal vlFretePesoDeclarado, BigDecimal vlFreteTabelaCheia, Boolean blParcelas, Boolean blServicosAdicionais, String dsCodigoBarras, BigDecimal vlMercadoria, DateTime dhEmissao, DateTime dhInclusao, DomainValue tpDocumentoServico, Boolean blBloqueado, BigDecimal vlBaseCalcImposto, BigDecimal vlIcmsSubstituicaoTributaria, BigDecimal vlIcmsSubstituicaoTributariaPesoDeclarado, Boolean blIncideIcmsPedagio, Short nrCfop, BigDecimal psReferenciaCalculo, DateTime dhAlteracao, DateTime dhEntradaSetorEntrega, DomainValue tpCalculoPreco, Boolean blPrioridadeCarregamento, String nrAidf, BigDecimal pcAliquotaIcms, BigDecimal psReal, BigDecimal psAforado, BigDecimal psAferido, Integer qtVolumes, YearMonthDay dtPrevEntrega, Short nrDiasPrevEntrega, String dsEnderecoEntregaReal, Short nrDiasRealEntrega, Short nrDiasBloqueio, String obComplementoLocalizacao, Boolean blSpecialService, BigDecimal nrFatorDensidade, Cliente clienteByIdClienteBaseCalculo, Cliente clienteByIdClienteDestinatario, Cliente clienteByIdClienteConsignatario, Cliente clienteByIdClienteRemetente, Cliente clienteByIdClienteRedespacho, Usuario usuarioByIdUsuarioInclusao, Usuario usuarioByIdUsuarioAlteracao, EnderecoPessoa enderecoPessoa, FluxoFilial fluxoFilial, Filial filialDestinoOperacional, InscricaoEstadual inscricaoEstadualRemetente, InscricaoEstadual inscricaoEstadualDestinatario, TabelaPreco tabelaPreco, Moeda moeda, Servico servico, DivisaoCliente divisaoCliente, RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaReal, RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaSugerid, PedidoColeta pedidoColeta, Filial filialByIdFilialDestino, Filial filialByIdFilialOrigem, Pais paisOrigem, RotaIntervaloCep rotaIntervaloCep, RotaIdaVolta rotaIdaVolta, ParametroCliente parametroCliente, TarifaPreco tarifaPreco, Impressora impressora, LocalizacaoMercadoria localizacaoMercadoria, Filial filialLocalizacao, DoctoServico doctoServicoOriginal, List<DoctoServicoIndenizacao> doctoServicoIndenizacoes, List cartaMercadoriaDisposicoes, List<LiberacaoDocServ> liberacaoDocServs, List<DevedorDocServFat> devedorDocServFats, List agendamentoDoctoServicos, List documentoServicoRetiradas, List servicoEmbalagems, List<ServAdicionalDocServ> servAdicionalDocServs, List<OcorrenciaDoctoServico> ocorrenciaDoctoServicos, List<NaoConformidade> naoConformidades, List valorCustos, DomainValue tpSituacaoConhecimento, List<EventoDocumentoServico> eventoDocumentoServicos, List<PreManifestoDocumento> preManifestoDocumentos, List<PreManifestoVolume> preManifestoVolumes, List<ObservacaoDoctoServico> observacaoDoctoServicos, List reciboReembolsoDoctoServicos, List<ManifestoEntregaDocumento> manifestoEntregaDocumentos, List<ManifestoEntregaVolume> manifestoEntregaVolumes, List itemMdasDoctoServico, List reciboReembolsosByIdDoctoServReembolsado, List<ParcelaDoctoServico> parcelaDoctoServicos, List<SinistroDoctoServico> sinistroDoctoServicos, List<DevedorDocServ> devedorDocServs, List mercadoriaPendenciaMzs, List<Cotacao> cotacoes, List registroDocumentoEntregas, List justificativasDoctosNaoCarregados, Boolean blExecutarVerificacaoDocumentoManifestado, Boolean blPaletizacao, List<NotaCreditoDocto> notaCreditoDoctos, BigDecimal psCubadoReal, BigDecimal nrFatorCubagem, BigDecimal psEstatistico, BigDecimal nrCubagemEstatistica, BigDecimal nrFatorCubagemCliente, BigDecimal nrFatorCubagemSegmento, DomainValue tpPesoCalculo, Boolean blUtilizaPesoEdi, Boolean blPesoFatPorCubadoAferido, Boolean blPesoCubadoPorDensidade, BigDecimal vlTotalSorter, BigDecimal vlLiquidoSorter, BigDecimal nrCubagemAferida, BigDecimal nrCubagemCalculo, BigDecimal psCubadoAferido, BigDecimal psCubadoDeclarado, BigDecimal nrCubagemDeclarada, List<ImpostoServico> impostoServicosNFS, String nrChaveNfAnulacao, BigDecimal nrNfAnulacao, String dsSerieAnulacao, YearMonthDay dtEmissaoNfAnulacao, BigDecimal vlNfAnulacao, Short nrDiasAgendamento, Short nrDiasTentativasEntregas, YearMonthDay dtProjetadaEntrega, String dsMotivoAnulacao, List<DoctoServicoFranqueado> doctoServicoFranqueados, List<DetalheColeta> detalheColetas, BigDecimal vlImpostoDifal, DomainValue tpSituacaoPendencia) {
		this.idDoctoServico = idDoctoServico;
		this.nrDoctoServico = nrDoctoServico;
		this.vlTotalDocServico = vlTotalDocServico;
		this.vlTotalParcelas = vlTotalParcelas;
		this.vlTotalServicos = vlTotalServicos;
		this.vlImposto = vlImposto;
		this.vlImpostoPesoDeclarado = vlImpostoPesoDeclarado;
		this.vlDesconto = vlDesconto;
		this.vlLiquido = vlLiquido;
		this.vlFretePesoDeclarado = vlFretePesoDeclarado;
		this.vlFreteTabelaCheia = vlFreteTabelaCheia;
		this.blParcelas = blParcelas;
		this.blServicosAdicionais = blServicosAdicionais;
		this.dsCodigoBarras = dsCodigoBarras;
		this.vlMercadoria = vlMercadoria;
		this.dhEmissao = dhEmissao;
		this.dhInclusao = dhInclusao;
		this.tpDocumentoServico = tpDocumentoServico;
		this.blBloqueado = blBloqueado;
		this.vlBaseCalcImposto = vlBaseCalcImposto;
		this.vlIcmsSubstituicaoTributaria = vlIcmsSubstituicaoTributaria;
		this.vlIcmsSubstituicaoTributariaPesoDeclarado = vlIcmsSubstituicaoTributariaPesoDeclarado;
		this.blIncideIcmsPedagio = blIncideIcmsPedagio;
		this.nrCfop = nrCfop;
		this.psReferenciaCalculo = psReferenciaCalculo;
		this.dhAlteracao = dhAlteracao;
		this.dhEntradaSetorEntrega = dhEntradaSetorEntrega;
		this.tpCalculoPreco = tpCalculoPreco;
		this.blPrioridadeCarregamento = blPrioridadeCarregamento;
		this.nrAidf = nrAidf;
		this.pcAliquotaIcms = pcAliquotaIcms;
		this.psReal = psReal;
		this.psAforado = psAforado;
		this.psAferido = psAferido;
		this.qtVolumes = qtVolumes;
		this.dtPrevEntrega = dtPrevEntrega;
		this.nrDiasPrevEntrega = nrDiasPrevEntrega;
		this.dsEnderecoEntregaReal = dsEnderecoEntregaReal;
		this.nrDiasRealEntrega = nrDiasRealEntrega;
		this.nrDiasBloqueio = nrDiasBloqueio;
		this.obComplementoLocalizacao = obComplementoLocalizacao;
		this.blSpecialService = blSpecialService;
		this.nrFatorDensidade = nrFatorDensidade;
		this.clienteByIdClienteBaseCalculo = clienteByIdClienteBaseCalculo;
		this.clienteByIdClienteDestinatario = clienteByIdClienteDestinatario;
		this.clienteByIdClienteConsignatario = clienteByIdClienteConsignatario;
		this.clienteByIdClienteRemetente = clienteByIdClienteRemetente;
		this.clienteByIdClienteRedespacho = clienteByIdClienteRedespacho;
		this.usuarioByIdUsuarioInclusao = usuarioByIdUsuarioInclusao;
		this.usuarioByIdUsuarioAlteracao = usuarioByIdUsuarioAlteracao;
		this.enderecoPessoa = enderecoPessoa;
		this.fluxoFilial = fluxoFilial;
		this.filialDestinoOperacional = filialDestinoOperacional;
		this.inscricaoEstadualRemetente = inscricaoEstadualRemetente;
		this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
		this.tabelaPreco = tabelaPreco;
		this.moeda = moeda;
		this.servico = servico;
		this.divisaoCliente = divisaoCliente;
		this.rotaColetaEntregaByIdRotaColetaEntregaReal = rotaColetaEntregaByIdRotaColetaEntregaReal;
		this.rotaColetaEntregaByIdRotaColetaEntregaSugerid = rotaColetaEntregaByIdRotaColetaEntregaSugerid;
		this.pedidoColeta = pedidoColeta;
		this.filialByIdFilialDestino = filialByIdFilialDestino;
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
		this.paisOrigem = paisOrigem;
		this.rotaIntervaloCep = rotaIntervaloCep;
		this.rotaIdaVolta = rotaIdaVolta;
		this.parametroCliente = parametroCliente;
		this.tarifaPreco = tarifaPreco;
		this.impressora = impressora;
		this.localizacaoMercadoria = localizacaoMercadoria;
		this.filialLocalizacao = filialLocalizacao;
		this.doctoServicoOriginal = doctoServicoOriginal;
		this.doctoServicoIndenizacoes = doctoServicoIndenizacoes;
		this.cartaMercadoriaDisposicoes = cartaMercadoriaDisposicoes;
		this.liberacaoDocServs = liberacaoDocServs;
		this.devedorDocServFats = devedorDocServFats;
		this.agendamentoDoctoServicos = agendamentoDoctoServicos;
		this.documentoServicoRetiradas = documentoServicoRetiradas;
		this.servicoEmbalagems = servicoEmbalagems;
		this.servAdicionalDocServs = servAdicionalDocServs;
		this.ocorrenciaDoctoServicos = ocorrenciaDoctoServicos;
		this.naoConformidades = naoConformidades;
		this.valorCustos = valorCustos;
		this.tpSituacaoConhecimento = tpSituacaoConhecimento;
		this.eventoDocumentoServicos = eventoDocumentoServicos;
		this.preManifestoDocumentos = preManifestoDocumentos;
		this.preManifestoVolumes = preManifestoVolumes;
		this.observacaoDoctoServicos = observacaoDoctoServicos;
		this.reciboReembolsoDoctoServicos = reciboReembolsoDoctoServicos;
		this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
		this.manifestoEntregaVolumes = manifestoEntregaVolumes;
		this.itemMdasDoctoServico = itemMdasDoctoServico;
		this.reciboReembolsosByIdDoctoServReembolsado = reciboReembolsosByIdDoctoServReembolsado;
		this.parcelaDoctoServicos = parcelaDoctoServicos;
		this.sinistroDoctoServicos = sinistroDoctoServicos;
		this.devedorDocServs = devedorDocServs;
		this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
		this.cotacoes = cotacoes;
		this.registroDocumentoEntregas = registroDocumentoEntregas;
		this.justificativasDoctosNaoCarregados = justificativasDoctosNaoCarregados;
		this.blExecutarVerificacaoDocumentoManifestado = blExecutarVerificacaoDocumentoManifestado;
		this.blPaletizacao = blPaletizacao;
		this.notaCreditoDoctos = notaCreditoDoctos;
		this.psCubadoReal = psCubadoReal;
		this.nrFatorCubagem = nrFatorCubagem;
		this.psEstatistico = psEstatistico;
		this.nrCubagemEstatistica = nrCubagemEstatistica;
		this.nrFatorCubagemCliente = nrFatorCubagemCliente;
		this.nrFatorCubagemSegmento = nrFatorCubagemSegmento;
		this.tpPesoCalculo = tpPesoCalculo;
		this.blUtilizaPesoEdi = blUtilizaPesoEdi;
		this.blPesoFatPorCubadoAferido = blPesoFatPorCubadoAferido;
		this.blPesoCubadoPorDensidade = blPesoCubadoPorDensidade;
		this.vlTotalSorter = vlTotalSorter;
		this.vlLiquidoSorter = vlLiquidoSorter;
		this.nrCubagemAferida = nrCubagemAferida;
		this.nrCubagemCalculo = nrCubagemCalculo;
		this.psCubadoAferido = psCubadoAferido;
		this.psCubadoDeclarado = psCubadoDeclarado;
		this.nrCubagemDeclarada = nrCubagemDeclarada;
		this.impostoServicosNFS = impostoServicosNFS;
		this.nrChaveNfAnulacao = nrChaveNfAnulacao;
		this.nrNfAnulacao = nrNfAnulacao;
		this.dsSerieAnulacao = dsSerieAnulacao;
		this.dtEmissaoNfAnulacao = dtEmissaoNfAnulacao;
		this.vlNfAnulacao = vlNfAnulacao;
		this.nrDiasAgendamento = nrDiasAgendamento;
		this.nrDiasTentativasEntregas = nrDiasTentativasEntregas;
		this.dtProjetadaEntrega = dtProjetadaEntrega;
		this.dsMotivoAnulacao = dsMotivoAnulacao;
		this.doctoServicoFranqueados = doctoServicoFranqueados;
		this.detalheColetas = detalheColetas;
		this.vlImpostoDifal = vlImpostoDifal;
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

	public DoctoServico(Long idDoctoServico, Long nrDoctoServico, BigDecimal vlTotalDocServico, BigDecimal vlTotalParcelas, BigDecimal vlTotalServicos, BigDecimal vlImposto, BigDecimal vlImpostoPesoDeclarado, BigDecimal vlDesconto, BigDecimal vlLiquido, BigDecimal vlFretePesoDeclarado, BigDecimal vlFreteTabelaCheia, Boolean blParcelas, Boolean blServicosAdicionais, String dsCodigoBarras, BigDecimal vlMercadoria, DateTime dhEmissao, DateTime dhInclusao, DomainValue tpDocumentoServico, Boolean blBloqueado, BigDecimal vlBaseCalcImposto, BigDecimal vlIcmsSubstituicaoTributaria, BigDecimal vlIcmsSubstituicaoTributariaPesoDeclarado) {
		this.idDoctoServico = idDoctoServico;
		this.nrDoctoServico = nrDoctoServico;
		this.vlTotalDocServico = vlTotalDocServico;
		this.vlTotalParcelas = vlTotalParcelas;
		this.vlTotalServicos = vlTotalServicos;
		this.vlImposto = vlImposto;
		this.vlImpostoPesoDeclarado = vlImpostoPesoDeclarado;
		this.vlDesconto = vlDesconto;
		this.vlLiquido = vlLiquido;
		this.vlFretePesoDeclarado = vlFretePesoDeclarado;
		this.vlFreteTabelaCheia = vlFreteTabelaCheia;
		this.blParcelas = blParcelas;
		this.blServicosAdicionais = blServicosAdicionais;
		this.dsCodigoBarras = dsCodigoBarras;
		this.vlMercadoria = vlMercadoria;
		this.dhEmissao = dhEmissao;
		this.dhInclusao = dhInclusao;
		this.tpDocumentoServico = tpDocumentoServico;
		this.blBloqueado = blBloqueado;
		this.vlBaseCalcImposto = vlBaseCalcImposto;
		this.vlIcmsSubstituicaoTributaria = vlIcmsSubstituicaoTributaria;
		this.vlIcmsSubstituicaoTributariaPesoDeclarado = vlIcmsSubstituicaoTributariaPesoDeclarado;
	}

	public Long getIdDoctoServico() {
		return this.idDoctoServico;
	}

	public void setIdDoctoServico(Long idDoctoServico) {
		this.idDoctoServico = idDoctoServico;
	}

	public Long getNrDoctoServico() {
		return this.nrDoctoServico;
	}

	public void setNrDoctoServico(Long nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}

	public BigDecimal getVlTotalDocServico() {
		return this.vlTotalDocServico;
	}

	public void setVlTotalDocServico(BigDecimal vlTotalDocServico) {
		this.vlTotalDocServico = vlTotalDocServico;
	}

	public BigDecimal getVlTotalParcelas() {
		return this.vlTotalParcelas;
	}

	public void setVlTotalParcelas(BigDecimal vlTotalParcelas) {
		this.vlTotalParcelas = vlTotalParcelas;
	}

	public BigDecimal getVlTotalServicos() {
		return this.vlTotalServicos;
	}

	public void setVlTotalServicos(BigDecimal vlTotalServicos) {
		this.vlTotalServicos = vlTotalServicos;
	}

	public BigDecimal getVlImposto() {
		return this.vlImposto;
	}

	public void setVlImposto(BigDecimal vlImposto) {
		this.vlImposto = vlImposto;
	}

	public BigDecimal getVlDesconto() {
		return this.vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public BigDecimal getVlLiquido() {
		return vlLiquido;
	}

	public void setVlLiquido(BigDecimal vlLiquido) {
		this.vlLiquido = vlLiquido;
	}

	public BigDecimal getVlFretePesoDeclarado() {
		return this.vlFretePesoDeclarado;
	}

	public void setVlFretePesoDeclarado(BigDecimal vlFretePesoDeclarado) {
		this.vlFretePesoDeclarado = vlFretePesoDeclarado;
	}

	public BigDecimal getVlFreteTabelaCheia() {
		return this.vlFreteTabelaCheia;
	}

	public void setVlFreteTabelaCheia(BigDecimal vlFreteTabelaCheia) {
		this.vlFreteTabelaCheia = vlFreteTabelaCheia;
	}

	public Boolean getBlParcelas() {
		return blParcelas;
	}

	public void setBlParcelas(Boolean blParcelas) {
		this.blParcelas = blParcelas;
	}

	public Boolean getBlServicosAdicionais() {
		return blServicosAdicionais;
	}

	public void setBlServicosAdicionais(Boolean blServicosAdicionais) {
		this.blServicosAdicionais = blServicosAdicionais;
	}

	public DomainValue getTpDocumentoServico() {
		return this.tpDocumentoServico;
	}

	public void setTpDocumentoServico(DomainValue tpDocumentoServico) {
		this.tpDocumentoServico = tpDocumentoServico;
	}

	public Boolean getBlBloqueado() {
		return this.blBloqueado;
	}

	public void setBlBloqueado(Boolean blBloqueado) {
		this.blBloqueado = blBloqueado;
	}

	public BigDecimal getVlBaseCalcImposto() {
		return this.vlBaseCalcImposto;
	}

	public void setVlBaseCalcImposto(BigDecimal vlBaseCalcImposto) {
		this.vlBaseCalcImposto = vlBaseCalcImposto;
	}

	public BigDecimal getVlIcmsSubstituicaoTributaria() {
		return vlIcmsSubstituicaoTributaria;
	}

	public void setVlIcmsSubstituicaoTributaria(
			BigDecimal vlIcmsSubstituicaoTributaria) {
		this.vlIcmsSubstituicaoTributaria = vlIcmsSubstituicaoTributaria;
	}

	public Boolean getBlIncideIcmsPedagio() {
		return blIncideIcmsPedagio;
	}

	public void setBlIncideIcmsPedagio(Boolean blIncideIcmsPedagio) {
		this.blIncideIcmsPedagio = blIncideIcmsPedagio;
	}

	public Short getNrCfop() {
		return this.nrCfop;
	}

	public void setNrCfop(Short nrCfop) {
		this.nrCfop = nrCfop;
	}

	public BigDecimal getPsReferenciaCalculo() {
		return this.psReferenciaCalculo;
	}

	public void setPsReferenciaCalculo(BigDecimal psReferenciaCalculo) {
		this.psReferenciaCalculo = psReferenciaCalculo;
	}

	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}

	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}

	public DateTime getDhEntradaSetorEntrega() {
		return dhEntradaSetorEntrega;
	}

	public void setDhEntradaSetorEntrega(DateTime dhEntradaSetorEntrega) {
		this.dhEntradaSetorEntrega = dhEntradaSetorEntrega;
	}

	public DomainValue getTpCalculoPreco() {
		return this.tpCalculoPreco;
	}

	public void setTpCalculoPreco(DomainValue tpCalculoPreco) {
		this.tpCalculoPreco = tpCalculoPreco;
	}

	public Boolean getBlPrioridadeCarregamento() {
		return this.blPrioridadeCarregamento;
	}

	public void setBlPrioridadeCarregamento(Boolean blPrioridadeCarregamento) {
		this.blPrioridadeCarregamento = blPrioridadeCarregamento;
	}

	public String getNrAidf() {
		return this.nrAidf;
	}

	public void setNrAidf(String nrAidf) {
		this.nrAidf = nrAidf;
	}

	public BigDecimal getPcAliquotaIcms() {
		return this.pcAliquotaIcms;
	}

	public void setPcAliquotaIcms(BigDecimal pcAliquotaIcms) {
		this.pcAliquotaIcms = pcAliquotaIcms;
	}

	public BigDecimal getPsReal() {
		return this.psReal;
	}

	public void setPsReal(BigDecimal psReal) {
		this.psReal = psReal;
	}

	public BigDecimal getPsAforado() {
		return this.psAforado;
	}

	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}

	public Integer getQtVolumes() {
		return this.qtVolumes;
	}

	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public YearMonthDay getDtPrevEntrega() {
		return dtPrevEntrega;
	}

	public void setDtPrevEntrega(YearMonthDay dtPrevEntrega) {
		this.dtPrevEntrega = dtPrevEntrega;
	}

	public Short getNrDiasPrevEntrega() {
		return this.nrDiasPrevEntrega;
	}

	public void setNrDiasPrevEntrega(Short nrDiasPrevEntrega) {
		this.nrDiasPrevEntrega = nrDiasPrevEntrega;
	}

	public String getDsEnderecoEntregaReal() {
		return this.dsEnderecoEntregaReal;
	}

	public void setDsEnderecoEntregaReal(String dsEnderecoEntregaReal) {
		this.dsEnderecoEntregaReal = dsEnderecoEntregaReal;
	}

	public Short getNrDiasRealEntrega() {
		return this.nrDiasRealEntrega;
	}

	public void setNrDiasRealEntrega(Short nrDiasRealEntrega) {
		this.nrDiasRealEntrega = nrDiasRealEntrega;
	}

	public Short getNrDiasBloqueio() {
		return this.nrDiasBloqueio;
	}

	public void setNrDiasBloqueio(Short nrDiasBloqueio) {
		this.nrDiasBloqueio = nrDiasBloqueio;
	}

	public String getObComplementoLocalizacao() {
		return obComplementoLocalizacao;
	}

	public void setObComplementoLocalizacao(String obComplementoLocalizacao) {
		this.obComplementoLocalizacao = obComplementoLocalizacao;
	}

	public Boolean getBlSpecialService() {
		return blSpecialService;
	}

	public void setBlSpecialService(Boolean blSpecialService) {
		this.blSpecialService = blSpecialService;
	}

	public Cliente getClienteByIdClienteDestinatario() {
		return this.clienteByIdClienteDestinatario;
	}

	public void setClienteByIdClienteDestinatario(
			Cliente clienteByIdClienteDestinatario) {
		this.clienteByIdClienteDestinatario = clienteByIdClienteDestinatario;
	}

	public Cliente getClienteByIdClienteBaseCalculo() {
		return this.clienteByIdClienteBaseCalculo;
	}

	public void setClienteByIdClienteBaseCalculo(
			Cliente clienteByIdClienteBaseCalculo) {
		this.clienteByIdClienteBaseCalculo = clienteByIdClienteBaseCalculo;
	}

	public Cliente getClienteByIdClienteConsignatario() {
		return this.clienteByIdClienteConsignatario;
	}

	public void setClienteByIdClienteConsignatario(
			Cliente clienteByIdClienteConsignatario) {
		this.clienteByIdClienteConsignatario = clienteByIdClienteConsignatario;
	}

	public Cliente getClienteByIdClienteRemetente() {
		return this.clienteByIdClienteRemetente;
	}

	public void setClienteByIdClienteRemetente(
			Cliente clienteByIdClienteRemetente) {
		this.clienteByIdClienteRemetente = clienteByIdClienteRemetente;
	}

	public Cliente getClienteByIdClienteRedespacho() {
		return this.clienteByIdClienteRedespacho;
	}

	public void setClienteByIdClienteRedespacho(
			Cliente clienteByIdClienteRedespacho) {
		this.clienteByIdClienteRedespacho = clienteByIdClienteRedespacho;
	}

	public Usuario getUsuarioByIdUsuarioInclusao() {
		return this.usuarioByIdUsuarioInclusao;
	}

	public void setUsuarioByIdUsuarioInclusao(Usuario usuarioByIdUsuarioInclusao) {
		this.usuarioByIdUsuarioInclusao = usuarioByIdUsuarioInclusao;
	}

	public Usuario getUsuarioByIdUsuarioAlteracao() {
		return this.usuarioByIdUsuarioAlteracao;
	}

	public void setUsuarioByIdUsuarioAlteracao(
			Usuario usuarioByIdUsuarioAlteracao) {
		this.usuarioByIdUsuarioAlteracao = usuarioByIdUsuarioAlteracao;
	}

	public EnderecoPessoa getEnderecoPessoa() {
		return this.enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public FluxoFilial getFluxoFilial() {
		return this.fluxoFilial;
	}

	public void setFluxoFilial(FluxoFilial fluxoFilial) {
		this.fluxoFilial = fluxoFilial;
	}

	public Filial getFilialDestinoOperacional() {
		return this.filialDestinoOperacional;
	}

	public void setFilialDestinoOperacional(Filial filialDestinoOperacional) {
		this.filialDestinoOperacional = filialDestinoOperacional;
	}

	public TabelaPreco getTabelaPreco() {
		return this.tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public Moeda getMoeda() {
		return this.moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public Servico getServico() {
		return this.servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public DivisaoCliente getDivisaoCliente() {
		return this.divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public RotaColetaEntrega getRotaColetaEntregaByIdRotaColetaEntregaReal() {
		return this.rotaColetaEntregaByIdRotaColetaEntregaReal;
	}

	public void setRotaColetaEntregaByIdRotaColetaEntregaReal(
			RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaReal) {
		this.rotaColetaEntregaByIdRotaColetaEntregaReal = rotaColetaEntregaByIdRotaColetaEntregaReal;
	}

	public RotaColetaEntrega getRotaColetaEntregaByIdRotaColetaEntregaSugerid() {
		return this.rotaColetaEntregaByIdRotaColetaEntregaSugerid;
	}

	public void setRotaColetaEntregaByIdRotaColetaEntregaSugerid(
			RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaSugerid) {
		this.rotaColetaEntregaByIdRotaColetaEntregaSugerid = rotaColetaEntregaByIdRotaColetaEntregaSugerid;
	}

	public PedidoColeta getPedidoColeta() {
		return this.pedidoColeta;
	}

	public void setPedidoColeta(PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}

	public Filial getFilialByIdFilialDestino() {
		return this.filialByIdFilialDestino;
	}

	public void setFilialByIdFilialDestino(Filial filialByIdFilialDestino) {
		this.filialByIdFilialDestino = filialByIdFilialDestino;
	}

	public Filial getFilialByIdFilialOrigem() {
		return this.filialByIdFilialOrigem;
	}

	public void setFilialByIdFilialOrigem(Filial filialByIdFilialOrigem) {
		this.filialByIdFilialOrigem = filialByIdFilialOrigem;
	}

	public Pais getPaisOrigem() {
		return this.paisOrigem;
	}

	public void setPaisOrigem(Pais paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public RotaIntervaloCep getRotaIntervaloCep() {
		return this.rotaIntervaloCep;
	}

	public void setRotaIntervaloCep(RotaIntervaloCep rotaIntervaloCep) {
		this.rotaIntervaloCep = rotaIntervaloCep;
	}

	public RotaIdaVolta getRotaIdaVolta() {
		return this.rotaIdaVolta;
	}

	public void setRotaIdaVolta(RotaIdaVolta rotaIdaVolta) {
		this.rotaIdaVolta = rotaIdaVolta;
	}

	public ParametroCliente getParametroCliente() {
		return this.parametroCliente;
	}

	public void setParametroCliente(ParametroCliente parametroCliente) {
		this.parametroCliente = parametroCliente;
	}

	public TarifaPreco getTarifaPreco() {
		return this.tarifaPreco;
	}

	public void setTarifaPreco(TarifaPreco tarifaPreco) {
		this.tarifaPreco = tarifaPreco;
	}

	public Impressora getImpressora() {
		return this.impressora;
	}

	public void setImpressora(Impressora impressora) {
		this.impressora = impressora;
	}

	public LocalizacaoMercadoria getLocalizacaoMercadoria() {
		return this.localizacaoMercadoria;
	}

	public void setLocalizacaoMercadoria(
			LocalizacaoMercadoria localizacaoMercadoria) {
		this.localizacaoMercadoria = localizacaoMercadoria;
	}

	public Filial getFilialLocalizacao() {
		return this.filialLocalizacao;
	}

	public void setFilialLocalizacao(Filial filialLocalizacao) {
		this.filialLocalizacao = filialLocalizacao;
	}

	public DoctoServico getDoctoServicoOriginal() {
		return doctoServicoOriginal;
	}

	public void setDoctoServicoOriginal(DoctoServico doctoServicoOriginal) {
		this.doctoServicoOriginal = doctoServicoOriginal;
	}

	public InscricaoEstadual getInscricaoEstadualDestinatario() {
		return inscricaoEstadualDestinatario;
	}

	public void setInscricaoEstadualDestinatario(
			InscricaoEstadual inscricaoEstadualDestinatario) {
		this.inscricaoEstadualDestinatario = inscricaoEstadualDestinatario;
	}

	public InscricaoEstadual getInscricaoEstadualRemetente() {
		return inscricaoEstadualRemetente;
	}

	public void setInscricaoEstadualRemetente(
			InscricaoEstadual inscricaoEstadualRemetente) {
		this.inscricaoEstadualRemetente = inscricaoEstadualRemetente;
	}

	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public DateTime getDhEmissao() {
		return dhEmissao;
	}

	public void setDhEmissao(DateTime dhEmissao) {
		this.dhEmissao = dhEmissao;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.CartaMercadoriaDisposicao.class)
	public List getCartaMercadoriaDisposicoes() {
		return this.cartaMercadoriaDisposicoes;
	}

	public void setCartaMercadoriaDisposicoes(List cartaMercadoriaDisposicoes) {
		this.cartaMercadoriaDisposicoes = cartaMercadoriaDisposicoes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao.class)
	public List<DoctoServicoIndenizacao> getDoctoServicoIndenizacoes() {
		return doctoServicoIndenizacoes;
	}

	public void setDoctoServicoIndenizacoes(
			List<DoctoServicoIndenizacao> doctoServicoIndenizacoes) {
		this.doctoServicoIndenizacoes = doctoServicoIndenizacoes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.LiberacaoDocServ.class)
	public List<LiberacaoDocServ> getLiberacaoDocServs() {
		return this.liberacaoDocServs;
	}

	public void setLiberacaoDocServs(List<LiberacaoDocServ> liberacaoDocServs) {
		this.liberacaoDocServs = liberacaoDocServs;
	}

	public void addLiberacaoEmbarque(LiberacaoDocServ liberacaoDocServ) {
		if(this.liberacaoDocServs == null) {
			this.liberacaoDocServs = new ArrayList<LiberacaoDocServ>();
		}
		this.liberacaoDocServs.add(liberacaoDocServ);
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.DevedorDocServFat.class)
	public List<DevedorDocServFat> getDevedorDocServFats() {
		return this.devedorDocServFats;
	}

	public void setDevedorDocServFats(List<DevedorDocServFat> devedorDocServFats) {
		this.devedorDocServFats = devedorDocServFats;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.AgendamentoDoctoServico.class)
	public List getAgendamentoDoctoServicos() {
		return this.agendamentoDoctoServicos;
	}

	public void setAgendamentoDoctoServicos(List agendamentoDoctoServicos) {
		this.agendamentoDoctoServicos = agendamentoDoctoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.DocumentoServicoRetirada.class)
	public List getDocumentoServicoRetiradas() {
		return this.documentoServicoRetiradas;
	}

	public void setDocumentoServicoRetiradas(List documentoServicoRetiradas) {
		this.documentoServicoRetiradas = documentoServicoRetiradas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ServicoEmbalagem.class)
	public List getServicoEmbalagems() {
		return this.servicoEmbalagems;
	}

	public void setServicoEmbalagems(List servicoEmbalagems) {
		this.servicoEmbalagems = servicoEmbalagems;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ServAdicionalDocServ.class)
	public List<ServAdicionalDocServ> getServAdicionalDocServs() {
		return this.servAdicionalDocServs;
	}

	public void setServAdicionalDocServs(
			List<ServAdicionalDocServ> servAdicionalDocServs) {
		this.servAdicionalDocServs = servAdicionalDocServs;
	}

	public void addServicoAdicional(ServAdicionalDocServ servAdicionalDocServ) {
		if(this.servAdicionalDocServs == null) {
			this.servAdicionalDocServs = new ArrayList<ServAdicionalDocServ>();
		}
		this.servAdicionalDocServs.add(servAdicionalDocServ);
	}

	public boolean removeServicoAdicional(ServAdicionalDocServ servico){
		if(this.servAdicionalDocServs != null) {
			return this.servAdicionalDocServs.remove(servico);
		}
		return false;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico.class)
	public List<OcorrenciaDoctoServico> getOcorrenciaDoctoServicos() {
		return this.ocorrenciaDoctoServicos;
	}

	public void setOcorrenciaDoctoServicos(List<OcorrenciaDoctoServico> ocorrenciaDoctoServicos) {
		this.ocorrenciaDoctoServicos = ocorrenciaDoctoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.rnc.model.NaoConformidade.class)
	public List<NaoConformidade> getNaoConformidades() {
		return this.naoConformidades;
	}

	public void setNaoConformidades(List<NaoConformidade> naoConformidades) {
		this.naoConformidades = naoConformidades;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ValorCusto.class)
	public List getValorCustos() {
		return this.valorCustos;
	}

	public void setValorCustos(List valorCustos) {
		this.valorCustos = valorCustos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.EventoDocumentoServico.class)
	public List<EventoDocumentoServico> getEventoDocumentoServicos() {
		return this.eventoDocumentoServicos;
	}

	public void setEventoDocumentoServicos(
			List<EventoDocumentoServico> eventoDocumentoServicos) {
		this.eventoDocumentoServicos = eventoDocumentoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PreManifestoDocumento.class)
	public List<PreManifestoDocumento> getPreManifestoDocumentos() {
		return this.preManifestoDocumentos;
	}

	public void setPreManifestoDocumentos(
			List<PreManifestoDocumento> preManifestoDocumentos) {
		this.preManifestoDocumentos = preManifestoDocumentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PreManifestoVolume.class)
	public List<PreManifestoVolume> getPreManifestoVolumes() {
		return preManifestoVolumes;
	}

	public void setPreManifestoVolumes(
			List<PreManifestoVolume> preManifestoVolumes) {
		this.preManifestoVolumes = preManifestoVolumes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ObservacaoDoctoServico.class)
	public List<ObservacaoDoctoServico> getObservacaoDoctoServicos() {
		return this.observacaoDoctoServicos;
	}

	public void setObservacaoDoctoServicos(
			List<ObservacaoDoctoServico> observacaoDoctoServicos) {
		this.observacaoDoctoServicos = observacaoDoctoServicos;
	}

	public void addObservacaoDoctoServico(
			ObservacaoDoctoServico observacaoDoctoServico) {
		if(observacaoDoctoServicos == null) {
			observacaoDoctoServicos = new ArrayList<ObservacaoDoctoServico>();
		}
		observacaoDoctoServicos.add(observacaoDoctoServico);
	}

	public boolean removeObservacaoDoctoServico(
			ObservacaoDoctoServico observacao) {
		if(observacaoDoctoServicos != null) {
			return observacaoDoctoServicos.remove(observacao);
		}
		return false;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ReciboReembolsoDoctoServico.class)
	public List getReciboReembolsoDoctoServicos() {
		return this.reciboReembolsoDoctoServicos;
	}

	public void setReciboReembolsoDoctoServicos(
			List reciboReembolsoDoctoServicos) {
		this.reciboReembolsoDoctoServicos = reciboReembolsoDoctoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntregaDocumento.class)
	public List<ManifestoEntregaDocumento> getManifestoEntregaDocumentos() {
		return this.manifestoEntregaDocumentos;
	}

	public void setManifestoEntregaDocumentos(
			List<ManifestoEntregaDocumento> manifestoEntregaDocumentos) {
		this.manifestoEntregaDocumentos = manifestoEntregaDocumentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ItemMda.class)
	public List getItemMdasDoctoServico() {
		return this.itemMdasDoctoServico;
	}

	public void setItemMdasDoctoServico(List itemMdasDoctoServico) {
		this.itemMdasDoctoServico = itemMdasDoctoServico;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ReciboReembolso.class)
	public List getReciboReembolsosByIdDoctoServReembolsado() {
		return this.reciboReembolsosByIdDoctoServReembolsado;
	}

	public void setReciboReembolsosByIdDoctoServReembolsado(
			List reciboReembolsosByIdDoctoServReembolsado) {
		this.reciboReembolsosByIdDoctoServReembolsado = reciboReembolsosByIdDoctoServReembolsado;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ParcelaDoctoServico.class)
	public List<ParcelaDoctoServico> getParcelaDoctoServicos() {
		return this.parcelaDoctoServicos;
	}

	public void setParcelaDoctoServicos(
			List<ParcelaDoctoServico> parcelaDoctoServicos) {
		this.parcelaDoctoServicos = parcelaDoctoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.seguros.model.SinistroDoctoServico.class)
	public List<SinistroDoctoServico> getSinistroDoctoServicos() {
		return this.sinistroDoctoServicos;
	}

	public void setSinistroDoctoServicos(
			List<SinistroDoctoServico> sinistroDoctoServicos) {
		this.sinistroDoctoServicos = sinistroDoctoServicos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DevedorDocServ.class)
	public List<DevedorDocServ> getDevedorDocServs() {
		return this.devedorDocServs;
	}

	public void setDevedorDocServs(List<DevedorDocServ> devedorDocServs) {
		this.devedorDocServs = devedorDocServs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class)
	public List getMercadoriaPendenciaMzs() {
		return this.mercadoriaPendenciaMzs;
	}

	public void setMercadoriaPendenciaMzs(List mercadoriaPendenciaMzs) {
		this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cotacao.class)
	public List<Cotacao> getCotacoes() {
		return this.cotacoes;
	}

	public void setCotacoes(List<Cotacao> cotacoes) {
		this.cotacoes = cotacoes;
	}

	public void addCotacao(Cotacao cotacao) {
		if(this.cotacoes == null) {
			this.cotacoes = new ArrayList<Cotacao>();
		}
		this.cotacoes.add(cotacao);
	}

	@ParametrizedAttribute(type = com.mercurio.lms.entrega.model.RegistroDocumentoEntrega.class)
	public List getRegistroDocumentoEntregas() {
		return this.registroDocumentoEntregas;
	}

	public void setRegistroDocumentoEntregas(List registroDocumentoEntregas) {
		this.registroDocumentoEntregas = registroDocumentoEntregas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.JustificativaDoctoNaoCarregado.class)
	public List getJustificativasDoctosNaoCarregados() {
		return justificativasDoctosNaoCarregados;
	}

	public void setJustificativasDoctosNaoCarregados(
			List justificativasDoctosNaoCarregados) {
		this.justificativasDoctosNaoCarregados = justificativasDoctosNaoCarregados;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idDoctoServico",
				getIdDoctoServico()).toString();
	}

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_DOCTO_SERVICO", idDoctoServico)
				.append("ID_MOEDA", moeda != null ? moeda.getIdMoeda() : null)
				.append("ID_CLIENTE_REMETENTE", clienteByIdClienteRemetente != null ? clienteByIdClienteRemetente.getIdCliente() : null)
				.append("ID_SERVICO", servico != null ? servico.getIdServico() : null)
				.append("ID_USUARIO_INCLUSAO", usuarioByIdUsuarioInclusao != null ? usuarioByIdUsuarioInclusao.getIdUsuario() : null)
				.append("ID_FILIAL_ORIGEM", filialByIdFilialOrigem != null ? filialByIdFilialOrigem.getIdFilial() : null)
				.append("VL_TOTAL_DOC_SERVICO", vlTotalDocServico)
				.append("VL_TOTAL_PARCELAS", vlTotalParcelas)
				.append("VL_TOTAL_SERVICOS", vlTotalServicos)
				.append("VL_IMPOSTO", vlImposto)
				.append("VL_DESCONTO", vlDesconto)
				.append("DH_EMISSAO", dhEmissao)
				.append("DH_INCLUSAO", dhInclusao)
				.append("TP_DOCUMENTO_SERVICO", tpDocumentoServico != null ? tpDocumentoServico.getValue() : null)
				.append("BL_BLOQUEADO", blBloqueado)
				.append("ID_PAIS", paisOrigem != null ? paisOrigem.getIdPais() : null)
				.append("ID_ROTA_INTERVALO_CEP", rotaIntervaloCep != null ? rotaIntervaloCep.getIdRotaIntervaloCep() : null)
				.append("ID_IE_DESTINATARIO", inscricaoEstadualDestinatario != null ? inscricaoEstadualDestinatario.getIdInscricaoEstadual() : null)
				.append("ID_IE_REMETENTE", inscricaoEstadualRemetente != null ? inscricaoEstadualRemetente.getIdInscricaoEstadual() : null)
				.append("ID_PEDIDO_COLETA", pedidoColeta != null ? pedidoColeta.getIdPedidoColeta() : null)
				.append("ID_FILIAL_DESTINO", filialByIdFilialDestino != null ? filialByIdFilialDestino.getIdFilial() : null)
				.append("ID_CLIENTE_DESTINATARIO", clienteByIdClienteDestinatario != null ? clienteByIdClienteDestinatario.getIdCliente() : null)
				.append("ID_CLIENTE_CONSIGNATARIO", clienteByIdClienteConsignatario != null ? clienteByIdClienteConsignatario.getIdCliente() : null)
				.append("ID_CLIENTE_REDESPACHO", clienteByIdClienteRedespacho != null ? clienteByIdClienteRedespacho.getIdCliente() : null)
				.append("ID_ROTA_COLETA_ENTREGA_REAL", rotaColetaEntregaByIdRotaColetaEntregaReal != null ? rotaColetaEntregaByIdRotaColetaEntregaReal.getIdRotaColetaEntrega() : null)
				.append("ID_ROTA_COLETA_ENTREGA_SUGERID", rotaColetaEntregaByIdRotaColetaEntregaSugerid != null ? rotaColetaEntregaByIdRotaColetaEntregaSugerid.getIdRotaColetaEntrega() : null)
				.append("ID_DIVISAO_CLIENTE", divisaoCliente != null ? divisaoCliente.getIdDivisaoCliente() : null)
				.append("ID_ENDERECO_ENTREGA", enderecoPessoa != null ? enderecoPessoa.getIdEnderecoPessoa() : null)
				.append("ID_FLUXO_FILIAL", fluxoFilial != null ? fluxoFilial.getIdFluxoFilial() : null)
				.append("VL_BASE_CALC_IMPOSTO", vlBaseCalcImposto)
				.append("VL_MERCADORIA", vlMercadoria)
				.append("NR_CFOP", nrCfop)
				.append("PS_REFERENCIA_CALCULO", psReferenciaCalculo)
				.append("ID_USUARIO_ALTERACAO", usuarioByIdUsuarioAlteracao != null ? usuarioByIdUsuarioAlteracao.getIdUsuario() : null)
				.append("ID_TABELA_PRECO", tabelaPreco != null ? tabelaPreco.getIdTabelaPreco() : null)
				.append("DH_ALTERACAO", dhAlteracao)
				.append("DH_ENTRADA_SETOR_ENTREGA", dhEntradaSetorEntrega)
				.append("TP_CALCULO_PRECO", tpCalculoPreco != null ? tpCalculoPreco.getValue() : null)
				.append("TP_MOTIVO_LIBERACAO", tpMotivoLiberacao != null ? tpMotivoLiberacao.getValue() : null)
				.append("BL_PRIORIDADE_CARREGAMENTO", blPrioridadeCarregamento)
				.append("NR_AIDF", nrAidf)
				.append("ID_DOCTO_SERVICO_ORIGINAL", doctoServicoOriginal != null ? doctoServicoOriginal.getIdDoctoServico() : null)
				.append("PC_ALIQUOTA_ICMS", pcAliquotaIcms)
				.append("ID_PARAMETRO_CLIENTE", parametroCliente != null ? parametroCliente.getIdParametroCliente() : null)
				.append("ID_TARIFA_PRECO", tarifaPreco != null ? tarifaPreco.getIdTarifaPreco() : null)
				.append("ID_IMPRESSORA", impressora != null ? impressora.getIdImpressora() : null)
				.append("ID_LOCALIZACAO_MERCADORIA", localizacaoMercadoria != null ? localizacaoMercadoria.getIdLocalizacaoMercadoria() : null)
				.append("PS_REAL", psReal)
				.append("PS_AFORADO", psAforado)
				.append("QT_VOLUMES", qtVolumes)
				.append("DT_PREV_ENTREGA", dtPrevEntrega)
				.append("NR_DOCTO_SERVICO", nrDoctoServico)
				.append("ID_FILIAL_LOCALIZACAO", filialLocalizacao != null ? filialLocalizacao.getIdFilial() : null)
				.append("NR_DIAS_PREV_ENTREGA", nrDiasPrevEntrega)
				.append("DS_ENDERECO_ENTREGA_REAL", dsEnderecoEntregaReal)
				.append("NR_DIAS_REAL_ENTREGA", nrDiasRealEntrega)
				.append("ID_ROTA_IDA_VOLTA", rotaIdaVolta != null ? rotaIdaVolta.getIdRotaIdaVolta() : null)
				.append("ID_FILIAL_DESTINO_OPERACIONAL", filialDestinoOperacional != null ? filialDestinoOperacional.getIdFilial() : null)
				.append("OB_COMPLEMENTO_LOCALIZACAO", obComplementoLocalizacao)
				.append("VL_ICMS_ST", vlIcmsSubstituicaoTributaria)
				.append("VL_FRETE_LIQUIDO", vlLiquido)
				.append("BL_INCIDENCIA_ICMS_PEDAGIO", blIncideIcmsPedagio)
				.append("NR_DIAS_BLOQUEIO", nrDiasBloqueio)
				.append("PS_AFERIDO", psAferido)
				.append("ID_CLIENTE_BASE_CALCULO", clienteByIdClienteBaseCalculo != null ? clienteByIdClienteBaseCalculo.getIdCliente() : null)
				.append("VL_FRETE_PESO_DECLARADO", vlFretePesoDeclarado)
				.append("VL_FRETE_TABELA_CHEIA", vlFreteTabelaCheia)
				.append("BL_PALETIZACAO", blPaletizacao)
				.append("BL_SPECIAL_SERVICE", blSpecialService)
				.append("NR_FATOR_DENSIDADE", nrFatorDensidade)
				.append("PS_CUBADO_REAL", psCubadoReal)
				.append("NR_FATOR_CUBAGEM", nrFatorCubagem)
				.append("NR_CUBAGEM_ESTATISTICA", nrCubagemEstatistica)
				.append("NR_FATOR_CUBAGEM_CLIENTE", nrFatorCubagemCliente)
				.append("NR_FATOR_CUBAGEM_SEGMENTO", nrFatorCubagemSegmento)
				.append("TP_PESO_CALCULO", tpPesoCalculo != null ? tpPesoCalculo.getValue() : null)
				.append("BL_UTILIZA_PESO_EDI", blUtilizaPesoEdi)
				.append("BL_PESO_FAT_POR_CUBADO_AFERIDO", blPesoFatPorCubadoAferido)
				.append("BL_PESO_CUBADO_POR_DENSIDADE", blPesoCubadoPorDensidade)
				.append("PS_ESTATISTICO", psEstatistico)
				.append("VL_TOTAL_SORTER", vlTotalSorter)
				.append("VL_LIQUIDO_SORTER", vlLiquidoSorter)
				.append("NR_CUBAGEM_DECLARADA", nrCubagemDeclarada)
				.append("NR_CUBAGEM_AFERIDA", nrCubagemAferida)
				.append("NR_CUBAGEM_CALCULO", nrCubagemCalculo)
				.append("PS_CUBADO_DECLARADO", psCubadoDeclarado)
				.append("PS_CUBADO_AFERIDO", psCubadoAferido)
				.append("NR_DIAS_AGENDAMENTO", nrDiasAgendamento)
				.append("NR_DIAS_TENT_ENTREGAS", nrDiasTentativasEntregas)
				.append("DT_PROJETADA_ENTREGA", dtProjetadaEntrega)
				.append("VL_IMPOSTO_PESO_DECLARADO", vlImpostoPesoDeclarado)
				.append("VL_ICMS_ST_PESO_DECLARADO", vlIcmsSubstituicaoTributariaPesoDeclarado)
				.append("NR_CHAVE_NF_ANULACAO", nrChaveNfAnulacao)
				.append("NR_NF_ANULACAO", nrNfAnulacao)
				.append("DS_SERIE_ANULACAO", dsSerieAnulacao)
				.append("VL_NF_ANULACAO", vlNfAnulacao)
				.append("DT_EMISSAO_NF_ANULACAO", dtEmissaoNfAnulacao)
				.append("VL_IMPOSTO_DIFAL", vlImpostoDifal)
				.append("TP_SITUACAO_PENDENCIA", tpSituacaoPendencia != null ? tpSituacaoPendencia.getValue() : null)
				.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof DoctoServico)) {
			return false;
		}
		DoctoServico castOther = (DoctoServico) other;
		return new EqualsBuilder().append(this.getIdDoctoServico(),
				castOther.getIdDoctoServico()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdDoctoServico()).toHashCode();
	}

	/**
	 * @return the psAferido
	 */
	public BigDecimal getPsAferido() {
		return psAferido;
	}

	/**
	 * @param psAferido the psAferido to set
	 */
	public void setPsAferido(BigDecimal psAferido) {
		this.psAferido = psAferido;
	}

	public Boolean getBlPaletizacao() {
		return blPaletizacao;
}

	public void setBlPaletizacao(Boolean blPaletizacao) {
		this.blPaletizacao = blPaletizacao;
	}

	public void setManifestoEntregaVolumes(
			List<ManifestoEntregaVolume> manifestoEntregaVolumes) {
		this.manifestoEntregaVolumes = manifestoEntregaVolumes;
	}

	public List<ManifestoEntregaVolume> getManifestoEntregaVolumes() {
		return manifestoEntregaVolumes;
	}

	public DomainValue getTpSituacaoConhecimento() {
		return tpSituacaoConhecimento;
	}

	public void setTpSituacaoConhecimento(DomainValue tpSituacaoConhecimento) {
		this.tpSituacaoConhecimento = tpSituacaoConhecimento;
	}

    public List<NotaCreditoDocto> getNotaCreditoDoctos() {
        return notaCreditoDoctos;
}

    public void setNotaCreditoDoctos(List<NotaCreditoDocto> notaCreditoDoctos) {
        this.notaCreditoDoctos = notaCreditoDoctos;
    }

	public BigDecimal getNrFatorDensidade() {
		return nrFatorDensidade;
	}

	public void setNrFatorDensidade(BigDecimal nrFatorDensidade) {
		this.nrFatorDensidade = nrFatorDensidade;
	}

	public Boolean getBlExecutarVerificacaoDocumentoManifestado() {
		return blExecutarVerificacaoDocumentoManifestado;
	}


	public void setBlExecutarVerificacaoDocumentoManifestado(Boolean blExecutarVerificacaoDocumentoManifestado) {
		this.blExecutarVerificacaoDocumentoManifestado = blExecutarVerificacaoDocumentoManifestado;
	}

	public BigDecimal getPsCubadoReal() {
		return psCubadoReal;
}


	public void setPsCubadoReal(BigDecimal psCubadoReal) {
		this.psCubadoReal = psCubadoReal;
	}

	public BigDecimal getNrFatorCubagem() {
		return nrFatorCubagem;
	}

	public void setNrFatorCubagem(BigDecimal nrFatorCubagem) {
		this.nrFatorCubagem = nrFatorCubagem;
	}

	public BigDecimal getPsEstatistico() {
		return psEstatistico;
	}

	public void setPsEstatistico(BigDecimal psEstatistico) {
		this.psEstatistico = psEstatistico;
	}

	public BigDecimal getNrCubagemEstatistica() {
		return nrCubagemEstatistica;
	}

	public void setNrCubagemEstatistica(BigDecimal nrCubagemEstatistica) {
		this.nrCubagemEstatistica = nrCubagemEstatistica;
	}

	public BigDecimal getNrFatorCubagemCliente() {
		return nrFatorCubagemCliente;
	}

	public void setNrFatorCubagemCliente(BigDecimal nrFatorCubagemCliente) {
		this.nrFatorCubagemCliente = nrFatorCubagemCliente;
	}

	public BigDecimal getNrFatorCubagemSegmento() {
		return nrFatorCubagemSegmento;
	}

	public void setNrFatorCubagemSegmento(BigDecimal nrFatorCubagemSegmento) {
		this.nrFatorCubagemSegmento = nrFatorCubagemSegmento;
	}

	public DomainValue getTpPesoCalculo() {
		return tpPesoCalculo;
	}

	public void setTpPesoCalculo(DomainValue tpPesoCalculo) {
		this.tpPesoCalculo = tpPesoCalculo;
	}

	public Boolean getBlUtilizaPesoEdi() {
		return blUtilizaPesoEdi;
	}

	public void setBlUtilizaPesoEdi(Boolean blUtilizaPesoEdi) {
		this.blUtilizaPesoEdi = blUtilizaPesoEdi;
	}

	public Boolean getBlPesoFatPorCubadoAferido() {
		return blPesoFatPorCubadoAferido;
	}

	public void setBlPesoFatPorCubadoAferido(Boolean blPesoFatPorCubadoAferido) {
		this.blPesoFatPorCubadoAferido = blPesoFatPorCubadoAferido;
	}

	public Boolean getBlPesoCubadoPorDensidade() {
		return blPesoCubadoPorDensidade;
	}

	public void setBlPesoCubadoPorDensidade(Boolean blPesoCubadoPorDensidade) {
		this.blPesoCubadoPorDensidade = blPesoCubadoPorDensidade;
	}

	public BigDecimal getVlTotalSorter() {
		return vlTotalSorter;
	}

	public void setVlTotalSorter(BigDecimal vlTotalSorter) {
		this.vlTotalSorter = vlTotalSorter;
	}

	public BigDecimal getVlLiquidoSorter() {
		return vlLiquidoSorter;
	}

	public void setVlLiquidoSorter(BigDecimal vlLiquidoSorter) {
		this.vlLiquidoSorter = vlLiquidoSorter;
	}

    public BigDecimal getNrCubagemAferida() {
        return nrCubagemAferida;
    }

    public void setNrCubagemAferida(BigDecimal nrCubagemAferida) {
        this.nrCubagemAferida = nrCubagemAferida;
    }

    public BigDecimal getNrCubagemCalculo() {
        return nrCubagemCalculo;
    }

    public void setNrCubagemCalculo(BigDecimal nrCubagemCalculo) {
        this.nrCubagemCalculo = nrCubagemCalculo;
    }

    public BigDecimal getPsCubadoAferido() {
        return psCubadoAferido;
    }

    public void setPsCubadoAferido(BigDecimal psCubadoAferido) {
        this.psCubadoAferido = psCubadoAferido;
    }

    public BigDecimal getPsCubadoDeclarado() {
        return psCubadoDeclarado;
    }

    public void setPsCubadoDeclarado(BigDecimal psCubadoDeclarado) {
        this.psCubadoDeclarado = psCubadoDeclarado;
    }

    public BigDecimal getNrCubagemDeclarada() {
        return nrCubagemDeclarada;
    }

    public void setNrCubagemDeclarada(BigDecimal nrCubagemDeclarada) {
        this.nrCubagemDeclarada = nrCubagemDeclarada;
    }

	public String getDsCodigoBarras() {
		return dsCodigoBarras;
}

	public void setDsCodigoBarras(String dsCodigoBarras) {
		this.dsCodigoBarras = dsCodigoBarras;
	}

	public List<ImpostoServico> getImpostoServicosNFS() {
		return impostoServicosNFS;
}

	public void setImpostoServicosNFS(List<ImpostoServico> impostoServicosNFS) {
		this.impostoServicosNFS = impostoServicosNFS;
	}

	public String getNrChaveNfAnulacao() {
		return nrChaveNfAnulacao;
	}

	public void setNrChaveNfAnulacao(String nrChaveNfAnulacao) {
		this.nrChaveNfAnulacao = nrChaveNfAnulacao;
	}

	public BigDecimal getNrNfAnulacao() {
		return nrNfAnulacao;
	}

	public void setNrNfAnulacao(BigDecimal nrNfAnulacao) {
		this.nrNfAnulacao = nrNfAnulacao;
	}

	public String getDsSerieAnulacao() {
		return dsSerieAnulacao;
	}

	public void setDsSerieAnulacao(String dsSerieAnulacao) {
		this.dsSerieAnulacao = dsSerieAnulacao;
	}

	public YearMonthDay getDtEmissaoNfAnulacao() {
		return dtEmissaoNfAnulacao;
	}

	public void setDtEmissaoNfAnulacao(YearMonthDay dtEmissaoNfAnulacao) {
		this.dtEmissaoNfAnulacao = dtEmissaoNfAnulacao;
	}

	public BigDecimal getVlNfAnulacao() {
		return vlNfAnulacao;
	}

	public void setVlNfAnulacao(BigDecimal vlNfAnulacao) {
		this.vlNfAnulacao = vlNfAnulacao;
	}

	public BigDecimal getVlImpostoPesoDeclarado() {
		return vlImpostoPesoDeclarado;
}

	public void setVlImpostoPesoDeclarado(BigDecimal vlImpostoPesoDeclarado) {
		this.vlImpostoPesoDeclarado = vlImpostoPesoDeclarado;
	}

	public BigDecimal getVlIcmsSubstituicaoTributariaPesoDeclarado() {
		return vlIcmsSubstituicaoTributariaPesoDeclarado;
	}

	public void setVlIcmsSubstituicaoTributariaPesoDeclarado(BigDecimal vlIcmsSubstituicaoTributariaPesoDeclarado) {
		this.vlIcmsSubstituicaoTributariaPesoDeclarado = vlIcmsSubstituicaoTributariaPesoDeclarado;
	}

	public Short getNrDiasAgendamento() {
		return nrDiasAgendamento;
}

	public void setNrDiasAgendamento(Short nrDiasAgendamento) {
		this.nrDiasAgendamento = nrDiasAgendamento;
	}

	public Short getNrDiasTentativasEntregas() {
		return nrDiasTentativasEntregas;
	}

	public void setNrDiasTentativasEntregas(Short nrDiasTentativasEntregas) {
		this.nrDiasTentativasEntregas = nrDiasTentativasEntregas;
	}

	public YearMonthDay getDtProjetadaEntrega() {
		return dtProjetadaEntrega;
	}

	public void setDtProjetadaEntrega(YearMonthDay dtProjetadaEntrega) {
		this.dtProjetadaEntrega = dtProjetadaEntrega;
	}

	public String getDsMotivoAnulacao() {
		return dsMotivoAnulacao;
}

	public void setDsMotivoAnulacao(String dsMotivoAnulacao) {
		this.dsMotivoAnulacao = dsMotivoAnulacao;
	}

	public List<DoctoServicoFranqueado> getDoctoServicoFranqueados() {
		return doctoServicoFranqueados;
}

	public void setDoctoServicoFranqueados(
			List<DoctoServicoFranqueado> doctoServicoFranqueados) {
		this.doctoServicoFranqueados = doctoServicoFranqueados;
	}

	public List<DetalheColeta> getDetalheColetas() {
		return detalheColetas;
	}

	public void setDetalheColetas(List<DetalheColeta> detalheColetas) {
		this.detalheColetas = detalheColetas;
	}

	public Boolean getBlDesconsiderouPesoCubado() {
		return blDesconsiderouPesoCubado;
	}

	public void setBlDesconsiderouPesoCubado(Boolean blDesconsiderouPesoCubado) {
		this.blDesconsiderouPesoCubado = blDesconsiderouPesoCubado;
	}

	public DomainValue getTpMotivoLiberacao() {
		return tpMotivoLiberacao;
	}

	public void setTpMotivoLiberacao(DomainValue tpMotivoLiberacao) {
		this.tpMotivoLiberacao = tpMotivoLiberacao;
	}

    public Boolean getBlFluxoSubcontratacao() {
        return blFluxoSubcontratacao;
    }

    public void setBlFluxoSubcontratacao(Boolean blFluxoSubcontratacao) {
        this.blFluxoSubcontratacao = blFluxoSubcontratacao;
    }

    public DomainValue getTpCargaDocumento() {
        return tpCargaDocumento;
    }

	public void setTpCargaDocumento(DomainValue tpCargaDocumento) {
		this.tpCargaDocumento = tpCargaDocumento;
	}

	public BigDecimal getVlImpostoDifal() {
		return vlImpostoDifal;
	}

	public void setVlImpostoDifal(BigDecimal vlImpostoDifal) {
		this.vlImpostoDifal = vlImpostoDifal;
	}

	public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

	public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

	public BigDecimal getPcIcmsUfFim() {
		return pcIcmsUfFim;
	}

	public void setPcIcmsUfFim(BigDecimal pcIcmsUfFim) {
		this.pcIcmsUfFim = pcIcmsUfFim;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}
}

