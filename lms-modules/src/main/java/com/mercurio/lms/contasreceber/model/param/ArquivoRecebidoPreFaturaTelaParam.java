package com.mercurio.lms.contasreceber.model.param;

import java.io.BufferedReader;

import org.joda.time.YearMonthDay;

import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;



public class ArquivoRecebidoPreFaturaTelaParam {
	
	private BufferedReader arquivo;
	
	private Cliente cliente;
	
	private Cedente cedente;
	
	private DivisaoCliente divisaoCliente;
	
	private YearMonthDay dtEmissao;
	
	private YearMonthDay dtVencimento;
	
	private Boolean blGerarBoleto;

	private Boolean blClienteDHL = Boolean.FALSE;

	public BufferedReader getArquivo() {
		return arquivo;
	}

	public void setArquivo(BufferedReader arquivo) {
		this.arquivo = arquivo;
	}

	public Boolean getBlGerarBoleto() {
		return blGerarBoleto;
	}

	public void setBlGerarBoleto(Boolean blGerarBoleto) {
		this.blGerarBoleto = blGerarBoleto;
	}

	public Cedente getCedente() {
		return cedente;
	}

	public void setCedente(Cedente cedente) {
		this.cedente = cedente;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public DivisaoCliente getDivisaoCliente() {
		return divisaoCliente;
	}

	public void setDivisaoCliente(DivisaoCliente divisaoCliente) {
		this.divisaoCliente = divisaoCliente;
	}

	public YearMonthDay getDtEmissao() {
		return dtEmissao;
	}

	public void setDtEmissao(YearMonthDay dtEmissao) {
		this.dtEmissao = dtEmissao;
	}

	public YearMonthDay getDtVencimento() {
		return dtVencimento;
	}

	public void setDtVencimento(YearMonthDay dtVencimento) {
		this.dtVencimento = dtVencimento;
	}

	public void setBlClienteDHL(Boolean blClienteDHL) {
		this.blClienteDHL = blClienteDHL;
}
	
	public Boolean getBlClienteDHL() {
		return blClienteDHL;
	}
	
	public boolean isDHL() {
		return Boolean.TRUE.equals(blClienteDHL);
	}

}
