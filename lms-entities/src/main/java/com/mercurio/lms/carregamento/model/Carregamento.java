package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class Carregamento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Long idCarregamento;
	private Long cnpjRemetenteCliente;
	private Long cnpjRemetenteTNT;
	private Long cnpjDestinatarioTNT;
	private DateTime dtInicio;
	private DateTime dtFim;
	private DomainValue codigoStatus;
	private Long totalVolumes;
	private BigDecimal totalPeso;
	private BigDecimal totalCubagem;
	private Long matriculaChefia;
	private String tipoCarregamento;
	private String frotaVeiculo;
	private String placaVeiculo;
	private String chapaVeiculo;
	private String docaCarregamento;
	private String rotaCarregamento;
	private List<Volume> volumes;
	
	public Carregamento(Long id) {
		this.idCarregamento = id;
	}

	public Carregamento() {
	}

	public Long getIdCarregamento() {
		return idCarregamento;
	}

	public void setIdCarregamento(Long idCarregamento) {
		this.idCarregamento = idCarregamento;
	}

	public Long getCnpjRemetenteCliente() {
		return cnpjRemetenteCliente;
	}

	public void setCnpjRemetenteCliente(Long cnpjRemetenteCliente) {
		this.cnpjRemetenteCliente = cnpjRemetenteCliente;
	}

	public Long getCnpjRemetenteTNT() {
		return cnpjRemetenteTNT;
	}

	public void setCnpjRemetenteTNT(Long cnpjRemetenteTNT) {
		this.cnpjRemetenteTNT = cnpjRemetenteTNT;
	}

	public Long getCnpjDestinatarioTNT() {
		return cnpjDestinatarioTNT;
	}

	public void setCnpjDestinatarioTNT(Long cnpjDestinatarioTNT) {
		this.cnpjDestinatarioTNT = cnpjDestinatarioTNT;
	}

	public DateTime getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(DateTime dtInicio) {
		this.dtInicio = dtInicio;
	}

	public DateTime getDtFim() {
		return dtFim;
	}

	public void setDtFim(DateTime dtFim) {
		this.dtFim = dtFim;
	}

	public DomainValue getCodigoStatus() {
		return codigoStatus;
	}

	public void setCodigoStatus(DomainValue codigoStatus) {
		this.codigoStatus = codigoStatus;
	}

	public Long getTotalVolumes() {
		return totalVolumes;
	}

	public void setTotalVolumes(Long totalVolumes) {
		this.totalVolumes = totalVolumes;
	}

	public BigDecimal getTotalPeso() {
		return totalPeso;
	}

	public void setTotalPeso(BigDecimal totalPeso) {
		this.totalPeso = totalPeso;
	}

	public BigDecimal getTotalCubagem() {
		return totalCubagem;
	}

	public void setTotalCubagem(BigDecimal totalCubagem) {
		this.totalCubagem = totalCubagem;
	}

	public Long getMatriculaChefia() {
		return matriculaChefia;
	}

	public void setMatriculaChefia(Long matriculaChefia) {
		this.matriculaChefia = matriculaChefia;
	}

	public String getTipoCarregamento() {
		return tipoCarregamento;
	}

	public void setTipoCarregamento(String tipoCarregamento) {
		this.tipoCarregamento = tipoCarregamento;
	}

	public String getFrotaVeiculo() {
		return frotaVeiculo;
	}

	public void setFrotaVeiculo(String frotaVeiculo) {
		this.frotaVeiculo = frotaVeiculo;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}

	public String getChapaVeiculo() {
		return chapaVeiculo;
	}

	public void setChapaVeiculo(String chapaVeiculo) {
		this.chapaVeiculo = chapaVeiculo;
	}

	public String getDocaCarregamento() {
		return docaCarregamento;
	}

	public void setDocaCarregamento(String docaCarregamento) {
		this.docaCarregamento = docaCarregamento;
	}

	public String getRotaCarregamento() {
		return rotaCarregamento;
	}

	public void setRotaCarregamento(String rotaCarregamento) {
		this.rotaCarregamento = rotaCarregamento;
	}

	public List<Volume> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<Volume> volumes) {
		this.volumes = volumes;
	}
	
	@Transient
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		bean.put("idCarregamento", this.getIdCarregamento());
		bean.put("chapaVeiculo", this.getChapaVeiculo());
		bean.put("placaVeiculo", this.getPlacaVeiculo());
		bean.put("docaCarregamento", this.getDocaCarregamento());
		bean.put("frotaVeiculo", this.getFrotaVeiculo());
		
		return bean;
	}
	
}
