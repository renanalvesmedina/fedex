package com.mercurio.lms.coleta.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author LMS Custom Hibernate CodeGenerator */
public class PedidoColetaProduto implements Serializable {

	private static final long serialVersionUID = 1L;

    /** identifier field */
    private Long idPedidoColetaProduto;

    /** persistent field */
    private com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta;
    
    /** persistent field */
    private com.mercurio.lms.expedicao.model.Produto produto;

    public Long getIdPedidoColetaProduto() {
        return this.idPedidoColetaProduto;
    }

    public void setIdPedidoColetaProduto(Long idPedidoColetaProduto) {
        this.idPedidoColetaProduto = idPedidoColetaProduto;
    }

	public com.mercurio.lms.expedicao.model.Produto getProduto() {
		return this.produto;
	}

	public void setProduto(com.mercurio.lms.expedicao.model.Produto produto) {
		this.produto = produto;
	}    

	public com.mercurio.lms.coleta.model.PedidoColeta getPedidoColeta() {
		return this.pedidoColeta;
	}

	public void setPedidoColeta(
			com.mercurio.lms.coleta.model.PedidoColeta pedidoColeta) {
		this.pedidoColeta = pedidoColeta;
	}
	
    public String toString() {
		return new ToStringBuilder(this).append("idPedidoColetaProduto",
				getIdPedidoColetaProduto()).toString();
    }

    public boolean equals(Object other) {
		if ((this == other))
			return true;
		if (!(other instanceof PedidoColetaProduto))
			return false;
        PedidoColetaProduto castOther = (PedidoColetaProduto) other;
		return new EqualsBuilder().append(this.getIdPedidoColetaProduto(),
				castOther.getIdPedidoColetaProduto()).isEquals();
    }

    public int hashCode() {
		return new HashCodeBuilder().append(getIdPedidoColetaProduto())
            .toHashCode();
    }
}
