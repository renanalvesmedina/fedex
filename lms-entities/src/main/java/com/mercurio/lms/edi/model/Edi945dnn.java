package com.mercurio.lms.edi.model;

import java.util.Map;

public class Edi945dnn {

	private Long id;
	//ST
	private String transaction;
	//W06	
	private String warehouse;
	//N1
	private String name;
	//N9
	private String referenceID;
	//G62
	private String dateTransact;
	//NTE
	private String specialInstruction;
	
	private EDI945File edi945;
	
	private boolean temDanfe;
	
	private String nomeDanfeRecebido;
	
	private boolean notfisGerado;
	
	private String nomeArquivoNotfis;
	
	private String product;
	
	private Map<String, Edi945dnnLabes> dnnLabels;
	
	private String transportWay;
	
	private String divisao;
	
	public Long Id() {
		return id;
	}

	public void setEdi945Id(Long Id) {
		this.id = Id;
	}

	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReferenceID() {
		return referenceID;
	}

	public void setReferenceID(String referenceID) {
		this.referenceID = referenceID;
	}

	public String getDateTransact() {
		return dateTransact;
	}

	public void setDateTransact(String dateTransact) {
		this.dateTransact = dateTransact;
	}

	public String getSpecialInstruction() {
		return specialInstruction;
	}

	public void setSpecialInstruction(String specialInstruction) {
		this.specialInstruction = specialInstruction;
	}

	public void setEdi945(EDI945File edi945) {
		this.edi945 = edi945;
	}

	public EDI945File getEdi945() {
		return edi945;
	}

	public void setTemDanfe(boolean temDanfe) {
		this.temDanfe = temDanfe;
	}

	public boolean isTemDanfe() {
		return temDanfe;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getProduct() {
		return product;
	}

	public void setDnnLabels(Map<String, Edi945dnnLabes> dnnLabels) {
		this.dnnLabels = dnnLabels;
	}

	public Map<String, Edi945dnnLabes> getDnnLabels() {
		return dnnLabels;
	}

	public void setTransportWay(String transportWay) {
		this.transportWay = transportWay;
	}

	public String getTransportWay() {
		return transportWay;
	}

	public void setNotfisGerado(boolean notfisGerado) {
		this.notfisGerado = notfisGerado;
	}

	public boolean isNotfisGerado() {
		return notfisGerado;
	}

	public void setNomeArquivoNotfis(String nomeArquivoNotfis) {
		this.nomeArquivoNotfis = nomeArquivoNotfis;
	}

	public String getNomeArquivoNotfis() {
		return nomeArquivoNotfis;
	}

	public void setNomeDanfeRecebido(String nomeDanfeRecebido) {
		this.nomeDanfeRecebido = nomeDanfeRecebido;
	}

	public String getNomeDanfeRecebido() {
		return nomeDanfeRecebido;
	}

	public String getDivisao() {
		
		if (divisao == null)
			return "";
		else
			return divisao;
		
	}

	public void setDivisao(String d) {
		this.divisao = d;
	}
} 