package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoOcorrenciaBanco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoOcorrenciaBanco;

    /** persistent field */
    private Short nrMotivoOcorrenciaBanco;

    /** persistent field */
    private String dsMotivoOcorrenciaBanco;

    /** persistent field */
    private com.mercurio.lms.contasreceber.model.OcorrenciaBanco ocorrenciaBanco;

    /** persistent field */
    private List historicoMotivoOcorrencias;

    public Long getIdMotivoOcorrenciaBanco() {
        return this.idMotivoOcorrenciaBanco;
    }

    public void setIdMotivoOcorrenciaBanco(Long idMotivoOcorrenciaBanco) {
        this.idMotivoOcorrenciaBanco = idMotivoOcorrenciaBanco;
    }

    public Short getNrMotivoOcorrenciaBanco() {
        return this.nrMotivoOcorrenciaBanco;
    }

    public void setNrMotivoOcorrenciaBanco(Short nrMotivoOcorrenciaBanco) {
        this.nrMotivoOcorrenciaBanco = nrMotivoOcorrenciaBanco;
    }

    public String getDsMotivoOcorrenciaBanco() {
        return this.dsMotivoOcorrenciaBanco;
    }

    public void setDsMotivoOcorrenciaBanco(String dsMotivoOcorrenciaBanco) {
        this.dsMotivoOcorrenciaBanco = dsMotivoOcorrenciaBanco;
    }

    public com.mercurio.lms.contasreceber.model.OcorrenciaBanco getOcorrenciaBanco() {
        return this.ocorrenciaBanco;
    }

	public void setOcorrenciaBanco(
			com.mercurio.lms.contasreceber.model.OcorrenciaBanco ocorrenciaBanco) {
        this.ocorrenciaBanco = ocorrenciaBanco;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia.class)     
    public List getHistoricoMotivoOcorrencias() {
        return this.historicoMotivoOcorrencias;
    }

    public void setHistoricoMotivoOcorrencias(List historicoMotivoOcorrencias) {
        this.historicoMotivoOcorrencias = historicoMotivoOcorrencias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoOcorrenciaBanco",
				getIdMotivoOcorrenciaBanco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoOcorrenciaBanco))
			return false;
        MotivoOcorrenciaBanco castOther = (MotivoOcorrenciaBanco) other;
		return new EqualsBuilder().append(this.getIdMotivoOcorrenciaBanco(),
				castOther.getIdMotivoOcorrenciaBanco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoOcorrenciaBanco())
            .toHashCode();
    }

}
