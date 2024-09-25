package com.mercurio.lms.edi.dto;

import java.io.Serializable;
import java.util.List;

public class FinalizaProcessamentoDTO implements Serializable {

    private Long idPedidoColeta;
    private Long idMonitoramentoDescarga;
    private String tpProcessamento;
    private List<Long> listIdsNotasFiscaisEdiInformada;
    private Long idFilial;

    public List<Long> getListIdsNotasFiscaisEdiInformada() {
        return listIdsNotasFiscaisEdiInformada;
    }

    public void setListIdsNotasFiscaisEdiInformada(List<Long> listIdsNotasFiscaisEdiInformada) {
        this.listIdsNotasFiscaisEdiInformada = listIdsNotasFiscaisEdiInformada;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public Long getIdMonitoramentoDescarga() {
        return idMonitoramentoDescarga;
    }

    public void setIdMonitoramentoDescarga(Long idMonitoramentoDescarga) {
        this.idMonitoramentoDescarga = idMonitoramentoDescarga;
    }

    public String getTpProcessamento() {
        return tpProcessamento;
    }

    public void setTpProcessamento(String tpProcessamento) {
        this.tpProcessamento = tpProcessamento;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }
}
