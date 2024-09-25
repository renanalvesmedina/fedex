package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ConhecimentoSemPesagemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idConhecimento;
    private Boolean blContingencia;
    private ProcessaNotasEdiItemDto processaNotasEdiItem;


    public ConhecimentoSemPesagemDto
    (Long idConhecimento, Boolean blContingencia, ProcessaNotasEdiItemDto processaNotasEdiItem) {
        this.idConhecimento = idConhecimento;
        this.blContingencia = blContingencia;
        this.processaNotasEdiItem = processaNotasEdiItem;
    }

    public ConhecimentoSemPesagemDto() {
    }

    public Long getIdConhecimento() {
        return idConhecimento;
    }

    public void setIdConhecimento(Long idConhecimento) {
        this.idConhecimento = idConhecimento;
    }

    public Boolean getBlContingencia() {
        return blContingencia;
    }

    public void setBlContingencia(Boolean blContingencia) {
        this.blContingencia = blContingencia;
    }

    public ProcessaNotasEdiItemDto getProcessaNotasEdiItem() {
        return processaNotasEdiItem;
    }

    public void setProcessaNotasEdiItem(ProcessaNotasEdiItemDto processaNotasEdiItem) {
        this.processaNotasEdiItem = processaNotasEdiItem;
    }

}
