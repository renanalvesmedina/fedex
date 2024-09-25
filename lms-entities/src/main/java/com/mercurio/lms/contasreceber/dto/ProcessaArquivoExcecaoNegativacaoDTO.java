package com.mercurio.lms.contasreceber.dto;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import com.mercurio.adsm.framework.BusinessException;

public class ProcessaArquivoExcecaoNegativacaoDTO {

	private String filial;
	private String fatura;
	private String dtInicial;
	private String dtFinal;
	private String observacao;

	public ProcessaArquivoExcecaoNegativacaoDTO() {
	}
	
	public ProcessaArquivoExcecaoNegativacaoDTO(String line) {
		List<String> campos = Arrays.asList(line.split(";"));
		
		filial = campos.get(0).trim();
		fatura = campos.get(1).trim();
		dtInicial = campos.get(2).trim();
		dtFinal = campos.get(3).trim();
		try {
			observacao = campos.get(4).trim();
		}catch(ArrayIndexOutOfBoundsException e){}
		
	}

	public String getFilial() {
		return filial;
	}
	public void setFilial(String filial) {
		this.filial = filial;
	}
	public String getFatura() {
		return fatura;
	}
	public void setFatura(String fatura) {
		this.fatura = fatura;
	}
	public String getDtInicial() {
		return dtInicial;
	}
	public void setDtInicial(String dtInicial) {
		this.dtInicial = dtInicial;
	}
	public String getDtFinal() {
		return dtFinal;
	}
	public void setDtFinal(String dtFinal) {
		this.dtFinal = dtFinal;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	@Override
	public String toString() {
		return "ProcessaArquivoExcecaoNegativacaoDTO [filial=" + filial + ", fatura=" + fatura
				+ ", dtInicial=" + dtInicial + ", dtFinal=" + dtFinal + ", observacao=" + observacao + "]";
	}
	
}
