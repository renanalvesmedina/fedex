package com.mercurio.lms.expedicao.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCargaConhScan;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.franqueados.model.DoctoServicoFranqueado;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoDocto;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tributos.model.TipoTributacaoIcms;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vol.model.VolEventosCelular;

/** @author LMS Custom Hibernate CodeGenerator */
public class Conhecimento extends DoctoServico {

    private static final long serialVersionUID = 1L;

    private transient boolean generateUniqueNumber = false;
    private transient boolean blGeraReceita;

    /** persistent field */
    private Long nrConhecimento;

    /** persistent field */
    private Long nrCodigoBarras;

    /** persistent field */
    private String nrCepEntrega;

    /** persistent field */
    private String nrCepColeta;

    /** persistent field */
    private String dsEspecieVolume;

    /** persistent field */
    private DomainValue tpFrete;

    /** persistent field */
    private DomainValue tpConhecimento;

    /** persistent field */
    private Boolean blIndicadorEdi;

    /** persistent field */
    private Boolean blIndicadorFretePercentual;

    /** persistent field */
    private DomainValue tpSituacaoConhecimento;

    /** persistent field */
    private Boolean blPermiteTransferencia;

    /** persistent field */
    private Boolean blReembolso;

    /** persistent field */
    private DomainValue tpDevedorFrete;

    /** persistent field */
    private Boolean blSeguroRr;

    /** persistent field */
    private String dsEnderecoEntrega;

    /** nullable persistent field */
    private Integer dvConhecimento;

    /** nullable persistent field */
    private Boolean blPesoAferido;

    /** nullable persistent field */
    private Boolean blCalculaFrete;

    /** nullable persistent field */
    private Boolean blCalculaServico;

    /** nullable persistent field */
    private Long nrFormulario;

    /** nullable persistent field */
    private Long nrSeloFiscal;

    /** nullable persistent field */
    private String nrEntrega;

    /** nullable persistent field */
    private Long nrReentrega;

    /** nullable persistent field */
    private YearMonthDay dtAutDevMerc;

    /** nullable persistent field */
    private DateTime dtColeta;

    /** nullable persistent field */
    private String dsSerie;

    /** nullable persistent field */
    private String dsSerieSeloFiscal;

    /** nullable persistent field */
    private String dsCodigoColeta;

    /** nullable persistent field */
    private String dsRespAutDevMerc;

    /** nullable persistent field */
    private String dsCtoRedespacho;

    /** nullable persistent field */
    private String dsCalculadoAte;

    /** nullable persistent field */
    private String dsComplementoEntrega;

    /** nullable persistent field */
    private String dsBairroEntrega;

    /** persistent field */
    private DomainValue tpDoctoServico;

    /** persistent field */
    private DomainValue tpCtrcParceria;

    /** nullable persistent field */
    private String dsLocalEntrega;

    /** persistent field */
    private Boolean blColetaEmergencia;

    /** persistent field */
    private Boolean blEntregaEmergencia;

    /** persistent field */
    private Boolean blCtrcCotMac;

    /** nullable persistent field */
    private String nrCtrcSubcontratante;

    /** nullable persistent field */
    private String tpSituacaoAtualizacao;

    /** persistent field */
    private TipoTributacaoIcms tipoTributacaoIcms;

    /** persistent field */
    private TipoLogradouro tipoLogradouroEntrega;

    /** persistent field */
    private Municipio municipioByIdMunicipioEntrega;

    /** persistent field */
    private Municipio municipioByIdMunicipioColeta;

    /** persistent field */
    private NaturezaProduto naturezaProduto;

    /** persistent field */
    private TipoLocalizacaoMunicipio localizacaoDestino;

    /** persistent field */
    private TipoLocalizacaoMunicipio localizacaoOrigem;

    /** persistent field */
    private Produto produto;

    /** persistent field */
    private Densidade densidade;

    /** persistent field */
    private Filial filialOrigem;

    /** persistent field */
    private Filial filialByIdFilialDeposito;

    /** persistent field */
    private MotivoCancelamento motivoCancelamento;

    /** persistent field */
    private Aeroporto aeroportoByIdAeroportoOrigem;

    /** persistent field */
    private Aeroporto aeroportoByIdAeroportoDestino;

    /** persistent field */
    private ProdutoEspecifico produtoEspecifico;

    /** persistent field */
    private List<CtoAwb> ctoAwbs;

    /** persistent field */
    private List<ImpostoServico> impostoServicos;

    /** persistent field */
    private List<CtoCtoCooperada> ctoCtoCooperadas;

    /** persistent field */
    private List<Dimensao> dimensoes;

    /** persistent field */
    private List<DoctoServicoSeguros> doctoServicoSeguros;

    /** persistent field */
    private List<NotaFiscalConhecimento> notaFiscalConhecimentos;

    /** persistent field */
    private List<DadosComplemento> dadosComplementos;

    /** Campo transiente utilizado para validação do CTRC **/
    private DoctoServicoDadosCliente dadosCliente;

    /** persistent field */
    private List<VolEventosCelular> volEventosCelulars;

    /** persistent field */
    private List<ControleCargaConhScan> controleCargaConhScans;

    private Integer qtEtiquetasEmitidas;

    private Integer qtPaletes;

    // Setado o valor default para True.
    private Boolean blEmitidoLms = Boolean.TRUE;

    private Long nrOrdemEmissaoEDI;

    private String nrCae;

    @Transient
    private String nrProcessamento;

    /** persistent field */
    private Boolean blProcessamentoTomador;

    /** persistent field */
    private Boolean blRedespachoIntermediario;

    private Boolean blRedespachoColeta;
    private Boolean blObrigaAgendamento;

    /** persistent field */
    private Boolean blProdutoPerigoso;

    /** persistent field */
    private Boolean blProdutoControlado;

    private Boolean blOperacaoSpitFire;

    public Conhecimento() {
        blEmitidoLms = true;
    }

    @SuppressWarnings("rawtypes")
    public Conhecimento(Long idDoctoServico, Long nrDoctoServico, BigDecimal vlTotalDocServico,
                        BigDecimal vlTotalParcelas, BigDecimal vlTotalServicos, BigDecimal vlImposto,
                        BigDecimal vlImpostoPesoDeclarado, BigDecimal vlDesconto, BigDecimal vlLiquido,
                        BigDecimal vlFretePesoDeclarado, BigDecimal vlFreteTabelaCheia, Boolean blParcelas,
                        Boolean blServicosAdicionais, String dsCodigoBarras, BigDecimal vlMercadoria, DateTime dhEmissao,
                        DateTime dhInclusao, DomainValue tpDocumentoServico, Boolean blBloqueado, BigDecimal vlBaseCalcImposto,
                        BigDecimal vlIcmsSubstituicaoTributaria, BigDecimal vlIcmsSubstituicaoTributariaPesoDeclarado,
                        Boolean blIncideIcmsPedagio, Short nrCfop, BigDecimal psReferenciaCalculo, DateTime dhAlteracao,
                        DateTime dhEntradaSetorEntrega, DomainValue tpCalculoPreco, Boolean blPrioridadeCarregamento, String nrAidf,
                        BigDecimal pcAliquotaIcms, BigDecimal psReal, BigDecimal psAforado, BigDecimal psAferido, Integer qtVolumes,
                        YearMonthDay dtPrevEntrega, Short nrDiasPrevEntrega, String dsEnderecoEntregaReal, Short nrDiasRealEntrega,
                        Short nrDiasBloqueio, String obComplementoLocalizacao, Boolean blSpecialService,
                        BigDecimal nrFatorDensidade, Cliente clienteByIdClienteBaseCalculo, Cliente clienteByIdClienteDestinatario,
                        Cliente clienteByIdClienteConsignatario, Cliente clienteByIdClienteRemetente,
                        Cliente clienteByIdClienteRedespacho, Usuario usuarioByIdUsuarioInclusao,
                        Usuario usuarioByIdUsuarioAlteracao, EnderecoPessoa enderecoPessoa, FluxoFilial fluxoFilial,
                        Filial filialDestinoOperacional, InscricaoEstadual inscricaoEstadualRemetente,
                        InscricaoEstadual inscricaoEstadualDestinatario, TabelaPreco tabelaPreco, Moeda moeda, Servico servico,
                        DivisaoCliente divisaoCliente, RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaReal,
                        RotaColetaEntrega rotaColetaEntregaByIdRotaColetaEntregaSugerid, PedidoColeta pedidoColeta,
                        Filial filialByIdFilialDestino, Filial filialByIdFilialOrigem, Pais paisOrigem,
                        RotaIntervaloCep rotaIntervaloCep, RotaIdaVolta rotaIdaVolta, ParametroCliente parametroCliente,
                        TarifaPreco tarifaPreco, Impressora impressora, LocalizacaoMercadoria localizacaoMercadoria,
                        Filial filialLocalizacao, DoctoServico doctoServicoOriginal,
                        List<DoctoServicoIndenizacao> doctoServicoIndenizacoes, List cartaMercadoriaDisposicoes,
                        List<LiberacaoDocServ> liberacaoDocServs, List<DevedorDocServFat> devedorDocServFats,
                        List agendamentoDoctoServicos, List documentoServicoRetiradas, List servicoEmbalagems,
                        List<ServAdicionalDocServ> servAdicionalDocServs, List<OcorrenciaDoctoServico> ocorrenciaDoctoServicos,
                        List<NaoConformidade> naoConformidades, List valorCustos, DomainValue tpSituacaoConhecimento,
                        List<EventoDocumentoServico> eventoDocumentoServicos, List<PreManifestoDocumento> preManifestoDocumentos,
                        List<PreManifestoVolume> preManifestoVolumes, List<ObservacaoDoctoServico> observacaoDoctoServicos,
                        List reciboReembolsoDoctoServicos, List<ManifestoEntregaDocumento> manifestoEntregaDocumentos,
                        List<ManifestoEntregaVolume> manifestoEntregaVolumes, List itemMdasDoctoServico,
                        List reciboReembolsosByIdDoctoServReembolsado, List<ParcelaDoctoServico> parcelaDoctoServicos,
                        List<SinistroDoctoServico> sinistroDoctoServicos, List<DevedorDocServ> devedorDocServs,
                        List mercadoriaPendenciaMzs, List<Cotacao> cotacoes, List registroDocumentoEntregas,
                        List justificativasDoctosNaoCarregados, Boolean blExecutarVerificacaoDocumentoManifestado,
                        Boolean blPaletizacao, List<NotaCreditoDocto> notaCreditoDoctos, BigDecimal psCubadoReal,
                        BigDecimal nrFatorCubagem, BigDecimal psEstatistico, BigDecimal nrCubagemEstatistica,
                        BigDecimal nrFatorCubagemCliente, BigDecimal nrFatorCubagemSegmento, DomainValue tpPesoCalculo,
                        Boolean blUtilizaPesoEdi, Boolean blPesoFatPorCubadoAferido, Boolean blPesoCubadoPorDensidade,
                        BigDecimal vlTotalSorter, BigDecimal vlLiquidoSorter, BigDecimal nrCubagemAferida,
                        BigDecimal nrCubagemCalculo, BigDecimal psCubadoAferido, BigDecimal psCubadoDeclarado,
                        BigDecimal nrCubagemDeclarada, List<ImpostoServico> impostoServicosNFS, String nrChaveNfAnulacao,
                        BigDecimal nrNfAnulacao, String dsSerieAnulacao, YearMonthDay dtEmissaoNfAnulacao, BigDecimal vlNfAnulacao,
                        Short nrDiasAgendamento, Short nrDiasTentativasEntregas, YearMonthDay dtProjetadaEntrega,
                        String dsMotivoAnulacao, List<DoctoServicoFranqueado> doctoServicoFranqueados,
                        List<DetalheColeta> detalheColetas, BigDecimal vlImpostoDifal, DomainValue tpSituacaoPendencia, boolean generateUniqueNumber, boolean blGeraReceita,
                        Long nrConhecimento, Long nrCodigoBarras, String nrCepEntrega, String nrCepColeta, String dsEspecieVolume,
                        DomainValue tpFrete, DomainValue tpConhecimento, Boolean blIndicadorEdi, Boolean blIndicadorFretePercentual,
                        DomainValue tpSituacaoConhecimento1, Boolean blPermiteTransferencia, Boolean blReembolso,
                        DomainValue tpDevedorFrete, Boolean blSeguroRr, String dsEnderecoEntrega, Integer dvConhecimento,
                        Boolean blPesoAferido, Boolean blCalculaFrete, Boolean blCalculaServico, Long nrFormulario,
                        Long nrSeloFiscal, String nrEntrega, Long nrReentrega, YearMonthDay dtAutDevMerc, DateTime dtColeta,
                        String dsSerie, String dsSerieSeloFiscal, String dsCodigoColeta, String dsRespAutDevMerc,
                        String dsCtoRedespacho, String dsCalculadoAte, String dsComplementoEntrega, String dsBairroEntrega,
                        DomainValue tpDoctoServico, DomainValue tpCtrcParceria, String dsLocalEntrega, Boolean blColetaEmergencia,
                        Boolean blEntregaEmergencia, Boolean blCtrcCotMac, String nrCtrcSubcontratante,
                        String tpSituacaoAtualizacao, TipoTributacaoIcms tipoTributacaoIcms, TipoLogradouro tipoLogradouroEntrega,
                        Municipio municipioByIdMunicipioEntrega, Municipio municipioByIdMunicipioColeta,
                        NaturezaProduto naturezaProduto, TipoLocalizacaoMunicipio localizacaoDestino,
                        TipoLocalizacaoMunicipio localizacaoOrigem, Produto produto, Densidade densidade, Filial filialOrigem,
                        Filial filialByIdFilialDeposito, MotivoCancelamento motivoCancelamento,
                        Aeroporto aeroportoByIdAeroportoOrigem, Aeroporto aeroportoByIdAeroportoDestino,
                        ProdutoEspecifico produtoEspecifico, List<CtoAwb> ctoAwbs, List<ImpostoServico> impostoServicos,
                        List<CtoCtoCooperada> ctoCtoCooperadas, List<Dimensao> dimensoes,
                        List<DoctoServicoSeguros> doctoServicoSeguros, List<NotaFiscalConhecimento> notaFiscalConhecimentos,
                        List<DadosComplemento> dadosComplementos, DoctoServicoDadosCliente dadosCliente,
                        List<VolEventosCelular> volEventosCelulars, List<ControleCargaConhScan> controleCargaConhScans,
                        Integer qtEtiquetasEmitidas, Integer qtPaletes, Boolean blEmitidoLms, Long nrOrdemEmissaoEDI, String nrCae,
                        String nrProcessamento) {
        super(idDoctoServico, nrDoctoServico, vlTotalDocServico, vlTotalParcelas, vlTotalServicos, vlImposto,
                vlImpostoPesoDeclarado, vlDesconto, vlLiquido, vlFretePesoDeclarado, vlFreteTabelaCheia, blParcelas,
                blServicosAdicionais, dsCodigoBarras, vlMercadoria, dhEmissao, dhInclusao, tpDocumentoServico,
                blBloqueado, vlBaseCalcImposto, vlIcmsSubstituicaoTributaria, vlIcmsSubstituicaoTributariaPesoDeclarado,
                blIncideIcmsPedagio, nrCfop, psReferenciaCalculo, dhAlteracao, dhEntradaSetorEntrega, tpCalculoPreco,
                blPrioridadeCarregamento, nrAidf, pcAliquotaIcms, psReal, psAforado, psAferido, qtVolumes,
                dtPrevEntrega, nrDiasPrevEntrega, dsEnderecoEntregaReal, nrDiasRealEntrega, nrDiasBloqueio,
                obComplementoLocalizacao, blSpecialService, nrFatorDensidade, clienteByIdClienteBaseCalculo,
                clienteByIdClienteDestinatario, clienteByIdClienteConsignatario, clienteByIdClienteRemetente,
                clienteByIdClienteRedespacho, usuarioByIdUsuarioInclusao, usuarioByIdUsuarioAlteracao, enderecoPessoa,
                fluxoFilial, filialDestinoOperacional, inscricaoEstadualRemetente, inscricaoEstadualDestinatario,
                tabelaPreco, moeda, servico, divisaoCliente, rotaColetaEntregaByIdRotaColetaEntregaReal,
                rotaColetaEntregaByIdRotaColetaEntregaSugerid, pedidoColeta, filialByIdFilialDestino,
                filialByIdFilialOrigem, paisOrigem, rotaIntervaloCep, rotaIdaVolta, parametroCliente, tarifaPreco,
                impressora, localizacaoMercadoria, filialLocalizacao, doctoServicoOriginal, doctoServicoIndenizacoes,
                cartaMercadoriaDisposicoes, liberacaoDocServs, devedorDocServFats, agendamentoDoctoServicos,
                documentoServicoRetiradas, servicoEmbalagems, servAdicionalDocServs, ocorrenciaDoctoServicos,
                naoConformidades, valorCustos, tpSituacaoConhecimento, eventoDocumentoServicos, preManifestoDocumentos,
                preManifestoVolumes, observacaoDoctoServicos, reciboReembolsoDoctoServicos, manifestoEntregaDocumentos,
                manifestoEntregaVolumes, itemMdasDoctoServico, reciboReembolsosByIdDoctoServReembolsado,
                parcelaDoctoServicos, sinistroDoctoServicos, devedorDocServs, mercadoriaPendenciaMzs, cotacoes,
                registroDocumentoEntregas, justificativasDoctosNaoCarregados, blExecutarVerificacaoDocumentoManifestado,
                blPaletizacao, notaCreditoDoctos, psCubadoReal, nrFatorCubagem, psEstatistico, nrCubagemEstatistica,
                nrFatorCubagemCliente, nrFatorCubagemSegmento, tpPesoCalculo, blUtilizaPesoEdi,
                blPesoFatPorCubadoAferido, blPesoCubadoPorDensidade, vlTotalSorter, vlLiquidoSorter, nrCubagemAferida,
                nrCubagemCalculo, psCubadoAferido, psCubadoDeclarado, nrCubagemDeclarada, impostoServicosNFS,
                nrChaveNfAnulacao, nrNfAnulacao, dsSerieAnulacao, dtEmissaoNfAnulacao, vlNfAnulacao, nrDiasAgendamento,
                nrDiasTentativasEntregas, dtProjetadaEntrega, dsMotivoAnulacao, doctoServicoFranqueados,
                detalheColetas, vlImpostoDifal, tpSituacaoPendencia);
        this.generateUniqueNumber = generateUniqueNumber;
        this.blGeraReceita = blGeraReceita;
        this.nrConhecimento = nrConhecimento;
        this.nrCodigoBarras = nrCodigoBarras;
        this.nrCepEntrega = nrCepEntrega;
        this.nrCepColeta = nrCepColeta;
        this.dsEspecieVolume = dsEspecieVolume;
        this.tpFrete = tpFrete;
        this.tpConhecimento = tpConhecimento;
        this.blIndicadorEdi = blIndicadorEdi;
        this.blIndicadorFretePercentual = blIndicadorFretePercentual;
        this.tpSituacaoConhecimento = tpSituacaoConhecimento1;
        this.blPermiteTransferencia = blPermiteTransferencia;
        this.blReembolso = blReembolso;
        this.tpDevedorFrete = tpDevedorFrete;
        this.blSeguroRr = blSeguroRr;
        this.dsEnderecoEntrega = dsEnderecoEntrega;
        this.dvConhecimento = dvConhecimento;
        this.blPesoAferido = blPesoAferido;
        this.blCalculaFrete = blCalculaFrete;
        this.blCalculaServico = blCalculaServico;
        this.nrFormulario = nrFormulario;
        this.nrSeloFiscal = nrSeloFiscal;
        this.nrEntrega = nrEntrega;
        this.nrReentrega = nrReentrega;
        this.dtAutDevMerc = dtAutDevMerc;
        this.dtColeta = dtColeta;
        this.dsSerie = dsSerie;
        this.dsSerieSeloFiscal = dsSerieSeloFiscal;
        this.dsCodigoColeta = dsCodigoColeta;
        this.dsRespAutDevMerc = dsRespAutDevMerc;
        this.dsCtoRedespacho = dsCtoRedespacho;
        this.dsCalculadoAte = dsCalculadoAte;
        this.dsComplementoEntrega = dsComplementoEntrega;
        this.dsBairroEntrega = dsBairroEntrega;
        this.tpDoctoServico = tpDoctoServico;
        this.tpCtrcParceria = tpCtrcParceria;
        this.dsLocalEntrega = dsLocalEntrega;
        this.blColetaEmergencia = blColetaEmergencia;
        this.blEntregaEmergencia = blEntregaEmergencia;
        this.blCtrcCotMac = blCtrcCotMac;
        this.nrCtrcSubcontratante = nrCtrcSubcontratante;
        this.tpSituacaoAtualizacao = tpSituacaoAtualizacao;
        this.tipoTributacaoIcms = tipoTributacaoIcms;
        this.tipoLogradouroEntrega = tipoLogradouroEntrega;
        this.municipioByIdMunicipioEntrega = municipioByIdMunicipioEntrega;
        this.municipioByIdMunicipioColeta = municipioByIdMunicipioColeta;
        this.naturezaProduto = naturezaProduto;
        this.localizacaoDestino = localizacaoDestino;
        this.localizacaoOrigem = localizacaoOrigem;
        this.produto = produto;
        this.densidade = densidade;
        this.filialOrigem = filialOrigem;
        this.filialByIdFilialDeposito = filialByIdFilialDeposito;
        this.motivoCancelamento = motivoCancelamento;
        this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
        this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
        this.produtoEspecifico = produtoEspecifico;
        this.ctoAwbs = ctoAwbs;
        this.impostoServicos = impostoServicos;
        this.ctoCtoCooperadas = ctoCtoCooperadas;
        this.dimensoes = dimensoes;
        this.doctoServicoSeguros = doctoServicoSeguros;
        this.notaFiscalConhecimentos = notaFiscalConhecimentos;
        this.dadosComplementos = dadosComplementos;
        this.dadosCliente = dadosCliente;
        this.volEventosCelulars = volEventosCelulars;
        this.controleCargaConhScans = controleCargaConhScans;
        this.qtEtiquetasEmitidas = qtEtiquetasEmitidas;
        this.qtPaletes = qtPaletes;
        this.blEmitidoLms = blEmitidoLms;
        this.nrOrdemEmissaoEDI = nrOrdemEmissaoEDI;
        this.nrCae = nrCae;
        this.nrProcessamento = nrProcessamento;
    }

    public Conhecimento(Long idDoctoServico, Long nrDoctoServico, BigDecimal vlTotalDocServico,
                        BigDecimal vlTotalParcelas, BigDecimal vlTotalServicos, BigDecimal vlImposto,
                        BigDecimal vlImpostoPesoDeclarado, BigDecimal vlDesconto, BigDecimal vlLiquido,
                        BigDecimal vlFretePesoDeclarado, BigDecimal vlFreteTabelaCheia, Boolean blParcelas,
                        Boolean blServicosAdicionais, String dsCodigoBarras, BigDecimal vlMercadoria, DateTime dhEmissao,
                        DateTime dhInclusao, DomainValue tpDocumentoServico, Boolean blBloqueado, BigDecimal vlBaseCalcImposto,
                        BigDecimal vlIcmsSubstituicaoTributaria, BigDecimal vlIcmsSubstituicaoTributariaPesoDeclarado,
                        boolean generateUniqueNumber, boolean blGeraReceita, Long nrConhecimento, Long nrCodigoBarras,
                        String nrCepEntrega, String nrCepColeta, String dsEspecieVolume, DomainValue tpFrete,
                        DomainValue tpConhecimento, Boolean blIndicadorEdi, Boolean blIndicadorFretePercentual,
                        DomainValue tpSituacaoConhecimento, Boolean blPermiteTransferencia, Boolean blReembolso,
                        DomainValue tpDevedorFrete, Boolean blSeguroRr, String dsEnderecoEntrega, Integer dvConhecimento,
                        Boolean blPesoAferido, Boolean blCalculaFrete, Boolean blCalculaServico, Long nrFormulario) {
        super(idDoctoServico, nrDoctoServico, vlTotalDocServico, vlTotalParcelas, vlTotalServicos, vlImposto,
                vlImpostoPesoDeclarado, vlDesconto, vlLiquido, vlFretePesoDeclarado, vlFreteTabelaCheia, blParcelas,
                blServicosAdicionais, dsCodigoBarras, vlMercadoria, dhEmissao, dhInclusao, tpDocumentoServico,
                blBloqueado, vlBaseCalcImposto, vlIcmsSubstituicaoTributaria,
                vlIcmsSubstituicaoTributariaPesoDeclarado);
        this.generateUniqueNumber = generateUniqueNumber;
        this.blGeraReceita = blGeraReceita;
        this.nrConhecimento = nrConhecimento;
        this.nrCodigoBarras = nrCodigoBarras;
        this.nrCepEntrega = nrCepEntrega;
        this.nrCepColeta = nrCepColeta;
        this.dsEspecieVolume = dsEspecieVolume;
        this.tpFrete = tpFrete;
        this.tpConhecimento = tpConhecimento;
        this.blIndicadorEdi = blIndicadorEdi;
        this.blIndicadorFretePercentual = blIndicadorFretePercentual;
        this.tpSituacaoConhecimento = tpSituacaoConhecimento;
        this.blPermiteTransferencia = blPermiteTransferencia;
        this.blReembolso = blReembolso;
        this.tpDevedorFrete = tpDevedorFrete;
        this.blSeguroRr = blSeguroRr;
        this.dsEnderecoEntrega = dsEnderecoEntrega;
        this.dvConhecimento = dvConhecimento;
        this.blPesoAferido = blPesoAferido;
        this.blCalculaFrete = blCalculaFrete;
        this.blCalculaServico = blCalculaServico;
        this.nrFormulario = nrFormulario;
    }

    public List<ControleCargaConhScan> getControleCargaConhScans() {
        return controleCargaConhScans;
    }

    public void setControleCargaConhScans(List<ControleCargaConhScan> controleCargaConhScans) {
        this.controleCargaConhScans = controleCargaConhScans;
    }

    public Long getNrConhecimento() {
        return this.nrConhecimento;
    }

    public void setNrConhecimento(Long nrConhecimento) {
        this.nrConhecimento = nrConhecimento;
    }

    public String getNrCepEntrega() {
        return this.nrCepEntrega;
    }

    public void setNrCepEntrega(String nrCepEntrega) {
        this.nrCepEntrega = nrCepEntrega;
    }

    public String getNrCepColeta() {
        return this.nrCepColeta;
    }

    public void setNrCepColeta(String nrCepColeta) {
        this.nrCepColeta = nrCepColeta;
    }

    public String getDsEspecieVolume() {
        return this.dsEspecieVolume;
    }

    public void setDsEspecieVolume(String dsEspecieVolume) {
        this.dsEspecieVolume = dsEspecieVolume;
    }

    public DomainValue getTpFrete() {
        return this.tpFrete;
    }

    public void setTpFrete(DomainValue tpFrete) {
        this.tpFrete = tpFrete;
    }

    public DomainValue getTpConhecimento() {
        return this.tpConhecimento;
    }

    public void setTpConhecimento(DomainValue tpConhecimento) {
        this.tpConhecimento = tpConhecimento;
    }

    public Boolean getBlIndicadorEdi() {
        return this.blIndicadorEdi;
    }

    public void setBlIndicadorEdi(Boolean blIndicadorEdi) {
        this.blIndicadorEdi = blIndicadorEdi;
    }

    public Boolean getBlIndicadorFretePercentual() {
        return this.blIndicadorFretePercentual;
    }

    public void setBlIndicadorFretePercentual(Boolean blIndicadorFretePercentual) {
        this.blIndicadorFretePercentual = blIndicadorFretePercentual;
    }

    public DomainValue getTpSituacaoConhecimento() {
        return this.tpSituacaoConhecimento;
    }

    public void setTpSituacaoConhecimento(DomainValue tpSituacaoConhecimento) {
        this.tpSituacaoConhecimento = tpSituacaoConhecimento;
    }

    public Boolean getBlPermiteTransferencia() {
        return this.blPermiteTransferencia;
    }

    public void setBlPermiteTransferencia(Boolean blPermiteTransferencia) {
        this.blPermiteTransferencia = blPermiteTransferencia;
    }

    public Boolean getBlReembolso() {
        return this.blReembolso;
    }

    public void setBlReembolso(Boolean blReembolso) {
        this.blReembolso = blReembolso;
    }

    public DomainValue getTpDevedorFrete() {
        return this.tpDevedorFrete;
    }

    public void setTpDevedorFrete(DomainValue tpDevedorFrete) {
        this.tpDevedorFrete = tpDevedorFrete;
    }

    public Boolean getBlSeguroRr() {
        return this.blSeguroRr;
    }

    public void setBlSeguroRr(Boolean blSeguroRr) {
        this.blSeguroRr = blSeguroRr;
    }

    public String getDsEnderecoEntrega() {
        return this.dsEnderecoEntrega;
    }

    public void setDsEnderecoEntrega(String dsEnderecoEntrega) {
        this.dsEnderecoEntrega = dsEnderecoEntrega;
    }

    public Integer getDvConhecimento() {
        return this.dvConhecimento;
    }

    public void setDvConhecimento(Integer dvConhecimento) {
        this.dvConhecimento = dvConhecimento;
    }

    public Long getNrFormulario() {
        return this.nrFormulario;
    }

    public void setNrFormulario(Long nrFormulario) {
        this.nrFormulario = nrFormulario;
    }

    public Long getNrSeloFiscal() {
        return this.nrSeloFiscal;
    }

    public void setNrSeloFiscal(Long nrSeloFiscal) {
        this.nrSeloFiscal = nrSeloFiscal;
    }

    public String getNrEntrega() {
        return this.nrEntrega;
    }

    public void setNrEntrega(String nrEntrega) {
        this.nrEntrega = nrEntrega;
    }

    public YearMonthDay getDtAutDevMerc() {
        return dtAutDevMerc;
    }

    public void setDtAutDevMerc(YearMonthDay dtAutDevMerc) {
        this.dtAutDevMerc = dtAutDevMerc;
    }

    public DateTime getDtColeta() {
        return dtColeta;
    }

    public void setDtColeta(DateTime dtColeta) {
        this.dtColeta = dtColeta;
    }

    public String getDsSerie() {
        return this.dsSerie;
    }

    public void setDsSerie(String dsSerie) {
        this.dsSerie = dsSerie;
    }

    public String getDsSerieSeloFiscal() {
        return this.dsSerieSeloFiscal;
    }

    public void setDsSerieSeloFiscal(String dsSerieSeloFiscal) {
        this.dsSerieSeloFiscal = dsSerieSeloFiscal;
    }

    public String getDsCodigoColeta() {
        return this.dsCodigoColeta;
    }

    public void setDsCodigoColeta(String dsCodigoColeta) {
        this.dsCodigoColeta = dsCodigoColeta;
    }

    public String getDsRespAutDevMerc() {
        return this.dsRespAutDevMerc;
    }

    public void setDsRespAutDevMerc(String dsRespAutDevMerc) {
        this.dsRespAutDevMerc = dsRespAutDevMerc;
    }

    public String getDsCtoRedespacho() {
        return this.dsCtoRedespacho;
    }

    public void setDsCtoRedespacho(String dsCtoRedespacho) {
        this.dsCtoRedespacho = dsCtoRedespacho;
    }

    public String getDsCalculadoAte() {
        return this.dsCalculadoAte;
    }

    public void setDsCalculadoAte(String dsCalculadoAte) {
        this.dsCalculadoAte = dsCalculadoAte;
    }

    public String getDsComplementoEntrega() {
        return this.dsComplementoEntrega;
    }

    public void setDsComplementoEntrega(String dsComplementoEntrega) {
        this.dsComplementoEntrega = dsComplementoEntrega;
    }

    public String getDsBairroEntrega() {
        return this.dsBairroEntrega;
    }

    public void setDsBairroEntrega(String dsBairroEntrega) {
        this.dsBairroEntrega = dsBairroEntrega;
    }

    public DomainValue getTpDoctoServico() {
        return this.tpDoctoServico;
    }

    public void setTpDoctoServico(DomainValue tpDoctoServico) {
        this.tpDoctoServico = tpDoctoServico;
    }

    public DomainValue getTpCtrcParceria() {
        return this.tpCtrcParceria;
    }

    public void setTpCtrcParceria(DomainValue tpCtrcParceria) {
        this.tpCtrcParceria = tpCtrcParceria;
    }

    public String getDsLocalEntrega() {
        return this.dsLocalEntrega;
    }

    public void setDsLocalEntrega(String dsLocalEntrega) {
        this.dsLocalEntrega = dsLocalEntrega;
    }

    public Boolean getBlColetaEmergencia() {
        return this.blColetaEmergencia;
    }

    public void setBlColetaEmergencia(Boolean blColetaEmergencia) {
        this.blColetaEmergencia = blColetaEmergencia;
    }

    public Boolean getBlEntregaEmergencia() {
        return this.blEntregaEmergencia;
    }

    public void setBlEntregaEmergencia(Boolean blEntregaEmergencia) {
        this.blEntregaEmergencia = blEntregaEmergencia;
    }

    public String getNrCtrcSubcontratante() {
        return this.nrCtrcSubcontratante;
    }

    public void setNrCtrcSubcontratante(String nrCtrcSubcontratante) {
        this.nrCtrcSubcontratante = nrCtrcSubcontratante;
    }

    public TipoTributacaoIcms getTipoTributacaoIcms() {
        return this.tipoTributacaoIcms;
    }

    public void setTipoTributacaoIcms(TipoTributacaoIcms tipoTributacaoIcms) {
        this.tipoTributacaoIcms = tipoTributacaoIcms;
    }

    public TipoLogradouro getTipoLogradouroEntrega() {
        return this.tipoLogradouroEntrega;
    }

    public void setTipoLogradouroEntrega(TipoLogradouro tipoLogradouroEntrega) {
        this.tipoLogradouroEntrega = tipoLogradouroEntrega;
    }

    public Municipio getMunicipioByIdMunicipioEntrega() {
        return this.municipioByIdMunicipioEntrega;
    }

    public void setMunicipioByIdMunicipioEntrega(Municipio municipioByIdMunicipioEntrega) {
        this.municipioByIdMunicipioEntrega = municipioByIdMunicipioEntrega;
    }

    public Municipio getMunicipioByIdMunicipioColeta() {
        return this.municipioByIdMunicipioColeta;
    }

    public void setMunicipioByIdMunicipioColeta(Municipio municipioByIdMunicipioColetada) {
        this.municipioByIdMunicipioColeta = municipioByIdMunicipioColetada;
    }

    public NaturezaProduto getNaturezaProduto() {
        return this.naturezaProduto;
    }

    public void setNaturezaProduto(NaturezaProduto naturezaProduto) {
        this.naturezaProduto = naturezaProduto;
    }

    public TipoLocalizacaoMunicipio getLocalizacaoDestino() {
        return this.localizacaoDestino;
    }

    public void setLocalizacaoDestino(TipoLocalizacaoMunicipio localizacaoDestino) {
        this.localizacaoDestino = localizacaoDestino;
    }

    public TipoLocalizacaoMunicipio getLocalizacaoOrigem() {
        return this.localizacaoOrigem;
    }

    public void setLocalizacaoOrigem(TipoLocalizacaoMunicipio localizacaoOrigem) {
        this.localizacaoOrigem = localizacaoOrigem;
    }

    public Produto getProduto() {
        return this.produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Densidade getDensidade() {
        return this.densidade;
    }

    public void setDensidade(Densidade densidade) {
        this.densidade = densidade;
    }

    public Filial getFilialOrigem() {
        return this.filialOrigem;
    }

    public void setFilialOrigem(Filial filialOrigem) {
        this.filialOrigem = filialOrigem;
    }

    public Filial getFilialByIdFilialDeposito() {
        return this.filialByIdFilialDeposito;
    }

    public void setFilialByIdFilialDeposito(Filial filialByIdFilialDeposito) {
        this.filialByIdFilialDeposito = filialByIdFilialDeposito;
    }

    public MotivoCancelamento getMotivoCancelamento() {
        return this.motivoCancelamento;
    }

    public void setMotivoCancelamento(MotivoCancelamento motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public Aeroporto getAeroportoByIdAeroportoOrigem() {
        return this.aeroportoByIdAeroportoOrigem;
    }

    public void setAeroportoByIdAeroportoOrigem(Aeroporto aeroportoByIdAeroportoOrigem) {
        this.aeroportoByIdAeroportoOrigem = aeroportoByIdAeroportoOrigem;
    }

    public Aeroporto getAeroportoByIdAeroportoDestino() {
        return this.aeroportoByIdAeroportoDestino;
    }

    public void setAeroportoByIdAeroportoDestino(Aeroporto aeroportoByIdAeroportoDestino) {
        this.aeroportoByIdAeroportoDestino = aeroportoByIdAeroportoDestino;
    }

    public ProdutoEspecifico getProdutoEspecifico() {
        return this.produtoEspecifico;
    }

    public void setProdutoEspecifico(ProdutoEspecifico produtoEspecifico) {
        this.produtoEspecifico = produtoEspecifico;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoAwb.class)
    public List<CtoAwb> getCtoAwbs() {
        return this.ctoAwbs;
    }

    public void setCtoAwbs(List<CtoAwb> ctoAwbs) {
        this.ctoAwbs = ctoAwbs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ImpostoServico.class)
    public List<ImpostoServico> getImpostoServicos() {
        return this.impostoServicos;
    }

    public void setImpostoServicos(List<ImpostoServico> impostoServicos) {
        this.impostoServicos = impostoServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class)
    public List<CtoCtoCooperada> getCtoCtoCooperadas() {
        return this.ctoCtoCooperadas;
    }

    public void setCtoCtoCooperadas(List<CtoCtoCooperada> ctoCtoCooperadas) {
        this.ctoCtoCooperadas = ctoCtoCooperadas;
    }

    public void addCtoCtoCooperada(CtoCtoCooperada ctoCtoCooperada) {
        if (this.ctoCtoCooperadas == null) {
            this.ctoCtoCooperadas = new ArrayList<CtoCtoCooperada>();
        }
        this.ctoCtoCooperadas.add(ctoCtoCooperada);
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Dimensao.class)
    public List<Dimensao> getDimensoes() {
        return this.dimensoes;
    }

    public void setDimensoes(List<Dimensao> dimensoes) {
        this.dimensoes = dimensoes;
    }

    public void addDimensao(Dimensao dimensao) {
        if (this.dimensoes == null) {
            this.dimensoes = new ArrayList<Dimensao>();
        }
        this.dimensoes.add(dimensao);
    }

    public boolean removeDimensao(Dimensao dimensao) {
        if (this.dimensoes != null) {
            return this.dimensoes.remove(dimensao);
        }
        return false;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DoctoServicoSeguros.class)
    public List<DoctoServicoSeguros> getDoctoServicoSeguros() {
        return this.doctoServicoSeguros;
    }

    public void setDoctoServicoSeguros(List<DoctoServicoSeguros> doctoServicoSeguros) {
        this.doctoServicoSeguros = doctoServicoSeguros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.NotaFiscalConhecimento.class)
    public List<NotaFiscalConhecimento> getNotaFiscalConhecimentos() {
        return this.notaFiscalConhecimentos;
    }

    public void setNotaFiscalConhecimentos(List<NotaFiscalConhecimento> notaFiscalConhecimentos) {
        this.notaFiscalConhecimentos = notaFiscalConhecimentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DadosComplemento.class)
    public List<DadosComplemento> getDadosComplementos() {
        return this.dadosComplementos;
    }

    public void setDadosComplementos(List<DadosComplemento> dadosComplementos) {
        this.dadosComplementos = dadosComplementos;
    }

    public void addDadoComplemento(DadosComplemento dadosComplemento) {
        if (dadosComplementos == null) {
            dadosComplementos = new ArrayList<DadosComplemento>();

        } else {
            Iterator<DadosComplemento> iterator = dadosComplementos.iterator();
            while (iterator.hasNext()) {
                DadosComplemento element = iterator.next();
                if (isEqualDadoComplemento(element, dadosComplemento)) {
                    iterator.remove();
                }
            }
        }

        if (dadosComplemento != null && StringUtils.isNotEmpty(dadosComplemento.getDsValorCampo())) {
            this.dadosComplementos.add(dadosComplemento);
        }
    }

    public boolean isEqualDadoComplemento(DadosComplemento existente, DadosComplemento novo) {
        return existente != null && existente.getInformacaoDocServico() != null
                && existente.getInformacaoDocServico().getDsCampo() != null && novo != null
                && novo.getInformacaoDocServico() != null && novo.getInformacaoDocServico().getDsCampo() != null
                && existente.getInformacaoDocServico().getDsCampo().equals(novo.getInformacaoDocServico().getDsCampo());
    }

    public boolean removeDadoComplemento(DadosComplemento dadoComplemento) {
        if (this.dadosComplementos != null) {
            return this.dadosComplementos.remove(dadoComplemento);
        }
        return false;
    }

    public void removeAllDadoComplemento() {
        if (this.dadosComplementos != null) {
            this.dadosComplementos.clear();
        }
    }

    public void removeDadoComplementoByDsCampo(String dsCampo) {
        if (this.dadosComplementos == null || StringUtils.isBlank(dsCampo)) {
            return;
        }
        for (int i = 0; i < this.dadosComplementos.size(); i++) {
            DadosComplemento dadosComplemento = (DadosComplemento) this.dadosComplementos.get(i);
            InformacaoDocServico informacaoDocServico = dadosComplemento.getInformacaoDocServico();
            InformacaoDoctoCliente informacaoDoctoCliente = dadosComplemento.getInformacaoDoctoCliente();
            if (informacaoDoctoCliente == null
                    && (informacaoDocServico == null || dsCampo.equals(informacaoDocServico.getDsCampo()))) {
                this.dadosComplementos.remove(i);
            }
        }
    }

    public DoctoServicoDadosCliente getDadosCliente() {
        if (this.dadosCliente == null) {
            this.dadosCliente = new DoctoServicoDadosCliente();
        }
        return this.dadosCliente;
    }

    public void setDadosCliente(DoctoServicoDadosCliente dadosCliente) {
        this.dadosCliente = dadosCliente;
    }

    public List<VolEventosCelular> getVolEventosCelulars() {
        return volEventosCelulars;
    }

    public void setVolEventosCelulars(List<VolEventosCelular> volEventosCelulars) {
        this.volEventosCelulars = volEventosCelulars;
    }

    /**
     * @return the blPesoAferido
     */
    public Boolean getBlPesoAferido() {
        return blPesoAferido;
    }

    /**
     * @param blPesoAferido
     *            the blPesoAferido to set
     */
    public void setBlPesoAferido(Boolean blPesoAferido) {
        this.blPesoAferido = blPesoAferido;
    }

    public Boolean getBlCtrcCotMac() {
        return blCtrcCotMac;
    }

    public void setBlCtrcCotMac(Boolean blCtrcCotMac) {
        this.blCtrcCotMac = blCtrcCotMac;
    }

    /**
     * @return the blCalculaFrete
     */
    public Boolean getBlCalculaFrete() {
        return blCalculaFrete;
    }

    /**
     * @param blCalculaFrete
     *            the blCalculaFrete to set
     */
    public void setBlCalculaFrete(Boolean blCalculaFrete) {
        this.blCalculaFrete = blCalculaFrete;
    }

    /**
     * @return the blCalculaServico
     */
    public Boolean getBlCalculaServico() {
        return blCalculaServico;
    }

    /**
     * @param blCalculaServico
     *            the blCalculaServico to set
     */
    public void setBlCalculaServico(Boolean blCalculaServico) {
        this.blCalculaServico = blCalculaServico;
    }

    /**
     * @return the tpSituacaoAtualizacao
     */
    public String getTpSituacaoAtualizacao() {
        return tpSituacaoAtualizacao;
    }

    /**
     * @param tpSituacaoAtualizacao
     *            the tpSituacaoAtualizacao to set
     */
    public void setTpSituacaoAtualizacao(String tpSituacaoAtualizacao) {
        this.tpSituacaoAtualizacao = tpSituacaoAtualizacao;
    }

    /**
     * @return the nrCodigoBarras to get
     */

    public Long getNrCodigoBarras() {
        return nrCodigoBarras;
    }

    /**
     * @param nrCodigoBarras
     *            the nrCodigoBarras to set
     */
    public void setNrCodigoBarras(Long nrCodigoBarras) {
        this.nrCodigoBarras = nrCodigoBarras;
    }

    public Integer getQtEtiquetasEmitidas() {
        return qtEtiquetasEmitidas;
    }

    public void setQtEtiquetasEmitidas(Integer qtEtiquetasEmitidas) {
        this.qtEtiquetasEmitidas = qtEtiquetasEmitidas;
    }

    public Integer getQtPaletes() {
        return qtPaletes;
    }

    public void setQtPaletes(Integer qtPaletes) {
        this.qtPaletes = qtPaletes;
    }

    public Long getNrReentrega() {
        return nrReentrega;
    }

    public void setNrReentrega(Long nrReentrega) {
        this.nrReentrega = nrReentrega;
    }

    public Boolean getBlEmitidoLms() {
        return blEmitidoLms;
    }

    public void setBlEmitidoLms(Boolean blEmitidoLms) {
        this.blEmitidoLms = blEmitidoLms;
    }

    public Long calculaQtVolumesTotalFrota() {
        Long qtVolumesTotalFrota = null;
        List<NotaFiscalConhecimento> list = this.getNotaFiscalConhecimentos();
        if (list != null && list.size() > 0) {
            qtVolumesTotalFrota = 0l;
            for (int i = 0; i < list.size(); i++) {
                NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento) list.get(i);
                qtVolumesTotalFrota = qtVolumesTotalFrota + notaFiscalConhecimento.getQtVolumes();
            }
        }
        return qtVolumesTotalFrota;
    }

    public String getNrCae() {
        return nrCae;
    }

    public void setNrCae(String nrCae) {
        this.nrCae = nrCae;
    }

    public String getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(String nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Long getNrOrdemEmissaoEDI() {
        return nrOrdemEmissaoEDI;
    }

    public void setNrOrdemEmissaoEDI(Long nrOrdemEmissaoEDI) {
        this.nrOrdemEmissaoEDI = nrOrdemEmissaoEDI;
    }

    public boolean isGenerateUniqueNumber() {
        return generateUniqueNumber;
    }

    public void setGenerateUniqueNumber(boolean generateUniqueNumber) {
        this.generateUniqueNumber = generateUniqueNumber;
    }

    public void setBlGeraReceita(boolean blGeraReceita) {
        this.blGeraReceita = blGeraReceita;
    }

    public boolean isGeraReceita() {
        return blGeraReceita;
    }

    public Boolean getBlProcessamentoTomador() {
        return blProcessamentoTomador;
    }

    public void setBlProcessamentoTomador(Boolean blProcessamentoTomador) {
        this.blProcessamentoTomador = blProcessamentoTomador;
    }

    public Boolean getBlRedespachoIntermediario() {
        return blRedespachoIntermediario;
    }

    public void setBlRedespachoIntermediario(Boolean blRedespachoIntermediario) {
        this.blRedespachoIntermediario = blRedespachoIntermediario;
    }

    public Boolean getBlRedespachoColeta() {
        return blRedespachoColeta;
    }

    public void setBlRedespachoColeta(Boolean blRedespachoColeta) {
        this.blRedespachoColeta = blRedespachoColeta;
    }

    public Boolean getBlObrigaAgendamento() {
        return blObrigaAgendamento;
    }

    public void setBlObrigaAgendamento(Boolean blObrigaAgendamento) {
        this.blObrigaAgendamento = blObrigaAgendamento;
    }	public Boolean getBlProdutoPerigoso() {
        return blProdutoPerigoso;
    }

    public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
        this.blProdutoPerigoso = blProdutoPerigoso;
    }

    public Boolean getBlProdutoControlado() {
        return blProdutoControlado;
    }

    public void setBlProdutoControlado(Boolean blProdutoControlado) {
        this.blProdutoControlado = blProdutoControlado;
    }

    public Boolean getBlOperacaoSpitFire() { return blOperacaoSpitFire; }

    public void setBlOperacaoSpitFire(Boolean blOperacaoSpitFire) { this.blOperacaoSpitFire = blOperacaoSpitFire; }
}
