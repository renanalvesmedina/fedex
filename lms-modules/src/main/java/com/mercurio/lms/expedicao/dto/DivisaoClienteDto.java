package com.mercurio.lms.expedicao.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DivisaoClienteDto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long idDivisaoCliente;
	private String descricaoDivisaoCliente;

    public DivisaoClienteDto(Long idDivisaoCliente, String descricaoDivisaoCliente) {
        this.idDivisaoCliente = idDivisaoCliente;
        this.descricaoDivisaoCliente = descricaoDivisaoCliente;
    }

    public Long getIdDivisaoCliente() {
        return idDivisaoCliente;
    }

    public void setIdDivisaoCliente(Long idDivisaoCliente) {
        this.idDivisaoCliente = idDivisaoCliente;
    }

    public String getDescricaoDivisaoCliente() {
        return descricaoDivisaoCliente;
    }

    public void setDescricaoDivisaoCliente(String descricaoDivisaoCliente) {
        this.descricaoDivisaoCliente = descricaoDivisaoCliente;
    }
}
