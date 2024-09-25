package com.mercurio.lms.edi.dto;


import java.util.List;

public class LogErroDTO {
    private Long idUsuario;
    private List<LogErrosEdiDTO> logErrosEdiDTO;

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<LogErrosEdiDTO> getLogErrosEdiDTO() {
        return logErrosEdiDTO;
    }

    public void setLogErrosEdiDTO(List<LogErrosEdiDTO> logErrosEdiDTO) {
        this.logErrosEdiDTO = logErrosEdiDTO;
    }
}
