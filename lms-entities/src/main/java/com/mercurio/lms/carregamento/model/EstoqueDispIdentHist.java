package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

/** @author LMS Custom Hibernate CodeGenerator */
public class EstoqueDispIdentHist implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEstoqueDispIdentHist;

    /** persistent field */
    private YearMonthDay dtReferencia;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

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
		return new ToStringBuilder(this).append("idEstoqueDispIdentHist",
				getIdEstoqueDispIdentHist()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EstoqueDispIdentificado))
			return false;
        EstoqueDispIdentificado castOther = (EstoqueDispIdentificado) other;
		return new EqualsBuilder().append(this.getIdEstoqueDispIdentHist(),
				castOther.getIdEstoqueDispIdentificado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEstoqueDispIdentHist())
            .toHashCode();
    }

	public YearMonthDay getDtReferencia() {
		return dtReferencia;
	}

	public void setDtReferencia(YearMonthDay dtReferencia) {
		this.dtReferencia = dtReferencia;
	}

	public Long getIdEstoqueDispIdentHist() {
		return idEstoqueDispIdentHist;
	}

	public void setIdEstoqueDispIdentHist(Long idEstoqueDispIdentHist) {
		this.idEstoqueDispIdentHist = idEstoqueDispIdentHist;
	}

}
