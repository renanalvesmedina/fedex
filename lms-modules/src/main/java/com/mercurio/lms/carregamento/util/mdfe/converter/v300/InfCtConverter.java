package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.mdfe.model.v300.InfCT;

public class InfCtConverter {

    private Conhecimento conhecimento;

    public InfCtConverter(Conhecimento conhecimento) {
        super();
        this.conhecimento = conhecimento;
    }

    public InfCT convert() {
        InfCT infCt = new InfCT();
        
        infCt.setNCT(conhecimento.getNrDoctoServico() == null ? null : conhecimento.getNrDoctoServico().toString());
        infCt.setSerie("0");
        infCt.setDEmi(conhecimento.getDhEmissao() == null ? null : conhecimento.getDhEmissao().toString("yyyy'-'MM'-'dd'"));

        DecimalFormat formatDoisDecimais = new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(new Locale("pt_br")));
        
        infCt.setVCarga(conhecimento.getVlMercadoria() == null ? null : formatDoisDecimais.format(conhecimento.getVlMercadoria()));

        return infCt;
    }
    
}
