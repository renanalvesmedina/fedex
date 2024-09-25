package com.mercurio.lms.indenizacoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class MdaSalvadoIndenizacao implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMdaSalvadoIndenizacao;

    /** persistent field */
    private com.mercurio.lms.pendencia.model.Mda mda;

    /** persistent field */
    private com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao;
    
    /** persistent field */
    private Integer versao;
 
    public Long getIdMdaSalvadoIndenizacao() {
        return this.idMdaSalvadoIndenizacao;
    }

    public void setIdMdaSalvadoIndenizacao(Long idMdaSalvadoIndenizacao) {
        this.idMdaSalvadoIndenizacao = idMdaSalvadoIndenizacao;
    }

    public com.mercurio.lms.pendencia.model.Mda getMda() {
        return this.mda;
    }

    public void setMda(com.mercurio.lms.pendencia.model.Mda mda) {
        this.mda = mda;
    }

    public com.mercurio.lms.indenizacoes.model.ReciboIndenizacao getReciboIndenizacao() {
        return this.reciboIndenizacao;
    }

	public void setReciboIndenizacao(
			com.mercurio.lms.indenizacoes.model.ReciboIndenizacao reciboIndenizacao) {
        this.reciboIndenizacao = reciboIndenizacao;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMdaSalvadoIndenizacao",
				getIdMdaSalvadoIndenizacao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof MdaSalvadoIndenizacao))
			return false;
        MdaSalvadoIndenizacao castOther = (MdaSalvadoIndenizacao) other;
		return new EqualsBuilder().append(this.getIdMdaSalvadoIndenizacao(),
				castOther.getIdMdaSalvadoIndenizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMdaSalvadoIndenizacao())
            .toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

}
