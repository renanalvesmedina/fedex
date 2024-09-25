package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoVisita implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoVisita;

    /** persistent field */
    private VarcharI18n dsTipoVisita;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private List etapaVisitas;

    public Long getIdTipoVisita() {
        return this.idTipoVisita;
    }

    public void setIdTipoVisita(Long idTipoVisita) {
        this.idTipoVisita = idTipoVisita;
    }

    public VarcharI18n getDsTipoVisita() {
		return dsTipoVisita;
    }

	public void setDsTipoVisita(VarcharI18n dsTipoVisita) {
        this.dsTipoVisita = dsTipoVisita;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.EtapaVisita.class)     
    public List getEtapaVisitas() {
        return this.etapaVisitas;
    }

    public void setEtapaVisitas(List etapaVisitas) {
        this.etapaVisitas = etapaVisitas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoVisita",
				getIdTipoVisita()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoVisita))
			return false;
        TipoVisita castOther = (TipoVisita) other;
		return new EqualsBuilder().append(this.getIdTipoVisita(),
				castOther.getIdTipoVisita()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoVisita()).toHashCode();
    }

}
