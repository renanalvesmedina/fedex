package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/** @author LMS Custom Hibernate CodeGenerator */
public class SegmentoMercado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSegmentoMercado;

    /** persistent field */
    private VarcharI18n dsSegmentoMercado;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private List municipioFilialSegmentos;

    /** persistent field */
    private List clientes;

    private BigDecimal nrFatorCubagemReal;
    
    public Long getIdSegmentoMercado() {
        return this.idSegmentoMercado;
    }

    public void setIdSegmentoMercado(Long idSegmentoMercado) {
        this.idSegmentoMercado = idSegmentoMercado;
    }

    public VarcharI18n getDsSegmentoMercado() {
		return dsSegmentoMercado;
    }

	public void setDsSegmentoMercado(VarcharI18n dsSegmentoMercado) {
        this.dsSegmentoMercado = dsSegmentoMercado;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.municipios.model.MunicipioFilialSegmento.class)     
    public List getMunicipioFilialSegmentos() {
        return this.municipioFilialSegmentos;
    }

    public void setMunicipioFilialSegmentos(List municipioFilialSegmentos) {
        this.municipioFilialSegmentos = municipioFilialSegmentos;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.vendas.model.Cliente.class)     
    public List getClientes() {
        return this.clientes;
    }

    public void setClientes(List clientes) {
        this.clientes = clientes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSegmentoMercado",
				getIdSegmentoMercado()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SegmentoMercado))
			return false;
        SegmentoMercado castOther = (SegmentoMercado) other;
		return new EqualsBuilder().append(this.getIdSegmentoMercado(),
				castOther.getIdSegmentoMercado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSegmentoMercado())
            .toHashCode();
    }

	public BigDecimal getNrFatorCubagemReal() {
		return nrFatorCubagemReal;
}

	public void setNrFatorCubagemReal(BigDecimal nrFatorCubagemReal) {
		this.nrFatorCubagemReal = nrFatorCubagemReal;
	}

}
