package com.mercurio.lms.expedicao.dto;

public class QtdVolumesConhecimentoDto {
    private Long idConhecimento;
    private Long qtd;

    public QtdVolumesConhecimentoDto(Long idConhecimento, Long qtd) {
        this.idConhecimento = idConhecimento;
        this.qtd = qtd;
    }

    public Long getIdConhecimento() {
        return idConhecimento;
    }

    public void setIdConhecimento(Long idConhecimento) {
        this.idConhecimento = idConhecimento;
    }

    public Long getQtd() {
        return qtd;
    }

    public void setQtd(Long qtd) {
        this.qtd = qtd;
    }
}
