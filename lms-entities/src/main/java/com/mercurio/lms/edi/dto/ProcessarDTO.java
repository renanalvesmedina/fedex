package com.mercurio.lms.edi.dto;

import java.util.List;

public class ProcessarDTO {

    private List listIdNotaFiscalEdi;
    private Long nrProcessamento;

    public List getListIdNotaFiscalEdi() {
        return listIdNotaFiscalEdi;
    }

    public void setListIdNotaFiscalEdi(List listIdNotaFiscalEdi) {
        this.listIdNotaFiscalEdi = listIdNotaFiscalEdi;
    }

    public Long getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(Long nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }
}
