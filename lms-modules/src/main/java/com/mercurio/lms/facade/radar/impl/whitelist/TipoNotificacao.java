package com.mercurio.lms.facade.radar.impl.whitelist;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public class TipoNotificacao {

    private final String tipo;

    private TipoNotificacao(String tipo) {
        this.tipo = tipo;
    }

    public static TipoNotificacao from(TypedFlatMap request) {
        return new TipoNotificacao(request.getString("tipoEnvio"));
    }

    public boolean isCompleto() {
        return "C".equals(tipo);
    }

    public boolean isSomenteEntrega() {
        return "E".equals(tipo);
    }

}
