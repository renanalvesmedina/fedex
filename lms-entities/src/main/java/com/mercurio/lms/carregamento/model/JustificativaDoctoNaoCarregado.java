package com.mercurio.lms.carregamento.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class JustificativaDoctoNaoCarregado implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idJustificativaDoctoNaoCarregado;

    /** persistent field */
    private String dsJustificativa;
    
    /** persistent field */
    private DateTime dhJustificativa;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioJustificativa;
    
    /** persistent field */
    private com.mercurio.lms.expedicao.model.DoctoServico doctoServico;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga;

    /** persistent field */
    private com.mercurio.lms.sim.model.RegistroPriorizacaoDocto registroPriorizacaoDocto;

    public com.mercurio.lms.carregamento.model.CarregamentoDescarga getCarregamentoDescarga() {
		return carregamentoDescarga;
	}

	public void setCarregamentoDescarga(
			com.mercurio.lms.carregamento.model.CarregamentoDescarga carregamentoDescarga) {
		this.carregamentoDescarga = carregamentoDescarga;
	}

	public com.mercurio.lms.expedicao.model.DoctoServico getDoctoServico() {
		return doctoServico;
	}

	public void setDoctoServico(
			com.mercurio.lms.expedicao.model.DoctoServico doctoServico) {
		this.doctoServico = doctoServico;
	}

	public String getDsJustificativa() {
		return dsJustificativa;
	}

	public void setDsJustificativa(String dsJustificativa) {
		this.dsJustificativa = dsJustificativa;
	}

	public Long getIdJustificativaDoctoNaoCarregado() {
		return idJustificativaDoctoNaoCarregado;
	}

	public void setIdJustificativaDoctoNaoCarregado(
			Long idJustificativaDoctoNaoCarregado) {
		this.idJustificativaDoctoNaoCarregado = idJustificativaDoctoNaoCarregado;
	}

    public DateTime getDhJustificativa() {
		return dhJustificativa;
	}

	public void setDhJustificativa(DateTime dhJustificativa) {
		this.dhJustificativa = dhJustificativa;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioJustificativa() {
		return usuarioJustificativa;
	}

	public void setUsuarioJustificativa(
			com.mercurio.lms.configuracoes.model.Usuario usuarioJustificativa) {
		this.usuarioJustificativa = usuarioJustificativa;
	}
	
	public com.mercurio.lms.sim.model.RegistroPriorizacaoDocto getRegistroPriorizacaoDocto() {
		return registroPriorizacaoDocto;
	}

	public void setRegistroPriorizacaoDocto(
			com.mercurio.lms.sim.model.RegistroPriorizacaoDocto registroPriorizacaoDocto) {
		this.registroPriorizacaoDocto = registroPriorizacaoDocto;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof JustificativaDoctoNaoCarregado))
			return false;
        JustificativaDoctoNaoCarregado castOther = (JustificativaDoctoNaoCarregado) other;
		return new EqualsBuilder().append(
				this.getIdJustificativaDoctoNaoCarregado(),
				castOther.getIdJustificativaDoctoNaoCarregado()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(
				getIdJustificativaDoctoNaoCarregado()).toHashCode();
    }
}