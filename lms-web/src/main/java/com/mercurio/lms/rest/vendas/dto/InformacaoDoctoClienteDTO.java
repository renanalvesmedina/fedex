package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseDTO;

public class InformacaoDoctoClienteDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	
	private Long idInformacaoDoctoCliente;
	private String dsCampo;
	private String tpCampo;
	private String dsFormatacao;
	private Integer nrTamanho;
	private boolean blOpcional;
	private String dsValorPadrao;
	private boolean blValorFixo;
	
	public InformacaoDoctoClienteDTO() {
	}
	
	public InformacaoDoctoClienteDTO(Long idInformacaoDoctoCliente,
			String dsCampo, String tpCampo, String dsFormatacao, Integer nrTamanho,
			boolean blOpcional, String dsValorPadrao, boolean blValorFixo) {
		super();
		this.idInformacaoDoctoCliente = idInformacaoDoctoCliente;
		this.dsCampo = dsCampo;
		this.tpCampo = tpCampo;
		this.dsFormatacao = dsFormatacao;
		this.nrTamanho = nrTamanho;
		this.blOpcional = blOpcional;
		this.dsValorPadrao = dsValorPadrao;
		this.blValorFixo = blValorFixo;
	}

	public Long getIdInformacaoDoctoCliente() {
		return idInformacaoDoctoCliente;
	}
	public void setIdInformacaoDoctoCliente(Long idInformacaoDoctoCliente) {
		this.idInformacaoDoctoCliente = idInformacaoDoctoCliente;
	}
	public String getDsCampo() {
		return dsCampo;
	}
	public void setDsCampo(String dsCampo) {
		this.dsCampo = dsCampo;
	}
	public String getTpCampo() {
		return tpCampo;
	}
	public void setTpCampo(String tpCampo) {
		this.tpCampo = tpCampo;
	}
	public String getDsFormatacao() {
		return dsFormatacao;
	}
	public void setDsFormatacao(String dsFormatacao) {
		this.dsFormatacao = dsFormatacao;
	}
	public Integer getNrTamanho() {
		return nrTamanho;
	}
	public void setNrTamanho(Integer nrTamanho) {
		this.nrTamanho = nrTamanho;
	}
	public boolean isBlOpcional() {
		return blOpcional;
	}
	public void setBlOpcional(boolean blOpcional) {
		this.blOpcional = blOpcional;
	}
	public String getDsValorPadrao() {
		return dsValorPadrao;
	}
	public void setDsValorPadrao(String dsValorPadrao) {
		this.dsValorPadrao = dsValorPadrao;
	}
	public boolean isBlValorFixo() {
		return blValorFixo;
	}
	public void setBlValorFixo(boolean blValorFixo) {
		this.blValorFixo = blValorFixo;
	}
	
	
}
