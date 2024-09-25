package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.lms.util.Vigencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class VersaoPce implements Serializable, Vigencia {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idVersaoPce;

    private Integer versao;
    /** persistent field */
    private Integer nrVersaoPce;

    /** persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private List versaoDescritivoPces;

    public Long getIdVersaoPce() {
        return this.idVersaoPce;
    }

    public void setIdVersaoPce(Long idVersaoPce) {
        this.idVersaoPce = idVersaoPce;
    }

    public Integer getNrVersaoPce() {
        return this.nrVersaoPce;
    }

    public void setNrVersaoPce(Integer nrVersaoPce) {
        this.nrVersaoPce = nrVersaoPce;
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

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.VersaoDescritivoPce.class)     
    public List getVersaoDescritivoPces() {
        return this.versaoDescritivoPces;
    }

    public void setVersaoDescritivoPces(List versaoDescritivoPces) {
        this.versaoDescritivoPces = versaoDescritivoPces;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idVersaoPce", getIdVersaoPce()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VersaoPce))
			return false;
        VersaoPce castOther = (VersaoPce) other;
		return new EqualsBuilder().append(this.getIdVersaoPce(),
				castOther.getIdVersaoPce()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdVersaoPce()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
