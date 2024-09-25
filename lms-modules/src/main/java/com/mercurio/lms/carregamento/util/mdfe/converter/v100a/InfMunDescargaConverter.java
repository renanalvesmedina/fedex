package com.mercurio.lms.carregamento.util.mdfe.converter.v100a;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.mdfe.model.v100a.InfMunDescarga;
import com.mercurio.lms.municipios.model.Filial;

public class InfMunDescargaConverter {

    private ManifestoEletronico mdfe;
    
    
    private List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes;
    
    public InfMunDescargaConverter(ManifestoEletronico mdfe,
            List<MonitoramentoDocEletronico> monitoramentoDocEltronicoCtes) {
        super();
        this.mdfe = mdfe;
        this.monitoramentoDocEltronicoCtes = monitoramentoDocEltronicoCtes;
    }

    public InfMunDescarga[] convertViagem() {
    	List<InfMunDescarga> toReturn = new ArrayList<InfMunDescarga>();
        
        Filial filial = MdfeConverterUtils.getFilialProximoDestinoControleCarga(mdfe.getControleCarga());
        
        InfMunDescarga infMunDescarga = new InfMunDescarga();
    	toReturn.add(infMunDescarga);
        infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(filial.getPessoa().getEnderecoPessoa().getMunicipio()));
        infMunDescarga.setXMunDescarga(MdfeConverterUtils.tratarString(filial.getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 60));

        for (Conhecimento conhecimento: mdfe.getConhecimentos()) {
    		if ("CTE".equals(conhecimento.getTpDocumentoServico().getValue())) {
    			infMunDescarga.addInfCTe(new InfCteConverter(conhecimento, monitoramentoDocEltronicoCtes).convert());
    		} 
    	}
        
        return toReturn.toArray(new InfMunDescarga[] {});
        
    }
    
    public InfMunDescarga[] convert(boolean isViagem, boolean isAgrupaPorUFDestino) {
    	List<InfMunDescarga> toReturn = new ArrayList<InfMunDescarga>();        
        
		InfMunDescarga infMunDescarga = new InfMunDescarga();
		
		if(!isViagem && isAgrupaPorUFDestino && mdfe.getConhecimentos() != null && ! mdfe.getConhecimentos().isEmpty()){
			infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(mdfe.getConhecimentos().get(0).getMunicipioByIdMunicipioEntrega()));
			infMunDescarga.setXMunDescarga(MdfeConverterUtils.tratarString(mdfe.getConhecimentos().get(0).getMunicipioByIdMunicipioEntrega().getNmMunicipio(), 60));
		}else{
			infMunDescarga.setCMunDescarga(MdfeConverterUtils.formatCMun(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio()));
			infMunDescarga.setXMunDescarga(MdfeConverterUtils.tratarString(mdfe.getFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 60));
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
