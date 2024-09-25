package com.mercurio.lms.rest.vendas.dto;

import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseDTO;

public class HistoricoEfetivacaoDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

	private Long idHistoricoEfetivacao;
	private DateTime dhSolicitacao;
	private String nmUsuarioSolicitacao;
	private DateTime dhReprovacao;
	private String nmUsuarioReprovador;
	private String dsMotivo;
	private String dsMotivoReprovacao;

	public HistoricoEfetivacaoDTO(Long idHistoricoEfetivacao, DateTime dhSolicitacao, String nmUsuarioSolicitacao, DateTime dhReprovacao,
			String nmUsuarioReprovador, String dsMotivo, String dsMotivoReprovacao) {
		super();
		this.idHistoricoEfetivacao = idHistoricoEfetivacao;
		this.dhSolicitacao = dhSolicitacao;
		this.nmUsuarioSolicitacao = nmUsuarioSolicitacao;
		this.dhReprovacao = dhReprovacao;
		this.nmUsuarioReprovador = nmUsuarioReprovador;
		this.dsMotivo = dsMotivo;
		this.dsMotivoReprovacao = dsMotivoReprovacao;
	}

	public Long getIdHistoricoEfetivacao() {
		return idHistoricoEfetivacao;
	}

	public void setIdHistoricoEfetivacao(Long idHistoricoEfetivacao) {
		this.idHistoricoEfetivacao = idHistoricoEfetivacao;
	}

	public DateTime getDhSolicitacao() {
		return dhSolicitacao;
	}

	public void setDhSolicitacao(DateTime dhSolicitacao) {
		this.dhSolicitacao = dhSolicitacao;
	}

	public String getNmUsuarioSolicitacao() {
		return nmUsuarioSolicitacao;
	}

	public void setNmUsuarioSolicitacao(String nmUsuarioSolicitacao) {
		this.nmUsuarioSolicitacao = nmUsuarioSolicitacao;
	}

	public DateTime getDhReprovacao() {
		return dhReprovacao;
	}

	public void setDhReprovacao(DateTime dhReprovacao) {
		this.dhReprovacao = dhReprovacao;
	}

	public String getNmUsuarioReprovador() {
		return nmUsuarioReprovador;
	}

	public void setNmUsuarioReprovador(String nmUsuarioReprovador) {
		this.nmUsuarioReprovador = nmUsuarioReprovador;
	}

	public String getDsMotivo() {
		return dsMotivo;
	}

	public void setDsMotivo(String dsMotivo) {
		this.dsMotivo = dsMotivo;
	}

	public String getDsMotivoReprovacao() {
		return dsMotivoReprovacao;
	}

	public void setDsMotivoReprovacao(String dsMotivoReprovacao) {
		this.dsMotivoReprovacao = dsMotivoReprovacao;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
