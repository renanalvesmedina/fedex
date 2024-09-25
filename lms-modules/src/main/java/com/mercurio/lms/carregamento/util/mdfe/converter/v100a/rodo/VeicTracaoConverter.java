package com.mercurio.lms.carregamento.util.mdfe.converter.v100a.rodo;

import com.mercurio.lms.carregamento.util.mdfe.MdfeConverterUtils;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.expedicao.model.ManifestoEletronico;
import com.mercurio.lms.mdfe.model.v100a.VeicTracao;
import com.mercurio.lms.mdfe.model.v100a.types.TpCarType;
import com.mercurio.lms.mdfe.model.v100a.types.TpRodType;

public class VeicTracaoConverter {
    
    private ManifestoEletronico mdfe;
    private String retiraZeroInicialIe;
    
    public VeicTracaoConverter(ManifestoEletronico mdfe, String retiraZeroInicialIe) {
        super();
        this.mdfe = mdfe;
        this.retiraZeroInicialIe = retiraZeroInicialIe;
    }

    public VeicTracao convert() {
        VeicTracao veicTracao = new VeicTracao();
        
        MeioTransporte veiculo = mdfe.getControleCarga().getMeioTransporteByIdTransportado();
        veicTracao.setCInt(MdfeConverterUtils.tratarString(veiculo.getNrFrota(),10));
        veicTracao.setPlaca(MdfeConverterUtils.tratarString(veiculo.getNrIdentificador(),7));
        veicTracao.setTara(veiculo.getNrCapacidadeKg() == null ? null : ""+veiculo.getNrCapacidadeKg().intValue());
        veicTracao.setCapKG(veiculo.getNrCapacidadeKg() == null ? null : ""+veiculo.getNrCapacidadeKg().intValue());
        veicTracao.setCapM3(veiculo.getNrCapacidadeM3() == null ? null : ""+veiculo.getNrCapacidadeM3().intValue());
        veicTracao.setRENAVAM(veiculo.getMeioTransporteRodoviario().getCdRenavam() == null ? null : MdfeConverterUtils.formatRENAVAM(veiculo.getMeioTransporteRodoviario().getCdRenavam().toString(), 9));
        
        //Prop
        if (!"P".equals(veiculo.getTpVinculo().getValue())) {
        	veicTracao.setProp(new PropConverter(mdfe, retiraZeroInicialIe).convert());
        }
        
        //Condutor
        veicTracao.setCondutor(new CondutorConverter(mdfe).convert());
        
        //tpRod
        veicTracao.setTpRod(TpRodType.fromValue(getTpRodado()));
        
        //tpCar
        veicTracao.setTpCar(TpCarType.fromValue(MdfeConverterUtils.getTpCar(mdfe.getControleCarga().getMeioTransporteByIdTransportado())));
        
        //UF
        veicTracao.setUF(MdfeConverterUtils.get100aUF((mdfe.getControleCarga().getMeioTransporteByIdTransportado())));
        
        return veicTracao;
    }
    
	/**
	 * Para V.ID_MEIO_TRANSPORTE = CC.ID_TRANSPORTADO e
	 * V.ID_MODELO_MEIO_TRANSPORTE = MMT.ID_MODELO_MEIO_TRANSPORTE e
	 * MMT.ID_TIPO_MEIO_TRANSPORTE = TMT.ID_TIPO_MEIO_TRANSPORTE
	 * 
	 * "03" se TMT.DS_TIPO_MEIO_TRANSPORTE = "Cavalo-Trator"
	 * "04" se TMT.DS_TIPO_MEIO_TRANSPORTE = "Van"         
	 * "01" se TMT.DS_TIPO_MEIO_TRANSPORTE = "Truck"        
	 * "02" se TMT.DS_TIPO_MEIO_TRANSPORTE = "Toco"         
	 * "02" se TMT.DS_TIPO_MEIO_TRANSPORTE = "Caminhão 3/4" 
	 * "05" se TMT.DS_TIPO_MEIO_TRANSPORTE = "Utilitário"   
	 * "06" para as demais situções
	 * 
	 * @return
	 */
	private String getTpRodado() {
		String tpRodado = "06";

		if (mdfe.getControleCarga() != null 
				&& mdfe.getControleCarga().getMeioTransporteByIdTransportado() != null
				&& mdfe.getControleCarga().getMeioTransporteByIdTransportado().getModeloMeioTransporte() != null
				&& mdfe.getControleCarga().getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte() != null) {
			
			String dsTpMeioTransporte = mdfe.getControleCarga().getMeioTransporteByIdTransportado().getModeloMeioTransporte().getTipoMeioTransporte()
					.getDsTipoMeioTransporte();

			if ("Cavalo-Trator".equals(dsTpMeioTransporte)) {
				tpRodado = "03";
			} else if ("Van".equals(dsTpMeioTransporte)) {
				tpRodado = "04";
			} else if ("Truck".equals(dsTpMeioTransporte)) {
				tpRodado = "01";
			} else if ("Toco".equals(dsTpMeioTransporte)) {
				tpRodado = "02";
			} else if ("Caminhão 3/4".equals(dsTpMeioTransporte)) {
				tpRodado = "02";
			} else if ("Utilitário".equals(dsTpMeioTransporte)) {
				tpRodado = "05";
			} else {
				tpRodado = "06";
			}
		}

		return tpRodado;
	}
	
}
