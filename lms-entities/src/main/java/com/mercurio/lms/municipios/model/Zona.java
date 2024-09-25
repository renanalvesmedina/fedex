package com.mercurio.lms.municipios.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author Hibernate CodeGenerator */
public class Zona implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idZona;

    /** persistent field */
    private VarcharI18n  dsZona;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List rotaPrecosByIdZonaDestino;

    /** persistent field */
    private List rotaPrecosByIdZonaOrigem;

    /** persistent field */
    private List zonaServicos;

    /** persistent field */
    private List pais;

    /** persistent field */
    private List parametroClientesByIdZonaDestino;

    /** persistent field */
    private List parametroClientesByIdZonaOrigem;

	public Long getIdZona() {
        return this.idZona;
    }

    public void setIdZona(Long idZona) {
        this.idZona = idZona;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.RotaPreco.class)     
    public List getRotaPrecosByIdZonaDestino() {
        return this.rotaPrecosByIdZonaDestino;
    }

    public void setRotaPrecosByIdZonaDestino(List rotaPrecosByIdZonaDestino) {
        this.rotaPrecosByIdZonaDestino = rotaPrecosByIdZonaDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.tabelaprecos.model.RotaPreco.class)     
    public List getRotaPrecosByIdZonaOrigem() {
        return this.rotaPrecosByIdZonaOrigem;
    }

    public void setRotaPrecosByIdZonaOrigem(List rotaPrecosByIdZonaOrigem) {
        this.rotaPrecosByIdZonaOrigem = rotaPrecosByIdZonaOrigem;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.ZonaServico.class)     
    public List getZonaServicos() {
        return this.zonaServicos;
    }

    public void setZonaServicos(List zonaServicos) {
        this.zonaServicos = zonaServicos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.Pais.class)     
    public List getPais() {
        return this.pais;
    }

    public void setPais(List pais) {
        this.pais = pais;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class)     
    public List getParametroClientesByIdZonaDestino() {
        return this.parametroClientesByIdZonaDestino;
    }

	public void setParametroClientesByIdZonaDestino(
			List parametroClientesByIdZonaDestino) {
        this.parametroClientesByIdZonaDestino = parametroClientesByIdZonaDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.ParametroCliente.class)     
    public List getParametroClientesByIdZonaOrigem() {
        return this.parametroClientesByIdZonaOrigem;
    }

	public void setParametroClientesByIdZonaOrigem(
			List parametroClientesByIdZonaOrigem) {
        this.parametroClientesByIdZonaOrigem = parametroClientesByIdZonaOrigem;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idZona", getIdZona())
            .toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Zona))
			return false;
        Zona castOther = (Zona) other;
		return new EqualsBuilder().append(this.getIdZona(),
				castOther.getIdZona()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdZona()).toHashCode();
    }

	public VarcharI18n getDsZona() {
		return dsZona;
	}

	public void setDsZona(VarcharI18n dsZona) {
		this.dsZona = dsZona;
	}

}
