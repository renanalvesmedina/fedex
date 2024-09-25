package com.mercurio.lms.edi.dto;

import com.mercurio.adsm.framework.model.DomainValue;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessamentoEdiDTO implements Serializable {

    private Long idProcessamentoEdi;
    private Long idFilial;
    private String nomeClienteProcessamento;
    private String usuario;
    private String dhProcessamento;
    private DomainValue status;
    private List<ProcessamentoNotaEdiDTO> processamentoNotaEdiList;

    public Long getIdProcessamentoEdi() {
        return idProcessamentoEdi;
    }

    public void setIdProcessamentoEdi(Long idProcessamentoEdi) {
        this.idProcessamentoEdi = idProcessamentoEdi;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public String getNomeClienteProcessamento() {
        return nomeClienteProcessamento;
    }

    public void setNomeClienteProcessamento(String nomeClienteProcessamento) {
        this.nomeClienteProcessamento = nomeClienteProcessamento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDhProcessamento() {
        return dhProcessamento;
    }

    public void setDhProcessamento(String dhProcessamento) {
        this.dhProcessamento = dhProcessamento;
    }

    public DomainValue getStatus() {
        return status;
    }

    public void setStatus(DomainValue status) {
        this.status = status;
    }

    public List<ProcessamentoNotaEdiDTO> getProcessamentoNotaEdiList() {
        return processamentoNotaEdiList;
    }

    public void setProcessamentoNotaEdiList(List<ProcessamentoNotaEdiDTO> processamentoNotaEdiList) {
        this.processamentoNotaEdiList = processamentoNotaEdiList;
    }
}
