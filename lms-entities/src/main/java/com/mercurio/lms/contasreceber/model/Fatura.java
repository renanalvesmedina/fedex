package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Servico;

/** @author LMS Custom Hibernate CodeGenerator */
public class Fatura implements Serializable {

	private static final long serialVersionUID = 2L;

    /** identifier field */
    private Long idFatura;
    
    private Integer versao;

    /** persistent field */
    private Long nrFatura;

    /** persistent field */
    private Integer qtDocumentos;

    /** persistent field */
    private BigDecimal vlBaseCalcPisCofinsCsll;

    /** persistent field */
    private BigDecimal vlBaseCalcIr;

    /** persistent field */
    private BigDecimal vlPis;

    /** persistent field */
    private BigDecimal vlCofins;

    /** persistent field */
    private BigDecimal vlCsll;

    /** persistent field */
    private BigDecimal vlIr;

    /** persistent field */
    private BigDecimal vlIva;

    /** persistent field */
    private BigDecimal vlTotal;

    /** persistent field */
    private BigDecimal vlDesconto;

    /** persistent field */
    private BigDecimal vlTotalRecebido;

    /** persistent field */
    private BigDecimal vlJuroCalculado;

    /** persistent field */
    private BigDecimal vlJuroRecebido;

    /** persistent field */
    private BigDecimal vlCotacaoMoeda;

    /** persistent field */
    private YearMonthDay dtEmissao;

    /** persistent field */
    private YearMonthDay dtVencimento;
    
    /** persistent field */
    private YearMonthDay dtTransmissaoEdi;

    /** persistent field */
    private Boolean blGerarEdi;

    /** persistent field */
    private Boolean blGerarBoleto;

    /** persistent field */
    private Boolean blFaturaReemitida;

    /** persistent field */
    private Boolean blIndicadorImpressao;

    /** persistent field */
    private DomainValue tpFatura;

    /** persistent field */
    private DomainValue tpSituacaoAprovacao;

    /** persistent field */
    private DomainValue tpSituacaoFatura;

    /** persistent field */
    private DomainValue tpOrigem;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private DomainValue tpModal;
    
	/** persistent field */
	private DomainValue tpFrete;    

    /** nullable persistent field */
    private YearMonthDay dtLiquidacao;

    /** nullable persistent field */
    private DateTime dhTransmissao;

    /** nullable persistent field */
    private DateTime dhReemissao;
    
    private DateTime dhEnvioCobTerceira;
    
    private DateTime dhPagtoCobTerceira;
    
    private DateTime dhDevolCobTerceira;

    /** nullable persistent field */
    private String nrPreFatura;

    /** nullable persistent field */
    private String obFatura;

    private Boolean blOcorrenciaCorp;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.CotacaoMoeda cotacaoMoeda;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;    
    
    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Fatura fatura;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.RelacaoCobranca relacaoCobranca;

    /** persistent field */
    private com.mercurio.lms.vendas.model.TipoAgrupamento tipoAgrupamento;

    /** persistent field */
    private com.mercurio.lms.vendas.model.AgrupamentoCliente agrupamentoCliente;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;

    /** persistent field */
    private com.mercurio.lms.entrega.model.ManifestoEntrega manifestoEntrega;

    /** persistent field */
    private com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilial;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialCobradora;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDebitada;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Cedente cedente;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendencia;
    
    /** persistent field */
    private Long idPendenciaDesconto; 
    
	/** persistent field */
	private Servico servico;  
	
	/** nullable persistent field */
	private DomainValue tpSetorCausadorAbatimento; 
	
	/** nullable persistent field */
	private String obAcaoCorretiva;
	
	/** nullable persistent field */
	private MotivoDesconto motivoDesconto;
		
    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifestoOrigem;
 
    /** persistent field */
    private com.mercurio.lms.entrega.model.ManifestoEntrega manifestoEntregaOrigem;	
    
    /** persistent field */
    private Boleto boleto;
    
    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Recibo recibo;
    
    /** persistent field */
    private com.mercurio.lms.contasreceber.model.Redeco redeco;
    
    /** persistent field */
    private com.mercurio.lms.contasreceber.model.NotaDebitoNacional notaDebitoNacional;    
	
    /** persistent field */
    private List repositorioItemRedecos;

    /** persistent field */
    private List itemDepositoCcorrentes;

    /** persistent field */
    private List chequeFaturas;

    /** persistent field */
    private List itemRedecos;

    /** persistent field */
    private List itemFaturas;

    /** persistent field */
    private List itemCobrancas;

    /** persistent field */
    private List faturaRecibos;

    /** persistent field */
    private List faturas;

    /** persistent field */
    private List boletos;

    /** persistent field */
    private List itemNotaDebitoNacionais;

    private List anexos;

    private Boolean blConhecimentoResumo;
    
    private DateTime dhNegativacaoSerasa;
    
    private DateTime dhExclusaoSerasa;
    
    /** persistent field */
    private List itemLoteSerasa;
    
    private Boolean blCancelaFaturaInteira;

    /** persistent field */
    private YearMonthDay dtPreFatura;

    /** persistent field */
    private YearMonthDay dtImpotacao;

    /** persistent field */
    private YearMonthDay dtEnvioAceite;

    /** persistent field */
    private YearMonthDay dtRetornoAceite;
    
    public Fatura() {
    	super();
    	setBlCancelaFaturaInteira(false);
    }
    
    public Long getIdFatura() {
        return this.idFatura;
    }

    public void setIdFatura(Long idFatura) {
        this.idFatura = idFatura;
    }

    public Long getNrFatura() {
        return this.nrFatura;
    }

    public void setNrFatura(Long nrFatura) {
        this.nrFatura = nrFatura;
    }

    public Integer getQtDocumentos() {
        return this.qtDocumentos;
    }

    public void setQtDocumentos(Integer qtDocumentos) {
        this.qtDocumentos = qtDocumentos;
    }

    public BigDecimal getVlBaseCalcPisCofinsCsll() {
        return this.vlBaseCalcPisCofinsCsll;
    }

    public void setVlBaseCalcPisCofinsCsll(BigDecimal vlBaseCalcPisCofinsCsll) {
        this.vlBaseCalcPisCofinsCsll = vlBaseCalcPisCofinsCsll;
    }

    public BigDecimal getVlBaseCalcIr() {
        return this.vlBaseCalcIr;
    }

    public void setVlBaseCalcIr(BigDecimal vlBaseCalcIr) {
        this.vlBaseCalcIr = vlBaseCalcIr;
    }

    public BigDecimal getVlPis() {
        return this.vlPis;
    }

    public void setVlPis(BigDecimal vlPis) {
        this.vlPis = vlPis;
    }

    public BigDecimal getVlCofins() {
        return this.vlCofins;
    }

    public void setVlCofins(BigDecimal vlCofins) {
        this.vlCofins = vlCofins;
    }

    public BigDecimal getVlCsll() {
        return this.vlCsll;
    }

    public void setVlCsll(BigDecimal vlCsll) {
        this.vlCsll = vlCsll;
    }

    public BigDecimal getVlIr() {
        return this.vlIr;
    }

    public void setVlIr(BigDecimal vlIr) {
        this.vlIr = vlIr;
    }

    public BigDecimal getVlIva() {
        return this.vlIva;
    }

    public void setVlIva(BigDecimal vlIva) {
        this.vlIva = vlIva;
    }

    public BigDecimal getVlTotal() {
        return this.vlTotal;
    }

    public void setVlTotal(BigDecimal vlTotal) {
        this.vlTotal = vlTotal;
    }

    public BigDecimal getVlDesconto() {
        return this.vlDesconto;
    }

    public void setVlDesconto(BigDecimal vlDesconto) {
        this.vlDesconto = vlDesconto;
    }

    public BigDecimal getVlTotalRecebido() {
        return this.vlTotalRecebido;
    }

    public void setVlTotalRecebido(BigDecimal vlTotalRecebido) {
        this.vlTotalRecebido = vlTotalRecebido;
    }

    public BigDecimal getVlJuroCalculado() {
        return this.vlJuroCalculado;
    }

    public void setVlJuroCalculado(BigDecimal vlJuroCalculado) {
        this.vlJuroCalculado = vlJuroCalculado;
    }

    public BigDecimal getVlJuroRecebido() {
        return this.vlJuroRecebido;
    }

    public void setVlJuroRecebido(BigDecimal vlJuroRecebido) {
        this.vlJuroRecebido = vlJuroRecebido;
    }

    public BigDecimal getVlCotacaoMoeda() {
        return this.vlCotacaoMoeda;
    }

    public void setVlCotacaoMoeda(BigDecimal vlCotacaoMoeda) {
        this.vlCotacaoMoeda = vlCotacaoMoeda;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public YearMonthDay getDtVencimento() {
        return this.dtVencimento;
    }

    public void setDtVencimento(YearMonthDay dtVencimento) {
        this.dtVencimento = dtVencimento;
    }

    public Boolean getBlGerarEdi() {
        return this.blGerarEdi;
    }

    public void setBlGerarEdi(Boolean blGerarEdi) {
        this.blGerarEdi = blGerarEdi;
    }

    public Boolean getBlGerarBoleto() {
        return this.blGerarBoleto;
    }

    public void setBlGerarBoleto(Boolean blGerarBoleto) {
        this.blGerarBoleto = blGerarBoleto;
    }

    public Boolean getBlFaturaReemitida() {
        return this.blFaturaReemitida;
    }

    public void setBlFaturaReemitida(Boolean blFaturaReemitida) {
        this.blFaturaReemitida = blFaturaReemitida;
    }

    public Boolean getBlIndicadorImpressao() {
        return this.blIndicadorImpressao;
    }

    public void setBlIndicadorImpressao(Boolean blIndicadorImpressao) {
        this.blIndicadorImpressao = blIndicadorImpressao;
    }

    public DomainValue getTpFatura() {
        return this.tpFatura;
    }

    public void setTpFatura(DomainValue tpFatura) {
        this.tpFatura = tpFatura;
    }

    public DomainValue getTpSituacaoAprovacao() {
        return this.tpSituacaoAprovacao;
    }

    public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
        this.tpSituacaoAprovacao = tpSituacaoAprovacao;
    }

    public DomainValue getTpSituacaoFatura() {
        return this.tpSituacaoFatura;
    }

    public void setTpSituacaoFatura(DomainValue tpSituacaoFatura) {
        this.tpSituacaoFatura = tpSituacaoFatura;
    }

    public DomainValue getTpOrigem() {
        return this.tpOrigem;
    }

    public void setTpOrigem(DomainValue tpOrigem) {
        this.tpOrigem = tpOrigem;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public YearMonthDay getDtLiquidacao() {
        return this.dtLiquidacao;
    }

    public void setDtLiquidacao(YearMonthDay dtLiquidacao) {
        this.dtLiquidacao = dtLiquidacao;
    }

    public DateTime getDhReemissao() {
        return this.dhReemissao;
    }

    public void setDhReemissao(DateTime dhReemissao) {
        this.dhReemissao = dhReemissao;
    }

    public String getNrPreFatura() {
        return this.nrPreFatura;
    }

    public void setNrPreFatura(String nrPreFatura) {
        this.nrPreFatura = nrPreFatura;
    }

    public String getObFatura() {
        return this.obFatura;
    }

    public void setObFatura(String obFatura) {
        this.obFatura = obFatura;
    }

    public com.mercurio.lms.configuracoes.model.CotacaoMoeda getCotacaoMoeda() {
        return this.cotacaoMoeda;
    }

	public void setCotacaoMoeda(
			com.mercurio.lms.configuracoes.model.CotacaoMoeda cotacaoMoeda) {
        this.cotacaoMoeda = cotacaoMoeda;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.contasreceber.model.Fatura getFatura() {
        return this.fatura;
    }

    public void setFatura(com.mercurio.lms.contasreceber.model.Fatura fatura) {
        this.fatura = fatura;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contasreceber.model.RelacaoCobranca getRelacaoCobranca() {
        return this.relacaoCobranca;
    }

	public void setRelacaoCobranca(
			com.mercurio.lms.contasreceber.model.RelacaoCobranca relacaoCobranca) {
        this.relacaoCobranca = relacaoCobranca;
    }

    public com.mercurio.lms.vendas.model.TipoAgrupamento getTipoAgrupamento() {
        return this.tipoAgrupamento;
    }

	public void setTipoAgrupamento(
			com.mercurio.lms.vendas.model.TipoAgrupamento tipoAgrupamento) {
        this.tipoAgrupamento = tipoAgrupamento;
    }

    public com.mercurio.lms.vendas.model.AgrupamentoCliente getAgrupamentoCliente() {
        return this.agrupamentoCliente;
    }

	public void setAgrupamentoCliente(
			com.mercurio.lms.vendas.model.AgrupamentoCliente agrupamentoCliente) {
        this.agrupamentoCliente = agrupamentoCliente;
    }

    public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
        return this.manifesto;
    }

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
        this.manifesto = manifesto;
    }

    public com.mercurio.lms.entrega.model.ManifestoEntrega getManifestoEntrega() {
        return this.manifestoEntrega;
    }

	public void setManifestoEntrega(
			com.mercurio.lms.entrega.model.ManifestoEntrega manifestoEntrega) {
        this.manifestoEntrega = manifestoEntrega;
    }

    public com.mercurio.lms.vendas.model.DivisaoCliente getDivisaoCliente() {
        return this.divisaoCliente;
    }

	public void setDivisaoCliente(
			com.mercurio.lms.vendas.model.DivisaoCliente divisaoCliente) {
        this.divisaoCliente = divisaoCliente;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilial() {
        return this.filialByIdFilial;
    }

	public void setFilialByIdFilial(
			com.mercurio.lms.municipios.model.Filial filialByIdFilial) {
        this.filialByIdFilial = filialByIdFilial;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialCobradora() {
        return this.filialByIdFilialCobradora;
    }

	public void setFilialByIdFilialCobradora(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialCobradora) {
        this.filialByIdFilialCobradora = filialByIdFilialCobradora;
    }

    public com.mercurio.lms.contasreceber.model.Cedente getCedente() {
        return this.cedente;
    }

    public void setCedente(com.mercurio.lms.contasreceber.model.Cedente cedente) {
        this.cedente = cedente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.RepositorioItemRedeco.class)     
    public List getRepositorioItemRedecos() {
        return this.repositorioItemRedecos;
    }

    public void setRepositorioItemRedecos(List repositorioItemRedecos) {
        this.repositorioItemRedecos = repositorioItemRedecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente.class)     
    public List getItemDepositoCcorrentes() {
        return this.itemDepositoCcorrentes;
    }

    public void setItemDepositoCcorrentes(List itemDepositoCcorrentes) {
        this.itemDepositoCcorrentes = itemDepositoCcorrentes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ChequeFatura.class)     
    public List getChequeFaturas() {
        return this.chequeFaturas;
    }

    public void setChequeFaturas(List chequeFaturas) {
        this.chequeFaturas = chequeFaturas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemRedeco.class)     
    public List getItemRedecos() {
        return this.itemRedecos;
    }

    public void setItemRedecos(List itemRedecos) {
        this.itemRedecos = itemRedecos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemFatura.class)     
    public List getItemFaturas() {
        return this.itemFaturas;
    }

    public void setItemFaturas(List itemFaturas) {
        this.itemFaturas = itemFaturas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemCobranca.class)     
    public List getItemCobrancas() {
        return this.itemCobrancas;
    }

    public void setItemCobrancas(List itemCobrancas) {
        this.itemCobrancas = itemCobrancas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.FaturaRecibo.class)     
    public List getFaturaRecibos() {
        return this.faturaRecibos;
    }

    public void setFaturaRecibos(List faturaRecibos) {
        this.faturaRecibos = faturaRecibos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.Fatura.class)     
    public List getFaturas() {
        return this.faturas;
    }

    public void setFaturas(List faturas) {
        this.faturas = faturas;
    }

    @ParametrizedAttribute(type = Boleto.class)     
    public List getBoletos() {
        return this.boletos;
    }

    public void setBoletos(List boletos) {
        this.boletos = boletos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.ItemNotaDebitoNacional.class)     
    public List getItemNotaDebitoNacionais() {
        return this.itemNotaDebitoNacionais;
    }

    public void setItemNotaDebitoNacionais(List itemNotaDebitoNacionais) {
        this.itemNotaDebitoNacionais = itemNotaDebitoNacionais;
    }

    @Override
	public String toString() {
		return new ToStringBuilder(this).append("idFatura", getIdFatura())
            .toString();
    }

    @Override
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Fatura))
			return false;
        Fatura castOther = (Fatura) other;
		return new EqualsBuilder().append(this.getIdFatura(),
				castOther.getIdFatura()).isEquals();
    }

    @Override
	public int hashCode() {
		return new HashCodeBuilder().append(getIdFatura()).toHashCode();
    }

	public DateTime getDhTransmissao() {
		return dhTransmissao;
	}

	public void setDhTransmissao(DateTime dhTransmissao) {
		this.dhTransmissao = dhTransmissao;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
		this.moeda = moeda;
	}

	public YearMonthDay getDtTransmissaoEdi() {
		return dtTransmissaoEdi;
	}

	public void setDtTransmissaoEdi(YearMonthDay dtTransmissaoEdi) {
		this.dtTransmissaoEdi = dtTransmissaoEdi;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public DomainValue getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public com.mercurio.lms.entrega.model.ManifestoEntrega getManifestoEntregaOrigem() {
		return manifestoEntregaOrigem;
	}

	public void setManifestoEntregaOrigem(
			com.mercurio.lms.entrega.model.ManifestoEntrega manifestoEntregaOrigem) {
		this.manifestoEntregaOrigem = manifestoEntregaOrigem;
	}

	public com.mercurio.lms.carregamento.model.Manifesto getManifestoOrigem() {
		return manifestoOrigem;
	}

	public void setManifestoOrigem(
			com.mercurio.lms.carregamento.model.Manifesto manifestoOrigem) {
		this.manifestoOrigem = manifestoOrigem;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public com.mercurio.lms.contasreceber.model.NotaDebitoNacional getNotaDebitoNacional() {
		return notaDebitoNacional;
	}

	public void setNotaDebitoNacional(
			com.mercurio.lms.contasreceber.model.NotaDebitoNacional notaDebitoNacional) {
		this.notaDebitoNacional = notaDebitoNacional;
	}

	public com.mercurio.lms.contasreceber.model.Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(com.mercurio.lms.contasreceber.model.Recibo recibo) {
		this.recibo = recibo;
	}

	public com.mercurio.lms.contasreceber.model.Redeco getRedeco() {
		return redeco;
	}

	public void setRedeco(com.mercurio.lms.contasreceber.model.Redeco redeco) {
		this.redeco = redeco;
	}

	public void setTpSetorCausadorAbatimento(
			DomainValue tpSetorCausadorAbatimento) {
		this.tpSetorCausadorAbatimento = tpSetorCausadorAbatimento;
}

	public DomainValue getTpSetorCausadorAbatimento() {
		return tpSetorCausadorAbatimento;
	}

	public void setObAcaoCorretiva(String obAcaoCorretiva) {
		this.obAcaoCorretiva = obAcaoCorretiva;
	}

	public String getObAcaoCorretiva() {
		return obAcaoCorretiva;
	}

	public void setMotivoDesconto(MotivoDesconto motivoDesconto) {
		this.motivoDesconto = motivoDesconto;
	}

	public MotivoDesconto getMotivoDesconto() {
		return motivoDesconto;
	}

	public void setIdPendenciaDesconto(Long idPendenciaDesconto) {
		this.idPendenciaDesconto = idPendenciaDesconto;
	}

	public Long getIdPendenciaDesconto() {
		return idPendenciaDesconto;
	}

	public void setAnexos(List anexos) {
		this.anexos = anexos;
	}

	public List getAnexos() {
		return anexos;
	}

	public Boolean getBlConhecimentoResumo() {
		return blConhecimentoResumo;
	}

	public void setBlConhecimentoResumo(Boolean blConhecimentoResumo) {
		this.blConhecimentoResumo = blConhecimentoResumo;
}

	public Boolean getBlOcorrenciaCorp() {
    	return blOcorrenciaCorp;
}

	public void setBlOcorrenciaCorp(Boolean blOcorrenciaCorp) {
    	this.blOcorrenciaCorp = blOcorrenciaCorp;
    }

	public List getItemLoteSerasa() {
		return itemLoteSerasa;
}

	public void setItemLoteSerasa(List itemLoteSerasa) {
		this.itemLoteSerasa = itemLoteSerasa;
	}

	public DateTime getDhNegativacaoSerasa() {
		return dhNegativacaoSerasa;
	}

	public void setDhNegativacaoSerasa(DateTime dhNegativacaoSerasa) {
		this.dhNegativacaoSerasa = dhNegativacaoSerasa;
	}

	public DateTime getDhExclusaoSerasa() {
		return dhExclusaoSerasa;
	}

	public void setDhExclusaoSerasa(DateTime dhExclusaoSerasa) {
		this.dhExclusaoSerasa = dhExclusaoSerasa;
	}

	public Boolean getBlCancelaFaturaInteira() {
		return blCancelaFaturaInteira;
}

	public void setBlCancelaFaturaInteira(Boolean blCancelaFaturaInteira) {
		this.blCancelaFaturaInteira = blCancelaFaturaInteira;
	}

	public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDebitada() {
		return filialByIdFilialDebitada;
}

	public void setFilialByIdFilialDebitada(com.mercurio.lms.municipios.model.Filial filialByIdFilialDebitada) {
		this.filialByIdFilialDebitada = filialByIdFilialDebitada;
	}

	public void setDhEnvioCobTerceira(DateTime dhEnvioCobTerceira) {
		this.dhEnvioCobTerceira = dhEnvioCobTerceira;
	}
	public DateTime getDhEnvioCobTerceira() {
		return dhEnvioCobTerceira;
	}

	public DateTime getDhPagtoCobTerceira() {
		return dhPagtoCobTerceira;
	}

	public void setDhPagtoCobTerceira(DateTime dhPagtoCobTerceira) {
		this.dhPagtoCobTerceira = dhPagtoCobTerceira;
	}

	public DateTime getDhDevolCobTerceira() {
		return dhDevolCobTerceira;
	}

	public void setDhDevolCobTerceira(DateTime dhDevolCobTerceira) {
		this.dhDevolCobTerceira = dhDevolCobTerceira;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public YearMonthDay getDtPreFatura() {
		return dtPreFatura;
	}

	public void setDtPreFatura(YearMonthDay dtPreFatura) {
		this.dtPreFatura = dtPreFatura;
	}

	public YearMonthDay getDtImpotacao() {
		return dtImpotacao;
	}

	public void setDtImpotacao(YearMonthDay dtImpotacao) {
		this.dtImpotacao = dtImpotacao;
	}

	public YearMonthDay getDtEnvioAceite() {
		return dtEnvioAceite;
	}

	public void setDtEnvioAceite(YearMonthDay dtEnvioAceite) {
		this.dtEnvioAceite = dtEnvioAceite;
	}

	public YearMonthDay getDtRetornoAceite() {
		return dtRetornoAceite;
	}

	public void setDtRetornoAceite(YearMonthDay dtRetornoAceite) {
		this.dtRetornoAceite = dtRetornoAceite;
	}
	
}
