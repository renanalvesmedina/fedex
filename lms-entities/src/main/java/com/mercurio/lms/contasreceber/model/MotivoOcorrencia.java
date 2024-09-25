package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class MotivoOcorrencia implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMotivoOcorrencia;

    /** persistent field */
    private VarcharI18n dsMotivoOcorrencia;

    /** persistent field */
    private DomainValue tpMotivoOcorrencia;

    /** persistent field */
    private List historicoBoletos;
    
    /** persistent field */
    private DomainValue tpSituacao;

    public Long getIdMotivoOcorrencia() {
        return this.idMotivoOcorrencia;
    }

    public void setIdMotivoOcorrencia(Long idMotivoOcorrencia) {
        this.idMotivoOcorrencia = idMotivoOcorrencia;
    }

    public VarcharI18n getDsMotivoOcorrencia() {
		return dsMotivoOcorrencia;
    }

	public void setDsMotivoOcorrencia(VarcharI18n dsMotivoOcorrencia) {
        this.dsMotivoOcorrencia = dsMotivoOcorrencia;
    }

    public DomainValue getTpMotivoOcorrencia() {
        return this.tpMotivoOcorrencia;
    }

    public void setTpMotivoOcorrencia(DomainValue tpMotivoOcorrencia) {
        this.tpMotivoOcorrencia = tpMotivoOcorrencia;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoBoleto.class)     
    public List getHistoricoBoletos() {
        return this.historicoBoletos;
    }

    public void setHistoricoBoletos(List historicoBoletos) {
        this.historicoBoletos = historicoBoletos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMotivoOcorrencia",
				getIdMotivoOcorrencia()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MotivoOcorrencia))
			return false;
        MotivoOcorrencia castOther = (MotivoOcorrencia) other;
		return new EqualsBuilder().append(this.getIdMotivoOcorrencia(),
				castOther.getIdMotivoOcorrencia()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMotivoOcorrencia())
            .toHashCode();
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DomainValue getTpSituacao() {
        return tpSituacao;
    }

}
