package com.mercurio.lms.rest.contratacaoveiculos;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;

public class ProprietarioDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idProprietario;

	private String nrIdentificacao;

	private DomainValue tpProprietario;
	private DomainValue tpIdentificacao;
	private String tpIdentificacaoFormatado;

	private String nmPessoa;

	private DomainValue tpSituacao;

	private YearMonthDay dtVigenciaInicial;
	private YearMonthDay dtVigenciaFinal;

	public Long getIdProprietario() {
		return idProprietario;
	}

	public void setIdProprietario(Long idProprietario) {
		this.idProprietario = idProprietario;
	}

	public DomainValue getTpProprietario() {
		return tpProprietario;
	}

	public void setTpProprietario(DomainValue tpProprietario) {
		this.tpProprietario = tpProprietario;
	}

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

	public DomainValue getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(DomainValue tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

	public String getTpIdentificacaoFormatado() {
		return tpIdentificacaoFormatado;
	}

	public void setTpIdentificacaoFormatado(String tpIdentificacaoFormatado) {
		this.tpIdentificacaoFormatado = tpIdentificacaoFormatado;
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

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
}
