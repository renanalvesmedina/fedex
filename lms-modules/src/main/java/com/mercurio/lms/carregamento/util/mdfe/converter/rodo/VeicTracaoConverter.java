package com.mercurio.lms.carregamento.util.mdfe.converter.rodo;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.VeicPrincipal;
import com.mercurio.lms.mdfe.model.v100.VeicTracao;

public class VeicTracaoConverter {
    
    private ManifestoEletronico mdfe;
    
    public VeicTracaoConverter(ManifestoEletronico mdfe) {
        super();
        this.mdfe = mdfe;
    }

    public VeicTracao convert() {
        VeicTracao veicTracao = new VeicTracao();
        
        MeioTransporte veiculo = mdfe.getControleCarga().getMeioTransporteByIdTransportado();
        veicTracao.setCInt(MdfeConverterUtils.tratarString(veiculo.getNrFrota(),10));
        veicTracao.setPlaca(MdfeConverterUtils.tratarString(veiculo.getNrIdentificador(),7));
        veicTracao.setTara(veiculo.getNrCapacidadeKg() == null ? null : ""+veiculo.getNrCapacidadeKg().intValue());
        veicTracao.setCapKG(veiculo.getNrCapacidadeKg() == null ? null : ""+veiculo.getNrCapacidadeKg().intValue());
        veicTracao.setCapM3(veiculo.getNrCapacidadeM3() == null ? null : ""+veiculo.getNrCapacidadeM3().intValue());
        
        //Prop
        veicTracao.setProp(new PropConverter(mdfe).convert());
        
        //Condutor
        veicTracao.setCondutor(new CondutorConverter(mdfe).convert());
        
        return veicTracao;
    }
    
}
