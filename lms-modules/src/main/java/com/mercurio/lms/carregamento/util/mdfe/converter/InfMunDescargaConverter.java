package com.mercurio.lms.carregamento.util.mdfe.converter;

import java.util.List;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100.InfMunDescarga;

public class InfMunDescargaConverter {

    private ManifestoEletronico mdfe;
    
    private List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    public InfMunDescargaConverter(ManifestoEletronico mdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes) {
        super();
        this.mdfe = mdfe;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
    }

    public InfMunDescarga[] convert() {

        InfMunDescarga infMunDescarga = new InfMunDescarga();
        infMunDescarga = new InfMunDescarga();
        infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(mdfe.getFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio()));
        infMunDescarga.setXMunDescarga(MdfeConverterUtils.tratarString(mdfe.getFilialDestino().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 60));
        
        List<Conhecimento> conhecimentos = mdfe.getConhecimentos();
        
        for (Conhecimento conhecimento: conhecimentos) {

            if ("CTE".equals(conhecimento.getTpDocumentoServico().getValue())) {
                infMunDescarga.addInfCTe(new InfCteConverter(conhecimento, monitoramentoDocEltronicoCtes).convert());
            } else {
                infMunDescarga.addInfCT(new InfCtConverter(conhecimento).convert());
            }
        }
        
        return new InfMunDescarga[]{infMunDescarga};
        
    }
    
}
