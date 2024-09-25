package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.municipios.model.Filial;

public class ArquivoGeracao implements Serializable {
							
	public ArquivoGeracao(){};
	
	public ArquivoGeracao(Long idArquivoGeracao, String nmPI, DomainValue tpDocumento, Filial filial, String nrDocumento, byte[] arquivo, YearMonthDay dtGeracao, String dtGeracaoFormatada, AcaoIntegracao acaoIntegracao){
		this.idArquivoGeracao = idArquivoGeracao;
		this.nmPI = nmPI;
		this.tpDocumento = tpDocumento;
		this.filial = filial;
		this.nrDocumento = nrDocumento;
		this.arquivo = arquivo;
		this.dtGeracao = dtGeracao;
		this.dtGeracaoFormatada = dtGeracaoFormatada;
		this.acaoIntegracao = acaoIntegracao;
	}
	
	private static final long serialVersionUID = 1L;
	private Long idArquivoGeracao;
	private String nmPI;
	private DomainValue tpDocumento;
	private Filial filial;
	private String nrDocumento;
	private byte[] arquivo;
	private YearMonthDay dtGeracao;
	private String dtGeracaoFormatada;
	private AcaoIntegracao acaoIntegracao;
	
	public Long getIdArquivoGeracao() {
		return idArquivoGeracao;
	}

	public void setIdArquivoGeracao(Long idArquivoGeracao) {
		this.idArquivoGeracao = idArquivoGeracao;
	}

	public String getNmPI() {
		return nmPI;
	}

	public void setNmPI(String nmPI) {
		this.nmPI = nmPI;
	}

	public DomainValue getTpDocumento() {
		return tpDocumento;
	}

	public void setTpDocumento(DomainValue tpDocumento) {
		this.tpDocumento = tpDocumento;
	}

	public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public byte[] getArquivo() {
		return arquivo;
	}

	public void setArquivo(byte[] arquivo) {
		this.arquivo = arquivo;
	}

	public YearMonthDay getDtGeracao() {
		return dtGeracao;
	}

	public void setDtGeracao(YearMonthDay dtGeracao) {
		this.dtGeracao = dtGeracao;
	}

	public AcaoIntegracao getAcaoIntegracao() {
		return acaoIntegracao;
	}

	public void setAcaoIntegracao(AcaoIntegracao acaoIntegracao) {
		this.acaoIntegracao = acaoIntegracao;
	}
	
	public String toString() {
		return new ToStringBuilder(this).append("idArquivoGeracao",
				getIdArquivoGeracao()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ArquivoGeracao))
			return false;
		ArquivoGeracao castOther = (ArquivoGeracao) other;
		return new EqualsBuilder().append(this.getIdArquivoGeracao(),
				castOther.getIdArquivoGeracao()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdArquivoGeracao()).toHashCode();
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getNrDocumento() {
		return nrDocumento;
	}

	public String getDtGeracaoFormatada() {
		return dtGeracaoFormatada;
	}

	public void setDtGeracaoFormatada(String dtGeracaoFormatada) {
		this.dtGeracaoFormatada = dtGeracaoFormatada;
	}

}
