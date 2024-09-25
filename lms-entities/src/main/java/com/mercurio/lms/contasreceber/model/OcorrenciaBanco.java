package com.mercurio.lms.contasreceber.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaBanco implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaBanco;

    /** persistent field */
    private Short nrOcorrenciaBanco;

    /** persistent field */
    private DomainValue tpOcorrenciaBanco;

    /** persistent field */
    private String dsOcorrenciaBanco;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Banco banco;

    /** persistent field */
    private List historicoBoletos;

    /** persistent field */
    private List tarifaBoletos;

    /** persistent field */
    private List motivoOcorrenciaBancos;

    public Long getIdOcorrenciaBanco() {
        return this.idOcorrenciaBanco;
    }

    public void setIdOcorrenciaBanco(Long idOcorrenciaBanco) {
        this.idOcorrenciaBanco = idOcorrenciaBanco;
    }

    public Short getNrOcorrenciaBanco() {
        return this.nrOcorrenciaBanco;
    }

    public void setNrOcorrenciaBanco(Short nrOcorrenciaBanco) {
        this.nrOcorrenciaBanco = nrOcorrenciaBanco;
    }

    public DomainValue getTpOcorrenciaBanco() {
        return this.tpOcorrenciaBanco;
    }

    public void setTpOcorrenciaBanco(DomainValue tpOcorrenciaBanco) {
        this.tpOcorrenciaBanco = tpOcorrenciaBanco;
    }

    public String getDsOcorrenciaBanco() {
        return this.dsOcorrenciaBanco;
    }

    public void setDsOcorrenciaBanco(String dsOcorrenciaBanco) {
        this.dsOcorrenciaBanco = dsOcorrenciaBanco;
    }

    public com.mercurio.lms.configuracoes.model.Banco getBanco() {
        return this.banco;
    }

    public void setBanco(com.mercurio.lms.configuracoes.model.Banco banco) {
        this.banco = banco;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.HistoricoBoleto.class)     
    public List getHistoricoBoletos() {
        return this.historicoBoletos;
    }

    public void setHistoricoBoletos(List historicoBoletos) {
        this.historicoBoletos = historicoBoletos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.TarifaBoleto.class)     
    public List getTarifaBoletos() {
        return this.tarifaBoletos;
    }

    public void setTarifaBoletos(List tarifaBoletos) {
        this.tarifaBoletos = tarifaBoletos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco.class)     
    public List getMotivoOcorrenciaBancos() {
        return this.motivoOcorrenciaBancos;
    }

    public void setMotivoOcorrenciaBancos(List motivoOcorrenciaBancos) {
        this.motivoOcorrenciaBancos = motivoOcorrenciaBancos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaBanco",
				getIdOcorrenciaBanco()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaBanco))
			return false;
        OcorrenciaBanco castOther = (OcorrenciaBanco) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaBanco(),
				castOther.getIdOcorrenciaBanco()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaBanco())
            .toHashCode();
    }

}
