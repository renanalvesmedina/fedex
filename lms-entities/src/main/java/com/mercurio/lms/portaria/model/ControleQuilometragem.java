package com.mercurio.lms.portaria.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class ControleQuilometragem implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Long MAX_QUILOMETRAGEM = Long.valueOf(1000000);

    /** identifier field */
    private Long idControleQuilometragem;

    /** persistent field */
    private DateTime dhMedicao;

    /** persistent field */
    private Integer nrQuilometragem;

    /** persistent field */
    private Boolean blVirouHodometro;

    /** persistent field */
    private Boolean blSaida;

    /** nullable persistent field */
    private DateTime dhCorrecao;

    /** nullable persistent field */
    private String obControleQuilometragem;

    /** persistent field */
    private DomainValue tpSituacaoPendencia;

    /** persistent field */
    private com.mercurio.lms.carregamento.model.ControleCarga controleCarga;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuario;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCorrecao;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;

    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;

    /** persistent field */
    private com.mercurio.lms.portaria.model.OrdemSaida ordemSaida;
    
    /** persistent field */
    private com.mercurio.lms.workflow.model.Pendencia pendencia;

    public Long getIdControleQuilometragem() {
        return this.idControleQuilometragem;
    }

    public void setIdControleQuilometragem(Long idControleQuilometragem) {
        this.idControleQuilometragem = idControleQuilometragem;
    }

    public DateTime getDhMedicao() {
        return this.dhMedicao;
    }

    public void setDhMedicao(DateTime dhMedicao) {
        this.dhMedicao = dhMedicao;
    }

    public Integer getNrQuilometragem() {
        return this.nrQuilometragem;
    }

    public void setNrQuilometragem(Integer nrQuilometragem) {
        this.nrQuilometragem = nrQuilometragem;
    }

    public Boolean getBlVirouHodometro() {
        return this.blVirouHodometro;
    }

    public void setBlVirouHodometro(Boolean blVirouHodometro) {
        this.blVirouHodometro = blVirouHodometro;
    }

    public Boolean getBlSaida() {
        return this.blSaida;
    }

    public void setBlSaida(Boolean blSaida) {
        this.blSaida = blSaida;
    }

    public DateTime getDhCorrecao() {
        return this.dhCorrecao;
    }

    public void setDhCorrecao(DateTime dhCorrecao) {
        this.dhCorrecao = dhCorrecao;
    }

    public String getObControleQuilometragem() {
        return this.obControleQuilometragem;
    }

    public void setObControleQuilometragem(String obControleQuilometragem) {
        this.obControleQuilometragem = obControleQuilometragem;
    }

    public DomainValue getTpSituacaoPendencia() {
		return tpSituacaoPendencia;
	}

    public void setTpSituacaoPendencia(DomainValue tpSituacaoPendencia) {
		this.tpSituacaoPendencia = tpSituacaoPendencia;
	}

    public com.mercurio.lms.carregamento.model.ControleCarga getControleCarga() {
        return this.controleCarga;
    }

	public void setControleCarga(
			com.mercurio.lms.carregamento.model.ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuario() {
        return this.usuarioByIdUsuario;
    }

	public void setUsuarioByIdUsuario(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuario) {
        this.usuarioByIdUsuario = usuarioByIdUsuario;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuarioByIdUsuarioCorrecao() {
        return this.usuarioByIdUsuarioCorrecao;
    }

	public void setUsuarioByIdUsuarioCorrecao(
			com.mercurio.lms.configuracoes.model.Usuario usuarioByIdUsuarioCorrecao) {
        this.usuarioByIdUsuarioCorrecao = usuarioByIdUsuarioCorrecao;
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

    public String toString() {
		return new ToStringBuilder(this).append("idControleQuilometragem",
				getIdControleQuilometragem()).toString();
    }
    
    public com.mercurio.lms.portaria.model.OrdemSaida getOrdemSaida() {
        return this.ordemSaida;
    }

	public void setOrdemSaida(
			com.mercurio.lms.portaria.model.OrdemSaida ordemSaida) {
        this.ordemSaida = ordemSaida;
    }

    public com.mercurio.lms.workflow.model.Pendencia getPendencia() {
		return pendencia;
	}

	public void setPendencia(com.mercurio.lms.workflow.model.Pendencia pendencia) {
		this.pendencia = pendencia;
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ControleQuilometragem))
			return false;
        ControleQuilometragem castOther = (ControleQuilometragem) other;
		return new EqualsBuilder().append(this.getIdControleQuilometragem(),
				castOther.getIdControleQuilometragem()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdControleQuilometragem())
            .toHashCode();
    }

}
