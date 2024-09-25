package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class SinistroDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSinistroDoctoServico;

    /** persistent field */
    private DomainValue tpPrejuizo;

    /** nullable persistent field */
    private BigDecimal vlPrejuizo;

    /** nullable persistent field */
    private DateTime dhGeracaoCartaOcorrencia;

    /** nullable persistent field */
    private DateTime dhEnvioEmailOcorrencia;

    /** nullable persistent field */
    private DateTime dhGeracaoCartaRetificacao;

    /** nullable persistent field */
    private DateTime dhEnvioEmailRetificacao;

    /** nullable persistent field */
    private DateTime dhGeracaoFilialRim;

    /** nullable persistent field */
    private DateTime dhEnvioEmailFilialRim;

    /** nullable persistent field */
    private DomainValue tpFilialNotifEmissaoRim;

	/** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    //LMS-6178
    /** persistent field */
    private com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro;
    
    //LMS-6611
    /** persistent field **/
    private DomainValue blPrejuizoProprio;
    
    public DomainValue getBlPrejuizoProprio() {
		return blPrejuizoProprio;
	}

	public void setBlPrejuizoProprio(DomainValue blPrejuizoProprio) {
		this.blPrejuizoProprio = blPrejuizoProprio;
	}
        
    public Long getIdSinistroDoctoServico() {
        return this.idSinistroDoctoServico;
    }

    public void setIdSinistroDoctoServico(Long idSinistroDoctoServico) {
        this.idSinistroDoctoServico = idSinistroDoctoServico;
    }

    public DomainValue getTpPrejuizo() {
        return this.tpPrejuizo;
    }

    public void setTpPrejuizo(DomainValue tpPrejuizo) {
        this.tpPrejuizo = tpPrejuizo;
    }

    public BigDecimal getVlPrejuizo() {
        return this.vlPrejuizo;
    }

    public void setVlPrejuizo(BigDecimal vlPrejuizo) {
        this.vlPrejuizo = vlPrejuizo;
    }

    public DateTime getDhGeracaoCartaOcorrencia() {
        return this.dhGeracaoCartaOcorrencia;
    }

    public void setDhGeracaoCartaOcorrencia(DateTime dhGeracaoCartaOcorrencia) {
        this.dhGeracaoCartaOcorrencia = dhGeracaoCartaOcorrencia;
    }

    public DateTime getDhEnvioEmailOcorrencia() {
        return this.dhEnvioEmailOcorrencia;
    }

    public void setDhEnvioEmailOcorrencia(DateTime dhEnvioEmailOcorrencia) {
        this.dhEnvioEmailOcorrencia = dhEnvioEmailOcorrencia;
    }

    public DateTime getDhGeracaoCartaRetificacao() {
        return this.dhGeracaoCartaRetificacao;
    }

    public void setDhGeracaoCartaRetificacao(DateTime dhGeracaoCartaRetificacao) {
        this.dhGeracaoCartaRetificacao = dhGeracaoCartaRetificacao;
    }

    public DateTime getDhEnvioEmailRetificacao() {
        return this.dhEnvioEmailRetificacao;
    }

    public void setDhEnvioEmailRetificacao(DateTime dhEnvioEmailRetificacao) {
        this.dhEnvioEmailRetificacao = dhEnvioEmailRetificacao;
    }

    public DateTime getDhGeracaoFilialRim() {
        return this.dhGeracaoFilialRim;
    }

    public void setDhGeracaoFilialRim(DateTime dhGeracaoFilialRim) {
        this.dhGeracaoFilialRim = dhGeracaoFilialRim;
    }

    public DateTime getDhEnvioEmailFilialRim() {
        return this.dhEnvioEmailFilialRim;
    }

    public void setDhEnvioEmailFilialRim(DateTime dhEnvioEmailFilialRim) {
        this.dhEnvioEmailFilialRim = dhEnvioEmailFilialRim;
    }

    public DomainValue getTpFilialNotifEmissaoRim() {
        return this.tpFilialNotifEmissaoRim;
    }

    public void setTpFilialNotifEmissaoRim(DomainValue tpFilialNotifEmissaoRim) {
        this.tpFilialNotifEmissaoRim = tpFilialNotifEmissaoRim;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

	//LMS-6178
    public com.mercurio.lms.seguros.model.ProcessoSinistro getProcessoSinistro() {
		return processoSinistro;
	}

	public void setProcessoSinistro(
			com.mercurio.lms.seguros.model.ProcessoSinistro processoSinistro) {
		this.processoSinistro = processoSinistro;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idSinistroDoctoServico",
				getIdSinistroDoctoServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SinistroDoctoServico))
			return false;
        SinistroDoctoServico castOther = (SinistroDoctoServico) other;
		return new EqualsBuilder().append(this.getIdSinistroDoctoServico(),
				castOther.getIdSinistroDoctoServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSinistroDoctoServico())
            .toHashCode();
    }

}
