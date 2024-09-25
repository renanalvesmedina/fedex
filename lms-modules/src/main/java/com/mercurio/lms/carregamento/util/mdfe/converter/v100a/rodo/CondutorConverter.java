package com.mercurio.lms.carregamento.util.mdfe.converter.v100a.rodo;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.Condutor;

public class CondutorConverter {
    
    private ManifestoEletronico mdfe;
    
    public CondutorConverter(ManifestoEletronico mdfe) {
        super();
        this.mdfe = mdfe;
    }

    public Condutor[] convert() {
        List<Condutor> toReturn = new ArrayList<Condutor>();
        
        Condutor condutor = new Condutor();
        toReturn.add(condutor);
        
        condutor.setXNome(MdfeConverterUtils.tratarString(mdfe.getControleCarga().getMotorista().getPessoa().getNmPessoa(), 60));
        condutor.setCPF(mdfe.getControleCarga().getMotorista().getPessoa().getNrIdentificacao());
        
        return toReturn.toArray(new Condutor[]{});
    }

}
