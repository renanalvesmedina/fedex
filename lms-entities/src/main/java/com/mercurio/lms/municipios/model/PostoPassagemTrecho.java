package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class PostoPassagemTrecho implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPostoPassagemTrecho;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;
    
    /** persistent field */
    private Filial filialOrigem;

    /** persistent field */
    private Filial filialDestino;

    /** persistent field */
    private PostoPassagem postoPassagem;
    
    public Long getIdPostoPassagemTrecho() {
		return idPostoPassagemTrecho;
	}

	public void setIdPostoPassagemTrecho(Long idPostoPassagemTrecho) {
		this.idPostoPassagemTrecho = idPostoPassagemTrecho;
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

    public com.mercurio.lms.municipios.model.Filial getFilialOrigem() {
        return this.filialOrigem;
    }

	public void setFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialOrigem) {
        this.filialOrigem = filialOrigem;
    }

    public com.mercurio.lms.municipios.model.PostoPassagem getPostoPassagem() {
        return this.postoPassagem;
    }

	public void setPostoPassagem(
			com.mercurio.lms.municipios.model.PostoPassagem postoPassagem) {
        this.postoPassagem = postoPassagem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialDestino() {
        return this.filialDestino;
    }

	public void setFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialDestino) {
        this.filialDestino = filialDestino;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idPostoPassagemTrecho",
				getIdPostoPassagemTrecho()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PostoPassagemTrecho))
			return false;
        PostoPassagemTrecho castOther = (PostoPassagemTrecho) other;
		return new EqualsBuilder().append(this.getIdPostoPassagemTrecho(),
				castOther.getIdPostoPassagemTrecho()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPostoPassagemTrecho())
            .toHashCode();
    }

}    
