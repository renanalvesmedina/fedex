package com.mercurio.lms.util;

import com.mercurio.lms.expedicao.dto.NotaFiscalConhecimentoDto;
import com.mercurio.lms.expedicao.dto.ProcessaNotasEdiItemDto;
import com.mercurio.lms.vendas.model.Cliente;

import java.util.List;

public class ErroProcessarNotasFiscaisEdiItemException extends Exception{
    private List<NotaFiscalConhecimentoDto> notaFiscalConhecimentoDtos;
    private Cliente cliente;
    private ProcessaNotasEdiItemDto processaNotasEdiItemDto;
    private Throwable throwable;

    public ErroProcessarNotasFiscaisEdiItemException(List<NotaFiscalConhecimentoDto> notaFiscalConhecimentoDtos, Cliente cliente, ProcessaNotasEdiItemDto processaNotasEdiItemDto, Throwable throwable) {
        this.notaFiscalConhecimentoDtos = notaFiscalConhecimentoDtos;
        this.cliente = cliente;
        this.processaNotasEdiItemDto = processaNotasEdiItemDto;
        this.throwable = throwable;
    }

    public List<NotaFiscalConhecimentoDto> getNotaFiscalConhecimentoDtos() {
        return notaFiscalConhecimentoDtos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public ProcessaNotasEdiItemDto getProcessaNotasEdiItemDto() {
        return processaNotasEdiItemDto;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
