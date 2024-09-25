package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

public class DiaCorteFaturamento implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long idDiaCorteFaturamento;
	private Usuario usuario;
	private YearMonthDay dtCorte;
	private DomainValue blSemanal;
	private DomainValue blDecendial;
	private DomainValue blQuinzenal;
	private DomainValue blMensal;
	private String obDiaCorteFaturamento;
	private DateTime dhAlteracao;
	
	public Long getIdDiaCorteFaturamento() {
		return idDiaCorteFaturamento;
	}
	public void setIdDiaCorteFaturamento(Long idDiaCorteFaturamento) {
		this.idDiaCorteFaturamento = idDiaCorteFaturamento;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public YearMonthDay getDtCorte() {
		return dtCorte;
	}
	public void setDtCorte(YearMonthDay dtCorte) {
		this.dtCorte = dtCorte;
	}
	public DomainValue getBlSemanal() {
		return blSemanal;
	}
	public void setBlSemanal(DomainValue blSemanal) {
		this.blSemanal = blSemanal;
	}
	public DomainValue getBlDecendial() {
		return blDecendial;
	}
	public void setBlDecendial(DomainValue blDecendial) {
		this.blDecendial = blDecendial;
	}
	public DomainValue getBlQuinzenal() {
		return blQuinzenal;
	}
	public void setBlQuinzenal(DomainValue blQuinzenal) {
		this.blQuinzenal = blQuinzenal;
	}
	public DomainValue getBlMensal() {
		return blMensal;
	}
	public void setBlMensal(DomainValue blMensal) {
		this.blMensal = blMensal;
	}
	public String getObDiaCorteFaturamento() {
		return obDiaCorteFaturamento;
	}
	public void setObDiaCorteFaturamento(String obDiaCorteFaturamento) {
		this.obDiaCorteFaturamento = obDiaCorteFaturamento;
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
