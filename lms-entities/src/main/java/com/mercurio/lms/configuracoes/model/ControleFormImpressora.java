package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ControleFormImpressora implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idControleFormImpressora;

    /** persistent field */
    private Long nrFormularioInicial;

    /** persistent field */
    private Long nrFormularioFinal;

    /** persistent field */
    private Long nrUltimaImpressao;

    /** nullable persistent field */
    private String dsSerie;

    /** nullable persistent field */
    private Long nrSeloFiscalInicial;

    /** nullable persistent field */
    private com.mercurio.lms.configuracoes.model.ControleFormulario controleFormulario;

    /** nullable persistent field */
    private com.mercurio.lms.expedicao.model.Impressora impressora;

    public Long getIdControleFormImpressora() {
        return this.idControleFormImpressora;
    }

    public void setIdControleFormImpressora(Long idControleFormImpressora) {
        this.idControleFormImpressora = idControleFormImpressora;
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

    public Long getNrUltimaImpressao() {
        return this.nrUltimaImpressao;
    }

    public void setNrUltimaImpressao(Long nrUltimaImpressao) {
        this.nrUltimaImpressao = nrUltimaImpressao;
    }

    public String getDsSerie() {
        return this.dsSerie;
    }

    public void setDsSerie(String dsSerie) {
        this.dsSerie = dsSerie;
    }

    public Long getNrSeloFiscalInicial() {
        return this.nrSeloFiscalInicial;
    }

    public void setNrSeloFiscalInicial(Long nrSeloFiscalInicial) {
        this.nrSeloFiscalInicial = nrSeloFiscalInicial;
    }

    public com.mercurio.lms.configuracoes.model.ControleFormulario getControleFormulario() {
        return this.controleFormulario;
    }

	public void setControleFormulario(
			com.mercurio.lms.configuracoes.model.ControleFormulario controleFormulario) {
        this.controleFormulario = controleFormulario;
    }

    public com.mercurio.lms.expedicao.model.Impressora getImpressora() {
        return this.impressora;
    }

	public void setImpressora(
			com.mercurio.lms.expedicao.model.Impressora impressora) {
        this.impressora = impressora;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idControleFormImpressora",
						getIdControleFormImpressora())
            .append("nrFormularioInicial", getNrFormularioInicial())
            .append("nrFormularioFinal", getNrFormularioFinal())
            .append("nrUltimaImpressao", getNrUltimaImpressao())
            .append("dsSerie", getDsSerie())
            .append("nrSeloFiscalInicial", getNrSeloFiscalInicial())
            .toString();
    }

}
