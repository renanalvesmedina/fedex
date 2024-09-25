package com.mercurio.lms.rest.sgr.dto;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.rest.BaseDTO;

public class FornecedorEscoltaDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private String nrIdentificacao;
	private String nmPessoa;
	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;
	private String dsEmailFornecedor;
	private String dsTelefone1;
	private String dsTelefone2;
	private String dsTelefone3;

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public String getNmPessoa() {
		return nmPessoa;
	}

	public void setNmPessoa(String nmPessoa) {
		this.nmPessoa = nmPessoa;
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

	public String getDsEmailFornecedor() {
		return dsEmailFornecedor;
	}

	public void setDsEmailFornecedor(String dsEmailFornecedor) {
		this.dsEmailFornecedor = dsEmailFornecedor;
	}

	public String getDsTelefone1() {
		return dsTelefone1;
	}

	public void setDsTelefone1(String dsTelefone1) {
		this.dsTelefone1 = dsTelefone1;
	}

	public String getDsTelefone2() {
		return dsTelefone2;
	}

	public void setDsTelefone2(String dsTelefone2) {
		this.dsTelefone2 = dsTelefone2;
	}

	public String getDsTelefone3() {
		return dsTelefone3;
	}

	public void setDsTelefone3(String dsTelefone3) {
		this.dsTelefone3 = dsTelefone3;
	}

}
