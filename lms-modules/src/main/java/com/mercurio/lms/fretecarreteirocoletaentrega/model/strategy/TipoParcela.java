package com.mercurio.lms.fretecarreteirocoletaentrega.model.strategy;

import com.mercurio.adsm.framework.model.DomainValue;

public enum TipoParcela {

    DIARIA(new DomainValue("DH")),
    QUILOMETRAGEM(new DomainValue("QU")),
    FRETE_PESO(new DomainValue("FP")),
    PERCENTUAL_SOBRE_VALOR(new DomainValue("PV")),
    PERCENTUAL_SOBRE_FRETE(new DomainValue("PF")),
    PERCENTUAL_SOBRE_MERCADORIA(new DomainValue("PC")),
    EVENTO(new DomainValue("EV")),
    VOLUME(new DomainValue("VO"));

    private final DomainValue tipo;

    private TipoParcela(DomainValue tipo) {
        this.tipo = tipo;
    }

    public DomainValue value() {
        return tipo;
    }

    public boolean isEqualTo(DomainValue t) {
        return tipo.equals(t);
    }

    public boolean isEqualTo(TipoParcela t) {
        return tipo.equals(t.value());
    }

}
