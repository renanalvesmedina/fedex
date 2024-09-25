package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.configuracoes.model.Usuario;

public class RecebimentoPosLiqFatura implements Serializable {

	private static final long serialVersionUID = 2L;
	
	private Long idRecebimentoPosLiqFatura;
	private Fatura fatura;
	private Usuario usuario;
	private YearMonthDay dtRecebimento;
	private BigDecimal vlRecebimento;
	private String obRecebimento;
	private DateTime dhAlteracao;
	public Long getIdRecebimentoPosLiqFatura() {
		return idRecebimentoPosLiqFatura;
	}
	public void setIdRecebimentoPosLiqFatura(Long idRecebimentoPosLiqFatura) {
		this.idRecebimentoPosLiqFatura = idRecebimentoPosLiqFatura;
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
	public YearMonthDay getDtRecebimento() {
		return dtRecebimento;
	}
	public void setDtRecebimento(YearMonthDay dtRecebimento) {
		this.dtRecebimento = dtRecebimento;
	}
	public BigDecimal getVlRecebimento() {
		return vlRecebimento;
	}
	public void setVlRecebimento(BigDecimal vlRecebimento) {
		this.vlRecebimento = vlRecebimento;
	}
	public String getObRecebimento() {
		return obRecebimento;
	}
	public void setObRecebimento(String obRecebimento) {
		this.obRecebimento = obRecebimento;
	}
	public DateTime getDhAlteracao() {
		return dhAlteracao;
	}
	public void setDhAlteracao(DateTime dhAlteracao) {
		this.dhAlteracao = dhAlteracao;
	}
}
