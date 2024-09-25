package com.mercurio.lms.sim.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.entrega.model.OcorrenciaEntrega;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoVolume implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private DomainValue tpScan;

    /** identifier field */
    private Long idEventoVolume;

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
    private com.mercurio.lms.municipios.model.Filial filial;
    
    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;
    
    /** persistent field */
    private VolumeNotaFiscal volumeNotaFiscal;
    
    /** persistent field */
    private OcorrenciaEntrega ocorrenciaEntrega;

	public Long getIdEventoVolume() {
		return idEventoVolume;
	}

	public void setIdEventoVolume(Long idEventoVolume) {
		this.idEventoVolume = idEventoVolume;
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

	public VolumeNotaFiscal getVolumeNotaFiscal() {
		return volumeNotaFiscal;
	}

	public void setVolumeNotaFiscal(VolumeNotaFiscal volumeNotaFiscal) {
		this.volumeNotaFiscal = volumeNotaFiscal;
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

	public void setOcorrenciaEntrega(OcorrenciaEntrega ocorrenciaEntrega) {
		this.ocorrenciaEntrega = ocorrenciaEntrega;
	}

	public OcorrenciaEntrega getOcorrenciaEntrega() {
		return ocorrenciaEntrega;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEventoVolume",
				getIdEventoVolume()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_EVENTO_VOLUME", idEventoVolume)
				.append("ID_EVENTO", evento != null ? evento.getIdEvento() : null)
				.append("ID_VOLUME_NOTA_FISCAL", volumeNotaFiscal != null ? volumeNotaFiscal.getIdVolumeNotaFiscal() : null)
				.append("ID_FILIAL", filial != null ? filial.getIdFilial() : null)
				.append("OB_COMPLEMENTO", obComplemento)
				.append("BL_EVENTO_CANCELADO", blEventoCancelado)
				.append("DH_EVENTO", dhEvento)
				.append("DH_INCLUSAO", dhInclusao)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.append("TP_SCAN", tpScan != null ? tpScan.getValue() : null)
				.append("ID_OCORRENCIA_ENTREGA", ocorrenciaEntrega != null ? ocorrenciaEntrega.getIdOcorrenciaEntrega() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoVolume))
			return false;
        EventoVolume castOther = (EventoVolume) other;
		return new EqualsBuilder().append(this.getIdEventoVolume(),
				castOther.getIdEventoVolume()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoVolume()).toHashCode();
    }

}
