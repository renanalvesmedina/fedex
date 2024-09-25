package com.mercurio.lms.mobilescanapp.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreAlocarDto extends StoreUnitilizacaoDto{
    private static final long serialVersionUID = 1L;
    private String idEnderecoTerminal;

    public String getIdEnderecoTerminal() {
        return idEnderecoTerminal;
    }

    public void setIdEnderecoTerminal(String idEnderecoTerminal) {
        this.idEnderecoTerminal = idEnderecoTerminal;
    }

}
