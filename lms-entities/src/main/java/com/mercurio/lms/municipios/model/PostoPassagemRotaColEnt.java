package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoPassagemRotaColEnt implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoPassagemRotaColEnt;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.PostoPassagem postoPassagem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega;

    public Long getIdPostoPassagemRotaColEnt() {
        return this.idPostoPassagemRotaColEnt;
    }

    public void setIdPostoPassagemRotaColEnt(Long idPostoPassagemRotaColEnt) {
        this.idPostoPassagemRotaColEnt = idPostoPassagemRotaColEnt;
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

    public com.mercurio.lms.municipios.model.RotaColetaEntrega getRotaColetaEntrega() {
        return this.rotaColetaEntrega;
    }

	public void setRotaColetaEntrega(
			com.mercurio.lms.municipios.model.RotaColetaEntrega rotaColetaEntrega) {
        this.rotaColetaEntrega = rotaColetaEntrega;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoPassagemRotaColEnt",
				getIdPostoPassagemRotaColEnt()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoPassagemRotaColEnt))
			return false;
        PostoPassagemRotaColEnt castOther = (PostoPassagemRotaColEnt) other;
		return new EqualsBuilder().append(this.getIdPostoPassagemRotaColEnt(),
				castOther.getIdPostoPassagemRotaColEnt()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoPassagemRotaColEnt())
            .toHashCode();
    }

}
