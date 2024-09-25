package com.mercurio.lms.tributos.model.param;

import java.math.BigDecimal;

public class CalcularIDifalParam {

    private BigDecimal pcImpostoDifial;
    private BigDecimal pcIcmsUfFim;

    public CalcularIDifalParam(BigDecimal pcImpostoDifial, BigDecimal pcIcmsUfFim) {
        this.pcImpostoDifial = pcImpostoDifial;
        this.pcIcmsUfFim = pcIcmsUfFim;
    }

    public BigDecimal getPcImpostoDifial() {
        return pcImpostoDifial;
    }

    public void setPcImpostoDifial(BigDecimal pcImpostoDifial) {
        this.pcImpostoDifial = pcImpostoDifial;
    }

    public BigDecimal getPcIcmsUfFim() {
        return pcIcmsUfFim;
    }

    public void setPcIcmsUfFim(BigDecimal pcIcmsUfFim) {
        this.pcIcmsUfFim = pcIcmsUfFim;
    }

}
