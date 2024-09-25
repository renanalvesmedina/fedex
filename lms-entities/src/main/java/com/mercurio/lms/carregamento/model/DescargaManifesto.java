package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class DescargaManifesto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDescargaManifesto;

    /** nullable persistent field */
    private DateTime dhInicioDescarga;

    /** nullable persistent field */
    private DateTime dhFimDescarga;

    /** nullable persistent field */
    private DateTime dhCancelamentoDescarga;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.EquipeOperacao equipeOperacao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.Manifesto manifesto;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;

    /** nullable persistent field */
    private com.mercurio.lms.coleta.model.ManifestoColeta manifestoColeta;

    public Long getIdDescargaManifesto() {
        return this.idDescargaManifesto;
    }

    public void setIdDescargaManifesto(Long idDescargaManifesto) {
        this.idDescargaManifesto = idDescargaManifesto;
    }

    public DateTime getDhInicioDescarga() {
        return this.dhInicioDescarga;
    }

    public void setDhInicioDescarga(DateTime dhInicioDescarga) {
        this.dhInicioDescarga = dhInicioDescarga;
    }

    public DateTime getDhFimDescarga() {
        return this.dhFimDescarga;
    }

    public void setDhFimDescarga(DateTime dhFimDescarga) {
        this.dhFimDescarga = dhFimDescarga;
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

    public com.mercurio.lms.coleta.model.ManifestoColeta getManifestoColeta() {
        return this.manifestoColeta;
    }

	public void setManifestoColeta(
			com.mercurio.lms.coleta.model.ManifestoColeta manifestoColeta) {
        this.manifestoColeta = manifestoColeta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDescargaManifesto",
				getIdDescargaManifesto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DescargaManifesto))
			return false;
        DescargaManifesto castOther = (DescargaManifesto) other;
		return new EqualsBuilder().append(this.getIdDescargaManifesto(),
				castOther.getIdDescargaManifesto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDescargaManifesto())
            .toHashCode();
    }

    public DateTime getDhCancelamentoDescarga() {
        return dhCancelamentoDescarga;
    }

    public void setDhCancelamentoDescarga(DateTime dhCancelamentoDescarga) {
        this.dhCancelamentoDescarga = dhCancelamentoDescarga;
    }

}
