package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class BoxFinalidade implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idBoxFinalidade;

    /** persistent field */
    private TimeOfDay hrInicial;

    /** persistent field */
    private TimeOfDay hrFinal;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Finalidade finalidade;

    /** persistent field */
    private com.mercurio.lms.portaria.model.Box box;

    public Long getIdBoxFinalidade() {
        return this.idBoxFinalidade;
    }

    public void setIdBoxFinalidade(Long idBoxFinalidade) {
        this.idBoxFinalidade = idBoxFinalidade;
    }

    public TimeOfDay getHrInicial() {
        return this.hrInicial;
    }

    public void setHrInicial(TimeOfDay hrInicial) {
        this.hrInicial = hrInicial;
    }

    public TimeOfDay getHrFinal() {
        return this.hrFinal;
    }

    public void setHrFinal(TimeOfDay hrFinal) {
        this.hrFinal = hrFinal;
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

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public com.mercurio.lms.portaria.model.Finalidade getFinalidade() {
        return this.finalidade;
    }

	public void setFinalidade(
			com.mercurio.lms.portaria.model.Finalidade finalidade) {
        this.finalidade = finalidade;
    }

    public com.mercurio.lms.portaria.model.Box getBox() {
        return this.box;
    }

    public void setBox(com.mercurio.lms.portaria.model.Box box) {
        this.box = box;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idBoxFinalidade",
				getIdBoxFinalidade()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof BoxFinalidade))
			return false;
        BoxFinalidade castOther = (BoxFinalidade) other;
		return new EqualsBuilder().append(this.getIdBoxFinalidade(),
				castOther.getIdBoxFinalidade()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdBoxFinalidade()).toHashCode();
    }

}
