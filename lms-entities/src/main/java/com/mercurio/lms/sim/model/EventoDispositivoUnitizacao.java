package com.mercurio.lms.sim.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoDispositivoUnitizacao implements Serializable {

	private static final long serialVersionUID = 1L;

	/** identifier field */
	private DomainValue tpScan;
	
    /** identifier field */
    private Long idEventoDispositivoUnitizacao;

    /** persistent field */
    private DateTime dhEvento;

    /** persistent field */
    private DateTime dhInclusao;

    /** persistent field */
    private Boolean blEventoCancelado;

    /** nullable persistent field */
    private String obComplemento;
    
    /** persistent field */
    private com.mercurio.lms.sim.model.Evento evento;
    
    /** persistent field */
    private com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao;
    
    /** persistent field */
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    public Long getIdEventoDispositivoUnitizacao() {
		return idEventoDispositivoUnitizacao;
	}

	public void setIdEventoDispositivoUnitizacao(
			Long idEventoDispositivoUnitizacao) {
		this.idEventoDispositivoUnitizacao = idEventoDispositivoUnitizacao;
	}

	public DateTime getDhEvento() {
		return dhEvento;
	}

	public void setDhEvento(DateTime dhEvento) {
		this.dhEvento = dhEvento;
	}

	public DateTime getDhInclusao() {
		return dhInclusao;
	}

	public void setDhInclusao(DateTime dhInclusao) {
		this.dhInclusao = dhInclusao;
	}

	public Boolean getBlEventoCancelado() {
		return blEventoCancelado;
	}

	public void setBlEventoCancelado(Boolean blEventoCancelado) {
		this.blEventoCancelado = blEventoCancelado;
	}

	public String getObComplemento() {
		return obComplemento;
	}

	public void setObComplemento(String obComplemento) {
		this.obComplemento = obComplemento;
	}

	public com.mercurio.lms.sim.model.Evento getEvento() {
		return evento;
	}

	public void setEvento(com.mercurio.lms.sim.model.Evento evento) {
		this.evento = evento;
	}

	public com.mercurio.lms.municipios.model.Filial getFilial() {
		return filial;
	}

	public void setFilial(com.mercurio.lms.municipios.model.Filial filial) {
		this.filial = filial;
	}

	public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
		this.usuario = usuario;
	}

	public com.mercurio.lms.carregamento.model.DispositivoUnitizacao getDispositivoUnitizacao() {
		return dispositivoUnitizacao;
	}

	public void setDispositivoUnitizacao(
			com.mercurio.lms.carregamento.model.DispositivoUnitizacao dispositivoUnitizacao) {
		this.dispositivoUnitizacao = dispositivoUnitizacao;
	}

	/**
	 * @param tpScan
	 *            the tpScan to set
	 */
	public void setTpScan(DomainValue tpScan) {
		this.tpScan = tpScan;
	}

	/**
	 * @return the tpScan
	 */
	public DomainValue getTpScan() {
		return tpScan;
	}

	public String toString() {
		return new ToStringBuilder(this).append(
				"idEventoDispositivoUnitizacao",
				getIdEventoDispositivoUnitizacao()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_EVENTO_DISPOSITIVO_UNITIZAC", idEventoDispositivoUnitizacao)
				.append("ID_EVENTO", evento != null ? evento.getIdEvento() : null)
				.append("ID_DISPOSITIVO_UNITIZACAO", dispositivoUnitizacao != null ? dispositivoUnitizacao.getIdDispositivoUnitizacao() : null)
				.append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
				.append("OB_COMPLEMENTO", obComplemento)
				.append("BL_EVENTO_CANCELADO", blEventoCancelado)
				.append("DH_EVENTO", dhEvento)
				.append("DH_INCLUSAO", dhInclusao)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.append("TP_SCAN", tpScan != null ? tpScan.getValue() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoDispositivoUnitizacao))
			return false;
        EventoDispositivoUnitizacao castOther = (EventoDispositivoUnitizacao) other;
		return new EqualsBuilder().append(
				this.getIdEventoDispositivoUnitizacao(),
				castOther.getIdEventoDispositivoUnitizacao()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoDispositivoUnitizacao())
            .toHashCode();
    }

}
