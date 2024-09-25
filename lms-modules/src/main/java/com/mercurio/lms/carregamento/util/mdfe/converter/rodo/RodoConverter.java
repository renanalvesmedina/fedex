package com.mercurio.lms.carregamento.util.mdfe.converter.rodo;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100.Rodo;
import com.mercurio.lms.util.FormatUtils;

public class RodoConverter {
    
    private ManifestoEletronico mdfe;
    
    private Proprietario proprietarioFilialOrigem;
    
    private final boolean utilizarTagVeicTracao;
    
    public RodoConverter(ManifestoEletronico mdfe,
            Proprietario proprietarioFilialOrigem, boolean utilizarTagVeicTracao) {
        super();
        this.mdfe = mdfe;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.utilizarTagVeicTracao = utilizarTagVeicTracao;
    }



    public Rodo convert() {
        Rodo rodo = new Rodo();
        
        rodo.setRNTRC(FormatUtils.fillNumberWithZero(proprietarioFilialOrigem.getNrAntt() == null ? null : proprietarioFilialOrigem.getNrAntt().toString(),8));

        if(utilizarTagVeicTracao){
        	rodo.setVeicTracao(new VeicTracaoConverter(mdfe).convert());
        }else{
        	rodo.setVeicPrincipal(new VeicPrincipalConverter(mdfe).convert());
        }
        
        
        //VeicReboque
        if(mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado() != null){
        	rodo.setVeicReboque(new VeicReboqueConverter(mdfe).convert());
        }
        
        return rodo;
    }
}
