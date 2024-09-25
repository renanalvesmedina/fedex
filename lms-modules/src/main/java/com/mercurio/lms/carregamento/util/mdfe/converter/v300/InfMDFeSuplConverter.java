package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import com.mercurio.lms.mdfe.model.v300.InfMDFeSupl;

public class InfMDFeSuplConverter {

    private final String qrCodMDFe;

    public InfMDFeSuplConverter(String qrCodMDFe) {
        super();
        this.qrCodMDFe = qrCodMDFe;
    }

    public InfMDFeSupl convert() {
        InfMDFeSupl infMDFeSupl = new InfMDFeSupl();
        infMDFeSupl.setQrCodMDFe(qrCodMDFe);
        return infMDFeSupl;
    }

}
