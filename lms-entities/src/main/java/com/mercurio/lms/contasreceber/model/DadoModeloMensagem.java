package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.DomainValue;

public class DadoModeloMensagem implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idDadoModeloMensagem;
	private ModeloMensagem idModeloMensagem;
	private String nmDadoModeloMensagem;
	private DomainValue tpDadoModeloMensagem;
	private String dsDadoModeloMensagem;
	private String dsConteudoDadoModMens;
	
	public Long getIdDadoModeloMensagem() {
		return idDadoModeloMensagem;
	}
	public void setIdDadoModeloMensagem(Long idDadoModeloMensagem) {
		this.idDadoModeloMensagem = idDadoModeloMensagem;
	}
	public ModeloMensagem getIdModeloMensagem() {
		return idModeloMensagem;
	}
	public void setIdModeloMensagem(ModeloMensagem idModeloMensagem) {
		this.idModeloMensagem = idModeloMensagem;
	}
	public String getNmDadoModeloMensagem() {
		return nmDadoModeloMensagem;
	}
	public void setNmDadoModeloMensagem(String nmDadoModeloMensagem) {
		this.nmDadoModeloMensagem = nmDadoModeloMensagem;
	}
	public DomainValue getTpDadoModeloMensagem() {
		return tpDadoModeloMensagem;
	}
	public void setTpDadoModeloMensagem(DomainValue tpDadoModeloMensagem) {
		this.tpDadoModeloMensagem = tpDadoModeloMensagem;
	}
	public String getDsDadoModeloMensagem() {
		return dsDadoModeloMensagem;
	}
	public void setDsDadoModeloMensagem(String dsDadoModeloMensagem) {
		this.dsDadoModeloMensagem = dsDadoModeloMensagem;
	}
	public String getDsConteudoDadoModMens() {
		return dsConteudoDadoModMens;
	}
	public void setDsConteudoDadoModMens(String dsConteudoDadoModMens) {
		this.dsConteudoDadoModMens = dsConteudoDadoModMens;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
