package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;

/** @author LMS Custom Hibernate CodeGenerator */
public class EventoColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoColeta;

    /** persistent field */
    private DateTime dhEvento;

    /** persistent field */
    private DomainValue tpEventoColeta;

    /** nullable persistent field */
    private String dsDescricao;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.coleta.model.OcorrenciaColeta ocorrenciaColeta;

    /** persistent field */
    private com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta;

    /** persistent field */
    private com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario;
    
    /** persistent field */
    private com.mercurio.lms.coleta.model.DetalheColeta detalheColeta;

    public EventoColeta() {
    }

    public EventoColeta(Long idEventoColeta, DateTime dhEvento, DomainValue tpEventoColeta, String dsDescricao, Usuario usuario, OcorrenciaColeta ocorrenciaColeta, PedidoColeta pedidoColeta, MeioTransporteRodoviario meioTransporteRodoviario, DetalheColeta detalheColeta) {
        this.idEventoColeta = idEventoColeta;
        this.dhEvento = dhEvento;
        this.tpEventoColeta = tpEventoColeta;
        this.dsDescricao = dsDescricao;
        this.usuario = usuario;
        this.ocorrenciaColeta = ocorrenciaColeta;
        this.pedidoColeta = pedidoColeta;
        this.meioTransporteRodoviario = meioTransporteRodoviario;
        this.detalheColeta = detalheColeta;
    }

    public Long getIdEventoColeta() {
        return this.idEventoColeta;
    }

    public void setIdEventoColeta(Long idEventoColeta) {
        this.idEventoColeta = idEventoColeta;
    }

    public DateTime getDhEvento() {
        return this.dhEvento;
    }

    public void setDhEvento(DateTime dhEvento) {
        this.dhEvento = dhEvento;
    }

    public DomainValue getTpEventoColeta() {
        return this.tpEventoColeta;
    }

    public void setTpEventoColeta(DomainValue tpEventoColeta) {
        this.tpEventoColeta = tpEventoColeta;
    }

    public String getDsDescricao() {
        return this.dsDescricao;
    }

    public void setDsDescricao(String dsDescricao) {
        this.dsDescricao = dsDescricao;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.coleta.model.OcorrenciaColeta getOcorrenciaColeta() {
        return this.ocorrenciaColeta;
    }

	public void setOcorrenciaColeta(
			com.mercurio.lms.coleta.model.OcorrenciaColeta ocorrenciaColeta) {
        this.ocorrenciaColeta = ocorrenciaColeta;
    }

    public com.mercurio.lms.coleta.model.PedidoColeta getPedidoColeta() {
        return this.pedidoColeta;
    }

	public void setPedidoColeta(
			com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta) {
        this.pedidoColeta = pedidoColeta;
    }

    public com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario getMeioTransporteRodoviario() {
        return meioTransporteRodoviario;
    }

	public void setMeioTransporteRodoviario(
			com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario meioTransporteRodoviario) {
        this.meioTransporteRodoviario = meioTransporteRodoviario;
    }
    
    public com.mercurio.lms.coleta.model.DetalheColeta getDetalheColeta() {
		return detalheColeta;
	}

	public void setDetalheColeta(
			com.mercurio.lms.coleta.model.DetalheColeta detalheColeta) {
		this.detalheColeta = detalheColeta;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEventoColeta",
				getIdEventoColeta()).toString();
    }

	public String toString(ToStringStyle style) {
		return new ToStringBuilder(this, style)
				.append("ID_EVENTO_COLETA", idEventoColeta)
				.append("ID_PEDIDO_COLETA", pedidoColeta != null ? pedidoColeta.getIdPedidoColeta() : null)
				.append("ID_OCORRENCIA_COLETA", ocorrenciaColeta != null ? ocorrenciaColeta.getIdOcorrenciaColeta() : null)
				.append("ID_USUARIO", usuario != null ? usuario.getIdUsuario() : null)
				.append("DH_EVENTO", dhEvento)
				.append("TP_EVENTO_COLETA", tpEventoColeta != null ? tpEventoColeta.getValue() : null)
				.append("DS_DESCRICAO", dsDescricao)
				.append("ID_MEIO_TRANSPORTE", meioTransporteRodoviario != null ? meioTransporteRodoviario.getIdMeioTransporte() : null)
				.append("ID_DETALHE_COLETA", detalheColeta != null ? detalheColeta.getIdDetalheColeta() : null)
				.toString();
	}

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof EventoColeta))
			return false;
        EventoColeta castOther = (EventoColeta) other;
		return new EqualsBuilder().append(this.getIdEventoColeta(),
				castOther.getIdEventoColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoColeta()).toHashCode();
    }

}
