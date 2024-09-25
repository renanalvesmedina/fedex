package com.mercurio.lms.carregamento.util.mdfe.converter.v300.rodo;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.CIOT;
import com.mercurio.lms.mdfe.model.v300.InfANTT;
import com.mercurio.lms.mdfe.model.v300.InfContratante;
import com.mercurio.lms.util.FormatUtils;

public class ANTTConverter {
    
	private static final int NUMERO_CASAS = 8;
	
    private Proprietario proprietarioFilialOrigem;
    private CIOT ciot;
    private String cnpjCiot;
    private List<InfContratante> infContratanteList;
    
    public ANTTConverter(Proprietario proprietarioFilialOrigem, CIOT ciot, String cnpjCiot, List<InfContratante> infContratanteList) {
        super();
        this.ciot = ciot;
        this.proprietarioFilialOrigem = proprietarioFilialOrigem;
        this.cnpjCiot = cnpjCiot;
        this.infContratanteList = infContratanteList;
    }

    public InfANTT convert() {
    	InfANTT infANTT = new InfANTT();
    	
    	infANTT.setRNTRC(FormatUtils.fillNumberWithZero(proprietarioFilialOrigem.getNrAntt() == null ? null : proprietarioFilialOrigem.getNrAntt().toString(), NUMERO_CASAS));

        if(ciot != null && ciot.getNrCIOT() != null){
        	infANTT.setInfCIOT(new CIOTConverter(ciot, cnpjCiot).convert());
        }
        
        if(CollectionUtils.isNotEmpty(infContratanteList)) {
        	infANTT.setInfContratante(new InfContratanteConverter(infContratanteList).convert());
        }
        
        return infANTT;
    }
}
