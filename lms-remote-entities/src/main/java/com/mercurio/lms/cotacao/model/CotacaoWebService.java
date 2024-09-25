package com.mercurio.lms.cotacao.model;

import java.io.Serializable;

/**
 * Pojo com os parâmetros enviados pelo cliente para o web service. O nome do pojo mantém a coerência com o pojo lado
 * do LMS.
 */
public class CotacaoWebService implements Serializable{
	
	private static final long serialVersionUID = 27L;

	public static final String TP_FRETE_CIF = "C";
	public static final String TP_FRETE_FOB = "F";
	
	private String login;
	private String senha;
	private String nrIdentifClienteRem;
	private String nrIdentifClienteDest;
	private String tpFrete;
	private String tpServico;
	private String cepOrigem;
	private String cepDestino;
	private String vlMercadoria;
	private Double psReal;
	private String nrInscricaoEstadualRemetente;
	private String nrInscricaoEstadualDestinatario;
	private String tpSituacaoTributariaRemetente;
	private String tpSituacaoTributariaDestinatario;
	private Long cdDivisaoCliente;
	private String tpPessoaRemetente;
	private String tpPessoaDestinatario;
	private String tpConhecimento;
	private String tpCalculo;
	private Double psCubado;
	private Long densidade;
	private Long idClienteCalculo;
	private Long idMunicipioOrigem;
	private Long idMunicipioDestino;
	private Long idEstadoOrigem;
	private Long idEstadoDestino;
	private String tarifa;
	private String calcularFrete;
	private String calcularServicoAdicional;
	private Long qtVolumes;
	private Long idNaturezaMercadoria;
	private String pesoAferido;
	
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

	public Double getPsReal() {
		return psReal;
	}
	
	public void setPsReal(Double psReal) {
		this.psReal = psReal;
}

	public String getTpConhecimento() {
		return tpConhecimento;
	}

	public void setTpConhecimento(String tpConhecimento) {
		this.tpConhecimento = tpConhecimento;
	}

	public String getTpCalculo() {
		return tpCalculo;
	}

	public void setTpCalculo(String tpCalculo) {
		this.tpCalculo = tpCalculo;
	}

	public Double getPsCubado() {
		return psCubado;
	}

	public void setPsCubado(Double psCubado) {
		this.psCubado = psCubado;
	}

	public Long getDensidade() {
		return densidade;
	}

	public void setDensidade(Long densidade) {
		this.densidade = densidade;
	}

	public Long getIdClienteCalculo() {
		return idClienteCalculo;
	}

	public void setIdClienteCalculo(Long idClienteCalculo) {
		this.idClienteCalculo = idClienteCalculo;
	}

	public Long getIdMunicipioOrigem() {
		return idMunicipioOrigem;
	}

	public void setIdMunicipioOrigem(Long idMunicipioOrigem) {
		this.idMunicipioOrigem = idMunicipioOrigem;
	}

	public Long getIdMunicipioDestino() {
		return idMunicipioDestino;
	}

	public void setIdMunicipioDestino(Long idMunicipioDestino) {
		this.idMunicipioDestino = idMunicipioDestino;
	}

	public Long getIdEstadoOrigem() {
		return idEstadoOrigem;
	}

	public void setIdEstadoOrigem(Long idEstadoOrigem) {
		this.idEstadoOrigem = idEstadoOrigem;
	}

	public Long getIdEstadoDestino() {
		return idEstadoDestino;
	}

	public void setIdEstadoDestino(Long idEstadoDestino) {
		this.idEstadoDestino = idEstadoDestino;
	}

	public String getTarifa() {
		return tarifa;
	}

	public void setTarifa(String tarifa) {
		this.tarifa = tarifa;
	}

	public String getCalcularFrete() {
		return calcularFrete;
	}

	public void setCalcularFrete(String calcularFrete) {
		this.calcularFrete = calcularFrete;
	}

	public String getCalcularServicoAdicional() {
		return calcularServicoAdicional;
	}

	public void setCalcularServicoAdicional(String calcularServicoAdicional) {
		this.calcularServicoAdicional = calcularServicoAdicional;
	}

	public Long getQtVolumes() {
		return qtVolumes;
	}

	public void setQtVolumes(Long qtVolumes) {
		this.qtVolumes = qtVolumes;
	}

	public Long getIdNaturezaMercadoria() {
		return idNaturezaMercadoria;
	}

	public void setIdNaturezaMercadoria(Long idNaturezaMercadoria) {
		this.idNaturezaMercadoria = idNaturezaMercadoria;
	}

	public String getPesoAferido() {
		return pesoAferido;
	}

	public void setPesoAferido(String pesoAferido) {
		this.pesoAferido = pesoAferido;
	}
	
}
