package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

public class PessoaLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private long idPessoaLog;
	private Pessoa pessoa;
	private String nmPessoa;
	private YearMonthDay dtEmissaoRg;
	private String dsEmail;
	private String nrRg;
	private String dsOrgaoEmissorRg;
	private String nmFantasia;
	private String nrInscricaoMunicipal;
	private DomainValue tpIdentificacao;
	private String nrIdentificacao;
	private DomainValue tpPessoa;
	private EnderecoPessoa enderecoPessoa;
	private Boolean blAtualizacaoCountasse;
	private DateTime dhInclusao;
	private DomainValue tpOrigemLog;
	private String loginLog;
	private DateTime dhLog;
	private DomainValue opLog;
   
	public long getIdPessoaLog() {
   
		return idPessoaLog;
	}
   
	public void setIdPessoaLog(long idPessoaLog) {
   
		this.idPessoaLog = idPessoaLog;
	}
	
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public String getNmPessoa() {
   
		return nmPessoa;
	}
   
	public void setNmPessoa(String nmPessoa) {
   
		this.nmPessoa = nmPessoa;
	}
	
	public YearMonthDay getDtEmissaoRg() {
   
		return dtEmissaoRg;
	}
   
	public void setDtEmissaoRg(YearMonthDay dtEmissaoRg) {
   
		this.dtEmissaoRg = dtEmissaoRg;
	}
	
	public String getDsEmail() {
   
		return dsEmail;
	}
   
	public void setDsEmail(String dsEmail) {
   
		this.dsEmail = dsEmail;
	}
	
	public String getNrRg() {
   
		return nrRg;
	}
   
	public void setNrRg(String nrRg) {
   
		this.nrRg = nrRg;
	}
	
	public String getDsOrgaoEmissorRg() {
   
		return dsOrgaoEmissorRg;
	}
   
	public void setDsOrgaoEmissorRg(String dsOrgaoEmissorRg) {
   
		this.dsOrgaoEmissorRg = dsOrgaoEmissorRg;
	}
	
	public String getNmFantasia() {
   
		return nmFantasia;
	}
   
	public void setNmFantasia(String nmFantasia) {
   
		this.nmFantasia = nmFantasia;
	}
	
	public String getNrInscricaoMunicipal() {
   
		return nrInscricaoMunicipal;
	}
   
	public void setNrInscricaoMunicipal(String nrInscricaoMunicipal) {
   
		this.nrInscricaoMunicipal = nrInscricaoMunicipal;
	}
	
	public DomainValue getTpIdentificacao() {
		return tpIdentificacao;
	}

	public void setTpIdentificacao(DomainValue tpIdentificacao) {
		this.tpIdentificacao = tpIdentificacao;
	}

	public String getNrIdentificacao() {
		return nrIdentificacao;
	}

	public void setNrIdentificacao(String nrIdentificacao) {
		this.nrIdentificacao = nrIdentificacao;
	}

	public DomainValue getTpPessoa() {
		return tpPessoa;
	}

	public void setTpPessoa(DomainValue tpPessoa) {
		this.tpPessoa = tpPessoa;
	}

	public EnderecoPessoa getEnderecoPessoa() {
		return enderecoPessoa;
	}

	public void setEnderecoPessoa(EnderecoPessoa enderecoPessoa) {
		this.enderecoPessoa = enderecoPessoa;
	}

	public Boolean getBlAtualizacaoCountasse() {
		return blAtualizacaoCountasse;
	}

	public void setBlAtualizacaoCountasse(Boolean blAtualizacaoCountasse) {
		this.blAtualizacaoCountasse = blAtualizacaoCountasse;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
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
		return new ToStringBuilder(this)
				.append("idPessoaLog", getIdPessoaLog()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PessoaLog))
			return false;
		PessoaLog castOther = (PessoaLog) other;
		return new EqualsBuilder().append(this.getIdPessoaLog(),
				castOther.getIdPessoaLog()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdPessoaLog()).toHashCode();
	}
} 