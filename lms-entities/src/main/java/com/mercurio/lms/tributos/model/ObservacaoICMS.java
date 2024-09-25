package com.mercurio.lms.tributos.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ObservacaoICMS implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idObservacaoICMS;
    
    /** persistent field */
    private Integer versao;

    /** persistent field */
    private Long nrOrdemImpressao;
    
    /** persistent field */
    private DomainValue tpObservacaoICMS;
    
    /** persistent field */
    private String obObservacaoICMS;

    /** persistent field */
    private String cdEmbLegalMastersaf;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    private DescricaoTributacaoIcms descricaoTributacaoIcms;

	public DescricaoTributacaoIcms getDescricaoTributacaoIcms() {
		return descricaoTributacaoIcms;
	}

	public void setDescricaoTributacaoIcms(
			DescricaoTributacaoIcms descricaoTributacaoIcms) {
		this.descricaoTributacaoIcms = descricaoTributacaoIcms;
	}

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

	public Long getIdObservacaoICMS() {
		return idObservacaoICMS;
	}

	public void setIdObservacaoICMS(Long idObservacaoICMS) {
		this.idObservacaoICMS = idObservacaoICMS;
	}

	public Long getNrOrdemImpressao() {
		return nrOrdemImpressao;
	}

	public void setNrOrdemImpressao(Long nrOrdemImpressao) {
		this.nrOrdemImpressao = nrOrdemImpressao;
	}

	public String getObObservacaoICMS() {
		return obObservacaoICMS;
	}

	public void setObObservacaoICMS(String obObservacaoICMS) {
		this.obObservacaoICMS = obObservacaoICMS;
	}

	public DomainValue getTpObservacaoICMS() {
		return tpObservacaoICMS;
	}

	public void setTpObservacaoICMS(DomainValue tpObservacaoICMS) {
		this.tpObservacaoICMS = tpObservacaoICMS;
	}

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}
	
    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ObservacaoICMS))
			return false;
        ObservacaoICMS castOther = (ObservacaoICMS) other;
		return new EqualsBuilder().append(this.getIdObservacaoICMS(),
				castOther.getIdObservacaoICMS()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdObservacaoICMS()).toHashCode();
    }	
}
