package com.mercurio.lms.edi.dto;

public class ProcessamentoNotaDTO {
    private Long nrProcessamento;
    private int notasEdiProcessamentoSize;
    private Boolean isFinished;

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public int getNotasEdiProcessamentoSize() {
        return notasEdiProcessamentoSize;
    }

    public void setNotasEdiProcessamentoSize(int notasEdiProcessamentoSize) {
        this.notasEdiProcessamentoSize = notasEdiProcessamentoSize;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }
}
