package com.mercurio.lms.layoutedi.model;

public class DadosVolume {
	private Long idComposicaoVolume;
	private Long idComposicaoLayoutEDI;
	private Long idCampoLayoutEDI;
	private Integer ordem;
	private Integer tamanho;
	private String formato;
	private String complementoPreenchimento;
	private String alinhamento;
	private String indicadorCalculo;
	private String nomeCampo;
	private String nomeComplemento;
	
	
	public String getNomeComplemento() {
		return nomeComplemento;
	}
	public void setNomeComplemento(String nomeComplemento) {
		this.nomeComplemento = nomeComplemento;
	}
	public Long getIdComposicaoVolume() {
		return idComposicaoVolume;
	}
	public void setIdComposicaoVolume(Long idComposicaoVolume) {
		this.idComposicaoVolume = idComposicaoVolume;
	}
	public Long getIdComposicaoLayoutEDI() {
		return idComposicaoLayoutEDI;
	}
	public void setIdComposicaoLayoutEDI(Long idComposicaoLayoutEDI) {
		this.idComposicaoLayoutEDI = idComposicaoLayoutEDI;
	}
	public Long getIdCampoLayoutEDI() {
		return idCampoLayoutEDI;
	}
	public void setIdCampoLayoutEDI(Long idCampoLayoutEDI) {
		this.idCampoLayoutEDI = idCampoLayoutEDI;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public Integer getTamanho() {
		return tamanho;
	}
	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}
	public String getFormato() {
		return formato;
	}
	public void setFormato(String formato) {
		this.formato = formato;
	}
	public String getComplementoPreenchimento() {
		return complementoPreenchimento;
	}
	public void setComplementoPreenchimento(String complementoPreenchimento) {
		this.complementoPreenchimento = complementoPreenchimento;
	}
	public String getAlinhamento() {
		return alinhamento;
	}
	public void setAlinhamento(String alinhamento) {
		this.alinhamento = alinhamento;
	}
	public String getIndicadorCalculo() {
		return indicadorCalculo;
	}
	public void setIndicadorCalculo(String indicadorCalculo) {
		this.indicadorCalculo = indicadorCalculo;
	}
	public String getNomeCampo() {
		return nomeCampo;
	}
	public void setNomeCampo(String nomeCampo) {
		this.nomeCampo = nomeCampo;
	}
	
	
}
