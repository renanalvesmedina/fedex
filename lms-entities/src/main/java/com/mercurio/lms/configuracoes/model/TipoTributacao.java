package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TipoTributacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoTributacao;

    /** persistent field */
    private String dsTipoTributacao;

    /** persistent field */
    private String tpSituacao;

    public Long getIdTipoTributacao() {
        return this.idTipoTributacao;
    }

    public void setIdTipoTributacao(Long idTipoTributacao) {
        this.idTipoTributacao = idTipoTributacao;
    }

    public String getDsTipoTributacao() {
        return this.dsTipoTributacao;
    }

    public void setDsTipoTributacao(String dsTipoTributacao) {
        this.dsTipoTributacao = dsTipoTributacao;
    }

    public String getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(String tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("idTipoTributacao", getIdTipoTributacao())
            .append("dsTipoTributacao", getDsTipoTributacao())
				.append("tpSituacao", getTpSituacao()).toString();
    }

}
