/**
 * 
 */
package com.mercurio.lms.workflow.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Usuario;

/**
 * @author Bruno Zaccolo
 * 
 */
public class EmailEventoUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idEmailEventoUsuario;

	private DomainValue tpSituacao;

	private Usuario usuario;

	private EventoWorkflow eventoWorkflow;

	private List filialEmailEventoUsuario;

    public com.mercurio.lms.workflow.model.EventoWorkflow getEventoWorkflow() {
		return eventoWorkflow;
	}

	public void setEventoWorkflow(
			com.mercurio.lms.workflow.model.EventoWorkflow eventoWorkflow) {
		this.eventoWorkflow = eventoWorkflow;
	}

	public List getFilialEmailEventoUsuario() {
		return filialEmailEventoUsuario;
	}

	public void setFilialEmailEventoUsuario(List filialEmailEventoUsuario) {
		this.filialEmailEventoUsuario = filialEmailEventoUsuario;
	}

	public Long getIdEmailEventoUsuario() {
		return idEmailEventoUsuario;
	}

	public void setIdEmailEventoUsuario(Long idEmailEventoUsuario) {
		this.idEmailEventoUsuario = idEmailEventoUsuario;
	}

	public DomainValue getTpSituacao() {
		return tpSituacao;
	}

	public void setTpSituacao(DomainValue tpSituacao) {
		this.tpSituacao = tpSituacao;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String getCodigoDescricao(){
		eventoWorkflow = getEventoWorkflow();
		if (eventoWorkflow == null) {
			return null;
		}
		StringBuffer codigoDescricao = new StringBuffer()
				.append(eventoWorkflow.getTipoEvento().getNrTipoEvento())
				.append(" - ")
		.append(eventoWorkflow.getTipoEvento().getDsTipoEvento());
		return codigoDescricao.toString();
    }

	public String getCodigoDescricaoUsuario(){
		usuario = getUsuario();
		if (usuario == null){
			return null;
		}
		StringBuilder codigoDescricaoUser = new StringBuilder()
		.append(usuario.getNrMatricula()).append(" - ")
		.append(usuario.getNmUsuario());
		return codigoDescricaoUser.toString();
    }

	public String toString() {
		return new ToStringBuilder(this).append("idEmailEventoUsuario",
				getIdEmailEventoUsuario()).toString();
    }
	
	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Integrante))
			return false;
		Integrante castOther = (Integrante) other;
		return new EqualsBuilder().append(this.getIdEmailEventoUsuario(),
				castOther.getIdIntegrante()).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getIdEmailEventoUsuario())
				.toHashCode();
	}	
}
