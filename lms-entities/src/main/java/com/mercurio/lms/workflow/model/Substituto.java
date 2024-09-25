package com.mercurio.lms.workflow.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class Substituto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSubstituto;

    /** persistent field */
    private YearMonthDay dtSubstituicaoInicial;

    /** persistent field */
    private YearMonthDay dtSubstituicaoFinal;

    /** persistent field */
    private Usuario usuarioByIdUsuarioSubstituido;

    /** persistent field */
    private Usuario usuarioByIdUsuarioSubstituto;
    
    /** persistent field */
    private Perfil perfilSubstituido;

    public Long getIdSubstituto() {
        return this.idSubstituto;
    }

    public void setIdSubstituto(Long idSubstituto) {
        this.idSubstituto = idSubstituto;
    }

    public YearMonthDay getDtSubstituicaoInicial() {
        return this.dtSubstituicaoInicial;
    }

    public void setDtSubstituicaoInicial(YearMonthDay dtSubstituicaoInicial) {
        this.dtSubstituicaoInicial = dtSubstituicaoInicial;
    }

    public YearMonthDay getDtSubstituicaoFinal() {
        return this.dtSubstituicaoFinal;
    }

    public void setDtSubstituicaoFinal(YearMonthDay dtSubstituicaoFinal) {
        this.dtSubstituicaoFinal = dtSubstituicaoFinal;
    }

    public Usuario getUsuarioByIdUsuarioSubstituido() {
        return this.usuarioByIdUsuarioSubstituido;
    }

	public void setUsuarioByIdUsuarioSubstituido(
			Usuario usuarioByIdUsuarioSubstituido) {
        this.usuarioByIdUsuarioSubstituido = usuarioByIdUsuarioSubstituido;
    }

    public Usuario getUsuarioByIdUsuarioSubstituto() {
        return this.usuarioByIdUsuarioSubstituto;
    }

	public void setUsuarioByIdUsuarioSubstituto(
			Usuario usuarioByIdUsuarioSubstituto) {
        this.usuarioByIdUsuarioSubstituto = usuarioByIdUsuarioSubstituto;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSubstituto",
				getIdSubstituto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Substituto))
			return false;
        Substituto castOther = (Substituto) other;
		return new EqualsBuilder().append(this.getIdSubstituto(),
				castOther.getIdSubstituto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSubstituto()).toHashCode();
    }

    /**
     * @return Returns the perfil.
     */
    public Perfil getPerfilSubstituido() {
        return perfilSubstituido;
    }

    /**
	 * @param perfil
	 *            The perfil to set.
     */
    public void setPerfilSubstituido(Perfil perfilSubstituido) {
        this.perfilSubstituido = perfilSubstituido;
    }

}
