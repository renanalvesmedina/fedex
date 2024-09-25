package com.mercurio.lms.seguros.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ReciboReembolsoDoctoServico implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idReciboReembolsoDoctoServ;

    /** persistent field */
    private BigDecimal vlReembolso;

    /** persistent field */
    private ReciboReembolsoProcesso reciboReembolsoProcesso;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Moeda moeda;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;
    
    /** persistent field */
    private Integer versao;

    public Long getIdReciboReembolsoDoctoServ() {
        return this.idReciboReembolsoDoctoServ;
    }

    public void setIdReciboReembolsoDoctoServ(Long idReciboReembolsoDoctoServ) {
        this.idReciboReembolsoDoctoServ = idReciboReembolsoDoctoServ;
    }

    public BigDecimal getVlReembolso() {
        return this.vlReembolso;
    }

    public void setVlReembolso(BigDecimal vlReembolso) {
        this.vlReembolso = vlReembolso;
    }

    public com.mercurio.lms.seguros.model.ReciboReembolsoProcesso getReciboReembolsoProcesso() {
        return this.reciboReembolsoProcesso;
    }

	public void setReciboReembolsoProcesso(
			com.mercurio.lms.seguros.model.ReciboReembolsoProcesso reciboReembolsoProcesso) {
        this.reciboReembolsoProcesso = reciboReembolsoProcesso;
    }

    public com.mercurio.lms.configuracoes.model.Moeda getMoeda() {
        return this.moeda;
    }

    public void setMoeda(com.mercurio.lms.configuracoes.model.Moeda moeda) {
        this.moeda = moeda;
    }

    public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
        return this.doctoServico;
    }

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
        this.doctoServico = doctoServico;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idReciboReembolsoDoctoServ",
				getIdReciboReembolsoDoctoServ()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ReciboReembolsoDoctoServico))
			return false;
        ReciboReembolsoDoctoServico castOther = (ReciboReembolsoDoctoServico) other;
		return new EqualsBuilder().append(this.getIdReciboReembolsoDoctoServ(),
				castOther.getIdReciboReembolsoDoctoServ()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdReciboReembolsoDoctoServ())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
