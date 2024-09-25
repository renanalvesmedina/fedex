package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo com o cálculo do frete que será retornado para o cliente. O nome do pojo mantém a coerência com o nome do pojo
 * do no LMS
 * @author lucianos - 01/2009
 *
 *
 */
public class CalculoFreteWebServiceRetorno implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** parcelas que compõe o cálculo */
	private List<ParcelasFreteWebService> parcelas;
	private List<ServicoAdicionalWebService> servicosAdicionais;
	private String nmRemetente;
	private String nmDestinatario;
	private String vlDesconto;
	private String vlTotalFrete;
	private String vlTotalServico;
	private String vlTotalCtrc;
	private String vlICMSubstituicaoTributaria;
	private Boolean blIncidenciaIcmsPedagio;
	private String vlDevido;
	private String vlImposto;
	private String vlTotalTributos;
	private String nrDDDFilialOrigem;
	private String nrTelefoneFilialOrigem;
	private String nrDDDFilialDestino;
	private String nrTelefoneFilialDestino;	
	private String nmMunicipioOrigem;
	private String nmMunicipioDestino;	
	private Long prazoEntrega;
	/** lista erros ocorridos durante o processo */ 
	private List<String> errorList;

	public List<String> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<String> errorList) {
		this.errorList = errorList;
	}

	public List<ParcelasFreteWebService> getParcelas() {
		return parcelas;
	}
	public void setParcelas(List<ParcelasFreteWebService> parcelas) {
		this.parcelas = parcelas;
	}

	public List<ServicoAdicionalWebService> getServicosAdicionais() {
		return servicosAdicionais;
	}
	public void setServicosAdicionais(
			List<ServicoAdicionalWebService> servicosAdicionais) {
		this.servicosAdicionais = servicosAdicionais;
	}
	
	public Long getPrazoEntrega() {
		return prazoEntrega;
	}

	public void setPrazoEntrega(Long prazoEntrega) {
		this.prazoEntrega = prazoEntrega;
	}

	public String getNmMunicipioDestino() {
		return nmMunicipioDestino;
	}

	public void setNmMunicipioDestino(String nmMunicipioDestino) {
		this.nmMunicipioDestino = nmMunicipioDestino;
	}

	public String getNmMunicipioOrigem() {
		return nmMunicipioOrigem;
	}

	public void setNmMunicipioOrigem(String nmMunicipioOrigem) {
		this.nmMunicipioOrigem = nmMunicipioOrigem;
	}

	public String getNmRemetente() {
		return nmRemetente;
	}

	public void setNmRemetente(String nmRemetente) {
		this.nmRemetente = nmRemetente;
	}

	public String getNmDestinatario() {
		return nmDestinatario;
	}

	public void setNmDestinatario(String nmDestinatario) {
		this.nmDestinatario = nmDestinatario;
	}

	public String getNrTelefoneFilialOrigem() {
		return nrTelefoneFilialOrigem;
	}

	public void setNrTelefoneFilialOrigem(String nrTelefoneFilialOrigem) {
		this.nrTelefoneFilialOrigem = nrTelefoneFilialOrigem;
	}

	public String getNrTelefoneFilialDestino() {
		return nrTelefoneFilialDestino;
	}

	public void setNrTelefoneFilialDestino(String nrTelefoneFilialDestino) {
		this.nrTelefoneFilialDestino = nrTelefoneFilialDestino;
	}

	public String getNrDDDFilialOrigem() {
		return nrDDDFilialOrigem;
	}

	public void setNrDDDFilialOrigem(String nrDDDFilialOrigem) {
		this.nrDDDFilialOrigem = nrDDDFilialOrigem;
	}

	public String getNrDDDFilialDestino() {
		return nrDDDFilialDestino;
	}

	public void setNrDDDFilialDestino(String nrDDDFilialDestino) {
		this.nrDDDFilialDestino = nrDDDFilialDestino;
	}

	public String getVlDesconto() {
		return vlDesconto;
	}

	public void setVlDesconto(String vlDesconto) {
		this.vlDesconto = vlDesconto;
	}

	public String getVlTotalFrete() {
		return vlTotalFrete;
}

	public void setVlTotalFrete(String vlTotalFrete) {
		this.vlTotalFrete = vlTotalFrete;
	}

	public String getVlTotalServico() {
		return vlTotalServico;
	}

	public void setVlTotalServico(String vlTotalServico) {
		this.vlTotalServico = vlTotalServico;
	}

	public String getVlTotalCtrc() {
		return vlTotalCtrc;
	}

	public void setVlTotalCtrc(String vlTotalCtrc) {
		this.vlTotalCtrc = vlTotalCtrc;
	}

	public String getVlICMSubstituicaoTributaria() {
		return vlICMSubstituicaoTributaria;
	}

	public void setVlICMSubstituicaoTributaria(String vlICMSubstituicaoTributaria) {
		this.vlICMSubstituicaoTributaria = vlICMSubstituicaoTributaria;
	}

	public String getVlImposto() {
		return vlImposto;
	}

	public void setVlImposto(String vlImposto) {
		this.vlImposto = vlImposto;
	}
	public String getVlTotalTributos() {
		return vlTotalTributos;
	}
	public void setVlTotalTributos(String vlTotalTributos) {
		this.vlTotalTributos = vlTotalTributos;
	}
	public String getVlDevido() {
		return vlDevido;
	}
	public void setVlDevido(String vlDevido) {
		this.vlDevido = vlDevido;
	}
	public Boolean getBlIncidenciaIcmsPedagio() {
		return blIncidenciaIcmsPedagio;
	}
	public void setBlIncidenciaIcmsPedagio(Boolean blIncidenciaIcmsPedagio) {
		this.blIncidenciaIcmsPedagio = blIncidenciaIcmsPedagio;
	}

}
