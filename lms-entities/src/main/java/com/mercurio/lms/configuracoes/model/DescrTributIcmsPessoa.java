package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescrTributIcmsPessoa implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescrTributIcmsPessoa;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private String dsObservacao;

    /** nullable persistent field */
    private String dsObservacaoExportacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.InscricaoEstadual inscricaoEstadual;

    public Long getIdDescrTributIcmsPessoa() {
        return this.idDescrTributIcmsPessoa;
    }

    public void setIdDescrTributIcmsPessoa(Long idDescrTributIcmsPessoa) {
        this.idDescrTributIcmsPessoa = idDescrTributIcmsPessoa;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public String getDsObservacao() {
        return this.dsObservacao;
    }

    public void setDsObservacao(String dsObservacao) {
        this.dsObservacao = dsObservacao;
    }

    public String getDsObservacaoExportacao() {
        return this.dsObservacaoExportacao;
    }

    public void setDsObservacaoExportacao(String dsObservacaoExportacao) {
        this.dsObservacaoExportacao = dsObservacaoExportacao;
    }

    public com.mercurio.lms.configuracoes.model.InscricaoEstadual getInscricaoEstadual() {
        return this.inscricaoEstadual;
    }

	public void setInscricaoEstadual(
			com.mercurio.lms.configuracoes.model.InscricaoEstadual inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescrTributIcmsPessoa",
				getIdDescrTributIcmsPessoa()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescrTributIcmsPessoa))
			return false;
        DescrTributIcmsPessoa castOther = (DescrTributIcmsPessoa) other;
		return new EqualsBuilder().append(this.getIdDescrTributIcmsPessoa(),
				castOther.getIdDescrTributIcmsPessoa()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescrTributIcmsPessoa())
            .toHashCode();
    }

}
