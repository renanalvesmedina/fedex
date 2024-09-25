package com.mercurio.lms.facade.radar.impl.whitelist;

import com.mercurio.adsm.framework.util.TypedFlatMap;

public class TipoWhiteList {

    private final String tipo;

    private TipoWhiteList(String tipo) {
        this.tipo = tipo;
    }

    public static TipoWhiteList from(TypedFlatMap request) {
        return new TipoWhiteList(request.getString("tpWhiteList"));
    }

    public boolean isForEmail() {
        return "E".equals(tipo);
    }

    public boolean isForPush() {
        return "P".equals(tipo);
    }

}
