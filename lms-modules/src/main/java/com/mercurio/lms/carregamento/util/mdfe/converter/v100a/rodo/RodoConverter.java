package com.mercurio.lms.carregamento.util.mdfe.converter.v100a.rodo;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.Rodo;
import com.mercurio.lms.util.FormatUtils;

public class RodoConverter {
    
    private ManifestoEletronico mdfe;
    
    private String retiraZeroInicialIe;
    
    private Proprietario proprietarioFilialOrigem;
    
    private final boolean utilizarTagVeicTracao;
    
    private CIOT ciot;
    
    public RodoConverter(ManifestoEletronico mdfe,
            Proprietario proprietarioFilialOrigem, boolean utilizarTagVeicTracao, String retiraZeroInicialIe, CIOT ciot) {
        super();
        this.mdfe = mdfe;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.utilizarTagVeicTracao = utilizarTagVeicTracao;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
        this.ciot = ciot;
    }



    public Rodo convert() {
        Rodo rodo = new Rodo();
        
        rodo.setRNTRC(FormatUtils.fillNumberWithZero(proprietarioFilialOrigem.getNrAntt() == null ? null : proprietarioFilialOrigem.getNrAntt().toString(),8));

        if(ciot != null && ciot.getNrCIOT() != null){
        	rodo.setCIOT( new String[] {FormatUtils.fillNumberWithZero(ciot.getNrCIOT().toString(), 12)});
        }
        
        if(utilizarTagVeicTracao){
        	rodo.setVeicTracao(new VeicTracaoConverter(mdfe, retiraZeroInicialIe).convert());
        }
        
        
        //VeicReboque
        if(mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado() != null){
        	rodo.setVeicReboque(new VeicReboqueConverter(mdfe, retiraZeroInicialIe).convert());
        }
        
        return rodo;
    }
}
