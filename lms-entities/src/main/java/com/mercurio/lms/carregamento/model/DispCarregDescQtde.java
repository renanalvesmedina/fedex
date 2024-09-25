package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class DispCarregDescQtde implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDispCarregDescQtde;
    
    /** identifier field */
    private Integer versao;

    /** persistent field */
    private Integer qtDispositivo;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoPreManifesto carregamentoPreManifesto;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao tipoDispositivoUnitizacao;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Empresa empresa;

    public Long getIdDispCarregDescQtde() {
        return this.idDispCarregDescQtde;
    }

    public void setIdDispCarregDescQtde(Long idDispCarregDescQtde) {
        this.idDispCarregDescQtde = idDispCarregDescQtde;
    }

    public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public Integer getQtDispositivo() {
        return this.qtDispositivo;
    }

    public void setQtDispositivo(Integer qtDispositivo) {
        this.qtDispositivo = qtDispositivo;
    }

    public com.mercurio.lms.carregamento.model.CarregamentoPreManifesto getCarregamentoPreManifesto() {
        return this.carregamentoPreManifesto;
    }

	public void setCarregamentoPreManifesto(
			com.mercurio.lms.carregamento.model.CarregamentoPreManifesto carregamentoPreManifesto) {
        this.carregamentoPreManifesto = carregamentoPreManifesto;
    }

    public com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao getTipoDispositivoUnitizacao() {
        return this.tipoDispositivoUnitizacao;
    }

	public void setTipoDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao tipoDispositivoUnitizacao) {
        this.tipoDispositivoUnitizacao = tipoDispositivoUnitizacao;
    }

    public com.mercurio.lms.carregamento.model.CarregamentoDescarga getCarregamentoDescarga() {
        return this.carregamentoDescarga;
    }

	public void setCarregamentoDescarga(
			com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga) {
        this.carregamentoDescarga = carregamentoDescarga;
    }

    public com.mercurio.lms.municipios.model.Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(com.mercurio.lms.municipios.model.Empresa empresa) {
		this.empresa = empresa;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idDispCarregDescQtde",
				getIdDispCarregDescQtde()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DispCarregDescQtde))
			return false;
        DispCarregDescQtde castOther = (DispCarregDescQtde) other;
		return new EqualsBuilder().append(this.getIdDispCarregDescQtde(),
				castOther.getIdDispCarregDescQtde()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDispCarregDescQtde())
            .toHashCode();
    }

}
