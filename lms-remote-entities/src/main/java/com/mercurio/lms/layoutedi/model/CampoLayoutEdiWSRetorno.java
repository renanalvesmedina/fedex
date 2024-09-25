package com.mercurio.lms.layoutedi.model;

/**
 * Classe que carrega as informações dos campos do layout dos clientes do EDI
 * necessárias para o WS.
 * 
 * @author ThiagoFA
 * 
 */
public class CampoLayoutEdiWSRetorno {

	/* DADOS DE COMPOSICAO_LAYOUT_EDI */
	private Long idComposicaoLayout;
	private Long idCampoLayout;
	private String formato;
	private String complementoFormato;
	private Integer tamanho;
	private Integer qtdDecimais;
	private Integer posicao;
	private String valorDefault;
	private String xpath;

	/* DADOS DE CAMPO_LAYOUT_EDI */
	private String nomeCampo;
	private String campoTabelaTemp;
	private String tipoDePara;
	private String nomeComplemento;
	private String obrigatorio;
	private String temDePara;

	
	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}	
	
	public String getValorDefault() {
		return valorDefault;
	}

	public void setValorDefault(String valorDefault) {
		this.valorDefault = valorDefault;
	}

	public String getTemDePara() {
		return temDePara;
	}

	public void setTemDePara(String temDePara) {
		this.temDePara = temDePara;
	}

	public Long getIdComposicaoLayout() {
		return idComposicaoLayout;
	}

	public void setIdComposicaoLayout(Long idComposicaoLayout) {
		this.idComposicaoLayout = idComposicaoLayout;
	}

	public Long getIdCampoLayout() {
		return idCampoLayout;
	}

	public void setIdCampoLayout(Long idCampoLayout) {
		this.idCampoLayout = idCampoLayout;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getComplementoFormato() {
		return complementoFormato;
	}

	public void setComplementoFormato(String complementoFormato) {
		this.complementoFormato = complementoFormato;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getQtdDecimais() {
		return qtdDecimais;
	}

	public void setQtdDecimais(Integer qtdDecimais) {
		this.qtdDecimais = qtdDecimais;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public String getNomeCampo() {
		return nomeCampo;
	}

	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}

	public String getCampoTabelaTemp() {
		return campoTabelaTemp;
	}

	public void setCampoTabelaTemp(String campoTabelaTemp) {
		this.campoTabelaTemp = campoTabelaTemp;
	}

	public String getTipoDePara() {
		return tipoDePara;
	}

	public void setTipoDePara(String tipoDePara) {
		this.tipoDePara = tipoDePara;
	}

	public String getNomeComplemento() {
		return nomeComplemento;
	}

	public void setNomeComplemento(String nomeComplemento) {
		this.nomeComplemento = nomeComplemento;
	}

	public String getObrigatorio() {
		return obrigatorio;
	}

	public void setObrigatorio(String obrigatorio) {
		this.obrigatorio = obrigatorio;
	}
}
