package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoSinistro implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoSinistro;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private VarcharI18n dsTipo;

    /** persistent field */
    private VarcharI18n dsTextoCartaOcorrencia;

    /** persistent field */
    private VarcharI18n dsTextoCartaRetificacao;
    
    /** persistent field */
    private List processoSinistros;

    /** persistent field */
    private List seguroTipoSinistros;

    public Long getIdTipoSinistro() {
        return this.idTipoSinistro;
    }

    public void setIdTipoSinistro(Long idTipoSinistro) {
        this.idTipoSinistro = idTipoSinistro;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public VarcharI18n getDsTipo() {
		return dsTipo;
    }

	public void setDsTipo(VarcharI18n dsTipo) {
        this.dsTipo = dsTipo;
    }

	public VarcharI18n getDsTextoCartaOcorrencia() {
		return dsTextoCartaOcorrencia;
    }

	public void setDsTextoCartaOcorrencia(VarcharI18n dsTextoCartaOcorrencia) {
        this.dsTextoCartaOcorrencia = dsTextoCartaOcorrencia;
    }

	public void setDsTextoCartaRetificacao(VarcharI18n dsTextoCartaRetificacao) {
		this.dsTextoCartaRetificacao = dsTextoCartaRetificacao;
	}

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.ProcessoSinistro.class)     
    public List getProcessoSinistros() {
        return this.processoSinistros;
    }

    public void setProcessoSinistros(List processoSinistros) {
        this.processoSinistros = processoSinistros;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.seguros.model.SeguroTipoSinistro.class)     
    public List getSeguroTipoSinistros() {
        return this.seguroTipoSinistros;
    }

    public void setSeguroTipoSinistros(List seguroTipoSinistros) {
        this.seguroTipoSinistros = seguroTipoSinistros;
    }

    public VarcharI18n getDsTextoCartaRetificacao() {
		return dsTextoCartaRetificacao;
	}

    public String toString() {
		return new ToStringBuilder(this).append("idTipoSinistro",
				getIdTipoSinistro()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoSinistro))
			return false;
        TipoSinistro castOther = (TipoSinistro) other;
		return new EqualsBuilder().append(this.getIdTipoSinistro(),
				castOther.getIdTipoSinistro()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoSinistro()).toHashCode();
    }

}