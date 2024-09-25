package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.ProdPred;
import com.mercurio.lms.mdfe.model.v300.types.TpCargaType;

public class ProdPredConverter {

	private ManifestoEletronico mdfe;
    private String dsProdutoPredominante;
    private String cepCarregadoMDFE;
    private String cepDescarregadoMDFE;
    
    public ProdPredConverter(ManifestoEletronico mdfe, String dsProdutoPredominante, String cepCarregadoMDFE, String cepDescarregadoMDFE) {
        super();
        this.mdfe = mdfe;
        this.dsProdutoPredominante = dsProdutoPredominante;
        this.cepCarregadoMDFE = cepCarregadoMDFE;
        this.cepDescarregadoMDFE = cepDescarregadoMDFE;
    }

    public ProdPred convert() {
    	ProdPred prodPred = new ProdPred();
    	
    	prodPred.setTpCarga(TpCargaType.VALUE_5);
    	prodPred.setXProd(dsProdutoPredominante);
    	if(mdfe.getConhecimentos().size() < 2){
    		prodPred.setInfLotacao(new InfLotacaoConverter(cepCarregadoMDFE, cepDescarregadoMDFE).convert());
    	}

        return prodPred;
    }

}
