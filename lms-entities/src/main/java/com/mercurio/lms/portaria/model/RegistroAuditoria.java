package com.mercurio.lms.portaria.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.workflow.model.Pendencia;

/** @author LMS Custom Hibernate CodeGenerator */
public class RegistroAuditoria implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idRegistroAuditoria;

    /** persistent field */
    private DateTime dhRegistroAuditoria;

    /** persistent field */
    private Integer nrRegistroAuditoria;

    /** persistent field */
    private DomainValue tpResultado;

    /** persistent field */
    private DomainValue tpSituacao;

    /** persistent field */
    private String obComentarios;

    /** nullable persistent field */
    private DateTime dhLiberacao;

    /** nullable persistent field */
    private DateTime dhEmissao;

    /** nullable persistent field */
    private String obMotivoLiberacao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;
    
    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;
    
    /** nullable field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario semiReboque;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** nullable field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioLiberacao;
    
    /** nullable field */
    private Pendencia pendencia;

    /** persistent field */
    private EquipeOperacao equipeOperacao;
    
    /** persistent field */
    private List naoConformidadeAuditorias;

    /** persistent field */
    private List equipeAuditorias;

    /** persistent field */
    private List lacresRegistroAuditoria;
    
    public Long getIdRegistroAuditoria() {
        return this.idRegistroAuditoria;
    }

    public void setIdRegistroAuditoria(Long idRegistroAuditoria) {
        this.idRegistroAuditoria = idRegistroAuditoria;
    }

    public DateTime getDhRegistroAuditoria() {
        return this.dhRegistroAuditoria;
    }

    public void setDhRegistroAuditoria(DateTime dhRegistroAuditoria) {
        this.dhRegistroAuditoria = dhRegistroAuditoria;
    }

    public Integer getNrRegistroAuditoria() {
        return this.nrRegistroAuditoria;
    }

    public void setNrRegistroAuditoria(Integer nrRegistroAuditoria) {
        this.nrRegistroAuditoria = nrRegistroAuditoria;
    }

    public DomainValue getTpResultado() {
        return this.tpResultado;
    }

    public void setTpResultado(DomainValue tpResultado) {
        this.tpResultado = tpResultado;
    }

    public DomainValue getTpSituacao() {
        return this.tpSituacao;
    }

    public void setTpSituacao(DomainValue tpSituacao) {
        this.tpSituacao = tpSituacao;
    }

    public String getObComentarios() {
        return this.obComentarios;
    }

    public void setObComentarios(String obComentarios) {
        this.obComentarios = obComentarios;
    }

    public DateTime getDhLiberacao() {
        return this.dhLiberacao;
    }

    public void setDhLiberacao(DateTime dhLiberacao) {
        this.dhLiberacao = dhLiberacao;
    }

    public DateTime getDhEmissao() {
        return this.dhEmissao;
    }

    public void setDhEmissao(DateTime dhEmissao) {
        this.dhEmissao = dhEmissao;
    }

    public String getObMotivoLiberacao() {
        return this.obMotivoLiberacao;
    }

    public void setObMotivoLiberacao(String obMotivoLiberacao) {
        this.obMotivoLiberacao = obMotivoLiberacao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return this.meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }

    public com.mercurio.lms.municipios.model.Filial getFilial() {
        return this.filial;
    }

    public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
        this.filial = filial;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.NaoConformidadeAuditoria.class)     
    public List getNaoConformidadeAuditorias() {
        return this.naoConformidadeAuditorias;
    }

    public void setNaoConformidadeAuditorias(List naoConformidadeAuditorias) {
        this.naoConformidadeAuditorias = naoConformidadeAuditorias;
    }

    @ParametrizedAttribute(type = com.mercurio.lms.portaria.model.EquipeAuditoria.class)     
    public List getEquipeAuditorias() {
        return this.equipeAuditorias;
    }

    public void setEquipeAuditorias(List equipeAuditorias) {
        this.equipeAuditorias = equipeAuditorias;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idRegistroAuditoria",
				getIdRegistroAuditoria()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof RegistroAuditoria))
			return false;
        RegistroAuditoria castOther = (RegistroAuditoria) other;
		return new EqualsBuilder().append(this.getIdRegistroAuditoria(),
				castOther.getIdRegistroAuditoria()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdRegistroAuditoria())
            .toHashCode();
    }

	public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
		return controleCarga;
	}

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
		this.controleCarga = controleCarga;
	}

	public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getSemiReboque() {
		return semiReboque;
	}

	public void setSemiReboque(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario semiReboque) {
		this.semiReboque = semiReboque;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuarioLiberacao() {
		return usuarioLiberacao;
	}

	public void setUsuarioLiberacao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioLiberacao) {
		this.usuarioLiberacao = usuarioLiberacao;
	}

	@ParametrizedAttribute(type = com.mercurio.lms.portaria.model.LacreRegistroAuditoria.class)     
	public List getLacresRegistroAuditoria() {
		return lacresRegistroAuditoria;
	}

	public void setLacresRegistroAuditoria(List lacresRegistroAuditoria) {
		this.lacresRegistroAuditoria = lacresRegistroAuditoria;
	}

	/**
	 * @return Returns the pendencia.
	 */
	public Pendencia getPendencia() {
		return pendencia;
	}

	/**
	 * @param pendencia
	 *            The pendencia to set.
	 */
	public void setPendencia(Pendencia pendencia) {
		this.pendencia = pendencia;
	}

	/**
	 * @return Returns the equipeOperacao.
	 */
	public EquipeOperacao getEquipeOperacao() {
		return equipeOperacao;
	}

	/**
	 * @param equipeOperacao
	 *            The equipeOperacao to set.
	 */
	public void setEquipeOperacao(EquipeOperacao equipeOperacao) {
		this.equipeOperacao = equipeOperacao;
	}

}
