package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoMotivoOcorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idHistoricoMotivoOcorrencia;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.HistoricoBoleto historicoBoleto;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco motivoOcorrenciaBanco;

    public Long getIdHistoricoMotivoOcorrencia() {
        return this.idHistoricoMotivoOcorrencia;
    }

    public void setIdHistoricoMotivoOcorrencia(Long idHistoricoMotivoOcorrencia) {
        this.idHistoricoMotivoOcorrencia = idHistoricoMotivoOcorrencia;
    }

    public com.mercurio.lms.contasreceber.model.HistoricoBoleto getHistoricoBoleto() {
        return this.historicoBoleto;
    }

	public void setHistoricoBoleto(
			com.mercurio.lms.contasreceber.model.HistoricoBoleto historicoBoleto) {
        this.historicoBoleto = historicoBoleto;
    }

    public com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco getMotivoOcorrenciaBanco() {
        return this.motivoOcorrenciaBanco;
    }

	public void setMotivoOcorrenciaBanco(
			com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco motivoOcorrenciaBanco) {
        this.motivoOcorrenciaBanco = motivoOcorrenciaBanco;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idHistoricoMotivoOcorrencia",
				getIdHistoricoMotivoOcorrencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoMotivoOcorrencia))
			return false;
        HistoricoMotivoOcorrencia castOther = (HistoricoMotivoOcorrencia) other;
		return new EqualsBuilder().append(
				this.getIdHistoricoMotivoOcorrencia(),
				castOther.getIdHistoricoMotivoOcorrencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoMotivoOcorrencia())
            .toHashCode();
    }

}
