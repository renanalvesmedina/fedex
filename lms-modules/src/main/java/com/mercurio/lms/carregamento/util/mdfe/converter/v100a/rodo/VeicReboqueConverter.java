package com.mercurio.lms.carregamento.util.mdfe.converter.v100a.rodo;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.VeicReboque;
import com.mercurio.lms.mdfe.model.v100a.types.TUf;
import com.mercurio.lms.mdfe.model.v100a.types.TpCarType;
import com.mercurio.lms.util.FormatUtils;

public class VeicReboqueConverter {

    private ManifestoEletronico mdfe;
    private String retiraZeroInicialIe;
    
    public VeicReboqueConverter(ManifestoEletronico mdfe, String retiraZeroInicialIe) {
        super();
        this.mdfe = mdfe;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
    }

    public VeicReboque[] convert() {
        VeicReboque veicReboque = new VeicReboque();
        
        MeioTransporte reboque = mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado();
        veicReboque.setCInt(MdfeConverterUtils.tratarString(reboque.getNrFrota(),10));
        veicReboque.setPlaca(MdfeConverterUtils.tratarString(reboque.getNrIdentificador(),7));
        veicReboque.setTara(reboque.getNrCapacidadeKg() == null ? null : ""+reboque.getNrCapacidadeKg().intValue());
        veicReboque.setCapKG(reboque.getNrCapacidadeKg() == null ? null : ""+reboque.getNrCapacidadeKg().intValue());
        veicReboque.setCapM3(reboque.getNrCapacidadeM3() == null ? null : ""+reboque.getNrCapacidadeM3().intValue());
        veicReboque.setRENAVAM(reboque.getMeioTransporteRodoviario().getCdRenavam() == null ? null : MdfeConverterUtils.formatRENAVAM(reboque.getMeioTransporteRodoviario().getCdRenavam().toString(), 9));

        //Prop
        if (mdfe.getControleCarga().getMeioTransporteByIdTransportado() != null 
        		&& mdfe.getControleCarga().getMeioTransporteByIdTransportado().getTpVinculo() != null
        		&& !"P".equals(mdfe.getControleCarga().getMeioTransporteByIdTransportado().getTpVinculo().getValue())) {
        	veicReboque.setProp(new PropConverter(mdfe, retiraZeroInicialIe).convert());
        }
        
        //TpRod
        veicReboque.setTpCar(TpCarType.fromValue(MdfeConverterUtils.getTpCar(mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado())));
        
       	veicReboque.setUF(MdfeConverterUtils.get100aUF(mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado()));
        
        return new VeicReboque[] {veicReboque};
    }
    
}
