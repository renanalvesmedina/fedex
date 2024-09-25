package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class CartaCorrecao implements Serializable {
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Long idCartaCorrecao;

    /** persistent field */
    private DomainValue nrCampo;

    /** persistent field */
    private String dsConteudoAtual;

    /** persistent field */
    private String dsConteudoAlterado;

    /** nullable persistent field */
    private YearMonthDay dtEmissao;

    /** nullable persistent field */
    private String nmDestinatario;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional;

    public Long getIdCartaCorrecao() {
        return this.idCartaCorrecao;
    }

    public void setIdCartaCorrecao(Long idCartaCorrecao) {
        this.idCartaCorrecao = idCartaCorrecao;
    }

    public DomainValue getNrCampo() {
        return this.nrCampo;
    }

    public void setNrCampo(DomainValue nrCampo) {
        this.nrCampo = nrCampo;
    }

    public String getDsConteudoAtual() {
        return this.dsConteudoAtual;
    }

    public void setDsConteudoAtual(String dsConteudoAtual) {
        this.dsConteudoAtual = dsConteudoAtual;
    }

    public String getDsConteudoAlterado() {
        return this.dsConteudoAlterado;
    }

    public void setDsConteudoAlterado(String dsConteudoAlterado) {
        this.dsConteudoAlterado = dsConteudoAlterado;
    }

    public YearMonthDay getDtEmissao() {
        return this.dtEmissao;
    }

    public void setDtEmissao(YearMonthDay dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getNmDestinatario() {
        return this.nmDestinatario;
    }

    public void setNmDestinatario(String nmDestinatario) {
        this.nmDestinatario = nmDestinatario;
    }

    public com.mercurio.lms.expedicao.model.CtoInternacional getCtoInternacional() {
        return this.ctoInternacional;
    }

	public void setCtoInternacional(
			com.mercurio.lms.expedicao.model.CtoInternacional ctoInternacional) {
        this.ctoInternacional = ctoInternacional;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idCartaCorrecao",
				getIdCartaCorrecao()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof CartaCorrecao))
			return false;
        CartaCorrecao castOther = (CartaCorrecao) other;
		return new EqualsBuilder().append(this.getIdCartaCorrecao(),
				castOther.getIdCartaCorrecao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdCartaCorrecao()).toHashCode();
    }

}
