package com.mercurio.lms.rest.vendas.dto;

import com.mercurio.adsm.rest.BaseFilterDTO;

public class ParametrosClienteFiltroDTO extends BaseFilterDTO {
	 private static final long serialVersionUID = 1L;
	    
	    private String tipoPessoa;
	    private String tipoIdentificacao;
	    private String identificacao;
	    private String nomeRazaoSocial;
	    private String nomeFantasia;
	    private String numeroConta;
	    private String tipoCliente;
	    private String situacao;
	    
		public String getTipoPessoa() {
			return tipoPessoa;
		}
		public void setTipoPessoa(String tipoPessoa) {
			this.tipoPessoa = tipoPessoa;
		}
		public String getTipoIdentificacao() {
			return tipoIdentificacao;
		}
		public void setTipoIdentificacao(String tipoIdentificacao) {
			this.tipoIdentificacao = tipoIdentificacao;
		}
		public String getIdentificacao() {
			return identificacao;
		}
		public void setIdentificacao(String identificacao) {
			this.identificacao = identificacao;
		}
		public String getNomeRazaoSocial() {
			return nomeRazaoSocial;
		}
		public void setNomeRazaoSocial(String nomeRazaoSocial) {
			this.nomeRazaoSocial = nomeRazaoSocial;
		}
		public String getNomeFantasia() {
			return nomeFantasia;
		}
		public void setNomeFantasia(String nomeFantasia) {
			this.nomeFantasia = nomeFantasia;
		}
		public String getNumeroConta() {
			return numeroConta;
		}
		public void setNumeroConta(String numeroConta) {
			this.numeroConta = numeroConta;
		}
		public String getTipoCliente() {
			return tipoCliente;
		}
		public void setTipoCliente(String tipoCliente) {
			this.tipoCliente = tipoCliente;
		}
		public String getSituacao() {
			return situacao;
		}
		public void setSituacao(String situacao) {
			this.situacao = situacao;
		}    
	    
}
