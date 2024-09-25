package com.mercurio.lms.coleta.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class OcorrenciaColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idOcorrenciaColeta;

    /** persistent field */
    private DomainValue tpEventoColeta;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private VarcharI18n dsDescricaoResumida;

    /** nullable persistent field */
    private VarcharI18n dsDescricaoCompleta;

    /** nullable persistent field */
    private Boolean blIneficienciaFrota;
    
    /** persistent field */
    private Short codigo;

    /** persistent field */
    private List eventoColetas;
    
    public Long getIdOcorrenciaColeta() {
        return this.idOcorrenciaColeta;
    }

    public void setIdOcorrenciaColeta(Long idOcorrenciaColeta) {
        this.idOcorrenciaColeta = idOcorrenciaColeta;
    }

    public DomainValue getTpEventoColeta() {
        return this.tpEventoColeta;
    }

    public void setTpEventoColeta(DomainValue tpEventoColeta) {
        this.tpEventoColeta = tpEventoColeta;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public VarcharI18n getDsDescricaoResumida() {
		return dsDescricaoResumida;
    }

	public void setDsDescricaoResumida(VarcharI18n dsDescricaoResumida) {
        this.dsDescricaoResumida = dsDescricaoResumida;
    }

	public VarcharI18n getDsDescricaoCompleta() {
		return dsDescricaoCompleta;
    }

	public void setDsDescricaoCompleta(VarcharI18n dsDescricaoCompleta) {
        this.dsDescricaoCompleta = dsDescricaoCompleta;
    }

    public Boolean getBlIneficienciaFrota() {
        return this.blIneficienciaFrota;
    }

    public void setBlIneficienciaFrota(Boolean blIneficienciaFrota) {
        this.blIneficienciaFrota = blIneficienciaFrota;
    }

    public Short getCodigo() {
		return codigo;
	}

	public void setCodigo(Short codigo) {
		this.codigo = codigo;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.coleta.model.EventoColeta.class)     
    public List getEventoColetas() {
        return this.eventoColetas;
    }

    public void setEventoColetas(List eventoColetas) {
        this.eventoColetas = eventoColetas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idOcorrenciaColeta",
				getIdOcorrenciaColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof OcorrenciaColeta))
			return false;
        OcorrenciaColeta castOther = (OcorrenciaColeta) other;
		return new EqualsBuilder().append(this.getIdOcorrenciaColeta(),
				castOther.getIdOcorrenciaColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdOcorrenciaColeta())
            .toHashCode();
    }
}
