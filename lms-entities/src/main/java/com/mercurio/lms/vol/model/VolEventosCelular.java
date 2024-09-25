package com.mercurio.lms.vol.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.Conhecimento;

/** @author LMS Custom Hibernate CodeGenerator */
public class VolEventosCelular implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idEventoCelular;

    /** persistent field */
    private DateTime dhSolicitacao;

    /** persistent field */
    private DomainValue tpOrigem;

    /** nullable persistent field */
    private String obAtendente;

    /** nullable persistent field */
    private DateTime dhAtendimento;

    /** persistent field */
    private Conhecimento conhecimento;

    /** persistent field */
    private MeioTransporte meioTransporte;

    /** persistent field */
    private ControleCarga controleCarga;

    /** persistent field */
    private Usuario usuario;

    /** persistent field */
    private VolTiposEventos volTiposEvento;
    
    /** persistent field */
    private PedidoColeta pedidoColeta;
    
    public Long getIdEventoCelular() {
        return this.idEventoCelular;
    }

    public void setIdEventoCelular(Long idEventoCelular) {
        this.idEventoCelular = idEventoCelular;
    }

    public DateTime getDhSolicitacao() {
        return this.dhSolicitacao;
    }

    public void setDhSolicitacao(DateTime dhSolicitacao) {
        this.dhSolicitacao = dhSolicitacao;
    }

    public DomainValue getTpOrigem() {
        return this.tpOrigem;
    }

    public void setTpOrigem(DomainValue tpOrigem) {
        this.tpOrigem = tpOrigem;
    }

    public String getObAtendente() {
        return this.obAtendente;
    }

    public void setObAtendente(String obAtendente) {
        this.obAtendente = obAtendente;
    }

    public DateTime getDhAtendimento() {
        return this.dhAtendimento;
    }

    public void setDhAtendimento(DateTime dhAtendimento) {
        this.dhAtendimento = dhAtendimento;
    }

    public Conhecimento getConhecimento() {
        return this.conhecimento;
    }

    public void setConhecimento(Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public MeioTransporte getMeioTransporte() {
        return this.meioTransporte;
    }

    public void setMeioTransporte(MeioTransporte meioTransporte) {
        this.meioTransporte = meioTransporte;
    }

    public ControleCarga getControleCarga() {
        return this.controleCarga;
    }

    public void setControleCarga(ControleCarga controleCarga) {
        this.controleCarga = controleCarga;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public VolTiposEventos getVolTiposEvento() {
        return this.volTiposEvento;
    }

    public void setVolTiposEvento(VolTiposEventos volTiposEvento) {
        this.volTiposEvento = volTiposEvento;
    }    
    
    public PedidoColeta getPedidoColeta() {
		return pedidoColeta;
	}

	public void setPedidoColeta(PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}

	public String toString() {
		return new ToStringBuilder(this).append("idEventoCelular",
				getIdEventoCelular()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof VolEventosCelular))
			return false;
        VolEventosCelular castOther = (VolEventosCelular) other;
		return new EqualsBuilder().append(this.getIdEventoCelular(),
				castOther.getIdEventoCelular()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdEventoCelular()).toHashCode();
    }

}
