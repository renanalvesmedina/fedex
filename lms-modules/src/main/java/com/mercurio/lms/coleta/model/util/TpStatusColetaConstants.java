package com.mercurio.lms.coleta.model.util;

public enum TpStatusColetaConstants {

    EM_ABERTO("AB"),
    TRANSMITIDA("TR"),
    MANIFESTADA("MA"),
    EXECUTADA("EX"),
    CANCELADA("CA"),
    AGUARDANDO_DESCARGA("AD"),
    EM_DESCARGA("ED"),
    NO_TERMINAL("NT"),
    FINALIZADA("FI"),
    NO_MANIFESTO("NM");    
    
    private final String value;

    private TpStatusColetaConstants(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}