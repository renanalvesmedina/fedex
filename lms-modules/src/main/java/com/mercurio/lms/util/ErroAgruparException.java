package com.mercurio.lms.util;

import com.mercurio.lms.expedicao.dto.ErroAgruparNotaFiscalDto;

public class ErroAgruparException extends Exception{

    private ErroAgruparNotaFiscalDto erroAgruparNotaFiscalDto;

    public ErroAgruparException(ErroAgruparNotaFiscalDto erroAgruparNotaFiscalDto) {
        super();
        this.erroAgruparNotaFiscalDto = erroAgruparNotaFiscalDto;
    }

    public ErroAgruparNotaFiscalDto getErroAgruparNotaFiscalDto() {
        return erroAgruparNotaFiscalDto;
    }
}
