package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Regional;

public class ClienteLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idClienteLog;
	private Cliente cliente;
	private Usuario usuarioInclusao;
	private Cliente clienteResponsavelFrete;
	private Filial filialCobranca;
	private Filial filialAtendeOperacional;
	private Filial filialAtendeComercial;
	private DomainValue tpSituacao;
	private DomainValue tpCliente;
	private Boolean blGeraReciboFreteEntrega;
	private Boolean blPermanente;
	private Boolean blResponsavelFrete;
	private Boolean blBaseCalculo;
	private Boolean blCobraReentrega;
	private Boolean blCobraDevolucao;
	private Boolean blColetaAutomatica;
	private Boolean blFobDirigido;
	private Boolean blPesoAforadoPedagio;
	private Boolean blIcmsPedagio;
	private Boolean blIndicadorProtesto;
	private BigDecimal pcDescontoFreteCif;
	private BigDecimal pcDescontoFreteFob;
	private Long nrCasasDecimaisPeso;
	private Boolean blMatriz;
	private YearMonthDay dtGeracao;
	private Boolean blObrigaRecebedor;
	private Moeda moedaLimCred;
	private Moeda moedaLimDoctos;
	private Moeda moedaFatPrev;
	private Moeda moedaSaldoAtual;
	private Usuario usuarioAlteracao;
	private SegmentoMercado segmentoMercado;
	private Regional regionalComercial;
	private Regional regionalOperacional;
	private Regional regionalFinanceiro;
	private Cedente cedente;
	private Banco banco;
	private RamoAtividade ramoAtividade;
	private GrupoEconomico grupoEconomico;
	private Long nrConta;
	private DomainValue tpDificuldadeColeta;
	private DomainValue tpDificuldadeEntrega;
	private DomainValue tpDificuldadeClassificacao;
	private Boolean blEmiteBoletoCliDestino;
	private BigDecimal vlLimiteCredito;
	private Long nrDiasLimiteDebito;
	private BigDecimal vlFaturamentoPrevisto;
	private BigDecimal vlSaldoAtual;
	private BigDecimal pcJuroDiario;
	private BigDecimal vlLimiteDocumentos;
	private YearMonthDay dtUltimoMovimento;
	private YearMonthDay dtFundacaoEmpresa;
	private DomainValue tpCobranca;
	private DomainValue tpMeioEnvioBoleto;
	private String dsSite;
	private String obCliente;
	private DomainValue tpAtividadeEconomica;
	private Boolean blAgrupaFaturamentoMes;
	private DomainValue tpFormaArredondamento;
	private DomainValue tpLocalEmissaoConReent;
	private Boolean blAgrupaNotas;
	private Boolean blCadastradoColeta;
	private DomainValue tpPeriodicidadeTransf;
	private Boolean blRessarceFreteFob;
	private Boolean blPreFatura;
	private Boolean blFaturaDocsEntregues;
	private Boolean blCobrancaCentralizada;
	private Boolean blFaturaDocsConferidos;
	private Boolean blAgendamentoPessoaFisica;
	private Boolean blAgendamentoPessoaJuridica;
	private Boolean blFronteiraRapida;
	private Boolean blOperadorLogistico;
	private DomainValue tpFrequenciaVisita;
	private ObservacaoConhecimento observacaoConhecimento;
	private Boolean blFaturaDocReferencia;
	private Cliente clienteMatriz;
	private Boolean blDificuldadeEntrega;
	private Boolean blRetencaoComprovanteEnt;
	private Boolean blDivulgaLocalizacao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private YearMonthDay dhLog;
	private DomainValue opLog;
   
	public long getIdClienteLog() {
   
		return idClienteLog;
	}
   
	public void setIdClienteLog(long idClienteLog) {
   
		this.idClienteLog = idClienteLog;
	}
	
	public Cliente getCliente() {
   
		return cliente;
	}
   
	public void setCliente(Cliente cliente) {
   
		this.cliente = cliente;
	}
	
	public Usuario getUsuarioInclusao() {
   
		return usuarioInclusao;
	}
   
	public void setUsuarioInclusao(Usuario usuarioInclusao) {
   
		this.usuarioInclusao = usuarioInclusao;
	}
	
	public Cliente getClienteResponsavelFrete() {
   
		return clienteResponsavelFrete;
	}
   
	public void setClienteResponsavelFrete(Cliente clienteResponsavelFrete) {
   
		this.clienteResponsavelFrete = clienteResponsavelFrete;
	}
	
	public Filial getFilialCobranca() {
   
		return filialCobranca;
	}
   
	public void setFilialCobranca(Filial filialCobranca) {
   
		this.filialCobranca = filialCobranca;
	}
	
	public Filial getFilialAtendeOperacional() {
   
		return filialAtendeOperacional;
	}
   
	public void setFilialAtendeOperacional(Filial filialAtendeOperacional) {
   
		this.filialAtendeOperacional = filialAtendeOperacional;
	}
	
	public Filial getFilialAtendeComercial() {
   
		return filialAtendeComercial;
	}
   
	public void setFilialAtendeComercial(Filial filialAtendeComercial) {
   
		this.filialAtendeComercial = filialAtendeComercial;
	}
	
	public DomainValue getTpSituacao() {
   
		return tpSituacao;
	}
   
	public void setTpSituacao(DomainValue tpSituacao) {
   
		this.tpSituacao = tpSituacao;
	}
	
	public DomainValue getTpCliente() {
   
		return tpCliente;
	}
   
	public void setTpCliente(DomainValue tpCliente) {
   
		this.tpCliente = tpCliente;
	}
	
	public Boolean isBlGeraReciboFreteEntrega() {
   
		return blGeraReciboFreteEntrega;
	}
   
	public void setBlGeraReciboFreteEntrega(Boolean blGeraReciboFreteEntrega) {
   
		this.blGeraReciboFreteEntrega = blGeraReciboFreteEntrega;
	}
	
	public Boolean isBlPermanente() {
   
		return blPermanente;
	}
   
	public void setBlPermanente(Boolean blPermanente) {
   
		this.blPermanente = blPermanente;
	}
	
	public Boolean isBlResponsavelFrete() {
   
		return blResponsavelFrete;
	}
   
	public void setBlResponsavelFrete(Boolean blResponsavelFrete) {
   
		this.blResponsavelFrete = blResponsavelFrete;
	}
	
	public Boolean isBlBaseCalculo() {
   
		return blBaseCalculo;
	}
   
	public void setBlBaseCalculo(Boolean blBaseCalculo) {
   
		this.blBaseCalculo = blBaseCalculo;
	}
	
	public Boolean isBlCobraReentrega() {
   
		return blCobraReentrega;
	}
   
	public void setBlCobraReentrega(Boolean blCobraReentrega) {
   
		this.blCobraReentrega = blCobraReentrega;
	}
	
	public Boolean isBlCobraDevolucao() {
   
		return blCobraDevolucao;
	}
   
	public void setBlCobraDevolucao(Boolean blCobraDevolucao) {
   
		this.blCobraDevolucao = blCobraDevolucao;
	}
	
	public Boolean isBlColetaAutomatica() {
   
		return blColetaAutomatica;
	}
   
	public void setBlColetaAutomatica(Boolean blColetaAutomatica) {
   
		this.blColetaAutomatica = blColetaAutomatica;
	}
	
	public Boolean isBlFobDirigido() {
   
		return blFobDirigido;
	}
   
	public void setBlFobDirigido(Boolean blFobDirigido) {
   
		this.blFobDirigido = blFobDirigido;
	}
	
	public Boolean isBlPesoAforadoPedagio() {
   
		return blPesoAforadoPedagio;
	}
   
	public void setBlPesoAforadoPedagio(Boolean blPesoAforadoPedagio) {
   
		this.blPesoAforadoPedagio = blPesoAforadoPedagio;
	}
	
	public Boolean isBlIcmsPedagio() {
   
		return blIcmsPedagio;
	}
   
	public void setBlIcmsPedagio(Boolean blIcmsPedagio) {
   
		this.blIcmsPedagio = blIcmsPedagio;
	}
	
	public Boolean isBlIndicadorProtesto() {
   
		return blIndicadorProtesto;
	}
   
	public void setBlIndicadorProtesto(Boolean blIndicadorProtesto) {
   
		this.blIndicadorProtesto = blIndicadorProtesto;
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
	
	public Long getNrCasasDecimaisPeso() {
   
		return nrCasasDecimaisPeso;
	}
   
	public void setNrCasasDecimaisPeso(Long nrCasasDecimaisPeso) {
   
		this.nrCasasDecimaisPeso = nrCasasDecimaisPeso;
	}
	
	public Boolean isBlMatriz() {
   
		return blMatriz;
	}
   
	public void setBlMatriz(Boolean blMatriz) {
   
		this.blMatriz = blMatriz;
	}
	
	public YearMonthDay getDtGeracao() {
   
		return dtGeracao;
	}
   
	public void setDtGeracao(YearMonthDay dtGeracao) {
   
		this.dtGeracao = dtGeracao;
	}
	
	public Boolean isBlObrigaRecebedor() {
   
		return blObrigaRecebedor;
	}
   
	public void setBlObrigaRecebedor(Boolean blObrigaRecebedor) {
   
		this.blObrigaRecebedor = blObrigaRecebedor;
	}
	
	public Moeda getMoedaLimCred() {
   
		return moedaLimCred;
	}
   
	public void setMoedaLimCred(Moeda moedaLimCred) {
   
		this.moedaLimCred = moedaLimCred;
	}
	
	public Moeda getMoedaLimDoctos() {
   
		return moedaLimDoctos;
	}
   
	public void setMoedaLimDoctos(Moeda moedaLimDoctos) {
   
		this.moedaLimDoctos = moedaLimDoctos;
	}
	
	public Moeda getMoedaFatPrev() {
   
		return moedaFatPrev;
	}
   
	public void setMoedaFatPrev(Moeda moedaFatPrev) {
   
		this.moedaFatPrev = moedaFatPrev;
	}
	
	public Moeda getMoedaSaldoAtual() {
   
		return moedaSaldoAtual;
	}
   
	public void setMoedaSaldoAtual(Moeda moedaSaldoAtual) {
   
		this.moedaSaldoAtual = moedaSaldoAtual;
	}
	
	public Usuario getUsuarioAlteracao() {
   
		return usuarioAlteracao;
	}
   
	public void setUsuarioAlteracao(Usuario usuarioAlteracao) {
   
		this.usuarioAlteracao = usuarioAlteracao;
	}
	
	public SegmentoMercado getSegmentoMercado() {
   
		return segmentoMercado;
	}
   
	public void setSegmentoMercado(SegmentoMercado segmentoMercado) {
   
		this.segmentoMercado = segmentoMercado;
	}
	
	public Regional getRegionalComercial() {
   
		return regionalComercial;
	}
   
	public void setRegionalComercial(Regional regionalComercial) {
   
		this.regionalComercial = regionalComercial;
	}
	
	public Regional getRegionalOperacional() {
   
		return regionalOperacional;
	}
   
	public void setRegionalOperacional(Regional regionalOperacional) {
   
		this.regionalOperacional = regionalOperacional;
	}
	
	public Regional getRegionalFinanceiro() {
   
		return regionalFinanceiro;
	}
   
	public void setRegionalFinanceiro(Regional regionalFinanceiro) {
   
		this.regionalFinanceiro = regionalFinanceiro;
	}
	
	public Cedente getCedente() {
   
		return cedente;
	}
   
	public void setCedente(Cedente cedente) {
   
		this.cedente = cedente;
	}
	
	public Banco getBanco() {
   
		return banco;
	}
   
	public void setBanco(Banco banco) {
   
		this.banco = banco;
	}
	
	public RamoAtividade getRamoAtividade() {
   
		return ramoAtividade;
	}
   
	public void setRamoAtividade(RamoAtividade ramoAtividade) {
   
		this.ramoAtividade = ramoAtividade;
	}
	
	public GrupoEconomico getGrupoEconomico() {
   
		return grupoEconomico;
	}
   
	public void setGrupoEconomico(GrupoEconomico grupoEconomico) {
   
		this.grupoEconomico = grupoEconomico;
	}
	
	public Long getNrConta() {
   
		return nrConta;
	}
   
	public void setNrConta(Long nrConta) {
   
		this.nrConta = nrConta;
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
	
	public DomainValue getTpDificuldadeClassificacao() {
   
		return tpDificuldadeClassificacao;
	}
   
	public void setTpDificuldadeClassificacao(
			DomainValue tpDificuldadeClassificacao) {
   
		this.tpDificuldadeClassificacao = tpDificuldadeClassificacao;
	}
	
	public Boolean isBlEmiteBoletoCliDestino() {
   
		return blEmiteBoletoCliDestino;
	}
   
	public void setBlEmiteBoletoCliDestino(Boolean blEmiteBoletoCliDestino) {
   
		this.blEmiteBoletoCliDestino = blEmiteBoletoCliDestino;
	}
	
	public BigDecimal getVlLimiteCredito() {
   
		return vlLimiteCredito;
	}
   
	public void setVlLimiteCredito(BigDecimal vlLimiteCredito) {
   
		this.vlLimiteCredito = vlLimiteCredito;
	}
	
	public Long getNrDiasLimiteDebito() {
   
		return nrDiasLimiteDebito;
	}
   
	public void setNrDiasLimiteDebito(Long nrDiasLimiteDebito) {
   
		this.nrDiasLimiteDebito = nrDiasLimiteDebito;
	}
	
	public BigDecimal getVlFaturamentoPrevisto() {
   
		return vlFaturamentoPrevisto;
	}
   
	public void setVlFaturamentoPrevisto(BigDecimal vlFaturamentoPrevisto) {
   
		this.vlFaturamentoPrevisto = vlFaturamentoPrevisto;
	}
	
	public BigDecimal getVlSaldoAtual() {
   
		return vlSaldoAtual;
	}
   
	public void setVlSaldoAtual(BigDecimal vlSaldoAtual) {
   
		this.vlSaldoAtual = vlSaldoAtual;
	}
	
	public BigDecimal getPcJuroDiario() {
   
		return pcJuroDiario;
	}
   
	public void setPcJuroDiario(BigDecimal pcJuroDiario) {
   
		this.pcJuroDiario = pcJuroDiario;
	}
	
	public BigDecimal getVlLimiteDocumentos() {
   
		return vlLimiteDocumentos;
	}
   
	public void setVlLimiteDocumentos(BigDecimal vlLimiteDocumentos) {
   
		this.vlLimiteDocumentos = vlLimiteDocumentos;
	}
	
	public YearMonthDay getDtUltimoMovimento() {
   
		return dtUltimoMovimento;
	}
   
	public void setDtUltimoMovimento(YearMonthDay dtUltimoMovimento) {
   
		this.dtUltimoMovimento = dtUltimoMovimento;
	}
	
	public YearMonthDay getDtFundacaoEmpresa() {
   
		return dtFundacaoEmpresa;
	}
   
	public void setDtFundacaoEmpresa(YearMonthDay dtFundacaoEmpresa) {
   
		this.dtFundacaoEmpresa = dtFundacaoEmpresa;
	}
	
	public DomainValue getTpCobranca() {
   
		return tpCobranca;
	}
   
	public void setTpCobranca(DomainValue tpCobranca) {
   
		this.tpCobranca = tpCobranca;
	}
	
	public DomainValue getTpMeioEnvioBoleto() {
   
		return tpMeioEnvioBoleto;
	}
   
	public void setTpMeioEnvioBoleto(DomainValue tpMeioEnvioBoleto) {
   
		this.tpMeioEnvioBoleto = tpMeioEnvioBoleto;
	}
	
	public String getDsSite() {
   
		return dsSite;
	}
   
	public void setDsSite(String dsSite) {
   
		this.dsSite = dsSite;
	}
	
	public String getObCliente() {
   
		return obCliente;
	}
   
	public void setObCliente(String obCliente) {
   
		this.obCliente = obCliente;
	}
	
	public DomainValue getTpAtividadeEconomica() {
   
		return tpAtividadeEconomica;
	}
   
	public void setTpAtividadeEconomica(DomainValue tpAtividadeEconomica) {
   
		this.tpAtividadeEconomica = tpAtividadeEconomica;
	}
	
	public Boolean isBlAgrupaFaturamentoMes() {
   
		return blAgrupaFaturamentoMes;
	}
   
	public void setBlAgrupaFaturamentoMes(Boolean blAgrupaFaturamentoMes) {
   
		this.blAgrupaFaturamentoMes = blAgrupaFaturamentoMes;
	}
	
	public DomainValue getTpFormaArredondamento() {
   
		return tpFormaArredondamento;
	}
   
	public void setTpFormaArredondamento(DomainValue tpFormaArredondamento) {
   
		this.tpFormaArredondamento = tpFormaArredondamento;
	}
	
	public DomainValue getTpLocalEmissaoConReent() {
   
		return tpLocalEmissaoConReent;
	}
   
	public void setTpLocalEmissaoConReent(DomainValue tpLocalEmissaoConReent) {
   
		this.tpLocalEmissaoConReent = tpLocalEmissaoConReent;
	}
	
	public Boolean isBlAgrupaNotas() {
   
		return blAgrupaNotas;
	}
   
	public void setBlAgrupaNotas(Boolean blAgrupaNotas) {
   
		this.blAgrupaNotas = blAgrupaNotas;
	}
	
	public Boolean isBlCadastradoColeta() {
   
		return blCadastradoColeta;
	}
   
	public void setBlCadastradoColeta(Boolean blCadastradoColeta) {
   
		this.blCadastradoColeta = blCadastradoColeta;
	}
	
	public DomainValue getTpPeriodicidadeTransf() {
   
		return tpPeriodicidadeTransf;
	}
   
	public void setTpPeriodicidadeTransf(DomainValue tpPeriodicidadeTransf) {
   
		this.tpPeriodicidadeTransf = tpPeriodicidadeTransf;
	}
	
	public Boolean isBlRessarceFreteFob() {
   
		return blRessarceFreteFob;
	}
   
	public void setBlRessarceFreteFob(Boolean blRessarceFreteFob) {
   
		this.blRessarceFreteFob = blRessarceFreteFob;
	}
	
	public Boolean isBlPreFatura() {
   
		return blPreFatura;
	}
   
	public void setBlPreFatura(Boolean blPreFatura) {
   
		this.blPreFatura = blPreFatura;
	}
	
	public Boolean isBlFaturaDocsEntregues() {
   
		return blFaturaDocsEntregues;
	}
   
	public void setBlFaturaDocsEntregues(Boolean blFaturaDocsEntregues) {
   
		this.blFaturaDocsEntregues = blFaturaDocsEntregues;
	}
	
	public Boolean isBlCobrancaCentralizada() {
   
		return blCobrancaCentralizada;
	}
   
	public void setBlCobrancaCentralizada(Boolean blCobrancaCentralizada) {
   
		this.blCobrancaCentralizada = blCobrancaCentralizada;
	}
	
	public Boolean isBlFaturaDocsConferidos() {
   
		return blFaturaDocsConferidos;
	}
   
	public void setBlFaturaDocsConferidos(Boolean blFaturaDocsConferidos) {
   
		this.blFaturaDocsConferidos = blFaturaDocsConferidos;
	}
	
	public Boolean isBlAgendamentoPessoaFisica() {
   
		return blAgendamentoPessoaFisica;
	}
   
	public void setBlAgendamentoPessoaFisica(Boolean blAgendamentoPessoaFisica) {
   
		this.blAgendamentoPessoaFisica = blAgendamentoPessoaFisica;
	}
	
	public Boolean isBlAgendamentoPessoaJuridica() {
   
		return blAgendamentoPessoaJuridica;
	}
   
	public void setBlAgendamentoPessoaJuridica(
			Boolean blAgendamentoPessoaJuridica) {
   
		this.blAgendamentoPessoaJuridica = blAgendamentoPessoaJuridica;
	}
	
	public Boolean isBlFronteiraRapida() {
   
		return blFronteiraRapida;
	}
   
	public void setBlFronteiraRapida(Boolean blFronteiraRapida) {
   
		this.blFronteiraRapida = blFronteiraRapida;
	}
	
	public Boolean isBlOperadorLogistico() {
   
		return blOperadorLogistico;
	}
   
	public void setBlOperadorLogistico(Boolean blOperadorLogistico) {
   
		this.blOperadorLogistico = blOperadorLogistico;
	}
	
	public DomainValue getTpFrequenciaVisita() {
   
		return tpFrequenciaVisita;
	}
   
	public void setTpFrequenciaVisita(DomainValue tpFrequenciaVisita) {
   
		this.tpFrequenciaVisita = tpFrequenciaVisita;
	}
	
	public ObservacaoConhecimento getObservacaoConhecimento() {
   
		return observacaoConhecimento;
	}
   
	public void setObservacaoConhecimento(
			ObservacaoConhecimento observacaoConhecimento) {
   
		this.observacaoConhecimento = observacaoConhecimento;
	}
	
	public Boolean isBlFaturaDocReferencia() {
   
		return blFaturaDocReferencia;
	}
   
	public void setBlFaturaDocReferencia(Boolean blFaturaDocReferencia) {
   
		this.blFaturaDocReferencia = blFaturaDocReferencia;
	}
	
	public Cliente getClienteMatriz() {
   
		return clienteMatriz;
	}
   
	public void setClienteMatriz(Cliente clienteMatriz) {
   
		this.clienteMatriz = clienteMatriz;
	}
	
	public Boolean isBlDificuldadeEntrega() {
   
		return blDificuldadeEntrega;
	}
   
	public void setBlDificuldadeEntrega(Boolean blDificuldadeEntrega) {
   
		this.blDificuldadeEntrega = blDificuldadeEntrega;
	}
	
	public Boolean isBlRetencaoComprovanteEnt() {
   
		return blRetencaoComprovanteEnt;
	}
   
	public void setBlRetencaoComprovanteEnt(Boolean blRetencaoComprovanteEnt) {
   
		this.blRetencaoComprovanteEnt = blRetencaoComprovanteEnt;
	}
	
	public Boolean isBlDivulgaLocalizacao() {
   
		return blDivulgaLocalizacao;
	}
   
	public void setBlDivulgaLocalizacao(Boolean blDivulgaLocalizacao) {
   
		this.blDivulgaLocalizacao = blDivulgaLocalizacao;
	}
	
	public DomainValue getTpOrigemLog() {
   
		return tpOrigemLog;
	}
   
	public void setTpOrigemLog(DomainValue tpOrigemLog) {
   
		this.tpOrigemLog = tpOrigemLog;
	}
	
	public String getLoginLog() {
   
		return loginLog;
	}
   
	public void setLoginLog(String loginLog) {
   
		this.loginLog = loginLog;
	}
	
	public DomainValue getOpLog() {
   
		return opLog;
	}
   
	public void setOpLog(DomainValue opLog) {
   
		this.opLog = opLog;
	}
	
   	public String toString() {
		return new ToStringBuilder(this).append("idClienteLog",
				getIdClienteLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ClienteLog))
			return false;
		ClienteLog castOther = (ClienteLog) other;
		return new EqualsBuilder().append(this.getIdClienteLog(),
				castOther.getIdClienteLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdClienteLog()).toHashCode();
	}

	public YearMonthDay getDhLog() {
		return dhLog;
	}

	public void setDhLog(YearMonthDay dhLog) {
		this.dhLog = dhLog;
	}
} 