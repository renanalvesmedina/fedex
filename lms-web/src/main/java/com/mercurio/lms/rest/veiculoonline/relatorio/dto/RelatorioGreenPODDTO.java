package com.mercurio.lms.rest.veiculoonline.relatorio.dto;

import java.util.Date;

import com.mercurio.adsm.rest.BaseDTO;

public class RelatorioGreenPODDTO extends BaseDTO {

	private static final long serialVersionUID = 6048924347158915403L;
	
	private String existeImagem;
	private Date dataEmissao;
	private String filialEntrega;
	private Integer cnpjRemetente;
	private Integer cnpjDestinatario;
	private String nrControleCarga;
	private Integer nrManifestoEntrega;
	private String regional;
	private String municipioEntrega;
	private String veiculoRastreado;
	private String placaVeiculo;
	private String nrCelular;
	private String filialOrigem;
	private String cte;
	private String nrPedidoNatura;	
	
	public String getExisteImagem() {
		return existeImagem;
	}

	public void setExisteImagem(String existeImagem) {
		this.existeImagem = existeImagem;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getFilialEntrega() {
		return filialEntrega;
	}

	public void setFilialEntrega(String filialEntrega) {
		this.filialEntrega = filialEntrega;
	}

	public Integer getCnpjRemetente() {
		return cnpjRemetente;
	}

	public void setCnpjRemetente(Integer cnpjRemetente) {
		this.cnpjRemetente = cnpjRemetente;
	}

	public Integer getCnpjDestinatario() {
		return cnpjDestinatario;
	}

	public void setCnpjDestinatario(Integer cnpjDestinatario) {
		this.cnpjDestinatario = cnpjDestinatario;
	}

	public String getNrControleCarga() {
		return nrControleCarga;
	}

	public void setNrControleCarga(String nrControleCarga) {
		this.nrControleCarga = nrControleCarga;
	}

	public Integer getNrManifestoEntrega() {
		return nrManifestoEntrega;
	}

	public void setNrManifestoEntrega(Integer nrManifestoEntrega) {
		this.nrManifestoEntrega = nrManifestoEntrega;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getMunicipioEntrega() {
		return municipioEntrega;
	}

	public void setMunicipioEntrega(String municipioEntrega) {
		this.municipioEntrega = municipioEntrega;
	}

	public String getVeiculoRastreado() {
		return veiculoRastreado;
	}

	public void setVeiculoRastreado(String veiculoRastreado) {
		this.veiculoRastreado = veiculoRastreado;
	}

	public String getPlacaVeiculo() {
		return placaVeiculo;
	}

	public void setPlacaVeiculo(String placaVeiculo) {
		this.placaVeiculo = placaVeiculo;
	}

	public String getNrCelular() {
		return nrCelular;
	}

	public void setNrCelular(String nrCelular) {
		this.nrCelular = nrCelular;
	}

	public String getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(String filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public String getCte() {
		return cte;
	}

	public void setCte(String cte) {
		this.cte = cte;
	}

	public String getNrPedidoNatura() {
		return nrPedidoNatura;
	}

	public void setNrPedidoNatura(String nrPedidoNatura) {
		this.nrPedidoNatura = nrPedidoNatura;
	}

}
