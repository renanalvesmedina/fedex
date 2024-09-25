package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class ServicoAdicionalColeta implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idServicoAdicionalColeta;

    /** persistent field */
    private com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional;

    /** persistent field */
    private com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta;

    public Long getIdServicoAdicionalColeta() {
        return this.idServicoAdicionalColeta;
    }

    public void setIdServicoAdicionalColeta(Long idServicoAdicionalColeta) {
        this.idServicoAdicionalColeta = idServicoAdicionalColeta;
    }

    public com.mercurio.lms.configuracoes.model.ServicoAdicional getServicoAdicional() {
        return this.servicoAdicional;
    }

	public void setServicoAdicional(
			com.mercurio.lms.configuracoes.model.ServicoAdicional servicoAdicional) {
        this.servicoAdicional = servicoAdicional;
    }

    public com.mercurio.lms.coleta.model.PedidoColeta getPedidoColeta() {
        return this.pedidoColeta;
    }

	public void setPedidoColeta(
			com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta) {
        this.pedidoColeta = pedidoColeta;
    }

    public String toString() {
		return new ToStringBuilder(this).append("idServicoAdicionalColeta",
				getIdServicoAdicionalColeta()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof ServicoAdicionalColeta))
			return false;
        ServicoAdicionalColeta castOther = (ServicoAdicionalColeta) other;
		return new EqualsBuilder().append(this.getIdServicoAdicionalColeta(),
				castOther.getIdServicoAdicionalColeta()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdServicoAdicionalColeta())
            .toHashCode();
    }

}
