package com.mercurio.lms.expedicao.model;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaFiscalServico extends DoctoServico {

	private static final long serialVersionUID = 1L;

	/** persistent field */
	private Long nrNotaFiscalServico;

	/** persistent field */
	private DomainValue tpSituacaoNf;

	/** nullable persistent field */
	private YearMonthDay dtInicial;

	/** nullable persistent field */
	private YearMonthDay dtFinal;
	
	/** persistent field */
	private DateTime dhCancelamento;

	/** nullable persistent field */
	private String dsSerie;
	
	/** persistent field */
	private Integer nrFormulario;

	private DomainValue tpNotaFiscalServico; 

	/** persistent field */
	private com.mercurio.lms.municipios.model.Municipio municipio;

	/** persistent field */
	private com.mercurio.lms.municipios.model.Filial filial;

	/** persistent field */
	private List impostoServicos;

	public Long getNrNotaFiscalServico() {
		return this.nrNotaFiscalServico;
	}

	public void setNrNotaFiscalServico(Long nrNotaFiscalServico) {
		this.nrNotaFiscalServico = nrNotaFiscalServico;
	}

	public DomainValue getTpSituacaoNf() {
		return this.tpSituacaoNf;
	}

	public void setTpSituacaoNf(DomainValue tpSituacaoNf) {
		this.tpSituacaoNf = tpSituacaoNf;
	}

	public YearMonthDay getDtInicial() {
		return this.dtInicial;
	}

	public void setDtInicial(YearMonthDay dtInicial) {
		this.dtInicial = dtInicial;
	}

	public YearMonthDay getDtFinal() {
		return this.dtFinal;
	}

	public void setDtFinal(YearMonthDay dtFinal) {
		this.dtFinal = dtFinal;
	}

	public String getDsSerie() {
		return this.dsSerie;
	}

	public void setDsSerie(String dsSerie) {
		this.dsSerie = dsSerie;
	}

	public Integer getNrFormulario() {
		return nrFormulario;
	}

	public void setNrFormulario(Integer nrFormulario) {
		this.nrFormulario = nrFormulario;
	}

	public com.mercurio.lms.municipios.model.Municipio getMunicipio() {
		return this.municipio;
	}

	public void setMunicipio(
			com.mercurio.lms.municipios.model.Municipio municipio) {
		this.municipio = municipio;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return this.filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ImpostoServico.class) 
	public List getImpostoServicos() {
		return this.impostoServicos;
	}

	public void setImpostoServicos(List impostoServicos) {
		this.impostoServicos = impostoServicos;
	}

	public DomainValue getTpNotaFiscalServico() {
		return tpNotaFiscalServico;
}

	public void setTpNotaFiscalServico(DomainValue tpNotaFiscalServico) {
		this.tpNotaFiscalServico = tpNotaFiscalServico;
	}

	public DateTime getDhCancelamento() {
		return dhCancelamento;
	}

	public void setDhCancelamento(DateTime dhCancelamento) {
		this.dhCancelamento = dhCancelamento;
	}

}
