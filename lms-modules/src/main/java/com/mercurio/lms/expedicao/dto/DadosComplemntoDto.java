package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DadosComplemntoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<CampoAdicionalConhecimentoDto> campoAdicionalConhecimento;

    public List<CampoAdicionalConhecimentoDto> getCampoAdicionalConhecimento() {
        return campoAdicionalConhecimento;
    }

    public void setCampoAdicionalConhecimento(List<CampoAdicionalConhecimentoDto> campoAdicionalConhecimento) {
        this.campoAdicionalConhecimento = campoAdicionalConhecimento;
    }
}
