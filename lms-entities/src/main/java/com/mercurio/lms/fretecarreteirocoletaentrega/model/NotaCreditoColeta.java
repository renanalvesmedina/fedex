package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.mercurio.lms.coleta.model.PedidoColeta;

@Entity
@Table(name = "NOTA_CREDITO_COLETA")
@SequenceGenerator(name = "NOTA_CREDITO_COLETA_SQ", sequenceName = "NOTA_CREDITO_COLETA_SQ")
public class NotaCreditoColeta extends NotaCreditoDocumentoFrete {

    private static final long serialVersionUID = 1L;

    private Long idNotaCreditoColeta;
    private PedidoColeta pedidoColeta;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTA_CREDITO_COLETA_SQ")
    @Column(name = "ID_NOTA_CREDITO_COLETA", nullable = false)
    public Long getIdNotaCreditoColeta() {
        return idNotaCreditoColeta;
    }

    public void setIdNotaCreditoColeta(Long idNotaFiscalColeta) {
        this.idNotaCreditoColeta = idNotaFiscalColeta;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PEDIDO_COLETA", nullable = false)
    public PedidoColeta getPedidoColeta() {
        return pedidoColeta;
    }

    public void setPedidoColeta(PedidoColeta pedidoColeta) {
        this.pedidoColeta = pedidoColeta;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getNotaCredito() == null) ? 0 : getNotaCredito().hashCode());
        result = prime * result + ((pedidoColeta == null) ? 0 : pedidoColeta.hashCode());
        result = prime * result + ((getTabelaColetaEntrega() == null) ? 0 : getTabelaColetaEntrega().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NotaCreditoColeta other = (NotaCreditoColeta) obj;
        if (getNotaCredito() == null) {
            if (other.getNotaCredito() != null)
                return false;
        } else if (!getNotaCredito().equals(other.getNotaCredito()))
            return false;
        if (pedidoColeta == null) {
            if (other.pedidoColeta != null)
                return false;
        } else if (!pedidoColeta.equals(other.pedidoColeta))
            return false;
        if (getTabelaColetaEntrega() == null) {
            if (other.getTabelaColetaEntrega() != null)
                return false;
        } else if (!getTabelaColetaEntrega().equals(other.getTabelaColetaEntrega()))
            return false;
        return true;
    }

}
