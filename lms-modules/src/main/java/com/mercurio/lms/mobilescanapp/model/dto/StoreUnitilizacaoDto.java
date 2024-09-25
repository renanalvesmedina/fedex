package com.mercurio.lms.mobilescanapp.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreUnitilizacaoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String idDispositivoUnitizado;
    protected String idsVolumes;
    protected String idsDispositivosUnitizacao;
    protected Long idFilial;
    protected Long idUsuario;

    public String getIdDispositivoUnitizado() {
        return idDispositivoUnitizado;
    }

    public void setIdDispositivoUnitizado(String idDispositivoUnitizado) {
        this.idDispositivoUnitizado = idDispositivoUnitizado;
    }

    public String getIdsVolumes() {
        return idsVolumes;
    }

    public void setIdsVolumes(String idsVolumes) {
        this.idsVolumes = idsVolumes;
    }

    public String getIdsDispositivosUnitizacao() {
        return idsDispositivosUnitizacao;
    }

    public void setIdsDispositivosUnitizacao(String idsDispositivosUnitizacao) {
        this.idsDispositivosUnitizacao = idsDispositivosUnitizacao;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }
}
