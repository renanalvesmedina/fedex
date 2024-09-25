package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoPassagemMunicipio implements Serializable,Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoPassagemMunicipio;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.PostoPassagem postoPassagem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial;

    public Long getIdPostoPassagemMunicipio() {
        return this.idPostoPassagemMunicipio;
    }

    public void setIdPostoPassagemMunicipio(Long idPostoPassagemMunicipio) {
        this.idPostoPassagemMunicipio = idPostoPassagemMunicipio;
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

    public com.mercurio.lms.municipios.model.PostoPassagem getPostoPassagem() {
        return this.postoPassagem;
    }

	public void setPostoPassagem(
			com.mercurio.lms.municipios.model.PostoPassagem postoPassagem) {
        this.postoPassagem = postoPassagem;
    }

    public com.mercurio.lms.municipios.model.MunicipioFilial getMunicipioFilial() {
        return this.municipioFilial;
    }

	public void setMunicipioFilial(
			com.mercurio.lms.municipios.model.MunicipioFilial municipioFilial) {
        this.municipioFilial = municipioFilial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoPassagemMunicipio",
				getIdPostoPassagemMunicipio()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoPassagemMunicipio))
			return false;
        PostoPassagemMunicipio castOther = (PostoPassagemMunicipio) other;
		return new EqualsBuilder().append(this.getIdPostoPassagemMunicipio(),
				castOther.getIdPostoPassagemMunicipio()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoPassagemMunicipio())
            .toHashCode();
    }

}
