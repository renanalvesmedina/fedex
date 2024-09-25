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
public class ProcessoPce implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final long ID_PROCESSO_PCE_COLETA = 1;
	public static final long ID_PROCESSO_PCE_RECEPCAO = 2;
	public static final long ID_PROCESSO_PCE_EXPEDICAO = 3;
	public static final long ID_PROCESSO_PCE_CARREGAMENTO = 4;
	public static final long ID_PROCESSO_PCE_DESCARGA = 5;
	public static final long ID_PROCESSO_PCE_AGENDAMENTO = 6;
	public static final long ID_PROCESSO_PCE_RNC = 7;
	public static final long ID_PROCESSO_PCE_ENTREGA = 8;
	public static final long ID_PROCESSO_PCE_SINISTRO = 9;
	public static final long ID_PROCESSO_PCE_MERCADORIA_DISPOSICAO = 10;
	public static final long ID_PROCESSO_PCE_INDENIZACAO = 11;
	public static final long ID_PROCESSO_PCE_PENDENCIA = 12;

    /** identifier field */
    private Long idProcessoPce;

    /** persistent field */
    private VarcharI18n dsProcessoPce;

    /** persistent field */
    private DomainValue tpSituacao;

    /** nullable persistent field */
    private DomainValue tpModal;
    
    private DomainValue tpAbrangencia;
    
    private Long cdProcessoPce;

    /** persistent field */
    private List eventoPces;

    public String getProcessoPceGrid() {
    	if(this.cdProcessoPce != null && this.dsProcessoPce != null)
			return this.cdProcessoPce
					+ " - "
					+ this.dsProcessoPce.getValue(LocaleContextHolder
							.getLocale());
		return null;
	}
       
    public String getProcessoPceCombo(){
    	StringBuffer sb = new StringBuffer();
    	if (this.cdProcessoPce != null && this.dsProcessoPce != null)
			sb.append(this.cdProcessoPce)
					.append(" - ")
					.append(this.dsProcessoPce.getValue(LocaleContextHolder
							.getLocale()));
    	if (this.tpModal != null && this.tpModal.getDescription() != null)
			sb.append(" - ").append(
					this.tpModal.getDescription().getValue(
							LocaleContextHolder.getLocale()));
		if (this.tpAbrangencia != null
				&& this.tpAbrangencia.getDescription() != null)
			sb.append(" - ").append(
					this.tpAbrangencia.getDescription().getValue(
							LocaleContextHolder.getLocale()));
    	return sb.toString();
    }

    public Long getIdProcessoPce() {
        return this.idProcessoPce;
    }

    public void setIdProcessoPce(Long idProcessoPce) {
        this.idProcessoPce = idProcessoPce;
    }

    public VarcharI18n getDsProcessoPce() {
		return dsProcessoPce;
    }

	public void setDsProcessoPce(VarcharI18n dsProcessoPce) {
        this.dsProcessoPce = dsProcessoPce;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }
    
    public Long getCdProcessoPce() {
    	return this.cdProcessoPce;
	}
    
    public void setCdProcessoPce(Long cdProcessoPce) {
		this.cdProcessoPce = cdProcessoPce;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.vendas.model.EventoPce.class)     
    public List getEventoPces() {
        return this.eventoPces;
    }

    public void setEventoPces(List eventoPces) {
        this.eventoPces = eventoPces;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idProcessoPce",
				getIdProcessoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ProcessoPce))
			return false;
        ProcessoPce castOther = (ProcessoPce) other;
		return new EqualsBuilder().append(this.getIdProcessoPce(),
				castOther.getIdProcessoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdProcessoPce()).toHashCode();
    }

	public DomainValue getTpAbrangencia() {
		return tpAbrangencia;
	}

	public void setTpAbrangencia(DomainValue tpAbrangencia) {
		this.tpAbrangencia = tpAbrangencia;
	}

}
