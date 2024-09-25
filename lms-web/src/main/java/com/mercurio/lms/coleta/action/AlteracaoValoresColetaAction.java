package com.mercurio.lms.coleta.action;

import java.io.Serializable;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.alteracaoValoresColetaAction"
 */

public class AlteracaoValoresColetaAction  {
	
	private PedidoColetaService pedidoColetaService;

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}


    public TypedFlatMap findById(java.lang.Long id) {
    	PedidoColeta pedidoColeta = getPedidoColetaService().findById(id);

    	TypedFlatMap mapPedidoColeta = new TypedFlatMap(); 
    	mapPedidoColeta.put("idPedidoColeta", pedidoColeta.getIdPedidoColeta());

    	String sgFilial = null;
    	if (pedidoColeta.getFilialByIdFilialResponsavel()!=null) {
    		 sgFilial = pedidoColeta.getFilialByIdFilialResponsavel().getSgFilial();
    	}
    	mapPedidoColeta.put("filialByIdFilialResponsavel.sgFilial",sgFilial);
    	mapPedidoColeta.put("nrColeta", pedidoColeta.getNrColeta());
    	mapPedidoColeta.put("qtTotalVolumesInformado", pedidoColeta.getQtTotalVolumesInformado());
    	mapPedidoColeta.put("vlTotalInformado", pedidoColeta.getVlTotalInformado());
    	mapPedidoColeta.put("psTotalInformado", pedidoColeta.getPsTotalInformado());
    	mapPedidoColeta.put("qtTotalVolumesVerificado", pedidoColeta.getQtTotalVolumesVerificado());
    	mapPedidoColeta.put("vlTotalVerificado", pedidoColeta.getVlTotalVerificado());
    	mapPedidoColeta.put("psTotalVerificado", pedidoColeta.getPsTotalVerificado());

    	if (pedidoColeta.getMoeda()!=null) {
    		mapPedidoColeta.put("siglaSimboloInformado", pedidoColeta.getMoeda().getSiglaSimbolo());
    		mapPedidoColeta.put("siglaSimboloVerificado", pedidoColeta.getMoeda().getSiglaSimbolo());
    	}
    	return mapPedidoColeta;
    }


    public Serializable store(PedidoColeta pedidoColeta) {
    	return getPedidoColetaService().storeAlteracaoValoresColeta(pedidoColeta);
    }
}