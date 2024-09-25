package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import org.joda.time.YearMonthDay;



public class ReajusteTabelaPreco implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long idTabelaBase;
	
	private Long idTabelaNova;
	
	private Long idFilial;
	
	private Long nrReajuste;
	
	private BigDecimal percentualReajusteGeral;
	
	private YearMonthDay dtVigenciaInicial;
	
	private Date dtVigenciaInicialDate;
	
	private YearMonthDay dtVigenciaFinal;
	
	private String tipo;
	
	private String subTipo;
	
	private Long idUsuario;
	
	private String usuario;
	
	private Long[] selection;
	
	private String versao;

	private String dtGeracao;
	
	private YearMonthDay dtAgendamento;
	
	private YearMonthDay dtEfetivacao;
	
	private String checkedEfetivacao;
	
	private boolean checkedFechaVigenciaTabelaBase;
	
	private String tabelaBase;
	
	
	public ReajusteTabelaPreco() { 
	}




	public Long getId() {
		return id;
	}




	public void setId(Long id) {
		this.id = id;
	}



	public Long getIdTabelaNova() {
		return idTabelaNova;
	}




	public void setIdTabelaNova(Long idTabelaNova) {
		this.idTabelaNova = idTabelaNova;
	}




	public Long getIdFilial() {
		return idFilial;
	}




	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}



	public Long getNrReajuste() {
		return nrReajuste;
	}




	public void setNrReajuste(Long nrReajuste) {
		this.nrReajuste = nrReajuste;
	}




	public BigDecimal getPercentualReajusteGeral() {
		return percentualReajusteGeral;
	}




	public void setPercentualReajusteGeral(BigDecimal percentualReajusteGeral) {
		this.percentualReajusteGeral = percentualReajusteGeral;
	}


	public String getTipo() {
		return tipo;
	}




	public void setTipo(String tipo) {
		this.tipo = tipo;
	}




	public String getSubTipo() {
		return subTipo;
	}




	public void setSubTipo(String subTipo) {
		this.subTipo = subTipo;
	}




	public Long getIdUsuario() {
		return idUsuario;
	}




	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}




	public String getUsuario() {
		return usuario;
	}




	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}




	public Long[] getSelection() {
		return selection;
	}




	public void setSelection(Long[] selection) {
		this.selection = selection;
	}

	public String getVersao() {
		return versao;
	}




	public void setVersao(String versao) {
		this.versao = versao;
	}



	public String getCheckedEfetivacao() {
		return checkedEfetivacao;
	}




	public void setCheckedEfetivacao(String checkedEfetivacao) {
		this.checkedEfetivacao = checkedEfetivacao;
	}
	


	public boolean isCheckedFechaVigenciaTabelaBase() {
		return checkedFechaVigenciaTabelaBase;
	}




	public void setCheckedFechaVigenciaTabelaBase(boolean checkedFechaVigenciaTabelaBase) {
		this.checkedFechaVigenciaTabelaBase = checkedFechaVigenciaTabelaBase;
	}




	public Long getIdTabelaBase() {
		return idTabelaBase;
	}




	public void setIdTabelaBase(Long idTabelaBase) {
		this.idTabelaBase = idTabelaBase;
	}





	public String getDtGeracao() {
		return dtGeracao;
	}




	public void setDtGeracao(String dtGeracao) {
		this.dtGeracao = dtGeracao;
	}





	public Date getDtVigenciaInicialDate() {
		return dtVigenciaInicialDate;
	}




	public void setDtVigenciaInicialDate(Date dtVigenciaInicialDate) {
		this.dtVigenciaInicialDate = dtVigenciaInicialDate;
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




	public YearMonthDay getDtAgendamento() {
		return dtAgendamento;
	}




	public void setDtAgendamento(YearMonthDay dtAgendamento) {
		this.dtAgendamento = dtAgendamento;
	}




	public YearMonthDay getDtEfetivacao() {
		return dtEfetivacao;
	}




	public void setDtEfetivacao(YearMonthDay dtEfetivacao) {
		this.dtEfetivacao = dtEfetivacao;
	}




	public String getTabelaBase() {
		return tabelaBase;
	}




	public void setTabelaBase(String tabelaBase) {
		this.tabelaBase = tabelaBase;
	}




}
