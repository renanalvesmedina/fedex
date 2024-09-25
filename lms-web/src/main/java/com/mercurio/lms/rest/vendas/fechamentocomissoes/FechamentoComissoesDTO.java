package com.mercurio.lms.rest.vendas.fechamentocomissoes;

import com.mercurio.adsm.rest.BaseDTO; 
 
public class FechamentoComissoesDTO extends BaseDTO { 
	private static final long serialVersionUID = 1L; 
 
	private Long idFechamentoComissao;
	private Boolean desabilitaBotaoFechamento;
	private Boolean desabilitaBotaoAprovacao;
	private Boolean desabilitaEnvioRh;
	private Long idPendeciaAprovacao;
	
	public FechamentoComissoesDTO() {
	}

	public FechamentoComissoesDTO(Long idFechamentoComissao, Boolean desabilitaBotaoFechamento, Boolean desabilitaBotaoAprovacao, Boolean desabilitaEnvioRh) {
		this.desabilitaBotaoFechamento = desabilitaBotaoFechamento;
		this.desabilitaBotaoAprovacao = desabilitaBotaoAprovacao;
		this.desabilitaEnvioRh = desabilitaEnvioRh;
		this.idFechamentoComissao = idFechamentoComissao;
	}

	public FechamentoComissoesDTO(Long idFechamentoComissao, Boolean desabilitaBotaoFechamento, 
			Boolean desabilitaBotaoAprovacao, Boolean desabilitaEnvioRh, Long idPendencia) {

		this.desabilitaBotaoFechamento = desabilitaBotaoFechamento;
		this.desabilitaBotaoAprovacao = desabilitaBotaoAprovacao;
		this.desabilitaEnvioRh = desabilitaEnvioRh;
		this.idFechamentoComissao = idFechamentoComissao;
		this.idPendeciaAprovacao = idPendencia;
	}

	public Boolean getDesabilitaBotaoFechamento() {
		return desabilitaBotaoFechamento;
	}

	public void setDesabilitaBotaoFechamento(Boolean desabilitaBotaoFechamento) {
		this.desabilitaBotaoFechamento = desabilitaBotaoFechamento;
	}

	public Boolean getDesabilitaBotaoAprovacao() {
		return desabilitaBotaoAprovacao;
	}

	public void setDesabilitaBotaoAprovacao(Boolean desabilitaBotaoAprovacao) {
		this.desabilitaBotaoAprovacao = desabilitaBotaoAprovacao;
	}

	public Boolean getDesabilitaEnvioRh() {
		return desabilitaEnvioRh;
	}

	public void setDesabilitaEnvioRh(Boolean desabilitaEnvioRh) {
		this.desabilitaEnvioRh = desabilitaEnvioRh;
	}
	
	public Long getIdFechamentoComissao() {
		return idFechamentoComissao;
	}
	public void setIdFechamentoComissao(Long idFechamentoComissao) {
		this.idFechamentoComissao = idFechamentoComissao;
	}

	public Long getIdPendeciaAprovacao() {
		return idPendeciaAprovacao;
	}
	
	public void setIdPendeciaAprovacao(Long idPendeciaAprovacao) {
		this.idPendeciaAprovacao = idPendeciaAprovacao;
	}
	
}