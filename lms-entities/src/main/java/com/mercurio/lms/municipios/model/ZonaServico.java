package com.mercurio.lms.municipios.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.util.Vigencia;

/** @author Hibernate CodeGenerator */
public class ZonaServico implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idZonaServico;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Zona zona;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Servico servico;
    
    public Long getIdZonaServico() {
        return this.idZonaServico;
    }

    public void setIdZonaServico(Long idZonaServico) {
        this.idZonaServico = idZonaServico;
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

    public com.mercurio.lms.municipios.model.Zona getZona() {
        return this.zona;
    }

    public void setZona(com.mercurio.lms.municipios.model.Zona zona) {
        this.zona = zona;
    }

    public com.mercurio.lms.configuracoes.model.Servico getServico() {
        return this.servico;
    }

    public void setServico(com.mercurio.lms.configuracoes.model.Servico servico) {
        this.servico = servico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idZonaServico",
				getIdZonaServico()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ZonaServico))
			return false;
        ZonaServico castOther = (ZonaServico) other;
		return new EqualsBuilder().append(this.getIdZonaServico(),
				castOther.getIdZonaServico()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdZonaServico()).toHashCode();
    }

}
