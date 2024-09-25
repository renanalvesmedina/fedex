package com.mercurio.lms.fretecarreteirocoletaentrega.model;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NotaCreditoDocumentoFrete implements Serializable {

    private static final long serialVersionUID = 1L;

    private NotaCredito notaCredito;
    private TabelaColetaEntrega tabelaColetaEntrega;

    public void setNotaCredito(NotaCredito notaCredito) {
        this.notaCredito = notaCredito;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_NOTA_CREDITO", nullable = false)
    public NotaCredito getNotaCredito() {
        return notaCredito;
    }

    public void setTabelaColetaEntrega(TabelaColetaEntrega tabelaColetaEntrega) {
        this.tabelaColetaEntrega = tabelaColetaEntrega;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TABELA_COLETA_ENTREGA", nullable = false)
    public TabelaColetaEntrega getTabelaColetaEntrega() {
        return tabelaColetaEntrega;
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

}
