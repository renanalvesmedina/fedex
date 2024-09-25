package com.mercurio.lms.facade.radar.impl.whitelist;

public class ResultadoNotificacao {

    private final String success;
    private final String failure;
    private final Exception exception;

    private ResultadoNotificacao(String success, String failure, Exception exception) {
        this.success = success;
        this.failure = failure;
        this.exception = exception;
    }

    public static ResultadoNotificacao success(String message) {
        return new ResultadoNotificacao(message, null, null);
    }

    public static ResultadoNotificacao failure(String message, Exception exception) {
        return new ResultadoNotificacao(null, message, exception);
    }

    public boolean isSuccessful() {
        return success != null;
    }

    public String successMessage() {
        return success;
    }

    public String failureMessage() {
        return failure;
    }

    public Exception exception() {
        return exception;
    }

}
