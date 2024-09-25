package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v300.Tot;
import com.mercurio.lms.mdfe.model.v300.types.CUnidType;

public class TotConverter {
    
    private ManifestoEletronico mdfe;
    
    public TotConverter(ManifestoEletronico mdfe) {
        super();
        this.mdfe = mdfe;
    }

    public Tot convert() {
        Tot tot = new Tot();
        
        int qCTe = 0;
        BigDecimal vCarga = BigDecimal.ZERO;
        BigDecimal qCarga = BigDecimal.ZERO;
        
        List<Conhecimento> conhecimentos = mdfe.getConhecimentos();
        
        for (Conhecimento conhecimento: conhecimentos) {
            if ("CTE".equals(conhecimento.getTpDocumentoServico().getValue())) {
                qCTe++;
            }
            
            vCarga = vCarga.add(conhecimento.getVlMercadoria());
            if (conhecimento.getPsReal() != null) {
                qCarga = qCarga.add(conhecimento.getPsReal());
            }
        }
        
        if (qCTe > 0) {
            tot.setQCTe(""+qCTe);
        }
        tot.setVCarga(MdfeConverterUtils.formatDoisDecimais(vCarga));
        tot.setCUnid(CUnidType.VALUE_1);
        tot.setQCarga(MdfeConverterUtils.formatQuatroDecimais(qCarga));
        
        return tot;
    }

}
