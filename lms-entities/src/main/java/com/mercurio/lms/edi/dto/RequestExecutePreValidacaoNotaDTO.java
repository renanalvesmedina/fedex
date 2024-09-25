package com.mercurio.lms.edi.dto;

import java.io.Serializable;

public class RequestExecutePreValidacaoNotaDTO implements Serializable {

    private DadosValidacaoEdiDTO dadosValidacaoEdi;
    private Long idPedidoColeta;
    private RequestFiltroNotasFiscaisEdiDTO requestFiltroNotasFiscaisEdi;

    public RequestExecutePreValidacaoNotaDTO(DadosValidacaoEdiDTO dadosValidacaoEdi, Long idPedidoColeta,
                                             RequestFiltroNotasFiscaisEdiDTO requestEdidto) {
        this.dadosValidacaoEdi = dadosValidacaoEdi;
        this.idPedidoColeta = idPedidoColeta;
        this.requestFiltroNotasFiscaisEdi = requestEdidto;
    }

    public DadosValidacaoEdiDTO getDadosValidacaoEdi() {
        return dadosValidacaoEdi;
    }

    public void setDadosValidacaoEdi(DadosValidacaoEdiDTO dadosValidacaoEdi) {
        this.dadosValidacaoEdi = dadosValidacaoEdi;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public RequestFiltroNotasFiscaisEdiDTO getRequestFiltroNotasFiscaisEdi() {
        return requestFiltroNotasFiscaisEdi;
    }

    public void setRequestFiltroNotasFiscaisEdi(RequestFiltroNotasFiscaisEdiDTO requestFiltroNotasFiscaisEdi) {
        this.requestFiltroNotasFiscaisEdi = requestFiltroNotasFiscaisEdi;
    }
}
