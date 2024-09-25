package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.mercurio.adsm.framework.model.DomainValue;

public class ClienteEDI implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long idClienteEDI;
	
	private DomainValue tpGeracao;
	
	private Boolean rocEntrega;
	
	private DomainValue ocoEntrega;
	
	private String cpRemetente;
	
	private String cpDestinatario;
	
	private String serieNf;
	
	private DomainValue recebeLocal;
	
	private String nmContato;
	
	private String foneContato;
	
	private String cpContato;
			
	public DomainValue getTpGeracao() {
		return tpGeracao;
	}

	public void setTpGeracao(DomainValue tpGeracao) {
		this.tpGeracao = tpGeracao;
	}

	public DomainValue getOcoEntrega() {
		return ocoEntrega;
	}

	public void setOcoEntrega(DomainValue ocoEntrega) {
		this.ocoEntrega = ocoEntrega;
	}

	public String getCpRemetente() {
		return cpRemetente;
	}

	public void setCpRemetente(String cpRemetente) {
		this.cpRemetente = cpRemetente;
	}

	public String getCpDestinatario() {
		return cpDestinatario;
	}

	public void setCpDestinatario(String cpDestinatario) {
		this.cpDestinatario = cpDestinatario;
	}

	public String getSerieNf() {
		return serieNf;
	}

	public void setSerieNf(String serieNf) {
		this.serieNf = serieNf;
	}

	public DomainValue getRecebeLocal() {
		return recebeLocal;
	}

	public void setRecebeLocal(DomainValue recebeLocal) {
		this.recebeLocal = recebeLocal;
	}

	public String getNmContato() {
		return nmContato;
	}

	public void setNmContato(String nmContato) {
		this.nmContato = nmContato;
	}
	

	public String getFoneContato() {
		return foneContato;
	}

	public void setFoneContato(String foneContato) {
		this.foneContato = foneContato;
	}

	public String getCpContato() {
		return cpContato;
	}

	public void setCpContato(String cpContato) {
		this.cpContato = cpContato;
	}

	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		
		/*Id Cliente EDI */
		bean.put("idClienteEdi", this.getIdClienteEDI());
		
		/*Tipo de geração*/
		bean.put("tpGeracao", this.getTpGeracao().getValue());
		
		/*Recebe oco entrega*/
		bean.put("rocEntrega", this.getRocEntrega().booleanValue());
		
		/*Oco. Entrega*/
		bean.put("ocoEntrega", this.getOcoEntrega().getValue());
		
		/*Cx postal remetente*/
		bean.put("cpRemetente", this.getCpRemetente());
		
		/*Cx postal destinatario*/
		bean.put("cpDestinatario", this.getCpDestinatario());
		
		/*Serie nota*/
		bean.put("serieNf", this.getSerieNf());
		
		/*Recebe localizacao*/
		bean.put("recebeLocal", this.getRecebeLocal().getValue());
		
		/*Nome contato*/
		bean.put("nmContato", this.getNmContato());
		
		/*Telefone contato*/
		bean.put("foneContato", this.getFoneContato());
		
		/*Caixa postal contato*/
		bean.put("cpContato", this.getCpContato());
		
		return bean;
	}

	public Long getIdClienteEDI() {
		return idClienteEDI;
	}

	public void setIdClienteEDI(Long idClienteEDI) {
		this.idClienteEDI = idClienteEDI;
	}

	public Boolean getRocEntrega() {
		return rocEntrega;
	}

	public void setRocEntrega(Boolean rocEntrega) {
		this.rocEntrega = rocEntrega;
	}
}
