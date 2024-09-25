package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

public class InformacaoDocServicoLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idInformacaoDocServicoLog;
	private InformacaoDocServico informacaoDocServico;
	private TipoRegistroComplemento tipoRegistroComplemento;
	private Long nrTamanho;
	private String dsCampo;
	private boolean blImprimeConhecimento;
	private DomainValue tpCampo;
	private DomainValue tpSituacao;
	private Long nrDecimais;
	private boolean blOpcional;
	private String dsFormatacao;
	private boolean blIndicadorNotaFiscal;
	private Long nrVersao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdInformacaoDocServicoLog() {
   
		return idInformacaoDocServicoLog;
	}
   
	public void setIdInformacaoDocServicoLog(long idInformacaoDocServicoLog) {
   
		this.idInformacaoDocServicoLog = idInformacaoDocServicoLog;
	}
	
	public InformacaoDocServico getInformacaoDocServico() {
   
		return informacaoDocServico;
	}
   
	public void setInformacaoDocServico(
			InformacaoDocServico informacaoDocServico) {
   
		this.informacaoDocServico = informacaoDocServico;
	}
	
	public TipoRegistroComplemento getTipoRegistroComplemento() {
   
		return tipoRegistroComplemento;
	}
   
	public void setTipoRegistroComplemento(
			TipoRegistroComplemento tipoRegistroComplemento) {
   
		this.tipoRegistroComplemento = tipoRegistroComplemento;
	}
	
	public Long getNrTamanho() {
   
		return nrTamanho;
	}
   
	public void setNrTamanho(Long nrTamanho) {
   
		this.nrTamanho = nrTamanho;
	}
	
	public String getDsCampo() {
   
		return dsCampo;
	}
   
	public void setDsCampo(String dsCampo) {
   
		this.dsCampo = dsCampo;
	}
	
	public boolean isBlImprimeConhecimento() {
   
		return blImprimeConhecimento;
	}
   
	public void setBlImprimeConhecimento(boolean blImprimeConhecimento) {
   
		this.blImprimeConhecimento = blImprimeConhecimento;
	}
	
	public DomainValue getTpCampo() {
   
		return tpCampo;
	}
   
	public void setTpCampo(DomainValue tpCampo) {
   
		this.tpCampo = tpCampo;
	}
	
	public DomainValue getTpSituacao() {
   
		return tpSituacao;
	}
   
	public void setTpSituacao(DomainValue tpSituacao) {
   
		this.tpSituacao = tpSituacao;
	}
	
	public Long getNrDecimais() {
   
		return nrDecimais;
	}
   
	public void setNrDecimais(Long nrDecimais) {
   
		this.nrDecimais = nrDecimais;
	}
	
	public boolean isBlOpcional() {
   
		return blOpcional;
	}
   
	public void setBlOpcional(boolean blOpcional) {
   
		this.blOpcional = blOpcional;
	}
	
	public String getDsFormatacao() {
   
		return dsFormatacao;
	}
   
	public void setDsFormatacao(String dsFormatacao) {
   
		this.dsFormatacao = dsFormatacao;
	}
	
	public boolean isBlIndicadorNotaFiscal() {
   
		return blIndicadorNotaFiscal;
	}
   
	public void setBlIndicadorNotaFiscal(boolean blIndicadorNotaFiscal) {
   
		this.blIndicadorNotaFiscal = blIndicadorNotaFiscal;
	}
	
	public Long getNrVersao() {
   
		return nrVersao;
	}
   
	public void setNrVersao(Long nrVersao) {
   
		this.nrVersao = nrVersao;
	}
	
	public DomainValue getTpOrigemLog() {
   
		return tpOrigemLog;
	}
   
	public void setTpOrigemLog(DomainValue tpOrigemLog) {
   
		this.tpOrigemLog = tpOrigemLog;
	}
	
	public String getLoginLog() {
   
		return loginLog;
	}
   
	public void setLoginLog(String loginLog) {
   
		this.loginLog = loginLog;
	}
	
	public DateTime getDhLog() {
   
		return dhLog;
	}
   
	public void setDhLog(DateTime dhLog) {
   
		this.dhLog = dhLog;
	}
	
	public DomainValue getOpLog() {
   
		return opLog;
	}
   
	public void setOpLog(DomainValue opLog) {
   
		this.opLog = opLog;
	}
	
   	public String toString() {
		return new ToStringBuilder(this).append("idInformacaoDocServicoLog",
				getIdInformacaoDocServicoLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof InformacaoDocServicoLog))
			return false;
		InformacaoDocServicoLog castOther = (InformacaoDocServicoLog) other;
		return new EqualsBuilder().append(this.getIdInformacaoDocServicoLog(),
				castOther.getIdInformacaoDocServicoLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdInformacaoDocServicoLog())
			.toHashCode();
	}
} 