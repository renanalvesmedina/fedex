package com.mercurio.lms.rest.contasareceber.diascortefaturamento.dto;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.contasreceber.model.DiaCorteFaturamento;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.util.session.SessionUtils;
 
public class DiasCorteFaturamentoDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
 
	private YearMonthDay dataCorte;
	private String dataCorteFormatada;
	private UsuarioDTO usuario;
	private DomainValue tpFaturarSemanal;
	private DomainValue tpFaturarDecendial;
	private DomainValue tpFaturarQuinzenal;
	private DomainValue tpFaturarMensal;
	private String observacao;
	private DateTime dataAlteracao;
	private String dataAlteracaoFormatada;
	
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public YearMonthDay getDataCorte() {
		return dataCorte;
	}
	public void setDataCorte(YearMonthDay dataCorte) {
		this.dataCorte = dataCorte;
	}
	public DomainValue getTpFaturarSemanal() {
		return tpFaturarSemanal;
	}
	public void setTpFaturarSemanal(DomainValue tpFaturarSemanal) {
		this.tpFaturarSemanal = tpFaturarSemanal;
	}
	public DomainValue getTpFaturarDecendial() {
		return tpFaturarDecendial;
	}
	public void setTpFaturarDecendial(DomainValue tpFaturarDecendial) {
		this.tpFaturarDecendial = tpFaturarDecendial;
	}
	public DomainValue getTpFaturarQuinzenal() {
		return tpFaturarQuinzenal;
	}
	public void setTpFaturarQuinzenal(DomainValue tpFaturarQuinzenal) {
		this.tpFaturarQuinzenal = tpFaturarQuinzenal;
	}
	public DomainValue getTpFaturarMensal() {
		return tpFaturarMensal;
	}
	public void setTpFaturarMensal(DomainValue tpFaturarMensal) {
		this.tpFaturarMensal = tpFaturarMensal;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public DateTime getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(DateTime dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDataCorteFormatada() {
		return dataCorteFormatada;
	}
	public void setDataCorteFormatada(String dataCorteFormatada) {
		this.dataCorteFormatada = dataCorteFormatada;
	}
	public String getDataAlteracaoFormatada() {
		return dataAlteracaoFormatada;
	}
	public void setDataAlteracaoFormatada(String dataAlteracaoFormatada) {
		this.dataAlteracaoFormatada = dataAlteracaoFormatada;
	}
	
	public DiaCorteFaturamento build(DiaCorteFaturamento current){
		DiaCorteFaturamento diaCorteFaturamento = current;
		diaCorteFaturamento.setUsuario(SessionUtils.getUsuarioLogado());
		diaCorteFaturamento.setDtCorte(getDataCorte());
		diaCorteFaturamento.setBlSemanal(getTpFaturarSemanal());
		diaCorteFaturamento.setBlDecendial(getTpFaturarDecendial());
		diaCorteFaturamento.setBlQuinzenal(getTpFaturarQuinzenal());
		diaCorteFaturamento.setBlMensal(getTpFaturarMensal());
		diaCorteFaturamento.setObDiaCorteFaturamento(getObservacao());
		
		return diaCorteFaturamento;
	}
	
} 
