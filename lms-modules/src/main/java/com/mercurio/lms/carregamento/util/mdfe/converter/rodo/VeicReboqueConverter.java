package com.mercurio.lms.carregamento.util.mdfe.converter.rodo;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.VeicReboque;

public class VeicReboqueConverter {

    private ManifestoEletronico mdfe;
    
    public VeicReboqueConverter(ManifestoEletronico mdfe) {
        super();
        this.mdfe = mdfe;
    }

    public VeicReboque[] convert() {
        VeicReboque veicReboque = new VeicReboque();
        
        MeioTransporte reboque = mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado();
        veicReboque.setCInt(MdfeConverterUtils.tratarString(reboque.getNrFrota(),10));
        veicReboque.setPlaca(MdfeConverterUtils.tratarString(reboque.getNrIdentificador(),7));
        veicReboque.setTara(reboque.getNrCapacidadeKg() == null ? null : ""+reboque.getNrCapacidadeKg().intValue());
        veicReboque.setCapKG(reboque.getNrCapacidadeKg() == null ? null : ""+reboque.getNrCapacidadeKg().intValue());
        veicReboque.setCapM3(reboque.getNrCapacidadeM3() == null ? null : ""+reboque.getNrCapacidadeM3().intValue());

        
        return new VeicReboque[] {veicReboque};
    }
    
}
