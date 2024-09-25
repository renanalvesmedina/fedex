package com.mercurio.lms.expedicao.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.configuracoes.model.InscricaoEstadual;

/** @author LMS Custom Hibernate CodeGenerator */
public class DevedorDocServ implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idDevedorDocServ;

    /** nullable persistent field */
    private BigDecimal vlDevido;

    /** persistent field */
    private com.mercurio.lms.vendas.model.Cliente cliente;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private InscricaoEstadual inscricaoEstadual;

    public Long getIdDevedorDocServ() {
        return this.idDevedorDocServ;
    }

    public void setIdDevedorDocServ(Long idDevedorDocServ) {
        this.idDevedorDocServ = idDevedorDocServ;
    }

    public BigDecimal getVlDevido() {
        return this.vlDevido;
    }

    public void setVlDevido(BigDecimal vlDevido) {
        this.vlDevido = vlDevido;
    }

    public com.mercurio.lms.vendas.model.Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(com.mercurio.lms.vendas.model.Cliente cliente) {
        this.cliente = cliente;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idDevedorDocServ",
				getIdDevedorDocServ()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof DevedorDocServ))
			return false;
        DevedorDocServ castOther = (DevedorDocServ) other;
		return new EqualsBuilder().append(this.getIdDevedorDocServ(),
				castOther.getIdDevedorDocServ()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdDevedorDocServ()).toHashCode();
    }

	/**
	 * @return the inscricaoEstadual
	 */
	public InscricaoEstadual getInscricaoEstadual() {
		return inscricaoEstadual;
}

	/**
	 * @param inscricaoEstadual
	 *            the inscricaoEstadual to set
	 */
	public void setInscricaoEstadual(InscricaoEstadual inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

}
