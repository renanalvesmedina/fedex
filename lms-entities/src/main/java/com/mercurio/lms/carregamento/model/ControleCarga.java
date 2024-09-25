package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.RotaIdaVolta;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.Mir;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCredito;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.sgr.model.ExigenciaIndicada;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.SolicitacaoSinal;
import com.mercurio.lms.sgr.model.ValorEscolta;

/** @author LMS Custom Hibernate CodeGenerator */
public class ControleCarga implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idControleCarga;

    /** persistent field */
    private Long nrControleCarga;

    /** persistent field */
    private Long nrManif;

    /** persistent field */
    private Long nrSMP;

    /** persistent field */
    private DomainValue tpControleCarga;
    
    /** persistent field */
    private DomainValue tpRotaViagem;

    /** persistent field */
    private DomainValue tpStatusControleCarga;

    /** persistent field */
    private BigDecimal vlPedagio;

    /** nullable persistent field */
    private BigDecimal vlFreteCarreteiro;

    /** nullable persistent field */
    private String dsSenhaCelularVeiculo;

    /** nullable persistent field */
    private DateTime dhGeracaoSeguro;
    
    /** nullable persistent field */
    private DateTime dhGeracao;

    /** nullable persistent field */
    private DateTime dhSaidaColetaEntrega;

    /** nullable persistent field */
    private DateTime dhChegadaColetaEntrega;

    /** nullable persistent field */
    private BigDecimal pcOcupacaoCalculado;

    /** nullable persistent field */
    private BigDecimal pcOcupacaoAforadoCalculado;

    /** nullable persistent field */
    private BigDecimal pcOcupacaoInformado;
    
    /** nullable persistent field */
    private BigDecimal psTotalAforado;
    
    /** nullable persistent field */
    private BigDecimal psTotalFrota;
    
    /** nullable persistent field */
    private BigDecimal psColetado;
    
    /** nullable persistent field */
    private BigDecimal psAColetar;
    
    /** nullable persistent field */
    private BigDecimal psEntregue;
    
    /** nullable persistent field */
    private BigDecimal psAEntregar;
    
    /** nullable persistent field */
    private BigDecimal vlTotalFrota;

    /** nullable persistent field */
    private BigDecimal vlColetado;

    /** nullable persistent field */
    private BigDecimal vlAColetar;

    /** nullable persistent field */
    private BigDecimal vlEntregue;

    /** nullable persistent field */
    private BigDecimal vlAEntregar;
    
    /** nullable persistent field */
    private BigDecimal pcFreteAgregado;
    
    /** nullable persistent field */
    private BigDecimal pcFreteEventual;

    /** nullable persistent field */
    private BigDecimal pcFreteMercurio;
    
    /** nullable persistent field */
    private DateTime dhPrevisaoSaida;

    /** nullable persistent field */
    private Integer nrTempoViagem;
    
    /** identifier field */
    private Boolean blEntregaDireta;

    private Boolean blExigeCIOT;

    private Boolean blEnviadoIntegCIOT;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporteByIdSemiRebocado;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporteByIdTransportado;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega tabelaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Rota rota;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista motorista;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Motorista instrutorMotorista;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.MotivoCancelamentoCc motivoCancelamentoCc;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialAtualizaStatus;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;
    
    /** persistent field */
    private List<Manifesto> manifestos;

    /** persistent field */
    private List<VeiculoControleCarga> veiculoControleCargas;

    /** persistent field */
    private List<LacreControleCarga> lacreControleCargas;

    /** persistent field */
    private List<PagtoProprietarioCc> pagtoProprietarioCcs;

    /** persistent field */
    private List<ManifestoColeta> manifestoColetas;

    /** persistent field */
    private List<EstoqueDispositivoQtde> estoqueDispositivoQtdes;

    /** persistent field */
    private List<EstoqueDispIdentificado> estoqueDispIdentificados;

    /** persistent field */
    private List<ManifestoEntrega> manifestoEntregas;

    /** persistent field */
    private List<ControleQuilometragem> controleQuilometragems;

    /** persistent field */
    private List<PostoPassagemCc> postoPassagemCcs;

    /** persistent field */
    private List<PostoAvancadoCc> postoAvancadoCcs;

    /** persistent field */
    private List<EventoControleCarga> eventoControleCargas;

    /** persistent field */
    private List<OcorrenciaNaoConformidade> ocorrenciaNaoConformidades;

    /** persistent field */
    private List<ControleTrecho> controleTrechos;

    /** persistent field */
    private List<ExigenciaIndicada> exigenciaIndicadas;

    /** persistent field */
    private List<MotoristaControleCarga> motoristaControleCargas;

    /** persistent field */
    private List<PagtoPedagioCc> pagtoPedagioCcs;

    /** persistent field */
    private List<CarregamentoDescarga> carregamentoDescargas;

    /** persistent field */
    private List<SolicitacaoSinal> solicitacaoSinais;

    /** persistent field */
    private List<ReciboFreteCarreteiro> reciboFreteCarreteiros;

    /** persistent field */
    private List<ProcessoSinistro> processoSinistros;

    /** persistent field */
    private List<EquipeOperacao> equipeOperacoes;

    /** persistent field */
    private List<SemiReboqueCc> semiReboqueCcs;

    /** persistent field */
    private List<Mir> mirs;

    /** persistent field */
    private List<ValorEscolta> valorEscoltas;

    /** persistent field */
	private List<SolicMonitPreventivo> solicMonitPreventivos;
	
	/** persistent field */
	private List<FilialRotaCc> filialRotaCcs;

	private List<ManifestoEletronico> manifestosEletronicos;
    private List<NotaCredito> notasCredito;
    
    private NotaCredito notaCredito;

    private List<TabelaColetaEntregaCC> tabelasColetaEntregaCC;

    public ControleCarga() {
    }

    public ControleCarga(Long idControleCarga, Long nrControleCarga, Long nrManif, Long nrSMP, DomainValue tpControleCarga, DomainValue tpRotaViagem, DomainValue tpStatusControleCarga, BigDecimal vlPedagio, BigDecimal vlFreteCarreteiro, String dsSenhaCelularVeiculo, DateTime dhGeracaoSeguro, DateTime dhGeracao, DateTime dhSaidaColetaEntrega, DateTime dhChegadaColetaEntrega, BigDecimal pcOcupacaoCalculado, BigDecimal pcOcupacaoAforadoCalculado, BigDecimal pcOcupacaoInformado, BigDecimal psTotalAforado, BigDecimal psTotalFrota, BigDecimal psColetado, BigDecimal psAColetar, BigDecimal psEntregue, BigDecimal psAEntregar, BigDecimal vlTotalFrota, BigDecimal vlColetado, BigDecimal vlAColetar, BigDecimal vlEntregue, BigDecimal vlAEntregar, BigDecimal pcFreteAgregado, BigDecimal pcFreteEventual, BigDecimal pcFreteMercurio, DateTime dhPrevisaoSaida, Integer nrTempoViagem, Boolean blEntregaDireta, Boolean blExigeCIOT, Boolean blEnviadoIntegCIOT, MeioTransporte meioTransporteByIdSemiRebocado, MeioTransporte meioTransporteByIdTransportado, SolicitacaoContratacao solicitacaoContratacao, TipoTabelaColetaEntrega tipoTabelaColetaEntrega, TabelaColetaEntrega tabelaColetaEntrega, RotaIdaVolta rotaIdaVolta, Rota rota, Moeda moeda, Motorista motorista, Motorista instrutorMotorista, MotivoCancelamentoCc motivoCancelamentoCc, Proprietario proprietario, RotaColetaEntrega rotaColetaEntrega, Filial filialByIdFilialAtualizaStatus, Filial filialByIdFilialOrigem, Filial filialByIdFilialDestino, List<Manifesto> manifestos, List<VeiculoControleCarga> veiculoControleCargas, List<LacreControleCarga> lacreControleCargas, List<PagtoProprietarioCc> pagtoProprietarioCcs, List<ManifestoColeta> manifestoColetas, List<EstoqueDispositivoQtde> estoqueDispositivoQtdes, List<EstoqueDispIdentificado> estoqueDispIdentificados, List<ManifestoEntrega> manifestoEntregas, List<ControleQuilometragem> controleQuilometragems, List<PostoPassagemCc> postoPassagemCcs, List<PostoAvancadoCc> postoAvancadoCcs, List<EventoControleCarga> eventoControleCargas, List<OcorrenciaNaoConformidade> ocorrenciaNaoConformidades, List<ControleTrecho> controleTrechos, List<ExigenciaIndicada> exigenciaIndicadas, List<MotoristaControleCarga> motoristaControleCargas, List<PagtoPedagioCc> pagtoPedagioCcs, List<CarregamentoDescarga> carregamentoDescargas, List<SolicitacaoSinal> solicitacaoSinais, List<ReciboFreteCarreteiro> reciboFreteCarreteiros, List<ProcessoSinistro> processoSinistros, List<EquipeOperacao> equipeOperacoes, List<SemiReboqueCc> semiReboqueCcs, List<Mir> mirs, List<ValorEscolta> valorEscoltas, List<SolicMonitPreventivo> solicMonitPreventivos, List<FilialRotaCc> filialRotaCcs, List<ManifestoEletronico> manifestosEletronicos, List<NotaCredito> notasCredito, NotaCredito notaCredito, List<TabelaColetaEntregaCC> tabelasColetaEntregaCC) {
        this.idControleCarga = idControleCarga;
        this.nrControleCarga = nrControleCarga;
        this.nrManif = nrManif;
        this.nrSMP = nrSMP;
        this.tpControleCarga = tpControleCarga;
        this.tpRotaViagem = tpRotaViagem;
        this.tpStatusControleCarga = tpStatusControleCarga;
        this.vlPedagio = vlPedagio;
        this.vlFreteCarreteiro = vlFreteCarreteiro;
        this.dsSenhaCelularVeiculo = dsSenhaCelularVeiculo;
        this.dhGeracaoSeguro = dhGeracaoSeguro;
        this.dhGeracao = dhGeracao;
        this.dhSaidaColetaEntrega = dhSaidaColetaEntrega;
        this.dhChegadaColetaEntrega = dhChegadaColetaEntrega;
        this.pcOcupacaoCalculado = pcOcupacaoCalculado;
        this.pcOcupacaoAforadoCalculado = pcOcupacaoAforadoCalculado;
        this.pcOcupacaoInformado = pcOcupacaoInformado;
        this.psTotalAforado = psTotalAforado;
        this.psTotalFrota = psTotalFrota;
        this.psColetado = psColetado;
        this.psAColetar = psAColetar;
        this.psEntregue = psEntregue;
        this.psAEntregar = psAEntregar;
        this.vlTotalFrota = vlTotalFrota;
        this.vlColetado = vlColetado;
        this.vlAColetar = vlAColetar;
        this.vlEntregue = vlEntregue;
        this.vlAEntregar = vlAEntregar;
        this.pcFreteAgregado = pcFreteAgregado;
        this.pcFreteEventual = pcFreteEventual;
        this.pcFreteMercurio = pcFreteMercurio;
        this.dhPrevisaoSaida = dhPrevisaoSaida;
        this.nrTempoViagem = nrTempoViagem;
        this.blEntregaDireta = blEntregaDireta;
        this.blExigeCIOT = blExigeCIOT;
        this.blEnviadoIntegCIOT = blEnviadoIntegCIOT;
        this.meioTransporteByIdSemiRebocado = meioTransporteByIdSemiRebocado;
        this.meioTransporteByIdTransportado = meioTransporteByIdTransportado;
        this.solicitacaoContratacao = solicitacaoContratacao;
        this.tipoTabelaColetaEntrega = tipoTabelaColetaEntrega;
        this.tabelaColetaEntrega = tabelaColetaEntrega;
        this.rotaIdaVolta = rotaIdaVolta;
        this.rota = rota;
        this.moeda = moeda;
        this.motorista = motorista;
        this.instrutorMotorista = instrutorMotorista;
        this.motivoCancelamentoCc = motivoCancelamentoCc;
        this.proprietario = proprietario;
        this.rotaColetaEntrega = rotaColetaEntrega;
        this.filialByIdFilialAtualizaStatus = filialByIdFilialAtualizaStatus;
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
        this.filialByIdFilialDestino = filialByIdFilialDestino;
        this.manifestos = manifestos;
        this.veiculoControleCargas = veiculoControleCargas;
        this.lacreControleCargas = lacreControleCargas;
        this.pagtoProprietarioCcs = pagtoProprietarioCcs;
        this.manifestoColetas = manifestoColetas;
        this.estoqueDispositivoQtdes = estoqueDispositivoQtdes;
        this.estoqueDispIdentificados = estoqueDispIdentificados;
        this.manifestoEntregas = manifestoEntregas;
        this.controleQuilometragems = controleQuilometragems;
        this.postoPassagemCcs = postoPassagemCcs;
        this.postoAvancadoCcs = postoAvancadoCcs;
        this.eventoControleCargas = eventoControleCargas;
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
        this.controleTrechos = controleTrechos;
        this.exigenciaIndicadas = exigenciaIndicadas;
        this.motoristaControleCargas = motoristaControleCargas;
        this.pagtoPedagioCcs = pagtoPedagioCcs;
        this.carregamentoDescargas = carregamentoDescargas;
        this.solicitacaoSinais = solicitacaoSinais;
        this.reciboFreteCarreteiros = reciboFreteCarreteiros;
        this.processoSinistros = processoSinistros;
        this.equipeOperacoes = equipeOperacoes;
        this.semiReboqueCcs = semiReboqueCcs;
        this.mirs = mirs;
        this.valorEscoltas = valorEscoltas;
        this.solicMonitPreventivos = solicMonitPreventivos;
        this.filialRotaCcs = filialRotaCcs;
        this.manifestosEletronicos = manifestosEletronicos;
        this.notasCredito = notasCredito;
        this.notaCredito = notaCredito;
        this.tabelasColetaEntregaCC = tabelasColetaEntregaCC;
    }

    public Long getIdControleCarga() {
        return this.idControleCarga;
    }

    public void setIdControleCarga(Long idControleCarga) {
        this.idControleCarga = idControleCarga;
    }

    public Long getNrControleCarga() {
        return this.nrControleCarga;
    }

    public void setNrControleCarga(Long nrControleCarga) {
        this.nrControleCarga = nrControleCarga;
    }

    public Long getNrManif() {
		return nrManif;
	}

	public void setNrManif(Long nrManif) {
		this.nrManif = nrManif;
	}

    public Long getNrSMP() {
		return nrSMP;
	}

	public void setNrSMP(Long nrSMP) {
		this.nrSMP = nrSMP;
	}
	
    public DomainValue getTpControleCarga() {
        return this.tpControleCarga;
    }

    public void setTpControleCarga(DomainValue tpControleCarga) {
        this.tpControleCarga = tpControleCarga;
    }

    public DomainValue getTpRotaViagem() {
		return tpRotaViagem;
	}

	public void setTpRotaViagem(DomainValue tpRotaViagem) {
		this.tpRotaViagem = tpRotaViagem;
	}

	public DomainValue getTpStatusControleCarga() {
        return this.tpStatusControleCarga;
    }

    public void setTpStatusControleCarga(DomainValue tpStatusControleCarga) {
        this.tpStatusControleCarga = tpStatusControleCarga;
    }

    public BigDecimal getVlPedagio() {
        return this.vlPedagio;
    }

    public void setVlPedagio(BigDecimal vlPedagio) {
        this.vlPedagio = vlPedagio;
    }

    public BigDecimal getVlFreteCarreteiro() {
        return this.vlFreteCarreteiro;
    }

    public void setVlFreteCarreteiro(BigDecimal vlFreteCarreteiro) {
        this.vlFreteCarreteiro = vlFreteCarreteiro;
    }

    public String getDsSenhaCelularVeiculo() {
        return this.dsSenhaCelularVeiculo;
    }

    public void setDsSenhaCelularVeiculo(String dsSenhaCelularVeiculo) {
        this.dsSenhaCelularVeiculo = dsSenhaCelularVeiculo;
    }

    public DateTime getDhGeracaoSeguro() {
        return this.dhGeracaoSeguro;
    }

    public void setDhGeracaoSeguro(DateTime dhGeracaoSeguro) {
        this.dhGeracaoSeguro = dhGeracaoSeguro;
    }

    public DateTime getDhGeracao() {
		return dhGeracao;
	}

	public void setDhGeracao(DateTime dhGeracao) {
		this.dhGeracao = dhGeracao;
	}

	public DateTime getDhSaidaColetaEntrega() {
        return this.dhSaidaColetaEntrega;
    }

    public void setDhSaidaColetaEntrega(DateTime dhSaidaColetaEntrega) {
        this.dhSaidaColetaEntrega = dhSaidaColetaEntrega;
    }

    public DateTime getDhChegadaColetaEntrega() {
        return this.dhChegadaColetaEntrega;
    }

    public void setDhChegadaColetaEntrega(DateTime dhChegadaColetaEntrega) {
        this.dhChegadaColetaEntrega = dhChegadaColetaEntrega;
    }

    public BigDecimal getPcOcupacaoCalculado() {
        return this.pcOcupacaoCalculado;
    }

    public void setPcOcupacaoCalculado(BigDecimal pcOcupacaoCalculado) {
        this.pcOcupacaoCalculado = pcOcupacaoCalculado;
    }

    public BigDecimal getPcOcupacaoAforadoCalculado() {
        return this.pcOcupacaoAforadoCalculado;
    }

	public void setPcOcupacaoAforadoCalculado(
			BigDecimal pcOcupacaoAforadoCalculado) {
        this.pcOcupacaoAforadoCalculado = pcOcupacaoAforadoCalculado;
    }

    public BigDecimal getPcOcupacaoInformado() {
        return this.pcOcupacaoInformado;
    }

    public void setPcOcupacaoInformado(BigDecimal pcOcupacaoInformado) {
        this.pcOcupacaoInformado = pcOcupacaoInformado;
    }

    public BigDecimal getPsTotalAforado() {
		return psTotalAforado;
	}

	public void setPsTotalAforado(BigDecimal psTotalAforado) {
		this.psTotalAforado = psTotalAforado;
	}

	public BigDecimal getPsAColetar() {
		return psAColetar;
	}

	public void setPsAColetar(BigDecimal psAColetar) {
		this.psAColetar = psAColetar;
	}

	public BigDecimal getPsAEntregar() {
		return psAEntregar;
	}

	public void setPsAEntregar(BigDecimal psAEntregar) {
		this.psAEntregar = psAEntregar;
	}

	public BigDecimal getPsColetado() {
		return psColetado;
	}

	public void setPsColetado(BigDecimal psColetado) {
		this.psColetado = psColetado;
	}

	public BigDecimal getPsEntregue() {
		return psEntregue;
	}

	public void setPsEntregue(BigDecimal psEntregue) {
		this.psEntregue = psEntregue;
	}

	public BigDecimal getPsTotalFrota() {
		return psTotalFrota;
	}

	public void setPsTotalFrota(BigDecimal psTotalFrota) {
		this.psTotalFrota = psTotalFrota;
	}

	public BigDecimal getVlAColetar() {
		return vlAColetar;
	}

	public void setVlAColetar(BigDecimal vlAColetar) {
		this.vlAColetar = vlAColetar;
	}

	public BigDecimal getVlAEntregar() {
		return vlAEntregar;
	}

	public void setVlAEntregar(BigDecimal vlAEntregar) {
		this.vlAEntregar = vlAEntregar;
	}

	public BigDecimal getVlColetado() {
		return vlColetado;
	}

	public void setVlColetado(BigDecimal vlColetado) {
		this.vlColetado = vlColetado;
	}

	public BigDecimal getVlEntregue() {
		return vlEntregue;
	}

	public void setVlEntregue(BigDecimal vlEntregue) {
		this.vlEntregue = vlEntregue;
	}

	public BigDecimal getVlTotalFrota() {
		return vlTotalFrota;
	}

	public void setVlTotalFrota(BigDecimal vlTotalFrota) {
		this.vlTotalFrota = vlTotalFrota;
	}
	
	public BigDecimal getPcFreteAgregado() {
		return pcFreteAgregado;
	}

	public void setPcFreteAgregado(BigDecimal pcFreteAgregado) {
		this.pcFreteAgregado = pcFreteAgregado;
	}

	public BigDecimal getPcFreteEventual() {
		return pcFreteEventual;
	}

	public void setPcFreteEventual(BigDecimal pcFreteEventual) {
		this.pcFreteEventual = pcFreteEventual;
	}

	public BigDecimal getPcFreteMercurio() {
		return pcFreteMercurio;
	}

	public void setPcFreteMercurio(BigDecimal pcFreteMercurio) {
		this.pcFreteMercurio = pcFreteMercurio;
	}

	public DateTime getDhPrevisaoSaida() {
		return dhPrevisaoSaida;
	}

	public void setDhPrevisaoSaida(DateTime dhPrevisaoSaida) {
		this.dhPrevisaoSaida = dhPrevisaoSaida;
	}

	public Integer getNrTempoViagem() {
		return nrTempoViagem;
	}

	public void setNrTempoViagem(Integer nrTempoViagem) {
		this.nrTempoViagem = nrTempoViagem;
	}
	
	public Boolean getBlEntregaDireta() {
		return blEntregaDireta;
	}

	public void setBlEntregaDireta(Boolean blEntregaDireta) {
		this.blEntregaDireta = blEntregaDireta;
	}

	public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporteByIdSemiRebocado() {
        return this.meioTransporteByIdSemiRebocado;
    }

	public void setMeioTransporteByIdSemiRebocado(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporteByIdSemiRebocado) {
        this.meioTransporteByIdSemiRebocado = meioTransporteByIdSemiRebocado;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporte getMeioTransporteByIdTransportado() {
        return this.meioTransporteByIdTransportado;
    }

	public void setMeioTransporteByIdTransportado(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporte meioTransporteByIdTransportado) {
        this.meioTransporteByIdTransportado = meioTransporteByIdTransportado;
    }

    public com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao getSolicitacaoContratacao() {
        return this.solicitacaoContratacao;
    }

	public void setSolicitacaoContratacao(
			com.mercurio.lms.contratacaoveiculos.model.SolicitacaoContratacao solicitacaoContratacao) {
        this.solicitacaoContratacao = solicitacaoContratacao;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega getTipoTabelaColetaEntrega() {
        return this.tipoTabelaColetaEntrega;
    }

	public void setTipoTabelaColetaEntrega(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega tipoTabelaColetaEntrega) {
        this.tipoTabelaColetaEntrega = tipoTabelaColetaEntrega;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega getTabelaColetaEntrega() {
        return this.tabelaColetaEntrega;
    }

	public void setTabelaColetaEntrega(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega tabelaColetaEntrega) {
        this.tabelaColetaEntrega = tabelaColetaEntrega;
    }

    public com.mercurio.lms.municipios.model.RotaIdaVolta getRotaIdaVolta() {
        return this.rotaIdaVolta;
    }

	public void setRotaIdaVolta(
			com.mercurio.lms.municipios.model.RotaIdaVolta rotaIdaVolta) {
        this.rotaIdaVolta = rotaIdaVolta;
    }
    
    public com.mercurio.lms.municipios.model.Rota getRota() {
        return this.rota;
    }

    public void setRota(com.mercurio.lms.municipios.model.Rota rota) {
        this.rota = rota;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Motorista getMotorista() {
        return this.motorista;
    }

	public void setMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista motorista) {
        this.motorista = motorista;
    }
	
    public com.mercurio.lms.contratacaoveiculos.model.Motorista getInstrutorMotorista() {
		return instrutorMotorista;
	}

	public void setInstrutorMotorista(
			com.mercurio.lms.contratacaoveiculos.model.Motorista instrutorMotorista) {
		this.instrutorMotorista = instrutorMotorista;
	}

	public com.mercurio.lms.carregamento.model.MotivoCancelamentoCc getMotivoCancelamentoCc() {
        return this.motivoCancelamentoCc;
    }

	public void setMotivoCancelamentoCc(
			com.mercurio.lms.carregamento.model.MotivoCancelamentoCc motivoCancelamentoCc) {
        this.motivoCancelamentoCc = motivoCancelamentoCc;
    }

    public com.mercurio.lms.contratacaoveiculos.model.Proprietario getProprietario() {
        return this.proprietario;
    }

	public void setProprietario(
			com.mercurio.lms.contratacaoveiculos.model.Proprietario proprietario) {
        this.proprietario = proprietario;
    }

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialAtualizaStatus() {
        return this.filialByIdFilialAtualizaStatus;
    }

	public void setFilialByIdFilialAtualizaStatus(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialAtualizaStatus) {
        this.filialByIdFilialAtualizaStatus = filialByIdFilialAtualizaStatus;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
        return this.filialByIdFilialDestino;
    }

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
        this.filialByIdFilialDestino = filialByIdFilialDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.FilialRotaCc.class)     
    public List<FilialRotaCc> getFilialRotaCcs() {
        return this.filialRotaCcs;
    }

    public void setFilialRotaCcs(List<FilialRotaCc> filialRotaCcs) {
        this.filialRotaCcs = filialRotaCcs;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ManifestoEletronico.class)
    public List<ManifestoEletronico> getManifestosEletronicos() {
		return manifestosEletronicos;
	}

	public void setManifestosEletronicos(
			List<ManifestoEletronico> manifestosEletronicos) {
		this.manifestosEletronicos = manifestosEletronicos;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicMonitPreventivo.class)     
    public List<SolicMonitPreventivo> getSolicMonitPreventivos() {
        return this.solicMonitPreventivos;
    }

	public void setSolicMonitPreventivos(
			List<SolicMonitPreventivo> solicMonitPreventivos) {
        this.solicMonitPreventivos = solicMonitPreventivos;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.Manifesto.class)     
    public List<Manifesto> getManifestos() {
        return this.manifestos;
    }

    public void setManifestos(List<Manifesto> manifestos) {
        this.manifestos = manifestos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.VeiculoControleCarga.class)     
    public List<VeiculoControleCarga> getVeiculoControleCargas() {
        return this.veiculoControleCargas;
    }

	public void setVeiculoControleCargas(
			List<VeiculoControleCarga> veiculoControleCargas) {
        this.veiculoControleCargas = veiculoControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.LacreControleCarga.class)     
    public List<LacreControleCarga> getLacreControleCargas() {
        return this.lacreControleCargas;
    }

	public void setLacreControleCargas(
			List<LacreControleCarga> lacreControleCargas) {
        this.lacreControleCargas = lacreControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoProprietarioCc.class)     
    public List<PagtoProprietarioCc> getPagtoProprietarioCcs() {
        return this.pagtoProprietarioCcs;
    }

	public void setPagtoProprietarioCcs(
			List<PagtoProprietarioCc> pagtoProprietarioCcs) {
        this.pagtoProprietarioCcs = pagtoProprietarioCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.coleta.model.ManifestoColeta.class)     
    public List<ManifestoColeta> getManifestoColetas() {
        return this.manifestoColetas;
    }

    public void setManifestoColetas(List<ManifestoColeta> manifestoColetas) {
        this.manifestoColetas = manifestoColetas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EstoqueDispositivoQtde.class)     
    public List<EstoqueDispositivoQtde> getEstoqueDispositivoQtdes() {
        return this.estoqueDispositivoQtdes;
    }

	public void setEstoqueDispositivoQtdes(
			List<EstoqueDispositivoQtde> estoqueDispositivoQtdes) {
        this.estoqueDispositivoQtdes = estoqueDispositivoQtdes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EstoqueDispIdentificado.class)     
    public List<EstoqueDispIdentificado> getEstoqueDispIdentificados() {
        return this.estoqueDispIdentificados;
    }

	public void setEstoqueDispIdentificados(
			List<EstoqueDispIdentificado> estoqueDispIdentificados) {
        this.estoqueDispIdentificados = estoqueDispIdentificados;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.ManifestoEntrega.class)     
    public List<ManifestoEntrega> getManifestoEntregas() {
        return this.manifestoEntregas;
    }

    public void setManifestoEntregas(List<ManifestoEntrega> manifestoEntregas) {
        this.manifestoEntregas = manifestoEntregas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.ControleQuilometragem.class)     
    public List<ControleQuilometragem> getControleQuilometragems() {
        return this.controleQuilometragems;
    }

	public void setControleQuilometragems(
			List<ControleQuilometragem> controleQuilometragems) {
        this.controleQuilometragems = controleQuilometragems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoPassagemCc.class)     
    public List<PostoPassagemCc> getPostoPassagemCcs() {
        return this.postoPassagemCcs;
    }

    public void setPostoPassagemCcs(List<PostoPassagemCc> postoPassagemCcs) {
        this.postoPassagemCcs = postoPassagemCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PostoAvancadoCc.class)     
    public List<PostoAvancadoCc> getPostoAvancadoCcs() {
        return this.postoAvancadoCcs;
    }

    public void setPostoAvancadoCcs(List<PostoAvancadoCc> postoAvancadoCcs) {
        this.postoAvancadoCcs = postoAvancadoCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EventoControleCarga.class)     
    public List<EventoControleCarga> getEventoControleCargas() {
        return this.eventoControleCargas;
    }

	public void setEventoControleCargas(
			List<EventoControleCarga> eventoControleCargas) {
        this.eventoControleCargas = eventoControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade.class)     
    public List<OcorrenciaNaoConformidade> getOcorrenciaNaoConformidades() {
        return this.ocorrenciaNaoConformidades;
    }

	public void setOcorrenciaNaoConformidades(
			List<OcorrenciaNaoConformidade> ocorrenciaNaoConformidades) {
        this.ocorrenciaNaoConformidades = ocorrenciaNaoConformidades;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.ControleTrecho.class)     
    public List<ControleTrecho> getControleTrechos() {
        return this.controleTrechos;
    }

    public void setControleTrechos(List<ControleTrecho> controleTrechos) {
        this.controleTrechos = controleTrechos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.ExigenciaIndicada.class)     
    public List<ExigenciaIndicada> getExigenciaIndicadas() {
        return this.exigenciaIndicadas;
    }

    public void setExigenciaIndicadas(List<ExigenciaIndicada> exigenciaIndicadas) {
        this.exigenciaIndicadas = exigenciaIndicadas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.MotoristaControleCarga.class)     
    public List<MotoristaControleCarga> getMotoristaControleCargas() {
        return this.motoristaControleCargas;
    }

	public void setMotoristaControleCargas(
			List<MotoristaControleCarga> motoristaControleCargas) {
        this.motoristaControleCargas = motoristaControleCargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.PagtoPedagioCc.class)     
    public List<PagtoPedagioCc> getPagtoPedagioCcs() {
        return this.pagtoPedagioCcs;
    }

    public void setPagtoPedagioCcs(List<PagtoPedagioCc> pagtoPedagioCcs) {
        this.pagtoPedagioCcs = pagtoPedagioCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.CarregamentoDescarga.class)     
    public List<CarregamentoDescarga> getCarregamentoDescargas() {
        return this.carregamentoDescargas;
    }

	public void setCarregamentoDescargas(
			List<CarregamentoDescarga> carregamentoDescargas) {
        this.carregamentoDescargas = carregamentoDescargas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.SolicitacaoSinal.class)     
    public List<SolicitacaoSinal> getSolicitacaoSinais() {
        return this.solicitacaoSinais;
    }

    public void setSolicitacaoSinais(List<SolicitacaoSinal> solicitacaoSinais) {
        this.solicitacaoSinais = solicitacaoSinais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro.class)     
    public List<ReciboFreteCarreteiro> getReciboFreteCarreteiros() {
        return this.reciboFreteCarreteiros;
    }

	public void setReciboFreteCarreteiros(
			List<ReciboFreteCarreteiro> reciboFreteCarreteiros) {
        this.reciboFreteCarreteiros = reciboFreteCarreteiros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ProcessoSinistro.class)     
    public List<ProcessoSinistro> getProcessoSinistros() {
        return this.processoSinistros;
    }

    public void setProcessoSinistros(List<ProcessoSinistro> processoSinistros) {
        this.processoSinistros = processoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EquipeOperacao.class)     
    public List<EquipeOperacao> getEquipeOperacoes() {
        return this.equipeOperacoes;
    }

    public void setEquipeOperacoes(List<EquipeOperacao> equipeOperacoes) {
        this.equipeOperacoes = equipeOperacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.SemiReboqueCc.class)     
    public List<SemiReboqueCc> getSemiReboqueCcs() {
        return this.semiReboqueCcs;
    }

    public void setSemiReboqueCcs(List<SemiReboqueCc> semiReboqueCcs) {
        this.semiReboqueCcs = semiReboqueCcs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.Mir.class)     
    public List<Mir> getMirs() {
        return this.mirs;
    }

    public void setMirs(List<Mir> mirs) {
        this.mirs = mirs;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.sgr.model.ValorEscolta.class)     
    public List<ValorEscolta> getValorEscoltas() {
        return this.valorEscoltas;
    }

    public void setValorEscoltas(List<ValorEscolta> valorEscoltas) {
        this.valorEscoltas = valorEscoltas;
    }

    public List<NotaCredito> getNotasCredito() {
        return notasCredito;
    }

    public void setNotasCredito(List<NotaCredito> notasCredito) {
        this.notasCredito = notasCredito;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idControleCarga",
				getIdControleCarga()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_CONTROLE_CARGA", idControleCarga)
				.append("ID_FILIAL_ORIGEM", filialByIdFilialOrigem != null ? filialByIdFilialOrigem.getIdFilial() : null)
				.append("ID_FILIAL_ATUALIZA_STATUS", filialByIdFilialAtualizaStatus != null ? filialByIdFilialAtualizaStatus.getIdFilial() : null)
				.append("ID_MOTORISTA", motorista != null ? motorista.getIdMotorista() : null)
				.append("ID_PROPRIETARIO", proprietario != null ? proprietario.getIdProprietario() : null)
				.append("NR_CONTROLE_CARGA", nrControleCarga)
				.append("TP_CONTROLE_CARGA", tpControleCarga != null ? tpControleCarga.getValue() : null)
				.append("TP_STATUS_CONTROLE_CARGA", tpStatusControleCarga != null ? tpStatusControleCarga.getValue() : null)
				.append("VL_PEDAGIO", vlPedagio)
				.append("ID_FILIAL_DESTINO", filialByIdFilialDestino != null ? filialByIdFilialDestino.getIdFilial() : null)
				.append("ID_SOLICITACAO_CONTRATACAO", solicitacaoContratacao != null ? solicitacaoContratacao.getIdSolicitacaoContratacao() : null)
				.append("ID_TRANSPORTADO", meioTransporteByIdTransportado != null ? meioTransporteByIdTransportado.getIdMeioTransporte() : null)
				.append("ID_SEMI_REBOCADO", meioTransporteByIdSemiRebocado != null ? meioTransporteByIdSemiRebocado.getIdMeioTransporte() : null)
				.append("ID_ROTA_COLETA_ENTREGA", rotaColetaEntrega != null ? rotaColetaEntrega.getIdRotaColetaEntrega() : null)
				.append("ID_ROTA_IDA_VOLTA", rotaIdaVolta != null ? rotaIdaVolta.getIdRotaIdaVolta() : null)
				.append("ID_MOTIVO_CANCELAMENTO_CC", motivoCancelamentoCc != null ? motivoCancelamentoCc.getIdMotivoCancelamentoCc() : null)
				.append("ID_TABELA_COLETA_ENTREGA", tabelaColetaEntrega != null ? tabelaColetaEntrega.getIdTabelaColetaEntrega() : null)
				.append("ID_TIPO_TABELA_COLETA_ENTREGA", tipoTabelaColetaEntrega != null ? tipoTabelaColetaEntrega.getIdTipoTabelaColetaEntrega() : null)
				.append("ID_MOEDA", moeda != null ? moeda.getIdMoeda() : null)
				.append("VL_FRETE_CARRETEIRO", vlFreteCarreteiro)
				.append("DS_SENHA_CELULAR_VEICULO", dsSenhaCelularVeiculo)
				.append("DH_GERACAO_SEGURO", dhGeracaoSeguro)
				.append("DH_SAIDA_COLETA_ENTREGA", dhSaidaColetaEntrega)
				.append("DH_CHEGADA_COLETA_ENTREGA", dhChegadaColetaEntrega)
				.append("PC_OCUPACAO_CALCULADO", pcOcupacaoCalculado)
				.append("PC_OCUPACAO_AFORADO_CALCULADO", pcOcupacaoAforadoCalculado)
				.append("PC_OCUPACAO_INFORMADO", pcOcupacaoInformado)
				.append("PS_TOTAL_FROTA", psTotalFrota)
				.append("PS_TOTAL_AFORADO", psTotalAforado)
				.append("PS_COLETADO", psColetado)
				.append("PS_A_COLETAR", psAColetar)
				.append("PS_ENTREGUE", psEntregue)
				.append("PS_A_ENTREGAR", psAEntregar)
				.append("VL_TOTAL_FROTA", vlTotalFrota)
				.append("VL_COLETADO", vlColetado)
				.append("VL_A_COLETAR", vlAColetar)
				.append("VL_ENTREGUE", vlEntregue)
				.append("VL_A_ENTREGAR", vlAEntregar)
				.append("ID_NOTA_CREDITO", notaCredito != null ? notaCredito.getIdNotaCredito() : null)
				.append("PC_FRETE_AGREGADO", pcFreteAgregado)
				.append("PC_FRETE_EVENTUAL", pcFreteEventual)
				.append("PC_FRETE_MERCURIO", pcFreteMercurio)
				.append("ID_ROTA", rota != null ? rota.getIdRota() : null)
				.append("TP_ROTA_VIAGEM", tpRotaViagem != null ? tpRotaViagem.getValue() : null)
				.append("DH_GERACAO", dhGeracao)
				.append("DH_PREVISAO_SAIDA", dhPrevisaoSaida)
				.append("NR_TEMPO_VIAGEM", nrTempoViagem)
				.append("BL_ENTREGA_DIRETA", blEntregaDireta)
				.append("NR_MANIF", nrManif)
				.append("NR_SMP", nrSMP)
				.append("ID_INSTRUTOR_MOTORISTA", instrutorMotorista != null ? instrutorMotorista.getIdMotorista() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ControleCarga))
			return false;
        ControleCarga castOther = (ControleCarga) other;
		return new EqualsBuilder().append(this.getIdControleCarga(),
				castOther.getIdControleCarga()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdControleCarga()).toHashCode();
    }

    public List<TabelaColetaEntregaCC> getTabelasColetaEntregaCC() {
        return tabelasColetaEntregaCC;
}

    public void setTabelasColetaEntregaCC(List<TabelaColetaEntregaCC> tabelasColetaEntregaCC) {
        this.tabelasColetaEntregaCC = tabelasColetaEntregaCC;
    }

	public void setNotaCredito(NotaCredito notaCredito) {
		this.notaCredito = notaCredito;
	}

	public NotaCredito getNotaCredito() {
		return notaCredito;
	}

	public Boolean getBlExigeCIOT() {
		return blExigeCIOT;
	}

	public void setBlExigeCIOT(Boolean blExigeCIOT) {
		this.blExigeCIOT = blExigeCIOT;
	}

	public Boolean getBlEnviadoIntegCIOT() {
		return blEnviadoIntegCIOT;
	}

	public void setBlEnviadoIntegCIOT(Boolean blEnviadoIntegCIOT) {
		this.blEnviadoIntegCIOT = blEnviadoIntegCIOT;
	}

}
