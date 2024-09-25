package com.mercurio.lms.rest.vendas.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ServicoAdicionalClienteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idServicoAdicionalCliente;
	private BigDecimal vlValor;
	private String vlValorFormatado;
	private String dsTpIndicador;
	private String nmParcelaPreco;
	private Boolean blCobrancaRetroativa;
	private Integer nrDecursoPrazo;
	private Integer nrQuantidadeDias;
	private Map<String, Object> parcelaPreco;
	private Map<String, Object> simulacao;
	private DomainValue tpFormaCobranca;
	private DomainValue tpIndicador;
	private BigDecimal vlMinimo;
	private DomainValue tpUnidMedidaCalcCobr;
	
	public ServicoAdicionalClienteDTO() {
	}

	public ServicoAdicionalClienteDTO(Long idServicoAdicionalCliente, BigDecimal vlValor, String vlValorFormatado, String dsTpIndicador, String nmParcelaPreco) {
		super();
		this.idServicoAdicionalCliente = idServicoAdicionalCliente;
		this.vlValor = vlValor;
		this.vlValorFormatado = vlValorFormatado;
		this.dsTpIndicador = dsTpIndicador;
		this.nmParcelaPreco = nmParcelaPreco;
	}

	public Long getIdServicoAdicionalCliente() {
		return idServicoAdicionalCliente;
	}

	public void setIdServicoAdicionalCliente(Long idServicoAdicionalCliente) {
		this.idServicoAdicionalCliente = idServicoAdicionalCliente;
	}

	public BigDecimal getVlValor() {
		return vlValor;
	}

	public void setVlValor(BigDecimal vlValor) {
		this.vlValor = vlValor;
	}

	public String getVlValorFormatado() {
		return vlValorFormatado;
	}

	public void setVlValorFormatado(String vlValorFormatado) {
		this.vlValorFormatado = vlValorFormatado;
	}

	public String getDsTpIndicador() {
		return dsTpIndicador;
	}

	public void setDsTpIndicador(String dsTpIndicador) {
		this.dsTpIndicador = dsTpIndicador;
	}

	public String getNmParcelaPreco() {
		return nmParcelaPreco;
	}

	public void setNmParcelaPreco(String nmParcelaPreco) {
		this.nmParcelaPreco = nmParcelaPreco;
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

	public Integer getNrQuantidadeDias() {
		return nrQuantidadeDias;
	}

	public void setNrQuantidadeDias(Integer nrQuantidadeDias) {
		this.nrQuantidadeDias = nrQuantidadeDias;
	}

	public Map<String, Object> getParcelaPreco() {
		return parcelaPreco;
	}

	public void setParcelaPreco(Map<String, Object> parcelaPreco) {
		this.parcelaPreco = parcelaPreco;
	}

	public Map<String, Object> getSimulacao() {
		return simulacao;
	}

	public void setSimulacao(Map<String, Object> simulacao) {
		this.simulacao = simulacao;
	}

	public DomainValue getTpFormaCobranca() {
		return tpFormaCobranca;
	}

	public void setTpFormaCobranca(DomainValue tpFormaCobranca) {
		this.tpFormaCobranca = tpFormaCobranca;
	}

	public DomainValue getTpIndicador() {
		return tpIndicador;
	}

	public void setTpIndicador(DomainValue tpIndicador) {
		this.tpIndicador = tpIndicador;
	}

	public BigDecimal getVlMinimo() {
		return vlMinimo;
	}

	public void setVlMinimo(BigDecimal vlMinimo) {
		this.vlMinimo = vlMinimo;
	}

	public DomainValue getTpUnidMedidaCalcCobr() {
		return tpUnidMedidaCalcCobr;
	}

	public void setTpUnidMedidaCalcCobr(DomainValue tpUnidMedidaCalcCobr) {
		this.tpUnidMedidaCalcCobr = tpUnidMedidaCalcCobr;
	}

}
