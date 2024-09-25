package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ObservacaoICMSPessoa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idObservacaoICMSPessoa;

    /** persistent field */
    private Long nrOrdemImpressao;
    
    private DomainValue tpObservacaoICMSPessoa;
    
    private String obObservacaoICMSPessoa;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private String cdEmbLegalMastersaf;
    
    private InscricaoEstadual inscricaoEstadual;

	public String getCdEmbLegalMastersaf() {
		return cdEmbLegalMastersaf;
	}

	public void setCdEmbLegalMastersaf(String cdEmbLegalMastersaf) {
		this.cdEmbLegalMastersaf = cdEmbLegalMastersaf;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public Long getIdObservacaoICMSPessoa() {
		return idObservacaoICMSPessoa;
	}

	public void setIdObservacaoICMSPessoa(Long idObservacaoICMSPessoa) {
		this.idObservacaoICMSPessoa = idObservacaoICMSPessoa;
	}

	public InscricaoEstadual getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(InscricaoEstadual inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public Long getNrOrdemImpressao() {
		return nrOrdemImpressao;
	}

	public void setNrOrdemImpressao(Long nrOrdemImpressao) {
		this.nrOrdemImpressao = nrOrdemImpressao;
	}

	public String getObObservacaoICMSPessoa() {
		return obObservacaoICMSPessoa;
	}

	public void setObObservacaoICMSPessoa(String obObservacaoICMSPessoa) {
		this.obObservacaoICMSPessoa = obObservacaoICMSPessoa;
	}

	public DomainValue getTpObservacaoICMSPessoa() {
		return tpObservacaoICMSPessoa;
	}

	public void setTpObservacaoICMSPessoa(DomainValue tpObservacaoICMSPessoa) {
		this.tpObservacaoICMSPessoa = tpObservacaoICMSPessoa;
	}
}
