package com.mercurio.lms.carregamento.util.mdfe.converter.v300.rodo;

import java.util.List;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.mdfe.model.v300.Rodo;

public class RodoConverter {
    
    private ManifestoEletronico mdfe;
    private String retiraZeroInicialIe;
    private final boolean utilizarTagVeicTracao;
    private Proprietario proprietarioFilialOrigem;
    private CIOT ciot;
    private String cnpjCiot;
    private List<InfContratante> infContratanteList;
    
    public RodoConverter(ManifestoEletronico mdfe,
            Proprietario proprietarioFilialOrigem, boolean utilizarTagVeicTracao, String retiraZeroInicialIe, CIOT ciot, String cnpjCiot, List<InfContratante> infContratanteList) {
        super();
        this.mdfe = mdfe;
        this.utilizarTagVeicTracao = utilizarTagVeicTracao;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.ciot = ciot;
        this.cnpjCiot = cnpjCiot;
        this.infContratanteList = infContratanteList;
    }



    public Rodo convert() {
        Rodo rodo = new Rodo();
        
        rodo.setInfANTT(new ANTTConverter(proprietarioFilialOrigem, ciot, cnpjCiot, infContratanteList).convert());
        
        if(utilizarTagVeicTracao){
        	rodo.setVeicTracao(new VeicTracaoConverter(mdfe, retiraZeroInicialIe).convert());
        }else{
        	//rodo.setVeicPrincipal(new VeicPrincipalConverter(mdfe).convert());
        }
        
        //VeicReboque
        if(mdfe.getControleCarga().getMeioTransporteByIdSemiRebocado() != null){
        	rodo.setVeicReboque(new VeicReboqueConverter(mdfe, retiraZeroInicialIe).convert());
        }
        
        return rodo;
    }
}
