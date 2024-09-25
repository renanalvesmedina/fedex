package com.mercurio.lms.coleta.action;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.util.FormatUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.programacaoColetasVeiculosDadosVeiculoAction"
 */

public class ProgramacaoColetasVeiculosDadosVeiculoAction {
	
	private ControleCargaService controleCargaService;

    public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public TypedFlatMap findById(java.lang.Long id) {
    	ControleCarga cc = getControleCargaService().findById(id);
    	MeioTransporte meioTransporteTransportado = cc.getMeioTransporteByIdTransportado();
    	Moeda moeda = cc.getMoeda();
    	TypedFlatMap map = new TypedFlatMap();
    	map.put("idControleCarga", cc.getIdControleCarga());
    	map.put("nrControleCarga", cc.getNrControleCarga());
    	map.put("pcOcupacaoInformado", cc.getPcOcupacaoInformado());
    	map.put("vlTotalFrota", cc.getVlTotalFrota());
    	map.put("vlColetado", cc.getVlColetado());
    	map.put("vlAColetar", cc.getVlAColetar());
    	map.put("vlAEntregar", cc.getVlAEntregar());
    	map.put("vlEntregue", cc.getVlEntregue());
    	map.put("psTotalFrota", cc.getPsTotalFrota());
    	map.put("psColetado", cc.getPsColetado());
    	map.put("psAColetar", cc.getPsAColetar());
    	map.put("psAEntregar", cc.getPsAEntregar());
    	map.put("psEntregue", cc.getPsEntregue());
    	map.put("filialByIdFilialOrigem.sgFilial", cc.getFilialByIdFilialOrigem().getSgFilial());
    	if (moeda!=null){
    		String siglaMaisSimboloMoeda = FormatUtils.concatSiglaSimboloMoeda(moeda);
    		if (cc.getVlTotalFrota()!= null){
        		map.put("moedaVlTotalFrota", siglaMaisSimboloMoeda);
    		}
        	if (cc.getVlColetado()!= null){
        		map.put("moedaVlColetado", siglaMaisSimboloMoeda);
        	}
        	if (cc.getVlAColetar()!= null){
        		map.put("moedaVlAColetar", siglaMaisSimboloMoeda);
        	}
        	if (cc.getVlAEntregar()!= null){
        		map.put("moedaVlAEntregar", siglaMaisSimboloMoeda);
        	}
        	if (cc.getVlEntregue()!= null){
        		map.put("moedaVlEntregue", siglaMaisSimboloMoeda);
        	}
    	}
    	if (meioTransporteTransportado!=null){
    		map.put("meioTransporteByIdTransportado.nrFrota", meioTransporteTransportado.getNrFrota());
        	map.put("meioTransporteByIdTransportado.nrIdentificador", meioTransporteTransportado.getNrIdentificador());
        	map.put("meioTransporteByIdTransportado.nrCapacidadeKg", meioTransporteTransportado.getNrCapacidadeKg());
        	map.put("meioTransporteByIdTransportado.modeloMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte", meioTransporteTransportado.getModeloMeioTransporte().getTipoMeioTransporte().getDsTipoMeioTransporte());
    	}
    	return map;
    }
}