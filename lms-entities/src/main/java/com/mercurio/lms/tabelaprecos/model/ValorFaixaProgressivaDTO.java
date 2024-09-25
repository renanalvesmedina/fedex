package com.mercurio.lms.tabelaprecos.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ValorFaixaProgressivaDTO implements Serializable {

    private BigDecimal valorUnitarioParcela;

    private BigDecimal vlTaxaFixa;

    private BigDecimal vlKmExtra;

    private Long idTarifaPreco;

    public ValorFaixaProgressivaDTO(BigDecimal valorUnitarioParcela, BigDecimal vlTaxaFixa, BigDecimal vlKmExtra, Long idTarifaPreco) {
        this.valorUnitarioParcela = valorUnitarioParcela;
        this.vlTaxaFixa = vlTaxaFixa;
        this.vlKmExtra = vlKmExtra;
        this.idTarifaPreco = idTarifaPreco;
    }

    public BigDecimal getValorUnitarioParcela() {
        return valorUnitarioParcela;
    }

    public void setValorUnitarioParcela(BigDecimal valorUnitarioParcela) {
        this.valorUnitarioParcela = valorUnitarioParcela;
    }

    public BigDecimal getVlTaxaFixa() {
        return vlTaxaFixa;
    }

    public void setVlTaxaFixa(BigDecimal vlTaxaFixa) {
        this.vlTaxaFixa = vlTaxaFixa;
    }

    public BigDecimal getVlKmExtra() {
        return vlKmExtra;
    }

    public void setVlKmExtra(BigDecimal vlKmExtra) {
        this.vlKmExtra = vlKmExtra;
    }

    public Long getIdTarifaPreco() {
        return idTarifaPreco;
    }

    public void setIdTarifaPreco(Long idTarifaPreco) {
        this.idTarifaPreco = idTarifaPreco;
    }
}
