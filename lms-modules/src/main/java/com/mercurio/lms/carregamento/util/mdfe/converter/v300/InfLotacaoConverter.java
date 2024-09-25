package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import com.mercurio.lms.mdfe.model.v300.InfLocalCarrega;
import com.mercurio.lms.mdfe.model.v300.InfLocalDescarrega;
import com.mercurio.lms.mdfe.model.v300.InfLotacao;

public class InfLotacaoConverter {

    private String cepCarregadoMDFE;
    private String cepDescarregadoMDFE;
    
    
    public InfLotacaoConverter(String cepCarregadoMDFE, String cepDescarregadoMDFE) {
        super();
        this.cepCarregadoMDFE = cepCarregadoMDFE;
        this.cepDescarregadoMDFE = cepDescarregadoMDFE;
    }

    public InfLotacao convert() {
    	InfLotacao infLotacao = new InfLotacao();
    	
    	InfLocalCarrega infLocalCarrega = new InfLocalCarrega();
    	infLocalCarrega.setCEP(cepCarregadoMDFE);
    	infLotacao.setInfLocalCarrega(infLocalCarrega);
    	
    	InfLocalDescarrega infLocalDescCarrega = new InfLocalDescarrega();
    	infLocalDescCarrega.setCEP(cepDescarregadoMDFE);
    	infLotacao.setInfLocalDescarrega(infLocalDescCarrega);
    	
        return infLotacao;
    }

}
