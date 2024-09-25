package com.mercurio.lms.contasreceber.dto;

import java.math.BigDecimal;
import java.util.Map;

import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;

public class ProcessaArquivoLinhaDTO {

	private Fatura faturaMaster;
	private Map<String, ItemFatura> keyItems;
	private String sgFilialFatura;
	private Long nrFatura;
	private String tpDocumento;
	private String sgFilialDocto;
	private Long nrDocto;
	private BigDecimal vlDevido;
	private BigDecimal vlDesconto;
	private Integer nrLinha;

	public Fatura getFaturaMaster() {
		return faturaMaster;
	}

	public void setFaturaMaster(Fatura faturaMaster) {
		this.faturaMaster = faturaMaster;
	}

	public Map<String, ItemFatura> getKeyItems() {
		return keyItems;
	}

	public void setKeyItems(Map<String, ItemFatura> keyItems) {
		this.keyItems = keyItems;
	}

	public String getSgFilialFatura() {
		return sgFilialFatura;
	}

	public void setSgFilialFatura(String sgFilialFatura) {
		this.sgFilialFatura = sgFilialFatura;
	}

	public Long getNrFatura() {
		return nrFatura;
	}

	public void setNrFatura(Long nrFatura) {
		this.nrFatura = nrFatura;
	}

	public String getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(String tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public String getSgFilialDocto() {
		return sgFilialDocto;
	}

	public void setSgFilialDocto(String sgFilialDocto) {
		this.sgFilialDocto = sgFilialDocto;
	}

	public Long getNrDocto() {
		return nrDocto;
	}

	public void setNrDocto(Long nrDocto) {
		this.nrDocto = nrDocto;
	}

	public BigDecimal getVlDevido() {
		return vlDevido;
	}

	public void setVlDevido(BigDecimal vlDevido) {
		this.vlDevido = vlDevido;
	}

	public BigDecimal getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(BigDecimal vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public Integer getNrLinha() {
		return nrLinha;
	}

	public void setNrLinha(Integer nrLinha) {
		this.nrLinha = nrLinha;
	}

}
