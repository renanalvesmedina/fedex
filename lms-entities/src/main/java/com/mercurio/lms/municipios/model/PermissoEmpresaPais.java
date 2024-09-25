package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class PermissoEmpresaPais implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPermissoEmpresaPais;

    /** persistent field */
    private Integer nrPermisso;

    /** persistent field */
    private String nrPermissoMic;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais paisByIdPaisDestino;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Pais paisByIdPaisOrigem;
    
    /** persistent field */
    private List ctoInternacionais;

    public Long getIdPermissoEmpresaPais() {
        return this.idPermissoEmpresaPais;
    }

    public void setIdPermissoEmpresaPais(Long idPermissoEmpresaPais) {
        this.idPermissoEmpresaPais = idPermissoEmpresaPais;
    }

    public Integer getNrPermisso() {
        return this.nrPermisso;
    }

    public void setNrPermisso(Integer nrPermisso) {
        this.nrPermisso = nrPermisso;
    }

    public String getNrPermissoMic() {
        return this.nrPermissoMic;
    }

    public void setNrPermissoMic(String nrPermissoMic) {
        this.nrPermissoMic = nrPermissoMic;
    }

    public YearMonthDay getDtVigenciaInicial() {
        return this.dtVigenciaInicial;
    }

    public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
        this.dtVigenciaInicial = dtVigenciaInicial;
    }

    public YearMonthDay getDtVigenciaFinal() {
        return this.dtVigenciaFinal;
    }

    public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
        this.dtVigenciaFinal = dtVigenciaFinal;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
        this.empresa = empresa;
    }

    public com.mercurio.lms.municipios.model.Pais getPaisByIdPaisDestino() {
        return this.paisByIdPaisDestino;
    }

	public void setPaisByIdPaisDestino(
			com.mercurio.lms.municipios.model.Pais paisByIdPaisDestino) {
        this.paisByIdPaisDestino = paisByIdPaisDestino;
    }

    public com.mercurio.lms.municipios.model.Pais getPaisByIdPaisOrigem() {
        return this.paisByIdPaisOrigem;
    }

	public void setPaisByIdPaisOrigem(
			com.mercurio.lms.municipios.model.Pais paisByIdPaisOrigem) {
        this.paisByIdPaisOrigem = paisByIdPaisOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoInternacional.class)     
    public List getCtoInternacionais() {
        return this.ctoInternacionais;
    }

    public void setCtoInternacionais(List ctoInternacionais) {
        this.ctoInternacionais = ctoInternacionais;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPermissoEmpresaPais",
				getIdPermissoEmpresaPais()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PermissoEmpresaPais))
			return false;
        PermissoEmpresaPais castOther = (PermissoEmpresaPais) other;
		return new EqualsBuilder().append(this.getIdPermissoEmpresaPais(),
				castOther.getIdPermissoEmpresaPais()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPermissoEmpresaPais())
            .toHashCode();
    }

}
