package com.mercurio.lms.workflow.model.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

public class PendenciaHistoricoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum CampoHistoricoWorkflow {
		STDV, PBCL, TBPR, OBDM, TPCL, FTDN, FTCB, FCOM, FOPE, FCOB, LEMB, TRTC, TDES, TEFE, PGR1, PGR2, PGR3, TBPA, TEFA, TDEA, PCAE, ATCF, AICL, ATCL;
	}

	public static enum TabelaHistoricoWorkflow {
		DIVISAO_CLIENTE, TABELA_DIVISAO_CLIENTE, TABELA_PRECO, CLIENTE, LIBERACAO_EMBARQUE, TRT_CLIENTE, CONTROLE_CARGA, SIMULACAO, CLIENTE_TERRITORIO, FECHAMENTO_COMISSAO;
	}

	private Long idFilial;
	private Short nrTipoEvento;
	private Long idProcesso;
	private String dsProcesso;
	private DateTime dhLiberacao;
	private TabelaHistoricoWorkflow tabelaHistoricoWorkflow;
	private CampoHistoricoWorkflow campoHistoricoWorkflow;
	private String dsVlAntigo;
	private String dsVlNovo;
	private String dsObservacao;

	public Long getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Long idFilial) {
		this.idFilial = idFilial;
	}

	public Short getNrTipoEvento() {
		return nrTipoEvento;
	}

	public void setNrTipoEvento(Short nrTipoEvento) {
		this.nrTipoEvento = nrTipoEvento;
	}

	public Long getIdProcesso() {
		return idProcesso;
	}

	public void setIdProcesso(Long idProcesso) {
		this.idProcesso = idProcesso;
	}

	public String getDsProcesso() {
		return dsProcesso;
	}

	public void setDsProcesso(String dsProcesso) {
		this.dsProcesso = dsProcesso;
	}

	public DateTime getDhLiberacao() {
		return dhLiberacao;
	}

	public void setDhLiberacao(DateTime dhLiberacao) {
		this.dhLiberacao = dhLiberacao;
	}

	public TabelaHistoricoWorkflow getTabelaHistoricoWorkflow() {
		return tabelaHistoricoWorkflow;
	}

	public void setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow tabelaHistoricoWorkflow) {
		this.tabelaHistoricoWorkflow = tabelaHistoricoWorkflow;
	}

	public CampoHistoricoWorkflow getCampoHistoricoWorkflow() {
		return campoHistoricoWorkflow;
	}

	public void setCampoHistoricoWorkflow(CampoHistoricoWorkflow campoHistoricoWorkflow) {
		this.campoHistoricoWorkflow = campoHistoricoWorkflow;
	}

	public String getDsVlAntigo() {
		return dsVlAntigo;
	}

	public void setDsVlAntigo(String dsVlAntigo) {
		this.dsVlAntigo = dsVlAntigo;
	}

	public String getDsVlNovo() {
		return dsVlNovo;
	}

	public void setDsVlNovo(String dsVlNovo) {
		this.dsVlNovo = dsVlNovo;
	}

	public String getDsObservacao() {
		return dsObservacao;
	}

	public void setDsObservacao(String dsObservacao) {
		this.dsObservacao = dsObservacao;
	}
}