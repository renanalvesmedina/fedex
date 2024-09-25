package com.mercurio.lms.edi.enums;

public enum SituacaoMonitoramentoProcesso {

    EM_PROCESSAMENTO("EP"),
    FINALIZADO_ERRO("FE"),
    FINALIZADO("PF");

    SituacaoMonitoramentoProcesso(String situacao) {
        this.situacao = situacao;
    }

    private String situacao;

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
