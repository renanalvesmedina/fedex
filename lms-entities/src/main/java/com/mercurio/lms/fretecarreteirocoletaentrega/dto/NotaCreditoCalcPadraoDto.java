package com.mercurio.lms.fretecarreteirocoletaentrega.dto;

import com.mercurio.lms.fretecarreteirocoletaentrega.model.NotaCreditoCalcPadrao;

public class NotaCreditoCalcPadraoDto {
    private Long idTabelaFcFaixaPeso;
    private NotaCreditoCalcPadrao notaCreditoCalcPadrao;

    public NotaCreditoCalcPadraoDto(Long idTabelaFcFaixaPeso, NotaCreditoCalcPadrao notaCreditoCalcPadrao) {
        this.idTabelaFcFaixaPeso = idTabelaFcFaixaPeso;
        this.notaCreditoCalcPadrao = notaCreditoCalcPadrao;
    }

    public Long getIdTabelaFcFaixaPeso() {
        return idTabelaFcFaixaPeso;
    }

    public void setIdTabelaFcFaixaPeso(Long idTabelaFcFaixaPeso) {
        this.idTabelaFcFaixaPeso = idTabelaFcFaixaPeso;
    }

    public NotaCreditoCalcPadrao getNotaCreditoCalcPadrao() {
        return notaCreditoCalcPadrao;
    }

    public void setNotaCreditoCalcPadrao(NotaCreditoCalcPadrao notaCreditoCalcPadrao) {
        this.notaCreditoCalcPadrao = notaCreditoCalcPadrao;
    }
}
