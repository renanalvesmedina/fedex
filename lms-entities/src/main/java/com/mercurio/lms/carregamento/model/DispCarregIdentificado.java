package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DispCarregIdentificado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDispCarregIdentificado;

    /** identifier field */
    private Integer versao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoPreManifesto carregamentoPreManifesto;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;

    public Long getIdDispCarregIdentificado() {
        return this.idDispCarregIdentificado;
    }

    public void setIdDispCarregIdentificado(Long idDispCarregIdentificado) {
        this.idDispCarregIdentificado = idDispCarregIdentificado;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public com.mercurio.lms.carregamento.model.CarregamentoPreManifesto getCarregamentoPreManifesto() {
        return this.carregamentoPreManifesto;
    }

	public void setCarregamentoPreManifesto(
			com.mercurio.lms.carregamento.model.CarregamentoPreManifesto carregamentoPreManifesto) {
        this.carregamentoPreManifesto = carregamentoPreManifesto;
    }

    public com.mercurio.lms.carregamento.model.DispositivoUnitizacao getDispositivoUnitizacao() {
        return this.dispositivoUnitizacao;
    }

	public void setDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao) {
        this.dispositivoUnitizacao = dispositivoUnitizacao;
    }

    public com.mercurio.lms.carregamento.model.CarregamentoDescarga getCarregamentoDescarga() {
        return this.carregamentoDescarga;
    }

	public void setCarregamentoDescarga(
			com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga) {
        this.carregamentoDescarga = carregamentoDescarga;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDispCarregIdentificado",
				getIdDispCarregIdentificado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DispCarregIdentificado))
			return false;
        DispCarregIdentificado castOther = (DispCarregIdentificado) other;
		return new EqualsBuilder().append(this.getIdDispCarregIdentificado(),
				castOther.getIdDispCarregIdentificado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDispCarregIdentificado())
            .toHashCode();
    }

}
