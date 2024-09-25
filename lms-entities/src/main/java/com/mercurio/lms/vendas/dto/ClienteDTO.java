package com.mercurio.lms.vendas.dto;

import com.mercurio.lms.vendas.model.Cliente;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ClienteDTO implements Serializable {

    private Long idCliente;
    private Boolean blAtualizaDestinatarioEdi;
    private Boolean blObrigaPesoCubadoEdi;
    private String nomePessoa;
    private Long idFilial;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente cliente) {
        this.idCliente = cliente.getIdCliente();
        this.blAtualizaDestinatarioEdi = cliente.getBlAtualizaDestinatarioEdi();
        this.blObrigaPesoCubadoEdi = cliente.getBlObrigaPesoCubadoEdi();
    }

    public Long getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public void setBlAtualizaDestinatarioEdi(Boolean blAtualizaDestinatarioEdi) {
        this.blAtualizaDestinatarioEdi = blAtualizaDestinatarioEdi;
    }

    public Boolean getBlAtualizaDestinatarioEdi() {
        return blAtualizaDestinatarioEdi;
    }

    public Boolean getBlObrigaPesoCubadoEdi() {
        return blObrigaPesoCubadoEdi;
    }

    public void setBlObrigaPesoCubadoEdi(Boolean blObrigaPesoCubadoEdi) {
        this.blObrigaPesoCubadoEdi = blObrigaPesoCubadoEdi;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public Long getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Long idFilial) {
        this.idFilial = idFilial;
    }
}
