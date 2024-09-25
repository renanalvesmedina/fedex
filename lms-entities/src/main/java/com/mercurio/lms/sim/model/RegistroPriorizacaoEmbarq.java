package com.mercurio.lms.sim.model;

import java.io.Serializable;
import java.util.List;
 
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.vendas.model.Cliente;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegistroPriorizacaoEmbarq implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegistroPriorizacaoEmbarq;

    /** persistent field */
    private DateTime dhRegistro;

    /** persistent field */
    private DateTime dhRegistroEmbarque;

    /** persistent field */
    private String obPriorizacao;

    /** persistent field */
    private String nmSolicitante;

    /** persistent field */
    private DateTime dhCancelamento;

    /** persistent field */
    private String obCancelamanto;

    /** persistent field */
    private DateTime dhPrevistaRetirada;

    private Cliente remetente;
    
    private Cliente destinatario;
    
    private Usuario usuarioCriacao;
    
    private Usuario usuarioCancelamento;
    
    private Filial filial;
    
    private List registroPriorizacaoDocto;
 
    public Filial getFilial() {
		return filial;
	}

	public void setFilial(Filial filial) {
		this.filial = filial;
	}

	public Cliente getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Cliente destinatario) {
		this.destinatario = destinatario;
	}

	public DateTime getDhCancelamento() {
		return dhCancelamento;
	}

	public void setDhCancelamento(DateTime dhCancelamento) {
		this.dhCancelamento = dhCancelamento;
	}

	public DateTime getDhPrevistaRetirada() {
		return dhPrevistaRetirada;
	}

	public void setDhPrevistaRetirada(DateTime dhPrevistaRetirada) {
		this.dhPrevistaRetirada = dhPrevistaRetirada;
	}

	public DateTime getDhRegistro() {
		return dhRegistro;
	}

	public void setDhRegistro(DateTime dhRegistro) {
		this.dhRegistro = dhRegistro;
	}

	public DateTime getDhRegistroEmbarque() {
		return dhRegistroEmbarque;
	}

	public void setDhRegistroEmbarque(DateTime dhRegistroEmbarque) {
		this.dhRegistroEmbarque = dhRegistroEmbarque;
	}

	public Long getIdRegistroPriorizacaoEmbarq() {
		return idRegistroPriorizacaoEmbarq;
	}

	public void setIdRegistroPriorizacaoEmbarq(Long idRegistroPriorizacaoEmbarq) {
		this.idRegistroPriorizacaoEmbarq = idRegistroPriorizacaoEmbarq;
	}

	public String getObCancelamanto() {
		return obCancelamanto;
	}

	public void setObCancelamanto(String obCancelamanto) {
		this.obCancelamanto = obCancelamanto;
	}

	public String getObPriorizacao() {
		return obPriorizacao;
	}

	public void setObPriorizacao(String obPriorizacao) {
		this.obPriorizacao = obPriorizacao;
	}

	public Cliente getRemetente() {
		return remetente;
	}

	public void setRemetente(Cliente remetente) {
		this.remetente = remetente;
	}

	public Usuario getUsuarioCancelamento() {
		return usuarioCancelamento;
	}

	public void setUsuarioCancelamento(Usuario usuarioCancelamento) {
		this.usuarioCancelamento = usuarioCancelamento;
	}

	public Usuario getUsuarioCriacao() {
		return usuarioCriacao;
	}

	public void setUsuarioCriacao(Usuario usuarioCriacao) {
		this.usuarioCriacao = usuarioCriacao;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idRegistroPriorizacaoEmbarq",
				getIdRegistroPriorizacaoEmbarq()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegistroPriorizacaoEmbarq))
			return false;
        RegistroPriorizacaoEmbarq castOther = (RegistroPriorizacaoEmbarq) other;
		return new EqualsBuilder().append(
				this.getIdRegistroPriorizacaoEmbarq(),
				castOther.getIdRegistroPriorizacaoEmbarq()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegistroPriorizacaoEmbarq())
            .toHashCode();
    }

	public List getRegistroPriorizacaoDocto() {
		return registroPriorizacaoDocto;
	}

	public void setRegistroPriorizacaoDocto(List registroPriorizacaoDocto) {
		this.registroPriorizacaoDocto = registroPriorizacaoDocto;
	}

	public String getNmSolicitante() {
		return nmSolicitante;
	}

	public void setNmSolicitante(String nmSolicitante) {
		this.nmSolicitante = nmSolicitante;
	}

}
