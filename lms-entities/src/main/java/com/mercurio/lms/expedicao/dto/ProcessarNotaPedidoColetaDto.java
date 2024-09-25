package com.mercurio.lms.expedicao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessarNotaPedidoColetaDto  implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private Long idPedidoColeta;
    private Boolean blProdutoDiferenciado;
    private String coletaDhEvento;
    private Long numeroColeta;

    public ProcessarNotaPedidoColetaDto(Long idPedidoColeta, Boolean blProdutoDiferenciado, String coletaDhEvento, Long numeroColeta) {
        this.idPedidoColeta = idPedidoColeta;
        this.blProdutoDiferenciado = blProdutoDiferenciado;
        this.coletaDhEvento = coletaDhEvento;
        this.numeroColeta = numeroColeta;
    }

    public Long getIdPedidoColeta() {
        return idPedidoColeta;
    }

    public void setIdPedidoColeta(Long idPedidoColeta) {
        this.idPedidoColeta = idPedidoColeta;
    }

    public Boolean getBlProdutoDiferenciado() {
        return blProdutoDiferenciado;
    }

    public void setBlProdutoDiferenciado(Boolean blProdutoDiferenciado) {
        this.blProdutoDiferenciado = blProdutoDiferenciado;
    }

    public String getColetaDhEvento() {
        return coletaDhEvento;
    }

    public void setColetaDhEvento(String coletaDhEvento) {
        this.coletaDhEvento = coletaDhEvento;
    }

    public Long getNumeroColeta() {
        return numeroColeta;
    }

    public void setNumeroColeta(Long numeroColeta) {
        this.numeroColeta = numeroColeta;
    }
}
