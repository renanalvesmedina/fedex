package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/** @author LMS Custom Hibernate CodeGenerator */
public class CarregamentoPreManifesto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idCarregamentoPreManifesto;

    /** nullable persistent field */
    private DateTime dhInicioCarregamento;

    /** nullable persistent field */
    private DateTime dhFimCarregamento;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.EquipeOperacao equipeOperacao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;

    /** persistent field */
    private List dispCarregDescQtdes;

    /** persistent field */
    private List dispCarregIdentificados;

    public Long getIdCarregamentoPreManifesto() {
        return this.idCarregamentoPreManifesto;
    }

    public void setIdCarregamentoPreManifesto(Long idCarregamentoPreManifesto) {
        this.idCarregamentoPreManifesto = idCarregamentoPreManifesto;
    }

    public DateTime getDhInicioCarregamento() {
        return this.dhInicioCarregamento;
    }

    public void setDhInicioCarregamento(DateTime dhInicioCarregamento) {
        this.dhInicioCarregamento = dhInicioCarregamento;
    }

    public DateTime getDhFimCarregamento() {
        return this.dhFimCarregamento;
    }

    public void setDhFimCarregamento(DateTime dhFimCarregamento) {
        this.dhFimCarregamento = dhFimCarregamento;
    }

    public com.mercurio.lms.carregamento.model.EquipeOperacao getEquipeOperacao() {
        return this.equipeOperacao;
    }

	public void setEquipeOperacao(
			com.mercurio.lms.carregamento.model.EquipeOperacao equipeOperacao) {
        this.equipeOperacao = equipeOperacao;
    }

    public com.mercurio.lms.carregamento.model.Manifesto getManifesto() {
        return this.manifesto;
    }

	public void setManifesto(
			com.mercurio.lms.carregamento.model.Manifesto manifesto) {
        this.manifesto = manifesto;
    }

    public com.mercurio.lms.carregamento.model.CarregamentoDescarga getCarregamentoDescarga() {
        return this.carregamentoDescarga;
    }

	public void setCarregamentoDescarga(
			com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga) {
        this.carregamentoDescarga = carregamentoDescarga;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispCarregDescQtde.class)     
    public List getDispCarregDescQtdes() {
        return this.dispCarregDescQtdes;
    }

    public void setDispCarregDescQtdes(List dispCarregDescQtdes) {
        this.dispCarregDescQtdes = dispCarregDescQtdes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispCarregIdentificado.class)     
    public List getDispCarregIdentificados() {
        return this.dispCarregIdentificados;
    }

    public void setDispCarregIdentificados(List dispCarregIdentificados) {
        this.dispCarregIdentificados = dispCarregIdentificados;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCarregamentoPreManifesto",
				getIdCarregamentoPreManifesto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CarregamentoPreManifesto))
			return false;
        CarregamentoPreManifesto castOther = (CarregamentoPreManifesto) other;
		return new EqualsBuilder().append(this.getIdCarregamentoPreManifesto(),
				castOther.getIdCarregamentoPreManifesto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCarregamentoPreManifesto())
            .toHashCode();
    }

}
