package com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto;

import java.io.Serializable;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;

public class DadoModeloMensagemDTO  implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idDadoModeloMensagem;
	private String nmDadoModeloMensagem;
	private DomainValue tpDadoModeloMensagem;
	private String dsDadoModeloMensagem;
	private String dsConteudoDadoModMens;
	
	public DadoModeloMensagemDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public DadoModeloMensagemDTO(
			String nmDadoModeloMensagem, DomainValue tpDadoModeloMensagem,
			String dsDadoModeloMensagem, String dsConteudoDadoModMens) {
		super();
		this.nmDadoModeloMensagem = nmDadoModeloMensagem;
		this.tpDadoModeloMensagem = tpDadoModeloMensagem;
		this.dsDadoModeloMensagem = dsDadoModeloMensagem;
		this.dsConteudoDadoModMens = dsConteudoDadoModMens;
	}

	public Long getIdDadoModeloMensagem() {
		return idDadoModeloMensagem;
	}
	public void setIdDadoModeloMensagem(Long idDadoModeloMensagem) {
		this.idDadoModeloMensagem = idDadoModeloMensagem;
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