package com.mercurio.lms.edi.dto;

import java.io.Serializable;
import java.util.List;

public class ResponseFiltroNotasFiscaisEdiDTO implements Serializable {

    private ProcessamentoNotaDTO processamentoNotaDTO = new ProcessamentoNotaDTO();
    private List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTO;
    private List<Long> listIdsNotasFiscaisEdiInformada;

    public ResponseFiltroNotasFiscaisEdiDTO
    (
        Long nrProcessamento, int notasEdiProcessamentoSize,
        List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTO,
        List<Long> listIdsNotasFiscaisEdiInformada
    ) {
        this.processamentoNotaDTO.setNrProcessamento(nrProcessamento);
        this.processamentoNotaDTO.setNotasEdiProcessamentoSize(notasEdiProcessamentoSize);
        this.dadosValidacaoEdiDTO = dadosValidacaoEdiDTO;
        this.listIdsNotasFiscaisEdiInformada = listIdsNotasFiscaisEdiInformada;
    }

    public ProcessamentoNotaDTO getProcessamentoNotaDTO() {
        return processamentoNotaDTO;
    }

    public void setProcessamentoNotaDTO(ProcessamentoNotaDTO processamentoNotaDTO) {
        this.processamentoNotaDTO = processamentoNotaDTO;
    }

    public List<DadosValidacaoEdiDTO> getDadosValidacaoEdiDTO() {
        return dadosValidacaoEdiDTO;
    }

    public void setDadosValidacaoEdiDTO(List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTO) {
        this.dadosValidacaoEdiDTO = dadosValidacaoEdiDTO;
    }

    public List<Long> getListIdsNotasFiscaisEdiInformada() {
        return listIdsNotasFiscaisEdiInformada;
    }

    public void setListIdsNotasFiscaisEdiInformada(List<Long> listIdsNotasFiscaisEdiInformada) {
        this.listIdsNotasFiscaisEdiInformada = listIdsNotasFiscaisEdiInformada;
    }
}
