package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescritivoPce implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescritivoPce;

    /** persistent field */
    private VarcharI18n dsDescritivoPce;

    /** persistent field */
    private DomainValue tpAcao;

    /** persistent field */
    private Boolean blIndicadorAviso;

    /** persistent field */
    private DomainValue tpSituacao;
    
    private Long cdDescritivoPce;

    /** persistent field */
    private com.mercurio.lms.vendas.model.OcorrenciaPce ocorrenciaPce;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;
    
    /** persistent field */
    private List versaoDescritivoPces;

    /** persistent field */
    private List historicoPces;

    public String getDescritivoCombo() {
    	if (this.idDescritivoPce != null && this.dsDescritivoPce != null)
			return this.idDescritivoPce
					+ " - "
					+ this.dsDescritivoPce.getValue(LocaleContextHolder
							.getLocale());
    	return null;
    }

    public Long getIdDescritivoPce() {
        return this.idDescritivoPce;
    }

    public void setIdDescritivoPce(Long idDescritivoPce) {
        this.idDescritivoPce = idDescritivoPce;
    }

    public VarcharI18n getDsDescritivoPce() {
		return dsDescritivoPce;
    }

	public void setDsDescritivoPce(VarcharI18n dsDescritivoPce) {
        this.dsDescritivoPce = dsDescritivoPce;
    }

    public DomainValue getTpAcao() {
        return this.tpAcao;
    }

    public void setTpAcao(DomainValue tpAcao) {
        this.tpAcao = tpAcao;
    }

    public Boolean getBlIndicadorAviso() {
        return this.blIndicadorAviso;
    }

    public void setBlIndicadorAviso(Boolean blIndicadorAviso) {
        this.blIndicadorAviso = blIndicadorAviso;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }
    
    public Long getCdDescritivoPce() {
		return cdDescritivoPce;
	}
    
	public void setCdDescritivoPce(Long cdDescritivoPce) {
		this.cdDescritivoPce = cdDescritivoPce;
	}
	
	public com.mercurio.lms.vendas.model.OcorrenciaPce getOcorrenciaPce() {
        return this.ocorrenciaPce;
    }

	public void setOcorrenciaPce(
			com.mercurio.lms.vendas.model.OcorrenciaPce ocorrenciaPce) {
        this.ocorrenciaPce = ocorrenciaPce;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }
    
    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.VersaoDescritivoPce.class)     
    public List getVersaoDescritivoPces() {
        return this.versaoDescritivoPces;
    }

    public void setVersaoDescritivoPces(List versaoDescritivoPces) {
        this.versaoDescritivoPces = versaoDescritivoPces;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.HistoricoPce.class)     
    public List getHistoricoPces() {
        return this.historicoPces;
    }

    public void setHistoricoPces(List historicoPces) {
        this.historicoPces = historicoPces;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescritivoPce",
				getIdDescritivoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescritivoPce))
			return false;
        DescritivoPce castOther = (DescritivoPce) other;
		return new EqualsBuilder().append(this.getIdDescritivoPce(),
				castOther.getIdDescritivoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescritivoPce()).toHashCode();
    }
	
}
