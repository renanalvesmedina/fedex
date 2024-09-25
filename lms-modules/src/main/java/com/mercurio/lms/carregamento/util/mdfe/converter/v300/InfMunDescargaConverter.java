package com.mercurio.lms.carregamento.util.mdfe.converter.v300;

import java.util.List;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v300.InfMunDescarga;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;

public class InfMunDescargaConverter {

	private static final int ZERO = 0;
	
    private ManifestoEletronico mdfe;
        
    private List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    public InfMunDescargaConverter(ManifestoEletronico mdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes) {
        super();
        this.mdfe = mdfe;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
    }

    public InfMunDescarga[] convertViagem() {
        
        Filial filialDestino = MdfeConverterUtils.getFilialDestinoByManifesto(mdfe);
        
        InfMunDescarga infMunDescarga = new InfMunDescarga();
        infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(filialDestino.getPessoa().getEnderecoPessoa().getMunicipio()));
        infMunDescarga.setXMunDescarga(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(filialDestino.getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 60)));

        for (Conhecimento conhecimento: mdfe.getConhecimentos()) {
    		if ("CTE".equals(conhecimento.getTpDocumentoServico().getValue())) {
    			infMunDescarga.addInfCTe(new InfCteConverter(conhecimento, monitoramentoDocEltronicoCtes).convert());
    		} 
    	}
        
        return new InfMunDescarga[]{infMunDescarga};
        
    }
    
    public InfMunDescarga[] convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
        
		InfMunDescarga infMunDescarga = new InfMunDescarga();
		
		if(!isViagem && isAgrupaPorUFDestino && mdfe.getConhecimentos() != null && ! mdfe.getConhecimentos().isEmpty()){
			infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(mdfe.getConhecimentos().get(ZERO).getMunicipioByIdMunicipioEntrega()));
			infMunDescarga.setXMunDescarga(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(mdfe.getConhecimentos().get(0).getMunicipioByIdMunicipioEntrega().getNmMunicipio(), 60)));
		}else{
			infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio()));
			infMunDescarga.setXMunDescarga(FormatUtils.removeAccents(MdfeConverterUtils.tratarString(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 60)));
		}
	    
	    List<Conhecimento> conhecimentos = mdfe.getConhecimentos();
	    for (Conhecimento conhecimento: conhecimentos) {
			
			if ("CTE".equals(conhecimento.getTpDocumentoServico().getValue())) {
				infMunDescarga.addInfCTe(new InfCteConverter(conhecimento, monitoramentoDocEltronicoCtes).convert());
			} 
		}
        
	    return new InfMunDescarga[]{infMunDescarga};
        
    }
}
