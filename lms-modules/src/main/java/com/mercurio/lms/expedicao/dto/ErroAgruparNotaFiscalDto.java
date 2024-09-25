package com.mercurio.lms.expedicao.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ErroAgruparNotaFiscalDto  implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long nrProcessamento;
    @JsonIgnore
    private Throwable throwable;
    private String messageKey;
    private Object[] messageArgs;
    private String message;
    private StackTraceElement[] stackTrace;
    private ProcessaNotasEdiItemDto processaNotasEdiItemDto;

    public ErroAgruparNotaFiscalDto() {
    }

    public ErroAgruparNotaFiscalDto(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }

    public void setMessageArgs(Object[] messageArgs) {
        this.messageArgs = messageArgs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ProcessaNotasEdiItemDto getProcessaNotasEdiItemDto() {
        return processaNotasEdiItemDto;
    }

    public void setProcessaNotasEdiItemDto(ProcessaNotasEdiItemDto processaNotasEdiItemDto) {
        this.processaNotasEdiItemDto = processaNotasEdiItemDto;
    }
}
