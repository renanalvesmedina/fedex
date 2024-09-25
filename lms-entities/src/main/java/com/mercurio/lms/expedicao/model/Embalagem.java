package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class Embalagem implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEmbalagem;

    /** persistent field */
    private Integer nrAltura;

    /** persistent field */
    private Integer nrLargura;

    /** persistent field */
    private Integer nrComprimento;

    /** persistent field */
    private VarcharI18n dsEmbalagem;

    /** persistent field */
    private Boolean blPrecificada;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List awbEmbalagems;

    /** persistent field */
    private List ctoCtoCooperadas;

    /** persistent field */
    private List servicoEmbalagems;

    public Long getIdEmbalagem() {
        return this.idEmbalagem;
    }

    public void setIdEmbalagem(Long idEmbalagem) {
        this.idEmbalagem = idEmbalagem;
    }

    public Integer getNrAltura() {
        return this.nrAltura;
    }

    public void setNrAltura(Integer nrAltura) {
        this.nrAltura = nrAltura;
    }

    public Integer getNrLargura() {
        return this.nrLargura;
    }

    public void setNrLargura(Integer nrLargura) {
        this.nrLargura = nrLargura;
    }

    public Integer getNrComprimento() {
        return this.nrComprimento;
    }

    public void setNrComprimento(Integer nrComprimento) {
        this.nrComprimento = nrComprimento;
    }

    public VarcharI18n getDsEmbalagem() {
		return dsEmbalagem;
    }

	public void setDsEmbalagem(VarcharI18n dsEmbalagem) {
        this.dsEmbalagem = dsEmbalagem;
    }

    public Boolean getBlPrecificada() {
        return this.blPrecificada;
    }

    public void setBlPrecificada(Boolean blPrecificada) {
        this.blPrecificada = blPrecificada;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.AwbEmbalagem.class)     
    public List getAwbEmbalagems() {
        return this.awbEmbalagems;
    }

    public void setAwbEmbalagems(List awbEmbalagems) {
        this.awbEmbalagems = awbEmbalagems;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.CtoCtoCooperada.class)     
    public List getCtoCtoCooperadas() {
        return this.ctoCtoCooperadas;
    }

    public void setCtoCtoCooperadas(List ctoCtoCooperadas) {
        this.ctoCtoCooperadas = ctoCtoCooperadas;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.expedicao.model.ServicoEmbalagem.class)     
    public List getServicoEmbalagems() {
        return this.servicoEmbalagems;
    }

    public void setServicoEmbalagems(List servicoEmbalagems) {
        this.servicoEmbalagems = servicoEmbalagems;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idEmbalagem", getIdEmbalagem()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Embalagem))
			return false;
        Embalagem castOther = (Embalagem) other;
		return new EqualsBuilder().append(this.getIdEmbalagem(),
				castOther.getIdEmbalagem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEmbalagem()).toHashCode();
    }

}
