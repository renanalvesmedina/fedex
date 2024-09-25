package com.mercurio.lms.carregamento.util.mdfe.converter.rodo;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.VeicPrincipal;

public class VeicPrincipalConverter {
    
    private ManifestoEletronico mdfe;
    
    public VeicPrincipalConverter(ManifestoEletronico mdfe) {
        super();
        this.mdfe = mdfe;
    }

    public VeicPrincipal convert() {
        VeicPrincipal veicPrincipal = new VeicPrincipal();
        
        MeioTransporte veiculo = mdfe.getControleCarga().getMeioTransporteByIdTransportado();
        veicPrincipal.setCInt(MdfeConverterUtils.tratarString(veiculo.getNrFrota(),10));
        veicPrincipal.setPlaca(MdfeConverterUtils.tratarString(veiculo.getNrIdentificador(),7));
        veicPrincipal.setTara(veiculo.getNrCapacidadeKg() == null ? null : ""+veiculo.getNrCapacidadeKg().intValue());
        veicPrincipal.setCapKG(veiculo.getNrCapacidadeKg() == null ? null : ""+veiculo.getNrCapacidadeKg().intValue());
        veicPrincipal.setCapM3(veiculo.getNrCapacidadeM3() == null ? null : ""+veiculo.getNrCapacidadeM3().intValue());
        
        //Prop
        veicPrincipal.setProp(new PropConverter(mdfe).convert());
        
        //Condutor
        veicPrincipal.setCondutor(new CondutorConverter(mdfe).convert());
        
        return veicPrincipal;
    }
    
}
