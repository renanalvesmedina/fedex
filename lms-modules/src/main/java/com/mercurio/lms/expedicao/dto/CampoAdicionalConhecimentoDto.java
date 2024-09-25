package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class CampoAdicionalConhecimentoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long idInformacaoDocServico;
    private String dsCampo;
    private Long idCampoAdicionalConhecimento;
    private String dsValorCampo;
    private Long id;

    public Long getIdInformacaoDocServico() {
        return idInformacaoDocServico;
    }

    public void setIdInformacaoDocServico(Long idInformacaoDocServico) {
        this.idInformacaoDocServico = idInformacaoDocServico;
    }

    public String getDsCampo() {
        return dsCampo;
    }

    public void setDsCampo(String dsCampo) {
        this.dsCampo = dsCampo;
    }

    public Long getIdCampoAdicionalConhecimento() {
        return idCampoAdicionalConhecimento;
    }

    public void setIdCampoAdicionalConhecimento(Long idCampoAdicionalConhecimento) {
        this.idCampoAdicionalConhecimento = idCampoAdicionalConhecimento;
    }

    public String getDsValorCampo() {
        return dsValorCampo;
    }

    public void setDsValorCampo(String dsValorCampo) {
        this.dsValorCampo = dsValorCampo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
