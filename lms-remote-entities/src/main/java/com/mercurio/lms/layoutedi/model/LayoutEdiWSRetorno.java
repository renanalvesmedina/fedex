package com.mercurio.lms.layoutedi.model;

import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;

/**
 * Classe responsável por carregar as informações dos layouts dos clientes do
 * EDI necessárias para o WS.
 * 
 * @author ThiagoFA
 * 
 */
public class LayoutEdiWSRetorno {

	private Long idRegistroLayout;
	private String identificador;
	private String nomeIdentificador;
	private Integer tamanhoRegistro;
	private Integer ordem;
	private String descricao;
	private Long idRegistroLayoutPai;
	private List<CampoLayoutEdiWSRetorno> campos;
	private List<LayoutEdiWSRetorno> filhos;
	private LayoutEdiWSRetorno pai;
	private Boolean principal;
	private Long idDePara;
	private Integer ocorrencias;
	private DomainValue preenchimento;

	public Long getIdDePara() {
		return idDePara;
	}

	public void setIdDePara(Long idDePara) {
		this.idDePara = idDePara;
	}

	public String getNomeIdentificador() {
		return nomeIdentificador;
	}

	public void setNomeIdentificador(String nomeIdentificador) {
		this.nomeIdentificador = nomeIdentificador;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public List<LayoutEdiWSRetorno> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<LayoutEdiWSRetorno> filhos) {
		this.filhos = filhos;
	}

	public LayoutEdiWSRetorno getPai() {
		return pai;
	}

	public void setPai(LayoutEdiWSRetorno pai) {
		this.pai = pai;
	}

	public Long getIdRegistroLayout() {
		return idRegistroLayout;
	}

	public void setIdRegistroLayout(Long idRegistroLayout) {
		this.idRegistroLayout = idRegistroLayout;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public Integer getTamanhoRegistro() {
		return tamanhoRegistro;
	}

	public void setTamanhoRegistro(Integer tamanhoRegistro) {
		this.tamanhoRegistro = tamanhoRegistro;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Long getIdRegistroLayoutPai() {
		return idRegistroLayoutPai;
	}

	public void setIdRegistroLayoutPai(Long idRegistroLayoutPai) {
		this.idRegistroLayoutPai = idRegistroLayoutPai;
	}

	public List<CampoLayoutEdiWSRetorno> getCampos() {
		return campos;
	}

	public void setCampos(List<CampoLayoutEdiWSRetorno> campos) {
		this.campos = campos;
	}

	public Integer getOcorrencias() {
		return ocorrencias;
	}

	public void setOcorrencias(Integer ocorrencias) {
		this.ocorrencias = ocorrencias;
	}

	public DomainValue getPreenchimento() {
		return preenchimento;
	}

	public void setPreenchimento(DomainValue preenchimento) {
		this.preenchimento = preenchimento;
	}
}
