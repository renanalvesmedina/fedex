package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import com.mercurio.lms.expedicao.model.Dimensao;

/**
 * Pojo com os parâmetros enviados pelo cliente para o web service. O nome do pojo mantém a coerência com o pojo lado
 * do LMS.
 * @author lucianos - 01/2009
 *
 */
public class CotacaoWebService implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String login;
	
	private String senha;

	/** número de identificação do remetente */
	private String nrIdentifClienteRem;

	/** número de identificação do destinatário */
	private String nrIdentifClienteDest;
	
	/** tipo de frete C - CIF ou F - FOB */ 
	private String tpFrete;
	
	/** tipo de serviço aéreo/rodoviário */
	private String tpServico;
	
	private String cepOrigem;
	
	private String cepDestino;
	
	private String vlMercadoria;

	/** peso real */
	private String psReal;
	
	private String psCubado;
	
	private String psAforado;
	
	private String nrInscricaoEstadualRemetente;

	private String nrInscricaoEstadualDestinatario;
	
	private String tpSituacaoTributariaRemetente;
	
	private String tpSituacaoTributariaDestinatario;
	
	private Long cdDivisaoCliente;

	private String tpPessoaRemetente;
	
	private String tpPessoaDestinatario;

	private List<Dimensao> dimensoes;
	
	private String pesoAforado;
	
	private Long qtVolumes;

	public String getCepDestino() {
		return cepDestino;
	}

	public void setCepDestino(String cepDestino) {
		this.cepDestino = cepDestino;
	}

	public String getCepOrigem() {
		return cepOrigem;
	}

	public void setCepOrigem(String cepOrigem) {
		this.cepOrigem = cepOrigem;
	}

	public String getNrIdentifClienteDest() {
		return nrIdentifClienteDest;
	}

	public void setNrIdentifClienteDest(String nrIdentifClienteDest) {
		this.nrIdentifClienteDest = nrIdentifClienteDest;
	}

	public String getNrIdentifClienteRem() {
		return nrIdentifClienteRem;
	}

	public void setNrIdentifClienteRem(String nrIdentifClienteRem) {
		this.nrIdentifClienteRem = nrIdentifClienteRem;
	}



	public String getTpFrete() {
		return tpFrete;
	}

	public void setTpFrete(String tpFrete) {
		this.tpFrete = tpFrete;
	}

	public String getTpServico() {
		return tpServico;
	}

	public void setTpServico(String tpServico) {
		this.tpServico = tpServico;
	}

	public String getTpSituacaoTributariaRemetente() {
		return tpSituacaoTributariaRemetente;
	}

	public void setTpSituacaoTributariaRemetente(
			String tpSituacaoTributariaRemetente) {
		this.tpSituacaoTributariaRemetente = tpSituacaoTributariaRemetente;
	}

	public String getTpSituacaoTributariaDestinatario() {
		return tpSituacaoTributariaDestinatario;
	}

	public void setTpSituacaoTributariaDestinatario(
			String tpSituacaoTributariaDestinatario) {
		this.tpSituacaoTributariaDestinatario = tpSituacaoTributariaDestinatario;
	}

	public String getNrInscricaoEstadualRemetente() {
		return nrInscricaoEstadualRemetente;
	}

	public void setNrInscricaoEstadualRemetente(String nrInscricaoEstadualRemetente) {
		this.nrInscricaoEstadualRemetente = nrInscricaoEstadualRemetente;
	}

	public String getNrInscricaoEstadualDestinatario() {
		return nrInscricaoEstadualDestinatario;
	}

	public void setNrInscricaoEstadualDestinatario(
			String nrInscricaoEstadualDestinatario) {
		this.nrInscricaoEstadualDestinatario = nrInscricaoEstadualDestinatario;
	}

	public String getTpPessoaDestinatario() {
		return tpPessoaDestinatario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setTpPessoaDestinatario(String tpPessoaDestinatario) {
		this.tpPessoaDestinatario = tpPessoaDestinatario;
	}

	public Long getCdDivisaoCliente() {
		return cdDivisaoCliente;
	}

	public void setCdDivisaoCliente(Long cdDivisaoCliente) {
		this.cdDivisaoCliente = cdDivisaoCliente;
	}

	public String getTpPessoaRemetente() {
		return tpPessoaRemetente;
	}

	public void setTpPessoaRemetente(String tpPessoaRemetente) {
		this.tpPessoaRemetente = tpPessoaRemetente;
	}

	public String getVlMercadoria() {
		return vlMercadoria;
	}

	public void setVlMercadoria(String vlMercadoria) {
		this.vlMercadoria = vlMercadoria;
	}

	public String getPsReal() {
		return psReal;
	}
	
	public void setPsReal(String psReal) {
		this.psReal = psReal;
}

	public String getPsCubado() {
		return psCubado;
	}

	public void setPsCubado(String psCubado) {
		this.psCubado = psCubado;
	}
	
	public List<Dimensao> getDimensoes() {
		return dimensoes;
}

	public void setDimensoes(List<Dimensao> dimensoes) {
		this.dimensoes = dimensoes;
	}

	public String getPesoAforado() {
		return pesoAforado;
	}

	public void setPesoAforado(String pesoAforado) {
		this.pesoAforado = pesoAforado;
	}

	public String getPsAforado() {
		return psAforado;
	}

	public void setPsAforado(String psAforado) {
		this.psAforado = psAforado;
	}

	public Long getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Long qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

}
