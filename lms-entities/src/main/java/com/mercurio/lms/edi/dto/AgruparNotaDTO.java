package com.mercurio.lms.edi.dto;

import com.mercurio.lms.expedicao.model.Conhecimento;

import java.util.Map;

public class AgruparNotaDTO {
    private Conhecimento conhecimento;
    private Map mapMeioTransporte;
    private String nrProcessamento;

    public Conhecimento getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(Conhecimento conhecimento) {
        this.conhecimento = conhecimento;
    }

    public Map getMapMeioTransporte() {
        return mapMeioTransporte;
    }

    public void setMapMeioTransporte(Map mapMeioTransporte) {
        this.mapMeioTransporte = mapMeioTransporte;
    }

    public String getNrProcessamento() {
        return nrProcessamento;
    }

    public void setNrProcessamento(String nrProcessamento) {
        this.nrProcessamento = nrProcessamento;
    }
}
