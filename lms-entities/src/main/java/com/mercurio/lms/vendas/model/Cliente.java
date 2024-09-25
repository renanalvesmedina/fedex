package com.mercurio.lms.vendas.model;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/** @author LMS Custom Hibernate CodeGenerator */
public class Cliente implements Serializable {
	private static final long serialVersionUID = 5L;

	/** identifier field */
	private Long idCliente;

	private DomainValue tpSituacao;
	private DomainValue tpCliente;
	private DomainValue tpClienteSolicitado;
	private YearMonthDay dtGeracao;
	private Boolean blMatriz;

	private Cliente clienteMatriz;

	/** nullable persistent field */
	private Long nrConta;

	/** nullable persistent field */
	private Long nrInscricaoSuframa;

	/** nullable persistent field */
	private String obFatura;

	/** nullable persistent field */
	private YearMonthDay dtUltimoMovimento;

	/** nullable persistent field */
	private String dsSite;

	/** nullable persistent field */
	private String obCliente;

	/** nullable persistent field */
	private DomainValue tpAtividadeEconomica;

	/** nullable persistent field */
	private YearMonthDay dtFundacaoEmpresa;

	/** nullable persistent field */
	private Pessoa pessoa;

	private GrupoEconomico grupoEconomico;

	private Usuario usuarioByIdUsuarioInclusao;

	private Usuario usuarioByIdUsuarioAlteracao;

	private RamoAtividade ramoAtividade;

	private SegmentoMercado segmentoMercado;

	private ObservacaoConhecimento observacaoConhecimento;

	private List usuariosCliente;
	
	private Boolean temPendenciaTpCliente;

	private Boolean	blEnviaDacteXmlFat;
	
	/*COMERCIAL COMERCIAL COMERCIAL COMERCIAL COMERCIAL COMERCIAL COMERCIAL*/
	private Boolean blPermiteCte;

	private Boolean blSeparaFaturaModal;

	private Boolean blObrigaSerie;

	private Boolean blPermanente;

	private Boolean blCobraReentrega;

	private Boolean blCobraDevolucao;

	private Boolean blFobDirigido;

	private Boolean blFobDirigidoAereo;

	private Boolean blPesoAforadoPedagio;

	private Boolean blIcmsPedagio;

	private Short nrCasasDecimaisPeso;

	/** nullable persistent field */
	private BigDecimal vlFaturamentoPrevisto;

	/** nullable persistent field */
	private DomainValue tpFormaArredondamento;

	/** nullable persistent field */
	private DomainValue tpFrequenciaVisita;

	/** nullable persistent field */
	private Boolean blOperadorLogistico;

	/** nullable persistent field */
	private Boolean blAceitaFobGeral;

	private Boolean blNaoAtualizaDBI;

	private Boolean blAtualizaDestinatarioEdi;

	private Boolean blAtualizaConsignatarioEdi;

	private Filial filialByIdFilialAtendeComercial;

	private Filial filialByIdFilialComercialSolicitada;
	

	private Regional regionalComercial;


	private Moeda moedaByIdMoedaFatPrev;
	
	private Boolean blClienteEstrategico;

	private Boolean blDpeFeriado;

	/*
	 * OPERACIONAL OPERACIONAL OPERACIONAL OPERACIONAL OPERACIONAL OPERACIONAL
	 * OPERACIONAL
	 */
	private Boolean blPesagemOpcional;

	private Boolean blColetaAutomatica;

	/** nullable persistent field */
	private DomainValue tpDificuldadeColeta;

	/** nullable persistent field */
	private DomainValue tpDificuldadeEntrega;

	/** nullable persistent field */
	private DomainValue tpDificuldadeClassificacao;

	/** nullable persistent field */
	private DomainValue tpLocalEmissaoConReent;
	
	/** nullable persistent field */
	private Boolean blAgrupaNotas;

	/** nullable persistent field */
	private Boolean blCadastradoColeta;

	/** nullable persistent field */
	private Boolean blAgendamentoPessoaFisica;

	/** nullable persistent field */
	private Boolean blAgendamentoPessoaJuridica;

	/** nullable persistent field */
	private Boolean blObrigaRecebedor;

	private Filial filialByIdFilialAtendeOperacional;

	private Filial filialByIdFilialOperacionalSolicitada;
	

	private Regional regionalOperacional;	

	private InformacaoDoctoCliente informacaoDoctoCliente;
	
	private DomainValue tpAgrupamentoEDI;
	
	/** nullable persistent field */
	private Boolean blAgendamento;
	
	/** nullable persistent field */
	private Boolean blConfAgendamento;
	
	private Boolean blRecolheICMS;
	
	private Boolean blPermiteEmbarqueRodoNoAereo;
	
	private Boolean blSeguroDiferenciadoAereo;
	
	private Boolean blClienteCCT;

	private Boolean bldesconsiderarDificuldade;
	
	private Boolean blEmissaoDiaNaoUtil;
	
	private Boolean blEmissaoSabado;
	
	private Boolean blObrigaBO;
	
	private Boolean blRemetenteOTD;
	
	private Boolean blObrigaPesoCubadoEdi;
	
	private Boolean blMtzLiberaRIM;
	
	private Boolean blGerarParcelaFreteValorLiquido;
	/*
	 * FINANCEIRO FINANCEIRO FINANCEIRO FINANCEIRO FINANCEIRO FINANCEIRO
	 * FINANCEIRO
	 */
	private Boolean blGeraReciboFreteEntrega;

	private Boolean blResponsavelFrete;

	private Boolean blBaseCalculo;

	private Boolean blIndicadorProtesto;

	private BigDecimal pcDescontoFreteCif;

	private BigDecimal pcDescontoFreteFob;

	/** nullable persistent field */
	private Boolean blEmiteBoletoCliDestino;

	/** nullable persistent field */
	private BigDecimal vlLimiteCredito;

	/** nullable persistent field */
	private Short nrDiasLimiteDebito;

	/** nullable persistent field */
	private DomainValue tpCobranca;

	/** nullable persistent field */
	private DomainValue tpCobrancaSolicitado;

	/** nullable persistent field */
	private DomainValue tpCobrancaAprovado;

	/** nullable persistent field */
	private DomainValue tpFormaCobranca;

	/** nullable persistent field */
	private DomainValue tpMeioEnvioBoleto;

	/** nullable persistent field */
	private Boolean blAgrupaFaturamentoMes;

	/** nullable persistent field */
	private DomainValue tpPeriodicidadeTransf;

	/** nullable persistent field */
	private Boolean blRessarceFreteFob;

	/** nullable persistent field */
	private BigDecimal vlSaldoAtual;

	/** nullable persistent field */
	private Boolean blPreFatura;

	/** nullable persistent field */
	private BigDecimal pcJuroDiario;

	/** nullable persistent field */
	private Boolean blFaturaDocsEntregues;

	/** nullable persistent field */
	private Boolean blCobrancaCentralizada;

	/** nullable persistent field */
	private Boolean blFaturaDocsConferidos;

	/** nullable persistent field */
	private Boolean blFronteiraRapida;

	/** nullable persistent field */
	private BigDecimal vlLimiteDocumentos;

	/** nullable persistent field */
	private Long nrReentregasCobranca;

	private Cliente cliente;	

	private Cedente cedente;	

	private Moeda moedaByIdMoedaLimCred;

	private Moeda moedaByIdMoedaLimDoctos;

	private Moeda moedaByIdMoedaSaldoAtual;

	private Filial filialByIdFilialCobranca;

	private Filial filialByIdFilialCobrancaSolicitada;

	private Regional regionalFinanceiro;

	private Boolean blFaturaDocReferencia;

	private Boolean blDificuldadeEntrega;

	private Boolean blRetencaoComprovanteEntrega;

	private Boolean blDivulgaLocalizacao;

	private Boolean blCalculoArqPreFatura;

	private DomainValue tpPesoCalculo;	

	/* EDI */
	private Boolean blNumeroVolumeEDI;

	private DomainValue tpOrdemEmissaoEDI;

	private Boolean blUtilizaPesoEDI; 

	private Boolean blUtilizaFreteEDI; 
	
	private Boolean blLiberaEtiquetaEdi;
	
	private Boolean blClientePostoAvancado;
	
	private InformacaoDoctoCliente informacaoDoctoClienteEDI;
	
	/** transient field */
	private String dsMotivoSolicitacao;

	private List ciaAereaClientes;

	private List horarioCorteClientes;

	private List clienteRegioes;

	private List itemTransferenciasByIdNovoResponsavel;

	private List itemTransferenciasByIdAntigoResponsavel;

	private List substAtendimentoFiliais;

	private List destinatarioEmissaoLotes;

	private List documentoClientes;

	private List divisaoClientes;

	private List atendimentoClientes;

	private List produtoClientes;

	private List clienteDespachantes;

	private List gerenciaRegionais;

	private List tipoTabelaPrecos;

	private List informacaoDoctoClientes;

	private List valorCampoComplementars;

	private List reciboDescontos;

	private List clientes;

	private List detalheColetas;

	private List prazoEntregaClientes;

	private List cheques;

	private List milkRemetentes;

	private List configuracaoComunicacoes;

	private List aliquotaContribuicaoServs;

	private List historicoPces;

	private List seguroClientes;

	private List coletaAutomaticaClientes;

	private List ctoInternacionais;

	private List devedorDocServs;

	private List manifestos;

	private List proibidoEmbarques;

	private List ctoCtoCooperadasByIdDestinatario;

	private List ctoCtoCooperadasByIdRedespacho;

	private List ctoCtoCooperadasByIdDevedor;

	private List ctoCtoCooperadasByIdConsignatario;

	private List ctoCtoCooperadasByIdRemetente;

	private List formaAgrupamentos;

	private List devedorDocServFats;

	private List faturas;

	private List protocoloTransferencias;

	private List promotorClientes;

	private List notaDebitoNacionais;

	private List potencialComercialClientes;

	private List histTrocaFilialClientes;

	private List lotePendencias;

	private List municipioFilialCliOrigems;

	private List clienteEnquadramentos;

	private List classificacaoClientes;

	private List simulacoesByIdCliente;

	private List simulacoesByIdClienteBase;

	private List postoAvancados;

	private List depositoCcorrentes;

	private List cobrancaInadimplencias;

	private List ocorrenciaClientes;

	private List solicitacaoRetiradasByIdClienteRemetente;

	private List solicitacaoRetiradasByIdClienteDestinatario;

	private List versaoPces;

	private List eventoClientes;

	private List milkRuns;

	private List pedidoComprasByIdClienteRemetente;

	private List pedidoComprasByIdClienteDestinatario;

	private List liberacaoEmbarques;

	private List agendaTransferencias;

	private List visitas;

	private List awbsByIdClienteExpedidor;

	private List awbsByIdClienteDestinatario;

	private List cotacoesByIdCliente;

	private List cotacoesByIdClienteSolicitou;

	private List mercadoriaPendenciaMzs;

	private List naoConformidadesByIdClienteRemetente;

	private List naoConformidadesByIdClienteDestinatario;

	private List usuariosPadraoCliente;
	
	private Boolean blAtualizaIEDestinatarioEdi;

	private Boolean blSemChaveNfeEdi;

	private Boolean blVeiculoDedicado;

	private Boolean blAgendamentoEntrega;

	private Boolean blPaletizacao;

	private Boolean blPaleteFechado;

	private Boolean blEtiquetaPorVolume;

	private Boolean blCustoDescarga;
	
	private Boolean blEmiteDacteFaturamento;

	private List rotaViagems;

	private DivisaoCliente divisaoClienteResponsavel;
	private Boolean blEmissorNfe;
	private DomainValue tpEmissaoDoc;
	private DomainValue tpModalObrigaBO;
	private Boolean blAtualizaRazaoSocialDest;
	private InformacaoDoctoCliente informacaoDoctoClienteConsolidado;
	private Boolean blGeraNovoDPE;
	private Boolean blAssinaturaDigital;
	private Boolean blLiberaEtiquetaEdiLinehaul;
	private Boolean blDivisao;
	private Boolean blObrigaComprovanteEntrega;
	private Boolean blNfeConjulgada;
	private Boolean blObrigaRg;
	private Boolean blObrigaRgEdi;
	private Boolean blPermiteBaixaParcial;
	private Boolean blObrigaBaixaPorVolume;
	private Boolean blObrigaQuizBaixa;
	private Boolean blObrigaParentesco;
	private Boolean blProdutoPerigoso;
	private Boolean blControladoPoliciaCivil;
	private Boolean blControladoPoliciaFederal;
	private Boolean blControladoExercito;
    private Boolean blNaoPermiteSubcontratacao;
	private Boolean blEnviaDocsFaturamentoNas;
	private Boolean blValidaCobrancDifTdeDest;
	private Boolean blCobrancaTdeDiferenciada;
	private BigDecimal vlLimiteValorMercadoriaCteRodo;
	private BigDecimal vlLimiteValorMercadoriaCteAereo;

	public Cliente(Long id) {
		this.idCliente = id;
	}

	public Cliente() {
	}

	public Cliente(Long idCliente, Boolean blAtualizaDestinatarioEdi, Boolean blObrigaPesoCubadoEdi) {
		this.idCliente = idCliente;
		this.blAtualizaDestinatarioEdi = blAtualizaDestinatarioEdi;
		this.blObrigaPesoCubadoEdi = blObrigaPesoCubadoEdi;
	}

	public Long getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public DomainValue getTpSituacao() {
		return this.tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public DomainValue getTpCliente() {
		return this.tpCliente;
	}

	public void setTpCliente(DomainValue tpCliente) {
		this.tpCliente = tpCliente;
	}

	public YearMonthDay getDtGeracao() {
		return this.dtGeracao;
	}

	public void setDtGeracao(YearMonthDay dtGeracao) {
		this.dtGeracao = dtGeracao;
	}

	public Boolean getBlMatriz() {
		return this.blMatriz;
	}

	public void setBlMatriz(Boolean blMatriz) {
		this.blMatriz = blMatriz;
	}

	public Cliente getClienteMatriz() {
		return clienteMatriz;
	}

	public void setClienteMatriz(Cliente clienteMatriz) {
		this.clienteMatriz = clienteMatriz;
	}

	public Long getNrConta() {
		return this.nrConta;
	}

	public void setNrConta(Long nrConta) {
		this.nrConta = nrConta;
	}

	public YearMonthDay getDtUltimoMovimento() {
		return this.dtUltimoMovimento;
	}

	public void setDtUltimoMovimento(YearMonthDay dtUltimoMovimento) {
		this.dtUltimoMovimento = dtUltimoMovimento;
	}

	public String getDsSite() {
		return this.dsSite;
	}

	public void setDsSite(String dsSite) {
		this.dsSite = dsSite;
	}

	public String getObCliente() {
		return this.obCliente;
	}

	public void setObCliente(String obCliente) {
		this.obCliente = obCliente;
	}

	public DomainValue getTpAtividadeEconomica() {
		return this.tpAtividadeEconomica;
	}

	public void setTpAtividadeEconomica(DomainValue tpAtividadeEconomica) {
		this.tpAtividadeEconomica = tpAtividadeEconomica;
	}

	public YearMonthDay getDtFundacaoEmpresa() {
		return this.dtFundacaoEmpresa;
	}

	public void setDtFundacaoEmpresa(YearMonthDay dtFundacaoEmpresa) {
		this.dtFundacaoEmpresa = dtFundacaoEmpresa;
	}

	public Pessoa getPessoa() {
		return this.pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public GrupoEconomico getGrupoEconomico() {
		return this.grupoEconomico;
	}

	public void setGrupoEconomico(GrupoEconomico grupoEconomico) {
		this.grupoEconomico = grupoEconomico;
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

	public RamoAtividade getRamoAtividade() {
		return this.ramoAtividade;
	}

	public void setRamoAtividade(RamoAtividade ramoAtividade) {
		this.ramoAtividade = ramoAtividade;
	}

	public SegmentoMercado getSegmentoMercado() {
		return this.segmentoMercado;
	}

	public void setSegmentoMercado(SegmentoMercado segmentoMercado) {
		this.segmentoMercado = segmentoMercado;
	}

	public Boolean getBlDificuldadeEntrega() {
		return blDificuldadeEntrega;
	}

	public void setBlDificuldadeEntrega(Boolean blDificuldadeEntrega) {
		this.blDificuldadeEntrega = blDificuldadeEntrega;
	}

	public Boolean getBlDivulgaLocalizacao() {
		return blDivulgaLocalizacao;
	}

	public void setBlDivulgaLocalizacao(Boolean blDivulgaLocalizacao) {
		this.blDivulgaLocalizacao = blDivulgaLocalizacao;
	}

	public Boolean getBlRetencaoComprovanteEntrega() {
		return blRetencaoComprovanteEntrega;
	}

	public void setBlRetencaoComprovanteEntrega(
			Boolean blRetencaoComprovanteEntrega) {
		this.blRetencaoComprovanteEntrega = blRetencaoComprovanteEntrega;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.CiaAereaCliente.class) 
	public List getCiaAereaClientes() {
		return this.ciaAereaClientes;
	}

	public void setCiaAereaClientes(List ciaAereaClientes) {
		this.ciaAereaClientes = ciaAereaClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.HorarioCorteCliente.class) 
	public List getHorarioCorteClientes() {
		return this.horarioCorteClientes;
	}

	public void setHorarioCorteClientes(List horarioCorteClientes) {
		this.horarioCorteClientes = horarioCorteClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ClienteRegiao.class) 
	public List getClienteRegioes() {
		return this.clienteRegioes;
	}

	public void setClienteRegioes(List clienteRegioes) {
		this.clienteRegioes = clienteRegioes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemTransferencia.class) 
	public List getItemTransferenciasByIdNovoResponsavel() {
		return this.itemTransferenciasByIdNovoResponsavel;
	}

	public void setItemTransferenciasByIdNovoResponsavel(
			List itemTransferenciasByIdNovoResponsavel) {
		this.itemTransferenciasByIdNovoResponsavel = itemTransferenciasByIdNovoResponsavel;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemTransferencia.class) 
	public List getItemTransferenciasByIdAntigoResponsavel() {
		return this.itemTransferenciasByIdAntigoResponsavel;
	}

	public void setItemTransferenciasByIdAntigoResponsavel(
			List itemTransferenciasByIdAntigoResponsavel) {
		this.itemTransferenciasByIdAntigoResponsavel = itemTransferenciasByIdAntigoResponsavel;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.SubstAtendimentoFilial.class) 
	public List getSubstAtendimentoFiliais() {
		return this.substAtendimentoFiliais;
	}

	public void setSubstAtendimentoFiliais(List substAtendimentoFiliais) {
		this.substAtendimentoFiliais = substAtendimentoFiliais;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.DestinatarioEmissaoLote.class) 
	public List getDestinatarioEmissaoLotes() {
		return this.destinatarioEmissaoLotes;
	}

	public void setDestinatarioEmissaoLotes(List destinatarioEmissaoLotes) {
		this.destinatarioEmissaoLotes = destinatarioEmissaoLotes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DocumentoCliente.class) 
	public List getDocumentoClientes() {
		return this.documentoClientes;
	}

	public void setDocumentoClientes(List documentoClientes) {
		this.documentoClientes = documentoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.DivisaoCliente.class) 
	public List getDivisaoClientes() {
		return this.divisaoClientes;
	}

	public void setDivisaoClientes(List divisaoClientes) {
		this.divisaoClientes = divisaoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.AtendimentoCliente.class) 
	public List getAtendimentoClientes() {
		return this.atendimentoClientes;
	}

	public void setAtendimentoClientes(List atendimentoClientes) {
		this.atendimentoClientes = atendimentoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ProdutoCliente.class) 
	public List getProdutoClientes() {
		return this.produtoClientes;
	}

	public void setProdutoClientes(List produtoClientes) {
		this.produtoClientes = produtoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ClienteDespachante.class) 
	public List getClienteDespachantes() {
		return this.clienteDespachantes;
	}

	public void setClienteDespachantes(List clienteDespachantes) {
		this.clienteDespachantes = clienteDespachantes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.GerenciaRegional.class) 
	public List getGerenciaRegionais() {
		return this.gerenciaRegionais;
	}

	public void setGerenciaRegionais(List gerenciaRegionais) {
		this.gerenciaRegionais = gerenciaRegionais;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.TipoTabelaPreco.class) 
	public List getTipoTabelaPrecos() {
		return this.tipoTabelaPrecos;
	}

	public void setTipoTabelaPrecos(List tipoTabelaPrecos) {
		this.tipoTabelaPrecos = tipoTabelaPrecos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.InformacaoDoctoCliente.class) 
	public List getInformacaoDoctoClientes() {
		return this.informacaoDoctoClientes;
	}

	public void setInformacaoDoctoClientes(List informacaoDoctoClientes) {
		this.informacaoDoctoClientes = informacaoDoctoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ValorCampoComplementar.class) 
	public List getValorCampoComplementars() {
		return this.valorCampoComplementars;
	}

	public void setValorCampoComplementars(List valorCampoComplementars) {
		this.valorCampoComplementars = valorCampoComplementars;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ReciboDesconto.class) 
	public List getReciboDescontos() {
		return this.reciboDescontos;
	}

	public void setReciboDescontos(List reciboDescontos) {
		this.reciboDescontos = reciboDescontos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.DetalheColeta.class) 
	public List getDetalheColetas() {
		return this.detalheColetas;
	}

	public void setDetalheColetas(List detalheColetas) {
		this.detalheColetas = detalheColetas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PrazoEntregaCliente.class) 
	public List getPrazoEntregaClientes() {
		return this.prazoEntregaClientes;
	}

	public void setPrazoEntregaClientes(List prazoEntregaClientes) {
		this.prazoEntregaClientes = prazoEntregaClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Cheque.class) 
	public List getCheques() {
		return this.cheques;
	}

	public void setCheques(List cheques) {
		this.cheques = cheques;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.MilkRemetente.class) 
	public List getMilkRemetentes() {
		return this.milkRemetentes;
	}

	public void setMilkRemetentes(List milkRemetentes) {
		this.milkRemetentes = milkRemetentes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.ConfiguracaoComunicacao.class) 
	public List getConfiguracaoComunicacoes() {
		return this.configuracaoComunicacoes;
	}

	public void setConfiguracaoComunicacoes(List configuracaoComunicacoes) {
		this.configuracaoComunicacoes = configuracaoComunicacoes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.tributos.model.AliquotaContribuicaoServ.class) 
	public List getAliquotaContribuicaoServs() {
		return this.aliquotaContribuicaoServs;
	}

	public void setAliquotaContribuicaoServs(List aliquotaContribuicaoServs) {
		this.aliquotaContribuicaoServs = aliquotaContribuicaoServs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.HistoricoPce.class) 
	public List getHistoricoPces() {
		return this.historicoPces;
	}

	public void setHistoricoPces(List historicoPces) {
		this.historicoPces = historicoPces;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.SeguroCliente.class) 
	public List getSeguroClientes() {
		return this.seguroClientes;
	}

	public void setSeguroClientes(List seguroClientes) {
		this.seguroClientes = seguroClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ColetaAutomaticaCliente.class) 
	public List getColetaAutomaticaClientes() {
		return this.coletaAutomaticaClientes;
	}

	public void setColetaAutomaticaClientes(List coletaAutomaticaClientes) {
		this.coletaAutomaticaClientes = coletaAutomaticaClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoInternacional.class) 
	public List getCtoInternacionais() {
		return this.ctoInternacionais;
	}

	public void setCtoInternacionais(List ctoInternacionais) {
		this.ctoInternacionais = ctoInternacionais;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.DevedorDocServ.class) 
	public List getDevedorDocServs() {
		return this.devedorDocServs;
	}

	public void setDevedorDocServs(List devedorDocServs) {
		this.devedorDocServs = devedorDocServs;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.Manifesto.class) 
	public List getManifestos() {
		return this.manifestos;
	}

	public void setManifestos(List manifestos) {
		this.manifestos = manifestos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ProibidoEmbarque.class) 
	public List getProibidoEmbarques() {
		return this.proibidoEmbarques;
	}

	public void setProibidoEmbarques(List proibidoEmbarques) {
		this.proibidoEmbarques = proibidoEmbarques;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class) 
	public List getCtoCtoCooperadasByIdDestinatario() {
		return this.ctoCtoCooperadasByIdDestinatario;
	}

	public void setCtoCtoCooperadasByIdDestinatario(
			List ctoCtoCooperadasByIdDestinatario) {
		this.ctoCtoCooperadasByIdDestinatario = ctoCtoCooperadasByIdDestinatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class) 
	public List getCtoCtoCooperadasByIdRedespacho() {
		return this.ctoCtoCooperadasByIdRedespacho;
	}

	public void setCtoCtoCooperadasByIdRedespacho(
			List ctoCtoCooperadasByIdRedespacho) {
		this.ctoCtoCooperadasByIdRedespacho = ctoCtoCooperadasByIdRedespacho;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class) 
	public List getCtoCtoCooperadasByIdDevedor() {
		return this.ctoCtoCooperadasByIdDevedor;
	}

	public void setCtoCtoCooperadasByIdDevedor(List ctoCtoCooperadasByIdDevedor) {
		this.ctoCtoCooperadasByIdDevedor = ctoCtoCooperadasByIdDevedor;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class) 
	public List getCtoCtoCooperadasByIdConsignatario() {
		return this.ctoCtoCooperadasByIdConsignatario;
	}

	public void setCtoCtoCooperadasByIdConsignatario(
			List ctoCtoCooperadasByIdConsignatario) {
		this.ctoCtoCooperadasByIdConsignatario = ctoCtoCooperadasByIdConsignatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class) 
	public List getCtoCtoCooperadasByIdRemetente() {
		return this.ctoCtoCooperadasByIdRemetente;
	}

	public void setCtoCtoCooperadasByIdRemetente(
			List ctoCtoCooperadasByIdRemetente) {
		this.ctoCtoCooperadasByIdRemetente = ctoCtoCooperadasByIdRemetente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.FormaAgrupamento.class) 
	public List getFormaAgrupamentos() {
		return this.formaAgrupamentos;
	}

	public void setFormaAgrupamentos(List formaAgrupamentos) {
		this.formaAgrupamentos = formaAgrupamentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.DevedorDocServFat.class) 
	public List getDevedorDocServFats() {
		return this.devedorDocServFats;
	}

	public void setDevedorDocServFats(List devedorDocServFats) {
		this.devedorDocServFats = devedorDocServFats;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class) 
	public List getFaturas() {
		return this.faturas;
	}

	public void setFaturas(List faturas) {
		this.faturas = faturas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ProtocoloTransferencia.class) 
	public List getProtocoloTransferencias() {
		return this.protocoloTransferencias;
	}

	public void setProtocoloTransferencias(List protocoloTransferencias) {
		this.protocoloTransferencias = protocoloTransferencias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PromotorCliente.class) 
	public List getPromotorClientes() {
		return this.promotorClientes;
	}

	public void setPromotorClientes(List promotorClientes) {
		this.promotorClientes = promotorClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.NotaDebitoNacional.class) 
	public List getNotaDebitoNacionais() {
		return this.notaDebitoNacionais;
	}

	public void setNotaDebitoNacionais(List notaDebitoNacionais) {
		this.notaDebitoNacionais = notaDebitoNacionais;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.PotencialComercialCliente.class) 
	public List getPotencialComercialClientes() {
		return this.potencialComercialClientes;
	}

	public void setPotencialComercialClientes(List potencialComercialClientes) {
		this.potencialComercialClientes = potencialComercialClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.HistTrocaFilialCliente.class) 
	public List getHistTrocaFilialClientes() {
		return this.histTrocaFilialClientes;
	}

	public void setHistTrocaFilialClientes(List histTrocaFilialClientes) {
		this.histTrocaFilialClientes = histTrocaFilialClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.LotePendencia.class) 
	public List getLotePendencias() {
		return this.lotePendencias;
	}

	public void setLotePendencias(List lotePendencias) {
		this.lotePendencias = lotePendencias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialCliOrigem.class) 
	public List getMunicipioFilialCliOrigems() {
		return this.municipioFilialCliOrigems;
	}

	public void setMunicipioFilialCliOrigems(List municipioFilialCliOrigems) {
		this.municipioFilialCliOrigems = municipioFilialCliOrigems;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sgr.model.ClienteEnquadramento.class) 
	public List getClienteEnquadramentos() {
		return this.clienteEnquadramentos;
	}

	public void setClienteEnquadramentos(List clienteEnquadramentos) {
		this.clienteEnquadramentos = clienteEnquadramentos;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ClassificacaoCliente.class) 
	public List getClassificacaoClientes() {
		return this.classificacaoClientes;
	}

	public void setClassificacaoClientes(List classificacaoClientes) {
		this.classificacaoClientes = classificacaoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Simulacao.class) 
	public List getSimulacoesByIdCliente() {
		return this.simulacoesByIdCliente;
	}

	public void setSimulacoesByIdCliente(List simulacoesByIdCliente) {
		this.simulacoesByIdCliente = simulacoesByIdCliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Simulacao.class) 
	public List getSimulacoesByIdClienteBase() {
		return this.simulacoesByIdClienteBase;
	}

	public void setSimulacoesByIdClienteBase(List simulacoesByIdClienteBase) {
		this.simulacoesByIdClienteBase = simulacoesByIdClienteBase;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.PostoAvancado.class) 
	public List getPostoAvancados() {
		return this.postoAvancados;
	}

	public void setPostoAvancados(List postoAvancados) {
		this.postoAvancados = postoAvancados;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.DepositoCcorrente.class) 
	public List getDepositoCcorrentes() {
		return this.depositoCcorrentes;
	}

	public void setDepositoCcorrentes(List depositoCcorrentes) {
		this.depositoCcorrentes = depositoCcorrentes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.CobrancaInadimplencia.class) 
	public List getCobrancaInadimplencias() {
		return this.cobrancaInadimplencias;
	}

	public void setCobrancaInadimplencias(List cobrancaInadimplencias) {
		this.cobrancaInadimplencias = cobrancaInadimplencias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.OcorrenciaCliente.class) 
	public List getOcorrenciaClientes() {
		return this.ocorrenciaClientes;
	}

	public void setOcorrenciaClientes(List ocorrenciaClientes) {
		this.ocorrenciaClientes = ocorrenciaClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.SolicitacaoRetirada.class) 
	public List getSolicitacaoRetiradasByIdClienteRemetente() {
		return this.solicitacaoRetiradasByIdClienteRemetente;
	}

	public void setSolicitacaoRetiradasByIdClienteRemetente(
			List solicitacaoRetiradasByIdClienteRemetente) {
		this.solicitacaoRetiradasByIdClienteRemetente = solicitacaoRetiradasByIdClienteRemetente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.SolicitacaoRetirada.class) 
	public List getSolicitacaoRetiradasByIdClienteDestinatario() {
		return this.solicitacaoRetiradasByIdClienteDestinatario;
	}

	public void setSolicitacaoRetiradasByIdClienteDestinatario(
			List solicitacaoRetiradasByIdClienteDestinatario) {
		this.solicitacaoRetiradasByIdClienteDestinatario = solicitacaoRetiradasByIdClienteDestinatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.VersaoPce.class) 
	public List getVersaoPces() {
		return this.versaoPces;
	}

	public void setVersaoPces(List versaoPces) {
		this.versaoPces = versaoPces;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.EventoCliente.class) 
	public List getEventoClientes() {
		return this.eventoClientes;
	}

	public void setEventoClientes(List eventoClientes) {
		this.eventoClientes = eventoClientes;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.MilkRun.class) 
	public List getMilkRuns() {
		return this.milkRuns;
	}

	public void setMilkRuns(List milkRuns) {
		this.milkRuns = milkRuns;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.PedidoCompra.class) 
	public List getPedidoComprasByIdClienteRemetente() {
		return this.pedidoComprasByIdClienteRemetente;
	}

	public void setPedidoComprasByIdClienteRemetente(
			List pedidoComprasByIdClienteRemetente) {
		this.pedidoComprasByIdClienteRemetente = pedidoComprasByIdClienteRemetente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.sim.model.PedidoCompra.class) 
	public List getPedidoComprasByIdClienteDestinatario() {
		return this.pedidoComprasByIdClienteDestinatario;
	}

	public void setPedidoComprasByIdClienteDestinatario(
			List pedidoComprasByIdClienteDestinatario) {
		this.pedidoComprasByIdClienteDestinatario = pedidoComprasByIdClienteDestinatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.LiberacaoEmbarque.class) 
	public List getLiberacaoEmbarques() {
		return this.liberacaoEmbarques;
	}

	public void setLiberacaoEmbarques(List liberacaoEmbarques) {
		this.liberacaoEmbarques = liberacaoEmbarques;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.AgendaTransferencia.class) 
	public List getAgendaTransferencias() {
		return this.agendaTransferencias;
	}

	public void setAgendaTransferencias(List agendaTransferencias) {
		this.agendaTransferencias = agendaTransferencias;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Visita.class) 
	public List getVisitas() {
		return this.visitas;
	}

	public void setVisitas(List visitas) {
		this.visitas = visitas;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Awb.class) 
	public List getAwbsByIdClienteExpedidor() {
		return this.awbsByIdClienteExpedidor;
	}

	public void setAwbsByIdClienteExpedidor(List awbsByIdClienteExpedidor) {
		this.awbsByIdClienteExpedidor = awbsByIdClienteExpedidor;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.Awb.class) 
	public List getAwbsByIdClienteDestinatario() {
		return this.awbsByIdClienteDestinatario;
	}

	public void setAwbsByIdClienteDestinatario(List awbsByIdClienteDestinatario) {
		this.awbsByIdClienteDestinatario = awbsByIdClienteDestinatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cotacao.class) 
	public List getCotacoesByIdCliente() {
		return this.cotacoesByIdCliente;
	}

	public void setCotacoesByIdCliente(List cotacoesByIdCliente) {
		this.cotacoesByIdCliente = cotacoesByIdCliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cotacao.class) 
	public List getCotacoesByIdClienteSolicitou() {
		return this.cotacoesByIdClienteSolicitou;
	}

	public void setCotacoesByIdClienteSolicitou(
			List cotacoesByIdClienteSolicitou) {
		this.cotacoesByIdClienteSolicitou = cotacoesByIdClienteSolicitou;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaPendenciaMz.class) 
	public List getMercadoriaPendenciaMzs() {
		return this.mercadoriaPendenciaMzs;
	}

	public void setMercadoriaPendenciaMzs(List mercadoriaPendenciaMzs) {
		this.mercadoriaPendenciaMzs = mercadoriaPendenciaMzs;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("idCliente", getIdCliente())
			.toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Cliente))
			return false;
		Cliente castOther = (Cliente) other;
		return new EqualsBuilder().append(this.getIdCliente(),
				castOther.getIdCliente()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdCliente()).toHashCode();
	}

	public List getClientes() {
		return clientes;
	}

	public void setClientes(List clientes) {
		this.clientes = clientes;
	}

	public Boolean getBlAgendamentoPessoaFisica() {
		return blAgendamentoPessoaFisica;
	}

	public void setBlAgendamentoPessoaFisica(Boolean blAgendamentoPessoaFisica) {
		this.blAgendamentoPessoaFisica = blAgendamentoPessoaFisica;
	}

	public Boolean getBlAgendamentoPessoaJuridica() {
		return blAgendamentoPessoaJuridica;
	}

	public void setBlAgendamentoPessoaJuridica(
			Boolean blAgendamentoPessoaJuridica) {
		this.blAgendamentoPessoaJuridica = blAgendamentoPessoaJuridica;
	}

	public Boolean getBlAgrupaFaturamentoMes() {
		return blAgrupaFaturamentoMes;
	}

	public void setBlAgrupaFaturamentoMes(Boolean blAgrupaFaturamentoMes) {
		this.blAgrupaFaturamentoMes = blAgrupaFaturamentoMes;
	}

	public Boolean getBlAgrupaNotas() {
		return blAgrupaNotas;
	}

	public void setBlAgrupaNotas(Boolean blAgrupaNotas) {
		this.blAgrupaNotas = blAgrupaNotas;
	}

	public Boolean getBlBaseCalculo() {
		return blBaseCalculo;
	}

	public void setBlBaseCalculo(Boolean blBaseCalculo) {
		this.blBaseCalculo = blBaseCalculo;
	}

	public Boolean getBlCadastradoColeta() {
		return blCadastradoColeta;
	}

	public void setBlCadastradoColeta(Boolean blCadastradoColeta) {
		this.blCadastradoColeta = blCadastradoColeta;
	}

	public Boolean getBlCobraDevolucao() {
		return blCobraDevolucao;
	}

	public void setBlCobraDevolucao(Boolean blCobraDevolucao) {
		this.blCobraDevolucao = blCobraDevolucao;
	}

	public Boolean getBlCobrancaCentralizada() {
		return blCobrancaCentralizada;
	}

	public void setBlCobrancaCentralizada(Boolean blCobrancaCentralizada) {
		this.blCobrancaCentralizada = blCobrancaCentralizada;
	}

	public Boolean getBlCobraReentrega() {
		return blCobraReentrega;
	}

	public void setBlCobraReentrega(Boolean blCobraReentrega) {
		this.blCobraReentrega = blCobraReentrega;
	}

	public Boolean getBlPesagemOpcional() {
		return blPesagemOpcional;
	}

	public void setBlPesagemOpcional(Boolean blPesagemOpcional) {
		this.blPesagemOpcional = blPesagemOpcional;
	}
	
	public Boolean getBlColetaAutomatica() {
		return blColetaAutomatica;
	}

	public void setBlColetaAutomatica(Boolean blColetaAutomatica) {
		this.blColetaAutomatica = blColetaAutomatica;
	}

	public Boolean getBlEmiteBoletoCliDestino() {
		return blEmiteBoletoCliDestino;
	}

	public void setBlEmiteBoletoCliDestino(Boolean blEmiteBoletoCliDestino) {
		this.blEmiteBoletoCliDestino = blEmiteBoletoCliDestino;
	}

	public Boolean getBlFaturaDocsConferidos() {
		return blFaturaDocsConferidos;
	}

	public void setBlFaturaDocsConferidos(Boolean blFaturaDocsConferidos) {
		this.blFaturaDocsConferidos = blFaturaDocsConferidos;
	}

	public Boolean getBlFaturaDocsEntregues() {
		return blFaturaDocsEntregues;
	}

	public void setBlFaturaDocsEntregues(Boolean blFaturaDocsEntregues) {
		this.blFaturaDocsEntregues = blFaturaDocsEntregues;
	}

	public Boolean getBlFobDirigido() {
		return blFobDirigido;
	}

	public Boolean getBlFobDirigidoAereo() {
		return blFobDirigidoAereo;
	}

	public void setBlFobDirigidoAereo(Boolean blFobDirigidoAereo) {
		this.blFobDirigidoAereo = blFobDirigidoAereo;
	}

	public void setBlFobDirigido(Boolean blFobDirigido) {
		this.blFobDirigido = blFobDirigido;
	}

	public Boolean getBlFronteiraRapida() {
		return blFronteiraRapida;
	}

	public void setBlFronteiraRapida(Boolean blFronteiraRapida) {
		this.blFronteiraRapida = blFronteiraRapida;
	}

	public Boolean getBlGeraReciboFreteEntrega() {
		return blGeraReciboFreteEntrega;
	}

	public void setBlGeraReciboFreteEntrega(Boolean blGeraReciboFreteEntrega) {
		this.blGeraReciboFreteEntrega = blGeraReciboFreteEntrega;
	}

	public Boolean getBlIcmsPedagio() {
		return blIcmsPedagio;
	}

	public void setBlIcmsPedagio(Boolean blIcmsPedagio) {
		this.blIcmsPedagio = blIcmsPedagio;
	}

	public Boolean getBlIndicadorProtesto() {
		return blIndicadorProtesto;
	}

	public void setBlIndicadorProtesto(Boolean blIndicadorProtesto) {
		this.blIndicadorProtesto = blIndicadorProtesto;
	}

	public Boolean getBlObrigaRecebedor() {
		return blObrigaRecebedor;
	}

	public void setBlObrigaRecebedor(Boolean blObrigaRecebedor) {
		this.blObrigaRecebedor = blObrigaRecebedor;
	}

	public Boolean getBlOperadorLogistico() {
		return blOperadorLogistico;
	}

	public void setBlOperadorLogistico(Boolean blOperadorLogistico) {
		this.blOperadorLogistico = blOperadorLogistico;
	}

	public Boolean getBlAceitaFobGeral() {
		return blAceitaFobGeral;
	}

	public void setBlAceitaFobGeral(Boolean blAceitaFobGeral) {
		this.blAceitaFobGeral = blAceitaFobGeral;
	}

	public Boolean getBlPermanente() {
		return blPermanente;
	}

	public void setBlPermanente(Boolean blPermanente) {
		this.blPermanente = blPermanente;
	}

	public Boolean getBlPesoAforadoPedagio() {
		return blPesoAforadoPedagio;
	}

	public void setBlPesoAforadoPedagio(Boolean blPesoAforadoPedagio) {
		this.blPesoAforadoPedagio = blPesoAforadoPedagio;
	}

	public Boolean getBlPreFatura() {
		return blPreFatura;
	}

	public void setBlPreFatura(Boolean blPreFatura) {
		this.blPreFatura = blPreFatura;
	}

	public Boolean getBlResponsavelFrete() {
		return blResponsavelFrete;
	}

	public void setBlResponsavelFrete(Boolean blResponsavelFrete) {
		this.blResponsavelFrete = blResponsavelFrete;
	}

	public Boolean getBlRessarceFreteFob() {
		return blRessarceFreteFob;
	}

	public void setBlRessarceFreteFob(Boolean blRessarceFreteFob) {
		this.blRessarceFreteFob = blRessarceFreteFob;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Filial getFilialByIdFilialAtendeComercial() {
		return filialByIdFilialAtendeComercial;
	}

	public void setFilialByIdFilialAtendeComercial(
			Filial filialByIdFilialAtendeComercial) {
		this.filialByIdFilialAtendeComercial = filialByIdFilialAtendeComercial;
	}

	public Filial getFilialByIdFilialAtendeOperacional() {
		return filialByIdFilialAtendeOperacional;
	}

	public void setFilialByIdFilialAtendeOperacional(
			Filial filialByIdFilialAtendeOperacional) {
		this.filialByIdFilialAtendeOperacional = filialByIdFilialAtendeOperacional;
	}

	public Filial getFilialByIdFilialCobranca() {
		return filialByIdFilialCobranca;
	}

	public void setFilialByIdFilialCobranca(Filial filialByIdFilialCobranca) {
		this.filialByIdFilialCobranca = filialByIdFilialCobranca;
	}

	public Moeda getMoedaByIdMoedaFatPrev() {
		return moedaByIdMoedaFatPrev;
	}

	public void setMoedaByIdMoedaFatPrev(Moeda moedaByIdMoedaFatPrev) {
		this.moedaByIdMoedaFatPrev = moedaByIdMoedaFatPrev;
	}

	public Moeda getMoedaByIdMoedaLimCred() {
		return moedaByIdMoedaLimCred;
	}

	public void setMoedaByIdMoedaLimCred(Moeda moedaByIdMoedaLimCred) {
		this.moedaByIdMoedaLimCred = moedaByIdMoedaLimCred;
	}

	public Moeda getMoedaByIdMoedaLimDoctos() {
		return moedaByIdMoedaLimDoctos;
	}

	public void setMoedaByIdMoedaLimDoctos(Moeda moedaByIdMoedaLimDoctos) {
		this.moedaByIdMoedaLimDoctos = moedaByIdMoedaLimDoctos;
	}

	public Moeda getMoedaByIdMoedaSaldoAtual() {
		return moedaByIdMoedaSaldoAtual;
	}

	public void setMoedaByIdMoedaSaldoAtual(Moeda moedaByIdMoedaSaldoAtual) {
		this.moedaByIdMoedaSaldoAtual = moedaByIdMoedaSaldoAtual;
	}

	public Short getNrCasasDecimaisPeso() {
		return nrCasasDecimaisPeso;
	}

	public void setNrCasasDecimaisPeso(Short nrCasasDecimaisPeso) {
		this.nrCasasDecimaisPeso = nrCasasDecimaisPeso;
	}

	public Short getNrDiasLimiteDebito() {
		return nrDiasLimiteDebito;
	}

	public void setNrDiasLimiteDebito(Short nrDiasLimiteDebito) {
		this.nrDiasLimiteDebito = nrDiasLimiteDebito;
	}

	public BigDecimal getPcDescontoFreteCif() {
		return pcDescontoFreteCif;
	}

	public void setPcDescontoFreteCif(BigDecimal pcDescontoFreteCif) {
		this.pcDescontoFreteCif = pcDescontoFreteCif;
	}

	public BigDecimal getPcDescontoFreteFob() {
		return pcDescontoFreteFob;
	}

	public void setPcDescontoFreteFob(BigDecimal pcDescontoFreteFob) {
		this.pcDescontoFreteFob = pcDescontoFreteFob;
	}

	public BigDecimal getPcJuroDiario() {
		return pcJuroDiario;
	}

	public void setPcJuroDiario(BigDecimal pcJuroDiario) {
		this.pcJuroDiario = pcJuroDiario;
	}

	public Regional getRegionalComercial() {
		return regionalComercial;
	}

	public void setRegionalComercial(Regional regionalComercial) {
		this.regionalComercial = regionalComercial;
	}

	public Regional getRegionalFinanceiro() {
		return regionalFinanceiro;
	}

	public void setRegionalFinanceiro(Regional regionalFinanceiro) {
		this.regionalFinanceiro = regionalFinanceiro;
	}

	public Boolean getBlFaturaDocReferencia() {
		return blFaturaDocReferencia;
	}

	public void setBlFaturaDocReferencia(Boolean blFaturaDocReferencia) {
		this.blFaturaDocReferencia = blFaturaDocReferencia;
	}

	public Regional getRegionalOperacional() {
		return regionalOperacional;
	}

	public void setRegionalOperacional(Regional regionalOperacional) {
		this.regionalOperacional = regionalOperacional;
	}

	public DomainValue getTpCobranca() {
		return tpCobranca;
	}

	public void setTpCobranca(DomainValue tpCobranca) {
		this.tpCobranca = tpCobranca;
	}

	public DomainValue getTpCobrancaSolicitado() {
		return tpCobrancaSolicitado;
	}

	public void setTpCobrancaSolicitado(DomainValue tpCobrancaSolicitado) {
		this.tpCobrancaSolicitado = tpCobrancaSolicitado;
	}

	public DomainValue getTpCobrancaAprovado() {
		return tpCobrancaAprovado;
	}

	public void setTpCobrancaAprovado(DomainValue tpCobrancaAprovado) {
		this.tpCobrancaAprovado = tpCobrancaAprovado;
	}

	public DomainValue getTpDificuldadeClassificacao() {
		return tpDificuldadeClassificacao;
	}

	public void setTpDificuldadeClassificacao(
			DomainValue tpDificuldadeClassificacao) {
		this.tpDificuldadeClassificacao = tpDificuldadeClassificacao;
	}

	public DomainValue getTpDificuldadeColeta() {
		return tpDificuldadeColeta;
	}

	public void setTpDificuldadeColeta(DomainValue tpDificuldadeColeta) {
		this.tpDificuldadeColeta = tpDificuldadeColeta;
	}

	public DomainValue getTpDificuldadeEntrega() {
		return tpDificuldadeEntrega;
	}

	public void setTpDificuldadeEntrega(DomainValue tpDificuldadeEntrega) {
		this.tpDificuldadeEntrega = tpDificuldadeEntrega;
	}

	public DomainValue getTpFormaArredondamento() {
		return tpFormaArredondamento;
	}

	public void setTpFormaArredondamento(DomainValue tpFormaArredondamento) {
		this.tpFormaArredondamento = tpFormaArredondamento;
	}

	public DomainValue getTpFrequenciaVisita() {
		return tpFrequenciaVisita;
	}

	public void setTpFrequenciaVisita(DomainValue tpFrequenciaVisita) {
		this.tpFrequenciaVisita = tpFrequenciaVisita;
	}

	public DomainValue getTpLocalEmissaoConReent() {
		return tpLocalEmissaoConReent;
	}

	public void setTpLocalEmissaoConReent(DomainValue tpLocalEmissaoConReent) {
		this.tpLocalEmissaoConReent = tpLocalEmissaoConReent;
	}

	public DomainValue getTpMeioEnvioBoleto() {
		return tpMeioEnvioBoleto;
	}

	public void setTpMeioEnvioBoleto(DomainValue tpMeioEnvioBoleto) {
		this.tpMeioEnvioBoleto = tpMeioEnvioBoleto;
	}

	public DomainValue getTpPeriodicidadeTransf() {
		return tpPeriodicidadeTransf;
	}

	public void setTpPeriodicidadeTransf(DomainValue tpPeriodicidadeTransf) {
		this.tpPeriodicidadeTransf = tpPeriodicidadeTransf;
	}

	public Boolean getBlNumeroVolumeEDI() {
		return blNumeroVolumeEDI;
	}

	public void setBlNumeroVolumeEDI(Boolean blNumeroVolumeEDI) {
		this.blNumeroVolumeEDI = blNumeroVolumeEDI;
	}

	public DomainValue getTpAgrupamentoEDI() {
		return tpAgrupamentoEDI;
	}

	public void setTpAgrupamentoEDI(DomainValue tpAgrupamentoEDI) {
		this.tpAgrupamentoEDI = tpAgrupamentoEDI;
	}

	public DomainValue getTpOrdemEmissaoEDI() {
		return tpOrdemEmissaoEDI;
	}

	public void setTpOrdemEmissaoEDI(DomainValue tpOrdemEmissaoEDI) {
		this.tpOrdemEmissaoEDI = tpOrdemEmissaoEDI;
	}

	public InformacaoDoctoCliente getInformacaoDoctoCliente() {
		return informacaoDoctoCliente;
	}

	public void setInformacaoDoctoCliente(
			InformacaoDoctoCliente informacaoDoctoCliente) {
		this.informacaoDoctoCliente = informacaoDoctoCliente;
	}

	public BigDecimal getVlFaturamentoPrevisto() {
		return vlFaturamentoPrevisto;
	}

	public void setVlFaturamentoPrevisto(BigDecimal vlFaturamentoPrevisto) {
		this.vlFaturamentoPrevisto = vlFaturamentoPrevisto;
	}

	public BigDecimal getVlLimiteCredito() {
		return vlLimiteCredito;
	}

	public void setVlLimiteCredito(BigDecimal vlLimiteCredito) {
		this.vlLimiteCredito = vlLimiteCredito;
	}

	public BigDecimal getVlLimiteDocumentos() {
		return vlLimiteDocumentos;
	}

	public void setVlLimiteDocumentos(BigDecimal vlLimiteDocumentos) {
		this.vlLimiteDocumentos = vlLimiteDocumentos;
	}

	public BigDecimal getVlSaldoAtual() {
		return vlSaldoAtual;
	}

	public void setVlSaldoAtual(BigDecimal vlSaldoAtual) {
		this.vlSaldoAtual = vlSaldoAtual;
	}
	
	@ParametrizedAttribute(type = com.mercurio.lms.rnc.model.NaoConformidade.class) 
	public List getNaoConformidadesByIdClienteDestinatario() {
		return naoConformidadesByIdClienteDestinatario;
	}

	public void setNaoConformidadesByIdClienteDestinatario(
			List naoConformidadesByIdClienteDestinatario) {
		this.naoConformidadesByIdClienteDestinatario = naoConformidadesByIdClienteDestinatario;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.rnc.model.NaoConformidade.class) 
	public List getNaoConformidadesByIdClienteRemetente() {
		return naoConformidadesByIdClienteRemetente;
	}

	public void setNaoConformidadesByIdClienteRemetente(
			List naoConformidadesByIdClienteRemetente) {
		this.naoConformidadesByIdClienteRemetente = naoConformidadesByIdClienteRemetente;
	}

	public Cedente getCedente() {
		return cedente;
	}

	public void setCedente(Cedente cedente) {
		this.cedente = cedente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.ClienteUsuario.class) 
	public List getUsuariosCliente() {
		return usuariosCliente;
	}

	public void setUsuariosCliente(List usuariosCliente) {
		this.usuariosCliente = usuariosCliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.configuracoes.model.Usuario.class) 
	public List getUsuariosPadraoCliente() {
		return usuariosPadraoCliente;
	}

	public void setUsuariosPadraoCliente(List usuariosPadraoCliente) {
		this.usuariosPadraoCliente = usuariosPadraoCliente;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.municipios.model.RotaViagem.class) 
	public List getRotaViagems() {
		return rotaViagems;
	}

	public void setRotaViagems(List rotaViagems) {
		this.rotaViagems = rotaViagems;
	}

	public ObservacaoConhecimento getObservacaoConhecimento() {
		return observacaoConhecimento;
	}

	public void setObservacaoConhecimento(
			ObservacaoConhecimento observacaoConhecimento) {
		this.observacaoConhecimento = observacaoConhecimento;
	}

	public Long getNrReentregasCobranca() {
		return nrReentregasCobranca;
	}

	public void setNrReentregasCobranca(Long nrReentregasCobranca) {
		this.nrReentregasCobranca = nrReentregasCobranca;
	}

	public DomainValue getTpPesoCalculo() {
		return tpPesoCalculo;
	}

	public void setTpPesoCalculo(DomainValue tpPesoCalculo) {
		this.tpPesoCalculo = tpPesoCalculo;
	}

	public Long getNrInscricaoSuframa() {
		return nrInscricaoSuframa;
	}

	public void setNrInscricaoSuframa(Long nrInscricaoSuframa) {
		this.nrInscricaoSuframa = nrInscricaoSuframa;
	}

	public Boolean getBlPermiteCte() {
		return blPermiteCte;
	}

	public void setBlPermiteCte(Boolean blPermiteCte) {
		this.blPermiteCte = blPermiteCte;
	}

	public Boolean getBlSeparaFaturaModal() {
		return blSeparaFaturaModal;
	}

	public void setBlSeparaFaturaModal(Boolean blSeparaFaturaModal) {
		this.blSeparaFaturaModal = blSeparaFaturaModal;
	}

	public Boolean getBlObrigaSerie() {
		return blObrigaSerie;
	}

	public void setBlObrigaSerie(Boolean blObrigaSerie) {
		this.blObrigaSerie = blObrigaSerie;
	}

	public Boolean getBlUtilizaPesoEDI() {
		return blUtilizaPesoEDI;
	}

	public void setBlUtilizaPesoEDI(Boolean blUtilizaPesoEDI) {
		this.blUtilizaPesoEDI = blUtilizaPesoEDI;
	}

	public Boolean getBlUtilizaFreteEDI() {
		return blUtilizaFreteEDI;
	}

	public void setBlUtilizaFreteEDI(Boolean blUtilizaFreteEDI) {
		this.blUtilizaFreteEDI = blUtilizaFreteEDI;
	}
	
	public Boolean getBlNaoAtualizaDBI() {
		return blNaoAtualizaDBI;
	}

	public void setBlNaoAtualizaDBI(Boolean blNaoAtualizaDBI) {
		this.blNaoAtualizaDBI = blNaoAtualizaDBI;
	}

	public void setTpFormaCobranca(DomainValue tpFormaCobranca) {
		this.tpFormaCobranca = tpFormaCobranca;
	}

	public DomainValue getTpFormaCobranca() {
		return tpFormaCobranca;
	}

	public void setDefaultTpFormaCobranca() {
		this.tpFormaCobranca = tpFormaCobranca == null ? new DomainValue("A")
				: tpFormaCobranca;
	}
	
	public void setBlAtualizaDestinatarioEdi(Boolean blAtualizaDestinatarioEdi) {
		this.blAtualizaDestinatarioEdi = blAtualizaDestinatarioEdi;
	}

	public Boolean getBlAtualizaDestinatarioEdi() {
		return blAtualizaDestinatarioEdi;
	}
	
	public Boolean getBlAtualizaConsignatarioEdi() {
		return blAtualizaConsignatarioEdi;
	}

	public void setBlAtualizaConsignatarioEdi(Boolean blAtualizaConsignatarioEdi) {
		this.blAtualizaConsignatarioEdi = blAtualizaConsignatarioEdi;
	}

	public Boolean getBlLiberaEtiquetaEdi() {
		return blLiberaEtiquetaEdi;
	}

	public void setBlLiberaEtiquetaEdi(Boolean blLiberaEtiquetaEdi) {
		this.blLiberaEtiquetaEdi = blLiberaEtiquetaEdi;
	}

	public Boolean getBlAtualizaIEDestinatarioEdi() {
		return blAtualizaIEDestinatarioEdi;
	}

	public void setBlAtualizaIEDestinatarioEdi(
			Boolean blAtualizaIEDestinatarioEdi) {
		this.blAtualizaIEDestinatarioEdi = blAtualizaIEDestinatarioEdi;
	}
	
	public DivisaoCliente getDivisaoClienteResponsavel() {
		return divisaoClienteResponsavel;
	}

	public void setDivisaoClienteResponsavel(DivisaoCliente divisaoClienteResponsavel) {
		this.divisaoClienteResponsavel = divisaoClienteResponsavel;
	}

	public InformacaoDoctoCliente getInformacaoDoctoClienteEDI() {
		return informacaoDoctoClienteEDI;
	}

	public void setInformacaoDoctoClienteEDI(
			InformacaoDoctoCliente informacaoDoctoClienteEDI) {
		this.informacaoDoctoClienteEDI = informacaoDoctoClienteEDI;
	}

	public Boolean getBlVeiculoDedicado() {
		return blVeiculoDedicado;
	}

	public void setBlVeiculoDedicado(Boolean blVeiculoDedicado) {
		this.blVeiculoDedicado = blVeiculoDedicado;
	}

	public Boolean getBlAgendamentoEntrega() {
		return blAgendamentoEntrega;
	}

	public void setBlAgendamentoEntrega(Boolean blAgendamentoEntrega) {
		this.blAgendamentoEntrega = blAgendamentoEntrega;
	}

	public Boolean getBlPaletizacao() {
		return blPaletizacao;
	}

	public void setBlPaletizacao(Boolean blPaletizacao) {
		this.blPaletizacao = blPaletizacao;
	}

	public Boolean getBlCustoDescarga() {
		return blCustoDescarga;
	}

	public void setBlCustoDescarga(Boolean blCustoDescarga) {
		this.blCustoDescarga = blCustoDescarga;
	}

	public Boolean getBlCalculoArqPreFatura() {
		return blCalculoArqPreFatura;
	}

	public void setBlCalculoArqPreFatura(Boolean blCalculoArqPreFatura) {
		this.blCalculoArqPreFatura = blCalculoArqPreFatura;
	}
	
	public Boolean getBlEmiteDacteFaturamento() {
		return blEmiteDacteFaturamento;
	}

	public void setBlEmiteDacteFaturamento(Boolean blEmiteDacteFaturamento) {
		this.blEmiteDacteFaturamento = blEmiteDacteFaturamento;
	}

	public Boolean getBlAgendamento() {
		return blAgendamento;
	}

	public void setBlAgendamento(Boolean blAgendamento) {
		this.blAgendamento = blAgendamento;
	}

	public Boolean getBlConfAgendamento() {
		return blConfAgendamento;
	}

	public void setBlConfAgendamento(Boolean blConfAgendamento) {
		this.blConfAgendamento = blConfAgendamento;
	}

	public Boolean getBlRecolheICMS() {
		return blRecolheICMS;
	}

	public void setBlRecolheICMS(Boolean blRecolheICMS) {
		this.blRecolheICMS = blRecolheICMS;
	}

	public Boolean getBlPaleteFechado() {
		return blPaleteFechado;
	}

	public void setBlPaleteFechado(Boolean blPaleteFechado) {
		this.blPaleteFechado = blPaleteFechado;
	}
	

	public Boolean getBlSemChaveNfeEdi() {
		return blSemChaveNfeEdi;
	}

	public void setBlSemChaveNfeEdi(Boolean blSemChaveNfeEdi) {
		this.blSemChaveNfeEdi = blSemChaveNfeEdi;
	}

	public Boolean getBlEtiquetaPorVolume() {
		return blEtiquetaPorVolume;
	}

	public void setBlEtiquetaPorVolume(Boolean blEtiquetaPorVolume) {
		this.blEtiquetaPorVolume = blEtiquetaPorVolume;
	}

	public Boolean getBlSeguroDiferenciadoAereo() {
		return blSeguroDiferenciadoAereo;
	}

	public void setBlSeguroDiferenciadoAereo(Boolean blSeguroDiferenciadoAereo) {
		this.blSeguroDiferenciadoAereo = blSeguroDiferenciadoAereo;
	}

	public Boolean getBlPermiteEmbarqueRodoNoAereo() {
		return blPermiteEmbarqueRodoNoAereo;
	}

	public void setBlPermiteEmbarqueRodoNoAereo(
			Boolean blPermiteEmbarqueRodoNoAereo) {
		this.blPermiteEmbarqueRodoNoAereo = blPermiteEmbarqueRodoNoAereo;
	}

	public DomainValue getTpEmissaoDoc() {
		return tpEmissaoDoc;
	}

	public void setTpEmissaoDoc(DomainValue tpEmissaoDoc) {
		this.tpEmissaoDoc = tpEmissaoDoc;
	}

	public Boolean getBlClienteCCT() {
		return blClienteCCT;
	}

	public void setBlClienteCCT(Boolean blClienteCCT) {
		this.blClienteCCT = blClienteCCT;
	}
	
	public Boolean getBlEmissorNfe() {
		return blEmissorNfe;
	}
	
	public void setBlEmissorNfe(Boolean blEmissorNfe) {
		this.blEmissorNfe = blEmissorNfe;
	}

	public Boolean getBldesconsiderarDificuldade() {
		return bldesconsiderarDificuldade;
	}

	public void setBldesconsiderarDificuldade(Boolean bldesconsiderarDificuldade) {
		this.bldesconsiderarDificuldade = bldesconsiderarDificuldade;
	}

	public void setDefaultBldesconsiderarDificuldade(){
		this.bldesconsiderarDificuldade = bldesconsiderarDificuldade == null ? Boolean.FALSE : bldesconsiderarDificuldade; 
	}

	public Boolean getBlEmissaoDiaNaoUtil() {
		return blEmissaoDiaNaoUtil;
	}

	public void setBlEmissaoDiaNaoUtil(Boolean blEmissaoDiaNaoUtil) {
		this.blEmissaoDiaNaoUtil = blEmissaoDiaNaoUtil;
	}

	public void setDefaultBlEmissaoDiaNaoUtil() {
		this.blEmissaoDiaNaoUtil = blEmissaoDiaNaoUtil == null ? Boolean.FALSE : blEmissaoDiaNaoUtil;;
	}
	
	public Boolean getBlEmissaoSabado() {
		return blEmissaoSabado;
	}

	public void setBlEmissaoSabado(Boolean blEmissaoSabado) {
		this.blEmissaoSabado = blEmissaoSabado;
	}

	public void setDefaultBlEmissaoSabado() {
		this.blEmissaoSabado = blEmissaoSabado == null ? Boolean.FALSE : blEmissaoSabado;
	}

	public DomainValue getTpClienteSolicitado() {
		return tpClienteSolicitado;
	}

	public void setTpClienteSolicitado(DomainValue tpClienteSolicitado) {
		this.tpClienteSolicitado = tpClienteSolicitado;
	}

	public Boolean getTemPendenciaTpCliente() {
		return temPendenciaTpCliente;
	}

	public void setTemPendenciaTpCliente(Boolean temPendenciaTpCliente) {
		this.temPendenciaTpCliente = temPendenciaTpCliente;
	}

	public String getDsMotivoSolicitacao() {
		return dsMotivoSolicitacao;
	}

	public void setDsMotivoSolicitacao(String dsMotivoSolicitacao) {
		this.dsMotivoSolicitacao = dsMotivoSolicitacao;
	}

	public Filial getFilialByIdFilialCobrancaSolicitada() {
		return filialByIdFilialCobrancaSolicitada;
	}

	public void setFilialByIdFilialCobrancaSolicitada(Filial filialByIdFilialCobrancaSolicitada) {
		this.filialByIdFilialCobrancaSolicitada = filialByIdFilialCobrancaSolicitada;
	}

	public Filial getFilialByIdFilialComercialSolicitada() {
		return filialByIdFilialComercialSolicitada;
	}

	public void setFilialByIdFilialComercialSolicitada(Filial filialByIdFilialComercialSolicitada) {
		this.filialByIdFilialComercialSolicitada = filialByIdFilialComercialSolicitada;
	}

	public Filial getFilialByIdFilialOperacionalSolicitada() {
		return filialByIdFilialOperacionalSolicitada;
	}

	public void setFilialByIdFilialOperacionalSolicitada(Filial filialByIdFilialOperacionalSolicitada) {
		this.filialByIdFilialOperacionalSolicitada = filialByIdFilialOperacionalSolicitada;
	}

	public Boolean getBlObrigaBO() {
		return blObrigaBO;
	}

	public void setBlObrigaBO(Boolean blObrigaBO) {
		this.blObrigaBO = blObrigaBO;
	}
	
	public Boolean getBlRemetenteOTD() {
		return blRemetenteOTD;
	}
	
	public void setBlRemetenteOTD(Boolean blRemetenteOTD) {
		this.blRemetenteOTD = blRemetenteOTD;
	}
	
	public Boolean getBlObrigaPesoCubadoEdi() {
		return blObrigaPesoCubadoEdi;
	}
	
	public void setBlObrigaPesoCubadoEdi(Boolean blObrigaPesoCubadoEdi) {
		this.blObrigaPesoCubadoEdi = blObrigaPesoCubadoEdi;
	}

	public DomainValue getTpModalObrigaBO() {
		return tpModalObrigaBO;
	}

	public void setTpModalObrigaBO(DomainValue tpModalObrigaBO) {
		this.tpModalObrigaBO = tpModalObrigaBO;
	}
	public void setBlClienteEstrategico(Boolean blClienteEstrategico) {
		this.blClienteEstrategico = blClienteEstrategico;
	}
	public Boolean getBlClienteEstrategico() {
		return blClienteEstrategico;
	}

	public String getObFatura() {
		return obFatura;
	}
	public void setObFatura(String obFatura) {
		this.obFatura = obFatura;
	}

	public Boolean getBlMtzLiberaRIM() {
		return blMtzLiberaRIM;
	}

	public void setBlMtzLiberaRIM(Boolean blMtzLiberaRIM) {
		this.blMtzLiberaRIM = blMtzLiberaRIM;
	}
	
	public Cliente(Long idPessoa, String nmPessoa, String nrIdentificacao, Long idFilial, String sgFilial, String nmFantasia) {
		super();
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(idPessoa);
		pessoa.setNmPessoa(nmPessoa);
		pessoa.setNrIdentificacao(nrIdentificacao);
		this.pessoa = pessoa;		
		Filial filialByIdFilialComercialSolicitada = new Filial();
		filialByIdFilialComercialSolicitada.setIdFilial(idFilial);
		filialByIdFilialComercialSolicitada.setSgFilial(sgFilial);
		Pessoa pessoaFilial = new Pessoa();
		pessoaFilial.setNmFantasia(nmFantasia);
		filialByIdFilialComercialSolicitada.setPessoa(pessoaFilial);
		this.filialByIdFilialComercialSolicitada = filialByIdFilialComercialSolicitada;
	}
	
	public Cliente(Long idCliente,Long idPessoa, String nmPessoa, String nrIdentificacao) {
		super();
		Pessoa pessoa = new Pessoa();
		pessoa.setIdPessoa(idPessoa);
		pessoa.setNmPessoa(nmPessoa);
		pessoa.setNrIdentificacao(nrIdentificacao);
		this.pessoa = pessoa;
		this.idCliente = idCliente;
	}

    public Boolean getBlClientePostoAvancado() {
        return blClientePostoAvancado;
    }

    public void setBlClientePostoAvancado(Boolean blClientePostoAvancado) {
        this.blClientePostoAvancado = blClientePostoAvancado;
    }

	public Boolean getBlAtualizaRazaoSocialDest() {
		return blAtualizaRazaoSocialDest;
	}

	public void setBlAtualizaRazaoSocialDest(Boolean blAtualizaRazaoSocialDest) {
		this.blAtualizaRazaoSocialDest = blAtualizaRazaoSocialDest;
	}

	public Boolean getBlEnviaDacteXmlFat() {
		return blEnviaDacteXmlFat != null ? blEnviaDacteXmlFat : false;
	}

	public void setBlEnviaDacteXmlFat(Boolean blEnviaDacteXmlFat) {
		this.blEnviaDacteXmlFat = blEnviaDacteXmlFat;
		if (getBlEnviaDacteXmlFat())
			this.blEnviaDocsFaturamentoNas = false;
	}

	public Boolean getBlGerarParcelaFreteValorLiquido() {
		return blGerarParcelaFreteValorLiquido;
	}

	public void setBlGerarParcelaFreteValorLiquido(Boolean blGerarParcelaFreteValorLiquido) {
		this.blGerarParcelaFreteValorLiquido = blGerarParcelaFreteValorLiquido;
	}

	public void setDefaultBlGerarParcelaFreteValorLiquido(){
		this.blGerarParcelaFreteValorLiquido = blGerarParcelaFreteValorLiquido == null ? Boolean.FALSE : blGerarParcelaFreteValorLiquido; 
	}

	public Boolean getBlDpeFeriado() {
		return blDpeFeriado;
	}

	public void setBlDpeFeriado(Boolean blDpeFeriado) {
		this.blDpeFeriado = blDpeFeriado;
	}

	public InformacaoDoctoCliente getInformacaoDoctoClienteConsolidado() {
		return informacaoDoctoClienteConsolidado;
	}

	public void setInformacaoDoctoClienteConsolidado(InformacaoDoctoCliente informacaoDoctoClienteConsolidado) {
		this.informacaoDoctoClienteConsolidado = informacaoDoctoClienteConsolidado;
	}

	public Boolean getBlGeraNovoDPE() {
		return blGeraNovoDPE;
	}

	public void setBlGeraNovoDPE(Boolean blGeraNovoDPE) {
		this.blGeraNovoDPE = blGeraNovoDPE;
	}

	public Boolean getBlAssinaturaDigital() {
		return blAssinaturaDigital;
	}

	public void setBlAssinaturaDigital(Boolean blAssinaturaDigital) {
		this.blAssinaturaDigital = blAssinaturaDigital;
	}	
	

    public Boolean getBlLiberaEtiquetaEdiLinehaul() {
        return blLiberaEtiquetaEdiLinehaul;
    }

    public void setBlLiberaEtiquetaEdiLinehaul(Boolean blLiberaEtiquetaEdiLinehaul) {
        this.blLiberaEtiquetaEdiLinehaul = blLiberaEtiquetaEdiLinehaul;
    }
	
	public Boolean getBlDivisao() {
		return blDivisao;
	}

	public void setBlDivisao(Boolean blDivisao) {
		this.blDivisao = blDivisao;
	}

	public Boolean getBlObrigaComprovanteEntrega() {
		return blObrigaComprovanteEntrega;
	}

	public void setBlObrigaComprovanteEntrega(Boolean blObrigaComprovanteEntrega) {
		this.blObrigaComprovanteEntrega = blObrigaComprovanteEntrega;
	}

	public Boolean getBlNfeConjulgada() {
		return blNfeConjulgada;
	}

	public void setBlNfeConjulgada(Boolean blNfeConjulgada) {
		this.blNfeConjulgada = blNfeConjulgada;
	}

	public Boolean getBlObrigaRg() {
		return blObrigaRg;
	}

	public void setBlObrigaRg(Boolean blObrigaRg) {
		this.blObrigaRg = blObrigaRg;
	}

	public Boolean getBlPermiteBaixaParcial() {
		return blPermiteBaixaParcial;
	}

	public void setBlPermiteBaixaParcial(Boolean blPermiteBaixaParcial) {
		this.blPermiteBaixaParcial = blPermiteBaixaParcial;
	}

	public Boolean getBlObrigaBaixaPorVolume() {
		return blObrigaBaixaPorVolume;
	}

	public void setBlObrigaBaixaPorVolume(Boolean blObrigaBaixaPorVolume) {
		this.blObrigaBaixaPorVolume = blObrigaBaixaPorVolume;
	}

	public Boolean getBlObrigaQuizBaixa() {
		return blObrigaQuizBaixa;
	}

	public void setBlObrigaQuizBaixa(Boolean blObrigaQuizBaixa) {
		this.blObrigaQuizBaixa = blObrigaQuizBaixa;
	}

    public Boolean getBlObrigaParentesco() {
        return blObrigaParentesco;
    }

    public void setBlObrigaParentesco(Boolean blObrigaParentesco) {
        this.blObrigaParentesco = blObrigaParentesco;
    }
    
	public Boolean getBlProdutoPerigoso() {
		return blProdutoPerigoso;
	}

	public void setBlProdutoPerigoso(Boolean blProdutoPerigoso) {
		this.blProdutoPerigoso = blProdutoPerigoso;
	}

	public Boolean getBlControladoPoliciaCivil() {
		return blControladoPoliciaCivil;
	}

	public void setBlControladoPoliciaCivil(Boolean blControladoPoliciaCivil) {
		this.blControladoPoliciaCivil = blControladoPoliciaCivil;
	}

	public Boolean getBlControladoPoliciaFederal() {
		return blControladoPoliciaFederal;
	}

	public void setBlControladoPoliciaFederal(Boolean blControladoPoliciaFederal) {
		this.blControladoPoliciaFederal = blControladoPoliciaFederal;
	}

	public Boolean getBlControladoExercito() {
		return blControladoExercito;
	}

	public void setBlControladoExercito(Boolean blControladoExercito) {
		this.blControladoExercito = blControladoExercito;
	}
		
    public Boolean getBlNaoPermiteSubcontratacao() {
        return blNaoPermiteSubcontratacao;
    }

    public void setBlNaoPermiteSubcontratacao(Boolean blNaoPermiteSubcontratacao) {
        this.blNaoPermiteSubcontratacao = blNaoPermiteSubcontratacao;
    }	
	public Boolean getBlObrigaRgEdi() {
		return blObrigaRgEdi;
	}

	public void setBlObrigaRgEdi(Boolean blObrigaRgEdi) {
		this.blObrigaRgEdi = blObrigaRgEdi;
	}

	public Boolean getBlEnviaDocsFaturamentoNas() {
		return blEnviaDocsFaturamentoNas != null ? blEnviaDocsFaturamentoNas : false;
	}

	public void setBlEnviaDocsFaturamentoNas(Boolean blEnviaDocsFaturamentoNas) {
		this.blEnviaDocsFaturamentoNas = blEnviaDocsFaturamentoNas;
		if (getBlEnviaDocsFaturamentoNas())
			this.blEnviaDacteXmlFat = false;
	}

	public Boolean getBlValidaCobrancDifTdeDest() {
		return blValidaCobrancDifTdeDest;
	}

	public void setBlValidaCobrancDifTdeDest(Boolean blValidaCobrancDifTdeDest) {
		this.blValidaCobrancDifTdeDest = blValidaCobrancDifTdeDest;
	}

	public Boolean getBlCobrancaTdeDiferenciada() {
		return blCobrancaTdeDiferenciada;
	}

	public void setBlCobrancaTdeDiferenciada(Boolean blCobrancaTdeDiferenciada) {
		this.blCobrancaTdeDiferenciada = blCobrancaTdeDiferenciada;
	}

	public BigDecimal getVlLimiteValorMercadoriaCteAereo() {
		return vlLimiteValorMercadoriaCteAereo;
	}

	public void setVlLimiteValorMercadoriaCteAereo(BigDecimal vlLimiteValorMercadoriaCteAereo) {
		this.vlLimiteValorMercadoriaCteAereo = vlLimiteValorMercadoriaCteAereo;
	}

	public BigDecimal getVlLimiteValorMercadoriaCteRodo() {
		return vlLimiteValorMercadoriaCteRodo;
	}

	public void setVlLimiteValorMercadoriaCteRodo(BigDecimal vlLimiteValorMercadoriaCteRodo) {
		this.vlLimiteValorMercadoriaCteRodo = vlLimiteValorMercadoriaCteRodo;
	}
}
