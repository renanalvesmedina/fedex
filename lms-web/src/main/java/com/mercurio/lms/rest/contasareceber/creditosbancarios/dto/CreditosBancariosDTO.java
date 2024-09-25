package com.mercurio.lms.rest.contasareceber.creditosbancarios.dto;

import java.math.BigDecimal;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.rest.BaseDTO;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.configuracoes.dto.BancoSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;

public class CreditosBancariosDTO extends BaseDTO {
	private static final long serialVersionUID = 1L;
	
	private FilialSuggestDTO filial;
	private BancoSuggestDTO banco;
	private UsuarioDTO usuario;
	private YearMonthDay dtCredito;
	private BigDecimal vlCredito;
	private BigDecimal saldo;
	private DomainValue tpModalidade;
	private DomainValue tpOrigem;
	private DomainValue tpClassificacao;
	private DomainValue tpSituacao;
	private String dsBoleto;
	private String obCreditoBancario;
	private String dtAlteracao;
	private String dsCpfCnpj;
	private String dsNomeRazaoSocial;
	
	public CreditosBancariosDTO() {
		
	}
	
	public CreditosBancariosDTO(Long id, FilialSuggestDTO filial, BancoSuggestDTO banco,
			UsuarioDTO usuario, YearMonthDay dtCredito, BigDecimal vlCredito,
			BigDecimal saldo, DomainValue tpModalidade, DomainValue tpOrigem,
			DomainValue tpClassificacao, DomainValue tpSituacao, String dsBoleto, 
			String obCreditoBancario, String dtAlteracao, String dsCpfCnpj, 
			String dsNomeRazaoSocial) {
		
		super();
		setId(id);
		this.filial = filial;
		this.banco = banco;
		this.usuario = usuario;
		this.dtCredito = dtCredito;
		this.vlCredito = vlCredito;
		this.saldo = saldo;
		this.tpModalidade = tpModalidade;
		this.tpOrigem = tpOrigem;
		this.tpClassificacao = tpClassificacao;
		this.tpSituacao = tpSituacao;
		this.dsBoleto = dsBoleto;
		this.obCreditoBancario = obCreditoBancario;
		this.dtAlteracao = dtAlteracao;
		this.dsCpfCnpj = dsCpfCnpj;
		this.dsNomeRazaoSocial = dsNomeRazaoSocial;
	}

	public FilialSuggestDTO getFilial() {
		return filial;
	}
	public void setFilial(FilialSuggestDTO filial) {
		this.filial = filial;
	}
	public BancoSuggestDTO getBanco() {
		return banco;
	}
	public void setBanco(BancoSuggestDTO banco) {
		this.banco = banco;
	}
	public UsuarioDTO getUsuario() {
		return usuario;
	}
	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}
	public YearMonthDay getDtCredito() {
		return dtCredito;
	}
	public void setDtCredito(YearMonthDay dtCredito) {
		this.dtCredito = dtCredito;
	}
	public BigDecimal getVlCredito() {
		return vlCredito;
	}
	public void setVlCredito(BigDecimal vlCredito) {
		this.vlCredito = vlCredito;
	}
	public DomainValue getTpModalidade() {
		return tpModalidade;
	}
	public void setTpModalidade(DomainValue tpModalidade) {
		this.tpModalidade = tpModalidade;
	}
	public DomainValue getTpOrigem() {
		return tpOrigem;
	}
	public void setTpOrigem(DomainValue tpOrigem) {
		this.tpOrigem = tpOrigem;
	}
	public DomainValue getTpSituacao() {
		return tpSituacao;
	}
	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}
	public String getDsCpfCnpj() {
		return dsCpfCnpj;
	}
	public void setDsCpfCnpj(String dsCpfCnpj) {
		this.dsCpfCnpj = dsCpfCnpj;
	}
	public String getDsNomeRazaoSocial() {
		return dsNomeRazaoSocial;
	}
	public void setDsNomeRazaoSocial(String dsNomeRazaoSocial) {
		this.dsNomeRazaoSocial = dsNomeRazaoSocial;
	}
	public String getDsBoleto() {
		return dsBoleto;
	}
	public void setDsBoleto(String dsBoleto) {
		this.dsBoleto = dsBoleto;
	}
	public String getObCreditoBancario() {
		return obCreditoBancario;
	}
	public void setObCreditoBancario(String obCreditoBancario) {
		this.obCreditoBancario = obCreditoBancario;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	public BigDecimal getSaldo() {
		return saldo;
	}
	public String getDtAlteracao() {
		return dtAlteracao;
	}
	public void setDtAlteracao(String dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	public DomainValue getTpClassificacao() {
		return tpClassificacao;
	}
	public void setTpClassificacao(DomainValue tpClassificacao) {
		this.tpClassificacao = tpClassificacao;
	}
}
