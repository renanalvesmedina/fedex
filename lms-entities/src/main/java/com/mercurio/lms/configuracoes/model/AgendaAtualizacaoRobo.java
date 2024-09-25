package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class AgendaAtualizacaoRobo implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAgendaAtualizacaoRobo;

    private Long nrVersao;
    
    private DateTime dhAtualizacao;
    
    private String dsObservacoes;
    
    /** persistent field */
    private byte[] arquivoZip;

    private Long nrVersaoSOM;

	public Long getIdAgendaAtualizacaoRobo() {
		return idAgendaAtualizacaoRobo;
	}

	public void setIdAgendaAtualizacaoRobo(Long idAgendaAtualizacaoRobo) {
		this.idAgendaAtualizacaoRobo = idAgendaAtualizacaoRobo;
	}

	public Long getNrVersao() {
		return nrVersao;
	}

	public void setNrVersao(Long nrVersao) {
		this.nrVersao = nrVersao;
	}

	public DateTime getDhAtualizacao() {
		return dhAtualizacao;
	}

	public void setDhAtualizacao(DateTime dhAtualizacao) {
		this.dhAtualizacao = dhAtualizacao;
	}

	public String getDsObservacoes() {
		return dsObservacoes;
	}

	public void setDsObservacoes(String dsObservacoes) {
		this.dsObservacoes = dsObservacoes;
	}

	public byte[] getArquivoZip() {
		return arquivoZip;
	}

	public void setArquivoZip(byte[] arquivoZip) {
		this.arquivoZip = arquivoZip;
	}

	public Long getNrVersaoSOM() {
		return nrVersaoSOM;
}

	public void setNrVersaoSOM(Long nrVersaoSOM) {
		this.nrVersaoSOM = nrVersaoSOM;
	}

}
