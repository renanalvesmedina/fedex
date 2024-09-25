package com.mercurio.lms.coleta.model.util;

import com.mercurio.adsm.framework.model.DomainValue;

public enum ConstantesColeta {

    COLETA_EXECUTADA(new DomainValue("EX")),
    COLETA_FINALIZADA(new DomainValue("FI")),
    COLETA_NO_TERMINAL(new DomainValue("NT"));

    private final DomainValue value;

    private ConstantesColeta(DomainValue value) {
        this.value = value;
    }

    public boolean isEqualTo(DomainValue domainValue) {
        return value.equals(domainValue);
    }

}
