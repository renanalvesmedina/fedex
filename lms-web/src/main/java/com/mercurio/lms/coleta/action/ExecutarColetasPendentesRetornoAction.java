package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.OcorrenciaColeta;
import com.mercurio.lms.coleta.model.service.OcorrenciaColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.executarColetasPendentesRetornoAction"
 */

public class ExecutarColetasPendentesRetornoAction {
	
	private MeioTransporteService meioTransporteService;
	private OcorrenciaColetaService ocorrenciaColetaService;
	private PedidoColetaService pedidoColetaService;


	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setOcorrenciaColetaService(OcorrenciaColetaService ocorrenciaColetaService) {
		this.ocorrenciaColetaService = ocorrenciaColetaService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public List findOcorrenciaColeta(Map criteria) {
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsDescricaoCompleta:asc");
    	List list = ocorrenciaColetaService.findListByCriteria(criteria, campoOrdenacao);
    	List listaRetorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		OcorrenciaColeta oc = (OcorrenciaColeta)iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idOcorrenciaColeta", oc.getIdOcorrenciaColeta());
    		tfm.put("dsDescricaoCompleta", oc.getDsDescricaoCompleta());
    		tfm.put("blIneficienciaFrota", oc.getBlIneficienciaFrota());
    		listaRetorno.add(tfm);
    	}
        return listaRetorno;
    }
    
	public List findLookupMeioTransporte(Map criteria) {
    	List list = meioTransporteService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		MeioTransporte meioTransporte = (MeioTransporte)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idMeioTransporte", meioTransporte.getIdMeioTransporte());
    		typedFlatMap.put("nrIdentificador", meioTransporte.getNrIdentificador());
    		typedFlatMap.put("nrFrota", meioTransporte.getNrFrota());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
	
    public void generateRetornarColeta(TypedFlatMap criteria) {
    	pedidoColetaService.generateRetornarColeta(criteria);
    }
}