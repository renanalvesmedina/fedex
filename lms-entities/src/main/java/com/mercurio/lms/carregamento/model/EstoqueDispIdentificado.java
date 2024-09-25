package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class EstoqueDispIdentificado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEstoqueDispIdentificado;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    public Long getIdEstoqueDispIdentificado() {
        return this.idEstoqueDispIdentificado;
    }

    public void setIdEstoqueDispIdentificado(Long idEstoqueDispIdentificado) {
        this.idEstoqueDispIdentificado = idEstoqueDispIdentificado;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.carregamento.model.DispositivoUnitizacao getDispositivoUnitizacao() {
        return this.dispositivoUnitizacao;
    }

	public void setDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao) {
        this.dispositivoUnitizacao = dispositivoUnitizacao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEstoqueDispIdentificado",
				getIdEstoqueDispIdentificado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EstoqueDispIdentificado))
			return false;
        EstoqueDispIdentificado castOther = (EstoqueDispIdentificado) other;
		return new EqualsBuilder().append(this.getIdEstoqueDispIdentificado(),
				castOther.getIdEstoqueDispIdentificado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEstoqueDispIdentificado())
            .toHashCode();
    }

}
