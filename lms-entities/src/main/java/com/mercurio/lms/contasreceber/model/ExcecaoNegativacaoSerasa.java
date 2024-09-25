package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.Usuario;

public class ExcecaoNegativacaoSerasa implements Serializable {
		
	private static final long serialVersionUID = 1L;
	private Long idExcecaoNegativacaoSerasa;
	private Fatura fatura;
	private Usuario usuario;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private String obExcecaoNegativacaoSerasa;
	private DateTime dhAlteracao;
	
	public Long getIdExcecaoNegativacaoSerasa() {
		return idExcecaoNegativacaoSerasa;
	}
	public void setIdExcecaoNegativacaoSerasa(Long idExcecaoNegativacaoSerasa) {
		this.idExcecaoNegativacaoSerasa = idExcecaoNegativacaoSerasa;
	}
	public Fatura getFatura() {
		return fatura;
	}
	public void setFatura(Fatura fatura) {
		this.fatura = fatura;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	public String getObExcecaoNegativacaoSerasa() {
		return obExcecaoNegativacaoSerasa;
	}
	public void setObExcecaoNegativacaoSerasa(String obExcecaoNegativacaoSerasa) {
		this.obExcecaoNegativacaoSerasa = obExcecaoNegativacaoSerasa;
	}
	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}
	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
