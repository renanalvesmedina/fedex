package com.mercurio.lms.carregamento.util.mdfe.type;

public enum TipoEventoMdfe {

    ENCERRAMENTO,CANCELAMENTO;
    
    
    public String getTpEvento() {
        return CANCELAMENTO.equals(this) ? "110111" : "110112";
    }
    
}
