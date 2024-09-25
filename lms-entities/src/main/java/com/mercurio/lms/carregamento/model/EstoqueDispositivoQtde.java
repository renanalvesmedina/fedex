package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;

/** @author LMS Custom Hibernate CodeGenerator */
public class EstoqueDispositivoQtde implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEstoqueDispositivoQtde;

    /** persistent field */
    private Integer qtEstoque;

    /** persistent field */
    private ControleCarga controleCarga;

    /** persistent field */
    private TipoDispositivoUnitizacao tipoDispositivoUnitizacao;

    /** persistent field */
    private Filial filial;
    
    /** persistent field */
    private Empresa empresa;

    public Long getIdEstoqueDispositivoQtde() {
        return this.idEstoqueDispositivoQtde;
    }

    public void setIdEstoqueDispositivoQtde(Long idEstoqueDispositivoQtde) {
        this.idEstoqueDispositivoQtde = idEstoqueDispositivoQtde;
    }

    public Integer getQtEstoque() {
        return this.qtEstoque;
    }

    public void setQtEstoque(Integer qtEstoque) {
        this.qtEstoque = qtEstoque;
    }

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao getTipoDispositivoUnitizacao() {
        return this.tipoDispositivoUnitizacao;
    }

	public void setTipoDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao tipoDispositivoUnitizacao) {
        this.tipoDispositivoUnitizacao = tipoDispositivoUnitizacao;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idEstoqueDispositivoQtde",
				getIdEstoqueDispositivoQtde()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EstoqueDispositivoQtde))
			return false;
        EstoqueDispositivoQtde castOther = (EstoqueDispositivoQtde) other;
		return new EqualsBuilder().append(this.getIdEstoqueDispositivoQtde(),
				castOther.getIdEstoqueDispositivoQtde()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEstoqueDispositivoQtde())
            .toHashCode();
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

}
