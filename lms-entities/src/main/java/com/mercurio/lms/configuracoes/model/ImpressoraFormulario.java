package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.expedicao.model.Impressora;

/** @author LMS Custom Hibernate CodeGenerator */
public class ImpressoraFormulario implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idImpressoraFormulario;

    /** persistent field */
    private Long nrFormularioInicial;

    /** persistent field */
    private Long nrFormularioFinal;

    /** persistent field */
    private Long nrUltimoFormulario;

    /** nullable persistent field */
    private Long nrSeloFiscalInicial;

    /** nullable persistent field */
    private String cdSerie;

    /** nullable persistent field */
    private Long nrUltimoSeloFiscal;

    /** nullable persistent field */
    private Long nrSeloFiscalFinal;

    /** persistent field */
    private Long nrCodigoBarrasInicial;

    /** persistent field */
    private Long nrCodigoBarrasFinal;

    /** persistent field */
    private Long nrUltimoCodigoBarras;

    /** persistent field */
    private ControleFormulario controleFormulario;

    /** persistent field */
    private Impressora impressora;

    public Long getIdImpressoraFormulario() {
        return this.idImpressoraFormulario;
    }

    public void setIdImpressoraFormulario(Long idImpressoraFormulario) {
        this.idImpressoraFormulario = idImpressoraFormulario;
    }

    public Long getNrFormularioInicial() {
        return this.nrFormularioInicial;
    }

    public void setNrFormularioInicial(Long nrFormularioInicial) {
        this.nrFormularioInicial = nrFormularioInicial;
    }

    public Long getNrFormularioFinal() {
        return this.nrFormularioFinal;
    }

    public void setNrFormularioFinal(Long nrFormularioFinal) {
        this.nrFormularioFinal = nrFormularioFinal;
    }

    public Long getNrUltimoFormulario() {
        return this.nrUltimoFormulario;
    }

    public void setNrUltimoFormulario(Long nrUltimoFormulario) {
        this.nrUltimoFormulario = nrUltimoFormulario;
    }

    public Long getNrSeloFiscalInicial() {
        return this.nrSeloFiscalInicial;
    }

    public void setNrSeloFiscalInicial(Long nrSeloFiscalInicial) {
        this.nrSeloFiscalInicial = nrSeloFiscalInicial;
    }

    public String getCdSerie() {
        return this.cdSerie;
    }

    public void setCdSerie(String cdSerie) {
        this.cdSerie = cdSerie;
    }

    public Long getNrUltimoSeloFiscal() {
        return this.nrUltimoSeloFiscal;
    }

    public void setNrUltimoSeloFiscal(Long nrUltimoSeloFiscal) {
        this.nrUltimoSeloFiscal = nrUltimoSeloFiscal;
    }

    public Long getNrSeloFiscalFinal() {
        return this.nrSeloFiscalFinal;
    }

    public void setNrSeloFiscalFinal(Long nrSeloFiscalFinal) {
        this.nrSeloFiscalFinal = nrSeloFiscalFinal;
    }

    public ControleFormulario getControleFormulario() {
        return this.controleFormulario;
    }

    public void setControleFormulario(ControleFormulario controleFormulario) {
        this.controleFormulario = controleFormulario;
    }

    public Impressora getImpressora() {
        return this.impressora;
    }

    public void setImpressora(Impressora impressora) {
        this.impressora = impressora;
    }

    public Long getNrCodigoBarrasInicial() {
		return nrCodigoBarrasInicial;
	}

	public void setNrCodigoBarrasInicial(Long nrCodigoBarrasInicial) {
		this.nrCodigoBarrasInicial = nrCodigoBarrasInicial;
	}

	public Long getNrCodigoBarrasFinal() {
		return nrCodigoBarrasFinal;
	}

	public void setNrCodigoBarrasFinal(Long nrCodigoBarrasFinal) {
		this.nrCodigoBarrasFinal = nrCodigoBarrasFinal;
	}

	public Long getNrUltimoCodigoBarras() {
		return nrUltimoCodigoBarras;
	}

	public void setNrUltimoCodigoBarras(Long nrUltimoCodigoBarras) {
		this.nrUltimoCodigoBarras = nrUltimoCodigoBarras;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idImpressoraFormulario",
				getIdImpressoraFormulario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ImpressoraFormulario))
			return false;
        ImpressoraFormulario castOther = (ImpressoraFormulario) other;
		return new EqualsBuilder().append(this.getIdImpressoraFormulario(),
				castOther.getIdImpressoraFormulario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdImpressoraFormulario())
            .toHashCode();
    }

}
