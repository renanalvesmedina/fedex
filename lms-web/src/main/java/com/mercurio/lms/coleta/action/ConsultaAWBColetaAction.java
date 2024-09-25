package com.mercurio.lms.coleta.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.util.AwbUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.coleta.AWBColetaListAction"
 */

public class ConsultaAWBColetaAction  {
	
	private DetalheColetaService detalheColetaService;
	private AwbService awbService;

	public Integer getRowCountAwbs(TypedFlatMap criteria) {    	
    	return this.findAwbsByIdPedidoColeta(criteria.getLong("idPedidoColeta")).size();
    }
    
	public ResultSetPage<Map<String, Object>> findPaginatedAwbs(TypedFlatMap criteria) {
    	
		List<Map<String, Object>> listAwbs = findAwbsByIdPedidoColeta(criteria.getLong("idPedidoColeta"));
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		
		int sizeQuery = listAwbs.size();

		int endPos = findDefinition.getPageSize().intValue()
				* findDefinition.getCurrentPage().intValue();

		int initPos = findDefinition.getPageSize().intValue()
				* (findDefinition.getCurrentPage().intValue() - 1);

		List subList = listAwbs.subList(initPos,
				(endPos > sizeQuery) ? sizeQuery : endPos);

		return new ResultSetPage(findDefinition.getCurrentPage(), findDefinition
				.getCurrentPage().intValue() > 1, sizeQuery > endPos, subList);
    }
    
    private List<Map<String, Object>> findAwbsByIdPedidoColeta(Long idPedidoColeta) {
    	List<DetalheColeta> detalhes = detalheColetaService.findDetalheColeta(idPedidoColeta);
		
    	List<Map<String, Object>> listAwbs = new ArrayList<Map<String,Object>>();
		
		for (DetalheColeta detalheColeta : detalhes) {
			if(detalheColeta.getDoctoServico() != null){
				final Awb awb = awbService.findUltimoAwbByDoctoServico(detalheColeta.getDoctoServico().getIdDoctoServico());
				
				Boolean exists = 0 < CollectionUtils.countMatches(listAwbs, new Predicate() {					
					@Override
					public boolean evaluate(Object obj) {
						Map<String, Object> map = (Map<String, Object>)obj;
						return ((Long)map.get("idAwb")).equals(awb.getIdAwb());
					}
				});
				
				if(!exists){
					Map<String, Object> mapAWB = new HashMap<String, Object>();
					mapAWB.put("idAwb", awb.getIdAwb());
					mapAWB.put("nrAwb", AwbUtils.getSgEmpresaAndNrAwbFormated(awb));
					listAwbs.add(mapAWB);
				}
			}
		}
		
		Collections.sort(listAwbs, new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> map0, Map<String, Object> map1) {
				return map0.get("nrAwb").toString().compareTo(map1.get("nrAwb").toString());
			}
		});
		
		return listAwbs;
	}

	public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
		this.detalheColetaService = detalheColetaService;
	}

	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

}
