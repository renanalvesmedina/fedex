package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.AgrupamentoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TipoAgrupamento;
import com.mercurio.lms.workflow.model.Pendencia;

public class FaturaLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Fatura fatura;
    
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

    /** nullable persistent field */
    private String nrPreFatura;

    /** nullable persistent field */
    private String obFatura;

    /** persistent field */
    private CotacaoMoeda cotacaoMoeda;

    /** persistent field */
    private Moeda moeda;    
    
    /** persistent field */
    private Cliente cliente;

    /** persistent field */
    private Fatura faturaOriginal;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private RelacaoCobranca relacaoCobranca;

    /** persistent field */
    private TipoAgrupamento tipoAgrupamento;

    /** persistent field */
    private AgrupamentoCliente agrupamentoCliente;

    /** persistent field */
    private Manifesto manifesto;

    /** persistent field */
    private ManifestoEntrega manifestoEntrega;

    /** persistent field */
    private DivisaoCliente divisaoCliente;

    /** persistent field */
    private Filial filial;

    /** persistent field */
    private Filial filialCobradora;

    /** persistent field */
    private Cedente cedente;
    
    /** persistent field */
    private Pendencia pendencia;
    
    /** persistent field */
    private Pendencia pendenciaDesconto; 
    
	/** persistent field */
	private Servico servico;  
	
    /** persistent field */
    private Manifesto manifestoOrigem;
 
    /** persistent field */
    private Manifesto manifestoEntregaOrigem;	
    
    /** persistent field */
    private Boleto boleto;
    
    /** persistent field */
    private Recibo recibo;
    
    /** persistent field */
    private Redeco redeco;
    
    /** persistent field */
    private NotaDebitoNacional notaDebitoNacional;
    
    private Long idFaturaLog;
    
    private String loginLog;
    
    private DateTime dhLog;
    
    private String opLog;
    
    private List itensFaturaLog;
	
    public Boolean getBlFaturaReemitida() {
		return blFaturaReemitida;
	}

	public void setBlFaturaReemitida(Boolean blFaturaReemitida) {
		this.blFaturaReemitida = blFaturaReemitida;
	}

	public Boolean getBlGerarBoleto() {
		return blGerarBoleto;
	}

	public void setBlGerarBoleto(Boolean blGerarBoleto) {
		this.blGerarBoleto = blGerarBoleto;
	}

	public Boolean getBlGerarEdi() {
		return blGerarEdi;
	}

	public void setBlGerarEdi(Boolean blGerarEdi) {
		this.blGerarEdi = blGerarEdi;
	}

	public Boolean getBlIndicadorImpressao() {
		return blIndicadorImpressao;
	}

	public void setBlIndicadorImpressao(Boolean blIndicadorImpressao) {
		this.blIndicadorImpressao = blIndicadorImpressao;
	}

	public DateTime getDhReemissao() {
		return dhReemissao;
	}

	public void setDhReemissao(DateTime dhReemissao) {
		this.dhReemissao = dhReemissao;
	}

	public DateTime getDhTransmissao() {
		return dhTransmissao;
	}

	public void setDhTransmissao(DateTime dhTransmissao) {
		this.dhTransmissao = dhTransmissao;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public YearMonthDay getDtLiquidacao() {
		return dtLiquidacao;
	}

	public void setDtLiquidacao(YearMonthDay dtLiquidacao) {
		this.dtLiquidacao = dtLiquidacao;
	}

	public YearMonthDay getDtTransmissaoEdi() {
		return dtTransmissaoEdi;
	}

	public void setDtTransmissaoEdi(YearMonthDay dtTransmissaoEdi) {
		this.dtTransmissaoEdi = dtTransmissaoEdi;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public Long getNrFatura() {
		return nrFatura;
	}

	public void setNrFatura(Long nrFatura) {
		this.nrFatura = nrFatura;
	}

	public String getNrPreFatura() {
		return nrPreFatura;
	}

	public void setNrPreFatura(String nrPreFatura) {
		this.nrPreFatura = nrPreFatura;
	}

	public String getObFatura() {
		return obFatura;
	}

	public void setObFatura(String obFatura) {
		this.obFatura = obFatura;
	}

	public Integer getQtDocumentos() {
		return qtDocumentos;
	}

	public void setQtDocumentos(Integer qtDocumentos) {
		this.qtDocumentos = qtDocumentos;
	}

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

	public DomainValue getTpFatura() {
		return tpFatura;
	}

	public void setTpFatura(DomainValue tpFatura) {
		this.tpFatura = tpFatura;
	}

	public DomainValue getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(DomainValue tpFrete) {
		this.tpFrete = tpFrete;
	}

	public DomainValue getTpModal() {
		return tpModal;
	}

	public void setTpModal(DomainValue tpModal) {
		this.tpModal = tpModal;
	}

	public DomainValue getTpOrigem() {
		return tpOrigem;
	}

	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}

	public DomainValue getTpSituacaoAprovacao() {
		return tpSituacaoAprovacao;
	}

	public void setTpSituacaoAprovacao(DomainValue tpSituacaoAprovacao) {
		this.tpSituacaoAprovacao = tpSituacaoAprovacao;
	}

	public DomainValue getTpSituacaoFatura() {
		return tpSituacaoFatura;
	}

	public void setTpSituacaoFatura(DomainValue tpSituacaoFatura) {
		this.tpSituacaoFatura = tpSituacaoFatura;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public BigDecimal getVlBaseCalcIr() {
		return vlBaseCalcIr;
	}

	public void setVlBaseCalcIr(BigDecimal vlBaseCalcIr) {
		this.vlBaseCalcIr = vlBaseCalcIr;
	}

	public BigDecimal getVlBaseCalcPisCofinsCsll() {
		return vlBaseCalcPisCofinsCsll;
	}

	public void setVlBaseCalcPisCofinsCsll(BigDecimal vlBaseCalcPisCofinsCsll) {
		this.vlBaseCalcPisCofinsCsll = vlBaseCalcPisCofinsCsll;
	}

	public BigDecimal getVlCofins() {
		return vlCofins;
	}

	public void setVlCofins(BigDecimal vlCofins) {
		this.vlCofins = vlCofins;
	}

	public BigDecimal getVlCotacaoMoeda() {
		return vlCotacaoMoeda;
	}

	public void setVlCotacaoMoeda(BigDecimal vlCotacaoMoeda) {
		this.vlCotacaoMoeda = vlCotacaoMoeda;
	}

	public BigDecimal getVlCsll() {
		return vlCsll;
	}

	public void setVlCsll(BigDecimal vlCsll) {
		this.vlCsll = vlCsll;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public BigDecimal getVlIr() {
		return vlIr;
	}

	public void setVlIr(BigDecimal vlIr) {
		this.vlIr = vlIr;
	}

	public BigDecimal getVlIva() {
		return vlIva;
	}

	public void setVlIva(BigDecimal vlIva) {
		this.vlIva = vlIva;
	}

	public BigDecimal getVlJuroCalculado() {
		return vlJuroCalculado;
	}

	public void setVlJuroCalculado(BigDecimal vlJuroCalculado) {
		this.vlJuroCalculado = vlJuroCalculado;
	}

	public BigDecimal getVlJuroRecebido() {
		return vlJuroRecebido;
	}

	public void setVlJuroRecebido(BigDecimal vlJuroRecebido) {
		this.vlJuroRecebido = vlJuroRecebido;
	}

	public BigDecimal getVlPis() {
		return vlPis;
	}

	public void setVlPis(BigDecimal vlPis) {
		this.vlPis = vlPis;
	}

	public BigDecimal getVlTotal() {
		return vlTotal;
	}

	public void setVlTotal(BigDecimal vlTotal) {
		this.vlTotal = vlTotal;
	}

	public BigDecimal getVlTotalRecebido() {
		return vlTotalRecebido;
	}

	public void setVlTotalRecebido(BigDecimal vlTotalRecebido) {
		this.vlTotalRecebido = vlTotalRecebido;
	}
	
	public DateTime getDhLog() {
		return dhLog;
	}

	public void setDhLog(DateTime dhLog) {
		this.dhLog = dhLog;
	}

	public Long getIdFaturaLog() {
		return idFaturaLog;
	}

	public void setIdFaturaLog(Long idFaturaLog) {
		this.idFaturaLog = idFaturaLog;
	}

	public String getLoginLog() {
		return loginLog;
	}

	public void setLoginLog(String loginLog) {
		this.loginLog = loginLog;
	}

	public String getOpLog() {
		return opLog;
	}

	public void setOpLog(String opLog) {
		this.opLog = opLog;
	}

	/**  @@ParametrizedAttribute("com.mercurio.lms.contasreceber.model.ItemFaturaLog")     */
	public List getItensFaturaLog() {
		return itensFaturaLog;
	}

	public void setItensFaturaLog(List itensFaturaLog) {
		this.itensFaturaLog = itensFaturaLog;
	}

	public AgrupamentoCliente getAgrupamentoCliente() {
		return agrupamentoCliente;
	}

	public void setAgrupamentoCliente(AgrupamentoCliente agrupamentoCliente) {
		this.agrupamentoCliente = agrupamentoCliente;
	}

	public Boleto getBoleto() {
		return boleto;
	}

	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

	public Cedente getCedente() {
		return cedente;
	}

	public void setCedente(Cedente cedente) {
		this.cedente = cedente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CotacaoMoeda getCotacaoMoeda() {
		return cotacaoMoeda;
	}

	public void setCotacaoMoeda(CotacaoMoeda cotacaoMoeda) {
		this.cotacaoMoeda = cotacaoMoeda;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public Fatura getFatura() {
		return fatura;
	}

	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}

	public Fatura getFaturaOriginal() {
		return faturaOriginal;
	}

	public void setFaturaOriginal(Fatura faturaOriginal) {
		this.faturaOriginal = faturaOriginal;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Filial getFilialCobradora() {
		return filialCobradora;
	}

	public void setFilialCobradora(Filial filialCobradora) {
		this.filialCobradora = filialCobradora;
	}

	public Manifesto getManifesto() {
		return manifesto;
	}

	public void setManifesto(Manifesto manifesto) {
		this.manifesto = manifesto;
	}

	public ManifestoEntrega getManifestoEntrega() {
		return manifestoEntrega;
	}

	public void setManifestoEntrega(ManifestoEntrega manifestoEntrega) {
		this.manifestoEntrega = manifestoEntrega;
	}

	public Manifesto getManifestoEntregaOrigem() {
		return manifestoEntregaOrigem;
	}

	public void setManifestoEntregaOrigem(Manifesto manifestoEntregaOrigem) {
		this.manifestoEntregaOrigem = manifestoEntregaOrigem;
	}

	public Manifesto getManifestoOrigem() {
		return manifestoOrigem;
	}

	public void setManifestoOrigem(Manifesto manifestoOrigem) {
		this.manifestoOrigem = manifestoOrigem;
	}

	public Moeda getMoeda() {
		return moeda;
	}

	public void setMoeda(Moeda moeda) {
		this.moeda = moeda;
	}

	public NotaDebitoNacional getNotaDebitoNacional() {
		return notaDebitoNacional;
	}

	public void setNotaDebitoNacional(NotaDebitoNacional notaDebitoNacional) {
		this.notaDebitoNacional = notaDebitoNacional;
	}

	public Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	public Pendencia getPendenciaDesconto() {
		return pendenciaDesconto;
	}

	public void setPendenciaDesconto(Pendencia pendenciaDesconto) {
		this.pendenciaDesconto = pendenciaDesconto;
	}

	public Recibo getRecibo() {
		return recibo;
	}

	public void setRecibo(Recibo recibo) {
		this.recibo = recibo;
	}

	public Redeco getRedeco() {
		return redeco;
	}

	public void setRedeco(Redeco redeco) {
		this.redeco = redeco;
	}

	public RelacaoCobranca getRelacaoCobranca() {
		return relacaoCobranca;
	}

	public void setRelacaoCobranca(RelacaoCobranca relacaoCobranca) {
		this.relacaoCobranca = relacaoCobranca;
	}

	public Servico getServico() {
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public TipoAgrupamento getTipoAgrupamento() {
		return tipoAgrupamento;
	}

	public void setTipoAgrupamento(TipoAgrupamento tipoAgrupamento) {
		this.tipoAgrupamento = tipoAgrupamento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String toString() {
        return new ToStringBuilder(this)
				.append("idFaturaLog", getIdFaturaLog()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof FaturaLog))
			return false;
        FaturaLog castOther = (FaturaLog) other;
		return new EqualsBuilder().append(this.getIdFaturaLog(),
				castOther.getIdFaturaLog()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdFaturaLog()).toHashCode();
    }
}