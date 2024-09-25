package com.mercurio.lms.services.evento;

public class EventoDoctoServicoAvonException extends Exception{

    String returnCode;
    String returnMessage;
    
    public EventoDoctoServicoAvonException(String returnCode,String message) {
        this.returnCode = returnCode;
        this.returnMessage = message;
    }
    
    public String getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }
    
}
