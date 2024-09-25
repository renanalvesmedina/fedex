package com.mercurio.lms.edi.enums;

public enum StatusProcessamento {

    INICIADO("I"),
    ERROR("E"),
    FINALIZADO("F");

    StatusProcessamento(String status) {
        this.status = status;
    }

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
