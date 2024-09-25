package com.mercurio.lms.expedicao.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;

/** @author LMS Custom Hibernate CodeGenerator */
public class PreAlerta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPreAlerta;

    /** persistent field */
    private Integer nrPreAlerta;

    /** persistent field */
    private DateTime dhSaida;

    /** persistent field */
    private DateTime dhChegada;

    /** persistent field */
    private String dsVoo;

    /** nullable persistent field */
    private DateTime dhRecebimentoMens;

    /** nullable persistent field */
    private Boolean blVooConfirmado;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.Usuario usuario;

    /** persistent field */
    private com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta;

    /** persistent field */
    private com.mercurio.lms.expedicao.model.Awb awb;

    public Long getIdPreAlerta() {
        return this.idPreAlerta;
    }

    public void setIdPreAlerta(Long idPreAlerta) {
        this.idPreAlerta = idPreAlerta;
    }

    public Integer getNrPreAlerta() {
        return this.nrPreAlerta;
    }

    public void setNrPreAlerta(Integer nrPreAlerta) {
        this.nrPreAlerta = nrPreAlerta;
    }

    public DateTime getDhSaida() {
        return this.dhSaida;
    }

    public void setDhSaida(DateTime dhSaida) {
        this.dhSaida = dhSaida;
    }

    public DateTime getDhChegada() {
        return this.dhChegada;
    }

    public void setDhChegada(DateTime dhChegada) {
        this.dhChegada = dhChegada;
    }

    public String getDsVoo() {
        return this.dsVoo;
    }

    public void setDsVoo(String dsVoo) {
        this.dsVoo = dsVoo;
    }

    public DateTime getDhRecebimentoMens() {
        return this.dhRecebimentoMens;
    }

    public void setDhRecebimentoMens(DateTime dhRecebimentoMens) {
        this.dhRecebimentoMens = dhRecebimentoMens;
    }

    public Boolean getBlVooConfirmado() {
        return this.blVooConfirmado;
    }

    public void setBlVooConfirmado(Boolean blVooConfirmado) {
        this.blVooConfirmado = blVooConfirmado;
    }

    public com.mercurio.lms.configuracoes.model.Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(com.mercurio.lms.configuracoes.model.Usuario usuario) {
        this.usuario = usuario;
    }

    public com.mercurio.lms.coleta.model.PedidoColeta getPedidoColeta() {
        return this.pedidoColeta;
    }

	public void setPedidoColeta(
			com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta) {
        this.pedidoColeta = pedidoColeta;
    }

    public com.mercurio.lms.expedicao.model.Awb getAwb() {
        return this.awb;
    }

    public void setAwb(com.mercurio.lms.expedicao.model.Awb awb) {
        this.awb = awb;
    }

    public String toString() {
        return new ToStringBuilder(this)
				.append("idPreAlerta", getIdPreAlerta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PreAlerta))
			return false;
        PreAlerta castOther = (PreAlerta) other;
		return new EqualsBuilder().append(this.getIdPreAlerta(),
				castOther.getIdPreAlerta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPreAlerta()).toHashCode();
    }

}
