package com.mercurio.lms.configuracoes.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.lms.vendas.model.Cliente;

public class ClienteUsuario implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long idClienteUsuario;
	private Cliente cliente;
	private UsuarioLMS usuarioLMS;
	private Boolean blRemetente;
	private Boolean blDestinatario;
	private Boolean blRespFrete;
	
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Long getIdClienteUsuario() {
		return idClienteUsuario;
	}

	public void setIdClienteUsuario(Long idClienteUsuario) {
		this.idClienteUsuario = idClienteUsuario;
	}

	public UsuarioLMS getUsuarioLMS() {
		return usuarioLMS;
	}

	public void setUsuarioLMS(UsuarioLMS usuarioLMS) {
		this.usuarioLMS = usuarioLMS;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idClienteUsuario",
				getIdClienteUsuario()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ClienteUsuario))
			return false;
        ClienteUsuario castOther = (ClienteUsuario) other;
		return new EqualsBuilder().append(this.getIdClienteUsuario(),
				castOther.getIdClienteUsuario()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdClienteUsuario()).toHashCode();
    }

		/**
		 * @return Returns the blDestinatario.
		 */
		public Boolean getBlDestinatario() {
			return blDestinatario;
		}

		/**
	 * @param blDestinatario
	 *            The blDestinatario to set.
		 */
		public void setBlDestinatario(Boolean blDestinatario) {
			this.blDestinatario = blDestinatario;
		}

		/**
		 * @return Returns the blRemetente.
		 */
		public Boolean getBlRemetente() {
			return blRemetente;
		}

		/**
	 * @param blRemetente
	 *            The blRemetente to set.
		 */
		public void setBlRemetente(Boolean blRemetente) {
			this.blRemetente = blRemetente;
		}

		/**
		 * @return Returns the blRespFrete.
		 */
		public Boolean getBlRespFrete() {
			return blRespFrete;
		}

		/**
	 * @param blRespFrete
	 *            The blRespFrete to set.
		 */
		public void setBlRespFrete(Boolean blRespFrete) {
			this.blRespFrete = blRespFrete;
		}

}
