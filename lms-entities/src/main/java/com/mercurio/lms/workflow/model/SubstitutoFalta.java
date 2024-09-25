package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class SubstitutoFalta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idSubstitutoFalta;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private Perfil perfil;

    /** persistent field */
    private com.mercurio.lms.workflow.model.Integrante integrante;

    /** persistent field */
    private List substitutoFaltaAcoes;

    public Long getIdSubstitutoFalta() {
        return this.idSubstitutoFalta;
    }

    public void setIdSubstitutoFalta(Long idSubstitutoFalta) {
        this.idSubstitutoFalta = idSubstitutoFalta;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.workflow.model.Integrante getIntegrante() {
        return this.integrante;
    }

	public void setIntegrante(
			com.mercurio.lms.workflow.model.Integrante integrante) {
        this.integrante = integrante;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.workflow.model.SubstitutoFaltaAcao.class)     
    public List getSubstitutoFaltaAcoes() {
        return this.substitutoFaltaAcoes;
    }

    public void setSubstitutoFaltaAcoes(List substitutoFaltaAcoes) {
        this.substitutoFaltaAcoes = substitutoFaltaAcoes;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idSubstitutoFalta",
				getIdSubstitutoFalta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof SubstitutoFalta))
			return false;
        SubstitutoFalta castOther = (SubstitutoFalta) other;
		return new EqualsBuilder().append(this.getIdSubstitutoFalta(),
				castOther.getIdSubstitutoFalta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdSubstitutoFalta())
            .toHashCode();
    }

    /**
     * @return Returns the perfil.
     */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
	 * @param perfil
	 *            The perfil to set.
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

}
