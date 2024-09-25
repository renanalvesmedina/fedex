package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.TimeOfDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ParcelaTabelaCe implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idParcelaTabelaCe;

    /** persistent field */
    private DomainValue tpParcela;
 
    /** nullable persistent field */
    private TimeOfDay hrInicial;

    /** nullable persistent field */
    private TimeOfDay hrFinal;

    /** nullable persistent field */
    private BigDecimal vlSugerido;

    /** nullable persistent field */
    private BigDecimal vlMaximoAprovado;

    /** nullable persistent field */
    private BigDecimal vlNegociado;

    /** nullable persistent field */
    private BigDecimal vlDefinido;

    /** nullable persistent field */
    private BigDecimal vlReferencia;
    
    /** nullable persistent field */
    private BigDecimal pcSobreValor;

    /** persistent field */
    private com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega tabelaColetaEntrega;

    /** persistent field */
    private List notaCreditoParcelas;

    public Long getIdParcelaTabelaCe() {
        return this.idParcelaTabelaCe;
    }

    public void setIdParcelaTabelaCe(Long idParcelaTabelaCe) {
        this.idParcelaTabelaCe = idParcelaTabelaCe;
    }

    public DomainValue getTpParcela() {
        return this.tpParcela;
    }

    public void setTpParcela(DomainValue tpParcela) {
        this.tpParcela = tpParcela;
    }

    public TimeOfDay getHrInicial() {
        return this.hrInicial;
    }

    public void setHrInicial(TimeOfDay hrInicial) {
        this.hrInicial = hrInicial;
    }

    public TimeOfDay getHrFinal() {
        return this.hrFinal;
    }

    public void setHrFinal(TimeOfDay hrFinal) {
        this.hrFinal = hrFinal;
    }

    public BigDecimal getVlSugerido() {
        return this.vlSugerido;
    }

    public void setVlSugerido(BigDecimal vlSugerido) {
        this.vlSugerido = vlSugerido;
    }

    public BigDecimal getVlMaximoAprovado() {
        return this.vlMaximoAprovado;
    }

    public void setVlMaximoAprovado(BigDecimal vlMaximoAprovado) {
        this.vlMaximoAprovado = vlMaximoAprovado;
    }

    public BigDecimal getVlNegociado() {
        return this.vlNegociado;
    }

    public void setVlNegociado(BigDecimal vlNegociado) {
        this.vlNegociado = vlNegociado;
    }

    public com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega getTabelaColetaEntrega() {
        return this.tabelaColetaEntrega;
    }

	public void setTabelaColetaEntrega(
			com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega tabelaColetaEntrega) {
        this.tabelaColetaEntrega = tabelaColetaEntrega;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoParcela.class)     
    public List getNotaCreditoParcelas() {
        return this.notaCreditoParcelas;
    }

    public void setNotaCreditoParcelas(List notaCreditoParcelas) {
        this.notaCreditoParcelas = notaCreditoParcelas;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idParcelaTabelaCe",
				getIdParcelaTabelaCe()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ParcelaTabelaCe))
			return false;
        ParcelaTabelaCe castOther = (ParcelaTabelaCe) other;
		return new EqualsBuilder().append(this.getIdParcelaTabelaCe(),
				castOther.getIdParcelaTabelaCe()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdParcelaTabelaCe())
            .toHashCode();
    }

	public BigDecimal getVlDefinido() {
		return vlDefinido;
	}

	public void setVlDefinido(BigDecimal vlDefinido) {
		this.vlDefinido = vlDefinido;
	}

	public BigDecimal getVlReferencia() {
		return vlReferencia;
	}

	public void setVlReferencia(BigDecimal vlReferencia) {
		this.vlReferencia = vlReferencia;
	}

	public BigDecimal getPcSobreValor() {
		return pcSobreValor;
	}

	public void setPcSobreValor(BigDecimal pcSobreValor) {
		this.pcSobreValor = pcSobreValor;
	}

}
