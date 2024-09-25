package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

public class ServicoAdicionalWebService implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String nmServico;
	private String vlServico;
	private String dsComplemento;
	private String sgMoeda;
	
	public String getNmServico() {
		return nmServico;
	}
	public void setNmServico(String nmServico) {
		this.nmServico = nmServico;
	}
	public String getVlServico() {
		return vlServico;
	}
	public void setVlServico(String vlServico) {
		this.vlServico = vlServico;
	}
	public String getDsComplemento() {
		return dsComplemento;
	}
	public void setDsComplemento(String dsComplemento) {
		this.dsComplemento = dsComplemento;
	}
	public String getSgMoeda() {
		return sgMoeda;
	}
	public void setSgMoeda(String sgMoeda) {
		this.sgMoeda = sgMoeda;
	} 
}
