package com.mercurio.lms.rest.coleta.programacaocoletasveiculos.dto;

import java.math.BigDecimal;

import com.mercurio.adsm.core.util.VarcharI18nConverter;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.rest.BaseDTO;

public class DetalhesColetaDTO extends BaseDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nmMunicipio;
	private String sgFilial;
	private String dsNaturezaProduto;
	private String tpIdentificacao;
	private String dsServico;
	private String tpFrete;
	private BigDecimal psAforado;
	private Integer qtVolumes;
	private BigDecimal vlMercadoria;
	private String awb;
	private String sgFilialOrigemDocto;
	private Integer nrDoctoServico;
	private String blEntregaDireta;
	private String cotacao_sgFilial;
	private Integer nrCotacao;
	private String ctoInternacional_sgPais;
	private Integer ctoInternacional_nrCrt;
	private String nrIdentificacaoFormatado;
	private String nmPessoa;
	
	
	public String getNmMunicipio() {
		return nmMunicipio;
	}
	public void setNmMunicipio(String nmMunicipio) {
		this.nmMunicipio = nmMunicipio;
	}
	public String getSgFilial() {
		return sgFilial;
	}
	public void setSgFilial(String sgFilial) {
		this.sgFilial = sgFilial;
	}
	public String getDsNaturezaProduto() {
		return dsNaturezaProduto;
	}
	public void setDsNaturezaProduto(String dsNaturezaProduto) {
		this.dsNaturezaProduto = dsNaturezaProduto;
	}
	public String getTpIdentificacao() {
		return tpIdentificacao;
	}
	public void setTpIdentificacao(String tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}
	public String getDsServico() {
		return dsServico;
	}
	public void setDsServico(String dsServico) {
		this.dsServico = dsServico;
	}
	public String getTpFrete() {
		return tpFrete;
	}
	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}
	public BigDecimal getPsAforado() {
		return psAforado;
	}
	public void setPsAforado(BigDecimal psAforado) {
		this.psAforado = psAforado;
	}
	public Integer getQtVolumes() {
		return qtVolumes;
	}
	public void setQtVolumes(Integer qtVolumes) {
		this.qtVolumes = qtVolumes;
	}
	public BigDecimal getVlMercadoria() {
		return vlMercadoria;
	}
	public void setVlMercadoria(BigDecimal vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}
	public String getAwb() {
		return awb;
	}
	public void setAwb(String awb) {
		this.awb = awb;
	}
	public String getSgFilialOrigemDocto() {
		return sgFilialOrigemDocto;
	}
	public void setSgFilialOrigemDocto(String sgFilialOrigemDocto) {
		this.sgFilialOrigemDocto = sgFilialOrigemDocto;
	}
	public Integer getNrDoctoServico() {
		return nrDoctoServico;
	}
	public void setNrDoctoServico(Integer nrDoctoServico) {
		this.nrDoctoServico = nrDoctoServico;
	}
	public String getBlEntregaDireta() {
		return blEntregaDireta;
	}
	public void setBlEntregaDireta(String blEntregaDireta) {
		this.blEntregaDireta = blEntregaDireta;
	}
	public String getCotacao_sgFilial() {
		return cotacao_sgFilial;
	}
	public void setCotacao_sgFilial(String cotacao_sgFilial) {
		this.cotacao_sgFilial = cotacao_sgFilial;
	}
	public Integer getNrCotacao() {
		return nrCotacao;
	}
	public void setNrCotacao(Integer nrCotacao) {
		this.nrCotacao = nrCotacao;
	}
	public String getCtoInternacional_sgPais() {
		return ctoInternacional_sgPais;
	}
	public void setCtoInternacional_sgPais(String ctoInternacional_sgPais) {
		this.ctoInternacional_sgPais = ctoInternacional_sgPais;
	}
	public Integer getCtoInternacional_nrCrt() {
		return ctoInternacional_nrCrt;
	}
	public void setCtoInternacional_nrCrt(Integer ctoInternacional_nrCrt) {
		this.ctoInternacional_nrCrt = ctoInternacional_nrCrt;
	}
	public String getObPedidoColeta() {
		return obPedidoColeta;
	}
	public void setObPedidoColeta(String obPedidoColeta) {
		this.obPedidoColeta = obPedidoColeta;
	}
	public String getNrIdentificacaoFormatado() {
		return nrIdentificacaoFormatado;
	}
	public void setNrIdentificacaoFormatado(String nrIdentificacaoFormatado) {
		this.nrIdentificacaoFormatado = nrIdentificacaoFormatado;
	}
	public String getNmPessoa() {
		return nmPessoa;
	}
	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
	}
	private String obPedidoColeta;
}
