package com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.contasreceber.model.DadoModeloMensagem;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;

public class ModelosDadosMensagensDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;
	private Long idModeloMensagem;
	private String nomeDado;
	private DomainValue tpDado;
	private String descricao;
	private String conteudo;
	
	public ModelosDadosMensagensDTO() {
	}
	
	public Long getIdModeloMensagem() {
		return idModeloMensagem;
	}
	public void setIdModeloMensagem(Long idModeloMensagem) {
		this.idModeloMensagem = idModeloMensagem;
	}
	public String getNomeDado() {
		return nomeDado;
	}
	public void setNomeDado(String nomeDado) {
		this.nomeDado = nomeDado;
	}
	public DomainValue getTpDado() {
		return tpDado;
	}
	public void setTpDado(DomainValue tpDado) {
		this.tpDado = tpDado;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	
	public DadoModeloMensagem build(){
		DadoModeloMensagem modelo = new DadoModeloMensagem();
		modelo.setDsConteudoDadoModMens(getConteudo());
		modelo.setIdModeloMensagem(new ModeloMensagem(getIdModeloMensagem()));
		modelo.setIdDadoModeloMensagem(getId());
		modelo.setNmDadoModeloMensagem(getNomeDado());
		modelo.setTpDadoModeloMensagem(getTpDado());
		modelo.setDsDadoModeloMensagem(getDescricao());
		return modelo;
	}
}
