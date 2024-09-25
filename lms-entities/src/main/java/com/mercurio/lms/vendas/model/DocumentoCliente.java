package com.mercurio.lms.vendas.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class DocumentoCliente implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDocumentoCliente;

    /** persistent field */
    private DomainValue tpModal;

    /** persistent field */
    private DomainValue tpAbrangencia;

    /** persistent field */
    private Boolean blFaturaVinculada;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaFinal;

    /** nullable persistent field */
    private YearMonthDay dtVigenciaInicial;

    /** persistent field */
    private com.mercurio.lms.entrega.model.TipoDocumentoEntrega tipoDocumentoEntrega;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    public Long getIdDocumentoCliente() {
        return this.idDocumentoCliente;
    }

    public void setIdDocumentoCliente(Long idDocumentoCliente) {
        this.idDocumentoCliente = idDocumentoCliente;
    }

    public DomainValue getTpModal() {
        return this.tpModal;
    }

    public void setTpModal(DomainValue tpModal) {
        this.tpModal = tpModal;
    }

    public DomainValue getTpAbrangencia() {
        return this.tpAbrangencia;
    }

    public void setTpAbrangencia(DomainValue tpAbrangencia) {
        this.tpAbrangencia = tpAbrangencia;
    }

    public Boolean getBlFaturaVinculada() {
        return this.blFaturaVinculada;
    }

    public void setBlFaturaVinculada(Boolean blFaturaVinculada) {
        this.blFaturaVinculada = blFaturaVinculada;
    }

    public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public com.mercurio.lms.entrega.model.TipoDocumentoEntrega getTipoDocumentoEntrega() {
        return this.tipoDocumentoEntrega;
    }

	public void setTipoDocumentoEntrega(
			com.mercurio.lms.entrega.model.TipoDocumentoEntrega tipoDocumentoEntrega) {
        this.tipoDocumentoEntrega = tipoDocumentoEntrega;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDocumentoCliente",
				getIdDocumentoCliente()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DocumentoCliente))
			return false;
        DocumentoCliente castOther = (DocumentoCliente) other;
		return new EqualsBuilder().append(this.getIdDocumentoCliente(),
				castOther.getIdDocumentoCliente()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDocumentoCliente())
            .toHashCode();
    }

}
