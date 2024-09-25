package com.mercurio.lms.pendencia.model;

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

/** @author LMS Custom Hibernate CodeGenerator */
public class LotePendencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idLotePendencia;

    /** persistent field */
    private Short nrAno;

    /** persistent field */
    private Integer nrLote;

    /** persistent field */
    private DateTime dhGeracao;

    /** persistent field */
    private DomainValue tpLote;

    /** persistent field */
    private Integer qtVolumes;

    /** persistent field */
    private BigDecimal vlMercadoria;

    /** nullable persistent field */
    private YearMonthDay dtVenda;

    /** nullable persistent field */
    private YearMonthDay dtEntrega;

    /** nullable persistent field */
    private YearMonthDay dtVencimentoPagamento;

    /** nullable persistent field */
    private BigDecimal vlPago;

    /** nullable persistent field */
    private String nrEnderecoComprador;

    /** nullable persistent field */
    private String nrCep;

    /** nullable persistent field */
    private String edComprador;

    /** nullable persistent field */
    private String dsComplementoEndereco;

    /** nullable persistent field */
    private String dsBairro;

    /** nullable persistent field */
    private DomainValue tpFormaPagamento;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Municipio municipio;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.ProtocoloTransferencia protocoloTransferencia;

    /** persistent field */
    private List mercadoriaLotePendencias;

    /** persistent field */
    private List emissaoLotes;

    /** persistent field */
    private List chequePagtoVendaLotes;

    public Long getIdLotePendencia() {
        return this.idLotePendencia;
    }

    public void setIdLotePendencia(Long idLotePendencia) {
        this.idLotePendencia = idLotePendencia;
    }

    public Short getNrAno() {
        return this.nrAno;
    }

    public void setNrAno(Short nrAno) {
        this.nrAno = nrAno;
    }

    public Integer getNrLote() {
        return this.nrLote;
    }

    public void setNrLote(Integer nrLote) {
        this.nrLote = nrLote;
    }

    public DateTime getDhGeracao() {
        return this.dhGeracao;
    }

    public void setDhGeracao(DateTime dhGeracao) {
        this.dhGeracao = dhGeracao;
    }

    public DomainValue getTpLote() {
        return this.tpLote;
    }

    public void setTpLote(DomainValue tpLote) {
        this.tpLote = tpLote;
    }

    public Integer getQtVolumes() {
        return this.qtVolumes;
    }

    public void setQtVolumes(Integer qtVolumes) {
        this.qtVolumes = qtVolumes;
    }

    public BigDecimal getVlMercadoria() {
        return this.vlMercadoria;
    }

    public void setVlMercadoria(BigDecimal vlMercadoria) {
        this.vlMercadoria = vlMercadoria;
    }

    public YearMonthDay getDtVenda() {
        return this.dtVenda;
    }

    public void setDtVenda(YearMonthDay dtVenda) {
        this.dtVenda = dtVenda;
    }

    public YearMonthDay getDtEntrega() {
        return this.dtEntrega;
    }

    public void setDtEntrega(YearMonthDay dtEntrega) {
        this.dtEntrega = dtEntrega;
    }

    public YearMonthDay getDtVencimentoPagamento() {
        return this.dtVencimentoPagamento;
    }

    public void setDtVencimentoPagamento(YearMonthDay dtVencimentoPagamento) {
        this.dtVencimentoPagamento = dtVencimentoPagamento;
    }

    public BigDecimal getVlPago() {
        return this.vlPago;
    }

    public void setVlPago(BigDecimal vlPago) {
        this.vlPago = vlPago;
    }

    public String getNrEnderecoComprador() {
        return this.nrEnderecoComprador;
    }

    public void setNrEnderecoComprador(String nrEnderecoComprador) {
        this.nrEnderecoComprador = nrEnderecoComprador;
    }

    public String getNrCep() {
        return this.nrCep;
    }

    public void setNrCep(String nrCep) {
        this.nrCep = nrCep;
    }

    public String getEdComprador() {
        return this.edComprador;
    }

    public void setEdComprador(String edComprador) {
        this.edComprador = edComprador;
    }

    public String getDsComplementoEndereco() {
        return this.dsComplementoEndereco;
    }

    public void setDsComplementoEndereco(String dsComplementoEndereco) {
        this.dsComplementoEndereco = dsComplementoEndereco;
    }

    public String getDsBairro() {
        return this.dsBairro;
    }

    public void setDsBairro(String dsBairro) {
        this.dsBairro = dsBairro;
    }

    public DomainValue getTpFormaPagamento() {
        return this.tpFormaPagamento;
    }

    public void setTpFormaPagamento(DomainValue tpFormaPagamento) {
        this.tpFormaPagamento = tpFormaPagamento;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
        return this.municipio;
    }

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
        this.municipio = municipio;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.pendencia.model.ProtocoloTransferencia getProtocoloTransferencia() {
        return this.protocoloTransferencia;
    }

	public void setProtocoloTransferencia(
			com.mercurio.lms.pendencia.model.ProtocoloTransferencia protocoloTransferencia) {
        this.protocoloTransferencia = protocoloTransferencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.MercadoriaLotePendencia.class)     
    public List getMercadoriaLotePendencias() {
        return this.mercadoriaLotePendencias;
    }

    public void setMercadoriaLotePendencias(List mercadoriaLotePendencias) {
        this.mercadoriaLotePendencias = mercadoriaLotePendencias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.EmissaoLote.class)     
    public List getEmissaoLotes() {
        return this.emissaoLotes;
    }

    public void setEmissaoLotes(List emissaoLotes) {
        this.emissaoLotes = emissaoLotes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.pendencia.model.ChequePagtoVendaLote.class)     
    public List getChequePagtoVendaLotes() {
        return this.chequePagtoVendaLotes;
    }

    public void setChequePagtoVendaLotes(List chequePagtoVendaLotes) {
        this.chequePagtoVendaLotes = chequePagtoVendaLotes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idLotePendencia",
				getIdLotePendencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof LotePendencia))
			return false;
        LotePendencia castOther = (LotePendencia) other;
		return new EqualsBuilder().append(this.getIdLotePendencia(),
				castOther.getIdLotePendencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdLotePendencia()).toHashCode();
    }

}
