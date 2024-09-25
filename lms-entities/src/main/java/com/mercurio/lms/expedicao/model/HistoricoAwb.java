package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;

/** @author LMS Custom Hibernate CodeGenerator */
public class HistoricoAwb implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Long idHistoricoAwb;

	/** persistent field */
	private DateTime dhInclusao;

	/** persistent field */
	private String dsHistoricoAwb;

	/** persistent field */
	private Boolean blGerarMensagem;

	/** persistent field */
	private Awb awb;

	/** persistent field */
	private Usuario usuario;

	public Long getIdHistoricoAwb() {
		return this.idHistoricoAwb;
	}

	public void setIdHistoricoAwb(Long idHistoricoAwb) {
		this.idHistoricoAwb = idHistoricoAwb;
	}

	public DateTime getDhInclusao() {
		return this.dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public String getDsHistoricoAwb() {
		return this.dsHistoricoAwb;
	}

	public void setDsHistoricoAwb(String dsHistoricoAwb) {
		this.dsHistoricoAwb = dsHistoricoAwb;
	}

	public Boolean getBlGerarMensagem() {
		return this.blGerarMensagem;
	}

	public void setBlGerarMensagem(Boolean blGerarMensagem) {
		this.blGerarMensagem = blGerarMensagem;
	}

	public Awb getAwb() {
		return this.awb;
	}

	public void setAwb(Awb awb) {
		this.awb = awb;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idHistoricoAwb",
				getIdHistoricoAwb()).toString();
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof HistoricoAwb))
			return false;
		HistoricoAwb castOther = (HistoricoAwb) other;
		return new EqualsBuilder().append(this.getIdHistoricoAwb(),
				castOther.getIdHistoricoAwb()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdHistoricoAwb()).toHashCode();
	}

}
