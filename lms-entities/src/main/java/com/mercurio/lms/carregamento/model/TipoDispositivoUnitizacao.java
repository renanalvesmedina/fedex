package com.mercurio.lms.carregamento.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class TipoDispositivoUnitizacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idTipoDispositivoUnitizacao;

    /** persistent field */
    private VarcharI18n dsTipoDispositivoUnitizacao;

    /** persistent field */
    private DomainValue tpControleDispositivo;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List dispositivoUnitizacoes;

    /** persistent field */
    private List dispCarregDescQtdes;

    /** persistent field */
    private List estoqueDispositivoQtdes;

    public Long getIdTipoDispositivoUnitizacao() {
        return this.idTipoDispositivoUnitizacao;
    }

    public void setIdTipoDispositivoUnitizacao(Long idTipoDispositivoUnitizacao) {
        this.idTipoDispositivoUnitizacao = idTipoDispositivoUnitizacao;
    }

    public VarcharI18n getDsTipoDispositivoUnitizacao() {
		return dsTipoDispositivoUnitizacao;
    }

	public void setDsTipoDispositivoUnitizacao(
			VarcharI18n dsTipoDispositivoUnitizacao) {
        this.dsTipoDispositivoUnitizacao = dsTipoDispositivoUnitizacao;
    }

    public DomainValue getTpControleDispositivo() {
        return this.tpControleDispositivo;
    }

    public void setTpControleDispositivo(DomainValue tpControleDispositivo) {
        this.tpControleDispositivo = tpControleDispositivo;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispositivoUnitizacao.class)     
    public List getDispositivoUnitizacoes() {
        return this.dispositivoUnitizacoes;
    }

    public void setDispositivoUnitizacoes(List dispositivoUnitizacoes) {
        this.dispositivoUnitizacoes = dispositivoUnitizacoes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.DispCarregDescQtde.class)     
    public List getDispCarregDescQtdes() {
        return this.dispCarregDescQtdes;
    }

    public void setDispCarregDescQtdes(List dispCarregDescQtdes) {
        this.dispCarregDescQtdes = dispCarregDescQtdes;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.carregamento.model.EstoqueDispositivoQtde.class)     
    public List getEstoqueDispositivoQtdes() {
        return this.estoqueDispositivoQtdes;
    }

    public void setEstoqueDispositivoQtdes(List estoqueDispositivoQtdes) {
        this.estoqueDispositivoQtdes = estoqueDispositivoQtdes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idTipoDispositivoUnitizacao",
				getIdTipoDispositivoUnitizacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof TipoDispositivoUnitizacao))
			return false;
        TipoDispositivoUnitizacao castOther = (TipoDispositivoUnitizacao) other;
		return new EqualsBuilder().append(
				this.getIdTipoDispositivoUnitizacao(),
				castOther.getIdTipoDispositivoUnitizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdTipoDispositivoUnitizacao())
            .toHashCode();
    }

}
