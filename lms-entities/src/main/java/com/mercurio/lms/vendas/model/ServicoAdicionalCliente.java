package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoAdicionalCliente implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idServicoAdicionalCliente;

	/** persistent field */
	private BigDecimal vlValor;

	/** not persistent field */
	private String vlValorFormatado;

	/** nullable persistent field */
	private BigDecimal vlMinimo;

	/** nullable persistent field */
	private DomainValue tpIndicador;

	/** nullable persistent field */
	private Integer nrQuantidadeDias;

	/** nullable persistent field */
	private DomainValue tpFormaCobranca;
	
	/** nullable persistent field */
	private Boolean blCobrancaRetroativa;
	
	/** nullable persistent field */
	private Integer nrDecursoPrazo;
	
	/** nullable persistent field */
	private Boolean blPagaParaTodos;

	private Boolean blSeparaDocumentosNfs;

	/** persistent field */
	private com.mercurio.lms.vendas.model.Simulacao simulacao;

	/** persistent field */
	private com.mercurio.lms.vendas.model.TabelaDivisaoCliente tabelaDivisaoCliente;

	/** persistent field */
	private com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco;

	/** persistent field */
	private com.mercurio.lms.vendas.model.Cotacao cotacao;

	private List<ServicoAdicionalClienteDestinatario> destinatariosClientePagaServico;

	private Boolean blAlterou = false;
	
	/** not persistent field */
	private Long idProposta; 
	
	private DomainValue tpUnidMedidaCalcCobr;
	
	/** nullable persistent field */
    private Boolean blCobrancaCte;

	public Long getIdServicoAdicionalCliente() {
		return this.idServicoAdicionalCliente;
	}

	public void setIdServicoAdicionalCliente(Long idServicoAdicionalCliente) {
		this.idServicoAdicionalCliente = idServicoAdicionalCliente;
	}

	public BigDecimal getVlValor() {
		return this.vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
	}

	public void setVlValorFormatado(String vlValorFormatado) {
		this.vlValorFormatado = vlValorFormatado;
	}

	public String getVlValorFormatado() {
		return this.vlValorFormatado;
	}

	public BigDecimal getVlMinimo() {
		return this.vlMinimo;
	}

	public void setVlMinimo(BigDecimal vlMinimo) {
		this.vlMinimo = vlMinimo;
	}

	public DomainValue getTpIndicador() {
		return this.tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public Integer getNrQuantidadeDias() {
		return this.nrQuantidadeDias;
	}

	public void setNrQuantidadeDias(Integer nrQuantidadeDias) {
		this.nrQuantidadeDias = nrQuantidadeDias;
	}

	public DomainValue getTpFormaCobranca() {
		return tpFormaCobranca;
	}

	public void setTpFormaCobranca(DomainValue tpFormaCobranca) {
		this.tpFormaCobranca = tpFormaCobranca;
	}

	public Boolean getBlCobrancaRetroativa() {
		return blCobrancaRetroativa;
	}

	public void setBlCobrancaRetroativa(Boolean blCobrancaRetroativa) {
		this.blCobrancaRetroativa = blCobrancaRetroativa;
	}

	public Integer getNrDecursoPrazo() {
		return nrDecursoPrazo;
	}

	public void setNrDecursoPrazo(Integer nrDecursoPrazo) {
		this.nrDecursoPrazo = nrDecursoPrazo;
	}

	public Boolean getBlPagaParaTodos() {
		return blPagaParaTodos;
	}

	public void setBlPagaParaTodos(Boolean blPagaParaTodos) {
		this.blPagaParaTodos = blPagaParaTodos;
	}

	public com.mercurio.lms.vendas.model.Simulacao getSimulacao() {
		return this.simulacao;
	}

	public void setSimulacao(com.mercurio.lms.vendas.model.Simulacao simulacao) {
		this.simulacao = simulacao;
	}

	public com.mercurio.lms.vendas.model.TabelaDivisaoCliente getTabelaDivisaoCliente() {
		return this.tabelaDivisaoCliente;
	}

	public void setTabelaDivisaoCliente(
			com.mercurio.lms.vendas.model.TabelaDivisaoCliente tabelaDivisaoCliente) {
		this.tabelaDivisaoCliente = tabelaDivisaoCliente;
	}

	public com.mercurio.lms.tabelaprecos.model.ParcelaPreco getParcelaPreco() {
		return this.parcelaPreco;
	}

	public void setParcelaPreco(
			com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public com.mercurio.lms.vendas.model.Cotacao getCotacao() {
		return this.cotacao;
	}

	public void setCotacao(com.mercurio.lms.vendas.model.Cotacao cotacao) {
		this.cotacao = cotacao;
	}

	public Boolean getBlAlterou() {
		return blAlterou;
	}

	public void setBlAlterou(Boolean blAlterou) {
		this.blAlterou = blAlterou;
	}

	public List<ServicoAdicionalClienteDestinatario> getDestinatariosClientePagaServico() {
		return destinatariosClientePagaServico;
	}

	public void setDestinatariosClientePagaServico(
			List<ServicoAdicionalClienteDestinatario> destinatariosClientePagaServico) {
		this.destinatariosClientePagaServico = destinatariosClientePagaServico;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idServicoAdicionalCliente",
				getIdServicoAdicionalCliente()).toString();
	}

	public Boolean getBlSeparaDocumentosNfs() {
		return blSeparaDocumentosNfs;
	}

	public void setBlSeparaDocumentosNfs(Boolean blSeparaDocumentosNfs) {
		this.blSeparaDocumentosNfs = blSeparaDocumentosNfs;
	}

	public Long getIdProposta() {
		return idProposta;
	}

	public void setIdProposta(Long idProposta) {
		this.idProposta = idProposta;
	}
	
	public Boolean getBlCobrancaCte() {
        return blCobrancaCte;
    }

    public void setBlCobrancaCte(Boolean blCobrancaCte) {
        this.blCobrancaCte = blCobrancaCte;
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoAdicionalCliente))
			return false;
		ServicoAdicionalCliente castOther = (ServicoAdicionalCliente) other;
		return new EqualsBuilder().append(this.getIdServicoAdicionalCliente(),
				castOther.getIdServicoAdicionalCliente()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoAdicionalCliente())
				.toHashCode();
	}

	public ServicoAdicionalCliente clone(){
		ServicoAdicionalCliente servicoAdicionalCliente = new ServicoAdicionalCliente();
		servicoAdicionalCliente.setParcelaPreco(parcelaPreco);
		servicoAdicionalCliente.setVlValor(vlValor);
		servicoAdicionalCliente.setTabelaDivisaoCliente(tabelaDivisaoCliente);
		servicoAdicionalCliente.setNrQuantidadeDias(nrQuantidadeDias);
		servicoAdicionalCliente.setVlMinimo(vlMinimo);
		servicoAdicionalCliente.setTpIndicador(tpIndicador);
		servicoAdicionalCliente.setTpFormaCobranca(tpFormaCobranca);
		servicoAdicionalCliente.setBlCobrancaRetroativa(blCobrancaRetroativa);
		servicoAdicionalCliente.setNrDecursoPrazo(nrDecursoPrazo);
		servicoAdicionalCliente.setBlPagaParaTodos(blPagaParaTodos);
		servicoAdicionalCliente.setBlAlterou(blAlterou);
		servicoAdicionalCliente.setCotacao(cotacao);
		servicoAdicionalCliente.setSimulacao(simulacao);
		servicoAdicionalCliente.setTabelaDivisaoCliente(tabelaDivisaoCliente);
		servicoAdicionalCliente.setVlValorFormatado(vlValorFormatado);
		servicoAdicionalCliente.setBlSeparaDocumentosNfs(blSeparaDocumentosNfs);
		servicoAdicionalCliente.setTpUnidMedidaCalcCobr(tpUnidMedidaCalcCobr);
		servicoAdicionalCliente.setBlCobrancaCte(blCobrancaCte);
		return servicoAdicionalCliente;
}

	public DomainValue getTpUnidMedidaCalcCobr() {
		return tpUnidMedidaCalcCobr;
	}

	public void setTpUnidMedidaCalcCobr(DomainValue tpUnidMedidaCalcCobr) {
		this.tpUnidMedidaCalcCobr = tpUnidMedidaCalcCobr;
	}
}
