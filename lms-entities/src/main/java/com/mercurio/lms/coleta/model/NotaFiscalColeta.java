package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class NotaFiscalColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idNotaFiscalColeta;

    /** persistent field */
    private Integer nrNotaFiscal;

    private String nrChave;

    /** persistent field */
    private com.mercurio.lms.coleta.model.DetalheColeta detalheColeta;

    public Long getIdNotaFiscalColeta() {
        return this.idNotaFiscalColeta;
    }

    public void setIdNotaFiscalColeta(Long idNotaFiscalColeta) {
        this.idNotaFiscalColeta = idNotaFiscalColeta;
    }

    public Integer getNrNotaFiscal() {
        return this.nrNotaFiscal;
    }

    public void setNrNotaFiscal(Integer nrNotaFiscal) {
        this.nrNotaFiscal = nrNotaFiscal;
    }

    public com.mercurio.lms.coleta.model.DetalheColeta getDetalheColeta() {
        return this.detalheColeta;
    }

	public void setDetalheColeta(
			com.mercurio.lms.coleta.model.DetalheColeta detalheColeta) {
        this.detalheColeta = detalheColeta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idNotaFiscalColeta",
				getIdNotaFiscalColeta()).toString();
    }

    public String getNrChave() {
		return nrChave;
	}

	public void setNrChave(String nrChave) {
		this.nrChave = nrChave;
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof NotaFiscalColeta))
			return false;
        NotaFiscalColeta castOther = (NotaFiscalColeta) other;
		return new EqualsBuilder().append(this.getIdNotaFiscalColeta(),
				castOther.getIdNotaFiscalColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdNotaFiscalColeta())
            .toHashCode();
    }

}
