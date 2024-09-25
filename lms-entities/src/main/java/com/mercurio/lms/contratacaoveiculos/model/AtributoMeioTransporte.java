package com.mercurio.lms.contratacaoveiculos.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class AtributoMeioTransporte implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idAtributoMeioTransporte;

    /** persistent field */
    private VarcharI18n dsAtributoMeioTransporte;

    /** persistent field */
    private DomainValue tpComponente;
    
    /** persistent field */
    private DomainValue tpInformacao;
    
    /** persistent field */
    private Long nrTamanho;

    /** persistent field */
    private VarcharI18n dsGrupo;

    /** persistent field */
    private Short nrOrdem;
 
    /** nullable persistent field */
    private Byte nrDecimais;

    /** persistent field */
    private DomainValue tpSituacao;
    
    /** persistent field */
    private List modeloMeioTranspAtributos;
    
    public Long getIdAtributoMeioTransporte() {
        return this.idAtributoMeioTransporte;
    }

    public void setIdAtributoMeioTransporte(Long idAtributoMeioTransporte) {
        this.idAtributoMeioTransporte = idAtributoMeioTransporte;
    }

    public VarcharI18n getDsAtributoMeioTransporte() {
		return dsAtributoMeioTransporte;
    }

	public void setDsAtributoMeioTransporte(VarcharI18n dsAtributoMeioTransporte) {
        this.dsAtributoMeioTransporte = dsAtributoMeioTransporte;
    }

    public DomainValue getTpInformacao() {
        return this.tpInformacao;
    }

    public void setTpInformacao(DomainValue tpInformacao) {
        this.tpInformacao = tpInformacao;
    }
    
    public DomainValue getTpComponente() {
        return this.tpComponente;
    }

    public void setTpComponente(DomainValue tpComponente) {
        this.tpComponente = tpComponente;
    }

    public Long getNrTamanho() {
        return this.nrTamanho;
    }

    public void setNrTamanho(Long nrTamanho) {
        this.nrTamanho = nrTamanho;
    }

    public VarcharI18n getDsGrupo() {
		return dsGrupo;
    }

	public void setDsGrupo(VarcharI18n dsGrupo) {
        this.dsGrupo = dsGrupo;
    }

    public Short getNrOrdem() {
        return this.nrOrdem;
    }

    public void setNrOrdem(Short nrOrdem) {
        this.nrOrdem = nrOrdem;
    }

    public Byte getNrDecimais() {
        return this.nrDecimais;
    }

    public void setNrDecimais(Byte nrDecimais) {
        this.nrDecimais = nrDecimais;
    }
    
    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.contratacaoveiculos.model.ModeloMeioTranspAtributo.class)     
    public List getModeloMeioTranspAtributos() {
        return this.modeloMeioTranspAtributos;
    }

    public void setModeloMeioTranspAtributos(List modeloMeioTranspAtributos) {
        this.modeloMeioTranspAtributos = modeloMeioTranspAtributos;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idAtributoMeioTransporte",
				getIdAtributoMeioTransporte()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof AtributoMeioTransporte))
			return false;
        AtributoMeioTransporte castOther = (AtributoMeioTransporte) other;
		return new EqualsBuilder().append(this.getIdAtributoMeioTransporte(),
				castOther.getIdAtributoMeioTransporte()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdAtributoMeioTransporte())
            .toHashCode();
    }

}
