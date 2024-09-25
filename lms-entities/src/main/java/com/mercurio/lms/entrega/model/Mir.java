package com.mercurio.lms.entrega.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class Mir implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idMir;

    /** field */
    private Integer versao;
    
    /** persistent field */
    private Integer nrMir;

    /** persistent field */
    private DomainValue tpMir;

    /** nullable persistent field */
    private DateTime dhRecebimento;

    /** nullable persistent field */
    private DateTime dhEmissao;

    /** persistent field */
    private DomainValue tpDocumentoMir;

    /** nullable persistent field */
    private DateTime dhEnvio;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCriacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioRecebimento;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino;

    /** persistent field */
    private List documentoMirs;

    public Long getIdMir() {
        return this.idMir;
    }

    public void setIdMir(Long idMir) {
        this.idMir = idMir;
    }

    public Integer getNrMir() {
        return this.nrMir;
    }

    public void setNrMir(Integer nrMir) {
        this.nrMir = nrMir;
    }

    public DomainValue getTpMir() {
        return this.tpMir;
    }

    public void setTpMir(DomainValue tpMir) {
        this.tpMir = tpMir;
    }

    public DateTime getDhRecebimento() {
        return this.dhRecebimento;
    }

    public void setDhRecebimento(DateTime dhRecebimento) {
        this.dhRecebimento = dhRecebimento;
    }

    public DateTime getDhEmissao() {
        return this.dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public DateTime getDhEnvio() {
        return this.dhEnvio;
    }

    public void setDhEnvio(DateTime dhEnvio) {
        this.dhEnvio = dhEnvio;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioCriacao() {
        return this.usuarioByIdUsuarioCriacao;
    }

	public void setUsuarioByIdUsuarioCriacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCriacao) {
        this.usuarioByIdUsuarioCriacao = usuarioByIdUsuarioCriacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioRecebimento() {
        return this.usuarioByIdUsuarioRecebimento;
    }

	public void setUsuarioByIdUsuarioRecebimento(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioRecebimento) {
        this.usuarioByIdUsuarioRecebimento = usuarioByIdUsuarioRecebimento;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialOrigem() {
        return this.filialByIdFilialOrigem;
    }

	public void setFilialByIdFilialOrigem(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialOrigem) {
        this.filialByIdFilialOrigem = filialByIdFilialOrigem;
    }

    public com.mercurio.lms.municipios.model.Filial getFilialByIdFilialDestino() {
        return this.filialByIdFilialDestino;
    }

	public void setFilialByIdFilialDestino(
			com.mercurio.lms.municipios.model.Filial filialByIdFilialDestino) {
        this.filialByIdFilialDestino = filialByIdFilialDestino;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.entrega.model.DocumentoMir.class)     
    public List getDocumentoMirs() {
        return this.documentoMirs;
    }

    public void setDocumentoMirs(List documentoMirs) {
        this.documentoMirs = documentoMirs;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idMir", getIdMir()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof Mir))
			return false;
        Mir castOther = (Mir) other;
        return new EqualsBuilder()
				.append(this.getIdMir(), castOther.getIdMir()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdMir()).toHashCode();
    }

	public Integer getVersao() {
		return versao;
	}

	public void setVersao(Integer versao) {
		this.versao = versao;
	}

	public DomainValue getTpDocumentoMir() {
		return tpDocumentoMir;
	}

	public void setTpDocumentoMir(DomainValue tpDocumentoMir) {
		this.tpDocumentoMir = tpDocumentoMir;
	}

}
