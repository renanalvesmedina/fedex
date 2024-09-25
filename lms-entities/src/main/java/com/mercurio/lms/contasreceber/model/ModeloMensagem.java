package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class ModeloMensagem implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idModeloMensagem;
	private com.mercurio.lms.configuracoes.model.Usuario usuario;
	private String dsModeloMensagem;
	private DomainValue tpModeloMensagem;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private String dcModeloAssunto;
	private String dcModeloCorpo;
	private DateTime dhAlteracao;
	private List<DadoModeloMensagem> dadosModeloMensagem;
	
	public ModeloMensagem() {
	}
	
	public ModeloMensagem(Long idModeloMensagem) {
		this.idModeloMensagem = idModeloMensagem;
	}
	public Long getIdModeloMensagem() {
		return idModeloMensagem;
	}
	public void setIdModeloMensagem(Long idModeloMensagem) {
		this.idModeloMensagem = idModeloMensagem;
	}
	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}
	public String getDsModeloMensagem() {
		return dsModeloMensagem;
	}
	public void setDsModeloMensagem(String dsModeloMensagem) {
		this.dsModeloMensagem = dsModeloMensagem;
	}
	public DomainValue getTpModeloMensagem() {
		return tpModeloMensagem;
	}
	public void setTpModeloMensagem(DomainValue tpModeloMensagem) {
		this.tpModeloMensagem = tpModeloMensagem;
	}
	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}
	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}
	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}
	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}
	public String getDcModeloAssunto() {
		return dcModeloAssunto;
	}
	public void setDcModeloAssunto(String dcModeloAssunto) {
		this.dcModeloAssunto = dcModeloAssunto;
	}
	public String getDcModeloCorpo() {
		return dcModeloCorpo;
	}
	public void setDcModeloCorpo(String dcModeloCorpo) {
		this.dcModeloCorpo = dcModeloCorpo;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}
	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}
	public void setDadosModeloMensagem(List<DadoModeloMensagem> dadosModeloMensagem) {
		this.dadosModeloMensagem = dadosModeloMensagem;
	}
	
	public List<DadoModeloMensagem> getDadosModeloMensagem() {
		return dadosModeloMensagem;
	}
}
