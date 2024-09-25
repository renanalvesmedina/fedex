package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;

public class FaixaPeso {

    private final FaixaPesoParcelaTabelaCE faixa;
    private Integer quantidade = 0;

    public FaixaPeso(FaixaPesoParcelaTabelaCE faixa) {
        this.faixa = faixa;
    }

    public FaixaPesoParcelaTabelaCE getFaixa() {
        return faixa;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((faixa == null) ? 0 : faixa.hashCode());
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
        FaixaPeso other = (FaixaPeso) obj;
        if (faixa == null) {
            if (other.faixa != null)
                return false;
        } else if (!faixa.equals(other.faixa))
            return false;
        return true;
    }

}
