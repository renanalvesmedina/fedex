package com.mercurio.lms.vendas.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class GeneralidadeCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idGeneralidadeCliente;

    /** persistent field */
    private DomainValue tpIndicador;

    /** persistent field */
    private BigDecimal vlGeneralidade;

    /** nullable persistent field */
    private BigDecimal pcReajGeneralidade;

    /** persistent field */
    private com.mercurio.lms.vendas.model.ParametroCliente parametroCliente;

    /** persistent field */
    private com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco;

    private String dsSimbolo;
    
    private String valorIndicador;
    
    private DomainValue tpIndicadorMinimo;
    
    private BigDecimal vlMinimo;
    
    private BigDecimal pcReajMinimo;
    
    private Boolean blAlterou = false;

    /**
	 * @return Returns the valorIndicador.
	 */
	public String getValorIndicador() {
		return valorIndicador;
	}

	/**
	 * @param valorIndicador
	 *            The valorIndicador to set.
	 */
	public void setValorIndicador(String valorIndicador) {
		this.valorIndicador = valorIndicador;
	}

    public Long getIdGeneralidadeCliente() {
        return this.idGeneralidadeCliente;
    }

    public void setIdGeneralidadeCliente(Long idGeneralidadeCliente) {
        this.idGeneralidadeCliente = idGeneralidadeCliente;
    }

    public DomainValue getTpIndicador() {
        return this.tpIndicador;
    }

    public void setTpIndicador(DomainValue tpIndicador) {
        this.tpIndicador = tpIndicador;
    }

    public BigDecimal getVlGeneralidade() {
        return this.vlGeneralidade;
    }

    public void setVlGeneralidade(BigDecimal vlGeneralidade) {
        this.vlGeneralidade = vlGeneralidade;
    }

    public BigDecimal getPcReajGeneralidade() {
        return this.pcReajGeneralidade;
    }

    public void setPcReajGeneralidade(BigDecimal pcReajGeneralidade) {
        this.pcReajGeneralidade = pcReajGeneralidade;
    }

    public com.mercurio.lms.vendas.model.ParametroCliente getParametroCliente() {
        return this.parametroCliente;
    }

	public void setParametroCliente(
			com.mercurio.lms.vendas.model.ParametroCliente parametroCliente) {
        this.parametroCliente = parametroCliente;
    }

    public com.mercurio.lms.tabelaprecos.model.ParcelaPreco getParcelaPreco() {
        return this.parcelaPreco;
    }

	public void setParcelaPreco(
			com.mercurio.lms.tabelaprecos.model.ParcelaPreco parcelaPreco) {
        this.parcelaPreco = parcelaPreco;
    }

    public DomainValue getTpIndicadorMinimo() {
		return tpIndicadorMinimo;
	}

	public void setTpIndicadorMinimo(DomainValue tpIndicadorMinimo) {
		this.tpIndicadorMinimo = tpIndicadorMinimo;
	}

	public BigDecimal getVlMinimo() {
		return vlMinimo;
	}

	public void setVlMinimo(BigDecimal vlMinimo) {
		this.vlMinimo = vlMinimo;
	}

	public BigDecimal getPcReajMinimo() {
		return pcReajMinimo;
	}

	public void setPcReajMinimo(BigDecimal pcReajusteGeneralidade) {
		this.pcReajMinimo = pcReajusteGeneralidade;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idGeneralidadeCliente",
				getIdGeneralidadeCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof GeneralidadeCliente))
			return false;
        GeneralidadeCliente castOther = (GeneralidadeCliente) other;
		return new EqualsBuilder().append(this.getIdGeneralidadeCliente(),
				castOther.getIdGeneralidadeCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdGeneralidadeCliente())
            .toHashCode();
    }

	/**
	 * @return Returns the dsSimbolo.
	 */
	public String getDsSimbolo() {
		return dsSimbolo;
	}

	/**
	 * @param dsSimbolo
	 *            The dsSimbolo to set.
	 */
	public void setDsSimbolo(String dsSimbolo) {
		this.dsSimbolo = dsSimbolo;
	}

	public Boolean getBlAlterou() {
		return blAlterou;
}

	public void setBlAlterou(Boolean blAlterou) {
		this.blAlterou = blAlterou;
	}

}
